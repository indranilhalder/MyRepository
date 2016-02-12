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

import com.tisl.mpl.core.model.OrderPlacedFeedbackProcessModel;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedFeedbackEvent;


/**
 * @author 592217
 *
 */
public class OrderPlacedFeedbackEventListener extends AbstractSiteEventListener<OrderPlacedFeedbackEvent>
{

	@Override
	protected void onSiteEvent(final OrderPlacedFeedbackEvent orderPlacedFeedbackEvent)
	{
		final OrderPlacedFeedbackProcessModel orderPlacedFeedbackProcessModel = (OrderPlacedFeedbackProcessModel) getBusinessProcessService()
				.createProcess("orderPlacedFeedbackEmailProcess-" + "-" + System.currentTimeMillis(),
						"orderPlacedFeedbackEmailProcess");
		orderPlacedFeedbackProcessModel.setSite(orderPlacedFeedbackEvent.getSite());
		orderPlacedFeedbackProcessModel.setLanguage(orderPlacedFeedbackEvent.getLanguage());
		orderPlacedFeedbackProcessModel.setOrderCode(orderPlacedFeedbackEvent.getOrderCode());
		orderPlacedFeedbackProcessModel.setRefNo(orderPlacedFeedbackEvent.getRefNo());
		orderPlacedFeedbackProcessModel.setTrancNo(orderPlacedFeedbackEvent.getTrancNo());
		orderPlacedFeedbackProcessModel.setNameOfProduct(orderPlacedFeedbackEvent.getNameOfProduct());
		orderPlacedFeedbackProcessModel.setFeedback(orderPlacedFeedbackEvent.getFeedback());
		orderPlacedFeedbackProcessModel.setSellerName(orderPlacedFeedbackEvent.getSellerName());
		getModelService().save(orderPlacedFeedbackProcessModel);
		getBusinessProcessService().startProcess(orderPlacedFeedbackProcessModel);

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
	protected boolean shouldHandleEvent(final OrderPlacedFeedbackEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}


}