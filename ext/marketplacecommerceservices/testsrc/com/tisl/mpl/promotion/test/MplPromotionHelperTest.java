/**
 *
 */
package com.tisl.mpl.promotion.test;


import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
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
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
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

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;
import com.tisl.mpl.servicelayer.MplServicelayerTest;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "unused", "deprecation" })
public class MplPromotionHelperTest extends MplServicelayerTest
{

	private static final Logger LOG = Logger.getLogger(MplPromotionHelperTest.class);
	private ProductModel product1;
	private ProductModel product2;
	private CatalogVersionModel version;
	private UserModel user;
	private CurrencyModel currency;
	private CartModel cart;
	private MplPromotionHelper mplPromotionHelper;
	private ProductService productService;
	private ModelService modelService;
	private CatalogVersionService catalogVersionService;
	private CartService cartService;
	private FlexibleSearchService flexibleSearchService;
	private AbstractOrderModel abstractOrder;
	private AbstractOrderEntryModel abstractOrderEntry;
	private OrderModel order;
	private AbstractOrder abstOrder;
	private DefaultPromotionManager defaultPromotionManager;

	@Before
	public void setUp() throws Exception
	{
		LOG.info("Setting Up Data");
		createDefaultUsers();
		MockitoAnnotations.initMocks(this);
		this.version = Mockito.mock(CatalogVersionModel.class);
		this.product1 = Mockito.mock(ProductModel.class);
		this.product2 = Mockito.mock(ProductModel.class);
		this.productService = Mockito.mock(ProductService.class);
		this.cartService = Mockito.mock(CartService.class);
		this.flexibleSearchService = Mockito.mock(FlexibleSearchService.class);
		this.abstractOrderEntry = Mockito.mock(AbstractOrderEntryModel.class);
		this.abstractOrder = Mockito.mock(AbstractOrderModel.class);
		this.order = Mockito.mock(OrderModel.class);
		this.abstOrder = Mockito.mock(AbstractOrder.class);
		this.defaultPromotionManager = Mockito.mock(DefaultPromotionManager.class);
		mplPromotionHelper = new MplPromotionHelper();
		mplPromotionHelper.setCartService(cartService);

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


	private ModelService getModelService()
	{
		return modelService;
	}

	//TODO
	//	@SuppressWarnings("deprecation")
	//	private void testCheckMultipleSeller()
	//	{
	//		final boolean checkflag = false;
	//		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
	//		junit.framework.Assert.assertNotNull(modelService);
	//		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
	//				"flexibleSearchService");
	//		junit.framework.Assert.assertNotNull(flexibleSearchService);
	//		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
	//				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
	//		junit.framework.Assert.assertNotNull(deliveryMode1);
	//		final DeliveryModePromotionRestrictionModel deliveryModePromotionRestriction = new DeliveryModePromotionRestrictionModel();
	//		final List<DeliveryModeModel> deliveryModeList = new ArrayList<DeliveryModeModel>();
	//		deliveryModeList.add(deliveryMode1);
	//		deliveryModePromotionRestriction.setDeliveryModeDetailsList(deliveryModeList);
	//		modelService.save(deliveryModePromotionRestriction);
	//		final AbstractPromotionRestriction abstractPromotionRestriction = modelService.getSource(deliveryModePromotionRestriction);
	//		final List<AbstractPromotionRestriction> restrictionList = new ArrayList<AbstractPromotionRestriction>();
	//		restrictionList.add(abstractPromotionRestriction);
	//		final boolean flag = mplPromotionHelper.checkMultipleSeller(restrictionList);
	//		Assert.assertEquals(checkflag, flag);
	//	}

	private void testGetAssociatedData()
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
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {PK}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);
		final Map<String, AbstractOrderEntry> validProductUssidMap = new HashMap<String, AbstractOrderEntry>();
		validProductUssidMap.put(selectedUSSID, abstractOrderEntry);
		final String skuFreebie = "123654098765485130011712";
		final Map<String, List<String>> flag = mplPromotionHelper.getAssociatedData(order, validProductUssidMap, skuFreebie);
		Assert.assertNotNull(flag);
	}

	private void testValidateEntryForFreebie()
	{
		final boolean checkflag = false;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final boolean flag = mplPromotionHelper.validateEntryForFreebie(abstractOrderEntry);
		Assert.assertEquals(checkflag, flag);
	}

	//TODO
	//	private void testFetchPromotionGroupDetails()
	//	{
	//		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
	//		junit.framework.Assert.assertNotNull(modelService);
	//		final String promoGroup = "mplPromoGrp";
	//		final PromotionGroupModel flag = mplPromotionHelper.fetchPromotionGroupDetails(promoGroup);
	//		Assert.assertNotNull(flag);
	//	}

	private void testGetTotalPrice()
	{
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final CartModel cart = (CartModel) flexibleSearchService.search("SELECT {PK} FROM {Cart} WHERE {pk}='8796093055019'")
				.getResult().get(0);
		junit.framework.Assert.assertNotNull(cart);
		final AbstractOrder order = modelService.getSource(cart);
		final double flag = mplPromotionHelper.getTotalPrice(order);
		Assert.assertNotNull(new Double(flag));
	}

	private void testCheckIfBOGOProduct()
	{
		final boolean checkflag = false;
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final boolean flag = mplPromotionHelper.checkIfBOGOProduct(abstractOrderEntry);
		Assert.assertEquals(checkflag, flag);
	}

	private void testGetvalidProdQCForOrderShippingPromotion()
	{
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		final DeliveryModeModel deliveryMode1 = (DeliveryModeModel) flexibleSearchService
				.search("SELECT {PK} FROM {DeliveryMode} WHERE {code}='home-delivery'").getResult().get(0);
		junit.framework.Assert.assertNotNull(deliveryMode1);
		final DeliveryMode deliveryModeList1 = modelService.getSource(deliveryMode1);
		final List<DeliveryMode> deliveryModeList = new ArrayList<DeliveryMode>();
		deliveryModeList.add(deliveryModeList1);
		final AbstractOrderEntryModel ordermodel = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(ordermodel);
		final AbstractOrderEntry abstractOrderEntry = modelService.getSource(ordermodel);
		final String selectedUSSID = "123654098765485130011712";
		final Map<String, AbstractOrderEntry> validUssidMap = new HashMap<String, AbstractOrderEntry>();
		validUssidMap.put(selectedUSSID, abstractOrderEntry);
		final Map<String, Integer> flag = mplPromotionHelper.getvalidProdQCForOrderShippingPromotion(deliveryModeList,
				validUssidMap);
		Assert.assertNotNull(flag);
	}

	private void testGetDeliveryEntryCharge()
	{
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		junit.framework.Assert.assertNotNull(flexibleSearchService);
		final double deliveryVal = 100.00;
		final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractOrderEntry} WHERE {PK}='8796519039020'").getResult().get(0);
		junit.framework.Assert.assertNotNull(entry);
		final double flag = mplPromotionHelper.getDeliveryEntryCharge(deliveryVal, entry);
		Assert.assertNotNull(new Double(flag));

	}

	@Test
	public void test() throws Exception
	{
		LOG.info("Running Test Data ");
		//TODO
		//testCheckMultipleSeller();
		testGetAssociatedData();
		testValidateEntryForFreebie();
		//TODO
		//testFetchPromotionGroupDetails();
		testGetTotalPrice();
		testCheckIfBOGOProduct();
		//TODO
		//testCheckCartPromoPriority();
		testGetvalidProdQCForOrderShippingPromotion();
		testGetDeliveryEntryCharge();
	}

	@After
	public void tearDown()
	{
		LOG.info("Tear Down Method");
	}



}
