package com.tisl.mpl.integration.oms.order.service.impl;

//import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.integration.commons.hystrix.OndemandHystrixCommandConfiguration;
import de.hybris.platform.integration.commons.hystrix.OndemandHystrixCommandFactory;
import de.hybris.platform.integration.oms.order.data.OrderPlacementResult;
import de.hybris.platform.integration.oms.order.service.OmsOrderService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.events.model.CsCustomerEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketBusinessService;
import de.hybris.platform.util.localization.Localization;

import java.io.StringWriter;
import java.util.Date;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.commons.client.RestCallException;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.UpdatedSinceList;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.service.MplCustomerWebService;
import com.tisl.mpl.service.MplSendOrderFromCommerceToCRM;


public class CustomOmsOrderService implements OmsOrderService
{
	private static final Logger LOG = Logger.getLogger(CustomOmsOrderService.class);
	private OndemandHystrixCommandConfiguration hystrixCommandConfig;
	private Converter<OrderModel, Order> orderConverter;
	private OrderFacade orderRestClient;
	private TicketBusinessService ticketBusinessService;
	private ModelService modelService;
	private OndemandHystrixCommandFactory ondemandHystrixCommandFactory;


	@Autowired
	private MplSendOrderFromCommerceToCRM ordercreation;
	@Autowired
	private MplCustomerWebService mplCustomerWebService;



	public OrderPlacementResult createOmsOrder(final OrderModel orderModel)
	{
		orderModel.setExportedToOmsRetryCount(Integer.valueOf(orderModel.getExportedToOmsRetryCount().intValue() + 1));
		getModelService().save(orderModel);
		OrderPlacementResult result = null;
		Order order = null;

		try
		{
			//Order request xml and response xml changes made for Audit purpose

			order = getOrderConverter().convert(orderModel);
			LOG.debug(">>>>>>>>>>>>> before OMS order call <<<<<<<<<<<<<<<<");
			ordercreation.orderCreationDataToCRM(order);
			LOG.debug(">>>>>>>>>>>>> After CRM order call <<<<<<<<<<<<<<<<");
			if (null != orderModel.getUser().getUid())
			{
				LOG.debug(">>>>>>>>>>>>> calling customer update after order place <<<<<<<<<<<<<<<<");
				mplCustomerWebService.customerModeltoWsData((CustomerModel) orderModel.getUser(), "U", false);
				LOG.debug(">>>>>>>>>>>>>******* customer update success *********<<<<<<<<<<<<<<<<");
			}

			final String requestXml = getOrderAuditXml(order);

			if (StringUtils.isNotEmpty(requestXml))
			{
				orderModel.setRequestXML(requestXml);
			}
			else
			{
				LOG.debug("CustomOmsOrderService : createOmsOrder requestXml is null or empty ");
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
				LOG.debug("CustomOmsOrderService : createOmsOrder responseXml is null or empty ");
			}

			getModelService().save(orderModel);





			result = new OrderPlacementResult(OrderPlacementResult.Status.SUCCESS);

		}
		catch (final RestCallException e)
		{
			if ((e.getResponse() != null) && (Response.Status.SERVICE_UNAVAILABLE.equals(e.getResponse().getStatus())))
			{
				result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, e);
			}
		}
		catch (final Exception ex)
		{
			LOG.error("CustomOmsOrderService >> createOmsOrder >> Exception occured while placing order due to  ", ex);
			result = new OrderPlacementResult(OrderPlacementResult.Status.FAILED, ex);
		}


		if (OrderPlacementResult.Status.SUCCESS.equals(result.getResult()))
		{
			orderModel.setOrderExportTime(new Date());
			getModelService().save(orderModel);
		}

		return result;

	}

	public UpdatedSinceList<String> getUpdatedOrderIds(final Date updatedSince)
	{
		UpdatedSinceList<String> listofOrders = null;
		final Date newSinceDate = new Date(updatedSince.getTime() - 180000L);
		LOG.warn("time offset added: " + updatedSince + " -> " + newSinceDate);
		listofOrders = getOrderRestClient().getOrderIdsUpdatedAfter(updatedSince);
		return listofOrders;
	}

	public void flagTheOrderAsFailed(final OrderModel orderModel, final Throwable cause)
	{
		final String ticketTitle = Localization.getLocalizedString("message.ticket.ordernotsent.title");
		final String ticketMessage = Localization.getLocalizedString("message.ticket.ordernotsent.content", new Object[]
		{ orderModel.getCode(), cause.getLocalizedMessage() });
		createTicket(ticketTitle, ticketMessage, orderModel, CsTicketCategory.PROBLEM, CsTicketPriority.HIGH);
	}

	public Order getOrderByOrderId(final String orderId)
	{
		Order order = null;
		order = getOrderRestClient().getOrderByOrderId(orderId); //Open the commented line before Push
		return order;
	}

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
			ordercreation.orderCreationDataToCRM(order);
			LOG.debug(">>>>>>>>>>>>> After CRM order call <<<<<<<<<<<<<<<<");
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
			/*
			 * if (xmlString.length() > 4000) { xmlString = xmlString.substring(0, 3998); }
			 */
			LOG.debug("Order create xml end   =======================");
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in getOrderAuditXml due to ", ex);
		}
		return xmlString;
	}

	public OndemandHystrixCommandConfiguration getHystrixCommandConfig()
	{
		return this.hystrixCommandConfig;
	}

	public void setHystrixCommandConfig(final OndemandHystrixCommandConfiguration hystrixCommandConfig)
	{
		this.hystrixCommandConfig = hystrixCommandConfig;
	}

	public Converter<OrderModel, Order> getOrderConverter()
	{
		return this.orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, Order> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public OrderFacade getOrderRestClient()
	{
		return this.orderRestClient;
	}

	@Required
	public void setOrderRestClient(final OrderFacade orderRestClient)
	{
		this.orderRestClient = orderRestClient;
	}

	public TicketBusinessService getTicketBusinessService()
	{
		return this.ticketBusinessService;
	}

	@Required
	public void setTicketBusinessService(final TicketBusinessService ticketBusinessService)
	{
		this.ticketBusinessService = ticketBusinessService;
	}

	public ModelService getModelService()
	{
		return this.modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected OndemandHystrixCommandFactory getOndemandHystrixCommandFactory()
	{
		return this.ondemandHystrixCommandFactory;
	}

	@Required
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
}