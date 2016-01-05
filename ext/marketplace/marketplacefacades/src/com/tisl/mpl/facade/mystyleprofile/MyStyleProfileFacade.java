package com.tisl.mpl.facade.mystyleprofile;

/**
 *
 */

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;

import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.facades.product.data.MyStyleProfileData;


/**
 * @author TCS
 *
 */
public interface MyStyleProfileFacade
{

	void saveCategoryData(MyStyleProfileData styleProfileData);

	List<CategoryModel> getInterstedCategories();

	boolean isStyleProfileCreated();

	//New Methods
	List<MyRecommendationsConfigurationModel> fetchRecommendedData(String genderData);

	List<MyRecommendationsConfigurationModel> fetchBrands(String genderData, String catCode);

	void saveBrandData(MyStyleProfileData styleProfileData);

	void saveGenderData(String genderData);

	String fetchGenderData();

	List<CategoryModel> fetchSubCategories(String genderData, String category);

	List<CategoryModel> getInterstedBrands();

	void saveSubCategoryData(MyStyleProfileData styleProfileData);

	boolean removeMyStyleProfile();

	void removeSingleBrand(final MyStyleProfileData styleProfileData);

	public void modifyBrand(final MyStyleProfileData styleProfileData);

	public void modifyCategory(final MyStyleProfileData styleProfileDataCategory);

	public void removeSingleCategory(final MyStyleProfileData styleProfileDataCategory,
			final MyStyleProfileData styleProfileDataBrand);

	List<MyRecommendationsBrandsModel> fetchSubCatdOfBrands(String catCode);
}
