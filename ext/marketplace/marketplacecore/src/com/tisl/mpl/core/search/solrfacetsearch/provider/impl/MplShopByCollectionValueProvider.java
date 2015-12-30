/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
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

import com.tisl.mpl.core.model.MplShopByLookModel;


/**
 * @author TCS
 *
 */
//Index collectionId for a PcmProductVariantModel
public class MplShopByCollectionValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns the collectionId of a specific variant product
	 *
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
		//	final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();

		final List<MplShopByLookModel> shopByLookModels = (List<MplShopByLookModel>) productModel.getMplShopByLook();
		final List<String> collectionIds = new ArrayList<String>();
		if (shopByLookModels != null && shopByLookModels.size() > 0)
		{
			//Fetch occasion Id in all the Variants
			for (final MplShopByLookModel mplShopByLook : shopByLookModels)
			{

				final MplShopByLookModel shopByLookModel = mplShopByLook;

				if (shopByLookModel.getCollectionId() != null)
				{

					collectionIds.add(shopByLookModel.getCollectionId());

				}

			}


		}


		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

		{
			//add field values
			fieldValues.addAll(createFieldValue(collectionIds, indexedProperty));
		}
		//return the field values
		return fieldValues;


	}

	/**
	 * @return fieldValues
	 * @param collectionIds
	 *           ,indexedProperty
	 * @description: It create collectionIds field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final List<String> collectionIds, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, collectionIds));
		}
		return fieldValues;

	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
