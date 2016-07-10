/**
 *
 */
package com.tisl.mpl.coupon.job;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.coupon.service.CampaignVoucherDataService;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class CampaignVoucherJob extends AbstractJobPerformable<CronJobModel>
{
	private final static Logger LOG = Logger.getLogger(CampaignVoucherJob.class.getName());

	@Resource(name = "campaignVoucherDataService")
	private CampaignVoucherDataService campaignVoucherDataService;

	/**
	 * @Descriptiion: CronJob to create .csv File for Campaign Team
	 * @param: oModel
	 * @return :PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		try
		{
			LOG.debug("CampaignVoucherCronJob : CronJob Starts");
			generateFileData();
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
	 * @Descriptiion: CronJob to create .csv File for Campaign Team
	 *
	 */
	private void generateFileData()
	{
		LOG.debug("Generating the .CSV for Campaign Team ");

		getCampaignVoucherDataService().generateCSV();

		LOG.debug("CampaignVoucherCronJob : CronJob Ends");
	}

	/**
	 * @return the campaignVoucherDataService
	 */
	public CampaignVoucherDataService getCampaignVoucherDataService()
	{
		return campaignVoucherDataService;
	}

	/**
	 * @param campaignVoucherDataService
	 *           the campaignVoucherDataService to set
	 */
	public void setCampaignVoucherDataService(final CampaignVoucherDataService campaignVoucherDataService)
	{
		this.campaignVoucherDataService = campaignVoucherDataService;
	}

}
