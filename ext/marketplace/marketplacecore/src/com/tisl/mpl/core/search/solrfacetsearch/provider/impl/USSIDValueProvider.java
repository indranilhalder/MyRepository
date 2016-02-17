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

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;


/**
 * @author TCS
 *
 */
public class USSIDValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	private BuyBoxService buyBoxService;


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


	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns the seller Id of a specific product
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{

		final ProductModel productModel = (ProductModel) model;
		String sellerArticleSku = null;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productModel.getCode());

		//if (buyBoxModelList != null && buyBoxModelList.size() > 0)
		if (CollectionUtils.isNotEmpty(buyBoxModelList))
		{
			BuyBoxModel buyBoxWinnerModel = buyBoxModelList.get(0);

			for (final BuyBoxModel buyBox : buyBoxModelList)
			{
				if (buyBox.getAvailable().intValue() > 0)
				{
					buyBoxWinnerModel = buyBox;
					break;
				}
			}

			sellerArticleSku = buyBoxWinnerModel.getSellerArticleSKU();


		}

		if (sellerArticleSku != null)
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				fieldValues.addAll(createFieldValue(sellerArticleSku, indexedProperty));
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
