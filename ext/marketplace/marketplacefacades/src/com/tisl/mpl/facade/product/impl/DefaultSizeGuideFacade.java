/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.comparator.SizeGuideComparator;
import com.tisl.mpl.facade.product.SizeGuideFacade;
import com.tisl.mpl.facades.product.data.SizeGuideData;
import com.tisl.mpl.marketplacecommerceservices.service.SizeGuideService;
import com.tisl.mpl.wsdto.SizeGuideWsDTO;
import com.tisl.mpl.wsdto.SizeGuideWsData;
import com.tisl.mpl.wsdto.SizeGuideWsDataValue;


/**
 * @author TCS
 *
 */
public class DefaultSizeGuideFacade implements SizeGuideFacade
{
	/**
	 *
	 */
	private static final String FOOTWEAR = "Footwear";

	/**
	 *
	 */
	/* SONAR FIX JEWELLERY */
	//private static final String CLOTHING = "Clothing";

	/**
	 * Added FashionAccessories START ::::
	 *
	 */
	private static final String ACCESSORIES = "Accessories";

	@Resource
	private SizeGuideService sizeGuideService;

	@Resource
	private Converter<SizeGuideModel, SizeGuideData> sizeGuideConverter;

	@Resource(name = "sizeGuideComparator")
	private SizeGuideComparator sizeGuideComparator;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	/**
	 * @description It is used for fetching all distinct Size Guides of an online product
	 * @param productCode
	 * @return list of SizeGuideData
	 */
	@Override
	public Map<String, List<SizeGuideData>> getProductSizeguide(final String productCode, final String categoryType)
			throws CMSItemNotFoundException
	{
		List<SizeGuideModel> sizeGuideModels = null; //changed to null
		List<SizeGuideData> sizeDataValues = null;
		SizeGuideData sizeGuideData = null;
		final TreeMap<String, List<SizeGuideData>> sizeGuideDatas = new TreeMap<String, List<SizeGuideData>>();
		final TreeMap<String, List<SizeGuideData>> sizeGuideSortedDatas = new TreeMap<String, List<SizeGuideData>>();
		final List<SizeGuideData> sizeGuideDataListForFootwear = new ArrayList<SizeGuideData>();
		try
		{
			sizeGuideModels = sizeGuideService.getProductSizeGuideList(productCode);
			if (sizeGuideModels != null)
			{
				for (final SizeGuideModel sizeGuideModel : sizeGuideModels)
				{
					//convertor is added
					sizeGuideData = getSizeGuideConverter().convert(sizeGuideModel);
					if (categoryType.equalsIgnoreCase(FOOTWEAR) || categoryType.equalsIgnoreCase(ACCESSORIES))
					{
						sizeGuideDataListForFootwear.add(sizeGuideData);
					}
					else
					{
						addToMap(sizeGuideDatas, sizeGuideModel.getDimension(), sizeGuideData);
					}
				}
			}
			/* sorting the Size guide map based on dimension */

			if (categoryType.equalsIgnoreCase(FOOTWEAR) || categoryType.equalsIgnoreCase(ACCESSORIES))
			{
				Collections.sort(sizeGuideDataListForFootwear, sizeGuideComparator);
				sizeGuideSortedDatas.put(productCode, sizeGuideDataListForFootwear);
			}
			else
			{
				for (final String key : sizeGuideDatas.keySet())
				{
					sizeDataValues = sizeGuideDatas.get(key);
					Collections.sort(sizeDataValues, sizeGuideComparator);
					sizeGuideSortedDatas.put(key, sizeDataValues);
				}

			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return sizeGuideSortedDatas;
	}

	/**
	 * @description It is used for fetching all distinct sizes of an online product
	 * @param productCode
	 * @return list of SizeGuideData
	 */
	@Override
	public SizeGuideWsDTO getWSProductSizeguide(final String productCode) throws CMSItemNotFoundException
	{
		final SizeGuideWsDTO sizeDTO = new SizeGuideWsDTO();
		List<SizeGuideWsData> sizeGuideDataList = null;
		List<SizeGuideWsDataValue> sizeGuideDataValueList = null;
		String ImageURL = "";
		SizeGuideWsData sizeGuideData = null;
		SizeGuideWsDataValue sizeGuideWsDataValue = null;
		List<SizeGuideData> sizeDataValues = new ArrayList<>();

		try
		{
			ProductModel productModel = null;
			ProductData productData = null;
			if (StringUtils.isNotEmpty(productCode))
			{
				productModel = productService.getProductForCode(productCode);
			}
			if (null != productModel)
			{
				productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC,
						ProductOption.SELLER, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
						ProductOption.GALLERY, ProductOption.PROMOTIONS, ProductOption.VARIANT_FULL, ProductOption.CLASSIFICATION));
			}

			Map<String, List<SizeGuideData>> sizeGuideDatas = null;
			/* TISMOBQ-42 */
			if (StringUtils.isNotEmpty(productCode) && null != productData && StringUtils.isNotEmpty(productData.getRootCategory()))
			{
				sizeGuideDatas = getProductSizeguide(productCode, productData.getRootCategory());
			}

			/* Converting the SizeGuide Model to SizeGuide Web service Data transaction */
			if (null != sizeGuideDatas)
			{
				sizeGuideDataList = new ArrayList<SizeGuideWsData>();
				for (final String dimension : sizeGuideDatas.keySet())
				{
					sizeGuideDataValueList = new ArrayList<SizeGuideWsDataValue>();
					sizeGuideData = new SizeGuideWsData();
					//// move declaration out of for loop
					//line deleted
					sizeGuideData.setDimension(dimension);
					//// move declaration out of for loop
					sizeDataValues = sizeGuideDatas.get(dimension); //initialization moved out of for loop
					for (final SizeGuideData sizeGuideValue : sizeDataValues)
					{
						sizeGuideWsDataValue = new SizeGuideWsDataValue();
						if (StringUtils.isNotEmpty(sizeGuideValue.getDimensionValue()))
						{
							sizeGuideWsDataValue.setDimensionValue(sizeGuideValue.getDimensionValue());
						}
						if (StringUtils.isNotEmpty(sizeGuideValue.getDimensionUnit()))
						{
							sizeGuideWsDataValue.setDimensionUnit(sizeGuideValue.getDimensionUnit());
						}
						if (StringUtils.isNotEmpty(sizeGuideValue.getDimensionSize()))
						{
							sizeGuideWsDataValue.setDimensionSize(sizeGuideValue.getDimensionSize());

						}
						if (StringUtils.isNotEmpty(sizeGuideValue.getAge()))

						{
							sizeGuideWsDataValue.setAge(sizeGuideValue.getAge());

						}
						if (StringUtils.isNotEmpty(sizeGuideValue.getEuroSize()))
						{
							sizeGuideWsDataValue.setEuroSize(sizeGuideValue.getEuroSize());

						}
						if (StringUtils.isNotEmpty(sizeGuideValue.getUsSize()))

						{
							sizeGuideWsDataValue.setUsSize(sizeGuideValue.getUsSize());

						}
						if (productData.getRootCategory().equalsIgnoreCase(FOOTWEAR)
								&& StringUtils.isNotEmpty(sizeGuideValue.getDimension()))
						{
							sizeGuideWsDataValue.setFootlength(sizeGuideValue.getDimension());
						}




						/**
						 * Add for Accessories Belt Product START By SAP START::::
						 */
						if (productData.getRootCategory().equalsIgnoreCase(ACCESSORIES))
						{
							if (StringUtils.isNotEmpty(sizeGuideValue.getCmsBeltSize()))
							{
								sizeGuideWsDataValue.setCmsBeltSize(sizeGuideValue.getCmsBeltSize());
							}
							if (StringUtils.isNotEmpty(sizeGuideValue.getCmsWaistSize()))
							{
								sizeGuideWsDataValue.setCmsWaistSize(sizeGuideValue.getCmsWaistSize());
							}
							if (StringUtils.isNotEmpty(sizeGuideValue.getInchesBeltLength()))
							{
								sizeGuideWsDataValue.setInchesBeltLength(sizeGuideValue.getInchesBeltLength());
							}
							if (StringUtils.isNotEmpty(sizeGuideValue.getInchesBeltSize()))
							{
								sizeGuideWsDataValue.setInchesBeltSize(sizeGuideValue.getInchesBeltSize());
							}
							if (StringUtils.isNotEmpty(sizeGuideValue.getInchesWaistSize()))
							{
								sizeGuideWsDataValue.setInchesWaistSize(sizeGuideValue.getInchesWaistSize());
							}
						}


						/**
						 * Add for Accessories Belt Product START By SAP END::::
						 */
						//single image is needed to show per product
						ImageURL = sizeGuideValue.getImageURL();
						sizeGuideDataValueList.add(sizeGuideWsDataValue);

					}
					sizeGuideData.setDimensionList(sizeGuideDataValueList);

					sizeGuideDataList.add(sizeGuideData);
				}

				sizeDTO.setSizeGuideList(sizeGuideDataList);
				sizeDTO.setImageURL(ImageURL);
				sizeDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				sizeDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				sizeDTO.setError(MarketplacecommerceservicesConstants.SIZEGUIDE_NOT_FOUND);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			//ExceptionUtil.etailNonBusinessExceptionHandler(e);
			sizeDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			sizeDTO.setError(MarketplacecommerceservicesConstants.SIZEGUIDE_NOT_FOUND);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return sizeDTO;
	}

	/**
	 * @description Duplicate Keys if inserted than list of inserted Item will be added as List with same key within Map
	 * @param map
	 *           Original map where keys to be added and returned
	 * @param key
	 *           key to be inserted
	 * @param value
	 *           Value to be inserted
	 */
	private void addToMap(final Map<String, List<SizeGuideData>> map, final String key, final SizeGuideData value)
	{
		if (!map.containsKey(key))
		{
			map.put(key, new ArrayList<SizeGuideData>());
		}
		map.get(key).add(value);
	}

	/**
	 * @return the sizeGuideService
	 */
	public SizeGuideService getSizeGuideService()
	{
		return sizeGuideService;
	}

	/**
	 * @param sizeGuideService
	 *           the sizeGuideService to set
	 */
	public void setSizeGuideService(final SizeGuideService sizeGuideService)
	{
		this.sizeGuideService = sizeGuideService;
	}

	/**
	 * @return the sizeGuideConverter
	 */
	public Converter<SizeGuideModel, SizeGuideData> getSizeGuideConverter()
	{
		return sizeGuideConverter;
	}

	/**
	 * @param sizeGuideConverter
	 *           the sizeGuideConverter to set
	 */
	public void setSizeGuideConverter(final Converter<SizeGuideModel, SizeGuideData> sizeGuideConverter)
	{
		this.sizeGuideConverter = sizeGuideConverter;
	}

	/**
	 * @return the sizeGuideComparator
	 */
	public SizeGuideComparator getSizeGuideComparator()
	{
		return sizeGuideComparator;
	}

	/**
	 * @param sizeGuideComparator
	 *           the sizeGuideComparator to set
	 */
	public void setSizeGuideComparator(final SizeGuideComparator sizeGuideComparator)
	{
		this.sizeGuideComparator = sizeGuideComparator;
	}

	/*
	 * (non-Javadoc)
	 *
	 *
	 * @see com.tisl.mpl.facade.product.SizeGuideFacade#getWSProductSizeguide(java.lang.String)
	 */

}