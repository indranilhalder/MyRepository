/**
 *
 */
package com.tisl.mpl.coupon.service;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;


public interface MplCustomerDetailsService
{

	List<CustomerModel> getCustomer();

	List<VoucherModel> getVoucher();

	List<CartModel> getCartDetails();

}