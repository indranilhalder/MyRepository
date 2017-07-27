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
import com.tisl.mpl.wsdto.OrderInfoWsDTO;


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
	OrderEntryData fetchOrderEntryDetails(OrderEntryData orderEntryData, OrderData subOrder) throws EtailNonBusinessExceptions;

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



	/**
	 * Update PickUpDEetails
	 *
	 * @param orderId
	 * @param name
	 * @param mobile
	 * @return String
	 */
	String editPickUpInfo(String orderId, String name, String mobile);

	/**
	 *
	 * Create Ticket CRM For UpdatePickUpDetails
	 */
	public void createCrmTicketUpdatePickDetails(String orderId);

	/**
	 * raise ticket in CRM when updating pickup details from cscockpit
	 *
	 * @param orderId
	 */
	public void createcrmTicketForCockpit(OrderModel mainOrder, String customerId, String source);

	/**
	 * Sorted DeliveryMode
	 *
	 * @return DeliverYMode List Type
	 */
	public List<String> filterDeliveryMode();

	/**
	 * TISPT-175
	 *
	 * @param orderCode
	 * @return OrderModel
	 */
	OrderModel getOrder(String orderCode);

	//TPR-5225 starts here
	/**
	 * This method returns the order model based on mobile number
	 *
	 * @param orderCode
	 * @return OrderModel
	 */
	public List<OrderModel> getOrderWithMobileNo(final String mobileNo, int queryCount);

	public String getL4CategoryNameForProduct(final String productCode);

	public OrderInfoWsDTO storeOrderInfoByMobileNo(List<OrderModel> orderModels, int countLimit);

	//TPR-5225 ends here

	//TPR-4840
	public OrderModel getOrderByParentOrderNo(final String orderRefNo);

	//TPR-4840
	public OrderInfoWsDTO storeOrderInfoByOrderNo(OrderModel orderModel);

	//TPR-4841
	public OrderInfoWsDTO storeOrderInfoByTransactionId(OrderModel orderModel, String transactionId);

	//TPR-4841
	public OrderModel fetchOrderInfoByTransactionId(final String transactionId);


	/**
	 * This method used to get Order Model by orderNumber for anonymous users
	 *
	 * @param orderNumber
	 * @return OrderModel
	 */
	OrderModel getOrderForAnonymousUser(String orderNumber);


}
