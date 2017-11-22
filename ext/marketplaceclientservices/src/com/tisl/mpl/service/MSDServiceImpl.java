package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import javax.annotation.Resource;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
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

	private static final Logger LOG = Logger.getLogger(MSDServiceImpl.class);

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
	//	private MSDResponsedata reverseMSDresponse(final MSDRequestdata msdRequest)
	//	{
	//
	//		final Client client = Client.create();
	//		ClientResponse response = null;
	//		WebResource webResource = null;
	//		MSDResponsedata responsefromMSD = null;
	//
	//		try
	//		{
	//			//final ObjectMapper
	//			final String connectionTimeout = configurationService.getConfiguration()
	//					.getString(MarketplacecclientservicesConstants.MSD_CONNECTION_TIMEOUT, "5000").trim();
	//			final String readTimeout = configurationService.getConfiguration()
	//					.getString(MarketplacecclientservicesConstants.MSD_READ_TIMEOUT, "5000").trim();
	//			final String httpErrorCode = configurationService.getConfiguration()
	//					.getString(MarketplacecclientservicesConstants.MSD_HTTP_ERRORCODE, "404,503").trim();
	//
	//			client.setConnectTimeout(Integer.valueOf(connectionTimeout));
	//			client.setReadTimeout(Integer.valueOf(readTimeout));
	//			//End : Code added for OMS fallback cases
	//
	//			webResource = client.resource(UriBuilder.fromUri(
	//					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.MSD_WIDGET_URL)).build());
	//
	//
	//			/*
	//			 * final JAXBContext context = JAXBContext.newInstance(MSDRequestdata.class); final Marshaller marshaller =
	//			 * context.createMarshaller(); //for pretty-print XML in JAXB
	//			 * marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); final StringWriter stringWriter =
	//			 * new StringWriter();
	//			 *
	//			 * if (null != msdRequest) { marshaller.marshal(msdRequest, stringWriter); }
	//			 */
	//
	//
	//			final ObjectMapper mapper = new ObjectMapper();
	//
	//			final String jsonString = mapper.writeValueAsString(msdRequest);
	//
	//			response = webResource.type(MediaType.APPLICATION_JSON).accept("application/json").entity(jsonString)
	//					.post(ClientResponse.class);
	//
	//
	//			if (null != response)
	//			{
	//				final String output = response.getEntity(String.class);
	//				responsefromMSD = mapper.readValue(output, MSDResponsedata.class);
	//
	//				//				final JAXBContext jaxbContext = JAXBContext.newInstance(MSDResponsedata.class);
	//				//				Unmarshaller unmarshaller = null;
	//				//				if (null != jaxbContext)
	//				//				{
	//				//					unmarshaller = jaxbContext.createUnmarshaller();
	//				//				}
	//				//				final StringReader reader = new StringReader(output);
	//				//				responsefromMSD = (MSDResponsedata) unmarshaller.unmarshal(reader);
	//			}
	//			else
	//			{
	//				LOG.debug(" ************** Reverse MSD avaliablity response is null");
	//			}
	//		}
	//		catch (final Exception e)
	//		{
	//			e.printStackTrace();
	//		}
	//		return responsefromMSD;
	//
	//	}
	private MSDResponsedata reverseMSDresponse(final MSDRequestdata msdRequest)
	{

		final Client client = Client.create();
		final ClientResponse response = null;
		WebResource webResource = null;
		MSDResponsedata responsefromMSD = null;
		HttpURLConnection purgeUrlHTTPConnection = null;
		OutputStream os = null;
		BufferedReader reader = null;
		final BufferedReader statusReader = null;
		final StringBuffer sb = new StringBuffer();

		try
		{
			//final ObjectMapper
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


			final ObjectMapper mapper = new ObjectMapper();

			final String jsonString = mapper.writeValueAsString(msdRequest);
			purgeUrlHTTPConnection = getURLConnection(
					configurationService.getConfiguration().getString(MarketplacecclientservicesConstants.MSD_WIDGET_URL), "POST");

			if (null != purgeUrlHTTPConnection)
			{
				purgeUrlHTTPConnection.setRequestProperty("Content-Length", Integer.toString(jsonString.getBytes().length));

				//Get OutputStream to send JSON in request Body.
				LOG.error("DEBUG: Going to call the purgeUrlHTTPConnection.getOutputStream() .......");
				os = purgeUrlHTTPConnection.getOutputStream();
				os.write(jsonString.getBytes());
				os.flush();
				os.close();

				//Get InputStream to read response.
				LOG.error("DEBUG: Going to call the purgeUrlHTTPConnection.getInputStream() .......");
				reader = new BufferedReader(new InputStreamReader(purgeUrlHTTPConnection.getInputStream()));

				String line;
				while (reader != null && ((line = reader.readLine()) != null))
				{
					sb.append(line);
				}

				if (sb != null && sb.toString().trim().length() > 0)
				{
					LOG.error("DEBUG: Returned Response:\n" + sb.toString());
				}
				else
				{
					LOG.error("ERROR: !!! in gtting response(null or Empty) from ..");
				}
			}
			else
			{
				LOG.error("ERROR: in getting Connection " + jsonString);
			}
			/*
			 * final JAXBContext context = JAXBContext.newInstance(MSDRequestdata.class); final Marshaller marshaller =
			 * context.createMarshaller(); //for pretty-print XML in JAXB
			 * marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); final StringWriter stringWriter =
			 * new StringWriter();
			 * 
			 * if (null != msdRequest) { marshaller.marshal(msdRequest, stringWriter); }
			 */




			//			response = webResource.type(MediaType.APPLICATION_JSON).accept("application/json").entity(jsonString)
			//					.post(ClientResponse.class);


			//if (null != sb)
			{
				final String output = sb.toString();
				responsefromMSD = mapper.readValue(output, MSDResponsedata.class);

				//				final JAXBContext jaxbContext = JAXBContext.newInstance(MSDResponsedata.class);
				//				Unmarshaller unmarshaller = null;
				//				if (null != jaxbContext)
				//				{
				//					unmarshaller = jaxbContext.createUnmarshaller();
				//				}
				//				final StringReader reader = new StringReader(output);
				//				responsefromMSD = (MSDResponsedata) unmarshaller.unmarshal(reader);
			}
			//			else
			//			{
			//				LOG.debug(" ************** Reverse MSD avaliablity response is null");
			//			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return responsefromMSD;

	}

	private HttpURLConnection getURLConnection(final String url, final String methodSent)
	{
		Proxy proxy = null;
		HttpURLConnection urlHTTPConn = null;

		try
		{
			final String proxyEnabled = configurationService.getConfiguration().getString("proxy.enabled");
			final String proxyAddress = configurationService.getConfiguration().getString("proxy.address");
			final String proxyPort = configurationService.getConfiguration().getString("proxy.port");
			final URL urlObj = new URL(url);
			if (proxyEnabled != null && "true".equals(proxyEnabled) && !StringUtils.isEmpty(proxyAddress)
					&& !StringUtils.isEmpty(proxyPort))
			{
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddress.trim(), Integer.parseInt(proxyPort.trim())));
				urlHTTPConn = (HttpURLConnection) urlObj.openConnection(proxy);
			}
			else
			{
				urlHTTPConn = (HttpURLConnection) urlObj.openConnection();
			}

			if (StringUtils.isNotEmpty(methodSent))
			{
				urlHTTPConn.setRequestMethod(methodSent);
			}
			else
			{
				urlHTTPConn.setRequestMethod("GET");
			}
			urlHTTPConn.setRequestProperty("Accept", "application/json");
			urlHTTPConn.setRequestProperty("Content-type", "application/json");
			urlHTTPConn.setRequestProperty("Content-Language", "en-US");
			urlHTTPConn.setRequestProperty("charset", "utf-8");
			urlHTTPConn.setUseCaches(false);
			urlHTTPConn.setDoInput(true);
			urlHTTPConn.setDoOutput(true);

			return urlHTTPConn;
		}
		catch (final Exception e)
		{
			LOG.error("ERROR !! in getting getURLConnection() " + e);
			e.printStackTrace();
		}

		return null;
	}
}
