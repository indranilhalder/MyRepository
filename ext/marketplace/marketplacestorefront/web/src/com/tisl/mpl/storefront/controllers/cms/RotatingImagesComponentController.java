/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.cms2lib.model.components.BannerComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;

import java.util.LinkedHashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.storefront.controllers.ControllerConstants;







/**
 * @author TCS
 *
 */
@Controller("RotatingImagesComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.RotatingImagesComponent)
public class RotatingImagesComponentController extends AbstractCMSComponentController<RotatingImagesComponentModel>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
	 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final RotatingImagesComponentModel component)
	{
		final LinkedHashSet<BannerComponentModel> desktopBanners = new LinkedHashSet<BannerComponentModel>();
		final LinkedHashSet<BannerComponentModel> moblileBanners = new LinkedHashSet<BannerComponentModel>();
		for (final BannerComponentModel banner : component.getBanners())
		{
			if (banner.getBannerView().getCode().equalsIgnoreCase("mobileView"))
			{
				moblileBanners.add(banner);
			}
			else
			{
				desktopBanners.add(banner);
			}
		}
		model.addAttribute("mobileView", moblileBanners);
		model.addAttribute("desktopView", desktopBanners);
		model.addAttribute("banners", component.getBanners());
	}
}
