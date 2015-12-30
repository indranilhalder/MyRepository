/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface ExtCommerceCartMergingStrategy
{
	public abstract void mergeCarts(CartModel paramCartModel1, CartModel paramCartModel2, List<CommerceCartModification> paramList)
			throws CommerceCartMergingException;
}
