/**
 *
 */
package com.tisl.mpl.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tisl.mpl.service.HomePageAppService;
import com.tisl.mpl.wsdto.ComponentRequestDTO;


/**
 * @author TCS
 *
 */
public class HomePageAppServiceImpl implements HomePageAppService
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.service.HomePageAppService#getAdobeTargetData()
	 */
	private String use;
	private String domain;
	private String method;
	private String proxyPort;
	private String proxyEnabled;
	private String proxyAddress;
	SocketAddress addr = null;
	Proxy proxy = null;
	private static final String NEW_LINE = "\n";

	void setProxy()
	{
		final int portproxy = Integer.parseInt(getProxyPort());
		addr = new InetSocketAddress(getProxyAddress(), portproxy);
		proxy = new Proxy(Proxy.Type.HTTP, addr);
	}

	@Override
	public String getAdobeTargetDataOfferWidget(final ComponentRequestDTO ComponentRequestDTO)
	{
		try
		{
			// YTODO Auto-generated method stub
			final String url = getDomain();
			BufferedReader br = null;

			//final HttpURLConnection urlConnection = null;
			//setProxy();
			//final URL url = new URL(stwUrl);
			//urlConnection = (HttpURLConnection) url.openConnection(proxy);
			/*
			 * urlConnection = (HttpURLConnection) url.openConnection(); urlConnection.setConnectTimeout(15000);//15 secs
			 * urlConnection.setRequestMethod("POST"); urlConnection.setRequestProperty("Content-length", "0");
			 * urlConnection.setRequestProperty("charset", "utf-8"); urlConnection.setRequestProperty("Content-Type",
			 * "application/x-www-form-urlencoded"); urlConnection.setUseCaches(false); urlConnection.connect(); final int
			 * responseCode = urlConnection.getResponseCode();
			 */
			final CloseableHttpClient client = HttpClients.createDefault();
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Content-length", "0");
			httpPost.setHeader("charset", "utf-8");
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonInString = mapper.writeValueAsString(ComponentRequestDTO);
			final StringEntity entity = new StringEntity(jsonInString);
			httpPost.setEntity(entity);
			final CloseableHttpResponse response = client.execute(httpPost);

			System.out.println("Response from Target" + response.getEntity().getContent().toString());
			final int responsecode = response.getStatusLine().getStatusCode();
			switch (responsecode)
			{
				case 200:
				case 201:
					//final BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					final StringBuilder sb = new StringBuilder();
					String readLine;
					while ((readLine = br.readLine()) != null)
					{
						sb.append(readLine + NEW_LINE);
					}
					br.close();
					return sb.toString();
				default://Sonar Fix
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the use
	 */
	public String getUse()
	{
		return use;
	}

	/**
	 * @param use
	 *           the use to set
	 */
	public void setUse(final String use)
	{
		this.use = use;
	}

	/**
	 * @return the domain
	 */
	public String getDomain()
	{
		return domain;
	}

	/**
	 * @param domain
	 *           the domain to set
	 */
	public void setDomain(final String domain)
	{
		this.domain = domain;
	}

	/**
	 * @return the method
	 */
	public String getMethod()
	{
		return method;
	}

	/**
	 * @param method
	 *           the method to set
	 */
	public void setMethod(final String method)
	{
		this.method = method;
	}

	/**
	 * @return the proxyPort
	 */
	public String getProxyPort()
	{
		return proxyPort;
	}

	/**
	 * @param proxyPort
	 *           the proxyPort to set
	 */
	public void setProxyPort(final String proxyPort)
	{
		this.proxyPort = proxyPort;
	}

	/**
	 * @return the proxyEnabled
	 */
	public String getProxyEnabled()
	{
		return proxyEnabled;
	}

	/**
	 * @param proxyEnabled
	 *           the proxyEnabled to set
	 */
	public void setProxyEnabled(final String proxyEnabled)
	{
		this.proxyEnabled = proxyEnabled;
	}

	/**
	 * @return the proxyAddress
	 */
	public String getProxyAddress()
	{
		return proxyAddress;
	}

	/**
	 * @param proxyAddress
	 *           the proxyAddress to set
	 */
	public void setProxyAddress(final String proxyAddress)
	{
		this.proxyAddress = proxyAddress;
	}

}
