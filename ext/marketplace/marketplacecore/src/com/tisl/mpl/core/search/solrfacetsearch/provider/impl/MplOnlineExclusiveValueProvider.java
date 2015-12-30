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

import com.tisl.mpl.enums.OnlineExclusiveEnum;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
//Index onlineExclusive for a PcmProductVariantModel
public class MplOnlineExclusiveValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	//	@Autowired
	//	private ConfigurationService configurationService;

	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns if a specific variant product is new or not
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

		boolean isOnlineExclusive = false;
		final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{

			if (seller != null)
			{

				final OnlineExclusiveEnum onlineExclusiveEnum = seller.getOnlineExclusive();
				if (null != onlineExclusiveEnum)
				{
					isOnlineExclusive = true;
					break;
				}

			}

		}
		fieldValues.addAll(createFieldValue(isOnlineExclusive, indexedProperty));

		//return the field values
		return fieldValues;

	}


	/**
	 * @return fieldValues
	 * @param isOnlineExclusive
	 *           ,indexedProperty
	 * @description: It create isProductNew field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final boolean isOnlineExclusive, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = isOnlineExclusive;
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
