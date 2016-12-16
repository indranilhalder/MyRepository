/**
 * 
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.ReturnQuickDropProcessModel;



/**
 * @author TO-OW101
 *
 */
public class OrderReturnToStoreEventListener extends AbstractSiteEventListener<OrderReturnToStoreEvent>
{
	private ModelService modelService;
	private BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final OrderReturnToStoreEvent event)
	{
		final OrderModel orderModel = event.getProcess().getOrder();
		final ReturnQuickDropProcessModel returnToStoreProcessModel = (ReturnQuickDropProcessModel) getBusinessProcessService().createProcess(
				"returnToStoreEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
				"returnToStoreEmailProcess");
		List<String> storeIds = returnToStoreProcessModel.getStoreIds();
		List<String> storeNames = returnToStoreProcessModel.getStoreNames();
		String transactionId = returnToStoreProcessModel.getTransactionId();
		returnToStoreProcessModel.setTransactionId(transactionId);
		returnToStoreProcessModel.setStoreIds(storeIds);
		returnToStoreProcessModel.setStoreNames(storeNames);
		returnToStoreProcessModel.setOrder(orderModel);
		getModelService().save(returnToStoreProcessModel);
		getBusinessProcessService().startProcess(returnToStoreProcessModel);

	}

	@Override
	protected boolean shouldHandleEvent(final OrderReturnToStoreEvent event)
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
