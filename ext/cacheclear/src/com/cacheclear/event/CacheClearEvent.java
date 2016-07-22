/**
 *
 */
package com.cacheclear.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import com.cacheclear.model.CacheClearCronJobModel;
import com.cacheclear.model.CacheClearingStatusModel;


/**
 * <h1>Cache Clear Event</h1>
 * <p>
 * This event is a cluster aware event.
 * </p>
 *
 * @author Krishnakumar Raju
 *
 */
public class CacheClearEvent extends AbstractEvent implements ClusterAwareEvent
{

	private final CacheClearCronJobModel cronjob;
	private final int nodeID;
	private final CacheClearingStatusModel status;

	/**
	 * @param cronjob
	 *           CacheClearCronJobModel
	 * @param nodeID
	 *           int
	 * @param status
	 *           CacheClearStatusModel
	 *
	 * @author Krishnakumar Raju
	 */
	public CacheClearEvent(final CacheClearCronJobModel cronjob, final int nodeID, final CacheClearingStatusModel status)
	{
		this.cronjob = cronjob;
		this.nodeID = nodeID;
		this.status = status;
	}

	/**
	 * The event is published only when the given nodeID matches the targetNodeID
	 * @param sourceNodeID
	 *           the source node publishing this event
	 * @param targetNodeID
	 *           the target node at which this event should be captured
	 * @return boolean
	 * @author Krishnakumar Raju
	 */
	@Override
	public boolean publish(final int sourceNodeID, final int targetNodeID)
	{
		return targetNodeID == nodeID;
	}


	/**
	 * @return the cronjob
	 */
	public CacheClearCronJobModel getCronjob()
	{
		return cronjob;
	}


	/**
	 * @return the nodeID
	 */
	public int getNodeID()
	{
		return nodeID;
	}


	/**
	 * @return the status
	 */
	public CacheClearingStatusModel getStatus()
	{
		return status;
	}

}
