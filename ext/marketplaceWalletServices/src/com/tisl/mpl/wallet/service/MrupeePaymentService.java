/**
 *
 */
package com.tisl.mpl.wallet.service;

import org.apache.log4j.Logger;

import com.tcs.mRupee.PG.Checksum.Jar.MerchantChecksum;
import com.tisl.mpl.wallet.request.MRupeeRefundRequest;


/**
 * @author TCS
 *
 */
public class MrupeePaymentService
{
	private static final Logger LOG = Logger.getLogger(MrupeePaymentService.class);

	//

	/**
	 * Generating the checksum for Purchase
	 *
	 * @param walletOrderId
	 * @param amount
	 * @return String
	 *
	 */
	public String generateCheckSum(final String walletOrderId, final String amount)
	{
		final String[] parameters =
		{ "TULA", "P", walletOrderId, "uat", amount };//Purchase
		final String checksumKey = "U82Q3MW53S";

		final String stChesksum = MerchantChecksum.generateChecksum(parameters, checksumKey);
		LOG.debug("Checksum New:" + stChesksum);
		return stChesksum;

	}

	/**
	 * Generating the checksum for Verification
	 *
	 * @param walletOrderId
	 * @return String
	 *
	 */

	public String generateCheckSumForVerification(final String walletOrderId)
	{
		final String[] parameters =
		{ "TULA", "V", walletOrderId };//verification
		final String checksumKey = "U82Q3MW53S";
		final String stChesksum = MerchantChecksum.generateChecksum(parameters, checksumKey);
		LOG.debug("Checksum New:" + stChesksum);
		return stChesksum;

	}

	/**
	 * Generating the checksum for Refund
	 *
	 * @param mRupeeRefundRequest
	 * @return String
	 */

	public String generateCheckSum(final MRupeeRefundRequest mRupeeRefundRequest)
	{
		final String[] parameters =
		{ "TULA", "R", mRupeeRefundRequest.getRefNo(), "uat", mRupeeRefundRequest.getAmount().toString(),
				mRupeeRefundRequest.getPurchaseRefNo() };//Refund
		final String checksumKey = "U82Q3MW53S";

		final String Chesksum = MerchantChecksum.generateChecksum(parameters, checksumKey);
		LOG.debug("Checksum New:" + Chesksum);
		return Chesksum;

	}

}
