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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.filters.btg.support.BTGSegmentStrategy;
import com.tisl.mpl.storefront.interceptors.BeforeViewHandler;


public class BtgSegmentBeforeViewHandler implements BeforeViewHandler
{
	private BTGSegmentStrategy btgSegmentStrategy;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		try
		{
			getBtgSegmentStrategy().evaluateSegment(request);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	protected BTGSegmentStrategy getBtgSegmentStrategy()
	{
		return btgSegmentStrategy;
	}

	@Required
	public void setBtgSegmentStrategy(final BTGSegmentStrategy btgSegmentStrategy)
	{
		this.btgSegmentStrategy = btgSegmentStrategy;
	}
}
