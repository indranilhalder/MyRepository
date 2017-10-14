
/**
 * @author Techouts
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.marketplacecommerceservices.daos.JuspayWebHookDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplQcPaymentFailService;
import com.tisl.mpl.pojo.request.QCCreditRequest;
import com.tisl.mpl.pojo.response.QCCard;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.promotion.dao.MplQcPaymentFailDao;
import com.tisl.mpl.service.MplWalletServices;
import com.tisl.mpl.util.GenericUtilityMethods;


public class MplQcPaymentFailServiceImpl implements MplQcPaymentFailService
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplQcPaymentFailServiceImpl.class.getName());

	//	@Autowired
	//	private ModelService modelService;
	@Autowired
	private MplQcPaymentFailDao mplQcPaymentFailDao;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplOrderDao mplOrderDao;

	@Autowired
	private MplProcessOrderDao mplProcessOrderDao;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;


	@Autowired
	private ModelService modelService;

	@Autowired
	private MplWalletServices mplWalletService;


	//@Autowired
	//private CancelReturnFacade cancelReturnFacade; 








	/**
	 *
	 * The Method is used to generate the .csv file data For Failed Refunds Of QC
	 *
	 */
	@Override
	public void generateData()
	{
		final List<WalletApportionReturnInfoModel> walletInfoList = mplQcPaymentFailDao.getPendingQcPayments();
		if (CollectionUtils.isNotEmpty(walletInfoList))
		{
			LOG.debug("The Required QcPaymentFail Data Fetched");
			final List<CilqCashWalletPojo> failDataList = populateDataList(walletInfoList);

			if (CollectionUtils.isNotEmpty(failDataList))
			{
				generateCSV(failDataList);
			}
		}
	}


	private List<CilqCashWalletPojo> populateDataList(final List<WalletApportionReturnInfoModel> qcPaymentFailList)
	{
		final List<CilqCashWalletPojo> cliqCashList = new ArrayList<CilqCashWalletPojo>();

		for (final WalletApportionReturnInfoModel walletInfo : qcPaymentFailList)
		{
			final CilqCashWalletPojo data = new CilqCashWalletPojo();

			final OrderModel order = mplQcPaymentFailDao.getOrder(walletInfo.getOrderId());
			final CustomerModel customer = (CustomerModel) order.getUser();
			double refundAmount = 0.0D;
			if (null != walletInfo.getType()
					&& walletInfo.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.QC_REFUND_TYPE_CANCEL))
			{
				double qcApportionAmount = getQcApportionValue(walletInfo.getQcApportionPartValue());
				if (qcApportionAmount > 0.0D)
				{
					refundAmount += qcApportionAmount;
				}

				double qcDeliveryPartAmount = getQcDeliveryPartValue(walletInfo.getQcDeliveryPartValue());
				if (qcDeliveryPartAmount > 0.0D)
				{
					refundAmount += qcDeliveryPartAmount;
				}

				double qcSchedulingPartAmount = getQcSchedulingPartValue(walletInfo.getQcSchedulingPartValue());
				if (qcSchedulingPartAmount > 0.0D)
				{
					refundAmount += qcSchedulingPartAmount;
				}

				double qcShippingPartAmount = getQcApportionValue(walletInfo.getQcShippingPartValue());
				if (qcShippingPartAmount > 0.0D)
				{
					refundAmount += qcShippingPartAmount;
				}

			}
			else if (null != walletInfo.getType()
					&& walletInfo.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.QC_REFUND_TYPE_RETURN))
			{
				double qcApportionAmount = getQcApportionValue(walletInfo.getQcApportionPartValue());
				if (qcApportionAmount > 0.0D)
				{
					refundAmount += qcApportionAmount;
				}

				double qcDeliveryPartAmount = getQcDeliveryPartValue(walletInfo.getQcDeliveryPartValue());
				if (qcDeliveryPartAmount > 0.0D)
				{
					refundAmount += qcDeliveryPartAmount;
				}

				/*
				 * double qcSchedulingPartAmount = getQcSchedulingPartValue(walletInfo.getQcSchedulingPartValue());
				 * if(qcSchedulingPartAmount > 0.0D) { refundAmount+=qcSchedulingPartAmount; }
				 * 
				 * double qcShippingPartAmount = getQcApportionValue(walletInfo.getQcShippingPartValue());
				 * if(qcShippingPartAmount > 0.0D) { refundAmount+=qcShippingPartAmount; }
				 */
			}
			else if (null != walletInfo.getType()
					&& walletInfo.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.QC_REFUND_TYPE_REFUND))
			{
				double qcApportionAmount = getQcApportionValue(walletInfo.getQcApportionPartValue());
				if (qcApportionAmount > 0.0D)
				{
					refundAmount += qcApportionAmount;
				}

				double qcDeliveryPartAmount = getQcDeliveryPartValue(walletInfo.getQcDeliveryPartValue());
				if (qcDeliveryPartAmount > 0.0D)
				{
					refundAmount += qcDeliveryPartAmount;
				}

				double qcSchedulingPartAmount = getQcSchedulingPartValue(walletInfo.getQcSchedulingPartValue());
				if (qcSchedulingPartAmount > 0.0D)
				{
					refundAmount += qcSchedulingPartAmount;
				}

				double qcShippingPartAmount = getQcApportionValue(walletInfo.getQcShippingPartValue());
				if (qcShippingPartAmount > 0.0D)
				{
					refundAmount += qcShippingPartAmount;
				}
			}
			if (refundAmount > 0.0D)
			{
				data.setAmount(String.valueOf(refundAmount));
			}
			if (null != customer && null != customer.getOriginalUid())
			{
				data.setCustomerEmailId(customer.getOriginalUid());
			}
			if (refundAmount > 0.0D)
			{
				data.setAmount(String.valueOf(refundAmount));
			}
			data.setBucketName(MarketplacecommerceservicesConstants.BUCKET_NAME_PROMOTON);
			cliqCashList.add(data);
		}
		return cliqCashList;
	}


	/**
	 * @param qcApportionPartValue
	 */
	private double getQcApportionValue(String qcApportionPartValue)
	{
		double amount = 0.0D;
		{
			if (null != qcApportionPartValue)
			{
				Double qcApportionValue = Double.valueOf(qcApportionPartValue);
				if (null != qcApportionValue && qcApportionValue.doubleValue() > 0.0D)
				{
					amount = qcApportionValue.doubleValue();
				}
			}
		}
		return amount;
	}

	private double getQcDeliveryPartValue(String qcDeliveryPartValue)
	{
		double amount = 0.0D;
		{
			if (null != qcDeliveryPartValue)
			{
				Double qcDeliveryValue = Double.valueOf(qcDeliveryPartValue);
				if (null != qcDeliveryValue && qcDeliveryValue.doubleValue() > 0.0D)
				{
					amount = qcDeliveryValue.doubleValue();
				}
			}
		}
		return amount;
	}

	private double getQcSchedulingPartValue(String qcSchedulingPartValue)
	{
		double amount = 0.0D;
		{
			if (null != qcSchedulingPartValue)
			{
				Double qcSchedulingValue = Double.valueOf(qcSchedulingPartValue);
				if (null != qcSchedulingValue && qcSchedulingValue.doubleValue() > 0.0D)
				{
					amount = qcSchedulingValue.doubleValue();
				}
			}
		}
		return amount;
	}




	private void generateCSV(final List<CilqCashWalletPojo> binDataList)
	{
		FileWriter fileWriter = null;

		String datePrefix = MarketplacecommerceservicesConstants.EMPTYSPACE;
		if (null != GenericUtilityMethods.convertSysDateToString(new Date()))
		{
			datePrefix = GenericUtilityMethods.convertSysDateToString(new Date());
		}

		final File rootFolderOfQcpayment = new File(
				configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.QC_PAYMENT_FAIL_FILE_LOCATION,
						MarketplacecommerceservicesConstants.QC_PAYMENT_FAIL_PATH),
				MarketplacecommerceservicesConstants.QC_PAYMENT_FAIL_NAME + datePrefix
						+ configurationService.getConfiguration().getString("cronjob.campaign.extension", ".csv"));

		try
		{
			fileWriter = new FileWriter(rootFolderOfQcpayment);
			fileWriter.append(MarketplacecommerceservicesConstants.QC_PAYMENT_FAIL_HEADER);

			fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);

			for (final CilqCashWalletPojo data : binDataList)
			{
				fileWriter.append(data.getCustomerName());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getCustomerEmailId());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getAmount());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(data.getBucketName());
				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_DELIMITTER);

				fileWriter.append(MarketplacecommerceservicesConstants.CAMPAIGN_FILE_NEW_LINE_SEPARATOR);
			}
		}
		catch (final IOException exception)
		{
			LOG.error("IO Exception", exception);
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}

		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException exception)
			{
				LOG.error("Error while flushing/closing fileWriter !!!" + exception.getMessage());
			}
		}


		//		if (rootFolderOfBinError.exists())
		//		{
		//			LOG.error("**************Removing all Data post Report Generation********************");
		//			modelService.removeAll(binErrorList);
		//			LOG.error("**************Removing all Data post Report Generation Succcessful********************");
		//		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplQcPaymentFailService#processQcPaymentFailedOrders()
	 */
	@Override
	public void processQcPaymentFailedOrders() throws EtailNonBusinessExceptions
	{
		List<OrderModel> orders = new ArrayList<OrderModel>();
		orders = mplQcPaymentFailDao.getRmsVerificationFailedOrders(OrderStatus.RMS_VERIFICATION_FAILED.toString());

		if (CollectionUtils.isNotEmpty(orders))
		{
			//getting list of Juspay req ids for Payment Pending orders
			for (final OrderModel orderModel : orders)
			{
				String paymentSplitMode = orderModel.getSplitModeInfo();
				if (null != paymentSplitMode)
				{
					if (paymentSplitMode.trim().equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_MODE_SPLIT.trim()))
					{

						// Refunding Amount Paid Through Juspay 
						try {
							processJuspayRefund(orderModel);
						}catch (Exception e) {
							LOG.error("Exception occurred while while Refunding Juspay AMount"+e.getMessage());
						}

						// Refunding Amount Paid Through QC 
						try {
						processQcRefund(orderModel);
						}catch (Exception e) {
							LOG.error("Exception occurred while while Refunding Qc AMount"+e.getMessage());
						}
					}else if(paymentSplitMode.trim().equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_MODE_LIQ_CASH.trim())){
						
						try {
						processQcRefund(orderModel);
						}catch (Exception e) {
							LOG.error("Exception occurred while while Refunding Qc  AMount"+e.getMessage());
						}
					}
				}
			}
		}

	}


	/**
	 * @param orderModel
	 */
	private void processQcRefund(OrderModel orderModel)
	{
		double amount = 0.0D;
		for (AbstractOrderEntryModel entry : orderModel.getEntries())
		{
			double entryAmount = calculateSplitQcRefundAmount(entry);
			amount += entryAmount;
		}

		String walletId = null;
		DecimalFormat daecimalFormat = new DecimalFormat("#.00");
		CustomerModel customerModel = (CustomerModel) orderModel.getUser();
		if (null != customerModel && null != customerModel.getCustomerWalletDetail())
		{
			walletId = customerModel.getCustomerWalletDetail().getWalletId();
		}

		QCCreditRequest qcCreditRequest = new QCCreditRequest();
		qcCreditRequest.setAmount(daecimalFormat.format(amount));
		qcCreditRequest.setInvoiceNumber(orderModel.getCode());
		qcCreditRequest.setNotes("Cancel for " + daecimalFormat.format(amount));
		QCRedeeptionResponse response = mplWalletService.qcCredit(walletId, qcCreditRequest);
		if(null != response ) {
			LOG.debug("*****************************" + response.getResponseCode());

			for (AbstractOrderEntryModel entry : orderModel.getEntries())
			{
				constructQuickCilverOrderEntry(response, entry);
			}
		}
		

	}


	private double calculateSplitQcRefundAmount(AbstractOrderEntryModel orderEntry)
	{
		double refundAmountForQc = 0.0D;
		double cashBackAmt = 0;
		if (null != orderEntry && null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getWalletCardList())
		{
			for (WalletCardApportionDetailModel cardApportionDetail : orderEntry.getWalletApportionPaymentInfo().getWalletCardList())
			{
				if (null != cardApportionDetail.getBucketType() && cardApportionDetail.getBucketType().equalsIgnoreCase("CASHBACK"))
				{
					cashBackAmt += Double.parseDouble(cardApportionDetail.getQcApportionValue())
							+ Double.parseDouble(
									null != cardApportionDetail.getQcDeliveryValue() ? cardApportionDetail.getQcDeliveryValue() : "" + 0)
							+ Double.parseDouble(null != cardApportionDetail.getQcSchedulingValue()
									? cardApportionDetail.getQcSchedulingValue() : "" + 0)
							+ Double.parseDouble(
									null != cardApportionDetail.getQcShippingValue() ? cardApportionDetail.getQcShippingValue() : "" + 0);
				}
			}
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcApportionPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcApportionPartValue()).doubleValue();
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcDeliveryPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcDeliveryPartValue()).doubleValue();
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcSchedulingPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcSchedulingPartValue()).doubleValue();
		}
		if (null != orderEntry.getWalletApportionPaymentInfo()
				&& null != orderEntry.getWalletApportionPaymentInfo().getQcShippingPartValue())
		{
			refundAmountForQc += Double.valueOf(orderEntry.getWalletApportionPaymentInfo().getQcShippingPartValue()).doubleValue();
		}


		if (cashBackAmt > 0)
		{

			refundAmountForQc -= cashBackAmt;
		}

		return refundAmountForQc;
	}


	private void constructQuickCilverOrderEntry(final QCRedeeptionResponse response,
			AbstractOrderEntryModel abstractOrderEntryModel)
	{


		final WalletApportionReturnInfoModel walletApportionModel = modelService.create(WalletApportionReturnInfoModel.class);
		List<WalletCardApportionDetailModel> walletCardApportionDetailModelList = new ArrayList<WalletCardApportionDetailModel>();
		if (null != response /*&& null != response.getCards()*/)
		{
			for (QCCard qcCard : response.getCards())
			{
				final WalletCardApportionDetailModel model = modelService.create(WalletCardApportionDetailModel.class);
				model.setCardNumber(qcCard.getCardNumber());
				model.setCardExpiry(qcCard.getExpiry());
				model.setCardAmount(qcCard.getAmount().toString());
				model.setBucketType(qcCard.getBucketType());
				walletCardApportionDetailModelList.add(model);
			}
			modelService.saveAll(walletCardApportionDetailModelList);
			walletApportionModel.setWalletCardList(walletCardApportionDetailModelList);
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue())
			{
				walletApportionModel
						.setQcApportionPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcApportionPartValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue())
			{
				walletApportionModel
						.setQcDeliveryPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcDeliveryPartValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue())
			{
				walletApportionModel
						.setQcSchedulingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcSchedulingPartValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue())
			{
				walletApportionModel
						.setQcShippingPartValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getQcShippingPartValue());
			}
			walletApportionModel.setType("CANCEL");
			if (StringUtils.equalsIgnoreCase(response.getResponseCode().toString(), "0"))
			{
				walletApportionModel.setStatusForQc("SUCCESS");
			}
			else
			{
				walletApportionModel.setStatusForQc("PENDING");
			}
			
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspayApportionValue())
			{
				walletApportionModel
						.setJuspayApportionValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspayApportionValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspayDeliveryValue())
			{
				walletApportionModel
						.setJuspayDeliveryValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspayDeliveryValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspaySchedulingValue())
			{
				walletApportionModel
						.setJuspaySchedulingValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspaySchedulingValue());
			}
			if (null != abstractOrderEntryModel.getWalletApportionPaymentInfo()
					&& null != abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspayShippingValue())
			{
				walletApportionModel
						.setJuspayShippingValue(abstractOrderEntryModel.getWalletApportionPaymentInfo().getJuspayShippingValue());
			}
			walletApportionModel.setType("CANCEL");
			if (StringUtils.equalsIgnoreCase(response.getResponseCode().toString(), "0"))
			{
				walletApportionModel.setStatus("SUCCESS");
			}
			else
			{
				walletApportionModel.setStatus("PENDING");
			}
			modelService.save(walletApportionModel);
			abstractOrderEntryModel.setWalletApportionReturnInfo(walletApportionModel);
			modelService.save(abstractOrderEntryModel);
			modelService.refresh(abstractOrderEntryModel);
		}

	}


	private void updateWebHookExpired(final JuspayWebhookModel oModel)
	{
		if (null != oModel)
		{
			oModel.setIsExpired(Boolean.TRUE);
			modelService.save(oModel);
		}
	}

	private List<JuspayWebhookModel> checkstatusAtJuspay(final String juspayOrder)
	{
		return mplProcessOrderDao.getEventsForPendingOrders(juspayOrder);
	}

	/**
	 * @param orderModel
	 */
	private void processJuspayRefund(OrderModel orderModel)
	{
		if (null != orderModel.getGuid())
		{

			try
			{
				//If from Audit JuspayOrderId is fetched than no need to fetch from Session
				MplPaymentAuditModel auditModel = null;
				if (StringUtils.isNotEmpty(orderModel.getGuid())) //IQA for TPR-629
				{
					auditModel = mplOrderDao.getAuditList(orderModel.getGuid());
					LOG.debug("Latest Audit ID:- " + auditModel + "for respective GUID:- " + orderModel.getGuid());
				}

				List<JuspayWebhookModel> hooks = null;
				if (null != auditModel && StringUtils.isNotEmpty(auditModel.getAuditId()))
				{
					hooks = checkstatusAtJuspay(auditModel.getAuditId());
				}

				JuspayWebhookModel webHookModel = hooks.get(0);
				if (webHookModel.getOrderStatus().getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED)
						&& CollectionUtils.isEmpty(webHookModel.getOrderStatus().getRefunds()))

				{

					try
					{
						//TISPRO-130
						if (null != webHookModel.getOrderStatus().getPaymentMethodType() && webHookModel.getOrderStatus()
								.getPaymentMethodType().equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
						{
							//calling refund service where there will be cart only for NB
							mplJusPayRefundService.doRefund(auditModel.getAuditId(),
									webHookModel.getOrderStatus().getPaymentMethodType());
						}
						//TISPRO-675
						else if (StringUtils.isNotEmpty(webHookModel.getOrderStatus().getEmiBank())
								&& StringUtils.isNotEmpty(webHookModel.getOrderStatus().getEmiTenure()))
						{
							//calling refund service where there will be cart only for EMI
							mplJusPayRefundService.doRefund(auditModel.getAuditId(), MarketplacecommerceservicesConstants.EMI);
						}
						else
						{
							//calling refund service where there will be cart only for CARD
							mplJusPayRefundService.doRefund(auditModel.getAuditId(),
									webHookModel.getOrderStatus().getCardResponse().getCardType());
						}
					}
					catch (final Exception e)
					{
						LOG.error(e.getMessage(), e);
					}
					LOG.debug(MarketplacecommerceservicesConstants.WEBHOOKUPDATEMSG);
				//	updateWebHookExpired(webHookModel); //Commented for TPR-629 --- forward flow handled in processOrderJob

				}
			}
			catch (final Exception e)
			{
				LOG.error("Exception in picking up juspay order id from session...reverting to fallback mechanism with exception ",
						e);
			}
		}

	}

}
