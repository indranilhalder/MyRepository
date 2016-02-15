package com.tisl.mpl.storefront.controllers.helpers;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;



public class FBGraph
{
	private static final Logger LOG = Logger.getLogger(FBGraph.class);
	private final String accessToken;
	private static final String n = "\n";

	public FBGraph(final String accessToken)
	{
		this.accessToken = accessToken;
	}

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(ModelAttributetConstants.CONFIGURATION_SERVICE, ConfigurationService.class);
	}

	public String getFBGraph()
	{
		String graph = null;
		try
		{

			final String g = MessageConstants.FACEBOOK_URL_PREFIX + accessToken;
			final URL u = new URL(g);
			final SocketAddress addr = new InetSocketAddress(getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_PROXY), Integer.parseInt(getConfigurationService().getConfiguration().getString(
					MessageConstants.SOCIAL_LOGIN_PROXY_PORT)));
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			final HttpURLConnection c = (HttpURLConnection) u.openConnection(proxy);
			LOG.info(MessageConstants.RESPONSE_CODE_FROM_FB + c.getResponseCode());
			final BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String inputLine;
			final StringBuffer b = new StringBuffer();
			while ((inputLine = in.readLine()) != null)
			{
				b.append(inputLine + n);
			}
			in.close();
			graph = b.toString();

		}
		catch (final Exception e)
		{
			LOG.error(e);
			LOG.debug(MessageConstants.ERROR_GETTING_FB_GRAPH);
			throw new EtailNonBusinessExceptions(e);
		}
		return graph;
	}

	public Map<String, String> getGraphData(final String fbGraph)
	{
		final Map<String, String> fbProfile = new HashMap<String, String>();
		try
		{
			final JSONObject json = new JSONObject(fbGraph);
			fbProfile.put(ModelAttributetConstants.ID, json.getString(ModelAttributetConstants.ID));
			fbProfile.put(ModelAttributetConstants.FIRST_NAME, json.getString(ModelAttributetConstants.FIRST_NAME));
			if (json.has(ModelAttributetConstants.EMAIL))
			{
				fbProfile.put(ModelAttributetConstants.EMAIL, json.getString(ModelAttributetConstants.EMAIL));
			}
			if (json.has(ModelAttributetConstants.GENDER))
			{
				fbProfile.put(ModelAttributetConstants.GENDER, json.getString(ModelAttributetConstants.GENDER));
			}
		}
		catch (final JSONException e)
		{
			LOG.error(e);
			LOG.debug(MessageConstants.ERROR_GETTING_FB_GRAPH);
			throw new EtailNonBusinessExceptions(e);
		}
		return fbProfile;
	}
}
