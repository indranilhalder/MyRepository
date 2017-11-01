/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.actions.order;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.service.MplNotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;


/**
 * @author TCS
 *
 */
public class AutoRefundInitiateAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(AutoRefundInitiateAction.class);
	private static final String REFUND_MODE_C = "C";
	private static final String REFUND_MODE_WALLET = "W";

	@Autowired
	private ModelService modelService;

	@Autowired
	private MplPaymentService mplPaymentService;

	@Autowired
	private MplNotificationService mplNotificationSaveService;


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
		LOG.error("Inside AutoRefundInitiateAction for Order #" + orderModel.getCode());
		if (orderModel != null) //Changed for SDI-930
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
											LOG.error("AutoRefundInitiateAction: Going to call mplPaymentService.doRefundPayment(refundList); for Order #"
													+ orderModel.getCode());
											final String result = mplPaymentService.doRefundPayment(refundList);
											LOG.error("AutoRefundInitiateAction: After call mplPaymentService.doRefundPayment(refundList); for Order #"
													+ orderModel.getCode());
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
