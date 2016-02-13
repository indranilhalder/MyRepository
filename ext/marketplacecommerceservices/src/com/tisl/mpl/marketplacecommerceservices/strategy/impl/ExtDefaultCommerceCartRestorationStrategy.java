/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy.impl;

import de.hybris.platform.commerceservices.order.CommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartRestorationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartFactory;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.strategy.ExtCommerceCartRestorationStrategy;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplCommerceAddToCartStrategy;


/**
 * @author TCS
 *
 */
public class ExtDefaultCommerceCartRestorationStrategy extends DefaultCommerceCartRestorationStrategy implements
		ExtCommerceCartRestorationStrategy
{
	@Autowired
	private MplCommerceAddToCartStrategy mplCommerceAddToCartStrategy;

	private static final Logger LOG = Logger.getLogger(DefaultCommerceCartRestorationStrategy.class);

	private CartFactory cartFactory;
	private TimeService timeService;
	private KeyGenerator guidKeyGenerator;
	private BaseSiteService baseSiteService;
	private CommerceAddToCartStrategy commerceAddToCartStrategy;

	@Override
	public CommerceCartRestoration restoreCart(final CommerceCartParameter parameter) throws CommerceCartRestorationException
	{
		final CartModel cartModel = parameter.getCart();
		final CommerceCartRestoration restoration = new CommerceCartRestoration();
		final List modifications = new ArrayList();
		if (cartModel != null)
		{
			if (getBaseSiteService().getCurrentBaseSite().equals(cartModel.getSite()))
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Restoring from cart " + cartModel.getCode() + ".");
				}
				if (isCartInValidityPeriod(cartModel))
				{
					cartModel.setCalculated(Boolean.FALSE);
					if (!(cartModel.getPaymentTransactions().isEmpty()))
					{
						clearPaymentTransactionsOnCart(cartModel);

						cartModel.setGuid(getGuidKeyGenerator().generate().toString());
					}

					getModelService().save(cartModel);
					getCartService().setSessionCart(cartModel);
					try
					{
						//getCommerceCartCalculationStrategy().recalculateCart(parameter);
						getCommerceCartCalculationStrategy().recalculateCart(cartModel);
						getCartService().setSessionCart(cartModel);
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
				else
				{
					try
					{
						modifications.addAll(rebuildSessionCart(parameter));
					}
					catch (final CommerceCartModificationException e)
					{
						throw new CommerceCartRestorationException(e.getMessage(), e);
					}
				}
			}
			else
			{
				LOG.warn(String.format("Current Site %s does not equal to cart %s Site %s", new Object[]
				{ getBaseSiteService().getCurrentBaseSite(), cartModel, cartModel.getSite() }));
			}
		}
		restoration.setModifications(modifications);
		return restoration;
	}

	@Override
	protected Collection<CommerceCartModification> rebuildSessionCart(final CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		final List modifications = new ArrayList();
		final CartModel newCart = getCartFactory().createCart();

		if (!(parameter.getCart().equals(newCart)))
		{
			rewriteEntriesFromCartToCart(parameter, parameter.getCart(), newCart, modifications);

			newCart.setCalculated(Boolean.FALSE);
			getCartService().setSessionCart(newCart);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cart " + parameter.getCart().getCode()
						+ " was found and was invalid. A new cart was created and populated.");
			}
			getCommerceCartCalculationStrategy().calculateCart(parameter);
			getModelService().remove(parameter.getCart());
		}
		return modifications;
	}

	@Override
	protected void rewriteEntriesFromCartToCart(final CommerceCartParameter parameter, final CartModel fromCartModel,
			final CartModel toCartModel, final List<CommerceCartModification> modifications)
			throws CommerceCartModificationException
	{

		for (final AbstractOrderEntryModel entry : fromCartModel.getEntries())
		{
			final CommerceCartParameter newCartParameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			newCartParameter.setCart(toCartModel);
			newCartParameter.setProduct(entry.getProduct());
			newCartParameter.setPointOfService(entry.getDeliveryPointOfService());
			newCartParameter.setQuantity((entry.getQuantity() == null) ? 0L : entry.getQuantity().longValue());
			newCartParameter.setUssid((null != entry.getSelectedUSSID()) ? entry.getSelectedUSSID() : "");
			newCartParameter.setUnit(entry.getUnit());
			newCartParameter.setCreateNewEntry(false);
			CommerceCartModification modification = mplCommerceAddToCartStrategy.addToCart(newCartParameter);

			if (modification.getQuantityAdded() == 0L)
			{
				parameter.setPointOfService(null);

				modification = mplCommerceAddToCartStrategy.addToCart(newCartParameter);
				modification.setDeliveryModeChanged(Boolean.TRUE);
			}

			modifications.add(modification);
		}

	}



	@Override
	protected boolean isCartInValidityPeriod(final CartModel cartModel)
	{
		return new DateTime(cartModel.getModifiedtime()).isAfter(new DateTime(getTimeService().getCurrentTime())
				.minusSeconds(getCartValidityPeriod()));
	}

	@Override
	protected void clearPaymentTransactionsOnCart(final CartModel cartModel)
	{
		getModelService().removeAll(cartModel.getPaymentTransactions());
		cartModel.setPaymentTransactions(Collections.EMPTY_LIST);
	}

	@Override
	protected CartFactory getCartFactory()
	{
		return this.cartFactory;
	}

	@Override
	@Required
	public void setCartFactory(final CartFactory cartFactory)
	{
		this.cartFactory = cartFactory;
	}

	@Override
	protected TimeService getTimeService()
	{
		return this.timeService;
	}

	@Override
	@Required
	public void setTimeService(final TimeService timeService)
	{
		this.timeService = timeService;
	}

	@Override
	protected BaseSiteService getBaseSiteService()
	{
		return this.baseSiteService;
	}

	@Override
	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Override
	protected KeyGenerator getGuidKeyGenerator()
	{
		return this.guidKeyGenerator;
	}

	@Override
	@Required
	public void setGuidKeyGenerator(final KeyGenerator guidKeyGenerator)
	{
		this.guidKeyGenerator = guidKeyGenerator;
	}

	@Override
	protected CommerceAddToCartStrategy getCommerceAddToCartStrategy()
	{
		return this.commerceAddToCartStrategy;
	}

	@Override
	@Required
	public void setCommerceAddToCartStrategy(final CommerceAddToCartStrategy commerceAddToCartStrategy)
	{
		this.commerceAddToCartStrategy = commerceAddToCartStrategy;
	}

}
