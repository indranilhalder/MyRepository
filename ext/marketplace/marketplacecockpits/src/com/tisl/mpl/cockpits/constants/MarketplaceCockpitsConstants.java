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
	
	/** The Constant CSCOCKPIT_USER_GROUP_CHANGEDELIVERYCSAGENTGROUP. */
	public static final String CSCOCKPIT_USER_GROUP_CHANGEDELIVERYCSAGENTGROUP="cscockpit.user.group.changedeliverycsagentgroup";

	
	/** The Constant CSCOCKPIT_USER_GROUP_SYCNCSAGENTGROUP. */
	public static final String CSCOCKPIT_USER_GROUP_SYNCCSAGENTGROUP = "cscockpit.user.group.synccsagentgroup";

	
	/** The Constant CSCOCKPIT_USER_GROUP_STOREMANAGERAGENTGROUP for OIS. */
	public static final String CSCOCKPIT_USER_GROUP_STOREMANAGERAGENTGROUP = "storemanageragentgroup";

	
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
		    "CnC", "click-and-collect",
		    "CNC", "click-and-collect"
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
	public static final String FAILED = "FAILED";
	
	public static final List<ConsignmentStatus> validInvoiceStatus = Arrays.asList(ConsignmentStatus.HOTC,
			ConsignmentStatus.OUT_FOR_DELIVERY,ConsignmentStatus.REACHED_NEAREST_HUB,ConsignmentStatus.DELIVERED
			);	
	
	public static final String COUPON_REDEEM = "coupon_redeem";
	/* TPR-1075  */
	public static final String FIRST_PURCHASE_ERROR= "newCustomer_not_valid";
	
	public static final String SOURCE="CSCockpit";
	public static final String PAYMENT_MODE_COD="COD";
	public static final String FIRST_NAME_NOT_VALID = "Please Enter a Valid First Name";
	public static final String LAST_NAME_NOT_VALID = "Please Enter a Valid Last Name";
	public static final String LINE1_NOT_VALID = "Please enter valid Address line 1 ";
	public static final String LINE2_NOT_VALID= "Please enter valid Address line 2";
	public static final String LINE3_NOT_VALID = "Please enter valid  Address line 3";
	public static final String EMAIL_NOT_VALID = "Please enter valid Email";
	public static final String CITY_NOT_VALID = "Please enter valid City Name";
	public static final String STATE_NOT_VALID = "Please enter valid State Name";
	public static final String COUNTRY_NOT_VALID = "Please enter valid countryName";
	public static final String MOBILENUMBER_NOT_VALID = "Please enter valid Mobile Number";
	public static final String LAND_MARK_NOT_VALID = "Please enter valid landmark";
	public static final String PINCODE_NOT_SERVISABLE = "Pincode Is Not Servisable";
	public static final String SUCCESS="success";

	public static final String NONE_OF_ABOVE = "None of Above";
	public static final String OTHERS = "Others";
	public static final String NO_LANDMARKS_FOUND = "No LandMarks Found";
	public static final String SELECT_LANDMARK = "Select LandMark";
	public static final String CU = "CU";
	public static final String SD = "SD";
	public static final String CA = "CA";
	public static final String RETURN_TYPE="01";

	public static final String BOTH = "BOTH";
	public static final String TSHIP = "TSHIP";
	public static final String SSHIP = "SSHIP";
	public static final String ED = "ED";
	public static final String HD = "ED";
	public static final String TO = "TO";
	public static final String SPACE = " ";
	public static final String AND = "and";
	public static final String Between = "BETWEEN";
	public static final String NA = "NA";
	public static final String REFUND_MODE_C = "C";
	public final static String TICKET_SUB_TYPE_DNC = "DNC";
	public final static String RETURN_REFUND_MODE_N = "N";

	
	public static final String CSCOCKPITRETURN_URL = "juspay.cscockpit.returnUrl".intern();
	
	public static final String JUSPAYPAYMENTPAGEURL = "juspay.paymentpage.url".intern();
	
	public static final String ORDERPAYMENTSTATUSURL = "juspay.paymentstatus.url".intern();
	
	public static final String PAYMENT_METHOD_TYPE= "CARD";
	
	public static final String OIS_NETBANKING = "Netbanking";
	
	public static final String JUSPAY_RESPONSE_CARD_KEY = "card";
	
	public static final String JUSPAY_RESPONSE_CARTTYPE_KEY = "card_type";
	
	public static final String JUSPAY_ORDER_STATUS = "CHARGED";
	
	public static final String PAYMENT_SUCCESSFUL = "Payment Successsful";
	
	public static final String PAYMENT_UNSUCCESSFUL = "Please try again";
	
	public static final String PAYMENT_COMPLETED = "Either You have already paid or System issue, Please Check again OR Contact with Site Administrator";
	
	public static final String NONCOD_PRODUCT_EXIST = "Non COD Product Exist, Please Check again OR Contact with Site Administrator";
	
	public static final String JUSPAY_PAYMENT = "paynow";
	
	public static final String JUSPAY_PAYMENT_VALUE = "Pay Now";
	
	public static final String LUXURYPREFIX = "lux";
}
