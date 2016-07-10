/**
 *
 */
package com.tisl.mpl.review.daos.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.review.daos.MplGigyaReviewCommentDao;


/**
 * @author TCS
 *
 */
public class MplGigyaReviewCommentDaoImpl implements MplGigyaReviewCommentDao
{

	private final static Logger LOG = Logger.getLogger(MplGigyaReviewCommentDaoImpl.class.getName());
	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;

	/**
	 * @Desc : Returns the order history with duration as filter based on Sub-order
	 * @param customerModel
	 * @param store
	 * @param paramPageableData
	 * @return SearchPageData
	 */
	@Override
	public SearchPageData<OrderModel> getPagedFilteredSubOrderHistory(final CustomerModel customerModel,
			final BaseStoreModel store, final PageableData paramPageableData)
	{
		try
		{
			final Map queryParams = new HashMap();
			queryParams.put("customer", customerModel);
			queryParams.put("store", store);
			queryParams.put("type", "SubOrder");


			final List sortQueries = Arrays
					.asList(new SortQueryData[]
					{ createSortQueryData(MarketplacecclientservicesConstants.BY_DATE,
							"SELECT {pk}, {creationtime}, {code} FROM {Order} WHERE {user} = ?customer "
									+ "AND {versionID} IS NULL AND {store} = ?store AND {type} = ?type  "
									+ "ORDER BY {creationtime} DESC, {pk}"), });


			return pagedFlexibleSearchService.search(sortQueries, MarketplacecclientservicesConstants.BY_DATE, queryParams,
					paramPageableData);

		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return null;
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}
}
