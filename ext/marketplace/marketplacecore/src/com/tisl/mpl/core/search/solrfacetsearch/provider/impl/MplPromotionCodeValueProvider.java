/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.ExcludeManufacturersRestrictionModel;
import com.tisl.mpl.model.ManufacturersRestrictionModel;


public class MplPromotionCodeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;
	private PromotionsService promotionService;
	private TimeService timeService;

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected PromotionsService getPromotionsService()
	{
		return this.promotionService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionService)
	{
		this.promotionService = promotionService;
	}

	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if (model instanceof PcmProductVariantModel)
		{
			final PcmProductVariantModel product = (PcmProductVariantModel) model;

			final Collection fieldValues = new ArrayList();

			if (indexedProperty.isMultiValue())
			{
				fieldValues.addAll(createFieldValues(product, indexConfig, indexedProperty));
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, indexConfig, indexedProperty));
			}
			return fieldValues;
		}
		else if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final Collection fieldValues = new ArrayList();

			if (indexedProperty.isMultiValue())
			{
				fieldValues.addAll(createFieldValues(product, indexConfig, indexedProperty));
			}
			else
			{
				fieldValues.addAll(createFieldValue(product, indexConfig, indexedProperty));
			}
			return fieldValues;
		}
		throw new FieldValueProviderException("Cannot get promotion codes of non-product item");
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);

			final List<ProductPromotionModel> productPromotions = getPromotionsService().getProductPromotions(
					Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product, true, currentTimeRoundedToMinute);

			final List<ProductPromotionModel> restrictedPromotions = validatePromotionRestrictions(productPromotions, product);

			final Iterator localIterator = restrictedPromotions.iterator();


			if (localIterator.hasNext())
			{
				final ProductPromotionModel promotion = (ProductPromotionModel) localIterator.next();

				addFieldValues(fieldValues, indexedProperty, null, promotion.getCode());
			}
		}

		return fieldValues;
	}


	protected List<FieldValue> createFieldValue(final PcmProductVariantModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);

			final List<ProductPromotionModel> productPromotions = getPromotionsService().getProductPromotions(
					Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product, true, currentTimeRoundedToMinute);

			final List<ProductPromotionModel> restrictedPromotions = validatePromotionRestrictions(productPromotions, product);

			final Iterator localIterator = restrictedPromotions.iterator();

			if (localIterator.hasNext())
			{
				final ProductPromotionModel promotion = (ProductPromotionModel) localIterator.next();

				addFieldValues(fieldValues, indexedProperty, null, promotion.getCode());
			}
		}

		return fieldValues;
	}

	protected List<FieldValue> createFieldValues(final PcmProductVariantModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final List<ProductPromotionModel> promotions = new ArrayList<ProductPromotionModel>();

			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);

			final List<ProductPromotionModel> productPromotions = getPromotionsService().getProductPromotions(
					Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product, true, currentTimeRoundedToMinute);

			final List<ProductPromotionModel> restrictedPromotions = validatePromotionRestrictions(productPromotions, product);

			for (final ProductPromotionModel promotion : restrictedPromotions)
			{
				if (promotion.getChannel().contains(SalesApplication.WEB) || promotion.getChannel().isEmpty())
				{
					promotions.add(promotion);
				}
			}

			if (!promotions.isEmpty())
			{

				Collections.sort(promotions, new Comparator<ProductPromotionModel>()
				{

					@Override
					public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
					{

						if (o1.getPriority() == o2.getPriority())
						{
							return 0;
						}
						else if (o1.getPriority() < o2.getPriority())
						{
							return 1;
						}
						else
						{
							return -1;
						}

					}
				});
			}


			if (promotions.size() > 0 && !promotions.isEmpty())

			{
				addFieldValues(fieldValues, indexedProperty, null, promotions.get(0).getCode());

			}

		}
		return fieldValues;
	}



	protected List<FieldValue> createFieldValues(final ProductModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();
		final BaseSiteModel baseSiteModel = indexConfig.getBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final List<ProductPromotionModel> promotions = new ArrayList<ProductPromotionModel>();

			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);

			final List<ProductPromotionModel> productPromotions = getPromotionsService().getProductPromotions(
					Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), product, true, currentTimeRoundedToMinute);

			final List<ProductPromotionModel> restrictedPromotions = validatePromotionRestrictions(productPromotions, product);

			for (final ProductPromotionModel promotion : restrictedPromotions)
			{

				promotions.add(promotion);
			}

			if (!promotions.isEmpty())
			{

				Collections.sort(promotions, new Comparator<ProductPromotionModel>()
				{

					@Override
					public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
					{

						if (o1.getPriority() == o2.getPriority())
						{
							return 0;
						}
						else if (o1.getPriority() < o2.getPriority())
						{
							return 1;
						}
						else
						{
							return -1;
						}

					}
				});
			}


			if (promotions.size() > 0 && !promotions.isEmpty())

			{
				addFieldValues(fieldValues, indexedProperty, null, promotions.get(0).getCode());

			}

		}
		return fieldValues;
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

	/**
	 * @return the timeService
	 */
	public TimeService getTimeService()
	{
		return timeService;
	}

	/**
	 * @param timeService
	 *           the timeService to set
	 */
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}


	public List<ProductPromotionModel> validatePromotionRestrictions(final List<ProductPromotionModel> promotions,
			final ProductModel productModel)
	{
		//excluded product check starts
		boolean isFreeBee = false;
		final List<ProductPromotionModel> toRemovePromotionList = new ArrayList<ProductPromotionModel>();
		if (null != promotions)
		{
			for (final ProductPromotionModel productPromotion : promotions)
			{
				boolean excludePromotion = false;
				if (null != productPromotion)
				{
					isFreeBee = isFreeBeePromotionExists(productPromotion);//check bogo promotion present or not
					if (isFreeBee && productPromotion.getRestrictions().isEmpty())
					{
						toRemovePromotionList.add(productPromotion);
						excludePromotion = true;
						break;
					}
					if (null != productPromotion.getExcludedProducts() && (!productPromotion.getExcludedProducts().isEmpty()))
					{
						final List<ProductModel> excludedList = new ArrayList<ProductModel>(productPromotion.getExcludedProducts());
						for (final ProductModel product : excludedList)
						{
							if (null != product.getCode() && product.getCode().equalsIgnoreCase(productModel.getCode()))
							{
								LOG.debug("*******Product not applicable for Excluded product criteria:" + product.getCode());
								toRemovePromotionList.add(productPromotion);
								excludePromotion = true;
								break;
							}
						}

						if (excludePromotion)
						{
							continue;
						}

					}

					///brand restriction check


					for (final AbstractPromotionRestrictionModel restriction : productPromotion.getRestrictions())
					{

						boolean excluseBrandRestrictionPresent = false;

						//checking if BOGO promotion present or not and removing the promotion if seller restriction not present
						if (!(restriction instanceof EtailSellerSpecificRestrictionModel) && isFreeBee)
						{
							toRemovePromotionList.add(productPromotion);
							excludePromotion = true;
							break;
						}

						//checking Exclude brandRestriction
						if (restriction instanceof ExcludeManufacturersRestrictionModel)
						{

							final ExcludeManufacturersRestrictionModel brandRestriction = (ExcludeManufacturersRestrictionModel) restriction;
							final List<CategoryModel> brandRestrictions = new ArrayList<CategoryModel>(
									brandRestriction.getManufacturers());
							final List<String> brands = new ArrayList<String>(getBrandsForProduct(productModel));
							for (final CategoryModel retriction : brandRestrictions)
							{
								if (brands.contains(retriction.getCode()))
								{
									excluseBrandRestrictionPresent = true;
									break;
								}

							}

							if (excluseBrandRestrictionPresent)
							{
								LOG.debug("*******Product not applicable for Exclude brand restriction:" + productModel.getCode());
								toRemovePromotionList.add(productPromotion);
							}
						}

						//checking brandRestriction
						if (restriction instanceof ManufacturersRestrictionModel)
						{
							boolean brandRestrictionPresent = false;
							final ManufacturersRestrictionModel brandRestriction = (ManufacturersRestrictionModel) restriction;
							final List<CategoryModel> brandRestrictions = new ArrayList<CategoryModel>(
									brandRestriction.getManufacturers());
							final List<String> brands = new ArrayList<String>(getBrandsForProduct(productModel));
							for (final CategoryModel retriction : brandRestrictions)
							{
								if (brands.contains(retriction.getCode()))
								{
									brandRestrictionPresent = true;
									break;
								}

							}

							if (!brandRestrictionPresent)
							{
								LOG.debug("*******Product not applicable for brand restriction:" + productModel.getCode());
								toRemovePromotionList.add(productPromotion);
							}
						}


					}


				}
			} //end promotion for loop
		}
		if (!toRemovePromotionList.isEmpty())
		{
			promotions.removeAll(toRemovePromotionList);
		}

		return promotions;

	}

	private boolean isFreeBeePromotionExists(final ProductPromotionModel productPromotion)
	{
		boolean isFreeBree = false;
		if (productPromotion instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			isFreeBree = true;
		}
		return isFreeBree;
	}

	public List<String> getBrandsForProduct(final ProductModel productModel)
	{
		List<String> brandList = null;
		try
		{

			final List<CategoryModel> categories = getImmediateSuperCategory(productModel);//(List<CategoryModel>) productModel.getSupercategories();
			if (categories != null && !categories.isEmpty())
			{
				brandList = new ArrayList<String>();
				for (final CategoryModel categoryModel : categories)
				{
					if (categoryModel.getCode().startsWith("MBH"))
					{
						brandList.add(categoryModel.getCode());
					}

				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return brandList;
	}

	private List<CategoryModel> getImmediateSuperCategory(final ProductModel product)
	{

		List<CategoryModel> superCategories = new ArrayList<CategoryModel>();
		try
		{
			if (product != null)
			{

				superCategories = (List<CategoryModel>) product.getSupercategories();

			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return superCategories;


	}


	/*
	 * public static class PromotionComparator implements Comparator {
	 *
	 *
	 * public int compare(final Object arg0, final Object arg1) { // YTODO Auto-generated method stub final Integer
	 * number1 = promo1.getPriority(); final Integer number2 = promo2.getPriority();
	 *
	 * if (number1 != null && number2 != null) { return Integer.compare(number1, number2);
	 *
	 * } }
	 *
	 * }
	 */


}