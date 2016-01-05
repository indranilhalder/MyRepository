/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.facades.data.AWBResponseData;
import com.tisl.mpl.facades.product.data.ReturnReasonData;


/**
 * @author TCS
 *
 */
public interface MplOrderService
{
	/**
	 * @return List
	 */
	List<ReturnReasonData> getReturnReasonForOrderItem();

	/**
	 *
	 * @param paramCustomerModel
	 * @param paramBaseStoreModel
	 * @param paramArrayOfOrderStatus
	 * @param paramPageableData
	 * @return SearchPageData
	 */
	abstract SearchPageData<OrderModel> getSubOrderList(CustomerModel paramCustomerModel, BaseStoreModel paramBaseStoreModel,
			OrderStatus[] paramArrayOfOrderStatus, PageableData paramPageableData);

	/**
	 *
	 * @param paramCustomerModel
	 * @param paramBaseStoreModel
	 * @param paramArrayOfOrderStatus
	 * @param paramPageableData
	 * @return SearchPageData
	 */
	abstract SearchPageData<OrderModel> getParentOrderList(CustomerModel paramCustomerModel, BaseStoreModel paramBaseStoreModel,
			OrderStatus[] paramArrayOfOrderStatus, PageableData paramPageableData);

	/**
	 * @param consignmentCode
	 * @return ConsignmentModel
	 */
	ConsignmentModel fetchConsignment(String consignmentCode);

	/**
	 * @return List<CancellationReasonModel>
	 */
	List<CancellationReasonModel> getCancellationReason();

	/**
	 * @param awbNumber
	 * @param tplcode
	 * @return AWBResponseData
	 */
	AWBResponseData prepAwbStatus(String awbNumber, String tplcode);

	/**
	 * @param productCode
	 * @return
	 */
	ProductModel findProductsByCode(String productCode);

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

}
