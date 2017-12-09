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
package com.tisl.mpl.constants;

/**
 * Global class for all Marketplacecheckoutaddon constants. You can add global constants for your extension into this
 * class.
 */
public final class MarketplacecheckoutaddonConstants extends GeneratedMarketplacecheckoutaddonConstants
{
	//TPR-4461 starts here
	//public static final String VOUCHERPAYMENTMODERESTRICTIONMESSAGE = "Sorry,Your Voucher is not applicable for the current payment mode you selected";
	public static final String BINBANKCHECK = "/binBankCheck/{bin:.*}";
	public static final String REDIRECTTOCOUPON = "redirect_with_coupon";
	//TPR-4461 ends here

	public static final String EXTENSIONNAME = "marketplacecheckoutaddon";
	public static final String MPLPAYMENTURL = "/checkout/multi/payment-method";
	public final static String PAYMENT_METHOD = "payment-method";
	public final static String MPLSTORE = "mpl";
	public final static String ADDVALUE = "/add";
	public final static String PAYVALUE = "/pay";
	public final static String VALUE = "?value=";
	public final static String ISCART = "isCart";
	public final static String CHOOSEVALUE = "/choose";
	public final static String BACKVALUE = "/back";
	public final static String NEXTVALUE = "/next";
	public final static String REMOVEVALUE = "/remove";
	public final static String PAYMENTMODES = "paymentModes";
	public final static String SOPPAYMENTDETAILSFORM = "sopPaymentDetailsForm";
	public final static String GETTERMS = "/getTerms";
	/* Order Status Notification */
	public final static String MARKREAD = "/markAsRead";
	public final static String VIEWVALUE = "/view";
	public final static String CARTDATA = "cartData";
	public final static String ORDERDATA = "orderData";
	public final static String CHECKOUTRESPONSEURL = "/checkout/multi/sop/response";
	public final static String CHECKOUTCALLBACKURL = "/integration/merchant_callback";
	public final static String SOPPAGEDATA = "silentOrderPageData";
	public final static String PAYMENTFORMMPLURL = "paymentFormMplUrl";
	public final static String PAYMENTVIEWURL = "/checkout/multi/payment-method/view";
	public final static String LOGERROR = "Failed to load Payment Page";
	public final static String ERRORMSG = "checkout.multi.paymentMethod.addPaymentDetails.generalError";
	public final static String PAYMENTTRANERRORMSG = "checkout.multi.paymentMethod.addPaymentDetails.PaymentTransactionError";
	public final static String TRANERRORMSG = "checkout.multi.paymentMethod.addPaymentDetails.transactionError";
	public final static String DECLINEDERRORMSG = "checkout.multi.paymentMethod.addPaymentDetails.declinedError";
	public final static String TRANERRORMSGID = "transactionError".intern();
	public final static String VBVERRORMSG = "checkout.multi.paymentMethod.addPaymentDetails.vbvError";
	public final static String LOGWARN = "Failed to set up silent order post page ";
	public final static String GLOBALERROR = "checkout.multi.sop.globalError";
	public final static String SOPFORM = "silentOrderPostForm";
	public final static String POPULARBANKS = "popularBankNames";
	public final static String OTHERBANKS = "otherBankNames";
	public final static String EMIBANKS = "emiBankNames";
	public final static String PAYMENTFORM = "paymentForm";
	public final static String CARTVALUE = "cartValue";
	public final static String LOWERLIMIT = "lowerLimit";
	public final static String UPPERLIMIT = "upperLimit";
	public final static String CUSTBLACKLIST = "mplCustomerIsBlackListed";
	public final static String MOBBLACKLIST = "mplMobileIsBlackListed";
	public final static String PAYMENTNB = "Netbanking";
	public final static String PAYMENTEMI = "EMI";
	public final static String PAYMENTID = "paymentId";
	public final static String SELECTEDEMIBANK = "Selected EMI Bank";
	public final static String COMMACHECK = ",";
	public final static String SUCCESS = "success";
	public final static String FAIL = "fail";
	public static final String MPLSUMMARYURL = "/checkout/multi/summary";
	public static final String ALLITEMSCART = "allItems";
	public static final String DELIVERYADDRESS = "deliveryAddress";
	public static final String DELIVERYMODE = "deliveryMode";
	public static final String PAYMENTINFO = "paymentInfo";
	public static final String REQUESTSECURITYCODE = "requestSecurityCode";
	public static final String PROCESSCONVCHARGESURL = "/setConvCharge";
	public static final String GENERATEOTPURL = "/generateOTP";
	public static final String VALIDATEOTPURL = "/validateOTP/{otpNUMField:.*}";
	public static final String CONFIRMCODORDER = "/confirmCodOrder";
	public static final String OTPNUMFIELD = "otpNUMField";
	public static final String RESETCONVCHARGEURL = "/resetConvCharge";
	public static final String SETCELLNOURL = "/setCellNo";
	public static final String SUMMARYBREADCRUMB = "checkout.multi.summary.breadcrumb";
	public static final String PAYMENTBREADCRUMB = "checkout.multi.paymentMethod.breadcrumb";
	public static final String METAROBOTS = "metaRobots";
	public static final String NOINDEX_NOFOLLOW = "noindex,nofollow";
	public static final String PLACEORDERURL = "/placeOrder";
	public static final String PLACEORDERFORM = "placeOrderForm";
	public static final String CART = "/cart";
	public static final String PLACEORDERFAILED = "checkout.placeOrder.failed";
	public static final String DELIVERYADDRESSNOTSELECTED = "checkout.deliveryAddress.notSelected";
	public static final String DELIVERYMETHODNOTSELECTED = "checkout.deliveryMethod.notSelected";
	public static final String NOSECURITYCODE = "checkout.paymentMethod.noSecurityCode";
	public static final String TERMSNOTACCEPTED = "checkout.error.terms.not.accepted";
	public static final String CARTERRORMESSAGE = "Cart %s does not have any tax values, which means the tax cacluation was not properly done, placement of order can't continue";
	public static final String TAXMISSING = "checkout.error.tax.missing";
	public static final String CARTCALCULATEDERRORMESSAGE = "Cart %s has a calculated flag of FALSE, placement of order can't continue";
	public static final String CARTNOTCALCULATED = "checkout.error.cart.notcalculated";
	public static final String PAYMENTINFOID = "paymentInfoId";
	public static final String REMOVEMESSAGE = "text.account.profile.paymentCart.removed";
	public static final String SELECTEDPAYMENTMETHODID = "selectedPaymentMethodId";
	public static final String HASNOPAYMENTINFO = "hasNoPaymentInfo";
	public static final String ORDERPLACEFAIL = "Failed to place Order";
	public static final String CARDPAYMENT = "/cardPayment/{guid:.*}";
	public static final String CHARGED = "CHARGED";
	public static final String REDIRECT = "redirect:";
	public static final String CREATEDEBITCARDORDER = "/createDebitCardOrder";
	public static final String MERCHANTID = "payment.juspay.merchantID";
	public final static String PAYMENTCOD = "COD";
	public final static String PAYMENTMODE = "paymentMode".intern();
	public final static String PAYMENTMODEFORPROMOTION = "paymentModeForPromotion";
	public final static String CARDPAYMENTERRORMSG = "Card Payment Cancelled. Please select a Payment Method and proceed again";
	public final static String SETSHIPPINGADDRESS = "/setShippingAddress";
	public final static String NULLVALUE = "empty";
	public final static String DEBITCARDS = "debitCards";
	public final static String CREDITCARDS = "creditCards";
	public final static String CODELIGIBLE = "codEligible";
	public final static String FULFILLED = "tFulfilled";
	public final static double WALLETAMOUNT = 0.00D;
	public final static String WALLET = "Wallet";
	public static final String APPLYPROMOTIONS = "/applyPromotions";
	public static final String PROMOGROUP = "mplPromoGrp";
	public static final String JUSPAYORDERID = "juspayOrderId";
	public static final String AUTHORIZATION_FAILED = "AUTHORIZATION_FAILED";
	public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
	public static final String JUSPAY_DECLINED = "JUSPAY_DECLINED";
	public static final String PENDING_VBV = "PENDING_VBV";
	public static final String NEW = "NEW";
	public static final String ORDERID = "orderID";
	public static final String MERCHANT_ID = "merchantID";
	public static final String BINCHECK = "/binCheck/{bin:.*}";
	public static final String BINNO = "bin";
	public static final String STRINGSEPARATOR = "|";
	public static final String TRUEVALUE = "true";
	public static final String BANKFROMBIN = "bank";
	public static final String EMPTY = "".intern();
	public static final String POTENTIAL_PROMO_MESSAGE = "PotentialPromotion".intern();
	public static final String CASHBACK_FIREDPROMO_MESSAGE = "CashBackFired".intern();
	public static final String PRODUCT_PROMO = "ProductPromotion".intern();
	public static final String CART_PROMO = "OrderPromotion".intern();
	public static final String MOBILEBLACKLIST = "/mobileBlacklist";
	public static final String RESETCONVCHARGEELSEWHEREURL = "/resetConvChargeElsewhere";
	public static final String SUBMITNBFORM = "/submitNBForm";
	public static final String SUBMIT_PAYTM_FORM = "/submitPaytmForm"; //paytm changes

