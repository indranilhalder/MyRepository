
/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;


import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.basecommerce.enums.StockLevelUpdateType;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.stock.exception.StockLevelNotFoundException;
import de.hybris.platform.stock.impl.DefaultStockService;
import de.hybris.platform.stock.model.StockLevelHistoryEntryModel;
import de.hybris.platform.stock.strategy.ProductAvailabilityStrategy;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;
import de.hybris.platform.util.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.daos.ExtStockLevelDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockService;


/**
 * @author TCS
 *
 */
public class DefaultExtStockService extends DefaultStockService implements ExtStockService
{

	private ExtStockLevelDao stockLevelDao;
	private StockLevelStatusStrategy stockLevelStatusStrategy;
	private ProductAvailabilityStrategy productAvailabilityStrategy;
	//private StockLevelProductStrategy stockLevelProductStrategy;
	private static final Logger LOG = Logger.getLogger(DefaultExtStockService.class);

	@Override
	public StockLevelStatus getProductStatus(final String articleSKUID, final Collection<WarehouseModel> warehouses)
	{
		final List stockLevels = new ArrayList(this.stockLevelDao.findStockLevels(articleSKUID, warehouses));
		final StockLevelStatus status = this.stockLevelStatusStrategy.checkStatus(stockLevels);
		return status;
	}

	@Override
	public StockLevelModel createStockLevel(final String articleSKUID, final WarehouseModel warehouse, final int available)
	{
		final StockLevelModel stockLevel = createStockLevel(articleSKUID, warehouse, available, 0, 0, InStockStatus.NOTSPECIFIED,
				0, true);
		return stockLevel;
	}

	@Override
	public StockLevelModel createStockLevel(final String articleSKUID, final WarehouseModel warehouse, final int available,
			final int overSelling, final int reserved, final InStockStatus status, final int maxStockLevelHistoryCount,
			final boolean treatNegativeAsZero)
	{
		if (available < 0)
		{
			throw new IllegalArgumentException("available amount cannot be negative.");
		}
		if (overSelling < 0)
		{
			throw new IllegalArgumentException("overSelling amount cannot be negative.");
		}

		StockLevelModel stockLevel = this.stockLevelDao.findStockLevel(warehouse, articleSKUID);
		if (stockLevel != null)
		{
			throw new JaloSystemException("ussid [" + articleSKUID + "] in warehouse [" + warehouse.getName()
					+ "] already exists. the same ussid cannot be created in the same warehouse again.");
		}

		stockLevel = (StockLevelModel) getModelService().create(StockLevelModel.class);
		stockLevel.setProductCode(articleSKUID);
		stockLevel.setWarehouse(warehouse);
		stockLevel.setAvailable(available);
		stockLevel.setOverSelling(overSelling);
		stockLevel.setReserved(reserved);
		stockLevel.setInStockStatus(status);
		stockLevel.setMaxStockLevelHistoryCount(maxStockLevelHistoryCount);
		stockLevel.setTreatNegativeAsZero(treatNegativeAsZero);

		if (maxStockLevelHistoryCount != 0)
		{
			final List historyEntries = new ArrayList();
			final StockLevelHistoryEntryModel entry = createStockLevelHistoryEntry(stockLevel, available, 0,
					StockLevelUpdateType.WAREHOUSE, "new in stock");
			historyEntries.add(entry);
			stockLevel.setStockLevelHistoryEntries(historyEntries);
		}
		getModelService().save(stockLevel);
		return stockLevel;
	}



	@Override
	public int getTotalStockLevelAmount(final String articleSKUID)
	{
		final List stockLevels = new ArrayList(this.stockLevelDao.findAllStockLevels(articleSKUID));
		if (stockLevels.isEmpty())
		{
			throw new StockLevelNotFoundException("no stock level for ussid [" + articleSKUID + "] found.");
		}
		final int totalActualAmount = calculateTotalActualAmount(stockLevels);
		return totalActualAmount;
	}

	@Override
	public int getTotalStockLevelAmount(final String articleSKUID, final Collection<WarehouseModel> warehouses)
	{
		final List stockLevels = new ArrayList(this.stockLevelDao.findStockLevels(articleSKUID, warehouses));
		if (stockLevels.isEmpty())
		{
			throw new StockLevelNotFoundException("no stock level for ussid [" + articleSKUID + "] found.");
		}
		final int totalActualAmount = calculateTotalActualAmount(stockLevels);
		return totalActualAmount;
	}

	private int calculateTotalActualAmount(final List<StockLevelModel> stockLevels)
	{
		int totalActualAmount = 0;
		for (final StockLevelModel stockLevel : stockLevels)
		{
			final int actualAmount = stockLevel.getAvailable() - stockLevel.getReserved();
			if ((actualAmount <= 0) && (stockLevel.isTreatNegativeAsZero()))
			{
				continue;
			}
			totalActualAmount += actualAmount;
		}

		return totalActualAmount;
	}

