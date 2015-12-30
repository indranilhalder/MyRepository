package com.tisl.mpl.seller.product.facades.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.enums.OnlineExclusiveEnum;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.RichAttributeData;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;


/**
 * @author TCS
 *
 */
public class BuyBoxFacadeImpl implements BuyBoxFacade
{


	/**
	 *
	 */
	private static final String Y = "Y";

	private static final String CN_C = "CnC";

	private static final String ED = "ED";
	private static final String HD = "HD";


	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BuyBoxFacadeImpl.class);


	@Autowired
	private ConfigurationService configurationService;
	@Resource
	private BuyBoxService buyBoxService;
	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;



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

	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}

	/**
	 * @param mplDeliveryCostService
	 *           the mplDeliveryCostService to set
	 */
	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}

	private PriceDataFactory priceDataFactory;

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}





	/**
	 * This method is responsible for get the winning buybox seller and other sellers count and minimum price information
	 * for the given product code
	 *
	 * @param productCode
	 * @return-buyboxData
	 */


	@Override
	public BuyBoxData buyboxPrice(final String productCode) throws EtailNonBusinessExceptions
	{
		final BuyBoxData buyboxData = new BuyBoxData();
		boolean onlyBuyBoxHasStock = false;
		BuyBoxModel buyBoxMod = null;

		try
		{
			List<BuyBoxModel> buyboxModelList = new ArrayList<BuyBoxModel>(buyBoxService.buyboxPrice(productCode));
			buyboxData.setAllOOStock(MarketplaceFacadesConstants.N);

			//If all the sellers has stock zero, then display any product having non zero price
			if (buyboxModelList.isEmpty())
			{
				LOG.info("************* No Seller with inventory>0 inventory, Fetching buy box rows having price>0 *********");
				buyboxData.setAllOOStock(MarketplaceFacadesConstants.Y);
				buyboxModelList = buyBoxService.buyBoxPriceNoStock(productCode);
				if (CollectionUtils.isNotEmpty(buyboxModelList))
				{
					buyBoxMod = buyboxModelList.get(0);
				}
				else
				{
					LOG.info("************* Fetching buy box rows having price>0 and without inventory check *********");
					buyboxData.setSellerAssociationstatus(SellerAssociationStatusEnum.NO.toString());
				}
				//	buyboxModelList = new CopyOnWriteArrayList<BuyBoxModel>(buyBoxService.buyBoxPriceNoStock(productCode));
			}

			else if (buyboxModelList.size() == 1)
			{
				//	List<BuyBoxModel> buyboxList=buyboxModelList;
				onlyBuyBoxHasStock = true;
				//final List<BuyBoxModel> buyboxList = buyBoxService.buyBoxPriceNoStock(productCode);
				buyboxModelList = buyBoxService.buyBoxPriceNoStock(productCode);
				for (final BuyBoxModel buybx : buyboxModelList)
				{
					if (buybx.getAvailable().doubleValue() > 0)
					{
						//	buyboxModelList.set(0, buybx);
						buyBoxMod = buybx;
						break;
					}
				}

			}
			else
			{
				buyBoxMod = buyboxModelList.get(0);
			}
			//if (buyboxModelList != null && buyboxModelList.size() > 0)
			if (buyboxModelList.size() > 0)
			{


				if (null != buyBoxMod.getSpecialPrice() && buyBoxMod.getSpecialPrice().doubleValue() > 0)
				{
					final double spPrice = buyBoxMod.getSpecialPrice().doubleValue();
					//final double roundedSpPrice = Math.round(spPrice * 100) / 100;
					buyboxData.setSpecialPrice(productDetailsHelper.formPriceData(new Double(spPrice)));
				}
				final double price = buyBoxMod.getPrice().doubleValue();
				buyboxData.setPrice(productDetailsHelper.formPriceData(new Double(price)));
				buyboxData.setSellerAssociationstatus(SellerAssociationStatusEnum.YES.toString());
				buyboxData.setSellerName(buyBoxMod.getSellerName());
				buyboxData.setSellerId(buyBoxMod.getSellerId());
				buyboxData.setSellerArticleSKU(buyBoxMod.getSellerArticleSKU());
				buyboxData.setAvailable(buyBoxMod.getAvailable());
				if (null != buyBoxMod.getMrp())
				{
					buyboxData.setMrp(productDetailsHelper.formPriceData(new Double(buyBoxMod.getMrp().doubleValue())));
				}
				buyboxData.setMrpPriceValue(productDetailsHelper.formPriceData(new Double(buyBoxMod.getMrp().doubleValue())));

				//other sellers count
				final int sellerSize = buyboxModelList.size() - 1;
				final Integer noofsellers = Integer.valueOf(sellerSize);
				if (onlyBuyBoxHasStock && sellerSize > 0)
				{
					buyboxData.setNumberofsellers(Integer.valueOf(-1));
					//buyboxData.setNumberofsellers(Integer.valueOf(-1));
				}
				else
				{
					buyboxData.setNumberofsellers(noofsellers);
				}
				//Minimum price for other sellers
				double minPrice = 0.0d;
				if (sellerSize > 0)
				{

					for (int i = 1; i <= sellerSize; i++)
					{
						if (null != buyboxModelList.get(i).getSpecialPrice()
								&& buyboxModelList.get(i).getSpecialPrice().doubleValue() > 0)
						{

							final double specialPrice = buyboxModelList.get(i).getSpecialPrice().doubleValue();

							if (i == 1)
							{
								minPrice = specialPrice;
							}
							else
							{
								if (minPrice > specialPrice)
								{
									minPrice = specialPrice;
								}
							}
						}
						else
						{
							double actualPrice = 0.0D;
							if (null != buyboxModelList.get(i).getPrice())
							{
								actualPrice = buyboxModelList.get(i).getPrice().doubleValue();
							}
							if (i == 1)
							{
								minPrice = actualPrice;
							}
							else
							{
								if (minPrice > actualPrice)
								{
									minPrice = actualPrice;
								}
							}
						}
					}

				}
				final double roundedMinPrice = Math.round(minPrice * 100) / 100;
				buyboxData.setMinPrice(productDetailsHelper.formPriceData(new Double(roundedMinPrice)));

			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3001);
			}
		}
		catch (final NumberFormatException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0001);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return buyboxData;
	}

	/**
	 * This method is responsible for getting the list of the sellers for the given product code
	 *
	 * @param productCode
	 * @return SellerInformationDataList
	 */
	@Override
	public List<SellerInformationData> getsellersDetails(final String productCode) throws EtailNonBusinessExceptions,
			EtailBusinessExceptions
	{
		final List<SellerInformationData> SellerInformationDataList = new ArrayList<SellerInformationData>();

		for (final Map<BuyBoxModel, RichAttributeModel> resultMap : buyBoxService.getsellersDetails(productCode))
		{
			for (final Map.Entry<BuyBoxModel, RichAttributeModel> entry : resultMap.entrySet())
			{
				final BuyBoxModel buyBox = entry.getKey();


				final RichAttributeModel rich = entry.getValue();

				final SellerInformationData sellerData = new SellerInformationData();
				sellerData.setLeadTimeForHomeDelivery(rich.getLeadTimeForHomeDelivery());
				sellerData.setAvailableStock(buyBox.getAvailable());
				sellerData.setSellerAssociationstatus(SellerAssociationStatusEnum.YES.toString());
				if (null != buyBox.getWeightage())
				{
					sellerData.setBuyBoxWeightage(buyBox.getWeightage());
				}
				if (null != buyBox.getSellerId())
				{
					sellerData.setSellerID(buyBox.getSellerId());
				}
				if (null != buyBox.getSpecialPrice())
				{
					sellerData.setSpPrice(productDetailsHelper.formPriceData(buyBox.getSpecialPrice()));
				}
				else
				{
					sellerData.setSpPrice(productDetailsHelper.formPriceData(new Double(0.0D)));
				}
				if (null != buyBox.getPrice())
				{
					sellerData.setMopPrice(productDetailsHelper.formPriceData(buyBox.getPrice()));
				}
				else
				{
					sellerData.setMopPrice(productDetailsHelper.formPriceData(new Double(0.0D)));
				}
				if (null != buyBox.getMrp())
				{
					sellerData.setMrpPrice(productDetailsHelper.formPriceData(buyBox.getMrp()));
				}
				else
				{
					sellerData.setMrpPrice(productDetailsHelper.formPriceData(new Double(0.0D)));
				}
				sellerData.setUssid(buyBox.getSellerArticleSKU());
				sellerData.setSellername(buyBox.getSellerName());

				sellerData.setDeliveryModes(productDetailsHelper.getDeliveryModeLlist(rich, buyBox.getSellerArticleSKU()));


				if (null != rich.getShippingModes())
				{
					sellerData.setShippingMode(rich.getShippingModes().getCode());
				}


				if (null != rich.getDeliveryFulfillModes() && rich.getDeliveryFulfillModes().equals(DeliveryFulfillModesEnum.TSHIP))
				{

					sellerData.setFullfillment(rich.getDeliveryFulfillModes().getCode());
				}
				else
				{

					sellerData.setFullfillment(rich.getDeliveryFulfillModes().getCode());
				}
				if (null != rich.getPaymentModes()
						&& (PaymentModesEnum.COD.toString().equalsIgnoreCase(rich.getPaymentModes().getCode()) || (PaymentModesEnum.BOTH
								.toString().equalsIgnoreCase(rich.getPaymentModes().getCode()))))


				{
					sellerData.setIsCod(MarketplaceFacadesConstants.Y);
				}
				else
				{
					sellerData.setIsCod(MarketplaceFacadesConstants.N);
				}

				sellerData.setReturnPolicy(rich.getReturnWindow());
				sellerData.setReplacement(rich.getReplacementWindow());

				SellerInformationDataList.add(sellerData);

			}
		}
		return SellerInformationDataList;

	}


	/**
	 * This method is responsible for getting the RICH ATTRIBUTE DETAILS
	 *
	 * @param productModel
	 * @param buyboxid
	 * @return richData
	 */
	@Override
	public RichAttributeData getRichAttributeDetails(final ProductModel productModel, final String buyboxid)
			throws EtailNonBusinessExceptions, EtailBusinessExceptions
	{
		final RichAttributeData richData = new RichAttributeData();
		final StringBuilder deliveryModes = new StringBuilder();
		boolean onlineExclusive = false;
		Date existDate = null;
		final String allowNew = configurationService.getConfiguration().getString("attribute.new.display");
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if ((seller.getSellerAssociationStatus() == null || seller.getSellerAssociationStatus().equals(
					SellerAssociationStatusEnum.YES))
					&& (null != seller.getStartDate() && new Date().after(seller.getStartDate()) && null != seller.getEndDate() && new Date()
							.before(seller.getEndDate())))
			{
				if (null != seller.getOnlineExclusive()
						&& (OnlineExclusiveEnum.YES).toString().equalsIgnoreCase(seller.getOnlineExclusive().getCode()))
				{
					onlineExclusive = true;
				}

				if (buyboxid.equals(seller.getSellerArticleSKU()) && null != seller.getRichAttribute())
				{

					for (final RichAttributeModel rich : seller.getRichAttribute())
					{
						if (null != rich.getPaymentModes()
								&& (PaymentModesEnum.COD.toString().equalsIgnoreCase(rich.getPaymentModes().getCode()) || (PaymentModesEnum.BOTH
										.toString().equalsIgnoreCase(rich.getPaymentModes().getCode()))))


						{
							richData.setIsCod(MarketplaceFacadesConstants.Y);
						}
						if (null != rich.getDeliveryFulfillModes())
						{
							richData.setFulfillment(rich.getDeliveryFulfillModes().getCode());
						}
						if (HomeDeliveryEnum.YES.equals(rich.getHomeDelivery()))
						{
							deliveryModes.append(HD).append(','); // SONAR Fixes
						}
						if (ExpressDeliveryEnum.YES.equals(rich.getExpressDelivery()))
						{
							deliveryModes.append(ED).append(','); // SONAR Fixes
						}
						if (ClickAndCollectEnum.YES.equals(rich.getClickAndCollect()))
						{
							deliveryModes.append(CN_C).append(','); // SONAR Fixes
						}

						if (!deliveryModes.toString().isEmpty())
						{
							richData.setDeliveryModes(deliveryModes.toString().substring(0, deliveryModes.toString().length() - 1));
						}
						richData.setLeadTimeForHomeDelivery(rich.getLeadTimeForHomeDelivery());
						if (null != rich.getReturnWindow())
						{
							richData.setReturnWindow(rich.getReturnWindow());
						}
						//productDetailsHelper.getDeliveryModeATMap(deliveryInfoList)
					}

				}
				if (null != allowNew && allowNew.equalsIgnoreCase(Y))
				{
					//Find the oldest startDate of the seller
					if (null == existDate)
					{
						existDate = seller.getStartDate();
					}
					else if (existDate.after(seller.getStartDate()))
					{
						existDate = seller.getStartDate();
					}
				}
			}
		}
		//New Attribute
		if (null != existDate && isNew(existDate))
		{
			richData.setNewProduct(Y);
		}
		richData.setOnlineExclusive(onlineExclusive);
		LOG.info("******New Product::::::::::::" + richData.getNewProduct());
		return richData;
	}

	private boolean isNew(final Date existDate)
	{
		boolean newAttr = false;
		if (null != existDate)
		{
			final Date sysDate = new Date();
			final long dayDiff = calculateDays(existDate, sysDate);

			LOG.info("******" + existDate + "  --- dayDiff: " + dayDiff);
			final String validDaysSt = configurationService.getConfiguration().getString("attribute.new.validDays");
			final int validDays = validDaysSt == null ? 0 : Integer.parseInt(validDaysSt);

			if (validDays > dayDiff)
			{
				newAttr = true;
			}
		}

		return newAttr;
	}

	private long calculateDays(final Date dateEarly, final Date dateLater)
	{
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
	}


	/*
	 * This method is used to get the price of a product by giving the ussid
	 * 
	 * @see com.tisl.mpl.seller.product.facades.BuyBoxFacade#getpriceForUssid(java.lang.String)
	 */

	@Override
	public BuyBoxModel getpriceForUssid(final String ussid)
	{
		return buyBoxService.getpriceForUssid(ussid);
	}


}