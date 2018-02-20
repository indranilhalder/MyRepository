/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.utilities.CodeMasterUtility;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceCancellationController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.RefundFomType;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.WalletEnum;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.ordercancel.MplOrderCancelRequest;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.response.QCCard;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
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
import de.hybris.platform.core.model.user.CustomerModel;
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
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

/**
 * @author TCS
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
	

	@Autowired
	private MplMWalletRefundService mplMWalletRefundService;
	
	@Autowired
	private MplOrderService mplOrderService;
	
	@Autowired
	private MplPaymentService mplPaymentService;
	
	@Autowired
	private MplWalletFacade mplWalletFacade;
	
	@Autowired
	private ModelService modelService;

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
					double deliveryCost = 0.0D;
					if(null != orderEntry.getIsEDtoHD() && orderEntry.getIsEDtoHD() && null != orderEntry.getRefundedEdChargeAmt() && orderEntry.getRefundedEdChargeAmt().doubleValue() != 0D) {
							deliveryCost = orderEntry.getHdDeliveryCharge() != null ? orderEntry
									.getHdDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
					}else {
						 deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry
								.getCurrDelCharge() : NumberUtils.DOUBLE_ZERO;
					}
					
						//  Added in R2.3 Returning Schedule Delivery delivery cost in case of cancellation 	START
							deliveryCost+=	orderEntry.getScheduledDeliveryCharge() != null ? orderEntry
									.getScheduledDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
						//  Added in R2.3 Returning Schedule Delivery delivery cost in case of cancellation 	END
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
			if ((orderCancelRecord.getRefundableAmount() != null&& orderCancelRecord.getRefundableAmount() > NumberUtils.DOUBLE_ZERO))  {


//				Mrupee implementation 
				// Done for INC144317893
//				final OrderModel order=orderCancelRecord.getOriginalVersion().getOrder();
				
//				if((null !=order.getIsWallet() &&  WalletEnum.NONWALLET.toString().equalsIgnoreCase(order.getIsWallet().getCode()))||null ==order.getIsWallet())
//				{
				
					final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
					OrderModel order = orderCancelRecord.getOriginalVersion().getOrder();
                    String splitModeinfo = order.getSplitModeInfo();
               
				if(null != splitModeinfo && splitModeinfo.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLIQCASH)){
					String walletId = null;
						final CustomerModel customerModel = (CustomerModel) order.getUser();
						if (null != customerModel && null != customerModel.getCustomerWalletDetail()){
							walletId = customerModel.getCustomerWalletDetail().getWalletId();
						 }
						for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord.getOrderEntriesModificationEntries()) {
							OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
							ConsignmentStatus newStatus = null;
							try{
							final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = cancelOrderEntryForQcPaymentMode(orderEntry.getTransactionID(), walletId);
						   	 constructQuickCilverOrderEntry(orderEntry.getTransactionID(), order, walletCardApportionDetailModelList,orderCancelRecord);
						   	if (CollectionUtils.isNotEmpty(orderCancelRecord.getOrderEntriesModificationEntries())){
								for (final OrderEntryModificationRecordEntryModel cancelModificationEntry : orderCancelRecord.getOrderEntriesModificationEntries()){
									final OrderEntryModel cancelOrderEntry = cancelModificationEntry.getOrderEntry();
									
									if (orderEntry != null){
										if (StringUtils.equalsIgnoreCase(cancelOrderEntry.getWalletApportionReturnInfo().getStatus(),MarketplacecommerceservicesConstants.SUCCESS)){
											newStatus = ConsignmentStatus.ORDER_CANCELLED;
										}else if (StringUtils.equalsIgnoreCase(cancelOrderEntry.getWalletApportionReturnInfo().getStatus(),MarketplacecommerceservicesConstants.PENDING)){
											newStatus = ConsignmentStatus.REFUND_INITIATED;
										}else{
											newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
										}
										modelService.save(cancelOrderEntry);
										LOG.debug("****** initiateRefund : Step 3  >>Payment transaction mode is not null >> Calling OMS with status as received from JUSPAY "
												+ newStatus.getCode());
										mplJusPayRefundService.makeRefundOMSCall(orderEntry, null,
												Double.valueOf(orderCancelRecord.getRefundableAmount()), newStatus, null);
									}
								}
							}
							}catch(Exception e){
								e.getMessage();
								LOG.error("Quck Cilver giving  Order Id :"+ order.getCode());
								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,Double.valueOf(orderCancelRecord.getRefundableAmount()), newStatus,null);
							}
						}
				}else if(null != splitModeinfo && splitModeinfo.equalsIgnoreCase(MarketplacecommerceservicesConstants.SPLIT)){
					String walletId = null;
					double juspayTotalAmount =0.0D;
					WalletApportionReturnInfoModel returnModel = null;
					final CustomerModel customerModel = (CustomerModel) order.getUser();
					if (null != customerModel && null != customerModel.getCustomerWalletDetail()){
						walletId = customerModel.getCustomerWalletDetail().getWalletId();
					 }
					for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord.getOrderEntriesModificationEntries()) {
						OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
						try{
							final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = cancelOrderEntryForQcPaymentMode( orderEntry.getTransactionID(), walletId);
							returnModel = constructQuickCilverOrderEntryForSplit(walletCardApportionDetailModelList,orderEntry.getTransactionID(), order,orderEntry);
						}catch(Exception e){
							LOG.error("Refund updation data failed");
						}
					}
					//Juspay Amount calculation ....
					for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord.getOrderEntriesModificationEntries()) {
						OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
						juspayTotalAmount +=calculateSplitJuspayRefundAmount(orderEntry);
					}
					try{
						paymentTransactionModel = mplJusPayRefundService.doRefund(
								orderCancelRecord.getOriginalVersion().getOrder(),
								juspayTotalAmount,
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
									if(null != orderEntry && null != orderEntry.getWalletApportionReturnInfo()){
										saveQCandJuspayResponse(orderEntry, paymentTransactionModel, orderEntry.getWalletApportionReturnInfo());	
									}
									
									ConsignmentStatus newStatus = null;
									if (orderEntry != null) {
										try{
											if(CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries())){	
												ConsignmentModel consignmentModel = orderEntry.getConsignmentEntries().iterator().next().getConsignment();
												consignmentModel.setRefundDetails(RefundFomType.AUTOMATIC);
												getModelService().save(consignmentModel);
											}
											
										}catch(Exception e ){
											LOG.error("Refund updation data failed");
										}
										if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),"SUCCESS")) {
											newStatus = ConsignmentStatus.ORDER_CANCELLED;
										} else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),"PENDING")) {
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
													.setRefundAmount(juspayTotalAmount);
											getModelService().save(
													refundTransactionMappingModel);
										} else {
											newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
										}
									}
									//Start TISPRD-871
									if(newStatus.equals(ConsignmentStatus.ORDER_CANCELLED)){
										orderEntry.setJuspayRequestId(uniqueRequestId);
									}
									//End TISPRD-871
									
									getModelService().save(orderEntry);
									mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,juspayTotalAmount, newStatus,null);
								}
							}
						}else {
							LOG.error("Refund Failed");
							//TISSIT-1790 Code addition started
							mplJusPayRefundService.createCancelRefundPgErrorEntry(orderCancelRecord, PaymentTransactionType.CANCEL,
									JuspayRefundType.CANCELLED, uniqueRequestId);
						}
						
					}catch(Exception e){
						LOG.error("Refund updation data failed");
						mplJusPayRefundService.createCancelRefundExceptionEntry(orderCancelRecord, PaymentTransactionType.CANCEL,
								JuspayRefundType.CANCELLED, uniqueRequestId);
					}
				}else{
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
								
								double currDeliveryCharges = orderEntry
										.getCurrDelCharge() != null ? orderEntry
										.getCurrDelCharge()
										: NumberUtils.DOUBLE_ZERO;
										double deliveryCost  = 0.0D;
										if(null != orderEntry.getIsEDtoHD() && orderEntry.getIsEDtoHD() && null != orderEntry.getRefundedEdChargeAmt() && orderEntry.getRefundedEdChargeAmt().doubleValue() != 0D) {
											deliveryCost=orderEntry.getHdDeliveryCharge() != null ? orderEntry
													.getHdDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
										}else {
											deliveryCost = orderEntry.getCurrDelCharge();
										}
//								double deliveryCost = orderEntry
//										.getCurrDelCharge() != null ? orderEntry
//										.getCurrDelCharge()
//										: NumberUtils.DOUBLE_ZERO;
										// Added in R2.3 START  
								double	scheduleDeliveryCost=orderEntry
												.getScheduledDeliveryCharge() != null ? orderEntry
														.getScheduledDeliveryCharge()
														: NumberUtils.DOUBLE_ZERO;
										// Added in R2.3 END 
								ConsignmentStatus newStatus = null;
								// If CosignmentEnteries are present then update
								// OMS with the state.
								if (orderEntry != null
										/*&& CollectionUtils
												.isNotEmpty(orderEntry
														.getConsignmentEntries())*/) {
									
									//h2refund Added to know the refund type from CSCOCKPIT
									
									try{
										
										if(CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries())){
											
											ConsignmentModel consignmentModel = orderEntry.getConsignmentEntries().iterator().next().getConsignment();
											consignmentModel.setRefundDetails(RefundFomType.AUTOMATIC);
											getModelService().save(consignmentModel);
											
										}
										
									}catch(Exception e ){
										LOG.error("Refund updation data failed");
									}
																		
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
														+deliveryCost+scheduleDeliveryCost);//TISPRO-216 : Refund amount Set in RTM
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
									//TISPRD-1827
									//mplJusPayRefundService.makeOMSStatusUpdate(orderEntry, newStatus);

								}

								double totalprice = orderEntry.getNetAmountAfterAllDisc();
								orderEntry.setRefundedDeliveryChargeAmt(currDeliveryCharges);
								if(null != orderEntry.getIsEDtoHD() && orderEntry.getIsEDtoHD() && null != orderEntry.getRefundedEdChargeAmt() && orderEntry.getRefundedEdChargeAmt().doubleValue() == 0D){
									double hdDeliveryCharges=0.0D;
									if(null !=orderEntry.getHdDeliveryCharge() ) {
										hdDeliveryCharges = orderEntry.getHdDeliveryCharge();
									}
									orderEntry.setRefundedEdChargeAmt(currDeliveryCharges - hdDeliveryCharges);
								}
								
								orderEntry.setCurrDelCharge(0D);
								
								orderEntry.setRefundedScheduleDeliveryChargeAmt(scheduleDeliveryCost);
								orderEntry.setScheduledDeliveryCharge(0D);
								//Start TISPRD-871
								if(newStatus.equals(ConsignmentStatus.ORDER_CANCELLED)){
									orderEntry.setJuspayRequestId(uniqueRequestId);
								}
								//End TISPRD-871
								
								getModelService().save(orderEntry);
								mplJusPayRefundService.makeRefundOMSCall(
										orderEntry, paymentTransactionModel,
										totalprice + deliveryCost+scheduleDeliveryCost, newStatus,null);

							}
						}
					} else {
						LOG.error("Refund Failed");
						//TISSIT-1790 Code addition started
						
						mplJusPayRefundService.createCancelRefundPgErrorEntry(orderCancelRecord, PaymentTransactionType.CANCEL,
								JuspayRefundType.CANCELLED, uniqueRequestId);
						
					}
					
				
					
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					// Setting status as of Pending in case of any exception
					// occurs.
					
					mplJusPayRefundService.createCancelRefundExceptionEntry(orderCancelRecord, PaymentTransactionType.CANCEL,
							JuspayRefundType.CANCELLED, uniqueRequestId);
				}
			}
				// Done for INC144317893
