/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.FPCRefundMethod;
import com.tisl.mpl.core.enums.FPCRefundStatus;
import com.tisl.mpl.core.enums.FPCRefundType;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.model.FPCRefundEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.Refund;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.ForwardPaymentCleanUpDao;
import com.tisl.mpl.marketplacecommerceservices.service.ForwardPaymentCleanUpService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
public class ForwardPaymentCleanUpServiceImpl implements ForwardPaymentCleanUpService
{

	@Autowired
	ForwardPaymentCleanUpDao forwardPaymentCleanUpDao;

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	OrderStatusSpecifier orderStatusSpecifier;

	@Autowired
	ModelService modelService;

	private final static Logger LOG = Logger.getLogger(ForwardPaymentCleanUpServiceImpl.class.getName());

	@Override
	public List<OrderModel> fetchOrdersWithMultiplePayments(final Date startTime, final Date endTime)
	{
		return forwardPaymentCleanUpDao.fetchOrdersWithMultiplePayments(startTime, endTime);
	}

	@Override
	public List<OrderModel> fetchPaymentFailedOrders(final Date startTime, final Date endTime)
	{
		return forwardPaymentCleanUpDao.fetchPaymentFailedOrders(startTime, endTime);
	}

	@Override
	public List<OrderModel> fetchRmsFailedOrders(final Date startTime, final Date endTime)
	{
		return forwardPaymentCleanUpDao.fetchRmsFailedOrders(startTime, endTime);
	}

	@Override
	public List<MplPaymentAuditModel> fetchAuditsWithoutOrder(final Date startTime, final Date endTime)
	{
		return forwardPaymentCleanUpDao.fetchAuditsWithoutOrder(startTime, endTime);
	}


	@Override
	public List<FPCRefundEntryModel> fetchSpecificRefundEntries(final String expiredFlag)
	{
		return forwardPaymentCleanUpDao.fetchSpecificRefundEntries(expiredFlag);
	}


	@Override
	public MplConfigurationModel fetchConfigDetails(final String code)
	{
		return forwardPaymentCleanUpDao.fetchConfigDetails(code);
	}

	@Override
	public void createRefundEntryForMultiplePayments(final OrderModel orderModel)
	{
		if (!checkCOD(orderModel))
		{
			final List<MplPaymentAuditModel> paymentAuditList = forwardPaymentCleanUpDao.fetchAuditsForGUID(orderModel.getGuid());

			if (paymentAuditList.size() > 1)
			{
				LOG.debug("Found multiple audits for order: " + orderModel.getCode());
				final List<PaymentTransactionModel> paymentTransactionList = orderModel.getPaymentTransactions();

				String firstAuditId = null;
				for (final PaymentTransactionModel paymentTransaction : paymentTransactionList)
				{
					if (null != firstAuditId)
					{
						break;
					}
					for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
				 	{
						if ((PaymentTransactionType.CAPTURE.equals(paymentTransactionEntry.getType()) || PaymentTransactionType.AUTHORIZATION
								.equals(paymentTransactionEntry.getType()))
								&& ("success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus()) || "ACCEPTED"
										.equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus())) &&  (null != paymentTransactionEntry.getType() 
										&& !paymentTransactionEntry.getType().equals(PaymentTransactionType.QC_CAPTURE)) )
						{
							firstAuditId = paymentTransactionEntry.getRequestToken();
							break;
						}
					}
				}
				if (null != firstAuditId)
				{
					LOG.debug("Valid audit ID for the order: " + firstAuditId);

					for (final MplPaymentAuditModel paymentAudit : paymentAuditList)
					{
						try
						{
							if (!StringUtils.equalsIgnoreCase(paymentAudit.getAuditId(), firstAuditId))
							{
								if (!refundEntryExists(paymentAudit.getAuditId()))
								{
									final FPCRefundEntryModel refundEntry = modelService.create(FPCRefundEntryModel.class);
									refundEntry.setAuditId(paymentAudit.getAuditId());
									refundEntry.setAudit(paymentAudit);
									refundEntry.setCartGUID(paymentAudit.getCartGUID());
									refundEntry.setOrder(orderModel);
									refundEntry.setParentAuditId(firstAuditId);
									refundEntry.setRefundStatus(FPCRefundStatus.CREATED);
									refundEntry.setRefundType(FPCRefundType.DUPLICATE_PAYMENT);
									refundEntry.setIsExpired(Boolean.FALSE);
									modelService.save(refundEntry);
									LOG.debug("RefundEntry created for the audit :" + paymentAudit.getAuditId());
								}
							}
						}
						catch (final Exception e)
						{
							LOG.error("Error while creating refund entry for audit: " + paymentAudit.getAuditId());
							LOG.error(e.getMessage(), e);
						}
					}
				}
			}
			else
			{
				LOG.debug("No valid audit id found for the order: " + orderModel.getCode());
			}
		}
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

