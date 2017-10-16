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
package com.tisl.mpl.core.constants;

/**
 * Global class for all MarketplaceCore constants. You can add global constants for your extension into this class.
 */
public final class MarketplaceCoreConstants extends GeneratedMarketplaceCoreConstants
{
	public static final String EXTENSIONNAME = "marketplacecore";
	public static final String ALL_CATEGORY = "all";
	public static final String SUCCESS = "SUCCESS";
	public static final String BRAND = "brand";
	public static final String CATEGORY = "category";
	public static final String SELLER = "seller";
	public static final String NAME = "name";
	public static final String OCCASSION = "Occasion";
	public static final String BRAND_CMS_PAGE_ID = "_brand";
	public static final String SELLER_CMS_PAGE_ID = "_seller";
	public static final String USER_LOCATION = "userLocation";
	public static final String SNS_SELLER_ID = "sellerId";

	public static final String BUYAALONGBGETSHIPPINGFREE = "BuyAalongBgetShippingFree".intern();
	public static final String BUYAANDBPERCENTAGEDISCOUNT = "BuyAandBPrecentageDiscount".intern();
	public static final String BUYAANDBGETC = "BuyAandBgetC".intern();
	public static final String BUYABFREEPERCENTAGEDISCOUNT = "BuyABFreePrecentageDiscount".intern();
	public static final String BOGO = "CustomProductBOGOFPromotion".intern();
	public static final String BUYXOFAGETBFREEANDDISCOUNT = "BuyXItemsofproductAgetproductBforfree".intern();
	public static final String BUYAPERCENTAGEDISCOUNT = "BuyAPercentageDiscount".intern();
	public static final String CARTDISCOUNTPROMO = "CartOrderThresholdDiscountPromotion".intern();
	public static final String CARTFREEBIEPROMO = "CustomOrderThresholdFreeGiftPromotion".intern();
	public static final String ABCASHBACKPROMO = "BuyAandBGetPrecentageDiscountCashback".intern();
	public static final String ACASHBACKPROMO = "BuyAGetPrecentageDiscountCashback".intern();
	public static final String CARTCASHBACKPROMO = "CartOrderThresholdDiscountCashback".intern();
	public static final String PROMOTION = "Promotion :".intern();
	public static final String PROMOTION_CODE = "Promotion Code :".intern();
	public static final String SPACE = " ".intern();
	public static final String BUYAABOVEXGETPERCENTAGEORAMOUNTOFF = "BuyAAboveXGetPercentageOrAmountOff".intern();

	public static final String PROMOCODE = "code".intern();
	public static final String MEDIA_WEBROOT = "${mediaweb.webroot:/medias}";

	public static final String MEDIA_LEGACY_PRETTY_URL_ENABLED = "${media.legacy.prettyURL:true}";
	public static final String CONFIGURATION_SER = "configurationService";

	public static final String FOLDER_CONF_REQ_ERROR = "Folder config is required to perform this operation";
	public static final String MEDIA_SRC_REQ_ERROR = "MediaSource is required to perform this operation";

	public static final String MEDIA_HOST_NAME = "media.dammedia.host";
	public static final String MEDIA_LOCAL_ENABLED = "media.local.wks";
	public static final String HTTPS = "https";
	public static final String HTTP = "http";

	public static final String SYSTEM = "sys_";
	public static final String ATTACHMENT = "attachment";
	public static final String SELLER_ID = "sellerId";

	private MarketplaceCoreConstants()
	{
		//empty
	}

	public static final String SeperaterHashHead = "\n################################### Cronjob Stats ##################################";
	public static final String SeperaterHash = "\n###################################################################################";
	public static final String EMPTY = "";

	//For Luxury Products
	public static final String LUXURY = "luxury";
	public static final String Marketplace = "Marketplace";

	// implement here constants used by this extension

	//Price Update
	public static final String MAX_THREADS = "mpl.price.batch.impex.max-threads";
	public static final String PRODUCT_CODE = "mpl.priceFallback.productCode";
	public static final String PRICE_HEADER = "INSERT_UPDATE PriceRow";
	public static final String PRICE_FALLBACK = "mpl.priceFallback.use";
	public static final String NEW_LINE = "\n";
	public static final String SEMICOLON = ";";
	public static final String PRODUCT = "product";
	public static final String PRODUCTCODE_STRING = ";productCodeString;";
	public static final String FOR = "for";
	public static final String PRICEFALLBACK = ";PRICEFALLBACK";
	public static final String IMPORTCONFIG_NOTFOUND = "Import config not found. The file %s won't be imported.";
	public static final String YES = "Y";


	//Added for CAR-183
	public static final String COLORFAMILYFOOTWEAR = "colorfamilyfootwear";
	public static final String COLORFAMILYFOOTWEARBLANK = "";
	public static final String DIALCOLORELECTRONICS = "dialcolor"; // for INC_12606
	public static final String DIALCOLORELECTRONICSBLANK = ""; // for INC_12606
	public static final String COLORELECTRONICS = "colorelectronics"; //INC144316508
	public static final String COLORELECTRONICSBLANK = ""; //INC144316508
	public static final String DYNAMICATTRIBUTE = "classification.attirbutes.dynamic.";
	public static final String LSH1 = "LSH1";
	public static final String INR = "INR";

	//Added for adhoc.impex
	public static final String releaseImpex = "marketplacecore.projectdata.release";
	public static final String ALL = "all";
	//PRDI-423
	public static final String HYPHEN_REGEX = "--+".intern();
	public static final String SINGLE_HYPHEN = "-".intern();
	public static final String BRAND_TAG = "<brand>".intern();
	public static final String REGEX = "[^a-zA-Z0-9+-]".intern();
	public static final int ZERO_INT = 0;
	public static final String APOSTROPHE = "'".intern();
	//public static final String AND = "and".intern();
	public static final String WHOLE_WORD_REGEX = "\\band\\b".intern();
	public static final String AMPERSAND = "&".intern();
}
