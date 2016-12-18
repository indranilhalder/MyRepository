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
package com.tisl.mpl.coupon.constants;

/**
 * Global class for all Marketplacecoupon constants. You can add global constants for your extension into this class.
 */
public final class MarketplacecouponConstants extends GeneratedMarketplacecouponConstants
{
	public static final String EXTENSIONNAME = "marketplacecoupon";

	public static final String VOUCHERWITHINDATEQUERY = "select {d.voucher} from {DateRestriction as d} where sysdate>={d.startdate} and sysdate<={d.enddate}";
	public static final String COUPONCODE = "couponCode".intern();
	public static final String COUPONVALUE = "couponValue".intern();
	public static final String ZEROPOINTZEROONE = "0.01".intern();

	public static final String CRONJOB_TITLE = "The CronJob";

	//for customer list generation
	public static final String CUSTOMER_LIST_FILE_LOCATION = "closedCoupon.customer.list.path".intern();
	public static final String CUSTOMER_LIST_FILE_HEADER = "UID,FIRSTNAME,LASTNAME,ORIGINALUID,MOBILENO,GENDER,CREATIONTIME(dd/MM/yyyy HH:mm:ss),DEFAULTSHIPMENTADDRESS,DATEOFBIRTH(dd/MM/yyyy HH:mm:ss),DATEOFANNIVERSARY(dd/MM/yyyy HH:mm:ss)"
			.intern();
	public static final String CUSTOMER_LIST_FILE_NAME = "customerList".intern();

	public static final String CLOSED_VOUCHER = "select {v.pk} from {voucher as v JOIN userrestriction as ur ON {v.pk}={ur.voucher} JOIN daterestriction as dr ON {v.pk}={dr.voucher}} where {dr.startdate} <= sysdate and sysdate<= {dr.enddate} "
			+ " AND {ur.users} like ('%  ?customerPk %')" + " ORDER BY {dr.startdate} ASC";//,{dr.pk}

	public static final String CUSTOMER_LIST_FILE_EXTENSION = "closedCoupon.customer.list.extension";

	public static final String EMPTYSPACE = "".intern();
	public static final String CAMPAIGN_MULTIDATA_SEPERATOR = "|".intern();
	public static final String CAMPAIGN_FILE_LOCATION = "campaign.voucher.csv.path".intern();
	public static final String CAMPAIGN_FILE_NAME = "campaign_Voucher".intern();
	public static final String CAMPAIGN_HEADER = "IDENTIFIER,NAME,PROMOTION_TEXT,VOUCHER_CODE,VALUE,CURRENCY,REDEEM_QUANTITY,REDEEM_QUANTITY_USER,MAX_DISCOUNT,STARTDATE,ENDDATE,PRODUCTS,FOR_SELECTED_PRODUCTS,CATEGORIES,FOR_SELECTED_CATEGORIES,USERGROUP,USER,FOR_SELECTED_USER/USERGROUP"
			.intern();

	public static final String CAMPAIGN_FILE_NEW_LINE_SEPARATOR = "\n".intern();
	public static final String CAMPAIGN_FILE_DELIMITTER = ",".intern();

	public static final String COUPONREDEEM = "/redeem";
	public static final String COUPONRELEASE = "/release";

	//Coupon Error Messages
	public static final String PRICEEXCEEDED = "Price_exceeded".intern();
	public static final String INVALID = "Invalid".intern();
	public static final String EXPIRED = "Expired".intern();
	public static final String ISSUE = "Issue".intern();
	public static final String NOTAPPLICABLE = "Not_Applicable".intern();
	public static final String NOTRESERVABLE = "Not_Reservable".intern();
	public static final String USERINVALID = "User_Invalid".intern();
	public static final String FREEBIE = "Freebie".intern();
	public static final String RELEASEISSUE = "Release Issue".intern();
	//TPR-1075
	public static final String FIRSTPURUSERINVALID = "First_Purchase_User_Invalid".intern();
	//Coupon Exception Message
	public static final String EXCPRICEEXCEEDED = "total price exceeded".intern();
	public static final String EXCINVALID = "Voucher not found".intern();
	public static final String EXCEXPIRED = "Voucher cannot be redeemed".intern();
	public static final String EXCISSUE = "Error while".intern();
	public static final String EXCNOTAPPLICABLE = "Voucher is not applicable".intern();
	public static final String EXCNOTRESERVABLE = "Voucher is not reservable".intern();
	public static final String EXCFREEBIE = "freebie".intern();
	public static final String EXCUSERINVALID = "User not valid".intern();

	public static final String COUPONRELISSUE = "Issue with voucher release ".intern();
	public static final String CUSTOMERPK = "customerPk";
	public static final String BYDATE = "byDate";
	//TPR-1075
	public static final String EXCFIRSTPURUSERINVALID = "Voucher for New Customer".intern();
	//Delimiter used in CSV file
	public static final String COMMA_DELIMITER = ",";
	public static final String COLON_DELIMITER = ":";
	public static final String HYPHEN_DELIMITER = "-";
	public static final String NEW_LINE_SEPARATOR = "\n";

	public static final String VOUCHERCAMPAIGNJOBEXTN = "cronjob.campaign.voucher.extension".intern();
	public static final String DEFVOUCAMPAIGNJOBEXTN = ".csv".intern();

	private MarketplacecouponConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
