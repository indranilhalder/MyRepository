/*

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

import de.hybris.platform.core.model.BulkCancellationProcessModel;
import de.hybris.platform.core.model.BulkReturnProcessModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReturnOrderModel;

import java.util.Date;

import com.tisl.mpl.core.model.FreebieDetailModel;
import com.tisl.mpl.core.model.ProductFreebieDetailModel;


/**
 * Global class for all Marketplacecommerceservices constants. You can add global constants for your extension into this
 * class.
 */
@SuppressWarnings(
{ "PMD", "deprecation" })
public final class MarketplacecommerceservicesConstants extends GeneratedMarketplacecommerceservicesConstants
{

	//TPR-6272 starts here IQA
	public static final String COMMACONSTANT = ",";
	public static final int PLATFORM_ZERO = 0;
	public static final int PLATFORM_ONE = 1;
	public static final int PLATFORM_TWO = 2;
	public static final int PLATFORM_THREE = 3;
	public static final int PLATFORM_FOUR = 4;
	public static final int PLATFORM_FIVE = 5;
	//TPR-6272 ends here
	//TPR-4461 STARTS HERE
	public static final String COUPONFAILUREMESSAGE = "Sorry,Voucher is not applicable for the PAYMENT MODE/BANK you have selected.In order to proceed with this payment mode, please release the coupon or select an alternative payment mode";

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
	public static final String IS_BOGO_APPLIED = "isBOGOapplied";
	public static final String BOGO_ITEM_COUNT = "bogoFreeItmCount";
	public static final String FINEJEWELLERY = "FineJewellery";
	public static final String FASHIONJEWELLERY = "FashionJewellery";

	//SONAR FIX
	public static final String SHIPPING = "SHIPPING";
	public static final String CONSIGNMENT_STATUS = " Consignment status :";
	public static final String BOXING = "boxing";
	public static final String APPLICATION_JSON_VALUE = "application/json";
	public static final String NEW_PASSWORD = "newPassword";
	public static final String PRICE_NOT_AVAILABLE = "Price not Available";
	public static final String PWD = "pwd";

	public static final String ZeroDeliveryCost = "0.0";

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
	public static final String COUPONS_TXN_DATE_FORMAT = "MM";
	public static final String TRUE = "true";
	public static final String TRUE_UPPER = "TRUE";
	public static final String ZERO = "0";
	public static final String LINK_PASSWORD_CHANGE = "/login/pw/change";
	//Sonar Fix Jewellery
	public static final String STONE = "Stone";
	public static final String SIZE = "size";

	//For Promotion Intercepter

	public static final String PROMOCODE = "Promotion Identifier :".intern();
	public static final String PROMOPRODUCT = "Promotion Product :".intern();
	public static final String PROMOCATEGORY = "Promotion Category :".intern();
	public static final String PROMOPRIORITY = "Promotion Priority :".intern();
	public static final String PRODUCT_PRICE_COLUMN = "price".intern();
	public static final String PRESENT_CATEGORY = "Present Category :".intern();

	public static final String PROMO_ERROR_MESSAGE = "Cannot exceed 25 characters.".intern();
	//public static final String PROMO_ERROR_MESSAGE =
	//"Title cannot exceed 25 characters.".intern();

	//For Bulk Orders Return Initiation
	public static final String COMMA_DELIMITER = ",";
	//public static final String TICKETTYPECODE = "R";
	//public static final String REFUNDTYPE = "S";
	//public static final String REASONCODE = "03"; //Hard coded value -- I'm
	//not happy with the product quality

	//Bulk Cancellation
	public static final String initiate_cancel_job_cancellation_count = "initiate.cancel.job.cancellation.count";

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

	//Return Item
	public static final String REVERCE_LOGISTIC_PINCODE_SERVICEABLE_NOTAVAIL_MESSAGE = "SORRY! We cannot pickup from the address provided, Please provide other address or You can Self - ship and let us know!";

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
	//public static final String CALLINGPAYUSERVICE =
	//"Calling PAYU service:::::::";
	//public static final String ORDERAMOUNT = "Amount of Order::::";
	//public static final String BANKCODE = "Bank Code:::::";
	//public static final String CUSTOMEREMAIL = "Email of Customer::::";
	//public static final String CUSTOMERFIRSTNAME =
	//"Customer's FirstName:::::";
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
	public static final String na = "na".intern();
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
	public static final String DINERS = "DINERS".intern();
	public static final String JCB = "JCB".intern();
	public static final String DISCOVER = "DISCOVER".intern();
	public static final String VISA = "VISA".intern();
	public static final String EUROCARD = "EuroCard".intern();
	public static final String SWITCHCARD = "SwitchCard".intern();
	public static final String ROOTCATEGORY = "marketplace.mplcatalog.rootcategory.code".intern();
	public static final String RETURNURL = "payment.juspay.returnUrl".intern();
	public static final String ON = "on".intern();
	public static final String WAREHOUSE = "mpl_warehouse";

	//Luxury Return URL
	public static final String RETURNURLLUX = "payment.juspay.returnUrl.lux".intern();

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

	//TISPRO-179
	public static final String EMIBANK_FOR_BANKNAMES_QUERY = "select {b:pk} from {emiBank As b} ,{bank as m} where {b.emiLowerLimit}<=?cartValue and {b.emiUpperLimit}>=?cartValue and {b.name}={m.pk}  and upper({m.bankname}) = ?bankName order by {m.bankname}"
			.intern();

	public static final String EMIBANTERMSSQUERY = "select {e:pk} from {emibank as e},{bank as b} where {e.name}={b.pk} and {b.bankName}=?bank"
			.intern();
	public static final String PAYMENTTYPEFORAPPORTIONQUERY = "select {p:pk} from {PaymentType As p} WHERE {p.mode}=?paymentMode and {b.basestore}=?baseStore"
			.intern();
	public static final String BANKFORBINQUERY = "select {b:pk} from {Bin As b} WHERE {b.binno}=?bin".intern();
	public static final String GETPAYMENTTRANSACTIONFROMCARTQUERY = "select {p:pk} from {PaymentTransaction as p},{Cart as c},{Customer as u} where {p.order}={c.pk} and {c.user}={u.pk} and {u.uid}=?customerId and {p.code}=?juspayOrderId and {p.status}='success' "
			.intern();
	public static final String MOBILEBLACKLIST = "select {b:pk} from {blacklist As b} where {b.phoneNumber} =?phoneNumber"
			.intern();
	public static final String MOBILENO = "phoneNumber".intern();
	public static final String PAYMENTMODETYPE = "select {b:pk} from {PaymentType As b} where {b.mode}=?mode and {b.basestore}=?baseStore"
			.intern();
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

	//TISPRO-179

	public static final String BANKMODELQUERY = "select {bnk.pk} from {bank as bnk} where upper({bnk.bankname}) =?bankname";

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
	public static final String BOTH = "both".intern();
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

	//No WishList Available
	public static final String NOWISHLISTAVAILABLE = "No Wishlist Available for this user".intern();

	//Mobile all cat
	public static final String SALESCATEGORYTYPE = "marketplace.mplcatalog.salescategory.code";
	public static final String DEFAULTCATALOGID = "cronjob.promotion.catelog";
	public static final String DEFAULTLUXURYCATALOGID = "cronjob.luxury.promotion.catelog";
	public static final String DEFAULTLUXURYSITEID = "luxury.site.id";
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
	public static final String SLAVE_MASTER_XSD_PATH = "xsd/Slave_Master_v1.1.xsd";
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
	public static final String SEARCHPAGE = "searchPage";
	public static final String CARTPAGE = "cartPage";
	public static final String LUXURYSEARCHPAGE = "luxurySearchPage";
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
	//0 1 2 3 4 5 6 7 8
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
	public static final String OTP_SENT = "Your OTP has been sent."; //UF-277
	public static final String OTP_EXPIRY_MESSAGE = "Sorry! This OTP has expired.";
	public static final String INVALID_OTP = "The OTP entered is incorrect or invalid."; //UF-277
	public static final String OTPERROR = "Please Enter valid OTP";
	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";
	public static final int SHOP_BY_LOOK_PAGE_SIZE = 2;

	//Changes for Delivery Mode
	public static final String INR = "INR";
	public static final String HD = "HD";
	public static final String ED = "ED";
	public static final String CnC = "CNC"; //Changed after SAP code merging
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

	public static final String SMS_SENDER_ID = "marketplace.sms.sender.name".intern();
	public static final String SMS_MESSAGE_FORGOT_PWD = "Dear Customer, One Time Password for your request is {0}. Please enter the same to submit the request. Regards, Team Tata Unistore.";
	/* 1.Anniversary Changes */
	public static final String SMS_MESSAGE_ORDER_PLACED = "Hi {0}, your order no ({1}) is confirmed. While we will keep you posted, track your order at {2}. Hope you enjoyed shopping with us.";

	public static final String SMS_MESSAGE_ORDER_SHIPPED = "Hey! we have shipped {0} item(s) of your order #{1} via {2}.Give it 2-3 working days to reach you. Can't control the excitement? Track your order here {3} .Thanks!";
	public static final String SMS_MESSAGE_ORDER_OUTFORDELIVERY_PREPAID = "Awesome news! {0} item(s) of your order  # {1} will reach you today. High five for shopping with us. Don't forget to download our app at {2} .";
	/* 3.Anniversary Changes */
	public static final String SMS_MESSAGE_ORDER_OUTFORDELIVERY_COD = "Hi, {0} item(s) of your order # {1} will reach you today. Please keep INR {2} ready for COD. Next time, shop on the go, on our app! {3} .";
	public static final String SMS_MESSAGE = "Test from TatauniStore";
	public static final String SMS_MESSAGE_COD_OTP = "Peek-a-boo {0}! One-time password for your COD order is {1}. Please feel free to call us at {2} in case of any queries.";
	public static final String SMS_MESSAGE_C2C_OTP = "Hi, one time password for your request is {0}. Please enter this to submit the request. Thanks!";
	public static final String SMS_MESSAGE_PAYMENT_PENDING = "Hmmm There seems to be a spot of bother. Please hold on.";
	public static final String SMS_MESSAGE_PAYMENT_FAILED = "Uh oh. Looks like your order was declined for some reason. Please try again.";
	public static final String SMS_MESSAGE_PAYMENT_TIMEOUT = "Oh no! Your order couldn't go through due to techincal issues. Please try again.";
	public static final String SMS_MESSAGE_INVENTORY_RESERVATION_FAILED = "Uh oh! Looks like what you wanted isn't available right now, but it could come back soon. Please try again later";

