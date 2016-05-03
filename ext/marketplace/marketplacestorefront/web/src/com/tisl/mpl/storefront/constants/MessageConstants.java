/**
 *
 */
package com.tisl.mpl.storefront.constants;


/**
 * @author TCS
 *
 */
public final class MessageConstants
{

	private MessageConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public static final String EXCEPTION_IS = "Exception is : ";
	public static final String SYSTEM_ERROR_PAGE_BUSINESS = "system.error.page.business";
	public static final String SYSTEM_ERROR_PAGE_NON_BUSINESS = "system.error.page.non.business";
	public static final String NON_BUSINESS_ERROR = "Non Business Error";
	public static final String BUSINESS_ERROR = "Business Error";
	public static final String BREADCRUMB_NOT_FOUND = "breadcrumb.not.found";
	public static final String SYSTEM_PDP_ERROR_PAGE_NON_BUSINESS = "Sorry!We are unable to open the product";
	public static final String MOBILE_PREFIX_IN_91 = "+91 - ";
	public static final String FORM_GLOBAL_ERROR = "form.global.error";
	public static final String REGISTRATION_FAILED = "registration failed: ";
	public static final String NEWLY_CREATED = "Newly created : ";

	public static final String SOCIAL_LOGIN_GOOGLEAPI_CLIENT_ID = "social_login_googleapi_client_id";
	public static final String SOCIAL_LOGIN_GOOGLEAPI_CLIENT_SECRET = "social_login_googleapi_client_secret";
	public static final String SOCIAL_LOGIN_GOOGLEAPI_URI_USER_INFO_PROFILE = "social_login_googleapi_uri_user_info_profile";
	public static final String SOCIAL_LOGIN_GOOGLEAPI_URI_USER_INFO_EMAIL = "social_login_googleapi_uri_user_info_email";
	public static final String SOCIAL_LOGIN_PROXY_ENABLED = "social_login_proxy_enabled";
	public static final String SOCIAL_LOGIN_PROXY = "social_login_proxy";
	public static final String SOCIAL_LOGIN_PROXY_PORT = "social_login_proxy_port";
	public static final String SOCIAL_LOGIN_CALLBACK_URI = "social_login_callback_uri";
	public static final String SOCIAL_LOGIN_GOOGLEAPI_URI_USER_INFO_OAUTH2 = "social_login_googleapi_uri_user_info_oauth2";
	public static final String APPLICATION_JSON = "application/json";
	public static final String TRUE_FLAG = "true";
	public static final String GOOGLE_PREFFIX = "google;";

	public static final String SITEADMIN_UID = "siteadmin";
	public static final String SITEADMIN_PWD = "1qaz@WSX3edc";

	public static final String INSIDE_METHOD_GOOGLE = "Inside getUserInfoJson( ) Google";
	public static final String INSIDE_METHOD_GOOGLE_CREDENTIAL = "Inside getUserInfoJson( ) Google Credential to be created";
	public static final String FACEBOOK_URL_PREFIX = "https://graph.facebook.com/me?";
	public static final String RESPONSE_CODE_FROM_FB = "response Code from FB";
	public static final String ERROR_GETTING_FB_GRAPH = "ERROR in getting FB graph data";

	public static final String SOCIAL_LOGIN_FB_APP_ID = "social_login_fb_app_id";
	public static final String SOCIAL_LOGIN_FB_URI_DIALOG_OAUTH2 = "social_login_fb_uri_dialog_oauth2";
	public static final String CLIENT_ID = "client_id=";
	public static final String AMPERSAND_REDIRECT_URI = "&redirect_uri=";
	public static final String UTF_8 = "UTF-8";
	public static final String AMPERSAND_SCOPE_EQUALS_TO_EMAIL = "&scope=email";
	public static final String AMPERSAND_CODE_IS_EQUALS = "&code=";
	public static final String UNABLE_TO_CONNECT_WITH_FACEBOOK = "Unable to connect with Facebook";
	public static final String ERROR_ACCESS_TOKEN_INVALID = "ERROR: Access Token Invalid";
	public static final String SOCIAL_LOGIN_FB_SECRET_ID = "social_login_fb_secret_id";
	public static final String SOCIAL_LOGIN_FB_URI_OAUTH_ACCESS_TOKEN = "social_login_fb_uri_oauth_access_token";
	public static final String AMPERSAND_CLIENT_SECRET = "&client_secret=";
	public static final String INVALID_CODE_RECEIVED = "Invalid code received";

	public static final String ORDER_NOT_EXIST_OR_INVALID = "Attempted to load a order that does not exist or is not visible";

	//Key constants
	public static final String TEXT_ACCOUNT_ORDERHISTORY = "text.account.orderHistory";
	public static final String TEXT_ACCOUNT_ORDER_ORDERBREADCRUMB = "text.account.order.orderBreadcrumb";
	public static final String TEXT_ACCOUNT_ORDER_TICKET_SUCCESS = "text.account.order.ticket.success";
	public static final String TEXT_ACCOUNT_ORDER_TICKET_FAILURE = "text.account.order.ticket.failure";
	public static final String DEFAULT_INVOICE_URL = "default.invoice.url";
	public static final String TEXT_ACCOUNT_ORDER_INVOICE_SUCCESS = "text.account.order.invoice.success";
	public static final String TEXT_ACCOUNT_ORDER_INVOICE_FAILURE = "text.account.order.ticket.failure";
	public static final String TEXT_ACCOUNT_PROFILE = "text.account.profile";
	public static final String TEXT_ACCOUNT_PROFILE_NICKNAME = "text.account.profile.update.nickname";

