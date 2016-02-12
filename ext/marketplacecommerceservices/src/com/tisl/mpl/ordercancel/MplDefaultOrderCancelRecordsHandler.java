/**
 *
 */
package com.tisl.mpl.ordercancel;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;

import java.util.Map;


/**
 * @author TCS
 *
 */
public class MplDefaultOrderCancelRecordsHandler extends DefaultOrderCancelRecordsHandler
{

	/*
	 * @Autowired private OrderCancelDao orderCancelDao;
	 *
	 * @Autowired private OrderHistoryService orderHistoryService;
	 */

	@Override
	protected OrderCancelRecordEntryModel createCancelRecordEntry(final OrderCancelRequest request, final OrderModel order,
			final OrderCancelRecordModel cancelRecord, final OrderHistoryEntryModel snapshot,
			final Map<Integer, AbstractOrderEntryModel> originalOrderEntriesMapping) throws OrderCancelRecordsHandlerException
	{
		final OrderCancelRecordEntryModel result = super.createCancelRecordEntry(request, order, cancelRecord, snapshot,
				originalOrderEntriesMapping);

		if (request instanceof MplOrderCancelRequest)
		{
			result.setRefundableAmount(((MplOrderCancelRequest) request).getAmountToRefund());
		}
		getModelService().save(result);
		return result;
	}

