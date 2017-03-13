/**
 *
 */
package com.tisl.mpl.promotion.test;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService;
import com.tisl.mpl.model.CityModel;
import com.tisl.mpl.model.DeliveryModePromotionRestrictionModel;
import com.tisl.mpl.model.StateModel;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.servicelayer.MplServicelayerTest;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "unused", "deprecation" })
public class DefaultPromotionManagerTest extends MplServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultPromotionManagerTest.class);
	private ProductModel product1;
	private ProductModel product2;
	private CatalogVersionModel version;
	private UserModel user;
	private CurrencyModel currency;
	private CartModel cart;
	private DefaultPromotionManager defaultPromotionManager;
	private MplCategoryService mplCategoryServiceImpl;
	private ProductService productService;
	private ModelService modelService;
	private ConfigurationService configurationService;
	private CatalogVersionService catalogVersionService;
	private CategoryService categoryService;
	private CartService cartService;
	private Configuration configuration;
	private FlexibleSearchService flexibleSearchService;
	private SellerBasedPromotionService sellerBasedPromotionService;
	private AbstractOrderModel abstractOrder;
	private AbstractOrderEntryModel abstractOrderEntry;
	private OrderModel order;
	private ItemModel state;
	private ItemModel city;
	private AbstractOrder abstOrder;
	private StateModel statemodel;
	private CityModel citymodel;

	@Before
	public void setUp() throws Exception
	{
		LOG.info("Setting Up Data");
		createDefaultUsers();
		MockitoAnnotations.initMocks(this);
		this.version = Mockito.mock(CatalogVersionModel.class);
		this.product1 = Mockito.mock(ProductModel.class);
		this.product2 = Mockito.mock(ProductModel.class);
		this.mplCategoryServiceImpl = Mockito.mock(MplCategoryService.class);
		this.productService = Mockito.mock(ProductService.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.configurationService = Mockito.mock(ConfigurationService.class);
		this.catalogVersionService = Mockito.mock(CatalogVersionService.class);
		this.categoryService = Mockito.mock(CategoryService.class);
		this.cartService = Mockito.mock(CartService.class);
		this.flexibleSearchService = Mockito.mock(FlexibleSearchService.class);
		this.sellerBasedPromotionService = Mockito.mock(SellerBasedPromotionService.class);
		this.configuration = Mockito.mock(Configuration.class);
		this.abstractOrderEntry = Mockito.mock(AbstractOrderEntryModel.class);
		this.abstractOrder = Mockito.mock(AbstractOrderModel.class);
		this.order = Mockito.mock(OrderModel.class);
		this.state = Mockito.mock(ItemModel.class);
		this.city = Mockito.mock(ItemModel.class);
		this.abstOrder = Mockito.mock(AbstractOrder.class);
		this.statemodel = Mockito.mock(StateModel.class);
		this.citymodel = Mockito.mock(CityModel.class);
		defaultPromotionManager = new DefaultPromotionManager();
		defaultPromotionManager.setMplCategoryServiceImpl(mplCategoryServiceImpl);
		defaultPromotionManager.setProductService(productService);
		defaultPromotionManager.setModelService(modelService);
		defaultPromotionManager.setConfigurationService(configurationService);
		defaultPromotionManager.setCatalogVersionService(catalogVersionService);
		defaultPromotionManager.setCategoryService(categoryService);
		defaultPromotionManager.setCartService(cartService);
		getCatalogData();
		product1 = getProductData("987654381");
		product2 = getProductData("987654391");
		setUser("2000000018");
		setCurrency("INR");
		cart = getCart();
	}

	@SuppressWarnings("deprecation")
	private void getCatalogData()
	{
		final CatalogVersionService catalog = (CatalogVersionService) Registry.getApplicationContext().getBean(
				"catalogVersionService");
		junit.framework.Assert.assertNotNull(catalog);
		version = catalog.getCatalogVersion("mplProductCatalog", "Online");
		junit.framework.Assert.assertNotNull(version);
		catalog.addSessionCatalogVersion(version);

	}

	/**
	 * @param producCode
	 * @return ProductModel
	 */
	@SuppressWarnings("deprecation")
	private ProductModel getProductData(final String producCode)
	{
		final ProductService product = (ProductService) Registry.getApplicationContext().getBean("productService");
		junit.framework.Assert.assertNotNull(product);
		return product.getProductForCode(producCode);
	}

	@SuppressWarnings("deprecation")
	private void setCurrency(final String currencyData)
	{
		final CommonI18NService currencysvc = (CommonI18NService) Registry.getApplicationContext().getBean("commonI18NService");
		junit.framework.Assert.assertNotNull(currencysvc);
		currency = currencysvc.getCurrency(currencyData);
		junit.framework.Assert.assertNotNull(currency);
		currencysvc.setCurrentCurrency(currency);
	}

	/**
	 * @param userUID
	 */
	@SuppressWarnings("deprecation")
	private void setUser(final String userUID)
	{
		final UserService usersvc = (UserService) Registry.getApplicationContext().getBean("userService");
		junit.framework.Assert.assertNotNull(usersvc);
		user = usersvc.getUserForUID(userUID);
		junit.framework.Assert.assertNotNull(user);
		usersvc.setCurrentUser(user);
	}

	@SuppressWarnings("deprecation")
	private void testBrandDataCheck()
	{
		final boolean checkflag = false;
		final String configbrandName1 = "Pantaloons".intern();
		final String configbrandName2 = "Westside".intern();
		final String configbrandName3 = "TataRetailer".intern();
		final List<String> brandList = new ArrayList<String>();
		brandList.add(configbrandName1);
		brandList.add(configbrandName2);
		brandList.add(configbrandName3);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final Product product = modelService.getSource(product1);
		final boolean flag = defaultPromotionManager.brandDataCheck(brandList, product);
		Assert.assertEquals(checkflag, flag);
	}

	//TODO
	//	@SuppressWarnings("deprecation")
	//	private void testGetCategoryData()
	//	{
	//		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
	//		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID))
	//				.thenReturn("mplProductCatalog");
	//		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID)).thenReturn("Online");
	//		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
	//				"flexibleSearchService");
	//		junit.framework.Assert.assertNotNull(flexibleSearchService);
	//		final ProductModel product = (ProductModel) flexibleSearchService
	//				.search("SELECT {PK} FROM {Product} WHERE {code}='987654381'").getResult().get(0);
	//		junit.framework.Assert.assertNotNull(product);
	//		final List<CategoryModel> productCategoryData = new ArrayList<>(product.getSupercategories());
	//		final Collection<CategoryModel> superCategoryData = mplCategoryServiceImpl
	//				.getAllSupercategoriesForCategoryList(productCategoryData);
	//		Mockito.when(mplCategoryServiceImpl.getAllSupercategoriesForCategoryList(productCategoryData))
	//				.thenReturn(superCategoryData);
	//		final List<CategoryModel> flag = defaultPromotionManager.getcategoryData(product);
	//		Assert.assertNotNull(flag);
	//	}

	@SuppressWarnings("deprecation")
	private void testGetGiftProducts()
	{
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID))
				.thenReturn("mplProductCatalog");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID)).thenReturn("Online");

		final List<Product> productList = new ArrayList<>();
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final Product product_1 = modelService.getSource(product1);
		final Product product_2 = modelService.getSource(product2);
		productList.add(product_1);
		productList.add(product_2);
		final String sellerID = "123654";
		final Map<String, Product> flag = defaultPromotionManager.getGiftProducts(productList, sellerID);
		Assert.assertNotNull(flag);
	}


	@SuppressWarnings("deprecation")
	private void testGetCartPromoGiftProducts()
	{
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID))
				.thenReturn("mplProductCatalog");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID)).thenReturn("Online");
		final List<Product> productList = new ArrayList<>();
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final Product product_1 = modelService.getSource(product1);
		final Product product_2 = modelService.getSource(product2);
		productList.add(product_1);
		productList.add(product_2);
		final Map<String, Product> flag = defaultPromotionManager.getCartPromoGiftProducts(productList);
		Assert.assertNotNull(flag);
	}

	@SuppressWarnings("deprecation")
	private void testGetGiftProductsUSSID()
	{
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID))
				.thenReturn("mplProductCatalog");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID)).thenReturn("Online");
		final List<Product> productList = new ArrayList<>();
		final List<String> sellerDetails = new ArrayList<>();
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		final Product product_1 = modelService.getSource(product1);
		final Product product_2 = modelService.getSource(product2);
		productList.add(product_1);
		productList.add(product_2);
		sellerDetails.add("WESTSIDE");
		sellerDetails.add("PANTALOONS");
		sellerDetails.add("TataRetailer");
		final Map<String, Product> flag = defaultPromotionManager.getGiftProductsUSSID(productList, sellerDetails);
		Assert.assertNotNull(flag);
	}


	@SuppressWarnings("deprecation")
	private void testGetProdShippingPromoAppliedMap()
	{
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID))
				.thenReturn("mplProductCatalog");
		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID)).thenReturn("Online");
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {pk}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);
		Mockito.when((AbstractOrderModel) getModelService().get(order)).thenReturn(cart);
		final Map<String, Boolean> flag = defaultPromotionManager.getProdShippingPromoAppliedMap(order);
		Assert.assertNotNull(flag);
	}

	@SuppressWarnings("deprecation")
	private CartModel getCart()
	{
		final CartService cartSvc = (CartService) Registry.getApplicationContext().getBean("cartService");
		junit.framework.Assert.assertNotNull(cartSvc);
		cart = createCart();
		cartSvc.addNewEntry(cart, product1, 1, product1.getUnit());
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		modelService.save(cart);
		return cart;
	}


	private CartModel createCart()
	{
		final String cartModelTypeCode = Config.getString(JaloSession.CART_TYPE, "Cart");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		final CartModel cartModel = modelService.create(cartModelTypeCode);
		cartModel.setUser(user);
		cartModel.setCurrency(currency);
		cartModel.setCode(UUID.randomUUID().toString());
		cartModel.setDate(new Date());
		cartModel.setNet(Boolean.TRUE);
		modelService.save(cartModel);
		return cartModel;
	}

	@SuppressWarnings("deprecation")
	private void testGetDelModeRestrEvalForOrderPromo()
	{
		final boolean checkflag = false;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final DeliveryModePromotionRestrictionModel deliveryModePromotionRestriction = new DeliveryModePromotionRestrictionModel();
		final List<DeliveryModeModel> deliveryModeList = new ArrayList<DeliveryModeModel>();
		deliveryModeList.add(deliveryMode1);
		deliveryModePromotionRestriction.setDeliveryModeDetailsList(deliveryModeList);
		modelService.save(deliveryModePromotionRestriction);
		final AbstractPromotionRestriction abstractPromotionRestriction = modelService.getSource(deliveryModePromotionRestriction);
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>();
		restrictionList.add(abstractPromotionRestriction);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {PK}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);
		Mockito.when((AbstractOrderModel) getModelService().get(order)).thenReturn(cart);
		final boolean flag = defaultPromotionManager.getDelModeRestrEvalForOrderPromo(restrictionList, order);
		Assert.assertEquals(checkflag, flag);
	}

	@SuppressWarnings("deprecation")
	private void testFetchProductRichAttribute()
	{
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final Map<String, Integer> qCount = new HashMap<String, Integer>();
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final String USSID = "123654098765485130011712";
		final Integer qulifyingcount = Integer.valueOf(1);
		qCount.put(USSID, qulifyingcount);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {PK}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);
		Mockito.when((AbstractOrderModel) getModelService().get(order)).thenReturn(cart);
		final Map<String, String> flag = defaultPromotionManager.fetchProductRichAttribute(qCount, order);
		Assert.assertNotNull(flag);
	}

	private ModelService getModelService()
	{
		return modelService;
	}

	@SuppressWarnings("deprecation")
	private void testIsSellerRestrExists()
	{
		final boolean checkflag = false;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final DeliveryModePromotionRestrictionModel deliveryModePromotionRestriction = new DeliveryModePromotionRestrictionModel();
		final List<DeliveryModeModel> deliveryModeList = new ArrayList<DeliveryModeModel>();
		deliveryModeList.add(deliveryMode1);
		deliveryModePromotionRestriction.setDeliveryModeDetailsList(deliveryModeList);
		modelService.save(deliveryModePromotionRestriction);
		final AbstractPromotionRestriction abstractPromotionRestriction = modelService.getSource(deliveryModePromotionRestriction);
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>();
		restrictionList.add(abstractPromotionRestriction);
		final boolean flag = defaultPromotionManager.isSellerRestrExists(restrictionList);
		Assert.assertEquals(checkflag, flag);
	}


	@SuppressWarnings("deprecation")
	private void testGetQualifyingCountForABPromotion()
	{
		final String ussid1 = "123654098765485130011712";
		final String ussid2 = "123654098765485130011713";
		final String ussid3 = "123654098765485130011714";
		final int totalCountFactor = 3;
		final List<String> ussid = new ArrayList<String>();
		ussid.add(ussid1);
		ussid.add(ussid2);
		ussid.add(ussid3);
		final Map<String, Integer> itemLevelQC = defaultPromotionManager.getQualifyingCountForABPromotion(ussid, totalCountFactor);
		Assert.assertNotNull(itemLevelQC);
	}

	@SuppressWarnings("deprecation")
	private void testGetDelModeRestrEvalForAPromo()
	{
		final boolean checkflag = false;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final String selectedUSSID = "123654098765485130011712";
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final DeliveryModePromotionRestrictionModel deliveryModePromotionRestriction = new DeliveryModePromotionRestrictionModel();
		final List<DeliveryModeModel> deliveryModeList = new ArrayList<DeliveryModeModel>();
		deliveryModeList.add(deliveryMode1);
		deliveryModePromotionRestriction.setDeliveryModeDetailsList(deliveryModeList);
		modelService.save(deliveryModePromotionRestriction);
		final AbstractPromotionRestriction abstractPromotionRestriction = modelService.getSource(deliveryModePromotionRestriction);
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>();
		restrictionList.add(abstractPromotionRestriction);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {PK}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);

		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		validProductUssidMap.put(selectedUSSID, abstractOrderEntry);
		Mockito.when((AbstractOrderModel) getModelService().get(order)).thenReturn(cart);
		final boolean flag = defaultPromotionManager.getDelModeRestrEvalForAPromo(restrictionList, validProductUssidMap, order);
		Assert.assertEquals(checkflag, flag);
	}

	@SuppressWarnings("deprecation")
	private void testGetDelModeRestrEvalForABPromo()
	{
		final boolean checkflag = false;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final String selectedUSSID = "123654098765485130011712";
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final DeliveryModePromotionRestrictionModel deliveryModePromotionRestriction = new DeliveryModePromotionRestrictionModel();
		final List<DeliveryModeModel> deliveryModeList = new ArrayList<DeliveryModeModel>();
		deliveryModeList.add(deliveryMode1);
		deliveryModePromotionRestriction.setDeliveryModeDetailsList(deliveryModeList);
		modelService.save(deliveryModePromotionRestriction);
		final AbstractPromotionRestriction abstractPromotionRestriction = modelService.getSource(deliveryModePromotionRestriction);
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>();
		restrictionList.add(abstractPromotionRestriction);
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {PK}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		validProductUssidMap.put(selectedUSSID, abstractOrderEntry);
		Mockito.when((AbstractOrderModel) getModelService().get(order)).thenReturn(cart);
		final boolean flag = defaultPromotionManager.getDelModeRestrEvalForAPromo(restrictionList, validProductUssidMap, order);
		Assert.assertEquals(checkflag, flag);
	}

	@SuppressWarnings("deprecation")
	private void testGetTotalValidProdPrice()
	{
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final String selectedUSSID = "123654098765485130011712";
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		validProductUssidMap.put(selectedUSSID, abstractOrderEntry);
		final Map<String, Integer> validProductList = new HashMap<String, Integer>();
		final int totalCountFactor = 3;
		validProductList.put(selectedUSSID, Integer.valueOf(totalCountFactor));
		final double totalvalidproductsPricevalue = defaultPromotionManager.getTotalValidProdPrice(validProductUssidMap,
				validProductList);
		Assert.assertNotNull(String.valueOf(totalvalidproductsPricevalue));
	}


	@SuppressWarnings("deprecation")
	private void testGetTotalDelCostForValidProds()
	{
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final String selectedUSSID = "123654098765485130011712";
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);

		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		validProductUssidMap.put(selectedUSSID, abstractOrderEntry);
		final Map<String, Integer> qCountMap = new HashMap<String, Integer>();
		final int totalCountFactor = 3;
		qCountMap.put(selectedUSSID, Integer.valueOf(totalCountFactor));
		final double totalDeliveryCostForValidProds = defaultPromotionManager.getTotalValidProdPrice(validProductUssidMap,
				qCountMap);
		Assert.assertNotNull(String.valueOf(totalDeliveryCostForValidProds));
	}

	@SuppressWarnings("deprecation")
	private void testGetAssociatedItemsForABorFreebiePromotions()
	{
		final String ussid1 = "123654098765485130011712";
		final String ussid2 = "123654098765485130011713";
		final String ussid3 = "123654098765485130011714";
		final String ussid4 = "123654098765485130011715";
		final String freebieussid = "123654098765485130011716";
		final List<String> ussidA = new ArrayList<String>();
		ussidA.add(ussid1);
		ussidA.add(ussid2);
		final List<String> ussidB = new ArrayList<String>();
		ussidB.add(ussid3);
		ussidB.add(ussid4);
		Map<String, List<String>> productAssociatedItemsMap = new HashMap<String, List<String>>();
		productAssociatedItemsMap = defaultPromotionManager
				.getAssociatedItemsForABorFreebiePromotions(ussidA, ussidB, freebieussid);
		Assert.assertNotNull(productAssociatedItemsMap);

	}


	@SuppressWarnings("deprecation")
	private void testGetAssociatedItemsForAFreebiePromotions()
	{
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final String selectedUSSID = "123654098765485130011712";
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		validProductUssidMap.put(selectedUSSID, abstractOrderEntry);
		final String freebieussid1 = "123654098765485130011713";
		final String freebieussid2 = "123654098765485130011714";
		final List<String> freebieussid = new ArrayList<String>();
		freebieussid.add(freebieussid1);
		freebieussid.add(freebieussid2);
		Map<String, List<String>> productAssociatedItemsMap = new HashMap<String, List<String>>();
		productAssociatedItemsMap = defaultPromotionManager.getAssociatedItemsForAFreebiePromotions(validProductUssidMap,
				freebieussid);
		Assert.assertNotNull(productAssociatedItemsMap);
	}

	//TODO
	//	@SuppressWarnings("deprecation")
	//	private void testCheckPincodeSpecificRestriction()
	//	{
	//		final boolean checkflag = false;
	//		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
	//		junit.framework.Assert.assertNotNull(modelService);
	//		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
	//				"flexibleSearchService");
	//		junit.framework.Assert.assertNotNull(flexibleSearchService);
	//
	//		final ItemModel state = (ItemModel) flexibleSearchService.search("SELECT {PK} FROM {State} WHERE {PK}='8796093473276'")
	//				.getResult().get(0);
	//		junit.framework.Assert.assertNotNull(state);
	//		final State stmodel = modelService.getSource(state);
	//		final ItemModel city = (ItemModel) flexibleSearchService.search("SELECT {PK} FROM {City} WHERE {PK}='8796125823075'")
	//				.getResult().get(0);
	//		junit.framework.Assert.assertNotNull(city);
	//		final City ctmodel = modelService.getSource(city);
	//		final MplPincodeSpecificRestriction restriction = new MplPincodeSpecificRestriction();
	//		final List<State> includedStates = new ArrayList<State>();
	//		includedStates.add(stmodel);
	//		final List<City> excudedCity = new ArrayList<City>();
	//		excudedCity.add(ctmodel);
	//		restriction.setStates(includedStates);
	//		restriction.setCities(excudedCity);
	//		final AbstractPromotionRestrictionModel abstractPromotionRestriction = (AbstractPromotionRestrictionModel) flexibleSearchService
	//				.search("SELECT {PK} FROM {AbstractPromotionRestriction} WHERE {PK}='8796125827987'").getResult().get(0);
	//		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>();
	//		final AbstractPromotionRestriction abstractPromotionRestriction1 = modelService.getSource(abstractPromotionRestriction);
	//		restrictionList.add(abstractPromotionRestriction1);
	//		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {PK}='8796093055019'")
	//				.getResult().get(0);
	//		junit.framework.Assert.assertNotNull(cart);
	//		cart.setStateForPincode("Maharashtra");
	//		cart.setCityForPincode("Mumbai");
	//		final AbstractOrder order = modelService.getSource(cart);
	//		Mockito.when((AbstractOrderModel) getModelService().get(order)).thenReturn(cart);
	//		Mockito.when(restriction.getStates()).thenReturn(includedStates);
	//		Mockito.when(restriction.getCities()).thenReturn(excudedCity);
	//		final boolean flag = defaultPromotionManager.checkPincodeSpecificRestriction(restrictionList, order);
	//		Assert.assertEquals(checkflag, flag);
	//	}

	@SuppressWarnings("deprecation")
	private void testGetFreeGiftCount()
	{
		final String sellerId = "123654";
		final String keyUssid = "123654098765485130011712";
		final int count = 3;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final Map<AbstractOrderEntry, String> eligibleProductMap = new HashMap<AbstractOrderEntry, String>();
		eligibleProductMap.put(abstractOrderEntry, sellerId);
		final int giftcount = defaultPromotionManager.getFreeGiftCount(keyUssid, eligibleProductMap, count);
		Assert.assertNotNull(Integer.valueOf(giftcount));
	}


	@SuppressWarnings("deprecation")
	private void testMinimumBrandAmountCheck()
	{
		final boolean checkflag = false;
		final String manufacture1 = "Pantaloons".intern();
		final List<String> manufactureList = new ArrayList<String>();
		manufactureList.add(manufacture1);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final String selectedUSSID = "123654098765485130011712";
		final Map<String, AbstractOrderEntry> minBrandAmountMap = new HashMap<String, AbstractOrderEntry>();
		minBrandAmountMap.put(selectedUSSID, abstractOrderEntry);
		final double minimumManufactureAmountTest = 1000.00;
		final boolean flag = defaultPromotionManager.minimumBrandAmountCheck(manufactureList, minBrandAmountMap,
				minimumManufactureAmountTest);
		Assert.assertEquals(checkflag, flag);

	}

	//TODO
	//	@SuppressWarnings("deprecation")
	//	private void testGetPrimarycategoryData()
	//	{
	//		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
	//		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGID))
	//				.thenReturn("mplProductCatalog");
	//		Mockito.when(configuration.getString(MarketplacecommerceservicesConstants.DEFAULTCATALOGVERISONID)).thenReturn("Online");
	//		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
	//				"flexibleSearchService");
	//		junit.framework.Assert.assertNotNull(flexibleSearchService);
	//		final ProductModel product = (ProductModel) flexibleSearchService
	//				.search("SELECT {PK} FROM {Product} WHERE {code}='987654381'").getResult().get(0);
	//		junit.framework.Assert.assertNotNull(product);
	//		final List<CategoryModel> productCategoryData = defaultPromotionManager.getPrimarycategoryData(product);
	//		Assert.assertNotNull(productCategoryData);
	//
	//	}


	@SuppressWarnings(
	{ "deprecation", "unused" })
	private void testCalcDeliveryCharges()
	{
		final double charge = 100.00;
		final boolean flag = true;
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMap = new HashMap<String, Map<String, Double>>();
		final Map<String, Double> prodPrevCurrDelChargeMap1 = new HashMap<String, Double>();
		prodPrevCurrDelChargeMap1.put(MarketplacecommerceservicesConstants.PREVDELIVERYCHARGE, new Double(123.22));
		prodPrevCurrDelChargeMap1.put(MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE, new Double(100.00));
		final String selectedUSSID = "123654098765485130011712";
		prodPrevCurrDelChargeMap.put(selectedUSSID, prodPrevCurrDelChargeMap1);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {pk}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);

		final Map<String, Boolean> isProdShippingPromoAppliedMap = new HashMap<String, Boolean>();
		isProdShippingPromoAppliedMap.put(selectedUSSID, Boolean.TRUE);
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final List<AbstractOrderEntryModel> abstractOrder = cart.getEntries();
		junit.framework.Assert.assertNotNull(abstractOrder);
		final AbstractOrderEntryModel abstractOrder1 = abstractOrder.get(0);
		abstractOrder1.setSelectedUSSID(selectedUSSID);
		abstractOrder1.setDeliveryMode(deliveryMode1);
		final AbstractOrderEntry entryJalo = modelService.getSource(abstractOrder1);
		Mockito.when((AbstractOrderEntryModel) getModelService().get(entryJalo)).thenReturn(abstractOrder1);
		final Map<String, Map<String, Double>> prodPrevCurrDelChargeMapComp = defaultPromotionManager.calcDeliveryCharges(flag,
				charge, selectedUSSID, order, isProdShippingPromoAppliedMap);
		Assert.assertNotNull(prodPrevCurrDelChargeMapComp);
	}


	@SuppressWarnings(
	{ "deprecation", "unused" })
	private void testIsExSellerRestrExists()
	{
		final boolean checkflag = false;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final DeliveryModePromotionRestrictionModel deliveryModePromotionRestriction = new DeliveryModePromotionRestrictionModel();
		final List<DeliveryModeModel> deliveryModeList = new ArrayList<DeliveryModeModel>();
		deliveryModeList.add(deliveryMode1);
		deliveryModePromotionRestriction.setDeliveryModeDetailsList(deliveryModeList);
		modelService.save(deliveryModePromotionRestriction);
		final AbstractPromotionRestriction abstractPromotionRestriction = modelService.getSource(deliveryModePromotionRestriction);
		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>();
		restrictionList.add(abstractPromotionRestriction);
		final boolean restrictionListComp = defaultPromotionManager.isSellerRestrExists(restrictionList);
		Assert.assertEquals(checkflag, restrictionListComp);

	}


	@SuppressWarnings("deprecation")
	private void testGetAssociatedItemsForAorBOGOorFreebiePromotions()
	{
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final String selectedUSSID = "123654098765485130011712";
		final String skuForFreebie = "123654098765485130011713";
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		validProductUssidMap.put(selectedUSSID, abstractOrderEntry);
		Map<String, List<String>> productAssociatedItemsMap = new HashMap<String, List<String>>();
		productAssociatedItemsMap = defaultPromotionManager.getAssociatedItemsForAorBOGOorFreebiePromotions(validProductUssidMap,
				skuForFreebie);
		Assert.assertNotNull(productAssociatedItemsMap);
	}


	@SuppressWarnings("deprecation")
	private void testGetvalidProdQCForOrderShippingPromotion()
	{
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final Map<String, Integer> validProdQCountMap = new HashMap<String, Integer>();
		validProdQCountMap.put("123654098765485130011712", new Integer(1));
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final DeliveryMode deliveryMode = modelService.getSource(deliveryMode1);
		final List<DeliveryMode> deliveryModeList = new ArrayList<DeliveryMode>();
		deliveryModeList.add(deliveryMode);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {PK}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);
		Mockito.when((AbstractOrderModel) getModelService().get(order)).thenReturn(cart);
		final Map<String, Integer> validProdQCountMapComp = defaultPromotionManager.getvalidProdQCForOrderShippingPromotion(
				deliveryModeList, order);
		Assert.assertNotNull(validProdQCountMapComp);
	}

	@SuppressWarnings("deprecation")
	private void testGetConvertedPercentageDiscount()
	{
		final Long obj = Long.valueOf(5l);
		final double percentageDiscountComp = defaultPromotionManager.getConvertedPercentageDiscount(10, 900.00d, obj, 1000.00d);
		Assert.assertNotNull(Double.valueOf(percentageDiscountComp));

	}


	@Test
	public void test() throws Exception
	{
		LOG.info("Running Test Data ");
		testBrandDataCheck();
		//TODO
		//testGetCategoryData();
		testGetGiftProducts();
		testGetCartPromoGiftProducts();
		testGetGiftProductsUSSID();
		testGetProdShippingPromoAppliedMap();
		testGetDelModeRestrEvalForOrderPromo();
		testFetchProductRichAttribute();
		testIsSellerRestrExists();
		testGetQualifyingCountForABPromotion();
		testGetDelModeRestrEvalForAPromo();
		testGetDelModeRestrEvalForABPromo();
		testGetTotalValidProdPrice();
		testGetTotalDelCostForValidProds();
		testGetAssociatedItemsForABorFreebiePromotions();
		testGetAssociatedItemsForAFreebiePromotions();
		//TODO
		//testCheckPincodeSpecificRestriction();
		testGetFreeGiftCount();
		testMinimumBrandAmountCheck();
		//TODO
		//testGetPrimarycategoryData();
		testGetAssociatedItemsForAorBOGOorFreebiePromotions();
		testCalcDeliveryCharges();
		testIsExSellerRestrExists();
		testGetvalidProdQCForOrderShippingPromotion();
		testGetConvertedPercentageDiscount();
	}

	@After
	public void tearDown()
	{
		LOG.info("Tear Down Method");
	}

}
