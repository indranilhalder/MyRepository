/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * @author TCS
 *
 */
public class MplOffersValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	/**
	 * @description: It returns the promotion offers of a specific product
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @return Collection<fieldValues>
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{

		final ProductModel productModel = (ProductModel) model;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		final Set<String> offersList = new HashSet<String>();

		final List<ProductPromotionModel> offers = (List<ProductPromotionModel>) productModel.getPromotions();
		final Calendar cal = Calendar.getInstance();
		for (final ProductPromotionModel offer : offers)
		{
			if (offer.getEnabled().booleanValue() && cal.getTime().before(offer.getStartDate())
					&& cal.getTime().after(offer.getEndDate()))
			{
				offersList.add(offer.getPromotionType());
			}
		}
		if (!offersList.isEmpty())
		{

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			for (final String offer : offersList)
			{
				fieldValues.addAll(createFieldValue(offer, indexedProperty));
			}


			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * @return fieldValues
	 * @param offer
	 *           ,indexedProperty
	 * @description: It create offer field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final String offer, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = offer;
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
