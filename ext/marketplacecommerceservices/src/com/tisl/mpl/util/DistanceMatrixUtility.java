/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.granule.json.JSONArray;
import com.granule.json.JSONException;
import com.granule.json.JSONObject;


/**
 * @author TCS
 *
 */

public class DistanceMatrixUtility
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DistanceMatrixUtility.class);

	private static String readAll(final Reader rd) throws IOException
	{
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1)
		{
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(final String url, final Proxy proxy) throws IOException, JSONException
	{
		final InputStream is = new URL(url).openConnection(proxy).getInputStream();
		try
		{
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			final String jsonText = readAll(rd);
			final JSONObject json = new JSONObject(jsonText);
			return json;
		}
		finally
		{
			is.close();
		}
	}





	public List<Double> calcDistance(final StringBuffer beg, final StringBuffer end)
	{
		JSONObject json = null;
		JSONArray jsonArray = null;
		final List<Double> distance = new ArrayList<>();
		double disKM = 0;
		SocketAddress addr = null;
		Proxy proxy = null;
		final int portproxy = Integer.parseInt(getConfigurationService().getConfiguration().getString("proxy.port"));
		addr = new InetSocketAddress(getConfigurationService().getConfiguration().getString("proxy.address"), portproxy);
		proxy = new Proxy(Proxy.Type.HTTP, addr);
		try
		{

			json = readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + beg + "&destinations="
					+ end + "&mode=driving&sensor=false", proxy);
			json.get("rows");
			JSONArray arr = null;
			arr = json.getJSONArray("rows");
			jsonArray = arr.getJSONObject(0).getJSONArray("elements");
			for (int i = 0; i < jsonArray.length(); i++)
			{
				final JSONObject jsonObj = jsonArray.getJSONObject(i);
				disKM = (jsonObj.getJSONObject("distance").getDouble("value")) / 1000;
				disKM = (double) Math.round(disKM * 100) / 100;
				distance.add(Double.valueOf(disKM));
			}

		}
		catch (final JSONException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return distance;
	}


	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}
}
