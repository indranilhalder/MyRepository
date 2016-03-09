/**
 *
 */
package com.tisl.mpl.util;

//import de.hybris.platform.catalog.model.ProductFeatureModel;
//import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
		price = buyBoxWinnerModel.getPrice();

		if (buyBoxWinnerModel.getSpecialPrice() != null && buyBoxWinnerModel.getSpecialPrice().intValue() > 0)
		{
			price = buyBoxWinnerModel.getSpecialPrice();
		}
		return price;
	}

	public Double getBuyBoxMrpPrice(final ProductModel productModel) throws EtailNonBusinessExceptions
	{
		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		Double mrpPrice = Double.valueOf(0);
		mrpPrice = buyBoxWinnerModel.getMrp();
		return mrpPrice;
	}

	public String getLeastSizeProduct(final ProductModel productModel) throws EtailNonBusinessExceptions
	{
		final BuyBoxModel buyBoxWinnerModel = getBuyBoxPrice(productModel);
		return buyBoxWinnerModel.getProduct();
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

		}

		//Get simple product code other than variant
		//if (sortedVariantsList.size() == 0)
		if (CollectionUtils.isEmpty(sortedVariantsList))
		{
			productCode = getInConditionProductCodes(productModel.getCode(), "");
		}

		//Get buyboxmodel for products from
		final List<BuyBoxModel> buyBoxModelList = buyBoxService.getBuyboxPricesForSearch(productCode);

		//Get buybox map
		final Map<String, List<BuyBoxModel>> buyBoxMap = getBuyBoxMap(buyBoxModelList);

		final BuyBoxModel buyBoxWinnerModel = getBuyBoxWinnerModel(buyBoxModelList, sortedVariantsList, buyBoxMap);

		return buyBoxWinnerModel;
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

				pcmProductVariantModelList.add(pcmVariantProductModel);

				if (null != pcmVariantProductModel.getSize())
				{
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
		if (isCapacityVariantPresent && selectedVariantModel.getProductCategoryType().equalsIgnoreCase("Electronics"))
		{
			variantComparator.setVariantType("capacity");
			Collections.sort(pcmProductVariantModelList, variantComparator);
		}

		return pcmProductVariantModelList;

	}

	public BuyBoxModel getBuyBoxWinnerModel(final List<BuyBoxModel> buyBoxModelList,
			final List<PcmProductVariantModel> sortedVariantsList, final Map<String, List<BuyBoxModel>> buyBoxMap)
			throws EtailNonBusinessExceptions
	{


		BuyBoxModel buyBoxWinnerModel = new BuyBoxModel();

		//if (sortedVariantsList.size() > 0)
		if (CollectionUtils.isNotEmpty(sortedVariantsList))
		{
			buyBoxWinnerModel = getLeastVariantSizeModel(sortedVariantsList, buyBoxMap);
		}
		else
		{
			// BuyBox winner Price Model
			buyBoxWinnerModel = buyBoxModelList.get(0);
			for (final BuyBoxModel buyBox : buyBoxModelList)
			{
				if (buyBox.getAvailable().intValue() > 0)
				{
					buyBoxWinnerModel = buyBox;
					break;
				}
			}
		}

		return buyBoxWinnerModel;
	}

	public BuyBoxModel getLeastVariantSizeModel(final List<PcmProductVariantModel> sortedVariantsList,
			final Map<String, List<BuyBoxModel>> buyBoxMap) throws EtailNonBusinessExceptions
	{
		int leastVariant = 0;
		boolean isAllOutofStock = false;
		BuyBoxModel buyBoxWinnerModel = new BuyBoxModel();
		BuyBoxModel leastSizebuyBoxWithoutStock = new BuyBoxModel();
		// Iterate VariantSize product code and identify least size price.
		for (final PcmProductVariantModel variantOptionData : sortedVariantsList)
		{
			final List<BuyBoxModel> productBBModelList = buyBoxMap.get(variantOptionData.getCode());
			//if (productBBModelList != null && productBBModelList.size() > 0)
			if (CollectionUtils.isNotEmpty(productBBModelList))
			{
				if (leastVariant == 0)
				{
					// BuyBox winner Price Model
					buyBoxWinnerModel = productBBModelList.get(0);
				}
				boolean isInStock = false;
				for (final BuyBoxModel buyBox : productBBModelList)
				{
					if (buyBox.getAvailable().intValue() > 0)
					{
						buyBoxWinnerModel = buyBox;
						isAllOutofStock = true;
						isInStock = true;
						break;
					}
				}

				if (!isInStock && leastVariant == 0)
				{
					leastSizebuyBoxWithoutStock = productBBModelList.get(0);
				}
				leastVariant = leastVariant + 1;

			}

			if (!isAllOutofStock)
			{
				buyBoxWinnerModel = leastSizebuyBoxWithoutStock;
			}
			else
			{
				break;//break the loop as we find instock buybox model
			}
		}
		return buyBoxWinnerModel;
	}

	public Map getBuyBoxMap(final List<BuyBoxModel> buyBoxModelList) throws EtailNonBusinessExceptions
	{
		final Map<String, List<BuyBoxModel>> buyBoxMap = new HashMap<String, List<BuyBoxModel>>();

		// Iterate BuyBox results and add into Map<ProductCode,List<BuyBoxModel>> format
		//if (buyBoxModelList != null && buyBoxModelList.size() > 0)
		if (CollectionUtils.isNotEmpty(buyBoxModelList))
		{
			for (final BuyBoxModel buyBoxModel : buyBoxModelList)
			{
				List<BuyBoxModel> bbList = new ArrayList<BuyBoxModel>();
				if (null != buyBoxMap.get(buyBoxModel.getProduct()))
				{
					bbList = buyBoxMap.get(buyBoxModel.getProduct());
					bbList.add(buyBoxModel);
				}
				else
				{
					bbList.add(buyBoxModel);
				}
				buyBoxMap.put(buyBoxModel.getProduct(), bbList);
			}

		}
		return buyBoxMap;
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
			if (variantProductModel.getProductCategoryType().equals(CLOTHING))
			{
				if (null != productFeature.getClassificationAttributeAssignment()
						&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
						&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
								.equalsIgnoreCase(COLORAPPAREL))

				{
					variantColor = productFeature.getValue().toString();
					break;
				}

			}
			else if (variantProductModel.getProductCategoryType().equals(ELECTRONICS))
			{
				if (null != productFeature.getClassificationAttributeAssignment()
						&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
						&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
								.equalsIgnoreCase(COLORELECTRONICS))

				{
					variantColor = productFeature.getValue().toString();
					break;
				}

			}
			else if (variantProductModel.getProductCategoryType().equals(FOOTWEAR))
			{
				if (null != productFeature.getClassificationAttributeAssignment()
						&& null != productFeature.getClassificationAttributeAssignment().getClassificationAttribute()
						&& productFeature.getClassificationAttributeAssignment().getClassificationAttribute().getCode()
								.equalsIgnoreCase(COLORFAMILYFOOTWEAR))

				{
					variantColor = productFeature.getValue().toString();
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
}
