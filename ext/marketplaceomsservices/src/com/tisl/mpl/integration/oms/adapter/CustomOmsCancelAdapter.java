/**
 * 
 */
package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;

import de.hybris.platform.commercefacades.order.data.OrderData;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;

import com.tisl.mpl.model.CRMTicketDetailModel;
import com.tisl.mpl.ordercancel.MplOrderCancelEntry;
import com.tisl.mpl.ordercancel.MplOrderCancelRequest;
import com.tisl.mpl.service.ReturnLogisticsService;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;
import com.tisl.mpl.wsdto.PushNotificationData;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.wsdto.TicketMasterXMLData;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;
import com.tisl.mpl.xml.pojo.ReturnLogisticsResponse;

/**
 * @author Tech
 *
 */
public class CustomOmsCancelAdapter implements Serializable
{
	
	private static final Logger LOG = Logger.getLogger(CustomOmsCancelAdapter.class);
	
	@Autowired
	private OrderCancelService orderCancelService;
	
	
	
	@Autowired
	private OrderModelService orderModelService;
	
	
	
	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;
	
	@Autowired
	private PriceDataFactory priceDataFactory;
	
	@Autowired
	private TicketCreationCRMservice ticketCreate;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private ReturnLogisticsService returnLogistics;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private MplOrderService mplOrderService;
	
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;
	
	@Autowired
	private MplSNSMobilePushServiceImpl mplSNSMobilePushService;
	
	
	
	@Autowired
	private BusinessProcessService businessProcessService;
	

	
	public boolean createTicketInCRM( final String subOrderEntryTransactionId,
			final String ticketTypeCode, final String reasonCode, final String refundType, 
			 final OrderModel subOrderModel)
	{
		boolean ticketCreationStatus = false;
		try
		{
			final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
			final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();

			final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntryTransactionId);
			for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
			{
				final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
				sendTicketLineItemData.setLineItemId(abstractOrderEntryModel.getOrderLineId());
				if (ticketTypeCode.equalsIgnoreCase("C"))
				{
					sendTicketLineItemData.setCancelReasonCode(reasonCode);
					sendTicketRequestData.setRefundType(refundType);
					sendTicketRequestData.setTicketSubType(MarketplaceomsordersConstants.TICKET_SUB_TYPE_CODE);
				}
				else
				{
					sendTicketLineItemData.setReturnReasonCode(reasonCode);
					sendTicketRequestData.setRefundType(refundType);
					boolean returnLogisticsCheck = true;
					//Start
					
					final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderModel);
					if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
					{
						for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
						{
							if (StringUtils.isNotEmpty(response.getIsReturnLogisticsAvailable())
									&& response.getIsReturnLogisticsAvailable().equalsIgnoreCase("N"))
							{
								returnLogisticsCheck = false;
								break;
							}
						}
					}
					else
					{
						returnLogisticsCheck = false;
					}
					LOG.info(">>createTicketInCRM >> Setting Type of Return :" + returnLogisticsCheck);
					if (returnLogisticsCheck)
					{
						//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
						sendTicketRequestData.setReturnCategory("RSP");
					}
					else
					{
						//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
						sendTicketRequestData.setReturnCategory("RSS");
					}

					//lineItemDataList.add(sendTicketLineItemData);
					//End
				}

				lineItemDataList.add(sendTicketLineItemData);
			}
			//sendTicketRequestData.setCustomerID(customerData.getUid());
			sendTicketRequestData.setCustomerID(subOrderModel.getUser().getUid());
			sendTicketRequestData.setLineItemDataList(lineItemDataList);
			sendTicketRequestData.setOrderId(subOrderModel.getParentReference().getCode());
			sendTicketRequestData.setSubOrderId(subOrderModel.getCode());
			sendTicketRequestData.setTicketType(ticketTypeCode);

			final String asyncEnabled = configurationService.getConfiguration()
					.getString(MarketplacecommerceservicesConstants.ASYNC_ENABLE).trim();
			//create ticket only if async is not working
			if (asyncEnabled.equalsIgnoreCase("N"))
			{
				ticketCreate.ticketCreationModeltoWsDTO(sendTicketRequestData);
			}
			else
			{
				// CRM ticket Cron JOB data preparation
				saveTicketDetailsInCommerce(sendTicketRequestData);
			}

