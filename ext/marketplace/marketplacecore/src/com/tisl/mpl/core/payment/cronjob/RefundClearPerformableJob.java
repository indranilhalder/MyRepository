/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.RefundClearPerformableService;


/**
 * @author TCS
 *
 */
public class RefundClearPerformableJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(RefundClearPerformableJob.class.getName());

	@Autowired
	private RefundClearPerformableService refundClearPerformableService;

	@Override
	public PerformResult perform(final CronJobModel cronModel)
	{

		getRefundClearPerformableService().processRefundOrders();
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

	}

	public RefundClearPerformableService getRefundClearPerformableService()
	{
		return refundClearPerformableService;
	}

	public void setRefundClearPerformableService(final RefundClearPerformableService refundClearPerformableService)
	{
		this.refundClearPerformableService = refundClearPerformableService;
	}

}
