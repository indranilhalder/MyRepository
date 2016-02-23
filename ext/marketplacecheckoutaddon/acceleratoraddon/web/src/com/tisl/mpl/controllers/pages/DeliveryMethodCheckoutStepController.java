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
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.storelocator.data.TimeData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.ATSResponseData;
import com.tisl.mpl.facades.data.ProudctWithPointOfServicesData;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.service.MplSlaveMasterService;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.util.CSRFTokenManager;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.util.ExceptionUtil;


@Controller
@RequestMapping(value = "/checkout/multi/delivery-method")
public class DeliveryMethodCheckoutStepController extends AbstractCheckoutStepController
{
	
	private final static String PICKUP_LOCATION = "pickup-location";
	
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

	@Resource(name = ModelAttributetConstants.ACCELERATOR_CHECKOUT_FACADE)
	private CheckoutFacade checkoutFacade;

	@Autowired
	private AccountAddressFacade accountAddressFacade;

	@Autowired
	private MplAddressValidator mplAddressValidator;

	@Autowired
	private CartService cartService;

	@Autowired
	private CommerceCartService commerceCartService;

	@Resource(name = "pincodeService")
	private PincodeService pincodeService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;
	
	@Resource(name = "productService")
	private ProductService productService;
	
	@Resource(name = "modelService")
	private ModelService modelService;

	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;
	
	@Resource(name="addressConverter")
	private Converter<AddressModel, AddressData> addressConverter;
	
	@Resource(name = "mplSlaveMasterService")
	private MplSlaveMasterService mplSlaveMasterService;
	
	@Autowired
	private MplCouponFacade mplCouponFacade;

	private final String checkoutPageName = "Choose Your Delivery Options";

	private final String checkoutPageName1 = "New Address";
	private final String selectAddress = "Select Address";
	
	@Autowired
	private MplSellerInformationService mplSellerInformationService;
	
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;

	@Resource(name = "pinCodeFacade")
	private PinCodeServiceAvilabilityFacade pinCodeFacade;
	 
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;
	 
	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;
	 
	@Resource(name="timeDataConverter")
	private Converter<Date, TimeData> timeDataConverter;

