/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

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
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;


/**
 * @author 361234
 *
 */
public class MplOffersExistingValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch
	 * .config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns the color of all the variant product except the current variant
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{


		boolean offersExist = false;
		if (model instanceof PcmProductVariantModel)
		{
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel productModel = (PcmProductVariantModel) model;

			final List<ProductPromotionModel> offers = (List<ProductPromotionModel>) productModel.getPromotions();

			if (offers != null && offers.size() > 0)
			{
				final Calendar cal = Calendar.getInstance();
				for (final ProductPromotionModel productPromotion : offers)
				{
					if (productPromotion.getEnabled().booleanValue() && productPromotion.getStartDate().before(cal.getTime())
							&& productPromotion.getEndDate().after(cal.getTime()))
					{
						offersExist = true;
						break;
					}
				}


				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				{
					//add field values
					fieldValues.addAll(createFieldValue(offersExist, indexedProperty));
				}
				//return the field values
				return fieldValues;


			}
			else
			{
				return Collections.emptyList();
			}

		}
		else
		{
			return Collections.emptyList();
		}




	}

	/**
	 * @return List<FieldValue>
	 * @param offersExist
	 *           ,indexedProperty
	 * @description: It returns the if the Products has offers
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final boolean offersExist, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, offersExist));
		}
		return fieldValues;
	}

	/**
	 * @return void
	 * @param fieldNameProvider
	 * @description: Set Field name provider
	 *
	 */
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
