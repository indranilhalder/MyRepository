/**
 *
 */
package com.tisl.mpl.coupon.dao;


import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;



public interface MplCustomerDetailsDao
{

	/**
	 *
	 * @return List<CustomerModel>
	 */
	List<CustomerModel> findCustomer();



	/**
	 *
	 * @return List<CartModel>
	 */
	List<CartModel> findCart();

}