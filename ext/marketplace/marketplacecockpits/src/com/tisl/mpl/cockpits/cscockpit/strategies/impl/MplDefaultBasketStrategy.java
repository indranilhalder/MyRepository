/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.strategies.impl;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.cscockpit.widgets.controllers.strategies.impl.DefaultBasketStrategy;

/**
 * @author 890223
 *
 */
public class MplDefaultBasketStrategy extends DefaultBasketStrategy {

	@Override
	protected void updateCallContextFromCart(CartModel cart) {
		cart.setChannel(SalesApplication.CALLCENTER);
		getModelService().save(cart);
		super.updateCallContextFromCart(cart);
	}

	@Override
	protected void updateCartFromCallContext(CartModel cart) {
		cart.setChannel(SalesApplication.CALLCENTER);
		getModelService().save(cart);
		super.updateCartFromCallContext(cart);
	}

}
