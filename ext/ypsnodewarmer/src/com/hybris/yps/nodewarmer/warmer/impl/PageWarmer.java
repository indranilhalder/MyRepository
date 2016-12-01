/**
 * 
 */
package com.hybris.yps.nodewarmer.warmer.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import com.hybris.yps.nodewarmer.warmer.WarmerStrategy;


/**
 * @author brendan.dobbs
 * 
 */
public class PageWarmer implements WarmerStrategy
{
	private List<String> urls;
	private static final String USER_AGENT = "PageWarmerAgent";
	private String hostname;
	private boolean throwExceptionOnFailure = true;
	private static final Logger LOG = Logger.getLogger(PageWarmer.class);

	/**
	 * @param hostname
	 *           the hostname to set
	 */
	public void setHostname(final String hostname)
	{
		this.hostname = hostname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.yps.nodewarmer.warmer.WarmerStrategy#execute()
	 */
	@Override
	public boolean execute()
	{
		if (CollectionUtils.isEmpty(urls))
		{
			return true;
		}
		else
		{
			final StopWatch stopWatch = new StopWatch("Page warming");
			stopWatch.start();
			for (final String url : urls)
			{
				final int statusCode = makeRequest(url);
				if (statusCode != HttpStatus.SC_OK)
				{
					if (throwExceptionOnFailure)
					{
						throw new RuntimeException("Failed to execute URL [" + url + "], returned code [" + statusCode + "]");
					}
				}
			}
			stopWatch.stop();
			LOG.info(stopWatch.shortSummary());
			return true;
		}
	}

	public int makeRequest(String url)
	{
		// check if the URL is fully qualified
		if (!StringUtils.contains(url, "http://"))
		{
			url = hostname + (url.startsWith("/") ? "" : "/") + url;
		}

		final HttpClient client = new HttpClient();
		final HttpMethod method = new GetMethod(url);
		method.addRequestHeader("User-Agent", USER_AGENT);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		try
		{
			final int statusCode = client.executeMethod(method);
			return statusCode;
		}
		catch (final Exception e)
		{
			if (throwExceptionOnFailure)
			{
				throw new RuntimeException("Failed to execute URL [" + url + "]", e);
			}
			else
			{
				LOG.error("Failed to execute URL [" + url + "]", e);
				return HttpStatus.SC_METHOD_FAILURE;
			}
		}
		finally
		{
			// Release the connection.
			method.releaseConnection();
		}
	}

	/**
	 * @param urls
	 *           the urls to set
	 */
	public void setUrls(final List<String> urls)
	{
		this.urls = urls;
	}

	/**
	 * @param throwExceptionOnFailure
	 *           the throwExceptionOnFailure to set
	 */
	public void setThrowExceptionOnFailure(final boolean throwExceptionOnFailure)
	{
		this.throwExceptionOnFailure = throwExceptionOnFailure;
	}

}
