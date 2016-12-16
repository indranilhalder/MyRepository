/**
 * 
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

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
		if ( sendUnColletedToCRMEvent.getShipmentNewStatus().equals(ConsignmentStatus.CANCELLATION_INITIATED))
		{
      		for (final AbstractOrderEntryModel orderEntryModel : sendUnColletedToCRMEvent.getOrderModel().getEntries())
      		{
					if(sendUnColletedToCRMEvent.getShipment().getShipmentId().equals(orderEntryModel.getTransactionID()) && sendUnColletedToCRMEvent.getConsignmentModel().getCode().equals(orderEntryModel.getTransactionID())){
						LOG.debug("Consignment Status : " + sendUnColletedToCRMEvent.getConsignmentModel().getStatus() + " :Transaction Id : "+orderEntryModel.getTransactionID()+ " : OrderLine Id :"+orderEntryModel.getOrderLineId());
						LOG.debug("******* sendUnColletedToCRMEvent.getShipment().getIsEDtoHD() : "+sendUnColletedToCRMEvent.getShipment().getIsEDtoHD());
						boolean isSsb=false;
						if(null!=sendUnColletedToCRMEvent.getShipment().getSsb() && sendUnColletedToCRMEvent.getShipment().getSsb().booleanValue()){
						 isSsb=sendUnColletedToCRMEvent.getShipment().getSsb().booleanValue();
						 sendUnColletedToCRMEvent.getConsignmentModel().setSsb(Boolean.TRUE);
						 sendUnColletedToCRMEvent.getConsignmentModel().setSsbCheck(Boolean.TRUE);
        			    modelService.save(sendUnColletedToCRMEvent.getConsignmentModel());
						}
						customOmsCancelAdapter.createTicketInCRM(orderEntryModel.getTransactionID(),
				MarketplaceomsordersConstants.TICKET_TYPE_CODE, MarketplaceomsordersConstants.EMPTY,
				MarketplaceomsordersConstants.REFUND_TYPE_CODE, sendUnColletedToCRMEvent.getOrderModel(),isSsb);
						
					}
				}
	      }
		}
		catch(Exception e){
			LOG.error("Exception Occer Create CRM Ticket"+sendUnColletedToCRMEvent.getConsignmentModel().getCode() +" -- " +e.getMessage());
		}
	}
}
