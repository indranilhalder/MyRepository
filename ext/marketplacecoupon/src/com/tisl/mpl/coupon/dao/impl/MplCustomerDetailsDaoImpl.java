/**
 *
 */
package com.tisl.mpl.coupon.dao.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.coupon.dao.MplCustomerDetailsDao;



@Component(value = "customerDetailsDao")
public class MplCustomerDetailsDaoImpl implements MplCustomerDetailsDao
{

	private static final Logger LOG = Logger.getLogger(MplCustomerDetailsDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.CustomerDetailsDao#findCustomer()
	 */
	@Override
	public List<CustomerModel> findCustomer()
	{
		final String queryString = //
		"SELECT {p:" + CustomerModel.PK + "} "//
				+ "FROM {" + CustomerModel._TYPECODE + " AS p} ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);


		return flexibleSearchService.<CustomerModel> search(query).getResult();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.CustomerDetailsDao#findCart()
	 */
	@Override
	public List<CartModel> findCart()
	{
		final String queryString = //
		"SELECT {p:" + CartModel.PK + "} "//
				+ "FROM {" + CartModel._TYPECODE + " AS p} ";

		LOG.debug("QueryString is :::  " + queryString);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);


		return flexibleSearchService.<CartModel> search(query).getResult();
	}

	@Override
	public List<VoucherModel> findVoucher()
	{
		// TODO Auto-generated method stub
		return null;
	}



}