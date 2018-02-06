/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.RefundFomType;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.Refund;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.RefundClearPerformableDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.RefundClearPerformableService;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
public class DefaultRefundClearPerformableServiceImpl implements RefundClearPerformableService
{

	private final static Logger LOG = Logger.getLogger(DefaultRefundClearPerformableServiceImpl.class.getName());

	@Autowired
	private RefundClearPerformableDao refundClearPerformableDao;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;


	private ModelService modelService;

	final static Double refundClearTATFinal = new Double(10);

	final static Double refundStartTime = new Double(120);

	private static final String SUCCESS = "SUCCESS";

	@Override
	public void processRefundOrders()
	{

		List<ConsignmentModel> consignmentModelList = new ArrayList<ConsignmentModel>();

		final String refundClearTAT = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.REFUNDCLEAR_SKIPTIME);
		final String refundOrderFetchStartTime = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.ORDERFETCH_STARTTIME);

		final Double skipRefundOrdersTAT = (StringUtils.isNotEmpty(refundClearTAT) ? Double.valueOf(refundClearTAT)
				: refundClearTATFinal);
		final Double skipPendingOrdersTAT = (StringUtils.isNotEmpty(refundOrderFetchStartTime) ? Double
				.valueOf(refundOrderFetchStartTime) : refundStartTime);

		final Calendar cal = Calendar.getInstance();

		cal.setTime(new Date());


		cal.add(Calendar.MINUTE, -skipRefundOrdersTAT.intValue());
		final Date queryTAT = cal.getTime();

		final Calendar cal_order = Calendar.getInstance();
		cal_order.setTime(new Date());
		cal_order.add(Calendar.MINUTE, -skipPendingOrdersTAT.intValue());
		final Date queryStartTime = cal_order.getTime();

		consignmentModelList = refundClearPerformableDao.getRefundClearConsignments(queryTAT, queryStartTime);

		if (CollectionUtils.isNotEmpty(consignmentModelList))
		{
			for (final ConsignmentModel consignment : consignmentModelList)
			{
				final OrderModel order = (OrderModel) consignment.getOrder();
				if (null != order && !checkCOD(order))
				{
					Map<String, RefundTransactionMappingModel> refundTransactionMap = new HashMap<String, RefundTransactionMappingModel>();
					AbstractOrderEntryModel orderEntry = null;
					if (consignment.getStatus().equals(ConsignmentStatus.REFUND_IN_PROGRESS)
							|| consignment.getStatus().equals(ConsignmentStatus.REFUND_INITIATED))
					{

						final Set consignmentEntries = consignment.getConsignmentEntries();
						for (final Iterator iteratorC = consignmentEntries.iterator(); iteratorC.hasNext();)
						{
							final ConsignmentEntryModel consignmentEntry = (ConsignmentEntryModel) iteratorC.next();
							orderEntry = consignmentEntry.getOrderEntry();
							if (null != orderEntry)
							{
								break;
							}
						}

					}
					refundTransactionMap = refundClearPerformableDao.fetchRefundTransactionMapping(orderEntry);
					LOG.error("Going to process the Order NO:" + order.getCode());
					checkWebhookEntryForRefund(order, refundTransactionMap, consignment, orderEntry);
				}
			}
		}

	}

	private void checkWebhookEntryForRefund(final OrderModel order,
			final Map<String, RefundTransactionMappingModel> refundTransactionMap, final ConsignmentModel consignment,
			final AbstractOrderEntryModel orderEntry)
	{
		String juspayOrderID = null;
		for (final PaymentTransactionModel paymentTransaction : order.getPaymentTransactions())
		{
			if (null != juspayOrderID)
			{
				break;
			}
			for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
			{
				if ((PaymentTransactionType.CAPTURE.equals(paymentTransactionEntry.getType()) || PaymentTransactionType.AUTHORIZATION
						.equals(paymentTransactionEntry.getType()))
						&& ("success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus()) || "ACCEPTED"
								.equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus())))
				{
					juspayOrderID = paymentTransactionEntry.getRequestToken();
					break;
				}
			}
		}
		if (StringUtils.isNotEmpty(juspayOrderID))
		{
			checkStatusMakeRefund(juspayOrderID, order, refundTransactionMap, consignment, orderEntry);
		}

	}

	private void checkStatusMakeRefund(final String juspayOrderID, final OrderModel order,
			final Map<String, RefundTransactionMappingModel> refundTransactionMap, final ConsignmentModel consignment,
			final AbstractOrderEntryModel orderEntry)
	{
		GetOrderStatusResponse orderStatusResponse = new GetOrderStatusResponse();
		try
		{
			orderStatusResponse = getOrderStatusFromJuspay(juspayOrderID);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		if (null != orderStatusResponse)
		{
			final Refund refund = getRefundEntry(orderStatusResponse, refundTransactionMap.keySet());

			final String refundStatus = checkRefundStatus(orderStatusResponse, refundTransactionMap.keySet());
			if (null == refundStatus)
			{
				if (!checkManualRefund(orderStatusResponse))
				{
					makeRefundUpdateStatus(refund, order, consignment, refundTransactionMap, orderEntry);
				}
				else
				{
					LOG.error("Manual refund present fot the juspay order. Refund retry cancelled for transaction :"
							+ consignment.getCode());
				}
			}
			else if (refundStatus.equals(MarketplacecommerceservicesConstants.SUCCESS))
			{
				updateStatusOnly(refund, order, consignment, refundTransactionMap, orderEntry);
			}
			else if (refundStatus.equals(MarketplacecommerceservicesConstants.PENDING))
			{
				if (!consignment.getStatus().equals(ConsignmentStatus.REFUND_IN_PROGRESS))
				{
					updateStatus(ConsignmentStatus.REFUND_IN_PROGRESS, consignment, order);
				}
			}
		}
	}


	private void updateStatusOnly(final Refund refund, final OrderModel order, final ConsignmentModel consignment,
			final Map<String, RefundTransactionMappingModel> refundTransactionMap, final AbstractOrderEntryModel orderEntry)
	{
		RefundTransactionMappingModel refundTransactionModel = new RefundTransactionMappingModel();

		refundTransactionModel = refundTransactionMap.get(refund.getUniqueRequestId());
		if (null != refundTransactionModel)
		{
			LOG.error("Updating status for Order:" + order.getCode());
			PaymentTransactionType paymentTransactionType = null;
			boolean riskFlag = false;
			PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();
			if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().equals(JuspayRefundType.CANCELLED))
			{
				paymentTransactionType = PaymentTransactionType.CANCEL;
				updateStatus(ConsignmentStatus.ORDER_CANCELLED, consignment, order);

			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().equals(JuspayRefundType.RETURN))
			{
				paymentTransactionType = PaymentTransactionType.RETURN;
				updateStatus(ConsignmentStatus.RETURN_COMPLETED, consignment, order);
			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().equals(JuspayRefundType.CANCELLED_FOR_RISK))
			{
				paymentTransactionType = PaymentTransactionType.CANCEL;
				riskFlag = true;
			}
			else
			{
				final List<OrderHistoryEntryModel> orderHistoryEntryList = order.getHistoryEntries();
				for (final OrderHistoryEntryModel ohe : orderHistoryEntryList)
				{
					if (ohe.getLineId().equals(consignment.getCode()) && ohe.getDescription().equals("CANCELLATION_INITIATED"))
					{
						paymentTransactionType = PaymentTransactionType.CANCEL;
						updateStatus(ConsignmentStatus.ORDER_CANCELLED, consignment, order);
						break;

					}
					else if (ohe.getLineId().equals(consignment.getCode()) && ohe.getDescription().equals("RETURN_INITIATED"))
					{
						paymentTransactionType = PaymentTransactionType.RETURN;
						updateStatus(ConsignmentStatus.ORDER_CANCELLED, consignment, order);
						break;
					}
				}
			}
			final String uniqueSuccessRequestId = getSuccessUniqueRequestId(refund.getUniqueRequestId(), orderEntry.getOrderLineId());

			try
			{
				paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(order,
						MarketplacecommerceservicesConstants.SUCCESS, refundTransactionModel.getRefundAmount(), paymentTransactionType,
						MarketplacecommerceservicesConstants.SUCCESS, uniqueSuccessRequestId);
				mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);
			}
			catch (final Exception e)
			{
				LOG.error("paymentTransactionModel creation failed from updateStatusOnly for order:" + order.getCode() + " "
						+ e.getMessage());
			}

			try
			{
				orderEntry.setJuspayRequestId(uniqueSuccessRequestId);
				modelService.save(orderEntry);
			}
			catch (final Exception e)
			{
				LOG.error("Error while saving orderentry:" + order.getCode(), e);
			}

			if (riskFlag)
			{
				updateOrderStatusCancelledForRisk(order);
			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().equals(JuspayRefundType.CANCELLED))
			{
				updateOrderStatusCancelled(refundTransactionModel.getRefundedOrderEntry(), paymentTransactionModel);
			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().equals(JuspayRefundType.RETURN))
			{
				updateOrderStatusReturn(refundTransactionModel.getRefundedOrderEntry(), paymentTransactionModel);
			}

		}
		else
		{
			LOG.error("RTM entry not present for order:" + order.getCode());
		}
	}


	private boolean makeRefundUpdateStatus(final Refund refund, final OrderModel order, final ConsignmentModel consignment,
			final Map<String, RefundTransactionMappingModel> refundTransactionMap, final AbstractOrderEntryModel orderEntry)
	{
		final boolean returnflag = false;
		double refundAmount = 0.0D;
		String uniqueReqIdOld = null;
		try
		{
			final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
			RefundTransactionMappingModel rtmModel = new RefundTransactionMappingModel();

			if (null != refund && null != refund.getAmount() && null != refund.getUniqueRequestId())
			{
				rtmModel = refundTransactionMap.get(refund.getUniqueRequestId());
				refundAmount = refund.getAmount().doubleValue();
				uniqueReqIdOld = refund.getUniqueRequestId();
			}
			else
			{
				rtmModel = getRefundTransaction(refundTransactionMap);
				refundAmount = rtmModel.getRefundAmount().doubleValue();
				uniqueReqIdOld = rtmModel.getJuspayRefundId();

			}
			JuspayRefundType refundType = null;
			if (null != rtmModel)
			{
				refundType = rtmModel.getRefundType();

				//Default initialization cancel
				String refundReason = JuspayRefundType.CANCELLED.toString();
				PaymentTransactionType paymentTransactionType = PaymentTransactionType.CANCEL;
				boolean cancelledForRisk = false;



				if (null != refundType && refundType.equals(JuspayRefundType.CANCELLED)
						&& rtmModel.getJuspayRefundId().equals(uniqueReqIdOld))
				{
					refundReason = rtmModel.getRefundType().toString();
					paymentTransactionType = PaymentTransactionType.CANCEL;


				}
				else if (null != refundType && refundType.equals(JuspayRefundType.RETURN)
						&& rtmModel.getJuspayRefundId().equals(uniqueReqIdOld))
				{
					refundReason = rtmModel.getRefundType().toString();
					paymentTransactionType = PaymentTransactionType.RETURN;

				}
				else if (null != refundType && refundType.equals(JuspayRefundType.CANCELLED_FOR_RISK)
						&& rtmModel.getJuspayRefundId().equals(uniqueReqIdOld))
				{
					refundReason = rtmModel.getRefundType().toString();
					paymentTransactionType = PaymentTransactionType.CANCEL;
					cancelledForRisk = true;

				}

				PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
				LOG.error("Going to make refund for Order: " + order.getCode());
				try
				{
					createRefundTransactionEntry(orderEntry, uniqueRequestId, refundType, new Double(refundAmount));
					paymentTransactionModel = mplJusPayRefundService.doRefund(order, refundAmount, paymentTransactionType,
							uniqueRequestId);
				}
				catch (final Exception e)
				{
					LOG.error("Refund failed" + e);
				}

				if (null != paymentTransactionModel)
				{
					mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);
				}
				else
				{
					LOG.error("Refund failed");
				}

				//h2refund Added to know the refund type
				try
				{

					if (CollectionUtils.isNotEmpty(rtmModel.getRefundedOrderEntry().getConsignmentEntries()))
					{

						final ConsignmentModel consignmentModel = rtmModel.getRefundedOrderEntry().getConsignmentEntries().iterator()
								.next().getConsignment();
						consignmentModel.setRefundDetails(RefundFomType.AUTOMATIC);
						getModelService().save(consignmentModel);

					}

				}
				catch (final Exception e)
				{
					LOG.error("Refund type consignment updation failed" + e);
				}


				if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), SUCCESS) && cancelledForRisk)
				{
					updateOrderStatusCancelledForRisk(order);
				}
				else if (refundReason.equalsIgnoreCase(JuspayRefundType.CANCELLED.toString())
						&& StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), SUCCESS))
				{
					updateStatus(ConsignmentStatus.ORDER_CANCELLED, consignment, order);
					updateOrderStatusCancelled(rtmModel.getRefundedOrderEntry(), paymentTransactionModel);
				}
				else if (refundReason.equalsIgnoreCase(JuspayRefundType.RETURN.toString())
						&& StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), SUCCESS))
				{
					updateStatus(ConsignmentStatus.RETURN_COMPLETED, consignment, order);
					updateOrderStatusReturn(rtmModel.getRefundedOrderEntry(), paymentTransactionModel);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return returnflag;

	}

	private void updateOrderStatusReturn(final AbstractOrderEntryModel orderEntry,
			final PaymentTransactionModel paymentTransactionModel)
	{
		try
		{

			mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel, orderEntry.getNetAmountAfterAllDisc(),
					ConsignmentStatus.RETURN_COMPLETED, null);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	private void updateOrderStatusCancelled(final AbstractOrderEntryModel orderEntry,
			final PaymentTransactionModel paymentTransactionModel)
	{
		try
		{

			mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel, orderEntry.getNetAmountAfterAllDisc(),
					ConsignmentStatus.ORDER_CANCELLED, null);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	private void updateOrderStatusCancelledForRisk(final OrderModel order)
	{
		try
		{
			orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	private String checkRefundStatus(final GetOrderStatusResponse orderStatusResponse, final Set<String> uniqRequestIDList)
	{
		boolean isPending = false;
		boolean isSuccess = false;
		if (null != orderStatusResponse && CollectionUtils.isNotEmpty(orderStatusResponse.getRefunds()))
		{
			final List<Refund> refundResponseList = orderStatusResponse.getRefunds();
			for (final Refund refundResponse : refundResponseList)
			{
				if (null != refundResponse.getStatus() && null != refundResponse.getUniqueRequestId()
						&& uniqRequestIDList.contains(refundResponse.getUniqueRequestId())
						&& refundResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					isSuccess = true;
					break;
				}
				if (null != refundResponse.getStatus() && null != refundResponse.getUniqueRequestId()
						&& uniqRequestIDList.contains(refundResponse.getUniqueRequestId())
						&& refundResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING))
				{
					isPending = true;
				}
			}
		}
		if (isSuccess)
		{
			return MarketplacecommerceservicesConstants.SUCCESS;
		}
		else if (isPending)
		{
			return MarketplacecommerceservicesConstants.PENDING;
		}
		return null;
	}

	private Refund getRefundEntry(final GetOrderStatusResponse orderStatusResponse, final Set<String> uniqRequestIDList)
	{
		Refund refund = null;
		if (null != orderStatusResponse && CollectionUtils.isNotEmpty(orderStatusResponse.getRefunds()))
		{
			final List<Refund> refundResponseList = orderStatusResponse.getRefunds();
			for (final Refund refundResponse : refundResponseList)
			{
				if (null != refundResponse.getStatus() && null != refundResponse.getUniqueRequestId()
						&& uniqRequestIDList.contains(refundResponse.getUniqueRequestId()))
				{
					refund = refundResponse;
					if (refundResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
					{
						return refundResponse;
					}
				}

			}
		}
		return refund;
	}

	private void updateStatus(final ConsignmentStatus consignmentStatus, final ConsignmentModel consignment, final OrderModel order)
	{
		consignment.setStatus(consignmentStatus);
		consignment.setRefundDetails(RefundFomType.AUTOMATIC);
		modelService.save(consignment);
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(Calendar.getInstance().getTime());
		historyEntry.setOrder(order);
		historyEntry.setLineId(consignment.getCode());
		historyEntry.setDescription(consignmentStatus.toString());
		modelService.save(historyEntry);
	}

	private boolean checkManualRefund(final GetOrderStatusResponse orderStatusResponse)
	{
		try
		{
			final boolean isEnabled = getConfigurationService().getConfiguration().getBoolean(
					MarketplacecommerceservicesConstants.MANUAL_REFUND_CHECK_ENABLED, false);
			if (!isEnabled)
			{
				return false;
			}
			else if (null != orderStatusResponse && CollectionUtils.isNotEmpty(orderStatusResponse.getRefunds()))
			{
				final List<String> juspayRefundIds = new ArrayList<String>();
				for (final Refund refund : orderStatusResponse.getRefunds())
				{
					if (StringUtils.isNotEmpty(refund.getStatus())
							&& (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS) || refund.getStatus()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING)))
					{
						juspayRefundIds.add(refund.getUniqueRequestId());
					}
				}
				if (CollectionUtils.isEmpty(juspayRefundIds))
				{
					return false;
				}
				else
				{
					final List<String> rtmRefundIds = refundClearPerformableDao.fetchRtmRequestIds(juspayRefundIds);
					if (CollectionUtils.isEmpty(rtmRefundIds))
					{
						for (final String juspayRefundId : juspayRefundIds)
						{
							if (!rtmRefundIds.contains(juspayRefundId))
							{
								return true;
							}
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("error while checking for manualrefund " + e.getMessage());
		}
		return false;
	}

	private RefundTransactionMappingModel getRefundTransaction(
			final Map<String, RefundTransactionMappingModel> refundTranscationMap)
	{
		RefundTransactionMappingModel rtm = null;
		for (final String refId : refundTranscationMap.keySet())
		{
			rtm = refundTranscationMap.get(refId);
			if (null != rtm)
			{
				return rtm;
			}
		}
		return rtm;
	}

	private GetOrderStatusResponse getOrderStatusFromJuspay(final String orderId)
	{
		GetOrderStatusResponse orderStatusResponse = null;
		try
		{
			final PaymentService juspayService = new PaymentService();

			juspayService.setBaseUrl(configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.JUSPAYBASEURL));
			juspayService.withKey(
					configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
					.withMerchantId(
							configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

			final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();
			orderStatusRequest.withOrderId(orderId);

			orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);
		}
		catch (final Exception e)
		{
			LOG.error("Exception while fetching order status for juspay order id : " + orderId);
			LOG.error(e.getMessage(), e);
		}

		return orderStatusResponse;
	}

	private void createRefundTransactionEntry(final AbstractOrderEntryModel orderEntry, final String refundId,
			final JuspayRefundType refundType, final Double refundAmount)
	{
		final RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(
				RefundTransactionMappingModel.class);
		refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
		refundTransactionMappingModel.setJuspayRefundId(refundId);
		refundTransactionMappingModel.setCreationtime(new Date());
		refundTransactionMappingModel.setRefundType(refundType);
		refundTransactionMappingModel.setRefundAmount(refundAmount);
		getModelService().save(refundTransactionMappingModel);
	}

	private boolean checkCOD(final OrderModel orderModel)
	{
		boolean isCOD = false;

		if (null != orderModel.getPaymentInfo() && orderModel.getPaymentInfo() instanceof CODPaymentInfoModel)
		{
			isCOD = true;
		}
		return isCOD;
	}

	private String getSuccessUniqueRequestId(final String uniqueRequestId, final String transactionId)
	{
		String requestId = MarketplacecommerceservicesConstants.EMPTY;

		if (StringUtils.isNotEmpty(uniqueRequestId) && StringUtils.isNotEmpty(transactionId))
		{
			requestId = uniqueRequestId + MarketplacecommerceservicesConstants.UNDER_SCORE + transactionId;
		}
		else
		{
			requestId = UUID.randomUUID().toString();
		}
		return requestId;
	}

}