	public static final String CREATEJUSPAYORDER = "/createJuspayOrder";
	public static final String SETBANK = "/setBankForSavedCard";
	public final static String COUNTRY = "country".intern();
	public static final String PAYMENTMETHODTYPE = "NB".intern();
	public final static String JSON = "json".intern();
	public final static String JUSPAYRETURNMETHOD = "payment.juspay.returnMethod".intern();
	public final static String GETEMIBANKS = "/getEMIBanks";
	public final static String LISTEMICARDS = "/listEMICards";
	public final static String EBSDOWNCHECK = "ebsDownCheck".intern();
	public final static String JUSPAYJSNAME = "juspayJsName".intern();
	public final static String JUSPAYJSNAMEVALUE = "payment.juspay.juspayJsFile".intern();
	public final static String TNCLINK = "tncLink".intern();
	public final static String TNCLINKVALUE = "payment.termsNConditions.url".intern();
	public final static String PROMOAVAILABLE = "promoAvailable".intern();
	public final static String BANKAVAILABLE = "bankAvailable".intern();

	//R2.3 FL04

	public static final String LANDMARKS = "/landmarks";

	// Model Attribute

	public static final String FULFILLMENTDATA = "fullfillmentData";
	public static final String SHOWDELIVERYMETHOD = "showDeliveryMethod";
	public static final String SHOWADDRESS = "showAddress";
	public static final String SHOWEDITADDRESS = "showEditAddress";
	public static final String SHOWADDADDRESS = "addAddress";
	public static final String DELIVERYADDRESSES = "deliveryAddresses";
	public static final String NOADDRESS = "noAddress";
	public static final String ADDRESSFORM = "addressForm";
	public static final String SHOWSAVEDTOADDRESSBOOK = "showSaveToAddressBook";
	public static final String ADDRESSTYPE = "addressType";
	public static final String COUNTRYISO = "IN";
	public static final String TIMEOUT = "timeout";
	//TPR-429
	public static final String CHECKOUT_SELLER_IDS = "checkoutSellerIDs";
	//PRDI-36/INC144315559
	public static final String DELIVERYADDRESSID = "deliveryAddressID";

