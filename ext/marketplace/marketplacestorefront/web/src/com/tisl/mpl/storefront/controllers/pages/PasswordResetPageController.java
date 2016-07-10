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

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.ForgottenPwdForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.core.Constants.USER;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.ForgetPasswordFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.ForgetPasswordService;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.MplUpdatePwdForm;
import com.tisl.mpl.storefront.web.forms.SendSmsOtpForm;
import com.tisl.mpl.storefront.web.forms.validator.MplEmailValidator;
import com.tisl.mpl.storefront.web.forms.validator.MplForgotPwdFormValidator;
import com.tisl.mpl.storefront.web.forms.validator.SendSmsOtpFormValidator;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Controller for the forgotten password pages. Supports requesting a password reset email as well as changing the
 * password once you have got the token that was sent via email.
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LINK_LOGIN_PW)
public class PasswordResetPageController extends AbstractPageController
{
	@SuppressWarnings("unused")
	private static final String REDIRECT_PWD_REQ_CONF = "redirect:/login/pw/request/external/conf";
	private static final String REDIRECT_LOGIN = "redirect:/login";
	private static final String REDIRECT_HOME = "redirect:/";
	private static final String UPDATE_PWD_CMS_PAGE = "updatePassword";
	private static final String OTP_CMS_PAGE = "otpValidation";
	private static final String BZ_ERROR_CMS_PAGE = "businessErrorFound";
	private static final String NBZ_ERROR_CMS_PAGE = "nonBusinessErrorFound";
	private static final String UTF = "UTF-8";

	@Resource(name = ModelAttributetConstants.SIMPLE_BREADCRUMB_BUILDER)
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	@Autowired
	private MplEmailValidator mplEmailValidator;
	@Autowired
	private ForgetPasswordFacade forgetPasswordFacade;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private MplForgotPwdFormValidator mplForgotPwdFormValidator;
	@Autowired
	private SendSMSFacade sendSMSFacade;
	@Autowired
	private SendSmsOtpFormValidator sendSmsOtpFormValidator;
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	//	@Autowired
	//	private MplCustomerProfileFacade mplCustomerProfileFacade;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ExtendedUserService extendedUserService;
	@Autowired
	UserService userService;
	private boolean validate = false;
	protected static final String SPRING_SECURITY_LAST_USERNAME = "SPRING_SECURITY_LAST_USERNAME";


	/**
	 * @description method is called to get PasswordRequest
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REQUEST, method = RequestMethod.GET)
	public String getPasswordRequest(final Model model, final HttpSession session) throws CMSItemNotFoundException
	{
		try
		{
			final ForgottenPwdForm forgottenPwdForm = new ForgottenPwdForm();
			final LoginForm loginForm = new LoginForm();
			final String password = loginForm.getJ_password();
			final String userName = (String) session.getAttribute(SPRING_SECURITY_LAST_USERNAME);

			if (StringUtils.isNotEmpty((String) sessionService
					.getAttribute(ModelAttributetConstants.LAST_USERNAME_WITH_ERROR_ATTEMPT)))
			{
				final String username = (String) sessionService
						.getAttribute(ModelAttributetConstants.LAST_USERNAME_WITH_ERROR_ATTEMPT);
				forgottenPwdForm.setEmail(username);
				model.addAttribute(forgottenPwdForm);

			}
			else
			{
				if (userName != null && password == null)
				{
					session.removeAttribute(SPRING_SECURITY_LAST_USERNAME);
					forgottenPwdForm.setEmail(userName);
				}
				model.addAttribute(new ForgottenPwdForm());
			}
			return ControllerConstants.Views.Fragments.Password.PasswordResetRequestPopup;
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
	}

	/**
	 *
	 * @param email
	 * @return boolean value
	 */
	public boolean checkExixtence(final String email)
	{
		boolean validate = false;
		if (StringUtils.isNotEmpty(email) && !(forgetPasswordService.getCustomer(email)).isEmpty())
		{
			validate = true;
		}
		return validate;
	}

