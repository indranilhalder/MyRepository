/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListResponse;
import com.tisl.mpl.wsdto.StoreLocatorAtsResponseObject;


/**
 * @author TCS
 *
 */
public interface PinCodeDeliveryModeService
{
	PinCodeDeliveryModeListResponse prepPinCodeDeliveryModetoOMS(String pin, List<PincodeServiceData> reqData);

	/**
	 * @param storeLocationRequestDataList
	 * @return
	 */
	StoreLocatorAtsResponseObject prepStoreLocationsToOMS(List<StoreLocationRequestData> storeLocationRequestDataList,
			CartModel cartModel);


	/*
	 * @desc used for validate connect timeout and read time out exceptions from oms rest call for pincode serviceabilty
	 * and inventory reservation
	 *
	 * @param ex
	 *
	 * @param exceptionType
	 *
	 * @return void
	 *
	 * @throws ClientEtailNonBusinessExceptions
	 */
	void validateOMSException(final Exception ex, final String exceptionType) throws ClientEtailNonBusinessExceptions;
}
