/**
 * 
 */
package com.hybris.yps.nodewarmer.listener;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.event.events.AfterTenantRestartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.tenant.TenantService;

import javax.annotation.Resource;

import com.hybris.yps.nodewarmer.service.NodeWarmupService;


/**
 * @author brendan.dobbs
 * 
 */
public class TenantRestartListener extends AbstractEventListener<AfterTenantRestartEvent>
{
	@Resource
	private TenantService tenantService;

	@Resource
	private NodeWarmupService nodeWarmupService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.event.impl.AbstractEventListener#onEvent(de.hybris.platform.servicelayer.event
	 * .events.AbstractEvent)
	 */
	@Override
	protected void onEvent(final AfterTenantRestartEvent event)
	{
		if (!tenantService.getCurrentTenantId().equals(event.getTenantId()))
		{
			Registry.setCurrentTenantByID(event.getTenantId());
		}
		nodeWarmupService.run();
	}
}
