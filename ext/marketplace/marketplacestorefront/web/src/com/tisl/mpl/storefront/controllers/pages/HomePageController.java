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

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.facades.account.register.MplCustomerProfileFacade;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.util.CSRFTokenManager;


/**
 * Controller for home page
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.ROOT)
public class HomePageController extends AbstractPageController
{

	@Resource
	private ModelService modelService;

	@Resource(name = "brandFacade")
	private BrandFacade brandFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Autowired
	private MplCustomerProfileFacade mplCustomerProfileFacade;

	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	private static final String userFirstName = "userFirstName";

	/**
	 * @description this is called to load home page
	 * @param logout
	 * @param model
	 * @param redirectModel
	 * @return getViewForPage
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(
			@RequestParam(value = ModelAttributetConstants.LOGOUT, defaultValue = ModelAttributetConstants.FALSE) final boolean logout,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (logout)
		{
			//GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, "account.confirmation.signout.title");
			return REDIRECT_PREFIX + ROOT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		updatePageTitle(model, getContentPageForLabelOrId(null));

		return getViewForPage(model);
	}

	/**
	 * @description this is called to update the page title
	 * @param model
	 * @param cmsPage
	 */
	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
	}

	/**
	 * @description Used to store emailid for newslettersubscription
	 * @param emailId
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = ModelAttributetConstants.NEWSLETTER, method = RequestMethod.GET)
	public String saveNewsletterSubscriptionEmail(@RequestParam(value = "email") String emailId)
	{
		final MplNewsLetterSubscriptionModel newsLetter = modelService.create(MplNewsLetterSubscriptionModel.class);
		emailId = emailId.toLowerCase();
		if (!validateEmailAddress(emailId))
		{
			return "mailFormatError";
		}
		else
		{
			newsLetter.setEmailId(emailId);
			final boolean result = brandFacade.checkEmailId(emailId);

			//newsLetter.setIsSaved(Boolean.TRUE);

			if (result)
			{
				modelService.save(newsLetter);
				return "success";
			}
			else
			{
				return "fail";
			}

		}

	}

	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * @description Used to store emailid for newslettersubscription
	 * @param emailId
	 * @return String
	 */
	@ResponseBody
	@RequestMapping(value = "/setheader", method = RequestMethod.GET)
	public Map<String, Object> setHeaderData(final HttpSession session)
	{

		final Map<String, Object> header = new HashMap<String, Object>();
		final CartData cartData = cartFacade.getMiniCart();
		header.put("cartcount", String.valueOf(cartData.getTotalItems()));

		header.put("dts", CSRFTokenManager.getTokenForSession(session));

		//customer name in the header
		if (!userFacade.isAnonymousUser())
		{
			header.put("loggedInStatus", true);
			final Object sessionFirstName = session.getAttribute(userFirstName);
			if (sessionFirstName == null)
			{
				final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
				String firstName = currentCustomer.getFirstName();
				if (StringUtils.contains(firstName, '@'))
				{
					firstName = StringUtils.EMPTY;
				}
				else if (StringUtils.length(firstName) > 25)
				{
					firstName = StringUtils.substring(firstName, 0, 25);
				}
				header.put(userFirstName, firstName);
				session.setAttribute(userFirstName, firstName);
			}
			else
			{
				header.put(userFirstName, sessionFirstName);
			}
		}
		else
		{
			header.put("loggedInStatus", false);
			header.put(userFirstName, null);
		}

		return header;
	}

}
