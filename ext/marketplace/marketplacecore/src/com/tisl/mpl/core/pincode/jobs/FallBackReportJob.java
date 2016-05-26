/**
 *
 */
package com.tisl.mpl.core.pincode.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class FallBackReportJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(FallBackReportJob.class);

	private MplPincodeService mplPincodeService;

	/**
	 * The Method generates a csv report containing information regarding orders in queue
	 *
	 * @param oModel
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		try
		{
			LOG.debug("Inside OMS Fallback Order Details Report Job");

			final Map<String, Date> dataMap = mplPincodeService.getConfigurationData(oModel.getCode());
			if (MapUtils.isNotEmpty(dataMap))
			{
				jobPerformable(dataMap, oModel.getCode());
			}
			else
			{
				fetchAllData();
			}

			mplPincodeService.saveCronData(oModel.getStartTime(), oModel.getCode());
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


	/**
	 * Generates Report with All data
	 *
	 */
	private void fetchAllData()
	{
		mplPincodeService.fetchOrderData(null, new Date());
	}


	/**
	 * Generates Report with Data between Job Last Run Date and System Data
	 *
	 * @param dataMap
	 * @param jobCode
	 */
	private void jobPerformable(final Map<String, Date> dataMap, final String jobCode)
	{
		final Date lastRunDate = dataMap.get(jobCode);

		mplPincodeService.fetchOrderData(lastRunDate, new Date());
	}


	/**
	 * @return the mplPincodeService
	 */
	public MplPincodeService getMplPincodeService()
	{
		return mplPincodeService;
	}

	/**
	 * @param mplPincodeService
	 *           the mplPincodeService to set
	 */
	public void setMplPincodeService(final MplPincodeService mplPincodeService)
	{
		this.mplPincodeService = mplPincodeService;
	}


}
