/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.FPCRefundEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.marketplacecommerceservices.service.ForwardPaymentCleanUpService;
import com.tisl.mpl.marketplacecommerceservices.service.MplQcPaymentFailService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class ForwardPaymentCleanUpJob extends AbstractJobPerformable<CronJobModel>
{

	@Autowired
	ForwardPaymentCleanUpService forwardPaymentCleanUpService;

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	ModelService modelService;

	@Autowired
	private MplQcPaymentFailService mplQcPaymentFailService;

	private final static Logger LOG = Logger.getLogger(ForwardPaymentCleanUpJob.class.getName());

	private static final String COMMA = ",";
	private static final String DUPLICATE_PAYMENT = "DUPLICATE_PAYMENT";
	private static final String QC_DUPLICATE_PAYMENT = "QC_DUPLICATE_PAYMENT";
	private static final String PAYMENT_FAILED = "PAYMENT_FAILED";
	private static final String ORDER_NOT_GENERATED = "ORDER_NOT_GENERATED";
	private static final String RMS_FAILED = "RMS_FAILED";
	private static final String COD_CHARGED = "COD_CHARGED";

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		final MplConfigurationModel mplConfig = forwardPaymentCleanUpService.fetchConfigDetails(cronJob.getCode());

		final int skipDuration = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.FPC_DURATION_SKIP, 0);
		final int thresholdDuration = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.FPC_DURATION_THRESHOLD, 0);
		final int overlapDuration = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.FPC_DURATION_OVERLAP, 0);

		final Calendar endTime = Calendar.getInstance();
		endTime.add(Calendar.MINUTE, (0 - skipDuration));

		final Calendar startTime = Calendar.getInstance();
		startTime.setTime(endTime.getTime());
		startTime.add(Calendar.MINUTE, (0 - thresholdDuration));

		if (null != mplConfig && null != mplConfig.getMplConfigDate() && mplConfig.getMplConfigDate().after(startTime.getTime()))
		{
			startTime.setTime(mplConfig.getMplConfigDate());
		}
		else
		{
			LOG.error("MplConfiguration model not found or config date is less than max allowed duration");
		}

		startTime.add(Calendar.MINUTE, (0 - overlapDuration));

		LOG.debug("Order data start time: " + startTime.getTime());
		LOG.debug("Order data end time: " + endTime.getTime());



		createdRefundEntries(startTime.getTime(), endTime.getTime());
		initiateRefundProcess(startTime.getTime());



		if (null != mplConfig)
		{
			mplConfig.setMplConfigDate(endTime.getTime());
			modelService.save(mplConfig);
		}
		else
		{
			LOG.error("MplConfiguration model not present for the cronjob");
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private void createdRefundEntries(final Date startTime, final Date endTime)
	{
		final String refundTypes = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.FPC_REFUND_TYPES);
		final List<String> refundTypeList = Arrays.asList(refundTypes.split(COMMA));

		if (refundTypeList.contains(DUPLICATE_PAYMENT))
		{
			final List<OrderModel> multiPayOrderList = forwardPaymentCleanUpService.fetchOrdersWithMultiplePayments(startTime,
					endTime);
			if (CollectionUtils.isNotEmpty(multiPayOrderList))
			{
				for (final OrderModel orderModel : multiPayOrderList)
				{
					try
					{
						forwardPaymentCleanUpService.createRefundEntryForMultiplePayments(orderModel, false);
					}
					catch (final Exception e)
					{
						LOG.error("Error while processing refund for order: " + orderModel.getCode());
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		else
		{
			LOG.error("ForwardPaymentCleanUp has been disabled for Duplicate Payment");
		}

		if (refundTypeList.contains(QC_DUPLICATE_PAYMENT))
		{
			final List<OrderModel> multiPayCliqCashOrderList = forwardPaymentCleanUpService.fetchCliqCashOrdersWithMultiplePayments(
					startTime, endTime);
			if (null != multiPayCliqCashOrderList && CollectionUtils.isNotEmpty(multiPayCliqCashOrderList))
			{
				for (final OrderModel orderModel : multiPayCliqCashOrderList)
				{
					try
					{
						forwardPaymentCleanUpService.createRefundEntryForMultiplePayments(orderModel, true);
					}
					catch (final Exception e)
					{
						LOG.error("Error while processing refund for order: " + orderModel.getCode());
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		else
		{
			LOG.error("ForwardPaymentCleanUp has been disabled for CliqCash Duplicate Payment");
		}

		if (refundTypeList.contains(PAYMENT_FAILED))
		{
			final List<OrderModel> failedOrderList = forwardPaymentCleanUpService.fetchPaymentFailedOrders(startTime, endTime);
			if (CollectionUtils.isNotEmpty(failedOrderList))
			{
				for (final OrderModel orderModel : failedOrderList)
				{
					try
					{
						forwardPaymentCleanUpService.createRefundEntryForFailedOrders(orderModel);
					}
					catch (final Exception e)
					{
						LOG.error("Error while processing refund for order: " + orderModel.getCode());
						LOG.error(e.getMessage(), e);
					}
					final String splitInfoMode = orderModel.getSplitModeInfo();
					LOG.debug("Payment SplitMode for order  " + orderModel.getCode() + " " + splitInfoMode);
					if (null != splitInfoMode
							&& splitInfoMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_MODE_SPLIT))
					{
						try
						{
							mplQcPaymentFailService.processQcRefund(orderModel);
						}
						catch (final Exception e)
						{
							LOG.error("Error while processing QC refund for order: " + orderModel.getCode());
							LOG.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		else
		{
			LOG.error("ForwardPaymentCleanUp has been disabled for Payment Failed Orders");
		}

		if (refundTypeList.contains(COD_CHARGED))
		{
			final List<OrderModel> codChargedOrderList = forwardPaymentCleanUpService.fetchCodChargedOrder(startTime, endTime);
			if (CollectionUtils.isNotEmpty(codChargedOrderList))
			{
				for (final OrderModel orderModel : codChargedOrderList)
				{
					try
					{
						forwardPaymentCleanUpService.createRefundEntryForCodChargedOrders(orderModel);
					}
					catch (final Exception e)
					{
						LOG.error("Error while processing refund for order: " + orderModel.getCode());
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
		else
		{
			LOG.error("ForwardPaymentCleanUp has been disabled for COD Charged Orders");
		}

		if (refundTypeList.contains(RMS_FAILED))
		{
			final Calendar rmsStartTime = Calendar.getInstance();
			rmsStartTime.setTime(startTime);
			final int rmsTAT = configurationService.getConfiguration().getInt(MarketplacecommerceservicesConstants.FPC_RMS_TAT, 360);
			rmsStartTime.add(Calendar.MINUTE, -rmsTAT);
			final List<OrderModel> rmsFailedOrderList = forwardPaymentCleanUpService.fetchRmsFailedOrders(rmsStartTime.getTime(),
					endTime);
			if (CollectionUtils.isNotEmpty(rmsFailedOrderList))
			{
				for (final OrderModel orderModel : rmsFailedOrderList)
				{
					try
					{
						forwardPaymentCleanUpService.createRefundEntryForRmsFailedOrders(orderModel);
					}
					catch (final Exception e)
					{
						LOG.error("Error while processing refund for order: " + orderModel.getCode());
						LOG.error(e.getMessage(), e);
					}

					final String splitInfoMode = orderModel.getSplitModeInfo();
					LOG.debug("Payment SplitMode for order  " + orderModel.getCode() + " " + splitInfoMode);
					if (null != splitInfoMode
							&& splitInfoMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_MODE_SPLIT))
					{
						try
						{
							mplQcPaymentFailService.processQcRefund(orderModel);
						}
						catch (final Exception e)
						{
							LOG.error("Error while processing QC refund for order: " + orderModel.getCode());
							LOG.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		else
		{
			LOG.error("ForwardPaymentCleanUp has been disabled for RMS failed Orders");
		}

		if (refundTypeList.contains(ORDER_NOT_GENERATED))
		{
			final List<MplPaymentAuditModel> auditList = forwardPaymentCleanUpService.fetchAuditsWithoutOrder(startTime, endTime);
			if (CollectionUtils.isNotEmpty(auditList))
			{
				for (final MplPaymentAuditModel auditModel : auditList)
				{
					try
					{
						forwardPaymentCleanUpService.createRefundEntryForAuditsWithoutOrder(auditModel);
					}
					catch (final Exception e)
					{
						LOG.error("Error while processing refund for audit: " + auditModel.getAuditId());
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}

	}

	@SuppressWarnings("boxing")
	private void initiateRefundProcess(final Date startTime)
	{
		final Boolean tatEnabled = configurationService.getConfiguration().getBoolean(
				MarketplacecommerceservicesConstants.FPC_TAT_ENABLED, Boolean.TRUE);
		final int tatDuration = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.FPC_TAT_DURATION, 0);
		final Calendar expireTime = Calendar.getInstance();
		expireTime.setTime(startTime);
		expireTime.add(Calendar.MINUTE, (0 - tatDuration));
		final List<FPCRefundEntryModel> refundList = forwardPaymentCleanUpService
				.fetchSpecificRefundEntries(MarketplacecommerceservicesConstants.ZERO);
		for (final FPCRefundEntryModel refundEntry : refundList)
		{
			try
			{
				if (tatEnabled)
				{
					if (refundEntry.getCreationtime().before(expireTime.getTime()))
					{
						forwardPaymentCleanUpService.expireRefundEntry(refundEntry);
					}
					else
					{
						forwardPaymentCleanUpService.processRefund(refundEntry);
					}
				}
				else
				{
					forwardPaymentCleanUpService.processRefund(refundEntry);
				}
			}
			catch (final Exception e)
			{
				LOG.error("Exception while processing the refund for audit : " + refundEntry.getAuditId());
				LOG.error(e.getMessage(), e);
			}
		}
	}
}