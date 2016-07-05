/**
 *
 */
package com.tis.mpl.facade.changedelivery;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * @author prasad1
 *
 */
public interface ChangeDeliveryAddressFacade
{
	public boolean changeDeliveryRequestToOMS(String orderId, AddressModel newDeliveryAddress);

	public void createcrmTicketForChangeDeliveryAddress(OrderModel order, String customerId, String source);
}
