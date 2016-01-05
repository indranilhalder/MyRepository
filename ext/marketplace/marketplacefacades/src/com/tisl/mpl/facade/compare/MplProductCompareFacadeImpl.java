/**
 *
 */
package com.tisl.mpl.facade.compare;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.product.ProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facades.product.data.CompareData;
import com.tisl.mpl.facades.product.data.CompareValueData;
import com.tisl.mpl.facades.product.data.ProductBasicCompareData;
import com.tisl.mpl.facades.product.data.ProductCompareData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;


/**
 * @author 584443
 *
 */
public class MplProductCompareFacadeImpl implements MplProductCompareFacade
{

	private static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.VARIANT_FULL, ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.CLASSIFICATION,
			ProductOption.CATEGORIES);

	private ProductService productService;

	private ProductFacade productFacade;

	private BuyBoxFacade buyBoxFacade;


	/**
	 * @return the buyBoxFacade
	 */
	public BuyBoxFacade getBuyBoxFacade()
	{
		return buyBoxFacade;
	}

	/**
	 * @param buyBoxFacade
	 *           the buyBoxFacade to set
	 */
	public void setBuyBoxFacade(final BuyBoxFacade buyBoxFacade)
	{
		this.buyBoxFacade = buyBoxFacade;
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.compare.MplProductCompareFacade#getWebServicesForProductCompare(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ProductCompareData getWebServicesForProductCompare(final String firstProductCode, final String secondProductCode)
	{

		final ProductData firstProductData = getProductFacade()
				.getProductForOptions(getProductService().getProductForCode(firstProductCode), PRODUCT_OPTIONS);

		final ProductData secondProductData = getProductFacade()
				.getProductForOptions(getProductService().getProductForCode(secondProductCode), PRODUCT_OPTIONS);

		final List<ProductData> compareList = new ArrayList<ProductData>();
		compareList.add(firstProductData);
		compareList.add(secondProductData);


		final ProductCompareData productCompareData = populateBasicInfo(firstProductData, secondProductData);

		//final ProductCompareData productCompareData = new ProductCompareData();

		final List<CompareData> compareDatas = new ArrayList<CompareData>();
		if (null != firstProductData && null != secondProductCode && null != firstProductData.getClassifications()
				&& null != secondProductData.getClassifications())
		{
			final List<String> distinctKeyList = new ArrayList<String>();
			for (final ClassificationData firstClassificationData : firstProductData.getClassifications())
			{
				for (final ClassificationData secondClassificationData : secondProductData.getClassifications())
				{
					// Deeply nested if..then statements are hard to read
					//					if (firstClassificationData.getCode().equals(secondClassificationData.getCode()))
					//					{
					//						if (null != firstClassificationData.getName() && !firstClassificationData.getName().equalsIgnoreCase("NA")
					//								&& !distinctKeyList.contains(firstClassificationData.getName()))
					//						{
					//							distinctKeyList.add(firstClassificationData.getName());
					//						}
					//					}

					if (firstClassificationData.getCode().equals(secondClassificationData.getCode())
							&& null != firstClassificationData.getName() && !firstClassificationData.getName().equalsIgnoreCase("NA")
							&& !distinctKeyList.contains(firstClassificationData.getName()))
					{
						distinctKeyList.add(firstClassificationData.getName());
					}

				}
			}
			for (final String distinctKey : distinctKeyList)
			{
				final CompareData compareData = new CompareData();
				compareData.setKey(distinctKey);
				final List<CompareValueData> compareValueDatas = new ArrayList<CompareValueData>();
				for (final ClassificationData firstClassificationData : firstProductData.getClassifications())
				{
					for (final ClassificationData secondClassificationData : secondProductData.getClassifications())
					{
						if (firstClassificationData.getCode().equals(secondClassificationData.getCode())
								&& firstClassificationData.getName().equalsIgnoreCase(distinctKey))
						{
							//final List<CompareValueData> compareValueDatas = new ArrayList<CompareValueData>();
							//final CompareData compareData = new CompareData();
							//compareData.setKey(firstClassificationData.getName());
							for (final FeatureData firstFeatureData : firstClassificationData.getFeatures())
							{
								//final List<CompareValueData> compareValueDatas = new ArrayList<CompareValueData>();
								for (final FeatureData secondFeatureData : secondClassificationData.getFeatures())
								{
									if (firstFeatureData.getCode().equals(secondFeatureData.getCode()))
									{
										final CompareValueData compareValueData = new CompareValueData();
										compareValueData.setSubKey(firstFeatureData.getName());
										if (firstFeatureData.getFeatureValues().size() > 0)
										{
											for (final FeatureValueData firstFeatureValue : firstFeatureData.getFeatureValues())
											{
												if (!StringUtils.isEmpty(firstFeatureValue.getValue()))
												{
													final StringBuilder builder = new StringBuilder(firstFeatureValue.getValue())
															.append(firstFeatureData.getFeatureUnit() != null
																	? firstFeatureData.getFeatureUnit().getSymbol() : "");
													compareValueData.setProduct1Value(builder.toString());
													continue;
												}

											}
										}
										else
										{
											compareValueData.setProduct1Value("NA");
										}
										//private method created for sonar fix
										setSecondFeatureValue(secondFeatureData, compareValueData, compareValueDatas);
										//										if (secondFeatureData.getFeatureValues().size() > 0)
										//										{
										//											for (final FeatureValueData secondFeatureValue : secondFeatureData.getFeatureValues())
										//											{
										//												if (!StringUtils.isEmpty(secondFeatureValue.getValue()))
										//												{
										//													final StringBuilder builder = new StringBuilder(secondFeatureValue.getValue())
										//															.append(secondFeatureData.getFeatureUnit() != null ? secondFeatureData
										//																	.getFeatureUnit().getSymbol() : "");
										//													compareValueData.setProduct2Value(builder.toString());
										//													continue;
										//												}
										//											}
										//										}
										//										else
										//										{
										//											compareValueData.setProduct2Value("NA");
										//										}
										//										compareValueDatas.add(compareValueData);
									}
								}
								compareData.setValue(compareValueDatas);
							}
							//compareDatas.add(compareData);
						}
					}
				}
				compareDatas.add(compareData);
			}

			productCompareData.setComparisions(compareDatas);
			productCompareData.setStatus("Comparision successful");
		}
		//		else
		//		{
		//			productCompareData.setStatus("Comparision of product not possible. Products do not have classification attribute");
		//		}


		return productCompareData;
	}


	public ProductCompareData populateBasicInfo(final ProductData firstProductData, final ProductData secondProductData)
	{
		final ProductCompareData productCompareData = new ProductCompareData();
		final Map<String, ProductBasicCompareData> basicProductAttributeMap = new HashMap();

		basicProductAttributeMap.put("Code", populateCompareValues("Code", firstProductData, secondProductData));
		basicProductAttributeMap.put("Name", populateCompareValues("Name", firstProductData, secondProductData));
		basicProductAttributeMap.put("Brand", populateCompareValues("Brand", firstProductData, secondProductData));
		basicProductAttributeMap.put("Price", populateCompareValues("Price", firstProductData, secondProductData));
		basicProductAttributeMap.put("MRP", populateCompareValues("MRP", firstProductData, secondProductData));
		basicProductAttributeMap.put("Description", populateCompareValues("Desc", firstProductData, secondProductData));
		basicProductAttributeMap.put("Rating", populateCompareValues("Rating", firstProductData, secondProductData));
		basicProductAttributeMap.put("ImageURL", populateCompareValues("ImageURL", firstProductData, secondProductData));

		productCompareData.setBasicAttributes(basicProductAttributeMap);
		return productCompareData;

	}

	public ProductBasicCompareData populateCompareValues(final String qualifier, final ProductData firstProductData,
			final ProductData secondProductData)
	{
		ProductBasicCompareData basicCompareData = null;

		//if (qualifier.equals("Code")) Critical Sonar fixes
		if (("Code").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			basicCompareData.setProduct1Value(firstProductData.getCode());
			basicCompareData.setProduct2Value(secondProductData.getCode());
			return basicCompareData;
		}

		//if (qualifier.equals("Name"))  Critical Sonar fixes
		if (("Name").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			basicCompareData.setProduct1Value(firstProductData.getName());
			basicCompareData.setProduct2Value(secondProductData.getName());
			return basicCompareData;
		}

		//if (qualifier.equals("Brand")) Critical Sonar fixes
		if (("Brand").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			basicCompareData.setProduct1Value(firstProductData.getBrand() != null ? firstProductData.getBrand().getBrandname() : "");
			basicCompareData
					.setProduct2Value(secondProductData.getBrand() != null ? secondProductData.getBrand().getBrandname() : "");
			return basicCompareData;
		}
		//if (qualifier.equals("Price")) Critical Sonar fixes
		if (("Price").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			final BuyBoxData firstBuyboxData = getBuyBoxFacade().buyboxPrice(firstProductData.getCode());
			final BuyBoxData secondBuyboxData = getBuyBoxFacade().buyboxPrice(secondProductData.getCode());
			final PriceData firstPrice = getPriceForBuyBox(firstBuyboxData);
			final PriceData secondPrice = getPriceForBuyBox(secondBuyboxData);
			basicCompareData.setProduct1Value(
					firstPrice != null ? firstPrice.getFormattedValue() : MarketplacecommerceservicesConstants.PRICE_NOT_AVAILABLE);
			basicCompareData.setProduct2Value(
					secondPrice != null ? secondPrice.getFormattedValue() : MarketplacecommerceservicesConstants.PRICE_NOT_AVAILABLE);
			return basicCompareData;
		}

		if (("MRP").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			final BuyBoxData firstBuyboxData = getBuyBoxFacade().buyboxPrice(firstProductData.getCode());
			final BuyBoxData secondBuyboxData = getBuyBoxFacade().buyboxPrice(secondProductData.getCode());
			final PriceData firstPrice = getMrpPriceForBuyBox(firstBuyboxData);
			final PriceData secondPrice = getMrpPriceForBuyBox(secondBuyboxData);
			basicCompareData.setProduct1Value(
					firstPrice != null ? firstPrice.getFormattedValue() : MarketplacecommerceservicesConstants.PRICE_NOT_AVAILABLE);
			basicCompareData.setProduct2Value(
					secondPrice != null ? secondPrice.getFormattedValue() : MarketplacecommerceservicesConstants.PRICE_NOT_AVAILABLE);
			return basicCompareData;
		}
		//if (qualifier.equals("Desc")) Critical Sonar fixes
		if (("Desc").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			basicCompareData.setProduct1Value(firstProductData.getDescription());
			basicCompareData.setProduct2Value(secondProductData.getDescription());
			return basicCompareData;
		}
		//if (qualifier.equals("Rating")) Critical Sonar fixes
		if (("Rating").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			basicCompareData.setProduct1Value(
					firstProductData.getAverageRating() != null ? firstProductData.getAverageRating().toString() : "");
			basicCompareData.setProduct2Value(
					secondProductData.getAverageRating() != null ? secondProductData.getAverageRating().toString() : "");
			return basicCompareData;
		}

		if (("ImageURL").equals(qualifier))
		{
			basicCompareData = new ProductBasicCompareData();
			final ImageData imgData1 = getPrimaryImageForProductAndFormat(firstProductData, "searchPage");
			final ImageData imgData2 = getPrimaryImageForProductAndFormat(secondProductData, "searchPage");
			if (null != imgData1 && null != imgData1.getUrl())
			{
				basicCompareData.setProduct1Value(imgData1.getUrl());
			}
			else
			{
				basicCompareData.setProduct1Value("");
			}
			if (null != imgData2 && null != imgData2.getUrl())
			{
				basicCompareData.setProduct2Value(imgData2.getUrl());
			}
			else
			{
				basicCompareData.setProduct2Value("");
			}
			return basicCompareData;
		}
		return null;
	}

	public PriceData getPriceForBuyBox(final BuyBoxData buyBoxData)
	{
		if (buyBoxData != null)
		{
			if (buyBoxData.getSpecialPrice() != null)
			{
				return buyBoxData.getSpecialPrice();
			}
			else if (buyBoxData.getPrice() != null)
			{
				return buyBoxData.getPrice();
			}
			else
			{
				return buyBoxData.getMrp();
			}
		}
		return null;
	}

	public PriceData getMrpPriceForBuyBox(final BuyBoxData buyBoxData)
	{
		if (buyBoxData != null)
		{
			return buyBoxData.getMrp();
		}
		return null;
	}


	/**
	 * @param secondFeatureData
	 * @param compareValueData
	 * @param compareValueDatas
	 */
	private List<CompareValueData> setSecondFeatureValue(final FeatureData secondFeatureData,
			final CompareValueData compareValueData, final List<CompareValueData> compareValueDatas)
	{
		if (secondFeatureData.getFeatureValues().size() > 0)
		{
			for (final FeatureValueData secondFeatureValue : secondFeatureData.getFeatureValues())
			{
				if (!StringUtils.isEmpty(secondFeatureValue.getValue()))
				{
					final StringBuilder builder = new StringBuilder(secondFeatureValue.getValue())
							.append(secondFeatureData.getFeatureUnit() != null ? secondFeatureData.getFeatureUnit().getSymbol() : "");
					compareValueData.setProduct2Value(builder.toString());
					continue;
				}
			}
		}
		else
		{
			compareValueData.setProduct2Value("NA");
		}
		compareValueDatas.add(compareValueData);
		return compareValueDatas;

	}

	private ImageData getPrimaryImageForProductAndFormat(final ProductData product, final String format)
	{
		if (product != null && format != null)
		{
			final Collection<ImageData> images = product.getImages();
			if (images != null && !images.isEmpty())
			{
				for (final ImageData image : images)
				{
					if (ImageDataType.PRIMARY.equals(image.getImageType()) && format.equals(image.getFormat()))
					{
						return image;
					}
				}
			}
		}
		return null;
	}

}
