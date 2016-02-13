/**
 *
 */
package com.tisl.mpl.marketplacecommerceservice.url;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;


/**
 * @author TCS
 *
 */
public class ExtDefaultProductModelUrlResolver extends DefaultProductModelUrlResolver
{

	@Resource
	private ConfigurationService configurationService;

	/**
 *
 */
	@Override
	public String resolveInternal(final ProductModel source)
	{
		//final ProductModel baseProduct = getBaseProduct(source);

		//final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

		String url = getPattern();

		//		if ((currentBaseSite != null) && (url.contains("{baseSite-uid}")))
		//		{
		//			url = url.replace("{baseSite-uid}", currentBaseSite.getUid());
		//		}
		//		if (url.contains("{category-path}"))
		//		{
		//			url = url.replace("{category-path}", buildPathString(getCategoryPath(baseProduct)));
		//		}
		if (url.contains("{product-name}"))
		{
			url = url.replace("{product-name}", urlSafe(source.getName()));
		}
		if (url.contains("{product-code}"))
		{
			url = url.replace("{product-code}", source.getCode());
		}

		return url;
	}

	@Override
	public String buildPathString(final List<CategoryModel> path)
	{
		if ((path == null) || (path.isEmpty()))
		{
			return "c";
		}

		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < path.size(); ++i)
		{
			if (i != 0)
			{
				result.append('/');
			}
			result.append(urlSafe(path.get(i).getName()));
		}

		return result.toString();
	}

	@Override
	public List<CategoryModel> getCategoryPath(final ProductModel product)
	{
		final CategoryModel category = getSalesCategoryForProduct(product);
		if (category != null)
		{
			return getCategoryPath(category);
		}
		return Collections.emptyList();
	}

	@Override
	public CategoryModel getPrimaryCategoryForProduct(final ProductModel product)
	{
		for (final CategoryModel category : product.getSupercategories())
		{
			if (null != category.getCode())
			{
				if (!(category instanceof ClassificationClassModel))
				{
					return category;
				}
				/*
				 * if (!(category instanceof ClassificationClassModel)) { return category; }
				 */
			}
		}
		return null;
	}

	public CategoryModel getSalesCategoryForProduct(final ProductModel product)
	{
		for (final CategoryModel category : product.getSupercategories())
		{
			if (null != category.getCode())
			{

				if (category.getCode().startsWith(
						configurationService.getConfiguration().getString("marketplace.mplcatalog.salescategory.code")))
				{
					return category;
				}

			}
		}
		return null;
	}

	@Override
	public List<CategoryModel> getCategoryPath(final CategoryModel category)
	{
		final Collection paths = getCommerceCategoryService().getPathsForCategory(category);

		return ((List) paths.iterator().next());
	}

	@Override
	public ProductModel getBaseProduct(final ProductModel product)
	{
		ProductModel current = product;

		while (current instanceof VariantProductModel)
		{
			final ProductModel baseProduct = ((VariantProductModel) current).getBaseProduct();
			if (baseProduct == null)
			{
				break;
			}

			current = baseProduct;
		}

		return current;
	}
}
