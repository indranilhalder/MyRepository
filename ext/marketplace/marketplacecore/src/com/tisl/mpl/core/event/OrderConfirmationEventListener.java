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
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
//import de.hybris.platform.orderprocessing.events.OrderPlacedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.WalletCardApportionDetailModel;
import com.tisl.mpl.core.mplqcprogram.dao.MplQCProgramDao;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedEvent;
import com.tisl.mpl.shorturl.service.ShortUrlService;
import com.tisl.mpl.sms.MplSendSMSService;


/**
 * Listener for order confirmation events.
 */
public class OrderConfirmationEventListener extends AbstractSiteEventListener<OrderPlacedEvent>
{

	/**
	 * 
	 */
	private static final String CUSTOMER2 = "Customer";

	/**
	 * 
	 */
	private static final String JUSPAY = "Juspay";

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplSendSMSService sendSMSService;

	@Autowired
	private ShortUrlService googleShortUrlService;
	@Autowired
	MplQCProgramDao mplQCProgramDao;

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
		final String shortTrackingUrl = googleShortUrlService
				.genearateShortURL(orderModel.getParentReference() == null ? orderModel.getCode() : orderModel
						.getParentReference().getCode());
		orderProcessModel.setOrder(orderModel);
		getModelService().save(orderProcessModel);
		if(null != shortTrackingUrl) {
			orderProcessModel.setOrderTrackUrl(shortTrackingUrl);
		}
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
					firstName = CUSTOMER2;
				}
			}
			
			final String orderReferenceNumber = orderDetails.getCode();
			//SDI-2038
			final String trackingUrl = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT)
					+ "/" + orderReferenceNumber;
			String url=null;
			if (null != orderDetails.getIsEGVCart() && orderDetails.getIsEGVCart().booleanValue())
			{
				url = "My Account";
			}
			else
			{
				url = null != shortTrackingUrl ? shortTrackingUrl : trackingUrl;
			}
//			final String shortTrackingUrl = googleShortUrlService
//					.genearateShortURL(orderModel.getParentReference() == null ? orderModel.getCode() : orderModel
//							.getParentReference().getCode());
			try{
			final String content = MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderReferenceNumber)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, url);

			final SendSMSRequestData smsRequestData = new SendSMSRequestData();
			smsRequestData.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestData.setContent(content);
			smsRequestData.setRecipientPhoneNumber(mobileNumber);
			sendSMSService.sendSMS(smsRequestData);
			}catch (final Exception ex)
			{
				LOG.error("Exceptions occured while sending sms " + ex);
			}
			/***
			 * Added Code Send sms Notification For Wallet redeemed  Start
			 *
			 */

			  sendNotificationWalletRedeemed(orderModel, customer);
          //Send sms Notification For Wallet redeemed  Start

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

	/**
	 * @param orderModel
	 * @param customer
	 * @param content
	 * 
	 * Send sms redeemed  amount from 
	 */
	private void sendNotificationWalletRedeemed(final OrderModel orderModel, CustomerModel customer)
	{
		if (StringUtils.isNotEmpty(orderModel.getSplitModeInfo()) && !JUSPAY.equalsIgnoreCase(orderModel.getSplitModeInfo()))
		{
			for (AbstractOrderEntryModel entry : orderModel.getEntries())
			{
				if (entry.getWalletApportionPaymentInfo() != null
						&& entry.getWalletApportionPaymentInfo().getWalletCardList() != null)
				{
					for (WalletCardApportionDetailModel cardSplitValue : entry.getWalletApportionPaymentInfo().getWalletCardList())
					{
						if (CUSTOMER2.equalsIgnoreCase(cardSplitValue.getBucketType()))
						{
							String totalAmt = mplQCProgramDao.getCardTotalAmount(cardSplitValue.getCardNumber());
							int totalAmount = Integer.parseInt(totalAmt);
							int redeemedTotalAmount = Integer.parseInt(cardSplitValue.getCardAmount());
							int remainingAmount = totalAmount - redeemedTotalAmount;
							sendNotificationForRedeemedAmountFromWallet(customer, cardSplitValue.getCardNumber(),
									cardSplitValue.getCardAmount(), Integer.toString(remainingAmount));
						}
					}
				}
			}
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
	
	//Send sms redeemed  amount from 
	public void sendNotificationForRedeemedAmountFromWallet(final CustomerModel customerModel, final String cardNumber,
			final String redeemedAmount, String remainingAmount)
	{
		try
		{
			if (StringUtils.isNotEmpty(customerModel.getQcVerifyMobileNo()))
			{
				String content = MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED_FROM_WALLET
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, cardNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, redeemedAmount)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, remainingAmount);
				final SendSMSRequestData smsRequestData = new SendSMSRequestData();
				smsRequestData.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				smsRequestData.setContent(content);
				smsRequestData.setRecipientPhoneNumber(customerModel.getQcVerifyMobileNo());
				sendSMSService.sendSMS(smsRequestData);
			}
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}

	}

	
}
