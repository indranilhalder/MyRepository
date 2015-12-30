/**
 *
 */
package com.tisl.mpl.integration.job;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class SalesOrderPaymentInfoJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SalesOrderPaymentInfoJob.class.getName());

	/**
	 * @Description : Fetch Customer Creation and Update Records
	 * @param: oModel
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		try
		{
			final MplConfigurationModel configModel = getFetchSalesOrderService().getCronDetails(oModel.getCode());
			if (null != configModel && null != configModel.getMplConfigDate())
			{
				LOG.debug("CRON START DATE" + configModel.getMplConfigDate());
				populateSpecifiedData(configModel.getMplConfigDate(), oModel.getStartTime());
			}
			else
			{
				LOG.debug("1st time call");
				populateOrderData();
			}

			saveCronData(oModel);
		}
		catch (final EtailBusinessExceptions exception)
		{
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @Description : Fetch All Parent Order Details within the stipulated date range
	 * @param mplConfigDate
	 * @param startTime
	 */
	private void populateSpecifiedData(final Date mplConfigDate, final Date startTime)
	{
		final List<OrderModel> orderData = getFetchSalesOrderService().fetchSpecifiedData(mplConfigDate, startTime);
		if (null != orderData && !orderData.isEmpty())
		{
			LOG.debug("specified orders db fetch successful: fico");
			getSalesOrderXMLUtility().generateOrderData(orderData);

		}
	}

	/**
	 * @Description : Fetch All Parent Order Details
	 * @return: void
	 */
	private void populateOrderData()
	{
		final List<OrderModel> orderData = getFetchSalesOrderService().fetchOrderDetails();
		if (null != orderData && !orderData.isEmpty())
		{
			LOG.debug("all orders db fetch successful");
			getSalesOrderXMLUtility().generateOrderData(orderData);
		}
	}

	/**
	 * @Description : Save Cron Job Details
	 * @param oModel
	 */
	private void saveCronData(final CronJobModel oModel)
	{
		if (null != oModel && null != oModel.getStartTime() && null != oModel.getCode())
		{
			getFetchSalesOrderService().saveCronDetails(oModel.getStartTime(), oModel.getCode());
		}

	}

	protected FetchSalesOrderService getFetchSalesOrderService()
	{
		return Registry.getApplicationContext().getBean("fetchSalesOrderServiceImpl", FetchSalesOrderService.class);
	}

	protected SalesOrderXMLUtility getSalesOrderXMLUtility()
	{
		return Registry.getApplicationContext().getBean("salesOrderXMLUtility", SalesOrderXMLUtility.class);
	}

}
