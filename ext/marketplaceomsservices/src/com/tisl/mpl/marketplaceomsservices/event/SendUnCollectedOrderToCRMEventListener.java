/**
 * 
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.integration.oms.adapter.CustomOmsCancelAdapter;

/**
 * @author Tech
 *
 */
public class SendUnCollectedOrderToCRMEventListener extends AbstractEventListener<SendUnCollectedOrderToCRMEvent>
{

	private static final Logger LOG = Logger.getLogger(SendUnCollectedOrderToCRMEventListener.class);
	
	@Autowired
	private CustomOmsCancelAdapter customOmsCancelAdapter;
	@Autowired
	private ModelService modelService;
	@Override
	protected void onEvent(SendUnCollectedOrderToCRMEvent sendUnColletedToCRMEvent)
	{
		try{
		LOG.debug("Inside onEvent method of SendUnCollectedOrderToCRMEventListener");
		//(sendUnColletedToCRMEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) || sendUnColletedToCRMEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.ORDER_UNCOLLECTED))&&
		/*if ( sendUnColletedToCRMEvent.getShipmentNewStatus().equals(ConsignmentStatus.CANCELLATION_INITIATED))
		{*/
      		for (final AbstractOrderEntryModel orderEntryModel : sendUnColletedToCRMEvent.getOrderModel().getEntries())
      		{
					if(sendUnColletedToCRMEvent.getShipment().getShipmentId().equals(orderEntryModel.getTransactionID()) && sendUnColletedToCRMEvent.getConsignmentModel().getCode().equals(orderEntryModel.getTransactionID())){
						LOG.debug("Consignment Status : " + sendUnColletedToCRMEvent.getConsignmentModel().getStatus() + " :Transaction Id : "+orderEntryModel.getTransactionID()+ " : OrderLine Id :"+orderEntryModel.getOrderLineId());
						LOG.debug("******* sendUnColletedToCRMEvent.getShipment().getIsEDtoHD() : "+sendUnColletedToCRMEvent.getShipment().getIsEDtoHD());
						boolean isSsb=false;
						boolean isSdb=false;
						boolean isEdtoHd=false;
					   String paymentRefundType=null;
						final List<PaymentTransactionModel> tranactions = sendUnColletedToCRMEvent.getOrderModel().getPaymentTransactions();
						if (CollectionUtils.isNotEmpty(tranactions))
						{
							final PaymentTransactionEntryModel entry = tranactions.iterator().next().getEntries().iterator().next();
							if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
									&& "COD".equalsIgnoreCase(entry.getPaymentMode().getMode())){
								paymentRefundType="N";
							}else{
								paymentRefundType=MarketplaceomsordersConstants.REFUND_TYPE_CODE;
							}
						}else{
							paymentRefundType=MarketplaceomsordersConstants.REFUND_TYPE_CODE;
						}
						if(null!=sendUnColletedToCRMEvent.getShipment().getSsb() && sendUnColletedToCRMEvent.getShipment().getSsb().booleanValue()){
						 isSsb=sendUnColletedToCRMEvent.getShipment().getSsb().booleanValue();
						 sendUnColletedToCRMEvent.getConsignmentModel().setSsb(Boolean.TRUE);
						 sendUnColletedToCRMEvent.getConsignmentModel().setSsbCheck(Boolean.TRUE);
        			    modelService.save(sendUnColletedToCRMEvent.getConsignmentModel());
        			    
        			     customOmsCancelAdapter.createTicketInCRM(orderEntryModel.getTransactionID(),
        			   		sendUnColletedToCRMEvent.getTicketType(), MarketplaceomsordersConstants.EMPTY,
        			   		paymentRefundType, sendUnColletedToCRMEvent.getOrderModel(),isSsb,isSdb,isEdtoHd);
        			  
						}else if(null!=sendUnColletedToCRMEvent.getShipment().getSdb() && sendUnColletedToCRMEvent.getShipment().getSdb().booleanValue()){
							isSdb=sendUnColletedToCRMEvent.getShipment().getSdb().booleanValue();  
							customOmsCancelAdapter.createTicketInCRM(orderEntryModel.getTransactionID(),
									sendUnColletedToCRMEvent.getTicketType(), MarketplaceomsordersConstants.EMPTY,
									paymentRefundType, sendUnColletedToCRMEvent.getOrderModel(),isSsb,isSdb,isEdtoHd);
						}else if(null!=sendUnColletedToCRMEvent.getShipment().getIsEDtoHD() && sendUnColletedToCRMEvent.getShipment().getIsEDtoHD().booleanValue()){
							isEdtoHd=sendUnColletedToCRMEvent.getShipment().getIsEDtoHD().booleanValue(); 
							customOmsCancelAdapter.createTicketInCRM(orderEntryModel.getTransactionID(),
									sendUnColletedToCRMEvent.getTicketType(), MarketplaceomsordersConstants.EMPTY,
									paymentRefundType, sendUnColletedToCRMEvent.getOrderModel(),isSsb,isSdb,isEdtoHd);
						}else{
							customOmsCancelAdapter.createTicketInCRM(orderEntryModel.getTransactionID(),
									sendUnColletedToCRMEvent.getTicketType(), MarketplaceomsordersConstants.EMPTY,
									paymentRefundType, sendUnColletedToCRMEvent.getOrderModel(),isSsb,isSdb,isEdtoHd);
						}
						
						
					}
				}
	     // }
		}
		catch(Exception e){
			LOG.error("Exception Occer Create CRM Ticket"+sendUnColletedToCRMEvent.getConsignmentModel().getCode() +" -- " +e.getMessage());
		}
	}
}
