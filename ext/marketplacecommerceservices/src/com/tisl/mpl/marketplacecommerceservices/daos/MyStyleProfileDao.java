/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;


/**
 * @author TCS
 *
 */
public interface MyStyleProfileDao
{

	List<MyRecommendationsConfigurationModel> fetchRecommendedData(String genderData);

	List<MyRecommendationsConfigurationModel> fetchBrands(String genderData, String catCode);

	List<MyRecommendationsBrandsModel> fetchSubCatdOfBrands(String catCode);
}
