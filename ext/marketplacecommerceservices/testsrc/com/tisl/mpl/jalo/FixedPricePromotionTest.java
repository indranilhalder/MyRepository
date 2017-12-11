package com.tisl.mpl.jalo;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tisl.mpl.model.FixedPricePromotionModel;
import com.tisl.mpl.servicelayer.MplServicelayerTest;


/**
 * Test for ProductFixedPricePromotion.
 */
public class FixedPricePromotionTest extends MplServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(MplProductSteppedMultiBuyPromotionTest.class);

	private ProductModel product1;
	private UserModel user;
	private CurrencyModel currency;
	private CartModel cart;
	private PromotionGroupModel promotionGroup;
	private CatalogVersionModel version;

	/*
	 * @Resource private CatalogVersionService catalogVersionService;
	 *
	 * @Resource private ProductService productService;
	 *
	 * @Resource private UserService userService;
	 *
	 * @Resource private CartService cartService;
	 *
	 * @Resource private CalculationService calculationService;
	 *
	 * @Resource private CommonI18NService commonI18NService;
	 *
	 * @Resource private ModelService modelService;
	 *
	 * @Resource private PromotionsService promotionsService;
	 */

	@Before
	public void setUp() throws Exception
	{
		createDefaultUsers();
		LOG.info("Setting Up Data");
		MockitoAnnotations.initMocks(this);
		this.user = Mockito.mock(UserModel.class);
		this.currency = Mockito.mock(CurrencyModel.class);
		this.version = Mockito.mock(CatalogVersionModel.class);
		this.product1 = Mockito.mock(ProductModel.class);
		this.cart = Mockito.mock(CartModel.class);
		this.promotionGroup = Mockito.mock(PromotionGroupModel.class);

		getCatalogData();
		product1 = getProductData("MP000000000595772");
		setUser("1000000000");
		setCurrency("INR");

		promotionSetup(product1);
	}


	/**
	 * @param product
	 * @Description : Setting up the Promotion
	 *
	 */
	@SuppressWarnings("deprecation")
	private void promotionSetup(final ProductModel product)
	{
		final List<ProductModel> productList = new ArrayList<ProductModel>();
		final List<SalesApplication> channelList = new ArrayList<SalesApplication>();
		final FixedPricePromotionModel dicountModel = new FixedPricePromotionModel();
		final Map<CurrencyModel, Double> countListData = new HashMap<CurrencyModel, Double>();
		countListData.put(currency, Double.valueOf(400));
		dicountModel.setCode("FixedPricePromotion");
		dicountModel.setTitle("Get Fixed Discounts");
		dicountModel.setChannel(channelList);
		dicountModel.setQuantity(Long.valueOf(5));
		dicountModel.setPriority(Integer.valueOf(1000));
		dicountModel.setDescription("Get Fixed Discounts");
		dicountModel.setPromotionGroup(getPromoGroup("mplPromoGrp"));
		if (null != product)
		{
			productList.add(product);
			if (CollectionUtils.isNotEmpty(productList))
			{
				dicountModel.setProducts(productList);
			}
		}

		dicountModel.setProductFixedUnitPrice(getPriceRowData(countListData));

		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		modelService.save(dicountModel);
	}



	/**
	 * @param countListData
	 * @return dataList
	 */
	private Collection<PromotionPriceRowModel> getPriceRowData(final Map<CurrencyModel, Double> countListData)
	{
		final List<PromotionPriceRowModel> dataList = new ArrayList<PromotionPriceRowModel>();

		if (MapUtils.isNotEmpty(countListData))
		{
			for (final Map.Entry<CurrencyModel, Double> entry : countListData.entrySet())
			{
				final PromotionPriceRowModel oModel = new PromotionPriceRowModel();
				oModel.setCurrency(entry.getKey());
				oModel.setPrice(entry.getValue());
			}
		}
		return dataList;
	}



	@Test
	public void test() throws Exception
	{
		LOG.info("Running Test Data ");
		cart = getCart();
		calculateCart();

	}


	/**
	 * @param promoGrp
	 */
	@SuppressWarnings("deprecation")
	private PromotionGroupModel getPromoGroup(final String promoGrp)
	{
		final PromotionsService promoSvc = (PromotionsService) Registry.getApplicationContext().getBean("promotionsService");
		junit.framework.Assert.assertNotNull(promoSvc.getPromotionGroup(promoGrp));
		promotionGroup = promoSvc.getPromotionGroup(promoGrp);
		junit.framework.Assert.assertNotNull(promotionGroup);

		return promotionGroup;

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


	/**
	 * @param currencyData
	 */

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
	 * @return CartModel
	 */
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


	/**
	 * @return CartModel
	 */
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


	private void calculateCart()
	{
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		modelService.refresh(cart);
		modelService.save(cart);
	}


	@SuppressWarnings("deprecation")
	@After
	public void tearDown()
	{
		LOG.info("Tear Down Method");
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		final AbstractPromotionModel promotion = (AbstractPromotionModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractPromotion} WHERE {code}='FixedPricePromotion'").getResult().get(0);
		junit.framework.Assert.assertNotNull(promotion);

		System.out.println("Promotion Code" + promotion.getCode());
		promotion.setEnabled(Boolean.FALSE);
		modelService.save(promotion);
	}



}
