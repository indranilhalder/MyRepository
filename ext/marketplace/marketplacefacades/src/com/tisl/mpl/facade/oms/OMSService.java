/**
 *
 */
package com.tisl.mpl.facade.oms;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;

import java.util.List;


/**
 * @author 559379
 *
 */
public interface OMSService
{

	public void sendPINCodeandDeliveryModetoOMS(String pin, List<PincodeServiceData> reqData);
}
