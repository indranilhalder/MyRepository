/**
 * 
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercefacades.address.AddressVerificationFacade;
import de.hybris.platform.commercefacades.address.data.AddressVerificationResult;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.customergroups.CustomerGroupFacade;
import de.hybris.platform.commercefacades.i18n.I18NFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.commercewebservicescommons.oauth2.token.OAuthTokenService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.wishlist2.Wishlist2Service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.lux.facade.CommonUtils;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.impl.MplCheckoutFacadeImpl;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.myfavbrandcategory.MplMyFavBrandCategoryFacade;
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
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.account.reviews.GigyaFacade;
import com.tisl.mpl.facades.egv.data.EgvDetailsData;
import com.tisl.mpl.facades.order.impl.DefaultGetOrderDetailsFacadeImpl;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.populators.CustomAddressReversePopulator;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.facades.webform.MplWebFormFacade;
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
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtendedUserServiceImpl;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.populator.HttpRequestCustomerDataPopulator;
import com.tisl.mpl.populator.options.PaymentInfoOption;
import com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.service.MplMobileUserService;
import com.tisl.mpl.service.impl.MplProductWebServiceImpl;
import com.tisl.mpl.v2.controller.UsersController.AddressField;
import com.tisl.mpl.v2.helper.OrdersHelper;
import com.tisl.mpl.wsdto.ApplyCliqCashWsDto;
import com.tisl.mpl.wsdto.BuyingEgvRequestWsDTO;
import com.tisl.mpl.wsdto.BuyingEgvResponceWsDTO;
import com.tisl.mpl.wsdto.EgvWalletCreateRequestWsDTO;
import com.tisl.mpl.wsdto.ErrorDTO;
import com.tisl.mpl.wsdto.RedeemCliqVoucherWsDTO;
import com.tisl.mpl.wsdto.ResendEGVNotificationWsDTO;
import com.tisl.mpl.wsdto.TotalCliqCashBalanceWsDto;
import com.tisl.mpl.wsdto.UserCliqCashWsDto;

/**
 * @author IT
 *
 */

@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
@Controller
@RequestMapping(value = "/{baseSiteId}/users", headers = "Accept=application/xml,application/json")
@CacheControl(directive = CacheControlDirective.PRIVATE)
public class WalletController
{
	private static final Logger LOG = Logger.getLogger(UsersController.class);
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	private CheckoutCustomerStrategy checkoutCustomerStrategy;
	@Resource(name = "ordersHelper")
	private OrdersHelper ordersHelper;
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
	
	@Resource(name = "mplWebFormFacade")
	private MplWebFormFacade mplWebFormFacade;

	@Autowired
	private ExtendedUserServiceImpl userexService;

	@Resource(name = "oauthTokenService")
	private OAuthTokenService oauthTokenService;
	@Autowired
	private MplCustomerProfileFacade mplCustomerProfileFacade;

	@Resource(name = "i18NFacade")
	private I18NFacade i18NFacade;

	@Resource(name = "mplVoucherService")
	private MplVoucherService mplVoucherService;
	
	@Autowired
	private RegisterCustomerFacade registerCustomerFacade;


	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String ROLE_CLIENT = "ROLE_CLIENT";
	private static final String APPLICATION_TYPE = "application/json";
	private static final String USER_ID = "/{userId}";
	private static final String ROLE_GUEST = "ROLE_GUEST";
	
