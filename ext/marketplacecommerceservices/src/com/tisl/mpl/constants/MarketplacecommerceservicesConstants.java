/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.constants;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReturnOrderModel;

import java.util.Date;


/**
 * Global class for all Marketplacecommerceservices constants. You can add global constants for your extension into this
 * class.
 */
@SuppressWarnings(
{ "PMD", "deprecation" })
public final class MarketplacecommerceservicesConstants extends GeneratedMarketplacecommerceservicesConstants
{
	public static final String EXTENSIONNAME = "marketplacecommerceservices";

	public static final String EMPTYSPACE = "";
	public static final String PromotionRestriction_manufacturerName = "brandName";
	public static final String EXCLUDEDPRODUCTS = "excludedProducts";
	public static final String CHANNEL = "channel";
	public static final String DISCOUNT_PRICES = "discountPrices";
	public static final String MINIMUM_AMOUNT = "minimumAmount";
	public static final String PROMO_CATEGORY = "supercategories";
	public static final String THRESHOLD_TOTALS = "thresholdTotals";
	public static final String NOT_VALID = "not valid";
	public static final String PERCENTAGE_MESSAGE = "Percentage";
	public static final String DISCOUNT_PRICES_MESSAGE = "Discount Price";
	public static final String NO_ADDRESS_FOUND = "No addresses found";
	public static final String SINGLE_SPACE = " ";
	public static final String EMPTY = "".intern();
	public static final String SPACE = " ";
	public static final String BUSINESS_EXCEPTION_TEXT = "Business Exception";
	public static final String MODEL_NOT_SAVED = "model not saved";
	public static final String CHANNEL_WEB = "WEB";
	public static final String CHANNEL_WEBMOBILE = "WEBMOBILE";
	public static final String CHANNEL_MOBILE = "MOBILE";
	public static final String CHANNEL_CALLCENTER = "CALLCENTER";
	public static final String Already_Have_Wishlists = "You already have the following whishlists:";
	public static final String PRODUCT_PRIORITY = "7";
	public static final String CART_DELISTED_STATUS = "Cart Delisted Status :";

	//SONAR FIX
	public static final String CONSIGNMENT_STATUS = " Consignment status :";
	public static final String BOXING = "boxing";
	public static final String APPLICATION_JSON_VALUE = "application/json";
	public static final String NEW_PASSWORD = "newPassword";
	public static final String PRICE_NOT_AVAILABLE = "Price not Available";
	public static final String PWD = "pwd";

	public static final String LIST_EMPTY = "List is Empty";

	public static final String CustomerSpecificRestriction_REGISTERED = "REGISTERED";
	public static final String CustomerSpecificRestriction_FIRSTTIMEUSER = "FIRSTTIMEUSER";
	public static final String CustomerSpecificRestriction_RETURNINGUSER = "RETURNINGUSER";

	public static final String customerCookieVal = "REGCOOKIE";
	public static final String customerCookieName = "CUSREG";
	public static final String firstTimeUserSessionVal = "FIRSTTIMEUSER";
	public static final String promotionProductDelimiter = "|";
	public static final String ERRORMESSAGE = "NO VALUE|WRONG VALUE";

	public static final String MALE = "Male";
	public static final String FEMALE = "Female";
	public static final String MALE_CAPS = "MALE";
	public static final String FEMALE_CAPS = "FEMALE";
	public static final String EXCEPTION_IS = "Exception is : ";
	public static final String DMY_DATE_FORMAT = "dd/MM/yyyy";
	public static final String DMY_DATE_FORMAT_INT = "yyyyMMdd";
	public static final String COUPONS_DATE_FORMAT = "MMM dd, YYYY";
	public static final String TRUE = "true";
	public static final String TRUE_UPPER = "TRUE";
	public static final String ZERO = "0";
	public static final String LINK_PASSWORD_CHANGE = "/login/pw/change";

	//For Promotion Intercepter
	public static final String PROMOCODE = "Promotion Identifier :".intern();
	public static final String PROMOPRODUCT = "Promotion Product :".intern();
	public static final String PROMOCATEGORY = "Promotion Category :".intern();
	public static final String PROMOPRIORITY = "Promotion Priority :".intern();
	public static final String PRODUCT_PRICE_COLUMN = "price".intern();
	public static final String PRESENT_CATEGORY = "Present Category :".intern();

	//For SuperCategoryDecorator
	public static final String CONFIGURATION_SER = "configurationService";
	public static final String PRIMARYHIERARCHY = "decorator.primary";
	public static final String CLASSIFICATIONCATEGORYCATALOG = "decorator.classificationcatalog";
	public static final String MARKETPLACECLASSIFICATIONCATALOG = "decorator.marketplacecatalog";

	public static final String FRONTSLASH = "/";


	//For AddProductCategory Intercepter
	public static final String COMMA = ",";
	public static final String HYPHEN = "-";
	public static final String KEY = "0";
	public static final String VALUE = "1";


	//For AddProductCategory Intercepter
	public static final String APPAREL = "MPH11";
	public static final String ELECTORNICS = "MPH12";
	public static final String CATEGORY_APPAREL = "Clothing";
	public static final String CATEGORY_ELECTORNICS = "Electronics";


	//For Customer Facing Interceptor
	public static final String NOTIFICATION_STATUS = "notification.status";
	public static final String USE_NOTIFICATION = "notification.use";
	public static final String FIRE_NOTIFICATION = "notification.fire.status";
	public static final String PARAMETER_MISSING = "Some Important Parameters are Missing or Null";
	public static final String CUSTOMER_STATUS_NOT_PRESENT_ERROR = "Customer Facing Status Not Present in Local Properties. Please Check";
	public static final String STATUS_NOT_PRESENT_ERROR = "Status Not present in Local Properties.Please Validate";
	//PAYMENT FACADE
	public static final String MPLPAYMENTFACADE = "mplPaymentFacade";
	public static final String TIMEFOROTP = "OTP_Valid_Time_milliSeconds".intern();
	public static final String SPLITSTRING = "\\|".intern();
	public static final String CONCTASTRING = "|".intern();
	//public static final String HASHCALMETHOD = "SHA-512".intern();
	public static final String HASHVALUE = "Empty".intern();
	public static final String HASHAPPEND = "0".intern();
	public static final String EMI = "EMI".intern();
	public static final String DEBIT = "Debit Card".intern();
	public static final String CREDIT = "Credit Card".intern();
	public static final String COD = "COD".intern();
	public static final String NETBANKING = "Netbanking".intern();
	public final static String PAYMENTMODE = "paymentMode".intern();
	public final static String PAYMENTMODEFORPROMOTION = "paymentModeForPromotion".intern();
	public final static String BINNO = "bin".intern();
	public final static String EMPTYSTRING = "".intern();
	public final static String BANKFROMBIN = "bank".intern();
	public final static String VALID = "VALID".intern();
	public final static Date EMPTYDATE = null;
	//PAYMENT SERVICE
	public static final String MPLPAYMENTSERVICE = "mplPaymentService";
	//public static final String CALLINGPAYUSERVICE = "Calling PAYU service:::::::";
	//public static final String ORDERAMOUNT = "Amount of Order::::";
	//public static final String BANKCODE = "Bank Code:::::";
	//public static final String CUSTOMEREMAIL = "Email of Customer::::";
	//public static final String CUSTOMERFIRSTNAME = "Customer's FirstName:::::";
	//public static final String CUSTOMERLASTNAME = "Customer's LastName:::::";
	//public static final String CUSTOMERPHONENO = "Phone No:::::";
	//public static final String PG = "PG";
	//public static final String HASH = "Hash::::";
	public static final String SUCCESS = "success".intern();
	public static final String FAILURE = "failure".intern();
	public static final double MONTHDENO = 1200;
	public static final double AMOUNTTOBEDEDUCTED = 0.00D;
	public static final String FORMAT = "%.2f".intern();
	public static final String FORMATONE = "%.1f".intern();
	public static final String WALLET = "Wallet".intern();
	public static final String PAYMENTSUCCESSFUL = "Payment Successful".intern();
	public static final double PERCENTVALUE = 100;
	public static final String AUTHORIZATION_FAILED = "AUTHORIZATION_FAILED".intern();
	public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED".intern();
	public static final String JUSPAY_DECLINED = "JUSPAY_DECLINED".intern();

	//JusPay Merchant
	public final static String JUSPAYMERCHANTTESTKEY = "payment.juspay.key".intern();
	public final static String JUSPAYBASEURL = "payment.juspay.baseUrl".intern();
	public final static String JUSPAYRETURNMETHOD = "payment.juspay.returnMethod".intern();
	public final static String JUSPAYMERCHANTID = "payment.juspay.merchant".intern();
	public static final String MARCHANTID = "payment.juspay.merchantID".intern();
	public static final String JUSPAYURL = "payment.juspay.environment.url".intern();
	public static final String JUSPAY_ORDER_RESP = "Init order response:  ".intern();
	public static final String JUSPAY_ORDER_ID = "juspayOrderId".intern();
	public static final String MERCHANT_JUSPAY = "juspay".intern();
	public static final String CHARGED = "CHARGED".intern();
	public static final String JUSPAY_ORDER_STAT_RESP = "Order status response: ".intern();
	public static final String NA = "NA".intern();
	public static final String DUMMYCCOWNER = "DUMMY NAME".intern();
	public static final String DUMMYNUMBER = "XXXXXXXXXXXXXXXX".intern();
	public static final String DUMMYCARDREF = "xxxxxxxxxxxxxxxxxx".intern();
	public static final String DUMMYMM = "MM".intern();
	public static final String DUMMYYY = "YYYY".intern();
	public static final String MASTERCARD = "MasterCard".intern();
	public static final String MAESTRO = "Maestro".intern();
	public static final String AMEX = "AMEX".intern();
	public static final String AMERICAN_EXPRESS = "AMERICAN EXPRESS".intern();
	public static final String DINERSCARD = "DinersCard".intern();
	public static final String VISA = "VISA".intern();
	public static final String EUROCARD = "EuroCard".intern();
	public static final String SWITCHCARD = "SwitchCard".intern();
	public static final String ROOTCATEGORY = "marketplace.mplcatalog.rootcategory.code".intern();
	public static final String RETURNURL = "payment.juspay.returnUrl".intern();
	public static final String ON = "on".intern();
	public static final String WAREHOUSE = "mpl_warehouse";


