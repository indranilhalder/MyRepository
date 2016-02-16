/**
 *
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.SubmitRequestProcessModel;
import com.tisl.mpl.marketplacecommerceservices.event.SubmitRequestEvent;


/**
 * @author 765463
 *
 */
public class SubmitRequestEventListener extends AbstractSiteEventListener<SubmitRequestEvent>
{
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
	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


	private BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final SubmitRequestEvent submitRequestEvent)
	{


		final SubmitRequestProcessModel submitRequestProcessModel = (SubmitRequestProcessModel) getBusinessProcessService()
				.createProcess("submitRequestEmailProcess-" + "-" + System.currentTimeMillis(), "submitRequestEmailProcess");
		submitRequestProcessModel.setSite(submitRequestEvent.getSite());
		submitRequestProcessModel.setLanguage(submitRequestEvent.getLanguage());
		submitRequestProcessModel.setOrderCode(submitRequestEvent.getOrderCode());
		submitRequestProcessModel.setMessage(submitRequestEvent.getMessage());
		submitRequestProcessModel.setAttachedFileName(submitRequestEvent.getAttachedFileName());
		submitRequestProcessModel.setCustomer(submitRequestEvent.getCustomer());

		submitRequestProcessModel.setCustomerEmailId(submitRequestEvent.getCustomerEmailId());



		submitRequestProcessModel.setIssueDetails(submitRequestEvent.getIssueDetails());


		submitRequestProcessModel.setIssueType(submitRequestEvent.getIssueType());

		getModelService().save(submitRequestProcessModel);
		getBusinessProcessService().startProcess(submitRequestProcessModel);
	}


	@Override
	protected boolean shouldHandleEvent(final SubmitRequestEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}

}
