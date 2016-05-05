package com.tisl.mpl.interceptor;

/**
 *
 */

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;


public class PromotionPriorityInterceptor implements ValidateInterceptor
{
	@Autowired
	private ProductService productService;
	private ModelService modelService;
	private static final Logger LOG = Logger.getLogger(PromotionPriorityInterceptor.class);
	private static final String MODIFY_MESSAGE = "promotion.priorityIntercepter.modifyData.message";
	private static final String ERROR_MESSAGE = "promotion.priority.product.sameSeller.errormessage";
	private static final String PRODUCT_ERROR_MESSAGE = "promotion.priority.product.errormessage";
	@Autowired
	private MplPromotionHelper mplPromotionHelper;

	@Autowired
	private ConfigurationService configurationService;


	public ModelService getModelService()
	{
		return modelService;
	}


	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}


	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}


	/**
	 * @return the mplPromotionHelper
	 */
	public MplPromotionHelper getMplPromotionHelper()
	{
		return mplPromotionHelper;
	}


	/**
	 * @param mplPromotionHelper
	 *           the mplPromotionHelper to set
	 */
	public void setMplPromotionHelper(final MplPromotionHelper mplPromotionHelper)
	{
		this.mplPromotionHelper = mplPromotionHelper;
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

	/**
	 * @Description : The Method checks the Promotion Priority prior to its creation from HMCR
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onValidate(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		LOG.debug(Localization.getLocalizedString("promotion.priorityIntercepter.message"));

		if (object instanceof ProductPromotionModel)
		{
			//@Description :To check if an Enabled Promotions exists with the Product and same priority
			final ProductPromotionModel promotion = (ProductPromotionModel) object;
			populatePromotionGroup(promotion);
			if (promotion.getProducts() != null && !promotion.getProducts().isEmpty())
			{

				for (final ProductModel product : promotion.getProducts())
				{
					if (product.getPromotions() != null && !product.getPromotions().isEmpty())
					{
						for (final ProductPromotionModel productPromotion : product.getPromotions())
						{
							if (productPromotion.getCode().equalsIgnoreCase(promotion.getCode())
									&& productPromotion.getPromotionType().equals(promotion.getPromotionType()))
							{
								LOG.debug(Localization.getLocalizedString(MODIFY_MESSAGE));
								continue;
							}

							if (productPromotion.getEnabled().booleanValue()
									&& productPromotion.getPriority().equals(promotion.getPriority()))
							{
								final List<AbstractPromotionRestrictionModel> promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
										promotion.getRestrictions());
								final List<AbstractPromotionRestrictionModel> singleProdRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
										productPromotion.getRestrictions());
								String errorMsg = null;

								if (!isSellerRestrExistsForModel(promotionRestrictionList)
										|| !isSellerRestrExistsForModel(singleProdRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
								}
								else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
								}
								else
								{
									break;
								}
								throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.PROMOCODE + productPromotion.getCode()
										+ MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.PROMOPRODUCT + product.getCode() + "(" + product.getName()
										+ ")" + MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.PROMOPRIORITY + productPromotion.getPriority()
										+ MarketplacecommerceservicesConstants.PROMOPRIORITY + productPromotion.getPriority());
								//}
							}

						}
					}


					//@Description :To check if an Enabled Promotions exists with the Category to which the Product belongs and same priority
					final List<CategoryModel> productCategoryList = getDefaultPromotionsManager().getcategoryData(product);
					if (null != productCategoryList)
					{
						for (final CategoryModel category : productCategoryList)
						{
							if (null != category.getPromotions() && !category.getPromotions().isEmpty())
							{
								for (final ProductPromotionModel promo : category.getPromotions())
								{
									if (promo.getCode().equalsIgnoreCase(promotion.getCode())
											&& promo.getPromotionType().equals(promotion.getPromotionType()))
									{
										LOG.debug(Localization.getLocalizedString(MODIFY_MESSAGE));
										continue;
									}

									if (promo.getEnabled().booleanValue() && promo.getPriority().equals(promotion.getPriority()))
									{
										final List<AbstractPromotionRestrictionModel> promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
												promotion.getRestrictions());
										final List<AbstractPromotionRestrictionModel> singleProdRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
												promo.getRestrictions());
										String errorMsg = null;

										if (!isSellerRestrExistsForModel(promotionRestrictionList)
												|| !isSellerRestrExistsForModel(singleProdRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
										}
										else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
										}
										else
										{
											break;
										}

										throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PROMOCODE + promo.getCode()
												+ MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PROMOCATEGORY + category.getCode() + "("
												+ category.getName() + ")" + MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PROMOPRIORITY + promo.getPriority());
										//}
									}
								}
							}
						}
					}
				}
			}
			else if (promotion.getCategories() != null && !promotion.getCategories().isEmpty())
			{
				//@Description :To check if an Enabled Promotions exists with the Category and same priority
				for (final CategoryModel category : promotion.getCategories())
				{
					if (category.getPromotions() != null && !category.getPromotions().isEmpty())
					{
						for (final ProductPromotionModel categoryPromotion : category.getPromotions())
						{
							if (categoryPromotion.getCode().equalsIgnoreCase(promotion.getCode())
									&& categoryPromotion.getPromotionType().equals(promotion.getPromotionType()))

							{
								LOG.debug(Localization.getLocalizedString(MODIFY_MESSAGE));
								continue;
							}

							if (categoryPromotion.getEnabled().booleanValue()
									&& categoryPromotion.getPriority().equals(promotion.getPriority()))
							{
								final List<AbstractPromotionRestrictionModel> promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
										promotion.getRestrictions());
								final List<AbstractPromotionRestrictionModel> singleProdRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
										categoryPromotion.getRestrictions());
								String errorMsg = null;

								if (!isSellerRestrExistsForModel(promotionRestrictionList)
										|| !isSellerRestrExistsForModel(singleProdRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
								}
								else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
								}
								else
								{
									break;
								}
								throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.PROMOCODE + categoryPromotion.getCode()
										+ MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.PROMOCATEGORY + category.getCode() + "("
										+ category.getName() + ")" + MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.PROMOPRIORITY + categoryPromotion.getPriority());
								//}
							}
						}
					}

					//@Description :To check if an Enabled Promotions exists with the Product under the set Category  and same priority
					final List<ProductModel> productData = productService.getProductsForCategory(category);
					if (null != productData && !productData.isEmpty())
					{
						for (final ProductModel promoProduct : productData)
						{
							if (null != promoProduct.getPromotions() && !promoProduct.getPromotions().isEmpty())
							{
								for (final ProductPromotionModel promo : promoProduct.getPromotions())
								{
									if (promo.getCode().equalsIgnoreCase(promotion.getCode())
											&& promo.getPromotionType().equals(promotion.getPromotionType()))
									{
										LOG.debug(Localization.getLocalizedString(MODIFY_MESSAGE));
										continue;
									}

									if (promo.getEnabled().booleanValue() && promo.getPriority().equals(promotion.getPriority()))
									{
										final List<AbstractPromotionRestrictionModel> promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
												promotion.getRestrictions());
										final List<AbstractPromotionRestrictionModel> singleProdRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
												promo.getRestrictions());
										String errorMsg = null;

										if (!isSellerRestrExistsForModel(promotionRestrictionList)
												|| !isSellerRestrExistsForModel(singleProdRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
										}
										else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
										}
										else
										{
											break;
										}
										throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PROMOCODE + promo.getCode()
												+ MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PROMOPRODUCT + promoProduct.getCode() + "("
												+ promoProduct.getName() + ")" + MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PRESENT_CATEGORY + category.getName()
												+ MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PROMOPRIORITY + promo.getPriority());
									}
								}
							}
						}

					}
				}
			}
		}
		else if (object instanceof OrderPromotionModel)
		{
			final OrderPromotionModel promotion = (OrderPromotionModel) object;
			populatePromotionGroup(promotion);
			final String promoCode = checkCartPromoPriority(promotion);
			if (StringUtils.isNotEmpty(promoCode))
			{
				final String errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
				throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ MarketplacecommerceservicesConstants.PROMOCODE + promoCode
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE + MarketplacecommerceservicesConstants.PROMOPRIORITY
						+ promotion.getPriority());
			}
		}
	}

	/**
	 * The Method checks the priority for Active Cart Promotions
	 *
	 * @param promotion
	 * @return flag
	 */
	private String checkCartPromoPriority(final OrderPromotionModel promotion)
	{
		String promoCode = MarketplacecommerceservicesConstants.EMPTY;

		if (null != promotion.getEnabled() && promotion.getEnabled().booleanValue())
		{
			LOG.debug("Checking Priority of Cart Promotion----Present Priority:" + promotion.getPriority());
			promoCode = mplPromotionHelper.checkCartPromoPriority(promotion);
		}
		return promoCode;
	}


	/**
	 * The Method to auto populate a promotion group.
	 *
	 * @param promotion
	 */
	private void populatePromotionGroup(final AbstractPromotionModel promotion)
	{
		if (null != promotion && null == promotion.getPromotionGroup())
		{
			promotion.setPromotionGroup(mplPromotionHelper.fetchPromotionGroupDetails(configurationService.getConfiguration()
					.getString("promotion.default.promotionGroup.identifier")));
		}
	}


	private boolean checkIfSetforSameSeller(final List<AbstractPromotionRestrictionModel> singleProdRestrictionList,
			final List<AbstractPromotionRestrictionModel> promotionRestrictionList)
	{
		boolean flag = false;
		List<String> singleProdSellerIds = null;
		List<String> promotionSellerIds = null;

		List<String> commonSellerIdList = null;
		try
		{
			for (final AbstractPromotionRestrictionModel restriction : singleProdRestrictionList)
			{
				if (restriction instanceof EtailSellerSpecificRestrictionModel)
				{
					final EtailSellerSpecificRestrictionModel etailSellerSpecificRestriction = (EtailSellerSpecificRestrictionModel) restriction;
					singleProdSellerIds = new ArrayList<String>();

					for (final SellerMasterModel singleProdSeller : etailSellerSpecificRestriction.getSellerMasterList())
					{
						singleProdSellerIds.add(singleProdSeller.getId());
					}

				}
			}

			for (final AbstractPromotionRestrictionModel restriction : promotionRestrictionList)
			{
				if (restriction instanceof EtailSellerSpecificRestrictionModel)
				{
					final EtailSellerSpecificRestrictionModel etailSellerSpecificRestriction = (EtailSellerSpecificRestrictionModel) restriction;
					promotionSellerIds = new ArrayList<String>();

					for (final SellerMasterModel promotionProdSeller : etailSellerSpecificRestriction.getSellerMasterList())
					{
						promotionSellerIds.add(promotionProdSeller.getId());
					}
				}
			}

			if (singleProdSellerIds != null && !singleProdSellerIds.isEmpty() && promotionSellerIds != null
					&& !promotionSellerIds.isEmpty())
			{
				commonSellerIdList = new ArrayList<String>();
				commonSellerIdList.addAll(singleProdSellerIds);
				commonSellerIdList.retainAll(promotionSellerIds);

				if (!commonSellerIdList.isEmpty())
				{
					flag = true;
				}
			}
			//}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return flag;

	}

	/**
	 * @Description: For checking whether seller restriction is attached with the promotion
	 * @param restrictionList
	 * @return boolean
	 */

	public boolean isSellerRestrExistsForModel(final List<AbstractPromotionRestrictionModel> restrictionList)
	{
		boolean isSellerRestr = false;
		if (restrictionList != null && !restrictionList.isEmpty())
		{
			for (final AbstractPromotionRestrictionModel restriction : restrictionList)
			{
				if (restriction instanceof EtailSellerSpecificRestrictionModel)
				{
					isSellerRestr = true;
					break;
				}
			}
		}
		return isSellerRestr;
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}
}
