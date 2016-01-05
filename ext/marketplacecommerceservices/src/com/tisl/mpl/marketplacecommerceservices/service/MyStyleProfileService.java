/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;

import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;


/**
 * @author TCS
 *
 */
public interface MyStyleProfileService
{

	void saveCategoryData(List<CategoryModel> categoryList);

	List<CategoryModel> getInterstedCategories();

	boolean isStyleProfileCreated();

	//New Method
	List<MyRecommendationsConfigurationModel> fetchRecommendedData(String genderData);

	List<MyRecommendationsConfigurationModel> fetchBrands(String genderData, String catCode);

	void saveBrandData(List<CategoryModel> categoryList);

	void saveGenderData(String genderData);

	String fetchGenderData();

	List<CategoryModel> fetchSubCategories(String genderData, String category);

	List<CategoryModel> getInterstedBrands();

	void saveSubCategoryData(List<CategoryModel> categoryList);

	public boolean removeMyStyleProfile();

	public void removeSingleBrand(final List<CategoryModel> categoryList);

	public void modifyBrand(final List<CategoryModel> categoryList);

	public void removeSingleCategory(final List<CategoryModel> categoryList, final List<CategoryModel> brandList);

	public void modifyCategory(final List<CategoryModel> categoryList);

	List<MyRecommendationsBrandsModel> fetchSubCatdOfBrands(String catCode);
}