	public static final String PUSH_MESSAGE_ORDER_PLACED = "Awesome! We think your bag looks great. Track your order {0} in the Order History section of your account.";
	public static final String PUSH_MESSAGE_ORDER_OFD_PREPAID = "Hi! Your order {0} of {1} item(s) will reach you today. To know more, check out the Order History section of the app.";
	public static final String PUSH_MESSAGE_ORDER_OFD_COD = "Hi! Your order {0} of {1} item(s) will reach you today. Please keep INR {2} cash handy. To know more, check out the Order History section of the app.";
	/* 14. Anniversary Changes */
	public static final String PUSH_MESSAGE_ORDER_CANCELLED = "Hi, a refund of INR {0} has been initiated on the cancellation of {2} product(s). Check your email for details.";

	public static final String SMS_SERVICE_CONTACTNO = "marketplace.sms.service.contactno";
	public static final String SMS_ORDER_TRACK_URL = "marketplace.sms.order.track.url";
	public static final String SMS_ORDER_TRACK_LONG_URL = "marketplace.sms.track.longUrl";
	public static final String MPL_TRACK_ORDER_LONG_URL_FORMAT = "mpl.order.track.longurl.format".intern();
	public static final String SMS_SHORT_ORDER_TRACK_URL = "marketplace.sms.shortOrder.track.url";

	public static final String SMS_SERVICE_APP_DWLD_URL = "marketplace.sms.app.download.url";
	public static final String SMS_SERVICE_WEBSITE_URL = "marketplace.sms.order.website.url";

	//public static final String SMS_MESSAGE_HOTC =
	//"Hey! we have shipped {0} item(s) of your order #{1} via {2}.Give it 2-3 working days to reach you. Can't control the excitement? Track your order here {3} .Thanks!";
	/* 2.Anniversary Changes */
	public static final String SMS_MESSAGE_HOTC = "Hi! Your order # {1} with {0} item(s) has been shipped via {2}. You can track your order at {3}. Next time, shop on the go, on our app!";
	/* 5.Anniversary Changes */
	public static final String SMS_MESSAGE_ORDER_DELIVERY = "Hi, order no.{1} has been deliverd. We had tons of fun serving you. Check your email/account for details. Next time, shop on the go, on our app! {2}.";
	public static final String SMS_MESSAGE_REFUND_INITIATED = "Hey! We have initiated a refund of INR {0} to you against return of {1} item(s) of your order ref # {2} with us. Please check your email for more details.";
	/* 4.Anniversary Changes */
	public static final String SMS_MESSAGE_ORDER_UNDELIVERED = "Hi {0}! We tried delivering your order {1} with {2} item(s) but missed you. We will try again. Have a question? Call on {3}.";
	/* 7.Anniversary Changes */

	public static final String SMS_MESSAGE_ORDER_RISK = "We're sorry! Your Tata CLiQ order no.{0} has been put on hold for some checks. You might get a call from Tata CLiQ Care.";
	/* 8. Anniversary Changes */
	public static final String SMS_MESSAGE_ORDER_RISK_CONFIRMED = "Hi, your order no. {0} is now confirmed. While we'll keep you posted, track your order at {1}. Hope you enjoyed shopping with us.";
	/* 9.Anniversary Changes */
	public static final String SMS_MESSAGE_ORDER_RISK_REJECTED = "Hi, there's been a problem and your order no.{0} has been rejected. Log on to your account {1} or call us on {3} for more details.";
	//R2.3 Added ShipmentSecondaryStatus NEW SMS
	//public static final String SMS_MESSAGE_ADDRESS_ISSUE =
	//"Your order of {0}, in order no. {1} has been delayed due to issues involving your address. We deeply regret the inconvenience. We will call you within 48 hrs to confirm your address.";
	public static final String SMS_MESSAGE_ADDRESS_ISSUE = "Your order of {0} item, in order no. {1} has been delayed due to issues involving your address. We deeply regret the inconvenience. We will call you within 48 hrs to confirm your address.";

	public static final String SMS_MESSAGE_UNDELIVERED = "Your order of {0},  in order no. {1} is undelivered. We deeply regret the inconvenience. We will attempt to ship it to you within 48 hrs.";
	public static final String SMS_MESSAGE_OUT_FOR_DELIVERY = "Your order of {0}, items in order no. {1} is out for delivery. Are you excited? We are for sure! :D";
	public static final String SMS_MESSAGE_DISPATCH = "Your order of {0},  items in order no.{1} has been dispatched throught Blue Dart with AWB Number: {2} . We will try have it delivered to you within the next 2-4 days.";
	public static final String SMS_MESSAGE_DELIVERED = "Good news, everyone!  Your {0} {1}, was successfully delivered. We had tons of fun serving you. Hope you're happy with your CAMEL as well";
	public static final String SMS_MESSAGE_RTO_INITIATED = "We are  initiated RTO for your shipment. If you want delivery then let us know within 24 hr";
	public static final String SMS_MESSAGE_MIS_ROUTE = "Hi! Your order of {0}  in order no. {1} has been delay due to mis route. We deeply regret the inconvenience. ";

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

	public static final String SMS_MESSAGE_CD_OTP = "Peek-a-boo {0}! One-time password to Change delivery Address {1}. Please feel free to call us at {2} in case of any queries.";
	public static final String SMS_MESSAGE_RETURN_TO_STORE = "Hey {0}. You can return Order {1} at {2},  by {3} at store location:{4} .";

	//For Bulk Upload in Promotions
	public final static char FIELD_SEPARATOR = ',';

	public static final String BUYAALONGBGETSHIPPINGFREE = "BuyAalongBgetShippingFree".intern();
	public static final String BUYAANDBPERCENTAGEDISCOUNT = "BuyAandBPrecentageDiscount".intern();
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
	//TISSPTEN-116 remove full stop
	public static final String INTERESTED_IN_EMAIL = "I am interested in receiving e-mails";
	public static final String NOT_INTERESTED_IN_EMAIL = "I am not interested in receiving e-mails";
	public static final String BRAND_NAME_PREFIX = "MBH";

	public static final String CUSTOMERNOTFOUND = "Customer not found";
	public static final String NOBANKLIST = "bank list is not available";

	public static final String SSHIPCODE = "SSHIP";
	public static final String TSHIPCODE = "TSHIP";
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
	public final static String SESSION_PINCODE_RES = "sessionPincoderesponseData";

	public final static String SESSION_PINCODE_PDP = "pincode";

	//For Cancellation Reason Query
	public static final String CANCELLATIONREASONQUERY = "select {c:pk} from {CancellationReason As c}".intern();

	//Add to Cart
	public static final String MAX_ORDER_QUANTITY_EXCEEDED = "maxOrderQuantityExceeded";
	public static final String LOWSTOCK = "lowStock";
	public static final String NOSTOCK = "noStock";
	public static final String TYPE_MISMATCH_ERROR_CODE = "typeMismatch";
	public static final String ERROR_MSG_TYPE = "errorMsg";
	public static final String ERROR_MSG_TYPE_FREEBIE = "freebieErrorMsg";
	public static final String QUANTITY_INVALID_BINDING_MESSAGE_KEY = "basket.error.quantity.invalid.binding";
	public static final String MINIMUM_CONFIGURED_QUANTIY = "mpl.cart.minimumConfiguredQuantity.lineItem";
	public static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";


	//added for jewellery

	public static final String MINIMUM_CONFIGURED_QUANTIY_JEWELLERY = "mpl.cart.minimumConfiguredQuantityForJewellery.lineItem";
	public static final String MAXIMUM_CONFIGURED_QUANTIY_JEWELLERY = "mpl.cart.maximumConfiguredQuantityForJewellery.lineItem";

	//end

	public static final String CROSSED_MAX_LIMIT = "crossedMaxLimit";
	public static final String REACHED_MAX_LIMIT = "reachedMaxLimit";
	public static final String WISHLIST_DISPLAY_QUANTITY = "mpl.cart.wishlist.display.quantity";
	public static final String OUT_OF_INVENTORY = "outofinventory";
	public static final String INVENTORY_WIIL_EXCEDE = "willexceedeinventory";
	public static final String MAX_QUANTITY_ADDED_FOR_EXCHANGE = "maxqtyexchange";
	//TISJEWST-10
	public static final String MAX_QUANTITY_ADDED_FOR_FINEJEWELLERY = "maxqtyaddedforfinejewellery";

	//For SellerPriority Report
	public static final String CSVFILEHEADER_SELLERPRIORITY = "Modified Time, User ID, Seller ID, Seller Name, Category ID, Product ID, Start Date, End Date, Active?, Newly_Created?, Modified Swllwe ID, Modified Seller Name, Modified Start Date, Modified End Date, Modified Active Flag";

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
	public static final String E0017 = "E0017";
	public static final String E0018 = "E0018";
	public static final String E0019 = "E0019";
	public static final String E0020 = "E0020";
	//TISPRO-607
	public static final String E0021 = "E0021";
	public static final String E0022 = "E0022";

	//System/Non Business constants

	//pdp error constants
	public static final String B3000 = "B3000";
	public static final String B3001 = "B3001";
	public static final String B3002 = "B3002";
	public static final String B3003 = "B3003";
	public static final String B3004 = "B3004";
	//pdp error constants

	//My Account Constants
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
	//My Account Constants ends

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
	public static final String B9076 = "B9076";
	public static final String B9078 = "B9078"; //TPR-4461
	public static final String B9079 = "B9079"; //TPR-4461

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
	public static final String B9310 = "B9310";
	public static final String B9500 = "B9500";
	public static final String B9501 = "B9501";
	public static final String B9502 = "B9502";
	public static final String B9503 = "B9503";
	public static final String B9504 = "B9504";
	public static final String B9505 = "B9505";
	public static final String B9506 = "B9506";
	public static final String B9507 = "B9507";
	public static final String B9508 = "B9508";
	public static final String B9509 = "B9509";
	public static final String B9510 = "B9510";
	public static final String B9511 = "B9511";
	public static final String B9512 = "B9512";
	public static final String B9513 = "B9513";
	public static final String B9514 = "B9514";
	public static final String B9515 = "B9515";
	public static final String B9516 = "B9516";
	public static final String B9517 = "B9517";
	public static final String B9518 = "B9518";
	public static final String B9519 = "B9519";
	public static final String B9520 = "B9520";
	public static final String B9521 = "B9521";
	public static final String B9103 = "B9103";

	public static final String B9320 = "B9320";
	public static final String B9321 = "B9321";
	public static final String B9322 = "B9322";
	public static final String B9323 = "B9323";
	public static final String B9324 = "B9324";
	public static final String B9325 = "B9325";
	public static final String B9326 = "B9326";
	public static final String B9327 = "B9327";
	//Added for TPR-1290
	public static final String B9332 = "B9332";
	public static final String B9328 = "B9328";
	public static final String B9700 = "B9700";