	/*
	 * @Override public OrderCancelRecordEntryModel createRecordEntry(final OrderCancelRequest request, final
	 * PrincipalModel requestor) throws OrderCancelRecordsHandlerException { if (request == null) { throw new
	 * OrderCancelRecordsHandlerException(null, "Cancel request cannot be null"); } if (request.getOrder() == null) {
	 * throw new OrderCancelRecordsHandlerException(null, "Cancel request contains no order reference"); }
	 * 
	 * final OrderModel order = request.getOrder();
	 * 
	 * final OrderModel version = this.orderHistoryService.createHistorySnapshot(order); final Map
	 * originalOrderEntriesMapping = storeOriginalOrderEntriesMapping(version);
	 * 
	 * final String description = ((!(request.isPartialCancel())) ? "Full c" : "Partial c") + "ancel request for order: "
	 * + order.getCode(); final OrderHistoryEntryModel snapshot = createSnaphot(order, version, description, requestor);
	 * try { final OrderCancelRecordModel cancelRecord = getOrCreateCancelRecord(order); if (cancelRecord.isInProgress())
	 * { //LOG.info("Order recored partially cancelled."); throw new IllegalStateException(
	 * "Cannot create new Orde cancel request - the order cancel record indicates: Cancel already in progress"); }
	 * cancelRecord.setInProgress(true); getModelService().save(cancelRecord); return createCancelRecordEntry(request,
	 * order, cancelRecord, snapshot, originalOrderEntriesMapping); } catch (final OrderCancelDaoException e) { throw new
	 * OrderCancelRecordsHandlerException(order.getCode(), e); } }
	 * 
	 * private Map<Integer, AbstractOrderEntryModel> storeOriginalOrderEntriesMapping(final OrderModel order) { final Map
	 * mapping = new HashMap(order.getEntries().size()); for (final AbstractOrderEntryModel currentEntry :
	 * order.getEntries()) { mapping.put(currentEntry.getEntryNumber(), currentEntry); } return mapping; }
	 * 
	 * @Override protected OrderCancelRecordModel getOrCreateCancelRecord(final OrderModel order) throws
	 * OrderCancelDaoException { OrderCancelRecordModel cancelRecord = this.orderCancelDao.getOrderCancelRecord(order);
	 * if (cancelRecord == null) { cancelRecord = createCancelRecord(order); } return cancelRecord; }
	 * 
	 * @Override protected OrderHistoryEntryModel createSnaphot(final OrderModel order, final OrderModel version, final
	 * String description) { return createSnaphot(order, version, description, null); }
	 * 
	 * @Override protected OrderHistoryEntryModel createSnaphot(final OrderModel order, final OrderModel version, final
	 * String description, final PrincipalModel requestor) { this.orderHistoryService.saveHistorySnapshot(version);
	 * 
	 * final OrderHistoryEntryModel historyEntry = (OrderHistoryEntryModel)
	 * getModelService().create(OrderHistoryEntryModel.class); historyEntry.setOrder(order);
	 * historyEntry.setPreviousOrderVersion(version); historyEntry.setDescription(description);
	 * historyEntry.setTimestamp(new Date()); if (requestor instanceof EmployeeModel) {
	 * historyEntry.setEmployee((EmployeeModel) requestor); } getModelService().save(historyEntry); return historyEntry;
	 * }
	 * 
	 * @Override public OrderCancelRecordEntryModel getPendingCancelRecordEntry(final OrderModel order) throws
	 * OrderCancelRecordsHandlerException { final OrderCancelRecordModel orderCancelRecord =
	 * this.orderCancelDao.getOrderCancelRecord(order); if ((orderCancelRecord == null) ||
	 * (!(orderCancelRecord.isInProgress()))) { throw new IllegalStateException("Order[" + order.getCode() +
	 * "]: cancel is not currently in progress"); } final Collection entries =
	 * orderCancelRecord.getModificationRecordEntries(); if ((entries == null) || (entries.isEmpty())) { throw new
	 * IllegalStateException("Order[" + order.getCode() + "]: has no cancel records"); } OrderCancelRecordEntryModel
	 * currentCancelEntry = null; for (final Iterator iter = entries.iterator(); iter.hasNext();) { final
	 * OrderCancelRecordEntryModel entry = (OrderCancelRecordEntryModel) iter.next(); if
	 * (!(entry.getStatus().equals(OrderModificationEntryStatus.INPROGRESS))) { continue; } if (currentCancelEntry ==
	 * null) { currentCancelEntry = entry; } else { throw new IllegalStateException(
	 * "Cannot create new Orde cancel request - the order cancel record indicates: Cancel already in progress");
	 * //LOG.info("More than 1 entries to be cancelled"); } }
	 * 
	 * return currentCancelEntry; }
	 * 
	 * @Override public OrderCancelRecordModel getCancelRecord(final OrderModel order) { return
	 * this.orderCancelDao.getOrderCancelRecord(order); }
	 * 
	 * 
	 * @Override public OrderCancelRecordEntryModel updateRecordEntry(final OrderCancelResponse response) throws
	 * OrderCancelRecordsHandlerException { if (response == null) { throw new
	 * IllegalArgumentException("Cancel response cannot be null"); } if (response.getOrder() == null) { throw new
	 * IllegalArgumentException("Cancel response contains no order reference"); } final OrderModel order =
	 * response.getOrder();
	 * 
	 * final OrderCancelRecordEntryModel currentEntry = getPendingCancelRecordEntry(order); switch
	 * (response.getResponseStatus().ordinal()) { case 2: currentEntry.setCancelResult(OrderCancelEntryStatus.FULL);
	 * currentEntry.setStatus(OrderModificationEntryStatus.SUCCESSFULL); break; case 3:
	 * currentEntry.setCancelResult(OrderCancelEntryStatus.PARTIAL);
	 * currentEntry.setStatus(OrderModificationEntryStatus.SUCCESSFULL); break; case 1:
	 * currentEntry.setCancelResult(OrderCancelEntryStatus.DENIED); currentEntry.setRefusedMessage(response.getNotes());
	 * currentEntry.setStatus(OrderModificationEntryStatus.SUCCESSFULL); break; case 4:
	 * currentEntry.setStatus(OrderModificationEntryStatus.FAILED); currentEntry.setFailedMessage(response.getNotes()); }
	 * 
	 * for (final OrderCancelEntry responseEntry : response.getEntriesToCancel()) { final
	 * OrderEntryCancelRecordEntryModel orderEntryRecord = this.orderCancelDao.getOrderEntryCancelRecord(
	 * (OrderEntryModel) responseEntry.getOrderEntry(), currentEntry);
	 * orderEntryRecord.setCancelledQuantity(Integer.valueOf((int) responseEntry.getCancelQuantity()));
	 * 
	 * getModelService().save(orderEntryRecord); } getModelService().save(currentEntry);
	 * 
	 * final OrderModificationRecordModel record = currentEntry.getModificationRecord(); record.setInProgress(false);
	 * getModelService().save(record); return currentEntry; }
	 */
}