	/**
	 *
	 * @param request
	 * @param forgottenPwdForm
	 * @param bindingResult
	 * @param response
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REQUEST_CONFIRMEMAIL, method = RequestMethod.GET)
	public String checkEmailClicked(@RequestParam("forgotPassword_email") final String forgotPasswordEmail,
			final HttpServletRequest request, @Valid final ForgottenPwdForm forgottenPwdForm, final BindingResult bindingResult,
			final HttpServletResponse response, final Model model) throws CMSItemNotFoundException, IOException, ServletException
	{
		try
		{
			if (null != forgotPasswordEmail)
			{
				final String emailId = forgotPasswordEmail;
				final boolean isExist = checkExixtence(emailId);
				final PrintWriter pw = response.getWriter();
				boolean isBlank = false;

				if (StringUtils.isEmpty(emailId))
				{
					isBlank = true;
				}
				else if (!mplEmailValidator.validateEmailAddress(emailId) || !mplEmailValidator.validDomain(emailId))
				{
					pw.print(ModelAttributetConstants.INVALID_EMAIL_FORMAT);
				}
				else if (!isExist)
				{
					pw.print(ModelAttributetConstants.INVALID_EMAIL);
				}
				else
				{

					final String returnSuccess = passwordRequest(ModelAttributetConstants.N_CAPS_VAL, emailId, bindingResult, request,
							model);
					pw.print(returnSuccess);
				}

				if (isBlank)
				{
					pw.print(ModelAttributetConstants.BLANK_OR_NULL_EMAIL);
				}
			}
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
		return null;
	}

	/**
	 *
	 * @param request
	 * @param forgottenPwdForm
	 * @param bindingResult
	 * @param response
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REQUEST_CONFIRMEMAIL_SMS, method = RequestMethod.GET)
	public String checkSMSClicked(final HttpServletRequest request, @Valid final ForgottenPwdForm forgottenPwdForm,
			final BindingResult bindingResult, final HttpServletResponse response, final Model model)
			throws CMSItemNotFoundException, IOException
	{
		try
		{
			if (null != request.getParameter(ModelAttributetConstants.FORGOTPASSWORD_EMAIL))
			{
				final String emailId = request.getParameter(ModelAttributetConstants.FORGOTPASSWORD_EMAIL);
				final boolean isExist = checkExixtence(emailId);
				final PrintWriter pw = response.getWriter();
				boolean isBlank = false;
				if (StringUtils.isEmpty(emailId))
				{
					isBlank = true;
				}
				else if (!mplEmailValidator.validateEmailAddress(emailId) || !mplEmailValidator.validDomain(emailId))
				{
					pw.print(ModelAttributetConstants.INVALID_EMAIL_FORMAT);
				}
				else if (!isExist)
				{
					pw.print(ModelAttributetConstants.INVALID_EMAIL);
				}
				else
				{
					passwordRequest(ModelAttributetConstants.Y_CAPS_VAL, emailId, bindingResult, request, model);
				}

				if (isBlank)
				{
					pw.print(ModelAttributetConstants.BLANK_OR_NULL_EMAIL);
				}
			}
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
		return null;
	}

	/**
	 * @description method is called to get PasswordRequest
	 * @param bindingResult
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REQUEST, method = RequestMethod.POST)
	public String passwordRequest(@RequestParam(ModelAttributetConstants.SMS) final String SMS, final String email,
			final BindingResult bindingResult, final HttpServletRequest request, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			sessionService.setAttribute(ModelAttributetConstants.SESSION_USER_EMAIL, email.trim().toLowerCase());
			if (!(StringUtils.isEmpty(SMS)) && !(StringUtils.isBlank(SMS)) && SMS.equals(ModelAttributetConstants.Y_CAPS_VAL))
			{
				final String currentUserToken = forgetPasswordFacade.forgottenPassword(email);
				String mobileNumber = null;
				mobileNumber = forgetPasswordFacade.validateMobile(email);
				if (StringUtils.isEmpty(mobileNumber))
				{

					final String messageKey = MessageConstants.ACCOUNT_CONFIRMATION_FORGOTTEN_PASSWORD_MOBILE;

					return frontEndErrorHelper.callBusinessError(model, messageKey);
				}
				else
				{
					model.addAttribute(ModelAttributetConstants.VALUE, ModelAttributetConstants.VALUE_MS);

					final String otp = forgetPasswordFacade.generateOTPNumber(email.trim().toLowerCase());
					storeCmsPageInModel(model, getContentPageForLabelOrId(null));
					setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
					model.addAttribute(WebConstants.BREADCRUMBS_KEY,
							resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.FORGOTTEN_PWD_TITLE));
					sessionService.setAttribute(ModelAttributetConstants.SESSION_USER_EMAIL, email.trim().toLowerCase());

					sendSMSFacade.sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID,
							MarketplacecommerceservicesConstants.SMS_MESSAGE_FORGOT_PWD.replace(
									MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, otp), mobileNumber);
					if (null != currentUserToken)
					{
						sessionService.setAttribute(ModelAttributetConstants.SESSION_USER_TOKEN, currentUserToken);
					}
					return REDIRECT_PREFIX + RequestMappingUrlConstants.LINK_LOGIN_PW + RequestMappingUrlConstants.LINK_OTPVALIDATION;
				}
			}

			final URL requestUrl = new URL(request.getRequestURL().toString());


			final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
			final StringBuilder builder = new StringBuilder(150);
			builder.append(requestUrl.getProtocol());
			builder.append("://");
			builder.append(requestUrl.getHost());
			builder.append(portString);
			final String baseUrl = builder.toString();
			//			final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + "";

			final String securePasswordPath = request.getContextPath() + RequestMappingUrlConstants.LINK_LOGIN_PW
					+ RequestMappingUrlConstants.LINK_CHANGE;

			final String securePasswordUrl = baseUrl + securePasswordPath;

			forgetPasswordFacade.forgottenPasswordForEmail(email, securePasswordUrl);
		}
		catch (final UnknownIdentifierException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			callNonBusinessError(model, MessageConstants.ACCOUNT_CONFIRMATION_INVALID_USER);
			return ModelAttributetConstants.FAILURE;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			return ModelAttributetConstants.FAILURE;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS1);
			return ModelAttributetConstants.FAILURE;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			callBusinessError(model, MessageConstants.ACCOUNT_CONFIRMATION_FORGOTTEN_PASSWORD_MOBILE);
			return ModelAttributetConstants.FAILURE;
		}
		return ModelAttributetConstants.SUCCESS;

	}



	/**
	 * @description method is called to get ExternalPassword
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REQUEST_EXTERNAL, method = RequestMethod.GET)
	public String getExternalPasswordRequest(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			model.addAttribute(new ForgottenPwdForm());
			storeCmsPageInModel(model, getContentPageForLabelOrId(null));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.FORGOTTEN_PWD_TITLE));

			return ControllerConstants.Views.Pages.Password.PasswordResetRequest;
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
	}

	/**
	 * @description method is called to get ExternalPasswordConf
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REQUEST_EXTERNAL_CONF, method = RequestMethod.GET)
	public String getExternalPasswordRequestConf(final Model model) throws CMSItemNotFoundException
	{
		try
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(null));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.FORGOTTEN_PWD_TITLE));

			return ControllerConstants.Views.Pages.Password.PasswordResetRequestConfirmation;
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
	}

	/**
	 * @description method is called to get ExternalPassword Request
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_REQUEST_EXTERNAL, method = RequestMethod.POST)
	public String externalPasswordRequest(@Valid final ForgottenPwdForm form, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(null));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(null));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.FORGOTTEN_PWD_TITLE));
		final String securePasswordUrl = request.getContextPath() + RequestMappingUrlConstants.LINK_LOGIN_PW
				+ RequestMappingUrlConstants.LINK_CHANGE;


		if (bindingResult.hasErrors())
		{
			return ControllerConstants.Views.Pages.Password.PasswordResetRequest;
		}
		else
		{
			try
			{
				forgetPasswordFacade.forgottenPasswordForEmail(form.getEmail(), securePasswordUrl);
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
						MessageConstants.ACCOUNT_CONFIRMATION_FORGOTTEN_PASSWORD_LINK_SENT);

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
			return REDIRECT_PWD_REQ_CONF;
		}
	}

	/**
	 * @description method is called to Validate the OTP Number
	 * @param sendSmsOtpForm
	 * @param bindingResult
	 * @param model
	 * @param errors
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws UnsupportedEncodingException
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_OTPVALIDATION, method = RequestMethod.POST)
	public String validateOTP(@Valid final SendSmsOtpForm sendSmsOtpForm, final BindingResult bindingResult, final Model model,
			final Errors errors, final RedirectAttributes redirectModel) throws CMSItemNotFoundException,
			UnsupportedEncodingException
	{
		try
		{
			sendSmsOtpFormValidator.validate(sendSmsOtpForm, bindingResult);
			if (bindingResult.hasErrors())
			{
				GlobalMessages.addErrorMessage(model, MessageConstants.PROFILE_OTPNUMBER_INVALID);
			}
			else
			{
				if (null != sessionService.getAttribute(ModelAttributetConstants.SESSION_USER_EMAIL))
				{
					final String emailId = sessionService.getAttribute(ModelAttributetConstants.SESSION_USER_EMAIL);
					validate = forgetPasswordFacade.validateOTP(emailId, sendSmsOtpForm.getOTPNumber(), OTPTypeEnum.FORGOT_PASSWORD,
							600000);
					if (validate)
					{
						if (null != sessionService.getAttribute(ModelAttributetConstants.SESSION_USER_TOKEN))
						{
							final String token = sessionService.getAttribute(ModelAttributetConstants.SESSION_USER_TOKEN);

							return REDIRECT_PREFIX + MessageConstants.LOGIN_PW_CHANGE + MessageConstants.TOKEN
									+ getURLEncodedToken(token);
						}
						else
						{
							final SendSmsOtpForm sendSmsOtpFormNew = new SendSmsOtpForm();
							model.addAttribute(ModelAttributetConstants.SEND_SMS_OTP_FORM, sendSmsOtpFormNew);
							storeCmsPageInModel(model, getContentPageForLabelOrId(OTP_CMS_PAGE));
							setUpMetaDataForContentPage(model, getContentPageForLabelOrId(OTP_CMS_PAGE));
							model.addAttribute(WebConstants.BREADCRUMBS_KEY,
									resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.VALIDATE_OTP_TITLE));
							model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);

							return getViewForPage(model);
						}
					}
					else
					{
						GlobalMessages.addErrorMessage(model, MessageConstants.PROFILE_OTPNUMBER_INVALID);
					}
				}
			}
			final SendSmsOtpForm sendSmsOtpFormNew = new SendSmsOtpForm();
			model.addAttribute(ModelAttributetConstants.SEND_SMS_OTP_FORM, sendSmsOtpFormNew);
			storeCmsPageInModel(model, getContentPageForLabelOrId(OTP_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(OTP_CMS_PAGE));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.VALIDATE_OTP_TITLE));

			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			return getViewForPage(model);
		}
		catch (final UnsupportedEncodingException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0013));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
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
	}

	/**
	 * @description method is called to get the OTP Number
	 * @param model
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_PASSWORDOTP, method = RequestMethod.GET)
	public String getOTP(final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			final SendSmsOtpForm sendSmsOtpForm = new SendSmsOtpForm();
			model.addAttribute(ModelAttributetConstants.SEND_SMS_OTP_FORM, sendSmsOtpForm);
			storeCmsPageInModel(model, getContentPageForLabelOrId(OTP_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(OTP_CMS_PAGE));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.VALIDATE_OTP_TITLE));

			model.addAttribute(ModelAttributetConstants.METAROBOTS, ModelAttributetConstants.NOINDEX_NOFOLLOW);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
					MessageConstants.ACCOUNT_CONFIRMATION_OTP_GENERATED);

			return getViewForPage(model);
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
	}

	/**
	 * @description method is called to get ChangePassword Request
	 * @param token
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = RequestMappingUrlConstants.LINK_CHANGE, method = RequestMethod.GET)
	public String getChangePassword(@RequestParam(required = false) final String token,
			@RequestParam(value = ModelAttributetConstants.PARAM, required = false) final String param, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			if (StringUtils.isBlank(token))
			{
				return REDIRECT_HOME;
			}
			final UserModel user = userService.getCurrentUser();

			if (null != user.getName() && (!user.getName().equalsIgnoreCase(USER.ANONYMOUS_CUSTOMER)))
			{
				return REDIRECT_HOME;
			}
			final MplUpdatePwdForm form = new MplUpdatePwdForm();
			form.setToken(token);
			model.addAttribute(form);
			if (StringUtils.isNotEmpty(param))
			{
				model.addAttribute(ModelAttributetConstants.TOKEN, token);
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.FAILURE);
			}
			else
			{
				model.addAttribute(ModelAttributetConstants.RESULT, ModelAttributetConstants.EMPTY);
			}
			storeCmsPageInModel(model, getContentPageForLabelOrId(UPDATE_PWD_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(UPDATE_PWD_CMS_PAGE));
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.UPDATEPWD_TITLE));

			return ControllerConstants.Views.Pages.Password.PasswordResetChangePage;
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
	}

	/**
	 * @description method is called to Change the Password
	 * @param form
	 * @param bindingResult
	 * @param model
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings("boxing")
	@RequestMapping(value = RequestMappingUrlConstants.LINK_CHANGE, method = RequestMethod.POST)
	public String changePassword(@Valid final MplUpdatePwdForm form, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		try
		{
			//
			final String newPassword = java.net.URLDecoder.decode(form.getPwd(), UTF);
			final String confirmNewPassword = java.net.URLDecoder.decode(form.getCheckPwd(), UTF);
			form.setPwd(newPassword);
			form.setCheckPwd(confirmNewPassword);
		}
		catch (final EtailNonBusinessExceptions | UnsupportedEncodingException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
		}



		mplForgotPwdFormValidator.validate(form, bindingResult);

		if (bindingResult.hasErrors())
		{
			prepareErrorMessage(model, UPDATE_PWD_CMS_PAGE);
			return ControllerConstants.Views.Pages.Password.PasswordResetChangePage;
		}

		if (!StringUtils.isBlank(form.getToken()))
		{
			try
			{
				if (null == sessionService.getAttribute(ModelAttributetConstants.SESSION_USER_EMAIL))
				{
					final CustomerModel currentUser = extendedUserService.getUserForCustomerUid(form.getCustomerUniqueID());
					if (null != currentUser)
					{
						final String emailId = currentUser.getOriginalUid();
						if (forgetPasswordFacade.checkPasswordEncodedFormat(emailId, form.getPwd()))
						{
							final String originalToken = forgetPasswordFacade.getOriginalToken(form.getToken(), emailId);
							forgetPasswordFacade.updatePassword(originalToken, form.getPwd());
							//							mplCustomerProfileFacade.sendEmailForChangePassword(currentUser);
							final String specificUrl = RequestMappingUrlConstants.LINK_MY_ACCOUNT
									+ RequestMappingUrlConstants.LINK_UPDATE_PROFILE;
							final String profileUpdateUrl = urlForEmailContext(request, specificUrl);
							final List<String> updatedDetailList = new ArrayList<String>();
							updatedDetailList.add(MarketplacecommerceservicesConstants.PASSWORD_suffix);
							forgetPasswordFacade.sendEmailForUpdateCustomerProfile(currentUser, updatedDetailList, profileUpdateUrl);
							GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
									MessageConstants.ACCOUNT_CONFIRMATION_PASSWORD_UPDATED);

						}
						else
						{
							GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
									MessageConstants.ACCOUNT_CONFIRMATION_PASSWORD_ENTERUNIQUEPASSWORD);
							try
							{
								return REDIRECT_PREFIX + MessageConstants.LOGIN_PW_CHANGE + MessageConstants.TOKEN
										+ getURLEncodedToken(form.getToken()) + ModelAttributetConstants.AMPARSAND
										+ ModelAttributetConstants.PARAM + ModelAttributetConstants.EQUALS
										+ ModelAttributetConstants.FAILURE;
							}
							catch (final UnsupportedEncodingException e)
							{
								ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
										MarketplacecommerceservicesConstants.E0013));
								return REDIRECT_LOGIN;
							}
						}

					}
				}


				else if (null != sessionService.getAttribute(ModelAttributetConstants.SESSION_USER_EMAIL))
				{

					final String emailId = sessionService.getAttribute(ModelAttributetConstants.SESSION_USER_EMAIL);
					if (forgetPasswordFacade.checkPasswordEncodedFormat(emailId, form.getPwd()))
					{
						final String originalToken = forgetPasswordFacade.getOriginalToken(form.getToken(), emailId);
						forgetPasswordFacade.updatePassword(originalToken, form.getPwd());
						CustomerModel currentUser = modelService.create(CustomerModel.class);
						currentUser = extendedUserService.getUserForUid(emailId);
						//						mplCustomerProfileFacade.sendEmailForChangePassword(currentUser);
						final String specificUrl = RequestMappingUrlConstants.LINK_MY_ACCOUNT
								+ RequestMappingUrlConstants.LINK_UPDATE_PROFILE;
						final String profileUpdateUrl = urlForEmailContext(request, specificUrl);
						final List<String> updatedDetailList = new ArrayList<String>();
						updatedDetailList.add(MarketplacecommerceservicesConstants.PASSWORD_suffix);
						forgetPasswordFacade.sendEmailForUpdateCustomerProfile(currentUser, updatedDetailList, profileUpdateUrl);
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
								MessageConstants.ACCOUNT_CONFIRMATION_PASSWORD_UPDATED);
						//						sessionService.setAttribute(ModelAttributetConstants.FORGET_PASSWORD_UPDATE_SUCCESS,
						//								ModelAttributetConstants.TRUE);
					}
					else
					{
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
								MessageConstants.ACCOUNT_CONFIRMATION_PASSWORD_ENTERUNIQUEPASSWORD);
						try
						{
							return REDIRECT_PREFIX + MessageConstants.LOGIN_PW_CHANGE + MessageConstants.TOKEN
									+ getURLEncodedToken(form.getToken()) + ModelAttributetConstants.AMPARSAND
									+ ModelAttributetConstants.PARAM + ModelAttributetConstants.EQUALS + ModelAttributetConstants.FAILURE;
						}
						catch (final UnsupportedEncodingException e)
						{
							ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
									MarketplacecommerceservicesConstants.E0013));
							return REDIRECT_LOGIN;
						}
					}
				}

				else
				{
					return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
				}
			}

			catch (final TokenInvalidatedException e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
						MarketplacecommerceservicesConstants.B0011));
				return frontEndErrorHelper.callNonBusinessError(model, MessageConstants.UPDATEPWD_TOKEN_INVALID);
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
		}
		return REDIRECT_LOGIN;
	}

	/**
	 * @description method is called to generate update profileURL
	 * @param request
	 * @param specificUrl
	 * @throws CMSItemNotFoundException
	 */
	public String urlForEmailContext(final HttpServletRequest request, final String specificUrl) throws CMSItemNotFoundException
	{
		URL requestUrl;
		final Model model = null;
		String profileUpdateUrl = ModelAttributetConstants.EMPTY;
		try
		{
			requestUrl = new URL(request.getRequestURL().toString());
			final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
			//final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + ""; Do not add empty strings
			final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;

			final String profileUpdatePath = request.getContextPath() + specificUrl;
			profileUpdateUrl = baseUrl + profileUpdatePath;
		}
		catch (final MalformedURLException e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0016));
			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			return ModelAttributetConstants.FAILURE;
		}
		return profileUpdateUrl;
	}

	/**
	 * @description Prepares the view to display an error message
	 * @throws CMSItemNotFoundException
	 */
	protected void prepareErrorMessage(final Model model, final String page) throws CMSItemNotFoundException
	{
		GlobalMessages.addErrorMessage(model, ModelAttributetConstants.FORM_GLOBAL_ERROR);
		storeCmsPageInModel(model, getContentPageForLabelOrId(page));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(page));
	}

	/**
	 * @description method is called to get URLEncodedToken
	 * @param token
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String getURLEncodedToken(final String token) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(token, MessageConstants.UTF_8);
	}

	/**
	 * @description method is called to call NonBusinessError Page
	 * @param model
	 * @param messageKey
	 * @throws CMSItemNotFoundException
	 */
	private void callNonBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.BREADCRUMB_NOT_FOUND));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, ModelAttributetConstants.NON_BUSINESS_ERROR);
	}

	/**
	 * @description method is called to call BusinessError Page
	 * @param model
	 * @param messageKey
	 * @throws CMSItemNotFoundException
	 */
	private void callBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(BZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs(ModelAttributetConstants.BREADCRUMB_NOT_FOUND));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, ModelAttributetConstants.BUSINESS_ERROR);
	}
}
