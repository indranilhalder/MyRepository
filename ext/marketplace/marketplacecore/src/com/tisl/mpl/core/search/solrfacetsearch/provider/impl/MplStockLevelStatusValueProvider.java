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

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;


/**
 * @author tcs
 *
 */
public class MplStockLevelStatusValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
{

	private FieldNameProvider fieldNameProvider;

	private BuyBoxService buyBoxService;
	private static final String PRODUCT_CATEGORY_TYPE_FINE = "FineJewellery";
	private static final String PRODUCT_CATEGORY_TYPE_FASHION = "FashionJewellery";

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
		//String productType = "";
		//String productCode = "";
		String productType = null;
		String productCode = null;
		try
		{
			if (model instanceof PcmProductVariantModel)
			{
				final PcmProductVariantModel product = (PcmProductVariantModel) model;
				//productType will set jewelleryVariant for Jewellery otherwise variant
				if ((product.getProductCategoryType().equalsIgnoreCase(PRODUCT_CATEGORY_TYPE_FINE))
						|| (product.getProductCategoryType().equalsIgnoreCase(PRODUCT_CATEGORY_TYPE_FASHION)))
				{
					productType = "jewelleryVariant";
				}
				else
				{
					productType = "variant";
				}
				productCode = product.getCode();
				return getFieldValues(productCode, productType, indexedProperty);
			}
			else if (model instanceof ProductModel)
			{
				productType = "simple";
				final ProductModel product = (ProductModel) model;
				productCode = product.getCode();
				return getFieldValues(productCode, productType, indexedProperty);
			}
			return Collections.emptyList();
		}
		catch (final Exception e) /* added part of value provider go through */
		{
			throw new FieldValueProviderException(
					"Cannot evaluate " + indexedProperty.getName() + " using " + super.getClass().getName() + "exception" + e, e);
		}

	}

	public Collection getFieldValues(final String productCode, final String productType, final IndexedProperty indexedProperty)
	{
		boolean stockStatus = false;
		final Collection fieldValues = new ArrayList();

		final Integer availableStock = buyBoxService.getBuyboxInventoryForSearch(productCode, productType);

		//StockLevelStatus stockLevelStatus = StockLevelStatus.OUTOFSTOCK;

		if (availableStock != null && availableStock.intValue() > 0)
		{
			stockStatus = true;
		}

		fieldValues.addAll(createFieldValue(stockStatus, indexedProperty));

		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final boolean stockLevelStatus, final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();

		addFieldValues(fieldValues, indexedProperty, Boolean.valueOf(stockLevelStatus));

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
	 * @return the buyBoxService
	 */
	public BuyBoxService getBuyBoxService()
	{
		return buyBoxService;
	}

	/**
	 * @param buyBoxService
	 *           the buyBoxService to set
	 */
	public void setBuyBoxService(final BuyBoxService buyBoxService)
	{
		this.buyBoxService = buyBoxService;
	}


}
