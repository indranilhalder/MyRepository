/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.data.RefundDeliveryData;
import com.tisl.mpl.cockpits.cscockpit.services.MarketPlaceOrderSearchServices;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.GeneratedMarketplaceCoreConstants.Attributes.Consignment;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.WalletEnum;
import com.tisl.mpl.core.model.InvoiceDetailModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.product.data.SendInvoiceData;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.integration.oms.adapter.CustomOmsOrderSyncAdapter;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.response.QCCard;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderController;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * @author 1006687
 *
 */
public class MarketPlaceDefaultOrderController extends DefaultOrderController
		implements MarketPlaceOrderController {

	@Autowired
	private MarketPlaceOrderSearchServices marketPlaceOrderSearchServices;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private OrderHistoryService orderHistoryService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private FormatFactory formatFactory;

	@Autowired
	private RegisterCustomerFacade registerCustomerFacade;
	
	@Autowired
	private CustomOmsOrderService omsOrderService;
	@Autowired
	private CustomOmsOrderSyncAdapter customOmsOrderSyncAdapter;
	
	
	//car-80 
	@Autowired
	private OrderModelService orderModelService;
	
	@Autowired
	private MplOrderDao mplOrderDao;

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceDefaultOrderController.class);
	
	@Autowired
	private MplMWalletRefundService mplMWalletRefundService;
	
	@Autowired
	private MplWalletFacade mplWalletFacade;
	
	@Autowired
	private MplOrderService mplOrderService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.cockpits.cscockpit.widgets.controllers.
	 * MarketPlaceOrderController#getAssociatedOrders()
	 */
	@Override
	public List<TypedObject> getAssociatedOrders() {
		TypedObject typedObject = getCallContextController().getCurrentOrder();
		if (typedObject != null && typedObject.getObject() != null
				&& typedObject.getObject() instanceof OrderModel) {
			OrderModel currentOrder = (OrderModel) typedObject.getObject();
			Collection<OrderModel> orders = marketPlaceOrderSearchServices
					.getAssociatedOrders(currentOrder.getCode());
			return getCockpitTypeService().wrapItems(orders);
		}

		return Collections.emptyList();
	}

	@Override
	public CallContextController getContextController() {
		return getCallContextController();
	}

	@Override
	public List<TypedObject> getPreviousOrders() {
		TypedObject typedObject = getCallContextController()
				.getCurrentCustomer();
		if (typedObject != null && typedObject.getObject() != null
				&& typedObject.getObject() instanceof CustomerModel) {
			CustomerModel currentCustomer = (CustomerModel) typedObject
					.getObject();

			Collection<OrderModel> oldOrders = new ArrayList<OrderModel>(
					currentCustomer.getOrders());

			if (CollectionUtils.isNotEmpty(oldOrders)) {
				final Calendar cal = Calendar.getInstance();
				int monthThreshold = configurationService.getConfiguration()
						.getInt("cscockpit.order.invoice.threshold.months")
						* -1;
				cal.add(Calendar.MONTH, monthThreshold);

				CollectionUtils.filter(oldOrders, new Predicate() {

					@Override
					public boolean evaluate(Object obj) {
						OrderModel temp = (OrderModel) obj;
						return cal.before(temp.getCreationtime())
								|| cal.equals(temp.getCreationtime());

					}
				});
				return getCockpitTypeService().wrapItems(oldOrders);
			}
		}
		return Collections.emptyList();
	}

	@Override
	public String doRefundPayment(List<OrderEntryModel> orderEntryModel) {
		Double totalRefundAmount = 0d;
		Double totalRefundAmountForQc = 0d;
		PaymentTransactionModel paymentTransactionModel = null;
		boolean splitPaymentStatus = false;
		boolean qcPaymentStatus = false;
		for (OrderEntryModel orderEntry : orderEntryModel) {
			OrderModel orderModel = orderEntry.getOrder().getParentReference();
			if(null != orderModel.getSplitModeInfo()  &&  orderModel.getSplitModeInfo().equalsIgnoreCase("Split")){
				splitPaymentStatus=true;
				totalRefundAmount += calculateSplitJuspayRefundAmount(orderEntry);
				totalRefundAmountForQc += calculateSplitQcRefundAmount(orderEntry);
			}else if(null != orderModel.getSplitModeInfo()  &&  orderModel.getSplitModeInfo().equalsIgnoreCase("CliqCash")){
				qcPaymentStatus=true;
				totalRefundAmountForQc += calculateSplitQcRefundAmount(orderEntry);
			}else{
			totalRefundAmount += orderEntry.getNetAmountAfterAllDisc();
			}
		}
//		Mrupee implementation 
		// final OrderModel order=orderEntryModel.get(0).getOrder();
		final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
		// Done for INC144317893
		
//		if((null !=order.getIsWallet() &&  WalletEnum.NONWALLET.toString().equalsIgnoreCase(order.getIsWallet().getCode()))||null ==order.getIsWallet())
//		{
		String walletId =null;
		QCRedeeptionResponse response =null;
	  	CustomerModel customerModel= (CustomerModel)orderEntryModel.get(0).getOrder().getUser();
	  	if(null!=customerModel && null!= customerModel.getCustomerWalletDetail()){
	  		walletId=customerModel.getCustomerWalletDetail().getWalletId();
	  	}
  	
		try {
			
			if(splitPaymentStatus){
				try{
			      	QCCreditRequest qcCreditRequest =new QCCreditRequest();
			      	qcCreditRequest.setAmount(totalRefundAmountForQc.toString());
			      	qcCreditRequest.setInvoiceNumber(orderEntryModel.get(0).getOrder().getParentReference().getCode());
			      	qcCreditRequest.setNotes("Cancel for "+ totalRefundAmountForQc.toString());    	
			      	response = mplWalletFacade.qcCredit(walletId , qcCreditRequest);
			      	LOG.debug("Quck Cilver giving response code "+response.getResponseCode()+" Order Id :"+orderEntryModel.get(0).getOrder().getParentReference().getCode());
		      		}catch(Exception e){
		      			e.getMessage();
		      			LOG.error("Quck Cilver giving response code "+response.getResponseCode()+" Order Id :"+orderEntryModel.get(0).getOrder().getParentReference().getCode());
		      		}
				try{
					paymentTransactionModel = mplJusPayRefundService.doRefund(
							orderEntryModel.get(0).getOrder(), totalRefundAmount,
							PaymentTransactionType.RETURN,uniqueRequestId);
				}catch(Exception e){
	      			e.getMessage();
	      			LOG.error("Juspay response code  Order Id :"+orderEntryModel.get(0).getOrder().getParentReference().getCode());
				}
			}else if (qcPaymentStatus){
				 try{
			   	      	QCCreditRequest qcCreditRequest =new QCCreditRequest();
			   	      	qcCreditRequest.setAmount(totalRefundAmountForQc.toString());
			   	      	qcCreditRequest.setInvoiceNumber(orderEntryModel.get(0).getOrder().getParentReference().getCode());
			   	      	qcCreditRequest.setNotes("Cancel for "+ totalRefundAmountForQc.toString());    	
			   	      	response = mplWalletFacade.qcCredit(walletId , qcCreditRequest);
				   	     for (OrderEntryModel orderEntry : orderEntryModel) {
				   	    	    if(null != orderEntry.getOrder().getSplitModeInfo() && orderEntry.getOrder().getSplitModeInfo().equalsIgnoreCase("CliqCash")){
				   	    	    	constructQuickCilverOrderEntry(response,orderEntry.getTransactionID(),orderEntry.getOrder());
				   	    	    	ConsignmentStatus newStatus = null;
									if (orderEntry != null
											&& CollectionUtils.isNotEmpty(orderEntry
													.getConsignmentEntries())) {
										// ConsignmentModel consignmentModel = orderEntry
										// .getConsignmentEntries().iterator().next()
										// .getConsignment();
										if (response.getResponseCode() == Integer.valueOf("0")) {
											newStatus = ConsignmentStatus.RETURN_COMPLETED;
										} else {
											newStatus = ConsignmentStatus.REFUND_INITIATED;
										}
										// getModelService().save(consignmentModel);
										mplJusPayRefundService.makeRefundOMSCall(orderEntry,
												paymentTransactionModel,
												calculateSplitQcRefundAmount(orderEntry),
												newStatus,null);
										
										//Start TISPRD-871
										if(newStatus.equals(ConsignmentStatus.RETURN_COMPLETED)){
											orderEntry.setJuspayRequestId(uniqueRequestId);
											getModelService().save(orderEntry);
										}
									}
				   	    	    }
				   	         	
				   	     }
			   	      	LOG.debug("*****************************"+response.getResponseCode());
				      	 }catch(Exception e){
				      			e.getMessage();
				      			LOG.error("Quck Cilver giving response code "+response.getResponseCode()+" Order Id :"+orderEntryModel.get(0).getOrder().getParentReference().getCode());
				      	} 
				       String qcStatus =null;
				       if(response.getResponseCode() == Integer.valueOf(0)){
				    	   qcStatus="SUCCESS" ;
				       }else {
				    	   qcStatus="PENDING";
				       }
				 
				 return  qcStatus + ","+ response.getTransactionId() + "," + totalRefundAmountForQc;
				
			}else {	
				paymentTransactionModel = mplJusPayRefundService.doRefund(
						orderEntryModel.get(0).getOrder(), totalRefundAmount,
						PaymentTransactionType.RETURN,uniqueRequestId);
			}
			if (null != paymentTransactionModel) {
				mplJusPayRefundService.attachPaymentTransactionModel(
						orderEntryModel.get(0).getOrder(),
						paymentTransactionModel);
				WalletApportionReturnInfoModel returnModel = null;
				for (OrderEntryModel orderEntry : orderEntryModel) {

					// If CosignmentEnteries are present then update OMS with
					// the state.
					if(null != orderEntry.getOrder().getSplitModeInfo() && orderEntry.getOrder().getSplitModeInfo().equalsIgnoreCase("Split")){
						returnModel =constructQuickCilverOrderEntryForSplit(response,orderEntry.getTransactionID());
						saveQCandJuspayResponse(orderEntry,paymentTransactionModel,returnModel ,orderEntry.getOrder());
					}
					ConsignmentStatus newStatus = null;
					if (orderEntry != null
							&& CollectionUtils.isNotEmpty(orderEntry
									.getConsignmentEntries())) {
						// ConsignmentModel consignmentModel = orderEntry
						// .getConsignmentEntries().iterator().next()
						// .getConsignment();
						if (StringUtils.equalsIgnoreCase(
								paymentTransactionModel.getStatus(), "SUCCESS")) {
							newStatus = ConsignmentStatus.RETURN_COMPLETED;
						} else if (StringUtils.equalsIgnoreCase(
								paymentTransactionModel.getStatus(), "PENDING")) {
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
									.setRefundType(JuspayRefundType.RETURN);
							refundTransactionMappingModel
									.setRefundAmount(orderEntry.
											getNetAmountAfterAllDisc());//TISPRO-216 : Refund amount Set in RTM
							
							getModelService().save(
									refundTransactionMappingModel);
						} else {
							newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
						}
						// getModelService().save(consignmentModel);
						mplJusPayRefundService.makeRefundOMSCall(orderEntry,
								paymentTransactionModel,
								orderEntry.getNetAmountAfterAllDisc(),
								newStatus,null);
						
						//Start TISPRD-871
						if(newStatus.equals(ConsignmentStatus.RETURN_COMPLETED)){
							orderEntry.setJuspayRequestId(uniqueRequestId);
							getModelService().save(orderEntry);
						}
					}
				}
			} else {
				
				//TISSIT-1801
				LOG.error("Manual Refund Failed");
				for (OrderEntryModel orderEntry : orderEntryModel) {
					mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_IN_PROGRESS,null);
				}
				
				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(), "FAILURE", totalRefundAmount,
								PaymentTransactionType.RETURN, "NO Response FROM PG", uniqueRequestId);
				mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
				//TISSIT-1801
				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			// TISSIT-1784 Code addition started
			for (OrderEntryModel orderEntry : orderEntryModel) {
				mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_INITIATED,null);

				// Making RTM entry to be picked up by webhook job	
				RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(RefundTransactionMappingModel.class);
				refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
				refundTransactionMappingModel.setJuspayRefundId(uniqueRequestId);
				refundTransactionMappingModel.setCreationtime(new Date());
				refundTransactionMappingModel.setRefundType(JuspayRefundType.RETURN);
				refundTransactionMappingModel.setRefundAmount(orderEntry.getNetAmountAfterAllDisc());//TISPRO-216 : Refund amount Set in RTM
				getModelService().save(refundTransactionMappingModel);
			}
			// TISSIT-1784 Code addition ended
			
			paymentTransactionModel = mplJusPayRefundService
					.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(), "FAILURE", totalRefundAmount,
							PaymentTransactionType.RETURN, "FAILURE", uniqueRequestId);
			mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
			
		}
		// Done for INC144317893
