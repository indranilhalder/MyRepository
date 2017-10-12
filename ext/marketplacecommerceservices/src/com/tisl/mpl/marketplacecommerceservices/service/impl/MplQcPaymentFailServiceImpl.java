/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.marketplacecommerceservices.service.MplQcPaymentFailService;
import com.tisl.mpl.promotion.dao.MplQcPaymentFailDao;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * @author Techouts
 *
 */
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
			 double refundAmount=0.0D;
			if (null != walletInfo.getType()
					&& walletInfo.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.QC_REFUND_TYPE_CANCEL))
			{
				double qcApportionAmount = getQcApportionValue(walletInfo.getQcApportionPartValue());
				if(qcApportionAmount > 0.0D) {
					refundAmount+=qcApportionAmount;
				}
				
				double qcDeliveryPartAmount = getQcDeliveryPartValue(walletInfo.getQcDeliveryPartValue());
				if(qcDeliveryPartAmount > 0.0D) {
					refundAmount+=qcDeliveryPartAmount;
				}
				
				double qcSchedulingPartAmount = getQcSchedulingPartValue(walletInfo.getQcSchedulingPartValue());
				if(qcSchedulingPartAmount > 0.0D) {
					refundAmount+=qcSchedulingPartAmount;
				}
				
				double qcShippingPartAmount = getQcApportionValue(walletInfo.getQcShippingPartValue());
				if(qcShippingPartAmount > 0.0D) {
					refundAmount+=qcShippingPartAmount;
				}

			}else if(null != walletInfo.getType()
					&& walletInfo.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.QC_REFUND_TYPE_RETURN)){
				double qcApportionAmount = getQcApportionValue(walletInfo.getQcApportionPartValue());
				if(qcApportionAmount > 0.0D) {
					refundAmount+=qcApportionAmount;
				}
				
				double qcDeliveryPartAmount = getQcDeliveryPartValue(walletInfo.getQcDeliveryPartValue());
				if(qcDeliveryPartAmount > 0.0D) {
					refundAmount+=qcDeliveryPartAmount;
				}
				
				/*double qcSchedulingPartAmount = getQcSchedulingPartValue(walletInfo.getQcSchedulingPartValue());
				if(qcSchedulingPartAmount > 0.0D) {
					refundAmount+=qcSchedulingPartAmount;
				}
				
				double qcShippingPartAmount = getQcApportionValue(walletInfo.getQcShippingPartValue());
				if(qcShippingPartAmount > 0.0D) {
					refundAmount+=qcShippingPartAmount;
				}*/
			}else if(null != walletInfo.getType()
					&& walletInfo.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.QC_REFUND_TYPE_REFUND))
			{
				double qcApportionAmount = getQcApportionValue(walletInfo.getQcApportionPartValue());
				if(qcApportionAmount > 0.0D) {
					refundAmount+=qcApportionAmount;
				}
				
				double qcDeliveryPartAmount = getQcDeliveryPartValue(walletInfo.getQcDeliveryPartValue());
				if(qcDeliveryPartAmount > 0.0D) {
					refundAmount+=qcDeliveryPartAmount;
				}
				
				double qcSchedulingPartAmount = getQcSchedulingPartValue(walletInfo.getQcSchedulingPartValue());
				if(qcSchedulingPartAmount > 0.0D) {
					refundAmount+=qcSchedulingPartAmount;
				}
				
				double qcShippingPartAmount = getQcApportionValue(walletInfo.getQcShippingPartValue());
				if(qcShippingPartAmount > 0.0D) {
					refundAmount+=qcShippingPartAmount;
				}
			}	
			if(refundAmount > 0.0D) {
				data.setAmount(String.valueOf(refundAmount));
			}
			if(null != customer && null != customer.getOriginalUid() ) {
				data.setCustomerEmailId(customer.getOriginalUid());
			}
			if(refundAmount > 0.0D) {
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
			if(null != qcApportionPartValue) {
				Double qcApportionValue = Double.valueOf(qcApportionPartValue);
				if(null !=qcApportionValue && qcApportionValue.doubleValue() >0.0D ) {
					amount=qcApportionValue.doubleValue();
				}
			}
		}
		return amount;
	}
	
	private double getQcDeliveryPartValue(String qcDeliveryPartValue)
	{
		double amount = 0.0D;
		{
			if(null != qcDeliveryPartValue) {
				Double qcDeliveryValue = Double.valueOf(qcDeliveryPartValue);
				if(null !=qcDeliveryValue && qcDeliveryValue.doubleValue() >0.0D ) {
					amount=qcDeliveryValue.doubleValue();
				}
			}
		}
		return amount;
	}
	
	private double getQcSchedulingPartValue(String qcSchedulingPartValue)
	{
		double amount = 0.0D;
		{
			if(null != qcSchedulingPartValue) {
				Double qcSchedulingValue = Double.valueOf(qcSchedulingPartValue);
				if(null !=qcSchedulingValue && qcSchedulingValue.doubleValue() >0.0D ) {
					amount=qcSchedulingValue.doubleValue();
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

}
