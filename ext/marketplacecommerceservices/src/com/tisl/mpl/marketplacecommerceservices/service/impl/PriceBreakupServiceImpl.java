/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.JewelleryPriceRowModel;
import de.hybris.platform.core.model.OrderJewelEntryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.PriceBreakupDao;
import com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService;



/**
 * @author Tcs
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

	private static final String GOLDVALUE = "goldValue";
	private static final String SILVERVALUE = "silverValue";
	private static final String PLATINUMVALUE = "platinumValue";
	private static final String SOLITAIREVALUE = "solitaireValue";
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

	@Autowired
	private ModelService modelService;


	@Override
	//public List<JewelleryPriceRowModel> getPricebreakup(final String productCode, final String ussid)
	public LinkedHashMap<String, PriceData> getPricebreakup(final String ussid)
	{

		//List<JewelleryPriceRowModel> jewelleryPriceRowList = new ArrayList<JewelleryPriceRowModel>();

		final List<JewelleryPriceRowModel> jewelleryPriceRowList = priceBreakupDao.getPricebreakup(ussid);

		final LinkedHashMap<String, PriceData> PriceMap = new LinkedHashMap<String, PriceData>();

		String key = "";

		for (final JewelleryPriceRowModel jewellery : jewelleryPriceRowList)
		{
			if (jewellery.getGoldValue() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(GOLDVALUE))
				{
					key = getJewelleryAttrMapping().get(GOLDVALUE);
				}

				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getGoldValue().doubleValue()),
						commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getSilverValue() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(SILVERVALUE))
				{
					key = getJewelleryAttrMapping().get(SILVERVALUE);
				}

				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getSilverValue().doubleValue()),
						commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getPlatinumValue() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(PLATINUMVALUE))
				{
					key = getJewelleryAttrMapping().get(PLATINUMVALUE);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY,
						new BigDecimal(jewellery.getPlatinumValue().doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getSolitaireValue() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(SOLITAIREVALUE))
				{
					key = getJewelleryAttrMapping().get(SOLITAIREVALUE);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY,
						new BigDecimal(jewellery.getSolitaireValue().doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}

			if (jewellery.getTotalDiamondValue() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(DIAMOND))
				{
					key = getJewelleryAttrMapping().get(DIAMOND);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getTotalDiamondValue()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}

			if (jewellery.getTotalStoneValue() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(GEMSTONE))
				{
					key = getJewelleryAttrMapping().get(GEMSTONE);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jewellery.getTotalStoneValue()
						.doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}

			if (jewellery.getMakingCharges() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(MAKINGCHARGE))
				{
					key = getJewelleryAttrMapping().get(MAKINGCHARGE);
				}

				final PriceData price = createPriceSign(PriceDataType.BUY,
						new BigDecimal(jewellery.getMakingCharges().doubleValue()), commonI18NService.getCurrency(INR));

				PriceMap.put(key, price);
			}
			if (jewellery.getWastageCharges() != null)
			{
				if (getJewelleryAttrMapping() != null && getJewelleryAttrMapping().containsKey(WASTAGETAX))
				{
					key = getJewelleryAttrMapping().get(WASTAGETAX);
				}


				final PriceData price = createPriceSign(PriceDataType.BUY,
						new BigDecimal(jewellery.getWastageCharges().doubleValue()), commonI18NService.getCurrency(INR));

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

	//Added for 3782
	@Override
	public boolean createPricebreakupOrder(final AbstractOrderEntryModel entry)
	{
		boolean returnFlag = false;
		try
		{
			final List<JewelleryPriceRowModel> jewelleryPriceRow = priceBreakupDao.getPricebreakup(entry.getSelectedUSSID());
			final JewelleryPriceRowModel jewelleryModel = jewelleryPriceRow.get(0);
			final OrderJewelEntryModel orderJewelEntryModel = modelService.create(OrderJewelEntryModel.class);

			orderJewelEntryModel.setBaseMetalPrice1(jewelleryModel.getGoldValue());
			orderJewelEntryModel.setBaseMetalPrice2(jewelleryModel.getSilverValue());
			orderJewelEntryModel.setBaseMetalPrice3(jewelleryModel.getPlatinumValue());
			orderJewelEntryModel.setBaseMetalPrice4(jewelleryModel.getSolitaireValue());
			orderJewelEntryModel.setBaseMetalRate1(jewelleryModel.getGoldRate());
			orderJewelEntryModel.setBaseMetalRate2(jewelleryModel.getSilverRate());
			orderJewelEntryModel.setBaseMetalRate3(jewelleryModel.getPlatinumRate());
			orderJewelEntryModel.setBaseMetalRate4(jewelleryModel.getSolitaireRate());
			orderJewelEntryModel.setDiamondPrice1(jewelleryModel.getDiamondValueType1());
			orderJewelEntryModel.setDiamondPrice2(jewelleryModel.getDiamondValueType2());
			orderJewelEntryModel.setDiamondPrice3(jewelleryModel.getDiamondValueType3());
			orderJewelEntryModel.setDiamondPrice4(jewelleryModel.getDiamondValueType4());
			orderJewelEntryModel.setDiamondPrice5(jewelleryModel.getDiamondValueType5());
			orderJewelEntryModel.setDiamondPrice6(jewelleryModel.getDiamondValueType6());
			orderJewelEntryModel.setDiamondPrice7(jewelleryModel.getDiamondValueType7());
			orderJewelEntryModel.setDiamondtotalprice(jewelleryModel.getTotalDiamondValue());
			orderJewelEntryModel.setGemStonePrice1(jewelleryModel.getStoneValueType1());
			orderJewelEntryModel.setGemStonePrice2(jewelleryModel.getStoneValueType2());
			orderJewelEntryModel.setGemStonePrice3(jewelleryModel.getStoneValueType3());
			orderJewelEntryModel.setGemStonePrice4(jewelleryModel.getStoneValueType4());
			orderJewelEntryModel.setGemStonePrice5(jewelleryModel.getStoneValueType5());
			orderJewelEntryModel.setGemStonePrice6(jewelleryModel.getStoneValueType6());
			orderJewelEntryModel.setGemStonePrice7(jewelleryModel.getStoneValueType7());
			orderJewelEntryModel.setGemStonePrice8(jewelleryModel.getStoneValueType8());
			orderJewelEntryModel.setGemStonePrice9(jewelleryModel.getStoneValueType9());
			orderJewelEntryModel.setGemStonePrice10(jewelleryModel.getStoneValueType10());
			orderJewelEntryModel.setGemstonetotalprice(jewelleryModel.getTotalStoneValue());
			orderJewelEntryModel.setMakingCharge(jewelleryModel.getMakingCharges());
			orderJewelEntryModel.setWastageTax(jewelleryModel.getWastageCharges());

			//abstractOrderEntryModel.getOrderJewelEntry();
			orderJewelEntryModel.setAbstractOrderEntryjewel(entry);
			entry.setOrderJewelEntry(orderJewelEntryModel);
			modelService.saveAll(orderJewelEntryModel, entry);
			returnFlag = true;
		}
		catch (final Exception e)
		{
			LOG.debug("Exception while Price Breakup Create" + e.getMessage());
			returnFlag = false;
		}
		return returnFlag;

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
