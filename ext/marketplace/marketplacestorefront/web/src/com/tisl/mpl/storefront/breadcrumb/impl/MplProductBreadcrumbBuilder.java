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

import org.apache.log4j.Logger;


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


	protected static final Logger LOG = Logger.getLogger(MplProductBreadcrumbBuilder.class);

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

		LOG.debug("====Inside breadcum methods==========");
		final List<Breadcrumb> breadcrumbs = new ArrayList<>();

		final Collection<CategoryModel> categoryModels = new ArrayList<>();
		final Breadcrumb last;
		LOG.debug("====Step 1==========");
		final ProductModel baseProductModel = getBaseProduct(productModel);
		LOG.debug("====Step 2==========");
		last = getProductBreadcrumb(productModel);//Fetching the product name in Breadcrumb instead of base product name
		LOG.debug("====Step 3==========" + last.getCategoryCode());
		LOG.debug("====Step 3==========" + last.getName());
		categoryModels.addAll(baseProductModel.getSupercategories());
		LOG.debug("====Step 4==========" + categoryModels.size());
		last.setLinkClass(LAST_LINK_CLASS);
		LOG.debug("====Step 5==========" + last.getLinkClass());

		breadcrumbs.add(last);
		LOG.debug("====Step 6==========" + breadcrumbs.size());

		while (!categoryModels.isEmpty())
		{
			LOG.debug("====Step 7==========");
			CategoryModel toDisplay = null;
			for (final CategoryModel categoryModel : categoryModels)
			{
				LOG.debug("====Step 8==========");
				//if (!(categoryModel instanceof ClassificationClassModel))
				if (categoryModel.getCode().startsWith(
						configurationService.getConfiguration().getString("marketplace.mplcatalog.salescategory.code")))
				{
					LOG.debug("====Step 9==========");
					if (toDisplay == null)
					{
						LOG.debug("====Step 10==========");
						toDisplay = categoryModel;
					}
					if (getBrowseHistory().findEntryMatchUrlEndsWith(categoryModel.getCode()) != null)
					{
						LOG.debug("====Step 11==========");
						break;
					}
				}
			}
			categoryModels.clear();
			if (toDisplay != null)
			{
				LOG.debug("====Step 12==========");
				//do not add the root category(Sales/Primary) to the bread crumb
				if (!categoryService.isRoot(toDisplay))
				{
					LOG.debug("====Step 13==========");
					breadcrumbs.add(getCategoryBreadcrumb(toDisplay));
					categoryModels.addAll(toDisplay.getSupercategories());
				}
			}
		}
		LOG.debug("====Step 14==========");
		Collections.reverse(breadcrumbs);
		LOG.debug("====Step 15==========");
		return breadcrumbs;
	}


	@Override
	public Breadcrumb getProductBreadcrumb(final ProductModel product)
	{
		final String productUrl = getProductModelUrlResolver().resolve(product);
		return new Breadcrumb(productUrl, product.getTitle(), null);
	}
}
