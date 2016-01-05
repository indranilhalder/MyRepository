/**
 *
 */
package com.tisl.mpl.jalo;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionPriceRowModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.tisl.mpl.model.BuyAAboveXGetPercentageOrAmountOffModel;


/**
 * @author TCS
 *
 */
public class BuyAAboveXGetPercentageOrAmountOffTest extends ServicelayerTransactionalTest
{

	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private ProductService productService;
	@Resource
	private UserService userService;
	@Resource
	private CartService cartService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private ModelService modelService;
	@Resource
	private PromotionsService promotionsService;

	private ProductModel product;
	private PromotionGroupModel promotionGroup;
	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		final CatalogVersionModel version = catalogVersionService.getCatalogVersion("mplProductCatalog", "Online");
		catalogVersionService.addSessionCatalogVersion(version);
		userService.setCurrentUser(userService.getUserForUID("demo"));//Add User ID
		currency = commonI18NService.getCurrency("EUR");
		promotionGroup = createPromotionGroup("mplPromoGrp");
		product = productService.getProductForCode(version, "mplProductCatalog-987654341");
	}

	private PromotionGroupModel createPromotionGroup(final String name)
	{
		final PromotionGroupModel promotionGroup = new PromotionGroupModel();
		promotionGroup.setIdentifier(name);
		modelService.save(promotionGroup);
		return promotionGroup;
	}

	private BuyAAboveXGetPercentageOrAmountOffModel preparePromotion(final String code, final double thresholdTotals,
			final Double percentageDiscount, PromotionGroupModel promotionGroup, final double discountPrices)
	{
		final BuyAAboveXGetPercentageOrAmountOffModel promo = modelService.create(BuyAAboveXGetPercentageOrAmountOffModel.class);
		promo.setCode(code);
		promo.setEnabled(Boolean.TRUE);
		promo.setPriority(Integer.valueOf(1000));
		promotionGroup = promotionsService.getPromotionGroup("mplPromoGrp");
		final Collection<PromotionGroupModel> promotionGroups = new ArrayList<PromotionGroupModel>();
		promotionGroups.add(promotionGroup);
		promo.setPercentageOrAmount(Boolean.FALSE);
		promo.setPercentageDiscount(percentageDiscount);
		promo.setDiscountPrices(createPriceRow(currency, discountPrices));
		promo.setThresholdTotals(createPriceRow(currency, thresholdTotals));

		modelService.save(promo);
		return promo;
	}

	private Collection<PromotionPriceRowModel> createPriceRow(final CurrencyModel currency, final double discount)
	{
		final PromotionPriceRowModel priceRow = new PromotionPriceRowModel();
		priceRow.setCurrency(currency);
		priceRow.setPrice(Double.valueOf(discount));
		modelService.save(priceRow);
		final List<PromotionPriceRowModel> rows = new ArrayList<PromotionPriceRowModel>();
		rows.add(priceRow);
		return rows;
	}

	@Test
	public void testHighDiscountPromotion()
	{
		final CartModel cart = cartService.getSessionCart();
		final List<AbstractPromotionModel> promotions = new ArrayList<AbstractPromotionModel>();
		promotions.add(preparePromotion("ProductThreshPromo", 100.00, Double.valueOf(10.00), promotionGroup, 50));
		promotionGroup.setPromotions(promotions);

		//Adding a new Entry
		cartService.addNewEntry(cart, product, 1, product.getUnit());
		modelService.save(cart); //Saving the Cart
		promotionsService.updatePromotions(Collections.singletonList(promotionGroup), cart, false, AutoApplyMode.APPLY_ALL,
				AutoApplyMode.APPLY_ALL, new java.util.Date());
		modelService.refresh(cart);
		assertTrue("Price must be positive: " + cart.getTotalPrice(), cart.getTotalPrice().doubleValue() >= 0);
	}

	@Test
	public void testLowDiscountPromotion()
	{
		final CartModel cart = cartService.getSessionCart();
		final List<AbstractPromotionModel> promotions = new ArrayList<AbstractPromotionModel>();
		promotions.add(preparePromotion("ProductThreshPromo", 100.00, Double.valueOf(10.00), promotionGroup, 50));
		promotionGroup.setPromotions(promotions);
		//Adding a new Entry
		cartService.addNewEntry(cart, product, 1, product.getUnit());
		modelService.save(cart); //Saving the Cart
		promotionsService.updatePromotions(Collections.singletonList(promotionGroup), cart, false, AutoApplyMode.APPLY_ALL,
				AutoApplyMode.APPLY_ALL, new java.util.Date());
		modelService.refresh(cart);
	}
}
