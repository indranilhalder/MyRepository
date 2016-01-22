/*
update * [y] hybris Platform
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
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateQuantityForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.EmailValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.PasswordValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.ProfileValidator;
import de.hybris.platform.acceleratorstorefrontcommons.forms.verification.AddressVerificationResultHandler;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.data.WishlistProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.granule.json.JSON;
import com.granule.json.JSONArray;
import com.granule.json.JSONException;
import com.granule.json.JSONObject;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.enums.AddressType;
import com.tisl.mpl.core.enums.FeedbackArea;
import com.tisl.mpl.core.enums.Frequency;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.MarketplacePreferenceModel;
import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.AddressTypeData;
import com.tisl.mpl.data.CouponHistoryData;
import com.tisl.mpl.data.CouponHistoryStoreDTO;
import com.tisl.mpl.data.EditWishlistNameData;
import com.tisl.mpl.data.ExistingWishlistData;
import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.data.NewWishlistData;
import com.tisl.mpl.data.ParticularWishlistData1;
import com.tisl.mpl.data.RemoveWishlistData;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.impl.MplCheckoutFacadeImpl;
import com.tisl.mpl.facade.mystyleprofile.MyStyleProfileFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.preference.MplPreferenceFacade;
import com.tisl.mpl.facades.account.register.FriendsInviteFacade;
import com.tisl.mpl.facades.account.register.MplCustomerProfileFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.account.reviews.impl.DefaultMplReviewFacade;
import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.data.MplPreferencePopulationData;
import com.tisl.mpl.facades.payment.impl.MplPaymentFacadeImpl;
import com.tisl.mpl.facades.product.data.CategoryData;
import com.tisl.mpl.facades.product.data.DayData;
import com.tisl.mpl.facades.product.data.GenderData;
import com.tisl.mpl.facades.product.data.MonthData;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.product.data.MyStyleProfileData;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.facades.product.data.SendInvoiceData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.facades.product.data.YearData;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.cms.components.MyWishListInHeaderComponentModel;
import com.tisl.mpl.order.facade.GetOrderDetailsFacade;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.GigyaService;
import com.tisl.mpl.service.MplGigyaReviewCommentService;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.util.AllProductsInWishlistByDate;
import com.tisl.mpl.storefront.util.AllWishListCompareByDate;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.FriendsInviteForm;
import com.tisl.mpl.storefront.web.forms.MplCustomerProfileForm;
import com.tisl.mpl.storefront.web.forms.ReturnRequestForm;
import com.tisl.mpl.storefront.web.forms.validator.AccountAddressValidator;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.storefront.web.forms.validator.MplCustomerProfileFormValidator;
import com.tisl.mpl.storefront.web.forms.validator.MplEmailValidator;
import com.tisl.mpl.storefront.web.forms.validator.MplPasswordValidator;
import com.tisl.mpl.storefront.web.forms.validator.MplUpdateEmailFormValidator;
import com.tisl.mpl.ticket.facades.MplSendTicketFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.GigyaProductReviewWsDTO;


/**
 * Controller for home page
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_MY_ACCOUNT)
public class AccountPageController extends AbstractMplSearchPageController
{
	// Internal Redirects
	private static final String REDIRECT_MY_ACCOUNT = REDIRECT_PREFIX + "/my-account";
	private static final String REDIRECT_TO_ADDRESS_BOOK_PAGE = REDIRECT_PREFIX + RequestMappingUrlConstants.LINK_MY_ACCOUNT
			+ RequestMappingUrlConstants.LINK_ADDRESS_BOOK;
	private static final String REDIRECT_TO_PAYMENT_INFO_PAGE = REDIRECT_PREFIX + "/my-account/payment-details";
	private static final String REDIRECT_TO_PROFILE_PAGE = REDIRECT_PREFIX + "/my-account/profile";
	private static final String REDIRECT_TO_UPDATE_PROFILE_PAGE = REDIRECT_PREFIX + "/my-account/update-profile";
	private static final String REDIRECT_TO_INVITE_FRIENDS_PAGE = REDIRECT_PREFIX + "/my-account/friendsInvite";

	// CMS Pages
	private static final String ACCOUNT_CMS_PAGE = "overview";
	private static final String PROFILE_CMS_PAGE = "profile";
	private static final String UPDATE_PASSWORD_CMS_PAGE = "updatePassword";
	private static final String UPDATE_PROFILE_CMS_PAGE = "update-profile";
	private static final String UPDATE_EMAIL_CMS_PAGE = "update-email";
	private static final String ADDRESS_BOOK_CMS_PAGE = "address-book";
	private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
	private static final String PAYMENT_DETAILS_CMS_PAGE = "payment-details";
	private static final String ORDER_HISTORY_CMS_PAGE = "orders";
	private static final String ORDER_DETAIL_CMS_PAGE = "order";
	private static final String ACCOUNT_CMS_COUPONS = "coupons";
	private static final String WISHLIST_CMS_PAGE = "wishlist";
	private static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";
	private static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";
	private static final String FRIENDS_INVITE_CMS_PAGE = "inviteFriends";
	private static final String SEND_INVOICE = "SendInvoice";
	private static final String BZ_ERROR_CMS_PAGE = "businessErrorFound";
	private static final String NBZ_ERROR_CMS_PAGE = "nonBusinessErrorFound";
	private static final String MARKETPLACE_PREFERENCE = "marketplacePreference";
	private static final String MY_INTEREST = "myInterest";
	private static final String MY_STYLE_PROFILE = "myStyleProfile";
	private static final String REVIEW_CMS_PAGE = "reviews";
	private static final Logger LOG = Logger.getLogger(AccountPageController.class);
	private String dateDOB = MarketplacecommerceservicesConstants.EMPTY;
	private String dateDOAnn = MarketplacecommerceservicesConstants.EMPTY;
	private static final String RETURN_REQUEST = "returnRequest";
	private static final String RETURN_SUBMIT = "returnSubmit";
	private static final String RETURN_SUCCESS = "returnSuccess";
	private static final String ERROR_MSG = "errorMsg";
	private static final String ERROR_OCCURED = "errorOccured";
	private static final String UTF = "UTF-8";
	public static final String ERROR_RESP = "gigys response error.";
	public static final String UNUSED = "unused";
	public static final String STATUS = "status";
	//	Variable declaration with @Resource annotation
	@Resource(name = ModelAttributetConstants.ACCELERATOR_CHECKOUT_FACADE)
	private CheckoutFacade checkoutFacade;
	@Resource(name = ModelAttributetConstants.USER_FACADE)
	private UserFacade userFacade;
	@Resource(name = ModelAttributetConstants.CUSTOMER_FACADE)
	private CustomerFacade customerFacade;
	@Resource(name = ModelAttributetConstants.ACCOUNT_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;
	@Resource(name = ModelAttributetConstants.PASSWORD_VALIDATOR)
	private PasswordValidator passwordValidator;
	@Resource(name = ModelAttributetConstants.ACCOUNT_ADDRESS_VALIDATOR)
	private AccountAddressValidator accountAddressValidator;
	@Resource(name = ModelAttributetConstants.PROFILE_VALIDATOR)
	private ProfileValidator profileValidator;
	@Resource(name = ModelAttributetConstants.EMAIL_VALIDATOR)
	private EmailValidator emailValidator;
	@Resource(name = ModelAttributetConstants.I18N_FACADE)
	private I18NFacade i18NFacade;
	@Resource(name = ModelAttributetConstants.ADDRESS_VARIFICATION_FACADE)
	private AddressVerificationFacade addressVerificationFacade;
	@Resource(name = ModelAttributetConstants.ADDRESS_VARIFICATION_RESULT_HANDLER)
	private AddressVerificationResultHandler addressVerificationResultHandler;
	//	@Resource(name = ModelAttributetConstants.PRODUCT_SERVICE)
	//	private ProductService productService;
	@Resource(name = ModelAttributetConstants.ACC_PRODUCT_FACADE)
	private ProductFacade productFacade;
	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;
	@Resource(name = ModelAttributetConstants.SESSION_SERVICE)
	private SessionService sessionService;
	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;
	/*
	 * @Resource(name = "defaultEmailService") private EmailService emailService;
	 */
	@Resource(name = "getOrderDetailsFacade")
	private GetOrderDetailsFacade getOrderDetailsFacade;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;


	//	Autowired variable declaration
	@Autowired
	private MplPaymentFacadeImpl mplPaymentFacadeImpl;
	@Autowired
	private MplSendTicketFacade mplSendTicketFacade;
	@Autowired
	private MplAddressValidator mplAddressValidator;
	@Autowired
	private UserService userService;
	@Autowired
	private WishlistFacade wishlistFacade;
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Autowired
	private FriendsInviteFacade friendsInviteFacade;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private MplCustomerProfileFacade mplCustomerProfileFacade;
	@Autowired
	private MplCouponFacade mplCouponFacade;
	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;
	@Autowired
	private MplCustomerProfileFormValidator mplCustomerProfileFormValidator;
	@Autowired
	private MplUpdateEmailFormValidator mplUpdateEmailFormValidator;
	@Autowired
	private RegisterCustomerFacade registerCustomerFacade;
	@Autowired
	private MplPasswordValidator mplPasswordValidator;
	@Autowired
	private GigyaService gigyaService;

	//	@Autowired Critical Sonar fixes Unused private Field
	//	private BaseSiteService baseSiteService;
	//	@Autowired
	//	private BaseStoreService baseStoreService;
	//	@Autowired
	//	private CommonI18NService commonI18NService;
	//	@Autowired
	//	private I18NService i18NService;
	//	@Autowired
	//	private BusinessProcessService businessProcessService;
	@Autowired
	private MplEmailValidator mplEmailValidator;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Autowired
	private MplCheckoutFacadeImpl mplCheckoutFacadeImpl;
	@Autowired
	private MplPreferenceFacade mplPreferenceFacade;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private CancelReturnFacade cancelReturnFacade;
	@Autowired
	private MplOrderService mplOrderService;
	@Autowired
	private BuyBoxFacade buyBoxFacade;
	@Autowired
	private ProductDetailsHelper productDetailsHelper;
	@Autowired
	private MplGigyaReviewCommentService gigyaCommentService;
	@Autowired
	private DefaultMplReviewFacade mplReviewrFacade;

	@Autowired
	private MyStyleProfileFacade myStyleProfileFacade;

	protected PasswordValidator getPasswordValidator()
	{
		return passwordValidator;
	}

	protected AccountAddressValidator getAddressValidator()
	{
		return accountAddressValidator;
	}

	protected ProfileValidator getProfileValidator()
	{
		return profileValidator;
	}

	protected EmailValidator getEmailValidator()
	{
		return emailValidator;
	}

	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	protected AddressVerificationFacade getAddressVerificationFacade()
	{
		return addressVerificationFacade;
	}

	protected AddressVerificationResultHandler getAddressVerificationResultHandler()
	{
		return addressVerificationResultHandler;
	}



	/**
	 * @description method is called to get the type of Address
	 * @return Collection
	 */
	private Collection<AddressTypeData> getAddressType()
	{
		final List<EnumerationValueModel> enumList = mplEnumerationHelper.getEnumerationValuesForCode(AddressType._TYPECODE);
		final Collection<AddressTypeData> addressTypes = new ArrayList<>();
		for (final EnumerationValueModel enumerationValueModel : enumList)
		{
			if (enumerationValueModel != null)
			{
				final AddressTypeData addressTypeData = new AddressTypeData();
				addressTypeData.setCode(enumerationValueModel.getCode());
				addressTypeData.setName(enumerationValueModel.getCode());
				addressTypes.add(addressTypeData);
			}
		}
		return addressTypes;
	}

	/**
	 * @description this method is called to fetch all countries
	 * @return Collection
	 */
	@ModelAttribute(ModelAttributetConstants.COUNTRIES)
	public Collection<CountryData> getCountries()
	{
		return checkoutFacade.getDeliveryCountries();
	}

	/**
	 * @description this method is called to fetch all titles
	 * @return Collection
	 */
	@ModelAttribute(ModelAttributetConstants.TITLES)
	public Collection<TitleData> getTitles()
	{
		return userFacade.getTitles();
	}

	/**
	 * @description method is called to get the CountryData for Address
	 * @return Map
	 */
	@ModelAttribute(ModelAttributetConstants.COUNTRY_DATA_MAP)
	public Map<String, CountryData> getCountryDataMap()
	{
		final Map<String, CountryData> countryDataMap = new HashMap<>();
		for (final CountryData countryData : getCountries())
		{
			countryDataMap.put(countryData.getIsocode(), countryData);
		}
		return countryDataMap;
	}

	/**
	 * @description method is called to get the CountryAddressForm for Address
	 * @param addressCode
	 * @param countryIsoCode
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ADDRESS_FORM, method = RequestMethod.GET)
	public String getCountryAddressForm(@RequestParam(ModelAttributetConstants.ADDRESS_CODE) final String addressCode,
			@RequestParam(ModelAttributetConstants.COUNTRY_ISO_CODE) final String countryIsoCode, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			model.addAttribute(ModelAttributetConstants.SUPPORTED_COUNTRIES, getCountries());
			model.addAttribute(ModelAttributetConstants.REGIONS, getI18NFacade().getRegionsForCountryIso(countryIsoCode));
			model.addAttribute(ModelAttributetConstants.COUNTRY, countryIsoCode);
			model.addAttribute(ModelAttributetConstants.ADDRESS_TYPE, getAddressType());

			final AccountAddressForm addressForm = new AccountAddressForm();
			model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			for (final AddressData addressData : getAccountAddressFacade().getAddressBook())
			{
				if (null != addressData && addressData.getId() != null && addressData.getId().equals(addressCode)
						&& null != countryIsoCode && countryIsoCode.equals(addressData.getCountry().getIsocode()))
				{
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
					if (addressData.getRegion() != null && StringUtils.isNotEmpty(addressData.getRegion().getIsocode()))
					{
						addressForm.setRegionIso(addressData.getRegion().getIsocode());
					}
					break;
				}
			}
			return ControllerConstants.Views.Fragments.Account.CountryAddressForm;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to get the overview page
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String account(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			sessionService.setAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
			sessionService.setAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG, ModelAttributetConstants.N_CAPS_VAL);
			storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.MY_ACCOUNT_OVERVIEW));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}


	/**
	 * This method returns the order history page
	 *
	 * @description
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ORDERS, method = RequestMethod.GET)
	@RequireHardLogIn
	public String orders(
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = ModelAttributetConstants.ZERO_VAL) final int page,
			@RequestParam(value = ModelAttributetConstants.SHOW, defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = ModelAttributetConstants.SORT, required = false) final String sortCode, final Model model)

	throws CMSItemNotFoundException
	{
		String formattedOrderDate = "";
		final List<OrderData> orderDataList = new ArrayList<OrderData>();
		List<OrderHistoryData> orderHistoryList = null;
		final List<OrderData> subOrderDetailsList = new ArrayList<OrderData>();
		final Map<String, String> orderFormattedDateMap = new HashMap<String, String>();
		final Map<String, List<OrderEntryData>> currentProductMap = new HashMap<>();
		List<OrderEntryData> cancelProduct = new ArrayList<OrderEntryData>();
		LOG.debug("Step1-************************Order History");
		try
		{
			final int pageSizeInoh = Integer.valueOf(configurationService.getConfiguration()
					.getString(MessageConstants.ORDER_HISTORY_PAGESIZE).trim());
			// TISPRO-48 - Changes made for implementing lazy loading in Order history pagination
			final int pageSize = Integer.valueOf(configurationService.getConfiguration()
					.getString(MessageConstants.ORDER_HISTORY_PAGESIZE, "10").trim());
			final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);

			//final SearchPageData<OrderHistoryData> searchPageDataParentOrder = getMplOrderFacade().getPagedParentOrderHistoryForStatuses(pageableData);

			//TISEE-1855
			final SearchPageData<OrderHistoryData> searchPageDataParentOrder = getMplOrderFacade()
					.getPagedFilteredParentOrderHistory(pageableData);


			populateModel(model, searchPageDataParentOrder, showMode);

			final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
			final List<OrderModel> orderModels = (List<OrderModel>) customerModel.getOrders();

			if (CollectionUtils.isNotEmpty(orderModels))
			{
				LOG.debug("Step1-************************Order History :fetching product serial no ");
				final Map<String, Map<String, String>> productSerrialNumber = getMplOrderFacade().fetchOrderSerialNoDetails(
						orderModels);
				model.addAttribute("productSerrialNumber", productSerrialNumber);

				LOG.debug("Step2-************************Order History :fetching invoice details ");
				final Map<String, Boolean> sortInvoice = getMplOrderFacade().fetchOrderInvoiceDetails(orderModels);

				orderHistoryList = searchPageDataParentOrder.getResults();
				for (final OrderHistoryData orderHistoryData : orderHistoryList)
				{
					LOG.debug("Step3-************************Order History :code" + orderHistoryData.getCode());

					final OrderData orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderHistoryData.getCode());
					//this scenario will occour only when product is missing in order entries.
					if (null == orderDetails)
					{
						continue;
					}
					LOG.debug("Step4-************************Order History: After order:" + orderHistoryData.getCode());
					final List<OrderData> subOrderList = orderDetails.getSellerOrderList();
					for (final OrderData subOrder : subOrderList)
					{
						for (OrderEntryData orderEntryData : subOrder.getEntries())
						{
							orderEntryData = getMplOrderFacade().fetchOrderEntryDetails(orderEntryData, sortInvoice, subOrder);
							if (null == orderEntryData)
							{
								continue;
							}
							LOG.debug("Step7-************************Order History: post fetching and populating order entry details "
									+ orderHistoryData.getCode());
							//setting cancel product for BOGO

							cancelProduct = cancelReturnFacade.associatedEntriesData(orderModelService.getOrder(subOrder.getCode()),
									orderEntryData.getOrderLineId());
							currentProductMap.put(subOrder.getCode() + orderEntryData.getOrderLineId(), cancelProduct);
						}
						subOrderDetailsList.add(subOrder);
					}
					formattedOrderDate = getFormattedDate(orderDetails.getCreated());
					orderFormattedDateMap.put(orderDetails.getCode(), formattedOrderDate);
					orderDataList.add(orderDetails);
				}
				LOG.debug("Step16-************************Order History: Finished:");

			}
			else
			{
				LOG.debug(" Orders History >> Order model is null or empty for customer " + customerModel.getOriginalUid());
			}


			final String showOrdersFrom = configurationService.getConfiguration().getString(MessageConstants.SHOW_ORDERS_FROM);
			final ReturnRequestForm returnRequestForm = new ReturnRequestForm();
			final List<CancellationReasonModel> cancellationReason = getMplOrderFacade().getCancellationReason();
			model.addAttribute(ModelAttributetConstants.SHOW_ORDERS_FROM, showOrdersFrom);
			model.addAttribute(ModelAttributetConstants.ORDER_HISTORY_PAGESIZE, pageSizeInoh);
			model.addAttribute(ModelAttributetConstants.RETURN_REQUEST_FORM, returnRequestForm);
			model.addAttribute(ModelAttributetConstants.ORDER_DATA_LIST, orderDataList);
			model.addAttribute(ModelAttributetConstants.SUB_ORDER_LIST, subOrderDetailsList);
			model.addAttribute(ModelAttributetConstants.ORDER_DATA_MAP, orderFormattedDateMap);
			model.addAttribute(ModelAttributetConstants.CANCELLATION_REASON, cancellationReason);
			model.addAttribute(ModelAttributetConstants.CANCEL_PRODUCT_MAP, currentProductMap);
			// TISPRO-48 - added page index and page size attribute for pagination
			model.addAttribute(ModelAttributetConstants.PAGE_INDEX, page);
			model.addAttribute(ModelAttributetConstants.PAGE_SIZE, pageSize);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
				accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_ORDERHISTORY));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	/**
	 * @param fmtDate
	 * @return String
	 */
	private String getFormattedDate(final Date fmtDate)
	{
		//final Date createdDate = orderDetails.getCreated();
		String finalOrderDate = ModelAttributetConstants.EMPTY;
		if (fmtDate != null)
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(fmtDate);
			final int year = cal.get(Calendar.YEAR);
			final int month = cal.get(Calendar.MONTH);
			final int day = cal.get(Calendar.DAY_OF_MONTH);

			final String strMonth = getMonthFromInt(month);
			String daySuffix = ModelAttributetConstants.SUFFIX_TH;
			if (String.valueOf(day).endsWith(ModelAttributetConstants.NUM_1))
			{
				if (!String.valueOf(day).equals(ModelAttributetConstants.NUM_11))
				{
					daySuffix = ModelAttributetConstants.SUFFIX_ST;
				}
			}
			else if (String.valueOf(day).endsWith(ModelAttributetConstants.NUM_2))
			{
				if (!String.valueOf(day).equals(ModelAttributetConstants.NUM_12))
				{
					daySuffix = ModelAttributetConstants.SUFFIX_ND;
				}
			}
			else if (String.valueOf(day).endsWith(ModelAttributetConstants.NUM_3))
			{
				if (!String.valueOf(day).equals(ModelAttributetConstants.NUM_13))
				{
					daySuffix = ModelAttributetConstants.SUFFIX_RD;
				}
			}
			else
			{
				daySuffix = ModelAttributetConstants.SUFFIX_TH;
			}

			//No need to call String.valueOf to append to a string.
			//			finalOrderDate = strMonth + ModelAttributetConstants.SINGLE_SPACE + String.valueOf(day) + daySuffix
			//			+ ModelAttributetConstants.COMMA + ModelAttributetConstants.SINGLE_SPACE + String.valueOf(year);

			finalOrderDate = strMonth + ModelAttributetConstants.SINGLE_SPACE + day + daySuffix + ModelAttributetConstants.COMMA
					+ ModelAttributetConstants.SINGLE_SPACE + year;
		}
		return finalOrderDate;
	}

	/**
	 * @param month
	 * @return String
	 */
	private String getMonthFromInt(final int month)
	{
		final List<String> months = Arrays.asList(ModelAttributetConstants.JANUARY, ModelAttributetConstants.FEBRUARY,
				ModelAttributetConstants.MARCH, ModelAttributetConstants.APRIL, ModelAttributetConstants.MAY,
				ModelAttributetConstants.JUNE, ModelAttributetConstants.JULY, ModelAttributetConstants.AUGUST,
				ModelAttributetConstants.SEPTEMBER, ModelAttributetConstants.OCTOBER, ModelAttributetConstants.NOVEMBER,
				ModelAttributetConstants.DECEMBER);
		final String strMonth = months.get(month);
		return strMonth;
	}

	/**
	 * @description method is called to get the order details page
	 * @param orderCode
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ORDER, method = RequestMethod.GET)
	@RequireHardLogIn
	public String order(@RequestParam(value = ModelAttributetConstants.ORDERCODE, required = false) final String orderCode,
			final Model model) throws CMSItemNotFoundException
	{
		if (null == orderCode)
		{
			return REDIRECT_PREFIX + RequestMappingUrlConstants.LINK_404;
		}
		final ReturnRequestForm returnRequestForm = new ReturnRequestForm();
		final Map<String, Map<String, List<AWBResponseData>>> trackStatusMap = new HashMap<>();
		final Map<String, String> currentStatusMap = new HashMap<>();
		String consignmentStatus = ModelAttributetConstants.EMPTY, formattedProductDate = ModelAttributetConstants.EMPTY, formattedActualProductDate = ModelAttributetConstants.EMPTY;

		final Map<String, String> formattedDeliveryDates = new HashMap<>();
		final Map<String, String> formattedActualDeliveryDates = new HashMap<>();
		final Map<String, String> trackStatusAWBMap = new HashMap<>();
		final Map<String, String> trackStatusLogisticMap = new HashMap<>();
		final Map<String, String> trackStatusReturnAWBMap = new HashMap<>();
		final Map<String, String> trackStatusReturnLogisticMap = new HashMap<>();
		final Map<String, String> trackStatusTrackingURLMap = new HashMap<>();

		final Map<String, Boolean> sortInvoice = new HashMap<>();
		ConsignmentModel consignmentModel = null;
		Map<String, List<AWBResponseData>> statusTrackMap = new HashMap<>();
		final Map<String, List<OrderEntryData>> currentProductMap = new HashMap<>();
		//TISEE-6290
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		List<OrderEntryData> cancelProduct = new ArrayList<>();
		try
		{
			final OrderData orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			final String finalOrderDate = getFormattedDate(orderDetail.getCreated());
			final List<OrderData> subOrderList = orderDetail.getSellerOrderList();

			for (final OrderData subOrder : subOrderList)
			{
				for (final OrderEntryData orderEntry : subOrder.getEntries())
				{
					//getting the product code
					final ProductModel productModel = getMplOrderFacade().getProductForCode(orderEntry.getProduct().getCode());
					if (CollectionUtils.isNotEmpty(productModel.getBrands()))
					{
						for (final BrandModel brand : productModel.getBrands())
						{
							orderEntry.setBrandName(brand.getName());
							break;
						}
					}

					//Fetching invoice from consignment entries
					if (null != orderEntry.getConsignment() && orderEntry.getConsignment().getStatus() != null)
					{
						consignmentModel = mplOrderService.fetchConsignment(orderEntry.getConsignment().getCode());
						//TISEE-1067
						consignmentStatus = orderEntry.getConsignment().getStatus().getCode();
						if (null != consignmentModel.getInvoice() && null != consignmentModel.getInvoice().getInvoiceUrl()
								&& consignmentStatus.equalsIgnoreCase(ModelAttributetConstants.DELIVERED))
						{
							sortInvoice.put(orderEntry.getTransactionId(), true);
							final String tranSactionId = orderEntry.getTransactionId();
							if (sortInvoice.containsKey(tranSactionId))
							{
								orderEntry.setShowInvoiceStatus(sortInvoice.get(tranSactionId));
							}
						}
					}

					final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
							.getSellerInformationRelator();

					for (final SellerInformationModel sellerInformationModel : sellerInfo)
					{
						if (sellerInformationModel.getSellerArticleSKU().equals(orderEntry.getSelectedUssid()))
						{
							final List<RichAttributeModel> richAttributeModelForSeller = (List<RichAttributeModel>) sellerInformationModel
									.getRichAttribute();
							final SellerInformationData sellerInfoData = new SellerInformationData();
							sellerInfoData.setSellername(sellerInformationModel.getSellerName());
							sellerInfoData.setUssid(sellerInformationModel.getSellerArticleSKU());
							orderEntry.setSelectedSellerInformation(sellerInfoData);
							//Code to check cancel status of an item
							if (richAttributeModelForSeller != null)
							{
								//TISEE-5389 - Cancellation window check while initiating a cancel request need not be performed.
								//Commenting cancellation window check, as not all seller will set this value. If it is not set,
								//the default value would be '0' and as per the below check cancellation link won't shown at all.
								/*
								 * if (Integer.parseInt(richAttributeModelForSeller.get(0).getCancellationWindow()) == 0) {
								 * orderEntry.setItemCancellationStatus(false); } else
								 */
								//TISEE-6419
								if (!mplOrderFacade.isChildCancelleable(subOrder, orderEntry.getTransactionId()))
								{
									orderEntry.setItemCancellationStatus(false);
								}
								else if (null == orderEntry.getConsignment() && orderEntry.getQuantity() != 0)
								{
									if (subOrder.getStatus() != null)
									{
										consignmentStatus = subOrder.getStatus().getCode();

										LOG.debug(" order: Consignemnt is null or empty : Order code :" + orderCode
												+ MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);

										if (getMplOrderFacade().checkCancelStatus(subOrder.getStatus().getCode(),
												MessageConstants.CANCEL_ORDER_STATUS))
										{
											LOG.debug(" order: Consignemnt is null or empty : Setting cancel status to true for  Order code :"
													+ orderCode + MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);
											orderEntry.setItemCancellationStatus(true);
										}
									}
								}
								else if (null != orderEntry.getConsignment() && null != orderEntry.getConsignment().getStatus())
								{
									consignmentStatus = orderEntry.getConsignment().getStatus().getCode();

									LOG.debug(" order :Inside Consignemnt is present : for  Order code :" + orderCode
											+ MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);

									if (getMplOrderFacade().checkCancelStatus(consignmentStatus, MessageConstants.CANCEL_STATUS))
									{
										orderEntry.setItemCancellationStatus(true);
										LOG.debug(" order :Inside Consignemnt is present : Setting cancel status to true for  Order code :"
												+ orderCode + MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);
									}


								}

								//Code to check return status of an item
								if (Integer.parseInt(richAttributeModelForSeller.get(0).getReturnWindow()) == 0)
								{
									orderEntry.setItemReturnStatus(false);
								}
								else
								{
									if (null != orderEntry.getConsignment() && null != orderEntry.getConsignment().getStatus())
									{
										consignmentStatus = orderEntry.getConsignment().getStatus().getCode();
										if (consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED)
												&& null != consignmentModel)
										{
											final Date sDate = new Date();
											final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(
													consignmentModel.getDeliveryDate(), sDate);
											final int actualReturnWindow = Integer.parseInt(richAttributeModelForSeller.get(0)
													.getReturnWindow());

											LOG.debug(" order : Setting Item Retrun status to true for  Order code :" + orderCode
													+ MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);

											if (returnWindow <= actualReturnWindow)
											{
												orderEntry.setItemReturnStatus(true);
											}
											else
											{
												LOG.debug(" order : Setting Item Retrun status to false for  Order code :" + orderCode
														+ MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);
												orderEntry.setItemReturnStatus(false);
											}
										}
										else
										{
											LOG.debug(" order : Setting Item Retrun status to false for  Order code :" + orderCode
													+ MarketplacecommerceservicesConstants.CONSIGNMENT_STATUS + consignmentStatus);
											orderEntry.setItemReturnStatus(false);
										}
									}
									else
									{
										LOG.debug(" order : Consignment is null or empty :Setting Item Retrun status to false for  Order code :"
												+ orderCode);
										orderEntry.setItemReturnStatus(false);
									}
								}
							}
							statusTrackMap = getOrderDetailsFacade.getOrderStatusTrack(orderEntry, subOrder, orderDetail);
							trackStatusMap.put(orderEntry.getOrderLineId(), statusTrackMap);
							currentStatusMap.put(orderEntry.getOrderLineId(), consignmentStatus);
							if (consignmentModel != null)
							{
								formattedProductDate = getFormattedDate(consignmentModel.getEstimatedDelivery());
								formattedDeliveryDates.put(orderEntry.getOrderLineId(), formattedProductDate);
								formattedActualProductDate = getFormattedDate(consignmentModel.getDeliveryDate());
								formattedActualDeliveryDates.put(orderEntry.getOrderLineId(), formattedActualProductDate);

								trackStatusAWBMap.put(orderEntry.getOrderLineId(), consignmentModel.getTrackingID());
								trackStatusLogisticMap.put(orderEntry.getOrderLineId(), consignmentModel.getCarrier());
								trackStatusReturnAWBMap.put(orderEntry.getOrderLineId(), consignmentModel.getReturnAWBNum());
								trackStatusReturnLogisticMap.put(orderEntry.getOrderLineId(), consignmentModel.getReturnCarrier());
								trackStatusTrackingURLMap.put(orderEntry.getOrderLineId(), consignmentModel.getTrackingURL());
							}

						}
					}

					//setting cancel product for BOGO
					cancelProduct = cancelReturnFacade.associatedEntriesData(orderModelService.getOrder(subOrder.getCode()),
							orderEntry.getOrderLineId());
					currentProductMap.put(orderEntry.getOrderLineId(), cancelProduct);

				}
			}

			////TISEE-6290
			fullfillmentDataMap = mplCartFacade.getOrderEntryFullfillmentMode(orderDetail);
			model.addAttribute(ModelAttributetConstants.CART_FULFILMENTDATA, fullfillmentDataMap);

			model.addAttribute(ModelAttributetConstants.TRACK_STATUS, trackStatusMap);
			model.addAttribute(ModelAttributetConstants.CURRENT_STATUS, currentStatusMap);
			model.addAttribute(ModelAttributetConstants.ORDER_DELIVERY_DATE, formattedDeliveryDates);
			model.addAttribute(ModelAttributetConstants.ORDER_DELIVERY_DATE_ACTUAL, formattedActualDeliveryDates);
			model.addAttribute(ModelAttributetConstants.CANCEL_PRODUCT_MAP, currentProductMap);

			final List<CancellationReasonModel> cancellationReason = getMplOrderFacade().getCancellationReason();
			model.addAttribute(ModelAttributetConstants.SUB_ORDER, orderDetail);
			model.addAttribute(ModelAttributetConstants.ORDER_DATE_FORMATED, finalOrderDate);
			model.addAttribute(ModelAttributetConstants.RETURN_REQUEST_FORM, returnRequestForm);
			model.addAttribute(ModelAttributetConstants.CANCELLATION_REASON, cancellationReason);

			model.addAttribute(ModelAttributetConstants.AWBNUM, trackStatusAWBMap);
			model.addAttribute(ModelAttributetConstants.LOGISCTIC, trackStatusLogisticMap);
			model.addAttribute(ModelAttributetConstants.RETURN_AWBNUM, trackStatusReturnAWBMap);
			model.addAttribute(ModelAttributetConstants.RETURN_LOGISCTIC, trackStatusReturnLogisticMap);
			model.addAttribute(ModelAttributetConstants.TRACKINGURL, trackStatusTrackingURLMap);

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb(RequestMappingUrlConstants.LINK_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS,
					getMessageSource().getMessage(MessageConstants.TEXT_ACCOUNT_ORDERHISTORY, null,
							getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb(ModelAttributetConstants.HASH_VAL, getMessageSource().getMessage(
					MessageConstants.TEXT_ACCOUNT_ORDER_ORDERBREADCRUMB, new Object[]
					{ orderDetail.getCode() }, ModelAttributetConstants.ORDER_NUMBER_SYNTAX, getI18nService().getCurrentLocale()),
					null));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS, breadcrumbs);

		}
		catch (final IllegalArgumentException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final NoSuchMessageException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final UnknownIdentifierException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		return getViewForPage(model);
	}



	/**
	 *
	 * @description This method returns the account management coupon details page along with offers & discounts with
	 *              coupon codes, transaction history and a user guide on how coupons are redeemed
	 * @param model
	 * @param page
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws VoucherOperationException
	 * @throws NullPointerException
	 */


	@SuppressWarnings("boxing")
	@RequestMapping(value = RequestMappingUrlConstants.LINK_COUPONS, method = RequestMethod.GET)
	@RequireHardLogIn
	public String getCoupons(
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = ModelAttributetConstants.ONE_VAL_COUPONS) final int page,
			@RequestParam(value = ModelAttributetConstants.PAGE_FOR, defaultValue = "") final String pageFor, final Model model)
			throws CMSItemNotFoundException, VoucherOperationException
	{
		try
		{
			/* for getting the logged in user */
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();

			/* getting all voucher in a list */

			//final List<VoucherModel> voucherList = mplCouponFacade.getAllCoupons();
			final List<VoucherDisplayData> closedVoucherDataList = mplCouponFacade.getAllClosedCoupons(customer);
			//List<VoucherDisplayData> openVoucherDataList = new ArrayList<VoucherDisplayData>();
			//List<VoucherDisplayData> closedVoucherDataList = new ArrayList<VoucherDisplayData>();
			List<CouponHistoryData> couponHistoryDTOListModified = new ArrayList<CouponHistoryData>();
			List<VoucherDisplayData> closedVoucherListModified = new ArrayList<VoucherDisplayData>();
			List<CouponHistoryData> couponHistoryDTOList = new ArrayList<CouponHistoryData>();
			CouponHistoryStoreDTO couponHistoryStoreDTO = new CouponHistoryStoreDTO();
			final double pageSize = getSiteConfigService().getInt(MessageConstants.PAZE_SIZE_COUPONS, 20);

			/* initializing the indexes for pagination */
			int start = 0;
			int end = 0;
			int couponhistoryListSize = 0;
			int closedVoucherDataListSize = 0;
			int startIndex = 0;
			int endIndex = 0;
			int pageMultMaxSize = 0;

			/* setting voucher list data */
			/*
			 * final AllVoucherListData allVoucherListData = mplCouponFacade.getAllVoucherList(customer, voucherList); if
			 * (null != allVoucherListData)
			 * 
			 * all type of voucher is shown in open voucher and personalized vouchers are shown as closed voucher
			 * 
			 * { openVoucherDataList = allVoucherListData.getOpenVoucherList(); closedVoucherDataList =
			 * allVoucherListData.getClosedVoucherList(); }
			 */

			/* getting all voucher transactions along with the order placed in a DTO */
			couponHistoryStoreDTO = mplCouponFacade.getCouponTransactions(customer);

			if (null != couponHistoryStoreDTO)
			{
				couponHistoryDTOList = couponHistoryStoreDTO.getCouponHistoryDTOList();
			}

			// usedCoupons
			if (!couponHistoryDTOList.isEmpty())
			{
				couponhistoryListSize = couponHistoryDTOList.size();
				/* Pagination starts */
				LOG.debug("Step 0-************************Pagination Starts********************");
				LOG.debug("Step 1-************************Inside couponHistoryDTOList NOT EMPTY");

				final double pages = Math.ceil(couponhistoryListSize / pageSize);
				final int totalPages = (int) pages;
				model.addAttribute(ModelAttributetConstants.TOTAL_PAGES_COUPONS, Integer.valueOf(totalPages));
				model.addAttribute(ModelAttributetConstants.COUPONS_LIST_SIZE, Integer.valueOf(couponhistoryListSize));
				if (page != 0)
				{
					start = (int) ((page - 1) * pageSize);
					end = (int) (start + pageSize);
				}
				else
				{
					start = 1;
					end = (int) (start + pageSize);
				}

				if (start > couponhistoryListSize)
				{
					start = 1;
					end = (int) (start + pageSize);
				}

				if (end > couponhistoryListSize)
				{
					LOG.debug("Step 2-************************Inside end > couponhistoryListSize NOT EMPTY");
					couponHistoryDTOListModified = couponHistoryDTOList.subList(start, couponhistoryListSize);
				}
				else
				{
					LOG.debug("Step 3-************************Inside couponhistoryListSize > end NOT EMPTY");
					couponHistoryDTOListModified = couponHistoryDTOList.subList(start, end);
				}
				if (page > 1)
				{
					pageMultMaxSize = (page * (int) pageSize);
					startIndex = ((page - 1) * (int) pageSize) + 1;

					if (startIndex == couponhistoryListSize)
					{
						endIndex = ((page - 1) * (int) pageSize) + couponhistoryListSize - startIndex + 1;
					}
					else if (couponhistoryListSize > pageMultMaxSize)
					{
						endIndex = pageMultMaxSize;
					}
					else
					{

						endIndex = couponhistoryListSize;
					}
				}
				else
				{
					if (couponhistoryListSize > pageSize)
					{
						LOG.debug("Step 3-************************Inside couponhistoryListSize > pageSize NOT EMPTY and page < 1 ");
						startIndex = 1;
						endIndex = (int) pageSize;
					}
					else
					{
						startIndex = 1;
						endIndex = couponhistoryListSize;
					}
				}
			}

			//unused coupon

			if (!closedVoucherDataList.isEmpty())
			{
				closedVoucherDataListSize = closedVoucherDataList.size();
				/* Pagination starts */
				LOG.debug("Step 0-************************Pagination Starts********************");
				LOG.debug("Step 1-************************Inside couponHistoryDTOList NOT EMPTY");

				final double pages = Math.ceil(closedVoucherDataListSize / pageSize);
				final int totalPages = (int) pages;
				//change
				model.addAttribute(ModelAttributetConstants.TOTAL_PAGES_COUPONS, Integer.valueOf(totalPages));
				model.addAttribute(ModelAttributetConstants.COUPONS_LIST_SIZE, Integer.valueOf(closedVoucherDataListSize));
				if (page != 0)
				{
					start = (int) ((page - 1) * pageSize);
					end = (int) (start + pageSize);
				}
				else
				{
					start = 1;
					end = (int) (start + pageSize);
				}

				if (start > couponhistoryListSize)
				{
					start = 1;
					end = (int) (start + pageSize);
				}

				if (end > couponhistoryListSize)
				{
					LOG.debug("Step 2-************************Inside end > couponhistoryListSize NOT EMPTY");
					closedVoucherListModified = closedVoucherDataList.subList(start, closedVoucherDataListSize);
				}
				else
				{
					LOG.debug("Step 3-************************Inside couponhistoryListSize > end NOT EMPTY");
					closedVoucherListModified = closedVoucherDataList.subList(start, end);
				}
				if (page > 1)
				{
					pageMultMaxSize = (page * (int) pageSize);
					startIndex = ((page - 1) * (int) pageSize) + 1;

					if (startIndex == closedVoucherDataListSize)
					{
						endIndex = ((page - 1) * (int) pageSize) + closedVoucherDataListSize - startIndex + 1;
					}
					else if (closedVoucherDataListSize > pageMultMaxSize)
					{
						endIndex = pageMultMaxSize;
					}
					else
					{

						endIndex = closedVoucherDataListSize;
					}
				}
				else
				{
					if (closedVoucherDataListSize > pageSize)
					{
						LOG.debug("Step 3-************************Inside couponhistoryListSize > pageSize NOT EMPTY and page < 1 ");
						startIndex = 1;
						endIndex = (int) pageSize;
					}
					else
					{
						startIndex = 1;
						endIndex = closedVoucherDataListSize;
					}
				}
			}



			//model.addAttribute(ModelAttributetConstants.OPEN_VOUCHER_DISPLAY_LIST, openVoucherDataList);
			//model.addAttribute(ModelAttributetConstants.CLOSED_VOUCHER_DISPLAY_LIST, closedVoucherDataList);
			model.addAttribute(ModelAttributetConstants.CLOSED_COUPON_LIST, closedVoucherListModified);
			model.addAttribute("closedCouponListSize", closedVoucherDataListSize);
			model.addAttribute(ModelAttributetConstants.COUPON_ORDER_DATA_DTO_LIST, couponHistoryDTOListModified);
			model.addAttribute(ModelAttributetConstants.TOTAL_SAVED_SUM, couponHistoryStoreDTO.getSavedSum());
			model.addAttribute(ModelAttributetConstants.COUPONS_REDEEMED_COUNT, couponHistoryStoreDTO.getCouponsRedeemedCount());
			model.addAttribute(ModelAttributetConstants.START_INDEX_COUPONS, Integer.valueOf(startIndex));
			model.addAttribute(ModelAttributetConstants.END_INDEX_COUPONS, Integer.valueOf(endIndex));


			storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_COUPONS));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_COUPONS));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_COUPONDETAILS));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return ControllerConstants.Views.Pages.Account.AccountCouponsPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * This method returns the return/refund request page
	 *
	 * @description Order Return reason
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ORDER_RETURN_REASON, method = RequestMethod.GET)
	@RequireHardLogIn
	public String returnRequest(@RequestParam(ModelAttributetConstants.ORDERCODE) final String orderCode,
			@RequestParam(ModelAttributetConstants.USSID) final String ussid,
			@RequestParam(ModelAttributetConstants.TRANSACTION_ID) final String transactionId, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			//OrderEntryData subOrderEntry = new OrderEntryData();
			OrderEntryData orderEntry = new OrderEntryData();
			List<OrderEntryData> returnOrderEntry = new ArrayList<OrderEntryData>();
			final Map<String, List<OrderEntryData>> returnProductMap = new HashMap<>();
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			model.addAttribute(ModelAttributetConstants.ORDERCODE, orderCode);
			final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();
			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId))
				{
					orderEntry = entry;
					returnOrderEntry = cancelReturnFacade.associatedEntriesData(orderModelService.getOrder(orderCode), transactionId);
					returnProductMap.put(orderEntry.getTransactionId(), returnOrderEntry);
					break;
				}

				boolean returnLogisticsAvailability = false;
				//TISEE-5557
				if (!(entry.isGiveAway() || entry.isIsBOGOapplied()))
				//if (!(entry.isGiveAway() || entry.isIsBOGOapplied()
				//		|| (null != entry.getAssociatedItems() && !entry.getAssociatedItems().isEmpty())))
				{
					returnLogisticsAvailability = true;
				}
				model.addAttribute(ModelAttributetConstants.RETURNLOGAVAIL, returnLogisticsAvailability);

			}
			model.addAttribute(ModelAttributetConstants.SUBORDER_ENTRY, orderEntry);
			model.addAttribute(ModelAttributetConstants.RETURN_PRODUCT_MAP, returnProductMap);
			model.addAttribute(ModelAttributetConstants.SUBORDER, subOrderDetails);
			model.addAttribute(ModelAttributetConstants.USSID, ussid);

			final ReturnRequestForm returnRequestForm = new ReturnRequestForm();
			final List<ReturnReasonData> reasonDataList = getMplOrderFacade().getReturnReasonForOrderItem();

			model.addAttribute(ModelAttributetConstants.REASON_DATA_LIST, reasonDataList);
			model.addAttribute(ModelAttributetConstants.RETURN_REQUEST_FORM, returnRequestForm);
			storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_REQUEST));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_REQUEST));

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb(RequestMappingUrlConstants.LINK_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS,
					getMessageSource().getMessage(MessageConstants.TEXT_ACCOUNT_ORDERHISTORY, null,
							getI18nService().getCurrentLocale()), null));

			breadcrumbs.add(new Breadcrumb(ModelAttributetConstants.HASH_VAL, getMessageSource().getMessage(
					MessageConstants.TEXT_ACCOUNT_ORDER_ORDERBREADCRUMB, new Object[]
					{ orderCode }, ModelAttributetConstants.ORDER_NUMBER_SYNTAX, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb(null, getMessageSource().getMessage(MessageConstants.RETURN_REQUEST_LOCALE, null,
					getI18nService().getCurrentLocale()), null));

			model.addAttribute(ModelAttributetConstants.BREADCRUMBS, breadcrumbs);

			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			storeContentPageTitleInModel(model, MessageConstants.RETURN_REQUEST);
			return ControllerConstants.Views.Pages.Account.AccountReturnReqPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}




	/**
	 * This method returns the return/refund submit page
	 *
	 * @param request
	 * @param returnRequestForm
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_RETURN_REQUEST, method = RequestMethod.POST)
	@RequireHardLogIn
	public String returnSubmit(final HttpServletRequest request, final ReturnRequestForm returnRequestForm, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final String reasonCode = returnRequestForm.getReasonCode();
			model.addAttribute(ModelAttributetConstants.REASON_CODE, reasonCode);

			final List<ReturnReasonData> reasonDataList = getMplOrderFacade().getReturnReasonForOrderItem();
			if (!reasonDataList.isEmpty())
			{
				for (final ReturnReasonData reason : reasonDataList)
				{
					if (null != reason.getCode() && reason.getCode().equalsIgnoreCase(reasonCode))
					{
						model.addAttribute(ModelAttributetConstants.REASON_DESCRIPTION, reason.getReasonDescription());
					}
				}
			}

			model.addAttribute(ModelAttributetConstants.REASON_DATA_LIST, reasonDataList);
			model.addAttribute(ModelAttributetConstants.RETURN_REQUEST_FORM, returnRequestForm);

			final String orderCode = returnRequestForm.getOrderCode();
			final String ussid = returnRequestForm.getUssid();
			final String transactionId = returnRequestForm.getTransactionId();

			OrderEntryData subOrderEntry = new OrderEntryData();
			List<OrderEntryData> returnSubOrderEntry = new ArrayList<>();
			final Map<String, List<OrderEntryData>> returnProductMap = new HashMap<>();
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			model.addAttribute(ModelAttributetConstants.ORDERCODE, orderCode);
			final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();
			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId))
				{
					subOrderEntry = entry;
					returnSubOrderEntry = cancelReturnFacade.associatedEntriesData(orderModelService.getOrder(orderCode),
							transactionId);
					returnProductMap.put(entry.getTransactionId(), returnSubOrderEntry);
					break;
				}

			}

			//getting the product code
			final ProductModel productModel = getMplOrderFacade().getProductForCode(subOrderEntry.getProduct().getCode());
			for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
			{
				if (sellerInfo.getSellerArticleSKU().equalsIgnoreCase(ussid))
				{
					model.addAttribute(ModelAttributetConstants.SELLERID, sellerInfo.getSellerID());
				}
			}

			//getting the returnLogisticsAvailability
			boolean returnLogisticsAvailability = false;
			//TISEE-5557
			if (!(subOrderEntry.isGiveAway() || subOrderEntry.isIsBOGOapplied()))
			//if (!(subOrderEntry.isGiveAway() || subOrderEntry.isIsBOGOapplied()
			//		|| (null != subOrderEntry.getAssociatedItems() && !subOrderEntry
			//		.getAssociatedItems().isEmpty())))
			{
				returnLogisticsAvailability = true;
			}
			model.addAttribute(ModelAttributetConstants.RETURNLOGAVAIL, returnLogisticsAvailability);

			boolean returnLogisticsCheck = true;
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = cancelReturnFacade
					.checkReturnLogistics(subOrderDetails);
			for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
			{
				model.addAttribute(ModelAttributetConstants.RETURNLOGMSG, response.getResponseMessage());
				if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase(ModelAttributetConstants.N_CAPS_VAL))
				{
					returnLogisticsCheck = false;
				}
			}
			model.addAttribute(ModelAttributetConstants.RETURNLOGCHECK, returnLogisticsCheck);

			model.addAttribute(ModelAttributetConstants.SUBORDER_ENTRY, subOrderEntry);
			model.addAttribute(ModelAttributetConstants.RETURN_PRODUCT_MAP, returnProductMap);
			model.addAttribute(ModelAttributetConstants.SUBORDER, subOrderDetails);
			model.addAttribute(ModelAttributetConstants.USSID, ussid);

			storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_SUBMIT));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_SUBMIT));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.RETURN_SUBMIT));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			storeContentPageTitleInModel(model, MessageConstants.RETURN_SUBMIT);
			return ControllerConstants.Views.Pages.Account.AccountReturnSubPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * This method returns the return/refund success page
	 *
	 * @param request
	 * @param returnRequestForm
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ORDER_RETURN_SUCCESS, method = RequestMethod.POST)
	@RequireHardLogIn
	public String returnSuccess(final HttpServletRequest request, final ReturnRequestForm returnRequestForm, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			OrderEntryData subOrderEntry = new OrderEntryData();
			Boolean returnLogisticsCheck = Boolean.TRUE;
			List<OrderEntryData> returnSubOrderEntry = new ArrayList<>();
			final Map<String, List<OrderEntryData>> returnProductMap = new HashMap<>();
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(returnRequestForm.getOrderCode());
			final String reasonCode = returnRequestForm.getReasonCode();
			final String ticketTypeCode = returnRequestForm.getTicketTypeCode();
			final String ussid = returnRequestForm.getUssid();
			final String refundType = returnRequestForm.getRefundType();
			final String transactionId = returnRequestForm.getTransactionId();

			final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();
			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId))
				{
					subOrderEntry = entry;
					returnSubOrderEntry = cancelReturnFacade.associatedEntriesData(
							orderModelService.getOrder(returnRequestForm.getOrderCode()), transactionId);
					returnProductMap.put(entry.getTransactionId(), returnSubOrderEntry);
					break;
				}
			}

			final CustomerData customerData = customerFacade.getCurrentCustomer();

			model.addAttribute(ModelAttributetConstants.ORDERCODE, returnRequestForm.getOrderCode());

			//getting the product code
			final ProductModel productModel = getMplOrderFacade().getProductForCode(subOrderEntry.getProduct().getCode());
			for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
			{
				if (sellerInfo.getSellerArticleSKU().equalsIgnoreCase(ussid))
				{
					model.addAttribute(ModelAttributetConstants.SELLERID, sellerInfo.getSellerID());
				}
			}

			final boolean cancellationStatus = cancelReturnFacade.implementCancelOrReturn(subOrderDetails, subOrderEntry,
					reasonCode, ussid, ticketTypeCode, customerData, refundType, true, SalesApplication.WEB);


			if (!cancellationStatus)
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						ModelAttributetConstants.RETURN_ERRORMSG);
				return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
			}

			model.addAttribute(ModelAttributetConstants.ORDERENTRY, subOrderEntry);

			final List<ReturnReasonData> reasonDataList = getMplOrderFacade().getReturnReasonForOrderItem();
			if (!reasonDataList.isEmpty())
			{
				for (final ReturnReasonData reason : reasonDataList)
				{
					if (null != reason.getCode() && reason.getCode().equalsIgnoreCase(reasonCode))
					{
						model.addAttribute(ModelAttributetConstants.REASON_DESCRIPTION, reason.getReasonDescription());
						break;
					}
				}
			}

			final List<ReturnLogisticsResponseData> returnLogisticsRespList = cancelReturnFacade
					.checkReturnLogistics(subOrderDetails);
			for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
			{
				model.addAttribute(ModelAttributetConstants.RETURNLOGMSG, response.getResponseMessage());
				if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase(ModelAttributetConstants.N_CAPS_VAL))
				{
					returnLogisticsCheck = Boolean.FALSE;
				}
			}
			//For Bogo only CRM ticket created
			final boolean bogoFlag = returnSubOrderEntry.size() > 1 ? true : false;
			model.addAttribute(ModelAttributetConstants.RETURN_BOGO_MESSAGE, bogoFlag);
			model.addAttribute(ModelAttributetConstants.RETURNLOGCHECK, returnLogisticsCheck);
			model.addAttribute(ModelAttributetConstants.REFUNDTYPE, ticketTypeCode);
			model.addAttribute(ModelAttributetConstants.SUBORDER, subOrderDetails);
			model.addAttribute(ModelAttributetConstants.RETURN_PRODUCT_MAP, returnProductMap);
			storeCmsPageInModel(model, getContentPageForLabelOrId(RETURN_SUCCESS));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(RETURN_SUCCESS));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS, resourceBreadcrumbBuilder.getBreadcrumbs("Return Success"));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			storeContentPageTitleInModel(model, MessageConstants.RETURN_SUBMIT);
			return ControllerConstants.Views.Pages.Account.AccountReturnSuccessPage;

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}




	/**
	 * This method is called when the customer clicks on Cancel submit button and returns the cancellation reason
	 *
	 * @param orderCode
	 * @param reasonCode
	 * @param ticketTypeCode
	 * @param ussid
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ORDER_CANCEL_SUCCESS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String cancelSuccess(final String orderCode, @SuppressWarnings(UNUSED) final String transactionId,
			final String reasonCode, final String ticketTypeCode, final String ussid, final Model model)
			throws CMSItemNotFoundException
	{
		try

		{
			String result = null, reasonDesc = null;
			boolean cancellationStatus = false;
			final String refundType = "S";
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			OrderEntryData subOrderEntry = null;
			for (final OrderEntryData orderEntry : subOrderDetails.getEntries())
			{
				if (transactionId.equalsIgnoreCase(orderEntry.getTransactionId()))
				{
					subOrderEntry = orderEntry;
				}
			}
			cancellationStatus = cancelReturnFacade.implementCancelOrReturn(subOrderDetails, subOrderEntry, reasonCode, ussid,
					ticketTypeCode, customerData, refundType, false, SalesApplication.WEB);
			if (cancellationStatus)
			{
				result = ModelAttributetConstants.SUCCESS + ModelAttributetConstants.PIPE;

			}
			else
			{
				result = ModelAttributetConstants.FAILURE + ModelAttributetConstants.PIPE
						+ getMessageSource().getMessage(MessageConstants.BOGO_CANCEL, null, getI18nService().getCurrentLocale());
			}
			final List<CancellationReasonModel> reasonDataList = getMplOrderFacade().getCancellationReason();
			if (!reasonDataList.isEmpty())
			{
				for (final CancellationReasonModel reason : reasonDataList)
				{
					if (null != reason.getReasonCode() && reason.getReasonCode().equalsIgnoreCase(reasonCode))
					{
						reasonDesc = reason.getReasonDescription();
					}
				}
			}
			return result + ModelAttributetConstants.PIPE + reasonDesc;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}



	/**
	 * @description This method is called to send ticket for selected order code
	 * @param orderCode
	 * @param action
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ORDER, method = RequestMethod.POST)
	@RequireHardLogIn
	public String orderAction(@RequestParam(ModelAttributetConstants.ORDERCODE) final String orderCode,
			@RequestParam(ModelAttributetConstants.ACTION) final String action, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			if (!(StringUtils.isEmpty(action)) && !(StringUtils.isBlank(action)) && !(StringUtils.isEmpty(orderCode))
					&& !(StringUtils.isBlank(orderCode)))
			{
				if (!action.equals(SEND_INVOICE))
				{
					sendTicketRequestData.setOrderId(orderCode);

					if (mplSendTicketFacade.setTicket(sendTicketRequestData))
					{
						GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
								MessageConstants.TEXT_ACCOUNT_ORDER_TICKET_SUCCESS, null);
					}
					else
					{
						GlobalMessages.addErrorMessage(model, MessageConstants.TEXT_ACCOUNT_ORDER_TICKET_FAILURE);
					}
				}
				else
				{
					final SendInvoiceData invoiceData = new SendInvoiceData();
					invoiceData.setCustomerEmail(customerData.getDisplayUid());
					invoiceData.setInvoiceUrl(configurationService.getConfiguration().getString(MessageConstants.DEFAULT_INVOICE_URL));
					invoiceData.setOrdercode(orderCode);
					registerCustomerFacade.sendInvoice(invoiceData, null);

					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
							MessageConstants.TEXT_ACCOUNT_ORDER_INVOICE_SUCCESS, null);
					return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
				}
			}
			else
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.TEXT_ACCOUNT_ORDER_INVOICE_FAILURE);
			}
			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
		}
		catch (final ConversionException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description This method is called to send invoice
	 * @param orderCode
	 * @param transactionId
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_INVOICE, method = RequestMethod.GET)
	@RequireHardLogIn
	public String sendInvoiceAction(@RequestParam(ModelAttributetConstants.ORDERCODE) final String orderCode,
			@RequestParam(ModelAttributetConstants.TRANSACTION_ID) final String transactionId, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final SendInvoiceData sendInvoiceData = new SendInvoiceData();
			// invoiceFile emailAttachedment
			final OrderModel orderModel = orderModelService.getOrder(orderCode);

			if (orderModel != null && orderModel.getEntries() != null)
			{


				for (final AbstractOrderEntryModel entry : orderModel.getEntries())
				{
					if (StringUtils.isNotEmpty(entry.getTransactionID()) && entry.getTransactionID().equalsIgnoreCase(transactionId))
					{
						//Fetching invoice from consignment entries
						for (final ConsignmentEntryModel c : entry.getConsignmentEntries())
						{
							if (null != c.getConsignment().getInvoice())
							{
								final String invoicePathURL = c.getConsignment().getInvoice().getInvoiceUrl();

								//Fix for defect TISPIT-145
								if (null == invoicePathURL)
								{
									LOG.error("***************INVOICE URL is missing******************");
									throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0015);
								}
								else
								{
									sendInvoiceData.setInvoiceUrl(invoicePathURL);
								}


							}

						}
					}
				}
			}




			final CustomerData customerData = customerFacade.getCurrentCustomer();

			if (!(StringUtils.isEmpty(orderCode)) && !(StringUtils.isBlank(orderCode)) && !(StringUtils.isEmpty(transactionId))
					&& !(StringUtils.isBlank(transactionId)))

			{
				OrderEntryData subOrderEntry = new OrderEntryData();
				final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);

				final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();
				for (final OrderEntryData entry : subOrderEntries)
				{
					if (entry.getTransactionId().equalsIgnoreCase(transactionId))
					{
						subOrderEntry = entry;
						break;
					}
				}
				final String lineID = subOrderEntry.getOrderLineId();
				sendInvoiceData.setCustomerEmail(customerData.getDisplayUid());
				sendInvoiceData.setOrdercode(orderCode);
				sendInvoiceData.setTransactionId(transactionId);
				sendInvoiceData.setLineItemId(lineID);
				registerCustomerFacade.sendInvoice(sendInvoiceData, null);

				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						MessageConstants.TEXT_ACCOUNT_ORDER_INVOICE_SUCCESS, null);
				return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
			}
			else
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.TEXT_ACCOUNT_ORDER_INVOICE_FAILURE);
			}
			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ORDERS;
		}
		catch (

		final ConversionException e)

		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (

		final EtailBusinessExceptions e)

		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (

		final EtailNonBusinessExceptions e)

		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (

		final Exception e)

		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

	}

	/**
	 * @description load my profile page with all saved data (if any)
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_PROFILE, method = RequestMethod.GET)
	@RequireHardLogIn
	public String profile(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			CustomerData customerData = new CustomerData();
			List<TitleData> titles = new ArrayList<TitleData>();
			try
			{
				titles = userFacade.getTitles();
				customerData = customerFacade.getCurrentCustomer();
			}
			catch (final ConversionException e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
						MarketplacecommerceservicesConstants.E0000));
				return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			}
			catch (final Exception ex)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(ex));
				return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			}

			if (customerData.getTitleCode() != null)
			{
				model.addAttribute(ModelAttributetConstants.TITLE, findTitleForCode(titles, customerData.getTitleCode()));
			}

			final MplCustomerProfileData mplCustomerProfileData = new MplCustomerProfileData();
			mplCustomerProfileData.setDisplayUid(customerData.getDisplayUid());

			final MplCustomerProfileData customerProfileData = mplCustomerProfileFacade.getCustomerProfileDetail(customerData
					.getDisplayUid());

			if (!(ModelAttributetConstants.EMPTY).equals(customerProfileData.getMobileNumber()))
			{
				customerProfileData.setMobileNumber(MessageConstants.MOBILE_PREFIX_IN_91 + customerProfileData.getMobileNumber());
			}
			if (null != sessionService.getAttribute(ModelAttributetConstants.SOCIAL_LOGIN))
			{
				if (sessionService.getAttribute(ModelAttributetConstants.SOCIAL_LOGIN).equals(ModelAttributetConstants.SOCIAL_LOGIN))
				{
					model.addAttribute(ModelAttributetConstants.UPDATE_PWD_ALLOWANCE, ModelAttributetConstants.Y_CAPS_VAL);
				}
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.UPDATE_PWD_ALLOWANCE, ModelAttributetConstants.N_CAPS_VAL);
			}
			model.addAttribute(ModelAttributetConstants.CUSTOMER_DATA, customerData);
			model.addAttribute(ModelAttributetConstants.CUSTOMER_PROFILE_DATA, customerProfileData);
			storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description this method is fetching codes for all available titles maintained in application
	 * @param titles
	 * @param code
	 * @return TitleData
	 */
	private TitleData findTitleForCode(final List<TitleData> titles, final String code)
	{
		if (code != null && !code.isEmpty() && titles != null && !titles.isEmpty())
		{
			for (final TitleData title : titles)
			{
				if (code.equals(title.getCode()))
				{
					return title;
				}
			}
		}
		return null;
	}

	/**
	 * @description this method called to populate the page for updating email address of an registered user
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_EMAIL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editEmail(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			final UpdateEmailForm updateEmailForm = new UpdateEmailForm();

			updateEmailForm.setEmail(customerData.getDisplayUid());

			model.addAttribute(ModelAttributetConstants.UPDATE_EMAIL_FORM, updateEmailForm);

			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to edit the email id of registered customer
	 * @param updateEmailForm
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return errorUpdatingEmail(model)
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_EMAIL, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateEmail(final UpdateEmailForm updateEmailForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		mplUpdateEmailFormValidator.validate(updateEmailForm, bindingResult);
		final MplCustomerProfileData mplCustomerProfileData = new MplCustomerProfileData();
		String returnAction = REDIRECT_TO_PROFILE_PAGE;

		if (!bindingResult.hasErrors() && !updateEmailForm.getEmail().equals(updateEmailForm.getChkEmail()))
		{
			bindingResult.rejectValue(ModelAttributetConstants.CHK_EMAIL, MessageConstants.VALIDATION_CHECKEMAIL_EQUALS,
					new Object[] {}, MessageConstants.VALIDATION_CHECKEMAIL_EQUALS);
		}

		if (bindingResult.hasErrors())
		{
			returnAction = errorUpdatingEmail(model);
		}
		else
		{
			try
			{
				mplCustomerProfileFacade.changeUid(updateEmailForm.getEmail(), updateEmailForm.getPassword());
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						MessageConstants.TEXT_ACCOUNT_PROFILE_CONFIRMATION_UPDATED, null);

				final String newUid = customerFacade.getCurrentCustomer().getDisplayUid().toLowerCase();
				final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
				final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUid, null,
						oldAuthentication.getAuthorities());
				newAuthentication.setDetails(oldAuthentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
				mplCustomerProfileData.setDisplayUid(newUid);
				mplCustomerProfileFacade.sendEmailForUpdate(mplCustomerProfileData);
			}
			catch (final DuplicateUidException e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
						MarketplacecommerceservicesConstants.B0002));
				bindingResult.rejectValue(ModelAttributetConstants.EMAIL, MessageConstants.PROFILE_EMAIL_UNIQUE);
				returnAction = errorUpdatingEmail(model);
			}
			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
				return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			}
		}

		return returnAction;
	}

	/**
	 * @description This is an internal method to populate the error page for any exception in updating email address
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	private String errorUpdatingEmail(final Model model) throws CMSItemNotFoundException
	{
		GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_EMAIL_CMS_PAGE));
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
				accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
		return getViewForPage(model);
	}

	/**
	 * @description load the update profile page
	 * @param model
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_PROFILE, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editProfile(final Model model,
			@RequestParam(value = ModelAttributetConstants.PARAM, required = false) final String param)
			throws CMSItemNotFoundException
	{
		try
		{
			String dobDay = null;
			String dobMonth = null;
			String dobYear = null;
			String doaDay = null;
			String doaMonth = null;
			String doaYear = null;
			// update personmal info
			final List<GenderData> genderList = mplCustomerProfileFacade.getGenders();
			model.addAttribute(ModelAttributetConstants.GENDER_DATA, genderList);

			final List<DayData> dayList = mplCustomerProfileFacade.getDayList();
			final List<MonthData> monthList = mplCustomerProfileFacade.getMonthList();
			final List<YearData> yearList = mplCustomerProfileFacade.getYearList();
			final List<YearData> yearAnniversaryList = mplCustomerProfileFacade.getYearAnniversaryList();

			model.addAttribute(ModelAttributetConstants.DAYLIST, dayList);
			model.addAttribute(ModelAttributetConstants.MONTHLIST, monthList);
			model.addAttribute(ModelAttributetConstants.YEARLIST, yearList);
			model.addAttribute(ModelAttributetConstants.YEARANNIVERSARYLIST, yearAnniversaryList);

			final CustomerData customerData = customerFacade.getCurrentCustomer();
			final MplCustomerProfileData customerProfileData = mplCustomerProfileFacade.getCustomerProfileDetail(customerData
					.getDisplayUid());

			final MplCustomerProfileForm mplCustomerProfileForm = new MplCustomerProfileForm();
			if (null != customerProfileData && null != customerProfileData.getDateOfBirth()
					&& customerProfileData.getDateOfBirth().contains(ModelAttributetConstants.SLASH))
			{
				final String[] dob = customerProfileData.getDateOfBirth().split(ModelAttributetConstants.SPLITTER_SLASH);
				mplCustomerProfileForm.setDateOfBirthDay(dob[0].trim());
				dobDay = dob[0].trim();
				mplCustomerProfileForm.setDateOfBirthMonth(dob[1].trim());
				dobMonth = dob[1].trim();
				mplCustomerProfileForm.setDateOfBirthYear(dob[2].trim());
				dobYear = dob[2].trim();
			}
			if (null != customerProfileData && null != customerProfileData.getDateOfAnniversary()
					&& customerProfileData.getDateOfAnniversary().contains(ModelAttributetConstants.SLASH))
			{
				final String[] doa = customerProfileData.getDateOfAnniversary().split(ModelAttributetConstants.SPLITTER_SLASH);
				mplCustomerProfileForm.setDateOfAnniversaryDay(doa[0].trim());
				doaDay = doa[0].trim();
				mplCustomerProfileForm.setDateOfAnniversaryMonth(doa[1].trim());
				doaMonth = doa[1].trim();
				mplCustomerProfileForm.setDateOfAnniversaryYear(doa[2].trim());
				doaYear = doa[2].trim();
			}
			mplCustomerProfileForm.setFirstName(customerProfileData.getFirstName().trim());
			mplCustomerProfileForm.setLastName(customerProfileData.getLastName().trim());
			mplCustomerProfileForm.setNickName(customerProfileData.getNickName());
			mplCustomerProfileForm.setMobileNumber(customerProfileData.getMobileNumber());
			if (!StringUtils.isEmpty(customerProfileData.getGender()))
			{
				mplCustomerProfileForm.setGender(customerProfileData.getGender().toUpperCase());
			}

			// update email id
			mplCustomerProfileForm.setEmailId(customerData.getDisplayUid());
			model.addAttribute(ModelAttributetConstants.MPL_CUSTOMER_PROFILE_FORM, mplCustomerProfileForm);


			// update password
			final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
			model.addAttribute(ModelAttributetConstants.UPDATE_PASSWORD_FORM, updatePasswordForm);

			// Get the data before editing
			final String channel = MarketplacecommerceservicesConstants.UPDATE_CHANNEL_WEB;
			mplCustomerProfileFacade.setPreviousDataToMap(customerData.getDisplayUid(), channel);

			model.addAttribute(ModelAttributetConstants.DOB_DAY, dobDay);
			model.addAttribute(ModelAttributetConstants.DOB_MONTH, dobMonth);
			model.addAttribute(ModelAttributetConstants.DOB_YEAR, dobYear);

			model.addAttribute(ModelAttributetConstants.DOA_DAY, doaDay);
			model.addAttribute(ModelAttributetConstants.DOA_MONTH, doaMonth);
			model.addAttribute(ModelAttributetConstants.DOA_YEAR, doaYear);

			if (StringUtils.isNotEmpty(param))
			{
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.FAILURE);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.EMPTY);
			}

			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to generate update profileURL
	 * @param request
	 * @param specificUrl
	 * @throws CMSItemNotFoundException
	 */
	public String urlForEmailContext(final HttpServletRequest request, final String specificUrl) throws CMSItemNotFoundException
	{
		URL requestUrl;
		final Model model = null;
		String profileUpdateUrl = ModelAttributetConstants.EMPTY;
		try
		{
			requestUrl = new URL(request.getRequestURL().toString());
			final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
			//final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + ""; Do not add empty strings
			final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;

			final String profileUpdatePath = request.getContextPath() + specificUrl;
			profileUpdateUrl = baseUrl + profileUpdatePath;
		}
		catch (final MalformedURLException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0016));
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			return ModelAttributetConstants.FAILURE;
		}
		return profileUpdateUrl;
	}

	/**
	 * @description method is called to update profile
	 * @param mplCustomerProfileForm
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 * @throws ParseException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_PARSONAL_DETAIL, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateProfile(final MplCustomerProfileForm mplCustomerProfileForm, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, ParseException
	{
		try
		{
			final String specificUrl = RequestMappingUrlConstants.LINK_MY_ACCOUNT + RequestMappingUrlConstants.LINK_UPDATE_PROFILE;
			final String profileUpdateUrl = urlForEmailContext(request, specificUrl);

			if (isDateValidatedDOB(mplCustomerProfileForm))
			{
				mplCustomerProfileForm.setDateOfBirth(dateDOB);
			}
			else
			{
				mplCustomerProfileForm.setDateOfBirth(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (isDateValidatedDOAnn(mplCustomerProfileForm))
			{
				mplCustomerProfileForm.setDateOfAnniversary(dateDOAnn);
			}
			else
			{
				mplCustomerProfileForm.setDateOfAnniversary(MarketplacecommerceservicesConstants.EMPTY);
			}
			mplCustomerProfileFormValidator.validate(mplCustomerProfileForm, bindingResult);
			if (!StringUtils.isEmpty(mplCustomerProfileForm.getEmailId())
					&& StringUtils.length(mplCustomerProfileForm.getEmailId()) > 240
					|| !mplEmailValidator.validateEmailAddress(mplCustomerProfileForm.getEmailId())
					|| !mplEmailValidator.validDomain(mplCustomerProfileForm.getEmailId()))
			{
				bindingResult.rejectValue("emailId", "register.email.invalid");
			}
			else
			{
				mplCustomerProfileForm.setEmailId(mplCustomerProfileForm.getEmailId().toLowerCase());
			}
			String returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEditPage;

			if (bindingResult.hasErrors())
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				final List<GenderData> genderList = mplCustomerProfileFacade.getGenders();
				model.addAttribute(ModelAttributetConstants.GENDER_DATA, genderList);
				model.addAttribute(ModelAttributetConstants.MPL_CUSTOMER_PROFILE_FORM, mplCustomerProfileForm);
				final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
				model.addAttribute(ModelAttributetConstants.UPDATE_PASSWORD_FORM, updatePasswordForm);
				storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				return REDIRECT_TO_UPDATE_PROFILE_PAGE;
			}
			else
			{
				returnAction = REDIRECT_TO_UPDATE_PROFILE_PAGE;
				final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
				final MplCustomerProfileData customerProfileData = mplCustomerProfileFacade
						.getCustomerProfileDetail(currentCustomerData.getDisplayUid());

				final MplCustomerProfileData mplCustomerProfileData = new MplCustomerProfileData();
				mplCustomerProfileData.setFirstName(mplCustomerProfileForm.getFirstName().trim());
				mplCustomerProfileData.setLastName(mplCustomerProfileForm.getLastName().trim());
				mplCustomerProfileData.setNickName(customerProfileData.getNickName());
				mplCustomerProfileData.setUid(currentCustomerData.getUid());
				mplCustomerProfileData.setDisplayUid(currentCustomerData.getDisplayUid());
				mplCustomerProfileData.setMobileNumber(mplCustomerProfileForm.getMobileNumber().trim());
				mplCustomerProfileData.setDateOfAnniversary(mplCustomerProfileForm.getDateOfAnniversary().trim());
				mplCustomerProfileData.setDateOfBirth(mplCustomerProfileForm.getDateOfBirth().trim());
				mplCustomerProfileData.setEmailId(mplCustomerProfileForm.getEmailId().trim().toLowerCase());

				if (null != mplCustomerProfileForm.getGender())
				{
					mplCustomerProfileData.setGender(mplCustomerProfileForm.getGender());
				}
				model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
				String currentEmail = ModelAttributetConstants.EMPTY;
				if (StringUtils.isNotEmpty(mplCustomerProfileData.getEmailId()))
				{
					currentEmail = mplCustomerProfileData.getEmailId();
				}
				else
				{
					currentEmail = currentCustomerData.getDisplayUid();
				}
				//	Update Personal Detail
				try
				{
					final Map<String, String> preSavedDetailMap = new HashMap<String, String>();
					if (!currentCustomerData.getDisplayUid()
							.equalsIgnoreCase(mplCustomerProfileForm.getEmailId().trim().toLowerCase()))
					{
						if (mplCustomerProfileFacade.checkUniquenessOfEmail(mplCustomerProfileData.getEmailId()))
						{
							sessionService.setAttribute(ModelAttributetConstants.IS_EMAIL_UPDATED, ModelAttributetConstants.TRUE);
							mplCustomerProfileFacade.updateCustomerProfile(mplCustomerProfileData);

							mplCustomerProfileFacade.checkChangesForSendingEmail(preSavedDetailMap, currentEmail, profileUpdateUrl);
							GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
									MessageConstants.TEXT_ACCOUNT_PROFILE_CONFIRMATION_UPDATED, null);
						}
						else
						{
							GlobalMessages.addErrorMessage(model, MessageConstants.REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE);
							returnAction = REDIRECT_TO_UPDATE_PROFILE_PAGE + ModelAttributetConstants.QS
									+ ModelAttributetConstants.PARAM + ModelAttributetConstants.EQUALS + ModelAttributetConstants.FAILURE;
						}
					}
					else
					{
						mplCustomerProfileFacade.updateCustomerProfile(mplCustomerProfileData);
						mplCustomerProfileFacade.checkChangesForSendingEmail(preSavedDetailMap, currentEmail, profileUpdateUrl);
						GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
								MessageConstants.TEXT_ACCOUNT_PROFILE_CONFIRMATION_UPDATED, null);
					}
				}
				catch (final DuplicateUidException e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
							MarketplacecommerceservicesConstants.B0002));
					bindingResult
							.rejectValue(ModelAttributetConstants.EMAIL, MessageConstants.REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE);
					GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				}


				//	post success token generation of updating email id
				final String newUid = customerFacade.getCurrentCustomer().getDisplayUid().toLowerCase();
				final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
				final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUid, null,
						oldAuthentication.getAuthorities());
				newAuthentication.setDetails(oldAuthentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
				mplCustomerProfileData.setDisplayUid(newUid);
				// NOTIFY GIGYA OF THE USER PROFILE CHANGES
				final String gigyaServiceSwitch = configurationService.getConfiguration().getString(MessageConstants.USE_GIGYA);

				if (gigyaServiceSwitch != null && !gigyaServiceSwitch.equalsIgnoreCase(MessageConstants.NO))
				{
					final String gigyaMethod = configurationService.getConfiguration().getString(
							MarketplacecclientservicesConstants.GIGYA_METHOD_UPDATE_USERINFO);

					gigyaService.notifyGigya(mplCustomerProfileData.getUid(), null, mplCustomerProfileData.getFirstName().trim(),
							mplCustomerProfileData.getLastName().trim(), mplCustomerProfileData.getEmailId().trim(), gigyaMethod);

				}
			}

			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return returnAction;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

	}


	/**
	 * The method is called to check whether date for date of birth is valid or not
	 *
	 * @param mplCustomerProfileForm
	 * @return boolean
	 */
	private boolean isDateValidatedDOB(final MplCustomerProfileForm mplCustomerProfileForm)
	{
		boolean isValidated = false;
		if (null != mplCustomerProfileForm && null != mplCustomerProfileForm.getDateOfBirthDay()
				&& null != mplCustomerProfileForm.getDateOfBirthMonth() && null != mplCustomerProfileForm.getDateOfBirthYear()
				&& !(MarketplacecommerceservicesConstants.EMPTY).equals(mplCustomerProfileForm.getDateOfBirthDay())
				&& !(MarketplacecommerceservicesConstants.EMPTY).equals(mplCustomerProfileForm.getDateOfBirthMonth())
				&& !(MarketplacecommerceservicesConstants.EMPTY).equals(mplCustomerProfileForm.getDateOfBirthYear())
				&& !(ModelAttributetConstants.SELECT_DAY).equalsIgnoreCase(mplCustomerProfileForm.getDateOfBirthDay())
				&& !(ModelAttributetConstants.SELECT_MONTH).equalsIgnoreCase(mplCustomerProfileForm.getDateOfBirthMonth())
				&& !(ModelAttributetConstants.SELECT_YEAR).equalsIgnoreCase(mplCustomerProfileForm.getDateOfBirthYear()))
		{
			dateDOB = mplCustomerProfileForm.getDateOfBirthDay() + ModelAttributetConstants.SLASH
					+ mplCustomerProfileForm.getDateOfBirthMonth() + ModelAttributetConstants.SLASH
					+ mplCustomerProfileForm.getDateOfBirthYear();
			isValidated = true;
		}
		return isValidated;
	}


	/**
	 * The method is called to check whether date for date of anniversary is valid or not
	 *
	 * @param mplCustomerProfileForm
	 * @return boolean
	 */
	private boolean isDateValidatedDOAnn(final MplCustomerProfileForm mplCustomerProfileForm)
	{
		boolean isValidated = false;
		if (null != mplCustomerProfileForm && null != mplCustomerProfileForm.getDateOfAnniversaryDay()
				&& null != mplCustomerProfileForm.getDateOfAnniversaryMonth()
				&& null != mplCustomerProfileForm.getDateOfAnniversaryYear()
				&& !(MarketplacecommerceservicesConstants.EMPTY).equals(mplCustomerProfileForm.getDateOfAnniversaryDay())
				&& !(MarketplacecommerceservicesConstants.EMPTY).equals(mplCustomerProfileForm.getDateOfAnniversaryMonth())
				&& !(MarketplacecommerceservicesConstants.EMPTY).equals(mplCustomerProfileForm.getDateOfAnniversaryYear())
				&& !(ModelAttributetConstants.SELECT_DAY).equalsIgnoreCase(mplCustomerProfileForm.getDateOfAnniversaryDay())
				&& !(ModelAttributetConstants.SELECT_MONTH).equalsIgnoreCase(mplCustomerProfileForm.getDateOfAnniversaryMonth())
				&& !(ModelAttributetConstants.SELECT_YEAR).equalsIgnoreCase(mplCustomerProfileForm.getDateOfAnniversaryYear()))
		{
			dateDOAnn = mplCustomerProfileForm.getDateOfAnniversaryDay() + ModelAttributetConstants.SLASH
					+ mplCustomerProfileForm.getDateOfAnniversaryMonth() + ModelAttributetConstants.SLASH
					+ mplCustomerProfileForm.getDateOfAnniversaryYear();
			isValidated = true;
		}
		return isValidated;
	}

	/**
	 * @description method is called to load update password
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_PASSWORD, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editPassword(final Model model) throws CMSItemNotFoundException
	{
		final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
		model.addAttribute(ModelAttributetConstants.UPDATE_PASSWORD_FORM, updatePasswordForm);
		storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PASSWORD_CMS_PAGE));
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
				accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE_UPDATE_PASSWORD_FORM));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		return getViewForPage(model);
	}

	/**
	 * @description method is called to update password
	 * @param updatePasswordForm
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_PASSWORD, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updatePassword(final UpdatePasswordForm updatePasswordForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		try
		{
			//Fix for defect TISEE-3986 : handling special character like #
			final String currentPassword = java.net.URLDecoder.decode(updatePasswordForm.getCurrentPassword(), UTF);
			final String newPassword = java.net.URLDecoder.decode(updatePasswordForm.getNewPassword(), UTF);
			final String checkPassword = java.net.URLDecoder.decode(updatePasswordForm.getCheckNewPassword(), UTF);

			updatePasswordForm.setCurrentPassword(currentPassword);
			updatePasswordForm.setNewPassword(newPassword);
			updatePasswordForm.setCheckNewPassword(checkPassword);

			String returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEditPage;
			mplPasswordValidator.validate(updatePasswordForm, bindingResult);
			if (bindingResult.hasErrors())
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				returnAction = REDIRECT_TO_UPDATE_PROFILE_PAGE;
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				return returnAction;
			}
			else
			{
				if ((updatePasswordForm.getNewPassword().equals(updatePasswordForm.getCheckNewPassword()))
						&& !updatePasswordForm.getCurrentPassword().equals(updatePasswordForm.getNewPassword())
						&& !updatePasswordForm.getCurrentPassword().equals(updatePasswordForm.getCheckNewPassword()))
				{
					try
					{
						customerFacade.changePassword(updatePasswordForm.getCurrentPassword(), updatePasswordForm.getNewPassword());
						//						final CustomerModel currentUser = (CustomerModel) userService.getCurrentUser();
						//						mplCustomerProfileFacade.sendEmailForChangePassword(currentUser);
						final String specificUrl = RequestMappingUrlConstants.LINK_MY_ACCOUNT
								+ RequestMappingUrlConstants.LINK_UPDATE_PROFILE;
						final String profileUpdateUrl = urlForEmailContext(request, specificUrl);
						final List<String> updatedDetailList = new ArrayList<String>();
						updatedDetailList.add(MarketplacecommerceservicesConstants.PASSWORD_suffix);
						mplCustomerProfileFacade.sendEmailForUpdateCustomerProfile(updatedDetailList, profileUpdateUrl);
					}
					catch (final PasswordMismatchException e)
					{
						ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
								MarketplacecommerceservicesConstants.B0009));
						bindingResult.rejectValue(ModelAttributetConstants.CURRENT_PASSWORD,
								MessageConstants.PROFILE_CURRENTPASSWORD_INVALID, new Object[] {},
								MessageConstants.PROFILE_CURRENTPASSWORD_INVALID);
					}
				}
				else
				{
					bindingResult.rejectValue(ModelAttributetConstants.CHECK_NEW_PASSWORD,
							MessageConstants.VALIDATION_CHECKPWD_EQUALS, new Object[] {}, MessageConstants.VALIDATION_CHECKPWD_EQUALS);
				}
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						MessageConstants.TEXT_ACCOUNT_CONFIRMATION_PASSWORD_UPDATED, null);
				returnAction = REDIRECT_TO_UPDATE_PROFILE_PAGE;
				storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				return returnAction;
			}
		}
		catch (final UnsupportedEncodingException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 *
	 * @param currentPassword
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_CHECK_CURRENT_PASSWORD, method = RequestMethod.POST)
	@RequireHardLogIn
	@ResponseBody
	public String checkOldPassword(@RequestParam(ModelAttributetConstants.CURRENT_PASSWORD) String currentPassword,
			final Model model) throws CMSItemNotFoundException
	{
		try
		{

			boolean isMatched = true;
			String returnMessage = null;
			try
			{
				currentPassword = java.net.URLDecoder.decode(currentPassword, UTF);
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.error("Exception occured while decoding the password");
			}

			isMatched = mplCustomerProfileFacade.changePassword(currentPassword);
			if (isMatched)
			{
				returnMessage = ModelAttributetConstants.VALID_PASSWORD;
			}
			else
			{
				returnMessage = ModelAttributetConstants.INVALID_PASSWORD;
			}
			return returnMessage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to update nick name
	 * @param mplCustomerProfileForm
	 * @param bindingResult
	 * @param model
	 * @param redirectAttributes
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 * @throws ParseException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UPDATE_NICK_NAME, method = RequestMethod.POST)
	@RequireHardLogIn
	public String updateNickName(final MplCustomerProfileForm mplCustomerProfileForm, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, ParseException
	{
		try
		{
			String returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEditPage;
			final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
			final MplCustomerProfileData customerProfileData = mplCustomerProfileFacade.getCustomerProfileDetail(currentCustomerData
					.getDisplayUid());
			final String specificUrl = RequestMappingUrlConstants.LINK_MY_ACCOUNT + RequestMappingUrlConstants.LINK_UPDATE_PROFILE;
			final String profileUpdateUrl = urlForEmailContext(request, specificUrl);

			if (bindingResult.hasErrors())
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				final List<GenderData> genderList = mplCustomerProfileFacade.getGenders();
				model.addAttribute(ModelAttributetConstants.GENDER_DATA, genderList);
				model.addAttribute(ModelAttributetConstants.MPL_CUSTOMER_PROFILE_FORM, mplCustomerProfileForm);
				final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();
				model.addAttribute(ModelAttributetConstants.UPDATE_PASSWORD_FORM, updatePasswordForm);
				storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE_NICKNAME));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				return getViewForPage(model);
			}
			else
			{
				final MplCustomerProfileData mplCustomerProfileData = new MplCustomerProfileData();

				mplCustomerProfileData.setNickName(mplCustomerProfileForm.getNickName().trim());

				mplCustomerProfileData.setFirstName(customerProfileData.getFirstName().trim());
				mplCustomerProfileData.setLastName(customerProfileData.getLastName().trim());
				mplCustomerProfileData.setUid(currentCustomerData.getUid());
				mplCustomerProfileData.setDisplayUid(currentCustomerData.getDisplayUid());
				mplCustomerProfileData.setMobileNumber(customerProfileData.getMobileNumber().trim());
				mplCustomerProfileData.setDateOfAnniversary(customerProfileData.getDateOfAnniversary().trim());
				mplCustomerProfileData.setDateOfBirth(customerProfileData.getDateOfBirth().trim());

				if (null != customerProfileData.getGender())
				{
					mplCustomerProfileData.setGender(customerProfileData.getGender());
				}
				model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());


				try
				{
					mplCustomerProfileFacade.updateCustomerProfile(mplCustomerProfileData);
					final Map<String, String> preSavedDetailMap = new HashMap<String, String>();
					mplCustomerProfileFacade.checkChangesForSendingEmail(preSavedDetailMap, currentCustomerData.getDisplayUid(),
							profileUpdateUrl);
				}
				catch (final DuplicateUidException e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
							MarketplacecommerceservicesConstants.B0002));
					bindingResult
							.rejectValue(ModelAttributetConstants.EMAIL, MessageConstants.REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE);
					GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				}
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						MessageConstants.TEXT_ACCOUNT_PROFILE_CONFIRMATION_UPDATED, null);
				returnAction = REDIRECT_TO_UPDATE_PROFILE_PAGE;
			}
			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PROFILE_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return returnAction;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

	}

	/**
	 * @description method is called to get the AddressBook of the customer
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ADDRESS_BOOK, method = RequestMethod.GET)
	@RequireHardLogIn
	public String getAddressBook(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final List<StateData> stateDataList = getAccountAddressFacade().getStates();
			final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
			final List<String> AddressRadioTypeList = getAddressRadioTypeList();
			model.addAttribute(ModelAttributetConstants.ADDRESS_TYPE, getAddressType());
			model.addAttribute(ModelAttributetConstants.ADDRESS_RADIO_TYPE_LIST, AddressRadioTypeList);
			model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
			model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());

			final AccountAddressForm addressForm = getPreparedAddressForm();
			if (null != addressForm)
			{
				addressForm.setCountryIso(ModelAttributetConstants.INDIA_ISO_CODE);
				model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			}
			model.addAttribute(ModelAttributetConstants.ACCOUNT_ADDRESS, Boolean.TRUE);
			model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
			model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS, Boolean.FALSE);
			model.addAttribute(ModelAttributetConstants.ADDRESS_DATA,
					mplCheckoutFacadeImpl.rePopulateDeliveryAddress(getAccountAddressFacade().getAddressBook()));
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			model.addAttribute(ModelAttributetConstants.ADDRESS_TYPE, getAddressType());
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @return List<String>
	 */
	private List<String> getAddressRadioTypeList()
	{
		final List<String> list = new ArrayList<String>();
		list.add(ModelAttributetConstants.LIST_VAL_RESIDENTIAL);
		list.add(ModelAttributetConstants.LIST_VAL_COMMERCIAL);
		return list;
	}

	/**
	 * @description method is called to add an Address to the Customer's AddressBook
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ADD_ADDRESS, method = RequestMethod.GET)
	@RequireHardLogIn
	public String addAddress(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final List<StateData> stateDataList = getAccountAddressFacade().getStates();
			final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
			model.addAttribute(ModelAttributetConstants.ADDRESS_TYPE, getAddressType());
			model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
			model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
			final AccountAddressForm addressForm = getPreparedAddressForm();

			if (null != addressForm)
			{
				addressForm.setCountryIso(ModelAttributetConstants.INDIA_ISO_CODE);
				model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			}
			model.addAttribute(ModelAttributetConstants.ACCOUNT_ADDRESS, Boolean.TRUE);
			model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
			model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS, Boolean.FALSE);

			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb(
					RequestMappingUrlConstants.LINK_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ADDRESS_BOOK, getMessageSource()
							.getMessage(MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK, null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb(ModelAttributetConstants.HASH_VAL, getMessageSource().getMessage(
					MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK_ADDEDITADDRESS, null, getI18nService().getCurrentLocale()), null));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS, breadcrumbs);
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}


	/**
	 * @description method is called to add an Address to the Customer's AddressBook
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_POPULATE_ADDRESS_DETAIL_AJAX, method = RequestMethod.GET)
	@RequireHardLogIn
	@ResponseBody
	public AccountAddressForm populateAddressDetail(@RequestParam(ModelAttributetConstants.ADDRESS_ID) final String addressId,
			final Model model) throws CMSItemNotFoundException
	{
		final AccountAddressForm addressForm = new AccountAddressForm();
		for (final AddressData addressData : getAccountAddressFacade().getAddressBook())
		{
			if (null != addressData && null != addressData.getId() && addressData.getId().equals(addressId))
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
				addressForm.setCountryIso(ModelAttributetConstants.INDIA_ISO_CODE);
				addressForm.setAddressType(addressData.getAddressType());
				addressForm.setMobileNo(addressData.getPhone());
				addressForm.setState(addressData.getState());
				addressForm.setLocality(addressData.getLocality());
				addressForm.setLine3(addressData.getLine3());
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
		return addressForm;
	}



	/**
	 * @description method is called to add an Address to the Customer's AddressBook
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ADD_NEW_ADDRESS, method = RequestMethod.POST)
	@RequireHardLogIn
	public String addAddress(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		try
		{
			if (null != request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE))
			{
				if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_RESIDENTIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_HOME);
				}
				else if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_COMMERCIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_WORK);
				}
			}
			final String errorMsg = mplAddressValidator.validate(addressForm);
			if (!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
			{
				GlobalMessages.addErrorMessage(model, errorMsg);
				final List<StateData> stateDataList = getAccountAddressFacade().getStates();
				final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
				model.addAttribute(ModelAttributetConstants.ACCOUNT_ADDRESS, Boolean.TRUE);
				final List<String> AddressRadioTypeList = getAddressRadioTypeList();
				model.addAttribute(ModelAttributetConstants.ADDRESS_RADIO_TYPE_LIST, AddressRadioTypeList);
				storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				return REDIRECT_TO_ADDRESS_BOOK_PAGE;
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
			newAddress.setVisibleInAddressBook(true);
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setState(addressForm.getState());
			newAddress.setCountry(getI18NFacade().getCountryForIsocode(ModelAttributetConstants.INDIA_ISO_CODE));
			newAddress.setLine3(addressForm.getLine3());
			newAddress.setLocality(addressForm.getLocality());

			if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
			{
				newAddress.setRegion(getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso()));
			}

			if (userFacade.isAddressBookEmpty())
			{
				newAddress.setDefaultAddress(Boolean.TRUE);
				newAddress.setVisibleInAddressBook(Boolean.TRUE);
			}
			else
			{
				if (null != request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX)
						&& request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX).equalsIgnoreCase(
								ModelAttributetConstants.TRUE))
				{
					addressForm.setDefaultAddress(Boolean.TRUE);
				}
				newAddress.setDefaultAddress(addressForm.getDefaultAddress() != null
						&& addressForm.getDefaultAddress().booleanValue());
			}
			getAccountAddressFacade().addaddress(newAddress);

			return REDIRECT_TO_ADDRESS_BOOK_PAGE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}



	/**
	 * @description method is called to edit Address of the Customer
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @param redirectModel
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS_NEW, method = RequestMethod.POST)
	@RequireHardLogIn
	public String editAddress(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		try
		{
			if (null != request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE))
			{
				if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_RESIDENTIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_HOME);
				}
				else if (request.getParameter(ModelAttributetConstants.ADDRESS_RADIO_TYPE).equalsIgnoreCase(
						ModelAttributetConstants.TYPE_COMMERCIAL))
				{
					addressForm.setAddressType(ModelAttributetConstants.TYPE_WORK);
				}
			}
			model.addAttribute(ModelAttributetConstants.ADDRESS_TYPE, getAddressType());
			final String errorMsg = mplAddressValidator.validate(addressForm);

			if (!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
			{
				GlobalMessages.addErrorMessage(model, errorMsg);
				final List<StateData> stateDataList = getAccountAddressFacade().getStates();
				final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
				model.addAttribute(ModelAttributetConstants.ACCOUNT_ADDRESS, Boolean.TRUE);
				final List<String> AddressRadioTypeList = getAddressRadioTypeList();
				model.addAttribute(ModelAttributetConstants.ADDRESS_RADIO_TYPE_LIST, AddressRadioTypeList);
				storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				return REDIRECT_TO_ADDRESS_BOOK_PAGE;
			}

			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);

			final AddressData newAddress = new AddressData();
			newAddress.setId(addressForm.getAddressId());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());
			newAddress.setLine1(addressForm.getLine1());
			newAddress.setLine2(addressForm.getLine2());
			newAddress.setTown(addressForm.getTownCity());
			newAddress.setPostalCode(addressForm.getPostcode());
			newAddress.setBillingAddress(false);
			newAddress.setShippingAddress(true);
			newAddress.setVisibleInAddressBook(true);
			newAddress.setCountry(getI18NFacade().getCountryForIsocode(ModelAttributetConstants.INDIA_ISO_CODE));
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setState(addressForm.getState());
			newAddress.setLine3(addressForm.getLine3());
			newAddress.setLocality(addressForm.getLocality());

			if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
			{
				newAddress.setRegion(getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso()));
			}
			//			if (Boolean.TRUE.equals(addressForm.getDefaultAddress()) || userFacade.getAddressBook().size() <= 1)
			//			{
			//				newAddress.setDefaultAddress(true);
			//				newAddress.setVisibleInAddressBook(true);
			//			}
			if (userFacade.isAddressBookEmpty())
			{
				newAddress.setDefaultAddress(Boolean.TRUE);
				newAddress.setVisibleInAddressBook(Boolean.TRUE);
			}
			else
			{
				if (null != request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX)
						&& request.getParameter(ModelAttributetConstants.DEFAULT_ADDRESS_CHECKBOX).equalsIgnoreCase(
								ModelAttributetConstants.TRUE))
				{
					addressForm.setDefaultAddress(Boolean.TRUE);
				}
				newAddress.setDefaultAddress(addressForm.getDefaultAddress() != null
						&& addressForm.getDefaultAddress().booleanValue());
			}

			LOG.info("addrestype=" + newAddress.getAddressType());
			getAccountAddressFacade().editAddress(newAddress);

			return REDIRECT_TO_ADDRESS_BOOK_PAGE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}


	/**
	 * @description adding a value at zeroth index of state list
	 * @param stateDataList
	 * @return List<StateData>
	 */
	private List<StateData> getFinalStateList(final List<StateData> stateDataList)
	{
		final StateData newdata = new StateData();
		newdata.setCode("00");
		newdata.setCountryKey("IN");
		newdata.setName("Select");
		stateDataList.add(0, newdata);
		return stateDataList;
	}

	/**
	 * @description method is called to get the AddressForm
	 * @return addressForm
	 */
	protected AccountAddressForm getPreparedAddressForm()
	{
		final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
		final AccountAddressForm addressForm = new AccountAddressForm();
		addressForm.setFirstName(currentCustomerData.getFirstName());
		addressForm.setLastName(currentCustomerData.getLastName());
		return addressForm;
	}



	/**
	 * @description method is called to setUp Error to AddressForm after getting Error
	 * @param addressForm
	 * @param model
	 */
	protected void setUpAddressFormAfterError(final AccountAddressForm addressForm, final Model model)
	{
		model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
		model.addAttribute(ModelAttributetConstants.ADDRESS_TYPE, getAddressType());
		model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
		model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
		model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
		model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS,
				Boolean.valueOf(isDefaultAddress(addressForm.getAddressId())));
		if (addressForm.getCountryIso() != null)
		{
			model.addAttribute(ModelAttributetConstants.REGIONS, getI18NFacade()
					.getRegionsForCountryIso(addressForm.getCountryIso()));
			model.addAttribute(ModelAttributetConstants.COUNTRY, addressForm.getCountryIso());
		}
	}


	/**
	 * @description method is called to edit Address of the Customer
	 * @param addressCode
	 * @param model
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editAddress(@PathVariable(ModelAttributetConstants.ADDRESS_CODE) final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final List<StateData> stateDataList = getAccountAddressFacade().getStates();
			final List<StateData> stateDataListNew = getFinalStateList(stateDataList);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataListNew);
			model.addAttribute(ModelAttributetConstants.ADDRESS_TYPE, getAddressType());
			model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
			model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
			final AccountAddressForm addressForm = new AccountAddressForm();
			model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));

			for (final AddressData addressData : getAccountAddressFacade().getAddressBook())
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
					addressForm.setCountryIso(ModelAttributetConstants.INDIA_ISO_CODE);
					addressForm.setAddressType(addressData.getAddressType());
					addressForm.setMobileNo(addressData.getPhone());
					addressForm.setState(addressData.getState());
					addressForm.setLocality(addressData.getLocality());
					addressForm.setLine3(addressData.getLine3());
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
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb(
					RequestMappingUrlConstants.LINK_MY_ACCOUNT + RequestMappingUrlConstants.LINK_ADDRESS_BOOK, getMessageSource()
							.getMessage(MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK, null, getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb(ModelAttributetConstants.HASH_VAL, getMessageSource().getMessage(
					MessageConstants.TEXT_ACCOUNT_ADDRESSBOOK_ADDEDITADDRESS, null, getI18nService().getCurrentLocale()), null));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS, breadcrumbs);
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			model.addAttribute(ModelAttributetConstants.EDIT, Boolean.TRUE);
			model.addAttribute(ModelAttributetConstants.ACCOUNT_ADDRESS, Boolean.TRUE);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
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
		final AddressData defaultAddress = getAccountAddressFacade().getDefaultAddress();
		return (defaultAddress != null && defaultAddress.getId() != null && defaultAddress.getId().equals(addressId));
	}


	/**
	 * @description method is called to Select Address from Suggested Address List
	 * @param addressForm
	 * @param redirectModel
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_SELECT_SUGGESTED_ADDRESS, method = RequestMethod.POST)
	public String doSelectSuggestedAddress(final AccountAddressForm addressForm, final RedirectAttributes redirectModel,
			final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final Set<String> resolveCountryRegions = org.springframework.util.StringUtils.commaDelimitedListToSet(Config
					.getParameter("resolve.country.regions"));

			final AddressData selectedAddress = new AddressData();
			selectedAddress.setId(addressForm.getAddressId());
			selectedAddress.setTitleCode(addressForm.getTitleCode());
			selectedAddress.setFirstName(addressForm.getFirstName());
			selectedAddress.setLastName(addressForm.getLastName());
			selectedAddress.setLine1(addressForm.getLine1());
			selectedAddress.setLine2(addressForm.getLine2());
			selectedAddress.setTown(addressForm.getTownCity());
			selectedAddress.setPostalCode(addressForm.getPostcode());
			selectedAddress.setBillingAddress(false);
			selectedAddress.setShippingAddress(true);
			selectedAddress.setVisibleInAddressBook(true);
			selectedAddress.setLine3(addressForm.getLine3());
			selectedAddress.setLocality(addressForm.getLocality());

			final CountryData countryData = i18NFacade.getCountryForIsocode(addressForm.getCountryIso());
			selectedAddress.setCountry(countryData);
			if (resolveCountryRegions.contains(countryData.getIsocode()) && null != addressForm.getRegionIso()
					&& !StringUtils.isEmpty(addressForm.getRegionIso()))
			{
				final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
				selectedAddress.setRegion(regionData);
			}

			if (Boolean.TRUE.equals(addressForm.getEditAddress()))
			{
				selectedAddress.setDefaultAddress(Boolean.TRUE.equals(addressForm.getDefaultAddress())
						|| getAccountAddressFacade().getAddressBook().size() <= 1);
				getAccountAddressFacade().editAddress(selectedAddress);
			}
			else
			{
				selectedAddress.setDefaultAddress(Boolean.TRUE.equals(addressForm.getDefaultAddress())
						|| userFacade.isAddressBookEmpty());
				userFacade.addAddress(selectedAddress);
			}

			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
					MessageConstants.ACCOUNT_CONFIRMATION_ADDRESS_ADDED);
			return REDIRECT_TO_ADDRESS_BOOK_PAGE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to remove Address from Customer's address book
	 * @param addressCode
	 * @param redirectModel
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REMOVE_ADDRESS + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public String removeAddress(@PathVariable(ModelAttributetConstants.ADDRESS_CODE) final String addressCode,
			final RedirectAttributes redirectModel, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final AddressData addressData = new AddressData();
			addressData.setId(addressCode);
			userFacade.removeAddress(addressData);

			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
					MessageConstants.ACCOUNT_CONFIRMATION_ADDRESS_REMOVED);
			return REDIRECT_TO_ADDRESS_BOOK_PAGE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to set DefaultAddress from existing Address of the customer
	 * @param addressCode
	 * @param redirectModel
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_SET_DEFAUT_ADDRESS + ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String setDefaultAddress(@PathVariable(ModelAttributetConstants.ADDRESS_CODE) final String addressCode,
			final RedirectAttributes redirectModel, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final AddressData addressData = new AddressData();
			addressData.setDefaultAddress(true);
			addressData.setVisibleInAddressBook(true);
			addressData.setId(addressCode);
			userFacade.setDefaultAddress(addressData);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
					MessageConstants.ACCOUNT_CONFIRMATION_DEFAULT_ADDRESS_CHANGED);
			return REDIRECT_TO_ADDRESS_BOOK_PAGE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}


	/**
	 * @description Method to get all stored cards
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_PAYMENT_DETAILS, method = RequestMethod.GET)
	@RequireHardLogIn
	public String getSavedCards(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			model.addAttribute(ModelAttributetConstants.CUSTOMER_DATA, customerFacade.getCurrentCustomer());
			Map<Date, SavedCardData> creditCard = new TreeMap<Date, SavedCardData>(Collections.reverseOrder());
			Map<Date, SavedCardData> debitCard = new TreeMap<Date, SavedCardData>(Collections.reverseOrder());

			//getting the current customer to fetch customer Id and customer email
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			//final String customerId = customer.getUid();

			creditCard = mplPaymentFacadeImpl.listStoredCreditCards(customer);
			debitCard = mplPaymentFacadeImpl.listStoredDebitCards(customer);

			model.addAttribute(ModelAttributetConstants.CREDIT_CARDS, creditCard);
			model.addAttribute(ModelAttributetConstants.DEBIT_CARDS, debitCard);
			storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PAYMENTDETAILS));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description Method to set default card detail
	 * @param paymentInfoId
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_SET_DEFAUT_PAYMENT_DETAILS, method = RequestMethod.POST)
	@RequireHardLogIn
	public String setDefaultPaymentDetails(@RequestParam final String paymentInfoId, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			CCPaymentInfoData paymentInfoData = null;
			if (StringUtils.isNotBlank(paymentInfoId))
			{
				paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentInfoId);
			}
			userFacade.setDefaultPaymentInfo(paymentInfoData);
			return REDIRECT_TO_PAYMENT_INFO_PAGE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description Method to remove stored cards
	 * @param model
	 * @param cardToken
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REMOVE_PAYMENT_METHOD, method = RequestMethod.GET)
	@RequireHardLogIn
	public String removePaymentMethod(final Model model,
			@RequestParam(value = ModelAttributetConstants.PAYMENT_INFO_ID) final String cardToken,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			mplCustomerProfileFacade.removeCardDetail(cardToken);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					MessageConstants.TEXT_ACCOUNT_PROFILE_PAYMENTCART_REMOVED);
			return REDIRECT_TO_PAYMENT_INFO_PAGE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}


	/**
	 * @description method is called to view all wishlists
	 * @param productCode
	 * @param model
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = PRODUCT_CODE_PATH_VARIABLE_PATTERN + RequestMappingUrlConstants.LINK_WISHLIST, method = RequestMethod.GET)
	@RequireHardLogIn
	public String showWishlistPage(@PathVariable(ModelAttributetConstants.PRODUCT_CODE) final String productCode, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			if (productCode.equals(ModelAttributetConstants.DEFAULT))
			{
				model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
				final ProductModel productModel = getMplOrderFacade().getProductForCode(productCode);
				final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
						ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
						ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW, ProductOption.DELIVERY_MODE_AVAILABILITY));
				populateProductData(productData, model);
			}
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();

			sessionService.setAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG, ModelAttributetConstants.Y_CAPS_VAL);

			model.addAttribute(ModelAttributetConstants.ALL_WISHLISTS, allWishlists);
			model.addAttribute(ModelAttributetConstants.EDIT_WISHLIST_NAME_DATA, new EditWishlistNameData());
			final NewWishlistData newWishlistData = new NewWishlistData();
			model.addAttribute(ModelAttributetConstants.NEW_WISHLIST_DATA, newWishlistData);
			final ExistingWishlistData existingWishlistData = new ExistingWishlistData();
			model.addAttribute(ModelAttributetConstants.EXISTING_WISHLIST_DATA, existingWishlistData);
			final ParticularWishlistData1 particularWishlistData = new ParticularWishlistData1();
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_DATA, particularWishlistData);
			final RemoveWishlistData removeWishlistData = new RemoveWishlistData();
			model.addAttribute(ModelAttributetConstants.REMOVE_WISHLIST_DATA, removeWishlistData);
			model.addAttribute(ModelAttributetConstants.RENDERING_METHOD, ModelAttributetConstants.RENDERING_METHOD_WISHLIST);
			model.addAttribute(ModelAttributetConstants.SHOW_WISHLIST, "n");
			storeCmsPageInModel(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_WISHLIST));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to create new wishlist and add product
	 * @param newWishlistData
	 * @param model
	 * @param redirectAttributes
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_CREATE_NEW_WISHLIST, method = RequestMethod.GET)
	public String createNewWishlistAndAddProduct(
			@ModelAttribute(ModelAttributetConstants.NEW_WISHLIST_DATA) final NewWishlistData newWishlistData, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final int size = allWishlists.size();
			LOG.info(MessageConstants.TOTAL_NO_WISHLIST + size);

			final UserModel user = userService.getCurrentUser();
			String name = null;
			String nameSet = null;
			int wSize = 0;
			if (null != newWishlistData)
			{
				if (null != newWishlistData.getNewWishlistName())
				{
					if (!newWishlistData.getNewWishlistName().isEmpty())
					{
						name = newWishlistData.getNewWishlistName();
					}
					else
					{
						nameSet = ModelAttributetConstants.WISHLIST_NO;
						for (final Wishlist2Model wishlist2Model : allWishlists)
						{
							if (wishlist2Model.getName().contains(nameSet))
							{
								wSize++;
							}
						}
						name = ModelAttributetConstants.WISHLIST_NO + ModelAttributetConstants.UNDER_SCORE + (wSize + 1);
					}
				}
				else
				{
					nameSet = ModelAttributetConstants.WISHLIST_NO;
					for (final Wishlist2Model wishlist2Model : allWishlists)
					{
						if (wishlist2Model.getName().contains(nameSet))
						{
							wSize++;
						}
					}
					name = ModelAttributetConstants.WISHLIST_NO + ModelAttributetConstants.UNDER_SCORE + (wSize + 1);
				}
			}

			Wishlist2Model createdWishlist = null;
			final Wishlist2Model particularWishlistCheck = wishlistFacade.getWishlistForName(name);
			if (null == particularWishlistCheck)
			{
				createdWishlist = wishlistFacade.createNewWishlist(user, name, newWishlistData.getProductCode());
				final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
				final List<OrderEntryData> cartEntry = cartData.getEntries();
				if (cartEntry != null && !cartEntry.isEmpty())
				{
					for (final OrderEntryData entry : cartData.getEntries())
					{
						if (entry.getProduct().getCode().equals(newWishlistData.getProductCode()))
						{
							//remove product
							try
							{
								mplCartFacade.updateCartEntry(entry.getEntryNumber().intValue(), 0L);
							}
							catch (final CommerceCartModificationException e)
							{
								LOG.info(e);
							}
							final UpdateQuantityForm uqf = new UpdateQuantityForm();
							uqf.setQuantity(entry.getQuantity());
							model.addAttribute(ModelAttributetConstants.UPDATE_QUANTITY_FORM + entry.getEntryNumber(), uqf);
						}
					}
				}
			}
			else
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.SYSTEM_ERROR_DUPLICATE_WISHLIST_NAME);
			}
			if (null != createdWishlist)
			{
				wishlistFacade.addProductToWishlist(createdWishlist, newWishlistData.getProductCode(),
						ModelAttributetConstants.TEST_USSID_NO, false);
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						MessageConstants.SYSTEM_ERROR_WISHLIST_CREATION_PRODUCT_ADD_SUCCESS, null);
			}
			else
			{
				LOG.info(MessageConstants.COULD_NOT_CREATE_WISHLIST);
			}

			final ProductModel productModel = getMplOrderFacade().getProductForCode(newWishlistData.getProductCode());
			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
					ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW, ProductOption.DELIVERY_MODE_AVAILABILITY));

			populateProductData(productData, model);

			final Wishlist2Model particularWishlist = wishlistFacade.getWishlistForName(name);

			final List<ProductData> datas = new ArrayList<ProductData>();
			final List<Wishlist2EntryModel> entryModels = particularWishlist.getEntries();
			if (entryModels.size() >= 1)
			{
				for (final Wishlist2EntryModel entryModel : entryModels)
				{
					final ProductData productData1 = productFacade.getProductForOptions(entryModel.getProduct(), Arrays.asList(
							ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
							ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW,
							ProductOption.DELIVERY_MODE_AVAILABILITY));
					datas.add(productData1);
				}
			}

			sessionService.setAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG, ModelAttributetConstants.Y_CAPS_VAL);

			model.addAttribute(ModelAttributetConstants.PRODUCT_DATAS, datas);
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_NAME, particularWishlist.getName());
			model.addAttribute(new WishlistData());
			model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
			model.addAttribute(ModelAttributetConstants.ALL_WISHLISTS, allWishlists);
			model.addAttribute(ModelAttributetConstants.EDIT_WISHLIST_NAME_DATA, new EditWishlistNameData());
			final NewWishlistData newWishlistData1 = new NewWishlistData();
			model.addAttribute(ModelAttributetConstants.NEW_WISHLIST_DATA, newWishlistData1);
			final ExistingWishlistData existingWishlistData = new ExistingWishlistData();
			model.addAttribute(ModelAttributetConstants.EXISTING_WISHLIST_DATA, existingWishlistData);
			final ParticularWishlistData1 particularWishlistData = new ParticularWishlistData1();
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_DATA, particularWishlistData);
			final RemoveWishlistData removeWishlistData = new RemoveWishlistData();
			model.addAttribute(ModelAttributetConstants.REMOVE_WISHLIST_DATA, removeWishlistData);
			model.addAttribute(ModelAttributetConstants.RENDERING_METHOD,
					ModelAttributetConstants.RENDERING_METHOD_CREATE_NEW_WISHLIST);
			model.addAttribute(ModelAttributetConstants.SHOW_WISHLIST, ModelAttributetConstants.Y_SMALL_VAL);
			storeCmsPageInModel(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_WISHLIST));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to create new wish list without adding ant product
	 * @param newWishlistData
	 * @param model
	 * @param redirectAttributes
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_CREATE_NEW_WISHLIST_WP, method = RequestMethod.GET)
	@ResponseBody
	public String createNewWishlistWP(@RequestParam("newWishlistData") final String newWishlistData, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final int size = allWishlists.size();
			LOG.info(MessageConstants.TOTAL_NO_WISHLIST + size);

			final UserModel user = userService.getCurrentUser();
			String nameWL = null;

			if (null != newWishlistData && !newWishlistData.isEmpty())
			{
				nameWL = newWishlistData;
				Wishlist2Model createdSavedWishlist = null;
				final Wishlist2Model particularWishlistCheck = wishlistFacade.getWishlistForName(nameWL);
				if (null == particularWishlistCheck)
				{
					createdSavedWishlist = wishlistFacade.createNewWishlist(user, nameWL, newWishlistData.toString());
					LOG.info(MessageConstants.NEWLY_CREATED + createdSavedWishlist.getName());
					return ModelAttributetConstants.SUCCESS;
				}
				else
				{
					return ModelAttributetConstants.DUPLICATE_WISHLIST_NAME;
				}

			}
			else
			{
				return ModelAttributetConstants.SYSTEM_ERROR_EMPTY_WISHLIST_NAME;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to add product in existing wishlist
	 * @param existingWishlistData
	 * @param model
	 * @param redirectAttributes
	 * @return getViewForPage(model)
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_ADD_IN_EXISTING_WISHLIST, method = RequestMethod.GET)
	public String createNewWishlistAndAddProduct(
			@ModelAttribute(ModelAttributetConstants.EXISTING_WISHLIST_DATA) final ExistingWishlistData existingWishlistData,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{

		try
		{
			final ProductModel productModel = getMplOrderFacade().getProductForCode(existingWishlistData.getProductCode());
			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
					ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
					ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW, ProductOption.DELIVERY_MODE_AVAILABILITY));

			populateProductData(productData, model);
			model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
			final Wishlist2Model existingWishlist = wishlistFacade
					.getWishlistForName(existingWishlistData.getExistingWishlistName());
			if (null != existingWishlist)
			{
				wishlistFacade.addProductToWishlist(existingWishlist, existingWishlistData.getProductCode(),
						ModelAttributetConstants.TEST_USSID_NO, Boolean.FALSE);
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
						MessageConstants.SYSTEM_ERROR_WISHLIST_PRODUCT_ADD_SUCCESS, null);
				final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(Boolean.TRUE);
				final List<OrderEntryData> cartEntry = cartData.getEntries();

				if (cartEntry != null && !cartEntry.isEmpty())
				{
					for (final OrderEntryData entry : cartData.getEntries())
					{
						if (entry.getProduct().getCode().equals(existingWishlistData.getProductCode()))
						{
							//remove product
							try
							{
								mplCartFacade.updateCartEntry(entry.getEntryNumber().intValue(), 0L);
								final UpdateQuantityForm uqf = new UpdateQuantityForm();
								uqf.setQuantity(entry.getQuantity());
								model.addAttribute(ModelAttributetConstants.UPDATE_QUANTITY_FORM + entry.getEntryNumber(), uqf);
							}
							catch (final CommerceCartModificationException e)
							{
								LOG.debug(e);
							}

						}

					}
				}
			}
			else
			{
				LOG.info(MessageConstants.NO_WISHLIST_PRESENT_WITH_NAME + existingWishlistData.getExistingWishlistName());
			}

			final Wishlist2Model particularWishlist = wishlistFacade.getWishlistForName(existingWishlistData
					.getExistingWishlistName());

			final List<ProductData> datas = new ArrayList<ProductData>();
			final List<Wishlist2EntryModel> entryModels = particularWishlist.getEntries();
			if (entryModels.size() >= 1)
			{
				for (final Wishlist2EntryModel entryModel : entryModels)
				{
					final ProductData productData1 = productFacade.getProductForOptions(entryModel.getProduct(), Arrays.asList(
							ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
							ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW,
							ProductOption.DELIVERY_MODE_AVAILABILITY));
					datas.add(productData1);
				}
			}
			sessionService.setAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG, ModelAttributetConstants.Y_CAPS_VAL);

			model.addAttribute(ModelAttributetConstants.PRODUCT_DATAS, datas);
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_NAME, particularWishlist.getName());
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			model.addAttribute(ModelAttributetConstants.ALL_WISHLISTS, allWishlists);
			model.addAttribute(new WishlistData());
			model.addAttribute(ModelAttributetConstants.EDIT_WISHLIST_NAME_DATA, new EditWishlistNameData());
			final NewWishlistData newWishlistData1 = new NewWishlistData();
			model.addAttribute(ModelAttributetConstants.NEW_WISHLIST_DATA, newWishlistData1);
			final ExistingWishlistData existingWishlistData1 = new ExistingWishlistData();
			model.addAttribute(ModelAttributetConstants.EXISTING_WISHLIST_DATA, existingWishlistData1);
			final ParticularWishlistData1 particularWishlistData = new ParticularWishlistData1();
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_DATA, particularWishlistData);
			final RemoveWishlistData removeWishlistData = new RemoveWishlistData();
			model.addAttribute(ModelAttributetConstants.REMOVE_WISHLIST_DATA, removeWishlistData);
			model.addAttribute(ModelAttributetConstants.RENDERING_METHOD,
					ModelAttributetConstants.RENDERING_METHOD_ADD_IN_EXISTING_WISHLIST);
			model.addAttribute(ModelAttributetConstants.SHOW_WISHLIST, ModelAttributetConstants.Y_SMALL_VAL);
			storeCmsPageInModel(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_WISHLIST));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description to delete the wishlist
	 * @param redirectAttributes
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_WISHLIST_DELETE, method = RequestMethod.GET)
	@RequireHardLogIn
	@ResponseBody
	public String deleteWishlist(@RequestParam("wishlistName") final String wishlistName,
			final RedirectAttributes redirectAttributes, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final List<Wishlist2Model> finalListOfWishlists = allWishlists;
			for (final Wishlist2Model wishlist2Model : finalListOfWishlists)
			{
				if (wishlist2Model.getName().equals(wishlistName))
				{
					modelService.remove(wishlist2Model);
				}
			}

			model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
			final List<Wishlist2Model> newAllWishlists = wishlistFacade.getAllWishlists();
			sessionService.setAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG, ModelAttributetConstants.Y_CAPS_VAL);

			model.addAttribute(ModelAttributetConstants.ALL_WISHLISTS, newAllWishlists);
			model.addAttribute(ModelAttributetConstants.EDIT_WISHLIST_NAME_DATA, new EditWishlistNameData());
			final NewWishlistData newWishlistData = new NewWishlistData();
			model.addAttribute(ModelAttributetConstants.NEW_WISHLIST_DATA, newWishlistData);
			final ExistingWishlistData existingWishlistData = new ExistingWishlistData();
			model.addAttribute(ModelAttributetConstants.EXISTING_WISHLIST_DATA, existingWishlistData);
			final ParticularWishlistData1 particularWishlistData = new ParticularWishlistData1();
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_DATA, particularWishlistData);
			final RemoveWishlistData removeWishlistData = new RemoveWishlistData();
			model.addAttribute(ModelAttributetConstants.REMOVE_WISHLIST_DATA, removeWishlistData);
			model.addAttribute(ModelAttributetConstants.RENDERING_METHOD, ModelAttributetConstants.RENDERING_METHOD_WISHLIST);
			model.addAttribute(ModelAttributetConstants.SHOW_WISHLIST, "n");
			storeCmsPageInModel(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_WISHLIST));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to view particular wishlist
	 * @param viewParticularWishlist
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_VIEW_PARTICULAR_WISHLIST, method = RequestMethod.GET)
	public String viewParticularWishlist(@RequestParam("particularWishlist") final String viewParticularWishlist, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d,yyyy");
			final Wishlist2Model particularWishlist = wishlistFacade.getWishlistForName(viewParticularWishlist);
			final String pageSizeInWl = configurationService.getConfiguration().getString(MessageConstants.WISHLIST_PAGESIZE).trim();
			final List<ProductData> datas = new ArrayList<ProductData>();
			final List<WishlistProductData> wpDataList = new ArrayList<WishlistProductData>();
			Boolean isDelisted = Boolean.FALSE;
			if (null != particularWishlist && null != particularWishlist.getEntries() && !particularWishlist.getEntries().isEmpty())
			{
				final List<Wishlist2EntryModel> entryModels = particularWishlist.getEntries();

				//List<Wishlist2EntryModel> updatedEntryModelList = new ArrayList<Wishlist2EntryModel>();

				for (final Wishlist2EntryModel entry : entryModels)
				{
					//TISEE-6376
					if (entry.getProduct() != null && entry.getProduct().getCode() != null)
					{
						final ProductModel productModel = getMplOrderFacade().getProductForCode(entry.getProduct().getCode());
						if (null != productModel.getSellerInformationRelator())
						{
							final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
									.getSellerInformationRelator();
							for (final SellerInformationModel sellerInformationModel : sellerInfo)
							{
								if (sellerInformationModel.getSellerArticleSKU().equals(entry.getUssid())
										&& null != sellerInformationModel.getSellerAssociationStatus()
										&& sellerInformationModel.getSellerAssociationStatus().equals(SellerAssociationStatusEnum.NO))
								{
									modelService.remove(entry);
									isDelisted = Boolean.TRUE;
								}
							}
						}
					}

					final boolean isWishlistEntryValid = mplCartFacade.isWishlistEntryValid(entry);
					if (!isDelisted && !isWishlistEntryValid)
					{
						modelService.remove(entry);
						isDelisted = Boolean.TRUE;
					}

				}

				//refreshing Wishlist2Model
				modelService.refresh(particularWishlist);
				final List<Wishlist2EntryModel> wishlist2EntryModels = particularWishlist.getEntries();

				final List<Wishlist2EntryModel> allProductsModifiable = new ArrayList<Wishlist2EntryModel>(wishlist2EntryModels);
				Collections.sort(allProductsModifiable, new AllProductsInWishlistByDate());
				if (allProductsModifiable.size() >= 1)
				{
					for (final Wishlist2EntryModel entryModel : allProductsModifiable)
					{
						final WishlistProductData wishlistProductData = new WishlistProductData();
						//TISEE-6376
						if (entryModel.getProduct() != null)
						{
							final ProductData productData1 = productFacade.getProductForOptions(entryModel.getProduct(), Arrays.asList(
									ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
									ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW,
									ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.SELLER));

							final BuyBoxModel buyboxmodel = buyBoxFacade.getpriceForUssid(entryModel.getUssid());
							double price = 0.0;
							if (null != buyboxmodel)
							{
								if (null != buyboxmodel.getSpecialPrice() && buyboxmodel.getSpecialPrice().doubleValue() > 0.0)
								{
									price = buyboxmodel.getSpecialPrice().doubleValue();
								}
								else if (null != buyboxmodel.getPrice() && buyboxmodel.getPrice().doubleValue() > 0.0)
								{
									price = buyboxmodel.getPrice().doubleValue();
								}
								else
								{
									price = buyboxmodel.getMrp().doubleValue();
								}
								final PriceData priceData = productDetailsHelper.formPriceData(new Double(price));
								wishlistProductData.setPrice(priceData);

							}

							datas.add(productData1);
							wishlistProductData.setProductData(productData1);

							if (null != productData1 && null != productData1.getSeller() && productData1.getSeller().size() > 0)
							{
								productData1.setUssID(entryModel.getUssid());
								final List<SellerInformationData> sellerDatas = productData1.getSeller();
								for (final SellerInformationData sellerData : sellerDatas)
								{
									if (sellerData.getUssid().equals(entryModel.getUssid()))
									{
										wishlistProductData.setSellerInfoData(sellerData);

										if (sellerData.getAvailableStock() <= 0)
										{
											wishlistProductData.setIsOutOfStock(ModelAttributetConstants.Y_CAPS_VAL);
										}
										else
										{
											wishlistProductData.setIsOutOfStock(ModelAttributetConstants.N_CAPS_VAL);
										}

										break;
									}
								}
								wishlistProductData.setWishlistAddedDate(simpleDateFormat.format(entryModel.getAddedDate()));
								wishlistProductData.setProductCategory(productData1.getRootCategory());
								if (entryModel.getSizeSelected().booleanValue())
								{
									wishlistProductData.setWishlistProductSize(productData1.getSize());
								}
							}
							wpDataList.add(wishlistProductData);
						}

					}
				}
			}
			sessionService.setAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG, ModelAttributetConstants.Y_CAPS_VAL);

			model.addAttribute(ModelAttributetConstants.IS_DELISTED, isDelisted);
			model.addAttribute(ModelAttributetConstants.WISHLIST_SIZE, pageSizeInWl);
			model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
			model.addAttribute(ModelAttributetConstants.PRODUCT_DATAS, datas);
			model.addAttribute(ModelAttributetConstants.WISHLIST_PRODUCT_DATA_LIST, wpDataList);
			model.addAttribute(new WishlistData());
			model.addAttribute(new ReviewForm());
			model.addAttribute(ModelAttributetConstants.EDIT_WISHLIST_NAME_DATA, new EditWishlistNameData());
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			model.addAttribute(ModelAttributetConstants.ALL_WISHLISTS, allWishlists);
			final NewWishlistData newWishlistData1 = new NewWishlistData();
			model.addAttribute(ModelAttributetConstants.NEW_WISHLIST_DATA, newWishlistData1);
			final ExistingWishlistData existingWishlistData = new ExistingWishlistData();
			model.addAttribute(ModelAttributetConstants.EXISTING_WISHLIST_DATA, existingWishlistData);
			final ParticularWishlistData1 particularWishlistData1 = new ParticularWishlistData1();
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_DATA, particularWishlistData1);
			model.addAttribute(ModelAttributetConstants.RENDERING_METHOD,
					ModelAttributetConstants.RENDERING_METHOD_VIEW_PARTICULAR_WISHLIST);
			model.addAttribute(ModelAttributetConstants.SHOW_WISHLIST, ModelAttributetConstants.Y_SMALL_VAL);
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_NAME, particularWishlist.getName());
			final RemoveWishlistData removeWishlistData = new RemoveWishlistData();
			model.addAttribute(ModelAttributetConstants.REMOVE_WISHLIST_DATA, removeWishlistData);
			storeCmsPageInModel(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_WISHLIST));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			// for MSD
			final String msdjsURL = configurationService.getConfiguration().getString("msd.js.url");
			final Boolean isMSDEnabled = Boolean.valueOf(configurationService.getConfiguration().getString("msd.enabled"));
			model.addAttribute(ModelAttributetConstants.MSD_JS_URL, msdjsURL);
			model.addAttribute(ModelAttributetConstants.IS_MSD_ENABLED, isMSDEnabled);
			//End MSD
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to edit wishlist name
	 * @param newWishlistName
	 * @param oldName
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_PARTICULAR_WISHLIST_NAME, method = RequestMethod.GET)
	@ResponseBody
	public String editWishlistName(@RequestParam(ModelAttributetConstants.NEW_WISHLIST_NAME) final String newWishlistName,
			@RequestParam(ModelAttributetConstants.WISHLIST_OLD_NAME) final String oldName, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final EditWishlistNameData editWishlistNameData = new EditWishlistNameData();
			if (!(StringUtils.isEmpty(newWishlistName)) && !(StringUtils.isEmpty(oldName)))
			{
				final Wishlist2Model particularWishlistCheck = wishlistFacade.getWishlistForName(newWishlistName);
				if (null == particularWishlistCheck)
				{
					editWishlistNameData.setNewWishlistName(newWishlistName);
					editWishlistNameData.setParticularWishlistName(oldName);
					wishlistFacade.editWishlistName(editWishlistNameData);
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
							MessageConstants.SYSTEM_ERROR_WISHLIST_NAME_EDIT_SUCCESS, null);
					return ModelAttributetConstants.SUCCESS;
				}
				else
				{
					return ModelAttributetConstants.DUPLICATE_WISHLIST_NAME;
				}
			}
			else
			{
				return ModelAttributetConstants.SYSTEM_ERROR_EMPTY_WISHLIST_NAME;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description method is called to edit wishlist name
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_WISHLIST_HOME_PAGE, method = RequestMethod.GET)
	public String wishlistHomePage(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			if (!allWishlists.isEmpty())
			{
				final String nameWL = allWishlists.get(0).getName();
				return REDIRECT_PREFIX + RequestMappingUrlConstants.LINK_MY_ACCOUNT
						+ RequestMappingUrlConstants.LINK_VIEW_PARTICULAR_WISHLIST + "?particularWishlist=" + nameWL;
			}
			else
			{
				return REDIRECT_PREFIX + RequestMappingUrlConstants.LINK_MY_ACCOUNT + RequestMappingUrlConstants.LINK_WISHLIST_PAGE;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

	}


	/**
	 * @description method is called to remove product from wishlist
	 * @param wishlistName
	 * @param productCodeWl
	 * @param ussidWl
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_WISHLIST_REMOVE, method = RequestMethod.GET)
	@ResponseBody
	public String removeItemFromWL(@RequestParam(ModelAttributetConstants.WISHLIST_NAME) final String wishlistName,
			@RequestParam(ModelAttributetConstants.PRODUCTCODE_WL) final String productCodeWl,
			@RequestParam(ModelAttributetConstants.USSID_WL) final String ussidWl, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		try
		{
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d,yyyy");
			final Wishlist2Model wishlist2Model = wishlistFacade.removeProductFromWl(productCodeWl, wishlistName, ussidWl);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
					MessageConstants.SYSTEM_ERROR_WISHLIST_PRODUCT_REMOVE_SUCCESS, null);

			model.addAttribute(ModelAttributetConstants.PRODUCT_DATA, wishlist2Model);
			final List<ProductData> datas = new ArrayList<ProductData>();
			final List<WishlistProductData> wpDataList = new ArrayList<WishlistProductData>();
			final List<Wishlist2EntryModel> entryModels = wishlist2Model.getEntries();
			if (entryModels.size() >= 1)
			{
				for (final Wishlist2EntryModel entryModel : entryModels)
				{
					if (null != entryModel && null != entryModel.getProduct())
					{
						final WishlistProductData wishlistProductData = new WishlistProductData();
						final ProductData productData1 = productFacade.getProductForOptions(entryModel.getProduct(), Arrays.asList(
								ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
								ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW,
								ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.SELLER));

						datas.add(productData1);
						wishlistProductData.setProductData(productData1);

						if (null != productData1 && null != productData1.getSeller() && productData1.getSeller().size() > 0)
						{
							final List<SellerInformationData> sellerDatas = productData1.getSeller();
							for (final SellerInformationData sellerData : sellerDatas)
							{
								if (sellerData.getUssid().equals(entryModel.getUssid()))
								{
									wishlistProductData.setSellerInfoData(sellerData);
									break;
								}
							}
							wishlistProductData.setWishlistAddedDate(simpleDateFormat.format(entryModel.getAddedDate()));
						}
						wpDataList.add(wishlistProductData);
					}
				}
			}
			sessionService.setAttribute(ModelAttributetConstants.MY_WISHLIST_FLAG, ModelAttributetConstants.Y_CAPS_VAL);

			model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.Y_CAPS_VAL);
			model.addAttribute(ModelAttributetConstants.PRODUCT_DATAS, datas);
			model.addAttribute(ModelAttributetConstants.WISHLIST_PRODUCT_DATA_LIST, wpDataList);
			model.addAttribute(new WishlistData());
			model.addAttribute(new ReviewForm());
			model.addAttribute(ModelAttributetConstants.EDIT_WISHLIST_NAME_DATA, new EditWishlistNameData());
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			model.addAttribute(ModelAttributetConstants.ALL_WISHLISTS, allWishlists);
			final NewWishlistData newWishlistData1 = new NewWishlistData();
			model.addAttribute(ModelAttributetConstants.NEW_WISHLIST_DATA, newWishlistData1);
			final ExistingWishlistData existingWishlistData = new ExistingWishlistData();
			model.addAttribute(ModelAttributetConstants.EXISTING_WISHLIST_DATA, existingWishlistData);
			final ParticularWishlistData1 particularWishlistData1 = new ParticularWishlistData1();
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_DATA, particularWishlistData1);
			model.addAttribute(ModelAttributetConstants.RENDERING_METHOD,
					ModelAttributetConstants.RENDERING_METHOD_VIEW_PARTICULAR_WISHLIST);
			model.addAttribute(ModelAttributetConstants.SHOW_WISHLIST, ModelAttributetConstants.Y_SMALL_VAL);
			model.addAttribute(ModelAttributetConstants.PARTICULAR_WISHLIST_NAME, wishlist2Model.getName());
			final RemoveWishlistData removeWishlistData = new RemoveWishlistData();
			model.addAttribute(ModelAttributetConstants.REMOVE_WISHLIST_DATA, removeWishlistData);
			storeCmsPageInModel(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WISHLIST_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_WISHLIST));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			// for MSD
			final String msdjsURL = configurationService.getConfiguration().getString("msd.js.url");
			final Boolean isMSDEnabled = Boolean.valueOf(configurationService.getConfiguration().getString("msd.enabled"));
			model.addAttribute(ModelAttributetConstants.MSD_JS_URL, msdjsURL);
			model.addAttribute(ModelAttributetConstants.IS_MSD_ENABLED, isMSDEnabled);
			//End MSD
			return getViewForPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @param model
	 * @param redirectAttributes
	 * @return JSONArray
	 * @throws CMSItemNotFoundException
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_WISHLIST_FLYOUT, method = RequestMethod.GET)
	public @ResponseBody JSONArray wishlistsAndItsItems(final Model model, final RedirectAttributes redirectAttributes,
			final HttpServletRequest request) throws JSONException, CMSItemNotFoundException, UnsupportedEncodingException,
			com.granule.json.JSONException, MalformedURLException
	{
		JSONObject wishlistJson = new JSONObject();
		final JSONArray jsonArray = new JSONArray();
		MyWishListInHeaderComponentModel component;
		try
		{
			component = (MyWishListInHeaderComponentModel) cmsComponentService.getSimpleCMSComponent("MyWishList");
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final List<Wishlist2Model> allWishlistsModifiable = new ArrayList<Wishlist2Model>(allWishlists);
			final List<Wishlist2Model> latestThreeWishList = new ArrayList<Wishlist2Model>();

			if (!allWishlistsModifiable.isEmpty())
			{
				Collections.sort(allWishlistsModifiable, new AllWishListCompareByDate());

				if (allWishlistsModifiable.size() < component.getShownProductCount())
				{
					for (int i = 0; i < allWishlistsModifiable.size(); i++)
					{
						wishlistJson = new JSONObject();
						latestThreeWishList.add(allWishlistsModifiable.get(i));
						final String url = request.getContextPath() + RequestMappingUrlConstants.LINK_MY_ACCOUNT
								+ RequestMappingUrlConstants.LINK_VIEW_PARTICULAR_WISHLIST + "?particularWishlist="
								+ latestThreeWishList.get(i).getName();
						wishlistJson
								.put(ControllerConstants.Views.Fragments.Account.WishlistName, latestThreeWishList.get(i).getName());
						wishlistJson.put(ControllerConstants.Views.Fragments.Account.WishlistSize, latestThreeWishList.get(i)
								.getEntries().size());
						wishlistJson.put(ControllerConstants.Views.Fragments.Account.WishlistUrl, url);
						jsonArray.put(wishlistJson);
					}
				}
				else
				{
					for (int i = 0; i < component.getShownProductCount(); i++)
					{
						wishlistJson = new JSONObject();
						latestThreeWishList.add(allWishlistsModifiable.get(i));
						final String url = request.getContextPath() + RequestMappingUrlConstants.LINK_MY_ACCOUNT
								+ RequestMappingUrlConstants.LINK_VIEW_PARTICULAR_WISHLIST + "?particularWishlist="
								+ latestThreeWishList.get(i).getName();
						wishlistJson
								.put(ControllerConstants.Views.Fragments.Account.WishlistName, latestThreeWishList.get(i).getName());
						wishlistJson.put(ControllerConstants.Views.Fragments.Account.WishlistSize, latestThreeWishList.get(i)
								.getEntries().size());
						wishlistJson.put(ControllerConstants.Views.Fragments.Account.WishlistUrl, url);
						jsonArray.put(wishlistJson);
					}
				}

			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			wishlistJson.put(ERROR_OCCURED, ERROR_MSG);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			wishlistJson.put(ERROR_OCCURED, ERROR_MSG);
		}
		return jsonArray;
	}


	@RequestMapping(value = RequestMappingUrlConstants.ADD_TO_BAG_FROM_WL, method = RequestMethod.GET)
	public @ResponseBody String addToBagFromWl(@RequestParam(ModelAttributetConstants.USSID) final String ussid,
			@RequestParam(value = ModelAttributetConstants.ADDED_TO_CART) final boolean addedToCart, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			for (final Wishlist2Model wishlist2Model : allWishlists)
			{
				for (final Wishlist2EntryModel entryModel : wishlist2Model.getEntries())
				{
					if (entryModel.getUssid().equalsIgnoreCase(ussid))
					{
						entryModel.setAddToCartFromWl(Boolean.valueOf(addedToCart));
						modelService.save(entryModel);
						break;
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		return null;
	}

	/**
	 * @description method is called to populate product data
	 * @param productData
	 * @param model
	 */
	protected void populateProductData(final ProductData productData, final Model model)
	{
		model.addAttribute(ModelAttributetConstants.PRODUCT, productData);
	}

	/**
	 * @description method is called to load cms page for business error
	 * @param model
	 */
	private void callNonBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.BREADCRUMB_NOT_FOUND));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, MessageConstants.NON_BUSINESS_ERROR);
	}


	/**
	 * @description method is called to load cms page for non business error
	 * @param model
	 */
	private void callBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.BREADCRUMB_NOT_FOUND));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, MessageConstants.BUSINESS_ERROR);
	}


	/**
	 * @description this is called populate page for invite friends
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_FRIENDS_INVITE, method = RequestMethod.GET)
	@RequireHardLogIn
	public String friendsInvite(final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		try
		{
			final List<TitleData> titles = userFacade.getTitles();

			final CustomerData customerData = customerFacade.getCurrentCustomer();
			if (customerData.getTitleCode() != null)
			{
				model.addAttribute(ModelAttributetConstants.TITLE, findTitleForCode(titles, customerData.getTitleCode()));
			}
			if (!model.containsAttribute(ModelAttributetConstants.FRIENDS_INVITE_FORM))
			{
				final FriendsInviteForm friendsInviteForm = new FriendsInviteForm();
				model.addAttribute(ModelAttributetConstants.FRIENDS_INVITE_FORM, friendsInviteForm);
			}

			final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
			final String affiliateId = currentCustomerData.getUid();
			final String specificUrl = RequestMappingUrlConstants.LINK_LOGIN + ModelAttributetConstants.QS
					+ ModelAttributetConstants.AFFILIATEID + ModelAttributetConstants.EQUALS + affiliateId;
			final String inviteFriendUrl = urlForEmailContext(request, specificUrl);

			model.addAttribute(ModelAttributetConstants.BASE_URL, inviteFriendUrl);
			model.addAttribute(ModelAttributetConstants.AFFILIATEID, affiliateId);
			model.addAttribute(ModelAttributetConstants.CUSTOMER_DATA, customerData);
			final String messageText = configurationService.getConfiguration()
					.getString(MessageConstants.INVITE_FRIENDS_MESSAGE_KEY);
			if (!StringUtils.isEmpty(messageText))
			{
				model.addAttribute(ModelAttributetConstants.FRIENDS_TEXT_MESSAGE, messageText);
			}
			final String googleClientid = configurationService.getConfiguration().getString("google.data-clientid");
			final String facebookAppid = configurationService.getConfiguration().getString("facebook.app_id");
			model.addAttribute(ModelAttributetConstants.GOOGLECLIENTID, googleClientid);
			model.addAttribute(ModelAttributetConstants.FACEBOOKAPPID, facebookAppid);
			storeCmsPageInModel(model, getContentPageForLabelOrId(FRIENDS_INVITE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(FRIENDS_INVITE_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_FRIENDSINVITE));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return ControllerConstants.Views.Pages.Account.AccountInviteFriendPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description this is called invite friends
	 * @param emailList
	 * @param textMessage
	 * @param model
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws JSONException
	 * @throws NullPointerException
	 * @throws MalformedURLException
	 */

	@RequireHardLogIn
	@ResponseBody
	@RequestMapping(value = RequestMappingUrlConstants.LINK_INVITE_FRIENDS, method = RequestMethod.GET)
	public String inviteFriends(final Model model, final HttpServletRequest request,
			@RequestParam(ModelAttributetConstants.FRIENDS_EMAIL_LIST) final String emailList,
			@RequestParam(ModelAttributetConstants.FRIENDS_TEXT_MESSAGE) String textMessage,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, NullPointerException, JSONException,
			MalformedURLException
	{
		try
		{
			final List<String> friendsEmailList = new ArrayList<String>();

			final List email = (List) JSON.parse(emailList);
			final JSONArray emailArrayList = (JSONArray) email;
			if (emailArrayList.length() > 0)
			{
				for (int i = 0; i < emailArrayList.length(); i++)
				{
					friendsEmailList.add(emailArrayList.get(i).toString());
				}
			}

			final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
			final String affiliateId = currentCustomerData.getUid();
			final String specificUrl = RequestMappingUrlConstants.LINK_LOGIN + ModelAttributetConstants.QS
					+ ModelAttributetConstants.AFFILIATEID + ModelAttributetConstants.EQUALS + affiliateId;
			final String inviteFriendUrl = urlForEmailContext(request, specificUrl);
			String returnAction = REDIRECT_TO_INVITE_FRIENDS_PAGE;

			try
			{
				if (friendsEmailList.size() > 0)
				{
					if (!friendsInviteFacade.isEmailEqualsToCustomer(friendsEmailList))
					{
						if (!friendsInviteFacade.checkUniquenessOfEmail(friendsEmailList))
						{
							final FriendsInviteData friendsData = new FriendsInviteData();
							friendsData.setCustomerEmail(currentCustomerData.getDisplayUid());
							friendsData.setFriendsEmailList(friendsEmailList);
							friendsData.setAffiliateId(currentCustomerData.getUid());
							friendsData.setInviteBaseUrl(inviteFriendUrl);

							final boolean flag = friendsInviteFacade.inviteFriends(friendsData);

							if (flag)
							{
								if (StringUtils.isEmpty(textMessage))
								{
									textMessage = ModelAttributetConstants.FRIEND_EMAIL_MESSAGE_TEXT;
								}
								friendsInviteFacade.sendInvite(friendsData, textMessage);

								GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
										MessageConstants.ACCOUNT_CONFIRMATION_FRIENDSINVITE_INVITESENT, null);
								returnAction = ModelAttributetConstants.SUCCESS;
							}
							else
							{
								returnAction = ModelAttributetConstants.ERROR_EMAIL_SENDING;
							}
						}
						else
						{
							returnAction = ModelAttributetConstants.ALREADY_REGISTERED_EMAIL;
						}
					}
					else
					{
						returnAction = ModelAttributetConstants.CUSTOMER_EMAIL;
					}
				}
			}
			catch (final Exception e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
						MarketplacecommerceservicesConstants.E0000));
				returnAction = handlePageError(model, FRIENDS_INVITE_CMS_PAGE);
			}

			storeCmsPageInModel(model, getContentPageForLabelOrId(FRIENDS_INVITE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(FRIENDS_INVITE_CMS_PAGE));
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_FRIENDSINVITE));
			return returnAction;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @description This method is used to handle error in loading cms page
	 * @param model
	 * @param pageId
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	protected String handlePageError(final Model model, final String pageId) throws CMSItemNotFoundException
	{
		GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
		storeCmsPageInModel(model, getContentPageForLabelOrId(pageId));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(pageId));
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
				accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_PROFILE));
		return getViewForPage(model);
	}


	/**
	 *
	 * @return String
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_MARKETPLACE_PREFERENCES, method = RequestMethod.GET)
	@RequireHardLogIn
	public String marketplacePreference(final Model model,
			@RequestParam(value = ModelAttributetConstants.PARAM, required = false) final String param)
			throws CMSItemNotFoundException
	{
		try
		{
			//For populating all default brands, categories, frequencies and other details

			final MplPreferencePopulationData mplPreferencePopulationData = mplPreferenceFacade.fetchAllMplPreferenceContents();

			if (null != mplPreferencePopulationData)
			{
				final List<CategoryModel> preferredCategoryList = mplPreferencePopulationData.getPreferredCategory();
				final List<CategoryModel> preferredBrandList = mplPreferencePopulationData.getPreferredBrand();
				final List<String> frequencyList = mplPreferencePopulationData.getFrequencyList();
				final List<String> feedbackAreaList = mplPreferencePopulationData.getFeedbackAreaList();
				final List<String> interestList = mplPreferencePopulationData.getInterestList();

				final List<CategoryData> categoryDataList = new ArrayList<CategoryData>();
				final List<CategoryData> categoryDataBrandList = new ArrayList<CategoryData>();
				if (preferredCategoryList.size() > 0)
				{
					for (final CategoryModel category : preferredCategoryList)
					{
						final CategoryData categoryData = new CategoryData();
						categoryData.setCode(category.getCode());
						categoryData.setName(category.getName());
						categoryDataList.add(categoryData);
					}
				}
				if (preferredBrandList.size() > 0)
				{
					for (final CategoryModel categoryBrand : preferredBrandList)
					{
						final CategoryData categoryData = new CategoryData();
						categoryData.setCode(categoryBrand.getCode());
						categoryData.setName(categoryBrand.getName());
						categoryDataBrandList.add(categoryData);
					}
				}

				//Fetching pre selected brands, categories, frequencies and other details (if any)

				final MplPreferenceData mplPreferenceData = mplPreferenceFacade.fetchPresavedFavouritePreferences();
				final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
				final MarketplacePreferenceModel mplPreferenceModel = currentCustomer.getMarketplacepreference();
				if (null != mplPreferenceModel)
				{
					model.addAttribute(ModelAttributetConstants.IS_ALREADY_SUBSCRIBED, ModelAttributetConstants.TRUE);
				}
				else
				{
					model.addAttribute(ModelAttributetConstants.IS_ALREADY_SUBSCRIBED, ModelAttributetConstants.FALSE);
				}

				if (null != mplPreferenceData)
				{
					List<String> selectedfeedbackAreaList = new ArrayList<String>();
					//	set category field
					if (null != mplPreferenceData.getSelectedCategory() && mplPreferenceData.getSelectedCategory().size() > 0)
					{
						model.addAttribute(ModelAttributetConstants.SELECTED_CATEGORY_LIST, mplPreferenceData.getSelectedCategory());
					}

					//	set brand field
					if (null != mplPreferenceData.getSelectedBrand() && mplPreferenceData.getSelectedBrand().size() > 0)
					{
						model.addAttribute(ModelAttributetConstants.SELECTED_BRAND_LIST, mplPreferenceData.getSelectedBrand());
					}

					//	set my interest field
					if (!StringUtils.isEmpty(mplPreferenceData.getMyInterest()))
					{
						model.addAttribute(ModelAttributetConstants.SELECTED_INTEREST_RADIO, mplPreferenceData.getMyInterest());
					}
					else
					{
						model.addAttribute(ModelAttributetConstants.SELECTED_INTEREST_RADIO,
								ModelAttributetConstants.INTERESTED_IN_EMAIL);
					}

					//	set frequency field
					if (!StringUtils.isEmpty(mplPreferenceData.getSelectedFrequency()))
					{
						model.addAttribute(ModelAttributetConstants.SELECTED_FREQ, mplPreferenceData.getSelectedFrequency());
					}
					else
					{
						model.addAttribute(ModelAttributetConstants.SELECTED_FREQ, Frequency.WEEKLY.toString());
					}

					//	set feedback field
					if (null != mplPreferenceModel)
					{
						if (mplPreferenceData.isFeedbackCustomerSurveys() && !mplPreferenceData.isFeedbackUserReview())
						{
							selectedfeedbackAreaList.add(convertFeedbackEnumToStr(FeedbackArea.CONSUMER_SURVEYS.toString()));
						}
						else if (!mplPreferenceData.isFeedbackCustomerSurveys() && mplPreferenceData.isFeedbackUserReview())
						{
							selectedfeedbackAreaList.add(convertFeedbackEnumToStr(FeedbackArea.USER_REVIEWS.toString()));
						}
						else if (mplPreferenceData.isFeedbackCustomerSurveys() && mplPreferenceData.isFeedbackUserReview())
						{
							selectedfeedbackAreaList.add(convertFeedbackEnumToStr(FeedbackArea.CONSUMER_SURVEYS.toString()));
							selectedfeedbackAreaList.add(convertFeedbackEnumToStr(FeedbackArea.USER_REVIEWS.toString()));
						}
						else if (!mplPreferenceData.isFeedbackCustomerSurveys() && !mplPreferenceData.isFeedbackUserReview())
						{
							selectedfeedbackAreaList = new ArrayList<String>();
						}
					}
					else
					{
						selectedfeedbackAreaList.add(convertFeedbackEnumToStr(FeedbackArea.CONSUMER_SURVEYS.toString()));
						selectedfeedbackAreaList.add(convertFeedbackEnumToStr(FeedbackArea.USER_REVIEWS.toString()));
					}
					model.addAttribute(ModelAttributetConstants.SELECTED_FEEDBACK_LIST, selectedfeedbackAreaList);
				}

				model.addAttribute(ModelAttributetConstants.CATEGORY_LIST, categoryDataList);
				model.addAttribute(ModelAttributetConstants.BRAND_LIST, categoryDataBrandList);
				model.addAttribute(ModelAttributetConstants.FREQUENCY_LIST, frequencyList);
				model.addAttribute(ModelAttributetConstants.FEEDBACK_AREA_LIST, feedbackAreaList);
				model.addAttribute(ModelAttributetConstants.INTEREST_LIST, interestList);
			}

			if (StringUtils.isNotEmpty(param) && param.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
			{
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.SUCCESS);
			}
			else if (StringUtils.isNotEmpty(param) && param.equalsIgnoreCase(ModelAttributetConstants.UNSUBSCRIBED))
			{
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.UNSUBSCRIBED);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.EMPTY);
			}
			storeCmsPageInModel(model, getContentPageForLabelOrId(MARKETPLACE_PREFERENCE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MARKETPLACE_PREFERENCE));

			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.MARKETPLACE_PREFERENCE));


			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			//			storeContentPageTitleInModel(model, MessageConstants.MARKETPLACE_PREFERENCE);
			return ControllerConstants.Views.Pages.Account.AccountMplPreferencePage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 *
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_SAVE_MPLPREFERENCES, method = RequestMethod.GET)
	@RequireHardLogIn
	@ResponseBody
	public String saveMarketplacePreference(final Model model,
			@RequestParam(value = ModelAttributetConstants.INTEREST, required = false) final String interest,
			@RequestParam(value = ModelAttributetConstants.CATEGORY_DATA, required = false) final String categoryData,
			@RequestParam(value = ModelAttributetConstants.BRAND_DATA, required = false) final String brandData,
			@RequestParam(value = ModelAttributetConstants.FREQUENCY, required = false) final String frequency,
			@RequestParam(value = ModelAttributetConstants.FEEDBACK_AREA, required = false) final String feedBackArea,
			@RequestParam(value = ModelAttributetConstants.IS_UNSUBSCIBED, required = false) final String isUnsubscibed)
			throws CMSItemNotFoundException, JSONException
	{
		try
		{
			final MplPreferenceData mplPreferenceData = new MplPreferenceData();
			final List<String> selectedCategory = new ArrayList<String>();
			final List<String> selectedBrand = new ArrayList<String>();
			final List<String> selectedFeedBackArea = new ArrayList<String>();

			final List category = (List) JSON.parse(categoryData);
			final JSONArray categoryList = (JSONArray) category;
			if (categoryList.length() > 0)
			{
				for (int i = 0; i < categoryList.length(); i++)
				{
					selectedCategory.add(categoryList.get(i).toString());
				}
			}

			final List brand = (List) JSON.parse(brandData);
			final JSONArray brandList = (JSONArray) brand;
			if (brandList.length() > 0)
			{
				for (int i = 0; i < brandList.length(); i++)
				{
					selectedBrand.add(brandList.get(i).toString());
				}
			}

			final List feedback = (List) JSON.parse(feedBackArea);
			final JSONArray feedBackAreaList = (JSONArray) feedback;
			if (feedBackAreaList.length() > 0)
			{
				for (int i = 0; i < feedBackAreaList.length(); i++)
				{
					selectedFeedBackArea.add(feedBackAreaList.get(i).toString());
				}
			}


			if (!StringUtils.isEmpty(interest) && interest.equalsIgnoreCase(ModelAttributetConstants.INTERESTED_IN_EMAIL))
			{
				mplPreferenceData.setMyInterest(interest);
			}
			else if (!StringUtils.isEmpty(interest) && interest.equalsIgnoreCase(ModelAttributetConstants.NOT_INTERESTED_IN_EMAIL))
			{
				mplPreferenceData.setMyInterest(ModelAttributetConstants.NOT_INTERESTED_IN_EMAIL);
			}
			mplPreferenceData.setSelectedCategory(selectedCategory);
			mplPreferenceData.setSelectedBrand(selectedBrand);
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final MarketplacePreferenceModel mplPreferenceModel = currentCustomer.getMarketplacepreference();
			if (selectedFeedBackArea.size() > 0)
			{
				if (selectedFeedBackArea.size() == 1)
				{
					if (selectedFeedBackArea.get(0).equalsIgnoreCase(convertFeedbackEnumToStr(FeedbackArea.USER_REVIEWS.toString())))
					{
						mplPreferenceData.setFeedbackUserReview(Boolean.TRUE);
						mplPreferenceData.setFeedbackCustomerSurveys(Boolean.FALSE);
					}
					else if (selectedFeedBackArea.get(0).equalsIgnoreCase(
							convertFeedbackEnumToStr(FeedbackArea.CONSUMER_SURVEYS.toString())))
					{
						mplPreferenceData.setFeedbackUserReview(Boolean.FALSE);
						mplPreferenceData.setFeedbackCustomerSurveys(Boolean.TRUE);
					}
				}
				else if (selectedFeedBackArea.size() == 2)
				{
					mplPreferenceData.setFeedbackCustomerSurveys(Boolean.TRUE);
					mplPreferenceData.setFeedbackUserReview(Boolean.TRUE);
				}
			}
			else
			{
				if (null != mplPreferenceModel)
				{
					mplPreferenceData.setFeedbackUserReview(Boolean.FALSE);
					mplPreferenceData.setFeedbackCustomerSurveys(Boolean.FALSE);
				}
				else
				{
					mplPreferenceData.setFeedbackUserReview(Boolean.TRUE);
					mplPreferenceData.setFeedbackCustomerSurveys(Boolean.TRUE);
				}
			}

			if (!StringUtils.isEmpty(frequency))
			{
				mplPreferenceData.setSelectedFrequency(frequency);
			}

			if (StringUtils.isNotEmpty(isUnsubscibed) && isUnsubscibed.equalsIgnoreCase(ModelAttributetConstants.TRUE))
			{
				mplPreferenceData.setMyInterest(ModelAttributetConstants.NOT_INTERESTED_IN_EMAIL);
				mplPreferenceData.setSelectedCategory(new ArrayList<String>());
				mplPreferenceData.setSelectedBrand(new ArrayList<String>());
				mplPreferenceData.setFeedbackCustomerSurveys(Boolean.FALSE);
				mplPreferenceData.setFeedbackUserReview(Boolean.FALSE);
				mplPreferenceData.setSelectedFrequency(Frequency.WEEKLY.toString());
				mplPreferenceFacade.unsubscribeAllPreferences(mplPreferenceData);
				return ModelAttributetConstants.UNSUBSCRIBED;
			}
			else
			{
				mplPreferenceFacade.saveMarketplacePreference(mplPreferenceData);
			}

			return ModelAttributetConstants.SUCCESS;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 *
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(value = RequestMappingUrlConstants.LINK_UNSUBSCRIBE_MPLPREFERENCES, method = RequestMethod.GET)
	@RequireHardLogIn
	public String unsubscribeMarketplacePreference(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final MplPreferenceData mplPreferenceData = new MplPreferenceData();
			final List<String> selectedCategory = new ArrayList<String>();
			final List<String> selectedBrand = new ArrayList<String>();

			mplPreferenceData.setMyInterest(ModelAttributetConstants.NOT_INTERESTED_IN_EMAIL);
			mplPreferenceData.setSelectedCategory(selectedCategory);
			mplPreferenceData.setSelectedBrand(selectedBrand);
			mplPreferenceData.setFeedbackCustomerSurveys(Boolean.FALSE);
			mplPreferenceData.setFeedbackUserReview(Boolean.FALSE);
			mplPreferenceData.setSelectedFrequency(Frequency.WEEKLY.toString());

			mplPreferenceFacade.saveMarketplacePreference(mplPreferenceData);

			return REDIRECT_MY_ACCOUNT + RequestMappingUrlConstants.LINK_MARKETPLACE_PREFERENCES;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @param enumVal
	 */
	private String convertFeedbackEnumToStr(final String enumVal)
	{
		final String enumCode = enumVal.replace(ModelAttributetConstants.UNDER_SCORE, ModelAttributetConstants.SINGLE_SPACE);
		return enumCode;
	}

	/**
	 * @Description:For My Recommendations
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST, method = RequestMethod.GET)
	@RequireHardLogIn
	public String myInterest(
			@RequestParam(value = ModelAttributetConstants.AUTOMATE, defaultValue = ModelAttributetConstants.FALSE) final String automate,
			final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final List<GenderData> genderDataList = mplCustomerProfileFacade.getGenders();
			final List<String> genderList = new ArrayList<String>();
			boolean isStyleProfileCreated = false;

			isStyleProfileCreated = myStyleProfileFacade.isStyleProfileCreated();

			if (!isStyleProfileCreated)
			{
				if (null != genderDataList && !genderDataList.isEmpty())
				{
					for (final GenderData gender : genderDataList)
					{
						if (gender.getCode().equalsIgnoreCase(Gender.MALE.toString()))
						{
							genderList.add(0, ModelAttributetConstants.MALE);
						}
						else if (gender.getCode().equalsIgnoreCase(Gender.FEMALE.toString()))
						{
							genderList.add(1, ModelAttributetConstants.FEMALE);
						}
					}
				}
				model.addAttribute("genderList", genderList);
			}

			/*
			 * get the selected categories if automation is set
			 */
			if (ModelAttributetConstants.TRUE.equalsIgnoreCase(automate))
			{
				List<CategoryModel> prevSelectedCats = new ArrayList<CategoryModel>();
				List<CategoryModel> prevSelectedBrands = new ArrayList<CategoryModel>();

				prevSelectedCats = myStyleProfileFacade.getInterstedCategories();
				prevSelectedBrands = myStyleProfileFacade.getInterstedBrands();

				model.addAttribute("prevSelectedCats", prevSelectedCats);
				model.addAttribute("prevSelectedBrands", prevSelectedBrands);

				storeCmsPageInModel(model, getContentPageForLabelOrId(MY_INTEREST));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_INTEREST));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.MY_QUESTIONARE));

				/*
				 * model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
				 * accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.MARKETPLACE_PREFERENCE));
				 */
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				storeContentPageTitleInModel(model, MessageConstants.MY_QUESTIONARE);
				return ControllerConstants.Views.Pages.Account.AccountMyInterestPage;
			}
			else
			{

				//For Fetching Selected Data
				List<CategoryModel> interestedCategories = new ArrayList<CategoryModel>();
				final Map<String, CategoryData> categoryDataMap = new HashMap<String, CategoryData>();
				List<CategoryModel> interestedBrands = new ArrayList<CategoryModel>();
				final Map<String, CategoryData> brandDataMap = new HashMap<String, CategoryData>();
				final List<String> preferredCategoryList = new ArrayList<String>();
				String customer = "";

				interestedCategories = myStyleProfileFacade.getInterstedCategories();
				final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
				if (null != interestedCategories && !interestedCategories.isEmpty())
				{
					for (final CategoryModel categoryLineItem : interestedCategories)
					{
						final CategoryData selCategoryData = new CategoryData();
						preferredCategoryList.add(categoryLineItem.getName());
						selCategoryData.setCode(categoryLineItem.getCode());
						selCategoryData.setName(categoryLineItem.getName());
						// Media needs to be set for Pic Display
						categoryDataMap.put(categoryLineItem.getCode(), selCategoryData);
					}
				}

				if (null != currentCustomerData)
				{
					if (null != currentCustomerData.getFirstName())
					{
						customer = currentCustomerData.getFirstName();
					}
					else if (null != currentCustomerData.getDisplayUid())
					{
						customer = currentCustomerData.getDisplayUid();
					}

				}

				interestedBrands = myStyleProfileFacade.getInterstedBrands();

				if (null != interestedBrands && !interestedBrands.isEmpty())
				{
					for (final CategoryModel categoryLineItem : interestedBrands)
					{
						final CategoryData selCategoryData = new CategoryData();
						preferredCategoryList.add(categoryLineItem.getName());
						selCategoryData.setCode(categoryLineItem.getCode());
						selCategoryData.setName(categoryLineItem.getName());
						// Media needs to be set for Pic Display
						brandDataMap.put(categoryLineItem.getCode(), selCategoryData);
					}
				}
				//For Fetching Selected Data Ends
				model.addAttribute("categoryDataMap", categoryDataMap);
				model.addAttribute("brandDataMap", brandDataMap);
				model.addAttribute("customer", customer);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MY_STYLE_PROFILE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_STYLE_PROFILE));
				model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
						resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.MY_STYLE_PROFILE));
				model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
				storeContentPageTitleInModel(model, MessageConstants.MY_STYLE_PROFILE);
				if (!isStyleProfileCreated)
				{
					return ControllerConstants.Views.Pages.Account.AccountMyInterestPage;
				}
				else
				{
					return "redirect:myStyleProfile";//ControllerConstants.Views.Pages.Account.AccountMyStyleProfilePage;
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}


	/**
	 * @Description : Populate Data corresponding to gender
	 * @param model
	 * @return categoryDataMap
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_GENDER, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, CategoryData> myInterestForCategory(
			@RequestParam(value = ModelAttributetConstants.GENDERDATA) final String genderData,
			@RequestParam(value = ModelAttributetConstants.AUTOMATE, defaultValue = ModelAttributetConstants.FALSE) final String automate,
			final Model model) throws CMSItemNotFoundException, NullPointerException
	{
		final Map<String, CategoryData> categoryDataMap = new HashMap<String, CategoryData>();
		try
		{
			List<MyRecommendationsConfigurationModel> myRecommendedData = new ArrayList<MyRecommendationsConfigurationModel>();
			if (null != genderData && genderData.length() > 0)
			{
				if (ModelAttributetConstants.FALSE.equalsIgnoreCase(automate))
				{
					saveGenderData(genderData);
				}
				myRecommendedData = myStyleProfileFacade.fetchRecommendedData(genderData);
				if (null != myRecommendedData && !myRecommendedData.isEmpty())
				{
					for (final MyRecommendationsConfigurationModel oModel : myRecommendedData)
					{
						if (oModel.getGenderData().toString().equalsIgnoreCase(genderData)
								|| oModel.getGenderData().toString().equalsIgnoreCase(ModelAttributetConstants.ALL))
						{
							final CategoryData categoryData = new CategoryData();
							categoryData.setCode(oModel.getConfiguredCategory().getCode());
							categoryData.setName(oModel.getConfiguredCategory().getName());
							categoryDataMap.put(oModel.getConfiguredCategory().getCode(), categoryData);
						}
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		return categoryDataMap;
	}




	/**
	 * @Desc get all brands for selected category
	 * @param model
	 * @return categoryDataMap
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 * @throws JSONException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_BRANDS, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, CategoryData> myInterestForBrand(
			@RequestParam(value = ModelAttributetConstants.CATEGORYDATA) final String categoryData,
			@RequestParam(value = "modify", defaultValue = ModelAttributetConstants.TRUE) final String modify, final Model model)
			throws CMSItemNotFoundException, NullPointerException, JSONException
	{
		final Map<String, CategoryData> categoryDataMap = new HashMap<String, CategoryData>();
		final List<String> categoryListData = new ArrayList<String>();
		try
		{
			final String genderData = fetchGenderData();
			List<MyRecommendationsConfigurationModel> myRecommendedData = new ArrayList<MyRecommendationsConfigurationModel>();


			final List category = (List) JSON.parse(categoryData);
			final List<String> categoryJSONData = new ArrayList<String>();
			final JSONArray recs = (JSONArray) category;

			for (int i = 0; i < recs.length(); i++)
			{
				final String code = recs.get(i).toString();
				LOG.debug("Category Code :" + code);
				categoryJSONData.add(code);
			}

			if (!categoryJSONData.isEmpty())
			{
				for (final String data : categoryJSONData)
				{
					categoryListData.add(data);
					myRecommendedData = myStyleProfileFacade.fetchBrands(genderData, data);
					if (null != myRecommendedData && !myRecommendedData.isEmpty())
					{
						for (final MyRecommendationsConfigurationModel oModel : myRecommendedData)
						{
							if (oModel.getConfiguredBrandsNew() != null)
							{
								for (final MyRecommendationsBrandsModel brand : oModel.getConfiguredBrandsNew())
								{
									final CategoryModel catModel = brand.getConfiguredBrands();
									final CategoryData oData = new CategoryData();
									oData.setCode(catModel.getCode());
									oData.setName(catModel.getName());
									categoryDataMap.put(catModel.getCode(), oData);
								}
							}
						}
					}
				}
			}

			if (!categoryListData.isEmpty() && categoryListData.size() > 0 && ModelAttributetConstants.TRUE.equalsIgnoreCase(modify))
			{
				saveCategoryData(categoryListData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		return categoryDataMap;
	}

	/**
	 * @Desc get all brands for selected category
	 * @param model
	 * @return categoryDataMap
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 * @throws JSONException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_SUBCATEGORIES, method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, CategoryData>> getBrandSubCategory(
			@SuppressWarnings(UNUSED) @RequestParam(value = ModelAttributetConstants.CATEGORYDATA, required = false) final String categoryData,
			@RequestParam(value = "subCategoryData") final String subCategoryData,
			@RequestParam(value = "selectedCategory") final String selectedCategory, final Model model)
			throws CMSItemNotFoundException, NullPointerException, JSONException
	{

		final List<Map<String, CategoryData>> categoryDataMapList = new ArrayList<Map<String, CategoryData>>();

		final Map<String, CategoryData> categoryDataMap = new HashMap<String, CategoryData>();
		Map<String, CategoryData> categoryDataMapApparel = null;
		Map<String, CategoryData> categoryDataMapElectronics = null;

		final List<String> brandListData = new ArrayList<String>();
		final List category = (List) JSON.parse(subCategoryData);
		final List<String> categoryJSONData = new ArrayList<String>();
		final JSONArray recs = (JSONArray) category;
		for (int i = 0; i < recs.length(); i++)
		{
			final String code = recs.get(i).toString();
			categoryJSONData.add(code);
		}


		try
		{
			//final String gender = fetchGenderData();

			if (selectedCategory.equalsIgnoreCase("both"))
			{
				categoryDataMapApparel = new HashMap<String, CategoryData>();
				categoryDataMapElectronics = new HashMap<String, CategoryData>();

				final String apparelCode = ModelAttributetConstants.APPAREL_CODE;
				final String electronicCode = ModelAttributetConstants.ELECTRONIC_CODE;
				/**
				 * getting apparel first
				 */
				final Map<String, CategoryData> allBrandsForApparel = myInterestForBrand(apparelCode, ModelAttributetConstants.FALSE,
						model);
				final Set<String> allApparelBrandsSet = allBrandsForApparel.keySet();

				for (final String allBrandCode : allApparelBrandsSet)
				{
					for (final String catCode : categoryJSONData)
					{
						if (catCode.equalsIgnoreCase(allBrandCode))
						{
							List<MyRecommendationsBrandsModel> myRecommendedData = new ArrayList<MyRecommendationsBrandsModel>();
							myRecommendedData = myStyleProfileFacade.fetchSubCatdOfBrands(catCode);

							if (null != myRecommendedData && !myRecommendedData.isEmpty())
							{
								for (final MyRecommendationsBrandsModel oModel : myRecommendedData)
								{
									if (oModel.getSubCategories() != null)
									{
										final List<CategoryModel> subCats = (List<CategoryModel>) oModel.getSubCategories();
										for (final CategoryModel catModel : subCats)
										{
											final CategoryData oData = new CategoryData();
											oData.setCode(catModel.getCode());
											oData.setName(catModel.getName());
											categoryDataMapApparel.put(catModel.getCode(), oData);
										}
									}
								}
							}
						}
					}
				}
				categoryDataMapList.add(categoryDataMapApparel);

				/**
				 * getting electronics second
				 */
				final Map<String, CategoryData> allBrandsForElectronics = myInterestForBrand(electronicCode,
						ModelAttributetConstants.FALSE, model);
				final Set<String> allElectronicsBrandsSet = allBrandsForElectronics.keySet();

				for (final String allBrandCode : allElectronicsBrandsSet)
				{
					for (final String catCode : categoryJSONData)
					{
						if (catCode.equalsIgnoreCase(allBrandCode))
						{
							List<MyRecommendationsBrandsModel> myRecommendedData = new ArrayList<MyRecommendationsBrandsModel>();
							myRecommendedData = myStyleProfileFacade.fetchSubCatdOfBrands(catCode);

							if (null != myRecommendedData && !myRecommendedData.isEmpty())
							{
								for (final MyRecommendationsBrandsModel oModel : myRecommendedData)
								{
									if (oModel.getSubCategories() != null)
									{
										final List<CategoryModel> subCats = (List<CategoryModel>) oModel.getSubCategories();
										for (final CategoryModel catModel : subCats)
										{
											final CategoryData oData = new CategoryData();
											oData.setCode(catModel.getCode());
											oData.setName(catModel.getName());
											categoryDataMapElectronics.put(catModel.getCode(), oData);
										}
									}
								}
							}
						}
					}
				}
				categoryDataMapList.add(categoryDataMapElectronics);
			}
			else
			{
				for (final String catCode : categoryJSONData)
				{
					List<MyRecommendationsBrandsModel> myRecommendedData = new ArrayList<MyRecommendationsBrandsModel>();
					myRecommendedData = myStyleProfileFacade.fetchSubCatdOfBrands(catCode);

					if (null != myRecommendedData && !myRecommendedData.isEmpty())
					{
						for (final MyRecommendationsBrandsModel oModel : myRecommendedData)
						{
							if (oModel.getSubCategories() != null)
							{
								final List<CategoryModel> subCats = (List<CategoryModel>) oModel.getSubCategories();
								for (final CategoryModel catModel : subCats)
								{
									final CategoryData oData = new CategoryData();
									oData.setCode(catModel.getCode());
									oData.setName(catModel.getName());
									categoryDataMap.put(catModel.getCode(), oData);
								}
							}
						}
					}
				}
				categoryDataMapList.add(categoryDataMap);
			}

			//final String genderData = fetchGenderData();
			if (!categoryJSONData.isEmpty())
			{
				for (final String data : categoryJSONData)
				{
					brandListData.add(data);
				}
			}

			if (!brandListData.isEmpty() && brandListData.size() > 0)
			{
				saveBrandData(brandListData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		return categoryDataMapList;
	}

	/**
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = RequestMappingUrlConstants.MY_STYLE_PROFILE, method = RequestMethod.GET)
	@RequireHardLogIn
	public String myStyleProfile(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			//For Fetching Selected Data
			List<CategoryModel> interestedCategories = new ArrayList<CategoryModel>();
			final Map<String, CategoryData> categoryDataMap = new HashMap<String, CategoryData>();
			List<CategoryModel> interestedBrands = new ArrayList<CategoryModel>();
			final Map<String, CategoryData> brandDataMap = new HashMap<String, CategoryData>();
			final List<String> preferredCategoryList = new ArrayList<String>();
			String customer = "";
			//get the gender data
			final String gender = myStyleProfileFacade.fetchGenderData();
			//get all categories in array
			final JSONArray jsoncatArray = new JSONArray();

			interestedCategories = myStyleProfileFacade.getInterstedCategories();
			final CustomerData currentCustomerData = customerFacade.getCurrentCustomer();
			if (null != interestedCategories && !interestedCategories.isEmpty())
			{
				for (final CategoryModel categoryLineItem : interestedCategories)
				{
					final CategoryData selCategoryData = new CategoryData();
					preferredCategoryList.add(categoryLineItem.getName());
					selCategoryData.setCode(categoryLineItem.getCode());
					jsoncatArray.add(categoryLineItem.getCode());
					selCategoryData.setName(categoryLineItem.getName());
					// Media needs to be set for Pic Display
					categoryDataMap.put(categoryLineItem.getCode(), selCategoryData);
				}
			}

			if (null != currentCustomerData)
			{
				if (null != currentCustomerData.getFirstName())
				{
					customer = currentCustomerData.getFirstName();
				}
				else if (null != currentCustomerData.getDisplayUid())
				{
					customer = currentCustomerData.getDisplayUid();
				}
			}

			interestedBrands = myStyleProfileFacade.getInterstedBrands();

			if (null != interestedBrands && !interestedBrands.isEmpty())
			{
				for (final CategoryModel categoryLineItem : interestedBrands)
				{
					final CategoryData selCategoryData = new CategoryData();
					preferredCategoryList.add(categoryLineItem.getName());
					selCategoryData.setCode(categoryLineItem.getCode());
					selCategoryData.setName(categoryLineItem.getName());
					//Media needs to be set for Pic Display
					brandDataMap.put(categoryLineItem.getCode(), selCategoryData);
				}
			}


			//For Fetching Selected Data Ends
			model.addAttribute("categoryDataMap", categoryDataMap);
			model.addAttribute("brandDataMap", brandDataMap);
			model.addAttribute("customer", customer);
			model.addAttribute("gender", gender);
			model.addAttribute("categoryIds", URLEncoder.encode(jsoncatArray.toString()));
			final boolean isStyleProfileCreated = myStyleProfileFacade.isStyleProfileCreated();

			storeCmsPageInModel(model, getContentPageForLabelOrId(MY_STYLE_PROFILE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MY_STYLE_PROFILE));


			model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
					resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.MY_STYLE_PROFILE));
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			storeContentPageTitleInModel(model, MessageConstants.MY_STYLE_PROFILE);
			if (!isStyleProfileCreated)
			{
				//+ ControllerConstants.Views.Pages.Account.AccountMyInterestPage
				return "redirect:myInterest";
			}
			else
			{
				return ControllerConstants.Views.Pages.Account.AccountMyStyleProfilePage;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}


	/**
	 * @Description : Save Brand Data
	 * @param brandListData
	 */
	private void saveBrandData(final List<String> brandListData)
	{
		final MyStyleProfileData styleProfileData = new MyStyleProfileData();
		if (null != brandListData && !brandListData.isEmpty())
		{
			styleProfileData.setCategoryCodeList(brandListData);
			myStyleProfileFacade.saveBrandData(styleProfileData);
		}

	}

	/**
	 * @Description : Save Category Data
	 * @param categoryListData
	 */
	private void saveCategoryData(final List<String> categoryListData)
	{
		final MyStyleProfileData styleProfileData = new MyStyleProfileData();
		if (null != categoryListData && !categoryListData.isEmpty())
		{
			styleProfileData.setCategoryCodeList(categoryListData);
			myStyleProfileFacade.saveCategoryData(styleProfileData);
		}

	}


	/**
	 * @Description : Save Gender Data
	 * @param genderData
	 */
	private void saveGenderData(final String genderData)
	{
		if (null != genderData && StringUtils.isNotBlank(genderData.trim()))
		{
			myStyleProfileFacade.saveGenderData(genderData);
		}

	}

	/**
	 * @Description: To fetch Gender Data
	 * @return gender
	 */
	private String fetchGenderData()
	{
		String gender = ModelAttributetConstants.ALL;
		String genderData = "";

		genderData = myStyleProfileFacade.fetchGenderData();
		if (null != genderData && genderData.length() > 0)
		{
			gender = genderData;
		}

		return gender;
	}


	/**
	 * @Description: Save the selected sub-categories
	 * @param categoryData
	 * @param model
	 * @return categoryDataMap
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 * @throws JSONException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_SEL_SUBCATEGORIES, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, CategoryData> saveSubCategoryData(
			@RequestParam(value = ModelAttributetConstants.CATEGORYDATA) final String categoryData, final Model model)
			throws CMSItemNotFoundException, NullPointerException, JSONException
	{

		final Map<String, CategoryData> categoryDataMap = new HashMap<String, CategoryData>();
		try
		{

			final List category = (List) JSON.parse(categoryData);
			final List<String> categoryJSONData = new ArrayList<String>();
			final JSONArray recs = (JSONArray) category;

			for (int i = 0; i < recs.length(); i++)
			{
				final String code = recs.get(i).toString();
				categoryJSONData.add(code);
			}
			if (!categoryJSONData.isEmpty())
			{
				saveSubCategoryData(categoryJSONData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		return categoryDataMap;

	}

	/**
	 * @Description complete removal of style profile
	 * @return boolean
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_REMOVE_PROFILE, method = RequestMethod.GET)
	@ResponseBody
	public boolean removeMyStyleProfile() throws CMSItemNotFoundException
	{
		return myStyleProfileFacade.removeMyStyleProfile();
	}

	/**
	 * @Description removal of brand
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_REMOVE_BRAND, method = RequestMethod.GET)
	@ResponseBody
	public void removeSingleBrand(@RequestParam(value = ModelAttributetConstants.CATEGORYDATA) final String categoryData,
			final Model model) throws CMSItemNotFoundException, NullPointerException, JSONException
	{
		try
		{
			final List<String> brandListData = new ArrayList<String>();

			final List category = (List) JSON.parse(categoryData);
			final List<String> categoryJSONData = new ArrayList<String>();
			final JSONArray recs = (JSONArray) category;

			for (int i = 0; i < recs.length(); i++)
			{
				final String code = recs.get(i).toString();
				LOG.debug("Category Code :" + code);
				categoryJSONData.add(code);
			}

			if (!categoryJSONData.isEmpty())
			{
				for (final String data : categoryJSONData)
				{
					brandListData.add(data);
				}
			}
			final MyStyleProfileData styleProfileData = new MyStyleProfileData();
			if (!brandListData.isEmpty())
			{
				styleProfileData.setCategoryCodeList(brandListData);
				myStyleProfileFacade.removeSingleBrand(styleProfileData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @Description removal of category
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_REMOVE_CATEGORY, method = RequestMethod.GET)
	@ResponseBody
	public void removeSingleCategory(@RequestParam(value = ModelAttributetConstants.CATEGORYDATA) final String categoryData,
			@RequestParam(value = ModelAttributetConstants.BRAND_DATA) final String brandData, final Model model)
			throws CMSItemNotFoundException, NullPointerException, JSONException
	{
		try
		{
			final List category = (List) JSON.parse(categoryData);
			final List brand = (List) JSON.parse(brandData);

			final List<String> categoryJSONData = new ArrayList<String>();
			final List<String> brandJSONData = new ArrayList<String>();

			final JSONArray recsCat = (JSONArray) category;

			for (int i = 0; i < recsCat.length(); i++)
			{
				final String code = recsCat.get(i).toString();
				categoryJSONData.add(code);
			}

			final JSONArray recsBrand = (JSONArray) brand;

			for (int i = 0; i < recsBrand.length(); i++)
			{
				final String code = recsBrand.get(i).toString();
				brandJSONData.add(code);
			}

			final MyStyleProfileData styleProfileDataCat = new MyStyleProfileData();
			final MyStyleProfileData styleProfileDataBrand = new MyStyleProfileData();

			if (!categoryJSONData.isEmpty() && !brandJSONData.isEmpty())
			{
				styleProfileDataCat.setCategoryCodeList(categoryJSONData);
				styleProfileDataBrand.setCategoryCodeList(brandJSONData);

				myStyleProfileFacade.removeSingleCategory(styleProfileDataCat, styleProfileDataBrand);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}


	/**
	 * @Description modification of brand
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_MODIFY_BRAND, method = RequestMethod.GET)
	@ResponseBody
	public void modifyBrand(@RequestParam(value = ModelAttributetConstants.CATEGORYDATA) final String categoryData,
			final Model model) throws CMSItemNotFoundException, NullPointerException, JSONException
	{
		try
		{
			final List<String> brandListData = new ArrayList<String>();

			final List category = (List) JSON.parse(categoryData);
			final List<String> categoryJSONData = new ArrayList<String>();
			final JSONArray recs = (JSONArray) category;

			for (int i = 0; i < recs.length(); i++)
			{
				final String code = recs.get(i).toString();
				LOG.debug("Category Code :" + code);
				categoryJSONData.add(code);
			}

			if (!categoryJSONData.isEmpty())
			{
				for (final String data : categoryJSONData)
				{
					brandListData.add(data);
				}
			}
			final MyStyleProfileData styleProfileData = new MyStyleProfileData();
			if (!brandListData.isEmpty())
			{
				styleProfileData.setCategoryCodeList(brandListData);
				myStyleProfileFacade.modifyBrand(styleProfileData);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @Description modify of category
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.MY_INTEREST_MODIFY_CATEGORY, method = RequestMethod.GET)
	@ResponseBody
	public void modifyCategory(@RequestParam(value = ModelAttributetConstants.CATEGORYDATA) final String categoryData,
			final Model model) throws CMSItemNotFoundException, NullPointerException, JSONException
	{
		try
		{
			final List category = (List) JSON.parse(categoryData);
			final List<String> categoryJSONData = new ArrayList<String>();
			final JSONArray recsCat = (JSONArray) category;

			for (int i = 0; i < recsCat.length(); i++)
			{
				final String code = recsCat.get(i).toString();
				categoryJSONData.add(code);
			}
			final MyStyleProfileData styleProfileDataCat = new MyStyleProfileData();
			if (!categoryJSONData.isEmpty())
			{
				styleProfileDataCat.setCategoryCodeList(categoryJSONData);
				myStyleProfileFacade.modifyCategory(styleProfileDataCat);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}

	/**
	 * @Description: Fetch the AWB status
	 * @param orderCode
	 * @param model
	 * @return categoryDataMap
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 * @throws JSONException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.AWB_STATUS_URL, method = RequestMethod.GET)
	@ResponseBody
	public AWBResponseData getAWBStatus(@RequestParam(value = ModelAttributetConstants.ORDERCODE) final String orderCode,
			@RequestParam(value = ModelAttributetConstants.OrderLineId) final String orderLineId,
			@RequestParam(value = ModelAttributetConstants.RETURN_AWB) final String returnAWB, final Model model)
			throws CMSItemNotFoundException, NullPointerException, JSONException
	{
		AWBResponseData responseData = new AWBResponseData();
		ConsignmentModel consignmentModel = null;
		try
		{
			final OrderData orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			final List<OrderData> subOrderList = orderDetail.getSellerOrderList();
			for (final OrderData subOrder : subOrderList)
			{
				for (final OrderEntryData orderEntry : subOrder.getEntries())
				{
					if (orderEntry.getConsignment() != null && orderLineId.equalsIgnoreCase(orderEntry.getOrderLineId()))
					{
						consignmentModel = mplOrderService.fetchConsignment(orderEntry.getConsignment().getCode());
						if (consignmentModel != null)
						{
							if (returnAWB.equalsIgnoreCase("Y"))
							{
								responseData = mplOrderService.prepAwbStatus(consignmentModel.getReturnAWBNum(),
										consignmentModel.getReturnCarrier());
							}
							else
							{
								responseData = mplOrderService.prepAwbStatus(consignmentModel.getTrackingID(),
										consignmentModel.getCarrier());
							}
						}
						break;
					}
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			return responseData;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return responseData;
		}
		return responseData;

	}

	/**
	 * @Description: To save Sub Category Data
	 * @param: categoryJSONData
	 */
	private void saveSubCategoryData(final List<String> categoryJSONData)
	{
		final MyStyleProfileData styleProfileData = new MyStyleProfileData();
		if (null != categoryJSONData && !categoryJSONData.isEmpty())
		{
			styleProfileData.setCategoryCodeList(categoryJSONData);
			myStyleProfileFacade.saveSubCategoryData(styleProfileData);
		}
	}

	/**
	 * @description: To find the Cancellation is enabled/disabled
	 * @param: currentStatus
	 * @return: currentStatus
	 */
	@Deprecated
	protected List<OrderEntryData> associatedEntries(final OrderEntryData orderEntry, final OrderData subOrder)
	{
		final List<OrderEntryData> cancelProduct = new ArrayList<>();
		if (orderEntry.getAssociatedItems() != null)
		{
			cancelProduct.add(orderEntry);
			for (final OrderEntryData entry : subOrder.getEntries())
			{
				for (final String associatedItem : orderEntry.getAssociatedItems())
				{
					if (associatedItem.equalsIgnoreCase(entry.getSelectedUssid())
							&& entry.getEntryNumber() != orderEntry.getEntryNumber())
					{
						cancelProduct.add(entry);
						break;
					}
				}
			}

		}
		else
		{
			cancelProduct.add(orderEntry);
		}
		return cancelProduct;
	}


	/**
	 * @return the accountAddressFacade
	 */
	public AccountAddressFacade getAccountAddressFacade()
	{
		return accountAddressFacade;
	}

	/**
	 * @param accountAddressFacade
	 *           the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(final AccountAddressFacade accountAddressFacade)
	{
		this.accountAddressFacade = accountAddressFacade;
	}

	/**
	 * @return the mplOrderFacade
	 */
	public MplOrderFacade getMplOrderFacade()
	{
		return mplOrderFacade;
	}

	/**
	 * @param mplOrderFacade
	 *           the mplOrderFacade to set
	 */
	public void setMplOrderFacade(final MplOrderFacade mplOrderFacade)
	{
		this.mplOrderFacade = mplOrderFacade;
	}

	/**
	 * @Description: Fetching Reviews given by user
	 * @param page
	 *           ,show,sort,sortcode,model
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings(UNUSED)
	@RequestMapping(value = "/reviews", method = RequestMethod.GET)
	@RequireHardLogIn
	public String review(
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = ModelAttributetConstants.ONE_VAL) final int page,
			final Model model) throws CMSItemNotFoundException
	{
		final double pageSize = getSiteConfigService().getInt(MessageConstants.PAZE_SIZE, 5);
		final Map<String, ProductData> productDataMap = new LinkedHashMap<String, ProductData>();
		final Map<String, ProductData> productDataModifyMap = new LinkedHashMap<String, ProductData>();
		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		final List<OrderModel> orderModels = (List<OrderModel>) customerModel.getOrders();
		final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
				ProductOption.VARIANT_FULL, ProductOption.CATEGORIES);
		ProductData productData = new ProductData();
		List<GigyaProductReviewWsDTO> commentsWithProductDataModified = new ArrayList<GigyaProductReviewWsDTO>();
		int startIndex = 0;
		int endIndex = 0;
		int commentListSize = 0;
		try
		{
			if (CollectionUtils.isNotEmpty(orderModels))
			{
				final List<OrderModel> modifiableOrderList = new ArrayList<OrderModel>();
				modifiableOrderList.addAll(orderModels);

				Collections.sort(modifiableOrderList, new Comparator<OrderModel>()
				{
					@Override
					public int compare(final OrderModel o1, final OrderModel o2)
					{
						final int compare = o1.getCreationtime().compareTo(o2.getCreationtime());
						return compare;
					}
				});
				Collections.reverse(modifiableOrderList);
				for (final OrderModel order : modifiableOrderList)
				{
					for (final OrderModel sellerOrder : order.getChildOrders())
					{
						for (final AbstractOrderEntryModel entry : sellerOrder.getEntries())
						{
							final ProductModel productmodel = entry.getProduct();
							final Double netPrice = entry.getNetAmountAfterAllDisc();
							final PriceData price = productDetailsHelper.formPriceData(netPrice);
							try
							{
								if (productFacade.getProductForOptions(productmodel, PRODUCT_OPTIONS) != null)
								{
									productData = (productFacade.getProductForOptions(productmodel, PRODUCT_OPTIONS));
									productData.setPrice(price);
								}
							}
							catch (final Exception exception)
							{
								LOG.error("Review  exception: " + exception);
								throw new EtailNonBusinessExceptions(exception);
							}
							productDataMap.put(productData.getCode(), productData);

							LOG.debug("**********ProductDataMap************** " + productDataMap);
						}
					}
				}
			}

			if (!productDataMap.isEmpty())
			{
				final Iterator productDataMapIterator = productDataMap.entrySet().iterator();
				while (productDataMapIterator.hasNext())
				{
					final Map.Entry productEntry = (Map.Entry) productDataMapIterator.next();
					final ProductData productDataValue = (ProductData) productEntry.getValue();
					final boolean isCommented = gigyaCommentService.getReviewsByCategoryProductId(productDataValue.getRootCategory(),
							productDataValue.getCode(), customerModel.getUid());
					if (!isCommented)
					{
						productDataModifyMap.put(productDataValue.getCode(), productDataValue);
						if (productDataModifyMap.size() == 10)
						{
							break;
						}
					}

				}
			}
			final List<GigyaProductReviewWsDTO> commentsWithProductData = gigyaCommentService
					.getReviewsByUID(customerModel.getUid());
			commentsWithProductDataModified = mplReviewrFacade.getProductPrice(commentsWithProductData, orderModels);

			/* pagination logic */

			if (!commentsWithProductDataModified.isEmpty())
			{
				int start = 0;
				int end = 0;
				final int commentsListSize = commentsWithProductDataModified.size();
				commentListSize = commentsListSize;
				final double pages = Math.ceil(commentsListSize / pageSize);
				final int totalPages = (int) pages;
				model.addAttribute(ModelAttributetConstants.TOTAL_PAGES, Integer.valueOf(totalPages));
				model.addAttribute(ModelAttributetConstants.COMMENT_LIST_SIZE, Integer.valueOf(commentsListSize));
				if (page != 0)
				{
					start = (int) ((page - 1) * pageSize);
					end = (int) (start + pageSize);
				}
				else
				{
					start = 1;
					end = (int) (start + pageSize);
				}

				if (start > commentsListSize)
				{
					start = 1;
					end = (int) (start + pageSize);
				}

				if (end > commentsListSize)
				{
					commentsWithProductDataModified = commentsWithProductDataModified.subList(start, commentsListSize);
				}
				else
				{

					commentsWithProductDataModified = commentsWithProductDataModified.subList(start, end);
				}
			}
			if (page > 1)
			{
				startIndex = ((page - 1) * (int) pageSize) + 1;
				endIndex = ((page - 1) * (int) pageSize) + (int) pageSize;
			}

			else
			{
				if (commentListSize > pageSize)
				{
					startIndex = 1;
					endIndex = (int) pageSize;
				}
				else
				{
					startIndex = 1;
					endIndex = commentListSize;
				}
			}
			if (endIndex >= commentListSize)
			{
				endIndex = commentListSize;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(REVIEW_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(REVIEW_CMS_PAGE));
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS,
				accountBreadcrumbBuilder.getBreadcrumbs(MessageConstants.TEXT_ACCOUNT_REVIEWS));
		model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
		model.addAttribute(ModelAttributetConstants.COMMENTS, commentsWithProductDataModified);
		model.addAttribute(ModelAttributetConstants.START_INDEX, Integer.valueOf(startIndex));
		model.addAttribute(ModelAttributetConstants.END_INDEX, Integer.valueOf(endIndex));
		model.addAttribute(ModelAttributetConstants.PURCHASED_PRODUCT, productDataModifyMap);
		return getViewForPage(model);

	}

	/**
	 * @Description: Deleting and updating Reviews given by user
	 * @param categoryID
	 *           ,streamID,commentID,commentText,commentTitle,ratings,model
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings(UNUSED)
	@RequestMapping(value = "/review/{operation}", method = RequestMethod.GET)
	@RequireHardLogIn
	@ResponseBody
	public Map<String, String> modifyReview(
			@PathVariable("operation") final String operation,
			@RequestParam(value = ModelAttributetConstants.CATEGORY_ID, defaultValue = ModelAttributetConstants.CATEGORY_ID_VAL) final String categoryID,
			@RequestParam(value = ModelAttributetConstants.STREAM_ID, defaultValue = ModelAttributetConstants.STREAM_ID_VAL) final String streamID,
			@RequestParam(value = ModelAttributetConstants.COMMENT_ID, defaultValue = ModelAttributetConstants.COMMENT_ID_VAL) final String commentID,
			@RequestParam(value = ModelAttributetConstants.COMMENT_TEXT, defaultValue = ModelAttributetConstants.COMMENT_TEXT_VAL) final String commentText,
			@RequestParam(value = ModelAttributetConstants.COMMENT_TITLE, defaultValue = ModelAttributetConstants.COMMENT_TITLE_VAL) final String commentTitle,
			@RequestParam(value = ModelAttributetConstants.RATINGS, defaultValue = ModelAttributetConstants.RATINGS_VAL) final String ratings,
			final Model model) throws Exception
	{
		/**
		 * Edit comment service call
		 */
		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		final Map<String, String> jsonMap = new HashMap<String, String>();

		try
		{
			if (null != operation && operation.equals("edit"))
			{
				final String gigyaEditResponse = gigyaCommentService.editComment(categoryID, streamID, commentID, commentText,
						commentTitle, ratings, customerModel.getUid());

				if (null != gigyaEditResponse && gigyaEditResponse.equals("OK"))
				{
					jsonMap.put(STATUS, "success");
					return jsonMap;
				}
				else
				{
					jsonMap.put(STATUS, "failed");
					return jsonMap;
				}
			}

			if (null != operation && operation.equals("delete"))
			{
				final String gigyaEditResponse = gigyaCommentService.deleteComment(categoryID, streamID, commentID);

				if (null != gigyaEditResponse && gigyaEditResponse.equals("OK"))
				{
					jsonMap.put(STATUS, "success");
					return jsonMap;
				}
				else
				{
					jsonMap.put(STATUS, "failed");
					return jsonMap;
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);

			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			jsonMap.put(ERROR_OCCURED, ERROR_RESP);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			jsonMap.put(ERROR_OCCURED, ERROR_RESP);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			jsonMap.put(ERROR_OCCURED, ERROR_RESP);
		}
		return null;
	}





}
