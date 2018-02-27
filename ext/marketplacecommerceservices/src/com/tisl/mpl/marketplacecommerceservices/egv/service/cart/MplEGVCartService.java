/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.egv.service.cart;

import de.hybris.platform.core.model.order.CartModel;

/**
 * @author PankajS
 *
 */
public interface MplEGVCartService
{
	public void removeOldEGVCartCurrentCustomer();
	
	public CartModel getEGVCartModel(final String guid);
}
