/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.marketplacecommerceservices.daos.ForgetPasswordDao;


/**
 * @author 682160
 *
 */

@Component(value = "ForgetPasswordDao")
public class ForgetPasswordDaoImpl implements ForgetPasswordDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

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

	/**
	 * @description method is called to find the Customer from Database return List
	 */
	@Override
	public List<CustomerModel> findCustomer(final String email)
	{
		final String queryString = //
		"SELECT {p:" + CustomerModel.PK + "}" //
				+ "FROM {" + CustomerModel._TYPECODE + " AS p} "//
				+ "WHERE " + "{p:" + CustomerModel.ORIGINALUID + "}=?code ";


		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", email);

		return flexibleSearchService.<CustomerModel> search(query).getResult();
	}
}
