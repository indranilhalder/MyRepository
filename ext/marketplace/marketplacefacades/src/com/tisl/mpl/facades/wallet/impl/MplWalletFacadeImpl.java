/**
 *
 */
package com.tisl.mpl.facades.wallet.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
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
import com.tisl.mpl.service.MplWalletServices;


/**
 * @author TUL
 *
 */
public class MplWalletFacadeImpl implements MplWalletFacade
{


	@Resource(name = "mplWalletServices")
	private MplWalletServices mplWalletServices;

	@Resource
	private MplPaymentService mplPaymentService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	/**
	 * @return the mplPaymentService
	 */
	public MplPaymentService getMplPaymentService()
	{
		return mplPaymentService;
	}



	/**
	 * @param mplPaymentService
	 *           the mplPaymentService to set
	 */
	public void setMplPaymentService(final MplPaymentService mplPaymentService)
	{
		this.mplPaymentService = mplPaymentService;
	}



	/**
	 * @return the mplWalletServices
	 */
	public MplWalletServices getMplWalletServices()
	{
		return mplWalletServices;
	}



	/**
	 * @param mplWalletServices
	 *           the mplWalletServices to set
	 */
	public void setMplWalletServices(final MplWalletServices mplWalletServices)
	{
		this.mplWalletServices = mplWalletServices;
	}



	@Override
	public void getWalletInitilization()
	{

		getMplWalletServices().walletInitilization();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#createWalletContainer()
	 */
	@Override
	public QCCustomerRegisterResponse createWalletContainer(final QCCustomerRegisterRequest registerCustomerRequest)
	{
		return getMplWalletServices().registerCustomerWallet(registerCustomerRequest, generateQCTransactionId());

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#addEGVToWallet()
	 */
	@Override
	public void addEGVToWallet()
	{

		getMplWalletServices().addEgvToWallet();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getWalletBalance()
	 */
	@Override
	public BalanceBucketWise getQCBucketBalance(final String customerWalletId)

	{

		return getMplWalletServices().getQCBucketBalance(customerWalletId, generateQCTransactionId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getWalletRefundRedeem()
	 */
	@Override
	public QCRedeeptionResponse  getWalletRefundRedeem(String walletId, QCRefundRequest qcRefundRequest)
	{
		return getMplWalletServices().getWalletRefundRedeem(walletId,qcRefundRequest);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#addTULWalletCashBack()
	 */
	@Override
	public QCRedeeptionResponse addTULWalletCashBack(final String walletId, final QCCustomerPromotionRequest request)
	{
		final String transactionId = generateQCTransactionId();
		request.setInvoiceNumber(transactionId);
		return getMplWalletServices().addTULWalletCashBack(walletId, request);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#refundTULPromotionalCash()
	 */
	@Override
	public QCRedeeptionResponse refundTULPromotionalCash(final String walletId, final String transactionId)
	{

		return getMplWalletServices().refundTULPromotionalCash(walletId, transactionId);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getCustomerWallet()
	 */
	@Override
	public CustomerWalletDetailResponse getCustomerWallet(final String customerWalletId)
	{
		return getMplWalletServices().getCustomerWallet(customerWalletId, generateQCTransactionId());

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getWalletRedeem(java.lang.String, java.lang.String,
	 * com.tisl.mpl.pojo.request.QCRedeemRequest)
	 */
	@Override
	public QCRedeeptionResponse getWalletRedeem(final String customerWalletId, final QCRedeemRequest qcRedeemRequest)
	{

		final String transactionId = generateQCTransactionId();

		return getMplWalletServices().getWalletRedeem(customerWalletId, transactionId, qcRedeemRequest);
	}

	@Override
	public String generateQCTransactionId()
	{
		return getMplPaymentService().createQCPaymentId();
	}

	@Override
	public RedimGiftCardResponse getAddEGVToWallet(final String cardNumber, final String cardPin)
	{
		final String transactionId = generateQCTransactionId();
		RedimGiftCardResponse balance = new RedimGiftCardResponse();
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if (null != currentCustomer.getIsWalletActivated())
		{
			balance = getMplWalletServices().getAddEGVToWallet(cardNumber, cardPin, transactionId,
					currentCustomer.getCustomerWalletDetail().getWalletId());
			if (null != balance)
			{
				return balance;
			}

		}
		balance.setResponseMessage("error");
		return balance;

	}

	@Override
	public WalletTransacationsList getWalletTransactionList()
	{
		//List<WalletTrasacationsListData> walletTrasacationsListDataList=new ArrayList<WalletTrasacationsListData>();
		WalletTransacationsList walletTransacationsList = null;
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		//		if (null != currentCustomer && null != currentCustomer.getIsWalletActivated()){
		//			System.out.println("Customer has Actived try to redim the card");
		//			if(null != currentCustomer.getCustomerWalletDetail() && null!= currentCustomer.getCustomerWalletDetail().getWalletId()){
		final String transactionId = generateQCTransactionId();
		walletTransacationsList = getMplWalletServices()
				.getWalletTransactionList(currentCustomer.getCustomerWalletDetail().getWalletId(), transactionId);
		//			}
		//		 }

		if (null != walletTransacationsList)
		{
			//		for(WalletTransactions trasaction :walletTransacationsList.getWalletTransactions()){
			//			WalletTrasacationsListData data = new WalletTrasacationsListData();
			//			data.setWalletNumber(trasaction.getWalletNumber());
			//			data.setInvoiceNumber(trasaction.getInvoiceNumber());
			//			data.setDateAtServer(trasaction.getDateAtServer());
			//			data.setBatchNumber(trasaction.getBatchNumber());
			//			data.setAmount(trasaction.getAmount());
			//			data.setBalance(trasaction.getBalance());
			//			data.setBillAmount(trasaction.getBillAmount());
			//			data.setMerchantOutletName(trasaction.getMerchantOutletName());
			//			data.setTransactionPostDate(trasaction.getTransactionPostDate());
			//			data.setTransactionStatus(trasaction.getTransactionStatus());
			//			data.setUser(trasaction.getUser());
			//			data.setMerchantName(trasaction.getMerchantName());
			//			data.setpOSName(trasaction.getpOSName());
			//			data.setCustomerName(trasaction.getCustomerName());
			//			data.setWalletPIN(trasaction.getWalletPIN());
			//			data.setNotes(trasaction.getNotes());
			//			data.setApprovalCode(trasaction.getApprovalCode());
			//			data.setResponseCode(trasaction.getResponseCode());
			//			data.setResponseMessage(trasaction.getResponseMessage());
			//			data.setTransactionId(trasaction.getTransactionId());
			//			data.setTransactionType(trasaction.getTransactionType());
			//			data.setErrorCode(trasaction.getErrorCode());
			//			data.setErrorDescription(trasaction.getErrorDescription());
			//			
			//			walletTrasacationsListDataList.add(data);
			//		  }

			return walletTransacationsList;
		}
		return (WalletTransacationsList) CollectionUtils.EMPTY_COLLECTION;
	}



	@Override
	public QCRedeeptionResponse createPromotion(final String walletId, final QCCustomerPromotionRequest request)
	{
		final String transactionId = generateQCTransactionId();
		request.setInvoiceNumber(transactionId);
		return getMplWalletServices().createPromotion(walletId, request);
	}

}