	//PAYMENT DAO
	public static final String MPLPAYMENTDAO = "mplPaymentDao";
	public static final String MPLSTORE = "store".intern();
	public static final String MPLCARTVALUE = "cartValue".intern();
	public static final String MPLBANK = "bank".intern();
	public static final String CUSTOMERID = "customerId";
	public static final String JUSPAYORDERID = "juspayOrderId".intern();
	public static final String NBNORMALBANKSQUERY = "select {b:pk} from {bankForNetbanking As b},{bank as m} where {b.isAvailable}='1' and {b.priority}='0' and {b.name}={m.pk} order by {m.bankname}"
			.intern();
	public static final String NBPRIORITYBANKSQUERY = "select {b:pk} from {bankForNetbanking As b},{bank as m} where {b.isAvailable}='1' and {b.priority}='1' and {b.name}={m.pk} order by {m.bankname}"
			.intern();
	public static final String PAYMENTTYPESQUERY = "select {p:pk} from {PaymentType As p JOIN BaseStore AS b ON {p.basestore}={b.pk}} WHERE {b.uid}=?store"
			.intern();
	public static final String EMIBANKSQUERY = "select {b:pk} from {emiBank As b} ,{bank as m} where {b.emiLowerLimit}<=?cartValue and {b.emiUpperLimit}>=?cartValue and {b.name}={m.pk}  order by {m.bankname}"
			.intern();
	public static final String EMIBANTERMSSQUERY = "select {e:pk} from {emibank as e},{bank as b} where {e.name}={b.pk} and {b.bankName}=?bank"
			.intern();
	public static final String PAYMENTTYPEFORAPPORTIONQUERY = "select {p:pk} from {PaymentType As p} WHERE {p.mode}=?paymentMode"
			.intern();
	public static final String BANKFORBINQUERY = "select {b:pk} from {Bin As b} WHERE {b.binno}=?bin".intern();
	public static final String GETPAYMENTTRANSACTIONFROMCARTQUERY = "select {p:pk} from {PaymentTransaction as p},{Cart as c},{Customer as u} where {p.order}={c.pk} and {c.user}={u.pk} and {u.uid}=?customerId and {p.code}=?juspayOrderId and {p.status}='success' "
			.intern();
	public static final String MOBILEBLACKLIST = "select {b:pk} from {blacklist As b} where {b.phoneNumber} =?phoneNumber"
			.intern();
	public static final String MOBILENO = "phoneNumber".intern();
	public static final String PAYMENTMODETYPE = "select {b:pk} from {PaymentType As b} where {b.mode}=?mode".intern();
	public static final String MODE = "mode".intern();
	public static final String SAVEDCARDQUERY = "Select {s:pk} from {SavedCard as s},{Customer as c} where {s.customer}={c.pk} and {c.customerid}=?customerId and {s.cardReferenceNumber}=?cardRef"
			.intern();
	public static final String CARDREF = "cardRef".intern();
	public static final String COUNTRYISOQUERY = "select {c:pk} from {Country As c} where {c.name}=?countryName".intern();
	public static final String SAVEDCARDFORCUSTOMERQUERY = "Select {s:pk} from {SavedCard as s},{Customer as c} where {s.customer}={c.pk} and {c.customerid}=?customerId"
			.intern();
	public static final String COUNTRYNAME = "countryName".intern();
	public static final String IPBLACKLIST = "select {b:pk} from {Blacklist As b} where {b.ipAddress} =?ipAddress".intern();
	public static final String IPADDRESS = "ipAddress".intern();
	public static final String CODBLACKLISTQUERY = "select {b:pk} from {Blacklist As b} where {b.customerId} =?customerId or {b.emailID}=?emailID or {b.phoneNumber}=?phoneNumber or {b.ipAddress}=?ipAddress"
			.intern();
	public static final String CUSTNAME = "customerName".intern();
	public static final String EMAILID = "emailID".intern();
	public static final String COUNTRYLISTQUERY = "select {c:pk} from {Country As c}".intern();
	public static final String AUDITQUERY = "select {a:pk} from {MplPaymentAudit As a} where {a.auditId}=?auditId".intern();
	public static final String AUDITID = "auditId".intern();
	public static final String AUDITWITHGUIDQUERY = "select {a:pk} from {MplPaymentAudit As a} where {a.cartGUID}=?cartGUID order by {a.requestDate} desc"
			.intern();
	public static final String CARTGUID = "cartGUID".intern();
	public static final String CUSTOMERQUERY = "select {c:pk} from {customer As c} where {c.uid}=?uid".intern();
	public static final String UID = "uid".intern();
	public static final String ALLPROMOTIONSQUERY = "select {p:pk} from {abstractPromotion as p} where {p.enabled}='1' and sysdate<={p.enddate} and sysdate>={p.startdate}"
			.intern();




	//For Search Populator
	public static final String BRAND = "brand";
	public static final String SELLER = "seller";

	//For Promotions
	public static final String PROMO_PRODUCT = "products".intern();
	public static final String PROMO_CATEGORIES = "categories".intern();
	public static final String BUYAPERCENTAGEDISCOUNT = "BuyAPercentageDiscount".intern();
	public static final String BUYxAGETBFREE = "BuyXItemsofproductAgetproductBforfree".intern();
	public static final String CASHBACKPROMO = "BuyAGetPrecentageDiscountCashback".intern();
	public static final String CUSTOMBOGO = "CustomProductBOGOFPromotion".intern();
	public static final String ELIGIBLECOUNT = "eligibleCount".intern();
	public static final String TOTALELIGIBLECOUNT = "totalEligibleCount".intern();
	public static final String SELECTEDUSSID = "selectedUSSID".intern();

	public static final String GUID = "guid".intern();
	public static final String FREEPRODUCT = "freeProduct".intern();
	public static final String PRODUCT_SKUID = "productUSSID".intern();
	public static final String PROMOTIONRESULT = "promotionResult".intern();

	//For Promotion Apportioning
	public static final String AMOUNT = "amount".intern();
	public static final String ORDERENTRY_PRODUCT = "orderEntryProduct".intern();
	public static final String ORDERENTRY_NUMBER = "orderEntryNumber".intern();
	public static final String ORDERENTRY_QUANTITY = "orderEntryQuantity".intern();

	public static final String FREEGIFT_QUANTITY = "freeGiftQuantity".intern();

	public static final String PERCENTAGEDISCOUNT = "percentageDiscount".intern();
	public static final String TOTALVALIDPRODUCTSPRICEVALUE = "totalValidProductsPricevalue".intern();
	public static final String VALIDPRODUCTLIST = "validProductList".intern();

	//ADDED FOR APPORTIONMENT
	public static final String APORTIONEDITEMVALUE = "aportionedItemValue".intern();
	public static final String APORTIONEDPROMOTIONPRICE = "aportionedPromotionPrice".intern();
	public static final String DESCRIPTION = "description".intern();
	//public static final String TOTALQUANTITY = "totalQuantity".intern();
	public static final String QUALIFYINGCOUNT = "qualifyingCount".intern();
	public static final String FREECOUNT = "freeCount".intern();
	public static final String ASSOCIATEDITEMS = "associatedItems".intern();
	public static final String CARTPROMOCODE = "cartPromoCode".intern();
	public static final String PRODUCTPROMOCODE = "productPromoCode".intern();
	public static final String TOTALSALEPRICE = "totalSalePrice".intern();
	public static final String TOTALPRODUCTLEVELDISC = "totalProductLevelDisc".intern();
	public static final String NETSELLINGPRICE = "netSellingPrice".intern();
	public static final String CARTLEVELDISC = "cartLevelDisc".intern();
	public static final String NETAMOUNTAFTERALLDISC = "netAmountAfterAllDisc".intern();
	public static final String PROMOTIONTYPE = "promotionType".intern();
	public static final String QCFROMBOGOMODEL = "qcFromBogoModel".intern();
	public static final String FREEITEMFORCATBOGO = "freeItemForCatBogo".intern();
	//public static final String PROMOTYPE = "promoType".intern();
	public static final String ISPRODUCTLEVELBOGO = "isProductLevelBogo".intern();
	public static final String ISBOGOAPPLIED = "isBOGOapplied".intern();
	//FOR SHIPPING CHARGE PROMOTIONS
	public static final String TSHIP = "tShip".intern();
	public static final String SSHIP = "sShip".intern();
	public static final String PRODPREVCURRDELCHARGEMAP = "prodPrevCurrDelChargeMap".intern();
	public static final String PREVDELIVERYCHARGE = "prevDelCharge".intern();
	public static final String CURRENTDELIVERYCHARGE = "currDelCharge".intern();


