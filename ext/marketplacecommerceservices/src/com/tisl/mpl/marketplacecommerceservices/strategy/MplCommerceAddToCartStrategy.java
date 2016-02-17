/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * @author TCS
 *
 */
public interface MplCommerceAddToCartStrategy
{
	public abstract CommerceCartModification addToCart(CommerceCartParameter paramCommerceCartParameter)
			throws CommerceCartModificationException;
}
