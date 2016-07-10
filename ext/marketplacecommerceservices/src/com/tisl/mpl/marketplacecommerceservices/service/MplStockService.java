/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;


import de.hybris.platform.ordersplitting.model.StockLevelModel;

import java.util.List;
import java.util.Map;


/**
 * @author TCS
 *
 */
public interface MplStockService
{
	/*
	 * Methods to Retrieve Stock based on articleSKUID
	 */


	public List<StockLevelModel> getStockLevelDetail(final String articleSKUID);

	Map<String, Integer> getAllStockLevelDetail(String aticleSKUIDs);

}
