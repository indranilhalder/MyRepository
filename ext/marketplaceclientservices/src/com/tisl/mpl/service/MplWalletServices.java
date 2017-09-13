/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCInitializationResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.WalletTransacationsList;


/**
 * @author TUL
 *
 */
public interface MplWalletServices
{

	public QCInitializationResponse walletInitilization();


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

	public String getRedimWallet(String cardNumber, String cardPin, String transactionId);

	public WalletTransacationsList getWalletTransactionList(String cardNumber, String transactionId);

	public QCCustomerRegisterResponse createWalletContainer(QCCustomerRegisterRequest registerCustomerRequest, String transactionId);

}
