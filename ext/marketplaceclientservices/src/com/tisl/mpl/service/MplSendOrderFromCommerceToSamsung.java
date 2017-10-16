/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.order.OrderModel;

import org.json.simple.JSONObject;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.samsung.wsdto.OrderResponseWsDTO;


/**
 * @author TCS
 *
 */
public interface MplSendOrderFromCommerceToSamsung
{
	//For TPR-5667
	public JSONObject postResponseToSamsung(OrderResponseWsDTO orderResponse, OrderModel orderModel)
			throws ClientEtailNonBusinessExceptions;
}
