/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class MplProcessOrderJob extends AbstractJobPerformable<CronJobModel>
{
	private final static Logger LOG = Logger.getLogger(MplProcessOrderJob.class.getName());

	@Resource(name = "mplProcessOrderService")
	MplProcessOrderService mplProcessOrderService;

	/**
	 * @Description- This job will process a Payment_Pending Order to Payment_Successful / Payment_Failed when there is
	 *               any problem in the redirection from Juspay
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		try
		{
			//calling process Pending order method
			getMplProcessOrderService().processPaymentPedingOrders();

		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception exception)
		{
			LOG.error("Exception======================", exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @return the mplProcessOrderService
	 */
	public MplProcessOrderService getMplProcessOrderService()
	{
		return mplProcessOrderService;
	}

	/**
	 * @param mplProcessOrderService
	 *           the mplProcessOrderService to set
	 */
	public void setMplProcessOrderService(final MplProcessOrderService mplProcessOrderService)
	{
		this.mplProcessOrderService = mplProcessOrderService;
	}




}
