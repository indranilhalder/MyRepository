/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.RefundFomType;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.JuspayRefundResponseModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.Refund;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.RefundClearPerformableDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.RefundClearPerformableService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author 1079689
 *
 */
public class DefaultRefundClearPerformableServiceImpl implements RefundClearPerformableService
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultRefundClearPerformableServiceImpl.class.getName());

	@Autowired
	private RefundClearPerformableDao refundClearPerformableDao;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;


	private ModelService modelService;

	final static Double refundClearTATFinal = new Double(10);

	final static Double refundStartTime = new Double(120);


	//	private static final String REFUND = "REFUND_SUCCESSFUL";
	private static final String SUCCESS = "SUCCESS";



	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		// YTODO Auto-generated method stub
		return refundClearPerformableDao.getCronDetails(code);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void processRefundOrders(final Date lastStartDate)
	{
		// YTODO Auto-generated method stub

		//		final List<OrderModel> orderList = new ArrayList<OrderModel>();
		List<ConsignmentModel> consignmentModelList = new ArrayList<ConsignmentModel>();

		//final List<OrderModel> doRefundOrderList = new ArrayList<OrderModel>();

		final String refundClearTAT = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.REFUNDCLEAR_SKIPTIME);
		final Double skipRefundOrdersTAT = (StringUtils.isNotEmpty(refundClearTAT) ? Double.valueOf(refundClearTAT)
				: refundClearTATFinal);
		final Calendar cal = Calendar.getInstance();
		if (null != lastStartDate)
		{
			cal.setTime(lastStartDate);
		}
		else
		{
			cal.setTime(new Date());
		}

		cal.add(Calendar.MINUTE, -skipRefundOrdersTAT.intValue());
		final Date queryTAT = cal.getTime();

		final String refundOrderFetchStartTime = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.ORDERFETCH_STARTTIME);



		final Double skipPendingOrdersTAT = (StringUtils.isNotEmpty(refundOrderFetchStartTime) ? Double
				.valueOf(refundOrderFetchStartTime) : refundStartTime);
		final Calendar cal_order = Calendar.getInstance();
		cal_order.setTime(new Date());
		cal_order.add(Calendar.DATE, -skipPendingOrdersTAT.intValue());
		final Date queryStartTime = cal_order.getTime();

		consignmentModelList = refundClearPerformableDao.getRefundClearConsignments(queryTAT, queryStartTime);
		if (CollectionUtils.isNotEmpty(consignmentModelList))
		{
			for (final ConsignmentModel consignment : consignmentModelList)
			{
				final OrderModel order = (OrderModel) consignment.getOrder();
				//				final Set consignments = consignment.getConsignments();
				final ArrayList<String> refundRequestIdList = new ArrayList<String>();
				//				if (CollectionUtils.isNotEmpty(consignments))
				//				{
				//					for (final Iterator iterator = consignments.iterator(); iterator.hasNext();)
				//					{

				//						final ConsignmentModel consignment = (ConsignmentModel) iterator.next();

				if (consignment.getStatus().getCode().equals(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())
						|| consignment.getStatus().getCode().equals(ConsignmentStatus.REFUND_INITIATED.getCode()))
				{

					final Set consignmentEntries = consignment.getConsignmentEntries();
					for (final Iterator iteratorC = consignmentEntries.iterator(); iteratorC.hasNext();)
					{
						final ConsignmentEntryModel consignmentEntry = (ConsignmentEntryModel) iteratorC.next();
						final String requestID = fetchRefundRequestID(consignmentEntry.getOrderEntry());
						if (null != requestID)
						{
							refundRequestIdList.add(requestID);
						}
					}

				}
				//}
				//}
				LOG.error("Going to process the Order NO:" + order.getCode());
				checkWebhookEntryForRefund(order, refundRequestIdList, consignment);

			}
		}

	}

	/**
	 * @param abstractOrderEntryModel
	 * @return
	 */
	private String fetchRefundRequestID(final AbstractOrderEntryModel abstractOrderEntryModel)
	{
		// YTODO Auto-generated method stub
		String reqid = null;
		try
		{
			final RefundTransactionMappingModel refundTransactionMappingModel = refundClearPerformableDao
					.fetchRefundTransactionByEntry(abstractOrderEntryModel);

			reqid = refundTransactionMappingModel.getJuspayRefundId();
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return reqid;
	}

	private void checkWebhookEntryForRefund(final OrderModel order, final ArrayList<String> refundRequestIdList,
			final ConsignmentModel consignment)
	{
		String juspayOrderID = null;
		for (final PaymentTransactionModel paymentTransaction : order.getPaymentTransactions())
		{

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
			fetchPostingStatusTakeAction(juspayOrderID, order, refundRequestIdList, consignment);
		}

	}

	/**
	 * @param juspayOrderID
	 * @param order
	 * @param uniqRequestID
	 */
	private void checkStatusMakeRefund(final String juspayOrderID, final OrderModel order, final String uniqRequestID,
			final ConsignmentModel consignment)
	{
		// YTODO Auto-generated method stub

		final PaymentService juspayService = new PaymentService();
		GetOrderStatusResponse orderStatusResponse = new GetOrderStatusResponse();
		List<Refund> refundList = new ArrayList<Refund>();
		try
		{
			juspayService.setBaseUrl(getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.JUSPAYBASEURL));
			juspayService
					.withKey(
							getConfigurationService().getConfiguration().getString(
									MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY)).withMerchantId(
							getConfigurationService().getConfiguration()
									.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

			final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();

			orderStatusRequest.withOrderId(juspayOrderID);

			orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}

		if (CollectionUtils.isNotEmpty(orderStatusResponse.getRefunds()))
		{

			refundList = orderStatusResponse.getRefunds();
			boolean checkJuspayFlag = false;

			for (final Refund refund : refundList)
			{
				// Check the request ID reached to Juspay Or not
				if (refund.getUniqueRequestId().equalsIgnoreCase(uniqRequestID))
				{
					checkJuspayFlag = true;
				}

				if (StringUtils.isNotEmpty(refund.getStatus()) && refund.getStatus().equalsIgnoreCase(SUCCESS)
						&& refund.getUniqueRequestId().equalsIgnoreCase(uniqRequestID))
				{
					//update order status only
					final RefundTransactionMappingModel refundTransactionModel = fetchRefundTransactioModel(uniqRequestID);
					updateStatusOnly(refundTransactionModel, uniqRequestID, order, consignment);
				}
				else if (StringUtils.isNotEmpty(refund.getStatus()) && refund.getStatus().equalsIgnoreCase("FAILURE")
						&& refund.getUniqueRequestId().equalsIgnoreCase(uniqRequestID))
				{
					//do refund
					//update order status
					makeRefundUpdateStatus(refund, order, juspayOrderID, uniqRequestID);
				}

			}
			// if the request ID not in juspay it means we have to request juspay again for making refund for that req ID present in RTM
			if (!checkJuspayFlag)
			{
				makeRefundUpdateStatus(null, order, juspayOrderID, uniqRequestID);
			}

		}//If there is one one return request and that was not reached to Juspay then refund response is blank
		else
		{
			makeRefundUpdateStatus(null, order, juspayOrderID, uniqRequestID);
		}
	}


	/**
	 * @param refundTransactionModel
	 * @param uniqRequestID
	 * @param order
	 */
	private void updateStatusOnly(final RefundTransactionMappingModel refundTransactionModel, final String uniqRequestID,
			final OrderModel order, final ConsignmentModel consignment)
	{
		if (null != refundTransactionModel)
		{


			LOG.error("Updating status for Order:" + order.getCode());
			PaymentTransactionType paymentTransactionType = null;
			boolean riskFlag = false;
			PaymentTransactionModel paymentTransactionModel = new PaymentTransactionModel();
			if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED.toString()))
			{
				paymentTransactionType = PaymentTransactionType.CANCEL;
				orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);

			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.RETURN.toString()))
			{
				paymentTransactionType = PaymentTransactionType.RETURN;
				orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.RETURN_COMPLETED);
			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().toString()
							.equalsIgnoreCase(JuspayRefundType.CANCELLED_FOR_RISK.toString()))
			{
				paymentTransactionType = PaymentTransactionType.CANCEL;
				riskFlag = true;
			}
			else
			{
				final List<OrderHistoryEntryModel> orderHistoryEntryList = order.getHistoryEntries();
				for (final OrderHistoryEntryModel ohe : orderHistoryEntryList)
				{
					if (ohe.getLineId().equals(consignment.getCode()) && ohe.getDescription().contains("CANCEL"))
					{
						paymentTransactionType = PaymentTransactionType.CANCEL;
						updateOrderStatusCancelled(order);

					}
					else
					{
						paymentTransactionType = PaymentTransactionType.RETURN;
						updateOrderStatusReturn(order);

					}
				}
			}

			try
			{
				paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(order, SUCCESS,
						refundTransactionModel.getRefundAmount(), paymentTransactionType, SUCCESS, uniqRequestID);
				mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);
			}
			catch (final Exception e)
			{
				// YTODO Auto-generated catch block
				LOG.error("paymentTransactionModel creation failed from updateStatusOnly for order:" + order.getCode(), e);
			}


			if (riskFlag)
			{
				updateOrderStatusCancelledForRisk(order);
			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED.toString()))
			{
				updateOrderStatusCancelled(order, refundTransactionModel.getRefundedOrderEntry(), paymentTransactionModel);
			}
			else if (null != refundTransactionModel.getRefundType()
					&& refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.RETURN.toString()))
			{
				updateOrderStatusReturn(order, refundTransactionModel.getRefundedOrderEntry(), paymentTransactionModel);
			}


		}
		else
		{
			LOG.error("RTM entry not present for order:" + order.getCode());
		}
	}

	/**
	 * @param refund
	 * @param order
	 * @param uniqRequestID
	 */
	private boolean makeRefundUpdateStatus(final Refund refund, final OrderModel order, final String auditID,
			final String uniqRequestOldID)
	{
		final boolean returnflag = false;
		double refundAmount = 0.0D;
		try
		{
			final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
			RefundTransactionMappingModel rtmModel = new RefundTransactionMappingModel();

			if (null != refund && null != refund.getAmount())
			{
				rtmModel = fetchRefundTransactioModel(refund.getUniqueRequestId());
				refundAmount = refund.getAmount().doubleValue();
			}
			else
			{
				rtmModel = fetchRefundTransactioModel(uniqRequestOldID);
				refundAmount = rtmModel.getRefundAmount().doubleValue();
			}


			//Default initialization cancel
			String refundReason = JuspayRefundType.CANCELLED.toString();
			PaymentTransactionType paymentTransactionType = PaymentTransactionType.CANCEL;
			boolean cancelledForRisk = false;



			if (null != rtmModel.getRefundType()
					&& rtmModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED.toString())
					&& rtmModel.getJuspayRefundId().equals(uniqRequestOldID))
			{
				refundReason = rtmModel.getRefundType().toString();
				paymentTransactionType = PaymentTransactionType.CANCEL;


			}
			else if (null != rtmModel.getRefundType()
					&& rtmModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.RETURN.toString())
					&& rtmModel.getJuspayRefundId().equals(uniqRequestOldID))
			{
				refundReason = rtmModel.getRefundType().toString();
				paymentTransactionType = PaymentTransactionType.RETURN;

			}
			else if (null != rtmModel.getRefundType()
					&& rtmModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED_FOR_RISK.toString())
					&& rtmModel.getJuspayRefundId().equals(uniqRequestOldID))
			{
				refundReason = rtmModel.getRefundType().toString();
				paymentTransactionType = PaymentTransactionType.CANCEL;
				cancelledForRisk = true;

			}

			PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
			LOG.error("Going to make refund for Order: " + order.getCode());
			try
			{
				paymentTransactionModel = mplJusPayRefundService.doRefund(order, refundAmount, paymentTransactionType,
						uniqueRequestId);
			}
			catch (final Exception e)
			{
				orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.REFUND_IN_PROGRESS);
				LOG.error("Refund failed" + e);
			}

			if (null != paymentTransactionModel)
			{
				mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);
			}
			else
			{
				orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.REFUND_IN_PROGRESS);
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
				orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);
				updateOrderStatusCancelled(order, rtmModel.getRefundedOrderEntry(), paymentTransactionModel);
			}
			else if (refundReason.equalsIgnoreCase(JuspayRefundType.RETURN.toString())
					&& StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), SUCCESS))
			{
				orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.RETURN_COMPLETED);
				updateOrderStatusReturn(order, rtmModel.getRefundedOrderEntry(), paymentTransactionModel);
			}
			else if (!StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), SUCCESS))
			{
				orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.REFUND_IN_PROGRESS);
				updateOrderStatusReturn(order, rtmModel.getRefundedOrderEntry(), paymentTransactionModel);
			}

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return returnflag;

	}

	/**
	 * @param oldRequestId
	 * @return
	 */
	private RefundTransactionMappingModel fetchRefundTransactioModel(final String oldRequestId)
	{
		final List<RefundTransactionMappingModel> results = refundClearPerformableDao.fetchRefundTransactionMapping(oldRequestId);
		RefundTransactionMappingModel rtmModelInstance = new RefundTransactionMappingModel();
		if (CollectionUtils.isNotEmpty(results))
		{
			for (final RefundTransactionMappingModel rtmModel : results)
			{
				if (rtmModel.getJuspayRefundId().equals(oldRequestId))
				{
					rtmModelInstance = rtmModel;
				}
			}
		}

		return rtmModelInstance;
	}


	/**
	 * @param order
	 * @param paymentTransactionModel
	 * @param abstractOrderEntryModel
	 */
	private void updateOrderStatusReturn(final OrderModel order, final AbstractOrderEntryModel orderEntry,
			final PaymentTransactionModel paymentTransactionModel)
	{
		try
		{
			// YTODO Auto-generated method stub
			//orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.RETURN_COMPLETED);
			mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel, orderEntry.getNetAmountAfterAllDisc(),
					ConsignmentStatus.RETURN_COMPLETED, null);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * @param order
	 * @param orderEntry
	 * @param paymentTransactionModel
	 */
	private void updateOrderStatusCancelled(final OrderModel order, final AbstractOrderEntryModel orderEntry,
			final PaymentTransactionModel paymentTransactionModel)
	{
		try
		{
			// YTODO Auto-generated method stub
			//orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);
			mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel, orderEntry.getNetAmountAfterAllDisc(),
					ConsignmentStatus.ORDER_CANCELLED, null);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * @param order
	 */
	private void updateOrderStatusCancelledForRisk(final OrderModel order)
	{
		// YTODO Auto-generated method stub
		try
		{
			orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	private void updateOrderStatusCancelled(final OrderModel order)
	{
		// YTODO Auto-generated method stub
		try
		{
			orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	private void updateOrderStatusReturn(final OrderModel order)
	{
		// YTODO Auto-generated method stub
		try
		{
			orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * @param auditId
	 * @param order
	 * @param refundRequestIdList
	 * @return
	 */
	private void fetchPostingStatusTakeAction(final String auditId, final OrderModel order,
			final ArrayList<String> refundRequestIdList, final ConsignmentModel consignment)
	{
		List<JuspayOrderStatusModel> orderStatusList = new ArrayList<JuspayOrderStatusModel>();
		// YTODO Auto-generated method stub
		orderStatusList = refundClearPerformableDao.fetchWebhookTableStatus(auditId);
		final ArrayList<String> juspayOrderStatusRequestIdList = new ArrayList<String>();
		try
		{


			if (CollectionUtils.isNotEmpty(orderStatusList))
			{
				//Fetching the latest entry in JuspayOrderStaus from query
				final List<JuspayRefundResponseModel> RefundResponseList = orderStatusList.get(0).getRefunds();



				for (final JuspayRefundResponseModel juspayRefundResponseModel : RefundResponseList)
				{
					juspayOrderStatusRequestIdList.add(juspayRefundResponseModel.getUniqueRequestId());

					if (StringUtils.isNotEmpty(juspayRefundResponseModel.getStatus())
							&& juspayRefundResponseModel.getStatus().equalsIgnoreCase(SUCCESS))
					{
						//update order status only
						final RefundTransactionMappingModel refundTransactionModel = fetchRefundTransactioModel(juspayRefundResponseModel
								.getUniqueRequestId());

						if (null != refundTransactionModel)
						{

							updateStatusOnly(refundTransactionModel, juspayRefundResponseModel.getUniqueRequestId(), order, consignment);
						}

					}
					else if (StringUtils.isNotEmpty(juspayRefundResponseModel.getStatus())
							&& juspayRefundResponseModel.getStatus().equalsIgnoreCase("PENDING"))
					{
						LOG.error("PENDING post from juspay, chekstatus and making refund for order: " + order.getCode());
						checkStatusMakeRefund(auditId, order, juspayRefundResponseModel.getUniqueRequestId(), consignment);
					}
				}
			}

			//Filtering out the not posted refundlist in webhook / JuspayOrderStatus
			if (CollectionUtils.isNotEmpty(refundRequestIdList) && CollectionUtils.isNotEmpty(juspayOrderStatusRequestIdList))
			{
				refundRequestIdList.removeAll(juspayOrderStatusRequestIdList);
				if (CollectionUtils.isNotEmpty(refundRequestIdList))
				{
					for (final String reqid : refundRequestIdList)
					{
						checkStatusMakeRefund(auditId, order, reqid, consignment);
					}
				}
			}
			//If the number of return request is only one and that too not posted by juspay
			else if (CollectionUtils.isNotEmpty(refundRequestIdList) && CollectionUtils.isEmpty(juspayOrderStatusRequestIdList))
			{
				for (final String reqid : refundRequestIdList)
				{
					checkStatusMakeRefund(auditId, order, reqid, consignment);
				}
			}

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}


	}

	@Override
	public void saveCronDetails(final Date startTime, final String code)
	{
		final MplConfigurationModel oModel = refundClearPerformableDao.getCronDetails(code);
		if (null != oModel && null != oModel.getMplConfigCode())
		{
			LOG.debug("Saving CronJob Run Time :" + startTime);
			oModel.setMplConfigDate(startTime);
			getModelService().save(oModel);
			LOG.debug("Cron Job Details Saved for Code :" + code);
		}
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}



}