//			}
//				else if(null !=order.getIsWallet() &&  WalletEnum.MRUPEE.toString().equalsIgnoreCase(order.getIsWallet().getCode())){
//					final String uniqueRequestId = mplMWalletRefundService.getRefundUniqueRequestId();
//					try {
//						paymentTransactionModel = mplMWalletRefundService.doRefund(
//								orderCancelRecord.getOriginalVersion().getOrder(),
//								orderCancelRecord.getRefundableAmount(),
//								PaymentTransactionType.CANCEL,uniqueRequestId);
//						if (null != paymentTransactionModel) {
//							mplJusPayRefundService.attachPaymentTransactionModel(
//									orderCancelRecord.getOriginalVersion()
//											.getOrder(), paymentTransactionModel);
//							if (CollectionUtils.isNotEmpty(orderCancelRecord
//									.getOrderEntriesModificationEntries())) {
//
//								for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord
//										.getOrderEntriesModificationEntries()) {
//									OrderEntryModel orderEntry = modificationEntry
//											.getOrderEntry();
//									double deliveryCost = orderEntry
//											.getCurrDelCharge() != null ? orderEntry
//											.getCurrDelCharge()
//											: NumberUtils.DOUBLE_ZERO;
//											
//									ConsignmentStatus newStatus = null;
//									// Added in R2.3 START  
//									double	scheduleDeliveryCost=orderEntry
//													.getScheduledDeliveryCharge() != null ? orderEntry
//															.getScheduledDeliveryCharge()
//															: NumberUtils.DOUBLE_ZERO;
//											// Added in R2.3 END 
//									
//									// If CosignmentEnteries are present then update
//									// OMS with the state.
//									if (orderEntry != null) {
//										if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
//												MarketplacecommerceservicesConstants.SUCCESS))
//										{
//											newStatus = ConsignmentStatus.ORDER_CANCELLED;
//										}
//										else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
//												MarketplacecommerceservicesConstants.FAILURE))
//										{
//											newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
//										}
//										else
//										{
//											newStatus = ConsignmentStatus.REFUND_INITIATED;
//										}
//									}
//
//
//									double totalprice = orderEntry.getNetAmountAfterAllDisc();
//									orderEntry.setRefundedDeliveryChargeAmt(deliveryCost);
//									orderEntry.setCurrDelCharge(0D);
//									
//									orderEntry.setRefundedScheduleDeliveryChargeAmt(scheduleDeliveryCost);
//									orderEntry.setScheduledDeliveryCharge(0D);
//									getModelService().save(orderEntry);
////									mplJusPayRefundService.makeRefundOMSCall(
////											orderEntry, paymentTransactionModel,
////											totalprice + deliveryCost, newStatus);
//									//R2.3 Techout changes
//									mplJusPayRefundService.makeRefundOMSCall(
//											orderEntry, paymentTransactionModel,
//											totalprice + deliveryCost+scheduleDeliveryCost, newStatus,null);
//								}
//							}
//						} else {
//							LOG.error("Refund Failed");
//							mplMWalletRefundService.createCancelRefundPgErrorEntry(orderCancelRecord, PaymentTransactionType.CANCEL, uniqueRequestId);
//						}
//					} catch (Exception e) {
//						LOG.error(e.getMessage(), e);
//						mplMWalletRefundService.createCancelRefundExceptionEntry(orderCancelRecord, PaymentTransactionType.CANCEL, uniqueRequestId);
//					}
//				}

			} else {// Case of COD.
				Double refundedAmount = 0D;
				for (OrderEntryModificationRecordEntryModel modificationEntry : orderCancelRecord
						.getOrderEntriesModificationEntries()) {
					OrderEntryModel orderEntry = modificationEntry
							.getOrderEntry();
                                        ///TISPRDT-697
					/*refundedAmount += orderEntry.getNetAmountAfterAllDisc()
							+ orderEntry.getCurrDelCharge()+orderEntry.getScheduledDeliveryCharge()+orderEntry.getConvenienceChargeApportion();*/

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
						/*mplJusPayRefundService.makeOMSStatusUpdate(orderEntry,
								ConsignmentStatus.CLOSED_ON_CANCELLATION);*/
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
	
	
	 private List<WalletCardApportionDetailModel> cancelOrderEntryForQcPaymentMode(String transactionId,String walletId){
		 final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();
			QCRedeeptionResponse response = null;
			final DecimalFormat decimalFormat = new DecimalFormat("#.00");
		 try
			{
				final AbstractOrderEntryModel abstractOrderEntryModel = mplOrderService.getEntryModel(transactionId);
				if (null != abstractOrderEntryModel && null != abstractOrderEntryModel.getWalletApportionPaymentInfo())
				{
					for (final WalletCardApportionDetailModel cardApportionDetail : abstractOrderEntryModel
							.getWalletApportionPaymentInfo().getWalletCardList())
					{
						double qcCliqCashAmt = 0.0D;
						if (null != cardApportionDetail && null != cardApportionDetail.getBucketType())
						{
							if (!cardApportionDetail.getBucketType().equalsIgnoreCase("CASHBACK"))
							{
								qcCliqCashAmt = Double.parseDouble(cardApportionDetail.getQcApportionValue())
								+ Double.parseDouble(null != cardApportionDetail.getQcDeliveryValue() ? cardApportionDetail
										.getQcDeliveryValue() : "" + 0)
								+ Double.parseDouble(null != cardApportionDetail.getQcSchedulingValue() ? cardApportionDetail
										.getQcSchedulingValue() : "" + 0)
								+ Double.parseDouble(null != cardApportionDetail.getQcShippingValue() ? cardApportionDetail
										.getQcShippingValue() : "" + 0);
								final QCCreditRequest qcCreditRequest = new QCCreditRequest();
								qcCreditRequest.setAmount(decimalFormat.format(qcCliqCashAmt));
								qcCreditRequest.setInvoiceNumber(mplPaymentService.createQCPaymentId());
								qcCreditRequest.setNotes("Cancel for " + decimalFormat.format(qcCliqCashAmt));
								response = mplWalletFacade.qcCredit(walletId, qcCreditRequest);
								walletCardApportionDetailModelList.add(getQcWalletCardResponse(response, cardApportionDetail));
								LOG.debug("Quck Cilver giving response code " + response.getResponseCode() + " TransactionId Id :"
										+ transactionId);
							}
						}
					}
				}
				
			}
			catch (final Exception e)
			{
				e.getMessage();
				LOG.error("Quck Cilver giving response code " + response.getResponseCode() + " TransactionId Id :"	+ transactionId);
			}
		 return walletCardApportionDetailModelList;
	 }
	 
		private WalletCardApportionDetailModel getQcWalletCardResponse(final QCRedeeptionResponse response,
				final WalletCardApportionDetailModel walletObject)
		{
			final WalletCardApportionDetailModel walletCardApportionDetailModel =  modelService.create(WalletCardApportionDetailModel.class);
			if (null != response && null != response.getCards())
			{
				for (final QCCard qcCard : response.getCards())
				{
					walletCardApportionDetailModel.setCardNumber(qcCard.getCardNumber());
					walletCardApportionDetailModel.setCardExpiry(qcCard.getExpiry());
					walletCardApportionDetailModel.setCardAmount(qcCard.getAmount().toString());
					walletCardApportionDetailModel.setBucketType(qcCard.getBucketType());
				}
				if (StringUtils.equalsIgnoreCase(response.getResponseCode().toString(), "0"))
				{
					walletCardApportionDetailModel.setTrnsStatus("SUCCESS");
				}
				else
				{
					walletCardApportionDetailModel.setTrnsStatus("PENDING");
				}
				walletCardApportionDetailModel.setTransactionId(response.getTransactionId().toString());
				walletCardApportionDetailModel.setQcApportionValue(walletObject.getQcApportionValue());
				walletCardApportionDetailModel.setQcDeliveryValue(walletObject.getQcDeliveryValue());
				walletCardApportionDetailModel.setQcSchedulingValue(walletObject.getQcSchedulingValue());
				walletCardApportionDetailModel.setQcShippingValue(walletObject.getQcShippingValue());
			}else{
				walletCardApportionDetailModel.setTrnsStatus("SUCCESS");
				walletCardApportionDetailModel.setTransactionId("0");
				walletCardApportionDetailModel.setQcApportionValue("0");
				walletCardApportionDetailModel.setQcDeliveryValue("0");
				walletCardApportionDetailModel.setQcSchedulingValue("0");
				walletCardApportionDetailModel.setQcShippingValue("0");
			}

			return walletCardApportionDetailModel;
		}
		
		private void constructQuickCilverOrderEntry(final String transactionId, final OrderModel subOrderModel,
				final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList,
				final OrderCancelRecordEntryModel orderRequestRecord)
		{

			if (CollectionUtils.isNotEmpty(orderRequestRecord.getOrderEntriesModificationEntries()))
			{
				for (final OrderEntryModificationRecordEntryModel modificationEntry : orderRequestRecord
						.getOrderEntriesModificationEntries())
				{
					final OrderEntryModel abstractOrderEntryModel = modificationEntry.getOrderEntry();

					final WalletApportionReturnInfoModel walletApportionReturnModel = getModelService().create(
							WalletApportionReturnInfoModel.class);
					final List<String> qcResponseStatus = new ArrayList<String>();
					if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
							&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue())
					{
						walletApportionReturnModel.setQcApportionPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
								.getQcApportionPartValue());
					}
					if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
							&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue())
					{
						walletApportionReturnModel.setQcDeliveryPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
								.getQcDeliveryPartValue());
					}
					if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
							&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue())
					{
						walletApportionReturnModel.setQcSchedulingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
								.getQcSchedulingPartValue());
					}
					if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
							&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue())
					{
						walletApportionReturnModel.setQcShippingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
								.getQcShippingPartValue());
					}

					if (null != walletCardApportionDetailModelList && walletCardApportionDetailModelList.size() > 0)
					{

						for (final WalletCardApportionDetailModel walletCardApportionDetailModelObj : walletCardApportionDetailModelList){
							qcResponseStatus.add(walletCardApportionDetailModelObj.getTrnsStatus());
							createPaymentEntryForQCTransaction(subOrderModel, walletCardApportionDetailModelObj);
						}
					}
					walletApportionReturnModel.setWalletCardList(walletCardApportionDetailModelList);
					walletApportionReturnModel.setTransactionId(transactionId);
					walletApportionReturnModel.setType("CANCEL");
					if (qcResponseStatus.contains("PENDING"))
					{
						walletApportionReturnModel.setStatusForQc("PENDING");
					}
					else
					{
						walletApportionReturnModel.setStatusForQc("SUCCESS");
					}
					modelService.save(walletApportionReturnModel);
					abstractOrderEntryModel.setWalletApportionReturnInfo(walletApportionReturnModel);
					modelService.save(abstractOrderEntryModel);

				}

			}
		}
		private void createPaymentEntryForQCTransaction(final OrderModel subOrderModel,
				final WalletCardApportionDetailModel walletCardApportionDetailModel)
		{

			final PaymentTransactionModel paymentTransactionModel = modelService.create(PaymentTransactionModel.class);
			paymentTransactionModel.setCode(walletCardApportionDetailModel.getTransactionId().toString());
			paymentTransactionModel.setRequestId(walletCardApportionDetailModel.getTransactionId().toString());
			paymentTransactionModel.setStatus(walletCardApportionDetailModel.getTrnsStatus());
			paymentTransactionModel.setOrder(subOrderModel);
			final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService.create(PaymentTransactionEntryModel.class);
			paymentTransactionEntryModel.setCode(walletCardApportionDetailModel.getTransactionId().toString());
			final BigDecimal bigAmount = new BigDecimal(walletCardApportionDetailModel.getCardAmount(), MathContext.DECIMAL64);
			paymentTransactionEntryModel.setAmount(bigAmount);
			paymentTransactionEntryModel.setRequestId(walletCardApportionDetailModel.getTransactionId().toString());
			paymentTransactionEntryModel.setTime(new Date());
			paymentTransactionEntryModel.setCurrency(subOrderModel.getCurrency());
			paymentTransactionEntryModel.setTransactionStatus(walletCardApportionDetailModel.getTrnsStatus());
			paymentTransactionEntryModel.setTransactionStatusDetails(walletCardApportionDetailModel.getTrnsStatus());
			PaymentTypeModel model = new PaymentTypeModel();
			for (final PaymentTransactionModel paymentTransaction : subOrderModel.getPaymentTransactions())
			{
				for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
				{
					if ("success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus())
							|| "ACCEPTED".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus()))
					{
						model = paymentTransactionEntry.getPaymentMode();
					}

				}

			}
			paymentTransactionEntryModel.setPaymentMode(model);
			paymentTransactionEntryModel.setType(PaymentTransactionType.CANCEL);
			modelService.save(paymentTransactionEntryModel);
			final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
			entries.add(paymentTransactionEntryModel);
			paymentTransactionModel.setEntries(entries);
			modelService.save(paymentTransactionModel);
		
			LOG.debug("Payment Transaction created SuccessFully......:");

		}
		
		private WalletApportionReturnInfoModel constructQuickCilverOrderEntryForSplit(
				final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList, final String transactionId,
				final OrderModel subOrderModel,final OrderEntryModel orderEntry)
		{

			final AbstractOrderEntryModel abstractOrderEntryModel = mplOrderService.getEntryModel(transactionId);
			final WalletApportionReturnInfoModel walletApportionReturnModel = getModelService().create(
					WalletApportionReturnInfoModel.class);
			final List<String> qcResponseStatus = new ArrayList<String>();
			double totalQcApportionValue = 0d;
			double totalQcDeliveryValue = 0d;
			double totalQcSchedulingValue = 0d;
			double totalQcShippingValue = 0d;
			if (null != abstractOrderEntryModel && null != abstractOrderEntryModel.getWalletApportionPaymentInfo())
			{
				for (final WalletCardApportionDetailModel cardApportionDetail : abstractOrderEntryModel
						.getWalletApportionPaymentInfo().getWalletCardList())
				{
					if (null != cardApportionDetail && null != cardApportionDetail.getBucketType())
					{
						if (!cardApportionDetail.getBucketType().equalsIgnoreCase("CASHBACK"))
						{
							totalQcApportionValue += Double.parseDouble(cardApportionDetail.getQcApportionValue());
							totalQcDeliveryValue  +=Double.parseDouble(null != cardApportionDetail.getQcDeliveryValue() ? cardApportionDetail
									.getQcDeliveryValue() : "" + 0);
							totalQcSchedulingValue += Double.parseDouble(null != cardApportionDetail.getQcSchedulingValue() ? cardApportionDetail
									.getQcSchedulingValue() : "" + 0);
							totalQcShippingValue += Double.parseDouble(null != cardApportionDetail.getQcShippingValue() ? cardApportionDetail
											.getQcShippingValue() : "" + 0);
						}
					}
				}
			}
			walletApportionReturnModel.setQcApportionPartValue(String.valueOf(totalQcApportionValue));
			walletApportionReturnModel.setQcDeliveryPartValue(String.valueOf(totalQcDeliveryValue));
			walletApportionReturnModel.setQcSchedulingPartValue(String.valueOf(totalQcSchedulingValue));
			walletApportionReturnModel.setQcShippingPartValue(String.valueOf(totalQcShippingValue));
			if (null != walletCardApportionDetailModelList && walletCardApportionDetailModelList.size() > 0){
				for (final WalletCardApportionDetailModel walletCardApportionDetailModelObj : walletCardApportionDetailModelList){
					qcResponseStatus.add(walletCardApportionDetailModelObj.getTrnsStatus());
					createPaymentEntryForQCTransaction(subOrderModel, walletCardApportionDetailModelObj);
				}
			}
			walletApportionReturnModel.setWalletCardList(walletCardApportionDetailModelList);
			walletApportionReturnModel.setTransactionId(transactionId);
			walletApportionReturnModel.setType("CANCEL");
			if (qcResponseStatus.contains("PENDING"))
			{
				walletApportionReturnModel.setStatus("PENDING");
			}
			else
			{
				walletApportionReturnModel.setStatus("SUCCESS");
			}
			modelService.save(walletApportionReturnModel);
			System.out.println("After Saving Juspay Response is :");
			
			orderEntry.setWalletApportionReturnInfo(walletApportionReturnModel);
			System.out.println("Before setting  Order Entry Response is :");
			modelService.save(orderEntry);
			modelService.refresh(walletApportionReturnModel);
			
			System.out.println("After setting  Order Entry Response is :");
	     LOG.error("abstractOrderEntryModel QC Model  Saved Successfully..............");
			
			return walletApportionReturnModel;
		}
		
		private void saveQCandJuspayResponse(final OrderEntryModel orderEntry, final PaymentTransactionModel paymentTransactionModel,
				final WalletApportionReturnInfoModel returnModel)
		{
			final List<String> qcResponseStatus = new ArrayList<String>();
			if (null != orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue()){
				returnModel.setJuspayApportionValue(orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue());
			}
			if (null != orderEntry.getWalletApportionPaymentInfo()
					&& null != orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue())
			{
				returnModel.setJuspayDeliveryValue(orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue());
			}
			if (null != orderEntry.getWalletApportionPaymentInfo()
					&& null != orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue())
			{
				returnModel.setJuspaySchedulingValue(orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue());
			}
			if (null != orderEntry.getWalletApportionPaymentInfo()
					&& null != orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue())
			{
				returnModel.setJuspayShippingValue(orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue());
			}
			returnModel.setOrderId(orderEntry.getOrder().getCode());
			returnModel.setType("CANCEL");
			if (qcResponseStatus.contains("PENDING"))
			{
				returnModel.setStatusForQc("PENDING");
			}
			else
			{
				returnModel.setStatusForQc("SUCCESS");
			}
			if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), MarketplacecommerceservicesConstants.SUCCESS))
			{
				returnModel.setStatus("SUCCESS");
			}
			else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "PENDING"))
			{
				returnModel.setStatus("PENDING");
			}

			System.out.println("Before Saving Juspay Response is :" + returnModel.getJuspayApportionValue());
			modelService.save(returnModel);
			System.out.println("After Saving Juspay Response is :" + returnModel.getJuspayApportionValue());

			orderEntry.setWalletApportionReturnInfo(returnModel);
			System.out.println("Before setting  Order Entry Response is :" + returnModel.getJuspayApportionValue());
			modelService.save(orderEntry);
			System.out.println("After setting  Order Entry Response is :" + returnModel.getJuspayApportionValue());
			LOG.debug("abstractOrderEntryModel Saved Successfully..............");
		}
		
		private double calculateSplitJuspayRefundAmount(final AbstractOrderEntryModel orderEntry)
		{
			double refundAmount = 0.0D;
			if (null != orderEntry.getWalletApportionPaymentInfo()
					&& null != orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue())
			{
				refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue()).doubleValue();
			}
			if (null != orderEntry.getWalletApportionPaymentInfo()
					&& null != orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue())
			{
				refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue()).doubleValue();
			}
			if (null != orderEntry.getWalletApportionPaymentInfo()
					&& null != orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue())
			{
				refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue()).doubleValue();
			}
			if (null != orderEntry.getWalletApportionPaymentInfo()
					&& null != orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue())
			{
				refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue()).doubleValue();
			}
			return refundAmount;
		}
}
