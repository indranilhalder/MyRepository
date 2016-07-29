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

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author 880282
 *
 */
//public class MplDiscountFlagValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
//{
//	private FieldNameProvider fieldNameProvider;
//	private MplBuyBoxUtility mplBuyBoxUtility;
//
//	/**
//	 * @return the mplBuyBoxUtility
//	 */
//	public MplBuyBoxUtility getMplBuyBoxUtility()
//	{
//		return mplBuyBoxUtility;
//	}
//
//	/**
//	 * @param mplBuyBoxUtility
//	 *           the mplBuyBoxUtility to set
//	 */
//	@Resource
//	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
//	{
//		this.mplBuyBoxUtility = mplBuyBoxUtility;
//	}
//
//	protected FieldNameProvider getFieldNameProvider()
//	{
//		return this.fieldNameProvider;
//	}
//
//	@Required
//	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
//	{
//		this.fieldNameProvider = fieldNameProvider;
//	}
//
//	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
//			final Object model) throws FieldValueProviderException
//	{
//		// YTODO Auto-generated method stub
//		if (model instanceof ProductModel)
//		{
//			final ProductModel product = (ProductModel) model;
//
//			final Collection fieldValues = new ArrayList();
//
//			fieldValues.addAll(createFieldValue(product, indexConfig, indexedProperty));
//
//			return fieldValues;
//		}
//		else
//		{
//			return Collections.emptyList();
//
//		}
//	}
//
//	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
//			final LanguageModel language, final Object value)
//	{
//		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
//				(language == null) ? null : language.getIsocode());
//		for (final String fieldName : fieldNames)
//		{
//			fieldValues.add(new FieldValue(fieldName, value));
//		}
//	}
//
//	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexConfig indexConfig,
//			final IndexedProperty indexedProperty)
//	{
//
//		final List fieldValues = new ArrayList();
//
//		addFieldValues(fieldValues, indexedProperty, null, checkIfDiscountExist(indexConfig, product));
//
//		return fieldValues;
//	}
//
//
//
//
//	/**
//	 * @param indexConfig
//	 * @param product
//	 * @return
//	 */
//	/**
//	 * @param indexConfig
//	 * @param product
//	 */
//	private String checkIfDiscountExist(final IndexConfig indexConfig, final ProductModel product)
//	{
//
//		//		boolean offerExists = false;
//		//		final List<String> discountRangeList = new ArrayList<String>(0);
//		//		String discountRange;
//		double percentDiscount = 0.0;
//		final BuyBoxModel buyboxWinner = mplBuyBoxUtility.getLeastPriceBuyBoxModel(product);
//		if (buyboxWinner != null)
//		{
//
//			if (null != buyboxWinner.getSpecialPrice() && buyboxWinner.getSpecialPrice().intValue() > 0)
//			{
//
//				if (buyboxWinner.getMrp().doubleValue() - buyboxWinner.getSpecialPrice().doubleValue() > 0)
//				{
//					//					offerExists = true;
//					percentDiscount = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getSpecialPrice().doubleValue()) * 100)
//							/ buyboxWinner.getMrp().doubleValue();
//				}
//
//			}
//			else if (null != buyboxWinner.getPrice() && buyboxWinner.getPrice().intValue() > 0
//					&& buyboxWinner.getMrp().intValue() > buyboxWinner.getPrice().intValue())
//			{
//
//				if (buyboxWinner.getMrp().doubleValue() - buyboxWinner.getPrice().doubleValue() > 0)
//				{
//					//					offerExists = true;
//					percentDiscount = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getPrice().doubleValue()) * 100)
//							/ buyboxWinner.getMrp().doubleValue();
//				}
//			}
//		}
//		// TISPRM-134
//		//		discountRange = "Non-Discounted Items";
//		//		if (offerExists)
//		//		{
//		if (percentDiscount > 0 && percentDiscount <= 10)
//		{
//			return "Upto 10%";
//		}
//		else if (percentDiscount > 10 && percentDiscount <= 30)
//		{
//			return "10% - 30%";
//		}
//		else if (percentDiscount > 30 && percentDiscount <= 50)
//		{
//			return "30% - 50%";
//		}
//		else if (percentDiscount > 50 && percentDiscount <= 70)
//		{
//			return "50% - 70%";
//		}
//		else if (percentDiscount > 70 && percentDiscount <= 100)
//		{
//			return "70% and more";
//		}
//		else
//		{
//			return null;
//		}
//		//		}
//
//	}
//
//	/**
//	 * This method retrieves list of seller price for product id and choose best price(MRP) based on buybox logic
//	 *
//	 * @param product
//	 * @return buyboxwinner price
//	 */
//	public BuyBoxModel getBuyBoxPrice(final ProductModel productModel)
//	{
//		final BuyBoxModel seller = mplBuyBoxUtility.getLeastPriceBuyBoxModel(productModel);
//		return seller;
//	}
//}

