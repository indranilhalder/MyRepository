/**
 *
 */
package com.tisl.mpl.integration.oms.adapter;
import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.omsorders.notification.ModelChangeNotifier;
import de.hybris.platform.ordercancel.CancelDecision;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialStrategy;
import de.hybris.platform.ordercancel.OrderCancelDeniedException;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelRequestExecutor;
import de.hybris.platform.ordercancel.OrderCancelStateMappingStrategy;
import de.hybris.platform.ordercancel.dao.OrderCancelDao;
import de.hybris.platform.ordercancel.exceptions.OrderCancelDaoException;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.model.CRMTicketDetailModel;
import com.tisl.mpl.ordercancel.MplOrderCancelEntry;
import com.tisl.mpl.ordercancel.MplOrderCancelRequest;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.wsdto.TicketMasterXMLData;



/**
 * @author Tech
 *
 */
public class CustomOmsCancelAdapter implements Serializable
{

	private static final Logger LOG = Logger.getLogger(CustomOmsCancelAdapter.class);
	@Autowired
	private TicketCreationCRMservice ticketCreate;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private MplOrderService mplOrderService;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;
	

	private ModelChangeNotifier<ConsignmentModel> consignmentProcessNotifier;
	private TimeService timeService;
	private OrderCancelStateMappingStrategy stateMappingStrategy;
	private OrderCancelRecordsHandler orderCancelRecordsHandler;
	private List<OrderCancelDenialStrategy> cancelDenialStrategies;
	private Map<OrderCancelState, OrderCancelRequestExecutor> requestExecutorsMap;
	private OrderCancelDao orderCancelDao;
	private OrderHistoryService orderHistoryService;
	private UserService userService;
	public static final String TICKET_SUB_TYPE_CODE_ARR="ARR";

	public boolean createTicketInCRM(final String subOrderEntryTransactionId, final String ticketTypeCode,
			final String reasonCode, final String refundType, final OrderModel subOrderModel,boolean isSsb,boolean isSdb,boolean isEdtoHd)
	{
		boolean ticketCreationStatus = false;
		try
		{
			final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
			final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();

			final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntryTransactionId);
			LOG.info(">>createTicketInCRM >> Setting Type of orderEntries :" + orderEntries.size());
			for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
			{
				final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
				sendTicketLineItemData.setLineItemId(abstractOrderEntryModel.getOrderLineId());
				if (ticketTypeCode.equalsIgnoreCase("C"))
				{
					if(isSsb){
						sendTicketRequestData.setTicketSubType(MarketplaceomsordersConstants.TICKET_SUB_TYPE_CODE_SSB);
						sendTicketLineItemData.setCancelReasonCode(MarketplaceomsordersConstants.CRM_SSB_REASON_CODE);
					}else{
   					sendTicketRequestData.setTicketSubType(MarketplaceomsordersConstants.TICKET_SUB_TYPE_CODE);
   					sendTicketLineItemData.setCancelReasonCode(MarketplaceomsordersConstants.CRM_SSB_REASON_CODE);
					}
				}else if(ticketTypeCode.equalsIgnoreCase("A")){
					
					   if(isSdb || isEdtoHd){
						sendTicketRequestData.setTicketSubType(TICKET_SUB_TYPE_CODE_ARR);
						sendTicketLineItemData.setCancelReasonCode(MarketplaceomsordersConstants.CRM_SSB_REASON_CODE);
					   }
				}
				lineItemDataList.add(sendTicketLineItemData);
			}
			//sendTicketRequestData.setCustomerID(customerData.getUid());
			sendTicketRequestData.setRefundType(refundType);
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
			LOG.error(" >> Exception occured while CRM ticket creation"+subOrderModel.getParentReference().getCode(), ex);
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

	public boolean initiateCancellation(final String ticketTypeCode, final String subOrderEntryTrnxId, final OrderModel subOrderModel,
			final String reasonCode, final ConsignmentModel consignmentModel)
	{
		boolean cancellationInitiated = false;

		try
		{
			if ("C".equalsIgnoreCase(ticketTypeCode))
			{
				final MplOrderCancelRequest orderCancelRequest = buildCancelRequest(reasonCode, subOrderModel, subOrderEntryTrnxId);
				LOG.debug("orderCancelRequest refund Amount :"+orderCancelRequest.getAmountToRefund());
				requestOrderCancel(subOrderModel, orderCancelRequest, consignmentModel);
			}
			cancellationInitiated = true;
		}
		catch (final ModelSavingException e)
		{
			LOG.error(" initiateCancellation Exception:"+subOrderModel.getParentReference().getCode() + e.getMessage());
		}
		catch (final Exception ex)
		{
			LOG.error(" initiateCancellation Exception" + ex.getMessage());

		}
		return cancellationInitiated;
	}

	private MplOrderCancelRequest buildCancelRequest(final String reasonCode, final OrderModel subOrderModel,
			final String transactionId) throws OrderCancelException
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
			double deliveryCost = 0D;
			if (orderEntry.getCurrDelCharge() != null)
			{
				deliveryCost = orderEntry.getCurrDelCharge().doubleValue()+orderEntry.getScheduledDeliveryCharge().doubleValue();
			}


			refundAmount = refundAmount + orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost;

			refundAmount = mplJusPayRefundService.validateRefundAmount(refundAmount, subOrderModel);

		}
		//Setting Refund Amount
		orderCancelRequest.setAmountToRefund(new Double(refundAmount));

