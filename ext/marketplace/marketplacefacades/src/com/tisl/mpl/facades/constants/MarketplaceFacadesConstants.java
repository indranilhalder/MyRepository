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
package com.tisl.mpl.facades.constants;



/**
 * Global class for all MarketplaceFacades constants.
 */

@SuppressWarnings("PMD")
public class MarketplaceFacadesConstants extends GeneratedMarketplaceFacadesConstants
{
	public static final String EXTENSIONNAME = "marketplacefacades";
	public static final String STYLE_SWATCH = "styleSwatch";
	public static final String INR = "INR";
	public static final String PAYMENT_METHOD_CREDIT_CARD="Credit Card";
	public static final String PAYMENT_METHOD_DEBIT_CARD="Debit Card";
	public static final String PAYMENT_METHOS_COD = "COD";
	// EGV Changes Start 
	
	public static final String PAYMENT_METHOD_CLIQ_CASH = "Cliq Cash".intern();
	public static final String PAYMENT_METHOD_PAYTM = "PAYTM".intern();
	public static final String PAYMENT_METHOD_MRUPEE = "MRUPEE".intern();
	public static final String PAYMENT_METHOD_NET_BANKING = "Net Banking";
	public static final String PAYMENT_METHOD_NET_BANK ="Netbanking";
   // EGV Changes End 
	public static final String REFUND_FAILED = "Refund Failed";
	public static final String FAILURE = "FAILURE";

	public static final String TSHIPTHRESHOLDVALUE = "tship.item.threshold.value";
	public static final String TSHIPCODE = "TSHIP";
	// INC-144316545 START
	public static final String SSHIPCODE = "SSHIP";
	// INC-144316545 END
	public static final String HOMEDELIVERYCODE = "home-delivery";

	public static final String EXPRESS = "express-delivery";
	public static final String C_C = "click-and-collect";
	public static final String HD = "home-delivery";
	public static final String N = "N";
	public static final String Y = "Y";
	public static final String HOME_DELIVERY = "home-delivery";
	public static final String EXPRESS_DELIVERY = "express-delivery";
	public static final String SPACE = " ";

	//RatingReview
	public static final String ANONYMOUS_CUSTOMER = "anonymous".intern();
	public static final String UTF = "UTF-8";
	public static final String CUSTOMER_ERROR = "Rating Review Service cannot proceed as Customer UID is annonymous";

	//Consignment Status
	public static final String ALLOCATED = "ALLOCATED";
	public static final String PICK_IN_PROGRESS = "PICK_IN_PROGRESS";
	public static final String SELLER_ACCEPTED = "SELLER_ACCEPTED";
	public static final String SELLER_DECLINED = "SELLER_DECLINED";
	public static final String PICK_HOLD = "PICK_HOLD";
	public static final String PACKED = "PACKED";
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
	public static final String REACHED_NEAREST_HUB = "REACHED_NEAREST_HUB";
	public static final String OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
	public static final String LOST_IN_TRANSIT = "LOST_IN_TRANSIT";
	public static final String UNDELIVERED = "UNDELIVERED";