//	}
//		else if(null !=order.getIsWallet() &&  WalletEnum.MRUPEE.toString().equalsIgnoreCase(order.getIsWallet().getCode())){
//			final String uniqueRequestId1 = mplMWalletRefundService.getRefundUniqueRequestId();
//			try {
//				paymentTransactionModel = mplMWalletRefundService.doRefund(
//						orderEntryModel.get(0).getOrder(), totalRefundAmount,
//						PaymentTransactionType.RETURN,uniqueRequestId1);
//				if (null != paymentTransactionModel) {
//					mplJusPayRefundService.attachPaymentTransactionModel(
//							orderEntryModel.get(0).getOrder(),
//							paymentTransactionModel);
//					for (OrderEntryModel orderEntry : orderEntryModel) {
//						// If CosignmentEnteries are present then update OMS with
//						// the state.
//						ConsignmentStatus newStatus = null;
//						if (orderEntry != null
//								&& CollectionUtils.isNotEmpty(orderEntry
//										.getConsignmentEntries())) {
//							
//							if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
//									MarketplacecommerceservicesConstants.SUCCESS))
//							{
//								newStatus = ConsignmentStatus.RETURN_COMPLETED;
//							}
//							else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
//									MarketplacecommerceservicesConstants.FAILURE))
//							{
//								newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
//							}
//							else
//							{
//								newStatus = ConsignmentStatus.REFUND_INITIATED;
//							}
//							// getModelService().save(consignmentModel);
////							mplJusPayRefundService.makeRefundOMSCall(orderEntry,
////									paymentTransactionModel,
////									orderEntry.getNetAmountAfterAllDisc(),
////									newStatus);
//							//R2.3
//							mplJusPayRefundService.makeRefundOMSCall(orderEntry,
//									paymentTransactionModel,
//									orderEntry.getNetAmountAfterAllDisc(),
//									newStatus,null);
//							if(newStatus.equals(ConsignmentStatus.RETURN_COMPLETED)){
//								//orderEntry.setJuspayRequestId(uniqueRequestId);
//								getModelService().save(orderEntry);
//							}
//						}
//					}
//				} else {
//					LOG.error("Manual Refund Failed");
//					for (OrderEntryModel orderEntry : orderEntryModel) {
//						//mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_IN_PROGRESS);
//					//R2.3
//						mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_IN_PROGRESS,null);
//					}				
//					paymentTransactionModel = mplJusPayRefundService
//							.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(), "FAILURE", totalRefundAmount,
//									PaymentTransactionType.RETURN, "NO Response FROM PG", uniqueRequestId);
//					mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
//				}
//			} catch (Exception e) {
//				LOG.error(e.getMessage(), e);
//				for (OrderEntryModel orderEntry : orderEntryModel) {
//					//mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_INITIATED);
//				//R2.3
//					mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_INITIATED,null);
//				}
//				paymentTransactionModel = mplJusPayRefundService
//						.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(), "FAILURE", totalRefundAmount,
//								PaymentTransactionType.RETURN, "FAILURE", uniqueRequestId);
//				mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
//			}
//		}		
		String result = paymentTransactionModel.getStatus() + ","
				+ paymentTransactionModel.getCode() + "," + totalRefundAmount;
		return result;
	}





