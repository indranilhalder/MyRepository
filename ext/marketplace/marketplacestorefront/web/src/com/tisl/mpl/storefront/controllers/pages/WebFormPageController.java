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

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.StoreBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.facades.cms.data.WebForm;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.web.forms.TicketWebForm;


/**
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/ticketForm")
public class WebFormPageController extends AbstractPageController
{
	protected static final Logger LOG = Logger.getLogger(WebFormPageController.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "storeBreadcrumbBuilder")
	private StoreBreadcrumbBuilder storeBreadcrumbBuilder;

	private static final String WEB_FORM = "faq";

	@RequestMapping(method = RequestMethod.GET)
	public String ticketFormView(final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final TicketWebForm form = new TicketWebForm();
		model.addAttribute("ticketForm", form);
		final WebForm formFields = new WebForm();
		model.addAttribute("formFields", formFields);

		storeCmsPageInModel(model, getContentPageForLabelOrId(WEB_FORM));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WEB_FORM));
		return ControllerConstants.Views.Pages.Misc.webForm;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String ticketFormSave(@ModelAttribute final TicketWebForm webForm, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{


		return ControllerConstants.Views.Pages.Misc.webFormSuccess;
	}

}
