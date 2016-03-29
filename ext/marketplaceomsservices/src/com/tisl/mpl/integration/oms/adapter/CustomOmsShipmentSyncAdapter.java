/**
 *
 */
package com.tisl.mpl.integration.oms.adapter;


import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;

import de.hybris.platform.commercefacades.order.data.OrderData;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.integration.oms.OrderWrapper;
import de.hybris.platform.integration.oms.ShipmentWrapper;
import de.hybris.platform.integration.oms.adapter.OmsSyncAdapter;
import de.hybris.platform.integration.oms.mapping.OmsHybrisEnumMappingStrategy;
import de.hybris.platform.omsorders.notification.ModelChangeNotifier;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.time.TimeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;


import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.shipping.Shipment;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.core.model.ImeiDetailModel;
import com.tisl.mpl.core.model.InvoiceDetailModel;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
//import com.tisl.mpl.fulfilmentprocess.events.OrderRefundEvent;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCheckInvoice;
import com.tisl.mpl.marketplacecommerceservices.event.OrderRefundCreditedEvent;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplaceomsservices.event.SendNotificationEvent;
import com.tisl.mpl.service.ReturnLogisticsService;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.sms.MplSendSMSService;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;


//import com.tisl.mpl.marketplacecommerceservices.event.ShippingConfirmationEvent;

/**
 * @author TCS
 *
 */
public class CustomOmsShipmentSyncAdapter implements OmsSyncAdapter<OrderWrapper, ConsignmentModel>
{
	@Autowired
	private MplSendSMSService sendSMSService;
	@Autowired
	private EventService eventService;

	private ConfigurationService configurationService;

	private Map<String, OrderStatus> orderStatusMapping;
	private Converter<Shipment, ConsignmentModel> omsShipmentReverseConverter;
	private OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment> consignmentStatusMappingStrategy;
	private ModelService modelService;
	private ModelChangeNotifier<ConsignmentModel> consignmentProcessNotifier;
	private MplCheckInvoice checkInvoice;
	private TimeService timeService;
	private static final Logger LOG = Logger.getLogger(CustomOmsShipmentSyncAdapter.class);
	
	
	
	
	
	
	
	
	private static final String JAVADOC = "javadoc";
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ReturnService returnService;

	@Autowired
	private MplSNSMobilePushServiceImpl mplSNSMobilePushService;

	@Autowired
	private CustomOmsCancelAdapter  customOmsCancelAdapter;
	
	@Autowired
	private CustomOmsCollectedAdapter customOmsCollectedAdapter;

