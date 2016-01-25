/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
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

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractRegisterPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.web.forms.ExtRegisterForm;
import com.tisl.mpl.storefront.web.forms.validator.RegisterPageValidator;


/**
 * Register Controller for mobile. Handles login and register for the account flow.
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_REGISTER)
public class RegisterPageController extends AbstractRegisterPageController
{
	private HttpSessionRequestCache httpSessionRequestCache;
	@Resource(name = ModelAttributetConstants.REGISTER_PAGE_VALIDATOR)
	private RegisterPageValidator registerPageValidator;

	@Resource(name = ModelAttributetConstants.REGISTER_CUSTOMER_FACADE)
	private RegisterCustomerFacade registerCustomerFacade;

	private static final String BZ_ERROR_CMS_PAGE = "businessErrorFound";
	private static final String NBZ_ERROR_CMS_PAGE = "nonBusinessErrorFound";
	@Resource(name = ModelAttributetConstants.SIMPLE_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	/**
	 * @return the registerCustomerFacade
	 */
	public RegisterCustomerFacade getRegisterCustomerFacade()
	{
		return registerCustomerFacade;
	}

	/**
	 * @param registerCustomerFacade
	 *           the registerCustomerFacade to set
	 */
	public void setRegisterCustomerFacade(final RegisterCustomerFacade registerCustomerFacade)
	{
		this.registerCustomerFacade = registerCustomerFacade;
	}

	/**
	 * @return the registerPageValidator
	 */
	public RegisterPageValidator getRegisterPageValidator()
	{
		return registerPageValidator;
	}

	/**
	 * @param registerPageValidator
	 *           the registerPageValidator to set
	 */
	public void setRegisterPageValidator(final RegisterPageValidator registerPageValidator)
	{
		this.registerPageValidator = registerPageValidator;
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId(ModelAttributetConstants.REGISTER);
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		return RequestMappingUrlConstants.LINK_MY_ACCOUNT;
	}

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountRegisterPage;
	}

	@Resource(name = ModelAttributetConstants.HTTP_SESSION_REQUEST_CACHE)
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	/**
	 * @description this method is called for registration process
	 * @param referer
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return getDefaultRegistrationPage
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String doRegister(@RequestParam(required = false) final String affiliateId, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		try
		{
			if (StringUtils.isBlank(affiliateId))
			{
				final ExtRegisterForm form = new ExtRegisterForm();
				form.setAffiliateId(affiliateId);
				model.addAttribute(form);
			}
			else
			{
				model.addAttribute(new ExtRegisterForm());

			}
			return getDefaultRegistrationPage(model);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.info(MessageConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS;
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.info(MessageConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}

	/**
	 * @description this method is called for registration process
	 * @param referer
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return processRegisterUserRequest
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/newcustomer", method = RequestMethod.POST)
	public String doRegister(@RequestParam(required = false) final String affiliateId, final ExtRegisterForm form,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{

		getRegisterPageValidator().validate(form, bindingResult);
		return processRegisterUserRequest(null, form, bindingResult, model, request, response, redirectModel);
	}

	/**
	 * @description this is called for user registration process
	 * @param referer
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return post login page
	 * @throws CMSItemNotFoundException
	 */
	private String processRegisterUserRequest(final String referer, final ExtRegisterForm form, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			if (bindingResult.hasErrors())
			{
				model.addAttribute(form);
				model.addAttribute(new LoginForm());
				model.addAttribute(new GuestForm());
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				return handleRegistrationError(model);
			}

			final ExtRegisterData data = new ExtRegisterData();
			data.setLogin(form.getEmail());
			data.setPassword(form.getPwd());
			data.setAffiliateId(form.getAffiliateId());
			try
			{
				getRegisterCustomerFacade().register(data);
				getAutoLoginStrategy().login(form.getEmail().toLowerCase(), form.getPwd(), request, response);

				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
						"registration.confirmation.message.title");
			}
			catch (final DuplicateUidException e)
			{
				LOG.warn(MessageConstants.EXCEPTION_IS + e);
				model.addAttribute(form);
				model.addAttribute(new LoginForm());
				model.addAttribute(new GuestForm());
				bindingResult.rejectValue(ModelAttributetConstants.EMAIL, "registration.error.account.exists.title");
				GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
				return handleRegistrationError(model);
			}

			return REDIRECT_PREFIX + getSuccessRedirect(request, response);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.info(MessageConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final IllegalArgumentException e)
		{
			LOG.info(MessageConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.info(MessageConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS;
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.info(MessageConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}

	@Override
	protected String getDefaultRegistrationPage(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
				getI18nService().getCurrentLocale()), null);
		model.addAttribute(ModelAttributetConstants.BREADCRUMBS, Collections.singletonList(loginBreadcrumbEntry));
		return getView();
	}


	/**
	 * @param model
	 */
	private void callNonBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.BREADCRUMB_NOT_FOUND));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, MessageConstants.NON_BUSINESS_ERROR);
	}

	/**
	 * @param model
	 */
	private void callBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.BREADCRUMB_NOT_FOUND));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, MessageConstants.BUSINESS_ERROR);
	}
}
