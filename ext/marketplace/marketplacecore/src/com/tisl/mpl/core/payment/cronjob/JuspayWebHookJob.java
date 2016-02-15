/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayWebHookService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class JuspayWebHookJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(JuspayWebHookJob.class.getName());

	@Autowired
	private JuspayWebHookService juspayWebHookService;



	/**
	 * @Description : Job to validate Audit Table and WebHook Table Entries
	 * @param:cronModel
	 *
	 */
	@Override
	public PerformResult perform(final CronJobModel cronModel)
	{
		try
		{
			LOG.debug("Validating Audit and WebHook Table entries");
			final MplConfigurationModel configModel = getJuspayWebHookService().getCronDetails(cronModel.getCode());
			if (null != configModel && null != configModel.getMplConfigDate())
			{
				LOG.debug("CRON LAST START DATE" + configModel.getMplConfigDate());
				final Double webHookjobTAT = getJuspayWebHookService().getWebHookJobTAT();
				if (webHookjobTAT.doubleValue() > 0)
				{
					final Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.MINUTE, -webHookjobTAT.intValue());
					final Date webHookTAT = cal.getTime();
					fetchSpecificWebHookData(webHookTAT, cronModel.getStartTime());
				}
				else
				{
					fetchSpecificWebHookData(configModel.getMplConfigDate(), cronModel.getStartTime());
				}
			}
			else
			{
				fetchWebHookData();
			}
			saveCronData(cronModel);
		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error("", exception);
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


	/**
	 * @Description : Specific Web Hook Data within date range
	 * @param mplConfigDate
	 * @param startTime
	 */
	private void fetchSpecificWebHookData(final Date mplConfigDate, final Date startTime)
	{
		getJuspayWebHookService().fetchSpecificWebHookData(mplConfigDate, startTime);

	}

	/**
	 * @Description : Save Cron Job run time
	 * @param cronModel
	 */
	private void saveCronData(final CronJobModel cronModel)
	{
		if (null != cronModel && null != cronModel.getStartTime() && null != cronModel.getCode())
		{
			getJuspayWebHookService().saveCronDetails(cronModel.getStartTime(), cronModel.getCode());
		}

	}

	/**
	 * @Description : Fetch Web Hook Data
	 */
	private void fetchWebHookData()
	{
		getJuspayWebHookService().fetchWebHookData();
	}


	/**
	 * @return the juspayWebHookService
	 */
	public JuspayWebHookService getJuspayWebHookService()
	{
		return juspayWebHookService;
	}


	/**
	 * @param juspayWebHookService
	 *           the juspayWebHookService to set
	 */
	public void setJuspayWebHookService(final JuspayWebHookService juspayWebHookService)
	{
		this.juspayWebHookService = juspayWebHookService;
	}




}
