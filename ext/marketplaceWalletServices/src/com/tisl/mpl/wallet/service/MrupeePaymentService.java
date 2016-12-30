/**
 *
 */
package com.tisl.mpl.wallet.service;

import org.apache.log4j.Logger;

import com.tcs.mRupee.PG.Checksum.Jar.MerchantChecksum;


/**
 * @author TCS
 *
 */
public class MrupeePaymentService
{
	private static final Logger LOG = Logger.getLogger(MrupeePaymentService.class);

	//

	/**
	 * @param walletOrderId
	 * @param amount
	 * @return
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
}
