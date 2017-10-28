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
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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


	@Override
	public void executeAction(final OrderProcessModel process)
	{
		LOG.error("Inside AutoRefundInitiateAction");
		boolean refundedAtRts = false;
		boolean refundReasonSiteError = false;
		boolean refundedByWallet = false;

		//Changed for SDI-930
		final List<OrderEntryModel> refundList = Collections.synchronizedList(new ArrayList<OrderEntryModel>());
		final List<ReturnEntryModel> returnList = Collections.synchronizedList(new ArrayList<ReturnEntryModel>());

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
								if (returnEntry.getOrderEntry() != null
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
										populateRefundList(orderModel, refundList, returnList); //Changed for SDI-930

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
													modelService.refresh(historyEntry); //Added for SDI-930
													LOG.error("AutoRefundInitiateAction: historyEntry is set to REFUND_INITIATED for Order #"
															+ orderModel.getCode());
												}
											}

											//Start code for Split mode 
											String result = null;
											boolean qcstatus = false;
											if (null != orderModel.getSplitModeInfo()
													&& orderModel.getSplitModeInfo().equalsIgnoreCase("Split"))
											{
												//Start Added the code for QC
												try
												{
													LOG.debug("Step :1 getting the consignment for ORDER ...");
													if (CollectionUtils.isNotEmpty(returnEntry.getOrderEntry().getConsignmentEntries()))
													{
														LOG.debug("Step :2 getting the consignment status for ..."
																+ returnEntry.getOrderEntry().getConsignmentEntries());

														LOG.debug("Step :8 this is NON - RETURNINITIATED_BY_RTO order ");
														final QCRedeeptionResponse response = qcCallforReturnRefund(orderModel,
																(RefundEntryModel) returnEntry);
														qcstatus = constructQuickCilverOrderEntry(response,
																returnEntry.getOrderEntry().getTransactionID());
														LOG.debug("Step :9 successfully create constructQuickCilverOrderEntry for this order");
													}
													//End Added the code for QC
												}
												catch (final Exception e)
												{
													e.getMessage();
													LOG.error("AutoRefundInitiateAction: Exception is getting while QC responce for Order #"
															+ orderModel.getCode());
												}
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
													final QCRedeeptionResponse response = qcCallforReturnRefund(orderModel,
															(RefundEntryModel) returnEntry);
													qcstatus = constructQuickCilverOrderEntry(response,
															returnEntry.getOrderEntry().getTransactionID());
													//End Added the code for QC
												}
												catch (final Exception e)
												{
													//End Added the code for QC
													e.getMessage();
													LOG.error("AutoRefundInitiateAction: Exception is getting while QC responce for Order #"
															+ orderModel.getCode());
												}
											}
											else
											{
												LOG.error(
														"AutoRefundInitiateAction: Going to call mplPaymentService.doRefundPayment(refundList); for Order #"
																+ orderModel.getCode());
												result = mplPaymentService.doRefundPayment(refundList, null);
												LOG.error(
														"AutoRefundInitiateAction: After call mplPaymentService.doRefundPayment(refundList); for Order #"
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

														LOG.error(
																"AutoRefundInitiateAction: returnEntryTmp.setStatus(ReturnStatus.RETURN_COMPLETED) for Order #"
																		+ orderModel.getCode());

														modelService.save(returnEntryTmp);
														//TODO :Return Initiated Notification
														try
														{
															//send Notification
															final int noOfItems = returnList.size();
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

	private boolean createRefund(final OrderModel subOrderModel, final AbstractOrderEntryModel abstractOrderEntryModel,
			final String reasonCode, final SalesApplication salesApplication)
	{

		boolean returnReqCreated = false;
		final List<RefundEntryModel> refundList = new ArrayList<>();
		try
		{
			final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(subOrderModel);
			returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
			returnRequestModel.setTypeofreturn(TypeofReturn.REVERSE_PICKUP);
			returnRequestModel.setReturnRaisedFrom(salesApplication);
			//End

			if (null != abstractOrderEntryModel)
			{
				final RefundEntryModel refundEntryModel = modelService.create(RefundEntryModel.class);
				refundEntryModel.setOrderEntry(abstractOrderEntryModel);
				refundEntryModel.setReturnRequest(returnRequestModel);
				refundEntryModel.setReason(RefundReason.valueOf(getReasonDesc(reasonCode)));
				refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
				refundEntryModel.setAction(ReturnAction.IMMEDIATE);
				refundEntryModel.setNotes(getReasonDesc(reasonCode));
				refundEntryModel.setExpectedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setReceivedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setRefundedDate(new Date());
				final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
				if (CollectionUtils.isNotEmpty(tranactions))
				{
					final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator()
							.next();

					if (paymentTransEntry.getPaymentMode() != null && paymentTransEntry.getPaymentMode().getMode() != null
							&& COD.equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
					{
						refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
					}
					else
					{
						if (null != subOrderModel.getSplitModeInfo() && subOrderModel.getSplitModeInfo().equalsIgnoreCase("Split"))
						{
							double refundAmountForQc = 0.0D;
							double refundAmountForJuspay = 0.0D;
							//call for Juspay
							refundAmountForJuspay = calculateSplitJuspayRefundAmount(abstractOrderEntryModel);
							//call for QuckCilver
							refundAmountForQc = calculateSplitQcRefundAmount(abstractOrderEntryModel);
							refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(refundAmountForJuspay)));
							refundEntryModel.setAmountForQc(NumberUtils.createDouble(Double.toString(refundAmountForQc)));
						}
						else if (null != subOrderModel.getSplitModeInfo()
								&& subOrderModel.getSplitModeInfo().equalsIgnoreCase("CliqCash"))
						{
							double refundAmountForQc = 0.0D;
							//call for QuckCilver
							refundAmountForQc = calculateSplitQcRefundAmount(abstractOrderEntryModel);
							refundEntryModel.setAmountForQc(NumberUtils.createDouble(Double.toString(refundAmountForQc)));
							refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
						}
						else
						{

							final double amount = (abstractOrderEntryModel.getNetAmountAfterAllDisc() != null
									? abstractOrderEntryModel.getNetAmountAfterAllDisc().doubleValue() : 0D)
									+ (abstractOrderEntryModel.getCurrDelCharge() != null
											? abstractOrderEntryModel.getCurrDelCharge().doubleValue() : 0D)
									+ (abstractOrderEntryModel.getScheduledDeliveryCharge() != null
											? abstractOrderEntryModel.getScheduledDeliveryCharge().doubleValue() : 0D);

							refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(amount)));
							refundEntryModel.setAmountForQc(NumberUtils.createDouble("0"));

						}
					}
				}
				refundList.add(refundEntryModel);
			}
			modelService.saveAll(refundList);
			modelService.save(returnRequestModel);
			returnReqCreated = true;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return returnReqCreated;
	}

	private boolean constructQuickCilverOrderEntry(final QCRedeeptionResponse response, final String transactionId)
	{

		boolean qcStatus = false;
		final AbstractOrderEntryModel abstractOrderEntryModel = mplOrderService.getEntryModel(transactionId);
		final WalletApportionReturnInfoModel walletApportionModel = getModelService().create(WalletApportionReturnInfoModel.class);
		final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();
		if (null != response && null != response.getCards())
		{
			for (final QCCard qcCard : response.getCards())
			{
				final WalletCardApportionDetailModel model = getModelService().create(WalletCardApportionDetailModel.class);
				model.setCardNumber(qcCard.getCardNumber());
				model.setCardExpiry(qcCard.getExpiry());
				model.setCardAmount(qcCard.getAmount().toString());
				model.setBucketType(qcCard.getBucketType());
				walletCardApportionDetailModelList.add(model);
			}
			modelService.saveAll(walletCardApportionDetailModelList);
			walletApportionModel.setWalletCardList(walletCardApportionDetailModelList);
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue())
			{
				walletApportionModel
						.setQcApportionPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue())
			{
				walletApportionModel
						.setQcDeliveryPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue())
			{
				walletApportionModel
						.setQcSchedulingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue())
			{
				walletApportionModel
						.setQcShippingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue());
			}
			walletApportionModel.setType("RETURN");
			if (StringUtils.equalsIgnoreCase(response.getResponseCode().toString(), "0"))
			{
				walletApportionModel.setStatusForQc("SUCCESS");
				qcStatus = true;
			}
			else
			{
				walletApportionModel.setStatusForQc("PENDING");
			}
			modelService.save(walletApportionModel);
			abstractOrderEntryModel.setWalletApportionReturnInfo(walletApportionModel);
			modelService.save(abstractOrderEntryModel);
			modelService.refresh(abstractOrderEntryModel);
		}
		return qcStatus;
	}

	private QCRedeeptionResponse qcCallforReturnRefund(final OrderModel orderModel, final RefundEntryModel returnEntry)
	{
		LOG.debug("AutoRefundInitiateAction: Going to call QC  mplWalletServices.qcCredit(walletInfo); for Order #"
				+ orderModel.getCode());
		String walletId = null;
		QCRedeeptionResponse qcRedeeptionResponse = null;
		final CustomerModel customerModel = (CustomerModel) orderModel.getUser();

		try
		{

			if (null != customerModel && null != customerModel.getCustomerWalletDetail())
			{
				walletId = customerModel.getCustomerWalletDetail().getWalletId();
			}
			final QCCreditRequest qcCreditRequest = new QCCreditRequest();
			qcCreditRequest.setInvoiceNumber(orderModel.getParentReference().getCode());
			qcCreditRequest.setAmount(returnEntry.getAmountForQc().toString());
			qcCreditRequest.setNotes("Cancel for " + returnEntry.getAmountForQc().toString());
			qcRedeeptionResponse = mplWalletServices.qcCredit(walletId, qcCreditRequest);


		}
		catch (final Exception e)
		{
			LOG.error(
					"AutoRefundInitiateAction: After call mplWalletServices.qcCredit(walletInfo); for Order #" + orderModel.getCode());
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

	protected void populateRefundList(final OrderModel orderModel, final List refundList, final List returnList) //Changed for SDI-930
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
}
