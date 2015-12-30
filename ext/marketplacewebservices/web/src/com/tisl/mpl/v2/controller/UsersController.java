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
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
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
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.constants.YcommercewebservicesConstants;
import com.tisl.mpl.core.enums.FeedbackArea;
import com.tisl.mpl.core.enums.Frequency;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.EMIBankModel;
import com.tisl.mpl.data.EditWishlistNameData;
import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.data.ReturnLogisticsResponseDetails;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.myfavbrandcategory.MplMyFavBrandCategoryFacade;
import com.tisl.mpl.facade.netbank.MplNetBankingFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.preference.MplPreferenceFacade;
import com.tisl.mpl.facades.account.register.FriendsInviteFacade;
import com.tisl.mpl.facades.account.register.MplCustomerProfileFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.facades.data.MplFavBrandCategoryData;
import com.tisl.mpl.facades.data.MplFavBrandCategoryWsDTO;
import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.product.data.ReturnReasonDetails;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.helper.MplUserHelper;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.FriendsInviteService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.ExtendedUserServiceImpl;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.populator.HttpRequestCustomerDataPopulator;
import com.tisl.mpl.populator.options.PaymentInfoOption;
import com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.service.MplMobileUserService;
import com.tisl.mpl.service.impl.MplProductWebServiceImpl;
import com.tisl.mpl.user.data.AddressDataList;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.validation.data.AddressValidationData;
import com.tisl.mpl.webservice.businessvalidator.DefaultCommonAsciiValidator;
import com.tisl.mpl.wsdto.EMIBankListWsDTO;
import com.tisl.mpl.wsdto.EMIBankWsDTO;
import com.tisl.mpl.wsdto.EMITermRateDataForMobile;
import com.tisl.mpl.wsdto.FetchNewsLetterSubscriptionWsDTO;
import com.tisl.mpl.wsdto.GetCustomerDetailDto;
import com.tisl.mpl.wsdto.GetWishListDataWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.GetmerchantWsDTO;
import com.tisl.mpl.wsdto.MplAllFavouritePreferenceWsDTO;
import com.tisl.mpl.wsdto.MplOrderNotificationWsDto;
import com.tisl.mpl.wsdto.MplOrderTrackingNotificationsListWsDto;
import com.tisl.mpl.wsdto.MplPreferenceDataForMobile;
import com.tisl.mpl.wsdto.MplPreferenceListWsDto;
import com.tisl.mpl.wsdto.MplUserResultWsDto;
import com.tisl.mpl.wsdto.NetBankingListWsDTO;
import com.tisl.mpl.wsdto.NetBankingWsDTO;
import com.tisl.mpl.wsdto.OrderCreateInJusPayWsDto;
import com.tisl.mpl.wsdto.ReturnLogisticsResponseDetailsWsDTO;
import com.tisl.mpl.wsdto.ReturnReasonDetailsWsDTO;
import com.tisl.mpl.wsdto.UpdateCustomerDetailDto;
import com.tisl.mpl.wsdto.UserResultWsDto;
import com.tisl.mpl.wsdto.ValidateOtpWsDto;
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

	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private RegisterCustomerFacade registerCustomerFacade;
	@Autowired
	private Populator<AddressData, AddressModel> addressReversePopulator;
	@Autowired
	private Wishlist2Service wishlistService;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	//
	@Autowired
	private MplPreferenceFacade mplPreferenceFacade;
	@Autowired
	private ExtendedUserService extUserService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Autowired
	private MplMobileUserService mobileUserService;
	@Autowired
	private MplCustomerProfileService mplCustomerProfileService;
	@Autowired
	private UserService userService;
	@Autowired
	private FriendsInviteService friendsInviteService;
	@Autowired
	private CustomerAccountService customerAccountService;
	@Autowired
	private WishlistFacade wishlistFacade;
	@Autowired
	private FriendsInviteFacade friendsInviteFacade;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private HybrisOAuthTokenStore hybrisOAuthTokenStore;
	//	@Autowired
	//	private ForgetPasswordFacade forgetPasswordFacade;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Autowired
	private UpdateFeedbackFacade updateFeedbackFacade;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private EnumerationService enumerationService;
	//	@Autowired
	//	private ExtendedUserService extendedUserService;
	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private MplCartFacade mplCartFacade;

	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;
	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private NotificationFacade notificationFacade;

	@Autowired
	private CancelReturnFacade cancelReturnFacade;

	@Autowired
	private MplOrderFacade mplOrderFacade;

	@Autowired
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


	//@Autowired
	//private MplPaymentFacadeImpl mplPaymentFacadeImpl;
	//	@Autowired Critical Sonar fixes Unused private Field
	//	private CommerceCartService commerceCartService;
	//	@Autowired
	//	private ExtendedUserService extendedUserService;

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

	/**
	 * @param emailId
	 * @param password
	 * @param request
	 * @return MplUserResultWsDto
	 * @throws DuplicateUidException
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 */

	//Mobile registration
	@Secured(
	{ ROLE_CLIENT, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/registration", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto registerUser(@RequestParam final String emailId, @RequestParam final String password)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException

	{
		LOG.debug("****************** User Registration mobile web service ***********" + emailId);
		MplUserResultWsDto userResult = new MplUserResultWsDto();
		try
		{
			userResult = mobileUserService.registerNewMplUser(emailId, password);
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
	public MplUserResultWsDto loginUser(@PathVariable final String emailId, @RequestParam final String password)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		try
		{
			LOG.debug("****************** User Login mobile web service ***********" + emailId);
			//Login user with username and password
			result = mobileUserService.loginUser(emailId, password);
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
	 * Register in portal via social media login such as facebook and googleplus
	 *
	 * @param emailId
	 * @param socialMediaToken
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
	public MplUserResultWsDto socialMediaRegistration(@RequestParam final String emailId,
			@RequestParam final String socialMediaToken, @RequestParam final String socialMedia,
			@RequestParam(required = false) final String socialUserId)
					throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		try
		{
			LOG.debug("****************** Social Media User Registration mobile web service ***********" + emailId);
			if (!(StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.FACEBOOK)
					|| (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.GOOGLEPLUS))))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9020);
			}
			else if (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.FACEBOOK))
			{
				result = mobileUserService.socialFbRegistration(socialMediaToken, emailId);
			}
			else if (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.GOOGLEPLUS))
			{
				result = mobileUserService.socialGoogleRegistration(socialMediaToken, emailId, socialUserId);
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
	 * @param socialMediaToken
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
	public MplUserResultWsDto loginSocialUser(@PathVariable final String emailId, @RequestParam final String socialMediaToken,
			@RequestParam final String socialMedia, @RequestParam(required = false) final String socialUserId)
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
				result = mobileUserService.loginSocialFbUser(socialMediaToken, emailId);
			}
			else if (StringUtils.equalsIgnoreCase(socialMedia.toLowerCase(), MarketplacewebservicesConstants.GOOGLEPLUS))
			{
				result = mobileUserService.loginSocialGoogleUser(socialMediaToken, emailId, socialUserId);
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
		final String specificUrl = MarketplacewebservicesConstants.FORGOTPASSWORD_URL + MarketplacewebservicesConstants.LINK_LOGIN
				+ MarketplacewebservicesConstants.QS + MarketplacewebservicesConstants.AFFILIATEID
				+ MarketplacewebservicesConstants.EQUALS + affiliateId;
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
								textMessage = MarketplacecommerceservicesConstants.FRIEND_EMAIL_MESSAGE_TEXT;
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
			MplCustomerProfileData mplCustData = new MplCustomerProfileData();
			mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
			LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
			final UserModel user = userService.getUserForUID(mplCustData.getUid());

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

				final CustomerModel currentCustomer = (CustomerModel) user;

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
		MplCustomerProfileData mplCustData = new MplCustomerProfileData();
		final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
		final UserResultWsDto result = new UserResultWsDto();
		if (null == allWishlists)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9206);
		}
		boolean add = false;

		mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
		LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
		String name = null;
		final UserModel user = userService.getUserForUID(mplCustData.getUid());

		final List<WishlistData> wishListData = new ArrayList<WishlistData>();
		String nameSet = null;
		int wSize = 0;
		if (null == user)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
		}

		try
		{
			if (StringUtils.isNotEmpty(wishlistName))
			{
				name = wishlistName;
			}
			else
			{
				nameSet = MarketplacecommerceservicesConstants.WISHLIST_NO;
				for (final Wishlist2Model wishlist2Model : allWishlists)
				{
					if (wishlist2Model.getName().contains(nameSet))
					{
						wSize++;
					}
				}
				name = MarketplacecommerceservicesConstants.WISHLIST_NO + MarketplacecommerceservicesConstants.UNDER_SCORE
						+ (wSize + 1);
			}

			Wishlist2Model requiredWl = null;

			for (final Wishlist2Model wl : allWishlists)
			{
				if (null != wl && name.equals(wl.getName()))
				{
					requiredWl = wl;
					break;
				}
			}
			if (null != ussid && !ussid.isEmpty() && null != productCode && !productCode.isEmpty())
			{

				if (null != requiredWl)
				{
					//add to existing wishlists

					add = wishlistFacade.addProductToWishlist(requiredWl, productCode, ussid, isSelectedSize);
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

					if (!allWishlists.isEmpty() && allWishlists.size() > 0)
					{
						final List<String> wishlistnames = new ArrayList<String>();
						for (final Wishlist2Model wishlist2Model : allWishlists)
						{
							//							String wishList = new String(); Avoid instantiating String objects; this is usually unnecessary
							//							wishList = wish.getName();
							//							wishlistnames.add(wishList);

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

						final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, name, productCode);
						add = wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, isSelectedSize);
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

				if (!(null != requiredWl) || !(null != ussid) || StringUtils.isEmpty(ussid) || !(null != productCode)
						|| StringUtils.isEmpty(productCode))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9208);
				}

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
		return result;
	}

	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{emailId}/addAddress", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto addAddress(@RequestParam final String emailId, @RequestParam final String firstName,
			@RequestParam final String lastName, @RequestParam final String line1, @RequestParam final String line2,
			@RequestParam final String line3, @RequestParam final String town, @RequestParam final String state,
			@RequestParam final String countryIso, @RequestParam final String postalCode, @RequestParam final String phone,
			@RequestParam final String addressType, @RequestParam final boolean defaultFlag) throws RequestParameterException
	{

		String errorMsg = null;
		final UserResultWsDto result = new UserResultWsDto();
		boolean successFlag = false;
		try
		{
			MplCustomerProfileData mplCustData = new MplCustomerProfileData();
			mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
			LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
			final UserModel user = userService.getUserForUID(mplCustData.getUid());
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
				final CustomerModel currentCustomer = (CustomerModel) user;

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
	@RequestMapping(value = "/{emailId}/removeAddress", method = RequestMethod.POST)
	@ResponseBody
	public UserResultWsDto removeAddress(@RequestParam final String emailId, @RequestParam final String addressId)
			throws RequestParameterException
	{
		MplCustomerProfileData mplCustData = new MplCustomerProfileData();
		mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
		LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
		final UserModel user = userService.getUserForUID(mplCustData.getUid());
		final UserResultWsDto result = new UserResultWsDto();

		boolean successFlag = false;
		try
		{
			final AddressData addressData = new AddressData();
			addressData.setId(addressId);
			final CustomerModel currentCustomer = (CustomerModel) user;

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
			@RequestParam final String line2, @RequestParam final String line3, @RequestParam final String town,
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
			MplCustomerProfileData mplCustData = new MplCustomerProfileData();
			mplCustData = mplCustomerProfileService.getCustomerProfileDetail(emailId);
			LOG.debug(CUSTOMER_MESSAGE + mplCustData.getUid());
			final UserModel user = userService.getUserForUID(mplCustData.getUid());

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
				newAddress.setId(addressId);
				newAddress.setFirstName(firstName);
				newAddress.setLastName(lastName);
				newAddress.setLine1(line1);
				newAddress.setLine2(line2);
				newAddress.setLine3(line3);
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

				final CustomerModel currentCustomer = (CustomerModel) user;

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
				"address.line1.invalid"), LINE2("line2", "address.line2.invalid"), LINE3("line3", "address.line3.invalid"), TOWN(
						"townCity", "address.townCity.invalid"), POSTCODE("postcode", "address.postcode.invalid"), REGION("regionIso",
								"address.regionIso.invalid"), COUNTRY("countryIso", "address.country.invalid"), ADDRESSTYPE("addressType",
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
		try
		{
			List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			final UserModel user = userService.getCurrentUser();
			String name = null;
			String nameSet = null;
			int wSize = 0;
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
				nameSet = MarketplacecommerceservicesConstants.WISHLIST_NO;
				for (final Wishlist2Model wishlist2Model : allWishlists)
				{
					if (wishlist2Model.getName().contains(nameSet))
					{
						wSize++;
					}
				}
				name = MarketplacecommerceservicesConstants.WISHLIST_NO + MarketplacecommerceservicesConstants.UNDER_SCORE
						+ (wSize + 1);
			}
			allWishlists = wishlistService.getWishlists(user);
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
		String selectedSize = null;
		try
		{
			List<Wishlist2Model> allWishlists;
			final UserModel user = userService.getCurrentUser();
			if (null == user)
			{
				wlDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				wlDTO.setError(MarketplacecommerceservicesConstants.EMAIL_NOT_FOUND);
				return wlDTO;
			}

			allWishlists = wishlistService.getWishlists(user);
			if (null != allWishlists)
			{
				for (final Wishlist2Model requiredWl : allWishlists)
				{
					if (null != requiredWl)
					{
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
							wldpDTO = new GetWishListProductWsDTO();
							ProductData productData1 = null;
							if (null != entryModel.getProduct())
							{
								productData1 = productFacade.getProductForOptions(entryModel.getProduct(),
										Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY,
												ProductOption.DESCRIPTION, ProductOption.CATEGORIES, ProductOption.PROMOTIONS,
												ProductOption.STOCK, ProductOption.REVIEW, ProductOption.DELIVERY_MODE_AVAILABILITY,
												ProductOption.SELLER));

							}
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
							if (null != productData1 && null != mplProductWebService.getCategoryCodeOfProduct(productData1)
									&& !mplProductWebService.getCategoryCodeOfProduct(productData1).isEmpty())
							{
								wldpDTO.setProductCategoryId(mplProductWebService.getCategoryCodeOfProduct(productData1));
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
									if (null != img && StringUtils.isNotEmpty(img.getFormat())
											&& img.getFormat().toLowerCase().equals(MarketplacecommerceservicesConstants.THUMBNAIL))
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
										if (null != buyboxmodel)
										{
											if (null != buyboxmodel.getSpecialPrice() && buyboxmodel.getSpecialPrice().doubleValue() > 0.0)
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

							final ProductModel productModel = getMplOrderFacade().getProductForCode(entryModel.getProduct().getCode());
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
								wldDTO.setCount(Integer.valueOf(entryModels.size()));
							}
						}
					}
					wldDTO.setProducts(wldpDTOList);
					wldDTOList.add(wldDTO);
				}
			}
			wlDTO.setWishList(wldDTOList);
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
		wlDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
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
		final MplCustomerProfileData mplCustData = new MplCustomerProfileData();
		mplCustData.setDisplayUid(userId);
		try
		{
			final UserModel user = userexService.getUserForUID(mplCustData.getDisplayUid());
			if (null == user)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
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

			Wishlist2EntryModel wishlist2EntryModel = null;
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
							wishlist2EntryModel = entryModel;
							ProductFound = true;
						}

					}
				}

				if (ProductFound)
				{
					wishlistService.removeWishlistEntry(requiredWl, wishlist2EntryModel);
					//Set Success Flag to true
					successFlag = true;
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
	 * @param userId
	 * @return MplUserResultWsDto
	 */
	@Secured(
	{ TRUSTED_CLIENT, CUSTOMERMANAGER, CUSTOMER })
	@RequestMapping(value = "/{userId}/logout", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto logoutUser(@PathVariable final String userId, final String fields)
	{
		final MplUserResultWsDto mplUserResultWsDto = new MplUserResultWsDto();
		final List<OAuthAccessTokenModel> accessTokenModelList = oauthTokenService
				.getAccessTokensForClientAndUser(MarketplacecommerceservicesConstants.CLIENTID, userId);
		if (null == accessTokenModelList)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9012);
		}
		else
		{
			for (final OAuthAccessTokenModel accessTokenModel : accessTokenModelList)
			{
				try
				{
					oauthTokenService.removeAccessToken(accessTokenModel.getTokenId());
					mplUserResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				catch (final EtailNonBusinessExceptions e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(e);
					if (null != e.getErrorMessage())
					{
						mplUserResultWsDto.setError(e.getErrorMessage());
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
					mplUserResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
			}
		}
		return dataMapper.map(mplUserResultWsDto, MplUserResultWsDto.class, fields);
	}


	/**
	 * @description method is called to resend the OTP Number for COD
	 * @param emailid
	 * @param fields
	 * @return ValidateOtpWsDto
	 * @throws DuplicateUidException
	 *            , InvalidKeyException ,NoSuchAlgorithmException
	 */
	/*
	 * @Secured( { CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	 *
	 * @RequestMapping(value = "/{emailid}/resendOtpforcod", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	 *
	 * @ResponseBody public ValidateOtpWsDto resendOTP(@PathVariable final String emailid, @RequestParam final String
	 * mobilenumber, final String fields) throws DuplicateUidException, InvalidKeyException, NoSuchAlgorithmException {
	 * final ValidateOtpWsDto validateOtpWsDto = new ValidateOtpWsDto(); List<CartModel> cartModelList = null; try {
	 * final CustomerData customerData = customerFacade.getCurrentCustomer(); if (null == customerData) { throw new
	 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025); } if (null != customerData.getUid() && null
	 * != mplCartFacade.getCartDetails(customerData.getUid())) { cartModelList = (List<CartModel>)
	 * mplCartFacade.getCartDetails(customerData.getUid()); } String cartID = null; if (null != cartModelList &&
	 * !cartModelList.isEmpty()) { cartID = cartModelList.get(0).getCode(); }
	 *
	 * final String mplCustomerID = customerData.getUid(); final String mplCustomerName = customerData.getName() != null
	 * ? customerData.getName() : "";
	 *
	 *
	 * if (null != mplCustomerID && StringUtils.isNotEmpty(mplCustomerID)) {
	 *
	 * if (null != mobilenumber && StringUtils.isNotEmpty(mobilenumber)) { if (StringUtils.length(mobilenumber) ==
	 * MarketplacecommerceservicesConstants.MOBLENGTH &&
	 * mobilenumber.matches(MarketplacecommerceservicesConstants.MOBILE_REGEX)) { ///////// final boolean NotBlackListed
	 * = mplPaymentFacadeImpl.isMobileBlackListed(mobilenumber); if (NotBlackListed) { //////// final String validation =
	 * getMplPaymentFacade().generateOTPforCODWeb(mplCustomerID, mobilenumber, mplCustomerName, cartID); if (null !=
	 * validation && StringUtils.isNotEmpty(validation)) {
	 *
	 * validateOtpWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG); } else { throw new
	 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9022); } } else { throw new
	 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9202); } } else { throw new
	 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9023); } } else { throw new
	 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9024); } } else { throw new
	 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025); }
	 *
	 * } catch (final InvalidCartException ce) { throw new EtailNonBusinessExceptions(ce,
	 * MarketplacecommerceservicesConstants.B9004); } catch (final EtailNonBusinessExceptions e) {
	 * ExceptionUtil.etailNonBusinessExceptionHandler(e); if (null != e.getErrorMessage()) {
	 * validateOtpWsDto.setError(e.getErrorMessage()); }
	 * validateOtpWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG); } catch (final
	 * EtailBusinessExceptions e) { ExceptionUtil.etailBusinessExceptionHandler(e, null); if (null !=
	 * e.getErrorMessage()) { validateOtpWsDto.setError(e.getErrorMessage()); }
	 * validateOtpWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG); }
	 *
	 * return validateOtpWsDto; }
	 */

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
			final MplCustomerProfileData customerToSave = mplCustomerProfileService.getCustomerProfileDetail(userId);
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
				customerToSave.setDisplayUid(userId);
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
		final CustomerData customer = customerFacade.getCurrentCustomer();
		final ValidateOtpWsDto result = new ValidateOtpWsDto();
		if (null == customer)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
		}
		else
		{
			final String mplCustomerID = customer.getUid();
			try
			{
				if (StringUtils.isNotEmpty(mplCustomerID))
				{

					LOG.debug("************** Mobile web service Validate OTP for COD ******************" + emailid);

					final String validationMsg = getMplPaymentFacade().validateOTPforCODWeb(mplCustomerID, enteredOTPNumber);
					if (null != validationMsg)
					{

						LOG.debug("************** Mobile web service Validate OTP for COD  RESPONSE SUCCESSSSS ******************"
								+ emailid);

						if (validationMsg.equals(MarketplacecommerceservicesConstants.OTPVALIDITY))
						{
							result.setError(MarketplacecommerceservicesConstants.OTP_SENT);
							result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						}
						else if (validationMsg.equals(MarketplacecommerceservicesConstants.OTPEXPIRY))
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
		}
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
				customerData = mplCustomerProfileService.getCustomerProfileDetail(emailid);
				if (null != customerData)
				{
					if (null != emailid && StringUtils.isNotEmpty(emailid))
					{
						customer.setEmailID(emailid);
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
	 * @returnUserResultWsDto
	 */


	@Secured(
	{ ROLE_CLIENT, CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/resetpassword", method = RequestMethod.PUT, produces = APPLICATION_TYPE)
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
			validated = mplUserHelper.validateRegistrationData(userId, newPassword);
			if (null != validated.getStatus()
					&& validated.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.ERROR_FLAG))
			{
				Localization.getLocalizedString(MarketplacewebservicesConstants.INVALID_PASSWORD_MOBILE);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9010);
			}
			else
			{
				if (containsRole(auth, TRUSTED_CLIENT) || containsRole(auth, CUSTOMERMANAGER))
				{
					extUserService.setPassword(userId, newPassword);
				}
				else
				{
					final UserModel user = userService.getCurrentUser();
					getCustomerAccountService().changePassword(user, old, newPassword);
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
						netbankingfinallist.addAll(netbankingwstolist1);
						netbankingfinallist.addAll(netbankingwstolist);
						//TISEE-929
						final Comparator<NetBankingWsDTO> byName = (final NetBankingWsDTO o1, final NetBankingWsDTO o2) -> o1
								.getBankName().compareTo(o2.getBankName());
						Collections.sort(netbankingfinallist, byName);
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
		final EMIBankListWsDTO emiBankListWsDTO = new EMIBankListWsDTO();
		final List<EMIBankWsDTO> emiBankWsListDTO = new ArrayList<EMIBankWsDTO>();
		List<EMIBankModel> emiBankList = new ArrayList<>();
		CustomerData customerData = null;
		try
		{
			emiBankList = mplNetBankingFacade.getEMIBanks(cartValue);
			customerData = customerFacade.getCurrentCustomer();
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
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9029);
						}
						else
						{

							for (final EMIBankModel emibanking : emiBankList)
							{
								final EMIBankWsDTO eMIBankWsDTO = new EMIBankWsDTO();

								if (StringUtils.isNotEmpty(emibanking.getName().getBankName()))
								{
									eMIBankWsDTO.setEmiBank(emibanking.getName().getBankName());
								}
								if (StringUtils.isNotEmpty(emibanking.getEmiLowerLimit().toString()))
								{
									eMIBankWsDTO.setEmiLowerLimit(emibanking.getEmiLowerLimit().toString());
								}
								if (StringUtils.isNotEmpty(emibanking.getEmiUpperLimit().toString()))
								{
									eMIBankWsDTO.setEmiUpperLimit(emibanking.getEmiUpperLimit().toString());
								}
								emiBankWsListDTO.add(eMIBankWsDTO);
							}
							//TISEE-929
							final Comparator<EMIBankWsDTO> byName = (final EMIBankWsDTO o1, final EMIBankWsDTO o2) -> o1.getEmiBank()
									.compareTo(o2.getEmiBank());

							Collections.sort(emiBankWsListDTO, byName);
							emiBankListWsDTO.setBankList(emiBankWsListDTO);
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
			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			if (null != allWishlists && !allWishlists.isEmpty())
			{
				for (final Wishlist2Model wishlist2Model : allWishlists)
				{
					if (wishlist2Model.getName().equals(wishlistName))
					{
						modelService.remove(wishlist2Model);
						userResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
					}
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
						orderNotificationDto.setOrderID(notifyData.getOrderNumber());
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
							final String orderDetailStatus = notifyData.getNotificationCustomerStatus().replace("@",
									notifyData.getOrderNumber());
							orderNotificationDto.setOrderDetailStatus(orderDetailStatus);
						}
						else
						{
							orderNotificationDto.setOrderDetailStatus(notifyData.getNotificationOrderStatus());
						}
						orderNotificationDto.setReadStatus(notifyData.getNotificationRead());
						orderNotificationDto.setConsignmentId(notifyData.getTransactionID());
						orderNotificationDto.setActualOrderDetailStatus(notifyData.getNotificationCustomerStatus());

						final Date date = new Date();
						final Date orderCreationDate = notifyData.getNotificationCreationDate();
						final long diff = date.getTime() - orderCreationDate.getTime();
						/* System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)); */

						final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
						orderNotificationDto.setNotificationCreationTime(timeFormat.format(orderCreationDate));

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
			@RequestParam final String ussid, @RequestParam final String refundType, @RequestParam final String transactionId)
	{
		final MplUserResultWsDto output = new MplUserResultWsDto();
		boolean resultFlag = false;
		String result = null;
		try
		{
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);
			OrderEntryData orderEntry = new OrderEntryData();
			final List<OrderEntryData> subOrderEntries = orderDetails.getEntries();
			for (final OrderEntryData entry : subOrderEntries)
			{
				if (entry.getTransactionId().equalsIgnoreCase(transactionId))
				{
					orderEntry = entry;
					break;
				}
			}

			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + emailId);
			if (ticketTypeCode.equalsIgnoreCase("C"))
			{
				resultFlag = cancelReturnFacade.implementCancelOrReturn(orderDetails, orderEntry, reasonCode, ussid, ticketTypeCode,
						customerData, refundType, false, SalesApplication.MOBILE);


			}
			else
			{
				resultFlag = cancelReturnFacade.implementCancelOrReturn(orderDetails, orderEntry, reasonCode, ussid, ticketTypeCode,
						customerData, refundType, true, SalesApplication.MOBILE);
			}

			if (resultFlag)
			{
				result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
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
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "{emailId}/getFavCategoriesBrands", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplAllFavouritePreferenceWsDTO getFavCategoriesBrands(@PathVariable final String emailId, final String fields)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		final MplAllFavouritePreferenceWsDTO mplAllFavouritePreferenceWsDTO = new MplAllFavouritePreferenceWsDTO();
		final List<MplFavBrandCategoryWsDTO> favBrandCategoryDtoForCategory = new ArrayList<MplFavBrandCategoryWsDTO>();
		Map<String, MplFavBrandCategoryData> brandCategoryDataForCategory = new HashMap<String, MplFavBrandCategoryData>();
		try
		{
			brandCategoryDataForCategory = mplMyFavBrandCategoryFacade.fetchAllCategories();

			final List<CategoryModel> selectedCategoryList = mplMyFavBrandCategoryFacade.fetchFavCategories(emailId);
			boolean flag = false;
			if (null != brandCategoryDataForCategory)
			{
				final Iterator<Map.Entry<String, MplFavBrandCategoryData>> categoryEntries = brandCategoryDataForCategory.entrySet()
						.iterator();
				while (categoryEntries.hasNext())
				{
					final Map.Entry<String, MplFavBrandCategoryData> entry = categoryEntries.next();
					if (null != selectedCategoryList && selectedCategoryList.size() > 0)
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

			final List<CategoryModel> selectedBrandList = mplMyFavBrandCategoryFacade.fetchFavBrands(emailId);
			boolean result = false;
			if (null != brandCategoryDataForBrand)
			{
				final Iterator<Map.Entry<String, MplFavBrandCategoryData>> categoryEntries = brandCategoryDataForBrand.entrySet()
						.iterator();
				while (categoryEntries.hasNext())
				{
					final Map.Entry<String, MplFavBrandCategoryData> entry = categoryEntries.next();
					if (null != selectedBrandList && selectedBrandList.size() > 0)
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

			if (favBrandCategoryDtoForCategory.size() > 0 && favBrandCategoryDtoForBrand.size() > 0)
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

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "{emailId}/addFavCategoriesBrands", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto addFavCategoriesBrand(@PathVariable final String emailId, @RequestParam final List codeList,
			@RequestParam final String type) throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		final UserResultWsDto resultDto = new UserResultWsDto();
		boolean result = false;
		try
		{
			if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_CATEGORY))
			{
				result = mplMyFavBrandCategoryFacade.addFavCategories(emailId, codeList);
			}
			else if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_BRAND))
			{
				result = mplMyFavBrandCategoryFacade.addFavBrands(emailId, codeList);
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
		return resultDto;

	}

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "{emailId}/deleteFavCategoriesBrands", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto deleteFavCategoriesBrands(@PathVariable final String emailId, @RequestParam final String code,
			@RequestParam final String type) throws RequestParameterException, WebserviceValidationException, MalformedURLException
	{
		final UserResultWsDto resultDto = new UserResultWsDto();
		boolean result = false;
		try
		{
			if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_CATEGORY))
			{
				result = mplMyFavBrandCategoryFacade.deleteFavCategories(emailId, code);
			}
			else if (!StringUtils.isEmpty(type) && type.equalsIgnoreCase(MarketplacecommerceservicesConstants.TYPE_BRAND))
			{
				result = mplMyFavBrandCategoryFacade.deleteFavBrands(emailId, code);
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
			@RequestParam(required = false) final String line3, @RequestParam(required = false) final String town,
			@RequestParam(required = false) final String state, @RequestParam(required = false) final String countryIso,
			@RequestParam(required = false) final String postalCode, @RequestParam(required = false) final String phone,
			@RequestParam(required = false) final String addressType, @RequestParam(required = false) final boolean defaultFlag,
			@RequestParam(required = false) final boolean saveFlag) throws RequestParameterException
	{
		String errorMsg = null;
		String cartIdentifier;
		final UserResultWsDto result = new UserResultWsDto();
		try
		{
			//			final UserModel user = userService.getCurrentUser();
			//fetch usermodel against customer
			final UserModel user = getExtUserService().getUserForOriginalUid(emailId);
			LOG.debug("addAddressToOrder : user : " + user);
			// Check userModel null
			if (null != user)
			{
				// Type Cast User Model to Address Model
				final CustomerModel currentCustomer = (CustomerModel) user;

				LOG.debug(String.format("addAddressToOrder: | addressId: %s | currentCustomer : %s | cartId : %s ", addressId,
						currentCustomer.getUid(), cartId));

				//getting cartmodel using cart id and user
				final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartId, user);
				// Validate Cart Model is not null
				if (null != cartModel)
				{
					final AddressData newAddress = new AddressData();
					AddressModel addressmodel = modelService.create(AddressModel.class);
					//verifying if existing address

					if (StringUtils.isNotEmpty(addressId))
					{
						final List<AddressData> addressList = accountAddressFacade.getAddressBook();
						for (final AddressData addEntry : addressList)
						{
							// Validate if valid address id for for the specific user
							if (addEntry.getId().equals(addressId))
							{
								addressmodel = customerAccountService.getAddressForCode(currentCustomer, addEntry.getId());
								break;
							}
						}
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
	 *              juspayReturnUrl,juspayOrderId
	 * @return OrderCreateInJusPayWsDto
	 * @throws InvalidCartException
	 */
	@Secured(
	{ CUSTOMER, "ROLE_TRUSTED_CLIENT", CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/createJuspayOrder", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public OrderCreateInJusPayWsDto createJuspayOrder(@RequestParam final String firstName, @RequestParam final String lastName,
			@RequestParam final String addressLine1, @RequestParam final String addressLine2,
			@RequestParam final String addressLine3, @RequestParam final String country, @RequestParam final String city,
			@RequestParam final String state, @RequestParam final String pincode, @RequestParam final String cartID,
			@RequestParam final String cardSaved, @RequestParam final String sameAsShipping, @PathVariable final String userId)
					throws EtailNonBusinessExceptions
	{
		final OrderCreateInJusPayWsDto orderCreateInJusPayWsDto = new OrderCreateInJusPayWsDto();
		final UserModel user = userService.getCurrentUser();
		LOG.debug("********* Creating juspay Order mobile web service" + userId);
		if (null == user)
		{
			orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			orderCreateInJusPayWsDto.setError(MarketplacecommerceservicesConstants.USER_NOT_FOUND);
			return orderCreateInJusPayWsDto;
		}
		else
		{
			try
			{
				final CartModel cart = mplPaymentWebFacade.findCartValues(cartID);
				if (null != cart)
				{
					final CustomerModel customerModel = mplPaymentWebFacade.getCustomer(userId);

					final String juspayMerchantId = !getConfigurationService().getConfiguration()
							.getString(MarketplacecommerceservicesConstants.MARCHANTID).isEmpty()
									? getConfigurationService().getConfiguration()
											.getString(MarketplacecommerceservicesConstants.MARCHANTID)
									: "No juspayMerchantKey is defined in local properties";
					final String juspayReturnUrl = !getConfigurationService().getConfiguration()
							.getString(MarketplacecommerceservicesConstants.RETURNURL).isEmpty()
									? getConfigurationService().getConfiguration()
											.getString(MarketplacecommerceservicesConstants.RETURNURL)
									: "No juspayReturnUrl is defined in local properties";

					String juspayOrderId;
					juspayOrderId = mplPaymentFacade.createJuspayOrder(cart, firstName, lastName, addressLine1, addressLine2,
							addressLine3, country, state, city, pincode,
							cardSaved + MarketplacewebservicesConstants.STRINGSEPARATOR + sameAsShipping, juspayReturnUrl,
							customerModel.getUid(), MarketplacewebservicesConstants.CHANNEL_MOBILE);
					LOG.debug("********* Created juspay Order mobile web service *************" + juspayOrderId);

					orderCreateInJusPayWsDto.setJuspayMerchantId(juspayMerchantId);
					orderCreateInJusPayWsDto.setJuspayReturnUrl(juspayReturnUrl);
					orderCreateInJusPayWsDto.setJuspayOrderId(juspayOrderId);
					//final CartModel cartModel = mplPaymentWebDAO.findCartValues(cartID);
					try
					{
						boolean isAuditCreated = false;
						isAuditCreated = getMplPaymentService().createEntryInAudit(juspayOrderId,
								MarketplacewebservicesConstants.CHANNEL_MOBILE, cart.getGuid());
						LOG.debug("********* Juspay audit created mobile web service *************" + isAuditCreated);
					}
					catch (final Exception ex)
					{
						LOG.error("Audit Entry: " + ex);
					}

				}
				else
				{
					orderCreateInJusPayWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					orderCreateInJusPayWsDto.setError(MarketplacecommerceservicesConstants.CART_NOT_FOUND);
					return orderCreateInJusPayWsDto;
				}

			}
			catch (final AdapterException ex)
			{
				LOG.error(UsersController.LOG, ex);
				orderCreateInJusPayWsDto.setError(MarketplacewebservicesConstants.JUSPAY_CONN_ERROR);
			}
			catch (final EtailNonBusinessExceptions ex)
			{
				// Error message for All Exceptions
				ExceptionUtil.etailNonBusinessExceptionHandler(ex);
				if (null != ex.getErrorMessage())
				{
					orderCreateInJusPayWsDto.setError(ex.getErrorMessage());
					orderCreateInJusPayWsDto.setErrorCode(ex.getErrorCode());
				}
			}
			catch (final EtailBusinessExceptions ex)
			{
				// Error message for All Exceptions
				ExceptionUtil.etailBusinessExceptionHandler(ex, null);
				if (null != ex.getErrorMessage())
				{
					orderCreateInJusPayWsDto.setError(ex.getErrorMessage());
					orderCreateInJusPayWsDto.setErrorCode(ex.getErrorCode());
				}
			}
			catch (final Exception ex)
			{
				// Error message for All Exceptions
				if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
				{
					orderCreateInJusPayWsDto.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
					orderCreateInJusPayWsDto.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
				}
			}
		}
		return orderCreateInJusPayWsDto;
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

			final String profileUpdatePath = MarketplacecommerceservicesConstants.ContextURI + specificUrl;
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


	// Getter Setter

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
	public void setAddressReversePopulator(final Populator<AddressData, AddressModel> addressReversePopulator)
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

}
