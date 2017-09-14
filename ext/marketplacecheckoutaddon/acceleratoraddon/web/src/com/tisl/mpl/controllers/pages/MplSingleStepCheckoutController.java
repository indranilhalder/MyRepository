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
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCheckoutController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PaymentDetailsForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.Constants.USER;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.util.Tuple2;
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
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.beans.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
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

import com.granule.json.JSONException;
import com.granule.json.JSONObject;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.tisl.mpl.checkout.form.DeliveryMethodEntry;
import com.tisl.mpl.checkout.form.DeliveryMethodForm;
import com.tisl.mpl.checkout.form.DeliverySlotEntry;
import com.tisl.mpl.checkout.form.DeliverySlotForm;
import com.tisl.mpl.checkout.steps.validation.impl.ResponsivePaymentCheckoutStepValidator;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.core.enums.WalletEnum;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.checkout.storelocator.MplStoreLocatorFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplSlaveMasterFacade;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.ATSResponseData;
import com.tisl.mpl.facades.data.FreebieProduct;
import com.tisl.mpl.facades.data.ProudctWithPointOfServicesData;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.juspay.response.ListCardsResponse;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
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
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.util.CSRFTokenManager;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.PaymentForm;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;

import reactor.function.support.UriUtils;


/**
 * SingleStepCheckoutController
 */
@Controller
@RequestMapping(value = "/checkout/single")
public class MplSingleStepCheckoutController extends AbstractCheckoutController
{
	@SuppressWarnings("unused")
	protected static final Logger LOG = Logger.getLogger(MplSingleStepCheckoutController.class);

	protected static final String MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL = "multiStepCheckoutSummary";

