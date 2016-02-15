/**
 *
 */
package com.tisl.mpl.coupon.service;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;


public interface MplCustomerDetailsService
{

	List<CustomerModel> getCustomer();

	List<CartModel> getCartDetails();

}