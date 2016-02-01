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

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.commons.client.RestCallException;


public class CustomCreateOmsOrderAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(CustomCreateOmsOrderAction.class);
	private OmsOrderService omsOrderService;
	private int maxRetryCount;
	private int retryDelay;
	private CatalogVersionService catalogVersionService;
	private ImpersonationService impersonationService;


	@Override
	public Transition executeAction(final OrderProcessModel process) throws RetryLaterException
	{
		final OrderModel order = process.getOrder();

		final ImpersonationContext context = new ImpersonationContext();
		context.setOrder(order);
		context.setCatalogVersions(
				CommerceCatalogUtils.findProductCatalogVersions(getCatalogVersionService().getAllCatalogVersions()));

		final OrderPlacementResult result = getOmsOrderService().createOmsOrder(order);

		if (result.getResult().equals(OrderPlacementResult.Status.SUCCESS))
		{
			return Transition.OK;
		}
		if ((result.getResult().equals(OrderPlacementResult.Status.FAILED))
				&& (order.getExportedToOmsRetryCount().intValue() < getMaxRetryCount())
				&& result.getCause() instanceof RestCallException)
		{
			final RestCallException e = (RestCallException) result.getCause();
			if (Response.Status.SERVICE_UNAVAILABLE.equals(e.getResponse().getStatus())
					|| Response.Status.FORBIDDEN.equals(e.getResponse().getStatus())
					|| Response.Status.INTERNAL_SERVER_ERROR.equals(e.getResponse().getStatus())
					|| Response.Status.NOT_FOUND.equals(e.getResponse().getStatus())
					|| Response.Status.UNAUTHORIZED.equals(e.getResponse().getStatus()))
			{
				LOG.warn("Failed to send order " + order.getCode() + " to OMS. Service unavailable. Call will be retried. Error: "
						+ result.getCause().getMessage());
				order.setExportedToOmsRetryCount(Integer.valueOf(order.getExportedToOmsRetryCount().intValue() + 1));
				final RetryLaterException retryLaterException = new RetryLaterException(
						"Error occurred during oms order submission of order : " + order.getCode(), result.getCause());
				retryLaterException.setDelay(getRetryDelay());
				throw retryLaterException;
			}

		}

		return Transition.NOK;

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

	/**
	 * @return the catalogVersionService
	 */
	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	/**
	 * @param catalogVersionService
	 *           the catalogVersionService to set
	 */
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	/**
	 * @return the impersonationService
	 */
	public ImpersonationService getImpersonationService()
	{
		return impersonationService;
	}

	/**
	 * @param impersonationService
	 *           the impersonationService to set
	 */
	public void setImpersonationService(final ImpersonationService impersonationService)
	{
		this.impersonationService = impersonationService;
	}

}