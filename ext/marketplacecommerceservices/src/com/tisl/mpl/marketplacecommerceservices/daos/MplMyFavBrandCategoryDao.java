/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.List;

import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;


/**
 * @author TCS
 *
 */
public interface MplMyFavBrandCategoryDao
{
	/**
	 * @return List
	 */
	List<MyRecommendationsConfigurationModel> fetchRecommendedData();

	/**
	 * @param catCode
	 * @return List
	 */
	List<MyRecommendationsConfigurationModel> fetchBrands(String catCode);
}
