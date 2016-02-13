/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.coupon.dao.MplCustomerDetailsDao;
import com.tisl.mpl.coupon.service.MplCustomerDetailsService;




public class MplCustomerDetailsServiceImpl implements MplCustomerDetailsService
{

	private MplCustomerDetailsDao customerDetailsDao;


	/**
	 * @return the customerDetailsDao
	 */
	public MplCustomerDetailsDao getCustomerDetailsDao()
	{
		return customerDetailsDao;
	}

	private static final Logger LOG = Logger.getLogger(MplCustomerDetailsServiceImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.CustomerDetailsService#getCustomer()
	 */
	@Override
	public List<CustomerModel> getCustomer()
	{
		//Content
		return customerDetailsDao.findCustomer();
	}

	@Required
	public void setCustomerDetailsDao(final MplCustomerDetailsDao customerDetailsDao)
	{
		this.customerDetailsDao = customerDetailsDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.CustomerDetailsService#getVoucher()
	 */
	@Override
	public List<VoucherModel> getVoucher()
	{
		LOG.debug("Inside get Voucher in DefaultCustomerDetailsImpl");
		return customerDetailsDao.findVoucher();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.CustomerDetailsService#getCartDetails()
	 */
	@Override
	public List<CartModel> getCartDetails()
	{
		return customerDetailsDao.findCart();
	}

}