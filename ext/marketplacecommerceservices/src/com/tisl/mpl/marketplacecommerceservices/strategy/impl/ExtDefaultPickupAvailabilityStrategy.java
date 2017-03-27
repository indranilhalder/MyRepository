/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.marketplacecommerceservices.strategy.impl;

import de.hybris.platform.commerceservices.delivery.dao.StorePickupDao;
import de.hybris.platform.commerceservices.enums.PickupInStoreMode;
import de.hybris.platform.commerceservices.strategies.PickupAvailabilityStrategy;
import de.hybris.platform.commerceservices.strategies.PickupStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.stock.strategy.StockLevelProductStrategy;
import de.hybris.platform.store.BaseStoreModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author TCS
 *
 */
public class ExtDefaultPickupAvailabilityStrategy implements PickupAvailabilityStrategy
{
	private PickupStrategy pickupStrategy;
	private StockLevelProductStrategy stockLevelProductStrategy;
	private StorePickupDao storePickupDao;

	protected PickupStrategy getPickupStrategy()
	{
		return this.pickupStrategy;
	}

	@Required
	public void setPickupStrategy(final PickupStrategy pickupStrategy)
	{
		this.pickupStrategy = pickupStrategy;
	}

	protected StockLevelProductStrategy getStockLevelProductStrategy()
	{
		return this.stockLevelProductStrategy;
	}

	@Required
	public void setStockLevelProductStrategy(final StockLevelProductStrategy stockLevelProductStrategy)
	{
		this.stockLevelProductStrategy = stockLevelProductStrategy;
	}

	protected StorePickupDao getStorePickupDao()
	{
		return this.storePickupDao;
	}

	@Required
	public void setStorePickupDao(final StorePickupDao storePickupDao)
	{
		this.storePickupDao = storePickupDao;
	}

	@Override
	public Boolean isPickupAvailableForProduct(final ProductModel product, final BaseStoreModel baseStore)
	{
		ServicesUtil.validateParameterNotNull(product, "product must not be null");
		if (!(PickupInStoreMode.DISABLED.equals(getPickupStrategy().getPickupInStoreMode())))
		{
			ServicesUtil.validateParameterNotNull(baseStore, "baseStore must not be null");
			//Removed due to thread lock PT fix
			//if (CollectionUtils.isNotEmpty(baseStore.getPointsOfService()))
			//{
			return getStorePickupDao().checkProductForPickup(getStockLevelProductStrategy().convert(product), baseStore);
			//}
		}
		return Boolean.FALSE;
	}
}