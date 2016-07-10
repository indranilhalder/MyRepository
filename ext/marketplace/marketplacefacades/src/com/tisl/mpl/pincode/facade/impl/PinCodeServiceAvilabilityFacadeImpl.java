/**
 *
 */

package com.tisl.mpl.pincode.facade.impl;

import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;


/**
 * @author TCS
 *
 */

public class PinCodeServiceAvilabilityFacadeImpl implements PinCodeServiceAvilabilityFacade
{
	@Resource(name = "mplPincodeRestrictionService")
	private MplPincodeRestrictionService mplPincodeRestrictionService;

	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	/**
	 * @description this method checks the restriction list and calls pincode service accordingly
	 *
	 * @param pin
	 * @param productCode
	 * @return listingId
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<PinCodeResponseData> getResonseForPinCode(final String productCode, final String pin,
			final List<PincodeServiceData> requestData) throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions
	{
		final List<String> ussidList = new ArrayList<String>();
		final List<String> sellerIdList = new ArrayList<String>();
		for (final PincodeServiceData reqData : requestData)
		{
			ussidList.add(reqData.getUssid());
			sellerIdList.add(reqData.getSellerId());
		}

		//checing if any restricted pincodes are present
		final List<PincodeServiceData> validReqData = mplPincodeRestrictionService.getRestrictedPincode(ussidList, sellerIdList,
				productCode, pin, requestData);
		List<PinCodeResponseData> response = null;
		if ((null != validReqData))
		{
			response = mplCommerceCartService.getAllResponsesForPinCode(pin, validReqData);
		}

		return response;
	}

	/**
	 * @description this method checks the restriction list and calls pincode service accordingly
	 *
	 * @param pincode
	 * @param requestData
	 * @return List<PinCodeResponseData>
	 * @throws EtailNonBusinessExceptions
	 *            ,ClientEtailNonBusinessExceptions
	 */
	@Override
	public List<PinCodeResponseData> getServiceablePinCodeCart(final String pincode, final List<PincodeServiceData> requestData)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions
	{
		final List<PinCodeResponseData> response = mplCommerceCartService.getServiceablePinCodeCart(pincode, requestData);
		return response;
	}
}
