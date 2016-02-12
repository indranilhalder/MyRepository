/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.HelpMeShopOccasionModel;
import com.tisl.mpl.marketplacecommerceservices.daos.HelpMeShopDao;


/**
 * @author TCS
 *
 */
public class DefaultHelpMeShopDaoImpl implements HelpMeShopDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.HelpMeShopDao#getAllOccasions()
	 */
	@Override
	public List<HelpMeShopOccasionModel> getAllOccasions()
	{
		//final StringBuilder query = new StringBuilder(); SONAR Fixes
		final StringBuilder query = new StringBuilder(20);
		query.append("Select {").append(HelpMeShopOccasionModel.PK).append("} From {").append(HelpMeShopOccasionModel._TYPECODE)
				.append('}');
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());

		return flexibleSearchService.<HelpMeShopOccasionModel> search(searchQuery).getResult();
	}

}
