/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;


/**
 * @author TCS
 * 
 */
public interface MplCategoryDao
{
	CategoryModel getCategoryModelForName(CatalogVersionModel catModel, String catalogName);

	Collection<CategoryModel> getMplRootCategoriesForCatalogVersion(final CatalogVersionModel catalogVersion);

	CategoryModel getCategoryModelForCode(CatalogVersionModel catalogVersion, String catalogCode);
}
