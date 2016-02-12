package com.tisl.mpl.cockpits.cscockpit.services.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.services.RefundInitiatedNotificationService;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderRefundProcessModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.marketplacecommerceservices.event.RefundInitiatedEvent;
import com.tisl.mpl.sms.MplSendSMSService;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

public class DefaultRefundInitiatedNotificationService implements
		RefundInitiatedNotificationService {

	private static final Logger LOG = Logger
			.getLogger(DefaultRefundInitiatedNotificationService.class);
	@Autowired
	private ModelService modelService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Autowired
	private MplSendSMSService sendSMSService;

	@Autowired
	private EventService eventService;

	@Autowired
	private BusinessProcessService businessProcessService;

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *            the businessProcessService to set
	 */

	public void setBusinessProcessService(
			final BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @Description: For sending Notification for RefundInitiated
	 * @param noOfItems
	 * @param orderModel
	 * @param returnEntry
	 */
	@Override
	public void sendRefundInitiatedNotification(int noOfItems,
			ReturnEntryModel returnEntry, final OrderModel orderModel) {

		// ********* Refund Initiated Email *************
		/*
		 * final OrderProcessModel orderProcessModel = new OrderProcessModel();
		 * orderProcessModel.setOrder(orderModel); final RefundInitiatedEvent
		 * refundInitiatedEvent = new RefundInitiatedEvent( orderProcessModel);
		 * 
		 * try { eventService.publishEvent(refundInitiatedEvent); } catch (final
		 * Exception e1) {
		 * LOG.error("Exception during sending Refund Initiated  mail >> " +
		 * e1.getMessage()); }
		 */
		LOG.info("Starting Order Refund Mail");
		try {
//			final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService()
//					.createProcess(
//							"sendOrderRefundEmailProcess-"
//									+ orderModel.getCode() + "-"
//									+ System.currentTimeMillis(),
//							"sendOrderRefundEmailProcess");
			final OrderRefundProcessModel orderRefundProcessModel = (OrderRefundProcessModel) getBusinessProcessService()
					.createProcess(
							"sendOrderRefundEmailProcess-"
									+ orderModel.getCode() + "-"
									+ System.currentTimeMillis(),
							"sendOrderRefundEmailProcess");
			orderRefundProcessModel.setOrder(orderModel);
			orderRefundProcessModel.setReturnEntry((RefundEntryModel)returnEntry);
			modelService.save(orderRefundProcessModel);
			businessProcessService.startProcess(orderRefundProcessModel);
		} catch (final Exception e) {
			LOG.error("Exception during sending Refund Initiated  mail >> "
					+ e.getMessage());
		}
		LOG.info("Ending Order Refund Mail");
		// ********* Refund Initiated SMS *************
		String items = null;
		BigDecimal refundAmount = null;
		if (returnEntry instanceof RefundEntryModel) {
			RefundEntryModel refundEntry = (RefundEntryModel) returnEntry;
			AbstractOrderEntryModel orderEntry = refundEntry.getOrderEntry();
			double deliveryCharge = orderEntry.getCurrDelCharge() == null ? 0D
					: orderEntry.getCurrDelCharge().doubleValue();
			if (null != refundEntry.getOrderEntry()) {
				// refundAmount =
				// BigDecimal.valueOf(refundEntry.getAmount().doubleValue());refundEntry.getOrderEntry().getNetAmountAfterAllDisc();
				refundAmount = BigDecimal.valueOf(refundEntry.getOrderEntry()
						.getNetAmountAfterAllDisc());
				LOG.info("--------------------------- For order :"
						+ orderModel.getCode() + ", Refund Amount is >>>>>>"
						+ refundAmount + " where deliverycharge is :"
						+ deliveryCharge);
			}

			items = String.valueOf(refundEntry.getExpectedQuantity());

		}
		// final String items = String.valueOf(noOfItems);

		// final String amount = String.valueOf(returnEntry.getOrderEntry()
		// .getBasePrice());
		String orderNumber = null;
		if (returnEntry.getOrderEntry() != null
				&& returnEntry.getOrderEntry().getOrder() != null) {
			OrderModel subOrder = (OrderModel) returnEntry.getOrderEntry()
					.getOrder();
			if (subOrder.getParentReference() != null) {
				orderNumber = subOrder.getParentReference().getCode();
			}
		}
		final String mobileNumber = returnEntry.getReturnRequest().getOrder()
				.getDeliveryAddress().getPhone1();

		final SendSMSRequestData smsRequestDataRefundInitiated = new SendSMSRequestData();
		smsRequestDataRefundInitiated
				.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
		smsRequestDataRefundInitiated
				.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_REFUND_INITIATED
						.replace(
								MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
								String.valueOf(refundAmount))
						.replace(
								MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE,
								items)
						.replace(
								MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO,
								orderNumber));
		smsRequestDataRefundInitiated.setRecipientPhoneNumber(mobileNumber);

		try {
			sendSMSService.sendSMS(smsRequestDataRefundInitiated);
		} catch (final JAXBException e) {
			LOG.error("Exception during sending refund initiated SMS >> "
					+ e.getMessage());
		}

	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

}