	public static final String B9710 = "B9710";
	public static final String B9110 = "B9110";
	//Added in R2.3 start
	//Auto populating pincode details Error Codes Start
	public static final String B9351 = "B9351";
	public static final String B9352 = "B9352";
	public static final String B9353 = "B9353";
	public static final String B9354 = "B9354";
	//Auto popuklating pincode details Error Codes End
	//Added in R2.3 end
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
	public static final String B9213 = "B9213";
	public static final String B9214 = "B9214";
	public static final String B9215 = "B9215";
	public static final String B9216 = "B9216";
	public static final String B9217 = "B9217";
	public static final String B9218 = "B9218";
	public static final String B9219 = "B9219";
	public static final String B9329 = "B9329";
	public static final String B9330 = "B9330";
	public static final String B9331 = "B9331";
	public static final String B9300 = "B9300";
	public static final String B9301 = "B9301";

	public static final String B9161 = "B9161";

	//Added for TPR-4460
	public static final String B9302 = "B9302";
	public static final String B9303 = "B9303";
	public static final String B9304 = "B9304";

	//Added for TPR-1083
	public static final String B9305 = "B9305";
	public static final String B9306 = "B9306";
	public static final String EXCHANGE_REMOVAL_REASON = "Exchange Removed from Cart/Delivery Page due to Pincode Servicability";

	public static final String REVERSE_PINCODE_NOT_SERVICABLE = "Exchange is Not Servicable";
	//TISPRD-5986 MSH category 404 error handling
	public static final String E0023 = "E0023";

	//Search error codes ends

	//Browse Error code
	public static final String B2000 = "B2000";
	public static final String B2001 = "B2001";
	public static final String B2002 = "B2002";

	//For Sales Report
	public static final String DATE_FORMAT_REPORT = "ddMMyyyyHHmmss";
	public static final String ORDER_ERROR = "B8000";
	public static final String CSV_ERROR = "B8001";
	public static final String FILE_WRITER_ERROR = "B8002";
	public static final String ORDER_CURRENCY_ERROR = "B8003";
	public static final String PROMOTION_FEED_ERROR = "B8004";
	public static final String ORDER_PAYMENT_ERROR = "B8005";

	public static final String PROMOTIONS_REPORT_FILE_EXTENSION = "promotions.report.extension";
	public static final String FILE_PATH = "_";
	public static final String SALES_REPORT_INCREMENTAL = "incremental";
	public static final String SALES_REPORT_FULL = "full";
	public static final String SALES_REPORT_QUERY = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE + "} WHERE {"
			+ OrderModel.TYPE + "}=?type order by {" + OrderModel.CODE + "} desc";
	public static final String SALES_REPORT_QUERY_START = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE
			+ "} WHERE " + "{" + OrderModel.CREATIONTIME + "} >=?fromDate AND {" + OrderModel.TYPE + "}=?type order by {"
			+ OrderModel.CODE + "} desc";
	public static final String SALES_REPORT_QUERY_START_END = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE
			+ "} WHERE {" + OrderModel.CREATIONTIME + "} >= ?startDate AND {" + OrderModel.CREATIONTIME + "} <=?endDate AND {"
			+ OrderModel.TYPE + "}=?type order by {" + OrderModel.CODE + "} desc";

	//Bulk Return Initiation
	public static final String START_TIME = "START TIME";
	public static final String END_TIME = "END TIME";

	public static final String LOADSTATUS = "loadstatus";

	public static final String BULK_RETURN_DATA_QUERY_START = "SELECT {" + BulkReturnProcessModel.PK + "} FROM {"
			+ BulkReturnProcessModel._TYPECODE + "} WHERE {" + BulkReturnProcessModel.LOADSTATUS + "}=?loadstatus";

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
	//CAR-301
	public static final String ORDER_QUERY_SUB = "select {o:pk} from {Order As o} WHERE {o.type}=?type  AND {o.code}=?code AND {o.versionid} is null ";

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

	//Order Response

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

	//Local properties file keys
	public static final String PROPERTY_KEY_OMS_AWB_ACTIVE = "oms.awb.active";

	public static final String CANCEL_STATUS = "valid.order.statuses.CANCELLATION";
	public static final String CANCEL_ORDER_STATUS = "valid.order.statuses.CANCELLATION_ORDER";
	public static final String DELIVERED = "DELIVERED";
	public static final String ORDER_COLLECTED = "ORDER_COLLECTED";
	public static final String VALID_APPROVED = "valid.order.statuses.APPROVED";
	public static final String VALID_SHIPPING = "valid.order.statuses.SHIPPING";
	public static final String VALID_PROCESSING = "valid.order.statuses.PROCESSING";
	public static final String VALID_RETURN = "valid.order.statuses.RETURN";
	public static final String VALID_CANCEL = "valid.order.statuses.CANCEL";
	public static final String VALID_DELIVERY = "valid.order.statuses.DELIVERY";
	public static final String COLLECTED = "ORDER_COLLECTED";
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
	//For TPR-5666
	public static final String INVALID_CART_URL = "/cart/error-invalidCart";
	public static final String COULD_NOT_COVERT_CART = "Could not covert cart";

	public static final String PROMOTION_GROUP_DEFAULT = "default";

	public static final String CARTDATA = "No Data Available";
	public static final String PRE_SAVED_DETAIL_MAP = "preSavedDetailMap";
	public static final String UPDATE_CHANNEL_WEB = "web";
	public static final String UPDATE_CHANNEL_MOBILE = "mobile";
	public static final String LINK_MY_ACCOUNT = "/my-account";
	public static final String LINK_UPDATE_PROFILE = "/update-profile";
	public static final String MALFORMED_URL_EXCEPTION = "Malformed URL exception occurred";

	//Update profile email sending parameters

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
	public static final String PAYMENT_TRAN_ERR_LOG = "Exception while saving payment transaction with ".intern();

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
	/* Added in R2.3 START */
	public static final String SDB_FLAG = "RRS";
	public static final String EDTOHD_FLAG = "RRE";
	/* Added in R2.3 END */

	public static final String CASH_ON_DELIVERY = "COD";

	//Added for delivery mode estimated description default time
	public static final String DEFAULT_START_TIME = "1";
	public static final String DEFAULT_END_TIME = "2";
	public static final String DELIVERYMODE_DESC_PREFIX = "Delivered in ";
	public static final String DELIVERYMODE_DESC_SUFFIX = " days";

	public static final String ORDER_HISTORY_DURATION_DAYS = "order.history.duration.days";
	public static final String ORDER_HISTORY_DEFAULT_DURATION_DAYS = "180";
	public static final String FROM_DATE_MUST_NOT_BE_NULL = "From Date must not be null";

	public static final String ORDER_CODE = "ordercode";
	public static final String SUCCESS_VAL = "SUCCESS";
	public static final String PENDING_VAL = "PENDING";

	public static final String CART_DELISTED_SESSION_ID = "cartItemDelisted";
	public static final String IS_RESPONSIVE = "isResponsive";
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
	//Seler Priority Report Query
	//Within date range
	public static final String SELLERPRIORITYWITHINDATEQUERY = "Select {sv.pk} from {SavedValues as sv JOIN MplSellerPriority as msp ON {sv.modifieditem}={msp.pk} and {sv.creationtime} BETWEEN ?startDate and ?endDate JOIN SavedValueEntryType as sve ON {sv.modificationtype}={sve.pk}} order by {sv.Timestamp} desc";
	//public static final String SELLERPRIORITYWITHINDATEQUERY =
	//"Select {s.pk} from {SavedValueEntry as se},{SavedValues as s}, {Bin as bk }, {SavedValueEntryType as st} where {s.modificationtype}={st.pk} and {st.code} = 'changed'and {s.modifieditem}={bk.pk} and {s.pk} = {se.Parent} and {s.creationtime} BETWEEN ?startDate and ?endDate ";

	//full data
	public static final String SELLERPRIORITYQUERY = "Select {sv.pk} from {SavedValues as sv JOIN MplSellerPriority as msp ON {sv.modifieditem}={msp.pk} JOIN SavedValueEntryType as sve ON {sv.modificationtype}={sve.pk}} order by {sv.Timestamp} desc";

	public static final String SELLERPRIORITYDATAQUERY = "Select {sp.pk} from  {MplSellerPriority as sp }";

	public static final String CARTQUERY = "select {c:pk} from {Cart As c} where {c.guid}=?guid".intern();
	public static final String ISBUYAGETPROMO = "isBuyAGetPromo".intern();

	public static final String VOUCHERWITHINDATEQUERY = "select {d.voucher} from {DateRestriction as d} where sysdate>={d.startdate} and sysdate<={d.enddate}";

	public static final String GETPROMOTIONS = "select {p:pk} from {AbstractPromotion as p} where {p.enabled}='1' and sysdate<={p.enddate} and sysdate>={p.startdate} and {immutableKeyHash} is null";
	public static final String PRODUCT_PROMO_PERCENTAGE_FIRE_MSG = "product.promotion.firedMessage.ifPercentage";

	public static final String CARD_TYPE_CREDIT = "CREDIT".intern();
	public static final String CARD_TYPE_DEBIT = "DEBIT".intern();

	public static final String VOUCHERWITHINDATEQUERYFROMCOUPONMODEL = "select {p:pk} from {VoucherStatusNotification as p} where {p.voucherStartDate}<=?sysdate and {p.voucherEndDate}>=?sysdate ";

	//CRM Ticket Type
	public static final String TICKET_TYPE = "D";
	public static final String TICKET_SUB_TYPE = "AC";
	//CRM Ticket Source From Commerce
	public static final String SOURCE = "commerce";
	public static final String DEFAULT_COUNTRY_CODE = "IN";
	public static final int PIN_CODE_LENGTH = 6;

	//Coupon
	public static final String ZEROPOINTZEROONE = "0.01".intern();
	public static final String HUNDRED = "100".intern();

