/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;


/**
 * @author TCS
 *
 */
public interface MplCategoryService
{

	CategoryModel getCategoryModelForName(CatalogVersionModel catModel, String catalogName);

	Collection<CategoryModel> getMplRootCategoriesForCatalogVersion(final CatalogVersionModel catalogVersion);

	CategoryModel getMatchingCategory(final String categoryData);

	CategoryModel getCategoryModelForCode(CatalogVersionModel catModel, String catalogCode);
}
