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
package com.tisl.mpl.fulfilmentprocess.constants;

public final class MarketplaceFulfilmentProcessConstants extends GeneratedMarketplaceFulfilmentProcessConstants
{
	public static final String CONSIGNMENT_SUBPROCESS_END_EVENT_NAME = "ConsignmentSubprocessEnd";
	public static final String ORDER_PROCESS_NAME = "order-process";
	public static final String CONSIGNMENT_SUBPROCESS_NAME = "consignment-process";
	public static final String WAIT_FOR_WAREHOUSE = "WaitForWarehouse";
	public static final String CONSIGNMENT_PICKUP = "ConsignmentPickup";
	public static final String CONSIGNMENT_COUNTER = "CONSIGNMENT_COUNTER";
	public static final String PARENT_PROCESS = "PARENT_PROCESS";

	// Inventory Soft reservation
	public static final String OMS_INVENTORY_RESV_TYPE_CART = "cart";
	public static final String OMS_INVENTORY_RESV_TYPE_PAYMENT = "payment";
	public static final String OMS_INVENTORY_RESV_TYPE_ORDERHELD = "orderheld";
	public static final String OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE = "orderDeallocate";

	public static final String HOME_DELIVERY = "home-delivery";
	public static final String EXPRESS_DELIVERY = "express-delivery";
	public static final String CLICK_COLLECT = "click-and-collect";
	public static final String HD = "HD";
	public static final String ED = "ED";
	public static final String CnC = "CnC";
	public static final String CC = "CC";

	public static final String SUBMITTED = "SUBMITTED".intern();
	public static final String COMPLETED = "COMPLETED".intern();
	public static final String PENDING = "PENDING".intern();
	public static final String REVIEW = "REVIEW".intern();
	public static final String APPROVED = "APPROVED".intern();
	public static final String REJECTED = "REJECTED".intern();

	public static final String PAYMENT_SUCCESSFUL = "PAYMENT_SUCCESSFUL".intern();
	public static final String PAYMENT_FAILED = "PAYMENT_FAILED".intern();
	public static final String RMS_VERIFICATION_PENDING = "RMS_VERIFICATION_PENDING".intern();
	public static final String RMS_VERIFICATION_FAILED = "RMS_VERIFICATION_FAILED".intern();
	public static final String CANCELLED = "CANCELLED".intern();
	public static final String ORDER_CANCELLED = "ORDER_CANCELLED".intern();

	public static final String AUDITWITHGUIDQUERY = "select {a:pk} from {MplPaymentAudit As a} where {a.cartGUID}=?cartGUID order by {a.requestDate} desc"
			.intern();
	public static final String CARTGUID = "cartGUID".intern();
	public static final String EXCEPTION_IS = "Exception is : ";
	public static final String PROCESS = "Process: ";



	private MarketplaceFulfilmentProcessConstants()
	{
		//empty to avoid instantiating this constant class
	}
}

