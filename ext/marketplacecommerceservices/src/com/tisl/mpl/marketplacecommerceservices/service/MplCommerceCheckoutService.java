/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;


/**
 * @author TCS
 *
 */
public interface MplCommerceCheckoutService extends CommerceCheckoutService
{
	/**
	 *
	 * @param parameter
	 * @param result
	 * @throws InvalidCartException
	 */
	void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException;
}
