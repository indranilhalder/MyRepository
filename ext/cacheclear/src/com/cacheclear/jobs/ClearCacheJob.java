/**
 *
 */
package com.cacheclear.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.SimpleDateFormat;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.cacheclear.event.CacheClearEvent;
import com.cacheclear.model.CacheClearCronJobModel;
import com.cacheclear.model.CacheClearingStatusModel;


/**
 * <h1>Clearing Cache Job</h1>
 * <p>
 * This cronjob is used to clear specific cache on specific nodes. The nodes to clear are taken from the cronjob model
 * and set to CacheClearEvent before the event is published
 * </p>
 *
 * @author Krishnakumar Raju
 *
 */
public class ClearCacheJob extends AbstractJobPerformable<CronJobModel>
{
	private ModelService modelService;
	private EventService eventService;
	private static String DELIMITER = ",";

	private static final Logger LOG = Logger.getLogger(ClearCacheJob.class);

	/**
	 * The perform method creates a CacheClearingStatusModel. The cacheClearCronJob and code are set for this and saved.
	 * The nodes to be cleared are taken from the cacheClearCronJobModel after splitting the nodesToClear String
	 * attribute. The nodesToClear attribute is iterated and CacheClearEvent is created for each node. CacheClearEvent
	 * takes the cacheClearCronJobModel, current node and status as its constructor argument. The event is published for
	 * each node.
	 *
	 * @param CronJobModel
	 * @return PerformResult
	 * @author Krishnakumar Raju
	 */

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Inside cronjob to clear cache!");
		}
		try
		{
			if (cronJobModel instanceof CacheClearCronJobModel)
			{
				final CacheClearCronJobModel cacheClearCronJobModel = (CacheClearCronJobModel) cronJobModel;
				final CacheClearingStatusModel status = getModelService().create(CacheClearingStatusModel.class);
				final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
				final String date_string = dateFormat.format(cacheClearCronJobModel.getStartTime());
				status.setCode("cache_clear_status_" + date_string);
				status.setCacheClearCronJob(cacheClearCronJobModel);
				getModelService().save(status);

				final String nodesToClear = cacheClearCronJobModel.getNodesToClear();
				final String[] nodesArray = nodesToClear.split(DELIMITER);

				if (LOG.isDebugEnabled())
				{
					LOG.debug("Cronjob to clear cache will clear the regions - '" + cacheClearCronJobModel.getRegionsToClear() + "'"
							+ " on nodes - '" + nodesToClear + "'");
				}
				synchronized (this)
				{
					for (final String node : nodesArray)
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("Creating and publishing event for node - " + node);
						}
						final CacheClearEvent cacheClearEvent = new CacheClearEvent(cacheClearCronJobModel, Integer.parseInt(node),
								status);
						getEventService().publishEvent(cacheClearEvent);

					}
				}
			}
			else
			{
				throw new ClassCastException("Cannot cast " + cronJobModel.getClass() + " to CacheClearCronJobModel");
			}

			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final ClassCastException cce)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cronjob to clear cache failed unfortunately!", cce);
			}
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cronjob to clear cache failed unfortunately!", e);
			}
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}


}
