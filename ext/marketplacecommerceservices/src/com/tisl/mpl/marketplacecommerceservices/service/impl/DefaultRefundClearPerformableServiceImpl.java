/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
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
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.JuspayRefundResponseModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
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

	final Double refundClearTATFinal = new Double(10);


	private static final String REFUND = "REFUND_SUCCESSFUL";
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

		List<OrderModel> orderList = new ArrayList<OrderModel>();
		//final List<OrderModel> doRefundOrderList = new ArrayList<OrderModel>();

		final String refundClearTAT = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.REFUNDCLEAR_SKIPTIME);
		final Double skipPendingOrdersTAT = (null != refundClearTAT ? Double.valueOf(refundClearTAT) : refundClearTATFinal);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -skipPendingOrdersTAT.intValue());
		final Date queryTAT = cal.getTime();
		orderList = refundClearPerformableDao.getRefundClearOrders(queryTAT);
		if (CollectionUtils.isNotEmpty(orderList))
		{
			for (final OrderModel order : orderList)
			{
				final Set consignments = order.getConsignments();
				final ArrayList<String> refundRequestIdList = new ArrayList<String>();
				if (CollectionUtils.isNotEmpty(consignments))
				{
					for (final Iterator iterator = consignments.iterator(); iterator.hasNext();)
					{

						final ConsignmentModel consignment = (ConsignmentModel) iterator.next();

						if (consignment.getStatus().getCode().equals(ConsignmentStatus.REFUND_IN_PROGRESS.getCode())
								|| consignment.getStatus().getCode().equals(ConsignmentStatus.REFUND_INITIATED.getCode()))
						{

							final Set consignmentEntries = consignment.getConsignmentEntries();
							for (final Iterator iteratorC = consignmentEntries.iterator(); iteratorC.hasNext();)
							{
								final ConsignmentEntryModel consignmentEntry = (ConsignmentEntryModel) iteratorC.next();

								refundRequestIdList.add(fetchRefundRequestID(consignmentEntry.getOrderEntry()));
							}

						}
					}
				}

				checkWebhookEntryForRefund(order.getGuid(), order, refundRequestIdList);

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
		final RefundTransactionMappingModel refundTransactionMappingModel = refundClearPerformableDao
				.fetchRefundTransactionByEntry(abstractOrderEntryModel);
		return refundTransactionMappingModel.getJuspayRefundId();
	}



	private void checkWebhookEntryForRefund(final String guid, final OrderModel order, final ArrayList<String> refundRequestIdList)
	{
		List<MplPaymentAuditModel> auditModelList = new ArrayList<MplPaymentAuditModel>();
		auditModelList = refundClearPerformableDao.fetchAuditDataList(guid);

		String juspayOrderID = null;

		if (CollectionUtils.isNotEmpty(auditModelList))
		{
			for (final MplPaymentAuditModel PaymentAuditModel : auditModelList)
			{
				juspayOrderID = PaymentAuditModel.getAuditId();
				fetchPostingStatusTakeAction(juspayOrderID, order, refundRequestIdList);
			}
		}

	}

	/**
	 * @param juspayOrderID
	 * @param order
	 * @param uniqRequestID
	 */
	private void checkStatusMakeRefund(final String juspayOrderID, final OrderModel order, final String uniqRequestID)
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

			for (final Refund refund : refundList)
			{

				if (StringUtils.isNotEmpty(refund.getStatus()) && refund.getStatus().equalsIgnoreCase("SUCCESS")
						&& refund.getUniqueRequestId().equals(uniqRequestID))
				{
					//update order status only
					final RefundTransactionMappingModel refundTransactionModel = fetchRefundTransactioModel(uniqRequestID);
					updateStatusOnly(refundTransactionModel, uniqRequestID, order);
				}
				else if (StringUtils.isNotEmpty(refund.getStatus()) && refund.getStatus().equalsIgnoreCase("FAILURE")
						&& refund.getUniqueRequestId().equals(uniqRequestID))
				{

					//do refund
					//update order status

					makeRefundUpdateStatus(refund, order, juspayOrderID, uniqRequestID);
				}

			}

		}
	}


	/**
	 * @param refundTransactionModel
	 * @param uniqRequestID
	 * @param order
	 */
	private void updateStatusOnly(final RefundTransactionMappingModel refundTransactionModel, final String uniqRequestID,
			final OrderModel order)
	{

		PaymentTransactionType paymentTransactionType = null;
		boolean riskFlag = false;
		PaymentTransactionModel paymentTransactionModel;
		if (null != refundTransactionModel.getRefundType()
				&& refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED.toString()))
		{
			paymentTransactionType = PaymentTransactionType.CANCEL;

		}
		else if (null != refundTransactionModel.getRefundType()
				&& refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.RETURN.toString()))
		{
			paymentTransactionType = PaymentTransactionType.RETURN;
		}
		else if (null != refundTransactionModel.getRefundType()
				&& refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED_FOR_RISK.toString()))
		{
			paymentTransactionType = PaymentTransactionType.CANCEL;
			riskFlag = true;
		}

		paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(order, SUCCESS,
				refundTransactionModel.getRefundAmount(), paymentTransactionType, SUCCESS, uniqRequestID);
		mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);

		if (riskFlag)
		{
			updateOrderStatusCancelledForRisk(order);
		}
		else if (refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED.toString()))
		{
			updateOrderStatusCancelled(order, refundTransactionModel.getRefundedOrderEntry(), paymentTransactionModel);
		}
		else if (refundTransactionModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.RETURN.toString()))
		{
			updateOrderStatusReturn(order, refundTransactionModel.getRefundedOrderEntry(), paymentTransactionModel);
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
			final String oldRequestId = refund.getUniqueRequestId();
			final RefundTransactionMappingModel rtmModel = fetchRefundTransactioModel(oldRequestId);
			if (null != refund.getAmount())
			{
				refundAmount = refund.getAmount().doubleValue();
			}
			else
			{
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
			paymentTransactionModel = mplJusPayRefundService.doRefund(order, refundAmount, paymentTransactionType, uniqueRequestId);
			mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);

			if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "SUCCESS") && cancelledForRisk)
			{
				updateOrderStatusCancelledForRisk(order);
			}
			else if (refundReason.equalsIgnoreCase(JuspayRefundType.CANCELLED.toString())
					&& StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "SUCCESS"))
			{
				updateOrderStatusCancelled(order, rtmModel.getRefundedOrderEntry(), paymentTransactionModel);
			}
			else if (refundReason.equalsIgnoreCase(JuspayRefundType.RETURN.toString())
					&& StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "SUCCESS"))
			{
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
			orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.RETURN_COMPLETED);
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
			orderStatusSpecifier.setOrderStatus(order.getParentReference(), OrderStatus.ORDER_CANCELLED);
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

	/**
	 * @param auditId
	 * @param order
	 * @param refundRequestIdList
	 * @return
	 */
	private void fetchPostingStatusTakeAction(final String auditId, final OrderModel order,
			final ArrayList<String> refundRequestIdList)
	{
		List<JuspayOrderStatusModel> orderStatusList = new ArrayList<JuspayOrderStatusModel>();
		// YTODO Auto-generated method stub
		orderStatusList = refundClearPerformableDao.fetchWebhookTableStatus(auditId);
		final ArrayList<String> juspayOrderStatusRequestIdList = new ArrayList<String>();

		if (CollectionUtils.isNotEmpty(orderStatusList))
		{
			//Fetching the latest entry in JuspayOrderStaus from query
			final List<JuspayRefundResponseModel> RefundResponseList = orderStatusList.get(0).getRefunds();



			for (final JuspayRefundResponseModel juspayRefundResponseModel : RefundResponseList)
			{
				juspayOrderStatusRequestIdList.add(juspayRefundResponseModel.getUniqueRequestId());

				if (StringUtils.isNotEmpty(juspayRefundResponseModel.getStatus())
						&& juspayRefundResponseModel.getStatus().equalsIgnoreCase("SUCCESS"))
				{
					//update order status only
					final RefundTransactionMappingModel refundTransactionModel = fetchRefundTransactioModel(juspayRefundResponseModel
							.getUniqueRequestId());
					updateStatusOnly(refundTransactionModel, juspayRefundResponseModel.getUniqueRequestId(), order);
				}
				else if (StringUtils.isNotEmpty(juspayRefundResponseModel.getStatus())
						&& juspayRefundResponseModel.getStatus().equalsIgnoreCase("PENDING"))
				{
					checkStatusMakeRefund(auditId, order, juspayRefundResponseModel.getUniqueRequestId());
				}
			}

			//Filtering out the not posted refundlist in webhook / JuspayOrderStatus
			if (CollectionUtils.isNotEmpty(refundRequestIdList) && CollectionUtils.isNotEmpty(juspayOrderStatusRequestIdList))
			{
				refundRequestIdList.removeAll(juspayOrderStatusRequestIdList);

				for (final String reqid : refundRequestIdList)
				{
					checkStatusMakeRefund(auditId, order, reqid);
				}

			}

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
