/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;


import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.util.MplBuyBoxUtility;


public class MplDisplayUrlValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private UrlResolver<ProductModel> urlResolver;
	private FieldNameProvider fieldNameProvider;
	private CommonI18NService commonI18NService;
	private MplBuyBoxUtility mplBuyBoxUtility;


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
		if (model instanceof PcmProductVariantModel)
		{
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel pcmVariantModel = (PcmProductVariantModel) model;

			final JSONArray urlSizeJsonArray = new JSONArray();
			//	System.out.println("**Inside DisplayPriceValueProvider**");
			//	final Set<String> displayUrls = new TreeSet<String>();

			String productUrl = null;
			//	Fetch sizes in all the Variants
			for (final VariantProductModel pcmProductVariantModel : pcmVariantModel.getBaseProduct().getVariants())
			{

				final PcmProductVariantModel pcmSizeVariantModel = (PcmProductVariantModel) pcmProductVariantModel;

				//Included for Electronics Product
				final String sizeVariantColour = mplBuyBoxUtility.getVariantColour(pcmSizeVariantModel,
						pcmSizeVariantModel.getFeatures());


				final String pcmVariantColour = mplBuyBoxUtility.getVariantColour(pcmVariantModel, pcmVariantModel.getFeatures());
				if (sizeVariantColour != null && pcmVariantColour != null && sizeVariantColour.equalsIgnoreCase(pcmVariantColour)
						&& pcmSizeVariantModel.getSize() != null)
				{
					if (indexedProperty.isLocalized())
					{
						final Collection<LanguageModel> languages = indexConfig.getLanguages();
						for (final LanguageModel language : languages)
						{
							productUrl = getProductUrl(pcmSizeVariantModel, language);
						}
					}
					else
					{
						productUrl = getProductUrl(pcmSizeVariantModel, null);
						//productUrl.replaceAll(regex, replacement)
					}
					//	System.out.println("url" + productUrl.replace("\\", ""));
					final JSONObject urlSizeJson = new JSONObject();
					//urlSizeJson.put(pcmSizeVariantModel.getSize().toUpperCase(), productUrl.replace("\\", ""));
					urlSizeJson.put(pcmSizeVariantModel.getSize().toUpperCase(), productUrl);
					urlSizeJsonArray.add(urlSizeJson);
					//	System.out.println("url" + urlSizeJsonArray);
					//	displayUrls.add(pcmSizeVariantModel.getSize().toUpperCase() + ":" + productUrl);
				}
			}

			//final Double price = buyBoxService.buyboxPrice(pcmSizeVariantModel.getCode()).get(0).getPrice();
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			{
				//add field values
				fieldValues.addAll(createFieldValue(urlSizeJsonArray.toString(), indexedProperty));
				//System.out.println("fieldvalues" + fieldValues);
			}
			//return the field values
			return fieldValues;
		}
		else
		{

			return Collections.emptyList();
		}



		//		throw new FieldValueProviderException("Cannot evaluate rating of non-product item");
	}

	/**
	 * @return List<FieldValue>
	 * @param color
	 *           ,indexedProperty
	 * @description: It returns the size of all the variant product except the current variant
	 *
	 */
	//Create field values
	private List<FieldValue> createFieldValue(final String urlSizeJsonArray, final IndexedProperty indexedProperty)
	{
		// YTODO Auto-generated method stub
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, urlSizeJsonArray));
		}
		return fieldValues;
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
		//	this.i18nService.setCurrentLocale(this.commonI18NService.getLocaleForLanguage(language));
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

}