			ticketCreationStatus = true;

		}
		catch (final JAXBException ex)
		{
			LOG.error(" >> Exception occured while CRM ticket creation", ex);
		}
		catch (final Exception ex)
		{
			LOG.error(" >> Exception occured while CRM ticket creation", ex);
		}

		return ticketCreationStatus;
	}
	
	private List<AbstractOrderEntryModel> associatedEntries(final OrderModel subOrderDetails, final String transactionId)
	{
		final List<AbstractOrderEntryModel> orderEntries = new ArrayList<>();

		final List<String> parentTransactionIdList = new ArrayList<String>();
		for (final AbstractOrderEntryModel subEntry : subOrderDetails.getEntries())
		{
			final String parentTransactionId = subEntry.getParentTransactionID();
			if (StringUtils.isNotEmpty(parentTransactionId)
					&& (subEntry.getIsBOGOapplied().booleanValue() || subEntry.getGiveAway().booleanValue())
					&& parentTransactionId.split(",").length > 1 && parentTransactionId.contains(transactionId))
			{
				for (final String parentTransId : parentTransactionId.split(","))
				{
					parentTransactionIdList.add(parentTransId);
				}
				parentTransactionIdList.add(subEntry.getTransactionID());
			}
			else if (StringUtils.isNotEmpty(parentTransactionId)
					&& (subEntry.getIsBOGOapplied().booleanValue() || subEntry.getGiveAway().booleanValue())
					&& parentTransactionId.split(",").length == 1 && parentTransactionId.equalsIgnoreCase(transactionId))
			{
				parentTransactionIdList.add(parentTransactionId);
				parentTransactionIdList.add(subEntry.getTransactionID());
			}
		}


		LOG.debug("*** parentTransactionIdList " + parentTransactionIdList + " parentTransactionIdList :"
				+ parentTransactionIdList.size());

		for (final AbstractOrderEntryModel subEntry : subOrderDetails.getEntries())
		{
			if (transactionId.equalsIgnoreCase(subEntry.getTransactionID())
					|| (CollectionUtils.isNotEmpty(parentTransactionIdList) && parentTransactionIdList.contains(subEntry
							.getTransactionID())))
			{
				orderEntries.add(subEntry);
			}
		}

		return orderEntries;
	}
	
	public List<ReturnLogisticsResponseData> checkReturnLogistics(final OrderModel subOrderModel)
	{
		try
		{
			final List<AbstractOrderEntryModel> entries = subOrderModel.getEntries();
			final List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
			String transactionId = "";
			for (final AbstractOrderEntryModel eachEntry : entries)
			{
				final ReturnLogistics returnLogistics = new ReturnLogistics();
				//TISEE-5557
				if (!(eachEntry.getGiveAway().booleanValue() || eachEntry.getIsBOGOapplied().booleanValue()))
				//	|| (null != eachEntry.getAssociatedItems() && !eachEntry.getAssociatedItems().isEmpty())))
				{
					returnLogistics.setOrderId(subOrderModel.getCode());
					if (StringUtils.isNotEmpty(eachEntry.getOrderLineId()))
					{
						transactionId = eachEntry.getOrderLineId();
						returnLogistics.setTransactionId(eachEntry.getOrderLineId());
					}
					else if (StringUtils.isNotEmpty(eachEntry.getTransactionID()))
					{
						transactionId = eachEntry.getTransactionID();
						returnLogistics.setTransactionId(eachEntry.getTransactionID());
					}
				}
				returnLogisticsList.add(returnLogistics);
			}
			final List<OrderLineDataResponse> responseList = new ArrayList<OrderLineDataResponse>();
			final List<ReturnLogisticsResponseData> returnLogRespDataList = new ArrayList<ReturnLogisticsResponseData>();
			if (!returnLogisticsList.isEmpty())
			{
				final ReturnLogisticsResponse response = returnLogistics.returnLogisticsCheck(returnLogisticsList);
				if (null != response.getOrderlines())
				{
					for (final OrderLineDataResponse orderLine : response.getOrderlines())
					{
						final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
						if (null != orderLine.getOrderId())
						{
							returnLogRespData.setOrderId(orderLine.getOrderId());
						}
						if (null != orderLine.getTransactionId())
						{
							returnLogRespData.setTransactionId(orderLine.getTransactionId());
						}
						if (null != orderLine.getIsReturnLogisticsAvailable())
						{
							returnLogRespData.setIsReturnLogisticsAvailable(orderLine.getIsReturnLogisticsAvailable());
							if (orderLine.getIsReturnLogisticsAvailable().equalsIgnoreCase("Y"))
							{
								returnLogRespData
										.setResponseMessage(MarketplaceomsordersConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_MESSAGE);
								returnLogRespData
										.setResponseDescription(MarketplaceomsordersConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_DESC);
							}
							else
							{
								returnLogRespData
										.setResponseMessage(MarketplaceomsordersConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE);
								returnLogRespData
										.setResponseDescription(MarketplaceomsordersConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC);
							}

						}
						returnLogRespDataList.add(returnLogRespData);
						responseList.add(orderLine);
					}
				}
				else
				{
					//TISEE-5357
					LOG.debug("*****Reverse logistics availabilty  Response orderline is null*********");
					final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
					returnLogRespData.setIsReturnLogisticsAvailable("N");
					if (null != subOrderModel.getCode())
					{
						returnLogRespData.setOrderId(subOrderModel.getCode());
						returnLogRespData
								.setResponseMessage(MarketplaceomsordersConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE);
						returnLogRespData
								.setResponseDescription(MarketplaceomsordersConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC);
					}
					returnLogRespData.setTransactionId(transactionId);
					returnLogRespDataList.add(returnLogRespData);
				}
			}
			return returnLogRespDataList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplaceomsordersConstants.E0000);
		}
	}
	
	private void saveTicketDetailsInCommerce(final SendTicketRequestData sendTicketRequestData)
	{
		String crmRequest = null;

		final CRMTicketDetailModel ticket = modelService.create(CRMTicketDetailModel.class);
		if (null != sendTicketRequestData.getCustomerID())
		{
			ticket.setCustomerID(sendTicketRequestData.getCustomerID());
			LOG.debug("ticket create: customer Id>>>>> " + sendTicketRequestData.getCustomerID());
		}
		if (null != sendTicketRequestData.getOrderId())
		{
			ticket.setOrderId(sendTicketRequestData.getOrderId());
			LOG.debug("ticket create:order Id>>>>> " + sendTicketRequestData.getOrderId());
		}
		if (null != sendTicketRequestData.getSubOrderId())
		{
			ticket.setSubOrderId(sendTicketRequestData.getSubOrderId());
			LOG.debug("ticket create:suborder Id>>>>> " + sendTicketRequestData.getSubOrderId());
		}

		if (null != sendTicketRequestData.getTicketType())
		{
			ticket.setTicketType(sendTicketRequestData.getTicketType());
		}
		if (null != sendTicketRequestData.getRefundType())
		{
			ticket.setRefundType(sendTicketRequestData.getRefundType());
		}
		if (null != sendTicketRequestData.getReturnCategory())
		{
			ticket.setReturnCategory(sendTicketRequestData.getReturnCategory());
		}

		final TicketMasterXMLData ticketXmlData = ticketCreate.ticketCreationModeltoXMLData(sendTicketRequestData);
		if (ticketXmlData != null)
		{
			try
			{
				crmRequest = ticketCreate.createCRMRequestXml(ticketXmlData);
			}
			catch (final JAXBException ex)
			{
				LOG.info(MarketplaceomsordersConstants.EXCEPTION_IS);

			}
			ticket.setCRMRequest(crmRequest);
		}
		modelService.save(ticket);
	}
	
	boolean initiateCancellation(final String ticketTypeCode,
			final String subOrderEntryTrnxId, final OrderModel subOrderModel, final String reasonCode)
	{
		boolean cancellationInitiated = false;

		try
		{
			if ("C".equalsIgnoreCase(ticketTypeCode))
			{
				final MplOrderCancelRequest orderCancelRequest = buildCancelRequest(reasonCode, subOrderModel,
						subOrderEntryTrnxId);
			//	requestOrderCancel(subOrderDetails, subOrderModel, orderCancelRequest);
				requestOrderCancel( subOrderModel, orderCancelRequest);
			}
			cancellationInitiated = true;
		}
		catch (final ModelSavingException e)
		{
			e.printStackTrace();
			throw new EtailNonBusinessExceptions(e, MarketplaceomsordersConstants.E0007);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			throw new EtailNonBusinessExceptions(ex, MarketplaceomsordersConstants.E0000);
			
		}
		return cancellationInitiated;
	}
	
	private MplOrderCancelRequest buildCancelRequest(final String reasonCode,
			final OrderModel subOrderModel, final String transactionId) throws OrderCancelException
	{

		final List orderCancelEntries = new ArrayList();

		//Get the reason from Global Code master
		String reasonDescription = null;
		final List<CancellationReasonModel> cancellationReasonList = mplOrderService.getCancellationReason();
		for (final CancellationReasonModel cancellationReason : cancellationReasonList)
		{
			if (cancellationReason.getReasonCode().equalsIgnoreCase(reasonCode))
			{
				if (StringUtils.isNotEmpty(cancellationReason.getReasonDescription()))
				{
					reasonDescription = cancellationReason.getReasonDescription();
				}
				break;
			}
		}


		final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, transactionId);
		for (final AbstractOrderEntryModel orderEntryData : orderEntries)
		{
			final MplOrderCancelEntry orderCancelEntryData = new MplOrderCancelEntry(orderEntryData, orderEntryData.getQuantity()
					.longValue(), reasonDescription, reasonDescription);
			orderCancelEntries.add(orderCancelEntryData);
		}

		LOG.debug(">>> buildCancelRequest : orderCancelEntries size " + orderCancelEntries.size());

		//create order cancel request
		final CancelReason reason = CancelReason.valueOf(reasonDescription);
		final MplOrderCancelRequest orderCancelRequest = new MplOrderCancelRequest(subOrderModel, orderCancelEntries);
		orderCancelRequest.setCancelReason(reason);
		orderCancelRequest.setNotes(reasonDescription);
		double refundAmount = 0D;
		for (final OrderCancelEntry orderCancelEntry : orderCancelRequest.getEntriesToCancel())
		{
			final AbstractOrderEntryModel orderEntry = orderCancelEntry.getOrderEntry();
			final List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(
					subOrderModel.getPaymentTransactions());

			if (CollectionUtils.isNotEmpty(tranactions))
			{
				for (final PaymentTransactionModel transaction : tranactions)
				{
					if (CollectionUtils.isNotEmpty(transaction.getEntries()))
					{
						for (final PaymentTransactionEntryModel entry : transaction.getEntries())
						{
							if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
									&& entry.getPaymentMode().getMode().equalsIgnoreCase("COD"))
							{
								orderCancelRequest.setAmountToRefund(NumberUtils.DOUBLE_ZERO);
								return orderCancelRequest;
							}
						}
					}
				}
			}

			double deliveryCost = 0D;
			if (orderEntry.getCurrDelCharge() != null)
			{
				deliveryCost = orderEntry.getCurrDelCharge().doubleValue();
			}


			refundAmount = refundAmount + orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost;
			
			refundAmount = mplJusPayRefundService.validateRefundAmount(refundAmount, subOrderModel);

		}
		//Setting Refund Amount
		orderCancelRequest.setAmountToRefund(new Double(refundAmount));
	
		return orderCancelRequest;
	}
	
	private void requestOrderCancel(final OrderModel subOrderModel,
			final MplOrderCancelRequest orderCancelRequest) throws OrderCancelException
	{
		//cancel Order
		
		final OrderCancelRecordEntryModel orderRequestRecord = orderCancelService.requestOrderCancel(orderCancelRequest,subOrderModel.getUser());
		
		if (OrderCancelEntryStatus.DENIED.equals(orderRequestRecord.getCancelResult()))
		{
			final String orderCode = subOrderModel.getCode();

			String message = MarketplaceomsordersConstants.EMPTY;
			if (orderRequestRecord.getRefusedMessage() != null)
			{
				message = message + orderRequestRecord.getRefusedMessage();
			}
			if (orderRequestRecord.getFailedMessage() != null)
			{
				message = message + orderRequestRecord.getFailedMessage();
			}

			throw new OrderCancelException(orderCode, message);
		}
		else
		{
			initiateRefund( subOrderModel, orderRequestRecord);
		}
	}
	
	private void initiateRefund( final OrderModel subOrderModel,
			final OrderCancelRecordEntryModel orderRequestRecord)
	{

		PaymentTransactionModel paymentTransactionModel = null;
		if (orderRequestRecord.getRefundableAmount() != null
				&& orderRequestRecord.getRefundableAmount().doubleValue() > NumberUtils.DOUBLE_ZERO.doubleValue())
		{
			//TISSIT-1801
			final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
			try
			{
				LOG.debug("****** initiateRefund Step 1 >> Begin >> Calling for prepaid for " + orderRequestRecord.getCode());
				paymentTransactionModel = mplJusPayRefundService.doRefund(subOrderModel, orderRequestRecord.getRefundableAmount()
						.doubleValue(), PaymentTransactionType.CANCEL, uniqueRequestId);

				if (null != paymentTransactionModel)
				{
					mplJusPayRefundService.attachPaymentTransactionModel(subOrderModel, paymentTransactionModel);

					if (CollectionUtils.isNotEmpty(orderRequestRecord.getOrderEntriesModificationEntries()))
					{
						for (final OrderEntryModificationRecordEntryModel modificationEntry : orderRequestRecord
								.getOrderEntriesModificationEntries())
						{
							final OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
							ConsignmentStatus newStatus = null;
							if (orderEntry != null)
							{
								if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
										MarketplacecommerceservicesConstants.SUCCESS))
								{
									newStatus = ConsignmentStatus.ORDER_CANCELLED;
								}
								else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "PENDING"))
								{
									newStatus = ConsignmentStatus.REFUND_INITIATED;
									final RefundTransactionMappingModel refundTransactionMappingModel = modelService
											.create(RefundTransactionMappingModel.class);
									refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
									refundTransactionMappingModel.setJuspayRefundId(paymentTransactionModel.getCode());
									refundTransactionMappingModel.setCreationtime(new Date());
									refundTransactionMappingModel.setRefundType(JuspayRefundType.CANCELLED);
									modelService.save(refundTransactionMappingModel);
								}
								else
								{
									newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
								}
								final Double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge()
										: NumberUtils.DOUBLE_ZERO;
								orderEntry.setRefundedDeliveryChargeAmt(deliveryCost);
								orderEntry.setCurrDelCharge(new Double(0D));
								modelService.save(orderEntry);
								LOG.debug("****** initiateRefund : Step 3  >>Payment transaction mode is not null >> Calling OMS with status as received from JUSPAY "
										+ newStatus.getCode());

								final double refundAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue()
										+ deliveryCost.doubleValue();

								//mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,orderRequestRecord.getRefundableAmount(), newStatus);

								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
										Double.valueOf(refundAmount), newStatus);

							}
						}
					}
				}
				else
				{
					LOG.debug("****** initiateRefund >>Payment transaction mode is null");
					////TISSIT-1801
					mplJusPayRefundService.createCancelRefundPgErrorEntry(orderRequestRecord, PaymentTransactionType.CANCEL,
							JuspayRefundType.CANCELLED, uniqueRequestId);
				}
			}
			catch (final Exception e)
			{
				LOG.error(">>>> *****************initiateRefund*********** Exception occured " + e.getMessage(), e);
				////TISSIT-1801
				mplJusPayRefundService.createCancelRefundExceptionEntry(orderRequestRecord, PaymentTransactionType.CANCEL,
						JuspayRefundType.CANCELLED, uniqueRequestId);
			}
		}
		else
		{// Case of COD.

			LOG.debug("****** initiateRefund >> Begin >> OMS will not be called for COD  ");
			final double refundedAmount = 0D;
			paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(orderRequestRecord.getOriginalVersion()
					.getOrder(), MarketplaceomsordersConstants.FAILURE_FLAG, new Double(refundedAmount),
					PaymentTransactionType.CANCEL, MarketplaceomsordersConstants.FAILURE_FLAG, UUID.randomUUID().toString());
			mplJusPayRefundService.attachPaymentTransactionModel(orderRequestRecord.getOriginalVersion().getOrder(),
					paymentTransactionModel);
		}
		orderRequestRecord.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
		orderRequestRecord.setTransactionCode(paymentTransactionModel != null ? paymentTransactionModel.getCode()
				: MarketplacecommerceservicesConstants.EMPTY);
		modelService.save(orderRequestRecord);
	}
	
	PushNotificationData frameCancelPushNotification(final OrderModel subOrderModel, final String suborderEntryNumber,
			final String reasonCode)
	{
		PushNotificationData pushData = null;
		try
		{
			String refundableAmount = null;
			String cancelReason = null;
			String cancelledItems = null;
			AbstractOrderEntryModel entry = null;
			Long subOrderNumber=Long.valueOf(suborderEntryNumber);
			for (final AbstractOrderEntryModel orderEntry : subOrderModel.getEntries())
			{
				if (null != orderEntry.getEntryNumber() && orderEntry.getEntryNumber().longValue() == subOrderNumber.longValue())
				{
					entry = orderEntry;
					break;
				}
			}

			final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
			if (CollectionUtils.isNotEmpty(tranactions))
			{
				final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator().next();

				if (paymentTransEntry.getPaymentMode() != null && paymentTransEntry.getPaymentMode().getMode() != null
						&& "COD".equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
				{
					refundableAmount = "0";
				}
				else
				{
					final double amount = (entry.getNetAmountAfterAllDisc() != null ? entry.getNetAmountAfterAllDisc().doubleValue()
							: 0D) + (entry.getCurrDelCharge() != null ? entry.getCurrDelCharge().doubleValue() : 0D);

					refundableAmount = Double.toString(amount);
				}

			}
			OrderModel orderMod = null;
			if (null != subOrderModel.getCode() && !subOrderModel.getCode().isEmpty())
			{
				orderMod = orderModelService.getOrderPushNotification(subOrderModel.getCode());
			}
			AbstractOrderEntryModel cancelledEntry = null;
			if (null != orderMod)
			{
				for (final AbstractOrderEntryModel orderEntry : orderMod.getEntries())
				{
					if (null != orderEntry.getEntryNumber()
							&& orderEntry.getEntryNumber().longValue() == subOrderNumber.longValue())
					{
						cancelledEntry = orderEntry;
						break;
					}
				}
			}
			if (null != cancelledEntry && null != cancelledEntry.getQuantity())
			{
				cancelledItems = cancelledEntry.getQuantity().toString();
			}
			cancelReason = getReasonForCancellation(reasonCode);

			CustomerModel customer = getModelService().create(CustomerModel.class);
			//if (null != customerData.getUid() && !customerData.getUid().isEmpty())
			if (null != subOrderModel.getUser().getUid() && !subOrderModel.getUser().getUid().isEmpty())
			{
				//customer = getMplSNSMobilePushService().getCustForUId(customerData.getUid());
				customer = getMplSNSMobilePushService().getCustForUId(subOrderModel.getUser().getUid());
				if (null != customer && null != customer.getDeviceKey() && !customer.getDeviceKey().isEmpty()
						&& null != customer.getOriginalUid() && !customer.getOriginalUid().isEmpty() && null != customer.getIsActive()
						&& !customer.getIsActive().isEmpty() && customer.getIsActive().equalsIgnoreCase("Y"))
				{
					pushData = new PushNotificationData();
					if (null != refundableAmount && !refundableAmount.isEmpty() && null != cancelReason && !cancelReason.isEmpty())
					{
						pushData.setMessage(MarketplaceomsordersConstants.PUSH_MESSAGE_ORDER_CANCELLED
								.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO, refundableAmount)
								.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE, cancelledItems)
								.replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO, cancelReason));
					}
					if (null != subOrderModel.getParentReference() && null != subOrderModel.getParentReference().getCode()
							&& !subOrderModel.getParentReference().getCode().isEmpty())
					{
						pushData.setOrderId(subOrderModel.getParentReference().getCode());
					}
					getMplSNSMobilePushService().setUpNotification(customer.getOriginalUid(), pushData);

				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			//return returnReqCreated;
		}
		return pushData;
	}
	private String getReasonForCancellation(final String reasonCode)
	{
		//Get the reason from Global Code master
		String reasonDescription = null;
		final List<CancellationReasonModel> cancellationReason = mplOrderService.getCancellationReason();
		for (final CancellationReasonModel cancelModel : cancellationReason)
		{
			if (cancelModel.getReasonCode().equalsIgnoreCase(reasonCode)
					&& StringUtils.isNotEmpty(cancelModel.getReasonDescription()))
			{
				reasonDescription = cancelModel.getReasonDescription();
				break;
			}
		}
		return reasonDescription;
	}

	/**
	 * @return the orderConverter
	 */
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter the orderConverter to set
	 */
	public void setOrderConverter(Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}
	
	protected OrderData convertToData(final OrderModel orderModel)
	{
		OrderData orderData = null;
		CustomerData customerData = new CustomerData();
		CustomerModel customer = null;
		PriceData convenienceCharge = null;
		PriceData totalPriceWithConvenienceCharge = null;
		final List<OrderData> sellerOrderList = new ArrayList<OrderData>();
		try
		{
			LOG.debug("-----------Order Model to Order Data" + orderModel.getCode());
			final PriceData deliveryCost = createPrice(orderModel, orderModel.getDeliveryCost());
			if (orderModel.getConvenienceCharges() != null)
			{
				convenienceCharge = createPrice(orderModel, orderModel.getConvenienceCharges());
				LOG.debug("-----------Order Data ConvenienceCharges" + orderModel.getCode());
			}
			if (orderModel.getTotalPriceWithConv() != null)
			{
				totalPriceWithConvenienceCharge = createPrice(orderModel, orderModel.getTotalPriceWithConv());
				LOG.debug("-----------Order Data total Price with Conv" + orderModel.getCode());
			}
			//skip the order if product is missing in the order entries
			for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
			{
				if (null == orderEntry.getProduct()) // it means somehow product is deleted from the order entry.
				{
					LOG.info("************************Skipping order history for order :" + orderModel.getCode() + " and for user: "
							+ orderModel.getUser().getName() + " **************************");
					return null;
				}
			}
			orderData = getOrderConverter().convert(orderModel);
			orderData.setDeliveryCost(deliveryCost);
			if (convenienceCharge != null)
			{
				orderData.setConvenienceChargeForCOD(convenienceCharge);
			}
			if (totalPriceWithConvenienceCharge != null)
			{
				orderData.setTotalPriceWithConvCharge(totalPriceWithConvenienceCharge);
			}
			LOG.debug("-----------Order Model to Order Data Customer" + orderModel.getCode());
			if (orderModel.getUser() instanceof CustomerModel)
			{
				customer = (CustomerModel) orderModel.getUser();
			}
			if (customer != null && null != customer.getDefaultShipmentAddress())
			{
				customerData = new CustomerData();
				//TISUAT-4850
				if (customer.getOriginalUid() != null)
				{
					customerData.setEmail(customer.getOriginalUid());
				}
				else
				{
					customerData.setEmail(MarketplacecommerceservicesConstants.NA);
				}
				customerData.setRegistrationDate(customer.getCreationtime());
				orderData.setCustomerData(customerData);
			}

			for (final OrderModel sellerOrder : orderModel.getChildOrders())
			{
				final PriceData childDeliveryCost = createPrice(sellerOrder, sellerOrder.getDeliveryCost());
				final OrderData sellerOrderData = getOrderConverter().convert(sellerOrder);
				orderData.setDeliveryCost(childDeliveryCost);
				if (convenienceCharge != null)
				{
					sellerOrderData.setConvenienceChargeForCOD(convenienceCharge);
				}
				if (totalPriceWithConvenienceCharge != null)
				{
					sellerOrderData.setTotalPriceWithConvCharge(totalPriceWithConvenienceCharge);
				}
				if (orderModel.getUser() instanceof CustomerModel)
				{
					customer = (CustomerModel) orderModel.getUser();
				}
				if (customer != null)
				{
					customerData = new CustomerData();
					//TISUAT-4850
					if (customer.getOriginalUid() != null)
					{
						customerData.setEmail(customer.getOriginalUid());
					}
					else
					{
						customerData.setEmail(MarketplacecommerceservicesConstants.NA);
					}
					customerData.setRegistrationDate(customer.getCreationtime());
					sellerOrderData.setCustomerData(customerData);
				}
				sellerOrderList.add(sellerOrderData);
			}
			orderData.setSellerOrderList(sellerOrderList);

		}
		catch (final Exception e)
		{
			LOG.debug("-----------Order Model to Order Data Exception" + orderModel.getCode());
			return orderData;
		}
		return orderData;

	}
	
	private PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException(MarketplaceomsordersConstants.ORDER_ERROR);
		}

		final CurrencyModel currency = source.getCurrency();

		if (currency == null)
		{
			throw new IllegalArgumentException(MarketplaceomsordersConstants.ORDER_CURRENCY_ERROR);
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}


	
	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * @param priceDataFactory the priceDataFactory to set
	 */
	public void setPriceDataFactory(PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService the modelService to set
	 */
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the mplSNSMobilePushService
	 */
	public MplSNSMobilePushServiceImpl getMplSNSMobilePushService()
	{
		return mplSNSMobilePushService;
	}

	/**
	 * @param mplSNSMobilePushService the mplSNSMobilePushService to set
	 */
	public void setMplSNSMobilePushService(MplSNSMobilePushServiceImpl mplSNSMobilePushService)
	{
		this.mplSNSMobilePushService = mplSNSMobilePushService;
	}

	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService the businessProcessService to set
	 */
	public void setBusinessProcessService(BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
	
	

}
