/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplStockDao;


/**
 * @author TCS
 *
 */

public class MplStockDaoImpl implements MplStockDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;


	/*
	 * @Javadoc Method to retrieve list of StockLevelModel based on articleSKUID
	 *
	 * @param articleSKUID
	 *
	 * @return listOfStock
	 */
	@Override
	public List<StockLevelModel> getStockDetail(final String articleSKUID)
	{
		try
		{
			final String queryString = //
			"SELECT {c:" + StockLevelModel.PK + "}" //
					+ "FROM {" + StockLevelModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + StockLevelModel.SELLERARTICLESKU + "}=?articleSKUID";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("articleSKUID", articleSKUID);

			final List<StockLevelModel> listOfStock = flexibleSearchService.<StockLevelModel> search(query).getResult();
			return listOfStock;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/*
	 * @Javadoc Method to retrieve list of StockLevelModel based on articleSKUID
	 *
	 * @param articleSKUID
	 *
	 * @return listOfStock
	 */

	@Override
	public List<StockLevelModel> getAllStockDetail(final String articleSKUIDList)
	{
		try
		{
			final String queryString = //
			"SELECT {c:" + StockLevelModel.PK + "}" //
					+ "FROM {" + StockLevelModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + StockLevelModel.SELLERARTICLESKU + "} in (" + articleSKUIDList + ") ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			final List<StockLevelModel> listOfStock = flexibleSearchService.<StockLevelModel> search(query).getResult();
			return listOfStock;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}

	}

}
