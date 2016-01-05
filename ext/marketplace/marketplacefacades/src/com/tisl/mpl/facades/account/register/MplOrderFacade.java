/**
 *
 */
package com.tisl.mpl.facades.account.register;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.facades.product.data.ReturnReasonDetails;


/**
 * @author TCS
 *
 */
public interface MplOrderFacade
{
	List<ReturnReasonData> getReturnReasonForOrderItem();

	/**
	 * Returns the order history of the current user for given statuses.
	 *
	 * @param pageableData
	 *           paging information
	 * @param statuses
	 *           array of order statuses to filter the results
	 * @return The order history of the current user.
	 */
	SearchPageData<OrderHistoryData> getPagedSubOrderHistoryForStatuses(PageableData pageableData, OrderStatus... statuses);

	/**
	 * @param pageableData
	 * @return
	 */
	SearchPageData<OrderHistoryData> getPagedParentOrderHistoryForStatuses(PageableData pageableData, OrderStatus... statuses);

	/**
	 * @return List<CancellationReasonModel>
	 */
	List<CancellationReasonModel> getCancellationReason();


	/**
	 *
	 * @param returnCancelFlag
	 * @return
	 */
	ReturnReasonDetails getReturnReasonForOrderItem(String returnCancelFlag);

	SearchPageData<OrderData> getPagedParentOrderHistory(final PageableData pageableData, CustomerModel customer,
			final OrderStatus... statuses);

	/**
	 * @param code
	 * @return ProductModel
	 */
	ProductModel getProductForCode(String code);

	/*
	 * @Desc : Used to fetch IMEI details for Account Page order history
	 *
	 * @param : orderModelList
	 *
	 * @return Map<String, Map<String, String>>
	 *
	 * @ throws EtailNonBusinessExceptions
	 */
	Map<String, Map<String, String>> fetchOrderSerialNoDetails(final List<OrderModel> orderModelList)
			throws EtailNonBusinessExceptions;

	/*
	 * @Desc : Used to fetch Invoice details for Account Page order history
	 *
	 * @param : orderModelList
	 *
	 * @return Map<String, Boolean>
	 *
	 * @ throws EtailNonBusinessExceptions
	 */
	Map<String, Boolean> fetchOrderInvoiceDetails(final List<OrderModel> orderModelList) throws EtailNonBusinessExceptions;

	/*
	 * @Desc : Used to fetch and populate details for Account Page order history
	 *
	 * @param : orderEntryData
	 *
	 * @return OrderEntryData
	 *
	 * @ throws EtailNonBusinessExceptions
	 */
	OrderEntryData fetchOrderEntryDetails(OrderEntryData orderEntryData, Map<String, Boolean> sortInvoice, OrderData subOrder)
			throws EtailNonBusinessExceptions;

	boolean checkCancelStatus(final String currentStatus, final String status);


	/**
	 * Returns the order history with duration as filter TISEE-1855
	 *
	 * @param pageableData
	 *           paging information
	 * @return The order history of the current user.
	 */
	SearchPageData<OrderHistoryData> getPagedFilteredParentOrderHistory(PageableData pageableData);

	/**
	 * TISEE-6419
	 * 
	 * @param orderData
	 * @param transactionId
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	boolean isChildCancelleable(final OrderData orderData, final String transactionId) throws EtailNonBusinessExceptions;

}
