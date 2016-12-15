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

import com.tisl.mpl.core.model.LuxuryPdpQuestionProcessModel;
import com.tisl.mpl.marketplacecommerceservices.event.LuxuryPdpQuestionEvent;


/**
 * @author TCS
 *
 */
public class LuxuryPdpQuestionEventListener extends AbstractSiteEventListener<LuxuryPdpQuestionEvent>
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


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final LuxuryPdpQuestionEvent luxuryPdpQuestionEvent)
	{
		final LuxuryPdpQuestionProcessModel luxuryPdpQuestionProcessModel = (LuxuryPdpQuestionProcessModel) getBusinessProcessService()
				.createProcess("luxuryPdpQuestionEmailProcess-" + "-" + System.currentTimeMillis(), "luxuryPdpQuestionEmailProcess");
		luxuryPdpQuestionProcessModel.setSite(luxuryPdpQuestionEvent.getSite());
		luxuryPdpQuestionProcessModel.setLanguage(luxuryPdpQuestionEvent.getLanguage());
		luxuryPdpQuestionProcessModel.setProductCode(luxuryPdpQuestionEvent.getProductCode());
		luxuryPdpQuestionProcessModel.setMessage(luxuryPdpQuestionEvent.getMessage());
		luxuryPdpQuestionProcessModel.setAttachedFileName(luxuryPdpQuestionEvent.getAttachedFileName());
		luxuryPdpQuestionProcessModel.setCustomer(luxuryPdpQuestionEvent.getCustomer());
		luxuryPdpQuestionProcessModel.setCustomerEmailId(luxuryPdpQuestionEvent.getCustomerEmailId());
		luxuryPdpQuestionProcessModel.setEmailTo(luxuryPdpQuestionEvent.getEmailTo());
		luxuryPdpQuestionProcessModel.setIssueDetails(luxuryPdpQuestionEvent.getIssueDetails());
		luxuryPdpQuestionProcessModel.setIssueType(luxuryPdpQuestionEvent.getIssueType());

		getModelService().save(luxuryPdpQuestionProcessModel);
		getBusinessProcessService().startProcess(luxuryPdpQuestionProcessModel);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final LuxuryPdpQuestionEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}

}
