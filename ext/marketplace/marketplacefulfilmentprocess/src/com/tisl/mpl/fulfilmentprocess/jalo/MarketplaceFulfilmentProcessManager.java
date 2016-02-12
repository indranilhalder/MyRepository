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
package com.tisl.mpl.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.tisl.mpl.fulfilmentprocess.constants.MarketplaceFulfilmentProcessConstants;

import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class MarketplaceFulfilmentProcessManager extends GeneratedMarketplaceFulfilmentProcessManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( MarketplaceFulfilmentProcessManager.class.getName() );
	
	public static final MarketplaceFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplaceFulfilmentProcessManager) em.getExtension(MarketplaceFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