	@Override
	public void createRefundEntryForFailedOrders(final OrderModel orderModel)
	{
		if (!checkCOD(orderModel))
		{
			final OrderStatus orderStatus = orderModel.getStatus();
			final List<MplPaymentAuditModel> auditList = forwardPaymentCleanUpDao.fetchAuditsForGUID(orderModel.getGuid());
			if (CollectionUtils.isNotEmpty(auditList))
			{
				for (final MplPaymentAuditModel paymentAudit : auditList)
				{
					try
					{
						if (null != orderStatus)
						{
							if (orderStatus.equals(OrderStatus.PAYMENT_TIMEOUT))
							{
								if (!refundEntryExists(paymentAudit.getAuditId()))
								{
									final FPCRefundEntryModel refundEntry = modelService.create(FPCRefundEntryModel.class);
									refundEntry.setAuditId(paymentAudit.getAuditId());
									refundEntry.setAudit(paymentAudit);
									refundEntry.setCartGUID(paymentAudit.getCartGUID());
									refundEntry.setOrder(orderModel);
									refundEntry.setRefundStatus(FPCRefundStatus.CREATED);
									refundEntry.setRefundType(FPCRefundType.PAYMENT_TIMEOUT);
									refundEntry.setIsExpired(Boolean.FALSE);
									modelService.save(refundEntry);
									LOG.debug("RefundEntry created for the audit :" + paymentAudit.getAuditId());
								}
								else
								{
									LOG.error("Refund entry already exists for audit :" + paymentAudit.getAuditId());
								}
							}
							else if (orderStatus.equals(OrderStatus.PAYMENT_FAILED))
							{
								if (!refundEntryExists(paymentAudit.getAuditId()))
								{
									final FPCRefundEntryModel refundEntry = modelService.create(FPCRefundEntryModel.class);
									refundEntry.setAuditId(paymentAudit.getAuditId());
									refundEntry.setAudit(paymentAudit);
									refundEntry.setCartGUID(paymentAudit.getCartGUID());
									refundEntry.setOrder(orderModel);
									refundEntry.setRefundStatus(FPCRefundStatus.CREATED);
									refundEntry.setRefundType(FPCRefundType.PAYMENT_FAILED);
									refundEntry.setIsExpired(Boolean.FALSE);
									modelService.save(refundEntry);
									LOG.debug("RefundEntry created for the audit :" + paymentAudit.getAuditId());
								}
								else
								{
									LOG.error("Refund entry already exists for audit :" + paymentAudit.getAuditId());
								}
							}
						}
					}
					catch (final Exception e)
					{
						LOG.error("Error while creating refund entry for audit: " + paymentAudit.getAuditId());
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}

	}

	@Override
	public void createRefundEntryForRmsFailedOrders(final OrderModel orderModel)
	{
		if (!checkCOD(orderModel))
		{
			final OrderStatus orderStatus = orderModel.getStatus();
			final List<MplPaymentAuditModel> auditList = forwardPaymentCleanUpDao.fetchAuditsForGUID(orderModel.getGuid());
			if (CollectionUtils.isNotEmpty(auditList))
			{
				for (final MplPaymentAuditModel paymentAudit : auditList)
				{
					try
					{
						if (null != orderStatus && orderStatus.equals(OrderStatus.RMS_VERIFICATION_FAILED))
						{
							if (!refundEntryExists(paymentAudit.getAuditId()))
							{
								final FPCRefundEntryModel refundEntry = modelService.create(FPCRefundEntryModel.class);
								refundEntry.setAuditId(paymentAudit.getAuditId());
								refundEntry.setAudit(paymentAudit);
								refundEntry.setCartGUID(paymentAudit.getCartGUID());
								refundEntry.setOrder(orderModel);
								refundEntry.setRefundStatus(FPCRefundStatus.CREATED);
								refundEntry.setRefundType(FPCRefundType.RMS_VERIFICATION_FAILED);
								refundEntry.setIsExpired(Boolean.FALSE);
								modelService.save(refundEntry);
								LOG.debug("RefundEntry created for the audit :" + paymentAudit.getAuditId());
							}
							else
							{
								LOG.error("Refund entry already exists for audit :" + paymentAudit.getAuditId());
							}
						}
					}
					catch (final Exception e)
					{
						LOG.error("Error while creating refund entry for audit: " + paymentAudit.getAuditId());
						LOG.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	@Override
	public void createRefundEntryForAuditsWithoutOrder(final MplPaymentAuditModel auditModel)
	{
		try
		{
			if (!refundEntryExists(auditModel.getAuditId()))
			{
				final FPCRefundEntryModel refundEntry = modelService.create(FPCRefundEntryModel.class);
				refundEntry.setAuditId(auditModel.getAuditId());
				refundEntry.setAudit(auditModel);
				refundEntry.setCartGUID(auditModel.getCartGUID());
				refundEntry.setRefundStatus(FPCRefundStatus.CREATED);
				refundEntry.setRefundType(FPCRefundType.ORDER_NOT_GENERATED);
				refundEntry.setIsExpired(Boolean.FALSE);
				modelService.save(refundEntry);
				LOG.debug("RefundEntry created for the audit :" + auditModel.getAuditId());
			}
			else
			{
				LOG.error("Refund entry already exists for audit :" + auditModel.getAuditId());
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error while creating refund entry for audit: " + auditModel.getAuditId());
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void processRefund(final FPCRefundEntryModel refundEntry)
	{
		final FPCRefundType refundType = refundEntry.getRefundType();
		if (refundEntry.getRefundStatus().equals(FPCRefundStatus.CREATED))
		{
			refundEntry.setRefundStatus(FPCRefundStatus.PENDING);
		}
		try
		{
			if (null != refundType)
			{
				if (refundType.equals(FPCRefundType.PAYMENT_FAILED) || refundType.equals(FPCRefundType.PAYMENT_TIMEOUT)
						|| refundType.equals(FPCRefundType.ORDER_NOT_GENERATED))
				{
					final GetOrderStatusResponse orderStatusResponse = getOrderStatusFromJuspay(refundEntry.getAuditId());
					if (null != orderStatusResponse)
					{
						if (orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
						{
							final String refundResponseStatus = checkRefundStatus(orderStatusResponse);
							if (null == refundResponseStatus)
							{
								if (refundType.equals(FPCRefundType.ORDER_NOT_GENERATED))
								{
									initiateOrderRefundAudit(orderStatusResponse, refundEntry.getAudit());
								}
								else
								{
									initiateOrderRefund(orderStatusResponse, refundEntry.getAudit());
								}
								refundEntry.setRefundStatus(FPCRefundStatus.REFUND_INITIATED);
							}
							else if (refundResponseStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
							{
								refundEntry.setRefundStatus(FPCRefundStatus.REFUND_SUCCESSFUL);
								final String refundMethod = checkRefundMethod(orderStatusResponse);
								if (refundMethod.equals(MarketplacecommerceservicesConstants.AUTOMATIC))
								{
									refundEntry.setRefundMethod(FPCRefundMethod.AUTOMATIC);
								}
								else if (refundMethod.equals(MarketplacecommerceservicesConstants.MANUAL))
								{
									refundEntry.setRefundMethod(FPCRefundMethod.MANUAL);
								}
								expireRefundEntry(refundEntry);
							}
							else if (refundResponseStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING))
							{
								refundEntry.setRefundStatus(FPCRefundStatus.REFUND_IN_PROGRESS);
							}
						}
						else
						{
							refundEntry.setRefundStatus(FPCRefundStatus.NOT_APPLICABLE);
							expireRefundEntry(refundEntry);
						}
					}
				}
				else if (refundType.equals(FPCRefundType.DUPLICATE_PAYMENT))
				{
					if (null != refundEntry.getParentAuditId())
					{
						final GetOrderStatusResponse parentOrderStatusResponse = getOrderStatusFromJuspay(refundEntry
								.getParentAuditId());
						if (parentOrderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
						{
							final String parentRefundStatus = checkRefundStatus(parentOrderStatusResponse);
							if (null == parentRefundStatus)
							{
								final GetOrderStatusResponse orderStatusResponse = getOrderStatusFromJuspay(refundEntry.getAuditId());
								if (orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
								{
									final String refundResponseStatus = checkRefundStatus(orderStatusResponse);
									if (null == refundResponseStatus)
									{
										initiateOrderRefund(orderStatusResponse, refundEntry.getAudit());
										refundEntry.setRefundStatus(FPCRefundStatus.REFUND_INITIATED);
									}
									else if (refundResponseStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
									{
										refundEntry.setRefundStatus(FPCRefundStatus.REFUND_SUCCESSFUL);
										final String refundMethod = checkRefundMethod(orderStatusResponse);
										if (refundMethod.equals(MarketplacecommerceservicesConstants.AUTOMATIC))
										{
											refundEntry.setRefundMethod(FPCRefundMethod.AUTOMATIC);
										}
										else if (refundMethod.equals(MarketplacecommerceservicesConstants.MANUAL))
										{
											refundEntry.setRefundMethod(FPCRefundMethod.MANUAL);
										}
										expireRefundEntry(refundEntry);
									}
									else if (refundResponseStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING))
									{
										refundEntry.setRefundStatus(FPCRefundStatus.REFUND_IN_PROGRESS);

									}
								}
								else
								{
									refundEntry.setRefundStatus(FPCRefundStatus.NOT_APPLICABLE);
									expireRefundEntry(refundEntry);
								}
							}
							else
							{
								refundEntry.setRefundStatus(FPCRefundStatus.NOT_APPLICABLE);
								expireRefundEntry(refundEntry);
							}
						}
						else
						{
							refundEntry.setRefundStatus(FPCRefundStatus.NOT_APPLICABLE);
							expireRefundEntry(refundEntry);
							LOG.error("Parent audit id not charged : " + refundEntry.getParentAuditId());
						}
					}
				}
				else if (refundType.equals(FPCRefundType.RMS_VERIFICATION_FAILED))
				{
					final GetOrderStatusResponse orderStatusResponse = getOrderStatusFromJuspay(refundEntry.getAuditId());
					if (null != orderStatusResponse)
					{
						if (orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
						{
							final String refundResponseStatus = checkRefundStatus(orderStatusResponse);
							if (null == refundResponseStatus)
							{
								final OrderModel order = refundEntry.getOrder();
								if (null != order)
								{
									callRMSVerficationFailed(refundEntry.getOrder());
									refundEntry.setRefundStatus(FPCRefundStatus.REFUND_INITIATED);
								}
							}
							else if (refundResponseStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
							{
								final OrderModel order = refundEntry.getOrder();
								if (null != order)
								{
									orderStatusSpecifier.setOrderStatus(order, OrderStatus.ORDER_CANCELLED);
								}
								refundEntry.setRefundStatus(FPCRefundStatus.REFUND_SUCCESSFUL);
								final String refundMethod = checkRefundMethod(orderStatusResponse);
								if (refundMethod.equals(MarketplacecommerceservicesConstants.AUTOMATIC))
								{
									refundEntry.setRefundMethod(FPCRefundMethod.AUTOMATIC);
								}
								else if (refundMethod.equals(MarketplacecommerceservicesConstants.MANUAL))
								{
									refundEntry.setRefundMethod(FPCRefundMethod.MANUAL);
								}
								expireRefundEntry(refundEntry);
							}
							else if (refundResponseStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING))
							{
								refundEntry.setRefundStatus(FPCRefundStatus.REFUND_IN_PROGRESS);
							}
						}
						else
						{
							refundEntry.setRefundStatus(FPCRefundStatus.NOT_APPLICABLE);
							expireRefundEntry(refundEntry);
						}
					}
				}
			}
			else
			{
				expireRefundEntry(refundEntry);
			}
			modelService.save(refundEntry);
		}
		catch (final Exception e)
		{
			modelService.save(refundEntry);
			LOG.error("Error while processing refund for audit: " + refundEntry.getAuditId());
			LOG.error(e.getMessage(), e);
		}
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

	private void initiateOrderRefund(final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditModel paymentAudit)
	{
		try
		{
			if (null != orderStatusResponse.getPaymentMethodType()
					&& orderStatusResponse.getPaymentMethodType().equalsIgnoreCase(
							MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
			{
				mplJusPayRefundService.doRefund(paymentAudit.getAuditId(), orderStatusResponse.getPaymentMethodType());
			}
			else if (StringUtils.isNotEmpty(orderStatusResponse.getBankEmi())
					&& StringUtils.isNotEmpty(orderStatusResponse.getBankTenure()))
			{
				mplJusPayRefundService.doRefund(paymentAudit.getAuditId(), MarketplacecommerceservicesConstants.EMI);
			}
			else
			{
				mplJusPayRefundService.doRefund(paymentAudit.getAuditId(), orderStatusResponse.getCardResponse().getCardType());
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	private void initiateOrderRefundAudit(final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditModel paymentAudit)
	{
		try
		{
			if (null != orderStatusResponse.getPaymentMethodType()
					&& orderStatusResponse.getPaymentMethodType().equalsIgnoreCase(
							MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
			{
				mplJusPayRefundService.doRefundAudit(paymentAudit.getAuditId(), orderStatusResponse.getPaymentMethodType());
			}
			else if (StringUtils.isNotEmpty(orderStatusResponse.getBankEmi())
					&& StringUtils.isNotEmpty(orderStatusResponse.getBankTenure()))
			{
				mplJusPayRefundService.doRefundAudit(paymentAudit.getAuditId(), MarketplacecommerceservicesConstants.EMI);
			}
			else
			{
				mplJusPayRefundService.doRefundAudit(paymentAudit.getAuditId(), orderStatusResponse.getCardResponse().getCardType());
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	private boolean refundEntryExists(final String auditId)
	{
		final FPCRefundEntryModel refundEntry = forwardPaymentCleanUpDao.fetchRefundEntryForAuditId(auditId);
		if (null != refundEntry)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private String checkRefundStatus(final GetOrderStatusResponse orderStatusResponse)
	{
		boolean isPending = false;
		boolean isSuccess = false;
		if (null != orderStatusResponse && CollectionUtils.isNotEmpty(orderStatusResponse.getRefunds()))
		{
			final List<Refund> refundResponseList = orderStatusResponse.getRefunds();
			for (final Refund refundResponse : refundResponseList)
			{
				if (null != refundResponse.getStatus()
						&& refundResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					isSuccess = true;
					break;
				}
				else if (null != refundResponse.getStatus()
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

	private String checkRefundMethod(final GetOrderStatusResponse orderStatusResponse)
	{

		if (null != orderStatusResponse && CollectionUtils.isNotEmpty(orderStatusResponse.getRefunds()))
		{
			final List<Refund> refundResponseList = orderStatusResponse.getRefunds();
			for (final Refund refundResponse : refundResponseList)
			{
				if (null != refundResponse.getStatus()
						&& refundResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					if (StringUtils.isNotEmpty(refundResponse.getUniqueRequestId()))
					{
						return MarketplacecommerceservicesConstants.AUTOMATIC;
					}
					else
					{
						return MarketplacecommerceservicesConstants.MANUAL;
					}
				}
			}
		}
		return MarketplacecommerceservicesConstants.MANUAL;
	}

	@Override
	public void expireRefundEntry(final FPCRefundEntryModel refundEntry)
	{
		refundEntry.setIsExpired(Boolean.TRUE);
		modelService.save(refundEntry);
	}

	private void callRMSVerficationFailed(final OrderModel orderModel)
	{

		String defaultPinCode = "".intern();
		if (null != orderModel.getDeliveryAddress() && StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPostalcode()))
		{
			defaultPinCode = orderModel.getDeliveryAddress().getPostalcode();
		}
		else
		{
			defaultPinCode = MarketplacecommerceservicesConstants.PINCODE;
		}
		if (StringUtils.isNotEmpty(defaultPinCode))
		{
			final PaymentTransactionModel paymentTransactionModel = initiateRefundRms(orderModel);

			if (null != paymentTransactionModel && StringUtils.isNotEmpty(paymentTransactionModel.getCode()))
			{
				final String status = paymentTransactionModel.getStatus();
				if (StringUtils.isNotEmpty(status) && status.equalsIgnoreCase("SUCCESS"))
				{
					orderStatusSpecifier.setOrderStatus(orderModel, OrderStatus.ORDER_CANCELLED);
				}
			}


		}

	}

	private PaymentTransactionModel initiateRefundRms(final OrderModel order)
	{
		PaymentTransactionModel paymentTransactionModel = null;
		final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
		try
		{
			paymentTransactionModel = mplJusPayRefundService.doRefund(order, order.getTotalPriceWithConv().doubleValue(),
					PaymentTransactionType.CANCEL, uniqueRequestId);
			if (null != paymentTransactionModel)
			{
				mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);
			}
			else
			{
				LOG.error("Failed to Refund");
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(order, "FAILURE",
					order.getTotalPriceWithConv(), PaymentTransactionType.CANCEL, "FAILURE", uniqueRequestId);
			mplJusPayRefundService.attachPaymentTransactionModel(order, paymentTransactionModel);

			// TISSIT-1784 Code addition started
			if (CollectionUtils.isNotEmpty(order.getChildOrders()))
			{
				for (final OrderModel subOrderModel : order.getChildOrders())
				{
					if (subOrderModel != null && CollectionUtils.isNotEmpty(subOrderModel.getEntries()))
					{
						for (final AbstractOrderEntryModel subOrderEntryModel : subOrderModel.getEntries())
						{
							if (subOrderEntryModel != null)
							{
								double refundAmount = 0D;
								double deliveryCost = 0D;
								if (subOrderEntryModel.getCurrDelCharge() != null)
								{
									deliveryCost = subOrderEntryModel.getCurrDelCharge().doubleValue();
								}

								refundAmount = subOrderEntryModel.getNetAmountAfterAllDisc().doubleValue() + deliveryCost;
								refundAmount = mplJusPayRefundService.validateRefundAmount(refundAmount, subOrderModel);

								final RefundTransactionMappingModel refundTransactionMappingModel = modelService
										.create(RefundTransactionMappingModel.class);
								refundTransactionMappingModel.setRefundedOrderEntry(subOrderEntryModel);
								refundTransactionMappingModel.setJuspayRefundId(uniqueRequestId);
								refundTransactionMappingModel.setCreationtime(new Date());
								refundTransactionMappingModel.setRefundType(JuspayRefundType.CANCELLED_FOR_RISK);
								refundTransactionMappingModel.setRefundAmount(new Double(refundAmount));//TISPRO-216 : Refund amount Set in RTM
								modelService.save(refundTransactionMappingModel);
							}
						}
					}
				}
			}
		}
		return paymentTransactionModel;
	}

	@Override
	public List<OrderModel> fetchCliqCashOrdersWithMultiplePayments(Date startTime, Date endTime)
	{
		return forwardPaymentCleanUpDao.fetchCliqCashOrdersWithMultiplePayments(startTime, endTime);

	}

}
