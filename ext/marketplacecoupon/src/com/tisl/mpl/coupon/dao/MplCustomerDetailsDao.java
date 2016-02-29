/**
 *
 */
package com.tisl.mpl.coupon.dao;


import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;



public interface MplCustomerDetailsDao
{

	List<CustomerModel> findCustomer();

	List<VoucherModel> findVoucher();

	List<CartModel> findCart();

}