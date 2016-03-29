/**
 * 
 */
package com.tisl.mpl.integration.oms.adapter;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

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
	private ModelService modelService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private MplSendSMSService sendSMSService;
	
	private static final String UPDATE_CONSIGNMENT = "updateConsignment:: Inside ";
	
	public void sendNotificationForOrderCollected(final OrderModel orderModel, final OrderData orderData, final ConsignmentModel consignmentModel)
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
		   if(null !=orderData && null != orderData.getCustomerData()) {
		   	customerName=(null !=orderData.getCustomerData().getFirstName() &&  StringUtils.isNotEmpty(orderData.getCustomerData().getFirstName())) ?orderData.getCustomerData().getFirstName()  : " Customer " ;
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
    			String contentForSMS= MarketplaceomsordersConstants.ORDER_COLLECTED_SMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO_ORD_COLLECTED, customerName).replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE_ORD_COLLECTED, orderNumber).replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO_ORD_COLLECTED, storeName).replace(MarketplaceomsordersConstants.SMS_VARIABLE_THREE_ORD_COLLECTED, deliverdDate);
    			final String mobileNumber = (StringUtils.isEmpty(orderModel.getPickupPersonMobile())) ? MarketplaceomsordersConstants.EMPTY
						: orderModel.getPickupPersonMobile();
    			
    			final SendSMSRequestData smsRequestData = new SendSMSRequestData();
   			smsRequestData.setSenderID(MarketplaceomsordersConstants.SMS_SENDER_ID);
   			smsRequestData.setContent(contentForSMS);
   			smsRequestData.setRecipientPhoneNumber(mobileNumber);
   			sendSMSService.sendSMS(smsRequestData);
    			
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
