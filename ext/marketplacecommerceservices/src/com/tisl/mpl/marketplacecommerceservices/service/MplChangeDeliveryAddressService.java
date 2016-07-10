/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.OrderModel;

import com.tisl.mpl.core.model.TemproryAddressModel;


/**
 * @author prasad1
 *
 */
public interface MplChangeDeliveryAddressService
{


public boolean isDeliveryAddressChangable(OrderModel orderModel);


/**
 * If Old Address and  Changed address both are  different then save TemproryAddressModel
 * @param orderCode
 * @return flag true or false
 */
public boolean saveAsTemproryAddressForCustomer( final String orderCode, final TemproryAddressModel temproryAddressModel);

/**
 * Based on orderCode We get TemproryAddressModel And OrderModel
 * @param orderCode
 * @return flag
 */
public boolean changeDeliveryAddress(String orderCode);


/**
 * 
 * @param orderCode
 * @return TemproryAddressModel
 */
public TemproryAddressModel geTemproryAddressModel(String orderCode);
}
