/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.actions.order;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.TypeofReturn;
import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;

import com.tisl.mpl.core.enums.RefundFomType;
import com.tisl.mpl.core.model.InitiateRefundProcessModel;

import com.tisl.mpl.marketplacecommerceservices.service.MplNotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.response.QCCard;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.service.MplWalletServices;


/**
 * @author TCS
 *
 */
public class AutoRefundInitiateAction extends AbstractProceduralAction<InitiateRefundProcessModel> //SDI-2788
{
	private static final Logger LOG = Logger.getLogger(AutoRefundInitiateAction.class);
	private static final String REFUND_MODE_C = "C";
	private static final String REFUND_MODE_WALLET = "W";
	private static final String COD = "COD";

	private List<OrderEntryModel> refundList;
	private List<ReturnEntryModel> returnList;

	@Autowired
	private ModelService modelService;

	@Autowired
	private MplPaymentService mplPaymentService;

	@Autowired
	private MplNotificationService mplNotificationSaveService;

	@Autowired
	private MplWalletServices mplWalletServices;

	@Autowired
	private MplOrderService mplOrderService;

	@Autowired
	private ReturnService returnService;

	
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Override
	public void executeAction(final InitiateRefundProcessModel process) //SDI-2788
	{
		LOG.error("Inside AutoRefundInitiateAction");
		boolean refundedAtRts = false;
		boolean refundReasonSiteError = false;
		boolean refundedByWallet = false;

		//Changed for SDI-930
		final List<OrderEntryModel> refundList = new ArrayList<OrderEntryModel>();
		final List<ReturnEntryModel> returnList = new ArrayList<ReturnEntryModel>();

		//SDI-2788
		final String refundTransactionId = process.getRefundTransactionId();
		
		final OrderModel orderModel = process.getOrder();

		if (orderModel != null && !isOrderCOD(orderModel))
		{
			LOG.error("Inside AutoRefundInitiateAction for Order #" + orderModel.getCode());
			final List<ReturnRequestModel> returnRequestList = orderModel.getReturnRequests();
			if (CollectionUtils.isNotEmpty(returnRequestList))
			{
				for (final ReturnRequestModel returnRequest : returnRequestList)
				{
					if (CollectionUtils.isNotEmpty(returnRequest.getReturnEntries()))
					{
						for (final ReturnEntryModel returnEntry : returnRequest.getReturnEntries())
						{
							if (returnEntry instanceof RefundEntryModel)
							{
								//SDI-2788
								if (returnEntry.getOrderEntry() != null && returnEntry.getOrderEntry().getOrderLineId().equals(refundTransactionId)
										&& CollectionUtils.isNotEmpty(returnEntry.getOrderEntry().getConsignmentEntries()))
								{
									refundedAtRts = false;
									if (returnEntry instanceof RefundEntryModel && null != ((RefundEntryModel) returnEntry).getRefundMode()
											&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(REFUND_MODE_C)) // added for store return mode
									{
										refundedAtRts = true;
									}
									refundedByWallet = false;
									if (returnEntry instanceof RefundEntryModel && null != ((RefundEntryModel) returnEntry).getRefundMode()
											&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(REFUND_MODE_WALLET)) // added for wallet store return mode
									{
										refundedByWallet = true;
									}

									refundReasonSiteError = false;
									if (returnEntry instanceof RefundEntryModel && null != ((RefundEntryModel) returnEntry).getReason()
											&& ((RefundEntryModel) returnEntry).getReason().equals(RefundReason.SITEERROR)) // added for store return mode
									{
										refundReasonSiteError = true;
									}
									final ConsignmentStatus status = returnEntry.getOrderEntry().getConsignmentEntries().iterator().next()
											.getConsignment().getStatus();

									if (status.equals(ConsignmentStatus.RETURN_CLOSED) && !refundedAtRts && !refundedByWallet
											&& !refundReasonSiteError)
									{
										//SDI-2788
										populateRefundList(orderModel, refundList, returnList, refundTransactionId); //Changed for SDI-930

										if (CollectionUtils.isNotEmpty(refundList))
										{
											for (final OrderEntryModel orderEntry : refundList)
											{
												if (CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
												{
													final ConsignmentModel consignment = orderEntry.getConsignmentEntries().iterator().next()
															.getConsignment();
													consignment.setStatus(ConsignmentStatus.REFUND_INITIATED);
													//h2refund Added to know the refund type
													consignment.setRefundDetails(RefundFomType.AUTOMATIC);
													modelService.save(consignment);
													final OrderHistoryEntryModel historyEntry = modelService
															.create(OrderHistoryEntryModel.class);
													historyEntry.setTimestamp(Calendar.getInstance().getTime());
													historyEntry.setOrder(orderEntry.getOrder());
													historyEntry.setLineId(consignment.getCode());
													historyEntry.setDescription(ConsignmentStatus.REFUND_INITIATED.toString());
													modelService.save(historyEntry);
													modelService.refresh(historyEntry); //Added for SDI-930
													LOG.error("AutoRefundInitiateAction: historyEntry is set to REFUND_INITIATED for Order #"
															+ orderModel.getCode());
												}
											}

											//Start code for Split mode 
											String result = null;
											//String qcstatus = null;
											if (null != orderModel.getSplitModeInfo()
													&& orderModel.getSplitModeInfo().equalsIgnoreCase("Split"))
											{
												//Start Added the code for QC
												/*try
												{
													LOG.debug("Step :1 getting the consignment for ORDER ...");
													if (CollectionUtils.isNotEmpty(returnEntry.getOrderEntry().getConsignmentEntries()))
													{
														LOG.debug("Step :2 getting the consignment status for ..."
																+ returnEntry.getOrderEntry().getConsignmentEntries());

														LOG.debug("Step :8 this is NON - RETURNINITIATED_BY_RTO order ");
														final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = qcCallforReturnRefund(orderModel,
																(RefundEntryModel) returnEntry);
														 constructQuickCilverOrderEntry(walletCardApportionDetailModelList, returnEntry,orderModel);
														LOG.debug("Step :9 successfully create constructQuickCilverOrderEntry for this order");
													}
													//End Added the code for QC
												}
												catch (final Exception e)
												{
													e.getMessage();
													LOG.error("AutoRefundInitiateAction: Exception is getting while QC responce for Order #"
															+ orderModel.getCode());
												}*/
												try
												{
													LOG.error(
															"AutoRefundInitiateAction: Going to call mplPaymentService.doRefundPayment(refundList); for Order #"
																	+ orderModel.getCode());
													result = mplPaymentService.doRefundPayment(refundList,
															((RefundEntryModel) returnEntry).getAmount());
												}
												catch (final Exception e)
												{
													e.getMessage();
													LOG.error(
															"AutoRefundInitiateAction: After call mplPaymentService.doRefundPayment(refundList); for Order #"
																	+ orderModel.getCode());
												}

											}
											else if (null != orderModel.getSplitModeInfo()
													&& orderModel.getSplitModeInfo().equalsIgnoreCase("CliqCash"))
											{

												try
												{
													//Start Added the code for QC
													final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = qcCallforReturnRefund(orderModel,
															(RefundEntryModel) returnEntry);
													result = constructQuickCilverOrderEntry(walletCardApportionDetailModelList,returnEntry,orderModel);
													
													//End Added the code for QC
												}
												catch (final Exception e)
												{
													//End Added the code for QC
													e.getMessage();
													LOG.error("AutoRefundInitiateAction: Exception is getting while QC responce for Order #"
															+ orderModel.getCode());
							  						try{
							  							PaymentTransactionModel paymentTransactionModel= null;
							  							ConsignmentStatus newStatus =  ConsignmentStatus.REFUND_IN_PROGRESS;
							  							double refundAmount =0.0D;
							  							refundAmount = calculateSplitQcRefundAmount(returnEntry.getOrderEntry());
							  						     mplJusPayRefundService.makeRefundOMSCall(returnEntry.getOrderEntry(), paymentTransactionModel, Double.valueOf(refundAmount), newStatus, null);
							               	 }catch(Exception ex){
							               			ex.getMessage();
							               			LOG.error("Quck Cilver giving response code  Order Id :"+orderModel.getCode());
							               	} 

												}
											}
											else
											{
												
												
												try
												{
													LOG.error(
															"AutoRefundInitiateAction: Going to call mplPaymentService.doRefundPayment(refundList); for Order #"
																	+ orderModel.getCode());
													result = mplPaymentService.doRefundPayment(refundList,
															((RefundEntryModel) returnEntry).getAmount());
												}
												catch (final Exception e)
												{
													e.getMessage();
													LOG.error(
															"AutoRefundInitiateAction: After call mplPaymentService.doRefundPayment(refundList); for Order #"
																	+ orderModel.getCode());
												}
												
												if(((RefundEntryModel) returnEntry).getAmount() == null){
												LOG.error(
														"AutoRefundInitiateAction: Going to call mplPaymentService.doRefundPayment(refundList); for Order #"
																+ orderModel.getCode());
												result = mplPaymentService.doRefundPayment(refundList, null);
												LOG.error(
														"AutoRefundInitiateAction: After call mplPaymentService.doRefundPayment(refundList); for Order #"
																+ orderModel.getCode());
												}

											}
											final String[] resultArray = result.split(",");

											if (ArrayUtils.isNotEmpty(resultArray))
											{
												if (!resultArray[0].equalsIgnoreCase("FAILURE"))
												{
													for (final ReturnEntryModel returnEntryTmp : returnList)
													{
														returnEntryTmp.setStatus(ReturnStatus.RETURN_COMPLETED);

														LOG.error(
																"AutoRefundInitiateAction: returnEntryTmp.setStatus(ReturnStatus.RETURN_COMPLETED) for Order #"
																		+ orderModel.getCode());

														modelService.save(returnEntryTmp);
														//TODO :Return Initiated Notification
														try
														{
															//send Notification
															//SDI-2788 changed to refundlist to resolve duplicate returnrequest
															final int noOfItems = refundList.size();
															mplNotificationSaveService.sendRefundInitiatedNotification(noOfItems, returnEntryTmp,
																	orderModel);
															LOG.error(
																	"AutoRefundInitiateAction: mplNotificationSaveService.sendRefundInitiatedNotification( for Order #"
																			+ orderModel.getCode());
														}
														catch (final Exception e1)
														{
															LOG.error(
																	"Exception during sending Notification for automatic Refund Initiation>>> ",
																	e1);
														}

													}
												}
												else
												{
													for (final ReturnEntryModel returnEntryTmp : returnList)
													{
														returnEntryTmp.setStatus(ReturnStatus.RETURN_INPROGRESS);
														modelService.save(returnEntryTmp);
													}
												}
											}
											else
											{
												LOG.error("refundRequestedUnresponsive  for automatic Refund");
											}
										}
										else
										{
											LOG.error("errorNoRecordSelected for automatic Refund");
										}
									}
								}
							}
						}
					}
					else
					{
						LOG.error("No Request Available for automatic Refund");
					}
				}
			}
		}
	}


	public List<AbstractOrderEntryModel> associatedEntries(final OrderModel subOrderDetails, final String transactionId)
			throws Exception
	{
		final List<AbstractOrderEntryModel> orderEntries = new ArrayList<>();

		//final String associatedItemUssid = "";
		//final String productPromoCode = "";
		//TISSIT-1720
		final List<String> parentTransactionIdList = new ArrayList<String>();
		for (final AbstractOrderEntryModel subEntry : subOrderDetails.getEntries())
		{
			//Start TISPRO-249
			final String parentTransactionId = ((subEntry.getIsBOGOapplied().booleanValue() || subEntry.getGiveAway().booleanValue())
					&& mplOrderService.checkIfBuyABGetCApplied(subEntry)) ? subEntry.getBuyABGetcParentTransactionId()
							: subEntry.getParentTransactionID();
			//End TISPRO-249


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
			if (transactionId.equalsIgnoreCase(subEntry.getTransactionID()) || (CollectionUtils.isNotEmpty(parentTransactionIdList)
					&& parentTransactionIdList.contains(subEntry.getTransactionID())))

			{
				orderEntries.add(subEntry);
			}
		}

		return orderEntries;
	}

	private String constructQuickCilverOrderEntry(final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList, final ReturnEntryModel returnEntry,final OrderModel subOrderModel)
	{
		
         String result = "FAILURE";
         PaymentTransactionModel paymentTransactionModel= null;
         final OrderEntryModel abstractOrderEntryModel = (OrderEntryModel) returnEntry.getOrderEntry();
		   //final AbstractOrderEntryModel abstractOrderEntryModel = mplOrderService.getEntryModel(transactionId);
		   final List<WalletCardApportionDetailModel> walletCardApportionDetailList =new ArrayList<WalletCardApportionDetailModel>();
			final WalletApportionReturnInfoModel walletApportionReturnModel = getModelService().create(WalletApportionReturnInfoModel.class);
		   List<String> qcResponseStatus = new ArrayList<String>();
		   
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue()){
				walletApportionReturnModel.setQcApportionPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue()){
				walletApportionReturnModel.setQcDeliveryPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue()){
				walletApportionReturnModel.setQcSchedulingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue());
			}
			if(null !=abstractOrderEntryModel.getWalletApportionPaymentInfo() && null!= abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue()){
				walletApportionReturnModel.setQcShippingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue());
			}	
			if(null != walletCardApportionDetailModelList &&  walletCardApportionDetailModelList.size()>0){
				 
				 for(WalletCardApportionDetailModel walletCardApportionDetailModelObj :walletCardApportionDetailModelList){
						final WalletCardApportionDetailModel walletCardApportionDetailModel = getModelService().create(WalletCardApportionDetailModel.class);
						walletCardApportionDetailModel.setCardNumber(walletCardApportionDetailModelObj.getCardNumber());
						walletCardApportionDetailModel.setCardExpiry(walletCardApportionDetailModelObj.getCardExpiry());
						walletCardApportionDetailModel.setCardAmount(walletCardApportionDetailModelObj.getCardAmount().toString());
						walletCardApportionDetailModel.setBucketType(walletCardApportionDetailModelObj.getBucketType());
						walletCardApportionDetailModel.setQcApportionValue(walletCardApportionDetailModelObj.getQcApportionValue());
						walletCardApportionDetailModel.setQcDeliveryValue(walletCardApportionDetailModelObj.getQcDeliveryValue());
						walletCardApportionDetailModel.setQcSchedulingValue(walletCardApportionDetailModelObj.getQcSchedulingValue());
						walletCardApportionDetailModel.setQcShippingValue(walletCardApportionDetailModelObj.getQcShippingValue());
						walletCardApportionDetailModel.setTransactionId(walletCardApportionDetailModelObj.getTransactionId());
						walletCardApportionDetailModel.setTrnsStatus(walletCardApportionDetailModelObj.getTrnsStatus());
						walletCardApportionDetailList.add(walletCardApportionDetailModel);
						qcResponseStatus.add(walletCardApportionDetailModelObj.getTrnsStatus());
						paymentTransactionModel = createPaymentEntryForQCTransaction(subOrderModel,walletCardApportionDetailModel);
				 }
			 }
			 walletApportionReturnModel.setWalletCardList(walletCardApportionDetailList);
			 walletApportionReturnModel.setTransactionId(returnEntry.getOrderEntry().getTransactionID());
			 walletApportionReturnModel.setType("RETURN");
			if(qcResponseStatus.contains("PENDING")){
        	 walletApportionReturnModel.setStatus("PENDING");
         }else{
        	 walletApportionReturnModel.setStatus("SUCCESS");
        	result="SUCCESS";
         }
			modelService.save(walletApportionReturnModel);
			abstractOrderEntryModel.setWalletApportionReturnInfo(walletApportionReturnModel);
			modelService.save(abstractOrderEntryModel);
			
  					ConsignmentStatus newStatus = null;
  						if (StringUtils.equalsIgnoreCase(result,MarketplacecommerceservicesConstants.SUCCESS))
  						{
  							newStatus = ConsignmentStatus.RETURN_COMPLETED;
  						}
  						else if (StringUtils.equalsIgnoreCase(result, "PENDING"))
  						{
  							newStatus = ConsignmentStatus.REFUND_INITIATED;
  						}
  						else
  						{
  							newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
  						}


  						//modelService.save(abstractOrderEntryModel);
  						LOG.debug("****** initiateRefund : Step 3  >>Payment transaction mode is not null >> Calling OMS with status as received from JUSPAY "
  								+ newStatus.getCode());
  						try{
  						     mplJusPayRefundService.makeRefundOMSCall(abstractOrderEntryModel, paymentTransactionModel, Double.valueOf(calculateSplitQcRefundAmount(abstractOrderEntryModel)), newStatus, null);
               	 }catch(Exception e){
               			e.getMessage();
               			LOG.error("Quck Cilver giving response code  Order Id :"+subOrderModel.getParentReference().getCode());
               	} 
			return result;
		}


	private List<WalletCardApportionDetailModel>  qcCallforReturnRefund(final OrderModel orderModel, final RefundEntryModel returnEntry)
	{
		LOG.debug("AutoRefundInitiateAction: Going to call QC  mplWalletServices.qcCredit(walletInfo); for Order #"
				+ orderModel.getCode());
		String walletId = null;
		QCRedeeptionResponse qcRedeeptionResponse = null;
		List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();
		final CustomerModel customerModel = (CustomerModel) orderModel.getUser();

		try
		{

			if (null != customerModel && null != customerModel.getCustomerWalletDetail())
			{
				walletId = customerModel.getCustomerWalletDetail().getWalletId();
			}
			
			DecimalFormat decimalFormat =new DecimalFormat("#.00");
			AbstractOrderEntryModel abstractOrderEntryModel =returnEntry.getOrderEntry();
			 if(null != abstractOrderEntryModel && null != abstractOrderEntryModel.getWalletApportionPaymentInfo()){
   			 for(WalletCardApportionDetailModel cardApportionDetail : abstractOrderEntryModel.getWalletApportionPaymentInfo().getWalletCardList()){
   				 double qcCliqCashAmt =0.0D;
   					if(null != cardApportionDetail && null!= cardApportionDetail.getBucketType()){
   					if(!cardApportionDetail.getBucketType().equalsIgnoreCase("CASHBACK")){
   						 qcCliqCashAmt = Double.parseDouble(cardApportionDetail.getQcApportionValue()) +  Double.parseDouble( null != cardApportionDetail.getQcDeliveryValue() ? cardApportionDetail.getQcDeliveryValue() : ""+0 )
   								+Double.parseDouble( null != cardApportionDetail.getQcSchedulingValue() ? cardApportionDetail.getQcSchedulingValue() : ""+0 )+
   								Double.parseDouble( null != cardApportionDetail.getQcShippingValue() ? cardApportionDetail.getQcShippingValue() : ""+0 );
   					     
   						   QCCreditRequest qcCreditRequest =new QCCreditRequest();
   		    	      	qcCreditRequest.setAmount(decimalFormat.format(qcCliqCashAmt));
   		    	      	qcCreditRequest.setInvoiceNumber(mplPaymentService.createQCPaymentId());
   		    	      	qcCreditRequest.setNotes("Cancel for "+ decimalFormat.format(qcCliqCashAmt));    	
   		    	      	qcRedeeptionResponse = mplWalletServices.qcCredit(walletId, qcCreditRequest);
   		    	      	walletCardApportionDetailModelList.add(getQcWalletCardResponse(qcRedeeptionResponse,cardApportionDetail));
   					
   					}
   				 }
   			 }
   		 }
		}
		catch (final Exception e)
		{
			LOG.error(
					"AutoRefundInitiateAction: After call mplWalletServices.qcCredit(walletInfo); for Order #" + orderModel.getCode());
		}
		return walletCardApportionDetailModelList;
	}


	private boolean isOrderCOD(final OrderModel order)
	{
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

	protected void populateRefundList(final OrderModel orderModel, final List refundList, final List returnList, final String refundTransactionId) //Changed for SDI-930
	{
		boolean refundedAtRts = false;
		boolean refundedByWallet = false;
		Set<String> uniqueTransactionSet = new HashSet<String>();

		if (orderModel != null)
		{
			final List<ReturnRequestModel> returnRequestList = orderModel.getReturnRequests();

			if (null != refundList)
			{
				refundList.clear();
			}
			if (null != returnList)
			{
				returnList.clear();
			}


			if (CollectionUtils.isNotEmpty(returnRequestList))
			{
				for (final ReturnRequestModel returnRequest : returnRequestList)
				{
					if (CollectionUtils.isNotEmpty(returnRequest.getReturnEntries()))
					{
						for (final ReturnEntryModel returnEntry : returnRequest.getReturnEntries())
						{
							if (returnEntry instanceof RefundEntryModel)
							{
								//SDI-2788
								if (returnEntry.getOrderEntry() != null && null != returnEntry.getOrderEntry().getOrderLineId()
										&& returnEntry.getOrderEntry().getOrderLineId().equals(refundTransactionId)
										&& CollectionUtils.isNotEmpty(returnEntry.getOrderEntry().getConsignmentEntries()))
								{
									final ConsignmentStatus status = returnEntry.getOrderEntry().getConsignmentEntries().iterator().next()
											.getConsignment().getStatus();

									refundedAtRts = false;
									if (returnEntry instanceof RefundEntryModel && null != ((RefundEntryModel) returnEntry).getRefundMode()
											&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(REFUND_MODE_C)) // added for store return mode
									{
										refundedAtRts = true;
									}
									refundedByWallet = false;
									if (returnEntry instanceof RefundEntryModel && null != ((RefundEntryModel) returnEntry).getRefundMode()
											&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(REFUND_MODE_WALLET)) // added for wallet store return mode
									{
										refundedByWallet = true;
									}


									if (status.equals(ConsignmentStatus.RETURN_CLOSED) && !refundedAtRts && !refundedByWallet)
									{
										boolean isUnique = true;
										final ReturnEntryModel returnEntryModel = returnEntry;
										final OrderEntryModel orderEntryModel = (OrderEntryModel) returnEntryModel.getOrderEntry();
										isUnique = uniqueTransactionSet.add(orderEntryModel.getOrderLineId());
										if(isUnique)
										{
											refundList.add(orderEntryModel);
										}
										returnList.add(returnEntryModel);
									}
								}
							}
						}
					}
				}
			}
		}
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

	private double calculateSplitQcRefundAmount(final AbstractOrderEntryModel orderEntry)
	{
		double refundAmountForQc = 0.0D;
		double cashBackAmt = 0;
		if (null != orderEntry && null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getWalletCardList())
		{
			for (final WalletCardApportionDetailModel cardApportionDetail : orderEntry.getWalletApportionPaymentInfo()
					.getWalletCardList())
			{
				if (null != cardApportionDetail && null != cardApportionDetail.getBucketType())
				{
					if (cardApportionDetail.getBucketType().equalsIgnoreCase("CASHBACK"))
					{
						cashBackAmt += Double.parseDouble(cardApportionDetail.getQcApportionValue())
								+ Double.parseDouble(null != cardApportionDetail.getQcDeliveryValue()
										? cardApportionDetail.getQcDeliveryValue() : "" + 0)
								+ Double.parseDouble(null != cardApportionDetail.getQcSchedulingValue()
										? cardApportionDetail.getQcSchedulingValue() : "" + 0)
								+ Double.parseDouble(null != cardApportionDetail.getQcShippingValue()
										? cardApportionDetail.getQcShippingValue() : "" + 0);
					}
				}
			}
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcApportionPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcApportionPartValue()).doubleValue();
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcDeliveryPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcDeliveryPartValue()).doubleValue();
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcSchedulingPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcSchedulingPartValue()).doubleValue();
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcShippingPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcShippingPartValue()).doubleValue();
		}


		if (cashBackAmt > 0)
		{

			refundAmountForQc -= cashBackAmt;
		}

		return refundAmountForQc;
	}
	
	
	private WalletCardApportionDetailModel getQcWalletCardResponse(QCRedeeptionResponse response, WalletCardApportionDetailModel walletObject){
		 final WalletCardApportionDetailModel walletCardApportionDetailModel =new WalletCardApportionDetailModel();
		 if(null != response && null != response.getCards()){
			  for(QCCard qcCard:response.getCards()){
				  walletCardApportionDetailModel.setCardNumber(qcCard.getCardNumber());
				  walletCardApportionDetailModel.setCardExpiry(qcCard.getExpiry());
				  walletCardApportionDetailModel.setCardAmount(qcCard.getAmount().toString());
				  walletCardApportionDetailModel.setBucketType(qcCard.getBucketType());
				}
			  if(StringUtils.equalsIgnoreCase(response.getResponseCode().toString(),"0")){
				  walletCardApportionDetailModel.setTrnsStatus("SUCCESS");
				}else{
					walletCardApportionDetailModel.setTrnsStatus("PENDING");
				}
			  walletCardApportionDetailModel.setTransactionId(response.getTransactionId().toString());
			  walletCardApportionDetailModel.setQcApportionValue(walletObject.getQcApportionValue());
			  walletCardApportionDetailModel.setQcDeliveryValue(walletObject.getQcDeliveryValue());
			  walletCardApportionDetailModel.setQcSchedulingValue(walletObject.getQcSchedulingValue());
			  walletCardApportionDetailModel.setQcShippingValue(walletObject.getQcShippingValue());
		 }
		
		return walletCardApportionDetailModel;
	}
	
