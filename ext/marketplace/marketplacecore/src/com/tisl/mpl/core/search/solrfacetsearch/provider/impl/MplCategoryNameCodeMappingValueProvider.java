package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;


/*New Provider for Indexing Name and Code pair */

public class MplCategoryNameCodeMappingValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{
	//private String categoriesQualifier;
	private FieldNameProvider fieldNameProvider;
	private CommonI18NService commonI18NService;
	@Resource(name = "defaultProductService")
	private DefaultProductService defaultProductService;
	private CategorySource categorySource;


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
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{

		final Collection<CategoryModel> categories = getCategorySource().getCategoriesForConfigAndProperty(indexConfig,
				indexedProperty, model);
		if ((categories != null) && (!(categories.isEmpty())))
		{
			final Collection fieldValues = new ArrayList();

			if (indexedProperty.isLocalized())
			{
				final Collection<LanguageModel> languages = indexConfig.getLanguages();
				for (final LanguageModel language : languages)
				{
					for (final CategoryModel category : categories)
					{
						fieldValues.addAll(createFieldValue(category, language, indexedProperty));
					}
				}
			}
			else
			{
				for (final CategoryModel category : categories)
				{
					fieldValues.addAll(createFieldValue(category, null, indexedProperty));
				}
			}

			return fieldValues;
		}

		return Collections.emptyList();
	}

	private List<FieldValue> createFieldValue(final CategoryModel category, final LanguageModel language,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		String value = null;

		if (language != null)
		{
			final Locale locale = this.i18nService.getCurrentLocale();
			try
			{
				//this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
				this.i18nService.setCurrentLocale(getCommonI18NService().getLocaleForLanguage(language));
				value = getPropertyValue(category, "code") + "|" + getPropertyValue(category, "name");

				//System.out.println(" ***** value:" + value);
			}
			finally
			{
				this.i18nService.setCurrentLocale(locale);
			}
			final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, language.getIsocode());
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, value));
			}
		}
		else
		{
			value = getPropertyValue(category, "code") + "|" + getPropertyValue(category, "name");
			//System.out.println(" %%%%%%% value:" + value);
			final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, null);
			for (final String fieldNameVal : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldNameVal, value));
			}
		}

		return fieldValues;
	}

	private Object getPropertyValue(final Object model, final String propertyName)
	{
		return this.modelService.getAttributeValue(model, propertyName);
	}

	/*
	 * @Required public void setCategoriesQualifier(final String categoriesQualifier) { this.categoriesQualifier =
	 * categoriesQualifier; }
	 */
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}


	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}


}
