/**
 *
 */
package com.tisl.mpl.facades.wallet;

import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.QCInitializationResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;


/**
 * @author Nirav Bhanushali
 *
 */
public interface MplWalletFacade
{

	public QCInitializationResponse getWalletInitilization();

	public void createWalletContainer();

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
