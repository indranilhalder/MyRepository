/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.wsdto.ClickToCallWsDTO;


/**
 * @author TCS
 *
 */
public class MplClickToCallWebServiceImpl implements MplClickToCallWebService
{
	private final static Logger LOG = Logger.getLogger(MplClickToCallWebServiceImpl.class.getName());
	@Autowired
	private ConfigurationService configService;

	/**
	 * @return the configService
	 */
	public ConfigurationService getConfigService()
	{
		return configService;
	}

	/**
	 * @param configService
	 *           the configService to set
	 */
	public void setConfigService(final ConfigurationService configService)
	{
		this.configService = configService;
	}

	/**
	 * @throws IOException
	 *
	 */
	@Override
	public String clickToCallWebService(final ClickToCallWsDTO clickToCallWsDTO) throws IOException
	{
		HttpURLConnection connection = null;
		final String proxyPort = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY_PORT);
		final String proxySet = configService.getConfiguration()
				.getString(MarketplacecclientservicesConstants.RATING_PROXY_ENABLED);
		final String proxyHost = configService.getConfiguration().getString(MarketplacecclientservicesConstants.RATING_PROXY);

		String xmlEscapedResponse = null;
		final StringBuilder xmlContent = new StringBuilder(130);
		final StringBuilder sBuilder = new StringBuilder(130);
		final String wsURL = configService.getConfiguration().getString("clicktocall.webservice.url");

		final String encodedCustomerName = URLEncoder.encode(clickToCallWsDTO.getCustomerName(), "UTF-8");
		final String encodedCustomerEmail = clickToCallWsDTO.getCustomerEmail();
		final String encodedCustomerMobile = clickToCallWsDTO.getCustomerMobile();
		final String encodedReason = URLEncoder.encode(clickToCallWsDTO.getReasonToCall(), "UTF-8");

		sBuilder.append("?customerName=");
		sBuilder.append(encodedCustomerName);
		sBuilder.append("&customerEmail=");
		sBuilder.append(encodedCustomerEmail);
		sBuilder.append("&customerMobile=");
		sBuilder.append(encodedCustomerMobile);
		sBuilder.append("&reasonToCall=");
		sBuilder.append(encodedReason);

		final String request = wsURL + sBuilder.toString();

		if (null != proxySet && proxySet.equalsIgnoreCase("true"))
		{
			if (null != proxyHost && null != proxyPort)
			{
				LOG.debug("Connecting via proxy::::::" + request);
				final int proxyPortInt = Integer.parseInt(proxyPort);
				final SocketAddress addr = new InetSocketAddress(proxyHost, proxyPortInt);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				final URL url = new URL(request);
				connection = (HttpURLConnection) url.openConnection(proxy);
				//connection.setRequestMethod("POST");
				LOG.debug("Connected via proxy::::::" + request);

			}
		}
		else
		{
			LOG.debug("Connecting without proxy::::::" + request);
			final URL url = new URL(request);
			connection = (HttpURLConnection) url.openConnection();
			//connection.setRequestMethod("POST");
			LOG.debug("Connected without proxy::::::" + request);

		}

		final InputStream stream = connection.getInputStream();
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
		String line;

		while ((line = bufferedReader.readLine()) != null)
		{
			//xmlContent.append(line).append("\n");
			xmlContent.append(line).append('\n');
		}
		bufferedReader.close();
		if (null != xmlContent.toString())
		{
			xmlEscapedResponse = StringEscapeUtils.unescapeHtml(xmlContent.toString());
			LOG.debug("Click to call web service return " + xmlEscapedResponse);
		}

		return xmlEscapedResponse;
	}
}
