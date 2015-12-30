/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.ProductPromotionModel;
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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;


public class MplMobilePromotionCodeValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;
	private PromotionsService promotionService;

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected PromotionsService getPromotionsService()
	{
		return this.promotionService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionService)
	{
		this.promotionService = promotionService;
	}

	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel product = (PcmProductVariantModel) model;

			final Collection fieldValues = new ArrayList();

			if (indexedProperty.isMultiValue())
			{
				fieldValues.addAll(createFieldValues(product, indexConfig, indexedProperty));
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, indexConfig, indexedProperty));
			}
			return fieldValues;
		}
		else if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final Collection fieldValues = new ArrayList();

			if (indexedProperty.isMultiValue())
			{
				fieldValues.addAll(createFieldValues(product, indexConfig, indexedProperty));
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, indexConfig, indexedProperty));
			}
			return fieldValues;
		}
		throw new FieldValueProviderException("Cannot get promotion codes of non-product item");
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final Iterator localIterator = getPromotionsService()
					.getProductPromotions(Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product).iterator();
			if (localIterator.hasNext())
			{
				final ProductPromotionModel promotion = (ProductPromotionModel) localIterator.next();

				addFieldValues(fieldValues, indexedProperty, null, promotion.getCode());
			}
		}

		return fieldValues;
	}


	protected List<FieldValue> createFieldValue(final PcmProductVariantModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final Iterator localIterator = getPromotionsService()
					.getProductPromotions(Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product).iterator();
			if (localIterator.hasNext())
			{
				final ProductPromotionModel promotion = (ProductPromotionModel) localIterator.next();

				addFieldValues(fieldValues, indexedProperty, null, promotion.getCode());
			}
		}

		return fieldValues;
	}

	protected List<FieldValue> createFieldValues(final PcmProductVariantModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final List<ProductPromotionModel> promotions = new ArrayList<ProductPromotionModel>();

			for (final ProductPromotionModel promotion : getPromotionsService()
					.getProductPromotions(Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product))
			{
				if (promotion.getChannel().contains(SalesApplication.MOBILE) || promotion.getChannel().isEmpty())
				{

					promotions.add(promotion);
				}

			}

			if (!promotions.isEmpty())
			{

				Collections.sort(promotions, new Comparator<ProductPromotionModel>()
				{

					@Override
					public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
					{

						if (o1.getPriority() == o2.getPriority())
						{
							return 0;
						}
						else if (o1.getPriority() < o2.getPriority())
						{
							return 1;
						}
						else
						{
							return -1;
						}

					}
				});
			}


			if (promotions.size() > 0 && !promotions.isEmpty())

			{
				addFieldValues(fieldValues, indexedProperty, null, promotions.get(0).getCode());

			}

		}
		return fieldValues;
	}



	protected List<FieldValue> createFieldValues(final ProductModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final List<ProductPromotionModel> promotions = new ArrayList<ProductPromotionModel>();

			for (final ProductPromotionModel promotion : getPromotionsService()
					.getProductPromotions(Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product))
			{
				if (promotion.getChannel().contains(SalesApplication.MOBILE) || promotion.getChannel().isEmpty())
				{
					promotions.add(promotion);
				}
			}

			if (!promotions.isEmpty())
			{

				Collections.sort(promotions, new Comparator<ProductPromotionModel>()
				{

					@Override
					public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
					{

						if (o1.getPriority() == o2.getPriority())
						{
							return 0;
						}
						else if (o1.getPriority() < o2.getPriority())
						{
							return 1;
						}
						else
						{
							return -1;
						}

					}
				});
			}


			if (promotions.size() > 0 && !promotions.isEmpty())

			{
				addFieldValues(fieldValues, indexedProperty, null, promotions.get(0).getCode());

			}

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


	/*
	 * public static class PromotionComparator implements Comparator {
	 *
	 *
	 * public int compare(final Object arg0, final Object arg1) { // YTODO Auto-generated method stub final Integer
	 * number1 = promo1.getPriority(); final Integer number2 = promo2.getPriority();
	 *
	 * if (number1 != null && number2 != null) { return Integer.compare(number1, number2);
	 *
	 * } }
	 *
	 * }
	 */


}