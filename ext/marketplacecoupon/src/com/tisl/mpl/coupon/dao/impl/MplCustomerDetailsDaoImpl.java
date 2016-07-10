/**
 *
 */
package com.tisl.mpl.coupon.dao.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tisl.mpl.coupon.dao.MplCustomerDetailsDao;



@Component(value = "customerDetailsDao")
public class MplCustomerDetailsDaoImpl implements MplCustomerDetailsDao
{

	private static final Logger LOG = Logger.getLogger(MplCustomerDetailsDaoImpl.class);
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;


	/**
	 * Returns list of Customer Model
	 *
	 * @return List<CustomerModel>
	 *
	 */
	@Override
	public List<CustomerModel> findCustomer()
	{
		final String queryString = //
		"SELECT {p:" + CustomerModel.PK + "} "//
				+ "FROM {" + CustomerModel._TYPECODE + " AS p} ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);


		return getFlexibleSearchService().<CustomerModel> search(query).getResult();
	}


	/**
	 * Returns cartModel
	 *
	 * @return List<CartModel>
	 *
	 */
	@Override
	public List<CartModel> findCart()
	{
		final String queryString = //
		"SELECT {p:" + CartModel.PK + "} "//
				+ "FROM {" + CartModel._TYPECODE + " AS p} ";

		LOG.debug("QueryString is :::  " + queryString);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);


		return getFlexibleSearchService().<CartModel> search(query).getResult();
	}


	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}






}