/**
 * 
 */
package com.tisl.mpl.integration.oms.adapter;


import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.event.EventService;


import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.data.SendSMSRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.event.OrderCollectedByPersonEvent;
import com.tisl.mpl.sms.MplSendSMSService;


/**
 * @author Tech
 *
 */
public class CustomOmsCollectedAdapter
{
	private static final Logger LOG = Logger.getLogger(CustomOmsCollectedAdapter.class);
	
	
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private MplSendSMSService sendSMSService;
	
	private static final String UPDATE_CONSIGNMENT = "updateConsignment:: Inside ";
	
	public void sendNotificationForOrderCollected(final OrderModel orderModel, final ConsignmentModel consignmentModel)
	{
		
		LOG.debug(UPDATE_CONSIGNMENT + MarketplaceomsordersConstants.ORDER_STATUS_COLLECTED);
		
		final OrderProcessModel orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(orderModel);
		final  OrderCollectedByPersonEvent orderCollectedByPersonEvent = new OrderCollectedByPersonEvent(orderProcessModel);
		try
		{
			eventService.publishEvent(orderCollectedByPersonEvent);
		}
		catch (final Exception e1)
		{
			LOG.error("Exception during sending mail or SMS >> " + e1.getMessage());
		}
		try
		{
			String customerName=null;
			String orderNumber=null;
			String storeName=null;
			String deliverdDate=null;
			String pickUpPersonName=null;
			
			
			if(null !=orderModel && null != orderModel.getPickupPersonName()) {
				pickUpPersonName=(null !=orderModel.getPickupPersonName() &&  StringUtils.isNotEmpty(orderModel.getPickupPersonName())) ? orderModel.getPickupPersonName()  : " Customer " ;
		   }else{
		   	pickUpPersonName= " Customer ";	 
		   }
			
		   if(null !=orderModel && null != orderModel.getUser()) {
		   	customerName=(null !=orderModel.getUser().getName() &&  StringUtils.isNotEmpty(orderModel.getUser().getName())) ? orderModel.getUser().getName()  : " Customer " ;
		   }else{
		   	customerName= MarketplaceomsordersConstants.EMPTY;	 
		   }
		   if(null != orderModel){
		   	orderNumber= (null != orderModel.getCode() &&  StringUtils.isNotEmpty(orderModel.getCode())) ?  orderModel.getCode() :  MarketplaceomsordersConstants.EMPTY ;
		   }else{
		   	orderNumber= MarketplaceomsordersConstants.EMPTY;	 
		   }
		  
		   if(null != consignmentModel.getDeliveryPointOfService()){
		   		  if(consignmentModel.getDeliveryPointOfService()!=null && consignmentModel.getDeliveryPointOfService().getDisplayName()!=null && StringUtils.isNotEmpty(consignmentModel.getDeliveryPointOfService().getDisplayName())) {
		   			  storeName=consignmentModel.getDeliveryPointOfService().getDisplayName();
		   		  }else{
		   			  storeName= MarketplaceomsordersConstants.EMPTY;
		   		  }
		   }else{
		   	 storeName= MarketplaceomsordersConstants.EMPTY;	 
		   }
		   if(null !=consignmentModel.getDeliveryDate()){
		   	deliverdDate=(null!= consignmentModel.getDeliveryDate().toString() && StringUtils.isNotEmpty(consignmentModel.getDeliveryDate().toString())) ? consignmentModel.getDeliveryDate().toString() : MarketplaceomsordersConstants.EMPTY ; 
		   }else{
		   	deliverdDate= MarketplaceomsordersConstants.EMPTY;	 
		   }
    			String contentForSMS= MarketplaceomsordersConstants.ORDER_COLLECTED_SMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO_ORD_COLLECTED, pickUpPersonName).replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE_ORD_COLLECTED, orderNumber).replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO_ORD_COLLECTED, storeName).replace(MarketplaceomsordersConstants.SMS_VARIABLE_THREE_ORD_COLLECTED, deliverdDate);
    			if(orderModel != null && orderModel.getPickupPersonMobile()!= null ){
						final String mobileNumber = (StringUtils.isEmpty(orderModel.getPickupPersonMobile())) ? MarketplaceomsordersConstants.EMPTY
						: orderModel.getPickupPersonMobile();
    			
						final SendSMSRequestData smsRequestData = new SendSMSRequestData();
						smsRequestData.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
						smsRequestData.setContent(contentForSMS);
						smsRequestData.setRecipientPhoneNumber(mobileNumber);
						//Send SMS to PickupPerson
						sendSMSService.sendSMS(smsRequestData);
						LOG.debug("Sending SMS to Pickup Person Mobile Successfully >> ");
    			}
   			//Send SMS to Customer
					CustomerModel customer=null;
					if(orderModel.getUser() != null && orderModel.getUser() instanceof CustomerModel){
						customer=(CustomerModel) orderModel.getUser();
					}
					if(customer!=null && customer.getMobileNumber()  !=null ){
						final String customerMobile = (StringUtils.isEmpty(customer.getMobileNumber())) ? MarketplaceomsordersConstants.EMPTY
						: customer.getMobileNumber();
							if(customerMobile!= null && StringUtils.isNotEmpty(customerMobile)){
								String contentSMSForCustomer= MarketplaceomsordersConstants.ORDER_COLLECTED_SMS_CUSTOMER.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO_ORD_COLLECTED_CUSTOMER, customerName).replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE_ORD_COLLECTED_CUSTOMER, pickUpPersonName).replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO_ORD_COLLECTED_CUSTOMER, storeName).replace(MarketplaceomsordersConstants.SMS_VARIABLE_THREE_ORD_COLLECTED_CUSTOMER, deliverdDate).replace(MarketplaceomsordersConstants.SMS_VARIABLE_FOUR_ORD_COLLECTED_CUSTOMER, orderNumber);
								final SendSMSRequestData smsRequestDataforCustomer = new SendSMSRequestData();
								smsRequestDataforCustomer.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
								smsRequestDataforCustomer.setContent(contentSMSForCustomer);
								smsRequestDataforCustomer.setRecipientPhoneNumber(customerMobile);
								sendSMSService.sendSMS(smsRequestDataforCustomer);
								LOG.debug("Sending SMS to Customer Mobile Successfully >> ");
							}else{
								LOG.debug("Please provied Mobile Number for the Customer");
							}
					}
    			
		}
		catch ( final EtailNonBusinessExceptions ex)
		{
			LOG.error("EtailNonBusinessExceptions occured while sending sms " + ex);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (JAXBException e)
		{
			LOG.error("EtailNonBusinessExceptions occured while sending sms " + e);
		}

	}
}
