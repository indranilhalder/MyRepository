/**
 *
 */
package com.tisl.mpl.storefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @author TCS
 *
 */
@Controller
@Scope("tenant")
public class TealiumController extends AbstractController
{

	private static final Logger LOG = Logger.getLogger(TealiumController.class);

	private final String UTAG_SCRIPT_PROD = "</script><script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/prod/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script>";
	private final String UTAG_SCRIPT_DEV = "</script><script type='text/javascript'>(function(a,b,c,d){a='//tags.tiqcdn.com/utag/tataunistore/main/dev/utag.js';b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);})();</script>";
	private final String IA_COMPANY = "IA_company";
	private final String TEALIUM_ERROR = "Exception while populating tealium data ::::";
	private final String PAGETYPE = "page_type";
	private final String UTAG_DATA = "<script type='text/javascript'> var utag_data =";
	private final String TEALIUM_SCRIPT = "<TealiumScript>";
	private final String PAGE_NAME = "page_name";
	private final String SITE_SECTION = "site_section";

	@RequestMapping(value = "/getTealiumDataHome", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataHomepage() throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000); // SONAR FIX
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			utag.put(PAGETYPE, "home");
			utag.put(PAGE_NAME, "Homepage");
			utag.put(SITE_SECTION, "home");
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());

		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	@RequestMapping(value = "/getTealiumDataProduct", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataProductPage() throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000);
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			utag.put(PAGETYPE, "product");
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(TEALIUM_SCRIPT);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());

		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	@RequestMapping(value = "/getTealiumDataGeneric", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataGenericPages(@RequestParam("pageName") final String pageName) throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000); // SONAR FIX
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			utag.put(PAGETYPE, "generic");
			utag.put(PAGE_NAME, pageName);
			utag.put(SITE_SECTION, pageName);
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());

		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	@RequestMapping(value = "/getTealiumDataCategory", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataCategory() throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000); // SONAR FIX
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			utag.put(PAGETYPE, "category");
			utag.put("page_section_name", "");
			utag.put("page_subcategory_name", "");
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(TEALIUM_SCRIPT);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());

		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	@RequestMapping(value = "/getTealiumDataSearch", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataSearch() throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000); // SONAR FIX
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			utag.put(PAGETYPE, "search");
			utag.put(SITE_SECTION, "Search Results");
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(TEALIUM_SCRIPT);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());

		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	@RequestMapping(value = "/getTealiumDataCart", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataCart() throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000); // SONAR FIX
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			//utag.put(PAGETYPE, "checkout");
			utag.put(PAGETYPE, "cart"); //PRDI-95 Fix
			utag.put(SITE_SECTION, "My Bag");
			utag.put(PAGE_NAME, "Cart Page");
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(TEALIUM_SCRIPT);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());
		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	@RequestMapping(value = "/getTealiumDataCheckout", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataCart(@RequestParam("checkoutPageName") final String checkoutPageName) throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000); // SONAR FIX
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			utag.put(PAGETYPE, "checkout");
			utag.put(SITE_SECTION, "Checkout");
			utag.put(PAGE_NAME, "Multi Checkout Summary Page:" + checkoutPageName);
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(TEALIUM_SCRIPT);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());
		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	//order confirmation script
	@RequestMapping(value = "/getTealiumDataOrder", method = RequestMethod.GET)
	@ResponseBody
	public String getTealiumDataOrder() throws Exception
	{
		final StringBuilder tealiumData = new StringBuilder(1000); // SONAR FIX
		try
		{
			final JSONObject utag = populateCommonTealiumData();
			//utag.put(PAGETYPE, "checkout");
			utag.put(PAGETYPE, "orderconfirmation"); //PRDI-95 Fix
			utag.put(SITE_SECTION, "orderconfirmation");
			utag.put(PAGE_NAME, "Order Confirmation");
			final String utagData = utag.toJSONString();
			tealiumData.append(UTAG_DATA);
			tealiumData.append(utagData);
			tealiumData.append(TEALIUM_SCRIPT);
			tealiumData.append(getTealiumScript((String) utag.get(IA_COMPANY)));
		}
		catch (final Exception ex)
		{
			LOG.error(TEALIUM_ERROR + ex.getMessage());
		}
		LOG.debug(tealiumData.toString());
		return tealiumData.toString();
	}

	private static String getVisitorIpAddress(final HttpServletRequest request)
	{
		final String[] HEADERS_TO_TRY =
		{ "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
		for (final String header : HEADERS_TO_TRY)
		{
			final String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip))
			{
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	private static HttpServletRequest getRequest()
	{
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	private JSONObject populateCommonTealiumData()
	{

		final HttpServletRequest request = getRequest();
		final String visitorIP = getVisitorIpAddress(request);
		final HttpSession session = request.getSession();
		String sessionId = session.getId();
		String userType = null;
		String userId = null;
		final String domainName = request.getServerName();
		final Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (final Cookie cookie : cookies)
			{
				if (cookie.getName().equals("mpl-user"))
				{
					userId = cookie.getValue();
				}
				if (cookie.getName().equals("mpl-userType"))
				{
					userType = cookie.getValue();
				}
			}
		}
		if (sessionId.contains("."))
		{
			final String[] parts = sessionId.split("\\.");
			sessionId = parts[0];

		}

		final JSONObject utag = new JSONObject();
		utag.put("site_region", "en");
		utag.put("user_type", userType);
		utag.put("user_id", userId);
		utag.put("session_id", sessionId);
		utag.put("visitor_ip", visitorIP);
		utag.put("site_currency", "INR");
		utag.put(IA_COMPANY, domainName);

		return utag;
	}

	/**
	 *
	 */
	private String getTealiumScript(final String domainName)
	{
		String utagScript = UTAG_SCRIPT_DEV;

		if (domainName.equalsIgnoreCase("www.tatacliq.com"))
		{
			utagScript = UTAG_SCRIPT_PROD;
		}

		return utagScript;
	}

}
