/**
 *
 */
package com.tisl.mpl.core.cronjob.NPSMailer.jobs;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.NPSMailerModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.model.MplConfigurationModel;
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
	 * TPR-1984 This cron job is triggered as configured to run at 12 PM and 4 PM
	 *
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
			Map<OrderModel, OrderEntryModel> parentOrderAbstractEntryModel = null;
			final MplConfigurationModel configModel = getFetchSalesOrderService().getCronDetails(oModel.getCode());
			if (null != configModel && null != configModel.getMplConfigDate())
			{
				parentOrderAbstractEntryModel = getFetchSalesOrderService().fetchOrderDetailsforDeliveryMail(
						configModel.getMplConfigDate());
			}
			saveCronData(oModel);

			final List<NPSMailerModel> npsEmailerModelList = new ArrayList<NPSMailerModel>();
			if (MapUtils.isNotEmpty(parentOrderAbstractEntryModel))
			{
				LOG.info("parentOrderAbstractEntryModel is not empty");
				for (final Map.Entry<OrderModel, OrderEntryModel> entry : parentOrderAbstractEntryModel.entrySet())
				{
					LOG.info("Cronjob perform" + parentOrderAbstractEntryModel.entrySet());

					final NPSMailerModel npsEmailerModel = modelService.create(NPSMailerModel.class);
					npsEmailerModel.setAbstractOrderEntry(entry.getValue());
					npsEmailerModel.setTransactionId(entry.getValue().getTransactionID());
					npsEmailerModel.setParentOrderNo(entry.getKey());
					npsEmailerModel.setCustomer((CustomerModel) entry.getKey().getUser());
					//notificationService.triggerNpsEmail(entry.getValue(), entry.getKey());
					//npsEmailerModel.setIsEmailSent(Boolean.TRUE);
					//npsEmailerModel.setTimeSent(new Date());

					final String processState = notificationService.triggerNpsEmail(entry.getValue(), entry.getKey());
					if (processState.equalsIgnoreCase("SUCCEEDED"))
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
		}
		catch (final ModelSavingException e)
		{
			LOG.error(e.getMessage());
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
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

}