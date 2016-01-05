/**
 *
 */
package com.tisl.mpl.jalo;

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
import de.hybris.platform.promotions.AbstractPromotionTest;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


public class CustomProductBOGOFPromotionTest extends AbstractPromotionTest
{
	private ProductModel product1, product2;

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


	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		MockitoAnnotations.initMocks(this);
		final CustomProductBOGOFPromotion customProductBOGOFPromotion = new CustomProductBOGOFPromotion();

		final CatalogVersionModel version = catalogVersionService.getCatalogVersion("mplProductCatalog", "Online");
		catalogVersionService.addSessionCatalogVersion(version);
		product1 = this.productService.getProductForCode(version, "mplProductCatalog-23191");
		product2 = this.productService.getProductForCode(version, "mplProductCatalog-149243");


		final Product p1 = new Product();
		final Product p2 = new Product();
		p1.setCode(product1.getCode());
		p1.setName(product1.getName());
		p2.setCode(product2.getCode());
		p2.setName(product2.getName());

		//Newly Added : Need to be verified
		final List<Product> productListData = new ArrayList<>();
		productListData.add(p1);
		productListData.add(p2);
		customProductBOGOFPromotion.setProducts(productListData);
		customProductBOGOFPromotion.setFreeCount(2);


		final UserModel user = userService.getUserForUID("demo");
		userService.setCurrentUser(user);
		final CurrencyModel currency = commonI18NService.getCurrency("EUR");
		commonI18NService.setCurrentCurrency(currency);
	}

	@Test
	public void testProductBOGOFPromotion() throws CalculationException
	{
		final CartModel cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product1, 1L, product1.getUnit());
		cartService.addNewEntry(cart, product2, 1L, product2.getUnit());

		modelService.save(cart);
		calculationService.calculate(cart);
		Assert.assertEquals("cart total before updatePromotions", 906.69000000000005D, cart.getTotalPrice().doubleValue(), 0.01D);

		final PromotionGroupModel promotionGroup = promotionsService.getPromotionGroup("mplPromoGrp");
		final Collection promotionGroups = new ArrayList();
		promotionGroups.add(promotionGroup);
		promotionsService.updatePromotions(promotionGroups, cart, false, PromotionsManager.AutoApplyMode.APPLY_ALL,
				PromotionsManager.AutoApplyMode.APPLY_ALL, new Date());
		modelService.refresh(cart);
		Assert.assertEquals("cart total after updatePromotions", 876.78999999999996D, cart.getTotalPrice().doubleValue(), 0.01D);
	}
}