	@Override
	public void setInStockStatus(final String articleSKUID, final Collection<WarehouseModel> warehouses, final InStockStatus status)
	{
		final Collection<StockLevelModel> stockLevels = new ArrayList(this.stockLevelDao.findStockLevels(articleSKUID, warehouses));
		if (stockLevels.isEmpty())
		{
			return;
		}
		for (final StockLevelModel level : stockLevels)
		{
			level.setInStockStatus(status);
		}
		getModelService().saveAll(stockLevels);
	}

	@Override
	public InStockStatus getInStockStatus(final String articleSKUID, final WarehouseModel warehouse)
	{
		final StockLevelModel stockLevel = checkAndGetStockLevel(articleSKUID, warehouse);
		return stockLevel.getInStockStatus();
	}

	private void clearCacheForItem(final StockLevelModel stockLevel)
	{
		Utilities.invalidateCache(stockLevel.getPk());
		getModelService().refresh(stockLevel);
	}

	@Override
	public void reserve(final String articleSKUID, final WarehouseModel warehouse, final int amount, final String comment)
			throws InsufficientStockLevelException
	{
		if (amount <= 0)
		{
			throw new IllegalArgumentException("amount must be greater than zero.");
		}

		final StockLevelModel currentStockLevel = checkAndGetStockLevel(articleSKUID, warehouse);

		final Integer reserved = this.stockLevelDao.reserve(currentStockLevel, amount);
		if (reserved == null)
		{
			throw new InsufficientStockLevelException("insufficient available amount for stock level [" + currentStockLevel.getPk()
					+ "]");
		}

		clearCacheForItem(currentStockLevel);
		createStockLevelHistoryEntry(currentStockLevel, StockLevelUpdateType.CUSTOMER_RESERVE, reserved.intValue(), comment);
	}

	private StockLevelHistoryEntryModel createStockLevelHistoryEntry(final StockLevelModel stockLevel,
			final StockLevelUpdateType updateType, final int reserved, final String comment)
	{
		if (stockLevel.getMaxStockLevelHistoryCount() != 0)
		{
			final StockLevelHistoryEntryModel historyEntry = (StockLevelHistoryEntryModel) getModelService().create(
					StockLevelHistoryEntryModel.class);
			historyEntry.setStockLevel(stockLevel);
			historyEntry.setActual(stockLevel.getAvailable());
			historyEntry.setReserved(reserved);
			historyEntry.setUpdateType(updateType);
			if (comment != null)
			{
				historyEntry.setComment(comment);
			}
			historyEntry.setUpdateDate(new Date());
			getModelService().save(historyEntry);
			return historyEntry;
		}
		return null;
	}

	@Override
	public void release(final String articleSKUID, final WarehouseModel warehouse, final int amount, final String comment)
	{
		if (amount <= 0)
		{
			throw new IllegalArgumentException("amount must be greater than zero.");
		}
		final StockLevelModel currentStockLevel = checkAndGetStockLevel(articleSKUID, warehouse);
		final Integer reserved = this.stockLevelDao.release(currentStockLevel, amount);
		if (reserved == null)
		{
			throw new SystemException("release failed for stock level [" + currentStockLevel.getPk() + "]!");
		}

		clearCacheForItem(currentStockLevel);
		createStockLevelHistoryEntry(currentStockLevel, StockLevelUpdateType.CUSTOMER_RELEASE, reserved.intValue(), comment);
	}

	@Override
	public void updateActualStockLevel(final String articleSKUID, final WarehouseModel warehouse, final int actualAmount,
			final String comment)
	{
		StockLevelModel stockLevel;
		try
		{
			stockLevel = checkAndGetStockLevel(articleSKUID, warehouse);
		}
		catch (final StockLevelNotFoundException localStockLevelNotFoundException)
		{
			createStockLevel(articleSKUID, warehouse, actualAmount);
			return;
		}
		try
		{
			int _amount;
			if (actualAmount < 0)
			{
				_amount = 0;
				LOG.warn("actual amount is negative, changing amount to 0");
			}
			else
			{
				_amount = actualAmount;
			}
			this.stockLevelDao.updateActualAmount(stockLevel, _amount);
			clearCacheForItem(stockLevel);
			createStockLevelHistoryEntry(stockLevel, StockLevelUpdateType.WAREHOUSE, 0, comment);
		}
		catch (final Exception e)
		{
			LOG.error("update not successful: " + e.getMessage());
			throw new SystemException(e);
		}
	}

