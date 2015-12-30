/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplMyFavBrandCategoryDao;


/**
 * @author TCS
 *
 */
public class DefaultMplMyFavBrandCategoryDao implements MplMyFavBrandCategoryDao
{
	private static final Logger LOG = Logger.getLogger(DefaultMplMyFavBrandCategoryDao.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<MyRecommendationsConfigurationModel> fetchRecommendedData()
	{
		LOG.debug("Fetching My Recommendation Data");
		final String queryString = //
		"SELECT {p:" + MyRecommendationsConfigurationModel.PK + "} "//
				+ "FROM {" + MyRecommendationsConfigurationModel._TYPECODE + " AS p }";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<MyRecommendationsConfigurationModel> search(query).getResult();
	}


	@Override
	public List<MyRecommendationsConfigurationModel> fetchBrands(final String catCode)
	{
		LOG.debug("Fetching My fetchBrands Data");
		final String queryString = //
		"SELECT {p:" + MyRecommendationsConfigurationModel.PK + "} " + "FROM {" + MyRecommendationsConfigurationModel._TYPECODE
				+ " AS p }" + " WHERE " + "{p:" + MyRecommendationsConfigurationModel.CONFIGUREDCATEGORY + "}=?catCode";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("catCode", catCode);
		return flexibleSearchService.<MyRecommendationsConfigurationModel> search(query).getResult();
	}
	//
}
