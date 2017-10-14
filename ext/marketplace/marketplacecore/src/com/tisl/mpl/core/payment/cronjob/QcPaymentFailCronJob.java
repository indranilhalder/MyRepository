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

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplQcPaymentFailService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author Techouts
 *
 *         This Cron Job Is used To Refund Money Of Both QC and Juspay , If the payment is not successfull from QC
 *
 *
 */

public class QcPaymentFailCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOG = Logger.getLogger(MplBinErrorJob.class.getName());

	@Autowired
	private MplQcPaymentFailService mplQcPaymentFailService;

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		try
		{
			LOG.debug("Inside QcPaymentFailCron job");

			// Fetch the Orders Which Are in RMS_VERIFICATION_FAILED
			try
			{
				mplQcPaymentFailService.processQcPaymentFailedOrders();
			}
			catch (final Exception e)
			{
				LOG.error("Exception occurred while Refunding" + e.getMessage());
			}

			try
			{
				generateFileData();
			}
			catch (final Exception e)
			{
				LOG.error("Exception occurred while Generating File" + e.getMessage());
			}

			
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
	 * Generate File Data for Bin Error
	 */
	private void generateFileData()
	{
		mplQcPaymentFailService.generateData();
	}

}