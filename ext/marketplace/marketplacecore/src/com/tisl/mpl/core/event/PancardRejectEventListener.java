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
//import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.event.PancardRejectEvent;


/**
 * Listener for Pancard Reject events.
 */
public class PancardRejectEventListener extends AbstractSiteEventListener<PancardRejectEvent>
{
	//SONAR FIX JEWELLERY
	//@Autowired
	//private ConfigurationService configurationService;
	//SONAR FIX JEWELLERY
	//@Autowired
	//private MplSendSMSService sendSMSService;
	//SONAR FIX JEWELLERY
	//@Autowired
	//private ShortUrlService googleShortUrlService;
	//SONAR FIX JEWELLERY
	//	private static final Logger LOG = Logger.getLogger(PancardRejectEventListener.class);

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
	protected void onSiteEvent(final PancardRejectEvent PancardRejectEvent)
	{
		//send Email
		final OrderModel orderModel = PancardRejectEvent.getOrder();

		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"pancardRejectEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(), "pancardRejectEmailProcess");
		orderProcessModel.setOrder(orderModel);
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);

		//send SMS
		/*
		 * try { String mobileNumber = null; String firstName = null;
		 * 
		 * final OrderModel orderDetails = orderProcessModel.getOrder(); CustomerModel customer = null; if
		 * (orderModel.getUser() != null && orderModel.getUser() instanceof CustomerModel) { customer = (CustomerModel)
		 * orderModel.getUser(); } if (null != orderDetails && orderDetails.getDeliveryAddress() != null &&
		 * orderDetails.getDeliveryAddress().getPhone1() != null) { mobileNumber =
		 * orderDetails.getDeliveryAddress().getPhone1(); } else { mobileNumber = customer.getMobileNumber(); } if (null
		 * != orderDetails && orderDetails.getDeliveryAddress() != null &&
		 * orderDetails.getDeliveryAddress().getFirstname() != null) { firstName =
		 * orderDetails.getDeliveryAddress().getFirstname(); } else { if (null != customer && customer.getFirstName() !=
		 * null) { firstName = customer.getFirstName(); } else { firstName = "Customer"; } }
		 * 
		 * final String orderReferenceNumber = orderDetails.getCode(); final String trackingUrl =
		 * configurationService.getConfiguration().getString(
		 * MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT) + orderReferenceNumber;
		 * 
		 * final String shortTrackingUrl = googleShortUrlService .genearateShortURL(orderModel.getParentReference() ==
		 * null ? orderModel.getCode() : orderModel .getParentReference().getCode()); final String content =
		 * MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED
		 * .replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
		 * .replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderReferenceNumber)
		 * .replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, null != shortTrackingUrl ? shortTrackingUrl :
		 * trackingUrl);
		 * 
		 * final SendSMSRequestData smsRequestData = new SendSMSRequestData();
		 * smsRequestData.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
		 * smsRequestData.setContent(content); smsRequestData.setRecipientPhoneNumber(mobileNumber);
		 * sendSMSService.sendSMS(smsRequestData);
		 *
		 * } catch (final EtailNonBusinessExceptions ex) { LOG.error(
		 * "EtailNonBusinessExceptions occured while sending sms " + ex); } catch (final Exception ex) { LOG.error(
		 * "Exceptions occured while sending sms " + ex); }
		 */
	}

	@Override
	protected boolean shouldHandleEvent(final PancardRejectEvent event)
	{
		final OrderModel order = event.getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}
}
