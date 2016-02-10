package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultBrandCategorySource implements CategorySource
{
	private static final Logger LOG = Logger.getLogger(DefaultBrandCategorySource.class);
	private ModelService modelService;
	private String categoriesQualifier;
	private boolean includeClassificationClasses;
	private String rootCategory;
	private CategoryService categoryService;

	protected ModelService getModelService()
	{
		return this.modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected String getCategoriesQualifier()
	{
		return this.categoriesQualifier;
	}

	@Required
	public void setCategoriesQualifier(final String categoriesQualifier)
	{
		this.categoriesQualifier = categoriesQualifier;
	}

	public boolean isIncludeClassificationClasses()
	{
		return this.includeClassificationClasses;
	}

	public void setIncludeClassificationClasses(final boolean includeClassificationClasses)
	{
		this.includeClassificationClasses = includeClassificationClasses;
	}

	protected String getRootCategory()
	{
		return this.rootCategory;
	}

	public void setRootCategory(final String rootCategory)
	{
		this.rootCategory = rootCategory;
	}

	protected CategoryService getCategoryService()
	{
		return this.categoryService;
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public Collection<CategoryModel> getCategoriesForConfigAndProperty(final IndexConfig indexConfig,
			final IndexedProperty indexedProperty, final Object model)
	{
		final Set products = getProducts(model);
		final Set<CategoryModel> directCategories = getDirectSuperCategories(products);

		if ((directCategories != null) && (!directCategories.isEmpty()))
		{
			final Set rootCategories = lookupRootCategories(indexConfig.getCatalogVersions());

			final Set allCategories = new HashSet();
			for (final CategoryModel category : directCategories)
			{
				final Set intermediateCategories = new HashSet();
				for (final CategoryModel categoryModel : getAllCategories(category, rootCategories))
				{

					if (categoryModel != null && ((categoryModel.getCode().startsWith("MBH1") && categoryModel.getCode().length() <= 5)
							|| categoryModel.getCode().equalsIgnoreCase("MSH1")))
					{
						continue;
					}

					intermediateCategories.add(categoryModel);
				}
				allCategories.addAll(intermediateCategories);
			}
			return allCategories;
		}

		return Collections.emptyList();
	}

	protected Set<ProductModel> getProducts(final Object model)
	{
		if ((model instanceof VariantProductModel))
		{
			final Set products = new HashSet();

			ProductModel currentProduct = (ProductModel) model;
			while ((currentProduct instanceof VariantProductModel))
			{
				products.add(currentProduct);
				currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();
			}

			products.add(currentProduct);
			return products;
		}
		if ((model instanceof ProductModel))
		{
			return Collections.singleton((ProductModel) model);
		}
		return Collections.emptySet();
	}

	protected Set<CategoryModel> getDirectSuperCategories(final Set<ProductModel> products)
	{
		final Set categories = new HashSet();

		for (final ProductModel product : products)
		{
			final Collection directCategories = (Collection) getModelService().getAttributeValue(product, getCategoriesQualifier());
			if ((directCategories == null) || (directCategories.isEmpty()))
			{
				continue;
			}
			categories.addAll(directCategories);
		}

		return categories;
	}

	protected Collection<CategoryModel> getAllCategories(final CategoryModel directCategory,
			final Set<CategoryModel> rootCategories)
	{
		if (isBlockedCategory(directCategory))
		{
			return Collections.emptyList();
		}

		if ((rootCategories != null) && (!rootCategories.isEmpty()))
		{
			return collectSuperCategories(directCategory, rootCategories, new HashSet(3));
		}

		final Collection categories = new ArrayList();
		categories.add(directCategory);
		for (final CategoryModel superCategory : directCategory.getAllSupercategories())
		{
			if (isBlockedCategory(superCategory))
			{
				continue;
			}
			categories.add(superCategory);
		}

		return categories;
	}

	protected boolean isBlockedCategory(final CategoryModel category)
	{
		return ((category instanceof ClassificationClassModel)) && (!isIncludeClassificationClasses());
	}

	protected Set<CategoryModel> collectSuperCategories(final CategoryModel category, final Set<CategoryModel> rootCategories,
			final Set<CategoryModel> path)
	{
		if ((category == null) || (isBlockedCategory(category)))
		{
			return Collections.emptySet();
		}

		if (path.contains(category))
		{
			return Collections.emptySet();
		}

		path.add(category);

		if (rootCategories.contains(category))
		{
			return path;
		}

		final List<CategoryModel> superCategories = category.getSupercategories();
		if ((superCategories == null) || (superCategories.isEmpty()))
		{
			return Collections.emptySet();
		}

		if (superCategories.size() == 1)
		{
			return collectSuperCategories(superCategories.iterator().next(), rootCategories, path);
		}

		final HashSet result = new HashSet();

		for (final CategoryModel superCategory : superCategories)
		{
			if (isBlockedCategory(superCategory))
			{
				continue;
			}
			result.addAll(collectSuperCategories(superCategory, rootCategories, new HashSet(path)));
		}

		return result;
	}

	protected Set<CategoryModel> lookupRootCategories(final Collection<CatalogVersionModel> catalogVersions)
	{
		final String categoryCode = getRootCategory();
		if ((categoryCode != null) && (!categoryCode.isEmpty()))
		{
			String[] categoryCodeAry = null;
			if (categoryCode.contains(","))
			{
				categoryCodeAry = categoryCode.split(",");
			}
			else
			{
				categoryCodeAry = new String[]
				{ categoryCode };
			}
			final Set result = new HashSet(categoryCodeAry.length);

			for (final CatalogVersionModel catalogVersion : catalogVersions)
			{
				for (final String catCode : categoryCodeAry)
				{
					try
					{
						result.add(getCategoryService().getCategoryForCode(catalogVersion, catCode));
					}
					catch (final UnknownIdentifierException localUnknownIdentifierException)
					{
						LOG.warn("Failed to load category [" + categoryCode + "] from catalog version ["
								+ catalogVersionToString(catalogVersion) + "]");
					}
				}
			}

			if (result.isEmpty())
			{
				LOG.error("Failed to find Category with code [" + categoryCode + "] in catalog versions ["
						+ catalogVersionsToString(catalogVersions) + "]");
			}
			else
			{
				return result;
			}
		}

		return null;
	}

	protected String catalogVersionToString(final CatalogVersionModel catalogVersion)
	{
		if (catalogVersion == null)
		{
			return "<null>";
		}
		if (catalogVersion.getCatalog() == null)
		{
			return "<null>:" + catalogVersion.getVersion();
		}
		return catalogVersion.getCatalog().getId() + ":" + catalogVersion.getVersion();
	}

	protected String catalogVersionsToString(final Collection<CatalogVersionModel> catalogVersions)
	{
		final StringBuilder buf = new StringBuilder();
		for (final CatalogVersionModel catalogVersion : catalogVersions)
		{
			if (buf.length() > 0)
			{
				buf.append(", ");
			}

			buf.append(catalogVersionToString(catalogVersion));
		}
		return buf.toString();
	}
}