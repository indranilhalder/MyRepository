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
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.PrintWriter;
import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.data.FriendsInviteData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.FriendsInviteFacade;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FBConnection;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.controllers.helpers.GoogleAuthHelper;
import com.tisl.mpl.storefront.web.forms.ExtRegisterForm;
import com.tisl.mpl.storefront.web.forms.validator.RegisterPageValidator;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_LOGIN)
public class LoginPageController extends AbstractLoginPageController
{
	@Autowired
	private SessionService sessionService;
	@Autowired
	private FriendsInviteFacade friendsInviteFacade;
	@Autowired
	private ConfigurationService configurationService;
	private HttpSessionRequestCache httpSessionRequestCache;
	@Resource(name = ModelAttributetConstants.REGISTER_PAGE_VALIDATOR)
	private RegisterPageValidator registerPageValidator;
	//	@Resource(name = ModelAttributetConstants.SIMPLE_BREADCRUMB_BUILDER)
	//	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	private static final Logger LOG = Logger.getLogger(LoginPageController.class);


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

	@Resource(name = ModelAttributetConstants.REGISTER_CUSTOMER_FACADE)
	private RegisterCustomerFacade registerCustomerFacade;

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
	 * @description this method is called for load default login page
	 * @return String
	 */
	@SuppressWarnings("boxing")
	@Override
	protected String getDefaultLoginPage(boolean loginError, final HttpSession session, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final LoginForm loginForm = new LoginForm();
			model.addAttribute(loginForm);
			model.addAttribute(new GuestForm());
			sessionService.removeAttribute(ModelAttributetConstants.LAST_USERNAME_WITH_ERROR_ATTEMPT);
			final String username = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);

			if (username != null)
			{
				session.removeAttribute(SPRING_SECURITY_LAST_USERNAME);
			}

