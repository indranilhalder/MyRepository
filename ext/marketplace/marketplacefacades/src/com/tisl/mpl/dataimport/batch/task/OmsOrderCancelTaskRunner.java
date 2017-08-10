/**
 *
 */
package com.tisl.mpl.dataimport.batch.task;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.BulkCancellationProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.pojo.OmsOrderCancelTaskContext;


/**
 * @author TCS
 *
 */
public class OmsOrderCancelTaskRunner implements TaskRunner<TaskModel>
{

	private static final Logger LOG = Logger.getLogger(OmsOrderCancelTaskRunner.class);
	@Resource(name = "cancelReturnFacade")
	private CancelReturnFacade cancelReturnFacade;
	@Resource(name = "mplOrderFacade")
	private MplOrderFacade mplOrderFacade;
	@Resource(name = "orderModelService")
	OrderModelService orderModelService;
	@Resource(name = "modelService")
	ModelService modelService;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.task.TaskRunner#run(de.hybris.platform.task.TaskService,
	 * de.hybris.platform.task.TaskModel)
	 */
	@Override
	public void run(final TaskService taskService, final TaskModel task) throws RetryLaterException
	{
		// YTODO Auto-generated method stub
		try
		{
			final OmsOrderCancelTaskContext cancelTaskCOntext = (OmsOrderCancelTaskContext) task.getContext();
			final OrderEntryData orderEntryData = cancelTaskCOntext.getOrderEntryData();
			final OrderData childOrderData = cancelTaskCOntext.getOrderData();

			boolean docancel = false;
			boolean cancellationStatus = false;
			String orderConsignmentStatus = null;
			if (null != orderEntryData.getConsignment() && null != orderEntryData.getConsignment().getStatus())
			{
				orderConsignmentStatus = orderEntryData.getConsignment().getStatus().getCode();
				docancel = mplOrderFacade.checkCancelStatus(orderConsignmentStatus,
						MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS);
			}
			if (docancel)
			{
				cancellationStatus = cancelReturnFacade.implementCancelOrReturn(childOrderData, orderEntryData,
						MarketplacecommerceservicesConstants.REASONCODE, orderEntryData.getSelectedUssid(),
						MarketplacecommerceservicesConstants.TICKETTYPECODE_CANCEL, childOrderData.getCustomerData(),
						MarketplacecommerceservicesConstants.REFUNDTYPE, false, SalesApplication.WEB);

			}
			final BulkCancellationProcessModel bulkCancelModel = orderModelService.getBulkCancelData(orderEntryData
					.getTransactionId());
			String message = MarketplacecommerceservicesConstants.EMPTY;
			message = MarketplacecommerceservicesConstants.BLANK_SPACE + MarketplacecommerceservicesConstants.LEFT_PARENTHESIS
					+ MarketplacecommerceservicesConstants.LAST_CONSIGNMENT_STATUS + orderConsignmentStatus
					+ MarketplacecommerceservicesConstants.RIGHT_PARENTHESIS;
			if (cancellationStatus)
			{
				bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.SUCCESS_LOAD_STATUS);
				bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_SUCCESS_DESC + message);

			}
			else
			{
				bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.FAILURE_LOAD_STATUS);
				bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_FAILURE_DESC + message);
			}
			modelService.save(bulkCancelModel);
		}
		catch (final Exception exception)
		{
			LOG.error("ModelSave Exception While cancel Job" + exception.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.task.TaskRunner#handleError(de.hybris.platform.task.TaskService,
	 * de.hybris.platform.task.TaskModel, java.lang.Throwable)
	 */
	@Override
	public void handleError(final TaskService paramTaskService, final TaskModel paramT, final Throwable paramThrowable)
	{
		// YTODO Auto-generated method stub

	}

}