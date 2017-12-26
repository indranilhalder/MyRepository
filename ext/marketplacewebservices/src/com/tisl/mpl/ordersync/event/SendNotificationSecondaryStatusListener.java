/**
 *
 */
package com.tisl.mpl.ordersync.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.marketplaceomsservices.daos.EmailAndSmsNotification;
import com.tisl.mpl.marketplaceomsservices.event.SendNotificationEventListener;
import com.tisl.mpl.marketplaceomsservices.event.SendNotificationSecondaryStatusEvent;


/**
 * @author TCS
 *
 */
public class SendNotificationSecondaryStatusListener extends AbstractSiteEventListener<SendNotificationSecondaryStatusEvent>
{
	private ModelService modelService;



	private ConfigurationService configurationService;


	@Resource(name = "emailAndSmsNotification")
	private EmailAndSmsNotification emailAndSmsNotification;


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	private static final Logger LOG = Logger.getLogger(SendNotificationEventListener.class);
	private static final String UPDATE_CONSIGNMENT = "updateConsignment:: Inside ";

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
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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
	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


	private BusinessProcessService businessProcessService;


	@Override
	protected void onSiteEvent(final SendNotificationSecondaryStatusEvent sendNotificationEvent)
	{
		LOG.info("Inside onSiteEvent");
	}


	@Override
	protected boolean shouldHandleEvent(final SendNotificationSecondaryStatusEvent event)
	{
		LOG.info("*************Inside shouldHandleEvent *******************");
		final OrderModel order = event.getOrderModel();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		LOG.info("Inside shouldHandleEvent Channel: " + site.getChannel());
		LOG.info("Sending notification >>>>: ");
		sendNotification(event.getAwbSecondaryStatus(), event.getOrderLineID(), event.getOrderModel());
		return SiteChannel.B2C.equals(site.getChannel());
	}

	/**
	 * This method is responsible for sending Order Status related Notification
	 *
	 * @param orderModel
	 *
	 */
	private void sendNotification(final String awbSecondaryStatus, final String orderLineId, final OrderModel orderModel)
	{

		try
		{

			LOG.info("*************Inside sendNotification *******************");
			// TISPRDT-1399 START  , Sending Mail Order Id in SMS
			final String orderNumber = orderModel.getParentReference().getCode();
			// TISPRDT-1399 END
			String mobileNumber = null;
			if (orderModel.getDeliveryAddress() != null)
			{

				mobileNumber = StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()) ? orderModel.getDeliveryAddress()
						.getPhone1() : (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone2()) ? orderModel
						.getDeliveryAddress().getPhone2() : orderModel.getDeliveryAddress().getCellphone());
			}