		return orderCancelRequest;
	}

	private void requestOrderCancel(final OrderModel subOrderModel, final MplOrderCancelRequest orderCancelRequest,
			final ConsignmentModel consignmentModel) throws OrderCancelException
	{
		final OrderCancelRecordEntryModel orderRequestRecord =requestOrderCancel(orderCancelRequest,subOrderModel.getUser());
		LOG.debug("orderRequestRecord**********:"+orderRequestRecord.getCancelResult());
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
			initiateRefund(subOrderModel, orderRequestRecord, consignmentModel);
		}
	}

	
	public OrderCancelRecordEntryModel requestOrderCancel(
			MplOrderCancelRequest orderCancelRequest, PrincipalModel requestor)
			throws OrderCancelException {
		
		OrderCancelRecordEntryModel result;
		CancelDecision cancelDecision = isCancelPossible(
				orderCancelRequest.getOrder(), requestor,
				orderCancelRequest.isPartialCancel(),
				orderCancelRequest.isPartialEntryCancel());
		if (cancelDecision.isAllowed()) {
			 result =createRecordEntry(orderCancelRequest, requestor);
			OrderCancelState currentCancelState = this.stateMappingStrategy
					.getOrderCancelState(orderCancelRequest.getOrder());
			
			OrderCancelRequestExecutor ocre = this.requestExecutorsMap.get(currentCancelState);

			if (ocre == null) {
				throw new IllegalStateException(
						"Cannot find request executor for cancel state: "
								+ currentCancelState.name());
			}
          /*ocre.processCancelRequest(orderCancelRequest, result);
           * For Multiple Orderlies cancel getting  OrderCancel Denied Exception
           * processCancelRequest method will check for duplicated orderline id
           */
			//ocre.processCancelRequest(orderCancelRequest, result);
		} else {
			throw new OrderCancelDeniedException(orderCancelRequest.getOrder()
					.getCode(), cancelDecision);
		}
		
		return result;
	}
	private CancelDecision isCancelPossible(OrderModel order,
			PrincipalModel requestor, boolean partialCancel,
			boolean partialEntryCancel) {
		OrderCancelConfigModel configuration = getConfiguration();
		List reasons = new ArrayList();
		for (OrderCancelDenialStrategy ocas : this.cancelDenialStrategies) {
			OrderCancelDenialReason result = ocas.getCancelDenialReason(
					configuration, order, requestor, partialCancel,
					partialEntryCancel);
			if (result == null)
				continue;
			reasons.add(result);
		}
		return new CancelDecision(true, reasons);
	}
	
	public OrderCancelConfigModel getConfiguration() {
		return this.orderCancelDao.getOrderCancelConfiguration();
	}
	
	private OrderCancelRecordEntryModel createRecordEntry(
			MplOrderCancelRequest request, PrincipalModel requestor)
			throws OrderCancelRecordsHandlerException {
		
		if (request == null) {
			throw new OrderCancelRecordsHandlerException(null,
					"Cancel request cannot be null");
		}
		if (request.getOrder() == null) {
			throw new OrderCancelRecordsHandlerException(null,
					"Cancel request contains no order reference");
		}

		OrderModel order = request.getOrder();

		OrderModel version = this.orderHistoryService
				.createHistorySnapshot(order);
		Map originalOrderEntriesMapping = storeOriginalOrderEntriesMapping(version);

		String description = ((!(request.isPartialCancel())) ? "Full c"
				: "Partial c") + "ancel request for order: " + order.getCode();
		OrderHistoryEntryModel snapshot = createSnaphot(order, version,
				description, requestor);
		try {
			OrderCancelRecordModel cancelRecord = getOrCreateCancelRecord(order);
			cancelRecord.setInProgress(true);
			getModelService().save(cancelRecord);
			return createCancelRecordEntry(request, cancelRecord,
					snapshot, originalOrderEntriesMapping);
		} catch (OrderCancelDaoException e) {
			throw new OrderCancelRecordsHandlerException(order.getCode(), e);
		}
	}
	private Map<Integer, AbstractOrderEntryModel> storeOriginalOrderEntriesMapping(
			OrderModel order) {
		Map mapping = new HashMap(order.getEntries().size());
		for (AbstractOrderEntryModel currentEntry : order.getEntries()) {
			mapping.put(currentEntry.getEntryNumber(), currentEntry);
		}
		return mapping;
	}
	protected OrderCancelRecordModel getOrCreateCancelRecord(OrderModel order)
			throws OrderCancelDaoException {
		OrderCancelRecordModel cancelRecord = this.orderCancelDao
				.getOrderCancelRecord(order);
		if (cancelRecord == null) {
			cancelRecord = createCancelRecord(order);
		}
		return cancelRecord;
	}
	protected OrderCancelRecordModel createCancelRecord(OrderModel order) {
		OrderCancelRecordModel cancelRecord = (OrderCancelRecordModel) getModelService()
				.create(OrderCancelRecordModel.class);
		cancelRecord.setOrder(order);
		cancelRecord.setOwner(order);
		cancelRecord.setInProgress(false);
		getModelService().save(cancelRecord);
		return cancelRecord;
	}
	protected OrderCancelRecordEntryModel createCancelRecordEntry(
			MplOrderCancelRequest request, OrderCancelRecordModel cancelRecord,
			OrderHistoryEntryModel snapshot,
			Map<Integer, AbstractOrderEntryModel> originalOrderEntriesMapping)
			throws OrderCancelRecordsHandlerException {
		OrderCancelRecordEntryModel cancelRecordEntry = (OrderCancelRecordEntryModel) getModelService()
				.create(OrderCancelRecordEntryModel.class);
		cancelRecordEntry.setRefundableAmount(request.getAmountToRefund());
		cancelRecordEntry.setTimestamp(new Date());
		cancelRecordEntry.setCode(generateEntryCode(snapshot));
		cancelRecordEntry.setOriginalVersion(snapshot);
		cancelRecordEntry.setModificationRecord(cancelRecord);
		cancelRecordEntry.setPrincipal(this.userService.getCurrentUser());
		cancelRecordEntry.setOwner(cancelRecord);
		cancelRecordEntry.setStatus(OrderModificationEntryStatus.INPROGRESS);
		cancelRecordEntry
				.setCancelResult((request.isPartialCancel()) ? OrderCancelEntryStatus.PARTIAL
						: OrderCancelEntryStatus.FULL);
		cancelRecordEntry.setCancelReason(request.getCancelReason());
		cancelRecordEntry.setNotes(request.getNotes());
		
		getModelService().save(cancelRecordEntry);

		List orderEntriesRecords = new ArrayList();
		for (OrderCancelEntry cancelRequestEntry : request.getEntriesToCancel()) {
			OrderEntryCancelRecordEntryModel orderEntryRecordEntry = (OrderEntryCancelRecordEntryModel) getModelService()
					.create(OrderEntryCancelRecordEntryModel.class);
			orderEntryRecordEntry.setCode(cancelRequestEntry.getOrderEntry()
					.getPk().toString());
			orderEntryRecordEntry.setCancelRequestQuantity(Integer
					.valueOf((int) cancelRequestEntry.getCancelQuantity()));
			orderEntryRecordEntry.setModificationRecordEntry(cancelRecordEntry);
			orderEntryRecordEntry
					.setOrderEntry((OrderEntryModel) cancelRequestEntry
							.getOrderEntry());
			orderEntryRecordEntry.setOriginalOrderEntry(getOriginalOrderEntry(
					originalOrderEntriesMapping, cancelRequestEntry));
			orderEntryRecordEntry.setNotes(cancelRequestEntry.getNotes());
			orderEntryRecordEntry.setCancelReason(cancelRequestEntry
					.getCancelReason());
			getModelService().save(orderEntryRecordEntry);
			orderEntriesRecords.add(orderEntryRecordEntry);
		}
		cancelRecordEntry
				.setOrderEntriesModificationEntries(orderEntriesRecords);
		getModelService().save(cancelRecordEntry);

		return cancelRecordEntry;
	}
	protected OrderHistoryEntryModel createSnaphot(OrderModel order,
			OrderModel version, String description, PrincipalModel requestor) {
		this.orderHistoryService.saveHistorySnapshot(version);

		OrderHistoryEntryModel historyEntry = (OrderHistoryEntryModel) getModelService()
				.create(OrderHistoryEntryModel.class);
		historyEntry.setOrder(order);
		historyEntry.setPreviousOrderVersion(version);
		historyEntry.setDescription(description);
		historyEntry.setTimestamp(new Date());
		if (requestor instanceof EmployeeModel) {
			historyEntry.setEmployee((EmployeeModel) requestor);
		}
		getModelService().save(historyEntry);
		return historyEntry;
	}
	protected String generateEntryCode(OrderHistoryEntryModel snapshot) {
		return snapshot.getOrder().getCode() + "_v"
				+ snapshot.getPreviousOrderVersion().getVersionID() + "_c";
	}
	
	protected OrderEntryModel getOriginalOrderEntry(
			Map<Integer, AbstractOrderEntryModel> originalOrderEntriesMapping,
			OrderCancelEntry cancelRequestEntry)
			throws OrderCancelRecordsHandlerException {
		try {
			int entryPos = cancelRequestEntry.getOrderEntry().getEntryNumber()
					.intValue();
			return ((OrderEntryModel) originalOrderEntriesMapping.get(Integer
					.valueOf(entryPos)));
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			throw new IllegalStateException(
					"Cloned and original order have different number of entries");
		} catch (Exception e) {
			throw new OrderCancelRecordsHandlerException(cancelRequestEntry
					.getOrderEntry().getOrder().getCode(),
					"Error during getting historical orderEntry", e);
		}
	}

	private void initiateRefund(final OrderModel subOrderModel, final OrderCancelRecordEntryModel orderRequestRecord,
			final ConsignmentModel consignmentModel)
	{

		PaymentTransactionModel paymentTransactionModel = null;
		
		updateConsignmentStatus(consignmentModel, subOrderModel, ConsignmentStatus.CANCELLATION_INITIATED);
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
									refundTransactionMappingModel.setRefundAmount(orderRequestRecord.getRefundableAmount());
									modelService.save(refundTransactionMappingModel);
								}
								else
								{
									newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
								}
								final Double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge()
										: NumberUtils.DOUBLE_ZERO;
								final Double scheduleDeliveryCost = orderEntry.getScheduledDeliveryCharge() != null ? orderEntry.getScheduledDeliveryCharge()
										: NumberUtils.DOUBLE_ZERO;
								orderEntry.setRefundedDeliveryChargeAmt(deliveryCost);
								if(null != orderEntry.getIsEDtoHD() && orderEntry.getIsEDtoHD().booleanValue() && null != orderEntry.getRefundedEdChargeAmt() && orderEntry.getRefundedEdChargeAmt().doubleValue() == 0D){
									double hdDeliveryCharges=0.0D;
									if(null !=orderEntry.getHdDeliveryCharge()) {
										hdDeliveryCharges = orderEntry.getHdDeliveryCharge().doubleValue();
									}
									orderEntry.setRefundedEdChargeAmt(Double.valueOf(deliveryCost.doubleValue()-hdDeliveryCharges));
								}
								orderEntry.setCurrDelCharge(new Double(0D));
								// Added in R2.3 START 
								orderEntry.setRefundedScheduleDeliveryChargeAmt(scheduleDeliveryCost);
								orderEntry.setScheduledDeliveryCharge(new Double(0D));
							   // Added in R2.3 END 
								if (newStatus.equals(ConsignmentStatus.ORDER_CANCELLED))
								{
									orderEntry.setJuspayRequestId(uniqueRequestId);
								}
								modelService.save(orderEntry);
								LOG.debug("****** initiateRefund : Step 3  >>Payment transaction mode is not null >> Calling OMS with status as received from JUSPAY "
										+ newStatus.getCode());

								final double refundAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue()
										+ deliveryCost.doubleValue()+scheduleDeliveryCost.doubleValue();
								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
										Double.valueOf(refundAmount), newStatus,null);
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
	}

	protected void updateConsignmentStatus(final ConsignmentModel consignmentModel, final OrderModel subOrderModel,
			final ConsignmentStatus consignmentStatus)
	{
		consignmentModel.setStatus(consignmentStatus);
		saveAndNotifyConsignment(consignmentModel);
		modelService.save(createHistoryLog(consignmentStatus.toString(), subOrderModel, consignmentModel.getCode()));
		LOG.info("Order History entry created for" + subOrderModel.getCode() + "Line ID :" + consignmentModel.getCode());
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
	private ModelChangeNotifier<ConsignmentModel> getConsignmentProcessNotifier()
	{
		return this.consignmentProcessNotifier;
	}

	@Required
	public void setConsignmentProcessNotifier(final ModelChangeNotifier<ConsignmentModel> consignmentProcessNotifier)
	{
		this.consignmentProcessNotifier = consignmentProcessNotifier;
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
	 * @return the stateMappingStrategy
	 */
	public OrderCancelStateMappingStrategy getStateMappingStrategy()
	{
		return stateMappingStrategy;
	}

	/**
	 * @param stateMappingStrategy the stateMappingStrategy to set
	 */
	public void setStateMappingStrategy(OrderCancelStateMappingStrategy stateMappingStrategy)
	{
		this.stateMappingStrategy = stateMappingStrategy;
	}

	/**
	 * @return the orderCancelRecordsHandler
	 */
	public OrderCancelRecordsHandler getOrderCancelRecordsHandler()
	{
		return orderCancelRecordsHandler;
	}

	/**
	 * @param orderCancelRecordsHandler the orderCancelRecordsHandler to set
	 */
	public void setOrderCancelRecordsHandler(OrderCancelRecordsHandler orderCancelRecordsHandler)
	{
		this.orderCancelRecordsHandler = orderCancelRecordsHandler;
	}

	/**
	 * @return the cancelDenialStrategies
	 */
	public List<OrderCancelDenialStrategy> getCancelDenialStrategies()
	{
		return cancelDenialStrategies;
	}

	/**
	 * @param cancelDenialStrategies the cancelDenialStrategies to set
	 */
	public void setCancelDenialStrategies(List<OrderCancelDenialStrategy> cancelDenialStrategies)
	{
		this.cancelDenialStrategies = cancelDenialStrategies;
	}

	/**
	 * @return the orderCancelDao
	 */
	public OrderCancelDao getOrderCancelDao()
	{
		return orderCancelDao;
	}

	/**
	 * @param orderCancelDao the orderCancelDao to set
	 */
	public void setOrderCancelDao(OrderCancelDao orderCancelDao)
	{
		this.orderCancelDao = orderCancelDao;
	}

	/**
	 * @return the orderHistoryService
	 */
	public OrderHistoryService getOrderHistoryService()
	{
		return orderHistoryService;
	}

	/**
	 * @param orderHistoryService the orderHistoryService to set
	 */
	public void setOrderHistoryService(OrderHistoryService orderHistoryService)
	{
		this.orderHistoryService = orderHistoryService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the requestExecutorsMap
	 */
	public Map<OrderCancelState, OrderCancelRequestExecutor> getRequestExecutorsMap()
	{
		return requestExecutorsMap;
	}

	/**
	 * @param requestExecutorsMap the requestExecutorsMap to set
	 */
	public void setRequestExecutorsMap(Map<OrderCancelState, OrderCancelRequestExecutor> requestExecutorsMap)
	{
		this.requestExecutorsMap = requestExecutorsMap;
	}
}
