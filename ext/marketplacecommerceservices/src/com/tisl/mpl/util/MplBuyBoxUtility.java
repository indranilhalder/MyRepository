/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.catalog.model.ProductFeatureModel;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;


/**
 * @author TCS
 *
 */
public class MplBuyBoxUtility
{
	/* private static final Logger LOG = Logger.getLogger(MplBuyBoxUtility.class); */

	//---------------Solve for Issue TISPRD-58---------------------//
	private static final String COLORAPPAREL = "colorapparel";
	public static final String CLOTHING = "Clothing";
	private static final String ELECTRONICS = "electronics";
	private static final String FOOTWEAR = "footwear";
	private static final String COLORFAMILYFOOTWEAR = "brandcolorfootwear";
	private static final String COLORELECTRONICS = "colorelectronics";

	//---------------Solve for Issue TISPRD-58---------------------//


	private BuyBoxService buyBoxService;

	@Autowired
	private MplVariantComparator variantComparator;

	private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;

	public Double getBuyBoxSellingPrice(final ProductModel productModel) throws EtailNonBusinessExceptions
	{
		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		Double price = Double.valueOf(0);
		if (buyBoxWinnerModel != null)
		{
			price = buyBoxWinnerModel.getPrice();

			if (null != buyBoxWinnerModel.getSpecialPrice() && buyBoxWinnerModel.getSpecialPrice().intValue() > 0)
			{
				price = buyBoxWinnerModel.getSpecialPrice();
			}
		}
		else
		{
			price = productModel.getMrp();
		}
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
				priceValueMap = getLeastPriceModel(buyBoxModelList, priceValueMap);
			}
			finalpriceValueMap.putAll(priceValueMap);
		}
		else
		{
			for (final PcmProductVariantModel productVariant : sortedVariantsList)
			{
				final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productVariant.getCode());
				priceValueMap = getLeastPriceModel(buyBoxModelList, priceValueMap);
				finalpriceValueMap.putAll(priceValueMap);
			}
		}
		//TISPRD-1079
		if (finalpriceValueMap.isEmpty())
		{
			final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productCode);
			priceValueMap = getLeastPriceModel(buyBoxModelList, priceValueMap);
			finalpriceValueMap.putAll(priceValueMap);
		}
		final List<Map.Entry<BuyBoxModel, Double>> priceList = new LinkedList<Map.Entry<BuyBoxModel, Double>>(
				finalpriceValueMap.entrySet());
		Collections.sort(priceList, new Comparator<Map.Entry<BuyBoxModel, Double>>()
		{
			@Override
			public int compare(final Map.Entry<BuyBoxModel, Double> o1, final Map.Entry<BuyBoxModel, Double> o2)
			{
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
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


	private Map<BuyBoxModel, Double> getLeastPriceModel(final List<BuyBoxModel> buyBoxModelList,
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
				else if (null != buyBox.getSpecialPrice() && buyBox.getPrice().doubleValue() > 0.0)
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


	public List<PcmProductVariantModel> compareVariants(final ProductModel baseProduct,
			final PcmProductVariantModel selectedVariantModel)
	{
		boolean isSizeVariantPresent = false;
		boolean isCapacityVariantPresent = false;
		// String variantColor = "";
		final List<PcmProductVariantModel> pcmProductVariantModelList = new ArrayList<PcmProductVariantModel>();
		final String selectedColor = getVariantColour(selectedVariantModel, selectedVariantModel.getFeatures());
		for (final VariantProductModel variantProductModel : baseProduct.getVariants())
		{

			final PcmProductVariantModel pcmVariantProductModel = (PcmProductVariantModel) variantProductModel;
			final String variantColor = getVariantColour(pcmVariantProductModel, pcmVariantProductModel.getFeatures());
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



	public String getVariantColour(final PcmProductVariantModel variantProductModel, final List<ProductFeatureModel> features)
	{
		String variantColor = "";
		for (final ProductFeatureModel productFeature : features)
		{

			if (CLOTHING.equals(variantProductModel.getProductCategoryType()))
			{
				if (null != productFeature.getClassificationAttributeAssignment()
						&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
						&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
								.equalsIgnoreCase(COLORAPPAREL))

				{
					if (null != productFeature.getValue())
					{
						variantColor = productFeature.getValue().toString();
						break;
					}
				}

			}
			if (ELECTRONICS.equals(variantProductModel.getProductCategoryType()))
			{
				if (null != productFeature.getClassificationAttributeAssignment()
						&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
						&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
								.equalsIgnoreCase(COLORELECTRONICS))

				{
					if (null != productFeature.getValue())
					{
						variantColor = productFeature.getValue().toString();
						break;
					}
				}

			}
			else if (FOOTWEAR.equals(variantProductModel.getProductCategoryType()))
			{
				if (null != productFeature.getClassificationAttributeAssignment()
						&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
						&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
								.equalsIgnoreCase(COLORFAMILYFOOTWEAR))

				{
					if (null != productFeature.getValue())
					{
						variantColor = productFeature.getValue().toString();
						break;
					}

				}
			}

		}
		if (StringUtils.isEmpty(variantColor))
		{
			variantColor = variantProductModel.getColour() != null ? variantProductModel.getColour() : "";
		}
		return variantColor;
	}
}
