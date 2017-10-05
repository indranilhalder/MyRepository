/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tisl.mpl.refunds.dao.RefundSmsDao;
import com.tisl.mpl.sms.SendSmsService;
import com.tisl.mpl.wsdto.BulkSmsPerBatch;


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
	@Autowired
	SendSmsService sendSmsService;
	@Autowired
	ConfigurationService configurationService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		LOG.debug("Inside perform method of SendRefundSmsCronJob class...");
		try
		{
			final String query = refundSmsDao.getAllTransactionsForSms();
			final List<BulkSmsPerBatch> result = refundSmsDao.searchResultsForRefund(query);
			final int rowCount = result.size();
			final int batch = configurationService.getConfiguration().getInt("bulksms.perbatch.sms");
			int dividedValue = 0;
			dividedValue = rowCount / batch;
			int remStart = 0;
			int remEnd = 0;
			int remainder = 0;
			//For removing remainder loop
			if (dividedValue == 0 && rowCount != 0)
			{
				remainder = rowCount;
				remStart = 0;
				remEnd = rowCount;
			}

			if (dividedValue * batch < rowCount)
			{
				remainder = rowCount - (dividedValue * batch);
			}
			final StringBuilder deleteDynamicQuery = new StringBuilder();
			int a = 0;
			int b = batch;
			List<BulkSmsPerBatch> result1 = null;
			boolean response = false;
			LOG.debug("========STEP:1==============");
			//parent loop
			for (int j = 1; j <= dividedValue; j++)
			{
				result1 = null;
				response = false;
				//for (int i = 1; i <= b; i++)
				//{
				result1 = result.subList(a, b);
				response = sendSmsService.sendBulkSms(result1);
				//}
				///delete dynamic query
				if (response)
				{
					for (final BulkSmsPerBatch obj : result1)
					{
						deleteDynamicQuery.append("'");
						deleteDynamicQuery.append(obj.getTxnId());
						deleteDynamicQuery.append("'");
						deleteDynamicQuery.append(",");
					}
				}
				a += batch;
				b += batch;
				if (b > rowCount)
				{
					remStart = rowCount - a;
					remEnd = rowCount;
					break;
				}
			}
			LOG.debug("========STEP:2==============");
			//Remainder loop
			if (remainder != 0)
			{
				for (int j = 1; j <= 1; j++)
				{
					result1 = null;
					response = false;

					//for (int i = rowCount - remainder; i <= rowCount; i++)
					//{
					result1 = result.subList(remStart, remEnd);
					response = sendSmsService.sendBulkSms(result1);
					//}
					///delete dynamic query
					if (response)
					{
						for (final BulkSmsPerBatch obj : result1)
						{
							deleteDynamicQuery.append("'");
							deleteDynamicQuery.append(obj.getTxnId());
							deleteDynamicQuery.append("'");
							deleteDynamicQuery.append(",");
						}
					}
				}
			}
			final String subQuery = deleteDynamicQuery.substring(0, deleteDynamicQuery.length() - 1);
			///delete rows
			LOG.debug("======== Delete query==========" + subQuery);
			refundSmsDao.deleteRows(subQuery);
			LOG.debug("Finished executing perform method of SendRefundSmsCronJob class...");
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Exception ex)
		{
			LOG.error(ex);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}
}
