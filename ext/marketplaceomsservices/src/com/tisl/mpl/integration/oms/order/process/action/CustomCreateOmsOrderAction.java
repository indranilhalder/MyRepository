package com.tisl.mpl.integration.oms.order.process.action;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commerceservices.util.CommerceCatalogUtils;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.integration.oms.order.data.OrderPlacementResult;
import de.hybris.platform.integration.oms.order.service.OmsOrderService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class CustomCreateOmsOrderAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(CustomCreateOmsOrderAction.class);
	private OmsOrderService omsOrderService;
	private int maxRetryCount;
	private int retryDelay;
	private ImpersonationService impersonationService;
	private CatalogVersionService catalogVersionService;

	@Override
	public AbstractSimpleDecisionAction.Transition executeAction(final OrderProcessModel process) throws RetryLaterException
	{
		final OrderModel order = process.getOrder();
		final ImpersonationContext context = new ImpersonationContext();
		context.setOrder(order);
		context.setCatalogVersions(CommerceCatalogUtils.findProductCatalogVersions(getCatalogVersionService()
				.getAllCatalogVersions()));

		final OrderPlacementResult result = CustomCreateOmsOrderAction.this.getOmsOrderService().createOmsOrder(order);

		if (result.getResult().equals(OrderPlacementResult.Status.SUCCESS))
		{
			return AbstractSimpleDecisionAction.Transition.OK;
		}
		if ((result.getResult().equals(OrderPlacementResult.Status.FAILED))
				&& (order.getExportedToOmsRetryCount().intValue() < CustomCreateOmsOrderAction.this.getMaxRetryCount()))
		{
			CustomCreateOmsOrderAction.LOG.warn(String.format(
					"Failed to send order %s to OMS. Service unavailable. Call will be retried. Error:  %s", new Object[]
					{ order.getCode(), result.getCause().getMessage() }));
			order.setExportedToOmsRetryCount(Integer.valueOf(order.getExportedToOmsRetryCount().intValue() + 1));
			final RetryLaterException retryLaterException = new RetryLaterException(
					"Error occurred during oms order submission of order : " + order.getCode(), result.getCause());
			retryLaterException.setDelay(CustomCreateOmsOrderAction.this.getRetryDelay());
			throw retryLaterException;
		}

		return null;

		//		return ((AbstractSimpleDecisionAction.Transition) getImpersonationService().executeInContext(context,
		//				new ImpersonationService.Executor(order)
		//				{
		//					public AbstractSimpleDecisionAction.Transition execute() throws RetryLaterException
		//					{
		//						final OrderPlacementResult result = CustomCreateOmsOrderAction.this.getOmsOrderService().createOmsOrder(
		//								this.val$order);
		//
		//						if (result.getResult().equals(OrderPlacementResult.Status.SUCCESS))
		//						{
		//							return AbstractSimpleDecisionAction.Transition.OK;
		//						}
		//						if ((result.getResult().equals(OrderPlacementResult.Status.FAILED))
		//								&& (this.val$order.getExportedToOmsRetryCount().intValue() < CustomCreateOmsOrderAction.this
		//										.getMaxRetryCount()))
		//						{
		//							CustomCreateOmsOrderAction.LOG.warn(String.format(
		//									"Failed to send order %s to OMS. Service unavailable. Call will be retried. Error:  %s", new Object[]
		//									{ this.val$order.getCode(), result.getCause().getMessage() }));
		//							this.val$order.setExportedToOmsRetryCount(Integer.valueOf(this.val$order.getExportedToOmsRetryCount()
		//									.intValue() + 1));
		//							final RetryLaterException retryLaterException = new RetryLaterException(
		//									"Error occurred during oms order submission of order : " + this.val$order.getCode(), result.getCause());
		//							retryLaterException.setDelay(CustomCreateOmsOrderAction.this.getRetryDelay());
		//							throw retryLaterException;
		//						}
		//
		//						CustomCreateOmsOrderAction.LOG.error(String.format(
		//								"Failed to send order %s to OMS. Service unavailable. Call won't be retried. Error:  %s", new Object[]
		//								{ this.val$order.getCode(), result.getCause().getMessage() }), result.getCause());
		//						CustomCreateOmsOrderAction.this.getOmsOrderService().flagTheOrderAsFailed(this.val$order, result.getCause());
		//						return AbstractSimpleDecisionAction.Transition.NOK;
		//					}
		//				}));
	}

	public OmsOrderService getOmsOrderService()
	{
		return this.omsOrderService;
	}

	public void setOmsOrderService(final OmsOrderService omsOrderService)
	{
		this.omsOrderService = omsOrderService;
	}

	public int getMaxRetryCount()
	{
		return this.maxRetryCount;
	}

	public void setMaxRetryCount(final int maxRetryCount)
	{
		this.maxRetryCount = maxRetryCount;
	}

	public int getRetryDelay()
	{
		return this.retryDelay;
	}

	public void setRetryDelay(final int retryDelay)
	{
		this.retryDelay = retryDelay;
	}

	protected ImpersonationService getImpersonationService()
	{
		return this.impersonationService;
	}

	@Required
	public void setImpersonationService(final ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return this.catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
}