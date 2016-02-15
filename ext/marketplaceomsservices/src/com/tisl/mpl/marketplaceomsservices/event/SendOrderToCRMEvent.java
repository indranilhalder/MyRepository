/**
 *
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * @author 884206
 *
 */
public class SendOrderToCRMEvent extends AbstractEvent implements ClusterAwareEvent
{
	private static final long serialVersionUID = 1L;
	private final OrderModel OrderModel;

	private static final Logger LOG = Logger.getLogger(SendOrderToCRMEvent.class);

	public SendOrderToCRMEvent(final OrderModel orderModel)
	{
		this.OrderModel = orderModel;

	}

	/**
	 * @return the orderModel
	 */
	public OrderModel getOrderModel()
	{
		return OrderModel;
	}



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
		LOG.info("inside mpl order placed to crm event");
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
