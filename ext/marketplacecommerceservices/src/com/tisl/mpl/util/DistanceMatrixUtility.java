/**
 *
 */
package com.tisl.mpl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

	public static JSONObject readJsonFromUrl(final String url) throws IOException, JSONException
	{
		final InputStream is = new URL(url).openStream();
		try
		{
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			final String jsonText = readAll(rd);
			LOG.debug(jsonText);
			final JSONObject json = new JSONObject(jsonText);
			LOG.debug(json);
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
		try
		{

			json = readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + beg + "&destinations="
					+ end + "&mode=driving&sensor=false");
			json.get("rows");
			JSONArray arr = null;
			arr = json.getJSONArray("rows");
			jsonArray = arr.getJSONObject(0).getJSONArray("elements");
			for (int i = 0; i < jsonArray.length(); i++)
			{
				final JSONObject jsonObj = jsonArray.getJSONObject(i);
				distance.add(Double.valueOf(jsonObj.getJSONObject("distance").getDouble("value")));
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
}
