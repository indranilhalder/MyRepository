/**
 *
 */
package com.tisl.mpl.jalo;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionException;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplStockService;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.util.ValueComparator;


/**
 * @author TCS
 *
 */
public class DefaultPromotionManager extends PromotionsManager
{
	private final static Logger LOG = Logger.getLogger(DefaultPromotionManager.class.getName());
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CatalogVersionService catalogVersionService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private ProductService productService;
	@Autowired
	private MplStockService mplStockService;
	@Autowired
	private CartService cartService;
	@Autowired
	private MplDeliveryCostService deliveryCostService;


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

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
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
	 * @return the mplStockService
	 */
	public MplStockService getMplStockService()
	{
		return mplStockService;
	}

	/**
	 * @param mplStockService
	 *           the mplStockService to set
	 */
	public void setMplStockService(final MplStockService mplStockService)
	{
		this.mplStockService = mplStockService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the deliveryCostService
	 */
	public MplDeliveryCostService getDeliveryCostService()
	{
		return deliveryCostService;
	}

	/**
	 * @param deliveryCostService
	 *           the deliveryCostService to set
	 */
	public void setDeliveryCostService(final MplDeliveryCostService deliveryCostService)
	{
		this.deliveryCostService = deliveryCostService;
	}


	/**
	 * @Description: Checks Restriction Brand names with Product Brand name
	 * @param promotionManufacturerList
	 * @param product
	 * @return boolean
	 */
	public boolean brandDataCheck(final List<String> promotionManufacturerList, final Product product)
	{
		boolean applyPromotion = false;
		try
		{
			final ProductModel productModel = productService.getProductForCode(product.getCode());
			if (!productModel.getBrands().isEmpty())
			{
				final BrandModel brand = ((List<BrandModel>) productModel.getBrands()).get(0);

				if (null != promotionManufacturerList && !promotionManufacturerList.isEmpty() && null != brand)
				{
					applyPromotion = isEligibleManufacturer(promotionManufacturerList, brand);
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
		return applyPromotion;
	}


	/**
	 * Checks for Valid Manufacturer : For Sonar Fix
	 *
	 * @param promotionManufacturerList
	 * @param brand
	 * @return isValid
	 */
	private boolean isEligibleManufacturer(final List<String> promotionManufacturerList, final BrandModel brand)
	{
		boolean isValid = false;

		for (final String promoManufacturer : promotionManufacturerList)
		{
			//if (promoManufacturer.toLowerCase().equalsIgnoreCase(brand.getName().toLowerCase())) Sonar fix
			if (promoManufacturer.equalsIgnoreCase(brand.getName()))
			{
				isValid = true;
				break;
			}
		}

		return isValid;
	}

	/**
	 * @Description: Returns true if Cart Entry Products sum amount is greater or equal to than minimum brand value
	 * @param manufactureList
	 * @param validProductUssidMap
	 * @param minimumManufactureAmount
	 * @return boolean
	 */
	public boolean minimumBrandAmountCheck(final List<String> manufactureList,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final double minimumManufactureAmount)
	{
		if (minimumManufactureAmount > 0)
		{
			double totalEligibleEntryAmount = 0.0D;
			//for (final AbstractOrderEntry entry : order.getEntries())
			for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
			{
				final AbstractOrderEntry entry = mapEntry.getValue();
				final Product product = entry.getProduct();
				if (brandDataCheck(manufactureList, product))
				{
					totalEligibleEntryAmount += entry.getTotalPrice().doubleValue();
				}

			}
			if (totalEligibleEntryAmount >= minimumManufactureAmount)
			{
				LOG.debug(Localization.getLocalizedString("promotion.cart.greaterThanBrandValue"));
				return true;
			}

			LOG.debug(Localization.getLocalizedString("promotion.cart.lessThanBrandValue"));
			return false;

		}

		LOG.debug(Localization.getLocalizedString("promotion.cart.brandValueValudate.notRequired"));
		return true;
	}


	/**
	 * @param promotionProductList
	 * @param promotionSecondProductList
	 * @param categorylist
	 * @param bCategorylist
	 * @param excludedProductList
	 * @param excludeManufactureList
	 * @param order
	 * @param minimumCategoryValue
	 * @param ctx
	 * @return boolean
	 */
	public boolean minimumCategoryValueForABCheck(final List<Product> promotionProductList,
			final List<Product> promotionSecondProductList, final List<Category> categorylist, final List<Category> bCategorylist,
			final List<Product> excludedProductList, final List<String> excludeManufactureList, final AbstractOrder order,
			final double minimumCategoryValue, final SessionContext ctx)
	{
		try
		{
			final List<Category> totalCategorylist = new ArrayList<Category>();

			double totalEligibleEntryAmount = 0.0D;

			if (!categorylist.isEmpty() && promotionProductList.isEmpty())
			{
				totalCategorylist.addAll(categorylist);
			}
			if (!bCategorylist.isEmpty() && promotionSecondProductList.isEmpty())
			{
				totalCategorylist.addAll(bCategorylist);
			}

			if (!totalCategorylist.isEmpty() && minimumCategoryValue > 0)
			{
				for (final AbstractOrderEntry entry : order.getEntries())
				{
					final Product product = entry.getProduct();
					//excluded product check
					if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
							|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList))
					{
						continue;
					}
					List<String> productCategoryList = null;
					productCategoryList = getcategoryList(product, ctx);
					if (null != productCategoryList && !productCategoryList.isEmpty()
							&& GenericUtilityMethods.productExistsIncat(totalCategorylist, productCategoryList))
					{
						totalEligibleEntryAmount += entry.getTotalPrice().doubleValue();
					}
				}

				if (totalEligibleEntryAmount >= minimumCategoryValue)
				{
					LOG.debug(Localization.getLocalizedString("promotion.categoryVal.more"));
					return true;
				}
				LOG.debug(Localization.getLocalizedString("promotion.categoryVal.less"));
				return false;
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

		LOG.debug(Localization.getLocalizedString("promotion.categoryVal.validate.notRequired"));
		return true;

	}

	/**
	 * @Description: Sorts Product List by Base Price
	 * @param cart
	 * @param validProductList
	 * @return Map<Product, Integer>
	 */
	public Map<Product, Integer> getProductListSortByBasePrice(final AbstractOrder cart,
			final Map<Product, Integer> validProductList)
	{
		final Map<Product, Double> unSortedMap = new HashMap<Product, Double>();
		for (final AbstractOrderEntry entry : cart.getEntries())
		{
			final Product product = entry.getProduct();
			if (null != validProductList.get(product))
			{
				unSortedMap.put(product, entry.getBasePrice());
			}
		}

		final ValueComparator bvc = new ValueComparator(unSortedMap);
		final TreeMap<Product, Double> sortedMap = new TreeMap<Product, Double>(bvc);
		sortedMap.putAll(unSortedMap);

		final Map<Product, Integer> finalSortedMap = new HashMap<Product, Integer>();

		for (final Map.Entry<Product, Double> entry : sortedMap.entrySet())
		{
			final Product product = entry.getKey();
			finalSortedMap.put(product, validProductList.get(product));
		}
		return finalSortedMap;

	}

	public boolean checkChannelData(final List<EnumerationValue> listOfChannel, final AbstractOrder cart)
	{
		Object cartChennal = null;
		try
		{
			if (cart instanceof de.hybris.platform.jalo.order.Cart)
			{
				cartChennal = cart.getAttribute(CartModel.CHANNEL);
			}
			else if (cart instanceof Order)
			{
				cartChennal = cart.getAttribute(OrderModel.SALESAPPLICATION);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
		}
		return checkChannelData(listOfChannel, cartChennal == null ? null : (EnumerationValue) cartChennal);
	}

	/**
	 * @Description: Validates Channel
	 * @param listOfChannel
	 * @param channel
	 * @return flag
	 */
	public boolean checkChannelData(final List<EnumerationValue> listOfChannel, final EnumerationValue channel)
	{
		boolean flag = true; //TODO: When Web Journey is ready flag must be by default false
		boolean dataFlag = false;
		if (null != channel && null != listOfChannel && !listOfChannel.isEmpty())
		{
			for (final EnumerationValue enumVal : listOfChannel)
			{
				if (enumVal.getCode().equalsIgnoreCase(channel.getCode()))
				{
					dataFlag = true;
					break;
				}
			}
			if (dataFlag)
			{
				flag = true;
			}
			else
			{
				flag = false;
			}

		}
		// YTODO Auto-generated method stub
		return flag;
	}



	protected PromotionUtilityPOJO getPromotionUtilityPOJO()
	{
		return Registry.getApplicationContext().getBean("promotionUtilityPOJO", PromotionUtilityPOJO.class);
	}

	/**
	 * @Description: Count of Eligible Products
	 * @param ctx
	 * @param promoContext
	 * @param promo
	 * @param excludedProductList
	 * @param excludeManufactureList
	 * @return noOfProducts
	 */
	public int noOfEligibleProducts(final SessionContext ctx, final PromotionEvaluationContext promoContext,
			final AbstractPromotion promo, final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		int noOfProducts = 0;
		int realQuantity = 0;
		final AbstractOrder cart = promoContext.getOrder();
		List<Product> promotionProductList = null;
		List<Category> promotionCategoryList = null;

		if (promo instanceof BuyXItemsofproductAgetproductBforfree)
		{
			promotionProductList = new ArrayList<>(((BuyXItemsofproductAgetproductBforfree) promo).getProducts());
			promotionCategoryList = new ArrayList<>(((BuyXItemsofproductAgetproductBforfree) promo).getCategories());
		}
		else if (promo instanceof BuyAPercentageDiscount)
		{
			promotionProductList = new ArrayList<>(((BuyAPercentageDiscount) promo).getProducts());
			promotionCategoryList = new ArrayList<>(((BuyAPercentageDiscount) promo).getCategories());
		}
		else if (promo instanceof BuyABFreePrecentageDiscount)
		{
			promotionProductList = new ArrayList<>(((BuyABFreePrecentageDiscount) promo).getProducts());
			promotionCategoryList = new ArrayList<>(((BuyABFreePrecentageDiscount) promo).getCategories());
		}
		else if (promo instanceof BuyAGetPromotionOnShippingCharges)
		{
			promotionProductList = new ArrayList<>(((BuyAGetPromotionOnShippingCharges) promo).getProducts());
			promotionCategoryList = new ArrayList<>(((BuyAGetPromotionOnShippingCharges) promo).getCategories());
		}
		for (final AbstractOrderEntry entry : cart.getEntries())
		{
			boolean applyPromotion = false;
			final Product product = entry.getProduct();
			//			if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
			//					|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList))
			//			{
			//				continue;
			//			}
			if (!promotionProductList.isEmpty())
			{
				if (promotionProductList.contains(product))
				{
					applyPromotion = true;
				}
			}
			else if (!promotionCategoryList.isEmpty())
			{
				final List<String> productCategoryList = getcategoryList(product, ctx);
				applyPromotion = GenericUtilityMethods.productExistsIncat(promotionCategoryList, productCategoryList);
			}

			if (applyPromotion)
			{
				realQuantity += entry.getQuantity().intValue();
			}
		}
		if (realQuantity > 0)
		{
			noOfProducts = realQuantity;
		}
		return noOfProducts;
	}

	/**
	 * @Description: Category Data corresponding to a Product
	 * @param product
	 * @param paramSessionContext
	 * @return productCategoryList
	 */

	public List<String> getcategoryList(final Product product, final SessionContext paramSessionContext)
	{
		List<String> productCategoryList = null;
		List<Category> productCategoryData = null;
		List<CategoryModel> superCategoryData = null;
		try
		{
			productCategoryData = (List<Category>) product.getAttribute(paramSessionContext,
					MarketplacecommerceservicesConstants.PROMO_CATEGORY);
			if (null != productCategoryData && !productCategoryData.isEmpty())
			{
				productCategoryList = new ArrayList<String>();
				for (final Category category : productCategoryData)
				{
					if (null != category && null != category.getCode(paramSessionContext))
					{
						productCategoryList.add(category.getCode(paramSessionContext));
						final CategoryModel oModel = categoryService.getCategoryForCode(category.getCode(paramSessionContext));
						superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(oModel));
						if (!superCategoryData.isEmpty())
						{
							for (final CategoryModel categoryModel : superCategoryData)
							{
								productCategoryList.add(categoryModel.getCode());
							}
						}
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
		return productCategoryList;
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param totalAdjustment
	 */
	public CustomPromotionOrderAdjustTotalAction createCustomPromotionOrderAdjustTotalAction(final SessionContext ctx,
			final double totalAdjustment)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(totalAdjustment));
		return createCustomPromotionOrderAdjustTotalAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion aportioned Promotion Price
	 * @param ctx
	 * @param attributeValues
	 */
	private CustomPromotionOrderAdjustTotalAction createCustomPromotionOrderAdjustTotalAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CustomPromotionOrderAdjustTotalAction");
			return ((CustomPromotionOrderAdjustTotalAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CustomPromotionOrderAdjustTotalAction : " + e.getMessage(), 0);
		}
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomPromotionOrderEntryAdjustAction
	 */

	public CustomPromotionOrderEntryAdjustAction createCustomPromotionOrderEntryAdjustAction(final SessionContext ctx,
			final AbstractOrderEntry entry, final long quantity, final double adjustment)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(adjustment));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_PRODUCT, entry.getProduct(ctx));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_NUMBER, entry.getEntryNumber());
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_QUANTITY, Long.valueOf(quantity));
		return createCustomPromotionOrderEntryAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomPromotionOrderEntryAdjustAction
	 */

	public CustomPromotionOrderEntryAdjustAction createCustomPromotionOrderEntryAdjustAction(final SessionContext ctx,
			final AbstractOrderEntry entry, final double adjustment)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(adjustment));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_PRODUCT, entry.getProduct(ctx));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_NUMBER, entry.getEntryNumber());
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_QUANTITY, entry.getQuantity(ctx));
		return createCustomPromotionOrderEntryAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param attributeValues
	 * @return CustomPromotionOrderEntryAdjustAction
	 */

	public CustomPromotionOrderEntryAdjustAction createCustomPromotionOrderEntryAdjustAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CustomPromotionOrderEntryAdjustAction");
			return ((CustomPromotionOrderEntryAdjustAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CustomPromotionOrderEntryAdjustAction : " + e.getMessage(), 0);
		}
	}


	/**
	 * @Description: Category Data corresponding to a Product for Promotion Intercepter
	 * @param product
	 * @return productCategoryData
	 */

	public List<CategoryModel> getcategoryData(final ProductModel productdata)
	{
		List<CategoryModel> productCategoryData = null;
		List<CategoryModel> superCategoryData = null;
		final CatalogVersionModel oCatalogVersionModel = catalogData();

		if (null != productdata && null != productdata.getSupercategories() && null != oCatalogVersionModel)
		{
			superCategoryData = new ArrayList<CategoryModel>(productdata.getSupercategories());
			if (!superCategoryData.isEmpty())
			{
				productCategoryData = new ArrayList<CategoryModel>();
				for (final CategoryModel category : superCategoryData)
				{
					if (null != category && null != category.getCode())
					{
						final CategoryModel oModel = categoryService.getCategoryForCode(oCatalogVersionModel, category.getCode());
						productCategoryData.add(oModel);
						superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(oModel));
						if (!superCategoryData.isEmpty())
						{
							for (final CategoryModel categoryModel : superCategoryData)
							{
								productCategoryData.add(categoryModel);
							}
						}
					}
				}
			}
		}
		return productCategoryData;
	}

