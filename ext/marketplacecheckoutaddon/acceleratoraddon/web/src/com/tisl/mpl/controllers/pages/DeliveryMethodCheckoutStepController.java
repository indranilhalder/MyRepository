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
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
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

import com.granule.json.JSONObject;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.tisl.lux.facade.CommonUtils;
import com.tisl.mpl.checkout.form.DeliveryMethodEntry;
import com.tisl.mpl.checkout.form.DeliveryMethodForm;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.util.DateUtilHelper;
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
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.facades.data.ProudctWithPointOfServicesData;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.sellerinfo.facades.MplSellerInformationFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.security.cookie.PDPPincodeCookieGenerator;
import com.tisl.mpl.storefront.util.CSRFTokenManager;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.MaxLimitData;


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

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = ModelAttributetConstants.ACCELERATOR_CHECKOUT_FACADE)
	private CheckoutFacade checkoutFacade;

	@Resource(name = "mplSellerInformationService")
	private MplSellerInformationService mplSellerInformationService;

	@Resource(name = "pdpPincodeCookieGenerator")
	private PDPPincodeCookieGenerator pdpPincodeCookie;

	@Autowired
	private MplAccountAddressFacade accountAddressFacade;

	@Autowired
	private MplAddressValidator mplAddressValidator;

	@Autowired
	private CartService cartService;

	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private CommonUtils utils;

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

	@Autowired
	private DateUtilHelper dateUtilHelper;

	@Autowired
	ProductService productService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

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
			//JWLSPCUAT-68 - Redirecting to one page if by chance this method is called
			//Luxury site still continues on multi step checkout
			if (!utils.isLuxurySite() && Boolean.valueOf(MarketplacecheckoutaddonConstants.TRUE).booleanValue())
			{
				return getCheckoutStep().previousStep();//This will take you to single page
			}

			final CartModel serviceCart = getCartService().getSessionCart();
			setExpressCheckout(serviceCart);

			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(serviceCart); //TISPT-104
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			//TPR-5346 STARTS
			//This method will update the cart with respect to the max quantity configured for the product
			//final boolean updateCartOnMaxLimExceeds = getMplCartFacade().UpdateCartOnMaxLimExceeds(serviceCart);
			final Map<String, MaxLimitData> updateCartOnMaxLimExceeds = getMplCartFacade().updateCartOnMaxLimExceeds(serviceCart);
			if (MapUtils.isNotEmpty(updateCartOnMaxLimExceeds) && updateCartOnMaxLimExceeds.size() > 0)
			{
				//	redirectAttributes.addFlashAttribute("updateCartOnMaxLimExceeds", Boolean.valueOf(updateCartOnMaxLimExceeds));
				String errorMsg = null;
				final Map<String, String> msgMap = new HashMap<String, String>();
				if (CollectionUtils.isNotEmpty(serviceCart.getEntries()))
				{
					for (final AbstractOrderEntryModel orderEntry : serviceCart.getEntries())
					{

						if (null != orderEntry.getProduct() && null != orderEntry.getProduct().getName()
								&& null != orderEntry.getProduct().getMaxOrderQuantity())
						{
							errorMsg = MarketplacecommerceservicesConstants.PRECOUNTMSG
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE + orderEntry.getProduct().getName().toString()
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE + MarketplacecommerceservicesConstants.MIDCOUNTMSG
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE
									+ orderEntry.getProduct().getMaxOrderQuantity().toString()
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE
									+ MarketplacecommerceservicesConstants.LASTCOUNTMSG;
							msgMap.put(orderEntry.getProduct().getCode(), errorMsg);
						}
					}
				}
				if (MapUtils.isNotEmpty(msgMap))
				{
					for (final Map.Entry<String, String> entry : msgMap.entrySet())
					{
						if (updateCartOnMaxLimExceeds.containsKey(entry.getKey()))
						{
							GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER, entry.getValue());
						}
					}
				}
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			//TPR-5346 ENDS

			//TISBOX-1618
			//final CartModel cartModel = getCartService().getSessionCart();
			if (!utils.isLuxurySite())
			{
				getMplCouponFacade().releaseVoucherInCheckout(serviceCart);
				//TISPT-104 // Cart recalculation method invoked inside this method
				//applyPromotions();
			}
			getMplCartFacade().removeDeliveryMode(serviceCart);

			final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);

			timeOutSet(model);
			if (StringUtils.isNotEmpty(defaultPinCodeId) && (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries())))
			{
				//CAR-126/128/129
				responseData = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
				if (CollectionUtils.isEmpty(responseData))
				{
					responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartData, serviceCart);
				}
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
							pinCodeResponseData = getMplCartFacade().getVlaidDeliveryModesByInventory(pinCodeResponseData, cartData);
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
				//deliveryModeDataMap = mplCheckoutFacade.repopulateTshipDeliveryCost(deliveryModeDataMap, cartData);

				model.addAttribute("deliveryModeData", deliveryModeDataMap);
				model.addAttribute("deliveryMethodForm", new DeliveryMethodForm());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
				model.addAttribute("defaultPincode", defaultPinCodeId);

				//INC144316212
				final String isDeliveryOptionPage = "yes";
				model.addAttribute(MarketplacecheckoutaddonConstants.IS_DELIVERY_OPTION_PAGE, isDeliveryOptionPage);

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
			final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		//TISPT-400
		if (getUserFacade().isAnonymousUser())
		{
			return getCheckoutStep().previousStep();
		}
		//JWLSPCUAT-68 - Redirecting to one page if by chance this method is called
		//Luxury site still continues on multi step checkout
		if (!utils.isLuxurySite() && Boolean.valueOf(MarketplacecheckoutaddonConstants.TRUE).booleanValue())
		{
			return getCheckoutStep().previousStep();//This will take you to single page
		}

		String returnPage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			final CartModel cartModel = getCartService().getSessionCart();

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
			//INC144313695
			final HttpSession session = request.getSession();
			deliveryMethodForm = (DeliveryMethodForm) session.getAttribute("deliveryMethodForm");

			//prdi-36/INC144315559
			String deliveryCode = null;
			final int deliveryModeCount = deliveryMethodForm.getDeliveryMethodEntry().size();
			int deliveryModeMatch = 0;
			if (deliveryMethodForm.getDeliveryMethodEntry() != null && !deliveryMethodForm.getDeliveryMethodEntry().isEmpty())
			{


				for (final DeliveryMethodEntry deliveryEntry : deliveryMethodForm.getDeliveryMethodEntry())
				{
					deliveryCode = deliveryEntry.getDeliveryCode();

					if (StringUtils.isNotEmpty(deliveryCode)
							&& deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT)) // Code optimized as part of performance fix TISPT-104
					{
						deliveryModeMatch++;
					}
				}
				//TISSQAEE-1121
				if (deliveryModeMatch == deliveryModeCount)
				{
					return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method"
							+ MarketplacecheckoutaddonConstants.MPLDELIVERYCHOOSEURL;
				}
			}
			// end prdi-36/INC144315559

			if (deliveryMethodForm.getDeliveryMethodEntry() == null)
			{
				//if cart contains cnc and home/express delivery modes
				//count++;
				//INC144313695
				//final HttpSession session = request.getSession();
				//deliveryMethodForm = (DeliveryMethodForm) session.getAttribute("deliveryMethodForm");
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
			//TPR3780 STARTS HERE
			final CartModel cartModelBeforeinventoryCheck = getCartService().getSessionCart();
			final double prevTotalCartPrice = cartModelBeforeinventoryCheck.getTotalPrice().doubleValue();
			//TPR3780 ENDS HERE

			//commented for CAR:127

			/*
			 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
			 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartModel);
			 */

			//CAR:127
			//final CartData caData = getMplCartFacade().getCartDataFromCartModel(cartModel, false);
			CartData cartUssidData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			//TISST-13012
			final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartUssidData, cartModel);
			/* MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, null); */

			//TPR3780 STARTS HERE
			final CartModel cartModelAfterinventoryCheck = getCartService().getSessionCart();
			final double newTotalCartPrice = cartModelAfterinventoryCheck.getTotalPrice().doubleValue();

			if (prevTotalCartPrice == newTotalCartPrice)
			{
				final String updateStatus = null;
			}
			else
			{
				//final String updateStatus = "updated";
				final String updateStatus = MarketplacecheckoutaddonConstants.UPDATED;
				final String totalCartPriceAsString = String.valueOf(newTotalCartPrice);
				redirectAttributes.addFlashAttribute("flashupdateStatus", updateStatus);
				redirectAttributes.addFlashAttribute("flashtotalCartPriceAsString", totalCartPriceAsString);
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			//TPR3780 ENDS HERE
			if (!inventoryReservationStatus)
			{
				getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			/*** Inventory Soft Reservation Start ***/

			LOG.debug(">>>>>>>>>>  Step 4:  Inventory soft reservation status  " + inventoryReservationStatus);
			Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
			List<PinCodeResponseData> responseData = null;

			//commented for CAR:127
			//final CartData cartUssidData = getMplCartFacade().getSessionCartWithEntryOrdering(true);

			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);


			if (StringUtils.isNotEmpty(defaultPinCodeId)
					&& (cartUssidData != null && cartUssidData.getEntries() != null && !cartUssidData.getEntries().isEmpty()))
			{
				//CAR-126/128/129
				responseData = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
				if (CollectionUtils.isEmpty(responseData))
				{
					responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartUssidData, cartModel);
				}
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartUssidData, responseData, cartModel);

				getMplCartFacade().setDeliveryDate(cartUssidData, responseData);
				//TISUTO-72 TISST-6994,TISST-6990 set cart COD eligible
				final boolean isCOdEligible = getMplCartFacade().addCartCodEligible(deliveryModeDataMap, responseData, cartModel,
						cartUssidData);
				LOG.info("isCOdEligible " + isCOdEligible);

			}

			LOG.debug(">>>>>>>>>>  Step 5:  Cod status setting done  ");

			// TPR-429 START
			final String cartLevelSellerID = populateCheckoutSellers(cartUssidData);

			model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, cartLevelSellerID);
			// TPR-429 END



			//CAR-130
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			cartUssidData = getMplCustomAddressFacade().getCheckoutCart();
			List<AddressData> deliveryAddress = null;

			//TIS 391:  In case of normal checkout , delivery address will be null and for express checkout delivery address will be ore selected from the cart page
			// In case of express checkout it will redirect to the payment page from delivery mode selection

			if (cartData != null)
			{
				if (cartData.getDeliveryAddress() != null)
				{
					LOG.debug("Express checkout selected for address id : " + cartData.getDeliveryAddress().getId());

					//Added the code for Delivery Method slots
					final CartData cartDataSupport = getMplCartFacade().getSessionCartWithEntryOrdering(true);
					final List<String> deliveryModelList = new ArrayList<String>();
					for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
					{
						if (null != cartEntryData && null != cartEntryData.getMplDeliveryMode())
						{
							if (cartEntryData.getMplDeliveryMode().getCode()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
							{
								deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode());
							}
							else if (cartEntryData.getMplDeliveryMode().getCode()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
							{
								deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode());
							}
						}
					}
					if (deliveryModelList.size() > 0)
					{
						LOG.debug("****************:" + MarketplacecommerceservicesConstants.REDIRECT
								+ MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL
								+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL);
						/*
						 * return MarketplacecommerceservicesConstants.REDIRECT +
						 * MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL +
						 * MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL;
						 */
						/**** PRDI-36/INC144315559 *******/
						model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSID, cartUssidData.getDeliveryAddress()
								.getId());

					}
					else
					{
						return getCheckoutStep().nextStep();
					}

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

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartUssidData);
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
			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartUssidData);
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
	//@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYCHECKURL, method = RequestMethod.POST)
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYCHECKURL)
	@RequireHardLogIn
	public String doFindDelivaryMode(final DeliveryMethodForm deliveryMethodForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request,
			final HttpServletResponse response1, final HttpSession session) throws CMSItemNotFoundException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from doFindDelivaryMode methodin Controller");
		}
		//JWLSPCUAT-68 - Redirecting to one page if by chance this method is called
		//Luxury site still continues on multi step checkout
		if (!utils.isLuxurySite() && Boolean.valueOf(MarketplacecheckoutaddonConstants.TRUE).booleanValue())
		{
			return getCheckoutStep().previousStep();//This will take you to single page
		}

		//INC144315801 starts
		if (deliveryMethodForm.getDeliveryMethodEntry() == null)
		{
			return getCheckoutStep().previousStep();
		}
		//INC144315801 ends

		//TISPT-400

		if (getCartService().hasSessionCart())
		{
			final CartModel cartModel = getCartService().getSessionCart();


			//CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);

			try
			{
				//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cartData); //CAR-197
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


			// CAR-197  start
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart(); //CAR-197

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
			if (cartData != null && cartData.getEntries() != null) //CAR-197
			{
				//for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
				for (final OrderEntryData cartD : cartData.getEntries())
				{
					//					if (null != cartD && !cartD.isGiveAway() && null != deliveryMethodForm //CAR-197
					//							&& CollectionUtils.isNotEmpty(deliveryMethodForm.getDeliveryMethodEntry()))

					/*
					 * if (null != cartEntryModel && null != cartEntryModel.getGiveAway() &&
					 * !cartEntryModel.getGiveAway().booleanValue() && null != deliveryMethodForm &&
					 * CollectionUtils.isNotEmpty(deliveryMethodForm.getDeliveryMethodEntry()))
					 */
					if (null != cartD && !cartD.isGiveAway()
							&& CollectionUtils.isNotEmpty(deliveryMethodForm.getDeliveryMethodEntry())) //CAR-197
					{
						try
						{
							//final ProductModel productModel = cartEntryModel.getProduct(); / Performance fix TISPT-104
							//final ProductData productData = productFacade.getProductForOptions(productModel,
							//		Arrays.asList(ProductOption.BASIC, ProductOption.SELLER, ProductOption.PRICE)); // Performance fix TISPT-104

							/*
							 * final String deliveryCode = deliveryMethodForm.getDeliveryMethodEntry()
							 * .get(cartEntryModel.getEntryNumber().intValue()).getDeliveryCode();
							 */
							final String deliveryCode = deliveryMethodForm.getDeliveryMethodEntry()
									.get(cartD.getEntryNumber().intValue()).getDeliveryCode();
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


								/*
								 * final StoreLocationRequestData storeLocationRequestData = papulateClicknCollectRequesrData(
								 * cartEntryModel.getSelectedUSSID(), myLocation.getGPS(), Double.valueOf(configurableRadius));
								 */



								final StoreLocationRequestData storeLocationRequestData = papulateClicknCollectRequesrData(
										cartD.getSelectedUssid(), myLocation.getGPS(), Double.valueOf(configurableRadius));
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
							 * TISPT-104 if (StringUtils.isNotEmpty(deliveryCode)) { Double deliveryCost =
							 * Double.valueOf(0.00D); if
							 * (deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT)) {
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
							getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID,
									"TRUE");
						}
						catch (final Exception e)
						{
							ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
									MarketplacecommerceservicesConstants.E0000));
							getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID,
									"TRUE");
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
		else
		{
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
		}
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
				if (null != storeLocationRequestData.getStoreId())
				{
					for (int i = 0; i < storeLocationRequestData.getStoreId().size(); i++)
					{
						if (i == 3)
						{
							break;
						}
						final PointOfServiceModel posModel = mplSlaveMasterFacade.findPOSBySellerAndSlave(
								sellerInfoModel.getSellerID(), storeLocationRequestData.getStoreId().get(i));
						posModelList.add(posModel);
					}
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
		final CartModel cartModel = getCartService().getSessionCart(); //CAR-194
		String returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			List<AddressData> deliveryAddress = null;
			if (null != cartData)
			{
				deliveryAddress = getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress(), cartModel); //CAR-194
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
					//changes TPR-3402
					//TISUATSE-73 starts
					String addressLine1 = addressData.getLine1();
					String addressLine2 = "";
					String addressLine3 = "";

					if (StringUtils.isNotEmpty(addressData.getLine2()))
					{
						addressLine2 = addressData.getLine2();
						addressLine1 = addressLine1 + addressLine2;
					}
					if (StringUtils.isNotEmpty(addressData.getLine3()))
					{
						addressLine3 = addressData.getLine3();
						addressLine1 = addressLine1 + addressLine3;
					}
					//TISUATSE-73 ends
					//					if (addressLine2.length() > 0)
					//					{
					//						addressLine1 = addressLine1 + addressLine2;
					//					}
					//					if (addressLine3.length() > 0)
					//					{
					//						addressLine1 = addressLine1 + addressLine3;
					//					}
					addressForm.setLine1(addressLine1);
					//addressForm.setLine1(addressData.getLine1());
					//addressForm.setLine2(addressData.getLine2());
					addressForm.setTownCity(addressData.getTown());
					addressForm.setPostcode(addressData.getPostalCode());
					addressForm.setCountryIso(addressData.getCountry().getIsocode());
					addressForm.setAddressType(addressData.getAddressType());
					addressForm.setMobileNo(addressData.getPhone());
					addressForm.setState(addressData.getState());
					//addressForm.setLine3(addressData.getLine3());
					addressForm.setLocality(addressData.getLocality());
					addressForm.setLandmark(addressData.getLandmark());

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
			LOG.error("+++++++++++Error in edit address 1111+++++++++++", e);
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
			final CartModel cartModel = getCartService().getSessionCart();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);
			final List<StateData> stateDataList = accountAddressFacade.getStates();

			if (null != cartData)
			{
				deliveryAddress = getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress(), cartModel); //CAR-194
			}


			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;

			//final CartModel cartModel = getCartService().getSessionCart();
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
			//Added the code for Delivery Method slots
			final CartData cartDataSupport = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			final List<String> deliveryModelList = new ArrayList<String>();
			for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
			{
				if (null != cartEntryData && null != cartEntryData.getMplDeliveryMode())
				{
					if (cartEntryData.getMplDeliveryMode().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
					{
						deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode());
					}
					else if (cartEntryData.getMplDeliveryMode().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
					{
						deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode());
					}
				}
			}
			if (deliveryModelList.size() > 0)
			{
				LOG.debug("****************:" + MarketplacecommerceservicesConstants.REDIRECT
						+ MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL
						+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL);
				return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL
						+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL;
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



	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String selectDeleverySlots(final Model model) throws CMSItemNotFoundException
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		try
		{

			final CartData cartDataSupport = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			final List<String> deliveryModelList = new ArrayList<String>();
			final Map<String, String> selectedDateBetWeen = new LinkedHashMap<String, String>();
			final List<CartSoftReservationData> cartSoftReservationDataList = getSessionService().getAttribute(
					MarketplacecommerceservicesConstants.RESERVATION_DATA_TO_SESSION);
			LOG.debug("****************cartSoftReservationDataList :" + cartSoftReservationDataList.toString());

			for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
			{
				if (null != cartEntryData && null != cartEntryData.getMplDeliveryMode())
				{

					final ProductModel productModel = productService.getProductForCode(cartEntryData.getProduct().getCode());

					String productRichAttr = null;
					String sellerRichAttr = null;
					List<RichAttributeModel> productRichAttributeModel = null;
					if (productModel != null && productModel.getRichAttribute() != null)
					{
						productRichAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();
						if (productRichAttributeModel != null && !productRichAttributeModel.isEmpty()
								&& productRichAttributeModel.get(0).getScheduledDelivery() != null)
						{
							productRichAttr = productRichAttributeModel.get(0).getScheduledDelivery().toString();
						}
					}

					final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(cartEntryData
							.getSelectedUssid());
					List<RichAttributeModel> sellerRichAttributeModel = null;
					if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
					{
						sellerRichAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
						if (sellerRichAttributeModel != null && sellerRichAttributeModel.get(0).getScheduledDelivery() != null)
						{
							sellerRichAttr = sellerRichAttributeModel.get(0).getScheduledDelivery().toString();
						}
					}

					if (cartEntryData.getMplDeliveryMode().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
					{

						for (final CartSoftReservationData cartSoftReservationData : cartSoftReservationDataList)
						{
							if (cartSoftReservationData.getUSSID().equals(cartEntryData.getSelectedUssid()))
							{

								deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode()
										+ MarketplacecommerceservicesConstants.COMMA + productRichAttr
										+ MarketplacecommerceservicesConstants.COMMA + sellerRichAttr
										+ MarketplacecommerceservicesConstants.COMMA + cartEntryData.getSelectedUssid()
										+ MarketplacecommerceservicesConstants.COMMA + cartSoftReservationData.getFulfillmentType());
							}
						}
					}
					else if (cartEntryData.getMplDeliveryMode().getCode()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
					{
						for (final CartSoftReservationData cartSoftReservationData : cartSoftReservationDataList)
						{
							if (cartSoftReservationData.getUSSID().equals(cartEntryData.getSelectedUssid()))
							{

								deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode()
										+ MarketplacecommerceservicesConstants.COMMA + productRichAttr
										+ MarketplacecommerceservicesConstants.COMMA + sellerRichAttr
										+ MarketplacecommerceservicesConstants.COMMA + cartEntryData.getSelectedUssid()
										+ MarketplacecommerceservicesConstants.COMMA + cartSoftReservationData.getFulfillmentType());
							}
						}
					}
				}
			}

			boolean isScheduleServiceble = false;
			if (deliveryModelList.size() > 0)
			{
				String selectedDeliveryMode = null;
				String productScheduleDelivery = null;
				String sellerScheduleDelivery = null;
				String selectedUssId = null;
				String fulfillmentType = null;
				List<String> dateList;
				final CartModel cartModel = getCartService().getSessionCart();
				final InvReserForDeliverySlotsRequestData deliverySlotsRequestData = new InvReserForDeliverySlotsRequestData();
				deliverySlotsRequestData.setCartId(cartDataSupport.getGuid());
				final InvReserForDeliverySlotsResponseData deliverySlotsResponseData = getMplCartFacade()
						.convertDeliverySlotsDatatoWsdto(deliverySlotsRequestData, cartModel);
				final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
				final MplLPHolidaysModel mplLPHolidaysModel = mplConfigFacade
						.getMplLPHolidays(MarketplacecommerceservicesConstants.CAMPAIGN_URL_ALL);
				final Map<String, List<String>> dateTimeslotMapList = new HashMap<String, List<String>>();



				final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
				for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
				{
					if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
					{
						if (null != cartEntryModel.getEdScheduledDate() && StringUtils.isNotEmpty(cartEntryModel.getEdScheduledDate()))
						{
							cartEntryModel.setEdScheduledDate("".trim());
							cartEntryModel.setTimeSlotFrom("".trim());
							cartEntryModel.setTimeSlotTo("".trim());

						}
					}
				}
				modelService.saveAll(cartEntryList);

				for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
				{
					if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
					{
						if (null != deliverySlotsResponseData)
						{
							for (final InvReserForDeliverySlotsItemEDDInfoData responseData : deliverySlotsResponseData
									.getInvReserForDeliverySlotsItemEDDInfoData())
							{
								if (responseData.getUssId().equalsIgnoreCase(cartEntryModel.getSelectedUSSID()))
								{
									String estDeliveryDateAndTime = null;
									if (responseData.getEDD() != null && StringUtils.isNotEmpty(responseData.getEDD()))
									{
										estDeliveryDateAndTime = responseData.getEDD();
									}
									else if (responseData.getNextEDD() != null && StringUtils.isNotEmpty(responseData.getNextEDD()))
									{
										estDeliveryDateAndTime = responseData.getNextEDD();
									}
									if (null != estDeliveryDateAndTime && StringUtils.isNotEmpty(estDeliveryDateAndTime))
									{
										final SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
										final Date dateForEDD = parseFormat.parse(estDeliveryDateAndTime);
										cartEntryModel.setExpectedDeliveryDate(dateForEDD);
									}
								}
							}
						}
					}
				}
				modelService.saveAll(cartEntryList);

				for (final String selectedDeliveryModes : deliveryModelList)
				{
					final String[] splitString = selectedDeliveryModes.split(MarketplacecommerceservicesConstants.COMMA);
					if (null != splitString && !splitString.equals(""))
					{
						selectedDeliveryMode = splitString[0];
						productScheduleDelivery = splitString[1];
						sellerScheduleDelivery = splitString[2];
						selectedUssId = splitString[3];
						fulfillmentType = splitString[4];
					}
					LOG.debug("DeliveryMode :" + selectedDeliveryMode + "-- ProductScheduleDelivery" + productScheduleDelivery
							+ "-- SellerScheduleDelivery" + sellerScheduleDelivery + "-- selectedUssId" + selectedUssId);
					for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
					{
						if (!cartEntryData.isIsBOGOapplied() || !cartEntryData.isGiveAway())
						{
							if ((cartEntryData.getSelectedUssid().equalsIgnoreCase(selectedUssId)
									&& selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)
									&& productScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES) && sellerScheduleDelivery
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES))
									|| (cartEntryData.getSelectedUssid().equalsIgnoreCase(selectedUssId)
											&& selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)
											&& productScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES) && sellerScheduleDelivery
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)))
							{
								for (final InvReserForDeliverySlotsItemEDDInfoData deliverySlotsResponse : dateUtilHelper
										.getUniqueEddDatesList(deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData()))
								{
									if (cartEntryData.getSelectedUssid().equalsIgnoreCase(deliverySlotsResponse.getUssId()))
									{
										if (cartEntryData.getSelectedUssid().equalsIgnoreCase(deliverySlotsResponse.getUssId())
												&& fulfillmentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP.toUpperCase()))
										{

											mplCheckoutFacade.constructDeliverySlotsForEDAndHD(deliverySlotsResponse, cartEntryData,
													mplLPHolidaysModel);
											dateList = new ArrayList<String>();
											if (null != cartEntryData.getDeliverySlotsTime()
													&& cartEntryData.getDeliverySlotsTime().size() > 0)
											{

												for (final Entry<String, List<String>> entry : cartEntryData.getDeliverySlotsTime()
														.entrySet())
												{
													dateList.add(entry.getKey());
												}
											}
											String startAndEndDates = null;
											if (dateList.size() > 0)
											{
												startAndEndDates = dateList.get(0) + MarketplacecommerceservicesConstants.AND
														+ dateList.get(dateList.size() - 1);

												selectedDateBetWeen.put(cartEntryData.getSelectedUssid(), startAndEndDates);
											}
											if (deliverySlotsResponse.getIsScheduled().equalsIgnoreCase(
													MarketplacecommerceservicesConstants.Y))
											{
												isScheduleServiceble = true;

											}
											else
											{
												cartEntryData.setDeliverySlotsTime(dateTimeslotMapList);
											}
											//cartEntryData.isIsBOGOapplied() ||
											if (cartEntryData.isGiveAway())
											{
												cartEntryData.setDeliverySlotsTime(dateTimeslotMapList);
											}

										}
										else if (cartEntryData.getSelectedUssid().equalsIgnoreCase(deliverySlotsResponse.getUssId())
												&& fulfillmentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP.toUpperCase()))
										{
											//isScheduleServiceble=true;
											mplCheckoutFacade.constructDeliverySlotsForEDAndHD(deliverySlotsResponse, cartEntryData,
													mplLPHolidaysModel);

											dateList = new ArrayList<String>();
											if (null != cartEntryData.getDeliverySlotsTime()
													&& cartEntryData.getDeliverySlotsTime().size() > 0)
											{
												for (final Entry<String, List<String>> entry : cartEntryData.getDeliverySlotsTime()
														.entrySet())
												{
													dateList.add(entry.getKey());
												}
											}
											String startAndEndDates = null;
											if (dateList.size() > 0)
											{
												startAndEndDates = dateList.get(0) + MarketplacecommerceservicesConstants.AND
														+ dateList.get(dateList.size() - 1);
												selectedDateBetWeen.put(cartEntryData.getSelectedUssid(), startAndEndDates);
											}
											cartEntryData.setDeliverySlotsTime(dateTimeslotMapList);
										}
									}
								}
							}
						}
					}
				}

				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.DELIVERY_SLOTS_TO_SESSION, selectedDateBetWeen);
				if (!isScheduleServiceble)
				{
					return getCheckoutStep().nextStep();
				}
				String deliverySlotCharge = MarketplacecommerceservicesConstants.EMPTY;
				final DecimalFormat df = new DecimalFormat("#.00");
				if (configModel.getSdCharge() > 0)
				{
					//final CartModel cartModel = getCartService().getSessionCart();
					cartDataSupport.setDeliverySlotCharge(mplCheckoutFacade.createPrice(cartModel,
							Double.valueOf(configModel.getSdCharge())));
					deliverySlotCharge = df.format(configModel.getSdCharge());
				}
				fullfillmentDataMap = getMplCartFacade().getFullfillmentMode(cartDataSupport);
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartDataSupport);
				model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
				model.addAttribute("mplconfigModel", deliverySlotCharge);
				model.addAttribute("defaultPincode",
						getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));

				this.prepareDataForPage(model);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(
						WebConstants.BREADCRUMBS_KEY,
						getResourceBreadcrumbBuilder().getBreadcrumbs(
								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				model.addAttribute("metaRobots", "noindex,nofollow");
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliverySlotPage;
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

	//Added new  code for deliverySlots....
	@RequestMapping(value = MarketplacecheckoutaddonConstants.DELIVERY_SLOTCOST_FOR_ED, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String calculateDeliverySlotCostForED(@RequestParam("deliverySlotCost") final String deliverySlotCost,
			@RequestParam("deliverySlotDate") final String deliverySlotDate,
			@RequestParam("deliverySlotTime") final String deliverySlotTime, @RequestParam("ussId") final String ussId)
	{
		String totalPriceFormatted = MarketplacecommerceservicesConstants.EMPTY;
		String formatDeliveryCost = MarketplacecommerceservicesConstants.EMPTY;
		final CartModel cartModel = getCartService().getSessionCart();
		final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
		final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
		final String currencySymbol = currency.getSymbol();
		for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
		{
			if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
			{
				if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(ussId))
				{
					//Start of UF-281
					boolean isSaveRequired = false;
					if (null != cartEntryModel.getEdScheduledDate() && StringUtils.isNotEmpty(cartEntryModel.getEdScheduledDate()))
					{
						cartEntryModel.setEdScheduledDate("".trim());
						cartEntryModel.setTimeSlotFrom("".trim());
						cartEntryModel.setTimeSlotTo("".trim());
					}
					if (cartEntryModel.getScheduledDeliveryCharge() != null
							&& cartEntryModel.getScheduledDeliveryCharge().doubleValue() != 0.0)
					{
						isSaveRequired = true;
						if (cartModel.getTotalPriceWithConv() != null)
						{
							cartModel.setTotalPriceWithConv(new Double(cartModel.getTotalPriceWithConv().doubleValue()
									- Double.valueOf(cartEntryModel.getScheduledDeliveryCharge().doubleValue()).doubleValue()));
						}
						final Double finalDeliveryCost = Double.valueOf(cartModel.getDeliveryCost().doubleValue()
								- cartEntryModel.getScheduledDeliveryCharge().doubleValue());
						cartModel.setDeliveryCost(finalDeliveryCost);
						final Double totalPriceAfterDeliveryCost = Double.valueOf(cartModel.getTotalPrice().doubleValue()
								- cartEntryModel.getScheduledDeliveryCharge().doubleValue());
						cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
						cartEntryModel.setScheduledDeliveryCharge(Double.valueOf(0));
					}
					if (isSaveRequired)
					{
						modelService.save(cartModel);
						modelService.refresh(cartModel);
					}
					//End of UF-281
					cartEntryModel.setEdScheduledDate(deliverySlotDate);
					if (null != deliverySlotTime)
					{
						final String[] timeSlots = deliverySlotTime.split(MarketplacecommerceservicesConstants.TO);
						cartEntryModel.setTimeSlotFrom(timeSlots[0].trim());
						cartEntryModel.setTimeSlotTo(timeSlots[1].trim());
						if (null != deliverySlotCost && !deliverySlotCost.isEmpty() && !deliverySlotCost.matches("0"))
						{
							cartEntryModel.setScheduledDeliveryCharge(Double.valueOf(deliverySlotCost));
							if (cartModel.getTotalPriceWithConv() != null && StringUtils.isNotEmpty(deliverySlotCost))
							{
								cartModel.setTotalPriceWithConv(new Double(cartModel.getTotalPriceWithConv().doubleValue()
										+ Double.valueOf(deliverySlotCost).doubleValue()));
							}

						}
					}
				}
			}

		}

		modelService.saveAll(cartEntryList);
		final Double subTotal = cartModel.getTotalPrice();
		Double finalDeliveryCost = null;
		Double totalPriceAfterDeliveryCost = null;
		if (null != deliverySlotCost && !deliverySlotCost.isEmpty() && !deliverySlotCost.matches("0"))
		{
			final Double deliverySlotCharge = Double.valueOf(deliverySlotCost);
			finalDeliveryCost = Double.valueOf(cartModel.getDeliveryCost().doubleValue() + deliverySlotCharge.doubleValue());
			cartModel.setDeliveryCost(finalDeliveryCost);
			totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + deliverySlotCharge.doubleValue());
			cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
			modelService.save(cartModel);
			modelService.refresh(cartModel);
			LOG.debug("Cart Moel Saved Successfully.....");
			final DecimalFormat df = new DecimalFormat("#.00");
			totalPriceFormatted = df.format(totalPriceAfterDeliveryCost);
			formatDeliveryCost = df.format(finalDeliveryCost);
		}

		return currencySymbol + formatDeliveryCost + MarketplacecommerceservicesConstants.HYPHEN + currencySymbol
				+ totalPriceFormatted;
	}

	// = "updateDeliverySlotCostForEd";
	@RequestMapping(value = MarketplacecheckoutaddonConstants.UPDATE_DELIVERY_SLOTCOST_FOR_ED, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String updateCalculateDeliverySlotCostForED(
			@RequestParam("deliverySlotCost") final String deliverySlotCost, @RequestParam("ussId") final String ussId)
	{
		String totalPriceFormatted = MarketplacecommerceservicesConstants.EMPTY;
		String formatDeliveryCost = MarketplacecommerceservicesConstants.EMPTY;
		final CartModel cartModel = getCartService().getSessionCart();
		final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
		final String currencySymbol = currency.getSymbol();
		final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
		for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
		{
			if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
			{
				if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(ussId))
				{
					cartEntryModel.setEdScheduledDate("");
					cartEntryModel.setTimeSlotFrom("");
					cartEntryModel.setTimeSlotTo("");
					cartEntryModel.setScheduledDeliveryCharge(Double.valueOf(0));

				}
			}
		}

		modelService.saveAll(cartEntryList);
		Double finalDeliveryCost = null;
		Double totalPriceAfterDeliveryCost = null;
		final Double subTotal = cartModel.getTotalPrice();
		if (null != deliverySlotCost && !deliverySlotCost.isEmpty() && !deliverySlotCost.matches("0"))
		{
			final Double deliverySlotCharge = Double.valueOf(deliverySlotCost);
			finalDeliveryCost = Double.valueOf(cartModel.getDeliveryCost().doubleValue() - deliverySlotCharge.doubleValue());
			cartModel.setDeliveryCost(finalDeliveryCost);
			totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() - deliverySlotCharge.doubleValue());
			cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
			try
			{
				if (cartModel.getTotalPriceWithConv() != null && StringUtils.isNotEmpty(deliverySlotCost))
				{
					cartModel.setTotalPriceWithConv(new Double(cartModel.getTotalPriceWithConv().doubleValue()
							- Double.valueOf(deliverySlotCost).doubleValue()));
				}
			}
			catch (final Exception exception)
			{
				LOG.error("Exception rasing while Convert prise amount  " + exception.getMessage());
			}
			modelService.save(cartModel);
			modelService.refresh(cartModel);
			LOG.debug("Cart Moel Saved Successfully.....");
			final DecimalFormat df = new DecimalFormat("#.00");
			totalPriceFormatted = df.format(totalPriceAfterDeliveryCost);
			formatDeliveryCost = df.format(finalDeliveryCost);
		}

		return currencySymbol + formatDeliveryCost + MarketplacecommerceservicesConstants.HYPHEN + currencySymbol
				+ totalPriceFormatted;
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
			final Model model, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		//TPR-1214
		// Save call has been changed to Ajax for saving a new address instead of HTTP request submission.
		final JSONObject jsonObj = new JSONObject();
		final CartModel oModel = getCartService().getSessionCart();
		int pincodeCookieMaxAge;
		final Cookie cookie = GenericUtilityMethods.getCookieByName(request, "pdpPincode");
		final String cookieMaxAge = getConfigurationService().getConfiguration().getString("pdpPincode.cookie.age");
		pincodeCookieMaxAge = (Integer.valueOf(cookieMaxAge)).intValue();
		final String domain = getConfigurationService().getConfiguration().getString("shared.cookies.domain");
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

				final String fullAddress = addressForm.getLine1();


				final AddressData newAddress = new AddressData();

				String addressLine1 = "";
				String addressLine2 = "";
				String addressLine3 = "";

				if (fullAddress.length() <= 40)
				{
					addressLine1 = fullAddress.substring(0, fullAddress.length());
				}
				else if (fullAddress.length() <= 80 && fullAddress.length() > 40)
				{
					addressLine1 = fullAddress.substring(0, 40);
					addressLine2 = fullAddress.substring(40, fullAddress.length());
				}
				else if (fullAddress.length() > 80 && fullAddress.length() <= 120)
				{
					addressLine1 = fullAddress.substring(0, 40);
					addressLine2 = fullAddress.substring(40, 80);
					addressLine3 = fullAddress.substring(80, fullAddress.length());
				}

				newAddress.setLine1(addressLine1);
				newAddress.setLine2(addressLine2);
				newAddress.setLine3(addressLine3);
				newAddress.setTitleCode(addressForm.getTitleCode());
				newAddress.setFirstName(addressForm.getFirstName());
				newAddress.setLastName(addressForm.getLastName());
				//				newAddress.setLine1(addressForm.getLine1());
				//				newAddress.setLine2(addressForm.getLine2());
				//				newAddress.setLine3(addressForm.getLine3());
				newAddress.setTown(addressForm.getTownCity());
				newAddress.setPostalCode(addressForm.getPostcode());
				newAddress.setBillingAddress(false);
				newAddress.setShippingAddress(true);
				newAddress.setAddressType(addressForm.getAddressType());
				newAddress.setState(addressForm.getState());
				newAddress.setPhone(addressForm.getMobileNo());
				newAddress.setLocality(addressForm.getLocality());
				//TPR-6654
				if (StringUtils.isNotEmpty(newAddress.getPostalCode()))
				{
					if (cookie != null && cookie.getValue() != null)
					{
						cookie.setValue(newAddress.getPostalCode());
						cookie.setMaxAge(pincodeCookieMaxAge);
						cookie.setPath("/");

						if (null != domain && !domain.equalsIgnoreCase("localhost"))
						{
							cookie.setSecure(true);
						}
						cookie.setDomain(domain);
						response.addCookie(cookie);
						getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE,
								newAddress.getPostalCode());
					}
					else
					{
						pdpPincodeCookie.addCookie(response, addressForm.getPostcode());
						getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE,
								newAddress.getPostalCode());
					}
				}
				// R2.3 changes
				if (StringUtils.isNotBlank(addressForm.getLandmark())
						&& !addressForm.getLandmark().equalsIgnoreCase(MarketplacecommerceservicesConstants.OTHER))
				{
					newAddress.setLandmark(addressForm.getLandmark());
				}
				else if (StringUtils.isNotBlank(addressForm.getOtherLandmark()))
				{
					newAddress.setLandmark(addressForm.getOtherLandmark());
				}

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

				//Added the code for Delivery Method slots
				final CartData cartDataSupport = getMplCartFacade().getSessionCartWithEntryOrdering(true);
				final List<String> deliveryModelList = new ArrayList<String>();
				for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
				{
					if (null != cartEntryData && null != cartEntryData.getMplDeliveryMode())
					{
						if (cartEntryData.getMplDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
						{
							deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode());
						}
						else if (cartEntryData.getMplDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
						{
							deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode());
						}
					}
				}
				//Start the Buf Fix TATA-862
				if ("" != redirectURL && null != redirectURL && redirectURL.trim().length() > 0)
				{
					jsonObj.put("redirect_url", redirectURL);
					//return redirectURL;
				}
				else
				{
					if (deliveryModelList.size() > 0)
					{
						LOG.debug("****************:" + MarketplacecommerceservicesConstants.REDIRECT
								+ MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL
								+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL);
						//						return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL
						//								+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL;
						jsonObj.put("redirect_url", MarketplacecommerceservicesConstants.REDIRECT
								+ MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL
								+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL);
					}
					else
					{
						jsonObj.put("redirect_url", getCheckoutStep().nextStep());
					}

				}
				// End for Bug Fix - TATA-862
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
	public String edit(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		int pincodeCookieMaxAge;
		final Cookie cookie = GenericUtilityMethods.getCookieByName(request, "pdpPincode");
		final String cookieMaxAge = getConfigurationService().getConfiguration().getString("pdpPincode.cookie.age");
		pincodeCookieMaxAge = (Integer.valueOf(cookieMaxAge)).intValue();
		final String domain = getConfigurationService().getConfiguration().getString("shared.cookies.domain");
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

			final String fullAddress = addressForm.getLine1();

			String addressLine1 = "";
			String addressLine2 = "";
			String addressLine3 = "";



			if (fullAddress.length() <= 40)
			{
				addressLine1 = fullAddress.substring(0, fullAddress.length());
			}
			else if (fullAddress.length() <= 80 && fullAddress.length() > 40)
			{
				addressLine1 = fullAddress.substring(0, 40);
				addressLine2 = fullAddress.substring(40, fullAddress.length());
			}
			else if (fullAddress.length() > 80 && fullAddress.length() <= 120)
			{
				addressLine1 = fullAddress.substring(0, 40);
				addressLine2 = fullAddress.substring(40, 80);
				addressLine3 = fullAddress.substring(80, fullAddress.length());
			}
			newAddress.setLine1(addressLine1);
			newAddress.setLine2(addressLine2);
			newAddress.setLine3(addressLine3);
			newAddress.setId(addressForm.getAddressId());
			newAddress.setTitleCode(addressForm.getTitleCode());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());
			/*
			 * newAddress.setLine1(addressForm.getLine1()); newAddress.setLine2(addressForm.getLine2());
			 * newAddress.setLine3(addressForm.getLine3());
			 */

			newAddress.setTown(addressForm.getTownCity());
			newAddress.setPostalCode(addressForm.getPostcode());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setBillingAddress(false);
			newAddress.setShippingAddress(true);
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setLocality(addressForm.getLocality());
			newAddress.setState(addressForm.getState());
			//TPR-6654
			if (StringUtils.isNotEmpty(newAddress.getPostalCode()))
			{
				if (cookie != null && cookie.getValue() != null)
				{
					cookie.setValue(newAddress.getPostalCode());
					cookie.setMaxAge(pincodeCookieMaxAge);
					cookie.setPath("/");

					if (null != domain && !domain.equalsIgnoreCase("localhost"))
					{
						cookie.setSecure(true);
					}
					cookie.setDomain(domain);
					response.addCookie(cookie);
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, newAddress.getPostalCode());
				}
				else
				{
					pdpPincodeCookie.addCookie(response, newAddress.getPostalCode());
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, newAddress.getPostalCode());
				}
			}
			if (StringUtils.isNotBlank(addressForm.getLandmark())
					&& !addressForm.getLandmark().equalsIgnoreCase(MarketplacecommerceservicesConstants.OTHER))
			{
				newAddress.setLandmark(addressForm.getLandmark());
			}
			else if (StringUtils.isNotBlank(addressForm.getOtherLandmark()))
			{
				newAddress.setLandmark(addressForm.getOtherLandmark());
			}

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
		//return getCheckoutStep().nextStep();

		final CartData cartDataSupport = getMplCartFacade().getSessionCartWithEntryOrdering(true);
		final List<String> deliveryModelList = new ArrayList<String>();
		for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
		{
			if (null != cartEntryData && null != cartEntryData.getMplDeliveryMode())
			{
				if (cartEntryData.getMplDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)
						|| cartEntryData.getMplDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
				{
					deliveryModelList.add(cartEntryData.getMplDeliveryMode().getCode());
				}
			}
		}
		if (deliveryModelList.size() > 0)
		{
			LOG.debug("****************:" + MarketplacecommerceservicesConstants.REDIRECT
					+ MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL + MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLDELIVERYMETHODURL
					+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL;
		}
		else
		{
			return getCheckoutStep().nextStep();
		}
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
			//TISSTRT-1647
			final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
			final String currencySymbol = currency.getSymbol();
			if (null != cartData && StringUtils.isEmpty(cartData.getCurrencySymbol()))
			{
				cartData.setCurrencySymbol(currencySymbol);
			}

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
		//added for CAR:127
		final CartModel cart = cartService.getSessionCart();
		final CartData cData = getMplCartFacade().getCartDataFromCartModel(cart, false);
		//added for CAR:127

		if (LOG.isDebugEnabled())
		{
			LOG.debug("from cncCartReservation method ");
		}
		//commented for CAR:127
		/*
		 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
		 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, null);
		 */
		final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
				MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cData, cart);
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

					//if (deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT))
					if (deliveryCode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CLICK_N_COLLECT))
					{
						//For TISBBC-43
						getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode, deliveryEntry.getSellerArticleSKU(),
								cartModel); //TISPT-400
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
	 * Getting Details about the given pincode city, state, country and list of landmarks
	 *
	 * @param pincode
	 * @return PincodeData
	 */

	@RequestMapping(value = MarketplacecheckoutaddonConstants.LANDMARKS, method = RequestMethod.GET)
	@ResponseBody
	public PincodeData getPincodedata(@RequestParam(value = "pincode") final String pincode)
	{
		PincodeData pincodeData = null;
		try
		{
			if (null != pincode && !StringUtils.isEmpty(pincode))
			{
				pincodeData = pincodeServiceFacade.getAutoPopulatePincodeData(pincode);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			LOG.error("EtailNonBusinessExceptions in getting the pincode Details :: " + ex.getErrorMessage());
		}
		catch (final EtailBusinessExceptions ex)
		{
			LOG.error("EtailBusinessExceptions in getting the pincode Details :: " + ex.getErrorMessage());
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in getting the pincode Details :: " + ex.getMessage());
		}
		return pincodeData;
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
