/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.comparator.SizeGuideComparator;
import com.tisl.mpl.facade.product.SizeGuideFacade;
import com.tisl.mpl.facades.product.data.SizeGuideData;
import com.tisl.mpl.marketplacecommerceservices.service.SizeGuideService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.SizeGuideWsDTO;
import com.tisl.mpl.wsdto.SizeGuideWsData;
import com.tisl.mpl.wsdto.SizeGuideWsDataValue;


/**
 * @author TCS
 *
 */
public class DefaultSizeGuideFacade implements SizeGuideFacade
{
	@Resource
	private SizeGuideService sizeGuideService;

	@Resource
	private Converter<SizeGuideModel, SizeGuideData> sizeGuideConverter;

	@Autowired
	private SizeGuideComparator sizeGuideComparator;


	/**
	 * @description It is used for fetching all distinct Size Guides of an online product
	 * @param productCode
	 * @return list of SizeGuideData
	 */
	@Override
	public Map<String, List<SizeGuideData>> getProductSizeguide(final String productCode) throws CMSItemNotFoundException
	{
		//// change it null. no need of new ArrayList<SizeGuideModel>();
		List<SizeGuideModel> sizeGuideModels = null; //changed to null
		List<SizeGuideData> sizeDataValues = null;
		SizeGuideData sizeGuideData = null;
		final HashMap<String, List<SizeGuideData>> sizeGuideDatas = new HashMap<String, List<SizeGuideData>>();
		final HashMap<String, List<SizeGuideData>> sizeGuideSortedDatas = new HashMap<String, List<SizeGuideData>>();

		try
		{
			sizeGuideModels = sizeGuideService.getProductSizeGuideList(productCode);
			/* Converting the SizeGuide Model to SizeGuide Data transaction */
			if (sizeGuideModels != null)
			{
				//// Y no converter and populator for SizeGuide??? Y we are writing code here
				//// Converting the SizeGuide Model to SizeGuide Data transaction ????
				for (final SizeGuideModel sizeGuideModel : sizeGuideModels)
				{
					//convertor is added
					sizeGuideData = getSizeGuideConverter().convert(sizeGuideModel);
					addToMap(sizeGuideDatas, sizeGuideModel.getDimension(), sizeGuideData);
				}

			}

			/* sorting the Size guide map based on dimension */
			for (final String key : sizeGuideDatas.keySet())
			{
				sizeDataValues = sizeGuideDatas.get(key);
				Collections.sort(sizeDataValues, sizeGuideComparator);
				sizeGuideSortedDatas.put(key, sizeDataValues);
			}




		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
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
			final Map<String, List<SizeGuideData>> sizeGuideDatas = getProductSizeguide(productCode);

			/* Converting the SizeGuide Model to SizeGuide Web service Data transaction */
			if (sizeGuideDatas != null)
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
						sizeGuideWsDataValue.setDimensionValue(sizeGuideValue.getDimensionValue());
						sizeGuideWsDataValue.setDimensionUnit(sizeGuideValue.getDimensionUnit());
						sizeGuideWsDataValue.setDimensionSize(sizeGuideValue.getDimensionSize());
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
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			sizeDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			sizeDTO.setError(MarketplacecommerceservicesConstants.SIZEGUIDE_NOT_FOUND);
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


}
