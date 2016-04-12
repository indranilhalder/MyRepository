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

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.enumeration.EnumerationService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.core.enums.FeedbackCategory;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.search.feedback.facades.UpdateFeedbackFacade;


@Controller
@Scope("tenant")
@RequestMapping(value = "/feedback")
public class FeedbackController extends AbstractPageController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(FeedbackController.class);

	@Autowired
	private UpdateFeedbackFacade updateFeedbackFacade;

	@Autowired
	private EnumerationService enumerationService;

	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;

	@RequestMapping(value = "/feedbackyes", method = RequestMethod.GET)
	public void captureFeedbackYes(@RequestParam("emailField") final String email,
			@RequestParam("commentyes") final String comment, @RequestParam("name") final String name,
			final HttpServletRequest request, final Model model, final HttpServletResponse response) throws CMSItemNotFoundException
	{

		response.setContentType("text/html");
		PrintWriter pw = null;
		final String returnValue = updateFeedbackFacade.updateFeedbackYes(email, comment, name);
		try
		{
			pw = response.getWriter();
			pw.print(returnValue);
		}
		catch (final IOException e)
		{
			LOG.info("IOException for Taking Feed Back");
		}
		finally
		{
			if (null != pw)
			{
				pw.close();
			}
		}

	}

	@RequestMapping(value = "/feedbackno", method = RequestMethod.GET)
	public String captureFeedbackNo(@RequestParam("commentNO") final String comment,
			@RequestParam("category") final String category, @RequestParam("emailField") final String customerEmail,
			@RequestParam("searchCategoryhidden") final String searchCategory, @RequestParam("searchText") final String searchText,
			final HttpServletRequest request, final Model model, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		response.setContentType("text/html");
		PrintWriter pw = null;
		final String returnValue = updateFeedbackFacade.updateFeedbackNo(comment, category, customerEmail, searchCategory,
				searchText);
		try
		{
			pw = response.getWriter();
			pw.print(returnValue);
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			LOG.info("IOException for Taking Feed Back");
		}
		finally
		{
			if (null != pw)
			{
				pw.close();
			}
		}

		return getViewForPage(model);
	}

	@ResponseBody
	@RequestMapping(value = "/searchcategotylist", method = RequestMethod.GET, produces = "application/json")
	public Map getFeedbackCategory(final HttpServletRequest request, final HttpServletResponse response)
	{
		//		final List<FeedbackCategory> category = enumerationService.getEnumerationValues(FeedbackCategory.class);
		//		final Map<String, String> sFeedBack = new HashMap<String, String>();
		//		for (final FeedbackCategory s : category)
		//		{
		//			sFeedBack.put(s.getCode(), s.getCode());
		//		}
		final List<EnumerationValueModel> category = mplEnumerationHelper.getEnumerationValuesForCode(FeedbackCategory._TYPECODE);
		final Map<String, String> sFeedBack = new HashMap<String, String>();
		for (final EnumerationValueModel enumVal : category)
		{
			if (enumVal != null)
			{
				sFeedBack.put(enumVal.getCode(), enumVal.getName());
			}

		}
		return sFeedBack;

	}


}
