/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

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

		refundSmsDao.getAllTransactionsForSms();

		final List<RefundSmsData> result = refundSmsDao.searchResultsForRefund(null);

		System.out.println("============" + result.get(0).getName() + "::::::::::::::" + result.get(0).getTransactionId()
				+ ":::::::::::::" + result.get(0).getPhoneNo());



		LOG.debug("Finished executing perform method of SendRefundSmsCronJob class...");

		// YTODO Auto-generated method stub
		return null;
	}
}
