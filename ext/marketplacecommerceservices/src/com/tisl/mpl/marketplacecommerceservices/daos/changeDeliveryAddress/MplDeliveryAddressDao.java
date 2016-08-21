/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress;


import java.util.List;

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
	
	
	public List<TemproryAddressModel> getTemporaryAddressModelList(String dateFrom,String toDate);
}