			// Notifications: Out for Delivery : Email & SMS
			if (StringUtils.isNotEmpty(awbSecondaryStatus)
					&& MarketplacecommerceservicesConstants.OFD.equalsIgnoreCase(awbSecondaryStatus))
			{
				LOG.info("******************** Sending notification for OUT FOR DELIVERY");
				sendNotificationSecondaryOFD(orderModel, orderNumber, mobileNumber, orderLineId);
			}
			if (StringUtils.isNotEmpty(awbSecondaryStatus)
					&& MarketplacecommerceservicesConstants.ADDRESS_ISSUE.equalsIgnoreCase(awbSecondaryStatus))
			{
				LOG.info("******************** Sending notification for AddressIssue");
				sendNotificationSecondaryAddressIssue(orderModel, orderNumber, mobileNumber, orderLineId);
			}

		}
		catch (final Exception e)
		{
			LOG.error("******************** ERROR Sending notification " + e.getMessage());
		}
	}


	private void sendNotificationSecondaryAddressIssue(final OrderModel orderModel, final String orderNumber,
			final String mobileNumber, final String orderLineID)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(UPDATE_CONSIGNMENT + MarketplacecommerceservicesConstants.ADDRESS_ISSUE);
		}
		try
		{

			final List<AbstractOrderEntryModel> childOrders = orderModel.getEntries();
			//sending SMS
			LOG.info("********* Sending SMS for Address Issue ");
			sendSMSAddressIssue(childOrders, orderNumber, mobileNumber, orderModel);
			LOG.info("******************* SMS sent");
			//sending Email
			LOG.info("********* Sending Email for Address Issue ");
			sendEmailAddressIssue(orderModel, childOrders, orderLineID);


		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS and EMAIL  notification -  " + "\n" + e.getMessage());
		}
	}




	/**
	 * This method is responsible for sending Email Notification for Out for delivery
	 *
	 * @param orderModel
	 * @param childOrders
	 */
	private void sendEmailAddressIssue(final OrderModel orderModel, final List<AbstractOrderEntryModel> childOrders,
			final String orderLineID)
	{
		LOG.info("*******Before checking isToSendNotification for OUT FOR DELIVERY Email********");

		LOG.info("Starting email process:::::");
		final OrderUpdateProcessModel orderUpdateProcessModel = (OrderUpdateProcessModel) getBusinessProcessService()
				.createProcess("orderDeliveryAddressIssueEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
						"orderDeliveryAddressIssueEmailProcess");

		final List<String> entryNumber = new ArrayList<String>();
		orderUpdateProcessModel.setOrder(orderModel);
		entryNumber.add(orderLineID);
		orderUpdateProcessModel.setEntryNumber(entryNumber);
		modelService.saveAll(orderUpdateProcessModel);
		businessProcessService.startProcess(orderUpdateProcessModel);

	}



	/**
	 * @description Method to form SMS template for sending AddressIssue Notification
	 * @param childOrders
	 * @param orderNumber
	 * @param mobileNumber
	 */

	private void sendSMSAddressIssue(final List<AbstractOrderEntryModel> childOrders, final String orderNumber,
			final String mobileNumber, final OrderModel orderModel)
	{
		try
		{
			final SendSMSRequestData smsRequestDataOutForDeliveryCOD = new SendSMSRequestData();
			smsRequestDataOutForDeliveryCOD.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataOutForDeliveryCOD.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ADDRESS_ISSUE.replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(1)).replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber));
			smsRequestDataOutForDeliveryCOD.setRecipientPhoneNumber(mobileNumber);

			LOG.info("*******Before checking isToSendNotification for MESSAGE ADDRESS ISSUE SMS********");
			LOG.info("*******After checking isToSendNotification for MESSAGE ADDRESS ISSUE SMS********");

			final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
					.createProcess("outForDeliverySmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
							"outForDeliverySmsProcess");
			orderUpdateSmsProcessModel.setOrder(orderModel);
			orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_ADDRESS_ISSUE.replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(1)).replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber));
			orderUpdateSmsProcessModel.setRecipientPhoneNumber(mobileNumber);
			final List<String> entryNumber = new ArrayList<String>();
			for (final AbstractOrderEntryModel child : childOrders)
			{
				entryNumber.add(child.getEntryNumber().toString());
			}
			orderUpdateSmsProcessModel.setEntryNumber(entryNumber);
			modelService.save(orderUpdateSmsProcessModel);
			businessProcessService.startProcess(orderUpdateSmsProcessModel);


		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS OutForDeliveryCOD>>>" + e.getMessage());
		}


	}


	/**
	 * This method is responsible for sending Notification for Secondary Status For Out for delivery
	 *
	 * @param orderModel
	 * @param orderNumber
	 * @param mobileNumber
	 *
	 */
	private void sendNotificationSecondaryOFD(final OrderModel orderModel, final String orderNumber, final String mobileNumber,
			final String orderLineID)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(UPDATE_CONSIGNMENT + MarketplacecommerceservicesConstants.ORDER_STATUS_OFD);
		}
		try
		{
			final List<AbstractOrderEntryModel> childOrders = orderModel.getEntries();
			//sending SMS
			LOG.info("********* Sending SMS for Out for delivery ");
			sendSMSOFD(childOrders, orderNumber, mobileNumber, orderModel);
			LOG.info("******************* SMS sent");
			//sending Email
			LOG.info("********* Sending Email for Out for delivery ");
			sendEmailOFD(orderModel, childOrders, orderLineID);


		}
		catch (final Exception e)
		{

			LOG.error("Exception during sending SMS and EMAIL HOTC notification -  " + "\n" + e.getMessage());
		}
	}


	/**
	 * @description Method to form SMS template for sending OutForDelivery Notification
	 * @param childOrders
	 * @param orderNumber
	 * @param mobileNumber
	 */

	private void sendSMSOFD(final List<AbstractOrderEntryModel> childOrders, final String orderNumber, final String mobileNumber,
			final OrderModel orderModel)
	{
		try
		{
			final SendSMSRequestData smsRequestDataOutForDeliveryCOD = new SendSMSRequestData();
			smsRequestDataOutForDeliveryCOD.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataOutForDeliveryCOD.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_OUT_FOR_DELIVERY.replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size())).replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber));


			final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
					.createProcess("outForDeliverySmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
							"outForDeliverySmsProcess");
			orderUpdateSmsProcessModel.setOrder(orderModel);
			orderUpdateSmsProcessModel.setShipmentStatus(ConsignmentStatus.OUT_FOR_DELIVERY);
			orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_OUT_FOR_DELIVERY.replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size())).replace(
					MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber));
			orderUpdateSmsProcessModel.setRecipientPhoneNumber(mobileNumber);
			final List<String> entryNumber = new ArrayList<String>();
			for (final AbstractOrderEntryModel child : childOrders)
			{
				entryNumber.add(child.getEntryNumber().toString());
			}
			orderUpdateSmsProcessModel.setEntryNumber(entryNumber);

			modelService.save(orderUpdateSmsProcessModel);
			businessProcessService.startProcess(orderUpdateSmsProcessModel);


		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS OutForDeliveryCOD>>>" + e.getMessage());
		}

	}



	/**
	 * This method is responsible for sending Email Notification for Out for delivery
	 *
	 * @param orderModel
	 * @param childOrders
	 */
	private void sendEmailOFD(final OrderModel orderModel, final List<AbstractOrderEntryModel> childOrders,
			final String orderLineID)
	{
		LOG.info("*******Before checking isToSendNotification for OUT FOR DELIVERY Email********");

		LOG.info("Starting email process:::::");
		final OrderUpdateProcessModel orderUpdateProcessModel = (OrderUpdateProcessModel) getBusinessProcessService()
				.createProcess("orderOutForDeliveryEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
						"orderOutForDeliveryEmailProcess");
		orderUpdateProcessModel.setOrder(orderModel);
		orderUpdateProcessModel.setShipmentStatus(ConsignmentStatus.OUT_FOR_DELIVERY);
		final List<String> entryNumber = new ArrayList<String>();
		entryNumber.add(orderLineID);
		orderUpdateProcessModel.setEntryNumber(entryNumber);
		modelService.saveAll(orderUpdateProcessModel);
		businessProcessService.startProcess(orderUpdateProcessModel);

	}






	public List<OrderUpdateProcessModel> checkEmailSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
	{

		return emailAndSmsNotification.checkEmailSent(awbNumber, shipmentStatus);

	}

	public List<OrderUpdateSmsProcessModel> checkSmsSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
	{

		return emailAndSmsNotification.checkSmsSent(awbNumber, shipmentStatus);

	}





}
