/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author 361234
 *
 */
public class MplSizeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{




	private FieldNameProvider fieldNameProvider;
	private MplBuyBoxUtility mplBuyBoxUtility;


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch
	 * .config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	/**
	 * @return the mplBuyBoxUtility
	 */

	public MplBuyBoxUtility getMplBuyBoxUtility()
	{
		return mplBuyBoxUtility;
	}

	/**
	 * @param mplBuyBoxUtility
	 *           the mplBuyBoxUtility to set
	 */
	@Required
	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
	{
		this.mplBuyBoxUtility = mplBuyBoxUtility;
	}

	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns the size of all the variant product except the current variant
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{


		if (model instanceof PcmProductVariantModel)
		{
			String qualifier = "";
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel pcmVariantModel = (PcmProductVariantModel) model;


			final Set<String> sizes = new TreeSet<String>();
			if (pcmVariantModel.getProductCategoryType().equalsIgnoreCase("FineJewellery")
					|| pcmVariantModel.getProductCategoryType().equalsIgnoreCase("FashionJewellery")
					|| pcmVariantModel.getProductCategoryType().equalsIgnoreCase(MarketplaceCoreConstants.HOME_FURNISHING))
			{
				final List<String> lengthCategoryList = Arrays.asList(getConfigurationService().getConfiguration()
						.getString("mpl.jewellery.category", "").split(","));
				final List<String> weightCategoryList = Arrays.asList(getConfigurationService().getConfiguration()
						.getString("mpl.homefurnishing.category.weight", "").split(","));
				final List<String> volumeCategoryList = Arrays.asList(getConfigurationService().getConfiguration()
						.getString("mpl.homefurnishing.category.volume", "").split(","));

				final Collection<CategoryModel> superCategories = pcmVariantModel.getSupercategories();
				if (CollectionUtils.isNotEmpty(superCategories) && CollectionUtils.isNotEmpty(lengthCategoryList))
				{
					for (final CategoryModel primaryCategory : superCategories)
					{
						if (primaryCategory != null && StringUtils.isNotEmpty(primaryCategory.getCode()))
						{
							if (lengthCategoryList.contains(primaryCategory.getCode()))
							{
								//sizes.add("Length:");
								qualifier = "Length";
							}
							if (weightCategoryList.contains(primaryCategory.getCode()))
							{

								qualifier = "Weight";
							}
							if (volumeCategoryList.contains(primaryCategory.getCode()))
							{

								qualifier = "Volume";
							}
						}
					}
				}
			}
			if (StringUtils.isNotEmpty(qualifier))
			{
				sizes.add(qualifier);
			}
			//	Fetch sizes in all the Variants
			for (final VariantProductModel pcmProductVariantModel : pcmVariantModel.getBaseProduct().getVariants())
			{

				final PcmProductVariantModel pcmSizeVariantModel = (PcmProductVariantModel) pcmProductVariantModel;

				//SONAR Fix
				//				if (pcmSizeVariantModel.getColour() != null && pcmVariantModel.getColour() != null)
				//				{
				//
				//					if (pcmSizeVariantModel.getColour().equals(pcmVariantModel.getColour()))
				//					{
				//						//Included for Electronics Product
				//						if (pcmSizeVariantModel.getSize() != null)
				//						{
				//							sizes.add(pcmSizeVariantModel.getSize());
				//						}
				//					}
				//
				//
				//				}

				//Included for Electronics Product
				final String sizeVariantColour = mplBuyBoxUtility.getVariantColour(pcmSizeVariantModel);

				final String pcmVariantColour = mplBuyBoxUtility.getVariantColour(pcmVariantModel);
				if (sizeVariantColour != null && pcmVariantColour != null && sizeVariantColour.equalsIgnoreCase(pcmVariantColour)
						&& pcmSizeVariantModel.getSize() != null)
				{

					sizes.add(pcmSizeVariantModel.getSize().toUpperCase());

				}

			}


			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				fieldValues.addAll(createFieldValue(sizes, indexedProperty));
			}
			//return the field values
			return fieldValues;

		}
		else
		{
			return Collections.emptyList();
		}



	}

	/**
	 * @return List<FieldValue>
	 * @param color
	 *           ,indexedProperty
	 * @description: It returns the size of all the variant product except the current variant
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final Set<String> color, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, color));
		}
		return fieldValues;
	}

	/**
	 * @return void
	 * @param fieldNameProvider
	 * @description: Set Field name provider
	 *
	 */
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}

}
