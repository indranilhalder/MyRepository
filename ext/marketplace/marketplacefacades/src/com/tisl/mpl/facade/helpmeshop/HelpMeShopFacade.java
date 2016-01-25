/**
 *
 */
package com.tisl.mpl.facade.helpmeshop;

import de.hybris.platform.commercefacades.product.data.CategoryData;

import java.util.List;

import com.tisl.mpl.data.GenderOrTitleData;
import com.tisl.mpl.data.HelpmeShopCategoryData;
import com.tisl.mpl.data.HelpmeShopData;
import com.tisl.mpl.data.ReasonOrEventData;
import com.tisl.mpl.data.TypeOfProductData;


/**
 * @author TCS
 *
 */
public interface HelpMeShopFacade
{
	List<GenderOrTitleData> getGenderOrTitle();

	List<ReasonOrEventData> getReasonOrEvent();

	List<TypeOfProductData> getTypeOfProduct();

	/**
	 * @param categoryCode
	 * @return
	 */
	List<CategoryData> getCategoryForConceirgeSearch(String categoryCode);

	/**
	 * @return
	 */
	HelpmeShopData getWebServiceForConceirgeSearch();

	/**
	 * @param categoryId
	 * @return
	 */
	HelpmeShopCategoryData getWebServicesForConceirgeCategory(String categoryId);

}
