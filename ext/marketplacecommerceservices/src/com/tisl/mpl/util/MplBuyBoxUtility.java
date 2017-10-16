/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;


/**
 * @author TCS
 *
 */
public class MplBuyBoxUtility
{

	private static final Logger LOG = Logger.getLogger(MplBuyBoxUtility.class); //comment out for TISPRD-8944


	//---------------Solve for Issue TISPRD-58---------------------//
	private static final String COLORAPPAREL = "colorapparel";
	public static final String CLOTHING = "Clothing";
	private static final String ELECTRONICS = "electronics";
	private static final String FOOTWEAR = "footwear";
	private static final String COLORFAMILYFOOTWEAR = "brandcolorfootwear";
	private static final String COLORELECTRONICS = "colorelectronics";
	private static final String HYPHEN = " - ";
	private static final String FINE_JEWELLERY = "FineJewellery";

	//---------------Solve for Issue TISPRD-58---------------------//


	private BuyBoxService buyBoxService;

	private ClassificationService classificationService;

	@Autowired
	private MplVariantComparator variantComparator;

	private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;

	@Resource(name = "buyBoxDao")
	private BuyBoxDao buyBoxDao;

	public Double getBuyBoxSellingPrice(final ProductModel productModel) throws EtailNonBusinessExceptions
	{
		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		Double price = Double.valueOf(0);
		if (buyBoxWinnerModel != null)
		{
			// TPR-1886 | Fetch price logic for Jewellery
			if (productModel.getProductCategoryType().equalsIgnoreCase(FINE_JEWELLERY))
			{
				if ((buyBoxWinnerModel.getPLPMaxPrice() != null && buyBoxWinnerModel.getPLPMaxPrice().doubleValue() > 0)
						&& (buyBoxWinnerModel.getPLPMinPrice() != null && buyBoxWinnerModel.getPLPMinPrice().doubleValue() > 0))//SONAR FIX JEWELLERY
				{
					final double maxPrice = buyBoxWinnerModel.getPLPMaxPrice().doubleValue();
					final double minPrice = buyBoxWinnerModel.getPLPMinPrice().doubleValue();
					final double avgPrice = (maxPrice + minPrice) / 2;
					price = Double.valueOf(avgPrice);
				}
				else
				{
					// Any of Max price and min price is null
					price = productModel.getMrp();
				}
			} /// end of Jewellery
			else
			{
				price = buyBoxWinnerModel.getPrice();

				if (null != buyBoxWinnerModel.getSpecialPrice() && buyBoxWinnerModel.getSpecialPrice().intValue() > 0)
				{
					price = buyBoxWinnerModel.getSpecialPrice();
				}
			}
		}
		else
		{
			price = productModel.getMrp();

		}
		LOG.debug("Inside MplBuyBoxUtility price value is" + price);
		return price;
	}

	public Double getBuyBoxMrpPrice(final ProductModel productModel) throws EtailNonBusinessExceptions
	{
		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		Double mrpPrice = Double.valueOf(0);
		if (buyBoxWinnerModel != null)
		{
			mrpPrice = buyBoxWinnerModel.getMrp();
		}
		else
		{
			mrpPrice = productModel.getMrp();
		}
		return mrpPrice;
	}

	public String getLeastSizeProduct(final ProductModel productModel) throws EtailNonBusinessExceptions
	{
		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		String productCode = null;
		if (buyBoxWinnerModel != null)
		{
			productCode = buyBoxWinnerModel.getProduct();
		}
		else
		{
			productCode = productModel.getCode();
		}
		return productCode;

	}

