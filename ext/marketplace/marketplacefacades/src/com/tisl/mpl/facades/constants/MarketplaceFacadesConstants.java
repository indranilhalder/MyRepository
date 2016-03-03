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

	public static final String PAYMENT_METHOS_COD = "COD";
	public static final String REFUND_FAILED = "Refund Failed";
	public static final String FAILURE = "FAILURE";

	public static final String TSHIPTHRESHOLDVALUE = "tship.item.threshold.value";
	public static final String TSHIPCODE = "TSHIP";
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




	private MarketplaceFacadesConstants()
	{
		//empty
	}
}
