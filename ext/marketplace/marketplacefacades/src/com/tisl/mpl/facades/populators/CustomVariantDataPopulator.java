package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.VariantFullPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
public class CustomVariantDataPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends VariantFullPopulator<SOURCE, TARGET>
{

	/**
	 *
	 */
	private static final String Y = "Y";
	private static final String FINEJEWELLERY = "FineJewellery";
	private static final String FASHIONJEWELLERY = "FashionJewellery";
	protected static final Logger LOG = Logger.getLogger(CustomVariantDataPopulator.class);

	@Autowired
	private VariantSizeComparator variantSizeComparator;
	@Autowired
	private VariantCapacityComparator variantCapacityComparator;

	@Autowired
	private VariantsService variantsService;

	@Autowired
	private UrlResolver<ProductModel> productModelUrlResolver;






	/**
	 * @return the productModelUrlResolver
	 */
	public UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}



	/**
	 * @param productModelUrlResolver
	 *           the productModelUrlResolver to set
	 */
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}



	/**
	 * @return the variantsService
	 */
	public VariantsService getVariantsService()
	{
		return variantsService;
	}



	/**
	 * @param variantsService
	 *           the variantsService to set
	 */
	public void setVariantsService(final VariantsService variantsService)
	{
		this.variantsService = variantsService;
	}



	/**
	 * @description method is to populate variant details from product model to be displayed in pdp
	 * @param productModel
	 * @param productData
	 * @throws ConversionException
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public void populate(final SOURCE productModel, final TARGET productData)
			throws ConversionException, EtailNonBusinessExceptions
	{
		final List<String> allVariantsId = new ArrayList<String>();
		VariantOptionData variantOptionData = null;
		String selectedSize = null;
		if (productModel instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel selectedVariantModel = (PcmProductVariantModel) productModel;
			//TISPRM-56
			selectedSize = selectedVariantModel.getSize();
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
					if (null != vm.getCode())
					{
						allVariantsId.add(vm.getCode());
					}

					final Map<String, String> sizeLink = new HashMap<String, String>();
					final PcmProductVariantModel pm = (PcmProductVariantModel) vm;
					variantOptionData = getVariantOptionDataConverter().convert(pm);
					//Added For TPR-210
					if (null != selectedSize && null != variantOptionData.getImage())
					{
						if (null != pm.getSize())
						{
							//chceking size variant exists or not
							//TISPRO-50 - null check added
							//Added For TPR-210
							//Jewellery changes added
							if (selectedSize.equals(pm.getSize()) && null != variantOptionData.getImage()
									&& !FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
									&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
							{
								isSizeVariantPresent = true;
								defaultColorMap.put(variantOptionData.getImage().getUrl(), Y);
							}
							else if (!FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
									&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
							{
								isSizeVariantPresent = true;
								final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode())
										? pm.getColourHexCode() : pm.getColour().toLowerCase());
								defaultColorMap.put(color, Y);
							}
							//checking for colour variant
							if (null != pm.getColour() && !FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
									&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
							{
								final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode())
										? pm.getColourHexCode() : pm.getColour().toLowerCase());
								variantOptionData.setColourCode(color);
								variantOptionData.setColour(pm.getColour());
							}
							//checking for colour hex code
							sizeLink.put(variantOptionData.getUrl(), pm.getSize());
							variantOptionData.setSizeLink(sizeLink);
						}
						//chceking capacity variant exists or not
						else if (null != pm.getCapacity())
						{
							isCapacityVariantPresent = true;
							//TISPRO-50 - null check added
							if (null != selectedCapacity && selectedCapacity.equals(pm.getCapacity())
									&& !FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
									&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
							{
								final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode())
										? pm.getColourHexCode() : pm.getColour().toLowerCase());
								defaultColorMap.put(color, Y);

								variantOptionData.setDefaultUrl(variantOptionData.getUrl());
							}
							variantOptionData.setCapacity(pm.getCapacity());
							//checking for colour variant
							if (null != pm.getColour() && !FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
									&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
							{
								final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode())
										? pm.getColourHexCode() : pm.getColour().toLowerCase());
								variantOptionData.setColourCode(color);
								variantOptionData.setColour(pm.getColour());
							}

						}
						//checking for colour variant
						else
						{
							if (null != pm.getColour() && !FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
									&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
							{
								variantOptionData.setDefaultUrl(variantOptionData.getUrl());
								final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode())
										? pm.getColourHexCode() : pm.getColour().toLowerCase());
								variantOptionData.setColourCode(color);
								variantOptionData.setColour(pm.getColour());
								defaultColorMap.put(color, Y);
							}
						}
					}

					//Added For TPR-210
					else if (null == variantOptionData.getImage() || null == selectedSize)
					{
						//variantOptionData.setDefaultUrl(variantOptionData.getUrl());
						isSizeVariantPresent = true;
						if (!FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
								&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
						{
							final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode())
									? pm.getColourHexCode() : pm.getColour().toLowerCase());
							variantOptionData.setColourCode(color);
							variantOptionData.setColour(pm.getColour());
							defaultColorMap.put(color, Y);
						}

						sizeLink.put(variantOptionData.getUrl(), pm.getSize());
						variantOptionData.setSizeLink(sizeLink);
					}

					variantOptions.add(variantOptionData);
					if (null == variantOptionData.getColourCode()
							&& !FINEJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType())
							&& !FASHIONJEWELLERY.equalsIgnoreCase(variantOptionData.getProductType()))
					{
						final String color = (pm.getColourHexCode() != null && StringUtils.isNotEmpty(pm.getColourHexCode())
								? pm.getColourHexCode() : pm.getColour().toLowerCase());
						variantOptionData.setColourCode(color);
					}
					if (sizeLink.isEmpty())
					{
						isSizeVariantPresent = false;
					}
				}
				productData.setAllVariantsId(allVariantsId);
				variantOptions = populateColor(variantOptions);
				productData.setVariantOptions(variantOptions);
				if (isSizeVariantPresent && CollectionUtils.isNotEmpty(productData.getVariantOptions()))
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
	 */
	//Added For TPR-210
	private List<VariantOptionData> populateColor(final List<VariantOptionData> variantOptions)
	{
		final Map<String, VariantOptionData> colourMap = new HashMap<>();

		for (final VariantOptionData variantData : variantOptions)
		{
			//if colourMap doesnot contain the value
			if (!colourMap.containsKey(variantData.getColourCode()))

			{

				//Image Present
				if (variantData.getImage() != null)
				{
					colourMap.put(variantData.getColourCode(), variantData);
				}
				//Image not Present
				else
				{
					colourMap.put(variantData.getColourCode(), variantData);
				}
			}
			//Check if colourMap contains the variant data with swatch
			else if ((colourMap.containsKey(variantData.getColourCode())))
			{
				if (null != colourMap.get(variantData.getColourCode()).getImage())
				{
					//do nothing
				}
				else
				{
					colourMap.put(variantData.getColourCode(), variantData);
				}
			}

		}

		for (final VariantOptionData variantData : variantOptions)

		{
			if (colourMap.containsValue(variantData))
			{
				variantData.setDefaultUrl(colourMap.get(variantData.getColourCode()).getUrl());

			}
		}

		return variantOptions;
	}

}