package com.tisl.mpl.converter.populator;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;


public class MplDeliveryModePopulator<SOURCE extends MplZoneDeliveryModeValueModel, TARGET extends MarketplaceDeliveryModeData>
		implements Populator<SOURCE, TARGET>
{
	@Autowired
	private PriceDataFactory priceDataFactory;

	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getDeliveryMode().getCode());
		target.setName(source.getDeliveryMode().getName());
		//target.setDescription(source.getDeliveryMode().getDescription());

		//TISEE-950
		final String startValue = source.getDeliveryMode().getStart() != null ? source.getDeliveryMode().getStart().toString()
				: MarketplacecommerceservicesConstants.DEFAULT_START_TIME;

		final String endValue = source.getDeliveryMode().getEnd() != null ? source.getDeliveryMode().getEnd().toString()
				: MarketplacecommerceservicesConstants.DEFAULT_END_TIME;

		target.setDescription(getMplCommerceCartService().getDeliveryModeDescription(source.getSellerArticleSKU(),
				source.getDeliveryMode().getCode(), startValue, endValue));

		target.setSellerArticleSKU(source.getSellerArticleSKU());
		final PriceData priceData = formPriceData(source.getValue());
		target.setDeliveryCost(priceData);
	}

	public PriceData formPriceData(final Double price)
	{
		final PriceData priceData = new PriceData();
		priceData.setPriceType(PriceDataType.BUY);
		priceData.setValue(new BigDecimal(price.doubleValue()));
		priceData.setCurrencyIso(MarketplacecommerceservicesConstants.INR);
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(priceData.getCurrencyIso());
		currency.setSymbol(priceData.getCurrencyIso());
		final PriceData pData = priceDataFactory.create(PriceDataType.BUY, priceData.getValue(), currency);
		return pData;
	}

	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}
}
