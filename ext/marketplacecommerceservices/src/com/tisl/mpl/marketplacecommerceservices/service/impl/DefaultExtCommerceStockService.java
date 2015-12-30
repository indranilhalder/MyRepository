/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.service.ExtCommerceStockService;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockService;


/**
 * @author TCS
 *
 */
public class DefaultExtCommerceStockService extends DefaultCommerceStockService implements ExtCommerceStockService
{

	@Autowired
	private ExtStockService stockService;
	private CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;
	private WarehouseSelectionStrategy warehouseSelectionStrategy;
	private StockLevelProductStrategy stockLevelProductStrategy;

	@Override
	public StockLevelStatus getStockLevelStatusForProductAndBaseStore(final BaseStoreModel baseStore, final String articleSKUID)
	{
		ServicesUtil.validateParameterNotNull(articleSKUID, "ussid  cannot be null");
		ServicesUtil.validateParameterNotNull(baseStore, "baseStore cannot be null");

		return stockService.getProductStatus(articleSKUID, getWarehouseSelectionStrategy().getWarehousesForBaseStore(baseStore));
	}

	@Override
	public Long getStockLevelForProductAndBaseStore(final String articleSKUID, final BaseStoreModel baseStore)
	{
		ServicesUtil.validateParameterNotNull(articleSKUID, "ussid cannot be null");
		ServicesUtil.validateParameterNotNull(baseStore, "baseStore cannot be null");

		return getCommerceStockLevelCalculationStrategy().calculateAvailability(
				stockService.getStockLevels(articleSKUID, getWarehouseSelectionStrategy().getWarehousesForBaseStore(baseStore)));
	}

	@Override
	public StockLevelStatus getStockLevelStatusForProductAndPointOfService(final String articleSKUID,
			final PointOfServiceModel pointOfService)
	{
		ServicesUtil.validateParameterNotNull(articleSKUID, "ussid cannot be null");
		ServicesUtil.validateParameterNotNull(pointOfService, "pointOfService cannot be null");

		if (pointOfService.getWarehouses().isEmpty())
		{
			return StockLevelStatus.OUTOFSTOCK;
		}

		return stockService.getProductStatus(articleSKUID, pointOfService.getWarehouses());
	}

	@Override
	public Long getStockLevelForProductAndPointOfService(final String articleSKUID, final PointOfServiceModel pointOfService)
	{
		ServicesUtil.validateParameterNotNull(articleSKUID, "ussid cannot be null");
		ServicesUtil.validateParameterNotNull(pointOfService, "pointOfService cannot be null");

		return getCommerceStockLevelCalculationStrategy().calculateAvailability(
				stockService.getStockLevels(articleSKUID, pointOfService.getWarehouses()));
	}


	@Override
	protected ExtStockService getStockService()
	{
		return this.stockService;
	}


	@Required
	public void setStockService(final ExtStockService stockService)
	{
		this.stockService = stockService;
	}

	@Override
	protected CommerceAvailabilityCalculationStrategy getCommerceStockLevelCalculationStrategy()
	{
		return this.commerceStockLevelCalculationStrategy;
	}

	@Override
	@Required
	public void setCommerceStockLevelCalculationStrategy(
			final CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy)
	{
		this.commerceStockLevelCalculationStrategy = commerceStockLevelCalculationStrategy;
	}

	@Override
	protected WarehouseSelectionStrategy getWarehouseSelectionStrategy()
	{
		return this.warehouseSelectionStrategy;
	}

	@Override
	@Required
	public void setWarehouseSelectionStrategy(final WarehouseSelectionStrategy warehouseSelectionStrategy)
	{
		this.warehouseSelectionStrategy = warehouseSelectionStrategy;
	}


	@Override
	protected StockLevelProductStrategy getStockLevelProductStrategy()
	{
		return this.stockLevelProductStrategy;
	}

	@Override
	@Required
	public void setStockLevelProductStrategy(final StockLevelProductStrategy stockLevelProductStrategy)
	{
		this.stockLevelProductStrategy = stockLevelProductStrategy;
	}


}
