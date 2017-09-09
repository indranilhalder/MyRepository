/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;


/**
 * @author TUL
 *
 */
public interface MplWalletServices
{

	public void walletInitilization();


	public QCCustomerRegisterResponse registerCustomerWallet(final QCCustomerRegisterRequest registerCustomerRequest,
			final String transactionId);

	public void purchaseEgv();

	public void addEgvToWallet();

	public BalanceBucketWise getQCBucketBalance(String customerWalletId, String transactionId);

	public QCRedeeptionResponse getWalletRedeem(String customerWalletId, String transactionId, QCRedeemRequest qcRedeemRequest);

	public void getWalletRefundRedeem();

	public void addTULWalletCashBack();

	public void refundTULPromotionalCash();

	public void getCustomerWallet();


}