	public static final String SUCCESS_FLAG = "Success";
	public static final String ERROR_FLAG = "Failure";
	public static final String BAD_CREDENTIALS = "Bad credentials";
	public static final String CREDENTIALS_EXP = "User credentials have expired";
	public static final String uusid = "uusid";
	public static final String articleSKUID = "articleSKUID";

	public static final String USER_NOT_FOUND = "No user found for the current Email ID";
	public static final String WISHLIST_NOT_FOUND = "No wishlist found for the current User";
	public static final String PRODUCT_NOT_FOUND = "Product not found in the wishlist";
	public static final String WISHLIST_FOUND_EMPTY = "Wishlist is Empty";
	public static final String PRODUCT_ADDED = "Product added successfully to the wishlist";
	public static final String PRODUCT_NOT_ADDED = "Product already exists";
	public static final String MANDATORY_PARAMETERS_CANNOT_BE_BLANK = "Mandatory Parameters cannot be left blank";

	// No WishList Available
	public static final String NOWISHLISTAVAILABLE = "No Wishlist Available for this user".intern();

	//Mobile all cat
	public static final String SALESCATEGORYTYPE = "marketplace.mplcatalog.salescategory.code";
	public static final String DEFAULTCATALOGID = "cronjob.promotion.catelog";
	public static final String DEFAULTCATALOGVERISONID = "cronjob.promotion.catalogVersionName";
	public static final String SALESROOTCATEGORY = "marketplace.mplcatalog.sales.rootcategory.code";
	public static final String SHOPBYBRANDCOMPONENT = "marketplace.mplcatalog.shopbybrand.component.id";
	public static final String SHOPBYDEPTCOMPONENT = "marketplace.mplcatalog.shopbydept.component.id";
	public static final String SALESCLOTH = "marketplace.mplcatalog.sales.cloth.id";
	public static final String SALESMEN = "marketplace.mplcatalog.sales.men.id";
	public static final String SALESWOMEN = "marketplace.mplcatalog.sales.women.id";
	public static final String SALESELECTRONICS = "marketplace.mplcatalog.sales.electronics.id";


	//Seller Master Constants
	public static final String SELLER_MASTER_XSD_PATH = "xsd/Seller_Master.xsd";
	public static final String SELLER_MASTER = "SELLER MASTER";
	public static final String XSD_DATE_FORMAT = "yyyy-MM-dd";
	public static final String ERROR_CODE_1 = "101";
	public static final String ERROR_MSG_INVALID_TYPE_CODE = "Invalid seller type code";
	public static final String SELLER_MASTER_ERROR_MSG = "Exception in saving seller master data";
	public static final String SELLER_INFO_UPDATE_ERROR_MSG = "Exception in updating seller information with seller master";
	public static final String INVALID_SCHEMA_MSG = "Invalid XML, XML did not match XSD schema";
	public static final String DATA_SAVED_MSG = "Data save successfully.!!!";
	public static final String STATUS = "STATUS";
	public static final String FALSE = "False";
	public static final String SUCCESSS_RESP = "SUCCESS";
	public static final String CVC_DATATYPE = "cvc-datatype-valid";
	public static final String CVC_COMPLEX = "cvc-complex-type";
	public static final String MSG = "MSG";
	public static final String SELLER_INFO = "sellerInformation";
	public static final String SELLER_TYPE_GLOBAL_CODES = "mplSellerTypeGlobalCodesService";
	public static final String YYYYMMDD = "yyyy/MM/dd";
	public static final String CREATE_WISHLIST = "CreateWishlist";
	public static final String EMAIL_NOT_FOUND = "user.email.not_found";
	public static final String WISHLIST_NO = "Wishlist";
	public static final String THUMBNAIL = "thumbnail";
	public static final String WISHLIST_EXISTS_MSG = "Wishlist name already exists.";


	//Mobile home page
	public static final String HOMEPAGECOMPONENT = "marketplace.homepage.component.id";

	//Cart Constants
	public static final String NULL_USER = "Null values received for Email Id and Anonymous";
	public static final String ANONYMOUS = "anonymous";
	public static final String CART_EXISTS = "Cart already exists for the user.";
	public static final String INVALID_CART_ID = "Invalid cart id:";
	public static final String INVALID_PRODUCT_CODE = "Invalid product code:";
	public static final String INVALID_PRODUCT_QUANTITY = "Invalid product quantity:";
	public static final String FAILURE_CARTADD = "Unable to add to cart.";
	public static final String HOME_DELIVERY = "home-delivery";
	public static final String FIELD_QUANTITY = "quantity";
	public static final String FIELD_NOT_EMPTY_MSG = "Field cannot be empty";
	public static final String EXPRESS_DELIVERY = "express-delivery";
	public static final String CLICK_COLLECT = "click-and-collect";
	public static final String CART_NOT_FOUND = "Cart not found.";
	public static final String COULD_NOT_MODIFY_CART = "Could not modify cart:";
	public static final String YES = "yes";
	public static final String YOUMAYALOSLIKECOMPONENT = "MplCartSuggestions";
	public static final String NON_EMPTY = "";
	public static final String COLON = ":";
	public static final String COLON_SLASH = "://";
	public static final String NO_CONTACT_NUMBER = "No Contact available for this user";
	public static final String ANONYMOUS_USER_NOT_ALLOWED_MSG = "Anonymous user is not allowed to copy cart!";
	public static final String CART_NOT_ANONYMOUS_MSG = "Cart is not anonymous";
	public static final String CART_NOT_CURRENT_USERS_MSG = "Cart is not current user's cart";
	public static final String COULD_NOT_MERGE_CARTS_MSG = "Couldn't merge carts";
	public static final String COULD_NOT_RESTORE_CARTS_MSG = "Couldn't restore cart";
	public static final String MKTURL = "http://localhost:9001/store/login?clear=true&site=mpl";
	public static final String ContextURI = "/store/mpl/en";
	public static final String UNDER_SCORE = "_";
	public static final String DELIVERY_MODE_SELECT = "SelectDeliveryMode";
	public static final String DELIVERY_MODE_NOT_SET = "Delivery mode not saved";
	//OTP
	public static final int[] DOUBLEDIGITS =
	{ 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 };

