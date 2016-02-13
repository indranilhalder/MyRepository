/**
 *
 */
package com.tisl.mpl.facades.order.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


/**
 * @author TCS
 *
 */
public class MplDefaultPriceDataFactory extends DefaultPriceDataFactory
{

	@Autowired
	private CommonI18NService commonI18NService;

	/*
	 * @Autowired private SiteConfigService siteConfigService;
	 */

	@Autowired
	private ConfigurationService configurationService;




	@Override
	public PriceData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
	{
		Assert.notNull(priceType, "Parameter priceType cannot be null.");
		Assert.notNull(value, "Parameter value cannot be null.");
		Assert.notNull(currency, "Parameter currency cannot be null.");

		final PriceData priceData = new PriceData();

		priceData.setPriceType(priceType);
		priceData.setValue(value);
		priceData.setDoubleValue(Double.valueOf(value.doubleValue()));
		final String currencyIsoCode = currency.getIsocode();
		priceData.setCurrencyIso(currencyIsoCode);

		//for Sales report its creating issue
		//final String decimalFormat = siteConfigService.getString("site.decimal.format", "0.00");
		final String decimalFormat = configurationService.getConfiguration().getString("site.decimal.format", "0.00");

		final String currencySymbol = currency.getSymbol();

		final DecimalFormat df = new DecimalFormat(decimalFormat);
		final String totalPriceFormatted = df.format(value);
		StringBuilder stb = new StringBuilder(20);
		stb = stb.append(currencySymbol).append(totalPriceFormatted);
		priceData.setFormattedValue(stb.toString());

		return priceData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commercefacades.product.PriceDataFactory#create(de.hybris.platform.commercefacades.product.
	 * data.PriceDataType, java.math.BigDecimal, java.lang.String)
	 */
	@Override
	public PriceData create(final PriceDataType priceType, final BigDecimal value, final String currencyIso)
	{
		final CurrencyModel currency = commonI18NService.getCurrency(currencyIso);
		return create(priceType, value, currency);
	}
}
