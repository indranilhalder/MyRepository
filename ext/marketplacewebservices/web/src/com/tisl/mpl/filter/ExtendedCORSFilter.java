/**
 *
 */
package com.tisl.mpl.filter;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thetransactioncompany.cors.CORSConfiguration;
import com.thetransactioncompany.cors.CORSConfigurationException;
import com.thetransactioncompany.cors.CORSConfigurationLoader;
import com.thetransactioncompany.cors.CORSFilter;
import com.thetransactioncompany.cors.CORSOriginDeniedException;
import com.thetransactioncompany.cors.CORSRequestHandler;
import com.thetransactioncompany.cors.CORSRequestType;
import com.thetransactioncompany.cors.HTTPMethod;
import com.thetransactioncompany.cors.HeaderFieldName;
import com.thetransactioncompany.cors.InvalidCORSRequestException;
import com.thetransactioncompany.cors.UnsupportedHTTPHeaderException;
import com.thetransactioncompany.cors.UnsupportedHTTPMethodException;


/**
 * @author TCS
 *
 */
public class ExtendedCORSFilter extends CORSFilter
{

	private CORSRequestHandler handler;
	private CORSConfiguration config;


	@Override
	public CORSConfiguration getConfiguration()
	{
		return this.config;
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException
	{
		final CORSConfigurationLoader configLoader = new CORSConfigurationLoader(filterConfig);
		try
		{
			this.config = configLoader.load();
		}
		catch (final CORSConfigurationException e)
		{
			throw new ServletException(e.getMessage(), e);
		}

		this.handler = new CORSRequestHandler(this.config);
	}

	private void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		this.handler.tagRequest(request);

		final CORSRequestType type = CORSRequestType.detect(request);
		try
		{
			if (type.equals(CORSRequestType.ACTUAL))
			{
				this.handler.handleActualRequest(request, response);
				chain.doFilter(request, response);
			}
			else if (type.equals(CORSRequestType.PREFLIGHT))
			{ //Added
				this.handler.handlePreflightRequest(request, response);
				if (getConfigurationService().getConfiguration() != null
						&& getConfigurationService().getConfiguration().getString("allow.preflight.request").equalsIgnoreCase("true"))
				{
					if (request.getMethod().equalsIgnoreCase("OPTIONS"))
					{
						try
						{
							response.getWriter().print("OK");
							response.getWriter().flush();
						}
						catch (final IOException e)
						{
							e.printStackTrace();
						}
					}
					//End
				}

			}
			else if (this.config.allowGenericHttpRequests)
			{
				request.setAttribute("cors.isCorsRequest", Boolean.valueOf(false));
				chain.doFilter(request, response);
			}
			else
			{
				request.setAttribute("cors.isCorsRequest", Boolean.valueOf(false));
				printMessage(response, 403, "Generic HTTP requests not allowed");
			}
		}
		catch (final InvalidCORSRequestException e)
		{
			request.setAttribute("cors.isCorsRequest", Boolean.valueOf(false));
			printMessage(response, 400, e.getMessage());
		}
		catch (final CORSOriginDeniedException e)
		{
			final String msg = e.getMessage() + ": " + e.getRequestOrigin();
			printMessage(response, 403, msg);
		}
		catch (final UnsupportedHTTPMethodException e)
		{
			String msg = e.getMessage();

			final HTTPMethod method = e.getRequestedMethod();

			if (method != null)
			{
				msg = msg + ": " + method.toString();
			}
			printMessage(response, 405, msg);
		}
		catch (final UnsupportedHTTPHeaderException e)
		{
			String msg = e.getMessage();

			final HeaderFieldName header = e.getRequestHeader();

			if (header != null)
			{
				msg = msg + ": " + header.toString();
			}
			printMessage(response, 403, msg);
		}
	}

	private void printMessage(final HttpServletResponse response, final int sc, final String msg) throws IOException,
			ServletException
	{
		response.setStatus(sc);

		response.resetBuffer();

		response.setContentType("text/plain");

		final PrintWriter out = response.getWriter();

		out.println("Cross-Origin Resource Sharing (CORS) Filter: " + msg);
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse))
		{
			doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
		}
		else
		{
			throw new ServletException("Cannot filter non-HTTP requests/responses");
		}
	}

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}

}
