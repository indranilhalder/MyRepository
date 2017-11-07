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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.granule.json.JSONException;
import com.granule.json.JSONObject;
import com.tisl.mpl.facades.cms.data.WebFormData;
import com.tisl.mpl.facades.webform.MplWebFormFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
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
			@RequestParam(value = ModelAttributetConstants.SORT, required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
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
	public String ticketFormSave(@ModelAttribute final TicketWebForm webForm, final Model model) throws CMSItemNotFoundException
	{
		final WebFormData formData = new WebFormData();
		String fileUploadLocation = null, nowDate = null;
		Path path = null;
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
			//uploading filre if any
			if (CollectionUtils.isNotEmpty(webForm.getAttachments()))
			{
				fileUploadLocation = configurationService.getConfiguration().getString(RequestMappingUrlConstants.FILE_UPLOAD_PATH);
				if (StringUtils.isNotEmpty(fileUploadLocation))
				{
					try
					{
						for (final MultipartFile filename : webForm.getAttachments())
						{
							final byte barr[] = filename.getBytes();
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
							final BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + File.separator
									+ filename.getOriginalFilename()));
							bout.write(barr);
							bout.flush();
							bout.close();
							LOG.debug("FileUploadLocation   :" + fileUploadLocation);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception is:" + e);
					}

				}
			}



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
