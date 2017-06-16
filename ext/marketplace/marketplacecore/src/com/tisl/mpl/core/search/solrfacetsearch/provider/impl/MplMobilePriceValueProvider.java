/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.PriceService;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author TCS
 *
 */
public class MplMobilePriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private static final Logger LOG = Logger.getLogger(MplMobilePriceValueProvider.class);
	private FieldNameProvider fieldNameProvider;

	private MplPriceRowService mplPriceRowService;

	private PriceService priceService;

	private MplBuyBoxUtility mplBuyBoxUtility;

	/**
	 * @description: It returns the price range and price value of a specific product
	 * @param indexConfig
	 *           ,indexedProperty,model
	 *
	 * @return Collection<fieldValues>
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final Collection fieldValues = new ArrayList();
		try
		{
			ProductModel product = null;
			if (model instanceof ProductModel)
			{
				product = (ProductModel) model;
			}
			else
			{
				throw new FieldValueProviderException("Cannot evaluate price of non-product item");
			}

			if (indexConfig.getCurrencies().isEmpty())
			{

				final Double value = getBuyBoxMobilePrice(product);

				if (value != null && value > 0)
				{

					final CurrencyModel productCurrency = this.i18nService.getCurrentCurrency();
					if (productCurrency != null)
					{

						final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty,
								productCurrency.getIsocode().toLowerCase());
						for (final String fieldName : fieldNames)
						{

							fieldValues.add(new FieldValue(fieldName, value));
						}
					}
				}
				else
				{
					return Collections.emptyList();
				}

			}
			else
			{
				for (final CurrencyModel currency : indexConfig.getCurrencies())
				{
					final CurrencyModel sessionCurrency = this.i18nService.getCurrentCurrency();
					try
					{
						this.i18nService.setCurrentCurrency(currency);
						final Double value = getBuyBoxMobilePrice(product);
						if (value != null && value > 0)
						{
							String currencyValue = "";
							if (currency.getIsocode() != null)
							{
								currencyValue = currency.getIsocode().toLowerCase();
							}
							else
							{
								currencyValue = currency.getIsocode();
							}
							final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, currencyValue);
							for (final String fieldName : fieldNames)
							{
								fieldValues.add(new FieldValue(fieldName, value));

							}
						}
						else
						{
							return Collections.emptyList();
						}
					}
					finally
					{
						this.i18nService.setCurrentCurrency(sessionCurrency);
					}
				}

			}


		}
		catch (final Exception e)
		{
			throw new FieldValueProviderException(
					"Cannot evaluate " + indexedProperty.getName() + " using " + super.getClass().getName() + "exception" + e, e);
		}
		return fieldValues;
	}


	/**
	 * This method retrieves list of seller price for product id and choose best price(MRP) based on buybox logic
	 *
	 * @param product
	 * @return buyboxwinner price
	 */
	public Double getBuyBoxMobilePrice(final ProductModel productModel)
	{
		final Double mobileprice = mplBuyBoxUtility.getBuyBoxSellingMobilePrice(productModel);
		LOG.debug("Mobile Price is not available in Buy Box for product :>>>>>>>>>>>>>>>>>>" + mobileprice);
		if (mobileprice != null && mobileprice.intValue() <= 0)
		{
			LOG.debug("Price is not available in Buy Box for product :" + productModel.getCode());
		}

		return mobileprice;
	}


	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	/**
	 * @return the mplPriceRowService
	 */
	public MplPriceRowService getMplPriceRowService()
	{
		return mplPriceRowService;
	}

	/**
	 * @param mplPriceRowService
	 *           the mplPriceRowService to set
	 */
	@Required
	public void setMplPriceRowService(final MplPriceRowService mplPriceRowService)
	{
		this.mplPriceRowService = mplPriceRowService;
	}

	/**
	 * @return the priceService
	 */
	public PriceService getPriceService()
	{
		return priceService;
	}

	/**
	 * @param priceService
	 *           the priceService to set
	 */
	@Required
	public void setPriceService(final PriceService priceService)
	{
		this.priceService = priceService;
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
