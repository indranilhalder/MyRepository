package com.tisl.mpl.service;


import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringWriter;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import com.hybris.oms.domain.order.Order;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;





public class OrderWebServiceImpl implements OrderWebService
{
	private static final Logger LOG = Logger.getLogger(OrderWebServiceImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.OrderWebService#createOmsOrder(com.hybris.oms.domain.order.Order)
	 */
	@Override
	public Order createOmsOrder(final Order orderData)
	{
		// YTODO Auto-generated method stub

		// YTODO Auto-generated method stub

		final Client client = Client.create();
		WebResource webResource = null;
		final StringWriter stringWriter = new StringWriter();
		Marshaller marshaller = null;
		Order response = null;
		try
		{
			LOG.debug("OMS Order Create Call Starting for" + orderData.getOrderId());
			if (null != client)
			{
				final String connectionTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_SEND_ORDER_CON_TIMEOUT, "5000").trim();
				final String readTimeout = configurationService.getConfiguration()
						.getString(MarketplacecclientservicesConstants.OMS_SEND_ORDER_READ_TIMEOUT, "5000").trim();
				client.setConnectTimeout(Integer.valueOf(connectionTimeout));
				client.setReadTimeout(Integer.valueOf(readTimeout));
				webResource = client.resource(UriBuilder.fromUri(
						configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.OMS_ORDER_CREATE_URL))
						.build());
			}
			final JAXBContext context = JAXBContext.newInstance(Order.class);

			if (null != context)
			{
				marshaller = context.createMarshaller();

			}
			if (null != marshaller)
			{
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			}

			if (null != marshaller)
			{
				marshaller.marshal(orderData, stringWriter);
			}
			if (null != webResource)
			{
				LOG.debug(stringWriter.toString());
				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("X-tenantId", "single")
						.header("X-locale", "en").header("X-role", "accelerator").entity(stringWriter.toString()).post(Order.class);
			}


		}

		catch (final JAXBException e)
		{
			// YTODO Auto-generated catch block
			LOG.error("JAXBException while Create Order ID : " + orderData.getOrderId() + "and Exception " + e.getMessage());
		}
		return response;
	}
}