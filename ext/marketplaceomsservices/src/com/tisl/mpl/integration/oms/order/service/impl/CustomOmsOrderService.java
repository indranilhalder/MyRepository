package com.tisl.mpl.integration.oms.order.service.impl;

//import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.integration.commons.hystrix.OndemandHystrixCommandConfiguration;
import de.hybris.platform.integration.commons.hystrix.OndemandHystrixCommandFactory;
import de.hybris.platform.integration.oms.order.data.OrderPlacementResult;
import de.hybris.platform.integration.oms.order.service.impl.DefaultOmsOrderService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.util.localization.Localization;

import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.util.Date;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.commons.client.RestCallException;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.RestClientException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.UpdatedSinceList;
import com.hybris.oms.domain.pickupinfo.PickupInfo;
import com.hybris.oms.picupinfo.facade.PickupInfoFacade;
import com.sun.jersey.api.client.ClientHandlerException;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.service.MplCustomerWebService;
import com.tisl.mpl.service.MplSendOrderFromCommerceToCRM;


public class CustomOmsOrderService extends DefaultOmsOrderService implements MplOmsOrderService
{
	private static final Logger LOG = Logger.getLogger(CustomOmsOrderService.class);
	private OndemandHystrixCommandConfiguration hystrixCommandConfig;
	private Converter<OrderModel, Order> orderConverter;
	private OrderFacade orderRestClient;
	private TicketBusinessService ticketBusinessService;
	private ModelService modelService;
	private OndemandHystrixCommandFactory ondemandHystrixCommandFactory;
	private MplSendOrderFromCommerceToCRM ordercreation;
	private MplCustomerWebService mplCustomerWebService;
	@Autowired
	private PickupInfoFacade pickupInfoRestClient;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	@Override
	public OrderPlacementResult createCrmOrder(final OrderModel orderModel)
	{
		//orderModel.setExportedToCrmRetryCount(Integer.valueOf(orderModel.getExportedToCrmRetryCount().intValue() + 1));
		//getModelService().save(orderModel);
		OrderPlacementResult result = null;
		Order order = null;
		try
		{
			order = getOrderConverter().convert(orderModel);
			LOG.debug("Before CRM order call for : " + order.getOrderId());
			// SprintPaymentFixes:- To avoid 26 digit Malformed Ordert ID processing
			if (!order.getOrderId().contains("-"))
			{
				getOrdercreation().orderCreationDataToCRM(order);
				LOG.debug("After CRM order call for : " + order.getOrderId());
				if (orderModel.getUser() != null && null != orderModel.getUser().getUid())
				{
					LOG.debug("Customer update after order place for Order : " + order.getOrderId() + " and Customer"
							+ orderModel.getUser().getUid());
					getMplCustomerWebService().customerModeltoWsData((CustomerModel) orderModel.getUser(), "U", false);
					LOG.debug("Customer update success");
				}
				result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
			}
			else
			{
				LOG.error("CreateOmsOrder -- Wrong Order Processed to CRM Order ID:- " + order.getOrderId());
				result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, new Exception("Malformed OrderId to CRM:: "
						+ order.getOrderId()));
			}
		}
		catch (final Exception ex)
		{
			LOG.error("CreateOmsOrder -- Exception occured while placing order due to  ", ex);
			result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, ex);
		}
		return result;
	}

	@Override
	public OrderPlacementResult createOmsOrder(final OrderModel orderModel)
	{
		OrderPlacementResult result = null;
		Order order = null;
		String httpErrorCode = MarketplacecommerceservicesConstants.EMPTY;

		//Exceptions declared in properties file
		boolean errorFlag = false;
		String[] exceptions = null;

		try
		{
			exceptions = getOmsException();//getting Fallback exceptions

			LOG.info("Fallback Exceptions Are**::" + exceptions);

			httpErrorCode = getConfigurationService().getConfiguration()
					.getString(MarketplacecclientservicesConstants.OMS_HTTP_ERROR_CODE, "404,503").trim();
			order = getOrderConverter().convert(orderModel);
			// SprintPaymentFixes:- To avoid 26 digit Malformed Ordert ID processing
			if (!order.getOrderId().contains("-"))
			{
				//Order request xml and response xml changes made for Audit purpose
				final String requestXml = getOrderAuditXml(order);
				if (StringUtils.isNotEmpty(requestXml))
				{
					orderModel.setRequestXML(requestXml);
				}
				else
				{
					LOG.debug("createOmsOrder requestXml is null or empty ");
				}
				getModelService().save(orderModel);

				final Order orderResponse = getOrderRestClient().createOrder(order);
				final String responseXml = getOrderAuditXml(orderResponse);

				if (StringUtils.isNotEmpty(responseXml))
				{
					orderModel.setResponseXML(responseXml);
				}
				else
				{
					LOG.debug("createOmsOrder responseXml is null or empty ");
				}
				//PaymentFix2017
				orderModel.setIsSentToOMS(Boolean.TRUE);
				getModelService().save(orderModel);
				result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
			}
			else
			{
				LOG.error("CreateOmsOrder -- Wrong Order Processed to OMS Order ID:- " + order.getOrderId());
				result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, new Exception("Malformed OrderId to OMS:: "
						+ order.getOrderId()));

			}
		}
		catch (final DuplicateEntityException dee)
		{
			LOG.error("DuplicateEntityException occured due to order already created", dee);
			//PaymentFix2017
			orderModel.setIsSentToOMS(Boolean.TRUE);
			getModelService().save(orderModel);
			result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
		}
		catch (final RestClientException rce)
		{
			LOG.error("RestClientException occured while creating order", rce);
			result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, rce);

		}
		catch (final ClientHandlerException cex)
		{
			LOG.error("ClientHandlerException occured while creating order", cex);
			result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, cex);
			if (cex.getCause() instanceof SocketTimeoutException)
			{
				LOG.error("SocketTimeoutException occured while creating order", cex);
				result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, cex);

			}
		}
		catch (final RestCallException ex)
		{
			LOG.error("RestCallException occured while creating order", ex);

			for (final String exception : exceptions)
			{
				if (ex.getMessage().contains(exception))
				{
					errorFlag = true;
					break;
				}
			}
			if ((ex.getResponse() != null && ex.getResponse().getStatus() != null && (httpErrorCode.contains(String.valueOf(ex
					.getResponse().getStatus().getStatusCode()))))
					|| errorFlag)
			{
				result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, ex);
			}
		}
		catch (final Exception ex)
		{
			LOG.error("CreateOmsOrder >> Exception occured while placing order due to ", ex);
			LOG.error("***CreateOmsOrder >> Error Is***" + ex.getMessage());
			result = new OrderPlacementResult(OrderPlacementResult.Status.ERROR, ex);
		}
		if (OrderPlacementResult.Status.SUCCESS.equals(result.getResult()))
		{
			orderModel.setOrderExportTime(new Date());
			getModelService().save(orderModel);
		}

		return result;

	}

	//	@Override
	//	public OrderPlacementResult createOmsOrder(final OrderModel orderModel)
	//	{
	//		OrderPlacementResult result = null;
	//		Order order = null;
	//		try
	//		{
	//			order = getOrderConverter().convert(orderModel);
	//			//Order request xml and response xml changes made for Audit purpose
	//			final String requestXml = getOrderAuditXml(order);
	//			if (StringUtils.isNotEmpty(requestXml))
	//			{
	//				orderModel.setRequestXML(requestXml);
	//			}
	//			else
	//			{
	//				LOG.debug("createOmsOrder requestXml is null or empty ");
	//			}
	//			getModelService().save(orderModel);
	//
	//			final Order orderResponse = getOrderRestClient().createOrder(order);
	//			final String responseXml = getOrderAuditXml(orderResponse);
	//
	//			if (StringUtils.isNotEmpty(responseXml))
	//			{
	//				orderModel.setResponseXML(responseXml);
	//			}
	//			else
	//			{
	//				LOG.debug("createOmsOrder responseXml is null or empty ");
	//			}
	//			getModelService().save(orderModel);
	//			result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);
	//
	//		}
	//		catch (final RestCallException e)
	//		{
	//			if ((e.getResponse() != null) && (Response.Status.SERVICE_UNAVAILABLE.equals(e.getResponse().getStatus())))
	//			{
	//				result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, e);
	//			}
	//		}
	//		catch (final Exception ex)
	//		{
	//			LOG.error("CreateOmsOrder >> Exception occured while placing order due to ", ex);
	//			result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, ex);
	//		}
	//		if (OrderPlacementResult.Status.SUCCESS.equals(result.getResult()))
	//		{
	//			orderModel.setOrderExportTime(new Date());
	//			getModelService().save(orderModel);
	//		}
	//
	//		return result;
	//
	//	}

	@Override
	public UpdatedSinceList<String> getUpdatedOrderIds(final Date updatedSince)
	{
		UpdatedSinceList<String> listofOrders = null;
		final Date newSinceDate = new Date(updatedSince.getTime() - 180000L);
		LOG.warn("time offset added: " + updatedSince + " -> " + newSinceDate);
		listofOrders = getOrderRestClient().getOrderIdsUpdatedAfter(updatedSince);
		return listofOrders;
	}

	@Override
	public void flagTheOrderAsFailed(final OrderModel orderModel, final Throwable cause)
	{
		final String ticketTitle = Localization.getLocalizedString("message.ticket.ordernotsent.title");
		final String ticketMessage = Localization.getLocalizedString("message.ticket.ordernotsent.content", new Object[]
		{ orderModel.getCode(), cause.getLocalizedMessage() });
		createTicket(ticketTitle, ticketMessage, orderModel, CsTicketCategory.PROBLEM, CsTicketPriority.HIGH);
	}

	@Override
	public Order getOrderByOrderId(final String orderId)
	{
		Order order = null;
		order = getOrderRestClient().getOrderByOrderId(orderId); //Open the commented line before Push
		return order;
	}

	@Override
	protected CsTicketModel createTicket(final String subject, final String description, final OrderModel orderModel,
			final CsTicketCategory category, final CsTicketPriority priority)
	{
		final CsTicketModel newTicket = (CsTicketModel) getModelService().create(CsTicketModel.class);
		newTicket.setHeadline(subject);
		newTicket.setCategory(category);
		newTicket.setPriority(priority);
		newTicket.setOrder(orderModel);
		newTicket.setCustomer(orderModel.getUser());

		final CsCustomerEventModel newTicketEvent = new CsCustomerEventModel();
		newTicketEvent.setText(description);

		return getTicketBusinessService().createTicket(newTicket, newTicketEvent);
	}


	public void createCRMTicket(final OrderModel orderModel)
	{
		final Order order = returnOrder(orderModel);
		try
		{
			getOrdercreation().orderCreationDataToCRM(order);
			LOG.info(" CRM order call for RMS verfication pending");
			orderModel.setCrmSubmitStatus(MarketplaceomsservicesConstants.SUCCESS);
			getModelService().save(orderModel);
			LOG.debug("After CRM order call for Ticket for order :" + order.getOrderId());
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex.getMessage());
		}
	}


	private Order returnOrder(final OrderModel orderModel)
	{
		final Order order = getOrderConverter().convert(orderModel);
		return order;
	}

	/*
	 * @Desc Used for generating xml
	 *
	 * @param order
	 *
	 * @return String
	 */
	protected String getOrderAuditXml(final Order order)
	{
		Marshaller marshaller = null;
		String xmlString = "";
		final StringWriter writer = new StringWriter();
		try
		{
			final JAXBContext context = JAXBContext.newInstance(Order.class);
			if (null != context)
			{
				marshaller = context.createMarshaller();
				if (null != marshaller)
				{
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
					marshaller.marshal(order, writer);
				}
			}
			xmlString = writer.toString();
			LOG.debug("Order create xml end");
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in getOrderAuditXml due to ", ex);
		}
		return xmlString;
	}

	@Override
	public OndemandHystrixCommandConfiguration getHystrixCommandConfig()
	{
		return this.hystrixCommandConfig;
	}

	@Override
	public void setHystrixCommandConfig(final OndemandHystrixCommandConfiguration hystrixCommandConfig)
	{
		this.hystrixCommandConfig = hystrixCommandConfig;
	}

	@Override
	public Converter<OrderModel, Order> getOrderConverter()
	{
		return this.orderConverter;
	}

	@Override
	public void setOrderConverter(final Converter<OrderModel, Order> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	@Override
	public OrderFacade getOrderRestClient()
	{
		return this.orderRestClient;
	}

	@Override
	public void setOrderRestClient(final OrderFacade orderRestClient)
	{
		this.orderRestClient = orderRestClient;
	}

	@Override
	public TicketBusinessService getTicketBusinessService()
	{
		return this.ticketBusinessService;
	}

	@Override
	public void setTicketBusinessService(final TicketBusinessService ticketBusinessService)
	{
		this.ticketBusinessService = ticketBusinessService;
	}

	@Override
	public ModelService getModelService()
	{
		return this.modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	protected OndemandHystrixCommandFactory getOndemandHystrixCommandFactory()
	{
		return this.ondemandHystrixCommandFactory;
	}

	@Override
	public void setOndemandHystrixCommandFactory(final OndemandHystrixCommandFactory ondemandHystrixCommandFactory)
	{
		this.ondemandHystrixCommandFactory = ondemandHystrixCommandFactory;
	}

	/**
	 * @return the ordercreation
	 */
	public MplSendOrderFromCommerceToCRM getOrdercreation()
	{
		return ordercreation;
	}

	/**
	 * @param ordercreation
	 *           the ordercreation to set
	 */
	public void setOrdercreation(final MplSendOrderFromCommerceToCRM ordercreation)
	{
		this.ordercreation = ordercreation;
	}

	//Update PickUpDetails OMS Call
	public void upDatePickUpDetails(final OrderModel orderModel)
	{
		final PickupInfo pickInfo = new PickupInfo();
		if (null != orderModel.getCode())
		{
			pickInfo.setOrderId(orderModel.getCode());
		}
		if (null != orderModel.getPickupPersonName())
		{
			pickInfo.setPickupPerson(orderModel.getPickupPersonName());
		}
		if (null != orderModel.getPickupPersonMobile())
		{
			pickInfo.setAlternateContactNumber(orderModel.getPickupPersonMobile());
		}
		try
		{
			LOG.info("OMS PickUpDetails Upadet Call");
			//orderRestClient.createOrder(pickInfo);
			pickupInfoRestClient.updatePickupInfo(pickInfo);
		}
		catch (final Exception e)
		{

			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	private String[] getOmsException()
	{
		final String connectTimeoutException = getConfigurationService().getConfiguration().getString(
				MarketplacecclientservicesConstants.OMS_FALLBACK_CONNECT_TIMEOUT_EXCEP);
		final String readTimeoutException = getConfigurationService().getConfiguration().getString(
				MarketplacecclientservicesConstants.OMS_FALLBACK_READ_TIMEOUT_EXCEP);

		final StringBuffer stb = new StringBuffer(300);

		if (StringUtils.isNotEmpty(connectTimeoutException))
		{
			stb.append(connectTimeoutException);
		}
		if (StringUtils.isNotEmpty(readTimeoutException))
		{
			stb.append(',');
			stb.append(readTimeoutException);
		}
		return stb.toString().split(",");
	}

	/**
	 * @return the mplCustomerWebService
	 */
	public MplCustomerWebService getMplCustomerWebService()
	{
		return mplCustomerWebService;
	}

	/**
	 * @param mplCustomerWebService
	 *           the mplCustomerWebService to set
	 */
	public void setMplCustomerWebService(final MplCustomerWebService mplCustomerWebService)
	{
		this.mplCustomerWebService = mplCustomerWebService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}