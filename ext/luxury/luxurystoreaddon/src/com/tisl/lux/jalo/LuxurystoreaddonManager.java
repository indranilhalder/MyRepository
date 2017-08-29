package com.tisl.lux.jalo;

import com.tisl.lux.constants.LuxurystoreaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class LuxurystoreaddonManager extends GeneratedLuxurystoreaddonManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( LuxurystoreaddonManager.class.getName() );
	
	public static final LuxurystoreaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (LuxurystoreaddonManager) em.getExtension(LuxurystoreaddonConstants.EXTENSIONNAME);
	}
	
}
