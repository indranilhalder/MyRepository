/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * Controller for web robots instructions
 */
@Controller
@Scope("tenant")
public class RobotsController extends AbstractController
{

	private static final String SITE_URL = "siteUrl";
	private static final String MPL = "mpl";
	private static final String LUX = "lux";

	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	// Number of seconds in one day
	private static final String ONE_DAY = String.valueOf(60 * 60 * 24);

	@RequestMapping(value = "/robots.txt", method = RequestMethod.GET)
	public String getRobots(final HttpServletResponse response, final Model model)
	{
		// Add cache control header to cache response for a day
		response.setHeader("Cache-Control", "public, max-age=" + ONE_DAY);
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();
		if (currentSite.getUid() == MPL || currentSite.getUid().equals(MPL))
		{
			model.addAttribute(SITE_URL, MPL);
		}
		else
		{
			model.addAttribute(SITE_URL, LUX);
		}

		return ControllerConstants.Views.Pages.Misc.MiscRobotsPage;
	}


	//Changes start  for TPR-5812
	@RequestMapping(value = "/manifests.json", method = RequestMethod.GET)
	public String getManifest(final Model model)
	{
		final String gcm_sender_id = configurationService.getConfiguration().getString("izooto.manifest.gcm_sender_id");
		model.addAttribute("gcm_sender_id", gcm_sender_id);
		return ControllerConstants.Views.Pages.Misc.manifest;
	}

	@RequestMapping(value = "/service-worker.js", method = RequestMethod.GET)
	public String getserviceWorker(final Model model)
	{
		final String izootooJsUrl = configurationService.getConfiguration().getString("izooto.serviceWorker.url");
		model.addAttribute("izootooJsUrl", izootooJsUrl);
		return ControllerConstants.Views.Pages.Misc.serviceWorker;
	}
	//changes end for TPR-5812
}