public class MplDiscountFlagValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
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


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.FieldValueProvider#getFieldValues(de.hybris.platform.solrfacetsearch.
	 * config.IndexConfig, de.hybris.platform.solrfacetsearch.config.IndexedProperty, java.lang.Object)
	 */
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException, EtailNonBusinessExceptions
	{

		if (model instanceof PcmProductVariantModel)
		{
			//Model should be instance of PcmProductVariantModel
			final PcmProductVariantModel product = (PcmProductVariantModel) model;
			final Double value = getDiscountPrice(product);

			if (null != value)
			{
				//final Double averageRating = product.getRatingReview().getMplAverageRating();
				//If averageRating is not empty
				//				if (averageRating != null)
				//				{
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				{
					final Collection<LanguageModel> languages = indexConfig.getLanguages();

					for (final LanguageModel language : languages)
					{
						fieldValues.addAll(createFieldValue(value, language, indexedProperty, "DISCOUNT"));
					}

				}
				//return the field values
				return fieldValues;
				//}

				//				else
				//				{
				//					return Collections.emptyList();
				//				}

			}

			else
			{
				return Collections.emptyList();
			}

		}
		else
		{
			return Collections.emptyList();
		}

		//-------------------------------------------------------------------
		//		final Collection fieldValues = new ArrayList();
		//		try
		//		{
		//			List<String> rangeNameList = null;
		//			ProductModel product = null;
		//			if (model instanceof ProductModel)
		//			{
		//				product = (ProductModel) model;
		//				System.out.println(product);
		//			}
		//			else
		//			{
		//				throw new FieldValueProviderException("Cannot evaluate price of non-product item");
		//			}
		//			if (indexConfig.getCurrencies().isEmpty())
		//			{
		//				System.out.println(indexConfig.getCurrencies().isEmpty());
		//			}
		//			else
		//			{
		//
		//
		//				for (final CurrencyModel currency : indexConfig.getCurrencies())
		//				{
		//					final CurrencyModel sessionCurrency = this.i18nService.getCurrentCurrency();
		//					try
		//					{
		//						this.i18nService.setCurrentCurrency(currency);
		//						final Double value = getDiscountPrice(product);
		//						if (value != null && value > 0)
		//						{
		//
		//							final String rangeKey = currency.getIsocode() + "-DISCOUNT";
		//							rangeNameList = getRangeNameList(indexedProperty, value, rangeKey);
		//							String currencyValue = "";
		//							if (currency.getIsocode() != null)
		//							{
		//								currencyValue = currency.getIsocode().toLowerCase();
		//							}
		//							else
		//							{
		//								currencyValue = currency.getIsocode();
		//							}
		//							final Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, currencyValue);
		//							for (final String fieldName : fieldNames)
		//							{
		//								if (rangeNameList != null)
		//								{
		//
		//									if (rangeNameList.isEmpty())
		//									{
		//										fieldValues.add(new FieldValue(fieldName, value));
		//									}
		//									else
		//									{
		//										for (final String rangeName : rangeNameList)
		//										{
		//											fieldValues.add(new FieldValue(fieldName, (rangeName == null) ? value : rangeName));
		//										}
		//									}
		//								}
		//							}
		//
		//
		//						}
		//						else
		//						{
		//							return Collections.emptyList();
		//						}
		//					}
		//					finally
		//					{
		//						this.i18nService.setCurrentCurrency(sessionCurrency);
		//					}
		//				}
		//
		//
		//
		//			}
		//
		//		}
		//		catch (final Exception e)
		//		{
		//			throw new FieldValueProviderException("Cannot evaluate " + indexedProperty.getName() + " using "
		//					+ super.getClass().getName() + "exception" + e, e);
		//		}
		//
		//		return fieldValues;
		//
	}


	public Double getDiscountPrice(final ProductModel product)
	{
		double discountedPercent = 0.0;
		final BuyBoxModel buyboxWinner = mplBuyBoxUtility.getLeastPriceBuyBoxModel(product);
		if (buyboxWinner != null)
		{

			if (null != buyboxWinner.getSpecialPrice() && buyboxWinner.getSpecialPrice().intValue() > 0)
			{
				discountedPercent = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getSpecialPrice().doubleValue()) * 100)
						/ buyboxWinner.getMrp().doubleValue();
			}
			else if (null != buyboxWinner.getPrice() && buyboxWinner.getPrice().intValue() > 0
					&& buyboxWinner.getMrp().intValue() > buyboxWinner.getPrice().intValue())
			{
				discountedPercent = ((buyboxWinner.getMrp().doubleValue() - buyboxWinner.getPrice().doubleValue()) * 100)
						/ buyboxWinner.getMrp().doubleValue();
			}
		}
		return discountedPercent;
	}

	/**
	 * @return List<FieldValue>
	 * @param size
	 *           ,indexedProperty
	 * @description: It creates field values
	 *
	 */
	//Create field values
	protected List<FieldValue> createFieldValue(final Double value, final LanguageModel language,
			final IndexedProperty indexedProperty, final String rangeKey)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
		addFieldValues(fieldValues, language, indexedProperty, value, rangeKey);
		/*
		 * for (final String fieldName : fieldNames) { //Add field values fieldValues.add(new FieldValue(fieldName,
		 * avgRating)); }
		 */
		return fieldValues;
	}


	protected void addFieldValues(final List<FieldValue> fieldValues, final LanguageModel language,
			final IndexedProperty indexedProperty, final Object value, final String rangeKey)
	{
		List<String> rangeNameList = null;
		try
		{
			rangeNameList = getRangeNameList(indexedProperty, value, rangeKey);
		}
		catch (final FieldValueProviderException e)
		{
			LOG.error("Could not get Range value", e);
		}
		String rangeName = null;
		if (CollectionUtils.isNotEmpty(rangeNameList))
		{
			rangeName = rangeNameList.get(0);
		}


		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				language == null ? null : language.getIsocode());
		final Object valueToPass = (rangeName == null ? value : rangeName);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, valueToPass));
		}
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