	public static final String CAMPAIGN_DISCOUNT = "DISCOUNT OFFER".intern();
	public static final String CAMPAIGN_FREEBIE = "FREEBIE OFFER".intern();
	public static final String CAMPAIGN_SHIPPING = "SHIPPING DISCOUNT OFFER".intern();
	public static final String CAMPAIGN_BOGO = "BOGOF OFFER".intern();
	public static final String CAMPAIGN_CASHBACK = "CASHBACK OFFER".intern();
	public static final String CAMPAIGN_CHANNEL = "WEB|WEBMOBILE|MOBILE|CALLCENTER|KIOSK".intern();
	public static final String CAMPAIGN_MULTIDATA_SEPERATOR = "|".intern();
	public static final String CAMPAIGN_FILE_LOCATION = "campaign.promotion.csv.path".intern();
	public static final String CAMPAIGN_HEADER = "IDENTIFIER,TITLE,PROMOTIONGROUP,DESCRIPTION,ENABLED,PRIORITY,CHANNEL,PRODUCTS,CATEGORIES,EXCLUDED PRODUCTS,CATEGORY MIN AMOUNT,QUANTITY,MAX DISCOUNT,IS PERCENTAGE,PERCENTAGE,DISCOUNT PRICES,GIFT PRODUCTS,START DATE,END DATE,RESTRICTIONS,FIRED MESSAGE,COULD FIRE MESSAGE,SECOND PRODUCTS,SECOND CATEGORIES,THRESH TOTALS,TSHIP,SSHIP,DISCOUNT TYPE,DELIVERY MODE,FREE COUNT,URL"
			.intern();
	public static final String CAMPAIGN_FILE_DELIMITTER = ",".intern();
	public static final String CAMPAIGN_FILE_NEW_LINE_SEPARATOR = "\n".intern();
	public static final String CAMPAIGN_FILE_NAME = "campaign".intern();
	public static final String CAMPAIGN_WEBSITE = "http://tatacliq.com/store".intern();
	public static final String CAMPAIGN_URL_ALL = "all".intern();

	public static final String CAMPAIGN_URL_OFFER_IDENTIFIER = "/o/".intern();
	public static final String CAMPAIGN_URL_OFFER_ID_URL = "?offer=".intern();
	public static final String CAMPAIGN_FILE_PATH = "${HYBRIS_DATA_DIR}/feed/campaign".intern();

	//For Special Price Updation
	public static final String SPECIALPRICE_PROMOTIONS = "promotions".intern();
	public static final String SPECIALPRICE_QUANTITY = "quantity".intern();
	public static final String SPECIALPRICE_PRIORITY = "priority".intern();
	public static final String HTTP = "http:".intern();
	public static final String HTTPS = "https:".intern();
	public static final String STAGED = "Staged".intern();
	//TISSQAUAT-673 starts
	public static final String ONLINE = "Online".intern();
	//TISSQAUAT-673 ends

	public static final String BANNER_IMAGE = "bannerImage";
	public static final String BANNER_ALTTEXT = "bannerAltText";

	//Audit Report
	public static final String CHANGED = "CHANGED";
	public static final String PRIORITYSTARTDATE = "priorityStartDate";
	public static final String PRIORITYENDDATE = "priorityEndDate";
	public static final String ISACTIVE = "isactive";
	public static final String ACTIVATE = "Activate";
	public static final String DEACTIVATED = "Deactivated";
	public static final String SELLERID = "sellerid";
	public static final String CATEGORYID = "categoryid";
	public static final String LISTINGID = "listingid";
	public static final String SIMPLEDATEFORMAT = "dd-MM-yyyy HH:mm:ss";
	public static final String SIMPLEDATEFORMATDB = "yyyy-MM-dd HH:mm:ss";
	public static final String ERROR_MSG_SELLERPRIORITY_IN_SAVEDVALUES = "No sellerpriority data in saved values";
	public static final String FILEPATHNOTAVAILABLE = "File Path not available";
	public static final String FILENOTFOUNDEXCEPTION = "Cannot find file for batch update.";
	public static final String IOEXCEPTION = "Exception closing file handle. ";

	public static final String PARENT = "parent";

	//Month list
	public static final String JANUARY = "January";
	public static final String FEBRUARY = "February";
	public static final String MARCH = "March";
	public static final String APRIL = "April";
	public static final String MAY = "May";
	public static final String JUNE = "June";
	public static final String JULY = "July";
	public static final String AUGUST = "August";
	public static final String SEPTEMBER = "September";
	public static final String OCTOBER = "October";
	public static final String NOVEMBER = "November";
	public static final String DECEMBER = "December";

	//Month list

	public static final String COUPONREDEEMERROR = "Coupon cannot be redeemed".intern();
	public static final String COUPONTOPCOUNT = "coupon.display.topCount";
	public static final String COUPONTOPCOUNTDEFVAL = "5";

	//Coupon Exception Message
	public static final String EXCPRICEEXCEEDED = "total price exceeded".intern();
	public static final String EXCINVALID = "Voucher not found".intern();
	public static final String EXCEXPIRED = "Voucher cannot be redeemed".intern();
	public static final String EXCISSUE = "Error while".intern();
	public static final String EXCNOTAPPLICABLE = "Voucher is not applicable".intern();
	public static final String EXCNOTRESERVABLE = "Voucher is not reservable".intern();
	public static final String EXCFREEBIE = "freebie".intern();
	public static final String EXCUSERINVALID = "User not valid".intern();

	public static final String USER = "user".intern();
	public static final String DATE = "Date".intern();
	//TPR-1075
	public static final String NEWCUSTOMER = "NewCustomer".intern();
	public static final String NEWUSERRESTVIOLATION = "Voucher for New Customer is violated".intern();
	public static final String VOUCHERINVALIDNEWCUST = "Voucher for New Customer : ".intern();
	public static final String VOUCHERNOTFOUND = "Voucher not found: ".intern();
	public static final String VOUCHERNOTREDEEMABLE = "Voucher cannot be redeemed: ".intern();
	public static final String VOUCHERINVALIDUSER = "User not valid for : ".intern();
	public static final String VOUCHERINAPPLICABLE = "Voucher is not applicable: ".intern();
	public static final String VOUCHERNOTRESERVABLE = "Voucher is not reservable: ".intern();
	public static final String ERRORAPPLYVOUCHER = "Error while applying voucher: ".intern();
	public static final String PRICEEXCEEDED = "Price_exceeded".intern();
	public static final String NOTAPPLICABLE = "not_applicable".intern();
	public static final String VOUCHER = "voucher".intern();
	public static final String FREEBIEERROR = " cannot be redeemed: freebie".intern();
	public static final String PRICEEXCEEDERROR = " cannot be redeemed: total price exceeded".intern();
	public static final String DATERESTVIOLATION = "Date restriction is violated".intern();
	public static final String USERRESTVIOLATION = "User restriction is violated".intern();
	public static final String VOUCHERCODE = "voucherCode".intern();
	public static final String SYSDATE = "sysdate".intern();
	public static final String VOUCHERIDENTIFIER = "voucherIndentifier".intern();
	//TPR-4460
	public static final String CHANNELRESTVIOLATION_WEB = "Voucher Not applicable for Web Channel".intern();
	public static final String CHANNELRESTVIOLATION_MOBILE = "Voucher Not applicable for Mobile Channel".intern();
	public static final String CHANNELRESTVIOLATION_CALLCENTRE = "Voucher Not applicable for CallCentre Channel".intern();
	public static final String CHANNEL_RESTRICTION_MOBILE = "ChannelMobile".intern();
	public static final String CHANNEL_RESTRICTION_WEB = "ChannelWeb".intern();
	public static final String CHANNEL_RESTRICTION = "Channel Restriction for coupons".intern();

	public static final String FIND_USER_BY_UID = "SELECT {u.pk} FROM {User as u} WHERE ({u.UID} = ?uid )";
	//Added for constants for clickandcollect and active.
	public static final String CLICK_N_COLLECT = "Y";
	public static final String ACTIVE = "Y";

	public static final String BANKNAME = "bankName";

	public static final String EBS_DOWNTIME = "payment.ebs.downtime".intern();

	public static final String PROMO_PRODUCT_UPLOAD_SEPARATOR = ",".intern();

	//store url change
	public static final String MISSING_IMAGE_URL = "/_ui/desktop/theme-blue/images/missing-product-300x300.jpg";

	public static final String BIN_DATA_UPLOAD_VERSION = "mpl.payment.bin.uploadversion".intern();

	//TISCR-410
	public static final String ORDERSTAGEQUERY = "select {o.pk} from {OrderStatusCodeMaster as o} where {o.statusCode}=?orderEntryStatus";
	public static final String ORDERENTRYSTATUS = "orderEntryStatus".intern();
	//public static final String CAMPAIGN_HEADER_1 =
	//"IDENTIFIER,TITLE,PROMOTIONGROUP,DESCRIPTION,ENABLED,PRIORITY,".intern();
	//
	//public static final String CAMPAIGN_HEADER_2 =
	//"PRODUCTS,CATEGORIES,EXCLUDED PRODUCTS,CATEGORY MIN AMOUNT,QUANTITY,MAX DISCOUNT,IS PERCENTAGE,PERCENTAGE,DISCOUNT PRICES,GIFT PRODUCTS,START DATE,END DATE,RESTRICTIONS,FIRED MESSAGE,COULD FIRE MESSAGE,SECOND PRODUCTS,SECOND CATEGORIES,THRESH TOTALS,TSHIP,SSHIP,DISCOUNT TYPE,DELIVERY MODE,FREE COUNT,URL"
	//.intern();

	public static final String TYPE = "type";

	public static final String DELIVERY_STARTTIME = "startTime".intern();
	public static final String DELIVERY_ENDTIME = "endTime".intern();

	//OMS FALLBACK Start
	public static final String SINGLE_QUOTE = "\'";
	public static final String FALLBACK_ORDER_HEADER_REPORT = "ORDER,USER,ORDER_DATE,TRANSACTION_IDs,ORDER STATUS".intern();
	public static final String FALLBACK_REPORT_LOCATION = "oms.fallback.report.location".intern();
	public static final String FALLBACK_REPORT_EXTENSION = "oms.fallback.report.extension".intern();
	public static final String FALLBACK_REPORT_NAME = "OrderInQueueReport".intern();
	//OMS FALLBACK End

	public static final String CATEGORY_PATH = "{category-path}".intern();

	public static final String IAFEED_QUERY = "mpl.ia.query.";
	public static final String IAFEED_UPDATEQUERY = "mpl.ia.updatequery.";
	public static final String IA_CATEGORY_PRODUCT = "categoryproduct";
	public static final String IA_BRAND_PRODUCT = "brandproduct";
	public static final String IA_PRICE_INVENTORY = "priceinventory";
	public static final String IA_PRICEINVENTORY_CONTROL = "priceinventorycontrol";
	public static final String DOT = ".";
	public static final String IA_FILE_EXTENSION = "csv";
	public static final String IA_FILENAME_PRODUCTCATEGORY = "ia.filename.productcategory";
	public static final String IA_FILENAME_BRANDPRODUCT = "ia.filename.brandproduct";
	public static final String IA_FILENAME_PRICEINVENTORY = "ia.filename.priceinventory";
	public static final String IA_FILENAME_SELLERPRICEDETAILS = "ia.filename.sellerpricedetails";
	public static final String IA_FILENAME_PRICEINVENTORYCONTROL = "ia.filename.priceinventorycontrol";
	public static final String IA_EXPORT_FOLDER = "ia.path.export";
	public static final String IA_CATEGORYEXPORT_FOLDER = "ia.path.catexport";
	public static final String IA_BRANDEXPORT_FOLDER = "ia.path.brandexport";
	public static final String IA_PRICE_INVENTORYEXPORT_FOLDER = "ia.path.priceinventoryexport";
	public static final String IA_SELLERPRICEDETAILSEXPORT_FOLDER = "ia.path.sellerpricedetails";
	public static final String IA_PRICEINVENTORYCONTROL_FOLDER = "ia.path.priceinventorycontrol";
	public static final String ENCODING = "UTF-8";
	public static final String IA_BATCHVALUE = "mpl.ia.batchvalue";
	public static final String IA_SPDETAILSHEADER = "mpl.ia.spdetailsheader";
	public static final String IA_SPDETAILSQUERYHEADER = "mpl.ia.spdetailsqueryheader";

