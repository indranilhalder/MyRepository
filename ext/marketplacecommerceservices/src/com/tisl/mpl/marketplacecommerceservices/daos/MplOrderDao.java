/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.ReturnReasonModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplOrderDao
{

	/**
	 * @return List<StateModel>
	 */
	List<ReturnReasonModel> getReturnReasonForOrderItem();

	/**
	 * @param customerModel
	 * @param store
	 * @param status
	 * @param pageableData
	 * @return SearchPageData
	 */
	abstract SearchPageData<OrderModel> findSubOrdersByCustomerAndStore(CustomerModel customerModel, BaseStoreModel store,
			OrderStatus[] status, PageableData pageableData);

	/**
	 * @param customerModel
	 * @param store
	 * @param status
	 * @param pageableData
	 * @return SearchPageData
	 */
	abstract SearchPageData<OrderModel> findParentOrdersByCustomerAndStore(CustomerModel customerModel, BaseStoreModel store,
			OrderStatus[] status, PageableData pageableData);

	/**
	 * @param consignmentCode
	 * @return ConsignmentModel
	 */
	ConsignmentModel fetchConsignment(String consignmentCode);

	/**
	 * @return List<CancellationReasonModel>
	 */
	List<CancellationReasonModel> fetchCancellationReason();

	/**
	 *
	 * @param cartGUID
	 * @return MplPaymentAuditModel
	 */
	MplPaymentAuditModel getAuditList(String cartGUID);

	/**
	 * @Desc : Returns the order history with duration as filter TISEE-1855
	 * @param paramCustomerModel
	 * @param paramBaseStoreModel
	 * @param paramPageableData
	 * @param fromDate
	 * @return SearchPageData
	 */
	SearchPageData<OrderModel> getPagedFilteredParentOrderHistory(CustomerModel paramCustomerModel,
			BaseStoreModel paramBaseStoreModel, PageableData paramPageableData, Date fromDate);



	/**
	 * @Desc :To fetch order model list for a guid //TISPRD-181
	 * @param cartModel
	 * @return List<OrderModel>
	 * @throws EtailNonBusinessExceptions
	 */
	List<OrderModel> getOrderForGuid(CartModel cartModel) throws EtailNonBusinessExceptions;

}