private PaymentTransactionModel createPaymentEntryForQCTransaction(final OrderModel subOrderModel,final WalletCardApportionDetailModel walletCardApportionDetailModel){
		
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
  			paymentTransactionEntryModel.setType(PaymentTransactionType.RETURN);
  			modelService.save(paymentTransactionEntryModel);
  			final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
  			entries.add(paymentTransactionEntryModel);
  			paymentTransactionModel.setEntries(entries);
  			modelService.save(paymentTransactionModel);
  			LOG.debug("Payment Transaction created SuccessFully......:");
     return paymentTransactionModel;
	}
	

	private String getReasonDesc(final String reasonCode)
	{

		//Get the reason from Global Code master
		String reasonDescription = null;
		final List<ReturnReasonData> returnReasonList = mplOrderService.getReturnReasonForOrderItem();
		for (final ReturnReasonData returnReasonData : returnReasonList)
		{
			if (returnReasonData.getCode().equalsIgnoreCase(reasonCode))
			{
				if (StringUtils.isNotEmpty(returnReasonData.getReasonDescription()))
				{
					reasonDescription = returnReasonData.getReasonDescription();
				}
				break;
			}
		}
		//End

		LOG.info("****==Actual return reason desc from Global code master : " + reasonDescription);
		return reasonDescription;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the mplNotificationSaveService
	 */
	public MplNotificationService getMplNotificationSaveService()
	{
		return mplNotificationSaveService;
	}

	/**
	 * @param mplNotificationSaveService
	 *           the mplNotificationSaveService to set
	 */
	public void setMplNotificationSaveService(final MplNotificationService mplNotificationSaveService)
	{
		this.mplNotificationSaveService = mplNotificationSaveService;
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
}
