/**
 *
 */
package com.tisl.mpl.facades.wallet.impl;

import javax.annotation.Resource;

import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
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
	public void getCustomerWallet()
	{
		getMplWalletServices().getCustomerWallet();

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

}
