/**
 *
 */
package com.tisl.mpl.promotion.service.impl;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.promotion.service.UpdateSplPriceHelperService;


/**
 * @author TCS
 *
 */
public class DefaultUpdateSplPriceHelperService implements UpdateSplPriceHelperService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultUpdateSplPriceHelperService.class.getName());

	@Autowired
	private CategoryService categoryService;

	/**
	 * Populates the Product Data for Categories
	 *
	 * * TISPRO-352 : Fix
	 *
	 * @param brands
	 * @param rejectBrandList
	 * @param priority
	 * @return dataMap
	 */
	@Override
	public ConcurrentHashMap<List<String>, List<String>> getEligibleProductList(final List<String> brands,
			final List<String> rejectBrandList, final Integer priority, final List<Category> categories)
	{
		final List<CategoryModel> categoryList = getAllCategories(categories);
		List<String> productPKList = null;
		List<String> productCodeList = null;
		ConcurrentHashMap<List<String>, List<String>> dataMap = null;

		if (CollectionUtils.isNotEmpty(categoryList))
		{
			productPKList = new ArrayList<String>();
			productCodeList = new ArrayList<String>();
			dataMap = new ConcurrentHashMap<List<String>, List<String>>();
			LOG.debug("Populating eligible products in List" + "Category List:" + categoryList);
			for (final CategoryModel oModel : categoryList)
			{
				if (CollectionUtils.isNotEmpty(oModel.getProducts()))
				{
					for (final ProductModel product : oModel.getProducts())
					{
						if (getBrandsForProduct(product, rejectBrandList, brands) && validateCategoryProductData(product, priority))
						{
							productPKList.add(product.getPk().toString());
							productCodeList.add(product.getCode());
						}
					}
				}
			}


			if (CollectionUtils.isNotEmpty(productPKList) && CollectionUtils.isNotEmpty(productCodeList))
			{
				dataMap.put(productPKList, productCodeList);
			}
		}
		return dataMap;
	}

	/**
	 * Validates Category Promotion for Priority
	 *
	 * @param product
	 * @param priority
	 */
	private boolean validateCategoryProductData(final ProductModel product, final Integer priority)
	{
		try
		{
			int maxPriority = priority.intValue();
			final List<ProductPromotionModel> promotionData = new ArrayList<ProductPromotionModel>();


			final Collection<CategoryModel> categoriesList = getDefaultPromotionsManager().getcategoryData(product);
			final Collection<ProductPromotionModel> productPromoData = product.getPromotions();

			if (CollectionUtils.isNotEmpty(categoriesList))
			{
				for (final CategoryModel category : categoriesList)
				{
					promotionData.addAll(category.getPromotions());
				}

				if (CollectionUtils.isNotEmpty(productPromoData))
				{
					promotionData.addAll(productPromoData);
				}

				if (CollectionUtils.isNotEmpty(promotionData))
				{
					for (final ProductPromotionModel promotion : promotionData)
					{
						if ((promotion instanceof BuyAPercentageDiscountModel
								&& null != ((BuyAPercentageDiscountModel) promotion).getQuantity() && ((BuyAPercentageDiscountModel) promotion)
								.getQuantity().intValue() == 1)
								|| (promotion instanceof BuyABFreePrecentageDiscountModel
										&& null != ((BuyABFreePrecentageDiscountModel) promotion).getQuantity() && ((BuyABFreePrecentageDiscountModel) promotion)
										.getQuantity().intValue() == 1))
						{
							if (maxPriority < promotion.getPriority().intValue() && BooleanUtils.isTrue(promotion.getEnabled()))
							{
								maxPriority = promotion.getPriority().intValue();
								break;
							}
						}

					}
					if ((priority.intValue() == maxPriority) || (priority.intValue() > maxPriority))
					{
						return true;
					}
				}

			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return false;
	}

	/**
	 * Checks Brand Restriction Data
	 *
	 * @param product
	 * @param rejectBrandList
	 * @param brands
	 * @return allow
	 */
	private boolean getBrandsForProduct(final ProductModel product, final List<String> rejectBrandList, final List<String> brands)
	{
		boolean allow = false;
		List<String> brandList = null;

		try
		{
			if (brands.isEmpty() && rejectBrandList.isEmpty())//no need to proceed if there is no brand restriction
			{
				return true;
			}

			final Collection<CategoryModel> superCategoryList = product.getSupercategories();
			if (CollectionUtils.isNotEmpty(superCategoryList))
			{
				brandList = new ArrayList<String>();
				for (final CategoryModel category : superCategoryList)
				{
					if (category.getCode().startsWith("MBH"))
					{
						brandList.add(category.getCode());
					}
				}
			}

			if (CollectionUtils.isNotEmpty(brands) && CollectionUtils.isNotEmpty(brandList))
			{
				final String productBrand = brandList.get(0);
				if (brands.contains(productBrand))
				{
					allow = true;
				}
			}

			if (CollectionUtils.isNotEmpty(rejectBrandList) && CollectionUtils.isNotEmpty(brandList))
			{
				final String productBrand = brandList.get(0);
				if (rejectBrandList.contains(productBrand))
				{
					allow = false;
				}
				else
				{
					allow = true;
				}
			}

			LOG.debug("******** Special Price - product:" + product.getCode() + " is brand restricted.");
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return allow;
	}

	/**
	 * Returns List of All Categories
	 *
	 * @param categories
	 * @return categoryList
	 */
	private List<CategoryModel> getAllCategories(final List<Category> categories)
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		try
		{
			for (final Category category : categories)
			{
				final CategoryModel oModel = categoryService.getCategoryForCode(getDefaultPromotionsManager().catalogData(),
						category.getCode());
				if (null != oModel)
				{
					categoryList.add(oModel);
					final Collection<CategoryModel> subCategoryList = categoryService.getAllSubcategoriesForCategory(oModel);
					if (CollectionUtils.isNotEmpty(subCategoryList))
					{
						categoryList.addAll(populateSubCategoryData(subCategoryList));
					}
				}
			}
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}
		return categoryList;
	}

	/**
	 * Return Sub Categories except Classification Categories
	 *
	 * @param subCategoryList
	 * @return categoryList
	 */
	private List<CategoryModel> populateSubCategoryData(final Collection<CategoryModel> subCategoryList)
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		for (final CategoryModel category : subCategoryList)
		{
			if (!(category instanceof ClassificationClassModel))
			{
				categoryList.add(category);
			}
		}

		return categoryList;
	}


	/**
	 * Validates Product Priority Logic
	 *
	 * TISPRO-352 : Fix
	 *
	 * @param product
	 * @return boolean
	 */
	@Override
	public boolean validateProductData(final ProductModel product, final Integer priority)
	{
		return validateCategoryProductData(product, priority);
	}

	/**
	 * @return the categoryService
	 */
	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}


}