	//Customer Facing Status
	public static final String ORDER_PLACED = "Order Placed";
	public static final String PENDING_VERIFICATION = "Pending Verification";
	public static final String UNABLE_TO_PROCESS = "Unable to Process Order";
	public static final String ORDER_CONFIRMED = "Order Confirmed";
	public static final String SHIPPING_COMMENCED = "Shipping commenced";
	public static final String IN_TRANSIT = "In Transit";
	public static final String SHIPMENT_DELIVERED = "Shipment Delivered";
	public static final String RETURN_INITIATED = "Return Initiated";
	public static final String RETURN_IN_PROGRESS = "Return in Progress";
	public static final String REDESPATCH_INITIATED = "Re-dispatch Initiated";
	public static final String ORDER_RET_CLOSED = "Order Returned and Closed";
	public static final String REFUND_INITIATED = "Refund initiated";
	public static final String REFUND_COMPLETED = "Refund completed";
	public static final String MANUAL_REFUND_INITIATED = "Manual refund initiated";
	public static final String RETURN_REJECTED = "Return Rejected";
	public static final String ORDER_CANCELLED = "Order Cancelled";
	public static final String ORDER_CANCELLED_CLOSED = "Order Cancelled and Closed";
	//Customer Facing Status Stages
	public static final String APPROVED = "APPROVED";
	public static final String SHIPPING = "SHIPPING";
	public static final String PROCESSING = "PROCESSING";
	public static final String RETURN = "RETURN";
	public static final String CANCEL = "CANCEL";
	public static final String DELIVERY = "DELIVERY";
	public static final String PAYMENT = "PAYMENT";

	public static final String VALID_APPROVED = "valid.order.statuses.APPROVED";
	public static final String VALID_SHIPPING = "valid.order.statuses.SHIPPING";
	public static final String VALID_PROCESSING = "valid.order.statuses.PROCESSING";
	public static final String VALID_RETURN = "valid.order.statuses.RETURN";
	public static final String VALID_CANCEL = "valid.order.statuses.CANCEL";
	public static final String VALID_DELIVERY = "valid.order.statuses.DELIVERY";

	//FOR MSD
	public static final String MSD_JS_URL = "msdjsURL";
	public static final String IS_MSD_ENABLED = "isMSDEnabled";
	public static final String TRACK_STATUS = "orderStatusMap";

	//For Payment
	public static final String PAYMENTTYPEERROR = "Exception while adding payment types:::: ".intern();
	public static final String PRIORITYBANKSERROR = "Exception while fetching priority banks for Netbanking==== ".intern();
	public static final String NONPRIORITYBANKSERROR = "Exception while fetching non-priority banks for Netbanking==== ".intern();
	public static final String EMITERMSERROR = "Exception while fetching bank terms for EMI ==== ".intern();
	public static final String BLACKLISTINGERROR = "Exception while fetching blacklisted customer for COD==== ".intern();
	//For Cancel Async
	public static final String MAX_RETRY = "cancel.crm.task.max.retry";
	public static final String TIME_DELAY = "cancel.crm.task.time.delay";


	public static final String CART_HOME_DELIVERY = "Home Delivery".intern();
	public static final String CART_EXPRESS_DELIVERY = "Express Delivery ".intern();
	public static final String CART_CLICK_COLLECT = "Click And Collect".intern();
	public static final int PRIORITY_INCREMENT = 1000;
	public static final String CLICK_AND_COLLECT = "click-and-collect";
	public static final String STATUS_FAILURE = "failure";
	public static final String STATUS_SUCESS = "sucess";

	public static final String COLLECTIONDAYS_CONFIG = "configurable-collectionDays";
	public static final String CONFIGURABLE_RADIUS = "configurable-radius";
	public static final String BRANDCODE = "MBH1";
	public static final String DEFAULT_COLLECTION_DAYS = "7";

	public static final String SAVE_STORE_TOPORUDCT_SUCCESS_MSG = "yes";
	public static final String SAVE_STORE_TOPORUDCT_FAIL_MSG = "no";
	public static final String UNKNOWN = "unknown".intern();
	public static final String DEFAULT_WISHLIST_NAME = "My Wishlist";
	public static final String CART_ENTRY_NULL = "Cart Entry not present".intern();
	public static final String STATIC_VIDEO = "store/_ui/responsive/common/images/video-play.png";
	public static final String B8000 = "B8000";
	public static final String B9327 = "B9327";
	public static final String E0000 = "E0000";
	// TISLUX-356
	public static final String CATEGORY = "category";
	public static final String PRODUCT = "product";
	public static final String ROOT_NAME = "Luxury";

	public static final String LUXURY_STYLE_SWATCH = "luxuryStyleSwatch";

