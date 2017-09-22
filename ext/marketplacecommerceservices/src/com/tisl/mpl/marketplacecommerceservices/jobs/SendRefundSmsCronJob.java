/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tisl.mpl.data.RefundSmsData;
import com.tisl.mpl.refunds.dao.RefundSmsDao;


/**
 * @author TCS
 *
 */
public class SendRefundSmsCronJob extends AbstractJobPerformable<CronJobModel>
{
	private final static Logger LOG = Logger.getLogger(SendRefundSmsCronJob.class);

	@Autowired
	@Qualifier("refundsms")
	RefundSmsDao refundSmsDao;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		LOG.debug("Inside perform method of SendRefundSmsCronJob class...");
		try
		{
			final String query = refundSmsDao.getAllTransactionsForSms();

			final List<RefundSmsData> result = refundSmsDao.searchResultsForRefund(query);

			for (final RefundSmsData obj : result)
			{
				System.out.println("============" + obj.getName() + "::::::::::::::" + obj.getTransactionId() + ":::::::::::::"
						+ obj.getPhoneNo());
			}



			LOG.debug("Finished executing perform method of SendRefundSmsCronJob class...");
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Exception ex)
		{
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}
}
