/**
 *
 */
package com.tisl.lux.core.homepagetypes;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

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

}
