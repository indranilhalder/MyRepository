/**
 * 
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.integration.oms.adapter.CustomOmsCancelAdapter;

/**
 * @author Tech
 *
 */
public class SendUnColletedOrderToCRMEventListener extends AbstractEventListener<SendUnColletedOrderToCRMEvent>
{

	private static final Logger LOG = Logger.getLogger(SendUnColletedOrderToCRMEventListener.class);
	
	@Autowired
	private CustomOmsCancelAdapter customOmsCancelAdapter;
	
	/* (non-Javadoc)
	 * @see de.hybris.platform.servicelayer.event.impl.AbstractEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected void onEvent(SendUnColletedOrderToCRMEvent sendUnColletedToCRMEvent)
	{
		try{
		LOG.debug("SendUnColletedOrderToCRMEventListener Event class:"+customOmsCancelAdapter);
		for (final AbstractOrderEntryModel orderEntryModel : sendUnColletedToCRMEvent.getOrderModel().getEntries())
		{
				if ((sendUnColletedToCRMEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) || sendUnColletedToCRMEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.ORDER_UNCOLLECTED))&& sendUnColletedToCRMEvent.getShipmentNewStatus().equals(ConsignmentStatus.CANCELLATION_INITIATED))
				{
					if(sendUnColletedToCRMEvent.getShipment().getShipmentId().equals(orderEntryModel.getTransactionID()) && sendUnColletedToCRMEvent.getConsignmentModel().getCode().equals(orderEntryModel.getTransactionID())){
						LOG.debug("********Consignment Status**********" + sendUnColletedToCRMEvent.getConsignmentModel().getStatus());
						LOG.debug("********Transaction Id**********" + orderEntryModel.getTransactionID());
						LOG.debug("********OrderLine Id**********" + orderEntryModel.getOrderLineId());
			customOmsCancelAdapter.createTicketInCRM(orderEntryModel.getTransactionID(),
				MarketplaceomsordersConstants.TICKET_TYPE_CODE, MarketplaceomsordersConstants.EMPTY,
				MarketplaceomsordersConstants.REFUND_TYPE_CODE, sendUnColletedToCRMEvent.getOrderModel());
					}
				}
	      }
		}
		catch(Exception e){
			LOG.error("Exception Occer Create CRM Ticket"+sendUnColletedToCRMEvent.getConsignmentModel().getCode() +" -- " +e.getMessage());
		}
	}
}