	public static final String VALIDATION_CHECKEMAIL_EQUALS = "validation.checkEmail.equals";
	public static final String TEXT_ACCOUNT_PROFILE_CONFIRMATION_UPDATED = "text.account.profile.confirmationUpdated";
	public static final String PROFILE_EMAIL_UNIQUE = "profile.email.unique";
	public static final String PROFILE_CURRENTPASSWORD_INVALID = "profile.currentPassword.invalid";
	public static final String REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE = "registration.error.account.exists.title";
	public static final String TEXT_ACCOUNT_PROFILE_UPDATE_PASSWORD_FORM = "text.account.profile.updatePasswordForm";
	public static final String VALIDATION_CHECKPWD_EQUALS = "validation.checkPwd.equals";
	public static final String TEXT_ACCOUNT_CONFIRMATION_PASSWORD_UPDATED = "text.account.confirmation.password.updated";
	public static final String TEXT_ACCOUNT_ADDRESSBOOK = "text.account.addressBook";
	public static final String TEXT_ACCOUNT_ADDRESSBOOK_ADDEDITADDRESS = "text.account.addressBook.addEditAddress";
	public static final String ACCOUNT_CONFIRMATION_ADDRESS_ADDED = "account.confirmation.address.added";
	public static final String ACCOUNT_CONFIRMATION_ADDRESS_REMOVED = "account.confirmation.address.removed";
	public static final String ACCOUNT_CONFIRMATION_DEFAULT_ADDRESS_CHANGED = "account.confirmation.default.address.changed";

	public static final String TEXT_ACCOUNT_PAYMENTDETAILS = "text.account.paymentDetails";
	public static final String TEXT_ACCOUNT_PROFILE_PAYMENTCART_REMOVED = "text.account.profile.paymentCart.removed";
	public static final String TEXT_ACCOUNT_WISHLIST = "text.account.wishlist";
	public static final String TOTAL_NO_WISHLIST = "total number of wishlist are : ";
	public static final String SYSTEM_ERROR_DUPLICATE_WISHLIST_NAME = "system.error.duplicate.wishlist.name";
	public static final String SYSTEM_ERROR_EMPTY_WISHLIST_NAME = "system.error.empty.wishlist.name";

	public static final String SYSTEM_ERROR_WISHLIST_CREATION_PRODUCT_ADD_SUCCESS = "system.error.wishlist.creation.product.add.success";
	public static final String COULD_NOT_CREATE_WISHLIST = "Could not create Wishlist";
	public static final String SYSTEM_ERROR_WISHLIST_PRODUCT_ADD_SUCCESS = "system.error.wishlist.product.add.success";
	public static final String NO_WISHLIST_PRESENT_WITH_NAME = "No wishlist present with name : ";
	public static final String SYSTEM_ERROR_WISHLIST_NAME_EDIT_SUCCESS = "system.error.wishlist.name.edit.success";
	public static final String SYSTEM_ERROR_WISHLIST_PRODUCT_REMOVE_SUCCESS = "system.error.wishlist.product.remove.success";
	public static final String TEXT_ACCOUNT_FRIENDSINVITE = "text.account.friendsInvite";
	public static final String TEXT_ACCOUNT_FRIENDSINVITE_EMAILINVALID = "text.account.friendsInvite.emailInvalid";
	public static final String ACCOUNT_CONFIRMATION_FRIENDSINVITE_INVITESENT = "account.confirmation.friendsInvite.invitesent";
	public static final String ACCOUNT_CONFIRMATION_FRIENDSINVITE_ALREADY_REGISTERED = "account.confirmation.friendsInvite.already.registered";
	public static final String ACCOUNT_CONFIRMATION_FRIENDSINVITE_ERROR = "account.confirmation.friendsInvite.error";
	public static final String HEADER_LINK_LOGIN = "header.link.login";
	public static final String LOGIN_ERROR_ACCOUNT_NOT_FOUND_TITLE = "login.error.account.not.found.title";
	public static final String REGISTRATION_CONFIRMATION_MESSAGE_TITLE = "registration.confirmation.message.title";
	public static final String GOT_WISHLIST_WITH_NAME = "got wishlist with name ";
	public static final String WISHLIST_NAME_HAS_BEEN_CHANGED = "Wishlist name has been changed\t";


	public static final String ACCOUNT_CONFIRMATION_FORGOTTEN_PASSWORD_MOBILE = "account.confirmation.forgotten.password.mobile ";
	public static final String LOGIN_PW_PASSWORDOTP = "/login/pw/passwordOTP";
	public static final String ACCOUNT_CONFIRMATION_INVALID_USER = "account.confirmation.invalid.user";

