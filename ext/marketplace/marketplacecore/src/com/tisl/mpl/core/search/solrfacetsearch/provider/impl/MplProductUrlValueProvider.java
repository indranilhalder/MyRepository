/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;


import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
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

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.util.MplBuyBoxUtility;


public class MplProductUrlValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private UrlResolver<ProductModel> urlResolver;
	private FieldNameProvider fieldNameProvider;
	private CommonI18NService commonI18NService;
	private MplBuyBoxUtility mplBuyBoxUtility;
	private ProductService productService;
	private CMSSiteService cmsSiteService;

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected UrlResolver<ProductModel> getUrlResolver()
	{
		return this.urlResolver;
	}

	@Required
	public void setUrlResolver(final UrlResolver<ProductModel> urlResolver)
	{
		this.urlResolver = urlResolver;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return this.commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;
			final String leastSizeProduct = mplBuyBoxUtility.getLeastSizeProduct(product);
			final Collection fieldValues = new ArrayList();
			if (leastSizeProduct != null && !leastSizeProduct.isEmpty())
			{

				final ProductModel leastSizeProductModel = productService.getProductForCode(product.getCatalogVersion(),
						leastSizeProduct);

				if (indexedProperty.isLocalized())
				{
					final Collection<LanguageModel> languages = indexConfig.getLanguages();
					for (final LanguageModel language : languages)
					{
						fieldValues.addAll(createFieldValue(leastSizeProductModel, language, indexedProperty));
					}
				}
				else
				{
					fieldValues.addAll(createFieldValue(leastSizeProductModel, null, indexedProperty));
				}
				return fieldValues;
			}
			else
			{
				if (indexedProperty.isLocalized())
				{
					final Collection<LanguageModel> languages = indexConfig.getLanguages();
					for (final LanguageModel language : languages)
					{
						fieldValues.addAll(createFieldValue(product, language, indexedProperty));
					}
				}
				else
				{
					fieldValues.addAll(createFieldValue(product, null, indexedProperty));
				}
			}
			return Collections.emptyList();
		}

		throw new FieldValueProviderException("Cannot evaluate rating of non-product item");
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final LanguageModel language,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final String productUrl = getProductUrl(product, language);
		if (productUrl != null)
		{
			addFieldValues(fieldValues, indexedProperty, language, productUrl);
		}

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				(language == null) ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected String getProductUrl(final ProductModel product, final LanguageModel language)
	{
		this.i18nService.setCurrentLocale(this.commonI18NService.getLocaleForLanguage(language));
		return getUrlResolver().resolve(product);
	}

	/**
	 * @return the mplBuyBoxUtility
	 */
	public MplBuyBoxUtility getMplBuyBoxUtility()
	{
		return mplBuyBoxUtility;
	}

	/**
	 * @param mplBuyBoxUtility
	 *           the mplBuyBoxUtility to set
	 */
	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
	{
		this.mplBuyBoxUtility = mplBuyBoxUtility;
	}

	/**
	 * @return the cmsSiteService
	 */
	public CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	/**
	 * @param cmsSiteService
	 *           the cmsSiteService to set
	 */
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

}