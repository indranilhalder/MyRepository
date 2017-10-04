package com.tisl.mpl.v2.controller;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customergroups.CustomerGroupFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoDatas;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.UserGroupDataList;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressValidationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserGroupListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserSignUpWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.commercewebservicescommons.model.OAuthAccessTokenModel;
import de.hybris.platform.commercewebservicescommons.oauth2.token.OAuthTokenService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.PK.PKException;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tisl.lux.facade.CommonUtils;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.constants.YcommercewebservicesConstants;
import com.tisl.mpl.core.enums.FeedbackArea;
import com.tisl.mpl.core.enums.Frequency;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.EditWishlistNameData;
import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoRequestData;
import com.tisl.mpl.data.ReturnInfoData;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.data.ReturnLogisticsResponseDetails;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.impl.MplCheckoutFacadeImpl;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.myfavbrandcategory.MplMyFavBrandCategoryFacade;
import com.tisl.mpl.facade.netbank.MplNetBankingFacade;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplCouponWebFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.preference.MplPreferenceFacade;
import com.tisl.mpl.facades.account.register.FriendsInviteFacade;
import com.tisl.mpl.facades.account.register.MplCustomerProfileFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.facades.account.reviews.GigyaFacade;
import com.tisl.mpl.facades.data.MplFavBrandCategoryData;
import com.tisl.mpl.facades.data.MplFavBrandCategoryWsDTO;
import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.facades.egv.data.EgvDetailsData;
import com.tisl.mpl.facades.order.impl.DefaultGetOrderDetailsFacadeImpl;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.populators.CustomAddressReversePopulator;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.facades.product.data.ReturnReasonDetails;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.helper.MplUserHelper;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.egv.service.cart.MplEGVCartService;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.FriendsInviteService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtendedUserServiceImpl;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;
import com.tisl.mpl.model.PaymentModeRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.order.data.OrderEntryDataList;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.populator.HttpRequestCustomerDataPopulator;
import com.tisl.mpl.populator.options.PaymentInfoOption;
import com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.service.MplMobileUserService;
import com.tisl.mpl.service.impl.MplProductWebServiceImpl;
import com.tisl.mpl.user.data.AddressDataList;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.MplTimeconverUtility;
import com.tisl.mpl.validation.data.AddressValidationData;
import com.tisl.mpl.webservice.businessvalidator.DefaultCommonAsciiValidator;
import com.tisl.mpl.wsdto.BuyingEgvRequestWsDTO;
import com.tisl.mpl.wsdto.BuyingEgvResponceWsDTO;
import com.tisl.mpl.wsdto.CommonCouponsDTO;
import com.tisl.mpl.wsdto.EMIBankListWsDTO;
import com.tisl.mpl.wsdto.EMITermRateDataForMobile;
import com.tisl.mpl.wsdto.FetchNewsLetterSubscriptionWsDTO;
import com.tisl.mpl.wsdto.GetCustomerDetailDto;
import com.tisl.mpl.wsdto.GetWishListDataWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.GetmerchantWsDTO;
import com.tisl.mpl.wsdto.GigyaWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListRequestWsDTO;
import com.tisl.mpl.wsdto.MplAllFavouritePreferenceWsDTO;
import com.tisl.mpl.wsdto.MplOrderNotificationWsDto;
import com.tisl.mpl.wsdto.MplOrderTrackingNotificationsListWsDto;
import com.tisl.mpl.wsdto.MplPreferenceDataForMobile;
import com.tisl.mpl.wsdto.MplPreferenceListWsDto;
import com.tisl.mpl.wsdto.MplUserResultWsDto;
import com.tisl.mpl.wsdto.NetBankingListWsDTO;
import com.tisl.mpl.wsdto.NetBankingWsDTO;
import com.tisl.mpl.wsdto.OrderCreateInJusPayWsDto;
import com.tisl.mpl.wsdto.OrderProductWsDTO;
import com.tisl.mpl.wsdto.PayCliqCashWsDto;
import com.tisl.mpl.wsdto.QuickDropStoresList;
import com.tisl.mpl.wsdto.RedeemCliqVoucherWsDTO;
import com.tisl.mpl.wsdto.ResendEGVNotificationWsDTO;
import com.tisl.mpl.wsdto.ReturnDetailsWsDTO;
import com.tisl.mpl.wsdto.ReturnLogisticsResponseDTO;
import com.tisl.mpl.wsdto.ReturnLogisticsResponseDetailsWsDTO;
import com.tisl.mpl.wsdto.ReturnModesWsDTO;
import com.tisl.mpl.wsdto.ReturnPincodeDTO;
import com.tisl.mpl.wsdto.ReturnReasonDTO;
import com.tisl.mpl.wsdto.ReturnReasonDetailsWsDTO;
import com.tisl.mpl.wsdto.ReturnRequestDTO;
import com.tisl.mpl.wsdto.RevSealJwlryDataWsDTO;
import com.tisl.mpl.wsdto.ThirdPartyWalletWsDTO;
import com.tisl.mpl.wsdto.TotalCliqCashBalanceWsDto;
import com.tisl.mpl.wsdto.UpdateCustomerDetailDto;
import com.tisl.mpl.wsdto.UserResultWsDto;
import com.tisl.mpl.wsdto.ValidateOtpWsDto;
import com.tisl.mpl.wsdto.WalletPaymentWsDTO;
import com.tisl.mpl.wsdto.WebSerResponseWsDTO;


/**
 * Main Controller for Users
 *
 * @pathparam userId User identifier or one of the literals below :
 *            <ul>
 *            <li>'current' for currently authenticated user</li>
 *            <li>'anonymous' for anonymous user</li>
 *            </ul>
 * @pathparam addressId Address identifier
 * @pathparam paymentDetailsId - Payment details identifier
 */
