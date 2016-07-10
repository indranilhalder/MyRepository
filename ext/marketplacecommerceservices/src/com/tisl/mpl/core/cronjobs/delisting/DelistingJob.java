/**
 *
 */
package com.tisl.mpl.core.cronjobs.delisting;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.DelistingJobHelperModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class DelistingJob extends AbstractJobPerformable<DelistingJobHelperModel>
{ //DelistingJobHelperModel

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DelistingJob.class.getName());

	@Autowired
	private DelistingProcessor mplDelistingProcessor;



	/**
	 * @return the mplDelistingProcessor
	 */
	public DelistingProcessor getMplDelistingProcessor()
	{
		return mplDelistingProcessor;
	}

	/**
	 * @param mplDelistingProcessor
	 *           the mplDelistingProcessor to set
	 */
	public void setMplDelistingProcessor(final DelistingProcessor mplDelistingProcessor)
	{
		this.mplDelistingProcessor = mplDelistingProcessor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final DelistingJobHelperModel delisting)
	{
		try
		{
			LOG.debug("Delisting Job Retry Mechanism...");

			getMplDelistingProcessor().process();

		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

	}



}
