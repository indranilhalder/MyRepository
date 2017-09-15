/**
 *
 */
package com.tisl.mpl.facades.wallet;

import java.util.List;

import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.WalletTrasacationsListData;


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

	public CustomerWalletDetailResponse getCustomerWallet(String customerWalletId);

	public String generateQCTransactionId();

	public String getRedimWallet(String cardNumber, String cardPin);

	public List<WalletTrasacationsListData> getWalletTransactionList();

	public List<WalletTrasacationsListData> getCashBackWalletTrasacationsList(
			List<WalletTrasacationsListData> walletTrasacationsListData, String transactionType);
	

}
