/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringWriter;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hybris.oms.domain.order.Order;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;


/**
 * @author TCS
 *
 */

@Component
@Qualifier("mplSendOrder")
public class MplSendOrderFromCommerceToCRMImpl implements MplSendOrderFromCommerceToCRM
{

	private static final Logger LOG = Logger.getLogger(MplSendOrderFromCommerceToCRMImpl.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

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

	@Override
	public void orderCreationDataToCRM(final Order orderData) throws Exception
	{
		final Client client = Client.create();
		WebResource webResource = null;
		Marshaller marshaller = null;
		String xmlString = "";
		final StringWriter writer = new StringWriter();
		ClientResponse response = null;

		LOG.info(">>>>>>>>>>>>>> Inside orderCreationDataToCRM <<<<<<<<<<<");
		if (null != client && null != configurationService && null != configurationService.getConfiguration())
		{
			final String password = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_PASSWORD);
			final String userId = configurationService.getConfiguration().getString(
					MarketplacecclientservicesConstants.CUSTOMERMASTER_ENDPOINT_USERID);
			client.addFilter(new HTTPBasicAuthFilter(userId, password));
			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString("crm.ordercreate.endpoint.url")).build());
		}


		final JAXBContext context = JAXBContext.newInstance(Order.class);

		if (null != context)
		{
			marshaller = context.createMarshaller();

			if (null != marshaller)
			{
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.marshal(orderData, writer);
			}
		}
		xmlString = writer.toString();
		xmlString = xmlString.substring(204);
		xmlString = xmlString.replace("ns2:order", "order");
		xmlString = ("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><order>").concat(xmlString);
		LOG.info(xmlString);
		if (null != webResource)
		{
			response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
		}
		if (response.getStatus() != 200)
		{
			throw new Exception(response.getStatus() + MarketplacecclientservicesConstants.E0019);
		}
		else
		{
			final String output = response.getEntity(String.class);
			LOG.info("Response output order Create : " + output);
		}

	}
}
