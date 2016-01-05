package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.VariantFullPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.comparator.VariantCapacityComparator;
import com.tisl.mpl.facade.comparator.VariantSizeComparator;



/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */


@SuppressWarnings(
{ "PMD" })
public class CustomVariantDataPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		VariantFullPopulator<SOURCE, TARGET>
{

	/**
	 *
	 */
	private static final String Y = "Y";
	protected static final Logger LOG = Logger.getLogger(CustomVariantDataPopulator.class);

	@Autowired
	private VariantSizeComparator variantSizeComparator;
	@Autowired
	private VariantCapacityComparator variantCapacityComparator;


	/**
	 * @description method is to populate variant details from product model to be displayed in pdp
	 * @param productModel
	 * @param productData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException,
			EtailNonBusinessExceptions
	{
		VariantOptionData variantOptionData = null;
		if (productModel instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel selectedVariantModel = (PcmProductVariantModel) productModel;
			final String selectedSize = selectedVariantModel.getSize();
			final String selectedCapacity = selectedVariantModel.getCapacity();
			final ProductModel baseProduct = selectedVariantModel.getBaseProduct();
			final Map<String, String> defaultColorMap = new HashMap<String, String>();
			boolean isSizeVariantPresent = false;
			boolean isCapacityVariantPresent = false;
			List<VariantOptionData> variantOptions = new ArrayList<VariantOptionData>();
			if (null != baseProduct.getVariants())
			{

				for (final VariantProductModel vm : baseProduct.getVariants())
				{
					final Map<String, String> sizeLink = new HashMap<String, String>();
					final PcmProductVariantModel pm = (PcmProductVariantModel) vm;
					variantOptionData = getVariantOptionDataConverter().convert(pm);
					if (null != pm.getSize())
					{
						//chceking size variant exists or not
						if (selectedSize.equals(pm.getSize()))
						{
							isSizeVariantPresent = true;
							final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode()) ? pm
									.getColourHexCode() : pm.getColour().toLowerCase());
							defaultColorMap.put(color, Y);
							variantOptionData.setDefaultUrl(variantOptionData.getUrl());
						}
						//checking for colour variant
						if (null != pm.getColour())
						{
							final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode()) ? pm
									.getColourHexCode() : pm.getColour().toLowerCase());
							variantOptionData.setColourCode(color);
							variantOptionData.setColour(pm.getColour());
						}
						//checking for colour hex code
						/*
						 * if (null != pm.getColourHexCode()) { variantOptionData.setColourCode(pm.getColourHexCode()); }
						 */
						sizeLink.put(variantOptionData.getUrl(), pm.getSize());
						variantOptionData.setSizeLink(sizeLink);
					}
					//chceking capacity variant exists or not
					else if (null != pm.getCapacity())
					{
						isCapacityVariantPresent = true;
						if (selectedCapacity.equals(pm.getCapacity()))
						{
							final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode()) ? pm
									.getColourHexCode() : pm.getColour().toLowerCase());
							defaultColorMap.put(color, Y);

							variantOptionData.setDefaultUrl(variantOptionData.getUrl());
						}
						variantOptionData.setCapacity(pm.getCapacity());
						//checking for colour variant
						if (null != pm.getColour())
						{
							final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode()) ? pm
									.getColourHexCode() : pm.getColour().toLowerCase());
							variantOptionData.setColourCode(color);
							variantOptionData.setColour(pm.getColour());
						}

					}
					//checking for colour variant
					else
					{
						if (null != pm.getColour())
						{
							variantOptionData.setDefaultUrl(variantOptionData.getUrl());
							final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode()) ? pm
									.getColourHexCode() : pm.getColour().toLowerCase());
							variantOptionData.setColourCode(color);
							variantOptionData.setColour(pm.getColour());
							defaultColorMap.put(color, Y);
						}
					}

					variantOptions.add(variantOptionData);
				}
				variantOptions = populateColor(variantOptions, defaultColorMap);
				productData.setVariantOptions(variantOptions);
				if (isSizeVariantPresent)
				{
					Collections.sort(productData.getVariantOptions(), variantSizeComparator);
				}
				if (isCapacityVariantPresent)
				{
					Collections.sort(productData.getVariantOptions(), variantCapacityComparator);
				}
			}
		}
	}



	/**
	 * method to check whether any default is set against a color or not
	 *
	 * @param variantOptions
	 * @param defaultColorMap
	 */
	private List<VariantOptionData> populateColor(final List<VariantOptionData> variantOptions,
			final Map<String, String> defaultColorMap)
	{

		for (final VariantOptionData variantData : variantOptions)
		{
			if (!(defaultColorMap.containsKey(variantData.getColourCode())))
			{

				variantData.setDefaultUrl(variantData.getUrl());
				defaultColorMap.put(variantData.getColourCode(), Y);
			}
		}

		return variantOptions;
	}
}
