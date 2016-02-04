/**
 *
 */
package com.tisl.mpl.promotion.service.impl;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.BuyAPercentageDiscount;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.dao.impl.UpdatePromotionalPriceDaoImpl;
import com.tisl.mpl.promotion.service.UpdatePromotionalPriceService;


/**
 * @author TCS
 *
 */
public class UpdatePromotionalPriceServiceImpl implements UpdatePromotionalPriceService
{
	private ModelService modelService;


	@Resource(name = "mplUpdatePromotionPriceDao")
	private UpdatePromotionalPriceDaoImpl updatePromotionalPriceDao;

	private static final Logger LOG = Logger.getLogger(UpdatePromotionalPriceServiceImpl.class);

	public ModelService getModelService()
	{
		return modelService;
	}


	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.promotion.service.UpdatePromotionalPriceService#updatePromotionalPrice(java.util.Collection,
	 * java.util.Collection, java.lang.Double, java.util.Date, java.util.Date, boolean)
	 */
	@Override
	public void updatePromotionalPrice(final List<Product> products, final List<Category> categories, final Double value,
			final Date startDate, final Date endtDate, final boolean percent, final Integer priority, final List<String> sellers,
			final List<String> brands)
	{

		try
		{
			final List<String> product = new ArrayList<String>();
			if (products != null && !products.isEmpty())
			{
				for (final Product itrProduct : products)
				{
					if (getBrandsForProduct(itrProduct, brands) && validateProductData(itrProduct, priority))
					{
						product.add(itrProduct.getAttribute("pk").toString());
					}
				}
			}

			if (categories != null && !categories.isEmpty())
			{
				for (final Category category : categories)
				{

					for (final Product itrProduct : category.getProducts())
					{
						if (getBrandsForProduct(itrProduct, brands) && validateCategoryProductData(itrProduct, priority))////call same method for product
						{
							product.add(itrProduct.getAttribute("pk").toString());
						}
					}

				}

			}

			LOG.debug("******** Special Price - Promotion Applicable product List:" + product);
			//filter product list based on brand restriction


			if (!product.isEmpty())
			{
				boolean updateSpecialPrice = false;
				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product);
				for (final PriceRowModel price : priceRow)
				{
					//updating price row
					boolean isEligibletoDisable = false;
					if (sellers.isEmpty())
					{
						updateSpecialPrice = true;//set the flag true if there is no seller restriction
					}
					else
					{
						updateSpecialPrice = isPriceToUpdate(price, sellers);
						if (!updateSpecialPrice)
						{
							isEligibletoDisable = true;
						}
					}

					if (updateSpecialPrice)
					{
						if (!percent)
						{
							price.setPromotionStartDate(startDate);
							price.setPromotionEndDate(endtDate);
							price.setIsPercentage(Boolean.valueOf(percent));
							price.setPromotionValue(value);
						}
						else
						{
							price.setPromotionStartDate(startDate);
							price.setPromotionEndDate(endtDate);
							price.setIsPercentage(Boolean.valueOf(percent));
							price.setPromotionValue(value);
						}
						modelService.save(price);
					}
					else if (!updateSpecialPrice && isEligibletoDisable)
					{
						LOG.debug("Removing Promotion Details from the Price Row : USSID not eligible for Promotion");
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);

						LOG.debug("Saving Price Row ");
						modelService.save(price);
					}

				}
			}

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Description: To check whether to update the special price or not if seller restriction exists
	 * @param price
	 * @param sellers
	 * @return: updateSpecialPrice
	 */
	private boolean isPriceToUpdate(final PriceRowModel price, final List<String> sellers)
	{
		boolean updateSpecialPrice = false;
		final List<SellerInformationModel> sellerModels = new ArrayList<SellerInformationModel>(price.getProduct()
				.getSellerInformationRelator());
		for (final SellerInformationModel seller : sellerModels)
		{
			if (!sellers.isEmpty() && sellers.contains(seller.getSellerID())
					&& price.getSellerArticleSKU().equalsIgnoreCase(seller.getSellerArticleSKU()))
			{
				LOG.debug("******** Special Price - Promotion Applicable ussid List:" + seller.getSellerArticleSKU());
				updateSpecialPrice = true;
			}
		}

		return updateSpecialPrice;
	}

	@Override
	public void disablePromotionalPrice(final List<Product> products, final List<Category> categories, final boolean isEnabled,
			final Integer priority, final List<String> brands)
	{
		try
		{
			final List<String> product = new ArrayList<String>();
			if (products != null && !products.isEmpty())
			{
				for (final Product itrProduct : products)
				{
					if (getBrandsForProduct(itrProduct, brands) && validateProductData(itrProduct, priority))
					{
						product.add(itrProduct.getAttribute("pk").toString());
					}


				}
			}

			if (categories != null && !categories.isEmpty())
			{
				for (final Category category : categories)
				{
					for (final Product itrProduct : category.getProducts())
					{
						if (getBrandsForProduct(itrProduct, brands) && validateCategoryProductData(itrProduct, priority))//call same method for product
						{
							product.add(itrProduct.getAttribute("pk").toString());
						}

					}

				}

			}

			LOG.debug("******** Special Price - Disable Promotion Applicable product List:" + product);


			if (!product.isEmpty())
			{

				final List<PriceRowModel> priceRow = updatePromotionalPriceDao.fetchPricedData(product);
				for (final PriceRowModel price : priceRow)
				{
					if (!isEnabled)
					{
						price.setPromotionStartDate(null);
						price.setPromotionEndDate(null);
						price.setIsPercentage(null);
						price.setPromotionValue(null);
					}
					modelService.save(price);
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


	private boolean validateProductData(final Product product, final Integer priority)
	{
		try
		{
			final List<ProductPromotion> promotionData = (List<ProductPromotion>) product.getAttribute("promotions");
			//	final boolean isHigherPromotionExists = true;
			int maxPriority = priority.intValue();
			if (null != promotionData && !promotionData.isEmpty())
			{
				for (final ProductPromotion promotion : promotionData)
				{
					if (promotion instanceof BuyAPercentageDiscount && null != promotion.getAttribute("quantity")
							&& Integer.parseInt(promotion.getAttribute("quantity").toString()) == 1)
					{
						if (maxPriority < Integer.parseInt(promotion.getAttribute("priority").toString())
								&& promotion.isEnabled().booleanValue())
						{
							maxPriority = Integer.parseInt(promotion.getAttribute("priority").toString());
							//	isHigherPromotionExists = false;
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
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}


		return false;
	}


	private boolean validateCategoryProductData(final Product product, final Integer priority)
	{
		try
		{
			int maxPriority = priority.intValue();
			final List<ProductPromotion> promotionData = new ArrayList<ProductPromotion>();
			final List<Category> categoriesList = getImmediateSuperCategory(product);

			final List<ProductPromotion> productPromoData = (List<ProductPromotion>) product.getAttribute("promotions");
			if (CollectionUtils.isNotEmpty(categoriesList))
			{
				for (final Category category : categoriesList)
				{
					promotionData.addAll(checkCategoryData(category));
				}

				if (CollectionUtils.isNotEmpty(productPromoData))
				{
					promotionData.addAll(productPromoData);
				}

				if (CollectionUtils.isNotEmpty(promotionData))
				{
					for (final ProductPromotion promotion : promotionData)
					{
						if (promotion instanceof BuyAPercentageDiscount && null != promotion.getAttribute("quantity")
								&& Integer.parseInt(promotion.getAttribute("quantity").toString()) == 1)
						{
							if (maxPriority < Integer.parseInt(promotion.getAttribute("priority").toString())
									&& promotion.isEnabled().booleanValue())
							{
								maxPriority = Integer.parseInt(promotion.getAttribute("priority").toString());
								//	isHigherPromotionExists = false;
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
	 * @param category
	 */
	private List<ProductPromotion> checkCategoryData(final Category category)
	{
		List<ProductPromotion> promotionData = new ArrayList<ProductPromotion>();
		try
		{
			if (null != category && null != category.getAttribute("promotions"))
			{
				promotionData = (List<ProductPromotion>) category.getAttribute("promotions");
			}
		}
		catch (final JaloInvalidParameterException exception)
		{
			throw new EtailNonBusinessExceptions(exception, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final JaloSecurityException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return promotionData;

	}


	//SONAR Fixes
	//	@Deprecated
	//	private boolean validateCategoryProductData(final Product product, final Integer priority)
	//	{
	//		try
	//		{
	//			//final String quantity = configurationService.getConfiguration().getString("promotion.quantity.value");
	//			final List<ProductPromotion> promotionData = (List<ProductPromotion>) product.getAttribute("promotions");
	//
	//			if (null != promotionData && !promotionData.isEmpty())
	//			{
	//				for (final ProductPromotion promotion : promotionData)
	//				{
	//					if (promotion instanceof BuyAPercentageDiscount && null != promotion.getAttribute("quantity")
	//							&& Integer.parseInt(promotion.getAttribute("quantity").toString()) == 1)
	//					{
	//						if (Integer.parseInt(promotion.getAttribute("priority").toString()) >= priority.intValue()
	//								&& promotion.isEnabled().booleanValue())
	//						{
	//							return false;
	//						}
	//						else if (Integer.parseInt(promotion.getAttribute("priority").toString()) == priority.intValue()
	//								&& !promotion.isEnabled().booleanValue())//disabling scenario
	//						{
	//							return true;
	//						}
	//					}
	//
	//				}
	//
	//			}
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
	//		}
	//
	//
	//		return true;
	//	}

	private boolean getBrandsForProduct(final Product product, final List<String> brands)
	{
		boolean allow = false;
		List<String> brandList = null;

		try
		{

			if (brands.isEmpty())//no need to proceed if there is no brand restriction
			{
				return true;
			}

			final List<Category> categories = getImmediateSuperCategory(product);
			if (categories != null && !categories.isEmpty())
			{
				brandList = new ArrayList<String>();
				for (final Category category : categories)
				{
					if (category.getCode().startsWith("MBH"))
					{
						brandList.add(category.getCode());
					}

				}
			}

			if (null != brandList && !brandList.isEmpty())
			{
				final String productBrand = brandList.get(0);
				if (brands.contains(productBrand))
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


	private List<Category> getImmediateSuperCategory(final Product product)
	{

		List<Category> superCategories = new ArrayList<Category>();

		if (product != null)
		{

			try
			{
				superCategories = (List<Category>) product.getAttribute("supercategories");
			}
			catch (final JaloInvalidParameterException e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}
			catch (final JaloSecurityException e)
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


}
