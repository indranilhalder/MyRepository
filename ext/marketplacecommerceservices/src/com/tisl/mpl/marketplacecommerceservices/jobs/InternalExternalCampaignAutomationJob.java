/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.IntExtCampaignAutomationJobModel;
import com.tisl.mpl.marketplacecommerceservices.service.InternalExternalAutomationService;


/**
 * @author TCS
 *
 */
public class InternalExternalCampaignAutomationJob extends AbstractJobPerformable<IntExtCampaignAutomationJobModel>
{

	Logger LOG = Logger.getLogger(this.getClass());
	@Autowired
	private InternalExternalAutomationService automationService;

	/*
	 * cron job to perform periodic search for components and producing CSV/Excel
	 */
	@Override
	public PerformResult perform(final IntExtCampaignAutomationJobModel automation)
	{
		final Map<String, String> bannerAutomationReport = automationService.automationGetAllBanner();
		LOG.info("Automation Ran with Map" + bannerAutomationReport.toString());
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

}
