package com.tisl.mpl.jalo;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class MarketplacecommerceservicesManager extends GeneratedMarketplacecommerceservicesManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( MarketplacecommerceservicesManager.class.getName() );
	
	public static final MarketplacecommerceservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplacecommerceservicesManager) em.getExtension(MarketplacecommerceservicesConstants.EXTENSIONNAME);
	}
	
}
