/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


/**
 * @author TCS
 * @Description :create url
 * @return : void
 */
public class MerchantMasterWebServiceImpl
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(MerchantMasterWebServiceImpl.class);

	public void merchantMaster(final String xmlString)
	{
		final Client client = Client.create();
		final WebResource webResource = client.resource(UriBuilder.fromUri(
				configurationService.getConfiguration().getString("merchantmaster_url")).build());
		try
		{

			final ClientResponse response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").entity(xmlString)
					.post(ClientResponse.class);
			LOG.info(response);
		}

		catch (final Exception ex)
		{
			LOG.debug("EXCEPTION IS" + ex);
		}

	}

}
