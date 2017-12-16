/**
 *
 */
package com.tisl.mpl.ordersync.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.integration.oms.mapping.OmsHybrisEnumMappingStrategy;
import de.hybris.platform.omsorders.notification.ModelChangeNotifier;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.ImeiDetailModel;
import com.tisl.mpl.core.model.InitiateRefundProcessModel;
import com.tisl.mpl.core.model.InvoiceDetailModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.OrderSyncLogModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
import com.tisl.mpl.marketplacecommerceservices.event.OrderCollectedByPersonEvent;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.marketplaceomsservices.event.SendNotificationSecondaryStatusEvent;
import com.tisl.mpl.order.DeliveryAddressDTO;
import com.tisl.mpl.order.DeliveryDTO;
import com.tisl.mpl.order.OrderLineDTO;
import com.tisl.mpl.order.OrderStatusUpdateDTO;
import com.tisl.mpl.order.SellerOrderDTO;
import com.tisl.mpl.order.ShipmentDTO;
import com.tisl.mpl.ordersync.OrderSyncUtility;
import com.tisl.mpl.ordersync.event.SendNotificationEvent;
import com.tisl.mpl.ordersync.event.SendUnCollectedOrderToCRMEvent;
import com.tisl.mpl.ordersync.event.UnCollectedOrderToInitiateRefundEvent;
import com.tisl.mpl.sms.MplSendSMSService;


/**
 * @author TCS
 *
 */
public class OrderSyncUtilityImpl implements OrderSyncUtility
{
	@Autowired
	private OrderModelService orderService;

	private static final Logger LOG = Logger.getLogger(OrderSyncUtilityImpl.class);

	private ModelService modelService;

	@Autowired
	private MplDeliveryCostService mplDeliveryCostService;

	//To Set DeliveryPointOfService of Consignment
	private PointOfServiceService pointOfServiceService;

	//To Set Address in Address Model
	private CommonI18NService commonI18NService;
	//To Set Address in Address Model
	private CustomerNameStrategy customerNameStrategy;

	//To Set Address of Consignment
	private Map<String, ConsignmentStatus> consignmentStatusMapping;

	//To Set Warehouse of Consignment
	private WarehouseService warehouseService;

	//To set Time in Order History
	private TimeService timeService;

	//For save and Notify method
	private ModelChangeNotifier<ConsignmentModel> consignmentProcessNotifier;

	//For Consignment Status Mapping
	private OmsHybrisEnumMappingStrategy<ConsignmentStatus, ShipmentDTO> consignmentStatusMappingStrategy;

	//For Event Firing
	@Autowired
	private EventService eventService;

	//For Event Firing
	private ConfigurationService configurationService;

	@Autowired
	private ReturnService returnService;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private BusinessProcessService businessProcessService; // Added for TPR-1348

	@Autowired
	private MplSendSMSService sendSMSService;

	private Map<String, OrderStatus> orderStatusMapping;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private final StringBuffer callTrace = new StringBuffer(5000);

	private boolean isError = false;

