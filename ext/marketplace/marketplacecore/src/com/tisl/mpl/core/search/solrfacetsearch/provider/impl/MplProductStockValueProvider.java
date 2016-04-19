/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
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

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.marketplacecommerceservices.service.BuyBoxService;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author TCS
 *
 */
public class MplProductStockValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{

	private FieldNameProvider fieldNameProvider;
	private MplBuyBoxUtility mplBuyBoxUtility;
	@Resource
	private BuyBoxService buyBoxService;


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
			final JSONArray sizeStockJsonArray = new JSONArray();

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

					final int stockValue = buyBoxService.getBuyboxPricesForSearch(pcmSizeVariantModel.getCode()).get(0).getAvailable()
							.intValue();

					final JSONObject sizeStockJson = new JSONObject();
					//	final StockLevelStatus stockLevelStatus = StockLevelStatus.OUTOFSTOCK;
					String stockStatus = "";
					if (stockValue > 0)
					{
						stockStatus = StockLevelStatus.INSTOCK.toString();
					}
					else if (stockValue == 0)
					{
						stockStatus = StockLevelStatus.OUTOFSTOCK.toString();
					}
					sizeStockJson.put(pcmSizeVariantModel.getSize().toUpperCase(), stockStatus);
					sizeStockJsonArray.add(sizeStockJson);

					//final  JSONPObject  buyboxJson = new JSONPObject();
					//final JSON buyboxJson = new JSONObject();

					//					obj.put("name", "foo");
					//					obj.put("num", new Integer(100));
					//	displaySizes.add(pcmSizeVariantModel.getSize().toUpperCase() + ":" + price);

				}

			}


			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				//fieldValues.addAll(createFieldValue(displaySizes, indexedProperty));
				fieldValues.addAll(createFieldValue(sizeStockJsonArray.toString(), indexedProperty));
			}
			System.out.println("##########stock value" + fieldValues);
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
	private List<FieldValue> createFieldValue(final String sizeStockJsonArray, final IndexedProperty indexedProperty)
	{
		// YTODO Auto-generated method stub
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, sizeStockJsonArray));
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

}