/*R2.3 Refund schedule delivery charge call to oms */
	/**
	 * Refund Schedule delivery Charges  
	 */
	
	@Override
	public TypedObject createRefundScheduleDeliveryChargesRequest(
			OrderModel orderModel,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap) {
		PaymentTransactionModel paymentTransactionModel = null;
		Double totalRefundScheduleDeliveryCharges = Double.valueOf(0);
		
		if (MapUtils.isNotEmpty(refundMap)) {
			final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
			
			try {
				for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
						.entrySet()) {
					totalRefundScheduleDeliveryCharges = totalRefundScheduleDeliveryCharges
							+ refundEntry.getKey().getScheduledDeliveryCharge();

					refundEntry.getKey().setRefundedScheduleDeliveryChargeAmt(
							refundEntry.getKey().getScheduledDeliveryCharge());
					refundEntry.getKey().setScheduledDeliveryCharge(Double.valueOf(0));
					modelService.save(refundEntry.getKey());

				}
				LOG.debug("Total Refund Schedule Delivery Charges :"+totalRefundScheduleDeliveryCharges);
//				Mrupee implementation 
				// Done for INC144317893
//				if((null !=orderModel.getIsWallet() &&  WalletEnum.NONWALLET.toString().equalsIgnoreCase(orderModel.getIsWallet().getCode()))||null ==orderModel.getIsWallet())
//				{
				paymentTransactionModel = mplJusPayRefundService.doRefund(
						orderModel, totalRefundScheduleDeliveryCharges,
						PaymentTransactionType.REFUND_SCHEDULE_DELIVERY_CHARGES,uniqueRequestId);
				if (null != paymentTransactionModel) {
					mplJusPayRefundService.attachPaymentTransactionModel(
							orderModel, paymentTransactionModel);
					// commented  for BUG ID : TISRLREG-4131 START 
					
//					if ("PENDING".equalsIgnoreCase(paymentTransactionModel
//							.getStatus())) {
//						RefundTransactionMappingModel refundTransactionMappingModel = getModelService()
//								.create(RefundTransactionMappingModel.class);
//						refundTransactionMappingModel
//								.setRefundedOrderEntry(orderModel.getEntries()
//										.iterator().next());
//						refundTransactionMappingModel
//								.setJuspayRefundId(paymentTransactionModel
//										.getCode());
//						refundTransactionMappingModel
//								.setCreationtime(new Date());
//						refundTransactionMappingModel
//								.setRefundType(JuspayRefundType.REFUND_SCHEDULE_DELIVERY_CHARGE);
//						refundTransactionMappingModel
//								.setRefundAmount(totalRefundScheduleDeliveryCharges);//TISPRO-216 : Refund amount Set in RTM
					
//						getModelService().save(refundTransactionMappingModel);
//					}
					
					// commented  for BUG ID : TISRLREG-4131 END
					
					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
							.entrySet()) {
						 AbstractOrderEntryModel entry = refundEntry.getKey();
						 if(null != entry) {
							 entry.setScheduleChargesJuspayRequestId(uniqueRequestId);
							 modelService.save(entry);
						 }
						 
						 // Added for BUG ID : TISRLREG-4131 START 
						 Double refundedScheduleDeliveryCharges = entry.getRefundedScheduleDeliveryChargeAmt();
						 if ("PENDING".equalsIgnoreCase(paymentTransactionModel
									.getStatus())) {
							 
								RefundTransactionMappingModel refundTransactionMappingModel = getModelService()
										.create(RefundTransactionMappingModel.class);
								refundTransactionMappingModel
										.setRefundedOrderEntry(entry);
								refundTransactionMappingModel
										.setJuspayRefundId(paymentTransactionModel




												.getCode());
								refundTransactionMappingModel
										.setCreationtime(new Date());
								refundTransactionMappingModel
										.setRefundType(JuspayRefundType.REFUND_SCHEDULE_DELIVERY_CHARGE);
								refundTransactionMappingModel
										.setRefundAmount(refundedScheduleDeliveryCharges);//TISPRO-216 : Refund amount Set in RTM
								
								getModelService().save(refundTransactionMappingModel);
							}

						 // Added for BUG ID : TISRLREG-4131 END 
							ConsignmentStatus newStatus = null;
							 if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
								 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
							 }
							// sending current order status, as oms is not accepting null value and no status update is required
						mplJusPayRefundService.makeRefundOMSCall(refundEntry
								.getKey(), paymentTransactionModel, refundEntry
								.getKey().getRefundedScheduleDeliveryChargeAmt(), newStatus,MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);  
					}
				} else {
					LOG.error("Refund Schedule Delivery Charges Failed");

					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
							.entrySet()) {
						ConsignmentStatus newStatus = null;
						if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
							 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
						 }
						mplJusPayRefundService.makeRefundOMSCall(refundEntry.getKey(),paymentTransactionModel,refundEntry.getKey().getRefundedScheduleDeliveryChargeAmt(),newStatus,MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);

						}
					
					paymentTransactionModel = mplJusPayRefundService
							.createPaymentTransactionModel(orderModel, "FAILURE", totalRefundScheduleDeliveryCharges,
									PaymentTransactionType.RETURN, "NO Response FROM PG", uniqueRequestId);

					mplJusPayRefundService.attachPaymentTransactionModel(orderModel, paymentTransactionModel);
				}
				// Done for INC144317893
//				}
//				Mrupee payment mode implementation 
//				else if(null !=orderModel.getIsWallet() &&  WalletEnum.MRUPEE.toString().equalsIgnoreCase(orderModel.getIsWallet().getCode())){
//					
//					LOG.debug("%%%%%%%%%  Mrupee Refund Schedule Delivery Charges Started ^^^^^^^^^^^^^^" +orderModel.getIsWallet() + orderModel.getIsWallet().getCode());
//					
//					paymentTransactionModel = mplMWalletRefundService.doRefund(
//							orderModel, totalRefundScheduleDeliveryCharges,
//							PaymentTransactionType.REFUND_SCHEDULE_DELIVERY_CHARGES,uniqueRequestId);
//					if (null != paymentTransactionModel) {
//						mplJusPayRefundService.attachPaymentTransactionModel(
//								orderModel, paymentTransactionModel);
//						
//						/*made changes in r2.3 start  */
//						for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
//								.entrySet()) {
//							 AbstractOrderEntryModel entry = refundEntry.getKey();
//							 if(null != entry) {
//								 entry.setScheduleChargesJuspayRequestId(uniqueRequestId);
//								 modelService.save(entry);
//							 }
//							 
//							 // Added for BUG ID : TISRLREG-4131 END 
//								ConsignmentStatus newStatus = null;
//								 if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
//									 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
//								 }
//								// sending current order status, as oms is not accepting null value and no status update is required
//								 
//									LOG.debug(" Before OMS call  Mrupee Refund Schedule Delivery Charges Started ^^^^^^^^^^^^^^" );
//							mplJusPayRefundService.makeRefundOMSCall(refundEntry
//									.getKey(), paymentTransactionModel, refundEntry
//									.getKey().getRefundedScheduleDeliveryChargeAmt(), newStatus,MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);  
//							
//							LOG.debug(" After OMS call  Mrupee Refund Schedule Delivery Charges Started ^^^^^^^^^^^^^^" );
//						}
//						/*made changes in r2.3 end  */
//					} 
//					else {
//						LOG.error("@@@@@@  @@@@@  @@@  @@@@ Mrupee Refund Schedule Delivery Charges Failed +++++++++ +++++++++");
//
//						for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
//								.entrySet()) {
//							ConsignmentStatus newStatus = null;
//							if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
//								 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
//							 }
//							mplJusPayRefundService.makeRefundOMSCall(refundEntry.getKey(),paymentTransactionModel,refundEntry.getKey().getRefundedScheduleDeliveryChargeAmt(),newStatus,MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);
//
//							}
//						paymentTransactionModel = mplJusPayRefundService
//								.createPaymentTransactionModel(orderModel, "FAILURE", totalRefundScheduleDeliveryCharges,
//										PaymentTransactionType.RETURN, "NO Response FROM PG", uniqueRequestId);
//						mplJusPayRefundService.attachPaymentTransactionModel(orderModel, paymentTransactionModel);
//					}
//				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);

