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
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.shipping.Shipment;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.marketplaceomsservices.daos.EmailAndSmsNotification;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.shorturl.service.ShortUrlService;
import com.tisl.mpl.sms.MplSendSMSService;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;
import com.tisl.mpl.wsdto.PushNotificationData;


/**
 * @author TCS
 *
 */
public class SendNotificationEventListener extends AbstractSiteEventListener<SendNotificationEvent>
{
	private ModelService modelService;

	//	@Autowired
	//	private EventService eventService;

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
	protected void onSiteEvent(final SendNotificationEvent sendNotificationEvent)
	{
		LOG.info("Inside onSiteEvent");
		//sendNotification(sendNotificationEvent.getShipment(), sendNotificationEvent.getConsignmentModel(),
		//sendNotificationEvent.getOrderModel(), sendNotificationEvent.getShipmentNewStatus());
	}


	@Override
	protected boolean shouldHandleEvent(final SendNotificationEvent event)
	{
		LOG.info("*************Inside shouldHandleEvent *******************");
		final OrderModel order = event.getOrderModel();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		LOG.info("Inside shouldHandleEvent Channel: " + site.getChannel());
		LOG.info("Sending notification >>>>: ");
		sendNotification(event.getShipment(), event.getConsignmentModel(), event.getOrderModel(), event.getShipmentNewStatus());
		return SiteChannel.B2C.equals(site.getChannel());
	}

	/**
	 * This method is responsible for sending Order Status related Notification
	 *
	 * @param shipment
	 * @param consignmentModel
	 * @param orderModel
	 * @param shipmentNewStatus
	 *
	 */
	private void sendNotification(final Shipment shipment, final ConsignmentModel consignmentModel, final OrderModel orderModel,
			final ConsignmentStatus shipmentNewStatus)
	{
		String mobileNumber;
		String firstName;
		String storeName = null;
		String deliverdDate = null;
		String pickUpMobileNumber = null;
		String pickUpPersonName = null;

		LOG.info("*************Inside sendNotification *******************");
		final String orderNumber = (StringUtils.isEmpty(shipment.getOrderId())) ? MarketplacecommerceservicesConstants.EMPTY
				: shipment.getOrderId();
		final String trackingUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
				+ orderModel.getCode();
		if (shipment.getDelivery() != null && shipment.getDelivery().getDeliveryAddress() != null)
		{
			mobileNumber = (StringUtils.isEmpty(shipment.getDelivery().getDeliveryAddress().getPhoneNumber())) ? MarketplacecommerceservicesConstants.EMPTY
					: shipment.getDelivery().getDeliveryAddress().getPhoneNumber();
		}
		else
		{
			mobileNumber = orderModel.getPickupPersonMobile();
		}
		final String codAmount = (null != orderModel.getConvenienceCharges()) ? String.valueOf(orderModel.getConvenienceCharges())
				: MarketplacecommerceservicesConstants.ZERO;
		final String appDwldUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_APP_DWLD_URL);
		if (orderModel.getDeliveryAddress() != null)
		{
			firstName = orderModel.getDeliveryAddress().getFirstname();
		}
		else
		{
			firstName = orderModel.getPickupPersonName();
		}

