/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;


import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.PromotionalPriceRowModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.PromotionPriceUpdaterDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockLevelPromotionCheckService;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionPriceUpdaterService;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
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
	public List<ProductPromotionModel> getRequiredPromotion(final Date mplConfigDate)
	{

		return promotionPriceUpdaterDao.getRequiredPromotionList(mplConfigDate);
	}




	@Override
	public boolean poulatePriceRowData(final ProductPromotionModel promo)
	{
		boolean isPromoValid = false;


		if (promo instanceof BuyABFreePrecentageDiscountModel)
		{
			final BuyABFreePrecentageDiscountModel discount = (BuyABFreePrecentageDiscountModel) promo;
			isPromoValid = poulatePriceRowDataForBuyAPercentageDiscount(discount);
		}
		else if (promo instanceof BuyAPercentageDiscountModel)
		{
			final BuyAPercentageDiscountModel discount = (BuyAPercentageDiscountModel) promo;
			isPromoValid = poulatePriceRowDataForBuyAPercentageDiscount(discount);
		}

		return isPromoValid;

	}


	public boolean poulatePriceRowDataForBuyAPercentageDiscount(final BuyABFreePrecentageDiscountModel percentegeDiscount)
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
		//final List<String> brandList = new ArrayList<String>();
		String promoCode = MarketplacecommerceservicesConstants.EMPTYSPACE;
		final List<String> rejectSellerList = new ArrayList<String>();
		//final List<String> rejectBrandList = new ArrayList<String>();
		List<CategoryModel> brandRestrictions = null;
		List<CategoryModel> excludeBrandRestrictions = null;
		List<ProductModel> exproductList = null;
		boolean isStockRestriction = false;
		int stockCount = 0;

		List<String> channel = new ArrayList<String>();
		List<String> ussidList = new ArrayList<String>();//sonar fix

		try
		{
			if (null != percentegeDiscount)
			{
				//Bug Fix
				//final BuyAPercentageDiscount buyAPerDiscountPromotion = (BuyAPercentageDiscount) buyAPercentageDiscount;
				promoCode = percentegeDiscount.getCode();
				for (final AbstractPromotionRestrictionModel res : percentegeDiscount.getRestrictions())
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
						brandRestrictions = new ArrayList<CategoryModel>(brandRestriction.getManufacturers());
						//						for (final CategoryModel code : brandRestrictions)
						//						{
						//							brandList.add(code.getCode());
						//						}
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
						excludeBrandRestrictions = new ArrayList<CategoryModel>(brandRestriction.getManufacturers());
						//						for (final CategoryModel code : excludeBrandRestrictions)
						//						{
						//							rejectBrandList.add(code.getCode());
						//						}
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

				if (null != percentegeDiscount.getPriority())
				{
					priority = percentegeDiscount.getPriority();
				}
				if (null != percentegeDiscount.getPercentageOrAmount())
				{
					isPercentage = percentegeDiscount.getPercentageOrAmount().booleanValue();
				}
				if (null != percentegeDiscount.getProducts())
				{
					productList = new ArrayList<ProductModel>(percentegeDiscount.getProducts());
				}

				if (null != percentegeDiscount.getCategories())
				{
					categoryList = new ArrayList<CategoryModel>(percentegeDiscount.getCategories());
				}

				if (null != percentegeDiscount.getStartDate() && null != percentegeDiscount.getEndDate())
				{
					startDate = percentegeDiscount.getStartDate();
					endDate = percentegeDiscount.getEndDate();
				}

				if (null != percentegeDiscount.getEnabled())
				{
					isEnabled = percentegeDiscount.getEnabled().booleanValue();
				}

				if (null != percentegeDiscount.getQuantity())
				{
					quantity = percentegeDiscount.getQuantity();
				}

				if (null != percentegeDiscount.getMaxDiscountVal())
				{
					maxDiscount = percentegeDiscount.getMaxDiscountVal();
				}
				if (null != percentegeDiscount.getExcludedProducts())
				{
					exproductList = new ArrayList<ProductModel>(percentegeDiscount.getExcludedProducts());
				}


				//tpr-965 CHANGES FOR PRICE UPDATEA
				//sonar fix
				//List<String> ussidList = new ArrayList<String>();
				if (isStockRestriction && stockCount > 0)
				{
					ussidList = stockPromoCheckService.getStockForPromotion(promoCode, stockCount);
					LOG.debug("The ussid list is" + ussidList);//sonar fix
				}




				if (CollectionUtils.isNotEmpty(percentegeDiscount.getChannel()))
				{
					channel = getChannel(percentegeDiscount.getChannel());
				}

				price = getDiscountPriceForBuyABDiscount(isPercentage, percentegeDiscount);

				/*
				 * productJaloList = new ArrayList<Product>((Collection<? extends Product>) Arrays.asList(productList));
				 * categoryJaloList = new ArrayList<Category>((Collection<? extends Category>) Arrays.asList(categoryList));
				 */
				//**updating promo price for product list

				final Date sysdate = new Date();

				if ((null != productList && !productList.isEmpty()) && startDate.compareTo(sysdate) <= 0
						&& endDate.compareTo(sysdate) >= 0 && isEnabled && quantity.intValue() == 1)
				{

					LOG.debug("******** Special price check for product list:" + productList + " *** percentage discount:"
							+ isPercentage);
					if (isPercentage)
					{
						updatePromotionalPrice(productList, null, price, startDate, endDate, true, priority, sellerList,
								brandRestrictions, percentegeDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}
					else
					{
						updatePromotionalPrice(productList, null, price, startDate, endDate, false, priority, sellerList,
								brandRestrictions, percentegeDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}

				}
				else if ((null != categoryList && !categoryList.isEmpty()) && startDate.compareTo(sysdate) <= 0
						&& endDate.compareTo(sysdate) >= 0 && isEnabled && quantity.intValue() == 1)
				{
					LOG.debug("******** Special price check for product list in category:" + productList
							+ MarketplacecommerceservicesConstants.PDISCOUNT + isPercentage);
					if (isPercentage)
					{
						updatePromotionalPrice(null, categoryList, price, startDate, endDate, true, priority, sellerList,
								brandRestrictions, percentegeDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}
					else
					{
						updatePromotionalPrice(null, categoryList, price, startDate, endDate, false, priority, sellerList,
								brandRestrictions, percentegeDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& !isEnabled && quantity.intValue() == 1)
				{
					LOG.debug(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTION + productList + CATLIST + categoryList);
					//					disablePromotionalPrice(productList, categoryList, isEnabled, priority, brandList, quantity, rejectSellerList,
					//							rejectBrandList, promoCode);
					disablePromotionalPrice(promoCode);
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& quantity.intValue() > 1) // If Qauntity is increased from 1 to Multiple //Fix for TISPRD-383
				{

					LOG.debug(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTION + productList + CATLIST + categoryList);
					//					disablePromotionalPrice(productList, categoryList, isEnabled, priority, brandList, quantity, rejectSellerList,
					//							rejectBrandList, promoCode);
					disablePromotionalPrice(promoCode);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTIONERROR + productList + CATLIST + categoryList
					+ MarketplacecommerceservicesConstants.PROMOTIONCODE + promoCode);
			errorFlag = true;
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTIONERROR + productList + CATLIST + categoryList
					+ MarketplacecommerceservicesConstants.PROMOTIONCODE + promoCode);
			errorFlag = true;
			throw e;
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTIONERROR + productList + CATLIST + categoryList
					+ MarketplacecommerceservicesConstants.PROMOTIONCODE + promoCode);
			errorFlag = true;
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return errorFlag;
	}

	/**
	 * Removes Promotion Content in Price Rows
	 *
	 * @param promoCode
	 */
	private void disablePromotionalPrice(final String promoCode)
	{
		try
		{
			clearExistingData(promoCode);
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

	/**
	 * @description poulatePriceRowData
	 * @param buyAPercentageDiscount
	 */

	public boolean poulatePriceRowDataForBuyAPercentageDiscount(final BuyAPercentageDiscountModel buyAPercentageDiscount)
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
		//final List<String> brandList = new ArrayList<String>();
		String promoCode = MarketplacecommerceservicesConstants.EMPTYSPACE;
		final List<String> rejectSellerList = new ArrayList<String>();
		//final List<String> rejectBrandList = new ArrayList<String>();
		List<CategoryModel> brandRestrictions = null;
		List<CategoryModel> excludeBrandRestrictions = null;
		List<ProductModel> exproductList = null;
		List<String> ussidList = new ArrayList<String>();//sonar fix
		boolean isStockRestriction = false;
		int stockCount = 0;

		List<String> channel = new ArrayList<String>();
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
						brandRestrictions = new ArrayList<CategoryModel>(brandRestriction.getManufacturers());
						//						for (final CategoryModel code : brandRestrictions)
						//						{
						//							brandList.add(code.getCode());
						//						}
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
						excludeBrandRestrictions = new ArrayList<CategoryModel>(brandRestriction.getManufacturers());
						//						for (final CategoryModel code : brandRestrictions)
						//						{
						//							rejectBrandList.add(code.getCode());
						//						}
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

				//sonar fix
				//List<String> ussidList = new ArrayList<String>();
				//tpr-965 CHANGES FOR PRICE UPDATEA
				if (isStockRestriction && stockCount > 0)
				{
					ussidList = stockPromoCheckService.getStockForPromotion(promoCode, stockCount);
					LOG.debug("The ussidList is" + ussidList);//sonar fix
				}

				if (CollectionUtils.isNotEmpty(buyAPercentageDiscount.getChannel()))
				{
					channel = getChannel(buyAPercentageDiscount.getChannel());
				}

				price = getDiscountPrice(isPercentage, buyAPercentageDiscount);

				/*
				 * productJaloList = new ArrayList<Product>((Collection<? extends Product>) Arrays.asList(productList));
				 * categoryJaloList = new ArrayList<Category>((Collection<? extends Category>) Arrays.asList(categoryList));
				 */
				//**updating promo price for product list
				final Date sysdate = new Date();
				if ((null != productList && !productList.isEmpty()) && startDate.compareTo(sysdate) <= 0
						&& endDate.compareTo(sysdate) >= 0 && isEnabled && quantity.intValue() == 1)
				{

					LOG.debug("******** Special price check for product list:" + productList + " *** percentage discount:"
							+ isPercentage);

					if (isPercentage)
					{
						updatePromotionalPrice(productList, null, price, startDate, endDate, true, priority, sellerList,
								brandRestrictions, buyAPercentageDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}
					else
					{
						updatePromotionalPrice(productList, null, price, startDate, endDate, false, priority, sellerList,
								brandRestrictions, buyAPercentageDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}

				}
				else if ((null != categoryList && !categoryList.isEmpty()) && startDate.compareTo(sysdate) <= 0
						&& endDate.compareTo(sysdate) >= 0 && isEnabled && quantity.intValue() == 1)
				{
					LOG.debug("******** Special price check for product list in category:" + productList
							+ MarketplacecommerceservicesConstants.PDISCOUNT + isPercentage);
					if (isPercentage)
					{
						updatePromotionalPrice(null, categoryList, price, startDate, endDate, true, priority, sellerList,
								brandRestrictions, buyAPercentageDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}
					else
					{
						updatePromotionalPrice(null, categoryList, price, startDate, endDate, false, priority, sellerList,
								brandRestrictions, buyAPercentageDiscount, rejectSellerList, excludeBrandRestrictions, maxDiscount,
								exproductList, channel);
					}
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& !isEnabled && quantity.intValue() == 1)
				{
					LOG.debug(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTION + productList + CATLIST + categoryList);
					//					disablePromotionalPrice(productList, categoryList, isEnabled, priority, brandList, quantity, rejectSellerList,
					//							rejectBrandList, promoCode);
					disablePromotionalPrice(promoCode);
				}
				else if ((null != categoryList && !categoryList.isEmpty()) || ((null != productList && !productList.isEmpty()))
						&& quantity.intValue() > 1) // If Qauntity is increased from 1 to Multiple //Fix for TISPRD-383
				{

					LOG.debug(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTION + productList + CATLIST + categoryList);
					//					disablePromotionalPrice(productList, categoryList, isEnabled, priority, brandList, quantity, rejectSellerList,
					//							rejectBrandList, promoCode);
					disablePromotionalPrice(promoCode);
				}
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTIONERROR + productList + CATLIST + categoryList
					+ MarketplacecommerceservicesConstants.PROMOTIONCODE + promoCode);
			errorFlag = true;
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTIONERROR + productList + CATLIST + categoryList
					+ MarketplacecommerceservicesConstants.PROMOTIONCODE + promoCode);
			errorFlag = true;
			throw e;
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.SPECIALPRICEPROMOTIONERROR + productList + CATLIST + categoryList
					+ MarketplacecommerceservicesConstants.PROMOTIONCODE + promoCode);
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

	/*
	 * fetching discounted price based on percentage
	 */

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
	 *
	 * @param isPercentage
	 * @param buyAPercentageDiscount
	 * @return price
	 */
	private Double getDiscountPriceForBuyABDiscount(final boolean isPercentage,
			final BuyABFreePrecentageDiscountModel buyAPercentageDiscount)
	{
		Double price = new Double(0.0);
		try
		{
			if (!isPercentage)
			{
				price = setDiscountPriceForBuyABFreePercentegeDiscount(buyAPercentageDiscount, false);
			}
			else if (null != buyAPercentageDiscount.getDiscountPrices())
			{
				price = setDiscountPriceForBuyABFreePercentegeDiscount(buyAPercentageDiscount, true);
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
	 * @param promoDiscount
	 * @param flag
	 * @return flag
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

	/**
	 * @Description: To Set Discount Price
	 * @param buyAPercentageDiscount
	 * @return flag
	 */
	private Double setDiscountPriceForBuyABFreePercentegeDiscount(final BuyABFreePrecentageDiscountModel buyAPercentageDiscount,
			final boolean flag)
	{
		Double price = new Double(0.0);
		try
		{
			if (flag)
			{
				price = buyAPercentageDiscount.getPercentageDiscount();
			}
			else
			{
				final List<PromotionPriceRowModel> priceRowData = new ArrayList<PromotionPriceRowModel>(
						buyAPercentageDiscount.getDiscountPrices());

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



	private void updatePromotionalPrice(final List<ProductModel> products, final List<CategoryModel> categories,
			final Double value, final Date startDate, final Date endtDate, final boolean percent, final Integer priority,
			final List<String> sellers, final List<CategoryModel> brands, final ProductPromotionModel promoCurrent,
			final List<String> rejectSellerList, final List<CategoryModel> rejectBrandList, final Double maxDiscount,
			final List<ProductModel> exproductListdata, final List<String> channelList)//final String promoCode,
	{
		try
		{
			//boolean isValidatePriority = true;//Introducted for pricerow updation issue after bulk upload
			//final List<String> productPkList = new ArrayList<String>();
			//			clearExistingData(promoCurrent.getCode());
			//final List<String> product = new ArrayList<String>();
			//	final List<String> stagedProductList = new ArrayList<String>();
			//	final List<String> promoproductList = new ArrayList<String>();

			clearExistingData(promoCurrent.getCode());
			final List<String> productUssidList = new ArrayList<String>();

			//List<String> exProductList = new ArrayList<String>();
			//final List<PriceRowModel> priceList = new ArrayList<PriceRowModel>();
			final List<PromotionalPriceRowModel> promoPriceList = new ArrayList<PromotionalPriceRowModel>(); //added for channel specific promotion

			//			if (CollectionUtils.isNotEmpty(exproductListdata))
			//			{
			//				exProductList = getExcludedProductData(exproductListdata);
			//			}

			//Introducted for pricerow updation issue after bulk upload
			//			if (CollectionUtils.isNotEmpty(sellers) || CollectionUtils.isNotEmpty(brands)
			//					|| CollectionUtils.isNotEmpty(rejectSellerList) || CollectionUtils.isNotEmpty(rejectBrandList))
			//			{
			//				isValidatePriority = false;
			//			}

			if (CollectionUtils.isNotEmpty(products))
			{
				for (final ProductModel itrProduct : products)
				{
					productUssidList.addAll(validatePromotion(itrProduct, sellers, rejectSellerList, promoCurrent));

					//if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority))
					//Modified for pricerow updation issue after bulk upload
					//					if (getBrandsForProduct(itrProduct, brands, rejectBrandList)
					//							&& (isValidatePriority ? validateProductData(itrProduct, priority) : true))/
					//					{
					//product.add(itrProduct.getAttribute("pk").toString());
					//product.add(itrProduct.getCode());
					//productPkList.add(itrProduct.getPk().toString());
					//	promoproductList.add(itrProduct.getCode()); //For staged Product Details Car-153
					//}
				}
			}
			//Car-158 CHANGE
			if (CollectionUtils.isNotEmpty(categories))
			{
				//TISPRO-352 : Fix
				//final List<ProductModel> productList = fetchProductList(categories);//Car-158
				final List<ProductModel> productList = getProductsForCategory(categories, exproductListdata, brands, rejectBrandList);//Car-158

				for (final ProductModel prdct : productList)
				{
					productUssidList.addAll(validatePromotion(prdct, sellers, rejectSellerList, promoCurrent));


					//					if (getBrandsForProduct(prdct, brands, rejectBrandList) && validateCategoryProductData(prdct, priority)
					//							&& validateExclusion(exProductList, prdct))
					//Modified for pricerow updation issue after bulk upload
					//					if (getBrandsForProduct(prdct, brands, rejectBrandList)
					//							&& (isValidatePriority ? validateCategoryProductData(prdct, priority) : true)
					//							&& validateExclusion(exProductList, prdct))/
					//					{
					//						product.add(prdct.getCode());
					//						productPkList.add(prdct.getPk().toString());
					//					}
				}
				//Car-158 CHANGE
				//				final ConcurrentHashMap<List<String>, List<String>> categoryDetailsMap = getProductListWitoutExProduct(brands,
				//						rejectBrandList, priority, categories, exProductList);
				//				if (MapUtils.isNotEmpty(categoryDetailsMap))
				//				{
				//					for (final ConcurrentHashMap.Entry<List<String>, List<String>> entry : categoryDetailsMap.entrySet())
				//					{
				//						product.addAll(entry.getKey());
				//						//		promoproductList.addAll(entry.getValue());//Car-158
				//						LOG.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				//					}
				//				}
			}

			//LOG.debug("******** Special Price - Promotion Applicable product List:" + product);
			LOG.debug("******** Special Price - Promotion Applicable SKU List:" + productUssidList);
			//filter product list based on brand restriction

			if (!productUssidList.isEmpty())
			{
				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(productUssidList);
				final List<PriceRowModel> priceRowtobeSaved = new ArrayList<>(); //added for channel specific promotion
				for (final PriceRowModel price : priceRow)//loops req
				{
					if (CollectionUtils.isEmpty(channelList))
					{
						PromotionalPriceRowModel pm = null;
						final List<PromotionalPriceRowModel> pmList = new ArrayList<>();
						if (!percent)
						{
							pm = modelService.create(PromotionalPriceRowModel.class);
							pm.setPromotionStartDate(startDate);
							pm.setPromotionEndDate(endtDate);
							pm.setIsPercentage(Boolean.valueOf(percent));
							pm.setPromotionValue(value);
							pm.setPromotionIdentifier(promoCurrent.getCode());
							pm.setMaxDiscount(maxDiscount);
							pmList.addAll(getPromoPriceRowList(price.getPromotionalPriceRow()));
							pmList.add(pm);
							price.setPromotionalPriceRow(pmList);
							priceRowtobeSaved.add(price);
						}
						else
						{
							pm = modelService.create(PromotionalPriceRowModel.class);
							pm.setPromotionStartDate(startDate);
							pm.setPromotionEndDate(endtDate);
							pm.setIsPercentage(Boolean.valueOf(percent));
							pm.setPromotionValue(value);
							pm.setPromotionIdentifier(promoCurrent.getCode());
							pm.setMaxDiscount(maxDiscount);
							pmList.addAll(getPromoPriceRowList(price.getPromotionalPriceRow()));
							pmList.add(pm);
							price.setPromotionalPriceRow(pmList);
							priceRowtobeSaved.add(price);
						}
						promoPriceList.add(pm);
					}
					else
					{
						for (final String channel : channelList)
						{
							PromotionalPriceRowModel pm = null;
							final List<PromotionalPriceRowModel> pmList = new ArrayList<>();
							if (!percent)
							{
								pm = modelService.create(PromotionalPriceRowModel.class);
								pm.setPromotionStartDate(startDate);
								pm.setPromotionEndDate(endtDate);
								pm.setIsPercentage(Boolean.valueOf(percent));
								pm.setPromotionValue(value);
								pm.setPromotionIdentifier(promoCurrent.getCode());
								pm.setMaxDiscount(maxDiscount);
								if (channel.equalsIgnoreCase("MOBILE"))
								{
									pm.setPromotionChannel("Mobile");
								}
								if (channel.equalsIgnoreCase("WEB"))
								{
									pm.setPromotionChannel("Web");
								}
								if (channel.equalsIgnoreCase("WEBMOBILE"))
								{
									pm.setPromotionChannel("WebMobile");
								}
								pmList.addAll(getPromoPriceRowList(price.getPromotionalPriceRow(), pm.getPromotionChannel()));
								pmList.add(pm);
								price.setPromotionalPriceRow(pmList);
								priceRowtobeSaved.add(price);
							}
							else
							{
								pm = modelService.create(PromotionalPriceRowModel.class);
								pm.setPromotionStartDate(startDate);
								pm.setPromotionEndDate(endtDate);
								pm.setIsPercentage(Boolean.valueOf(percent));
								pm.setPromotionValue(value);
								pm.setPromotionIdentifier(promoCurrent.getCode());
								pm.setMaxDiscount(maxDiscount);
								if (channel.equalsIgnoreCase("MOBILE"))
								{
									pm.setPromotionChannel("Mobile");
								}
								if (channel.equalsIgnoreCase("WEB"))
								{
									pm.setPromotionChannel("Web");
								}
								if (channel.equalsIgnoreCase("WEBMOBILE"))
								{
									pm.setPromotionChannel("WebMobile");
								}
								pmList.addAll(getPromoPriceRowList(price.getPromotionalPriceRow(), pm.getPromotionChannel()));
								pmList.add(pm);
								price.setPromotionalPriceRow(pmList);
								priceRowtobeSaved.add(price);
							}
							promoPriceList.add(pm);
						}
					}

				}
				if (CollectionUtils.isNotEmpty(promoPriceList))
				{
					modelService.saveAll(priceRowtobeSaved);
				}
			}
		}

		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}

	private List<PromotionalPriceRowModel> getPromoPriceRowList(final Collection<PromotionalPriceRowModel> promotionalPriceRow)
	{
		final List<PromotionalPriceRowModel> dataList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(promotionalPriceRow))
		{
			for (final PromotionalPriceRowModel oModel : promotionalPriceRow)
			{
				if (StringUtils.isNotEmpty(oModel.getPromotionChannel()))
				{
					dataList.add(oModel);
				}
			}
		}
		return dataList;
	}




	private List<PromotionalPriceRowModel> getPromoPriceRowList(final Collection<PromotionalPriceRowModel> promotionalPriceRow,
			final String promotionChannel)
	{
		final List<PromotionalPriceRowModel> dataList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(promotionalPriceRow))
		{
			//List<PromotionalPriceRowModel> realDataList=new ArrayList<>(promotionalPriceRow);
			for (final PromotionalPriceRowModel oModel : promotionalPriceRow)
			{
				if (!StringUtils.equalsIgnoreCase(oModel.getPromotionChannel(), promotionChannel))
				{
					dataList.add(oModel);
				}
			}
		}

		return dataList;
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
			/*
			 * Commented for Channel specific Promotion
			 */
			/*
			 * final List<PriceRowModel> priceRowList = updatePromotionalPriceDao.fetchPromoPriceData(promoCode); //final
			 * List<PriceRowModel> priceList = new ArrayList<PriceRowModel>(); final List<PriceRowModel> priceRowtobeSaved
			 * = new ArrayList<>(); //added for channel specific promotion final List<PromotionalPriceRowModel>
			 * promoPriceList = new ArrayList<PromotionalPriceRowModel>(); if (CollectionUtils.isNotEmpty(priceRowList)) {
			 * for (final PriceRowModel price : priceRowList) { final PromotionalPriceRowModel pm = new
			 * PromotionalPriceRowModel(); final List<PromotionalPriceRowModel> pmList = new ArrayList<>();
			 * pm.setPromotionStartDate(null); pm.setPromotionEndDate(null); pm.setIsPercentage(null);
			 * pm.setPromotionValue(null); pm.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
			 * pm.setMaxDiscount(null); pm.setPromotionChannel(null); pmList.addAll(price.getPromotionalPriceRow());
			 * pmList.add(pm); price.setPromotionalPriceRow(pmList); priceRowtobeSaved.add(price); }
			 *
			 * if (CollectionUtils.isNotEmpty(promoPriceList)) { modelService.saveAll(priceRowtobeSaved); }
			 */
			final List<PromotionalPriceRowModel> priceRowModelList = updatePromotionalPriceDao.fetchPromoPriceData(promoCode);

			final Set<PriceRowModel> priceModifiedtsChange = new HashSet<>();


			if (CollectionUtils.isNotEmpty(priceRowModelList))
			{
				for (final PromotionalPriceRowModel priceRow : priceRowModelList)
				{
					final PriceRowModel finalModel = priceRow.getPriceRow(); //Added for PRDI-549
					if (null != finalModel)
					{
						finalModel.setPromotionEndDate(new Date());
						priceModifiedtsChange.add(finalModel);
					}
				}

				modelService.removeAll(priceRowModelList);
				//Added for PRDI-549
				if (CollectionUtils.isNotEmpty(priceModifiedtsChange))
				{
					modelService.saveAll(priceModifiedtsChange);
				}
			}
		}

	}






	//Car-158
	//	private List<String> getStagedProductDetails(final List<String> productList)
	//	{
	//		final List<String> stagedProductList = new ArrayList<String>();
	//
	//		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(configurationService
	//				.getConfiguration().getString("cronjob.promotion.catelog"), MarketplacecommerceservicesConstants.STAGED);
	//
	//		if (CollectionUtils.isNotEmpty(productList))
	//		{
	//			for (final String productCode : productList)
	//			{
	//				final ProductModel product = productService.getProductForCode(catalogVersionModel, productCode);
	//				stagedProductList.add(product.getPk().toString());
	//			}
	//		}
	//
	//		return stagedProductList;
	//	}

	private boolean validateProductData(final ProductModel product, final Integer priority)
	{
		boolean flag = false;
		try
		{
			//			final ProductModel oModel = productService.getProductForCode(getDefaultPromotionsManager().catalogData(),
			//					product.getCode());//change
			//			if (null != oModel)
			//			{//Car-158 chnage
			flag = validateCategoryProductData(product, priority);
			//}
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

			if (brands.isEmpty() && rejectBrandList.isEmpty()) //no need to proceed if there is no brand restriction
			{
				return true;
			}

			final List<CategoryModel> categories = getImmediateSuperCategory(product);
			String brandCode = "";//Car-158
			if (categories != null && !categories.isEmpty())
			{
				brandList = new ArrayList<String>();
				for (final CategoryModel category : categories)
				{
					if (category.getCode().startsWith("MBH"))
					{
						brandList.add(category.getCode());
						brandCode = brandList.get(0);
						break; //Car-158
					}

				}
			}

			if (CollectionUtils.isNotEmpty(brands) && CollectionUtils.isNotEmpty(brandList))
			{
				//final String productBrand = brandList.get(0);//Car-158 CHNAGES
				if (brands.contains(brandCode))
				{
					allow = true;
				}
			}


			if (CollectionUtils.isNotEmpty(rejectBrandList) && CollectionUtils.isNotEmpty(brandList))
			{
				//	final String productBrand = brandList.get(0);
				if (rejectBrandList.contains(brandCode)) //Car-158 CHNAGES
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



	//	private boolean isPriceToUpdate(final PriceRowModel price, final List<String> sellers, final List<String> rejectSellerList)
	//	{
	//		boolean updateSpecialPrice = false;
	//		final List<SellerInformationModel> sellerModels = new ArrayList<SellerInformationModel>(price.getProduct()
	//				.getSellerInformationRelator());
	//
	//		if (CollectionUtils.isNotEmpty(sellers))
	//		{
	//			for (final SellerInformationModel seller : sellerModels)
	//			{
	//				if (sellers.contains(seller.getSellerID())
	//						&& price.getSellerArticleSKU().equalsIgnoreCase(seller.getSellerArticleSKU()))
	//				{
	//					LOG.debug("******** Special Price - Promotion Applicable ussid List:" + seller.getSellerArticleSKU());
	//					updateSpecialPrice = true;
	//				}
	//			}
	//		}
	//		else if (CollectionUtils.isNotEmpty(rejectSellerList))
	//		{
	//			for (final SellerInformationModel seller : sellerModels)
	//			{
	//				if (rejectSellerList.contains(seller.getSellerID())
	//						&& price.getSellerArticleSKU().equalsIgnoreCase(seller.getSellerArticleSKU()))
	//				{
	//					LOG.debug("******** Special Price - Promotion Applicable ussid List:" + seller.getSellerArticleSKU());
	//					updateSpecialPrice = false;
	//					break;
	//				}
	//				else
	//				{
	//					updateSpecialPrice = true;
	//				}
	//			}
	//		}
	//
	//
	//		return updateSpecialPrice;
	//	}


	//SONAR FIX
	/*
	 * private void disablePromotionalPrice(final List<ProductModel> products, final List<CategoryModel> categories,
	 * final boolean isEnabled, final Integer priority, final List<String> brands, final Long quantity, final
	 * List<String> rejectSellerList, final List<String> rejectBrandList, final String promoCode) { try {
	 * clearExistingData(promoCode); final List<String> product = new ArrayList<String>(); //List<String>
	 * stagedProductList = new ArrayList<String>();//why? // final List<String> promoproductList = new
	 * ArrayList<String>();//Car-153 final List<PriceRowModel> priceList = new ArrayList<PriceRowModel>();
	 *
	 * if (CollectionUtils.isNotEmpty(products)) { for (final ProductModel itrProduct : products) { if
	 * (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority)) {
	 * product.add(itrProduct.getPk().toString()); //promoproductList.add(itrProduct.getCode());//Car-158 } } }
	 *
	 * if (CollectionUtils.isNotEmpty(categories)) { //TISPRO-352 : Fix final List<ProductModel> productList =
	 * fetchProductList(categories); if (CollectionUtils.isNotEmpty(productList)) { for (final ProductModel itrProduct :
	 * productList) { if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct,
	 * priority)) { product.add(itrProduct.getPk().toString()); //promoproductList.add(itrProduct.getCode());//CAR-158 }
	 * } }
	 *
	 *
	 * //Car-158 // final ConcurrentHashMap<List<String>, List<String>> categoryDetailsMap =
	 * getEligibleProductList(brands, // rejectBrandList, priority, categories); // if
	 * (MapUtils.isNotEmpty(categoryDetailsMap)) // { // for (final ConcurrentHashMap.Entry<List<String>, List<String>>
	 * entry : categoryDetailsMap.entrySet()) // { // product.addAll(entry.getKey()); // //
	 * promoproductList.addAll(entry.getValue());//Car-158 // LOG.debug("Key = " + entry.getKey() + ", Value = " +
	 * entry.getValue()); // } // } }
	 *
	 *
	 * LOG.debug("******** Special Price - Disable Promotion Applicable product List:" + product);
	 *
	 *
	 * if (!product.isEmpty()) { //Car-158 // stagedProductList = getStagedProductDetails(promoproductList); // For
	 * adding the staged catalog price Row for Product // if (CollectionUtils.isNotEmpty(stagedProductList)) // { //
	 * product.addAll(stagedProductList); // }
	 *
	 * final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product); for (final PriceRowModel
	 * price : priceRow) { if (!isEnabled) { price.setPromotionStartDate(null); price.setPromotionEndDate(null);
	 * price.setIsPercentage(null); price.setPromotionValue(null);
	 * price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY); price.setMaxDiscount(null); } else if
	 * (null != quantity && quantity.longValue() > 1) //For TISPRD-383 : If Validated remove Special Price Details {
	 * price.setPromotionStartDate(null); price.setPromotionEndDate(null); price.setIsPercentage(null);
	 * price.setPromotionValue(null); price.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
	 * price.setMaxDiscount(null); } priceList.add(price); }
	 *
	 * if (CollectionUtils.isNotEmpty(priceList)) { modelService.saveAll(priceList); //NEED CHANGE }
	 *
	 * } }
	 *
	 * catch (final EtailBusinessExceptions e) { throw e; } catch (final EtailNonBusinessExceptions e) { throw e; } catch
	 * (final Exception e) { throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000); } }
	 */


	//	private void disablePromotionalPrice(final List<ProductModel> products, final List<CategoryModel> categories,
	//			final boolean isEnabled, final Integer priority, final List<String> brands, final Long quantity,
	//			final List<String> rejectSellerList, final List<String> rejectBrandList, final String promoCode,
	//			final List<String> channel)
	//	{
	//		try
	//		{
	//			clearExistingData(promoCode);
	//			final List<String> product = new ArrayList<String>();
	//			//List<String> stagedProductList = new ArrayList<String>();//why?
	//			//	final List<String> promoproductList = new ArrayList<String>();//Car-153
	//			//final List<PriceRowModel> priceList = new ArrayList<PriceRowModel>();
	//			final List<PromotionalPriceRowModel> promoPriceList = new ArrayList<PromotionalPriceRowModel>();
	//			if (CollectionUtils.isNotEmpty(products))
	//			{
	//				for (final ProductModel itrProduct : products)
	//				{
	//					if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority))
	//					{
	//						product.add(itrProduct.getPk().toString());
	//						//promoproductList.add(itrProduct.getCode());//Car-158
	//					}
	//				}
	//			}
	//
	//			if (CollectionUtils.isNotEmpty(categories))
	//			{
	//				//TISPRO-352 : Fix
	//				final List<ProductModel> productList = fetchProductList(categories);
	//				if (CollectionUtils.isNotEmpty(productList))
	//				{
	//					for (final ProductModel itrProduct : productList)
	//					{
	//						if (getBrandsForProduct(itrProduct, brands, rejectBrandList) && validateProductData(itrProduct, priority))
	//						{
	//							product.add(itrProduct.getPk().toString());
	//							//promoproductList.add(itrProduct.getCode());//CAR-158
	//						}
	//					}
	//				}
	//
	//
	//				//Car-158
	//				//				final ConcurrentHashMap<List<String>, List<String>> categoryDetailsMap = getEligibleProductList(brands,
	//				//						rejectBrandList, priority, categories);
	//				//				if (MapUtils.isNotEmpty(categoryDetailsMap))
	//				//				{
	//				//					for (final ConcurrentHashMap.Entry<List<String>, List<String>> entry : categoryDetailsMap.entrySet())
	//				//					{
	//				//						product.addAll(entry.getKey());
	//				//						//	promoproductList.addAll(entry.getValue());//Car-158
	//				//						LOG.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	//				//					}
	//				//				}
	//			}
	//
	//
	//			LOG.debug("******** Special Price - Disable Promotion Applicable product List:" + product);
	//
	//
	//			if (!product.isEmpty())
	//			{
	//				//Car-158
	//				//	stagedProductList = getStagedProductDetails(promoproductList); // For adding the staged catalog price Row for Product
	//				//				if (CollectionUtils.isNotEmpty(stagedProductList))
	//				//				{
	//				//					product.addAll(stagedProductList);
	//				//				}
	//
	//				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product);
	//				final List<PriceRowModel> priceRowtobeSaved = new ArrayList<>(); //added for channel specific promotion
	//				for (final PriceRowModel price : priceRow)
	//				{
	//					final PromotionalPriceRowModel pm = new PromotionalPriceRowModel();
	//					final List<PromotionalPriceRowModel> pmList = new ArrayList<>();
	//					if (!isEnabled)
	//					{
	//						pm.setPromotionStartDate(null);
	//						pm.setPromotionEndDate(null);
	//						pm.setIsPercentage(null);
	//						pm.setPromotionValue(null);
	//						pm.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
	//						pm.setMaxDiscount(null);
	//						pm.setPromotionChannel(null);
	//					}
	//					else if (null != quantity && quantity.longValue() > 1) //For TISPRD-383 : If Validated remove Special Price Details
	//					{
	//						pm.setPromotionStartDate(null);
	//						pm.setPromotionEndDate(null);
	//						pm.setIsPercentage(null);
	//						pm.setPromotionValue(null);
	//						pm.setPromotionIdentifier(MarketplacecommerceservicesConstants.EMPTY);
	//						pm.setMaxDiscount(null);
	//						pm.setPromotionChannel(null);
	//					}
	//					pmList.addAll(price.getPromotionalPriceRow());
	//					pmList.add(pm);
	//					price.setPromotionalPriceRow(pmList);
	//					priceRowtobeSaved.add(price);
	//
	//				}
	//
	//				if (CollectionUtils.isNotEmpty(promoPriceList))
	//				{
	//					modelService.save(priceRowtobeSaved); //NEED CHANGE
	//				}
	//
	//			}
	//		}
	//
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			throw e;
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			throw e;
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
	//		}
	//	}


	//	private ConcurrentHashMap<List<String>, List<String>> getEligibleProductList(final List<String> brands,
	//			final List<String> rejectBrandList, final Integer priority, final List<CategoryModel> categories)
	//	{
	//		final List<CategoryModel> categoryList = getAllCategories(categories);
	//		List<String> productPKList = null;
	//		List<String> productCodeList = null;
	//		ConcurrentHashMap<List<String>, List<String>> dataMap = null;
	//
	//		if (CollectionUtils.isNotEmpty(categoryList))
	//		{
	//			productPKList = new ArrayList<String>();
	//			productCodeList = new ArrayList<String>();
	//			dataMap = new ConcurrentHashMap<List<String>, List<String>>();
	//			LOG.debug("Populating eligible products in List" + "Category List:" + categoryList);
	//			for (final CategoryModel oModel : categoryList)
	//			{
	//				if (CollectionUtils.isNotEmpty(oModel.getProducts()))
	//				{
	//					for (final ProductModel product : oModel.getProducts())
	//					{
	//						if (getBrandsForProduct(product, rejectBrandList, brands) && validateCategoryProductData(product, priority))
	//						{
	//							productPKList.add(product.getPk().toString());
	//							productCodeList.add(product.getCode());
	//						}
	//					}
	//				}
	//			}
	//
	//
	//			if (CollectionUtils.isNotEmpty(productPKList) && CollectionUtils.isNotEmpty(productCodeList))
	//			{
	//				dataMap.put(productPKList, productCodeList);
	//			}
	//		}
	//		return dataMap;
	//	}
	/**
	 * Car-158
	 *
	 * @param categories
	 * @return productList
	 */
	//	private List<ProductModel> fetchProductList(final List<CategoryModel> categories)
	//	{
	//		final List<CategoryModel> categoryList = getAllCategories(categories);
	//		final List<ProductModel> productList = new ArrayList<ProductModel>();
	//		if (CollectionUtils.isNotEmpty(categoryList))
	//		{
	//			LOG.debug("Populating eligible products in List" + "Category List:" + categoryList);
	//			for (final CategoryModel catModel : categoryList)
	//			{
	//				if (CollectionUtils.isNotEmpty(catModel.getProducts()))
	//				{
	//					productList.addAll(catModel.getProducts());
	//				}
	//			}
	//
	//		}
	//		return productList;
	//	}

	private List<CategoryModel> getAllCategories(final List<CategoryModel> categories)
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		try
		{
			for (final CategoryModel category : categories)
			{
				//				final CategoryModel oModel = categoryService.getCategoryForCode(getDefaultPromotionsManager().catalogData(),
				//						category.getCode());Car-158
				if (null != category)
				{
					categoryList.add(category);
					final Collection<CategoryModel> subCategoryList = categoryService.getAllSubcategoriesForCategory(category);
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


			final Collection<CategoryModel> categoriesList = getcategoryData(product);//check
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
						if (StringUtils.isEmpty(promotion.getImmutableKey()) && promotion instanceof BuyAPercentageDiscountModel
								&& null != ((BuyAPercentageDiscountModel) promotion).getQuantity()
								&& ((BuyAPercentageDiscountModel) promotion).getQuantity().intValue() == 1
								&& promotion.getEndDate().after(new Date()))
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
	 * Car-158
	 *
	 * @Description: Category Data corresponding to a Product for Promotion Intercepter
	 * @param productdata
	 * @return productCategoryData
	 */

	public List<CategoryModel> getcategoryData(final ProductModel productdata)
	{
		List<CategoryModel> productCategoryData = null;
		//final List<CategoryModel> superCategoryData = null;
		//final CatalogVersionModel oCatalogVersionModel = catalogData();

		HashSet<CategoryModel> superCategoryData = null;
		List<CategoryModel> superCategoryList = null;

		if (null != productdata)
		{
			productCategoryData = new ArrayList<>(productdata.getSupercategories());

			if (CollectionUtils.isNotEmpty(productCategoryData))
			{
				superCategoryList = new ArrayList<CategoryModel>();
				superCategoryData = (HashSet<CategoryModel>) getAllSupercategories(productCategoryData);
				if (CollectionUtils.isNotEmpty(superCategoryData))
				{
					final List<CategoryModel> dataList = new ArrayList<CategoryModel>(superCategoryData);
					superCategoryList.addAll(dataList);
				}
				superCategoryList.addAll(productCategoryData);
			}
		}
		return superCategoryList;
	}

	/**
	 * car-158 Get All Category Tree Structure
	 *
	 * @param categories
	 * @return result
	 */
	private Collection<CategoryModel> getAllSupercategories(final Collection<CategoryModel> categories)
	{
		Collection<CategoryModel> result = null;
		Collection<CategoryModel> currentLevel = new ArrayList<CategoryModel>();
		for (final CategoryModel categoryModel : categories)
		{
			final List<CategoryModel> superCategories = categoryModel.getSupercategories();
			if (superCategories != null)
			{
				currentLevel.addAll(superCategories);
			}
		}

		while (!CollectionUtils.isEmpty(currentLevel))
		{
			for (final Iterator iterator = currentLevel.iterator(); iterator.hasNext();)
			{
				final CategoryModel categoryModel = (CategoryModel) iterator.next();
				if (result == null)
				{
					result = new HashSet<CategoryModel>();
				}
				if (!result.add(categoryModel))
				{
					// avoid cycles by removing all which are already found
					iterator.remove();
				}
			}

			if (currentLevel.isEmpty())
			{
				break;
			}
			final Collection<CategoryModel> nextLevel = getAllSupercategories(currentLevel);
			currentLevel = nextLevel;
		}

		return result == null ? Collections.EMPTY_LIST : result;
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


	//	public ConcurrentHashMap<List<String>, List<String>> getProductListWitoutExProduct(final List<String> brands,
	//			final List<String> rejectBrandList, final Integer priority, final List<CategoryModel> categories,
	//			final List<String> exProductList)
	//	{
	//
	//		final List<CategoryModel> categoryList = getAllCategories(categories);
	//		List<String> productPKList = null;
	//		List<String> productCodeList = null;
	//		ConcurrentHashMap<List<String>, List<String>> dataMap = null;
	//
	//		if (CollectionUtils.isNotEmpty(categoryList))
	//		{
	//			productPKList = new ArrayList<String>();
	//			productCodeList = new ArrayList<String>();
	//			dataMap = new ConcurrentHashMap<List<String>, List<String>>();
	//			LOG.debug("Populating eligible products in List" + "Category List:" + categoryList);
	//			for (final CategoryModel oModel : categoryList)//LOOP REQUIRED
	//			{
	//				if (CollectionUtils.isNotEmpty(oModel.getProducts()))
	//				{
	//					for (final ProductModel product : oModel.getProducts())
	//					{
	//						if (getBrandsForProduct(product, rejectBrandList, brands) && validateCategoryProductData(product, priority)
	//								&& validateExclusion(exProductList, product))
	//						{
	//							productPKList.add(product.getPk().toString());
	//							productCodeList.add(product.getCode());
	//						}
	//					}
	//				}
	//			}
	//
	//
	//			if (CollectionUtils.isNotEmpty(productPKList) && CollectionUtils.isNotEmpty(productCodeList))
	//			{
	//				dataMap.put(productPKList, productCodeList);
	//			}
	//		}
	//		return dataMap;
	//
	//	}


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

	private List<String> getChannel(final List<SalesApplication> channelList)
	{
		final List<String> finalChannel = new ArrayList<>();
		for (final SalesApplication channel : channelList)
		{
			finalChannel.add(channel.getCode().toUpperCase());
		}
		return finalChannel;
	}

	/**
	 * @Description : This method removes redundant Promotional Price Rows from DB
	 *
	 */
	@Override
	public void purgeRedundantData()
	{
		final List<PromotionalPriceRowModel> promoPriceRowList = updatePromotionalPriceDao.getRedundantData();
		try
		{
			if (CollectionUtils.isNotEmpty(promoPriceRowList))
			{
				modelService.removeAll(promoPriceRowList);
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Error in Job ||| Method : purgeRedundantData ");
			LOG.error("Error during purging of data " + exception.getMessage());
		}

	}

	private List<String> validatePromotion(final ProductModel product, final List<String> promoSellersList,
			final List<String> promoRejectSellerList, final ProductPromotionModel promoCurrent)//, final Integer priority
	{
		//final List<String> productPromoValidSelletList = new ArrayList<String>();
		//final List<String> productPromoValidUssidList = new ArrayList<String>();
		//final Map<ProductModel, List<String>> productPromoUssidMap = new HashMap<ProductModel, List<String>>();
		final Map<String, String> sellerIdUssidMap = new HashMap<String, String>();

		//Get valid promotional seller list for product
		final Map params = new HashMap();
		params.put("product", product.getPk());

		final StringBuilder queryString = new StringBuilder("SELECT {" + SellerInformationModel.PK + "} FROM {"
				+ SellerInformationModel._TYPECODE + " AS sellerInfo} " + " WHERE {sellerInfo."
				+ SellerInformationModel.PRODUCTSOURCE + "} = ?product ");

		if (CollectionUtils.isNotEmpty(promoSellersList))
		{
			queryString.append(" AND {" + SellerInformationModel.SELLERID + " } IN (?promotionSeller)");
			params.put("promotionSeller", promoSellersList);
		}
		else if (CollectionUtils.isNotEmpty(promoRejectSellerList))
		{
			queryString.append(" AND {" + SellerInformationModel.SELLERID + " } NOT IN (?promotionRejectedSeller)");
			params.put("promotionRejectedSeller", promoRejectSellerList);
		}

		LOG.debug("QUERY>>>>>>" + queryString);
		//final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		//query.addQueryParameter("product", product.getPk());
		//query.addQueryParameter("promotionSeller", promoSellersList);
		//query.addQueryParameter("promotionRejectedSeller", promoRejectSellerList);

		final List<SellerInformationModel> productValidSellerList = flexibleSearchService.<SellerInformationModel> search(
				queryString.toString(), params).getResult();
		for (final SellerInformationModel seller : productValidSellerList)
		{
			sellerIdUssidMap.put(seller.getSellerID(), seller.getSellerArticleSKU());
			//productPromoValidUssidList.add(ussid);
		}

		//Get all promotions for the specific product
		final List<ProductPromotionModel> sortedPromoListForProduct = getSortedPromoListForProduct(product, promoCurrent);

		if (promoCurrent.getPk().equals(sortedPromoListForProduct.get(0).getPk()))
		{
			//productPromoUssidMap.put(product, productPromoValidUssidList);
			//return productPromoValidUssidList;
			return new ArrayList<String>(sellerIdUssidMap.values());
		}
		else
		{
			sortedPromoListForProduct.remove(promoCurrent);

			final StringBuilder queryStringSeller = new StringBuilder("SELECT DISTINCT pprom.sellers, pprom.restrType  FROM (");
			queryStringSeller.append(" {{ SELECT {sellerRestr." + EtailSellerSpecificRestrictionModel.SELLERMASTERLIST
					+ " AS sellers } , {composedType." + ComposedTypeModel.CODE + " AS restrType } ");
			queryStringSeller.append(MarketplacecommerceservicesConstants.QUERYFROM + "{ "
					+ EtailSellerSpecificRestrictionModel._TYPECODE + " AS sellerRestr ");
			queryStringSeller.append("JOIN " + ComposedTypeModel._TYPECODE + " AS composedType ");
			queryStringSeller.append("ON sellerRestr." + EtailSellerSpecificRestrictionModel.ITEMTYPE + " = composedType."
					+ ComposedTypeModel.PK + "} ");
			queryStringSeller.append(" WHERE {sellerRestr." + EtailSellerSpecificRestrictionModel.PROMOTION
					+ "} IN (?highestPriorityPromo) }} ");

			queryStringSeller.append(MarketplacecommerceservicesConstants.QUERYUNION);

			queryStringSeller.append(" {{ SELECT {exSellerRestr." + EtailExcludeSellerSpecificRestrictionModel.SELLERMASTERLIST
					+ "AS exSellers } , {composedType." + ComposedTypeModel.CODE + " AS restrType } ");
			queryStringSeller.append(MarketplacecommerceservicesConstants.QUERYFROM + "{ "
					+ EtailExcludeSellerSpecificRestrictionModel._TYPECODE + " AS exSellerRestr ");
			queryStringSeller.append("JOIN " + ComposedTypeModel._TYPECODE + " AS composedType ");
			queryStringSeller.append("ON exSellerRestr." + EtailExcludeSellerSpecificRestrictionModel.ITEMTYPE + " = composedType."
					+ ComposedTypeModel.PK + "} ");
			queryStringSeller.append(" WHERE {exSellerRestr." + EtailExcludeSellerSpecificRestrictionModel.PROMOTION
					+ "} IN (?highestPriorityPromo) }} ");

			if (!(Config.isOracleUsed()))
			{
				queryStringSeller.append(" ) AS pprom");
			}
			else
			{
				queryStringSeller.append(" ) pprom");
			}

			LOG.debug("QUERY IS : " + queryStringSeller);

			final FlexibleSearchQuery querySeller = new FlexibleSearchQuery(queryStringSeller);
			querySeller.addQueryParameter("highestPriorityPromo", sortedPromoListForProduct);
			querySeller.setResultClassList(Arrays.asList(String.class, String.class));

			final SearchResult<List<Object>> result = flexibleSearchService.search(querySeller);
			for (final List<Object> row : result.getResult())
			{
				final String sellers = (String) row.get(0);
				final String restrType = (String) row.get(1);
				final List<String> sellerList = Arrays.asList(sellers.substring(4).split(","));
				for (final String seller : sellerList)
				{
					if (sellerIdUssidMap.containsKey(seller)
							&& restrType.equalsIgnoreCase(EtailSellerSpecificRestrictionModel._TYPECODE))
					{
						sellerIdUssidMap.remove(seller);
					}
				}
			}

			return new ArrayList<String>(sellerIdUssidMap.values());
		}
	}

	private List<ProductPromotionModel> getSortedPromoListForProduct(final ProductModel product,
			final ProductPromotionModel promoCurrent)
	{
		/////////////////-------------------------//////////////////////////////////////////
		//		final List<ProductPromotionModel> productPromoData = new ArrayList<ProductPromotionModel>(product.getPromotions());
		//		final Collection<CategoryModel> categoriesList = getcategoryData(product);
		//		final List<ProductPromotionModel> validPromosForProduct = new ArrayList<ProductPromotionModel>();
		//
		//		//		final List<BuyAPercentageDiscountModel> validBuyAPerOffPromosForProduct = new ArrayList<BuyAPercentageDiscountModel>();
		//		//		final List<BuyABFreePrecentageDiscountModel> validBuyAgetBFreePerOffPromosForProduct = new ArrayList<BuyABFreePrecentageDiscountModel>();
		//
		//		if (CollectionUtils.isNotEmpty(categoriesList))
		//		{
		//			for (final CategoryModel category : categoriesList)
		//			{
		//				if (CollectionUtils.isNotEmpty(productPromoData))
		//				{
		//					productPromoData.addAll(category.getPromotions());
		//				}
		//			}
		//		}
		//
		//		if (promoCurrent instanceof BuyAPercentageDiscountModel)
		//		{
		//			for (final ProductPromotionModel promo : productPromoData)
		//			{
		//				final Date sysdate = new Date();
		//				if (promo instanceof BuyAPercentageDiscountModel)
		//				{
		//					final BuyAPercentageDiscountModel buyAgetPerOff = (BuyAPercentageDiscountModel) promo;
		//					if (StringUtils.isEmpty(buyAgetPerOff.getImmutableKey()) && buyAgetPerOff.getEnabled().booleanValue()
		//							&& buyAgetPerOff.getStartDate().compareTo(sysdate) <= 0
		//							&& buyAgetPerOff.getEndDate().compareTo(sysdate) >= 0
		//							&& buyAgetPerOff.getPriority().intValue() >= promoCurrent.getPriority().intValue()
		//							//					&& (promo.getStartDate().equals(sysdate) || promo.getStartDate().before(sysdate))
		//							//					&& (promo.getEndDate().equals(sysdate) || promo.getEndDate().after(sysdate))
		//							&& buyAgetPerOff.getQuantity().intValue() == 1)
		//					{
		//						//validBuyAPerOffPromosForProduct.add(buyAgetPerOff);
		//						validPromosForProduct.add(buyAgetPerOff);
		//					}
		//				}
		//			}
		//
		//			//Sorting promotion collection on priority DESC
		//			//			Collections.sort(validPromosForProduct, new Comparator<ProductPromotionModel>()
		//			//			{
		//			//				public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
		//			//				{
		//			//					if (o1.getPriority().intValue() > o2.getPriority().intValue())
		//			//					{
		//			//						return 1;
		//			//					}
		//			//					else
		//			//					{
		//			//						return -1;
		//			//					}
		//			//				}
		//			//			});
		//			//return validBuyAPerOffPromosForProduct;
		//		}
		//		else if (promoCurrent instanceof BuyABFreePrecentageDiscountModel)
		//		{
		//			for (final ProductPromotionModel promo : productPromoData)
		//			{
		//				final Date sysdate = new Date();
		//				if (promo instanceof BuyABFreePrecentageDiscountModel)
		//				{
		//					final BuyABFreePrecentageDiscountModel buyAgetBfreePerOff = (BuyABFreePrecentageDiscountModel) promo;
		//					if (StringUtils.isEmpty(buyAgetBfreePerOff.getImmutableKey()) && buyAgetBfreePerOff.getEnabled().booleanValue()
		//							&& buyAgetBfreePerOff.getStartDate().compareTo(sysdate) <= 0
		//							&& buyAgetBfreePerOff.getEndDate().compareTo(sysdate) >= 0
		//							&& buyAgetBfreePerOff.getPriority().intValue() >= promoCurrent.getPriority().intValue()
		//							//					&& (promo.getStartDate().equals(sysdate) || promo.getStartDate().before(sysdate))
		//							//					&& (promo.getEndDate().equals(sysdate) || promo.getEndDate().after(sysdate))
		//							&& buyAgetBfreePerOff.getQuantity().intValue() == 1)
		//					{
		//						//validBuyAgetBFreePerOffPromosForProduct.add(buyAgetBfreePerOff);
		//						validPromosForProduct.add(buyAgetBfreePerOff);
		//					}
		//				}
		//			}
		//
		//			//Sorting promotion collection on priority DESC
		//			//			Collections.sort(validPromosForProduct, new Comparator<ProductPromotionModel>()
		//			//			{
		//			//				public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
		//			//				{
		//			//					if (o1.getPriority().intValue() > o2.getPriority().intValue())
		//			//					{
		//			//						return 1;
		//			//					}
		//			//					else
		//			//					{
		//			//						return -1;
		//			//					}
		//			//				}
		//			//			});
		//			//return validBuyAgetBFreePerOffPromosForProduct;
		//		}
		//
		//		//Sorting promotion collection on priority DESC
		//		Collections.sort(validPromosForProduct, new Comparator<ProductPromotionModel>()
		//		{
		//			public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
		//			{
		//				if (o1.getPriority().intValue() > o2.getPriority().intValue())
		//				{
		//					return 1;
		//				}
		//				else
		//				{
		//					return -1;
		//				}
		//			}
		//		});
		/////////////////-------------------------//////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////////////
		final List<ProductPromotionModel> validPromosForProduct = new ArrayList<ProductPromotionModel>();
		final Collection<CategoryModel> categories = getcategoryData(product);

		if (promoCurrent instanceof BuyAPercentageDiscountModel)
		{
			final StringBuilder queryString = new StringBuilder("SELECT DISTINCT pprom.pk, pprom.prio FROM (");
			queryString.append(" {{ SELECT {p." + BuyAPercentageDiscountModel.PK + "} AS pk, {p."
					+ BuyAPercentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyAPercentageDiscountModel._TYPECODE + " AS p ");
			queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION + " AS p2p ");
			queryString.append(" ON {p2p.target} = {p." + BuyAPercentageDiscountModel.PK + "}");
			queryString.append(" AND {p2p.source} = ?product }");
			//queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
			//			queryString.append(" AND {p2p.source} IN (?product) ");
			queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.ENABLED + "} = ?true ");
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.STARTDATE + "} <= ?sysdate ");
			queryString.append(" AND ?sysdate <= {p." + BuyAPercentageDiscountModel.ENDDATE + "} }}");

			if (CollectionUtils.isNotEmpty(categories))
			{
				queryString.append(MarketplacecommerceservicesConstants.QUERYUNION);
				queryString.append(" {{ SELECT {p." + BuyAPercentageDiscountModel.PK + "} AS pk, {p."
						+ BuyAPercentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyAPercentageDiscountModel._TYPECODE + " AS p ");
				queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION + " AS c2p ");
				queryString.append(" ON {p." + BuyAPercentageDiscountModel.PK + "} = {c2p.target} ");
				queryString.append(" AND {c2p.source} IN (?categories) }");
				//queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
				//				queryString.append(" AND {c2p.source} IN (?categories) ");
				queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.ENABLED + "} = ?true ");
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.STARTDATE + "} <= ?sysdate ");
				queryString.append(" AND ?sysdate <= {p." + BuyAPercentageDiscountModel.ENDDATE + "} }}");

				if (!(Config.isOracleUsed()))
				{
					queryString.append(" ) AS pprom ORDER BY pprom.prio DESC");
				}
				else
				{
					queryString.append(" ) pprom ORDER BY pprom.prio DESC");
				}

				LOG.debug("QUERY>>>>>>" + queryString);
				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

				query.addQueryParameter("product", product);
				query.addQueryParameter("categories", categories);
				//query.addQueryParameter("promotionGroup", "mplPromoGrp");
				query.addQueryParameter("promoCurrPriority", promoCurrent.getPriority());
				query.addQueryParameter("qualifyingCount", Integer.valueOf(1));
				query.addQueryParameter("sysdate", new Date());
				query.addQueryParameter("true", Boolean.TRUE);

				query.setResultClassList(Arrays.asList(ProductPromotionModel.class, Integer.class));

				LOG.debug("QUERY>>>>>>" + queryString);

				final SearchResult<List<Object>> result = flexibleSearchService.search(query);
				for (final List<Object> row : result.getResult())
				{
					validPromosForProduct.add((ProductPromotionModel) row.get(0));
					//final String priority = (String) row.get(1);
				}
			}
		}
		else if (promoCurrent instanceof BuyABFreePrecentageDiscountModel)
		{
			final StringBuilder queryString = new StringBuilder("SELECT DISTINCT pprom.pk, pprom.prio FROM (");
			queryString.append(" {{ SELECT {p." + BuyABFreePrecentageDiscountModel.PK + "} AS pk, {p."
					+ BuyABFreePrecentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyABFreePrecentageDiscountModel._TYPECODE
					+ " AS p ");
			queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION + " AS p2p ");
			queryString.append(" ON {p2p.target} = {p." + BuyABFreePrecentageDiscountModel.PK + "}");
			queryString.append(" AND {p2p.source} = ?product }");
			//queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
			//			queryString.append(" AND {p2p.source} IN (?product) ");
			queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.ENABLED + "} = ?true ");
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.STARTDATE + "} <= ?sysdate ");
			queryString.append(" AND ?sysdate <= {p." + BuyABFreePrecentageDiscountModel.ENDDATE + "} }}");

			if (CollectionUtils.isNotEmpty(categories))
			{
				queryString.append(MarketplacecommerceservicesConstants.QUERYUNION);
				queryString.append(" {{ SELECT {p." + BuyABFreePrecentageDiscountModel.PK + "} AS pk, {p."
						+ BuyABFreePrecentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyABFreePrecentageDiscountModel._TYPECODE
						+ " AS p ");
				queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION + " AS c2p ");
				queryString.append(" ON {p." + BuyABFreePrecentageDiscountModel.PK + "} = {c2p.target} ");
				queryString.append(" AND {c2p.source} IN (?categories) }");
				//queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
				//queryString.append(" WHERE {c2p.source} IN (?categories) ");
				queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.ENABLED + "} = ?true ");
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.STARTDATE + "} <= ?sysdate ");
				queryString.append(" AND ?sysdate <= {p." + BuyABFreePrecentageDiscountModel.ENDDATE + "} }}");
			}

			if (!(Config.isOracleUsed()))
			{
				queryString.append(" ) AS pprom ORDER BY pprom.prio DESC");
			}
			else
			{
				queryString.append(" ) pprom ORDER BY pprom.prio DESC");
			}

			LOG.debug("QUERY>>>>>>" + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter("product", product);
			query.addQueryParameter("categories", categories);
			//query.addQueryParameter("promotionGroup", "mplPromoGrp");
			query.addQueryParameter("promoCurrPriority", promoCurrent.getPriority());
			query.addQueryParameter("qualifyingCount", "1");
			query.addQueryParameter("sysdate", new Date());
			query.addQueryParameter("true", Boolean.TRUE);

			query.setResultClassList(Arrays.asList(ProductPromotionModel.class, Integer.class));

			LOG.debug("QUERY>>>>>>" + queryString);

			final SearchResult<List<Object>> result = flexibleSearchService.search(query);
			for (final List<Object> row : result.getResult())
			{
				validPromosForProduct.add((ProductPromotionModel) row.get(0));
				//final String priority = (String) row.get(1);
			}
		}
		///////////////////////////////////////////////////
		return validPromosForProduct;
	}

	private List<ProductModel> getProductsForCategory(final List<CategoryModel> categories,
			final List<ProductModel> exProductList, final List<CategoryModel> brands, final List<CategoryModel> rejectBrandList)
	{
		final List<CategoryModel> categoryList = getAllCategories(categories);
		final Map params = new HashMap();
		params.put("categories", categoryList);

		final StringBuilder queryString = new StringBuilder("SELECT DISTINCT pprom.pk FROM (");
		queryString.append(" {{ SELECT {c2p.target} as pk " + queryString.append(MarketplacecommerceservicesConstants.QUERYFROM)
				+ "{" + GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION + " AS c2p }");
		queryString.append(" WHERE {c2p.source} IN (?categories)");

		if (CollectionUtils.isEmpty(exProductList) && CollectionUtils.isEmpty(brands) && CollectionUtils.isEmpty(rejectBrandList))
		{
			queryString.append(" }} ");
		}
		else
		{
			if (CollectionUtils.isNotEmpty(exProductList))
			{
				queryString.append(" AND {c2p.target} NOT IN (?exProducts) }}");
				params.put("exProducts", exProductList);
			}

			if (CollectionUtils.isNotEmpty(brands))
			{
				queryString.append(" INTERSECT ");
				queryString.append("{{ SELECT {cat2prod.target} as pk  ");
				queryString.append(MarketplacecommerceservicesConstants.QUERYFROM)
						.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS cat2prod } ");
				queryString.append(" WHERE {cat2prod.source} in (?brands) }} ");

				params.put("brands", brands);
			}
			else if (CollectionUtils.isNotEmpty(rejectBrandList))
			{
				queryString.append(" INTERSECT ");
				queryString.append("{{ SELECT {cat2prod:target} as pk  ");
				queryString.append(MarketplacecommerceservicesConstants.QUERYFROM).append(
						GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION);
				queryString.append(" AS cat2prod JOIN ").append(CategoryModel._TYPECODE)
						.append(" AS category on {cat2prod.source} = {category.pk}} ");
				queryString
						.append(
								"WHERE {cat2prod:target} in (?product) AND {cat2prod:source} not in (?rejectBrands) AND {category.code} like '%")
						.append(MarketplacecommerceservicesConstants.BRAND_NAME_PREFIX).append("%' }} ");

				params.put("rejectBrands", rejectBrandList);
			}
		}

		if (!(Config.isOracleUsed()))
		{
			queryString.append(" ) AS pprom");
		}
		else
		{
			queryString.append(" ) pprom");
		}

		LOG.debug("QUERY>>>>>>" + queryString);

		final List<ProductModel> productValidList = flexibleSearchService.<ProductModel> search(queryString.toString(), params)
				.getResult();

		return productValidList;

	}

	@Autowired
	private FlexibleSearchService flexibleSearchService;
}