//				if((null !=orderModel.getIsWallet() &&  WalletEnum.NONWALLET.toString().equalsIgnoreCase(orderModel.getIsWallet().getCode()))||null ==orderModel.getIsWallet()){
				// Done for INC144317893
				for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
						.entrySet()) {
					ConsignmentStatus newStatus = null;
					 AbstractOrderEntryModel entry = refundEntry.getKey();
					 if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
						 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
					 }
					mplJusPayRefundService.makeRefundOMSCall(entry,paymentTransactionModel,entry.getRefundedScheduleDeliveryChargeAmt(),newStatus,MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);

					// Making RTM entry to be picked up by webhook job	
					RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(RefundTransactionMappingModel.class);
					refundTransactionMappingModel.setRefundedOrderEntry(entry);
					refundTransactionMappingModel.setJuspayRefundId(uniqueRequestId);
					refundTransactionMappingModel.setCreationtime(new Date());
					refundTransactionMappingModel.setRefundType(JuspayRefundType.REFUND_SCHEDULE_DELIVERY_CHARGE);
					refundTransactionMappingModel.setRefundAmount(entry.getRefundedScheduleDeliveryChargeAmt());//TISPRO-216 : Refund amount Set in RTM
					getModelService().save(refundTransactionMappingModel);
				}
				// Done for INC144317893
//				}
				LOG.error(e.getMessage(), e);
				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(orderModel, "FAILURE",
								totalRefundScheduleDeliveryCharges,
								PaymentTransactionType.REFUND_SCHEDULE_DELIVERY_CHARGES,
								"FAILURE", uniqueRequestId);
				mplJusPayRefundService.attachPaymentTransactionModel(
						orderModel, paymentTransactionModel);
			}
		}
		return getCockpitTypeService().wrapItem(paymentTransactionModel);
	}		

	@Override
	public TypedObject createRefundDeliveryChargesRequest(
			OrderModel orderModel,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap,boolean isEdToHdRefund) {
		PaymentTransactionModel paymentTransactionModel = null;
		Double totalRefundDeliveryCharges = Double.valueOf(0);
		if (MapUtils.isNotEmpty(refundMap)) {
		final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
		PaymentTransactionType paymentTransactionType = null ; 
		JuspayRefundType juspayRefundType= null;
		if(isEdToHdRefund) {
			paymentTransactionType = PaymentTransactionType.REFUND_EXPRESS_DELIVERY_CHARGES;
			juspayRefundType = JuspayRefundType.REFUND_EXPRESS_DELIVERY_CHARGE;
		}else {
			paymentTransactionType = PaymentTransactionType.REFUND_DELIVERY_CHARGES;
			juspayRefundType = JuspayRefundType.REFUND_DELIVERY_CHARGE;
		}
			try {
				if(isEdToHdRefund) {
					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
							.entrySet()) {
						double currDeliveryCharges = refundEntry.getKey().getCurrDelCharge()!=0 ? refundEntry.getKey().getCurrDelCharge():refundEntry.getKey().getRefundedDeliveryChargeAmt();
						double hdDeliveryCharges = refundEntry.getKey().getHdDeliveryCharge() != null ? refundEntry.getKey()
								.getHdDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
						totalRefundDeliveryCharges = totalRefundDeliveryCharges
								+ (currDeliveryCharges - hdDeliveryCharges);
						refundEntry.getKey().setRefundedEdChargeAmt(currDeliveryCharges - hdDeliveryCharges);
						modelService.save(refundEntry.getKey());
					}
				}else {
					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
							.entrySet()) {
						if(null != refundEntry.getKey().getIsEDtoHD() && refundEntry.getKey().getIsEDtoHD()){
							double hdDeliveryCharges = refundEntry.getKey().getHdDeliveryCharge() != null ? refundEntry.getKey()
									.getHdDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
							totalRefundDeliveryCharges = totalRefundDeliveryCharges
									+ hdDeliveryCharges;
						}else {
							totalRefundDeliveryCharges = totalRefundDeliveryCharges
									+ refundEntry.getKey().getCurrDelCharge();
						}
						
						refundEntry.getKey().setRefundedDeliveryChargeAmt(
								refundEntry.getKey().getCurrDelCharge());
						refundEntry.getKey().setCurrDelCharge(Double.valueOf(0));
						modelService.save(refundEntry.getKey());

					}
				}
//				Mrupee implementation 
				// Done for INC144317893

//				if((null !=orderModel.getIsWallet() &&  WalletEnum.NONWALLET.toString().equalsIgnoreCase(orderModel.getIsWallet().getCode()))||null ==orderModel.getIsWallet())
//				{				
				paymentTransactionModel = mplJusPayRefundService.doRefund(
						orderModel, totalRefundDeliveryCharges,
						paymentTransactionType,uniqueRequestId);
				if (null != paymentTransactionModel) {
					mplJusPayRefundService.attachPaymentTransactionModel(
							orderModel, paymentTransactionModel);
									// commented  for BUG ID : TISRLREG-4131 START 
//					if ("PENDING".equalsIgnoreCase(paymentTransactionModel
//							.getStatus())) {
//						RefundTransactionMappingModel refundTransactionMappingModel = getModelService()
//								.create(RefundTransactionMappingModel.class);
//						refundTransactionMappingModel
//								.setRefundedOrderEntry(orderModel.getEntries()
//										.iterator().next());
//						refundTransactionMappingModel
//								.setJuspayRefundId(paymentTransactionModel
//										.getCode());
//						refundTransactionMappingModel
//								.setCreationtime(new Date());
//						refundTransactionMappingModel
//								.setRefundType(JuspayRefundType.REFUND_DELIVERY_CHARGE);
//						refundTransactionMappingModel
//								.setRefundAmount(totalRefundDeliveryCharges);//TISPRO-216 : Refund amount Set in RTM
//						getModelService().save(refundTransactionMappingModel);

//					}

// commented  for BUG ID : TISRLREG-4131 END 
					/*made changes in r2.3 start  */
					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
							.entrySet()) {
						 AbstractOrderEntryModel entry = refundEntry.getKey();
						 Double refundedDeliveryCharges = 0.0D;
						 if(isEdToHdRefund) {
							 refundedDeliveryCharges = entry.getRefundedEdChargeAmt();
						 }else if(null != entry.getIsEDtoHD() && entry.getIsEDtoHD()){
							 refundedDeliveryCharges=	 entry.getHdDeliveryCharge() != null ?entry
										.getHdDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
						 }else {
							 refundedDeliveryCharges =  entry.getRefundedDeliveryChargeAmt();
						 }
//						boolean isEDToHD = false;
//						if(null != refundEntry.getKey() && null != refundEntry.getKey().getIsEDtoHD() && refundEntry.getKey().getIsEDtoHD()) {
//							isEDToHD= true;
//						}
						 if(null != entry && !isEdToHdRefund) {
							 entry.setDelChargesJuspayRequestId(uniqueRequestId);
							 modelService.save(entry);
						 }else {
							 entry.setEdChargesJuspayRequestId(uniqueRequestId);
							 modelService.save(entry);
						 }
						 if ("PENDING".equalsIgnoreCase(paymentTransactionModel
									.getStatus())) {
							 
								RefundTransactionMappingModel refundTransactionMappingModel = getModelService()
										.create(RefundTransactionMappingModel.class);
								refundTransactionMappingModel
										.setRefundedOrderEntry(entry);
								refundTransactionMappingModel
										.setJuspayRefundId(paymentTransactionModel
												.getCode());
								refundTransactionMappingModel
										.setCreationtime(new Date());
								refundTransactionMappingModel
										.setRefundType(juspayRefundType);
								refundTransactionMappingModel
										.setRefundAmount(refundedDeliveryCharges);//TISPRO-216 : Refund amount Set in RTM
								getModelService().save(refundTransactionMappingModel);
							}
						 
							ConsignmentStatus newStatus = null;
							 if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
								 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
							 }
							
							 
							 // sending current order status, as oms is not accepting null value and no status update is required
						mplJusPayRefundService.makeRefundOMSCall(refundEntry
								.getKey(), paymentTransactionModel, refundedDeliveryCharges, newStatus,isEdToHdRefund?MarketplacecommerceservicesConstants.REFUND_CATEGORY_E:null);															// required.
					}
					/*made changes in r2.3 end  */
				} 
				else {
					LOG.error("Refund Delivery Charges Failed");

					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
							.entrySet()) {
						ConsignmentStatus newStatus = null;
						if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
							 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
						 }
						Double refundedDeliveryCharges = 0.0D;
						 if(isEdToHdRefund) {
							 refundedDeliveryCharges = refundEntry.getKey().getRefundedEdChargeAmt();
						 }else if(null != refundEntry.getKey().getIsEDtoHD() && refundEntry.getKey().getIsEDtoHD() ){
								 refundedDeliveryCharges =   refundEntry.getKey().getHdDeliveryCharge() != null ?refundEntry.getKey()
											.getHdDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
						 }else {
							 refundedDeliveryCharges =  refundEntry.getKey().getRefundedDeliveryChargeAmt();
						 }
						mplJusPayRefundService.makeRefundOMSCall(refundEntry.getKey(),paymentTransactionModel,refundedDeliveryCharges,newStatus,MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);


						}
					
					paymentTransactionModel = mplJusPayRefundService
							.createPaymentTransactionModel(orderModel, "FAILURE", totalRefundDeliveryCharges,
									paymentTransactionType, "NO Response FROM PG", uniqueRequestId);
					mplJusPayRefundService.attachPaymentTransactionModel(orderModel, paymentTransactionModel);
				}
				
				// Done for INC144317893

