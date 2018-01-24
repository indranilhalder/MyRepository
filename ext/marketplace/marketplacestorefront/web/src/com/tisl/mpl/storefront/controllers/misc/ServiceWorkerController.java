/**
 *
 */
package com.tisl.mpl.storefront.controllers.misc;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.facade.cms.MplCmsFacade;
import com.tisl.mpl.facades.cms.data.AmpMenifestData;
import com.tisl.mpl.facades.cms.data.AmpServiceWorkerData;


/**
 * @author TCS
 *
 */
@Controller
public class ServiceWorkerController
{
	@Resource
	private ConfigurationService configurationService;

	@Resource(name = "mplCmsFacade")
	private MplCmsFacade mplCmsFacade;

	/**
	 * Data needs to be fetched from persistence layer
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "cliq-service-worker.js", method = RequestMethod.GET)
	public void worker(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		//		final StringBuilder sb = new StringBuilder();
		//
		//		sb.append("self.addEventListener('activate', function(e) {");
		//		sb.append("  console.log('[ServiceWorker] Activate');");
		//		sb.append("});");
		//		sb.append("var cacheName = 'cliq-pwa-cache-v1';");
		//		sb.append("var filesToCache = ['/'];");
		//		sb.append("self.addEventListener('install',function(e){");
		//		sb.append("console.log(\"ServiceWorked Install\");");
		//		sb.append("e.waitUntil(caches.open(cacheName).then(function(cache){");
		//		sb.append("console.log('[ServiceWorker] Caching app shell');");
		//		sb.append("return cache.addAll(filesToCache);}));");
		//		sb.append("});");
		//		sb.append("self.addEventListener('fetch', function(event) {");
		//		sb.append("  event.respondWith(");
		//		sb.append("    caches.match(event.request)");
		//		sb.append("      .then(function(response) {");
		//		sb.append("        if (response) {");
		//		sb.append("          return response;");
		//		sb.append("        }");
		//		sb.append("        return fetch(event.request);");
		//		sb.append("      }");
		//		sb.append("    )");
		//		sb.append("  );");
		//		sb.append("});");


		final AmpServiceWorkerData ampServiceWorkerData = mplCmsFacade.getAmpServiceWorkerData();
		String serviceWorkerJS = null;
		if (null != ampServiceWorkerData && null != ampServiceWorkerData.getCacheName()
				&& null != ampServiceWorkerData.getCacheVersion() && null != ampServiceWorkerData.getFilesToCache())
		{
			final String cacheName = ampServiceWorkerData.getCacheName();
			final String cacheVersion = ampServiceWorkerData.getCacheVersion();
			final String filesToCache = "'" + ampServiceWorkerData.getFilesToCache() + "'";
			final String CACHE_PLUS_VERSION = "'" + cacheName + "-" + cacheVersion + "'";
			serviceWorkerJS = ampServiceWorkerData.getServiceworkerJs();
			serviceWorkerJS = serviceWorkerJS.replaceAll("CACHE_PLUS_VERSION", CACHE_PLUS_VERSION);
			serviceWorkerJS = serviceWorkerJS.replaceAll("FILES_TO_CACHE", filesToCache);
		}


		response.setContentType("application/javascript");
		//		response.getWriter().write(sb.toString());
		response.getWriter().write(serviceWorkerJS);
	}

	/**
	 * Data needs to be fetched from persistence layer
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "manifest.json", method = RequestMethod.GET)
	public void manifest(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		//		final StringBuilder manifest = new StringBuilder();
		//
		//		manifest.append("{");
		//		manifest.append("  \"name\": \"Tata Cliq\",");
		//		manifest.append("  \"short_name\": \"Tata Cliq\",");
		//		manifest.append("  \"start_url\": \"/pwamp\",");
		//		manifest.append("  \"display\": \"standalone\",");
		//		manifest.append("  \"background_color\": \"#a9133d\",");
		//		manifest.append("  \"theme_color\": \"#a9133d\",");
		//		manifest.append("  \"orientation\": \"portrait\",");
		//		manifest.append("  \"description\": \"Tata CLiQ: Online shopping in India at the most trusted destination.\",");
		//		manifest.append("  \"icons\": [{");
		//		manifest.append("    \"src\": \"/_ui/responsive/common/images/manifest/logo_48.png\",");
		//		manifest.append("    \"sizes\": \"48x48\",");
		//		manifest.append("    \"type\": \"image/png\"");
		//		manifest.append("  }, {");
		//		manifest.append("    \"src\": \"/_ui/responsive/common/images/manifest/logo_72.png\",");
		//		manifest.append("    \"sizes\": \"72x72\",");
		//		manifest.append("    \"type\": \"image/png\"");
		//		manifest.append("  }, {");
		//		manifest.append("    \"src\": \"/_ui/responsive/common/images/manifest/logo_96.png\",");
		//		manifest.append("    \"sizes\": \"96x96\",");
		//		manifest.append("    \"type\": \"image/png\"");
		//		manifest.append("  }, {");
		//		manifest.append("    \"src\": \"/_ui/responsive/common/images/manifest/logo_144.png\",");
		//		manifest.append("    \"sizes\": \"144x144\",");
		//		manifest.append("    \"type\": \"image/png\"");
		//		manifest.append("  }, {");
		//		manifest.append("    \"src\": \"/_ui/responsive/common/images/manifest/logo_168.png\",");
		//		manifest.append("    \"sizes\": \"168x168\",");
		//		manifest.append("    \"type\": \"image/png\"");
		//		manifest.append("  }, {");
		//		manifest.append("    \"src\": \"/_ui/responsive/common/images/manifest/logo_192.png\",");
		//		manifest.append("    \"sizes\": \"192x192\",");
		//		manifest.append("    \"type\": \"image/png\"");
		//		manifest.append("  }]");
		//		manifest.append("}");

		final AmpMenifestData ampMenifestData = mplCmsFacade.getAmpMenifestData();
		String menifestJson = null;
		if (null != ampMenifestData && null != ampMenifestData.getMenifestJson())
		{
			menifestJson = ampMenifestData.getMenifestJson();
		}

		response.setContentType("application/javascript");
		//response.getWriter().write(manifest.toString());
		response.getWriter().write(menifestJson);
	}

	@RequestMapping(value = "/iframeUtag_homepage.html", method = RequestMethod.GET)
	public String tealiumHtml(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		// code here
		final String ampAnalyticsHostTealium = configurationService.getConfiguration().getString("amp.analytics.source.origin",
				"*.tatacliq.com");
		response.addHeader("Access-Control-Allow-Origin", ampAnalyticsHostTealium);
		response.addHeader("AMP-Access-Control-Allow-Source-Origin", ampAnalyticsHostTealium);
		response.addHeader("Access-Control-Expose-Headers", ampAnalyticsHostTealium);
		return "pages/pwamp/iframeUtag";
	}
}
