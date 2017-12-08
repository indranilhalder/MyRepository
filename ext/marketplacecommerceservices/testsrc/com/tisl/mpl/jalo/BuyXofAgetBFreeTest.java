/**
 *
 */
package com.tisl.mpl.jalo;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.enums.DiscTypesEnum;
import com.tisl.mpl.servicelayer.MplServicelayerTest;


@SuppressWarnings("deprecation")
public class BuyXofAgetBFreeTest extends MplServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(BuyXofAgetBFreeTest.class);

	private ProductModel product1;
	private UserModel user;
	private CurrencyModel currency;
	private CartModel cart;
	private CatalogVersionModel version;
	DiscTypesEnum enumvalue;

	@Autowired
	private CommerceCartService commerceCartService;


	/**
	 * Add dependency in class path for following platform/bootstrap
	 * platform/ext/platformservices/classes/de/hybris/platform/order/exceptions
	 * commercefacades/classes/de/hybris/platform/commercefacades/product/converters/populator
	 * platform/ext/platformservices/classes/de/hybris/platform/catalog/synchronization
	 * platform/bootstrap/modelclasses/de/hybris/platform/core/model/order
	 *
	 *
	 * Run bogoFreebiePromotions.csv post required changes
	 *
	 * @Description : Executes before Test
	 * @throws Exception
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


		getCatalogData();
		product1 = getProductData("987654341");
		setUser("2000000011");
		setCurrency("INR");

		//Run bogoFreebiePromotions.csv post required changes
		enablePromotion();
	}

	/**
	 * Method to enable the Promotion
	 *
	 * Note: Replace The Promotion Code with the one in Impex
	 */
	private void enablePromotion()
	{
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		final AbstractPromotionModel promotion = (AbstractPromotionModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractPromotion} WHERE {code}='BuyAGetBFree'").getResult().get(0);
		junit.framework.Assert.assertNotNull(promotion);

		//	System.out.println("Promotion Code" + promotion.getCode());
		promotion.setEnabled(Boolean.TRUE);
		modelService.save(promotion);
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


	@Test
	public void test() throws Exception
	{
		LOG.info("Running Test Data ");
		cart = getCart();
		calculateCart();
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

	private void calculateCart()
	{
		//TODO : Presently Block as Price Factory needs to be overwritten by Cart Team
		//		final CommerceCartService comCartSvc = (CommerceCartService) Registry.getApplicationContext()
		//				.getBean("commerceCartService");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		//		junit.framework.Assert.assertNotNull(comCartSvc);
		//		comCartSvc.recalculateCart(cart);
		modelService.refresh(cart);
		modelService.save(cart);
		//System.out.println("Cart Total Price:" + cart.getTotalPrice());
	}

	/**
	 * Method executed after Test
	 *
	 * Note: Replace The Promotion Code with the one in Impex
	 */
	@After
	public void tearDown()
	{
		LOG.info("Tear Down Method");
		final FlexibleSearchService flexibleSearchService = (FlexibleSearchService) Registry.getApplicationContext().getBean(
				"flexibleSearchService");
		final ModelService modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
		junit.framework.Assert.assertNotNull(modelService);

		final AbstractPromotionModel promotion = (AbstractPromotionModel) flexibleSearchService
				.search("SELECT {PK} FROM {AbstractPromotion} WHERE {code}='BuyAGetBFree'").getResult().get(0);
		junit.framework.Assert.assertNotNull(promotion);

		//	System.out.println("Promotion Code" + promotion.getCode());
		promotion.setEnabled(Boolean.FALSE);
		modelService.save(promotion);
	}


}
