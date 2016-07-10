/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.service.MplChangeDeliveryAddressClientService;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplChangeDeliveryAddressRequest;
import com.tisl.mpl.xml.pojo.MplChangeDeliveryAddressResponce;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;


/**
 * @author prasad1
 *
 */
public class MplChangeDeliveryAddressClientServiceImpl implements MplChangeDeliveryAddressClientService
{
	@Autowired
	private ConfigurationService configurationService;
	private static final Logger LOG = Logger.getLogger(MplChangeDeliveryAddressClientServiceImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.service.MplChangeDeliveryAddressClientService#changeDeliveryAddressDataToOMS(com.tisl.mpl.xml.pojo
	 * .MplChangeDeliveryAddressRequest)
	 */
	@Override
	public MplChangeDeliveryAddressResponce changeDeliveryRequestCallToOMS(
			final MplChangeDeliveryAddressRequest changeDeliveryAddressRequest)
	{
		final Client client = Client.create();
		WebResource webResource = null;
		Marshaller marshaller = null;
		String xmlString;
		ClientResponse response = null;
		MplChangeDeliveryAddressResponce responsefromOMS = null;
		final StringWriter stringWriter = new StringWriter();
		if (null != client && null != configurationService)
		{
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.OMS_CHANGE_DELIVERY_URL))
					.build());
		}

		try
		{
			if (null != webResource)
			{
				final JAXBContext context = JAXBContext.newInstance(MplCancelOrderRequest.class);
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
					marshaller.marshal(changeDeliveryAddressRequest, stringWriter);
				}
				xmlString = stringWriter.toString();
				LOG.debug(xmlString);
				LOG.debug("Posting to >>>>>>>>>>>>>>>>>>>>" + webResource.getURI());
				response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("x-tenantId", "single")
						.entity(xmlString).post(ClientResponse.class);
			}
			if (null != response)
			{
				final String output = response.getEntity(String.class);
				LOG.debug("xml response<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + output);
				final JAXBContext jaxbContext = JAXBContext.newInstance(MplOrderIsCancellableResponse.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromOMS = (MplChangeDeliveryAddressResponce) unmarshaller.unmarshal(reader);
				LOG.debug(response);
			}
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecclientservicesConstants.EXCEPTION_IS + ex);
		}
		return responsefromOMS;
	}

}