	// Request Mapping URL

	public static final String MPLDELIVERYMETHODURL = "/checkout/multi/delivery-method";
	public static final String MPLSINGLEPAGEURL = "/checkout/single";//UF-281
	public static final String MPLDELIVERYCHOOSEURL = "/choose";
	public static final String MPLDELIVERYSELECTURL = "/select";
	public static final String MPLDELIVERYSELECTURLRESPONSIVE = "/select-responsive";
	public static final String MPLDELIVERYCHECKURL = "/check";
	public static final String GETCNCSTRORES = "/cncStores";
	public static final String GETREVIEWORDER = "/reviewOrder";//UF-281;
	public static final String MPLDELIVERYCNCINVRESV = "/invReservation";
	public static final String MPLDELIVERYNEWADDRESSURL = "/new-address";
	public static final String MPLRESPONSIVEDELIVERYNEWADDRESSURL = "/new-address-responsive";//UF-282
	public static final String SLOTDELIVERYRESPONSIVE = "/slotDelivery-responsive";//UF-282
	public static final String MPLSHOWMESSAGE = "/message";
	public static final String MPLDELIVERYSELECTADDRESSURL = "/select-address";
	public static final String MPLRESPONSIVESELECTADDRESSURL = "/select-address-responsive";//UF-282
	public static final String CHECKLOCATIONRESTRICTEDPINCODE = "/delModesOnAddrSelect/{pincode:.*}";
	public static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";
	public static final String CALCULATEDELIVERYCOST = "/calculateDeliveryCost/{deliveryCost:.*}";
	public static final String DELIVERYCOST = "deliveryCost";
	public static final String MPLSELECTSAVEDADDRESS = "/selectaddress";
	public static final String CHECKPINCODESERVICEABILITY = "/checkPincodeServiceability/{pincode:.*}";
	public static final String CHECKPINCODE = "/checkPincodeServiceability/{pincode:.*}";
	public static final String PINCODE = "pincode";
	public static final String CHECKEXPRESSCHECKOUTPINOCDESERVICEABILITY = "/checkExCheckoutPincodeServiceability/{selectedAddressId:.*}";
	public static final String SELECTEDADDRESSID = "selectedAddressId";