			loginForm.setJ_username(username);
			storeCmsPageInModel(model, getCmsPage());
			setUpMetaDataForContentPage(model, (ContentPageModel) getCmsPage());
			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.INDEX_NOFOLLOW);

			final Breadcrumb loginBreadcrumbEntry = new Breadcrumb(ModelAttributetConstants.HASH_VAL, getMessageSource().getMessage(
					MessageConstants.HEADER_LINK_LOGIN, null, getI18nService().getCurrentLocale()), null);
			model.addAttribute(ModelAttributetConstants.BREADCRUMBS, Collections.singletonList(loginBreadcrumbEntry));

			if (null != sessionService.getAttribute(ModelAttributetConstants.ERROR_ATTEPT))
			{
				final int totalErrorCount = sessionService.getAttribute(ModelAttributetConstants.ERROR_ATTEPT);

				if (totalErrorCount > 0)
				{
					loginError = true;
				}
				else
				{
					loginError = false;
				}
			}
			if (loginError)
			{
				sessionService.removeAttribute(ModelAttributetConstants.ERROR_ATTEPT);
				sessionService.setAttribute(ModelAttributetConstants.LAST_USERNAME_WITH_ERROR_ATTEMPT, loginForm.getJ_username());
				model.addAttribute(ModelAttributetConstants.LOGIN_ERROR, Boolean.valueOf(loginError));
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.FAILURE);
				model.addAttribute(ModelAttributetConstants.COUNT, sessionService.getAttribute(ModelAttributetConstants.COUNT));
			}
			storeContentPageTitleInModel(model, MessageConstants.LOGIN_PAGE_TITLE);
			return getView();
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

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
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
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId(ModelAttributetConstants.LOGIN);
	}


	@Resource(name = ModelAttributetConstants.HTTP_SESSION_REQUEST_CACHE)
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	/**
	 * @description this method is called for login process
	 * @param referer
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @return getDefaultLoginPage
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings(ModelAttributetConstants.BOXING)
	@RequestMapping(method = RequestMethod.GET)
	public String doLogin(@RequestHeader(value = ModelAttributetConstants.REFERER, required = false) final String referer,
			@RequestParam(value = ModelAttributetConstants.AFFILIATEID, required = false) final String affiliateId,
			@RequestParam(value = ModelAttributetConstants.IS_SIGN_IN_ACTIVE, required = false) final String isSignInActive,
			final Model model, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException
	{
		try
		{
			if (!StringUtils.isBlank(affiliateId))
			{
				final ExtRegisterForm form = new ExtRegisterForm();
				form.setAffiliateId(affiliateId);
				model.addAttribute(form);
			}
			else
			{
				model.addAttribute(new ExtRegisterForm());
			}
			storeReferer(referer, request, response);

			final GoogleAuthHelper googleAuthHelper = new GoogleAuthHelper();
			final FBConnection fbConnection = new FBConnection();
			if (request.getParameter(ModelAttributetConstants.CODE) == null
					|| request.getParameter(ModelAttributetConstants.STATE) == null)
			{
				request.getSession().setAttribute(ModelAttributetConstants.STATE, googleAuthHelper.getStateToken());
				session.setAttribute(ModelAttributetConstants.STATE, googleAuthHelper.getStateToken());
				model.addAttribute(ModelAttributetConstants.URL_VISIT, googleAuthHelper.buildLoginUrl());
				model.addAttribute(ModelAttributetConstants.URL_VISIT_FOR_FACEBOOK, fbConnection.getFBAuthUrl());
			}
			model.addAttribute(ModelAttributetConstants.RECAPTCHA_KEY, getCaptchaKey());
			if (null != isSignInActive && !StringUtils.isEmpty(isSignInActive))
			{
				model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, isSignInActive);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, ModelAttributetConstants.Y_CAPS_VAL);
			}
			return getDefaultLoginPage(false, session, model);
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

	/**
	 * @return captcha key This method fetches the captcha key from local.properties
	 */
	private String getCaptchaKey()
	{
		final String recaptchaKey = configurationService.getConfiguration().getString(
				ModelAttributetConstants.RECAPTCHA_PUBLIC_KEY_PROPERTY);

		return recaptchaKey;
	}

	public boolean isUserLoggedIn()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && !authentication.getName().equalsIgnoreCase(ModelAttributetConstants.ANONYMOUS)
				&& !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, RequestMappingUrlConstants.LINK_LOGIN))
		{
			httpSessionRequestCache.saveRequest(request, response);
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
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REGISTER, method = RequestMethod.POST)
	public String doRegister(@RequestHeader(value = ModelAttributetConstants.REFERER, required = false) final String referer,
			final ExtRegisterForm form, final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			//Fix for defect TISEE-3986 : handling special character like #
			final String password = java.net.URLDecoder.decode(request.getParameter("pwd"), "UTF-8");
			final String rePassword = java.net.URLDecoder.decode(request.getParameter("checkPwd"), "UTF-8");
			form.setPwd(password);
			form.setCheckPwd(rePassword);

			getRegisterPageValidator().validate(form, bindingResult);
			//return processRegisterUserRequestNew(referer, form, bindingResult, model, request, response, redirectModel);
			return processRegisterUserRequestNew(form, bindingResult, model, request, response, redirectModel);
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
	/*
	 * private String processRegisterUserRequestNew(final String referer, final ExtRegisterForm form, final BindingResult
	 * bindingResult, final Model model, final HttpServletRequest request, final HttpServletResponse response, final
	 * RedirectAttributes redirectModel) throws CMSItemNotFoundException
	 */
	private String processRegisterUserRequestNew(final ExtRegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			model.addAttribute(form);
			model.addAttribute(new LoginForm());
			model.addAttribute(new GuestForm());
			model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, ModelAttributetConstants.N_CAPS_VAL);
			GlobalMessages.addErrorMessage(model, MessageConstants.FORM_GLOBAL_ERROR);
			return handleRegistrationError(model);
		}

		final ExtRegisterData data = new ExtRegisterData();
		data.setLogin(form.getEmail().toLowerCase());
		data.setPassword(form.getPwd());
		data.setAffiliateId(form.getAffiliateId());
		try
		{
			if (getRegisterCustomerFacade().checkUniquenessOfEmail(data))
			{
				getRegisterCustomerFacade().register(data);
				// To avoid multiple time decoding of password containing '%' specially
				final String password = java.net.URLEncoder.encode(form.getPwd(), "UTF-8");
				form.setPwd(password);
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
		return REDIRECT_PREFIX + getSuccessRedirect(request, response);
	}

	/**
	 * @param form
	 * @param model
	 * @param bindingResult
	 *
	 */
	private void rePopulateLoginPageWithDuplicateUidErrorMsg(final Model model, final ExtRegisterForm form,
			final BindingResult bindingResult)
	{
		model.addAttribute(form);
		model.addAttribute(new LoginForm());
		model.addAttribute(new GuestForm());
		model.addAttribute(ModelAttributetConstants.IS_SIGN_IN_ACTIVE, ModelAttributetConstants.N_CAPS_VAL);
		bindingResult.rejectValue(ModelAttributetConstants.EMAIL, MessageConstants.REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE);
		GlobalMessages.addErrorMessage(model, MessageConstants.REGISTRATION_ERROR_ACCOUNT_EXISTS_TITLE);
	}


	/**
	 * @description this method is called for social login process
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_SOCIALLOGIN, method = RequestMethod.GET)
	public void doSocialLogin(final HttpServletRequest request, final HttpServletResponse response)
	{
		try
		{

			final GoogleAuthHelper helper = new GoogleAuthHelper();
			final FBConnection fBConnection = new FBConnection();
			request.getSession().setAttribute(ModelAttributetConstants.STATE, helper.getStateToken());
			response.setContentType(ModelAttributetConstants.TEXT_HTML);
			final PrintWriter pw = response.getWriter();
			pw.print(fBConnection.getFBAuthUrl() + ModelAttributetConstants.SEPARATOR_ANNOTATION + helper.buildLoginUrl());
			pw.close();

		}
		catch (final Exception e)
		{
			LOG.error(MessageConstants.EXCEPTION_IS + e);
		}

	}
}
