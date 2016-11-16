/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCheckoutService;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplCommercePlaceOrderStrategy;


/**
 * @author TCS
 *
 */
public class MplDefaultCommerceCheckoutService extends DefaultCommerceCheckoutService implements MplCommerceCheckoutService
{

	@Resource(name = "commercePlaceOrderStrategy")
	private MplCommercePlaceOrderStrategy mplCommercePlaceOrderStrategy;

	/**
	 * This method calls before submit order explicitly for TPR-629
	 *
	 * @throws CalculationException
	 *
	 */
	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException, CalculationException
	{
		mplCommercePlaceOrderStrategy.beforeSubmitOrder(parameter, result);
	}

}
