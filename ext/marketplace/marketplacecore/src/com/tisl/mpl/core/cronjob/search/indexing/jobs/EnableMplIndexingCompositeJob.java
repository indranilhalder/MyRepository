/**
 *
 */
package com.tisl.mpl.core.cronjob.search.indexing.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;


/**
 * @author Prabhu Selvaraj
 *
 */
public class EnableMplIndexingCompositeJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(EnableMplIndexingCompositeJob.class);

	private CronJobService cronJobService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOG.info("Enable Mpl Indexing composite cronjob");

		final CronJobModel compositeCronJob = cronJobService.getCronJob("mplIndexingCompositeCronjob");
		compositeCronJob.setActive(Boolean.TRUE);
		modelService.save(compositeCronJob);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
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
	 * @return the cronJobService
	 */
	public CronJobService getCronJobService()
	{
		return cronJobService;
	}

	/**
	 * @param cronJobService
	 *           the cronJobService to set
	 */
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}



}