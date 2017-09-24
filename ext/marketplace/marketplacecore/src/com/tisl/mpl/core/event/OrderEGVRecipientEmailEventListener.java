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

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.event.OrderEGVRecipientEmailEvent;

/**
 * @author PankajK
 *
 */
public class OrderEGVRecipientEmailEventListener extends AbstractSiteEventListener<OrderEGVRecipientEmailEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final OrderEGVRecipientEmailEvent event)
	{
		
		final OrderModel orderModel = event.getProcess().getOrder();
		final OrderProcessModel egvOrdreProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"orderEGVRecipientEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
				"orderEGVRecipientEmailProcess");
		getModelService().save(egvOrdreProcessModel);
		getBusinessProcessService().startProcess(egvOrdreProcessModel);
	}

	@Override
	protected boolean shouldHandleEvent(final OrderEGVRecipientEmailEvent event)
	{
		final OrderModel order = event.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}



}
