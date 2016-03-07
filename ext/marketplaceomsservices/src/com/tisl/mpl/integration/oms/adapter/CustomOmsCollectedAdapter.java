/**
 * 
 */
package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.marketplaceomsservices.daos.EmailAndSmsNotification;

/**
 * @author Tech
 *
 */
public class CustomOmsCollectedAdapter
{
	private static final Logger LOG = Logger.getLogger(CustomOmsCollectedAdapter.class);
	
	@Resource(name = "emailAndSmsNotification")
	private EmailAndSmsNotification emailAndSmsNotification;
	
	@Autowired
	private BusinessProcessService businessProcessService;
	
	@Autowired
	private ModelService modelService;
	
	private static final String UPDATE_CONSIGNMENT = "updateConsignment:: Inside ";
	
	public void sendNotificationForDelivery(final OrderModel orderModel, final String orderNumber, final String mobileNumber,
			final String appDwldUrl)
	{
		
		LOG.debug(UPDATE_CONSIGNMENT + MarketplaceomsordersConstants.ORDER_STATUS_COLLECTED);
		String awbNumber = "";
		try
		{

			final Map<String, List<AbstractOrderEntryModel>> mapAWB = updateAWBNumber(orderModel, ConsignmentStatus.ORDER_COLLECTED);
			for (final Map.Entry<String, List<AbstractOrderEntryModel>> entry : mapAWB.entrySet())
			{
				awbNumber = entry.getKey();
				final List<AbstractOrderEntryModel> childOrders = entry.getValue();

				//sending SMS
				LOG.info("****************************Sending SMS for Order COLLECTED ");
				sendSMSDelivered(orderModel, childOrders, orderNumber, mobileNumber, appDwldUrl, awbNumber);


				//Send EMAIL
				LOG.info("****************************Sending Email for Order COLLECTED ");
				sendEmailDelivered(orderModel, childOrders, awbNumber);


			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS and EMAIL COLLECTED notification -  " + awbNumber + "\n" + e.getMessage());
		}

	}
	
	private Map<String, List<AbstractOrderEntryModel>> updateAWBNumber(final OrderModel orderModel,
			final ConsignmentStatus shipmentStatus)
	{
		final Map<String, List<AbstractOrderEntryModel>> aWPwiseOrderLines = new HashMap<String, List<AbstractOrderEntryModel>>();
		List<AbstractOrderEntryModel> orderLineList = null;

		for (final AbstractOrderEntryModel childOrder : orderModel.getEntries())
		{
			final Set<ConsignmentEntryModel> consignmentEntries = childOrder.getConsignmentEntries();
			for (final ConsignmentEntryModel consignment : consignmentEntries)
			{
				final ConsignmentModel consignModel = consignment.getConsignment();
				if (StringUtils.isEmpty(consignModel.getTrackingID()))
				{
					LOG.debug(">>>>>>>>>>>>>consignment.getOrderEntry().getTransactionID()<<<<<<<<<<<<<:::::"
							+ consignment.getOrderEntry().getTransactionID());
					continue;
				}

				if (null != aWPwiseOrderLines.get(consignModel.getTrackingID())
						&& !aWPwiseOrderLines.get(consignModel.getTrackingID()).isEmpty() && consignModel.getStatus() == shipmentStatus)//if AWB already exists
				{
					orderLineList = aWPwiseOrderLines.get(consignModel.getTrackingID());
					orderLineList.add(childOrder);
					aWPwiseOrderLines.put(consignModel.getTrackingID(), orderLineList);


				}
				else if (consignModel.getStatus() == shipmentStatus)
				//if AWB does not exist
				{
					orderLineList = new ArrayList<AbstractOrderEntryModel>();
					orderLineList.add(childOrder);
					aWPwiseOrderLines.put(consignModel.getTrackingID(), orderLineList);
				}
			}
		}
		return aWPwiseOrderLines;
	}
	/**
	 * @description Method to form SMS template for sending Delivered Notification
	 * @param childOrders
	 * @param orderNumber
	 * @param mobileNumber
	 * @param appDwldUrl
	 */
	private void sendSMSDelivered(final OrderModel orderModel, final List<AbstractOrderEntryModel> childOrders,
			final String orderNumber, final String mobileNumber, final String appDwldUrl, final String awbNumber)
	{
		try
		{
			final SendSMSRequestData smsRequestDataOrderDelivered = new SendSMSRequestData();

			smsRequestDataOrderDelivered.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
			smsRequestDataOrderDelivered.setContent(MarketplaceomsordersConstants.SMS_MESSAGE_ORDER_DELIVERY
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE, orderNumber)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO, appDwldUrl));
			smsRequestDataOrderDelivered.setRecipientPhoneNumber(mobileNumber);

			// Checking of SMS Notification previously sent or not
			final List<OrderUpdateSmsProcessModel> orderUpdateSmsModelList = checkSmsSent(awbNumber, ConsignmentStatus.DELIVERED);
			int numOfRows = 0;
			//int totalEntries = 0;

			if (null != orderUpdateSmsModelList && !orderUpdateSmsModelList.isEmpty())
			{
				numOfRows = orderUpdateSmsModelList.size();
				//final List<String> entryList = orderUpdateSmsModelList.get(0).getEntryNumber();
				//totalEntries = entryList.size();
			}
			LOG.info("*******Before checking isToSendNotification for DELIVERED SMS********");
			final boolean flag = isToSendNotification(awbNumber, orderModel, ConsignmentStatus.DELIVERED);
			LOG.info("*******After checking isToSendNotification for DELIVERED SMS******** SMS sent ? " + flag);
			LOG.info("No of Rows:::::sendSMSDelivered" + numOfRows);
			if (numOfRows == 0 && flag)
			{

				final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
						.createProcess("orderDeliverySmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
								"orderDeliverySmsProcess");
				orderUpdateSmsProcessModel.setOrder(orderModel);
				orderUpdateSmsProcessModel.setAwbNumber(awbNumber);
				orderUpdateSmsProcessModel.setShipmentStatus(ConsignmentStatus.DELIVERED);
				orderUpdateSmsProcessModel.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
				orderUpdateSmsProcessModel.setMessage(MarketplaceomsordersConstants.SMS_MESSAGE_ORDER_DELIVERY
						.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
						.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE, orderNumber)
						.replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO, appDwldUrl));
				orderUpdateSmsProcessModel.setRecipientPhoneNumber(mobileNumber);
				final List<String> entryNumber = new ArrayList<String>();
				for (final AbstractOrderEntryModel child : childOrders)
				{
					entryNumber.add(child.getEntryNumber().toString());
				}
				orderUpdateSmsProcessModel.setEntryNumber(entryNumber);