@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
@Controller
@RequestMapping(value = "/{baseSiteId}/users", headers = "Accept=application/xml,application/json")
@CacheControl(directive = CacheControlDirective.PRIVATE)
public class UsersController extends BaseCommerceController
{
	private static final Logger LOG = Logger.getLogger(UsersController.class);
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	private CheckoutCustomerStrategy checkoutCustomerStrategy;

	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "customerGroupFacade")
	private CustomerGroupFacade customerGroupFacade;
	@Resource(name = "addressVerificationFacade")
	private AddressVerificationFacade addressVerificationFacade;
	@Resource(name = "httpRequestCustomerDataPopulator")
	private HttpRequestCustomerDataPopulator httpRequestCustomerDataPopulator;
	@Resource(name = "httpRequestAddressDataPopulator")
	private Populator<HttpServletRequest, AddressData> httpRequestAddressDataPopulator;
	//	@Resource(name = "HttpRequestUserSignUpDTOPopulator") Critical Sonar fixes Unused private Field
	//	private Populator<HttpServletRequest, UserSignUpWsDTO> httpRequestUserSignUpDTOPopulator;
	@Resource(name = "addressValidator")
	private Validator addressValidator;
	@Resource(name = "httpRequestPaymentInfoPopulator")
	private ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> httpRequestPaymentInfoPopulator;
	@Resource(name = "addressDataErrorsPopulator")
	private Populator<AddressVerificationResult<AddressVerificationDecision>, Errors> addressDataErrorsPopulator;
	@Resource(name = "validationErrorConverter")
	private Converter<Object, List<ErrorWsDTO>> validationErrorConverter;
	@Resource(name = "ccPaymentInfoValidator")
	private Validator ccPaymentInfoValidator;
	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;
	@Resource(name = "putUserDTOValidator")
	private Validator putUserDTOValidator;
	//	@Resource(name = "userSignUpDTOValidator") Critical Sonar fixes Unused private Field
	//	private Validator userSignUpDTOValidator;
	//	@Resource(name = "guestConvertingDTOValidator")
	//	private Validator guestConvertingDTOValidator;
	@Resource(name = "passwordStrengthValidator")
	private Validator passwordStrengthValidator;

	@Resource
	private MplCheckoutFacade mplCheckoutFacade;
	/* R2.3 start */
	@Autowired
	private CustomAddressReversePopulator addressReversePopulator;
	/* R2.3 end */
	@Resource
	private Wishlist2Service wishlistService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;

	//Exchange Changes
	@Resource(name = "exchangeGuideFacade")
	private ExchangeGuideFacade exchangeFacade;

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	//
	@Resource
	private MplPreferenceFacade mplPreferenceFacade;
	@Resource
	private ExtendedUserService extUserService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource
	private MplMobileUserService mobileUserService;
	@Resource
	private MplCustomerProfileService mplCustomerProfileService;
	@Resource
	private UserService userService;
	@Resource
	private FriendsInviteService friendsInviteService;
	@Resource
	private CustomerAccountService customerAccountService;
	@Resource
	private WishlistFacade wishlistFacade;
	@Resource
	private FriendsInviteFacade friendsInviteFacade;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private HybrisOAuthTokenStore hybrisOAuthTokenStore;
	//	@Autowired
	//	private ForgetPasswordFacade forgetPasswordFacade;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Resource
	private MplAccountAddressFacade accountAddressFacade;
	@Resource
	private UpdateFeedbackFacade updateFeedbackFacade;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private EnumerationService enumerationService;
	//	@Autowired
	//	private ExtendedUserService extendedUserService;
	@Resource
	private CommerceCartService commerceCartService;

	@Resource
	private MplCartFacade mplCartFacade;

	@Resource
	private MplEnumerationHelper mplEnumerationHelper;
	@Resource
	private BaseSiteService baseSiteService;

	@Resource
	private NotificationFacade notificationFacade;

	@Resource
	private CancelReturnFacade cancelReturnFacade;

	@Resource
	private MplOrderFacade mplOrderFacade;

	@Resource
	private MplMyFavBrandCategoryFacade mplMyFavBrandCategoryFacade;

	@Autowired
	private CartService cartService;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private DefaultCheckoutFacade defaultCheckoutFacade;
	@Autowired
	private MplCartWebService mplCartWebService;
	@Autowired
	private MplUserHelper mplUserHelper;
	@Autowired
	private MplPaymentWebFacade mplPaymentWebFacade;
	@Autowired
	private MplProductWebServiceImpl mplProductWebService;

	@Autowired
	private BuyBoxFacade buyBoxFacade;
	@Autowired
	private ProductDetailsHelper productDetailsHelper;

	@Autowired
	private MplCouponWebFacade mplCouponWebFacade;
	@Autowired
	private GigyaFacade gigyaFacade;
	@Autowired
	private ExtendedUserService extendedUserService;
	@Resource(name = "productService")
	private ProductService productService;
	@Autowired
	private DefaultGetOrderDetailsFacadeImpl getOrderDetailsFacade;

	@Autowired
	private MplConfigFacade mplConfigFacade;
	@Autowired
	private PincodeServiceFacade pincodeServiceFacade;
	@Autowired
	private MplCheckoutFacadeImpl mplCheckoutFacadeImpl;
	@Autowired
	private MplOrderService mplOrderService;


	@Autowired
	private DateUtilHelper dateUtilHelper;
	@Autowired
	private OrderModelDao orderModelDao;
	@Autowired
	private CommonUtils commonUtils;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	@Resource(name = "voucherService")
	private VoucherService voucherService;

	@Autowired
	private MplWalletFacade mplWalletFacade;
	
	@Resource(name = "mplDefaultPriceDataFactory")
	private DefaultPriceDataFactory priceDataFactory;
	
	@Autowired
	private CommonI18NService commonI18NService;
	
	@Autowired
	private MplEGVCartService mplEGVCartService;


	//Sonar Fix
	private static final String NO_JUSPAY_URL = "No juspayReturnUrl is defined in local properties";

	private static final String NO_JUSPAY_MERCHANTKEY = "No juspayMerchantKey is defined in local properties";

	//@Autowired
	//private MplPaymentFacadeImpl mplPaymentFacadeImpl;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private CommerceCartService commerceCartService;
	//	@Autowired
	//	private ExtendedUserService extendedUserService;
	/**
	 * @return the voucherService
	 */
	public VoucherService getVoucherService()
	{
		return voucherService;
	}

	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}

	//	@Autowired Critical Sonar fixes Unused private Field
	//	private MplNetBankingServiceImpl mplNetBankingServiceImpl;
	@Autowired
	private MplNetBankingFacade mplNetBankingFacade;
	private static final int MAX_FIELD_LENGTH = 255;
	private static final int MAX_POSTCODE_LENGTH = 10;
	private static final int MAX_FIELD_LENGTH_NEW = 140;
	private static final int MAX_FIELD_LENGTH_ADDLINE = 40;
	private static final int MAX_FIELD_LENGTH_STATE = 20;
	private static final int MAX_FIELD_LENGTH_COUNTRY = 15;
	private static final int MAX_LANDMARK_LENGTH = 30;
	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";

	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String ROLE_CLIENT = "ROLE_CLIENT";
	private static final String APPLICATION_TYPE = "application/json";
	private static final String USER_ID = "/{userId}";
	private static final String ROLE_GUEST = "ROLE_GUEST";
	private static final String URL_ID = "/{userId}/addresses/{addressId}";
	private static final String ADDRESS_ID = "Address with given id: '";
	private static final String MESSAGE_STRING = "' doesn't exist or belong to another user";
	private static final String ADDRESS = "addressId";
	private static final String ADDRESS_DATA = "addressData";
	private static final String PAYMENT_ID = "/{userId}/paymentdetails/{paymentDetailsId}";
	private static final String PAYMENT_DETAILS = "paymentDetails";
	private static final String CUSTOMER_MESSAGE = "Customer UID is : ";
	private static final String CUSTOM_MESSAGE = " User Not found.Please contact administrator";
	private static final String DATA_MESSAGE = " DataAccess related error occured. Please contact administrator";
	private static final String SERVICE_MESSAGE = "Service Authentication error occured. Please contact administrator";
	private static final String AUTHENTICATION_MESSAGE = "Authentication error occured. Please contact administrator";
	private static final String ERROR_MESSAGE = "some error occured. Please contact administrator";
	private static final String UTF = "UTF-8";
	private static final String STORE_NA = "Store Not available";

	@Autowired
	private HttpServletRequest request;

	//Sonar fix
	//private static final String PAYMENT_M_RUPEE_MERCHANT_ID = "payment.mRupee.merchantID";

	/**
	 * TPR-1372
	 *
	 * @param emailId
	 * @param password
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 */

	//Mobile registration
	@Secured(
	{ ROLE_CLIENT, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/registration", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto registerUser(@RequestParam final String emailId, @RequestParam final String password,
			@RequestParam(required = false) final boolean tataTreatsEnable,
			@RequestParam(required = false) final String platformNumber)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException

	{
		LOG.debug("****************** User Registration mobile web service ***********" + emailId);
		MplUserResultWsDto userResult = new MplUserResultWsDto();
		GigyaWsDTO gigyaWsDto = new GigyaWsDTO();
		final boolean isNewusers = true;
		try
		{
			/* TPR-1140 Case-sensitive nature resulting in duplicate customer e-mails IDs */
			final String emailIdLwCase = emailId.toLowerCase();


			//TPR-6272 starts here
			LOG.debug("The platform number is " + platformNumber);
			int platformDecider;
			if (StringUtils.isNotEmpty(platformNumber))//IQA
			{
				platformDecider = Integer.parseInt(platformNumber);
			}
			else
			{
				platformDecider = MarketplacecommerceservicesConstants.PLATFORM_FOUR;//for backward compatiblity mobile app and iqa
			}
			LOG.debug("The platform number is " + platformDecider);
			//TPR-6272 ends here
			userResult = mobileUserService.registerNewMplUser(emailIdLwCase, password, tataTreatsEnable, platformDecider);//TPR-6272 Parameter platformNumber added
			final CustomerModel customerModel = mplPaymentWebFacade.getCustomer(emailIdLwCase);
			gigyaWsDto = gigyaFacade.gigyaLoginHelper(customerModel, isNewusers);
			if (StringUtils.isNotEmpty(gigyaWsDto.getSessionSecret()))
			{
				userResult.setSessionSecret(gigyaWsDto.getSessionSecret());
			}
			if (StringUtils.isNotEmpty(gigyaWsDto.getSessionToken()))
			{
				userResult.setSessionToken(gigyaWsDto.getSessionToken());
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				userResult.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResult.setErrorCode(e.getErrorCode());
			}
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				userResult.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResult.setErrorCode(e.getErrorCode());
			}
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return userResult;
	}

	/**
	 * Login user from mobile
	 *
	 * @param emailId
	 * @param password
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "{emailId}/loginMobile", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto loginUser(@PathVariable final String emailId, @RequestParam final String newCustomer,
			@RequestParam final String password) throws RequestParameterException, WebserviceValidationException,

			MalformedURLException

	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		GigyaWsDTO gigyaWsDTO = new GigyaWsDTO();
		boolean isNewusers = false;
		//		final CustomerModel customerModel = mplPaymentWebFacade.getCustomer(emailId);
		CustomerModel customerModel = null;
		try
		{
			//CAR Project performance issue fixed

			customerModel = (CustomerModel) userService.getCurrentUser();
			//customerModel = mplPaymentWebFacade.getCustomer(emailId);

			LOG.debug("****************** User Login mobile web service ***********" + emailId);
			//Login user with username and password
			isNewusers = newCustomer.equalsIgnoreCase(MarketplacecommerceservicesConstants.Y) ? true : false;
			result = mobileUserService.loginUser(emailId, password);
			gigyaWsDTO = gigyaFacade.gigyaLoginHelper(customerModel, isNewusers);
			if (StringUtils.isNotEmpty(gigyaWsDTO.getSessionSecret()))
			{
				result.setSessionSecret(gigyaWsDTO.getSessionSecret());
			}
			if (StringUtils.isNotEmpty(gigyaWsDTO.getSessionToken()))
			{
				result.setSessionToken(gigyaWsDTO.getSessionToken());
			}
			//Return result
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}

	/**
	 * Register in portal via social media login such as facebook and googleplus TPR-1372
	 *
	 * @param emailId
	 * @param socialMedia
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 */
	@Secured(
	{ ROLE_CLIENT, CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/socialMediaRegistration", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto socialMediaRegistration(@RequestParam final String emailId, @RequestParam final String socialMedia,
			@RequestParam(required = false) final boolean tataTreatsEnable)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		try
		{
			/* TPR-1140 Case-sensitive nature resulting in duplicate customer e-mails IDs */
			final String emailIdLwCase = emailId.toLowerCase();
			LOG.debug("****************** Social Media User Registration mobile web service ***********" + emailId);
			if (!(StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.FACEBOOK)
					|| (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.GOOGLEPLUS))))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9020);
			}
			else if (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.FACEBOOK))
			{
				result = mobileUserService.socialMediaRegistration(emailIdLwCase, MarketplacewebservicesConstants.FACEBOOK,
						tataTreatsEnable);
			}
			else if (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.GOOGLEPLUS))
			{
				result = mobileUserService.socialMediaRegistration(emailIdLwCase, MarketplacecommerceservicesConstants.GOOGLE,
						tataTreatsEnable);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		//Return result
		return result;
	}

	/**
	 * Login via social media
	 *
	 * @param emailId
	 * @param socialMedia
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "{emailId}/loginSocialUser", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto loginSocialUser(@PathVariable final String emailId, @RequestParam final String socialMedia)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		try
		{
			LOG.debug("****************** Social Media User Login mobile web service ***********" + emailId);

			//Social Media should not be anything other than FB or Google +
			if (!(StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.FACEBOOK)
					|| (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.GOOGLEPLUS))))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9020);
			}
			else if (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.FACEBOOK))
			{
				result = mobileUserService.loginSocialUser(emailId, socialMedia);
			}
			else if (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.GOOGLEPLUS))
			{
				result = mobileUserService.loginSocialUser(emailId, socialMedia);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		//Return result
		return result;
	}


	/**
	 * Convert the guest customer to user
	 *
	 * @param user
	 *           User's object
	 * @bodyparams login,password, guid * @throws de.hybris.platform.commerceservices.customer.DuplicateUidException in
	 *             case the requested login already exists
	 * @throws RequestParameterException
	 *            if the title code is invalid
	 * @throws DuplicateUidException
	 *            if any filed is invalid
	 */
	//	private UserResultWsDto convertToCustomer(final String login, final String password, final String guid)
	//			throws RequestParameterException, DuplicateUidException
	//	{
	//		final UserResultWsDto result = new UserResultWsDto();
	//		if (LOG.isDebugEnabled())
	//		{
	//			LOG.debug("registerUser: guid=" + sanitize(guid));
	//		}
	//
	//		try
	//		{
	//			//Set Success to result
	//			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
	//			//convert the guest to customer
	//			customerFacade.changeGuestToCustomer(password, guid);
	//		}
	//		catch (final UnknownIdentifierException ex)
	//		{
	//			//Set Error to result
	//			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	//			throw new RequestParameterException("Order with guid " + sanitize(guid) + " not found in current BaseStore",
	//					RequestParameterException.UNKNOWN_IDENTIFIER, "guid", ex);
	//		}
	//		catch (final IllegalArgumentException ex)
	//		{
	//			//Set Error to result
	//			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	//			// Occurs when order does not belong to guest user.
	//			// For security reasons it's better to treat it as "unknown identifier" error
	//			throw new RequestParameterException("Order with guid " + sanitize(guid) + " not found in current BaseStore",
	//					RequestParameterException.UNKNOWN_IDENTIFIER, "guid", ex);
	//		}
	//
	//		return result;
	//	}

	/**
	 * Returns customer profile.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 *
	 * @return Customer profile.
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = USER_ID, method = RequestMethod.GET)
	@ResponseBody
	public UserWsDTO getUser(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UserWsDTO dto = dataMapper.map(customerData, UserWsDTO.class, fields);
		return dto;
	}

	/**
	 * Updates customer profile. Attributes not provided in the request body will be defined again (set to null or
	 * default).
	 *
	 * @formparam firstName Customer's first name.
	 * @formparam lastName Customer's last name.
	 * @formparam titleCode Customer's title code. For a list of codes, see /{baseSiteId}/titles resource
	 * @formparam language Customer's language.
	 * @formparam currency Customer's currency.
	 * @throws DuplicateUidException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = USER_ID, method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void putUser(@RequestParam final String firstName, @RequestParam final String lastName,
			@RequestParam(required = true) final String titleCode, final HttpServletRequest request) throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("putCustomer: userId=" + customer.getUid());
		}
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setTitleCode(titleCode);
		customer.setLanguage(null);
		customer.setCurrency(null);
		httpRequestCustomerDataPopulator.populate(request, customer);

		customerFacade.updateFullProfile(customer);
	}

	/**
	 * Updates customer profile. Attributes not provided in the request body will be defined again (set to null or
	 * default).
	 *
	 * @param user
	 *           User's object
	 * @bodyparams firstName,lastName,titleCode,currency(isocode),language(isocode)
	 * @throws DuplicateUidException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = USER_ID, method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void putUser(@RequestBody final UserWsDTO user) throws DuplicateUidException
	{
		validate(user, "user", putUserDTOValidator);

		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("putCustomer: userId=" + customer.getUid());
		}

		dataMapper.map(user, customer, "firstName,lastName,titleCode,currency(isocode),language(isocode)", true);
		customerFacade.updateFullProfile(customer);
	}

	/**
	 * Updates customer profile. Only attributes provided in the request body will be changed.
	 *
	 * @formparam firstName Customer's first name.
	 * @formparam lastName Customer's last name.
	 * @formparam titleCode Customer's title code. For a list of codes, see /{baseSiteId}/titles resource
	 * @formparam language Customer's language.
	 * @formparam currency Customer's currency.
	 * @throws DuplicateUidException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = USER_ID, method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.OK)
	public void updateUser(final HttpServletRequest request) throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateUser: userId=" + customer.getUid());
		}
		httpRequestCustomerDataPopulator.populate(request, customer);
		customerFacade.updateFullProfile(customer);
	}

	/**
	 * Updates customer profile. Only attributes provided in the request body will be changed.
	 *
	 * @param user
	 *           User's object
	 * @bodyparams firstName,lastName,titleCode,currency(isocode),language(isocode)
	 * @throws DuplicateUidException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = USER_ID, method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void updateUser(@RequestBody final UserWsDTO user) throws DuplicateUidException
	{
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateUser: userId=" + customer.getUid());
		}

		dataMapper.map(user, customer, "firstName,lastName,titleCode,currency(isocode),language(isocode)", false);
		customerFacade.updateFullProfile(customer);
	}

	/**
	 * Removes customer profile.
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = USER_ID, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deactivateUser()
	{
		final UserModel userModel = extUserService.getCurrentUser();
		if (!userModel.isLoginDisabled())
		{
			userModel.setLoginDisabled(true);
			modelService.save(userModel);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deactivateUser: userId=" + userModel.getUid());
		}
	}

	/**
	 * Changes customer's login.
	 *
	 * @formparam newLogin Customer's new login. Customer login is case insensitive.
	 * @formparam password Customer's current password.
	 * @throws DuplicateUidException
	 * @throws PasswordMismatchException
	 * @throws RequestParameterException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/login", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void changeLogin(@RequestParam final String newLogin, @RequestParam final String password)
			throws DuplicateUidException, PasswordMismatchException, RequestParameterException
	{
		if (!EmailValidator.getInstance().isValid(newLogin))
		{
			throw new RequestParameterException("Login [" + newLogin + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, "newLogin");
		}
		customerFacade.changeUid(newLogin, password);
	}

	/**
	 * Changes customer's password.
	 *
	 * @formparam new New password
	 * @formparam old Old password. Required only for ROLE_CUSTOMERGROUP
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/password", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public void changePassword(@PathVariable final String userId, @RequestParam(required = false) final String old,
			@RequestParam(value = "new") final String newPassword)
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final UserSignUpWsDTO customer = new UserSignUpWsDTO();
		customer.setPassword(newPassword);
		validate(customer, "password", passwordStrengthValidator);
		if (containsRole(auth, TRUSTED_CLIENT) || containsRole(auth, CUSTOMERMANAGER))
		{
			extUserService.setPassword(userId, newPassword);
		}
		else
		{
			if (StringUtils.isEmpty(old))
			{
				throw new RequestParameterException("Request parameter 'old' is missing.", RequestParameterException.MISSING, "old");
			}
			customerFacade.changePassword(old, newPassword);
		}
	}

	private boolean containsRole(final Authentication auth, final String role)
	{
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns customer's addresses.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @return List of customer's addresses
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/addresses", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public AddressListWsDTO getAddresses(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		AddressListWsDTO dto = new AddressListWsDTO();
		try
		{
			final List<AddressData> addressList = accountAddressFacade.getAddressBook();
			boolean successFlag = false;
			if (!(addressList != null))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9204);
			}

			final AddressDataList addressDataList = new AddressDataList();
			addressDataList.setAddresses(addressList);
			successFlag = true;
			dto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			dto = dataMapper.map(addressDataList, AddressListWsDTO.class, fields);
			if (successFlag)
			{
				dto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				dto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				dto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				dto.setErrorCode(e.getErrorCode());
			}
			dto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				dto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				dto.setErrorCode(e.getErrorCode());
			}
			dto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return dto;

	}

	/**
	 * Creates a new address.
	 *
	 * @formparam firstName Customer's first name. This parameter is required.
	 * @formparam lastName Customer's last name. This parameter is required.
	 * @formparam titleCode Customer's title code. This parameter is required. For a list of codes, see
	 *            /{baseSiteId}/titles resource
	 * @formparam country.isocode Country isocode. This parameter is required and have influence on how rest of
	 *            parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)
	 * @formparam line1 First part of address. If this parameter is required depends on country (usually it is required).
	 * @formparam line2 Second part of address. If this parameter is required depends on country (usually it is not
	 *            required)
	 * @formparam town Town name. If this parameter is required depends on country (usually it is required)
	 * @formparam postalCode Postal code. If this parameter is required depends on country (usually it is required)
	 * @formparam region.isocode Isocode for region. If this parameter is required depends on country.
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @return Created address
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/addresses", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public AddressWsDTO createAddress(final HttpServletRequest request,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws WebserviceValidationException
	{
		final AddressData addressData = super.createAddressInternal(request);
		final AddressWsDTO dto = dataMapper.map(addressData, AddressWsDTO.class, fields);
		return dto;
	}

	/**
	 * Created a new address.
	 *
	 * @param address
	 *           Address object
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @bodyparams firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode),
	 *             defaultAddress
	 * @return Created address
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/addresses", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public AddressWsDTO createAddress(@RequestBody final AddressWsDTO address,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws WebserviceValidationException
	{
		validate(address, "address", addressDTOValidator);
		final AddressData addressData = dataMapper.map(address, AddressData.class,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress");
		addressData.setShippingAddress(true);
		addressData.setVisibleInAddressBook(true);

		userFacade.addAddress(addressData);
		if (addressData.isDefaultAddress())
		{
			userFacade.setDefaultAddress(addressData);
		}

		final AddressWsDTO dto = dataMapper.map(addressData, AddressWsDTO.class, fields);
		return dto;
	}

	/**
	 * Returns detailed information about address with a given id.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @return Address data
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = URL_ID, method = RequestMethod.GET)
	@ResponseBody
	public AddressWsDTO getAddress(@PathVariable final String addressId,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws WebserviceValidationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getAddress: id=" + sanitize(addressId));
		}

		final AddressData addressData = userFacade.getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(ADDRESS_ID + sanitize(addressId) + MESSAGE_STRING, RequestParameterException.INVALID,
					ADDRESS);
		}

		final AddressWsDTO dto = dataMapper.map(addressData, AddressWsDTO.class, fields);
		return dto;
	}

	/**
	 * Updates the address. Attributes not provided in the request will be defined again (set to null or default).
	 *
	 * @formparam firstName Customer's first name. This parameter is required.
	 * @formparam lastName Customer's last name. This parameter is required.
	 * @formparam titleCode Customer's title code. This parameter is required. For a list of codes, see
	 *            /{baseSiteId}/titles resource
	 * @formparam country .isocode Country isocode. This parameter is required and have influence on how rest of
	 *            parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)
	 * @formparam line1 First part of address. If this parameter is required depends on country (usually it is required).
	 * @formparam line2 Second part of address. If this parameter is required depends on country (usually it is not
	 *            required)
	 * @formparam town Town name. If this parameter is required depends on country (usually it is required)
	 * @formparam postalCode Postal code. If this parameter is required depends on country (usually it is required)
	 *            restparam region .isocode Isocode for region. If this parameter is required depends on country.
	 * @formparam defaultAddress Parameter specifies if address should be default for customer
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = URL_ID, method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void putAddress(@PathVariable final String addressId, final HttpServletRequest request)
			throws WebserviceValidationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("editAddress: id=" + sanitize(addressId));
		}
		final AddressData addressData = userFacade.getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(ADDRESS_ID + sanitize(addressId) + MESSAGE_STRING, RequestParameterException.INVALID,
					ADDRESS);
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFirstName(null);
		addressData.setLastName(null);
		addressData.setCountry(null);
		addressData.setLine1(null);
		addressData.setLine2(null);
		addressData.setPostalCode(null);
		addressData.setRegion(null);
		addressData.setTitle(null);
		addressData.setTown(null);
		addressData.setDefaultAddress(false);
		addressData.setFormattedAddress(null);

		httpRequestAddressDataPopulator.populate(request, addressData);

		final Errors errors = new BeanPropertyBindingResult(addressData, ADDRESS_DATA);
		addressValidator.validate(addressData, errors);

		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
		userFacade.editAddress(addressData);

		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			userFacade.setDefaultAddress(addressData);
		}
	}

	/**
	 * Updates the address. Attributes not provided in the request will be defined again (set to null or default).
	 *
	 * @param address
	 *           Address object
	 * @bodyparams firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),
	 *             defaultAddress
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = URL_ID, method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void putAddress(@PathVariable final String addressId, @RequestBody final AddressWsDTO address)
			throws WebserviceValidationException
	{
		validate(address, "address", addressDTOValidator);
		final AddressData addressData = userFacade.getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(ADDRESS_ID + sanitize(addressId) + MESSAGE_STRING, RequestParameterException.INVALID,
					ADDRESS);
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFormattedAddress(null);
		dataMapper.map(address, addressData,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress", true);

		userFacade.editAddress(addressData);

		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			userFacade.setDefaultAddress(addressData);
		}
	}

	/**
	 * Updates the address. Only attributes provided in the request body will be changed.
	 *
	 * @formparam firstName Customer's first name. This parameter is required.
	 * @formparam lastName Customer's last name. This parameter is required.
	 * @formparam titleCode Customer's title code. This parameter is required. For a list of codes, see
	 *            /{baseSiteId}/titles resource
	 * @formparam country.isocode Country isocode. This parameter is required and have influence on how rest of
	 *            parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)
	 * @formparam line1 First part of address. If this parameter is required depends on country (usually it is required).
	 * @formparam line2 Second part of address. If this parameter is required depends on country (usually it is not
	 *            required)
	 * @formparam town Town name. If this parameter is required depends on country (usually it is required)
	 * @formparam postalCode Postal code. If this parameter is required depends on country (usually it is required)
	 * @formparam region.isocode ISO code for region. If this parameter is required depends on country.
	 * @formparam defaultAddress Parameter specifies if address should be default for customer
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = URL_ID, method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.OK)
	public void patchAddress(@PathVariable final String addressId, final HttpServletRequest request)
			throws WebserviceValidationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("editAddress: id=" + sanitize(addressId));
		}
		final AddressData addressData = userFacade.getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(ADDRESS_ID + sanitize(addressId) + MESSAGE_STRING, RequestParameterException.INVALID,
					ADDRESS);
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFormattedAddress(null);
		final Errors errors = new BeanPropertyBindingResult(addressData, ADDRESS_DATA);

		httpRequestAddressDataPopulator.populate(request, addressData);
		addressValidator.validate(addressData, errors);

		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}

		if (addressData.getId().equals(userFacade.getDefaultAddress().getId()))
		{
			addressData.setDefaultAddress(true);
			addressData.setVisibleInAddressBook(true);
		}
		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			userFacade.setDefaultAddress(addressData);
		}
		userFacade.editAddress(addressData);
	}

	/**
	 * Updates the address. Only attributes provided in the request body will be changed.
	 *
	 * @param address
	 *           address object
	 * @bodyparams firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),
	 *             defaultAddress
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = URL_ID, method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void patchAddress(@PathVariable final String addressId, @RequestBody final AddressWsDTO address)
			throws WebserviceValidationException
	{
		final AddressData addressData = userFacade.getAddressForCode(addressId);
		if (addressData == null)
		{
			throw new RequestParameterException(ADDRESS_ID + sanitize(addressId) + MESSAGE_STRING, RequestParameterException.INVALID,
					ADDRESS);
		}
		final boolean isAlreadyDefaultAddress = addressData.isDefaultAddress();
		addressData.setFormattedAddress(null);

		dataMapper.map(address, addressData,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress", false);
		validate(addressData, "address", addressValidator);

		if (addressData.getId().equals(userFacade.getDefaultAddress().getId()))
		{
			addressData.setDefaultAddress(true);
			addressData.setVisibleInAddressBook(true);
		}
		if (!isAlreadyDefaultAddress && addressData.isDefaultAddress())
		{
			userFacade.setDefaultAddress(addressData);
		}
		userFacade.editAddress(addressData);
	}

	/**
	 * Removes customer's address.
	 *
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = URL_ID, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteAddress(@PathVariable final String addressId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deleteAddress: id=" + sanitize(addressId));
		}
		final AddressData address = userFacade.getAddressForCode(addressId);
		if (address == null)
		{
			throw new RequestParameterException(ADDRESS_ID + sanitize(addressId) + MESSAGE_STRING, RequestParameterException.INVALID,
					ADDRESS);
		}
		userFacade.removeAddress(address);
	}

	/**
	 * Verifies the address.
	 *
	 * @formparam country.isocode Country isocode. This parameter is required and have influence on how rest of
	 *            parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)
	 * @formparam line1 First part of address. If this parameter is required depends on country (usually it is required).
	 * @formparam line2 Second part of address. If this parameter is required depends on country (usually it is not
	 *            required)
	 * @formparam town Town name. If this parameter is required depends on country (usually it is required)
	 * @formparam postalCode Postal code. If this parameter is required depends on country (usually it is required)
	 * @formparam region.isocode Isocode for region. If this parameter is required depends on country.
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @return verification results. If address is incorrect there are information about errors and suggested addresses
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/addresses/verification", method = RequestMethod.POST)
	@ResponseBody
	public AddressValidationWsDTO verifyAddress(final HttpServletRequest request,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final AddressData addressData = new AddressData();
		final Errors errors = new BeanPropertyBindingResult(addressData, ADDRESS_DATA);
		AddressValidationData validationData = new AddressValidationData();

		httpRequestAddressDataPopulator.populate(request, addressData);
		if (isAddressValid(addressData, errors, validationData))
		{
			validationData = verifyAddresByService(addressData, errors, validationData);
		}
		final AddressValidationWsDTO dto = dataMapper.map(validationData, AddressValidationWsDTO.class, fields);
		return dto;
	}

	/**
	 * Verifies address
	 *
	 * @param address
	 *           address object
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @bodyparams firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode)
	 * @return verification results. If address is incorrect there are information about errors and suggested addresses
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/addresses/verification", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public AddressValidationWsDTO verifyAddress(@RequestBody final AddressWsDTO address,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		// validation is a bit different here
		final AddressData addressData = dataMapper.map(address, AddressData.class,
				"firstName,lastName,titleCode,line1,line2,town,postalCode,country(isocode),region(isocode)");
		final Errors errors = new BeanPropertyBindingResult(addressData, ADDRESS_DATA);
		AddressValidationData validationData = new AddressValidationData();

		if (isAddressValid(addressData, errors, validationData))
		{
			validationData = verifyAddresByService(addressData, errors, validationData);
		}
		final AddressValidationWsDTO dto = dataMapper.map(validationData, AddressValidationWsDTO.class, fields);
		return dto;
	}

	/**
	 * Checks if address is valid by a validators
	 *
	 * @formparam addressData
	 * @formparam errors
	 * @formparam validationData
	 * @return true - adress is valid , false - address is invalid
	 */
	private boolean isAddressValid(final AddressData addressData, final Errors errors, final AddressValidationData validationData)
	{
		addressValidator.validate(addressData, errors);

		if (errors.hasErrors())
		{
			validationData.setDecision(AddressVerificationDecision.REJECT.toString());
			validationData.setErrors(createResponseErrors(errors));
			return false;
		}
		return true;
	}

	/**
	 * Verifies address by commerce service
	 *
	 * @formparam addressData
	 * @formparam errors
	 * @formparam validationData
	 * @return object with verification errors and suggested addresses list
	 */
	private AddressValidationData verifyAddresByService(final AddressData addressData, final Errors errors,
			final AddressValidationData validationData)
	{
		final AddressVerificationResult<AddressVerificationDecision> verificationDecision = addressVerificationFacade
				.verifyAddressData(addressData);
		if (verificationDecision.getErrors() != null && !verificationDecision.getErrors().isEmpty())
		{
			populateErrors(errors, verificationDecision);
			validationData.setErrors(createResponseErrors(errors));
		}

		validationData.setDecision(verificationDecision.getDecision().toString());

		if (verificationDecision.getSuggestedAddresses() != null && verificationDecision.getSuggestedAddresses().size() > 0)
		{
			final AddressDataList addressDataList = new AddressDataList();
			addressDataList.setAddresses(verificationDecision.getSuggestedAddresses());
			validationData.setSuggestedAddressesList(addressDataList);
		}

		return validationData;
	}

	private ErrorListWsDTO createResponseErrors(final Errors errors)
	{
		final List<ErrorWsDTO> webserviceErrorDto = new ArrayList<>();
		validationErrorConverter.convert(errors, webserviceErrorDto);
		final ErrorListWsDTO webserviceErrorList = new ErrorListWsDTO();
		webserviceErrorList.setErrors(webserviceErrorDto);
		return webserviceErrorList;
	}

	/**
	 * Populates Errors object
	 *
	 * @param errors
	 * @param addressVerificationResult
	 */
	private void populateErrors(final Errors errors,
			final AddressVerificationResult<AddressVerificationDecision> addressVerificationResult)
	{
		addressDataErrorsPopulator.populate(addressVerificationResult, errors);
	}

	/**
	 * Return customer's credit card payment details list.
	 *
	 * @queryparam saved Type of payment details
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @return Customer's payment details list
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/paymentdetails", method = RequestMethod.GET)
	@ResponseBody
	public PaymentDetailsListWsDTO getPaymentInfos(@RequestParam(required = false, defaultValue = "false") final boolean saved,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getPaymentInfos");
		}

		final CCPaymentInfoDatas paymentInfoDataList = new CCPaymentInfoDatas();
		paymentInfoDataList.setPaymentInfos(userFacade.getCCPaymentInfos(saved));

		return dataMapper.map(paymentInfoDataList, PaymentDetailsListWsDTO.class, fields);
	}

	/**
	 * Returns customer's credit card payment details for a given id.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @return Customer's credit card payment details
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = PAYMENT_ID, method = RequestMethod.GET)
	@ResponseBody
	public PaymentDetailsWsDTO getPaymentDetails(@PathVariable final String paymentDetailsId,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final PaymentDetailsWsDTO dto = dataMapper.map(getPaymentInfo(paymentDetailsId), PaymentDetailsWsDTO.class, fields);
		return dto;
	}

	public CCPaymentInfoData getPaymentInfo(final String paymentDetailsId)
	{
		LOG.debug("getPaymentInfo : id = " + sanitize(paymentDetailsId));
		try
		{
			final CCPaymentInfoData paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentDetailsId);
			if (paymentInfoData == null)
			{
				throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.",
						RequestParameterException.UNKNOWN_IDENTIFIER, "paymentDetailsId");
			}
			return paymentInfoData;
		}
		catch (final PKException e)
		{
			throw new RequestParameterException("Payment details [" + sanitize(paymentDetailsId) + "] not found.",
					RequestParameterException.UNKNOWN_IDENTIFIER, "paymentDetailsId", e);
		}
	}

	/**
	 * Removes customer's credit card payment details based on its ID.
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = PAYMENT_ID, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deletePaymentInfo(@PathVariable final String paymentDetailsId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("deletePaymentInfo: id = " + sanitize(paymentDetailsId));
		}
		getPaymentInfo(paymentDetailsId);
		userFacade.removeCCPaymentInfo(paymentDetailsId);
	}

	/**
	 * Updates existing customer's credit card payment details based on its ID. Only attributes given in request will be
	 * changed.
	 *
	 * @formparam accountHolderName Name on card. This parameter is required.
	 * @formparam cardNumber Card number. This parameter is required.
	 * @formparam cardType Card type. This parameter is required. Call GET /{baseSiteId}/cardtypes beforehand to see what
	 *            card types are supported
	 * @formparam expiryMonth Month of expiry date. This parameter is required.
	 * @formparam expiryYear Year of expiry date. This parameter is required.
	 * @formparam issueNumber
	 * @formparam startMonth
	 * @formparam startYear
	 * @formparam subscriptionId
	 * @formparam saved Parameter defines if the payment details should be saved for the customer and than could be
	 *            reused for future orders.
	 * @formparam defaultPaymentInfo Parameter defines if the payment details should be used as default for customer.
	 * @formparam billingAddress.firstName Customer's first name. This parameter is required.
	 * @formparam billingAddress.lastName Customer's last name. This parameter is required.
	 * @formparam billingAddress.titleCode Customer's title code. This parameter is required. For a list of codes, see
	 *            /{baseSiteId}/titles resource
	 * @formparam billingAddress.country.isocode Country isocode. This parameter is required and have influence on how
	 *            rest of address parameters are validated (e.g. if parameters are required :
	 *            line1,line2,town,postalCode,region.isocode)
	 * @formparam billingAddress.line1 First part of address. If this parameter is required depends on country (usually
	 *            it is required).
	 * @formparam billingAddress.line2 Second part of address. If this parameter is required depends on country (usually
	 *            it is not required)
	 * @formparam billingAddress.town Town name. If this parameter is required depends on country (usually it is
	 *            required)
	 * @formparam billingAddress.postalCode Postal code. If this parameter is required depends on country (usually it is
	 *            required)
	 * @formparam billingAddress.region.isocode Isocode for region. If this parameter is required depends on country.
	 * @throws RequestParameterException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = PAYMENT_ID, method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.OK)
	public void updatePaymentInfo(@PathVariable final String paymentDetailsId, final HttpServletRequest request)
			throws RequestParameterException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updatePaymentInfo: id = " + sanitize(paymentDetailsId));
		}

		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);

		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		final Collection<PaymentInfoOption> options = new ArrayList<PaymentInfoOption>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		httpRequestPaymentInfoPopulator.populate(request, paymentInfoData, options);
		validate(paymentInfoData, PAYMENT_DETAILS, ccPaymentInfoValidator);

		userFacade.updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			userFacade.setDefaultPaymentInfo(paymentInfoData);
		}
	}

	/**
	 * Updates existing customer's credit card payment details based on its ID. Only attributes given in request will be
	 * changed.
	 *
	 * @param paymentDetails
	 *           payment details object
	 * @bodyparams accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,
	 *             subscriptionId
	 *             ,defaultPaymentInfo,saved,billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,
	 *             region(isocode),country(isocode),defaultAddress)
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = PAYMENT_ID, method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void updatePaymentInfo(@PathVariable final String paymentDetailsId,
			@RequestBody final PaymentDetailsWsDTO paymentDetails) throws RequestParameterException
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();



		dataMapper.map(paymentDetails, paymentInfoData,


				"accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,defaultPaymentInfo,saved,billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress)",
				false);
		validate(paymentInfoData, PAYMENT_DETAILS, ccPaymentInfoValidator);

		userFacade.updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			userFacade.setDefaultPaymentInfo(paymentInfoData);
		}

	}

	/**
	 * Updates existing customer's credit card payment info based on the payment info ID. Attributes not given in request
	 * will be defined again (set to null or default).
	 *
	 * @formparam accountHolderName Name on card. This parameter is required.
	 * @formparam cardNumber Card number. This parameter is required.
	 * @formparam cardType Card type. This parameter is required. Call GET /{baseSiteId}/cardtypes beforehand to see what
	 *            card types are supported
	 * @formparam expiryMonth Month of expiry date. This parameter is required.
	 * @formparam expiryYear Year of expiry date. This parameter is required.
	 * @formparam issueNumber
	 * @formparam startMonth
	 * @formparam startYear
	 * @formparam subscriptionId
	 * @formparam saved Parameter defines if the payment details should be saved for the customer and than could be
	 *            reused for future orders.
	 * @formparam defaultPaymentInfo Parameter defines if the payment details should be used as default for customer.
	 * @formparam billingAddress.firstName Customer's first name. This parameter is required.
	 * @formparam billingAddress.lastName Customer's last name. This parameter is required.
	 * @formparam billingAddress.titleCode Customer's title code. This parameter is required. For a list of codes, see
	 *            /{baseSiteId}/titles resource
	 * @formparam billingAddress.country.isocode Country isocode. This parameter is required and have influence on how
	 *            rest of address parameters are validated (e.g. if parameters are required :
	 *            line1,line2,town,postalCode,region.isocode)
	 * @formparam billingAddress.line1 First part of address. If this parameter is required depends on country (usually
	 *            it is required).
	 * @formparam billingAddress.line2 Second part of address. If this parameter is required depends on country (usually
	 *            it is not required)
	 * @formparam billingAddress.town Town name. If this parameter is required depends on country (usually it is
	 *            required)
	 * @formparam billingAddress.postalCode Postal code. If this parameter is required depends on country (usually it is
	 *            required)
	 * @formparam billingAddress.region.isocode Isocode for region. If this parameter is required depends on country.
	 * @throws RequestParameterException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = PAYMENT_ID, method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void putPaymentInfo(@PathVariable final String paymentDetailsId, final HttpServletRequest request)
			throws RequestParameterException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updatePaymentInfo: id = " + sanitize(paymentDetailsId));
		}

		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);

		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();
		paymentInfoData.setAccountHolderName(null);
		paymentInfoData.setCardNumber(null);
		paymentInfoData.setCardType(null);
		paymentInfoData.setExpiryMonth(null);
		paymentInfoData.setExpiryYear(null);
		paymentInfoData.setDefaultPaymentInfo(false);
		paymentInfoData.setSaved(false);

		paymentInfoData.setIssueNumber(null);
		paymentInfoData.setStartMonth(null);
		paymentInfoData.setStartYear(null);
		paymentInfoData.setSubscriptionId(null);

		final AddressData address = paymentInfoData.getBillingAddress();
		address.setFirstName(null);
		address.setLastName(null);
		address.setCountry(null);
		address.setLine1(null);
		address.setLine2(null);
		address.setPostalCode(null);
		address.setRegion(null);
		address.setTitle(null);
		address.setTown(null);

		final Collection<PaymentInfoOption> options = new ArrayList<PaymentInfoOption>();
		options.add(PaymentInfoOption.BASIC);
		options.add(PaymentInfoOption.BILLING_ADDRESS);

		httpRequestPaymentInfoPopulator.populate(request, paymentInfoData, options);
		validate(paymentInfoData, PAYMENT_DETAILS, ccPaymentInfoValidator);

		userFacade.updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			userFacade.setDefaultPaymentInfo(paymentInfoData);
		}
	}

	/**
	 * Updates existing customer's credit card payment info based on the payment info ID. Attributes not given in request
	 * will be defined again (set to null or default).
	 *
	 * @param paymentDetails
	 *           payment details object
	 * @bodyparams accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,
	 *             subscriptionId
	 *             ,defaultPaymentInfo,saved,billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,
	 *             region(isocode),country(isocode),defaultAddress)
	 * @throws RequestParameterException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = PAYMENT_ID, method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.OK)
	public void putPaymentInfo(@PathVariable final String paymentDetailsId, @RequestBody final PaymentDetailsWsDTO paymentDetails)
			throws RequestParameterException
	{
		final CCPaymentInfoData paymentInfoData = getPaymentInfo(paymentDetailsId);
		final boolean isAlreadyDefaultPaymentInfo = paymentInfoData.isDefaultPaymentInfo();

		validate(paymentDetails, PAYMENT_DETAILS, paymentDetailsDTOValidator);


		dataMapper.map(paymentDetails, paymentInfoData,


				"accountHolderName,cardNumber,cardType,issueNumber,startMonth,expiryMonth,startYear,expiryYear,subscriptionId,defaultPaymentInfo,saved,billingAddress(firstName,lastName,titleCode,line1,line2,town,postalCode,region(isocode),country(isocode),defaultAddress)",
				true);

		userFacade.updateCCPaymentInfo(paymentInfoData);
		if (paymentInfoData.isSaved() && !isAlreadyDefaultPaymentInfo && paymentInfoData.isDefaultPaymentInfo())
		{
			userFacade.setDefaultPaymentInfo(paymentInfoData);
		}
	}

	/**
	 * Returns all customer groups of a customer.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in the response)
	 * @return All customer groups of a customer.
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/customergroups", method = RequestMethod.GET)
	@ResponseBody
	public UserGroupListWsDTO getAllCustomerGroupsForCustomer(@PathVariable final String userId,
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserGroupDataList userGroupDataList = new UserGroupDataList();
		userGroupDataList.setUserGroups(customerGroupFacade.getCustomerGroupsForUser(userId));
		return dataMapper.map(userGroupDataList, UserGroupListWsDTO.class, fields);
	}


	/**
	 * Sends invitation to friend.
	 *
	 * @queryparam emailId,friendEmailId,channel
	 * @return result sending email
	 * @throws CMSItemNotFoundException
	 * @throws MalformedURLException
	 */
	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/inviteFriend", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto inviteFriend(@RequestParam final String custEmailId, @RequestParam String textMessage,
			@RequestParam final List friendEmailIdList, final HttpServletRequest request)
			throws RequestParameterException, CMSItemNotFoundException, MalformedURLException
	{
		final UserResultWsDto result = new UserResultWsDto();
		final MplCustomerProfileData mplCustData = mplCustomerProfileService.getCustomerProfileDetail(custEmailId);
		boolean successFlag = false;
		final String affiliateId = mplCustData.getUid();
		final URL requestUrl = new URL(request.getRequestURL().toString());
		final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
		final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;
		final String specificUrl = MarketplacewebservicesConstants.LINK_LOGIN + MarketplacewebservicesConstants.QS
				+ MarketplacewebservicesConstants.AFFILIATEID + MarketplacewebservicesConstants.EQUALS + affiliateId;
		final String securePasswordUrl = baseUrl + specificUrl;
		//final String inviteFriendUrl = urlForEmailContext(request, specificUrl);
		try
		{
			if (friendEmailIdList.size() > 0)
			{
				if (!friendsInviteFacade.isEmailEqualsToCustomer(friendEmailIdList))
				{
					if (!friendsInviteFacade.checkUniquenessOfEmail(friendEmailIdList))
					{
						final FriendsInviteData friendsData = new FriendsInviteData();
						friendsData.setCustomerEmail(custEmailId);
						friendsData.setFriendsEmailList(friendEmailIdList);
						friendsData.setAffiliateId(mplCustData.getUid());
						friendsData.setInviteBaseUrl(securePasswordUrl);
						final boolean flag = friendsInviteFacade.inviteFriends(friendsData);

						if (flag)
						{
							if (StringUtils.isEmpty(textMessage))
							{
								textMessage = configurationService.getConfiguration()
										.getString(MarketplacecommerceservicesConstants.INVITE_FRIENDS_MESSAGE_KEY, "NA");

							}
							friendsInviteFacade.sendInvite(friendsData, textMessage);
							successFlag = true;
						}
						else
						{
							successFlag = false;
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9100);
						}
					}
					else
					{
						successFlag = false;
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9101);
					}
				}
				else
				{
					successFlag = false;
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9102);
				}
			}
		}

		catch (final ModelSavingException me)
		{
			successFlag = false;
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(MarketplacecommerceservicesConstants.MODEL_NOT_SAVED);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				result.setError(e.getMessage());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return result;
	}

	/**
	 * @description method is called to generate update profileURL
	 * @param request
	 * @param specificUrl
	 * @throws CMSItemNotFoundException
	 */





	/**
	 * Sends invitation to friend.
	 *
	 * @queryparam emailId,friendEmailId,channel
	 * @return result sending email
	 */
	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/getFriendsInviteMessageText", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto getFriendsInviteMessageText() throws RequestParameterException
	{
		final UserResultWsDto result = new UserResultWsDto();
		boolean successFlag = false;
		try
		{
			final String messageText = configurationService.getConfiguration()
					.getString(MarketplacecommerceservicesConstants.INVITE_FRIENDS_MESSAGE_KEY);
			if (!StringUtils.isEmpty(messageText))
			{
				result.setFriendsInviteMessageText(messageText);
				successFlag = true;
			}
		}
		catch (final ModelSavingException me)
		{
			successFlag = false;
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + me);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(MarketplacecommerceservicesConstants.MODEL_NOT_SAVED);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return result;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + ":" + e);
			return result;
		}

		catch (final Exception e)
		{
			LOG.error("Exception" + e);
			successFlag = false;
		}

		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			result.setFriendsInviteMessageText(MarketplacecommerceservicesConstants.EMPTY);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return result;
	}

	/**
	 * add new Shipping Address to the addresses.
	 *
	 * @queryparam emailId,FirstName,LastName,Line1,Line2,Line3,Town,State,CountryIso,PostalCode,Phone,AddressType
	 * @return result after adding address
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/addShippingAddress", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto addShippingAddress(@RequestParam final String emailId, @RequestParam final String firstName,
			@RequestParam final String lastName, @RequestParam final String line1, @RequestParam final String line2,
			@RequestParam final String line3, @RequestParam final String town, @RequestParam final String state,
			@RequestParam final String countryIso, @RequestParam final String postalCode, @RequestParam final String phone,
			@RequestParam final String addressType, @RequestParam final boolean defaultFlag) throws WebserviceValidationException
	{

		String errorMsg = null;
		boolean successFlag = false;

		final UserResultWsDto result = new UserResultWsDto();
		try
		{
			//CAR-74
			//MplCustomerProfileData mplCustData = new MplCustomerProfileData();
			//mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
			//LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
			//final UserModel user = userService.getUserForUID(mplCustData.getUid());
			final UserModel user = userService.getCurrentUser();
			LOG.debug(CUSTOMER_MESSAGE + user.getUid());

			final AddressData addressData = new AddressData();

			errorMsg = validateStringField(countryIso, AddressField.COUNTRY, MAX_FIELD_LENGTH_COUNTRY);
			validation(errorMsg);
			errorMsg = validateStringField(firstName, AddressField.FIRSTNAME, MAX_FIELD_LENGTH_NEW);
			validation(errorMsg);
			errorMsg = validateStringField(lastName, AddressField.LASTNAME, MAX_FIELD_LENGTH_NEW);
			validation(errorMsg);
			errorMsg = validateStringField(line1, AddressField.LINE1, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(line2, AddressField.LINE2, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(line3, AddressField.LINE3, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(town, AddressField.TOWN, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(postalCode, AddressField.POSTCODE, MAX_POSTCODE_LENGTH);
			validation(errorMsg);
			errorMsg = validateStringField(addressType, AddressField.ADDRESSTYPE, MAX_FIELD_LENGTH);
			validation(errorMsg);
			errorMsg = validateStringField(state, AddressField.STATE, MAX_FIELD_LENGTH_STATE);
			validation(errorMsg);
			errorMsg = validateStringField(phone, AddressField.MOBILE, MAX_POSTCODE_LENGTH);
			validation(errorMsg);
			if (null == errorMsg)
			{
				addressData.setFirstName(firstName);
				addressData.setLastName(lastName);
				addressData.setLine1(line1);
				addressData.setLine2(line2);
				addressData.setLine3(line3);
				addressData.setTown(town);
				addressData.setState(state);
				addressData.setCountry(getI18NFacade().getCountryForIsocode(countryIso));
				addressData.setPostalCode(postalCode);
				addressData.setAddressType(addressType);
				addressData.setDefaultAddress(defaultFlag);
				addressData.setShippingAddress(true);
				addressData.setVisibleInAddressBook(true);
				addressData.setPhone(phone);

				LOG.debug("addrestype=addaddress" + addressData.getAddressType());
				//CAR-74
				final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

				if (null != currentCustomer)
				{
					final boolean makeThisAddressTheDefault = addressData.isDefaultAddress()
							|| (currentCustomer.getDefaultShipmentAddress() == null && addressData.isVisibleInAddressBook());

					final AddressModel addressmodel = modelService.create(AddressModel.class);
					addressReversePopulator.populate(addressData, addressmodel);
					addressmodel.setCellphone(addressData.getPhone());
					addressmodel.setDistrict(addressData.getState());
					addressmodel.setAddressType(addressData.getAddressType());
					addressmodel.setLocality(addressData.getLocality());
					addressmodel.setAddressLine3(addressData.getLine3());
					customerAccountService.saveAddressEntry(currentCustomer, addressmodel);

					addressData.setId(addressmodel.getPk().toString());

					if (makeThisAddressTheDefault)
					{
						customerAccountService.setDefaultAddressEntry(currentCustomer, addressmodel);
					}
					successFlag = true;
				}
			}
		}
		catch (final ModelSavingException me)
		{
			successFlag = false;
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(errorMsg);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}


		catch (final Exception e)
		{
			result.setError(errorMsg);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			successFlag = false;
		}

		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(errorMsg);
		}
		return result;
	}

	/**
	 * get Shipping Address from the addresses.
	 *
	 * @queryparam emailId
	 * @return result shipping addresses
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/getShippingAddresses", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public AddressListWsDTO getShippingAddresses(@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		AddressListWsDTO dto = new AddressListWsDTO();
		try
		{
			final List<AddressData> addressList = accountAddressFacade.getAddressBook();
			final AddressDataList addressDataList = new AddressDataList();
			final List<AddressData> addressData = new ArrayList();
			boolean successFlag = false;
			if (!(addressList != null))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9204);
			}

			for (final AddressData addressEntry : addressList)
			{
				if (addressEntry.isShippingAddress())
				{
					addressData.add(addressEntry);

				}
			}
			addressDataList.setAddresses(addressData);
			successFlag = true;
			dto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			dto = dataMapper.map(addressDataList, AddressListWsDTO.class, fields);
			if (successFlag)
			{
				dto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				dto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				dto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				dto.setErrorCode(e.getErrorCode());
			}
			dto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				dto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				dto.setErrorCode(e.getErrorCode());
			}
			dto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return dto;
	}


	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/addProductInWishlist", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto addProductInWishlist(@PathVariable final String emailId,
			@RequestParam(required = false) final String wishlistName, @RequestParam final String productCode,
			@RequestParam final String ussid, @RequestParam(required = false) final boolean isSelectedSize)
			throws RequestParameterException
	{
		final UserResultWsDto result = new UserResultWsDto();
		boolean add = false;
		String name = null;
		Wishlist2Model getWishlistforName = null;
		int getWishlistforNameCount = 0;
		final List<WishlistData> wishListData = new ArrayList<WishlistData>();
		Wishlist2Model requiredWl = null;
		List<Wishlist2Model> allWishlists = null;
		try
		{
			//CAR Project performance issue fixed
			final UserModel user = userService.getCurrentUser();
			if (null == user)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}

			if (StringUtils.isNotEmpty(wishlistName))
			{
				name = wishlistName;
				getWishlistforName = wishlistFacade.findMobileWishlistswithName(user, name);
				if (null != getWishlistforName)
				{
					requiredWl = getWishlistforName;
				}
			}
			//CAR Project performance issue fixed
			else
			{
				getWishlistforNameCount = wishlistFacade.findMobileWishlistswithNameCount(user,
						MarketplacecommerceservicesConstants.WISHLIST_NO);

				name = MarketplacecommerceservicesConstants.WISHLIST_NO + MarketplacecommerceservicesConstants.UNDER_SCORE
						+ (++getWishlistforNameCount);
			}

			//CAR Project performance issue fixed
			if (StringUtils.isNotEmpty(ussid) && StringUtils.isNotEmpty(productCode))
			{
				if (null != requiredWl)
				{
					//CAR Project performance issue fixed

					//add to existing wishlists
					//add = wishlistFacade.addProductToWishlist(requiredWl, productCode, ussid, isSelectedSize);
					add = wishlistFacade.addProductToWishlistMobile(requiredWl, productCode, ussid, isSelectedSize);
					if (add)
					{
						result.setStatus(MarketplacecommerceservicesConstants.PRODUCT_ADDED);
						return result;
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9207);
					}
				}
				else
				{
					allWishlists = wishlistFacade.getAllWishlists();
					//if (!allWishlists.isEmpty() && allWishlists.size() > 0)
					if (CollectionUtils.isNotEmpty(allWishlists))
					{
						final List<String> wishlistnames = new ArrayList<String>();
						for (final Wishlist2Model wishlist2Model : allWishlists)
						{
							wishlistnames.add(wishlist2Model.getName());
						}

						result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						result.setError(MarketplacecommerceservicesConstants.Already_Have_Wishlists);
						result.setWishlistNames(wishlistnames);
						return result;
					}
					else
					{
						//add product to new wishlist if there is no wishlist present
						//CAR Project performance issue fixed
						final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, name, productCode);
						//add = wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, isSelectedSize);
						add = wishlistFacade.addProductToWishlistMobile(createdWishlist, productCode, ussid, isSelectedSize);
						final WishlistData wishData = new WishlistData();
						wishData.setParticularWishlistName(createdWishlist.getName());
						wishData.setProductCode(productCode);
						wishListData.add(wishData);
						if (add)
						{
							result.setStatus(MarketplacecommerceservicesConstants.PRODUCT_ADDED);
							return result;

						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9207);
						}
					}
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9208);
			}

		}
		catch (final ModelSavingException me)
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + me.getMessage());
			return result;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return result;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return result;
		}
		catch (final Exception e)
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != e.getMessage())
			{
				result.setError(e.getMessage());
			}
			return result;
		}
	}

	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/addAddress", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto addAddress(@RequestParam final String emailId, @RequestParam final String firstName,
			@RequestParam final String lastName, @RequestParam final String line1, @RequestParam final String line2,
			@RequestParam final String line3, @RequestParam(required = false) final String landmark,


			@RequestParam final String town, @RequestParam final String state, @RequestParam final String countryIso,
			@RequestParam final String postalCode, @RequestParam final String phone, @RequestParam final String addressType,
			@RequestParam final boolean defaultFlag)


			throws RequestParameterException
	{

		String errorMsg = null;
		final UserResultWsDto result = new UserResultWsDto();
		boolean successFlag = false;
		try
		{
			//CAR-76
			//final MplCustomerProfileData mplCustData = new MplCustomerProfileData();
			//mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
			//LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
			//final UserModel user = userService.getUserForUID(mplCustData.getUid());
			final UserModel user = userService.getCurrentUser();
			LOG.debug(CUSTOMER_MESSAGE + user.getUid());


			final AddressData newAddress = new AddressData();

			errorMsg = validateStringField(countryIso, AddressField.COUNTRY, MAX_FIELD_LENGTH_COUNTRY);
			validation(errorMsg);
			errorMsg = validateStringField(firstName, AddressField.FIRSTNAME, MAX_FIELD_LENGTH_NEW);
			validation(errorMsg);
			errorMsg = validateStringField(lastName, AddressField.LASTNAME, MAX_FIELD_LENGTH_NEW);
			validation(errorMsg);
			errorMsg = validateStringField(line1, AddressField.LINE1, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(line2, AddressField.LINE2, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(line3, AddressField.LINE3, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			if (null != landmark)
			{
				errorMsg = validateStringField(landmark, AddressField.LANDMARK, MAX_LANDMARK_LENGTH);
			}
			validation(errorMsg);
			errorMsg = validateStringField(town, AddressField.TOWN, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(postalCode, AddressField.POSTCODE, MAX_POSTCODE_LENGTH);
			validation(errorMsg);
			errorMsg = validateStringField(addressType, AddressField.ADDRESSTYPE, MAX_FIELD_LENGTH);
			validation(errorMsg);
			errorMsg = validateStringField(state, AddressField.STATE, MAX_FIELD_LENGTH_STATE);
			validation(errorMsg);
			errorMsg = validateStringField(phone, AddressField.MOBILE, MAX_POSTCODE_LENGTH);
			validation(errorMsg);

			if (null == errorMsg)
			{
				newAddress.setFirstName(firstName);
				newAddress.setLastName(lastName);
				newAddress.setLine1(line1);
				newAddress.setLine2(line2);
				newAddress.setLine3(line3);
				newAddress.setLandmark(landmark);
				newAddress.setTown(town);
				newAddress.setPostalCode(postalCode);
				newAddress.setBillingAddress(false);
				newAddress.setShippingAddress(true);
				newAddress.setVisibleInAddressBook(true);
				newAddress.setAddressType(addressType);
				newAddress.setPhone(phone);
				newAddress.setState(state);
				newAddress.setCountry(getI18NFacade().getCountryForIsocode(countryIso));
				//				newAddress.setDefaultAddress(defaultFlag);

				if (userFacade.isAddressBookEmpty())
				{
					newAddress.setDefaultAddress(true);
					newAddress.setVisibleInAddressBook(true);
				}
				else
				{
					newAddress.setDefaultAddress(defaultFlag);
				}

				LOG.debug("addrestype=addaddress" + newAddress.getAddressType());
				//CAR-76
				final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

				if (null != currentCustomer)
				{
					final boolean makeThisAddressTheDefault = newAddress.isDefaultAddress()
							|| (currentCustomer.getDefaultShipmentAddress() == null && newAddress.isVisibleInAddressBook());
					LOG.debug("AddresId" + newAddress.getId());
					final AddressModel addressmodel = modelService.create(AddressModel.class);
					addressReversePopulator.populate(newAddress, addressmodel);
					addressmodel.setCellphone(newAddress.getPhone());
					addressmodel.setDistrict(newAddress.getState());
					addressmodel.setAddressType(newAddress.getAddressType());
					addressmodel.setLocality(newAddress.getLocality());
					addressmodel.setAddressLine3(newAddress.getLine3());

					customerAccountService.saveAddressEntry(currentCustomer, addressmodel);
					newAddress.setId(addressmodel.getPk().toString());

					if (makeThisAddressTheDefault)
					{
						customerAccountService.setDefaultAddressEntry(currentCustomer, addressmodel);
					}
					successFlag = true;
				}
			}
		}
		catch (final ModelSavingException me)
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (final Exception e)
		{
			result.setError(errorMsg);
		}

		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(errorMsg);
		}

		return result;
	}

	public UserResultWsDto validation(final String error)
	{
		final UserResultWsDto result = new UserResultWsDto();
		if (null != error)
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(error);

		}
		return result;
	}

	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/removeAddress", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto removeAddress(@RequestParam final String emailId, @RequestParam final String addressId)
			throws RequestParameterException
	{
		//CAR-76
		//MplCustomerProfileData mplCustData = new MplCustomerProfileData();
		//mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
		//LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
		//final UserModel user = userService.getUserForUID(mplCustData.getUid());
		final UserModel user = userService.getCurrentUser();
		LOG.debug(CUSTOMER_MESSAGE + user.getUid());
		final UserResultWsDto result = new UserResultWsDto();


		boolean successFlag = false;
		try
		{
			final AddressData addressData = new AddressData();
			addressData.setId(addressId);
			//CAR-76
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

			if (null != currentCustomer)
			{
				for (final AddressModel addressModel : getCustomerAccountService().getAddressBookEntries(currentCustomer))
				{
					if ((addressData.getId().equals(addressModel.getPk().getLongValueAsString())) && (addressData.getId() != null)
							&& (addressModel.getPk() != null))
					{
						getCustomerAccountService().deleteAddressEntry(currentCustomer, addressModel);
						successFlag = true;
						break;
					}
				}

			}
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				result.setError(e.getMessage());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return result;
	}

	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{baseSiteId}/editAddress", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto editAddress(@RequestParam final String emailId, @RequestParam final String addressId,
			@RequestParam final String firstName, @RequestParam final String lastName, @RequestParam final String line1,
			@RequestParam final String line2, @RequestParam final String line3,
			@RequestParam(required = false) final String landmark, @RequestParam final String town,


			@RequestParam final String state, @RequestParam final String countryIso, @RequestParam final String postalCode,
			@RequestParam final String phone, @RequestParam final String addressType, @RequestParam final boolean defaultFlag)
			throws RequestParameterException



	{

		String errorMsg = null;
		final AddressData newAddress = new AddressData();
		final UserResultWsDto result = new UserResultWsDto();
		boolean successFlag = false;
		try
		{
			//CAR-76
			//MplCustomerProfileData mplCustData = new MplCustomerProfileData();
			//mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
			//LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
			//final UserModel user = userService.getUserForUID(mplCustData.getUid());
			final UserModel user = userService.getCurrentUser();
			LOG.debug(CUSTOMER_MESSAGE + user.getUid());

			errorMsg = validateStringField(countryIso, AddressField.COUNTRY, MAX_FIELD_LENGTH_COUNTRY);
			validation(errorMsg);
			errorMsg = validateStringField(firstName, AddressField.FIRSTNAME, MAX_FIELD_LENGTH_NEW);
			validation(errorMsg);
			errorMsg = validateStringField(lastName, AddressField.LASTNAME, MAX_FIELD_LENGTH_NEW);
			validation(errorMsg);
			errorMsg = validateStringField(line1, AddressField.LINE1, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(line2, AddressField.LINE2, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(line3, AddressField.LINE3, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			if (null != landmark)
			{
				errorMsg = validateStringField(landmark, AddressField.LANDMARK, MAX_LANDMARK_LENGTH);
			}
			validation(errorMsg);
			errorMsg = validateStringField(town, AddressField.TOWN, MAX_FIELD_LENGTH_ADDLINE);
			validation(errorMsg);
			errorMsg = validateStringField(postalCode, AddressField.POSTCODE, MAX_POSTCODE_LENGTH);
			validation(errorMsg);
			errorMsg = validateStringField(addressType, AddressField.ADDRESSTYPE, MAX_FIELD_LENGTH);
			validation(errorMsg);
			errorMsg = validateStringField(state, AddressField.STATE, MAX_FIELD_LENGTH_STATE);
			validation(errorMsg);
			errorMsg = validateStringField(phone, AddressField.MOBILE, MAX_POSTCODE_LENGTH);
			validation(errorMsg);


			if (null == errorMsg)
			{
				newAddress.setId(addressId);
				newAddress.setFirstName(firstName);
				newAddress.setLastName(lastName);
				newAddress.setLine1(line1);
				newAddress.setLine2(line2);
				newAddress.setLine3(line3);
				newAddress.setLandmark(landmark);
				newAddress.setTown(town);
				newAddress.setPostalCode(postalCode);
				newAddress.setBillingAddress(false);
				newAddress.setShippingAddress(true);
				newAddress.setVisibleInAddressBook(true);
				newAddress.setCountry(getI18NFacade().getCountryForIsocode(countryIso));
				newAddress.setAddressType(addressType);
				LOG.debug("addrestype=" + newAddress.getAddressType());
				newAddress.setPhone(phone);
				newAddress.setState(state);
				newAddress.setDefaultAddress(defaultFlag);
				//CAR-76
				final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

				if (null != currentCustomer)
				{
					final AddressModel addressModel = customerAccountService.getAddressForCode(currentCustomer, newAddress.getId());
					LOG.debug("AddresId" + newAddress.getId());
					addressModel.setRegion(null);
					addressReversePopulator.populate(newAddress, addressModel);
					addressModel.setCellphone(newAddress.getPhone());
					addressModel.setDistrict(newAddress.getState());
					addressModel.setAddressType(newAddress.getAddressType());
					addressModel.setLocality(newAddress.getLocality());
					addressModel.setAddressLine3(newAddress.getLine3());
					customerAccountService.saveAddressEntry(currentCustomer, addressModel);
					successFlag = true;
					if (newAddress.isDefaultAddress())
					{
						customerAccountService.setDefaultAddressEntry(currentCustomer, addressModel);
					}
					else if (addressModel.equals(currentCustomer.getDefaultShipmentAddress()))
					{
						customerAccountService.clearDefaultAddressEntry(currentCustomer);
					}
				}
			}
		}
		catch (final ModelSavingException me)
		{
			successFlag = false;
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(errorMsg);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			result.setError(errorMsg);
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			successFlag = false;
		}

		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setError(errorMsg);
		}
		return result;

	}

	protected Set<OrderStatus> extractOrderStatuses(final String statuses)
	{
		final String statusesStrings[] = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		for (final String status : statusesStrings)
		{
			statusesEnum.add(OrderStatus.valueOf(status));
		}
		return statusesEnum;
	}

	protected OrderHistoriesData createOrderHistoriesData(final SearchPageData<OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();

		orderHistoriesData.setOrders(result.getResults());
		orderHistoriesData.setSorts(result.getSorts());
		orderHistoriesData.setPagination(result.getPagination());

		return orderHistoriesData;
	}

	protected CustomerModel getCurrentUserForCheckout()
	{
		return getCheckoutCustomerStrategy().getCurrentUserForCheckout();
	}

	protected enum AddressField
	{
		FIRSTNAME("firstName", "address.firstName.invalid"), LASTNAME("lastName", "address.lastName.invalid"), LINE1("line1",
				"address.line1.invalid"), LINE2("line2", "address.line2.invalid"), LINE3("line3", "address.line3.invalid"), LANDMARK(
						"landmark", "address.landmark.invalid"), TOWN("townCity", "address.townCity.invalid"), POSTCODE("postcode",
								"address.postcode.invalid"), REGION("regionIso", "address.regionIso.invalid"), COUNTRY("countryIso",
										"address.country.invalid"), ADDRESSTYPE("addressType",
												"address.addressType.invalid"), STATE("state", "address.selectState"), LOCALITY("locality",
														"address.locality.invalid"), MOBILE("mobileNo", "address.mobile.invalid");


		private final String fieldKey;
		private final String errorKey;

		private AddressField(final String fieldKey, final String errorKey)
		{
			this.fieldKey = fieldKey;
			this.errorKey = errorKey;
		}

		public String getFieldKey()
		{
			return fieldKey;
		}

		public String getErrorKey()
		{
			return errorKey;
		}
	}

	/**
	 * @description method is called to validate the StringField
	 * @param addressField
	 * @param fieldType
	 * @param maxFieldLength
	 *
	 */
	protected static String validateStringField(final String addressField, final AddressField fieldType, final int maxFieldLength)
	{
		String errorMsg = null;

		if (addressField == null || StringUtils.isEmpty(addressField) || (StringUtils.length(addressField) > maxFieldLength))
		{
			errorMsg = fieldType + MarketplacecommerceservicesConstants.EMPTYSPACE + MarketplacecommerceservicesConstants.NOT_VALID;
			return errorMsg;

		}
		return errorMsg;

	}



	/**
	 * CREATE WISHLIST
	 *
	 * @param wishlistName
	 * @return WebSerResponseWsDTO
	 * @throws RequestParameterException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/CreateWishlist", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public WebSerResponseWsDTO createNewWishlist(@RequestParam(required = false) final String wishlistName)
			throws RequestParameterException
	{
		final WebSerResponseWsDTO userResult = new WebSerResponseWsDTO();
		int getWishlistforNameCount = 0;
		try
		{
			//			List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			List<Wishlist2Model> allWishlists = null;

			final UserModel user = userService.getCurrentUser();
			String name = null;
			//			final String nameSet = null;
			//			final int wSize = 0;
			if (null == user)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			if (StringUtils.isNotEmpty(wishlistName))
			{
				name = wishlistName;
			}
			else
			{
				getWishlistforNameCount = wishlistFacade.findMobileWishlistswithNameCount(user,
						MarketplacecommerceservicesConstants.WISHLIST_NO);

				name = MarketplacecommerceservicesConstants.WISHLIST_NO + MarketplacecommerceservicesConstants.UNDER_SCORE
						+ (++getWishlistforNameCount);
			}
			allWishlists = wishlistFacade.getAllWishlists();
			//allWishlists = wishlistService.getWishlists(user);

			Wishlist2Model requiredWl = null;
			for (final Wishlist2Model wl : allWishlists)
			{
				if (null != wl && name.equals(wl.getName()))
				{
					requiredWl = wl;
					break;
				}
			}
			if (null == requiredWl)
			{
				wishlistFacade.createNewWishlist(user, name, MarketplacecommerceservicesConstants.EMPTYSPACE);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9209);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				userResult.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResult.setErrorCode(e.getErrorCode());
			}
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				userResult.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResult.setErrorCode(e.getErrorCode());
			}
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				userResult.setError(e.getMessage());
			}
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		userResult.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
		return userResult;

	} //End of create wishlist


	/**
	 * Get all Wish List data.
	 *
	 * @param request
	 * @return GetWishListWsDTO
	 * @throws RequestParameterException
	 * @throws MalformedURLException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/getAllWishlist", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public GetWishListWsDTO getAllWishlistAndProduct(final HttpServletRequest request)
			throws RequestParameterException, MalformedURLException


	{
		final GetWishListWsDTO wlDTO = new GetWishListWsDTO();
		GetWishListDataWsDTO wldDTO = new GetWishListDataWsDTO();
		final List<GetWishListDataWsDTO> wldDTOList = new ArrayList<GetWishListDataWsDTO>();
		GetWishListProductWsDTO wldpDTO = new GetWishListProductWsDTO();
		List<GetWishListProductWsDTO> wldpDTOList = new ArrayList<GetWishListProductWsDTO>();
		List<Wishlist2Model> allWishlists;
		String selectedSize = null;
		int entryModelSize = 0;//TPR-5787
		try
		{
			allWishlists = wishlistFacade.getAllWishlists();
			LOG.debug("Step1-************************Wishlist");
			//allWishlists = wishlistService.getWishlists();
			if (CollectionUtils.isNotEmpty(allWishlists))
			{
				for (final Wishlist2Model requiredWl : allWishlists)
				{
					LOG.debug("Step2-************************Wishlist");
					if (null != requiredWl)
					{
						LOG.debug("Step3-************************Wishlist");
						wldDTO = new GetWishListDataWsDTO();
						List<Wishlist2EntryModel> entryModels = null;
						if (null != requiredWl.getEntries())
						{
							entryModels = requiredWl.getEntries();
						}
						if (null != requiredWl.getName())
						{
							wldDTO.setName(requiredWl.getName());
							wldDTO.setCreation(requiredWl.getCreationtime());
						}
						//wldDTO.setCount(new Integer(entryModels.size())); Avoid instantiating Integer objects. Call Integer.valueOf() instead.
						//wldDTO.setCount(Integer.valueOf(entryModels.size()));

						wldpDTOList = new ArrayList<GetWishListProductWsDTO>();
						for (final Wishlist2EntryModel entryModel : entryModels)
						{
							LOG.debug("Step4-************************Wishlist");
							if (entryModel.getIsDeleted() == null
									|| (entryModel.getIsDeleted() != null && !entryModel.getIsDeleted().booleanValue()))//TPR-5787 check added
							{
								LOG.debug("Step5-************************Wishlist");
								wldpDTO = new GetWishListProductWsDTO();
								ProductData productData1 = null;
								if (null != entryModel.getProduct())
								{
									//								productData1 = productFacade.getProductForOptions(entryModel.getProduct(), Arrays.asList(
									//										ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
									//										ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW,
									//										ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.SELLER));
									productData1 = productFacade.getProductForOptions(entryModel.getProduct(),
											Arrays.asList(ProductOption.BASIC, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
													ProductOption.CATEGORIES, ProductOption.STOCK, ProductOption.SELLER));


								}
								LOG.debug("Step6-************************Wishlist");
								if (StringUtils.isNotEmpty(entryModel.getProduct().getCode()))

								{
									wldpDTO.setProductcode(entryModel.getProduct().getCode());

								}
								if (null != productData1 && StringUtils.isNotEmpty(productData1.getName()))

								{
									wldpDTO.setProductName(productData1.getName());

								}
								if (null != productData1 && null != productData1.getRootCategory()
										&& !productData1.getRootCategory().isEmpty())

								{
									wldpDTO.setRootCategory(productData1.getRootCategory());

								}
								//							if (null != productData1 && null != mplProductWebService.getCategoryCodeOfProduct(productData1)
								//									&& !mplProductWebService.getCategoryCodeOfProduct(productData1).isEmpty())

								//							{
								//								wldpDTO.setProductCategoryId(mplProductWebService.getCategoryCodeOfProduct(productData1));

								//							}

								final String prodCategory = mplProductWebService.getCategoryCodeOfProduct(productData1);
								LOG.debug("Step7-************************Wishlist");
								if (null != productData1 && StringUtils.isNotEmpty(prodCategory))
								{
									wldpDTO.setProductCategoryId(prodCategory);

								}
								if (null != productData1 && null != productData1.getBrand()
										&& StringUtils.isNotEmpty(productData1.getBrand().getBrandname()))
								{
									wldpDTO.setProductBrand(productData1.getBrand().getBrandname());
								}
								if (null != productData1 && null != productData1.getImages())
								{
									//Set product image(thumbnail) url
									for (final ImageData img : productData1.getImages())
									{
										/*
										 * if (null != img && StringUtils.isNotEmpty(img.getFormat()) //&&
										 * img.getFormat().toLowerCase().equals(MarketplacecommerceservicesConstants.THUMBNAIL)
										 * Sonar fix &&
										 * img.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.THUMBNAIL))
										 */
										if (null != img && StringUtils.isNotEmpty(img.getFormat())
										//&& img.getFormat().toLowerCase().equals(MarketplacecommerceservicesConstants.SEARCHPAGE) Sonar fix
												&& img.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.SEARCHPAGE))
										{
											wldpDTO.setImageURL(img.getUrl());
										}

									}
								}
								if (StringUtils.isNotEmpty(entryModel.getUssid()))
								{
									wldpDTO.setUSSID(entryModel.getUssid());
								}
								if (null != productData1 && StringUtils.isNotEmpty(productData1.getRootCategory()))
								{
									wldpDTO.setRootCategory(productData1.getRootCategory());
								}
								if (null != entryModel.getSizeSelected() && entryModel.getSizeSelected().booleanValue())
								{
									selectedSize = "Y";
								}
								else
								{
									selectedSize = "N";
								}
								if (!StringUtils.isEmpty(selectedSize))
								{
									wldpDTO.setSizeSelected(selectedSize);
								}
								if (null != productData1 && StringUtils.isNotEmpty(productData1.getColour()))
								{
									wldpDTO.setColor(productData1.getColour());
								}
								if (null != productData1 && null != productData1.getSize())
								{
									wldpDTO.setSize(productData1.getSize());
								}
								if (null != productData1 && null != productData1.getDescription())
								{
									wldpDTO.setDescription(productData1.getDescription());
								}
								if (null != productData1 && null != entryModel.getAddedDate())
								{
									wldpDTO.setDate(entryModel.getAddedDate());
								}
								String delistMessage = MarketplacewebservicesConstants.EMPTY;
								boolean delisted = false;
								if (null != productData1 && null != productData1.getSeller() && productData1.getSeller().size() > 0)
								{
									final List<SellerInformationData> sellerDatas = productData1.getSeller();
									for (final SellerInformationData sellerData : sellerDatas)
									{
										if (null != sellerData && StringUtils.isNotEmpty(sellerData.getUssid())
												&& StringUtils.isNotEmpty(entryModel.getUssid())
												&& sellerData.getUssid().equals(entryModel.getUssid()))
										{
											if (StringUtils.isNotEmpty(sellerData.getSellerID()))
											{
												wldpDTO.setSellerId(sellerData.getSellerID());
											}
											if (StringUtils.isNotEmpty(sellerData.getSellername()))
											{
												wldpDTO.setSellerName(sellerData.getSellername());
											}
											/*
											 * if (null != sellerData.getMopPrice()) { wldpDTO.setMop(sellerData.getMopPrice()); }
											 */
											if (null != sellerData.getAvailableStock())
											{
												wldpDTO.setAvailableStock(sellerData.getAvailableStock());
											}
											/*
											 * if (null != sellerData.getMrpPrice()) { wldpDTO.setMrp(sellerData.getMrpPrice()); }
											 * else if (null != productData1.getProductMRP()) {
											 * wldpDTO.setMrp(productData1.getProductMRP()); }
											 */

											final BuyBoxModel buyboxmodel = buyBoxFacade.getpriceForUssid(entryModel.getUssid());
											//final double price = 0.0;
											LOG.debug("Step8-************************Wishlist");
											if (null != buyboxmodel)
											{

												if (null != buyboxmodel.getSpecialPrice()
														&& buyboxmodel.getSpecialPrice().doubleValue() > 0.0)
												{
													final PriceData priceDataSP = productDetailsHelper
															.formPriceData(new Double(buyboxmodel.getSpecialPrice().doubleValue()));
													wldpDTO.setSpecialPrice(priceDataSP);

												}
												if (null != buyboxmodel.getPrice() && buyboxmodel.getPrice().doubleValue() > 0.0)

												{
													final PriceData priceDataMop = productDetailsHelper
															.formPriceData(new Double(buyboxmodel.getPrice().doubleValue()));





													wldpDTO.setMop(priceDataMop);

												}
												if (null != buyboxmodel.getMrp() && buyboxmodel.getMrp().doubleValue() > 0.0)

												{
													final PriceData priceDataMrp = productDetailsHelper
															.formPriceData(new Double(buyboxmodel.getMrp().doubleValue()));




													wldpDTO.setMrp(priceDataMrp);


												}
												break;
											}
										}
									}

								}
								else
								{//Set product price when product has no seller
									if (null != productData1 && null != productData1.getProductMRP())

									{
										wldpDTO.setMrp(productData1.getProductMRP());

									}
								}

								//	final ProductModel productModel = getMplOrderFacade().getProductForCode(entryModel.getProduct().getCode());
								LOG.debug("Step9-************************Wishlist");
								final ProductModel productModel = productService.getProductForCode(entryModel.getProduct().getCode());
								LOG.debug("Step10-************************Wishlist");
								if (null != productModel.getSellerInformationRelator())
								{
									final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
											.getSellerInformationRelator();
									for (final SellerInformationModel sellerInformationModel : sellerInfo)
									{
										if (sellerInformationModel.getSellerArticleSKU().equals(entryModel.getUssid())
												&& null != sellerInformationModel.getSellerAssociationStatus()
												&& sellerInformationModel.getSellerAssociationStatus().equals(SellerAssociationStatusEnum.NO))
										{
											delisted = true;
										}
									}
									if (!delisted)
									{
										wldpDTOList.add(wldpDTO);
									}
									else
									{
										wishlistService.removeWishlistEntry(requiredWl, entryModel);
										delistMessage = Localization
												.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_WISHLIST);
										wlDTO.setDelistedMessage(delistMessage);
									}
									//wldDTO.setCount(Integer.valueOf(entryModels.size()));
									entryModelSize += 1;//TPR-5787 modified

								}
							}
						}
						LOG.debug("Step11: The size of entries in wishlist is" + entryModelSize);//for TPR-5787
						wldDTO.setCount(Integer.valueOf(entryModelSize));//TPR-5787 modified
						wldDTO.setProducts(wldpDTOList);
						wldDTOList.add(wldDTO);
					}
				}
				wlDTO.setWishList(wldDTOList);
			}
			wlDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				wlDTO.setError(e.getErrorMessage());
			}
			wlDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				wlDTO.setError(e.getErrorMessage());
			}
			wlDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				wlDTO.setError(e.getMessage());
			}
			wlDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return wlDTO;

	}//End of Get all Wish List data.

	/**
	 * @Javadoc
	 * @param userId
	 * @param wishlistName
	 * @return result
	 * @throws RequestParameterException
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/removeProductFromWishlist", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto removeProductFromWishlist(@PathVariable final String userId, @RequestParam final String wishlistName,
			@RequestParam final String USSID) throws RequestParameterException
	{

		boolean successFlag = false;
		boolean userflag = false;
		boolean wishlistflag = false;

		final UserResultWsDto result = new UserResultWsDto();
		final Wishlist2EntryModel requiredWlEntry = null;
		//final boolean productFound = false;

		//		final MplCustomerProfileData mplCustData = new MplCustomerProfileData();
		//		mplCustData.setDisplayUid(userId);
		try
		{
			//final UserModel user = userexService.getUserForUID(mplCustData.getDisplayUid());
			final UserModel user = userService.getCurrentUser();

			if (null == user)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}

			/*
			 * final List<Wishlist2EntryModel> allWishlistEntry = wishlistFacade.getAllWishlistByUssid(USSID); if
			 * (CollectionUtils.isNotEmpty(allWishlistEntry)) { for (final Wishlist2EntryModel wishentryModel :
			 * allWishlistEntry) { if (null != wishentryModel.getWishlist() && StringUtils.isNotBlank(wishlistName) &&
			 * wishlistName.equalsIgnoreCase(wishentryModel.getWishlist().getName())) { productFound = true;
			 * requiredWlEntry = wishentryModel; break; } } } else
			 *
			 *
			 * { throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9211);
			 *
			 * }
			 */

			//Fetching all wishList for the user
			final List<Wishlist2Model> allWishlists = wishlistService.getWishlists(user);
			userflag = true;
			Wishlist2Model requiredWl = null;
			String name = null;

			//Checking wether wishlistname is empty or not
			if (!wishlistName.isEmpty())
			{

				name = wishlistName;
			}

			for (final Wishlist2Model wl : allWishlists)
			{

				if (name.equals(wl.getName()))
				{
					requiredWl = wl;
					wishlistflag = true;
					break;
				}
			}


			//Wishlist2EntryModel wishlist2EntryModel = null;
			boolean ProductFound = false;
			final List<Wishlist2EntryModel> entryModels = requiredWl.getEntries();
			if (entryModels.size() >= 1)
			{


				for (final Wishlist2EntryModel entryModel : entryModels)
				{
					final Collection<SellerInformationModel> sellerList = entryModel.getProduct().getSellerInformationRelator();
					for (final SellerInformationModel seller : sellerList)
					{
						if (seller.getSellerArticleSKU().equals(USSID))
						{
							//	wishlist2EntryModel = entryModel;
							ProductFound = true;
						}

					}
				}

				if (ProductFound)
				{
					//wishlistService.removeWishlistEntry(requiredWl, wishlist2EntryModel);
					successFlag = wishlistFacade.removeWishlistEntry(requiredWlEntry);
					//Set Success Flag to true
					//successFlag = true;
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9210);
				}
			}
			else
			{



				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9211);

			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			//Check if user was found
			if (!userflag)
			{
				result.setError(MarketplacecommerceservicesConstants.USER_NOT_FOUND);
			}
			//Check if Wishlist was found for user

			if (userflag)
			{
				if (!wishlistflag)
				{
					result.setError(MarketplacecommerceservicesConstants.WISHLIST_NOT_FOUND);
				}

			}

			//Set Success Flag to false
			successFlag = false;
		}

		if (successFlag)
		{
			//Set result status to success
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);

		}
		else
		{
			//Set result status to error
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return result;
	}

	/**
	 * @description method is called to logout from account
	 * @return MplUserResultWsDto
	 */
	@Secured(
	{ ROLE_CLIENT, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/logout", method =
	{ RequestMethod.POST }, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto logoutUser(@RequestParam final String userId, final String fields,
			final HttpServletRequest httpRequest, @RequestParam(required = false) final String invalid_access_token,
			@RequestParam(required = false) final String invalid_refresh_token)
	{
		final MplUserResultWsDto mplUserResultWsDto = new MplUserResultWsDto();
		CustomerModel currentUser = null;
		List<OAuthAccessTokenModel> accessTokenModelList = null;
		try
		{
			//currentUser = mplPaymentWebFacade.getCustomer(userId);
			final String userIdLwCase = userId.toLowerCase(); //INC144318796
			currentUser = mplPaymentWebFacade.getCustomer(userIdLwCase);

			final String authorization = httpRequest.getHeader("Authorization");
			String username = null;
			if (authorization != null && authorization.startsWith("Basic"))
			{
				// Authorization: Basic base64credentials
				final String base64Credentials = authorization.substring("Basic".length()).trim();
				final String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
				// credentials = username:password
				final String[] values = credentials.split(":", 2);
				username = values[0];
			}
			if (StringUtils.isNotEmpty(username))
			{
				accessTokenModelList = oauthTokenService.getAccessTokensForClientAndUser(username, userId);
			}

			if (CollectionUtils.isNotEmpty(accessTokenModelList))
			{
				for (final OAuthAccessTokenModel accessTokenModel : accessTokenModelList)
				{
					oauthTokenService.removeAccessToken(accessTokenModel.getTokenId());
					gigyaFacade.ratingLogoutHelper(currentUser);
					mplUserResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
			}
			else
			{
				gigyaFacade.ratingLogoutHelper(currentUser);
				mplUserResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplUserResultWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplUserResultWsDto.setErrorCode(e.getErrorCode());
			}

			mplUserResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				mplUserResultWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplUserResultWsDto.setErrorCode(e.getErrorCode());
			}
			mplUserResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return dataMapper.map(mplUserResultWsDto, MplUserResultWsDto.class, fields);
	}


	/**
	 * @description method is called to send personalized link to friends Email
	 * @param emailid
	 * @param fields
	 * @param model
	 * @return UserResultWsDto
	 * @throws DuplicateUidException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailid}/friendinvite", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto friendpersonalized(@PathVariable final String emailid, final String fields, final Model model,
			final HttpServletRequest request) throws DuplicateUidException
	{

		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		final CustomerData customerData = customerFacade.getCurrentCustomer();

		final String messageText = configurationService.getConfiguration()
				.getString(MarketplacecommerceservicesConstants.INVITE_FRIENDS_MESSAGE_KEY);

		final String affiliateId = customerData.getUid();
		final String specificUrl = MarketplacecommerceservicesConstants.LINK_LOGIN + MarketplacecommerceservicesConstants.QS
				+ MarketplacecommerceservicesConstants.AFFILIATEID + MarketplacecommerceservicesConstants.EQUALS + affiliateId;
		final String inviteFriendUrl = urlForMobileEmailContext(request, specificUrl);

		if (StringUtils.isEmpty(messageText))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
		}
		else
		{
			final String uid = customerData.getUid();
			try
			{
				if (null == uid && StringUtils.isNotEmpty(uid))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
				}
				else
				{
					userResultWsDto.setFriendsInviteMessageText(messageText);
					userResultWsDto.setSiteURL(inviteFriendUrl);
					userResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				if (null != e.getErrorMessage())
				{
					userResultWsDto.setError(e.getErrorMessage());
				}
				userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
				if (null != e.getErrorMessage())
				{
					userResultWsDto.setError(e.getErrorMessage());
				}
				userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		return dataMapper.map(userResultWsDto, UserResultWsDto.class, fields);
	}

	/**
	 * @description method is called to update the Profile of a customer
	 * @param firstName
	 * @param lastName
	 * @param emailid
	 * @param dateOfBirth
	 * @param dateOfAnniversary
	 * @param gender
	 * @param mobilenumber
	 * @param fields
	 * @param request
	 * @return UpdateCustomerDetailDto
	 * @throws RequestParameterException
	 * @throws DuplicateUidException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/updateprofile", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UpdateCustomerDetailDto updateprofile(@PathVariable final String userId,
			@RequestParam(required = false) final String emailid, @RequestParam(required = false) final String firstName,
			@RequestParam(required = false) final String lastName, @RequestParam(required = false) final String dateOfBirth,
			final String dateOfAnniversary, @RequestParam(required = false) final String nickName,
			@RequestParam(required = false) final String gender, @RequestParam(required = false) final String mobilenumber,
			final String fields, final HttpServletRequest request) throws RequestParameterException, DuplicateUidException
	{

		boolean duplicateEmail = false;
		final UpdateCustomerDetailDto updateCustomerDetailDto = new UpdateCustomerDetailDto();
		final UpdateCustomerDetailDto updateCustomerDetailError = new UpdateCustomerDetailDto();
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		if (null == customerData)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
		}
		else
		{
			final String userIdLwCase = userId.toLowerCase(); //INC144318796
			final MplCustomerProfileData customerToSave = mplCustomerProfileService.getCustomerProfileDetail(userIdLwCase);
			//final MplCustomerProfileData customerToSave = mplCustomerProfileService.getCustomerProfileDetail(userId);
			// Get the data before editing
			final String channel = MarketplacecommerceservicesConstants.UPDATE_CHANNEL_MOBILE;
			final Map<String, String> preSavedDetailMap = mplCustomerProfileFacade.setPreviousDataToMap(customerData.getDisplayUid(),



					channel);
			if (null == customerToSave)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			else
			{
				customerToSave.setUid(customerData.getUid());
				customerToSave.setDisplayUid(userIdLwCase);
				//customerToSave.setDisplayUid(userId);
				try
				{
					if (!StringUtils.isEmpty(firstName) && DefaultCommonAsciiValidator.validateAlphaWithSpaceNoSpCh(firstName)
							&& StringUtils.length(firstName) <= MarketplacecommerceservicesConstants.NAME)
					{
						customerToSave.setFirstName(firstName);
					}
					else
					{
						customerToSave.setFirstName(null);
					}
					if (!StringUtils.isEmpty(lastName) && DefaultCommonAsciiValidator.validateAlphaWithSpaceNoSpCh(lastName)
							&& StringUtils.length(lastName) <= MarketplacecommerceservicesConstants.NAME)
					{
						customerToSave.setLastName(lastName);
					}
					else
					{
						customerToSave.setLastName(null);
					}

					if (StringUtils.isNotEmpty(nickName) && StringUtils.length(nickName) <= MarketplacecommerceservicesConstants.NAME
							&& DefaultCommonAsciiValidator.validateAlphaWithSpaceNoSpCh(nickName))
					{
						customerToSave.setNickName(nickName);
					}
					else
					{
						customerToSave.setNickName(null);
					}
					if (StringUtils.isNotEmpty(mobilenumber))
					{
						if (StringUtils.length(mobilenumber) == MarketplacecommerceservicesConstants.MOBLENGTH
								&& mobilenumber.matches(MarketplacecommerceservicesConstants.MOBILE_REGEX))
						{
							customerToSave.setMobileNumber(mobilenumber);
						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9023);
						}
					}
					else
					{
						customerToSave.setMobileNumber(null);
					}
					if (null != gender)
					{
						if (gender.equalsIgnoreCase(MarketplacecommerceservicesConstants.MALE_CAPS))
						{
							customerToSave.setGender(gender);
						}
						else if (gender.equalsIgnoreCase(MarketplacecommerceservicesConstants.FEMALE_CAPS))
						{
							customerToSave.setGender(gender);
						}
						else if (gender.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMPTYSPACE)
								|| gender.equalsIgnoreCase(MarketplacecommerceservicesConstants.SPACE))
						{
							customerToSave.setGender(null);
						}
					}
					if (StringUtils.isNotEmpty(dateOfBirth))
					{
						final boolean birthdateValid = isThisDateValid(dateOfBirth,
								MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);
						final boolean isNotFutureDate = isNotFutureDate(dateOfBirth,
								MarketplacecommerceservicesConstants.DMY_DATE_FORMAT,
								MarketplacecommerceservicesConstants.DMY_DATE_FORMAT_INT);
						if (birthdateValid)
						{
							customerToSave.setDateOfBirth(dateOfBirth);
						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9026);
						}
						if (isNotFutureDate)
						{
							customerToSave.setDateOfBirth(dateOfBirth);
						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9031);
						}
					}
					else
					{
						customerToSave.setDateOfBirth(null);
					}
					if (StringUtils.isNotEmpty(dateOfAnniversary))
					{
						final boolean anniversarydateValid = isThisDateValid(dateOfAnniversary,
								MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);
						if (anniversarydateValid)
						{
							customerToSave.setDateOfAnniversary(dateOfAnniversary);
						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9026);
						}
					}
					else
					{
						customerToSave.setDateOfAnniversary(null);
					}
					if (StringUtils.isNotEmpty(emailid))
					{
						if (StringUtils.length(emailid) < 240 || DefaultCommonAsciiValidator.validateEmailAddress(emailid)
								|| DefaultCommonAsciiValidator.validDomain(emailid))
						{
							customerToSave.setEmailId(emailid);
						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9011);
						}
					}
					customerToSave.setUid(customerData.getUid());
					if (StringUtils.isNotEmpty(emailid))
					{
						customerToSave.setDisplayUid(emailid);
					}
					else
					{
						customerToSave.setDisplayUid(customerToSave.getDisplayUid());
					}
					if (StringUtils.isNotEmpty(emailid))
					{
						if (!customerData.getDisplayUid().equalsIgnoreCase(emailid.trim().toLowerCase()))
						{
							if (mplCustomerProfileFacade.checkUniquenessOfEmail(customerToSave.getEmailId()))
							{
								mplCustomerProfileFacade.updateCustomerProfile(customerToSave);
							}
							else
							{
								duplicateEmail = true;
								updateCustomerDetailError.setError(MarketplacecommerceservicesConstants.DUPLICATE_EMAIL);
								updateCustomerDetailError.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
								return dataMapper.map(updateCustomerDetailError, UpdateCustomerDetailDto.class, fields);
							}
						}
					}
					final String specificUrl = MarketplacecommerceservicesConstants.LINK_MY_ACCOUNT
							+ MarketplacecommerceservicesConstants.LINK_UPDATE_PROFILE;
					final String profileUpdateUrl = urlForEmailContext(request, specificUrl);
					mplCustomerProfileFacade.updateCustomerProfile(customerToSave);
					mplCustomerProfileFacade.checkChangesForSendingEmail(preSavedDetailMap, customerToSave.getDisplayUid(),
							profileUpdateUrl);
					if (StringUtils.isNotEmpty(customerToSave.getDateOfAnniversary()))
					{
						updateCustomerDetailDto.setDateOfAnniversary(customerToSave.getDateOfAnniversary());
					}
					if (StringUtils.isNotEmpty(customerToSave.getFirstName()))
					{
						updateCustomerDetailDto.setFirstName(customerToSave.getFirstName());
					}
					if (StringUtils.isNotEmpty(customerToSave.getLastName()))
					{
						updateCustomerDetailDto.setLastName(customerToSave.getLastName());
					}
					if (StringUtils.isNotEmpty(customerToSave.getNickName()))
					{
						updateCustomerDetailDto.setNickName(customerToSave.getNickName());
					}
					if (StringUtils.isNotEmpty(customerToSave.getEmailId()))
					{
						updateCustomerDetailDto.setEmailId(customerToSave.getEmailId());
					}

					if (StringUtils.isNotEmpty(customerToSave.getMobileNumber()))
					{
						updateCustomerDetailDto.setMobileNumber(customerToSave.getMobileNumber());
					}
					if (StringUtils.isNotEmpty(customerToSave.getGender()))
					{
						updateCustomerDetailDto.setGender(customerToSave.getGender());
					}
					if (StringUtils.isNotEmpty(customerToSave.getDateOfBirth()))
					{
						updateCustomerDetailDto.setDateOfBirth(customerToSave.getDateOfBirth());
					}
					// NOTIFY GIGYA OF THE USER PROFILE CHANGES
					final String gigyaServiceSwitch = configurationService.getConfiguration()
							.getString(MarketplacewebservicesConstants.USE_GIGYA);
					if (gigyaServiceSwitch != null && !gigyaServiceSwitch.equalsIgnoreCase(MarketplacewebservicesConstants.NO))
					{
						final String gigyaMethod = configurationService.getConfiguration()
								.getString(MarketplacewebservicesConstants.GIGYA_METHOD_UPDATE_USERINFO);
						String fnameGigya = null;
						String lnameGigya = null;

						if (StringUtils.isNotEmpty(updateCustomerDetailDto.getFirstName()))
						{
							fnameGigya = updateCustomerDetailDto.getFirstName().trim();
						}
						else
						{

							fnameGigya = MarketplacewebservicesConstants.EMPTY;
						}
						if (StringUtils.isNotEmpty(updateCustomerDetailDto.getLastName()))
						{
							lnameGigya = updateCustomerDetailDto.getLastName().trim();
						}
						else
						{
							lnameGigya = MarketplacewebservicesConstants.EMPTY;
						}

						gigyaFacade.notifyGigya(updateCustomerDetailDto.getEmailId(), null, fnameGigya, lnameGigya,
								updateCustomerDetailDto.getEmailId().trim(), gigyaMethod);
					}
				}
				catch (final DuplicateUidException e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(

							new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B0001));
				}
				catch (final EtailNonBusinessExceptions e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(e);
					if (null != e.getErrorMessage())
					{
						updateCustomerDetailError.setError(e.getErrorMessage());
					}
					updateCustomerDetailError.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					return dataMapper.map(updateCustomerDetailError, UpdateCustomerDetailDto.class, fields);
				}
				catch (final EtailBusinessExceptions e)
				{
					ExceptionUtil.etailBusinessExceptionHandler(e, null);
					if (null != e.getErrorMessage())
					{
						updateCustomerDetailError.setError(e.getErrorMessage());
					}
					updateCustomerDetailError.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					return dataMapper.map(updateCustomerDetailError, UpdateCustomerDetailDto.class, fields);
				}
				if (!duplicateEmail)
				{
					updateCustomerDetailDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					return dataMapper.map(updateCustomerDetailDto, UpdateCustomerDetailDto.class, fields);
				}
				else
				{
					updateCustomerDetailError.setError(MarketplacecommerceservicesConstants.DUPLICATE_EMAIL);
					updateCustomerDetailError.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					return dataMapper.map(updateCustomerDetailError, UpdateCustomerDetailDto.class, fields);
				}
			}
		}
	}

	/**
	 * @description method will return the validated formate of date
	 * @param dateToValidate
	 * @param dateFromat
	 * @return boolean
	 */
	private boolean isThisDateValid(final String dateToValidate, final String dateFromat)
	{
		if (dateToValidate == null)
		{
			return false;
		}
		final SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try
		{
			final Date date = sdf.parse(dateToValidate);
			LOG.info(date);
		}
		catch (final ParseException e)
		{
			return false;
		}
		return true;
	}

	/**
	 * @description method will return the validated formate of date
	 * @param dateToValidate
	 * @param dateFromat
	 * @return boolean
	 */
	private boolean isNotFutureDate(final String dateToValidate, final String dateFromat, final String dateFromatInt)
	{
		if (StringUtils.isEmpty(dateToValidate))
		{
			return false;
		}
		final SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		final SimpleDateFormat sdfInt = new SimpleDateFormat(dateFromatInt);
		sdf.setLenient(false);
		int dobInt = 0;
		int currentDateInt = 0;
		try
		{
			final Date dob = sdf.parse(dateToValidate);
			dobInt = Integer.parseInt(sdfInt.format(dob));
			final Date currDate = sdf.parse(sdf.format(new Date()));
			currentDateInt = Integer.parseInt(sdfInt.format(currDate));
			if (dobInt > currentDateInt)
			{
				return false;
			}
		}
		catch (final ParseException e)
		{
			return false;
		}
		return true;
	}

	/**
	 * @description method is called to validate the OTP Number for COD
	 * @param emailid
	 * @param enteredOTPNumber
	 * @param fields
	 * @return ValidateOtpWsDto
	 * @throws DuplicateUidException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailid}/validateOtpforcod", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ValidateOtpWsDto validateOtpforCOD(@PathVariable final String emailid, @RequestParam final String enteredOTPNumber,
			final String fields) throws DuplicateUidException
	{
		//final CustomerData customer = customerFacade.getCurrentCustomer();
		final ValidateOtpWsDto result = new ValidateOtpWsDto();
		//		if (null == customer)
		//		{
		//			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
		//		}
		//		else
		//		{
		//final String mplCustomerID = customer.getUid();
		try
		{
			if (StringUtils.isNotEmpty(emailid))
			{

				LOG.debug("************** Mobile web service Validate OTP for COD ******************" + emailid);

				final String validationMsg = getMplPaymentFacade().validateOTPforCODWeb(emailid, enteredOTPNumber);
				if (null != validationMsg)
				{

					LOG.debug(
							"************** Mobile web service Validate OTP for COD  RESPONSE SUCCESSSSS ******************" + emailid);



					if (validationMsg.equalsIgnoreCase(MarketplacecommerceservicesConstants.OTPVALIDITY))
					{
						result.setError(MarketplacecommerceservicesConstants.OTP_SENT);
						result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}
					else if (validationMsg.equalsIgnoreCase(MarketplacecommerceservicesConstants.OTPEXPIRY))
					{
						result.setError(MarketplacecommerceservicesConstants.OTP_EXPIRY_MESSAGE);
						result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9039);
					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9027);

				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		//}
		return result;
	}

	/**
	 * @description method is called to get the Profile Details
	 * @param emailid
	 * @param fields
	 * @return GetCustomerDetailDto
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailid}/getMyProfile", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public GetCustomerDetailDto getMyProfile(@PathVariable final String emailid, final String fields)
	{
		final GetCustomerDetailDto customer = new GetCustomerDetailDto();
		MplCustomerProfileData customerData = new MplCustomerProfileData();

		if (null == emailid && StringUtils.isEmpty(emailid))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
		}
		else
		{
			try
			{
				final String emailIdLwCase = emailid.toLowerCase(); //INC144318796
				customerData = mplCustomerProfileService.getCustomerProfileDetail(emailIdLwCase);

				//customerData = mplCustomerProfileService.getCustomerProfileDetail(emailid);
				if (null != customerData)
				{
					if (null != emailIdLwCase && StringUtils.isNotEmpty(emailIdLwCase))
					{
						customer.setEmailID(emailIdLwCase);
					}
					if (StringUtils.isNotEmpty(customerData.getFirstName())
							&& !customerData.getFirstName().equals(MarketplacecommerceservicesConstants.SPACE))
					{
						customer.setFirstName(customerData.getFirstName());
					}
					if (StringUtils.isNotEmpty(customerData.getLastName())
							&& !customerData.getLastName().equals(MarketplacecommerceservicesConstants.SPACE))
					{

						customer.setLastName(customerData.getLastName());
					}
					if (StringUtils.isNotEmpty(customerData.getDateOfBirth()))
					{
						customer.setDateOfBirth(customerData.getDateOfBirth());
					}
					if (StringUtils.isNotEmpty(customerData.getDateOfAnniversary()))
					{
						customer.setDateOfAnniversary(customerData.getDateOfAnniversary());
					}
					if (StringUtils.isNotEmpty(customerData.getGender()))
					{
						customer.setGender(customerData.getGender());
					}
					if (StringUtils.isNotEmpty(customerData.getMobileNumber()))
					{
						customer.setMobileNumber(customerData.getMobileNumber());
					}
					if (StringUtils.isNotEmpty(customerData.getNickName()))
					{
						customer.setNickName(customerData.getNickName());
					}

					customer.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
				}
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				if (null != e.getErrorMessage())
				{
					customer.setError(e.getErrorMessage());
				}
				if (null != e.getErrorCode())
				{
					customer.setErrorCode(e.getErrorCode());
				}
				customer.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
				if (null != e.getErrorMessage())
				{
					customer.setError(e.getErrorMessage());
				}
				if (null != e.getErrorCode())
				{
					customer.setErrorCode(e.getErrorCode());
				}
				customer.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		return customer;
	}

	/**
	 * reset password
	 *
	 * @param old
	 * @param new
	 *           password
	 *
	 * @returnUserResultWsDto
	 */


	@Secured(
	{ ROLE_CLIENT, CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/resetpassword", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto resetPassword(@PathVariable final String userId, @RequestParam final String old,
			@RequestParam final String newPassword, final HttpServletRequest request)
			throws RequestParameterException, de.hybris.platform.commerceservices.customer.PasswordMismatchException
	{
		final UserResultWsDto result = new UserResultWsDto();
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		MplUserResultWsDto validated = new MplUserResultWsDto();
		try
		{
			final String userIdLwCase = userId.toLowerCase(); //INC144318796
			validated = mplUserHelper.validateRegistrationData(userIdLwCase, newPassword);
			if (null != validated.getStatus()
					&& validated.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.ERROR_FLAG))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9010);
			}
			else
			{
				if (containsRole(auth, TRUSTED_CLIENT) || containsRole(auth, CUSTOMERMANAGER))
				{
					extUserService.setPassword(userIdLwCase, newPassword);
				}
				else
				{
					final UserModel user = userService.getCurrentUser();
					try
					{
						getCustomerAccountService().changePassword(user, old, newPassword);
					}
					catch (final Exception e)
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0009);
					}
				}
				CustomerModel currentUser = null;
				try
				{
					if (null != userService.getCurrentUser())
					{
						currentUser = (CustomerModel) userService.getCurrentUser();
					}
					if (null != currentUser)
					{
						//						mplCustomerProfileFacade.sendEmailForChangePassword(currentUser);
						//
						final String specificUrl = MarketplacecommerceservicesConstants.LINK_MY_ACCOUNT
								+ MarketplacecommerceservicesConstants.LINK_UPDATE_PROFILE;
						final String profileUpdateUrl = urlForMobileEmailContext(request, specificUrl);
						final List<String> updatedDetailList = new ArrayList<String>();
						updatedDetailList.add(MarketplacecommerceservicesConstants.PASSWORD_suffix);
						mplCustomerProfileFacade.sendEmailForUpdateCustomerProfile(updatedDetailList, profileUpdateUrl);
					}
				}
				catch (final Exception e)
				{
					LOG.error("*************** Exception in sending mail after change password MOBILE ******************* " + e);
				}
				result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}


	@RequestMapping(value = "/{emailid}/netbankingDetails", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public NetBankingListWsDTO netbankingDetails(
			@SuppressWarnings("unused") @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		final NetBankingListWsDTO netBankingListWsDTO = new NetBankingListWsDTO();
		final List<NetBankingWsDTO> netbankingwstolist = new ArrayList<NetBankingWsDTO>();
		final List<NetBankingWsDTO> netbankingwstolist1 = new ArrayList<NetBankingWsDTO>();
		final List<NetBankingWsDTO> netbankingfinallist = new ArrayList<NetBankingWsDTO>();
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		try
		{
			if (null == customerData)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			else
			{
				final String uid = customerData.getUid();
				if (null == uid && StringUtils.isEmpty(uid))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
				}
				else
				{
					final List<BankforNetbankingModel> bankList = getMplPaymentService().getBanksByPriority();
					final List<BankforNetbankingModel> bankList2 = getMplPaymentService().getOtherBanks();
					if (bankList.isEmpty() && bankList2.isEmpty())
					{

						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9028);
					}
					else
					{
						for (final BankforNetbankingModel netbanking : bankList)
						{
							final NetBankingWsDTO netBankingWsDTO = new NetBankingWsDTO();
							if (StringUtils.isNotEmpty(netbanking.getNbCode()))
							{
								netBankingWsDTO.setBankCode(netbanking.getNbCode());
							}
							if (StringUtils.isNotEmpty(netbanking.getName().getBankName()))
							{
								netBankingWsDTO.setBankName(netbanking.getName().getBankName());
							}
							if (StringUtils.isNotEmpty((netbanking.getPriority().toString())))
							{
								netBankingWsDTO.setPriority(netbanking.getPriority().toString());
							}

							if (StringUtils.isNotEmpty((netbanking.getIsAvailable().toString())))
							{
								netBankingWsDTO.setIsAvailable(netbanking.getIsAvailable().toString());
							}
							if (null != netbanking.getName().getBankLogo() && null != netbanking.getName().getBankLogo().getURL())
							{
								netBankingWsDTO.setLogoUrl(netbanking.getName().getBankLogo().getURL().toString());
							}
							netbankingwstolist.add(netBankingWsDTO);
						}
						for (final BankforNetbankingModel netbanking1 : bankList2)
						{
							final NetBankingWsDTO netBankingWsDTO1 = new NetBankingWsDTO();
							if (StringUtils.isNotEmpty(netbanking1.getNbCode()))
							{
								netBankingWsDTO1.setBankCode(netbanking1.getNbCode());
							}
							if (StringUtils.isNotEmpty(netbanking1.getName().getBankName()))
							{
								netBankingWsDTO1.setBankName(netbanking1.getName().getBankName());
							}
							if (StringUtils.isNotEmpty((netbanking1.getPriority().toString())))
							{
								netBankingWsDTO1.setPriority(netbanking1.getPriority().toString());
							}

							if (StringUtils.isNotEmpty((netbanking1.getIsAvailable().toString())))
							{
								netBankingWsDTO1.setIsAvailable(netbanking1.getIsAvailable().toString());
							}
							if (null != netbanking1.getName() && null != netbanking1.getName().getBankLogo()
									&& null != netbanking1.getName().getBankLogo().getURL())
							{
								netBankingWsDTO1.setLogoUrl(netbanking1.getName().getBankLogo().getURL().toString());
							}
							netbankingwstolist1.add(netBankingWsDTO1);
						}
						//TPR-4855
						netbankingfinallist.addAll(sortedList(netbankingwstolist));
						netbankingfinallist.addAll(sortedList(netbankingwstolist1));
						//TISEE-929
						//TPR-4855
						/*
						 * final Comparator<NetBankingWsDTO> byName = (final NetBankingWsDTO o1, final NetBankingWsDTO o2) ->
						 * o1 .getBankName().compareTo(o2.getBankName()); Collections.sort(netbankingfinallist, byName);
						 */
						netBankingListWsDTO.setBankList(netbankingfinallist);
						netBankingListWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				netBankingListWsDTO.setError(e.getErrorMessage());
			}
			netBankingListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				netBankingListWsDTO.setError(e.getErrorMessage());
			}
			netBankingListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return netBankingListWsDTO;
	}

	/**
	 * @Description : For Juspay Order Creation. It returns juspayMerchantKey, juspayMerchantId,
	 *              juspayReturnUrl,juspayOrderId
	 * @return OrderCreateInJusPayWsDto
	 * @throws InvalidCartException
	 */
	//Duplicate method for juspayorder id creation

	//	@Secured(
	//	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	//	@RequestMapping(value = "/{emailId}/createOrderInJuspay", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	//	@ResponseBody
	//	public OrderCreateInJusPayWsDto orderInJuspay() throws InvalidCartException, AdapterException
	//	{
	//		final OrderCreateInJusPayWsDto orderCreateInJusPayWsDto = new OrderCreateInJusPayWsDto();
	//		boolean successFlag = false;
	//		String cartIdentifier = " ";
	//		final UserModel user = userService.getCurrentUser();
	//		LOG.debug(CUSTOMER_MESSAGE + user);
	//		if (!(null != user))
	//		{
	//			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
	//		}
	//		try
	//		{
	//			cartIdentifier = getSessionCart().getCode();
	//			if (!(null != cartIdentifier))
	//			{
	//				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9040);
	//			}
	//
	//			//			final String juspayOrderId = !getConfigurationService().getConfiguration()
	//			//					.getString(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID).isEmpty() ? getConfigurationService()
	//			//					.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID)
	//			//					: "No juspayOrderId is defined in local properties";
	//
	//			final String juspayOrderId = mplPaymentService.createPaymentId();
	//			final String juspayMerchantKey = !getConfigurationService().getConfiguration()
	//					.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY).isEmpty() ? getConfigurationService()
	//					.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)
	//					: "No juspayMerchantKey is defined in local properties";
	//
	//			final String juspayMerchantId = !getConfigurationService().getConfiguration()
	//					.getString(MarketplacecommerceservicesConstants.MARCHANTID).isEmpty() ? getConfigurationService()
	//					.getConfiguration().getString(MarketplacecommerceservicesConstants.MARCHANTID)
	//					: "No juspayMerchantKey is defined in local properties";
	//			final String juspayReturnUrl = !getConfigurationService().getConfiguration()
	//					.getString(MarketplacecommerceservicesConstants.RETURNURL).isEmpty() ? getConfigurationService()
	//					.getConfiguration().getString(MarketplacecommerceservicesConstants.RETURNURL)
	//					: "No juspayReturnUrl is defined in local properties";
	//
	//
	//			orderCreateInJusPayWsDto.setJuspayMerchantKey(juspayMerchantKey);
	//			orderCreateInJusPayWsDto.setJuspayMerchantId(juspayMerchantId);
	//			orderCreateInJusPayWsDto.setJuspayReturnUrl(juspayReturnUrl);
	//			orderCreateInJusPayWsDto.setJuspayOrderId(juspayOrderId);
	//			successFlag = true;
	//		}
	//
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//			if (null != e.getErrorMessage())
	//			{
	//				orderCreateInJusPayWsDto.setError(e.getErrorMessage());
	//			}
	//			if (null != e.getErrorCode())
	//			{
	//				orderCreateInJusPayWsDto.setErrorCode(e.getErrorCode());
	//			}
	//			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//			if (null != e.getErrorMessage())
	//			{
	//				orderCreateInJusPayWsDto.setError(e.getErrorMessage());
	//			}
	//			if (null != e.getErrorCode())
	//			{
	//				orderCreateInJusPayWsDto.setErrorCode(e.getErrorCode());
	//			}
	//			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	//		}
	//
	//		if (successFlag)
	//		{
	//			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
	//		}
	//		else
	//		{
	//			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	//		}
	//
	//		return orderCreateInJusPayWsDto;
	//	}

	/**
	 * @description method is called to get the Details of Banks which are available for EMI
	 * @param cartValue
	 * @param fields
	 * @return EMIBankListWsDTO
	 */
	@RequestMapping(value = "/{emailid}/emibankingDetails", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public EMIBankListWsDTO bankdetailsforEMI(@RequestParam final Double cartValue, final String fields)
	{
		EMIBankListWsDTO emiBankListWsDTO = new EMIBankListWsDTO();
		//final List<EMIBankWsDTO> emiBankWsListDTO = new ArrayList<EMIBankWsDTO>();
		//final List<EMIBankModel> emiBankList = new ArrayList<>();
		//final CustomerData customerData = null;
		try
		{
			emiBankListWsDTO = mplNetBankingFacade.getEMIBanks(cartValue);
			//			customerData = customerFacade.getCurrentCustomer();
			//			if (null == customerData)
			//			{
			//				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			//			}
			//			else
			//			{
			//				final String uid = customerData.getUid();
			//				if (null == uid && StringUtils.isNotEmpty(uid))
			//				{
			//					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			//				}
			//				else
			//				{
			//					try
			//					{
			//						if (emiBankList.isEmpty())
			//						{
			//							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9029);
			//						}
			//						else
			//						{
			//
			//							for (final EMIBankModel emibanking : emiBankList)
			//							{
			//								final EMIBankWsDTO eMIBankWsDTO = new EMIBankWsDTO();
			//
			//								if (StringUtils.isNotEmpty(emibanking.getName().getBankName()))
			//								{
			//									eMIBankWsDTO.setEmiBank(emibanking.getName().getBankName());
			//								}
			//								if (StringUtils.isNotEmpty(emibanking.getCode()))
			//								{
			//									eMIBankWsDTO.setCode(emibanking.getCode());
			//								}
			//								if (StringUtils.isNotEmpty(emibanking.getEmiLowerLimit().toString()))
			//								{
			//									eMIBankWsDTO.setEmiLowerLimit(emibanking.getEmiLowerLimit().toString());
			//								}
			//								if (StringUtils.isNotEmpty(emibanking.getEmiUpperLimit().toString()))
			//								{
			//									eMIBankWsDTO.setEmiUpperLimit(emibanking.getEmiUpperLimit().toString());
			//								}
			//								emiBankWsListDTO.add(eMIBankWsDTO);
			//							}
			//							//TISEE-929
			//							final Comparator<EMIBankWsDTO> byName = (final EMIBankWsDTO o1, final EMIBankWsDTO o2) -> o1.getEmiBank()
			//									.compareTo(o2.getEmiBank());
			//
			//							Collections.sort(emiBankWsListDTO, byName);
			//							emiBankListWsDTO.setBankList(emiBankWsListDTO);
			//							emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			//
			//						}
			//					}
			//					catch (final EtailNonBusinessExceptions e)
			//					{
			//						ExceptionUtil.etailNonBusinessExceptionHandler(e);
			//						if (null != e.getErrorMessage())
			//						{
			//							emiBankListWsDTO.setError(e.getErrorMessage());
			//						}
			//						emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			//					}
			//					catch (final EtailBusinessExceptions e)
			//					{
			//						ExceptionUtil.etailBusinessExceptionHandler(e, null);
			//						if (null != e.getErrorMessage())
			//						{
			//							emiBankListWsDTO.setError(e.getErrorMessage());
			//						}
			//						emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			//					}
			//				}
			//			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				emiBankListWsDTO.setError(e.getErrorMessage());
			}
			emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				emiBankListWsDTO.setError(e.getErrorMessage());
			}
			emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return emiBankListWsDTO;
	}

	/**
	 * @description method is called to get the Details of EMI Terms for the Banks
	 * @param cartValue
	 * @param Bank
	 * @param fields
	 * @return EMIBankListWsDTO
	 */
	@RequestMapping(value = "/{emailid}/emiTermsDetails", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public EMIBankListWsDTO emiService(@RequestParam final Double cartValue, final String Bank, final String fields)
	{
		final EMIBankListWsDTO emiBankListWsDTO = new EMIBankListWsDTO();
		final List<EMITermRateDataForMobile> emiBankmobileWsListDTO = new ArrayList<EMITermRateDataForMobile>();
		final List<EMITermRateDataForMobile> emiBankList = mplNetBankingFacade.getBankTerms(Bank, cartValue);
		final CustomerData customerData = customerFacade.getCurrentCustomer();
		if (null == customerData)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
		}
		else
		{
			final String uid = customerData.getUid();
			if (null == uid && StringUtils.isNotEmpty(uid))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			else
			{
				try
				{
					if (emiBankList.isEmpty())
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9030);
					}
					else
					{

						for (final EMITermRateDataForMobile emilist : emiBankList)
						{
							final EMITermRateDataForMobile emilistforMobile = new EMITermRateDataForMobile();

							if (StringUtils.isNotEmpty(emilist.getInterestPayable()))
							{
								emilistforMobile.setInterestPayable(emilist.getInterestPayable());
							}
							if (StringUtils.isNotEmpty(emilist.getInterestRate()))
							{
								emilistforMobile.setInterestRate(emilist.getInterestRate());
							}
							if (StringUtils.isNotEmpty(emilist.getMonthlyInstallment()))
							{
								emilistforMobile.setMonthlyInstallment(emilist.getMonthlyInstallment());
							}
							if (StringUtils.isNotEmpty(emilist.getTerm()))
							{
								emilistforMobile.setTerm(emilist.getTerm());
							}

							emiBankmobileWsListDTO.add(emilistforMobile);
						}
						emiBankListWsDTO.setEmitermsrate(emiBankmobileWsListDTO);
						emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}
				}
				catch (final EtailNonBusinessExceptions e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(e);
					if (null != e.getErrorMessage())
					{
						emiBankListWsDTO.setError(e.getErrorMessage());
					}
					emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
				catch (final EtailBusinessExceptions e)
				{
					ExceptionUtil.etailBusinessExceptionHandler(e, null);
					if (null != e.getErrorMessage())
					{
						emiBankListWsDTO.setError(e.getErrorMessage());
					}
					emiBankListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
			}
		}
		return dataMapper.map(emiBankListWsDTO, EMIBankListWsDTO.class, fields);
	}

	/**
	 * @description to delete the wishlist
	 * @return userResultWsDto
	 * @throws CMSItemNotFoundException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailid}/deleteWishlist", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto deleteWishlist(@RequestParam final String wishlistName) throws CMSItemNotFoundException
	{
		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		try
		{
			//			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			//CAR Project performance issue fixed
			final UserModel user = userService.getCurrentUser();
			if (StringUtils.isNotEmpty(wishlistName))
			{
				final Wishlist2Model wishList = wishlistFacade.findMobileWishlistswithName(user, wishlistName);

				//if (null != allWishlists && !allWishlists.isEmpty())
				if (null != wishList)
				{
					//					for (final Wishlist2Model wishlist2Model : allWishlists)
					//					{
					//						if (wishlist2Model.getName().equals(wishlistName))
					//						{
					//					modelService.remove(wishlist2Model);
					modelService.remove(wishList);
					userResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
					//						}
					//					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				userResultWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResultWsDto.setErrorCode(e.getErrorCode());
			}
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				userResultWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResultWsDto.setErrorCode(e.getErrorCode());
			}
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return userResultWsDto;
	}

	/**
	 * @description to capture Feedback Yes
	 * @return userResultWsDto
	 * @throws CMSItemNotFoundException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/feedbackyes", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto captureFeedbackYes(@PathVariable final String emailId, @RequestParam final String comment,
			@RequestParam final String name) throws CMSItemNotFoundException
	{
		String returnValue = null;
		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		try
		{
			returnValue = updateFeedbackFacade.updateFeedbackYes(emailId, comment, name);
			userResultWsDto.setStatus(returnValue);
		}
		catch (final EtailBusinessExceptions e)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			userResultWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
			return userResultWsDto;
		}

		catch (final EtailNonBusinessExceptions e)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			userResultWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + ":" + e.getMessage());
			return userResultWsDto;
		}
		return userResultWsDto;
	}


	/**
	 * Rename WISHLIST
	 *
	 * @param oldWishlistName
	 * @param newWishlistName
	 * @return UserResultWsDto
	 * @throws CMSItemNotFoundException
	 */

	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/renameWishList", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto renameWishList(@RequestParam final String oldWishlistName, @RequestParam final String newWishlistName)
			throws CMSItemNotFoundException
	{
		final UserResultWsDto renameWishListStatus = new UserResultWsDto();
		try
		{
			final EditWishlistNameData editWishlistNameData = new EditWishlistNameData();
			if (!(StringUtils.isEmpty(newWishlistName)) && !(StringUtils.isEmpty(oldWishlistName)))
			{
				final Wishlist2Model particularWishlistCheck = wishlistFacade.getWishlistForName(newWishlistName);
				if (null == particularWishlistCheck)
				{
					editWishlistNameData.setNewWishlistName(newWishlistName);
					editWishlistNameData.setParticularWishlistName(oldWishlistName);
					wishlistFacade.editWishlistName(editWishlistNameData);
					renameWishListStatus.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
				}
				else if (oldWishlistName.equals(newWishlistName))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9209);
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9212);
				}
			}
			return renameWishListStatus;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				renameWishListStatus.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				renameWishListStatus.setErrorCode(e.getErrorCode());
			}
			renameWishListStatus.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				renameWishListStatus.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				renameWishListStatus.setErrorCode(e.getErrorCode());
			}
			renameWishListStatus.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
		}
		return renameWishListStatus;
	}


	/**
	 *
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/getMarketplacePreference", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplPreferenceListWsDto getMarketplacePreference() throws CMSItemNotFoundException
	{
		final MplPreferenceListWsDto mplPreferenceListWsDto = new MplPreferenceListWsDto();
		try
		{
			final List<String> preferredCategoryList = new ArrayList<String>();
			final List<String> preferredBrandList = new ArrayList<String>();
			final List<String> frequencyList = getFrequency();
			final List<String> feedbackAreaList = getFeedbackArea();
			final List<String> interestList = getInterestPreference();
			final MplPreferenceDataForMobile mplPreferenceForm = new MplPreferenceDataForMobile();
			final Collection<CategoryModel> categoryList = baseSiteService.getCurrentBaseSite().getMplPrefferedCategories();

			for (final CategoryModel categoryLineItem : categoryList)
			{
				if (!categoryLineItem.getCode().startsWith("MBH"))
				{
					preferredCategoryList.add(categoryLineItem.getName());
				}
				else
				{
					preferredBrandList.add(categoryLineItem.getName());
				}
			}
			mplPreferenceForm.setSelectedCategory(preferredCategoryList);
			mplPreferenceForm.setSelectedBrand(preferredBrandList);
			mplPreferenceForm.setMyInterest("I am interested in receiving e-mails");
			mplPreferenceForm.setSelectedFrequency(Frequency.WEEKLY.toString());
			mplPreferenceForm.setSelectedfeedbackArea(feedbackAreaList);

			mplPreferenceListWsDto.setSelectedCategoryList(preferredCategoryList);
			mplPreferenceListWsDto.setSelectedBrandList(preferredBrandList);
			mplPreferenceListWsDto.setSelectedFrequency(frequencyList);
			mplPreferenceListWsDto.setSelectedfeedbackAreaList(feedbackAreaList);
			mplPreferenceListWsDto.setMyInterest(interestList);
			mplPreferenceListWsDto.setPreference(mplPreferenceForm);
			mplPreferenceListWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			mplPreferenceListWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			mplPreferenceListWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return mplPreferenceListWsDto;
		}

		catch (final EtailNonBusinessExceptions e)
		{
			mplPreferenceListWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			mplPreferenceListWsDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return mplPreferenceListWsDto;
		}
		return mplPreferenceListWsDto;
	}

	/**
	 * @return
	 * @throws CMSItemNotFoundException
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/getOrderTrackingNotifications", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplOrderTrackingNotificationsListWsDto getOrderTrackingNotifications(@RequestParam final String emailId)
			throws CMSItemNotFoundException
	{
		final MplOrderTrackingNotificationsListWsDto mplOrderTrackingNotificationsListWsDto = new MplOrderTrackingNotificationsListWsDto();
		OrderStatusCodeMasterModel trackModel = null;
		try
		{


			List<NotificationData> notificationMessagelist = new ArrayList<NotificationData>();
			String orderDetailStatus = null;

			if (null != emailId)
			{
				notificationMessagelist = notificationFacade.getNotificationDetailForEmailID(emailId, false);
				final Map<String, OrderStatusCodeMasterModel> orderStatusCodeMap = orderModelService.getOrderStatusCodeMasterList();

				MplOrderNotificationWsDto orderNotificationDto;

				if (null != notificationMessagelist && !notificationMessagelist.isEmpty())
				{
					final List<MplOrderNotificationWsDto> mplOrderNotificationWsDtos = new ArrayList<MplOrderNotificationWsDto>();
					for (final NotificationData notifyData : notificationMessagelist)
					{
						orderNotificationDto = new MplOrderNotificationWsDto();
						if (notifyData.getOrderNumber() != null)
						{
							orderNotificationDto.setOrderID(notifyData.getOrderNumber());
						}
						if (notifyData.getCouponCode() != null) //coupon
						{
							orderNotificationDto.setCouponCode(notifyData.getCouponCode());
						}
						trackModel = orderStatusCodeMap.get(notifyData.getNotificationOrderStatus());
						if (trackModel != null)
						{
							orderNotificationDto.setOrderStatus(trackModel.getResponseStatus());
						}
						else
						{
							orderNotificationDto.setOrderStatus(notifyData.getNotificationOrderStatus());
						}
						if (notifyData.getNotificationCustomerStatus() != null
								&& notifyData.getNotificationCustomerStatus().contains("@"))
						{
							/*
							 * final String orderDetailStatus = notifyData.getNotificationCustomerStatus().replace("@",
							 * notifyData.getOrderNumber()); orderNotificationDto.setOrderDetailStatus(orderDetailStatus);
							 */
							/* Coupon code */
							if (notifyData.getCouponCode() != null) //coupon
							{
								orderDetailStatus = notifyData.getNotificationCustomerStatus().replace("@", notifyData.getCouponCode());
								orderNotificationDto.setDisplayCouponMessage(orderDetailStatus);
							}
							if (notifyData.getOrderNumber() != null)
							{
								orderDetailStatus = notifyData.getNotificationCustomerStatus().replace("@", notifyData.getOrderNumber());
								orderNotificationDto.setOrderDetailStatus(orderDetailStatus);
							}

						}
						else
						{
							if (notifyData.getOrderNumber() != null)
							{
								orderNotificationDto.setOrderDetailStatus(notifyData.getNotificationOrderStatus());
							}
							if (notifyData.getCouponCode() != null) //coupon
							{
								orderNotificationDto.setActualCouponMessage(notifyData.getNotificationOrderStatus());
							}
						}
						orderNotificationDto.setReadStatus(notifyData.getNotificationRead());
						orderNotificationDto.setConsignmentId(notifyData.getTransactionID());
						if (notifyData.getOrderNumber() != null)
						{
							orderNotificationDto.setActualOrderDetailStatus(notifyData.getNotificationCustomerStatus());
						}
						if (notifyData.getCouponCode() != null) //coupon
						{
							orderNotificationDto.setActualCouponMessage(notifyData.getNotificationCustomerStatus());
						}

						final Date date = new Date();
						final Date orderCreationDate = notifyData.getNotificationCreationDate();
						final long diff = date.getTime() - orderCreationDate.getTime();
						/* System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)); */

						final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
						orderNotificationDto.setNotificationCreationTime(timeFormat.format(orderCreationDate));

						if (notifyData.getOrderNumber() != null)
						{

							if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) != 1)
							{
								orderNotificationDto
										.setOrderNotificationPassDate(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days");




							}
							else
							{
								orderNotificationDto
										.setOrderNotificationPassDate(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Day");




							}
						}
						if (notifyData.getCouponCode() != null) // coupon
						{

							if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) != 1)
							{
								orderNotificationDto
										.setCouponNotificationPassDate(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days");




							}
							else
							{
								orderNotificationDto
										.setCouponNotificationPassDate(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Day");




							}
						}
						mplOrderNotificationWsDtos.add(orderNotificationDto);
					}
					final Integer count = notificationFacade.getUnReadNotificationCount(notificationMessagelist);
					mplOrderTrackingNotificationsListWsDto.setUnReadCount(count);
					mplOrderTrackingNotificationsListWsDto.setOrderNotifications(mplOrderNotificationWsDtos);
				}
				else
				{
					mplOrderTrackingNotificationsListWsDto
							.setStatus(MarketplacecommerceservicesConstants.SET_EMPTY_ORDER_NOTIFICATIONS);
				}
			}
			else
			{
				mplOrderTrackingNotificationsListWsDto.setStatus(MarketplacecommerceservicesConstants.EMAIL_ID_IS_EMPTY);
			}

		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplOrderTrackingNotificationsListWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplOrderTrackingNotificationsListWsDto.setErrorCode(e.getErrorCode());
			}
			mplOrderTrackingNotificationsListWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				mplOrderTrackingNotificationsListWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplOrderTrackingNotificationsListWsDto.setErrorCode(e.getErrorCode());
			}
			mplOrderTrackingNotificationsListWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return mplOrderTrackingNotificationsListWsDto;
	}


	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/markNotificationAsRead", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplOrderTrackingNotificationsListWsDto markNotificationAsRead(@RequestParam final String emailId,
			@RequestParam(required = true) final String orderID, @RequestParam(required = false) final String consignmentId,
			@RequestParam(required = true) final String orderDetailStatus) throws CMSItemNotFoundException
	{
		final MplOrderTrackingNotificationsListWsDto mplOrderTrackingNotificationsListWsDto = new MplOrderTrackingNotificationsListWsDto();
		try
		{
			notificationFacade.markNotificationReadForOriginalUid(emailId, orderID, consignmentId, orderDetailStatus);
			final List<NotificationData> notificationMessagelist = notificationFacade.getNotificationDetailForEmailID(emailId,

					false);
			if (null != emailId)
			{
				if (null != notificationMessagelist && !notificationMessagelist.isEmpty())
				{
					final Integer count = notificationFacade.getUnReadNotificationCount(notificationMessagelist);
					mplOrderTrackingNotificationsListWsDto.setUnReadCount(count);
					mplOrderTrackingNotificationsListWsDto.setStatus("success");
					mplOrderTrackingNotificationsListWsDto.setOrderNotifications(null);
				}
				else
				{
					mplOrderTrackingNotificationsListWsDto
							.setStatus(MarketplacecommerceservicesConstants.SET_EMPTY_ORDER_NOTIFICATIONS);
				}
			}
			else
			{
				mplOrderTrackingNotificationsListWsDto.setStatus(MarketplacecommerceservicesConstants.EMAIL_ID_IS_EMPTY);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplOrderTrackingNotificationsListWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplOrderTrackingNotificationsListWsDto.setErrorCode(e.getErrorCode());
			}
			mplOrderTrackingNotificationsListWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				mplOrderTrackingNotificationsListWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				mplOrderTrackingNotificationsListWsDto.setErrorCode(e.getErrorCode());
			}
			mplOrderTrackingNotificationsListWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return mplOrderTrackingNotificationsListWsDto;
	}

	/**
	 * @description method is called to get the type of Frequency
	 * @return Collection
	 */
	private List<String> getFrequency()
	{
		final List<EnumerationValueModel> enumList = mplEnumerationHelper.getEnumerationValuesForCode(Frequency._TYPECODE);
		final List<String> frequency = new ArrayList<String>();
		for (final EnumerationValueModel enumerationValueModel : enumList)
		{
			if (enumerationValueModel != null)
			{
				frequency.add(enumerationValueModel.getCode());
			}
		}
		return frequency;
	}



	/**
	 * @description method is called to get the type of Feedback
	 * @return Collection
	 */
	private List<String> getFeedbackArea()
	{
		final List<EnumerationValueModel> enumList = mplEnumerationHelper.getEnumerationValuesForCode(FeedbackArea._TYPECODE);
		final List<String> feedbackAreaList = new ArrayList<String>();
		for (final EnumerationValueModel enumerationValueModel : enumList)
		{
			if (enumerationValueModel != null)
			{
				final String code = enumerationValueModel.getCode().replace("_", " ");
				feedbackAreaList.add(code);
			}
		}
		return feedbackAreaList;
	}

	/**
	 * @description method is called to get the type of Interest Preference
	 * @return Collection
	 */
	private List<String> getInterestPreference()
	{
		final List<String> interestPreferenceList = new ArrayList<String>();
		interestPreferenceList.add(0, "I am interested in receiving e-mails");
		interestPreferenceList.add(1, "I am not interested in receiving e-mails");
		return interestPreferenceList;
	}










	//Added code from here

	// DeleteFavouritesBrand service

	/**
	 * DeleteFavouritesBrand Webservice
	 *
	 * @param login
	 * @param selectedBrand
	 * @param selectedCategory
	 * @param selectedFrequency
	 * @param myInterest
	 * @param feedbackUserReview
	 * @param feedbackCustomerSurveys
	 * @return
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/deleteFavouriteBrand", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'deleteFavouriteBrand',#fields)")
	public @ResponseBody MplUserResultWsDto deleteFavouriteBrand(@RequestParam final String login,
			@RequestParam final List selectedBrand, @RequestParam final List selectedCategory,
			@RequestParam final String selectedFrequency, @RequestParam final String myInterest,
			@RequestParam final boolean feedbackUserReview, @RequestParam final boolean feedbackCustomerSurveys)
	{
		final MplPreferenceData mplPreferenceData = new MplPreferenceData();
		final MplUserResultWsDto output = new MplUserResultWsDto();
		String result = null;
		//unused variable removed to solve sonar Critical issue
		//		final String error = null;

		mplPreferenceData.setFeedbackUserReview(feedbackUserReview);
		mplPreferenceData.setFeedbackCustomerSurveys(feedbackCustomerSurveys);
		if (null != selectedBrand && !selectedBrand.isEmpty())
		{
			mplPreferenceData.setSelectedBrand(selectedBrand);
		}

		if (null != selectedCategory && !selectedCategory.isEmpty())
		{
			mplPreferenceData.setSelectedCategory(selectedCategory);
		}

		if (null != selectedFrequency && !selectedFrequency.isEmpty())
		{
			mplPreferenceData.setSelectedFrequency(selectedFrequency);
		}

		if (null != myInterest)
		{
			mplPreferenceData.setMyInterest(myInterest);
		}

		try
		{

			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + login);
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
			mplPreferenceFacade.savePreferencesAndSubscription(mplPreferenceData);
			output.setStatus(result);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return output;
	}


	// to show favourtie brand/category in marketplace preference
	/**
	 * showFavoriteBrand service.
	 *
	 * @param login
	 * @return
	 * @throws Exception
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/showfavouritebrands", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'fetchPresavedFavouritePreferences',#fields)")
	public @ResponseBody MplPreferenceData fetchPresavedFavouritePreferences(@RequestParam final String login) throws Exception
	{
		MplPreferenceData mplPreferenceData = new MplPreferenceData();

		try
		{
			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + login);
			mplPreferenceData = mplPreferenceFacade.fetchPresavedFavouritePreferences();
		}
		catch (final UsernameNotFoundException notFound)
		{ //User name not found
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + notFound.getMessage());
			throw new Exception(CUSTOM_MESSAGE);
		}
		catch (final DataAccessException repositoryProblem)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + repositoryProblem.getMessage());
			throw new Exception(DATA_MESSAGE);
		}

		catch (final AuthenticationServiceException authServiceException)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + authServiceException.getMessage());
			throw new Exception(SERVICE_MESSAGE);
		}
		catch (final AuthenticationException authException)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + authException.getMessage());
			throw new Exception(AUTHENTICATION_MESSAGE);
		}
		catch (final Exception e)
		{ //Any other exception
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
			throw new Exception(ERROR_MESSAGE);
		}

		return mplPreferenceData;
	}


	/**
	 * unsubscribeAll Preferences Webservice
	 *
	 * @param login
	 * @return MplUserResultWsDto
	 * @throws Exception
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/unsubscribeAllPreferences", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	public @ResponseBody MplUserResultWsDto unsubscribeAllPreferences(@RequestParam final String login) throws Exception
	{
		final MplPreferenceData mplPreferenceData = new MplPreferenceData();
		final MplUserResultWsDto output = new MplUserResultWsDto();
		String result = null;

		try
		{
			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + login);
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
			mplPreferenceFacade.unsubscribeAllPreferences(mplPreferenceData);
			output.setStatus(result);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return output;
	}


	/**
	 * fetchNewsLetterSubscriptionContent Webservice
	 *
	 * @param login
	 * @return
	 * @throws Exception
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/fetchNewsLetterSubscrition", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	public @ResponseBody FetchNewsLetterSubscriptionWsDTO fetchNewsLetterSubscriptionContent(@RequestParam final String login)
			throws Exception
	{
		FetchNewsLetterSubscriptionWsDTO fetchNewsLetterSubscriptionWsDTO = new FetchNewsLetterSubscriptionWsDTO();

		try
		{
			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + login);
			fetchNewsLetterSubscriptionWsDTO = mobileUserService.fetchMplPreferenceContents();
			fetchNewsLetterSubscriptionWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				fetchNewsLetterSubscriptionWsDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				fetchNewsLetterSubscriptionWsDTO.setErrorCode(e.getErrorCode());
			}
			fetchNewsLetterSubscriptionWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				fetchNewsLetterSubscriptionWsDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				fetchNewsLetterSubscriptionWsDTO.setErrorCode(e.getErrorCode());
			}
			fetchNewsLetterSubscriptionWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return fetchNewsLetterSubscriptionWsDTO;
	}

	/**
	 * AddFavourite Brand WebService
	 *
	 * @param login
	 * @param selectedBrand
	 * @param selectedCategory
	 * @param selectedFrequency
	 * @param myInterest
	 * @param feedbackUserReview
	 * @param feedbackCustomerSurveys
	 * @return
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/addFavouriteBrand", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@Cacheable(value = "miscsCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'addFavouriteBrand',#fields)")
	public @ResponseBody MplUserResultWsDto addFavouriteBrand(@RequestParam final String login,
			@RequestParam final List selectedBrand, @RequestParam final List selectedCategory,
			@RequestParam final String selectedFrequency, @RequestParam final String myInterest,
			@RequestParam final boolean feedbackUserReview, @RequestParam final boolean feedbackCustomerSurveys)
	{
		final MplPreferenceData mplPreferenceData = new MplPreferenceData();
		final MplUserResultWsDto output = new MplUserResultWsDto();
		String result = null;
		mplPreferenceData.setFeedbackUserReview(feedbackUserReview);
		mplPreferenceData.setFeedbackCustomerSurveys(feedbackCustomerSurveys);
		if (null != selectedBrand && !selectedBrand.isEmpty())
		{
			mplPreferenceData.setSelectedBrand(selectedBrand);
		}

		if (null != selectedCategory && !selectedCategory.isEmpty())
		{
			mplPreferenceData.setSelectedCategory(selectedCategory);
		}

		if (null != selectedFrequency && !selectedFrequency.isEmpty())
		{
			mplPreferenceData.setSelectedFrequency(selectedFrequency);
		}

		if (null != myInterest)
		{
			mplPreferenceData.setMyInterest(myInterest);
		}

		try
		{

			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + login);
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
			mplPreferenceFacade.savePreferencesAndSubscription(mplPreferenceData);
			output.setStatus(result);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return output;


	}

	/**
	 * saveSubscription Webservice
	 *
	 * @param login
	 * @param selectedBrand
	 * @param selectedCategory
	 * @param selectedFrequency
	 * @param myInterest
	 * @return MplUserResultWsDto
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/saveSubscrition", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	public @ResponseBody MplUserResultWsDto saveSubscrition(@RequestParam final String login,
			@RequestParam final List selectedBrand, @RequestParam final List selectedCategory,
			@RequestParam final String selectedFrequency, @RequestParam final String myInterest,
			@RequestParam final boolean feedbackUserReview, @RequestParam final boolean feedbackCustomerSurveys)
	{
		final MplPreferenceData mplPreferenceData = new MplPreferenceData();
		final MplUserResultWsDto output = new MplUserResultWsDto();
		String result = null;
		//final String error = null;

		mplPreferenceData.setFeedbackCustomerSurveys(feedbackCustomerSurveys);
		mplPreferenceData.setFeedbackUserReview(feedbackUserReview);
		if (null != selectedBrand && !selectedBrand.isEmpty())
		{
			mplPreferenceData.setSelectedBrand(selectedBrand);
		}

		if (null != selectedCategory && !selectedCategory.isEmpty())
		{
			mplPreferenceData.setSelectedCategory(selectedCategory);
		}

		if (null != selectedFrequency && !selectedFrequency.isEmpty())
		{
			mplPreferenceData.setSelectedFrequency(selectedFrequency);
		}

		if (null != myInterest)
		{
			mplPreferenceData.setMyInterest(myInterest);
		}

		try
		{

			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + login);
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
			mplPreferenceFacade.savePreferencesAndSubscription(mplPreferenceData);
			output.setStatus(result);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return output;

	}

	/**
	 * getReasonForReturnsOrCancel webservice
	 *
	 * @param login
	 * @param returnCancelFlag
	 * @return ReturnReasonDetailsWsDTO
	 * @throws Exception
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/returnReasonForOrderItem", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	public @ResponseBody ReturnReasonDetailsWsDTO getReturnReasonForOrderItem(@RequestParam final String login,
			@RequestParam final String returnCancelFlag) throws Exception
	{
		ReturnReasonDetails returnReasonData = null;
		final ReturnReasonDetailsWsDTO returnReasonWsdto = new ReturnReasonDetailsWsDTO();
		try
		{
			//TODO This is a temporary fix , Casting addedd to resolved the build problem 16-AUG-15
			returnReasonData = mplOrderFacade.getReturnReasonForOrderItem(returnCancelFlag);
			if (null != returnReasonData && CollectionUtils.isNotEmpty(returnReasonData.getReturnReasonDetailsList()))
			{
				returnReasonWsdto.setReturnReasonDetailsList(returnReasonData.getReturnReasonDetailsList());
				returnReasonWsdto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}


		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				returnReasonWsdto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				returnReasonWsdto.setErrorCode(e.getErrorCode());
			}
			returnReasonWsdto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				returnReasonWsdto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				returnReasonWsdto.setErrorCode(e.getErrorCode());
			}
			returnReasonWsdto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return returnReasonWsdto;


	}

	/**
	 * webservices for generateReturnCancelRequest
	 *
	 * @param reasonCode
	 * @param ticketTypeCode
	 * @param ussid
	 * @param transactionId
	 * @param refundType
	 * @param orderCode
	 * @return MplUserResultWsDto
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/initiateRefund", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	public @ResponseBody MplUserResultWsDto initiateRefund(@PathVariable final String emailId,
			@RequestParam final String orderCode, @RequestParam final String reasonCode, @RequestParam final String ticketTypeCode,
			@RequestParam final String ussid, @RequestParam final String refundType, @RequestParam final String transactionId,
			@RequestParam(required = false) final String firstName, @RequestParam(required = false) final String lastName,
			@RequestParam(required = false) final String MobileNo, @RequestParam(required = false) final String addressLane1,
			@RequestParam(required = false) final String addressLane2, @RequestParam(required = false) final String countryIso,
			@RequestParam(required = false) final String city, @RequestParam(required = false) final String state,
			@RequestParam(required = false) final String landmark, @RequestParam(required = false) final String pincode,
			@RequestParam(required = false) final String revSealJwlry)
	{
		final MplUserResultWsDto output = new MplUserResultWsDto();
		boolean resultFlag = false;
		String result = null;
		final ReturnItemAddressData returnItemAddressData = new ReturnItemAddressData();
		OrderEntryData orderEntry = new OrderEntryData();
		try
		{
			returnItemAddressData.setFirstName(firstName);
			returnItemAddressData.setLastName(lastName);
			returnItemAddressData.setMobileNo(MobileNo);
			returnItemAddressData.setAddressLane1(addressLane1);
			returnItemAddressData.setAddressLane2(addressLane2);
			returnItemAddressData.setCity(city);
			returnItemAddressData.setCountry(countryIso);
			returnItemAddressData.setState(state);
			returnItemAddressData.setLandmark(landmark);
			returnItemAddressData.setPincode(pincode);
			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + emailId);
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
			final List<OrderEntryData> subOrderEntries = orderDetails.getEntries();
			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId))
				{
					orderEntry = entry;
					break;
				}
			}
			final boolean cancelFlag = getConfigurationService().getConfiguration()
					.getBoolean(MarketplacecommerceservicesConstants.CANCEL_ENABLE);
			final boolean returnFlag = getConfigurationService().getConfiguration()
					.getBoolean(MarketplacecommerceservicesConstants.RETURN_ENABLE);

			if (ticketTypeCode.equalsIgnoreCase("C") && cancelFlag)
			{
				resultFlag = cancelReturnFacade.implementCancelOrReturn(orderDetails, orderEntry, reasonCode, ussid, ticketTypeCode,
						customerData, refundType, false, SalesApplication.MOBILE);
			}
			else if (ticketTypeCode.equalsIgnoreCase("R") && returnFlag)
			{
				resultFlag = cancelReturnFacade.implementReturnItem(orderDetails, orderEntry, reasonCode, ussid, ticketTypeCode,
						customerData, refundType, true, SalesApplication.MOBILE, returnItemAddressData, revSealJwlry);
			}

			if (resultFlag)
			{
				result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
			}
			else
			{
				result = MarketplacecommerceservicesConstants.FAILURE_FLAG;
			}

			output.setStatus(result);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return output;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return output;
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			if (null != e.getMessage())
			{
				output.setError(e.getMessage());
			}
			output.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return output;
		}
		return output;

	}


	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/checkSelfCourierinReturnReq", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	public @ResponseBody ReturnLogisticsResponseDetailsWsDTO checkSelfCourierInReturnRequest(@RequestParam final String login,
			@RequestParam final String orderCode) throws Exception
	{
		List<ReturnLogisticsResponseData> response = new ArrayList<ReturnLogisticsResponseData>();
		ReturnLogisticsResponseDetails details = null;
		final ReturnLogisticsResponseDetailsWsDTO returnLogisticWsdto = new ReturnLogisticsResponseDetailsWsDTO();
		try
		{

			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + login);

			final OrderData orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			if (null != orderDetails)
			{
				response = cancelReturnFacade.checkReturnLogistics(orderDetails);
				details = new ReturnLogisticsResponseDetails();
				details.setReturnLogisticsResponseDataList(response);
				if (CollectionUtils.isNotEmpty(details.getReturnLogisticsResponseDataList()))
				{
					returnLogisticWsdto.setReturnLogisticsResponseDataList(details.getReturnLogisticsResponseDataList());
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				returnLogisticWsdto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				returnLogisticWsdto.setErrorCode(e.getErrorCode());
			}
			returnLogisticWsdto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				returnLogisticWsdto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				returnLogisticWsdto.setErrorCode(e.getErrorCode());
			}
			returnLogisticWsdto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return returnLogisticWsdto;
	}


	/**
	 *
	 * @param emailId
	 * @return MplFavBrandCategoryData
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 */
	/*
	 * @Secured( { CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	 */
	@RequestMapping(value = "{emailId}/getFavCategoriesBrands", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplAllFavouritePreferenceWsDTO getFavCategoriesBrands(@PathVariable final String emailId, final String fields,
			@RequestParam(required = false) final String deviceId)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException


	{
		final MplAllFavouritePreferenceWsDTO mplAllFavouritePreferenceWsDTO = new MplAllFavouritePreferenceWsDTO();
		final List<MplFavBrandCategoryWsDTO> favBrandCategoryDtoForCategory = new ArrayList<MplFavBrandCategoryWsDTO>();
		Map<String, MplFavBrandCategoryData> brandCategoryDataForCategory = new HashMap<String, MplFavBrandCategoryData>();
		try
		{
			brandCategoryDataForCategory = mplMyFavBrandCategoryFacade.fetchAllCategories();

			final List<CategoryModel> selectedCategoryList = mplMyFavBrandCategoryFacade.fetchFavCategories(emailId, deviceId);
			boolean flag = false;
			if (null != brandCategoryDataForCategory)
			{
				final Iterator<Map.Entry<String, MplFavBrandCategoryData>> categoryEntries = brandCategoryDataForCategory.entrySet()
						.iterator();
				while (categoryEntries.hasNext())
				{
					final Map.Entry<String, MplFavBrandCategoryData> entry = categoryEntries.next();
					//if (null != selectedCategoryList && selectedCategoryList.size() > 0)
					if (CollectionUtils.isNotEmpty(selectedCategoryList))
					{
						for (final CategoryModel catagory : selectedCategoryList)
						{
							if (catagory.getCode().equalsIgnoreCase(entry.getKey()))
							{
								flag = true;
								break;
							}
							else
							{
								flag = false;
							}
						}
						final MplFavBrandCategoryWsDTO mplFavBrandCategoryWsDTO = getDtoFromEntry(entry, flag);
						favBrandCategoryDtoForCategory.add(mplFavBrandCategoryWsDTO);
					}
					else
					{
						final MplFavBrandCategoryWsDTO mplFavBrandCategoryWsDTO = getDtoFromEntry(entry, false);
						favBrandCategoryDtoForCategory.add(mplFavBrandCategoryWsDTO);
					}
				}
			}


			//		fetching all brands
			final List<MplFavBrandCategoryWsDTO> favBrandCategoryDtoForBrand = new ArrayList<MplFavBrandCategoryWsDTO>();
			Map<String, MplFavBrandCategoryData> brandCategoryDataForBrand = new HashMap<String, MplFavBrandCategoryData>();
			brandCategoryDataForBrand = mplMyFavBrandCategoryFacade.fetchAllBrands();

			//final List<CategoryModel> selectedBrandList = mplMyFavBrandCategoryFacade.fetchFavBrands(emailId);
			final List<CategoryModel> selectedBrandList = mplMyFavBrandCategoryFacade.fetchFavBrands(emailId, deviceId);
			boolean result = false;
			if (null != brandCategoryDataForBrand)
			{
				final Iterator<Map.Entry<String, MplFavBrandCategoryData>> categoryEntries = brandCategoryDataForBrand.entrySet()
						.iterator();
				while (categoryEntries.hasNext())
				{
					final Map.Entry<String, MplFavBrandCategoryData> entry = categoryEntries.next();
					//if (null != selectedBrandList && selectedBrandList.size() > 0)
					if (CollectionUtils.isNotEmpty(selectedBrandList))
					{
						for (final CategoryModel catagory : selectedBrandList)
						{
							if (catagory.getCode().equalsIgnoreCase(entry.getKey()))
							{
								result = true;
								break;
							}
							else
							{
								result = false;
							}
						}
						final MplFavBrandCategoryWsDTO mplFavBrandCategoryWsDTO = getDtoFromEntry(entry, result);
						favBrandCategoryDtoForBrand.add(mplFavBrandCategoryWsDTO);
					}
					else
					{
						final MplFavBrandCategoryWsDTO mplFavBrandCategoryWsDTO = getDtoFromEntry(entry, false);
						favBrandCategoryDtoForBrand.add(mplFavBrandCategoryWsDTO);
					}
				}
			}

			//if (favBrandCategoryDtoForCategory.size() > 0 && favBrandCategoryDtoForBrand.size() > 0)
			if (CollectionUtils.isNotEmpty(favBrandCategoryDtoForCategory))
			{
				mplAllFavouritePreferenceWsDTO.setFavCategoryList(favBrandCategoryDtoForCategory);
				mplAllFavouritePreferenceWsDTO.setFavBrandList(favBrandCategoryDtoForBrand);
			}
			else
			{
				mplAllFavouritePreferenceWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				mplAllFavouritePreferenceWsDTO.setError(MarketplacecommerceservicesConstants.SEARCHNOTFOUND);
			}
		}
		catch (final IllegalArgumentException ex)
		{
			final EtailNonBusinessExceptions e = new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E9044);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplAllFavouritePreferenceWsDTO.setError(e.getErrorMessage());
			}
			mplAllFavouritePreferenceWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		catch (final NullPointerException ex)
		{
			final EtailNonBusinessExceptions e = new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E9044);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplAllFavouritePreferenceWsDTO.setError(e.getErrorMessage());
			}
			mplAllFavouritePreferenceWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		catch (final UnknownIdentifierException ex)
		{
			final EtailNonBusinessExceptions e = new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E9044);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplAllFavouritePreferenceWsDTO.setError(e.getErrorMessage());
			}
			mplAllFavouritePreferenceWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		catch (final Exception ex)
		{
			final EtailNonBusinessExceptions e = new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E9044);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplAllFavouritePreferenceWsDTO.setError(e.getErrorMessage());
			}
			mplAllFavouritePreferenceWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		return mplAllFavouritePreferenceWsDTO;
	}

	/**
	 * @param entry
	 * @return MplFavBrandCategoryWsDTO
	 */
	private MplFavBrandCategoryWsDTO getDtoFromEntry(final Entry<String, MplFavBrandCategoryData> entry, final boolean isSelected)
	{
		final MplFavBrandCategoryWsDTO mplFavBrandCategoryWsDTO = new MplFavBrandCategoryWsDTO();
		mplFavBrandCategoryWsDTO.setCode(entry.getValue().getCode());
		mplFavBrandCategoryWsDTO.setName(entry.getValue().getName());
		mplFavBrandCategoryWsDTO.setIsSelected(isSelected);
		if (null != entry.getValue().getLogo() && entry.getValue().getLogo().size() > 0)
		{
			mplFavBrandCategoryWsDTO.setLogo(entry.getValue().getLogo().get(0).getURL());
		}
		else
		{
			mplFavBrandCategoryWsDTO.setLogo(MarketplacecommerceservicesConstants.EMPTY);
		}
		return mplFavBrandCategoryWsDTO;
	}

	/*
	 * @Secured( { CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	 */
	@RequestMapping(value = "{emailId}/addFavCategoriesBrands", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto addFavCategoriesBrand(@PathVariable final String emailId, @RequestParam final List codeList,
			@RequestParam final String type, @RequestParam(required = false) final String deviceId)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		final UserResultWsDto resultDto = new UserResultWsDto();
		boolean result = false;
		try
		{
			if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_CATEGORY))
			{
				result = mplMyFavBrandCategoryFacade.addFavCategories(emailId, deviceId, codeList);
			}
			else if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_BRAND))
			{
				result = mplMyFavBrandCategoryFacade.addFavBrands(emailId, deviceId, codeList);
			}
			if (result)
			{
				resultDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				resultDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultDto.setErrorCode(e.getErrorCode());
			}
			resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				resultDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultDto.setErrorCode(e.getErrorCode());
			}
			resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ce)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ce);
			resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			resultDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_ERROR);
			resultDto.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			return resultDto;
		}
		return resultDto;

	}

	/*
	 * @Secured( { CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	 */
	@RequestMapping(value = "{emailId}/deleteFavCategoriesBrands", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto deleteFavCategoriesBrands(@PathVariable final String emailId, @RequestParam final String code,
			@RequestParam final String type, @RequestParam(required = false) final String deviceId)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		final UserResultWsDto resultDto = new UserResultWsDto();
		boolean result = false;
		try
		{
			if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_CATEGORY))
			{
				result = mplMyFavBrandCategoryFacade.deleteFavCategories(emailId, deviceId, code);
			}
			else if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_BRAND))
			{
				result = mplMyFavBrandCategoryFacade.deleteFavBrands(emailId, deviceId, code);
			}
			if (result)
			{
				resultDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				resultDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultDto.setErrorCode(e.getErrorCode());
			}
			resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				resultDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultDto.setErrorCode(e.getErrorCode());
			}
			resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ce)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ce);
			resultDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			resultDto.setError(MarketplacecommerceservicesConstants.EXCEPTION_ERROR);
			resultDto.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			return resultDto;
		}
		return resultDto;
	}

	/**
	 * @description method is called to add an Address to the Customer's Order
	 * @param addressId
	 * @param cartId
	 * @param firstName
	 * @param lastName
	 * @param line1
	 * @param line2
	 * @param line3
	 * @param town
	 * @param state
	 * @param countryIso
	 * @param postalCode
	 * @param phone
	 * @param addressType
	 * @param defaultFlag
	 * @param saveFlag
	 * @return UserResultWsDto
	 * @throws RequestParameterException
	 */
	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/addAddressToOrder", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto addAddressToOrder(@RequestParam(required = false) final String addressId,
			@PathVariable final String emailId, @RequestParam final String cartId,
			@RequestParam(required = false) final String firstName, @RequestParam(required = false) final String lastName,
			@RequestParam(required = false) final String line1, @RequestParam(required = false) final String line2,
			@RequestParam(required = false) final String line3, @RequestParam(required = false) final String landmark,
			@RequestParam(required = false) final String town, @RequestParam(required = false) final String state,
			@RequestParam(required = false) final String countryIso, @RequestParam(required = false) final String postalCode,
			@RequestParam(required = false) final String phone, @RequestParam(required = false) final String addressType,
			@RequestParam(required = false) final boolean defaultFlag, @RequestParam(required = false) final boolean saveFlag,
			@RequestParam(required = false, defaultValue = "false") final boolean removeExchangeFromCart)
			throws RequestParameterException
	{
		String errorMsg = null;
		String cartIdentifier;
		final UserResultWsDto result = new UserResultWsDto();
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			LOG.debug("addAddressToOrder : user : " + currentCustomer);
			// Check userModel null
			if (null != currentCustomer)
			{
				LOG.debug(String.format("addAddressToOrder: | addressId: %s | currentCustomer : %s | cartId : %s ", addressId,
						currentCustomer.getUid(), cartId));

				//getting cartmodel using cart id and user
				final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartId, currentCustomer);
				// Validate Cart Model is not null
				if (null != cartModel)
				{
					final AddressData newAddress = new AddressData();
					AddressModel addressmodel = modelService.create(AddressModel.class);
					//verifying if existing address

					if (StringUtils.isNotEmpty(addressId))
					{
						/*
						 * final List<AddressData> addressList = accountAddressFacade.getAddressBook(); for (final AddressData
						 * addEntry : addressList) { // Validate if valid address id for for the specific user if
						 * (addEntry.getId().equals(addressId)) {
						 */
						addressmodel = customerAccountService.getAddressForCode(currentCustomer, addressId);
						/*
						 * break; } }
						 */
					}
					//new address//
					else
					{
						errorMsg = validateStringField(countryIso, AddressField.COUNTRY, MAX_FIELD_LENGTH_COUNTRY);
						validation(errorMsg);
						errorMsg = validateStringField(firstName, AddressField.FIRSTNAME, MAX_FIELD_LENGTH_NEW);
						validation(errorMsg);
						errorMsg = validateStringField(lastName, AddressField.LASTNAME, MAX_FIELD_LENGTH_NEW);
						validation(errorMsg);
						errorMsg = validateStringField(line1, AddressField.LINE1, MAX_FIELD_LENGTH_ADDLINE);
						validation(errorMsg);
						errorMsg = validateStringField(line2, AddressField.LINE2, MAX_FIELD_LENGTH_ADDLINE);
						validation(errorMsg);
						errorMsg = validateStringField(line3, AddressField.LINE3, MAX_FIELD_LENGTH_ADDLINE);
						validation(errorMsg);
						if (null != landmark)
						{
							errorMsg = validateStringField(landmark, AddressField.LANDMARK, MAX_LANDMARK_LENGTH);
						}
						validation(errorMsg);
						errorMsg = validateStringField(town, AddressField.TOWN, MAX_FIELD_LENGTH_ADDLINE);
						validation(errorMsg);
						errorMsg = validateStringField(postalCode, AddressField.POSTCODE, MAX_POSTCODE_LENGTH);
						validation(errorMsg);
						errorMsg = validateStringField(addressType, AddressField.ADDRESSTYPE, MAX_FIELD_LENGTH);
						validation(errorMsg);
						errorMsg = validateStringField(state, AddressField.STATE, MAX_FIELD_LENGTH_STATE);
						validation(errorMsg);
						errorMsg = validateStringField(phone, AddressField.MOBILE, MAX_POSTCODE_LENGTH);
						validation(errorMsg);

						// If All fields validated
						if (null == errorMsg)
						{
							newAddress.setFirstName(firstName);
							newAddress.setLastName(lastName);
							newAddress.setLine1(line1);
							newAddress.setLine2(line2);
							newAddress.setLine3(line3);
							newAddress.setLandmark(landmark);
							newAddress.setTown(town);
							newAddress.setPostalCode(postalCode);
							newAddress.setBillingAddress(false);
							newAddress.setShippingAddress(true);
							newAddress.setVisibleInAddressBook(true);
							newAddress.setAddressType(addressType);
							newAddress.setPhone(phone);
							newAddress.setState(state);
							newAddress.setCountry(getI18NFacade().getCountryForIsocode(countryIso));
							newAddress.setDefaultAddress(defaultFlag);

							final boolean makeThisAddressTheDefault = newAddress.isDefaultAddress()
									|| (currentCustomer.getDefaultShipmentAddress() == null && newAddress.isVisibleInAddressBook());

							LOG.debug("addAddressToOrder : makeThisAddressTheDefault : " + makeThisAddressTheDefault);

							addressReversePopulator.populate(newAddress, addressmodel);
							addressmodel.setCellphone(newAddress.getPhone());
							addressmodel.setDistrict(newAddress.getState());
							addressmodel.setAddressType(newAddress.getAddressType());
							addressmodel.setLocality(newAddress.getLocality());
							addressmodel.setAddressLine3(newAddress.getLine3());

							//adding new address to user
							if (saveFlag)
							{
								customerAccountService.saveAddressEntry(currentCustomer, addressmodel);
								newAddress.setId(addressmodel.getPk().toString());
								LOG.debug("addAddressToOrder: AddresId" + newAddress.getId());
								if (makeThisAddressTheDefault)
								{
									customerAccountService.setDefaultAddressEntry(currentCustomer, addressmodel);
								}
							}
							else
							{
								//							final CartModel cart = getCartService().getSessionCart();
								addressmodel = mplCartWebService.createDeliveryAddressModel(newAddress, cartModel);
								modelService.save(addressmodel);
							}
						}
						else
						{
							throw new EtailBusinessExceptions(errorMsg);
						}
					}
					//Addition of address to cart
					if (userFacade.isAnonymousUser())
					{
						cartIdentifier = cartModel.getGuid();
						LOG.debug("addAddressToOrder: Anonymous User" + cartIdentifier + "  cartId: " + cartId);
					}
					else
					{
						cartIdentifier = cartModel.getCode();
						LOG.debug("addAddressToOrder: Loged in User" + cartIdentifier + "  cartId: " + cartId);
					}
					if (addressmodel != null && !removeExchangeFromCart)
					{
						for (final AbstractOrderEntryModel entry : cartModel.getEntries())
						{
							if (StringUtils.isNotEmpty(entry.getExchangeId()))
							{
								if (!exchangeFacade.isBackwardServiceble(addressmodel.getPostalcode()))
								{
									result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
									result.setError(MarketplacecommerceservicesConstants.REVERSE_PINCODE_NOT_SERVICABLE);

									throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9306);
								}

							}
						}

					}
					else if (removeExchangeFromCart)
					{
						for (final AbstractOrderEntryModel entry : cartModel.getEntries())
						{
							if (StringUtils.isNotEmpty(entry.getExchangeId()))
							{
								exchangeFacade.removeExchangefromCart(cartModel);

							}
						}
					}

					//true only in case of cance
					if (cartIdentifier.equals(cartId))
					{
						cartModel.setDeliveryAddress(addressmodel);
						modelService.save(cartModel);
						if (cartModel.getDeliveryAddress() != null)
						{
							result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							return result;
						}
					}
					else
					{
						result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						result.setError(MarketplacecommerceservicesConstants.INVALID_CART_ID);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
					}
				}
				else
				{
					//If  Cart Model is null display error message
					result.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
			}
			else
			{
				//If  User Model is null display error message
				result.setError(MarketplacewebservicesConstants.USEREMPTY);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
			}
		}
		catch (final ModelSavingException me)
		{
			// Error message for ModelSavingException Exceptions
			LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + me.getMessage());
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			result.setErrorCode(MarketplacecommerceservicesConstants.B9200);
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9200);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				result.setError(ex.getErrorMessage());
				result.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				result.setError(ex.getErrorMessage());
				result.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				result.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				result.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}
		return result;
	}

	/**
	 * @description method is called to get the details of merchant with respect to customer Email-Id
	 * @return GetmerchantWsDTO
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/getMerchant", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public GetmerchantWsDTO getMerchant(@PathVariable final String emailId)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{

		final GetmerchantWsDTO getmerchantWsDTO = new GetmerchantWsDTO();
		final UserModel user = userService.getCurrentUser();
		try
		{
			if (null == user && null == emailId)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			else
			{

				final String juspayMerchantKey = !getConfigurationService().getConfiguration()
						.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY).isEmpty()
								? getConfigurationService().getConfiguration()
										.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)
								: MarketplacecommerceservicesConstants.JUSPAYMERCHANTKEYNOTFOUND;

				final String juspayMerchantId = !getConfigurationService().getConfiguration()
						.getString(MarketplacecommerceservicesConstants.MARCHANTID).isEmpty()
								? getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.MARCHANTID)
								: MarketplacecommerceservicesConstants.JUSPAYMERCHANTIDNOTFOUND;

				getmerchantWsDTO.setMerchantID(juspayMerchantId);
				getmerchantWsDTO.setMerchantKey(juspayMerchantKey);
				getmerchantWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				getmerchantWsDTO.setError(e.getErrorMessage());
			}
			getmerchantWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				getmerchantWsDTO.setError(e.getErrorMessage());
			}
			getmerchantWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return getmerchantWsDTO;
	}


	/**
	 * @Description : For Juspay Order Creation. It returns juspayMerchantKey, juspayMerchantId,
	 *              juspayReturnUrl,juspayOrderId --TPR-629
	 * @return OrderCreateInJusPayWsDto
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.CREATEJUSPAYORDER, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public OrderCreateInJusPayWsDto createJuspayOrder(@RequestParam final String firstName, @RequestParam final String lastName,
			@RequestParam final String addressLine1, @RequestParam final String addressLine2,
			@RequestParam final String addressLine3, @RequestParam final String country, @RequestParam final String city,
			@RequestParam final String state, @RequestParam final String pincode, @RequestParam final String cardSaved,
			@RequestParam final String sameAsShipping, @PathVariable final String userId, @RequestParam final String cartGuid,
			@RequestParam(required = false) final String platform, @RequestParam(required = false) final String bankName,
			@RequestBody(required = false) final InventoryReservListRequestWsDTO item) throws EtailNonBusinessExceptions
	{
		OrderCreateInJusPayWsDto orderCreateInJusPayWsDto = new OrderCreateInJusPayWsDto();
		String uid = "";
		String failErrorCode = "";
		boolean failFlag = false;
		String juspayOrderId = "";
		OrderModel orderModel = null;
		//final OrderData orderData = null;
		CustomerModel customer = null;
		CartModel cart = null;
		String juspayMerchantId = "";
		String juspayReturnUrl = "";
		final StringBuilder returnUrlBuilder = new StringBuilder();
		String orderCode = null;
		double payableWalletAmount = 0.0D;
		double payableJuspayAmount = 0.0D;
		double totalWalletAmount = 0.0D;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("********* Creating juspay Order mobile web service" + userId);
		}


		try
		{

			// QC Wallet Changes Start

			// paying full Amount From Wallet in Case Of CLIQ CASH MODE
			orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			if (null != orderModel)
			{
				
				
				if (null != orderModel.getSplitModeInfo()
						&& orderModel.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH))
				{
					orderCreateInJusPayWsDto = patAmountUsingQC(userId, cartGuid, pincode, item);
					return orderCreateInJusPayWsDto;
				}
			}
			else
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				if (null != cart && null != cart.getSplitModeInfo()
						&& cart.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH))
				{
					orderCreateInJusPayWsDto = patAmountUsingQC(userId, cartGuid, pincode, item);
					return orderCreateInJusPayWsDto;
				}
			}
			// QC Wallet Changes END

			final String paymentAddressLine1 = java.net.URLDecoder.decode(addressLine1, UTF);
			final String paymentAddressLine2 = java.net.URLDecoder.decode(addressLine2, UTF);
			final String paymentAddressLine3 = java.net.URLDecoder.decode(addressLine3, UTF);

			customer = extendedUserService.getUserForOriginalUid(userId);
			if (null != customer)
			{
				uid = customer.getUid();
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0006);
			}
			// For Mobile
			juspayMerchantId = !getConfigurationService().getConfiguration()
					.getString(MarketplacecommerceservicesConstants.MARCHANTID).isEmpty()
							? getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.MARCHANTID)
							: NO_JUSPAY_MERCHANTKEY;

			if (commonUtils.isLuxurySite())
			{
				juspayReturnUrl = !getConfigurationService().getConfiguration()
						.getString(MarketplacecommerceservicesConstants.RETURNURLLUX).isEmpty()
								? getConfigurationService()


										.getConfiguration().getString(MarketplacecommerceservicesConstants.RETURNURLLUX)
								: NO_JUSPAY_URL;

			}
			else
			{
				juspayReturnUrl = !getConfigurationService().getConfiguration()
						.getString(MarketplacecommerceservicesConstants.RETURNURL).isEmpty()
								? getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.RETURNURL)
								: NO_JUSPAY_URL;
			}

			returnUrlBuilder.append(juspayReturnUrl);
			//To avoid backward- incompatibility,
			if (StringUtils.isNotEmpty(cartGuid))
			{
				returnUrlBuilder.append("?value=").append(cartGuid);
			}
			//Payment Soln changes
			orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			//If cart is present
			if (orderModel == null)
			{

				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				if (null != cart)
				{
					//TPR-4461 STARTS HERE WHEN ORDER MODEL IS NULL
					
					 // Buying Of EGV  Changes Start 
					if (null != cart.getIsEGVCart() && cart.getIsEGVCart().booleanValue())
					{

						orderCreateInJusPayWsDto = createJuspayOrderForEGV(firstName, lastName, country, state, city, pincode,
								cardSaved, sameAsShipping, cart.getGuid(), returnUrlBuilder, paymentAddressLine1, paymentAddressLine2,
								paymentAddressLine3, uid);
						if (null != orderCreateInJusPayWsDto && null != orderCreateInJusPayWsDto.getJuspayOrderId())
						{
							orderCreateInJusPayWsDto.setJuspayMerchantId(juspayMerchantId);
							orderCreateInJusPayWsDto.setJuspayReturnUrl(juspayReturnUrl);
							orderCreateInJusPayWsDto.setJuspayOrderId(juspayOrderId);
							orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						}
						else
						{
							orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);

						}
						return orderCreateInJusPayWsDto;

					}

					// Buying Of EGV  Changes End

					final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
							getVoucherService().getAppliedVouchers(cart));

					if (CollectionUtils.isNotEmpty(voucherList))
					{
						VoucherModel appliedVoucher = null;

						final DiscountModel discount = voucherList.get(0);

						if (discount instanceof PromotionVoucherModel)
						{
							final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
							appliedVoucher = promotionVoucherModel;

							final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
							for (final RestrictionModel restriction : restrictions)
							{
								if (restriction instanceof PaymentModeRestrictionModel)
								{
									boolean willApply = false;

									final String paymentModeCard = cart.getModeOfPayment();//Card Payment Mode (user)

									final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
											.getPaymentTypeData(); //Voucher Payment mode
									final List<BankModel> bankLists = ((PaymentModeRestrictionModel) restriction).getBanks(); //Voucher Bank Restriction List

									//	bank name missing	final String banknameforUserPaymentMode = getBankNameUserPaymentMode(); // Bank of User's Payment Mode

									if (CollectionUtils.isNotEmpty(paymentTypeList))
									{
										if (StringUtils.isNotEmpty(paymentModeCard))
										{
											for (final PaymentTypeModel paymentType : paymentTypeList)
											{
												if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
												{
													if (CollectionUtils.isEmpty(bankLists))
													{
														willApply = true;
													}
													else
													{
														willApply = getMplPaymentFacade().validateBank(bankLists, bankName);
													}
												}
												break;

											}
										}
										else
										{
											willApply = true;
										}
									}

									//if (willApply == false)
									if (!willApply)//SonarFix
									{

										if (StringUtils.isEmpty(bankName))
										{

											failFlag = true;
											failErrorCode = MarketplacecommerceservicesConstants.B9079;
										}


										else
										{
											orderCreateInJusPayWsDto
													.setErrorMessage(MarketplacecommerceservicesConstants.COUPONFAILUREMESSAGE);
											failFlag = true;
											failErrorCode = MarketplacecommerceservicesConstants.B9078;
											//failErrorCode = MarketplacecommerceservicesConstants.B9509;
										}
									}

								}
							}
						}
					}

					//TPR-4461 ENDS HERE WHEN ORDER MODEL IS NULL




					if (!failFlag && !mplCheckoutFacade.isPromotionValid(cart))
					{
						failFlag = true;
						failErrorCode = MarketplacecommerceservicesConstants.B9075;
					}
				}
				if (!failFlag && mplCartFacade.isCartEntryDelistedMobile(cart))
				{
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9325;
				}
				//TISUTO-12 , TISUTO-11
				//TODO Soft reservation calls already made


				if (!failFlag && !mplCartFacade.isInventoryReservedMobile(
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cart, pincode, item,
						SalesApplication.MOBILE))
				{
					//getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID,"TRUE");
					//getMplCartFacade().recalculate(cart);
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9047;
				}

				if (!failFlag)
				{
					final Double cartTotal = cart.getTotalPrice();
					final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();
					if (cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
					{

						final CustomerModel customerModel = mplPaymentWebFacade.getCustomer(userId);

						juspayMerchantId = !getConfigurationService().getConfiguration()
								.getString(MarketplacecommerceservicesConstants.MARCHANTID).isEmpty()
										? getConfigurationService().getConfiguration()
												.getString(MarketplacecommerceservicesConstants.MARCHANTID)
										: NO_JUSPAY_MERCHANTKEY;

						if (commonUtils.isLuxurySite())
						{
							juspayReturnUrl = !getConfigurationService().getConfiguration()
									.getString(MarketplacecommerceservicesConstants.RETURNURLLUX).isEmpty()
											? getConfigurationService().getConfiguration()
													.getString(MarketplacecommerceservicesConstants.RETURNURLLUX)
											: NO_JUSPAY_URL;

						}
						else
						{
							juspayReturnUrl = !getConfigurationService().getConfiguration()
									.getString(MarketplacecommerceservicesConstants.RETURNURL).isEmpty()
											? getConfigurationService().getConfiguration()
													.getString(MarketplacecommerceservicesConstants.RETURNURL)
											: NO_JUSPAY_URL;
						}

						if (null != cart.getSplitModeInfo() && cart.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT))
						{
							
							if (null != cart.getPayableWalletAmount())
							{
								totalWalletAmount = cart.getPayableWalletAmount().doubleValue();
							}
							
							if (totalWalletAmount > 0.0D)
							{
								payableJuspayAmount = cart.getTotalPrice().doubleValue()-totalWalletAmount;
							}else {
								payableJuspayAmount = cart.getTotalPrice().doubleValue();
							}
							orderCreateInJusPayWsDto.setCliqcashAmount(Double.valueOf(totalWalletAmount));
							orderCreateInJusPayWsDto.setCliqcashSelected(true);
						}
						else
						{
							payableJuspayAmount = cart.getTotalPrice().doubleValue();
						}
						juspayOrderId = mplPaymentFacade.createJuspayOrder(cart, null, firstName, lastName, addressLine1, addressLine2,
								addressLine3, country, state, city, pincode,
								cardSaved + MarketplacewebservicesConstants.STRINGSEPARATOR + sameAsShipping, juspayReturnUrl,
								customerModel.getUid(), MarketplacewebservicesConstants.CHANNEL_MOBILE, payableJuspayAmount);


						LOG.debug("********* Created juspay Order mobile web service *************" + juspayOrderId);

						orderCreateInJusPayWsDto.setJuspayMerchantId(juspayMerchantId);
						orderCreateInJusPayWsDto.setJuspayReturnUrl(juspayReturnUrl);
						orderCreateInJusPayWsDto.setJuspayOrderId(juspayOrderId);
						//final CartModel cartModel = mplPaymentWebDAO.findCartValues(cartID);
						//try
						//{
						//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
						failFlag = true;
						failErrorCode = MarketplacecommerceservicesConstants.B9509;
						//}
					}
				}
				//TISPRO-578
				if (!failFlag && !mplPaymentFacade.isValidCart(cart))
				{
					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID, "TRUE");
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9064;
				}

				if (failFlag)
				{
					throw new EtailBusinessExceptions(failErrorCode);
				}
				else
				{

					if (null != cart.getSplitModeInfo() && cart.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT))
					{
						if (null != cart.getPayableWalletAmount())
						{
							totalWalletAmount = cart.getPayableWalletAmount().doubleValue();
						}
						
						if (totalWalletAmount > 0.0D)
						{
							payableJuspayAmount = cart.getTotalPrice().doubleValue()-totalWalletAmount;
						}else {
							payableJuspayAmount = cart.getTotalPrice().doubleValue();
						}
						orderCreateInJusPayWsDto.setCliqcashAmount(Double.valueOf(payableWalletAmount));
						orderCreateInJusPayWsDto.setCliqcashSelected(true);
					}
					else
					{
						payableJuspayAmount = cart.getTotalPrice().doubleValue();
					}
					juspayOrderId = getMplPaymentFacade().createJuspayOrder(cart, null, firstName, lastName, paymentAddressLine1,
							paymentAddressLine2, paymentAddressLine3, country, state, city, pincode,
							cardSaved + MarketplacecommerceservicesConstants.STRINGSEPARATOR + sameAsShipping,
							returnUrlBuilder.toString(), uid, MarketplacecommerceservicesConstants.CHANNEL_MOBILE, payableJuspayAmount);
					//create order here
					//Mandatory checks agains cart
					final boolean isValidCart = getMplPaymentFacade().checkCart(cart);

					//getSessionService().setAttribute("guid", cart.getGuid());
					if (isValidCart)
					{
						//CAR-110
						//orderData = mplCheckoutFacade.placeOrderByCartId(cartGuid);
						orderCode = mplCheckoutFacade.placeOrderMobile(cart);
						if (orderCode == null)
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9321);
						}

					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
					}

				}


			}
			else
			{
				//TPR-4461 STARTS HERE WHEN ORDER MODEL IS NOT NULL
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
						getVoucherService().getAppliedVouchers(orderModel));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount instanceof PromotionVoucherModel)
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;

								final String paymentModeCard = orderModel.getModeOfOrderPayment();//Card Payment Mode (user)

								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode
								final List<BankModel> bankLists = ((PaymentModeRestrictionModel) restriction).getBanks(); //Voucher Bank Restriction List

								//	bank name missing	final String banknameforUserPaymentMode = getBankNameUserPaymentMode(); // Bank of User's Payment Mode

								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												if (CollectionUtils.isEmpty(bankLists))
												{
													willApply = true;
												}
												else
												{
													willApply = getMplPaymentFacade().validateBank(bankLists, bankName);
												}
											}
											break;

										}
									}
									else
									{
										willApply = true;
									}
								}

								//if (willApply == false)
								if (!willApply) //SonarFix
								{
									if (StringUtils.isEmpty(bankName))
									{
										//orderCreateInJusPayWsDto.setErrorMessage("Bank name is missing");
										failFlag = true;
										failErrorCode = MarketplacecommerceservicesConstants.B9079;
									}
									else
									{
										orderCreateInJusPayWsDto.setErrorMessage(MarketplacecommerceservicesConstants.COUPONFAILUREMESSAGE);
										failFlag = true;
										failErrorCode = MarketplacecommerceservicesConstants.B9078;
									}
								}

							}
						}
					}

				}
				//TPR-4461 ENDS HERE WHEN ORDER MODEL IS NOT NULL


				if (!getMplCheckoutFacade().isPromotionValid(orderModel))
				{

					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
					mplCartFacade.recalculateOrder(orderModel);
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9075;
				}
				//Soft reservation calls already made



				if (!failFlag && !mplCartFacade.isInventoryReservedMobile(
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel, pincode, item,
						SalesApplication.MOBILE))
				{
					//getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID,"TRUE");
					getMplCartFacade().recalculateOrder(orderModel);
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9047;
					//notify EMAil SMS TPR-815
					mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
				}

				if (failFlag)
				{
					throw new EtailBusinessExceptions(failErrorCode);
				}
				else
				{
					if (null != orderModel.getSplitModeInfo() && orderModel.getSplitModeInfo().equalsIgnoreCase(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT))
					{
						if (null != orderModel.getPayableWalletAmount())
						{
							totalWalletAmount = orderModel.getPayableWalletAmount().doubleValue();
						}
						
						if (totalWalletAmount > 0.0D)
						{
							payableJuspayAmount = orderModel.getTotalPrice().doubleValue()-totalWalletAmount;
						}else {
							payableJuspayAmount = orderModel.getTotalPrice().doubleValue();
						}
						orderCreateInJusPayWsDto.setCliqcashAmount(Double.valueOf(payableWalletAmount));
						orderCreateInJusPayWsDto.setCliqcashSelected(true);
					}
					else
					{
						payableJuspayAmount = orderModel.getTotalPrice().doubleValue();
					}
					juspayOrderId = getMplPaymentFacade().createJuspayOrder(null, orderModel, firstName, lastName, paymentAddressLine1,
							paymentAddressLine2, paymentAddressLine3, country, state, city, pincode,
							cardSaved + MarketplacecommerceservicesConstants.STRINGSEPARATOR + sameAsShipping,
							returnUrlBuilder.toString(), uid, MarketplacecommerceservicesConstants.CHANNEL_MOBILE, payableJuspayAmount);
					//CAR-110
					//orderData = mplCheckoutFacade.getOrderDetailsForCode(orderModel);
				}

			}
			if (StringUtils.isNotEmpty(juspayMerchantId))
			{
				orderCreateInJusPayWsDto.setJuspayMerchantId(juspayMerchantId);
			}
			if (StringUtils.isNotEmpty(juspayReturnUrl))
			{
				orderCreateInJusPayWsDto.setJuspayReturnUrl(juspayReturnUrl);
			}
			if (StringUtils.isNotEmpty(juspayOrderId))
			{
				orderCreateInJusPayWsDto.setJuspayOrderId(juspayOrderId);
			}
			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderCreateInJusPayWsDto.setCartGuid(cartGuid);
			}
			//if (orderCode != null && StringUtils.isNotEmpty(orderData.getCode()))
			if (orderCode != null && StringUtils.isNotEmpty(orderCode))
			{
				orderCreateInJusPayWsDto.setOrderId(orderCode);
			}

			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);

		}
		catch (final AdapterException e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				orderCreateInJusPayWsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9327));
				orderCreateInJusPayWsDto.setErrorCode(MarketplacecommerceservicesConstants.B9327);
			}
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			// Error message for All Exceptions
			if (null != e.getErrorMessage())
			{
				orderCreateInJusPayWsDto.setError(e.getErrorMessage());
				orderCreateInJusPayWsDto.setErrorCode(e.getErrorCode());
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				orderCreateInJusPayWsDto.setError(e.getErrorMessage());
				orderCreateInJusPayWsDto.setErrorCode(e.getErrorCode());
			}

		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				orderCreateInJusPayWsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				orderCreateInJusPayWsDto.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return orderCreateInJusPayWsDto;
	}

	private OrderCreateInJusPayWsDto createJuspayOrderForEGV(final String firstName, final String lastName, final String country,
			final String state, final String city, final String pincode, final String cardSaved, final String sameAsShipping,
			final String guid, final StringBuilder returnUrlBuilder, final String paymentAddressLine1,
			final String paymentAddressLine2, final String paymentAddressLine3, final String uid) throws InvalidCartException
	{

		final OrderCreateInJusPayWsDto juspayOrderWsDto = new OrderCreateInJusPayWsDto();
		try
		{

			final CartModel cart = mplEGVCartService.getEGVCartModel(guid);
			LOG.info("::Going to Create Juspay OrderId::");
			final String juspayOrderId = getMplPaymentFacade().createJuspayOrder(cart, null, firstName, lastName,
					paymentAddressLine1, paymentAddressLine2, paymentAddressLine3, country, state, city, pincode,
					cardSaved + MarketplacewebservicesConstants.STRINGSEPARATOR + sameAsShipping, returnUrlBuilder.toString(), uid,
					MarketplacewebservicesConstants.CHANNEL_MOBILE, cart.getTotalPrice().doubleValue());
			final OrderModel order = getMplCheckoutFacade().placeEGVOrder(cart);
			if (null != order && null != order.getCode())
			{
				juspayOrderWsDto.setOrderId(order.getCode());
			}
			if (null != juspayOrderId)
			{
				juspayOrderWsDto.setJuspayOrderId(juspayOrderId);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Exception Occurred while createJuspayOrderForEGV " + e.getMessage());
		}
		catch (final Exception e)
		{
			LOG.error("Exception Occurred while createJuspayOrderForEGV " + e.getMessage());
		}

		return juspayOrderWsDto;
	}


	/**
	 * @description method is called to generate update profileURL
	 * @param request
	 * @param specificUrl
	 */
	public String urlForEmailContext(final HttpServletRequest request, final String specificUrl)
	{
		URL requestUrl;
		String profileUpdateUrl = MarketplacecommerceservicesConstants.EMPTY;
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
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0016));




			return MarketplacecommerceservicesConstants.FAILURE;
		}
		return profileUpdateUrl;
	}





	/**
	 * @Description : For getting the details of all the Coupons available for the User
	 * @param emailId
	 * @param currentPage
	 * @param pageSize
	 * @param usedCoupon
	 * @param sortCode
	 * @return CommonCouponsDTO
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/getCoupons", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public CommonCouponsDTO getCoupons(@PathVariable final String emailId, @RequestParam final int currentPage,
			/* @RequestParam final int pageSize, */@RequestParam final String usedCoupon,
			@RequestParam(value = MarketplacewebservicesConstants.SORT, required = false) final String sortCode)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		CommonCouponsDTO couponDto = new CommonCouponsDTO();
		try
		{
			//couponDto = mplCouponWebFacade.getCoupons(currentPage, pageSize, emailId, usedCoupon, sortCode);
			couponDto = mplCouponWebFacade.getCoupons(currentPage, emailId, usedCoupon, sortCode);
			couponDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				couponDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				couponDto.setErrorCode(e.getErrorCode());
			}
			couponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				couponDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				couponDto.setErrorCode(e.getErrorCode());
			}
			couponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return couponDto;
	}


	/**
	 * @description method is called to generate update profileURL mobile service
	 * @param request
	 * @param specificUrl
	 */
	public String urlForMobileEmailContext(final HttpServletRequest request, final String specificUrl)
	{
		URL requestUrl;
		String profileUpdateUrl = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			requestUrl = new URL(request.getRequestURL().toString());
			final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
			//final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + ""; Do not add empty strings
			final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;

			profileUpdateUrl = baseUrl + specificUrl;
		}
		catch (final MalformedURLException e)
		{
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0016));




			return MarketplacecommerceservicesConstants.FAILURE;
		}
		return profileUpdateUrl;
	}


	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/returnPincode", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ReturnPincodeDTO returnPincodeServiceability(@RequestParam final String pincode, @RequestParam final String orderCode,
			@RequestParam final String transactionId) throws EtailNonBusinessExceptions
	{

		final ReturnPincodeDTO returnPincodeDTO = new ReturnPincodeDTO();

		ReturnPincodeDTO returnPincodeAvailDTO = null;

		try
		{

			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);

			boolean returnLogisticsCheck = true;
			for (final ConsignmentData consignmentData : orderDetails.getConsignments())
			{
				if (consignmentData.getStatus() != null && consignmentData.getStatus().getCode().equalsIgnoreCase("DELIVERED"))
				{

					returnPincodeAvailDTO = cancelReturnFacade.checkReturnLogisticsForApp(orderDetails, pincode, transactionId);
				}
				else
				{
					returnPincodeDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
			}


			for (final ReturnLogisticsResponseDTO response : returnPincodeAvailDTO.getReturnLogisticsResponseDTO())
			{
				if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase("N"))
				{
					returnLogisticsCheck = false;

				}

				if (!returnLogisticsCheck)
				{
					returnPincodeDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
					returnPincodeDTO.setIsPincodeServiceable(false);
					returnPincodeDTO.setReturnLogisticsResponseDTO(returnPincodeAvailDTO.getReturnLogisticsResponseDTO());
				}
				else
				{
					returnPincodeDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					returnPincodeDTO.setIsPincodeServiceable(true);
					returnPincodeDTO.setReturnLogisticsResponseDTO(returnPincodeAvailDTO.getReturnLogisticsResponseDTO());

				}
			}

		}




		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				returnPincodeDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				returnPincodeDTO.setErrorCode(e.getErrorCode());
			}
			returnPincodeDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				returnPincodeDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				returnPincodeDTO.setErrorCode(e.getErrorCode());
			}
			returnPincodeDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			returnPincodeDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
			returnPincodeDTO.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			returnPincodeDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return returnPincodeDTO;
	}


	/**
	 * TPR-1630 Display product details at time of return
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/returnProductDetails", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ReturnRequestDTO returnProductDetails(@RequestParam final String orderCode, @RequestParam final String transactionId,
			@RequestParam final String returnCancelFlag) throws EtailNonBusinessExceptions
	{
		final ReturnRequestDTO returnRequestDTO = new ReturnRequestDTO();
		ReturnReasonDetails returnReasonData = null;
		ReturnReasonDTO reasonDto = new ReturnReasonDTO();
		final List<ReturnReasonDTO> returnReasondtolist = new ArrayList<ReturnReasonDTO>();
		final String revSealSellerList = getConfigurationService().getConfiguration()
				.getString("finejewellery.reverseseal.sellername");
		boolean isFineJew = false;
		boolean showRevSeal = false;
		final RevSealJwlryDataWsDTO revSealFrJwlry = new RevSealJwlryDataWsDTO();
		final ReturnModesWsDTO returnModes = new ReturnModesWsDTO();
		try
		{
			final List<OrderProductWsDTO> orderproductWsDto = getOrderDetailsFacade.getOrderdetailsForApp(orderCode, transactionId,
					returnCancelFlag);
			if (orderproductWsDto.size() > 0)
			{

				returnRequestDTO.setOrderProductWsDTO(orderproductWsDto);
				returnReasonData = mplOrderFacade.getReturnReasonForOrderItem(returnCancelFlag);

				//TPR-4134 starts
				returnModes.setSelfCourier(true);
				returnModes.setSchedulePickup(true);
				returnModes.setQuickDrop(true);
				returnRequestDTO.setShowReverseSealFrJwlry(MarketplacecommerceservicesConstants.NO);

				if (StringUtils.isNotEmpty(revSealSellerList))
				{
					final List<String> sellerList = Arrays.asList(revSealSellerList.split(","));
					for (final OrderProductWsDTO orderEntry : orderproductWsDto)
					{
						if (sellerList.contains(orderEntry.getSellerName()))
						{
							showRevSeal = true;
							revSealFrJwlry.setMessage(MarketplacecommerceservicesConstants.REV_SEAL_JWLRY);
							revSealFrJwlry.setYes("Y");
							revSealFrJwlry.setNo("N");
							LOG.debug("Reverse seal section will be shown");
							break;
						}
					}
				}
				for (final OrderProductWsDTO orderEntryDto : orderproductWsDto)
				{
					final ProductModel productModel = getMplOrderFacade().getProductForCode(orderEntryDto.getProductcode());
					if (null != productModel && MarketplacecommerceservicesConstants.FINEJEWELLERY
							.equalsIgnoreCase(productModel.getProductCategoryType()))
					{
						isFineJew = true;
						if (showRevSeal)
						{
							returnRequestDTO.setShowReverseSealFrJwlry(MarketplacecommerceservicesConstants.YES);
						}
						returnModes.setSelfCourier(false);
						returnRequestDTO.setReverseSealFrJwlry(revSealFrJwlry);
						break;
					}
				}
				returnRequestDTO.setReturnModes(returnModes);
				//TPR-4134 ends
			}
			if (null != returnReasonData && CollectionUtils.isNotEmpty(returnReasonData.getReturnReasonDetailsList()))
			{
				for (final ReturnReasonData entry : returnReasonData.getReturnReasonDetailsList())
				{
					if (!isFineJew
							&& MarketplacecommerceservicesConstants.RETURN_FINEJEWELLERY.equalsIgnoreCase(entry.getReasonDescription()))
					{
						continue;
					}
					reasonDto = dataMapper.map(entry, ReturnReasonDTO.class);
					returnReasondtolist.add(reasonDto);

				}
				returnRequestDTO.setReturnReasonDetailsWsDTO(returnReasondtolist);
			}

			else
			{
				returnRequestDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				returnRequestDTO.setErrorCode(MarketplacecommerceservicesConstants.B9004);
				returnRequestDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				returnRequestDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9076));
			}

		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			returnRequestDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
			returnRequestDTO.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			returnRequestDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return returnRequestDTO;
	}



	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/returnRequest", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public ReturnDetailsWsDTO getReturnDetailsForOrderItem(final HttpServletRequest request, @RequestParam final String orderCode,
			final String transactionId, final String userId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields) throws Exception
	{
		String sellerRichAttrOfQuickDrop = null;
		String productRichAttrOfQuickDrop = null;
		boolean returnLogisticsAvailability = false;
		CODSelfShipData codSelfShipData = new CODSelfShipData();
		final ReturnDetailsWsDTO returnDeatails = new ReturnDetailsWsDTO();
		List<ReturnReasonData> reasonList = new ArrayList<ReturnReasonData>();
		List<PointOfServiceData> returnableStores = new ArrayList<PointOfServiceData>();
		String ussid = "";
		boolean isFineJew = false;
		try
		{
			final OrderModel subOrderModel = orderModelService.getOrder(orderCode);
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();
			OrderEntryData orderEntry = new OrderEntryData();
			List<OrderEntryData> returnOrderEntry = new ArrayList<OrderEntryData>();
			final Map<String, List<OrderEntryData>> returnProductMap = new HashMap<>();
			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId.trim()))
				{
					orderEntry = entry;
					returnOrderEntry = cancelReturnFacade.associatedEntriesData(orderModelService.getOrder(orderCode),
							transactionId.trim());
					returnProductMap.put(orderEntry.getTransactionId(), returnOrderEntry);

					final ProductModel productModel = getMplOrderFacade().getProductForCode(orderEntry.getProduct().getCode());
					List<RichAttributeModel> productRichAttributeModel = null;
					if (null != productModel && productModel.getRichAttribute() != null)
					{
						productRichAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();
						if (productRichAttributeModel != null && productRichAttributeModel.get(0).getReturnAtStoreEligible() != null)
						{
							productRichAttrOfQuickDrop = productRichAttributeModel.get(0).getReturnAtStoreEligible().toString();
						}
					}

					if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
					{
						isFineJew = true;
						final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
								.getJewelleryInfoByUssid(orderEntry.getSelectedUssid());
						ussid = (CollectionUtils.isNotEmpty(jewelleryInfo)) ? jewelleryInfo.get(0).getPCMUSSID() : "";

						LOG.debug("PCMUSSID FOR JEWELLERY :::::::::: " + "for " + orderEntry.getSelectedUssid() + " is "
								+ jewelleryInfo.get(0).getPCMUSSID());
					}
					else
					{
						ussid = orderEntry.getSelectedUssid();
					}

					final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
							.getSellerInformationRelator();

					for (final SellerInformationModel sellerInformationModel : sellerInfo)
					{
						/* if (sellerInformationModel.getSellerArticleSKU().equals(orderEntry.getSelectedUssid())) */
						if (sellerInformationModel.getSellerArticleSKU().equals(ussid))
						{
							List<RichAttributeModel> sellerRichAttributeModel = null;
							if (sellerInformationModel.getRichAttribute() != null)
							{
								sellerRichAttributeModel = (List<RichAttributeModel>) sellerInformationModel.getRichAttribute();
								if (sellerRichAttributeModel != null
										&& sellerRichAttributeModel.get(0).getReturnAtStoreEligible() != null)
								{
									sellerRichAttrOfQuickDrop = sellerRichAttributeModel.get(0).getReturnAtStoreEligible().toString();
								}
							}
						}
						if (!(entry.isGiveAway() || entry.isIsBOGOapplied()))
						{
							returnLogisticsAvailability = true;
						}
					}
					break;
				}

			}
			reasonList = mplOrderService.getReturnReasonForOrderItem();

			if (!isFineJew)
			{
				for (final Iterator<ReturnReasonData> iterator = reasonList.iterator(); iterator.hasNext();)
				{
					final ReturnReasonData returnData = iterator.next();
					if (MarketplacecommerceservicesConstants.RETURN_FINEJEWELLERY.equalsIgnoreCase(returnData.getReasonDescription()))
					{
						iterator.remove();
					}
				}
			}

			final List<String> timeSlots = mplConfigFacade.getDeliveryTimeSlots("RD");
			final List<String> returnableDates = cancelReturnFacade.getReturnableDates(orderEntry);
			returnDeatails.setReturnTimeSlots(timeSlots);
			returnDeatails.setReturnDates(returnableDates);
			returnDeatails.setReturnReasonDetailsList(reasonList);
			if (orderEntry.getDeliveryPointOfService() != null)
			{
				returnableStores = pincodeServiceFacade.getAllReturnableStores(
						orderEntry.getDeliveryPointOfService().getAddress().getPostalCode(),
						StringUtils.substring(orderEntry.getSelectedUssid(), 0, 6));
			}
			else
			{
				returnableStores = pincodeServiceFacade.getAllReturnableStores(subOrderDetails.getDeliveryAddress().getPostalCode(),
						StringUtils.substring(orderEntry.getSelectedUssid(), 0, 6));
			}
			returnDeatails.setReturnStoreDetailsList(returnableStores);
			final OrderEntryDataList dataList = new OrderEntryDataList();
			dataList.setOrderEntries(returnOrderEntry);
			final OrderEntryListWsDTO returndto = dataMapper.map(dataList, OrderEntryListWsDTO.class, fields);
			//OrderDataWsDTO orderDto = getOrderDetailsFacade.getOrderdetails(subOrderModel.getParentReference().getCode());
			//TISRLUAT-818	start
			final String scheme = request.getScheme();
			final String serverName = request.getServerName();
			final String portNumber = String.valueOf(request.getServerPort());
			final StringBuilder sb = new StringBuilder(scheme);
			sb.append(MarketplacewebservicesConstants.COLON);
			sb.append(MarketplacewebservicesConstants.FORWARD_SLASHES);
			sb.append(serverName);
			if (null != portNumber)
			{
				sb.append(MarketplacewebservicesConstants.COLON);
				sb.append(portNumber);
			}
			sb.append(MarketplacewebservicesConstants.RETURN_SELF_COURIER_FILE_DOWNLOAD_URL);
			sb.append(orderCode);
			sb.append(MarketplacewebservicesConstants.AMPERSAND);
			sb.append(MarketplacewebservicesConstants.TRANSACTION_ID);
			sb.append(MarketplacewebservicesConstants.EQUALS_TO);
			sb.append(transactionId);
			final String SelfCourierDocumentLink = String.valueOf(sb);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Self Courier return file download location for transaction id " + transactionId + " with order code  "
						+ orderCode + " is " + SelfCourierDocumentLink);
			}
			returnDeatails.setSelfCourierDocumentLink(SelfCourierDocumentLink);

			//TISRLUAT-818 end
			try
			{
				//TISRLUAT-1160 Start
				final CustomerModel customerModel = (CustomerModel) subOrderModel.getUser();
				codSelfShipData = cancelReturnFacade.getCustomerBankDetailsByCustomerId(customerModel.getUid());
				//TISRLUAT-1160 End

			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(
						"Exception occured for fecting CUstomer Bank details for customer ID :" + userId + " Actual Stack trace " + e);


			}
			catch (final Exception e)
			{
				LOG.error(
						"Exception occured for fecting CUstomer Bank details for customer ID :" + userId + " Actual Stack trace " + e);


			}
			final List<AddressData> addressList = mplCheckoutFacadeImpl
					.rePopulateDeliveryAddress(getAccountAddressFacade().getAddressBook());


			if (codSelfShipData != null)
			{
				returnDeatails.setCodSelfShipData(codSelfShipData);
			}
			returnDeatails.setDeliveryAddressesList(addressList);
			//returnDeatails.setOrderDetails(orderDto);

			returnDeatails.setDeliveryAddress(subOrderDetails.getDeliveryAddress());
			returnDeatails.setReturnEntry(returndto);
			returnDeatails.setProductRichAttrOfQuickDrop(productRichAttrOfQuickDrop);
			returnDeatails.setReturnLogisticsAvailability(returnLogisticsAvailability);
			returnDeatails.setSellerRichAttrOfQuickDrop(sellerRichAttrOfQuickDrop);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			returnDeatails.setErrorCode(e.getErrorMessage());

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			returnDeatails.setErrorCode(e.getErrorMessage());
		}
		catch (final Exception e)
		{
			returnDeatails.setErrorCode(e.getMessage());
		}
		return returnDeatails;
	}

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/returnInitiate", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto initiateRefund(@RequestBody final ReturnRequestDTO returnData) throws Exception
	{
		boolean cancellationStatus = false;
		final String orderCode = returnData.getOrderCode();
		final String transactionId = returnData.getTransactionId();
		final String pinCode = returnData.getPincode();
		final ReturnInfoData returnInfoData = new ReturnInfoData();
		final MplUserResultWsDto output = new MplUserResultWsDto();
		final ReturnItemAddressData returnAddrData = new ReturnItemAddressData();
		try
		{
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			OrderEntryData subOrderEntry = new OrderEntryData();
			final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();

			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId))
				{
					subOrderEntry = entry;
					break;
				}
			}

			//for schedule pickup
			if (StringUtils.isNotBlank(returnData.getReturnMethod())
					&& MarketplacecommerceservicesConstants.RETURN_SCHEDULE.equalsIgnoreCase(returnData.getReturnMethod()))
			{
				final List<String> times = MplTimeconverUtility.splitTime(returnData.getScheduleReturnTime());
				String timeSlotFrom = null;
				String timeSlotto = null;
				for (final String time : times)
				{
					if (null == timeSlotFrom)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Return Pickup Slot From Time :" + timeSlotFrom + " for the TransactionId :"
									+ returnData.getTransactionId());
						}
						timeSlotFrom = time;
					}
					else
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Return Pickup Slot From Time :" + timeSlotto + " for the TransactionId :"
									+ returnData.getTransactionId());
						}
						timeSlotto = time;
					}

				}
				boolean returnLogisticsCheck = true;
				String returnFulfillmentType = null;
				final List<ReturnLogisticsResponseData> returnLogisticsRespList = cancelReturnFacade

						.checkReturnLogistics(subOrderDetails, pinCode, transactionId);
				for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
				{
					if (response.getTransactionId().equalsIgnoreCase(returnData.getTransactionId()))
					{
						if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase("N"))
						{
							returnLogisticsCheck = false;
							output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
						}
						else if (response.getIsReturnLogisticsAvailable().equalsIgnoreCase("Y"))
						{
							returnFulfillmentType = response.getReturnFulfillmentType();
							output.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
						}
					}
				}
				if (!returnLogisticsCheck)
				{
					return output;
				}
				final String returnPickupDate = returnData.getScheduleReturnDate();
				returnInfoData.setReasonCode(returnData.getReturnReasonCode());
				if (returnData.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
				{
					returnInfoData.setRefundType(MarketplacecommerceservicesConstants.N);
				}
				else
				{
					returnInfoData.setRefundType(MarketplacecommerceservicesConstants.S);
				}
				returnInfoData.setReturnPickupDate(dateUtilHelper.convertDateWithFormat(returnPickupDate));
				returnInfoData.setTicketTypeCode(MarketplacecommerceservicesConstants.RETURN_TYPE);
				returnInfoData.setTimeSlotFrom(timeSlotFrom);
				returnInfoData.setTimeSlotTo(timeSlotto);
				returnInfoData.setUssid(returnData.getUssid());
				returnInfoData.setReturnMethod(returnData.getReturnMethod());
				returnInfoData.setReturnFulfillmentMode(returnFulfillmentType);

				returnAddrData.setAddressLane1(returnData.getAddrLine1());
				returnAddrData.setAddressLane2(returnData.getAddrLine2());
				returnAddrData.setAddressLine3(returnData.getAddrLine3());
				returnAddrData.setLandmark(returnData.getLandMark());
				returnAddrData.setCity(returnData.getCity());
				returnAddrData.setCountry(returnData.getCountry());
				returnAddrData.setFirstName(returnData.getFirstName());
				returnAddrData.setLastName(returnData.getLastName());
				returnAddrData.setMobileNo(returnData.getPhoneNumber());
				returnAddrData.setState(getStateCode(returnData.getState()));
				returnAddrData.setPincode(returnData.getPincode());
				//TPR-4134
				if (null != returnData.getReverseSealAvailable())
				{
					returnInfoData.setReverseSealLostflag(returnData.getReverseSealAvailable());
				}

				if (returnData.getRefundType().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_TYPE))
				{
					cancellationStatus = cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry, returnInfoData,
							customerData, SalesApplication.MOBILE, returnAddrData);
				}
				if (!cancellationStatus)
				{
					output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					return output;
				}
				else
				{
					output.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
				}
			}

			//for quick drop
			if (returnData.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_METHOD_QUICKDROP))
			{
				try
				{
					final RTSAndRSSReturnInfoRequestData infoRequestData = new RTSAndRSSReturnInfoRequestData();
					final List<String> stores = returnData.getStoreIds();
					if (null != subOrderDetails.getPurchaseOrderNumber())
					{
						infoRequestData.setOrderId(subOrderDetails.getPurchaseOrderNumber());
					}
					else
					{
						infoRequestData.setOrderId(returnData.getOrderCode());
					}
					infoRequestData.setRTSStore(stores);
					infoRequestData.setTransactionId(transactionId);
					infoRequestData.setReturnType(MarketplacecommerceservicesConstants.RETURN_TYPE_RTS);
					//return info call to OMS
					cancelReturnFacade.retrunInfoCallToOMS(infoRequestData);
					output.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
				}
				catch (final Exception e)
				{
					LOG.error("Eception occurred while doing return in quickDrop Mehod for order " + orderCode + " exception is "
							+ e.getMessage());
					output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					return output;
				}
			}

			if (returnData.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
			{
				final CODSelfShipData selfShipData = new CODSelfShipData();
				selfShipData.setCustomerNumber(customerData.getUid());
				selfShipData.setTitle(returnData.getTitle());
				selfShipData.setName(returnData.getAccountHolderName());
				selfShipData.setBankAccount(returnData.getAccountNumber());
				selfShipData.setBankName(returnData.getBankName());
				selfShipData.setBankKey(returnData.getIFSCCode());
				selfShipData.setOrderNo(returnData.getOrderCode());
				selfShipData.setTransactionID(returnData.getTransactionId());
				selfShipData.setPaymentMode(returnData.getRefundMode());

				if (null != returnData.getIsCODorder()
						&& returnData.getIsCODorder().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
				{
					//set ordertag POSTPAIDRRF for COD orders
					selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_POSTPAID);
				}
				else
				{
					//set ordertag POSTPAIDRRF for PREPAID orders
					selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_PREPAID);
				}
				try
				{
					//inser or update Customer Bank Details
					cancelReturnFacade.insertUpdateCustomerBankDetails(selfShipData);
				}

				catch (final EtailNonBusinessExceptions e)
				{
					LOG.error("Exception Occured during saving Customer BankDetails for COD order : " + orderCode
							+ MarketplacecommerceservicesConstants.EXCEPTIONCAUSELOG + e);
				}
				catch (final Exception e)
				{
					LOG.error("Exception Occured during saving Customer BankDetails for COD order : " + orderCode
							+ MarketplacecommerceservicesConstants.EXCEPTIONCAUSELOG + e);

				}

				try
				{

					// sending COD BANK Details to fico
					final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
					AbstractOrderEntryModel entry = modelService.create(AbstractOrderEntryModel.class);
					for (final AbstractOrderEntryModel e : orderModel.getEntries())
					{
						if (null != e.getTransactionID() && e.getTransactionID().equalsIgnoreCase(transactionId))
						{
							entry = e;
						}
					}
					final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					selfShipData.setOrderNo(orderCode);
					selfShipData.setOrderRefNo(orderModel.getParentReference().getCode());
					selfShipData.setTransactionType(MarketplacecommerceservicesConstants.RETURN_TRANSACTON_TYPE_01);
					selfShipData
							.setTransactionDate(dateUtilHelper.convertDateWithFormat(formatter.format(orderModel.getCreationtime())));
					selfShipData.setOrderDate(dateUtilHelper.convertDateWithFormat(formatter.format(orderModel.getCreationtime())));
					selfShipData.setOrderTag(MarketplacecommerceservicesConstants.ORDERTAG_TYPE_POSTPAID);
					selfShipData.setCustomerNumber(orderModel.getUser().getUid());

					selfShipData.setTransactionID(transactionId);
					if (null != entry.getTotalPrice())
					{
						selfShipData.setAmount(entry.getTotalPrice().toString());
					}
					if (null != entry.getNetAmountAfterAllDisc())
					{
						selfShipData.setAmount(entry.getNetAmountAfterAllDisc().toString());
					}
					cancelReturnFacade.codPaymentInfoToFICO(selfShipData);
				}
				catch (final EtailNonBusinessExceptions e)
				{
					LOG.error("Exception Occured while sending bank details to Fico  :  " + orderCode
							+ MarketplacecommerceservicesConstants.EXCEPTIONCAUSELOG + e);
				}
				catch (final Exception e)
				{
					LOG.error("Exception Occured while sending bank details to Fico  :  " + orderCode
							+ MarketplacecommerceservicesConstants.EXCEPTIONCAUSELOG + e);
				}
			}
			//for self Courier
			if (returnData.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_SELF))
			{
				LOG.debug(" returnForm>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + returnData.toString());

				final ReturnInfoData returnInfoDataObj = new ReturnInfoData();
				returnInfoDataObj.setTicketTypeCode(MarketplacecommerceservicesConstants.RETURN_TYPE);
				returnInfoDataObj.setReasonCode(returnData.getReturnReasonCode());
				returnInfoDataObj.setUssid(returnData.getUssid());
				returnInfoDataObj.setReturnMethod(returnData.getReturnMethod());
				returnInfoDataObj.setReasonCode(returnData.getReturnReasonCode());
				final boolean cancellationStatusForSelfShip = cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry,
						returnInfoDataObj, customerData, SalesApplication.MOBILE, returnAddrData);
				if (!cancellationStatusForSelfShip)
				{
					output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
				else
				{
					output.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				return output;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				output.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				output.setErrorCode(e.getErrorCode());
			}
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return output;
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			if (null != e.getMessage())
			{
				output.setError(e.getMessage());
			}
			output.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			output.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return output;
		}
		return output;
	}




	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/quickDropStores", method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public QuickDropStoresList quickDropStores(@RequestParam final String pincode, final String ussid) throws Exception
	{
		final QuickDropStoresList quickDropStores = new QuickDropStoresList();
		try
		{

			final List<PointOfServiceData> returnableStores = pincodeServiceFacade.getAllReturnableStores(pincode,
					StringUtils.substring(ussid, 0, 6));
			if (CollectionUtils.isNotEmpty(returnableStores))
			{
				quickDropStores.setReturnStoreDetailsList(returnableStores);
			}
			else
			{
				quickDropStores.setStatus(STORE_NA);
			}

		}
		catch (final Exception exception)
		{
			quickDropStores.setError(exception.getMessage());
			LOG.error("exception::::::" + exception.getMessage());

		}
		return quickDropStores;
	}

	// Getter Setter
	//Get State name R2.3 TISRLUAT-1090 start and
	private String getStateCode(final String statName)
	{
		try
		{
			for (final StateData state : accountAddressFacade.getStates())
			{
				if (state.getName().equalsIgnoreCase(statName))
				{
					return state.getCode();
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error(" UsersController Exception getting State name" + exception.getMessage());
		}
		LOG.info("State Code Not found This Name " + statName);
		return statName;
	}

	//Get State name R2.3 TISRLUAT-1090 END

	/**
	 * This method creates mRupee order
	 *
	 * @param cartId
	 * @param walletName
	 * @param cartGuid
	 * @return ThirdPartyWalletWsDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.THIRDPARTYWALLETORDER, method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public ThirdPartyWalletWsDTO createThirdPartyWalletOrder(@RequestParam final String cartId,
			@RequestParam final String walletName, @RequestParam final String cartGuid, @PathVariable final String userId,
			@RequestBody(required = false) final InventoryReservListRequestWsDTO item) throws EtailNonBusinessExceptions
	{

		List<String> orderId = new ArrayList<String>();
		CartModel cart = null;
		boolean failFlag = false;
		String failErrorCode = "";
		String refNumber = null;
		//String checksum = null;
		ThirdPartyWalletWsDTO thirdPartyWalletWsDTO = null;
		String orderData = null;
		try
		{
			final StringBuilder returnUrlBuilder = new StringBuilder(150);
			returnUrlBuilder.append(request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8)))
					.append(request.getContextPath()).append("/v2/mpl/users/").append(userId).append("/walletPayment");

			OrderModel orderModel = null;
			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}
			if (orderModel == null)
			{
				cart = mplPaymentWebFacade.findCartValues(cartId);
				//TPR-4461 COUPON FOR MRUPEE WHEN ORDER MODEL IS NULL STARTS HERE
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
						getVoucherService().getAppliedVouchers(cart));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount instanceof PromotionVoucherModel)
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;


								final String paymentModeCard = cart.getModeOfPayment();//Card Payment Mode


								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode


								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												willApply = true;
											}
											break;
										}
									}
									else
									{
										willApply = true;
									}
								}

								//if (willApply == false)
								if (!willApply)//SonarFix
								{
									thirdPartyWalletWsDTO = new ThirdPartyWalletWsDTO();
									thirdPartyWalletWsDTO.setErrorMessage(MarketplacecommerceservicesConstants.COUPONFAILUREMESSAGE);
									failFlag = true;
									failErrorCode = MarketplacecommerceservicesConstants.B9078;
								}
							}

						}
					}
				}

				//TPR-4461 COUPON FOR MRUPEE END WHEN ORDER MODEL IS NULL ENDS HERE




				final Double cartTotal = cart.getTotalPrice();
				final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();

				if (!failFlag && !mplCheckoutFacade.isPromotionValid(cart))
				{
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9075;
				}
				if (!failFlag && mplCartFacade.isCartEntryDelistedMobile(cart))
				{
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9325;
				}
				//TODO Soft reservation calls already made
				//				if (!failFlag
				//						&& !mplCartFacade.isInventoryReservedMobile(
				//								MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cart,
				//								cart.getPincodeNumber()))

				//R2.3 Techout changes

				if (!failFlag && !mplCartFacade.isInventoryReservedMobile(
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cart, cart.getPincodeNumber(),
						item, SalesApplication.MOBILE))

				{
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9047;
				}
				if (!failFlag && !getMplCheckoutFacade().isCouponValid(cart))
				{
					failErrorCode = "couponinvalid";
					failFlag = true;
					LOG.info("::setting redirect flag--4::");
				}

				if (!failFlag)
				{
					if (cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
					{
						failErrorCode = "Cart Amount Invalid";
						failFlag = true;
						LOG.info("::setting redirect flag--5::");
					}
				}
				if (!failFlag && !mplPaymentFacade.isValidCart(cart))
				{
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9064;
				}
				if (failFlag)
				{
					throw new EtailBusinessExceptions(failErrorCode);
				}
				else
				{
					//Create mRupee order
					LOG.info("::Going to Create Wallet OrderId::");
					orderId = getMplPaymentFacade().createWalletorder(cart, walletName,
							MarketplacewebservicesConstants.CHANNEL_MOBILE);

					LOG.info("::Created Wallet OrderId::" + orderId);
					if (CollectionUtils.isNotEmpty(orderId))
					{
						refNumber = orderId.get(0);
						//checksum = orderId.get(1);
					}
					final boolean isValidCart = getMplPaymentFacade().checkCart(cart);
					if (isValidCart)
					{
						mplPaymentWebFacade.entryInTPWaltAuditMobile(null, MarketplacewebservicesConstants.CHANNEL_MOBILE, cartGuid,
								refNumber);

						//						final OrderData orderData = mplCheckoutFacade.placeOrderByCartId(cartGuid);
						orderData = mplCheckoutFacade.placeOrderMobile(cart);
						if (orderData == null)
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9321);
						}
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
					}
					thirdPartyWalletWsDTO = new ThirdPartyWalletWsDTO();
					if (CollectionUtils.isNotEmpty(orderId))
					{
						thirdPartyWalletWsDTO.setOrderId(orderId.get(0));
						thirdPartyWalletWsDTO.setAmount(cartTotal.toString());
						thirdPartyWalletWsDTO.setTxnType("P");
						//getting redirect url mRupee
						thirdPartyWalletWsDTO.setMCode(getConfigurationService().getConfiguration()
								.getString(MarketplacewebservicesConstants.MRUPEE_MERCHANT_CODE));

						thirdPartyWalletWsDTO.setNarration(getConfigurationService().getConfiguration()
								.getString(MarketplacewebservicesConstants.MRUPEE_NARRATION_VALUE));
						//	thirdPartyWalletWsDTO.setNarration("uat");
						thirdPartyWalletWsDTO.setChecksum(orderId.get(1));
						thirdPartyWalletWsDTO.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
						thirdPartyWalletWsDTO.setRetUrl(returnUrlBuilder.toString());
					}
					else
					{
						LOG.debug("############## Order not created from mobile  ###############");
						thirdPartyWalletWsDTO.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
						//						thirdPartyWalletWsDTO.setErrorCode("E0005");
						//						thirdPartyWalletWsDTO.setError("ERROR");
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9710);
					}
				}
			}
			else
			{
				//TPR-4461 COUPON FOR MRUPEE WHEN ORDER MODEL IS NOT NULL STARTS HERE
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
						getVoucherService().getAppliedVouchers(orderModel));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount instanceof PromotionVoucherModel)
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;


								final String paymentModeCard = orderModel.getModeOfOrderPayment();//Card Payment Mode


								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode


								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												willApply = true;
											}
											break;
										}
									}
									else
									{
										willApply = true;
									}
								}

								//if (willApply == false)//SonarFix
								if (!willApply)
								{
									thirdPartyWalletWsDTO = new ThirdPartyWalletWsDTO();
									thirdPartyWalletWsDTO.setErrorMessage(MarketplacecommerceservicesConstants.COUPONFAILUREMESSAGE);
									failFlag = true;
									failErrorCode = MarketplacecommerceservicesConstants.B9078;
								}
							}

						}
					}
				}

				// TPR-4461 WHEN ORDER MODEL IS NOT NULL ENDS HERE FOR MRUPEE


				if (null == orderModel.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(orderModel.getStatus()))
				{

					if (!getMplCheckoutFacade().isPromotionValid(orderModel))
					{
						mplCartFacade.recalculateOrder(orderModel);
						failFlag = true;
						failErrorCode = MarketplacecommerceservicesConstants.B9075;
					}
					//Soft reservation calls already made

					//					if (!failFlag
					//							&& !mplCartFacade.isInventoryReservedMobile(
					//									MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel,
					//									orderModel.getPincodeNumber()))

					//R2.3 techout changes

					if (!failFlag && !mplCartFacade.isInventoryReservedMobile(
							MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel,
							orderModel.getPincodeNumber(), item, SalesApplication.MOBILE))
					{
						getMplCartFacade().recalculateOrder(orderModel);
						failFlag = true;
						failErrorCode = MarketplacecommerceservicesConstants.B9047;
						//notify EMAil SMS TPR-815
						mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
					}

					if (failFlag)
					{
						throw new EtailBusinessExceptions(failErrorCode);
					}
					else
					{
						orderId = getMplPaymentFacade().createWalletorder(orderModel, walletName,
								MarketplacewebservicesConstants.CHANNEL_MOBILE);

						LOG.debug("############## Order created from mobile  ###############");

						if (CollectionUtils.isNotEmpty(orderId))
						{
							refNumber = orderId.get(0);
							//checksum = orderId.get(1);
						}
						getMplPaymentFacade().entryInTPWaltAudit(null, MarketplacewebservicesConstants.CHANNEL_MOBILE, cartGuid,
								refNumber);
						LOG.info("::Created Wallet OrderId::" + orderId);

						thirdPartyWalletWsDTO = new ThirdPartyWalletWsDTO();

						if (CollectionUtils.isNotEmpty(orderId))
						{
							thirdPartyWalletWsDTO.setOrderId(orderId.get(0));
							thirdPartyWalletWsDTO.setAmount(orderModel.getTotalPrice().toString());
							thirdPartyWalletWsDTO.setTxnType("P");
							//thirdPartyWalletWsDTO.setNarration("uat");
							thirdPartyWalletWsDTO.setMCode(getConfigurationService().getConfiguration()
									.getString(MarketplacewebservicesConstants.MRUPEE_MERCHANT_CODE));

							thirdPartyWalletWsDTO.setNarration(getConfigurationService().getConfiguration()
									.getString(MarketplacewebservicesConstants.MRUPEE_NARRATION_VALUE));
							thirdPartyWalletWsDTO.setChecksum(orderId.get(1));
							thirdPartyWalletWsDTO.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
							thirdPartyWalletWsDTO.setRetUrl(returnUrlBuilder.toString());
						}
						else
						{
							thirdPartyWalletWsDTO.setStatus(MarketplacewebservicesConstants.UPDATE_FAILURE);
							//							thirdPartyWalletWsDTO.setErrorCode("E0005");
							//							thirdPartyWalletWsDTO.setError("Error");
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9710);
						}
					}
				}
				else if (null != orderModel.getPaymentInfo())
				{
					LOG.error("Order already has payment info >>>" + orderModel.getPaymentInfo().getCode());
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
				else
				{
					LOG.error("Order status is Payment_Pending for orderCode>>>" + orderModel.getCode());
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
			}
		}

		catch (final AdapterException e)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9327);
		}
		catch (final EtailBusinessExceptions e)
		{
			thirdPartyWalletWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			// Error message for All Exceptions
			if (null != e.getErrorMessage())
			{
				thirdPartyWalletWsDTO.setError(e.getErrorMessage());
				thirdPartyWalletWsDTO.setErrorCode(e.getErrorCode());
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			thirdPartyWalletWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				thirdPartyWalletWsDTO.setError(e.getErrorMessage());
				thirdPartyWalletWsDTO.setErrorCode(e.getErrorCode());
			}

		}
		catch (final Exception e)

		{
			LOG.error(MarketplacewebservicesConstants.THIRDPARTYWALLETORDER, e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				thirdPartyWalletWsDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				thirdPartyWalletWsDTO.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			thirdPartyWalletWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return thirdPartyWalletWsDTO;

	}

	/**
	 * This method will receive response from mRupee and update the order at commerce end
	 *
	 * @param amount
	 * @param mWRefCode
	 * @param paymentMode
	 * @param status
	 * @param refNo
	 * @return WalletPaymentWsDTO
	 * @throws EtailNonBusinessExceptions
	 * @throws EtailBusinessExceptions
	 * @throws CalculationException
	 */
	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.WALLETPAYMENT, method = RequestMethod.GET, produces = APPLICATION_TYPE)
	@ResponseBody
	public WalletPaymentWsDTO createWalletPayment(@RequestParam final String amount, @RequestParam final String mWRefCode,
			@RequestParam final String paymentMode, @RequestParam String status, @RequestParam final String refNo)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException
	{
		final WalletPaymentWsDTO walletPaymentWsDTO = new WalletPaymentWsDTO();
		try
		{
			String guid = null;
			OrderModel orderModel = null;
			if (StringUtils.isNotEmpty(refNo))
			{
				guid = getMplPaymentFacade().getWalletAuditEntries(refNo);
			}
			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}
			//			if (null != status && "S".equalsIgnoreCase(status) && null != orderModel && null != amount
			//					&& amount.equalsIgnoreCase(orderModel.getTotalPrice().toString())
			//					&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.MRUPEE))
			if (null != orderModel && null != orderModel.getTotalPrice())
			{
				LOG.debug("############## Order Amount###############" + orderModel.getTotalPrice().toString()
						+ "+++++++++Mrupee Amount+++++++++++" + amount);
			}
			if (null != status && "S".equalsIgnoreCase(status) && null != orderModel && null != amount
					&& paymentMode.equalsIgnoreCase(MarketplacewebservicesConstants.MRUPEE))
			{

				status = MarketplacewebservicesConstants.UPDATE_SUCCESS;

				mplPaymentWebFacade.entryInTPWaltAuditMobile(status, MarketplacewebservicesConstants.CHANNEL_MOBILE, guid, refNo);
				final double walletAmount = MarketplacewebservicesConstants.WALLETAMOUNT;
				//setting the payment modes and the amount against it in session to be used later
				final Map<String, Double> paymentInfo = new HashMap<String, Double>();
				paymentInfo.put(paymentMode, Double.valueOf(orderModel.getTotalPriceWithConv().doubleValue() - walletAmount));
				//saving TPWallet Payment related info
				mplPaymentWebFacade.saveTPWalletPaymentInfoMobile(orderModel, refNo, paymentInfo, amount);

				if (mplPaymentWebFacade.updateOrder(orderModel))
				{
					LOG.debug("############## Update order in mobile webservices WALLETPAYMENT ###############");

					walletPaymentWsDTO.setStatus(MarketplacewebservicesConstants.UPDATE_SUCCESS);
					walletPaymentWsDTO.setOrderId(orderModel.getCode());
				}
			}
			else
			{
				LOG.debug("############## order failed in mobile webservices WALLETPAYMENT ###############");
				status = MarketplacewebservicesConstants.FAIL;
				mplPaymentWebFacade.entryInTPWaltAuditMobile(status, MarketplacewebservicesConstants.CHANNEL_MOBILE,
						orderModel.getGuid(), refNo);
				walletPaymentWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9700);// please enter valid values
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			walletPaymentWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			// Error message for All Exceptions
			if (null != e.getErrorMessage())
			{
				walletPaymentWsDTO.setError(e.getErrorMessage());
				walletPaymentWsDTO.setErrorCode(e.getErrorCode());
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			walletPaymentWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				walletPaymentWsDTO.setError(e.getErrorMessage());
				walletPaymentWsDTO.setErrorCode(e.getErrorCode());
			}
		}
		catch (final Exception e)
		{
			LOG.error("MRUPEE ORDER", e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				walletPaymentWsDTO.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				walletPaymentWsDTO.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			walletPaymentWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return walletPaymentWsDTO;
	}


	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.APPLY_CLIQCASH, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public PayCliqCashWsDto applyCliqCash(@RequestParam final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException
	{
		LOG.info("Applying  cliq Cash ");
		final PayCliqCashWsDto payCliqCashWsDto = new PayCliqCashWsDto();
		final OrderModel orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
		CartModel cart = null;

		try
		{
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
				CustomerWalletDetailResponse responce=null;
				if (null != currentCustomer && null != currentCustomer.getCustomerWalletDetail()
						&& null != currentCustomer.getCustomerWalletDetail().getWalletId())
				{
					 responce = mplWalletFacade
							.getCustomerWallet(currentCustomer.getCustomerWalletDetail().getWalletId().trim());
				}
				payCliqCashWsDto.setDiscount(Double.valueOf(0));
				payCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
				if (null != responce && responce.getResponseCode() == Integer.valueOf(0)
						&& null != responce.getWallet() && null != responce.getWallet().getBalance())
				{

					final Double WalletAmt = responce.getWallet().getBalance();
					LOG.debug("Bucket Balance =" + WalletAmt);
					final Double totalAmt = cart.getTotalPrice();

					if (null != WalletAmt && null != totalAmt && WalletAmt.doubleValue() >= totalAmt.doubleValue())
					{
						cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
						cart.setPayableWalletAmount(totalAmt);
						getModelService().save(cart);
						getModelService().refresh(cart);
						payCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
						payCliqCashWsDto.setIsRemainingAmount(false);
						payCliqCashWsDto.setCliqCashApplied(totalAmt);
						payCliqCashWsDto.setPaybleAmount(Double.valueOf(0));
						payCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
						payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						
					}
					else
					{
						double juspayTotalAmt = 0.0D;
						if (null != totalAmt && totalAmt.doubleValue() > 0.0D && null != WalletAmt && WalletAmt.doubleValue() > 0.0D)
						{
							juspayTotalAmt = totalAmt.doubleValue() - WalletAmt.doubleValue();
						}
						else if (null != cart.getTotalPrice() && cart.getTotalPrice().doubleValue() > 0.0D)
						{
							juspayTotalAmt = cart.getTotalPrice().doubleValue();
						}
						payCliqCashWsDto.setIsRemainingAmount(true);
						if (null != WalletAmt && WalletAmt.doubleValue() > 0.0D)
						{
							payCliqCashWsDto.setCliqCashApplied(WalletAmt);
						}
						payCliqCashWsDto.setIsRemainingAmount(true);
						payCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
						payCliqCashWsDto.setPaybleAmount(Double.valueOf(juspayTotalAmt));
						payCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
						cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
						cart.setPayableWalletAmount(WalletAmt);
						getModelService().save(cart);
						getModelService().refresh(cart);
						payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}

				}else {
					payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				}
			}
			else
			{

				final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();

				CustomerWalletDetailResponse responce=null;
				if (null != currentCustomer && null != currentCustomer.getCustomerWalletDetail()
						&& null != currentCustomer.getCustomerWalletDetail().getWalletId())
				{
					 responce = mplWalletFacade
							.getCustomerWallet(currentCustomer.getCustomerWalletDetail().getWalletId().trim());
				}
				payCliqCashWsDto.setDiscount(Double.valueOf(0));
				payCliqCashWsDto.setTotalAmount(orderModel.getTotalPrice().toString());
				if (null != responce && responce.getResponseCode() == Integer.valueOf(0)
						&& null != responce.getWallet() && null != responce.getWallet().getBalance())
				{

					final Double WalletAmt = responce.getWallet().getBalance();
					final Double totalAmt = orderModel.getTotalPrice();

					if (null != WalletAmt && null != totalAmt && WalletAmt.doubleValue() >= totalAmt.doubleValue())
					{
						orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
						orderModel.setPayableWalletAmount(totalAmt);
						getModelService().save(orderModel);
						getModelService().refresh(orderModel);
						payCliqCashWsDto.setDiscount(orderModel.getTotalDiscounts());
						payCliqCashWsDto.setIsRemainingAmount(false);
						payCliqCashWsDto.setCliqCashApplied(totalAmt);
						payCliqCashWsDto.setPaybleAmount(Double.valueOf(0));
						payCliqCashWsDto.setTotalAmount(orderModel.getTotalPrice().toString());
						payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}
					else
					{
						double juspayTotalAmt = 0.0D;
						if (null != totalAmt && totalAmt.doubleValue() > 0.0D && null != WalletAmt && WalletAmt.doubleValue() > 0.0D)
						{
							juspayTotalAmt = totalAmt.doubleValue() - WalletAmt.doubleValue();
						}
						else if (null != orderModel.getTotalPrice() && orderModel.getTotalPrice().doubleValue() > 0.0D)
						{
							juspayTotalAmt = orderModel.getTotalPrice().doubleValue();
						}
						payCliqCashWsDto.setIsRemainingAmount(true);
						if (null != WalletAmt && WalletAmt.doubleValue() > 0.0D)
						{
							payCliqCashWsDto.setCliqCashApplied(WalletAmt);
						}

						payCliqCashWsDto.setDiscount(orderModel.getTotalDiscounts());
						payCliqCashWsDto.setPaybleAmount(Double.valueOf(juspayTotalAmt));
						payCliqCashWsDto.setTotalAmount(orderModel.getTotalPrice().toString());
						orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
						orderModel.setPayableWalletAmount(WalletAmt);
						getModelService().save(orderModel);
						getModelService().refresh(orderModel);
						payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					}

				}else {
					payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				}
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			payCliqCashWsDto.setError(ex.getErrorCode());
			payCliqCashWsDto.setErrorCode(ex.getErrorCode());
			payCliqCashWsDto.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while applying Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			payCliqCashWsDto.setError(ex.getMessage());
			LOG.error("Exception occrred while applying Cliq cash" + ex.getMessage());
		}
		return payCliqCashWsDto;

	}



	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REMOVE_CLIQCASH, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public PayCliqCashWsDto removeCliqCash(@RequestParam final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException
	{
		LOG.info("Removing cliq Cash ");
		final PayCliqCashWsDto payCliqCashWsDto = new PayCliqCashWsDto();
		final OrderModel orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
		CartModel cart = null;
		try
		{
			if (null != orderModel)
			{
				payCliqCashWsDto.setDiscount(orderModel.getTotalDiscounts());
				if (null != orderModel.getTotalPrice())
				{
					payCliqCashWsDto.setTotalAmount(orderModel.getTotalPrice().toString());
				}
				payCliqCashWsDto.setPaybleAmount(orderModel.getTotalPrice());
				orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
				orderModel.setPayableWalletAmount(Double.valueOf(0.0D));
				getModelService().save(orderModel);
				getModelService().refresh(orderModel);
				payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				payCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
				if (null != cart.getTotalPrice())
				{
					payCliqCashWsDto.setPaybleAmount(cart.getTotalPrice());
				}
				cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
				cart.setPayableWalletAmount(Double.valueOf(0.0D));
				getModelService().save(cart);
				getModelService().refresh(cart);
				payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			payCliqCashWsDto.setErrorCode(ex.getErrorCode());
			payCliqCashWsDto.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			payCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			payCliqCashWsDto.setError(ex.getMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		return payCliqCashWsDto;

	}








	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REDEEM_CLIQ_VOUCHER, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public RedeemCliqVoucherWsDTO redeemCliqVoucher(@RequestParam final String couponCode, @RequestParam final String passKey)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		LOG.info("Redeeming CLiq Cash Voucher");
		boolean customerRegisteredwithQc = false;
		final RedeemCliqVoucherWsDTO redeemCliqVoucherWsDTO = new RedeemCliqVoucherWsDTO();
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			if (null != currentCustomer
					&& (null == currentCustomer.getIsWalletActivated() || !currentCustomer.getIsWalletActivated().booleanValue()))
			{
				LOG.debug("Customer Is not Regitered with QC .. Registering with email " + currentCustomer.getOriginalUid());
				final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
				final Customer custInfo = new Customer();
				custInfo.setEmail(currentCustomer.getOriginalUid());
				custInfo.setEmployeeID(currentCustomer.getUid());
				custInfo.setCorporateName("Tata Unistore Ltd");

				if (null != currentCustomer.getFirstName())
				{
					custInfo.setFirstname(currentCustomer.getFirstName());
				}
				if (null != currentCustomer.getLastName())
				{
					custInfo.setLastName(currentCustomer.getLastName());
				}

				customerRegisterReq.setExternalwalletid(currentCustomer.getOriginalUid());
				customerRegisterReq.setCustomer(custInfo);
				customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getOriginalUid());
				final QCCustomerRegisterResponse customerRegisterResponse = mplWalletFacade
						.createWalletContainer(customerRegisterReq);
				if (null != customerRegisterResponse && null != customerRegisterResponse.getResponseCode()
						&& customerRegisterResponse.getResponseCode().intValue() == 0)
				{
					final CustomerWalletDetailModel custWalletDetail = modelService.create(CustomerWalletDetailModel.class);
					custWalletDetail.setWalletId(customerRegisterResponse.getWallet().getWalletNumber());
					custWalletDetail.setWalletState(customerRegisterResponse.getWallet().getStatus());
					custWalletDetail.setCustomer(currentCustomer);
					custWalletDetail.setServiceProvider("Tata Unistore Ltd");
					modelService.save(custWalletDetail);
					currentCustomer.setCustomerWalletDetail(custWalletDetail);
					currentCustomer.setIsWalletActivated(Boolean.TRUE);
					modelService.save(currentCustomer);
					customerRegisteredwithQc = true;
				}
			}else {
				customerRegisteredwithQc = true;
			}
			if (customerRegisteredwithQc)
			{
				final RedimGiftCardResponse response = mplWalletFacade.getAddEGVToWallet(couponCode, passKey);
				if (null != response && null != response.getResponseCode() && null == Integer.valueOf(0))
				{
					final TotalCliqCashBalanceWsDto totalCliqCashBalance = new TotalCliqCashBalanceWsDto();
					if (null != response.getWallet() && null != response.getWallet().getBalance())
					{
						final BigDecimal walletAmount = new BigDecimal(response.getWallet().getBalance().doubleValue());
						final CurrencyModel currency = commonI18NService.getCurrency(MarketplacecommerceservicesConstants.INR);
						final long valueLong = walletAmount.setScale(0, BigDecimal.ROUND_FLOOR).longValue();
						final String totalPriceNoDecimalPntFormatted = Long.toString(valueLong);
						StringBuilder stbND = new StringBuilder(20);
						if (null != currency && null != currency.getSymbol())
						{
							stbND = stbND.append(currency.getSymbol()).append(totalPriceNoDecimalPntFormatted);
						}
						redeemCliqVoucherWsDTO.setVoucherValue(stbND.toString());
						final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, walletAmount,
								MarketplacecommerceservicesConstants.INR);
						if (null != priceData)
						{
							totalCliqCashBalance.setCurrencyIso(priceData.getCurrencyIso());
							totalCliqCashBalance.setDoubleValue(priceData.getDoubleValue());
							totalCliqCashBalance.setFormattedValue(priceData.getFormattedValue());
							totalCliqCashBalance.setPriceType(priceData.getPriceType());
							totalCliqCashBalance.setFormattedValueNoDecimal(priceData.getFormattedValueNoDecimal());
							totalCliqCashBalance.setValue(priceData.getValue());
							redeemCliqVoucherWsDTO.setTotalCliqCashBalance(totalCliqCashBalance);
							redeemCliqVoucherWsDTO.setIsWalletLimitReached(false);
							redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						}
						else
						{
							redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
						}
					}
					else
					{
						redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
					}
				}
			}
			else
			{
				redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			redeemCliqVoucherWsDTO.setErrorCode(ex.getErrorCode());
			redeemCliqVoucherWsDTO.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			redeemCliqVoucherWsDTO.setError(ex.getMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		return redeemCliqVoucherWsDTO;
	}


	@Secured(
			{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
			@RequestMapping(value = MarketplacewebservicesConstants.CREATE_ELECTRONICS_GIFTCARD_AMOUNT, method = RequestMethod.POST, produces = APPLICATION_TYPE)
			@ResponseBody
			public BuyingEgvResponceWsDTO calculateGiftCardAmount(@RequestBody final BuyingEgvRequestWsDTO buyingEgvRequest)
					throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

			{
				LOG.info("Calculating Electronics Gift Cart Amount");
				final BuyingEgvResponceWsDTO buyingEgvResponce = new BuyingEgvResponceWsDTO();
				int quantity =1;
				double amountUserSelectedPerQty = 0.0D;
				double paybleAmount = 0.0D;
				try
				{
					if (null != buyingEgvRequest)
					{
						if (buyingEgvRequest.getQuantity() > 0)
						{
							quantity = buyingEgvRequest.getQuantity();
						}
						if (null != buyingEgvRequest.getPriceSelectedByUserPerQuantity() &&
								buyingEgvRequest.getPriceSelectedByUserPerQuantity().doubleValue() >0.0D)
						{
							amountUserSelectedPerQty = buyingEgvRequest.getPriceSelectedByUserPerQuantity().doubleValue();
							LOG.debug("amountUserSelectedPerQty  :"+amountUserSelectedPerQty);
						}
						if (quantity > 0 && amountUserSelectedPerQty > 0.0D)
						{
							paybleAmount = quantity*amountUserSelectedPerQty;
						}
						if(paybleAmount > 0.0D) {
							LOG.debug("Toatal Payable Amount :"+paybleAmount);
							buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							buyingEgvResponce.setPaybleAmount(Double.valueOf(paybleAmount));
							buyingEgvResponce.setTotalPrice(Double.valueOf(paybleAmount));
							buyingEgvResponce.setDiscounts(Double.valueOf(0.0D));
						}else {
							buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
						}
					}
					else
					{
						buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
					}
				}
		catch (final EtailNonBusinessExceptions ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setErrorCode(ex.getErrorCode());
			buyingEgvResponce.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while Calculating Electronics Gift Card Amount " + ex.getMessage());
		}
		catch (final Exception ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setError(ex.getMessage());
			LOG.error("Exception occrred Calculating Electronics Gift Card Amount " + ex.getMessage());
		}

		return buyingEgvResponce;
	}

	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.CREATE_ELECTRONICS_GIFTCARD_GUID, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public BuyingEgvResponceWsDTO createGiftCardGuid(@RequestBody final BuyingEgvRequestWsDTO buyingEgvRequest)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		LOG.info("Creating  Electronics Gift Card Guid");
		final BuyingEgvResponceWsDTO buyingEgvResponce = new BuyingEgvResponceWsDTO();
		CartData giftCartData = null;
		try
		{
			 EgvDetailsData egvDetailsData=null;
			if(null != buyingEgvRequest){
				egvDetailsData = populateEGVFormToData(buyingEgvRequest);
			}
			if (null != egvDetailsData)
			{
				giftCartData = mplCartFacade.getGiftCartModel(egvDetailsData);
			}
			if (null != giftCartData)
			{
				if (null != giftCartData.getTotalPrice())
				{
					buyingEgvResponce.setTotalPrice(giftCartData.getTotalPrice().getDoubleValue());
					buyingEgvResponce.setPaybleAmount(giftCartData.getTotalPrice().getDoubleValue());
				}
				if (null != giftCartData.getTotalDiscounts())
				{
					buyingEgvResponce.setDiscounts(giftCartData.getTotalDiscounts().getDoubleValue());
				}
				if (null != giftCartData.getGuid())
				{
					buyingEgvResponce.setEgvCartGuid(giftCartData.getGuid());
				}
				buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setErrorCode(ex.getErrorCode());
			buyingEgvResponce.setError(ex.getErrorMessage());
			LOG.error("Exception occrred Creating  Electronics Gift Card Guid" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			buyingEgvResponce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			buyingEgvResponce.setError(ex.getMessage());
			LOG.error("Exception occrred Creating  Electronics Gift Card Guid" + ex.getMessage());
		}

		return buyingEgvResponce;
	}


	
	@Secured(
			{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
			@RequestMapping(value = MarketplacewebservicesConstants.RESEND_NOTIFICATION_EGV, method = RequestMethod.POST, produces = APPLICATION_TYPE)
			@ResponseBody
			public ResendEGVNotificationWsDTO resendNotificationEgv(@RequestParam final String orderId)
					throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		ResendEGVNotificationWsDTO responce = new ResendEGVNotificationWsDTO();
		try
		{

			if (orderId != null)
			{
				LOG.info("SendNotificationRecipient  ");
				mplOrderFacade.sendNotificationEGVOrder(orderId);
				responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
			}
		}
		catch (final Exception ex)
		{
			responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			responce.setError(ex.getMessage());
			LOG.error("Exception occrred Creating  Electronics Gift Card Guid" + ex.getMessage());
		}
		return responce;
	}
	/**
	 * @param s
	 * @return
	 */
	private EgvDetailsData populateEGVFormToData(final BuyingEgvRequestWsDTO requestData)
	{
		final EgvDetailsData egvDetailsData = new EgvDetailsData();
		if (null != requestData)
		{
			final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
			egvDetailsData.setProductCode(requestData.getProductID());
			if (null != requestData.getPriceSelectedByUserPerQuantity()
					&& requestData.getPriceSelectedByUserPerQuantity().doubleValue() > 0.0D)
			{
				egvDetailsData.setGiftRange(requestData.getPriceSelectedByUserPerQuantity().doubleValue());
			}
			if (null != requestData.getPriceSelectedByUserPerQuantity()
					&& requestData.getPriceSelectedByUserPerQuantity().doubleValue() > 0.0D)
			{
				egvDetailsData.setOpenTextAmount(requestData.getPriceSelectedByUserPerQuantity().doubleValue());
			}
			if (null != requestData.getReceiverEmailID())
			{
				egvDetailsData.setToEmailAddress(requestData.getReceiverEmailID());
			}
			if (null != currentCustomer && null != currentCustomer.getOriginalUid())
			{
				egvDetailsData.setFromEmailAddress(currentCustomer.getOriginalUid());
			}
			if (null != requestData.getPriceSelectedByUserPerQuantity())
			{
				egvDetailsData.setGiftRange(requestData.getPriceSelectedByUserPerQuantity().doubleValue());
			}

			//			if(null != requestData.getMessageOnCard()) {
			//				egvDetailsData.setMessageBox(egvDetailForm.getMessageBox());
			//			}

			egvDetailsData.setTotalEGV(requestData.getQuantity());
		}

		return egvDetailsData;
	}

	/**
	 * @param item
	 * @param pincode
	 *
	 */
	private OrderCreateInJusPayWsDto patAmountUsingQC(final String userId, final String cartGuid, final String pincode,
			final InventoryReservListRequestWsDTO item)
	{
		LOG.info("Paying  Full amount through QC for GUID" + cartGuid);
		final OrderCreateInJusPayWsDto orderCreateInJusPayWsDto = new OrderCreateInJusPayWsDto();
		String uid = "";
		String failErrorCode = "";
		boolean failFlag = false;
		OrderModel orderModel = null;
		CustomerModel customer = null;
		CartModel cart = null;
		String orderCode = null;

		//		double payableWalletAmount = 0.0D;
		//		double payableJuspayAmount = 0.0D;
		//		double totalWalletAmount = 0.0D;

		QCRedeeptionResponse qcResponse = new QCRedeeptionResponse();

		final BalanceBucketWise balBucketwise = null;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("********* Creating QC  Order mobile web service");
		}
		try
		{
			customer = extendedUserService.getUserForOriginalUid(userId);
			if (null != customer)
			{
				uid = customer.getUid();
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0006);
			}
			// For Mobile

			//Payment Soln changes
			orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			//If cart is present
			if (orderModel == null)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				if (null != cart)
				{
					//TPR-4461 STARTS HERE WHEN ORDER MODEL IS NULL
					final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
							getVoucherService().getAppliedVouchers(cart));

					if (CollectionUtils.isNotEmpty(voucherList))
					{
						VoucherModel appliedVoucher = null;

						final DiscountModel discount = voucherList.get(0);

						if (discount instanceof PromotionVoucherModel)
						{
							final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
							appliedVoucher = promotionVoucherModel;

							final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
							for (final RestrictionModel restriction : restrictions)
							{
								if (restriction instanceof PaymentModeRestrictionModel)
								{
									boolean willApply = false;


									final String paymentModeCard = cart.getModeOfPayment();//Customer's selected Payment Mode


									final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
											.getPaymentTypeData(); //Voucher Payment mode


									if (CollectionUtils.isNotEmpty(paymentTypeList))
									{
										if (StringUtils.isNotEmpty(paymentModeCard))
										{
											for (final PaymentTypeModel paymentType : paymentTypeList)
											{
												if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
												{
													willApply = true;
													break;
												}

											}
										}
										else
										{
											willApply = true;
										}
									}

								}

							}
						}
					}

					//TPR-4461 ENDS HERE WHEN ORDER MODEL IS NULL
					if (!failFlag && !mplCheckoutFacade.isPromotionValid(cart))
					{
						failFlag = true;
						failErrorCode = MarketplacecommerceservicesConstants.B9075;
					}
				}
				if (!failFlag && mplCartFacade.isCartEntryDelistedMobile(cart))
				{
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9325;
				}
				//TISUTO-12 , TISUTO-11
				//TODO Soft reservation calls already made


				if (!failFlag && !mplCartFacade.isInventoryReservedMobile(
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cart, pincode, item,
						SalesApplication.MOBILE))
				{
					//getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID,"TRUE");
					//getMplCartFacade().recalculate(cart);
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9047;
				}

				if (!failFlag)
				{
					final Double cartTotal = cart.getTotalPrice();
					final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();
					if (cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
					{
						final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
						final String qcUniqueCode = mplPaymentFacade.generateQCCode();
						qcResponse = mplPaymentFacade.createQCOrderRequest(cart.getGuid(), cart,
								currentCustomer.getCustomerWalletDetail().getWalletId(),
								MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH, qcUniqueCode,
								MarketplacewebservicesConstants.CHANNEL_MOBILE, cart.getTotalPrice().doubleValue(), 0.0D);
						boolean egvStatus;
						if (null != qcResponse && null != qcResponse.getResponseCode() && qcResponse.getResponseCode().intValue() != 0)
						{
							cart.setStatus(OrderStatus.PAYMENT_FAILED); /// return QC fail and Update Audit Entry Try With Juspay
							modelService.save(cart);
							egvStatus = false;
						}

						else if (null == qcResponse || null == qcResponse.getResponseCode())
						{
							cart.setStatus(OrderStatus.PAYMENT_FAILED); /// NO Exception No qcResponse Try With Juspay
							modelService.save(cart);
							egvStatus = false;
						}
						orderCreateInJusPayWsDto.setCliqcashAmount(cart.getTotalPrice());
						orderCreateInJusPayWsDto.setCliqcashSelected(true);

						failFlag = true;
						failErrorCode = MarketplacecommerceservicesConstants.B9509;
						//}
					}
				}
				//TISPRO-578
				if (!failFlag && !mplPaymentFacade.isValidCart(cart))
				{
					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID, "TRUE");
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9064;
				}

				if (failFlag)
				{
					throw new EtailBusinessExceptions(failErrorCode);
				}
				else
				{
					final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
					final String qcUniqueCode = mplPaymentFacade.generateQCCode();
					qcResponse = mplPaymentFacade.createQCOrderRequest(cart.getGuid(), cart,
							currentCustomer.getCustomerWalletDetail().getWalletId(),
							MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH, qcUniqueCode,
							MarketplacewebservicesConstants.CHANNEL_MOBILE, cart.getTotalPrice().doubleValue(), 0.0D);
					boolean egvStatus;
					if (null != qcResponse && null != qcResponse.getResponseCode() && qcResponse.getResponseCode().intValue() != 0)
					{
						cart.setStatus(OrderStatus.PAYMENT_FAILED); /// return QC fail and Update Audit Entry Try With Juspay
						modelService.save(cart);
						egvStatus = false;
					}

					else if (null == qcResponse || null == qcResponse.getResponseCode())
					{
						cart.setStatus(OrderStatus.PAYMENT_FAILED); /// NO Exception No qcResponse Try With Juspay
						modelService.save(cart);
						egvStatus = false;
					}
					orderCreateInJusPayWsDto.setCliqcashAmount(cart.getTotalPrice());
					orderCreateInJusPayWsDto.setCliqcashSelected(true);
					//create order here
					//Mandatory checks agains cart
					final boolean isValidCart = getMplPaymentFacade().checkCart(cart);

					if (isValidCart)
					{
						//CAR-110
						orderCode = mplCheckoutFacade.placeOrderMobile(cart);
						if (orderCode == null)
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9321);
						}

					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
					}
				}
			}
			else
			{
				//TPR-4461 STARTS HERE WHEN ORDER MODEL IS NOT NULL
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(
						getVoucherService().getAppliedVouchers(orderModel));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount instanceof PromotionVoucherModel)
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;
						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;
								final String paymentModeCard = orderModel.getModeOfOrderPayment();//Customer's selected Payment Mode
								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode
								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												willApply = true;
												break;
											}

										}
									}
									else
									{
										willApply = true;
									}
								}
							}
						}
					}
				}
				if (!getMplCheckoutFacade().isPromotionValid(orderModel))
				{
					mplCartFacade.recalculateOrder(orderModel);
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9075;
				}

				if (!failFlag && !mplCartFacade.isInventoryReservedMobile(
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel, pincode, item,
						SalesApplication.MOBILE))
				{
					//getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID,"TRUE");
					getMplCartFacade().recalculateOrder(orderModel);
					failFlag = true;
					failErrorCode = MarketplacecommerceservicesConstants.B9047;
					//notify EMAil SMS TPR-815
					mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
				}

				if (failFlag)
				{
					throw new EtailBusinessExceptions(failErrorCode);
				}
				else
				{
					final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
					final String qcUniqueCode = mplPaymentFacade.generateQCCode();
					qcResponse = mplPaymentFacade.createQCOrderRequest(orderModel.getGuid(), orderModel,
							currentCustomer.getCustomerWalletDetail().getWalletId(),
							MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH, qcUniqueCode,
							MarketplacewebservicesConstants.CHANNEL_MOBILE, orderModel.getTotalPrice().doubleValue(), 0.0D);
					boolean egvStatus;
					if (null != qcResponse && null != qcResponse.getResponseCode() && qcResponse.getResponseCode().intValue() != 0)
					{
						orderModel.setStatus(OrderStatus.PAYMENT_FAILED); /// return QC fail and Update Audit Entry Try With Juspay
						modelService.save(orderModel);
						egvStatus = false;
					}
					else if (null == qcResponse || null == qcResponse.getResponseCode())
					{
						orderModel.setStatus(OrderStatus.PAYMENT_FAILED); /// NO Exception No qcResponse Try With Juspay
						modelService.save(orderModel);
						egvStatus = false;
					}
					orderCreateInJusPayWsDto.setCliqcashAmount(orderModel.getTotalPrice());
					orderCreateInJusPayWsDto.setCliqcashSelected(true);
				}
			}
			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderCreateInJusPayWsDto.setCartGuid(cartGuid);
			}
			if (StringUtils.isNotEmpty(orderCode))
			{
				orderCreateInJusPayWsDto.setOrderId(orderCode);
			}

			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);

		}
		catch (final AdapterException e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			if (null != e.getMessage())
			{
				orderCreateInJusPayWsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9327));
				orderCreateInJusPayWsDto.setErrorCode(MarketplacecommerceservicesConstants.B9327);
			}
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			if (null != e.getErrorMessage())
			{
				orderCreateInJusPayWsDto.setError(e.getErrorMessage());
				orderCreateInJusPayWsDto.setErrorCode(e.getErrorCode());
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			// Error message for All Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				orderCreateInJusPayWsDto.setError(e.getErrorMessage());
				orderCreateInJusPayWsDto.setErrorCode(e.getErrorCode());
			}

		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			// Error message for All Exceptions
			if (null != e.getMessage())
			{
				orderCreateInJusPayWsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				orderCreateInJusPayWsDto.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return orderCreateInJusPayWsDto;
	}

	/**
	 * @return the mplProductWebService
	 */
	public MplProductWebServiceImpl getMplProductWebService()
	{
		return mplProductWebService;
	}

	/**
	 * @param mplProductWebService
	 *           the mplProductWebService to set
	 */
	public void setMplProductWebService(final MplProductWebServiceImpl mplProductWebService)
	{
		this.mplProductWebService = mplProductWebService;
	}

	/**
	 * @return the mplCartWebService
	 */
	public MplCartWebService getMplCartWebService()
	{
		return mplCartWebService;
	}

	/**
	 * @param mplCartWebService
	 *           the mplCartWebService to set
	 */
	public void setMplCartWebService(final MplCartWebService mplCartWebService)
	{
		this.mplCartWebService = mplCartWebService;
	}

	/**
	 * @return the mplPaymentService
	 */
	public MplPaymentService getMplPaymentService()
	{
		return mplPaymentService;
	}

	/**
	 * @param mplPaymentService
	 *           the mplPaymentService to set
	 */
	public void setMplPaymentService(final MplPaymentService mplPaymentService)
	{
		this.mplPaymentService = mplPaymentService;
	}

	@Autowired
	private ExtendedUserServiceImpl userexService;

	@Resource(name = "oauthTokenService")
	private OAuthTokenService oauthTokenService;

	/**
	 * @return the oauthTokenService
	 */
	public OAuthTokenService getOauthTokenService()
	{
		return oauthTokenService;
	}

	/**
	 * @param oauthTokenService
	 *           the oauthTokenService to set
	 */
	public void setOauthTokenService(final OAuthTokenService oauthTokenService)
	{
		this.oauthTokenService = oauthTokenService;
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

	@Autowired
	private MplCustomerProfileFacade mplCustomerProfileFacade;

	protected CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	{
		return checkoutCustomerStrategy;
	}

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	public ModelService getModelService()
	{
		return modelService;
	}

	public FriendsInviteService getFriendsInviteService()
	{
		return friendsInviteService;
	}

	protected I18NFacade getI18NFacade()
	{
		return i18NFacade;
	}

	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @return the configurationService
	 */

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	//	/**
	//	 * @return the extendedUserService
	//	 */
	//	public ExtendedUserService getExtendedUserService()
	//	{
	//		return extendedUserService;
	//	}
	//
	//	/**
	//	 * @param extendedUserService
	//	 *           the extendedUserService to set
	//	 */
	//	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	//	{
	//		this.extendedUserService = extendedUserService;
	//	}

	/**
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	/**
	 * @return the customerFacade
	 */
	public CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	/**
	 * @param customerFacade
	 *           the customerFacade to set
	 */
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	/**
	 * @return the userFacade
	 */
	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	/**
	 * @param userFacade
	 *           the userFacade to set
	 */
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	/**
	 * @return the customerGroupFacade
	 */
	public CustomerGroupFacade getCustomerGroupFacade()
	{
		return customerGroupFacade;
	}

	/**
	 * @param customerGroupFacade
	 *           the customerGroupFacade to set
	 */
	public void setCustomerGroupFacade(final CustomerGroupFacade customerGroupFacade)
	{
		this.customerGroupFacade = customerGroupFacade;
	}

	/**
	 * @return the addressVerificationFacade
	 */
	public AddressVerificationFacade getAddressVerificationFacade()
	{
		return addressVerificationFacade;
	}

	/**
	 * @param addressVerificationFacade
	 *           the addressVerificationFacade to set
	 */
	public void setAddressVerificationFacade(final AddressVerificationFacade addressVerificationFacade)
	{
		this.addressVerificationFacade = addressVerificationFacade;
	}

	/**
	 * @return the httpRequestCustomerDataPopulator
	 */
	public HttpRequestCustomerDataPopulator getHttpRequestCustomerDataPopulator()
	{
		return httpRequestCustomerDataPopulator;
	}

	/**
	 * @param httpRequestCustomerDataPopulator
	 *           the httpRequestCustomerDataPopulator to set
	 */
	public void setHttpRequestCustomerDataPopulator(final HttpRequestCustomerDataPopulator httpRequestCustomerDataPopulator)
	{
		this.httpRequestCustomerDataPopulator = httpRequestCustomerDataPopulator;
	}

	/**
	 * @return the httpRequestAddressDataPopulator
	 */
	public Populator<HttpServletRequest, AddressData> getHttpRequestAddressDataPopulator()
	{
		return httpRequestAddressDataPopulator;
	}

	/**
	 * @param httpRequestAddressDataPopulator
	 *           the httpRequestAddressDataPopulator to set
	 */
	public void setHttpRequestAddressDataPopulator(
			final Populator<HttpServletRequest, AddressData> httpRequestAddressDataPopulator)

	{
		this.httpRequestAddressDataPopulator = httpRequestAddressDataPopulator;
	}

	/**
	 * @return the addressValidator
	 */
	public Validator getAddressValidator()
	{
		return addressValidator;
	}

	/**
	 * @param addressValidator
	 *           the addressValidator to set
	 */
	public void setAddressValidator(final Validator addressValidator)
	{
		this.addressValidator = addressValidator;
	}

	/**
	 * @return the addressDataErrorsPopulator
	 */
	public Populator<AddressVerificationResult<AddressVerificationDecision>, Errors> getAddressDataErrorsPopulator()
	{
		return addressDataErrorsPopulator;
	}

	/**
	 * @param addressDataErrorsPopulator
	 *           the addressDataErrorsPopulator to set
	 */
	public void setAddressDataErrorsPopulator(
			final Populator<AddressVerificationResult<AddressVerificationDecision>, Errors> addressDataErrorsPopulator)
	{
		this.addressDataErrorsPopulator = addressDataErrorsPopulator;
	}

	/**
	 * @return the validationErrorConverter
	 */
	public Converter<Object, List<ErrorWsDTO>> getValidationErrorConverter()
	{
		return validationErrorConverter;
	}

	/**
	 * @param validationErrorConverter
	 *           the validationErrorConverter to set
	 */
	public void setValidationErrorConverter(final Converter<Object, List<ErrorWsDTO>> validationErrorConverter)
	{
		this.validationErrorConverter = validationErrorConverter;
	}

	/**
	 * @return the ccPaymentInfoValidator
	 */
	public Validator getCcPaymentInfoValidator()
	{
		return ccPaymentInfoValidator;
	}

	/**
	 * @param ccPaymentInfoValidator
	 *           the ccPaymentInfoValidator to set
	 */
	public void setCcPaymentInfoValidator(final Validator ccPaymentInfoValidator)
	{
		this.ccPaymentInfoValidator = ccPaymentInfoValidator;
	}

	/**
	 * @return the orderFacade
	 */
	public OrderFacade getOrderFacade()
	{
		return orderFacade;
	}

	/**
	 * @param orderFacade
	 *           the orderFacade to set
	 */
	public void setOrderFacade(final OrderFacade orderFacade)
	{
		this.orderFacade = orderFacade;
	}

	/**
	 * @return the putUserDTOValidator
	 */
	public Validator getPutUserDTOValidator()
	{
		return putUserDTOValidator;
	}

	/**
	 * @param putUserDTOValidator
	 *           the putUserDTOValidator to set
	 */
	public void setPutUserDTOValidator(final Validator putUserDTOValidator)
	{
		this.putUserDTOValidator = putUserDTOValidator;
	}

	/**
	 * @return the passwordStrengthValidator
	 */
	public Validator getPasswordStrengthValidator()
	{
		return passwordStrengthValidator;
	}

	/**
	 * @param passwordStrengthValidator
	 *           the passwordStrengthValidator to set
	 */
	public void setPasswordStrengthValidator(final Validator passwordStrengthValidator)
	{
		this.passwordStrengthValidator = passwordStrengthValidator;
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

	/**
	 * @return the addressReversePopulator
	 */
	public Populator<AddressData, AddressModel> getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	/**
	 * @param addressReversePopulator
	 *           the addressReversePopulator to set
	 */
	public void setAddressReversePopulator(final CustomAddressReversePopulator addressReversePopulator)
	{
		this.addressReversePopulator = addressReversePopulator;
	}

	/**
	 * @return the wishlistService
	 */
	public Wishlist2Service getWishlistService()
	{
		return wishlistService;
	}

	/**
	 * @param wishlistService
	 *           the wishlistService to set
	 */
	public void setWishlistService(final Wishlist2Service wishlistService)
	{
		this.wishlistService = wishlistService;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @return the messages
	 */
	public MessageSourceAccessor getMessages()
	{
		return messages;
	}

	/**
	 * @param messages
	 *           the messages to set
	 */
	public void setMessages(final MessageSourceAccessor messages)
	{
		this.messages = messages;
	}

	/**
	 * @return the mplPreferenceFacade
	 */
	public MplPreferenceFacade getMplPreferenceFacade()
	{
		return mplPreferenceFacade;
	}

	/**
	 * @param mplPreferenceFacade
	 *           the mplPreferenceFacade to set
	 */
	public void setMplPreferenceFacade(final MplPreferenceFacade mplPreferenceFacade)
	{
		this.mplPreferenceFacade = mplPreferenceFacade;
	}

	/**
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}

	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
	}

	/**
	 * @return the mobileUserService
	 */
	public MplMobileUserService getMobileUserService()
	{
		return mobileUserService;
	}

	/**
	 * @param mobileUserService
	 *           the mobileUserService to set
	 */
	public void setMobileUserService(final MplMobileUserService mobileUserService)
	{
		this.mobileUserService = mobileUserService;
	}

	/**
	 * @return the mplCustomerProfileService
	 */
	public MplCustomerProfileService getMplCustomerProfileService()
	{
		return mplCustomerProfileService;
	}

	/**
	 * @param mplCustomerProfileService
	 *           the mplCustomerProfileService to set
	 */
	public void setMplCustomerProfileService(final MplCustomerProfileService mplCustomerProfileService)
	{
		this.mplCustomerProfileService = mplCustomerProfileService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the wishlistFacade
	 */
	public WishlistFacade getWishlistFacade()
	{
		return wishlistFacade;
	}

	/**
	 * @param wishlistFacade
	 *           the wishlistFacade to set
	 */
	public void setWishlistFacade(final WishlistFacade wishlistFacade)
	{
		this.wishlistFacade = wishlistFacade;
	}

	/**
	 * @return the friendsInviteFacade
	 */
	public FriendsInviteFacade getFriendsInviteFacade()
	{
		return friendsInviteFacade;
	}

	/**
	 * @param friendsInviteFacade
	 *           the friendsInviteFacade to set
	 */
	public void setFriendsInviteFacade(final FriendsInviteFacade friendsInviteFacade)
	{
		this.friendsInviteFacade = friendsInviteFacade;
	}

	/**
	 * @return the accountAddressFacade
	 */
	public MplAccountAddressFacade getAccountAddressFacade()
	{
		return accountAddressFacade;
	}

	/**
	 * @param accountAddressFacade
	 *           the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(final MplAccountAddressFacade accountAddressFacade)
	{
		this.accountAddressFacade = accountAddressFacade;
	}

	/**
	 * @return the updateFeedbackFacade
	 */
	public UpdateFeedbackFacade getUpdateFeedbackFacade()
	{
		return updateFeedbackFacade;
	}

	/**
	 * @param updateFeedbackFacade
	 *           the updateFeedbackFacade to set
	 */
	public void setUpdateFeedbackFacade(final UpdateFeedbackFacade updateFeedbackFacade)
	{
		this.updateFeedbackFacade = updateFeedbackFacade;
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
	 * @return the mplEnumerationHelper
	 */
	public MplEnumerationHelper getMplEnumerationHelper()
	{
		return mplEnumerationHelper;
	}

	/**
	 * @param mplEnumerationHelper
	 *           the mplEnumerationHelper to set
	 */
	public void setMplEnumerationHelper(final MplEnumerationHelper mplEnumerationHelper)
	{
		this.mplEnumerationHelper = mplEnumerationHelper;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the notificationFacade
	 */
	public NotificationFacade getNotificationFacade()
	{
		return notificationFacade;
	}

	/**
	 * @param notificationFacade
	 *           the notificationFacade to set
	 */
	public void setNotificationFacade(final NotificationFacade notificationFacade)
	{
		this.notificationFacade = notificationFacade;
	}

	/**
	 * @return the cancelReturnFacade
	 */
	public CancelReturnFacade getCancelReturnFacade()
	{
		return cancelReturnFacade;
	}

	/**
	 * @param cancelReturnFacade
	 *           the cancelReturnFacade to set
	 */
	public void setCancelReturnFacade(final CancelReturnFacade cancelReturnFacade)
	{
		this.cancelReturnFacade = cancelReturnFacade;
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
	 * @return the mplMyFavBrandCategoryFacade
	 */
	public MplMyFavBrandCategoryFacade getMplMyFavBrandCategoryFacade()
	{
		return mplMyFavBrandCategoryFacade;
	}

	/**
	 * @param mplMyFavBrandCategoryFacade
	 *           the mplMyFavBrandCategoryFacade to set
	 */
	public void setMplMyFavBrandCategoryFacade(final MplMyFavBrandCategoryFacade mplMyFavBrandCategoryFacade)
	{
		this.mplMyFavBrandCategoryFacade = mplMyFavBrandCategoryFacade;
	}

	/**
	 * @return the mplUserHelper
	 */
	public MplUserHelper getMplUserHelper()
	{
		return mplUserHelper;
	}

	/**
	 * @param mplUserHelper
	 *           the mplUserHelper to set
	 */
	public void setMplUserHelper(final MplUserHelper mplUserHelper)
	{
		this.mplUserHelper = mplUserHelper;
	}

	/**
	 * @return the mplPaymentWebFacade
	 */
	public MplPaymentWebFacade getMplPaymentWebFacade()
	{
		return mplPaymentWebFacade;
	}

	/**
	 * @param mplPaymentWebFacade
	 *           the mplPaymentWebFacade to set
	 */
	public void setMplPaymentWebFacade(final MplPaymentWebFacade mplPaymentWebFacade)
	{
		this.mplPaymentWebFacade = mplPaymentWebFacade;
	}

	/**
	 * @return the mplNetBankingFacade
	 */
	public MplNetBankingFacade getMplNetBankingFacade()
	{
		return mplNetBankingFacade;
	}

	/**
	 * @param mplNetBankingFacade
	 *           the mplNetBankingFacade to set
	 */
	public void setMplNetBankingFacade(final MplNetBankingFacade mplNetBankingFacade)
	{
		this.mplNetBankingFacade = mplNetBankingFacade;
	}

	/**
	 * @return the userexService
	 */
	public ExtendedUserServiceImpl getUserexService()
	{
		return userexService;
	}

	/**
	 * @param userexService
	 *           the userexService to set
	 */
	public void setUserexService(final ExtendedUserServiceImpl userexService)
	{
		this.userexService = userexService;
	}

	/**
	 * @return the mplCustomerProfileFacade
	 */
	public MplCustomerProfileFacade getMplCustomerProfileFacade()
	{
		return mplCustomerProfileFacade;
	}

	/**
	 * @param mplCustomerProfileFacade
	 *           the mplCustomerProfileFacade to set
	 */
	public void setMplCustomerProfileFacade(final MplCustomerProfileFacade mplCustomerProfileFacade)
	{
		this.mplCustomerProfileFacade = mplCustomerProfileFacade;
	}

	/**
	 * @param checkoutCustomerStrategy
	 *           the checkoutCustomerStrategy to set
	 */
	public void setCheckoutCustomerStrategy(final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @param friendsInviteService
	 *           the friendsInviteService to set
	 */
	public void setFriendsInviteService(final FriendsInviteService friendsInviteService)
	{
		this.friendsInviteService = friendsInviteService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
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
	 * @param i18nFacade
	 *           the i18NFacade to set
	 */
	public void setI18NFacade(final I18NFacade i18nFacade)
	{
		i18NFacade = i18nFacade;
	}

	/**
	 * @return the httpRequestPaymentInfoPopulator
	 */
	public ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> getHttpRequestPaymentInfoPopulator()
	{
		return httpRequestPaymentInfoPopulator;
	}

	/**
	 * @param httpRequestPaymentInfoPopulator
	 *           the httpRequestPaymentInfoPopulator to set
	 */
	public void setHttpRequestPaymentInfoPopulator(
			final ConfigurablePopulator<HttpServletRequest, CCPaymentInfoData, PaymentInfoOption> httpRequestPaymentInfoPopulator)
	{
		this.httpRequestPaymentInfoPopulator = httpRequestPaymentInfoPopulator;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}

	/**
	 * @return the maxFieldLength
	 */
	public static int getMaxFieldLength()
	{
		return MAX_FIELD_LENGTH;
	}

	/**
	 * @return the maxPostcodeLength
	 */
	public static int getMaxPostcodeLength()
	{
		return MAX_POSTCODE_LENGTH;
	}

	/**
	 * @return the maxFieldLengthNew
	 */
	public static int getMaxFieldLengthNew()
	{
		return MAX_FIELD_LENGTH_NEW;
	}

	/**
	 * @return the maxFieldLengthAddline
	 */
	public static int getMaxFieldLengthAddline()
	{
		return MAX_FIELD_LENGTH_ADDLINE;
	}

	/**
	 * @return the maxFieldLengthState
	 */
	public static int getMaxFieldLengthState()
	{
		return MAX_FIELD_LENGTH_STATE;
	}

	/**
	 * @return the maxFieldLengthCountry
	 */
	public static int getMaxFieldLengthCountry()
	{
		return MAX_FIELD_LENGTH_COUNTRY;
	}

	/**
	 * @return the buyBoxFacade
	 */
	public BuyBoxFacade getBuyBoxFacade()
	{
		return buyBoxFacade;
	}

	/**
	 * @param buyBoxFacade
	 *           the buyBoxFacade to set
	 */
	public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade)
	{
		this.buyBoxFacade = buyBoxFacade;
	}

	/**
	 * @return the productDetailsHelper
	 */
	public ProductDetailsHelper getProductDetailsHelper()
	{
		return productDetailsHelper;
	}

	/**
	 * @param productDetailsHelper
	 *           the productDetailsHelper to set
	 */
	public void setProductDetailsHelper(final ProductDetailsHelper productDetailsHelper)
	{
		this.productDetailsHelper = productDetailsHelper;
	}

	/**
	 * This method sorts the list of net banking banks.
	 *
	 * @param toSortList
	 * @return List
	 *
	 *         This method was developed for TPR-4855
	 */
	private List sortedList(final List<NetBankingWsDTO> toSortList)
	{
		final Comparator<NetBankingWsDTO> byName = (final NetBankingWsDTO o1, final NetBankingWsDTO o2) -> o1.getBankName()
				.compareTo(o2.getBankName());
		Collections.sort(toSortList, byName);
		return toSortList;
	}

}