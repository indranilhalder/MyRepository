/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.xml.pojo.MplChangeDeliveryAddressRequest;
import com.tisl.mpl.xml.pojo.MplChangeDeliveryAddressResponce;


/**
 * @author prasad1
 *
 */
public interface MplChangeDeliveryAddressClientService
{
	MplChangeDeliveryAddressResponce changeDeliveryAddressDataToOMS(MplChangeDeliveryAddressRequest changeDeliveryAddressRequest);
}
