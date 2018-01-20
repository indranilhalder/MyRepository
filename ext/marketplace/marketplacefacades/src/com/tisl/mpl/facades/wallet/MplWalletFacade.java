/**
 *
 */
package com.tisl.mpl.facades.wallet;

import de.hybris.platform.core.model.user.CustomerModel;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.facades.cms.data.WalletCreateData;
import com.tisl.mpl.pojo.request.QCCreditRequest;
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
	
	public QCRedeeptionResponse qcCredit(final String walletId, final QCCreditRequest request);
	
	public CustomerWalletDetailResponse activateQCUserAccount(String walletId);
	
	public CustomerWalletDetailResponse deactivateQCUserAccount(String walletId);
	
	public WalletCreateData getWalletCreateData();
	
	public OTPResponseData validateOTP(final String customerID, final String enteredOTPNumber);
	
	public void sendNotificationForWalletCreate(final CustomerModel customerModel, final String otPNumber, final String mobileNumber);
	
	public String generateOTP(final CustomerModel customerModel, final String mobileNumber);

}
