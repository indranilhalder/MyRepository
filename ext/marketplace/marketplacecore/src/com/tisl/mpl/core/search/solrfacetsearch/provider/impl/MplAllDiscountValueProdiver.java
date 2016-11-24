/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;


import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
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
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author 880282
 *
 */
public class MplAllDiscountValueProdiver extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;
	private MplBuyBoxUtility mplBuyBoxUtility;

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
	@Resource
	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
	{
		this.mplBuyBoxUtility = mplBuyBoxUtility;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		// YTODO Auto-generated method stub
		try
		{
			if (model instanceof ProductModel)
			{
				final ProductModel product = (ProductModel) model;

				final Collection fieldValues = new ArrayList();

				fieldValues.addAll(createFieldValue(product, indexConfig, indexedProperty));

				return fieldValues;
			}
			else
			{
				return Collections.emptyList();

			}
		}
		catch (final Exception e) /* added part of value provider go through */
		{
			throw new FieldValueProviderException(
					"Cannot evaluate " + indexedProperty.getName() + " using " + super.getClass().getName() + "exception" + e, e);
		}
	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				(language == null) ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{

		final List fieldValues = new ArrayList();

		addFieldValues(fieldValues, indexedProperty, null, checkIfDiscountExist(indexConfig, product));

		return fieldValues;
	}




	/**
	 * @param indexConfig
	 * @param product
	 * @return
	 */
	/**
	 * @param indexConfig
	 * @param product
	 */
	private String checkIfDiscountExist(final IndexConfig indexConfig, final ProductModel product)
	{

		boolean offerExists = false;
		String returnstring = null;
		//		double percentDiscount = 0.0;
		final BuyBoxModel buyboxWinner = mplBuyBoxUtility.getLeastPriceBuyBoxModel(product);
		if (buyboxWinner != null)
		{

			if (null != buyboxWinner.getSpecialPrice() && buyboxWinner.getSpecialPrice().intValue() > 0)
			{

				// TISPRM-92
				if (buyboxWinner.getMrp().doubleValue() - buyboxWinner.getSpecialPrice().doubleValue() > 0)
				{
					offerExists = true;
					//					percentDiscount = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getSpecialPrice().doubleValue()) * 100) / buyboxWinner.getMrp().doubleValue();
				}

			}
			else if (null != buyboxWinner.getPrice() && buyboxWinner.getPrice().intValue() > 0
					&& buyboxWinner.getMrp().intValue() > buyboxWinner.getPrice().intValue())
			{

				// TISPRM-92
				if (buyboxWinner.getMrp().doubleValue() - buyboxWinner.getPrice().doubleValue() > 0)
				{
					offerExists = true;
					//					percentDiscount = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getPrice().doubleValue()) * 100) / buyboxWinner.getMrp().doubleValue();
				}
			}
		}

		if (offerExists)
		{
			returnstring = "Discounted Items";
		}
		else
		{
			//return "Non - Discounted Items";
			returnstring = "Non - Discounted Items";
		}

		return returnstring;

	}

	/**
	 * This method retrieves list of seller price for product id and choose best price(MRP) based on buybox logic
	 *
	 * @param product
	 * @return buyboxwinner price
	 */
	public BuyBoxModel getBuyBoxPrice(final ProductModel productModel)
	{
		final BuyBoxModel seller = mplBuyBoxUtility.getLeastPriceBuyBoxModel(productModel);
		return seller;
	}
}
