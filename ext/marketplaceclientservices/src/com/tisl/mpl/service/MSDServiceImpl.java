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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.facades.data.MSDRequestdata;
import com.tisl.mpl.facades.data.MSDResponsedata;
//
///**
//// * @author TCS
//// *
//// */



public class MSDServiceImpl implements MSDService
{
	@Resource
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(ReturnLogisticsServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.MSDService#checkMSDServiceResponse(com.tisl.mpl.facades.data.MSDRequestdata)
	 */
	@Override
	public MSDResponsedata checkMSDServiceResponse(final MSDRequestdata msdRequest)
	{
		MSDResponsedata response = null;


		try
		{
			if (null != msdRequest)
			{

				response = reverseMSDresponse(msdRequest);

			}
		}

		catch (final Exception e)
		{
			LOG.error(e.getMessage());
			throw e;
		}

		return response;
	}

	@SuppressWarnings("unused")
	private MSDResponsedata reverseMSDresponse(final MSDRequestdata msdRequest)
	{

		final Client client = Client.create();
		ClientResponse response = null;
		WebResource webResource = null;
		MSDResponsedata responsefromMSD = new MSDResponsedata();

		try
		{

			final String connectionTimeout = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.MSD_CONNECTION_TIMEOUT, "5000").trim();
			final String readTimeout = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.MSD_READ_TIMEOUT, "5000").trim();
			final String httpErrorCode = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.MSD_HTTP_ERRORCODE, "404,503").trim();

			client.setConnectTimeout(Integer.valueOf(connectionTimeout));
			client.setReadTimeout(Integer.valueOf(readTimeout));
			//End : Code added for OMS fallback cases

			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.MSD_WIDGET_URL)).build());


			final JAXBContext context = JAXBContext.newInstance(MSDRequestdata.class);
			final Marshaller marshaller = context.createMarshaller(); //for pretty-print XML in JAXB
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			final StringWriter stringWriter = new StringWriter();

			if (null != msdRequest)
			{
				marshaller.marshal(msdRequest, stringWriter);
			}
			final String xmlString = stringWriter.toString();

			//LOG.debug(" ************** Checking reverse logisticts avaliablity request xml" + xmlString);

			response = webResource.type(MediaType.APPLICATION_XML).accept("application/xml").header("x-tenantId", "single")
					.entity(xmlString).post(ClientResponse.class);

			if (null != response)
			{
				final String output = response.getEntity(String.class);
				//LOG.debug(" ************** Reverse logisticts avaliablity response xml" + output);

				final JAXBContext jaxbContext = JAXBContext.newInstance(MSDResponsedata.class);
				Unmarshaller unmarshaller = null;
				if (null != jaxbContext)
				{
					unmarshaller = jaxbContext.createUnmarshaller();
				}
				final StringReader reader = new StringReader(output);
				responsefromMSD = (MSDResponsedata) unmarshaller.unmarshal(reader);
			}
			else
			{
				LOG.debug(" ************** Reverse MSD avaliablity response is null");
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return responsefromMSD;

	}
}
