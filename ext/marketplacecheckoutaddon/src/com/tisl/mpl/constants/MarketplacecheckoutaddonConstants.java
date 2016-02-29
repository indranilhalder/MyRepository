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
	public static final String EXTENSIONNAME = "marketplacecheckoutaddon";
	public static final String MPLPAYMENTURL = "/checkout/multi/payment-method";
	public final static String PAYMENT_METHOD = "payment-method";
	public final static String MPLSTORE = "mpl";
	public final static String ADDVALUE = "/add";
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
	public final static String CHECKOUTRESPONSEURL = "/checkout/multi/sop/response";
	public final static String CHECKOUTCALLBACKURL = "/integration/merchant_callback";
	public final static String SOPPAGEDATA = "silentOrderPageData";
	public final static String PAYMENTFORMMPLURL = "paymentFormMplUrl";
	public final static String PAYMENTVIEWURL = "/store/mpl/en/checkout/multi/payment-method/view";
	public final static String LOGERROR = "Failed to build beginCreateSubscription request";
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
	public static final String PROCESSCONVCHARGESURL = "/validateOTP";
	public static final String GENERATEOTPURL = "/generateOTP";
	public static final String VALIDATEOTPURL = "/validateOTP/{otpNUMField:.*}";
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
	public static final String CARDPAYMENT = "/cardPayment";
	public static final String CHARGED = "CHARGED";
	public static final String REDIRECT = "redirect:";
	public static final String CREATEDEBITCARDORDER = "/createDebitCardOrder";
	public static final String MERCHANTID = "payment.juspay.merchantID";
	public final static String PAYMENTCOD = "COD";
	public final static String PAYMENTMODE = "paymentMode";
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

	// Request Mapping URL

	public static final String MPLDELIVERYMETHODURL = "/checkout/multi/delivery-method";
	public static final String MPLDELIVERYCHOOSEURL = "/choose";
	public static final String MPLDELIVERYSELECTURL = "/select";
	public static final String MPLDELIVERYCHECKURL = "/check";
	public static final String MPLDELIVERYCNCINVRESV = "/invReservation";
	public static final String MPLDELIVERYNEWADDRESSURL = "/new-address";
	public static final String MPLDELIVERYSELECTADDRESSURL = "/select-address";
	public static final String ADDRESS_CODE_PATH_VARIABLE_PATTERN = "{addressCode:.*}";
	public static final String CALCULATEDELIVERYCOST = "/calculateDeliveryCost/{deliveryCost:.*}";
	public static final String DELIVERYCOST = "deliveryCost";
	public static final String MPLSELECTSAVEDADDRESS = "/selectaddress";
	public static final String CHECKPINCODESERVICEABILITY = "/checkPincodeServiceability/{pincode:.*}";
	public static final String PINCODE = "pincode";
	public static final String CHECKEXPRESSCHECKOUTPINOCDESERVICEABILITY = "/checkExCheckoutPincodeServiceability/{selectedAddressId:.*}";
	public static final String SELECTEDADDRESSID = "selectedAddressId";

	public static final String PROMOTIONEXPIRED = "Applied promotion(s) has expired";
	public static final String INVENTORYNOTAVAILABLE = "Stock is not available for the pincode";
	public static final String PAYNOWINVENTORYNOTPRESENT = "inventoryNotPresent";
	public static final String PAYNOWPROMOTIONEXPIRED = "promotionexpired";

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

	private MarketplacecheckoutaddonConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	/* Gigya Social Login */
	public final static String SOCIALLOGIN = "/socialLogin";
}