	/**
	 * @Description: Category Data corresponding to a Product for Promotion Intercepter
	 * @param product
	 * @return productCategoryData
	 */

	public List<CategoryModel> getPrimarycategoryData(final ProductModel product)
	{
		List<CategoryModel> productCategoryData = null;
		List<CategoryModel> superCategoryData = null;
		final CatalogVersionModel oCatalogVersionModel = catalogData();

		if (null != product && null != product.getSupercategories() && null != oCatalogVersionModel)
		{
			superCategoryData = new ArrayList<CategoryModel>(product.getSupercategories());
			if (!superCategoryData.isEmpty())
			{
				productCategoryData = new ArrayList<CategoryModel>();
				for (final CategoryModel category : superCategoryData)
				{
					//TISUAT-4621
					final String primaryCat = configurationService.getConfiguration().getString("decorator.primary", "");
					if (null != category && null != category.getCode() && category.getCode().indexOf(primaryCat) > -1)
					{
						final CategoryModel oModel = categoryService.getCategoryForCode(oCatalogVersionModel, category.getCode());
						productCategoryData.add(oModel);
						superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(oModel));
						if (!superCategoryData.isEmpty())
						{
							for (final CategoryModel categoryModel : superCategoryData)
							{
								productCategoryData.add(categoryModel);
							}
						}
					}
				}
			}
		}
		return productCategoryData;
	}

	/**
	 * @Description: Returns Catalog Data
	 * @return catalogVersionModel
	 */
	public CatalogVersionModel catalogData()
	{
		final String catalogId = configurationService.getConfiguration().getString("cronjob.promotion.catelog", "");
		final String catalogVersionName = configurationService.getConfiguration().getString("cronjob.promotion.catalogVersionName",
				"");
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogId, catalogVersionName);
		return catalogVersionModel;
	}

	/**
	 * @Description: Verify Seller Data
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param entry
	 * @return boolean
	 */
	public boolean checkSellerData(final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList, final AbstractOrderEntry entry)
	{
		boolean flag = false;
		String ussid = MarketplacecommerceservicesConstants.EMPTY;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
			{
				ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
				final CatalogVersionModel oModel = catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid, oModel);
				flag = GenericUtilityMethods.checkSellerData(restrictionList, productSellerData);
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
		return flag;
	}


	/**
	 * @Description: Verify Seller Data
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param entry
	 * @return boolean
	 */
	public boolean checkSellerBOGOData(final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList, final AbstractOrderEntry entry)
	{
		boolean flag = false;
		String ussid = MarketplacecommerceservicesConstants.EMPTY;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
			{
				ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
				final CatalogVersionModel oModel = catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid, oModel);
				flag = GenericUtilityMethods.checkBOGOData(restrictionList, productSellerData);
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
		return flag;
	}

	protected SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return Registry.getApplicationContext().getBean("sellerBasedPromotionService", SellerBasedPromotionService.class);
	}


	/**
	 * @Description: Returns Seller ID
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param entry
	 * @return sellerID
	 */
	public String getSellerID(final SessionContext paramSessionContext, final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrderEntry entry)
	{
		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
		String ussid = MarketplacecommerceservicesConstants.EMPTY;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
			{
				ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
				final CatalogVersionModel oModel = catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid, oModel);
				for (final SellerInformationModel seller : productSellerData)
				{
					sellerID = seller.getSellerID();
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
		return sellerID;
	}

	/**
	 * @Description: Matches Seller Information for Freebie Promotion
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param entry
	 * @param sellerID
	 * @return
	 */
	public boolean matchSellerID(final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList, final AbstractOrderEntry entry, final String sellerID)
	{
		boolean flag = false;
		String productSellerID = MarketplacecommerceservicesConstants.EMPTY;
		String ussid = MarketplacecommerceservicesConstants.EMPTY;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
			{
				ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
				final CatalogVersionModel oModel = catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid, oModel);
				for (final SellerInformationModel seller : productSellerData)
				{
					productSellerID = seller.getSellerID();
					if (productSellerID.equalsIgnoreCase(sellerID))
					{
						flag = true;
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
		return flag;
	}

	/**
	 * @Description: Checks Gift Products belongs to a specific Seller ID
	 * @param productList
	 * @param sellerID
	 * @return Map<String, Product> giftProductDetails
	 */
	public Map<String, Product> getGiftProducts(final List<Product> productList, final String sellerID)
	{
		final HashMap<String, Product> giftProductDetails = new HashMap<String, Product>();
		List<SellerInformationModel> giftProductSellerData = null;
		if (null != sellerID)
		{
			for (final Product product : productList)
			{
				final ProductModel oModel = productService.getProductForCode(catalogData(), product.getCode());
				if (null != oModel && null != oModel.getSellerInformationRelator())
				{
					giftProductSellerData = new ArrayList<SellerInformationModel>(oModel.getSellerInformationRelator());
					if (!giftProductSellerData.isEmpty())
					{
						for (final SellerInformationModel seller : giftProductSellerData)
						{
							if (seller.getSellerID().equalsIgnoreCase(sellerID))
							{
								final List<StockLevelModel> stockData = mplStockService.getStockLevelDetail(seller.getSellerArticleSKU());
								for (final StockLevelModel stockModel : stockData)
								{
									if (stockModel.getAvailable() > 0)
									{
										giftProductDetails.put(seller.getSellerArticleSKU(), product);
									}
								}
							}
						}
					}
				}
			}

		}
		return giftProductDetails;
	}


	/**
	 * @Description: check discount amount value with product price for discount promotion
	 * @param cart
	 * @param promotionProductList
	 * @param promotionCategoryList
	 * @return
	 */
	public boolean getValidProductListForAmtDiscount(final SessionContext paramSessionContext, final AbstractOrder cart,
			final List<Product> promotionProductList, final List<Category> promotionCategoryList, final Long eligibleQuantity,
			final Double discountPrice, final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		boolean _flagCouldFireMessage = true;
		double priceForDiscount = 0.00D;
		double totalValidProductPrice = 0.00D;


		for (final AbstractOrderEntry entry : validProductUssidMap.values())
		{
			totalValidProductPrice += entry.getTotalPriceAsPrimitive(paramSessionContext);
		}

		final Iterator iter = validProductUssidMap.entrySet().iterator();
		while (iter.hasNext())
		{
			final Map.Entry mapEntry = (Map.Entry) iter.next();
			final AbstractOrderEntry entry = (AbstractOrderEntry) mapEntry.getValue();

			if (!promotionProductList.isEmpty())
			{
				priceForDiscount = entry.getBasePrice(paramSessionContext).doubleValue();
			}
			else if (promotionProductList.isEmpty() && !promotionCategoryList.isEmpty())
			{
				if (eligibleQuantity.intValue() > 1)
				{
					priceForDiscount = totalValidProductPrice;
				}
				else
				{
					priceForDiscount = entry.getBasePrice(paramSessionContext).doubleValue();
				}

			}

			if (discountPrice.doubleValue() != 0.00
					&& (priceForDiscount * eligibleQuantity.intValue()) < discountPrice.doubleValue())
			{
				iter.remove();
				_flagCouldFireMessage = false;
			}

		}
		return _flagCouldFireMessage;
	}

	/**
	 * @Description: This method is for getting qualifying count for AB promotions
	 * @param eligibleProductList
	 * @param totalCountFactor
	 * @return Map<Product, Integer>
	 */
	public Map<String, Integer> getQualifyingCountForABPromotion(final List<String> eligibleProductList, final int totalCountFactor)
	{
		final Map<String, Integer> itemLevelQC = new HashMap<String, Integer>();
		for (final String ussid : eligibleProductList)
		{
			itemLevelQC.put(ussid, Integer.valueOf(totalCountFactor)/*
																					   * Integer.valueOf(Collections.frequency(
																					   * eligibleProductList, ussid))
																					   */);
		}
		return itemLevelQC;
	}



	/**
	 * @Description: This method is for getting qualifying count for A, BOGO, Freebie promotions
	 * @param validProductUssidMap
	 * @param sKUForFreebie
	 * @return Map<Product, Integer>
	 */
	public Map<String, List<String>> getAssociatedItemsForAorBOGOorFreebiePromotions(
			final Map<String, AbstractOrderEntry> validProductUssidMap, final String sKUForFreebie)
	{
		final Map<String, List<String>> productAssociatedItemsMap = new HashMap<String, List<String>>();
		final List<String> freebieAssociatedProductList = new ArrayList<String>();
		String validProductUssid = null;
		final List<String> validProdUssidList = new ArrayList<String>(validProductUssidMap.keySet());

		if (validProdUssidList.size() == 1) //for Buy A get B free, Buy A get B free and percentage off
		{
			final List<String> associatedProductList = new ArrayList<String>();
			validProductUssid = validProdUssidList.get(0);
			if (sKUForFreebie != null)
			{
				//get the free product and add that into associatedProductList
				associatedProductList.add(sKUForFreebie);
				//for freebie associated product mapping
				freebieAssociatedProductList.add(validProductUssid);
				//productAssociatedItemsMap.put(sKUForFreebie, freebieAssociatedProductList);
			}
			productAssociatedItemsMap.put(validProductUssid, associatedProductList);

		}
		else
		{
			List<String> associatedProductList = null;
			for (int i = 0; i < validProdUssidList.size(); i++)
			{
				associatedProductList = new ArrayList<String>();
				validProductUssid = validProdUssidList.get(i);
				if (i > 0)
				{
					for (int j = 0/* validProdUssidList.indexOf(validProductUssid) - i */; j < validProdUssidList.size(); j++)
					{
						final String currUssid = validProdUssidList.get(j);
						if (!validProductUssid.equalsIgnoreCase(currUssid))
						{
							associatedProductList.add(currUssid);
						}
					}

				}
				else
				{
					//need to add the final remaining elements
					associatedProductList.addAll(validProdUssidList.subList(validProdUssidList.indexOf(validProductUssid) + 1,
							validProdUssidList.size()));
				}

				if (sKUForFreebie != null)
				{
					//TODO get the free product and add that into associatedProductList
					associatedProductList.add(sKUForFreebie);
					//for freebie associated product mapping
					freebieAssociatedProductList.add(validProdUssidList.get(i));
				}
				productAssociatedItemsMap.put(validProductUssid, associatedProductList);
			}
		}

		if (!freebieAssociatedProductList.isEmpty())
		{
			productAssociatedItemsMap.put(sKUForFreebie, freebieAssociatedProductList);
		}
		return productAssociatedItemsMap;
	}

	/**
	 * @Description: Gets the USSID for Cart Promotion Free Product
	 * @param productList
	 * @return Map<String, Product> giftProductDetails
	 */
	public Map<String, Product> getCartPromoGiftProducts(final List<Product> productList)
	{
		final HashMap<String, Product> giftProductDetails = new HashMap<String, Product>();
		final List<Double> priceList = new ArrayList<Double>();
		final Map<String, PriceRowModel> mopMap = new HashMap<String, PriceRowModel>();
		List<PriceRowModel> priceInformation = new ArrayList<PriceRowModel>();
		boolean minmumValObtained = false;

		List<SellerInformationModel> giftProductSellerData = null;
		for (final Product product : productList)
		{
			final ProductModel oModel = productService.getProductForCode(catalogData(), product.getCode());
			if (null != oModel && null != oModel.getSellerInformationRelator())
			{
				giftProductSellerData = new ArrayList<SellerInformationModel>(oModel.getSellerInformationRelator());
				if (!giftProductSellerData.isEmpty())
				{
					for (final SellerInformationModel seller : giftProductSellerData)
					{
						if (null != seller.getSellerArticleSKU())
						{
							priceInformation = getSellerBasedPromotionService().fetchPriceInformation(seller.getSellerArticleSKU(),
									catalogData());
							for (final PriceRowModel price : priceInformation)
							{
								priceList.add(price.getPrice());
								mopMap.put(seller.getSellerArticleSKU(), price);
							}
						}
					}

					if (!priceList.isEmpty())
					{
						final Double minPrice = checkMinimumValue(priceList);
						for (final Map.Entry<String, PriceRowModel> entry : mopMap.entrySet())
						{
							if (entry.getValue().getPrice().doubleValue() == minPrice.doubleValue() && !minmumValObtained)
							{
								final String sellerArticleSKU = entry.getKey();
								final List<StockLevelModel> stockData = mplStockService.getStockLevelDetail(sellerArticleSKU);
								for (final StockLevelModel stockModel : stockData)
								{
									if (stockModel.getAvailable() > 0)
									{
										giftProductDetails.put(sellerArticleSKU, product);
										minmumValObtained = true;
									}
								}
							}
						}
					}
				}
			}
			minmumValObtained = false;
		}

		return giftProductDetails;
	}

	/**
	 * @Description: Returns Minimum Price
	 * @param priceList
	 */
	private Double checkMinimumValue(final List<Double> priceList)
	{
		Double minPrice = Double.valueOf(0);
		//if (null != priceList && priceList.size() > 0)
		if (CollectionUtils.isNotEmpty(priceList))
		{
			for (final Double value : priceList)
			{
				if (minPrice.doubleValue() == 0.00D)
				{
					minPrice = value;
				}

				if (minPrice.doubleValue() < value.doubleValue())
				{
					minPrice = value;
				}
			}
		}
		return minPrice;
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price BOGO
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomBOGOPromoOrderEntryAdjustAction
	 */

	public CustomBOGOPromoOrderEntryAdjustAction createCustomBOGOPromoOrderEntryAdjustAction(final SessionContext ctx,
			final AbstractOrderEntry entry, final long quantity, final double adjustment)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(adjustment));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_PRODUCT, entry.getProduct(ctx));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_NUMBER, entry.getEntryNumber());
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_QUANTITY, Long.valueOf(quantity));
		return createCustomBOGOPromoOrderEntryAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param adjustment
	 * @return CustomPromotionOrderEntryAdjustAction
	 */

	public CustomBOGOPromoOrderEntryAdjustAction createCustomBOGOPromoOrderEntryAdjustAction(final SessionContext ctx,
			final AbstractOrderEntry entry, final double adjustment)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(adjustment));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_PRODUCT, entry.getProduct(ctx));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_NUMBER, entry.getEntryNumber());
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_QUANTITY, entry.getQuantity(ctx));
		return createCustomBOGOPromoOrderEntryAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param attributeValues
	 * @return CustomPromotionOrderEntryAdjustAction
	 */

	public CustomBOGOPromoOrderEntryAdjustAction createCustomBOGOPromoOrderEntryAdjustAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CustomBOGOPromoOrderEntryAdjustAction");
			return ((CustomBOGOPromoOrderEntryAdjustAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CustomBOGOPromoOrderEntryAdjustAction : " + e.getMessage(), 0);
		}
	}

	/**
	 * @Description: This is for validating Delivery Mode Restriction
	 * @param restrictionList
	 * @param validProductListMap
	 * @return true if delivery mode restriction satisfies for A, else false and remove that element from
	 *         validProductListMap
	 */

	public boolean getDelModeRestrEvalForAPromo(final List<AbstractPromotionRestriction> restrictionList,
			final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		boolean flag = false;
		if (null == restrictionList || restrictionList.isEmpty())
		{
			flag = true;
		}
		else
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				flag = false;
				if (restriction instanceof DeliveryModePromotionRestriction)
				{
					final List<String> prodSatisfiesDelModeList = new ArrayList<String>();
					final CartModel cartModel = cartService.getSessionCart();
					final List<String> restrDeliveryModeList = new ArrayList<String>();

					for (final DeliveryMode deliveryMode : ((DeliveryModePromotionRestriction) restriction)
							.getDeliveryModeDetailsList())
					{
						restrDeliveryModeList.add(deliveryMode.getCode());
					}

					for (final AbstractOrderEntryModel entry : cartModel.getEntries())
					{
						final String validProdUSSID = entry.getSelectedUSSID();
						if (validProductUssidMap.containsKey(validProdUSSID) && entry.getMplDeliveryMode() != null)
						{
							final String selectedDeliveryMode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
							if (restrDeliveryModeList.contains(selectedDeliveryMode))
							{
								prodSatisfiesDelModeList.add(validProdUSSID);
							}
						}
					}

					if (prodSatisfiesDelModeList.size() == validProductUssidMap.size())
					{
						flag = true;
					}
					break;
				}
				else
				{
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * @Description: This is for validating Delivery Mode Restriction
	 * @param restrictionList
	 * @param validProductListMap
	 * @return true if delivery mode restriction satisfies for both A and B, else false
	 */

	public boolean getDelModeRestrEvalForABPromo(final List<AbstractPromotionRestriction> restrictionList,
			final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		boolean flag = false;
		if (null == restrictionList || restrictionList.isEmpty())
		{
			flag = true;
		}
		else
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				flag = false;
				if (restriction instanceof DeliveryModePromotionRestriction)
				{
					final List<String> prodSatisfiesDelModeList = new ArrayList<String>();
					//final List<Product> validProductListUniqueElements = new ArrayList<Product>(new HashSet<Product>(validProductList));
					final CartModel cartModel = cartService.getSessionCart();
					final List<String> restrDeliveryModeList = new ArrayList<String>();

					for (final DeliveryMode deliveryMode : ((DeliveryModePromotionRestriction) restriction)
							.getDeliveryModeDetailsList())
					{
						restrDeliveryModeList.add(deliveryMode.getCode());
					}

					for (final AbstractOrderEntryModel entry : cartModel.getEntries())
					{
						final String validProdUSSID = entry.getSelectedUSSID();
						if (validProductUssidMap.containsKey(validProdUSSID) && entry.getMplDeliveryMode() != null)
						{
							final String selectedDeliveryMode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
							if (restrDeliveryModeList.contains(selectedDeliveryMode))
							{
								prodSatisfiesDelModeList.add(validProdUSSID);
							}

						}
					}

					if (prodSatisfiesDelModeList.size() == validProductUssidMap.size())
					{
						flag = true;
					}
					break;
				}
				else
				{
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * @Description: This is for validating Delivery Mode Restriction at order level
	 * @param restrictionList
	 * @param order
	 * @return true if delivery mode restriction satisfies for all products, else false
	 */

	public boolean getDelModeRestrEvalForOrderPromo(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean flag = false;
		if (null == restrictionList || restrictionList.isEmpty())
		{
			flag = true;
		}
		else
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				flag = false;
				if (restriction instanceof DeliveryModePromotionRestriction)
				{
					final List<ProductModel> prodSatisfiesDelModeList = new ArrayList<ProductModel>();
					final CartModel cartModel = cartService.getSessionCart();
					final List<String> restrDeliveryModeList = new ArrayList<String>();

					for (final DeliveryMode deliveryMode : ((DeliveryModePromotionRestriction) restriction)
							.getDeliveryModeDetailsList())
					{
						restrDeliveryModeList.add(deliveryMode.getCode());
					}

					for (final AbstractOrderEntryModel entry : cartModel.getEntries())
					{
						if (entry.getMplDeliveryMode() != null)
						{
							final String selectedDeliveryMode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
							if (restrDeliveryModeList.contains(selectedDeliveryMode))
							{
								prodSatisfiesDelModeList.add(entry.getProduct());
							}
						}
					}

					if (prodSatisfiesDelModeList.size() == cartModel.getEntries().size())
					{
						flag = true;
					}
					break;
				}
				else
				{
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * @Description: This method is for fetching fullfillment type(rich attribute) for product and return product -
	 *               fullfillment type mapping.
	 * @param product
	 * @return Map<Product, String>
	 */
	public Map<String, String> fetchProductRichAttribute(final Map<String, Integer> qCount)
	{
		final Map<String, String> productfullfillmentTypeMap = new HashMap<String, String>();
		final CartModel cartModel = cartService.getSessionCart();
		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			final ProductModel entryProduct = entry.getProduct();
			final String selectedUSSID = entry.getSelectedUSSID();
			if (qCount.containsKey(selectedUSSID))
			{
				for (final SellerInformationModel seller : entryProduct.getSellerInformationRelator())
				{
					for (final RichAttributeModel rm : seller.getRichAttribute())
					{
						if (null != seller.getSellerArticleSKU() && seller.getSellerArticleSKU().equalsIgnoreCase(selectedUSSID))
						{
							productfullfillmentTypeMap.put(selectedUSSID, rm.getDeliveryFulfillModes().getCode());

						}

					}
				}
			}
		}
		return productfullfillmentTypeMap;
	}

	/**
	 * @Description: This method is for calculating delivery charges for product and returns product - calculated
	 *               delivery charges after promotion mapping.
	 * @param isPercentageFlag
	 * @param adjustedDeliveryCharge
	 * @param fetchProductRichAttribute
	 * @param validProductList
	 * @return Map<Product, Double>
	 */
	public Map<String, Map<String, Double>> updateDeliveryCharges(final boolean isDeliveryFreeFlag,
			final boolean isPercentageFlag, final double adjustedDeliveryCharge, final Map<String, Integer> qCount,
			final Map<String, String> fetchProductRichAttribute)
	{
		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
		final Map<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel> prodDelChargeMap = new HashMap<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel>();
		final CartModel cartModel = cartService.getSessionCart();
		double totalDeliveryCostForValidProds = 0.00D;

		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			final String selectedUSSID = entry.getSelectedUSSID();
			if (qCount.containsKey(selectedUSSID) && null != entry.getMplDeliveryMode())
			{
				final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
				final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
				ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
				ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
				ServicesUtil.validateParameterNotNull(selectedUSSID, "sellerArticleSKU cannot be null");
				final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService.getDeliveryCost(
						selectedDeliveryModeCode, currencyIsoCode, selectedUSSID);
				prodDelChargeMap.put(entry, mplZoneDeliveryModeValueModel);
				if (!isPercentageFlag
						&& null != mplZoneDeliveryModeValueModel.getValue()
						&& (fetchProductRichAttribute.containsKey(entry.getSelectedUSSID())
								&& null != fetchProductRichAttribute.get(entry.getSelectedUSSID()) && !fetchProductRichAttribute.get(
								entry.getSelectedUSSID()).equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP)))
				{
					totalDeliveryCostForValidProds += mplZoneDeliveryModeValueModel.getValue().doubleValue()
							* qCount.get(selectedUSSID).intValue();
				}
			}
		}
		double totalAmtTobeDeduced = 0.00D;
		final Iterator iter = prodDelChargeMap.entrySet().iterator();
		while (iter.hasNext())
		{
			final Map.Entry orderEntry = (Map.Entry) iter.next();

			final MplZoneDeliveryModeValueModel prodMplZoneDeliveryModeValueModel = (MplZoneDeliveryModeValueModel) orderEntry
					.getValue();
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) orderEntry.getKey();
			final Map<String, Double> prevCurrDeliveryChargeMap = new HashMap<String, Double>();

			if ((fetchProductRichAttribute.containsKey(entry.getSelectedUSSID())
					&& null != fetchProductRichAttribute.get(entry.getSelectedUSSID()) && fetchProductRichAttribute.get(
					entry.getSelectedUSSID()).equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP))
					|| prodMplZoneDeliveryModeValueModel.getValue().doubleValue() == 0.00D)
			{
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, Double.valueOf(0));
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, Double.valueOf(0));
				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
			}
			else
			{
				final double deliveryChargeForEntry = prodMplZoneDeliveryModeValueModel.getValue().doubleValue()
						* entry.getQuantity().intValue();
				double amtTobeDeduced = 0.00D;
				double deliveryChargeAfterPromotion = 0.00D;

				if (isPercentageFlag) //For percentage discount
				{
					amtTobeDeduced = (adjustedDeliveryCharge / 100) * deliveryChargeForEntry;

				}
				else
				//For free delivery and amount discount
				{
					if (isDeliveryFreeFlag) //For free delivery
					{
						amtTobeDeduced = deliveryChargeForEntry;
					}
					else
					//For amount discount
					{
						final double convertedPercentageForAmt = (adjustedDeliveryCharge / totalDeliveryCostForValidProds) * 100;
						if (prodDelChargeMap.size() == 1)
						{
							amtTobeDeduced = adjustedDeliveryCharge - totalAmtTobeDeduced;
						}
						else
						{
							amtTobeDeduced = (convertedPercentageForAmt / 100) * deliveryChargeForEntry;
							totalAmtTobeDeduced += amtTobeDeduced;
						}
					}
				}

				if (deliveryChargeForEntry >= amtTobeDeduced)
				{
					deliveryChargeAfterPromotion = deliveryChargeForEntry - amtTobeDeduced;
				}
				else
				{
					deliveryChargeAfterPromotion = deliveryChargeForEntry;
				}

				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
						Double.valueOf(deliveryChargeForEntry));
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
						Double.valueOf(deliveryChargeAfterPromotion));

				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
				iter.remove();
			}
		}
		return prodPrevCurrDelChargeMap;
	}

	/**
	 * @Description: For undoing delivery charges
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomShippingChargesPromotionAdjustAction
	 */

	public Map<String, Map<String, Double>> undoDeliveryCharges(final AbstractOrder order,
			final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap, final SessionContext ctx)
	{
		for (final Map.Entry<String, Map<String, Double>> mapEntry : prodPrevCurrDelChargeMap.entrySet())
		{
			final Map<String, Double> prevCurrDeliveryChargeMap = mapEntry.getValue();
			prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
					prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE));
			prodPrevCurrDelChargeMap.put(mapEntry.getKey(), prevCurrDeliveryChargeMap);
		}
		for (final AbstractOrderEntry orderEntry : order.getEntries())
		{
			String entryUSSID = null;
			try
			{
				entryUSSID = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
			}
			catch (final JaloInvalidParameterException | JaloSecurityException e)
			{
				LOG.error(e);
			}
			if (prodPrevCurrDelChargeMap.containsKey(entryUSSID))
			{
				final Map<String, Double> prevCurrDeliveryChargeMap = prodPrevCurrDelChargeMap.get(entryUSSID);
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
						prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE));
				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
						prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE));
			}

		}
		return prodPrevCurrDelChargeMap;
	}


	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomShippingChargesPromotionAdjustAction
	 */

	public CustomShippingChargesPromotionAdjustAction createCustomShippingChargesPromotionAdjustAction(final SessionContext ctx,
			final AbstractOrderEntry entry, final long quantity, final double adjustment)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(adjustment));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_PRODUCT, entry.getProduct(ctx));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_NUMBER, entry.getEntryNumber());
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_QUANTITY, Long.valueOf(quantity));
		return createCustomShippingChargesPromotionAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomShippingChargesPromotionAdjustAction
	 */

	public CustomShippingChargesPromotionAdjustAction createCustomShippingChargesPromotionAdjustAction(final SessionContext ctx,
			final AbstractOrderEntry entry, final double adjustment)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(adjustment));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_PRODUCT, entry.getProduct(ctx));
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_NUMBER, entry.getEntryNumber());
		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_QUANTITY, entry.getQuantity(ctx));
		return createCustomShippingChargesPromotionAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param attributeValues
	 * @return CustomShippingChargesPromotionAdjustAction
	 */

	public CustomShippingChargesPromotionAdjustAction createCustomShippingChargesPromotionAdjustAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CustomShippingChargesPromotionAdjustAction");
			return ((CustomShippingChargesPromotionAdjustAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CustomShippingChargesPromotionAdjustAction : " + e.getMessage(), 0);
		}
	}

	/**
	 * @Description: Verify Exclude Seller Data
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param entry
	 * @return boolean
	 */
	public boolean isProductExcludedForSeller(final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList, final AbstractOrderEntry entry)
	{
		boolean flag = false;
		String ussid = MarketplacecommerceservicesConstants.EMPTY;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
			{
				ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
				final CatalogVersionModel oModel = catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid, oModel);
				flag = GenericUtilityMethods.checkExcludeSellerData(restrictionList, productSellerData);
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
		return flag;
	}

	/**
	 * @Description: For Payment mode restriction evaluation
	 * @param ctx
	 * @param restrictionList
	 * @return boolean
	 */
	public boolean getPaymentModeRestrEval(final List<AbstractPromotionRestriction> restrictionList, final SessionContext ctx)
	{
		boolean flag = false;
		if (null == restrictionList || restrictionList.isEmpty())
		{
			flag = true;
		}
		else
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				flag = false;
				if (restriction instanceof PaymentModeSpecificPromotionRestriction)
				{
					String paymentMode = null;
					String selectedBank = MarketplacecommerceservicesConstants.EMPTY;
					if (null != ctx)
					{
						paymentMode = ctx.getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
						selectedBank = ctx.getAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN);
					}
					if (paymentMode != null)
					{
						final PaymentModeSpecificPromotionRestriction paymentModeRestriction = ((PaymentModeSpecificPromotionRestriction) restriction);
						final List<PaymentType> restrPaymentModeList = paymentModeRestriction.getPaymentModes();
						if (restrPaymentModeList != null)
						{
							for (final PaymentType paymentType : restrPaymentModeList)
							{
								if (paymentType.getMode().equalsIgnoreCase(paymentMode))
								{
									final List<Bank> restrBanks = paymentModeRestriction.getBanks(ctx);
									if (CollectionUtils.isEmpty(restrBanks))
									{
										flag = true;
										break;
									}
									else if ((paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING)
											|| paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT)
											|| paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT) || paymentMode
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI))
											&& (StringUtils.isNotEmpty(selectedBank) && checkBankData(selectedBank, restrBanks)))
									{
										flag = true;
										break;
									}
								}
							}
						}
					}
					break;
				}
				else
				{
					flag = true;
				}
			}


		}

		return flag;
	}

	/**
	 * @param selectedBank
	 * @param restrBanks
	 * @return boolean
	 */
	private boolean checkBankData(final String selectedBank, final List<Bank> restrBanks)
	{
		boolean flag = false;
		try
		{
			for (final Bank bank : restrBanks)
			{
				if (null != bank.getAttribute("bankName") && bank.getAttribute("bankName").toString().equals(selectedBank))
				{
					flag = true;
					break;
				}
			}
		}
		catch (JaloInvalidParameterException | JaloSecurityException e)
		{
			LOG.error(e.getMessage());
		}
		return flag;
	}

	/**
	 * @Description: For populating valid product, list of ussid map
	 * @param ctx
	 * @param restrictionList
	 * @param product
	 * @param cart
	 * @param entry
	 * @return mapping of valid product, list of ussid
	 */

	public Map<String, AbstractOrderEntry> populateValidProductUssidMap(final Product product, final AbstractOrder cart,
			final List<AbstractPromotionRestriction> restrictionList, final SessionContext ctx, final AbstractOrderEntry entry)
	{
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();

		final List<AbstractOrderEntry> orderEntryList = cart.getEntriesByProduct(product) != null ? cart
				.getEntriesByProduct(product) : new ArrayList<AbstractOrderEntry>();

		if (isSellerRestrExists(restrictionList))
		{
			String selectedUSSID = null;
			try
			{
				selectedUSSID = (String) entry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
			}
			catch (JaloInvalidParameterException | JaloSecurityException e)
			{
				LOG.error(e);
			}
			validProductUssidMap.put(selectedUSSID, entry);
		}
		else
		{
			for (final AbstractOrderEntry productEntry : orderEntryList)
			{
				if (null != productEntry.isGiveAway() && !productEntry.isGiveAway().booleanValue())
				{
					String selectedUSSID = null;
					try
					{
						selectedUSSID = (String) productEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
					}
					catch (JaloInvalidParameterException | JaloSecurityException e)
					{
						LOG.error(e);
					}
					validProductUssidMap.put(selectedUSSID, productEntry);
				}
			}

		}

		return validProductUssidMap;
	}

	/**
	 * @Description: For populating sorted valid product, ussid map
	 * @param cart
	 * @param validProductUssidTempMap
	 * @return mapping of valid ussids
	 */
	public Set<String> populateSortedValidProdUssidMap(final Map<String, AbstractOrderEntry> validProductUssidTempMap,
			final int totalEligibleCount, final SessionContext ctx, final List<AbstractPromotionRestriction> restrictionList,
			final Map<String, Integer> qCountMap)
	{
		List<AbstractOrderEntry> validEntries = null;
		if (validProductUssidTempMap != null)
		{
			validEntries = new ArrayList<AbstractOrderEntry>(validProductUssidTempMap.values());
		}
		Collections.sort(validEntries, new Comparator<AbstractOrderEntry>()
		{
			public int compare(final AbstractOrderEntry o1, final AbstractOrderEntry o2)
			{
				if (o1.getBasePriceAsPrimitive() > o2.getBasePriceAsPrimitive())
				{
					return 1;
				}
				else
				{
					return -1;
				}
			}
		});
		//}

		return doConsumeEntries(validEntries, totalEligibleCount, ctx, qCountMap);
	}

	/**
	 * @Description: For checking whether seller restriction is attached with the promotion
	 * @param restrictionList
	 * @return boolean
	 */

	public boolean isSellerRestrExists(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean isSellerRestr = false;
		if (restrictionList != null && !restrictionList.isEmpty())
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				if (restriction instanceof EtailSellerSpecificRestriction)
				{
					isSellerRestr = true;
					break;
				}
			}
		}
		return isSellerRestr;
	}

	/**
	 * @Description: For checking whether seller restriction is attached with the promotion
	 * @param deliveryModeDetailsList
	 * @return valid ussid mapping
	 */

	public Map<String, Integer> getvalidProdQCForOrderShippingPromotion(final List<DeliveryMode> deliveryModeDetailsList)
	{
		final List<String> deliveryModeCodeList = new ArrayList<String>();
		for (final DeliveryMode deliveryMode : deliveryModeDetailsList)
		{
			deliveryModeCodeList.add(deliveryMode.getCode());
		}
		final Map<String, Integer> validProdQCountMap = new HashMap<String, Integer>();
		final CartModel cartModel = cartService.getSessionCart();

		for (final AbstractOrderEntryModel entryModel : cartModel.getEntries())
		{
			if (entryModel.getMplDeliveryMode() != null)
			{
				final String selectedDeliveryMode = entryModel.getMplDeliveryMode().getDeliveryMode().getCode();
				if (deliveryModeCodeList.contains(selectedDeliveryMode))
				{
					validProdQCountMap.put(entryModel.getSelectedUSSID(), Integer.valueOf(entryModel.getQuantity().intValue()));
				}
			}
		}

		return validProdQCountMap;
	}

	/**
	 * @Description: This method is for getting qualifying count for A, BOGO, Freebie promotions
	 * @param eligibleProductList
	 * @param abstractOrderEntry
	 * @return Map<Product, Integer>
	 */
	public Map<String, List<String>> getAssociatedItemsForABorFreebiePromotions(final List<String> validProductListA,
			final List<String> validProductListB, final String sKUForFreebie)
	{
		final Map<String, List<String>> productAssociatedItemsMap = new HashMap<String, List<String>>();
		final List<String> freebieAssociatedProductList = new ArrayList<String>();
		List<String> associatedProductList = null;

		for (int i = 0; i < validProductListA.size(); i++)
		{
			associatedProductList = new ArrayList<String>();
			associatedProductList.add(validProductListB.get(i));
			final String validProdAUssid = validProductListA.get(i);
			productAssociatedItemsMap.put(validProdAUssid, associatedProductList);

			if (sKUForFreebie != null)
			{
				//get the free product and add that into associatedProductList
				associatedProductList.add(sKUForFreebie);
				//for freebie associated product mapping
				freebieAssociatedProductList.add(validProdAUssid);
			}

		}

		for (int i = 0; i < validProductListB.size(); i++)
		{
			associatedProductList = new ArrayList<String>();
			associatedProductList.add(validProductListA.get(i));
			final String validProdBUssid = validProductListB.get(i);
			productAssociatedItemsMap.put(validProdBUssid, associatedProductList);

			if (sKUForFreebie != null)
			{
				//get the free product and add that into associatedProductList
				associatedProductList.add(sKUForFreebie);
				//for freebie associated product mapping
				freebieAssociatedProductList.add(validProdBUssid);
			}
		}


		if (!freebieAssociatedProductList.isEmpty())
		{
			productAssociatedItemsMap.put(sKUForFreebie, freebieAssociatedProductList);
		}
		return productAssociatedItemsMap;
	}

	/**
	 * @Description: This method is for getting consumed entries
	 * @param
	 * @param
	 * @return
	 */

	public Set<String> doConsumeEntries(final List<AbstractOrderEntry> validEntries, int totalEligibleCount,
			final SessionContext ctx, final Map<String, Integer> qCountMap)
	{

		final Set<String> validProdUssidSet = new HashSet<String>();

		for (int i = 0; i < validEntries.size(); i++)
		{
			final AbstractOrderEntry sortedEntry = validEntries.get(i);
			final int entryTotalQty = sortedEntry.getQuantity().intValue();
			if (totalEligibleCount > 0)
			{
				String selectedUSSID = null;
				try
				{
					selectedUSSID = (String) sortedEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
				}
				catch (JaloInvalidParameterException | JaloSecurityException e)
				{
					LOG.error(e);
				}

				validProdUssidSet.add(selectedUSSID);
				final long consumeCount = (entryTotalQty <= totalEligibleCount) ? entryTotalQty : totalEligibleCount;
				if (qCountMap != null)
				{
					qCountMap.put(selectedUSSID, Integer.valueOf((int) consumeCount));
				}
				totalEligibleCount -= consumeCount;
			}
		}
		return validProdUssidSet;
	}

	/**
	 * @Description: This method is for getting Sorted Valid Product Ussid Map
	 * @param validProductUssidMap
	 * @param totalCount
	 * @param eligibleQty
	 * @param paramSessionContext
	 * @param restrictionList
	 * @return ussid - Qualifying count map
	 */

	Map<String, Integer> getSortedValidProdUssidMap(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final int totalCount, final long eligibleQty, final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList)
	{
		final Map<String, Integer> validUssidList = new HashMap<String, Integer>();
		final int totalFactorCount = totalCount / (int) eligibleQty;
		final int totalEligibleCount = totalFactorCount * (int) eligibleQty;

		validProductUssidMap.keySet().retainAll(
				populateSortedValidProdUssidMap(validProductUssidMap, totalEligibleCount, paramSessionContext, restrictionList,
						validUssidList));
		return validUssidList;
	}

	/**
	 * @Description : Returns Valid Product List
	 * @param cart
	 * @param paramSessionContext
	 * @return Map<Product, Integer>
	 */
	protected Map<String, AbstractOrderEntry> getValidProdListForBuyXofAPromo(final AbstractOrder cart,
			final SessionContext paramSessionContext, final List<Product> promotionProductList,
			final List<Category> promotionCategoryList, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> excludedProductList, final List<String> excludeManufactureList, final List<String> sellerIDData,
			final Map<AbstractOrderEntry, String> eligibleProductMap)
	{
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		boolean applyPromotion = false;
		boolean brandFlag = false;
		boolean sellerFlag = false;
		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
		boolean isFreebie = false;

		//final int productQty = 0;
		for (final AbstractOrderEntry entry : cart.getEntries())
		{
			applyPromotion = false;
			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);

			if (!isFreebie)
			{
				final Product product = entry.getProduct();

				//excluded product check
				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
						|| isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
				{
					continue;
				}

				//checking product is a valid product for promotion
				if (!promotionProductList.isEmpty())
				{
					if (promotionProductList.contains(product))
					{
						applyPromotion = true;
						brandFlag = true;
						sellerFlag = checkSellerData(paramSessionContext, restrictionList, entry);
					}
				}
				else if (promotionProductList.isEmpty() && !promotionCategoryList.isEmpty())
				//checking product category is permitted by promotion category or not
				{
					final List<String> productCategoryList = getcategoryList(product, paramSessionContext);
					applyPromotion = GenericUtilityMethods.productExistsIncat(promotionCategoryList, productCategoryList);
					brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
					sellerFlag = checkSellerData(paramSessionContext, restrictionList, entry);
				}

				if (applyPromotion && brandFlag && sellerFlag)
				{
					sellerID = getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
					validProductUssidMap.putAll(populateValidProductUssidMap(product, cart, restrictionList, paramSessionContext,
							entry));
					if (sellerIDData != null && eligibleProductMap != null)
					{
						sellerIDData.add(sellerID);
						eligibleProductMap.put(entry, sellerID);
					}
				}
			}
		}

		return validProductUssidMap;
	}

	/**
	 * @Description : Checks For Manufacturer Based Restrictions
	 * @param : SessionContext arg0,PromotionEvaluationContext arg1
	 * @return : flag
	 */
	public boolean checkMinimumBrandAmount(final SessionContext arg0, final PromotionEvaluationContext arg1,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean flag = true;
		try
		{
			if (null != restrictionList && !restrictionList.isEmpty())
			{
				for (final AbstractPromotionRestriction restriction : restrictionList)
				{
					flag = false;
					if (restriction instanceof ManufacturesRestriction)
					{
						final ManufacturesRestriction manufacturesRestriction = (ManufacturesRestriction) restriction;
						final List<Category> restrManufactureList = (List<Category>) manufacturesRestriction.getManufacturers();
						final List<String> manufactureList = new ArrayList<String>();
						for (final Category brand : restrManufactureList)
						{
							manufactureList.add(brand.getName());
						}

						final double minimumManufactureAmount = manufacturesRestriction.getMinimumManufactureAmount() == null ? 0.0D
								: manufacturesRestriction.getMinimumManufactureAmount().doubleValue();
						flag = minimumBrandAmountCheck(manufactureList, validProductUssidMap, minimumManufactureAmount);
						break;
					}
					else
					{
						flag = true;
					}
				}
			}
			else
			{
				flag = true;
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
		return flag;
	}

	/**
	 * @Description: Returns true if Cart Entry Products sum amount is greater or equal to than minumum category value
	 * @param promotionProductList
	 * @param promotionCategoryList
	 * @param excludedProductList
	 * @param excludeManufactureList
	 * @param order
	 * @param minimumCategoryValue
	 * @param ctx
	 * @return boolean
	 */
	public boolean checkMinimumCategoryValue(final Map<String, AbstractOrderEntry> validProductUssidMap, final SessionContext ctx,
			final ProductPromotion productPromotion)
	{
		try
		{
			final double minimumCategoryValue = productPromotion.getProperty(ctx,
					MarketplacecommerceservicesConstants.MINIMUM_AMOUNT) != null ? ((Double) productPromotion.getProperty(ctx,
					MarketplacecommerceservicesConstants.MINIMUM_AMOUNT)).doubleValue() : 0.00D;
			double totalEligibleEntryAmount = 0.0D;
			if (minimumCategoryValue == 0.00D)
			{
				return true;
			}
			else if (minimumCategoryValue > 0.00D)
			{
				if (validProductUssidMap != null)
				{
					for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
					{
						final AbstractOrderEntry entry = mapEntry.getValue();
						totalEligibleEntryAmount += entry.getTotalPrice().doubleValue();
					}
				}

				if (totalEligibleEntryAmount >= minimumCategoryValue)
				{
					LOG.debug(Localization.getLocalizedString("promotion.categoryVal.more"));
					return true;
				}

				LOG.debug(Localization.getLocalizedString("promotion.categoryVal.less"));
				return false;
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

		LOG.debug(Localization.getLocalizedString("promotion.categoryVal.validate.notRequired"));

		return true;
	}

	/**
	 * @Description :Converts amount to percentage
	 * @param : SessionContext paramSessionContext ,Map<Product, Integer> validProductList , AbstractOrder cart,int
	 *        totalCount,double discountPriceValue
	 * @return :double
	 */

	public double getConvertedPercentageDiscount(final int totalCount, final double discountPriceValue,
			final Long eligibleQuantity, final double totalPricevalue)
	{
		final int totalFactorCount = totalCount / eligibleQuantity.intValue();
		final double totalDiscount = totalFactorCount * discountPriceValue;
		final double percentageDiscount = (totalDiscount * 100) / totalPricevalue;

		return percentageDiscount;
	}

	/**
	 * @Description : Populate Free Gift Count for Buy A Freebie Promotions
	 * @param key
	 * @param eligibleProductMap
	 * @param count
	 * @return giftCount
	 */
	public int getFreeGiftCount(final String key, final Map<AbstractOrderEntry, String> eligibleProductMap, final int count)
	{
		int giftCount = 0;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			if (null != key && null != eligibleProductMap && !eligibleProductMap.isEmpty() && count > 0)
			{
				final CatalogVersionModel oModel = catalogData();
				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(key, oModel);

				if (null != productSellerData && !productSellerData.isEmpty())
				{
					for (final SellerInformationModel sellerData : productSellerData)
					{
						for (final Map.Entry<AbstractOrderEntry, String> entry : eligibleProductMap.entrySet())
						{
							if (sellerData.getSellerID().equalsIgnoreCase(entry.getValue()))
							{
								giftCount = giftCount + (entry.getKey().getQuantity().intValue() / count);
							}
						}
					}
				}

			}

		}
		catch (final ModelNotFoundException exception)
		{
			giftCount = 0;
			LOG.error(exception.getMessage());
		}
		catch (final Exception exception)
		{
			giftCount = 0;
			LOG.error(exception.getMessage());
		}

		return giftCount;
	}

	/**
	 * @Description : Populate Free Gift Count for Buy A and B Freebie Promotions
	 * @param key
	 * @param giftProductCountMap
	 * @return giftCount
	 */
	public int getFreeGiftCount(final String key, final Map<String, Integer> giftProductCountMap)
	{
		int giftCount = 0;
		List<SellerInformationModel> productSellerData = null;
		try
		{
			for (final Map.Entry<String, Integer> entry : giftProductCountMap.entrySet())
			{
				if (StringUtils.isNotEmpty(key))
				{

					final CatalogVersionModel oModel = catalogData();
					productSellerData = getSellerBasedPromotionService().fetchSellerInformation(key, oModel);

					if (null != productSellerData && !productSellerData.isEmpty())
					{
						for (final SellerInformationModel sellerData : productSellerData)
						{
							if (sellerData.getSellerID().equalsIgnoreCase(entry.getKey().toString()))
							{
								giftCount = entry.getValue().intValue();
							}
						}
					}
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			giftCount = 0;
			LOG.error(exception.getMessage());
		}
		catch (final Exception exception)
		{
			giftCount = 0;
			LOG.error(exception.getMessage());
		}

		return giftCount;
	}

	/**
	 * @Description: Checks Gift Products belongs to a specific Seller ID
	 * @param productList
	 * @param sellerDetails
	 * @return Map<String, Product> giftProductDetails
	 */
	public Map<String, Product> getGiftProductsUSSID(final List<Product> productList, final List<String> sellerDetails)
	{
		final HashMap<String, Product> giftProductDetails = new HashMap<String, Product>();
		List<SellerInformationModel> giftProductSellerData = null;
		if (null != sellerDetails)
		{
			for (final Product product : productList)
			{
				final ProductModel oModel = productService.getProductForCode(catalogData(), product.getCode());
				if (null != oModel && null != oModel.getSellerInformationRelator())
				{
					giftProductSellerData = new ArrayList<SellerInformationModel>(oModel.getSellerInformationRelator());
					if (!giftProductSellerData.isEmpty())
					{
						for (final SellerInformationModel seller : giftProductSellerData)
						{
							if (sellerDetails.contains(seller.getSellerID()))
							{
								final List<StockLevelModel> stockData = mplStockService.getStockLevelDetail(seller.getSellerArticleSKU());
								for (final StockLevelModel stockModel : stockData)
								{
									if (stockModel.getAvailable() > 0)
									{
										giftProductDetails.put(seller.getSellerArticleSKU(), product);
									}
								}
							}
						}
					}
				}
			}

		}
		return giftProductDetails;
	}

	/**
	 * @Description: Setting Free Gift for Freebie Promotions
	 * @param ctx
	 * @param product
	 * @param result
	 * @return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters)
	 */
	public CustomPromotionOrderAddFreeGiftAction createCustomPromotionOrderAddFreeGiftAction(final SessionContext ctx,
			final Product product, final PromotionResult result)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.FREEPRODUCT, product);
		parameters.put(MarketplacecommerceservicesConstants.PROMOTIONRESULT, result);
		return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters);
	}

	/**
	 * @Description: Setting Free Gift for Freebie Promotions
	 * @param ctx
	 * @param attributeValues
	 */
	private CustomPromotionOrderAddFreeGiftAction createCustomPromotionOrderAddFreeGiftAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CustomPromotionOrderAddFreeGiftAction");
			return ((CustomPromotionOrderAddFreeGiftAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CustomPromotionOrderAddFreeGiftAction : " + e.getMessage(), 0);
		}
	}

	/**
	 * @Description:Setting Free Gift for Freebie Promotions
	 * @param ctx
	 * @param product
	 * @param ussid
	 * @param result
	 * @param quantity
	 * @return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters)
	 */
	public CustomPromotionOrderAddFreeGiftAction createCustomPromotionOrderAddFreeGiftAction(final SessionContext ctx,
			final Product product, final String ussid, final PromotionResult result, final Double quantity)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.FREEPRODUCT, product);
		parameters.put(MarketplacecommerceservicesConstants.PRODUCT_SKUID, ussid);
		parameters.put(MarketplacecommerceservicesConstants.PROMOTIONRESULT, result);
		parameters.put(MarketplacecommerceservicesConstants.GIFT_QUANTITY, quantity);
		return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters);
	}

	/**
	 * @Description:Setting Free Gift for Freebie Promotions
	 * @param ctx
	 * @param product
	 * @param ussid
	 * @param result
	 * @return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters)
	 */
	public CustomPromotionOrderAddFreeGiftAction createCustomPromotionOrderAddFreeGiftAction(final SessionContext ctx,
			final Product product, final String ussid, final PromotionResult result)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.FREEPRODUCT, product);
		parameters.put(MarketplacecommerceservicesConstants.PRODUCT_SKUID, ussid);
		parameters.put(MarketplacecommerceservicesConstants.PROMOTIONRESULT, result);
		return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters);
	}

	/**
	 * @Description : This calculates the total valid product price
	 * @param : validProductUssidMap, validProductList
	 * @return :double
	 */
	public double getTotalValidProdPrice(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final Map<String, Integer> validProductList)
	{
		double totalvalidproductsPricevalue = 0.0D;
		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
		{
			//total adjustment to be deducted from total price
			final AbstractOrderEntry entry = mapEntry.getValue();
			final String validUssid = mapEntry.getKey();
			totalvalidproductsPricevalue += (entry.getBasePrice().doubleValue() * validProductList.get(validUssid).intValue());
		}
		return totalvalidproductsPricevalue;
	}

	/**
	 * @Description: Check for the products with fired promotions and put them in excluded product list
	 * @param ctx
	 * @param order
	 * @param excludedProductist
	 */
	public boolean promotionAlreadyFired(final SessionContext ctx, final Map<String, AbstractOrderEntry> validProductUssidMap)
	{
		boolean isPromoAlreadyFired = false;
		for (final AbstractOrderEntry orderEntry : validProductUssidMap.values())
		{
			String promoCode = null;
			try
			{
				promoCode = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.PRODUCTPROMOCODE);
			}
			catch (final JaloInvalidParameterException | JaloSecurityException e)
			{
				LOG.error(e.getMessage());
			}

			if (promoCode != null && !promoCode.isEmpty())
			{
				isPromoAlreadyFired = true;
				break;
			}
		}
		return isPromoAlreadyFired;
	}

	/**
	 * @Description: Validates if Cart Promotion Fired
	 * @param ctx
	 * @param order
	 * @return boolean
	 */
	public boolean cartPromotionAlreadyFired(final SessionContext ctx, final AbstractOrder order)
	{
		boolean isPromoAlreadyFired = false;

		for (final AbstractOrderEntry orderEntry : order.getEntries())
		{
			String cartPromoCode = null;
			try
			{
				cartPromoCode = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.CARTPROMOCODE);
			}
			catch (final JaloInvalidParameterException | JaloSecurityException e)
			{
				LOG.error(e.getMessage());
			}

			if (cartPromoCode != null && !cartPromoCode.isEmpty())
			{
				isPromoAlreadyFired = true;
				break;
			}
		}
		return isPromoAlreadyFired;
	}

	/**
	 * @Description : This is for getting consumed entries for freebie
	 * @param ctx
	 * @param promotion
	 * @param validProductUssidMap
	 * @param validProductList
	 * @param totalCountForAandBPromo
	 * @param tcMapForValidEntries
	 * @return PromotionOrderEntryConsumed object
	 */
	public List<PromotionOrderEntryConsumed> getConsumedEntriesForFreebie(final SessionContext ctx,
			final AbstractPromotion promotion, final Map<String, AbstractOrderEntry> validProductUssidMap,
			final Map<String, Integer> validProductList, final Integer totalCountForAandBPromo,
			final Map<String, Integer> tcMapForValidEntries)
	{
		final List<PromotionOrderEntryConsumed> consumed = new ArrayList<PromotionOrderEntryConsumed>();

		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
		{
			final AbstractOrderEntry entry = mapEntry.getValue();
			final String validUssid = mapEntry.getKey();

			final int eligibleCount = validProductList != null ? validProductList.get(validUssid).intValue()
					: (totalCountForAandBPromo != null ? totalCountForAandBPromo.intValue() : 0);

			consumed.add(consume(ctx, promotion, eligibleCount, eligibleCount, entry));

			if (tcMapForValidEntries != null)
			{
				tcMapForValidEntries.put(validUssid, Integer.valueOf(entry.getQuantity().intValue() - eligibleCount));
			}

		}
		return consumed;
	}

	/**
	 * @Description : This is for getting remaining entries
	 * @param ctx
	 * @param workingEntries
	 * @param validProductUssidMap
	 * @param tcMapForValidEntries
	 * @return PromotionOrderEntryConsumed list
	 */

	public List<PromotionOrderEntryConsumed> findRemainingEntries(final SessionContext ctx,
			final List<PromotionOrderEntry> workingEntries, final Map<String, Integer> tcMapForValidEntries)
	{
		final List consumed = new ArrayList();
		for (final PromotionOrderEntry entry : workingEntries)
		{
			//get order entry
			final AbstractOrderEntry orderEntry = entry.getBaseOrderEntry();
			String selectedussid = null;
			try
			{
				selectedussid = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
				LOG.debug("promo entry :" + orderEntry.getEntryNumber() + " selectedussid:" + selectedussid + "-- qty:"
						+ orderEntry.getQuantity());
			}
			catch (final JaloInvalidParameterException | JaloSecurityException e)
			{
				// YTODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
			if (tcMapForValidEntries.containsKey(selectedussid))
			{
				final long quantity = tcMapForValidEntries.get(selectedussid).longValue();
				if (quantity > 0L)
				{
					final PromotionOrderEntryConsumed consumedEntry = PromotionsManager.getInstance()
							.createPromotionOrderEntryConsumed(ctx, "", orderEntry, quantity);
					consumed.add(consumedEntry);
				}
			}
		}
		return consumed;
	}

	/**
	 * @Description : This is for getting consumed entries at USSID level
	 * @param ctx
	 * @param promotion
	 * @param quantity
	 * @param available
	 * @param orderEntry
	 * @return PromotionOrderEntryConsumed object
	 */
	public PromotionOrderEntryConsumed consume(final SessionContext ctx, final AbstractPromotion promotion, final long quantity,
			final long available, final AbstractOrderEntry orderEntry)
	{
		if (quantity == 0L)
		{
			throw new PromotionException("Cannot consume zero products from an OrderEntry");
		}

		final long resultingQuantity = available - quantity;
		if (resultingQuantity < 0L)
		{
			throw new PromotionException("Cannot remove " + quantity
					+ " items.  There is not a sufficient quantity of this product remaining.");
		}

		final PromotionOrderEntryConsumed consumed = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx, "",
				orderEntry, quantity);

		return consumed;
	}

	//For Referring to Promotion Helper Class
	protected MplPromotionHelper getMplPromotionHelper()
	{
		return Registry.getApplicationContext().getBean("mplPromotionHelper", MplPromotionHelper.class);
	}

	//TISEE-5339 : Multiple Seller Scenario Fix
	/**
	 * @Description: This method is for calculating delivery charges for product and returns product - calculated
	 *               delivery charges after promotion mapping.
	 * @param isPercentageFlag
	 * @param adjustedDeliveryCharge
	 * @param validProductList
	 * @return Map<Product, Double>
	 */
	public Map<String, Map<String, Double>> calcDeliveryCharges(final boolean isDeliveryFreeFlag, final boolean isPercentageFlag,
			final double adjustedDeliveryCharge, final Map<String, Integer> qCount, final String validProductUSSID)
	{
		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
		final Map<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel> prodDelChargeMap = new HashMap<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel>();
		final CartModel cartModel = cartService.getSessionCart();
		double totalDeliveryCostForValidProds = 0.00D;

		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			final String selectedUSSID = entry.getSelectedUSSID();
			if (qCount.containsKey(selectedUSSID) && null != entry.getMplDeliveryMode()
					&& selectedUSSID.equalsIgnoreCase(validProductUSSID))
			{
				final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
				final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
				ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
				ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
				ServicesUtil.validateParameterNotNull(selectedUSSID, "sellerArticleSKU cannot be null");
				final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService.getDeliveryCost(
						selectedDeliveryModeCode, currencyIsoCode, selectedUSSID);
				prodDelChargeMap.put(entry, mplZoneDeliveryModeValueModel);
				if (!isPercentageFlag && null != mplZoneDeliveryModeValueModel.getValue())
				{
					totalDeliveryCostForValidProds += mplZoneDeliveryModeValueModel.getValue().doubleValue()
							* qCount.get(selectedUSSID).intValue();
				}
			}
		}
		double totalAmtTobeDeduced = 0.00D;
		final Iterator iter = prodDelChargeMap.entrySet().iterator();
		while (iter.hasNext())
		{
			final Map.Entry orderEntry = (Map.Entry) iter.next();

			final MplZoneDeliveryModeValueModel prodMplZoneDeliveryModeValueModel = (MplZoneDeliveryModeValueModel) orderEntry
					.getValue();
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) orderEntry.getKey();
			final Map<String, Double> prevCurrDeliveryChargeMap = new HashMap<String, Double>();

			if (prodMplZoneDeliveryModeValueModel.getValue().doubleValue() == 0.00D)
			{
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
						prodMplZoneDeliveryModeValueModel.getValue());
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
						prodMplZoneDeliveryModeValueModel.getValue());
				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
			}
			else
			{
				final double deliveryChargeForEntry = prodMplZoneDeliveryModeValueModel.getValue().doubleValue()
						* entry.getQuantity().intValue();
				double amtTobeDeduced = 0.00D;
				double deliveryChargeAfterPromotion = 0.00D;

				if (isPercentageFlag) //For percentage discount
				{
					amtTobeDeduced = (adjustedDeliveryCharge / 100) * deliveryChargeForEntry;

				}
				else
				//For free delivery and amount discount
				{
					if (isDeliveryFreeFlag) //For free delivery
					{
						amtTobeDeduced = deliveryChargeForEntry;
					}
					else
					//For amount discount
					{
						final double convertedPercentageForAmt = (adjustedDeliveryCharge / totalDeliveryCostForValidProds) * 100;
						if (prodDelChargeMap.size() == 1)
						{
							amtTobeDeduced = adjustedDeliveryCharge - totalAmtTobeDeduced;
						}
						else
						{
							amtTobeDeduced = (convertedPercentageForAmt / 100) * deliveryChargeForEntry;
							totalAmtTobeDeduced += amtTobeDeduced;
						}
					}
				}

				if (deliveryChargeForEntry >= amtTobeDeduced)
				{
					deliveryChargeAfterPromotion = deliveryChargeForEntry - amtTobeDeduced;
				}
				else
				{
					deliveryChargeAfterPromotion = deliveryChargeForEntry;
				}

				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
						Double.valueOf(deliveryChargeForEntry));
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
						Double.valueOf(deliveryChargeAfterPromotion));

				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
				iter.remove();
			}
		}
		return prodPrevCurrDelChargeMap;
	}



	/**
	 * @Description: For checking whether seller restriction is attached with the promotion
	 * @param restrictionList
	 * @return boolean
	 */

	public boolean sellerRestrExists(final List<AbstractPromotionRestrictionModel> restrictionList)
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



	/**
	 * @Description : Returns Valid Product List
	 * @param cart
	 * @param paramSessionContext
	 * @return Map<Product, Integer>
	 */
	protected Map<String, AbstractOrderEntry> getValidProdListForBOGO(final AbstractOrder cart,
			final SessionContext paramSessionContext, final List<Product> promotionProductList,
			final List<Category> promotionCategoryList, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> excludedProductList, final List<String> excludeManufactureList, final List<String> sellerIDData,
			final Map<AbstractOrderEntry, String> eligibleProductMap)
	{
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		boolean applyPromotion = false;
		boolean brandFlag = false;
		boolean sellerFlag = false;
		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
		boolean isFreebie = false;

		//final int productQty = 0;
		for (final AbstractOrderEntry entry : cart.getEntries())
		{
			applyPromotion = false;
			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);

			if (!isFreebie)
			{
				final Product product = entry.getProduct();

				//excluded product check
				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
						|| isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
				{
					continue;
				}

				//checking product is a valid product for promotion
				if (!promotionProductList.isEmpty())
				{
					if (promotionProductList.contains(product))
					{
						applyPromotion = true;
						brandFlag = true;
						sellerFlag = checkSellerBOGOData(paramSessionContext, restrictionList, entry);
					}
				}
				else if (promotionProductList.isEmpty() && !promotionCategoryList.isEmpty())
				//checking product category is permitted by promotion category or not
				{
					final List<String> productCategoryList = getcategoryList(product, paramSessionContext);
					applyPromotion = GenericUtilityMethods.productExistsIncat(promotionCategoryList, productCategoryList);
					brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
					sellerFlag = checkSellerBOGOData(paramSessionContext, restrictionList, entry);
				}

				if (applyPromotion && brandFlag && sellerFlag)
				{
					sellerID = getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
					validProductUssidMap.putAll(populateValidProductUssidMap(product, cart, restrictionList, paramSessionContext,
							entry));
					if (sellerIDData != null && eligibleProductMap != null)
					{
						sellerIDData.add(sellerID);
						eligibleProductMap.put(entry, sellerID);
					}
				}
			}
		}

		return validProductUssidMap;
	}


	/**
	 * @Description: This Method is used to populate Associated Items for Buy A Free bie Promotion **Added for TISEE-5527
	 *               **
	 *
	 * @param validProductUssidMap
	 * @param sKUForFreebieList
	 * @return productAssociatedItemsMap
	 */
	public Map<String, List<String>> getAssociatedItemsForAFreebiePromotions(
			final Map<String, AbstractOrderEntry> validProductUssidMap, final List<String> sKUForFreebieList)
	{
		final Map<String, List<String>> productAssociatedItemsMap = new HashMap<String, List<String>>();
		final List<String> freebieAssociatedProductList = new ArrayList<String>();
		String validProductUssid = null;
		final List<String> validProdUssidList = new ArrayList<String>(validProductUssidMap.keySet());

		if (validProdUssidList.size() == 1) //for Buy A get B free, Buy A get B free and percentage off
		{
			final List<String> associatedProductList = new ArrayList<String>();
			validProductUssid = validProdUssidList.get(0);
			if (CollectionUtils.isNotEmpty(sKUForFreebieList))
			{
				//get the free product and add that into associatedProductList
				associatedProductList.addAll(sKUForFreebieList);
				//for freebie associated product mapping
				freebieAssociatedProductList.add(validProductUssid);
				//productAssociatedItemsMap.put(sKUForFreebie, freebieAssociatedProductList);
			}
			productAssociatedItemsMap.put(validProductUssid, associatedProductList);

		}
		else
		{
			List<String> associatedProductList = null;
			for (int i = 0; i < validProdUssidList.size(); i++)
			{
				associatedProductList = new ArrayList<String>();
				validProductUssid = validProdUssidList.get(i);
				if (i > 0)
				{
					for (int j = 0/* validProdUssidList.indexOf(validProductUssid) - i */; j < validProdUssidList.size(); j++)
					{
						final String currUssid = validProdUssidList.get(j);
						if (!validProductUssid.equalsIgnoreCase(currUssid))
						{
							associatedProductList.add(currUssid);
						}
					}

				}
				else
				{
					//need to add the final remaining elements
					associatedProductList.addAll(validProdUssidList.subList(validProdUssidList.indexOf(validProductUssid) + 1,
							validProdUssidList.size()));
				}

				if (CollectionUtils.isNotEmpty(sKUForFreebieList))
				{
					//TODO get the free product and add that into associatedProductList
					associatedProductList.addAll(sKUForFreebieList);
					//for freebie associated product mapping
					freebieAssociatedProductList.add(validProdUssidList.get(i));
				}
				productAssociatedItemsMap.put(validProductUssid, associatedProductList);
			}
		}

		if (!freebieAssociatedProductList.isEmpty() && !sKUForFreebieList.isEmpty())
		{
			for (final String sKUForFreebie : sKUForFreebieList)
			{
				productAssociatedItemsMap.put(sKUForFreebie, freebieAssociatedProductList);
			}
		}
		return productAssociatedItemsMap;
	}

	/**
	 * For TISEE-5527
	 *
	 * @Description:Setting Free Gift for Freebie Promotions
	 * @param ctx
	 * @param freegiftUSSIDMap
	 * @param result
	 * @param quantity
	 * @return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters)
	 */
	public CustomPromotionOrderAddFreeGiftAction createCustomPromotionOrderAddFreeGiftAction(final SessionContext ctx,
			final Map<String, Product> freegiftUSSIDMap, final PromotionResult result, final Double quantity)
	{
		final Map parameters = new HashMap();
		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
		parameters.put(MarketplacecommerceservicesConstants.PRODUCT_USSID_LIST, freegiftUSSIDMap);
		parameters.put(MarketplacecommerceservicesConstants.PROMOTIONRESULT, result);
		parameters.put(MarketplacecommerceservicesConstants.GIFT_QUANTITY, quantity);
		parameters.put(MarketplacecommerceservicesConstants.ISBUYAGETPROMO, Boolean.TRUE);
		return createCustomPromotionOrderAddFreeGiftAction(ctx, parameters);
	}


	/**
	 * Added For TISST-13948
	 *
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param cutomBogoPromoDataMap
	 * @return CustomPromotionOrderEntryAdjustAction
	 */

	public CustomBOGOPromoOrderEntryAdjustAction createCustomBOGOPromoOrderEntryAdjustAction(final SessionContext ctx,
			final Map<AbstractOrderEntry, Double> customBogoPromoDataMap, final boolean flag)
	{
		final Map parameters = new HashMap();
		if (flag)
		{
			parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
			parameters.put(MarketplacecommerceservicesConstants.BOGO_DETAILS_MAP, customBogoPromoDataMap);
		}
		return createCustomBOGOPromoOrderEntryAdjustAction(ctx, parameters);
	}
}
