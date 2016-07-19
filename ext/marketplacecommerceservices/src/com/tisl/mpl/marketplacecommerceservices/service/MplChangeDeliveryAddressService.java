/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.order.data.OrderData;

import com.tisl.mpl.core.model.TemproryAddressModel;


/**
 * @author prasad1
 *
 */
public interface MplChangeDeliveryAddressService
{


	public boolean isDeliveryAddressChangable(String orderId);


	/**
	 * If Old Address and Changed address both are different then save TemproryAddressModel
	 *
	 * @param orderCode
	 * @return flag true or false
	 */
	public boolean saveTemproryAddress(final String orderCode, final TemproryAddressModel temproryAddressModel);

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
	public TemproryAddressModel geTemproryAddressModel(String orderCode);

	public void removeTemproryAddress(String orderCode);
}
