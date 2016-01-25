/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.ContactUsDao;


/**
 * @author TCS
 *
 */
public class ContactUsDaoImpl implements ContactUsDao
{


	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.contactus.ContactUsTabComponentDao#getOrderId(java.lang.String)
	 */
	@Override
	public List<OrderModel> getOrderModel(final String orderCode)
	{

		//final OrderModel order = null;
		if (null != orderCode && !orderCode.isEmpty())
		{
			final String queryString = "SELECT {o.PK} FROM {" + OrderModel._TYPECODE + " AS o}"

			+ " WHERE {o:" + OrderModel.CODE + "}=?orderCode";



			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("orderCode", orderCode.trim());
			return flexibleSearchService.<OrderModel> search(query).getResult();
		}
		else
		{
			return null;
		}

	}


}
