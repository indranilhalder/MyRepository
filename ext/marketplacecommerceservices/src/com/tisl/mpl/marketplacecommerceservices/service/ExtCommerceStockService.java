/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * @author TCS
 *
 */
public interface ExtCommerceStockService extends CommerceStockService
{
	public StockLevelStatus getStockLevelStatusForProductAndBaseStore(final BaseStoreModel baseStore, final String articleSKUID);

	public Long getStockLevelForProductAndBaseStore(final String articleSKUID, final BaseStoreModel baseStore);

	public StockLevelStatus getStockLevelStatusForProductAndPointOfService(final String articleSKUID,
			final PointOfServiceModel pointOfService);

	public Long getStockLevelForProductAndPointOfService(final String articleSKUID, final PointOfServiceModel pointOfService);
}
