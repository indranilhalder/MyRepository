/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.JewelleryPriceRowModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.PriceBreakupDao;
import com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService;


/**
 * @author 970506
 *
 */
public class PriceBreakupServiceImpl implements PriceBreakupService
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService#getPricebreakup(java.lang.String,
	 * java.lang.String)
	 */
	protected static final Logger LOG = Logger.getLogger(PriceBreakupServiceImpl.class);

	private Map<String, String> jewelleryAttrMapping;

	private static final String BASEMETAL1 = "baseMetal1";
	private static final String BASEMETAL2 = "baseMetal2";
	private static final String BASEMETAL3 = "baseMetal3";
	private static final String BASEMETAL4 = "baseMetal4";
	private static final String DIAMOND = "diamond";
	private static final String GEMSTONE = "gemstone";
	private static final String WASTAGETAX = "wastagetax";
	private static final String MAKINGCHARGE = "makingcharge";

	public static final String INR = "INR";

	@Resource(name = "priceBreakupDao")
	private PriceBreakupDao priceBreakupDao;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CommonI18NService commonI18NService;


	@Override
	//public List<JewelleryPriceRowModel> getPricebreakup(final String productCode, final String ussid)
	public LinkedHashMap<String, PriceData> getPricebreakup(final String productCode, final String ussid)
	{

		List<JewelleryPriceRowModel> jewelleryPriceRowList = new ArrayList<JewelleryPriceRowModel>();

		jewelleryPriceRowList = priceBreakupDao.getPricebreakup(productCode, ussid);

		final LinkedHashMap<String, PriceData> PriceMap = new LinkedHashMap<String, PriceData>();

		String key = "";

		for (final JewelleryPriceRowModel jewellery : jewelleryPriceRowList)
		{
			if (jewellery.getBaseMetalPrice1() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(BASEMETAL1))
				{
					key = getJewelleryAttrMapping().get(BASEMETAL1);
				}

				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getBaseMetalPrice1()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getBaseMetalPrice2() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(BASEMETAL2))
				{
					key = getJewelleryAttrMapping().get(BASEMETAL2);
				}

				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getBaseMetalPrice2()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getBaseMetalPrice3() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(BASEMETAL3))
				{
					key = getJewelleryAttrMapping().get(BASEMETAL3);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getBaseMetalPrice3()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getBaseMetalPrice4() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(BASEMETAL4))
				{
					key = getJewelleryAttrMapping().get(BASEMETAL4);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getBaseMetalPrice4()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}

			if (jewellery.getDiamondtotalprice() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(DIAMOND))
				{
					key = getJewelleryAttrMapping().get(DIAMOND);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getDiamondtotalprice()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}

			if (jewellery.getGemstonetotalprice() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(GEMSTONE))
				{
					key = getJewelleryAttrMapping().get(GEMSTONE);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getGemstonetotalprice()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}

			if (jewellery.getMakingCharge() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(MAKINGCHARGE))
				{
					key = getJewelleryAttrMapping().get(MAKINGCHARGE);
				}

				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getMakingCharge().doubleValue()),
						commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getWastageTax() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(WASTAGETAX))
				{
					key = getJewelleryAttrMapping().get(WASTAGETAX);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getWastageTax().doubleValue()),
						commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
		}


		return PriceMap;

	}

	public PriceData createPriceSign(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
	{

		final PriceData priceData = new PriceData();

		priceData.setPriceType(priceType);
		priceData.setValue(value);
		priceData.setDoubleValue(Double.valueOf(value.doubleValue()));
		final String currencyIsoCode = currency.getIsocode();
		priceData.setCurrencyIso(currencyIsoCode);

		final String decimalFormat = configurationService.getConfiguration().getString("site.decimal.format", "0.00");

		final String currencySymbol = currency.getSymbol();

		final DecimalFormat df = new DecimalFormat(decimalFormat);
		final String totalPriceFormatted = df.format(value);
		StringBuilder stb = new StringBuilder(20);
		stb = stb.append(currencySymbol).append(totalPriceFormatted);
		priceData.setFormattedValue(stb.toString());

		return priceData;
	}


	/**
	 * @return the priceBreakupDao
	 */
	public PriceBreakupDao getPriceBreakupDao()
	{
		return priceBreakupDao;
	}


	/**
	 * @param priceBreakupDao
	 *           the priceBreakupDao to set
	 */
	public void setPriceBreakupDao(final PriceBreakupDao priceBreakupDao)
	{
		this.priceBreakupDao = priceBreakupDao;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the jewelleryAttrMapping
	 */
	public Map<String, String> getJewelleryAttrMapping()
	{
		return jewelleryAttrMapping;
	}

	/**
	 * @param jewelleryAttrMapping
	 *           the jewelleryAttrMapping to set
	 */
	public void setJewelleryAttrMapping(final Map<String, String> jewelleryAttrMapping)
	{
		this.jewelleryAttrMapping = jewelleryAttrMapping;
	}

}
