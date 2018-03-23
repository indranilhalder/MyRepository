/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.product.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.dao.impl.DefaultCustomerReviewDao;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.product.MplCustomerReviewDao;


/**
 * @author TCS
 *
 */
public class MplCustomerReviewDaoImpl extends DefaultCustomerReviewDao implements MplCustomerReviewDao
{
	@Autowired
	private PagedFlexibleSearchService pagedFlexibleSearchService;

	@Resource
	private ConfigurationService configurationService;

	@Override
	public SearchPageData<CustomerReviewModel> getReviewsForProductAndLanguage(final ProductModel product,
			final LanguageModel language, final PageableData pageableData, final String orderBy)
	{
		String orderByValue = "DESC";
		if (StringUtils.isNotEmpty(orderBy) && (orderBy.equalsIgnoreCase("DESC") || orderBy.equalsIgnoreCase("ASC")))
		{
			orderByValue = orderBy.toUpperCase();
		}
		//		final String query = "SELECT {" + Item.PK + "} FROM {" + "CustomerReview" + "} WHERE {" + "product" + "}=?product AND {"
		//				+ "language" + "}=?language ORDER BY {" + "creationtime" + "} "+orderByValue;
		final List sortQueries = Arrays.asList(new SortQueryData[]
		{
				createSortQueryData(MarketplacecommerceservicesConstants.BY_DATE, "SELECT {" + Item.PK + "} FROM {"
						+ "CustomerReview" + "} WHERE {" + "product" + "}=?product AND {" + "language" + "}=?language ORDER BY {"
						+ "creationtime" + "} " + orderByValue),
				createSortQueryData(MarketplacecommerceservicesConstants.BY_RATING, "SELECT {" + Item.PK + "} FROM {"
						+ "CustomerReview" + "} WHERE {" + "product" + "}=?product AND {" + "language" + "}=?language ORDER BY {"
						+ "rating" + "} " + orderByValue) });
		final Map queryParams = new HashMap();
		queryParams.put("product", product);
		queryParams.put("language", language);
		//		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
		//		fsQuery.addQueryParameter("product", product);
		//		fsQuery.addQueryParameter("language", language);
		//		fsQuery.setResultClassList(Collections.singletonList(CustomerReviewModel.class));

		//		final SearchResult searchResult = getFlexibleSearchService().search(fsQuery);

		return pagedFlexibleSearchService.search(sortQueries, MarketplacecommerceservicesConstants.BY_DATE, queryParams,
				pageableData);
	}

	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}

	@Override
	public List<List<Object>> getGroupByRatingsForProd(final ProductModel product, final LanguageModel language)
	{
		List<List<Object>> output = null;
		//		final String query = "SELECT {" + CustomerReviewModel.RATING + "},COUNT({" + CustomerReviewModel.RATING + "}),( COUNT({"
		//				+ CustomerReviewModel.RATING + "})*100/(SELECT COUNT(1) FROM {" + "CustomerReview" + "} WHERE {" + "product"
		//				+ "}=?product AND {" + "language" + "}=?language)) FROM {" + "CustomerReview" + "} WHERE {" + "product"
		//				+ "}=?product AND {" + "language" + "}=?language group by {" + CustomerReviewModel.RATING + "} order by {"
		//				+ CustomerReviewModel.RATING + "} ASC";
		final String query = "SELECT {CR2:RATING},COUNT({CR2:RATING}),(COUNT({CR2:RATING})*100/({{SELECT COUNT({CR1:RATING}) as CNT FROM {CustomerReview AS CR1} WHERE {CR1:product}=?product AND {CR1:language}=?language}})) FROM {CustomerReview AS CR2} WHERE {CR2:product}=?product AND {CR2:language}=?language group by {CR2:RATING} order by {CR2:RATING} ASC";
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
		fsQuery.addQueryParameter("product", product);
		fsQuery.addQueryParameter("language", language);
		fsQuery.setResultClassList(Arrays.asList(Double.class, Integer.class, Float.class));

		final SearchResult searchResult = getFlexibleSearchService().search(fsQuery);
		//IQA code Review fix
		if (null != searchResult && CollectionUtils.isNotEmpty(searchResult.getResult()))
		{
			output = searchResult.getResult();
		}
		return output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.product.MplCustomerReviewDao#reviewApplicableForGivenCustomer(de
	 * .hybris.platform.core.model.user.UserModel, de.hybris.platform.core.model.product.ProductModel)
	 */
	@Override
	public boolean reviewApplicableForGivenCustomer(final UserModel user, final ProductModel product)
	{
		boolean reviewApplicable = true;
		final StringBuilder query = new StringBuilder(300);
		query.append(
				"select count(*) from {CustomerReview as c join CustomerReviewApprovalType as cap on {cap.pk}={c.approvalstatus} } where {c.product}=?product and {c.user}=?user and {c.blocked}='0' and {cap.code} in (")
				.append(
						configurationService.getConfiguration().getString("review.restriction.approvalStatus", "'pending','approved'"))
				.append(")");
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
		fsQuery.addQueryParameter("product", product);
		fsQuery.addQueryParameter("user", user);
		fsQuery.setResultClassList(Arrays.asList(Integer.class));
		final List<Integer> instockResultList = getFlexibleSearchService().<Integer> search(fsQuery).getResult();
		if (CollectionUtils.isNotEmpty(instockResultList))
		{
			if (instockResultList.get(0).intValue() > 0)
			{
				reviewApplicable = false;
			}
		}
		return reviewApplicable;
	}
}