//			} 
//			else if(null !=orderModel.getIsWallet() &&  WalletEnum.MRUPEE.toString().equalsIgnoreCase(orderModel.getIsWallet().getCode())){
//				
//				LOG.debug(" ************** Mrupee Refund Delivery Charges Started ^^^^^^^^^^^^^^" );
//				
//				paymentTransactionModel = mplMWalletRefundService.doRefund(
//						orderModel, totalRefundDeliveryCharges,
//						PaymentTransactionType.REFUND_DELIVERY_CHARGES,uniqueRequestId);
//				if (null != paymentTransactionModel) {
//					mplJusPayRefundService.attachPaymentTransactionModel(
//							orderModel, paymentTransactionModel);
//					
//						/*made changes in r2.3 start  */
//						for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
//								.entrySet()) {
//							 AbstractOrderEntryModel entry = refundEntry.getKey();
//							// Double refundedDeliveryCharges = entry.getRefundedDeliveryChargeAmt();
//							boolean isEDToHD = false;
//							if(null != refundEntry.getKey() && null != refundEntry.getKey().getIsEDtoHD() && refundEntry.getKey().getIsEDtoHD()) {
//								isEDToHD= true;
//							}
//							 if(null != entry) {
//								 entry.setDelChargesJuspayRequestId(uniqueRequestId);
//								 modelService.save(entry);
//							 }
//							 
//								ConsignmentStatus newStatus = null;
//								 if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
//									 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
//								 }
//								
//								 
//								 // sending current order status, as oms is not accepting null value and no status update is required
//								 
//								 LOG.debug(" ************** Before OMS Mrupee Refund Delivery Charges Call ^^^^^^^^^^^^^^" );
//							mplJusPayRefundService.makeRefundOMSCall(refundEntry
//									.getKey(), paymentTransactionModel, refundEntry
//									.getKey().getRefundedDeliveryChargeAmt(), newStatus,isEDToHD?MarketplacecommerceservicesConstants.REFUND_CATEGORY_E:null);
//							// required.
//							 LOG.debug(" ************** After OMS Mrupee Refund Delivery Charges Call ^^^^^^^^^^^^^^" );
//						}
//						/*made changes in r2.3 end  */
//						}
//				else {
//				LOG.error("********Mrupee Refund Delivery Charges Failed &&&&&&&&&&");
//					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
//							.entrySet()) {
//						ConsignmentStatus newStatus = null;
//						if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
//							 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
//						 }
//						mplJusPayRefundService.makeRefundOMSCall(refundEntry.getKey(),paymentTransactionModel,refundEntry.getKey().getRefundedDeliveryChargeAmt(),newStatus,MarketplacecommerceservicesConstants.REFUND_CATEGORY_S);
//						}					
//					paymentTransactionModel = mplJusPayRefundService
//							.createPaymentTransactionModel(orderModel, "FAILURE", totalRefundDeliveryCharges,
//									PaymentTransactionType.REFUND_DELIVERY_CHARGES, "NO Response FROM PG", uniqueRequestId);
//					mplJusPayRefundService.attachPaymentTransactionModel(orderModel, paymentTransactionModel);
//				}
//			}
		}
			catch (Exception e) {		
			LOG.error(e.getMessage(), e);
			// Done for INC144317893
//			if((null !=orderModel.getIsWallet() &&  WalletEnum.NONWALLET.toString().equalsIgnoreCase(orderModel.getIsWallet().getCode()))||null ==orderModel.getIsWallet()){
				for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
						.entrySet()) {
					ConsignmentStatus newStatus = null;
					 AbstractOrderEntryModel entry = refundEntry.getKey();
					 if(null != refundEntry.getKey() && null != refundEntry.getKey().getConsignmentEntries()); {
						 newStatus = refundEntry.getKey().getConsignmentEntries().iterator().next().getConsignment().getStatus();;
					 }
					 Double refundedDeliveryCharges = 0.0D;
					 if(isEdToHdRefund) {
						 refundedDeliveryCharges = entry.getRefundedEdChargeAmt();
					 }else if(null != entry.getIsEDtoHD() && entry.getIsEDtoHD()){
							 refundedDeliveryCharges =  refundEntry.getKey().getHdDeliveryCharge() != null ?refundEntry.getKey()
										.getHdDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
					 }else {
						 refundedDeliveryCharges =  entry.getRefundedDeliveryChargeAmt();
					 }
					mplJusPayRefundService.makeRefundOMSCall(entry,paymentTransactionModel,refundedDeliveryCharges,newStatus,null);

					// Making RTM entry to be picked up by webhook job	
					RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(RefundTransactionMappingModel.class);
					refundTransactionMappingModel.setRefundedOrderEntry(entry);
					refundTransactionMappingModel.setJuspayRefundId(uniqueRequestId);
					refundTransactionMappingModel.setCreationtime(new Date());
					refundTransactionMappingModel.setRefundType(juspayRefundType);
					refundTransactionMappingModel.setRefundAmount(refundedDeliveryCharges);//TISPRO-216 : Refund amount Set in RTM
					getModelService().save(refundTransactionMappingModel);
				}
				// Done for INC144317893