	public static final String PROMOTIONEXPIRED = "Applied promotion(s) has expired";
	public static final String INVENTORYNOTAVAILABLE = "Stock is not available for the pincode";
	public static final String PAYNOWINVENTORYNOTPRESENT = "inventoryNotPresent";
	public static final String PAYNOWPROMOTIONEXPIRED = "promotionexpired";
	public static final String PAYMENTMODEINFO = "paymentModeInfo";//TPR-7486

	public static final String ITEMLEVELDISCOUNT = "itemLevelDiscount";

	public static final String NEWADDRESSLINK = "/checkout/multi/delivery-method/new-address";


	public static final String CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB = "checkout.multi.deliveryMethod.breadcrumb";
	public final static String DELIVERY_METHOD = "delivery-method";
	public final static String UPDATE_FLAG = "U";
	public final static String Y = "Y";
	public final static String N = "N";
	public final static String CHECKSERVICABLE = "checkIsServicable";
	public final static String ADDRESSFLAG = "addressFlag";

	public static final String CHANNEL_WEB = "WEB";

	public static final String UPDATE_CHECK_PINCODE = "/updatePincodeCheck";
	public static final String PAYMENTRELATEDOFFERS = "/paymentRelatedOffers";
	public static final String PAYMENTRELATEDOFFERSTERMS = "/paymentRelatedOffersTerms";

	public static final String ADDPOSORDERENTRY = "/addPosToOrderEntry";
	public static final String ADDPICKUPPERSONDETAILS = "/addPickupPersonDetails";

	//Payment Messages
	public static final String CVV_HELP = "payment.cards.CVVHelpContent";
	public static final String CVV_HELP_VAR = "cvvHelp";

	//Error Codes for Payment
	public static final String B6004 = "B6004";
	public static final String B6005 = "B6005";
	public static final String B6006 = "B6006";
	public static final String B6007 = "B6007";
	public static final String B6008 = "B6008";
	public static final String EBSDOWNTIME = "payment.ebs.downtime";
	public static final String NA = "NA";

	public static final String JUSPAYREDIRECT = "redirect";
	public static final String JUSPAYREDIRECTKEY = "payment.juspay.redirect";
	public static final String CELLNO = "cellNo";

	//Coupon
	public static final String NOOFYEARS = "payment.exp.no.of.years";
	public static final String EXPYEARS = "noOfYearsFromCurrentYear";

	public static final String MPLCOUPONURL = "/checkout/multi/coupon";
	//delivery mode
	public static final String CLICK_N_COLLECT = "click-and-collect";

	public static final String PAYNOWCOUPONINVALID = "couponinvalid";
	public static final String COUPONINVALID = "Applied coupon is not valid";
	public static final String CHANGEDELIVERYMODE = "/changeDeliveryMode";
	public static final String CONFIGURABLE_RADIUS = "configurable-radius";

	public static final String NETWORK_ERROR = "/networkError";

	public static final String SAVE_STORE_TOPORUDCT_SUCCESS_MSG = "yes";
	public static final String SAVE_STORE_TOPORUDCT_FAIL_MSG = "no";

	public final static String NEWPAYMENTFORMMPLURL = "newPaymentFormMplUrl";
	public final static String NEWPAYMENTVIEWURL = "/checkout/multi/payment-method/view";

