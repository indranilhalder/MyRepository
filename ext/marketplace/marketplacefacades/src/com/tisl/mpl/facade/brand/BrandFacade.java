/**
 *
 */
package com.tisl.mpl.facade.brand;

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;


/**
 * @author TCS
 *
 */
public interface BrandFacade
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
	 * @param string
	 * @return
	 */
	List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(String emailId, String string);

	/**
	 * @param emailId
	 * @return
	 */
	List<MplNewsLetterSubscriptionModel> checkEmailIdForMarketplace(String emailId);

	/**
	 * @param emailId
	 * @return
	 */
}
