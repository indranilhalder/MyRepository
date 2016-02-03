/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
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

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author 360641
 *
 */
public class MplStockLevelStatusValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
{

	private FieldNameProvider fieldNameProvider;

	private MplBuyBoxUtility mplBuyBoxUtility;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch.
	 * config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{

		if (model instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel product = (PcmProductVariantModel) model;
			return getFieldValues(product, indexedProperty);
		}
		else if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;
			return getFieldValues(product, indexedProperty);
		}
		return Collections.emptyList();
	}

	public Collection getFieldValues(final ProductModel product, final IndexedProperty indexedProperty)
	{
		final Collection fieldValues = new ArrayList();

		final Integer availableStock = mplBuyBoxUtility.getBuyBoxAvailableInventory(product);

		StockLevelStatus stockLevelStatus = StockLevelStatus.OUTOFSTOCK;

		if (availableStock != null && availableStock.intValue() > 0)
		{
			stockLevelStatus = StockLevelStatus.INSTOCK;
		}

		fieldValues.addAll(createFieldValue(stockLevelStatus, indexedProperty));

		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final StockLevelStatus stockLevelStatus, final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();

		addFieldValues(fieldValues, indexedProperty, stockLevelStatus);

		return fieldValues;
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	/**
	 * @return the fieldNameProvider
	 */
	public FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	/**
	 * @param fieldNameProvider
	 *           the fieldNameProvider to set
	 */
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
