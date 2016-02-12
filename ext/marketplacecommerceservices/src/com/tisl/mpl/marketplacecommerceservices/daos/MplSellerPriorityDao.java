/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import com.tisl.mpl.core.model.MplSellerPriorityLevelModel;
import com.tisl.mpl.core.model.MplSellerPriorityModel;


/**
 * @author TCS
 *
 */
public interface MplSellerPriorityDao
{
	//	public List<MplSellerPriorityModel> getEntryRowsForPriority();

	public List<MplSellerPriorityModel> getAllSellerPriorities();

	public List<MplSellerPriorityLevelModel> loadExistingUssid(String ussid);

	public List<ProductModel> getProductListForCategory(final CategoryModel categoryModel);

	public List<MplSellerPriorityLevelModel> loadExistingUssids();
}
