/**
 *
 */
package com.tisl.mpl.jalo;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.AbstractPromotionTest;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


public class BuyAgetBFreePercentageDiscountTest extends AbstractPromotionTest
{
	private ProductModel product1;

	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ProductService productService;
	@Resource
	private UserService userService;
	@Resource
	private CartService cartService;
	@Resource
	private CalculationService calculationService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private ModelService modelService;
	@Resource
	private PromotionsService promotionsService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final BuyABFreePrecentageDiscount buyABFreePercentageDiscount = new BuyABFreePrecentageDiscount();

		ProductModel product2;
		final CatalogVersionModel version = catalogVersionService.getCatalogVersion("mplProductCatalog", "Online");
		catalogVersionService.addSessionCatalogVersion(version);

		product1 = productService.getProductForCode(version, "mplProductCatalog-23191");
		product2 = productService.getProductForCode(version, "mplProductCatalog-149243");

		final Product p1 = new Product();
		p1.setCode(product1.getCode());
		p1.setName(product1.getName());

		final List<Product> product = new ArrayList<Product>();
		product.add(p1);
		buyABFreePercentageDiscount.setProducts(product);

		final Product p = new Product();
		p.setCode(product2.getCode());
		p.setName(product2.getName());

		final List<Product> giftProduct = new ArrayList<Product>();
		giftProduct.add(p);
		buyABFreePercentageDiscount.setGiftProducts(giftProduct);
		buyABFreePercentageDiscount.setPercentageDiscount(10.0);
		buyABFreePercentageDiscount.setPercentageOrAmount(true);
		buyABFreePercentageDiscount.setMaxDiscountVal(1000);
		//buyABFreePercentageDiscount.setDiscountPrices(createPriceRow(currency,20));


		final UserModel user = userService.getUserForUID("demo");
		userService.setCurrentUser(user);
		final CurrencyModel currency = commonI18NService.getCurrency("EUR");
		commonI18NService.setCurrentCurrency(currency);
	}

	/**
	 * HW1210-3411: 253 Euro, HW1100-0023: 523.99 Euro, HW2310-1001: 29.90 Euro, and HW2320-1009: 99.80 Euro. Adds all
	 * these products to cart, and check the price of the cart before and after the updatePromoitions(). Since the
	 * ProductBOGOFPromotion is enabled, the HW2310-1001(29.90 Euro) with the lowest price is sold for free.
	 */
	@Test
	public void testProductBOGOFPromotion() throws CalculationException
	{
		final CartModel cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product1, 1, product1.getUnit());

		modelService.save(cart);
		calculationService.calculate(cart);
		assertEquals("cart total before updatePromotions", 906.69, cart.getTotalPrice().doubleValue(), 0.01);

		final PromotionGroupModel promotionGroup = promotionsService.getPromotionGroup("mplPromoGrp");
		final Collection<PromotionGroupModel> promotionGroups = new ArrayList<PromotionGroupModel>();
		promotionGroups.add(promotionGroup);
		promotionsService.updatePromotions(promotionGroups, cart, false, AutoApplyMode.APPLY_ALL, AutoApplyMode.APPLY_ALL,
				new Date());
		modelService.refresh(cart);
		assertEquals("cart total after updatePromotions", 876.79, cart.getTotalPrice().doubleValue(), 0.01);
	}

}
