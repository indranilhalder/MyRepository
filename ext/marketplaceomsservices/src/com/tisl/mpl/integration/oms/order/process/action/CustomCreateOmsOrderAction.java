package com.tisl.mpl.integration.oms.order.process.action;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commerceservices.util.CommerceCatalogUtils;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.integration.oms.order.data.OrderPlacementResult;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.commons.client.RestCallException;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;


public class CustomCreateOmsOrderAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(CustomCreateOmsOrderAction.class);
	private CustomOmsOrderService omsOrderService;
	private int maxRetryCount;
	private int retryDelay;
	private ImpersonationService impersonationService;
	private CatalogVersionService catalogVersionService;
	private ModelService modelService;


	@Override
	public Transition executeAction(final OrderProcessModel process) throws RetryLaterException
	{
		final OrderModel order = process.getOrder();

		final ImpersonationContext context = new ImpersonationContext();
		context.setOrder(order);
		context.setCatalogVersions(CommerceCatalogUtils.findProductCatalogVersions(getCatalogVersionService()
				.getAllCatalogVersions()));
		OrderPlacementResult crmResult = null;
		OrderPlacementResult omsResult = null;
		if (order.getCrmSubmitStatus() == null || !order.getCrmSubmitStatus().equals(MarketplaceomsservicesConstants.SUCCESS))
		{
			crmResult = CustomCreateOmsOrderAction.this.getOmsOrderService().createCrmOrder(order);
			LOG.debug("After CRM call from Action : " + crmResult.getCause() + " : " + crmResult.getResult());
		}
		else if (order.getCrmSubmitStatus() != null && order.getCrmSubmitStatus().equals(MarketplaceomsservicesConstants.SUCCESS))
		{
			crmResult = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
		}

		if (order.getOmsSubmitStatus() == null || !order.getOmsSubmitStatus().equals(MarketplaceomsservicesConstants.SUCCESS))
		{
			omsResult = CustomCreateOmsOrderAction.this.getOmsOrderService().createOmsOrder(order);
			LOG.debug("After OMS call from Action : " + omsResult.getCause() + " : " + omsResult.getResult());
		}
		else if (order.getOmsSubmitStatus() != null && order.getOmsSubmitStatus().equals(MarketplaceomsservicesConstants.SUCCESS))
		{
			omsResult = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
		}

		if (omsResult != null && omsResult.getResult() != null)
		{
			order.setOmsSubmitStatus(omsResult.getResult().toString());
		}
		if (crmResult != null && crmResult.getResult() != null)
		{
			order.setCrmSubmitStatus(crmResult.getResult().toString());
		}
		getModelService().save(order);
		if (crmResult.getResult().equals(OrderPlacementResult.Status.SUCCESS)
				&& omsResult.getResult().equals(OrderPlacementResult.Status.SUCCESS))
		{
			return AbstractSimpleDecisionAction.Transition.OK;
		}
		//		if (null != omsResult && null != omsResult.getResult()
		//				&& (omsResult.getResult().equals(OrderPlacementResult.Status.FAILED))
		//				&& (order.getExportedToOmsRetryCount().intValue() < CustomCreateOmsOrderAction.this.getMaxRetryCount()))
		//		{
		//			CustomCreateOmsOrderAction.LOG.warn(String.format(
		//					"Failed to send order %s to OMS. Service unavailable. Call will be retried. Error:  %s", new Object[]
		//					{ order.getCode(), omsResult.getCause().getMessage() }));
		//			order.setExportedToOmsRetryCount(Integer.valueOf(order.getExportedToOmsRetryCount().intValue() + 1));
		//			getModelService().save(order);
		//			final RetryLaterException retryLaterException = new RetryLaterException(
		//					"Error occurred during oms order submission of order : " + order.getCode(), omsResult.getCause());
		//			retryLaterException.setRollBack(false);
		//			retryLaterException.setDelay(CustomCreateOmsOrderAction.this.getRetryDelay());
		//			throw retryLaterException;
		//		}

		if (null != omsResult && null != omsResult.getResult() && (omsResult.getResult().equals(OrderPlacementResult.Status.ERROR)))
		{
			CustomCreateOmsOrderAction.LOG.warn(String.format(
					"Failed to send order %s to OMS. Service unavailable. Call will be retried. Error:  %s", new Object[]
					{ order.getCode(), omsResult.getCause().getMessage() }));
			//order.setIsSentToOMS(Boolean.FALSE);
			//getModelService().save(order);
			return AbstractSimpleDecisionAction.Transition.OK;
		}
		if ((crmResult.getResult().equals(OrderPlacementResult.Status.FAILED))
				&& (order.getExportedToCrmRetryCount().intValue() < CustomCreateOmsOrderAction.this.getMaxRetryCount()))
		{
			CustomCreateOmsOrderAction.LOG.warn(String.format(
					"Failed to send order %s to CRM. Service unavailable. Call will be retried. Error:  %s", new Object[]
					{ order.getCode(), crmResult.getCause().getMessage() }));
			order.setExportedToCrmRetryCount(Integer.valueOf(order.getExportedToCrmRetryCount().intValue() + 1));
			getModelService().save(order);
			final RetryLaterException retryLaterException = new RetryLaterException(
					"Error occurred during CRM order submission of order : " + order.getCode(), crmResult.getCause());
			retryLaterException.setRollBack(false);
			retryLaterException.setDelay(CustomCreateOmsOrderAction.this.getRetryDelay());
			throw retryLaterException;
		}
		final OrderPlacementResult result = getOmsOrderService().createOmsOrder(order);

		if (result.getResult().equals(OrderPlacementResult.Status.SUCCESS))
		{
			order.setIsSentToOMS(Boolean.TRUE);
			getModelService().save(order);
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
	 *
	 *
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
	 *
	 *
	 * @param impersonationService
	 *           the impersonationService to set
	 */


	public void setImpersonationService(final ImpersonationService impersonationService)
	{

		this.impersonationService = impersonationService;
	}



	/**
	 * @return the omsOrderService
	 */
	public CustomOmsOrderService getOmsOrderService()
	{
		return omsOrderService;
	}



	/**
	 * @param omsOrderService
	 *           the omsOrderService to set
	 */
	public void setOmsOrderService(final CustomOmsOrderService omsOrderService)
	{
		this.omsOrderService = omsOrderService;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}