//			}
				LOG.error(e.getMessage(), e);
				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(orderModel, "FAILURE",
								totalRefundDeliveryCharges,
								paymentTransactionType,
								"FAILURE", uniqueRequestId);
				mplJusPayRefundService.attachPaymentTransactionModel(
						orderModel, paymentTransactionModel);
			}
		}
		return getCockpitTypeService().wrapItem(paymentTransactionModel);
	}


	@Override
	public TypedObject createOrderHistoryRequest(OrderModel orderModel,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap,
			Double totalRefundDeliveryCharges) {
		OrderModel snapshot = orderHistoryService
				.createHistorySnapshot(orderModel);
		final CurrencyModel cartCurrencyModel = orderModel.getCurrency();
		final NumberFormat currencyInstance = (NumberFormat) sessionService
				.executeInLocalView(new SessionExecutionBody() {
					public Object execute() {
						commonI18NService.setCurrentCurrency(cartCurrencyModel);
						return formatFactory.createCurrencyFormat();
					}
				});
		for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
				.entrySet()) {
			AbstractOrderEntryModel refundEntryModel = refundEntry.getKey();
			// refundEntryModel.setCurrDelCharge(Double.valueOf(0));
			OrderHistoryEntryModel entry = modelService
					.create(OrderHistoryEntryModel.class);
			entry.setTimestamp(new Date());
			entry.setOrder(orderModel);
			String desc = "Refunded Delivery Charges "
					+ currencyInstance.format(totalRefundDeliveryCharges)
					+ " for Transaction Id: "
					+ refundEntry.getKey().getTransactionID() + "- Reason: "
					+ refundEntry.getValue().getReason();
			if (StringUtils.isNotEmpty(refundEntry.getValue().getNotes())) {
				desc = desc + ", Notes: " + refundEntry.getValue().getNotes();
			}
			entry.setDescription(desc);
			entry.setPreviousOrderVersion(snapshot);
			modelService.saveAll(orderModel, refundEntryModel);
			modelService.saveAll(orderModel, entry);
		}
		return getCockpitTypeService().wrapItem(orderModel);
	}

	@Override
	public boolean isOrderCODforManualRefund(OrderModel order,
			List<OrderEntryModel> orderEntryModel) {
		List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(
				order.getPaymentTransactions());
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(tranactions)) {
			for (PaymentTransactionModel transaction : tranactions) {
				if (CollectionUtils.isNotEmpty(transaction.getEntries())) {
					for (PaymentTransactionEntryModel entry : transaction
							.getEntries()) {
						if (entry.getPaymentMode() != null
								&& entry.getPaymentMode().getMode() != null
								&& entry.getPaymentMode().getMode()
										.equalsIgnoreCase("COD")) {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					break;
				}
			}
		}
		if (flag) {
			Double totalRefundAmount = 0d;

			for (OrderEntryModel orderEntry : orderEntryModel) {
				ConsignmentModel consignment = orderEntry
						 .getConsignmentEntries().iterator().next()
						 .getConsignment();
				if (consignment.getDeliveryDate() != null) {
					totalRefundAmount += orderEntry.getNetAmountAfterAllDisc();
				}
			}

			PaymentTransactionModel paymentTransactionModel = null;

			if (totalRefundAmount > 0D) {
				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(order, "FAILURE",
								totalRefundAmount,
								PaymentTransactionType.RETURN, "FAILURE", UUID
										.randomUUID().toString());
				mplJusPayRefundService.attachPaymentTransactionModel(order,
						paymentTransactionModel);
			}
			for (OrderEntryModel orderEntry : orderEntryModel) {
				ConsignmentStatus newStatus = null;
				
				 ConsignmentModel consignment = orderEntry
				 .getConsignmentEntries().iterator().next()
				 .getConsignment();
				 
				if (consignment.getDeliveryDate() != null) {
					newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
				} else {
					newStatus = ConsignmentStatus.COD_CLOSED_WITHOUT_REFUND;
				}
				// Do not save status updates in commerce, rather update it on
				// OMS and let it come in sync.
				// modelService.save(consignment);

				if (paymentTransactionModel != null) {
					mplJusPayRefundService.makeRefundOMSCall(orderEntry,
							paymentTransactionModel,
							orderEntry.getNetAmountAfterAllDisc(), newStatus,null);
				}
				mplJusPayRefundService.makeOMSStatusUpdate(orderEntry,
						newStatus);

			}

			return true;
		}
		return false;
	}

	@Override
	public Map<String, List<TypedObject>> getOrderEntriesGroupByAWB(
			OrderModel orderModel) {
		if (orderModel != null) {
			List<AbstractOrderEntryModel> orderEntries = orderModel
					.getEntries();
			if (CollectionUtils.isNotEmpty(orderEntries)) {
				Map<String, List<TypedObject>> results = new HashMap<>();
				for (AbstractOrderEntryModel entry : orderEntries) {
					ConsignmentModel consignmentModel = getConsignment(entry);
					if (MarketplaceCockpitsConstants.validInvoiceStatus.contains(consignmentModel.getStatus())) {
						String awbNumber = consignmentModel != null ? consignmentModel
								.getTrackingID() : null;
						if (StringUtils.isNotBlank(awbNumber)) {
							List<TypedObject> entries = results.get(awbNumber);
							if (entries == null) {
								entries = new ArrayList<TypedObject>();
								results.put(awbNumber, entries);
							}
							entries.add(getCockpitTypeService().wrapItem(entry));

						}
					}
				}
				return results;
			}

		}
		return null;
	}

	protected ConsignmentModel getConsignment(
			AbstractOrderEntryModel orderEntryModel) {
		if (CollectionUtils.isNotEmpty(orderEntryModel.getConsignmentEntries())) {
			ConsignmentEntryModel consignmentEntry = orderEntryModel
					.getConsignmentEntries().iterator().next();
			return consignmentEntry.getConsignment();
		}
		return null;
	}

	@Override
	public boolean sendInvoice(List<TypedObject> orderLineId, String orderId) {
		String customerEmail = null;

		//car-80
		OrderModel orderModel = orderModelService.getOrder(orderId);
		CustomerModel customerModel = (CustomerModel) getCallContextController()
				.getCurrentCustomer().getObject();
		customerEmail = customerModel.getOriginalUid();

		if (CollectionUtils.isNotEmpty(orderLineId)) {
			for (TypedObject id : orderLineId) {
				if (id != null && id.getObject() != null
						&& id.getObject() instanceof AbstractOrderEntryModel) {
					AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) id
							.getObject();
					ConsignmentModel consignmet = getConsignment(orderEntry);
					if (consignmet != null) {
						SendInvoiceData invoiceData = new SendInvoiceData();
						invoiceData.setCustomerEmail(customerEmail);
						InvoiceDetailModel invoiceDetails = consignmet
								.getInvoice();
						if (invoiceDetails != null) {
							invoiceData.setInvoiceUrl(invoiceDetails
									.getInvoiceUrl());
						}
						invoiceData
								.setLineItemId(orderEntry.getTransactionID());
						invoiceData
						.setTransactionId(orderEntry.getTransactionID());
						invoiceData.setOrdercode(orderId);
						try {
							//car-80:orderModel added as parameter.
							registerCustomerFacade.sendInvoice(invoiceData,
									customerModel, orderModel);
							LOG.info("Invoice Sending Succesfull");
							return true;
						} catch (EtailNonBusinessExceptions e) {
							LOG.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		return false;
	}
	

	@Override
	public boolean isOrderCODforDeliveryCharges(OrderModel order,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap) {
		List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(
				order.getPaymentTransactions());
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(tranactions)) {
			for (PaymentTransactionModel transaction : tranactions) {
				if (CollectionUtils.isNotEmpty(transaction.getEntries())) {
					for (PaymentTransactionEntryModel entry : transaction
							.getEntries()) {
						if (entry.getPaymentMode() != null
								&& entry.getPaymentMode().getMode() != null
								&& entry.getPaymentMode().getMode()
										.equalsIgnoreCase("COD")) {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					break;
				}
			}
		}
		if (flag) {
			Double totalRefundDeliveryCharges = Double.valueOf(0);
			if (MapUtils.isNotEmpty(refundMap)) {
				for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
						.entrySet()) {
					totalRefundDeliveryCharges = totalRefundDeliveryCharges
							+ refundEntry.getKey().getCurrDelCharge();

					refundEntry.getKey().setRefundedDeliveryChargeAmt(
							refundEntry.getKey().getCurrDelCharge());
					refundEntry.getKey().setCurrDelCharge(Double.valueOf(0));
					modelService.save(refundEntry.getKey());
				}
			}
			PaymentTransactionModel paymentTransactionModel = mplJusPayRefundService
					.createPaymentTransactionModel(order, "FAILURE",
							totalRefundDeliveryCharges,
							PaymentTransactionType.REFUND_DELIVERY_CHARGES,
							"FAILURE", UUID.randomUUID().toString());
			mplJusPayRefundService.attachPaymentTransactionModel(order,
					paymentTransactionModel);
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean isOrderCODforScheduleDeliveryCharges(OrderModel order,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap) {
		List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(
				order.getPaymentTransactions());
		boolean flag = false;
		flag = checkIsOrderCod(tranactions);
		if (flag) {
			Double totalRefundScheduleDeliveryCharges = Double.valueOf(0);
			if (MapUtils.isNotEmpty(refundMap)) {
				for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
						.entrySet()) {
					totalRefundScheduleDeliveryCharges = totalRefundScheduleDeliveryCharges
							+ refundEntry.getKey().getScheduledDeliveryCharge();

					refundEntry.getKey().setRefundedScheduleDeliveryChargeAmt(
							refundEntry.getKey().getScheduledDeliveryCharge());
					refundEntry.getKey().setScheduledDeliveryCharge(Double.valueOf(0));
					modelService.save(refundEntry.getKey());
				}
			}
			PaymentTransactionModel paymentTransactionModel = mplJusPayRefundService
					.createPaymentTransactionModel(order, "FAILURE",
							totalRefundScheduleDeliveryCharges,
							PaymentTransactionType.REFUND_SCHEDULE_DELIVERY_CHARGES,
							"FAILURE", UUID.randomUUID().toString());
			mplJusPayRefundService.attachPaymentTransactionModel(order,
					paymentTransactionModel);
			return true;
		}
		return false;
	}
	
	private boolean checkIsOrderCod(List<PaymentTransactionModel> tranactions) {
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(tranactions)) {
			for (PaymentTransactionModel transaction : tranactions) {
				if (CollectionUtils.isNotEmpty(transaction.getEntries())) {
					for (PaymentTransactionEntryModel entry : transaction
							.getEntries()) {
						if (entry.getPaymentMode() != null
								&& entry.getPaymentMode().getMode() != null
								&& entry.getPaymentMode().getMode()
										.equalsIgnoreCase("COD")) {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					break;
				}
			}
		}
		return flag;
	}

	@Override
	public boolean syncOrder(OrderModel order){
		LOG.debug("Going for Manal Sync of Order" +order.getCode());
		order=getParentOrder(order);
		customOmsOrderSyncAdapter.update(omsOrderService.getOrderByOrderId(order.getCode()), new Date());
		LOG.debug("Manal Order Sync is Completed Successfully" +order.getCode());
		return true;
	}

	private OrderModel getParentOrder(OrderModel order) {
		// TODO Auto-generated method stub
		if(order.getParentReference()!=null){
			order=order.getParentReference();
			getParentOrder(order);
		}
		return order;
	}

	@Override
	public String getShortUrl(String orderCode) {
		try {
			return mplOrderDao.getShortUrl(orderCode);
		}catch(Exception e) {
			LOG.error("Exception while getting the short-url for order:"+orderCode);
		}
		return null;
	}
	
	
	private double calculateSplitJuspayRefundAmount(AbstractOrderEntryModel orderEntry){
		double refundAmount =0.0D;
		if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue()){
			refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue()).doubleValue();
		}
		if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue()){
			refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue()).doubleValue();
		}
		if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue()){
			refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue()).doubleValue();
		}
		if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue()){
			refundAmount += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue()).doubleValue();
		}
		return refundAmount;
	}
	
	private double calculateSplitQcRefundAmount(AbstractOrderEntryModel orderEntry){
		double refundAmountForQc =0.0D;
		double cashBackAmt=0;
		if(null !=orderEntry &&  null != orderEntry.getWalletApportionPaymentInfo() && null!= orderEntry.getWalletApportionPaymentInfo().getWalletCardList()){
			for(WalletCardApportionDetailModel cardApportionDetail : orderEntry.getWalletApportionPaymentInfo().getWalletCardList()){
				if(cardApportionDetail.getBucketType().equalsIgnoreCase("CASHBACK")){
					cashBackAmt += Double.parseDouble(cardApportionDetail.getQcApportionValue()) +  Double.parseDouble( null != cardApportionDetail.getQcDeliveryValue() ? cardApportionDetail.getQcDeliveryValue() : ""+0 )
							+Double.parseDouble( null != cardApportionDetail.getQcSchedulingValue() ? cardApportionDetail.getQcSchedulingValue() : ""+0 )+
							Double.parseDouble( null != cardApportionDetail.getQcShippingValue() ? cardApportionDetail.getQcShippingValue() : ""+0 );
				}
			}
		}
		if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getQcApportionPartValue()){
   		refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcApportionPartValue()).doubleValue();
   	}
   	if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getQcDeliveryPartValue()){
   		refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcDeliveryPartValue()).doubleValue();
   	}
   	if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getQcSchedulingPartValue()){
   		refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcSchedulingPartValue()).doubleValue();
   	}
   	if(null != orderEntry.getWalletApportionPaymentInfo() && null != orderEntry.getWalletApportionPaymentInfo().getQcShippingPartValue()){
   		refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcShippingPartValue()).doubleValue();
   	}
   	
   	
   	if(cashBackAmt > 0){
   		
   		refundAmountForQc -= cashBackAmt;
   	}
   	
		return refundAmountForQc;
	}
	
	private void constructQuickCilverOrderEntry(final QCRedeeptionResponse response,String transactionId,final OrderModel subOrderModel){
		
		
		 AbstractOrderEntryModel abstractOrderEntryModel = mplOrderService.getEntryModel(transactionId);
			final WalletApportionReturnInfoModel walletApportionModel = getModelService().create(WalletApportionReturnInfoModel.class);
		  List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();
		  String qcResponseStatus = "";
		 if(null != response && null != response.getCards()){
		  for(QCCard qcCard:response.getCards()){
				final WalletCardApportionDetailModel model = getModelService().create(WalletCardApportionDetailModel.class);
				model.setCardNumber(qcCard.getCardNumber());
				model.setCardExpiry(qcCard.getExpiry());
				model.setCardAmount(qcCard.getAmount().toString());
				model.setBucketType(qcCard.getBucketType());
				walletCardApportionDetailModelList.add(model);
			}
		   walletApportionModel.setTransactionId(response.getTransactionId().toString());
			walletApportionModel.setWalletCardList(walletCardApportionDetailModelList);
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue()){
				walletApportionModel.setQcApportionPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue()){
				walletApportionModel.setQcDeliveryPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue()){
				walletApportionModel.setQcSchedulingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue()){
				walletApportionModel.setQcShippingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue());
			}	
			walletApportionModel.setType("CANCEL");
			if(StringUtils.equalsIgnoreCase(response.getResponseCode().toString(),"0")){
				walletApportionModel.setStatusForQc("SUCCESS");
				qcResponseStatus= "SUCCESS";
			}else{
				walletApportionModel.setStatusForQc("PENDING");
				qcResponseStatus = "PENDING";
			}
			System.out.println("Before Saving Juspay Response is :"+walletApportionModel.getJuspayApportionValue());
			modelService.save(walletApportionModel);
			System.out.println("After Saving Juspay Response is :"+walletApportionModel.getJuspayApportionValue());
			
			abstractOrderEntryModel.setWalletApportionReturnInfo(walletApportionModel);
			System.out.println("Before setting  Order Entry Response is :"+walletApportionModel.getJuspayApportionValue());
			modelService.save(abstractOrderEntryModel);
			System.out.println("After setting  Order Entry Response is :"+walletApportionModel.getJuspayApportionValue());
	     LOG.debug("abstractOrderEntryModel Saved Successfully..............");
	     
	     final PaymentTransactionModel paymentTransactionModel = modelService.create(PaymentTransactionModel.class);
			paymentTransactionModel.setCode(response.getTransactionId().toString());
			paymentTransactionModel.setRequestId(response.getTransactionId().toString());
			paymentTransactionModel.setStatus(qcResponseStatus);
			paymentTransactionModel.setOrder(subOrderModel);
			final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService
					.create(PaymentTransactionEntryModel.class);
			paymentTransactionEntryModel.setCode(response.getTransactionId().toString());
			final BigDecimal bigAmount = new BigDecimal(response.getAmount().doubleValue(), MathContext.DECIMAL64);
			paymentTransactionEntryModel.setAmount(bigAmount);
			paymentTransactionEntryModel.setRequestId(response.getTransactionId().toString());
			paymentTransactionEntryModel.setTime(new Date());
			paymentTransactionEntryModel.setCurrency(subOrderModel.getCurrency());
			paymentTransactionEntryModel.setTransactionStatus(qcResponseStatus);
			paymentTransactionEntryModel.setTransactionStatusDetails(qcResponseStatus);
			PaymentTypeModel model= new PaymentTypeModel();
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
		
	}
	
	private WalletApportionReturnInfoModel constructQuickCilverOrderEntryForSplit(final QCRedeeptionResponse response,String transactionId){
		
		
		 AbstractOrderEntryModel abstractOrderEntryModel = mplOrderService.getEntryModel(transactionId);
			final WalletApportionReturnInfoModel walletApportionModel = getModelService().create(WalletApportionReturnInfoModel.class);
		  List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();
		 if(null != response && null != response.getCards()){
		  for(QCCard qcCard:response.getCards()){
				final WalletCardApportionDetailModel model = getModelService().create(WalletCardApportionDetailModel.class);
				model.setCardNumber(qcCard.getCardNumber());
				model.setCardExpiry(qcCard.getExpiry());
				model.setCardAmount(qcCard.getAmount().toString());
				model.setBucketType(qcCard.getBucketType());
				walletCardApportionDetailModelList.add(model);
			}
		  if(null != response.getTransactionId() ){
		   walletApportionModel.setTransactionId(response.getTransactionId().toString());
		  }
			walletApportionModel.setWalletCardList(walletCardApportionDetailModelList);
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue()){
				walletApportionModel.setQcApportionPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue()){
				walletApportionModel.setQcDeliveryPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue()){
				walletApportionModel.setQcSchedulingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue()){
				walletApportionModel.setQcShippingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue());
			}	
			walletApportionModel.setType("CANCEL");
			if(StringUtils.equalsIgnoreCase(response.getResponseCode().toString(),"0")){
				walletApportionModel.setStatusForQc("SUCCESS");
			}else{
				walletApportionModel.setStatusForQc("PENDING");
			}
		 }
		return walletApportionModel;
	}
	
	private void saveQCandJuspayResponse(final OrderEntryModel orderEntry,final PaymentTransactionModel paymentTransactionModel,final WalletApportionReturnInfoModel returnModel ,final OrderModel subOrderModel){
		WalletApportionReturnInfoModel walletApportionModel = getModelService().create(WalletApportionReturnInfoModel.class);
		
		 List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();
		 String qcResponseStatus ="";
		 if(null != returnModel && null != returnModel.getWalletCardList()){
		  for(WalletCardApportionDetailModel qcCard:returnModel.getWalletCardList()){
				final WalletCardApportionDetailModel model = getModelService().create(WalletCardApportionDetailModel.class);
				model.setCardNumber(qcCard.getCardNumber());
				model.setCardExpiry(qcCard.getCardExpiry());
				model.setCardAmount(qcCard.getCardAmount().toString());
				model.setBucketType(qcCard.getBucketType());
				walletCardApportionDetailModelList.add(model);
			}
   		  walletApportionModel.setWalletCardList(walletCardApportionDetailModelList);
   		  walletApportionModel.setTransactionId(returnModel.getTransactionId());
   		if( null!= returnModel.getQcApportionPartValue()){
   			walletApportionModel.setQcApportionPartValue(returnModel.getQcApportionPartValue());
   		}
   		if(null!= returnModel.getQcDeliveryPartValue()){
   			walletApportionModel.setQcDeliveryPartValue(returnModel.getQcDeliveryPartValue());
   		}
   		if(null!= returnModel.getQcSchedulingPartValue()){
   			walletApportionModel.setQcSchedulingPartValue(returnModel.getQcSchedulingPartValue());
   		}
   		if(null!= returnModel.getQcShippingPartValue()){
   			walletApportionModel.setQcShippingPartValue(returnModel.getQcShippingPartValue());
   		}	
		}
		if( null!= orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue()){
			walletApportionModel.setJuspayApportionValue(orderEntry.getWalletApportionPaymentInfo().getJuspayApportionValue());
		}
		if(null !=orderEntry.getWalletApportionPaymentInfo() && null!= orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue()){
			walletApportionModel.setJuspayDeliveryValue(orderEntry.getWalletApportionPaymentInfo().getJuspayDeliveryValue());
		}
		if(null !=orderEntry.getWalletApportionPaymentInfo() && null!= orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue()){
			walletApportionModel.setJuspaySchedulingValue(orderEntry.getWalletApportionPaymentInfo().getJuspaySchedulingValue());
		}
		if(null !=orderEntry.getWalletApportionPaymentInfo() && null!= orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue()){
			walletApportionModel.setJuspayShippingValue(orderEntry.getWalletApportionPaymentInfo().getJuspayShippingValue());
		}
		walletApportionModel.setOrderId(orderEntry.getOrder().getCode());
		walletApportionModel.setType("CANCEL");
		if(null != returnModel.getStatusForQc() ){
			walletApportionModel.setStatusForQc(returnModel.getStatusForQc());
			qcResponseStatus= "SUCCESS";
		}else{
			qcResponseStatus = "PENDING";
		}
		
		if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), MarketplacecommerceservicesConstants.SUCCESS)){
			walletApportionModel.setStatus("SUCCESS");
		}else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "PENDING")){
			walletApportionModel.setStatus("PENDING");
		}
		
		System.out.println("Before Saving Juspay Response is :"+walletApportionModel.getJuspayApportionValue());
		modelService.save(walletApportionModel);
		System.out.println("After Saving Juspay Response is :"+walletApportionModel.getJuspayApportionValue());
		
		orderEntry.setWalletApportionReturnInfo(walletApportionModel);
		System.out.println("Before setting  Order Entry Response is :"+walletApportionModel.getJuspayApportionValue());
		modelService.save(orderEntry);
		System.out.println("After setting  Order Entry Response is :"+walletApportionModel.getJuspayApportionValue());
     LOG.debug("abstractOrderEntryModel Saved Successfully..............");
     
     final PaymentTransactionModel transactionModel = modelService.create(PaymentTransactionModel.class);
		transactionModel.setCode(returnModel.getTransactionId().toString());
		transactionModel.setRequestId(returnModel.getTransactionId().toString());
		transactionModel.setStatus(qcResponseStatus);
		transactionModel.setOrder(subOrderModel);
		final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService
				.create(PaymentTransactionEntryModel.class);
		paymentTransactionEntryModel.setCode(returnModel.getTransactionId().toString());
		final BigDecimal bigAmount = new BigDecimal(calculateSplitQcRefundAmount(orderEntry), MathContext.DECIMAL64);
		paymentTransactionEntryModel.setAmount(bigAmount);
		paymentTransactionEntryModel.setRequestId(returnModel.getTransactionId().toString());
		paymentTransactionEntryModel.setTime(new Date());
		paymentTransactionEntryModel.setCurrency(subOrderModel.getCurrency());
		paymentTransactionEntryModel.setTransactionStatus(qcResponseStatus);
		paymentTransactionEntryModel.setTransactionStatusDetails(qcResponseStatus);
		PaymentTypeModel model= new PaymentTypeModel();
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
		transactionModel.setEntries(entries);
		modelService.save(transactionModel);
		LOG.debug("Payment Transaction for Split Mode.... created SuccessFully......:");
	}
}
