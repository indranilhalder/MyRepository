/**
 *
 */
package com.tils.luxury.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import com.tils.luxury.dao.HomePageTypesDao;
import com.tisl.lux.model.HomePageTypesModel;


/**
 * @author abhishek.singh
 *
 */
public class HomePageTypesDaoImpl implements HomePageTypesDao
{
	@Resource
	FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.lux.core.homepagetypes.HomePageTypesDao#getHomePageType()
	 */
	@Override
	public List<HomePageTypesModel> getHomePageType()
	{

		final String queryString = "Select {pk} from {HomePageTypes}";
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(queryString);

		return flexibleSearchService.<HomePageTypesModel> search(flexibleSearchQuery).getResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.lux.core.homepagetypes.HomePageTypesDao#getHomePageTypeCode()
	 */
	@Override
	public HomePageTypesModel getHomePageTypeCode(final String code)
	{
		// YTODO Auto-generated method stub
		final String queryString = "Select {pk} from {HomePageTypes} where {code}=?code ";
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(queryString);
		flexibleSearchQuery.addQueryParameter("code", code);
		final List<HomePageTypesModel> homepageTypes = flexibleSearchService.<HomePageTypesModel> search(flexibleSearchQuery)
				.getResult();

		return homepageTypes.isEmpty() ? null : homepageTypes.get(0);
	}

}
