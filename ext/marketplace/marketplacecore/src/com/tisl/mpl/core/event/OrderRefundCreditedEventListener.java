/**
 *
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.event.OrderRefundCreditedEvent;


/**
 * @author Dileep
 *
 */
public class OrderRefundCreditedEventListener extends AbstractSiteEventListener<OrderRefundCreditedEvent>
{

	private static final Logger LOG = Logger.getLogger(OrderRefundCreditedEventListener.class);

	private ModelService modelService;
	private BusinessProcessService businessProcessService;


	@Override
	protected void onSiteEvent(final OrderRefundCreditedEvent refundCredited)
	{
		LOG.info("Order RefundCredited  Event Listener Class ");
		final OrderModel orderModel = refundCredited.getProcess().getOrder();
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"orderRefundCreditedEmailProcess" + orderModel.getCode() + "-" + System.currentTimeMillis(),
				"orderRefundCreditedEmailProcess");
		LOG.info("OrderModel Details From Order Refund Credited Class " + orderModel);
		orderProcessModel.setOrder(orderModel);
		getModelService().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);

	}


	@Override
	protected boolean shouldHandleEvent(final OrderRefundCreditedEvent event)
	{
		final OrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}

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
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


}
