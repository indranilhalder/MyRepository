/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.commerceservices.event.RegisterEvent;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * @author 354634
 *
 */
public class MplRegisterEvent extends RegisterEvent implements ClusterAwareEvent
{
	private static final Logger LOG = Logger.getLogger(MplRegisterEvent.class);

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return (ConfigurationService) Registry.getApplicationContext().getBean("configurationService");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.event.ClusterAwareEvent#publish(int, int)
	 */
	@Override
	public boolean publish(final int sourceNodeId, final int targetNodeId)
	{
		final String sourceId = getConfigurationService().getConfiguration().getString("mpl.event.sourcenode.id");
		final String targetId = getConfigurationService().getConfiguration().getString("mpl.event.targetnode.id");
		LOG.debug("sourceId-->" + sourceId + "targetId-->" + targetId);
		if (StringUtils.isNotEmpty(sourceId) && StringUtils.isNotEmpty(targetId))
		{
			return sourceNodeId <= Integer.parseInt(sourceId) && targetNodeId >= Integer.parseInt(targetId);
		}
		else
		{
			LOG.debug("sourceNodeId-->" + sourceNodeId + "targetNodeId-->" + targetNodeId);
			return (sourceNodeId == targetNodeId);
		}
	}

}