	public static final Object PRICEINVENTORY_FEED = "priceinventory";

	public static final String CATEGORY_PATH_EMPTY = "/{category-path}".intern();

	//IA Feed For Luxury
	public static final String IAFEED_QUERY_LUXURY = "mpl.ia.luxury.query.";
	public static final String IA_CATEGORYEXPORT_LUXURYFOLDER = "ia.path.luxury.catexport";
	public static final String IA_BRANDEXPORT_LUXURYFOLDER = "ia.path.luxury.brandexport";
	public static final String IA_PRICE_INVENTORYEXPORT_LUXURYFOLDER = "ia.path.luxury.priceinventoryexport";
	//public static final String IA_SELLERPRICEDETAILSEXPORT_LUXURYFOLDER =
	//"ia.path.luxury.sellerpricedetails";
	//public static final String IA_PRICEINVENTORYCONTROL_LUXURYFOLDER =
	//"ia.path.luxury.priceinventorycontrol";

	//TISCR-421
	public static final String EBS_SESSION_ID_KEY = "payment.juspay.sessionId.length";
	public static final String JUSPAY_ENCODING_TYPE = "payment.juspay.encoding.type";
	public static final String EBS_SESSION_ID = "session_id";
	public static final String WISHLIST_BY_USSID = "SELECT {wishentry.pk} FROM {Wishlist2entry as wishentry}, {Wishlist2 as wish}  WHERE {wish.user} = ?user AND {wishentry.wishlist}={wish.pk} AND {wishentry.ussid}= ?ussid ORDER BY {creationtime} desc";

	//TISPRO-497
	public static final String CARTAMOUNTINVALID = "cartAmountInvalid";
	public static final String CART_TOTAL_INVALID_MESSAGE = "Cannot Apply Coupon - Order Amount is less than Coupon Amount!";

	public static final String NBBANKSQUERY = "select {b:pk} from {bankForNetbanking As b},{bank as m} where {b.isAvailable}='1' and {b.name}={m.pk} order by {m.bankname}"
			.intern();

	//TISPT-204
	public static final String SAVEDCARDERROR = "Exception while fetching saved credit/debit cards::::";
	public static final String DC = "DC";
	//TISPT-200
	public static final String GETAUDITID = "select {a.pk} from {mplpaymentaudit as a} where {a.cartGUID}=?cartGUID ORDER BY {a.requestDate} DESC"
			.intern();
	public static final String MOBILE_SOURCE = "&source=App".intern();

	public static final String MOBILE_SOURCE2 = "?source=App";
	public static final String Exchange_Slot = "ExchangeSlot";

	public final static String PROXYENABLED = "proxy.enabled";
	public final static String GENPROXY = "proxy.address";
	public final static String GENPROXYPORT = "proxy.port";

	public final static String IMAGEURLMSG = "Image url is:::";
	public final static String IMAGEDIMENSION = "257Wx257H";
	public final static String IMAGE_MEDIA_TYPE = "Image";

	public final static String DEFAULT_RISK = "-1.0";

	//New BuyBox Implementation

	public final static String BUYBOX = "buybox.";
	public final static String QUERY = "DataExtract";
	public final static String LASTRUNTIME = "lastruntime";
	public final static String HOTFOLDERLOCATION = "hotFolderLocation";
	public static final String BUYBOX_FILE_NAME = "filename";
	public static final String BUYBOX_FILE_EXTENSION = "csv";
	public static final String BUYBOX_FILE_NAME_TEMP = "filename.temp";
	public final static String TICKET_TYPE_CDA = "D";
	public final static String TICKET_SUB_TYPE_CDA = "DAC";
	public final static String TICKET_SUB_TYPE_DMC = "DMC";
	public final static String ADDRESS_TYPE_HOME = "Home";
	public final static String ADDRESS_TYPE_WORK = "Work";
	public final static String DELIVERY_MODE_SD = "SD";
	public final static String DELIVERY_MODE_ED = "ED";
	public final static String PINCODE_RESPONSE_DATA_TO_SESSION = "PincodeResponseDataForCart";
	public final static String INTERFACE_TYPE_CA = "CA";
	public final static String INTERFACE_TYPE_CU = "CU";
	public final static String INTERFACE_TYPE_SD = "SD";

	public static final String RETURNABLE = "Y";
	public static final String RETURN_SCHEDULE = "schedule";
	public static final String RETURN_SELF = "selfShipment";
	public static final String RETURN_RETURN_TO_STORE = "returntostore";
	public static final String FULFILMENT_TYPE_BOTH = "both";
	public static final String RETURN_TYPE_RTS = "RTS";
	public static final String RETURN_TYPE_RSS = "RSS";
	public static final String RETURN_TYPE_RSP = "RSP";
	public static final String TICKETID_PREFIX_E = "E";
	public static final String RETURN_METHOD_QUICKDROP = "quickdrop";
	public static final String RETURN_METHOD_SELFSHIP = "self";
	public static final String RETURN_TYPE = "R";
	public static final String ORDERTAG_TYPE_PREPAID = "PREPAIDRRF";
	public static final String ORDERTAG_TYPE_POSTPAID = "POSTPAIDRRF";

	public static final String CURRENT = "current".intern();
	public static final String INVERTED_COMMA = "'".intern();
	public static final String BRAND_NAME_PREFIX_LOWER = "mbh";
	public static final String SELLER_NAME_PREFIX = "MSH";

	//TISPRO-675
	public static final String EMIBANK = "emi_bank".intern();
	public static final String EMITENURE = "emi_tenure".intern();
	public static final String B9421 = "B9421".intern();
	public static final String RELEVANCE_CATEGORY = ":relevance:category:".intern();
	public static final String RELEVANCE_OFFER = ":relevance:allPromotions:".intern();
	public static final String RELEVANCE_COLOR = ":relevance:colour:".intern();
	public static final String RELEVANCE_SIZE = ":relevance:size:".intern();
	public static final String OFFER = "offer".intern();
	public static final String COLOUR = "colour".intern();
	public static final String SIZE_COLON = ":size:".intern();
	public static final String COLOUR_COLON = ":colour:".intern();
	public static final String OFFER_COLON = ":allPromotions:".intern();

	//CR Changes : TPR-715
	public static final String CART_SELLER_PRODUCTS = "cartSellerValidProducts".intern();
	public static final String VALIDATE_SELLER = "validateSeller".intern();

	public static final String PAYMENTPENDINGORDERQUERY = "select {pk} from {Order as o},{OrderStatus as os} where {o.status}={os.pk} and {os.code}=?status"
			.intern();

	//PAYMENTPENDINGQUERY Query change, 10 minute minus system time not working
	//public static final String PAYMENTPENDINGQUERY =
	//"SELECT {o.pk} FROM {order as o},{OrderStatus as os} WHERE {creationtime} > (to_date(sysdate,'YYYY/MM/DD HH24:MI:SS') - INTERVAL '10' MINUTE) and {o.status}={os.pk} and {os.code}=?status"
	//.intern();

	//SprintPaymentFixes:- New query added //PaymentFix2017:- queryTAT added
	public static final String PAYMENTPENDINGQUERY = "select {o.pk} from {Order as o},{OrderStatus as os} where  {o.creationtime} <= ?queryTAT and {o.status}={os.pk} and {os.code}=?status and {o.type}=?type"
			.intern();

	//public static final String PAYMENTPENDINGQUERY =
	//"select {o.pk} from {Order as o},{OrderStatus as os},{WalletEnum as w} where  {o.creationtime} <= ?queryTAT and {o.status}={os.pk} and {os.code}=?status"
	//+"and {w.code}!='mRupee' ".intern(); //Query to include mRupee

	public static final String PAYMENTPENDINGSTATUS = "status".intern();
	//PaymentFix2017:- queryTAT added
	public static final String PAYMENTPENDINGSKIPTIME = "queryTAT".intern();
	//PaymentFix2017:- order by {jw.creationtime} desc added
	public static final String PAYMENTPENDINGWEBHOOKUERY = "select {jw.pk} from {JuspayWebhook as jw}, {JuspayOrderStatus as js} where {jw.orderstatus}={js.pk} and {js.orderId}=?reqId order by {jw.creationtime} desc"
			.intern();
	public static final String WEBHOOKREQSTATUS = "reqId".intern();
	public static final String OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE = "orderDeallocate";
	public static final String PINCODE = "000000";
	public static final String JUSPAYWEBHOOKRETRYTATQUERY = "select {b.juspayWebhookRetryTAT} from {BaseStore AS b} WHERE {b.uid}=?store";
	public static final String JSON = "json".intern();

	//TPR-629
	//public static final String VOUCHERINVALIDATIONQUERY =
	//"select {v.pk} from {voucherinvalidation as v},{order as o},{customer as c},{voucher as vo} where {v.order}={o.pk} and {o.code}=?code and {v.user}={c.pk} and {c.originaluid}=?customerUid and {v.voucher}={vo.pk} and {vo.code}=?voucherIdentifier";
	//public static final String VOUCHERINVALIDATIONQUERY =
	//"select {v.pk} from {voucherinvalidation as v},{order as o},{customer as c},{voucher as vo} where {v.order}={o.pk} and {o.code}=?code and {v.user}={c.pk} and {c.originaluid}=?customerUid and {v.voucher}={vo.pk} ";
	public static final String CUSTOMERUID = "customerUid";
	public static final String OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING = "paymentPending";

	//luxury
	public static final String IS_LUXURY = "0".intern();
	public static final String IS_MARKETPLACE = "0".intern();

