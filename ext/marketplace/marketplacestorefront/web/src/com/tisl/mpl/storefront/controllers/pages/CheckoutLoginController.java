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

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.GuestValidator;
import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FBConnection;
import com.tisl.mpl.storefront.controllers.helpers.GoogleAuthHelper;
import com.tisl.mpl.storefront.web.forms.ExtRegisterForm;
import com.tisl.mpl.storefront.web.forms.validator.RegisterPageValidator;


/**
 * Checkout Login Controller. Handles login and register for the checkout flow.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.LOGIN_CHECKOUT)
public class CheckoutLoginController extends AbstractLoginPageController
{
	private final String BZ_ERROR_CMS_PAGE = "businessErrorFound";
	private final String NBZ_ERROR_CMS_PAGE = "nonBusinessErrorFound";

	@Resource(name = "checkoutFlowFacade")
	private CheckoutFlowFacade checkoutFlowFacade;

	@Resource(name = "guidCookieStrategy")
	private GUIDCookieStrategy guidCookieStrategy;

	@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager;

	@Resource(name = "guestValidator")
	private GuestValidator guestValidator;

	@Resource(name = "registerCustomerFacade")
	private RegisterCustomerFacade registerCustomerFacade;

	@Resource(name = "registerPageValidator")
	private RegisterPageValidator registerPageValidator;

	/**
	 * @return the resourceBreadcrumbBuilder
	 */
	public ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder()
	{
		return resourceBreadcrumbBuilder;
	}

	/**
	 * @param resourceBreadcrumbBuilder
	 *           the resourceBreadcrumbBuilder to set
	 */
	public void setResourceBreadcrumbBuilder(final ResourceBreadcrumbBuilder resourceBreadcrumbBuilder)
	{
		this.resourceBreadcrumbBuilder = resourceBreadcrumbBuilder;
	}

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

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

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("checkout-login");
	}

	/**
	 * @description this is called to populate checkout Login page
	 * @param loginError
	 * @param session
	 * @param model
	 * @param request
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String doCheckoutLogin(@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			final HttpSession session, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		try
		{
			model.addAttribute(ModelAttributetConstants.EXPRESS_CHECKOUT_ALLOWED,
					Boolean.valueOf(checkoutFlowFacade.isExpressCheckoutEnabledForStore()));
			model.addAttribute(new ExtRegisterForm());
			final GoogleAuthHelper helper = new GoogleAuthHelper();
			final FBConnection fBConnection = new FBConnection();
			if (request.getParameter("code") == null || request.getParameter("state") == null)
			{ /*
			   * * set the secure state token in session to be able to track what we sent to google
			   */
				request.getSession().setAttribute("state", helper.getStateToken());
				//session.setAttribute("state", helper.getStateToken()); //IQA Review Comment Fix - Redundant codes written
				model.addAttribute(ModelAttributetConstants.URLVISIT, helper.buildLoginUrl());
				model.addAttribute(ModelAttributetConstants.URLVISITFORFACEBOOK, fBConnection.getFBAuthUrl());
			}
			return getDefaultLoginPage(loginError, session, model);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS;
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}


	/**
	 * @description this is called to populate Login page
	 * @return String
	 */
	@Override
	protected String getDefaultLoginPage(final boolean loginError, final HttpSession session, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final LoginForm loginForm = new LoginForm();
			model.addAttribute(loginForm);
			model.addAttribute(new GuestForm());

			final String username = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);
			if (username != null)
			{
				session.removeAttribute(SPRING_SECURITY_LAST_USERNAME);
			}

			loginForm.setJ_username(username);
			storeCmsPageInModel(model, getCmsPage());
			setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
			model.addAttribute("metaRobots", "index,nofollow");

			final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
					getI18nService().getCurrentLocale()), null);
			model.addAttribute("breadcrumbs", Collections.singletonList(loginBreadcrumbEntry));

			if (loginError)
			{
				model.addAttribute("loginError", Boolean.valueOf(loginError));
				GlobalMessages.addErrorMessage(model, "login.error.account.not.found.title");
			}

			return getView();
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS;
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}

	private void callNonBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, "Non Business Error");
	}


	private void callBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, "Business Error");
	}


	@RequestMapping(value = RequestMappingUrlConstants.REGISTER_PAGE, method = RequestMethod.POST)
	public String doCheckoutRegister(final ExtRegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		try
		{
			getRegisterPageValidator().validate(form, bindingResult);
			return processRegisterUserRequestNew(null, form, bindingResult, model, request, response, redirectModel);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS;
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}

	/**
	 * @description This method is used to register in checkout process
	 * @param model
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/checkoutRegister", method = RequestMethod.POST)
	public String checkoutRegister(final Model model, final ExtRegisterForm form, final BindingResult bindingResult,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		getRegisterPageValidator().validate(form, bindingResult);
		return processRegisterUserRequestNew(null, form, bindingResult, model, request, response, redirectModel);
	}

	/**
	 * @description This method is used to guest checkout process
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/guest", method = RequestMethod.POST)
	public String doAnonymousCheckout(final GuestForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException
	{
		getGuestValidator().validate(form, bindingResult);
		return processAnonymousCheckoutUserRequest(form, bindingResult, model, request, response);
	}

	/**
	 * @description This method is used to populate register page in checkout process
	 * @param loginError
	 * @param session
	 * @param model
	 * @param request
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String checkoutRegister(@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			final HttpSession session, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		return doCheckoutLogin(loginError, session, model, request);
	}

	/**
	 * @description This method is used to populate guest checkout process page
	 * @param loginError
	 * @param session
	 * @param model
	 * @param request
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/guest", method = RequestMethod.GET)
	public String doAnonymousCheckout(@RequestParam(value = "error", defaultValue = "false") final boolean loginError,
			final HttpSession session, final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		return doCheckoutLogin(loginError, session, model, request);
	}

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Checkout.CheckoutLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (hasItemsInCart())
		{
			return getCheckoutUrl();
		}
		// Redirect to the main checkout controller to handle checkout.
		return "/checkout";
	}

	/**
	 * Checks if there are any items in the cart.
	 *
	 * @return returns true if items found in cart.
	 */
	protected boolean hasItemsInCart()
	{
		final CartData cartData = getCheckoutFlowFacade().getCheckoutCart();

		return (cartData.getEntries() != null && !cartData.getEntries().isEmpty());
	}

	protected String getCheckoutUrl()
	{
		// Default to the multi-step checkout
		return "/checkout/multi";
	}

	protected GuestValidator getGuestValidator()
	{
		return guestValidator;
	}

	protected CheckoutFlowFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

	@Override
	protected GUIDCookieStrategy getGuidCookieStrategy()
	{
		return guidCookieStrategy;
	}

	protected AuthenticationManager getAuthenticationManager()
	{
		return authenticationManager;
	}

	/**
	 * @description This method is used to validate, register, and auto login in checkout process
	 * @param referer
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	private String processRegisterUserRequestNew(final String referer, final ExtRegisterForm form,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			if (bindingResult.hasErrors())
			{
				model.addAttribute(form);
				model.addAttribute(new LoginForm());
				model.addAttribute(new GuestForm());
				GlobalMessages.addErrorMessage(model, "form.global.error");
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
				LOG.warn("registration failed: " + e);
				model.addAttribute(form);
				model.addAttribute(new LoginForm());
				model.addAttribute(new GuestForm());
				bindingResult.rejectValue("email", "registration.error.account.exists.title");
				GlobalMessages.addErrorMessage(model, "form.global.error");
				return handleRegistrationError(model);
			}
			return REDIRECT_PREFIX + getSuccessRedirect(request, response);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS;
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MessageConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			final String messageKey = MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS;
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}
}
