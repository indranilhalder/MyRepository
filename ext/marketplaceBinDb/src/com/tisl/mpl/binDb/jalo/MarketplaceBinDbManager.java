package com.tisl.mpl.binDb.jalo;

import com.tisl.mpl.binDb.constants.MarketplaceBinDbConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class MarketplaceBinDbManager extends GeneratedMarketplaceBinDbManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( MarketplaceBinDbManager.class.getName() );
	
	public static final MarketplaceBinDbManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MarketplaceBinDbManager) em.getExtension(MarketplaceBinDbConstants.EXTENSIONNAME);
	}
	
}
