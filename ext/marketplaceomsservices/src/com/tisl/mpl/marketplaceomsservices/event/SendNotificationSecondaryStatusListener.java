/**
 * 
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.shipping.Shipment;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.marketplaceomsservices.daos.EmailAndSmsNotification;
import com.tisl.mpl.shorturl.service.ShortUrlService;
import com.tisl.mpl.sms.MplSendSMSService;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;

/**
 * @author pankajk
 *
 */
public class SendNotificationSecondaryStatusListener extends AbstractSiteEventListener<SendNotificationSecondaryStatusEvent>
	{
		private ModelService modelService;

		@Autowired
		private MplSendSMSService sendSMSService;

		private ConfigurationService configurationService;
		@Autowired
		private MplSNSMobilePushServiceImpl mplSNSMobilePushService;

		@Resource(name = "emailAndSmsNotification")
		private EmailAndSmsNotification emailAndSmsNotification;

		@Resource(name = "googleShortUrlService")
		private ShortUrlService googleShortUrlService;

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
			sendNotification(event.getShipment(), event.getConsignmentModel(), event.getOrderModel(), event.getSecondaryShipmentNewStatus());
			return SiteChannel.B2C.equals(site.getChannel());
		}

		/**
		 * This method is responsible for sending Order Status related Notification
		 *
		 * @param shipment
		 * @param consignmentModel
		 * @param orderModel
		 * @param secondaryShipmentNewStatus
		 *
		 */
		private void sendNotification(final Shipment shipment, final ConsignmentModel consignmentModel, final OrderModel orderModel,
				final ConsignmentStatus secondaryShipmentNewStatus)
		{

			try
			{
			
				LOG.info("*************Inside sendNotification *******************");
				 String orderNumber =(StringUtils.isEmpty(shipment.getOrderId())) ? MarketplacecommerceservicesConstants.EMPTY
						: shipment.getOrderId();
              String mobileNumber =(StringUtils.isEmpty(shipment.getDelivery().getDeliveryAddress().getPhoneNumber())) ? MarketplacecommerceservicesConstants.EMPTY
  						: shipment.getDelivery().getDeliveryAddress().getPhoneNumber();
				
				
		    	// Notifications: Out for Delivery : Email & SMS
				if (StringUtils.isNotEmpty(secondaryShipmentNewStatus.toString()) && shipment.getAwbSecondaryStatus().toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_STATUS_OFD))
				{
					LOG.info("******************** Sending notification for OUT FOR DELIVERY");
					sendNotificationSecondaryOFD(orderModel, orderNumber,mobileNumber);
				}

			}
			catch (final Exception e)
			{
				LOG.error("******************** ERROR Sending notification " + e.getMessage());
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
		private void sendNotificationSecondaryOFD(final OrderModel orderModel, final String orderNumber,String mobileNumber)
		{
			if(LOG.isDebugEnabled()){
				LOG.debug(UPDATE_CONSIGNMENT + MarketplacecommerceservicesConstants.ORDER_STATUS_OFD);
			}
			String awbNumber = "";
			try
			{
				final Map<String, List<AbstractOrderEntryModel>> mapAWB = updateAWBNumber(orderModel, ConsignmentStatus.OUT_FOR_DELIVERY);
				for (final Map.Entry<String, List<AbstractOrderEntryModel>> entry : mapAWB.entrySet())
				{
					awbNumber = entry.getKey();
					final List<AbstractOrderEntryModel> childOrders = entry.getValue();
					//sending SMS
					LOG.info("********* Sending SMS for Out for delivery ");
					sendSMSOFD(childOrders, orderNumber, mobileNumber, awbNumber, orderModel);
					LOG.info("******************* SMS sent");
					 //sending Email
					LOG.info("********* Sending Email for Out for delivery ");
					sendEmailOFD(orderModel, childOrders, awbNumber);

				}
			}
			catch (final Exception e)
			{

				LOG.error("Exception during sending SMS and EMAIL HOTC notification -  " + awbNumber + "\n" + e.getMessage());
			}
		}
		

		/**
		 * @description Method to form SMS template for sending OutForDelivery Notification
		 * @param childOrders
		 * @param orderNumber
		 * @param mobileNumber
		 */

		private void sendSMSOFD(final List<AbstractOrderEntryModel> childOrders, final String orderNumber,
				final String mobileNumber, final String awbNumber, final OrderModel orderModel)
		{
			try
			{
				final SendSMSRequestData smsRequestDataOutForDeliveryCOD = new SendSMSRequestData();
				smsRequestDataOutForDeliveryCOD.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				smsRequestDataOutForDeliveryCOD.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_OUT_FOR_DELIVERY
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber));
				smsRequestDataOutForDeliveryCOD.setRecipientPhoneNumber(mobileNumber);
				final List<OrderUpdateSmsProcessModel> orderUpdateSmsModelList = checkSmsSent(awbNumber,
						ConsignmentStatus.OUT_FOR_DELIVERY);
				int numOfRows = 0;
				if (null != orderUpdateSmsModelList && !orderUpdateSmsModelList.isEmpty())
				{
					numOfRows = orderUpdateSmsModelList.size();
				}
				LOG.info("*******Before checking isToSendNotification for OUTFORDELIVERY SMS********");
				final boolean flag = isToSendNotification(awbNumber, orderModel, ConsignmentStatus.OUT_FOR_DELIVERY);
				LOG.info("*******After checking isToSendNotification for OUTFORDELIVERY SMS********");
				LOG.info("No of Rows:::::" + numOfRows);
				double totalAmount = 0.0;
				for (final AbstractOrderEntryModel child : childOrders)
				{
					totalAmount = totalAmount + child.getCurrDelCharge().doubleValue() + child.getNetAmountAfterAllDisc().doubleValue()
							+ child.getConvenienceChargeApportion().doubleValue();
				}
				if (numOfRows == 0 && flag)
				{

					final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
							.createProcess("outForDeliverySmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
									"outForDeliverySmsProcess");
					orderUpdateSmsProcessModel.setOrder(orderModel);
					orderUpdateSmsProcessModel.setAwbNumber(awbNumber);
					orderUpdateSmsProcessModel.setShipmentStatus(ConsignmentStatus.OUT_FOR_DELIVERY);
					orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
					orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_OUT_FOR_DELIVERY
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber));
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
		 * @param awbNumber
		 */
		private void sendEmailOFD(final OrderModel orderModel, final List<AbstractOrderEntryModel> childOrders, final String awbNumber)
		{
			final List<OrderUpdateProcessModel> orderUpdateModelList = checkEmailSent(awbNumber, ConsignmentStatus.OUT_FOR_DELIVERY);
			int numOfRows = 0;
			//int totalEntries = 0;
			if (null != orderUpdateModelList && !orderUpdateModelList.isEmpty())
			{
				numOfRows = orderUpdateModelList.size();
			}


			LOG.info("*******Before checking isToSendNotification for OUT FOR DELIVERY Email********");
			final boolean flag = true;
			LOG.info("*******After checking isToSendNotification for OUT FOR DELIVERY Email********");
			LOG.info("No of Rows:::::sendEmailOFD" + numOfRows);
			if (numOfRows == 0 && flag)
			{
				LOG.info("Starting email process:::::");
				final OrderUpdateProcessModel orderUpdateProcessModel = (OrderUpdateProcessModel) getBusinessProcessService()
						.createProcess("orderOutForDeliveryEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
								"orderOutForDeliveryEmailProcess");
				orderUpdateProcessModel.setOrder(orderModel);
				orderUpdateProcessModel.setAwbNumber(awbNumber);
				orderUpdateProcessModel.setShipmentStatus(ConsignmentStatus.OUT_FOR_DELIVERY);
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
					LOG.info(">>>>>>>>>>>>>>>>Tracking Id>>>>>>>>>>>>>>>>>" + consignModel.getCode() + " " + consignModel.getTrackingID());
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

		public List<OrderUpdateProcessModel> checkEmailSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
		{

			return emailAndSmsNotification.checkEmailSent(awbNumber, shipmentStatus);

		}

		public List<OrderUpdateSmsProcessModel> checkSmsSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
		{

			return emailAndSmsNotification.checkSmsSent(awbNumber, shipmentStatus);

		}

		
		/**
		 * @param awbNumber
		 * @param orderModel
		 * @param Out For Delivery
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

		

	}
