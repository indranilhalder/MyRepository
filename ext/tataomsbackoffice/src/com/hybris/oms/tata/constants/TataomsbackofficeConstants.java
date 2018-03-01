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
 */
package com.hybris.oms.tata.constants;

import de.hybris.platform.util.Config;


/**
 * Global class for all Ybackoffice constants. You can add global constants for your extension into this class.
 */
public final class TataomsbackofficeConstants extends GeneratedTataomsbackofficeConstants
{
	public static final String EXTENSIONNAME = "tataomsbackoffice";
	public static final String EXPRESSDELIVERY = "ED";
	public static final String RETURNDELIVERY = "RD";
	public static final String SCHEDULEDDELIVERY = "SD";
	public static final String SAVEMPLBUCCONFIG = "saveMplBucConfigaration";
	public static final String SCHEDULEDDELIVERY_ITEMDELETE = "sdItemDelete";
	public static final String EXPRESSDELIVERY_ITEMDELETE = "edItemDelete";
	public static final String RETURNDELIVERY_ITEMDELETE = "rdItemDelete";
	public static final String SCHEDULEDDELIVERY_POPUP_ITEMADD = "sdPopupSave";
	public static final String EXPRESSDELIVERY_POPUP_ITEMADD = "edPopupSave";
	public static final String RETURNDELIVERY_POPUP_ITEMADD = "rdPopupSave";
	public static final String SCHEDULEDDELIVERY_ITEMSAVE = "saveSdTimeSlots";
	public static final String EXPRESSDELIVERY_ITEMSAVE = "saveEdTimeSlots";
	public static final String RETURNDELIVERY_ITEMSAVE = "saveRdTimeSlots";
	public static final String ORDERSTATUS_NONE = "NONE";
	public static final String ORDERSTATUS_HOTCOURI = "HOTCOURI";
	public static final String ORDERSTATUS_ORDALLOC = "ORDALLOC";
	public static final String ORDERSTATUS_ODREALOC = "ODREALOC";
	public static final String ORDERSTATUS_PILIGENE = "PILIGENE";
	public static final String ORDERSTATUS_PICKCONF = "PICKCONF";
	public static final String ORDERSTATUS_SCANNED = "SCANNED";
	public static final String REVERSE_ORDERSTATUS_REVERSEAWB = "REVRSAWB";
	public static final String REVERSE_ORDERSTATUS_RETURINIT = "RETUINIT";
	public static final String LPNAME_NONE = "NONE";
	public static final String UPLOAD_LPANDAWB_FIELDS = "ordered,orderlineid,lpname,awbnumber,flowtype";
	public static final String SPLIT_BY_COMMA = ",";
	public static final String SPLIT_BY_NEWLINE = "\n";
	public static final String LPAWB_FORWARD_FiLE_PREFIX = "LPAWB_FORWARD_";
	public static final String LPAWB_REVERSE_FILE_PREFIX = "LPAWB_REVERSE_";
	public static final String AWB_FORWARD__FILE_PREFIX = "AWB_FORWARD_";
	public static final String AWB_REVERSE__FILE_PREFIX = "AWB_REVERSE_";
	public static final String LPAWB_FORWARD_FILE_PATH = Config.getParameter("tata.oms.lpawb.forward.path");
	public static final String LPAWB_REVERSE_FILE_PATH = Config.getParameter("tata.oms.lpawb.reverse.path");
	public static final String AWB_FORWARD_FILE_PATH = Config.getParameter("tata.oms.awb.forward.path");
	public static final String AWB_REVERSE_FILE_PATH = Config.getParameter("tata.oms.awb.reverse.path");
	public static final String LPAWB_RETURN_FILE_PATH = "";
	public static final String LPAWB_RETURN_FiLE_PREFIX = "";

	public static final String SCHEDULEDDELIVERY_POPUP_ITEMEDIT = "sdPopupEdit";
	public static final String EXPRESSDELIVERY_POPUP_ITEMEDIT = "edPopupEdit";
	public static final String RETURNDELIVERY_POPUP_ITEMEDIT = "rdPopupEdit";

	public static final String SCHEDULEDDELIVERY_EDIT = "sdItemEdit";
	public static final String EXPRESSDELIVERY_EDIT = "edItemEdit";
	public static final String RETURNDELIVERY_EDIT = "rdItemEdit";
    public static final String DELIVERY_PROMISED_REPORT_DEFAULT_DAYS = "backoffice.promisedReport.defaultReportDays";
    
    public static final String ERROR_CODE_10027 = "10027";
    public static final String ERROR_CODE_10004 = "10004";
    public static final String ERROR_CODE_10096 = "10096";
    public static final String ERROR_CODE_10528 = "10528";
    public static final String ERROR_CODE_10550 = "10550";
    public static final String ERROR_CODE_10086 = "10086";
    
    
    public static final String ERROR_CODE_10027_DESC = "Card is deactivated.";
    public static final String ERROR_CODE_10004_DESC = "Could not find card. Please enter valid card number.";
    public static final String ERROR_CODE_10096_DESC = "Merchant not authorized to accept this card.";
    public static final String ERROR_CODE_10528_DESC = "Wallet - Card Already Added";
    public static final String ERROR_CODE_10550_DESC = "Card Already Associated With Another Wallet. Card Cannot be Shared.";
    public static final String ERROR_CODE_10086_DESC = "Either card number or card pin is incorrect.";
	
	private TataomsbackofficeConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}