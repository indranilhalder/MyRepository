/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.actions.order;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.WalletApportionPaymentInfoModel;
import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplNotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.response.QCCard;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.service.MplWalletServices;


/**
 * @author TCS
 *
 */
public class AutoRefundInitiateAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(AutoRefundInitiateAction.class);
	private static final String REFUND_MODE_C = "C";
	private static final String REFUND_MODE_WALLET = "W";

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


	@Override
	public void executeAction(final OrderProcessModel process)
	{
		LOG.error("Inside AutoRefundInitiateAction");
		boolean refundedAtRts = false;
		boolean refundReasonSiteError = false;
		boolean refundedByWallet = false;

		refundList = Collections.synchronizedList(new ArrayList<OrderEntryModel>());
		returnList = Collections.synchronizedList(new ArrayList<ReturnEntryModel>());

		final OrderModel orderModel = process.getOrder();
		LOG.error("Inside AutoRefundInitiateAction for Order #" + orderModel.getCode());
		if (orderModel != null && !isOrderCOD(orderModel))
		{
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
								if (returnEntry.getOrderEntry() != null
										&& CollectionUtils.isNotEmpty(returnEntry.getOrderEntry().getConsignmentEntries()))
								{
									refundedAtRts = false;
									if (returnEntry instanceof RefundEntryModel
											&& null != ((RefundEntryModel) returnEntry).getRefundMode()
											&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(REFUND_MODE_C)) // added for store return mode
									{
										refundedAtRts = true;
									}
									refundedByWallet = false;
									if (returnEntry instanceof RefundEntryModel
											&& null != ((RefundEntryModel) returnEntry).getRefundMode()
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
										populateRefundList(orderModel);

										if (CollectionUtils.isNotEmpty(refundList))
										{
											for (final OrderEntryModel orderEntry : refundList)
											{
												if (CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
												{
													final ConsignmentModel consignment = orderEntry.getConsignmentEntries().iterator().next()
															.getConsignment();
													consignment.setStatus(ConsignmentStatus.REFUND_INITIATED);
													modelService.save(consignment);
													final OrderHistoryEntryModel historyEntry = modelService
															.create(OrderHistoryEntryModel.class);
													historyEntry.setTimestamp(Calendar.getInstance().getTime());
													historyEntry.setOrder(orderEntry.getOrder());
													historyEntry.setLineId(consignment.getCode());
													historyEntry.setDescription(ConsignmentStatus.REFUND_INITIATED.toString());
													modelService.save(historyEntry);
													LOG.error("AutoRefundInitiateAction: historyEntry is set to REFUND_INITIATED for Order #"
															+ orderModel.getCode());
												}
											}
											
											//Start code for Split mode 
											 String result =null;
											 boolean qcstatus = false ;
											 if(null != orderModel.getSplitModeInfo() && orderModel.getSplitModeInfo().equalsIgnoreCase("Split")){
													//Start Added the code for QC
												 try{
												 QCRedeeptionResponse  response =qcCallforReturnRefund( orderModel ,(RefundEntryModel) returnEntry);
												 qcstatus = constructQuickCilverOrderEntry(response,returnEntry.getOrderEntry().getTransactionID());
												//End Added the code for QC
												 }catch(Exception e){
													 //End Added the code for QC
													 e.getMessage();
													 LOG.error("AutoRefundInitiateAction: Exception is getting while QC responce for Order #"
																+ orderModel.getCode());
												 }
												 try{
												 LOG.error("AutoRefundInitiateAction: Going to call mplPaymentService.doRefundPayment(refundList); for Order #"
															+ orderModel.getCode());
													 result = mplPaymentService.doRefundPayment(refundList,((RefundEntryModel) returnEntry).getAmount());
												 }catch(Exception e){
													 e.getMessage();
													 LOG.error("AutoRefundInitiateAction: After call mplPaymentService.doRefundPayment(refundList); for Order #"
																+ orderModel.getCode());
												 }
													
											} else if (null != orderModel.getSplitModeInfo()  && orderModel.getSplitModeInfo().equalsIgnoreCase("CliqCash")){
											
												 try{
											//Start Added the code for QC
												 QCRedeeptionResponse  response =qcCallforReturnRefund( orderModel ,(RefundEntryModel) returnEntry);
												 qcstatus = constructQuickCilverOrderEntry(response,returnEntry.getOrderEntry().getTransactionID());
													//End Added the code for QC
												 }catch(Exception e){
													 //End Added the code for QC
													 e.getMessage();
													 LOG.error("AutoRefundInitiateAction: Exception is getting while QC responce for Order #"
																+ orderModel.getCode());
												 }
											}else {
											LOG.error("AutoRefundInitiateAction: Going to call mplPaymentService.doRefundPayment(refundList); for Order #"
															+ orderModel.getCode());
													 result = mplPaymentService.doRefundPayment(refundList ,null);
													LOG.error("AutoRefundInitiateAction: After call mplPaymentService.doRefundPayment(refundList); for Order #"
															+ orderModel.getCode());
											
											
											}
											final String[] resultArray = result.split(",");

											if (ArrayUtils.isNotEmpty(resultArray))
											{
												if (!resultArray[0].equalsIgnoreCase("FAILURE"))
												{
													for (final ReturnEntryModel returnEntryTmp : returnList)
													{
														returnEntryTmp.setStatus(ReturnStatus.RETURN_COMPLETED);

														LOG.error("AutoRefundInitiateAction: returnEntryTmp.setStatus(ReturnStatus.RETURN_COMPLETED) for Order #"
																+ orderModel.getCode());

														modelService.save(returnEntryTmp);
														//TODO :Return Initiated Notification
														try
														{
															//send Notification
															final int noOfItems = returnList.size();
															mplNotificationSaveService.sendRefundInitiatedNotification(noOfItems,
																	returnEntryTmp, orderModel);
															LOG.error("AutoRefundInitiateAction: mplNotificationSaveService.sendRefundInitiatedNotification( for Order #"
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
								else
								{
									LOG.error("No Order Entry or No Consignment for automatic Refund");
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
	private boolean constructQuickCilverOrderEntry(final QCRedeeptionResponse response,String transactionId){
	
		boolean qcStatus= false;
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
	  modelService.saveAll(walletCardApportionDetailModelList);
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
		walletApportionModel.setType("RETURN");
		if(StringUtils.equalsIgnoreCase(response.getResponseCode().toString(),"0")){
			walletApportionModel.setStatusForQc("SUCCESS");
			qcStatus=true;
		}else{
			walletApportionModel.setStatusForQc("PENDING");
		}
		modelService.save(walletApportionModel);
		abstractOrderEntryModel.setWalletApportionReturnInfo(walletApportionModel);
		modelService.save(abstractOrderEntryModel);
		modelService.refresh(abstractOrderEntryModel);
	 }
		return qcStatus;
	}
	private  QCRedeeptionResponse  qcCallforReturnRefund(OrderModel orderModel ,RefundEntryModel returnEntry){
		LOG.debug("AutoRefundInitiateAction: Going to call QC  mplWalletServices.qcCredit(walletInfo); for Order #"
				+ orderModel.getCode());
		String walletId =null;
		QCRedeeptionResponse  qcRedeeptionResponse= null;
		CustomerModel customerModel= (CustomerModel)orderModel.getUser();
   	
   	try{
   		
   		if(null!=customerModel && null!= customerModel.getCustomerWalletDetail()){
      		walletId=customerModel.getCustomerWalletDetail().getWalletId();
      	}
         	QCCreditRequest qcCreditRequest =new QCCreditRequest();
         	qcCreditRequest.setInvoiceNumber(orderModel.getParentReference().getCode());
         	if(null != returnEntry.getAmountForQc()){
         	qcCreditRequest.setAmount(returnEntry.getAmountForQc().toString());
         	qcCreditRequest.setNotes("Cancel for "+ returnEntry.getAmountForQc().toString()); 
         	}
         	qcRedeeptionResponse= mplWalletServices.qcCredit(walletId, qcCreditRequest);
      		
		
		}catch(Exception e){
			LOG.error("AutoRefundInitiateAction: After call mplWalletServices.qcCredit(walletInfo); for Order #"
					+ orderModel.getCode());	
		}
		return qcRedeeptionResponse;
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

	protected void populateRefundList(final OrderModel orderModel)
	{
		boolean refundedAtRts = false;
		boolean refundedByWallet = false;

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
								if (returnEntry.getOrderEntry() != null
										&& CollectionUtils.isNotEmpty(returnEntry.getOrderEntry().getConsignmentEntries()))
								{
									final ConsignmentStatus status = returnEntry.getOrderEntry().getConsignmentEntries().iterator().next()
											.getConsignment().getStatus();

									refundedAtRts = false;
									if (returnEntry instanceof RefundEntryModel
											&& null != ((RefundEntryModel) returnEntry).getRefundMode()
											&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(REFUND_MODE_C)) // added for store return mode
									{
										refundedAtRts = true;
									}
									refundedByWallet = false;
									if (returnEntry instanceof RefundEntryModel
											&& null != ((RefundEntryModel) returnEntry).getRefundMode()
											&& ((RefundEntryModel) returnEntry).getRefundMode().equalsIgnoreCase(REFUND_MODE_WALLET)) // added for wallet store return mode
									{
										refundedByWallet = true;
									}


									if (status.equals(ConsignmentStatus.RETURN_CLOSED) && !refundedAtRts && !refundedByWallet)
									{
										final ReturnEntryModel returnEntryModel = returnEntry;
										final OrderEntryModel orderEntryModel = (OrderEntryModel) returnEntryModel.getOrderEntry();

										refundList.add(orderEntryModel);
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
}
