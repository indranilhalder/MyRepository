///**
// *
// */
//package com.tisl.mpl.jalo;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import de.hybris.platform.catalog.CatalogVersionService;
//import de.hybris.platform.catalog.model.CatalogVersionModel;
//import de.hybris.platform.core.model.c2l.CurrencyModel;
//import de.hybris.platform.core.model.order.CartModel;
//import de.hybris.platform.core.model.product.ProductModel;
//import de.hybris.platform.order.CartService;
//import de.hybris.platform.product.ProductService;
//import de.hybris.platform.promotions.PromotionsService;
//import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
//import de.hybris.platform.promotions.model.AbstractPromotionModel;
//import de.hybris.platform.promotions.model.PromotionGroupModel;
//import de.hybris.platform.promotions.model.PromotionPriceRowModel;
//import de.hybris.platform.servicelayer.i18n.CommonI18NService;
//import de.hybris.platform.servicelayer.model.ModelService;
//import de.hybris.platform.servicelayer.user.UserService;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.tisl.mpl.model.CustomOrderThresholdFreeGiftPromotionModel;
//
//
///**
// * @author TCS
// *
// */
//public class CustomOrderThresholdFreeGiftPromotionTest
//{
//	@Resource
//	private CatalogVersionService catalogVersionService;
//	@Resource
//	private ProductService productService;
//	@Resource
//	private UserService userService;
//	@Resource
//	private CartService cartService;
//	@Resource
//	private CommonI18NService commonI18NService;
//	@Resource
//	private ModelService modelService;
//	@Resource
//	private PromotionsService promotionsService;
//
//	private ProductModel product;
//	private PromotionGroupModel promotionGroup;
//	private CurrencyModel currency;
//
//	@Before
//	public void setUp() throws Exception
//	{
//		//TISSEC-50
//		final CatalogVersionModel version = catalogVersionService.getCatalogVersion("", "");//TODO : Please enter catalogue name,Please enter version
//		catalogVersionService.addSessionCatalogVersion(version);
//		userService.setCurrentUser(userService.getUserForUID("demo"));
//		currency = commonI18NService.getCurrency("");//TODO : Please enter currency
//		promotionGroup = createPromotionGroup("");//TODO : Please enter promotion grp
//		product = productService.getProductForCode(version, "");//TODO : Please enter product code
//	}
//
//	private PromotionGroupModel createPromotionGroup(final String name)
//	{
//		final PromotionGroupModel promotionGroup = new PromotionGroupModel();
//		promotionGroup.setIdentifier(name);
//		modelService.save(promotionGroup);
//		return promotionGroup;
//	}
//
//	private CustomOrderThresholdFreeGiftPromotionModel preparePromotion(final String code, final double thresholdTotals,
//			PromotionGroupModel promotionGroup)
//	{
//		final CustomOrderThresholdFreeGiftPromotionModel promo = new CustomOrderThresholdFreeGiftPromotionModel();
//		promo.setCode(code);
//		promo.setEnabled(Boolean.TRUE);
//		promo.setPriority(Integer.valueOf(1000));
//		promotionGroup = promotionsService.getPromotionGroup("");//TODO : Please enter promotion grp
//		final Collection<PromotionGroupModel> promotionGroups = new ArrayList<PromotionGroupModel>();
//		promotionGroups.add(promotionGroup);
//		promo.setThresholdTotals(createPriceRow(currency, thresholdTotals));
//		modelService.save(promo);
//		return promo;
//	}
//
//	private Collection<PromotionPriceRowModel> createPriceRow(final CurrencyModel currency, final double discount)
//	{
//		final PromotionPriceRowModel priceRow = new PromotionPriceRowModel();
//		priceRow.setCurrency(currency);
//		priceRow.setPrice(Double.valueOf(discount));
//		modelService.save(priceRow);
//		final List<PromotionPriceRowModel> rows = new ArrayList<PromotionPriceRowModel>();
//		rows.add(priceRow);
//		return rows;
//	}
//
//	@Test
//	public void testHighDiscountPromotion()
//	{
//		final CartModel cart = cartService.getSessionCart();
//		final List<AbstractPromotionModel> promotions = new ArrayList<AbstractPromotionModel>();
//		promotions.add(preparePromotion("CartPromo", 100.00, promotionGroup));
//		promotionGroup.setPromotions(promotions);
//		cartService.addNewEntry(cart, product, 1, product.getUnit());
//		modelService.save(cart);
//		promotionsService.updatePromotions(Collections.singletonList(promotionGroup), cart, false, AutoApplyMode.APPLY_ALL,
//				AutoApplyMode.APPLY_ALL, new java.util.Date());
//		modelService.refresh(cart);
//		assertTrue("Price must be positive: " + cart.getTotalPrice(), cart.getTotalPrice().doubleValue() >= 0);
//	}
//
//	@Test
//	public void testLowDiscountPromotion()
//	{
//		cartService.removeSessionCart();
//		final CartModel cart = cartService.getSessionCart();
//		final List<AbstractPromotionModel> promotions = new ArrayList<AbstractPromotionModel>();
//		promotions.add(preparePromotion("CartPromo", 100.00, promotionGroup));
//		promotionGroup.setPromotions(promotions);
//		cartService.addNewEntry(cart, product, 1, product.getUnit());
//		modelService.save(cart);
//		promotionsService.updatePromotions(Collections.singletonList(promotionGroup), cart, false, AutoApplyMode.APPLY_ALL,
//				AutoApplyMode.APPLY_ALL, new java.util.Date());
//		modelService.refresh(cart);
//
//		assertEquals("Applied discout promotion: ", 19.90d, cart.getTotalPrice().doubleValue(), 0.001d);
//	}
//}
