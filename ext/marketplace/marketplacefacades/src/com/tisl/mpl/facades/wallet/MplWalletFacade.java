/**
 *
 */
package com.tisl.mpl.facades.wallet;

import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;


/**
 * @author Nirav Bhanushali
 *
 */
public interface MplWalletFacade
{

	public void getWalletInitilization();

	public QCCustomerRegisterResponse createWalletContainer(final QCCustomerRegisterRequest registerCustomerRequest);

	public void purchaseEGV();

	public void addEGVToWallet();

	public BalanceBucketWise getQCBucketBalance(final String customerWalletId);

	public QCRedeeptionResponse getWalletRedeem(String customerWalletId, QCRedeemRequest qcRedeemRequest);

	public void getWalletRefundRedeem();

	public void addTULWalletCashBack();

	public void refundTULPromotionalCash();

	public void getCustomerWallet();

	public String generateQCTransactionId();

}
