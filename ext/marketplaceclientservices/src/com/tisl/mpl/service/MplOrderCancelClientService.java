/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;


/**
 * @author TCS
 *
 */
public interface MplOrderCancelClientService
{

	/**
	 * @param cancelOrderRequest
	 * @return MplOrderIsCancellableResponse
	 */
	MplOrderIsCancellableResponse orderCancelDataToOMS(MplCancelOrderRequest cancelOrderRequest);

}
