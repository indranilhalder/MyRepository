/**
 *
 */
package com.tisl.mpl.integration.oms.adapter;


import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.integration.oms.OrderWrapper;
import de.hybris.platform.integration.oms.ShipmentWrapper;
import de.hybris.platform.integration.oms.adapter.DefaultOmsShipmentSyncAdapter;
import de.hybris.platform.integration.oms.mapping.OmsHybrisEnumMappingStrategy;
import de.hybris.platform.omsorders.notification.ModelChangeNotifier;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
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
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
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
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
//import com.tisl.mpl.fulfilmentprocess.events.OrderRefundEvent;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCheckInvoice;
import com.tisl.mpl.marketplacecommerceservices.event.OrderCollectedByPersonEvent;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplaceomsservices.event.SendNotificationEvent;
import com.tisl.mpl.marketplaceomsservices.event.SendNotificationSecondaryStatusEvent;
import com.tisl.mpl.marketplaceomsservices.event.SendUnCollectedOrderToCRMEvent;
import com.tisl.mpl.marketplaceomsservices.event.UnCollectedOrderToInitiateRefundEvent;
import com.tisl.mpl.sms.MplSendSMSService;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;


//import com.tisl.mpl.marketplacecommerceservices.event.ShippingConfirmationEvent;

/**
 * @author TCS
 *
 */
