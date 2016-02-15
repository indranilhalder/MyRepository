///**
// *
// */
//package com.tisl.mpl.core.event;
//
//import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
//import de.hybris.platform.commerceservices.enums.SiteChannel;
//import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
//import de.hybris.platform.core.model.order.OrderModel;
//import de.hybris.platform.orderprocessing.model.OrderProcessModel;
//import de.hybris.platform.processengine.BusinessProcessService;
//import de.hybris.platform.servicelayer.model.ModelService;
//import de.hybris.platform.servicelayer.util.ServicesUtil;
//
//import org.springframework.beans.factory.annotation.Required;
//
//import com.tisl.mpl.marketplacecommerceservices.event.OrderRejectRiskEvent;
//
//
///**
// * @author TCS
// *
// */
//public class OrderRejectRiskEventListener extends AbstractSiteEventListener<OrderRejectRiskEvent>
//{
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
//	 * .event.events.AbstractEvent)
//	 */
//	@Override
//	protected void onSiteEvent(final OrderRejectRiskEvent orderRejectRiskEvent)
//	{
//		final OrderModel orderModel = orderRejectRiskEvent.getProcess().getOrder();
//		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
//				"orderRejectRiskEmailProcess-" + orderModel.getCode() + "-" + System.currentTimeMillis(),
//				"orderRejectRiskEmailProcess");
//		orderProcessModel.setOrder(orderModel);
//		getModelService().save(orderProcessModel);
//		getBusinessProcessService().startProcess(orderProcessModel);
//	}
//
//	private ModelService modelService;
//
//	/**
//	 * @return the modelService
//	 */
//	public ModelService getModelService()
//	{
//		return modelService;
//	}
//
//
//	/**
//	 * @param modelService
//	 *           the modelService to set
//	 */
//	@Required
//	public void setModelService(final ModelService modelService)
//	{
//		this.modelService = modelService;
//	}
//
//
//	public BusinessProcessService getBusinessProcessService()
//	{
//		return businessProcessService;
//	}
//
//	/**
//	 * @param businessProcessService
//	 *           the businessProcessService to set
//	 */
//	@Required
//	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
//	{
//		this.businessProcessService = businessProcessService;
//	}
//
//
//	private BusinessProcessService businessProcessService;
//
//	/*
//	 * (non-Javadoc)
//	 *
//	 * @see
//	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
//	 * .event.events.AbstractEvent)
//	 */
//	@Override
//	protected boolean shouldHandleEvent(final OrderRejectRiskEvent orderRejectRiskEvent)
//	{
//		final OrderModel order = orderRejectRiskEvent.getProcess().getOrder();
//		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
//		final BaseSiteModel site = order.getSite();
//		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
//		return SiteChannel.B2C.equals(site.getChannel());
//	}
//
//}