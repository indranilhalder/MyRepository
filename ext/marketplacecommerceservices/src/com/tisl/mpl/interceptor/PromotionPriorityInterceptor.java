package com.tisl.mpl.interceptor;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
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
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.MplProductSteppedMultiBuyPromotionModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;


public class PromotionPriorityInterceptor implements ValidateInterceptor
{
	@Autowired
	private ProductService productService;
	private ModelService modelService;
	/**
	 *
	 */
	private static final String DUPLICATE_PROMO = "Duplicate Promotion Code:";
	/**
	 *
	 */
	private static final String ERROR_SAME_SELLER = "One Promotion already exist with the added Priority with same seller. Please change the seller. Promotion Details:";
	/**
	 *
	 */
	private static final String ERROR_MESSAGE_PRODUCT = "One Promotion already exist with the added Priority. Promotion Details:";

	private static final Logger LOG = Logger.getLogger(PromotionPriorityInterceptor.class);
	private static final String MODIFY_MESSAGE = "promotion.priorityIntercepter.modifyData.message";
	private static final String ERROR_MESSAGE = "promotion.priority.product.sameSeller.errormessage";
	private static final String PRODUCT_ERROR_MESSAGE = "promotion.priority.product.errormessage";

	@Autowired
	private MplPromotionHelper mplPromotionHelper;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private SellerBasedPromotionService sellerBasedPromotionService;

	public ModelService getModelService()
	{
		return modelService;
	}

	@Autowired
	private CategoryService categoryService;

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

		final boolean errorflag = checkDescriptionData(object);

		final boolean bundlelinknameflag = checkbundlelinknameData(object); //added for TPR-1325 link name character


		//TPR-7408 starts here
		final String costcentreflag = configurationService.getConfiguration().getString("promotion.coupon.costcentre.flag");


		if (StringUtils.isNotEmpty(costcentreflag) && StringUtils.equalsIgnoreCase(costcentreflag, "false"))
		{
			final boolean checkTotalCostCentrePercentage = checkCostCentreData(object);

			if (!checkTotalCostCentrePercentage)
			{
				throw new InterceptorException(Localization.getLocalizedString("promotion.costCentre.attributionPercentage.total"));
			}

		}

		//TPR-7408 ends here

		if (!errorflag)
		{
			//final String errorMsg = Localization.getLocalizedString(MarketplacecommerceservicesConstants.PROMO_ERROR_MESSAGE);
			//interceptor exeption is thrown(Message : Cannot exceed 25 characters).
			//throw new InterceptorException(errorMsg);

			throw new InterceptorException(Localization.getLocalizedString("promotion.title.length.count")
					+ MarketplacecommerceservicesConstants.SINGLE_SPACE + getPromotionTitleLength());
		}

		//added for TPR-1325 link name character
		if (!bundlelinknameflag)
		{
			throw new InterceptorException(Localization.getLocalizedString("promotion.bundlepromolinktext.length.count")
					+ MarketplacecommerceservicesConstants.SINGLE_SPACE + getPromotionbundlelinknameLength());
		}


		String errorMsg = null;

		if (object instanceof ProductPromotionModel)
		{
			//@Description :To check if an Enabled Promotions exists with the Product and same priority
			final ProductPromotionModel promotion = (ProductPromotionModel) object;
			if (null != promotion.getIsBulk() && promotion.getIsBulk().booleanValue()
					&& StringUtils.isEmpty(promotion.getImmutableKeyHash())) //TPR-4065,if promotion is getting created through bulk load
			{
				final List<AbstractPromotionModel> promoList = sellerBasedPromotionService.fetchPromotionDetails(promotion.getCode());
				if (CollectionUtils.isNotEmpty(promoList))
				{
					errorMsg = DUPLICATE_PROMO;
					throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
							+ MarketplacecommerceservicesConstants.PROMOCODE + promotion.getCode()
							+ MarketplacecommerceservicesConstants.SINGLE_SPACE + MarketplacecommerceservicesConstants.PROMOPRIORITY
							+ promotion.getPriority());
				}
				else
				{
					promotion.setIsBulk(Boolean.FALSE);
					//	modelService.save(promotion);
				}
			}
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
								List<AbstractPromotionRestrictionModel> promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>();
								List<AbstractPromotionRestrictionModel> singleProdRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>();

