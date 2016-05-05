/**
 *
 */
package com.tisl.mpl.review.daos;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 * @author TCS
 *
 */
public interface MplGigyaReviewCommentDao
{
	/**
	 * @Desc : Returns the order history with duration as filter TISEE-1855
	 * @param paramCustomerModel
	 * @param paramBaseStoreModel
	 * @param paramPageableData
	 * @return SearchPageData
	 */
	SearchPageData<OrderModel> getPagedFilteredSubOrderHistory(CustomerModel paramCustomerModel,
			BaseStoreModel paramBaseStoreModel, PageableData paramPageableData);
}
