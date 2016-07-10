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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class MplSellerValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;


	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns the seller name of a specific product
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

		final Set<String> uniqueSellerList = new HashSet<String>();
		final List<SellerInformationModel> sellers = (List<SellerInformationModel>) productModel.getSellerInformationRelator();
		for (final SellerInformationModel seller : sellers)
		{
			uniqueSellerList.add(seller.getSellerName());
		}
		if (!uniqueSellerList.isEmpty())
		{

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			for (final String seller : uniqueSellerList)
			{
				fieldValues.addAll(createFieldValue(seller, indexedProperty));
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
	 * @param seller
	 *           ,indexedProperty
	 * @description: It create seller field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final String seller, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = seller;
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
