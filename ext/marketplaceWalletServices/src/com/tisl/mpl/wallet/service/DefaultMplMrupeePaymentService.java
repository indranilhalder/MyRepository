/**
 *
 */
package com.tisl.mpl.wallet.service;

import com.tisl.mpl.wallet.request.MRupeeRefundRequest;


/**
 * @author TCS
 *
 */
public interface DefaultMplMrupeePaymentService
{
	/**
	 * Generating the checksum for Purchase
	 *
	 * @param walletOrderId
	 * @param amount
	 * @return String
	 *
	 */
	public String generateCheckSum(final String walletOrderId, final String amount);

	/**
	 * Generating the checksum for Verification
	 *
	 * @param walletOrderId
	 * @return String
	 *
	 */

	public String generateCheckSumForVerification(final String walletOrderId);

	/**
	 * Generating the checksum for Refund
	 *
	 * @param mRupeeRefundRequest
	 * @return String
	 */

	public String generateCheckSum(final MRupeeRefundRequest mRupeeRefundRequest);

}
