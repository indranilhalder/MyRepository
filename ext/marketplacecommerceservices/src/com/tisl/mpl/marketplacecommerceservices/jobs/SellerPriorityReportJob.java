/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerPriorityReportService;
import com.tisl.mpl.model.MplSellerPriorityJobModel;
import com.tisl.mpl.util.ExceptionUtil;



/**
 * @author TCS
 * @description Cron Job to get the Report for any new entry and changes in Seller Priority Model
 *
 */
public class SellerPriorityReportJob extends AbstractJobPerformable<MplSellerPriorityJobModel>
{
	@Autowired
	private MplSellerPriorityReportService mplSellerPriorityReportService;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SellerPriorityReportJob.class.getName());



	/**
	 * @Descriptiion: CronJob to generate Audit log for Seller Priority
	 * @param: cronModel
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final MplSellerPriorityJobModel cronModel)
	{
		try
		{
			if (null != cronModel.getStartDate() && null != cronModel.getEndDate())
			{
				//This Method fetches all modified details in SellerPriority Model within the prescribed date Range
				getMplSellerPriorityReportService().fetchSpecificDetails(cronModel.getStartDate(), cronModel.getEndDate());
			}
			else
			{
				//This Method fetches all details in SellerPriority Model
				getMplSellerPriorityReportService().fetchAllDetails();
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
	 * @return the mplSellerPriorityReportService
	 */
	public MplSellerPriorityReportService getMplSellerPriorityReportService()
	{
		return mplSellerPriorityReportService;
	}



	/**
	 * @param mplSellerPriorityReportService
	 *           the mplSellerPriorityReportService to set
	 */
	public void setMplSellerPriorityReportService(final MplSellerPriorityReportService mplSellerPriorityReportService)
	{
		this.mplSellerPriorityReportService = mplSellerPriorityReportService;
	}

}
