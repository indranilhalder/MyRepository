/**
 *
 */
package com.tisl.mpl.core.cronjob.NPSMailer.jobs;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.NPSEmailerModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class NPSMailerCronJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(NPSMailerCronJob.class.getName());

	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	@Autowired
	private NotificationService notificationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		LOG.info("Entering perform :: NPSMailerCronJob");
		try
		{
			/*
			 * Query for fetching all the orderModels along with the transaction ids from commerce end whose delivery date
			 * is 24 hours ago
			 */
			final Map<OrderModel, AbstractOrderEntryModel> parentOrderAbstractEntryModel = getFetchSalesOrderService()
					.fetchOrderDetailsforDeliveryMail();
			final List<NPSEmailerModel> npsEmailerModelList = new ArrayList<NPSEmailerModel>();

			for (final Map.Entry<OrderModel, AbstractOrderEntryModel> entry : parentOrderAbstractEntryModel.entrySet())
			{

				final NPSEmailerModel npsEmailerModel = modelService.create(NPSEmailerModel.class);
				npsEmailerModel.setAbstractOrderEntry(entry.getValue());
				npsEmailerModel.setTransactionId(entry.getValue().getTransactionID());
				npsEmailerModel.setParentOrderNo(entry.getKey());
				npsEmailerModel.setCustomer((CustomerModel) entry.getKey().getUser());
				final ProcessState processState = notificationService.triggerNpsEmail(entry.getValue());
				if (processState.getCode().equals(ProcessState.SUCCEEDED.toString()))
				{
					npsEmailerModel.setIsEmailSent(Boolean.TRUE);
					npsEmailerModel.setTimeSent(new Date());
				}
				else
				{
					npsEmailerModel.setIsEmailSent(Boolean.FALSE);
				}
				npsEmailerModelList.add(npsEmailerModel);
			}
			modelService.saveAll(npsEmailerModelList);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected FetchSalesOrderService getFetchSalesOrderService()
	{
		return Registry.getApplicationContext().getBean("fetchSalesOrderServiceImpl", FetchSalesOrderService.class);
	}

}