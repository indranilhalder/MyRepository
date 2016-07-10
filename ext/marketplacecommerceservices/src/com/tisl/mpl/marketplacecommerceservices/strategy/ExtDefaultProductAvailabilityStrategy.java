/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.strategy.impl.DefaultProductAvailabilityStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.ExtStockLevelDao;


/**
 * @author TCS
 *
 */
public class ExtDefaultProductAvailabilityStrategy extends DefaultProductAvailabilityStrategy
{
	@Autowired
	private ExtStockLevelDao stockLevelDao;

	@Override
	public Map<WarehouseModel, Integer> getAvailability(final String articleSKUID, final List<WarehouseModel> warehouses,
			final Date date)
	{
		final Map results = new HashMap();

		for (final WarehouseModel warehouse : warehouses)
		{
			final Integer quantity = this.stockLevelDao.getAvailableQuantity(warehouse, articleSKUID);
			results.put(warehouse, quantity);
		}
		return Collections.unmodifiableMap(results);
	}

	@Override
	public Map<WarehouseModel, Date> getAvailability(final String articleSKUID, final List<WarehouseModel> warehouses,
			final int preOrderQuantity)
	{
		final Map results = new HashMap();

		final Collection<StockLevelModel> stockLevels = this.stockLevelDao.findStockLevels(articleSKUID, warehouses,
				preOrderQuantity);

		for (final StockLevelModel stockLevel : stockLevels)
		{
			results.put(stockLevel.getWarehouse(), stockLevel.getNextDeliveryTime());
		}
		return Collections.unmodifiableMap(results);
	}

	/**
	 * @return the stockLevelDao
	 */
	public ExtStockLevelDao getStockLevelDao()
	{
		return stockLevelDao;
	}

	/**
	 * @param stockLevelDao
	 *           the stockLevelDao to set
	 */
	public void setStockLevelDao(final ExtStockLevelDao stockLevelDao)
	{
		this.stockLevelDao = stockLevelDao;
	}
}
