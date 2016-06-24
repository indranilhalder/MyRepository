/**
 *
 */
package com.tisl.mpl.facade.myfavbrandcategory;

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.facades.data.MplFavBrandCategoryData;


/**
 * @author TCS
 *
 */
public interface MplMyFavBrandCategoryFacade
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
	 * @param code
	 * @return boolean
	 */
	boolean addFavCategories(String emailId, List<String> code);

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
	boolean deleteFavCategories(String emailId, String deviceId, String code);

	/**
	 * @param emailId
	 * @param deviceId
	 * @param code
	 * @return boolean
	 */
	boolean deleteFavBrands(String emailId, String deviceId, String code);

	/**
	 * @param emailId
	 * @param deviceId
	 * @param code
	 * @return boolean
	 */
	boolean addFavCategories(String emailId, String deviceId, List codeList);

	/**
	 * @param emailId
	 * @param deviceId
	 * @param codeList
	 * @return
	 */
	boolean addFavBrands(String emailId, String deviceId, List codeList);

	/**
	 * @param emailId
	 * @param deviceId
	 * @return
	 */
	List<CategoryModel> fetchFavCategories(String emailId, String deviceId);

	/**
	 * @param emailId
	 * @param deviceId
	 * @return
	 */
	List<CategoryModel> fetchFavBrands(String emailId, String deviceId);
}
