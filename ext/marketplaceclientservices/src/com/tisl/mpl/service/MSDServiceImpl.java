package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.facades.data.MSDRequestdata;
//
///**
//// * @author TCS
//// *
//// */



public class MSDServiceImpl implements MSDService
{
	@Resource
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(MSDServiceImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MSDService#checkMSDServiceResponse(com.tisl.mpl.facades.data.MSDRequestdata)
	 */
	@Override
	public String checkMSDServiceResponse(final MSDRequestdata msdRequest)
	{
		String response = null;


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
	private String reverseMSDresponse(final MSDRequestdata msdRequest)
	{

		Client client = null;
		WebResource webResource = null;
		//final MSDResponsedata responsefromMSD = null;
		//MSDResponsedata responsefromMSD = null;
		String msdOutput = null;



		try
		{
			//final ObjectMapper
			final String connectionTimeout = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.MSD_CONNECTION_TIMEOUT, "5000").trim();
			final String readTimeout = configurationService.getConfiguration()
					.getString(MarketplacecclientservicesConstants.MSD_READ_TIMEOUT, "5000").trim();
			//			final String httpErrorCode = configurationService.getConfiguration()
			//					.getString(MarketplacecclientservicesConstants.MSD_HTTP_ERRORCODE, "404,503").trim();



			final String proxyEnabled = configurationService.getConfiguration().getString("proxy.enabled", "false").trim();
			if (proxyEnabled.equalsIgnoreCase("true"))
			{
				final URLConnectionClientHandler uch = new URLConnectionClientHandler(new ConnectionFactory());
				client = new Client(uch);
			}
			else
			{
				client = Client.create();
			}
			client.setConnectTimeout(Integer.valueOf(connectionTimeout));
			client.setReadTimeout(Integer.valueOf(readTimeout));

			webResource = client.resource(UriBuilder.fromUri(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.MSD_WIDGET_URL)).build());

			final MultivaluedMap form = new MultivaluedMapImpl();
			form.add("api_key", msdRequest.getApi_key());
			form.add("product_id", msdRequest.getProduct_id());
			form.add("mad_uuid", msdRequest.getMad_uuid());
			form.add("details", msdRequest.getDetails());
			form.add("widget_list", msdRequest.getWidget_list());
			form.add("num_results", msdRequest.getNum_results());

			final ClientResponse response = webResource
					.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_TYPE.toString())
					.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(ClientResponse.class, form);
			if (response.getStatus() == 200)
			{

				msdOutput = response.getEntity(String.class);
				LOG.debug("MSD Response=>" + msdOutput);

				//				final JAXBContext context = JAXBContext.newInstance(MSDResponsedata.class);
				//				final Unmarshaller unmarshaller = context.createUnmarshaller();
				//
				//				final StringReader reader = new StringReader(msdOutput);
				//				final ObjectMapper mapper = new ObjectMapper();
				//				responsefromMSD = mapper.readValue(reader, MSDResponsedata.class);
				//responsefromMSD = (MSDResponsedata) unmarshaller.unmarshal(reader);

			}
			else
			{
				LOG.debug("MSD Response=>Somthing went wrong..!=>" + response.getStatus());
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		return msdOutput;

	}

	public class ConnectionFactory implements HttpURLConnectionFactory
	{

		Proxy proxy;

		private void initializeProxy()
		{
			final String proxyAddress = configurationService.getConfiguration().getString("proxy.address", "proxy.tcs.com").trim();
			final String proxyPort = configurationService.getConfiguration().getString("proxy.port", "8080").trim();
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress, Integer.parseInt(proxyPort)));
		}

		@Override
		public HttpURLConnection getHttpURLConnection(final URL url) throws IOException
		{
			URLConnection responsefromMSD = null;
			try
			{
				initializeProxy();
				responsefromMSD = url.openConnection(proxy);
			}
			catch (final Exception e)
			{
				LOG.error(e);
			}
			return (HttpURLConnection) responsefromMSD;

		}
	}



}
