/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;


import de.hybris.platform.core.model.order.OrderModel;


import java.util.List;

import com.tisl.mpl.core.model.TemproryAddressModel;


/**
 * @author prasad1
 *
 */
public interface MplDeliveryAddressService
{


	public boolean isDeliveryAddressChangable(String orderId);


	/**
	 * If Old Address and Changed address both are different then save TemproryAddressModel
	 *
	 * @param orderCode
	 * @return flag true or false
	 */
	public boolean saveTemporaryAddress(OrderModel orderModel,TemproryAddressModel temproryAddressModel);

	/**
	 * Based on orderCode We get TemproryAddressModel And OrderModel
	 *
	 * @param orderCode
	 * @return flag
	 */
	public boolean saveDeliveryAddress(String orderCode);


	/**
	 *
	 * @param orderCode
	 * @return TemproryAddressModel
	 */
   
   public TemproryAddressModel getTemporaryAddressModel(String orderCode);

   public void removeTemporaryAddress(String orderCode);
	
   public boolean setStatusForTemporaryAddress(String orderId,boolean flag);
   
   public boolean updateContactDetails(TemproryAddressModel temproryAddressModel,OrderModel orderModel);
   
   List<TemproryAddressModel> getTemporaryAddressModelList(String dateFrom,String dateTo);
  
}
