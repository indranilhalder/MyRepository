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
package com.tisl.mpl.storefront.util;

import de.hybris.platform.acceleratorservices.storefront.util.PageTitleResolver;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Resolves page title according to page, search text, current category or product
 */
public class MplPageTitleResolver extends PageTitleResolver
{
	protected static final String TITLE_WORD_SEPARATOR = " | ";
	private static final Logger LOG = Logger.getLogger(MplPageTitleResolver.class);

	@Override
	public String resolveCategoryPageTitle(final CategoryModel category)
	{
		String pageTitle = null;
		int i = 0;
		final StringBuilder stringBuilder = new StringBuilder();
		final CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();
		if (currentSite != null)
		{
			if (null != currentSite.getName())
			{
				stringBuilder.append(currentSite.getName());
			}
		}
		final List<CategoryModel> categories = this.getCategoryPath(category);
		if (null != currentSite.getName())
		{
			for (final CategoryModel c : categories)
			{
				stringBuilder.append(TITLE_WORD_SEPARATOR).append(c.getName());
			}
		}
		else
		{
			for (final CategoryModel c : categories)
			{
				if (i > 0)
				{
					stringBuilder.append(TITLE_WORD_SEPARATOR);
				}
				stringBuilder.append(c.getName());
				i = i + 1;
			}
		}

		//pageTitle = StringEscapeUtils.escapeHtml(stringBuilder.toString());
		pageTitle = stringBuilder.toString();//PRDI-422
		LOG.debug("Inside category page title" + pageTitle);
		return pageTitle;
	}

	@Override
	public String resolveHomePageTitle(final String title)
	{
		final CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();
		final StringBuilder builder = new StringBuilder();

		if (currentSite != null)
		{
			if (null != currentSite.getName())
			{
				builder.append(currentSite.getName()).append(TITLE_WORD_SEPARATOR);
			}
		}
		if (!StringUtils.isEmpty(title))
		{
			builder.append(title);
		}
		//return StringEscapeUtils.escapeHtml(builder.toString());
		return builder.toString();//PRDI-422
	}

	@Override
	public String resolveContentPageTitle(final String title)
	{
		final CMSSiteModel currentSite = getCmsSiteService().getCurrentSite();

		final StringBuilder builder = new StringBuilder();
		if (!StringUtils.isEmpty(title))
		{
			builder.append(title);
		}
		if (currentSite != null)
		{
			if (null != currentSite.getName())
			{
				builder.append(TITLE_WORD_SEPARATOR).append(currentSite.getName());
			}
		}
		//return StringEscapeUtils.escapeHtml(builder.toString());
		return builder.toString();//PRDI-422
	}
}
