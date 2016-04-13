/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
//import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedEvent;
import com.tisl.mpl.sms.MplSendSMSService;


/**
 * Listener for order confirmation events.
 */
public class OrderConfirmationEventListener extends AbstractSiteEventListener<OrderPlacedEvent>
{

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplSendSMSService sendSMSService;

	private static final Logger LOG = Logger.getLogger(OrderConfirmationEventListener.class);

	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected void onSiteEvent(final OrderPlacedEvent orderPlacedEvent)
	{
		//send Email
		final OrderModel orderModel = orderPlacedEvent.getProcess().getOrder();
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"orderConfirmationEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
				"orderConfirmationEmailProcess");
		orderProcessModel.setOrder(orderModel);
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);

		//send SMS
		try
		{
			String mobileNumber = null;
			String firstName = null;
			
			final OrderModel orderDetails = orderProcessModel.getOrder();
			CustomerModel customer=null;
			if(orderModel.getUser() != null && orderModel.getUser() instanceof CustomerModel){
				 customer=(CustomerModel) orderModel.getUser();
			}
			if(null != orderDetails  && orderDetails.getDeliveryAddress() != null && orderDetails.getDeliveryAddress().getPhone1()!= null){
				mobileNumber = orderDetails.getDeliveryAddress().getPhone1();
			}else{
			 mobileNumber = customer.getMobileNumber();
			}
			if(null != orderDetails  && orderDetails.getDeliveryAddress() != null && orderDetails.getDeliveryAddress().getFirstname()!= null){
				firstName = orderDetails.getDeliveryAddress().getFirstname();
			}else{
				if (null != customer && customer.getFirstName() !=null){
				    firstName = customer.getFirstName();
				}else {
					firstName = "Customer";
				}
			}
			
			final String orderReferenceNumber = orderDetails.getCode();
			final String trackingUrl = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
					+ orderReferenceNumber;
			final String content = MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderReferenceNumber)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, trackingUrl);

			final SendSMSRequestData smsRequestData = new SendSMSRequestData();
			smsRequestData.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestData.setContent(content);
			smsRequestData.setRecipientPhoneNumber(mobileNumber);
			sendSMSService.sendSMS(smsRequestData);

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			LOG.error("EtailNonBusinessExceptions occured while sending sms " + ex);
		}
		catch (final Exception ex)
		{
			LOG.error("Exceptions occured while sending sms " + ex);
		}

	}

	@Override
	protected boolean shouldHandleEvent(final OrderPlacedEvent event)
	{
		final OrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}
}
