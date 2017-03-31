/**
 *
 */
package com.tisl.mpl.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

import org.apache.log4j.Logger;

import com.tisl.mpl.service.STWWidgetService;


/**
 * @author TCS
 *
 */
public class STWWidgetServiceImpl implements STWWidgetService
{

	Logger LOG = Logger.getLogger(this.getClass().getName());
	private String use;
	private String domain;
	private String method;
	private String proxyPort;
	private String proxyEnabled;
	private String proxyAddress;

	SocketAddress addr = null;
	Proxy proxy = null;

	void setProxy()
	{
		final int portproxy = Integer.parseInt(getProxyPort());
		addr = new InetSocketAddress(getProxyAddress(), portproxy);
		proxy = new Proxy(Proxy.Type.HTTP, addr);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.STWWidgetService#callSTWService()
	 */


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
	 * @param proxyAddress
	 *           the proxyAddress to set
	 */
	public void setProxyAddress(final String proxyAddress)
	{
		this.proxyAddress = proxyAddress;
	}



	@Override
	public String callSTWService()
	{

		HttpURLConnection urlConnection = null;
		BufferedReader br = null;

		try
		{
			setProxy();
			final String stwUrl = getDomain() + "/" + getMethod()
					+ "?pageType=Home&widgetType=STW&siteType=Marketplace&sendOnlyListingId=false";
			final URL url = new URL(stwUrl);
			//	final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(proxy);
			urlConnection = (HttpURLConnection) url.openConnection(proxy);

			urlConnection.setConnectTimeout(15000);//15 secs
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-length", "0");
			urlConnection.setRequestProperty("charset", "utf-8");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setUseCaches(false);
			urlConnection.connect();

			final int responseCode = urlConnection.getResponseCode();

			switch (responseCode)
			{
				case 200:
				case 201:
					//final BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					final StringBuilder sb = new StringBuilder();
					String readLine;
					while ((readLine = br.readLine()) != null)
					{
						sb.append(readLine + "\n");
					}
					br.close();
					return sb.toString();
			}

		}
		catch (final IOException e)
		{
			LOG.error("STW connection failed unable to render the widget", e);
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (final IOException e)
				{
					LOG.error("BufferReader  unable to close ", e);
				}
			}
			if (urlConnection != null)
			{
				urlConnection.disconnect();
			}

		}
		return null;
	}

}
