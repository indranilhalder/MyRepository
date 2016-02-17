/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

/**
 * @author 360641
 *
 */
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MplConstants;


public class MplDepartmentsValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private CategorySource categorySource;
	private FieldNameProvider fieldNameProvider;
	private CategoryService categoryService;

	protected CategorySource getCategorySource()
	{
		return this.categorySource;
	}

	@Required
	public void setCategorySource(final CategorySource categorySource)
	{
		this.categorySource = categorySource;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
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

	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final Collection categories = getCategorySource().getCategoriesForConfigAndProperty(indexConfig, indexedProperty, model);
		final Collection fieldValues = new ArrayList();
		if ((categories != null) && (!(categories.isEmpty())))
		{
			final Set categoryPaths = getCategoryPaths(categories);
			fieldValues.addAll(createFieldValue(categoryPaths, indexedProperty));
		}
		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final Collection<String> categoryPaths, final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			for (final String path : categoryPaths)
			{
				fieldValues.add(new FieldValue(fieldName, path));
			}
		}

		return fieldValues;
	}

	protected Set<String> getCategoryPaths(final Collection<CategoryModel> categories)
	{
		final Set allPaths = new HashSet();

		for (final CategoryModel category : categories)
		{
			if (category instanceof ClassificationClassModel)
			{
				continue;
			}
			final Collection<List<CategoryModel>> pathsForCategory = getCategoryService().getPathsForCategory(category);
			if (pathsForCategory == null)
			{
				continue;
			}
			for (final List categoryPath : pathsForCategory)
			{
				//if (categoryPath != null && categoryPath.size() > 0 && ((CategoryModel) categoryPath.get(0)).getCode().contains(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE))
				if (CollectionUtils.isNotEmpty(categoryPath)
						&& ((CategoryModel) categoryPath.get(0)).getCode().contains(MplConstants.SALES_HIERARCHY_ROOT_CATEGORY_CODE))
				{
					accumulateCategoryPaths(categoryPath, allPaths);
				}
			}

		}

		return allPaths;
	}

	@SuppressWarnings("boxing")
	protected void accumulateCategoryPaths(final List<CategoryModel> categoryPath, final Set<String> output)
	{
		int level = 0;
		for (final CategoryModel category : categoryPath)
		{
			if (category instanceof ClassificationClassModel)
			{
				return;
			}
			if (level == 1)
			{
				output.add(category.getCode());
				return;
			}

			level = level + 1;
		}
	}
}