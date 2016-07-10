/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartRestorationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.order.CartService;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.strategy.MplCommerceAddToCartStrategy;


/**
 * @author TCS
 *
 */
public class ExtDefaultCommerceCartRestorationStrategyTest
{
	private static final Logger LOG = Logger.getLogger(DefaultCommerceCartRestorationStrategy.class);
	@Mock
	private ExtDefaultCommerceCartRestorationStrategy extDefaultCommerceCartRestorationStrategy;
	@Mock
	private final CommerceCartParameter parameter = Mockito.mock(CommerceCartParameter.class);
	@Mock
	private final CartModel cartModel = Mockito.mock(CartModel.class);
	@Mock
	private final CommerceCartRestoration restoration = Mockito.mock(CommerceCartRestoration.class);

	@Autowired
	private ModelService modelService;
	@Autowired
	private CartService cartService;
	@Autowired
	private DefaultCommerceCartCalculationStrategy commerceCartCalculationStrategy;
	@Autowired
	private CartFactory cartFactory;
	@Autowired
	private MplCommerceAddToCartStrategy mplCommerceAddToCartStrategy;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.extDefaultCommerceCartRestorationStrategy = Mockito.mock(ExtDefaultCommerceCartRestorationStrategy.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.cartService = Mockito.mock(CartService.class);
	}

	@Test
	public void restoreCart() throws CommerceCartRestorationException
	{
		//TISSEC-50
		cartModel.setCheckUssid("");//TODO : Please enter ussid
		cartModel.setCode("");//TODO : Please enter code
		cartModel.setDeliveryCost(Double.valueOf(123));
		given(parameter.getCart()).willReturn(cartModel);
		final List modifications = new ArrayList();
		if (extDefaultCommerceCartRestorationStrategy.isCartInValidityPeriod(cartModel))
		{
			cartModel.setCalculated(Boolean.FALSE);
			final PaymentTransactionModel payment = Mockito.mock(PaymentTransactionModel.class);
			payment.setCode("COD");
			payment.setPaymentProvider("");//TODO : Please enter payment provider

			final List<PaymentTransactionModel> paymentList = new ArrayList<PaymentTransactionModel>();
			paymentList.add(payment);
			cartModel.setPaymentTransactions(paymentList);

			extDefaultCommerceCartRestorationStrategy.clearPaymentTransactionsOnCart(cartModel);

			cartModel.setGuid("");//TODO : Please enter guid
			modelService.save(cartModel);
			cartService.setSessionCart(cartModel);
			try
			{
				commerceCartCalculationStrategy.recalculateCart(cartModel);
				cartService.setSessionCart(cartModel);
			}
			catch (final IllegalStateException ex)
			{
				LOG.error("Failed to recalculate order [" + cartModel.getCode() + "]", ex);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cart " + cartModel.getCode() + " was found to be valid and was restored to the session.");
			}
		}


		restoration.setModifications(modifications);
		given(extDefaultCommerceCartRestorationStrategy.restoreCart(parameter)).willReturn(restoration);


	}

	@Test
	protected void testIsCartInValidityPeriod()
	{
		final boolean success = true;
		Mockito.when(Boolean.valueOf(extDefaultCommerceCartRestorationStrategy.isCartInValidityPeriod(cartModel))).thenReturn(
				Boolean.valueOf(success));
	}

	@Test
	protected void testClearPaymentTransactionsOnCart()
	{
		cartModel.setPaymentTransactions(Collections.EMPTY_LIST);
	}

	@Test
	protected void rebuildSessionCart() throws CommerceCartModificationException
	{
		final CartModel newCart = Mockito.mock(CartModel.class);
		newCart.setCheckUssid("");//TODO : Please enter ussid
		newCart.setCode("");//TODO : Please enter code
		newCart.setDeliveryCost(Double.valueOf(123));
		newCart.setCalculated(Boolean.TRUE);
		final List modifications = new ArrayList();
		given(cartFactory.createCart()).willReturn(newCart);
		if (!(parameter.getCart().equals(newCart)))
		{
			extDefaultCommerceCartRestorationStrategy.rewriteEntriesFromCartToCart(parameter, parameter.getCart(), newCart,
					modifications);

			newCart.setCalculated(Boolean.FALSE);
			cartService.setSessionCart(newCart);
			commerceCartCalculationStrategy.calculateCart(parameter);
			modelService.remove(parameter.getCart());
		}
		given(extDefaultCommerceCartRestorationStrategy.rebuildSessionCart(parameter)).willReturn(modifications);
	}

	@Test
	public void rewriteEntriesFromCartToCart() throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		//final CartModel fromCartModel = new CartModel();
		final CartModel toCartModel = new CartModel();
		final List<CommerceCartModification> modifications = new ArrayList<CommerceCartModification>();
		toCartModel.setCheckUssid("");//TODO : Please enter ussid
		toCartModel.setCode("");//TODO : Please enter code
		toCartModel.setDeliveryCost(Double.valueOf(123));

		final CommerceCartParameter newCartParameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		newCartParameter.setCart(toCartModel);
		final ProductModel productModel = new ProductModel();
		productModel.setCode("");//TODO : Please enter code
		productModel.setMrp(Double.valueOf(4563));
		productModel.setDescription("");//TODO : Please enter description
		newCartParameter.setProduct(productModel);

		final PointOfServiceModel pointOfService = new PointOfServiceModel();
		pointOfService.setDescription("");//TODO : Please enter description
		pointOfService.setDisplayName("");//TODO : Please enter display name
		newCartParameter.setPointOfService(pointOfService);
		newCartParameter.setQuantity(123);
		newCartParameter.setUssid("");//TODO : Please enter ussid
		final UnitModel unitModel = new UnitModel();
		unitModel.setCode("");//TODO : Please enter code
		unitModel.setName("");//TODO : Please enter name
		newCartParameter.setUnit(unitModel);
		newCartParameter.setCreateNewEntry(false);

		final CommerceCartModification modification = mplCommerceAddToCartStrategy.addToCart(newCartParameter);
		modifications.add(modification);
	}

}
