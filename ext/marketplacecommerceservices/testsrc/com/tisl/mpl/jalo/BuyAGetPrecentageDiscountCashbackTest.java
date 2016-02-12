/**
 *
 */
package com.tisl.mpl.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.constants.ServicelayerConstants;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.DefaultModelConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * @author TCS
 *
 */
public class BuyAGetPrecentageDiscountCashbackTest
{
	private static final Logger LOG = Logger.getLogger(BuyAGetPrecentageDiscountCashbackTest.class);
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
	@Resource
	private ConverterRegistry converterRegistry;
	@Resource
	private I18NService i18nService;

	private String beforeCfg;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final BuyAGetPrecentageDiscountCashback cashbackPromo = new BuyAGetPrecentageDiscountCashback();
		final CatalogVersionModel catVersion = catalogVersionService.getCatalogVersion("mplProductCatalog", "Online");
		catalogVersionService.addSessionCatalogVersion(catVersion);
		product1 = productService.getProductForCode(catVersion, "mplProductCatalog-987654341");

		final Product p = new Product();
		p.setCode(product1.getCode());
		p.setName(product1.getName());
		final List<Product> product = new ArrayList<Product>();
		product.add(p);

		cashbackPromo.setProducts(product);
		cashbackPromo.setQuantity(2);
		cashbackPromo.setPercentageDiscount(10);
		cashbackPromo.setPercentageOrAmount(true);

		final UserModel user = userService.getUserForUID("demo");//Add User ID
		userService.setCurrentUser(user);
		commonI18NService.setCurrentCurrency(commonI18NService.getCurrency("INR"));
	}


	private void reloadConvertersBefore(final String prfetechMode, final Map<String, Class<? extends ItemModel>> clazzez)
	{
		beforeCfg = Config.getParameter(ServicelayerConstants.PARAM_PREFETCH);
		Config.setParameter(ServicelayerConstants.PARAM_PREFETCH, prfetechMode);
		for (final Map.Entry<String, Class<? extends ItemModel>> entry : clazzez.entrySet())
		{
			reloadConverter(entry.getKey(), entry.getValue());
		}
	}

	private void revertConvertersAfter(final Map<String, Class<? extends ItemModel>> clazzez)
	{
		Config.setParameter(ServicelayerConstants.PARAM_PREFETCH, beforeCfg);
		for (final Map.Entry<String, Class<? extends ItemModel>> entry : clazzez.entrySet())
		{
			reloadConverter(entry.getKey(), entry.getValue());
		}
	}


	@Test
	public void testProductPercentageDiscountPromotionForDefaultPrefetch() throws CalculationException
	{
		testPromotionBody();
	}


	/**
	 * HW2110-0019: 381.64 Euro, with 19% discount 309.13 Euro, and HW2200-0561: 86.80 Euro.
	 * <ul>
	 * <li>adds HW2110-0019 in cart, and tests the total price,</li>
	 * <li>updates with 19% ProductPercentageDiscountPromotion, and test the total price,</li>
	 * <li>adds HW2200-0561 in cart, and tests the total price again</li>
	 * <li>then recalculate the cart and tests the total price again</li>
	 * <li>then update promotions for the cart and tests the total price again</li>
	 * </ul>
	 */
	private void testPromotionBody() throws CalculationException
	{
		final CartModel cart = cartService.getSessionCart();
		cartService.addNewEntry(cart, product1, 1, product1.getUnit());
		modelService.save(cart);
		calculationService.calculate(cart);
		assertEquals("before updatePromotions(BuyAPrecentageDiscount)", 1000, cart.getTotalPrice().doubleValue(), 0.01);

		final PromotionGroupModel promotionGroup = promotionsService.getPromotionGroup("mplPromoGrp");
		final Collection<PromotionGroupModel> promotionGroups = new ArrayList<PromotionGroupModel>();
		promotionGroups.add(promotionGroup);
		promotionsService.updatePromotions(promotionGroups, cart, false, AutoApplyMode.APPLY_ALL, AutoApplyMode.APPLY_ALL,
				new Date());
		modelService.refresh(cart);
		LOG.info("toal with promotion: " + cart.getTotalPrice().doubleValue());
		assertEquals("one product with promotion", 2000, cart.getTotalPrice().doubleValue(), 0.01);


		final AbstractOrderEntryModel entry1 = cart.getEntries().get(0);
		assertEquals(Boolean.TRUE, entry1.getCalculated());

		final AbstractOrderEntry firstEntry = modelService.getSource(entry1);
		assertTrue(firstEntry.isCalculatedAsPrimitive());

		calculationService.calculate(cart);
		assertEquals("two products with promotion", 1800, cart.getTotalPrice().doubleValue(), 0.01);

		calculationService.recalculate(cart);

		promotionsService.updatePromotions(promotionGroups, cart, false, AutoApplyMode.APPLY_ALL, AutoApplyMode.APPLY_ALL,
				new Date());
	}


	@Test
	public void testProductPercentageDiscountPromotionForLiteral() throws CalculationException
	{


		final Map<String, Class<? extends ItemModel>> map = new HashMap<String, Class<? extends ItemModel>>();
		map.put(CartModel._TYPECODE, CartModel.class);
		map.put(OrderModel._TYPECODE, OrderModel.class);
		map.put(AbstractOrderModel._TYPECODE, AbstractOrderModel.class);
		map.put(OrderEntryModel._TYPECODE, OrderEntryModel.class);
		map.put(AbstractOrderEntryModel._TYPECODE, AbstractOrderEntryModel.class);

		reloadConvertersBefore(ServicelayerConstants.VALUE_PREFETCH_LITERAL, map);

		try
		{
			testPromotionBody();
		}
		finally
		{
			revertConvertersAfter(map);
		}
	}



	//technical internals how to reload ItemModelConverter
	private ModelConverter adjustConverter(final String type, final Class modelClass, final ModelConverter newConverter,
			final ModelConverter oldConverter)
	{
		((DefaultModelConverterRegistry) converterRegistry).removeModelConverterBySourceType(type);
		((DefaultModelConverterRegistry) converterRegistry).registerModelConverter(type, modelClass, newConverter);
		LOG.info("reloading converter for " + modelClass + " type " + type);
		return oldConverter;
	}

	private ItemModelConverter deepCloneConverter(final ItemModelConverter given)
	{
		return new ItemModelConverter(modelService, i18nService, commonI18NService, given.getDefaultType(), given.getModelClass(),
				given.getSerializationStrategy());
	}

	private ItemModelConverter getCurrent(final Class modelClass)
	{
		return (ItemModelConverter) ((DefaultModelConverterRegistry) converterRegistry).getModelConverterByModelType(modelClass);
	}

	private void reloadConverter(final String type, final Class modelClass)
	{
		final ItemModelConverter existing = getCurrent(modelClass);
		final ItemModelConverter fresh = deepCloneConverter(existing);
		adjustConverter(type, modelClass, fresh, existing);
	}

}
