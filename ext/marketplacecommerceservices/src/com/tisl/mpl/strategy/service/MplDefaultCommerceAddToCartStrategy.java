/**
 *
 */
package com.tisl.mpl.strategy.service;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * @author TCS
 *
 */
public interface MplDefaultCommerceAddToCartStrategy
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.order.CommerceAddToCartStrategy#addToCart(de.hybris.platform.commerceservices
	 * .service.data.CommerceCartParameter)
	 */

	public CommerceCartModification addToCartForUSSID(final CommerceCartParameter parameter)
			throws CommerceCartModificationException;

}