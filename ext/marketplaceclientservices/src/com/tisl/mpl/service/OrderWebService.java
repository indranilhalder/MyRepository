package com.tisl.mpl.service;

import com.hybris.oms.domain.order.Order;



/*@author 884206*/

public interface OrderWebService
{

	/**
	 * @param order
	 * @return
	 */
	public Order createOmsOrder(Order order) throws Exception;
}
