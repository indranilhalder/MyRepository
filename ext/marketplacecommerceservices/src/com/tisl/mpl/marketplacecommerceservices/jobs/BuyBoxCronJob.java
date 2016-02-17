/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Utilities;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;


public class BuyBoxCronJob extends AbstractJobPerformable<CronJobModel>
{

	private static final Logger LOG = Logger.getLogger(BuyBoxCronJob.class);


	private ConfigurationService configurationService;


	@Autowired
	private BuyBoxService buyBoxService;

	/**
	 *
	 * This method is responsible to invalidate the pks every one hour
	 */


	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		LOG.debug("begin");

		//current time stamp
		final Calendar time = Calendar.getInstance();

		LOG.debug("current time is" + time.getTime());

		//Get the buybox cron job interval value from the local.property file
		final String interval = getConfigurationService().getConfiguration().getString("buybox.cronjob.interval");

		//substracted  one hour from current time
		time.add(Calendar.MINUTE, -Integer.parseInt(interval));

		LOG.debug("substracted one hour from  current time/After  time is" + time.getTime());

		final List<BuyBoxModel> invalidatepksList = buyBoxService.invalidatePkofBuybox(time.getTime());

		//if (null != invalidatepksList && invalidatepksList.size() > 0)
		if (CollectionUtils.isNotEmpty(invalidatepksList))
		{
			LOG.debug("Pks size :" + invalidatepksList.size());
			for (final BuyBoxModel buyboxModel : invalidatepksList)
			{

				final PK buyboxPK = buyboxModel.getPk();
				//invalidate the cache pks
				Utilities.invalidateCache(buyboxPK);


			}
		}
		LOG.debug("invalidated done");


		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}


	/**
	 * @param buyBoxService
	 *           the buyBoxService to set
	 */
	public void setBuyBoxService(final BuyBoxService buyBoxService)
	{
		this.buyBoxService = buyBoxService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
