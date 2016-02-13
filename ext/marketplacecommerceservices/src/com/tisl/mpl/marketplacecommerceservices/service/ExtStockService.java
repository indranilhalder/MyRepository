/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author TCS
 *
 */
public interface ExtStockService extends StockService
{

	public StockLevelStatus getProductStatus(final String articleSKUID, final Collection<WarehouseModel> warehouses);

	public StockLevelModel createStockLevel(final String articleSKUID, final WarehouseModel warehouse, final int available);

	public StockLevelModel createStockLevel(final String articleSKUID, final WarehouseModel warehouse, final int available,
			final int overSelling, final int reserved, final InStockStatus status, final int maxStockLevelHistoryCount,
			final boolean treatNegativeAsZero);

	public int getTotalStockLevelAmount(final String articleSKUID);

	public int getTotalStockLevelAmount(final String articleSKUID, final Collection<WarehouseModel> warehouses);

	public void setInStockStatus(final String articleSKUID, final Collection<WarehouseModel> warehouses, final InStockStatus status);

	public InStockStatus getInStockStatus(final String articleSKUID, final WarehouseModel warehouse);

	public void reserve(final String articleSKUID, final WarehouseModel warehouse, final int amount, final String comment)
			throws InsufficientStockLevelException;

	public void release(final String articleSKUID, final WarehouseModel warehouse, final int amount, final String comment);

	public void updateActualStockLevel(final String articleSKUID, final WarehouseModel warehouse, final int actualAmount,
			final String comment);

	public Map<WarehouseModel, Integer> getAvailability(final List<WarehouseModel> warehouses, final String articleSKUID,
			final Date date);

	public Map<WarehouseModel, Date> getAvailability(final List<WarehouseModel> warehouses, final String articleSKUID,
			final int quantity);

	public String getAvailability(final String articleSKUID, final List<WarehouseModel> warehouses, final Date date,
			final LanguageModel language);

	public String getAvailability(final String articleSKUID, final WarehouseModel warehouse, final Date date,
			final LanguageModel language);

	public String getAvailability(final String articleSKUID, final WarehouseModel warehouse, final int quantity,
			final LanguageModel language);

	public String getAvailability(final String articleSKUID, final List<WarehouseModel> warehouses, final int quantity,
			final LanguageModel language);

	public StockLevelModel getStockLevel(final String articleSKUID, final WarehouseModel warehouse);

	public Collection<StockLevelModel> getAllStockLevels(final String articleSKUID);

	public Collection<StockLevelModel> getStockLevels(final String articleSKUID, final Collection<WarehouseModel> warehouses);
}