	@Resource(name = "mplVoucherService")
	private MplVoucherService mplVoucherService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.ordersync.OrderSyncUtility#syncOrder(java.util.List)
	 */
	@Override
	public OrderSyncLogModel syncOrder(final InputStream orderSyncXML) throws IOException
	{
		final OrderSyncLogModel log = modelService.create(OrderSyncLogModel.class);

		OrderStatusUpdateDTO orderUpdate = null;
		try
		{


			final JAXBContext jaxbContext = JAXBContext.newInstance(OrderStatusUpdateDTO.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			callTrace.append("Unmarshalling XML:->");
			orderUpdate = (OrderStatusUpdateDTO) jaxbUnmarshaller.unmarshal(orderSyncXML);

			final SellerOrderDTO sellerOrder = orderUpdate.getSellerOrder();
			callTrace.append("Fetching Seller Order:->");
			final String orderUpdateTime = orderUpdate.getOrderUpdateTime();

			if (sellerOrder != null && StringUtils.isNotEmpty(orderUpdateTime))
			{
				OrderModel sOrder = null;
				callTrace.append("Fetch Order From Database:->");
				sOrder = orderService.getOrder(sellerOrder.getSellerOrderId());
				if (!(OrderStatus.CANCELLING.equals(sOrder.getStatus())))
				{
					callTrace.append("Create/Update Consignment");
					updateOrCreateConsignment(sellerOrder, sOrder);

				}

				//update order time if it succeeds
				if (StringUtils.isNotBlank(orderUpdateTime))
				{
					sOrder.setOrderUpdateRemoteTime(dateFormat.parse(orderUpdateTime));
				}
				modelService.save(sOrder);
			}
			else
			{
				isError = true;
				callTrace.append("Missing Seller Order or OrderUpdate Time:->");
			}

		}
		catch (final Exception e)
		{

			callTrace.append("Error in Sync Order" + e.getStackTrace());

		}
		finally
		{
			//TO_DO config read
			if (isError
					&& "Y".equalsIgnoreCase(getConfigurationService().getConfiguration()
							.getString(MarketplacewebservicesConstants.OMS_SYNC_SAVE_LOG,
									MarketplacewebservicesConstants.OMS_SYNC_SAVE_LOG_DEFAULT)))
			{
				try
				{
					if (orderUpdate != null && StringUtils.isNotEmpty(orderUpdate.getSellerOrder().getOrderLine().getOrderLineId())
							&& StringUtils.isNotEmpty(orderUpdate.getSellerOrder().getOrderLine().getShipment().getOlqsStatus()))
					{
						log.setTransactionId(orderUpdate.getSellerOrder().getOrderLine().getOrderLineId() + "_" + new Date().toString());
						log.setOrderStatus(orderUpdate.getSellerOrder().getOrderLine().getShipment().getOlqsStatus());

					}
					else
					{

						callTrace.append("Missing Order Line or Order Line Id Or XML unmarshalling error");
					}

					if (StringUtils.isEmpty(log.getTransactionId()))
					{
						log.setTransactionId(new Date().toString());
					}
					log.setErrorLog(callTrace.toString());
					final JAXBContext jaxbContext = JAXBContext.newInstance(OrderStatusUpdateDTO.class);
					final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
					callTrace.append("Marshalling Object:->");
					final StringWriter sw = new StringWriter();
					jaxbMarshaller.marshal(orderUpdate, sw);
					log.setStatusUpdateXML(sw.toString());
					modelService.save(log);
				}
				catch (final Exception e)
				{
					e.printStackTrace();
					LOG.error("Error while writing Log");
				}
			}
		}
		callTrace.delete(0, callTrace.length() - 1);
		return log;


	}


	/**
	 * @param existingConsignment
	 * @param invoiceNo
	 * @param invoiceUrl
	 * @param orderModel
	 * @return createInvoice
	 */

	private boolean validateAndUpdateInvoice(final ConsignmentModel existingConsignment, final String invoiceNo,
			final String invoiceUrl, final OrderModel orderModel)
	{
		callTrace.append("validateAndUpdateInvoice:->");
		boolean createInvoice = true;
		final InvoiceDetailModel invoice = findMatchedInvoice(orderModel, invoiceNo);
		if (invoice != null)
		{

			createInvoice = updateInvoice(invoice, invoiceNo, invoiceUrl, existingConsignment);

		}


		return createInvoice;
	}

	/**
	 * @param invoice
	 * @param invoiceNo
	 * @param invoiceUrl
	 */

	private boolean updateInvoice(final InvoiceDetailModel invoice, final String invoiceNo, final String invoiceUrl,
			final ConsignmentModel consignment)
	{
		callTrace.append("updateInvoice:->");
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
			callTrace.append("Consignment Status RollBack because of Invoice :" + e.getStackTrace());
			isError = true;
		}
		return createInvoice;

	}

