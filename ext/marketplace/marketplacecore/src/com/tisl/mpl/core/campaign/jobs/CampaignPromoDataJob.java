/**
 *
 */
package com.tisl.mpl.core.campaign.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.CampaignPromoDataService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class CampaignPromoDataJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(CampaignPromoDataJob.class.getName());//PromotionCreationJob

	@Autowired
	private CampaignPromoDataService campaignPromoDataService;

	/**
	 * @Descriptiion: CronJob to create .csv File for Campaign Team
	 * @param: oModel
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		try
		{
			LOG.debug("Creating Promotion Data for Campaign Team");
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
	 * Generate File Data for Campaign Team
	 */
	private void generateFileData()
	{
		campaignPromoDataService.generateData();
	}

	/**
	 * @return the campaignPromoDataService
	 */
	public CampaignPromoDataService getCampaignPromoDataService()
	{
		return campaignPromoDataService;
	}

	/**
	 * @param campaignPromoDataService
	 *           the campaignPromoDataService to set
	 */
	public void setCampaignPromoDataService(final CampaignPromoDataService campaignPromoDataService)
	{
		this.campaignPromoDataService = campaignPromoDataService;
	}

}