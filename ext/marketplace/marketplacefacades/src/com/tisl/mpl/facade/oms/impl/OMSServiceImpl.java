package com.tisl.mpl.facade.oms.impl;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.facade.oms.OMSService;
import com.tisl.mpl.service.PinCodeDeliveryModeService;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListResponse;


/**
 * @author TCS
 *
 */
public class OMSServiceImpl implements OMSService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(OMSServiceImpl.class);

	@Resource(name = "pinCodeDeliveryModeService")
	private PinCodeDeliveryModeService pinCodeDeliveryModeService;

	@Override
	public void sendPINCodeandDeliveryModetoOMS(final String pin, final List<PincodeServiceData> reqData)
	{

		final PinCodeDeliveryModeListResponse pincodeRes = pinCodeDeliveryModeService.prepPinCodeDeliveryModetoOMS(pin, reqData);
		LOG.debug("pincodeRes" + pincodeRes);
	}

	/**
	 * @return the pinCodeDeliveryModeService
	 */
	public PinCodeDeliveryModeService getPinCodeDeliveryModeService()
	{
		return pinCodeDeliveryModeService;
	}

	/**
	 * @param pinCodeDeliveryModeService
	 *           the pinCodeDeliveryModeService to set
	 */
	public void setPinCodeDeliveryModeService(final PinCodeDeliveryModeService pinCodeDeliveryModeService)
	{
		this.pinCodeDeliveryModeService = pinCodeDeliveryModeService;
	}
}
