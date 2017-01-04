/**
 *
 */
package com.tisl.mpl.promotion.service;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author TCS
 *
 */
public interface UpdateSplPriceHelperService
{

	/**
	 * Populates the Eligible Product List
	 *
	 * TISPRO-352 : Fix
	 *
	 * @param brands
	 * @param rejectBrandList
	 * @param priority
	 * @param categories
	 * @param exProductList
	 * @return ConcurrentHashMap<List<String>, List<String>>
	 */
	ConcurrentHashMap<List<String>, List<String>> getEligibleProductList(List<String> brands, List<String> rejectBrandList,
			Integer priority, List<Category> categories, List<String> exProductList);

	/**
	 * Populates the Eligible Product List
	 *
	 * TISPRO-352 : Fix
	 *
	 * @param brands
	 * @param rejectBrandList
	 * @param priority
	 * @param categories
	 * @return ConcurrentHashMap<List<String>, List<String>>
	 */
	ConcurrentHashMap<List<String>, List<String>> getEligibleProductList(List<String> brands, List<String> rejectBrandList,
			Integer priority, List<Category> categories);

	/**
	 * Validates Product Priority Logic
	 *
	 * TISPRO-352 : Fix
	 *
	 * @param product
	 * @param priority
	 * @return boolean
	 */
	boolean validateProductData(ProductModel product, Integer priority);
}
