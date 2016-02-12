/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;


import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.marketplacecommerceservices.event.OrderConfirmationRiskEvent;
import com.tisl.mpl.marketplacecommerceservices.event.OrderOnHoldEvent;
import com.tisl.mpl.marketplacecommerceservices.event.OrderRejectRiskEvent;
import com.tisl.mpl.marketplacecommerceservices.service.RMSVerificationNotificationService;
import com.tisl.mpl.sms.MplSendSMSService;


/**
 * @author TCS
 *
 */
public class DefaultRMSVerificationNotificationService implements RMSVerificationNotificationService
{

	private static final Logger LOG = Logger.getLogger(DefaultRMSVerificationNotificationService.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Autowired
	private MplSendSMSService sendSMSService;

	@Autowired
	private EventService eventService;

	/**
	 * @Description: For sending Notification for RMS verification
	 * @param orderModel
	 */
	@Override
	public void sendRMSNotification(final OrderModel orderModel)
	{
		String orderStatus = null;
		if (null != orderModel.getStatus())
		{
			orderStatus = orderModel.getStatus().toString();
		}


		//TODO:: Send notification about order placed which is subjected to terms due to medium risk/High risk
		if (null != orderStatus && orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.RMS_VERIFICATION_PENDING))
		{

			// Email ************* Order On Hold High Risk
			final OrderProcessModel orderProcessModel = new OrderProcessModel();
			orderProcessModel.setOrder(orderModel);
			final OrderOnHoldEvent orderOnHoldEvent = new OrderOnHoldEvent(orderProcessModel);

			try
			{
				eventService.publishEvent(orderOnHoldEvent);
			}
			catch (final Exception e1)
			{ // YTODO
			  // Auto-generated catch block
				LOG.error("Exception during sending mail >> " + e1.getMessage());
			}

			//SendSms--Start


			final String orderNumber = (null == orderModel.getCode()) ? "" : orderModel.getCode();
			final String mobileNumber = orderModel.getDeliveryAddress().getPhone1();
			final String contactNumber = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);

			final SendSMSRequestData smsRequestDataRMSonHold = new SendSMSRequestData();
			smsRequestDataRMSonHold.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataRMSonHold.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_RISK.replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderNumber).replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, contactNumber));
			smsRequestDataRMSonHold.setRecipientPhoneNumber(mobileNumber);

			try
			{
				sendSMSService.sendSMS(smsRequestDataRMSonHold);
			}
			catch (final JAXBException e)
			{

				LOG.error("Exception during sending SMS>>>>SMS_MESSAGE_ORDER_RISK>>>> " + e.getMessage());
			}


			//SendSms--Ends
		}
		//TODO::Send notification once the medium risk /High risk order is rejected
		else if (null != orderStatus && orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.RMS_VERIFICATION_FAILED))
		{

			// Email ************* Order Reject High Risk
			final OrderProcessModel orderProcessModel = new OrderProcessModel();
			orderProcessModel.setOrder(orderModel);
			final OrderRejectRiskEvent orderRejectRiskEvent = new OrderRejectRiskEvent(orderProcessModel);

			try
			{
				eventService.publishEvent(orderRejectRiskEvent);
			}
			catch (final Exception e1)
			{ // YTODO
			  // Auto-generated catch block
				LOG.error("Exception during sending mail >> " + e1.getMessage());
			}


			//SendSms--Start


			final String orderNumber = (null == orderModel.getCode()) ? "" : orderModel.getCode();
			final String mobileNumber = orderModel.getDeliveryAddress().getPhone1();
			final String contactNumber = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
			final String appDwldUrl = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_SERVICE_APP_DWLD_URL);
			final String webUrl = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_SERVICE_WEBSITE_URL);

			final SendSMSRequestData smsRequestDataRiskRejected = new SendSMSRequestData();
			smsRequestDataRiskRejected.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataRiskRejected.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_RISK_REJECTED
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderNumber)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, webUrl)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, appDwldUrl)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE, contactNumber));
			smsRequestDataRiskRejected.setRecipientPhoneNumber(mobileNumber);

			try
			{
				sendSMSService.sendSMS(smsRequestDataRiskRejected);
			}
			catch (final JAXBException e)
			{

				LOG.error("Exception during sending SMS>>>>SMS_MESSAGE_ORDER_RISK_REJECTED>> ", e);
			}


			//SendSms--Ends

		}

		//TODO::Send notification once the medium risk /high risk order is confirmed
		else if (null != orderStatus && orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENT_SUCCESSFUL))
		{

			// Email ************* Order Confirmation High Risk
			final OrderProcessModel orderProcessModel = new OrderProcessModel();
			orderProcessModel.setOrder(orderModel);
			final OrderConfirmationRiskEvent orderConfirmationRiskEvent = new OrderConfirmationRiskEvent(orderProcessModel);
			try
			{
				eventService.publishEvent(orderConfirmationRiskEvent);
			}
			catch (final Exception e1)
			{ // YTODO
			  // Auto-generated catch block
				LOG.error("Exception during sending mail >> " + e1.getMessage());
			}

			//SendSms--Start


			final String orderNumber = (null == orderModel.getCode()) ? "" : orderModel.getCode();
			final String mobileNumber = orderModel.getDeliveryAddress().getPhone1();
			final String trackingUrl = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
					+ orderNumber;

			final SendSMSRequestData smsRequestDataRiskConfirmed = new SendSMSRequestData();
			smsRequestDataRiskConfirmed.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataRiskConfirmed.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_RISK_CONFIRMED.replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderNumber).replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, trackingUrl));
			smsRequestDataRiskConfirmed.setRecipientPhoneNumber(mobileNumber);

			try
			{
				sendSMSService.sendSMS(smsRequestDataRiskConfirmed);
			}
			catch (final JAXBException e)
			{

				LOG.error("Exception during sending SMS>>>>SMS_MESSAGE_ORDER_RISK_CONFIRMED>>>> ", e);
			}


			//SendSms--Ends

		}
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

}
