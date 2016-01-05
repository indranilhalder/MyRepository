/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.services;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.services.checkout.CsCheckoutService;

/**
 * @author 890223
 *
 */
public interface MarketplaceCsCheckoutService extends CsCheckoutService {

	boolean checkCartReservationStatus(CartModel cart);

	void checkCustomerStatus(CartModel cart) throws ValidationException;


}
