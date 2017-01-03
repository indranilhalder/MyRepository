/**
 * TCS
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.helper.ProductDetailsHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockLevelPromotionCheckService;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.EtailLimitedStockRestrictionModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.ExcludeManufacturersRestrictionModel;
import com.tisl.mpl.model.ManufacturersRestrictionModel;


/**
 * @author TCS
 */
/**
 *
 * @description Populate the product data with product promotions
 *
 * @param <SOURCE>
 * @param <TARGET>
 */
public class CustomProductPromotionsPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{
	private PromotionsService promotionsService;
	private Converter<AbstractPromotionModel, PromotionData> promotionsConverter;
	private TimeService timeService;
	private BaseSiteService baseSiteService;
	@Resource(name = "productDetailsHelper")
	private ProductDetailsHelper productDetailsHelper;
	private static final String AS_CLASS = " AS bb}";

	private static final String WHERE_CLASS = " WHERE {bb:";
	protected static final Logger LOG = Logger.getLogger(CustomImagePopulator.class);
	@Resource(name = "stockPromoCheckService")
	private ExtStockLevelPromotionCheckService stockPromoCheckService;
	private static final String SELECT_CLASS = "SELECT {bb.PK} FROM {";
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	protected PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	@Required
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}

	protected Converter<AbstractPromotionModel, PromotionData> getPromotionsConverter()
	{
		return promotionsConverter;
	}

	@Required
	public void setPromotionsConverter(final Converter<AbstractPromotionModel, PromotionData> promotionsConverter)
	{
		this.promotionsConverter = promotionsConverter;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 *
	 * @author Populate the product data with product promotions
	 *
	 * @param productModel
	 * @param productData
	 */
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException,
			EtailNonBusinessExceptions
	{
		final BaseSiteModel baseSiteModel = getBaseSiteService().getCurrentBaseSite();
		if (baseSiteModel != null)
		{
			final PromotionGroupModel defaultPromotionGroup = baseSiteModel.getDefaultPromotionGroup();
			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);

			if (defaultPromotionGroup != null)
			{
				final List<ProductPromotionModel> promotions = getPromotionsService().getProductPromotions(
						Collections.singletonList(defaultPromotionGroup), productModel, true, currentTimeRoundedToMinute);



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
								final List<ProductModel> excludedList = new ArrayList<ProductModel>(
										productPromotion.getExcludedProducts());
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

							// TISPRD-2488
							boolean isSellerRestrPresent = false;

							for (final AbstractPromotionRestrictionModel restriction : productPromotion.getRestrictions())
							{

								boolean excluseBrandRestrictionPresent = false;

								//checking if BOGO promotion present or not and removing the promotion if seller restriction not present
								/*
								 * if (!(restriction instanceof EtailSellerSpecificRestrictionModel) && isFreeBee) {
								 * toRemovePromotionList.add(productPromotion); excludePromotion = true; break; }
								 */

								//checking if BOGO promotion present or not and removing the promotion if seller restriction not present
								if (restriction instanceof EtailSellerSpecificRestrictionModel)
								{
									isSellerRestrPresent = true;
								}

								//checking Exclude brandRestriction
								if (restriction instanceof ExcludeManufacturersRestrictionModel)
								{

									final ExcludeManufacturersRestrictionModel brandRestriction = (ExcludeManufacturersRestrictionModel) restriction;
									final List<CategoryModel> brandRestrictions = new ArrayList<CategoryModel>(
											brandRestriction.getManufacturers());
									final List<String> brands = new ArrayList<String>(
											productDetailsHelper.getBrandsForProduct(productModel));
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
									final List<String> brands = new ArrayList<String>(
											productDetailsHelper.getBrandsForProduct(productModel));
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

							if (!isSellerRestrPresent && isFreeBee)
							{
								toRemovePromotionList.add(productPromotion);
								excludePromotion = true;
							}
							/* TPR-4107 */
							for (final AbstractPromotionRestrictionModel restriction : productPromotion.getRestrictions())
							{
								if (restriction instanceof EtailLimitedStockRestrictionModel)
								{
									final EtailLimitedStockRestrictionModel stockRestrictrion = (EtailLimitedStockRestrictionModel) restriction;
									final String productCode = MarketplacecommerceservicesConstants.INVERTED_COMMA
											+ productModel.getCode() + MarketplacecommerceservicesConstants.INVERTED_COMMA;
									final Map<String, Integer> stockMap = stockPromoCheckService.getCumulativeStockMap(productCode,
											productPromotion.getCode(), false);
									if (!stockMap.isEmpty())
									{
										final Integer stockQuantity = stockMap.get(productModel.getCode());
										final int stockValue = stockQuantity == null ? 0 : stockQuantity.intValue();
										if ((stockRestrictrion.getMaxStock().intValue() - stockValue) == 0)
										{
											toRemovePromotionList.add(productPromotion);
										}
									}
								}
							}


							//TPR-965 changes starts

							//TPR-965 changes ends
						}//end
					}//end promotion for loop
				}
				if (!toRemovePromotionList.isEmpty())
				{
					promotions.removeAll(toRemovePromotionList);
				}



				//remove from potential promotion list

				//excluded product check ends

				productData.setPotentialPromotions(Converters.convertAll(promotions, getPromotionsConverter()));
			}
		}
	}


	/**
	 * @param restrictions
	 * @return
	 */
	//	private boolean isLimitedStockRestrictionExists(final Collection<AbstractPromotionRestrictionModel> restrictions)
	//	{
	//		boolean isExists = false;
	//		for (final AbstractPromotionRestrictionModel restriction : restrictions)
	//		{
	//			if (restriction instanceof EtailLimitedStockRestrictionModel)
	//			{
	//				isExists = true;
	//				break;
	//			}
	//		}
	//		return isExists;
	//	}

	/**
	 * checks for a BOGO promotion present in the product
	 *
	 * @param productPromotion
	 * @return
	 */
	private boolean isFreeBeePromotionExists(final ProductPromotionModel productPromotion)
	{
		boolean isFreeBree = false;
		if (productPromotion instanceof BuyXItemsofproductAgetproductBforfreeModel)
		{
			isFreeBree = true;
		}
		return isFreeBree;
	}



	public List<BuyBoxModel> buyBoxModelForSeller(final String sellerID)
	{
		try
		{

			final String queryStringForStock = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS + WHERE_CLASS + BuyBoxModel.SELLERID
					+ "}=?sellerid";

			final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(queryStringForStock);
			flexQuery.addQueryParameter("sellerid", sellerID);
			return flexibleSearchService.<BuyBoxModel> search(flexQuery).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


}