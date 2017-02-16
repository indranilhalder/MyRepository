/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;


import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.PromotionPriceUpdaterDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockLevelPromotionCheckService;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionPriceUpdaterService;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.EtailExcludeSellerSpecificRestrictionModel;
import com.tisl.mpl.model.EtailLimitedStockRestrictionModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.ExcludeManufacturersRestrictionModel;
import com.tisl.mpl.model.ManufacturersRestrictionModel;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.promotion.dao.impl.UpdatePromotionalPriceDaoImpl;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */

public class DefaultPromotionPriceUpdaterServiceImpl implements PromotionPriceUpdaterService
{
	@SuppressWarnings("boxing")
	@Resource
	private ModelService modelService;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultPromotionPriceUpdaterServiceImpl.class.getName());

	@Autowired
	private PromotionPriceUpdaterDao promotionPriceUpdaterDao;


	@Resource(name = "mplUpdatePromotionPriceDao")
	private UpdatePromotionalPriceDaoImpl updatePromotionalPriceDao;

	@Autowired
	private ProductService productService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ConfigurationService configurationService;

	//private UpdateSplPriceHelperService updateSplPriceHelperService;
	@Autowired
	private CategoryService categoryService;

	//SONAR FIX-STRING LITERAL USED MULTIPLE TIMES
	private final String CATLIST = " *** categoryList:";

	@Resource(name = "stockPromoCheckService")
	private ExtStockLevelPromotionCheckService stockPromoCheckService;


	/**
	 * @description getRequiredPromotion
	 * @param mplConfigDate
	 */
	@Override
	public List<BuyAPercentageDiscountModel> getRequiredPromotion(final Date mplConfigDate)
	{
		// YTODO Auto-generated method stub

		return promotionPriceUpdaterDao.getRequiredPromotionList(mplConfigDate);
	}




	/**
	 * @description poulatePriceRowData
	 * @param BuyAPercentageDiscountModel
	 */
	@Override
	public boolean poulatePriceRowData(final BuyAPercentageDiscountModel buyAPercentageDiscount)
	{

		boolean errorFlag = false;
		Double price = Double.valueOf(0.0D);
		Double maxDiscount = Double.valueOf(0.0D);
		boolean isPercentage = false;
		boolean isEnabled = false;
		Long quantity = Long.getLong("0L");
		List<ProductModel> productList = null;
		//List<Product> productJaloList = null;
		List<CategoryModel> categoryList = null;
		//final List<Category> categoryJaloList = null;
		Date startDate = null;
		Date endDate = null;

		Integer priority = Integer.valueOf(0);
		final List<String> sellerList = new ArrayList<String>();
		final List<String> brandList = new ArrayList<String>();
		String promoCode = MarketplacecommerceservicesConstants.EMPTYSPACE;
		final List<String> rejectSellerList = new ArrayList<String>();
		final List<String> rejectBrandList = new ArrayList<String>();
		List<ProductModel> exproductList = null;
		boolean isStockRestriction = false;
		int stockCount = 0;
		try
		{
			if (null != buyAPercentageDiscount)
			{
				//Bug Fix
				//final BuyAPercentageDiscount buyAPerDiscountPromotion = (BuyAPercentageDiscount) buyAPercentageDiscount;
				promoCode = buyAPercentageDiscount.getCode();

				for (final AbstractPromotionRestrictionModel res : buyAPercentageDiscount.getRestrictions())
				{
					if (res instanceof EtailSellerSpecificRestrictionModel)
					{

						final List<SellerMasterModel> sellerMasterList = ((EtailSellerSpecificRestrictionModel) res)
								.getSellerMasterList();
						for (final SellerMasterModel seller : sellerMasterList)
						{
							sellerList.add(seller.getId());
						}
					}
					if (res instanceof ManufacturersRestrictionModel)
					{
						final ManufacturersRestrictionModel brandRestriction = (ManufacturersRestrictionModel) res;
						final List<CategoryModel> brandRestrictions = new ArrayList<CategoryModel>(brandRestriction.getManufacturers());
						for (final CategoryModel code : brandRestrictions)
						{
							brandList.add(code.getCode());
						}
					}

					if (res instanceof EtailExcludeSellerSpecificRestrictionModel)
					{
						final List<SellerMasterModel> sellerMasterList = ((EtailExcludeSellerSpecificRestrictionModel) res)
								.getSellerMasterList();
						for (final SellerMasterModel seller : sellerMasterList)
						{
							rejectSellerList.add(seller.getId());
						}
					}

					if (res instanceof ExcludeManufacturersRestrictionModel)
					{
						final ExcludeManufacturersRestrictionModel brandRestriction = (ExcludeManufacturersRestrictionModel) res;
						final List<CategoryModel> brandRestrictions = new ArrayList<CategoryModel>(brandRestriction.getManufacturers());
						for (final CategoryModel code : brandRestrictions)
						{
							rejectBrandList.add(code.getCode());
						}
					}
					//changes for stock restriction TPR-965
					if (res instanceof EtailLimitedStockRestrictionModel)
					{
						isStockRestriction = true;
						stockCount = ((EtailLimitedStockRestrictionModel) res).getMaxStock() == null ? 0
								: ((EtailLimitedStockRestrictionModel) res).getMaxStock().intValue();
						//if(nul!!)
					}
				}
				//Bug Fix ends

				if (null != buyAPercentageDiscount.getPriority())
				{
					priority = buyAPercentageDiscount.getPriority();
				}

				if (null != buyAPercentageDiscount.getPercentageOrAmount())
				{
					isPercentage = buyAPercentageDiscount.getPercentageOrAmount().booleanValue();
				}
				if (null != buyAPercentageDiscount.getProducts())
				{
					productList = new ArrayList<ProductModel>(buyAPercentageDiscount.getProducts());
				}

				if (null != buyAPercentageDiscount.getCategories())
				{
					categoryList = new ArrayList<CategoryModel>(buyAPercentageDiscount.getCategories());
				}

				if (null != buyAPercentageDiscount.getStartDate() && null != buyAPercentageDiscount.getEndDate())
				{
					startDate = buyAPercentageDiscount.getStartDate();
					endDate = buyAPercentageDiscount.getEndDate();
				}

				if (null != buyAPercentageDiscount.getEnabled())
				{
					isEnabled = buyAPercentageDiscount.getEnabled().booleanValue();
				}

				if (null != buyAPercentageDiscount.getQuantity())
				{
					quantity = buyAPercentageDiscount.getQuantity();
				}

				if (null != buyAPercentageDiscount.getMaxDiscountVal())
				{
					maxDiscount = buyAPercentageDiscount.getMaxDiscountVal();
				}
				if (null != buyAPercentageDiscount.getExcludedProducts())
				{
					exproductList = new ArrayList<ProductModel>(buyAPercentageDiscount.getExcludedProducts());
				}

				List<String> ussidList = new ArrayList<String>();
				//tpr-965 CHANGES FOR PRICE UPDATEA
				if (isStockRestriction && stockCount > 0)
				{
					ussidList = stockPromoCheckService.getStockForPromotion(promoCode, stockCount);
				}
				price = getDiscountPrice(isPercentage, buyAPercentageDiscount);

				/*
				 * productJaloList = new ArrayList<Product>((Collection<? extends Product>) Arrays.asList(productList));
				 * categoryJaloList = new ArrayList<Category>((Collection<? extends Category>) Arrays.asList(categoryList));
				 */
				if ((null != productList && !productList.isEmpty()) && null != startDate && null != endDate && isEnabled
						&& quantity.intValue() == 1)
				{
					LOG.debug("******** Special price check for product list:" + productList + " *** percentage discount:"
							+ isPercentage);

					if (isPercentage)
					{
						updatePromotionalPrice(productList, null, price, startDate, endDate, true, priority, sellerList, brandList,
								promoCode, rejectSellerList, rejectBrandList, maxDiscount, exproductList, ussidList);
					}
					else
					{
						updatePromotionalPrice(productList, null, price, startDate, endDate, false, priority, sellerList, brandList,
								promoCode, rejectSellerList, rejectBrandList, maxDiscount, exproductList, ussidList);
					}

				}
				else if ((null != categoryList && !categoryList.isEmpty()) && null != startDate && null != endDate && isEnabled
						&& quantity.intValue() == 1)
				{
					LOG.debug("******** Special price check for product list in category:" + productList + " *** percentage discount:"
							+ isPercentage);
					if (isPercentage)
					{
						updatePromotionalPrice(null, categoryList, price, startDate, endDate, true, priority, sellerList, brandList,
								promoCode, rejectSellerList, rejectBrandList, maxDiscount, exproductList, ussidList);
					}
					else
					{
						updatePromotionalPrice(null, categoryList, price, startDate, endDate, false, priority, sellerList, brandList,
								promoCode, rejectSellerList, rejectBrandList, maxDiscount, exproductList, ussidList);
					}
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& !isEnabled && quantity.intValue() == 1)
				{
					LOG.debug("******** Special price check disabling promotion, productlist impacted:" + productList + CATLIST
							+ categoryList);
					disablePromotionalPrice(productList, categoryList, isEnabled, priority, brandList, quantity, rejectSellerList,
							rejectBrandList, promoCode);
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& quantity.intValue() > 1) // If Qauntity is increased from 1 to Multiple //Fix for TISPRD-383
				{

					LOG.debug("******** Special price check disabling promotion, productlist impacted:" + productList + CATLIST
							+ categoryList);
					disablePromotionalPrice(productList, categoryList, isEnabled, priority, brandList, quantity, rejectSellerList,
							rejectBrandList, promoCode);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error("******** Special price check disabling promotion, productlist error:" + productList + CATLIST + categoryList
					+ "PROMOTION CODE:" + promoCode);
			errorFlag = true;
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("******** Special price check disabling promotion, productlist error:" + productList + CATLIST + categoryList
					+ "PROMOTION CODE:" + promoCode);
			errorFlag = true;
			throw e;
		}
		catch (final Exception e)
		{
			LOG.error("******** Special price check disabling promotion, productlist error:" + productList + CATLIST + categoryList
					+ "PROMOTION CODE:" + promoCode);
			errorFlag = true;
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return errorFlag;
	}



	/**
	 * @Description : Save Cron Job Details
	 * @param code
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		return updatePromotionalPriceDao.getCronDetails(code);
	}

	/**
	 * @Description : Save CronJob Configuration Details
	 * @param: startTime
	 * @param: code
	 */
	@Override
	public void saveCronData(final CronJobModel cronModel)
	{
		if (null != cronModel && null != cronModel.getStartTime() && null != cronModel.getCode())
		{
			final MplConfigurationModel oModel = updatePromotionalPriceDao.getCronDetails(cronModel.getCode());
			if (null != oModel && null != oModel.getMplConfigCode())
			{
				LOG.debug("Saving CronJob Run Time :" + cronModel.getStartTime());
				oModel.setMplConfigDate(cronModel.getStartTime());
				modelService.save(oModel);
				LOG.debug("Cron Job Details Saved for Code :" + cronModel.getCode());
			}
		}

	}


	private Double getDiscountPrice(final boolean isPercentage, final BuyAPercentageDiscountModel buyAPercentageDiscount)
	{
		Double price = new Double(0.0);
		try
		{
			if (!isPercentage)
			{
				price = setDiscountPrice(buyAPercentageDiscount, false);
			}
			else if (null != buyAPercentageDiscount.getDiscountPrices())
			{
				price = setDiscountPrice(buyAPercentageDiscount, true);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}
		return price;
	}



	/**
	 * @Description: To Set Discount Price
	 * @param initialValues
	 * @return price
	 */
	private Double setDiscountPrice(final ProductPromotionModel promoDiscount, final boolean flag)
	{
		Double price = new Double(0.0);
		try
		{
			if (promoDiscount instanceof BuyAPercentageDiscountModel && flag)
			{
				final BuyAPercentageDiscountModel discountPromo = (BuyAPercentageDiscountModel) promoDiscount;
				price = discountPromo.getPercentageDiscount();
			}

			if (!flag)
			{
				List<PromotionPriceRowModel> priceRowData = null;
				if (promoDiscount instanceof BuyAPercentageDiscountModel)
				{
					final BuyAPercentageDiscountModel discountPromo = (BuyAPercentageDiscountModel) promoDiscount;
					priceRowData = new ArrayList<PromotionPriceRowModel>(discountPromo.getDiscountPrices());
				}
				//				if (promoDiscount instanceof LimitedStockPromotionModel)
				//				{
				//					final LimitedStockPromotionModel discountPromo = (LimitedStockPromotionModel) promoDiscount;
				//					priceRowData = new ArrayList<PromotionPriceRowModel>(discountPromo.getDiscountPrices());
				//				}
				for (final PromotionPriceRowModel priceRow : priceRowData)
				{
					if (null != priceRow.getPrice())
					{
						price = priceRow.getPrice();
					}
				}
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
		}

		return price;
	}

	private void updatePromotionalPrice(final List<ProductModel> products, final List<CategoryModel> categories, Double value,
			final Date startDate, final Date endtDate, final boolean percent, final Integer priority, final List<String> sellers,
			final List<String> brands, final String promoCode, final List<String> rejectSellerList,
			final List<String> rejectBrandList, final Double maxDiscount, final List<ProductModel> exproductListdata,
			final List<String> ussidList)
	{

		try
		{
			clearExistingData(promoCode);
			final List<String> product = new ArrayList<String>();
			List<String> stagedProductList = new ArrayList<String>();
			final List<String> promoproductList = new ArrayList<String>();
			final List<PriceRowModel> priceList = new ArrayList<PriceRowModel>();
			List<String> exProductList = new ArrayList<String>();


			if (CollectionUtils.isNotEmpty(exproductListdata))
			{
				exProductList = getExcludedProductData(exproductListdata);
			}

			if (CollectionUtils.isNotEmpty(products))
			{
				for (final ProductModel itrProduct : products)
				{
					if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority))
					{
						//product.add(itrProduct.getAttribute("pk").toString());
						product.add(itrProduct.getPk().toString());
						promoproductList.add(itrProduct.getCode()); //For staged Product Details
					}
				}
			}

			if (CollectionUtils.isNotEmpty(categories))
			{
				//TISPRO-352 : Fix
				final ConcurrentHashMap<List<String>, List<String>> categoryDetailsMap = getProductListWitoutExProduct(brands,
						rejectBrandList, priority, categories, exProductList);
				if (MapUtils.isNotEmpty(categoryDetailsMap))
				{
					for (final ConcurrentHashMap.Entry<List<String>, List<String>> entry : categoryDetailsMap.entrySet())
					{
						product.addAll(entry.getKey());
						promoproductList.addAll(entry.getValue());
						LOG.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
					}
				}
			}

			LOG.debug("******** Special Price - Promotion Applicable product List:" + product);
			//filter product list based on brand restriction


			if (!product.isEmpty())
			{
				stagedProductList = getStagedProductDetails(promoproductList); // For adding the staged catalog price Row for Product
				if (CollectionUtils.isNotEmpty(stagedProductList))
				{
					product.addAll(stagedProductList);
				}

				boolean updateSpecialPrice = false;
				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product);
				for (final PriceRowModel price : priceRow)
				{
					//updating price row
					boolean isEligibletoDisable = false;
					if (CollectionUtils.isEmpty(sellers) && CollectionUtils.isEmpty(rejectSellerList))
					{
						updateSpecialPrice = true;//set the flag true if there is no seller restriction
					}
					else
					{
						updateSpecialPrice = isPriceToUpdate(price, sellers, rejectSellerList);
						if (!updateSpecialPrice)
						{
							isEligibletoDisable = true;
						}
					}

					if (updateSpecialPrice)
					{
						//tpr-965 CHANGES FOR PRICE UPDATE
						if (CollectionUtils.isNotEmpty(ussidList) && ussidList.contains(price.getSellerArticleSKU()))
						{
							value = Double.valueOf(0.0);
						}
						if (!percent)
						{
							price.setPromotionStartDate(startDate);
							price.setPromotionEndDate(endtDate);
							price.setIsPercentage(Boolean.valueOf(percent));
							price.setPromotionValue(value);
							price.setPromotionIdentifier(promoCode);
							price.setMaxDiscount(maxDiscount);
						}
						else
						{
							price.setPromotionStartDate(startDate);
							price.setPromotionEndDate(endtDate);
							price.setIsPercentage(Boolean.valueOf(percent));
							price.setPromotionValue(value);
							price.setPromotionIdentifier(promoCode);
							price.setMaxDiscount(maxDiscount);
						}
						priceList.add(price);
					}
					else if (!updateSpecialPrice && isEligibletoDisable)
					{
						LOG.debug("Removing Promotion Details from the Price Row : USSID not eligible for Promotion");
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);
						price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
						price.setMaxDiscount(null);

						LOG.debug("Saving Price Row ");
						priceList.add(price);
					}

				}

				if (CollectionUtils.isNotEmpty(priceList))
				{
					modelService.saveAll(priceList);
				}
			}

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	private List<String> getExcludedProductData(final List<ProductModel> exproductList)
	{
		final List<String> exProductList = new ArrayList<String>();

		for (final ProductModel product : exproductList)
		{
			if (StringUtils.isNotEmpty(product.getCode()))
			{
				exProductList.add(product.getCode());
			}
		}

		return exProductList;
	}

	private void clearExistingData(final String promoCode)
	{
		if (StringUtils.isNotEmpty(promoCode))
		{
			final List<PriceRowModel> priceRowList = updatePromotionalPriceDao.fetchPromoPriceData(promoCode);
			final List<PriceRowModel> finalList = new ArrayList<PriceRowModel>();
			if (CollectionUtils.isNotEmpty(priceRowList))
			{
				for (final PriceRowModel price : priceRowList)
				{
					price.setPromotionStartDate(null);
					price.setPromotionEndDate(null);
					price.setIsPercentage(null);
					price.setPromotionValue(null);
					price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
					price.setMaxDiscount(null);

					finalList.add(price);
				}

				if (CollectionUtils.isNotEmpty(finalList))
				{
					modelService.saveAll(finalList);
				}
			}
		}
	}



	private List<String> getStagedProductDetails(final List<String> productList)
	{
		final List<String> stagedProductList = new ArrayList<String>();

		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(configurationService
				.getConfiguration().getString("cronjob.promotion.catelog"), MarketplacecommerceservicesConstants.STAGED);

		if (CollectionUtils.isNotEmpty(productList))
		{
			for (final String productCode : productList)
			{
				final ProductModel product = productService.getProductForCode(catalogVersionModel, productCode);
				stagedProductList.add(product.getPk().toString());
			}
		}

		return stagedProductList;
	}

	private boolean validateProductData(final ProductModel product, final Integer priority)
	{
		boolean flag = false;
		try
		{
			final ProductModel oModel = productService.getProductForCode(getDefaultPromotionsManager().catalogData(),
					product.getCode());
			if (null != oModel)
			{
				flag = validateCategoryProductData(oModel, priority);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return flag;
	}

	private boolean getBrandsForProduct(final ProductModel product, final List<String> brands, final List<String> rejectBrandList)
	{
		boolean allow = false;
		List<String> brandList = null;

		try
		{

			if (brands.isEmpty() && rejectBrandList.isEmpty())//no need to proceed if there is no brand restriction
			{
				return true;
			}

			final List<CategoryModel> categories = getImmediateSuperCategory(product);
			if (categories != null && !categories.isEmpty())
			{
				brandList = new ArrayList<String>();
				for (final CategoryModel category : categories)
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


	private List<CategoryModel> getImmediateSuperCategory(final ProductModel product)
	{
		List<CategoryModel> superCategories = new ArrayList<CategoryModel>();

		if (product != null)
		{

			try
			{
				superCategories = (List<CategoryModel>) product.getSupercategories();
			}
			catch (final JaloInvalidParameterException e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}
			catch (final Exception e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}

		}

		return superCategories;
	}



	private boolean isPriceToUpdate(final PriceRowModel price, final List<String> sellers, final List<String> rejectSellerList)
	{
		boolean updateSpecialPrice = false;
		final List<SellerInformationModel> sellerModels = new ArrayList<SellerInformationModel>(price.getProduct()
				.getSellerInformationRelator());

		if (CollectionUtils.isNotEmpty(sellers))
		{
			for (final SellerInformationModel seller : sellerModels)
			{
				if (sellers.contains(seller.getSellerID())
						&& price.getSellerArticleSKU().equalsIgnoreCase(seller.getSellerArticleSKU()))
				{
					LOG.debug("******** Special Price - Promotion Applicable ussid List:" + seller.getSellerArticleSKU());
					updateSpecialPrice = true;
				}
			}
		}
		else if (CollectionUtils.isNotEmpty(rejectSellerList))
		{
			for (final SellerInformationModel seller : sellerModels)
			{
				if (rejectSellerList.contains(seller.getSellerID())
						&& price.getSellerArticleSKU().equalsIgnoreCase(seller.getSellerArticleSKU()))
				{
					LOG.debug("******** Special Price - Promotion Applicable ussid List:" + seller.getSellerArticleSKU());
					updateSpecialPrice = false;
					break;
				}
				else
				{
					updateSpecialPrice = true;
				}
			}
		}


		return updateSpecialPrice;
	}


	private void disablePromotionalPrice(final List<ProductModel> products, final List<CategoryModel> categories,
			final boolean isEnabled, final Integer priority, final List<String> brands, final Long quantity,
			final List<String> rejectSellerList, final List<String> rejectBrandList, final String promoCode)
	{
		try
		{
			clearExistingData(promoCode);
			final List<String> product = new ArrayList<String>();
			List<String> stagedProductList = new ArrayList<String>();
			final List<String> promoproductList = new ArrayList<String>();
			final List<PriceRowModel> priceList = new ArrayList<PriceRowModel>();

			if (CollectionUtils.isNotEmpty(products))
			{
				for (final ProductModel itrProduct : products)
				{
					if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority))
					{
						product.add(itrProduct.getPk().toString());
						promoproductList.add(itrProduct.getCode());
					}
				}
			}

			if (CollectionUtils.isNotEmpty(categories))
			{
				//TISPRO-352 : Fix
				final ConcurrentHashMap<List<String>, List<String>> categoryDetailsMap = getEligibleProductList(brands,
						rejectBrandList, priority, categories);
				if (MapUtils.isNotEmpty(categoryDetailsMap))
				{
					for (final ConcurrentHashMap.Entry<List<String>, List<String>> entry : categoryDetailsMap.entrySet())
					{
						product.addAll(entry.getKey());
						promoproductList.addAll(entry.getValue());
						LOG.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
					}
				}
			}


			LOG.debug("******** Special Price - Disable Promotion Applicable product List:" + product);


			if (!product.isEmpty())
			{
				stagedProductList = getStagedProductDetails(promoproductList); // For adding the staged catalog price Row for Product
				if (CollectionUtils.isNotEmpty(stagedProductList))
				{
					product.addAll(stagedProductList);
				}

				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product);
				for (final PriceRowModel price : priceRow)
				{
					if (!isEnabled)
					{
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);
						price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
						price.setMaxDiscount(null);
					}
					else if (null != quantity && quantity.longValue() > 1) //For TISPRD-383 : If Validated remove Special Price Details
					{
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);
						price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
						price.setMaxDiscount(null);
					}
					priceList.add(price);
				}

				if (CollectionUtils.isNotEmpty(priceList))
				{
					modelService.save(priceList);
				}

			}
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
	}


	private ConcurrentHashMap<List<String>, List<String>> getEligibleProductList(final List<String> brands,
			final List<String> rejectBrandList, final Integer priority, final List<CategoryModel> categories)
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

	private List<CategoryModel> getAllCategories(final List<CategoryModel> categories)
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		try
		{
			for (final CategoryModel category : categories)
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
						if (promotion instanceof BuyAPercentageDiscountModel
								&& null != ((BuyAPercentageDiscountModel) promotion).getQuantity()
								&& ((BuyAPercentageDiscountModel) promotion).getQuantity().intValue() == 1)
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


	public ConcurrentHashMap<List<String>, List<String>> getProductListWitoutExProduct(final List<String> brands,
			final List<String> rejectBrandList, final Integer priority, final List<CategoryModel> categories,
			final List<String> exProductList)
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
						if (getBrandsForProduct(product, rejectBrandList, brands) && validateCategoryProductData(product, priority)
								&& validateExclusion(exProductList, product))
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


	private boolean validateExclusion(final List<String> exProductList, final ProductModel product)
	{
		boolean flag = true;

		if (CollectionUtils.isNotEmpty(exProductList) && exProductList.contains(product.getCode()))
		{
			flag = false;
		}

		return flag;
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
