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
public class UnColletedOrderToInitiateRefundEventListener extends AbstractEventListener<UnColletedOrderToInitiateRefundEvent>
{

	private static final Logger LOG = Logger.getLogger(UnColletedOrderToInitiateRefundEventListener.class);
	
	@Autowired
	private CustomOmsCancelAdapter customOmsCancelAdapter;
	
	

	/* (non-Javadoc)
	 * @see de.hybris.platform.servicelayer.event.impl.AbstractEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected void onEvent(UnColletedOrderToInitiateRefundEvent unColletedOrderToInitiateRefundEvent)
	{
		LOG.debug("I am in Event class customOmsCancelAdapter.....:"+customOmsCancelAdapter);
		for (final AbstractOrderEntryModel orderEntryModel : unColletedOrderToInitiateRefundEvent.getOrderModel().getEntries())
		{
				if ((unColletedOrderToInitiateRefundEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) || unColletedOrderToInitiateRefundEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.ORDER_UNCOLLECTED))&& unColletedOrderToInitiateRefundEvent.getShipmentNewStatus().equals(ConsignmentStatus.CANCELLATION_INITIATED))
				{
					if(unColletedOrderToInitiateRefundEvent.getShipment().getShipmentId().equals(orderEntryModel.getTransactionID()) && unColletedOrderToInitiateRefundEvent.getConsignmentModel().getCode().equals(orderEntryModel.getTransactionID())){
						customOmsCancelAdapter.initiateCancellation(MarketplaceomsordersConstants.TICKET_TYPE_CODE,
								orderEntryModel.getTransactionID(), unColletedOrderToInitiateRefundEvent.getOrderModel(), MarketplaceomsordersConstants.REASON_CODE,
								unColletedOrderToInitiateRefundEvent.getConsignmentModel());
					}
					
				}
		}
	}

}
