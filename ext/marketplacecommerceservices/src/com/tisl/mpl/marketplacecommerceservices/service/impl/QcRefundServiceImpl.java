package com.tisl.mpl.marketplacecommerceservices.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.pmd.util.CollectionUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.QcRefundService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.response.QCCard;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.service.MplWalletServices;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;


public class QcRefundServiceImpl implements  QcRefundService
{
	private final static Logger LOG = Logger.getLogger(QcRefundServiceImpl.class.getName());


	@Autowired
	private  MplWalletServices mplWalletService;
	
	@Autowired
	private MplPaymentService mplPaymentService;
	
	@Autowired
	private ModelService modelService;
	
	/**
	 * This method is used to process the refund of QC ..
	 */
	@Override
	public void processQcRefundEntryWise(AbstractOrderEntryModel abstractOrderEntryModel)
	{
		QCRedeeptionResponse response = null;
		final DecimalFormat decimalFormat = new DecimalFormat("#.00");

		final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();

		if (null != abstractOrderEntryModel && null != abstractOrderEntryModel.getWalletApportionPaymentInfo())
		{
			for (final WalletCardApportionDetailModel cardApportionDetail : abstractOrderEntryModel
					.getWalletApportionPaymentInfo().getWalletCardList())
			{
				double qcCliqCashAmt = 0.0D;
				if (null != cardApportionDetail && null != cardApportionDetail.getBucketType())
				{
					if (!cardApportionDetail.getBucketType().equalsIgnoreCase("CASHBACK"))
					{
						qcCliqCashAmt = Double.parseDouble(cardApportionDetail.getQcApportionValue())
								+ Double.parseDouble(null != cardApportionDetail.getQcDeliveryValue() ? cardApportionDetail
										.getQcDeliveryValue() : "" + 0)
								+ Double.parseDouble(null != cardApportionDetail.getQcSchedulingValue() ? cardApportionDetail
										.getQcSchedulingValue() : "" + 0)
								+ Double.parseDouble(null != cardApportionDetail.getQcShippingValue() ? cardApportionDetail
										.getQcShippingValue() : "" + 0);
						String walletId = null;
						CustomerModel customerModel = (CustomerModel) abstractOrderEntryModel.getOrder().getUser();
						if (null != customerModel && null != customerModel.getCustomerWalletDetail())
						{
							walletId = customerModel.getCustomerWalletDetail().getWalletId();
						}
						final QCCreditRequest qcCreditRequest = new QCCreditRequest();
						qcCreditRequest.setAmount(decimalFormat.format(qcCliqCashAmt));
						qcCreditRequest.setInvoiceNumber(mplPaymentService.createQCPaymentId());
						qcCreditRequest.setNotes("Cancel for " + decimalFormat.format(qcCliqCashAmt));
						response = mplWalletService.qcCredit(walletId, qcCreditRequest);
						walletCardApportionDetailModelList.add(getQcWalletCardResponse(response, cardApportionDetail));
					}
				}
			}
//			WalletApportionReturnInfoModel returnModel = null;
//			returnModel = 
			if(CollectionUtils.isNotEmpty(walletCardApportionDetailModelList)) {
				constructQuickCilverOrderEntryForSplit(walletCardApportionDetailModelList,
						abstractOrderEntryModel);
			}
		
	//		saveQCandJuspayResponse(abstractOrderEntryModel, returnModel);
		}
		
	}
	

	private WalletCardApportionDetailModel getQcWalletCardResponse(final QCRedeeptionResponse response,
			final WalletCardApportionDetailModel walletObject)
	{
		final WalletCardApportionDetailModel walletCardApportionDetailModel = new WalletCardApportionDetailModel();
		if (null != response && null != response.getCards())
		{
			for (final QCCard qcCard : response.getCards())
			{
				walletCardApportionDetailModel.setCardNumber(qcCard.getCardNumber());
				walletCardApportionDetailModel.setCardExpiry(qcCard.getExpiry());
				walletCardApportionDetailModel.setCardAmount(qcCard.getAmount().toString());
				walletCardApportionDetailModel.setBucketType(qcCard.getBucketType());
			}
			if (StringUtils.equalsIgnoreCase(response.getResponseCode().toString(), "0"))
			{
				walletCardApportionDetailModel.setTrnsStatus("SUCCESS");
			}
			else
			{
				walletCardApportionDetailModel.setTrnsStatus("PENDING");
			}
			walletCardApportionDetailModel.setTransactionId(response.getTransactionId().toString());
			walletCardApportionDetailModel.setQcApportionValue(walletObject.getQcApportionValue());
			walletCardApportionDetailModel.setQcDeliveryValue(walletObject.getQcDeliveryValue());
			walletCardApportionDetailModel.setQcSchedulingValue(walletObject.getQcSchedulingValue());
			walletCardApportionDetailModel.setQcShippingValue(walletObject.getQcShippingValue());
		}

		return walletCardApportionDetailModel;
	}

