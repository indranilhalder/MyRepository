/**
 *
 */
package com.tisl.mpl.facade.category;

import de.hybris.platform.commercefacades.catalog.CatalogFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;


/**
 * @author TCS
 *
 */
public interface MplCategoryFacade extends CatalogFacade
{

	/**
	 * It will fetch the sub category tree for the given category name. (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.category.MplCategoryFacade#getShopBrandCategories(java.lang.String)
	 * @param categoryName
	 */
	public CategoryData getShopBrandCategories(final String categoryName);

	/**
	 * @param sellerName
	 */
	public String getSellerInformationBySellerName(final String sellerName);

	/**
	 * @param sellerId
	 */
	public String getActiveSellerRootCategoryBySellerId(final String sellerId);
}