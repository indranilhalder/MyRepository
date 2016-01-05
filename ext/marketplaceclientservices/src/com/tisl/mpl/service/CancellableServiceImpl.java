/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.StringReader;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.xml.pojo.CancellableRequestData;
import com.tisl.mpl.xml.pojo.CancellableResponse;


/**
 * @author TCS
 *
 */
@Component
@Qualifier("cancellableService")
public class CancellableServiceImpl implements CancellableService
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(CancellableServiceImpl.class);

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
	public CancellableResponse cancelableCheck(final String orderId, final String transactionId)
	{
		CancellableResponse response = new CancellableResponse();
		try
		{
			final CancellableRequestData request = new CancellableRequestData();
			if (null != orderId && null != transactionId)
			{
				request.setOrderId(orderId);
				request.setTransactionId(transactionId);
			}
			response = cancellableStatus(request);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return response;

	}

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("javadoc")
	private CancellableResponse cancellableStatus(final CancellableRequestData req)
	{
		{
			final Client client = Client.create();
			WebResource webResource = null;
			if (null != configurationService && null != configurationService.getConfiguration()
					&& null != configurationService.getConfiguration().getString("Comerce.oms.isCancellable.url"))
			{
				webResource = client.resource(UriBuilder.fromUri(
						configurationService.getConfiguration().getString("Comerce.oms.isCancellable.url")).build());
			}
			CancellableResponse responsefromOMS = new CancellableResponse();

			try
			{
				final JAXBContext context = JAXBContext.newInstance(CancellableRequestData.class);
				final Marshaller marshaller = context.createMarshaller(); //for pretty-print XML in JAXB
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				final StringWriter stringWriter = new StringWriter();
				if (null != req)
				{
					marshaller.marshal(req, stringWriter);
				}
				final String xmlString = stringWriter.toString();
				//m.marshal(pincodeRequest, new File("D:\\Int_pinCode_v1.xml"));
				ClientResponse response = null;
				if (null != webResource)
				{
					response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
							.post(ClientResponse.class);
				}
				//String output = new String();
				String output = "";

				if (null != response)
				{
					output = response.getEntity(String.class);
				}

				final JAXBContext jaxbContext = JAXBContext.newInstance(CancellableResponse.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromOMS = (CancellableResponse) unmarshaller.unmarshal(reader);
			}
			catch (final Exception e)
			{
				LOG.error(e.getMessage());
			}
			return responsefromOMS;
		}
	}

}
