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
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.cms.data.WebForm;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.facades.cms.data.WebFormOrder;
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
	public String ticketFormView(final Model model) throws CMSItemNotFoundException
	{
		final TicketWebForm form = new TicketWebForm();
		model.addAttribute("ticketForm", form);

		model.addAttribute("formFields", mplWebFormFacade.getWebCRMForm());

		storeCmsPageInModel(model, getContentPageForLabelOrId(WEB_FORM));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(WEB_FORM));
		return ControllerConstants.Views.Pages.Misc.webForm;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String ticketFormSave(@ModelAttribute final TicketWebForm webForm, final Model model) throws CMSItemNotFoundException
	{
		final WebFormData formData = new WebFormData();
		String ticketRefId = null;
		try
		{
			final String ticketSubType = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.CRM_WEBFORM_TICKET_SUB, "L1C1");

			formData.setL0code(webForm.getNodeL0());
			formData.setL1code(webForm.getNodeL1());
			formData.setL2code(webForm.getNodeL2());
			formData.setL3code(webForm.getNodeL3());
			formData.setL4code(webForm.getNodeL4());
			formData.setCustomerEmail(webForm.getContactEmail());
			formData.setCustomerName(webForm.getContactName());
			formData.setCustomerMobile(webForm.getContactMobile());
			//CRM Mapping as per TPR-6872
			formData.setTicketType(webForm.getTicketType());
			if (webForm.getNodeL1().equalsIgnoreCase(ticketSubType))
			{
				formData.setTicketSubType(MarketplacecommerceservicesConstants.CRM_WEBFORM_TICKET_SUB_ORDER);
			}
			else
			{
				formData.setTicketSubType(MarketplacecommerceservicesConstants.CRM_WEBFORM_TICKET_SUB_NONORDER);
			}
			formData.setComment(webForm.getComment());
			formData.setOrderCode(webForm.getOrderCode());
			formData.setSubOrderCode(webForm.getSubOrderCode());
			formData.setTransactionId(webForm.getTransactionId());

			//uploading file if any
			if (CollectionUtils.isNotEmpty(webForm.getAttachmentFiles()))
			{
				formData.setAttachments(String.join(",", webForm.getAttachmentFiles()));
			}
			ticketRefId = mplWebFormFacade.sendWebformTicket(formData);
			model.addAttribute("ticketRefId", ticketRefId);
			model.addAttribute("ticketForm", webForm);
			model.addAttribute("issue", webForm.getNodeL2Text());
			model.addAttribute("subIssue", webForm.getNodeL3Text());
		}
		catch (final Exception e)
		{
			LOG.error("ticketFormSave" + e);
		}
		return ControllerConstants.Views.Pages.Misc.webFormSuccess;
	}

	@RequestMapping(value = "/crmChildrenNodes", method = RequestMethod.GET)
	public @ResponseBody WebForm getChildrens(@RequestParam(value = "nodeParent") final String nodeParent)
			throws CMSItemNotFoundException
	{
		WebForm jsonObj = new WebForm();
		try
		{
			jsonObj = mplWebFormFacade.getWebCRMChildren(nodeParent);
		}
		catch (final Exception e)
		{
			LOG.error("getChildrens" + e);
		}

		return jsonObj;
	}

	@RequestMapping(value = "/fileUpload", method =
	{ RequestMethod.POST })
	public @ResponseBody String fileUpload(@RequestPart(value = "uploadFile") final List<MultipartFile> uploadFile)
			throws CMSItemNotFoundException
	{
		String fileUploadLocation = null, nowDate = null;
		String filelocation = null;
		final List<String> filelocations = new ArrayList<>();
		Path path = null;
		fileUploadLocation = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.CRM_FILE_UPLOAD_PATH);
		if (StringUtils.isNotEmpty(fileUploadLocation))
		{
			try
			{
				for (final MultipartFile file : uploadFile)
				{
					final byte barr[] = file.getBytes();
					final SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
					nowDate = sdf.format(new Date());
					path = Paths.get(fileUploadLocation + File.separator + nowDate);
					//if directory exists?
					if (!Files.exists(path))
					{
						try
						{
							Files.createDirectories(path);
						}
						catch (final IOException e)
						{
							//fail to create directory
							LOG.error("Exception ,While creating the Directory " + e.getMessage());
						}
					}
					filelocation = path + File.separator + file.getOriginalFilename();
					final BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(filelocation));
					bout.write(barr);
					bout.flush();
					bout.close();
					LOG.debug("FileUploadLocation   :" + filelocation);
					filelocations.add(filelocation);
				}
			}
			catch (final Exception e)
			{
				LOG.error("Exception is:" + e);
				filelocation = "error";
				filelocations.add(filelocation);
			}
		}

		return String.join(",", filelocations);
	}

	@RequestMapping(value = "/webOrderlines", method = RequestMethod.GET)
	public @ResponseBody WebFormOrder getChildrens(
			@RequestParam(value = ModelAttributetConstants.PAGE, defaultValue = ModelAttributetConstants.ZERO_VAL) final int page,
			@RequestParam(value = ModelAttributetConstants.SHOW, defaultValue = ModelAttributetConstants.PAGE_VAL) final ShowMode showMode,
			@RequestParam(value = ModelAttributetConstants.SORT, required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		WebFormOrder form = new WebFormOrder();
		final int pageSize = configurationService.getConfiguration().getInt(MessageConstants.WEBFORM_ORDER_HISTORY_PAGESIZE, 5);
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		form = mplWebFormFacade.getWebOrderLines(pageableData);

		return form;
	}
}