	@Resource(name = ModelAttributetConstants.ACCELERATOR_CHECKOUT_FACADE)
	private CheckoutFacade checkoutFacade;

	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "mplSellerInformationService")
	private MplSellerInformationService mplSellerInformationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "mplStoreLocatorFacade")
	private MplStoreLocatorFacade mplStoreLocatorFacade;

	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

	@Resource(name = "productService")
	ProductService productService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "wishlistFacade")
	private WishlistFacade wishlistFacade;

	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;

	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "mplSlaveMasterFacade")
	private MplSlaveMasterFacade mplSlaveMasterFacade;

	@Resource(name = "mplCouponFacade")
	private MplCouponFacade mplCouponFacade;

	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;

	//private final String checkoutPageName = "Choose Your Delivery Options";

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "mplSellerInformationFacade")
	private MplSellerInformationFacade mplSellerInformationFacade;

	@Resource(name = "pointOfServiceConverter")
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	@Resource(name = "mplAddressValidator")
	private MplAddressValidator mplAddressValidator;

	@Resource(name = "mplConfigFacade")
	private MplConfigFacade mplConfigFacade;

	@Resource(name = "accountAddressFacade")
	private MplAccountAddressFacade accountAddressFacade;


	@Autowired
	private DateUtilHelper dateUtilHelper;
	@Resource(name = "defaultResponsivePaymentMethodCheckoutValidator")
	private ResponsivePaymentCheckoutStepValidator paymentValidator;

	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	private final String checkoutPageName1 = "New Address";
	private final String selectAddress = "Select Address";

	private final String RECEIVED_INR = "Received INR ";
	private final String DISCOUNT_MSSG = " discount on purchase of Promoted Product";
	private static final String UTF = "UTF-8";


	//Exchange Changes
	@Resource(name = "exchangeGuideFacade")
	private ExchangeGuideFacade exchangeGuideFacade;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

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
	 * Landing page for Single Page Journey This is the entry point of SinglePageCheckout, This method will return the
	 * Main page and will display the delivery address carousel by default on page load. On Ajax call i.e isAjax=true
	 * this method will only return the delivery addresses page. This is required as the delivery address carousel needs
	 * to be refreshed once a new address is added or an existing address is edited.
	 *
	 * Using @PreValidateCheckoutStep(checkoutStep = MarketplacecheckoutaddonConstants.DELIVERY_METHOD) as this only
	 * checks if the cart is valid
	 *
	 * @param model
	 * @param isAjax
	 * @return String - Jsp page
	 */
	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateCheckoutStep(checkoutStep = MarketplacecheckoutaddonConstants.DELIVERY_METHOD)
	public String checkoutPage(final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "false") final boolean isAjax,
			@RequestParam(required = false, defaultValue = "false") final boolean isResponsive,
			@RequestParam(required = false, defaultValue = "false") final boolean isNormal)
	{
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside checkoutPage Method...");
			}
			if (getUserFacade().isAnonymousUser())
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			String deviceType = "unknown";
			// Device identification is required, since the page load will be different for responsive web and tablet.
			final Device device = DeviceUtils.getCurrentDevice(request);
			if (null != device)
			{
				if (device.isNormal())
				{
					deviceType = "normal";
					LOG.debug("type of device=" + device.isNormal());
				}
				else if (device.isMobile())
				{
					deviceType = "mobile";
					LOG.debug("type of device=" + device.isMobile());
				}
				else if (device.isTablet())
				{
					deviceType = "tablet";
					LOG.debug("type of device=" + device.isTablet());
				}
			}
			final CartModel cartModel = getCartService().getSessionCart();
			LOG.debug("Device Type=" + deviceType);
			final String isServicable = getSessionService().getAttribute("isCartPincodeServiceable");
			// Handle scenarios where an user directly lands to checkout page.
			if ((StringUtils.isEmpty(isServicable)) || (isServicable.equalsIgnoreCase("N")))
			{
				getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_MSG_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			//This session attribute is required for responsive one page
			getSessionService().setAttribute("isCheckoutPincodeServiceable", isServicable);

			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			final CartData cartUssidData = getMplCustomAddressFacade().getCheckoutCart();
			List<AddressData> deliveryAddress = null;
			if (cartUssidData != null)
			{
				deliveryAddress = getMplCustomAddressFacade().getDeliveryAddresses(cartUssidData.getDeliveryAddress(), cartModel); //CAR-194
			}
			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;

			//TISPT-400 all address sorting logic
			deliveryAddress = getMplCheckoutFacade().rePopulateDeliveryAddress(deliveryAddress);

			LOG.debug("Before final model attribute level set ");

			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);

			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartUssidData);
			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(MarketplacecheckoutaddonConstants.METAROBOTS, MarketplacecheckoutaddonConstants.NOINDEX_NOFOLLOW);
			timeOutSet(model);


			if (!isAjax)
			{
				model.addAttribute("isPincodeRestrictedPromoPresent",
						Boolean.valueOf(mplCartFacade.checkPincodeRestrictedPromoOnCartProduct(cartModel)));
				//Tealium related data population
				final String cartLevelSellerID = populateCheckoutSellers(cartUssidData);
				model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, cartLevelSellerID);
				GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartUssidData);
				model.addAttribute("checkoutPageName", selectAddress);
				if (isResponsive)
				{
					model.addAttribute("deviceType", "mobile");
				}
				else if (isNormal)
				{
					model.addAttribute("deviceType", "normal");
				}
				else
				{
					model.addAttribute("deviceType", deviceType);
				}
				//Code to import payment on checkout page load
				model.addAttribute("openTab", "deliveryAddresses");
				model.addAttribute("prePopulateTab", "payment#deliveryMethod");
				prepareModelForPayment(model, cartUssidData);
				if (!deviceType.equals("normal") || isResponsive)
				{
					cartModel.setDeliveryCost(Double.valueOf(0.0));
					modelService.save(cartModel);
					modelService.refresh(cartModel);
					final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
					mplCheckoutFacade.resetSlotEntries(cartEntryList);
					prepareModelForDeliveryMode(model, cartModel);
				}
				GenericUtilityMethods.getCartPriceDetails(model, cartModel, null);
			}
			if (isAjax)
			{
				//final String selectedAddress = getSessionService().getAttribute("selectedAddress");
				//model.addAttribute("selectedAddress", selectedAddress);
				model.addAttribute("isResponsive", Boolean.valueOf(isResponsive));
				return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.DeliveryAddressCarousel;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions in checkoutPage method ", e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions in checkoutPage method ", e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			LOG.error("EtailBusinessExceptions in checkoutPage method ", e);
			return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.SingleStepCheckout.SinglePageCheckoutPage;
	}

	/*
	 * This method will be used for responsive site for location restricted promotion. Used to fetch the eligible
	 * delivery modes without page load.
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHECKLOCATIONRESTRICTEDPINCODE, method = RequestMethod.GET)
	public String checkLocationRestrictedPincode(
			@RequestParam(value = "contExchnage", required = false) final String exchangeEnabled,
			@PathVariable(MarketplacecheckoutaddonConstants.PINCODE) final String selectedPincode,
			@RequestParam(value = "locRestrictedPromoPresent", required = false, defaultValue = "false") final boolean locRestrictedPromoPresent,
			final HttpServletRequest request) throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside checkLocationRestrictedPincode Method...");
				LOG.debug("User Agent==>" + request.getHeader("user-agent"));
				LOG.debug("Method selectedPincode==>" + selectedPincode);
				LOG.debug("Method locRestrictedPromoPresent==>" + locRestrictedPromoPresent);
			}
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			if (!getCartService().hasSessionCart())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//TISST-13012
			final CartModel cart = getCartService().getSessionCart();
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cart);
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			Boolean exchangeAppliedCart = Boolean.FALSE;
			if (StringUtils.isEmpty(exchangeEnabled) && !cartItemDelistedStatus)
			{
				exchangeAppliedCart = cart.getExchangeAppliedCart();
				LOG.debug("exchangeAppliedCart=" + exchangeAppliedCart);
			}

			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			//TISSEC-11
			final String regex = "\\d{6}";

			if (selectedPincode.matches(regex))
			{
				final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
				if (StringUtils.isEmpty(sessionPincode))
				{
					final String requestQueryParam = UriUtils
							.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART + "&type=redirect", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				final String redirectURL = mplCartFacade.singlePagecheckPincode(selectedPincode, sessionPincode);
				if (redirectURL.trim().length() > 0)
				{
					final String requestQueryParam = UriUtils.encodeQuery(
							"?msg=" + MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error",
							UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}

				mplCartFacade.populatePinCodeData(cart, selectedPincode);
				// Recalculating Cart Model
				LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
				if (locRestrictedPromoPresent)
				{
					getMplCheckoutFacade().reCalculateCart(cartData);
				}
			}
			else if (!selectedPincode.matches(regex))
			{
				final String requestQueryParam = UriUtils.encodeQuery("?msg=Provide valid pincode&type=error", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			if (null != exchangeAppliedCart && exchangeAppliedCart.booleanValue() && selectedPincode.matches(regex)
					&& StringUtils.isEmpty(exchangeEnabled) && !cartItemDelistedStatus)
			{
				if (!exchangeGuideFacade.isBackwardServiceble(selectedPincode))
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg=Exchange Not Servicable&type=confirm", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
			}
			getSessionService().setAttribute(MarketplacecommerceservicesConstants.IS_RESPONSIVE,
					MarketplacecommerceservicesConstants.TRUE);
			if (StringUtils.isNotEmpty(exchangeEnabled))
			{
				exchangeGuideFacade.removeExchangefromCart(cart);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("End of checkLocationRestrictedPincode Method...Pincode is serviceable.");
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while selecting address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while checkingLocationRestrictedPincode:" + e);
			LOG.error("Stack trace:", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return FORWARD_PREFIX + "/checkout/single/choose?" + MarketplacecommerceservicesConstants.IS_RESPONSIVE + "="
				+ MarketplacecommerceservicesConstants.TRUE;
	}

	/**
	 * This method will return a pre-populated address form as a JSP.
	 *
	 * @param addressCode
	 * @param model
	 * @return String - Edit address JSP
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS
			+ MarketplacecheckoutaddonConstants.ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String editAddress(@PathVariable(ModelAttributetConstants.ADDRESS_CODE) final String addressCode, final Model model)
			throws UnsupportedEncodingException
	{

		List<StateData> stateDataList = new ArrayList<StateData>();
		final AccountAddressForm addressForm = new AccountAddressForm();
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside editAddress Method...");
			}
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
			model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
			model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
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
			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE);

			this.prepareDataForPage(model);
			model.addAttribute("metaRobots", "noindex,nofollow");
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while editing address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while editing address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			final String requestQueryParam = UriUtils.encodeQuery("?msg=" + "address.error.formentry.invalid" + "&type=errorCode",
					UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.EditAddressForm;
	}

	/**
	 * @description method is called to edit address and forward to payment step
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS
			+ MarketplacecheckoutaddonConstants.ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	public String edit(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			@RequestParam(value = "contExchnageAddEdit", required = false) final String exchangeEnabled)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside edit Method...");
			}
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//TISST-13012
			final CartModel cart = getCartService().getSessionCart();
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cart);
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final String errorMsg = mplAddressValidator.validate(addressForm);
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?msg=" + MarketplacecheckoutaddonConstants.VALIDATION_FAILURE_MESSAGE + "&type=error", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
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

			final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			if (StringUtils.isEmpty(sessionPincode))
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//TISUTO-12 , TISUTO-11 ,TISUTO 106
			final String redirectURL = mplCartFacade.singlePagecheckPincode(addressForm.getPostcode(), sessionPincode);
			if (redirectURL.trim().length() > 0)
			{
				final String requestQueryParam = UriUtils.encodeQuery(
						"?msg=" + MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			if (null != cart.getExchangeAppliedCart() && cart.getExchangeAppliedCart().booleanValue()
					&& StringUtils.isEmpty(exchangeEnabled))
			{
				if (!exchangeGuideFacade.isBackwardServiceble(addressForm.getPostcode()))
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg=Exchange Not Servicable&type=confirm", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
			}
			if (StringUtils.isNotEmpty(exchangeEnabled))
			{
				exchangeGuideFacade.removeExchangefromCart(cart);
			}
			saveAndSetDeliveryAddress(addressForm, true);

			//Recalculating Cart Model
			LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
			getMplCheckoutFacade().reCalculateCart(cartData);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while editing address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while editing address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while editing address :", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return REDIRECT_PREFIX + "/checkout/single/choose";

	}


	/**
	 * Method will return JSON response, This method is used to pass error messages to ajax request
	 *
	 * @param msg
	 * @param type
	 * @param url
	 * @param redirectString
	 * @return JSON Object
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLSHOWMESSAGE, method =
	{ RequestMethod.POST, RequestMethod.GET })
	private @ResponseBody JSONObject displayMessage(@RequestParam(required = false) final String msg,
			@RequestParam(required = true) final String type, @RequestParam(required = false) final String url,
			@RequestParam(required = false) final String redirectString)
	{
		final JSONObject jsonObj = new JSONObject();

		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside displayMessage Method...");
				LOG.debug("DisplayMessage Parameters=>msg:" + msg + "; type:" + type + "; url:" + url + "; redirectString:"
						+ redirectString + ";");
			}
			//Used for exceptions messages and error messages
			if (type.equals("error"))
			{
				jsonObj.put("displaymessage", msg);
				jsonObj.put("type", type);
			}
			//Used for exceptions messages and error messages with an error code. Error code is equivalent to code present in base_en.properties.
			if (type.equals("errorCode"))
			{
				jsonObj.put("displaymessage", msg);
				jsonObj.put("type", type);
			}
			//Used to redirect to the specified URL in case the user is unable to continue due to some exceptions
			if (type.equals("redirect"))
			{
				jsonObj.put("url", url);
				jsonObj.put("type", type);
			}
			//Used to redirect to a step in checkout journey
			if (type.equals("ajaxRedirect"))
			{
				jsonObj.put("redirectString", redirectString);
				jsonObj.put("type", type);
			}
			if (type.equals("confirm"))
			{
				jsonObj.put("displaymessage", msg);
				jsonObj.put("type", type);
			}
		}
		catch (final JSONException e)
		{
			LOG.debug("JSONException occured in displayMessage:", e);
		}
		catch (final Exception e)
		{
			LOG.debug("Exception occured in displayMessage:", e);
		}
		return jsonObj;
	}

	/**
	 * @description method is called after fetching address form and calling POST
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYNEWADDRESSURL, method = RequestMethod.GET)
	public String addNewAddress(final Model model) throws CMSItemNotFoundException
	{
		String addressFlag = "F";
		List<AddressData> deliveryAddress = null;
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside addNewAddress Method...");
			}
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			final CartModel cartModel = getCartService().getSessionCart();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);
			addressForm.setAddressType("Home");
			final List<StateData> stateDataList = accountAddressFacade.getStates();

			if (null != cartData)
			{
				deliveryAddress = getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress(), cartModel); //CAR-194
			}


			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;

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
			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE);
			model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS, Boolean.FALSE);
			this.prepareDataForPage(model);
			model.addAttribute("metaRobots", "noindex,nofollow");
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
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.AddAddressForm;
	}

	/**
	 * @description method is called to select address from list and forward to delivery mode step
	 * @param selectedAddressCode
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTADDRESSURL, method = RequestMethod.GET)
	public String selectAddress(@RequestParam("selectedAddressCode") final String selectedAddressCode,
			@RequestParam(value = "contExchnage", required = false) final String exchangeEnabled) throws UnsupportedEncodingException
	{
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside selectAddress Method...");
				LOG.debug("selectedAddressCode=" + selectedAddressCode);
				LOG.debug("exchangeEnabled=" + exchangeEnabled);
				LOG.debug("CurrentUser=" + userService.getCurrentUser().getUid());
			}
			//TISST-13012
			final CartModel cart = getCartService().getSessionCart();
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cart);
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			boolean exchangeAppliedCart = false;
			if ((StringUtils.isEmpty(exchangeEnabled) && !cartItemDelistedStatus) && (null != (cart.getExchangeAppliedCart())))
			{
				exchangeAppliedCart = (null != cart.getExchangeAppliedCart()) ? cart.getExchangeAppliedCart().booleanValue() : false;
				LOG.debug("exchangeAppliedCart=" + exchangeAppliedCart);
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
			finaladdressData.setDefaultAddress(true);
			finaladdressData.setVisibleInAddressBook(true);

			final String selectedPincode = finaladdressData.getPostalCode();
			LOG.debug("selectedPincode=" + selectedPincode);
			//TISSEC-11
			final String regex = "\\d{6}";

			if (selectedPincode.matches(regex))
			{
				final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
				LOG.debug("sessionPincode=" + sessionPincode);
				if (StringUtils.isEmpty(sessionPincode))
				{
					final String requestQueryParam = UriUtils
							.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART + "&type=redirect", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				final String redirectURL = mplCartFacade.singlePagecheckPincode(selectedPincode, sessionPincode);
				if (redirectURL.trim().length() > 0)
				{
					final String requestQueryParam = UriUtils.encodeQuery(
							"?msg=" + MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error",
							UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}

				getMplCustomAddressFacade().setDeliveryAddress(finaladdressData);
				userFacade.setDefaultAddress(finaladdressData);

				// Recalculating Cart Model check location restricted promotion
				LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
				getMplCheckoutFacade().reCalculateCart(cartData);
			}
			if (exchangeAppliedCart && selectedPincode.matches(regex) && StringUtils.isEmpty(exchangeEnabled)
					&& !cartItemDelistedStatus)
			{
				if (!exchangeGuideFacade.isBackwardServiceble(selectedPincode))
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg=Exchange Not Servicable&type=confirm", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
			}
			if (StringUtils.isNotEmpty(exchangeEnabled))
			{
				exchangeGuideFacade.removeExchangefromCart(cart);
			}
			else if (!selectedPincode.matches(regex))
			{
				final String requestQueryParam = UriUtils.encodeQuery("?msg=Provide valid pincode&type=error", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while selecting address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while selecting  address:" + e);
			LOG.error("Stack trace:", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		return FORWARD_PREFIX + "/checkout/single/choose";
	}

	/**
	 * @description method is called to set address from list and save in cart in payment button radio click
	 * @param selectedAddressCode
	 * @return Json Object
	 * @throws JSONException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLRESPONSIVESELECTADDRESSURL, method = RequestMethod.GET)
	public @ResponseBody JSONObject selectAddressResponsive(@RequestParam("selectedAddressCode") final String selectedAddressCode)
			throws JSONException
	{
		final JSONObject jsonObj = new JSONObject();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside selectAddress Responsive Method...");
				LOG.debug("selectedAddressCode=" + selectedAddressCode);
				LOG.debug("CurrentUser=" + userService.getCurrentUser().getUid());
			}
			final String isCheckoutPincodeServiceable = getSessionService().getAttribute("isCheckoutPincodeServiceable");
			if (isCheckoutPincodeServiceable.equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
			{
				jsonObj.put("msg", MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE);
				jsonObj.put("type", "error");
				return jsonObj;
			}
			//TISST-13012
			final CartModel cart = getCartService().getSessionCart();
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cart);
			if (cartItemDelistedStatus)
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}
			AddressData finaladdressData = new AddressData();
			for (final AddressData addressData : accountAddressFacade.getAddressBook())
			{
				if (null != addressData && null != addressData.getId() && addressData.getId().equals(selectedAddressCode))
				{
					finaladdressData = addressData;
					break;
				}
			}
			finaladdressData.setDefaultAddress(true);
			finaladdressData.setVisibleInAddressBook(true);
			getMplCustomAddressFacade().setDeliveryAddress(finaladdressData);
			userFacade.setDefaultAddress(finaladdressData);
			jsonObj.put("isAddressSet", "true");
			jsonObj.put("type", "response");
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while selecting address for responsive", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting address for responsive", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while selecting  address for responsive:" + e);
			LOG.error("Stack trace:", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		return jsonObj;
	}

	/**
	 * @description method is called to add new address while checkout and forward to delivery mode step
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @return String
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYNEWADDRESSURL, method = RequestMethod.POST)
	public String add(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			@RequestParam(value = "contExchnageAddEdit", required = false) final String exchangeEnabled)
			throws UnsupportedEncodingException
	{
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final String errorMsg = mplAddressValidator.validate(addressForm);
			//final List<StateData> stateDataList = accountAddressFacade.getStates();
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?msg=" + errorMsg + "&type=errorCode", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			else
			{
				if (StringUtils.isNotBlank(addressForm.getCountryIso()))
				{
					model.addAttribute(ModelAttributetConstants.REGIONS,
							getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
					model.addAttribute(ModelAttributetConstants.COUNTRY, addressForm.getCountryIso());
				}
				//Check if Cart has exchange entry or not
				Boolean exchangeAppliedCart = Boolean.FALSE;
				final CartModel cart = getCartService().getSessionCart();
				if (null != cart.getExchangeAppliedCart())
				{
					exchangeAppliedCart = cart.getExchangeAppliedCart();
					LOG.debug("exchangeAppliedCart=" + exchangeAppliedCart);
				}
				//Throw popup box for confirmation
				if (null != exchangeAppliedCart && exchangeAppliedCart.booleanValue() && StringUtils.isEmpty(exchangeEnabled))
				{
					if (!exchangeGuideFacade.isBackwardServiceble(addressForm.getPostcode()))
					{
						final String requestQueryParam = UriUtils.encodeQuery("?msg=Exchange Not Servicable&type=confirm", UTF);
						return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
					}
				}
				//Remove Exchange
				if (StringUtils.isNotEmpty(exchangeEnabled))
				{
					exchangeGuideFacade.removeExchangefromCart(cart);
				}

				final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
				if (StringUtils.isEmpty(sessionPincode))
				{
					final String requestQueryParam = UriUtils
							.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART + "&type=redirect", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				//TISUTO-12 , TISUTO-11
				final String redirectURL = mplCartFacade.singlePagecheckPincode(addressForm.getPostcode(), sessionPincode);
				if (redirectURL.trim().length() > 0)
				{
					final String requestQueryParam = UriUtils.encodeQuery(
							"?msg=" + MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error",
							UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				//Recalculating Cart Model
				saveAndSetDeliveryAddress(addressForm, false);
				LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
				getMplCheckoutFacade().reCalculateCart(cartData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  saving new address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while saving new address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return REDIRECT_PREFIX + "/checkout/single/choose";
	}

	/**
	 * @Description save a new address/edit address in responsive
	 * @param addressForm
	 * @param isEdit
	 * @param bindingResult
	 * @return JSONObject
	 * @throws JSONException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLRESPONSIVEDELIVERYNEWADDRESSURL, method = RequestMethod.POST)
	public @ResponseBody JSONObject addAddressResponsive(final AccountAddressForm addressForm,
			@RequestParam(required = false, defaultValue = "false") final boolean isEdit, final BindingResult bindingResult)
			throws JSONException
	{
		final JSONObject jsonObj = new JSONObject();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}
			final String isCheckoutPincodeServiceable = getSessionService().getAttribute("isCheckoutPincodeServiceable");
			if (isCheckoutPincodeServiceable.equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
			{
				jsonObj.put("msg", MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE);
				jsonObj.put("type", "error");
				return jsonObj;
			}
			final String errorMsg = mplAddressValidator.validate(addressForm);
			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{
				jsonObj.put("displaymessage", errorMsg);
				jsonObj.put("type", "errorCode");
				return jsonObj;
			}
			else
			{
				final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
				if (StringUtils.isEmpty(sessionPincode))
				{
					jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
					jsonObj.put("type", "redirect");
					return jsonObj;
				}
				if (!(addressForm.getPostcode().equals(sessionPincode)))
				{
					jsonObj.put("displaymessage", MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE);
					jsonObj.put("type", "error");
					return jsonObj;
				}
				saveAndSetDeliveryAddress(addressForm, isEdit);
				jsonObj.put("isAddressSaved", "true");
				jsonObj.put("isAddressSet", "true");
				jsonObj.put("type", "response");
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  saving new address ", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
			return jsonObj;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while saving new address ", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
			return jsonObj;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
			return jsonObj;
		}
		return jsonObj;
	}


	/**
	 * @Description Added code for deliverySlots form submission for responsive design ....
	 * @param deliverySlotForm
	 * @return deliverySlotPage
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.DELIVERY_SLOTCOST_FOR_ED, method = RequestMethod.POST)
	public @ResponseBody String calculateDeliverySlotCostForED(
			@ModelAttribute("deliverySlotForm") final DeliverySlotForm deliverySlotForm) throws UnsupportedEncodingException
	{
		String formatDeliveryCost = MarketplacecommerceservicesConstants.EMPTY;
		String totalPriceFormatted = MarketplacecommerceservicesConstants.EMPTY;
		final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
		final String currencySymbol = currency.getSymbol();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			if (deliverySlotForm.getDeliverySlotEntry() != null && !deliverySlotForm.getDeliverySlotEntry().isEmpty())
			{
				final CartModel cartModel = getCartService().getSessionCart();
				final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
				double deliverySlotCharge = 0.0;
				boolean isCartSaveRequired = false;
				for (final DeliverySlotEntry deliveryEntry : deliverySlotForm.getDeliverySlotEntry())
				{

					for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
					{
						if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
						{
							if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(deliveryEntry.getUssid()))
							{
								isCartSaveRequired = true;
								//Start of UF-281
								//								boolean isSaveRequired = false;
								//								if (null != cartEntryModel.getEdScheduledDate()
								//										&& StringUtils.isNotEmpty(cartEntryModel.getEdScheduledDate()))
								//								{
								//									cartEntryModel.setEdScheduledDate("".trim());
								//									cartEntryModel.setTimeSlotFrom("".trim());
								//									cartEntryModel.setTimeSlotTo("".trim());
								//								}
								//								if (cartEntryModel.getScheduledDeliveryCharge() != null
								//										&& cartEntryModel.getScheduledDeliveryCharge().doubleValue() != 0.0)
								//								{
								//									isSaveRequired = true;
								//									if (cartModel.getTotalPriceWithConv() != null)
								//									{
								//										cartModel.setTotalPriceWithConv(new Double(cartModel.getTotalPriceWithConv().doubleValue()
								//												- Double.valueOf(cartEntryModel.getScheduledDeliveryCharge().doubleValue()).doubleValue()));
								//									}
								//									final Double finalDeliveryCost = Double.valueOf(cartModel.getDeliveryCost().doubleValue()
								//											- cartEntryModel.getScheduledDeliveryCharge().doubleValue());
								//									cartModel.setDeliveryCost(finalDeliveryCost);
								//									final Double totalPriceAfterDeliveryCost = Double.valueOf(cartModel.getTotalPrice().doubleValue()
								//											- cartEntryModel.getScheduledDeliveryCharge().doubleValue());
								//									cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
								//									cartEntryModel.setScheduledDeliveryCharge(Double.valueOf(0));
								//								}
								//								if (isSaveRequired)
								//								{
								//									modelService.save(cartModel);
								//									modelService.refresh(cartModel);
								//								}
								//End of UF-281
								cartEntryModel.setEdScheduledDate(deliveryEntry.getDeliverySlotDate());
								if (null != deliveryEntry.getDeliverySlotTime())
								{
									final String[] timeSlots = deliveryEntry.getDeliverySlotTime()
											.split(MarketplacecommerceservicesConstants.TO);
									cartEntryModel.setTimeSlotFrom(timeSlots[0].trim());
									cartEntryModel.setTimeSlotTo(timeSlots[1].trim());
									if (null != deliveryEntry.getDeliverySlotCost() && !deliveryEntry.getDeliverySlotCost().isEmpty()
											&& !deliveryEntry.getDeliverySlotCost().matches("0"))
									{
										cartEntryModel.setScheduledDeliveryCharge(Double.valueOf(deliveryEntry.getDeliverySlotCost()));
										if (cartModel.getTotalPriceWithConv() != null
												&& StringUtils.isNotEmpty(deliveryEntry.getDeliverySlotCost()))
										{
											cartModel.setTotalPriceWithConv(new Double(cartModel.getTotalPriceWithConv().doubleValue()
													+ Double.valueOf(deliveryEntry.getDeliverySlotCost()).doubleValue()));
										}

									}
								}
								deliverySlotCharge += Double.parseDouble(deliveryEntry.getDeliverySlotCost());
							} //End of USSID comparison if
						} //End of cart entry if

					} //End of cart entry for loop
				} //End of slot entry for loop
				if (isCartSaveRequired)
				{
					modelService.saveAll(cartEntryList);
				}
				final Double subTotal = cartModel.getTotalPrice();
				Double finalDeliveryCost = null;
				Double totalPriceAfterDeliveryCost = null;
				if (deliverySlotCharge != 0.0)
				{
					//final Double deliverySlotCharge = Double.valueOf(deliverySlotCost);
					finalDeliveryCost = Double.valueOf(cartModel.getDeliveryCost().doubleValue() + deliverySlotCharge);
					cartModel.setDeliveryCost(finalDeliveryCost);
					totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + deliverySlotCharge);
					cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
					modelService.save(cartModel);
					modelService.refresh(cartModel);
					LOG.debug("Cart Moel Saved Successfully.....");
					final DecimalFormat df = new DecimalFormat("#.00");
					totalPriceFormatted = df.format(totalPriceAfterDeliveryCost);
					formatDeliveryCost = df.format(finalDeliveryCost);
				}
			} //End of slot entry if
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while saving slots ", e);
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :", e);
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return currencySymbol + formatDeliveryCost + MarketplacecommerceservicesConstants.HYPHEN + currencySymbol
				+ totalPriceFormatted;
	}

	/**
	 * @Description Common method to save new address/edited address
	 * @param addressForm
	 * @param isEditAddress
	 */
	public void saveAndSetDeliveryAddress(final AccountAddressForm addressForm, final boolean isEditAddress)
	{
		CartModel oModel = null;
		if (!isEditAddress)//For add address
		{
			oModel = getCartService().getSessionCart();
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
		if (isEditAddress)//For edit address
		{
			newAddress.setId(addressForm.getAddressId());
		}
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
		newAddress.setDefaultAddress(true);
		if (addressForm.getSaveInAddressBook() != null)
		{
			newAddress.setVisibleInAddressBook(Boolean.TRUE.equals(addressForm.getSaveInAddressBook()));
		}
		else if (getCheckoutCustomerStrategy().isAnonymousCheckout())
		{
			newAddress.setDefaultAddress(true);
			newAddress.setVisibleInAddressBook(true);
		}
		else
		{
			newAddress.setVisibleInAddressBook(true);
		}
		if (isEditAddress)//For edit address
		{
			accountAddressFacade.editAddress(newAddress);
		}
		else
		{//For add address
			if (null != oModel && null != oModel.getUser())
			{
				accountAddressFacade.addaddress(newAddress, (CustomerModel) oModel.getUser());
			}
		}
		//		newAddress.setDefaultAddress(getUserFacade().isAddressBookEmpty() || getUserFacade().getAddressBook().size() == 1
		//				|| Boolean.TRUE.equals(addressForm.getDefaultAddress()));
		getMplCustomAddressFacade().setDeliveryAddress(newAddress);
	}

	//Fetch delivery mode details in responsive and web view
	/**
	 * @param model
	 * @param cartModel
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 * @throws UnsupportedEncodingException
	 * @throws CMSItemNotFoundException
	 */
	private void prepareModelForDeliveryMode(final Model model, final CartModel cartModel)
			throws VoucherOperationException, EtailNonBusinessExceptions, UnsupportedEncodingException, CMSItemNotFoundException
	{
		final CartModel serviceCart = cartModel;
		mplCouponFacade.releaseVoucherInCheckout(serviceCart);
		mplCartFacade.removeDeliveryModeWdoutRecalclt(serviceCart); //TISPT-104 // Cart recalculation method invoked inside this method
		modelForDeliveryMode(model, cartModel);
	}

	//used for review order. If an user removes a product from review order and want to see delivery option page
	/**
	 * @param model
	 * @param cartModel
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 * @throws UnsupportedEncodingException
	 * @throws CMSItemNotFoundException
	 */
	private void prepModelForDelModeWdoutRemDlMod(final Model model, final CartModel cartModel)
			throws VoucherOperationException, EtailNonBusinessExceptions, UnsupportedEncodingException, CMSItemNotFoundException
	{
		final CartModel serviceCart = cartModel;
		mplCouponFacade.releaseVoucherInCheckout(serviceCart);
		//mplCartFacade.removeDeliveryMode(serviceCart); //TISPT-104 // Cart recalculation method invoked inside this method
		modelForDeliveryMode(model, cartModel);
	}

	/**
	 * @param model
	 * @param cartModel
	 * @throws VoucherOperationException
	 * @throws EtailNonBusinessExceptions
	 * @throws EtailBusinessExceptions
	 * @throws CMSItemNotFoundException
	 */
	private void modelForDeliveryMode(final Model model, final CartModel cartModel)
			throws VoucherOperationException, EtailNonBusinessExceptions, EtailBusinessExceptions, CMSItemNotFoundException
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<PinCodeResponseData> responseData = null;
		final CartModel serviceCart = cartModel;
		final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
		final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);

		if (StringUtils.isNotEmpty(defaultPinCodeId) && (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries())))
		{
			//CAR-126/128/129
			responseData = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
			if (CollectionUtils.isEmpty(responseData))
			{
				responseData = mplCartFacade.getOMSPincodeResponseData(defaultPinCodeId, cartData, serviceCart);
			}
			if (CollectionUtils.isNotEmpty(responseData))
			{
				for (PinCodeResponseData pinCodeResponseData : responseData)
				{
					try
					{
						if (pinCodeResponseData != null && pinCodeResponseData.getIsServicable() != null
								&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
						{
							pinCodeResponseData = mplCartFacade.getVlaidDeliveryModesByInventory(pinCodeResponseData, cartData);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception occured while checking inventory " + e.getCause());
						LOG.error("Stack trace:", e);
					}
				}
				//  TISPRD-1951  END //
				deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartData, responseData, serviceCart);
				fullfillmentDataMap = mplCartFacade.getFullfillmentMode(cartData);

				//TIS-397
				//deliveryModeDataMap = mplCheckoutFacade.repopulateTshipDeliveryCost(deliveryModeDataMap, cartData);

				model.addAttribute("deliveryModeData", deliveryModeDataMap);
				model.addAttribute("deliveryMethodForm", new DeliveryMethodForm());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
				model.addAttribute("defaultPincode", defaultPinCodeId);

				//TISPRO-625

				//final Boolean isExpressCheckoutSelected = (serviceCart != null && serviceCart.getDeliveryAddress() != null) ? Boolean.TRUE: Boolean.FALSE;
				//model.addAttribute(MarketplacecheckoutaddonConstants.CART_EXPRESS_CHECKOUT_SELECTED, isExpressCheckoutSelected);

				//	this.prepareDataForPage(model);
				//model.addAttribute("metaRobots", "noindex,nofollow");
				//model.addAttribute("checkoutPageName", checkoutPageName);
				model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629
			}
			else
			{
				LOG.error("Unable to proced. responseData cannot be empty.");
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9048);
			}
		}
		else
		{
			LOG.error("Unable to populate model with fullfillment mode.");
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9041);
		}
	}

	/**
	 * DELIVERY METHOD SECTION:This will return a JSP page where in a user can select delivery modes
	 *
	 * @param model
	 * @param redirectAttributes
	 * @return String - delivery method selection JSP page
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHOOSEVALUE, method = RequestMethod.GET)
	public String enterDeliveryModeStep(final Model model, final RedirectAttributes redirectAttributes,
			@RequestParam(required = false, defaultValue = "false") final boolean isResponsive,
			@RequestParam(required = false, defaultValue = "false") final boolean isCallAfterRemoveCartItem)
			throws UnsupportedEncodingException
	{
		String returnPage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside enterDeliveryModeStep Method...");
			}
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartModel serviceCart = getCartService().getSessionCart();
			//setExpressCheckout(serviceCart);

			//TISST-13012
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(serviceCart); //TISPT-104
			if (cartItemDelistedStatus)
			{
				LOG.debug("enterDeliveryModeStep:Cart Item is delisted, Hence redirecting to cart.");
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			if (isCallAfterRemoveCartItem)
			{
				prepModelForDelModeWdoutRemDlMod(model, serviceCart);
			}
			else
			{
				prepareModelForDeliveryMode(model, serviceCart);
			}
			//final String is_responsive = getSessionService().getAttribute(MarketplacecommerceservicesConstants.IS_RESPONSIVE);

			if (isResponsive)
			{
				returnPage = "addon:/marketplacecheckoutaddon/fragments/checkout/single/showDeliveryModesDetailsMobile";
				//getSessionService().removeAttribute(MarketplacecommerceservicesConstants.IS_RESPONSIVE);
			}
			else
			{
				returnPage = MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.DeliveyModesSelectionPanel;
			}
		}

		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return returnPage;
	}

	/**
	 * Method will return page for delivery slot selection page for web
	 *
	 * @param model
	 * @return String - delivery slot selection JSP page
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL, method = RequestMethod.GET)
	public String selectDeleverySlots(final Model model) throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartData cartDataSupport = mplCartFacade.getSessionCartWithEntryOrdering(true);
			final List<String> deliveryModelList = new ArrayList<String>();
			final Map<String, String> selectedDateBetWeen = new LinkedHashMap<String, String>();
			final List<CartSoftReservationData> cartSoftReservationDataList = getSessionService()
					.getAttribute(MarketplacecommerceservicesConstants.RESERVATION_DATA_TO_SESSION);
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
						if (productRichAttributeModel != null && productRichAttributeModel.size() > 0
								&& null != productRichAttributeModel.get(0)
								&& null != productRichAttributeModel.get(0).getScheduledDelivery())
						{
							productRichAttr = productRichAttributeModel.get(0).getScheduledDelivery().toString();
						}
					}

					final SellerInformationModel sellerInfoModel = mplSellerInformationService
							.getSellerDetail(cartEntryData.getSelectedUssid());
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

								deliveryModelList
										.add(cartEntryData.getMplDeliveryMode().getCode() + MarketplacecommerceservicesConstants.COMMA
												+ productRichAttr + MarketplacecommerceservicesConstants.COMMA + sellerRichAttr
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

								deliveryModelList
										.add(cartEntryData.getMplDeliveryMode().getCode() + MarketplacecommerceservicesConstants.COMMA
												+ productRichAttr + MarketplacecommerceservicesConstants.COMMA + sellerRichAttr
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
				final InvReserForDeliverySlotsResponseData deliverySlotsResponseData = mplCartFacade
						.convertDeliverySlotsDatatoWsdto(deliverySlotsRequestData, cartModel);
				final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
				final MplLPHolidaysModel mplLPHolidaysModel = mplConfigFacade
						.getMplLPHolidays(MarketplacecommerceservicesConstants.CAMPAIGN_URL_ALL);
				final Map<String, List<String>> dateTimeslotMapList = new HashMap<String, List<String>>();



				final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
				mplCheckoutFacade.resetSlotEntries(cartEntryList);

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
									&& productScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)
									&& sellerScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES))
									|| (cartEntryData.getSelectedUssid().equalsIgnoreCase(selectedUssId)
											&& selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)
											&& productScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)
											&& sellerScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)))
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
											if (deliverySlotsResponse.getIsScheduled()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
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
					final String requestQueryParam = UriUtils
							.encodeQuery("?redirectString=" + "redirectToReviewOrder" + "&type=ajaxRedirect", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
					//return getCheckoutStep().nextStep();
				}
				String deliverySlotCharge = MarketplacecommerceservicesConstants.EMPTY;
				final DecimalFormat df = new DecimalFormat("#.00");
				if (configModel.getSdCharge() > 0)
				{
					//final CartModel cartModel = getCartService().getSessionCart();
					cartDataSupport
							.setDeliverySlotCharge(mplCheckoutFacade.createPrice(cartModel, Double.valueOf(configModel.getSdCharge())));
					deliverySlotCharge = df.format(configModel.getSdCharge());
				}
				fullfillmentDataMap = mplCartFacade.getFullfillmentMode(cartDataSupport);
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartDataSupport);
				model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
				model.addAttribute("mplconfigModel", deliverySlotCharge);
				model.addAttribute("defaultPincode",
						getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));

				final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
				final String currencySymbol = currency.getSymbol();
				model.addAttribute("currencySymbol", currencySymbol);
				model.addAttribute("deliverySlotForm", new DeliverySlotForm());

				//				this.prepareDataForPage(model);
				//			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				//				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				//				model.addAttribute(
				//						WebConstants.BREADCRUMBS_KEY,
				//						getResourceBreadcrumbBuilder().getBreadcrumbs(
				//								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				//				model.addAttribute("metaRobots", "noindex,nofollow");
				//				setCheckoutStepLinksForModel(model, getCheckoutStep());
				//return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliverySlotPage;
				return "addon:/marketplacecheckoutaddon/pages/checkout/single/showChooseDeliverySlotPage";
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while selecting address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while selecting  delivery slots:" + e);
			LOG.error("Stack trace:", e);
		}
		final String requestQueryParam = UriUtils.encodeQuery("?redirectString=" + "redirectToReviewOrder" + "&type=ajaxRedirect",
				UTF);
		return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
	}

	/**
	 * @author TECH This method first checks the delivery modes if it has only home or express then it forwards to
	 *         doSelectDeliveryMode method and if it contains all the modes then first process cnc mode and then at jsp
	 *         level using continue link forward to doSelectDeliveryMode method.
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response1
	 * @param session
	 * @return string
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETCNCSTRORES, method = RequestMethod.GET)
	public String doFindCncStores(final String entryNumber, final String sellerArticleSKU, final String deliveryCode,
			final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request,
			final HttpServletResponse response1, final HttpSession session)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from doFindCncStores method in Controller");
		}
		if (getUserFacade().isAnonymousUser())
		{
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		List<PinCodeResponseData> responseData = null;
		//TISPT-400
		if (getCartService().hasSessionCart())
		{
			final CartModel cartModel = getCartService().getSessionCart();

			try
			{
				final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cartModel);
				if (cartItemDelistedStatus)
				{
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
							MarketplacecommerceservicesConstants.TRUE_UPPER);
					final String requestQueryParam = UriUtils
							.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
					//return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
				}
			}
			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
				getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
				final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			catch (final Exception e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(
						new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
				LOG.error("Stack trace:", e);
				getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
				final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}


			List<StoreLocationResponseData> response = null;

			// CAR-197  start
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart(); //CAR-197

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);

			final String defaultPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			//Validating request
			responseData = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
			if (CollectionUtils.isEmpty(responseData))
			{
				responseData = mplCartFacade.getOMSPincodeResponseData(defaultPincode, cartData, cartModel);
			}
			//final boolean isValid = false;
			if (CollectionUtils.isNotEmpty(responseData))
			{

				//handle freebie
				final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
				final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

				final List<StoreLocationRequestData> storeLocationRequestDataList = new ArrayList<StoreLocationRequestData>();
				//count cnc products in cart entries
				int count = 0;
				//count other modes in cart entries
				final int delModeCount = 0;
				final int expCheckout = 0;
				double configurableRadius = 0;
				//retrieve pincode from session

				if (cartData != null && cartData.getEntries() != null) //CAR-197
				{
					//for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
					for (final OrderEntryData cartD : cartData.getEntries())
					{
						if (null != cartD && !cartD.isGiveAway() && null != entryNumber //CAR-197
								&& null != sellerArticleSKU && null != deliveryCode
								&& cartD.getEntryNumber().toString().equals(entryNumber))
						{
							try
							{

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

									final StoreLocationRequestData storeLocationRequestData = papulateClicknCollectRequesrData(
											cartD.getSelectedUssid(), myLocation.getGPS(), Double.valueOf(configurableRadius));
									storeLocationRequestDataList.add(storeLocationRequestData);

									//getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode, sellerArticleSKU, cartModel);
								}


								break;
							}
							catch (final ArrayIndexOutOfBoundsException exception)
							{
								LOG.error("Error in Store selection Page", exception);
								//return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
								final String requestQueryParam = UriUtils
										.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
								return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
							}
							catch (final EtailBusinessExceptions e)
							{
								ExceptionUtil.etailBusinessExceptionHandler(e, null);
								LOG.error("EtailBusinessExceptions Error in Store selection Page ", e);
								//return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
								final String requestQueryParam = UriUtils
										.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
								return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
							}
							catch (final Exception e)
							{
								LOG.error("Error in Store selection Page ", e);
								//return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
								final String requestQueryParam = UriUtils
										.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
								return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
							}
						}
					}

					//cart has CNC.
					LOG.info("Cart Entries contain CNC mode");
					//calls oms to get inventories for given stores.
					//populate freebie data
					populateFreebieProductData(cartModel, freebieModelMap, freebieParentQtyMap);
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
						if (null != e.getErrorCode() && ("O0001".equalsIgnoreCase(e.getErrorCode())
								|| "O0002".equalsIgnoreCase(e.getErrorCode()) || "O0007".equalsIgnoreCase(e.getErrorCode())))
						{
							//populates oms response to data object
							productWithPOS = getProductWdPos(model, freebieParentQtyMap, storeLocationRequestDataList);
						}
					}

					if (CollectionUtils.isNotEmpty(productWithPOS)
							&& CollectionUtils.isNotEmpty(productWithPOS.get(0).getPointOfServices())
							&& productWithPOS.get(0).getPointOfServices().size() > 0)
					{
						//populate logged in user details as pickup details
						final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
						final StringBuffer pickupPerson = new StringBuffer();

						pickupPerson.append((null != customer.getFirstName()) ? customer.getFirstName() : "").append(" ")
								.append((null != customer.getLastName()) ? customer.getLastName() : "");

						final String pickupPersonName = pickupPerson.toString();
						final String pickUpPersonMobile = (null != customer.getMobileNumber()) ? customer.getMobileNumber() : "";

						model.addAttribute("entryNumber", entryNumber);
						model.addAttribute("pickupPersonName", pickupPersonName.trim());
						model.addAttribute("pickUpPersonMobile", pickUpPersonMobile);
						model.addAttribute("delModeCount", Integer.valueOf(delModeCount));
						model.addAttribute("cnccount", Integer.valueOf(count));
						model.addAttribute("expCheckout", Integer.valueOf(expCheckout));
						model.addAttribute("defaultPincode", defaultPincode);
						model.addAttribute("pwpos", productWithPOS);
						model.addAttribute("storesAvailable", Boolean.TRUE);
					}
					else
					{
						model.addAttribute("storesAvailable", Boolean.FALSE);
					}
					model.addAttribute("CSRFToken", CSRFTokenManager.getTokenForSession(request.getSession()));

				}

				//storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				//setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				//		/TPR-1803
				//	setCheckoutStepLinksForModel(model, getCheckoutStep());
				//		model.addAttribute("checkoutPageName", checkoutPageName);
				//model.addAttribute("progressBarClass", "choosePage");

			}
			else
			{
				final String requestQueryParam = UriUtils.encodeQuery("?msg=" + "USSID not CNC serviceable" + "&type=error", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
		}
		else
		{
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.PickupLocationSelectionPanel;
	}

	/**
	 * This method is called when a user select change pincode at cnc page.
	 *
	 * @param pin
	 * @param productCode
	 * @param ussId
	 * @return if successful return list of pos for a product else null.
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.UPDATE_CHECK_PINCODE, method = RequestMethod.GET)
	public String upDatePincodeServicabilityCheck(@RequestParam(value = "pin") final String pin,
			@RequestParam(value = "productCode") final String productCode, @RequestParam(value = "sellerId") final String ussId,
			@RequestParam(value = "entryNumber") final String entryNumber, final Model model)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from upDatePincodeServicabilityCheck method when customer change pincode at cnc page");
			LOG.debug("New Pincode is::::::::" + pin);
			LOG.debug("Ussid is::::::::" + ussId);
			LOG.debug("entryNumber is::::::::" + entryNumber);
		}
		//final List<PointOfServiceData> stores = new ArrayList<PointOfServiceData>();
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
			if (null != e.getErrorCode() && ("O0001".equalsIgnoreCase(e.getErrorCode()) || "O0002".equalsIgnoreCase(e.getErrorCode())
					|| "O0007".equalsIgnoreCase(e.getErrorCode())))
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
			//stores = productWithPOS.get(0).getPointOfServices();

			model.addAttribute("entryNumber", entryNumber);
			model.addAttribute("defaultPincode", pin);
			model.addAttribute("pwpos", productWithPOS);
		}

		if (!status)
		{
			final String requestQueryParam = UriUtils.encodeQuery("?msg=" + "noStoresFound" + "&type=errorCode", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.PickupLocationFragmentPanel;
	}

	/**
	 * This method gets called when the "Proceed" button is clicked. It sets the selected delivery mode.
	 *
	 * @param model
	 *           - the id of the delivery mode
	 * @param deliveryMethodForm
	 * @param bindingResult
	 * @return - a URL to the page to load.
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTURL, method = RequestMethod.POST)
	public String doSelectDeliveryMode(@ModelAttribute("deliveryMethodForm") final DeliveryMethodForm deliveryMethodForm,
			final BindingResult bindingResult, final Model model, final HttpSession session)
			throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartModel cartModel = getCartService().getSessionCart();

			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cartModel);
			//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				LOG.debug("doSelectDeliveryMode:Cart Item is delisted, Hence redirecting to cart");
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//INC144315801 starts
			if (deliveryMethodForm.getDeliveryMethodEntry() == null)
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?msg=" + "Unable to set delivery options, Try again." + "&type=error", UTF);
				return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//INC144315801 ends

			Double finalDeliveryCost = Double.valueOf(0.0);

			{
				//create session object for deliveryMethodForm which will be used if cart contains both cnc and home delivery.
				session.setAttribute("deliveryMethodForm", deliveryMethodForm);
				//TISPT-400
				finalDeliveryCost = populateMplZoneDeliveryMode(deliveryMethodForm, cartModel);
				final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
				getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost, deliveryChargePromotionMap, cartModel); //TIS 400

			}

			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

			applyPromotions();

			//populate freebie data
			populateFreebieProductData(cartModel, freebieModelMap, freebieParentQtyMap);

			//getMplCheckoutFacade().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap); //TISPT-169

			LOG.debug(">>>>>>>>>>  Step 2  :Freebie data preparation ");

			//timeOutSet(model);

			/*** Inventory Soft Reservation Start ***/
			//TPR3780 STARTS HERE
			//	final CartModel cartModelBeforeinventoryCheck = getCartService().getSessionCart();
			//	final double prevTotalCartPrice = cartModelBeforeinventoryCheck.getTotalPrice().doubleValue();
			//TPR3780 ENDS HERE

			//commented for CAR:127

			/*
			 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
			 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartModel);
			 */

			//CAR:127
			//final CartData caData = getMplCartFacade().getCartDataFromCartModel(cartModel, false);
			final CartData cartUssidData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			//TISST-13012

			final boolean inventoryReservationStatus = mplCartFacade
					.isInventoryReserved(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartUssidData, cartModel);

			if (!inventoryReservationStatus)
			{
				getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			Boolean replaced = Boolean.FALSE;
			replaced = getSessionService().getAttribute("replacedUssid");
			//TPR3780 STARTS HERE


			if (null != replaced && replaced.booleanValue())
			{
				//final String updateStatus = "updated";
				final String updateStatus = MarketplacecheckoutaddonConstants.UPDATED;
				//final String totalCartPriceAsString = String.valueOf(newTotalCartPrice);
				//redirectAttributes.addFlashAttribute("flashupdateStatus", updateStatus);
				//redirectAttributes.addFlashAttribute("flashtotalCartPriceAsString", totalCartPriceAsString);
				//	return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
				getSessionService().setAttribute("flashupdateStatus", updateStatus);
				getSessionService().removeAttribute("replacedUssid");
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			//TPR3780 ENDS HERE

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
					responseData = mplCartFacade.getOMSPincodeResponseData(defaultPinCodeId, cartUssidData, cartModel);
					final boolean isCOdEligible = mplCartFacade.addCartCodEligible(deliveryModeDataMap, responseData, cartModel,
							cartUssidData);
					LOG.info("isCOdEligible " + isCOdEligible);
				}
				deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartUssidData, responseData, cartModel);

				mplCartFacade.setDeliveryDate(cartUssidData, responseData);

			}
			final CartData cartDataSupport = mplCartFacade.getSessionCartWithEntryOrdering(true);
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
						+ MarketplacecheckoutaddonConstants.MPLSINGLEPAGEURL + MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL);
				return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLSINGLEPAGEURL
						+ MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL;
			}
			else
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?redirectString=" + "redirectToReviewOrder" + "&type=ajaxRedirect", UTF);
				return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error("CMSItemNotFoundException  while  selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  selecting delivery mode:" + e.getErrorCode(), e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exception  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return REDIRECT_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
	}

	/**
	 * This method gets called when the "Payment Type radio" button is clicked. It sets the selected delivery mode on the
	 * checkout facade and will perform inventory reservation and other calls.For Responsive design
	 *
	 * @param model
	 *           - the id of the delivery mode
	 * @param deliveryMethodForm
	 * @param bindingResult
	 * @return - a URL to the page to load.
	 * @throws JSONException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTURLRESPONSIVE, method = RequestMethod.POST)
	public @ResponseBody JSONObject doSelectDeliveryModeResponsive(
			@ModelAttribute("deliveryMethodForm") final DeliveryMethodForm deliveryMethodForm, final BindingResult bindingResult,
			final Model model, final HttpSession session) throws JSONException
	{
		final JSONObject jsonObj = new JSONObject();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}
			final CartModel cartModel = getCartService().getSessionCart();

			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cartModel);
			//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				jsonObj.put("url", "/cart");
				jsonObj.put("type", "redirect");
				return jsonObj;
			}
			//TISPT-400 ends

			Double finalDeliveryCost = Double.valueOf(0.0);

			{
				//create session object for deliveryMethodForm which will be used if cart contains both cnc and home delivery.
				session.setAttribute("deliveryMethodForm", deliveryMethodForm);
				//TISPT-400
				finalDeliveryCost = populateMplZoneDeliveryMode(deliveryMethodForm, cartModel);
				final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
				getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost, deliveryChargePromotionMap, cartModel); //TIS 400
				jsonObj.put("isDeliveryModeSet", "true");
			}

			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

			//	applyPromotions();

			//populate freebie data
			populateFreebieProductData(cartModel, freebieModelMap, freebieParentQtyMap);


			LOG.debug(">>>>>>>>>>  Step 2  :Freebie data preparation ");


			/*** Inventory Soft Reservation Start ***/

			final CartData cartUssidData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			//TISST-13012

			final boolean inventoryReservationStatus = mplCartFacade
					.isInventoryReserved(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartUssidData, cartModel);
			if (inventoryReservationStatus)
			{
				jsonObj.put("isInventoryReserved", "true");
				jsonObj.put("type", "response");
			}
			final List<CartSoftReservationData> cartSoftReservationDataList = getSessionService()
					.getAttribute(MarketplacecommerceservicesConstants.RESERVATION_DATA_TO_SESSION);
			LOG.debug("****************cartSoftReservationDataList :" + cartSoftReservationDataList.toString());


			if (!inventoryReservationStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				jsonObj.put("url", "/cart");
				jsonObj.put("type", "redirect");
				return jsonObj;
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
					responseData = mplCartFacade.getOMSPincodeResponseData(defaultPinCodeId, cartUssidData, cartModel);
					final boolean isCOdEligible = mplCartFacade.addCartCodEligible(deliveryModeDataMap, responseData, cartModel,
							cartUssidData);
					LOG.info("isCOdEligible " + isCOdEligible);
				}
				deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartUssidData, responseData, cartModel);

				mplCartFacade.setDeliveryDate(cartUssidData, responseData);

			}
			final CartData cartDataSupport = mplCartFacade.getSessionCartWithEntryOrdering(true);

			if (getSlotsAvailability(cartDataSupport))
			{
				final Map<String, Map<String, List<String>>> deliveryTimeSlotMap = getSessionService()
						.getAttribute(MarketplacecheckoutaddonConstants.DELIVERY_SLOTS_TO_SESSION);
				if (null != deliveryTimeSlotMap)
				{
					final JSONObject dlvrySltAvlbleForUssid = new JSONObject();
					for (final Map.Entry<String, Map<String, List<String>>> entry : deliveryTimeSlotMap.entrySet())
					{
						//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
						if (null != entry.getValue())
						{
							dlvrySltAvlbleForUssid.put(entry.getKey(), "true");
						}
					}
					jsonObj.put("dlvrySltAvlbleForUssid", dlvrySltAvlbleForUssid);
				}
				jsonObj.put("isScheduleServiceble", "true");
				jsonObj.put("type", "response");
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			jsonObj.put("url", "/cart");
			jsonObj.put("type", "redirect");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			jsonObj.put("url", "/cart");
			jsonObj.put("type", "redirect");
		}
		catch (final Exception e)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exception  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			jsonObj.put("url", "/cart");
			jsonObj.put("type", "redirect");
		}
		return jsonObj;
	}

	/**
	 * @param cartDataSupport
	 * @return isScheduleServiceble
	 */
	private boolean getSlotsAvailability(final CartData cartDataSupport)
	{
		final List<String> deliveryModelList = new ArrayList<String>();
		final Map<String, String> selectedDateBetWeen = new LinkedHashMap<String, String>();
		final List<CartSoftReservationData> cartSoftReservationDataList = getSessionService()
				.getAttribute(MarketplacecommerceservicesConstants.RESERVATION_DATA_TO_SESSION);
		boolean isScheduleServiceble = false;
		final Map<String, Map<String, List<String>>> deliveryTimeSlotMap = new HashMap<String, Map<String, List<String>>>();
		LOG.debug("****************cartSoftReservationDataList :" + cartSoftReservationDataList.toString());
		try
		{
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
						if (productRichAttributeModel != null && productRichAttributeModel.get(0).getScheduledDelivery() != null)
						{
							productRichAttr = productRichAttributeModel.get(0).getScheduledDelivery().toString();
						}
					}

					final SellerInformationModel sellerInfoModel = mplSellerInformationService
							.getSellerDetail(cartEntryData.getSelectedUssid());
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

								deliveryModelList
										.add(cartEntryData.getMplDeliveryMode().getCode() + MarketplacecommerceservicesConstants.COMMA
												+ productRichAttr + MarketplacecommerceservicesConstants.COMMA + sellerRichAttr
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

								deliveryModelList
										.add(cartEntryData.getMplDeliveryMode().getCode() + MarketplacecommerceservicesConstants.COMMA
												+ productRichAttr + MarketplacecommerceservicesConstants.COMMA + sellerRichAttr
												+ MarketplacecommerceservicesConstants.COMMA + cartEntryData.getSelectedUssid()
												+ MarketplacecommerceservicesConstants.COMMA + cartSoftReservationData.getFulfillmentType());
							}
						}
					}
				}
			}
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
				final InvReserForDeliverySlotsResponseData deliverySlotsResponseData = mplCartFacade
						.convertDeliverySlotsDatatoWsdto(deliverySlotsRequestData, cartModel);
				//			final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
				final MplLPHolidaysModel mplLPHolidaysModel = mplConfigFacade
						.getMplLPHolidays(MarketplacecommerceservicesConstants.CAMPAIGN_URL_ALL);
				final Map<String, List<String>> dateTimeslotMapList = new HashMap<String, List<String>>();



				final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
				mplCheckoutFacade.resetSlotEntries(cartEntryList);


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

					//final Map<String, Map<String, List<String>>> deliveryTimeSlotMap = new HashMap<String, Map<String, List<String>>>();
					for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
					{
						if (!cartEntryData.isIsBOGOapplied() || !cartEntryData.isGiveAway())
						{
							if ((cartEntryData.getSelectedUssid().equalsIgnoreCase(selectedUssId)
									&& selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)
									&& productScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)
									&& sellerScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES))
									|| (cartEntryData.getSelectedUssid().equalsIgnoreCase(selectedUssId)
											&& selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)
											&& productScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)
											&& sellerScheduleDelivery.equalsIgnoreCase(MarketplacecommerceservicesConstants.YES)))
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
											if (deliverySlotsResponse.getIsScheduled()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
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
						deliveryTimeSlotMap.put(cartEntryData.getSelectedUssid(), cartEntryData.getDeliverySlotsTime());
					}
				}
				//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.DELIVERY_SLOTS_TO_SESSION, selectedDateBetWeen);
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.DELIVERY_SLOTS_TO_SESSION, deliveryTimeSlotMap);
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
			LOG.error("Exception occured in getSlotAvailability:" + e);
			LOG.error("Stack trace:", e);
		}
		return isScheduleServiceble;
	}


	/**
	 * @Description slot delivery page for responsive
	 * @param model
	 * @return showChooseDeliverySlotPage,slot delivery page for responsive
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.SLOTDELIVERYRESPONSIVE, method = RequestMethod.GET)
	public String selectDeliverySlotResponsive(final Model model) throws UnsupportedEncodingException
	{
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
			Map<String, Map<String, List<String>>> deliveryTimeSlotMap = new HashMap<String, Map<String, List<String>>>();
			final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
			String deliverySlotCharge = MarketplacecommerceservicesConstants.EMPTY;
			final DecimalFormat df = new DecimalFormat("#.00");
			final CartModel cartModel = getCartService().getSessionCart();
			//final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
			//			boolean isSaveRequired = false;
			//			for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
			//			{
			//				if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
			//				{
			//
			//					if (null != cartEntryModel.getEdScheduledDate() && StringUtils.isNotEmpty(cartEntryModel.getEdScheduledDate()))
			//					{
			//						cartEntryModel.setEdScheduledDate("".trim());
			//						cartEntryModel.setTimeSlotFrom("".trim());
			//						cartEntryModel.setTimeSlotTo("".trim());
			//						//UF-281
			//						if (cartEntryModel.getScheduledDeliveryCharge() != null
			//								&& cartEntryModel.getScheduledDeliveryCharge().doubleValue() != 0.0)
			//						{
			//							isSaveRequired = true;
			//
			//							cartEntryModel.setScheduledDeliveryCharge(Double.valueOf(0));
			//						}
			//					}
			//				}
			//			}
			//			if (isSaveRequired)
			//			{
			//				modelService.save(cartModel);
			//				modelService.refresh(cartModel);
			//			}
			//End of UF-281

			final CartData cartDataSupport = mplCartFacade.getSessionCartWithEntryOrdering(true);
			if (configModel.getSdCharge() > 0)
			{
				cartDataSupport
						.setDeliverySlotCharge(mplCheckoutFacade.createPrice(cartModel, Double.valueOf(configModel.getSdCharge())));
				deliverySlotCharge = df.format(configModel.getSdCharge());
			}

			deliveryTimeSlotMap = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.DELIVERY_SLOTS_TO_SESSION);
			for (final OrderEntryData cartEntryData : cartDataSupport.getEntries())
			{
				for (final Map.Entry<String, Map<String, List<String>>> entry : deliveryTimeSlotMap.entrySet())
				{
					if (entry.getKey().equalsIgnoreCase(cartEntryData.getSelectedUssid()))
					{
						cartEntryData.setDeliverySlotsTime(entry.getValue());
					}
				}
			}
			fullfillmentDataMap = mplCartFacade.getFullfillmentMode(cartDataSupport);
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartDataSupport);
			model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
			model.addAttribute("mplconfigModel", deliverySlotCharge);
			model.addAttribute("defaultPincode",
					getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));
			getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.DELIVERY_SLOTS_TO_SESSION);
			final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
			final String currencySymbol = currency.getSymbol();
			model.addAttribute("currencySymbol", currencySymbol);
			model.addAttribute("deliverySlotForm", new DeliverySlotForm());
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  saving new address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while saving new address ", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/showChooseDeliverySlotPage";
	}

	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETREVIEWORDER, method = RequestMethod.GET)
	public String viewReviewOrder(final Model model) throws UnsupportedEncodingException
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			final CartModel cartModel = cartService.getSessionCart();
			mplCartFacade.setCartSubTotalForReviewOrder(cartModel);
			mplCartFacade.totalMrpCal(cartModel);
			//UF-260
			GenericUtilityMethods.getCartPriceDetails(model, cartModel, null);
			final List<PinCodeResponseData> responseData = getSessionService()
					.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
			final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			final Map<String, MarketplaceDeliveryModeData> delModeDataPopulateMap = mplCartFacade
					.getDeliveryModeMapForReviewOrder(cartData, responseData);
			model.addAttribute("deliveryModeData", delModeDataPopulateMap);
			final String defaultPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			model.addAttribute("defaultPincode", defaultPincode);
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);

			final Map<String, PriceData> mrpPriceMap = new HashMap<String, PriceData>();

			for (final OrderEntryData entryLatest : cartData.getEntries())
			{
				final long priceForStrikeOff = ((entryLatest.getMrp().getValue().longValue())
						* entryLatest.getQuantity().longValue());
				final BigDecimal strikeOffPrice = new BigDecimal(priceForStrikeOff);
				final PriceData strikeoffprice = priceDataFactory.create(PriceDataType.BUY, strikeOffPrice,
						MarketplaceFacadesConstants.INR);


				model.addAttribute("strikeoffprice", strikeoffprice);

				//TPR-774
				final BigDecimal mrptotal = new BigDecimal(
						entryLatest.getMrp().getValue().doubleValue() * entryLatest.getQuantity().doubleValue());
				final PriceData mrpTotalPrice = priceDataFactory.create(PriceDataType.BUY, mrptotal, MarketplaceFacadesConstants.INR);
				mrpPriceMap.put(entryLatest.getEntryNumber().toString(), mrpTotalPrice);
				//TPR-774
			}
			fullfillmentDataMap = mplCartFacade.getFullfillmentMode(cartData);
			model.addAttribute(ModelAttributetConstants.CART_FULFILMENTDATA, fullfillmentDataMap);
			model.addAttribute(ModelAttributetConstants.MRPPRICEMAP, mrpPriceMap);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :", e);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.ReviewOrder;
	}

	@RequestMapping(value = "/deliveryAddress", method = RequestMethod.GET)
	public @ResponseBody JSONObject getDeliveryAddress() throws JSONException
	{
		final JSONObject jsonObject = new JSONObject();
		String addressLine = null;
		final String emptySpace = " ";
		final String countrycode = "IN,";
		final String phoneCode = "+91";
		final String comma = ",";

		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				jsonObject.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObject.put("type", "redirect");
				return jsonObject;
			}
			final AddressData addressData = mplCustomAddressFacade.getDeliveryAddress();
			final StringBuilder pickupPerson = new StringBuilder();
			pickupPerson.append((null != addressData.getFirstName()) ? addressData.getFirstName() : "").append(" ")
					.append((null != addressData.getLastName()) ? addressData.getLastName() : "");

			final String pickUpPersonMobile = (null != addressData.getPhone()) ? addressData.getPhone() : "";
			jsonObject.put("firstName", addressData.getFirstName());
			jsonObject.put("lastName", addressData.getLastName());
			jsonObject.put("pickupPersonName", pickupPerson.toString());
			jsonObject.put("pickupPersonMobileNo", pickUpPersonMobile);
			final String Line1 = addressData.getLine1().concat(comma);
			if (StringUtils.isNotEmpty(addressData.getLine2()))
			{
				addressLine = Line1.concat(emptySpace).concat(addressData.getLine2()).concat(comma);
			}
			if (StringUtils.isNotEmpty(addressData.getLine3()))
			{
				addressLine = addressLine.concat(emptySpace).concat(addressData.getLine3()).concat(comma);
			}
			if (StringUtils.isNotEmpty(addressLine))
			{
				addressLine = addressLine.concat(emptySpace).concat(addressData.getTown()).concat(comma).concat(emptySpace)
						.concat(addressData.getState()).concat(comma).concat(emptySpace).concat(addressData.getPostalCode())
						.concat(emptySpace);
			}
			else
			{
				addressLine = Line1.concat(emptySpace).concat(addressData.getTown()).concat(comma).concat(emptySpace)
						.concat(addressData.getState()).concat(comma).concat(emptySpace).concat(addressData.getPostalCode())
						.concat(emptySpace);
			}
			final String countryAndPhone = countrycode.concat(emptySpace).concat(phoneCode).concat(emptySpace)
					.concat(addressData.getPhone());
			addressLine = addressLine.concat(countryAndPhone);
			jsonObject.put("fullAddress", addressLine);
			jsonObject.put("type", "response");

		}
		catch (final JSONException e)
		{
			LOG.error("JSONException  while fetching selected delivery address ", e);
			jsonObject.put("displaymessage", "jsonExceptionMsg");
			jsonObject.put("type", "errorCode");
		}
		catch (final Exception e)
		{
			LOG.error("Exception  while fetching selected delivery address ", e);
			jsonObject.put("displaymessage", "jsonExceptionMsg");
			jsonObject.put("type", "errorCode");
		}
		return jsonObject;
	}

	@RequestMapping(value = "/deliveryModesSelected", method = RequestMethod.GET)
	public @ResponseBody JSONObject deliveryModesSelected() throws JSONException
	{
		final JSONObject jsonObject = new JSONObject();
		JSONObject jsonObjectSelected = null;
		final JSONObject jsonEntryWiseDelMode = new JSONObject();
		int homeDelivery = 0;
		int expressDelivery = 0;
		int clickNcollect = 0;
		int countItems = 0;
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				jsonObject.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObject.put("type", "redirect");
				return jsonObject;
			}
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			for (final OrderEntryData cartD : cartData.getEntries())
			{
				jsonObjectSelected = new JSONObject();
				final MarketplaceDeliveryModeData marketplaceDeliveryModeData = cartD.getMplDeliveryMode();

				jsonObjectSelected.put("ussid", cartD.getSelectedUssid());
				jsonObjectSelected.put("deliveryMode", marketplaceDeliveryModeData.getCode());
				if (marketplaceDeliveryModeData.getCode().equalsIgnoreCase("home-delivery"))
				{
					homeDelivery++;
				}
				if (marketplaceDeliveryModeData.getCode().equalsIgnoreCase("express-delivery"))
				{
					expressDelivery++;
				}
				if (!cartD.isGiveAway() && marketplaceDeliveryModeData.getCode().equalsIgnoreCase("click-and-collect"))
				{
					jsonObjectSelected.put("storeName", cartD.getDeliveryPointOfService().getName());
					clickNcollect++;
				}
				jsonEntryWiseDelMode.put(cartD.getEntryNumber().toString(), jsonObjectSelected);
				countItems++;
			}

			jsonObject.put("totalPrice", cartData.getTotalPrice().getFormattedValueNoDecimal());
			jsonObject.put("hd", homeDelivery);
			jsonObject.put("ed", expressDelivery);
			jsonObject.put("cnc", clickNcollect);
			jsonObject.put("CountItems", countItems);
			jsonObject.put("deliveryDetails", jsonEntryWiseDelMode);
			jsonObject.put("type", "response");
		}
		catch (final JSONException e)
		{
			LOG.error("JSONException  while fetching selected delivery address ", e);
			jsonObject.put("displaymessage", "jsonExceptionMsg");
			jsonObject.put("type", "errorCode");
		}
		catch (final Exception e)
		{
			LOG.error("Exception  while fetching selected delivery address ", e);
			jsonObject.put("displaymessage", "jsonExceptionMsg");
			jsonObject.put("type", "errorCode");
		}
		return jsonObject;
	}

	private void prepareModelForPayment(final Model model, final CartData cartData) throws CMSItemNotFoundException
	{
		Map<String, Boolean> paymentModeMap = null;
		model.addAttribute(MarketplacecheckoutaddonConstants.CARTTOORDERCONVERT, Boolean.FALSE);
		model.addAttribute(MarketplacecheckoutaddonConstants.GUID, cartData.getGuid());
		//Getting Payment modes
		paymentModeMap = getMplPaymentFacade().getPaymentModes(MarketplacecheckoutaddonConstants.MPLSTORE, false, cartData);
		prepareDataForPage(model);
		model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODES, paymentModeMap);
		model.addAttribute(MarketplacecheckoutaddonConstants.TRANERRORMSG, "");
		//timeOutSet(model);
		//creating new Payment Form
		final PaymentForm paymentForm = new PaymentForm();
		//setting silent orders
		setupSilentOrderPostPage(paymentForm, model, null, cartData);
	}

	@RequestMapping(value = "/validatePayment", method = RequestMethod.GET)
	public @ResponseBody JSONObject validatePaymentDetails(final Model model, final RedirectAttributes redirectAttributes)
			throws UnsupportedEncodingException, JSONException
	{
		final JSONObject jsonObj = new JSONObject();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}
			final CartModel cartModel = getCartService().getSessionCart();
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			ValidationResults validationResult = null;

			//Below code is not required as this section is suuposed to execute only for payment failure scenario,Where as on payment failure this method is never called.
			//			final String refNumber = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.REFNUMBER);
			//
			//			LOG.debug("refNumber number is ...................." + refNumber);
			//			//Implementing mRupee Logic with cartGuid TISSQAEE-229
			//			OrderModel mRupeeorderModel = null;
			//			String cartGuid = null;
			//			/* Getting guid from audit table based on the reference no. received from mRupee */
			//			if (StringUtils.isNotEmpty(refNumber))
			//			{
			//				cartGuid = getMplPaymentFacade().getWalletAuditEntries(refNumber);
			//			}
			//			LOG.debug("cartGuid number is ...................." + cartGuid);
			//			if (StringUtils.isNotEmpty(cartGuid))
			//			{
			//				mRupeeorderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			//			}
			//			LOG.debug("mRupeeorderModel is ++++++++" + mRupeeorderModel);
			//			if (null != mRupeeorderModel && null != mRupeeorderModel.getIsWallet()
			//					&& !WalletEnum.MRUPEE.equals(mRupeeorderModel.getIsWallet()))
			//			{
			//				LOG.debug("mRupeeorderModel.getIsWallet() is ++++++++" + mRupeeorderModel.getIsWallet());
			//				validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
			//			}

			validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
			if (null != validationResult && ValidationResults.REDIRECT_TO_CART.equals(validationResult))
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}

			//final boolean selectPickupDetails = false;
			//code to restrict user to continue the checkout if he has not selected pickup person name and mobile number.
			//this is only when cart entry contains cnc delivery mode.
			// TPR-429 START

			//final String checkoutSellerID = populateCheckoutSellers(cartData);
			//model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, checkoutSellerID);
			// TPR-429 END

			if (cartData != null)
			{
				//cartModel.setIsExpressCheckoutSelected(Boolean.valueOf(true));
				//getModelService().save(cartModel);
				boolean deliveryPosPresent = false;
				final String pickupPersonName = cartData.getPickupPersonName();
				final String pickupPersonMobile = cartData.getPickupPersonMobile();
				for (final OrderEntryData orderEntry : cartData.getEntries())
				{
					if (null != orderEntry.getDeliveryPointOfService())
					{
						deliveryPosPresent = true;
						break;
					}
				}
				if (deliveryPosPresent && (StringUtils.isEmpty(pickupPersonName) || StringUtils.isEmpty(pickupPersonMobile)))
				{
					jsonObj.put("url", "/checkout/single");
					jsonObj.put("type", "redirect");
					return jsonObj;
				}
				if (!deliveryPosPresent && (StringUtils.isNotEmpty(pickupPersonName) || StringUtils.isNotEmpty(pickupPersonMobile)))
				{
					cartModel.setPickupPersonName(null);
					cartModel.setPickupPersonMobile(null);
					modelService.save(cartModel);
					modelService.refresh(cartModel);
				}
			}

			//Moved to single method in facade TPR-629
			getMplPaymentFacade().populateDeliveryPointOfServ(cartModel);

			//TISST-13012
			//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart()); TISPT-169
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cartModel); //Changed to cartModel as it is already assigned from session(reduce session call)

			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				jsonObj.put("url", "/cart");
				jsonObj.put("type", "redirect");
				return jsonObj;
			}

			// OrderIssues:- Set the value duplicatJuspayResponse in session to false  ones cart GUID available in session
			final Map<String, Boolean> duplicatJuspayResponseMap = new HashMap<String, Boolean>();
			duplicatJuspayResponseMap.put(cartData.getGuid(), Boolean.FALSE);

			getSessionService().setAttribute(MarketplacecommerceservicesConstants.DUPLICATEJUSPAYRESONSE, duplicatJuspayResponseMap);

			//model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629

			//model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			//UF-260
			GenericUtilityMethods.getCartPriceDetails(model, cartModel, null);
			jsonObj.put("validation", "success");
			jsonObj.put("subTotalPrice", model.asMap().get("cartTotalMrp"));
			jsonObj.put("totalPrice", cartData.getTotalPriceWithConvCharge().getFormattedValueNoDecimal());
			jsonObj.put("type", "response");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}

		return jsonObj;
	}

	@RequestMapping(value = "/payment/orderDetails", method = RequestMethod.GET)
	public String getOrderDetailsTag(final Model model) throws UnsupportedEncodingException
	{
		if (getUserFacade().isAnonymousUser())
		{
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		final CartModel cartModel = getCartService().getSessionCart();
		//UF-260
		GenericUtilityMethods.getCartPriceDetails(model, cartModel, null);
		final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(false);
		model.addAttribute("cartData", cartData);
		model.addAttribute("isCart", Boolean.TRUE);
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.OrderTotals;
	}

	//Method not used any more
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String getPaymentPage(final Model model, final RedirectAttributes redirectAttributes,
			@RequestParam(value = "value", required = false, defaultValue = "") final String guid)
			throws UnsupportedEncodingException
	{

		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			ValidationResults validationResult = null;

			final String refNumber = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.REFNUMBER);

			LOG.debug("refNumber number is ...................." + refNumber);

			//Implementing mRupee Logic with cartGuid TISSQAEE-229
			OrderModel mRupeeorderModel = null;
			String cartGuid = null;
			/* Getting guid from audit table based on the reference no. received from mRupee */
			if (StringUtils.isNotEmpty(refNumber))
			{
				cartGuid = getMplPaymentFacade().getWalletAuditEntries(refNumber);
			}
			LOG.debug("cartGuid number is ...................." + cartGuid);
			if (StringUtils.isNotEmpty(cartGuid))
			{
				mRupeeorderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}

			LOG.debug("mRupeeorderModel is ++++++++" + mRupeeorderModel);

			//Implementing mRupee Logic with cartGuid TISSQAEE-229
			if (StringUtils.isEmpty(guid) && null != mRupeeorderModel && null != mRupeeorderModel.getIsWallet()
					&& !WalletEnum.MRUPEE.equals(mRupeeorderModel.getIsWallet()))
			{
				LOG.debug("mRupeeorderModel.getIsWallet() is ++++++++" + mRupeeorderModel.getIsWallet());
				validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
			}
			if (null != validationResult && ValidationResults.REDIRECT_TO_CART.equals(validationResult))
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			boolean selectPickupDetails = false;
			//Payment page will be shown based on cart or order(after first failure payment) TPR-629
			OrderModel orderModel = null;
			//Implementing mRupee Logic with cartGuid TISSQAEE-229
			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}
			else if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}
			//code to restrict user to continue the checkout if he has not selected pickup person name and mobile number.
			//this is only when cart entry contains cnc delivery mode.
			final Map<String, MarketplaceDeliveryModeData> freebieModelMap = new HashMap<String, MarketplaceDeliveryModeData>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
			Map<String, Boolean> paymentModeMap = null;
			OrderData orderData = null;
			if (null == orderModel)
			{
				//Existing code


				// TPR-429 START

				//final String checkoutSellerID = populateCheckoutSellers(cartData);
				//model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, checkoutSellerID);
				// TPR-429 END

				if (cartData != null)
				{
					//cartModel.setIsExpressCheckoutSelected(Boolean.valueOf(true));
					//getModelService().save(cartModel);

					for (final OrderEntryData orderEntry : cartData.getEntries())
					{
						if (null != orderEntry.getDeliveryPointOfService())
						{
							final String pickupPersonName = cartData.getPickupPersonName();
							final String pickupPersonMobile = cartData.getPickupPersonMobile();
							if ((pickupPersonName == null) || (pickupPersonMobile == null))
							{
								selectPickupDetails = true;
								model.addAttribute("selectPickupDetails", Boolean.valueOf(selectPickupDetails));
								//return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/check";
							}
						}
						if (!orderEntry.isGiveAway() && orderEntry.getSelectedUssid() != null)
						{
							freebieModelMap.put(orderEntry.getSelectedUssid(), orderEntry.getMplDeliveryMode());
							freebieParentQtyMap.put(orderEntry.getSelectedUssid(), orderEntry.getQuantity());
						}
					}
				}

				final CartModel cartModel = getCartService().getSessionCart();
				//Moved to single method in facade TPR-629
				getMplPaymentFacade().populateDeliveryPointOfServ(cartModel);

				//TISST-13012
				//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart()); TISPT-169
				final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cartModel); //Changed to cartModel as it is already assigned from session(reduce session call)

				if (cartItemDelistedStatus)
				{
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
							MarketplacecommerceservicesConstants.TRUE_UPPER);
					final String requestQueryParam = UriUtils
							.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}

				//Getting Payment modes
				paymentModeMap = getMplPaymentFacade().getPaymentModes(MarketplacecheckoutaddonConstants.MPLSTORE, false, cartData);

				// OrderIssues:- Set the value duplicatJuspayResponse in session to false  ones cart GUID available in session
				final Map<String, Boolean> duplicatJuspayResponseMap = new HashMap<String, Boolean>();
				duplicatJuspayResponseMap.put(cartData.getGuid(), Boolean.FALSE);

				getSessionService().setAttribute(MarketplacecommerceservicesConstants.DUPLICATEJUSPAYRESONSE,
						duplicatJuspayResponseMap);

				//Cart guid added to propagate to further methods via jsp
				model.addAttribute(MarketplacecheckoutaddonConstants.GUID, cartData.getGuid());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTTOORDERCONVERT, Boolean.FALSE); //INC144315475

				//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
				//UF-260
				GenericUtilityMethods.getCartPriceDetails(model, cartModel, null);

			}
			//TPR-629 --- based on orderModel
			else
			{
				orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				// TPR-429 START
				//final String checkoutSellerID = populateCheckoutSellersForOrder(orderData);
				//model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, checkoutSellerID);
				// TPR-429 END
				//Getting Payment modes
				paymentModeMap = getMplPaymentFacade().getPaymentModes(MarketplacecheckoutaddonConstants.MPLSTORE, orderData);


				//TISSQAUAT-536 fixes

				model.addAttribute(MarketplacecheckoutaddonConstants.GUID, orderModel.getGuid());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTTOORDERCONVERT, Boolean.TRUE); //INC144315475

				//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
				//UF-260
				GenericUtilityMethods.getCartPriceDetails(model, orderModel, null);
			}

			//creating new Payment Form
			final PaymentForm paymentForm = new PaymentForm();
			setupAddPaymentPage(model);

			if (MapUtils.isNotEmpty(paymentModeMap)) // Code optimization for performance fix TISPT-169
			{
				//Adding payment modes in model to be accessed from jsp
				model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODES, paymentModeMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.TRANERRORMSG, "");
				timeOutSet(model);

				//setting silent orders
				setupSilentOrderPostPage(paymentForm, model, orderData, cartData);
			}

			final String payNowPromotionCheck = getSessionService()
					.getAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);
			if (StringUtils.isNotEmpty(payNowPromotionCheck)
					&& payNowPromotionCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);
				GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.PROMOTIONEXPIRED);
			}

		}
		catch (final BindingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			//GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,MarketplacecheckoutaddonConstants.ERRORMSG);

			final String requestQueryParam = UriUtils.encodeQuery(
					"?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again." + "&type=error",
					UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}

		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.E0007, e);
			//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			//					MarketplacecheckoutaddonConstants.ERRORMSG);

			final String requestQueryParam = UriUtils.encodeQuery(
					"?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again." + "&type=error",
					UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}

		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error(MarketplacecommerceservicesConstants.B6001, e);
			//frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			//					MarketplacecheckoutaddonConstants.ERRORMSG);

			final String requestQueryParam = UriUtils.encodeQuery(
					"?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again." + "&type=error",
					UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MarketplacecommerceservicesConstants.B6001, e);
			//frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			//					MarketplacecheckoutaddonConstants.ERRORMSG);

			final String requestQueryParam = UriUtils.encodeQuery(
					"?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again." + "&type=error",
					UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}

		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			//					MarketplacecheckoutaddonConstants.ERRORMSG);

			final String requestQueryParam = UriUtils.encodeQuery(
					"?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again." + "&type=error",
					UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.SingleStepCheckout.AddPaymentMethodPage;
	}

	@RequestMapping(value = "/pickupPerson/popup", method = RequestMethod.GET)
	public String getPickUpPersonPopUp() throws UnsupportedEncodingException
	{
		if (getUserFacade().isAnonymousUser())
		{
			final String requestQueryParam = UriUtils
					.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.Single.PickupPersonForm;
	}


	/**
	 * @author TECH This method populates List of Ats and ussid to the data object.
	 * @param response
	 * @return list of pos with product.
	 */
	private List<ProudctWithPointOfServicesData> getProductWdPos(final List<StoreLocationResponseData> response, final Model model,
			final Map<String, Long> freebieParentQtyMap)
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
						//						if (i == 3)
						//						{
						//							break;
						//						}
						final PointOfServiceModel posModel = mplSlaveMasterFacade.findPOSBySellerAndSlave(sellerInfoModel.getSellerID(),
								storeLocationRequestData.getStoreId().get(i));
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
			LOG.debug("sellerInfoModel PK=>" + sellerInfoModel.getPk());
			productModel = sellerInfoModel.getProductSource();
			LOG.debug("Product model in papulateClicknCollectRequesrData=>" + productModel);
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
				final String globalCodeShippingMode = MplGlobalCodeConstants.GLOBALCONSTANTSMAP
						.get(richAttributeModel.get(0).getShippingModes().getCode().toUpperCase());
				storeLocationRequestData.setTransportMode(globalCodeShippingMode);
			}
			else
			{
				LOG.debug(
						"storeLocationRequestData :  ShippingMode type not received for the " + sellerInfoModel.getSellerArticleSKU());
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

	/**
	 * @param model
	 */
	private void timeOutSet(final Model model)
	{
		final double deliveryMode = Double
				.parseDouble(configurationService.getConfiguration().getString("checkout.deliverymode.timeout"));
		final String timeOut = Double.toString(deliveryMode * 60 * 1000);
		model.addAttribute(MarketplacecheckoutaddonConstants.TIMEOUT, timeOut);
	}

	@SuppressWarnings("deprecation")
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

	//	private void setExpressCheckout(final CartModel serviceCart)
	//	{
	//		if (serviceCart.getDeliveryAddress() != null && serviceCart.getIsExpressCheckoutSelected().booleanValue())
	//		{
	//			serviceCart.setIsExpressCheckoutSelected(Boolean.valueOf(false));
	//			serviceCart.setDeliveryAddress(null);
	//			modelService.save(serviceCart);
	//		}
	//	}

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

						//UF-281 Starts
						String status = MarketplacecheckoutaddonConstants.SAVE_STORE_TOPORUDCT_SUCCESS_MSG;
						//get store information from commerce for posName
						final PointOfServiceModel posModel = mplSlaveMasterFacade.findPOSByName(deliveryEntry.getSelectedStore());
						status = (null != posModel)
								? mplStoreLocatorFacade.saveStoreForSelectedProduct(posModel, deliveryEntry.getSellerArticleSKU())
								: MarketplacecheckoutaddonConstants.SAVE_STORE_TOPORUDCT_FAIL_MSG;
						if (status.equals(MarketplacecheckoutaddonConstants.SAVE_STORE_TOPORUDCT_FAIL_MSG))
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B1002);
						}
						//UF-281 Ends
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

	protected void prepareDataForPage(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("isOmsEnabled", Boolean.valueOf(getSiteConfigService().getBoolean("oms.enabled", false)));
		model.addAttribute("supportedCountries", getCartFacade().getDeliveryCountries());
		model.addAttribute("expressCheckoutAllowed", Boolean.valueOf(getCheckoutFacade().isExpressCheckoutAllowedForCart()));
		model.addAttribute("taxEstimationEnabled", Boolean.valueOf(getCheckoutFacade().isTaxEstimationEnabledForCart()));
	}


	// REVIEW ORDER PAGE

	/**
	 * Cart Item Removal
	 *
	 * @throws UnsupportedEncodingException
	 *
	 *
	 */
	@RequestMapping(value = "/removereviewcart", method = RequestMethod.GET)
	public String removeFromMinicart(final Model model, final HttpServletRequest request)
			throws CommerceCartModificationException, UnsupportedEncodingException
	{
		CartModel cartModel = null;
		final String entryNumberString = request.getParameter("entryNumber");
		final String returnPage = REDIRECT_PREFIX + MarketplacecheckoutaddonConstants.MPLSINGLEPAGEURL
				+ MarketplacecheckoutaddonConstants.GETREVIEWORDER;
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils
						.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final long entryNumber = Long.parseLong(entryNumberString);

			if (mplCartFacade.hasEntries())
			{
				//Fetch delivery point of service before recalculation of cart
				//	CartModel cartModel = null;
				//				final Map deliveryPOSMap = new HashMap();
				cartModel = cartService.getSessionCart();
				for (final AbstractOrderEntryModel entry : cartModel.getEntries())
				{
					//					if (entry.getDeliveryPointOfService() != null)
					//					{
					//						deliveryPOSMap.put(entry.getSelectedUSSID(), entry.getDeliveryPointOfService());
					//					}
					//Removing slot delivery set in previous step
					if (entry.getEntryNumber().longValue() == entryNumber)
					{
						//Start of UF-281
						boolean isSaveRequired = false;
						if (null != entry.getEdScheduledDate() && StringUtils.isNotEmpty(entry.getEdScheduledDate()))
						{
							entry.setEdScheduledDate("".trim());
							entry.setTimeSlotFrom("".trim());
							entry.setTimeSlotTo("".trim());
						}
						if (entry.getScheduledDeliveryCharge() != null && entry.getScheduledDeliveryCharge().doubleValue() != 0.0)
						{
							isSaveRequired = true;
							//							if (cartModel.getTotalPriceWithConv() != null)
							//							{
							//								cartModel.setTotalPriceWithConv(new Double(cartModel.getTotalPriceWithConv().doubleValue()
							//										- Double.valueOf(entry.getScheduledDeliveryCharge().doubleValue()).doubleValue()));
							//							}
							//							final Double finalDeliveryCost = Double.valueOf(cartModel.getDeliveryCost().doubleValue()
							//									- entry.getScheduledDeliveryCharge().doubleValue());
							//							cartModel.setDeliveryCost(finalDeliveryCost);
							//							final Double totalPriceAfterDeliveryCost = Double.valueOf(cartModel.getTotalPrice().doubleValue()
							//									- entry.getScheduledDeliveryCharge().doubleValue());
							//							cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
							entry.setScheduledDeliveryCharge(Double.valueOf(0));
						}
						if (isSaveRequired)
						{
							modelService.save(cartModel);
							modelService.refresh(cartModel);
						}
						//End of UF-281
					}
				}
				//TPR-1083 Start
				if (cartModel.getExchangeAppliedCart() != null && cartModel.getExchangeAppliedCart().booleanValue())
				{
					exchangeGuideFacade.removeExchangefromCart(cartModel);
				}
				//TPR-1083 End
				final CartModificationData cartModification = mplCartFacade.updateCartEntry(entryNumber, 0);
				//	cartModel = cartService.getSessionCart();
				//Fetch delivery address before recalculation of cart
				final AddressData addressData = mplCustomAddressFacade.getDeliveryAddress();

				mplCouponFacade.releaseVoucherInCheckout(cartModel);
				mplCartFacade.getCalculatedCart(cartModel);

				if (cartModification.getQuantity() == 0)
				{
					if (!mplCartFacade.hasEntries())
					{
						final String requestQueryParam = UriUtils
								.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART + "&type=redirect", UTF);
						return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
					}
					else
					{
						final HttpSession session = request.getSession();
						final DeliveryMethodForm deliveryMethodForm = (DeliveryMethodForm) session.getAttribute("deliveryMethodForm");
						Double finalDeliveryCost = Double.valueOf(0.0);
						//Re-populating delivery modes after recalculation of cart
						{
							finalDeliveryCost = populateMplZoneDeliveryMode(deliveryMethodForm, cartModel);
							final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
							getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost, deliveryChargePromotionMap, cartModel); //TIS 400
							//session.removeAttribute("deliveryMethodForm");
						}
						final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
						final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

						applyPromotions();

						//populate freebie data
						populateFreebieProductData(cartModel, freebieModelMap, freebieParentQtyMap);
						//Re-populating delivery address after recalculation of cart
						getMplCustomAddressFacade().setDeliveryAddress(addressData);
						//Re-populating delivery point of service after recalculation of cart
						//getMplCheckoutFacade().rePopulateDeliveryPointOfService(deliveryPOSMap, cartModel);

						final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
						final List<PinCodeResponseData> responseData = getSessionService()
								.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
						Map<String, MarketplaceDeliveryModeData> deliveryModeDataMap = new HashMap<String, MarketplaceDeliveryModeData>();
						deliveryModeDataMap = mplCartFacade.getDeliveryModeMapForReviewOrder(cartData, responseData);

						model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
						model.addAttribute("deliveryModeData", deliveryModeDataMap);
						final String defaultPincode = getSessionService()
								.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
						model.addAttribute("defaultPincode", defaultPincode);
					}
				}
				else
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg=" + "Could not remove item from cart" + "&type=error",
							UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
			}
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.error("CommerceCartModificationException while remove item from minicart ", ex);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		catch (final EtailBusinessExceptions ex)
		{
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			LOG.error("EtailNonBusinessExceptions while remove item from minicart ", ex);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions while remove item from minicart ", ex);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		catch (final Exception ex)
		{
			LOG.error("Exception while remove item from minicart ", ex);
			final String requestQueryParam = UriUtils.encodeQuery("?msg=Opps...Something went wrong&type=error", UTF);
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;

		}
		return returnPage;
	}

	/*
	 * @Description adding wishlist popup in cart page
	 *
	 * @param String productCode,String wishName, model
	 */

	@ResponseBody
	@RequestMapping(value = "/addToWishListFromCart", method = RequestMethod.GET)
	public boolean addWishListsForCartPage(@RequestParam("product") final String productCode,
			@RequestParam("ussid") final String ussid, @RequestParam("wish") final String wishName, final Model model,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletRequest request,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletResponse response)
			throws CMSItemNotFoundException
	{


		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
		boolean add = false;
		try
		{
			final Wishlist2Model existingWishlist = wishlistFacade.getWishlistForName(wishName);
			if (null != existingWishlist)
			{
				wishlistFacade.addProductToWishlist(existingWishlist, productCode, ussid, true);
			}
			for (final Wishlist2EntryModel wlEntry : existingWishlist.getEntries())
			{
				final String product = wlEntry.getProduct().getCode();
				if (productCode.equals(product))
				{
					add = true;
					removeEntryByProductCode(productCode);
				}
			}
		}
		catch (final CMSItemNotFoundException cmsex)
		{
			LOG.error("CMSItemNotFoundException while adding to wishlist from cart ", cmsex);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("Exception while adding to wishlist from cart ", e);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("Exception while adding to wishlist from cart ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while adding to wishlist from cart ", ex);
		}

		return add;
	}

	/*
	 * @Description showing wishlist popup in cart page
	 *
	 * @param String productCode, model
	 */
	@ResponseBody
	@RequestMapping(value = "/wishlists", method = RequestMethod.GET)
	public List<WishlistData> showWishListsForCartPage(@RequestParam("productCode") final String productCode,
			@RequestParam("ussid") final String ussid, final Model model,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletRequest request,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletResponse response)
			throws CMSItemNotFoundException

	{

		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
		final UserModel user = userService.getCurrentUser();
		List<WishlistData> wishListData = null;

		//If the user is not logged in then ask customer to login.
		if (null != user.getName() && user.getName().equalsIgnoreCase(USER.ANONYMOUS_CUSTOMER))
		{
			wishListData = new ArrayList<WishlistData>();
			try

			{
				final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
				final int wishListSize = allWishlists.size();
				final String nameSet = ModelAttributetConstants.WISHLIST_NO + ModelAttributetConstants.UNDER_SCORE + "1";


				//check whether any wishlist exits for user or not or else create a new wishlist and add product to it
				if (wishListSize == 0)
				{
					//add product to new wishlist
					final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, nameSet, productCode);

					wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, true);
					final WishlistData wishData = new WishlistData();
					wishData.setParticularWishlistName(createdWishlist.getName());
					wishData.setProductCode(productCode);
					wishListData.add(wishData);
				}
				else
				{

					//view existing wishlists
					for (final Wishlist2Model wish : allWishlists)
					{
						final WishlistData wishList = new WishlistData();
						wishList.setParticularWishlistName(wish.getName());
						for (final Wishlist2EntryModel wlEntry : wish.getEntries())
						{
							if (wlEntry.getProduct().getCode().equals(productCode))
							{
								wishList.setProductCode(productCode);
							}
						}
						wishListData.add(wishList);
					}

				}
			}

			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
				LOG.error("Exception occured while showWishListsForCartPage ", e);

			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				LOG.error("Exception occured while showWishListsForCartPage ", e);
			}
			catch (final Exception ex)
			{
				LOG.error("Exception occured while showWishListsForCartPage ", ex);
			}
		}
		return wishListData;

	}


	/**
	 * @Description remove cart entry by product code
	 * @param ProductCode
	 */
	private void removeEntryByProductCode(final String ProductCode)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions, Exception

	{

		final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);


		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				if (entry.getProduct().getCode().equalsIgnoreCase(ProductCode))
				{
					try
					{
						@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
						final CartModificationData cartModification = mplCartFacade.updateCartEntry(entry.getEntryNumber(), 0);
					}
					catch (final CommerceCartModificationException e)
					{
						LOG.error("Class NameremoveEntryByProductCo :" + "Error on cart delete" + e);
					}
				}
			}
		}

	}

	/**
	 * This method is used to set up the Payment page
	 *
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @throws Exception
	 *
	 */
	private void setupAddPaymentPage(final Model model) throws CMSItemNotFoundException, Exception
	{
		model.addAttribute(MarketplacecheckoutaddonConstants.METAROBOTS, MarketplacecheckoutaddonConstants.NOINDEX_NOFOLLOW);

		//model.addAttribute(MarketplacecheckoutaddonConstants.HASNOPAYMENTINFO,
		//		Boolean.valueOf(getMplCustomAddressFacade().hasNoPaymentInfo()));

		prepareDataForPage(model);
		//		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
		//				getResourceBreadcrumbBuilder().getBreadcrumbs(MarketplacecheckoutaddonConstants.PAYMENTBREADCRUMB));
		final ContentPageModel contentPage = getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL);
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		//setCheckoutStepLinksForModel(model, getCheckoutStep());
		model.addAttribute("paymentPage", "paymentPage");
	}

	/**
	 * This method is used to set up the form and rendering it with the necessary values
	 *
	 * @param paymentForm
	 * @param model
	 */
	private void setupSilentOrderPostPage(final PaymentForm paymentForm, final Model model, final OrderData orderData,
			final CartData cartData)
	{
		try
		{
			//TISSTRT-1390
			//Commented as not used TPR-629
			//final PaymentData silentOrderPageData = getPaymentFacade().beginSopCreateSubscription(
			//		MarketplacecheckoutaddonConstants.CHECKOUTRESPONSEURL, MarketplacecheckoutaddonConstants.CHECKOUTCALLBACKURL);
			//model.addAttribute(MarketplacecheckoutaddonConstants.SOPPAGEDATA, silentOrderPageData);
			//paymentForm.setParameters(silentOrderPageData.getParameters());
			model.addAttribute(MarketplacecheckoutaddonConstants.NEWPAYMENTFORMMPLURL,
					MarketplacecheckoutaddonConstants.NEWPAYMENTVIEWURL);

			setupMplPaymentPage(model, orderData, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTFORM, paymentForm);

		}
		catch (final IllegalArgumentException e)
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.NEWPAYMENTFORMMPLURL, "");
			model.addAttribute(MarketplacecheckoutaddonConstants.SOPPAGEDATA, null);
			LOG.error(MarketplacecheckoutaddonConstants.LOGWARN, e);
			GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.GLOBALERROR);
		}

		catch (final Exception ex)
		{
			LOG.error(" Exception in setupSilentOrderPostPage", ex);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	private void setupMplPaymentPage(final Model model, final OrderData orderData, final CartData cartData) throws Exception
	{
		if (null == orderData)
		{
			//Existing code for cart
			//getting cartdata
			//final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if (null != cartData && cartData.getAppliedOrderPromotions() != null)
			{
				final double totalDiscount = getMplPaymentFacade().calculateTotalDiscount(cartData.getAppliedOrderPromotions());
				//				for (final PromotionResultData promotionResultData : cartData.getAppliedOrderPromotions())
				//				{
				//					final String st = promotionResultData.getDescription();
				//					final String result = stripNonDigits(st);
				//
				//					try
				//					{
				//						totalDiscount = totalDiscount + Double.parseDouble(result);
				//					}
				//					catch (final Exception e)
				//					{
				//						LOG.error("Exception during double parsing ", e);
				//						totalDiscount = totalDiscount + 0;
				//					}
				//				}
				final String promotionMssg = RECEIVED_INR + totalDiscount + DISCOUNT_MSSG;
				model.addAttribute("promotionMssgDeliveryMode", promotionMssg);
			}


			//For Voucher when reloaded
			//		final List<VoucherData> voucherDataList = voucherFacade.getVouchersForCart();
			//
			//		for (final VoucherData voucher : voucherDataList)
			//		{
			//			try
			//			{
			//				//voucherFacade.releaseVoucher(voucher.getVoucherCode());
			//				mplCouponFacade.releaseVoucher(voucher.getVoucherCode(), getCartService().getSessionCart());
			//			}
			//			catch (final VoucherOperationException e)
			//			{
			//				LOG.error("Voucher with voucher code " + voucher.getVoucherCode() + " could not be released");
			//				e.printStackTrace();
			//			}
			//		}


			//getting cart subtotal value
			//final Double cartValue = Double.valueOf(cartData.getSubTotal().getValue().doubleValue());
			//getting totalprice of cart
			final Double cartTotal = new Double(cartData.getTotalPrice().getValue().doubleValue());

			//setupMplNetbankingForm(model);		//TISPT-235 Commented to make ajax call for netbanking
			//setupMplCODForm(model, cartValue, cartData);
			setupMplCardForm(model, cartTotal);

			//Adding all the details in model to be accessed from jsp
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			//TODO: Top 5 coupons-----Commented as functionality out of scope of R2.1   Uncomment when in scope
			//model.addAttribute("voucherDataList",
			//		displayTopCoupons(getCartService().getSessionCart(), (CustomerModel) getUserService().getCurrentUser()));

			//saving cartmodel
			getMplPaymentFacade().saveCart(getCartService().getSessionCart());
			model.addAttribute("isCart", Boolean.TRUE);
		}
		else
		{
			//Code added for order TPR-629
			if (orderData.getAppliedOrderPromotions() != null)
			{
				final double totalDiscount = getMplPaymentFacade().calculateTotalDiscount(orderData.getAppliedOrderPromotions());
				final String promotionMssg = RECEIVED_INR + totalDiscount + DISCOUNT_MSSG;
				model.addAttribute("promotionMssgDeliveryMode", promotionMssg);
			}

			final Double cartTotal = new Double(orderData.getTotalPrice().getValue().doubleValue());

			setupMplCardForm(model, cartTotal);

			//Adding all the details in model to be accessed from jsp
			model.addAttribute(MarketplacecheckoutaddonConstants.ORDERDATA, orderData);
			model.addAttribute("isCart", Boolean.FALSE);
		}

		//Added for mRupee

		//getting merchant for mRupee
		model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_MERCHANT_URL,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.MRUPEEURL));

		//getting redirect url mRupee
		model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_CODE,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.MRUPEE_MERCHANT_CODE));

		model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_NARRATION,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.MRUPEE_NARRATION_VALUE));

		//mRupee configuration ends



		//		for (final OrderEntryData cartEntryData : cartData.getEntries())
		//		{
		//			final CartModel cartModel = getCartService().getSessionCart();
		//			final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
		//			for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
		//			{
		if (null != cartData)
		{
			if (null != cartData.getEntries())
			{
				//TISSTRT-1501 ends
				for (final OrderEntryData cartEntryData : cartData.getEntries())
				{
					final CartModel cartModel = getCartService().getSessionCart();
					final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
					for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
					{
						if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
						{
							if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(cartEntryData.getSelectedUssid()))
							{
								cartEntryData.setEddDateBetWeen(cartEntryModel.getSddDateBetween());
							}
						}
					}

					if (null != cartEntryData && cartEntryData.getScheduledDeliveryCharge() != null)
					{
						if (cartEntryData.getScheduledDeliveryCharge().doubleValue() > 0)
						{
							// final CartModel cartModel = getCartService().getSessionCart();
							final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
							cartData.setDeliverySlotCharge(
									mplCheckoutFacade.createPrice(cartModel, Double.valueOf(configModel.getSdCharge())));
						}
					}
				}
				//TISSTRT-1501 starts
			}

			//			if (null != cartEntryData && cartEntryData.getScheduledDeliveryCharge() != null)
			//			{
			//				if (cartEntryData.getScheduledDeliveryCharge().doubleValue() > 0)
			//				{
			//					// final CartModel cartModel = getCartService().getSessionCart();
			//					final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
			//					cartData
			//							.setDeliverySlotCharge(mplCheckoutFacade.createPrice(cartModel, Double.valueOf(configModel.getSdCharge())));
			//				}
			//			}

		}
		//			}
		//TISSTRT-1501 ends
		model.addAttribute(MarketplacecheckoutaddonConstants.JUSPAYJSNAME,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYJSNAMEVALUE));
		model.addAttribute(MarketplacecheckoutaddonConstants.SOPFORM, new PaymentDetailsForm());
		//Terms n Conditions Link
		model.addAttribute(MarketplacecheckoutaddonConstants.TNCLINK,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.TNCLINKVALUE));

	}

	/**
	 * @param model
	 * @param cartTotal
	 */
	private void setupMplCardForm(final Model model, final Double cartTotal)
	{
		//getting merchant ID ofJuspay
		model.addAttribute(MarketplacecheckoutaddonConstants.MERCHANT_ID,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.MERCHANTID));

		//getting redirect value(redirect or pop-up)
		model.addAttribute(MarketplacecheckoutaddonConstants.JUSPAYREDIRECT,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYREDIRECTKEY));

		//TISCR-421 : getting accountId to be sent to EBS
		model.addAttribute(MarketplacecheckoutaddonConstants.EBS_ACCOUNT_ID,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.EBS_ACCOUNT_ID_KEY));

		//Juspay-EBS session id
		final String sessionId = getMd5Encoding(getRandomAlphaNum(
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.EBS_SESSION_ID_KEY)));
		getSessionService().setAttribute(MarketplacecheckoutaddonConstants.EBS_SESSION_ID, sessionId);
		model.addAttribute(MarketplacecheckoutaddonConstants.EBS_SESSION_ID, sessionId);

		//getting the current customer to fetch customer Id and customer email
		final CustomerModel customer = (CustomerModel) userService.getCurrentUser();

		//getting the list of stored cards
		Map<Date, SavedCardData> savedCreditCards = new TreeMap<Date, SavedCardData>();
		Map<Date, SavedCardData> savedDebitCards = new TreeMap<Date, SavedCardData>();
		final Collection<SavedCardModel> savedCardForCustomer = customer.getSavedCard(); //TISPT-204 Point no 5

		// Code addition start TISPT-204 Point no 4
		ListCardsResponse listCardsResponse = null;
		try
		{
			if (CollectionUtils.isNotEmpty(savedCardForCustomer)) // Code added for TISPT-204 Point no  5
			{
				listCardsResponse = getMplPaymentFacade().getJuspayCardResponse(customer);
				LOG.debug("*********************************************************************");
				LOG.debug("The Juspay Responce for Saved Card : " + listCardsResponse);
				final Tuple2<?, ?> storedSavedCards = getMplPaymentFacade().listStoredCards(customer, listCardsResponse);
				LOG.debug("*********************************************************************");

				LOG.debug("Stored Card" + storedSavedCards);

				savedCreditCards = (Map<Date, SavedCardData>) storedSavedCards.getFirst();
				savedDebitCards = (Map<Date, SavedCardData>) storedSavedCards.getSecond();

				LOG.debug("Credit Card" + savedCreditCards);
				LOG.debug("Debit Card" + savedDebitCards);
				LOG.debug("*********************************************************************");
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6005, e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6005, e);
		}
		// Code addition end TISPT-204 Point no 4
		try
		{
			//savedCreditCards = getMplPaymentFacade().listStoredCreditCards(customer); Code commented for TISPT-204 Point no  4
			if (MapUtils.isNotEmpty(savedCreditCards))
			{
				//adding cards to model
				model.addAttribute(MarketplacecheckoutaddonConstants.CREDITCARDS, savedCreditCards);
			}
			else
			{
				LOG.info("No Saved credit cards found !!");
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6005, e);

			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6005, e);
		}

		try
		{
			//savedDebitCards = getMplPaymentFacade().listStoredDebitCards(customer); //Code commented for TISPT-204 Point no  4
			if (!savedDebitCards.isEmpty())
			{
				//adding cards to model
				model.addAttribute(MarketplacecheckoutaddonConstants.DEBITCARDS, savedDebitCards);
			}
			else
			{
				LOG.info("No Saved debit cards found !!");

			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6006, e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MarketplacecheckoutaddonConstants.B6006, e);

		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6006, e);
		}
		try
		{
			final List<String> countryList = getMplPaymentFacade().getCountries();
			if (CollectionUtils.isNotEmpty(countryList))
			{
				model.addAttribute(MarketplacecheckoutaddonConstants.COUNTRY, countryList);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6007, e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6007, e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6007, e);
		}


		final String ebsDowntime = configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.EBSDOWNTIME);

		if (StringUtils.isNotEmpty(ebsDowntime))
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.EBSDOWNCHECK, ebsDowntime);
		}
		else
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.EBSDOWNCHECK, MarketplacecheckoutaddonConstants.NA);
		}

		final String noOfExpYear = configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.NOOFYEARS,
				"25");
		model.addAttribute(MarketplacecheckoutaddonConstants.EXPYEARS, noOfExpYear);

		setupMplMessages(model);
	}

	/**
	 * To add CVV text help messages
	 *
	 * @param model
	 */
	private void setupMplMessages(final Model model)
	{
		if (null != configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.CVV_HELP))
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.CVV_HELP_VAR,
					configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.CVV_HELP));
		}
		else
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.CVV_HELP_VAR, "");
		}
	}

	/**
	 * This method returns random alpha numeric number
	 *
	 * @param len
	 * @return String
	 */
	private String getRandomAlphaNum(final String len)
	{
		return RandomStringUtils.randomAlphanumeric(Integer.parseInt(len)).toUpperCase();
	}

	/**
	 * This method handles MD5 encoding
	 *
	 * @param input
	 * @return String
	 */
	private String getMd5Encoding(final String input)
	{
		MessageDigest messageDigest;
		String result = null;
		try
		{
			messageDigest = MessageDigest.getInstance(
					configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAY_ENCODING_TYPE));
			messageDigest.reset();
			messageDigest.update(input.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			result = new String(Hex.encodeHex(resultByte));
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOG.error("Error while encoding=======");
		}

		return result;
	}

	/**
	 * @return the mplPaymentFacade
	 */
	public MplPaymentFacade getMplPaymentFacade()
	{
		return mplPaymentFacade;
	}

	/**
	 * @param mplPaymentFacade
	 *           the mplPaymentFacade to set
	 */
	public void setMplPaymentFacade(final MplPaymentFacade mplPaymentFacade)
	{
		this.mplPaymentFacade = mplPaymentFacade;
	}

	/**
	 * @Description Method to remove exchange from cart
	 * @return JSONObject
	 * @throws JSONException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.REMOVEEXCHANGEFROMCART, method = RequestMethod.GET)
	public @ResponseBody JSONObject removeAllExchangeFromCart() throws JSONException
	{
		final JSONObject jsonObj = new JSONObject();
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside selectAddress Responsive Method...");
			}
			if (getUserFacade().isAnonymousUser())
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}


			//TISST-13012
			final CartModel cart = getCartService().getSessionCart();
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cart);
			if (!cartItemDelistedStatus)
			{
				final Boolean exchangeCart = cart.getExchangeAppliedCart();
				if (null != exchangeCart && exchangeCart.booleanValue())
				{
					exchangeGuideFacade.removeExchangefromCart(cart);
					jsonObj.put("exchangeItemsRemoved", "true");

				}

			}


		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions Removing Exchange from Cart ", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  Removing Exchange from Cart ", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while Removing Exchange from Cart", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		return jsonObj;
	}

	/**
	 * @description method is called to update fields for tealium when remove is done from Review page of one page
	 *              checkout
	 * @return Json Object
	 * @throws JSONException
	 */
	@RequestMapping(value = "/updateTealiumData", method = RequestMethod.GET)
	public @ResponseBody JSONObject updateTealiumData(final Model model) throws UnsupportedEncodingException, JSONException
	{

		JSONObject jsonObj = new JSONObject();
		try
		{
			final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);

			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
			jsonObj = populateTealiumDataForCartCheckoutJSON(model);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}

		return jsonObj;

	}

	/**
	 * @Description create a json object for ajax response
	 * @param model
	 * @return JSONObject
	 */
	private JSONObject populateTealiumDataForCartCheckoutJSON(final Model model)
	{
		final JSONObject json = new JSONObject();
		try
		{

			json.put("productBrandList", model.asMap().get("productBrandList"));
			json.put("productIdList", model.asMap().get("productIdList"));
			json.put("productListPriceList", model.asMap().get("productListPriceList"));
			json.put("productNameList", model.asMap().get("productNameList"));
			json.put("productQuantityList", model.asMap().get("productQuantityList"));
			json.put("productSkuList", model.asMap().get("productSkuList"));
			json.put("productUnitPriceList", model.asMap().get("productUnitPriceList"));
			json.put("adobe_product", model.asMap().get("adobe_product"));
			json.put("cart_total", model.asMap().get("cart_total"));
			json.put("orderShippingCharges", model.asMap().get("orderShippingCharges"));
			json.put("pageSubCategories", model.asMap().get("pageSubCategories"));
			json.put("productCategoryList", model.asMap().get("productCategoryList"));
			json.put("page_subcategory_name_L3", model.asMap().get("page_subcategory_name_L3"));
			json.put("page_subcategory_name_L4", model.asMap().get("page_subcategory_name_L4"));
			json.put("checkoutSellerIDs", model.asMap().get("cartLevelSellerID"));
		}
		catch (final Exception te)
		{
			LOG.error("Error while populating tealium data in cart page:::::" + te.getMessage());
		}
		return json;
	}


}
