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
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.granule.json.JSONException;
import com.granule.json.JSONObject;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.facades.webform.MplWebFormFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.web.forms.TicketWebForm;


/**
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/ticketForm")
public class WebFormPageController extends AbstractMplSearchPageController
{
	protected static final Logger LOG = Logger.getLogger(WebFormPageController.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "storeBreadcrumbBuilder")
	private StoreBreadcrumbBuilder storeBreadcrumbBuilder;

	@Resource(name = "mplWebFormFacade")
	private MplWebFormFacade mplWebFormFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	private static final String WEB_FORM = "faq";

	@RequestMapping(method = RequestMethod.GET)
	public String ticketFormView(
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = ModelAttributetConstants.ZERO_VAL) final int page,
			@RequestParam(value = ModelAttributetConstants.SHOW, defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = ModelAttributetConstants.SORT, required = false) final String sortCode, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final TicketWebForm form = new TicketWebForm();
		model.addAttribute("ticketForm", form);

		final int pageSize = configurationService.getConfiguration().getInt(MessageConstants.ORDER_HISTORY_PAGESIZE, 10);
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		model.addAttribute("formFields", mplWebFormFacade.getWebCRMForm(pageableData));

		storeCmsPageInModel(model, getContentPageForLabelOrId(WEB_FORM));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WEB_FORM));
		return ControllerConstants.Views.Pages.Misc.webForm;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String ticketFormSave(@ModelAttribute final TicketWebForm webForm, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		final WebFormData formData = new WebFormData();
		try
		{
			formData.setComment(webForm.getComment());
			final CustomerData currentUser = customerFacade.getCurrentCustomer();
			if (currentUser != null)
			{
				formData.setCustomerId(currentUser.getUid());
			}
			formData.setOrderCode(webForm.getOrderCode());
			formData.setSubOrderCode(webForm.getSubOrderCode());
			formData.setTransactionId(webForm.getTransactionId());

			//formData.setL0code(webForm.get);

			mplWebFormFacade.sendWebformTicket(formData);
			model.addAttribute("ticketForm", webForm);
		}
		catch (final Exception e)
		{
			LOG.error("ticketFormSave" + e);
		}
		return ControllerConstants.Views.Pages.Misc.webFormSuccess;
	}

	@RequestMapping(value = "/crmChildrenNodes", method = RequestMethod.GET)
	public @ResponseBody JSONObject getChildrens(@RequestParam(value = "nodeParent") final String nodeParent)
			throws CMSItemNotFoundException
	{
		final JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("nodes", mplWebFormFacade.getWebCRMChildren(nodeParent));
		}
		catch (final JSONException e)
		{
			LOG.error("getChildrens" + e);
		}

		return jsonObj;
	}

}