	public BuyBoxModel getBuyBoxPrice(final ProductModel productModel) throws EtailNonBusinessExceptions
	{

		String productCode = productModel.getCode();
		boolean outOfStockFlagForSellers = false;
		// Run comparator and Get list of products codes for variants.
		List<PcmProductVariantModel> sortedVariantsList = new ArrayList<PcmProductVariantModel>();
		if (productModel instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel selectedVariantModel = (PcmProductVariantModel) productModel;
			final ProductModel baseProduct = selectedVariantModel.getBaseProduct();

			if (baseProduct != null && baseProduct.getVariants() != null && baseProduct.getVariants().size() > 0)
			{
				sortedVariantsList = compareVariants(baseProduct, selectedVariantModel);
				String variantProductsInCondition = "";
				for (final PcmProductVariantModel pcmProductVariantModel : sortedVariantsList)
				{
					variantProductsInCondition = getInConditionProductCodes(pcmProductVariantModel.getCode(),
							variantProductsInCondition);
				}
				productCode = variantProductsInCondition;
			}

		} //Get simple product code other than variant
		Map<BuyBoxModel, Double> priceValueMap = new HashMap<BuyBoxModel, Double>();
		final Map<BuyBoxModel, Double> finalpriceValueMap = new HashMap<BuyBoxModel, Double>();
		BuyBoxModel buyBoxModel = null;
		if (CollectionUtils.isEmpty(sortedVariantsList))
		{
			productCode = getInConditionProductCodes(productModel.getCode(), "");
			final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productCode);
			if (productModel instanceof PcmProductVariantModel)
			{
				//checking whether the product is Out of Stock or not --------- Changes for TISPRD-1691
				outOfStockFlagForSellers = isOutOfStock(buyBoxModelList);
				if (outOfStockFlagForSellers)
				{
					priceValueMap = getLeastPriceModelForOutOfStockProduct(buyBoxModelList, priceValueMap);
				}
				else
				{
					priceValueMap = getLeastPriceModel(buyBoxModelList, priceValueMap);
				}
			}
			finalpriceValueMap.putAll(priceValueMap);
			LOG.debug("value of priceValueMap >>>>>>>>>" + priceValueMap);
		}
		else
		{
			final boolean allOutOfStockVariants = chcekOutOfStockForAllVariants(sortedVariantsList);
			for (final PcmProductVariantModel productVariant : sortedVariantsList)
			{
				final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productVariant.getCode());

				//checking whether the product is Out of Stock or not --------- Changes for TISPRD-1691
				if (allOutOfStockVariants)
				{
					priceValueMap = getLeastPriceModelForOutOfStockProduct(buyBoxModelList, priceValueMap);
				}
				else
				{
					priceValueMap = getLeastPriceModel(buyBoxModelList, finalpriceValueMap);
				}
				finalpriceValueMap.putAll(priceValueMap);
			}
		}
		LOG.debug("value of priceValueMap *******" + priceValueMap);
		//TISPRD-1079
		if (finalpriceValueMap.isEmpty())
		{
			final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productCode);

			//checking whether the product is Out of Stock or not --------- Changes for TISPRD-1691
			outOfStockFlagForSellers = isOutOfStock(buyBoxModelList);
			if (outOfStockFlagForSellers)
			{
				priceValueMap = getLeastPriceModelForOutOfStockProduct(buyBoxModelList, priceValueMap);
			}
			else
			{
				priceValueMap = getLeastPriceModel(buyBoxModelList, finalpriceValueMap);
			}
			finalpriceValueMap.putAll(priceValueMap);
		}
		final List<Map.Entry<BuyBoxModel, Double>> priceList = new LinkedList<Map.Entry<BuyBoxModel, Double>>(
				finalpriceValueMap.entrySet());
		if (productModel.getProductCategoryType() != null && productModel.getProductCategoryType().equalsIgnoreCase(FINE_JEWELLERY))
		{
			//TPR-1886 | Sorts descending against price, for fine jewellery products
			Collections.sort(priceList, new Comparator<Map.Entry<BuyBoxModel, Double>>()
			{
				@Override
				public int compare(final Map.Entry<BuyBoxModel, Double> o1, final Map.Entry<BuyBoxModel, Double> o2)
				{
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
		}
		else
		{
			Collections.sort(priceList, new Comparator<Map.Entry<BuyBoxModel, Double>>()
			{
				@Override
				public int compare(final Map.Entry<BuyBoxModel, Double> o1, final Map.Entry<BuyBoxModel, Double> o2)
				{
					return (o1.getValue()).compareTo(o2.getValue());
				}
			});
		}

		final Map<BuyBoxModel, Double> sortedMap = new LinkedHashMap<BuyBoxModel, Double>();
		for (final Iterator<Map.Entry<BuyBoxModel, Double>> it = priceList.iterator(); it.hasNext();)
		{
			final Map.Entry<BuyBoxModel, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
			buyBoxModel = entry.getKey();
			break;
		}
		return buyBoxModel;
	}





	/**
	 * checking oos for all variants
	 *
	 * @param sortedVariantsList
	 */
	private boolean chcekOutOfStockForAllVariants(final List<PcmProductVariantModel> sortedVariantsList)
	{
		boolean allOutOfStockForAllVariants = true;
		for (final PcmProductVariantModel variant : sortedVariantsList)
		{

			final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(variant.getCode());
			final boolean allOutOfStock = isOutOfStock(buyBoxModelList);
			if (!allOutOfStock)
			{
				allOutOfStockForAllVariants = false;
			}
		}
		return allOutOfStockForAllVariants;
	}

	public List<PcmProductVariantModel> compareVariants(final ProductModel baseProduct,
			final PcmProductVariantModel selectedVariantModel)
	{
		boolean isSizeVariantPresent = false;
		boolean isCapacityVariantPresent = false;
		// String variantColor = "";
		final List<PcmProductVariantModel> pcmProductVariantModelList = new ArrayList<PcmProductVariantModel>();
		final String selectedColor = getVariantColour(selectedVariantModel);
		for (final VariantProductModel variantProductModel : baseProduct.getVariants())
		{

			final PcmProductVariantModel pcmVariantProductModel = (PcmProductVariantModel) variantProductModel;
			final String variantColor = getVariantColour(pcmVariantProductModel);
			if (selectedColor.equalsIgnoreCase(variantColor))
			{
				if (null != pcmVariantProductModel.getSize())
				{
					pcmProductVariantModelList.add(pcmVariantProductModel);
					isSizeVariantPresent = true;
				}
				if (null != pcmVariantProductModel.getCapacity())
				{
					isCapacityVariantPresent = true;
				}
			}
		}
		if (isSizeVariantPresent)
		{
			variantComparator.setVariantType("size");
			Collections.sort(pcmProductVariantModelList, variantComparator);
		}
		if (isCapacityVariantPresent && null != selectedVariantModel.getProductCategoryType()
				&& selectedVariantModel.getProductCategoryType().equalsIgnoreCase("Electronics"))
		{
			variantComparator.setVariantType("capacity");
			Collections.sort(pcmProductVariantModelList, variantComparator);
		}

		return pcmProductVariantModelList;

	}



	public String getInConditionProductCodes(final String productCode, final String queryString)
	{
		if (queryString.isEmpty())
		{
			return MplConstants.SINGLE_QUOTES + productCode + MplConstants.SINGLE_QUOTES;
		}
		else
		{
			return queryString + MplConstants.COMMA + MplConstants.SINGLE_QUOTES + productCode + MplConstants.SINGLE_QUOTES;
		}
	}

	public Converter<VariantProductModel, VariantOptionData> getVariantOptionDataConverter()
	{
		return variantOptionDataConverter;
	}

	@Required
	public void setVariantOptionDataConverter(final Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter)
	{
		this.variantOptionDataConverter = variantOptionDataConverter;
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

	/**
	 * @return the variantComparator
	 */
	public MplVariantComparator getVariantComparator()
	{
		return variantComparator;
	}

	/**
	 * @param variantComparator
	 *           the variantComparator to set
	 */
	public void setVariantSizeComparator(final MplVariantComparator variantComparator)
	{
		this.variantComparator = variantComparator;
	}



	public String getVariantColour(final PcmProductVariantModel variantProductModel)
	{

		String variantColor = "";
		String code = "";

		if (CLOTHING.equalsIgnoreCase(variantProductModel.getProductCategoryType()))
		{
			code = COLORAPPAREL;
		}
		if (ELECTRONICS.equalsIgnoreCase(variantProductModel.getProductCategoryType()))
		{
			code = COLORELECTRONICS;
		}
		else if (FOOTWEAR.equalsIgnoreCase(variantProductModel.getProductCategoryType()))
		{
			code = COLORFAMILYFOOTWEAR;
		}

		if (StringUtils.isNotEmpty(code))
		{
			final List<ClassAttributeAssignmentModel> classAttrAssgnList = buyBoxService.getClassAttrAssignmentsForCode(code);

			final FeatureList featureList = classificationService.getFeatures(variantProductModel, classAttrAssgnList);
			final Iterator itr = featureList.iterator();
			while (itr.hasNext())
			{
				final Feature feature = (Feature) itr.next();
				if (null != feature.getValue())
				{
					variantColor = (String) feature.getValue().getValue();
					break;
				}
			}

		}
		if (StringUtils.isEmpty(variantColor))
		{
			variantColor = variantProductModel.getColour() != null ? variantProductModel.getColour() : "";
		}
		return variantColor;
	}

	/**
	 * get least price corresponding to a product(oos products will not come into account)
	 *
	 * @param buyBoxModelList
	 * @param priceValueMap
	 * @return priceValueMap
	 */
	private Map<BuyBoxModel, Double> getLeastPriceModel(final List<BuyBoxModel> buyBoxModelList,
			final Map<BuyBoxModel, Double> priceValueMap)
	{

		if (buyBoxModelList != null)
		{
			for (final BuyBoxModel buyBox : buyBoxModelList)

			{
				if (buyBox.getAvailable().intValue() > 0)
				{
					if (null != buyBox.getSpecialPrice() && buyBox.getSpecialPrice().doubleValue() > 0.0)
					{
						priceValueMap.put(buyBox, buyBox.getSpecialPrice());
					}
					else if (null != buyBox.getPrice() && buyBox.getPrice().doubleValue() > 0.0)
					{
						priceValueMap.put(buyBox, buyBox.getPrice());
					}
					else
					{
						priceValueMap.put(buyBox, buyBox.getMrp());
					}
				}

			}
		}
		return priceValueMap;

	}


	/**
	 * getting all the price values for all out of stock product
	 *
	 * @param buyBoxModelList
	 * @param priceValueMap
	 * @return priceValueMap
	 */
	private Map<BuyBoxModel, Double> getLeastPriceModelForOutOfStockProduct(final List<BuyBoxModel> buyBoxModelList,
			final Map<BuyBoxModel, Double> priceValueMap)
	{

		if (buyBoxModelList != null)
		{
			for (final BuyBoxModel buyBox : buyBoxModelList)

			{
				if (null != buyBox.getSpecialPrice() && buyBox.getSpecialPrice().doubleValue() > 0.0)
				{
					priceValueMap.put(buyBox, buyBox.getSpecialPrice());
				}
				else if (null != buyBox.getPrice() && buyBox.getPrice().doubleValue() > 0.0)
				{
					priceValueMap.put(buyBox, buyBox.getPrice());
				}
				else
				{
					priceValueMap.put(buyBox, buyBox.getMrp());
				}


			}
		}
		return priceValueMap;

	}

	/**
	 * checking all the sellers corresponding to a product is oos or not
	 *
	 * @param buyBoxModelList
	 * @return isOutOfStock
	 */

	public boolean isOutOfStock(final List<BuyBoxModel> buyBoxModelList)
	{

		boolean isOutOfStock = true;

		if (null != buyBoxModelList)
		{
			for (final BuyBoxModel buyBox : buyBoxModelList)
			{
				if (buyBox.getAvailable().intValue() > 0)
				{
					isOutOfStock = false;
					break;
				}

			}

		}

		return isOutOfStock;

	}

	public BuyBoxModel getLeastPriceBuyBoxModel(final ProductModel productModel)
	{
		// YTODO Auto-generated method stub
		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		return buyBoxWinnerModel;
	}

	/**
	 * @return the classificationService
	 */
	public ClassificationService getClassificationService()
	{
		return classificationService;
	}

	/**
	 * @param classificationService
	 *           the classificationService to set
	 */
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

	/**
	 * @param productModel
	 * @return
	 */
	public Double getBuyBoxSellingMobilePrice(final ProductModel productModel)
	{
		// YTODO Auto-generated method stub

		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		Double mobileprice = Double.valueOf(0);
		if (buyBoxWinnerModel != null)
		{
			mobileprice = buyBoxWinnerModel.getPrice();



			if (null != buyBoxWinnerModel.getSpecialPriceMobile() && buyBoxWinnerModel.getSpecialPriceMobile().intValue() > 0)
			{
				mobileprice = buyBoxWinnerModel.getSpecialPriceMobile();
			}
		}
		else
		{
			mobileprice = productModel.getMrp();

		}
		LOG.debug("Inside MplBuyBoxUtility price value is" + mobileprice);
		return mobileprice;

	}

	/*
	 * @param sellerArticleSKUList
	 *
	 * @param currency
	 *
	 * @return priceRange
	 */

	//	public String getBuyBoxSellingVariantsPrice(final List<String> sellerArticleSKUList, final String currency)
	//			throws EtailNonBusinessExceptions
	public String getBuyBoxSellingVariantsPrice(final List<BuyBoxModel> modifiableBuyBox, final String currency)
			throws EtailNonBusinessExceptions
	{
		Double min = Double.valueOf(0);
		Double max = Double.valueOf(0);

		if (CollectionUtils.isNotEmpty(modifiableBuyBox))
		{
			min = modifiableBuyBox.get(modifiableBuyBox.size() - 1).getPrice();
			max = modifiableBuyBox.get(0).getPrice();
		}

		//final List<BuyBoxModel> listBuyBox = buyBoxService.getBuyboxSellerPricesForSearch(sellerArticleSKUList);
		//		if (CollectionUtils.isNotEmpty(listBuyBox))
		//		{
		//			if (listBuyBox.get(0).getPLPMinPrice() != null && listBuyBox.get(0).getPLPMaxPrice() != null)
		//			{
		//				min = listBuyBox.get(0).getPLPMinPrice();
		//				max = listBuyBox.get(0).getPLPMaxPrice();
		//			}
		//		}
		String priceRange = null;
		if (min.doubleValue() > 0 && max.doubleValue() > 0)
		{
			if (min.doubleValue() == max.doubleValue())
			{
				//priceRange = currency + String.valueOf(min);//SONAR FIX JEWELLERY
				priceRange = currency + Math.round(min.doubleValue());
			}
			else
			{
				//	priceRange = currency + String.valueOf(min) + HYPHEN + currency + String.valueOf(max);//SONAR FIX JEWELLERY
				priceRange = currency + Math.round(min.doubleValue()) + HYPHEN + currency + Math.round(max.doubleValue());
			}
		}
		return priceRange;
	}

	public List<BuyBoxModel> getVariantListForPriceRange(final String code)
	{
		return buyBoxDao.getVariantListForPriceRange(code);
	}
}
