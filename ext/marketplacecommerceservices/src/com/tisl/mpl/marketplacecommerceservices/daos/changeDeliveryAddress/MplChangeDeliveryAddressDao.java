/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.core.model.TemproryAddressModel;


/**
 * @author pankajk
 *
 */

public interface MplChangeDeliveryAddressDao
{

	/**
	 * Based On OrderId We get TemAdresss
	 * @param orderCode
	 * @return temproryAddressModel
	 */
	public TemproryAddressModel geTemproryAddressModel(String orderCode);
	public void  saveDeliveryAddress(OrderModel order,AddressModel address);
}