	public static final int[] DIGITS_POWER
	// 0 1 2 3 4 5 6 7 8
	=
	{ 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

	public static final String EMPTY_CART = "Cart is empty.";
	public static final String NODELIVERYADDRESS = "Delivery Address is Empty";
	public static final String NOSUBTOTAL = "Subtotal is Empty";
	public static final String NOTOTALPRICE = "Total Price is Empty";
	public static final String NO = "no";
	public static final String INSERT = "I".intern();
	public static final String UPDATE = "U".intern();

	public static final String DEFAULT_IMPORT_CATALOG_ID = "mplProductCatalog";
	public static final String DEFAULT_IMPORT_CATALOG_VERSION = "Online";
	public static final String HMAC_SHA1 = "HmacSHA1";
	public static final String RAW = "RAW";
	public static final String HMAC_SHA_1 = "HMAC-SHA-1";

	public static final String OTP = "OTP >>>>>>>  ";
	public static final String OTP_EXPIRED = "Otp has expired";
	public static final String OTP_MATCHED = "Otp matched !";

	public static final String ADDRESS_DATA = "addressData";
	public static final String ADDRESTYPE_EQUALS_ADDADDRESS = "addrestype=addaddress";
	public static final String MODEL_SAVING_EXCEPTION = "ModelSavingException";
	public static final String CLIENTID = "config.clientId";
	public static final int NAME = 40;
	public static final String MOBILENUMBERLENGTH = "Please Enter valid 10 digit phone number";
	public static final int MOBLENGTH = 10;
	public static final String INVALIDDATE = "Please Enter valid date in formate dd/mm/yyyy";
	public static final String OTPVALIDITY = "VALID";
	public static final String OTPEXPIRY = "EXPIRED";
	public static final String OTP_SENT = "OTP sent";
	public static final String OTP_EXPIRY_MESSAGE = "Sorry! This OTP has expired.";
	public static final String INVALID_OTP = "Are you sure that's the right OTP?";
	public static final String OTPERROR = "Please Enter valid OTP";
	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";
	public static final int SHOP_BY_LOOK_PAGE_SIZE = 2;

	//Changes for Delivery Mode
	public static final String INR = "INR";
	public static final String HD = "HD";
	public static final String ED = "ED";
	public static final String CnC = "CnC";
	public static final String CC = "CC";

	public static final String X = "X";
	public static final String RESTRICTION_SETUP_ERROR_MSG = "data not inserted";
	public static final String RESTRICTION_SETUP_ERROR = "Exception occured during restriction setup";
	public static final String RESTRICTION_SETUP = "non business error";

	public static final String SELLERNAME = "sellerName";
	public static final String NOT_APPLICABLE = "NA";

	public static final String ORDER_STATUS_HOTC = "HOTC";
	public static final String ORDER_STATUS_RNH = "REACHED_NEAREST_HUB";
	public static final String ORDER_STATUS_OFD = "OUT_FOR_DELIVERY";
	public static final String ORDER_STATUS_COD = "COD";
	public static final String ORDER_STATUS_REFUND = "REFUND_INITIATED";
	public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
	public static final String ORDER_STATUS_READY_TO_SHIP = "READY_TO_SHIP";
	public static final String ORDER_STATUS_UNDELIVERED = "UNDELIVERED";
	public static final String RMS_VERIFICATION_FAILED = "RMS_VERIFICATION_FAILED";
	public static final String PAYMENT_SUCCESSFUL = "PAYMENT_SUCCESSFUL";
	public static final String RMS_VERIFICATION_PENDING = "RMS_VERIFICATION_PENDING";

	public static final String SMS_VARIABLE_ZERO = "{0}";
	public static final String SMS_VARIABLE_ONE = "{1}";
	public static final String SMS_VARIABLE_TWO = "{2}";
	public static final String SMS_VARIABLE_THREE = "{3}";
	public static final String SMS_VARIABLE_FOUR = "{4}";




	public static final String SMS_SENDER_ID = "TATAUS";
	public static final String SMS_MESSAGE_FORGOT_PWD = "Dear Customer, One Time Password for your request is {0}. Please enter the same to submit the request. Regards, Team Tata Unistore.";
	public static final String SMS_MESSAGE_ORDER_PLACED = "Hi {0}, thank you for placing the order with us .Your order ref no is ({1}). Excited? Click here to track your order {2} .";
	public static final String SMS_MESSAGE_ORDER_SHIPPED = "Hey! we have shipped {0} item(s) of your order #{1} via {2}.Give it 2-3 working days to reach you. Can't control the excitement? Track your order here {3} .Thanks!";
	public static final String SMS_MESSAGE_ORDER_OUTFORDELIVERY_PREPAID = "Awesome news! {0} item(s) of your order  # {1} will reach you today. High five for shopping with us. Don't forget to download our app at {2} .";
	public static final String SMS_MESSAGE_ORDER_OUTFORDELIVERY_COD = "Awesome news! {0} item(s) of your order # {1} will reach you today. Please keep INR {2} ready for COD. High five for shopping with us. Download our app at {3} .";
	public static final String SMS_MESSAGE = "Test from TatauniStore";
	public static final String SMS_MESSAGE_COD_OTP = "Peek-a-boo {0}! One-time password for your COD order is {1}. Please feel free to call us at {2} in case of any queries.";
	public static final String SMS_MESSAGE_C2C_OTP = "Hi, one time password for your request is {0}. Please enter this to submit the request. Thanks!";


	public static final String PUSH_MESSAGE_ORDER_PLACED = "Awesome! We think your bag looks great. Track your order {0} in the Order History section of your account.";
	public static final String PUSH_MESSAGE_ORDER_OFD_PREPAID = "Hi! Your order {0} of {1} item(s) will reach you today. To know more, check out the Order History section of the app.";
	public static final String PUSH_MESSAGE_ORDER_OFD_COD = "Hi! Your order {0} of {1} item(s) will reach you today. Please keep INR {2} cash handy. To know more, check out the Order History section of the app.";
	public static final String PUSH_MESSAGE_ORDER_CANCELLED = "Hi! We have initiated a refund of INR {0} to you for the {1} item(s) you cancelled.The reason for this cancellation is {2}. To know more, check out the Order History section of the app.";


	public static final String SMS_SERVICE_CONTACTNO = "marketplace.sms.service.contactno";
	public static final String SMS_ORDER_TRACK_URL = "marketplace.sms.order.track.url";
	public static final String SMS_SERVICE_APP_DWLD_URL = "marketplace.sms.app.download.url";
	public static final String SMS_SERVICE_WEBSITE_URL = "marketplace.sms.order.website.url";


	//public static final String SMS_MESSAGE_HOTC = "Hey! we have shipped {0} item(s) of your order #{1} via {2}.Give it 2-3 working days to reach you. Can't control the excitement? Track your order here {3} .Thanks!";
	public static final String SMS_MESSAGE_HOTC = "Hey! We have shipped {0} item(s) of your order  # {1} via {2}. Give it 2-3 working days to reach you. Can't control the excitement? Track your order here {3} .Thanks!";
	public static final String SMS_MESSAGE_ORDER_DELIVERY = "And it's done! You received {0} item(s) of your order # {1} today. We had tons of fun serving you. Thanks! To shop on the move, download our app at {2} .";
	public static final String SMS_MESSAGE_REFUND_INITIATED = "Hey! We have initiated a refund of INR {0} to you against return of {1} item(s) of your order ref # {2} with us. Please check your email for more details.";
	public static final String SMS_MESSAGE_ORDER_UNDELIVERED = "Hey {0},we failed to deliver {1} item(s) of your order # {2} today. We will try delivering again. Feel free to call us at {3} in case of any queries.";

	public static final String SMS_MESSAGE_ORDER_RISK = "Oh no! Your order ref # {0}) has been placed but it's been put on hold. Don't worry, your transaction with us was completely safe We will keep you posted. Questions? Call us at {1} .";
	public static final String SMS_MESSAGE_ORDER_RISK_CONFIRMED = "High five! Our Risk Management team came to the rescue. Your order ref # {0} is confirmed. Go ahead and track your order at {1} . Thanks!";
	public static final String SMS_MESSAGE_ORDER_RISK_REJECTED = "Oh gosh! We were unable to accept your order ref # {0}. Should you wish to place another order, visit our website {1} or download our app at {2}. To know more about this order call {3}.";


	public static final int MAX_PAGE_LIMIT = 100;

	public static final String SNS_CATEGORY = "snsCategory";
	public static final String EMI_CUTOFF = "marketplace.emiCuttOffAmount";
	public static final String NEW_CUSTOMER_CREATE_FLAG = "I";
	public static final String SEARCH_NOT_FOUND = "Search Category not found";
	public static final String SIZEGUIDE_NOT_FOUND = "Size Guide not available";

	public static final String EMAIL_REPLY = "replyToAddress";
	public static final String EMAIL_ADDRESS_EMPTY = "toAddresses must not be empty";
	public static final String EMAIL_ADDRESS_NULL = "fromAddress must not be null";
	public static final String EMAIL_SUBJECT_EMPTY = "subject must not be empty";
	public static final String EMAIL_BODY_EMPTY = "body must not be empty";
	public static final String EMAIL_BODY_MEDIA = "bodyMedia-";



	//For Bulk Upload in Promotions
	public final static char FIELD_SEPARATOR = ',';

	public static final String BUYAALONGBGETSHIPPINGFREE = "BuyAalongBgetShippingFree".intern();
	public static final String BUYAANDBPERCENTAGEDISCOUNT = "BuyAandBPercentageDiscount".intern();
	public static final String BUYAANDBGETC = "BuyAandBgetC".intern();
	public static final String BUYABFREEPERCENTAGEDISCOUNT = "BuyABFreePrecentageDiscount".intern();
	public static final String BOGO = "CustomProductBOGOFPromotion".intern();
	public static final String BUYXOFAGETBFREEANDDISCOUNT = "BuyXItemsofproductAgetproductBforfree".intern();
	public static final String CARTDISCOUNTPROMO = "CartOrderThresholdDiscountPromotion".intern();
	public static final String CARTFREEBIEPROMO = "CustomOrderThresholdFreeGiftPromotion".intern();
	public static final String ABCASHBACKPROMO = "BuyAandBGetPrecentageDiscountCashback".intern();
	public static final String ACASHBACKPROMO = "BuyAGetPrecentageDiscountCashback".intern();
	public static final String CARTCASHBACKPROMO = "CartOrderThresholdDiscountCashback".intern();
	public static final String BUYAABOVEXGETPERCENTAGEORAMOUNTOFF = "BuyAAboveXGetPercentageOrAmountOff".intern();
	//For Bulk Upload in Promotion Restriction
	public static final String ETAILSELLERSPECIFICRESTRICTION = "EtailSellerSpecificRestriction".intern();
	public static final String BRANDRESTRICTION = "ManufacturersRestriction".intern();
	public static final String PAYMENTMODESPECIFICPROMOTIONRESTRICTION = "PaymentModeSpecificPromotionRestriction".intern();
	public static final String DELIVERYMODEPROMOTIONRESTRICTION = "DeliveryModePromotionRestriction".intern();
	public static final String UPDATE_EMAIL_URL = "update_Email_url";
	public static final String PASSWORD_RESET_URL = "password_reset_url";
	public static final String UNKNOWN = "Unknown";
	public static final String M = "M";
	public static final String F = "F";
	public static final String U = "U";
	public static final String ENTER_PWD = "Password policy validation error";

	//For Blacklist Report
	public static final String CSV_FILE_HEADER = "date,customerId,emailId,ipAddress,name,phoneNo";
	public static final String FILE_LOCATION = "cod.blacklist.report.path";
	public static final String REPORT = "report";
	public static final String SELLERPRIORITYREPORT = "sellerPriorityReport";

	public static final String ORDER_NOT_CONFIRMED = "Order Not Confirmed";
	public static final String INVALIDORDERID = "Please Enter valid Order number";
	public static final String PRODUCTNOTFOUND = "Product not found";
	public static final String ORDERNOTFOUND = "Order not found for this user";

	//For Sales Report
	public static final String SALES_CSV_FILE_HEADER = "Orderno,OrderDate,Orderstatus,ListingId,USSID,SellerSKUId,CustomerName,CustomerRegisterDate,TransactionRef,TransactinRefId,"
			+ "SellerName,Brand,productPrice,Quantity,Itemcategory,Itemsubcategory,PaymentMethod,BankName,Tenure(only if eMI),Shippingcity,zipcode,ShippingState,address,phnumber,"
			+ "producttype,TransactionRefNumberasgateway,Ipadddress,Totalprice,Email,Riskscore,MrpPrice,Mopprice,deliverytype";
	public static final String SALES_REPORT_TYPE = "salesreport.cronjob.type";
	public static final String SALES_FILE_LOCATION = "salesreport.cronjob.report.path";
	public static final String SALES_REPORT = "salesReport";
	public static final String OTPGENERATEERROR = "Otp has not generated";
	public static final String PREPAID_SPACE = "Prepaid";

	public static final String INTERESTED_IN_EMAIL = "I am interested in receiving e-mails";
	public static final String NOT_INTERESTED_IN_EMAIL = "I am not interested in receiving e-mails";
	public static final String BRAND_NAME_PREFIX = "MBH";
	public static final String CUSTOMERNOTFOUND = "Customer not found";
	public static final String NOBANKLIST = "bank list is not available";

	public static final String SSHIPCODE = "SSHIP";
	public static final String SET_EMPTY_ORDER_NOTIFICATIONS = "Notifications are not available";
	public static final String EMAIL_ID_IS_EMPTY = "Email ID for the current user is empty";

	public static final String N = "N";
	public static final String Y = "Y";

	public static final String DEPT_TYPE = "deptType";

	//for Promotional Report
	public static final String PROMOTIONS_REPORT_FILE_LOCATION = "promotions.report.path".intern();
	public static final String PROMOTIONS_CSV_FILE_HEADER = "USER,PROMOCODE,TIMESTAMP,MODIFICATIONTYPE,STARTDATE,ENDDATE,MODIFIED ATTRIBUTES"
			.intern();
	public static final String PROMOTION_REPORT = "promotionalReport".intern();
	public static final String PROMOTIONS_REPORT_STARTDATE = "promotions.report.startDate".intern();
	public static final String PROMOTIONS_REPORT_ENDDATE = "promotions.report.endDate".intern();

	//For sellerSKU level checking
	public static final String RESTRICTIONLIST = "restrictionList".intern();

	public static final String STRINGSEPARATOR = "|";
	public static final String POTENTIAL_PROMO_MESSAGE = "PotentialPromotion".intern();
	public static final String CART_PROMO = "OrderPromotion".intern();
	public static final String CASHBACK_FIREDPROMO_MESSAGE = "CashBackFired".intern();
	public static final String PRODUCT_PROMO = "ProductPromotion".intern();
	public static final String PROMOGROUP = "mplPromoGrp";
	public static final String OTHER_PROMO = "OtherPromotion".intern();

	public final static String DELIVERYCHARGE_PROMOTION_MAP_KEY = "currDelCharge";
	//For Consignment Query
	public static final String CONSIGNMENTNQUERY = "select {c:pk} from {Consignment As c} WHERE {c.code}=?consignmentCode"
			.intern();
	public static final String CONSIGNMENTCODE = "consignmentCode".intern();

	public final static String SESSION_PINCODE = "defaultPinCode";
	public final static String SESSION_PINCODE_PDP = "pincode";

	//For Cancellation Reason Query
	public static final String CANCELLATIONREASONQUERY = "select {c:pk} from {CancellationReason As c}".intern();


	//Add to Cart
	public static final String MAX_ORDER_QUANTITY_EXCEEDED = "maxOrderQuantityExceeded";
	public static final String LOWSTOCK = "lowStock";
	public static final String NOSTOCK = "noStock";
	public static final String TYPE_MISMATCH_ERROR_CODE = "typeMismatch";
	public static final String ERROR_MSG_TYPE = "errorMsg";
	public static final String QUANTITY_INVALID_BINDING_MESSAGE_KEY = "basket.error.quantity.invalid.binding";
	public static final String MINIMUM_CONFIGURED_QUANTIY = "mpl.cart.minimumConfiguredQuantity.lineItem";
	public static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";
	public static final String CROSSED_MAX_LIMIT = "crossedMaxLimit";
	public static final String REACHED_MAX_LIMIT = "reachedMaxLimit";
	public static final String WISHLIST_DISPLAY_QUANTITY = "mpl.cart.wishlist.display.quantity";
	public static final String OUT_OF_INVENTORY = "outofinventory";
	public static final String INVENTORY_WIIL_EXCEDE = "willexceedeinventory";

	//For SellerPriority Report
	public static final String CSVFILEHEADER_SELLERPRIORITY = "Modified Time, User ID, Seller ID, Seller Name, Category ID, Product ID, Start Date, End Date, Active?, Newly_Created?, Modified_Start Date, Modified_End Date, Modified_Active Flag";

	//System/Non Business constants
	public static final String E0000 = "E0000";
	public static final String E0001 = "E0001";
	public static final String E0002 = "E0002";
	public static final String E0005 = "E0005";
	public static final String E0006 = "E0006";
	public static final String E0007 = "E0007";
	public static final String E0008 = "E0008";
	public static final String E0009 = "E0009";
	public static final String E0010 = "E0010";
	public static final String E0011 = "E0011";
	public static final String E0012 = "E0012";
	public static final String E0013 = "E0013";
	public static final String E0014 = "E0014";
	public static final String E0015 = "E0015";
	public static final String E0016 = "E0016";
	//System/Non Business constants

	//pdp error constants
	public static final String B3000 = "B3000";
	public static final String B3001 = "B3001";
	public static final String B3002 = "B3002";
	public static final String B3003 = "B3003";
	public static final String B3004 = "B3004";
	//pdp error constants

	// My Account Constants
	public static final String B0001 = "B0001";
	public static final String B0002 = "B0002";
	public static final String B0003 = "B0003";
	public static final String B0004 = "B0004";
	public static final String B0005 = "B0005";
	public static final String B0006 = "B0006";
	public static final String B0007 = "B0007";
	public static final String B0008 = "B0008";
	public static final String B0009 = "B0009";
	public static final String B0010 = "B0010";
	public static final String B0011 = "B0011";
	public static final String B0012 = "B0012";
	public static final String B0013 = "B0013";
	public static final String B0014 = "B0014";
	public static final String B0015 = "B0015";
	// My Account Constants ends

	//Mobile web service error codes starts
	public static final String B9000 = "B9000";
	public static final String B9001 = "B9001";
	public static final String B9002 = "B9002";
	public static final String B9003 = "B9003";
	public static final String B9004 = "B9004";
	public static final String B9005 = "B9005";
	public static final String B9006 = "B9006";
	public static final String B9007 = "B9007";
	public static final String B9008 = "B9008";
	public static final String B9009 = "B9009";
	public static final String B9010 = "B9010";
	public static final String B9011 = "B9011";
	public static final String B9012 = "B9012";
	public static final String B9013 = "B9013";
	public static final String B9014 = "B9014";
	public static final String B9015 = "B9015";
	public static final String B9016 = "B9016";
	public static final String B9017 = "B9017";
	public static final String B9018 = "B9018";
	public static final String B9019 = "B9019";
	public static final String B9020 = "B9020";
	public static final String B9021 = "B9021";

	public static final String B9022 = "B9022";
	public static final String B9023 = "B9023";
	public static final String B9024 = "B9024";
	public static final String B9025 = "B9025";
	public static final String B9026 = "B9026";
	public static final String B9027 = "B9027";
	public static final String B9028 = "B9028";
	public static final String B9029 = "B9029";
	public static final String B9030 = "B9030";
	public static final String B9031 = "B9031";
	public static final String B9032 = "B9032";
	public static final String B9033 = "B9033";
	public static final String B9034 = "B9034";
	public static final String B9035 = "B9035";
	public static final String B9050 = "B9050";
	public static final String B9051 = "B9051";
	public static final String B9052 = "B9052";
	public static final String B9053 = "B9053";
	public static final String B9054 = "B9054";
	public static final String B9055 = "B9055";
	public static final String B9036 = "B9036";
	public static final String B9037 = "B9037";
	public static final String B9038 = "B9038";
	public static final String B9039 = "B9039";
	public static final String B9058 = "B9058";
	public static final String B9059 = "B9059";

	public static final String B9040 = "B9040";
	public static final String B9041 = "B9041";
	public static final String B9042 = "B9042";
	public static final String B9043 = "B9043";
	public static final String B9044 = "B9044";
	public static final String B9045 = "B9045";
	public static final String B9046 = "B9046";
	public static final String B9047 = "B9047";
	public static final String B9048 = "B9048";
	public static final String B9049 = "B9049";
	public static final String B9057 = "B9057";

	public static final String B9060 = "B9060";
	public static final String B9061 = "B9061";
	public static final String B9062 = "B9062";
	public static final String B9063 = "B9063";

	public static final String B9071 = "B9071";
	public static final String B9072 = "B9072";
	public static final String B9073 = "B9073";
	public static final String B9074 = "B9074";
	public static final String B9075 = "B9075";


	public static final String E9040 = "E9040";
	public static final String E9041 = "E9041";
	public static final String E9042 = "E9042";
	public static final String E9043 = "E9043";
	public static final String E9044 = "E9044";
	public static final String E9045 = "E9045";
	public static final String E9046 = "E9046";
	public static final String E9047 = "E9047";
	public static final String E9048 = "E9048";
	public static final String E9050 = "E9050";
	public static final String E9051 = "E9051";

	public static final String B9056 = "B9056";
	public static final String B9064 = "B9064";
	public static final String B9065 = "B9065";
	public static final String B9066 = "B9066";
	public static final String B9067 = "B9067";
	public static final String B9068 = "B9068";
	public static final String B9069 = "B9069";
	public static final String B9070 = "B9070";

	public static final String B9100 = "B9100";
	public static final String B9101 = "B9101";
	public static final String B9102 = "B9102";
	//Mobile web service error codes ends

	//Payment Error Codes
	public static final String B6001 = "B6001";
	public static final String B6002 = "B6002";
	public static final String B6003 = "B6003";
	public static final String B6004 = "B6004";
	public static final String B6005 = "B6005";
	public static final String B6006 = "B6006";
	public static final String B6007 = "B6007";
	public static final String B6008 = "B6008";

	//Search error codes starts
	public static final String B7000 = "B7000";
	public static final String B7001 = "B7001";
	public static final String B7002 = "B7002";
	public static final String B7003 = "B7003";
	public static final String B7004 = "B7004";
	public static final String B7005 = "B7005";
	public static final String B7006 = "B7006";
	public static final String B7007 = "B7007";
	public static final String B7008 = "B7008";
	public static final String B7009 = "B7009";
	public static final String B7010 = "B7010";
	public static final String B7011 = "B7011";
	public static final String B7012 = "B7012";
	public static final String B7013 = "B7013";
	public static final String B7014 = "B7014";
	public static final String B7015 = "B7015";
	public static final String B7016 = "B7016";
	public static final String B7017 = "B7017";
	public static final String B7018 = "B7018";
	public static final String B7019 = "B7019";
	public static final String B7020 = "B7020";
	public static final String B7021 = "B7021";

	public static final String B9200 = "B9200";
	public static final String B9201 = "B9201";
	public static final String B9202 = "B9202";
	public static final String B9800 = "B9800";
	public static final String B9801 = "B9801";
	public static final String B9802 = "B9802";
	public static final String B9803 = "B9803";
	public static final String B9203 = "B9203";
	public static final String B9204 = "B9204";
	public static final String B9205 = "B9205";
	public static final String B9206 = "B9206";
	public static final String B9207 = "B9207";
	public static final String B9208 = "B9208";
	public static final String B9209 = "B9209";
	public static final String B9210 = "B9210";
	public static final String B9211 = "B9211";
	public static final String B9212 = "B9212";


	//Search error codes ends

	//Browse Error code
	public static final String B2000 = "B2000";
	public static final String B2001 = "B2001";
	public static final String B2002 = "B2002";

	//For Sales Report
	public static final String DATE_FORMAT_REPORT = "ddMMyyyyHHmmss";
	public static final String ORDER_ERROR = "Order not found in current BaseStore";
	public static final String CSV_ERROR = "Error in CsvFileWriter !!!";
	public static final String FILE_WRITER_ERROR = "Error while flushing/closing fileWriter !!!";
	public static final String ORDER_CURRENCY_ERROR = "source order currency must not be null";

	public static final String FILE_PATH = "_";
	public static final String SALES_REPORT_INCREMENTAL = "incremental";
	public static final String SALES_REPORT_FULL = "full";
	public static final String SALES_REPORT_QUERY = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE + "} WHERE {"
			+ OrderModel.TYPE + "}=?type";
	public static final String SALES_REPORT_QUERY_START = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE
			+ "} WHERE " + "{" + OrderModel.CREATIONTIME + "} >=?fromDate AND {" + OrderModel.TYPE + "}=?type";
	public static final String SALES_REPORT_QUERY_START_END = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE
			+ "} WHERE {" + OrderModel.CREATIONTIME + "} >= ?startDate AND {" + OrderModel.CREATIONTIME + "} <=?endDate AND {"
			+ OrderModel.TYPE + "}=?type";


	public static final String NOEMIBANKLIST = "EMI Bank list is not available , Please Enter the correct data";
	public static final String NOEMITERMKLIST = "EMI Term list is not available , Please Enter the correct data";
	public static final String NONETBANKINGLIST = "Net Banking is not available , Please Enter the correct data";

	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String RETURNQUERY = "SELECT {" + ReturnOrderModel.PK + "} FROM {" + ReturnOrderModel._TYPECODE
			+ "} WHERE {" + ReturnOrderModel.CREATIONTIME + "} >= ?startDate AND {" + ReturnOrderModel.CREATIONTIME
			+ "} <=?endDate ";


	public static final String CART_NULL = "Cart model cannot be null";

	public static final String POS_NULL = "Point Of Service Cannot be null";

	public static final String UNKNOWN_ENTRY = "Unknown entry number";

	public static final String ENTRY_NOT_UPDATEABLE = "Entry is not updatable";

	public static final String BRANDSLIST = "brands".intern();

	//For Bulk Upload in Promotion Restriction
	public static final String EXCLUDEBRANDRESTRICTION = "ExcludeManufacturersRestriction".intern();
	//For Refund Report
	public static final String REFUND_REPORT_QUERY = "SELECT {" + RefundEntryModel.PK + "} FROM {" + RefundEntryModel._TYPECODE
			+ "} ";
	public static final String REFUND_REPORT_QUERY_START = "SELECT {" + RefundEntryModel.PK + "} FROM {"
			+ RefundEntryModel._TYPECODE + "} WHERE {" + RefundEntryModel.REFUNDEDDATE + "} >= ?startDate ";
	public static final String REFUND_REPORT_QUERY_START_END = "SELECT {" + RefundEntryModel.PK + "} FROM {"
			+ RefundEntryModel._TYPECODE + "} WHERE {" + RefundEntryModel.REFUNDEDDATE + "} >= ?startDate AND {"
			+ RefundEntryModel.REFUNDEDDATE + "} <=?endDate ";

	//For Replacement Report
	public static final String REPLACE_REPORT_QUERY = "SELECT {" + ReplacementEntryModel.PK + "} FROM {"
			+ ReplacementEntryModel._TYPECODE + "} ";
	public static final String REPLACE_REPORT_QUERY_START = "SELECT {" + ReplacementEntryModel.PK + "} FROM {"
			+ ReplacementEntryModel._TYPECODE + "} WHERE {" + ReplacementEntryModel.CREATIONTIME + "} >= ?startDate ";
	public static final String REPLACE_REPORT_QUERY_START_END = "SELECT {" + ReplacementEntryModel.PK + "} FROM {"
			+ ReplacementEntryModel._TYPECODE + "} WHERE {" + ReplacementEntryModel.CREATIONTIME + "} >= ?startDate AND {"
			+ ReplacementEntryModel.CREATIONTIME + "} <=?endDate ";

	//For Cancel Report
	public static final String CANCELLED_REPORT_QUERY = "SELECT {" + OrderCancelRecordEntryModel.PK + "} FROM {"
			+ OrderCancelRecordEntryModel._TYPECODE + "} ";
	public static final String CANCELLED_REPORT_QUERY_START = "SELECT {" + OrderCancelRecordEntryModel.PK + "} FROM {"
			+ OrderCancelRecordEntryModel._TYPECODE + "} WHERE {" + OrderCancelRecordEntryModel.CREATIONTIME + "} >= ?startDate ";
	public static final String CANCELLED_REPORT_QUERY_START_END = "SELECT {" + OrderCancelRecordEntryModel.PK + "} FROM {"
			+ OrderCancelRecordEntryModel._TYPECODE + "} WHERE {" + OrderCancelRecordEntryModel.CREATIONTIME
			+ "} >= ?startDate AND {" + OrderCancelRecordEntryModel.CREATIONTIME + "} <=?endDate ";


	public static final String REFUND_ERROR = "Refund not found in current BaseStore";
	public static final String REFUND_CONVERSION_ERROR = "Refund convertForOrderRefund error";

	public static final String ORDER_QUERY = "select {o:pk} from {Order As o} WHERE {o.type}=?type  AND {o.code}=?code";

	public static final String ORDER_STATUS_QUERY = "select {os:pk} from {OrderStatusCodeMaster As os} WHERE {os.statusCode} = ?code";

	public static final String ORDER_STATUS_QUERY_ALL = "select {os:pk} from {OrderStatusCodeMaster As os} ";

	public static final String INVALIDCATEGORYID = "Category id is not found , please enter valid category ID";
	public static final String INVALIDSEARCHKEY = "Search key is not found , please enter valid Search Key";
	public static final String SEARCHNOTFOUND = "No result found";


	public static final String SUBMITTED = "SUBMITTED".intern();
	public static final String COMPLETED = "COMPLETED".intern();
	public static final String PENDING = "PENDING".intern();
	public static final String REVIEW = "REVIEW".intern();
	public static final String APPROVED = "APPROVED".intern();
	public static final String REJECTED = "REJECTED".intern();
	public static final String DECLINED = "DECLINED".intern();

	//EbsRiskLevel
	public static final String RED = "RED".intern();
	public static final String YELLOW = "YELLOW".intern();
	public static final String GREEN = "GREEN".intern();

	//EBSCountry
	public static final String INDIA = "INDIA".intern();

	//Delhi team changes
	public static final String USERREVIEWS = "User Reviews";
	public static final String CUSTOMERSURVEYS = "Consumer Surveys";
	public static final String TYPE_CATEGORY = "category".intern();
	public static final String TYPE_BRAND = "brand".intern();
	public static final String FRIEND_EMAIL_MESSAGE_TEXT = "Hey,|I was blown away by the incredible shopping experience at Tata Mall. I highly recommend that you register as a member as well";
	public static final String INVITE_FRIENDS_MESSAGE_KEY = "account.invite.friends.default.message.text";

	public static final String PROMOALERTSENTGROUP = "promotionusergroup".intern();
	public static final String CODE = "code".intern();
	public static final String MPL = "mpl".intern();
	public static final String FREE = "Free".intern();
	public static final String JUSPAYMERCHANTKEYNOTFOUND = "No juspay MerchantKey is defined in local properties";
	public static final String JUSPAYMERCHANTIDNOTFOUND = "No juspay MerchantId is defined in local properties";
	public static final String FACEBOOK = "Facebook";
	public static final String GOOGLE = "Google";


	//FOR TRACK ORDER NOTIFICATION
	public static final String NOTIFICATION_COUNT = "notification.count";
	public static final String NOTIFICATION_COUNT_MOBILE = "notification.count.mobile";
	public static final String CUSTOMER_STATUS_FOR_COUPON_NOTIFICATION = "notification.coupon.status";

	public static final String BASESTORE_UID = "mpl".intern();
	public static final String WEBHOOK_ENTRY_EXPIRED = "0".intern();
	public static final String EBS_STATUS_REVIEW = "REVIEW".intern();
	public static final String EBS_STATUS_APPROVED = "APPROVED".intern();
	public static final String EBS_STATUS_REJECTED = "REJECTED".intern();
	public static final String EBS_STATUS_IGNORED = "IGNORED".intern();
	public static final String EBS_STATUS_FRAUD = "FRAUD".intern();
	public static final String EBS_STATUS_CHARGEBACK = "CHARGEBACK".intern();

	/*************************************************
	 * For Webhook Service
	 *********************************************************************/
	//orderStatusResponse
	public static final String MERCHENTID = "merchant_id".intern();
	public static final String ORDERID = "order_id".intern();
	public static final String CUSTOMER_ID = "customer_id".intern();
	public static final String CUSTOMEREMAIL = "customer_email".intern();
	public static final String CUSTOMERPHONE = "customer_phone".intern();
	public static final String PRODUCTID = "product_id".intern();
	public static final String ORDERSTATUS = "status".intern();
	public static final String STATUSID = "status_id".intern();
	public static final String CURRENCY = "currency".intern();
	public static final String REFUNDEDED = "refunded".intern();
	public static final String AMOUNTREFUNDED = "amount_refunded".intern();
	public static final String RETURN_URL = "return_url".intern();
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
	public static final String PAYMENTMETHOD = "payment_method".intern();
	public static final String PAYMENTMETHODTYPE = "payment_method_type".intern();
	public static final String PAYMENT_METHOD_NB = "NB".intern();

	// Order Response

	public static final String CARD = "card".intern();
	public static final String PAYMENT_GATEWAY_RESPONSE = "payment_gateway_response".intern();
	//product detail for product code web service
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
	public static final String EBSRISKLEVEL = "ebsRiskLevel".intern();
	public static final String EBSRISKPERCENTAGE = "ebsRiskPercentage".intern();
	public static final String EBSRISKSTATUS = "ebsRiskStatus".intern();
	public static final String EBSBINCOUNTRY = "ebs_bin_country".intern();
	public static final String PAID = "PAID".intern();

	public static final String DATEFORMAT = "yyyy-MM-dd".intern();
	public static final String DATEFORMAT_FULL = "MMM dd yyyy".intern();
	public static final String ORDER = "order".intern();
	public final static String DECIMALERROR = "Can't seem to understand the input".intern();
	public static final String DEFAULT_EBS_RISK_PERC = "-1.0".intern();

	//For Refund
	public static final String REFUND_STATUS = "status".intern();
	public static final String REF = "ref".intern();
	public static final String ID = "id".intern();
	public static final String REFUNDS = "refunds".intern();
	public static final String REFUND_INITIATED = "REFUND_INITIATED".intern();
	public static final String REFUND_SUCCESSFUL = "REFUND_SUCCESSFUL".intern();
	public static final String REFUND_UNSUCCESSFUL = "REFUND_UNSUCCESSFUL".intern();
	public static final String REFUND_IN_PROGRESS = "REFUND_IN_PROGRESS".intern();

	public static final String REFUND_PENDING = "PENDING".intern();
	public static final String REFUND_SUCCESS = "SUCCESS".intern();
	public static final String REFUND_FAILURE = "FAILURE".intern();
	public static final String UNIQUE_REQUEST_ID = "unique_request_id".intern();

	/**********************************************************************************************************************/

	public final static String GIFT_QUANTITY = "freeGiftQuantity".intern();
	public static final String LIST_VAL_RESIDENTIAL = "Residential";
	public static final String LIST_VAL_COMMERCIAL = "Commercial";

	public static final String REDIRECT = "redirect:";
	public static final String CART = "/cart";

	public static final String NO_GIFT_AVAILABLE = "No gift products are available";
	public static final String MICROSITE_SNS_CATEGORY = "micrositeSnsCategory";

	public static final String FULL_ADDRESS_FROM_FORM = "Full Address from Form :";
	public static final String FULL_ADDRESS_FROM_MODEL = "Full Address from Model :";

	// Local properties file keys
	public static final String PROPERTY_KEY_OMS_AWB_ACTIVE = "oms.awb.active";

	public static final String CANCEL_STATUS = "valid.order.statuses.CANCELLATION";
	public static final String CANCEL_ORDER_STATUS = "valid.order.statuses.CANCELLATION_ORDER";
	public static final String DELIVERED = "DELIVERED";
	public static final String VALID_APPROVED = "valid.order.statuses.APPROVED";
	public static final String VALID_SHIPPING = "valid.order.statuses.SHIPPING";
	public static final String VALID_PROCESSING = "valid.order.statuses.PROCESSING";
	public static final String VALID_RETURN = "valid.order.statuses.RETURN";
	public static final String VALID_CANCEL = "valid.order.statuses.CANCEL";
	public static final String VALID_DELIVERY = "valid.order.statuses.DELIVERY";
	//Post Seller Master Seller Name
	public static final String SELLERMASTER_NAME = "firstname".intern();
	public static final String CUSTOMER_MODEL_CANNOT_BE_NULL = "Customer model cannot be null";
	public static final String STORE_MUST_NOT_BE_NULL = "Store must not be null";
	public static final String PAGEABLEDATA_MUST_NOT_BE_NULL = "PageableData must not be null";
	public static final String DUPLICATE_EMAIL = "Email Id already exists, please try with another email Id";
	public static final String TOKEN_CANNOT_BE_EMPTY = "The field [token] cannot be empty";
	public static final String NEWPASSWORD_CANNOT_BE_EMPTY = "The field [newPassword] cannot be empty";
	public static final String UID_CANNOT_BE_EMPTY = "The field [uid] cannot be empty";
	public static final String BAG_ABOUT_TO_FILL = "Sorry, we don't seem to have the quantity you need. You might want to lower the quantity.";
	public static final String BAG_IS_FULL = "Bag is full!";

	//added for getcartdetails
	public static final String INVALID_CART = "Invalid Cart";
	public static final String COULD_NOT_COVERT_CART = "Could not covert cart";

	public static final String PROMOTION_GROUP_DEFAULT = "default";

	public static final String CARTDATA = "No Data Available";
	public static final String PRE_SAVED_DETAIL_MAP = "preSavedDetailMap";
	public static final String UPDATE_CHANNEL_WEB = "web";
	public static final String UPDATE_CHANNEL_MOBILE = "mobile";
	public static final String LINK_MY_ACCOUNT = "/my-account";
	public static final String LINK_UPDATE_PROFILE = "/update-profile";
	public static final String MALFORMED_URL_EXCEPTION = "Malformed URL exception occurred";

	//	Update profile email sending parameters

	public static final String F_NAME = "fName";
	public static final String L_NAME = "lName";
	public static final String N_NAME = "nName";
	public static final String D_OF_BIRTH = "dOfBirth";
	public static final String D_OF_ANNIVERSARY = "dOfAnniversary";
	public static final String M_NUMBER = "mNumber";
	public static final String GENDER_FIELD = "gender";
	public static final String EMAIL_ID = "emailId";

	public static final String F_NAME_suffix = "fn";
	public static final String L_NAME_suffix = "ln";
	public static final String N_NAME_suffix = "nn";
	public static final String D_OF_BIRTH_suffix = "dob";
	public static final String D_OF_ANNIVERSARY_suffix = "doa";
	public static final String M_NUMBER_suffix = "mob";
	public static final String GENDER_suffix = "gen";
	public static final String EMAIL_ID_suffix = "email";
	public static final String PASSWORD_suffix = "pwd";

	public static final String QS = "?";
	public static final String EQUALS = "=";
	public static final String LINK_LOGIN = "/login";
	public static final String AFFILIATEID = "affiliateId";
	public final static String LOGERROR = "Failed to build beginCreateSubscription request";


	public static final String NO_CARTID_PROVIDED = "No Cart ID Provided";
	public static final String NO_PINCODE_PROVIDED = "Please enter pincode";

	//Payment User Group
	public static final String PAYMENTUSERGROUP = "paymentusergroup".intern();

	public static final String ISPERCENTAGEDISC = "isPercentageDisc".intern();
	public static final String PRODLEVELPERCENTAGEDISC = "prodLevelPercentageDisc".intern();
	public static final String CARTLEVELPERCENTAGEDISC = "cartLevelPercentageDisc".intern();

	public static final String PAYMENT_EMI_PROMOERROR = "payment.emi.promoErrorMsg".intern();

	public static final String PAYMENT_EXC_LOG = "Exception while saving payment info model with ".intern();
	public static final String PAYMENT_EXC_LOG_END = " : Exception while saving payment info model with ".intern();
	public static final String PAYMENT_TRAN_EXC_LOG = "Exception while saving payment transaction entry with ".intern();

	//Webhook Report Path
	public static final String WEBHOOKREPORTPATH = "webhook.report.path".intern();
	public static final String WEBHOOKREPORT = "webhookReport".intern();
	public static final String WEBHOOKREPORT_CSV_FILE_HEADER = "OrderId,AuditId,Amount,Status,RefundRequestId";

	public static final String FAILURE_FLAG = "FAILURE".intern();

	public static final String SOURCENOTNULL = "Parameter source cannot be null.";
	public static final String TARGETNOTNULL = "Parameter target cannot be null.";

	public static final String BINNOERROR = "Exception while checking BIN no: ";
	public static final String SAVEDCCERROR = "Exception while fetching saved credit cards::::";
	public static final String SAVEDDCERROR = "Exception while fetching saved debit cards::::";
	public static final String NOBANKAVAILABLE = "No Bank Details Available";
	public static final String DEPT_L0 = "L0";
	public static final String DEPT_L1 = "L1";
	public static final String DEPT_L2 = "L2";
	public static final String DEPT_L3 = "L3";
	public static final String DEPT_L4 = "L4";

	public static final String CART_URL = "/cart";
	public static final String UNUSED = "unused";

	public static final String CATEGORY = "category";
	public static final String DEPARTMENT = "department";
	public static final String SALES_DATA_REPORT_JOB_IP = "cronjob.salesreport.ipaddress";


	public static final String ORDERCODE = "code".intern();
	public static final String ORDERTYPE = "type".intern();

	public static final String EXCEPTIONONCONV = "Exception on converter: ";
	public static final String SELLERARTICLESKU = "sellerArticleSKU";

	public static final String SUBORDER = "SubOrder";
	public static final String PARENTORDER = "Parent";
	public static final String WEBHOOKUPDATEMSG = "Updating the Web Hook Enty with status EXPIRED".intern();
	public static final String QUERYFROM = "FROM {".intern();
	public static final String QUERYWHERE = "WHERE ".intern();
	public static final String QUERYEMAIL = "}=?email ".intern();

	public static final String ORDER_CONF_SUCCESS = "message.orderProcessed";
	public static final String ORDER_CONF_HELD = "message.orderheld";

	//FICO
	public static final String RETURN_COMPLETED = "RETURN_COMPLETED";
	public static final String ORDER_CANCELLED = "ORDER_CANCELLED";

	public static final String RETURN_FLAG = "RRF";
	public static final String CANCEL_FLAG = "CAN";

	public static final String CASH_ON_DELIVERY = "COD";

	// Added for delivery mode estimated description default time
	public static final String DEFAULT_START_TIME = "1";
	public static final String DEFAULT_END_TIME = "2";
	public static final String DELIVERYMODE_DESC_PREFIX = "Delivered in ";
	public static final String DELIVERYMODE_DESC_SUFFIX = " business days";

	public static final String ORDER_HISTORY_DURATION_DAYS = "order.history.duration.days";
	public static final String ORDER_HISTORY_DEFAULT_DURATION_DAYS = "180";
	public static final String FROM_DATE_MUST_NOT_BE_NULL = "From Date must not be null";

	public static final String ORDER_CODE = "ordercode";
	public static final String SUCCESS_VAL = "SUCCESS";
	public static final String PENDING_VAL = "PENDING";

	public static final String CART_DELISTED_SESSION_ID = "cartItemDelisted";
	public static final String CART_DELISTED_SESSION_MESSAGE = "Sorry! Some of items in your cart is no longer available";
	public static final String ORDERHISTORY_CREATE_ERROR = "Error while creating orderhistoryentry at ";
	public static final String BY_DATE = "byDate";
	public static final String BY_ORDER_NO = "byOrderNumber";
	public static final String FOUND = "Found ";
	public static final String CERTAINTY = "certainty";
	public static final String SEARCH_STATE_DATA_MSG = "SearchStateData must not be null.";

	public static final String EXCEPTION_ERROR = "An exception occured at backend".intern();
	public static final String ASYNC_ENABLE = "cancel.crm.task.async".intern();
	public static final String REFUNDTHRESHOLD = "payment.refund.adjustable.amount".intern();
	public static final String QUOTE = "\"";

	public static final String REVERSE_LOGISTIC_AVAILABLE_RESPONSE_MESSAGE = "We'll send our man to pick up this item. Check our return policy here."
			.intern();
	public static final String REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE = "You'll courier this item back to us. Well, how kind of you! "
			.intern();

	public static final String REVERSE_LOGISTIC_AVAILABLE_RESPONSE_DESC = "Reverse Logistics is available. Click on 'Continue' to proceed"
			.intern();
	public static final String REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC = "Only Self-Courier is available. Click on 'Continue' to proceed"
			.intern();

	public static final String PRODUCT_USSID_LIST = "freeGiftInfoMap".intern();
	public static final String BOGO_DETAILS_MAP = "cutomBogoPromoDataMap".intern();
	public static final String NULL = "null";
	public static final String MAN_WOMAN = "Man,Woman";

	public static final String CART_HOME_DELIVERY = "Home Delivery".intern();
	public static final String CART_EXPRESS_DELIVERY = "Express Delivery ".intern();
	// Seler Priority Report Query
	// Within date range
	public static final String SELLERPRIORITYWITHINDATEQUERY = "Select {s.pk} from {SavedValues as s}, {MplSellerPriority as sp },{SavedValueEntryType as st} where {s.modificationtype}={st.pk} and {s.modifieditem}={sp.pk} and {s.creationtime} BETWEEN ?startDate and ?endDate order by {s.Timestamp} desc";
	//	public static final String SELLERPRIORITYWITHINDATEQUERY = "Select {s.pk} from {SavedValueEntry as se},{SavedValues as s}, {Bin as bk }, {SavedValueEntryType as st} where {s.modificationtype}={st.pk} and {st.code} = 'changed'and {s.modifieditem}={bk.pk} and {s.pk} = {se.Parent} and {s.creationtime} BETWEEN ?startDate and ?endDate ";

	// full data
	public static final String SELLERPRIORITYQUERY = "Select {s.pk} from {SavedValues as s}, {MplSellerPriority as sp },{SavedValueEntryType as st} where {s.modificationtype}={st.pk}  and {s.modifieditem}={sp.pk} order by {s.Timestamp} desc";

	public static final String SELLERPRIORITYDATAQUERY = "Select {sp.pk} from  {MplSellerPriority as sp }";


	public static final String CARTQUERY = "select {c:pk} from {Cart As c} where {c.guid}=?guid".intern();
	public static final String ISBUYAGETPROMO = "isBuyAGetPromo".intern();

	public static final String VOUCHERWITHINDATEQUERY = "select {d.voucher} from {DateRestriction as d} where sysdate>={d.startdate} and sysdate<={d.enddate}";

	public static final String GETPROMOTIONS = "select {p:pk} from {AbstractPromotion as p} where {p.enabled}='1' and sysdate<={p.enddate} and sysdate>={p.startdate} and {immutableKeyHash} is null";
	public static final String PRODUCT_PROMO_PERCENTAGE_FIRE_MSG = "product.promotion.firedMessage.ifPercentage";
	public static final String VOUCHERWITHINDATEQUERYFROMCOUPONMODEL = "select {p:pk} from {VoucherStatusNotification as p} where {p.voucherStartDate}<=?sysdate and {p.voucherEndDate}>=?sysdate ";


	//Coupon
	public static final String ZEROPOINTZEROONE = "0.01".intern();


	public static final String CAMPAIGN_DISCOUNT = "DISCOUNT OFFER".intern();
	public static final String CAMPAIGN_FREEBIE = "FREEBIE OFFER".intern();
	public static final String CAMPAIGN_SHIPPING = "SHIPPING DISCOUNT OFFER".intern();
	public static final String CAMPAIGN_BOGO = "BOGOF OFFER".intern();
	public static final String CAMPAIGN_CASHBACK = "CASHBACK OFFER".intern();
	public static final String CAMPAIGN_CHANNEL = "WEB|WEBMOBILE|MOBILE|CALLCENTER|KIOSK".intern();
	public static final String CAMPAIGN_MULTIDATA_SEPERATOR = "|".intern();
	public static final String CAMPAIGN_FILE_LOCATION = "campaign.promotion.csv.path".intern();
	public static final String CAMPAIGN_HEADER = "OFFER_ID,OFFER_NAME,OFFER_ACTIVE,OFFER_TYPE,OFFER_CHANNEL,OFFER_STARTDATE,OFFER_ENDDATE,URL,CREATION_DATE,MODIFIED_DATE"
			.intern();
	public static final String CAMPAIGN_FILE_DELIMITTER = ",".intern();
	public static final String CAMPAIGN_FILE_NEW_LINE_SEPARATOR = "\n".intern();
	public static final String CAMPAIGN_FILE_NAME = "campaign".intern();
	public static final String CAMPAIGN_WEBSITE = "http://tatacliq.com/store".intern();
	public static final String CAMPAIGN_URL_ALL = "all".intern();

	public static final String CAMPAIGN_URL_OFFER_IDENTIFIER = "/o/".intern();
	public static final String CAMPAIGN_URL_OFFER_ID_URL = "?offer=".intern();

	private MarketplacecommerceservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
