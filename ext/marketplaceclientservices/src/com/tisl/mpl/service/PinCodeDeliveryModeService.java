/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.List;

import com.tisl.mpl.wsdto.PinCodeDeliveryModeListResponse;


/**
 * @author 559379
 *
 */
public interface PinCodeDeliveryModeService
{
	public PinCodeDeliveryModeListResponse prepPinCodeDeliveryModetoOMS(String pin, List<PincodeServiceData> reqData);
}