								if (CollectionUtils.isNotEmpty(promotion.getRestrictions()))
								{
									promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
											promotion.getRestrictions());
								}
								if (CollectionUtils.isNotEmpty(promotion.getRestrictions()))
								{
									promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
											promotion.getRestrictions());
									singleProdRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
											promotion.getRestrictions());
								}
								//	String errorMsg = null;

								if (!isSellerRestrExistsForModel(promotionRestrictionList)
										|| !isSellerRestrExistsForModel(singleProdRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
									if (errorMsg.equalsIgnoreCase(PRODUCT_ERROR_MESSAGE))
									{
										errorMsg = ERROR_MESSAGE_PRODUCT;
									}
								}
								else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
									if (errorMsg.equalsIgnoreCase(ERROR_MESSAGE))
									{
										errorMsg = ERROR_SAME_SELLER;
									}
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
										//	String errorMsg = null;

										if (!isSellerRestrExistsForModel(promotionRestrictionList)
												|| !isSellerRestrExistsForModel(singleProdRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
											if (errorMsg.equalsIgnoreCase(PRODUCT_ERROR_MESSAGE))
											{
												errorMsg = ERROR_MESSAGE_PRODUCT;
											}
										}
										else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
											if (errorMsg.equalsIgnoreCase(ERROR_MESSAGE))
											{
												errorMsg = ERROR_SAME_SELLER;
											}
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
								//	String errorMsg = null;

								if (!isSellerRestrExistsForModel(promotionRestrictionList)
										|| !isSellerRestrExistsForModel(singleProdRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
									if (errorMsg.equalsIgnoreCase(PRODUCT_ERROR_MESSAGE))
									{
										errorMsg = ERROR_MESSAGE_PRODUCT;
									}
								}
								else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
								{
									errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
									if (errorMsg.equalsIgnoreCase(ERROR_MESSAGE))
									{
										errorMsg = ERROR_SAME_SELLER;
									}
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
					//					final List<ProductModel> productData = productService.getProductsForCategory(category);
					//					if (null != productData && !productData.isEmpty())
					//					{
					//						for (final ProductModel promoProduct : productData)
					//						{
					//							if (null != promoProduct.getPromotions() && !promoProduct.getPromotions().isEmpty())
					//							{
					//								for (final ProductPromotionModel promo : promoProduct.getPromotions())
					//								{
					//									if (promo.getCode().equalsIgnoreCase(promotion.getCode())
					//											&& promo.getPromotionType().equals(promotion.getPromotionType()))
					//									{
					//										LOG.debug(Localization.getLocalizedString(MODIFY_MESSAGE));
					//										continue;
					//									}
					//
					//									if (promo.getEnabled().booleanValue() && promo.getPriority().equals(promotion.getPriority()))
					//									{
					//										final List<AbstractPromotionRestrictionModel> promotionRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
					//												promotion.getRestrictions());
					//										final List<AbstractPromotionRestrictionModel> singleProdRestrictionList = new ArrayList<AbstractPromotionRestrictionModel>(
					//												promo.getRestrictions());
					//										//	String errorMsg = null;
					//
					//										if (!isSellerRestrExistsForModel(promotionRestrictionList)
					//												|| !isSellerRestrExistsForModel(singleProdRestrictionList))
					//										{
					//											errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
					//											if (errorMsg.equalsIgnoreCase(PRODUCT_ERROR_MESSAGE))
					//											{
					//												errorMsg = ERROR_MESSAGE_PRODUCT;
					//											}
					//										}
					//										else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
					//										{
					//											errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
					//											if (errorMsg.equalsIgnoreCase(ERROR_MESSAGE))
					//											{
					//												errorMsg = ERROR_SAME_SELLER;
					//											}
					//										}
					//										else
					//										{
					//											break;
					//										}
					//										throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
					//												+ MarketplacecommerceservicesConstants.PROMOCODE + promo.getCode()
					//												+ MarketplacecommerceservicesConstants.SINGLE_SPACE
					//												+ MarketplacecommerceservicesConstants.PROMOPRODUCT + promoProduct.getCode() + "("
					//												+ promoProduct.getName() + ")" + MarketplacecommerceservicesConstants.SINGLE_SPACE
					//												+ MarketplacecommerceservicesConstants.PRESENT_CATEGORY + category.getName()
					//												+ MarketplacecommerceservicesConstants.SINGLE_SPACE
					//												+ MarketplacecommerceservicesConstants.PROMOPRIORITY + promo.getPriority());
					//									}
					//								}
					//							}
					//						}
					//
					//					}
				}

				final List<CategoryModel> categoryList = new ArrayList<CategoryModel>(promotion.getCategories());
				if (CollectionUtils.isNotEmpty(categoryList))
				{
					//TISPRO-352 : Fix
					final List<ProductModel> productData = fetchProductList(categoryList);//Car-158
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
										//String errorMsg = null;

										if (!isSellerRestrExistsForModel(promotionRestrictionList)
												|| !isSellerRestrExistsForModel(singleProdRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
											if (errorMsg.equalsIgnoreCase(PRODUCT_ERROR_MESSAGE))
											{
												errorMsg = ERROR_MESSAGE_PRODUCT;
											}
										}
										else if (checkIfSetforSameSeller(singleProdRestrictionList, promotionRestrictionList))
										{
											errorMsg = Localization.getLocalizedString(ERROR_MESSAGE);
											if (errorMsg.equalsIgnoreCase(ERROR_MESSAGE))
											{
												errorMsg = ERROR_SAME_SELLER;
											}
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
												/* + MarketplacecommerceservicesConstants.PRESENT_CATEGORY + category.getName() */
												+ MarketplacecommerceservicesConstants.SINGLE_SPACE
												+ MarketplacecommerceservicesConstants.PROMOPRIORITY + promo.getPriority());
									}
								}
							}
						}

					}
				}





			}

			// Code Change for TISPRD-2637   commenting below section of code to remove staged product catalog from system
			/*
			 * if (promotion instanceof BuyXItemsofproductAgetproductBforfreeModel || promotion instanceof
			 * BuyAandBgetCModel || promotion instanceof BuyABFreePrecentageDiscountModel) { LOG.debug(
			 * "Check if Free bie Data Version"); final boolean isValid = validateFreebieData(promotion); if (!isValid) {
			 * throw new InterceptorException("Free Product Details Must be of Online Version"); } }
			 */

		}
		else if (object instanceof OrderPromotionModel)
		{
			final OrderPromotionModel promotion = (OrderPromotionModel) object;
			populatePromotionGroup(promotion);
			final String promoCode = checkCartPromoPriority(promotion);
			if (StringUtils.isNotEmpty(promoCode))
			{
				errorMsg = Localization.getLocalizedString(PRODUCT_ERROR_MESSAGE);
				throw new InterceptorException(errorMsg + MarketplacecommerceservicesConstants.SINGLE_SPACE
						+ MarketplacecommerceservicesConstants.PROMOCODE + promoCode
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE + MarketplacecommerceservicesConstants.PROMOPRIORITY
						+ promotion.getPriority());
			}
		}
	}


	/* TPR-7408 starts here */
	/**
	 * @param object
	 * @return boolean
	 */
	private boolean checkCostCentreData(final Object object)
	{
		if (object instanceof AbstractPromotionModel)
		{
			//final Integer titleLength = getPromotionTitleLength();
			final AbstractPromotionModel promo = (AbstractPromotionModel) object;

			final Double promoCostCentreOnePercentageValue = promo.getPromoCostCentreOnePercentage();
			final Double promoCostCentreTwoPercentageValue = promo.getPromoCostCentreTwoPercentage();
			final Double promoCostCentreThreePercentageValue = promo.getPromoCostCentreThreePercentage();

			if (promoCostCentreOnePercentageValue == null || promoCostCentreTwoPercentageValue == null
					|| promoCostCentreThreePercentageValue == null)
			{
				return false;
			}
			else
			{
				final double percentageTotal = promoCostCentreOnePercentageValue.doubleValue()
						+ promoCostCentreTwoPercentageValue.doubleValue() + promoCostCentreThreePercentageValue.doubleValue();
				if (percentageTotal != 100.0D)
				{
					return false;
				}
			}
		}
		return true;
	}

	/* TPR-7408 ends here */



	/**
	 * @param object
	 * @return boolean
	 */
	private boolean checkDescriptionData(final Object object)
	{
		if (object instanceof AbstractPromotionModel)
		{
			final Integer titleLength = getPromotionTitleLength();
			final AbstractPromotionModel promo = (AbstractPromotionModel) object;

			if (StringUtils.isNotEmpty(promo.getTitle()) && promo.getTitle().trim().length() > titleLength.intValue())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param categories
	 * @return productList
	 */
	private List<ProductModel> fetchProductList(final List<CategoryModel> categories)
	{
		final List<CategoryModel> categoryList = getAllCategories(categories);
		final List<ProductModel> productList = new ArrayList<ProductModel>();
		if (CollectionUtils.isNotEmpty(categoryList))
		{
			LOG.debug("Populating eligible products in List" + "Category List:" + categoryList);
			for (final CategoryModel catModel : categoryList)
			{
				if (CollectionUtils.isNotEmpty(catModel.getProducts()))
				{
					productList.addAll(catModel.getProducts());
				}
			}

		}
		return productList;
	}

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
	 * For TPR-1325
	 *
	 * @param object
	 * @return boolean
	 */
	private boolean checkbundlelinknameData(final Object object)
	{
		if (object instanceof MplProductSteppedMultiBuyPromotionModel)
		{
			final Integer nameLength = getPromotionbundlelinknameLength();
			final MplProductSteppedMultiBuyPromotionModel promo = (MplProductSteppedMultiBuyPromotionModel) object;
			//LOG.debug("Checking Name For Bundle Promotion Link::" + promo.getBundlepromolinktext().trim().length()); TISUATSE-126 fixed
			if (StringUtils.isNotEmpty(promo.getBundlepromolinktext())
					&& promo.getBundlepromolinktext().trim().length() > nameLength.intValue())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * The Method Returns the Max Possible Promotion Title Length
	 *
	 * @return titleLength
	 */
	private Integer getPromotionTitleLength()
	{
		Integer titleLength = Integer.valueOf(0);
		final String length = configurationService.getConfiguration().getString("promotion.title.length", "76");
		try
		{
			titleLength = Integer.valueOf(length);
		}
		catch (final NumberFormatException exception)
		{
			titleLength = Integer.valueOf(76);
		}
		return titleLength;
	}

	/**
	 * The Method Returns the Max Length of Name For Bundle Promotion Link for TPR-1325
	 *
	 * @return nameLength
	 */
	private Integer getPromotionbundlelinknameLength()
	{
		Integer nameLength = Integer.valueOf(0);
		final String length = configurationService.getConfiguration().getString("promotion.bundlepromolinktext.length", "185");
		try
		{
			nameLength = Integer.valueOf(length);
		}
		catch (final NumberFormatException exception)
		{
			nameLength = Integer.valueOf(185);
		}
		return nameLength;
	}

	/**
	 * Code Change for TISPRD-2637
	 *
	 *
	 * The method validates the freebie data
	 *
	 * @param promotion
	 * @return isValid
	 */
	// Sonar fix
	/*
	 * private boolean validateFreebieData(final ProductPromotionModel promotion) { boolean isValid = true; if (promotion
	 * instanceof BuyXItemsofproductAgetproductBforfreeModel) { final BuyXItemsofproductAgetproductBforfreeModel oModel =
	 * (BuyXItemsofproductAgetproductBforfreeModel) promotion; if (CollectionUtils.isNotEmpty(oModel.getGiftProducts()))
	 * { isValid = checkCatalogVersion(oModel.getGiftProducts()); } } else if (promotion instanceof BuyAandBgetCModel) {
	 * final BuyAandBgetCModel oModel = (BuyAandBgetCModel) promotion; if
	 * (CollectionUtils.isNotEmpty(oModel.getGiftProducts())) { isValid = checkCatalogVersion(oModel.getGiftProducts());
	 * } } else if (promotion instanceof BuyABFreePrecentageDiscountModel) { final BuyABFreePrecentageDiscountModel
	 * oModel = (BuyABFreePrecentageDiscountModel) promotion; if (CollectionUtils.isNotEmpty(oModel.getGiftProducts())) {
	 * isValid = checkCatalogVersion(oModel.getGiftProducts()); } }
	 *
	 * return isValid;
	 *
	 * }
	 */


	/**
	 * Code Change for TISPRD-2637
	 *
	 *
	 * The method validates the freebie data
	 *
	 * @param giftProducts
	 * @return isValid
	 */
	//	private boolean checkCatalogVersion(final Collection<ProductModel> giftProducts)
	//	{
	//		boolean isValid = true;
	//		final String compareCode = getDefaultPromotionsManager().catalogData().getVersion();
	//		for (final ProductModel oModel : giftProducts)
	//		{
	//			if (null != oModel.getCatalogVersion() && null != oModel.getCatalogVersion().getVersion()
	//					&& StringUtils.isNotEmpty(compareCode)
	//					&& !StringUtils.equalsIgnoreCase(oModel.getCatalogVersion().getVersion(), compareCode))
	//			{
	//				isValid = false;
	//				break;
	//			}
	//		}
	//		return isValid;
	//	}

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
