/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress;

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
	
}
