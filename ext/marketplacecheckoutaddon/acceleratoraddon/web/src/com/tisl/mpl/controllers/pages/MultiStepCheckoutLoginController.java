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
package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorfacades.flow.CheckoutFlowFacade;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.security.AutoLoginStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.FriendsInviteFacade;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FBConnection;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.controllers.helpers.GoogleAuthHelper;
import com.tisl.mpl.storefront.web.forms.ExtRegisterForm;
import com.tisl.mpl.storefront.web.forms.validator.RegisterPageValidator;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Checkout Login Controller. Handles login and register for the checkout flow.
 */
@Controller
//@Scope("tenant")
@RequestMapping(value = "/checkout/multi/checkoutlogin")
public class MultiStepCheckoutLoginController extends MplAbstractCheckoutStepController
{


	protected static final String SPRING_SECURITY_LAST_USERNAME = "SPRING_SECURITY_LAST_USERNAME";


	@Resource(name = "registerPageValidator")
	private RegisterPageValidator registerPageValidator;
	@Autowired
	private FriendsInviteFacade friendsInviteFacade;
	@Resource(name = "registerCustomerFacade")
	private RegisterCustomerFacade registerCustomerFacade;

	@Resource(name = "autoLoginStrategy")
	private AutoLoginStrategy autoLoginStrategy;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;


	/**
	 * @return the resourceBreadcrumbBuilder
	 */
	@Override
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

