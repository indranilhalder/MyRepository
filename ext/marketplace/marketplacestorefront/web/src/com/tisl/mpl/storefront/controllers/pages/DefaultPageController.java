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
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ContentPageBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.UserFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

import com.tisl.lux.facade.CommonUtils;
import com.tisl.mpl.storefront.security.cookie.EnhancedCookieGenerator;


/**
 * Error handler to show a CMS managed error page. This is the catch-all controller that handles all GET requests that
 * are not handled by other controllers.
 */
@Controller
@Scope("tenant")
//@RequestMapping()
public class DefaultPageController extends AbstractPageController
{
	private static final String ERROR_CMS_PAGE = "notFound";

	private final UrlPathHelper urlPathHelper = new UrlPathHelper();

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "contentPageBreadcrumbBuilder")
	private ContentPageBreadcrumbBuilder contentPageBreadcrumbBuilder;

	@Resource(name = "luxuryCookieGenerator")
	private EnhancedCookieGenerator luxuryCookieGenerator;

	@Autowired
	private CommonUtils commonUtils;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException
	{
		// Check for CMS Page where label or id is like /page
		final ContentPageModel pageForRequest = getContentPageForRequest(request);
		if (commonUtils.isLuxurySite() && userFacade.isAnonymousUser() && request.getParameter("isHomePage") != null)
		{
			luxuryCookieGenerator.addCookie(response, pageForRequest.getLabel());
		}
		if (pageForRequest != null)
		{
			storeCmsPageInModel(model, pageForRequest);
			setUpMetaDataForContentPage(model, pageForRequest);
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, contentPageBreadcrumbBuilder.getBreadcrumbs(pageForRequest));
			return getViewForPage(pageForRequest);
		}

		// No page found - display the notFound page with error from controller
		storeCmsPageInModel(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));
		//GlobalMessages.addErrorMessage(model, "system.error.page.not.found");

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);

		return getViewForPage(model);
	}

	/**
	 * Lookup the CMS Content Page for this request.
	 *
	 * @param request
	 *           The request
	 * @return the CMS content page
	 */
	protected ContentPageModel getContentPageForRequest(final HttpServletRequest request)
	{
		// Get the path for this request.
		// Note that the path begins with a '/'
		final String lookupPathForRequest = urlPathHelper.getLookupPathForRequest(request);
		//INC144319366 fix start
		try
		{
			// Lookup the CMS Content Page by label. Note that the label value must begin with a '/'.
			final ContentPageModel contentPageModel = getCmsPageService().getPageForLabel(lookupPathForRequest);
			return contentPageModel;
		}
		catch (final CMSItemNotFoundException ignore)
		{
			if (lookupPathForRequest.contains("page"))
			{
				final String excludedUrlPage = lookupPathForRequest.replaceAll("/page-[0-9]", "");
				try
				{
					return getCmsPageService().getPageForLabel(excludedUrlPage);
				}
				catch (final CMSItemNotFoundException e)
				{
					LOG.error(e);
				}
			}
			// Ignore exception
			LOG.error(ignore);
		}
		//INC144319366 fix ends
		return null;
	}
}
