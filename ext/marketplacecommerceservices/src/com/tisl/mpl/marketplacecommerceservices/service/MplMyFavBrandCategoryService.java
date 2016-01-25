/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.facades.data.MplFavBrandCategoryData;


/**
 * @author TCS
 *
 */
public interface MplMyFavBrandCategoryService
{
	/**
	 * @param emailId
	 * @return List
	 */
	List<CategoryModel> fetchFavCategories(String emailId);

	/**
	 * @param emailId
	 * @return List
	 */
	List<CategoryModel> fetchFavBrands(String emailId);

	/**
	 * @return Map
	 */
	Map<String, MplFavBrandCategoryData> fetchAllCategories();

	/**
	 * @return Map
	 */
	Map<String, MplFavBrandCategoryData> fetchAllBrands();

	/**
	 * @param emailId
	 * @param codeList
	 * @return boolean
	 */
	boolean addFavCategories(String emailId, List<String> codeList);

	/**
	 * @param emailId
	 * @param codeList
	 * @return boolean
	 */
	boolean addFavBrands(String emailId, List<String> codeList);

	/**
	 * @param emailId
	 * @param code
	 * @return boolean
	 */
	boolean deleteFavCategories(String emailId, String code);

	/**
	 * @param emailId
	 * @param code
	 * @return boolean
	 */
	boolean deleteFavBrands(String emailId, String code);

}
