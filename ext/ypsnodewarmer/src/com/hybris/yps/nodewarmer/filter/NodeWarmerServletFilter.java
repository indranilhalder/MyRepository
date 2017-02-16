/**
 * 
 */
package com.hybris.yps.nodewarmer.filter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.hybris.yps.nodewarmer.NodeStartupStateHolder;
import com.hybris.yps.nodewarmer.NodeStartupStateHolder.StartupState;


/**
 * {@link Filter} that returns 503 errors for requests while the server is starting. Once the server has been properly
 * warmed up a flag is set on {@link NodeStartupStateHolder} to resume normal passing of web traffic
 * 
 * This filter deliberately has no references to spring to ensure it will run while spring is still loading
 * 
 * @author brendan.dobbs
 * 
 */
public class NodeWarmerServletFilter implements Filter
{
	// a list of hosts that can make requests to the web app before the warming process has finish
	// usually this would just be localhost
	private final List<String> bypassableHosts = new LinkedList();

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException
	{

		final String remoteIP = request.getRemoteAddr();
		final String remoteHost = request.getRemoteHost();

		// if starting and the remote host is not a host that can bypass the filter
		if (NodeStartupStateHolder.getInstance().getStartupState().equals(StartupState.STARTING)
				&& CollectionUtils.isNotEmpty(bypassableHosts) && !bypassableHosts.contains(remoteIP)
				&& !bypassableHosts.contains(remoteHost))
		{
			final HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		else
		{
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void destroy()
	{
		//
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException
	{
		final String hosts = filterConfig.getInitParameter("bypassableHosts");
		final String[] hostArray = hosts.split(",");
		if (hostArray.length > 0)
		{
			for (final String host : hostArray)
			{
				bypassableHosts.add(StringUtils.strip(host));
			}
		}
	}

}
