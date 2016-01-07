/**
 *
 */
package com.tisl.mpl.constants.clientservice;

/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class MarketplacecclientservicesConstants
{



	public static final String DMY_DATE_FORMAT = "dd/MM/yyyy";
	public static final String MALE = "Male";
	public static final String FEMALE = "Female";
	public static final String EMPTY = "";
	public static final String HOME = "Wome";
	public static final String WORK = "Work";
	public static final String EXCEPTION_IS = "Exception is : ";
	public static final String CUSTOMER_UPDATE_INDICATOR = "U";
	public static final String TRUE = "true";
	public static final String X = "X";

	public static final String SMS_SENDER_TAG = "Sender_ID";
	public static final String SMS_MOBILE_NO_TAG = "Mobile_No";
	public static final String SMS_MESSAGE_TAG = "Message";
	public static final String SMS_ECOMMREQ_TAG = "SMSECommReq";
	public static final String SMS_MESSAGE_BLANK_SPACE_ONE = "|";
	public static final String SMS_MESSAGE_BLANK_SPACE_TWO = "%20";

	public static final String SMS_SERVICE_ENABLED = "marketplace.sms.service.enabled";
	public static final String SMS_SERVICE_URL = "marketplace.sms.service.url";
	public static final String B9044 = "B9044";
	public static final String SMS_SERVICE_USERNAME = "marketplace.sms.service.username";
	public static final String SMS_SERVICE_PASSWORD = "marketplace.sms.service.password";
	public static final String OMS_INVENTORY_RESERV_URL = "oms.inventoryreservation.url";
	public static final String M = "M";
	public static final String F = "F";
	public static final String U = "U";
	public static final String UNKNOWN = "Unknown";
	public static final String SOCIAL_DEV_LOGIN_PROXY = "proxy.address".intern();
	public static final String SOCIAL_DEV_PUSH_NOTIFICATION_PROXY = "proxy.address".intern();
	public static final String SOCIAL_DEV_LOGIN_PROXY_PORT = "proxy.port".intern();
	public static final String PUSH_NOTIFICATIONS_ACCESS_KEY = "accessKey".intern();
	public static final String PUSH_NOTIFICATIONS_SECRET_KEY = "secretKey".intern();
	public static final String PUSH_NOTIFICATIONS_AMAZON_SNS_URL = "push.notification.amazon.sns.url".intern();
	public static final String PUSH_NOTIFICATIONS_APP_NAME = "push.notifications.app.name".intern();
	public static final String PUSH_NOTIFICATIONS_SERVER_API_KEY = "push.notifications.server.api.key".intern();
	public static final String PUSH_NOTIFICATIONS_PLATFORM_ERROR = "push.notification.platform.error".intern();
	public static final String PUSH_NOTIFICATIONS_APNS_CERTIFICATE = "push.notification.apnsCertificate".intern();
	public static final String PUSH_NOTIFICATIONS_APNS_PRIVATE_KEY = "push.notification.apnsPrivateKey".intern();
	public static final String PUSH_NOTIFICATIONS_APNS_APP_NAME = "push.notification.apnsApplicationName".intern();
	public static final String PUSH_NOTIFICATIONS_APNS_SANDBOX_CERTIFICATE = "push.notification.apns.sandbox.certificate".intern();
	public static final String PUSH_NOTIFICATIONS_APNS_SANDBOX_PRIVATE_KEY = "push.notification.apns.sandbox.privateKey".intern();
	public static final String PUSH_NOTIFICATIONS_APNS_SANDBOX_APP_NAME = "push.notification.apns.sandbox.applicationName"
			.intern();
	public static final String PUSH_NOTIFICATIONS_APNS_DEVICE_KEY = "push.notification.apnsDeviceToken".intern();
	public static final String PUSH_NOTIFICATIONS_GCM_DEVICE_KEY = "push.notification.gcm.DeviceToken".intern();
	public static final String PUSH_NOTIFICATIONS_ERROR = "Push notifications error".intern();
	public static final String PUSH_NOTIFICATIONS_CUST_DETAIL_ERROR = "Error in fetching customer details for the given original id"
			.intern();
	public static final String PUSH_NOTIFICATIONS_ASE_ERROR = "Caught an AmazonServiceException, which means your request made it to Amazon SNS, but was rejected with an error response for some reason"
			.intern();
	public static final String PUSH_NOTIFICATIONS_ACE_ERROR = "Caught an AmazonClientException, which means the client encountereda serious internal problem while trying to communicate with SNS, such as not being able to access the network."
			.intern();
	public final static String PROXYENABLED = "proxy.enabled";

	public static final String URLFIRSTPHASE = "oms.pincodeserviceability.firstxmlphase";
	public static final String URLSECONDPHASE = "oms.pincodeserviceability.secondxmlphase";
	public static final String URLTHIRDPHASE = "oms.pincodeserviceability.thirdxmlphase";


	public static final String URLSOFTFIRSTPHASE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?><inventoryReservationResponseDTO>";
	public static final String URLSOFTSECONDPHASE = "<Item><ussId>S000030000000000000001</ussId><reservationStatus>success</reservationStatus></Item>";
	public static final String URLSOFTTHIRDPHASE = "</inventoryReservationResponseDTO>";
	public static final String PIN_CODE_DELIVERY_MODE_OMS_URL = "oms.pincode.serviceabilility.url";
	public static final String PIN_CODE_DELIVERY_MODE_OMS_MOCK = "oms.pincodeserviceability.realtimecall";
	public static final String OMS_SOFT_RESERV_URL = "oms.softReservation.duration";
	public static final String CRM_CUSTOMER_CREATEUPDATE_URL = "crm.customer.createupdate.url";
	public static final String CUSTOMERMASTER_ENDPOINT_USERID = "customerMaster.endpoint.userId";
	public static final String CUSTOMERMASTER_ENDPOINT_PASSWORD = "customerMaster.endpoint.password";

	public static final String RATING_SECRETKEY = "gigya.secretkey".intern();
	public static final String RATING_PROXY_SET = "proxy.enabled".intern();
	public static final String RATING_APIKEY = "gigya.apikey".intern();
	public static final String RATING_PROXY = "proxy.address".intern();
	public static final String RATING_PROXY_PORT = "proxy.port".intern();
	public static final String RATING_PROXY_ENABLED = "proxy.enabled".intern();
	public static final String HTTPS_PROXYSET = "https.proxySet";
	public static final String HTTPS_PROXYHOST = "https.proxyHost";
	public static final String HTTPS_PROXYPORT = "https.proxyPort";
	public static final String SERVICE_SWITCH_YES = "Y";
	public static final String SPLIT_AT = "@";
	public static final String FIRST_NAME = "firstName";
	public static final String METHOD_NOTIFY_LOGIN = "gigya.login.method".intern();
	public static final String METHOD_LOGOUT = "gigya.logout.method".intern();
	public static final String PARAM_SITEUID = "siteUID";
	public static final String PARAM_ISNEWUSER = "newUser";
	public static final String PARAM_USERINFO = "userInfo";
	public static final boolean PARAM_USEHTTPS = true;
	public static final String GIGYA_DOMAIN = "gigya.domain";
	public static final String WAIT_RESPONSE = "Waiting for Response from Gigya";
	public static final String RESPONSE_PARAM_COOKIENAME = "cookieName";
	public static final String RESPONSE_PARAM_COOKIEVALUE = "cookieValue";
	public static final String RESPONSE_PARAM_COOKIEDOMAIN = "cookieDomain";
	public static final String RESPONSE_PARAM_COOKIEPATH = "cookiePath";
	public static final String ERROR_RESPONSE = "Error In Response from Gigya:";
	public static final String NULL_RESPONSE = "Response is Null";
	public static final String CHECK_PROPERTIES_FILE = "Please Check Properties File-Missing Required Parameters";
	public static final String ASSOCIATED_KEYS = "Associated Key's :";
	public static final String KEY_NOT_FOUND = "Key Not Found";
	public static final String TICKET_CREATE_URL = "ticket.create.crm.url";
	public static final String CANCEL = "C";
	public static final String JAXB_EXCEPTION = "Jaxb Exception";
	public static final String EXCEPTION = "Exception after calling web service";
	public static final String PUSH_NOTIFICATION_LOCAL_SETUP = "push.notification.local.setup";

	// Inventory Soft reservation
	public static final String OMS_INVENTORY_RESV_TYPE_CART = "cart";
	public static final String OMS_INVENTORY_RESV_TYPE_PAYMENT = "payment";
	public static final String OMS_INVENTORY_RESV_TYPE_ORDERHELD = "orderheld";
	public static final String OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE = "orderDeallocate";
	public static final String OMS_INVENTORY_RESV_DURATION_CART = "oms.inventory.reservation.cart.duration";
	public static final String OMS_INVENTORY_RESV_DURATION_PAYMENT = "oms.inventory.reservation.payment.duration";
	public static final String OMS_INVENTORY_RESV_DURATION_ORDERHELD = "oms.inventory.reservation.order.held.duration";
	public static final String OMS_INVENTORY_RESV_DURATION_ORDERDEALLOCATE = "oms.inventory.reservation.order.deallocate.duration";
	public static final String OMS_INVENTORY_RESV_REALTIMECALL = "oms.inventory.reservation.realtimecall";
	public static final String OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLFIRSTPHASE = "oms.inventory.reservation.mock.urlfirstxmlphase";
	public static final String OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLSECONDPHASE = "oms.inventory.reservation.mock.urlsecondxmlphase";
	public static final String OMS_INVENTORY_RESV_REALTIMECALL_MOCK_URLTHIRDPHASE = "oms.inventory.reservation.mock.urlthirdxmlphase";
	public static final String OMS_INVENTORY_RESV_SUCCESS = "SUCCESS";
	public static final String Y = "Y";
	public static final String N = "N";
	public static final String OMS_INVENTORY_RESV_FAILURE_MESSAGE = "We are unable to Reserve Stock, please Retry!";
	public static final String OMS_INVENTORY_RESV_SESSION_ID = "inventoryReservationFailed";
	public static final String OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE = "One of your product(s) in cart is not serviceable for selected pincode";
	public static final String OMS_PINCODE_SERVICEABILTY_MSG_SESSION_ID = "pincodeVerificationFailed";
	public static final String OMS_PINCODE_REQUEST_TYPE_GET = "GET";
	public static final String OMS_PINCODE_REQUEST_TYPE_POST = "POST";
	public static final String DATE_FORMAT_AWB = "E dd MMM";
	public static final String TIME_FORMAT_AWB = "hh:mm a";

	public static final String DELIVERY_MODE_ENTER_STEP_ERROR_ID = "deliveryModeEnterStepError";
	public static final String DELIVERY_MODE_ENTER_STEP_ERROR_MESSAGE = "Something went wrong. Please try again.";

	public static final String GENERATE_ORDER_SEQUENCE = "numberseries.order.sequence.applied";

	//Gigya Social Login
	public static final String METHOD_NOTIFY_REGISTRATION = "gigya.notifyregistration.method".intern();
	public static final String LAST_NAME = "lastName";
	public static final String GIGYA_METHOD_LINK_ACCOUNTS = "gigya.linkaccount.method".intern();
	//account page review
	public static final String PROXY_HOST = "http.proxyHost";
	public static final String PROXY_PORT = "http.proxyPort";
	public static final String CATEGORY_ID = "categoryID";
	public static final String STREAM_ID = "streamID";
	public static final String COMMENT_ID = "commentID";
	public static final String SENDER_UID = "senderUID";
	public static final String COMMENT_TEXT = "commentText";
	public static final String COMMENT_TITLE = "commentTitle";
	public static final String RATINGS = "ratings";
	public static final String UID = "UID";

	public static final String REVIEWS_CATEGORYID_EXCEPTION = "Gigya 'getReviewsByCategoryProductId' for UID error ";
	public static final String REVIEWS_UID_EXCEPTION = "Gigya 'getReviewsByUID'  UID error";
}
