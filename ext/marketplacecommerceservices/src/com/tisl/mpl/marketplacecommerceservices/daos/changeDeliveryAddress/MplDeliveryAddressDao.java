/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress;

import com.tisl.mpl.core.model.TemproryAddressModel;


/**
 * @author pankajk
 *
 */

public interface MplDeliveryAddressDao
{

	/**
	 * Based On OrderId We get TemAdresss
	 * 
	 * @param orderCode
	 * @return temproryAddressModel
	 */
	public TemproryAddressModel getTemporaryAddressModel(String orderCode);
}
