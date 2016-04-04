/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.utilities.CodeMasterUtility;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceCancellationController;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.ordercancel.MplOrderCancelRequest;
import com.tisl.mpl.service.MplOrderCancelClientService;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest.OrderLine;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCancellationController;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

/**
 * @author 1006687
 *
 */
public class MarketPlaceDefaultCancellationController extends
		DefaultCancellationController implements
		MarketPlaceCancellationController {

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceDefaultCancellationController.class);

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private MplOrderCancelClientService mplOrderCancelClientService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private TypeService typeService;

	private static final String OMS_BYPASS_KEY = "cscockpit.oms.serviceability.check.bypass";

	protected boolean validateCreateCancellationRequest(OrderModel orderModel,
			Map<TypedObject, Long> cancelableOrderEntries,
			List<ObjectValueContainer> orderEntryCancelRecords)
			throws ValidationException {

		List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
		try {
			super.validateCreateCancellationRequest(orderModel,
					cancelableOrderEntries, orderEntryCancelRecords);
			validateCancelReason(orderModel, orderEntryCancelRecords,
					errorMessages);
			boolean isOMSBypass = configurationService.getConfiguration()
					.getBoolean(OMS_BYPASS_KEY);
			if (!isOMSBypass) {
				validateCancellationatOMS(orderEntryCancelRecords);
			}
		} catch (ValidationException e) {
			if (e.getResourceMessages() != null) {
				errorMessages.addAll(e.getResourceMessages());
			}
			throw new ValidationException(errorMessages);
		} catch (OrderCancelException e) {
			errorMessages.add(new ResourceMessage("cancelRecordEntry.validation.cancelRequestOMS.omsnotresponding"));
			throw new ValidationException(errorMessages);
		}
		return true;
	}

	@Override
	protected MplOrderCancelRequest buildCancelRequest(OrderModel order,
			Map<TypedObject, CancelEntryDetails> entriesToCancel,
			ObjectValueContainer cancelRequest) {
		OrderCancelRequest orderCancelRequest = super.buildCancelRequest(order,
				entriesToCancel, cancelRequest);
		MplOrderCancelRequest result = new MplOrderCancelRequest(order,
				orderCancelRequest.getEntriesToCancel());
		result.setCancelReason(orderCancelRequest.getCancelReason());
		result.setNotes(orderCancelRequest.getNotes());
		if (CollectionUtils.isNotEmpty(orderCancelRequest.getEntriesToCancel())) {
			double refundAmount = 0;

			for (OrderCancelEntry orderCancelEntry : orderCancelRequest
					.getEntriesToCancel()) {
				AbstractOrderEntryModel orderEntry = orderCancelEntry
						.getOrderEntry();
				AbstractOrderModel orderModel = orderEntry.getOrder();

				if (mplJusPayRefundService.isCODOrder(orderModel)) {
					result.setAmountToRefund(NumberUtils.DOUBLE_ZERO);//COD Order
				} else {

					long totalquantity = orderEntry.getQuantity();
					// Returning delivery cost in case of cancellation.
					double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry
							.getCurrDelCharge() : NumberUtils.DOUBLE_ZERO;
					double totalprice = orderEntry.getNetAmountAfterAllDisc();
					long cancelledQuantitiy = orderCancelEntry
							.getCancelQuantity();

					refundAmount += ((totalprice / totalquantity) * cancelledQuantitiy)
							+ deliveryCost;
				}
			}
			result.setAmountToRefund(refundAmount);
		}
		return result;

	}

	@Override
	public TypedObject createPartialOrderCancellationRequest(
			List<ObjectValueContainer> orderEntryCancelRecords,
			ObjectValueContainer cancelRequest) throws OrderCancelException,
			ValidationException {

		
		TypedObject typedObject = super.createPartialOrderCancellationRequest(
				orderEntryCancelRecords, cancelRequest);

		if (typedObject != null
				&& typedObject.getObject() != null
				&& typedObject.getObject() instanceof OrderCancelRecordEntryModel) {
			OrderCancelRecordEntryModel orderCancelRecord = (OrderCancelRecordEntryModel) typedObject
					.getObject();
			PaymentTransactionModel paymentTransactionModel = null;
			if (orderCancelRecord.getRefundableAmount() != null
					&& orderCancelRecord.getRefundableAmount() > NumberUtils.DOUBLE_ZERO) {

				final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
				
				try {
					paymentTransactionModel = mplJusPayRefundService.doRefund(
							orderCancelRecord.getOriginalVersion().getOrder(),
							orderCancelRecord.getRefundableAmount(),
							PaymentTransactionType.CANCEL,uniqueRequestId);
					if (null != paymentTransactionModel) {
						mplJusPayRefundService.attachPaymentTransactionModel(
								orderCancelRecord.getOriginalVersion()
										.getOrder(), paymentTransactionModel);
						if (CollectionUtils.isNotEmpty(orderCancelRecord
								.getOrderEntriesModificationEntries())) {

							for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord
									.getOrderEntriesModificationEntries()) {
								OrderEntryModel orderEntry = modificationEntry
										.getOrderEntry();
								double deliveryCost = orderEntry
										.getCurrDelCharge() != null ? orderEntry
										.getCurrDelCharge()
										: NumberUtils.DOUBLE_ZERO;
								ConsignmentStatus newStatus = null;
								// If CosignmentEnteries are present then update
								// OMS with the state.
								if (orderEntry != null
										/*&& CollectionUtils
												.isNotEmpty(orderEntry
														.getConsignmentEntries())*/) {
									/*
									 * ConsignmentModel consignmentModel =
									 * orderEntry
									 * .getConsignmentEntries().iterator()
									 * .next().getConsignment();
									 */
									if (StringUtils
											.equalsIgnoreCase(
													paymentTransactionModel
															.getStatus(),
													"SUCCESS")) {
										newStatus = ConsignmentStatus.ORDER_CANCELLED;
									} else if (StringUtils
											.equalsIgnoreCase(
													paymentTransactionModel
															.getStatus(),
													"PENDING")) {
										newStatus = ConsignmentStatus.REFUND_INITIATED;
										RefundTransactionMappingModel refundTransactionMappingModel = getModelService()
												.create(RefundTransactionMappingModel.class);
										refundTransactionMappingModel
												.setRefundedOrderEntry(orderEntry);
										refundTransactionMappingModel
												.setJuspayRefundId(paymentTransactionModel
														.getCode());
										refundTransactionMappingModel
												.setCreationtime(new Date());
										refundTransactionMappingModel
												.setRefundType(JuspayRefundType.CANCELLED);
										refundTransactionMappingModel
												.setRefundAmount(orderEntry.getNetAmountAfterAllDisc()
														+deliveryCost);//TISPRO-216 : Refund amount Set in RTM
										getModelService().save(
												refundTransactionMappingModel);
									} else {
										newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
									}
									// Do not save status updates in commerce,
									// rather update it on OMS and let it come
									// in sync.
									// getModelService().save(consignmentModel);
									// newStatus=consignmentModel.getStatus();
									mplJusPayRefundService.makeOMSStatusUpdate(
											orderEntry, newStatus);

								}

								double totalprice = orderEntry
										.getNetAmountAfterAllDisc();
								orderEntry
										.setRefundedDeliveryChargeAmt(deliveryCost);
								orderEntry.setCurrDelCharge(0D);
								getModelService().save(orderEntry);
								mplJusPayRefundService.makeRefundOMSCall(
										orderEntry, paymentTransactionModel,
										totalprice + deliveryCost, newStatus);

							}
						}
					} else {
						LOG.error("Refund Failed");
						//TISSIT-1790 Code addition started
						
						mplJusPayRefundService.createCancelRefundPgErrorEntry(orderCancelRecord, PaymentTransactionType.CANCEL,
								JuspayRefundType.CANCELLED, uniqueRequestId);
						
//						for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord
//								.getOrderEntriesModificationEntries()) {
//							OrderEntryModel orderEntry = modificationEntry
//									.getOrderEntry();
//							if (orderEntry != null) {
//								//mplJusPayRefundService.makeOMSStatusUpdate(orderEntry,ConsignmentStatus.REFUND_IN_PROGRESS);
//								
//								
//								double deliveryCost = orderEntry
//										.getCurrDelCharge() != null ? orderEntry
//										.getCurrDelCharge()
//										: NumberUtils.DOUBLE_ZERO;
//								double totalprice = orderEntry.getNetAmountAfterAllDisc();
//								orderEntry.setRefundedDeliveryChargeAmt(deliveryCost);
//								orderEntry.setCurrDelCharge(0D);
//								getModelService().save(orderEntry);
//								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,totalprice + deliveryCost, ConsignmentStatus.REFUND_IN_PROGRESS);
//								
//							}
//						}
//						//TISSIT-1790 Code addition ended
//						paymentTransactionModel = mplJusPayRefundService
//								.createPaymentTransactionModel(
//										orderCancelRecord.getOriginalVersion()
//												.getOrder(),
//										MarketplaceCockpitsConstants.FAILURE,
//										orderCancelRecord.getRefundableAmount(),
//										PaymentTransactionType.CANCEL,
//										"NO Response FROM PG", uniqueRequestId);
//						mplJusPayRefundService.attachPaymentTransactionModel(
//								orderCancelRecord.getOriginalVersion()
//										.getOrder(), paymentTransactionModel);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					// Setting status as of Pending in case of any exception
					// occurs.
					
					mplJusPayRefundService.createCancelRefundExceptionEntry(orderCancelRecord, PaymentTransactionType.CANCEL,
							JuspayRefundType.CANCELLED, uniqueRequestId);
					
					
//					for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord
//							.getOrderEntriesModificationEntries()) {
//						OrderEntryModel orderEntry = modificationEntry
//								.getOrderEntry();
//						if (orderEntry != null)
//						{
//							// Do not save status updates in commerce, rather
//							// update it on OMS and let it come in sync.
//							
//							double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge(): NumberUtils.DOUBLE_ZERO;
//							double totalprice = orderEntry.getNetAmountAfterAllDisc();
//							orderEntry.setRefundedDeliveryChargeAmt(deliveryCost);
//							orderEntry.setCurrDelCharge(0D);
//							getModelService().save(orderEntry);
//							mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,totalprice + deliveryCost, ConsignmentStatus.REFUND_INITIATED);
//							
//							// TISSIT-1784 Code addition started
//							
//							// Making RTM entry to be picked up by webhook job	
//							RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(RefundTransactionMappingModel.class);
//							refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
//							refundTransactionMappingModel.setJuspayRefundId(paymentTransactionModel.getCode());
//							refundTransactionMappingModel.setCreationtime(new Date());
//							refundTransactionMappingModel.setRefundType(JuspayRefundType.CANCELLED);
//							getModelService().save(refundTransactionMappingModel);
//							// TISSIT-1784 Code addition ended
//						}
//					}
//
//					paymentTransactionModel = mplJusPayRefundService
//							.createPaymentTransactionModel(orderCancelRecord
//									.getOriginalVersion().getOrder(),
//									MarketplaceCockpitsConstants.FAILURE, orderCancelRecord
//											.getRefundableAmount(),
//									PaymentTransactionType.CANCEL,
//									"An Exception Occured.", UUID.randomUUID()
//											.toString());
//					mplJusPayRefundService.attachPaymentTransactionModel(
//							orderCancelRecord.getOriginalVersion().getOrder(),
//							paymentTransactionModel);

				}

			} else {// Case of COD.
				Double refundedAmount = 0D;
				for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord
						.getOrderEntriesModificationEntries()) {
					OrderEntryModel orderEntry = modificationEntry
							.getOrderEntry();
					refundedAmount += orderEntry.getNetAmountAfterAllDisc()
							+ orderEntry.getCurrDelCharge()+orderEntry.getConvenienceChargeApportion();
					// If CosignmentEnteries are present then update OMS with
					// the state.
					if (orderEntry != null
							/*&& CollectionUtils.isNotEmpty(orderEntry
									.getConsignmentEntries())*/) {
						// Do not save status updates in commerce, rather update
						// it on OMS and let it come in sync.
						// ConsignmentModel consignmentModel = orderEntry
						// .getConsignmentEntries().iterator().next()
						// .getConsignment();
						// consignmentModel
						// .setStatus(ConsignmentStatus.CLOSED_ON_CANCELLATION);
						// getModelService().save(consignmentModel);
						mplJusPayRefundService.makeOMSStatusUpdate(orderEntry,
								ConsignmentStatus.CLOSED_ON_CANCELLATION);
					}
				}

				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(orderCancelRecord
								.getOriginalVersion().getOrder(), MarketplaceCockpitsConstants.FAILURE,
								refundedAmount, PaymentTransactionType.CANCEL,
								MarketplaceCockpitsConstants.FAILURE, UUID.randomUUID().toString());
				mplJusPayRefundService.attachPaymentTransactionModel(
						orderCancelRecord.getOriginalVersion().getOrder(),
						paymentTransactionModel);

			}
			orderCancelRecord
					.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
			orderCancelRecord
					.setTransactionCode(paymentTransactionModel != null ? paymentTransactionModel
							.getCode() : "");
			getModelService().save(orderCancelRecord);

			return getCockpitTypeService().wrapItem(orderCancelRecord);
		}

		return typedObject;

	}

	public String getSellerName(String ussid) {

		SellerInformationModel sellerinfoModel = getModelService().create(
				SellerInformationModel.class);
		sellerinfoModel.setSellerArticleSKU(ussid);
		try {
			List<SellerInformationModel> lst = flexibleSearchService
					.getModelsByExample(sellerinfoModel);
			return lst.get(0).getSellerName();
		} catch (ModelNotFoundException e) {
			LOG.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		}
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	private void validateCancelReason(OrderModel orderModel,
			List<ObjectValueContainer> orderEntryCancelRecords,
			List<ResourceMessage> errorMsg) throws ValidationException {
		if (CollectionUtils.isNotEmpty(orderEntryCancelRecords)) {
			for (ObjectValueContainer ovc : orderEntryCancelRecords) {
				ObjectValueContainer.ObjectValueHolder cancelRequestQuantity = getPropertyValue(
						ovc, "OrderCancelEntry.cancelQuantity");
				boolean isCancelling = false;
				if ((cancelRequestQuantity != null)
						&& (cancelRequestQuantity.getCurrentValue() instanceof Long)) {
					long cancelRequestQuantityValue = SafeUnbox
							.toLong((Long) cancelRequestQuantity
									.getCurrentValue());
					if (cancelRequestQuantityValue > 0) {
						isCancelling = true;
					}
				}
				CancelReason reason = (CancelReason) getPropertyValue(ovc,
						"OrderCancelEntry.cancelReason").getCurrentValue();
				if (reason == null && isCancelling) {
					errorMsg.add(new ResourceMessage(
							"cancelRecordEntry.validation.cancelRequestReason.missingReason"));
					throw new ValidationException(null);
				}
			}
		}
	}

	private void validateCancellationatOMS(
			List<ObjectValueContainer> orderEntryCancelRecords)
			throws OrderCancelException {

		try {
			if (CollectionUtils.isNotEmpty(orderEntryCancelRecords)) {
				ObjectValueContainer ov = orderEntryCancelRecords.get(0);
				//removed condition let oms validate for all scenario
				//Set<ConsignmentEntryModel> consignmentEntryModels = ((AbstractOrderEntryModel) ((TypedObject) ov
						//.getObject()).getObject()).getConsignmentEntries();
				//if (CollectionUtils.isNotEmpty(consignmentEntryModels)) {Lets OMS validate......
					MplCancelOrderRequest request = new MplCancelOrderRequest();
					OrderModel orderModel = null;
					for (ObjectValueContainer ovc : orderEntryCancelRecords) {

						ObjectValueContainer.ObjectValueHolder cancelRequestQuantity = getPropertyValue(
								ovc, "OrderCancelEntry.cancelQuantity");
						if ((cancelRequestQuantity != null)
								&& (cancelRequestQuantity.getCurrentValue() instanceof Long)) {
							long cancelRequestQuantityValue = SafeUnbox
									.toLong((Long) cancelRequestQuantity
											.getCurrentValue());

							if (cancelRequestQuantityValue > 0) {
								OrderLine orderLine = new OrderLine();
								TypedObject typedObject = (TypedObject) ovc
										.getObject();
								AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) typedObject
										.getObject();

								orderModel = (OrderModel) orderEntry.getOrder();
								orderLine.setOrderId(orderModel
										.getParentReference().getCode());
								orderLine.setTransactionId(orderEntry
										.getTransactionID());
								orderLine.setReturnCancelFlag("C");
								orderLine.setRequestID(UUID.randomUUID()
										.toString());
								CancelReason reason = (CancelReason) getPropertyValue(
										ovc, "OrderCancelEntry.cancelReason")
										.getCurrentValue();

								orderLine.setReasonCode(CodeMasterUtility
										.getglobalCode(reason.getCode()));
								String notes = (String) getPropertyValue(ovc,
										"OrderCancelEntry.notes")
										.getCurrentValue();
								orderLine.setReturnCancelRemarks(notes);
								request.getOrderLine().add(orderLine);

							}

						}

					}
					if (CollectionUtils.isNotEmpty(request.getOrderLine())) {
						MplOrderIsCancellableResponse response = mplOrderCancelClientService
								.orderCancelDataToOMS(request);
						if (null != response) {
							List<com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse.OrderLine> orderLines = response
									.getOrderLine();
							List<String> errormsg = new ArrayList<String>();
							if (CollectionUtils.isNotEmpty(orderLines)) {
								for (com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse.OrderLine order : orderLines) {

									if (order.isIsCancellable()
											.equalsIgnoreCase("N")) {
										errormsg.add(order.getTransactionId()
												+ " is not cancelleable at OMS-"
												+ order.isRemarks());
									} else{
										//Create Log History TISEE-6301
										createHistoryLog("CANCELLATION_INITIATED", orderModel, order.getTransactionId());
									}
								}

								if (CollectionUtils.isNotEmpty(errormsg)) {
									StringBuilder error = new StringBuilder();
									for (String err : errormsg) {
										error.append(err);
									}
									throw new OrderCancelException(
											orderModel.getCode(),
											error.toString());
								}
							}
						} else {
							throw new OrderCancelException(
									orderModel.getCode(),
									"OMS is not responding");
						}
					}
				//}
			}
		} catch (Exception ex) {
			throw new OrderCancelException("Failed to cancel", ex.getMessage());
		}

	}
	
	private void createHistoryLog(final String description, final OrderModel order, final String lineId)
	{
		final OrderHistoryEntryModel historyEntry = getModelService().create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(Calendar.getInstance().getTime());
		historyEntry.setOrder(order);
		historyEntry.setLineId(lineId);
		historyEntry.setDescription(description);
		getModelService().save(historyEntry);
	}

	@Override
	public boolean isFreebieAvaialble(
			List<ObjectValueContainer> orderEntryCancelRecords) {
		Set<String> freebie = new HashSet<>();
		Set<String> freebieSelected = new HashSet<>();
		for (ObjectValueContainer ovc : orderEntryCancelRecords) {
			TypedObject typedObject = (TypedObject) ovc.getObject();

			long cancelRequestQuantity = SafeUnbox
					.toLong((Long) getPropertyValue(ovc,
							"OrderCancelEntry.cancelQuantity")
							.getCurrentValue());
			AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) typedObject
					.getObject();
			freebie.clear();
			for(AbstractOrderEntryModel e : orderEntry.getOrder().getEntries()){
				if(e.getGiveAway() || e.getIsBOGOapplied()){
					for(String id : e.getParentTransactionID().split(",")){
						freebie.add(id);
					}
					freebie.add(e.getTransactionID());
				}
			}
			
			if (cancelRequestQuantity > 0L) {
				freebieSelected.add(orderEntry.getTransactionID());
				continue;
			}
		}
		int count= 0;
		for(String f : freebie){
			if( freebieSelected.contains(f)){
				count++;
			}
		}
		
		return   (freebie.size() != count && count>0) ;
	}

}