	public static final String CA = "CA";
	public static final String CU = "CU";
	public static final String CDP = "CDP";
	public static final String SUCCESS = "success";
	public static final String SERVER_EXCEPTION = "Internal Server Error, Please try again later";
	public static final String PINCODE_NOT_SERVICEABLE = "Pincode not Serviceable";
	public static final String TATA_CLIQ = "TATA-CLIQ";
	public static final String REFUND_MODE_C = "C";
	public static final String RETURN_SELF_COURIER_FILE_DOWNLOAD_URL = "/my-account/returns/returnFileDownload?orderCode=";
	public static final String TRANSACTION_ID = "transactionId";
	public static final String AMPERSAND = "&";
	public static final String EQUALS_TO = "=";
	public static final String COLON = ":";
	public static final String FORWARD_SLASHES = "//";
	//Adding code for Luxury Facades
	public static final String LuxuryPrefix = "lux";
	public static final String MplPrefix = "mpl";
	public static final String PRODUCT_TYPE = "FineJewellery";
	public static final String SESSION_SERVICE = "sessionService";
	public static final String CHANNEL_WEB = "WEB";
	public static final String CHANNEL_MOBILE = "MOBILE";
	public static final String EGV_RESEND_EMAILAVAILABLE = "mpl.egv.resendEmailEnabled";
	public static final String CLIQ_CASH = "CliqCash";


	//Added for HomeFurnishing
	public static final String CONFIGURABLE_ATTRIBUTE = "classification.attributes.";
	public static final String MAP_CONFIGURABLE_ATTRIBUTE = "mapConfigurableAttribute";

	public static final String MAP_CONFIGURABLE_ATTRIBUTES = "mapConfigurableAttributes";
	public static final String HOME_FURNISHING = "HomeFurnishing";
	public static final String CLASSIFICATION_ATTR = "classification.attributes.";
	public static final String CLASSIFICATION_ATTR_HF = "HomeFurnishing.";
	public static final String CLASSIFICATION_ATTR_PD = "ProductDetails";
	public static final String CLASSIFICATION_ATTR_SI = "SetInformation";
	public static final String CLASSIFICATION_ATTR_SI_SPACE = "Set Information";
	public static final String CLASSIFICATION_ATTR_SI_SIZE = "Size";
	public static final String CLASSIFICATION_ATTR_CAINS = "CareInstructions";
	public static final String CLASSIFICATION_ATTR_WASHCARE = "WashCare";
	public static final String CLASSIFICATION_ATTR_PF = "ProductFeatures";
	public static final String CLASSIFICATION_ATTR_CARE_INS = "Care Instructions";
	public static final String CLASSIFICATION_ATTR_WASH_CARE = "Wash Care";
	public static final String KEY_PROD_PTS = "Key Product Points";
	public static final String SET_COMPONENT = "Set Component";
	public static final String SET_COMPONENT_DETAILS = "Set Componenent";
	public static final String NAME = "Name";
	public static final String QTY = "Quantity";
	public static final String DETAILS = "Details";
	public static final String OVERVIEW_SEC_SEQ = "classification.attributes.HomeFurnishing.sectionSeq";
	public static final String SET = "Set";
	public static final String YES = "Yes";
	public static final String ALPHBET_REGEX = "[*a-zA-Z]";
	public static final String SPACE_REGEX = "\\s";
	public static final String PIPE_REGEX = "\\|";
	public static final String NO_SPACE = "";
	public static final String EMPTY = "";
	public static final String SINGLE_SPACE = " ";
	public static final String UNDER_SCORE = "_";
	public static final String COMMA = ",";
	public static final String PIPE = "|";
	public static final String WARRANTY = "Warranty";
	public static final String WARRANTY_DETAILS_HF = "Warranty Details";

	private MarketplaceFacadesConstants()
	{
		//empty
	}
}