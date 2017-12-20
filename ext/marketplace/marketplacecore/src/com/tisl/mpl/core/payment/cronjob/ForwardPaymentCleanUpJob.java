/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.ForwardPaymentCleanUpService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class ForwardPaymentCleanUpJob extends AbstractJobPerformable<CronJobModel>
{

	@Autowired
	ForwardPaymentCleanUpService forwardPaymentCleanUpService;

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	ModelService modelService;

	private final static Logger LOG = Logger.getLogger(ForwardPaymentCleanUpJob.class.getName());

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final MplConfigurationModel mplConfig = forwardPaymentCleanUpService.fetchConfigDetails(cronJob.getCode());

		final int skipDuration = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.FPC_DURATION_SKIP, 0);
		final int thresholdDuration = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.FPC_DURATION_THRESHOLD, 0);
		final int overlapDuration = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.FPC_DURATION_OVERLAP, 0);

		final Calendar endTime = Calendar.getInstance();
		endTime.add(Calendar.MINUTE, (0 - skipDuration));

		final Calendar startTime = Calendar.getInstance();
		startTime.setTime(endTime.getTime());
		startTime.add(Calendar.MINUTE, (0 - thresholdDuration));

		if (null != mplConfig && null != mplConfig.getMplConfigDate() && mplConfig.getMplConfigDate().after(startTime.getTime()))
		{
			startTime.setTime(mplConfig.getMplConfigDate());
		}
		else
		{
			LOG.error("MplConfiguration model not found or config date is less than max allowed duration");
		}

		startTime.add(Calendar.MINUTE, (0 - overlapDuration));

		LOG.debug("Order data start time: " + startTime.getTime());
		LOG.debug("Order data end time: " + endTime.getTime());

		final List<OrderModel> orderModelList = forwardPaymentCleanUpService.fetchSpecificOrders(startTime.getTime(),
				endTime.getTime());
		LOG.debug("No of orders fetched : " + orderModelList.size());

		cleanUpOrders(orderModelList);

		if (null != mplConfig)
		{
			mplConfig.setMplConfigDate(endTime.getTime());
			modelService.save(mplConfig);
		}
		else
		{
			LOG.error("MplConfiguration model not present for the cronjob");
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private void cleanUpOrders(final List<OrderModel> orderModelList)
	{
		for (final OrderModel orderModel : orderModelList)
		{
			forwardPaymentCleanUpService.cleanUpMultiplePayments(orderModel);
		}

	}

}
