/**
 *
 */
package com.tisl.mpl.wallet.service;

import com.tisl.mpl.wallet.refund.MRupeeRefundResponse;
import com.tisl.mpl.wallet.request.MRupeeRefundRequest;


/**
 * @author TCS
 *
 */
public interface DefaultMplMRupeeRefundService
{

	/**
	 * Method is called for doing refund at the time of cancel and return
	 *
	 * @param refundRequest
	 * @return MRupeeRefundResponse
	 */

	public MRupeeRefundResponse refund(final MRupeeRefundRequest refundRequest);
}
