/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedEvent;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.sns.push.service.MplSNSMobilePushService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.PushNotificationData;


/**
 * @author TCS
 *
 */
public class NotificationServiceImpl implements NotificationService
{
	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private EventService eventService;

	@Autowired
	private MplSNSMobilePushService mplSNSMobilePushService;


	/**
	 * @return the notificationDao
	 */
	public NotificationDao getNotificationDao()
	{
		return notificationDao;
	}

	/**
	 * @param notificationDao
	 *           the notificationDao to set
	 */
	public void setNotificationDao(final NotificationDao notificationDao)
	{
		this.notificationDao = notificationDao;
	}

	@Resource(name = "modelService")
	private ModelService modelService;




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
	}


	private static final Logger LOG = Logger.getLogger(NotificationServiceImpl.class);


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getNotification()
	 */
	@Override
	public List<NotificationData> getNotification()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * Getting notificationDetails of logged User (non-Javadoc) (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getNotificationDetails(com.tisl.mpl.data.
	 * NotificationData)
	 */
	@Override
	public List<OrderStatusNotificationModel> getNotificationDetails(final String customerUID, final boolean isDesktop)
	{
		List<OrderStatusNotificationModel> notificationdetailslist = null;
		/*
		 * final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser(); final String email =
		 * currentCustomer.getOriginalUid();
		 */
		try
		{
			notificationdetailslist = getNotificationDao().getNotificationDetail(customerUID, isDesktop);
			return notificationdetailslist;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return notificationdetailslist;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#checkCustomerFacingEntry(com.tisl.mpl.core
	 * .model.OrderStatusNotificationModel)
	 */
	@Override
	public boolean checkCustomerFacingEntry(final OrderStatusNotificationModel osnm)
	{
		boolean statusPresent = false;

		final List<OrderStatusNotificationModel> notificationList = getNotificationDao().checkCustomerFacingEntry(
				osnm.getCustomerUID(), osnm.getOrderNumber(), osnm.getTransactionId(), osnm.getCustomerStatus());
		if (notificationList != null && !notificationList.isEmpty())
		{
			statusPresent = true;
		}

		return statusPresent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#markNotificationRead(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void markNotificationRead(final String customerId, final String orderNo, final String consignmentNo,
			final String shopperStatus)
	{
		final List<OrderStatusNotificationModel> notificationList = getNotificationDao().getModelforDetails(customerId, orderNo,
				consignmentNo, shopperStatus);
		final Boolean isRead = Boolean.TRUE;
		for (final OrderStatusNotificationModel osn : notificationList)
		{
			osn.setIsRead(isRead);
			modelService.save(osn);

		}



	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#markNotificationRead(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<OrderStatusNotificationModel> isAlreadyNotified(final String customerId, final String orderNo,
			final String transactionId, final String orderStatus)
	{
		List<OrderStatusNotificationModel> notificationList = new ArrayList<OrderStatusNotificationModel>();
		try
		{
			notificationList = getNotificationDao().getNotification(customerId, orderNo, transactionId, orderStatus);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return notificationList;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return notificationList;
		}
		return notificationList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#triggerEmailAndSmsOnOrderConfirmation(de.
	 * hybris.platform.core.model.order.OrderModel, java.lang.String)
	 */
	@Override
	public void triggerEmailAndSmsOnOrderConfirmation(final OrderModel orderDetails, final String trackorderurl)
			throws JAXBException
	{
		//Moved the SMS service to EventListener
		//final String mobileNumber = orderDetails.getDeliveryAddress().getPhone1();
		//final String firstName = orderDetails.getDeliveryAddress().getFirstname();
		//final String orderReferenceNumber = orderDetails.getCode();
		//final String trackingUrl = configurationService.getConfiguration().getString(
		//		MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
		//		+ orderReferenceNumber;

		if (orderDetails.getStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL))
		{
			final OrderProcessModel orderProcessModel = new OrderProcessModel();
			orderProcessModel.setOrder(orderDetails);
			orderProcessModel.setOrderTrackUrl(trackorderurl);
			final OrderPlacedEvent orderplacedEvent = new OrderPlacedEvent(orderProcessModel);
			try
			{
				eventService.publishEvent(orderplacedEvent);
			}
			catch (final Exception e1)
			{ // YTODO
			  // Auto-generated catch block
				LOG.error("Exception during sending mail or SMS >> " + e1.getMessage());
			}

			//Moved the SMS service to OrderConfirmationEventListener
			//			try
			//			{
			//				final String content = MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED
			//						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
			//						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderReferenceNumber)
			//						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, trackingUrl);
			//
			//				final SendSMSRequestData smsRequestData = new SendSMSRequestData();
			//				smsRequestData.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			//				smsRequestData.setContent(content);
			//				smsRequestData.setRecipientPhoneNumber(mobileNumber);
			//				sendSMSService.sendSMS(smsRequestData);
			//
			//			}
			//			catch (final EtailNonBusinessExceptions ex)
			//			{
			//				LOG.error("EtailNonBusinessExceptions occured while sending sms " + ex);
			//			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#sendMobileNotifications(de.hybris.platform
	 * .core.model.order.OrderModel)
	 */
	@Override
	public void sendMobileNotifications(final OrderModel orderDetails)
	{
		try
		{
			String orderReferenceNumber = null;

			if (null != orderDetails.getCode() && !orderDetails.getCode().isEmpty())
			{
				orderReferenceNumber = orderDetails.getCode();
			}
			String uid = null;
			if (null != orderDetails.getUser() && null != orderDetails.getUser().getUid()
					&& !orderDetails.getUser().getUid().isEmpty())
			{
				uid = orderDetails.getUser().getUid();
			}
			PushNotificationData pushData = null;
			CustomerModel customer = getModelService().create(CustomerModel.class);
			if (null != uid && !uid.isEmpty())
			{
				customer = mplSNSMobilePushService.getCustForUId(uid);
				if (null != customer && null != customer.getDeviceKey() && !customer.getDeviceKey().isEmpty())
				{
					pushData = new PushNotificationData();
					if (null != orderReferenceNumber)
					{
						pushData.setMessage(MarketplacecommerceservicesConstants.PUSH_MESSAGE_ORDER_PLACED.replace(
								MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderReferenceNumber));
						pushData.setOrderId(orderReferenceNumber);
					}
					if (null != customer.getOriginalUid() && !customer.getOriginalUid().isEmpty() && null != customer.getIsActive()
							&& !customer.getIsActive().isEmpty() && customer.getIsActive().equalsIgnoreCase("Y"))
					{
						mplSNSMobilePushService.setUpNotification(customer.getOriginalUid(), pushData);
					}
				}
			}

		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9032);
		}

	}


}