	@Override
	public Map<WarehouseModel, Integer> getAvailability(final List<WarehouseModel> warehouses, final String articleSKUID,
			final Date date)
	{
		return this.productAvailabilityStrategy.getAvailability(articleSKUID, warehouses, date);
	}

	@Override
	public Map<WarehouseModel, Date> getAvailability(final List<WarehouseModel> warehouses, final String articleSKUID,
			final int quantity)
	{
		return this.productAvailabilityStrategy.getAvailability(articleSKUID, warehouses, quantity);
	}

	@Override
	public String getAvailability(final String articleSKUID, final WarehouseModel warehouse, final Date date,
			final LanguageModel language)
	{
		final List warehouses = new ArrayList();
		warehouses.add(warehouse);
		final String result = getAvailability(articleSKUID, warehouses, date, language);
		return result;
	}

	@Override
	public String getAvailability(final String articleSKUID, final List<WarehouseModel> warehouses, final Date date,
			final LanguageModel language)
	{
		final Map mappedAvalabilities = this.productAvailabilityStrategy.getAvailability(articleSKUID, warehouses, date);
		return this.productAvailabilityStrategy.parse(mappedAvalabilities, articleSKUID, date, language);
	}

	@Override
	public String getAvailability(final String articleSKUID, final WarehouseModel warehouse, final int quantity,
			final LanguageModel language)
	{
		final List warehouses = new ArrayList();
		warehouses.add(warehouse);
		final String result = getAvailability(articleSKUID, warehouses, quantity, language);
		return result;
	}

	@Override
	public String getAvailability(final String articleSKUID, final List<WarehouseModel> warehouses, final int quantity,
			final LanguageModel language)
	{
		final Map mappedAvalabilities = this.productAvailabilityStrategy.getAvailability(articleSKUID, warehouses, quantity);
		return this.productAvailabilityStrategy.parse(mappedAvalabilities, articleSKUID, quantity, language);
	}

	@Override
	public WarehouseModel getBestMatchOfQuantity(final Map<WarehouseModel, Integer> map)
	{
		return this.productAvailabilityStrategy.getBestMatchOfQuantity(map);
	}

	@Override
	public WarehouseModel getBestMatchOfAvailability(final Map<WarehouseModel, Date> map)
	{
		return this.productAvailabilityStrategy.getBestMatchOfAvailability(map);
	}

	@Override
	@Required
	public void setStockLevelStatusStrategy(final StockLevelStatusStrategy stockLevelStatusStrategy)
	{
		this.stockLevelStatusStrategy = stockLevelStatusStrategy;
	}

	//	@Override
	//	@Required
	//	public void setStockLevelProductStrategy(final StockLevelProductStrategy stockLevelProductStrategy)
	//	{
	//		this.stockLevelProductStrategy = stockLevelProductStrategy;
	//	}

	@Override
	@Required
	public void setProductAvailabilityStrategy(final ProductAvailabilityStrategy productAvailabilityStrategy)
	{
		this.productAvailabilityStrategy = productAvailabilityStrategy;
	}

	@Override
	public StockLevelModel getStockLevel(final String articleSKUID, final WarehouseModel warehouse)
	{
		return this.stockLevelDao.findStockLevel(articleSKUID, warehouse);
	}

	@Override
	public Collection<StockLevelModel> getAllStockLevels(final String articleSKUID)
	{
		final Collection stockLevels = this.stockLevelDao.findAllStockLevels(articleSKUID);
		return stockLevels;
	}

	@Override
	public Collection<StockLevelModel> getStockLevels(final String articleSKUID, final Collection<WarehouseModel> warehouses)
	{
		final Collection stockLevels = this.stockLevelDao.findStockLevels(articleSKUID, warehouses);
		return stockLevels;
	}


	private StockLevelModel checkAndGetStockLevel(final String articleSKUID, final WarehouseModel warehouse)
	{
		final StockLevelModel stockLevel = this.stockLevelDao.findStockLevel(warehouse, articleSKUID);
		if (stockLevel == null)
		{
			throw new StockLevelNotFoundException("no stock level for articleSKUID [" + articleSKUID + "] in warehouse ["
					+ warehouse.getName() + "] found.");
		}
		return stockLevel;
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

	@Required
	public void setStockLevelDao(final ExtStockLevelDao stockLevelDao)
	{
		this.stockLevelDao = stockLevelDao;
	}

	/**
	 * @return the stockLevelStatusStrategy
	 */
	public StockLevelStatusStrategy getStockLevelStatusStrategy()
	{
		return stockLevelStatusStrategy;
	}

	/**
	 * @return the productAvailabilityStrategy
	 */
	public ProductAvailabilityStrategy getProductAvailabilityStrategy()
	{
		return productAvailabilityStrategy;
	}

	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}

}

