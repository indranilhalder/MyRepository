/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.JuspayRefundResponseModel;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.JuspayWebHookDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayWebHookService;
import com.tisl.mpl.marketplacecommerceservices.service.MplFraudModelService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
public class DefaultJuspayWebHookServiceImpl implements JuspayWebHookService
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultJuspayWebHookServiceImpl.class.getName());

	private JuspayWebHookDao juspayWebHookDao;

	private ModelService modelService;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private MplPaymentService mplPaymentService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;

	@Autowired
	private MplFraudModelService mplFraudModelService;

	@Autowired
	private MplPaymentDao mplPaymentDao;

	private static final String REFUND = "REFUND_SUCCESSFUL";
	private static final String REFUND_FAIL = "REFUND_UNSUCCESSFUL";

	/**
	 * @Description : Fetch Web Hook Data
	 */
	@Override
	public void fetchWebHookData()
	{
		List<JuspayWebhookModel> webHookDetailList = new ArrayList<JuspayWebhookModel>();
		webHookDetailList = juspayWebHookDao.fetchWebHookData();
		if (null != webHookDetailList && !webHookDetailList.isEmpty())
		{
			validateWebHookData(webHookDetailList);
		}
	}

	/**
	 * @Description : Fetch From Audit based on Web Hook Details
	 * @param webHookDetailList
	 */
	private void validateWebHookData(final List<JuspayWebhookModel> webHookDetailList)
	{
		if (CollectionUtils.isNotEmpty(webHookDetailList))
		{
			final List<JuspayWebhookModel> uniqueList = new ArrayList<JuspayWebhookModel>();
			for (final JuspayWebhookModel oModel : webHookDetailList)
			{
				if (null != oModel.getOrderStatus() && oModel.getIsExpired().booleanValue())
				{
					//getting all the webhook data where isExpired is Y and adding into a list
					uniqueList.add(oModel);
				}
			}
			for (final JuspayWebhookModel hook : webHookDetailList)
			{
				if (CollectionUtils.isNotEmpty(uniqueList))
				{
					//iterating through the new list against the whole webhook data list
					boolean duplicateFound = false;
					for (final JuspayWebhookModel unique : uniqueList)
					{
						//if there is duplicate order id which is not expired(N) then setting it to Y
						if (unique.getOrderStatus().getOrderId().equalsIgnoreCase(hook.getOrderStatus().getOrderId())
								&& hook.getEventName().equalsIgnoreCase("ORDER_SUCCEEDED"))
						{
							duplicateFound = true;
							break;
						}
					}

					if (duplicateFound)
					{
						hook.setIsExpired(Boolean.TRUE);
						getModelService().save(hook);
					}
					else
					{
						processWebhook(hook, webHookDetailList);

						//TISSIT-1811:the processed hook in the uniqueList
						uniqueList.add(hook);
					}
				}
				else
				{
					processWebhook(hook, webHookDetailList);

					//TISSIT-1811:the processed hook in the uniqueList
					uniqueList.add(hook);
				}
			}
		}
	}


	/**
	 * WebHook Data Processing
	 *
	 * @param hook
	 * @param webHookDetailList
	 */
	private void processWebhook(final JuspayWebhookModel hook, final List<JuspayWebhookModel> webHookDetailList)
	{
		if (hook.getOrderStatus().getRefunds().isEmpty())
		{
			//For Positive Flow
			getResponseBasedOnStatus(hook, hook.getOrderStatus().getOrderId(), hook.getOrderStatus().getStatus());
		}
		//For Refund Flow
		else
		{
			//Action for refund scenarios
			performActionForRefund(webHookDetailList);
			//Processed in Webhook
			updateWebHookExpired(hook);
		}
	}

	/**
	 * This method performs all the functions based on combinations of scenarios for Refund
	 *
	 * @param webHookList
	 */
	private void performActionForRefund(final List<JuspayWebhookModel> webHookList)
	{
		boolean isError = false;
		final List<String> errorList = new ArrayList<String>();

		for (final JuspayWebhookModel hook : webHookList)
		{
			for (final JuspayRefundResponseModel refund : hook.getOrderStatus().getRefunds())
			{
				try
				{
					if (null == refund.getUniqueRequestId())
					{
						isError = true;
						errorList.add(hook.getOrderStatus().getOrderId());
						for (final RefundTransactionMappingModel rtmModel : results)
						{
							if (!rtmModel.getIsProcessed().booleanValue())
							{
								//fetching audit records
								final MplPaymentAuditModel auditModel = juspayWebHookDao.fetchAuditData(hook.getOrderStatus()
										.getOrderId());
								final OrderModel parentOrder = juspayWebHookDao.fetchParentOrder(auditModel.getCartGUID());

								//TISSIT-1802
								OrderModel subOrder = modelService.create(OrderModel.class);
								if (null != rtmModel.getRefundedOrderEntry() && null != rtmModel.getRefundedOrderEntry().getOrder())
								{
									subOrder = (OrderModel) rtmModel.getRefundedOrderEntry().getOrder();
								}

								if (null != subOrder && null != rtmModel.getRefundType()
										&& rtmModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED.toString()))
								{
									//Change status of Consignment against order line when it is SUCCESS at Juspay & CANCELLED in RTM
									changeConsignmentStatusForCancelled(rtmModel, refund, subOrder, hook.getOrderStatus().getOrderId());
								}
								else if (null != subOrder && null != rtmModel.getRefundType()
										&& rtmModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.RETURN.toString()))
								{
									//Change status of Consignment against order line when it is SUCCESS at Juspay & RETURN in RTM
									changeConsignmentStatusForReturn(rtmModel, refund, subOrder, hook.getOrderStatus().getOrderId());
								}
								else if (null != subOrder && null != rtmModel.getRefundType()
										&& rtmModel.getRefundType().equals(JuspayRefundType.CANCELLED_FOR_RISK))
								{
									//Change status of Consignment against parent order when it is SUCCESS at Juspay & CANCELLED_FOR_RISK in RTM
									changeConsignmentStatusForCancelledForRisk(rtmModel, refund, parentOrder, hook.getOrderStatus()
											.getOrderId());
								}
							}
						}
					}
					else
					{
						//fetching the refund transaction mapping records
						final List<RefundTransactionMappingModel> results = juspayWebHookDao.fetchRefundTransactionMapping(refund
								.getUniqueRequestId());

						if (!results.isEmpty())
						{
							for (final RefundTransactionMappingModel rtmModel : results)
							{
								if (!rtmModel.getIsProcessed().booleanValue())
								{
									//fetching audit records
									final MplPaymentAuditModel auditModel = juspayWebHookDao.fetchAuditData(hook.getOrderStatus()
											.getOrderId());
									final OrderModel parentOrder = juspayWebHookDao.fetchParentOrder(auditModel.getCartGUID());

									//TISSIT-1802
									OrderModel subOrder = modelService.create(OrderModel.class);
									if (null != rtmModel.getRefundedOrderEntry())
									{
										if (null != rtmModel.getRefundedOrderEntry().getOrder())
										{
											subOrder = (OrderModel) rtmModel.getRefundedOrderEntry().getOrder();
										}
									}

									if (null != subOrder && null != rtmModel.getRefundType()
											&& rtmModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.CANCELLED.toString()))
									{
										//Change status of Consignment against order line when it is SUCCESS at Juspay & CANCELLED in RTM
										changeConsignmentStatusForCancelled(rtmModel, refund, subOrder, hook.getOrderStatus().getOrderId());
									}
									else if (null != subOrder && null != rtmModel.getRefundType()
											&& rtmModel.getRefundType().toString().equalsIgnoreCase(JuspayRefundType.RETURN.toString()))
									{
										//Change status of Consignment against order line when it is SUCCESS at Juspay & RETURN in RTM
										changeConsignmentStatusForReturn(rtmModel, refund, subOrder, hook.getOrderStatus().getOrderId());
									}
									else if (null != subOrder && null != rtmModel.getRefundType()
											&& rtmModel.getRefundType().equals(JuspayRefundType.CANCELLED_FOR_RISK))
									{
										//Change status of Consignment against parent order when it is SUCCESS at Juspay & CANCELLED_FOR_RISK in RTM
										changeConsignmentStatusForCancelledForRisk(rtmModel, refund, parentOrder, hook.getOrderStatus()
												.getOrderId());
									}
								}
							}
						}
						else
						{
							//						final MplPaymentAuditModel audit = getModelService().create(MplPaymentAuditModel.class);
							//						audit.setAuditId(hook.getOrderStatus().getOrderId());
							//						final MplPaymentAuditModel mplPaymentAuditModel = getFlexibleSearchService().getModelByExample(audit);

							//Fetch data from Audit table against unique request id for Refund
							final MplPaymentAuditModel audit = juspayWebHookDao.fetchAuditData(hook.getOrderStatus().getOrderId());
							Collection<MplPaymentAuditEntryModel> collection = audit.getAuditEntries();
							final List<MplPaymentAuditEntryModel> entryList = new ArrayList<MplPaymentAuditEntryModel>();
							if (null == collection || collection.isEmpty())
							{
								collection = new ArrayList<MplPaymentAuditEntryModel>();
							}

							entryList.addAll(collection);

							//final List<MplPaymentAuditEntryModel> entryList = mplPaymentAuditModel.getAuditEntries();
							final MplPaymentAuditEntryModel entry = entryList.get(entryList.size() - 1);



							if (refund.getUniqueRequestId().equalsIgnoreCase(entry.getRefundReqId()))
							{
								if (entry.getStatus().equals(MplPaymentAuditStatusEnum.REFUND_INITIATED)
										&& refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
								{
									final MplPaymentAuditEntryModel mplPaymentAuditEntryModel = modelService
											.create(MplPaymentAuditEntryModel.class);

									final PaymentTransactionModel paymentTransactionModel = modelService
											.create(PaymentTransactionModel.class);
									final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService
											.create(PaymentTransactionEntryModel.class);
									final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
									BigDecimal bigAmount = null;

									mplPaymentAuditEntryModel.setAuditId(audit.getAuditId());
									mplPaymentAuditEntryModel.setResponseDate(new Date());
									paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS);
									paymentTransactionModel.setCode(entry.getRefundReqId()
											+ MarketplacecommerceservicesConstants.UNDER_SCORE + new Date().getSeconds());

									paymentTransactionEntryModel.setCode(entry.getRefundReqId()
											+ MarketplacecommerceservicesConstants.UNDER_SCORE + new Date().getSeconds());
									bigAmount = new BigDecimal(refund.getAmount().doubleValue(), MathContext.DECIMAL64);
									paymentTransactionEntryModel.setAmount(bigAmount);
									paymentTransactionEntryModel.setTime(new Date());
									//paymentTransactionEntryModel.setCurrency(orderModel.getPaymentTransactions().get(0).getEntries().get(0).getCurrency());
									paymentTransactionEntryModel.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS);
									paymentTransactionEntryModel.setTransactionStatusDetails(MarketplacecommerceservicesConstants.SUCCESS);
									paymentTransactionEntryModel.setType(PaymentTransactionType.REFUND_STANDALONE);

									//TISPRO-130
									PaymentTypeModel paymentTypeModel = getModelService().create(PaymentTypeModel.class);
									if (null != hook.getOrderStatus().getPaymentMethodType()
											&& hook.getOrderStatus().getPaymentMethodType()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
									{
										paymentTypeModel = getPaymentModeDetails(hook.getOrderStatus().getPaymentMethodType());
										setPaymentModeInTransaction(paymentTypeModel, paymentTransactionEntryModel);
									}
									else
									{
										paymentTypeModel = getPaymentModeDetails(hook.getOrderStatus().getCardResponse().getCardType());

										setPaymentModeInTransaction(paymentTypeModel, paymentTransactionEntryModel);
									}

									modelService.save(paymentTransactionEntryModel);

									entries.add(paymentTransactionEntryModel);
									paymentTransactionModel.setEntries(entries);
									modelService.save(paymentTransactionModel);

									mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL);

									//setting Payment transaction against auditentry in case where we dont have the order
									mplPaymentAuditEntryModel.setPaymentTransaction(paymentTransactionModel);

									//setting the unique request id of the refund request sent to Juspay in Audit entry
									mplPaymentAuditEntryModel.setRefundReqId(refund.getUniqueRequestId());

									entryList.add(mplPaymentAuditEntryModel);
									audit.setAuditEntries(entryList);
									modelService.save(audit);

								}
								else if (entry.getStatus().equals(MplPaymentAuditStatusEnum.REFUND_INITIATED)
										&& refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.FAILURE))
								{
									final MplPaymentAuditEntryModel mplPaymentAuditEntryModel = modelService
											.create(MplPaymentAuditEntryModel.class);

									final PaymentTransactionModel paymentTransactionModel = modelService
											.create(PaymentTransactionModel.class);
									final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService
											.create(PaymentTransactionEntryModel.class);
									final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
									BigDecimal bigAmount = null;

									mplPaymentAuditEntryModel.setAuditId(audit.getAuditId());
									mplPaymentAuditEntryModel.setResponseDate(new Date());
									paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.FAILURE);
									paymentTransactionModel.setCode(entry.getRefundReqId()
											+ MarketplacecommerceservicesConstants.UNDER_SCORE + new Date().getSeconds());

									paymentTransactionEntryModel.setCode(entry.getRefundReqId()
											+ MarketplacecommerceservicesConstants.UNDER_SCORE + new Date().getSeconds());
									bigAmount = new BigDecimal(refund.getAmount().doubleValue(), MathContext.DECIMAL64);
									paymentTransactionEntryModel.setAmount(bigAmount);
									paymentTransactionEntryModel.setTime(new Date());
									//paymentTransactionEntryModel.setCurrency(orderModel.getPaymentTransactions().get(0).getEntries().get(0).getCurrency());
									paymentTransactionEntryModel.setTransactionStatus(MarketplacecommerceservicesConstants.FAILURE);
									paymentTransactionEntryModel.setTransactionStatusDetails(MarketplacecommerceservicesConstants.FAILURE);
									paymentTransactionEntryModel.setType(PaymentTransactionType.REFUND_STANDALONE);

									//								final PaymentTypeModel paymentTypeModel = getPaymentModeDetails(hook.getOrderStatus().getCardResponse()
									//										.getCardType());
									//								setPaymentModeInTransaction(paymentTypeModel, paymentTransactionEntryModel);
									//

									//TISPRO-130
									PaymentTypeModel paymentTypeModel = getModelService().create(PaymentTypeModel.class);
									if (null != hook.getOrderStatus().getPaymentMethodType()
											&& hook.getOrderStatus().getPaymentMethodType()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
									{
										paymentTypeModel = getPaymentModeDetails(hook.getOrderStatus().getPaymentMethodType());
										setPaymentModeInTransaction(paymentTypeModel, paymentTransactionEntryModel);
									}
									else
									{
										paymentTypeModel = getPaymentModeDetails(hook.getOrderStatus().getCardResponse().getCardType());
										setPaymentModeInTransaction(paymentTypeModel, paymentTransactionEntryModel);
									}
									modelService.save(paymentTransactionEntryModel);

									entries.add(paymentTransactionEntryModel);
									paymentTransactionModel.setEntries(entries);
									modelService.save(paymentTransactionModel);

									mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL);

									//setting Payment transaction against auditentry in case where we dont have the order
									mplPaymentAuditEntryModel.setPaymentTransaction(paymentTransactionModel);

									//setting the unique request id of the refund request sent to Juspay in Audit entry
									mplPaymentAuditEntryModel.setRefundReqId(refund.getUniqueRequestId());

									entryList.add(mplPaymentAuditEntryModel);
									audit.setAuditEntries(entryList);
									modelService.save(audit);

								}
								else if (entry.getStatus().equals(MplPaymentAuditStatusEnum.REFUND_INITIATED)
										&& refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING))
								{
									break;
								}
								else
								{
									if (entry.getStatus().equals(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL))
									{
										//Set is Expired Y in webhook table
										updateWebHookExpired(hook);
									}
									else if (entry.getStatus().equals(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL))
									{
										//Set is Expired Y in webhook table
										updateWebHookExpired(hook);
									}

								}
							}
						}

					}
				}
				catch (final ModelSavingException e)
				{
					LOG.error(e.getMessage(), e);
				}
				catch (final EtailNonBusinessExceptions e)
				{
					LOG.error(e.getMessage(), e);
				}
			}
		}

		if (isError)
		{
			final Exception e = new Exception();
			for (final String data : errorList)
			{
				LOG.error("Unique request ID missing for the following Juspay Order ID " + data, e);
			}

			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0021);
		}
	}

	/**
	 *
	 * @param paymentTypeModel
	 * @param paymentTransactionEntryModel
	 */
	private void setPaymentModeInTransaction(final PaymentTypeModel paymentTypeModel,
			final PaymentTransactionEntryModel paymentTransactionEntryModel)
	{
		if (null != paymentTypeModel)
		{
			paymentTransactionEntryModel.setPaymentMode(paymentTypeModel);
		}
	}


	/**
	 * The Method is used to fetch the Payment Mode Details available in PG
	 *
	 *
	 * @param paymentType
	 */
	private PaymentTypeModel getPaymentModeDetails(final String paymentType)
	{
		PaymentTypeModel oModel = modelService.create(PaymentTypeModel.class);
		String paymentMode = MarketplacecommerceservicesConstants.EMPTY;
		if (StringUtils.isNotEmpty(paymentType))
		{
			if (paymentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.CARD_TYPE_CREDIT))
			{
				paymentMode = MarketplacecommerceservicesConstants.CREDIT;
				oModel = mplPaymentDao.getPaymentMode(paymentMode);
			}
			else if (paymentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.CARD_TYPE_DEBIT))
			{
				paymentMode = MarketplacecommerceservicesConstants.DEBIT;

				oModel = mplPaymentDao.getPaymentMode(paymentMode);
			}
			//TISPRO-130
			else if (paymentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
			{
				paymentMode = MarketplacecommerceservicesConstants.NETBANKING;

				oModel = mplPaymentDao.getPaymentMode(paymentMode);
			}
		}
		return oModel;
	}


	/**
	 * To change the consignment status against order entry line when the status is CANCELLED in RTM
	 *
	 * @param rtmModel
	 * @param refund
	 * @param order
	 * @param juspayOrderId
	 */
	private void changeConsignmentStatusForCancelled(final RefundTransactionMappingModel rtmModel,
			final JuspayRefundResponseModel refund, final OrderModel order, final String juspayOrderId)
	{
		try
		{
			if (null != rtmModel.getJuspayRefundId() && null != rtmModel.getRefundedOrderEntry()
					&& null != rtmModel.getRefundedOrderEntry().getTransactionID())
			{
				ConsignmentStatus newStatus = null;
				final Double refundAmount = rtmModel.getRefundAmount() != null ? rtmModel.getRefundAmount() : NumberUtils.DOUBLE_ZERO;
				PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);

				final String uniqueRequestId = getWebhookUniqueRequestId(rtmModel);

				if (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					paymentTransactionModel = getMplJusPayRefundService().createPaymentTransactionModel(order,
							MarketplacecommerceservicesConstants.SUCCESS, refundAmount, PaymentTransactionType.CANCEL, REFUND,
							uniqueRequestId);//TISPRO-216 : Refund Amt Picked from RTM
					newStatus = ConsignmentStatus.ORDER_CANCELLED;
				}
				else if (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.FAILURE))
				{
					paymentTransactionModel = getMplJusPayRefundService().createPaymentTransactionModel(order,
							MarketplacecommerceservicesConstants.FAILURE, refundAmount, PaymentTransactionType.CANCEL, REFUND_FAIL,
							uniqueRequestId);//TISPRO-216 : Refund Amt Picked from RTM
					newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
				}

				if (newStatus != null)
				{
					getMplJusPayRefundService().attachPaymentTransactionModel(order, paymentTransactionModel);

					//RTM processed set
					rtmModel.setIsProcessed(Boolean.TRUE);
					getModelService().save(rtmModel);

					final AbstractOrderEntryModel orderEntryModel = rtmModel.getRefundedOrderEntry();

					//Start TISPRD-871
					if (newStatus.equals(ConsignmentStatus.ORDER_CANCELLED))
					{
						orderEntryModel.setJuspayRequestId(uniqueRequestId);
						getModelService().save(orderEntryModel);
					}
					//End TISPRD-871

					LOG.info(" >> Calling OMS with  status :" + newStatus + " for refund amount " + refund.getAmount()
							+ " for order line id : " + orderEntryModel.getOrderLineId());
					mplJusPayRefundService.makeRefundOMSCall(orderEntryModel, paymentTransactionModel, refund.getAmount(), newStatus);

					//Update in Audit table with new status
					updateInAudit(juspayOrderId, refund.getStatus(), getWebhookUniqueRequestId(rtmModel), paymentTransactionModel);
				}
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(e.getMessage(), e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * To change the consignment status against order entry line when the status is RETURN in RTM
	 *
	 * @param rtmModel
	 * @param refund
	 * @param order
	 * @param juspayOrderId
	 */
	private void changeConsignmentStatusForReturn(final RefundTransactionMappingModel rtmModel,
			final JuspayRefundResponseModel refund, final OrderModel order, final String juspayOrderId)
	{
		try
		{
			if (null != rtmModel.getJuspayRefundId() && null != rtmModel.getRefundedOrderEntry()
					&& null != rtmModel.getRefundedOrderEntry().getTransactionID())
			{
				final Double refundAmount = rtmModel.getRefundAmount() != null ? rtmModel.getRefundAmount() : NumberUtils.DOUBLE_ZERO;
				PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);
				ConsignmentStatus newStatus = null;

				final String uniqueRequestId = getWebhookUniqueRequestId(rtmModel);

				if (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					paymentTransactionModel = getMplJusPayRefundService().createPaymentTransactionModel(order,
							MarketplacecommerceservicesConstants.SUCCESS, refundAmount, PaymentTransactionType.RETURN, REFUND,
							uniqueRequestId);//TISPRO-216 : Refund Amt Picked from RTM

					newStatus = ConsignmentStatus.RETURN_COMPLETED;
				}
				else if (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.FAILURE))
				{
					paymentTransactionModel = getMplJusPayRefundService().createPaymentTransactionModel(order,
							MarketplacecommerceservicesConstants.FAILURE, refundAmount, PaymentTransactionType.RETURN, REFUND_FAIL,
							uniqueRequestId);//TISPRO-216 : Refund Amt Picked from RTM
					newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
				}

				if (newStatus != null)
				{
					getMplJusPayRefundService().attachPaymentTransactionModel(order, paymentTransactionModel);

					//RTM processed set
					rtmModel.setIsProcessed(Boolean.TRUE);
					getModelService().save(rtmModel);

					final AbstractOrderEntryModel orderEntryModel = rtmModel.getRefundedOrderEntry();

					//Start TISPRD-871
					if (newStatus.equals(ConsignmentStatus.RETURN_COMPLETED))
					{
						orderEntryModel.setJuspayRequestId(uniqueRequestId);
						getModelService().save(orderEntryModel);
					}
					//End TISPRD-871
					//TO update the status to OMS
					LOG.debug(" >> Calling OMS with  status :" + newStatus + " for refund amount " + refund.getAmount()
							+ " for order line id : " + orderEntryModel.getOrderLineId());

					mplJusPayRefundService.makeRefundOMSCall(orderEntryModel, paymentTransactionModel, refund.getAmount(), newStatus);

					//Update in Audit table with new status
					updateInAudit(juspayOrderId, refund.getStatus(), getWebhookUniqueRequestId(rtmModel), paymentTransactionModel);
				}
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error(e.getMessage(), e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * To set the request id
	 *
	 * @param rtmModel
	 * @return String
	 */
	private String getWebhookUniqueRequestId(final RefundTransactionMappingModel rtmModel)
	{
		String requestId = MarketplacecommerceservicesConstants.EMPTY;

		if (rtmModel != null && rtmModel.getJuspayRefundId() != null && rtmModel.getRefundedOrderEntry() != null
				&& rtmModel.getRefundedOrderEntry().getTransactionID() != null)
		{
			requestId = rtmModel.getJuspayRefundId() + MarketplacecommerceservicesConstants.UNDER_SCORE
					+ rtmModel.getRefundedOrderEntry().getTransactionID();
		}
		else
		{
			requestId = UUID.randomUUID().toString();
		}
		return requestId;
	}

	/**
	 * To change the consignment status against order entry line when the status is CANCELLED in RTM
	 *
	 * @param rtmModel
	 * @param refund
	 * @param order
	 * @param juspayOrderId
	 */
	private void changeConsignmentStatusForCancelledForRisk(final RefundTransactionMappingModel rtmModel,
			final JuspayRefundResponseModel refund, final OrderModel order, final String juspayOrderId)
	{
		if (!refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.PENDING)
				&& null != rtmModel.getJuspayRefundId() && null != rtmModel.getRefundedOrderEntry()
				&& null != rtmModel.getRefundedOrderEntry().getTransactionID())
		{
			try
			{
				boolean notPending = false;
				final Double refundAmount = rtmModel.getRefundAmount() != null ? rtmModel.getRefundAmount() : NumberUtils.DOUBLE_ZERO;
				PaymentTransactionModel paymentTransactionModel = getModelService().create(PaymentTransactionModel.class);

				final String uniqueRequestId = getWebhookUniqueRequestId(rtmModel);

				if (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
				{
					//Order status update
					orderStatusSpecifier.setOrderStatus(order, OrderStatus.ORDER_CANCELLED);
					paymentTransactionModel = getMplJusPayRefundService().createPaymentTransactionModel(order,
							MarketplacecommerceservicesConstants.SUCCESS, refundAmount, PaymentTransactionType.CANCEL, REFUND,
							uniqueRequestId);//TISPRO-216 : Refund Amt Picked from RTM
					notPending = true;
				}
				else if (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.FAILURE))
				{
					//Order status update
					orderStatusSpecifier.setOrderStatus(order, OrderStatus.REFUND_IN_PROGRESS);

					paymentTransactionModel = getMplJusPayRefundService().createPaymentTransactionModel(order,
							MarketplacecommerceservicesConstants.FAILURE, refundAmount, PaymentTransactionType.CANCEL, REFUND_FAIL,
							uniqueRequestId);//TISPRO-216 : Refund Amt Picked from RTM

					notPending = true;
				}
				if (notPending)
				{
					getMplJusPayRefundService().attachPaymentTransactionModel(order, paymentTransactionModel);

					//RTM processed set
					rtmModel.setIsProcessed(Boolean.TRUE);
					getModelService().save(rtmModel);

					//Start TISPRD-871
					if (refund.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
					{
						final AbstractOrderEntryModel orderEntryModel = rtmModel.getRefundedOrderEntry();
						orderEntryModel.setJuspayRequestId(uniqueRequestId);
						getModelService().save(orderEntryModel);
					}
					//End TISPRD-871

					//Update in Audit table with new status
					updateInAudit(juspayOrderId, refund.getStatus(), getWebhookUniqueRequestId(rtmModel), paymentTransactionModel);
				}
			}

			catch (final ModelSavingException e)
			{
				LOG.error(e.getMessage(), e);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
	}


	/**
	 * @param oModel
	 * @Description : Validate Juspay response and Audit Data
	 * @param orderId
	 * @param status
	 */
	private void getResponseBasedOnStatus(final JuspayWebhookModel oModel, final String orderId, final String status)
	{
		try
		{
			//For Normal Forward Payment Flow
			if (status.equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED))
			{
				final MplPaymentAuditModel auditDataModel = juspayWebHookDao.fetchAuditData(orderId);

				if (null != auditDataModel && CollectionUtils.isNotEmpty(auditDataModel.getAuditEntries()))
				{
					//check parent order in Commerce exists or not
					final String guid = auditDataModel.getCartGUID();

					boolean isParentOrder = false;
					if (StringUtils.isNotEmpty(guid))
					{
						isParentOrder = checkParentOrderExists(guid);
						if (!isParentOrder)
						{
							try
							{
								//TISPRO-130
								if (null != oModel.getOrderStatus().getPaymentMethodType()
										&& oModel.getOrderStatus().getPaymentMethodType()
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
								{
									//calling refund service where there will be cart only for NB
									getMplJusPayRefundService().doRefund(auditDataModel.getAuditId(),
											oModel.getOrderStatus().getPaymentMethodType());
								}
								else
								{
									//calling refund service where there will be cart only for CARD
									getMplJusPayRefundService().doRefund(auditDataModel.getAuditId(),
											oModel.getOrderStatus().getCardResponse().getCardType());
								}

							}
							catch (final Exception e)
							{
								LOG.error(e.getMessage(), e);
							}

							LOG.debug(MarketplacecommerceservicesConstants.WEBHOOKUPDATEMSG);
							updateWebHookExpired(oModel);
						}
						else
						{
							updateWebHookExpired(oModel);
						}
					}
				}
			}

		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
		}
	}

	/**
	 * To check whether there is a parent order created against which Payment took place
	 *
	 * @param orderGuid
	 * @return boolean
	 */
	private boolean checkParentOrderExists(final String orderGuid)
	{
		final List<OrderModel> orders = getJuspayWebHookDao().fetchOrder(orderGuid);

		if (!CollectionUtils.isEmpty(orders))
		{
			int subOrderCount = 0;
			//int parentOrderCount = 0;
			for (final OrderModel orderModel : orders)
			{
				if (StringUtils.isNotEmpty(orderModel.getType()))
				{
					if (orderModel.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUBORDER)
							&& null != orderModel.getParentReference())
					{
						subOrderCount++;
					}
					else if (orderModel.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.PARENTORDER)
							&& null != orderModel.getChildOrders() && !CollectionUtils.isEmpty(orderModel.getChildOrders()))
					{
						continue;
					}
				}

			}

			return subOrderCount > 0;
		}
		else
		{
			return false;
		}
	}

	/**
	 * @Description : Update Web Hook Entry
	 * @param oModel
	 */
	private void updateWebHookExpired(final JuspayWebhookModel oModel)
	{
		if (null != oModel)
		{
			oModel.setIsExpired(Boolean.TRUE);
			modelService.save(oModel);
		}
	}

	/**
	 * Method to update in Audit table against every unique request id
	 *
	 * @param orderId
	 * @param type
	 * @param uniqueReqId
	 * @param transaction
	 *
	 */
	private void updateInAudit(final String orderId, final String type, final String uniqueReqId,
			final PaymentTransactionModel transaction)
	{
		//fetching audit records
		final MplPaymentAuditModel audit = juspayWebHookDao.fetchAuditData(orderId);

		Collection<MplPaymentAuditEntryModel> collection = audit.getAuditEntries();
		final List<MplPaymentAuditEntryModel> entries = new ArrayList<MplPaymentAuditEntryModel>();
		if (null == collection || collection.isEmpty())
		{
			collection = new ArrayList<MplPaymentAuditEntryModel>();
		}

		entries.addAll(collection);

		//final List<MplPaymentAuditEntryModel> entries = new ArrayList<MplPaymentAuditEntryModel>(audit.getAuditEntries());
		if (!CollectionUtils.isEmpty(entries))
		{
			final MplPaymentAuditEntryModel entry = getModelService().create(MplPaymentAuditEntryModel.class);
			entry.setAuditId(orderId);
			entry.setResponseDate(new Date());
			entry.setRefundReqId(uniqueReqId);
			entry.setPaymentTransaction(transaction);
			if (type.equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS))
			{
				entry.setStatus(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL);
			}
			else
			{
				entry.setStatus(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL);
			}

			getModelService().save(entry);
			entries.add(entry);
			audit.setAuditEntries(entries);
			getModelService().save(audit);
		}
	}


	/**
	 * @Description : Save Cron Job Details
	 * @param code
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		return juspayWebHookDao.getCronDetails(code);
	}

	/**
	 * @Description : Save CronJob Configuration Details
	 * @param: startTime
	 * @param: code
	 */
	@Override
	public void saveCronDetails(final Date startTime, final String code)
	{
		final MplConfigurationModel oModel = juspayWebHookDao.getCronDetails(code);
		if (null != oModel && null != oModel.getMplConfigCode())
		{
			LOG.debug("Saving CronJob Run Time :" + startTime);
			oModel.setMplConfigDate(startTime);
			getModelService().save(oModel);
			LOG.debug("Cron Job Details Saved for Code :" + code);
		}
	}

	/**
	 * @Description : Fetch Web Hook Data
	 * @param: mplConfigDate
	 * @param:startTime
	 */
	@Override
	public void fetchSpecificWebHookData(final Date mplConfigDate, final Date startTime)
	{
		List<JuspayWebhookModel> webHookDetailList = new ArrayList<JuspayWebhookModel>();
		webHookDetailList = juspayWebHookDao.fetchSpecificWebHookData(mplConfigDate, startTime);
		if (null != webHookDetailList && !webHookDetailList.isEmpty())
		{
			validateWebHookData(webHookDetailList);
		}
	}

	/**
	 * @Description : Fetch Configurable JOB TAT in Mins
	 * @return Double
	 *
	 */
	@Override
	public Double getWebHookJobTAT()
	{
		Double webHookjobTAT = Double.valueOf(0);
		try
		{
			final BaseStoreModel store = juspayWebHookDao.getJobTAT();
			if (null != store && null != store.getJuspayWebHookTAT())
			{
				webHookjobTAT = store.getJuspayWebHookTAT();
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
		}
		return webHookjobTAT;
	}

	/**
	 * This method fetches the TAT against the base store
	 *
	 * @return Double
	 *
	 */
	@Override
	public Double getEBSJobTAT()
	{
		Double webHookjobTAT = Double.valueOf(0);
		try
		{
			final BaseStoreModel store = juspayWebHookDao.getJobTAT();
			if (null != store && null != store.getJuspayEBSTAT())
			{
				webHookjobTAT = store.getJuspayEBSTAT();
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
		}
		return webHookjobTAT;
	}


	/**
	 * This method updates the audit Entry, Audit and the order status after getting risk response from Juspay when
	 * initially the risk response was empty
	 *
	 * @param orderStatusResponse
	 * @param auditModel
	 * @param orderModel
	 * @return OrderModel
	 *
	 */
	@Override
	public OrderModel updateAuditEntry(final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditModel auditModel,
			final OrderModel orderModel)
	{
		List<MplPaymentAuditEntryModel> collection = auditModel.getAuditEntries();
		final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
		if (null == collection || collection.isEmpty())
		{
			collection = new ArrayList<MplPaymentAuditEntryModel>();
		}
		auditEntryList.addAll(collection);


		final Collection<JuspayEBSResponseModel> ebsResponseColl = auditModel.getRisk();
		final List<JuspayEBSResponseModel> ebsResponseList = new ArrayList<JuspayEBSResponseModel>();
		if (null != ebsResponseColl)
		{
			ebsResponseList.addAll(ebsResponseColl);
		}

		//Condition when RiskResponse is available in OrderStatusResponse
		if (null != orderStatusResponse.getRiskResponse())
		{
			updateAuditRisk(orderStatusResponse, orderModel, auditEntryList, ebsResponseList, auditModel);
		}

		final ArrayList<JuspayEBSResponseModel> riskList = new ArrayList<JuspayEBSResponseModel>();
		if (null != auditModel.getRisk() && !auditModel.getRisk().isEmpty())
		{
			riskList.addAll(auditModel.getRisk());

			if (!riskList.isEmpty() && StringUtils.isNotEmpty(riskList.get(0).getEbsRiskPercentage())
					&& !riskList.get(0).getEbsRiskPercentage().equalsIgnoreCase("-1.0"))
			{
				getMplFraudModelService().updateFraudModel(orderModel, auditModel);
			}
		}

		return orderModel;
	}

	/**
	 * This method updates audit and audit entry after initially the risk has come as empty and then status is
	 * approved/rejected
	 *
	 * @param orderStatusResponse
	 * @param auditModel
	 * @param orderModel
	 * @return OrderModel
	 */
	@Override
	public OrderModel updAuditForEmptyRisk(final GetOrderStatusResponse orderStatusResponse,
			final MplPaymentAuditModel auditModel, final OrderModel orderModel)
	{
		List<MplPaymentAuditEntryModel> collection = auditModel.getAuditEntries();
		final List<MplPaymentAuditEntryModel> auditEntryList = new ArrayList<MplPaymentAuditEntryModel>();
		if (null == collection || collection.isEmpty())
		{
			collection = new ArrayList<MplPaymentAuditEntryModel>();
		}
		auditEntryList.addAll(collection);


		final Collection<JuspayEBSResponseModel> ebsResponseColl = auditModel.getRisk();
		final List<JuspayEBSResponseModel> ebsResponseList = new ArrayList<JuspayEBSResponseModel>();
		if (null != ebsResponseColl)
		{
			ebsResponseList.addAll(ebsResponseColl);
		}

		if (orderStatusResponse.getRiskResponse().getEbsPaymentStatus()
				.equalsIgnoreCase(MarketplacecommerceservicesConstants.APPROVED))
		{
			getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
		}
		else if (orderStatusResponse.getRiskResponse().getEbsPaymentStatus()
				.equalsIgnoreCase(MarketplacecommerceservicesConstants.REJECTED))
		{
			getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.RMS_VERIFICATION_FAILED);
		}


		if (!ebsResponseList.isEmpty())
		{
			for (final JuspayEBSResponseModel ebsResponse : ebsResponseList)
			{
				if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
						&& !orderStatusResponse.getRiskResponse().getEbsPaymentStatus()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAID))
				{
					getMplPaymentService().setEBSRiskStatus(orderStatusResponse.getRiskResponse().getEbsPaymentStatus(), ebsResponse);
				}
			}
		}


		if (!orderModel.getStatus().equals(OrderStatus.RMS_VERIFICATION_PENDING))
		{
			setAuditEntryStatus(orderStatusResponse, orderModel, auditEntryList, auditModel);
		}

		return orderModel;
	}


	/**
	 * This method fetches the base store and returns the TAT1 configured against it
	 *
	 * @return Double
	 */
	@Override
	public Double getEmptyEBSJobTAT()
	{
		Double webHookjobTAT = Double.valueOf(0);
		try
		{
			final BaseStoreModel store = juspayWebHookDao.getJobTAT();
			if (null != store && null != store.getJuspayEBSTAT1())
			{
				webHookjobTAT = store.getJuspayEBSTAT1();
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
		}
		return webHookjobTAT;
	}


	/**
	 * This method fetches the base store and returns the TAT1 configured against it
	 *
	 * @return Double
	 */
	@Override
	public Double getEBSTatExpiryAlertTime()
	{
		Double ebsTatExpiryAlertTime = Double.valueOf(0);
		try
		{
			final BaseStoreModel store = juspayWebHookDao.getJobTAT();
			if (null != store && null != store.getEbsTatExpiryAlertTime())
			{
				ebsTatExpiryAlertTime = store.getEbsTatExpiryAlertTime();
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
		}
		return ebsTatExpiryAlertTime;
	}


	/**
	 * This method updates the EBS Response Risk
	 *
	 * @param ebsResponseList
	 * @param orderStatusResponse
	 * @param auditEntry
	 */
	private void setEbsResponseRisk(final List<JuspayEBSResponseModel> ebsResponseList,
			final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditEntryModel auditEntry)
	{
		if (!ebsResponseList.isEmpty())
		{
			for (final JuspayEBSResponseModel ebsResponse : ebsResponseList)
			{
				if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsBinCountry()))
				{
					ebsResponse.setEbs_bin_country(orderStatusResponse.getRiskResponse().getEbsBinCountry());
				}
				if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsRiskLevel()))
				{
					getMplPaymentService().setEBSRiskLevel(orderStatusResponse.getRiskResponse().getEbsRiskLevel(), ebsResponse);
				}
				if (null != orderStatusResponse.getRiskResponse().getEbsRiskPercentage())
				{
					final Double scoreDouble = Double.valueOf(orderStatusResponse.getRiskResponse().getEbsRiskPercentage()
							.doubleValue());
					ebsResponse.setEbsRiskPercentage(scoreDouble.toString());
				}
				if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
						&& !orderStatusResponse.getRiskResponse().getEbsPaymentStatus()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAID))
				{
					getMplPaymentService().setEBSRiskStatus(orderStatusResponse.getRiskResponse().getEbsPaymentStatus(), ebsResponse);
				}
				else if (StringUtils.isEmpty(orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
						&& !ebsResponse.getEbsRiskPercentage().equalsIgnoreCase("-1.0"))
				{
					if (null != auditEntry && null != auditEntry.getStatus()
							&& StringUtils.isNotEmpty(auditEntry.getStatus().toString()))
					{
						if (MplPaymentAuditStatusEnum.PENDING.equals(auditEntry.getStatus()))
						{
							getMplPaymentService().setEBSRiskStatus(MarketplacecommerceservicesConstants.REVIEW, ebsResponse);
						}
						else if (MplPaymentAuditStatusEnum.COMPLETED.equals(auditEntry.getStatus()))
						{
							getMplPaymentService().setEBSRiskStatus(MarketplacecommerceservicesConstants.APPROVED, ebsResponse);
						}
						else if (MplPaymentAuditStatusEnum.DECLINED.equals(auditEntry.getStatus())
								|| MplPaymentAuditStatusEnum.EBS_DECLINED.equals(auditEntry.getStatus()))
						{
							getMplPaymentService().setEBSRiskStatus(MarketplacecommerceservicesConstants.REJECTED, ebsResponse);
						}
					}
				}
				modelService.save(ebsResponse);
			}
		}
	}


	/**
	 * This method updates the risk table related to the audit table
	 *
	 * @param orderStatusResponse
	 * @param orderModel
	 * @param auditEntryList
	 * @param auditModel
	 * @param ebsResponseList
	 */
	private void updateAuditRisk(final GetOrderStatusResponse orderStatusResponse, final OrderModel orderModel,
			final List<MplPaymentAuditEntryModel> auditEntryList, final List<JuspayEBSResponseModel> ebsResponseList,
			final MplPaymentAuditModel auditModel)
	{

		if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsRiskLevel()))
		{
			//Condition when RiskLevel is GREEN
			if (orderStatusResponse.getRiskResponse().getEbsRiskLevel().equalsIgnoreCase(MarketplacecommerceservicesConstants.GREEN))
			{
				getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
			}

			//Condition when RiskLevel is NOT GREEN
			else
			{
				//Condition for Domestic Card //TODO::Change once this is finalized
				if (StringUtils.isNotEmpty(orderStatusResponse.getRiskResponse().getEbsBinCountry())
						&& orderStatusResponse.getRiskResponse().getEbsBinCountry()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.INDIA))
				{
					if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.YELLOW)
							|| orderStatusResponse.getRiskResponse().getEbsRiskLevel()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.RED))
					{
						getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.RMS_VERIFICATION_PENDING);
					}
				}
				//Condition for International Card
				else
				{
					if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.YELLOW))
					{
						getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.RMS_VERIFICATION_PENDING);
					}
					if (orderStatusResponse.getRiskResponse().getEbsRiskLevel()
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.RED))
					{
						getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.RMS_VERIFICATION_FAILED);
					}
				}
			}
		}
		else
		{
			getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.RMS_VERIFICATION_PENDING);
		}

		if (!orderModel.getStatus().equals(OrderStatus.RMS_VERIFICATION_PENDING))
		{
			final MplPaymentAuditEntryModel auditEntry = setAuditEntryStatus(orderStatusResponse, orderModel, auditEntryList,
					auditModel);
			setEbsResponseRisk(ebsResponseList, orderStatusResponse, auditEntry);
		}

		else
		{
			for (final MplPaymentAuditEntryModel auditEntryModel : auditEntryList)
			{
				if (null != auditEntryModel.getStatus() && auditEntryModel.getStatus().equals(MplPaymentAuditStatusEnum.PENDING))
				{
					setEbsResponseRisk(ebsResponseList, orderStatusResponse, auditEntryModel);
					break;
				}
			}
		}

		auditModel.setRisk(ebsResponseList);
		modelService.save(auditModel);

	}

	/**
	 * This method creates a new entry in AuditEntryModel
	 *
	 * @param orderStatusResponse
	 * @param orderModel
	 * @param auditEntryList
	 * @return MplPaymentAuditEntryModel
	 */
	private MplPaymentAuditEntryModel setAuditEntryStatus(final GetOrderStatusResponse orderStatusResponse,
			final OrderModel orderModel, final List<MplPaymentAuditEntryModel> auditEntryList, final MplPaymentAuditModel auditModel)
	{
		final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
		if (StringUtils.isNotEmpty(orderStatusResponse.getOrderId()))
		{
			auditEntry.setAuditId(orderStatusResponse.getOrderId());
		}
		auditEntry.setResponseDate(new Date());
		if (orderModel.getStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL))
		{
			auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
			auditModel.setIsExpired(Boolean.TRUE);
		}
		else if (orderModel.getStatus().equals(OrderStatus.PAYMENT_FAILED))
		{
			auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
			auditModel.setIsExpired(Boolean.TRUE);
		}
		else if (orderModel.getStatus().equals(OrderStatus.RMS_VERIFICATION_FAILED))
		{
			auditEntry.setStatus(MplPaymentAuditStatusEnum.EBS_DECLINED);
			auditModel.setIsExpired(Boolean.TRUE);
		}

		modelService.save(auditEntry);
		auditEntryList.add(auditEntry);

		auditModel.setAuditEntries(auditEntryList);

		modelService.save(auditModel);

		return auditEntry;
	}

	//Getters and setters
	/**
	 * @return the juspayWebHookDao
	 */
	public JuspayWebHookDao getJuspayWebHookDao()
	{
		return juspayWebHookDao;
	}

	/**
	 * @param juspayWebHookDao
	 *           the juspayWebHookDao to set
	 */
	public void setJuspayWebHookDao(final JuspayWebHookDao juspayWebHookDao)
	{
		this.juspayWebHookDao = juspayWebHookDao;
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

	/**
	 * @return the mplJusPayRefundService
	 */
	public MplJusPayRefundService getMplJusPayRefundService()
	{
		return mplJusPayRefundService;
	}

	/**
	 * @param mplJusPayRefundService
	 *           the mplJusPayRefundService to set
	 */
	public void setMplJusPayRefundService(final MplJusPayRefundService mplJusPayRefundService)
	{
		this.mplJusPayRefundService = mplJusPayRefundService;
	}

	/**
	 * @return the mplPaymentService
	 */
	public MplPaymentService getMplPaymentService()
	{
		return mplPaymentService;
	}

	/**
	 * @param mplPaymentService
	 *           the mplPaymentService to set
	 */
	public void setMplPaymentService(final MplPaymentService mplPaymentService)
	{
		this.mplPaymentService = mplPaymentService;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * @return the orderStatusSpecifier
	 */
	public OrderStatusSpecifier getOrderStatusSpecifier()
	{
		return orderStatusSpecifier;
	}

	/**
	 * @param orderStatusSpecifier
	 *           the orderStatusSpecifier to set
	 */
	public void setOrderStatusSpecifier(final OrderStatusSpecifier orderStatusSpecifier)
	{
		this.orderStatusSpecifier = orderStatusSpecifier;
	}

	/**
	 * @return the mplFraudModelService
	 */
	public MplFraudModelService getMplFraudModelService()
	{
		return mplFraudModelService;
	}

	/**
	 * @param mplFraudModelService
	 *           the mplFraudModelService to set
	 */
	public void setMplFraudModelService(final MplFraudModelService mplFraudModelService)
	{
		this.mplFraudModelService = mplFraudModelService;
	}
}
