/**
 *
 */
package com.tisl.mpl.facades.wallet.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.pojo.response.WalletTransacationsList;
import com.tisl.mpl.pojo.response.WalletTransactions;
import com.tisl.mpl.pojo.response.WalletTrasacationsListData;
import com.tisl.mpl.service.MplWalletServices;
import com.tisl.mpl.pojo.request.Customer;

import de.hybris.platform.core.model.user.CustomerModel;

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
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#purchaseEGV()
	 */
	@Override
	public void purchaseEGV()
	{
		getMplWalletServices().purchaseEgv();

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
	public void getWalletRefundRedeem()
	{
		getMplWalletServices().getWalletRefundRedeem();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#addTULWalletCashBack()
	 */
	@Override
	public void addTULWalletCashBack()
	{

		getMplWalletServices().addTULWalletCashBack();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#refundTULPromotionalCash()
	 */
	@Override
	public void refundTULPromotionalCash()
	{

		getMplWalletServices().refundTULPromotionalCash();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#getCustomerWallet()
	 */
	@Override
	public CustomerWalletDetailResponse getCustomerWallet(String customerWalletId)
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
	public RedimGiftCardResponse getAddEGVToWallet(String cardNumber, String cardPin){
		final String transactionId = generateQCTransactionId();
		RedimGiftCardResponse balance = new RedimGiftCardResponse ();
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if (null != currentCustomer.getIsWalletActivated()){
			 balance = getMplWalletServices().getAddEGVToWallet(cardNumber, cardPin,transactionId,currentCustomer.getCustomerWalletDetail().getWalletId());
			 if(null != balance){				 
				 return balance;
			 }
			 
		 }
		  balance.setResponseMessage("error");
		 return balance;
		
	}
	
	@Override
	public List<WalletTrasacationsListData> getWalletTransactionList(){
		List<WalletTrasacationsListData> walletTrasacationsListDataList=new ArrayList<WalletTrasacationsListData>();
		WalletTransacationsList walletTransacationsList = null;
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		if (null != currentCustomer && null != currentCustomer.getIsWalletActivated()){
			System.out.println("Customer has Actived try to redim the card");
			if(null != currentCustomer.getCustomerWalletDetail() && null!= currentCustomer.getCustomerWalletDetail().getWalletId()){
				final String transactionId = generateQCTransactionId();
			walletTransacationsList =getMplWalletServices().getWalletTransactionList(currentCustomer.getCustomerWalletDetail().getWalletId(),transactionId);
			}
		 }

		if(null != walletTransacationsList && null != walletTransacationsList.getWalletTransactions()){
		for(WalletTransactions trasaction :walletTransacationsList.getWalletTransactions()){
			WalletTrasacationsListData data = new WalletTrasacationsListData();
			data.setWalletNumber(trasaction.getWalletNumber());
			data.setInvoiceNumber(trasaction.getInvoiceNumber());
			data.setDateAtServer(trasaction.getDateAtServer());
			data.setBatchNumber(trasaction.getBatchNumber());
			data.setAmount(trasaction.getAmount());
			data.setBalance(trasaction.getBalance());
			data.setBillAmount(trasaction.getBillAmount());
			data.setMerchantOutletName(trasaction.getMerchantOutletName());
			data.setTransactionPostDate(trasaction.getTransactionPostDate());
			data.setTransactionStatus(trasaction.getTransactionStatus());
			data.setUser(trasaction.getUser());
			data.setMerchantName(trasaction.getMerchantName());
			data.setpOSName(trasaction.getpOSName());
			data.setCustomerName(trasaction.getCustomerName());
			data.setWalletPIN(trasaction.getWalletPIN());
			data.setNotes(trasaction.getNotes());
			data.setApprovalCode(trasaction.getApprovalCode());
			data.setResponseCode(trasaction.getResponseCode());
			data.setResponseMessage(trasaction.getResponseMessage());
			data.setTransactionId(trasaction.getTransactionId());
			data.setTransactionType(trasaction.getTransactionType());
			data.setErrorCode(trasaction.getErrorCode());
			data.setErrorDescription(trasaction.getErrorDescription());
			
			walletTrasacationsListDataList.add(data);
		}
		}
		return walletTrasacationsListDataList;
	}
	
	@Override
	public List<WalletTrasacationsListData> getCashBackWalletTrasacationsList(List<WalletTrasacationsListData> walletTrasacationsListData,String transactionType){
		List<WalletTrasacationsListData> transactionList = new ArrayList<WalletTrasacationsListData>();
		for(WalletTrasacationsListData dataObject :walletTrasacationsListData){
			if(dataObject.getTransactionType().equalsIgnoreCase(transactionType)){
				WalletTrasacationsListData data = new WalletTrasacationsListData();
				data.setWalletNumber(dataObject.getWalletNumber());
				data.setInvoiceNumber(dataObject.getInvoiceNumber());
				data.setDateAtServer(dataObject.getDateAtServer());
				data.setBatchNumber(dataObject.getBatchNumber());
				data.setAmount(dataObject.getAmount());
				data.setBalance(dataObject.getBalance());
				data.setBillAmount(dataObject.getBillAmount());
				data.setMerchantOutletName(dataObject.getMerchantOutletName());
				data.setTransactionPostDate(dataObject.getTransactionPostDate());
				data.setTransactionStatus(dataObject.getTransactionStatus());
				data.setUser(dataObject.getUser());
				data.setMerchantName(dataObject.getMerchantName());
				data.setpOSName(dataObject.getpOSName());
				data.setCustomerName(dataObject.getCustomerName());
				data.setWalletPIN(dataObject.getWalletPIN());
				data.setNotes(dataObject.getNotes());
				data.setApprovalCode(dataObject.getApprovalCode());
				data.setResponseCode(dataObject.getResponseCode());
				data.setResponseMessage(dataObject.getResponseMessage());
				data.setTransactionId(dataObject.getTransactionId());
				data.setTransactionType(dataObject.getTransactionType());
				data.setErrorCode(dataObject.getErrorCode());
				data.setErrorDescription(dataObject.getErrorDescription());
				transactionList.add(data);
			}
		}
		
		return transactionList;
	}

}
