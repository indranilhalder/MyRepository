/**
 *
 */
package com.tisl.mpl.facades.task;

import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;

import org.apache.log4j.Logger;

import com.tisl.mpl.model.MPLCancelCRMTaskModel;


/**
 * @author 1047001
 *
 */
public class CancelAsyncTask implements TaskRunner<MPLCancelCRMTaskModel>
{
	protected static final Logger LOG = Logger.getLogger(CancelAsyncTask.class);

	//private Converter<UserModel, CustomerData> customerConverter;

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.task.TaskRunner#handleError(de.hybris.platform.task.TaskService,
	 * de.hybris.platform.task.TaskModel, java.lang.Throwable)
	 */
	@Override
	public void handleError(final TaskService arg0, final MPLCancelCRMTaskModel taskModel, final Throwable arg2)
	{
		//final String orderCode = taskModel.getOrderCode();
		LOG.debug("***********************************Ticket creation Asynchronously failed:");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.task.TaskRunner#run(de.hybris.platform.task.TaskService,
	 * de.hybris.platform.task.TaskModel)
	 */
	@Override
	public void run(final TaskService arg0, final MPLCancelCRMTaskModel taskModel) throws RetryLaterException
	{
		LOG.debug("***********************************Ticket creation Asynchronously failed");
		/*
		 *
		 * OrderData subOrderDetail = null; boolean cancelSuccess = false; OrderEntryData subOrderEntry = null;
		 * CustomerData customerData = null; CustomerModel customerModel = null;
		 *
		 * try { final Integer MAX_RETRIES = Integer.valueOf(configurationService.getConfiguration()
		 * .getString(MarketplaceFacadesConstants.MAX_RETRY).trim());
		 *
		 * final Long TIME_DELAY = Long.valueOf(configurationService.getConfiguration()
		 * .getString(MarketplaceFacadesConstants.TIME_DELAY).trim());
		 *
		 * final String orderCode = taskModel.getOrderCode(); //final String customerId = taskModel.getCustomerId(); final
		 * String reasonCode = taskModel.getReasonCode(); final String refundType = taskModel.getRefundType(); final
		 * String ticketTypeCode = taskModel.getTicketTypeCode(); final String transactionId =
		 * taskModel.getTransactionId(); final String ussid = taskModel.getUssid(); final Boolean cancelOrReturn =
		 * taskModel.getCancelOrReturn(); final Boolean consignmentPresent = taskModel.getConsignmentPresent();
		 * subOrderDetail = (OrderData) taskModel.getContext();
		 *
		 * LOG.debug("Step 1:***********************************Ticket is to be created for sub order Asynchronously:" +
		 * orderCode);
		 *
		 * final OrderModel subOrderModel = orderModelService.getOrder(orderCode); if (subOrderModel.getUser() instanceof
		 * CustomerModel) { customerModel = (CustomerModel) subOrderModel.getUser(); customerData =
		 * getCustomerConverter().convert(customerModel);
		 *
		 *
		 * for (final OrderEntryData orderEntry : subOrderDetail.getEntries()) { if
		 * (transactionId.equalsIgnoreCase(orderEntry.getTransactionId())) { subOrderEntry = orderEntry; } }
		 *
		 * //cancelSuccess = cancelReturnFacade.createTicketInCRM(subOrderDetail, subOrderEntry, ticketTypeCode,
		 * reasonCode, refundType, ussid, customerData, subOrderModel, consignmentPresent.booleanValue(),
		 * cancelOrReturn.booleanValue());
		 * LOG.debug("Step 2:***********************************Ticket creation Sent for sub order Asynchronously:" +
		 * cancelSuccess); if (cancelSuccess) { if (taskModel.getRetry().intValue() <= MAX_RETRIES.intValue()) { final
		 * RetryLaterException ex = new RetryLaterException("cannot perform"); ex.setDelay(TIME_DELAY.longValue()); //
		 * delay for 2 mins 2 * 60 * 1000 LOG.debug("Step 3:***********************************Retry CRM ticket:" +
		 * cancelSuccess); throw ex; } else { throw new IllegalStateException("finally cannot perform after " +
		 * taskModel.getRetry() + " retries"); } } else {
		 * LOG.debug("Step 4:***********************************Cancel Success Status: " + cancelSuccess); // perform... }
		 * } else { LOG.debug("Step 5:***********************************Custoemr not found of teh order: " + orderCode);
		 * // perform... } } catch (final Exception e) { ExceptionUtil.getCustomizedExceptionTrace(e); }
		 */
	}

	/**
	 * @return the customerConverter
	 */
	/*
	 * public Converter<UserModel, CustomerData> getCustomerConverter() { return customerConverter; }
	 *//**
	 * @param customerConverter
	 *           the customerConverter to set
	 */
	/*
	 * public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter) {
	 * this.customerConverter = customerConverter; }
	 */
}
