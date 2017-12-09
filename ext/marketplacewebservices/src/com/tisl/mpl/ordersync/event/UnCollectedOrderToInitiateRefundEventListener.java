/**
 *
 */
package com.tisl.mpl.ordersync.event;

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
 * @author TCS
 *
 */
public class UnCollectedOrderToInitiateRefundEventListener extends AbstractEventListener<UnCollectedOrderToInitiateRefundEvent>
{

	private static final Logger LOG = Logger.getLogger(UnCollectedOrderToInitiateRefundEventListener.class);

	@Autowired
	private CustomOmsCancelAdapter customOmsCancelAdapter;

	@Autowired
	private EventService eventService;


	@Override
	protected void onEvent(final UnCollectedOrderToInitiateRefundEvent unCollectedOrderToInitiateRefundEvent)
	{
		try
		{
			LOG.debug("Inside onEvent method of UnCollectedOrderToInitiateRefundEventListener, param - "
					+ unCollectedOrderToInitiateRefundEvent);

			//(unCollectedOrderToInitiateRefundEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.READY_FOR_COLLECTION) || unCollectedOrderToInitiateRefundEvent.getConsignmentModel().getStatus().equals(ConsignmentStatus.ORDER_UNCOLLECTED))&& 
			if (unCollectedOrderToInitiateRefundEvent.getShipmentNewStatus().equals(ConsignmentStatus.CANCELLATION_INITIATED))
			{
				for (final AbstractOrderEntryModel orderEntryModel : unCollectedOrderToInitiateRefundEvent.getOrderModel()
						.getEntries())
				{
					if (unCollectedOrderToInitiateRefundEvent.getShipment().getShipmentId().equals(orderEntryModel.getTransactionID())
							&& unCollectedOrderToInitiateRefundEvent.getConsignmentModel().getCode()
									.equals(orderEntryModel.getTransactionID()))
					{

						final String trackOrderUrl = unCollectedOrderToInitiateRefundEvent.getConfigurationService().getConfiguration()
								.getString(MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
								+ unCollectedOrderToInitiateRefundEvent.getOrderModel().getCode();
						final OrderProcessModel orderProcessModel = new OrderProcessModel();
						orderProcessModel.setOrder(unCollectedOrderToInitiateRefundEvent.getOrderModel());
						orderProcessModel.setOrderTrackUrl(trackOrderUrl);
						final OrderRefundCreditedEvent orderRefundCreditedEvent = new OrderRefundCreditedEvent(orderProcessModel);

						try
						{
							if (checkRefundStatus(unCollectedOrderToInitiateRefundEvent, orderEntryModel))
							{
								LOG.debug("Un collected orders for Refund status: true, Order Id " + orderEntryModel.getTransactionID());
								eventService.publishEvent(orderRefundCreditedEvent);
							}
							else
							{
								LOG.debug("Un collected orders for Refund status: false, Order Id " + orderEntryModel.getTransactionID());
							}
						}
						catch (final Exception e)
						{
							LOG.error("Exception during Refund Email Trigger for Un-Collected Orders >> "
									+ unCollectedOrderToInitiateRefundEvent.getConsignmentModel().getCode() + " -- " + e.getMessage());
						}

					}

				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception Occer Refund Initiation for Un collected Orders:" + e.getMessage());
		}
	}


	/**
	 * @param unCollectedOrderToInitiateRefundEvent
	 * @param orderEntryModel
	 * @return
	 */
	private boolean checkRefundStatus(final UnCollectedOrderToInitiateRefundEvent unCollectedOrderToInitiateRefundEvent,
			final AbstractOrderEntryModel orderEntryModel)
	{
		return customOmsCancelAdapter.initiateCancellation(MarketplaceomsordersConstants.TICKET_TYPE_CODE,
				orderEntryModel.getTransactionID(), unCollectedOrderToInitiateRefundEvent.getOrderModel(),
				MarketplaceomsordersConstants.REASON_CODE, unCollectedOrderToInitiateRefundEvent.getConsignmentModel());
	}

}
