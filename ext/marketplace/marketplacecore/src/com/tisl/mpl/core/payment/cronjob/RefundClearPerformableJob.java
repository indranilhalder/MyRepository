/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.RefundClearPerformableService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class RefundClearPerformableJob extends AbstractJobPerformable<CronJobModel>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(RefundClearPerformableJob.class.getName());

	@Autowired
	private RefundClearPerformableService refundClearPerformableService;

	@Override
	public PerformResult perform(final CronJobModel cronModel)
	{
		final MplConfigurationModel configModel = getRefundClearPerformableService().getCronDetails(cronModel.getCode());



		if (null != configModel && null != configModel.getMplConfigDate())
		{
			final Date cronLastStartDate = configModel.getMplConfigDate();
			LOG.error("CRON LAST START DATE" + cronLastStartDate);

			getRefundClearPerformableService().processRefundOrders(cronLastStartDate);
			saveCronData(cronModel);
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		else
		{
			LOG.error("CRON LAST START DATE IS NULL IN CONFIGMODEL");
			saveCronData(cronModel);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

	}

	/**
	 * @return the refundClearPerformableService
	 */
	public RefundClearPerformableService getRefundClearPerformableService()
	{
		return refundClearPerformableService;
	}

	/**
	 * @param refundClearPerformableService
	 *           the refundClearPerformableService to set
	 */
	public void setRefundClearPerformableService(final RefundClearPerformableService refundClearPerformableService)
	{
		this.refundClearPerformableService = refundClearPerformableService;
	}

	private void saveCronData(final CronJobModel cronModel)
	{
		if (null != cronModel.getStartTime())
		{
			getRefundClearPerformableService().saveCronDetails(cronModel.getStartTime(), cronModel.getCode());
		}

	}


}
