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
import com.tisl.mpl.util.MplBuyBoxUtility;


public class MplProductInStockFlagValueProvider extends ProductInStockFlagValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private CommerceStockService commerceStockService;
	private MplBuyBoxUtility mplBuyBoxUtility;

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


		if (model instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel product = (PcmProductVariantModel) model;
			return getFieldValues(product, indexedProperty, indexConfig);
		}
		else if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;
			return getFieldValues(product, indexedProperty, indexConfig);
		}
		return Collections.emptyList();

	}

	public Collection getFieldValues(final ProductModel productModel, final IndexedProperty indexedProperty,
			final IndexConfig indexConfig)
	{
		final Collection fieldValues = new ArrayList();

		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();

		if ((baseSiteModel != null) && (baseSiteModel.getStores() != null) && (!(baseSiteModel.getStores().isEmpty()))
				&& (getCommerceStockService().isStockSystemEnabled(baseSiteModel.getStores().get(0))))
		{
			fieldValues.addAll(createFieldValue(productModel, indexConfig.getBaseSite().getStores().get(0), indexedProperty));
		}
		else
		{
			fieldValues.addAll(createFieldValue(productModel, null, indexedProperty));
		}
		return fieldValues;
	}

	@Override
	protected List<FieldValue> createFieldValue(final ProductModel productCode, final BaseStoreModel baseStore,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		if (baseStore != null)
		{
			addFieldValues(fieldValues, indexedProperty, Boolean.valueOf(isInStock(productCode, baseStore)));
		}
		else
		{
			addFieldValues(fieldValues, indexedProperty, Boolean.valueOf(isInStock(productCode, baseStore)));
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

	@Override
	protected boolean isInStock(final ProductModel productModel, final BaseStoreModel baseStore)
	{
		if (baseStore != null)
		{
			// OOTB Base store product inventory logic is commented and Buybox is used for Inventory
			//return isInStock(getProductStockLevelStatus(product, baseStore));
			return isInStock(getBuyBoxStockLevelStatus(productModel));
		}
		else
		{
			return isInStock(getBuyBoxStockLevelStatus(productModel));
		}
	}

	protected StockLevelStatus getBuyBoxStockLevelStatus(final ProductModel productModel)
	{

		final Integer availableStock = mplBuyBoxUtility.getBuyBoxInventory(productModel);

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