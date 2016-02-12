/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.order;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * @author TCS
 *
 */
public interface MplCommerceCartCalculationStrategy
{
	boolean calculateCart(CommerceCartParameter paramCommerceCartParameter);

	boolean recalculateCart(CommerceCartParameter paramCommerceCartParameter);
}
