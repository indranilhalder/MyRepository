/**
 *
 */
package com.tisl.mpl.core.pincode.jobs;

import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.PincodeServiceabilityDataModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class MplPincodeDataJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MplPincodeDataJob.class);

	private MplPincodeService mplPincodeService;

	/**
	 * This method is responsible to invalidate the pks within a time range
	 *
	 * @param oModel
	 * @return PerformResult
	 */

	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		try
		{
			LOG.debug("Validating the Pincode Serviceability Data");
			final Calendar time = Calendar.getInstance();
			LOG.debug("Current time is" + time.getTime());

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
	 *
	 */
	private void fetchAllData()
	{
		final List<PincodeServiceabilityDataModel> dataList = mplPincodeService.fetchData();
		if (CollectionUtils.isNotEmpty(dataList))
		{
			invalidateData(dataList);
		}
	}

	/**
	 * Fetch Data for Job and invalidate the pks
	 *
	 * @param dataMap
	 * @param jobCode
	 */
	private void jobPerformable(final Map<String, Date> dataMap, final String jobCode)
	{
		final Date jobLastRunDate = dataMap.get(jobCode);
		final List<PincodeServiceabilityDataModel> dataList = mplPincodeService.getPincodeData(jobLastRunDate, new Date());
		if (CollectionUtils.isNotEmpty(dataList))
		{
			invalidateData(dataList);
		}
	}

	/**
	 * Invalidate the pKs
	 *
	 * @param dataList
	 */
	private void invalidateData(final List<PincodeServiceabilityDataModel> dataList)
	{
		LOG.debug("Pk List  size :" + dataList.size());
		for (final PincodeServiceabilityDataModel oModel : dataList)
		{
			final PK pinPk = oModel.getPk();
			//invalidate the cache pks
			Utilities.invalidateCache(pinPk);
		}
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
