/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.coupon.dao.MplCustomerDetailsDao;
import com.tisl.mpl.coupon.service.MplCustomerDetailsService;




public class MplCustomerDetailsServiceImpl implements MplCustomerDetailsService
{

	@Resource(name = "customerDetailsDao")
	private MplCustomerDetailsDao customerDetailsDao;


	/**
	 * @return the customerDetailsDao
	 */
	public MplCustomerDetailsDao getCustomerDetailsDao()
	{
		return customerDetailsDao;
	}

	/**
	 * @return List<CustomerModel>
	 */
	@Override
	public List<CustomerModel> getCustomer()
	{
		//Content
		return getCustomerDetailsDao().findCustomer();
	}


	/**
	 *
	 * @param customerDetailsDao
	 */
	@Required
	public void setCustomerDetailsDao(final MplCustomerDetailsDao customerDetailsDao)
	{
		this.customerDetailsDao = customerDetailsDao;
	}



	/**
	 * This method returns the list of CartModel
	 */
	@Override
	public List<CartModel> getCartDetails()
	{
		return getCustomerDetailsDao().findCart();
	}

}