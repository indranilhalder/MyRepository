/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * @author TCS
 *
 */
public interface ExtCommerceCartRestorationStrategy
{
	CommerceCartRestoration restoreCart(CommerceCartParameter parameter) throws CommerceCartRestorationException;
	
	
}
