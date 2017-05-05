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
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.Constants.USER;
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

import reactor.function.support.UriUtils;

import com.granule.json.JSONException;
import com.granule.json.JSONObject;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.tisl.mpl.checkout.form.DeliveryMethodEntry;
import com.tisl.mpl.checkout.form.DeliveryMethodForm;
import com.tisl.mpl.checkout.steps.validation.impl.ResponsivePaymentCheckoutStepValidator;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
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
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplSlaveMasterFacade;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
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


/**
 * SingleStepCheckoutController
 */
@Controller
@RequestMapping(value = "/checkout/single")
public class MplSingleStepCheckoutController extends AbstractCheckoutController
{
	@SuppressWarnings("unused")
	protected static final Logger LOG = Logger.getLogger(MplSingleStepCheckoutController.class);

	private static final String SINGLE_STEP_CHECKOUT_SUMMARY_CMS_PAGE = "singleStepCheckoutSummaryPage";
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

	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	ProductService productService;

	@Autowired
	private WishlistFacade wishlistFacade;

	@Resource
	private MplCheckoutFacade mplCheckoutFacade;

	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "mplSlaveMasterFacade")
	private MplSlaveMasterFacade mplSlaveMasterFacade;

	@Autowired
	private MplCouponFacade mplCouponFacade;

	@Autowired
	private MplDeliveryCostService mplDeliveryCostService;

	private final String checkoutPageName = "Choose Your Delivery Options";

	@Autowired
	private CartService cartService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Autowired
	private MplSellerInformationFacade mplSellerInformationFacade;

	@Resource(name = "pointOfServiceConverter")
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	@Autowired
	private MplAddressValidator mplAddressValidator;

	@Autowired
	private MplConfigFacade mplConfigFacade;

	@Autowired
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

	// Landing page for Single Page Journey

	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String checkoutPage(final Model model, @RequestParam(required = false, defaultValue = "false") final boolean isAjax)
	{
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			final CartModel cartModel = getCartService().getSessionCart();
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));

			//CAR-130
			/* final CartData cartData = getMplCustomAddressFacade().getCheckoutCart(); */
			final CartData cartUssidData = getMplCustomAddressFacade().getCheckoutCart();
			List<AddressData> deliveryAddress = null;

			//TIS 391:  In case of normal checkout , delivery address will be null and for express checkout delivery address will be ore selected from the cart page
			// In case of express checkout it will redirect to the payment page from delivery mode selection

			if (cartUssidData != null)
			{
				if (cartUssidData.getDeliveryAddress() != null)
				{
					LOG.debug("Express checkout selected for address id : " + cartUssidData.getDeliveryAddress().getId());
					//return getCheckoutStep().nextStep();
				}


				deliveryAddress = getMplCustomAddressFacade().getDeliveryAddresses(cartUssidData.getDeliveryAddress(), cartModel); //CAR-194

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

			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);

			model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartUssidData);
			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(MarketplacecheckoutaddonConstants.METAROBOTS, MarketplacecheckoutaddonConstants.NOINDEX_NOFOLLOW);
			timeOutSet(model);

			//Tealium related data population
			final String cartLevelSellerID = populateCheckoutSellers(cartUssidData);
			model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, cartLevelSellerID);
			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartUssidData);
			model.addAttribute("checkoutPageName", selectAddress);

			//Code to import payment on checkout page load
			model.addAttribute("openTab", "deliveryAddresses");
			model.addAttribute("prePopulateTab", "payment");
			prepareModelForPayment(model, cartUssidData);
		}
		catch (final CMSItemNotFoundException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final Exception e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isAjax)
		{
			final String selectedAddress = getSessionService().getAttribute("selectedAddress");
			model.addAttribute("selectedAddress", selectedAddress);
			return "addon:/marketplacecheckoutaddon/pages/checkout/single/showDeliveryAddressDetailsPage";
		}
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/singlePageCheckout";
	}

	// Address Section

	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS
			+ MarketplacecheckoutaddonConstants.ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	public String editAddress(@PathVariable(ModelAttributetConstants.ADDRESS_CODE) final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{

		List<StateData> stateDataList = new ArrayList<StateData>();
		final AccountAddressForm addressForm = new AccountAddressForm();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
			model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
			model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
			//timeOutSet(model);
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
			//			model.addAttribute(
			//					WebConstants.BREADCRUMBS_KEY,
			//					getResourceBreadcrumbBuilder().getBreadcrumbs(
			//							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			//		setCheckoutStepLinksForModel(model, getCheckoutStep());

		}
		catch (final Exception e)
		{
			//		this.prepareDataForPage(model);
			model.addAttribute("addressForm", addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			GlobalMessages.addErrorMessage(model, "address.error.formentry.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			//			model.addAttribute(
			//					WebConstants.BREADCRUMBS_KEY,
			//					getResourceBreadcrumbBuilder().getBreadcrumbs(
			//							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			//			setCheckoutStepLinksForModel(model, getCheckoutStep());
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/showEditAddressDetails";
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
	public String edit(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//TISST-13012
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final String errorMsg = mplAddressValidator.validate(addressForm);
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{
				//TODO
				final String requestQueryParam = UriUtils.encodeQuery("?msg="
						+ MarketplacecheckoutaddonConstants.VALIDATION_FAILURE_MESSAGE + "&type=error", "UTF-8");
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

			final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			if (StringUtils.isEmpty(sessionPincode))
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//TISUTO-12 , TISUTO-11 ,TISUTO 106
			final String redirectURL = mplCartFacade.singlePagecheckPincode(addressForm.getPostcode(), sessionPincode);
			if (redirectURL.trim().length() > 0)
			{
				final String requestQueryParam = UriUtils.encodeQuery("?msg="
						+ MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}


			newAddress.setDefaultAddress(getUserFacade().isAddressBookEmpty() || getUserFacade().getAddressBook().size() == 1
					|| Boolean.TRUE.equals(addressForm.getDefaultAddress()));
			accountAddressFacade.editAddress(newAddress);

			//getCheckoutFacade().setDeliveryAddress(newAddress);
			getMplCustomAddressFacade().setDeliveryAddress(newAddress);

			getSessionService().setAttribute("selectedAddress", newAddress.getId());
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
		return REDIRECT_PREFIX + "/checkout/single/choose";

	}

	/**
	 * @param pincodeServicable
	 * @param displayMessage
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLSHOWMESSAGE, method =
	{ RequestMethod.POST, RequestMethod.GET })
	private @ResponseBody JSONObject displayMessage(@RequestParam(required = false) final String msg,
			@RequestParam(required = true) final String type, @RequestParam(required = false) final String url,
			@RequestParam(required = false) final String redirectString)
	{
		// YTODO Auto-generated method stub
		final JSONObject jsonObj = new JSONObject();

		try
		{
			if (type.equals("error"))
			{
				jsonObj.put("displaymessage", msg);
				jsonObj.put("type", type);
			}
			if (type.equals("redirect"))
			{
				jsonObj.put("url", url);
				jsonObj.put("type", type);
			}
			if (type.equals("ajaxRedirect"))
			{
				jsonObj.put("redirectString", redirectString);
				jsonObj.put("type", type);
			}
		}
		catch (final JSONException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
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
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
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
			//timeOutSet(model);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			//			model.addAttribute(
			//					WebConstants.BREADCRUMBS_KEY,
			//					getResourceBreadcrumbBuilder().getBreadcrumbs(
			//							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			//	setCheckoutStepLinksForModel(model, getCheckoutStep());
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
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/showAddAddressDetails";
	}

	/**
	 * @description method is called to select address from list and forward to payment step
	 * @param selectedAddressCode
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTADDRESSURL, method = RequestMethod.GET)
	public String selectAddress(@RequestParam("selectedAddressCode") final String selectedAddressCode, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			//TISST-13012
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				// ** to be uncommented		return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
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
				final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
				if (StringUtils.isEmpty(sessionPincode))
				{
					final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART
							+ "&type=redirect", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				//getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
				//TISUTO-12 , TISUTO-11 singlePagecheckPincode
				//		final String redirectURL = mplCartFacade.checkPincodeAndInventory(selectedPincode);
				final String redirectURL = mplCartFacade.singlePagecheckPincode(selectedPincode, sessionPincode);
				if (redirectURL.trim().length() > 0)
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg="
							+ MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}

				getMplCustomAddressFacade().setDeliveryAddress(finaladdressData);

				// Recalculating Cart Model
				LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
				getMplCheckoutFacade().reCalculateCart(cartData);
			}
			else
			{
				final String requestQueryParam = UriUtils.encodeQuery("?msg=Provide valid pincode&type=error", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
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
		return FORWARD_PREFIX + "/checkout/single/choose";

		//	return getCheckoutStep().nextStep();
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
	public String add(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final CartModel oModel = getCartService().getSessionCart();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
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
				//				model.addAttribute(
				//						WebConstants.BREADCRUMBS_KEY,
				//						getResourceBreadcrumbBuilder().getBreadcrumbs(
				//								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				//				setCheckoutStepLinksForModel(model, getCheckoutStep());
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				//model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.TRUE);
				//timeOutSet(model);
				//return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseAddNewAddressPage;

				final String requestQueryParam = UriUtils.encodeQuery("?msg=" + errorMsg, "UTF-8");
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

				final String sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
				if (StringUtils.isEmpty(sessionPincode))
				{
					final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecommerceservicesConstants.CART
							+ "&type=redirect", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				//TISUTO-12 , TISUTO-11
				final String redirectURL = mplCartFacade.singlePagecheckPincode(addressForm.getPostcode(), sessionPincode);
				if (redirectURL.trim().length() > 0)
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg="
							+ MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				//Setting selected address id in session
				getSessionService().setAttribute("selectedAddress", newAddress.getId());
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
		return REDIRECT_PREFIX + "/checkout/single/choose";
	}

	// DELIVERY METHOD SECTION

	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHOOSEVALUE, method = RequestMethod.GET)
	public String enterDeliveryModeStep(final Model model, final RedirectAttributes redirectAttributes)
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<PinCodeResponseData> responseData = null;
		String returnPage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartModel serviceCart = getCartService().getSessionCart();
			setExpressCheckout(serviceCart);

			//TISST-13012
			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(serviceCart); //TISPT-104
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				//return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			//TISBOX-1618
			//final CartModel cartModel = getCartService().getSessionCart();
			mplCouponFacade.releaseVoucherInCheckout(serviceCart);
			mplCartFacade.removeDeliveryMode(serviceCart); //TISPT-104 // Cart recalculation method invoked inside this method
			//applyPromotions();


			final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);

			//timeOutSet(model);
			if (StringUtils.isNotEmpty(defaultPinCodeId) && (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries())))
			{
				//CAR-126/128/129
				responseData = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
				if (CollectionUtils.isEmpty(responseData))
				{
					responseData = mplCartFacade.getOMSPincodeResponseData(defaultPinCodeId, cartData);
				}
				if (CollectionUtils.isNotEmpty(responseData))
				{
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
								pinCodeResponseData = mplCartFacade.getVlaidDeliveryModesByInventory(pinCodeResponseData, cartData);
							}
						}
						catch (final Exception e)
						{
							LOG.error("Exception occured while checking inventory " + e.getCause());
						}
					}
					//  TISPRD-1951  END //
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartData, responseData, serviceCart);
					fullfillmentDataMap = mplCartFacade.getFullfillmentMode(cartData);

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

					//	this.prepareDataForPage(model);
					storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
					setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
					//				model.addAttribute(
					//						WebConstants.BREADCRUMBS_KEY,
					//						getResourceBreadcrumbBuilder().getBreadcrumbs(
					//								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
					model.addAttribute("metaRobots", "noindex,nofollow");
					//		setCheckoutStepLinksForModel(model, getCheckoutStep());
					//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);

					GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
					model.addAttribute("checkoutPageName", checkoutPageName);
					model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629
					model.addAttribute("progressBarClass", "choosePage");
				}
				else
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg="
							+ MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE + "&type=error", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
			}
			else
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + "/cart" + "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			//returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
			returnPage = "addon:/marketplacecheckoutaddon/pages/checkout/single/showDeliveryModesDetails";
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

	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSLOTSURL, method = RequestMethod.GET)
	public String selectDeleverySlots(final Model model) throws CMSItemNotFoundException, UnsupportedEncodingException
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartData cartDataSupport = mplCartFacade.getSessionCartWithEntryOrdering(true);
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
						if (productRichAttributeModel != null && productRichAttributeModel.size() > 0
								&& null != productRichAttributeModel.get(0)
								&& null != productRichAttributeModel.get(0).getScheduledDelivery())
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
				final InvReserForDeliverySlotsResponseData deliverySlotsResponseData = mplCartFacade.convertDeliverySlotsDatatoWsdto(
						deliverySlotsRequestData, cartModel);
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
					final String requestQueryParam = UriUtils.encodeQuery("?redirectString=" + "redirectToReviewOrder"
							+ "&type=ajaxRedirect", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
					//return getCheckoutStep().nextStep();
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
				fullfillmentDataMap = mplCartFacade.getFullfillmentMode(cartDataSupport);
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartDataSupport);
				model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
				model.addAttribute("mplconfigModel", deliverySlotCharge);
				model.addAttribute("defaultPincode",
						getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE));

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
		final String requestQueryParam = UriUtils.encodeQuery("?redirectString=" + "redirectToReviewOrder" + "&type=ajaxRedirect",
				"UTF-8");
		return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
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
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETCNCSTRORES, method = RequestMethod.GET)
	public String doFindCncStores(final String entryNumber, final String sellerArticleSKU, final String deliveryCode,
			final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request,
			final HttpServletResponse response1, final HttpSession session) throws CMSItemNotFoundException,
			UnsupportedEncodingException
	{
		if (getUserFacade().isAnonymousUser())
		{
			final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
					+ "&type=redirect", "UTF-8");
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from doFindDeliveryMode method in Controller");
		}
		List<PinCodeResponseData> responseData = null;
		//TISPT-400


		if (getCartService().hasSessionCart())
		{
			final CartModel cartModel = getCartService().getSessionCart();

			//CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);

			try
			{
				//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cartData); //CAR-197
				final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cartModel);
				if (cartItemDelistedStatus)
				{
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
							MarketplacecommerceservicesConstants.TRUE_UPPER);
					final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
							+ "&type=redirect", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
					//return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
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
			//	redirectAttributes.addFlashAttribute("deliveryMethodForm", deliveryMethodForm);
			//create session object for deliveryMethodForm which will be used if cart contains both cnc and home delivery.
			//		session.setAttribute("deliveryMethodForm", deliveryMethodForm);

			// CAR-197  start
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart(); //CAR-197

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);

			final String defaultPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			//Validating request
			responseData = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES);
			if (CollectionUtils.isEmpty(responseData))
			{
				responseData = mplCartFacade.getOMSPincodeResponseData(defaultPincode, cartData);
			}
			//final boolean isValid = false;
			if (CollectionUtils.isNotEmpty(responseData))
			{

				//handle freebie
				final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
				final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

				final List<StoreLocationRequestData> storeLocationRequestDataList = new ArrayList<StoreLocationRequestData>();
				Boolean selectPickupDetails = Boolean.FALSE;
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
						if (null != cartD && !cartD.isGiveAway()
								&& null != entryNumber //CAR-197
								&& null != sellerArticleSKU && null != deliveryCode
								&& cartD.getEntryNumber().toString().equals(entryNumber))
						{
							try
							{

								//							final String deliveryCode = deliveryMethodForm.getDeliveryMethodEntry()
								//									.get(cartD.getEntryNumber().intValue()).getDeliveryCode();
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

									getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode, sellerArticleSKU, cartModel);
								}


								break;
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

					//cart has CNC.
					LOG.info("Cart Entries contain CNC mode");
					//calls oms to get inventories for given stores.
					//response = mplCartFacade.getStoreLocationsforCnC(storeLocationRequestDataList);
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
						if (null != entryNumber && null != sellerArticleSKU && null != deliveryCode)
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

					model.addAttribute("entryNumber", entryNumber);
					model.addAttribute("pickupPersonName", pickupPersonName.trim());
					model.addAttribute("pickUpPersonMobile", pickUpPersonMobile);
					model.addAttribute("delModeCount", Integer.valueOf(delModeCount));
					model.addAttribute("cnccount", Integer.valueOf(count));
					model.addAttribute("expCheckout", Integer.valueOf(expCheckout));
					model.addAttribute("defaultPincode", defaultPincode);
					model.addAttribute("pwpos", productWithPOS);
					model.addAttribute("CSRFToken", CSRFTokenManager.getTokenForSession(request.getSession()));

				}

				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				//		/TPR-1803
				//	setCheckoutStepLinksForModel(model, getCheckoutStep());
				//		model.addAttribute("checkoutPageName", checkoutPageName);
				//model.addAttribute("progressBarClass", "choosePage");

			}
			else
			{
				String requestQueryParam;
				try
				{
					requestQueryParam = UriUtils.encodeQuery("?msg=" + "USSID not CNC serviceable" + "&type=error", "UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				catch (final UnsupportedEncodingException e)
				{
					// YTODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		else
		{
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
		}
		//return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChoosePickupLocationPage;
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/showPickupLocation";
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
	public String doSelectDeliveryMode(@ModelAttribute("deliveryMethodForm") final DeliveryMethodForm deliveryMethodForm,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request, final HttpSession session)
			throws CMSItemNotFoundException
	{
		String returnPage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartModel cartModel = getCartService().getSessionCart();

			final boolean cartItemDelistedStatus = mplCartFacade.isCartEntryDelisted(cartModel);
			//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
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

			//commented for CAR:127

			/*
			 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
			 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartModel);
			 */

			//CAR:127
			//final CartData caData = getMplCartFacade().getCartDataFromCartModel(cartModel, false);
			CartData cartUssidData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			//TISST-13012

			final boolean inventoryReservationStatus = mplCartFacade.isInventoryReserved(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, cartUssidData, cartModel);


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
					responseData = mplCartFacade.getOMSPincodeResponseData(defaultPinCodeId, cartUssidData);
				}
				deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartUssidData, responseData, cartModel);

				mplCartFacade.setDeliveryDate(cartUssidData, responseData);
				//TISUTO-72 TISST-6994,TISST-6990 set cart COD eligible
				final boolean isCOdEligible = mplCartFacade.addCartCodEligible(deliveryModeDataMap, responseData, cartModel,
						cartUssidData);
				LOG.info("isCOdEligible " + isCOdEligible);

			}

			LOG.debug(">>>>>>>>>>  Step 5:  Cod status setting done  ");

			// TPR-429 START
			final String cartLevelSellerID = populateCheckoutSellers(cartUssidData);

			model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, cartLevelSellerID);
			// TPR-429 END



			//CAR-130
			/* final CartData cartData = getMplCustomAddressFacade().getCheckoutCart(); */
			cartUssidData = getMplCustomAddressFacade().getCheckoutCart();
			List<AddressData> deliveryAddress = null;

			//TIS 391:  In case of normal checkout , delivery address will be null and for express checkout delivery address will be ore selected from the cart page
			// In case of express checkout it will redirect to the payment page from delivery mode selection

			if (cartUssidData != null)
			{
				if (cartUssidData.getDeliveryAddress() != null)
				{
					LOG.debug("Express checkout selected for address id : " + cartUssidData.getDeliveryAddress().getId());
					//return getCheckoutStep().nextStep();
				}


				deliveryAddress = getMplCustomAddressFacade().getDeliveryAddresses(cartUssidData.getDeliveryAddress(), cartModel); //CAR-194

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

			//final List<StateData> stateDataList = accountAddressFacade.getStates();
			//final AccountAddressForm addressForm = new AccountAddressForm();
			//addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);

			//model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, mplCartFacade.getSessionCartWithEntryOrdering(true));
			//model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			//model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			//model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			//model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
			//			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWSAVEDTOADDRESSBOOK, Boolean.TRUE);
			//			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			//			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.TRUE);
			//			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
			//			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			//			model.addAttribute(
			//					WebConstants.BREADCRUMBS_KEY,
			//					getResourceBreadcrumbBuilder().getBreadcrumbs(
			//							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartUssidData);
			//		model.addAttribute("checkoutPageName", selectAddress);
			//		setCheckoutStepLinksForModel(model, getCheckoutStep());
			//model.addAttribute("progressBarClass", "selectPage");
			//returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;

			//deliveryModeData = new HashMap<String, List<MarketplaceDeliveryModeData>>();
			final Map<String, MarketplaceDeliveryModeData> delModeDataPopulateMap = mplCartFacade
					.getDeliveryModeMapForReviewOrder(cartUssidData);
			model.addAttribute("deliveryModeData", delModeDataPopulateMap);

			final String defaultPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			model.addAttribute("defaultPincode", defaultPincode);
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
				final String requestQueryParam = UriUtils.encodeQuery("?redirectString=" + "redirectToReviewOrder"
						+ "&type=ajaxRedirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				//returnPage = "addon:/marketplacecheckoutaddon/pages/checkout/single/showReviewOrder";
			}
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

	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETREVIEWORDER, method = RequestMethod.GET)
	public String viewReviewOrder(final Model model)
	{
		try
		{
			final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			final Map<String, MarketplaceDeliveryModeData> delModeDataPopulateMap = mplCartFacade
					.getDeliveryModeMapForReviewOrder(cartData);
			model.addAttribute("deliveryModeData", delModeDataPopulateMap);
			final String defaultPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
			model.addAttribute("defaultPincode", defaultPincode);
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/showReviewOrder";
	}

	@RequestMapping(value = "/deliveryAddress", method = RequestMethod.GET)
	public @ResponseBody JSONObject getDeliveryAddress(final Model model)
	{
		final JSONObject jsonObject = new JSONObject();
		String addressLine = null;
		final String emptySpace = " ";
		final String countrycode = "IN,";
		final String phoneCode = "+91";
		final String comma = ",";
		final AddressData addressData = mplCustomAddressFacade.getDeliveryAddress();
		final StringBuilder pickupPerson = new StringBuilder();
		pickupPerson.append((null != addressData.getFirstName()) ? addressData.getFirstName() : "").append(" ")
				.append((null != addressData.getLastName()) ? addressData.getLastName() : "");

		final String pickUpPersonMobile = (null != addressData.getPhone()) ? addressData.getPhone() : "";

		try
		{
			jsonObject.put("firstName", addressData.getFirstName());
			jsonObject.put("lastName", addressData.getLastName());
			jsonObject.put("pickupPersonName", pickupPerson.toString());
			jsonObject.put("pickupPersonMobileNo", pickUpPersonMobile);
			final String Line1 = addressData.getLine1().concat(comma);
			if (null != addressData.getLine2())
			{
				addressLine = Line1.concat(emptySpace).concat(addressData.getLine2()).concat(comma);
			}
			if (null != addressData.getLine3())
			{
				addressLine = addressLine.concat(emptySpace).concat(addressData.getLine3()).concat(comma);
			}
			addressLine = addressLine.concat(emptySpace).concat(addressData.getTown()).concat(comma).concat(emptySpace)
					.concat(addressData.getState()).concat(comma).concat(emptySpace).concat(addressData.getPostalCode())
					.concat(emptySpace);
			final String countryAndPhone = countrycode.concat(emptySpace).concat(phoneCode).concat(emptySpace)
					.concat(addressData.getPhone());
			addressLine = addressLine.concat(countryAndPhone);
			jsonObject.put("fullAddress", addressLine);
			jsonObject.put("type", "response");

		}
		catch (final JSONException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping(value = "/deliveryModesSelected", method = RequestMethod.GET)
	public @ResponseBody JSONObject deliveryModesSelected(final Model model)
	{
		final JSONObject jsonObject = new JSONObject();
		int homeDelivery = 0;
		int expressDelivery = 0;
		int clickNcollect = 0;
		int countItems = 0;
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			for (final OrderEntryData cartD : cartData.getEntries())
			{
				final MarketplaceDeliveryModeData marketplaceDeliveryModeData = cartD.getMplDeliveryMode();
				if (marketplaceDeliveryModeData.getCode().equalsIgnoreCase("home-delivery"))
				{
					homeDelivery++;
				}
				if (marketplaceDeliveryModeData.getCode().equalsIgnoreCase("express-delivery"))
				{
					expressDelivery++;
				}
				if (marketplaceDeliveryModeData.getCode().equalsIgnoreCase("click-and-collect"))
				{
					clickNcollect++;
				}
				countItems++;
			}
			jsonObject.put("totalPrice", cartData.getTotalPriceWithConvCharge().getFormattedValueNoDecimal());
			jsonObject.put("hd", homeDelivery);
			jsonObject.put("ed", expressDelivery);
			jsonObject.put("cnc", clickNcollect);
			jsonObject.put("CountItems", countItems);
			jsonObject.put("type", "response");

		}
		catch (final JSONException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	private void prepareModelForPayment(final Model model, final CartData cartData) throws CMSItemNotFoundException
	{
		Map<String, Boolean> paymentModeMap = null;
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
			throws UnsupportedEncodingException
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
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			ValidationResults validationResult = null;
			validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
			if (null != validationResult && ValidationResults.REDIRECT_TO_CART.equals(validationResult))
			{
				jsonObj.put("url", MarketplacecheckoutaddonConstants.CART);
				jsonObj.put("type", "redirect");
				return jsonObj;
			}

			final boolean selectPickupDetails = false;
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

				for (final OrderEntryData orderEntry : cartData.getEntries())
				{
					if (null != orderEntry.getDeliveryPointOfService())
					{
						final String pickupPersonName = cartData.getPickupPersonName();
						final String pickupPersonMobile = cartData.getPickupPersonMobile();
						if ((pickupPersonName == null) || (pickupPersonMobile == null))
						{
							//						selectPickupDetails = true;
							//						model.addAttribute("selectPickupDetails", Boolean.valueOf(selectPickupDetails));
							//						return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/check";

							jsonObj.put("url", "/checkout/single");
							jsonObj.put("type", "redirect");
							return jsonObj;
						}
					}
				}
			}

			//model.addAttribute(MarketplacecheckoutaddonConstants.ISCART, Boolean.TRUE); //TPR-629

			//model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			jsonObj.put("validation", "success");
			jsonObj.put("totalPrice", cartData.getTotalPriceWithConvCharge().getFormattedValueNoDecimal());
			jsonObj.put("type", "response");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return jsonObj;
	}

	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String getPaymentPage(final Model model, final RedirectAttributes redirectAttributes,
			@RequestParam(value = "value", required = false, defaultValue = "") final String guid)
			throws UnsupportedEncodingException
	{

		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}
			final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
			ValidationResults validationResult = null;
			if (StringUtils.isEmpty(guid))
			{
				validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
			}
			if (null != validationResult && ValidationResults.REDIRECT_TO_CART.equals(validationResult))
			{
				final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
						+ "&type=redirect", "UTF-8");
				return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			}

			boolean selectPickupDetails = false;
			//Payment page will be shown based on cart or order(after first failure payment) TPR-629
			OrderModel orderModel = null;
			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
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
								return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/check";
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
					final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
							+ "&type=redirect", "UTF-8");
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

				//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);

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
				model.addAttribute(MarketplacecheckoutaddonConstants.GUID, orderModel.getGuid());

				//GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
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

			final String payNowPromotionCheck = getSessionService().getAttribute(
					MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);
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

			final String requestQueryParam = UriUtils
					.encodeQuery("?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again."
							+ "&type=error", "UTF-8");
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}

		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.E0007, e);
			//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			//					MarketplacecheckoutaddonConstants.ERRORMSG);

			final String requestQueryParam = UriUtils
					.encodeQuery("?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again."
							+ "&type=error", "UTF-8");
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

			final String requestQueryParam = UriUtils
					.encodeQuery("?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again."
							+ "&type=error", "UTF-8");
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

			final String requestQueryParam = UriUtils
					.encodeQuery("?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again."
							+ "&type=error", "UTF-8");
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}

		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			//					MarketplacecheckoutaddonConstants.ERRORMSG);

			final String requestQueryParam = UriUtils
					.encodeQuery("?msg=" + "An error occurred contacting the payment provider.  Wait a few minutes and try again."
							+ "&type=error", "UTF-8");
			return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
			//return getCheckoutStep().previousStep();
		}

		return "addon:/marketplacecheckoutaddon/pages/checkout/single/showAddPaymentMethodPage";
	}

	@RequestMapping(value = "/pickupPerson/popup", method = RequestMethod.GET)
	public String getPickUpPersonPopUp(final Model model)
	{
		return "addon:/marketplacecheckoutaddon/pages/checkout/single/showPickupPersonDetails";
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
		// YTODO Auto-generated method stub
		final double deliveryMode = Double.parseDouble(configurationService.getConfiguration().getString(
				"checkout.deliverymode.timeout"));
		final String timeOut = Double.toString(deliveryMode * 60 * 1000);
		model.addAttribute(MarketplacecheckoutaddonConstants.TIMEOUT, timeOut);
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
	 *
	 */
	@RequestMapping(value = "/removereviewcart", method = RequestMethod.GET)
	public String removeFromMinicart(final Model model, final HttpServletRequest request) throws CommerceCartModificationException
	{
		CartModel cartModel = null;
		final String entryNumberString = request.getParameter("entryNumber");
		final String returnPage = "addon:/marketplacecheckoutaddon/pages/checkout/single/showReviewOrder";
		try
		{
			final long entryNumber = Long.parseLong(entryNumberString);

			if (mplCartFacade.hasEntries())
			{
				//Fetch delivery point of service before recalculation of cart
				CartModel cartModelBeforeRecalculation = null;
				final Map deliveryPOSMap = new HashMap();
				cartModelBeforeRecalculation = cartService.getSessionCart();
				for (final AbstractOrderEntryModel entry : cartModelBeforeRecalculation.getEntries())
				{
					if (entry.getDeliveryPointOfService() != null)
					{
						deliveryPOSMap.put(entry.getSelectedUSSID(), entry.getDeliveryPointOfService());
					}
				}
				final CartModificationData cartModification = mplCartFacade.updateCartEntry(entryNumber, 0);
				cartModel = cartService.getSessionCart();
				//Fetch delivery address before recalculation of cart
				final AddressData addressData = mplCustomAddressFacade.getDeliveryAddress();

				mplCouponFacade.releaseVoucherInCheckout(cartModel);
				mplCartFacade.getCalculatedCart(cartModel);
				mplCartFacade.setCartSubTotal(cartModel);
				mplCartFacade.totalMrpCal(cartModel);


				if (cartModification.getQuantity() == 0)
				{
					if (!mplCartFacade.hasEntries())
					{
						final String requestQueryParam = UriUtils.encodeQuery("?url=" + MarketplacecheckoutaddonConstants.CART
								+ "&type=redirect", "UTF-8");
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
							session.removeAttribute("deliveryMethodForm");
						}
						//Re-populating delivery address after recalculation of cart
						getMplCustomAddressFacade().setDeliveryAddress(addressData);
						//Re-populating delivery point of service after recalculation of cart
						if (cartModelBeforeRecalculation != null)
						{
							getMplCheckoutFacade().rePopulateDeliveryPointOfService(deliveryPOSMap, cartModel);
						}
						final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
						Map<String, MarketplaceDeliveryModeData> deliveryModeDataMap = new HashMap<String, MarketplaceDeliveryModeData>();
						deliveryModeDataMap = mplCartFacade.getDeliveryModeMapForReviewOrder(cartData);

						model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
						model.addAttribute("deliveryModeData", deliveryModeDataMap);
						final String defaultPincode = getSessionService().getAttribute(
								MarketplacecommerceservicesConstants.SESSION_PINCODE);
						model.addAttribute("defaultPincode", defaultPincode);
					}
				}
				else
				{
					final String requestQueryParam = UriUtils.encodeQuery("?msg=" + "Could not remove item from cart" + "&type=error",
							"UTF-8");
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
			}
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.error("CommerceCartModificationException while remove item from minicart ", ex);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions while remove item from minicart ", ex);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while remove item from minicart ", ex);
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
	 *
	 * @param ProductCode
	 */
	private void removeEntryByProductCode(final String ProductCode) throws EtailBusinessExceptions, EtailNonBusinessExceptions,
			Exception

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

		model.addAttribute(MarketplacecheckoutaddonConstants.JUSPAYJSNAME,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYJSNAMEVALUE));
		model.addAttribute(MarketplacecheckoutaddonConstants.SOPFORM, new PaymentDetailsForm());
		//Terms n Conditions Link
		model.addAttribute(MarketplacecheckoutaddonConstants.TNCLINK,
				configurationService.getConfiguration().getString(MarketplacecheckoutaddonConstants.TNCLINKVALUE));

	}

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
		final String sessionId = getMd5Encoding(getRandomAlphaNum(configurationService.getConfiguration().getString(
				MarketplacecheckoutaddonConstants.EBS_SESSION_ID_KEY)));
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
			messageDigest = MessageDigest.getInstance(configurationService.getConfiguration().getString(
					MarketplacecheckoutaddonConstants.JUSPAY_ENCODING_TYPE));
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
}