	public static final String LATESTOTPQUERY = "select {o.pk} from {otp as o} where {o.customerid}=?customerPK and {o.otptype}=?OTPType and {o.isvalidated}='0' order by {creationtime} desc fetch first 1 rows only";
	public static final String LATESTOTPMOBILEQUERY = "select {o.pk} from {otp as o} where {o.emailid}=?emailId and {o.mobileNo}=?mobileNo and {o.otptype}=?OTPType and {o.isvalidated}='0' order by {creationtime} desc fetch first 1 rows only";
	public static final String LATESTOTPEMAILQUERY = "select {o.pk} from {otp as o} where {o.emailid}=?emailId and {o.otptype}=?OTPType and {o.isvalidated}='0' order by {creationtime} desc fetch first 1 rows only";
	public static final String LATESTOTPQUERYINV = "select {o.pk} from {otp as o} where {o.emailid}=?emailId and {o.otptype}=?OTPType order by {creationtime} desc fetch first 1 rows only";
	public static final String FAILED = "failed";
	public static final String TO = "TO".intern();
	public static final String AND = " AND ";
	public final static String RESERVATION_DATA_TO_SESSION = "reservationData";

	private MarketplacecommerceservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}

	//track order error codes
	public static final String B9104 = "B9104";
	public static final String B9105 = "B9105";
	public static final String B9106 = "B9106";
	public static final String B9107 = "B9107";
	public static final String B9108 = "B9108";

	//implement here constants used by this extension

	//Sonar Fix
	public static final String ALLVARIANTSSTRING = "allVariantsString";
	public static final String CHANGE_DELIVERY_ADDRESS = "changeDeliveryAddressData";
	public static final String RESCHEDULE_DATA_SESSION_KEY = "rescheduleDataList";
	public static final String SCHEDULE_DELIVRY_DATA = "scheduleDeliveryData";

	public static final String S = "S";
	public static final String SELF_COURIER = "SELF_COURIER";

	//public static final String PRODUCTOFFERDETMSGQUERY =
	//"SELECT {prodOffrDet.sellerId},{offerDet.message},{offerDet.messageDet},{prodOffrDet.startDate},{prodOffrDet.endDate} FROM {OfferDetail as offerDet}, {ProductOfferDetail as  prodOffrDet} "
	//+ "WHERE {prodOffrDet.productId}= ?productId " +
	//"AND {prodOffrDet.offer} = {offerDet.pk} "
	//+
	//"AND {prodOffrDet.startDate} <= ?sysdate AND {prodOffrDet.endDate} >= ?sysdate".intern();

	//Added for displaying Non HMC configurable offer messages , TPR-589
	public static final String PRODUCTOFFERDETMSGQUERY = "SELECT {prodOffrDet.sellerId},{offerDet.message},{offerDet.messageDet},{prodOffrDet.startDate},{prodOffrDet.endDate} FROM {OfferDetail as offerDet}, {ProductOfferDetail as  prodOffrDet} WHERE {prodOffrDet.productId}= ?productId AND {prodOffrDet.offer} = {offerDet.pk} AND {prodOffrDet.startDate} <= ?sysdate AND {prodOffrDet.endDate} >= ?sysdate"
			.intern();

	public static final String OFFERPRODUCTID = "productId".intern();

	public static final String MESSAGE = "message".intern();
	public static final String MESSAGEDET = "messageDet".intern();
	public static final String MESSAGESTARTDATE = "startDate".intern();
	public static final String MESSAGEENDDATE = "endDate".intern();

	public static final String TERMSANDCONDITIONS = "termsAndConditions".intern();//CAR-327 added

	//Added For TPR-1035
	public static final String BIN_ERROR_HEADER = "BIN,CUSTOMER_ID,PAYMENTMODE,DATE,TYPE_OF_ERROR".intern();
	public static final String BIN_ERROR_FILE_LOCATION = "bin.errorreport.csv.path".intern();
	public static final String BIN_ERROR_FILE_PATH = "${HYBRIS_DATA_DIR}/feed/report".intern();
	public static final String BIN_ERROR_FILE_NAME = "binErrorReport".intern();
	//Added for TPR-798
	public static final String MPLCATELOG = "internal.campaign.catelog";
	public static final String MPLCATALOGNNAME = "internal.campaign.catalogVersionName";
	public static final String WCMSPAGINATIONQUERY = "Select {CSP.pk} From {ContentSlotForPage AS CSP JOIN ContentPage as CP ON {CSP.page}={CP.pk}} "
			+ "where {CP.uid} = ?uid and {CSP.catalogVersion}=?version";
	//TPR-978
	public static final String DEFAULT_IMPORT_CONTENT_CATALOG_ID = "mplContentCatalog";
	public static final String DEFAULT_IMPORT_CONTENT_CATALOG_VERSION = "Staged";

	//Added for luxury
	public static final String CHANNEL_APP = "APP";
	public static final String MEGANAVNODE = "luxury.root.navigation.node.id";

	public static final String LUXURY_CARTICON = "luxuryCartIcon";
	public static final String LUXURYCARTPAGE = "luxuryCartPage";

	//TPR-1285
	//public static final String L4CATEGORYQUERY =
	//"SELECT distinct {cat.pk} FROM {Category AS cat},{CatalogVersion AS cv} WHERE NOT EXISTS ({{ SELECT * FROM {CategoryCategoryRelation} WHERE {source}={cat:pk} }} ) and {cat.code} like 'MPH%'"
	//.intern();

	public static final String L4CATEGORYQUERY = "SELECT distinct {cat.pk} FROM {Category AS cat},{CatalogVersion AS cv} WHERE  EXISTS ({{ SELECT * FROM {CategoryProductRelation} WHERE {source}={cat:pk} }} ) and {cat.code} like 'MSH%'"
			.intern();

	public static final String SITEMAP_FILE_LOCATION_BRAND = "mpl.sitemap.brandFileLocation".intern();
	public static final String SITEMAP_FILE_LOCATION_CUSTOM = "mpl.sitemap.customFileLocation".intern();
	public static final String SITEMAP_FILE_LOCATION_PRODUCT = "mpl.sitemap.productFileLocation".intern();
	public static final String SITEMAP_CATEGORY_QUERY = "mpl.sitemap.categoryQuery".intern();
	public static final String DEFAULT_SITEMAP_CATEGORY_QUERY = "SELECT {c.pk} FROM {Category AS c JOIN CatalogVersion AS cv ON {c.catalogVersion}={cv.pk} JOIN Catalog AS cat ON {cv.pk}={cat.activeCatalogVersion} JOIN CMSSite AS site ON {cat.pk}={site.defaultCatalog}}  WHERE {site.pk} = ?site AND ({c.code} like 'MSH%' or {c.code} like 'MBH%' ) AND NOT exists ({{select {cr.pk} from {CategoriesForRestriction as cr} where {cr.target} = {c.pk} }})"
			.intern();
	public static final String SITEMAP_PRODUCT_QUERY_DEFAULT = "select {p:pk} from {Product as p join CategoryProductRelation as prodrel on {p:pk}={prodrel:target} join BuyBox as bb on {bb.product}={p.code}} where {p.catalogversion}= ?catalogVersion and {bb.delisted}='0'and (sysdate between {bb.sellerstartdate} and {bb.sellerenddate}) and {prodrel:source} in( {{select distinct{cat.pk} from {Category as cat} where {cat.pk} in ( {{select {rel:target} from {Category as c Join CategoryCategoryRelation as rel 	ON {c:PK} = {rel:source}} where {c.code}=?l2code and {c.catalogversion}= ?catalogVersion }}) }} ) "
			.intern();
	public static final String SITEMAP_PRODUCT_QUERY = "mpl.sitemap.productQuery".intern();
	public static final String SITEMAP_CONTENT_QUERY = "mpl.sitemap.contentQuery".intern();
	public static final String DEFAULT_SITEMAP_CONTENT_QUERY = "select {cp.pk} from {ContentPage as cp},{CmsApprovalStatus as cas},{catalogversion as cat} where {cp.approvalstatus}={cas.pk} and {cas.code}='approved' and {cp.catalogversion}={cat.pk} and {cat.version}='Online'"
			.intern();
	public static final String SITEMAP_HIERARCHY = "mpl.sitemap.hierarchy".intern();
	public static final String SITEMAP_HIERARCHY_DEFAULT = "MSH1";
	//update the message for Freebie product TPR-1754
	//public static final String PRODUCTFREEBIEDETMSGQUERY =
	//"SELECT {prodOffrDet.ussId},{offerDet.freebieMsg},{prodOffrDet.startDate},{prodOffrDet.endDate}  FROM {FreebieDetail as offerDet}, {ProductFreebieDetail as  prodOffrDet} WHERE {prodOffrDet.ussId}= ?ussId AND {prodOffrDet.offer} = {offerDet.pk} AND {prodOffrDet.startDate} <=sysdate AND {prodOffrDet.endDate} >=sysdate"
	//.intern();
	public static final String PRODUCTFREEBEEDETMSGQUERY = "select {prodOffrDet.pk},{offerDet.pk} from {"
			+ ProductFreebieDetailModel._TYPECODE + " as prodOffrDet JOIN " + FreebieDetailModel._TYPECODE
			+ " as offerDet ON {prodOffrDet.offer} = {offerDet.pk} }"
			+ " where {prodOffrDet.ussId}= ?ussId AND sysdate between {prodOffrDet.startDate} AND {prodOffrDet.endDate}".intern();
	public static final String FREEBIEMSG = "freebieMsg".intern();
	public static final String FREEBIEUSSID = "ussId".intern();

	//Luxury Changes
	public static final String MARKETPLACE = "marketplace";
	public static final String LUXURY = "luxury";
	public static final String MSH = "MSH";
	public static final String LSH = "LSH";

	public static final String LUX_SALESCATEGORYTYPE = "luxury.salescategory.code";

	public static final String TICKETTYPECODE_CANCEL = "C";
	public static final String BULK_CANCEL_SUCCESS_DESC = "Cancellation Success";
	public static final String BULK_CANCEL_FAILURE_DESC = "Cancellation Failure";

	public static final String initiate_cancel_job_cancellation_flag = "initiate.cancel.job.cancellation.flag";
	public static final String FAILURE_LOAD_STATUS = "-1";
	public static final String SUCCESS_LOAD_STATUS = "1";
	public static final String LEFT_PARENTHESIS = "(";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String BLANK_SPACE = " ";
	public static final String LAST_CONSIGNMENT_STATUS = "Last Consignment status -";
	public static final String END_TIME_C = "Initiate Bulk Cancellation Job : End time : ";
	public static final String START_TIME_C = "Initiate Bulk Cancellation Job : Start time : ";
	public static final String TOTAL_TIME_TAKEN = "Total time taken : ";

	public static final String BULK_CANCEL_LOG_STEP_START = "######################################### PROCESS STARTS ##############################################";
	public static final String BULK_CANCEL_LOG_STEP_1 = "Initiate Bulk Cancellation Job : Data list size : ";
	public static final String BULK_CANCEL_LOG_STEP_2_1 = "Initiate Bulk Cancellation Job : Order No : ";
	public static final String BULK_CANCEL_LOG_STEP_2_2 = " and transactionId : ";
	public static final String BULK_CANCEL_LOG_STEP_3 = "Initiate Bulk Cancellation Job : Transaction ID matched : ";
	public static final String BULK_CANCEL_LOG_STEP_4 = "Initiate Bulk Cancellation Job : No Consignment Created : Status :";
	public static final String BULK_CANCEL_LOG_STEP_5 = "Initiate Bulk Cancellation Job : Consignment Status : ";
	public static final String BULK_CANCEL_LOG_STEP_6 = "Initiate Bulk Cancellation Job : isCancellable : ";
	public static final String BULK_CANCEL_LOG_STEP_7 = "Initiate Bulk Cancellation Job : OMS + CRM call skipped : Cancellation Status : ";
	public static final String BULK_CANCEL_LOG_STEP_8_1 = "Initiate Bulk Cancellation Job : subOrderDetails : ";
	public static final String BULK_CANCEL_LOG_STEP_8_2 = "and subOrderEntry : ";
	public static final String BULK_CANCEL_LOG_STEP_9 = "Initiate Bulk Cancellation Job : cancellationStatus : ";
	public static final String BULK_CANCEL_LOG_STEP_10 = "Update load status";
	public static final String BULK_CANCEL_LOG_STEP_11 = "Initiate Bulk Cancellation Job : Order Data Conversion Exception";
	public static final String BULK_CANCEL_LOG_STEP_12 = "Initiate Bulk Cancellation Job : No data found in the iteration of Bulk cancel table";
	public static final String BULK_CANCEL_LOG_STEP_13 = "Initiate Bulk Cancellation Job : Suborder level loop iteration count : ";
	public static final String BULK_CANCEL_LOG_STEP_14 = "Initiate Bulk Cancellation Job : BulKCancellationProcessModel is EMPTY";
	public static final String BULK_CANCEL_LOG_STEP_15 = "######################################### PROCESS ENDS ##############################################";
	/*
	 * public static final String BULK_CANCEL_DATA_QUERY_START = "SELECT {" + BulkCancellationProcessModel.PK +
	 * "} FROM {" + BulkCancellationProcessModel._TYPECODE + "} WHERE {" + BulkCancellationProcessModel.LOADSTATUS +
	 * "}=?loadstatus";
	 */

	public static final String BULK_RETURN_SUCCESS_DESC = "Return Success";
	public static final String BULK_RETURN_FAILURE_DESC = "Return Failure";

	public final static String RETURN_ENABLE = "order.return.enabled".intern();
	public final static String CANCEL_ENABLE = "order.cancel.enabled".intern();
	public static final String PRODUCT_IMAGE = "product";

	public static final String WALLETORDERID = "wallertOrderId";
	public static final String CHECKSUMKEY = "checksumKey";
	public static final String MRUPEE = "MRUPEE";
	public static final String MRUPEE_CODE = "MRupee";
	public static final String TPWALLETAUDITQUERY = "select {a:pk} from {MplPaymentAudit As a} where {a.auditId}=?auditId"
			.intern();
	public final static String MRUPEERETURNMETHOD = "payment.mRupee.returnMethod".intern();
	public static final String THIRDPARTYWALLET_ENTRY_EXPIRED = "0".intern();

	//mrupee
	//mrupee
	public static final String PAYMENTPENDING = "SELECT {o.pk}  FROM {order as o},{OrderStatus as os},{WalletEnum as w} WHERE SYSDATE - 10/1440 >  {creationtime} and {o.status}={os.pk} and  {o.iswallet}={w.pk} and ({os.code}=?status1 or {os.code}=?status2) and {w.code}='mRupee' "
			.intern();
	public static final String STATUS1 = "status1".intern();
	public static final String STATUS2 = "status2".intern();

	public final static String THIRDPARTYWALLET = "ThirdPartyWallet";
	public final static String MRUPEERETURNURL = "payment.mRupee.returnUrl".intern();

	public final static String MRUPEE_NARRATION_VALUE = "payment.mRupee.narration".intern();
	public final static String MRUPEE_MERCHANT_CODE = "payment.mRupee.merchantID".intern();
	public static final String MRUPEE_OPTION = "mRupee";
	public final static String MRUPEEHOSTNAME = "mRupee.hostname.disableSslVerification";
	public static final String TICKETTYPECODE = "R";
	public static final String REFUNDTYPE = "S";
	public static final String REASONCODE = "03"; //Hard coded value -- I'm not
	//happy with the product
	//quality

	//For Promotion Apportioning
	public final static String NONFREE_CONSUMED_ENTRIES = "nonFreeConsumedEntries".intern();
	//PaymentFix2017:-
	public static final String PAYMENTPENDING_SKIPTIME = "marketplace.PaymentPending.skipTime".intern();
	public static final String OTHER = "Other";

	public static final String COUNTRYCODE = "91".intern();
	//Promotion Related
	//public static final String BUYAANDBGETPROMOTIONONSHIPPINGCHARGES =
	//"BuyAandBGetPromotionOnShippingCharges".intern();
	//public static final String BUYAGETPERCENTAGEDISCOUNTONB =
	//"BuyAGetPercentageDiscountOnB".intern();
	public static final String DATEFORMATMMDDYYYY = "MM/dd/yy".intern();

	//FREEBIE FIX
	//public static final String FREEBIEPRICETHRESHOLD =
	//"freebiePriceThreshold";

	public static final String BUYAANDBGETPROMOTIONONSHIPPINGCHARGES = "BuyAandBGetPromotionOnShippingCharges".intern();
	public static final String BUYAGETPERCENTAGEDISCOUNTONB = "BuyAGetPercentageDiscountOnB".intern();
	//FREEBIE FIX
	public static final String FREEBIEPRICETHRESHOLD = "freebiePriceThreshold";

	//OrderIssues:- multiple Payment Response from juspay restriction
	public static final String DUPLICATEJUSPAYRESONSE = "duplicatJuspayResponse";

	//Query for fetching invalidation of a particular order-voucher-user
	public static final String VOUCHERINVALIDATIONQUERY = "select {pk} from {voucherinvalidation} where {order}=?order and {user}=?user and {voucher}=?voucher ";

	public static final String OFD = "OUT FOR DELIVERY";
	public static final String ADDRESS_ISSUE = "Address Issue";
	public static final String MIS_ROUTE = "Misrouted";
	public static final String RTO_INITIATED = "RTO Initiated";

	public static final String REFUND_CATEGORY_S = "S";
	public static final String REFUND_CATEGORY_E = "E";
	public static final String IS_NOT_CHANABLE = "isNotChangable";

	public static final String Between = "Between";
	public final static String TICKET_SUB_TYPE_DNC = "DNC";
	public static final String REFUND_MODE_C = "C";
	public final static String INTERFACE_TYPE = "02";
	public static final String FILE_UPLOAD_PATH = "return.fileupload.path";
	public static final String SHIPMENT_CHARGE_AMOUNT = "return.shipmentcharge";
	public static final String RSS = "RSS";
	public static final String RETURN_TRANSACTON_TYPE_01 = "01";
	public static final String ADDRESS_NOT_CHANGED = "ADDRESS_NOT_CHANGED";

	public static final String PROMO = "promo";
	public static final String QUERYSOURCE = "source";
	public static final String QUERYTARGET = "target";
	public static final String QUERYPRODUCT = "} in (?product) }} ";
	public static final String QUERYUNION = " UNION ";
	public static final String QUERYSELECT = "{{ SELECT {cat2prod:";
	public static final String QUERYAS = " AS cat2prod} ";
	public static final String QUERYWHERECAT = " WHERE {cat2prod:";
	public static final String QUERYPK = "} as pk ";
	public static final String PDISCOUNT = " *** percentage discount:";
	public static final String SPECIALPRICEPROMOTION = "******** Special price check disabling promotion, productlist impacted:";
	public static final String SPECIALPRICEPROMOTIONERROR = "******** Special price check disabling promotion, productlist error:";
	public static final String PROMOTIONCODE = "PROMOTION CODE:";
	public static final String COMPONENTMESSAGE = "Component visiblity set to false";
	public static final String SELLERIDSEARCH = "sellerId:";
	public static final String SELLERIDPARAM = "sellerId";
	public static final String HOMEPAGELOGINFO = "Component visiblity set to false";
	public static final String QUERYWHEREPROMO = " where {b.promoCode}=?promoCode ";
	public static final String QUERYERROR = "error in search query";
	public static final String QUANTITYCOUNTEXCEPTIONLOG = "exception getching the quantity count details aginst product/ussid";
	public static final String EXCEPTIONCAUSELOG = " Exception cause :";
	public static final String CARTNOTFOUNDEXCEPTION = "Cart not found.";
	public static final String PROMOTIONDEBUGLOG = "Fetching Promotion Details";
	public static final String QUERYJOIN = "JOIN ";
	public static final String USERPARAM = "user";
	public static final String NOENTRYSUBORDERLOG = "No  Entries available for Suborder ID:- ";

	public static final String SECONDPRODUCT = "secondProduct";

	//TPR-5346

	public static final String REACHED_MAX_LIMIT_FOR_PRODUCT = "reachedMaxLimitforproduct";

	public static final String PRECOUNTMSG = "Unfortunately, there is a restriction on the number of";
	public static final String MIDCOUNTMSG = "you can order. Since you can purchase only ";
	public static final String LASTCOUNTMSG = "we have modified your shopping bag to reflect this.";
	public static final String FOUNDCOMPONENT = "Found Component>>>>with id :::";

	public static final String AGENT_ID = "agentId";

	public static final String JUSPAY = "JUSPAY".intern();

	//OIS store manager agent group
	public static final String CSCOCKPIT_USER_GROUP_STOREMANAGERAGENTGROUP = "storemanageragentgroup";
	public static final String CSCOCKPIT_USER_GROUP_STOREADMINAGENTGROUP = "storeadminagentgroup";

	//Agent specific order search query
	public static final String ORDER_BY_AGENT = "select {pk} from {Order} where {user}=?user and {agentId}=?agentId ";

	//Agent who placed order from cscockpit

	//CAR-285
	public static final String XML_SITEMAP_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"> \n";
	public static final String SITEMAP_TAG_OPEN = "<sitemap>\n";
	public static final String SITEMAP_TAG_CLOSE = "</sitemap>\n";
	public static final String LOC_TAG_OPEN = "<loc>\n";
	public static final String LOC_TAG_CLOSE = "</loc>\n";
	public static final String XML_SITEMAP_END = "</sitemapindex>\n";
	public static final String SITEMAP_NAME = "mpl.sitemap.xml.name";
	public static final String SITEMAP_NAME_DEFAULT = "sitemap.xml";
	public static final String SITEMAP_LOCATION = "mpl.sitemap.xml.location";
	public static final String SITEMAP_LOCATION_DEFAULT = "/hybris/hybris/data/feed/report/sitemap/";
	public static final String SITEMAP_ZIP_LOCATION = "mpl.sitemap.zipLocation";
	public static final String SITEMAP_ZIP_LOCATION_DEFAULT = "/hybris/hybris/data/feed/report/sitemap/sitemaps";
	public static final String SITEMAP_UID = "mpl.sitemap.uid";
	public static final String SITEMAP_UID_DEFAULT = "mpl";
	public static final String SITEMAP_URL = "mpl.sitemap.url";
	public static final String SITEMAP_URL_DEFAULT = "https://www.tatacliq.com/";
	public static final String SITEMAP_FOLDER = "mpl.sitemap.folder";
	public static final String SITEMAP_FOLDER_DEFAULT = "sitemaps/";
	public static final String ZIP_FORMAT = ".gz";

	//PRDI - 151
	public static final String TYPE_OF_RETURN_FOR_RSS = "return.typeofreturn";

	//Payment Type changes
	public static final String BASESTORE = "baseStore".intern();

	//TPR-5733
	public static final String FOOTER_LINK_QUERY = "select pk from {MplFooterLink} order by {footerLinkRow},{footerLinkColumn} asc";

	//TPR-4512
	public static final String TRANSACTION_NO_KEY = "transaction.count";
	public static final String NULL_VALUE = "NULL".intern();
	public static final String COD_PAYMENT = "COD".intern();
	public static final String POSTPAID = "POSTPAID".intern();
	public static final String PREPAID = "PREPAID".intern();
	public static final String EMPTY_SPACE = " ".intern();
	public static final String REFUND_SUCCESSFUL_ = "REFUND_SUCCESSFUL".intern();
	public static final String MOBILE_NO_NOT_PRESENT = "Mobile number is not present in Commerce System".intern();
	public static final String TRANSACTION_ID_NOT_PRESENT = "TransactionId is not present in Commerce System".intern();
	public static final String ORDER_ID_NOT_PRESENT = "Order Reference Number is not present in Commerce System".intern();
	//SDI-1193
	public static final String MOBILE_QUERY = "SELECT UNIQUE {a:pk} FROM {order as a},{address as b} WHERE {a:user}={b:owner} AND {b.cellphone}=?mobileNo AND {a.type}=?type AND ({a.creationtime} > sysdate -?transactionLimit) order by {a.creationtime} desc fetch first ?queryCount rows only"
			.intern();
	//SDI-1193
	public static final String TRANSACTION_LIMIT_BY_DATE = "transaction.limit";
	public static final String MOBILE_QUERY_FOR_L4CATEGORY = "select distinct {c.pk} from {product as p},{CategoryProductRelation as cp},{Category as c},{catalogversion as cv} where {cp.TARGET} = {p.pk} and {cp.SOURCE} = {c.pk} and {c.code} like 'MPH%' and {p.varianttype} is null and {p.catalogversion}={cv.pk} and {cv.version}='Online' and {p.code} = ?productCode"
			.intern();
	public static final String TRANSACTION_QUERY = "select {b:pk} from {orderentry as a},{order as b} where p_orderlineid=?transactionId and {a:order}={b:pk} and {b:type}=?type"
			.intern();
	public static final String PARENT_ORDER_QUERY = "select {o:pk} from {order as o} where {o:type}=?type and {o:code}=?orderRefNo"
			.intern();


	public static final String ERROR_MSG_TYPE_MISMATCHUSSID = "mismatchUssid";
	public static final String TRANSACTIONID = "transactionid";

	public static final String BULK_CANCEL_DATA_QUERY_START = "SELECT {" + BulkCancellationProcessModel.PK + "} FROM {"
			+ BulkCancellationProcessModel._TYPECODE + "} WHERE {" + BulkCancellationProcessModel.TRANSACTIONID + "}=?transactionid";
	public static final String SUBORDER_DATA_FOR_BULK_CANCELLATION = "select {oe.pk} from {orderentry as oe},{BulkCancellationProcess as bc} where {oe.transactionID}={bc.transactionID} and {bc.loadstatus}='0'";

	public static final String initiate_cancel_job_thread_sleep = "initiate.cancel.job.thread.sleep";

	//added for TPR-1348 AutomatedOrder refund process
	public static final String CLICK_AND_COLLECT = "click-and-collect";

	//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode
	//Restriction doesn't work
	//public static final String BANKNAMEFORNETBANKING =
	//"bankNameforNetbanking";
	//TPR-5667 | Query to Find categoryCode for product
	public static final String CATEGORY_CODE_FOR_PRODUCT = "select {c.code} from {CategoryProductRelation as cpr JOIN Category as c ON {cpr.source}={c.pk}} where {cpr.target} in ({{select {p.pk} from { product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} where {cv.version} = ?catalogVersion AND {p.code} = ?code }}) AND ({c.code} LIKE ?mplCategoryPrefix OR {c.code} LIKE ?luxuryCategoryprefix)";

	//PRDI-423 Start
	public static final String SITEMAP_BRANDFILTER_QUERY = "mpl.sitemap.brandFilterQuery".intern();
	public static final String SITEMAP_BRANDFILTER_QUERY_DEFAULT = "select {mbf:pk} from {mplbrandfilter as mbf} where {mbf.l1}=?l1code and {mbf.l2}=?l2code"
			.intern();
	public static final String SITEMAP_BRANDFILTER = "mpl.sitemap.brandFilter.use".intern();
	public static final String COMMA_CONSTANT = ",".intern();
	public static final String FRONT_SLASH = "/".intern();
	//PRDI-423 End
	//UF-281
	public static final String B1002 = "B1002";

	public static final String HOME = "Home";
	//For sending pancard details to SP through PI and save data into database for new pancard entry
	public static final String PENDING_FOR_VERIFICATION = "PENDING_FOR_VERIFICATION";
	public static final String PAN_REJECTED = "PAN_REJECTED";
	public static final String PANCARDREDIRECTURL = "/pancard/pancarddetailsupload/";
	public static final String PANCARDREDIRECTURLSUFFIX = "?status=";
	//TPR-3782
	public static final String GOLD = "GOLD";
	public static final String SILVER = "SILVER";
	public static final String PLATINUM = "PLATINUM";
	public static final String SOLITAIRE = "SOLITAIRE";
	public static final String TANISHQ = "Tanishq";
	public static final String DIAMOND = "DIAMOND";
	public static final String GEMSTONE = "GEMSTONE";
	public static final String WASTAGETAX = "WASTAGE TAX";
	public static final String MAKINGCHARGE = "MAKING CHARGE";
	public static final String ATTHERATE = "@";
	public static final String GM = "gm";
	public static final String CT = "Ct";
	public static final String KG = "kg";
	public static final String METALWEIGHTFINEJEWELLERY = "metalweightfinejwlry";
	public static final String DIAMONDWEIGHTFINEJEWELLERY1 = "diamondweightfinejwlry1";
	public static final String DIAMONDWEIGHTFINEJEWELLERY2 = "diamondweightfinejwlry2";
	public static final String DIAMONDWEIGHTFINEJEWELLERY3 = "diamondweightfinejwlry3";
	public static final String DIAMONDWEIGHTFINEJEWELLERY4 = "diamondweightfinejwlry4";
	public static final String DIAMONDWEIGHTFINEJEWELLERY5 = "diamondweightfinejwlry5";
	public static final String DIAMONDWEIGHTFINEJEWELLERY6 = "diamondweightfinejwlry6";
	public static final String DIAMONDWEIGHTFINEJEWELLERY7 = "diamondweightfinejwlry7";

	//TPR-4134 web service
	public static final String REV_SEAL_JWLRY = "Do you have Reverse Seal with you?";
	public static final String REV_SEAL_RADIO_YES = "Yes,I have reverse seal";
	public static final String REV_SEAL_RADIO_NO = "No,I do not have reverse seal";

	//jewellery TPR-3765
	public static final String RETURN_FINEJEWELLERY = "Forward Seal Mismatch";
	public static final String INVENTORY_RESV_JWLRY_CART = "Price of the product(s) has changed as per current availability.";
	//Sonar Fix
	public static final String REPLACEDUSSID = "replacedUssid";
	public static final String SELFCOURIER = "selfCourier";
	public static final String SCHEDULE_PICKUP = "schedulePickup";
	public static final String QUICK_DROP = "quickDrop";
	public static final String ISCHECKOUT_PINCODE_SERVICEABLE = "isCheckoutPincodeServiceable";

	//PR-4
	public static final String BULK_SMS_1 = "select {sms: amount}, {sms: utrNumber} ,{sms: arnNumber} ,{a:orderlineid},{add:firstname},{add:phone1} from {orderentry as a join order as b on {a:order}={b:pk} join address as add on {b:deliveryAddress}={add:pk} join RefundTransactionEntry as sms on {a:orderlineid}={sms:transactionId}}  where p_orderlineid in ("
			.intern();
	public static final String BULK_SMS_2 = ") and {b:type}='SubOrder' and {b:VersionID} is null".intern();

	public static final String FINEJEW_SELFCOURIER_ERRORMSG = "Schedule pick up is not available at this pincode. Please <a href='/contact'>*contact us*</a> for returns.";
	public static final String FINEJEW_ORDER_RETURN = "Order return has been initiated";
	//PR-15 (Rainbow)
	public static final String CATEGORYRESTRICTION = "CategoryRestriction";

	public final static String PINCODE_RESPONSE_DATA_PDP = "PincodeResponseDataForPDP";
	public static final String KM = "km";

	//Return Window Increase
	public static final String FetchConsignmentList = "SELECT {c.pk} FROM {Order as o}, {Consignment as c}, {EnumerationValue as en} WHERE {c.order} ={o.pk} and {c.status} = {en.pk} and {en.code}='DELIVERED' and {o.versionid} IS NULL AND {o.type}  ='SubOrder' AND {c.code} IN (?code)";
	public static final String ConsignmentListFailure = "Error while fetching consignment list";

	public static final String RETURNWINDOWBATCH = "mpl.returnWindowIncrease.use";

	public static final String PROCESSED = "PROCESSED";
	public static final String NOTFOUND = "NOT_FOUND";

	public static final String FETCHCRONJOBDEBUGLOG = "Error while fetching cronjob with code :";

	public static final String CUSTOMERMASTER_ROWLIMIT = "customermaster.batchjob.rowlimit";
	public static final String PAYMENTINFO_F_ROWLIMIT = "paymentinfo.batchjob.forward.rowlimit";
	public static final String PAYMENTINFO_R_ROWLIMIT = "paymentinfo.batchjob.reverse.rowlimit";

	public static final String FINISHED = "FINISHED";

}
