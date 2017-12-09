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
package com.tisl.mpl.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorfacades.device.DeviceDetectionFacade;
import de.hybris.platform.acceleratorfacades.device.data.DeviceData;
import de.hybris.platform.acceleratorservices.addonsupport.RequiredAddOnsNameProvider;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.enums.SiteTheme;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import com.tisl.mpl.facade.latestoffers.impl.OffersComponentCacheKey;
import com.tisl.mpl.facade.latestoffers.impl.OffersComponentCacheValueLoader;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.interceptors.BeforeViewHandler;
import com.tisl.mpl.storefront.util.CSRFTokenManager;
import com.tisl.mpl.storefront.web.view.UiExperienceViewResolver;


/**
 * Interceptor to setup the paths to the UI resource paths in the model before passing it to the view. Sets up the path
 * to the web accessible UI resources for the following: * The current site * The current theme * The common resources
 * All of these paths are qualified by the current UiExperienceLevel
 */
public class UiThemeResourceBeforeViewHandler implements BeforeViewHandler
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UiThemeResourceBeforeViewHandler.class);

	protected static final String COMMON = "common";
	protected static final String SHARED = "shared";
	protected static final String RESOURCE_TYPE_JAVASCRIPT = "javascript";
	protected static final String RESOURCE_TYPE_CSS = "css";

	@Resource(name = "cmsSiteService")
	private CMSSiteService cmsSiteService;

	@Resource(name = "uiExperienceService")
	private UiExperienceService uiExperienceService;

	@Resource(name = "deviceDetectionFacade")
	private DeviceDetectionFacade deviceDetectionFacade;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "reqAddOnsNameProvider")
	private RequiredAddOnsNameProvider requiredAddOnsNameProvider;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "viewResolver")
	private UiExperienceViewResolver uiExperienceViewResolver;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private String defaultThemeName;

	@Resource(name = "offerCompCacheRegion")
	private CacheRegion offerCompCacheRegion;

	@Resource(name = "offersCompCacheValueLoader")
	private OffersComponentCacheValueLoader offersCompCacheValueLoader;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "userService")
	private UserService userService;

	@SuppressWarnings("boxing")
	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		final CMSSiteModel currentSite = cmsSiteService.getCurrentSite();

		final String siteName = currentSite.getUid();
		final String themeName = getThemeNameForSite(currentSite);
		final String uiExperienceCode = uiExperienceService.getUiExperienceLevel().getCode();
		final String uiExperienceCodeLower = uiExperienceViewResolver.getUiExperienceViewPrefix().isEmpty() ? uiExperienceCode
				.toLowerCase() : StringUtils.remove(
				uiExperienceViewResolver.getUiExperienceViewPrefix().get(uiExperienceService.getUiExperienceLevel()), "/");
		final Object urlEncodingAttributes = request.getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
		final String contextPath = StringUtils.remove(request.getContextPath(),
				(urlEncodingAttributes != null) ? urlEncodingAttributes.toString() : "");

		LOG.debug("Actual context path static ====> " + contextPath);
		final String staticResourceHost = configurationService.getConfiguration().getString("marketplace.static.resource.host");
		String siteRootUrl, addOnContextPath;
		if (StringUtils.isNotEmpty(staticResourceHost))
		{
			siteRootUrl = "//" + staticResourceHost + contextPath + "/_ui/" + uiExperienceCodeLower;
			addOnContextPath = "//" + staticResourceHost + contextPath;
			LOG.debug("Static resource host ====> " + addOnContextPath);
		}
		else
		{
			siteRootUrl = contextPath + "/_ui/" + uiExperienceCodeLower;
			addOnContextPath = contextPath;
		}
		LOG.debug("siteRootUrl ===> " + siteRootUrl);

		final String sharedResourcePath = contextPath + "/_ui/" + SHARED;
		final String siteResourcePath = siteRootUrl + "/site-" + siteName;
		final String themeResourcePath = siteRootUrl + "/theme-" + themeName;
		final String commonResourcePath = siteRootUrl + "/" + COMMON;
		final String encodedContextPath = request.getContextPath();
		final LanguageModel currentLanguage = commerceCommonI18NService.getCurrentLanguage();
		//FOr Gigya
		final String gigyaAPIKey = configurationService.getConfiguration().getString("gigya.apikey");
		final String gigyaSocialLoginURL = configurationService.getConfiguration().getString("gigya.sociallogin.url");
		final String isGigyaEnabled = configurationService.getConfiguration().getString(MessageConstants.USE_GIGYA);

		//for New Social Login Start
		final String useSocialLoginNative = configurationService.getConfiguration().getString("mpl.use.NativeSocialLogin", "N");
		final String facebookAppId = configurationService.getConfiguration().getString("mpl.fb.appid", "NA");
		final String googleAppId = configurationService.getConfiguration().getString("mpl.google.appid", "NA");
		final String luxfacebookAppId = configurationService.getConfiguration().getString("lux.fb.appid", "NA");
		final String luxgoogleAppId = configurationService.getConfiguration().getString("lux.google.appid", "NA");

		//FOR Feedback survey
		final String feedbackSurveyUrl = configurationService.getConfiguration().getString(MessageConstants.FEEDBACK_SURVEY_URL);
		//for izooto |TPR-5812
		final String isIzootoEnabled = configurationService.getConfiguration().getString("izooto.use");
		modelAndView.addObject(ModelAttributetConstants.IS_IZOOTO_ENABLED, isIzootoEnabled);
		modelAndView.addObject("contextPath", contextPath);
		modelAndView.addObject("sharedResourcePath", sharedResourcePath);
		modelAndView.addObject("siteResourcePath", siteResourcePath);
		modelAndView.addObject("themeResourcePath", themeResourcePath);
		modelAndView.addObject("commonResourcePath", commonResourcePath);
		modelAndView.addObject("encodedContextPath", encodedContextPath);
		modelAndView.addObject("siteRootUrl", siteRootUrl);
		modelAndView.addObject("language", (currentLanguage != null ? currentLanguage.getIsocode() : "en"));
		modelAndView.addObject("CSRFToken", CSRFTokenManager.getTokenForSession(request.getSession()));

		modelAndView.addObject("uiExperienceLevel", uiExperienceCode);

		final String detectedUiExperienceCode = uiExperienceService.getDetectedUiExperienceLevel().getCode();
		modelAndView.addObject("detectedUiExperienceCode", detectedUiExperienceCode);

		final UiExperienceLevel overrideUiExperienceLevel = uiExperienceService.getOverrideUiExperienceLevel();
		if (overrideUiExperienceLevel == null)
		{
			modelAndView.addObject("uiExperienceOverride", Boolean.FALSE);
		}
		else
		{
			modelAndView.addObject("uiExperienceOverride", Boolean.TRUE);
			modelAndView.addObject("overrideUiExperienceCode", overrideUiExperienceLevel.getCode());
		}

		//Header Title
		final OffersComponentCacheKey key = new OffersComponentCacheKey();
		final String title = (String) offerCompCacheRegion.getWithLoader(key, offersCompCacheValueLoader);
		modelAndView.addObject("headerConciergeTitle", title);

		modelAndView.addObject("isMinificationEnabled",
				Boolean.valueOf(siteConfigService.getBoolean("storefront.minification.enabled", false)));


		final DeviceData currentDetectedDevice = deviceDetectionFacade.getCurrentDetectedDevice();
		modelAndView.addObject("detectedDevice", currentDetectedDevice);

		final List<String> dependantAddOns = requiredAddOnsNameProvider.getAddOns(request.getSession().getServletContext()
				.getServletContextName());

		modelAndView.addObject("addOnCommonCssPaths",
				getAddOnCommonCSSPaths(addOnContextPath, uiExperienceCodeLower, dependantAddOns));
		modelAndView.addObject("addOnThemeCssPaths",
				getAddOnThemeCSSPaths(addOnContextPath, themeName, uiExperienceCodeLower, dependantAddOns));
		modelAndView.addObject("addOnJavaScriptPaths",
				getAddOnJSPaths(addOnContextPath, siteName, uiExperienceCodeLower, dependantAddOns));

		//for New Social Login Start
		modelAndView.addObject(ModelAttributetConstants.USE_NATIVE_API_SOCIAL, useSocialLoginNative);
		modelAndView.addObject(ModelAttributetConstants.FB_API_KEY, facebookAppId);
		modelAndView.addObject(ModelAttributetConstants.GOOGLE_API_KEY, googleAppId);
		modelAndView.addObject(ModelAttributetConstants.FB_API_KEY_LUXURY, luxfacebookAppId);
		modelAndView.addObject(ModelAttributetConstants.GOOGLE_API_KEY_LUXURY, luxgoogleAppId);
		//for New Social Login Stop

		modelAndView.addObject(ModelAttributetConstants.GIGYA_API_KEY, gigyaAPIKey);
		modelAndView.addObject(ModelAttributetConstants.GIGYA_SOCIAL_LOGIN_URL, gigyaSocialLoginURL);
		modelAndView.addObject(ModelAttributetConstants.IS_GIGYA_ENABLED, isGigyaEnabled);
		//HTML minification toggle
		modelAndView.addObject("minificationHTML", configurationService.getConfiguration().getString("minification.html"));
		modelAndView.addObject(ModelAttributetConstants.FEED_BACK_SURVEY_URL, feedbackSurveyUrl);
		//Logic added for Name in header
		if (!userFacade.isAnonymousUser())
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
			final String firstName = currentCustomer.getFirstName();
			modelAndView.addObject(ModelAttributetConstants.LUXURY_USER_FIRST_NAME, firstName);

		}
		if (StringUtils.isNotEmpty(currentSite.getBuildNumber()))
		{
			modelAndView.addObject(ModelAttributetConstants.BUILD_NUMBER, currentSite.getBuildNumber());
		}

		//UF-287
		modelAndView.addObject(ModelAttributetConstants.NO_CACHE, java.lang.Math.round(java.lang.Math.random() * 2));
	}

	protected List getAddOnCommonCSSPaths(final String contextPath, final String uiExperience, final List<String> addOnNames)
	{
		final String[] propertyNames = new String[]
		{ RESOURCE_TYPE_CSS + ".paths", //
				RESOURCE_TYPE_CSS + ".paths." + uiExperience //
		};

		return getAddOnResourcePaths(contextPath, addOnNames, propertyNames);
	}

	protected List getAddOnThemeCSSPaths(final String contextPath, final String themeName, final String uiExperience,
			final List<String> addOnNames)
	{
		final String[] propertyNames = new String[]
		{ RESOURCE_TYPE_CSS + ".paths." + uiExperience + "." + themeName };

		return getAddOnResourcePaths(contextPath, addOnNames, propertyNames);
	}

	protected List getAddOnJSPaths(final String contextPath, final String siteName, final String uiExperience,
			final List<String> addOnNames)
	{
		final String[] propertyNames = new String[]
		{ RESOURCE_TYPE_JAVASCRIPT + ".paths", //
				RESOURCE_TYPE_JAVASCRIPT + ".paths." + uiExperience //
		};

		return getAddOnResourcePaths(contextPath, addOnNames, propertyNames);
	}


	protected List getAddOnResourcePaths(final String contextPath, final List<String> addOnNames, final String[] propertyNames)
	{
		final List<String> addOnResourcePaths = new ArrayList<String>();

		for (final String addon : addOnNames)
		{
			for (final String propertyName : propertyNames)
			{
				final String addOnResourcePropertyValue = siteConfigService.getProperty(addon + "." + propertyName);
				if (addOnResourcePropertyValue != null)
				{
					final String[] propertyPaths = addOnResourcePropertyValue.split(";");
					for (final String propertyPath : propertyPaths)
					{
						addOnResourcePaths.add(contextPath + "/_ui/addons/" + addon + propertyPath);
					}
				}
			}
		}
		return addOnResourcePaths;
	}

	protected String getThemeNameForSite(final CMSSiteModel site)
	{
		final SiteTheme theme = site.getTheme();
		if (theme != null)
		{
			final String themeCode = theme.getCode();
			if (themeCode != null && !themeCode.isEmpty())
			{
				return themeCode;
			}
		}
		return getDefaultThemeName();
	}

	protected String getDefaultThemeName()
	{
		return defaultThemeName;
	}

	@Required
	public void setDefaultThemeName(final String defaultThemeName)
	{
		this.defaultThemeName = defaultThemeName;
	}



}
