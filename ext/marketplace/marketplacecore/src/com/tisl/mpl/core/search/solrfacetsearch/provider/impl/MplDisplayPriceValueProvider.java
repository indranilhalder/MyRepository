/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author 361234
 *
 */
public class MplDisplayPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{




	private FieldNameProvider fieldNameProvider;
	private MplBuyBoxUtility mplBuyBoxUtility;
	@Resource
	private BuyBoxService buyBoxService;
	private PriceDataFactory priceDataFactory;
	public static final String INR = "INR";

	protected PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

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
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel pcmVariantModel = (PcmProductVariantModel) model;

			//System.out.println("**Inside DisplayPriceValueProvider**");


			//	final Set<String> displaySizes = new TreeSet<String>();
			final JSONArray sizePriceJsonArray = new JSONArray();

			//	Fetch sizes in all the Variants
			for (final VariantProductModel pcmProductVariantModel : pcmVariantModel.getBaseProduct().getVariants())
			{

				final PcmProductVariantModel pcmSizeVariantModel = (PcmProductVariantModel) pcmProductVariantModel;

				//Included for Electronics Product
				final String sizeVariantColour = mplBuyBoxUtility.getVariantColour(pcmSizeVariantModel,
						pcmSizeVariantModel.getFeatures());


				final String pcmVariantColour = mplBuyBoxUtility.getVariantColour(pcmVariantModel, pcmVariantModel.getFeatures());
				if (sizeVariantColour != null && pcmVariantColour != null && sizeVariantColour.equalsIgnoreCase(pcmVariantColour)
						&& pcmSizeVariantModel.getSize() != null)
				{

					final Double price = buyBoxService.getBuyboxPricesForSearch(pcmSizeVariantModel.getCode()).get(0).getPrice();
					final JSONObject sizePriceJson = new JSONObject();
					sizePriceJson.put(pcmSizeVariantModel.getSize().toUpperCase(), price);
					sizePriceJsonArray.add(sizePriceJson);
				}

			}


			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				//fieldValues.addAll(createFieldValue(displaySizes, indexedProperty));
				fieldValues.addAll(createFieldValue(sizePriceJsonArray.toString(), indexedProperty));
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
	//	protected List<FieldValue> createFieldValue(final Set<String> color, final IndexedProperty indexedProperty)
	//	{
	//		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
	//		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
	//		for (final String fieldName : fieldNames)
	//		{
	//			fieldValues.add(new FieldValue(fieldName, color));
	//		}
	//		return fieldValues;
	//	}

	/**
	 * @param sizePriceJsonArray
	 * @param indexedProperty
	 * @return fieldValues
	 */
	private List<FieldValue> createFieldValue(final String sizePriceJsonArray, final IndexedProperty indexedProperty)
	{
		// YTODO Auto-generated method stub
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, sizePriceJsonArray));
		}
		return fieldValues;
	}

	public PriceData formPriceData(final Double price)
	{

		return priceDataFactory.create(PriceDataType.BUY, new BigDecimal(price.doubleValue()), INR);
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

}