	//TISCR-421
	public static final String EBS_ACCOUNT_ID = "account_id";
	public static final String EBS_ACCOUNT_ID_KEY = "payment.ebs.accountId";
	public static final String EBS_SESSION_ID = "session_id";
	public static final String EBS_SESSION_ID_KEY = "payment.juspay.sessionId.length";
	public static final String JUSPAY_ENCODING_TYPE = "payment.juspay.encoding.type";

	//TISPRO-497
	public static final String CARTAMOUNTINVALID = "cartAmountInvalid";

	//TISPRO-540
	public static final String CREDITCARDMODE = "Credit Card";
	public static final String DEBITCARDMODE = "Debit Card";
	public static final String NETBANKINGMODE = "Netbanking";
	public static final String EMIMODE = "EMI";

	//TPR-7486
	public static final String NEWCARD = "newCard";
	public static final String SAVEDCARD = "savedCard";

	public static final String SETUPMPLNETBANKINGFORM = "/setupMplNetbankingForm"; //TISPT-235
	public static final String SETUPMPLCODFORM = "/setupMplCODForm"; //TISPT-235

	//TISPRO-578
	public static final String CART_DELIVERYMODE_ADDRESS_INVALID = "cartDelModeAddrInvalid";
	public static final String CART_DELIVERYMODE_ADDRESS_INVALID_MSG = "Either delivery mode or delivery address is not present";
	public static final String CART_EXPRESS_CHECKOUT_SELECTED = "isExpressCheckoutSelected";
	public static final String YES = "yes";
	public static final String NO = "no";
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	//TPR-1215
	public static final String LINK_SET_DEFAUT_ADDRESS = "/set-default-address/";

	//TPR-629
	public static final String GUID = "guid";
	public static final String REDIRECTSTRING = "redirect";
	public static final String REDIRECTTOPAYMENT = "redirect_to_payment";
	//TPR-3780:fine jewellery
	public static final String INVENTORYRESERVED = "reload_for_inventory";
	public final static String PAYMENTIMEOUTRRORMSG = "checkout.multi.paymentMethod.addPaymentDetails.PaymentTimeoutError";

	//TPR-3780
	public final static String UPDATED = "updated";



	private MarketplacecheckoutaddonConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	/* Gigya Social Login */
	public final static String SOCIALLOGIN = "/socialLogin";
	public static final String CHECKSESSIONACTIVE = "/checkSessionActive";

	/* mRupee Changes */
	public final static String MRUPEERETURNMETHOD = "payment.mRupee.returnMethod".intern();

	public final static String MRUPEEURL = "payment.mRupee.baseUrl".intern();

	public final static String MRUPEE_MERCHANT_CODE = "payment.mRupee.merchantID".intern();

	public final static String THIRDPARTYWALLET = "ThirdPartyWallet";

	public final static String MRUPEE_NARRATION = "narration";

	public static final String MRUPEE_CODE = "mCode";

	public static final String MRUPEE_MERCHANT_URL = "mRupeeUrl";

	public final static String MRUPEE_NARRATION_VALUE = "payment.mRupee.narration".intern();
	public static final String RETURNTOPAYMENTPAGE = "/checkout/multi/payment-method/pay";
	public static final String REFNUMBER = "refNumber".intern();
	public static final String DELIVERY_SLOTCOST_FOR_ED = "deliverySlotCostForEd";

	public static final String UPDATE_DELIVERY_SLOTCOST_FOR_ED = "updateDeliverySlotCostForEd";
	public static final String MPLDELIVERYSLOTSURL = "/deliverySlotsUrl";

	public static final String DELIVERY_SLOTS_TO_SESSION = "deliverySlotstoSession";
	public static final String VALIDATION_FAILURE_MESSAGE = "Sorry, your address validation failed";

	//INC144315475
	public static final String CARTTOORDERCONVERT = "cartToOrderConvert";

	//TPR-1083
	public static final String REMOVEEXCHANGEFROMCART = "/removeExchangeFromCart";
	//INC144316212
	public static final String IS_DELIVERY_OPTION_PAGE = "isDeliveryOptionPage";

	public static final String CODELIGIBLE_SESSION = "codEligibleSession";
	public static final String JUSPAYBASEURL = "juspayBaseUrl";//TPR-7448


	//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode Restriction doesn't work
	//public static final String BANKNAMEFORNETBANKING = "bankNameforNetbanking";
}