				modelService.save(orderUpdateSmsProcessModel);
				businessProcessService.startProcess(orderUpdateSmsProcessModel);
				//sendSMSService.sendSMS(smsRequestDataOrderDelivered);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS for DELIVERY >> " + e.getMessage());
		}
	}
	
	/**
	 * @param awbNumber
	 * @param orderModel
	 * @return boolean
	 */
	private boolean isToSendNotification(final String awbNumber, final OrderModel orderModel, final ConsignmentStatus status)
	{
		int sameAWBRows = 0;
		int sameAWBStatus = 0;
		boolean sendNotification = false;
		for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
		{

			if (awbNumber.equals(orderEntry.getConsignmentEntries().iterator().next().getConsignment().getTrackingID()))
			{
				++sameAWBRows;
				if (status.equals(orderEntry.getConsignmentEntries().iterator().next().getConsignment().getStatus()))
				{
					++sameAWBStatus;
				}
			}
		}

		if (sameAWBRows == sameAWBStatus)
		{
			sendNotification = true;
		}


		LOG.info("****** send notification for awbNumber :" + awbNumber + " - sameAWBRows:" + sameAWBRows + " - sameAWBStatus:"
				+ sameAWBStatus);

		return sendNotification;
	}
	/**
	 * @description Method for sending Delivered Email Notification
	 * @param orderModel
	 * @param childOrders
	 * @param awbNumber
	 */
	private void sendEmailDelivered(final OrderModel orderModel, final List<AbstractOrderEntryModel> childOrders,
			final String awbNumber)
	{
		// Checking of SMS Notification previously sent or not
		final List<OrderUpdateProcessModel> orderUpdateModelList = checkEmailSent(awbNumber, ConsignmentStatus.DELIVERED);
		int numOfRows = 0;
		//int totalEntries = 0;
		if (null != orderUpdateModelList && !orderUpdateModelList.isEmpty())
		{
			numOfRows = orderUpdateModelList.size();
			//final List<String> entryList = orderUpdateModelList.get(0).getEntryNumber();
			//totalEntries = entryList.size();
		}
		LOG.info("*******Before checking isToSendNotification for DELIVERED EMAIL********");
		final boolean flag = isToSendNotification(awbNumber, orderModel, ConsignmentStatus.DELIVERED);
		LOG.info("*******After checking isToSendNotification for DELIVERED EMAIL********");
		LOG.info("No of Rows:::::sendEmailDelivered" + numOfRows);
		if (numOfRows == 0 && flag)
		{
			final OrderUpdateProcessModel orderUpdateProcessModel = (OrderUpdateProcessModel) getBusinessProcessService()
					.createProcess("orderDeliveryEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
							"orderDeliveryEmailProcess");
			orderUpdateProcessModel.setOrder(orderModel);
			orderUpdateProcessModel.setAwbNumber(awbNumber);
			orderUpdateProcessModel.setShipmentStatus(ConsignmentStatus.DELIVERED);

			final List<String> entryNumber = new ArrayList<String>();
			for (final AbstractOrderEntryModel child : childOrders)
			{
				entryNumber.add(child.getEntryNumber().toString());
			}

			orderUpdateProcessModel.setEntryNumber(entryNumber);
			modelService.save(orderUpdateProcessModel);
			businessProcessService.startProcess(orderUpdateProcessModel);
		}

	}
	
	public List<OrderUpdateProcessModel> checkEmailSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
	{

		return emailAndSmsNotification.checkEmailSent(awbNumber, shipmentStatus);

	}
	
	public List<OrderUpdateSmsProcessModel> checkSmsSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
	{

		return emailAndSmsNotification.checkSmsSent(awbNumber, shipmentStatus);

	}

	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService the businessProcessService to set
	 */
	public void setBusinessProcessService(BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
}