		final String contactNumber = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
		final String logisticPartner = (StringUtils.isEmpty(consignmentModel.getCarrier())) ? MarketplacecommerceservicesConstants.SPACE
				: consignmentModel.getCarrier();
		if (shipmentNewStatus.toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED))
		{
		  for (final AbstractOrderEntryModel orderEntryModel : orderModel.getEntries())
		  {
			if (shipment.getShipmentId().equals(orderEntryModel.getTransactionID())
					&& consignmentModel.getCode().equals(orderEntryModel.getTransactionID()))
			{


				/*
				 * storeName =(StringUtils.isEmpty(orderEntryModel.getDeliveryPointOfService().getDisplayName())) ?
				 * MarketplacecommerceservicesConstants.EMPTY :
				 * orderEntryModel.getDeliveryPointOfService().getDisplayName();
				 */
				storeName = (orderEntryModel.getDeliveryPointOfService() != null && StringUtils.isNotEmpty(orderEntryModel
						.getDeliveryPointOfService().getDisplayName())) ? orderEntryModel.getDeliveryPointOfService().getDisplayName()
						: MarketplacecommerceservicesConstants.EMPTY;
				final DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
				final Date currentDate = new Date();
				if (null != consignmentModel.getDeliveryDate())
				{
					final String formatDate = dateFormatter.format(consignmentModel.getDeliveryDate());
					deliverdDate = (StringUtils.isEmpty(formatDate)) ? dateFormatter.format(currentDate) : formatDate;
				}
				else
				{
					deliverdDate = dateFormatter.format(currentDate);
				}

				pickUpMobileNumber = (StringUtils.isEmpty(orderModel.getPickupPersonMobile())) ? MarketplaceomsordersConstants.EMPTY
						: orderModel.getPickupPersonMobile();

				pickUpPersonName = (StringUtils.isEmpty(orderModel.getPickupPersonName())) ? MarketplaceomsordersConstants.CUSTOMER_NAME
						: orderModel.getPickupPersonName();

			}

		}
		}



		LOG.info("*************checking multiple if *******************");
		try
		{

			// Notifications: OUT FOR DELIVERY : SMS & PUSH
			if (shipmentNewStatus.toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_STATUS_OFD))
			{
				LOG.info("******************** Sending notification for OUT FOR DELIVERY");
				sendNotificationOutForDelivery(consignmentModel, orderModel, orderNumber, mobileNumber, codAmount, appDwldUrl);
			}
			// Notifications: HOTC : Email & SMS
			//Bug Id TISRLUAT-986 20-02-2017 Start
			if (shipment.getInScan() != null)
			{
				if (shipment.getInScan().booleanValue())
				{
					LOG.info("******************** Sending notification for HOTC");
					sendNotificationForHotc(orderModel, orderNumber, mobileNumber, trackingUrl, logisticPartner,shipmentNewStatus);
				}
			}
			//Bug Id TISRLUAT-986 20-02-2017 End
			
			// Notifications: DELIVERED : Email & SMS
			if (shipmentNewStatus.toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_STATUS_DELIVERED))
			{
				LOG.info("******************** Sending notification for DELIVERED");
				sendNotificationForDelivery(orderModel, orderNumber, mobileNumber, appDwldUrl);
			}

			// Notifications: UNDELIVERED : SMS
			// No need to trigger the email and SMS for order status is UNDELIVERED PRDI-209
			
			/*if (shipmentNewStatus.toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_STATUS_UNDELIVERED))
			{
				boolean flag = true;
				if (MarketplacecommerceservicesConstants.ADDRESS_ISSUE.equalsIgnoreCase(shipment.getAwbSecondaryStatus()))
				{
					flag = false;
				}
				LOG.info("******************** Sending notification for UNDELIVERED");
				if (flag)
				{
					sendNotificationForUndelivered(orderModel, orderNumber, mobileNumber, contactNumber, firstName);
				}
			}*/

			// Notifications: ORDERCOLLETED : SMS
			if (shipmentNewStatus.toString().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED))
			{
				LOG.info("******************** Sending notification for ORDER COLLECTED");
				sendNotificationForCNCOrderColleted(orderNumber, mobileNumber, contactNumber, firstName, storeName, deliverdDate,
						pickUpMobileNumber, pickUpPersonName);
			}

			LOG.info("*************End of method*******************");

		}
		catch (final Exception e)
		{
			LOG.error("******************** ERROR Sending notification " + e.getMessage());
		}
	}

	/**
	 * @description Method to form SMS template for sending OutForDeliveryCOD Notification
	 * @param childOrders
	 * @param orderNumber
	 * @param mobileNumber
	 * @param codAmount
	 * @param appDwldUrl
	 */

	private void sendSMSOutForDeliveryCOD(final List<AbstractOrderEntryModel> childOrders, final String orderNumber,
			final String mobileNumber, String codAmount, final String appDwldUrl, final String awbNumber, final OrderModel orderModel)
	{
		try
		{
			final SendSMSRequestData smsRequestDataOutForDeliveryCOD = new SendSMSRequestData();
			smsRequestDataOutForDeliveryCOD.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataOutForDeliveryCOD.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_OUTFORDELIVERY_COD
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, codAmount)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE, appDwldUrl));
			smsRequestDataOutForDeliveryCOD.setRecipientPhoneNumber(mobileNumber);
			final List<OrderUpdateSmsProcessModel> orderUpdateSmsModelList = checkSmsSent(awbNumber,
					ConsignmentStatus.OUT_FOR_DELIVERY);
			int numOfRows = 0;
			if (null != orderUpdateSmsModelList && !orderUpdateSmsModelList.isEmpty())
			{
				numOfRows = orderUpdateSmsModelList.size();
				//final List<String> entryList = orderUpdateSmsModelList.get(0).getEntryNumber();
				//totalEntries = entryList.size();
			}
			LOG.info("*******Before checking isToSendNotification for OUTFORDELIVERY SMS********");
			final boolean flag = isToSendNotification(awbNumber, orderModel, ConsignmentStatus.OUT_FOR_DELIVERY);
			LOG.info("*******After checking isToSendNotification for OUTFORDELIVERY SMS********");
			LOG.info("No of Rows:::::sendSMSOutForDeliveryCOD" + numOfRows);
			double totalAmount = 0.0;
			for (final AbstractOrderEntryModel child : childOrders)
			{
				totalAmount = totalAmount + child.getCurrDelCharge().doubleValue() + child.getNetAmountAfterAllDisc().doubleValue()
						+ child.getConvenienceChargeApportion().doubleValue();
			}
			codAmount = String.valueOf(totalAmount);
			if (numOfRows == 0 && flag)
			{

				final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
						.createProcess("outForDeliverySmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
								"outForDeliverySmsProcess");
				orderUpdateSmsProcessModel.setOrder(orderModel);
				orderUpdateSmsProcessModel.setAwbNumber(awbNumber);
				orderUpdateSmsProcessModel.setShipmentStatus(ConsignmentStatus.OUT_FOR_DELIVERY);
				orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_OUTFORDELIVERY_COD
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, codAmount)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE, appDwldUrl));
				orderUpdateSmsProcessModel.setRecipientPhoneNumber(mobileNumber);
				final List<String> entryNumber = new ArrayList<String>();
				for (final AbstractOrderEntryModel child : childOrders)
				{
					entryNumber.add(child.getEntryNumber().toString());
					//child.getCurrDelCharge()+child.getNetAmountAfterAllDisc()+child.getConvenienceChargeApportion();
				}
				orderUpdateSmsProcessModel.setEntryNumber(entryNumber);

				modelService.save(orderUpdateSmsProcessModel);
				businessProcessService.startProcess(orderUpdateSmsProcessModel);
			}
			//sendSMSService.sendSMS(smsRequestDataOutForDeliveryCOD);

		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS OutForDeliveryCOD>>>" + e.getMessage());
		}

	}

	/**
	 * @description Method to form push Notification for OutForDeliveryCOD
	 * @param childOrders
	 * @param orderNumber
	 */

	private void sendPushNotificationOutForDeliveryCOD(final List<AbstractOrderEntryModel> childOrders, final String codAmount,
			final String orderNumber, final OrderModel orderModel)
	{

		CustomerModel customer = getModelService().create(CustomerModel.class);
		if (null != orderModel && null != orderModel.getUser() && null != orderModel.getUser().getUid()
				&& !orderModel.getUser().getUid().isEmpty())
		{
			customer = mplSNSMobilePushService.getCustForUId(orderModel.getUser().getUid());
			if (null != customer && null != customer.getDeviceKey() && !customer.getDeviceKey().isEmpty()
					&& null != customer.getOriginalUid() && !customer.getOriginalUid().isEmpty() && null != customer.getIsActive()
					&& !customer.getIsActive().isEmpty() && customer.getIsActive().equalsIgnoreCase("Y"))
			{

				//private method created to avoid sonar issue
				//setPushData(customer, orderNumber, childOrders);

				final PushNotificationData pushData = new PushNotificationData();
				pushData.setMessage(MarketplacecommerceservicesConstants.PUSH_MESSAGE_ORDER_OFD_COD
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, String.valueOf(childOrders.size()))
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, codAmount));
				if (null != orderNumber && !orderNumber.isEmpty())
				{
					pushData.setOrderId(orderNumber);
				}
				mplSNSMobilePushService.setUpNotification(customer.getOriginalUid(), pushData);

			}
		}

	}

	/**
	 * @param customer
	 * @param orderNumber
	 * @param childOrders
	 */
	/*
	 * private void setPushData(final CustomerModel customer, final String orderNumber, final
	 * List<AbstractOrderEntryModel> childOrders) { final PushNotificationData pushData = new PushNotificationData();
	 * pushData.setMessage(MarketplacecommerceservicesConstants.PUSH_MESSAGE_ORDER_OFD_PREPAID.replace(
	 * MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderNumber).replace(
	 * MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, String.valueOf(childOrders.size()))); if (null !=
	 * orderNumber && !orderNumber.isEmpty()) { pushData.setOrderId(orderNumber); }
	 * mplSNSMobilePushService.setUpNotification(customer.getOriginalUid(), pushData);
	 *
	 *
	 * }
	 */

	/**
	 * @description Method to form SMS template for sending OutForDelivery PREPAID Notification
	 * @param childOrders
	 * @param orderNumber
	 * @param mobileNumber
	 * @param appDwldUrl
	 */

	private void sendSMSOutForDeliveryPrepaid(final List<AbstractOrderEntryModel> childOrders, final String orderNumber,
			final String mobileNumber, final String appDwldUrl, final String awbNumber, final OrderModel orderModel)
	{
		try
		{
			final SendSMSRequestData smsRequestDataOutForDeliveryPrepaid = new SendSMSRequestData();
			smsRequestDataOutForDeliveryPrepaid.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataOutForDeliveryPrepaid
					.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_OUTFORDELIVERY_PREPAID
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
							.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, appDwldUrl));
			smsRequestDataOutForDeliveryPrepaid.setRecipientPhoneNumber(mobileNumber);
			final List<OrderUpdateSmsProcessModel> orderUpdateSmsModelList = checkSmsSent(awbNumber,
					ConsignmentStatus.OUT_FOR_DELIVERY);
			int numOfRows = 0;
			if (null != orderUpdateSmsModelList && !orderUpdateSmsModelList.isEmpty())
			{
				numOfRows = orderUpdateSmsModelList.size();
				//final List<String> entryList = orderUpdateSmsModelList.get(0).getEntryNumber();
				//totalEntries = entryList.size();
			}
			LOG.info("*******Before checking isToSendNotification for OUTFORDELIVERY SMS********");
			final boolean flag = isToSendNotification(awbNumber, orderModel, ConsignmentStatus.OUT_FOR_DELIVERY);
			LOG.info("*******After checking isToSendNotification for OUTFORDELIVERY SMS********");
			LOG.info("No of Rows:::::sendSMSOutForDeliveryPrepaid" + numOfRows);
			if (numOfRows == 0 && flag)
			{

				final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
						.createProcess("outForDeliverySmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
								"outForDeliverySmsProcess");
				orderUpdateSmsProcessModel.setOrder(orderModel);
				orderUpdateSmsProcessModel.setAwbNumber(awbNumber);
				orderUpdateSmsProcessModel.setShipmentStatus(ConsignmentStatus.OUT_FOR_DELIVERY);
				orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_OUTFORDELIVERY_PREPAID
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, appDwldUrl));
				orderUpdateSmsProcessModel.setRecipientPhoneNumber(mobileNumber);
				final List<String> entryNumber = new ArrayList<String>();
				for (final AbstractOrderEntryModel child : childOrders)
				{
					entryNumber.add(child.getEntryNumber().toString());
				}
				orderUpdateSmsProcessModel.setEntryNumber(entryNumber);

				modelService.save(orderUpdateSmsProcessModel);
				businessProcessService.startProcess(orderUpdateSmsProcessModel);
				//sendSMSService.sendSMS(smsRequestDataOutForDeliveryPrepaid);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS OutForDeliveryPrepaid>>>" + e.getMessage());
		}

	}

	/**
	 * @description Method to form push Notification for OutForDelivery-Prepaid
	 * @param childOrders
	 * @param orderNumber
	 */

	private void sendPushNotificationOutForDeliveryPrepaid(final List<AbstractOrderEntryModel> childOrders,
			final String orderNumber, final OrderModel orderModel)
	{
		CustomerModel customer = getModelService().create(CustomerModel.class);
		if (null != orderModel && null != orderModel.getUser() && null != orderModel.getUser().getUid()
				&& !orderModel.getUser().getUid().isEmpty())
		{
			customer = mplSNSMobilePushService.getCustForUId(orderModel.getUser().getUid());
			if (null != customer && null != customer.getDeviceKey() && !customer.getDeviceKey().isEmpty()
					&& null != customer.getOriginalUid() && !customer.getOriginalUid().isEmpty() && null != customer.getIsActive()
					&& !customer.getIsActive().isEmpty() && customer.getIsActive().equalsIgnoreCase("Y"))
			{
				//private method to avoid sonar issue
				//setPushData(customer, orderNumber, childOrders);

				final PushNotificationData pushData = new PushNotificationData();
				pushData.setMessage(MarketplacecommerceservicesConstants.PUSH_MESSAGE_ORDER_OFD_PREPAID.replace(
						MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderNumber).replace(
						MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, String.valueOf(childOrders.size())));
				if (null != orderNumber && !orderNumber.isEmpty())
				{
					pushData.setOrderId(orderNumber);
				}
				mplSNSMobilePushService.setUpNotification(customer.getOriginalUid(), pushData);

			}
		}


	}

	/**
	 * This method is responsible for sending Notification for Out For Delivery
	 *
	 * @param consignmentModel
	 * @param orderModel
	 * @param orderNumber
	 * @param mobileNumber
	 * @param codAmount
	 * @param appDwldUrl
	 *
	 */

	private void sendNotificationOutForDelivery(final ConsignmentModel consignmentModel, final OrderModel orderModel,
			final String orderNumber, final String mobileNumber, final String codAmount, final String appDwldUrl)
	{
		LOG.debug(UPDATE_CONSIGNMENT + MarketplacecommerceservicesConstants.ORDER_STATUS_OFD);
		String awbNumber = "";
		try
		{

			final Map<String, List<AbstractOrderEntryModel>> mapAWB = updateAWBNumber(orderModel, ConsignmentStatus.OUT_FOR_DELIVERY);
			for (final Map.Entry<String, List<AbstractOrderEntryModel>> entry : mapAWB.entrySet())
			{
				awbNumber = entry.getKey();
				final List<AbstractOrderEntryModel> childOrders = entry.getValue();
				final List<PaymentTransactionModel> paymentTransaction = consignmentModel.getOrder().getPaymentTransactions();

				for (final PaymentTransactionModel payment : paymentTransaction)
				{
					final List<PaymentTransactionEntryModel> paymentModel = payment.getEntries();
					for (final PaymentTransactionEntryModel payModel : paymentModel)
					{
						final PaymentTypeModel paymentMode = payModel.getPaymentMode();
						if (paymentMode.getMode().equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_STATUS_COD))
						{
							//sending SMS
							LOG.info("****************************Sending SMS for OutForDelivery COD ");
							sendSMSOutForDeliveryCOD(childOrders, orderNumber, mobileNumber, codAmount, appDwldUrl, awbNumber,
									orderModel);

							//Push Notifications Mobile
							LOG.info("****************************Sending PUSH notification for OutForDelivery COD ");
							sendPushNotificationOutForDeliveryCOD(childOrders, orderNumber, codAmount, orderModel);

						}
						else
						//For Prepaid
						{
							//sending SMS
							LOG.info("****************************Sending SMS for OutForDelivery Prepaid ");
							sendSMSOutForDeliveryPrepaid(childOrders, orderNumber, mobileNumber, appDwldUrl, awbNumber, orderModel);

							//Push Notifications Mobile
							LOG.info("****************************Sending PUSH Notification for OutForDelivery Prepaid ");
							sendPushNotificationOutForDeliveryPrepaid(childOrders, orderNumber, orderModel);


						}
					}
				}
			}

		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS or PUSH notification -  " + awbNumber + "\n" + e.getMessage());

		}
	}

	/**
	 * @description Method to form SMS template for sending HOTC Notification
	 * @param childOrders
	 * @param orderNumber
	 * @param mobileNumber
	 * @param trackingUrl
	 * @param logisticPartner
	 */

	private void sendSMSHotc(final List<AbstractOrderEntryModel> childOrders, final String orderNumber, final String mobileNumber,
			String trackingUrl, final String logisticPartner, final String awbNumber, final OrderModel orderModel,ConsignmentStatus newStatus)
	{
		try
		{
			LOG.info("******Inside sendSMSHotc******");
			trackingUrl = (getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT));
			final SendSMSRequestData smsRequestDataHOTC = new SendSMSRequestData();

			//call google short url service to generate short url for an order code
			final String shortTrackingUrl = googleShortUrlService
					.genearateShortURL(orderModel.getParentReference() == null ? orderModel.getCode() : orderModel
							.getParentReference().getCode());

			//print parent order number in the url
			trackingUrl = orderModel.getParentReference() == null ? (getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT)+"/"+orderModel.getCode()) : getConfigurationService()
					.getConfiguration().getString(MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT)
					+"/"+orderModel.getParentReference().getCode();

			smsRequestDataHOTC.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			
			final String smsContent = MarketplacecommerceservicesConstants.SMS_MESSAGE_HOTC
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, logisticPartner)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE,
							null == shortTrackingUrl ? trackingUrl : shortTrackingUrl);

			smsRequestDataHOTC.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataHOTC.setContent(smsContent);//Add Order tracking URL
			smsRequestDataHOTC.setRecipientPhoneNumber(mobileNumber);
			LOG.info("******Befor check SMS Sent******");
			final List<OrderUpdateSmsProcessModel> orderUpdateSmsModelList = checkSmsSent(awbNumber, ConsignmentStatus.HOTC);
			LOG.info("******After check SMS Sent******");
			int numOfRows = 0;
			if (null != orderUpdateSmsModelList && !orderUpdateSmsModelList.isEmpty())
			{
				numOfRows = orderUpdateSmsModelList.size();
				LOG.info("******Size high******" + orderUpdateSmsModelList.size());
				//final List<String> entryList = orderUpdateSmsModelList.get(0).getEntryNumber();
				//totalEntries = entryList.size();
			}
			LOG.info("*******Before checking isToSendNotification for HOTC SMS********");
			final boolean flag = isToSendNotification(awbNumber, orderModel,newStatus);
			LOG.info("*******After checking isToSendNotification for HOTC SMS******** SMS sent ? " + flag);
			if (numOfRows == 0 && flag)
			{

				final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
						.createProcess("shippingConfirmationSmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
								"shippingConfirmationSmsProcess");
				orderUpdateSmsProcessModel.setOrder(orderModel);
				orderUpdateSmsProcessModel.setAwbNumber(awbNumber);
				orderUpdateSmsProcessModel.setShipmentStatus(ConsignmentStatus.HOTC);
				orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_HOTC
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, logisticPartner)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE,
								null == shortTrackingUrl ? trackingUrl : shortTrackingUrl));
				orderUpdateSmsProcessModel.setRecipientPhoneNumber(mobileNumber);
				final List<String> entryNumber = new ArrayList<String>();
				for (final AbstractOrderEntryModel child : childOrders)
				{
					entryNumber.add(child.getEntryNumber().toString());
				}
				orderUpdateSmsProcessModel.setEntryNumber(entryNumber);
				LOG.info("******Start Business Process******");
				modelService.save(orderUpdateSmsProcessModel);
				businessProcessService.startProcess(orderUpdateSmsProcessModel);
				//sendSMSService.sendSMS(smsRequestDataHOTC);
			}

		}

		catch (final Exception e)
		{
			LOG.error("Exception during sending HOTC SMS>> " + e.getMessage());

		}

	}

	/**
	 * @param awbNumber
	 * @param orderModel
	 * @param hotc
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
	 * This method is responsible for sending Email Notification for Handed Over to Courier
	 *
	 * @param orderModel
	 * @param childOrders
	 * @param awbNumber
	 */
	private void sendEmailHotc(final OrderModel orderModel, final List<AbstractOrderEntryModel> childOrders, final String awbNumber,ConsignmentStatus newStatus)
	{
		final List<OrderUpdateProcessModel> orderUpdateModelList = checkEmailSent(awbNumber, ConsignmentStatus.HOTC);
		int numOfRows = 0;
		//int totalEntries = 0;
		if (null != orderUpdateModelList && !orderUpdateModelList.isEmpty())
		{
			numOfRows = orderUpdateModelList.size();
			//final List<String> entryList = orderUpdateModelList.get(0).getEntryNumber();
			//totalEntries = entryList.size();
		}


		LOG.info("*******Before checking isToSendNotification for HOTC Email********");
		final boolean flag = isToSendNotification(awbNumber, orderModel, newStatus);
		LOG.info("*******After checking isToSendNotification for HOTC Email********");
		LOG.info("No of Rows:::::sendEmailHotc" + numOfRows);
		if (numOfRows == 0 && flag)
		{
			LOG.info("Starting email process:::::");
			final OrderUpdateProcessModel orderUpdateProcessModel = (OrderUpdateProcessModel) getBusinessProcessService()
					.createProcess("shippingConfirmationEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
							"shippingConfirmationEmailProcess");
			orderUpdateProcessModel.setOrder(orderModel);
			orderUpdateProcessModel.setAwbNumber(awbNumber);
			orderUpdateProcessModel.setShipmentStatus(ConsignmentStatus.HOTC);
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


	/**
	 * This method is responsible for sending Notification for Handed Over to Courier
	 *
	 * @param orderModel
	 * @param orderNumber
	 * @param mobileNumber
	 * @param trackingUrl
	 * @param logisticPartner
	 *
	 */
	private void sendNotificationForHotc(final OrderModel orderModel, final String orderNumber, final String mobileNumber,
			final String trackingUrl, final String logisticPartner,ConsignmentStatus newStatus)
	{
		if(LOG.isDebugEnabled()){
			LOG.debug(UPDATE_CONSIGNMENT + MarketplacecommerceservicesConstants.ORDER_STATUS_HOTC);
		}
		String awbNumber = "";
		try
		{
			final Map<String, List<AbstractOrderEntryModel>> mapAWB = updateAWBNumber(orderModel,newStatus);
			for (final Map.Entry<String, List<AbstractOrderEntryModel>> entry : mapAWB.entrySet())
			{
				awbNumber = entry.getKey();
				final List<AbstractOrderEntryModel> childOrders = entry.getValue();

				//sending SMS
				LOG.info("********* Sending SMS for HOTC ");
				sendSMSHotc(childOrders, orderNumber, mobileNumber, trackingUrl, logisticPartner, awbNumber, orderModel,newStatus);
				LOG.info("******************* SMS sent");
				//sending Email
				LOG.info("********* Sending Email for HOTC ");
				sendEmailHotc(orderModel, childOrders, awbNumber,newStatus);

			}
		}
		catch (final Exception e)
		{

			LOG.error("Exception during sending SMS and EMAIL HOTC notification -  " + awbNumber + "\n" + e.getMessage());
		}
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

			smsRequestDataOrderDelivered.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataOrderDelivered.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_DELIVERY
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, appDwldUrl));
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
				orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_DELIVERY
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, String.valueOf(childOrders.size()))
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, appDwldUrl));
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

	/**
	 * This method is responsible for sending Notification for Order Delivered
	 *
	 * @param orderModel
	 * @param orderNumber
	 * @param mobileNumber
	 * @param appDwldUrl
	 *
	 */

	private void sendNotificationForDelivery(final OrderModel orderModel, final String orderNumber, final String mobileNumber,
			final String appDwldUrl)
	{
		LOG.debug(UPDATE_CONSIGNMENT + MarketplacecommerceservicesConstants.ORDER_STATUS_DELIVERED);
		String awbNumber = "";
		try
		{

			final Map<String, List<AbstractOrderEntryModel>> mapAWB = updateAWBNumber(orderModel, ConsignmentStatus.DELIVERED);
			for (final Map.Entry<String, List<AbstractOrderEntryModel>> entry : mapAWB.entrySet())
			{
				awbNumber = entry.getKey();
				final List<AbstractOrderEntryModel> childOrders = entry.getValue();

				//sending SMS
				LOG.info("****************************Sending SMS for Order Delivered ");
				sendSMSDelivered(orderModel, childOrders, orderNumber, mobileNumber, appDwldUrl, awbNumber);


				//Send EMAIL
				LOG.info("****************************Sending Email for Order Delivered ");
				sendEmailDelivered(orderModel, childOrders, awbNumber);


			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS and EMAIL DELIVERED notification -  " + awbNumber + "\n" + e.getMessage());
		}

	}

	/**
	 * @description Method to form SMS template for sending UnDelivered Notification
	 * @param childOrders
	 * @param orderNumber
	 * @param mobileNumber
	 * @param firstName
	 * @param contactNumber
	 */

	private void sendSMSUnDelivered(final List<AbstractOrderEntryModel> childOrders, final String orderNumber,
			final String mobileNumber, final String contactNumber, final String firstName, final OrderModel orderModel,
			final String awbNumber)
	{
		try
		{
			final SendSMSRequestData smsRequestDataOrderUndelivered = new SendSMSRequestData();
			smsRequestDataOrderUndelivered.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			smsRequestDataOrderUndelivered.setContent(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_UNDELIVERED
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, String.valueOf(childOrders.size()))
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, orderNumber)
					.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE, contactNumber));
			smsRequestDataOrderUndelivered.setRecipientPhoneNumber(mobileNumber);
			// Checking of SMS Notification previously sent or not
			final List<OrderUpdateSmsProcessModel> orderUpdateSmsModelList = checkSmsSent(awbNumber, ConsignmentStatus.UNDELIVERED);
			int numOfRows = 0;
			//int totalEntries = 0;

			if (null != orderUpdateSmsModelList && !orderUpdateSmsModelList.isEmpty())
			{
				numOfRows = orderUpdateSmsModelList.size();
				//final List<String> entryList = orderUpdateSmsModelList.get(0).getEntryNumber();
				//totalEntries = entryList.size();
			}
			LOG.info("*******Before checking isToSendNotification for UNDELIVERED SMS********");
			final boolean flag = isToSendNotification(awbNumber, orderModel, ConsignmentStatus.UNDELIVERED);
			LOG.info("*******After checking isToSendNotification for UNDELIVERED SMS********");
			LOG.info("No of Rows:::::sendSMSUnDelivered" + numOfRows);
			if (numOfRows == 0 && flag)
			{

				final OrderUpdateSmsProcessModel orderUpdateSmsProcessModel = (OrderUpdateSmsProcessModel) getBusinessProcessService()
						.createProcess("orderUndeliveredSmsProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
								"orderUndeliveredSmsProcess");
				orderUpdateSmsProcessModel.setOrder(orderModel);
				orderUpdateSmsProcessModel.setAwbNumber(awbNumber);
				orderUpdateSmsProcessModel.setShipmentStatus(ConsignmentStatus.UNDELIVERED);
				orderUpdateSmsProcessModel.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
				orderUpdateSmsProcessModel.setMessage(MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_UNDELIVERED
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, String.valueOf(childOrders.size()))
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, orderNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE, contactNumber));
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
			LOG.error("Exception during sending SMS for UNDELIVERED >> " + e.getMessage());
		}


	}


	/**
	 * This method is responsible for sending Notification for Order Undelivered
	 *
	 * @param orderModel
	 * @param orderNumber
	 * @param mobileNumber
	 * @param contactNumber
	 * @param firstName
	 *
	 */

	private void sendNotificationForUndelivered(final OrderModel orderModel, final String orderNumber, final String mobileNumber,
			final String contactNumber, final String firstName)
	{
		LOG.debug(UPDATE_CONSIGNMENT + MarketplacecommerceservicesConstants.ORDER_STATUS_UNDELIVERED);
		String awbNumber = "";
		try
		{
			final Map<String, List<AbstractOrderEntryModel>> mapAWB = updateAWBNumber(orderModel, ConsignmentStatus.UNDELIVERED);

			for (final Map.Entry<String, List<AbstractOrderEntryModel>> entry : mapAWB.entrySet())
			{
				awbNumber = entry.getKey();
				final List<AbstractOrderEntryModel> childOrders = entry.getValue();

				//sending SMS
				LOG.info("****************************Sending SMS for Order Delivered ");
				sendSMSUnDelivered(childOrders, orderNumber, mobileNumber, contactNumber, firstName, orderModel, awbNumber);
				
				//sending Email R2.3 New EMAIL
				LOG.info("****************************Sending Email for Order UnDelivered ");
				sendEmailUnDelivered(orderModel, childOrders, awbNumber);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS for UNDELIVERED >> " + awbNumber + "\n" + e.getMessage());
		}

	}
	
	
	/**
	 * Added R2.3 new  Email
	 * @description Method for sending UnDelivered Email Notification
	 * @param orderModel
	 * @param childOrders
	 * @param awbNumber
	 */
	private void sendEmailUnDelivered(final OrderModel orderModel, final List<AbstractOrderEntryModel> childOrders,
			final String awbNumber)
	{
		// Checking of SMS Notification previously sent or not
		final List<OrderUpdateProcessModel> orderUpdateModelList = checkEmailSent(awbNumber, ConsignmentStatus.UNDELIVERED);
		int numOfRows = 0;
		if (null != orderUpdateModelList && !orderUpdateModelList.isEmpty())
		{
			numOfRows = orderUpdateModelList.size();
			
		}
		LOG.info("*******Before checking isToSendNotification for UNDELIVERED EMAIL********");
		final boolean flag = isToSendNotification(awbNumber, orderModel, ConsignmentStatus.UNDELIVERED);
		LOG.info("*******After checking isToSendNotification for UNDELIVERED EMAIL********");
		LOG.info("No of Rows:::::sendEmailUnDelivered" + numOfRows);
		if (numOfRows == 0 && flag)
		{
			final OrderUpdateProcessModel orderUpdateProcessModel = (OrderUpdateProcessModel) getBusinessProcessService()
					.createProcess("orderUnDeliveredEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
							"orderUnDeliveredEmailProcess");
			orderUpdateProcessModel.setOrder(orderModel);
			orderUpdateProcessModel.setAwbNumber(awbNumber);
			orderUpdateProcessModel.setShipmentStatus(ConsignmentStatus.UNDELIVERED);

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
	 * @param orderNumber
	 * @param mobileNumber
	 * @param contactNumber
	 * @param firstName
	 * @param storeName
	 * @param deliverdDate
	 * @param pickUpMobileNumber
	 * @param pickUpPersonName
	 */

	private void sendNotificationForCNCOrderColleted(final String orderNumber, final String mobileNumber,
			final String contactNumber, final String firstName, final String storeName, final String deliverdDate,
			final String pickUpMobileNumber, final String pickUpPersonName)
	{
		try
		{
			final String contentForSMS = MarketplaceomsordersConstants.ORDER_COLLECTED_SMS
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO_ORD_COLLECTED, pickUpPersonName)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE_ORD_COLLECTED, orderNumber)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO_ORD_COLLECTED, storeName)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_THREE_ORD_COLLECTED, deliverdDate);

			final String contentSMSForCustomer = MarketplaceomsordersConstants.ORDER_COLLECTED_SMS_CUSTOMER
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO_ORD_COLLECTED_CUSTOMER, firstName)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE_ORD_COLLECTED_CUSTOMER, pickUpPersonName)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO_ORD_COLLECTED_CUSTOMER, storeName)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_THREE_ORD_COLLECTED_CUSTOMER, deliverdDate)
					.replace(MarketplaceomsordersConstants.SMS_VARIABLE_FOUR_ORD_COLLECTED_CUSTOMER, orderNumber);


			if (mobileNumber != null && pickUpMobileNumber != null && ObjectUtils.notEqual(mobileNumber, pickUpMobileNumber))
			{
				final SendSMSRequestData smsRequestData = new SendSMSRequestData();
				smsRequestData.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
				smsRequestData.setContent(contentForSMS);
				smsRequestData.setRecipientPhoneNumber(pickUpMobileNumber);
				//Send SMS to PickupPerson
				sendSMSService.sendSMS(smsRequestData);
				LOG.debug("Sending SMS to Pickup Person Mobile Successfully >> ");

				final SendSMSRequestData smsRequestDataforCustomer = new SendSMSRequestData();
				smsRequestDataforCustomer.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
				smsRequestDataforCustomer.setContent(contentSMSForCustomer);
				smsRequestDataforCustomer.setRecipientPhoneNumber(mobileNumber);
				//Send SMS to Customer
				sendSMSService.sendSMS(smsRequestDataforCustomer);
				LOG.debug("Sending SMS to Customer Mobile Successfully >> ");
			}
			else
			{
				final SendSMSRequestData smsRequestData = new SendSMSRequestData();
				smsRequestData.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
				smsRequestData.setContent(contentForSMS);
				smsRequestData.setRecipientPhoneNumber(mobileNumber);
				sendSMSService.sendSMS(smsRequestData);
				LOG.debug("Sending SMS to Customer Mobile Successfully >> ");
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception during sending SMS for ORDER_COLLECTED >> " + e.getMessage());
		}

	}

}
