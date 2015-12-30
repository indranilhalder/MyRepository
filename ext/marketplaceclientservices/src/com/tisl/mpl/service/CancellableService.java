/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.xml.pojo.CancellableResponse;


/**
 * @author TCS
 *
 */
public interface CancellableService
{
	public CancellableResponse cancelableCheck(final String orderId, final String transactionId);

}
