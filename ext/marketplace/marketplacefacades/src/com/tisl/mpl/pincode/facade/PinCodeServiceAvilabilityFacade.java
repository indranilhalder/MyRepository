/**
 *
 */
package com.tisl.mpl.pincode.facade;

import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.List;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface PinCodeServiceAvilabilityFacade
{

	public List<PinCodeResponseData> getResonseForPinCode(final String productCode, final String pin,
			List<PincodeServiceData> reqData) throws EtailNonBusinessExceptions;

	/**
	 * @param pin
	 * @param requestData
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws ClientEtailNonBusinessExceptions
	 */
	List<PinCodeResponseData> getServiceablePinCodeCart(String pin, List<PincodeServiceData> requestData)
			throws EtailNonBusinessExceptions;

}
