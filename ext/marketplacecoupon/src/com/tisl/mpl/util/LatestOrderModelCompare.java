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
	/**
	 * This method compares two orderModel for sorting
	 *
	 * @param o1
	 * @param o2
	 * @return int
	 *
	 */
	@Override
	public int compare(final OrderModel o1, final OrderModel o2)
	{
		return ((o2.getDate().compareTo(o1.getDate()) > 0) ? 1 : (-1));
	}
}