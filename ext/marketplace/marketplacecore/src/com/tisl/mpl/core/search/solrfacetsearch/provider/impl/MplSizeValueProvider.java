package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author TCS
 *
 */
public class MplSizeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{




	/**
	 *
	 */
	private static final String NOSTOCK = "NOSTOCK:";
	private static final String STOCK = "STOCK:";
	private FieldNameProvider fieldNameProvider;
	private MplBuyBoxUtility mplBuyBoxUtility;
	@Resource
	private BuyBoxService buyBoxService;




	/*
	 * /**
	 *
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
	 * this value provider will fetch stock related details
	 *
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

		final Set<String> oosproducts = new TreeSet<String>();
		final Set<String> products = new TreeSet<String>();
		try
		{
			if (model instanceof PcmProductVariantModel)
			{
				//Model should be instance of PcmProductVariantModel
				final PcmProductVariantModel pcmVariantModel = (PcmProductVariantModel) model;
				//	Fetch sizes in all the Variants
				for (final VariantProductModel pcmProductVariantModel : pcmVariantModel.getBaseProduct().getVariants())
				{

					final PcmProductVariantModel pcmSizeVariantModel = (PcmProductVariantModel) pcmProductVariantModel;
					//final Set<String> sizes = new TreeSet<String>();


					//Included for Electronics Product
					final String sizeVariantColour = mplBuyBoxUtility.getVariantColour(pcmSizeVariantModel);

					final String pcmVariantColour = mplBuyBoxUtility.getVariantColour(pcmVariantModel);
					/* TPR-249 CHANGES starts */
					if (sizeVariantColour != null && pcmVariantColour != null && sizeVariantColour.equalsIgnoreCase(pcmVariantColour)
							&& pcmSizeVariantModel.getSize() != null
							&& CollectionUtils.isNotEmpty(buyBoxService.buyboxPrice(pcmSizeVariantModel.getCode())))
					{
						//sizes added corresponding  the products having stock
						products.add(STOCK + pcmSizeVariantModel.getSize().toUpperCase());
					}
					else if (sizeVariantColour != null && pcmVariantColour != null
							&& sizeVariantColour.equalsIgnoreCase(pcmVariantColour) && pcmSizeVariantModel.getSize() != null
							&& CollectionUtils.isEmpty(buyBoxService.buyboxPrice(pcmSizeVariantModel.getCode())))
					{
						//sizes added corresponding  the products having no stock
						oosproducts.add(NOSTOCK + pcmSizeVariantModel.getSize().toUpperCase());
					}

				}
				if (CollectionUtils.isNotEmpty(oosproducts))
				{
					products.addAll(oosproducts);
				}
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				{
					//add field values
					fieldValues.addAll(createFieldValue(products, indexedProperty));
				}

				/* TPR-249 CHANGES ends */
				//return the field values
				return fieldValues;

			}
			else
			{
				return Collections.emptyList();
			}

		}
		catch (final Exception e) /* added part of value provider go through */
		{
			throw new FieldValueProviderException("Cannot evaluate " + indexedProperty.getName() + " using "
					+ super.getClass().getName() + "exception" + e, e);
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
	 *
	 * @param fieldNameProvider
	 * @description: Set Field name provider
	 *
	 */
	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

}