	private static final Logger LOG = Logger.getLogger(DeliveryMethodCheckoutStepController.class);

	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHOOSEVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = MarketplacecheckoutaddonConstants.DELIVERY_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<PinCodeResponseData> responseData = null;
		String returnPage = "";
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				return getCheckoutStep().previousStep();
			}

			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			//TISBOX-1618
			applyPromotions();
			getMplCartFacade().removeDeliveryMode(getCartService().getSessionCart());
			getMplCouponFacade().releaseVoucherInCheckout(getCartService().getSessionCart());
			final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);

			timeOutSet(model);
			if (StringUtils.isNotEmpty(defaultPinCodeId) && (cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
			{
				responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartData);
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartData, responseData);
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

				this.prepareDataForPage(model);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
						.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				model.addAttribute("metaRobots", "noindex,nofollow");
				setCheckoutStepLinksForModel(model, getCheckoutStep());
			}

			returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailNonBusinessExceptions  while  delivery mode show enter step ", e);
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
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			LOG.error("EtailNonBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		model.addAttribute("checkoutPageName", checkoutPageName);
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
	 * @throws CMSItemNotFoundException
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTURL)
	@RequireHardLogIn
	public String doSelectDeliveryMode(@ModelAttribute("deliveryMethodForm")DeliveryMethodForm deliveryMethodForm, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		Double finalDeliveryCost = Double.valueOf(0.0);
		String returnPage = "";
		int count =0;
		Boolean selectPickupDetails = false;
		final CartModel cartModel = getCartService().getSessionCart();
		if (deliveryMethodForm.getDeliveryMethodEntry() == null)
		{
			//if cart contains cnc and home/express delivery modes 
			count++;
			HttpSession session = request.getSession();
			deliveryMethodForm =(DeliveryMethodForm)session.getAttribute("deliveryMethodForm");
			final String pickupPersonName = cartModel.getPickupPersonName();
			final String pickupPersonMobile = cartModel.getPickupPersonMobile();
			if ((pickupPersonName == null) && (pickupPersonMobile == null))
			{
				selectPickupDetails = true;
				model.addAttribute("selectPickupDetails", selectPickupDetails);
				return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/check";
			}
			String deliveryCode = null;
			if (deliveryMethodForm.getDeliveryMethodEntry() != null && !deliveryMethodForm.getDeliveryMethodEntry().isEmpty())
			{
				for (final DeliveryMethodEntry deliveryEntry : deliveryMethodForm.getDeliveryMethodEntry())
				{
					deliveryCode = deliveryEntry.getDeliveryCode();
					if (StringUtils.isNotEmpty(deliveryCode))
					{
						Double deliveryCost = 0.0;
						if (deliveryCode.equalsIgnoreCase("click-and-collect"))
						{
							deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
									deliveryEntry.getSellerArticleSKU());
							deliveryCost = 0.0;
						}else {
							deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
									deliveryEntry.getSellerArticleSKU());
						}
						finalDeliveryCost = Double.valueOf(finalDeliveryCost.doubleValue() + deliveryCost.doubleValue());
					}
				}
			}

		}
		else 
		{
			String deliveryCode = null;
			if (deliveryMethodForm.getDeliveryMethodEntry() != null && !deliveryMethodForm.getDeliveryMethodEntry().isEmpty())
			{
				for (final DeliveryMethodEntry deliveryEntry : deliveryMethodForm.getDeliveryMethodEntry())
				{
					deliveryCode = deliveryEntry.getDeliveryCode();
					if (StringUtils.isNotEmpty(deliveryCode))
					{
						Double deliveryCost = 0.0;
						if (deliveryCode.equalsIgnoreCase("click-and-collect"))
						{
							deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
									deliveryEntry.getSellerArticleSKU());
							deliveryCost = 0.0;
						}else {
							deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
									deliveryEntry.getSellerArticleSKU());
						}
						finalDeliveryCost = Double.valueOf(finalDeliveryCost.doubleValue() + deliveryCost.doubleValue());
					}
				}
			}

		}
		
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				return getCheckoutStep().previousStep();
			}

			
			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

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

			applyPromotions();

			getMplCheckoutFacade().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);

			LOG.debug(">>>>>>>>>>  Step 2  :Freebie data preparation ");

			timeOutSet(model);

			final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
			final boolean calculationStatus = getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost,
					deliveryChargePromotionMap); //TIS 400

			LOG.debug(">>>>>>>>>>  Step 3:   Delivery cost final calculation status  " + calculationStatus);

			/*** Inventory Soft Reservation Start ***/

			final boolean inventoryReservationStatus = getMplCartFacade()
					.isInventoryReserved(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART);
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
					&& (cartUssidData.getEntries() != null && !cartUssidData.getEntries().isEmpty()))
			{
				responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartUssidData);
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartUssidData, responseData);

				getMplCartFacade().setDeliveryDate(cartUssidData, responseData);
				//TISUTO-72 TISST-6994,TISST-6990 set cart COD eligible
				final boolean isCOdEligible = getMplCartFacade().addCartCodEligible(deliveryModeDataMap, responseData);
				LOG.info("isCOdEligible " + isCOdEligible);
			}

			LOG.debug(">>>>>>>>>>  Step 5:  Cod status setting done  ");

			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			//TIS 391:  In case of normal checkout , delivery address will be null and for express checkout delivery address will be ore selected from the cart page
			// In case of express checkout it will redirect to the payment page from delivery mode selection
			if (cartData.getDeliveryAddress() != null)
			{
				LOG.debug("Express checkout selected for address id : " + cartData.getDeliveryAddress().getId());
				return getCheckoutStep().nextStep();
			}


			List<AddressData> deliveryAddress = (List<AddressData>) getMplCustomAddressFacade()
					.getDeliveryAddresses(cartData.getDeliveryAddress());
			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;
			deliveryAddress = getMplCheckoutFacade().rePopulateDeliveryAddress(deliveryAddress);

			//TISST-7473
			if (deliveryAddress == null || deliveryAddress.isEmpty())
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.NEWADDRESSLINK;
			}
			LOG.debug("Before final model attribute level set ");

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
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
					.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		return returnPage;
	}

	/**
	 * @author TECH
	 * This method first checks the delivery modes if it has only home or express then it forwards to doSelectDeliveryMode method
	 * and if it contains all the modes then first process cnc mode and then at jsp level using continue link forward to doSelectDeliveryMode method.
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
			final Model model,final RedirectAttributes redirectAttributes, final HttpServletRequest request,
		   final HttpServletResponse response1,final HttpSession session) throws CMSItemNotFoundException
	{
		LOG.debug("from doFindDelivaryMode methodin Controller");
		List<StoreLocationResponseData> response = null;
		redirectAttributes.addFlashAttribute("deliveryMethodForm", deliveryMethodForm);
		//create session object for deliveryMethodForm which will be used if cart contains both cnc and home delivery.
		session.setAttribute("deliveryMethodForm" , deliveryMethodForm);
		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		if (cartData.getDeliveryAddress() != null)
		{
			LOG.debug("Express checkout : ");
			return getCheckoutStep().nextStep();
		}		
		model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
		this.prepareDataForPage(model);
		
			final CartModel cartModel = getCartService().getSessionCart();
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
			final List<StoreLocationRequestData> storeLocationRequestDataList = new ArrayList<StoreLocationRequestData>();
			int count=0;
			int delModeCount =0;
			String defaultPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			if (cartModel != null && cartModel.getEntries() != null)
			{
				for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
				{
					final ProductModel productModel = cartEntryModel.getProduct();
					final ProductData productData = productFacade.getProductForOptions(productModel,
					Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));
					final String deliveryCode = deliveryMethodForm.getDeliveryMethodEntry().get(cartEntryModel.getEntryNumber()).getDeliveryCode();
					if (deliveryCode.equalsIgnoreCase("click-and-collect"))
					{
						count++;
						//retrieve latitude and longitude for given pincode from db
						PincodeModel pinCodeModelObj = null;
						if (null != defaultPincode)
						{
							pinCodeModelObj = pincodeService.getLatAndLongForPincode(defaultPincode);
						}
						//read radius from local properties file which is configurable.
						final String configurableRadius = Config.getParameter("marketplacestorefront.configure.radius");
						LOG.info("configurableRadius**********." + Double.parseDouble(configurableRadius));
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
								cartEntryModel.getSelectedUSSID(), myLocation.getGPS(), Double.parseDouble(configurableRadius));
						storeLocationRequestDataList.add(storeLocationRequestData);
					}
					else {
						//count other modes 
						delModeCount++;
					}
				}
				//if entry does not have any click and collect
				if (count == 0)
				{ 
					LOG.info("Cart Enties does not have any CNC mode");
					//redirect to select address
					return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/select";
				}
				if (count >0 && delModeCount == 0)
				{
					String deliveryCode = null;
					if (deliveryMethodForm.getDeliveryMethodEntry() != null && !deliveryMethodForm.getDeliveryMethodEntry().isEmpty())
					{
						for (final DeliveryMethodEntry deliveryEntry : deliveryMethodForm.getDeliveryMethodEntry())
						{
							deliveryCode = deliveryEntry.getDeliveryCode();
							if (StringUtils.isNotEmpty(deliveryCode))
							{
								Double deliveryCost = 0.0;
								if (deliveryCode.equalsIgnoreCase("click-and-collect"))
								{
									deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
											deliveryEntry.getSellerArticleSKU());
									deliveryCost = 0.0;
								}
							}
						}
					}

				}
				if (count>0)
				{
					//cart has CNC.
					LOG.info("Cart Entries contain CNC mode");
					//calls oms to get inventories for given stores.
					response = mplCartFacade.getStoreLocationsforCnC(storeLocationRequestDataList);
					List<ProudctWithPointOfServicesData> productWithPOS = new ArrayList<ProudctWithPointOfServicesData>();
					//populates oms response to data object
					productWithPOS = getProductWdPos(response,model);
	
					model.addAttribute("delModeCount",delModeCount);
					model.addAttribute("cnccount",count);
					model.addAttribute("defaultPincode", defaultPincode);
					model.addAttribute("pwpos", productWithPOS);
					model.addAttribute("CSRFToken", CSRFTokenManager.getTokenForSession(request.getSession()));
					
				}
			}
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChoosePickupLocationPage;
		}
	/**
	 * @author TECH
	 * This method populates List of Ats and ussid to the data object.
	 * @param response
	 * @return list of pos with product.
	 */
		private List<ProudctWithPointOfServicesData> getProductWdPos(final List<StoreLocationResponseData> response, final Model model)
		{
			LOG.debug("from getProductWdPos method which gets product with pos");
			List<ProudctWithPointOfServicesData> productWithPOS = new ArrayList<ProudctWithPointOfServicesData>();
			final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
			//iterate over response
			for (StoreLocationResponseData storeLocationResponseData : response)
			{
				ProudctWithPointOfServicesData pwPOS = new ProudctWithPointOfServicesData();
				List<PointOfServiceModel> posModelList = new ArrayList<PointOfServiceModel>();
				List<ATSResponseData> listAts = new ArrayList<ATSResponseData>();
				List<PointOfServiceData> posDataList = new ArrayList<PointOfServiceData>();
				String ussId = storeLocationResponseData.getUssId();
				final CartModel cartModel = getCartService().getSessionCart();
				int countStoreQuant = 0;
				//get only those stores which have quantity greater or equal to selected user quantity
				final CartModel cartModel1 = getCartService().getSessionCart();
				for (AbstractOrderEntryModel abstractCartEntry : cartModel1.getEntries())
				{
					if (null != abstractCartEntry)
					{
						if (abstractCartEntry.getSelectedUSSID().equalsIgnoreCase(ussId))
						{
							int quan = abstractCartEntry.getQuantity().intValue();
							for (ATSResponseData ats : storeLocationResponseData.getAts())
							{
								int quant = ats.getQuantity();
								if (quant >= quan)
								{
									listAts.add(ats);
								}
								
							}
						}
					}
				}
				storeLocationResponseData.setAts(listAts);
				
				LOG.debug("call to commerce db to get the seller details");
				final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(ussId);
				if (sellerInfoModel != null)
				{
					String sellerName = sellerInfoModel.getSellerName();
					model.addAttribute("sellerName", sellerName);
					model.addAttribute("sellerId",sellerInfoModel.getSellerID());
					model.addAttribute("ussid", ussId);
					
					final ProductModel productModel = sellerInfoModel.getProductSource();
					final ProductData productData = productFacade.getProductForOptions(productModel,
							Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));
					
					pwPOS.setUssId(ussId);
					LOG.debug("get stores from commerce based on SellerId and StoredId(slaveId)");
					for (int i = 0; i < storeLocationResponseData.getAts().size(); i++)
					{
						PointOfServiceModel posModel = mplSlaveMasterService.findPOSBySellerAndSlave(sellerInfoModel.getSellerID(),storeLocationResponseData.getAts().get(i).getStoreId());
						posModelList.add(posModel);
					}
					for (PointOfServiceModel pointOfServiceModel : posModelList)
					{
						//prepare pos data objects
						PointOfServiceData posData = new PointOfServiceData();
						GeoPoint geo = new GeoPoint();
						AddressData addData = new AddressData();
						if (null != pointOfServiceModel)
						{
							posData.setDisplayName(pointOfServiceModel.getName());
							if (pointOfServiceModel.getLatitude() != null)
							{
								geo.setLatitude(pointOfServiceModel.getLatitude());
							}
							if (pointOfServiceModel.getLongitude() != null)
							{
								geo.setLongitude(pointOfServiceModel.getLongitude());
							}
							posData.setGeoPoint(geo);
							addData = addressConverter.convert(pointOfServiceModel.getAddress());
							posData.setAddress(addData);
							
							/*if (null != pointOfServiceModel.getOpeningTime())
							{
								final String openingTime = pointOfServiceModel.getOpeningTime();
								posData.setMplClosingTime(timeDataConverter.convert(openingTime));
								
							}*/
							/*if (null != pointOfServiceModel.getClosingTime())
							{
								final Date closingTime = pointOfServiceModel.getClosingTime();
								posData.setMplClosingTime(timeDataConverter.convert(closingTime));
								
							}*/
							
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
	 * @author TECH
	 * This is an ajax call to save store for chossen cart entry.
	 * @param productCode
	 * @param posName
	 * @return yes if it saves successfully else no.
	 */
	@ResponseBody
	@RequestMapping(value = "/addPosToOrderEntry", method = RequestMethod.GET)
	public String addPosToOrderEntry( @RequestParam("ussId") final String ussId, @RequestParam("posName") final String posName)
	{
		LOG.debug("from addPosToOrderEntry method ");
		String status = "yes";
	
		//call service to retrieve POSModel for given posName
		final PointOfServiceModel posModel = mplSlaveMasterService.findPOSByName(posName);
		final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(ussId);
		
		try
		{
			final CartModel cartModel = getCartService().getSessionCart();
			if (cartModel != null && cartModel.getEntries() != null)
			{
				for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
				{
					if (cartEntryModel != null)
					{
						String collDays = "";
						if (sellerInfoModel != null)
						{
							final SellerMasterModel sellerMaster = sellerInfoModel.getSellerMaster();
							if (sellerMaster != null)
							{
								collDays = sellerMaster.getCollectionDays();
							}
						}
						if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(ussId))
						{
							if (StringUtils.isNotEmpty(collDays))
							{
								cartEntryModel.setCollectionDays(Integer.valueOf(collDays));
							}
							else
							{
								cartEntryModel.setCollectionDays(Integer.valueOf(0));
							}
							cartEntryModel.setDeliveryPointOfService(posModel);
							modelService.save(cartEntryModel);
							break;
						}
					}
				}
			}
			
		}
		catch (EtailNonBusinessExceptions e)
		{
			LOG.error("Errors while saving pos for cart entry");
			status = "no";
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * This method stores pickup person details to the cart.
	 * @author TECH
	 * @param pickupPersonName
	 * @param pickupPersonMobile
	 * @return success if pickupperson details is successfully saved else returns failure
	 */
	@ResponseBody
	@RequestMapping(value = "/addPickupPersonDetails", method = RequestMethod.GET)
	public String addPickupPersonDetails(@RequestParam("pickupPersonName") final String pickupPersonName,@RequestParam("pickupPersonMobile") final String pickupPersonMobile)
	{
		LOG.debug("from addPickupPersonDetails method, ajax call");
		String status = "";
		//get session cart
		try
		{
			final CartModel cartModel = getCartService().getSessionCart();
			if (null != pickupPersonName)
			{
				cartModel.setPickupPersonName(pickupPersonName);
			}
			if (null != pickupPersonMobile)
			{
				cartModel.setPickupPersonMobile(pickupPersonMobile);
				
			}
			modelService.save(cartModel);
			status = "success";
		}
		catch (Exception e)
		{
			status="failure";
			LOG.error("Exception while saving cartModel to db");
			e.printStackTrace();
		}
		
		return status;
		
	}

	/**
	 *
	 * @param model
	 *           - the id of the delivery mode
	 * @throws CMSItemNotFoundException
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLSELECTSAVEDADDRESS, method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectSavedAddress(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			List<AddressData> deliveryAddress = (List<AddressData>) getMplCustomAddressFacade()
					.getDeliveryAddresses(cartData.getDeliveryAddress());
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
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
					.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while showing saved address :" + e);
		}
		model.addAttribute("checkoutPageName", selectAddress);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;

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

			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
					.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
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
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
					.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
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
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);
			final List<StateData> stateDataList = accountAddressFacade.getStates();
			List<AddressData> deliveryAddress = (List<AddressData>) getMplCustomAddressFacade()
					.getDeliveryAddresses(cartData.getDeliveryAddress());
			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;

			final CartModel cartModel = getCartService().getSessionCart();

			for (final AddressModel userAddress : cartModel.getUser().getAddresses())
			{
				if (userAddress != null && userAddress.getVisibleInAddressBook() != null
						&& userAddress.getVisibleInAddressBook().booleanValue() == true)
				{
					addressFlag = "T";
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
			timeOutSet(model);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
					.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());

		}
		catch (final Exception ex)
		{
			LOG.error("Exception occured while showing new address:", ex);
		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
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
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Exception occured while selecting  address:" + e);
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
	public String add(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{
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
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
						.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.TRUE);
				timeOutSet(model);
				return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
			}

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
			accountAddressFacade.addaddress(newAddress);
			getMplCustomAddressFacade().setDeliveryAddress(newAddress);
			//TISUTO-12 , TISUTO-11
			final String redirectURL = getMplCartFacade().checkPincodeAndInventory(addressForm.getPostcode());
			if (redirectURL.trim().length() > 0)
			{
				return redirectURL;
			}

			//Recalculating Cart Model
			LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
			getMplCheckoutFacade().reCalculateCart(cartData);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Exception occured while saving new address :" + e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :" + e);
		}
		return getCheckoutStep().nextStep();
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
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder()
						.getBreadcrumbs(MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
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
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("EtailNonBusinessExceptions occured while saving new address :" + e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :" + e);
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

		LOG.info("deliveryCost " + deliveryCost);
		//TISST-13010
		Double discountValue = Double.valueOf(0.0);
		getMplCartFacade().removeDeliveryMode(getCartService().getSessionCart());

		final Double subTotal = getCartService().getSessionCart().getSubtotal();

		final CartModel cartModel = getCartService().getSessionCart();
		final CartData cartData = mplExtendedCartConverter.convert(cartModel);

		if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
		{
			discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
		}
		LOG.info("discountValue  " + discountValue);
		final Double totalPriceAfterDeliveryCharge = Double
				.valueOf(subTotal.doubleValue() + Double.parseDouble(deliveryCost) - discountValue.doubleValue());
		final DecimalFormat df = new DecimalFormat("#.00");
		final String totalPriceFormatted = df.format(totalPriceAfterDeliveryCharge);
		final String formatDeliveryCost = df.format(Double.valueOf(deliveryCost));
		return getCartService().getSessionCart().getCurrency().getSymbol() + MarketplacecheckoutaddonConstants.STRINGSEPARATOR
				+ formatDeliveryCost + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + totalPriceFormatted;

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


	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
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
			LOG.error(" Exception in applyPromotions due to " + e);
		}
	}


	/**
	 * This method gets store locations for given seller.
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
			storeList = pincodeService.getSortedLocationsNearby(gps, configurableRadius, sellerId);
		}
		catch (Exception e)
		{
			storeLocationRequestData.setStoreId(null);
			LOG.error("Exception while retrieving all the stores based on gps,sellerId and radius");
		}
		if (null != storeList && storeList.size() > 0)
		{
			final List<String> locationList = new ArrayList<String>();
			for (final Location location : storeList)
			{
				locationList.add(location.getName());
			}
			LOG.debug("Total number of Stores :" + locationList.size() + " for Seller" +sellerId);
			storeLocationRequestData.setStoreId(locationList);
		}
		storeLocationRequestData.setUssId(sellerUssId);
		//populate newly added fields
		//get SellerInfo based on sellerUssid
		final SellerInformationModel sellerInfoModel = mplSellerInformationService
				.getSellerDetail(sellerUssId);
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
				LOG.debug("storeLocationRequestData :  Fulfillment type not received for the SellerId"+sellerInfoModel.getSellerArticleSKU());
			}
			if (richAttributeModel != null && richAttributeModel.get(0) != null
					&& richAttributeModel.get(0).getShippingModes() != null
					&& richAttributeModel.get(0).getShippingModes().getCode() != null)
			{
				final String shippingMode = richAttributeModel.get(0).getShippingModes().getCode();
				storeLocationRequestData.setTransportMode(shippingMode.toUpperCase());
			}
			else
			{
				LOG.debug("storeLocationRequestData :  ShippingMode type not received for the "+sellerInfoModel.getSellerArticleSKU());
			}
			if ((null != productData.getSeller()) && (null != productData.getSeller().get(0)) && (null != productData.getSeller().get(0).getSpPrice()) && !(productData.getSeller().get(0).getSpPrice().equals("")))
			{
				storeLocationRequestData.setPrice(productData.getSeller().get(0).getSpPrice().getValue().doubleValue());
			}
			else if ((null != productData.getSeller()) && (null != productData.getSeller().get(0)) && (null != productData.getSeller().get(0).getMopPrice()) && !(productData.getSeller().get(0).getMopPrice().equals("")))
			{
				storeLocationRequestData.setPrice(productData.getSeller().get(0).getMopPrice().getValue().doubleValue());
			}
			else if (null != productData.getSeller().get(0).getMrpPrice() && !(productData.getSeller().get(0).getMrpPrice().equals("")))
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
			@RequestParam(value = "productCode") final String productCode, @RequestParam(value = "sellerId") final String ussId, final Model model)
			throws CMSItemNotFoundException
	{
		LOG.debug("from upDatePincodeServicabilityCheck method when customer change pincode at cnc page");
		List<PointOfServiceData> stores = new ArrayList<PointOfServiceData>();
		List<StoreLocationResponseData> omsResponse = new ArrayList<StoreLocationResponseData>();
		List<ProudctWithPointOfServicesData> productWithPOS = new ArrayList<ProudctWithPointOfServicesData>();
		//call to check pincode serviceability
		boolean status = false;

		//call service to get list of ATS and ussid
		omsResponse = pincodeServiceFacade.getListofStoreLocationsforPincode(pin, ussId, productCode);
		if (null != omsResponse && omsResponse.size() > 0)
		{
			productWithPOS = getProductWdPos(omsResponse,model);
		}
		
		if (productWithPOS.size() > 0)
		{
			status = true;
			stores = productWithPOS.get(0).getPointOfServices();
		}

		if (! status)
		{
			stores.clear();
			return stores;
		}
		
		return stores;
	}

	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYCNCINVRESV)
	@RequireHardLogIn
	public String cncCartReservation(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		/*** Inventory Soft Reservation Start ***/

		final boolean inventoryReservationStatus = getMplCartFacade()
				.isInventoryReserved(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART);
		if (!inventoryReservationStatus)
		{
			getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
		}
		/*** Inventory Soft Reservation Start ***/
		if (inventoryReservationStatus)
		{
			return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/payment-method/add";
		}
		return null;
	}

	 /**
	  * populating the request data to be send to oms
	  *
	  * @param productCode
	  * @param finalStoreList
	  * @return requestData
	  */
	 private List<PincodeServiceData> populatePinCodeServiceData(final String productCode, final GPS gps,
	   final Double configurableRadius)
	 {

	  final List<PincodeServiceData> requestData = new ArrayList<PincodeServiceData>();
	  PincodeServiceData data = null;
	  MarketplaceDeliveryModeData deliveryModeData = null;
	  try
	  {
	   final ProductModel productModel = productService.getProductForCode(productCode);
	   final ProductData productData = productFacade.getProductForOptions(productModel,
	     Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE));

	   for (final SellerInformationData seller : productData.getSeller())
	   {
	    final List<MarketplaceDeliveryModeData> deliveryModeList = new ArrayList<MarketplaceDeliveryModeData>();
	    data = new PincodeServiceData();
	    if ((null != seller.getDeliveryModes()) && !(seller.getDeliveryModes().isEmpty()))
	    {
	     for (final MarketplaceDeliveryModeData deliveryMode : seller.getDeliveryModes())
	     {
	      deliveryModeData = fetchDeliveryModeDataForUSSID(deliveryMode.getCode(), seller.getUssid());
	      deliveryModeList.add(deliveryModeData);
	     }
	     data.setDeliveryModes(deliveryModeList);
	    }
	    if (null != seller.getFullfillment() && StringUtils.isNotEmpty(seller.getFullfillment()))
	    {
	     data.setFullFillmentType(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getFullfillment().toUpperCase()));
	    }
	    if (null != seller.getShippingMode() && (StringUtils.isNotEmpty(seller.getShippingMode())))
	    {
	     data.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(seller.getShippingMode().toUpperCase()));
	    }
	    if (null != seller.getSpPrice() && !(seller.getSpPrice().equals(ModelAttributetConstants.EMPTY)))
	    {
	     data.setPrice(new Double(seller.getSpPrice().getValue().doubleValue()));
	    }
	    else if (null != seller.getMopPrice() && !(seller.getMopPrice().equals(ModelAttributetConstants.EMPTY)))
	    {
	     data.setPrice(new Double(seller.getMopPrice().getValue().doubleValue()));
	    }
	    else if (null != seller.getMrpPrice() && !(seller.getMrpPrice().equals(ModelAttributetConstants.EMPTY)))
	    {
	     data.setPrice(new Double(seller.getMrpPrice().getValue().doubleValue()));
	    }
	    else
	    {
	     LOG.debug("No price avaiable for seller :" + seller.getSellerID());
	     continue;
	    }
	    if (null != seller.getIsCod() && StringUtils.isNotEmpty(seller.getIsCod()))
	    {
	     data.setIsCOD(seller.getIsCod());
	    }

	    LOG.debug("Current locations for Seller Id**********" + seller.getSellerID());
	    @SuppressWarnings("boxing")
	    final List<Location> storeList = pincodeService.getSortedLocationsNearby(gps, configurableRadius,
	      seller.getSellerID());
	    LOG.debug("StoreList size is :" + storeList.size());
	    if (storeList.size() > 0)
	    {
	     final List<String> locationList = new ArrayList<String>();
	     for (final Location location : storeList)
	     {
	      locationList.add(location.getName());
	     }
	     LOG.debug("locationList:" + locationList.size());
	     data.setStore(locationList);
	    }
	    data.setSellerId(seller.getSellerID());
	    LOG.debug("seller.getSellerID():" + seller.getSellerID());
	    LOG.debug("seller.getUssid():" + seller.getUssid());
	    LOG.debug("seller.getUssid():" + seller.getUssid());
	    data.setUssid(seller.getUssid());
	    data.setIsDeliveryDateRequired(ControllerConstants.Views.Fragments.Product.N);
	    requestData.add(data);
	   }
	  }
	  catch (final EtailBusinessExceptions e)
	  {
	   ExceptionUtil.etailBusinessExceptionHandler(e, null);
	  }

	  catch (final Exception e)
	  {

	   throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
	  }
	  return requestData;
	 }

	 /**
	  * @param deliveryMode
	  * @param ussid
	  * @return deliveryModeData
	  */
	 private MarketplaceDeliveryModeData fetchDeliveryModeDataForUSSID(final String deliveryMode, final String ussid)
	 {
	  final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
	  final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = mplCheckoutFacade
	    .populateDeliveryCostForUSSIDAndDeliveryMode(deliveryMode, MarketplaceFacadesConstants.INR, ussid);

	  final PriceData priceData = productDetailsHelper.formPriceData(mplZoneDeliveryModeValueModel.getValue());
	  deliveryModeData.setCode(mplZoneDeliveryModeValueModel.getDeliveryMode().getCode());
	  deliveryModeData.setDescription(mplZoneDeliveryModeValueModel.getDeliveryMode().getDescription());
	  deliveryModeData.setName(mplZoneDeliveryModeValueModel.getDeliveryMode().getName());
	  deliveryModeData.setSellerArticleSKU(ussid);
	  deliveryModeData.setDeliveryCost(priceData);
	  return deliveryModeData;
	 }
	
	
	/**
	 * @param model
	 */
	private void timeOutSet(final Model model)
	{
		// YTODO Auto-generated method stub
		final double deliveryMode = Double
				.parseDouble(configurationService.getConfiguration().getString("checkout.deliverymode.timeout"));
		final String timeOut = Double.toString(deliveryMode * 60 * 1000);
		model.addAttribute(MarketplacecheckoutaddonConstants.TIMEOUT, timeOut);
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

}
