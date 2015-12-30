/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.impl.StockLevelDao;

import java.util.Collection;


/**
 * @author TCS
 *
 */
public interface ExtStockLevelDao extends StockLevelDao
{
	public StockLevelModel findStockLevel(final WarehouseModel warehouse, final String articleSKUID);

	@Override
	public Collection<StockLevelModel> findAllStockLevels(final String articleSKUID);

	@Override
	public Collection<StockLevelModel> findStockLevels(final String articleSKUID, final Collection<WarehouseModel> warehouses);

	@Override
	public Collection<StockLevelModel> findStockLevels(final String articleSKUID, final Collection<WarehouseModel> warehouses,
			final int preOrderQuantity);

	@Override
	public Integer getAvailableQuantity(final WarehouseModel warehouse, final String articleSKUID);
}