	private static final String BZ_ERROR_CMS_PAGE = "businessErrorFound";
	private static final String NBZ_ERROR_CMS_PAGE = "nonBusinessErrorFound";
	private final static String pageName = "DefaultLoginPage";
	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "checkoutFlowFacade")
	private CheckoutFlowFacade checkoutFlowFacade;


	@Override
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@RequireHardLogIn
	//@PreValidateCheckoutStep(checkoutStep = "checkout-login")
	public String enterStep(
			@RequestParam(value = ModelAttributetConstants.ERROR, defaultValue = ModelAttributetConstants.FALSE) final boolean loginError,
			final HttpSession session, final Model model, final HttpServletRequest request,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, CommerceCartModificationException
	{

		try
		{

			if (!getUserFacade().isAnonymousUser())
			{
				return getCheckoutStep().nextStep();
			}
			model.addAttribute("expressCheckoutAllowed", Boolean.valueOf(checkoutFlowFacade.isExpressCheckoutEnabledForStore()));
			model.addAttribute(new ExtRegisterForm());
			final GoogleAuthHelper helper = new GoogleAuthHelper();
			final FBConnection fBConnection = new FBConnection();
			if (request.getParameter("code") == null || request.getParameter("state") == null)
			{ /*
			   * * set the secure state token in session to be able to track what we sent to google
			   */
				request.getSession().setAttribute("state", helper.getStateToken());
				session.setAttribute("state", helper.getStateToken());
				model.addAttribute("urlVisit", helper.buildLoginUrl());
				model.addAttribute("urlVisitForFacebook", fBConnection.getFBAuthUrl());
			}

			prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryAddress.breadcrumb"));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			storeContentPageTitleInModel(model, "Checkout Login Sign Up");
			//return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.MplCheckOutLoginPage;
			model.addAttribute("isSignInActive", "Y");

			model.addAttribute("pageName", pageName);
			return getDefaultLoginPage(loginError, session, model);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.info("Exception is : " + e);
			final String messageKey = "system.error.page.business";
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.info("Exception is : " + e);
			final String messageKey = "system.error.page.non.business";
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
		try
		{
			//Fix for defect TISEE-3986 : handling special character like #
			final String password = java.net.URLDecoder.decode(request.getParameter("pwd"), "UTF-8");
			final String rePassword = java.net.URLDecoder.decode(request.getParameter("checkPwd"), "UTF-8");
			form.setPwd(password);
			form.setCheckPwd(rePassword);

			getRegisterPageValidator().validate(form, bindingResult);
			return processRegisterUserRequestNew(null, form, bindingResult, model, request, response, redirectModel);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
	}



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

			model.addAttribute("pageName", pageName);
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.MplCheckOutLoginPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.info("Exception is : " + e);
			final String messageKey = "system.error.page.business";
			callBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.info("Exception is : " + e);
			final String messageKey = "system.error.page.non.business";
			callNonBusinessError(model, messageKey);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
	}

	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("checkout-login");
	}

	protected String getView()
	{

		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.MplCheckOutLoginPage;
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


	private void callNonBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.not.found"));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, "Non Business Error");
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
		if (bindingResult.hasErrors())
		{
			model.addAttribute(form);
			model.addAttribute(new LoginForm());
			model.addAttribute(new GuestForm());
			model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, ModelAttributetConstants.N_CAPS_VAL);
			GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
			model.addAttribute("signupError", Boolean.TRUE);
			prepareDataForPage(model);
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			storeContentPageTitleInModel(model, "Checkout Login Sign Up");
			model.addAttribute("metaRobots", "index,nofollow");
			final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
					getI18nService().getCurrentLocale()), null);
			model.addAttribute("breadcrumbs", Collections.singletonList(loginBreadcrumbEntry));
			return handleRegistrationError(model);
		}

		final ExtRegisterData data = new ExtRegisterData();
		data.setLogin(form.getEmail().toLowerCase());
		data.setPassword(form.getPwd());
		data.setAffiliateId(form.getAffiliateId());


		//implementation for TISCR-278 :start

		if (null != request.getParameter(ModelAttributetConstants.CHECK_MY_REWARDS)
				&& request.getParameter(ModelAttributetConstants.CHECK_MY_REWARDS).equalsIgnoreCase(ModelAttributetConstants.TRUE))
		{

			data.setCheckTataRewards(true);

		}


		//implementation for TISCR-278 :end


		try
		{
			if (getRegisterCustomerFacade().checkUniquenessOfEmail(data))
			{
				getRegisterCustomerFacade().register(data);
				getAutoLoginStrategy().login(form.getEmail().toLowerCase(), form.getPwd(), request, response);

				//	updating the isRegistered flag in friends model (in case of valid affiliated id)
				if (null != form.getAffiliateId() && !StringUtils.isBlank(form.getAffiliateId()))
				{
					final FriendsInviteData friendsData = new FriendsInviteData();
					friendsData.setAffiliateId(form.getAffiliateId());
					friendsData.setFriendsEmail(form.getEmail());
					friendsInviteFacade.updateFriendsModel(friendsData);
				}
				model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, ModelAttributetConstants.N_CAPS_VAL);
			}
			else
			{
				rePopulateLoginPageWithDuplicateUidErrorMsg(model, form, bindingResult);
				return handleRegistrationError(model);
			}
		}
		//		catch (final DuplicateUidException e)
		//		{
		//			rePopulateLoginPageWithDuplicateUidErrorMsg(model, form, bindingResult);
		//			return handleRegistrationError(model);
		//		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}

		model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, ModelAttributetConstants.N_CAPS_VAL);
		//return REDIRECT_PREFIX + getSuccessRedirect(request, response);
		return getSuccessRedirect(request, response);
	}

	/**
	 * @param form
	 * @param model
	 * @param bindingResult
	 *
	 */
	private void rePopulateLoginPageWithDuplicateUidErrorMsg(final Model model, final ExtRegisterForm form,
			final BindingResult bindingResult) throws CMSItemNotFoundException
	{
		model.addAttribute(new LoginForm());
		model.addAttribute(new GuestForm());
		model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, ModelAttributetConstants.N_CAPS_VAL);
		bindingResult.rejectValue(ModelAttributetConstants.EMAIL, MessageConstants.REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE);
		GlobalMessages.addErrorMessage(model, MessageConstants.REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE);
		model.addAttribute("signupError", Boolean.TRUE);
		prepareDataForPage(model);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		storeContentPageTitleInModel(model, "Checkout Login Sign Up");
		model.addAttribute("metaRobots", "index,nofollow");
		final Breadcrumb loginBreadcrumbEntry = new Breadcrumb("#", getMessageSource().getMessage("header.link.login", null,
				getI18nService().getCurrentLocale()), null);
		model.addAttribute("breadcrumbs", Collections.singletonList(loginBreadcrumbEntry));
		model.addAttribute("isSignInActive", "N");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.controllers.pages.CheckoutStepController#back(org.springframework.web.servlet.mvc.support.
	 * RedirectAttributes)
	 */
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.controllers.pages.CheckoutStepController#next(org.springframework.web.servlet.mvc.support.
	 * RedirectAttributes)
	 */
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep("checkout-login");
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

	protected String handleRegistrationError(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getCmsPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
		return getView();
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

	/**
	 * @return the autoLoginStrategy
	 */
	protected AutoLoginStrategy getAutoLoginStrategy()
	{
		return autoLoginStrategy;
	}

	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (hasItemsInCart())
		{
			return getCheckoutUrl();
		}
		// Redirect to the main checkout controller to handle checkout.
		return "/checkout";
	}

	protected boolean hasItemsInCart()
	{
		final CartData cartData = getCheckoutFlowFacade().getCheckoutCart();

		return (cartData.getEntries() != null && !cartData.getEntries().isEmpty());
	}

	protected String getCheckoutUrl()
	{
		// Default to the multi-step checkout
		return getCheckoutStep().nextStep();
		//return "/checkout/multi";
	}
}
