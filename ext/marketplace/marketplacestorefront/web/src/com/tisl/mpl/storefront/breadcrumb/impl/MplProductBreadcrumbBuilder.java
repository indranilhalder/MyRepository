/**
 *
 */
package com.tisl.mpl.storefront.breadcrumb.impl;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;


/**
 * This class is extended to add some more functionality to the product bread crumb builder
 *
 * @author TCS
 *
 */
public class MplProductBreadcrumbBuilder extends ProductBreadcrumbBuilder
{
	private static final String LAST_LINK_CLASS = "active";
	@Resource
	private ConfigurationService configurationService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	/**
	 * This method creates the Bread crumb for the given product
	 *
	 * @param productModel
	 * @throws IllegalArgumentException
	 * @return List<Breadcrumb>
	 */
	@Override
	public List<Breadcrumb> getBreadcrumbs(final ProductModel productModel) throws IllegalArgumentException
	{


		final List<Breadcrumb> breadcrumbs = new ArrayList<>();

		final Collection<CategoryModel> categoryModels = new ArrayList<>();
		final Breadcrumb last;

		final ProductModel baseProductModel = getBaseProduct(productModel);
		last = getProductBreadcrumb(productModel);//Fetching the product name in Breadcrumb instead of base product name

		categoryModels.addAll(baseProductModel.getSupercategories());
		last.setLinkClass(LAST_LINK_CLASS);

		breadcrumbs.add(last);

		while (!categoryModels.isEmpty())
		{
			CategoryModel toDisplay = null;
			for (final CategoryModel categoryModel : categoryModels)
			{
				//if (!(categoryModel instanceof ClassificationClassModel))
				if (categoryModel.getCode().startsWith(
						configurationService.getConfiguration().getString("marketplace.mplcatalog.salescategory.code")))
				{
					if (toDisplay == null)
					{
						toDisplay = categoryModel;
					}
					if (getBrowseHistory().findEntryMatchUrlEndsWith(categoryModel.getCode()) != null)
					{
						break;
					}
				}
			}
			categoryModels.clear();
			if (toDisplay != null)
			{
				//do not add the root category(Sales/Primary) to the bread crumb
				if (!categoryService.isRoot(toDisplay))
				{
					breadcrumbs.add(getCategoryBreadcrumb(toDisplay));
					categoryModels.addAll(toDisplay.getSupercategories());
				}
			}
		}
		Collections.reverse(breadcrumbs);
		return breadcrumbs;
	}


	@Override
	public Breadcrumb getProductBreadcrumb(final ProductModel product)
	{
		final String productUrl = getProductModelUrlResolver().resolve(product);
		return new Breadcrumb(productUrl, product.getName(), null);
	}
}
