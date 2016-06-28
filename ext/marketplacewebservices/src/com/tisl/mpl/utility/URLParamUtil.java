/**
 *
 */
package com.tisl.mpl.utility;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;



/**
 * @author TCS
 *
 */
public class URLParamUtil
{

	/**
	 * @Description : For Filtering Product Promotions
	 * @param url
	 * @return params
	 */
	public static Map<String, List<String>> getQueryParams(final String url)
	{
		try
		{
			final Map<String, List<String>> params = new HashMap<String, List<String>>();
			if (StringUtils.isNotEmpty(url))
			{
				final String[] urlParts = url.split("\\?");
				if (urlParts.length > 1)
				{
					final String query = urlParts[1];
					for (final String param : query.split("&"))
					{
						final String[] pair = param.split("=");
						final String key = URLDecoder.decode(pair[0], "UTF-8");
						String value = "";
						if (pair.length > 1)
						{
							value = URLDecoder.decode(pair[1], "UTF-8");
						}

						List<String> values = params.get(key);
						if (values == null)
						{
							values = new ArrayList<String>();
							params.put(key, values);
						}
						values.add(value);
					}
				}
			}
			return params;
		}
		catch (final UnsupportedEncodingException ex)
		{
			throw new AssertionError(ex);
		}
	}

	/**
	 * @Description : For Filtering Product Promotions
	 * @param paramValue
	 * @return paramParsed
	 */
	public static String getQueryParamParsed(final String paramValue)
	{
		try
		{
			String param = null;
			String paramParse[] = null;
			String paramParsed = null;
			if (StringUtils.isNotEmpty(paramValue))
			{
				param = URLDecoder.decode(paramValue, "UTF-8");
				paramParse = param.split(":");
				paramParsed = paramParse[0];
			}
			return paramParsed;
		}
		catch (final UnsupportedEncodingException ex)
		{
			throw new AssertionError(ex);
		}
	}

	/**
	 *
	 * @param value
	 *           to be sanitized
	 * @return sanitized content
	 */
	public static String filter(final String value)
	{
		if (value == null)
		{
			return null;
		}
		String sanitized = value;
		sanitized = sanitized.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		sanitized = sanitized.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		sanitized = sanitized.replaceAll("'", "&#39;");
		sanitized = sanitized.replaceAll("eval\\((.*)\\)", "");
		sanitized = sanitized.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		return sanitized;
	}



}
