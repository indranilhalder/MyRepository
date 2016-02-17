/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand;

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;
import java.util.Map;


/**
 * @author TCS
 *
 */
public interface BrandService
{

	Map<Character, List<CategoryModel>> getAllBrandsInAplhabeticalOrder(String rootBrandCode);

	boolean checkEmailId(String emailId);
}
