/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;


import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.List;


/**
 * @author TCS
 * 
 */
public interface MplPriceRowDao
{ /*
   * @Javadoc Methods to Retrieve PriceRow based on articleSKUID and catalog version
   */
	List<PriceRowModel> getPriceRowDetail(final CatalogVersionModel catalogVersionModel, final String articleSKUID);

	/*
	 * @Javadoc Methods to Retrieve List of PriceRow based on articleSKUIDs and catalog version
	 */

	List<PriceRowModel> getAllPriceRowDetail(final CatalogVersionModel catalogVersionModel, final String articleSKUIDList);

	/*
	 * @Javadoc Methods to Retrieve PriceRow based on articleSKUID and catalogVersion with having stock
	 */
	PriceRowModel getPriceRowDetailForSKUWithStockCheck(final CatalogVersionModel catalogVersionModel,
			final String articleSKUIDList);

	/*
	 * @Javadoc Methods to Retrieve minimum PriceRow based on product and catalogVersion
	 */
	PriceRowModel getMinimumPriceForProduct(final CatalogVersionModel catalogVersionModel, final ProductModel productModel);

	/**
	 * @param catalogVersionModel
	 * @param articleSKUIDList
	 * @return
	 */
	List<PriceRowModel> getAllPriceRowDetail(CatalogVersionModel catalogVersionModel, List<String> articleSKUIDList);
}