/**
 *
 */
package com.tisl.mpl.constants;

import java.util.HashMap;
import java.util.Map;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class MarketplacewebservicesConstants
{



	//Mobile registration
	public static final String DUPLICATE_UID = "registration.error.account.exists.mobile".intern();
	public static final String MODEL_SAVE_ERROR = "mpl.register.model.save.error".intern();
	public static final String ETAIL_BUSINESS_EXCEPTION = "mpl.etail.business.error".intern();
	public static final String ETAIL_NON_BUSINESS_EXCEPTION = "mpl.etail.non.business.error".intern();
	public static final String OTHER_EXCEPTION = "mpl.other.error.mobile".intern();
	public static final String LONG_EMAIL_EXCEPTION = "register.email.invalid.long".intern();
	public static final String INVALID_EMAIL_MOBILE = "register.email.invalid".intern();
	public static final String INVALID_PASSWORD_MOBILE = "register.pwd.invalid".intern();
	public static final String INVALID_PASSWORD_POLICY = "register.pwd.invalid.pp".intern();
	public static final String LONG_PASSWORD_EXCEPTION = "register.pwd.invalid.long".intern();
	public static final String INVALID_SPACE_PASSWORD = "register.pwd.invalid.space".intern();
	public static final String USER_NOT_FOUND = "mobile.user.not.found".intern();
	public static final String USER_DETAILS_EXPIRED = "mobile.user.details.expired".intern();
	public static final String MOBILE_WRONG_PASSWORD = "mobile.user.wrong.password".intern();
	public static final String EXCEPTION_LOGIN = "mobile.user.other.excpetion".intern();
	public static final String MOBILE_AUTH_EXCEPTION = "mobile.auth.exception".intern();
	public static final String MOBILE_AUTH_SERVICE_EXCEPTION = "mobile.auth.service.exception".intern();
	public static final String MOBILE_DATA_ACCESS_EXCEPTION = "mobile.data.access.exception".intern();
	//Mobile all cat and brands
	public static final String DEFAULTCATALOGID = "cronjob.promotion.catelog".intern();
	public static final String DEFAULTCATALOGVERISONID = "cronjob.promotion.catalogVersionName".intern();
	public static final String SALESROOTCATEGORY = "marketplace.mplcatalog.sales.rootcategory.code".intern();
	public static final String SHOPBYBRANDCOMPONENT = "marketplace.mplcatalog.shopbybrand.component.id".intern();
	public static final String SHOPBYDEPTCOMPONENT = "marketplace.mplcatalog.shopbydept.component.id".intern();
	public static final String SALESCLOTH = "marketplace.mplcatalog.sales.cloth.id".intern();
	public static final String SALESMEN = "marketplace.mplcatalog.sales.men.id".intern();
	public static final String SALESWOMEN = "marketplace.mplcatalog.sales.women.id".intern();
	public static final String SALESELECTRONICS = "marketplace.mplcatalog.sales.electronics.id".intern();
	public static final String NULL_USER_DETAILS = "UserDetailsService returned null, which is an interface contract violation"
			.intern();
	//Mobile ProductDetailsForProductCode
	public static final String CONFIGURABLE_ATTRIBUTE = "classification.attributes.".intern();
	//social media registration
	public static final String EMAIL = "email".intern();
	public static final String SOCIAL_EMAIL_TOKEN_ERROR = "mobile.social.email.token.error".intern();
	public static final String FACEBOOK = "facebook".intern();
	public static final String GOOGLEPLUS = "googleplus".intern();
	public static final String INVALID_SOCIAL_MEDIA = "mobile.invalid.social.media".intern();
	public static final String FB_GRAPH_PARSE_ERROR = "ERROR in parsing FB graph data".intern();
	public static final String FB_GRAPH_GET_ERROR = "ERROR in getting FB graph data".intern();
	public static final String GOOGLE_AUTH_ERROR = "ERROR in getting data from google".intern();
	public static final String FB_RESPONSE_CODE = "response Code from FB".intern();
	public static final String SOCIAL_RESPONSE_CODE_ERROR_MSG = "invalid.request.social".intern();
	public static final String SOCIAL_RESPONSE_CODE_400_ERROR = "Server returned HTTP response code: 400 for URL".intern();
	public static final String SOCIAL_RESPONSE_CODE_401_ERROR = "Server returned HTTP response code: 401 for URL".intern();
	public static final String SOCIAL_RESPONSE_CODE_403_ERROR = "Server returned HTTP response code: 403 for URL".intern();
	public static final String USER_DETAILS = "User Details.....".intern();
	public static final String SOCIAL_LOGIN_GOOGLEAPI_URI_USER_INFO_OAUTH2 = "social_login_googleapi_uri_user_info_oauth2"
			.intern();
	public static final String APPLICATION_JSON = "application/json".intern();
	public static final String SOCIAL_LOGIN_GOOGLEAPI_CLIENT_ID = "social_login_googleapi_client_id".intern();
	public static final String SOCIAL_LOGIN_GOOGLEAPI_CLIENT_SECRET = "social_login_googleapi_client_secret".intern();
	public static final String SOCIAL_LOGIN_PROXY_ENABLED = "social_login_proxy_enabled".intern();
	public static final String SOCIAL_LOGIN_PROXY = "social_login_proxy".intern();
	public static final String SOCIAL_LOGIN_PROXY_PORT = "social_login_proxy_port".intern();
	public static final String SOCIAL_DEV_LOGIN_PROXY = "proxy.address".intern();
	public static final String SOCIAL_DEV_LOGIN_PROXY_PORT = "proxy.port".intern();
	public static final String SITEADMIN_UID = "siteadmin".intern();
	public static final String SITEADMIN_PWD = "1qaz@WSX3edc".intern();
	public static final String SOCIAL_GOOGLEAPI_ME_INFO = "social_login_googleapi_uri_me_info_mobile".intern();
	public static final String SOCIAL_FBAPI_ME_FIELDS = "social_login_fb_fields".intern();
	public static final String SOCIAL_FB_ME_INFO = "social_login_fb_uri_me_info_mobile".intern();
	public static final String VALUE = "value".intern();
	public static final String SOCIAL_FB_EMAIL = "email".intern();
	public static final String SOCIAL_GOOGLE_EMAIL = "emails".intern();
	public static final String SOCIAL_GOOGLE_USERID = "id".intern();
	public static final String SOCIALMEDIAINDICATOR = "There was an error while saving social media indicator for a particular customer"
			.intern();
	public static final String USER_IN_SESSION_ERROR = "There was an error while setting user in session for the user:".intern();
	public static final String SAVE_DEVICE_INFO_ERROR = "save.device.mobile.error".intern();
	public static final String EMAILID_DEVICE_NON_EXISTANT = "save.device.mobile.email.error".intern();

	// Cart Model Query
	public static final String CARTQUERY = "select {c:pk} from {Cart As c} where {c.code}=?cartID".intern();
	public static final String CARTID = "cartID".intern();

	public static final String CARTGUIDQUERY = "select {c:pk} from {Cart As c} where {c.guid}=?guid".intern();
	public static final String GUID = "guid".intern();

	//PaymentMode Query
	public static final String PAYMENTMODETYPE = "select {b:pk} from {PaymentType As b} where {b.mode}=?mode";

	//Country ISO Query
	public static final String COUNTRYISOQUERY = "select {c:pk} from {Country As c} where {c.name}=?countryName".intern();
	public static final String COUNTRYNAME = "countryName".intern();

	//Billing Address Query
	public static final String BILLINGADDRESSQUERY = "select {a:pk} from {SavedCard As a} where {a.cardreferencenumber}=?cardRefNo"
			.intern();
	public static final String SAVEDCARDFORCUSTOMERQUERY = "Select {s:pk} from {SavedCard as s},{Customer as c} where {s.customer}={c.pk} and {c.originalUid}=?originalUid and {s.cardreferencenumber}=?cardRefNo"
			.intern();
	public static final String ORIGINALUID = "originalUid".intern();
	public static final String CARDREFNUMBER = "cardRefNo".intern();

	public static final String POS_QUERY_FOR_SLAVE = "select {pos:pk} from {PointOfService As pos} where {pos.slaveId}=?slaveId"
			.intern();

	public static final String POS_QUERY_FOR_POSNAME = "select {pos:pk} from {PointOfService As pos} where {pos.name}=?name"
			.intern();
	public static final String POS_SLAVEID = "slaveId".intern();

	public static final String POS_QUERY_FOR_SELLER_AND_SLAVE = "select {pos:pk} from {PointOfService As pos} where {pos.sellerId}=?sellerId and {pos.slaveId}=?slaveId"
			.intern();
	public static final String POS_SELLERID = "sellerId".intern();
	public static final String POS_NAME = "name".intern();


	public static final String COD_ELIGIBLE = "Eligible for COD";
	//	public static final String SSHIP_ELIGIBLE = "One or more items in your order are Seller Fulfilled. Hence, No COD mode is available."
	//			.intern();

	public static final String SSHIP_ELIGIBLE = "Sorry! We are unable to offer COD as some of these items will be shipped directly by the seller.Try a prepaid option?"
			.intern();

	public static final String ITEM_ELIGIBLE = "Sorry! Some of these items are not eligible for  COD. Try a prepaid option?"
			.intern();
	//	public static final String BLACKLIST = "COD is not available. Please contact Customer Care or proceed with any other Payment Mode"
	//			.intern();

	public static final String BLACKLIST = "Sorry! We are unable to offer COD. Try a prepaid option?".intern();

	public static final String WRONGCUSTOMERID = "Wrong Customer ID".intern();
	public static final String PIN_CODE_ELIGIBLE = "Sorry! We can't offer COD for this pincode. Try a prepaid option?".intern();
	// Update Transaction Details
	public static final String UPDATE_SUCCESS = "Success".intern();
	public static final String UPDATE_FAILURE = "Failure".intern();
	public static final String MODEOFPAYMENT = "Incorrect Payment Mode";
	public static final double AMOUNTTOBEDEDUCTED = 0.00D;
	public static final String FORMAT = "%.2f".intern();
	public static final String NA = "NA".intern();

	// Mode of Payment
	public static final String EMI = "EMI".intern();
	public static final String DEBIT = "Debit Card".intern();
	public static final String CREDIT = "Credit Card".intern();
	public static final String COD = "COD".intern();
	public static final String NETBANKING = "Netbanking".intern();
	public static final String WALLET = "Wallet".intern();
	public static final String PAYMENTMODE = "paymentMode".intern();

	public static final String DECIMALULLCHK = "0.0".intern();
	public static final String STRINGNULLCHK = " ".intern();

	//orderStatusResponse
	public static final String MERCHENTID = "merchant_id".intern();
	public static final String ORDERID = "order_id".intern();
	public static final String CUSTOMERID = "customer_id".intern();
	public static final String CUSTOMEREMAIL = "customer_email".intern();
	public static final String CUSTOMERPHONE = "customer_phone".intern();
	public static final String PRODUCTID = "product_id".intern();
	public static final String STATUS = "status".intern();
	public static final String STATUSID = "status_id".intern();
	public static final String AMOUNT = "amount".intern();
	public static final String CURRENCY = "currency".intern();
	public static final String REFUNDEDED = "refunded".intern();
	public static final String AMOUNTREFUNDED = "amount_refunded".intern();
	public static final String RETURNURL = "return_url".intern();
	public static final String UFD1 = "udf1".intern();
	public static final String UFD2 = "udf2".intern();
	public static final String UFD3 = "udf3".intern();
	public static final String UFD4 = "udf4".intern();
	public static final String UFD5 = "udf5".intern();
	public static final String UFD6 = "udf6".intern();
	public static final String UFD7 = "udf7".intern();
	public static final String UFD8 = "udf8".intern();
	public static final String UFD9 = "udf9".intern();
	public static final String UFD10 = "udf10".intern();
	public static final String TXNID = "txn_id".intern();
	public static final String GATEWAYID = "gateway_id".intern();
	public static final String BANKERRORCODE = "bank_error_code".intern();
	public static final String BANKERRORMESSAGE = "bank_error_message".intern();

	// Order Response

	public static final String CARD = "card".intern();
	public static final String PAYMENT_GATEWAY_RESPONSE = "payment_gateway_response".intern();
	//product detail for product code web service
	public static final String HOME_DELIVERY = "home-delivery".intern();
	public static final String EXPRESS_DELIVERY = "express-delivery".intern();
	public static final String EMI_CUT_OFF_LIMIT = "marketplace.emiCuttOffAmount".intern();

	//Card
	public static final String LASTFOURDIGITS = "last_four_digits".intern();
	public static final String CARDISIN = "card_isin".intern();
	public static final String EXPIRYMONTH = "expiry_month".intern();
	public static final String EXPIRYYEAR = "expiry_year".intern();
	public static final String NAMEOFCARD = "name_on_card".intern();
	public static final String CARDTYPE = "card_type".intern();
	public static final String CARDISSUER = "card_issuer".intern();
	public static final String CARDBRAND = "card_brand".intern();
	public static final String CARDREFNO = "card_reference".intern();
	public static final String CARDFINGERPRINT = "card_fingerprint".intern();
	public static final String USINGSAVEDCARD = "using_saved_card".intern();
	public static final String SAVEDTOLOCKER = "saved_to_locker".intern();



	//payment_gateway_response
	public static final String CREATED = "created".intern();
	public static final String EPGTXNID = "epg_txn_id".intern();
	public static final String RRN = "rrn".intern();
	public static final String AUTHIDCODE = "auth_id_code".intern();
	public static final String TXNID_P = "txn_id".intern();
	public static final String RESPCODE = "resp_code".intern();
	public static final String RESPMSG = "resp_message".intern();


	//Web Hook
	public static final String EVENTID = "id".intern();
	public static final String DATECREATED = "date_created".intern();
	public static final String EVENTNAME = "event_name".intern();
	public static final String JUSPAYORDERSTATUS = "juspay_order_status".intern();
	public static final String CONTENT = "content".intern();

	//EBS response
	public static final String RISK = "risk".intern();
	public static final String EBSRISKLEVEL = "ebs_risk_level".intern();
	public static final String EBSRISKPERCENTAGE = "ebs_risk_percentage".intern();
	public static final String EBSBINCOUNTRY = "ebs_bin_country".intern();
	public static final String EBSPAYMENTSTATUS = "ebs_payment_status".intern();
	public static final String FLAGGED = "flagged".intern();
	public static final String MESSAGE = "message".intern();
	public static final String PROVIDER = "provider".intern();
	public static final String RECOMMENDEDACTION = "recommended_action".intern();
	public static final String EBSSTATUS = "status".intern();

	// About Us Query
	public static final String ABOUTUSBANNER = "AboutUsBannerComponent".intern();
	public static final String ABOUTUSPAGE = "AboutUsComponent".intern();
	public static final String ABOUTUSCONTACTADDRESS = "ContactAddress".intern();
	public static final String WORKINGWITHUS = "WorkingWithUsMediaParagraphComponent".intern();
	public static final String TATATRUSTMEDIA = "TataTrustMediaParagraphComponent".intern();
	public static final String TATABUSINESSETHICS = "TataBusinessEthicsMediaParagraphComponent".intern();
	public static final String ABOUTUSHTTPURL = "website.hybris.http".intern();
	public static final String ABOUTUSHTTPSURL = "website.hybris.https".intern();

	//Help and Services
	public static final String CALLUS = "CallUsMediaParagraphComponent".intern();
	public static final String CHATWITHUS = "ChatWithUsMediaParagraphComponent".intern();
	public static final String AUTHNEXCLUSIVE = "AuthenticExclusiveMediaParagraphComponent".intern();
	public static final String CUSTOMERFIRST = "CustomerFirstMediaParagraphComponent".intern();
	public static final String BESTCOLLECTION = "BestCollectionMediaParagraphComponent".intern();
	public static final String DELIVERYONTIME = "DeliveryOnTimeMediaParagraphComponent".intern();
	public static final String FAQPAGECOMP = "HelpServiceFAQPageComponent".intern();
	public static final String FAQORDERCOMP = "HelpServiceOrderPageComponent".intern();
	public static final String FAQRETURNCOMP = "HelpServiceRCPageComponent".intern();

	//Push Notifications
	public static final String PLATFORM_GCM = "GCM".intern();
	public static final String PLATFORM_APNS_SANDBOX = "APNS_SANDBOX".intern();
	public static final String PLATFORM_APNS = "APNS".intern();
	public static final String INVALID_PLATFORM = "mpl.invalid.push.platform".intern();
	public static final String INVALID_ACTIVE = "mpl.invalid.push.active".intern();
	public final static String PROXYENABLED = "proxy.enabled".intern();
	public final static String SAVE_DEVICE_NO_EMAIL = "Error while fetching customer information. Called with not existing customer for email:"
			.intern();

	public final static String NOBILLINGDETAILS = "No Data for the respective Card in available".intern();
	public final static String DECIMALERROR = "Can't seem to understand the input".intern();
	public final static String PAYMENTSAVEFALIUR = "Payment Transaction or Cart Saving faliur".intern();
	public final static String PAYMENTINFOSAVINFFALIUR = "Payment Information Saving faliur".intern();
	public final static String CARTMODELEMPTY = "No Cart Details available for the mentioned Cart ID".intern();
	public final static String USEREMPTY = "No User Available".intern();
	public final static String INCORRECTINPUT = "Incorrect Input".intern();
	public final static String MISSINGREQUIREDPARAMETERS = "Missing Required Parameters".intern();
	public final static String NOPROPERDATA = "Either provide Bank Name or Bin Number".intern();
	public final static String PROVIDEBANKNAME = "Please Provide Bank Name and the Bin Number".intern();
	public final static String BINNOEMPTY = "Please Provide binNo and remove bankName".intern();
	public final static String BANKNAMEEMPTY = "Please provide bankName and remove binNo".intern();
	public final static String INPUTNULL = "No PaymentMode Provided".intern();
	public final static String ENTRYEMPTY = "No Entry available against this Cart Model".intern();
	public final static String PRODUCTMODELEMPTY = "No Product Details available for the mentioned Cart ID".intern();
	public final static String NODATAAVAILABLE = "No Data Available".intern();
	public final static String UPDATE_CARD_TRAN_FAILED = "Update card transaction details failed.".intern();
	public final static String UPDATE_COD_TRAN_FAILED = "Update card transaction details failed.".intern();
	public final static String PLACE_ORDER_FAILED = "Place order failed.".intern();
	//Rename Wish List
	public static final String DUPLICATEWISHLISTNAME = "Duplicate Wish list Name".intern();


	// Pincode Servicibility
	public static final String URL = "/{baseSiteId}/users/{userId}/checkPincode";
	public static final String CREATEENTRYINAUDITURL = "/createEntryInAudit";
	public static final String SAVEDCARDS = "/savedCards";
	public static final String REMOVESAVEDCARDS = "/removeSavedCards";
	public static final String CREATEJUSPAYORDER = "/{userId}/createJuspayOrder";
	public static final String GETPAYMENTMODE = "/getPaymentModes";
	public static final String APPORJSON = "application/json";
	public static final String EMPTY = " ";
	public static final String EXCEPTION_IS = "Exception is : ".intern();

	public static String CLOTHING = "Clothing".intern();
	public static String ELECTRONICS = "Electronics".intern();

	public final static String PAYMENTMODEFORPROMOTION = "paymentModeForPromotion".intern();
	public static final String BANKFROMBIN = "bank".intern();
	public static final String BANKFORBINQUERY = "select {b:pk} from {Bin As b} WHERE {b.binno}=?bin".intern();
	public static final String BANKFROMSAVEDCARD = "select {b.pk} from {Bank as b} where {b:bankname}=?bankName".intern();
	public static final String BINNO = "bin".intern();
	public static final String BANKNAME = "bankName".intern();
	public static final String INVALIDBINNUMBER = "Invalid Bin Number".intern();
	public static final String CORRECTBINNUMBER = "Please provide correct Bin Number".intern();
	public static final String POTENTIALPROMOTION = "PotentialPromotion".intern();
	public static final String CASHBACKFIRED = "CashBackFired".intern();
	public static final String VALIDBINNUMBER = "[0-9]+".intern();
	public static final String INVALIDDATA = "Invalid Data".intern();
	public static final String SEARCH_COMPONENT = "MplEnhancedSearchBox".intern();
	public static final String USER_ID_INVALID = "User Id Invalid";
	public static final String PIN_CODE_RESPONSE_ERROR = "pincode.response.oms.error".intern();

	public static final String PIPESEPERATOR = "\\|";
	public static final String CODELIGIBILITYURL = "/getCODEligibility";
	public static final String BINVALIDATIONURL = "/binValidation";
	public static final String APPLICATIONPRODUCES = "application/json";
	public static final String UPDATETRANSACTIONFORCARDURL = "/updateTransactionDetailsforCard";
	public static final String UPDATETRANSACTIONFORCODURL = "/updateTransactionDetailsforCOD";
	public static final String BILLINGADDRESSURL = "/getBillingAddress";

	// Null Check
	public static final String PROMOTIONDATAEMPTY = "Promotion Mode is Empty".intern();
	public static final String CARTDATAEMPTY = "No Cart Data Available".intern();
	public static final String CRDREFNONULL = "Card Reference Number is Empty".intern();
	public static final String PAYMENTMODENULL = "Payment Mode is Empty".intern();
	public static final String CARTIDNULL = "Cart ID is Empty".intern();
	public static final String CUSTOMERNAMENULL = "Customer Name is Empty".intern();
	public static final String CUSTOMERIDNULL = "Customer ID is Empty".intern();
	public static final String CARTVALUENULL = "Cart Value is Empty".intern();
	public static final String TOTALVALUENULL = "Total Cart Value is Empty".intern();
	public static final String ORDERRESNULL = "Order Response is Empty".intern();
	public static final String PROPERMODEOFPAYMENT = "Please provide proper mode of payment.".intern();

	public static final String ORDER = "order".intern();
	public static final String DATEFORMAT = "MMMM dd, yyyy".intern();
	public static final String DATEFORMAT_FULL = "yyyy-MM-dd'T'HH:mm:ssZ".intern();

	public static final String PDP_RESPONSE_ERROR = "Error in PDP response".intern();
	public static final String ORDER_ERROR = "Order Not created";
	public static final String PDP_PRODUCT_CODE_ERROR = "Product code does not exist".intern();
	public static final String PAYMENTUPDATE_ERROR = "Payment Transaction not updated".intern();

	public final static String AUDITTABLEUPDATEFAILURE = "Audit Table Update failure".intern();
	public static final String ERROR_CUSTOMER_ID = "Error in retreiving the customer id".intern();
	public static final String PDP_SHARED_PRE = "share.pretext";
	public static final String PDP_SHARED_POST = "share.posttext";

	public static final String KNOW_MORE_FIRST = "know.more.first";
	public static final String KNOW_MORE_SECOND = "know.more.second";
	public static final String KNOW_MORE_THIRD = "know.more.third";
	public static final String KNOW_MORE_FOURTH = "know.more.fourth";
	public static final String KNOW_MORE_FIFTH = "know.more.fifth";

	//Consignment Status
	public static final String ALLOCATED = "ALLOCATED";
	public static final String PICK_IN_PROGRESS = "PICK_IN_PROGRESS";
	public static final String PICK_HOLD = "PICK_HOLD";
	public static final String PICK_CONFIRMED = "PICK_CONFIRMED";
	public static final String AWB_ASSIGNED = "AWB_ASSIGNED";
	public static final String PRINT_SHIPPING_LABEL = "PRINT_SHIPPING_LABEL";
	public static final String INVOICE_GENERATED = "INVOICE_GENERATED";
	public static final String PRINT_INVOICE = "PRINT_INVOICE";
	public static final String MANIFESTO_PENDING = "MANIFESTO_PENDING";
	public static final String READY_TO_SHIP = "READY_TO_SHIP";
	public static final String LOGISTIC_PARTNER_SWITCH = "LOGISTIC_PARTNER_SWITCH";
	public static final String HOTC = "HOTC";
	public static final String DELIVERED = "DELIVERED";
	public static final Map<String, String[]> CONSIGNMENT_STATUS_MAP = new HashMap<String, String[]>()
	{
		{
			put("APPROVED", new String[]
			{ "PENDING_SELLER_ASSIGNMENT", "ALLOCATED", "SELLER_ACCEPTED", "SELLER_DECLINED" });
			put("PROCESSING", new String[]
			{ "PICK_IN_PROGRESS", "PICK_HOLD", "PICK_CONFIRMED", "PACKED", "AWB_ASSIGNED", "PRINT_SHIPPING_LABEL",
					"INVOICE_GENERATED", "PRINT_INVOICE", "MANIFESTO_PENDING", "READY_TO_SHIP", "LOGISTIC_PARTNER_SWITCH" });
			put("SHIPPING", new String[]
			{ "HOTC", "REACHED_NEAREST_HUB", "DELIVERED", "UNDELIVERED" });
			put("DELIVERY", new String[]
			{ "DELIVERED" });
			put("CANCELLATION", new String[]
			{ "ORDER_CANCELLED", "CLOSED_ON_CANCELLATION" });
			put("RETURN", new String[]
			{ "RETURN_INITIATED", "REVERSE_AWB_ASSIGNED", "RETURN_COMPLETED", "QC_FAILED", "CLOSED_ON_RETURN_TO_ORIGIN",
					"REDISPATCH_INITIATED", "REFUND_COMPLETED", "REFUND_IN_PROGRESS", "RETURN_REJECTED" });
		}
	};

	public static final String CART_PINCODE_ERROR_OMS_CHECK = "Delivery mode check from OMS issue ".intern();
	public static final String CART_PINCODE_ERROR = "Delivery mode check issue ".intern();
	public static final String WISHLIST_NON_EXISTENT = "rename.wishlist.no.exist".intern();

	// saved Card
	public static final String CC = "CC".intern();
	public static final String DC = "DC".intern();
	public static final String CCDC_BOTH = "Both".intern();
	public static final String CARDTYPE_CONID = "Please provide card type and Bank Name".intern();
	public static final String CARDDETAILSFETCH_ERROR = "ERROR in fetching card details".intern();
	public static final String STRINGSEPARATOR = "|";
	public static final String CHANNEL_WEBMOBILE = "WebMobile";
	public static final String CHANNEL_MOBILE = "Mobile";
	public static final String MPLSTORE = "mpl";


	public static final String CUSTOMERQUERY = "select {c:pk} from {customer As c} where {c.originalUid}=?originalUid".intern();
	public static final String PRODUCT_LEVEL = "Product Level ".intern();
	public static final String CART_LEVEL = "Cart Level ".intern();

	public static final String CARDDATA = "No Card Details available".intern();
	public static final String ORDERTYPEPARENT = "Parent".intern();
	public static final String EXCEPTION_ERROR = "An exception occured at backend".intern();
	public static final String FORGOTPASSWORD_URL = "/store/mpl/en";
	public static final String NOCARDTOKEN = "Please provide card Token";
	public static final String AUDITENTRYINPUTDATA = "Please provide juspayOrderId , channel and cartID";
	public static final String BINCHECKINPUTDATA = "Please provide cartID and paymentMode";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String ORDERCACHE = "orderCache";
	public static final String PRODSEARCHCACHE = "productSearchCache";
	public static final String EBSDOWNNOBINRY = "EBS Down Time and No Bin Entry found";
	public static final String EBSDOWNINTERNATIONAL = "EBS Down Time and Card is International";

	//Sonar Fixes
	public static final String DEPRECATION = "deprecation";
	public static final String ROLE_CLIENT = "ROLE_CLIENT";
	public static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	public static final String MISCSCACHE = "miscsCache";

	public static final String AUTHORIZATION_FAILED = "AUTHORIZATION_FAILED";
	public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
	public static final String JUSPAY_DECLINED = "JUSPAY_DECLINED";
	public static final String PENDING_VBV = "PENDING_VBV";
	public static final String JUSPAY_DECLINED_ERROR = "Transaction rejected due to high risk".intern();
	public static final String JUSPAY_FAILED_ERROR = "Transaction failed authentication".intern();
	public static final String FIELD_REQD = "field.required";
	public static final String COREAUTH_BADCRED = "CoreAuthenticationProvider.badCredentials";

	public static final String MEMBERS = "members";

	// Order Promotion Query
	public static final String ORDERPROMOQRY = "Select {o:pk} from {OrderPromotion  As o} where {o.enabled} = 1".intern();
	public static final String DELIVERY_PRE_TEXT = "mpl.pdp.delivery.pretext";
	public static final String DELIVERY_POST_TEXT = "mpl.pdp.delivery.posttext";
	public static final String STARTFORHOME = "startForHome";
	public static final String ENDFORHOME = "endForHome";
	public static final String STARTFOREXPRESS = "startForExpress";
	public static final String ENDFOREXPRESS = "endForExpress";
	public static final String DELISTED_MESSAGE_CART = "Sorry! Some of the items in your Cart are no longer available";
	public static final String DELISTED_MESSAGE_WISHLIST = "Sorry! Some of the items in your wishlist are no longer available";
	public static final String JUSPAY_CONN_ERROR = "Sorry! The system is down, please try again";

	public static final String LINK_LOGIN = "/login";
	public static final String QS = "?";
	public static final String AFFILIATEID = "affiliateId";
	public static final String EQUALS = "=";
}
