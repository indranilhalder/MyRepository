/**
 *
 */
package com.tisl.mpl.service;

import com.hybris.oms.domain.order.Order;


/**
 * @author TCS
 *
 */
public interface MplSendOrderFromCommerceToCRM
{
	public void orderCreationDataToCRM(final Order orderData) throws Exception;
}
