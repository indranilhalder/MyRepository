/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.stock.impl.DefaultStockLevelDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.ExtStockLevelDao;


/**
 * @author TCS
 *
 */
public class DefaultExtStockLevelDao extends DefaultStockLevelDao implements ExtStockLevelDao
{
	private static final Logger LOG = Logger.getLogger(DefaultExtStockLevelDao.class);

	@Override
	public StockLevelModel findStockLevel(final WarehouseModel warehouse, final String sellerArticleSKU)
	{
		checkWarehouse(warehouse);
		checkUssid(sellerArticleSKU);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"SELECT {pk} FROM {StockLevel} WHERE {sellerArticleSKU} = ?sellerArticleSKU AND {warehouse} = ?warehouse");
		fQuery.addQueryParameter(MarketplacecommerceservicesConstants.SELLERARTICLESKU, sellerArticleSKU);
		fQuery.addQueryParameter("warehouse", warehouse);
		final SearchResult result = getFlexibleSearchService().search(fQuery);
		final List stockLevels = result.getResult();

		if (stockLevels.isEmpty())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("no stock level for ussid [" + sellerArticleSKU + "] in warehouse [" + warehouse.getName() + "] found.");
			}
			return null;
		}
		if (stockLevels.size() == 1)
		{
			return ((StockLevelModel) stockLevels.get(0));
		}

		LOG.error("more than one stock level with ussid [" + sellerArticleSKU + "] and warehouse [" + warehouse.getName()
				+ "] found, and the first one is returned.");
		return ((StockLevelModel) stockLevels.get(0));
	}

	@Override
	public Collection<StockLevelModel> findAllStockLevels(final String sellerArticleSKU)
	{
		checkUssid(sellerArticleSKU);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(
				"SELECT {pk} FROM {StockLevel} WHERE {sellerArticleSKU} = ?sellerArticleSKU");
		fQuery.addQueryParameter(MarketplacecommerceservicesConstants.SELLERARTICLESKU, sellerArticleSKU);
		final SearchResult result = getFlexibleSearchService().search(fQuery);
		return result.getResult();
	}

	@Override
	public Collection<StockLevelModel> findStockLevels(final String sellerArticleSKU, final Collection<WarehouseModel> warehouses)
	{
		return findStockLevelsImpl(sellerArticleSKU, warehouses, null);
	}

	@Override
	public Collection<StockLevelModel> findStockLevels(final String sellerArticleSKU, final Collection<WarehouseModel> warehouses,
			final int preOrderQuantity)
	{
		return findStockLevelsImpl(sellerArticleSKU, warehouses, Integer.valueOf(preOrderQuantity));
	}

	private Collection<StockLevelModel> findStockLevelsImpl(final String sellerArticleSKU,
			final Collection<WarehouseModel> warehouses, final Integer preOrderQuantity)
	{
		checkUssid(sellerArticleSKU);
		final List _warehouses = filterWarehouses(warehouses);
		if (_warehouses.isEmpty())
		{
			return Collections.EMPTY_LIST;
		}

		final StringBuilder query = new StringBuilder(150);

		query.append("SELECT {pk} FROM {StockLevel} WHERE {sellerArticleSKU} = ?sellerArticleSKU");

		if (preOrderQuantity != null)
		{
			query.append(" AND {maxPreOrder} >= ?maxPreOrder");
		}

		query.append(" AND {warehouse} IN (?WAREHOUSES_PARAM)");

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameter(MarketplacecommerceservicesConstants.SELLERARTICLESKU, sellerArticleSKU);
		if (preOrderQuantity != null)
		{
			fQuery.addQueryParameter("maxPreOrder", preOrderQuantity);
		}
		fQuery.addQueryParameter("WAREHOUSES_PARAM", _warehouses);
		final SearchResult result = getFlexibleSearchService().search(fQuery);
		return result.getResult();
	}

	@Override
	public Integer getAvailableQuantity(final WarehouseModel warehouse, final String sellerArticleSKU)
	{
		checkUssid(sellerArticleSKU);
		final Map params = new HashMap();
		/*
		 * final StringBuffer query = new StringBuffer("select {s.").append('available').append('}'); query.append(' from
		 * {').append('StockLevel').append(' as s}'); query.append(' where {s.').append('warehouse').append('} =
		 * ?').append('warehouse'); query.append(' and {s.').append('sellerArticleSKU').append('} =
		 * ?').append('sellerArticleSKU');
		 */

		final String query = "select {s. available' }  from {StockLevel as s}  where {s.warehouse} = ?warehouse  and {s.sellerArticleSKU} = ?sellerArticleSKU"
				.intern();
		params.put("warehouse", warehouse);
		params.put(MarketplacecommerceservicesConstants.SELLERARTICLESKU, sellerArticleSKU);

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query, params);
		final List resultClassList = new ArrayList();
		resultClassList.add(Integer.class);
		searchQuery.setResultClassList(resultClassList);
		final SearchResult result = getFlexibleSearchService().search(searchQuery);

		if (result.getResult().size() == 0)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("NO StockLevel instance found for ussid '" + sellerArticleSKU + "' and warehouse '" + warehouse + "'!.");
			}
			return Integer.valueOf(0);
		}
		if (result.getResult().size() > 1)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("More than one StockLevel instance found for ussid '" + sellerArticleSKU + "' and warehouse '" + warehouse
						+ "'!.");
			}
			return Integer.valueOf(0);
		}

		final Object res = result.getResult().iterator().next();

		return ((Integer) res);
	}


	private void checkUssid(final String sellerArticleSKU)
	{
		if (sellerArticleSKU != null)
		{
			return;
		}
		throw new IllegalArgumentException("ussid code cannot be null.");
	}

	private void checkWarehouse(final WarehouseModel warehouse)
	{
		if (warehouse != null)
		{
			return;
		}
		throw new IllegalArgumentException("warehouse cannot be null.");
	}

	private List<WarehouseModel> filterWarehouses(final Collection<WarehouseModel> warehouses)
	{
		if (warehouses == null)
		{
			throw new IllegalArgumentException("warehouses cannot be null.");
		}
		final Set result = new HashSet();
		for (final WarehouseModel house : warehouses)
		{
			if (house == null)
			{
				continue;
			}
			result.add(house);
		}

		return new ArrayList(result);
	}

}
