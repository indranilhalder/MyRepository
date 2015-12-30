/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;


import de.hybris.platform.ordersplitting.model.StockLevelModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplStockDao
{
	/*
	 * @Javadoc Methods to Retrieve Stock based on articleSKUID
	 */
	List<StockLevelModel> getStockDetail(final String articleSKUID);

	/*
	 * @Javadoc Methods to Retrieve List of Stock based on articleSKUIDs
	 */
	List<StockLevelModel> getAllStockDetail(final String articleSKUIDs);

}
