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
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
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
		final SortedSet<String> ussids = new TreeSet<String>();
		String sellerArticleSku = null;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productModel.getCode());

		Double priceVal = Double.valueOf(0.0);
		if (CollectionUtils.isNotEmpty(buyBoxModelList))
		{
			final BuyBoxModel buyBoxWinnerModel = buyBoxModelList.get(0);
			sellerArticleSku = buyBoxWinnerModel.getSellerArticleSKU();
			for (final BuyBoxModel buyBox : buyBoxModelList)
			{
				if (buyBox.getAvailable().intValue() > 0)
				{
					//sellerArticleSku = buyBox.getSellerArticleSKU();
					priceVal = getBuyBoxPrice(buyBox);
					ussids.add(buyBox.getSellerArticleSKU() + ":" + buyBox.getMrp() + ":" + priceVal.toString());
				}
			}
			if (CollectionUtils.isNotEmpty(ussids) && buyBoxWinnerModel.getAvailable().intValue() <= 0)
			{
				Double mrpVal = Double.valueOf(0.0);
				if (null != buyBoxWinnerModel.getMrp())
				{
					mrpVal = buyBoxWinnerModel.getMrp();
				}
				sellerArticleSku = ussids.first() + ":" + mrpVal + ":" + priceVal;
			}
		}

		if (sellerArticleSku != null && CollectionUtils.isNotEmpty(ussids))
		{
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				fieldValues.addAll(createFieldValue(ussids, indexedProperty));
			}
			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	/**
	 * @return List<FieldValue>
	 * @param ussids
	 *           ,indexedProperty
	 * @description: It returns the size of all the variant product except the current variant
	 *
	 */
	protected List<FieldValue> createFieldValue(final Set<String> ussids, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, ussids));
		}
		return fieldValues;
	}


	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	private Double getBuyBoxPrice(final BuyBoxModel buybox) throws EtailNonBusinessExceptions
	{
		Double price = Double.valueOf(0);
		if (buybox != null)
		{
			price = buybox.getPrice();

			if (null != buybox.getSpecialPrice() && buybox.getSpecialPrice().intValue() > 0)
			{
				price = buybox.getSpecialPrice();
			}
			else if (null != buybox.getPrice() && buybox.getPrice().intValue() > 0)
			{
				price = buybox.getPrice();
			}
			else
			{
				price = buybox.getMrp();
			}
		}

		return price;
	}
}