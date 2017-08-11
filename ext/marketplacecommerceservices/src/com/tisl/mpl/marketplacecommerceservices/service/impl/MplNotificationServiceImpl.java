/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderRefundProcessModel;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.marketplacecommerceservices.service.MplNotificationService;
import com.tisl.mpl.sms.MplSendSMSService;



/**
 * @author TCS
 */
public class MplNotificationServiceImpl implements MplNotificationService
{
	@Autowired
	private ModelService modelService;

	@Autowired
	private MplSendSMSService sendSMSService; // added for TPR-1348 AutoRefund initiation

	@Autowired
	private BusinessProcessService businessProcessService; // added for TPR-1348 AutoRefund initiation

	/**
	 * @return the sendSMSService
	 */
	public MplSendSMSService getSendSMSService()
	{
		return sendSMSService;
	}



	/**
	 * @param sendSMSService
	 *           the sendSMSService to set
	 */
	public void setSendSMSService(final MplSendSMSService sendSMSService)
	{
		this.sendSMSService = sendSMSService;
	}



	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}



	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	private static final Logger LOG = Logger.getLogger(MplNotificationServiceImpl.class);

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}



	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
		LOG.debug("Saved Into Model");
	}

	/*
	 *
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplPriceRowService#getPriceRowDetail(de.hybris.platform.europe1
	 * .model.PriceRowModel)
	 *
	 * @Javadoc Method to Retrieve Pricerow based on articleSKUID
	 *
	 * @param articleSKUID
	 *
	 * @return listOfPrice
	 */

	@Override
	public void saveToNotification(final OrderStatusNotificationModel osnm)
	{
		modelService.save(osnm);

	}

	/*
	 * @Javadoc Method to Retrieve Pricerow based on articleSKUIDs
	 *
	 * @param aticleSKUIDs
	 *
	 * @return mopMap
	 */

	@Override
	public void sendRefundInitiatedNotification(final int noOfItems, final ReturnEntryModel returnEntry,
			final OrderModel orderModel)
	{
		LOG.info("Starting Auto Order Refund Mail");
		try
		{
			// final OrderProcessModel orderProcessModel = (OrderProcessModel)
			// getBusinessProcessService()
			// .createProcess(
			// "sendOrderRefundEmailProcess-"
			// + orderModel.getCode() + "-"
			// + System.currentTimeMillis(),
			// "sendOrderRefundEmailProcess");
			final OrderRefundProcessModel orderRefundProcessModel = (OrderRefundProcessModel) getBusinessProcessService()
					.createProcess("sendOrderRefundEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
							"sendOrderRefundEmailProcess");
			orderRefundProcessModel.setOrder(orderModel);
			orderRefundProcessModel.setReturnEntry((RefundEntryModel) returnEntry);
			modelService.save(orderRefundProcessModel);
			businessProcessService.startProcess(orderRefundProcessModel);
		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending Refund Initiated  mail >> " + e.getMessage());
		}
		LOG.info("Ending Order Refund Mail");
		// ********* Refund Initiated SMS *************
		String items = null;
		BigDecimal refundAmount = null;
		if (returnEntry instanceof RefundEntryModel)
		{
			final RefundEntryModel refundEntry = (RefundEntryModel) returnEntry;
			final AbstractOrderEntryModel orderEntry = refundEntry.getOrderEntry();
			final double deliveryCharge = orderEntry.getCurrDelCharge() == null ? 0D : orderEntry.getCurrDelCharge().doubleValue();
			if (null != refundEntry.getOrderEntry())
			{
				// refundAmount =
				// BigDecimal.valueOf(refundEntry.getAmount().doubleValue());refundEntry.getOrderEntry().getNetAmountAfterAllDisc();
				refundAmount = BigDecimal.valueOf(refundEntry.getOrderEntry().getNetAmountAfterAllDisc());
				LOG.info("--------------------------- For order :" + orderModel.getCode() + ", Refund Amount is >>>>>>"
						+ refundAmount + " where deliverycharge is :" + deliveryCharge);
			}

			items = String.valueOf(refundEntry.getExpectedQuantity());

		}
		// final String items = String.valueOf(noOfItems);

		// final String amount = String.valueOf(returnEntry.getOrderEntry()
		// .getBasePrice());
		String orderNumber = null;
		if (returnEntry.getOrderEntry() != null && returnEntry.getOrderEntry().getOrder() != null)
		{
			final OrderModel subOrder = (OrderModel) returnEntry.getOrderEntry().getOrder();
			if (subOrder.getParentReference() != null)
			{
				orderNumber = subOrder.getParentReference().getCode();
			}
		}
		String mobileNumber = null;

		// in Case of CnC Order taking mobile number from CustomerModel
		try
		{
			if (null != returnEntry && null != returnEntry.getOrderEntry()
					&& null != returnEntry.getOrderEntry().getMplDeliveryMode()
					&& null != returnEntry.getOrderEntry().getMplDeliveryMode().getDeliveryMode())
			{
				if (returnEntry.getOrderEntry().getMplDeliveryMode().getDeliveryMode().getCode().trim()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_AND_COLLECT.trim()))
				{
					try
					{
						final CustomerModel customermodel = (CustomerModel) orderModel.getUser();
						if (null != customermodel && null != customermodel.getMobileNumber())
						{
							LOG.debug(" Customer mobile Number: " + mobileNumber);
							mobileNumber = customermodel.getMobileNumber();
						}
					}
					catch (final Exception e)
					{
						LOG.debug(" Customer mobile Number: " + mobileNumber);
					}
					if (null == mobileNumber || mobileNumber.isEmpty() && null != orderModel
							&& null != orderModel.getPickupPersonMobile())
					{
						mobileNumber = orderModel.getPickupPersonMobile();
						LOG.debug(" Customer mobile Number: " + mobileNumber);
					}
				}
				else if (null != returnEntry)
				{
					try
					{
						mobileNumber = returnEntry.getReturnRequest().getOrder().getDeliveryAddress().getPhone1();
						LOG.debug("SMS Mobile Number : " + mobileNumber);
					}
					catch (final Exception e)
					{
						LOG.info("Mobile Number null" + e);
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.debug("Customer Mobile Number null ");
		}

		final SendSMSRequestData smsRequestDataRefundInitiated = new SendSMSRequestData();
		smsRequestDataRefundInitiated.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
		smsRequestDataRefundInitiated.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_REFUND_INITIATED
				.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(refundAmount))
				.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, items)
				.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, orderNumber));
		smsRequestDataRefundInitiated.setRecipientPhoneNumber(mobileNumber);

		try
		{
			sendSMSService.sendSMS(smsRequestDataRefundInitiated);
		}
		catch (final JAXBException e)
		{
			LOG.error("Exception during sending refund initiated SMS >> " + e.getMessage());
		}

	}


}
