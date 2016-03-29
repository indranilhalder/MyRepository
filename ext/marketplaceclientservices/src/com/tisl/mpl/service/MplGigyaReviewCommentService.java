/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.net.Proxy;
import java.util.Date;
import java.util.List;

import com.gigya.socialize.GSObject;
import com.tisl.mpl.wsdto.GigyaProductReviewWsDTO;


/**
 * @author TCS
 *
 */
public interface MplGigyaReviewCommentService
{
	public boolean getReviewsByCategoryProductId(String category, String productId, String customerUID);

	public List<GigyaProductReviewWsDTO> getReviewsByUID(String customerUID);

	public String editComment(final String categoryID, final String streamID, final String commentID, final String commentText,
			final String commentTitle, final String commentMediaurl, final String ratings, String UID);

	public String deleteComment(String categoryID, String streamID, String commentID);

	public String getDate(final Date commentDateObj);

	public boolean checkItemKey(GSObject ratings, String key);

	public boolean checkItemArray(GSObject ratings, String key);

	public Proxy getConfiguredProxy();

	public SearchPageData<OrderModel> getPagedFilteredSubOrderHistory(CustomerModel paramCustomerModel,
			BaseStoreModel paramBaseStoreModel, PageableData paramPageableData);
}
