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
package com.tisl.mpl.cockpits.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;

// TODO: Auto-generated Javadoc
/**
 * Global class for all MarketplaceCockpits constants. You can add global
 * constants for your extension into this class.
 */
public final class MarketplaceCockpitsConstants extends
		GeneratedMarketplaceCockpitsConstants {
	
	/** The Constant EXTENSIONNAME. */
	public static final String EXTENSIONNAME = "marketplacecockpits";

	/** The Constant JASPER_REPORTS_MEDIA_FOLDER. */
	public final static String JASPER_REPORTS_MEDIA_FOLDER = "jasperreports";

	/** The Constant QUERY_FIND_ASSOCIATED_ORDERS. */
	public static final String QUERY_FIND_ASSOCIATED_ORDERS = "SELECT {o:PK} FROM {ORDER AS o} WHERE {o:code} LIKE ?orderId AND {o:code} != ?currentOrderId";
	
	/** The Constant EXCEPTION_IS. */
	public static final String EXCEPTION_IS = "Exception is : ";

	/** The Constant COUNTRY_CODE. */
	public static final String COUNTRY_CODE = "countryCode";
	
	/** The Constant ZERO. */
	public static final String ZERO="0";
	
	/** The Constant CONFIRM_ADDRESS_BTN. */
	public static final String CONFIRM_ADDRESS_BUTTON = "confirmAddressBtn";
	
	/** The Constant PHONE_NUMBER_HEADING. */
	public static final String PHONE_NUMBER_HEADING = "phoneNumberHeading";

	/** The Constant RESERVE_DURATION. */
	public static final String RESERVE_DURATION = "reserveDuration";

	/** The Constant INTEGRATION_STATUS. */
	public static final String INTEGRATION_STATUS = "integrationStatus";
	
	/** The Constant PIN_CODE. */
	public static final String PIN_CODE="pincode";

	/** The Constant BACKEND_ERROR. */
	public static final String BACKEND_ERROR = "backEndError";

	/** The Constant NO. */
	public static final String NO = "N";
	/** The Constant NO. */
	public static final String YES = "Y";

	/** The Constant SERVER_ERROR. */
	public static final String SERVER_ERROR = "serverError";
	
	/** The Constant COCKPIT_SERVICEABILITY_CHECK. */
	public static final String COCKPIT_SERVICEABILITY_CHECK_BYPASS="cscockpit.oms.serviceability.check.bypass";

	/** The Constant PIN_CODE_EMPTY. */
	public static final String PIN_CODE_EMPTY = "pinCodeEmpty";
	
	/** The Constant PIN_CODE_INVALID. */
	public static final String PIN_CODE_INVALID = "pinCodeInvalid";
	
	/** The Constant NO_SELLERS_FOR_PRODUCT. */
	public static final String NO_SELLERS_FOR_PRODUCT = "noSellersForProduct";
	
	/** The Constant CSCOCKPIT_WIDGET_CHECKOUT_PAYMENT. */
	public static final String CSCOCKPIT_WIDGET_CHECKOUT_PAYMENT_RESERVATION_TIME = "cscockpit.widget.checkout.cartReservationTime";
	
	/** The Constant INFO. */
	public static final String INFO = "Info";

	/** The Constant CSCOCKPIT_USER_GROUP_CANCELCSAGENTGROUP. */
	public static final String CSCOCKPIT_USER_GROUP_CANCELCSAGENTGROUP = "cscockpit.user.group.cancelcsagentgroup";

	/** The Constant CSCOCKPIT_USER_GROUP_REFUNDCSAGENTGROUP. */
	public static final String CSCOCKPIT_USER_GROUP_REFUNDCSAGENTGROUP = "cscockpit.user.group.refundcsagentgroup";

	/** The Constant CSCOCKPIT_USER_GROUP_REPLACECSAGENTGROUP. */
	public static final String CSCOCKPIT_USER_GROUP_REPLACECSAGENTGROUP = "cscockpit.user.group.replacecsagentgroup";

	//added for new User Group
	/** The Constant CSCOCKPIT_USER_GROUP_ALTERNATECONTACTCSAGENTGROUP. */
	public static final String CSCOCKPIT_USER_GROUP_ALTERNATECONTACTCSAGENTGROUP="cscockpit.user.group.alternatecontactcsagentgroup";
	
	/** The Constant NO_SELLER_COD. */
	public static final String NO_SELLER_COD = "noSellerCOD";

	/** The Constant NO_DELIVERY_ADDRESS. */
	public static final String NO_DELIVERY_ADDRESS = "noDeliveryAddress";

	/** The Constant NO_PHONE_NUMBER. */
	public static final String NO_PHONE_NUMBER = "noPhoneNumber";

	/** The Constant PRICING_FROM_BUY_BOX. */
	public static final String PRICING_FROM_BUY_BOX = "cscockpit.show.buybox.price";

	/** The Constant ADD_TO_BASKET. */
	public static final String ADD_TO_BASKET = "addToBasketBtn";

	/** The Constant NO_RESPONSE_FROM_SERVER. */
	public static final String NO_RESPONSE_FROM_SERVER = "noResponseFromServer";
	
	/** The Constant INR. */
	public static final String INR = "INR";

	/** The Constant HOME_DELIVERY. */
	public static final String HOME_DELIVERY = "home-delivery";
	
	/** The Constant EXPRESS_DELIVERY. */
	public static final String EXPRESS_DELIVERY="express-delivery";

	/** The Constant CLICK_AND_COLLECT. */
	public static final String CLICK_AND_COLLECT = "click-and-collect";
	
	/** The Constant ERROR. */
	public static final String ERROR = "Error";
	
	public static Map<String, String> delCodeMap = ImmutableMap.of(
		    "HD", "home-delivery",
		    "ED", "express-delivery",
		    "CnC", "click-and-collect"
		);
	
	public static Map<String, String> delNameMap = ImmutableMap.of(
		    "HD", "Home Delivery",
		    "ED", "Express Delivery",
		    "CnC", "Click and Collect"
		);
	
	public static Map<String, String> delOmsCodeMap = ImmutableMap.of(
		    "home-delivery","HD",
		    "express-delivery","ED",
		   "click-and-collect","CnC"
		);
	
	public static final String FAILURE = "FAILURE";
	
	
	public static final List<ConsignmentStatus> validInvoiceStatus = Arrays.asList(ConsignmentStatus.HOTC,
			ConsignmentStatus.OUT_FOR_DELIVERY,ConsignmentStatus.REACHED_NEAREST_HUB,ConsignmentStatus.DELIVERED
			);	
	
	public static final String COUPON_REDEEM = "coupon_redeem";
	
	public static final String SOURCE="CSCockpit";
	
	
}