	public static final String SYSTEM_ERROR_PAGE_NON_BUSINESS1 = "system.error.page.non.business1";
	public static final String MESSAGE_SUCCESS = "/?message=success";
	public static final String ACCOUNT_CONFIRMATION_FORGOTTEN_PASSWORD_LINK_SENT = "account.confirmation.forgotten.password.link.sent";
	public static final String DOES_NOT_EXIST_IN_THE_DATABASE = "does not exist in the database.";
	public static final String PROFILE_OTPNUMBER_INVALID = "profile.OTPNumber.invalid";
	public static final String LOGIN_PW_CHANGE = "/login/pw/change";
	public static final String TOKEN = "?token=";
	public static final String UPDATEPWD_TOKEN_INVALID = "updatePwd.token.invalid";
	public static final String ACCOUNT_CONFIRMATION_PASSWORD_UPDATED = "account.confirmation.password.updated";
	public static final String ACCOUNT_CONFIRMATION_PASSWORD_ENTERUNIQUEPASSWORD = "account.confirmation.password.enteruniquepassword";
	public static final String ACCOUNT_CONFIRMATION_OTP_GENERATED = "account.confirmation.otp.generated";


	public static final String MY_ACCOUNT_OVERVIEW = "Overview";
	public static final String RETURN_SUBMIT = "Return Submit";
	public static final String RETURN_REQUEST = "Return Request";
	public static final String MARKETPLACE_PREFERENCE = "text.account.marketplacePreference";
	public static final String LOGIN_COOKIE_EXPIRY_DAY = "login.cookie.expiry.day";
	public static final String MY_QUESTIONARE = "My Recommendation";
	public static final String MY_STYLE_PROFILE = "My Style Profile";

	public static final String SHOW_CHECKOUT_STRATEGY_OPTIONS = "storefront.show.checkout.flows";
	public static final String MINIMUM_CONFIGURED_QUANTIY = "mpl.cart.minimumConfiguredQuantity.lineItem";
	public static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";
	public static final String MINIMUM_GIFT_QUANTIY = "mpl.cart.giftQuantity.lineItem";
	public static final String ERROR_QUANTITY_INVALID = "basket.error.quantity.invalid";

	public static final String MY_MARKETPLACE = "My Tata CLiQ";
	public static final String INVITE_FRIENDS_MESSAGE_KEY = "account.invite.friends.default.message.text";
	public static final String LOGIN_PAGE_TITLE = "Sign In or Register";
	public static final String TEXT_ACCOUNT_PREFERENCE_SAVE_SUCCESS = "text.account.preference.save.success";
	public static final String TEXT_ACCOUNT_PREFERENCE_SAVE_FAILURE = "text.account.preference.save.failure";
	public static final String TEXT_ACCOUNT_COUPONDETAILS = "text.account.couponsDetails";

	//Rating Review
	public static final String USE_GIGYA = "gigya.use".intern();
	public static final String NO = "N";
	public static final String GIGYA_ERROR = "Gigya Service Switch is Set OFF or not SET";
	public static final String COOKIE_ERROR = "Error in Setting Cookie-:Cookie Data NULL";
	public static final String SHOW_ORDERS_FROM = "order.history.show.orders.from";
	public static final String RETURN_REQUEST_LOCALE = "order.history.return.request";

	public static final String CANCEL_STATUS = "valid.order.statuses.CANCELLATION";
	public static final String CANCEL_ORDER_STATUS = "valid.order.statuses.CANCELLATION_ORDER";
	public static final String WISHLIST_PAGESIZE = "wishlist.pagesize.defaultValue";
	public static final String ORDER_HISTORY_PAGESIZE = "orderHistory.pagesize.defaultValue";
	public static final String TWITTER_HANDLE = "twitter.handle";
	public static final String MEDIA_HOST = "media.dammedia.host";
	public static final String MEDIA_CODE = "brand.media.code";
	public static final String SITE_NAME = "site.name";
	public static final String EMAIL_URL = "update_Email_url";

	//Compare
	public static final String COMPARE_SYSTEM_ERROR = "Sorry! we are unable to compare the selected products";

	//Contact Us
	public static final String CONTACT_SYSTEM_ERROR = "Sorry! we are unable to process your request";
	//Gigya Rating Review
	public static final String GIGYA_RR_URL = "gigya.rating.url".intern();
	public static final String BOGO_CANCEL = "cancel.bogo.message".intern();
	public static final String ORDER_HISTORY_PAGEBLEDATA_COUNT = "orderHistory.pagebleData.count";

	//coupons
	public static final String PAZE_SIZE_COUPONS = "mpl.account.coupon.pazesize";
	//closed coupon
	public static final String PAZE_SIZE_VOUCHER = "mpl.account.voucher.pazesize";
	//reviews
	public static final String TEXT_ACCOUNT_REVIEWS = "text.account.reviews";
	public static final String PAZE_SIZE = "mpl.account.review.pazesize";
	public static final String ORDER_CAROUSEL_SIZE = "mpl.account.review.orderCarousel.size";
}
