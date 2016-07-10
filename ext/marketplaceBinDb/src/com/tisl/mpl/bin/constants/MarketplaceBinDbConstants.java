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
package com.tisl.mpl.bin.constants;

/**
 * Global class for all MarketplaceBinDb constants. You can add global constants for your extension into this class.
 */
public final class MarketplaceBinDbConstants /* extends GeneratedMarketplaceBinDbConstants */
{
	public static final String EXTENSIONNAME = "marketplaceBinDb";

	private MarketplaceBinDbConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public static final String BANKFORBINQUERY = "select {b:pk} from {Bin As b} WHERE {b.binno}=?bin and {b.version}=?version";
	public static final String BINNO = "bin";
	public static final String BINDAO = "binDao";
	public static final String BINSERVICE = "binService";
	public static final String BINFACADE = "binFacade";

	public static final String COMMA = ",".intern();
	public static final String BASESTORE_UID = "mpl".intern();

	public static final String BANK_FILE_LOCATION = "payment.bank.csv.path".intern();
	public static final String BANK_FILE_LOCATION_DATA = "${HYBRIS_DATA_DIR}/feed/bank".intern();
	public static final String BANK_FILE_NAME = "bank_data_master".intern();
	public static final String BANK_FILE_SLASH = "\"".intern();
	public static final String BANK_FILE_DELIMITTER = ",".intern();
	public static final String BANK_FILE_NEW_LINE_SEPARATOR = "\n".intern();

	public static final String BANKDATAQUERY = "select distinct {b:bankName} from {Bin As b} WHERE upper({b.bankName}) NOT IN ({{select distinct upper({bank:bankName}) from {Bank As bank}}})"
			.intern();

	public static final String BIN_VERSION = "version".intern();
	public static final String BIN_PRESENT_VERSION = "mpl.payment.bin.presentversion".intern();

}
