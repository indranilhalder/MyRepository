/**
 *
 */
package com.tisl.mpl.jalo;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.model.BuyAAboveXGetPercentageOrAmountOffModel;
import com.tisl.mpl.servicelayer.MplServicelayerTest;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
public class BuyAAboveXGetPercentageOrAmountOffTest extends MplServicelayerTest
{


	private static final Logger LOG = Logger.getLogger(BuyAAboveXGetPercentageOrAmountOffTest.class);

	private ProductModel product1;
	private UserModel user;
	private CurrencyModel currency;
	private CartModel cart;
	private PromotionGroupModel promotionGroup;
	private CatalogVersionModel version;

	@Autowired
	private CommerceCartService commerceCartService;

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
		product1 = getProductData("987654341");
		setUser("2000000018");
		setCurrency("INR");

		promotionSetup(product1);
	}

	/**
	 * @param currencyData
	 */


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
	private void setUser(final String userUID)
	{
		final UserService usersvc = (UserService) Registry.getApplicationContext().getBean("userService");
		junit.framework.Assert.assertNotNull(usersvc);
		user = usersvc.getUserForUID(userUID);
		junit.framework.Assert.assertNotNull(user);
		usersvc.setCurrentUser(user);
	}

	/**
	 * @param producCode
	 * @return ProductModel
	 */
	private ProductModel getProductData(final String producCode)
	{
		final ProductService product = (ProductService) Registry.getApplicationContext().getBean("productService");
		junit.framework.Assert.assertNotNull(product);

		return product.getProductForCode(producCode);
	}


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
	 * @param product
	 * @Description : Setting up the Promotion
	 *
	 */
	private void promotionSetup(final ProductModel product)
	{
		final List<ProductModel> productList = new ArrayList<ProductModel>();
		final List<SalesApplication> channelList = new ArrayList<SalesApplication>();
		final BuyAAboveXGetPercentageOrAmountOffModel dicountModel = new BuyAAboveXGetPercentageOrAmountOffModel();
		dicountModel.setCode("ProductThreshPromotion");
		dicountModel.setTitle("Buy above 500  10 Percent off");
		dicountModel.setChannel(channelList);
		dicountModel.setPriority(Integer.valueOf(999));
		dicountModel.setDescription("Buy above 500  10 Percent off");
		dicountModel.setPromotionGroup(getPromoGroup("mplPromoGrp"));
		dicountModel.setThresholdTotals(createPriceRow(currency, 500));
		if (null != product)
		{
			productList.add(product);
			if (CollectionUtils.isNotEmpty(productList))
			{
				dicountModel.setProducts(productList);
			}
		}
		dicountModel.setPercentageDiscount(Double.valueOf(10));
		dicountModel.setPercentageOrAmount(Boolean.TRUE);

		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		modelService.save(dicountModel);
	}

	@Test
	public void test() throws Exception
	{
		LOG.info("Running Test Data ");
		cart = getCart();
		calculateCart(cart);
	}

	/**
	 * @param promoGrp
	 */
	private PromotionGroupModel getPromoGroup(final String promoGrp)
	{
		final PromotionsService promoSvc = (PromotionsService) Registry.getApplicationContext().getBean("promotionsService");
		junit.framework.Assert.assertNotNull(promoSvc.getPromotionGroup(promoGrp));
		promotionGroup = promoSvc.getPromotionGroup(promoGrp);
		junit.framework.Assert.assertNotNull(promotionGroup);

		return promotionGroup;

	}

	/**
	 * @return CartModel
	 */
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

	private void calculateCart(final CartModel cartModel)
	{
		//TODO : Presently Block as Price Factory needs to be overwritten by Cart Team
		//		final CommerceCartService comCartSvc = (CommerceCartService) Registry.getApplicationContext()
		//				.getBean("commerceCartService");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		//		junit.framework.Assert.assertNotNull(comCartSvc);
		//		comCartSvc.recalculateCart(cartModel);
		modelService.refresh(cartModel);
		modelService.save(cartModel);
		//System.out.println("Cart Total Price:" + cartModel.getTotalPrice());
	}

	private List<PromotionPriceRowModel> createPriceRow(final CurrencyModel currency, final double discount)
	{
		final PromotionPriceRowModel priceRow = new PromotionPriceRowModel();
		priceRow.setCurrency(currency);
		priceRow.setPrice(Double.valueOf(discount));
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		modelService.save(priceRow);
		final List<PromotionPriceRowModel> rows = new ArrayList<PromotionPriceRowModel>();
		rows.add(priceRow);
		return rows;
	}

	@After
	public void tearDown()
	{
		System.out.println("Tear Down Method");
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		final AbstractPromotionModel promotion = (AbstractPromotionModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractPromotion} WHERE {code}='ProductThreshPromotion'").getResult().get(0);
		junit.framework.Assert.assertNotNull(promotion);

		//System.out.println("Promotion Code" + promotion.getCode());
		promotion.setEnabled(Boolean.FALSE);
		modelService.save(promotion);
	}



}
