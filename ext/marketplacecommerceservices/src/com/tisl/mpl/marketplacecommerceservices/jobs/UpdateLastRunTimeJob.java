/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplLastIndexTimeService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class UpdateLastRunTimeJob extends AbstractJobPerformable<CronJobModel>
{

	private static final Logger LOG = Logger.getLogger(UpdateLastRunTimeJob.class);

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "cronJobService")
	private CronJobService cronJobService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Autowired
	private MplLastIndexTimeService mplLastIndexTimeService;



	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{

		final String passedCronJobCode = cronJobModel.getCode();
		final String cronJobCode = passedCronJobCode.substring(0, passedCronJobCode.lastIndexOf("-"));

		final CronJobModel updateIndexCronJobModel = cronJobService.getCronJob(cronJobCode);

		MplConfigurationModel configModel = null;
		try
		{
			configModel = mplLastIndexTimeService.getMplConfiguration(cronJobCode);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.debug("MplConfig Doesn't exist for " + cronJobCode);
		}
		if (null != updateIndexCronJobModel
				&& updateIndexCronJobModel.getResult().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS_VAL)
				&& updateIndexCronJobModel.getStatus().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINISHED))
		{
			//final Date startTime = updateIndexCronJobModel.getStartTime();
			final Calendar startTime = Calendar.getInstance();

			startTime.setTime(updateIndexCronJobModel.getStartTime());

			final String interval = configurationService.getConfiguration().getString("index.cronjob.interval");

			//substracted  interval minute from start Time
			startTime.add(Calendar.MINUTE, -Integer.parseInt(interval));


			if (null != configModel)
			{
				configModel.setMplConfigDate(startTime.getTime());
				modelService.save(configModel);
			}
			else
			{
				final MplConfigurationModel newConfigModel = modelService.create(MplConfigurationModel.class);
				newConfigModel.setMplConfigCode(cronJobCode);
				newConfigModel.setMplConfigDate(startTime.getTime());
				modelService.save(newConfigModel);

			}

		}
		else
		{
			LOG.debug("Last " + cronJobCode + " didn't finished successfully. Hence timestamp is not updated in MplConfiguration");
		}


		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);


	}

}
