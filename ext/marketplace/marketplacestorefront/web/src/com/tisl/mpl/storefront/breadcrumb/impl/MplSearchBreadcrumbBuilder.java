/**
 *
 */
package com.tisl.mpl.storefront.breadcrumb.impl;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.SearchBreadcrumbBuilder;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;


/**
 * @author TCS
 *
 */
public class MplSearchBreadcrumbBuilder extends SearchBreadcrumbBuilder
{

	private static final String LAST_LINK_CLASS = "active";

	@Resource(name = "categoryService")
	private CategoryService categoryService;


	@Override
	public List<Breadcrumb> getBreadcrumbs(final String categoryCode, final String searchText, final boolean emptyBreadcrumbs)
			throws IllegalArgumentException
	{
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();

		if (categoryCode == null)
		{
			final Breadcrumb breadcrumb = new Breadcrumb("/search?text=" + getEncodedUrl(searchText),
					StringEscapeUtils.escapeHtml(searchText), (emptyBreadcrumbs ? LAST_LINK_CLASS : ""));
			breadcrumbs.add(breadcrumb);
		}
		else
		{
			// Create category hierarchy path for breadcrumb
			final List<Breadcrumb> categoryBreadcrumbs = new ArrayList<>();
			final Collection<CategoryModel> categoryModels = new ArrayList<>();
			final CategoryModel lastCategoryModel = getCommerceCategoryService().getCategoryForCode(categoryCode);
			categoryModels.addAll(lastCategoryModel.getSupercategories());
			categoryBreadcrumbs.add(getCategoryBreadcrumb(lastCategoryModel, (!emptyBreadcrumbs ? LAST_LINK_CLASS : "")));

			while (!categoryModels.isEmpty())
			{
				final CategoryModel categoryModel = categoryModels.iterator().next();
				//do not add the root category to the bread crumb
				if (!(categoryModel instanceof ClassificationClassModel) && categoryModel != null
						&& !(categoryService.isRoot(categoryModel)))
				{


					categoryBreadcrumbs.add(getCategoryBreadcrumb(categoryModel));
					categoryModels.clear();
					categoryModels.addAll(categoryModel.getSupercategories());



				}
				else
				{
					categoryModels.remove(categoryModel);
				}
			}
			Collections.reverse(categoryBreadcrumbs);
			breadcrumbs.addAll(categoryBreadcrumbs);
		}
		return breadcrumbs;
	}


	/**
	 * This method will return breadcrumb for empty search result
	 * 
	 * @param searchText
	 * @return emptyResultBreadcrumb
	 * @throws IllegalArgumentException
	 */
	public List<Breadcrumb> getEmptySearchResultBreadcrumbs(final String searchText) throws IllegalArgumentException
	{

		final List<Breadcrumb> emptyResultBreadcrumb = new ArrayList<>();

		final Breadcrumb breadcrumb = new Breadcrumb("#", StringEscapeUtils.escapeHtml(searchText), "#");

		emptyResultBreadcrumb.add(breadcrumb);

		return emptyResultBreadcrumb;
	}
}
