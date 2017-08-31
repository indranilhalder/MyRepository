/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.JewelleryPriceRowModel;
import de.hybris.platform.core.model.JewellerySellerDetailsModel;
import de.hybris.platform.core.model.OrderJewelEntryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.PriceBreakupData;
import com.tisl.mpl.marketplacecommerceservices.daos.MplJewelleryDao;
import com.tisl.mpl.marketplacecommerceservices.daos.PriceBreakupDao;
import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
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

	public static final String INR = "INR";

	@Resource(name = "priceBreakupDao")
	private PriceBreakupDao priceBreakupDao;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private MplProductDao productDao;

	@Resource(name = "productJwelConverter")
	private Converter<ProductModel, ProductData> productConverter;
	@Resource(name = "productConfiguredPopulator")
	private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

	@Resource(name = "mplJewelleryDao")
	private MplJewelleryDao mplJewelleryDao;

	@Override
	public List<PriceBreakupData> getPricebreakup(final String ussid, final String sellerId)
	{
		JewelleryPriceRowModel jPrice = null;

		JewellerySellerDetailsModel jSellerDetails = null;

		final List<JewelleryPriceRowModel> jewelleryPriceRowList = priceBreakupDao.getPricebreakup(ussid);
		if (CollectionUtils.isNotEmpty(jewelleryPriceRowList))
		{
			jPrice = priceBreakupDao.getPricebreakup(ussid).get(0);
		}
		final List<JewelleryInformationModel> jewelleryInfoList = priceBreakupDao.getJewelInfo(ussid);

		final List<JewellerySellerDetailsModel> jSellerDetailsList = mplJewelleryDao.getSellerMsgForRetRefTab(sellerId);

		if (CollectionUtils.isNotEmpty(jSellerDetailsList))
		{
			jSellerDetails = jSellerDetailsList.get(0);
		}

		PriceBreakupData priceBreakupData = null;
		List<String> weightRateList = null;
		final List<PriceBreakupData> priceBreakupDataList = new ArrayList<>();
		String key = "";
		final List<String> diamondWeightRateList = new ArrayList();
		final PriceBreakupData diamondPriceData = new PriceBreakupData();

		if (CollectionUtils.isNotEmpty(jewelleryInfoList) && null != jPrice)
		{
			for (final JewelleryInformationModel jInfo : jewelleryInfoList)
			{
				if (null != jInfo.getPIMAttributeId())
				{
					if (jInfo.getPIMAttributeId().equalsIgnoreCase(MarketplacecommerceservicesConstants.METALWEIGHTFINEJEWELLERY))
					{
						if (null != jPrice.getGoldValue() && null != jPrice.getGoldRate() && null != jInfo.getWeight()
								&& jPrice.getGoldValue().doubleValue() > 0 && jPrice.getGoldRate().doubleValue() > 0)
						{
							priceBreakupData = new PriceBreakupData();
							weightRateList = new ArrayList();

							priceBreakupData.setName(MarketplacecommerceservicesConstants.GOLD);
							key = jInfo.getWeight() + MarketplacecommerceservicesConstants.GM
									+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
									+ MarketplacecommerceservicesConstants.SPACE
									+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getGoldRate().doubleValue()),
											commonI18NService.getCurrency(INR)).getFormattedValue()
									+ MarketplacecommerceservicesConstants.FRONTSLASH;
							if (null != jSellerDetails)
							{
								if (StringUtils.isNotEmpty(jSellerDetails.getMetalRateUnitGold()))
								{
									key = key + jSellerDetails.getMetalRateUnitGold();
								}
								else
								{
									key = key + MarketplacecommerceservicesConstants.GM;
								}
							}
							else
							{
								key = key + MarketplacecommerceservicesConstants.GM;
							}
							weightRateList.add(key);
							final PriceData price = createPriceSign(PriceDataType.BUY,
									new BigDecimal(jPrice.getGoldValue().doubleValue()), commonI18NService.getCurrency(INR));
							priceBreakupData.setWeightRateList(weightRateList);
							priceBreakupData.setPrice(price);

							priceBreakupDataList.add(priceBreakupData);
						}
						if (null != jPrice.getSilverValue() && null != jPrice.getSilverRate() && null != jInfo.getWeight()
								&& jPrice.getSilverValue().doubleValue() > 0 && jPrice.getSilverRate().doubleValue() > 0)
						{
							priceBreakupData = new PriceBreakupData();
							weightRateList = new ArrayList();

							priceBreakupData.setName(MarketplacecommerceservicesConstants.SILVER);
							key = jInfo.getWeight() + MarketplacecommerceservicesConstants.GM
									+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
									+ MarketplacecommerceservicesConstants.SPACE
									+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getSilverRate().doubleValue()),
											commonI18NService.getCurrency(INR)).getFormattedValue()
									+ MarketplacecommerceservicesConstants.FRONTSLASH;
							if (null != jSellerDetails)
							{
								if (StringUtils.isNotEmpty(jSellerDetails.getMetalRateUnitSil()))
								{
									key = key + jSellerDetails.getMetalRateUnitSil();
								}
								else
								{
									key = key + MarketplacecommerceservicesConstants.KG;
								}
							}
							else
							{
								key = key + MarketplacecommerceservicesConstants.KG;
							}
							weightRateList.add(key);
							final PriceData price = createPriceSign(PriceDataType.BUY,
									new BigDecimal(jPrice.getSilverValue().doubleValue()), commonI18NService.getCurrency(INR));
							priceBreakupData.setWeightRateList(weightRateList);
							priceBreakupData.setPrice(price);

							priceBreakupDataList.add(priceBreakupData);
						}
						if (null != jPrice.getPlatinumValue() && null != jPrice.getPlatinumRate() && null != jInfo.getWeight()
								&& jPrice.getPlatinumValue().doubleValue() > 0 && jPrice.getPlatinumRate().doubleValue() > 0)
						{
							priceBreakupData = new PriceBreakupData();
							weightRateList = new ArrayList();

							priceBreakupData.setName(MarketplacecommerceservicesConstants.PLATINUM);
							key = jInfo.getWeight() + MarketplacecommerceservicesConstants.GM
									+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
									+ MarketplacecommerceservicesConstants.SPACE
									+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getPlatinumRate().doubleValue()),
											commonI18NService.getCurrency(INR)).getFormattedValue()
									+ MarketplacecommerceservicesConstants.FRONTSLASH;
							if (null != jSellerDetails)
							{
								if (StringUtils.isNotEmpty(jSellerDetails.getMetalRateUnitPlat()))
								{
									key = key + jSellerDetails.getMetalRateUnitPlat();
								}
								else
								{
									key = key + MarketplacecommerceservicesConstants.GM;
								}
							}
							else
							{
								key = key + MarketplacecommerceservicesConstants.GM;
							}
							weightRateList.add(key);
							final PriceData price = createPriceSign(PriceDataType.BUY,
									new BigDecimal(jPrice.getPlatinumValue().doubleValue()), commonI18NService.getCurrency(INR));
							priceBreakupData.setWeightRateList(weightRateList);
							priceBreakupData.setPrice(price);

							priceBreakupDataList.add(priceBreakupData);
						}
						if (null != jPrice.getSolitaireValue() && null != jPrice.getSolitaireRate() && null != jInfo.getWeight()
								&& jPrice.getSolitaireValue().doubleValue() > 0 && jPrice.getSolitaireRate().doubleValue() > 0)
						{
							priceBreakupData = new PriceBreakupData();
							weightRateList = new ArrayList();

							priceBreakupData.setName(MarketplacecommerceservicesConstants.SOLITAIRE);
							key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
									+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
									+ MarketplacecommerceservicesConstants.SPACE
									+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getSolitaireRate().doubleValue()),
											commonI18NService.getCurrency(INR)).getFormattedValue()
									+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
							weightRateList.add(key);
							final PriceData price = createPriceSign(PriceDataType.BUY,
									new BigDecimal(jPrice.getSolitaireValue().doubleValue()), commonI18NService.getCurrency(INR));
							priceBreakupData.setWeightRateList(weightRateList);
							priceBreakupData.setPrice(price);

							priceBreakupDataList.add(priceBreakupData);
						}
					}
					if (null != jPrice.getTotalDiamondValue() && jPrice.getTotalDiamondValue().doubleValue() > 0)
					{
						if (jInfo.getPIMAttributeId()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DIAMONDWEIGHTFINEJEWELLERY1))
						{
							if (null != jInfo.getWeight() && null != jPrice.getDiamondRateType1())
							{
								key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
										+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
										+ MarketplacecommerceservicesConstants.SPACE
										+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getDiamondRateType1().doubleValue()),
												commonI18NService.getCurrency(INR)).getFormattedValue()
										+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
								diamondWeightRateList.add(key);
							}
						}
						if (jInfo.getPIMAttributeId()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DIAMONDWEIGHTFINEJEWELLERY2))
						{
							if (null != jInfo.getWeight() && null != jPrice.getDiamondRateType2())
							{
								key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
										+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
										+ MarketplacecommerceservicesConstants.SPACE
										+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getDiamondRateType2().doubleValue()),
												commonI18NService.getCurrency(INR)).getFormattedValue()
										+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
								diamondWeightRateList.add(key);
							}
						}
						if (jInfo.getPIMAttributeId()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DIAMONDWEIGHTFINEJEWELLERY3))
						{
							if (null != jInfo.getWeight() && null != jPrice.getDiamondRateType3())
							{
								key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
										+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
										+ MarketplacecommerceservicesConstants.SPACE
										+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getDiamondRateType3().doubleValue()),
												commonI18NService.getCurrency(INR)).getFormattedValue()
										+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
								diamondWeightRateList.add(key);
							}
						}
						if (jInfo.getPIMAttributeId()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DIAMONDWEIGHTFINEJEWELLERY4))
						{
							if (null != jInfo.getWeight() && null != jPrice.getDiamondRateType4())
							{
								key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
										+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
										+ MarketplacecommerceservicesConstants.SPACE
										+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getDiamondRateType4().doubleValue()),
												commonI18NService.getCurrency(INR)).getFormattedValue()
										+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
								diamondWeightRateList.add(key);
							}
						}
						if (jInfo.getPIMAttributeId()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DIAMONDWEIGHTFINEJEWELLERY5))
						{
							if (null != jInfo.getWeight() && null != jPrice.getDiamondRateType5())
							{
								key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
										+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
										+ MarketplacecommerceservicesConstants.SPACE
										+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getDiamondRateType5().doubleValue()),
												commonI18NService.getCurrency(INR)).getFormattedValue()
										+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
								diamondWeightRateList.add(key);
							}
						}
						if (jInfo.getPIMAttributeId()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DIAMONDWEIGHTFINEJEWELLERY6))
						{
							if (null != jInfo.getWeight() && null != jPrice.getDiamondRateType6())
							{
								key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
										+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
										+ MarketplacecommerceservicesConstants.SPACE
										+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getDiamondRateType6().doubleValue()),
												commonI18NService.getCurrency(INR)).getFormattedValue()
										+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
								diamondWeightRateList.add(key);
							}
						}
						if (jInfo.getPIMAttributeId()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.DIAMONDWEIGHTFINEJEWELLERY7))
						{
							if (null != jInfo.getWeight() && null != jPrice.getDiamondRateType7())
							{
								key = jInfo.getWeight() + MarketplacecommerceservicesConstants.CT
										+ MarketplacecommerceservicesConstants.SPACE + MarketplacecommerceservicesConstants.ATTHERATE
										+ MarketplacecommerceservicesConstants.SPACE
										+ createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getDiamondRateType7().doubleValue()),
												commonI18NService.getCurrency(INR)).getFormattedValue()
										+ MarketplacecommerceservicesConstants.FRONTSLASH + MarketplacecommerceservicesConstants.CT;
								diamondWeightRateList.add(key);
							}
						}
					}
				}
			}

			if (CollectionUtils.isNotEmpty(diamondWeightRateList))
			{
				final PriceData diamondprice = createPriceSign(PriceDataType.BUY,
						new BigDecimal(jPrice.getTotalDiamondValue().doubleValue()), commonI18NService.getCurrency(INR));
				diamondPriceData.setName(MarketplacecommerceservicesConstants.DIAMOND);
				diamondPriceData.setPrice(diamondprice);
				diamondPriceData.setWeightRateList(diamondWeightRateList);
				priceBreakupDataList.add(diamondPriceData);
			}

			if (null != jPrice.getTotalStoneValue() && jPrice.getTotalStoneValue().doubleValue() > 0)
			{
				priceBreakupData = new PriceBreakupData();
				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getTotalStoneValue().doubleValue()),
						commonI18NService.getCurrency(INR));
				priceBreakupData.setName(MarketplacecommerceservicesConstants.GEMSTONE);
				priceBreakupData.setPrice(price);
				priceBreakupDataList.add(priceBreakupData);
			}
			if (null != jPrice.getMakingCharges() && jPrice.getMakingCharges().doubleValue() > 0)
			{
				priceBreakupData = new PriceBreakupData();
				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getMakingCharges().doubleValue()),
						commonI18NService.getCurrency(INR));
				priceBreakupData.setName(MarketplacecommerceservicesConstants.MAKINGCHARGE);
				priceBreakupData.setPrice(price);
				priceBreakupDataList.add(priceBreakupData);
			}
			if (null != jPrice.getWastageCharges() && jPrice.getWastageCharges().doubleValue() > 0)
			{
				priceBreakupData = new PriceBreakupData();
				final PriceData price = createPriceSign(PriceDataType.BUY, new BigDecimal(jPrice.getWastageCharges().doubleValue()),
						commonI18NService.getCurrency(INR));
				priceBreakupData.setName(MarketplacecommerceservicesConstants.WASTAGETAX);
				priceBreakupData.setPrice(price);
				priceBreakupDataList.add(priceBreakupData);
			}
		}


		return priceBreakupDataList;

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
	public boolean createPricebreakupOrder(final AbstractOrderEntryModel entry, final AbstractOrderEntryModel childOrderEntry)
	{
		boolean returnFlag = false;
		try
		{
			OrderJewelEntryModel orderJewelEntryModel = modelService.create(OrderJewelEntryModel.class);

			if (null != childOrderEntry && null != childOrderEntry.getOrderJewelEntry())
			{
				orderJewelEntryModel = childOrderEntry.getOrderJewelEntry();
			}
			else
			{
				JewelleryPriceRowModel jewelleryModel = null;
				final List<JewelleryPriceRowModel> jewelleryPriceRow = priceBreakupDao.getPricebreakup(entry.getSelectedUSSID());
				if (CollectionUtils.isNotEmpty(jewelleryPriceRow))
				{
					jewelleryModel = jewelleryPriceRow.get(0);
				}
				/*
				 * orderJewelEntryModel.setBaseMetalPrice1(jewelleryModel.getGoldValue());
				 * orderJewelEntryModel.setBaseMetalPrice2(jewelleryModel.getSilverValue());
				 * orderJewelEntryModel.setBaseMetalPrice3(jewelleryModel.getPlatinumValue());
				 * orderJewelEntryModel.setBaseMetalPrice4(jewelleryModel.getSolitaireValue());
				 * orderJewelEntryModel.setBaseMetalRate1(jewelleryModel.getGoldRate());
				 * orderJewelEntryModel.setBaseMetalRate2(jewelleryModel.getSilverRate());
				 * orderJewelEntryModel.setBaseMetalRate3(jewelleryModel.getPlatinumRate());
				 * orderJewelEntryModel.setBaseMetalRate4(jewelleryModel.getSolitaireRate());
				 */

				if (null != jewelleryModel && jewelleryModel.getDiamondRateType1() != null)
				{
					orderJewelEntryModel.setDiamondRateType1(jewelleryModel.getDiamondRateType1());
				}
				if (null != jewelleryModel && jewelleryModel.getDiamondRateType2() != null)
				{
					orderJewelEntryModel.setDiamondRateType2(jewelleryModel.getDiamondRateType2());
				}
				if (null != jewelleryModel && jewelleryModel.getDiamondRateType3() != null)
				{
					orderJewelEntryModel.setDiamondRateType3(jewelleryModel.getDiamondRateType3());
				}
				if (null != jewelleryModel && jewelleryModel.getDiamondRateType4() != null)
				{
					orderJewelEntryModel.setDiamondRateType4(jewelleryModel.getDiamondRateType4());
				}
				if (null != jewelleryModel && jewelleryModel.getDiamondRateType5() != null)
				{
					orderJewelEntryModel.setDiamondRateType5(jewelleryModel.getDiamondRateType5());
				}
				if (null != jewelleryModel && jewelleryModel.getDiamondRateType6() != null)
				{
					orderJewelEntryModel.setDiamondRateType6(jewelleryModel.getDiamondRateType6());
				}
				if (null != jewelleryModel && jewelleryModel.getDiamondRateType7() != null)
				{
					orderJewelEntryModel.setDiamondRateType7(jewelleryModel.getDiamondRateType7());
				}
				if (null != jewelleryModel && jewelleryModel.getTotalDiamondValue() != null)
				{
					orderJewelEntryModel.setDiamondValue(jewelleryModel.getTotalDiamondValue());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType1() != null)
				{
					orderJewelEntryModel.setStoneRateType1(jewelleryModel.getStoneRateType1());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType2() != null)
				{
					orderJewelEntryModel.setStoneRateType2(jewelleryModel.getStoneRateType2());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType3() != null)
				{
					orderJewelEntryModel.setStoneRateType3(jewelleryModel.getStoneRateType3());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType4() != null)
				{
					orderJewelEntryModel.setStoneRateType4(jewelleryModel.getStoneRateType4());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType5() != null)
				{
					orderJewelEntryModel.setStoneRateType5(jewelleryModel.getStoneRateType5());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType6() != null)
				{
					orderJewelEntryModel.setStoneRateType6(jewelleryModel.getStoneRateType6());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType7() != null)
				{
					orderJewelEntryModel.setStoneRateType7(jewelleryModel.getStoneRateType7());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType8() != null)
				{
					orderJewelEntryModel.setStoneRateType8(jewelleryModel.getStoneRateType8());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType9() != null)
				{
					orderJewelEntryModel.setStoneRateType9(jewelleryModel.getStoneRateType9());
				}
				if (null != jewelleryModel && jewelleryModel.getStoneRateType10() != null)
				{
					orderJewelEntryModel.setStoneRateType10(jewelleryModel.getStoneRateType10());
				}
				if (null != jewelleryModel && jewelleryModel.getTotalStoneValue() != null)
				{
					orderJewelEntryModel.setStoneValue(jewelleryModel.getTotalStoneValue());
				}
				if (null != jewelleryModel && jewelleryModel.getMakingCharges() != null)
				{
					orderJewelEntryModel.setMakingCharge(jewelleryModel.getMakingCharges());
				}
				if (null != jewelleryModel && jewelleryModel.getWastageCharges() != null)
				{
					orderJewelEntryModel.setWastageTax(jewelleryModel.getWastageCharges());
				}

				if (null != jewelleryModel && (jewelleryModel.getGoldRate() != null) && (jewelleryModel.getGoldValue() != null))
				{
					orderJewelEntryModel.setMetalName(MarketplacecommerceservicesConstants.GOLD);
					orderJewelEntryModel.setMetalRate(jewelleryModel.getGoldRate());
					orderJewelEntryModel.setMetalValue(jewelleryModel.getGoldValue());
					//orderJewelEntryModel.setBaseMetalPrice1(jewelleryModel.getGoldValue());
					//orderJewelEntryModel.setBaseMetalRate1(jewelleryModel.getGoldRate());
				}
				if (null != jewelleryModel && (jewelleryModel.getSilverRate() != null) && (jewelleryModel.getSilverValue() != null))
				{
					orderJewelEntryModel.setMetalName(MarketplacecommerceservicesConstants.SILVER);
					orderJewelEntryModel.setMetalRate(jewelleryModel.getSilverRate());
					orderJewelEntryModel.setMetalValue(jewelleryModel.getSilverValue());
				}
				if (null != jewelleryModel && (jewelleryModel.getPlatinumRate() != null)
						&& (jewelleryModel.getPlatinumValue() != null))
				{
					orderJewelEntryModel.setMetalName(MarketplacecommerceservicesConstants.PLATINUM);
					orderJewelEntryModel.setMetalRate(jewelleryModel.getPlatinumRate());
					orderJewelEntryModel.setMetalValue(jewelleryModel.getPlatinumValue());
				}
				if (null != jewelleryModel && (jewelleryModel.getSolitaireRate() != null)
						&& (jewelleryModel.getSolitaireValue() != null))
				{
					orderJewelEntryModel.setMetalName(MarketplacecommerceservicesConstants.SOLITAIRE);
					orderJewelEntryModel.setMetalRate(jewelleryModel.getSolitaireRate());
					orderJewelEntryModel.setMetalValue(jewelleryModel.getSolitaireValue());
					orderJewelEntryModel.setSolitaireValue(jewelleryModel.getSolitaireValue());
					orderJewelEntryModel.setSolitaireRate(jewelleryModel.getSolitaireRate());
				}

				final ProductModel productModel = productDao.findProductData(entry.getProduct().getCode());
				final ProductData productData = productConverter.convert(productModel);
				productConfiguredPopulator.populate(productModel, productData,
						Arrays.asList(ProductOption.BASIC, ProductOption.CLASSIFICATION));
				//	getProductClassificationPopulator().populate(entry.getProduct(), productData);

				if (null != productData && null != productData.getBrand() && null != productData.getBrand().getBrandname())
				{
					orderJewelEntryModel.setBrandName(productData.getBrand().getBrandname());
				}
				if (null != productData.getClassifications())
				{
					final List<ClassificationData> ConfigurableAttributeList = new ArrayList<ClassificationData>(
							productData.getClassifications());

					Collection<FeatureValueData> listFeatureValueData = null;
					Iterator<FeatureValueData> it = null;
					FeatureValueData featureValueData = null;
					String value = "";

					for (final ClassificationData configurableAttributData : ConfigurableAttributeList)
					{
						final List<FeatureData> featureDataList = new ArrayList<FeatureData>(configurableAttributData.getFeatures());


						for (final FeatureData featureData : featureDataList)
						{
							listFeatureValueData = new ArrayList(featureData.getFeatureValues());
							it = listFeatureValueData.iterator();
							featureValueData = it.next();
							value = featureValueData.getValue();

							final String name = featureData.getName();
							if (name.equalsIgnoreCase("Purity"))
							{
								orderJewelEntryModel.setPurity(value);
							}

							else if (featureData.getCode().contains("stonefinejwlry1"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType1(value);
								}
							}

							else if (featureData.getCode().contains("stonesizefinejwlry1"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType1(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry2"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType2(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry2"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType2(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry3"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType3(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry3"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType3(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry4"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType4(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry4"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType4(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry5"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType5(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry5"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType5(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry6"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType6(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry6"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType6(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry7"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType7(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry7"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType7(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry8"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType8(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry8"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType8(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry9"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType9(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry9"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType9(value);
								}
							}

							else if (featureData.getCode().contains("stonefinejwlry10"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.STONE))
								{
									orderJewelEntryModel.setStoneType10(value);
								}
							}
							else if (featureData.getCode().contains("stonesizefinejwlry10"))
							{
								if (name.equalsIgnoreCase(MarketplacecommerceservicesConstants.SIZE))
								{
									orderJewelEntryModel.setStoneSizeType10(value);
								}
							}


							else if (featureData.getCode().contains("diamondcolorfinejwlry1"))
							{
								orderJewelEntryModel.setDiamondColorType1(value);
							}

							else if (featureData.getCode().contains("diamondclarityfinejwlry1"))
							{
								orderJewelEntryModel.setDiamondClarityType1(value);
							}

							else if (featureData.getCode().contains("diamondcolorfinejwlry2"))
							{
								orderJewelEntryModel.setDiamondColorType2(value);
							}

							else if (featureData.getCode().contains("diamondclarityfinejwlry2"))
							{
								orderJewelEntryModel.setDiamondClarityType2(value);
							}

							else if (featureData.getCode().contains("diamondcolorfinejwlry3"))
							{
								orderJewelEntryModel.setDiamondColorType3(value);
							}

							else if (featureData.getCode().contains("diamondclarityfinejwlry3"))
							{
								orderJewelEntryModel.setDiamondClarityType3(value);
							}

							else if (featureData.getCode().contains("diamondcolorfinejwlry4"))
							{
								orderJewelEntryModel.setDiamondColorType4(value);
							}

							else if (featureData.getCode().contains("diamondclarityfinejwlry4"))
							{
								orderJewelEntryModel.setDiamondClarityType4(value);
							}

							else if (featureData.getCode().contains("diamondcolorfinejwlry5"))
							{
								orderJewelEntryModel.setDiamondColorType5(value);
							}

							else if (featureData.getCode().contains("diamondclarityfinejwlry5"))
							{
								orderJewelEntryModel.setDiamondClarityType5(value);
							}

							else if (featureData.getCode().contains("diamondcolorfinejwlry6"))
							{
								orderJewelEntryModel.setDiamondColorType6(value);
							}

							else if (featureData.getCode().contains("diamondclarityfinejwlry6"))
							{
								orderJewelEntryModel.setDiamondClarityType6(value);
							}

							else if (featureData.getCode().contains("diamondcolorfinejwlry7"))
							{
								orderJewelEntryModel.setDiamondColorType7(value);
							}

							else if (featureData.getCode().contains("diamondclarityfinejwlry7"))
							{
								orderJewelEntryModel.setDiamondClarityType7(value);
							}

							else if (featureData.getCode().contains("pricebreakuponinvoicefinejwlry"))
							{
								orderJewelEntryModel.setPriceBreakuponInvoice(value);
							}

						}
					}
				}
			}

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
