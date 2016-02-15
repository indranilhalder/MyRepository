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

import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author TCS
 *
 */
public class MplLeastSizeProductValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;
	private MplBuyBoxUtility mplBuyBoxUtility;

	/**
	 * @Description : Return leastSizeProduct
	 * @param :
	 *           final IndexConfig indexConfig, final IndexedProperty indexedProperty,final Object model
	 * @return : Collection<FieldValue>
	 */
	//Return leastSizeProduct against each Product
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ProductModel productModel = (ProductModel) model;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		final String leastSizeProduct = mplBuyBoxUtility.getLeastSizeProduct(productModel);

		if (leastSizeProduct != null && !leastSizeProduct.isEmpty())
		{

			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			fieldValues.addAll(createFieldValue(leastSizeProduct, indexedProperty));

			return fieldValues;
		}

		return Collections.emptyList();


	}


	/**
	 * @Description : Return filed values
	 * @param :
	 *           final String leastSizeProduct, final IndexedProperty indexedProperty
	 * @return : List<FieldValue>
	 */
	//Create field value for least size product
	protected List<FieldValue> createFieldValue(final String leastSizeProduct, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = leastSizeProduct;
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