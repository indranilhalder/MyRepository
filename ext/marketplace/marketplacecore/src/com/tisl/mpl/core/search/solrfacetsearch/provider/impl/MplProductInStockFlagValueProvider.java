/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductInStockFlagValueProvider;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;


public class MplProductInStockFlagValueProvider extends ProductInStockFlagValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private CommerceStockService commerceStockService;

	private BuyBoxService buyBoxService;

	@Override
	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Override
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	@Override
	protected CommerceStockService getCommerceStockService()
	{
		return this.commerceStockService;
	}

	@Override
	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{


		String productType = "";
		String productCode = "";
		if (model instanceof PcmProductVariantModel)
		{
			productType = "variant";
			final PcmProductVariantModel product = (PcmProductVariantModel) model;
			productCode = product.getCode();
			return getFieldValues(productCode, productType, indexedProperty, indexConfig);
		}
		else if (model instanceof ProductModel)
		{
			productType = "simple";
			final ProductModel product = (ProductModel) model;
			productCode = product.getCode();
			return getFieldValues(productCode, productType, indexedProperty, indexConfig);
		}
		return Collections.emptyList();

	}

	public Collection getFieldValues(final String productCode, final String productType, final IndexedProperty indexedProperty,
			final IndexConfig indexConfig)
	{
		final Collection fieldValues = new ArrayList();

		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();

		if ((baseSiteModel != null) && (baseSiteModel.getStores() != null) && (!(baseSiteModel.getStores().isEmpty()))
				&& (getCommerceStockService().isStockSystemEnabled(baseSiteModel.getStores().get(0))))
		{
			fieldValues
					.addAll(createFieldValue(productCode, productType, indexConfig.getBaseSite().getStores().get(0), indexedProperty));
		}
		else
		{
			fieldValues.addAll(createFieldValue(productCode, productType, null, indexedProperty));
		}
		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final String productCode, final String productType, final BaseStoreModel baseStore,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		if (baseStore != null)
		{
			addFieldValues(fieldValues, indexedProperty, Boolean.valueOf(isInStock(productCode, productType, baseStore)));
		}
		else
		{
			addFieldValues(fieldValues, indexedProperty, Boolean.valueOf(isInStock(productCode, productType, baseStore)));
		}

		return fieldValues;
	}

	@Override
	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	@Override
	protected StockLevelStatus getProductStockLevelStatus(final ProductModel product, final BaseStoreModel baseStore)
	{
		return getCommerceStockService().getStockLevelStatusForProductAndBaseStore(product, baseStore);
	}

	protected boolean isInStock(final String productCode, final String productType, final BaseStoreModel baseStore)
	{
		if (baseStore != null)
		{
			// OOTB Base store product inventory logic is commented and Buybox is used for Inventory
			//return isInStock(getProductStockLevelStatus(product, baseStore));
			return isInStock(getBuyBoxStockLevelStatus(productCode, productType));
		}
		else
		{
			return isInStock(getBuyBoxStockLevelStatus(productCode, productType));
		}
	}

	protected StockLevelStatus getBuyBoxStockLevelStatus(final String productCode, final String productType)
	{

		final Integer availableStock = buyBoxService.getBuyboxInventoryForSearch(productCode, productType);

		StockLevelStatus stockLevelStatus = StockLevelStatus.OUTOFSTOCK;

		if (availableStock != null && availableStock.intValue() > 0)
		{
			stockLevelStatus = StockLevelStatus.INSTOCK;
		}

		return stockLevelStatus;
	}

	@Override
	protected boolean isInStock(final StockLevelStatus stockLevelStatus)
	{
		return ((stockLevelStatus != null) && (!(StockLevelStatus.OUTOFSTOCK.equals(stockLevelStatus))));
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