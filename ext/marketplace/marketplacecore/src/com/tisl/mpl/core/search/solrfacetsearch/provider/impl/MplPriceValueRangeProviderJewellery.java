/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;


import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
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

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.MplBuyBoxUtility;
import com.tisl.mpl.util.MplJewelleryUtility;


/**
 * @author TCS
 *
 */
public class MplPriceValueRangeProviderJewellery extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{

	private static final String FINE_JEWELLERY = "FineJewellery";

	private FieldNameProvider fieldNameProvider;
	private MplBuyBoxUtility mplBuyBoxUtility;
	private MplJewelleryUtility mplJewelleryUtility;

	/**
	 * @return the mplJewelleryUtility
	 */
	public MplJewelleryUtility getMplJewelleryUtility()
	{
		return mplJewelleryUtility;
	}

	/**
	 * @param mplJewelleryUtility
	 *           the mplJewelleryUtility to set
	 */
	public void setMplJewelleryUtility(final MplJewelleryUtility mplJewelleryUtility)
	{
		this.mplJewelleryUtility = mplJewelleryUtility;
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
	@Resource
	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
	{
		this.mplBuyBoxUtility = mplBuyBoxUtility;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch.
	 * config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException, EtailNonBusinessExceptions
	{
		String currencySymbol = null;

		if (model instanceof ProductModel)
		{
			//Model should be instance of PcmProductVariantModel
			final ProductModel product = (ProductModel) model;
			String value = null;

			if (product.getProductCategoryType().equalsIgnoreCase(FINE_JEWELLERY))
			{
				final ArrayList<CurrencyModel> currencyList = new ArrayList<CurrencyModel>(indexConfig.getCurrencies());
				if (CollectionUtils.isNotEmpty(currencyList))
				{
					currencySymbol = currencyList.get(0).getSymbol();
				}
				value = getBuyBoxPrice(product, currencySymbol);
			}

			if (value != null && !value.isEmpty())
			{

				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				fieldValues.addAll(createFieldValue(value, indexedProperty));

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


	public String getBuyBoxPrice(final ProductModel productModel, final String currencySymbol)
	{
		final List<String> SellerArticleSKUList = new ArrayList();
		String priceRange = null;

		final List<JewelleryInformationModel> jewelleryInformationList = mplJewelleryUtility
				.getJewelleryInformationList(productModel.getCode());

		for (final JewelleryInformationModel sellerInfoList : jewelleryInformationList)
		{
			SellerArticleSKUList.add(sellerInfoList.getUSSID());
		}
		if (CollectionUtils.isNotEmpty(SellerArticleSKUList))
		{
			priceRange = mplBuyBoxUtility.getBuyBoxSellingVariantsPrice(SellerArticleSKUList, currencySymbol);
		}
		return priceRange;

	}

	/**
	 * @return List<FieldValue>
	 * @param value
	 * @param indexedProperty
	 * @description: It creates field values
	 *
	 */

	protected List<FieldValue> createFieldValue(final String value, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object values = value;
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, values));
		}
		return fieldValues;
	}

}