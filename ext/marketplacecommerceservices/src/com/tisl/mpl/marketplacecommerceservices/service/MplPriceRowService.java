/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.List;
import java.util.Map;


/**
 * @author TCS
 * 
 */
public interface MplPriceRowService
{
	/*
	 * @Javadoc Methods to Retrieve Pricerow based on articleSKUID
	 */
	List<PriceRowModel> getPriceRowDetail(final String aticleSKUID);

	Map<String, PriceRowModel> getAllPriceRowDetail(String aticleSKUIDs);

	PriceRowModel getLeastPriceForProduct(final ProductModel product);

	/**
	 * @param aticleSKUIDs
	 * @return
	 */
	Map<String, PriceRowModel> getAllPriceRowDetail(List<String> aticleSKUIDs);
}