	/**
	 * @param orderModel
	 * @param invoiceNo
	 * @return InvoiceDetailModel
	 */
	private InvoiceDetailModel findMatchedInvoice(final OrderModel orderModel, final String invoiceNo)
	{
		callTrace.append("findMatchedInvoice:->");
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

	@Override
	public void updateOrCreateConsignment(final SellerOrderDTO sellerOrder, final OrderModel childOrder)
	{
		try
		{
			callTrace.append("updateOrCreateConsignment:->");
			final ShipmentDTO shipment = sellerOrder.getOrderLine().getShipment();
			final DeliveryDTO delivery = shipment.getDelivery();
			ConsignmentModel consignmentFinal = null;

			if (shipment != null && shipment.getOlqsStatus() != null && delivery.getDeliveryMode() != null)
			{

				if ((delivery.getDeliveryMode().equalsIgnoreCase(MarketplacewebservicesConstants.CNC))
						&& ((shipment.getOlqsStatus().equalsIgnoreCase(MarketplacewebservicesConstants.HOTCOURI)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplacewebservicesConstants.OTFRDLVY)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplacewebservicesConstants.DELIVERD)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplacewebservicesConstants.RETTOORG)
								|| shipment.getOlqsStatus().equalsIgnoreCase(MarketplacewebservicesConstants.LOSTINTT) || shipment
								.getOlqsStatus().equalsIgnoreCase(MarketplaceomsservicesConstants.UNDLVERD))))
				{

					LOG.debug("Delivery Mode CNC and Order Status  :" + shipment.getOlqsStatus()
							+ " :Not Required to update Consignment");

				}
				else
				{
					final ConsignmentModel existingConsignmentModel = getConsignmentByShipment(shipment.getShipmentId(), childOrder);
					if (existingConsignmentModel == null)

					{

						consignmentFinal = createNewConsignment(shipment, childOrder);


					}
					else if (updateConsignment(shipment, existingConsignmentModel, childOrder))
					{
						consignmentFinal = existingConsignmentModel;
					}
				}
			}
			modelService.refresh(childOrder);
			updateOrderLines(sellerOrder.getOrderLine(), sellerOrder, childOrder);
			//TPR-7448 Starts here
			if (StringUtils.isNotEmpty(shipment.getOlqsStatus())
					&& getConsignmentStatusMapping().get(shipment.getOlqsStatus()).equals(ConsignmentStatus.ORDER_CANCELLED))
			{
				mplVoucherService.removeCPOVoucherInvalidation(childOrder);
			}
			//TPR-7448 Ends here
		}
		catch (final Exception e)
		{
			callTrace.append(e.getStackTrace());
			isError = true;
		}

	}

	private void updateOrderLines(final OrderLineDTO orderLine, final SellerOrderDTO sellerOrder, final OrderModel childOrder)
	{
		try
		{


			final ConsignmentModel existingConsignmentModel = getConsignmentByLine(orderLine, childOrder);

			if (sellerOrder.getSellerOrderStatus() != null)
			{

				updateOrderStatus(sellerOrder.getSellerOrderStatus(), childOrder);
			}
			if (existingConsignmentModel != null)
			{
				ConsignmentEntryModel consigmnetEntry = null;

				if (CollectionUtils.isEmpty(existingConsignmentModel.getConsignmentEntries()))
				{
					consigmnetEntry = createNewConsigmnetEntry(orderLine.getOrderLineId(), childOrder);
					consigmnetEntry.setConsignment(existingConsignmentModel);
					//TO-DO
					consigmnetEntry.setShippedQuantity(Long.valueOf(orderLine.getQuantity()));
					modelService.save(consigmnetEntry);
				}
				else
				{
					for (final ConsignmentEntryModel conEntry : existingConsignmentModel.getConsignmentEntries())
					{
						conEntry.setShippedQuantity(Long.valueOf(orderLine.getQuantity()));
						modelService.save(conEntry);
					}

				}

				if (StringUtils.isNotEmpty(orderLine.getAwbSecondaryStatus()) && StringUtils.isNotEmpty(orderLine.getCommunication()))
				{
					LOG.debug(" AwbSecondaryStatus for transaction id :" + orderLine.getAwbSecondaryStatus());
					if (orderLine.getCommunication().equalsIgnoreCase("Y"))
					{
						sendNotification(orderLine, existingConsignmentModel.getAwbSecondaryStatus(), childOrder);
					}

				}

				existingConsignmentModel.setAwbSecondaryStatus(orderLine.getAwbSecondaryStatus());
				existingConsignmentModel.setTrackingID(orderLine.getAwbNumber());
				if (StringUtils.isNotBlank(orderLine.getEstimatedDelivery()))
				{
					existingConsignmentModel.setEstimatedDelivery(dateFormat.parse(orderLine.getEstimatedDelivery()));
				}
				if (StringUtils.isNotBlank(orderLine.getDeliveryDate()))
				{
					existingConsignmentModel.setDeliveryDate(dateFormat.parse(orderLine.getDeliveryDate()));
				}
				if (StringUtils.isNotBlank(orderLine.getShippingDate()))
				{
					existingConsignmentModel.setShippingDate(dateFormat.parse(orderLine.getShippingDate()));
				}
				existingConsignmentModel.setShipmentStatus(orderLine.getShipmentStatus());
				existingConsignmentModel.setCarrier(orderLine.getLogisticProviderName());
				existingConsignmentModel.setReturnAWBNum(orderLine.getReturnAWBNum());
				existingConsignmentModel.setReturnCarrier(orderLine.getReverseLogisticProviderName());
				//existingConsignmentModel.setReceivedBy(orderLine.getReceivedBy());
				existingConsignmentModel.setCarrier(orderLine.getLogisticProviderName());
				//TPR-3809 changes start
				existingConsignmentModel.setForwardSealNum(orderLine.getForwardSealNo());
				existingConsignmentModel.setReverseSealNum(orderLine.getReverseSealNo());
				//TPR-3809 changes end



				if (StringUtils.isNotEmpty(orderLine.getInvoiceNo()) || StringUtils.isNotEmpty(orderLine.getInvoiceUrl()))
				{
					final boolean isInvoiceToBeCreated = validateAndUpdateInvoice(existingConsignmentModel, orderLine.getInvoiceNo(),
							orderLine.getInvoiceUrl(), childOrder);

					if (isInvoiceToBeCreated)
					{
						createInvoice(orderLine.getInvoiceNo(), orderLine.getInvoiceUrl(), existingConsignmentModel);
					}

				}
				else
				{
					LOG.error("Invoice no or invoice url is null for the order ID:" + orderLine.getShipment().getOrderId());
				}

				saveAndNotifyConsignment(existingConsignmentModel);
				LOG.info("=======After =========Consignment code :  " + existingConsignmentModel.getCode()
						+ " ================ Consignment tracking ID :   " + existingConsignmentModel.getTrackingID());

			}
			else
			{
				LOG.info("ConsignmentModel Not created but Trying to create ConsignmentEntry for Line : "
						+ orderLine.getOrderLineId());
			}
			if (orderLine.getQcReasonCode() != null
					&& (ConsignmentStatus.QC_FAILED.equals(existingConsignmentModel.getStatus()) || ConsignmentStatus.RETURN_CANCELLED
							.equals(existingConsignmentModel.getStatus())))
			{
				updateReturnReason(orderLine, childOrder);
			}
			final AbstractOrderEntryModel orderEntry = getOrderEntryByLine(orderLine.getOrderLineId(), childOrder);
			if (orderEntry != null)
			{
				try
				{


					ImeiDetailModel imeiDetails = orderEntry.getImeiDetail();
					if (imeiDetails == null && orderLine.getSerialNum() != null)
					{
						imeiDetails = getModelService().create(ImeiDetailModel.class);
						imeiDetails.setSerialNum(orderLine.getSerialNum());
						if (orderLine.getIMEINo1() != null || orderLine.getIMEINo2() != null || orderLine.getISBNNo() != null)
						{
							final List<String> identifiers = new ArrayList<String>();
							if (orderLine.getIMEINo1() != null)
							{
								identifiers.add(orderLine.getIMEINo1());
							}
							if (orderLine.getIMEINo2() != null)
							{
								identifiers.add(orderLine.getIMEINo2());
							}
							if (orderLine.getISBNNo() != null)
							{
								identifiers.add(orderLine.getISBNNo());
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
					callTrace.append("Could Not able to update Order From OMS for imeiDetails");
					callTrace.append(e.getStackTrace());
					isError = true;
				}
			}

		}
		catch (final Exception e)
		{
			callTrace.append("Some Issue in Updating order");
			callTrace.append(e.getStackTrace());
			isError = true;
		}


	}

	/**
	 * @param sellerOrderStatus
	 * @param orderModel
	 */
	private void updateOrderStatus(final String sellerOrderStatus, final OrderModel orderModel)
	{
		callTrace.append("updateOrderStatus:->");
		final OrderStatus orderStatus = getOrderStatusMapping().get(sellerOrderStatus);
		orderModel.setStatus(orderStatus);
		modelService.save(orderModel);

	}

	protected ConsignmentModel getConsignmentByShipment(final String shipmentId, final OrderModel orderModel)
	{
		callTrace.append("getConsignmentByShipment:->");
		for (final ConsignmentModel consignment : orderModel.getConsignments())
		{
			if (consignment.getCode().equals(shipmentId))
			{
				return consignment;
			}
		}

		return null;
	}

	protected ConsignmentModel createNewConsignment(final ShipmentDTO shipment, final OrderModel childOrder)
	{
		callTrace.append("createNewConsignment:->");
		final ConsignmentModel consignmentModel = (ConsignmentModel) getModelService().create(ConsignmentModel.class);
		shipment.setLocation(MarketplacecommerceservicesConstants.WAREHOUSE); // HardCode as one WareHouse in Commerce.
		populateConsignmentModel(shipment, consignmentModel, childOrder);

		consignmentModel.getShippingAddress().setOwner(childOrder);
		final ConsignmentEntryModel consigmnetEntry = createNewConsigmnetEntry(consignmentModel.getCode(), childOrder);
		consigmnetEntry.setConsignment(consignmentModel);
		consigmnetEntry.setShippedQuantity(Long.valueOf("1"));
		modelService.save(consigmnetEntry);

		// added for merging TCS_PROD_SUPPORT with SAP
		boolean createHistoryEntry = false;
		for (final OrderHistoryEntryModel entry : childOrder.getHistoryEntries())
		{
			if (consignmentModel.getStatus().toString().equalsIgnoreCase(entry.getDescription())
					&& consignmentModel.getCode().equalsIgnoreCase(entry.getLineId()))
			{
				createHistoryEntry = true;
			}

		}
		if (!createHistoryEntry)
		{


			modelService.save(createHistoryLog(consignmentModel.getStatus().toString(), childOrder, consignmentModel.getCode()));
		}

		// added for merging TCS_PROD_SUPPORT with SAP
		getModelService().save(consignmentModel.getShippingAddress());
		saveAndNotifyConsignment(consignmentModel);
		return consignmentModel;
	}

	private boolean updateConsignment(final ShipmentDTO shipment, final ConsignmentModel consignmentModel,
			final OrderModel orderModel)
	{

		try
		{
			callTrace.append("updateConsignment:->");
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
						shipment, consignmentModel, orderModel, shipmentNewStatus, eventService, getConfigurationService());
				try
				{
					LOG.debug("Create CRM Ticket for Cancel Initiated Orders");
					callTrace.append("publishEvent::sendUnCollectedOrderToCRMEvent->");
					eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
				}
				catch (final Exception e)
				{
					callTrace.append("Exception during CRM Ticket for Cancel Initiated Order Id >>" + e.getStackTrace());
					isError = true;
				}
				try
				{
					checkConsignmentStatus = true;
					LOG.debug("Refund Initiation  for Cancel Initiated Orders");
					callTrace.append("publishEvent::unCollectedOrderToInitiateRefundEvent->");
					eventService.publishEvent(unCollectedOrderToInitiateRefundEvent);
				}
				catch (final Exception e)
				{
					callTrace.append("Exception during Refund Initiation  for Un-Collected Orders >> " + e.getStackTrace());
					isError = true;
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
					callTrace.append("publishEvent::orderCollectedByPersonEvent->");
					eventService.publishEvent(orderCollectedByPersonEvent);

					sendOrderNotification(shipment, consignmentModel, orderModel, shipmentNewStatus);
				}
				catch (final Exception e1)
				{
					callTrace.append("Exception during sending mail or SMS >> " + e1.getMessage());
					isError = true;
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
				if ("Y".equalsIgnoreCase(getConfigurationService().getConfiguration().getString(
						MarketplaceomsservicesConstants.AUTO_REFUND_ENABLED))
						&& ConsignmentStatus.RETURN_CLOSED.equals(shipmentNewStatus) && !isOrderCOD(orderModel)) //Changed for SDI-930
				{
					//TISHS-87
					startAutomaticRefundProcess(orderModel, consignmentModel.getCode()); //Start the new Automatic Process
				}

				return true;
			}
			//R2.3  Start Bug Id TISRLUAT-986 20-02-2017 Start
			try
			{
				LOG.info("CustomOmsShipmentSyncAdapte:::InScan::::" + shipment.getInScan());
				if (shipment.getInScan() != null && Boolean.valueOf(shipment.getInScan()).booleanValue())
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
				callTrace.append("Exception ouccer trigger email " + exception.getMessage());
				isError = true;
			}
			//R2.3  Start Bug Id TISRLUAT-986 20-02-2017 END
		}
		catch (final Exception e)
		{
			callTrace.append("Exception either in consignment update or sending notification " + e.getStackTrace());
			isError = true;
		}


		return false;
	}

	protected void populateConsignmentModel(final ShipmentDTO source, final ConsignmentModel target, final OrderModel childOrder)

	{
		callTrace.append("populateConsignmentModel:->");
		final PointOfServiceModel pointOfService = getPointOfServiceService().getPointOfServiceForName(source.getLocation());
		target.setDeliveryPointOfService(pointOfService);

		final AddressModel addressModel = (AddressModel) getModelService().create(AddressModel.class);
		if (addressModel != null)
		{
			populateAdress(source.getDelivery().getDeliveryAddress(), addressModel);
			target.setShippingAddress(addressModel);
		}

		final ConsignmentStatus consignmentStatus = getConsignmentStatusMapping().get(source.getOlqsStatus());

		target.setStatus(consignmentStatus);

		if (source.getDelivery() != null)
		{
			target.setTrackingID(source.getDelivery().getTrackingID());
			target.setTrackingURL(source.getDelivery().getTrackingUrl());
			try
			{
				if (StringUtils.isNotBlank(source.getDelivery().getActualDeliveryDate()))
				{
					target.setShippingDate(dateFormat.parse(source.getDelivery().getActualDeliveryDate()));
				}
			}
			catch (final ParseException e)
			{
				callTrace.append(e.getStackTrace());
				isError = true;
			}
		}

		final WarehouseModel wareHouse = getWarehouseService().getWarehouseForCode(source.getLocation());
		target.setWarehouse(wareHouse);

		target.setCode(source.getShipmentId());

		if (source.getIsEDtoHD() != null)
		{
			target.setIsEDtoHD(Boolean.valueOf(source.getIsEDtoHD()));
		}
		if (source.getSsb() != null)
		{
			target.setSsb(Boolean.valueOf(source.getSsb()));
		}
		if (source.getReturnInScan() != null)
		{
			target.setReturnInscan(Boolean.valueOf(source.getReturnInScan()));
		}
		if (source.getReturnPickUp() != null)
		{
			target.setReturnPickUp(Boolean.valueOf(source.getReturnPickUp()));
		}
		if (source.getAwbSecondaryStatus() != null)
		{
			target.setAwbSecondaryStatus(source.getAwbSecondaryStatus());
		}
		if (source.getReturnAwbSecondaryStatus() != null)
		{
			target.setReturnAwbSecondaryStatus(source.getReturnAwbSecondaryStatus());
		}
		if (source.getSdb() != null)
		{
			target.setSdb(Boolean.valueOf(source.getSdb()));
		}


		target.setOrder(childOrder);


	}

	private void populateAdress(final DeliveryAddressDTO source, final AddressModel target)
	{
		callTrace.append("populateAdress:->");
		target.setLine1(source.getAddressLine1());
		target.setLine2(source.getAddressLine2());
		target.setTown(source.getCityName());
		target.setPostalcode(source.getPostalZone());

		if (source.getName() != null)
		{
			final String[] names = getCustomerNameStrategy().splitName(source.getName());
			if (names != null)
			{
				target.setFirstname(names[0]);
				target.setLastname(names[1]);
			}
		}

		final CountryModel countryModel = getCountryModel(source.getCountryIso3166Alpha2Code());
		target.setCountry(countryModel);

		final RegionModel regionModel = getRegionModel(countryModel, source.getCountrySubentity());
		target.setRegion(regionModel);


	}

	private CountryModel getCountryModel(final String isocode)
	{
		callTrace.append("getCountryModel:->");
		CountryModel countryModel = null;
		if (isocode != null)
		{
			try
			{
				countryModel = getCommonI18NService().getCountry(isocode);
			}
			catch (final UnknownIdentifierException e)
			{
				isError = true;
				throw new ConversionException("No country with the code " + isocode + " found.", e);

			}
			catch (final AmbiguousIdentifierException e)
			{
				isError = true;
				throw new ConversionException("More than one country with the code " + isocode + " found.", e);

			}

		}
		else
		{
			throw new ConversionException("Isocode for country is missing!");
		}
		return countryModel;
	}

	private RegionModel getRegionModel(final CountryModel countryModel, final String isocode)
	{
		callTrace.append("getRegionModel:->");
		RegionModel regionModel = null;
		if (isocode != null)
		{
			try
			{
				regionModel = getCommonI18NService().getRegion(countryModel, isocode);
			}
			catch (final UnknownIdentifierException localUnknownIdentifierException1)
			{
				try
				{
					regionModel = getCommonI18NService().getRegion(countryModel, countryModel.getIsocode() + "-" + isocode);
				}
				catch (final UnknownIdentifierException e2)
				{
					throw new ConversionException("No region with the code " + isocode + " found.", e2);
				}
			}
			catch (final AmbiguousIdentifierException e)
			{
				isError = true;
				throw new ConversionException("More than one region with the code " + isocode + " found.", e);
			}
		}
		return regionModel;
	}


	private ConsignmentEntryModel createNewConsigmnetEntry(final String lineId, final OrderModel orderModel)
	{
		callTrace.append("createNewConsigmnetEntry:->");
		final ConsignmentEntryModel newConsignmentEntry = (ConsignmentEntryModel) getModelService().create(
				ConsignmentEntryModel.class);

		newConsignmentEntry.setOrderEntry(getOrderEntryByLine(lineId, orderModel));

		newConsignmentEntry.setQuantity(Long.valueOf(1));
		return newConsignmentEntry;
	}

	private void saveAndNotifyConsignment(final ConsignmentModel consignmentModel)
	{
		callTrace.append("saveAndNotifyConsignment:->");
		getModelService().save(consignmentModel);
		callTrace.append("getConsignmentProcessNotifier:->");
		getConsignmentProcessNotifier().notify(consignmentModel);
		LOG.debug("Consignment updated with::" + consignmentModel.getStatus());
	}

	/**
	 * @description Method to Send Order Notification to Customer
	 * @param shipment
	 * @param consignmentModel
	 * @param orderModel
	 * @param shipmentNewStatus
	 */
	private void sendOrderNotification(final ShipmentDTO shipment, final ConsignmentModel consignmentModel,
			final OrderModel orderModel, final ConsignmentStatus shipmentNewStatus)
	{
		try
		{
			callTrace.append("sendOrderNotification:->");
			LOG.info("*************Send Notification Start *******************");
			final SendNotificationEvent sendNotificationEvent = new SendNotificationEvent(shipment, consignmentModel, orderModel,
					shipmentNewStatus);
			LOG.info("*************Send Notification publish event *******************");
			callTrace.append("publishEvent::sendOrderNotification:->");
			eventService.publishEvent(sendNotificationEvent);
			LOG.info("*************Send Notification Done *******************");
		}
		catch (final Exception e)
		{
			callTrace.append("Unable to send Notification..!! " + e.getStackTrace());
			isError = true;
		}


	}

	/**
	 * @param lineId
	 * @param orderModel
	 * @return AbstractOrderEntryModel
	 */

	private AbstractOrderEntryModel getOrderEntryByLine(final String lineId, final OrderModel orderModel)
	{
		callTrace.append("getOrderEntryByLine:->");
		for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
		{
			if (orderEntry.getOrderLineId().equals(lineId))
			{
				return orderEntry;
			}

		}
		return null;
	}

	protected OrderHistoryEntryModel createHistoryLog(final String description, final OrderModel order, final String lineId)
	{
		callTrace.append("createHistoryLog:->");
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(getTimeService().getCurrentTime());
		historyEntry.setOrder(order);
		historyEntry.setLineId(lineId);
		historyEntry.setDescription(description);
		return historyEntry;
	}

	private void createRefundEntry(final ShipmentDTO shipment, ConsignmentStatus newStatus,
			final ConsignmentModel consignmentModel, final OrderModel orderModel)
	{
		try
		{
			callTrace.append("createRefundEntry:->");
			Boolean isEDtoHDCheck = Boolean.FALSE;
			Boolean isSDBCheck = Boolean.FALSE;
			Boolean isRetrunInitiatedCheck = Boolean.FALSE;
			SendUnCollectedOrderToCRMEvent sendUnCollectedOrderToCRMEvent = null;
			final UnCollectedOrderToInitiateRefundEvent unCollectedOrderToInitiateRefundEvent = new UnCollectedOrderToInitiateRefundEvent(
					shipment, consignmentModel, orderModel, newStatus, eventService, configurationService);
			if (null != shipment && null != shipment.getIsEDtoHD())
			{
				if (Boolean.valueOf(shipment.getIsEDtoHD()).booleanValue()
						&& (CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
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
						callTrace.append("publishEvent::sendUnCollectedOrderToCRMEvent->");
						eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
					}
					catch (final Exception e)
					{
						callTrace.append("Exception during Create CRM Ticket for EDtoHD Order Cancel Initiated Id  >> "
								+ e.getStackTrace());
						isError = true;
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
						callTrace.append("Exception occurred while  refund info call to oms " + e.getStackTrace());
						isError = true;
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

				if (Boolean.valueOf(shipment.getSdb()).booleanValue()
						&& (CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
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
						callTrace.append("Exception occurred while  refund info call to oms " + e.getStackTrace());
						isError = true;
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
						callTrace.append("publishEvent::sendUnCollectedOrderToCRMEvent->");
						eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
					}
					catch (final Exception e)
					{
						callTrace.append("Exception during Create CRM Ticket for SDB Order Cancel Initiated Id  >> "
								+ e.getStackTrace());
						isError = true;
					}
					isSDBCheck = Boolean.FALSE;
				}
			}
			if (null != shipment && null != shipment.getSsb())
			{

				if (Boolean.valueOf(shipment.getSsb()).booleanValue()
						&& (CollectionUtils.isNotEmpty(consignmentModel.getConsignmentEntries()))
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
							callTrace.append("publishEvent::sendUnCollectedOrderToCRMEvent->");
							eventService.publishEvent(sendUnCollectedOrderToCRMEvent);
						}
						catch (final Exception e)
						{
							callTrace.append("Exception during Create CRM Ticket for SSB Order Cancel Initiated Id  >> "
									+ e.getStackTrace());
							isError = true;
						}
						try
						{
							LOG.debug("Refund Initiation  for SSB Order Cancel Initiated");
							callTrace.append("publishEvent::unCollectedOrderToInitiateRefundEvent->");
							eventService.publishEvent(unCollectedOrderToInitiateRefundEvent);
						}
						catch (final Exception e)
						{
							callTrace.append("Exception during Refund Initiation  SSB Order Cancel Initiated  >> " + e.getStackTrace());
							isError = true;
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
					callTrace.append("Exception occurred while checking return requests for order entry" + shipment.getShipmentId());
					isError = true;
				}

				consignmentModel.setReturnInitiateCheck(Boolean.TRUE);
				modelService.save(consignmentModel);
				isRetrunInitiatedCheck = Boolean.FALSE;
			}
		}
		catch (final Exception e)
		{
			callTrace.append(e.getStackTrace());
			isError = true;
		}


	}

	/**
	 * @param orderEntry
	 * @param refundcategoryType
	 *
	 *           R2.3 for refund info call to oms
	 *
	 */
	private void refundInfoCallToOMS(final AbstractOrderEntryModel orderEntry, final String refundcategoryType)
	{
		callTrace.append("refundInfoCallToOMS:->");
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

	private void createRefundEntryModel(final ConsignmentStatus newStatus, final ConsignmentModel consignmentModel,
			final OrderModel orderModel, final Boolean isEDtoHDCheck, final Boolean isSDBCheck, final Boolean isRetrunInitiatedCheck)
	{
		try
		{
			callTrace.append("createRefundEntryModel:->");
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
								&& MarketplacewebservicesConstants.COD.equalsIgnoreCase(entry.getPaymentMode().getMode()))
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
								&& MarketplacewebservicesConstants.COD.equalsIgnoreCase(entry.getPaymentMode().getMode()))
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
								&& MarketplacewebservicesConstants.COD.intern().equalsIgnoreCase(entry.getPaymentMode().getMode()))
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
			callTrace.append(e.getStackTrace());
			isError = true;
		}
	}

	private void startAutomaticRefundProcess(final OrderModel orderModel, final String refundTransactionId)
	{
		try
		{
			//TISHS-87
			final InitiateRefundProcessModel initiateRefundProcessModel = (InitiateRefundProcessModel) businessProcessService
					.createProcess("autorefundinitiate-process-" + System.currentTimeMillis(), "autorefundinitiate-process");
			initiateRefundProcessModel.setOrder(orderModel);
			initiateRefundProcessModel.setRefundTransactionId(refundTransactionId);
			businessProcessService.startProcess(initiateRefundProcessModel);
			LOG.error("CustomOmsShipmentSyncAdapter: in the CustomOmsShipmentSyncAdapter.startAutomaticRefundProcess() for Order #"
					+ orderModel.getCode());
		}
		catch (final Exception e)
		{
			callTrace.append("CustomOmsShipmentSyncAdapter: error creating AutoRefundProcess for Order #");
			isError = true;
		}
	}

	/**
	 * @param tranactions
	 * @return flag
	 */
	private boolean checkIsOrderCod(final List<PaymentTransactionModel> tranactions)
	{
		callTrace.append("checkIsOrderCod:->");
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
								&& entry.getPaymentMode().getMode().equalsIgnoreCase(MarketplacewebservicesConstants.COD))
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


	private boolean isOrderCOD(final OrderModel order)
	{
		callTrace.append("isOrderCOD:->");
		final List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(order.getPaymentTransactions());
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
								&& entry.getPaymentMode().getMode().equalsIgnoreCase("COD"))
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


	/**
	 * @param line
	 * @param orderModel
	 * @return ConsignmentModel
	 */

	private ConsignmentModel getConsignmentByLine(final OrderLineDTO line, final OrderModel orderModel)
	{
		callTrace.append("getConsignmentByLine:->");
		for (final ConsignmentModel consignment : orderModel.getConsignments())
		{
			if (consignment.getCode().equals(line.getOrderLineId()))
			{
				return consignment;
			}

		}
		return null;
	}


	// R2.3 SendNotification for SecondaryStatus
	private void sendNotification(final OrderLineDTO line, final String oldAwbSecondaryStatus, final OrderModel orderModel)
	{
		callTrace.append("sendNotification:->");
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
			callTrace.append("publishEvent:->sendNotificationSecondaryStatusEvent");
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


	private void sendSecondarySms(final OrderLineDTO entry, final OrderModel orderModel)
	{
		try
		{
			callTrace.append("sendSecondarySms:->");
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
			callTrace.append("CustomOmsShipmentSyncAdapter::::::::::::::Sending Secondary SMS " + exption.getMessage());
			isError = true;
		}
	}


	private void updateReturnReason(final OrderLineDTO line, final OrderModel orderModel)
	{
		callTrace.append("updateReturnReason:->");
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
	 * @param invoiceNo
	 * @param invoiceUrl
	 * @return InvoiceDetailModel
	 */

	private InvoiceDetailModel createInvoice(final String invoiceNo, final String invoiceUrl, final ConsignmentModel consignment)
	{
		callTrace.append("createInvoice:->");
		final InvoiceDetailModel invoice = getModelService().create(InvoiceDetailModel.class);
		//invoice.setConsignment(consignments);
		invoice.setInvoiceNo(invoiceNo);
		invoice.setInvoiceUrl(invoiceUrl);

		getModelService().save(invoice);
		consignment.setInvoice(invoice);
		getModelService().save(consignment);
		return invoice;
	}

	private String getProductName(final String orderLine, final OrderModel orderModel)
	{
		try
		{
			callTrace.append("getProductName:->");
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
			callTrace.append("CustomOmsShipmentSyncAdapte::::" + nullPointer.getMessage());
			isError = true;
		}

		return null;
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
	 * @return the pointOfServiceService
	 */
	public PointOfServiceService getPointOfServiceService()
	{
		return pointOfServiceService;
	}

	/**
	 * @param pointOfServiceService
	 *           the pointOfServiceService to set
	 */
	public void setPointOfServiceService(final PointOfServiceService pointOfServiceService)
	{
		this.pointOfServiceService = pointOfServiceService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the customerNameStrategy
	 */
	public CustomerNameStrategy getCustomerNameStrategy()
	{
		return customerNameStrategy;
	}

	/**
	 * @param customerNameStrategy
	 *           the customerNameStrategy to set
	 */
	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

	/**
	 * @return the consignmentStatusMapping
	 */
	public Map<String, ConsignmentStatus> getConsignmentStatusMapping()
	{
		return consignmentStatusMapping;
	}

	/**
	 * @param consignmentStatusMapping
	 *           the consignmentStatusMapping to set
	 */
	public void setConsignmentStatusMapping(final Map<String, ConsignmentStatus> consignmentStatusMapping)
	{
		this.consignmentStatusMapping = consignmentStatusMapping;
	}

	/**
	 * @return the warehouseService
	 */
	public WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	/**
	 * @param warehouseService
	 *           the warehouseService to set
	 */
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
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
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	/**
	 * @return the consignmentProcessNotifier
	 */
	public ModelChangeNotifier<ConsignmentModel> getConsignmentProcessNotifier()
	{
		return consignmentProcessNotifier;
	}

	/**
	 * @param consignmentProcessNotifier
	 *           the consignmentProcessNotifier to set
	 */
	public void setConsignmentProcessNotifier(final ModelChangeNotifier<ConsignmentModel> consignmentProcessNotifier)
	{
		this.consignmentProcessNotifier = consignmentProcessNotifier;
	}

	/**
	 * @return the consignmentStatusMappingStrategy
	 */
	public OmsHybrisEnumMappingStrategy<ConsignmentStatus, ShipmentDTO> getConsignmentStatusMappingStrategy()
	{
		return consignmentStatusMappingStrategy;
	}

	/**
	 * @param consignmentStatusMappingStrategy
	 *           the consignmentStatusMappingStrategy to set
	 */
	public void setConsignmentStatusMappingStrategy(
			final OmsHybrisEnumMappingStrategy<ConsignmentStatus, ShipmentDTO> consignmentStatusMappingStrategy)
	{
		this.consignmentStatusMappingStrategy = consignmentStatusMappingStrategy;
	}


	/**
	 * @return the orderStatusMapping
	 */
	public Map<String, OrderStatus> getOrderStatusMapping()
	{
		return orderStatusMapping;
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
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
