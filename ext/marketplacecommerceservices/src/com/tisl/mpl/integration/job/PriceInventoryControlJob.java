/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author 884206
 *
 */
public class PriceInventoryControlJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PriceInventoryControlJob.class.getName());
	@Resource
	private ModelService modelService;

	final Date startDate = new Date();
	@Resource
	IAFeedUtility iaFeedUtil;

	private final String FEEDNAME = "priceinventorycontrol";

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

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOG.info("Entering perform :: PriceInventoryControlJob");
		try
		{

			{

				iaFeedUtil.executeExport(FEEDNAME);
			}
			cronJob.setEndTime(new Date());
			getModelService().save(cronJob);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
}
