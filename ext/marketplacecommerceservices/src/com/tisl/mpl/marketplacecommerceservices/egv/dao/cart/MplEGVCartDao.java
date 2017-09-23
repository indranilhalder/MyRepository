/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.egv.dao.cart;

import de.hybris.platform.core.model.order.CartModel;

/**
 * @author PankajS
 *
 */
public interface MplEGVCartDao
{
 public void  removeOldEGVCartCurrentCustomer();
 
 public CartModel getEGVCartModel(String uid);
 
}
