/**
 *
 */
package com.tisl.mpl.facades.wallet.impl;

import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;

import javax.annotation.Resource;

import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.pojo.request.QCRedeemRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.QCInitializationResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.service.MplWalletServices;


/**
 * @author TUL
 *
 */
public class MplWalletFacadeImpl extends PersistentKeyGenerator implements MplWalletFacade
{


	@Resource(name = "mplWalletServices")
	private MplWalletServices mplWalletServices;



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
	public QCInitializationResponse getWalletInitilization()
	{

		return getMplWalletServices().walletInitilization();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.wallet.MplWalletFacade#createWalletContainer()
	 */
	@Override
	public void createWalletContainer()
	{
		getMplWalletServices().createWallet();

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
		return super.generate().toString();
	}

}
