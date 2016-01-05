package com.tisl.mpl.interceptor;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.LoadInterceptor;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.GlobalCodeService;
import com.tisl.mpl.mplcommerceservices.service.data.RefundInfo;
import com.tisl.mpl.service.MplRefundStatusService;
import com.tisl.mpl.util.ExceptionUtil;


public class ManualRefundOrderStatusInterceptor implements LoadInterceptor, PrepareInterceptor
{
	@Autowired
	private MplRefundStatusService mplRefundStatusService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private GlobalCodeService globalCodeService;

	@Override
	public void onLoad(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof ConsignmentModel)
		{
			final ConsignmentModel type = (ConsignmentModel) model;
			final ItemModelContextImpl imp = (ItemModelContextImpl) ModelContextUtils.getItemModelContext((ConsignmentModel) model);
			imp.getValueHistory().loadOriginalValue(type.STATUS, type.getStatus());
		}
	}

	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		try
		{
			if (object instanceof ConsignmentModel)
			{
				final ConsignmentModel consignmentModelObject = (ConsignmentModel) object;

				final ConsignmentStatus consignmentStatusObject = consignmentModelObject.getStatus();
				final String consignmentCodeObject = consignmentModelObject.getCode();

				final ConsignmentModel type = (ConsignmentModel) object;

				final ItemModelContextImpl imp = (ItemModelContextImpl) ModelContextUtils
						.getItemModelContext((ConsignmentModel) object);

				//SONAR Fix
				if (arg1.isModified(type, type.STATUS)
						&& imp.getValueHistory().isValueLoaded(type.STATUS)
						&& imp.getValueHistory().getOriginalValue(type.STATUS) != null
						&& imp.getValueHistory().getOriginalValue(type.STATUS).equals(ConsignmentStatus.REFUND_IN_PROGRESS)
						&& (consignmentStatusObject.equals(ConsignmentStatus.RETURN_COMPLETED)
								|| consignmentStatusObject.equals(ConsignmentStatus.CANCELLED) || consignmentStatusObject
									.equals(ConsignmentStatus.ORDER_CANCELLED)))
				{
					//					if (imp.getValueHistory().isValueLoaded(type.STATUS)
					//							&& imp.getValueHistory().getOriginalValue(type.STATUS) != null
					//							&& imp.getValueHistory().getOriginalValue(type.STATUS).equals(ConsignmentStatus.REFUND_IN_PROGRESS))
					//					{
					//						if (consignmentStatusObject.equals(ConsignmentStatus.RETURN_COMPLETED)
					//								|| consignmentStatusObject.equals(ConsignmentStatus.CANCELLED))
					//						{

					//Refund OMS Call

					final PaymentTransactionModel paymentModel = modelService.create(PaymentTransactionModel.class);

					paymentModel.setTransactionId(consignmentCodeObject);

					final PaymentTransactionModel paymentTransactionModel = flexibleSearchService.getModelByExample(paymentModel);

					final AbstractOrderEntryModel orderEntry = consignmentModelObject.getConsignmentEntries().iterator().next()
							.getOrderEntry();

					if (orderEntry != null)
					{
						final RefundInfo refundInfo = new RefundInfo();

						if (paymentTransactionModel != null)
						{
							refundInfo.setRefundedBankTrxID(paymentTransactionModel.getCode());
							//TISSIT-1768
							//refundInfo.setRefundedBankTrxStatus(paymentTransactionModel.getStatus());
							refundInfo.setRefundedBankTrxStatus(MarketplacecommerceservicesConstants.SUCCESS.toUpperCase());
							refundInfo.setRefundedAmt(paymentTransactionModel.getEntries().iterator().next().getAmount().floatValue());
						}
						else
						{
							refundInfo.setRefundedBankTrxID(StringUtils.EMPTY);
							refundInfo.setRefundedBankTrxStatus(StringUtils.EMPTY);
							refundInfo.setRefundedAmt(NumberUtils.FLOAT_ZERO.floatValue());
						}
						refundInfo.setRefundedBy("CsAgent");//TODO
						refundInfo.setRefundType("Back to Source");//TODO
						refundInfo.setRefundTriggeredDate(new Date());

						final List<RefundInfo> refundInfos = new ArrayList<RefundInfo>(2);
						refundInfos.add(refundInfo);

						final String referenceNumber = ((OrderModel) orderEntry.getOrder()).getParentReference().getCode();
						mplRefundStatusService.refundStatusDatatoWsdto(refundInfos, referenceNumber, orderEntry.getTransactionID(),
								globalCodeService.getGlobalMasterCode(consignmentStatusObject.toString()).getGlobalCode());
						//Create Log History TISEE-5545
						createHistoryLog("RETURN_COMPLETED", ((OrderModel) orderEntry.getOrder()), orderEntry.getTransactionID());
					}

				}
				//					}
				//				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
	}


	private void createHistoryLog(final String description, final OrderModel order, final String lineId)
	{
		final OrderHistoryEntryModel historyEntry = modelService.create(OrderHistoryEntryModel.class);
		historyEntry.setTimestamp(Calendar.getInstance().getTime());
		historyEntry.setOrder(order);
		historyEntry.setLineId(lineId);
		historyEntry.setDescription(description);
		modelService.save(historyEntry);
	}

}