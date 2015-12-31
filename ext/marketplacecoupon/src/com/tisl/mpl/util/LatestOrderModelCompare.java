/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.Comparator;


/**
 * @author TCS
 *
 */
public class LatestOrderModelCompare implements Comparator<OrderModel>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final OrderModel o1, final OrderModel o2)
	{
		System.out.println(o1.getDate() + ", " + o2.getDate() + ", " + o2.getDate().compareTo(o1.getDate()));
		if (o2.getDate().compareTo(o1.getDate()) > 0)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

}