/**
 *
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.NPSEmailerModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.event.NpsEmailEvent;


/**
 * @author 1080468
 *
 */
public class NPSEmailEventListener extends AbstractSiteEventListener<NpsEmailEvent>
{

	@Override
	protected void onSiteEvent(final NpsEmailEvent npsEmailEvent)
	{
		final OrderModel orderModel = npsEmailEvent.getProcess().getOrder();
		final NPSEmailerModel npsEmailProcessModel = (NPSEmailerModel) getBusinessProcessService().createProcess(
				"npsEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(), "npsEmailProcess");

		getModelService().save(npsEmailProcessModel);
		getBusinessProcessService().startProcess(npsEmailProcessModel);


	}


	private ModelService modelService;

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
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


	private BusinessProcessService businessProcessService;


	@Override
	protected boolean shouldHandleEvent(final NpsEmailEvent npsEmailEvent)
	{
		final OrderModel order = npsEmailEvent.getProcess().getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		final BaseSiteModel site = order.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}


}
