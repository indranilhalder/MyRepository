/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;


/**
 * @author TCS
 *
 */
public interface MplCommercePlaceOrderStrategy extends CommercePlaceOrderStrategy
{
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException, CalculationException;
}