public class CustomOmsShipmentSyncAdapter extends DefaultOmsShipmentSyncAdapter implements
		CustomOmsSyncAdapter<OrderWrapper, ConsignmentModel>
{
	/**
	 *
	 */
	private static final String LOG_MSG_STRING = " ::";
	/**
	 *
	 */
	private static final String COD = "COD";
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
	private CustomOmsCancelAdapter customOmsCancelAdapter;

	@Autowired
	private CustomOmsCollectedAdapter customOmsCollectedAdapter;

	@Autowired
	private MplDeliveryCostService mplDeliveryCostService;
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private BusinessProcessService businessProcessService; // Added for TPR-1348

	@Override
	public ConsignmentModel update(final OrderWrapper wrapper, final ItemModel parent)
	{
		ConsignmentModel consignmentFinal = null;
		final OrderModel orderModel = (OrderModel) parent;
		LOG.info("Going to sync Sub-order :" + orderModel.getCode());
		for (final ShipmentWrapper shipmentWrapper : wrapper.getShipments())
		{
			final Shipment shipment = shipmentWrapper.getShipment();
			if (shipment != null && shipment.getOlqsStatus() != null && shipment.getDeliveryMode() != null)
			{

				if ((shipment.getDeliveryMode().equalsIgnoreCase(MarketplaceomsservicesConstants.CNC))
						&& ((shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.HOTCOURI)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.OTFRDLVY)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.DELIVERD)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.RETTOORG)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.LOSTINTT) || shipment
								.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.UNDLVERD))))
				{

					LOG.debug("Delivery Mode CNC and Order Status  :" + shipment.getOlqsStatus()
							+ " :Not Required to update Consignment");

				}
				else
				{
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

					if (StringUtils.isNotEmpty(line.getAwbSecondaryStatus()) && StringUtils.isNotEmpty(line.getCommunication()))
					{
						LOG.debug(" AwbSecondaryStatus for transaction id :" + line.getAwbSecondaryStatus());
						if (line.getCommunication().equalsIgnoreCase("Y"))
						{
							sendNotification(line, existingConsignmentModel.getAwbSecondaryStatus(), orderModel);
						}

					}

					existingConsignmentModel.setAwbSecondaryStatus(line.getAwbSecondaryStatus());
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
					//TPR-3809 changes start
					existingConsignmentModel.setForwardSealNum(line.getForwardSealNo());
					existingConsignmentModel.setReverseSealNum(line.getReverseSealNo());
					//TPR-3809 changes end



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
					LOG.info("=======After =========Consignment code :  " + existingConsignmentModel.getCode()
							+ " ================ Consignment tracking ID :   " + existingConsignmentModel.getTrackingID());

				}
				else
				{
					LOG.info("ConsignmentModel Not created but Trying to create ConsignmentEntry for Line : " + line.getId());
				}
				if (line.getQcReasonCode() != null
						&& (ConsignmentStatus.QC_FAILED.equals(existingConsignmentModel.getStatus()) || ConsignmentStatus.RETURN_CANCELLED
								.equals(existingConsignmentModel.getStatus())))
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
			if (null != consignmentModel)
			{
				LOG.info("=======Before =========Consignment code :  " + consignmentModel.getCode()
						+ " ================ Consignment tracking ID :   " + consignmentModel.getTrackingID());
			}
			LOG.debug("Delivery Mode CNC and Order Status  :" + shipment.getOlqsStatus() + " :Not Required to update Consignment");
			final ConsignmentStatus shipmentNewStatus = getConsignmentStatusMappingStrategy().getHybrisEnumFromDto(shipment);
			final ConsignmentStatus shipmentCurrentStatus = consignmentModel.getStatus();
			boolean checkConsignmentStatus = false;
			if ((consignmentModel.getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) || consignmentModel.getStatus().equals(
					ConsignmentStatus.ORDER_UNCOLLECTED))
					&& shipmentNewStatus.equals(ConsignmentStatus.CANCELLATION_INITIATED))
			{

				LOG.debug("Calling cancel Initiation process started");
				final SendUnCollectedOrderToCRMEvent sendUnCollectedOrderToCRMEvent = new SendUnCollectedOrderToCRMEvent(shipment,
						consignmentModel, orderModel, shipmentNewStatus, MarketplaceomsordersConstants.TICKET_TYPE_CODE);
				final UnCollectedOrderToInitiateRefundEvent unCollectedOrderToInitiateRefundEvent = new UnCollectedOrderToInitiateRefundEvent(
						shipment, consignmentModel, orderModel, shipmentNewStatus, eventService, configurationService);
				try
				{
					LOG.debug("Create CRM Ticket for Cancel Initiated Orders");
					eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
				}
				catch (final Exception e)
				{
					LOG.error("Exception during CRM Ticket for Cancel Initiated Order Id >> " + orderModel.getCode() + LOG_MSG_STRING
							+ e.getMessage());
				}
				try
				{
					checkConsignmentStatus = true;
					LOG.debug("Refund Initiation  for Cancel Initiated Orders");
					eventService.publishEvent(unCollectedOrderToInitiateRefundEvent);
				}
				catch (final Exception e)
				{
					LOG.error("Exception during Refund Initiation  for Un-Collected Orders >> " + orderModel.getCode()
							+ LOG_MSG_STRING + e.getMessage());
				}

			}

			if (ObjectUtils.notEqual(shipmentCurrentStatus, shipmentNewStatus)
					&& shipmentNewStatus.equals(ConsignmentStatus.ORDER_COLLECTED))
			{
				final OrderProcessModel orderProcessModel = new OrderProcessModel();
				orderProcessModel.setOrder(orderModel);
				final OrderCollectedByPersonEvent orderCollectedByPersonEvent = new OrderCollectedByPersonEvent(orderProcessModel);
				try
				{
					eventService.publishEvent(orderCollectedByPersonEvent);
					sendOrderNotification(shipment, consignmentModel, orderModel, shipmentNewStatus);
				}
				catch (final Exception e1)
				{
					LOG.error("Exception during sending mail or SMS for Order Id:  >> " + orderModel.getCode() + LOG_MSG_STRING
							+ e1.getMessage());
				}

			}
			createRefundEntry(shipment, shipmentNewStatus, consignmentModel, orderModel);
			if (ObjectUtils.notEqual(shipmentCurrentStatus, shipmentNewStatus))
			{
				if (!checkConsignmentStatus)
				{
					//if(shipmentCurrentStatus.equals(ConsignmentStatus.RETURN_INITIATED) && shipmentNewStatus.equals(ConsignmentStatus.DELIVERED) ){
					//	return false;
					//}else{
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
					//}
				}

				//Added TPR-1348
				if ("Y".equalsIgnoreCase(configurationService.getConfiguration().getString(
						MarketplaceomsservicesConstants.AUTO_REFUND_ENABLED))
						&& ConsignmentStatus.RETURN_CLOSED.equals(shipmentNewStatus))
				{
					startAutomaticRefundProcess(orderModel); //Start the new Automatic Process
				}

				return true;
			}
			//R2.3  Start Bug Id TISRLUAT-986 20-02-2017 Start
			try
			{
				LOG.info("CustomOmsShipmentSyncAdapte:::InScan::::" + shipment.getInScan());
				if (shipment.getInScan() != null && shipment.getInScan().booleanValue())
				{
					if (consignmentModel.getIsInscan() == null)
					{
						sendOrderNotification(shipment, consignmentModel, orderModel, shipmentNewStatus);
						consignmentModel.setIsInscan(Boolean.TRUE);
						modelService.saveAll(consignmentModel);
					}
					else if (!consignmentModel.getIsInscan().booleanValue())
					{
						sendOrderNotification(shipment, consignmentModel, orderModel, shipmentNewStatus);
						consignmentModel.setIsInscan(Boolean.TRUE);
						modelService.saveAll(consignmentModel);
					}
				}
			}
			catch (final Exception exception)
			{
				LOG.info("Exception ouccer trigger email " + exception.getMessage());
			}
			//R2.3  Start Bug Id TISRLUAT-986 20-02-2017 END
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

	@Override
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

	@Override
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

		// added for merging TCS_PROD_SUPPORT with SAP
		boolean createHistoryEntry = false;
		for (final OrderHistoryEntryModel entry : ((OrderModel) owner).getHistoryEntries())
		{
			if (consignmentModel.getStatus().toString().equalsIgnoreCase(entry.getDescription())
					&& consignmentModel.getCode().equalsIgnoreCase(entry.getLineId()))
			{
				createHistoryEntry = true;
			}

		}
		if (!createHistoryEntry)
		{


			modelService.save(createHistoryLog(consignmentModel.getStatus().toString(), (OrderModel) owner,
					consignmentModel.getCode()));
		}

		//modelService.save(createHistoryLog(consignmentModel.getStatus().toString(), (OrderModel) owner, consignmentModel.getCode()));
		// added for merging TCS_PROD_SUPPORT with SAP
		getModelService().save(consignmentModel.getShippingAddress());
		saveAndNotifyConsignment(consignmentModel);
		return consignmentModel;
	}

	@Override
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

	@Override
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

	private void createRefundEntryModel(final ConsignmentStatus newStatus, final ConsignmentModel consignmentModel,
			final OrderModel orderModel, final Boolean isEDtoHDCheck, final Boolean isSDBCheck, final Boolean isRetrunInitiatedCheck)
	{
		try
		{
			final AbstractOrderEntryModel orderEntry = consignmentModel.getConsignmentEntries().iterator().next().getOrderEntry();
			RefundEntryModel refundEntryModel = new RefundEntryModel();
			boolean returnAndRefundStatus = false;
			refundEntryModel.setOrderEntry(orderEntry);
			//Create Multiple Refund Entry Models for SDB and IsEDTOHD
			//if (CollectionUtils.isEmpty(flexibleSearchService.getModelsByExample(refundEntryModel)))
			if (isEDtoHDCheck.booleanValue() || isSDBCheck.booleanValue() || isRetrunInitiatedCheck.booleanValue())
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
				else if (isEDtoHDCheck.booleanValue())
				{
					refundEntryModel.setReason(RefundReason.ISEDTOHD);
					refundEntryModel.setStatus(ReturnStatus.ISEDTOHD);
				}
				else if (isSDBCheck.booleanValue())
				{
					refundEntryModel.setReason(RefundReason.ISSDB);
					refundEntryModel.setStatus(ReturnStatus.ISSDB);
				}
				else
				{
					returnAndRefundStatus = true;
					refundEntryModel.setReason(RefundReason.SITEERROR);
					refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
				}
				refundEntryModel.setAction(ReturnAction.IMMEDIATE);

				if (isEDtoHDCheck.booleanValue())
				{
					refundEntryModel.setNotes("IsEDToHD Breach ");
				}
				else if (isSDBCheck.booleanValue())
				{
					refundEntryModel.setNotes("IsSDB Breach ");
				}
				else
				{
					refundEntryModel.setNotes("Return Initiated by Seller Portal");
				}
				refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
				refundEntryModel.setAction(ReturnAction.IMMEDIATE);
				refundEntryModel.setNotes("Return Initiated by Seller Portal");
				refundEntryModel.setExpectedQuantity(orderEntry.getQuantity());//Single line quantity
				refundEntryModel.setReceivedQuantity(orderEntry.getQuantity());//Single line quantity
				refundEntryModel.setRefundedDate(new Date());
				if (isSDBCheck.booleanValue())
				{
					orderEntry.setIsSdb(Boolean.TRUE);
					modelService.save(orderEntry);
				}
				final List<PaymentTransactionModel> tranactions = orderModel.getPaymentTransactions();
				if (CollectionUtils.isNotEmpty(tranactions))
				{
					final PaymentTransactionEntryModel entry = tranactions.iterator().next().getEntries().iterator().next();

					if (isEDtoHDCheck.booleanValue())
					{
						if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
								&& COD.intern().equalsIgnoreCase(entry.getPaymentMode().getMode()))
						{
							refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
						}
						else
						{
							final Double amount = orderEntry.getCurrDelCharge();

							refundEntryModel.setAmount(NumberUtils.createBigDecimal(amount.toString()));
						}

					}
					else if (isSDBCheck.booleanValue())
					{

						if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
								&& COD.intern().equalsIgnoreCase(entry.getPaymentMode().getMode()))
						{
							refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
						}
						else
						{
							final Double amount = orderEntry.getScheduledDeliveryCharge();

							refundEntryModel.setAmount(NumberUtils.createBigDecimal(amount.toString()));
						}
					}
					else
					{
						if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
								&& COD.intern().equalsIgnoreCase(entry.getPaymentMode().getMode()))
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
				}
				if (!returnAndRefundStatus)
				{
					modelService.save(refundEntryModel);
					modelService.save(returnRequestModel);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * @param entry
	 *
	 *           R2.3 for refund info call to oms
	 *
	 */
	private void refundInfoCallToOMS(final AbstractOrderEntryModel orderEntry, final String refundcategoryType)
	{
		PaymentTransactionModel paymentTransactionModel = null;

		final double totalRefundAmount = null != orderEntry.getCurrDelCharge() ? orderEntry.getCurrDelCharge().doubleValue() : 0.0D;
		if (totalRefundAmount > 0D)
		{
			paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel((OrderModel) orderEntry.getOrder(),
					"FAILURE", Double.valueOf(totalRefundAmount), PaymentTransactionType.RETURN, "FAILURE", UUID.randomUUID()
							.toString());
			mplJusPayRefundService.attachPaymentTransactionModel((OrderModel) orderEntry.getOrder(), paymentTransactionModel);
		}
		ConsignmentStatus newStatus = null;
		if (null != orderEntry.getConsignmentEntries())
		{
			newStatus = orderEntry.getConsignmentEntries().iterator().next().getConsignment().getStatus();
		}
		if (paymentTransactionModel != null)
		{
			mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel, orderEntry.getNetAmountAfterAllDisc(),
					newStatus, refundcategoryType); // sending null as no status update needed
		}
	}


	private void createRefundEntry(final Shipment shipment, ConsignmentStatus newStatus, final ConsignmentModel consignmentModel,
			final OrderModel orderModel)
	{
		try
		{
			Boolean isEDtoHDCheck = Boolean.FALSE;
			Boolean isSDBCheck = Boolean.FALSE;
			Boolean isRetrunInitiatedCheck = Boolean.FALSE;
			SendUnCollectedOrderToCRMEvent sendUnCollectedOrderToCRMEvent = null;
			final UnCollectedOrderToInitiateRefundEvent unCollectedOrderToInitiateRefundEvent = new UnCollectedOrderToInitiateRefundEvent(
					shipment, consignmentModel, orderModel, newStatus, eventService, configurationService);
			if (null != shipment && null != shipment.getIsEDtoHD())
			{
				if (shipment.getIsEDtoHD().booleanValue() && (CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
						&& (consignmentModel.getIsEDtoHDCheck() == null || consignmentModel.getIsEDtoHDCheck() == Boolean.FALSE))
				{
					LOG.debug("************************In IsEDtoHD Check .......");
					isEDtoHDCheck = Boolean.TRUE;
					/* Duplicate Return Model is Creating in R2.3 */
					// createRefundEntryModel(newStatus,consignmentModel,orderModel,isEDtoHDCheck,isSDBCheck,isRetrunInitiatedCheck);

					consignmentModel.setIsEDtoHD(Boolean.TRUE);
					consignmentModel.setIsEDtoHDCheck(Boolean.TRUE);
					modelService.save(consignmentModel);
					try
					{
						sendUnCollectedOrderToCRMEvent = new SendUnCollectedOrderToCRMEvent(shipment, consignmentModel, orderModel,
								newStatus, MarketplaceomsordersConstants.TICKET_TYPE_CODE_EDTOHD_SDB);
						LOG.debug("Create CRM Ticket for EDtoHD Order Cancel Initiated ");
						eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
					}
					catch (final Exception e)
					{
						LOG.error("Exception during Create CRM Ticket for EDtoHD Order Cancel Initiated Id  >> " + orderModel.getCode()
								+ LOG_MSG_STRING + e.getMessage());
					}

					final AbstractOrderEntryModel entry = consignmentModel.getConsignmentEntries().iterator().next().getOrderEntry();
					/* R2.3 REFUND INFO CALL TO OMS START */
					try
					{

						final List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(entry.getOrder()
								.getPaymentTransactions());
						boolean flag = false;
						flag = checkIsOrderCod(tranactions);
						if (flag)
						{
							refundInfoCallToOMS(entry, MarketplacecommerceservicesConstants.REFUND_CATEGORY_E);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception occurred while  refund info call to oms " + e.getMessage());
					}
					/* R2.3 REFUND INFO CALL TO OMS END */
					if (MarketplacecommerceservicesConstants.EXPRESS_DELIVERY.equalsIgnoreCase(entry.getMplDeliveryMode()
							.getDeliveryMode().getCode()))
					{
						if (entry.getTransactionID().equalsIgnoreCase(shipment.getShipmentId()))
						{
							final MplZoneDeliveryModeValueModel deliveryModel = mplDeliveryCostService.getDeliveryCost(
									MarketplacecommerceservicesConstants.HOME_DELIVERY, MarketplacecommerceservicesConstants.INR,
									entry.getSelectedUSSID());
							entry.setMplDeliveryMode(deliveryModel);
							entry.setIsEDtoHD(Boolean.TRUE);
							modelService.save(entry);
						}

					}

					isEDtoHDCheck = Boolean.FALSE;
				}
			}
			if (null != shipment && null != shipment.getSdb())
			{

				if (shipment.getSdb().booleanValue() && (CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
						&& (consignmentModel.getSdbCheck() == null || consignmentModel.getSdbCheck() == Boolean.FALSE))
				{

					LOG.debug("************************In SDB Check .......");
					isSDBCheck = Boolean.TRUE;
					/* Duplicate Return Model is Creating in R2.3 */
					//createRefundEntryModel(newStatus,consignmentModel,orderModel,isEDtoHDCheck,isSDBCheck,isRetrunInitiatedCheck);
					consignmentModel.setSdb(Boolean.TRUE);
					consignmentModel.setSdbCheck(Boolean.TRUE);
					modelService.save(consignmentModel);
					/* R2.3 REFUND INFO CALL TO OMS START */
					final AbstractOrderEntryModel entry = consignmentModel.getConsignmentEntries().iterator().next().getOrderEntry();
					try
					{
						final ConsignmentModel consignment = entry.getConsignmentEntries().iterator().next().getConsignment();

						if (consignment.getDeliveryDate() != null)
						{
							newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
						}
						else
						{
							newStatus = ConsignmentStatus.COD_CLOSED_WITHOUT_REFUND;
						}
						final List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(entry.getOrder()
								.getPaymentTransactions());
						boolean flag = false;
						flag = checkIsOrderCod(tranactions);
						if (flag)
						{
							refundInfoCallToOMS(entry, MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception occurred while  refund info call to oms " + e.getMessage());
					}
					/* R2.3 REFUND INFO CALL TO OMS END */
					if (entry.getTransactionID().equalsIgnoreCase(shipment.getShipmentId()))
					{
						final Boolean sdb = Boolean.TRUE;
						LOG.debug("Before setting IsSdb " + entry.getIsSdb());
						LOG.debug("Before setting Sdb " + entry.getSdb());
						entry.setIsSdb(sdb);
						entry.setSdb(sdb);
						modelService.save(entry);
						LOG.debug("Before setting IsSdb " + entry.getIsSdb());
						LOG.debug("Before setting Sdb " + entry.getSdb());
					}
					try
					{
						sendUnCollectedOrderToCRMEvent = new SendUnCollectedOrderToCRMEvent(shipment, consignmentModel, orderModel,
								newStatus, MarketplaceomsordersConstants.TICKET_TYPE_CODE_EDTOHD_SDB);
						LOG.debug("Create CRM Ticket for SDB Order Cancel Initiated ");
						eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
					}
					catch (final Exception e)
					{
						LOG.error("Exception during Create CRM Ticket for SDB Order Cancel Initiated Id  >> " + orderModel.getCode()
								+ LOG_MSG_STRING + e.getMessage());
					}
					isSDBCheck = Boolean.FALSE;
				}
			}
			if (null != shipment && null != shipment.getSsb())
			{

				if (shipment.getSsb().booleanValue() && (CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
						&& (consignmentModel.getSsbCheck() == null || consignmentModel.getSsbCheck() == Boolean.FALSE))
				{
					if (newStatus.equals(ConsignmentStatus.CANCELLATION_INITIATED))
					{
						LOG.debug("Calling cancel Initiation process started");

						try
						{
							sendUnCollectedOrderToCRMEvent = new SendUnCollectedOrderToCRMEvent(shipment, consignmentModel, orderModel,
									newStatus, MarketplaceomsordersConstants.TICKET_TYPE_CODE);
							LOG.debug("Create CRM Ticket for SSB Order Cancel Initiated ");
							eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
						}
						catch (final Exception e)
						{
							LOG.error("Exception during Create CRM Ticket for SSB Order Cancel Initiated Id  >> " + orderModel.getCode()
									+ LOG_MSG_STRING + e.getMessage());
						}
						try
						{
							LOG.debug("Refund Initiation  for SSB Order Cancel Initiated");
							eventService.publishEvent(unCollectedOrderToInitiateRefundEvent);
						}
						catch (final Exception e)
						{
							LOG.error("Exception during Refund Initiation  SSB Order Cancel Initiated  >> " + orderModel.getCode()
									+ LOG_MSG_STRING + e.getMessage());
						}
					}

				}
			}
			if ((ConsignmentStatus.RETURN_INITIATED.equals(newStatus) || ConsignmentStatus.LOST_IN_TRANSIT.equals(newStatus) || ConsignmentStatus.RETURN_TO_ORIGIN
					.equals(newStatus))
					|| (ConsignmentStatus.RETURNINITIATED_BY_RTO.equals(newStatus))
					|| (ConsignmentStatus.QC_FAILED.equals(newStatus))
					|| (ConsignmentStatus.RETURN_CLOSED.equals(newStatus))
					|| (ConsignmentStatus.RETURN_CANCELLED.equals(newStatus))
					&& CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
			{
				LOG.debug("************************In " + newStatus + " Check .......");
				isRetrunInitiatedCheck = Boolean.TRUE;

				RefundEntryModel refundEntryModel = modelService.create(RefundEntryModel.class);
				boolean refundEntryModelExists = false;
				try
				{
					if (null != orderModel.getReturnRequests())
					{
						if (null != orderModel.getReturnRequests())
						{
							for (final ReturnRequestModel returnRequest : orderModel.getReturnRequests())
							{
								if (null != returnRequest.getReturnEntries())
								{
									for (final ReturnEntryModel returnEntry : returnRequest.getReturnEntries())
									{
										if (null != returnEntry.getOrderEntry() && null != returnEntry.getOrderEntry().getTransactionID())
										{
											if (returnEntry.getOrderEntry().getTransactionID().equalsIgnoreCase(shipment.getShipmentId()))
											{
												refundEntryModel = (RefundEntryModel) returnEntry;
												refundEntryModelExists = true;
												break;
											}
										}
									}
								}
							}
						}
					}

					if (!refundEntryModelExists)
					{
						createRefundEntryModel(newStatus, consignmentModel, orderModel, isEDtoHDCheck, isSDBCheck,
								isRetrunInitiatedCheck);
					}
					else if (null != refundEntryModel)
					{
						final String refundMode = shipment.getRefundType();
						if (LOG.isDebugEnabled())
						{
							LOG.debug(" Refund Mode for the order Line id :" + shipment.getShipmentId() + " is " + refundMode);
						}
						if (null != refundMode)
						{
							refundEntryModel.setRefundMode(refundMode);
							modelService.save(refundEntryModel);
						}
					}
				}
				catch (final Exception e)
				{
					LOG.error("Exception occurred while checking return requests for order entry" + shipment.getShipmentId());
				}

				consignmentModel.setReturnInitiateCheck(Boolean.TRUE);
				modelService.save(consignmentModel);
				isRetrunInitiatedCheck = Boolean.FALSE;
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}


	}


	/**
	 * @param tranactions
	 * @return
	 */
	private boolean checkIsOrderCod(final List<PaymentTransactionModel> tranactions)
	{
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(tranactions))
		{
			for (final PaymentTransactionModel transaction : tranactions)
			{
				if (CollectionUtils.isNotEmpty(transaction.getEntries()))
				{
					for (final PaymentTransactionEntryModel entry : transaction.getEntries())
					{
						if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
								&& entry.getPaymentMode().getMode().equalsIgnoreCase(COD.intern()))
						{
							flag = true;
							break;
						}
					}
				}
				if (flag)
				{
					break;
				}
			}
		}
		return flag;
	}

	// R2.3 SendNotification for SecondaryStatus
	private void sendNotification(final OrderLine line, final String oldAwbSecondaryStatus, final OrderModel orderModel)
	{
		LOG.info(" old awbSecondary status " + oldAwbSecondaryStatus + " and new awbSecondary status"
				+ line.getAwbSecondaryStatus() + " for order line id" + line.getOrderLineId());
		if (!line.getAwbSecondaryStatus().equalsIgnoreCase(oldAwbSecondaryStatus)
				&& (MarketplacecommerceservicesConstants.ADDRESS_ISSUE.equalsIgnoreCase(line.getAwbSecondaryStatus()) || MarketplacecommerceservicesConstants.OFD
						.equalsIgnoreCase(line.getAwbSecondaryStatus())))
		{
			LOG.info(" For " + line.getAwbSecondaryStatus()
					+ " old awbSecondary status  and new awbSecondary status are different for order line id" + line.getOrderLineId());
			final SendNotificationSecondaryStatusEvent sendNotificationSecondaryStatusEvent = new SendNotificationSecondaryStatusEvent(
					line.getAwbSecondaryStatus(), line.getOrderLineId(), orderModel);
			eventService.publishEvent(sendNotificationSecondaryStatusEvent);
		}
		else if (!line.getAwbSecondaryStatus().equalsIgnoreCase(oldAwbSecondaryStatus)
				&& (MarketplacecommerceservicesConstants.MIS_ROUTE.equalsIgnoreCase(line.getAwbSecondaryStatus()) || MarketplacecommerceservicesConstants.RTO_INITIATED
						.equalsIgnoreCase(line.getAwbSecondaryStatus())))
		{
			LOG.info(" For " + line.getAwbSecondaryStatus()
					+ " old awbSecondary status  and new awbSecondary status are different for order line id" + line.getOrderLineId());
			sendSecondarySms(line, orderModel);
		}
		LOG.info("AwbSecondaryStatus:::" + line.getAwbSecondaryStatus() + "Order Line ID " + line.getOrderLineId());
	}


	//send sms data For Secondary data R2.3 Change BUG ID E2E 1563
	private void sendSecondarySms(final OrderLine entry, final OrderModel orderModel)
	{
		try
		{
			String mobileNumber = null;
			String content = null;
			final String productName = getProductName(entry.getOrderLineId(), orderModel);
			if (orderModel.getDeliveryAddress() != null)
			{

				mobileNumber = StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()) ? orderModel.getDeliveryAddress()
						.getPhone1() : (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone2()) ? orderModel
						.getDeliveryAddress().getPhone2() : orderModel.getDeliveryAddress().getCellphone());
			}

			if (entry.getAwbSecondaryStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.RTO_INITIATED))
			{
				content = MarketplacecommerceservicesConstants.SMS_MESSAGE_RTO_INITIATED;
			}
			else if (entry.getAwbSecondaryStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.MIS_ROUTE))
			{
				content = MarketplacecommerceservicesConstants.SMS_MESSAGE_MIS_ROUTE.replace(
						MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, productName).replace(
						MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderModel.getCode());
			}
			if (StringUtils.isNotEmpty(mobileNumber))
			{
				final SendSMSRequestData smsRequestData = new SendSMSRequestData();
				smsRequestData.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				smsRequestData.setContent(content);
				smsRequestData.setRecipientPhoneNumber(mobileNumber);
				sendSMSService.sendSMS(smsRequestData);
			}
		}
		catch (final Exception exption)
		{
			LOG.error("CustomOmsShipmentSyncAdapter::::::::::::::Sending Secondary SMS " + exption.getMessage());
		}
	}

	private String getProductName(final String orderLine, final OrderModel orderModel)
	{
		try
		{
			for (final AbstractOrderEntryModel entry : orderModel.getEntries())
			{
				if (orderLine.equalsIgnoreCase(entry.getTransactionID()))
				{
					return entry.getProduct().getName();
				}
			}
		}
		catch (final NullPointerException nullPointer)
		{
			LOG.error("CustomOmsShipmentSyncAdapte::::" + nullPointer.getMessage());
		}

		return null;
	}

	//send sms data For Secondary data R2.3 Change BUG ID E2E 1563 END
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

	//	/**
	//	 * @return the returnService
	//	 */
	//	public ReturnService getReturnService()
	//	{
	//		return returnService;
	//	}
	//
	//	/**
	//	 * @param returnService
	//	 *           the returnService to set
	//	 */
	//	public void setReturnService(final ReturnService returnService)
	//	{
	//		this.returnService = returnService;
	//	}

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
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * return null; }
	 */

	private ModelService getModelService()
	{
		return this.modelService;
	}

	@Override
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	private Converter<Shipment, ConsignmentModel> getOmsShipmentReverseConverter()
	{
		return this.omsShipmentReverseConverter;
	}

	@Override
	public void setOmsShipmentReverseConverter(final Converter<Shipment, ConsignmentModel> omsShipmentReverseConverter)
	{
		this.omsShipmentReverseConverter = omsShipmentReverseConverter;
	}

	private OmsHybrisEnumMappingStrategy<ConsignmentStatus, Shipment> getConsignmentStatusMappingStrategy()
	{
		return this.consignmentStatusMappingStrategy;
	}

	@Override
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

	@Override
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
	 * @param customOmsCancelAdapter
	 *           the customOmsCancelAdapter to set
	 */
	public void setCustomOmsCancelAdapter(final CustomOmsCancelAdapter customOmsCancelAdapter)
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
	 * @param customOmsCollectedAdapter
	 *           the customOmsCollectedAdapter to set
	 */
	public void setCustomOmsCollectedAdapter(final CustomOmsCollectedAdapter customOmsCollectedAdapter)
	{
		this.customOmsCollectedAdapter = customOmsCollectedAdapter;
	}

	//Added for TPR-1348
	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


	private void startAutomaticRefundProcess(final OrderModel orderModel)
	{
		try
		{
			final OrderProcessModel orderProcessModel = (OrderProcessModel) businessProcessService.createProcess(
					"autorefundinitiate-process-" + System.currentTimeMillis(), "autorefundinitiate-process");
			orderProcessModel.setOrder(orderModel);
			businessProcessService.startProcess(orderProcessModel);
			LOG.error("CustomOmsShipmentSyncAdapter: in the CustomOmsShipmentSyncAdapter.startAutomaticRefundProcess() for Order #"
					+ orderModel.getCode());
		}
		catch (final Exception e)
		{
			LOG.error("CustomOmsShipmentSyncAdapter: error creating AutoRefundProcess for Order #" + orderModel.getCode());
		}
	}
	//End for TPR-1348
}