/**
 *
 */
package com.tisl.mpl.jalo;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
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
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.promotions.jalo.AbstractPromotion;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.promotions.jalo.OrderPromotion;
import de.hybris.platform.promotions.jalo.ProductPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionException;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.Flat3Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockLevelPromotionCheckService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplStockService;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyAGetPrecentageDiscountCashbackModel;
import com.tisl.mpl.model.BuyAGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.BuyAandBGetPromotionOnShippingChargesModel;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.EtailSellerSpecificRestrictionModel;
import com.tisl.mpl.model.FixedPricePromotionModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.util.ValueComparator;


/**
 * Customized PromotionsManager for mpl
 *
 */
@SuppressWarnings("deprecation")
public class DefaultPromotionManager extends PromotionsManager
{
	private final static Logger LOG = Logger.getLogger(DefaultPromotionManager.class.getName());

	@Autowired
	private BaseSiteService baseSiteService;

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
	@Autowired
	private ModelService modelService;

	@Autowired
	private MplJewelleryService jewelleryService;


	@Resource(name = "stockPromoCheckService")
	private ExtStockLevelPromotionCheckService stockPromoCheckService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "mplCategoryServiceImpl")
	MplCategoryService mplCategoryServiceImpl;

	private static final String AS_PPROM = " ) AS pprom";
	private static final String PPROM = " ) pprom";
	private static final String SELECT_CAT2PROD_TARGET = "{{ SELECT {cat2prod:target} as pk  ";

	//Change for FineJewellery
	/**
	 * @return the jewelleryService
	 */
	public MplJewelleryService getMplJewelleryService()
	{
		return jewelleryService;
	}

	/**
	 * @param jewelleryService
	 *           the jewelleryService to set
	 */
	public void setMplJewelleryService(final MplJewelleryService jewelleryService)
	{
		this.jewelleryService = jewelleryService;
	}

	//end FineJewellery
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
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @Description: Checks Restriction Brand names with Product Brand name
	 * @param promotionManufacturerList
	 * @param product
	 * @return boolean
	 */
	public boolean brandDataCheck(final List<String> promotionManufacturerList, final Product product)
	{
		//Code Modified for  TISPT-148
		boolean applyPromotion = false;
		String brandName = MarketplacecommerceservicesConstants.EMPTY;
		try
		{ //final ProductModel productModel = productService.getProductForCode(product.getCode());
			final List<Category> categoryList = (List<Category>) product.getAttribute("supercategories");
			if (CollectionUtils.isNotEmpty(categoryList))
			{
				for (final Category category : categoryList)
				{
					if (category.getCode().startsWith("MBH"))
					{
						brandName = category.getName();
						break;
					}
				}
			}

			if (CollectionUtils.isNotEmpty(promotionManufacturerList) && StringUtils.isNotEmpty(brandName))
			{
				applyPromotion = isEligibleManufacturer(promotionManufacturerList, brandName);
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
	 * //Code Modified for TISPT-148
	 *
	 * @param promotionManufacturerList
	 * @return isValid
	 */
	private boolean isEligibleManufacturer(final List<String> promotionManufacturerList, final String brandName)
	{
		boolean isValid = false;

		for (final String promoManufacturer : promotionManufacturerList)
		{
			//if (promoManufacturer.toLowerCase().equalsIgnoreCase(brand.getName().toLowerCase())) Sonar fix

			//Code Modified for TISPT-148
			if (promoManufacturer.equalsIgnoreCase(brandName))
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
				//final Product product = entry.getProduct();
				//				if (brandDataCheck(manufactureList, product))
				//				{
				totalEligibleEntryAmount += entry.getTotalPrice().doubleValue();
				//}

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
	//	public boolean minimumCategoryValueForABCheck(final List<Product> promotionProductList,
	//			final List<Product> promotionSecondProductList, final List<Category> categorylist, final List<Category> bCategorylist,
	//			final List<Product> excludedProductList, final List<String> excludeManufactureList, final AbstractOrder order,
	//			final double minimumCategoryValue, final SessionContext ctx)
	//	{
	//		try
	//		{
	//			final List<Category> totalCategorylist = new ArrayList<Category>();
	//
	//			double totalEligibleEntryAmount = 0.0D;
	//
	//			if (!categorylist.isEmpty() && promotionProductList.isEmpty())
	//			{
	//				totalCategorylist.addAll(categorylist);
	//			}
	//			if (!bCategorylist.isEmpty() && promotionSecondProductList.isEmpty())
	//			{
	//				totalCategorylist.addAll(bCategorylist);
	//			}
	//
	//			if (!totalCategorylist.isEmpty() && minimumCategoryValue > 0)
	//			{
	//				for (final AbstractOrderEntry entry : order.getEntries())
	//				{
	//					final Product product = entry.getProduct();
	//					//excluded product check
	//					if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
	//							|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList))
	//					{
	//						continue;
	//					}
	//					List<String> productCategoryList = null;
	//					productCategoryList = getcategoryList(product, ctx);
	//					if (null != productCategoryList && !productCategoryList.isEmpty()
	//							&& GenericUtilityMethods.productExistsIncat(totalCategorylist, productCategoryList))
	//					{
	//						totalEligibleEntryAmount += entry.getTotalPrice().doubleValue();
	//					}
	//				}
	//
	//				if (totalEligibleEntryAmount >= minimumCategoryValue)
	//				{
	//					LOG.debug(Localization.getLocalizedString("promotion.categoryVal.more"));
	//					return true;
	//				}
	//				LOG.debug(Localization.getLocalizedString("promotion.categoryVal.less"));
	//				return false;
	//			}
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//		}
	//		catch (final Exception e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
	//		}
	//
	//		LOG.debug(Localization.getLocalizedString("promotion.categoryVal.validate.notRequired"));
	//		return true;
	//
	//	}

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
		//if (null != channel && null != listOfChannel && !listOfChannel.isEmpty())	//TPR-969
		if (null != channel && CollectionUtils.isNotEmpty(listOfChannel))
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
	 * @return noOfProducts
	 */
	//	public int noOfEligibleProducts(final SessionContext ctx, final PromotionEvaluationContext promoContext,
	//			final AbstractPromotion promo, final Map<String, AbstractOrderEntry> validProductUssidMap)
	//	{
	//		int noOfProducts = 0;
	//		int realQuantity = 0;
	//		final AbstractOrder cart = promoContext.getOrder();
	//		List<Product> promotionProductList = null;
	//		List<Category> promotionCategoryList = null;
	//
	//		if (promo instanceof BuyXItemsofproductAgetproductBforfree)
	//		{
	//			promotionProductList = new ArrayList<>(((BuyXItemsofproductAgetproductBforfree) promo).getProducts());
	//			promotionCategoryList = new ArrayList<>(((BuyXItemsofproductAgetproductBforfree) promo).getCategories());
	//		}
	//		else if (promo instanceof BuyAPercentageDiscount)
	//		{
	//			promotionProductList = new ArrayList<>(((BuyAPercentageDiscount) promo).getProducts());
	//			promotionCategoryList = new ArrayList<>(((BuyAPercentageDiscount) promo).getCategories());
	//		}
	//		else if (promo instanceof BuyABFreePrecentageDiscount)
	//		{
	//			promotionProductList = new ArrayList<>(((BuyABFreePrecentageDiscount) promo).getProducts());
	//			promotionCategoryList = new ArrayList<>(((BuyABFreePrecentageDiscount) promo).getCategories());
	//		}
	//		else if (promo instanceof BuyAGetPromotionOnShippingCharges)
	//		{
	//			promotionProductList = new ArrayList<>(((BuyAGetPromotionOnShippingCharges) promo).getProducts());
	//			promotionCategoryList = new ArrayList<>(((BuyAGetPromotionOnShippingCharges) promo).getCategories());
	//		}
	//		for (final AbstractOrderEntry entry : cart.getEntries())
	//		{
	//			boolean applyPromotion = false;
	//			final Product product = entry.getProduct();
	//			//			if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
	//			//					|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList))
	//			//			{
	//			//				continue;
	//			//			}
	//			if (!promotionProductList.isEmpty())
	//			{
	//				if (promotionProductList.contains(product))
	//				{
	//					applyPromotion = true;
	//				}
	//			}
	//			else if (!promotionCategoryList.isEmpty())
	//			{
	//				final List<String> productCategoryList = getcategoryList(product, ctx);
	//				applyPromotion = GenericUtilityMethods.productExistsIncat(promotionCategoryList, productCategoryList);
	//			}
	//
	//			if (applyPromotion)
	//			{
	//				realQuantity += entry.getQuantity().intValue();
	//			}
	//		}
	//		if (realQuantity > 0)
	//		{
	//			noOfProducts = realQuantity;
	//		}
	//		return noOfProducts;
	//	}

	/**
	 * @Description: Category Data corresponding to a Product
	 * @param product
	 * @param paramSessionContext
	 * @return productCategoryList
	 */

	public List<String> getcategoryList(final Product product, final SessionContext paramSessionContext)
	{
		List<String> productCategoryList = null;
		//final List<Category> productCategoryData = null;
		//List<CategoryModel> superCategoryData = null;
		List<CategoryModel> productCategoryData = null;

		HashSet<CategoryModel> superCategoryData = null;

		//final CatalogVersionModel oCatalogVersionModel = catalogData();
		try
		{
			//			productCategoryData = (List<Category>) product.getAttribute(paramSessionContext,
			//					MarketplacecommerceservicesConstants.PROMO_CATEGORY);
			//			if (CollectionUtils.isNotEmpty(productCategoryData))
			//			{
			//				productCategoryList = new ArrayList<String>();
			//				for (final Category category : productCategoryData)
			//				{
			//					if (null != category && null != category.getCode(paramSessionContext))
			//					{
			//						productCategoryList.add(category.getCode(paramSessionContext));
			//						//final CategoryModel oModel = categoryService.getCategoryForCode(category.getCode(paramSessionContext));
			//						//Commented to fix defect TISUATPII-1558 -- multiple category giving erroneous promotion result
			//						final CategoryModel oModel = categoryService.getCategoryForCode(oCatalogVersionModel,
			//								category.getCode(paramSessionContext));
			//						superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(oModel));
			//						if (!superCategoryData.isEmpty())
			//						{
			//							for (final CategoryModel categoryModel : superCategoryData)
			//							{
			//								productCategoryList.add(categoryModel.getCode());
			//							}
			//						}
			//					}
			//				}
			//			}


			final ProductModel productModel = getModelService().get(product);

			productCategoryData = new ArrayList<>(productModel.getSupercategories());
			if (CollectionUtils.isNotEmpty(productCategoryData))
			{
				productCategoryList = new ArrayList<String>();

				superCategoryData = (HashSet<CategoryModel>) mplCategoryServiceImpl
						.getAllSupercategoriesForCategoryList(productCategoryData);
				if (CollectionUtils.isNotEmpty(superCategoryData))
				{
					final List<CategoryModel> dataList = new ArrayList<CategoryModel>(superCategoryData);

					for (final CategoryModel oModel : dataList)
					{
						productCategoryList.add(oModel.getCode());
					}
				}

				for (final CategoryModel oModel : productCategoryData)
				{
					productCategoryList.add(oModel.getCode());
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

		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomPromotionOrderAdjustTotalAction(ctx, parameters);
		}

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
	 * @param adjustment
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

		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomPromotionOrderEntryAdjustAction(ctx, parameters);
		}

		return createCustomPromotionOrderEntryAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param adjustment
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

		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomPromotionOrderEntryAdjustAction(ctx, parameters);
		}

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

			//			superCategoryData = new ArrayList<CategoryModel>(productdata.getSupercategories());
			//			if (!superCategoryData.isEmpty())
			//			{
			//				productCategoryData = new ArrayList<CategoryModel>();
			//				for (final CategoryModel category : superCategoryData)
			//				{
			//					if (null != category && null != category.getCode())
			//					{
			//						//final CategoryModel oModel = categoryService.getCategoryForCode(oCatalogVersionModel, category.getCode());
			//						productCategoryData.add(category);
			//						superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(category));
			//						if (!superCategoryData.isEmpty())
			//						{
			//							for (final CategoryModel categoryModel : superCategoryData)
			//							{
			//								productCategoryData.add(categoryModel);
			//							}
			//						}
			//					}
			//				}
			//			}

			if (CollectionUtils.isNotEmpty(productCategoryData))
			{
				superCategoryList = new ArrayList<CategoryModel>();
				superCategoryData = (HashSet<CategoryModel>) mplCategoryServiceImpl
						.getAllSupercategoriesForCategoryList(productCategoryData);
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
	 * @Description: Category Data corresponding to a Product for Promotion Intercepter
	 * @param product
	 * @return productCategoryData
	 */

	public List<CategoryModel> getPrimarycategoryData(final ProductModel product)
	{
		List<CategoryModel> productCategoryData = null;
		List<CategoryModel> superCategoryData = null;
		//final CatalogVersionModel oCatalogVersionModel = catalogData();
		HashSet<CategoryModel> categoryData = null;

		if (null != product)
		{
			superCategoryData = new ArrayList<CategoryModel>(product.getSupercategories());
			//if (!superCategoryData.isEmpty())
			if (CollectionUtils.isNotEmpty(superCategoryData))
			{
				productCategoryData = new ArrayList<CategoryModel>();
				final List<CategoryModel> finalCategoryList = new ArrayList<CategoryModel>();
				final String primaryCat = configurationService.getConfiguration().getString("decorator.primary",
						MarketplacecommerceservicesConstants.EMPTY);

				//				for (final CategoryModel category : superCategoryData)
				//				{
				//					//TISUAT-4621
				//					final String primaryCat = configurationService.getConfiguration().getString("decorator.primary", "");
				//					if (null != category && null != category.getCode() && category.getCode().indexOf(primaryCat) > -1)
				//					{
				//						final CategoryModel oModel = categoryService.getCategoryForCode(oCatalogVersionModel, category.getCode());
				//						productCategoryData.add(oModel);
				//						superCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(oModel));
				//						if (!superCategoryData.isEmpty())
				//						{
				//							for (final CategoryModel categoryModel : superCategoryData)
				//							{
				//								productCategoryData.add(categoryModel);
				//							}
				//						}
				//					}
				//				}

				for (final CategoryModel category : superCategoryData)
				{
					if (StringUtils.isNotEmpty(category.getCode()) && category.getCode().indexOf(primaryCat) > -1)
					{
						finalCategoryList.add(category);
					}
				}

				if (CollectionUtils.isNotEmpty(finalCategoryList))
				{
					categoryData = (HashSet<CategoryModel>) mplCategoryServiceImpl
							.getAllSupercategoriesForCategoryList(finalCategoryList);
					if (CollectionUtils.isNotEmpty(categoryData))
					{
						final List<CategoryModel> dataList = new ArrayList<CategoryModel>(categoryData);
						productCategoryData.addAll(dataList);
					}
				}
				productCategoryData.addAll(finalCategoryList);
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
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		String catalogId = "";
		if (null != currentBaseSite && StringUtils.isNotBlank(currentBaseSite.getUid())

				&& currentBaseSite.getUid().equals(
						configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.DEFAULTLUXURYSITEID)))
		{





			catalogId = configurationService.getConfiguration()
					.getString(MarketplacecommerceservicesConstants.DEFAULTLUXURYCATALOGID, "");
		}
		else
		{

			catalogId = configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID, "");
		}
		final String catalogVersionName = configurationService.getConfiguration()
				.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID, "");
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

		try
		{
			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
			{
				final String ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID)
						.toString();
				final CatalogVersionModel oModel = catalogData();
				List<SellerInformationModel> productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid,
						oModel);
				//change for FineJewellery
				if (CollectionUtils.isEmpty(productSellerData))
				{
					final List<JewelleryInformationModel> jewelleryInfo = getMplJewelleryService().getJewelleryInfoByUssid(
							entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString());
					if (CollectionUtils.isNotEmpty(jewelleryInfo))
					{
						productSellerData = getSellerBasedPromotionService().fetchSellerInformation(jewelleryInfo.get(0).getPCMUSSID(),
								oModel);
					}

				}
				//end FineJewellery
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
	//	public boolean checkSellerBOGOData(final SessionContext paramSessionContext,
	//			final List<AbstractPromotionRestriction> restrictionList, final AbstractOrderEntry entry)
	//	{
	//		boolean flag = false;
	//		String ussid = MarketplacecommerceservicesConstants.EMPTY;
	//		List<SellerInformationModel> productSellerData = null;
	//		try
	//		{
	//			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID))
	//			{
	//				ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString();
	//				final CatalogVersionModel oModel = catalogData();
	//				productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid, oModel);
	//				flag = GenericUtilityMethods.checkBOGOData(restrictionList, productSellerData);
	//			}
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//		}
	//		catch (final Exception e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
	//		}
	//		return flag;
	//	}

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
				//change for FineJewellery
				if (CollectionUtils.isEmpty(productSellerData))
				{
					final List<JewelleryInformationModel> jewelleryInfo = getMplJewelleryService().getJewelleryInfoByUssid(
							entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString());
					if (CollectionUtils.isNotEmpty(jewelleryInfo))
					{
						productSellerData = getSellerBasedPromotionService().fetchSellerInformation(jewelleryInfo.get(0).getPCMUSSID(),
								oModel);
					}

				}
				//end FineJewellery
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
								//TISSIT-1906---Stock check removal for freebie Items
								giftProductDetails.put(seller.getSellerArticleSKU(), product);
								//								final List<StockLevelModel> stockData = mplStockService.getStockLevelDetail(seller.getSellerArticleSKU());
								//								for (final StockLevelModel stockModel : stockData)
								//								{
								//									if (stockModel.getAvailable() > 0)
								//									{
								//										giftProductDetails.put(seller.getSellerArticleSKU(), product);
								//									}
								//								}
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
	//	public boolean getValidProductListForAmtDiscount(final SessionContext paramSessionContext, final AbstractOrder cart,
	//			final List<Product> promotionProductList, final List<Category> promotionCategoryList, final Long eligibleQuantity,
	//			final Double discountPrice, final Map<String, AbstractOrderEntry> validProductUssidMap)
	//	{
	//		boolean _flagCouldFireMessage = true;
	//		double priceForDiscount = 0.00D;
	//		double totalValidProductPrice = 0.00D;
	//
	//
	//		for (final AbstractOrderEntry entry : validProductUssidMap.values())
	//		{
	//			totalValidProductPrice += entry.getTotalPriceAsPrimitive(paramSessionContext);
	//		}
	//
	//		final Iterator iter = validProductUssidMap.entrySet().iterator();
	//		while (iter.hasNext())
	//		{
	//			final Map.Entry mapEntry = (Map.Entry) iter.next();
	//			final AbstractOrderEntry entry = (AbstractOrderEntry) mapEntry.getValue();
	//
	//			if (!promotionProductList.isEmpty())
	//			{
	//				priceForDiscount = entry.getBasePrice(paramSessionContext).doubleValue();
	//			}
	//			else if (promotionProductList.isEmpty() && !promotionCategoryList.isEmpty())
	//			{
	//				if (eligibleQuantity.intValue() > 1)
	//				{
	//					priceForDiscount = totalValidProductPrice;
	//				}
	//				else
	//				{
	//					priceForDiscount = entry.getBasePrice(paramSessionContext).doubleValue();
	//				}
	//
	//			}
	//
	//			if (discountPrice.doubleValue() != 0.00
	//					&& (priceForDiscount * eligibleQuantity.intValue()) < discountPrice.doubleValue())
	//			{
	//				iter.remove();
	//				_flagCouldFireMessage = false;
	//			}
	//
	//		}
	//		return _flagCouldFireMessage;
	//	}

	/**
	 * @Description: check discount amount value with product price for discount promotion
	 * @param cart
	 * @param promotionProductList
	 * @param promotionCategoryList
	 * @return
	 */
	public boolean getValidProductListForAmtDiscount(final SessionContext paramSessionContext, final AbstractOrder cart,
			final List<Product> allowedProductList, final Long eligibleQuantity, final Double discountPrice,
			final Map<String, AbstractOrderEntry> validProductUssidMap)
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

			if (CollectionUtils.isNotEmpty(allowedProductList))
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
	public Map<String, Integer> getQualifyingCountForABPromotion(final List<String> eligibleProductList,
			final int totalCountFactor)
	{
		final Map<String, Integer> itemLevelQC = new HashMap<String, Integer>();
		for (final String ussid : eligibleProductList)
		{
			itemLevelQC.put(ussid,
					Integer.valueOf(totalCountFactor)/*
																 * Integer.valueOf(Collections.frequency( eligibleProductList, ussid))
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
					associatedProductList.addAll(
							validProdUssidList.subList(validProdUssidList.indexOf(validProductUssid) + 1, validProdUssidList.size()));
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
								final StockLevelModel stockData = mplStockService.getStockLevelDetail(sellerArticleSKU);
								if (stockData.getAvailable() > 0)

								{
									giftProductDetails.put(sellerArticleSKU, product);
									minmumValObtained = true;
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
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomPromotionOrderEntryAdjustAction
	 */
	//TPR-961
	//	public CustomBuyAgetPercentageDiscountOnBAdjustAction createCustomBuyAgetPercentageDiscountOnBAdjustAction(
	//			final SessionContext ctx, final AbstractOrderEntry entry, final long quantity, final double adjustment)
	//	{
	//		final Map parameters = new HashMap();
	//		parameters.put(MarketplacecommerceservicesConstants.GUID, makeActionGUID());
	//		parameters.put(MarketplacecommerceservicesConstants.AMOUNT, Double.valueOf(adjustment));
	//		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_PRODUCT, entry.getProduct(ctx));
	//		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_NUMBER, entry.getEntryNumber());
	//		parameters.put(MarketplacecommerceservicesConstants.ORDERENTRY_QUANTITY, Long.valueOf(quantity));
	//		return createCustomBuyAgetPercentageDiscountOnBAdjustAction(ctx, parameters);
	//	}


	/**
	 * @param ctx
	 * @param parameters
	 * @return
	 */
	//	private CustomBuyAgetPercentageDiscountOnBAdjustAction createCustomBuyAgetPercentageDiscountOnBAdjustAction(
	//			final SessionContext ctx, final Map attributeValues)
	//
	//	{
	//		try
	//		{
	//			@SuppressWarnings("deprecation")
	//			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
	//					.getComposedType("CustomBuyAgetPercentageDiscountOnBAdjustAction");
	//			return ((CustomBuyAgetPercentageDiscountOnBAdjustAction) type.newInstance(ctx, attributeValues));
	//		}
	//		catch (final JaloGenericCreationException e)
	//		{
	//			final Throwable cause = e.getCause();
	//			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
	//		}
	//		catch (final JaloBusinessException e)
	//		{
	//			throw new JaloSystemException(e, "error creating CustomPromotionOrderEntryAdjustAction : " + e.getMessage(), 0);
	//		}
	//
	//	}

	/**
	 * @Description: For Promotion apportioned Promotion Price BOGO
	 * @param ctx
	 * @param adjustment
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
		//parameters.put(MarketplacecommerceservicesConstants.NONFREE_CONSUMED_ENTRIES, nonFreeConsumed);

		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomBOGOPromoOrderEntryAdjustAction(ctx, parameters);
		}

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
	 * @param validProductUssidMap
	 * @param order
	 * @return true if delivery mode restriction satisfies for A, else false and remove that element from
	 *         validProductListMap
	 */

	public boolean getDelModeRestrEvalForAPromo(final List<AbstractPromotionRestriction> restrictionList,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final AbstractOrder order)
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
					//final CartModel cartModel = cartService.getSessionCart();
					final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order);
					final List<String> restrDeliveryModeList = new ArrayList<String>();

					for (final DeliveryMode deliveryMode : ((DeliveryModePromotionRestriction) restriction)
							.getDeliveryModeDetailsList())
					{
						restrDeliveryModeList.add(deliveryMode.getCode());
					}

					for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
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
	 * @param validProductUssidMap
	 * @param order
	 * @return true if delivery mode restriction satisfies for both A and B, else false
	 */

	public boolean getDelModeRestrEvalForABPromo(final List<AbstractPromotionRestriction> restrictionList,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final AbstractOrder order)
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
					//final CartModel cartModel = cartService.getSessionCart();
					final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order);
					final List<String> restrDeliveryModeList = new ArrayList<String>();

					for (final DeliveryMode deliveryMode : ((DeliveryModePromotionRestriction) restriction)
							.getDeliveryModeDetailsList())
					{
						restrDeliveryModeList.add(deliveryMode.getCode());
					}

					for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
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

	public boolean getDelModeRestrEvalForOrderPromo(final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder order)
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
				//flag = false;	//Unwanted - TPR-969
				if (restriction instanceof DeliveryModePromotionRestriction)
				{
					final List<ProductModel> prodSatisfiesDelModeList = new ArrayList<ProductModel>();
					//final CartModel cartModel = cartService.getSessionCart();
					final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order);
					final List<String> restrDeliveryModeList = new ArrayList<String>();

					for (final DeliveryMode deliveryMode : ((DeliveryModePromotionRestriction) restriction)
							.getDeliveryModeDetailsList())
					{
						restrDeliveryModeList.add(deliveryMode.getCode());
					}

					for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
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

					if (prodSatisfiesDelModeList.size() == abstractOrderModel.getEntries().size())
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
	 * @param qCount
	 * @param order
	 * @return Map<Product, String>
	 */
	public Map<String, String> fetchProductRichAttribute(final Map<String, Integer> qCount, final AbstractOrder order)
	{
		final Map<String, String> productfullfillmentTypeMap = new HashMap<String, String>();
		//final CartModel cartModel = cartService.getSessionCart();
		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order);
		for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
		{
			if (null != entry && !entry.getGiveAway().booleanValue()) // Added for TPR-1702 : Sprint1.0
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
		}
		return productfullfillmentTypeMap;
	}

	/**
	 * @Description: This method is for calculating delivery charges for product and returns product - calculated
	 *               delivery charges after promotion mapping.
	 * @param isPercentageFlag
	 * @param adjustedDeliveryCharge
	 * @param fetchProductRichAttribute
	 * @param order
	 * @return Map<Product, Double>
	 */
	//	public Map<String, Map<String, Double>> updateDeliveryCharges(final boolean isDeliveryFreeFlag,
	//			final boolean isPercentageFlag, final double adjustedDeliveryCharge, final Map<String, Integer> qCount,
	//			final Map<String, String> fetchProductRichAttribute, final AbstractOrder order)
	//	{
	//		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
	//		final Map<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel> prodDelChargeMap = new HashMap<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel>();
	//		//final CartModel cartModel = cartService.getSessionCart();
	//		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order); //TPR-969
	//		double totalDeliveryCostForValidProds = 0.00D;
	//
	//		for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
	//		{
	//			final String selectedUSSID = entry.getSelectedUSSID();
	//			if (qCount.containsKey(selectedUSSID) && null != entry.getMplDeliveryMode() && !entry.getGiveAway().booleanValue())
	//			{
	//				final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
	//				final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
	//				ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
	//				ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
	//				ServicesUtil.validateParameterNotNull(selectedUSSID, "sellerArticleSKU cannot be null");
	//				final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService.getDeliveryCost(
	//						selectedDeliveryModeCode, currencyIsoCode, selectedUSSID);
	//				prodDelChargeMap.put(entry, mplZoneDeliveryModeValueModel);
	//				if (!isPercentageFlag
	//						&& null != mplZoneDeliveryModeValueModel.getValue()
	//						&& (fetchProductRichAttribute.containsKey(entry.getSelectedUSSID())
	//								&& null != fetchProductRichAttribute.get(entry.getSelectedUSSID()) && !fetchProductRichAttribute.get(
	//								entry.getSelectedUSSID()).equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP)))
	//				{
	//					totalDeliveryCostForValidProds += mplZoneDeliveryModeValueModel.getValue().doubleValue()
	//							* qCount.get(selectedUSSID).intValue();
	//				}
	//			}
	//		}
	//		double totalAmtTobeDeduced = 0.00D;
	//		final Iterator iter = prodDelChargeMap.entrySet().iterator();
	//		while (iter.hasNext())
	//		{
	//			final Map.Entry orderEntry = (Map.Entry) iter.next();
	//
	//			final MplZoneDeliveryModeValueModel prodMplZoneDeliveryModeValueModel = (MplZoneDeliveryModeValueModel) orderEntry
	//					.getValue();
	//			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) orderEntry.getKey();
	//			final Map<String, Double> prevCurrDeliveryChargeMap = new HashMap<String, Double>();
	//
	//			if ((fetchProductRichAttribute.containsKey(entry.getSelectedUSSID())
	//					&& null != fetchProductRichAttribute.get(entry.getSelectedUSSID()) && fetchProductRichAttribute.get(
	//					entry.getSelectedUSSID()).equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP))
	//					|| prodMplZoneDeliveryModeValueModel.getValue().doubleValue() == 0.00D)
	//			{
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, Double.valueOf(0));
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, Double.valueOf(0));
	//				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
	//			}
	//			else
	//			{
	//				//Blocked for TISSTRT-1418
	//				//				final double deliveryChargeForEntry = prodMplZoneDeliveryModeValueModel.getValue().doubleValue()
	//				//						* entry.getQuantity().intValue();
	//
	//				final double deliveryChargeForEntry = getMplPromotionHelper().getDeliveryEntryCharge(
	//						prodMplZoneDeliveryModeValueModel.getValue().doubleValue(), entry);
	//				//Modification for TISSTRT-1418 ends
	//
	//				double amtTobeDeduced = 0.00D;
	//				double deliveryChargeAfterPromotion = 0.00D;
	//
	//				if (isPercentageFlag) //For percentage discount
	//				{
	//					amtTobeDeduced = (adjustedDeliveryCharge / 100) * deliveryChargeForEntry;
	//
	//				}
	//				else
	//				//For free delivery and amount discount
	//				{
	//					if (isDeliveryFreeFlag) //For free delivery
	//					{
	//						amtTobeDeduced = deliveryChargeForEntry;
	//					}
	//					else
	//					//For amount discount
	//					{
	//						final double convertedPercentageForAmt = (adjustedDeliveryCharge / totalDeliveryCostForValidProds) * 100;
	//						if (prodDelChargeMap.size() == 1)
	//						{
	//							amtTobeDeduced = adjustedDeliveryCharge - totalAmtTobeDeduced;
	//						}
	//						else
	//						{
	//							amtTobeDeduced = (convertedPercentageForAmt / 100) * deliveryChargeForEntry;
	//							totalAmtTobeDeduced += amtTobeDeduced;
	//						}
	//					}
	//				}
	//
	//				if (deliveryChargeForEntry >= amtTobeDeduced)
	//				{
	//					deliveryChargeAfterPromotion = deliveryChargeForEntry - amtTobeDeduced;
	//				}
	//				else
	//				{
	//					deliveryChargeAfterPromotion = deliveryChargeForEntry;
	//				}
	//
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
	//						Double.valueOf(deliveryChargeForEntry));
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
	//						Double.valueOf(deliveryChargeAfterPromotion));
	//
	//				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
	//				iter.remove();
	//			}
	//		}
	//		return prodPrevCurrDelChargeMap;
	//	}

	/**
	 * @Description: For undoing delivery charges
	 * @param ctx
	 * @param totalAdjustment
	 * @return CustomShippingChargesPromotionAdjustAction
	 */

	//	public Map<String, Map<String, Double>> undoDeliveryCharges(final AbstractOrder order,
	//			final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap, final SessionContext ctx)
	//	{
	//		for (final Map.Entry<String, Map<String, Double>> mapEntry : prodPrevCurrDelChargeMap.entrySet())
	//		{
	//			final Map<String, Double> prevCurrDeliveryChargeMap = mapEntry.getValue();
	//			prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
	//					prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE));
	//			prodPrevCurrDelChargeMap.put(mapEntry.getKey(), prevCurrDeliveryChargeMap);
	//		}
	//		for (final AbstractOrderEntry orderEntry : order.getEntries())
	//		{
	//			String entryUSSID = null;
	//			try
	//			{
	//				entryUSSID = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
	//			}
	//			catch (final JaloInvalidParameterException | JaloSecurityException e)
	//			{
	//				LOG.error(e);
	//			}
	//			if (prodPrevCurrDelChargeMap.containsKey(entryUSSID))
	//			{
	//				final Map<String, Double> prevCurrDeliveryChargeMap = prodPrevCurrDelChargeMap.get(entryUSSID);
	//				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
	//						prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE));
	//				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
	//						prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE));
	//			}
	//
	//		}
	//		return prodPrevCurrDelChargeMap;
	//	}


	/**
	 * @Description: This method is for calculating delivery charges for product and returns product - calculated
	 *               delivery charges after promotion mapping.
	 * @param isPercentageFlag
	 * @param adjustedDeliveryCharge
	 * @param validProductList
	 * @return Map<Product, Double>
	 */
	public Map<String, Map<String, Double>> calcDeliveryCharges(final boolean isDeliveryFreeFlag,
			final double adjustedDeliveryCharge, final String validProductUSSID, final AbstractOrder order,
			final Map<String, Boolean> isProdShippingPromoAppliedMap)
	{
		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
		final Map<AbstractOrderEntryModel, Double> prodDelChargeMap = new HashMap<AbstractOrderEntryModel, Double>();
		//final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order); //TPR-969

		Double currDelCharge = Double.valueOf(0.00D);
		//for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
		for (final AbstractOrderEntry entryJalo : order.getEntries())
		{
			currDelCharge = (Double) entryJalo.getProperty(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE);
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) getModelService().get(entryJalo);
			final String selectedUSSID = entry.getSelectedUSSID();
			if (null != entry.getMplDeliveryMode() && selectedUSSID.equalsIgnoreCase(validProductUSSID))
			{
				final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
				final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
				ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
				ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
				ServicesUtil.validateParameterNotNull(selectedUSSID, "sellerArticleSKU cannot be null");
				final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService
						.getDeliveryCost(selectedDeliveryModeCode, currencyIsoCode, selectedUSSID);
				if (null != isProdShippingPromoAppliedMap && isProdShippingPromoAppliedMap.containsKey(selectedUSSID)
						&& isProdShippingPromoAppliedMap.get(selectedUSSID).booleanValue())
				{
					prodDelChargeMap.put(entry, currDelCharge);
				}
				else
				{
					prodDelChargeMap.put(entry, mplZoneDeliveryModeValueModel.getValue());
				}
			}
		}

		final Iterator iter = prodDelChargeMap.entrySet().iterator();
		while (iter.hasNext())
		{
			final Map.Entry orderEntry = (Map.Entry) iter.next();
			final Double entryLevelDelCharge = (Double) orderEntry.getValue();
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) orderEntry.getKey();
			final Map<String, Double> prevCurrDeliveryChargeMap = new HashMap<String, Double>();

			if (entryLevelDelCharge.doubleValue() == 0.00D)
			{
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, entryLevelDelCharge);
				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, entryLevelDelCharge);
				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
			}
			else
			{
				final double deliveryChargeForEntry = entryLevelDelCharge.doubleValue() * entry.getQuantity().intValue();

				double amtTobeDeduced = 0.00D;
				double deliveryChargeAfterPromotion = 0.00D;
				if (isDeliveryFreeFlag) //For free delivery
				{
					amtTobeDeduced = deliveryChargeForEntry;
				}
				else
				{
					amtTobeDeduced = (adjustedDeliveryCharge / 100) * deliveryChargeForEntry;
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

	public void undoDeliveryCharges(final AbstractOrder order, final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap)
	{

		//		for (final Map.Entry<String, Map<String, Double>> mapEntry : prodPrevCurrDelChargeMap.entrySet())
		//		{
		//			final Map<String, Double> prevCurrDeliveryChargeMap = mapEntry.getValue();
		//			prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
		//					prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE));
		//			prodPrevCurrDelChargeMap.put(mapEntry.getKey(), prevCurrDeliveryChargeMap);
		//		}
		//		for (final AbstractOrderEntry orderEntry : order.getEntries())
		//		{
		//			String entryUSSID = null;
		//			try
		//			{
		//				entryUSSID = (String) orderEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
		//			}
		//			catch (final JaloInvalidParameterException | JaloSecurityException e)
		//			{
		//				LOG.error(e);
		//			}
		//			if (prodPrevCurrDelChargeMap.containsKey(entryUSSID))
		//			{
		//				final Map<String, Double> prevCurrDeliveryChargeMap = prodPrevCurrDelChargeMap.get(entryUSSID);
		//				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
		//						prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE));
		//				orderEntry.setProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
		//						prevCurrDeliveryChargeMap.get(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE));
		//			}
		//
		//		}
		//

		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order);
		for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
		{
			final String selectedUSSID = entry.getSelectedUSSID();

			if (prodPrevCurrDelChargeMap.containsKey(selectedUSSID) && null != entry.getMplDeliveryMode())
			{
				entry.setCurrDelCharge(Double.valueOf(0.00D));
				entry.setPrevDelCharge(Double.valueOf(0.00D));
			}
		}
	}

	/**
	 * For getting total tdelivery charges of eligible products
	 *
	 * @param validProductUssidMap
	 * @param qCountMap
	 * @return totalDeliveryCostForValidProds
	 *
	 */
	public double getTotalDelCostForValidProds(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final Map<String, Integer> qCountMap)
	{
		double totalDeliveryCostForValidProds = 0.00D;

		for (final AbstractOrderEntry entryJalo : validProductUssidMap.values())
		{
			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) getModelService().get(entryJalo);
			final String entryUssid = entry.getSelectedUSSID();
			final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
			final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
			ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
			ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
			ServicesUtil.validateParameterNotNull(entryUssid, "sellerArticleSKU cannot be null");
			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService
					.getDeliveryCost(selectedDeliveryModeCode, currencyIsoCode, entryUssid);

			totalDeliveryCostForValidProds += mplZoneDeliveryModeValueModel.getValue().doubleValue()
					* qCountMap.get(entryUssid).intValue();
		}
		return totalDeliveryCostForValidProds;
	}

	/**
	 * @Description: For populating map of product level shipping info
	 * @param order
	 * @return Map
	 */
	//	public Map<String, Boolean> getProdShippingPromoAppliedMap(final AbstractOrder order)
	//	{
	//		final Map<String, Boolean> isProdShippingPromoAppliedMap = new HashMap<String, Boolean>();
	//
	//		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order); //TPR-969
	//		final List<PromotionResultModel> allPromoResult = new ArrayList<PromotionResultModel>(
	//				abstractOrderModel.getAllPromotionResults());
	//
	//		for (final PromotionResultModel promoResult : allPromoResult)
	//		{
	//			final AbstractPromotionModel appliedPromotion = promoResult.getPromotion();
	//			if (promoResult.getCertainty().floatValue() >= 1.0F
	//					&& null != appliedPromotion
	//					&& (appliedPromotion instanceof BuyAGetPromotionOnShippingChargesModel || appliedPromotion instanceof BuyAandBGetPromotionOnShippingChargesModel))
	//			{
	//				for (final PromotionOrderEntryConsumedModel consumed : promoResult.getConsumedEntries())
	//				{
	//					isProdShippingPromoAppliedMap.put(consumed.getOrderEntry().getSelectedUSSID(), Boolean.TRUE);
	//				}
	//			}
	//
	//
	//			if (prodPrevCurrDelChargeMap.containsKey(selectedUSSID) && null != entry.getMplDeliveryMode())
	//			{
	//				//				final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
	//				//				final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
	//				//				ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
	//				//				ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
	//				//				ServicesUtil.validateParameterNotNull(selectedUSSID, "sellerArticleSKU cannot be null");
	//				//				final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService.getDeliveryCost(
	//				//						selectedDeliveryModeCode, currencyIsoCode, selectedUSSID);
	//				entry.setCurrDelCharge(Double.valueOf(0.00D));
	//				entry.setPrevDelCharge(Double.valueOf(0.00D));
	//			}
	//		}
	//
	//
	//		//return prodPrevCurrDelChargeMap;
	//
	//		return isProdShippingPromoAppliedMap;
	//
	//	}


	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param adjustment
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

		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomShippingChargesPromotionAdjustAction(ctx, parameters);
		}

		return createCustomShippingChargesPromotionAdjustAction(ctx, parameters);
	}

	/**
	 * @Description: For Promotion apportioned Promotion Price
	 * @param ctx
	 * @param adjustment
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


		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomShippingChargesPromotionAdjustAction(ctx, parameters);
		}

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
	//	public boolean isProductExcludedForSeller(final SessionContext paramSessionContext,
	//			final List<AbstractPromotionRestriction> restrictionList, final AbstractOrderEntry entry)
	//	{
	//		boolean flag = false;
	//
	//		try
	//		{
	//			if (null != entry && null != entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID)
	//					&& isExSellerRestrExists(restrictionList))
	//			{
	//				final String ussid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID)
	//						.toString();
	//				final CatalogVersionModel oModel = catalogData();
	//				final List<SellerInformationModel> productSellerData = getSellerBasedPromotionService().fetchSellerInformation(ussid,
	//						oModel);
	//				flag = GenericUtilityMethods.checkExcludeSellerData(restrictionList, productSellerData);
	//			}
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//		}
	//		catch (final Exception e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
	//		}
	//		return flag;
	//	}



	/**
	 * Check if Exclude Seller Restriction Exist
	 *
	 * @param restrictionList
	 * @return flag
	 */
	public boolean isExSellerRestrExists(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(restrictionList))
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				if (restriction instanceof EtailExcludeSellerSpecificRestriction)
				{
					flag = true;
					break;
				}
			}
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
		boolean isPaymentResticted = false;
		if (null == restrictionList || restrictionList.isEmpty())
		{
			flag = true;
		}
		else
		{

			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				//flag = false;	//Unwanted - TPR-969
				if (restriction instanceof PaymentModeSpecificPromotionRestriction)
				{
					isPaymentResticted = true;
					String paymentMode = null;
					String selectedBank = MarketplacecommerceservicesConstants.EMPTY;
					//String selectedNBBank = MarketplacecommerceservicesConstants.EMPTY;//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode Restriction doesn't work
					if (null != ctx)
					{
						paymentMode = ctx.getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODEFORPROMOTION);
						selectedBank = ctx.getAttribute(MarketplacecommerceservicesConstants.BANKFROMBIN);
						//selectedNBBank = ctx.getAttribute(MarketplacecommerceservicesConstants.BANKNAMEFORNETBANKING);//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode Restriction doesn't work
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
									//									else if ((paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING) && (StringUtils
									//											.isNotEmpty(selectedNBBank) && checkBankData(selectedNBBank, restrBanks)))
									//											|| ((paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT)
									//													|| paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT) || paymentMode
									//														.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI)) && (StringUtils
									//													.isNotEmpty(selectedBank) && checkBankData(selectedBank, restrBanks))))
									else if ((paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING)
											|| paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT)
											|| paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT)
											|| paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI))
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
			}



			if (!isPaymentResticted)
			{
				flag = true;
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

		if (isSellerRestrExists(restrictionList) || isExSellerRestrExists(restrictionList))
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
			final List<AbstractOrderEntry> orderEntryList = cart.getEntriesByProduct(product) != null
					? cart.getEntriesByProduct(product) : new ArrayList<AbstractOrderEntry>();

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
			final Map<String, Integer> qCountMap, final String promoCode)
	{
		Set<String> validProdUssidSet = new HashSet<String>();
		Map<String, Integer> stockCountMap = null;
		//int totalstockCount = 0;

		//Check whether Stock level restriction exists
		int stockCount = getStockRestrictionVal(restrictionList);
		boolean sellerFlag = false;

		if (stockCount > 0 && null != validProductUssidTempMap)
		{
			//totalstockCount = stockCount * getStockCount(promoCode);
			stockCount *= getStockCount(promoCode);
			stockCountMap = new HashMap<String, Integer>();
			sellerFlag = getSellerRestrictionVal(restrictionList);
			final Set<String> validSetAfterStockCheck = getValidMapAfterStockLevelRestriction(validProductUssidTempMap, promoCode,
					stockCount, stockCountMap, sellerFlag);
			validProductUssidTempMap.keySet().retainAll(validSetAfterStockCheck);
		}

		List<AbstractOrderEntry> validEntries = null;

		if (MapUtils.isNotEmpty(validProductUssidTempMap))
		{
			validEntries = new ArrayList<AbstractOrderEntry>(validProductUssidTempMap.values());

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

			validProdUssidSet = doConsumeEntries(validEntries, totalEligibleCount, ctx, qCountMap, stockCount, promoCode,
					stockCountMap, sellerFlag);
			//			validProdUssidSet = doConsumeEntries(validEntries, totalEligibleCount, ctx, qCountMap, stockCount, promoCode,
			//					stockCountMap, sellerFlag);
		}

		return validProdUssidSet;
	}

	/**
	 * @param promoCode
	 * @return count
	 */
	private int getStockCount(final String promoCode)
	{
		int count = 1;

		try
		{
			final ProductPromotionModel oModel = getPromoDetails(promoCode);
			if (oModel instanceof BuyAPercentageDiscountModel)
			{
				count = ((BuyAPercentageDiscountModel) oModel).getQuantity().intValue();
			}
			else if (oModel instanceof BuyXItemsofproductAgetproductBforfreeModel)
			{
				count = ((BuyXItemsofproductAgetproductBforfreeModel) oModel).getQualifyingCount().intValue();
			}
			else if (oModel instanceof BuyABFreePrecentageDiscountModel)
			{
				count = ((BuyABFreePrecentageDiscountModel) oModel).getQuantity().intValue();
			}
			//TISSQAUAT-476 fix starts here
			else if (oModel instanceof BuyAGetPrecentageDiscountCashbackModel)
			{
				count = ((BuyAGetPrecentageDiscountCashbackModel) oModel).getQuantity().intValue();
			}
			//TISSQAUAT-476 fix ends here
			//PR-13 starts here
			else if (oModel instanceof FixedPricePromotionModel)
			{
				count = ((FixedPricePromotionModel) oModel).getQuantity().intValue();
			}
			//PR-13 ends here
		}
		catch (final Exception exception)
		{
			LOG.debug("Error in Fetching of Qualifying Count. Setting it as 1");
			count = 1;
		}

		return count;
	}

	/**
	 * @Description: For checking whether seller restriction is attached with the promotion
	 * @param restrictionList
	 * @return boolean
	 */

	public boolean isSellerRestrExists(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean isSellerRestr = false;
		if (CollectionUtils.isNotEmpty(restrictionList))
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
	 * @param order
	 * @return valid ussid mapping
	 */

	public Map<String, Integer> getvalidProdQCForOrderShippingPromotion(final List<DeliveryMode> deliveryModeDetailsList,
			final AbstractOrder order)
	{
		final List<String> deliveryModeCodeList = new ArrayList<String>();
		for (final DeliveryMode deliveryMode : deliveryModeDetailsList)
		{
			deliveryModeCodeList.add(deliveryMode.getCode());
		}
		final Map<String, Integer> validProdQCountMap = new HashMap<String, Integer>();
		//final CartModel cartModel = cartService.getSessionCart();
		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order); //TPR-969

		final List<AbstractOrderEntryModel> entries = (abstractOrderModel != null) ? abstractOrderModel.getEntries()
				: new ArrayList<AbstractOrderEntryModel>();

		//		if (CollectionUtils.isNotEmpty(abstractOrderModel.getEntries()))
		//		{
		for (final AbstractOrderEntryModel entryModel : entries)
		{
			if (null != entryModel && !entryModel.getGiveAway().booleanValue()) // Added for TPR-1702 : Sprint 1.0
			{
				if (null != entryModel.getMplDeliveryMode() && null != entryModel.getMplDeliveryMode().getDeliveryMode()
						&& null != entryModel.getMplDeliveryMode().getDeliveryMode().getCode())
				{
					final String selectedDeliveryMode = entryModel.getMplDeliveryMode().getDeliveryMode().getCode();
					if (deliveryModeCodeList.contains(selectedDeliveryMode))
					{
						validProdQCountMap.put(entryModel.getSelectedUSSID(), Integer.valueOf(entryModel.getQuantity().intValue()));
					}
				}
			}
		}
		//}


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
			final SessionContext ctx, final Map<String, Integer> qCountMap, final int stockCount, final String promoCode,
			final Map<String, Integer> stockCountMap, final boolean sellerFlag)
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
				long consumeCount = (entryTotalQty <= totalEligibleCount) ? entryTotalQty : totalEligibleCount;
				//Added for stock level restriction
				consumeCount = (stockCount > 0)
						? getCountForStock(stockCount, stockCountMap, (int) consumeCount, sortedEntry, sellerFlag, ctx) : consumeCount;
				totalEligibleCount = (stockCount > 0)
						? getCountForStock(stockCount, stockCountMap, totalEligibleCount, sortedEntry, sellerFlag, ctx)
						: totalEligibleCount;

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
	 * @Description: This method is for getting consumed entries for limited stock restriction
	 * @param
	 * @param
	 * @return
	 */
	//	private long getConsumeCountForLimitedStock(final int stockCount, final long consumeCount, final String selectedUSSID,
	//			final Map<String, Integer> stockMap)
	//	{
	//		long limitedStockConsumedCount = 0L;
	//
	//		if (MapUtils.isNotEmpty(stockMap) && null != stockMap.get(selectedUSSID))
	//		{
	//			if ((stockCount - stockMap.get(selectedUSSID).intValue()) > 0)
	//			{
	//				final int remainingStock = stockCount - stockMap.get(selectedUSSID).intValue();
	//				limitedStockConsumedCount = (consumeCount <= remainingStock) ? consumeCount : remainingStock;
	//			}
	//
	//		}
	//		else if (MapUtils.isEmpty(stockMap))
	//		{
	//			limitedStockConsumedCount = (consumeCount <= stockCount) ? consumeCount : stockCount;
	//		}
	//
	//		return limitedStockConsumedCount;
	//	}

	/**
	 * @Description: This method is for getting Eligible Count for limited stock restriction
	 * @param
	 * @param
	 * @return
	 */
	//	private int getTotalEligibleCountForStock(final int stockCount, final Map<String, Integer> stockMap,
	//			final int totalEligibleCount, final String selectedUSSID)
	//	{
	//		int totalEligibleCountForStock = 0;
	//		if (MapUtils.isNotEmpty(stockMap) && null != stockMap.get(selectedUSSID))
	//		{
	//			if ((stockCount - stockMap.get(selectedUSSID).intValue()) > 0)
	//			{
	//				final int remainingStock = stockCount - stockMap.get(selectedUSSID).intValue();
	//				totalEligibleCountForStock = (totalEligibleCount <= remainingStock) ? totalEligibleCount : remainingStock;
	//			}
	//
	//		}
	//		else if (MapUtils.isEmpty(stockMap))
	//		{
	//			totalEligibleCountForStock = (totalEligibleCount <= stockCount) ? totalEligibleCount : stockCount;
	//		}
	//
	//		return totalEligibleCountForStock;
	//	}

	/**
	 * @Description: This method is for getting Eligible Count for limited stock restriction
	 * @param
	 * @param
	 * @return
	 */
	private int getCountForStock(final int stockCount, final Map<String, Integer> stockMap, final int totalCount,
			final AbstractOrderEntry entry, final boolean sellerFlag, final SessionContext ctx)
	{
		int totalEligibleCountForStock = 0;
		String toCheckWith = null;

		if (sellerFlag)
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
			toCheckWith = selectedUSSID;
		}
		else
		{
			toCheckWith = entry.getProduct().getCode();
		}

		if (MapUtils.isNotEmpty(stockMap) && null != stockMap.get(toCheckWith))
		{
			if ((stockCount - stockMap.get(toCheckWith).intValue()) > 0)
			{
				final int remainingStock = stockCount - stockMap.get(toCheckWith).intValue();
				totalEligibleCountForStock = (totalCount <= remainingStock) ? totalCount : remainingStock;
			}

		}
		else if (MapUtils.isEmpty(stockMap))
		{
			totalEligibleCountForStock = (totalCount <= stockCount) ? totalCount : stockCount;
		}
		else if (MapUtils.isNotEmpty(stockMap) && !stockMap.containsKey(toCheckWith))
		{
			totalEligibleCountForStock = (totalCount <= stockCount) ? totalCount : stockCount;
		}

		return totalEligibleCountForStock;
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
			final List<AbstractPromotionRestriction> restrictionList, final String promoCode)
	{
		final Map<String, Integer> validUssidList = new HashMap<String, Integer>();
		final int totalFactorCount = totalCount / (int) eligibleQty;
		final int totalEligibleCount = totalFactorCount * (int) eligibleQty;

		validProductUssidMap.keySet().retainAll(populateSortedValidProdUssidMap(validProductUssidMap, totalEligibleCount,
				paramSessionContext, restrictionList, validUssidList, promoCode));
		return validUssidList;
	}

	/**
	 * @Description : Returns Valid Product List
	 * @param cart
	 * @param paramSessionContext
	 * @return Map<Product, Integer>
	 */
	//	protected Map<String, AbstractOrderEntry> getValidProdListForBuyXofAPromo(final AbstractOrder cart,
	//			final SessionContext paramSessionContext, final List<Product> promotionProductList,
	//			final List<Category> promotionCategoryList, final List<AbstractPromotionRestriction> restrictionList,
	//			final List<Product> excludedProductList, final List<String> excludeManufactureList, final List<String> sellerIDData,
	//			final Map<AbstractOrderEntry, String> eligibleProductMap)
	//	{
	//		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
	//		boolean applyPromotion = false;
	//		boolean brandFlag = false;
	//		boolean sellerFlag = false;
	//		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
	//		boolean isFreebie = false;
	//
	//		//final int productQty = 0;
	//		for (final AbstractOrderEntry entry : cart.getEntries())
	//		{
	//			applyPromotion = false;
	//			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);
	//
	//			if (!isFreebie)
	//			{
	//				final Product product = entry.getProduct();
	//
	//				//excluded product check
	//				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
	//						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
	//						|| isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
	//				{
	//					continue;
	//				}
	//
	//				//checking product is a valid product for promotion
	//				if (!promotionProductList.isEmpty())
	//				{
	//					if (promotionProductList.contains(product))
	//					{
	//						applyPromotion = true;
	//						brandFlag = true;
	//						sellerFlag = checkSellerData(paramSessionContext, restrictionList, entry);
	//					}
	//				}
	//				else if (promotionProductList.isEmpty() && !promotionCategoryList.isEmpty())
	//				//checking product category is permitted by promotion category or not
	//				{
	//					final List<String> productCategoryList = getcategoryList(product, paramSessionContext);
	//					applyPromotion = GenericUtilityMethods.productExistsIncat(promotionCategoryList, productCategoryList);
	//					brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
	//					sellerFlag = checkSellerData(paramSessionContext, restrictionList, entry);
	//				}
	//
	//				if (applyPromotion && brandFlag && sellerFlag)
	//				{
	//					sellerID = getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
	//					validProductUssidMap.putAll(populateValidProductUssidMap(product, cart, restrictionList, paramSessionContext,
	//							entry));
	//					if (sellerIDData != null && eligibleProductMap != null)
	//					{
	//						sellerIDData.add(sellerID);
	//						eligibleProductMap.put(entry, sellerID);
	//					}
	//				}
	//			}
	//		}
	//
	//		return validProductUssidMap;
	//	}

	/**
	 * @Description : Returns Valid Product List
	 * @param cart
	 * @param paramSessionContext
	 * @return Map<Product, Integer>
	 */
	protected Map<String, AbstractOrderEntry> getValidProdListForBuyXofA(final AbstractOrder cart,
			final SessionContext paramSessionContext, final List<Product> allowedProductList,
			final List<AbstractPromotionRestriction> restrictionList, final List<String> sellerIDData,
			final Map<AbstractOrderEntry, String> eligibleProductMap)
	{
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
		boolean isFreebie = false;

		//final int productQty = 0;
		for (final AbstractOrderEntry entry : cart.getEntries())
		{
			//boolean brandFlag = false;
			boolean sellerFlag = false;
			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);

			if (!isFreebie)
			{
				final Product product = entry.getProduct();

				//excluded product check
				//				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
				//						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
				//						|| isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
				//				if (isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
				//				{
				//					continue;
				//				}

				//checking product is a valid product for promotion
				if (CollectionUtils.isNotEmpty(allowedProductList) && allowedProductList.contains(product))
				{
					//brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
					sellerFlag = checkSellerData(paramSessionContext, restrictionList, entry);
				}

				//				if (brandFlag && sellerFlag)
				if (sellerFlag)
				{
					sellerID = getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
					validProductUssidMap
							.putAll(populateValidProductUssidMap(product, cart, restrictionList, paramSessionContext, entry));
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
	 * @Description : Returns Valid Product List
	 * @param cart
	 * @param paramSessionContext
	 * @return Map<Product, Integer>
	 */
	//	protected Map<String, AbstractOrderEntry> getValidProdListForBuyXofA(final AbstractOrder cart,
	//			final SessionContext paramSessionContext, final List<Product> allowedProductList,
	//			final List<AbstractPromotionRestriction> restrictionList, final List<Product> excludedProductList,
	//			final List<String> excludeManufactureList, final List<String> sellerIDData,
	//			final Map<AbstractOrderEntry, String> eligibleProductMap)
	//	{
	//		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
	//		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
	//		boolean isFreebie = false;
	//
	//		//final int productQty = 0;
	//		for (final AbstractOrderEntry entry : cart.getEntries())
	//		{
	//			boolean brandFlag = false;
	//			boolean sellerFlag = false;
	//			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);
	//
	//			if (!isFreebie)
	//			{
	//				final Product product = entry.getProduct();
	//
	//				//excluded product check
	//				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
	//						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
	//						|| isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
	//				{
	//					continue;
	//				}
	//
	//				//checking product is a valid product for promotion
	//				if (CollectionUtils.isNotEmpty(allowedProductList) && allowedProductList.contains(product))
	//				{
	//					brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
	//					sellerFlag = checkSellerData(paramSessionContext, restrictionList, entry);
	//				}
	//
	//				if (brandFlag && sellerFlag)
	//				{
	//					sellerID = getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
	//					validProductUssidMap.putAll(populateValidProductUssidMap(product, cart, restrictionList, paramSessionContext,
	//							entry));
	//					if (sellerIDData != null && eligibleProductMap != null)
	//					{
	//						sellerIDData.add(sellerID);
	//						eligibleProductMap.put(entry, sellerID);
	//					}
	//				}
	//			}
	//		}
	//
	//		return validProductUssidMap;
	//	}

	/**
	 * @Description : Checks For Manufacturer Based Restrictions
	 * @param :
	 *           SessionContext arg0,PromotionEvaluationContext arg1
	 * @return : flag
	 */
	public boolean checkMinimumBrandAmount(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final List<AbstractPromotionRestriction> restrictionList)
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
					MarketplacecommerceservicesConstants.MINIMUM_AMOUNT) != null
							? ((Double) productPromotion.getProperty(ctx, MarketplacecommerceservicesConstants.MINIMUM_AMOUNT))
									.doubleValue()
							: 0.00D;
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
	 * @param :
	 *           SessionContext paramSessionContext ,Map<Product, Integer> validProductList , AbstractOrder cart,int
	 *           totalCount,double discountPriceValue
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
	public int getFreeGiftCount(final String key, final Map<AbstractOrderEntry, String> eligibleProductMap, final int count,
			final Map<String, Integer> validProductList)
	{
		LOG.debug("ValidProductList" + validProductList);
		int giftCount = 0;
		int quantity = 0;
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
								//quantity = quantity + validProductList.get(entry.getValue()).intValue();
								quantity = quantity + (entry.getKey().getQuantity().intValue());
								//giftCount = giftCount + (entry.getKey().getQuantity().intValue() / count);
							}
						}
					}
				}

				//Newly Added Code
				if (quantity > 0)
				{
					giftCount = quantity / count;
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
								//TISSIT-1906
								giftProductDetails.put(seller.getSellerArticleSKU(), product);
								//								final List<StockLevelModel> stockData = mplStockService.getStockLevelDetail(seller.getSellerArticleSKU());
								//								for (final StockLevelModel stockModel : stockData)
								//								{
								//									if (stockModel.getAvailable() > 0)
								//									{
								//										giftProductDetails.put(seller.getSellerArticleSKU(), product);
								//									}
								//								}
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

		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomPromotionOrderAddFreeGiftAction(ctx, parameters);
		}

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
	 * @param :
	 *           validProductUssidMap, validProductList
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


	//PR-13 starts here
	/**
	 * @Description : This calculates the total valid product price
	 * @param :
	 *           validProductUssidMap, validProductList
	 * @return :double
	 */
	public double getTotalValidProdFixedPrice(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final Map<String, Integer> validProductList, final double fixedUnitPrice)
	{
		double totalValidProdFixedPrice = 0.0D;
		for (final Map.Entry<String, AbstractOrderEntry> mapEntry : validProductUssidMap.entrySet())
		{
			final String validUssid = mapEntry.getKey();
			totalValidProdFixedPrice += (fixedUnitPrice * validProductList.get(validUssid).intValue());
		}
		return totalValidProdFixedPrice;
	}

	//PR-13 ends here


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
			throw new PromotionException(
					"Cannot remove " + quantity + " items.  There is not a sufficient quantity of this product remaining.");
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

	//	public Map<String, Map<String, Double>> calcDeliveryCharges(final boolean isDeliveryFreeFlag,
	//			final double adjustedDeliveryCharge, final String validProductUSSID, final AbstractOrder order,
	//			final Map<String, Boolean> isProdShippingPromoAppliedMap)
	//	{
	//		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
	//		final Map<AbstractOrderEntryModel, Double> prodDelChargeMap = new HashMap<AbstractOrderEntryModel, Double>();
	//		//final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order); //TPR-969
	//
	//		Double currDelCharge = Double.valueOf(0.00D);
	//		//for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
	//		for (final AbstractOrderEntry entryJalo : order.getEntries())
	//		{
	//			currDelCharge = (Double) entryJalo.getProperty(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE);
	//			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) getModelService().get(entryJalo);
	//			final String selectedUSSID = entry.getSelectedUSSID();
	//			if (null != entry.getMplDeliveryMode() && selectedUSSID.equalsIgnoreCase(validProductUSSID))
	//			{
	//				final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
	//				final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
	//				ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
	//				ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
	//				ServicesUtil.validateParameterNotNull(selectedUSSID, "sellerArticleSKU cannot be null");
	//				final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService.getDeliveryCost(
	//						selectedDeliveryModeCode, currencyIsoCode, selectedUSSID);
	//				if (null != isProdShippingPromoAppliedMap && isProdShippingPromoAppliedMap.containsKey(selectedUSSID)
	//						&& isProdShippingPromoAppliedMap.get(selectedUSSID).booleanValue())
	//				{
	//					prodDelChargeMap.put(entry, currDelCharge);
	//				}
	//				else
	//				{
	//					prodDelChargeMap.put(entry, mplZoneDeliveryModeValueModel.getValue());
	//				}
	//			}
	//		}
	//
	//		final Iterator iter = prodDelChargeMap.entrySet().iterator();
	//		while (iter.hasNext())
	//		{
	//			final Map.Entry orderEntry = (Map.Entry) iter.next();
	//			final Double entryLevelDelCharge = (Double) orderEntry.getValue();
	//			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) orderEntry.getKey();
	//			final Map<String, Double> prevCurrDeliveryChargeMap = new HashMap<String, Double>();
	//
	//			if (entryLevelDelCharge.doubleValue() == 0.00D)
	//			{
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, entryLevelDelCharge);
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, entryLevelDelCharge);
	//				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
	//			}
	//			else
	//			{
	//				//double deliveryChargeForEntry = 0.00D;
	//
	//				//				if (isCartShippingPromo && isProdShippingPromoApplied)
	//				//				{
	//				//					deliveryChargeForEntry = currDelCharge.doubleValue();
	//				//				}
	//				//				else
	//				//				{
	//				//					deliveryChargeForEntry = entryLevelDelCharge.doubleValue() * entry.getQuantity().intValue();
	//				//				}
	//
	//				final double deliveryChargeForEntry = entryLevelDelCharge.doubleValue() * entry.getQuantity().intValue();
	//
	//				double amtTobeDeduced = 0.00D;
	//				double deliveryChargeAfterPromotion = 0.00D;
	//				if (isDeliveryFreeFlag) //For free delivery
	//				{
	//					amtTobeDeduced = deliveryChargeForEntry;
	//				}
	//				else
	//				{
	//					amtTobeDeduced = (adjustedDeliveryCharge / 100) * deliveryChargeForEntry;
	//				}
	//
	//
	//				//				if (isPercentageFlag) //For percentage discount
	//				//				{
	//				//					amtTobeDeduced = (adjustedDeliveryCharge / 100) * deliveryChargeForEntry;
	//				//
	//				//				}
	//				//				else
	//				//				//For free delivery and amount discount
	//				//				{
	//				//					if (isDeliveryFreeFlag) //For free delivery
	//				//					{
	//				//						amtTobeDeduced = deliveryChargeForEntry;
	//				//					}
	//				//					else
	//				//					//For amount discount
	//				//					{
	//				//						final double convertedPercentageForAmt = (adjustedDeliveryCharge / totalDelCostForValidProds) * 100;
	//				//						if (prodDelChargeMap.size() == 1)
	//				//						{
	//				//							amtTobeDeduced = adjustedDeliveryCharge - totalAmtTobeDeduced;
	//				//						}
	//				//						else
	//				//						{
	//				//							amtTobeDeduced = (convertedPercentageForAmt / 100) * deliveryChargeForEntry;
	//				//							totalAmtTobeDeduced += amtTobeDeduced;
	//				//						}
	//				//					}
	//				//				}
	//
	//				if (deliveryChargeForEntry >= amtTobeDeduced)
	//				{
	//					deliveryChargeAfterPromotion = deliveryChargeForEntry - amtTobeDeduced;
	//				}
	//				else
	//				{
	//					deliveryChargeAfterPromotion = deliveryChargeForEntry;
	//				}
	//
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
	//						Double.valueOf(deliveryChargeForEntry));
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
	//						Double.valueOf(deliveryChargeAfterPromotion));
	//
	//				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
	//				//iter.remove();
	//			}
	//		}
	//		return prodPrevCurrDelChargeMap;
	//	}


	//	public Map<String, Map<String, Double>> calcDeliveryCharges(final boolean isDeliveryFreeFlag, final boolean isPercentageFlag,
	//			final double adjustedDeliveryCharge, final Map<String, Integer> qCount, final String validProductUSSID,
	//			final AbstractOrder order)
	//	{
	//		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
	//		final Map<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel> prodDelChargeMap = new HashMap<AbstractOrderEntryModel, MplZoneDeliveryModeValueModel>();
	//		//final CartModel cartModel = cartService.getSessionCart();
	//		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order); //TPR-969
	//		double totalDeliveryCostForValidProds = 0.00D;
	//
	//		for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
	//		{
	//			final String selectedUSSID = entry.getSelectedUSSID();
	//			if (qCount.containsKey(selectedUSSID) && null != entry.getMplDeliveryMode()
	//					&& selectedUSSID.equalsIgnoreCase(validProductUSSID))
	//			{
	//				final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
	//				final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
	//				ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
	//				ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
	//				ServicesUtil.validateParameterNotNull(selectedUSSID, "sellerArticleSKU cannot be null");
	//				final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService.getDeliveryCost(
	//						selectedDeliveryModeCode, currencyIsoCode, selectedUSSID);
	//				prodDelChargeMap.put(entry, mplZoneDeliveryModeValueModel);
	//				if (!isPercentageFlag && null != mplZoneDeliveryModeValueModel.getValue())
	//				{
	//					totalDeliveryCostForValidProds += mplZoneDeliveryModeValueModel.getValue().doubleValue()
	//							* qCount.get(selectedUSSID).intValue();
	//				}
	//			}
	//		}
	//		double totalAmtTobeDeduced = 0.00D;
	//		final Iterator iter = prodDelChargeMap.entrySet().iterator();
	//		while (iter.hasNext())
	//		{
	//			final Map.Entry orderEntry = (Map.Entry) iter.next();
	//
	//			final MplZoneDeliveryModeValueModel prodMplZoneDeliveryModeValueModel = (MplZoneDeliveryModeValueModel) orderEntry
	//					.getValue();
	//			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) orderEntry.getKey();
	//			final Map<String, Double> prevCurrDeliveryChargeMap = new HashMap<String, Double>();
	//
	//			if (prodMplZoneDeliveryModeValueModel.getValue().doubleValue() == 0.00D)
	//			{
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
	//						prodMplZoneDeliveryModeValueModel.getValue());
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
	//						prodMplZoneDeliveryModeValueModel.getValue());
	//				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
	//			}
	//			else
	//			{
	//				final double deliveryChargeForEntry = prodMplZoneDeliveryModeValueModel.getValue().doubleValue()
	//						* entry.getQuantity().intValue();
	//				double amtTobeDeduced = 0.00D;
	//				double deliveryChargeAfterPromotion = 0.00D;
	//
	//				if (isPercentageFlag) //For percentage discount
	//				{
	//					amtTobeDeduced = (adjustedDeliveryCharge / 100) * deliveryChargeForEntry;
	//
	//				}
	//				else
	//				//For free delivery and amount discount
	//				{
	//					if (isDeliveryFreeFlag) //For free delivery
	//					{
	//						amtTobeDeduced = deliveryChargeForEntry;
	//					}
	//					else
	//					//For amount discount
	//					{
	//						final double convertedPercentageForAmt = (adjustedDeliveryCharge / totalDeliveryCostForValidProds) * 100;
	//						if (prodDelChargeMap.size() == 1)
	//						{
	//							amtTobeDeduced = adjustedDeliveryCharge - totalAmtTobeDeduced;
	//						}
	//						else
	//						{
	//							amtTobeDeduced = (convertedPercentageForAmt / 100) * deliveryChargeForEntry;
	//							totalAmtTobeDeduced += amtTobeDeduced;
	//						}
	//					}
	//				}
	//
	//				if (deliveryChargeForEntry >= amtTobeDeduced)
	//				{
	//					deliveryChargeAfterPromotion = deliveryChargeForEntry - amtTobeDeduced;
	//				}
	//				else
	//				{
	//					deliveryChargeAfterPromotion = deliveryChargeForEntry;
	//				}
	//
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE,
	//						Double.valueOf(deliveryChargeForEntry));
	//				prevCurrDeliveryChargeMap.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE,
	//						Double.valueOf(deliveryChargeAfterPromotion));
	//
	//				prodPrevCurrDelChargeMap.put(entry.getSelectedUSSID(), prevCurrDeliveryChargeMap);
	//				iter.remove();
	//			}
	//		}
	//		return prodPrevCurrDelChargeMap;
	//	}




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
	//	protected Map<String, AbstractOrderEntry> getValidProdListForBOGO(final AbstractOrder cart,
	//			final SessionContext paramSessionContext, final List<Product> promotionProductList,
	//			final List<Category> promotionCategoryList, final List<AbstractPromotionRestriction> restrictionList,
	//			final List<Product> excludedProductList, final List<String> excludeManufactureList, final List<String> sellerIDData,
	//			final Map<AbstractOrderEntry, String> eligibleProductMap)
	//	{
	//		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
	//		boolean applyPromotion = false;
	//		boolean brandFlag = false;
	//		boolean sellerFlag = false;
	//		String sellerID = MarketplacecommerceservicesConstants.EMPTY;
	//		boolean isFreebie = false;
	//
	//		//final int productQty = 0;
	//		for (final AbstractOrderEntry entry : cart.getEntries())
	//		{
	//			applyPromotion = false;
	//			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);
	//
	//			if (!isFreebie)
	//			{
	//				final Product product = entry.getProduct();
	//
	//				//excluded product check
	//				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
	//						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
	//						|| isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
	//				{
	//					continue;
	//				}
	//
	//				//checking product is a valid product for promotion
	//				if (!promotionProductList.isEmpty())
	//				{
	//					if (promotionProductList.contains(product))
	//					{
	//						applyPromotion = true;
	//						brandFlag = true;
	//						sellerFlag = checkSellerBOGOData(paramSessionContext, restrictionList, entry);
	//					}
	//				}
	//				else if (promotionProductList.isEmpty() && !promotionCategoryList.isEmpty())
	//				//checking product category is permitted by promotion category or not
	//				{
	//					final List<String> productCategoryList = getcategoryList(product, paramSessionContext);
	//					applyPromotion = GenericUtilityMethods.productExistsIncat(promotionCategoryList, productCategoryList);
	//					brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
	//					sellerFlag = checkSellerBOGOData(paramSessionContext, restrictionList, entry);
	//				}
	//
	//				if (applyPromotion && brandFlag && sellerFlag)
	//				{
	//					sellerID = getSellerID(paramSessionContext, restrictionList, entry);//Gets the Seller ID of the Primary Promotion Product
	//					validProductUssidMap.putAll(populateValidProductUssidMap(product, cart, restrictionList, paramSessionContext,
	//							entry));
	//					if (sellerIDData != null && eligibleProductMap != null)
	//					{
	//						sellerIDData.add(sellerID);
	//						eligibleProductMap.put(entry, sellerID);
	//					}
	//				}
	//			}
	//		}
	//
	//		return validProductUssidMap;
	//	}


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
					associatedProductList.addAll(
							validProdUssidList.subList(validProdUssidList.indexOf(validProductUssid) + 1, validProdUssidList.size()));
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

		if (isCachingAllowed(ctx).booleanValue())
		{
			return createCachedCustomPromotionOrderAddFreeGiftAction(ctx, parameters);
		}

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

	/**
	 * Exclude Brand Data
	 *
	 * @param excludedManufactureList
	 * @param product
	 * @return
	 */
	public boolean excludeBrandDataCheck(final List<String> excludedManufactureList, final Product product)
	{
		//final long startTime = System.currentTimeMillis();

		//Code Modified for  TISPT-148
		boolean isExcludeBrand = false;
		String brandName = MarketplacecommerceservicesConstants.EMPTY;
		try
		{ //final ProductModel productModel = productService.getProductForCode(product.getCode());
			final List<Category> categoryList = (List<Category>) product.getAttribute("supercategories");
			if (CollectionUtils.isNotEmpty(categoryList))
			{
				for (final Category category : categoryList)
				{
					if (category.getCode().startsWith("MBH"))
					{
						brandName = category.getName();
						break;
					}
				}
			}

			if (CollectionUtils.isNotEmpty(excludedManufactureList) && StringUtils.isNotEmpty(brandName))
			{
				for (final String manufacturer : excludedManufactureList)
				{
					if (manufacturer.equalsIgnoreCase(brandName))
					{
						isExcludeBrand = true;
						break;
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

		//		final long endTime = System.currentTimeMillis();
		//		LOG.info("*******************New TIME DIFFERENCE*****************************************************************"
		//				+ (endTime - startTime));

		return isExcludeBrand;

	}

	/**
	 *
	 * TPR-970 changes
	 *
	 * @Description: This is for validating pincode specific restriction against order level
	 * @param restrictionList
	 * @param order
	 * @return true
	 */

	public boolean checkPincodeSpecificRestriction(final List<AbstractPromotionRestriction> restrictionList,
			final AbstractOrder order)
	{
		boolean flag = false;
		boolean isPinCodeRestrictionPresent = false;
		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order);

		if (CollectionUtils.isEmpty(restrictionList))
		{
			return true;
		}
		else if (CollectionUtils.isNotEmpty(restrictionList) && null != abstractOrderModel)
		{
			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				if (restriction instanceof MplPincodeSpecificRestriction)
				{
					isPinCodeRestrictionPresent = true;
					final List<State> includedStates = ((MplPincodeSpecificRestriction) restriction).getStates();
					final List<City> excudedCity = ((MplPincodeSpecificRestriction) restriction).getCities();
					if (CollectionUtils.isEmpty(includedStates) && CollectionUtils.isEmpty(excudedCity))
					{
						flag = true;
					}
					else if (excudedCity.isEmpty())
					{
						flag = isAppliedPinCodeStatesIncludes(includedStates, abstractOrderModel.getStateForPincode());
					}
					else
					{
						final boolean isValid = ((MplPincodeSpecificRestriction) restriction).isIncludeCities().booleanValue();
						flag = checkForCityRestriction(isValid, excudedCity, includedStates, abstractOrderModel.getCityForPincode(),
								abstractOrderModel.getStateForPincode());
					}

				}
			}

			if (!isPinCodeRestrictionPresent)
			{
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * TPR-970 changes check for city restriction against a particular pincode
	 *
	 * @param isValid
	 * @param includedStates
	 * @param cityName
	 * @param state
	 * @param isCityIncluded
	 *
	 */
	private boolean checkForCityRestriction(final boolean isValid, final List<City> excudedCity, final List<State> includedStates,
			final String cityName, final String state)
	{
		boolean isCityIncluded = false;
		//boolean flag = false;
		for (final City city : excudedCity)
		{
			if (isValid)
			{
				if (city.getCityName().equalsIgnoreCase(cityName))
				{
					isCityIncluded = true;
					break;
				}
			}
			else
			{
				if (city.getCityName() != cityName)
				{
					isCityIncluded = true;
					break;
				}
			}

		}
		return isCityIncluded;

	}


	/**
	 * consume entries based on stock
	 */
	/**
	 * TPR-965
	 *
	 * @param validEntries
	 * @param ctx
	 * @param qCountMap
	 * @param code
	 * @return validProdUssidSet
	 */
	public Set<String> doConsumeEntriesForStockPromo(final List<AbstractOrderEntry> validEntries, final int stockLevel,
			final SessionContext ctx, final Map<String, Integer> qCountMap, final String code)
	{

		final Set<String> validProdUssidSet = new HashSet<String>();

		for (int i = 0; i < validEntries.size(); i++)
		{
			final AbstractOrderEntry sortedEntry = validEntries.get(i);
			final int entryTotalQty = sortedEntry.getQuantity().intValue();
			//	if (stockLevel > 0)
			//			{
			String selectedUSSID = null;
			try
			{
				selectedUSSID = (String) sortedEntry.getAttribute(ctx, MarketplacecommerceservicesConstants.SELECTEDUSSID);
			}
			catch (JaloInvalidParameterException | JaloSecurityException e)
			{
				LOG.error(e);
			}
			long consumeCount = 0L;
			validProdUssidSet.add(selectedUSSID);
			final String ussid = MarketplacecommerceservicesConstants.INVERTED_COMMA + selectedUSSID
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA;
			final Map<String, Integer> stockMap = stockPromoCheckService.getCumulativeStockMap(ussid, code, true);
			final Integer stockQuantity = stockMap.get(selectedUSSID);

			if (stockMap.isEmpty())
			{
				consumeCount = (entryTotalQty <= stockLevel) ? entryTotalQty : stockLevel;
			}
			else if (stockLevel > stockQuantity.intValue())
			{
				consumeCount = (entryTotalQty > (stockLevel - stockQuantity.intValue())) ? (stockLevel - stockQuantity.intValue())
						: entryTotalQty;
			}
			if (qCountMap != null)
			{
				qCountMap.put(selectedUSSID, Integer.valueOf((int) consumeCount));
			}
			//		stockLevel -= consumeCount;
			//	}
		}
		return validProdUssidSet;
	}

	/**
	 * TPR-965
	 *
	 * @param validProductUssidMap
	 * @param eligibleQty
	 * @param paramSessionContext
	 * @param restrictionList
	 * @param code
	 * @return validUssidList
	 */
	Map<String, Integer> getProductUssidMapForStockPromo(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final int stockLevelCount, final SessionContext paramSessionContext,
			final List<AbstractPromotionRestriction> restrictionList, final String code)
	{
		final Map<String, Integer> validUssidList = new HashMap<String, Integer>();
		//		final int totalFactorCount = totalCount / (int) eligibleQty;
		//		final int totalEligibleCount = totalFactorCount * (int) eligibleQty;

		validProductUssidMap.keySet().retainAll(populateStockOfSortedValidProdUssidMap(validProductUssidMap, stockLevelCount,
				paramSessionContext, restrictionList, validUssidList, code, true));
		return validUssidList;
	}

	/**
	 * @Description: For populating sorted valid product, ussid map
	 * @param validProductUssidTempMap
	 * @param code
	 * @param isStockPromo
	 * @return mapping of valid ussids
	 */
	public Set<String> populateStockOfSortedValidProdUssidMap(final Map<String, AbstractOrderEntry> validProductUssidTempMap,
			final int totalEligibleCount, final SessionContext ctx, final List<AbstractPromotionRestriction> restrictionList,
			final Map<String, Integer> qCountMap, final String code, final boolean isStockPromo)
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
		if (!isStockPromo)

		{
			//return doConsumeEntries(validEntries, totalEligibleCount, ctx, qCountMap, 0, code);
			//return doConsumeEntries(validEntries, totalEligibleCount, ctx, qCountMap, 0, code, null);
			return doConsumeEntries(validEntries, totalEligibleCount, ctx, qCountMap, 0, code, null, false);
		}
		else

		{
			return doConsumeEntriesForStockPromo(validEntries, totalEligibleCount, ctx, qCountMap, code);
		}

	}

	/**
	 * TPR-970 changes checking whether a particular state exits against a pincode or not
	 *
	 * @param includedStates
	 * @param pinCode
	 *
	 */
	private boolean isAppliedPinCodeStatesIncludes(final List<State> includedStates, final String pinCode)
	{
		boolean flag = false;
		if (CollectionUtils.isEmpty(includedStates))
		{
			flag = true;
		}
		for (final State state : includedStates)
		{
			if (StringUtils.isNotEmpty(pinCode) && state.getCountrykey().equalsIgnoreCase(pinCode))
			{
				flag = true;
				break;
			}

		}
		return flag;
	}


	/**
	 * TPR-965 changes
	 *
	 * @param cart
	 * @param paramSessionContext
	 * @param promotionProductList
	 * @param promotionCategoryList
	 * @param restrictionList
	 * @param stockCount
	 * @param code
	 * @return validProductUssidMap
	 * @throws JaloInvalidParameterException
	 * @throws JaloSecurityException
	 */
	public Map<String, AbstractOrderEntry> getValidEntriesForStockLevelPromo(final AbstractOrder cart,
			final SessionContext paramSessionContext, final List<Product> promotionProductList,
			final List<Category> promotionCategoryList, final List<AbstractPromotionRestriction> restrictionList,
			final List<Product> excludedProductList, final List<String> excludeManufactureList, final int stockCount,
			final String code) throws JaloInvalidParameterException, JaloSecurityException
	{
		Map<String, Integer> stockCountMap = new HashMap<String, Integer>();
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		final StringBuilder ussidIds = new StringBuilder();
		final StringBuilder productCodes = new StringBuilder();
		boolean isSellerRestricPresent = false;
		if (cart != null)
		{
			for (final AbstractOrderEntry entry : cart.getEntries())
			{
				productCodes.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getProduct().getCode()
						+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
				/* * SONAR FIX */
				productCodes.append(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
				ussidIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA
						+ entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID).toString()
						+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
				/* * SONAR FIX */
				ussidIds.append(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
			}
		}

		if (CollectionUtils.isNotEmpty(restrictionList))
		{

			for (final AbstractPromotionRestriction restriction : restrictionList)
			{
				if (restriction instanceof EtailSellerSpecificRestriction)
				{
					isSellerRestricPresent = true;
					stockCountMap = stockPromoCheckService
							.getCumulativeStockMap(ussidIds.toString().substring(0, ussidIds.lastIndexOf(",")), code, true);
				}
				if (restriction instanceof EtailExcludeSellerSpecificRestriction)
				{
					isSellerRestricPresent = true;
					stockCountMap = stockPromoCheckService
							.getCumulativeStockMap(ussidIds.toString().substring(0, ussidIds.lastIndexOf(",")), code, true);
				}
			}
			if (!isSellerRestricPresent && CollectionUtils.isNotEmpty(restrictionList))
			{
				stockCountMap = stockPromoCheckService
						.getCumulativeStockMap(productCodes.toString().substring(0, productCodes.lastIndexOf(",")), code, false);
			}

		}
		else
		{
			stockCountMap = stockPromoCheckService
					.getCumulativeStockMap(productCodes.toString().substring(0, productCodes.lastIndexOf(",")), code, false);
		}
		for (final AbstractOrderEntry entry : cart.getEntries())
		{

			final Product product = entry.getProduct();


			//checking product is a valid product for promotion
			boolean applyPromotion = false;
			boolean brandFlag = false;
			boolean sellerFlag = false;
			if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
					|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList))
			//|| isProductExcludedForSeller(paramSessionContext, restrictionList, entry))
			{
				continue;
			}
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
			final String selectedUssid = entry.getAttribute(paramSessionContext, MarketplacecommerceservicesConstants.SELECTEDUSSID)
					.toString();
			final String productCode = entry.getProduct().getCode();
			if (applyPromotion && brandFlag && sellerFlag && isSellerRestricPresent && null != stockCountMap.get(selectedUssid)
					&& stockCount - stockCountMap.get(selectedUssid).intValue() > 0)
			{
				validProductUssidMap.put(selectedUssid, entry);
			}
			else if (applyPromotion && brandFlag && sellerFlag && !isSellerRestricPresent && null != stockCountMap.get(productCode)
					&& stockCount - stockCountMap.get(productCode).intValue() > 0)
			{
				validProductUssidMap.put(selectedUssid, entry);

			}
			else if (applyPromotion && brandFlag && sellerFlag && !isSellerRestricPresent && null == stockCountMap.get(productCode))
			{
				validProductUssidMap.put(selectedUssid, entry);
			}
			else if (applyPromotion && brandFlag && sellerFlag && isSellerRestricPresent && null == stockCountMap.get(selectedUssid))
			{
				validProductUssidMap.put(selectedUssid, entry);
			}
			else if (applyPromotion && brandFlag && sellerFlag && stockCountMap.isEmpty())
			{
				validProductUssidMap.put(selectedUssid, entry);
			}

		}
		return validProductUssidMap;
	}


	/**
	 * For getting total tdelivery charges of eligible products
	 *
	 * @param validProductUssidMap
	 * @param qCountMap
	 * @return totalDeliveryCostForValidProds
	 *
	 */
	//	public double getTotalDelCostForValidProds(final Map<String, AbstractOrderEntry> validProductUssidMap,
	//			final Map<String, Integer> qCountMap)
	//	{
	//		double totalDeliveryCostForValidProds = 0.00D;
	//
	//		for (final AbstractOrderEntry entryJalo : validProductUssidMap.values())
	//		{
	//			final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) getModelService().get(entryJalo);
	//			final String entryUssid = entry.getSelectedUSSID();
	//			final String selectedDeliveryModeCode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
	//			final String currencyIsoCode = MarketplacecommerceservicesConstants.INR;
	//			ServicesUtil.validateParameterNotNull(selectedDeliveryModeCode, "deliveryCode cannot be null");
	//			ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
	//			ServicesUtil.validateParameterNotNull(entryUssid, "sellerArticleSKU cannot be null");
	//			final MplZoneDeliveryModeValueModel mplZoneDeliveryModeValueModel = deliveryCostService.getDeliveryCost(
	//					selectedDeliveryModeCode, currencyIsoCode, entryUssid);
	//
	//			totalDeliveryCostForValidProds += mplZoneDeliveryModeValueModel.getValue().doubleValue()
	//					* qCountMap.get(entryUssid).intValue();
	//		}
	//		return totalDeliveryCostForValidProds;
	//	}

	public Map<String, Boolean> getProdShippingPromoAppliedMap(final AbstractOrder order)
	{
		final Map<String, Boolean> isProdShippingPromoAppliedMap = new HashMap<String, Boolean>();

		final AbstractOrderModel abstractOrderModel = (AbstractOrderModel) getModelService().get(order); //TPR-969
		final List<PromotionResultModel> allPromoResult = new ArrayList<PromotionResultModel>(
				abstractOrderModel.getAllPromotionResults());

		for (final PromotionResultModel promoResult : allPromoResult)
		{
			final AbstractPromotionModel appliedPromotion = promoResult.getPromotion();
			if (promoResult.getCertainty().floatValue() >= 1.0F && null != appliedPromotion
					&& (appliedPromotion instanceof BuyAGetPromotionOnShippingChargesModel
							|| appliedPromotion instanceof BuyAandBGetPromotionOnShippingChargesModel))
			{
				for (final PromotionOrderEntryConsumedModel consumed : promoResult.getConsumedEntries())
				{
					isProdShippingPromoAppliedMap.put(consumed.getOrderEntry().getSelectedUSSID(), Boolean.TRUE);
				}
			}

		}

		return isProdShippingPromoAppliedMap;
	}


	/*
	 * public Set<String> getValidMapAfterStockLevelRestriction(final Map<String, AbstractOrderEntry>
	 * multiSellerValidUSSIDMap, final String code, final List<AbstractPromotionRestriction> restrictionList)
	 */
	public Set<String> getValidMapAfterStockLevelRestriction(final Map<String, AbstractOrderEntry> multiSellerValidUSSIDMap,
			final String code, final int stockCount, final Map<String, Integer> stockCountMap, final boolean sellerFlag)
	{
		final StringBuilder ussidIds = new StringBuilder();

		final StringBuilder productCodes = new StringBuilder();
		final Set<String> ussidSet = new HashSet<String>();
		final StringBuilder categoryCodes = new StringBuilder();

		//Map<String, Integer> stockCountMap = new HashMap<String, Integer>();
		//final int stockCount = getStockRestrictionVal(restrictionList);
		//final Map<String, Integer> mapQuantCount = new HashMap<String, Integer>();

		for (final Map.Entry<String, AbstractOrderEntry> entry : multiSellerValidUSSIDMap.entrySet())
		{
			ussidIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getKey()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
			/* * SONAR FIX */
			ussidIds.append(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
			productCodes.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getValue().getProduct().getCode()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
			/* * SONAR FIX */
			productCodes.append(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
			//mapQuantCount.put(entry.getKey(), Integer.valueOf(entry.getValue().getQuantity().intValue()));
		}

		//		final boolean sellerFlag = getSellerRestrictionVal(restrictionList);
		StringBuilder idsToCheck = null;

		if (sellerFlag)
		{
			//			stockCountMap = stockPromoCheckService.getCumulativeStockMap(
			//					ussidIds.toString().substring(0, ussidIds.lastIndexOf(",")), code, true);
			idsToCheck = ussidIds;
		}
		else
		{
			//			stockCountMap = stockPromoCheckService.getCumulativeStockMap(
			//					productCodes.toString().substring(0, productCodes.lastIndexOf(",")), code, false);
			idsToCheck = productCodes;
		}

		final boolean isCategory = checkForCategoryPromotion(code);
		if (!isCategory)
		{
			stockCountMap.putAll(stockPromoCheckService
					.getCumulativeStockMap(idsToCheck.toString().substring(0, idsToCheck.lastIndexOf(",")), code, sellerFlag));
		}
		else
		{
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap = splitCategoryDetails(multiSellerValidUSSIDMap, code, sellerFlag);
			if (MapUtils.isNotEmpty(dataMap))
			{
				for (final Map.Entry<String, String> entry : dataMap.entrySet())
				{
					categoryCodes.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getValue()
							+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
					/* * SONAR FIX */
					categoryCodes.append(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
				}

				stockCountMap.putAll(stockPromoCheckService.getCumulativeCatLevelStockMap(
						categoryCodes.toString().substring(0, categoryCodes.lastIndexOf(",")), code, dataMap));
			}
			else
			{
				stockCountMap.putAll(stockPromoCheckService
						.getCumulativeStockMap(idsToCheck.toString().substring(0, idsToCheck.lastIndexOf(",")), code, sellerFlag));
			}
		}


		for (final Map.Entry<String, AbstractOrderEntry> entry : multiSellerValidUSSIDMap.entrySet())
		{
			if (null != stockCountMap.get(entry.getKey()) && sellerFlag
					&& (stockCount - stockCountMap.get(entry.getKey()).intValue() > 0))
			{
				ussidSet.add(entry.getKey());
			}
			else if (null != stockCountMap.get(entry.getValue().getProduct().getCode()) && !sellerFlag
					&& (stockCount - stockCountMap.get(entry.getValue().getProduct().getCode()).intValue() > 0))
			{
				ussidSet.add(entry.getKey());
			}
			else if (stockCountMap.isEmpty() && stockCount > 0)
			{
				ussidSet.add(entry.getKey());
			}
			else if (!stockCountMap.isEmpty() && !(stockCountMap.containsKey(entry.getKey())) && sellerFlag && stockCount > 0)
			{
				ussidSet.add(entry.getKey());
			}
			else if (!stockCountMap.isEmpty() && !(stockCountMap.containsKey(entry.getValue().getProduct().getCode())) && !sellerFlag
					&& stockCount > 0)
			{
				ussidSet.add(entry.getKey());
			}

		}
		return ussidSet;
	}

	/**
	 * @param multiSellerValidUSSIDMap
	 * @param code
	 * @param sellerFlag
	 * @return
	 */
	public Map<String, String> splitCategoryDetails(final Map<String, AbstractOrderEntry> multiSellerValidUSSIDMap,
			final String code, final boolean sellerFlag)
	{
		final Map<String, String> dataMap = new HashMap<String, String>();
		final ProductPromotionModel oModel = getPromoDetails(code);

		for (final Map.Entry<String, AbstractOrderEntry> data : multiSellerValidUSSIDMap.entrySet())
		{
			if (null != data.getValue() && null != data.getValue().getProduct() && null != data.getValue().getProduct().getCode())
			{
				final ProductModel product = productService.getProduct(data.getValue().getProduct().getCode());
				if (null != product && CollectionUtils.isNotEmpty(product.getSupercategories()) && null != oModel)
				{
					final List<CategoryModel> getCategoryDataList = getSuperCategoryData(product.getSupercategories());

					if (CollectionUtils.isNotEmpty(getCategoryDataList))
					{
						for (final CategoryModel category : getCategoryDataList /* product.getSupercategories() */)
						{
							for (final CategoryModel promocategory : oModel.getCategories())
							{
								if (StringUtils.equalsIgnoreCase(category.getCode(), promocategory.getCode()))
								{
									if (sellerFlag)
									{
										dataMap.put(data.getKey(), promocategory.getCode());
									}
									else
									{
										dataMap.put(data.getValue().getProduct().getCode(), promocategory.getCode());
									}
								}
							}
						}
					}
				}
			}
		}
		return dataMap;
	}


	/**
	 * Tree Traversal for Category Promotions
	 *
	 * @param supercategories
	 * @return categoryList
	 */
	public List<CategoryModel> getSuperCategoryData(final Collection<CategoryModel> supercategories)
	{
		final List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		List<CategoryModel> subList = null;

		for (final CategoryModel category : supercategories)
		{
			subList = new ArrayList<CategoryModel>(categoryService.getAllSupercategoriesForCategory(category));
			categoryList.addAll(subList);
		}

		categoryList.addAll(supercategories);

		return categoryList;
	}

	/**
	 * @param code
	 * @return flag
	 */
	public boolean checkForCategoryPromotion(final String code)
	{
		boolean flag = false;
		try
		{
			final ProductPromotionModel promotion = getPromoDetails(code);
			if (CollectionUtils.isNotEmpty(promotion.getCategories()))
			{
				flag = true;
			}
		}
		catch (final Exception exception)
		{
			flag = false;
		}

		return flag;
	}

	/**
	 * @param code
	 * @return
	 */
	private ProductPromotionModel getPromoDetails(final String code)
	{
		final ProductPromotionModel oModel = modelService.create(ProductPromotionModel.class);
		oModel.setCode(code);
		oModel.setEnabled(Boolean.TRUE);

		return flexibleSearchService.getModelByExample(oModel);
	}

	public int getStockRestrictionVal(final List<AbstractPromotionRestriction> restrictionList)
	{
		int stockVal = 0;
		for (final AbstractPromotionRestriction restriction : restrictionList)
		{
			if (restriction instanceof EtailLimitedStockRestriction)
			{
				if (((EtailLimitedStockRestriction) restriction).getMaxStock() != null)
				{
					stockVal = ((EtailLimitedStockRestriction) restriction).getMaxStock().intValue();
					break;
				}
			}
		}
		return stockVal;
	}

	public boolean getSellerRestrictionVal(final List<AbstractPromotionRestriction> restrictionList)
	{
		boolean isPresent = false;
		for (final AbstractPromotionRestriction restriction : restrictionList)
		{
			if (restriction instanceof EtailSellerSpecificRestriction)
			{
				isPresent = true;
				break;
			}
		}
		return isPresent;
	}

	/**
	 * @param validProductUssidMap
	 * @param code
	 * @param restrictionList
	 * @return
	 */
	public Set<String> getStockLevelRestriction(final Map<String, AbstractOrderEntry> validProductUssidMap, final String code,
			final List<AbstractPromotionRestriction> restrictionList)
	{
		final StringBuilder ussidIds = new StringBuilder();

		final StringBuilder productCodes = new StringBuilder();
		final Set<String> ussidSet = new HashSet<String>();
		Map<String, Integer> stockCountMap = new HashMap<String, Integer>();
		final int stockCount = getStockRestrictionVal(restrictionList);
		final Map<String, Integer> mapQuantCount = new HashMap<String, Integer>();
		for (final Map.Entry<String, AbstractOrderEntry> entry : validProductUssidMap.entrySet())
		{
			ussidIds.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getKey()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
			/* * SONAR FIX */
			ussidIds.append(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
			productCodes.append(MarketplacecommerceservicesConstants.INVERTED_COMMA + entry.getValue().getProduct().getCode()
					+ MarketplacecommerceservicesConstants.INVERTED_COMMA);
			/* * SONAR FIX */
			productCodes.append(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
			mapQuantCount.put(entry.getKey(), Integer.valueOf(entry.getValue().getQuantity().intValue()));
		}
		final boolean sellerFlag = getSellerRestrictionVal(restrictionList);
		if (sellerFlag)
		{
			stockCountMap = stockPromoCheckService.getCumulativeStockMap(ussidIds.toString().substring(0, ussidIds.lastIndexOf(",")),
					code, true);
		}
		else
		{
			stockCountMap = stockPromoCheckService
					.getCumulativeStockMap(productCodes.toString().substring(0, productCodes.lastIndexOf(",")), code, false);
		}
		for (final Map.Entry<String, AbstractOrderEntry> entry : validProductUssidMap.entrySet())
		{
			if (null != stockCountMap.get(entry.getKey()) && sellerFlag
					&& (stockCount - stockCountMap.get(entry.getKey()).intValue() > 0))
			{
				ussidSet.add(entry.getKey());
			}
			else if (null != stockCountMap.get(entry.getKey()) && !sellerFlag
					&& (stockCount - stockCountMap.get(entry.getValue().getProduct()).intValue() > 0))
			{
				ussidSet.add(entry.getKey());
			}
			else if (stockCountMap.isEmpty() && stockCount > 0)
			{
				ussidSet.add(entry.getKey());
			}

		}
		return ussidSet;
	}

	/**
	 * @param cart
	 * @param ctx
	 * @param allowedProductList
	 * @param excludedProductList
	 * @param excludeManufactureList
	 * @param restrictionList
	 *
	 *
	 * @return Map<String, AbstractOrderEntry>
	 */
	public Map<String, AbstractOrderEntry> getValidProductListBOGO(final AbstractOrder cart, final SessionContext ctx,
			final List<Product> allowedProductList, final List<AbstractPromotionRestriction> restrictionList)
	{
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();


		//boolean brandFlag = false;
		boolean sellerFlag = false;
		boolean isFreebie = false;

		for (final AbstractOrderEntry entry : cart.getEntries())
		{

			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);
			if (!isFreebie)
			{
				final Product product = entry.getProduct();

				//				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
				//						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
				//						|| isProductExcludedForSeller(ctx, restrictionList, entry))
				//				if (isProductExcludedForSeller(ctx, restrictionList, entry))
				//				{
				//					continue;
				//				}

				if (CollectionUtils.isNotEmpty(allowedProductList) && allowedProductList.contains(product))
				{
					//brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
					//sellerFlag = checkSellerBOGOData(ctx, restrictionList, entry);
					sellerFlag = checkSellerData(ctx, restrictionList, entry);
				}

				//if (sellerFlag && brandFlag)
				if (sellerFlag)
				{
					validProductUssidMap.putAll(populateValidProductUssidMap(product, cart, restrictionList, ctx, entry));

					sellerFlag = false;
					//brandFlag = false;
				}
			}

		}

		return validProductUssidMap;
	}

	/**
	 * @param cart
	 * @param ctx
	 * @param allowedProductList
	 * @param excludedProductList
	 * @param excludeManufactureList
	 * @param restrictionList
	 *
	 *
	 * @return Map<String, AbstractOrderEntry>
	 */
	//	public Map<String, AbstractOrderEntry> getValidProductListBOGO(final AbstractOrder cart, final SessionContext ctx,
	//			final List<Product> allowedProductList, final List<Product> excludedProductList,
	//			final List<String> excludeManufactureList, final List<AbstractPromotionRestriction> restrictionList)
	//	{
	//		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
	//
	//
	//		boolean brandFlag = false;
	//		boolean sellerFlag = false;
	//		boolean isFreebie = false;
	//
	//		for (final AbstractOrderEntry entry : cart.getEntries())
	//		{
	//
	//			isFreebie = getMplPromotionHelper().validateEntryForFreebie(entry);
	//			if (!isFreebie)
	//			{
	//				final Product product = entry.getProduct();
	//
	//				if (GenericUtilityMethods.isProductExcluded(product, excludedProductList)
	//						|| GenericUtilityMethods.isProductExcludedForManufacture(product, excludeManufactureList)
	//						|| isProductExcludedForSeller(ctx, restrictionList, entry))
	//				{
	//					continue;
	//				}
	//
	//				if (CollectionUtils.isNotEmpty(allowedProductList) && allowedProductList.contains(product))
	//				{
	//					brandFlag = GenericUtilityMethods.checkBrandData(restrictionList, product);
	//					sellerFlag = checkSellerBOGOData(ctx, restrictionList, entry);
	//				}
	//
	//				if (sellerFlag && brandFlag)
	//				{
	//					validProductUssidMap.putAll(populateValidProductUssidMap(product, cart, restrictionList, ctx, entry));
	//
	//					sellerFlag = false;
	//					brandFlag = false;
	//				}
	//			}
	//
	//		}
	//
	//		return validProductUssidMap;
	//	}

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
		int quantity = 0;
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
								quantity = quantity + (entry.getKey().getQuantity().intValue());
								//giftCount = giftCount + (entry.getKey().getQuantity().intValue() / count);
							}
						}
					}
				}

				//Newly Added Code
				if (quantity > 0)
				{
					giftCount = quantity / count;
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

	//Added for A and B promotion getProducts fix: starts
	/**
	 * @param promoContext
	 * @param ctx
	 * @param promotion
	 * @param categories
	 * @param secondCategories
	 * @param primaryProductList
	 * @param secondaryProductList
	 * @return RestrictionSetResult
	 */
	protected PromotionsManager.RestrictionSetResult findEligibleProductsInBasketForBuyAandBPromo(final SessionContext ctx,
			final PromotionEvaluationContext promoContext, final AbstractPromotion promotion, final Collection<Category> categories,
			final Collection<Category> secondCategories, final List<Product> primaryProductList,
			final List<Product> secondaryProductList)
	{
		final Flat3Map params = new Flat3Map();
		//critical sonar fix
		params.put(MarketplacecommerceservicesConstants.PROMO, promotion);

		final Collection products = getBaseProductsForOrderForBuyAandBPromo(ctx, promoContext.getOrder(), secondaryProductList,
				promotion, params, secondCategories);

		if (!(products.isEmpty()))
		{
			final Set promotionCategories = new HashSet();
			for (final Category cat : categories)
			{
				promotionCategories.add(cat);
				promotionCategories.addAll(cat.getAllSubcategories(ctx));
			}

			final List promotionCategoriesList = new ArrayList(promotionCategories);

			//			final Flat3Map params = new Flat3Map();
			//			params.put("promo", promotion);
			params.put(MarketplacecommerceservicesConstants.PRODUCT_IMAGE, products);

			final StringBuilder promQuery = new StringBuilder("SELECT DISTINCT pprom.pk FROM (");
			promQuery.append(" {{ SELECT {p2p:").append(MarketplacecommerceservicesConstants.QUERYSOURCE)
					.append(MarketplacecommerceservicesConstants.QUERYPK);
			promQuery.append(" FROM {").append(GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION).append(" AS p2p } ");
			promQuery.append(" WHERE ?promo = {p2p:").append(MarketplacecommerceservicesConstants.QUERYTARGET).append("} ");
			promQuery.append(" AND {p2p:").append(MarketplacecommerceservicesConstants.QUERYSOURCE)
					.append(MarketplacecommerceservicesConstants.QUERYPRODUCT);


			if (!(Config.isOracleUsed()))
			{
				if (!(promotionCategoriesList.isEmpty()))
				{
					promQuery.append(MarketplacecommerceservicesConstants.QUERYUNION);

					promQuery.append(MarketplacecommerceservicesConstants.QUERYSELECT)
							.append(MarketplacecommerceservicesConstants.QUERYTARGET)
							.append(MarketplacecommerceservicesConstants.QUERYPK);
					promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
							.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION)
							.append(MarketplacecommerceservicesConstants.QUERYAS);
					promQuery.append(MarketplacecommerceservicesConstants.QUERYWHERECAT)
							.append(MarketplacecommerceservicesConstants.QUERYSOURCE).append("} in (?promotionCategories)  ");
					promQuery.append("   AND {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
							.append(MarketplacecommerceservicesConstants.QUERYPRODUCT);

					params.put("promotionCategories", promotionCategories);
				}
				//promQuery.append(AS_PPROM);
			}
			else
			{
				if (!(promotionCategoriesList.isEmpty()))
				{
					int pages = 0;
					for (int i = 0; i < promotionCategoriesList.size(); i += 1000)
					{
						params.put("promotionCategories_" + pages,
								promotionCategoriesList.subList(i, Math.min(i + 1000, promotionCategoriesList.size())));
						++pages;
					}
					for (int i = 0; i < pages; ++i)
					{
						promQuery.append(MarketplacecommerceservicesConstants.QUERYUNION);

						promQuery.append(MarketplacecommerceservicesConstants.QUERYSELECT)
								.append(MarketplacecommerceservicesConstants.QUERYTARGET)
								.append(MarketplacecommerceservicesConstants.QUERYPK);
						promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
								.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION)
								.append(MarketplacecommerceservicesConstants.QUERYAS);
						promQuery.append(MarketplacecommerceservicesConstants.QUERYWHERECAT)
								.append(MarketplacecommerceservicesConstants.QUERYSOURCE).append("} in (?promotionCategories_").append(i);
						promQuery.append(")   AND {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
								.append(MarketplacecommerceservicesConstants.QUERYPRODUCT);
					}
				}
				//promQuery.append(PPROM);
			}

			//---------Check for brand: starts------------//
			final StringBuilder brandQuery = evaluateBrandRestriction(params, promotion, ctx);
			if (brandQuery != null)
			{
				promQuery.append(brandQuery);
			}
			//---------Check for brand: starts------------//

			if (!(Config.isOracleUsed()))
			{
				promQuery.append(AS_PPROM);
			}
			else
			{
				promQuery.append(PPROM);
			}

			final List cartProducts = getSession().getFlexibleSearch().search(ctx, promQuery.toString(), params, Product.class)
					.getResult();

			primaryProductList.addAll(cartProducts);

			final List<Product> allProductList = new ArrayList<Product>(primaryProductList);
			allProductList.addAll(secondaryProductList);

			if (promoContext.getObserveRestrictions())
			{
				return PromotionsManager.getInstance().evaluateRestrictions(ctx, allProductList, promoContext.getOrder(), promotion,
						promoContext.getDate());
			}

			return new PromotionsManager.RestrictionSetResult(allProductList);
		}

		return new PromotionsManager.RestrictionSetResult(new ArrayList(0));
	}

	/**
	 * @param params
	 * @param ctx
	 * @param promotion
	 * @param secondCategories
	 * @param order
	 * @param secondaryProductList
	 * @return Collection<Product>
	 */
	private Collection<Product> getBaseProductsForOrderForBuyAandBPromo(final SessionContext ctx, final AbstractOrder order,
			final List<Product> secondaryProductList, final AbstractPromotion promotion, final Flat3Map params,
			final Collection<Category> secondCategories)
	{
		final SortedSet products = new TreeSet();
		String secondProductsAsString = null;
		String promotionType = null;

		if (promotion instanceof BuyAandBPrecentageDiscount)
		{
			promotionType = MarketplacecommerceservicesConstants.BUYAANDBPERCENTAGEDISCOUNT;
		}
		else if (promotion instanceof BuyAandBgetC)
		{
			promotionType = MarketplacecommerceservicesConstants.BUYAANDBGETC;
		}
		else if (promotion instanceof BuyAandBGetPromotionOnShippingCharges)
		{
			promotionType = MarketplacecommerceservicesConstants.BUYAANDBGETPROMOTIONONSHIPPINGCHARGES;
		}
		else if (promotion instanceof BuyAGetPercentageDiscountOnB)
		{
			promotionType = MarketplacecommerceservicesConstants.BUYAGETPERCENTAGEDISCOUNTONB;
		}
		else if (promotion instanceof BuyAandBGetPrecentageDiscountCashback)
		{
			promotionType = MarketplacecommerceservicesConstants.ABCASHBACKPROMO;
		}

		final List<String> resultList = fetchSecProdsExcludedProdsForPromotion(params, ctx, promotionType);

		if (CollectionUtils.isEmpty(secondCategories) && CollectionUtils.isNotEmpty(resultList))
		{
			secondProductsAsString = resultList.get(0);
		}

		final Collection<String> excludedProductList = (CollectionUtils.isNotEmpty(resultList)
				&& StringUtils.isNotEmpty(resultList.get(1))) ? Arrays.asList(resultList.get(1).split(",")) : new ArrayList<String>();

		for (final AbstractOrderEntry aoe : order.getEntries())
		{
			final Product product = aoe.getProduct(ctx);

			if (product == null
					|| (CollectionUtils.isNotEmpty(excludedProductList) && excludedProductList.contains(product.getPK().toString())))
			{
				continue;
			}
			//products.add(product);

			//			final List baseProducts = Helper.getBaseProducts(ctx, product);
			//			if ((baseProducts == null) || (baseProducts.isEmpty()))
			//			{
			//				continue;
			//			}
			//			products.addAll(baseProducts);

			if (CollectionUtils.isEmpty(secondCategories) && getSecondProducts(product, secondProductsAsString))
			{
				secondaryProductList.add(product);
			}
			else
			{
				products.add(product);
			}

		}

		if (CollectionUtils.isNotEmpty(secondCategories))
		{
			params.put(MarketplacecommerceservicesConstants.PRODUCT_IMAGE, products);
			populateSecondaryListForCategory(secondCategories, secondaryProductList, params, ctx);
			products.removeAll(secondaryProductList);
		}
		else
		{
			checkBrandForSecProd(secondaryProductList, params, ctx);
			//secondaryProductList.addAll(getSecondProducts(params, products, secondProductsAsString, ctx));
		}

		return products;
	}

	/**
	 * @param product
	 * @param secondProductsAsString
	 * @return RestrictionSetResult
	 * @return Collection<Product>
	 */
	//private List<Product> getSecondProducts(final Product product, final String secondProductsAsString)
	//	private List<Product> getSecondProducts(final Flat3Map params, final SortedSet products, final String secondProductsAsString,
	//			final SessionContext ctx)
	//	{
	//		final List<Product> secondProductList = new ArrayList<Product>();
	//
	//		//		if (product != null && StringUtils.isNotEmpty(secondProductsAsString)
	//		//				&& secondProductsAsString.contains(product.getPK().toString()))
	//		//		{
	//		//			secondProductList.add(product);
	//		//		}
	//
	//		for (final Object obj : products)
	//		{
	//			final Product product = (Product) obj;
	//			if (StringUtils.isNotEmpty(secondProductsAsString) && secondProductsAsString.contains(product.getPK().toString()))
	//			{
	//				secondProductList.add(product);
	//			}
	//		}
	//	}

	/**
	 * @param products
	 * @param secondProductsAsString
	 * @return RestrictionSetResult
	 * @return Collection<Product>
	 */
	private boolean getSecondProducts(final Product product, final String secondProductsAsString)
	{
		return (StringUtils.isNotEmpty(secondProductsAsString) && secondProductsAsString.contains(product.getPK().toString()));
	}


	/**
	 * @param secondProductList
	 * @param params
	 * @return ctx
	 * @return Collection<Product>
	 */
	//	private List<Product> checkBrandForSecProd(final List<Product> secondProductList, final Flat3Map params,
	//			final SessionContext ctx)
	private void checkBrandForSecProd(final List<Product> secondProductList, final Flat3Map params, final SessionContext ctx)
	{
		params.put(MarketplacecommerceservicesConstants.SECONDPRODUCT, secondProductList);
		//critical sonar fix
		final StringBuilder promQuery = evaluateBrandRestriction(params,
				(AbstractPromotion) params.get(MarketplacecommerceservicesConstants.PROMO), ctx);
		if (promQuery != null)
		{
			final List<Product> cartSecondProducts = getSession().getFlexibleSearch()
					.search(ctx, promQuery.toString(), params, Product.class).getResult();
			params.remove(MarketplacecommerceservicesConstants.SECONDPRODUCT);
			secondProductList.retainAll(cartSecondProducts);
			//return cartSecondProducts;
		}

		//return secondProductList;
	}

	/**
	 * @param params
	 * @param ctx
	 * @param promotionType
	 * @return String
	 */
	private List<String> fetchSecProdsExcludedProdsForPromotion(final Flat3Map params, final SessionContext ctx,
			final String promotionType)
	{
		/* SONAR FIX */
		final StringBuilder promQuery = new StringBuilder(200);
		promQuery.append("SELECT {promo.secondProducts} as secondProducts, {promo.excludedProducts} as excludedProducts  ");
		promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM).append(promotionType).append(" AS promo} ");
		promQuery.append(" WHERE {promo:pk} = ?promo ");
		//promQuery.append(" WHERE {promo.secondProducts} IS NOT NULL AND {promo:pk} = ?promo ");

		final List<List<String>> resultStr = getSession().getFlexibleSearch()
				.search(ctx, promQuery.toString(), params, Arrays.asList(String.class, String.class), true, true, 0, -1).getResult();

		return CollectionUtils.isNotEmpty(resultStr) ? resultStr.get(0) : new ArrayList<String>();
	}

	/**
	 * @param secondCategories
	 * @param ctx
	 * @param secondaryProductList
	 * @param params
	 * @return String
	 */
	private void populateSecondaryListForCategory(final Collection<Category> secondCategories,
			final List<Product> secondaryProductList, final Flat3Map params, final SessionContext ctx)
	{
		final StringBuilder promQuery = new StringBuilder(150);
		final Set promotionCategories = new HashSet();
		for (final Category cat : secondCategories)
		{
			promotionCategories.add(cat);
			promotionCategories.addAll(cat.getAllSubcategories(ctx));
		}
		final List promotionCategoriesList = new ArrayList(promotionCategories);

		promQuery.append("SELECT DISTINCT pprom.pk FROM ( ");

		if (!(Config.isOracleUsed()))
		{
			if (!(promotionCategoriesList.isEmpty()))
			{
				promQuery.append(" {{ SELECT {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
						.append(MarketplacecommerceservicesConstants.QUERYPK);
				promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
						.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION)
						.append(MarketplacecommerceservicesConstants.QUERYAS);
				promQuery.append(MarketplacecommerceservicesConstants.QUERYWHERECAT)
						.append(MarketplacecommerceservicesConstants.QUERYSOURCE).append("} in (?promotionCategories)  ");
				promQuery.append("   AND {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
						.append(MarketplacecommerceservicesConstants.QUERYPRODUCT);

				params.put("promotionCategories", promotionCategories);
			}
		}
		else
		{
			if (!(promotionCategoriesList.isEmpty()))
			{
				int pages = 0;
				for (int i = 0; i < promotionCategoriesList.size(); i += 1000)
				{
					params.put("promotionCategories_" + pages,
							promotionCategoriesList.subList(i, Math.min(i + 1000, promotionCategoriesList.size())));
					++pages;
				}
				for (int i = 0; i < pages; ++i)
				{
					promQuery.append(" {{ SELECT {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
							.append(MarketplacecommerceservicesConstants.QUERYPK);
					promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
							.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION)
							.append(MarketplacecommerceservicesConstants.QUERYAS);
					promQuery.append(MarketplacecommerceservicesConstants.QUERYWHERECAT)
							.append(MarketplacecommerceservicesConstants.QUERYSOURCE).append("} in (?promotionCategories_").append(i);
					promQuery.append(")   AND {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
							.append(MarketplacecommerceservicesConstants.QUERYPRODUCT);
				}
			}
		}
		//critical sonar fix
		final StringBuilder brandQuery = evaluateBrandRestriction(params,
				(AbstractPromotion) params.get(MarketplacecommerceservicesConstants.PROMO), ctx);
		if (brandQuery != null)
		{
			promQuery.append(brandQuery);
		}

		if (!(Config.isOracleUsed()))
		{
			promQuery.append(AS_PPROM);
		}
		else
		{
			promQuery.append(PPROM);
		}

		final List cartSecondProducts = getSession().getFlexibleSearch().search(ctx, promQuery.toString(), params, Product.class)
				.getResult();

		secondaryProductList.addAll(cartSecondProducts);
	}

	//ends
	//Added for A type promotions getProducts fix: starts
	/**
	 * @param promoContext
	 * @param ctx
	 * @param promotion
	 * @param categories
	 * @param secondCategories
	 * @param primaryProductList
	 * @param secondaryProductList
	 * @return RestrictionSetResult
	 */
	protected PromotionsManager.RestrictionSetResult findEligibleProductsInBasket(final SessionContext ctx,
			final PromotionEvaluationContext promoContext, final AbstractPromotion promotion, final Collection<Category> categories)
	{
		final Flat3Map params = new Flat3Map();
		//critical sonar fix
		params.put(MarketplacecommerceservicesConstants.PROMO, promotion);

		final Collection products = getBaseProductsInBasket(ctx, promoContext.getOrder(), params);

		if (!(products.isEmpty()))
		{
			final Set promotionCategories = new HashSet();
			for (final Category cat : categories)
			{
				promotionCategories.add(cat);
				promotionCategories.addAll(cat.getAllSubcategories(ctx));
			}
			final List promotionCategoriesList = new ArrayList(promotionCategories);

			//			final Flat3Map params = new Flat3Map();
			//			params.put("promo", promotion);
			params.put(MarketplacecommerceservicesConstants.PRODUCT_IMAGE, products);

			final StringBuilder promQuery = new StringBuilder("SELECT DISTINCT pprom.pk FROM (");
			promQuery.append(" {{ SELECT {p2p:").append(MarketplacecommerceservicesConstants.QUERYSOURCE)
					.append(MarketplacecommerceservicesConstants.QUERYPK);
			promQuery.append(" FROM {").append(GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION).append(" AS p2p } ");
			promQuery.append(" WHERE ?promo = {p2p:").append(MarketplacecommerceservicesConstants.QUERYTARGET).append("} ");
			promQuery.append(" AND {p2p:").append(MarketplacecommerceservicesConstants.QUERYSOURCE).append("} in (?product) }}");

			if (!(Config.isOracleUsed()))
			{
				if (!(promotionCategoriesList.isEmpty()))
				{
					promQuery.append(MarketplacecommerceservicesConstants.QUERYUNION);

					promQuery.append(MarketplacecommerceservicesConstants.QUERYSELECT)
							.append(MarketplacecommerceservicesConstants.QUERYTARGET)
							.append(MarketplacecommerceservicesConstants.QUERYPK);
					promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
							.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION)
							.append(MarketplacecommerceservicesConstants.QUERYAS);
					promQuery.append(MarketplacecommerceservicesConstants.QUERYWHERECAT)
							.append(MarketplacecommerceservicesConstants.QUERYSOURCE).append("} in (?promotionCategories)  ");
					promQuery.append("   AND {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
							.append("} in (?product) }}");

					params.put("promotionCategories", promotionCategories);
				}
				//promQuery.append(AS_PPROM);
			}
			else
			{
				if (!(promotionCategoriesList.isEmpty()))
				{
					int pages = 0;
					for (int i = 0; i < promotionCategoriesList.size(); i += 1000)
					{
						params.put("promotionCategories_" + pages,
								promotionCategoriesList.subList(i, Math.min(i + 1000, promotionCategoriesList.size())));
						++pages;
					}
					for (int i = 0; i < pages; ++i)
					{
						promQuery.append(MarketplacecommerceservicesConstants.QUERYUNION);

						promQuery.append(MarketplacecommerceservicesConstants.QUERYSELECT)
								.append(MarketplacecommerceservicesConstants.QUERYTARGET)
								.append(MarketplacecommerceservicesConstants.QUERYPK);
						promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
								.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION)
								.append(MarketplacecommerceservicesConstants.QUERYAS);
						promQuery.append(MarketplacecommerceservicesConstants.QUERYWHERECAT)
								.append(MarketplacecommerceservicesConstants.QUERYSOURCE).append("} in (?promotionCategories_").append(i);
						promQuery.append(")   AND {cat2prod:").append(MarketplacecommerceservicesConstants.QUERYTARGET)
								.append("} in (?product) }}");
					}
				}
				//				promQuery.append(PPROM);
			}

			//---------Check for brand: starts------------//
			final StringBuilder brandQuery = evaluateBrandRestriction(params, promotion, ctx);
			if (brandQuery != null)
			{
				promQuery.append(brandQuery);
			}
			//---------Check for brand: starts------------//

			if (!(Config.isOracleUsed()))
			{
				promQuery.append(AS_PPROM);
			}
			else
			{
				promQuery.append(PPROM);
			}

			final List cartProducts = getSession().getFlexibleSearch().search(ctx, promQuery.toString(), params, Product.class)
					.getResult();

			if (promoContext.getObserveRestrictions())
			{
				return PromotionsManager.getInstance().evaluateRestrictions(ctx, cartProducts, promoContext.getOrder(), promotion,
						promoContext.getDate());
			}

			return new PromotionsManager.RestrictionSetResult(cartProducts);
		}

		return new PromotionsManager.RestrictionSetResult(new ArrayList(0));
	}

	/**
	 * @param params
	 * @param ctx
	 * @param order
	 * @return Collection<Product>
	 */
	private Collection<Product> getBaseProductsInBasket(final SessionContext ctx, final AbstractOrder order, final Flat3Map params)
	{
		final SortedSet products = new TreeSet();
		final Collection<String> excludedProductList = fetchExcludedProductsForPromotion(params, ctx);

		for (final AbstractOrderEntry aoe : order.getEntries())
		{
			final Product product = aoe.getProduct(ctx);
			if (product == null
					|| (CollectionUtils.isNotEmpty(excludedProductList) && excludedProductList.contains(product.getPK().toString())))
			{
				continue;
			}
			products.add(product);

			//			final List baseProducts = Helper.getBaseProducts(ctx, product);
			//			if ((baseProducts == null) || (baseProducts.isEmpty()))
			//			{
			//				continue;
			//			}
			//			products.addAll(baseProducts);
		}

		return products;
	}


	//PR-15 modification starts
	/**
	 * @param params
	 * @param ctx
	 * @return String
	 */
	private Collection<String> fetchExcludedProductsForPromotion(final Flat3Map params, final SessionContext ctx)
	{
		/* * SONAR FIX */
		final StringBuilder promQuery = new StringBuilder(160);
		promQuery.append("SELECT {promotion.excludedProducts} as excludedProducts ");
		promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM);
		//		promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM).append(MarketplacecommerceservicesConstants.PRODUCT_PROMO)
		//				.append(" AS promotion} ");
		final AbstractPromotion promotion = (AbstractPromotion) params.get(MarketplacecommerceservicesConstants.PROMO);
		if (promotion instanceof ProductPromotion)
		{
			promQuery.append(MarketplacecommerceservicesConstants.PRODUCT_PROMO);
		}
		else if (promotion instanceof OrderPromotion)
		{
			promQuery.append(MarketplacecommerceservicesConstants.CART_PROMO);
		}

		promQuery.append(" AS promotion} ");
		promQuery.append("WHERE {promotion.excludedProducts} IS NOT NULL AND {promotion:pk} = ?promo ");

		final List<String> excludedProductStr = getSession().getFlexibleSearch()
				.search(ctx, promQuery.toString(), params, String.class).getResult();

		final String excludedProducts = CollectionUtils.isNotEmpty(excludedProductStr) ? excludedProductStr.get(0) : "";

		return Arrays.asList(excludedProducts.split(","));

	}

	//PR-15 modification ends




	/**
	 * @param params
	 * @param ctx
	 * @param promotionType
	 * @return Collection<String>
	 */
	private Collection<String> fetchBrandsForPromotion(final Flat3Map params, final SessionContext ctx, final String PromotionType)
	{
		/* * SONAR FIX */
		final StringBuilder promQuery = new StringBuilder(150);
		promQuery.append("SELECT {brand.manufacturers} as brands ");
		promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM).append(PromotionType).append(" AS brand} ");
		promQuery.append("WHERE {brand.manufacturers} IS NOT NULL AND {brand.promotion} = ?promo ");

		final List<String> brandStr = getSession().getFlexibleSearch().search(ctx, promQuery.toString(), params, String.class)
				.getResult();

		final String brands = CollectionUtils.isNotEmpty(brandStr) ? brandStr.get(0) : "";

		return Arrays.asList(brands.substring(4).split(","));
	}

	/**
	 * @param brandList
	 * @param PromotionType
	 * @return StringBuilder query
	 */
	private StringBuilder constructBrandQuery(final Collection<String> brandList, final String PromotionType,
			final Flat3Map params)
	{
		final StringBuilder promQuery = new StringBuilder();

		if ((params.get(MarketplacecommerceservicesConstants.SECONDPRODUCT) != null))
		{
			if (StringUtils.isNotEmpty(PromotionType)
					&& PromotionType.equalsIgnoreCase(MarketplacecommerceservicesConstants.BRANDRESTRICTION)
					&& CollectionUtils.isNotEmpty(brandList))
			{
				promQuery.append("SELECT {cat2prod:target} as pk  ");
				promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
						.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS cat2prod } ");
				promQuery.append("WHERE {cat2prod:target} in (?secondProduct) AND {cat2prod:source} in (?brands)");
			}
			else if (StringUtils.isNotEmpty(PromotionType)
					&& PromotionType.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXCLUDEBRANDRESTRICTION)
					&& CollectionUtils.isNotEmpty(brandList))
			{
				promQuery.append("SELECT {cat2prod:target} as pk  ");
				promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
						.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION);
				promQuery.append(" AS cat2prod JOIN ").append(MarketplacecommerceservicesConstants.TYPE_CATEGORY)
						.append(" AS category on {cat2prod:source} = {category.pk}} ");
				promQuery
						.append(
								"WHERE {cat2prod:target} in (?secondProduct) AND {cat2prod:source} not in (?brands) AND {category.code} like '%")
						.append(MarketplacecommerceservicesConstants.BRAND_NAME_PREFIX).append("%'");
			}

		}
		else
		{
			promQuery.append(" INTERSECT ");

			if (StringUtils.isNotEmpty(PromotionType)
					&& PromotionType.equalsIgnoreCase(MarketplacecommerceservicesConstants.BRANDRESTRICTION)
					&& CollectionUtils.isNotEmpty(brandList))
			{
				promQuery.append(SELECT_CAT2PROD_TARGET);
				promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
						.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS cat2prod } ");
				promQuery.append("WHERE {cat2prod:target} in (?product) AND {cat2prod:source} in (?brands) }} ");
			}
			else if (StringUtils.isNotEmpty(PromotionType)
					&& PromotionType.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXCLUDEBRANDRESTRICTION)
					&& CollectionUtils.isNotEmpty(brandList))
			{
				promQuery.append(SELECT_CAT2PROD_TARGET);
				promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM).append(
						GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION);
				promQuery.append(" AS cat2prod JOIN ").append(MarketplacecommerceservicesConstants.TYPE_CATEGORY)
						.append(" AS category on {cat2prod:source} = {category.pk}} ");
				promQuery
						.append(
								"WHERE {cat2prod:target} in (?product) AND {cat2prod:source} not in (?brands) AND {category.code} like '%")
						.append(MarketplacecommerceservicesConstants.BRAND_NAME_PREFIX).append("%' }} ");
			}
		}
		return promQuery;
	}

	/**
	 * @param params
	 * @param promotion
	 * @param ctx
	 * @return StringBuilder query
	 */
	private StringBuilder evaluateBrandRestriction(final Flat3Map params, final AbstractPromotion promotion,
			final SessionContext ctx)
	{
		StringBuilder stringBuilder = null;
		String PromotionType = null;
		Collection<String> brandList = null;

		for (final AbstractPromotionRestriction restriction : promotion.getRestrictions())
		{
			if (restriction instanceof ManufacturesRestriction)
			{
				PromotionType = MarketplacecommerceservicesConstants.BRANDRESTRICTION;
				brandList = fetchBrandsForPromotion(params, ctx, PromotionType);
				break;
			}
			else if (restriction instanceof ExcludeManufacturesRestriction)
			{
				PromotionType = MarketplacecommerceservicesConstants.EXCLUDEBRANDRESTRICTION;
				brandList = fetchBrandsForPromotion(params, ctx, PromotionType);
				break;
			}
		}

		if (CollectionUtils.isNotEmpty(brandList))
		{
			params.put(MarketplacecommerceservicesConstants.BRANDSLIST, brandList);
			stringBuilder = constructBrandQuery(brandList, PromotionType, params);
		}
		return stringBuilder;

	}

	/**
	 * @return the mplCategoryServiceImpl
	 */
	public MplCategoryService getMplCategoryServiceImpl()
	{
		return mplCategoryServiceImpl;
	}

	/**
	 * @param mplCategoryServiceImpl
	 *           the mplCategoryServiceImpl to set
	 */
	public void setMplCategoryServiceImpl(final MplCategoryService mplCategoryServiceImpl)
	{
		this.mplCategoryServiceImpl = mplCategoryServiceImpl;
	}


	/**
	 *
	 * Caching action Class
	 *
	 * @param ctx
	 * @param attributeValues
	 * @return CachedCustomPromotionOrderEntryAdjustAction
	 */
	public CachedCustomPromotionOrderEntryAdjustAction createCachedCustomPromotionOrderEntryAdjustAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CachedCustomPromotionOrderEntryAdjustAction");
			return ((CachedCustomPromotionOrderEntryAdjustAction) type.newInstance(ctx, attributeValues));
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
	 * Caching action Class
	 *
	 * @param ctx
	 * @param attributeValues
	 * @return CachedCustomBOGOPromoOrderEntryAdjustAction
	 */
	public CachedCustomBOGOPromoOrderEntryAdjustAction createCachedCustomBOGOPromoOrderEntryAdjustAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CachedCustomBOGOPromoOrderEntryAdjustAction");
			return ((CachedCustomBOGOPromoOrderEntryAdjustAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CachedCustomBOGOPromoOrderEntryAdjustAction : " + e.getMessage(), 0);
		}
	}

	/**
	 * Caching action Class
	 *
	 * @param ctx
	 * @param attributeValues
	 * @return CachedCustomPromotionOrderAddFreeGiftAction
	 */
	public CachedCustomPromotionOrderAddFreeGiftAction createCachedCustomPromotionOrderAddFreeGiftAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CachedCustomPromotionOrderAddFreeGiftAction");
			return ((CachedCustomPromotionOrderAddFreeGiftAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CachedCustomPromotionOrderAddFreeGiftAction : " + e.getMessage(), 0);
		}
	}

	/**
	 * Caching action Class
	 *
	 * @param ctx
	 * @param attributeValues
	 * @return CachedCustomPromotionOrderAdjustTotalAction
	 */
	private CachedCustomPromotionOrderAdjustTotalAction createCachedCustomPromotionOrderAdjustTotalAction(final SessionContext ctx,
			final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CachedCustomPromotionOrderAdjustTotalAction");
			return ((CachedCustomPromotionOrderAdjustTotalAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CachedCustomPromotionOrderAdjustTotalAction : " + e.getMessage(), 0);
		}
	}

	/**
	 *
	 * Caching action Class
	 *
	 * @param ctx
	 * @param attributeValues
	 * @return CachedCustomShippingChargesPromotionAdjustAction
	 */
	public CachedCustomShippingChargesPromotionAdjustAction createCachedCustomShippingChargesPromotionAdjustAction(
			final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			final ComposedType type = getTenant().getJaloConnection().getTypeManager()
					.getComposedType("CachedCustomShippingChargesPromotionAdjustAction");
			return ((CachedCustomShippingChargesPromotionAdjustAction) type.newInstance(ctx, attributeValues));
		}
		catch (final JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
		}
		catch (final JaloBusinessException e)
		{
			throw new JaloSystemException(e, "error creating CachedCustomShippingChargesPromotionAdjustAction : " + e.getMessage(),
					0);
		}
	}

	private Boolean isCachingAllowed(final SessionContext ctx)
	{
		final Boolean allowed = (Boolean) ctx.getAttribute("de.hybris.platform.promotions.jalo.cachingAllowed");
		return (((allowed == null) || (allowed == Boolean.FALSE)) ? Boolean.FALSE : Boolean.TRUE);
	}

	/*-----PR-15 starts-------*/
	/**
	 * @param promoContext
	 * @param ctx
	 * @param promotion
	 * @return RestrictionSetResult
	 */
	protected PromotionsManager.RestrictionSetResult findEligibleProductsInBasketForCartPromo(final SessionContext ctx,
			final PromotionEvaluationContext promoContext, final AbstractPromotion promotion)
	{
		final Flat3Map params = new Flat3Map();
		params.put(MarketplacecommerceservicesConstants.PROMO, promotion);

		final Collection products = getBaseProductsInBasket(ctx, promoContext.getOrder(), params);

		// Introduced for CAR-326
		if (CollectionUtils.isEmpty(products))
		{
			return new PromotionsManager.RestrictionSetResult(new ArrayList(products)); // Introduced for CAR-326
		}
		else
		{
			params.put(MarketplacecommerceservicesConstants.PRODUCT_IMAGE, products);
		}
		// Code changes for  for CAR-326 ends
		final StringBuilder categoryRestrQuery = evaluateCategoryRestriction(params, promotion, ctx);

		final StringBuilder brandQuery = evaluateBrandRestriction(params, promotion, ctx);

		final StringBuilder promQuery = new StringBuilder("SELECT DISTINCT pprom.pk FROM (");

		if (categoryRestrQuery == null && brandQuery == null)
		{
			return new PromotionsManager.RestrictionSetResult(new ArrayList(products));
		}
		else if (categoryRestrQuery != null && brandQuery != null)
		{
			promQuery.append(categoryRestrQuery);
			promQuery.append(brandQuery);
		}
		else if (categoryRestrQuery != null && brandQuery == null)
		{
			promQuery.append(categoryRestrQuery);
		}
		else if (categoryRestrQuery == null && brandQuery != null)
		{
			brandQuery.delete(0, 11);//to avoid INTERSECT in the query
			promQuery.append(brandQuery);

		}

		if (!(Config.isOracleUsed()))
		{
			promQuery.append(AS_PPROM);
		}
		else
		{
			promQuery.append(PPROM);
		}

		final List cartProducts = getSession().getFlexibleSearch().search(ctx, promQuery.toString(), params, Product.class)
				.getResult();

		if (promoContext.getObserveRestrictions())
		{
			return PromotionsManager.getInstance().evaluateRestrictions(ctx, cartProducts, promoContext.getOrder(), promotion,
					promoContext.getDate());
		}

		return new PromotionsManager.RestrictionSetResult(cartProducts);

	}


	/**
	 * @param params
	 * @param promotion
	 * @param ctx
	 * @return StringBuilder query
	 */
	private StringBuilder evaluateCategoryRestriction(final Flat3Map params, final AbstractPromotion promotion,
			final SessionContext ctx)
	{
		StringBuilder stringBuilder = null;
		//String PromotionType = null;
		Collection<String> categoryList = null;
		boolean isIncluded = false;

		for (final AbstractPromotionRestriction restriction : promotion.getRestrictions())
		{
			if (restriction instanceof CategoryRestriction)
			{
				//				if (((CategoryRestriction) restriction).isIncludedAsPrimitive(ctx))
				//				{
				//PromotionType = MarketplacecommerceservicesConstants.CATEGORYRESTRICTION;
				isIncluded = ((CategoryRestriction) restriction).isIncludedAsPrimitive(ctx);
				categoryList = fetchCategoriesForPromotion(params, ctx, MarketplacecommerceservicesConstants.CATEGORYRESTRICTION);
				break;
				//}
			}
			//else if (restriction instanceof ExcludeManufacturesRestriction)
			//{
			//	PromotionType = MarketplacecommerceservicesConstants.EXCLUDECATEGORYRESTRICTION;
			//	categoryList = fetchCategoriesForPromotion(params, ctx, PromotionType);
			//	break;
			//}
		}

		if (CollectionUtils.isNotEmpty(categoryList))
		{
			params.put(MarketplacecommerceservicesConstants.PROMO_CATEGORIES, categoryList);
			stringBuilder = constructCategoryQuery(isIncluded);
		}
		return stringBuilder;

	}

	/**
	 * @param params
	 * @param ctx
	 * @param promotionType
	 * @return Collection<String>
	 */
	private Collection<String> fetchCategoriesForPromotion(final Flat3Map params, final SessionContext ctx,
			final String PromotionType)
	{
		/* * SONAR FIX */
		final StringBuilder promQuery = new StringBuilder(150);
		promQuery.append("SELECT {category.categories} as categories ");
		promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM).append(PromotionType).append(" AS category} ");
		promQuery.append("WHERE {category.categories} IS NOT NULL AND {category.promotion} = ?promo ");

		final List<String> categoryStr = getSession().getFlexibleSearch().search(ctx, promQuery.toString(), params, String.class)
				.getResult();

		final String categories = CollectionUtils.isNotEmpty(categoryStr) ? categoryStr.get(0) : "";

		return Arrays.asList(categories.substring(4).split(","));
	}

	/**
	 * @param isIncluded
	 * @return StringBuilder query
	 */
	private StringBuilder constructCategoryQuery(final boolean isIncluded)
	{
		final StringBuilder promQuery = new StringBuilder();

		if (isIncluded)
		{
			promQuery.append(SELECT_CAT2PROD_TARGET);
			promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM)
					.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS cat2prod } ");
			promQuery.append("WHERE {cat2prod:target} in (?product) AND {cat2prod:source} in (?categories) }} ");
		}
		else
		{
			promQuery.append(SELECT_CAT2PROD_TARGET);
			promQuery.append(MarketplacecommerceservicesConstants.QUERYFROM).append(
					GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION);
			promQuery.append(" AS cat2prod JOIN ").append(MarketplacecommerceservicesConstants.TYPE_CATEGORY)
					.append(" AS category on {cat2prod:source} = {category.pk}} ");
			promQuery
					.append(
							"WHERE {cat2prod:target} in (?product) AND {cat2prod:source} not in (?categories) AND {category.code} like '%")
					.append(MarketplacecommerceservicesConstants.SELLER_NAME_PREFIX).append("%' }} ");
		}

		return promQuery;
	}

	/*-----PR-15 ends-------*/
}
