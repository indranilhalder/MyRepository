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
package com.tisl.mpl.storefront.web.view;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.site.BaseSiteService;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.tisl.mpl.storefront.constants.ModelAttributetConstants;


/**
 * A view resolver that detects the device a request is coming from and directs it to the appropriate view. This view
 * resolver extends Spring's org.springframework.web.servlet.view.InternalResourceViewResolver.
 */
public class UiExperienceViewResolver extends InternalResourceViewResolver
{
	private static final Logger LOG = Logger.getLogger(UiExperienceViewResolver.class);

	public static final String UNDERSCORE = "_";

	private UiExperienceService uiExperienceService;
	public Map<UiExperienceLevel, String> uiExperienceViewPrefix;
	private String unknownUiExperiencePrefix;
	private String addOnPrefix;


	@Autowired
	private BaseSiteService baseSiteService;

	@Resource(name = "addonReplacePrefixMap")
	private Map<String, Map<String, String>> addonReplacePrefixMap;


	protected UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	public Map<UiExperienceLevel, String> getUiExperienceViewPrefix()
	{
		return uiExperienceViewPrefix;
	}

	@Required
	public void setUiExperienceViewPrefix(final Map<UiExperienceLevel, String> uiExperienceViewPrefix)
	{
		this.uiExperienceViewPrefix = uiExperienceViewPrefix;
	}

	protected String getUnknownUiExperiencePrefix()
	{
		return unknownUiExperiencePrefix;
	}

	@Required
	public void setUnknownUiExperiencePrefix(final String unknownUiExperiencePrefix)
	{
		this.unknownUiExperiencePrefix = unknownUiExperiencePrefix;
	}

	protected String getAddOnPrefix()
	{
		return addOnPrefix;
	}

	@Required
	public void setAddOnPrefix(final String addOnPrefix)
	{
		this.addOnPrefix = addOnPrefix;
	}

	@Override
	protected Object getCacheKey(final String viewName, final Locale locale)
	{
		// Incorporate the UiExperienceLevel into the view cache
		Object obj;
		if (null != baseSiteService && null != baseSiteService.getCurrentBaseSite()
				&& null != baseSiteService.getCurrentBaseSite().getUid())
		{
			obj = super.getCacheKey(baseSiteService.getCurrentBaseSite().getUid() + UNDERSCORE + viewName, locale) + UNDERSCORE
					+ getUiExperienceService().getUiExperienceLevel().getCode();
		}
		else
		{
			obj = super.getCacheKey(viewName, locale) + UNDERSCORE + getUiExperienceService().getUiExperienceLevel().getCode();
		}
		return obj;
	}

	@Override
	protected AbstractUrlBasedView buildView(final String viewName) throws Exception
	{
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		final String expandedViewName = getViewName(uiExperienceLevel, viewName);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Expanded View Name [" + viewName + "] into [" + expandedViewName + "]");
		}

		final InternalResourceView view = (InternalResourceView) super.buildView(expandedViewName);
		view.setAlwaysInclude(false);
		return view;
	}

	public String getViewName(final UiExperienceLevel uiExperienceLevel, String viewName)
	{
		final String prefix = getUiExperienceViewPrefix().get(uiExperienceLevel);

		if (prefix != null)
		{
			//Logic added to change prefix for supporting addon pages
			final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
			final String site = currentBaseSite.getUid();

			final Map<String, String> siteAddonReplacePrefixMap = addonReplacePrefixMap.get(site);
			if (null != siteAddonReplacePrefixMap)
			{
				if (StringUtils.isNotBlank(siteAddonReplacePrefixMap.get(viewName)))
				{
					viewName = siteAddonReplacePrefixMap.get(viewName);
				}
			}


			if (viewName.startsWith(AbstractPageController.PAGE_ROOT + ModelAttributetConstants.ADDON))
			{
				viewName = viewName.replace(AbstractPageController.PAGE_ROOT + ModelAttributetConstants.ADDON,
						ModelAttributetConstants.ADDON); // ...pages/addon:/<extension-name>/.../<component-view>....->....addon:/<extension-name>/.../<component-view>
			}

			if (viewName.startsWith(ModelAttributetConstants.ADDON))
			{
				viewName = viewName.replace(ModelAttributetConstants.ADDON, ""); // ................................addon:/<extension-name>/cms/<component-view>....->..../<extension-name>/cms/<component-view>
				viewName = viewName.substring(1, viewName.length()); // ....................../<extension-name>/cms/<component-view>..........->....<extension-name>/cms/<component-view>
				final String extensionName = viewName.substring(0, viewName.indexOf('/')); // <extension-name>/cms/<component-view>...........->....<extension-name>
				viewName = viewName.substring(viewName.indexOf('/'), viewName.length()); // ..<extension-name>/cms/<component-view>...........->..../cms/<component-view>

				return getAddOnPrefix() + "/" + extensionName + "/" + prefix + viewName; // ..<addon-prefix>/<extension-name>/<ui-prefix>/cms/<component-view>
			}


			return prefix + viewName;
		}
		return getUnknownUiExperiencePrefix() + viewName;
	}
}
