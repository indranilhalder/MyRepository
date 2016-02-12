package com.tisl.mpl.jalo;

import com.tisl.mpl.constants.MarketplaceclientservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class MarketplaceclientservicesManager extends GeneratedMarketplaceclientservicesManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( MarketplaceclientservicesManager.class.getName() );
	
	public static final MarketplaceclientservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplaceclientservicesManager) em.getExtension(MarketplaceclientservicesConstants.EXTENSIONNAME);
	}
	
}