	@Override
	public ConsignmentModel update(final OrderWrapper wrapper, final ItemModel parent)
	{
		ConsignmentModel consignmentFinal = null;
		final OrderModel orderModel = (OrderModel) parent;
		LOG.info("Going to sync Sub-order :" + orderModel.getCode());
		for (final ShipmentWrapper shipmentWrapper : wrapper.getShipments())
		{
			final Shipment shipment = shipmentWrapper.getShipment();
			if(shipment != null && shipment.getOlqsStatus()!= null && shipment.getDeliveryMode() != null){
				
				if((shipment.getDeliveryMode().equalsIgnoreCase(MarketplaceomsservicesConstants.CNC)) && 
						((shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.HOTCOURI)
						|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.OTFRDLVY)
						|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.DELIVERD)
						|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.RETTOORG)
						|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.LOSTINTT)
						|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.UNDLVERD)))){
					
					LOG.debug("Delivery Mode CNC and Order Status  :"+ shipment.getOlqsStatus() +" :Not Required to update Consignment");
					
				}else{
					final ConsignmentModel existingConsignmentModel = getConsignmentByShipment(shipment, orderModel);
					if (existingConsignmentModel == null)
					{
						if (shipmentMustBeCreated(shipment))
						{
							consignmentFinal = createNewConsignment(shipmentWrapper, orderModel);

						}

					}
					else if (updateConsignment(shipment, existingConsignmentModel, orderModel))
					{
						consignmentFinal = existingConsignmentModel;
					}
				}
			}
		}
		try
		{
			Thread.sleep(1000);
			for (final OrderLine line : wrapper.getOrder().getOrderLines())
			{
				final ConsignmentModel existingConsignmentModel = getConsignmentByLine(line, orderModel);
				if (line.getSellerOrderStatus() != null)
				{
					updateOrderStatus(line.getSellerOrderStatus(), orderModel);
				}
				if (existingConsignmentModel != null)
				{
					ConsignmentEntryModel consigmnetEntry = null;
				
					if (CollectionUtils.isEmpty(existingConsignmentModel.getConsignmentEntries()))
					{
						consigmnetEntry = createNewConsigmnetEntry(line.getOrderLineId(), orderModel);
						consigmnetEntry.setConsignment(existingConsignmentModel);
						consigmnetEntry.setShippedQuantity(Long.valueOf(line.getShippedQuantity()));
						modelService.save(consigmnetEntry);
					}
					else
					{
						for (final ConsignmentEntryModel conEntry : existingConsignmentModel.getConsignmentEntries())
						{
							conEntry.setShippedQuantity(Long.valueOf(line.getShippedQuantity()));
							modelService.save(conEntry);
						}

					}

					existingConsignmentModel.setTrackingID(line.getAwbNumber());
					existingConsignmentModel.setEstimatedDelivery(line.getEstimatedDelivery());
					existingConsignmentModel.setDeliveryDate(line.getDeliveryDate());
					existingConsignmentModel.setShippingDate(line.getShippingDate());
					existingConsignmentModel.setShipmentStatus(line.getShipmentStatus());
					existingConsignmentModel.setCarrier(line.getLogisticProviderName());
					existingConsignmentModel.setReturnAWBNum(line.getReturnAWBNum());
					existingConsignmentModel.setReturnCarrier(line.getReverseLogisticProviderName());
					existingConsignmentModel.setReceivedBy(line.getReceivedBy());
					existingConsignmentModel.setCarrier(line.getLogisticProviderName());
					if (line.getInvoiceNo() != null || line.getInvoiceUrl() != null)
					{
						final boolean isInvoiceToBeCreated = validateAndUpdateInvoice(existingConsignmentModel, line.getInvoiceNo(),
								line.getInvoiceUrl(), orderModel);

						if (isInvoiceToBeCreated)
						{
							createInvoice(line.getInvoiceNo(), line.getInvoiceUrl(), existingConsignmentModel);
						}

					}
					else
					{
						LOG.error("Invoice no or invoice url is null for the order ID:" + line.getId());
					}

					saveAndNotifyConsignment(existingConsignmentModel);

				}
				else
				{
					LOG.info("ConsignmentModel Not created but Trying to create ConsignmentEntry for Line : " + line.getId());
				}
				if (line.getQcReasonCode() != null && ConsignmentStatus.QC_FAILED.equals(existingConsignmentModel.getStatus()))
				{
					updateReturnReason(line, orderModel);
				}
				final AbstractOrderEntryModel orderEntry = getOrderEntryByLine(line.getOrderLineId(), orderModel);
				if (orderEntry != null)
				{
					try
					{


						ImeiDetailModel imeiDetails = orderEntry.getImeiDetail();
						if (imeiDetails == null && line.getSerialNum() != null)
						{
							imeiDetails = getModelService().create(ImeiDetailModel.class);
							imeiDetails.setSerialNum(line.getSerialNum());
							if (line.getIMEINo1() != null || line.getIMEINo2() != null || line.getISBNNo() != null)
							{
								final List<String> identifiers = new ArrayList<String>();
								if (line.getIMEINo1() != null)
								{
									identifiers.add(line.getIMEINo1());
								}
								if (line.getIMEINo2() != null)
								{
									identifiers.add(line.getIMEINo2());
								}
								if (line.getISBNNo() != null)
								{
									identifiers.add(line.getISBNNo());
								}
								if (!identifiers.isEmpty())
								{
									imeiDetails.setIdentifiers(identifiers);
								}
								getModelService().save(imeiDetails);
							}

							orderEntry.setImeiDetail(imeiDetails);
							getModelService().save(orderEntry);
						}

					}
					catch (final Exception e)
					{
						LOG.error("Could Not able to update Order From OMS for imeiDetails", e);
						//e.printStackTrace();
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.info("Some Issue in Updating order " + orderModel.getCode());
		}

		return consignmentFinal;
	}

	/**
	 * @param invoice
	 * @param invoiceNo
	 * @param invoiceUrl
	 * @param orderModel
	 */
	@SuppressWarnings(JAVADOC)
	private boolean validateAndUpdateInvoice(final ConsignmentModel existingConsignment, final String invoiceNo,
			final String invoiceUrl, final OrderModel orderModel)
	{

		boolean createInvoice = true;
		final InvoiceDetailModel invoice = findMatchedInvoice(orderModel, invoiceNo);
		if (invoice != null)
		{

			createInvoice = updateInvoice(invoice, invoiceNo, invoiceUrl, existingConsignment);

		}


		return createInvoice;
	}

	/**
	 * @param orderModel
	 * @param invoiceNo
	 * @return
	 */
	private InvoiceDetailModel findMatchedInvoice(final OrderModel orderModel, final String invoiceNo)
	{
		// YTODO Auto-generated method stub
		final InvoiceDetailModel matchedInvoice = null;
		for (final ConsignmentModel cons : orderModel.getConsignments())
		{
			LOG.info("Invoice found in the existing Order Model.");
			if (cons.getInvoice() != null && cons.getInvoice().getInvoiceNo().equals(invoiceNo))
			{
				LOG.info("Invoice matched----" + cons.getInvoice());
				return cons.getInvoice();
			}
		}
		return matchedInvoice;

	}

	private void updateReturnReason(final OrderLine line, final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		for (final ReturnRequestModel returnRequest : orderModel.getReturnRequests())
		{
			for (final ReturnEntryModel returnEntry : returnRequest.getReturnEntries())
			{
				if (returnEntry.getOrderEntry().equals(getOrderEntryByLine(line.getOrderLineId(), orderModel)))
				{
					String qcReasonCodeEnum = MplCodeMasterUtility.getQCFailedDesc(line.getQcReasonCode());
					qcReasonCodeEnum = (qcReasonCodeEnum != null) ? qcReasonCodeEnum : line.getQcReasonCode();
					returnRequest.setRejectionReason(qcReasonCodeEnum);
					modelService.save(returnRequest);
				}
			}

		}

	}

	/**
	 * @param invoice
	 * @param invoiceNo
	 * @param invoiceUrl
	 * @param existingConsignmentModel
	 */
	@SuppressWarnings(JAVADOC)
	private boolean updateInvoice(final InvoiceDetailModel invoice, final String invoiceNo, final String invoiceUrl,
			final ConsignmentModel consignment)
	{
		boolean createInvoice = true;
		// YTODO Auto-generated method stub
		//	final List<ConsignmentModel> consignments = new ArrayList<ConsignmentModel>(invoice.getConsignment());
		try
		{
			//consignments.add(consignment);
			//invoice.setConsignment(consignments);
			createInvoice = false;
			invoice.setInvoiceUrl(invoiceUrl);
			getModelService().save(invoice);
			consignment.setInvoice(invoice);
			getModelService().save(consignment);

		}
		catch (final Exception e)
		{
			LOG.info("Consignment Status RollBack because of Invoice :" + invoice.getInvoiceNo(), e);
		}
		return createInvoice;

	}

	/**
	 * @param line
	 * @param orderModel
	 * @return
	 */
	@SuppressWarnings(JAVADOC)
	private AbstractOrderEntryModel getOrderEntryByLine(final String lineId, final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
		{
			if (orderEntry.getOrderLineId().equals(lineId))
			{
				return orderEntry;
			}

		}
		return null;
	}

	/**
	 * @param invoiceNo
	 * @param invoiceUrl
	 * @return
	 */
	@SuppressWarnings(JAVADOC)
	private InvoiceDetailModel createInvoice(final String invoiceNo, final String invoiceUrl, final ConsignmentModel consignment)
	{
		// YTODO Auto-generated method stub
		//final List<ConsignmentModel> consignments = new ArrayList<ConsignmentModel>();
		//consignments.add(consignment);
		final InvoiceDetailModel invoice = getModelService().create(InvoiceDetailModel.class);
		//invoice.setConsignment(consignments);
		invoice.setInvoiceNo(invoiceNo);
		invoice.setInvoiceUrl(invoiceUrl);

		getModelService().save(invoice);
		consignment.setInvoice(invoice);
		getModelService().save(consignment);
		return invoice;
	}

	//	private void populateInvoice(final InvoiceDetailModel invoice, final String invoiceNo, final String invoiceUrl)
	//	{
	//
	//		invoice.setInvoiceNo(invoiceNo);
	//		invoice.setInvoiceUrl(invoiceUrl);
	//		getModelService().save(invoice);
	//
	//	}

	/**
	 * @param line
	 * @param orderModel
	 * @return
	 */
	@SuppressWarnings(JAVADOC)
	private ConsignmentModel getConsignmentByLine(final OrderLine line, final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		for (final ConsignmentModel consignment : orderModel.getConsignments())
		{
			if (consignment.getCode().equals(line.getOrderLineId()))
			{
				return consignment;
			}

		}
		return null;
	}

	private boolean updateConsignment(final Shipment shipment, final ConsignmentModel consignmentModel, final OrderModel orderModel)
	{

		try
		{
			final ConsignmentStatus shipmentNewStatus = getConsignmentStatusMappingStrategy().getHybrisEnumFromDto(shipment);
			final ConsignmentStatus shipmentCurrentStatus = consignmentModel.getStatus();
																 
			if(consignmentModel.getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) && shipmentNewStatus.equals(ConsignmentStatus.CANCELLATION_INITIATED) ){
				LOG.debug("Calling cancel Initiation process started");
				OrderData orderData =customOmsCancelAdapter.convertToData(orderModel);
				LOG.debug("orderData:"+orderData);
				
				for(AbstractOrderEntryModel orderEntryModel:orderModel.getEntries()){
				 for(ConsignmentEntryModel consigmEntry:orderEntryModel.getConsignmentEntries()){
					if(consigmEntry.getConsignment().getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) && shipmentNewStatus.equals(ConsignmentStatus.CANCELLATION_INITIATED)){
						LOG.debug("******************"+consigmEntry.getConsignment().getStatus());
						LOG.debug("******************"+orderEntryModel.getTransactionID());
						LOG.debug("******************"+orderEntryModel.getOrderLineId());
					   customOmsCancelAdapter.createTicketInCRM(orderData, orderEntryModel.getTransactionID(), MarketplaceomsordersConstants.TICKET_TYPE_CODE, MarketplaceomsordersConstants.EMPTY,
								MarketplaceomsordersConstants.REFUND_TYPE_CODE,  orderData.getCustomerData(), orderModel);
						customOmsCancelAdapter.initiateCancellation(MarketplaceomsordersConstants.TICKET_TYPE_CODE, orderData, orderEntryModel.getTransactionID(), orderModel, MarketplaceomsordersConstants.REASON_CODE);
						final String trackOrderUrl = configurationService.getConfiguration().getString(
								MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
								+ orderModel.getCode();
						final OrderProcessModel orderProcessModel = new OrderProcessModel();
						orderProcessModel.setOrder(orderModel);
						orderProcessModel.setOrderTrackUrl(trackOrderUrl);
						final OrderRefundCreditedEvent orderRefundCreditedEvent = new OrderRefundCreditedEvent(orderProcessModel);
						try
						{
							eventService.publishEvent(orderRefundCreditedEvent);
						}
						catch (final Exception e1)
						{
							LOG.error("Exception during sending mail or SMS >> " + e1.getMessage());
						}
						customOmsCancelAdapter.frameCancelPushNotification(orderModel, orderEntryModel.getOrderLineId(), MarketplaceomsordersConstants.REASON_CODE, orderData.getCustomerData());
						
					}
				 }
				}
				
			/*	for(OrderEntryData subOrderEntries: orderData.getEntries()){
					if(subOrderEntries.getConsignment().getStatus().equals(ConsignmentStatus.CANCELLATION_INITIATED)){
					customOmsCancelAdapter.createTicketInCRM(orderData, subOrderEntries, MarketplaceomsordersConstants.TICKET_TYPE_CODE, MarketplaceomsordersConstants.EMPTY,
							MarketplaceomsordersConstants.REFUND_TYPE_CODE,  orderData.getCustomerData(), orderModel);
					customOmsCancelAdapter.initiateCancellation(MarketplaceomsordersConstants.TICKET_TYPE_CODE, orderData, subOrderEntries, orderModel, MarketplaceomsordersConstants.REASON_CODE);
					final String trackOrderUrl = configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
							+ orderModel.getCode();
					final OrderProcessModel orderProcessModel = new OrderProcessModel();
					orderProcessModel.setOrder(orderModel);
					orderProcessModel.setOrderTrackUrl(trackOrderUrl);
					final OrderRefundCreditedEvent orderRefundCreditedEvent = new OrderRefundCreditedEvent(orderProcessModel);
					try
					{
						eventService.publishEvent(orderRefundCreditedEvent);
					}
					catch (final Exception e1)
					{
						LOG.error("Exception during sending mail or SMS >> " + e1.getMessage());
					}
					customOmsCancelAdapter.frameCancelPushNotification(orderModel, subOrderEntries.getEntryNumber(), MarketplaceomsordersConstants.REASON_CODE, orderData.getCustomerData());
					}
				}*/
				
				
			}
			
			if(ObjectUtils.notEqual(shipmentCurrentStatus, shipmentNewStatus) && shipmentNewStatus.equals(ConsignmentStatus.ORDER_COLLECTED)){
				LOG.debug("Calling deliverd Initiation process started");
				OrderData orderData =customOmsCancelAdapter.convertToData(orderModel);
				customOmsCollectedAdapter.sendNotificationForOrderCollected(orderModel, orderData, consignmentModel);
			}
			
			createRefundEntry(shipmentNewStatus, consignmentModel, orderModel);
			if (ObjectUtils.notEqual(shipmentCurrentStatus, shipmentNewStatus))
			{
				LOG.info("updateConsignment:: Inside ObjectUtils.notEqual(shipmentCurrentStatus, shipmentNewStatus) >>> shipmentCurrentStatus >>"
						+ shipmentCurrentStatus
						+ "<<shipmentNewStatus>>"
						+ shipmentNewStatus
						+ "OrderID::"
						+ orderModel.getCode()
						+ "Order line ID::" + consignmentModel.getCode());
				consignmentModel.setStatus(shipmentNewStatus);
				LOG.info("Consignment Status::" + consignmentModel.getStatus());
				saveAndNotifyConsignment(consignmentModel);
				modelService.save(createHistoryLog(shipmentNewStatus.toString(), orderModel, consignmentModel.getCode()));
				LOG.info("Order History entry created for" + orderModel.getCode() + "Line ID" + consignmentModel.getCode());

				LOG.info("****************************************Order synced succesfully - Now sending notificatioon to customer *******************");
				//call send notification method
				sendOrderNotification(shipment, consignmentModel, orderModel, shipmentNewStatus);


				return true;

			}
		}
		catch (final Exception e)
		{
			LOG.info("Exception either in consignment update or sending notification " + e.getMessage());

		}


		return false;
	}

	/**
	 * @description Method to Send Order Notification to Customer
	 * @param shipment
	 * @param consignmentModel
	 * @param orderModel
	 * @param shipmentNewStatus
	 */
	private void sendOrderNotification(final Shipment shipment, final ConsignmentModel consignmentModel,
			final OrderModel orderModel, final ConsignmentStatus shipmentNewStatus)
	{
		try
		{
			LOG.info("*************Send Notification Start *******************");
			final SendNotificationEvent sendNotificationEvent = new SendNotificationEvent(shipment, consignmentModel, orderModel,
					shipmentNewStatus);
			LOG.info("*************Send Notification publish event *******************");
			eventService.publishEvent(sendNotificationEvent);
			LOG.info("*************Send Notification Done *******************");
		}
		catch (final Exception e)
		{
			LOG.error("Unable to send Notification..!! " + e.getMessage());
			throw e;
		}


	}

	protected void saveAndNotifyConsignment(final ConsignmentModel consignmentModel)
	{
		getModelService().save(consignmentModel);
		getConsignmentProcessNotifier().notify(consignmentModel);
		LOG.info("Consignment updated with::" + consignmentModel.getStatus());
	}

	protected OrderHistoryEntryModel createHistoryLog(final String description, final OrderModel order, final String lineId)
	{
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(getTimeService().getCurrentTime());
		historyEntry.setOrder(order);
		historyEntry.setLineId(lineId);
		historyEntry.setDescription(description);
		return historyEntry;
	}

	protected ConsignmentModel createNewConsignment(final ShipmentWrapper wrapper, final ItemModel owner)
	{
		final ConsignmentModel consignmentModel = (ConsignmentModel) getModelService().create(ConsignmentModel.class);
		wrapper.getShipment().setLocation(MarketplacecommerceservicesConstants.WAREHOUSE); // HardCode as one WareHouse in Commerce.
		getOmsShipmentReverseConverter().convert(wrapper.getShipment(), consignmentModel);
		consignmentModel.getShippingAddress().setOwner(owner);
		final ConsignmentEntryModel consigmnetEntry = createNewConsigmnetEntry(consignmentModel.getCode(), (OrderModel) owner);
		consigmnetEntry.setConsignment(consignmentModel);
		consigmnetEntry.setShippedQuantity(Long.valueOf("1"));
		modelService.save(consigmnetEntry);
		modelService
				.save(createHistoryLog(consignmentModel.getStatus().toString(), (OrderModel) owner, consignmentModel.getCode()));
		getModelService().save(consignmentModel.getShippingAddress());
		saveAndNotifyConsignment(consignmentModel);
		return consignmentModel;
	}

	protected boolean shipmentMustBeCreated(@SuppressWarnings("unused") final Shipment shipment)
	{
		/*
		 * final ConsignmentStatus shipmentMappedStatus =
		 * getConsignmentStatusMappingStrategy().getHybrisEnumFromDto(shipment); return
		 * ((ConsignmentStatus.ALLOCATED.equals(shipmentMappedStatus)) || (ConsignmentStatus.PENDING_SELLER_ASSIGNMENT
		 * .equals(shipmentMappedStatus)));
		 */
		return true;
	}

	protected ConsignmentModel getConsignmentByShipment(final Shipment shipment, final OrderModel orderModel)
	{
		for (final ConsignmentModel consignment : orderModel.getConsignments())
		{
			if (consignment.getCode().equals(shipment.getShipmentId()))
			{
				return consignment;
			}
		}

		return null;
	}

	ConsignmentEntryModel createNewConsigmnetEntry(final String lineId, final OrderModel orderModel)
	{
		final ConsignmentEntryModel newConsignmentEntry = (ConsignmentEntryModel) getModelService().create(
				ConsignmentEntryModel.class);

		newConsignmentEntry.setOrderEntry(getOrderEntryByLine(lineId, orderModel));

		newConsignmentEntry.setQuantity(Long.valueOf(1));
		return newConsignmentEntry;
	}

	
	private void createRefundEntry(final ConsignmentStatus newStatus, final ConsignmentModel consignmentModel,
			final OrderModel orderModel)
	{
		if ((ConsignmentStatus.RETURN_INITIATED.equals(newStatus) || ConsignmentStatus.LOST_IN_TRANSIT.equals(newStatus) || ConsignmentStatus.RETURN_TO_ORIGIN
				.equals(newStatus)) || (ConsignmentStatus.RETURNINITIATED_BY_RTO.equals(newStatus)) || (ConsignmentStatus.QC_FAILED.equals(newStatus)) || (ConsignmentStatus.RETURN_CLOSED.equals(newStatus)) 
            ||(ConsignmentStatus.RETURN_CANCELLED.equals(newStatus)) && CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
		{
			try
			{
				final AbstractOrderEntryModel orderEntry = consignmentModel.getConsignmentEntries().iterator().next().getOrderEntry();
				RefundEntryModel refundEntryModel = new RefundEntryModel();
				refundEntryModel.setOrderEntry(orderEntry);
				if (CollectionUtils.isEmpty(flexibleSearchService.getModelsByExample(refundEntryModel)))
				{
					final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(orderModel);
					returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
					refundEntryModel = modelService.create(RefundEntryModel.class);
					refundEntryModel.setOrderEntry(orderEntry);
					refundEntryModel.setReturnRequest(returnRequestModel);
					//TISEE-5246
					//refundEntryModel.setReason(RefundReason.SITEERROR);
					if (ConsignmentStatus.LOST_IN_TRANSIT.equals(newStatus))
					{
						refundEntryModel.setReason(RefundReason.LOSTINTRANSIT);
					}
					else if (ConsignmentStatus.RETURN_TO_ORIGIN.equals(newStatus))
					{
						refundEntryModel.setReason(RefundReason.RETURNTOORIGIN);
					}
					//CM 1: Added as part of R2.1 to handle new order status 'RETURNINITIATED_BY_RTO','QC_FAILED','RETURN_CLOSED'
					else if (ConsignmentStatus.RETURNINITIATED_BY_RTO.equals(newStatus))
					{
						refundEntryModel.setReason(RefundReason.RETURNTOORIGIN);
					}
					else if (ConsignmentStatus.QC_FAILED.equals(newStatus))
					{
						refundEntryModel.setReason(RefundReason.LOSTINTRANSIT);
					}
					else if (ConsignmentStatus.RETURN_CLOSED.equals(newStatus))
					{
						refundEntryModel.setReason(RefundReason.LOSTINTRANSIT);
					}
					else
					{
						refundEntryModel.setReason(RefundReason.SITEERROR);
					}
					refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
					refundEntryModel.setAction(ReturnAction.IMMEDIATE);
					refundEntryModel.setNotes("Return Initiated by Seller Portal");
					refundEntryModel.setExpectedQuantity(orderEntry.getQuantity());//Single line quantity
					refundEntryModel.setReceivedQuantity(orderEntry.getQuantity());//Single line quantity
					refundEntryModel.setRefundedDate(new Date());
					final List<PaymentTransactionModel> tranactions = orderModel.getPaymentTransactions();
					if (CollectionUtils.isNotEmpty(tranactions))
					{
						final PaymentTransactionEntryModel entry = tranactions.iterator().next().getEntries().iterator().next();

						if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
								&& "COD".equalsIgnoreCase(entry.getPaymentMode().getMode()))
						{
							refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
						}
						else
						{
							final Double amount = orderEntry.getNetAmountAfterAllDisc()
									+ (orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge() : 0D);

							refundEntryModel.setAmount(NumberUtils.createBigDecimal(amount.toString()));
						}

					}

					modelService.save(refundEntryModel);
					modelService.save(returnRequestModel);
				}
			}
			catch (final Exception e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @return the sendSMSService
	 */
	public MplSendSMSService getSendSMSService()
	{
		return sendSMSService;
	}

	/**
	 * @param sendSMSService
	 *           the sendSMSService to set
	 */
	public void setSendSMSService(final MplSendSMSService sendSMSService)
	{
		this.sendSMSService = sendSMSService;
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
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
	 * @return the returnService
	 */
	public ReturnService getReturnService()
	{
		return returnService;
	}

	/**
	 * @param returnService
	 *           the returnService to set
	 */
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	/**
	 * @return the mplSNSMobilePushService
	 */
	public MplSNSMobilePushServiceImpl getMplSNSMobilePushService()
	{
		return mplSNSMobilePushService;
	}

	/**
	 * @param mplSNSMobilePushService
	 *           the mplSNSMobilePushService to set
	 */
	public void setMplSNSMobilePushService(final MplSNSMobilePushServiceImpl mplSNSMobilePushService)
	{
		this.mplSNSMobilePushService = mplSNSMobilePushService;
	}

	/**
	 * @param sellerOrderStatus
	 * @param orderModel
	 */
	private void updateOrderStatus(final String sellerOrderStatus, final OrderModel orderModel)
	{
		// YTODO Auto-generated method stub
		final OrderStatus orderStatus = getOrderStatusMapping().get(sellerOrderStatus);
		orderModel.setStatus(orderStatus);
		modelService.save(orderModel);


	}

	/*
	 * protected ConsignmentModel getConsignmentByLine(final OrderLine line, final OrderModel orderModel) { for (final
	 * ConsignmentModel consignment : orderModel.getConsignments()) { for (final ConsignmentEntryModel s :
	 * consignment.getConsignmentEntries()) { if (s.getOrderEntry().getEntryNumber().equals(line.getOrderLineId())) {
	 * return consignment; } }
	 * 
	 * }
	 * 
	 * return null; }
	 */

	private ModelService getModelService()
	{
		return this.modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	private Converter<Shipment, ConsignmentModel> getOmsShipmentReverseConverter()
	{
		return this.omsShipmentReverseConverter;
	}

	public void setOmsShipmentReverseConverter(final Converter<Shipment, ConsignmentModel> omsShipmentReverseConverter)
	{
		this.omsShipmentReverseConverter = omsShipmentReverseConverter;
	}

	private OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment> getConsignmentStatusMappingStrategy()
	{
		return this.consignmentStatusMappingStrategy;
	}

	@Required
	public void setConsignmentStatusMappingStrategy(
			final OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment> consignmentStatusMappingStrategy)
	{
		this.consignmentStatusMappingStrategy = consignmentStatusMappingStrategy;
	}

	private ModelChangeNotifier<ConsignmentModel> getConsignmentProcessNotifier()
	{
		return this.consignmentProcessNotifier;
	}

	@Required
	public void setConsignmentProcessNotifier(final ModelChangeNotifier<ConsignmentModel> consignmentProcessNotifier)
	{
		this.consignmentProcessNotifier = consignmentProcessNotifier;
	}

	public ConsignmentModel update(final OrderWrapper dto, final Date updateTime)
	{
		return null;
	}

	/**
	 * @return the checkInvoice
	 */
	public MplCheckInvoice getCheckInvoice()
	{
		return checkInvoice;
	}

	/**
	 * @param checkInvoice
	 *           the checkInvoice to set
	 */
	@Required
	public void setCheckInvoice(final MplCheckInvoice checkInvoice)
	{
		this.checkInvoice = checkInvoice;
	}

	/**
	 * @return the timeService
	 */
	public TimeService getTimeService()
	{
		return timeService;
	}

	/**
	 * @param timeService
	 *           the timeService to set
	 */
	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	/**
	 * @return the orderStatusMapping
	 */
	public Map<String, OrderStatus> getOrderStatusMapping()
	{
		return this.orderStatusMapping;
	}

	/**
	 * @param orderStatusMapping
	 *           the orderStatusMapping to set
	 */
	public void setOrderStatusMapping(final Map<String, OrderStatus> orderStatusMapping)
	{
		this.orderStatusMapping = orderStatusMapping;
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
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the customOmsCancelAdapter
	 */
	public CustomOmsCancelAdapter getCustomOmsCancelAdapter()
	{
		return customOmsCancelAdapter;
	}

	/**
	 * @param customOmsCancelAdapter the customOmsCancelAdapter to set
	 */
	public void setCustomOmsCancelAdapter(CustomOmsCancelAdapter customOmsCancelAdapter)
	{
		this.customOmsCancelAdapter = customOmsCancelAdapter;
	}

	/**
	 * @return the customOmsCollectedAdapter
	 */
	public CustomOmsCollectedAdapter getCustomOmsCollectedAdapter()
	{
		return customOmsCollectedAdapter;
	}

	/**
	 * @param customOmsCollectedAdapter the customOmsCollectedAdapter to set
	 */
	public void setCustomOmsCollectedAdapter(CustomOmsCollectedAdapter customOmsCollectedAdapter)
	{
		this.customOmsCollectedAdapter = customOmsCollectedAdapter;
	}
	
	
}
