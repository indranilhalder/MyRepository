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

	public static final String BANKFORBINQUERY = "select {b:pk} from {Bin As b} WHERE {b.binno}=?bin";
	public static final String BINNO = "bin";
	public static final String BINDAO = "binDao";
	public static final String BINSERVICE = "binService";
	public static final String BINFACADE = "binFacade";
}
