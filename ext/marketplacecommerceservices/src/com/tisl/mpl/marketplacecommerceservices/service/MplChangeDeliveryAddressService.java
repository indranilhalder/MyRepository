/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * @author prasad1
 *
 */
public interface MplChangeDeliveryAddressService
{
	public abstract boolean changeDeliveryAddressCallToOMS(String code, AddressModel newDeliveryAddress);
	public boolean isDeliveryAddressChangable(OrderModel orderModel);
}