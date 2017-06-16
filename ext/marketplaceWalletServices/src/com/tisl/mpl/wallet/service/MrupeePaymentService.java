/**
 *
 */
package com.tisl.mpl.wallet.service;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.log4j.Logger;

import com.tcs.mRupee.PG.Checksum.Jar.MerchantChecksum;
import com.tisl.mpl.wallet.constants.MarketplaceWalletServicesConstants;
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
		final String merchantCode = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_MERCHANT_CODE);

		final String narration = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_NARRATION);

		final String[] parameters =
		{ merchantCode, "P", walletOrderId, narration, amount };//Purchase
		final String checksumKey = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_CHECKSUM);

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
		final String merchantCodeVerfication = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_MERCHANT_CODE);
		final String[] parameters =
		{ merchantCodeVerfication, "V", walletOrderId };//verification
		final String checksumKey = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_CHECKSUM);
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
		final String merchantCodeRefund = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_MERCHANT_CODE);

		final String narration = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_NARRATION);

		final String[] parameters =
		{ merchantCodeRefund, "R", mRupeeRefundRequest.getRefNo(), narration, mRupeeRefundRequest.getAmount().toString(),
				mRupeeRefundRequest.getPurchaseRefNo() };//Refund
		final String checksumKey = getConfigurationService().getConfiguration().getString(
				MarketplaceWalletServicesConstants.MRUPEE_CHECKSUM);

		final String Chesksum = MerchantChecksum.generateChecksum(parameters, checksumKey);
		LOG.debug("Checksum New:" + Chesksum);
		return Chesksum;

	}


	/**
	 * @return the configurationService
	 */
	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}
}
