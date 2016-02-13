/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartSplitStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;


/**
 * @author 1006687
 *
 */
public class MarketPlaceCommerceCartSplitStrategy extends DefaultCommerceCartSplitStrategy
{

	@Override
	public long split(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		ServicesUtil.validateParameterNotNull(parameters.getCart(), "Cart model cannot be null");

		final AbstractOrderEntryModel cartEntry = getEntryForNumber(parameters.getCart(), (int) parameters.getEntryNumber());
		if ((cartEntry != null) && (cartEntry.getQuantity().longValue() > 1L))

		{
			final CommerceCartParameter updateQuantityParams = new CommerceCartParameter();
			updateQuantityParams.setEnableHooks(true);
			updateQuantityParams.setCart(parameters.getCart());
			updateQuantityParams.setEntryNumber(parameters.getEntryNumber());
			updateQuantityParams.setQuantity(cartEntry.getQuantity().longValue() - 1L);
			updateQuantityParams.setUssid(cartEntry.getSelectedUSSID());
			getCommerceUpdateCartEntryStrategy().updateQuantityForCartEntry(updateQuantityParams);


			final CommerceCartParameter addToCartParams = new CommerceCartParameter();
			addToCartParams.setEnableHooks(true);
			addToCartParams.setCart(parameters.getCart());
			addToCartParams.setProduct(cartEntry.getProduct());
			addToCartParams.setUnit(cartEntry.getUnit());
			addToCartParams.setQuantity(1L);
			addToCartParams.setCreateNewEntry(true);
			addToCartParams.setUssid(cartEntry.getSelectedUSSID());

			final CommerceCartModification modification = getCommerceAddToCartStrategy().addToCart(addToCartParams);
			return (((modification != null) && (modification.getEntry() != null) && (modification.getEntry().getEntryNumber() != null)) ? modification
					.getEntry().getEntryNumber().longValue()
					: 0L);
		}
		return 0L;
	}
}
