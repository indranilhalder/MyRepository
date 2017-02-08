/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.checkout.form.DeliveryMethodEntry;
import com.tisl.mpl.checkout.form.DeliveryMethodForm;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.checkout.storelocator.MplStoreLocatorFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.MplSlaveMasterFacade;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.data.ATSResponseData;
import com.tisl.mpl.facades.data.FreebieProduct;
import com.tisl.mpl.facades.data.ProudctWithPointOfServicesData;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.sellerinfo.facades.MplSellerInformationFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.util.CSRFTokenManager;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


@Controller
@RequestMapping(value = "/checkout/multi/delivery-method")
public class DeliveryMethodCheckoutStepController extends AbstractCheckoutStepController
{

	@Resource
	private MplCheckoutFacade mplCheckoutFacade;

	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = ModelAttributetConstants.ACCELERATOR_CHECKOUT_FACADE)
	private CheckoutFacade checkoutFacade;

	@Autowired
	private MplAccountAddressFacade accountAddressFacade;

	@Autowired
	private MplAddressValidator mplAddressValidator;

	@Autowired
	private CartService cartService;

	@Autowired
	private CommerceCartService commerceCartService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	//Unused --- commented
	//@Resource(name = "productService")
	//private ProductService productService;

	//@Autowired
	//private Converter<CartModel, CartData> mplExtendedCartConverter;

	//TISPT-104
	@Autowired
	private Converter<CartModel, CartData> mplExtendedPromoCartConverter;


	@Autowired
	private MplCouponFacade mplCouponFacade;

	private final String checkoutPageName = "Choose Your Delivery Options";

	private final String checkoutPageName1 = "New Address";
	private final String selectAddress = "Select Address";

	private static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";

	@Autowired
	private MplSellerInformationFacade mplSellerInformationFacade;

	//Commented as unused -- PMD
	//@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	//private SessionService sessionService;

	//@Resource(name = "pinCodeFacade")
	//private PinCodeServiceAvilabilityFacade pinCodeFacade;

	//@Resource(name = "productDetailsHelper")
	//private ProductDetailsHelper productDetailsHelper;

	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "userService")
	private UserService userService;


	@Resource(name = "mplStoreLocatorFacade")
	private MplStoreLocatorFacade mplStoreLocatorFacade;

	@Resource(name = "mplSlaveMasterFacade")
	private MplSlaveMasterFacade mplSlaveMasterFacade;

	@Resource(name = "pointOfServiceConverter")
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	@Autowired
	private MplConfigFacade mplConfigFacade;


	private static final Logger LOG = Logger.getLogger(DeliveryMethodCheckoutStepController.class);

	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHOOSEVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = MarketplacecheckoutaddonConstants.DELIVERY_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes)
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<PinCodeResponseData> responseData = null;
		String returnPage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				return getCheckoutStep().previousStep();
			}
			final CartModel serviceCart = getCartService().getSessionCart();
			setExpressCheckout(serviceCart);

			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(serviceCart); //TISPT-104
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			//TISBOX-1618
			//final CartModel cartModel = getCartService().getSessionCart();
			getMplCouponFacade().releaseVoucherInCheckout(serviceCart);
			getMplCartFacade().removeDeliveryMode(serviceCart); //TISPT-104 // Cart recalculation method invoked inside this method
			//applyPromotions();


			final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);

			timeOutSet(model);
			if (StringUtils.isNotEmpty(defaultPinCodeId) && (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries())))
			{
				responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartData);
				// TPR-429 START
				final String cartLevelSellerID = populateCheckoutSellers(cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, cartLevelSellerID);
				// TPR-429 END

				//  TISPRD-1951  START //

				// Checking whether inventory is availbale or not
				// if inventory is not available for particular delivery Mode
				// then removing that deliveryMode in Choose DeliveryMode Page
				for (PinCodeResponseData pinCodeResponseData : responseData)
				{
					try
					{
						if (pinCodeResponseData != null && pinCodeResponseData.getIsServicable() != null
								&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
						{
							pinCodeResponseData = getMplCartFacade().getVlaidDeliveryModesByInventory(pinCodeResponseData);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception occured while checking inventory " + e.getCause());
					}
				}
				//  TISPRD-1951  END //
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartData, responseData, serviceCart);
				fullfillmentDataMap = getMplCartFacade().getFullfillmentMode(cartData);

				//TIS-397
				deliveryModeDataMap = mplCheckoutFacade.repopulateTshipDeliveryCost(deliveryModeDataMap, cartData);

				model.addAttribute("deliveryModeData", deliveryModeDataMap);
				model.addAttribute("deliveryMethodForm", new DeliveryMethodForm());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
				model.addAttribute("defaultPincode", defaultPinCodeId);

				//TISPRO-625

				final Boolean isExpressCheckoutSelected = (serviceCart != null && serviceCart.getDeliveryAddress() != null) ? Boolean.TRUE
						: Boolean.FALSE;
				model.addAttribute(MarketplacecheckoutaddonConstants.CART_EXPRESS_CHECKOUT_SELECTED, isExpressCheckoutSelected);

				this.prepareDataForPage(model);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(
						WebConstants.BREADCRUMBS_KEY,
						getResourceBreadcrumbBuilder().getBreadcrumbs(
								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				model.addAttribute("metaRobots", "noindex,nofollow");
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);

				GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
				model.addAttribute("checkoutPageName", checkoutPageName);
				model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629
				model.addAttribute("progressBarClass", "choosePage");
			}
			else
			{
				return MarketplacecommerceservicesConstants.REDIRECT + "/cart";
			}

			returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}

		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" CMSItemNotFoundException while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		return returnPage;
	}

	/**
	 * This method gets called when the "Use Selected Delivery Method" button is clicked. It sets the selected delivery
	 * mode on the checkout facade and reloads the page highlighting the selected delivery Mode.
	 *
	 * @param model
	 *           - the id of the delivery mode
	 * @param deliveryMethodForm
	 * @param bindingResult
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTURL)
	@RequireHardLogIn
	public String doSelectDeliveryMode(@ModelAttribute("deliveryMethodForm") DeliveryMethodForm deliveryMethodForm,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		//TISPT-400
		if (getUserFacade().isAnonymousUser())
		{
			return getCheckoutStep().previousStep();
		}

		String returnPage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			final CartModel cartModel = getCartService().getSessionCart();
			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cartModel);
			//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			//TISPT-400 ends

			Double finalDeliveryCost = Double.valueOf(0.0);

			//int count = 0;
			Boolean selectPickupDetails = Boolean.FALSE;

			if (deliveryMethodForm.getDeliveryMethodEntry() == null)
			{
				//if cart contains cnc and home/express delivery modes
				//count++;
				final HttpSession session = request.getSession();
				deliveryMethodForm = (DeliveryMethodForm) session.getAttribute("deliveryMethodForm");
				String pickupPersonName = null;
				String pickupPersonMobile = null;

				if (cartModel != null)
				{
					pickupPersonName = cartModel.getPickupPersonName();
					pickupPersonMobile = cartModel.getPickupPersonMobile();
				}
				if ((pickupPersonName == null) && (pickupPersonMobile == null))
				{
					selectPickupDetails = Boolean.TRUE;
					model.addAttribute("selectPickupDetails", selectPickupDetails);
					return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method"
							+ MarketplacecheckoutaddonConstants.MPLDELIVERYCHECKURL;
				}
			}
			else
			{
				//populate mplZone delivery mode
				//finalDeliveryCost = populateMplZoneDeliveryMode(deliveryMethodForm);
				//TISPT-400
				finalDeliveryCost = populateMplZoneDeliveryMode(deliveryMethodForm, cartModel);
				final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
				//final boolean calculationStatus =
				getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost, deliveryChargePromotionMap, cartModel); //TIS 400

			}
			//TISPT-400
			//			if (getUserFacade().isAnonymousUser())
			//			{
			//				return getCheckoutStep().previousStep();
			//			}
			//
			//			//TISST-13012
			//			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			//			if (cartItemDelistedStatus)
			//			{
			//				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			//			}
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

			applyPromotions();

			//populate freebie data
			populateFreebieProductData(cartModel, freebieModelMap, freebieParentQtyMap);

			//getMplCheckoutFacade().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap); //TISPT-169

			LOG.debug(">>>>>>>>>>  Step 2  :Freebie data preparation ");

			timeOutSet(model);

			/*** Inventory Soft Reservation Start ***/

			final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartModel);
			if (!inventoryReservationStatus)
			{
				getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			/*** Inventory Soft Reservation Start ***/

			LOG.debug(">>>>>>>>>>  Step 4:  Inventory soft reservation status  " + inventoryReservationStatus);
			Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
			List<PinCodeResponseData> responseData = null;
			final CartData cartUssidData = getMplCartFacade().getSessionCartWithEntryOrdering(true);

			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);


			if (StringUtils.isNotEmpty(defaultPinCodeId)
					&& (cartUssidData != null && cartUssidData.getEntries() != null && !cartUssidData.getEntries().isEmpty()))
			{
				responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartUssidData);
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartUssidData, responseData, cartModel);

				getMplCartFacade().setDeliveryDate(cartUssidData, responseData);
				//TISUTO-72 TISST-6994,TISST-6990 set cart COD eligible
				final boolean isCOdEligible = getMplCartFacade().addCartCodEligible(deliveryModeDataMap, responseData, cartModel);
				LOG.info("isCOdEligible " + isCOdEligible);
			}

			LOG.debug(">>>>>>>>>>  Step 5:  Cod status setting done  ");

			// TPR-429 START
			final String cartLevelSellerID = populateCheckoutSellers(cartUssidData);

			model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, cartLevelSellerID);
			// TPR-429 END

			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			List<AddressData> deliveryAddress = null;

			//TIS 391:  In case of normal checkout , delivery address will be null and for express checkout delivery address will be ore selected from the cart page
			// In case of express checkout it will redirect to the payment page from delivery mode selection

			if (cartData != null)
			{
				if (cartData.getDeliveryAddress() != null)
				{
					LOG.debug("Express checkout selected for address id : " + cartData.getDeliveryAddress().getId());
					return getCheckoutStep().nextStep();
				}

				deliveryAddress = (List<AddressData>) getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress());
			}




			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;
			//			deliveryAddress = getMplCheckoutFacade().rePopulateDeliveryAddress(deliveryAddress);

			//TISST-7473
			/*
			 * if (deliveryAddress == null || deliveryAddress.isEmpty()) { return
			 * MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.NEWADDRESSLINK; }
			 */

			//TISPT-400
			deliveryAddress = getMplCheckoutFacade().rePopulateDeliveryAddress(deliveryAddress);


			LOG.debug("Before final model attribute level set ");

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);

			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWSAVEDTOADDRESSBOOK, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
			model.addAttribute("checkoutPageName", selectAddress);
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			model.addAttribute("progressBarClass", "selectPage");
			returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error("CMSItemNotFoundException  while  selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exception  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		return returnPage;
	}

	/**
	 * @author TECH This method first checks the delivery modes if it has only home or express then it forwards to
	 *         doSelectDeliveryMode method and if it contains all the modes then first process cnc mode and then at jsp
	 *         level using continue link forward to doSelectDeliveryMode method.
	 * @param deliveryMethodForm
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response1
	 * @param session
	 * @return string
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYCHECKURL, method = RequestMethod.POST)
	@RequireHardLogIn
	public String doFindDelivaryMode(final DeliveryMethodForm deliveryMethodForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request,
			final HttpServletResponse response1, final HttpSession session) throws CMSItemNotFoundException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from doFindDelivaryMode methodin Controller");
		}
		//TISPT-400
		final CartModel cartModel = getCartService().getSessionCart();

		try
		{
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cartModel);
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
		}


		List<StoreLocationResponseData> response = null;
		redirectAttributes.addFlashAttribute("deliveryMethodForm", deliveryMethodForm);
		//create session object for deliveryMethodForm which will be used if cart contains both cnc and home delivery.
		session.setAttribute("deliveryMethodForm", deliveryMethodForm);

		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
		this.prepareDataForPage(model);

		//		final CartModel cartModel = getCartService().getSessionCart();			//TISPT-400

		//handle freebie
		final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
		final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

		final List<StoreLocationRequestData> storeLocationRequestDataList = new ArrayList<StoreLocationRequestData>();

		//count cnc products in cart entries
		int count = 0;
		//count other modes in cart entries
		int delModeCount = 0;
		int expCheckout = 0;
		double configurableRadius = 0;
		//retrieve pincode from session
		final String defaultPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
		if (cartModel != null && cartModel.getEntries() != null)
		{
			for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
			{
				if (null != cartEntryModel && null != cartEntryModel.getGiveAway() && !cartEntryModel.getGiveAway().booleanValue()
						&& null != deliveryMethodForm && CollectionUtils.isNotEmpty(deliveryMethodForm.getDeliveryMethodEntry()))
				{
					try
					{
						//final ProductModel productModel = cartEntryModel.getProduct(); / Performance fix TISPT-104
						//final ProductData productData = productFacade.getProductForOptions(productModel,
						//		Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE)); // Performance fix TISPT-104

						final String deliveryCode = deliveryMethodForm.getDeliveryMethodEntry()
								.get(cartEntryModel.getEntryNumber().intValue()).getDeliveryCode();
						if (deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT))
						{
							count++;
							//retrieve latitude and longitude for given pincode from db
							PincodeModel pinCodeModelObj = null;
							if (null != defaultPincode)
							{
								pinCodeModelObj = pincodeServiceFacade.getLatAndLongForPincode(defaultPincode);
							}
							//read radius from local properties file which is configurable.
							final String configRadius = mplConfigFacade
									.getCongigValue(MarketplacecheckoutaddonConstants.CONFIGURABLE_RADIUS);

							configurableRadius = Double.parseDouble(configRadius);
							LOG.debug("**********configrableRadius:" + configurableRadius);
							//this dto holds latitude and longitude
							final LocationDTO dto = new LocationDTO();
							if (null != pinCodeModelObj)
							{
								dto.setLongitude(pinCodeModelObj.getLongitude().toString());
								dto.setLatitude(pinCodeModelObj.getLatitude().toString());
							}
							final Location myLocation = new LocationDtoWrapper(dto);
							//first calls commerce to get all the stores for a sellerId based on the given pincode
							final StoreLocationRequestData storeLocationRequestData = papulateClicknCollectRequesrData(
									cartEntryModel.getSelectedUSSID(), myLocation.getGPS(), Double.valueOf(configurableRadius));
							storeLocationRequestDataList.add(storeLocationRequestData);
						}
						else
						{
							//count other modes
							delModeCount++;
						}
					}
					catch (final ArrayIndexOutOfBoundsException exception)
					{
						LOG.error("Error in Store selection Page", exception);
						return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
					}
					catch (final EtailBusinessExceptions e)
					{
						ExceptionUtil.etailBusinessExceptionHandler(e, null);
						LOG.error("EtailBusinessExceptions Error in Store selection Page ", e);
						return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
					}
					catch (final Exception e)
					{
						LOG.error("Error in Store selection Page ", e);
						return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
					}
				}
			}
			//if entry does not have any click and collect
			if (count == 0)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Cart Enties does not have any CNC mode");
				}
				//redirect to select address
				return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method"
						+ MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTURL;
			}
			if (count > 0 && delModeCount == 0)
			{
				//cart has only cnc
				//for express checkout
				if (cartData.getDeliveryAddress() != null)
				{
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Express checkout Having Delivery Mode as CNC: ");
					}
					cartModel.setDeliveryAddress(null);
					try
					{
						modelService.save(cartModel);
					}
					catch (final ModelSavingException e)
					{
						LOG.error("Exception while saving CardModel" + e.getMessage());
					}
				}
				String deliveryCode = null;
				if (deliveryMethodForm.getDeliveryMethodEntry() != null && !deliveryMethodForm.getDeliveryMethodEntry().isEmpty())
				{
					for (final DeliveryMethodEntry deliveryEntry : deliveryMethodForm.getDeliveryMethodEntry())
					{
						deliveryCode = deliveryEntry.getDeliveryCode();

						if (StringUtils.isNotEmpty(deliveryCode)
								&& deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT)) // Code optimized as part of performance fix TISPT-104
						{
							//getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode, deliveryEntry.getSellerArticleSKU());
							getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode, deliveryEntry.getSellerArticleSKU(),
									cartModel); //TISPT-400
						}

						/*
						 * TISPT-104 if (StringUtils.isNotEmpty(deliveryCode)) { Double deliveryCost = Double.valueOf(0.00D);
						 * if (deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT)) {
						 * deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
						 * deliveryEntry.getSellerArticleSKU()); deliveryCost = Double.valueOf(0.00D); } }
						 */
					}
				}
				applyPromotions();

				//populate freebie data
				populateFreebieProductData(cartModel, freebieModelMap, freebieParentQtyMap);

				timeOutSet(model);

			}
			if (count > 0 && delModeCount > 0)
			{
				try
				{
					Double finalDeliveryCost = Double.valueOf(0.0);
					//populate mplzone delivery mode
					//finalDeliveryCost = populateMplZoneDeliveryMode(deliveryMethodForm);
					//TISPT-400
					finalDeliveryCost = populateMplZoneDeliveryMode(deliveryMethodForm, cartModel);
					final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
					//final boolean calculationStatus = getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost,deliveryChargePromotionMap); //TIS 400
					getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost, deliveryChargePromotionMap, cartModel);
				}
				catch (final EtailBusinessExceptions e)
				{
					ExceptionUtil.etailBusinessExceptionHandler(e, null);
					getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
				}
				catch (final Exception e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
							MarketplacecommerceservicesConstants.E0000));
					getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
				}
				//for express checkout
				if (cartData.getDeliveryAddress() != null)
				{
					expCheckout++;
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Express checkout Having Mixed Delivery Mode as CNC and HD/Ed: ");
					}
					//TISST-13012
					try
					{
						//TISPT-400
						//						//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart()); TISPT-169
						//						final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cartModel);
						//						if (cartItemDelistedStatus)
						//						{
						//							return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
						//						}
						applyPromotions();

						//populate freebie data
						populateFreebieProductData(cartModel, freebieModelMap, freebieParentQtyMap);


						timeOutSet(model);

					}
					catch (final EtailBusinessExceptions e)
					{
						ExceptionUtil.etailBusinessExceptionHandler(e, null);
						getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
					}
					catch (final Exception e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
								MarketplacecommerceservicesConstants.E0000));
						getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
					}
				}
			}
			if (count > 0)
			{
				//cart has CNC.
				LOG.info("Cart Entries contain CNC mode");
				//calls oms to get inventories for given stores.
				//response = mplCartFacade.getStoreLocationsforCnC(storeLocationRequestDataList);
				List<ProudctWithPointOfServicesData> productWithPOS = new ArrayList<ProudctWithPointOfServicesData>();
				try
				{

					response = mplCartFacade.getStoreLocationsforCnC(storeLocationRequestDataList, cartModel);
					if (null != response && response.size() > 0)
					{
						//populates oms response to data object
						productWithPOS = getProductWdPos(response, model, freebieParentQtyMap);
					}
				}
				catch (final ClientEtailNonBusinessExceptions e)
				{
					LOG.error("::::::Exception in calling OMS Pincode service:::::::::" + e.getErrorCode());
					if (null != e.getErrorCode()
							&& ("O0001".equalsIgnoreCase(e.getErrorCode()) || "O0002".equalsIgnoreCase(e.getErrorCode()) || "O0007"
									.equalsIgnoreCase(e.getErrorCode())))
					{
						//populates oms response to data object
						productWithPOS = getProductWdPos(model, freebieParentQtyMap, storeLocationRequestDataList);
					}
				}

				//populate logged in user details as pickup details
				final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
				final StringBuffer pickupPerson = new StringBuffer();

				pickupPerson.append((null != customer.getFirstName()) ? customer.getFirstName() : "").append(" ")
						.append((null != customer.getLastName()) ? customer.getLastName() : "");

				final String pickupPersonName = pickupPerson.toString();
				final String pickUpPersonMobile = (null != customer.getMobileNumber()) ? customer.getMobileNumber() : "";

				model.addAttribute("pickupPersonName", pickupPersonName.trim());
				model.addAttribute("pickUpPersonMobile", pickUpPersonMobile);
				model.addAttribute("delModeCount", Integer.valueOf(delModeCount));
				model.addAttribute("cnccount", Integer.valueOf(count));
				model.addAttribute("expCheckout", Integer.valueOf(expCheckout));
				model.addAttribute("defaultPincode", defaultPincode);
				model.addAttribute("pwpos", productWithPOS);
				model.addAttribute("CSRFToken", CSRFTokenManager.getTokenForSession(request.getSession()));

			}
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		//		/TPR-1803
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		model.addAttribute("checkoutPageName", checkoutPageName);
		model.addAttribute("progressBarClass", "choosePage");
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChoosePickupLocationPage;
	}

	/**
	 * @author TECH This method populates List of Ats and ussid to the data object.
	 * @param response
	 * @return list of pos with product.
	 */
	private List<ProudctWithPointOfServicesData> getProductWdPos(final List<StoreLocationResponseData> response,
			final Model model, final Map<String, Long> freebieParentQtyMap)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getProductWdPos method which gets product with pos");
		}
		final List<ProudctWithPointOfServicesData> productWithPOS = new ArrayList<ProudctWithPointOfServicesData>();
		//iterate over oms response
		for (final StoreLocationResponseData storeLocationResponseData : response)
		{
			Map<String, Long> freebieProductsWithQuant = new HashMap<String, Long>();
			final List<PointOfServiceModel> posModelList = new ArrayList<PointOfServiceModel>();
			final String ussId = storeLocationResponseData.getUssId();
			//find parent cartEntry for a ussid
			final AbstractOrderEntryModel parentCartEntry = mplStoreLocatorFacade.getCartEntry(ussId);
			if (MapUtils.isNotEmpty(freebieParentQtyMap))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Find freebie product");
				}
				//add freebie product in the map
				freebieProductsWithQuant = mplStoreLocatorFacade.addFreebieProducts(parentCartEntry);
			}

			//filter only those stores which has qty greater than user ordered qty.
			mplStoreLocatorFacade.filterStoresWithQtyGTSelectedUserQty(parentCartEntry, storeLocationResponseData);

			//get seller information for a ussid
			final SellerInformationModel sellerInfoModel = mplSellerInformationFacade.getSellerDetail(ussId);

			//get stores from commerce
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Get stores from commerce");
			}
			for (final ATSResponseData atsResponseData : storeLocationResponseData.getAts())
			{
				final PointOfServiceModel posModel = mplSlaveMasterFacade.findPOSBySellerAndSlave(sellerInfoModel.getSellerID(),
						atsResponseData.getStoreId());
				if (null != posModel)
				{
					posModelList.add(posModel);
				}
			}

			//populate product with stores from oms
			final ProudctWithPointOfServicesData pwPOS = mplStoreLocatorFacade.populateProductWithStoresForUssid(ussId, model,
					freebieProductsWithQuant, posModelList, sellerInfoModel);
			productWithPOS.add(pwPOS);
		}
		return productWithPOS;
	}

	/**
	 * @author TECH This method populates List of stores and product to the data object if oms is down.
	 * @param model
	 * @param freebieParentQtyMap
	 * @param storeLocationRequestDataList
	 * @return list of product with stores
	 */
	private List<ProudctWithPointOfServicesData> getProductWdPos(final Model model, final Map<String, Long> freebieParentQtyMap,
			final List<StoreLocationRequestData> storeLocationRequestDataList)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getProductWdPos method if oms is down which gets product with pos");
		}
		final List<ProudctWithPointOfServicesData> productWithPOS = new ArrayList<ProudctWithPointOfServicesData>();
		final CartModel cartModel = getCartService().getSessionCart(); //TISPT-163
		//get only those stores which have quantity greater or equal to selected user quantity
		final CartModel cartModel1 = cartModel; //getCartService().getSessionCart(); //TISPT-163

		for (final StoreLocationRequestData storeLocationRequestData : storeLocationRequestDataList)
		{
			final ProudctWithPointOfServicesData pwPOS = new ProudctWithPointOfServicesData();
			final List<PointOfServiceModel> posModelList = new ArrayList<PointOfServiceModel>();
			final List<FreebieProduct> freebieProducts = new ArrayList<FreebieProduct>();
			final Map<String, Long> freebieProductsWithQuant = new HashMap<String, Long>();
			final List<PointOfServiceData> posDataList = new ArrayList<PointOfServiceData>();
			final String ussId = storeLocationRequestData.getUssId();
			//			final CartModel cartModel = getCartService().getSessionCart(); TISPT-163
			//			//get only those stores which have quantity greater or equal to selected user quantity
			//			final CartModel cartModel1 = getCartService().getSessionCart();
			for (final AbstractOrderEntryModel abstractCartEntry : cartModel1.getEntries())
			{
				if (null != abstractCartEntry && abstractCartEntry.getSelectedUSSID().equalsIgnoreCase(ussId)
						&& null != freebieParentQtyMap && CollectionUtils.isNotEmpty(abstractCartEntry.getAssociatedItems())) //TISPT-163
				{
					//				if (null != abstractCartEntry)
					//				{
					//					if (abstractCartEntry.getSelectedUSSID().equalsIgnoreCase(ussId))
					//					{
					//
					//						if (null != freebieParentQtyMap)
					//						{
					//							if (null != abstractCartEntry.getAssociatedItems() && abstractCartEntry.getAssociatedItems().size() > 0)
					//							{
					for (final String ussid : abstractCartEntry.getAssociatedItems())
					{
						//check for freebie entry in the cart
						if (cartModel != null && cartModel.getEntries() != null)
						{
							for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
							{
								if (cartEntryModel != null && cartEntryModel.getSelectedUSSID() != null
										&& cartEntryModel.getGiveAway() != null && cartEntryModel.getGiveAway().booleanValue())
								{

									if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(ussid))
									{
										LOG.info("Freebie Parent Product USSID" + abstractCartEntry.getSelectedUSSID());
										LOG.info("Freebie Product USSID" + ussid);
										if (cartEntryModel.getAssociatedItems().size() == 1)
										{
											freebieProductsWithQuant.put(ussid, cartEntryModel.getQuantity());
										}
									}
								}
							}
						}
					}
					//}
					//	}

					//}
				}
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("call to commerce db to get the seller details");
			}
			final SellerInformationModel sellerInfoModel = mplSellerInformationFacade.getSellerDetail(ussId);
			if (sellerInfoModel != null)
			{
				final String sellerName = sellerInfoModel.getSellerName();
				model.addAttribute("ussid", ussId);

				final ProductModel productModel = sellerInfoModel.getProductSource();
				final ProductData productData = productFacade.getProductForOptions(productModel,
						Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));
				//freebie starts
				if (freebieProductsWithQuant.size() > 0)
				{
					for (final Map.Entry<String, Long> entry : freebieProductsWithQuant.entrySet())
					{
						final FreebieProduct freebieProductData = new FreebieProduct();
						final String uss = entry.getKey();
						final Long qty = entry.getValue();
						final SellerInformationModel sellerInfo = mplSellerInformationFacade.getSellerDetail(uss);
						if (null != sellerInfo)
						{
							LOG.info("Associated Product USSID " + uss);
							final String assoicatedProductSellerName = sellerInfo.getSellerName();
							final ProductModel associateProductModel = sellerInfo.getProductSource();
							if (null != associateProductModel)
							{
								LOG.info("Associated ProductCode " + associateProductModel.getCode());
							}
							final ProductData associateProductData = productFacade.getProductForOptions(associateProductModel,
									Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));
							freebieProductData.setProduct(associateProductData);
							freebieProductData.setQty(qty);
							freebieProductData.setSellerName(assoicatedProductSellerName);
							freebieProducts.add(freebieProductData);
						}

					}
				}
				if (freebieProducts.size() > 0)
				{
					productData.setFreebieProducts(freebieProducts);
				}
				//freebie ends
				pwPOS.setUssId(ussId);
				pwPOS.setSellerName(sellerName);
				for (final AbstractOrderEntryModel abstractCartEntry : cartModel1.getEntries())
				{
					//					if (null != abstractCartEntry)
					//					{
					//						if (abstractCartEntry.getSelectedUSSID().equalsIgnoreCase(ussId) && abstractCartEntry.getGiveAway() != null
					//								&& !abstractCartEntry.getGiveAway().booleanValue())
					//						{
					//							final Long quantity = abstractCartEntry.getQuantity();
					//							pwPOS.setQuantity(quantity);
					//						}
					//					}
					//PMD fixed for collapsible if statement
					if (null != abstractCartEntry && abstractCartEntry.getSelectedUSSID().equalsIgnoreCase(ussId)
							&& abstractCartEntry.getGiveAway() != null && !abstractCartEntry.getGiveAway().booleanValue())
					{
						final Long quantity = abstractCartEntry.getQuantity();
						pwPOS.setQuantity(quantity);
					}
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("get stores from commerce based on SellerId and StoredId(slaveId)");
				}
				for (int i = 0; i < storeLocationRequestData.getStoreId().size(); i++)
				{
					if (i == 3)
					{
						break;
					}
					final PointOfServiceModel posModel = mplSlaveMasterFacade.findPOSBySellerAndSlave(sellerInfoModel.getSellerID(),
							storeLocationRequestData.getStoreId().get(i));
					posModelList.add(posModel);
				}
				for (final PointOfServiceModel pointOfServiceModel : posModelList)
				{
					//prepare pos data objects
					PointOfServiceData posData = new PointOfServiceData();
					if (null != pointOfServiceModel)
					{
						posData = pointOfServiceConverter.convert(pointOfServiceModel);
						posDataList.add(posData);
					}
				}
				pwPOS.setProduct(productData);
				pwPOS.setPointOfServices(posDataList);
				productWithPOS.add(pwPOS);
			}
		}
		return productWithPOS;
	}

	/**
	 * @author TECH This is an ajax call to save store for chossen cart entry.
	 * @param ussId
	 * @param posName
	 * @return yes if it saves successfully else no.
	 */
	@ResponseBody
	@RequestMapping(value = MarketplacecheckoutaddonConstants.ADDPOSORDERENTRY, method = RequestMethod.GET)
	public String addPosToOrderEntry(@RequestParam("ussId") final String ussId, @RequestParam("posName") final String posName)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from addPosToOrderEntry method ");
			LOG.debug("Store name is:::::::::::" + posName);
			LOG.debug("Cart entry Ussid is::::::::::: " + ussId);
		}
		String status = MarketplacecheckoutaddonConstants.SAVE_STORE_TOPORUDCT_SUCCESS_MSG;
		//get store information from commerce for posName
		final PointOfServiceModel posModel = mplSlaveMasterFacade.findPOSByName(posName);
		status = (null != posModel) ? mplStoreLocatorFacade.saveStoreForSelectedProduct(posModel, ussId)
				: MarketplacecheckoutaddonConstants.SAVE_STORE_TOPORUDCT_FAIL_MSG;
		return status;
	}

	/**
	 * This method stores pickup person details to the cart.
	 *
	 * @author TECH
	 * @param pickupPersonName
	 * @param pickupPersonMobile
	 * @return success if pickupperson details is successfully saved else returns failure
	 */
	@ResponseBody
	@RequestMapping(value = MarketplacecheckoutaddonConstants.ADDPICKUPPERSONDETAILS, method = RequestMethod.GET)
	public String addPickupPersonDetails(@RequestParam("pickupPersonName") final String pickupPersonName,
			@RequestParam("pickupPersonMobile") final String pickupPersonMobile)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from addPickupPersonDetails method, ajax call");
		}
		String status = "";
		//get session cart
		try
		{
			final CartModel cartModel = getCartService().getSessionCart();
			if (cartModel != null && null != pickupPersonName)
			{
				cartModel.setPickupPersonName(pickupPersonName);
			}
			if (cartModel != null && null != pickupPersonMobile)
			{
				cartModel.setPickupPersonMobile(pickupPersonMobile);

			}
			modelService.save(cartModel);
			status = "success";
		}
		catch (final Exception e)
		{
			status = "failure";
			LOG.error("Exception while saving cartModel to db");
		}
		return status;
	}

	/**
	 *
	 * @param model
	 *           - the id of the delivery mode
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLSELECTSAVEDADDRESS, method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectSavedAddress(final Model model)
	{

		String returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			List<AddressData> deliveryAddress = null;
			if (null != cartData)
			{
				deliveryAddress = (List<AddressData>) getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress());
			}

			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;
			deliveryAddress = getMplCheckoutFacade().rePopulateDeliveryAddress(deliveryAddress);

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWSAVEDTOADDRESSBOOK, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			timeOutSet(model);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629
			setCheckoutStepLinksForModel(model, getCheckoutStep());
		}
		catch (final CMSItemNotFoundException cmsEx)
		{
			LOG.error("CMSItemNotFoundException  while  selecting saved address ", cmsEx);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  selecting saved address ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting saved address ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while showing saved address :" + e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		model.addAttribute("checkoutPageName", selectAddress);
		return returnPage;
	}

	/**
	 * @description method is called while editing an address
	 * @param model
	 * @param addressCode
	 * @throws CMSItemNotFoundException
	 * @return String
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS
			+ MarketplacecheckoutaddonConstants.ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editAddress(@PathVariable(ModelAttributetConstants.ADDRESS_CODE) final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{

		List<StateData> stateDataList = new ArrayList<StateData>();
		final AccountAddressForm addressForm = new AccountAddressForm();
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
			model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
			model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
			timeOutSet(model);
			stateDataList = accountAddressFacade.getStates();
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);

			for (final AddressData addressData : accountAddressFacade.getAddressBook())
			{
				if (null != addressData && null != addressData.getId() && addressData.getId().equals(addressCode))
				{
					model.addAttribute(ModelAttributetConstants.REGIONS,
							getI18NFacade().getRegionsForCountryIso(addressData.getCountry().getIsocode()));
					model.addAttribute(ModelAttributetConstants.COUNTRY, addressData.getCountry().getIsocode());
					model.addAttribute(ModelAttributetConstants.ADDRESS_DATA, addressData);
					addressForm.setAddressId(addressData.getId());
					addressForm.setFirstName(addressData.getFirstName());
					addressForm.setLastName(addressData.getLastName());
					addressForm.setLine1(addressData.getLine1());
					addressForm.setLine2(addressData.getLine2());
					addressForm.setTownCity(addressData.getTown());
					addressForm.setPostcode(addressData.getPostalCode());
					addressForm.setCountryIso(addressData.getCountry().getIsocode());
					addressForm.setAddressType(addressData.getAddressType());
					addressForm.setMobileNo(addressData.getPhone());
					addressForm.setState(addressData.getState());
					addressForm.setLine3(addressData.getLine3());
					addressForm.setLocality(addressData.getLocality());

					if (addressData.getRegion() != null && !StringUtils.isEmpty(addressData.getRegion().getIsocode()))
					{
						addressForm.setRegionIso(addressData.getRegion().getIsocode());
					}

					if (isDefaultAddress(addressData.getId()))
					{
						addressForm.setDefaultAddress(Boolean.TRUE);
						model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS, Boolean.TRUE);
					}
					else
					{
						addressForm.setDefaultAddress(Boolean.FALSE);
						model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS, Boolean.FALSE);
					}
					break;
				}
			}

			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			model.addAttribute(ModelAttributetConstants.EDIT, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE);

			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());

		}
		catch (final Exception e)
		{
			this.prepareDataForPage(model);
			model.addAttribute("addressForm", addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			GlobalMessages.addErrorMessage(model, "address.error.formentry.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodEditPage;
	}

	/**
	 * @description method is called after selecting delivery mode and forward to address
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYNEWADDRESSURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String addNewAddress(final Model model) throws CMSItemNotFoundException
	{
		String addressFlag = "F";
		List<AddressData> deliveryAddress = null;
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);
			final List<StateData> stateDataList = accountAddressFacade.getStates();

			if (null != cartData)
			{
				deliveryAddress = (List<AddressData>) getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress());
			}


			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;

			final CartModel cartModel = getCartService().getSessionCart();
			if (cartModel != null)


			{
				for (final AddressModel userAddress : cartModel.getUser().getAddresses())


				{
					if (userAddress != null && userAddress.getVisibleInAddressBook() != null
							&& userAddress.getVisibleInAddressBook().booleanValue() == true)
					{
						addressFlag = "T";
					}
				}
			}

			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFLAG, addressFlag);

			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWSAVEDTOADDRESSBOOK, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.TRUE);
			model.addAttribute(ModelAttributetConstants.EDIT, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629
			timeOutSet(model);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  adding new address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while adding new address ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception occured while adding new address:", ex);
		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		//return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseAddNewAddressPage;
	}

	/**
	 * @description method is called to select address from list and forward to payment step
	 * @param selectedAddressCode
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTADDRESSURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String selectAddress(@RequestParam("selectedAddressCode") final String selectedAddressCode)
			throws CMSItemNotFoundException
	{
		try
		{
			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			AddressData finaladdressData = new AddressData();
			for (final AddressData addressData : accountAddressFacade.getAddressBook())
			{
				if (null != addressData && null != addressData.getId() && addressData.getId().equals(selectedAddressCode))
				{
					finaladdressData = addressData;
					break;
				}
			}

			final String selectedPincode = finaladdressData.getPostalCode();

			//TISSEC-11
			final String regex = "\\d{6}";

			if (selectedPincode.matches(regex))
			{
				//TISUTO-12 , TISUTO-11
				final String redirectURL = getMplCartFacade().checkPincodeAndInventory(selectedPincode);
				if (redirectURL.trim().length() > 0)
				{
					return redirectURL;
				}
				getMplCustomAddressFacade().setDeliveryAddress(finaladdressData);

				// Recalculating Cart Model
				LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
				getMplCheckoutFacade().reCalculateCart(cartData);
			}
			else
			{
				return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while selecting address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting address ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while selecting  address:" + e);
		}
		return getCheckoutStep().nextStep();
	}

	/**
	 * @description method is called to add new address while checkout and forward to payment step
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 */

	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYNEWADDRESSURL, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody JSONObject add(final AccountAddressForm addressForm, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		//TPR-1214
		// Save call has been changed to Ajax for saving a new address instead of HTTP request submission.
		final JSONObject jsonObj = new JSONObject();
		final CartModel oModel = getCartService().getSessionCart();
		try
		{
			final String errorMsg = mplAddressValidator.validate(addressForm);
			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{

				this.prepareDataForPage(model);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
				model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
						Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
				GlobalMessages.addErrorMessage(model, errorMsg);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(
						WebConstants.BREADCRUMBS_KEY,
						getResourceBreadcrumbBuilder().getBreadcrumbs(
								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.TRUE);
				timeOutSet(model);
				//return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseAddNewAddressPage;
				jsonObj.put("error", errorMsg);
			}
			else
			{
				if (StringUtils.isNotBlank(addressForm.getCountryIso()))
				{
					model.addAttribute(ModelAttributetConstants.REGIONS,
							getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
					model.addAttribute(ModelAttributetConstants.COUNTRY, addressForm.getCountryIso());
				}

				final AddressData newAddress = new AddressData();
				newAddress.setTitleCode(addressForm.getTitleCode());
				newAddress.setFirstName(addressForm.getFirstName());
				newAddress.setLastName(addressForm.getLastName());
				newAddress.setLine1(addressForm.getLine1());
				newAddress.setLine2(addressForm.getLine2());
				newAddress.setTown(addressForm.getTownCity());
				newAddress.setPostalCode(addressForm.getPostcode());
				newAddress.setBillingAddress(false);
				newAddress.setShippingAddress(true);
				newAddress.setAddressType(addressForm.getAddressType());
				newAddress.setState(addressForm.getState());
				newAddress.setPhone(addressForm.getMobileNo());
				newAddress.setLine3(addressForm.getLine3());
				newAddress.setLocality(addressForm.getLocality());

				if (StringUtils.isNotEmpty(addressForm.getCountryIso()))
				{
					final CountryData countryData = getI18NFacade().getCountryForIsocode(addressForm.getCountryIso());
					newAddress.setCountry(countryData);
				}
				if (StringUtils.isNotEmpty(addressForm.getRegionIso()))
				{
					final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
					newAddress.setRegion(regionData);
				}
				if (addressForm.getSaveInAddressBook() != null)
				{
					newAddress.setVisibleInAddressBook(addressForm.getSaveInAddressBook().booleanValue());
					if (addressForm.getSaveInAddressBook().booleanValue() && getUserFacade().isAddressBookEmpty())
					{
						newAddress.setDefaultAddress(true);
					}
				}
				else if (getCheckoutCustomerStrategy().isAnonymousCheckout())
				{
					newAddress.setDefaultAddress(true);
					newAddress.setVisibleInAddressBook(true);
				}

				if (null != oModel && null != oModel.getUser())
				{
					accountAddressFacade.addaddress(newAddress, (CustomerModel) oModel.getUser());
				}

				getMplCustomAddressFacade().setDeliveryAddress(newAddress);
				//TISUTO-12 , TISUTO-11
				final String redirectURL = getMplCartFacade().checkPincodeAndInventory(addressForm.getPostcode());
				if (redirectURL.trim().length() > 0)
				{
					jsonObj.put("redirect_url", redirectURL);
					//return redirectURL;
				}
				else
				{
					jsonObj.put("redirect_url", getCheckoutStep().nextStep());
				}

				//Recalculating Cart Model
				LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
				getMplCheckoutFacade().reCalculateCart(cartData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  saving new address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while saving new address ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :", e);
		}
		//return getCheckoutStep().nextStep();
		return jsonObj;
	}

	/**
	 * @description method is called to edit address and forward to payment step
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS
			+ MarketplacecheckoutaddonConstants.ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	@RequireHardLogIn
	public String edit(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final String errorMsg = mplAddressValidator.validate(addressForm);
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{
				this.prepareDataForPage(model);
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
				model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
						Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
				GlobalMessages.addErrorMessage(model, errorMsg);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(
						WebConstants.BREADCRUMBS_KEY,
						getResourceBreadcrumbBuilder().getBreadcrumbs(
								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);

				return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
			}

			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);

			if (StringUtils.isNotBlank(addressForm.getCountryIso()))
			{
				model.addAttribute(ModelAttributetConstants.REGIONS,
						getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
				model.addAttribute(ModelAttributetConstants.COUNTRY, addressForm.getCountryIso());
			}
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));

			final AddressData newAddress = new AddressData();
			newAddress.setId(addressForm.getAddressId());
			newAddress.setTitleCode(addressForm.getTitleCode());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());
			newAddress.setLine1(addressForm.getLine1());
			newAddress.setLine2(addressForm.getLine2());
			newAddress.setTown(addressForm.getTownCity());
			newAddress.setPostalCode(addressForm.getPostcode());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setBillingAddress(false);
			newAddress.setShippingAddress(true);
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setLine3(addressForm.getLine3());
			newAddress.setLocality(addressForm.getLocality());
			newAddress.setState(addressForm.getState());

			if (StringUtils.isNotEmpty(addressForm.getCountryIso()))
			{
				final CountryData countryData = getI18NFacade().getCountryForIsocode(addressForm.getCountryIso());
				newAddress.setCountry(countryData);
			}
			if (StringUtils.isNotEmpty(addressForm.getRegionIso()))
			{
				final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
				newAddress.setRegion(regionData);
			}

			if (addressForm.getSaveInAddressBook() == null)
			{
				newAddress.setVisibleInAddressBook(true);
			}
			else
			{
				newAddress.setVisibleInAddressBook(Boolean.TRUE.equals(addressForm.getSaveInAddressBook()));
			}

			//TISUTO-12 , TISUTO-11 ,TISUTO 106
			final String redirectURL = getMplCartFacade().checkPincodeAndInventory(addressForm.getPostcode());
			if (redirectURL.trim().length() > 0)
			{
				return redirectURL;
			}


			newAddress.setDefaultAddress(getUserFacade().isAddressBookEmpty() || getUserFacade().getAddressBook().size() == 1
					|| Boolean.TRUE.equals(addressForm.getDefaultAddress()));
			accountAddressFacade.editAddress(newAddress);

			//getCheckoutFacade().setDeliveryAddress(newAddress);
			getMplCustomAddressFacade().setDeliveryAddress(newAddress);

			//Recalculating Cart Model
			LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
			getMplCheckoutFacade().reCalculateCart(cartData);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while editing address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while editing address ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while editing address :", e);
		}
		return getCheckoutStep().nextStep();

	}

	/**
	 * This is a GET method which helps in resetting the convenience charges whenever it is not required
	 *
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws CalculationException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CALCULATEDELIVERYCOST, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody String calculateDeliveryCost(
			@PathVariable(MarketplacecheckoutaddonConstants.DELIVERYCOST) final String deliveryCost)
			throws NoSuchAlgorithmException, CalculationException
	{

		//LOG.info("deliveryCost " + deliveryCost);
		//TISST-13010
		CartData cartData = null;
		String totalPriceFormatted = MarketplacecommerceservicesConstants.EMPTY;
		String formatDeliveryCost = MarketplacecommerceservicesConstants.EMPTY;

		try
		{
			Double discountValue = Double.valueOf(0.0);
			final CartModel cartModel = getCartService().getSessionCart();
			//TISPT-104 Scenario needs to be covered if user redirect from payment page to delivery mode selection page
			// Cart recalculation method is invoked here
			getMplCartFacade().removeDeliveryMode(cartModel); //TISPT-104 Scenario needs to be covered if user redirect from payment page to delivery mode selection page
			//applyPromotions(); //TISPT-104


			//final CartModel cart = getCartService().getSessionCart();
			final Double subTotal = cartModel.getSubtotal();
			//final CartData cartData = mplExtendedCartConverter.convert(cartModel); //TISPT-104

			cartData = mplExtendedPromoCartConverter.convert(cartModel);

			if (null != cartData && cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
			{
				discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
			}
			LOG.info("discountValue  " + discountValue);
			final Double totalPriceAfterDeliveryCharge = Double.valueOf(subTotal.doubleValue() + Double.parseDouble(deliveryCost)
					- discountValue.doubleValue());
			final DecimalFormat df = new DecimalFormat("#.00");
			totalPriceFormatted = df.format(totalPriceAfterDeliveryCharge);
			formatDeliveryCost = df.format(Double.valueOf(deliveryCost));
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions in calculateDeliveryCost ", ex);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions in calculateDeliveryCost ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in calculateDeliveryCost ", ex);
		}

		return cartData.getCurrencySymbol() + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + formatDeliveryCost
				+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + totalPriceFormatted;
	}

	/**
	 * @Method checks if address is set as default
	 *
	 * @param addressId
	 *           - identifier for address to check
	 * @return true if address is default, false if address is not default
	 */
	protected boolean isDefaultAddress(final String addressId)
	{
		final AddressData defaultAddress = accountAddressFacade.getDefaultAddress();
		return (defaultAddress != null && defaultAddress.getId() != null && defaultAddress.getId().equals(addressId));
	}

	//@RequestMapping(value = "/back", method = RequestMethod.GET)
	//@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	//@RequestMapping(value = "/next", method = RequestMethod.GET)
	//@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(MarketplacecheckoutaddonConstants.DELIVERY_METHOD);
	}

	/**
	 * @return the mplCustomAddressFacade
	 */
	public MplCustomAddressFacade getMplCustomAddressFacade()
	{
		return mplCustomAddressFacade;
	}

	/**
	 * @param mplCustomAddressFacade
	 *           the mplCustomAddressFacade to set
	 */
	public void setMplCustomAddressFacade(final MplCustomAddressFacade mplCustomAddressFacade)
	{
		this.mplCustomAddressFacade = mplCustomAddressFacade;
	}

	/**
	 * @return the mplCheckoutFacade
	 */
	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}

	/**
	 * @param mplCheckoutFacade
	 *           the mplCheckoutFacade to set
	 */
	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}

	private void applyPromotions()
	{
		final CartModel cart = getCartService().getSessionCart();
		//recalculating cart
		try
		{
			commerceCartService.recalculateCart(cart);
		}
		catch (final CalculationException e)
		{
			LOG.error(" Exception in applyPromotions due to ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in  applyPromotions", ex);
		}
	}


	/**
	 * This method gets store locations for given seller.
	 *
	 * @param sellerUssId
	 * @param gps
	 * @param configurableRadius
	 * @return list of stores wrapped in StoreLocationRequestData obj.
	 */
	public StoreLocationRequestData papulateClicknCollectRequesrData(final String sellerUssId, final GPS gps,
			final Double configurableRadius)
	{
		LOG.debug("from papulateClicknCollectRequesrData method");
		//reads seller id from ussid which is first six digits
		final String sellerId = sellerUssId.substring(0, 6);
		final StoreLocationRequestData storeLocationRequestData = new StoreLocationRequestData();

		List<Location> storeList = new ArrayList<Location>();
		try
		{
			//calls commerce db to get all the stores in sorting order.
			storeList = pincodeServiceFacade.getSortedLocationsNearby(gps, configurableRadius.doubleValue(), sellerId); // Code optimized as part of performance fix TISPT-104
		}
		catch (final Exception e)
		{
			storeLocationRequestData.setStoreId(null);
			LOG.error("Exception while retrieving all the stores based on gps,sellerId and radius");
		}
		if (CollectionUtils.isNotEmpty(storeList))

		{
			final List<String> locationList = new ArrayList<String>();
			for (final Location location : storeList)
			{
				locationList.add(location.getName());
			}
			LOG.debug("Total number of Stores :" + locationList.size() + " for Seller" + sellerId);
			storeLocationRequestData.setStoreId(locationList);
		}
		storeLocationRequestData.setUssId(sellerUssId);
		//populate newly added fields
		//get SellerInfo based on sellerUssid
		final SellerInformationModel sellerInfoModel = mplSellerInformationFacade.getSellerDetail(sellerUssId);
		ProductModel productModel = null;
		ProductData productData = null;
		if (null != sellerInfoModel)
		{
			productModel = sellerInfoModel.getProductSource();
			productData = productFacade.getProductForOptions(productModel,
					Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));
			storeLocationRequestData.setSellerId(sellerInfoModel.getSellerID());
		}
		List<RichAttributeModel> richAttributeModel = null;
		if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
		{
			richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
			if (richAttributeModel != null && richAttributeModel.get(0) != null
					&& richAttributeModel.get(0).getDeliveryFulfillModes() != null
					&& richAttributeModel.get(0).getDeliveryFulfillModes().getCode() != null)
			{
				final String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
				storeLocationRequestData.setFulfillmentType(fulfillmentType.toUpperCase());
			}
			else
			{
				LOG.debug("storeLocationRequestData :  Fulfillment type not received for the SellerId"
						+ sellerInfoModel.getSellerArticleSKU());
			}
			if (richAttributeModel != null && richAttributeModel.get(0) != null
					&& richAttributeModel.get(0).getShippingModes() != null
					&& richAttributeModel.get(0).getShippingModes().getCode() != null)
			{
				final String globalCodeShippingMode = MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(richAttributeModel.get(0)
						.getShippingModes().getCode().toUpperCase());
				storeLocationRequestData.setTransportMode(globalCodeShippingMode);
			}
			else
			{
				LOG.debug("storeLocationRequestData :  ShippingMode type not received for the "
						+ sellerInfoModel.getSellerArticleSKU());
			}
			if ((null != productData.getSeller()) && (null != productData.getSeller().get(0))
					&& (null != productData.getSeller().get(0).getSpPrice())
					&& !(productData.getSeller().get(0).getSpPrice().equals("")))
			{
				storeLocationRequestData.setPrice(productData.getSeller().get(0).getSpPrice().getValue().doubleValue());
			}
			else if ((null != productData.getSeller()) && (null != productData.getSeller().get(0))
					&& (null != productData.getSeller().get(0).getMopPrice())
					&& !(productData.getSeller().get(0).getMopPrice().equals("")))
			{
				storeLocationRequestData.setPrice(productData.getSeller().get(0).getMopPrice().getValue().doubleValue());
			}
			else if (null != productData.getSeller().get(0).getMrpPrice()
					&& !(productData.getSeller().get(0).getMrpPrice().equals("")))
			{
				storeLocationRequestData.setPrice(productData.getSeller().get(0).getMrpPrice().getValue().doubleValue());
			}
			else
			{
				LOG.debug("No price avaiable for seller :" + productData.getSeller().get(0).getSellerID());
			}
		}

		return storeLocationRequestData;
	}

	/**
	 * This method is called when a user select change pincode at cnc page.
	 *
	 * @param pin
	 * @param productCode
	 * @param sellerId
	 * @return if successful retursn list of pos for a prouct else null.
	 * @throws CMSItemNotFoundException
	 */
	@ResponseBody
	@RequestMapping(value = MarketplacecheckoutaddonConstants.UPDATE_CHECK_PINCODE, method = RequestMethod.GET)
	public List<PointOfServiceData> upDatePincodeServicabilityCheck(@RequestParam(value = "pin") final String pin,
			@RequestParam(value = "productCode") final String productCode, @RequestParam(value = "sellerId") final String ussId,
			final Model model) throws CMSItemNotFoundException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from upDatePincodeServicabilityCheck method when customer change pincode at cnc page");
			LOG.debug("New Pincode is::::::::" + pin);
			LOG.debug("Ussid is::::::::" + ussId);
		}
		List<PointOfServiceData> stores = new ArrayList<PointOfServiceData>();
		List<StoreLocationResponseData> omsResponse = new ArrayList<StoreLocationResponseData>();
		List<ProudctWithPointOfServicesData> productWithPOS = new ArrayList<ProudctWithPointOfServicesData>();
		List<StoreLocationRequestData> storeLocationRequestDataList = new ArrayList<StoreLocationRequestData>();

		//call to check pincode serviceability
		boolean status = false;

		//call service to get list of ATS and ussid



		try
		{
			omsResponse = pincodeServiceFacade.getListofStoreLocationsforPincode(pin, ussId, productCode, null);
			if (omsResponse.size() > 0)
			{
				productWithPOS = getProductWdPos(omsResponse, model, null);
			}
		}
		catch (final ClientEtailNonBusinessExceptions e)
		{
			LOG.error("::::::Exception in calling OMS Pincode service:::::::::" + e.getErrorCode());
			if (null != e.getErrorCode()
					&& ("O0001".equalsIgnoreCase(e.getErrorCode()) || "O0002".equalsIgnoreCase(e.getErrorCode()) || "O0007"
							.equalsIgnoreCase(e.getErrorCode())))
			{
				storeLocationRequestDataList = pincodeServiceFacade.getStoresFromCommerce(pin, ussId);
				if (storeLocationRequestDataList.size() > 0)
				{
					//populates oms response to data object
					productWithPOS = getProductWdPos(model, null, storeLocationRequestDataList);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(productWithPOS))
		{
			status = true;
			stores = productWithPOS.get(0).getPointOfServices();
		}

		if (!status)
		{
			stores.clear();
			return stores;
		}
		return stores;
	}

	/**
	 * This method is called for inventory reservation when cart contains only cnc mode.
	 *
	 * @author TECH
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYCNCINVRESV)
	@RequireHardLogIn
	public String cncCartReservation(final Model model, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("from cncCartReservation method ");
		}
		final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
				MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, null);

		if (!inventoryReservationStatus)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("cart reservation failed");
			}
			getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
		}

		//forward to payment page
		return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/payment-method/pay";
	}

	/**
	 * populating the request data to be send to oms
	 *
	 * @param productCode
	 * @param finalStoreList
	 * @return requestData
	 */
	/*
	 * private List<PincodeServiceData> populatePinCodeServiceData(final String productCode, final GPS gps, final Double
	 * configurableRadius) {
	 * 
	 * final List<PincodeServiceData> requestData = new ArrayList<PincodeServiceData>(); PincodeServiceData data = null;
	 * MarketplaceDeliveryModeData deliveryModeData = null; try { final ProductModel productModel =
	 * productService.getProductForCode(productCode); final ProductData productData =
	 * productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC, ProductOption.SELLER,
	 * ProductOption.PRICE));
	 * 
	 * if (productData != null)
	 * 
	 * { for (final SellerInformationData seller : productData.getSeller())
	 * 
	 * 
	 * 
	 * { final List<MarketplaceDeliveryModeData> deliveryModeList = new ArrayList<MarketplaceDeliveryModeData>(); data =
	 * new PincodeServiceData(); if ((null != seller.getDeliveryModes()) && !(seller.getDeliveryModes().isEmpty())) { for
	 * (final MarketplaceDeliveryModeData deliveryMode : seller.getDeliveryModes()) { deliveryModeData =
	 * fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid()); deliveryModeList.add(deliveryModeData);
	 * } data.setDeliveryModes(deliveryModeList); } if (null != seller.getFullfillment() &&
	 * StringUtils.isNotEmpty(seller.getFullfillment())) {
	 * data.setFullFillmentType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getFullfillment().toUpperCase())); }
	 * if (null != seller.getShippingMode() && (StringUtils.isNotEmpty(seller.getShippingMode()))) {
	 * data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getShippingMode().toUpperCase())); } if
	 * (null != seller.getSpPrice() && !(seller.getSpPrice().equals(ModelAttributetConstants.EMPTY))) { data.setPrice(new
	 * Double(seller.getSpPrice().getValue().doubleValue())); } else if (null != seller.getMopPrice() &&
	 * !(seller.getMopPrice().equals(ModelAttributetConstants.EMPTY))) { data.setPrice(new
	 * Double(seller.getMopPrice().getValue().doubleValue())); } else if (null != seller.getMrpPrice() &&
	 * !(seller.getMrpPrice().equals(ModelAttributetConstants.EMPTY))) { data.setPrice(new
	 * Double(seller.getMrpPrice().getValue().doubleValue())); } else { LOG.debug("No price avaiable for seller :" +
	 * seller.getSellerID()); continue; } if (null != seller.getIsCod() && StringUtils.isNotEmpty(seller.getIsCod())) {
	 * data.setIsCOD(seller.getIsCod()); }
	 * 
	 * 
	 * LOG.debug("Current locations for Seller Id**********" + seller.getSellerID());
	 * 
	 * @SuppressWarnings("boxing") final List<Location> storeList = pincodeServiceFacade.getSortedLocationsNearby(gps,
	 * configurableRadius.doubleValue(), seller.getSellerID()); // Code optimized as part of performance fix TISPT-104
	 * LOG.debug("StoreList size is :" + storeList.size()); if (CollectionUtils.isNotEmpty(storeList)) { final
	 * List<String> locationList = new ArrayList<String>(); for (final Location location : storeList) {
	 * locationList.add(location.getName()); } LOG.debug("locationList:" + locationList.size());
	 * data.setStore(locationList); } data.setSellerId(seller.getSellerID()); LOG.debug("seller.getSellerID():" +
	 * seller.getSellerID()); LOG.debug("seller.getUssid():" + seller.getUssid()); LOG.debug("seller.getUssid():" +
	 * seller.getUssid()); data.setUssid(seller.getUssid());
	 * data.setIsDeliveryDateRequired(ControllerConstants.Views.Fragments.Product.N); requestData.add(data);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * } } catch (final EtailBusinessExceptions e) { ExceptionUtil.etailBusinessExceptionHandler(e, null); }
	 * 
	 * catch (final Exception e) {
	 * 
	 * throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000); } return requestData; }
	 */

	/**
	 * @param deliveryMode
	 * @param ussid
	 * @return deliveryModeData
	 */
	/*
	 * private MarketplaceDeliveryModeData fetchDeliveryModeDataForUSSID(final String deliveryMode, final String ussid) {
	 * final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData(); final
	 * MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = mplCheckoutFacade
	 * .populateDeliveryCostForUSSIDAndDeliveryMode(deliveryMode, MarketplaceFacadesConstants.INR, ussid);
	 * 
	 * final PriceData priceData = productDetailsHelper.formPriceData(mplZoneDeliveryModeValueModel.getValue());
	 * deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
	 * deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
	 * deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
	 * deliveryModeData.setSellerArticleSKU(ussid); deliveryModeData.setDeliveryCost(priceData); return deliveryModeData;
	 * }
	 */


	/**
	 * @param model
	 */
	private void timeOutSet(final Model model)
	{
		// YTODO Auto-generated method stub
		final double deliveryMode = Double.parseDouble(configurationService.getConfiguration().getString(
				"checkout.deliverymode.timeout"));
		final String timeOut = Double.toString(deliveryMode * 60 * 1000);
		model.addAttribute(MarketplacecheckoutaddonConstants.TIMEOUT, timeOut);
	}

	/**
	 * Populates freebie data
	 *
	 * @author TECH
	 * @param cartModel
	 * @param freebieModelMap
	 * @param freebieParentQtyMap
	 */
	private void populateFreebieProductData(final CartModel cartModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from populateFreebieProductData method");
			LOG.debug(">>>>>>>>>>  Step 2  :Freebie data preparation ");
		}
		if (cartModel != null && cartModel.getEntries() != null)
		{
			for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
			{
				if (cartEntryModel != null && !cartEntryModel.getGiveAway().booleanValue()
						&& cartEntryModel.getSelectedUSSID() != null)
				{

					freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
					freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
				}
			}
		}
		getMplCheckoutFacade().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);
	}

	/**
	 * Populates mplZoneDeliveryMode.
	 *
	 * @author TECH
	 * @param deliveryMethodForm
	 * @return finalDeliveryCost
	 */
	private Double populateMplZoneDeliveryMode(final DeliveryMethodForm deliveryMethodForm, final CartModel cartModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from populateMplZoneDeliveryMode method");
		}
		String deliveryCode = null;
		Double finalDeliveryCost = Double.valueOf(0.0);
		//populate mplzone delivery mode
		if (deliveryMethodForm.getDeliveryMethodEntry() != null && !deliveryMethodForm.getDeliveryMethodEntry().isEmpty())
		{
			for (final DeliveryMethodEntry deliveryEntry : deliveryMethodForm.getDeliveryMethodEntry())
			{
				deliveryCode = deliveryEntry.getDeliveryCode();
				if (StringUtils.isNotEmpty(deliveryCode))
				{
					Double deliveryCost = Double.valueOf(0.00D); // Code optimized as part of performance fix TISPT-104

					if (deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT))
					{
						//						deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
						//								deliveryEntry.getSellerArticleSKU());
						deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
								deliveryEntry.getSellerArticleSKU(), cartModel); //TISPT-400
						deliveryCost = Double.valueOf(0.00D); // Code optimized as part of performance fix TISPT-104
					}
					else
					{
						//deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
						//		deliveryEntry.getSellerArticleSKU());
						deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
								deliveryEntry.getSellerArticleSKU(), cartModel); //TISPT-400
					}
					finalDeliveryCost = Double.valueOf(finalDeliveryCost.doubleValue() + deliveryCost.doubleValue());
				}
			}
		}
		return finalDeliveryCost;
	}

	/**
	 * Method call to remove delivery point of service when redirecting from store locator page to delivery method choose
	 * page
	 *
	 * @author TECHOUTS
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHANGEDELIVERYMODE, method = RequestMethod.GET)
	@RequireHardLogIn
	public String chnageDeliveryMode(final Model model, final RedirectAttributes redirectAttributes)
	{
		final CartModel cartModel = getCartService().getSessionCart();
		if (cartModel != null && CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
			boolean isSaveRequired = false;
			for (final AbstractOrderEntryModel cartEntry : cartEntryList)
			{
				if (cartEntry.getDeliveryPointOfService() != null)
				{
					isSaveRequired = true;
					cartEntry.setDeliveryPointOfService(null);
					//modelService.save(cartEntry);
				}
			}
			if (isSaveRequired) //TISPT-169
			{
				modelService.saveAll(cartEntryList);
			}

			//			for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
			//			{
			//				if (cartEntry.getDeliveryPointOfService() != null)
			//				{
			//					cartEntry.setDeliveryPointOfService(null);
			//					modelService.save(cartEntry);
			//				}
			//			}
		}

		return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/choose";
	}

	private void setExpressCheckout(final CartModel serviceCart)
	{
		if (serviceCart.getDeliveryAddress() != null && serviceCart.getIsExpressCheckoutSelected().booleanValue())
		{
			serviceCart.setIsExpressCheckoutSelected(Boolean.valueOf(false));
			serviceCart.setDeliveryAddress(null);
			modelService.save(serviceCart);
		}
	}

	/**
	 * TRP- 1215
	 *
	 * @description method is called to set DefaultAddress from existing Address of the customer
	 * @param addressCode
	 * @param redirectModel
	 * @param model
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = MarketplacecheckoutaddonConstants.LINK_SET_DEFAUT_ADDRESS + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	@ResponseBody
	public boolean setDefaultAddress(@PathVariable("addressCode") final String addressCode,
			final RedirectAttributes redirectModel, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final AddressData addressData = new AddressData();
			addressData.setDefaultAddress(true);
			addressData.setVisibleInAddressBook(true);
			addressData.setId(addressCode);
			userFacade.setDefaultAddress(addressData);
			return true;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions in setDefaultAddress ", e);
			return false;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailBusinessExceptions in setDefaultAddress ", e);
			return false;
		}
	}

	/**
	 * @return the mplCartFacade
	 */
	public MplCartFacade getMplCartFacade()
	{
		return mplCartFacade;
	}


	/**
	 * @param mplCartFacade
	 *           the mplCartFacade to set
	 */
	public void setMplCartFacade(final MplCartFacade mplCartFacade)
	{
		this.mplCartFacade = mplCartFacade;
	}


	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}


	/**
	 * @return the mplCouponFacade
	 */
	public MplCouponFacade getMplCouponFacade()
	{
		return mplCouponFacade;
	}


	/**
	 * @param mplCouponFacade
	 *           the mplCouponFacade to set
	 */
	public void setMplCouponFacade(final MplCouponFacade mplCouponFacade)
	{
		this.mplCouponFacade = mplCouponFacade;
	}

	//TPR-429 change
	public static String populateCheckoutSellers(final CartData cartData)
	{
		String cartLevelSellerID = null;
		final List<OrderEntryData> sellerList = cartData.getEntries();
		for (final OrderEntryData seller : sellerList)
		{
			final String sellerID = seller.getSelectedSellerInformation().getSellerID();
			if (cartLevelSellerID != null)
			{
				cartLevelSellerID += "_" + sellerID;
			}
			else
			{
				cartLevelSellerID = sellerID;
			}
		}
		return cartLevelSellerID;
	}
}
