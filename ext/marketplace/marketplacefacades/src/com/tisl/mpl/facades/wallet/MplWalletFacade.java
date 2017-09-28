/**
 *
 */
package com.tisl.mpl.facades.wallet;

import com.tisl.mpl.pojo.request.QCCustomerPromotionRequest;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.request.QCRefundRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.pojo.response.WalletTransacationsList;


/**
 * @author TUL
 *
 */
public interface MplWalletFacade
{

	public void getWalletInitilization();

	public QCCustomerRegisterResponse createWalletContainer(final QCCustomerRegisterRequest registerCustomerRequest);

	public void addEGVToWallet();

	public BalanceBucketWise getQCBucketBalance(final String customerWalletId);

	public QCRedeeptionResponse getWalletRedeem(String customerWalletId, QCRedeemRequest qcRedeemRequest);

	public QCRedeeptionResponse getWalletRefundRedeem(String walletId, QCRefundRequest qcRefundRequest);

	public QCRedeeptionResponse addTULWalletCashBack(String walletId, QCCustomerPromotionRequest request);

	public QCRedeeptionResponse refundTULPromotionalCash(String walletId, String transactionId);

	public CustomerWalletDetailResponse getCustomerWallet(String customerWalletId);

	public String generateQCTransactionId();

	public RedimGiftCardResponse getAddEGVToWallet(String cardNumber, String cardPin);

	public WalletTransacationsList getWalletTransactionList();

	public QCRedeeptionResponse createPromotion(String walletId, QCCustomerPromotionRequest request);
}
