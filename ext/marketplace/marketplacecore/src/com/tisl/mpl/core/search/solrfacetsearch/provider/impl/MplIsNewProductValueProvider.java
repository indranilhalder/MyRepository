/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
//Index isProductNew for a PcmProductVariantModel
public class MplIsNewProductValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * @return Collection<fieldValues>
	 * @param indexConfig
	 *           ,indexedProperty,model
	 * @description: It returns if a specific variant product is new or not
	 *
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final ProductModel productModel = (ProductModel) model;
		if (productModel == null)
		{
			return Collections.emptyList();
		}

		Date existDate = null;
		boolean isNewProduct = false;

		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{

			if (seller != null)
			{


				//Find the oldest startDate of the seller
				if (null == existDate && seller.getStartDate() != null)
				{
					existDate = seller.getStartDate();
				}
				else if (seller.getStartDate() != null && existDate.after(seller.getStartDate()))
				{
					existDate = seller.getStartDate();
				}
			}


		}

		//New Attribute
		if (null != existDate && isNew(existDate))
		{
			isNewProduct = true;
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

			{
				//add field values
				fieldValues.addAll(createFieldValue(isNewProduct, indexedProperty));
			}
			//return the field values
			return fieldValues;

		}
		return Collections.emptyList();


	}


	private boolean isNew(final Date existDate)
	{
		boolean newAttr = false;
		if (null != existDate)
		{
			final Date sysDate = new Date();
			final long dayDiff = calculateDays(existDate, sysDate);

			LOG.info("******" + existDate + "  --- dayDiff: " + dayDiff);
			final String validDaysSt = configurationService.getConfiguration().getString("attribute.new.validDays");
			final int validDays = validDaysSt == null ? 0 : Integer.parseInt(validDaysSt);

			if (validDays > dayDiff)
			{
				newAttr = true;
			}
		}

		return newAttr;
	}


	private long calculateDays(final Date dateEarly, final Date dateLater)
	{
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * @return fieldValues
	 * @param isProductNew
	 *           ,indexedProperty
	 * @description: It create isProductNew field with adding index property
	 *
	 */
	protected List<FieldValue> createFieldValue(final boolean isProductNew, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		final Object value = isProductNew;
		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}
}
