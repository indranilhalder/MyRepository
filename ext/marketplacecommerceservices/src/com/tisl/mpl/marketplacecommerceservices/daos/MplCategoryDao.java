/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplCategoryDao
{
	CategoryModel getCategoryModelForName(CatalogVersionModel catModel, String catalogName);

	Collection<CategoryModel> getMplRootCategoriesForCatalogVersion(final CatalogVersionModel catalogVersion);

	CategoryModel getCategoryModelForCode(CatalogVersionModel catalogVersion, String catalogCode);

	//TPR-1285
	List<CategoryModel> getLowestPrimaryCategories();

	List<ProductModel> getProductForL2code(CatalogVersionModel catalogVersion, String l2CategoryCode);
}
