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
		
    			String contentForSMS= MarketplaceomsordersConstants.ORDER_COLLECTED_SMS;
    			          if(null !=orderData && null != orderData.getCustomerData() ){
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO_ORD_COLLECTED, (StringUtils.isEmpty(orderData.getCustomerData().getFirstName())) ? MarketplaceomsordersConstants.EMPTY : orderData.getCustomerData().getFirstName());
    			          }else{
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ZERO_ORD_COLLECTED, "");
    			          }
    			          if(null != orderModel){
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE_ORD_COLLECTED, (StringUtils.isEmpty(orderModel.getCode())) ? MarketplaceomsordersConstants.EMPTY:  orderModel.getCode());
    			          }else {
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_ONE_ORD_COLLECTED, "");
    			          }
    			          if(null != orderModel && null!= orderModel.getStore()){
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO_ORD_COLLECTED, (StringUtils.isEmpty(orderModel.getStore().getName())) ? MarketplaceomsordersConstants.EMPTY :  orderModel.getStore().getName());
    			          }else{
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_TWO_ORD_COLLECTED, ""); 
    			          }
    			          if(null != consignmentModel){
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_THREE_ORD_COLLECTED, (StringUtils.isEmpty(consignmentModel.getDeliveryDate().toString())) ? MarketplaceomsordersConstants.EMPTY :  consignmentModel.getDeliveryDate().toString());
    			          }else{
    			         	contentForSMS.replace(MarketplaceomsordersConstants.SMS_VARIABLE_THREE_ORD_COLLECTED, ""); 
    			          }
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
