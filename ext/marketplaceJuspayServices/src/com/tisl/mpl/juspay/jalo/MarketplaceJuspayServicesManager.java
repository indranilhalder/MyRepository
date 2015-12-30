package com.tisl.mpl.juspay.jalo;

import com.tisl.mpl.juspay.constants.MarketplaceJuspayServicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class MarketplaceJuspayServicesManager extends GeneratedMarketplaceJuspayServicesManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( MarketplaceJuspayServicesManager.class.getName() );
	
	public static final MarketplaceJuspayServicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplaceJuspayServicesManager) em.getExtension(MarketplaceJuspayServicesConstants.EXTENSIONNAME);
	}
	
}
