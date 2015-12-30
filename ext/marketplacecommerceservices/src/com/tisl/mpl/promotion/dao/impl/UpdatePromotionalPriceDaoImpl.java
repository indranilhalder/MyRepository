/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.promotion.dao.UpdatePromotionalPriceDao;



/**
 * @author TCS
 *
 */
public class UpdatePromotionalPriceDaoImpl implements UpdatePromotionalPriceDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.promotion.dao.UpdatePromotionalPriceDao#fetchPricedData(java.util.List)
	 */
	@Override
	public List<PriceRowModel> fetchPricedData(final List<String> product)
	{

		// YTODO Auto-generated method stub
		List<PriceRowModel> priceRow = null;
		//final StringBuilder query = new StringBuilder("SELECT {pri." + PriceRowModel.PK + "} ");
		final StringBuilder query = new StringBuilder(48);
		query.append("SELECT {pri." + PriceRowModel.PK + "} ");
		query.append("FROM {" + PriceRowModel._TYPECODE + " AS pri} ");
		query.append("WHERE {pri." + PriceRowModel.PRODUCT + "} in (?" + PriceRowModel.PRODUCT + ")");

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(PriceRowModel.PRODUCT, product);
		final SearchResult<PriceRowModel> searchRes = flexibleSearchService.search(query.toString(), queryParams);
		if (searchRes != null)
		{
			priceRow = searchRes.getResult();
		}

		return priceRow;
	}




}
