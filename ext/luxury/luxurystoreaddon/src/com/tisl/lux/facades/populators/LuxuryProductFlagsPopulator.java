/**
 *
 */
package com.tisl.lux.facades.populators;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.enums.OnlineExclusiveEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.ExcludeManufacturersRestrictionModel;
import com.tisl.mpl.model.ManufacturersRestrictionModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.util.MplBuyBoxUtility;


/**
 * @author Madhavan
 *
 */
public class LuxuryProductFlagsPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends AbstractProductPopulator<SOURCE, TARGET>
{


	private MplBuyBoxUtility mplBuyBoxUtility;

	private ConfigurationService configurationService;

	private PromotionsService promotionService;
	private TimeService timeService;

	private BaseSiteService baseSiteService;




	/**
	 *
	 */
	public LuxuryProductFlagsPopulator()
	{
		super();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		productData.setIsProductNew(Boolean.valueOf(findIsNew(productModel)));
		productData.setIsOfferExisting(Boolean.valueOf(findIsOnSale(productModel)));
		productData.setIsOnlineExclusive(Boolean.valueOf(findIsExclusive(productModel)));

	}


	/**
	 * @param productModel
	 * @return
	 */
	private boolean findIsNew(final ProductModel productModel)
	{
		Date existDate = null;
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if (seller != null)
			{
				//Find the oldest startDate of the seller
				if ((null == existDate && seller.getStartDate() != null)
						|| (existDate != null && seller.getStartDate() != null && existDate.after(seller.getStartDate())))
				{
					existDate = seller.getStartDate();
				}

			}
		}

		if (null != existDate && isNew(existDate))
		{
			return true;
		}
		return false;
	}

	private boolean isNew(final Date existDate)
	{
		boolean newAttr = false;
		if (null != existDate)
		{
			final Date sysDate = new Date();
			final long dayDiff = calculateDays(existDate, sysDate);

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
	 * @param productModel
	 * @return
	 */
	private boolean findIsOnSale(final ProductModel productModel)
	{
		final boolean offerExists = false;
		final BaseSiteModel baseSiteModel = baseSiteService.getCurrentBaseSite();
		if ((baseSiteModel != null) && (baseSiteModel.getDefaultPromotionGroup() != null))
		{
			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);
			final List<ProductPromotionModel> productPromotions = getPromotionService().getProductPromotions(
					Collections.singletonList(baseSiteModel.getDefaultPromotionGroup()), productModel, true,
					currentTimeRoundedToMinute);

			final List<ProductPromotionModel> restrictedPromotions = validatePromotionRestrictions(productPromotions, productModel);


			for (final ProductPromotionModel promotion : restrictedPromotions)
			{
				if (promotion.getChannel().contains(SalesApplication.WEB) || promotion.getChannel().isEmpty())
				{
					return true;
				}
			}

		}

		return offerExists;
	}

	/**
	 * @return the promotionService
	 */
	public PromotionsService getPromotionService()
	{
		return promotionService;
	}


	/**
	 * @param promotionService
	 *           the promotionService to set
	 */
	public void setPromotionService(final PromotionsService promotionService)
	{
		this.promotionService = promotionService;
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

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}


	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @param productModel
	 * @return
	 */
	private boolean findIsExclusive(final ProductModel productModel)
	{
		boolean isOnlineExclusive = false;
		for (final SellerInformationModel seller : productModel.getSellerInformationRelator())
		{
			if (seller != null)
			{
				final OnlineExclusiveEnum onlineExclusiveEnum = seller.getOnlineExclusive();
				if (null != onlineExclusiveEnum)
				{
					isOnlineExclusive = true;
					break;
				}
			}
		}
		return isOnlineExclusive;
	}


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
	public void setMplBuyBoxUtility(final MplBuyBoxUtility mplBuyBoxUtility)
	{
		this.mplBuyBoxUtility = mplBuyBoxUtility;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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

					boolean checkSellerRestrictionForFreeBee = false;
					for (final AbstractPromotionRestrictionModel restriction : productPromotion.getRestrictions())
					{

						boolean excluseBrandRestrictionPresent = false;

						//checking if BOGO promotion present or not and removing the promotion if seller restriction not present
						if (restriction instanceof EtailSellerSpecificRestrictionModel
								&& isPromoEligibleForproduct(restriction, productModel))
						{
							checkSellerRestrictionForFreeBee = true;
						}


						//Seller restriction check for non free bee promotion
						if (restriction instanceof EtailSellerSpecificRestrictionModel
								&& !isPromoEligibleForproduct(restriction, productModel))
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

								toRemovePromotionList.add(productPromotion);
							}
						}


					}
					if (!checkSellerRestrictionForFreeBee && isFreeBee)
					{
						toRemovePromotionList.add(productPromotion);
						excludePromotion = true;
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

	//Seller restriction check for non free bee promotion
	private boolean isPromoEligibleForproduct(final AbstractPromotionRestrictionModel restriction, final ProductModel product)
	{
		List<String> allowedSellerList = null;
		boolean eligibleForPromo = false;
		if (restriction instanceof EtailSellerSpecificRestrictionModel)
		{
			allowedSellerList = new ArrayList<String>();
			final EtailSellerSpecificRestrictionModel sellerRestriction = (EtailSellerSpecificRestrictionModel) restriction;
			if (null != sellerRestriction.getSellerMasterList() && !sellerRestriction.getSellerMasterList().isEmpty())
			{
				final List<SellerMasterModel> sellerList = sellerRestriction.getSellerMasterList();
				for (final SellerMasterModel seller : sellerList)
				{
					allowedSellerList.add(seller.getId());
				}

			}

		}



		if (null != allowedSellerList && !allowedSellerList.isEmpty())
		{
			for (final SellerInformationModel seller : product.getSellerInformationRelator())
			{
				if (allowedSellerList.contains(seller.getSellerID()))
				{
					eligibleForPromo = true;
					break;
				}
			}
		}



		return eligibleForPromo;

	}


}
