/**
 *
 */
package com.tisl.mpl.security.captcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import net.tanesha.recaptcha.http.HttpLoader;

import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class MplHttpLoader implements HttpLoader
{
	protected static final Logger LOG = Logger.getLogger(MplHttpLoader.class);

	/**
	 * @description this get method is used to set connection with proxy
	 * @return String
	 */
	@Override
	public String httpGet(final String urlS)
	{
		InputStream in = null;
		HttpURLConnection connection = null;
		try
		{
			final URL url = new URL(urlS);
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.18.18.12", 8080));


			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.connect();
			//setJdk15Timeouts(connection);

			in = connection.getInputStream();

			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final byte[] buf = new byte[1024];
			while (true)
			{
				final int rc = in.read(buf);
				if (rc <= 0)
				{
					break;
				}
				bout.write(buf, 0, rc);
			}

			final String rc = bout.toString();

			return rc;
		}
		catch (final IOException e)
		{
			LOG.info("exception>>>>>>" + e.getMessage());
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (final Exception e)
			{
				LOG.info("exception>>>>>>" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * @description this post method is used to set connection with proxy
	 * @return String
	 */
	@Override
	public String httpPost(final String urlS, final String postdata)
	{
		InputStream in = null;
		URLConnection connection = null;

		try
		{

			final URL url = new URL(urlS);
			LOG.info("setting proxy now and connecting.....");
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.18.18.12", 8080));
			connection = url.openConnection(proxy);

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);

			final OutputStream out = connection.getOutputStream();
			out.write(postdata.getBytes());
			out.flush();

			in = connection.getInputStream();

			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final byte[] buf = new byte[1024];
			while (true)
			{
				final int rc = in.read(buf);
				if (rc <= 0)
				{
					break;
				}
				bout.write(buf, 0, rc);
			}

			out.close();
			in.close();

			final String rc = bout.toString();

			return rc;
		}
		catch (final IOException e)
		{
			LOG.info("exception>>>>>>" + e.getMessage());
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
			}
			catch (final Exception e)
			{
				LOG.info("exception>>>>>>" + e.getMessage());
			}
		}
		return null;
	}


}
