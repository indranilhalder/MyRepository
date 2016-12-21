package com.tisl.mpl.shorturl.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;


public class ConnectionFactory implements HttpURLConnectionFactory
{

	ClientConfig config = new DefaultClientConfig();
	Client client = new Client(new URLConnectionClientHandler(new HttpURLConnectionFactory()
	{
		Proxy p = null;

		@Override
		public HttpURLConnection getHttpURLConnection(final URL url) throws IOException
		{
			if (p == null)
			{
				if (/* System.getProperties().containsKey("http.proxyHost") */true)
				{
					p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.78.1", 8080));
				}
				else
				{
					p = Proxy.NO_PROXY;
				}
			}
			return (HttpURLConnection) url.openConnection(p);
		}
	}), config);
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.jersey.client.urlconnection.HttpURLConnectionFactory#getHttpURLConnection(java.net.URL)
	 */
	//	@Override
	//	public HttpURLConnection getHttpURLConnection(URL paramURL) throws IOException
	//	{
	//		// YTODO Auto-generated method stub
	//		return null;
	//	}
	Proxy proxy;

	private void initializeProxy()
	{
		//  proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("myproxy.com", 3128));
		proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.78.1", 8080));
	}

	@Override
	public HttpURLConnection getHttpURLConnection(final URL url) throws IOException
	{
		Proxy p = null;
		if (/* System.getProperties().containsKey("http.proxyHost") */true)
		{
			p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.10.78.1", 8080));
		}
		else
		{
			p = Proxy.NO_PROXY;
		}
		return (HttpURLConnection) url.openConnection(p);
	}
}