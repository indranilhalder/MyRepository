package com.tisl.mpl.seller.product.facades.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

	private static final String CN_C = "CNC";

	private static final String ED = "ED";
	private static final String HD = "HD";
	//TISCR-414 - Chairmans demo feedback 10thMay CR
	private static final String LINGERIE1 = "LINGERIE1";
	private static final String LINGERIE2 = "LINGERIE2";

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

	private static final String BUYBOX_LIST = "buyboxList";

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



	//TISPRM-56

	/**
	 * This method is responsible for get the winning buybox seller and other sellers count and minimum price information
	 * for the given product code
	 *
	 * @param productCode
	 * @param bBoxSellerId
	 * @return-buyboxData
	 */


	@Override
	public Map<String, Object> buyboxPricePDP(final String productCode, final String bBoxSellerId //CKD:TPR-250
	) throws EtailNonBusinessExceptions
	{
		BuyBoxData buyboxData = new BuyBoxData();
		boolean onlyBuyBoxHasStock = false;
		BuyBoxModel buyBoxMod = null;
		//OOS Error Changes PDP
		int totalProductCount = 0;
		final List<BuyBoxData> buyBoxDataList = new ArrayList<BuyBoxData>();
		//TISPRM -56
		String products[] = null;
		String pdpProduct = null;
		List<String> productsList = new ArrayList<String>();

		final Map<String, Object> returnData = new HashMap<String, Object>();
		List<BuyBoxModel> buyboxModelList = new ArrayList<BuyBoxModel>();
		List<String> productsWithNoStock = new ArrayList<String>();
		List<String> arrayToProductList = null;

		if (productCode.indexOf(MarketplacecommerceservicesConstants.COMMA) != -1)
		{
			products = productCode.split(MarketplacecommerceservicesConstants.COMMA);
			pdpProduct = products[0];
			arrayToProductList = new ArrayList<String>(Arrays.asList(products));
			//For Apparel
			totalProductCount = arrayToProductList.size();
		}
		else
		{
			pdpProduct = productCode;
			//For Electronics
			totalProductCount = 1;//as there are no variants
		}
		boolean isSellerPresent = false;
		//END
		try
		{
			//TISPRM -56
			List<BuyBoxModel> buyboxModelListAll = null;
			//CKD:TPR-250 Start : Manipulating (rearranging) elements of the buy box list for microsite seller
			if (StringUtils.isNotBlank(bBoxSellerId))
			{
				// overridding buyBox for buyboxPrice method for  microsite TPR-250 (TPR-4955) : adding sellers with No stock as well
				buyboxModelListAll = new ArrayList<BuyBoxModel>(buyBoxService.buyboxPriceForMicrosite(productCode));
				isSellerPresent = true;
				rearrangeBuyBoxListElements(bBoxSellerId, buyboxModelListAll);
			}
			else
			{
				buyboxModelListAll = new ArrayList<BuyBoxModel>(buyBoxService.buyboxPrice(productCode));
			}
			//CKD:TPR-250 End
			for (final BuyBoxModel buyBoxModel : buyboxModelListAll)
			{
				if (null != pdpProduct && pdpProduct.equalsIgnoreCase(buyBoxModel.getProduct()))
				{
					buyboxModelList.add(buyBoxModel);
				}
				if (!productsList.contains(buyBoxModel.getProduct()))
				{
					productsList.add(buyBoxModel.getProduct());
				}
			}
			//END
			buyboxData.setAllOOStock(MarketplaceFacadesConstants.N);

			//If all the sellers has stock zero, then display any product having non zero price
			if (buyboxModelList.isEmpty())
			{
				LOG.info("************* No Seller with inventory>0 inventory, Fetching buy box rows having price>0 *********");
				// buyboxData.setAllOOStock(MarketplaceFacadesConstants.Y);// its is valid for electronics product
				buyboxModelList = buyBoxService.buyBoxPriceNoStock(pdpProduct);
				if (CollectionUtils.isNotEmpty(buyboxModelList))
				{
					//TODO
					buyBoxMod = buyboxModelList.get(0);
				}
				else
				{
					LOG.info("************* Fetching buy box rows having price>0 and without inventory check *********");
					buyboxData.setSellerAssociationstatus(SellerAssociationStatusEnum.NO.toString());
				}
				//no stock for all products
				if (null != arrayToProductList)
				{
					arrayToProductList.removeAll(productsList);
					productsWithNoStock = arrayToProductList;
				}

				//Added For Electronics INC_11278
				else if (buyBoxMod.getProduct().equalsIgnoreCase(pdpProduct))
				{
					productsWithNoStock.add(pdpProduct);
				}
			}
			// TPR-250: Start
			else if (isSellerPresent)
			{
				buyboxModelList = buyBoxService.buyBoxPriceNoStock(pdpProduct);
				for (final BuyBoxModel buybx : buyboxModelList)
				{
					if (buybx.getSellerId().equals(bBoxSellerId))
					{
						buyBoxMod = buybx;
						break;
					}
				}
				//Availability counts
				if (null != arrayToProductList)
				{
					productsList = identifyProductsWithNoStock(buyboxModelListAll, pdpProduct, productsList);
					arrayToProductList.removeAll(productsList);
					productsWithNoStock = arrayToProductList;
				}
			}
			// TPR-250: End
			else if (buyboxModelList.size() == 1)
			{
				onlyBuyBoxHasStock = true;
				buyboxModelList = buyBoxService.buyBoxPriceNoStock(pdpProduct);
				for (final BuyBoxModel buybx : buyboxModelList)
				{
					if (buybx.getAvailable().doubleValue() > 0)
					{
						buyBoxMod = buybx;
						break;
					}
				}
				//Availability counts
				if (null != arrayToProductList)
				{
					arrayToProductList.removeAll(productsList);
					productsWithNoStock = arrayToProductList;
				}
			}
			else
			{
				buyBoxMod = buyboxModelList.get(0);
				//Availability counts
				if (null != arrayToProductList)
				{
					arrayToProductList.removeAll(productsList);
					productsWithNoStock = arrayToProductList;
				}
			}
			//OOS Error Changes PDP
			if (totalProductCount == productsWithNoStock.size())
			{
				buyboxData.setAllOOStock(MarketplaceFacadesConstants.Y);// its is valid for electronics product
			}

			//TPR-1375 changes start
			if (CollectionUtils.isNotEmpty(buyboxModelList) && buyboxModelList.size() > 1)
			{
				for (final BuyBoxModel buyBoxModel : buyboxModelList)
				{
					buyBoxDataList
							.add(populateBuyBoxData(buyBoxModel, onlyBuyBoxHasStock, buyboxModelList, buyboxData.getAllOOStock()));
				}

			}
			returnData.put(BUYBOX_LIST, buyBoxDataList);
			//TPR-1375 changes end
			if (buyboxModelList.size() > 0)
			{
				buyboxData = populateBuyBoxData(buyBoxMod, onlyBuyBoxHasStock, buyboxModelList, buyboxData.getAllOOStock());
				//TPR-1375 changes starts
				if (CollectionUtils.isEmpty(buyBoxDataList))
				{
					returnData.put(BUYBOX_LIST, Arrays.asList(buyboxData));
				}
				//TPR-1375 changes ends
			}
			else
			{
				//Fix for Defect TISPT-152
				LOG.warn("No buybox present for the product with product code ::: " + productCode);
				//throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3001);
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
		returnData.put("pdp_buy_box", buyboxData);
		returnData.put("no_stock_p_codes", productsWithNoStock);

		return returnData;
	}

	/**
	 * @param buyboxModelListAll
	 * @param pdpProduct
	 * @param productsList
	 * @return productsList
	 */
	//CKD:TPR-250: Start
	//  to create a productlist to generate availability property in buybox json response similar to non-microsite call in PDP
	List<String> identifyProductsWithNoStock(final List<BuyBoxModel> buyboxModelListAll, final String pdpProduct,
			final List<String> productsList)
	{
		for (final BuyBoxModel bbModel : buyboxModelListAll)
		{
			if (bbModel.getAvailable() <= 0)
			{
				productsList.remove(bbModel.getProduct());
			}
		}
		return productsList;
	}

	//CKD:TPR-250: End

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
			}

			else if (buyboxModelList.size() == 1)
			{
				onlyBuyBoxHasStock = true;
				buyboxModelList = buyBoxService.buyBoxPriceNoStock(productCode);
				for (final BuyBoxModel buybx : buyboxModelList)
				{
					if (buybx.getAvailable().doubleValue() > 0)
					{
						buyBoxMod = buybx;
						break;
					}
				}

			}
			else
			{
				buyBoxMod = buyboxModelList.get(0);
			}
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
				final int oosSellerforMsite = getOosSellerCountInBuyBoxModel(buyboxModelList);
				final int sellerSize = buyboxModelList.size() - 1 - oosSellerforMsite; // CKD:TPR-250:TPR-4495
				final Integer noofsellers = Integer.valueOf(sellerSize);

				//TPR-250:Start
				//				if (onlyBuyBoxHasStock && sellerSize > 0)
				//				{
				//					buyboxData.setNumberofsellers(Integer.valueOf(-1));
				//					//buyboxData.setNumberofsellers(Integer.valueOf(-1));
				//				}
				//				else
				//				{
				buyboxData.setNumberofsellers(noofsellers);
				//}
				//TPR-250:End
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
	 * This method is responsible to get the buybox data for the given product code and seller ID
	 *
	 * @param productCode
	 * @param sellerId
	 * @return-buyboxData
	 */
	@Override
	public BuyBoxData buyboxForSizeGuide(final String productCode, final String sellerId) throws EtailNonBusinessExceptions
	{
		LOG.debug(String.format("buyboxForSizeGuide : productCode:  %s | sellerId : %s ", productCode, sellerId));

		final BuyBoxData buyboxData = new BuyBoxData();

		try
		{
			//return buybox data for product code and seller id
			final BuyBoxModel buyBoxMod = buyBoxService.buyboxForSizeGuide(productCode, sellerId);

			//If all the sellers has stock zero, then display any product having non zero price

			if (null != buyBoxMod.getSpecialPrice() && buyBoxMod.getSpecialPrice().doubleValue() > 0)
			{
				final double spPrice = buyBoxMod.getSpecialPrice().doubleValue();

				// Get formated price data
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
		//TISCR-414 - Chairmans demo feedback 10thMay CR starts
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>(productModel.getSupercategories());
		final String configLingerieCategoris1 = configurationService.getConfiguration().getString("pdp.lingerie1.code");
		final String configLingerieCategoris2 = configurationService.getConfiguration().getString("pdp.lingerie2.code");

		if (StringUtils.isNotEmpty(configLingerieCategoris1) && isCatLingerie(categoryList, configLingerieCategoris1))
		{
			richData.setReturnWindow(LINGERIE1);
		}
		else if (StringUtils.isNotEmpty(configLingerieCategoris2) && isCatLingerie(categoryList, configLingerieCategoris2))
		{
			richData.setReturnWindow(LINGERIE2);
		}


		LOG.info("******New Product::::::::::::" + richData.getNewProduct() + " return window:" + richData.getReturnWindow());
		//TISCR-414 - Chairmans demo feedback 10thMay CR ends
		return richData;
	}

	//TISCR-414 - Chairmans demo feedback 10thMay CR
	@Override
	public boolean isCatLingerie(final List<CategoryModel> categoryList, final String configLingerieCategoris)
			throws EtailNonBusinessExceptions

	{
		boolean isLingerie = false;
		try
		{
			if (null != categoryList && !categoryList.isEmpty())
			{
				final List<String> categoryCodeList = new ArrayList<String>();
				for (final CategoryModel catModel : categoryList)
				{
					categoryCodeList.add(catModel.getCode());
				}
				if (StringUtils.isNotEmpty(configLingerieCategoris))
				{
					final String[] longeriesCatCodeData = configLingerieCategoris.split(",");
					for (int index = 0; index < longeriesCatCodeData.length; index++)
					{
						if (categoryCodeList.contains(longeriesCatCodeData[index].trim()))
						{
							isLingerie = true;
							break;
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return isLingerie;
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

	/**
	 * changes for TPR-1375 poulating buybox details from buybox models
	 *
	 * @param buyBoxMod
	 * @param buyboxModelList
	 * @param onlyBuyBoxHasStock
	 * @return buyboxData
	 */
	private BuyBoxData populateBuyBoxData(final BuyBoxModel buyBoxMod, final boolean onlyBuyBoxHasStock,
			final List<BuyBoxModel> buyboxModelList, final String outOfStockFlag)
	{
		final BuyBoxData buyboxData = new BuyBoxData();
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
		//CKD:TPR-250:Start: checking if list has Buy Box list has OOS seller to be removed from other sellers count when call comes from microsite
		final int oosSellerforMsite = getOosSellerCountInBuyBoxModel(buyboxModelList);
		//other sellers count
		final int sellerSize = buyboxModelList.size() - 1 - oosSellerforMsite;
		//CKD:TPR-250:End
		final Integer noofsellers = Integer.valueOf(sellerSize);
		//TPR-250:Start
		//		if (onlyBuyBoxHasStock && sellerSize > 0)
		//		{
		//			buyboxData.setNumberofsellers(Integer.valueOf(-1));
		//			//buyboxData.setNumberofsellers(Integer.valueOf(-1));
		//		}
		////		else
		//		{
		buyboxData.setNumberofsellers(noofsellers);
		//	}
		//TPR-250:end
		//Minimum price for other sellers
		double minPrice = 0.0d;
		if (sellerSize > 0)
		{

			for (int i = 1; i <= sellerSize; i++)
			{
				if (null != buyboxModelList.get(i).getSpecialPrice() && buyboxModelList.get(i).getSpecialPrice().doubleValue() > 0)
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
		buyboxData.setAllOOStock(outOfStockFlag);
		return buyboxData;

	}

	/**
	 * @param buyboxModelList
	 * @return
	 */
	private int getOosSellerCountInBuyBoxModel(final List<BuyBoxModel> buyboxModelList)
	{
		int oosSellerforMsite = 0;
		for (final BuyBoxModel buyBoxModel : buyboxModelList)
		{
			if (buyBoxModel.getAvailable() <= 0)
			{
				oosSellerforMsite = oosSellerforMsite + 1;
			}
		}
		return oosSellerforMsite;
	}

	/**
	 * @param bBoxSellerId
	 * @param buyboxModelListAll
	 */
	private void rearrangeBuyBoxListElements(final String bBoxSellerId, final List<BuyBoxModel> buyboxModelListAll)
	{
		{
			List<BuyBoxModel> msiteBboxOtherSellerList = null;
			List<BuyBoxModel> msiteBboxWinningSellerList = null;
			msiteBboxWinningSellerList = new ArrayList<BuyBoxModel>();
			msiteBboxOtherSellerList = new ArrayList<BuyBoxModel>();
			for (final BuyBoxModel buyBoxModel : buyboxModelListAll)
			{
				if (bBoxSellerId.equalsIgnoreCase(buyBoxModel.getSellerId()))
				{
					msiteBboxWinningSellerList.add(buyBoxModel);
				}
				else
				{
					// Adding only those as other sellers which are having stock
					if (buyBoxModel.getAvailable() > 0)
					{
						msiteBboxOtherSellerList.add(buyBoxModel);
					}
				}
			}
			buyboxModelListAll.clear();
			buyboxModelListAll.addAll(msiteBboxWinningSellerList);
			buyboxModelListAll.addAll(msiteBboxOtherSellerList);
			// nullifying the lists as they are no more required
			msiteBboxWinningSellerList = null;
			msiteBboxOtherSellerList = null;
		}
	}
}
