/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.order.OrderModel;

import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.pojo.request.PurchaseEGVRequest;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.request.QCCustomerPromotionRequest;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.request.QCRefundRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.PurchaseEGVResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCInitializationResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
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

	public PurchaseEGVResponse purchaseEgv(final PurchaseEGVRequest purchaseEGVRequest, final String transactionId);

	public void addEgvToWallet();

	public BalanceBucketWise getQCBucketBalance(String customerWalletId, String transactionId);

	public QCRedeeptionResponse getWalletRedeem(String customerWalletId, String transactionId, QCRedeemRequest qcRedeemRequest);

	public QCRedeeptionResponse getWalletRefundRedeem(String walletId, QCRefundRequest qcRefundRequest);

	public QCRedeeptionResponse addTULWalletCashBack(String walletId, QCCustomerPromotionRequest request);

	public QCRedeeptionResponse refundTULPromotionalCash(String walletId, String transactionId);

	public CustomerWalletDetailResponse getCustomerWallet(String customerWalletId, String transactionId);

	public RedimGiftCardResponse getAddEGVToWallet(String cardNumber, String cardPin, String transactionId,
			String customerWalletId);

	public WalletTransacationsList getWalletTransactionList(String cardNumber, String transactionId);

	public QCCustomerRegisterResponse createWalletContainer(QCCustomerRegisterRequest registerCustomerRequest,
			String transactionId);

	public QCRedeeptionResponse createPromotion(String walletId, QCCustomerPromotionRequest request);

	public QCRedeeptionResponse qcCredit(String walletId, QCCreditRequest request);
	
   public WalletCardApportionDetailModel getOrderFromWalletCardNumber(final String cardNumber);
   
	public CustomerWalletDetailResponse activateQCUserAccount(final String walletId,final String transactionId);
	
	public CustomerWalletDetailResponse deactivateQCUserAccount(final String walletId, final String transactionId);


	/**
	 * @param registerCustomerRequest
	 * @param walletId
	 * @param transactionId
	 * @return
	 */
	CustomerWalletDetailResponse updateCustomerWallet(QCCustomerRegisterRequest registerCustomerRequest, String walletId,
			String transactionId);

}
