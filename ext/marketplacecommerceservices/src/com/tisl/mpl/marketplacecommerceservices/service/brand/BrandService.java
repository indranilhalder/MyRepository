/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand;

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.PriorityBrandsModel;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;


/**
 * @author TCS
 *
 */
public interface BrandService
{

	Map<Character, List<CategoryModel>> getAllBrandsInAplhabeticalOrder(String rootBrandCode);

	boolean checkEmailId(String emailId);

	Map<Character, List<CategoryModel>> getAllBrandsFromCmsCockpit(String componentUid);

	/**
	 * @param emailId
	 * @return
	 */
	List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(String emailId);

	/**
	 * @param emailId
	 * @return
	 */
	List<MplNewsLetterSubscriptionModel> checkEmailIdForMarketplace(String emailId);


	List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(final String emailId, final String isLuxury);
	public List<PriorityBrandsModel> priorityBrands(final String categoryCode);
}
