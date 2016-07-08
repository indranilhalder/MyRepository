/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * @author prasad1
 *
 */
public interface MplChangeDeliveryAddressService
{


public boolean isDeliveryAddressChangable(OrderModel orderModel);
}