	/**
	 * This API is used to USe Cliq Cash While Placing Order 
	 * 
	 * 
	 * @param cartGuid
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws EtailBusinessExceptions
	 * @throws CalculationException
	 */
	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.APPLY_CLIQCASH, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ApplyCliqCashWsDto applyCliqCash(@RequestParam final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException
	{
		ApplyCliqCashWsDto applyCliqCashWsDto = new ApplyCliqCashWsDto();
		OrderModel orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
		CartModel cart = null;

		try
		{
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				LOG.info("Applying  cliq Cash For Card Guid " + cartGuid);
				applyCliqCashWsDto = mplCartWebService.applyCLiqCash(cart, null);
			}
			else
			{
				//orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
				LOG.info("Applying  cliq Cash For Order Guid " + cartGuid);
				applyCliqCashWsDto = mplCartWebService.applyCLiqCash(orderModel, null);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			applyCliqCashWsDto.setError(ex.getErrorCode());
			applyCliqCashWsDto.setErrorCode(ex.getErrorCode());
			applyCliqCashWsDto.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while applying Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			applyCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			applyCliqCashWsDto.setError(ex.getMessage());
			LOG.error("Exception occrred while applying Cliq cash" + ex.getMessage());
		}
		return applyCliqCashWsDto;

	}

	
/**
 * This API is used to Remove the Cliq Cash From Cart
 * @param cartGuid
 * @return
 * @throws EtailNonBusinessExceptions
 * @throws EtailBusinessExceptions
 * @throws CalculationException
 */

	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REMOVE_CLIQCASH, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ApplyCliqCashWsDto removeCliqCash(@RequestParam final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException
	{
		LOG.info("Removing cliq Cash ");
		 ApplyCliqCashWsDto removeCliqCashWsDto = new ApplyCliqCashWsDto();
		 OrderModel orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
		CartModel cart = null;
		try
		{
			
			//  Removing the cliqCash balacne from Cart and Setting SplitModeInfo To JUSPAY
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				commerceCartService.recalculateCart(cart);
				 
				removeCliqCashWsDto.setDiscount(cart.getTotalDiscounts());
				if (null != cart.getTotalPrice())
				{
					removeCliqCashWsDto.setTotalAmount(cart.getTotalPrice().toString());
				}
				removeCliqCashWsDto.setPaybleAmount(cart.getTotalPrice());
				cart.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
				cart.setPayableWalletAmount(Double.valueOf(0.0D));
				modelService.save(cart);
				modelService.refresh(cart);
				removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
			//	orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
				AbstractOrderModel order =orderModel; 
				commerceCartService.recalculateCart((CartModel)order);
					removeCliqCashWsDto.setDiscount(orderModel.getTotalDiscounts());
					if (null != orderModel.getTotalPrice())
					{
						removeCliqCashWsDto.setPaybleAmount(orderModel.getTotalPrice());
					}
					orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
					orderModel.setPayableWalletAmount(Double.valueOf(0.0D));
					modelService.save(orderModel);
					modelService.refresh(orderModel);
					removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			removeCliqCashWsDto.setErrorCode(ex.getErrorCode());
			removeCliqCashWsDto.setError(ex.getErrorMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			removeCliqCashWsDto.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			removeCliqCashWsDto.setError(ex.getMessage());
			LOG.error("Exception occrred while Removing Cliq cash" + ex.getMessage());
		}
		return removeCliqCashWsDto;

	}






/**
 * This API Is used to Redeem Cliq Cash using Cart Number and Pin 
 * @param couponCode
 * @param passKey
 * @return
 * @throws EtailNonBusinessExceptions
 * @throws EtailBusinessExceptions
 * @throws CalculationException
 */

	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = MarketplacewebservicesConstants.REDEEM_CLIQ_VOUCHER, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public RedeemCliqVoucherWsDTO redeemCliqVoucher(@RequestParam final String couponCode, @RequestParam final String passKey,
			@RequestParam(required=false) final String cartGuid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

	{
		LOG.info("Redeeming CLiq Cash Voucher Card Number " + couponCode);
		boolean customerRegisteredwithQc = false;
		RedeemCliqVoucherWsDTO redeemCliqVoucherWsDTO = new RedeemCliqVoucherWsDTO();
		OrderModel orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
		CartModel cart = null;
		ApplyCliqCashWsDto applyCliqCashWsDto = null;
//		if (null != cartGuid)
//		{
//			cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
//		}
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
					redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				else
				{
					redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
				}
			}
			else
			{
				customerRegisteredwithQc = true;
			}
			if (customerRegisteredwithQc)
			{
				LOG.debug("Calling To QC For Adding money to Wallet");
				final RedimGiftCardResponse response = mplWalletFacade.getAddEGVToWallet(couponCode, passKey);
				if (null != response && null != response.getResponseCode() && response.getResponseCode() == Integer.valueOf(0))
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
							redeemCliqVoucherWsDTO.setAcknowledgement("Congrats!  Money has been added to your Cliq Cash balance");
							redeemCliqVoucherWsDTO.setIsWalletLimitReached(false);
							redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							if (null == orderModel)
							{
								cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
								applyCliqCashWsDto = mplCartWebService.applyCLiqCash(cart, response.getWallet().getBalance());

							}
							else if (null != cartGuid)
							{
								applyCliqCashWsDto = mplCartWebService.applyCLiqCash(orderModel, response.getWallet().getBalance());
							}
							if (null != applyCliqCashWsDto)
							{
								redeemCliqVoucherWsDTO.setApplyCliqCash(applyCliqCashWsDto);
							}
						}
						else
						{
							redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
							redeemCliqVoucherWsDTO.setError(response.getResponseMessage());
						}


					}
					else
					{
						redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
						redeemCliqVoucherWsDTO.setError(response.getResponseMessage());
					}
				}
				else
				{
					redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
					if (null != response)
					{
						redeemCliqVoucherWsDTO.setError(response.getResponseMessage());
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
			LOG.error("Exception occrred while Redeeming Cliq cash" + ex.getMessage());
		}
		catch (final Exception ex)
		{
			redeemCliqVoucherWsDTO.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			redeemCliqVoucherWsDTO.setError(ex.getMessage());
			LOG.error("Exception occrred while Redeeming Cliq cash" + ex.getMessage());
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
	
	private EgvDetailsData populateEGVFormToData(final BuyingEgvRequestWsDTO requestData)
	{
		final EgvDetailsData egvDetailsData = new EgvDetailsData();
		if (null != requestData)
		{
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
			if (null != requestData.getFrom())
			{
				egvDetailsData.setFromEmailAddress(requestData.getFrom());
			}
			if (null != requestData.getPriceSelectedByUserPerQuantity())
			{
				egvDetailsData.setGiftRange(requestData.getPriceSelectedByUserPerQuantity().doubleValue());
			}
			if (null != requestData.getMobileNumber())
			{
				egvDetailsData.setFromPhoneNo(requestData.getMobileNumber());
			}

			if (null != requestData.getMessageOnCard())
			{
				egvDetailsData.setMessageBox(requestData.getMessageOnCard());
			}

			egvDetailsData.setTotalEGV(requestData.getQuantity());
		}

		return egvDetailsData;
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

@Secured(
		{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
		@RequestMapping(value = MarketplacewebservicesConstants.USER_CLIQCASH_DETAILS, method = RequestMethod.POST, produces = APPLICATION_TYPE)
		@ResponseBody
		public UserCliqCashWsDto getUserCliqCashDetails()
				throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

{
	UserCliqCashWsDto responce = new UserCliqCashWsDto();
	try
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if (null != currentCustomer)
		{

			responce = mplCartWebService.getUserCliqCashDetails(currentCustomer);
			if (null == responce)
			{
				responce = new UserCliqCashWsDto();
				responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			}
		}
		else
		{
			responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			responce.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B5002));
			responce.setErrorCode(MarketplacecommerceservicesConstants.B5002);
		}
	}
	catch (final Exception ex)
	{
		{
			responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			responce.setError(ex.getMessage());
			LOG.error("Exception occrred Getting Cliq Cash Details of user " + ex.getMessage());
		}
	}
	return responce;
}


@Secured(
		{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
		@RequestMapping(value = MarketplacewebservicesConstants.CHECK_WALLET_MOBILE_NUMBER, method = RequestMethod.POST, produces = APPLICATION_TYPE)
		@ResponseBody
		public ErrorDTO checkWalletMobileNumber(@RequestBody final EgvWalletCreateRequestWsDTO request)
				throws EtailNonBusinessExceptions, EtailBusinessExceptions, CalculationException

{
	ErrorDTO responce = new ErrorDTO();
	try
	{
			if (null != request)
			{
				if (null == request.getFirstName() || request.getFirstName().isEmpty())
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5011);
				}
				else if (null == request.getLastName() || request.getLastName().isEmpty())
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5012);
				}
				else if (null == request.getMobileNumber() || request.getMobileNumber().isEmpty())
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5013);
				}
				
				if (registerCustomerFacade.checkUniquenessOfMobileForWallet(request.getMobileNumber()))
				{
					registerCustomerFacade.registerWalletMobileNumber(request.getFirstName(),request.getLastName(),request.getMobileNumber());//TPR-6272 parameter platformNumber passed
					//Set success flag
					responce.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5010);
				}
			}else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B5010);
			}
		
	}
	catch (final Exception ex)
	{
		{
			responce.setStatus(MarketplacecommerceservicesConstants.FAILURE_FLAG);
			responce.setError(ex.getMessage());
			LOG.error("Exception occrred while saving mobile number for QC wallet " + ex.getMessage());
		}
	}
	return responce;
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


}
