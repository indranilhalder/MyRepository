/**
 * 
 */
package com.tisl.mpl.marketplaceomsservices.event;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsordersConstants;
import com.tisl.mpl.integration.oms.adapter.CustomOmsCancelAdapter;
import com.tisl.mpl.marketplacecommerceservices.event.OrderRefundCreditedEvent;

/**
 * @author Tech
 *
 */
public class UnColletedOrderToInitiateRefundEventListener extends AbstractEventListener<UnColletedOrderToInitiateRefundEvent>
{

	private static final Logger LOG = Logger.getLogger(UnColletedOrderToInitiateRefundEventListener.class);
	
	@Autowired
	private CustomOmsCancelAdapter customOmsCancelAdapter;
	
	@Autowired
	private EventService eventService;

	/* (non-Javadoc)
	 * @see de.hybris.platform.servicelayer.event.impl.AbstractEventListener#onEvent(de.hybris.platform.servicelayer.event.events.AbstractEvent)
	 */
	@Override
	protected void onEvent(UnColletedOrderToInitiateRefundEvent unColletedOrderToInitiateRefundEvent)
	{
	try{
		LOG.debug("I am in Event class customOmsCancelAdapter.....:"+customOmsCancelAdapter);
		  for (final AbstractOrderEntryModel orderEntryModel : unColletedOrderToInitiateRefundEvent.getOrderModel().getEntries())
		  {
				if ((unColletedOrderToInitiateRefundEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) || unColletedOrderToInitiateRefundEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.ORDER_UNCOLLECTED))&& unColletedOrderToInitiateRefundEvent.getShipmentNewStatus().equals(ConsignmentStatus.CANCELLATION_INITIATED))
				{
					if(unColletedOrderToInitiateRefundEvent.getShipment().getShipmentId().equals(orderEntryModel.getTransactionID()) && unColletedOrderToInitiateRefundEvent.getConsignmentModel().getCode().equals(orderEntryModel.getTransactionID())){
						boolean refundStatus=customOmsCancelAdapter.initiateCancellation(MarketplaceomsordersConstants.TICKET_TYPE_CODE,
								orderEntryModel.getTransactionID(), unColletedOrderToInitiateRefundEvent.getOrderModel(), MarketplaceomsordersConstants.REASON_CODE,
								unColletedOrderToInitiateRefundEvent.getConsignmentModel());
						LOG.debug("Un colleted orders for Refund status:"+refundStatus);
						final String trackOrderUrl = unColletedOrderToInitiateRefundEvent.getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL) + unColletedOrderToInitiateRefundEvent.getOrderModel().getCode();
						final OrderProcessModel orderProcessModel = new OrderProcessModel();
						orderProcessModel.setOrder(unColletedOrderToInitiateRefundEvent.getOrderModel());
						orderProcessModel.setOrderTrackUrl(trackOrderUrl);
						final OrderRefundCreditedEvent orderRefundCreditedEvent = new OrderRefundCreditedEvent(orderProcessModel);
						try
						{
							if(refundStatus){
							LOG.debug("Refund Email Trigger for Un-Colleted Orders");
							eventService.publishEvent(orderRefundCreditedEvent);
							}else{
								LOG.debug("Unable to  TriggerRefund Email  for Un-Colleted Orders"+refundStatus);
							}
						}
						catch (final Exception e)
						{
							LOG.error("Exception during Refund Email Trigger for Un-Colleted Orders >> " + e.getMessage());
						}
	
					}
					
				}
		 }
		}catch(Exception e){
			LOG.error("Exception Occer Refund Initiation for Un colleted Orders:"+e.getMessage());
		}
	}

}
