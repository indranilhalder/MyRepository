/**
 *
 */
package com.tisl.mpl.storefront.controllers.misc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author TCS
 *
 */
@Controller
public class ServiceWorkerController
{
	// BuildMyString.com generated code. Please enjoy your string responsibly.



	@RequestMapping(value = "cliq-service-worker.js", method = RequestMethod.GET)
	public void worker(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		final StringBuilder sb = new StringBuilder();

		sb.append("self.addEventListener('activate', function(e) {");
		sb.append("  console.log('[ServiceWorker] Activate');");
		sb.append("});");
		sb.append("var cacheName = 'cliq-pwa-cache-v1';");
		sb.append("var filesToCache = ['/'];");
		sb.append("self.addEventListener('install',function(e){");
		sb.append("console.log(\"ServiceWorked Install\");");
		sb.append("e.waitUntil(caches.open(cacheName).then(function(cache){");
		sb.append("console.log('[ServiceWorker] Caching app shell');");
		sb.append("return cache.addAll(filesToCache);}));");
		sb.append("});");
		sb.append("self.addEventListener('fetch', function(event) {");
		sb.append("  event.respondWith(");
		sb.append("    caches.match(event.request)");
		sb.append("      .then(function(response) {");
		sb.append("        if (response) {");
		sb.append("          return response;");
		sb.append("        }");
		sb.append("        return fetch(event.request);");
		sb.append("      }");
		sb.append("    )");
		sb.append("  );");
		sb.append("});");


		response.setContentType("application/javascript");
		response.getWriter().write(sb.toString());
	}

	@RequestMapping(value = "manifest.json", method = RequestMethod.GET)
	public void manifest(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		final StringBuilder sb = new StringBuilder();

		sb.append("{");
		sb.append("  \"short_name\": \"Tata Cliq\",");
		sb.append("  \"name\": \"Tata Cliq\",");
		sb.append("  \"display\": \"standalone\",");
		sb.append("  \"background_color\": \"#a9133d\",");
		sb.append("  \"theme_color\": \"#a9133d\",");
		sb.append("  \"icons\": [");
		sb.append("    {");
		sb.append("      \"src\": \"//assets.tatacliq.com/medias/sys_master/images/9906406817822.png\",");
		sb.append("      \"type\": \"image/png\",");
		sb.append("      \"sizes\": \"48x48\"");
		sb.append("    }");
		sb.append("  ],");
		sb.append("  \"start_url\": \"/\"");
		sb.append("}");



		response.setContentType("application/javascript");
		response.getWriter().write(sb.toString());
	}
}