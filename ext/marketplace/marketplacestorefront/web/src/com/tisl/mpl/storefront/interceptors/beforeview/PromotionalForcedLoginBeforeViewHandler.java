/**
 *
 */
package com.tisl.mpl.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorfacades.device.DeviceDetectionFacade;
import de.hybris.platform.acceleratorfacades.device.data.DeviceData;
import de.hybris.platform.commercefacades.user.UserFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.tisl.mpl.storefront.interceptors.BeforeViewHandler;


/**
 * @author TCS
 *
 */
public class PromotionalForcedLoginBeforeViewHandler implements BeforeViewHandler
{
	/**
	 * This class checks whether user browsing is logged in or anonymous user.
	 */
	Logger LOG = Logger.getLogger(this.getClass().getName());
	@Resource(name = "userFacade")
	private UserFacade userFacade;
	@Resource(name = "deviceDetectionFacade")
	private DeviceDetectionFacade deviceDetectionFacade;

	private String domain;
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

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		final String forcedLoginAttr = request.getParameter("boxed-login");
		if (null != forcedLoginAttr && userFacade.isAnonymousUser())
		{
			final URL requestUrl = new URL(request.getRequestURL().toString());
			final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
			final StringBuilder builder = new StringBuilder(150);
			builder.append(requestUrl.getProtocol());
			builder.append("://");
			builder.append(requestUrl.getHost());
			builder.append(portString);
			final String baseUrl = builder.toString();
			setDomain(baseUrl);
			modelAndView.addObject("forced_login_user", "Y");
			final String loginHTML = this.getHTMLFromURL("/login?frame=true&box-login");
			modelAndView.addObject("login_register_html", loginHTML);
			//device detection
			deviceDetectionFacade.initializeRequest(request);
			final DeviceData deviceData = deviceDetectionFacade.getCurrentDetectedDevice();
			modelAndView.addObject("is_mobile", deviceData.getMobileBrowser());
		}
		else
		{
			modelAndView.addObject("forced_login_user", "N");
		}

	}

	/**
	 * getHTMLFromURL
	 *
	 * @param urlto
	 * @return String
	 */
	public String getHTMLFromURL(final String urlto)
	{
		HttpURLConnection urlConnection = null;
		BufferedReader br = null;
		try
		{
			setProxy();
			final String Url = getDomain() + urlto;
			final URL url = new URL(Url);
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
				default://Sonar Fix
			}

		}
		catch (final IOException e)
		{
			LOG.error("Connection failed unable to get HTML", e);
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

	/**
	 * @return the userFacade
	 */
	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	/**
	 * @param userFacade
	 *           the userFacade to set
	 */
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
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
	 * @param proxyAddress
	 *           the proxyAddress to set
	 */
	public void setProxyAddress(final String proxyAddress)
	{
		this.proxyAddress = proxyAddress;
	}
}