	@Override
	public  void constructQuickCilverOrderEntryForSplit(
			final List<WalletCardApportionDetailModel> walletCardApportionDetailModelList, final AbstractOrderEntryModel abstractOrderEntryModel
		)
	{
		final List<WalletCardApportionDetailModel> walletCardApportionDetailList = new ArrayList<WalletCardApportionDetailModel>();
		final WalletApportionReturnInfoModel walletApportionReturnModel = modelService.create(
				WalletApportionReturnInfoModel.class);
		final List<String> qcResponseStatus = new ArrayList<String>();
		if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
				&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue())
		{
			walletApportionReturnModel.setQcApportionPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
					.getQcApportionPartValue());
		}
		if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
				&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue())
		{
			walletApportionReturnModel.setQcDeliveryPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
					.getQcDeliveryPartValue());
		}
		if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
				&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue())
		{
			walletApportionReturnModel.setQcSchedulingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
					.getQcSchedulingPartValue());
		}
		if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
				&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue())
		{
			walletApportionReturnModel.setQcShippingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo()
					.getQcShippingPartValue());
		}

		if (null != walletCardApportionDetailModelList && walletCardApportionDetailModelList.size() > 0)
		{

			for (final WalletCardApportionDetailModel walletCardApportionDetailModelObj : walletCardApportionDetailModelList)
			{
				walletCardApportionDetailList.add(walletCardApportionDetailModelObj);
				qcResponseStatus.add(walletCardApportionDetailModelObj.getTrnsStatus());
				createPaymentEntryForQCTransaction((OrderModel)abstractOrderEntryModel.getOrder(), walletCardApportionDetailModelObj);
				// modelService.save(walletCardApportionDetailModel);
			}
		}
		walletApportionReturnModel.setWalletCardList(walletCardApportionDetailList);
		walletApportionReturnModel.setTransactionId(abstractOrderEntryModel.getTransactionID());
		walletApportionReturnModel.setType("CANCEL");
		if (qcResponseStatus.contains("PENDING"))
		{
			walletApportionReturnModel.setStatus("PENDING");
		}
		else
		{
			walletApportionReturnModel.setStatus("SUCCESS");
		}
		
		LOG.debug("Before Saving Juspay Response is :" + walletApportionReturnModel.getJuspayApportionValue());
		modelService.save(walletApportionReturnModel);
		LOG.debug("After Saving Juspay Response is :" + walletApportionReturnModel.getJuspayApportionValue());

		abstractOrderEntryModel.setWalletApportionReturnInfo(walletApportionReturnModel);
		LOG.debug("Before setting  Order Entry Response is :" + walletApportionReturnModel.getJuspayApportionValue());
		modelService.save(abstractOrderEntryModel);
		LOG.debug("After setting  Order Entry Response is :" + walletApportionReturnModel.getJuspayApportionValue());
	}

	@Override
	public void createPaymentEntryForQCTransaction(final OrderModel subOrderModel,
			final WalletCardApportionDetailModel walletCardApportionDetailModel)
	{
     if(null != subOrderModel && null != walletCardApportionDetailModel) {
  		try {
  			LOG.debug("Inside createPaymentEntryForQCTransaction for  order "+subOrderModel.getCode() );
			final PaymentTransactionModel paymentTransactionModel = modelService.create(PaymentTransactionModel.class);
			final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService.create(PaymentTransactionEntryModel.class);

			if(null != walletCardApportionDetailModel.getTransactionId()) {
				paymentTransactionModel.setCode(walletCardApportionDetailModel.getTransactionId().toString());
				paymentTransactionModel.setRequestId(walletCardApportionDetailModel.getTransactionId().toString());
				paymentTransactionEntryModel.setCode(walletCardApportionDetailModel.getTransactionId().toString());
				paymentTransactionEntryModel.setRequestId(walletCardApportionDetailModel.getTransactionId().toString());
			}
			paymentTransactionModel.setStatus(walletCardApportionDetailModel.getTrnsStatus());
			paymentTransactionModel.setOrder(subOrderModel);
			final BigDecimal bigAmount = new BigDecimal(walletCardApportionDetailModel.getCardAmount(), MathContext.DECIMAL64);
			paymentTransactionEntryModel.setAmount(bigAmount);
			paymentTransactionEntryModel.setTime(new Date());
			paymentTransactionEntryModel.setCurrency(subOrderModel.getCurrency());
			paymentTransactionEntryModel.setTransactionStatus(walletCardApportionDetailModel.getTrnsStatus());
			paymentTransactionEntryModel.setTransactionStatusDetails(walletCardApportionDetailModel.getTrnsStatus());
			PaymentTypeModel model = new PaymentTypeModel();
			if(null != subOrderModel.getPaymentTransactions() && !subOrderModel.getPaymentTransactions().isEmpty() ) {
				for (final PaymentTransactionModel paymentTransaction : subOrderModel.getPaymentTransactions())
				{
					for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
					{
						if ("success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus())
								|| "ACCEPTED".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus()))
						{
							model = paymentTransactionEntry.getPaymentMode();
						}
					}
				}
			}
			
			paymentTransactionEntryModel.setPaymentMode(model);
			paymentTransactionEntryModel.setType(PaymentTransactionType.CANCEL);
			modelService.save(paymentTransactionEntryModel);
			final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
			entries.add(paymentTransactionEntryModel);
			paymentTransactionModel.setEntries(entries);
			modelService.save(paymentTransactionModel);
			LOG.debug("Payment Transaction created SuccessFully......:");

		}catch(Exception e) {
			LOG.error("Exception occurred while  createPaymentEntryForQCTransaction  for order "+subOrderModel.getCode()+" "+e.getMessage(),e);
		}
     }

		
	}
	
}
