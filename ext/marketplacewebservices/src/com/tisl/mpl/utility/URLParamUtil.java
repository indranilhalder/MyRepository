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



}
