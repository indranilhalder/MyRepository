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
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;

import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.ForgetPasswordFacade;
import com.tisl.mpl.helper.MplUserHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ForgetPasswordService;
import com.tisl.mpl.service.MplMobileUserService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.MplRegistrationResultWsDto;
import com.tisl.mpl.wsdto.MplUserResultWsDto;
import com.tisl.mpl.wsdto.UserResultWsDto;


@Controller
@RequestMapping(value = "/{baseSiteId}/forgottenpasswordtokens")
@CacheControl(directive = CacheControlDirective.NO_STORE)
public class ForgottenPasswordsController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(UsersController.class);
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Autowired
	private ForgetPasswordFacade forgetPasswordFacade;
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	@Autowired
	MplUserHelper mplUserHelper;
	@Resource
	private MplMobileUserService mobileUserService;
	@Resource
	private UserService userService;
	@Resource
	private CustomerAccountService customerAccountService;


	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String ROLE_CLIENT = "ROLE_CLIENT";
	private static final String APPLICATION_TYPE = "application/json";

	/**
	 * Generates a token to restore customer's forgotten password.
	 *
	 * @formparam userId Customer's user id. Customer user id is case insensitive.
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void restorePassword(@RequestParam final String userId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("restorePassword: uid=" + sanitize(userId));
		}
		customerFacade.forgottenPassword(userId);
	}

	/**
	 * @description method is called to change the password through Email
	 * @param emailid
	 * @param fields
	 * @return UserResultWsDto
	 */
	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/forgotPasswordforEmail", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public UserResultWsDto forgotPassword(@RequestParam final String emailid, final String fields, final HttpServletRequest request)
	{
		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		String message = "";
		try
		{
			if (null == emailid)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			else
			{
				final String emailidLwCase = emailid.toLowerCase(); //INC144318796
				validateEmail(emailidLwCase);
				final URL requestUrl = new URL(request.getRequestURL().toString());
				final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
				//final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + ""; Do not add empty strings
				final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;
				final String securePasswordUrl = baseUrl + MarketplacecommerceservicesConstants.LINK_PASSWORD_CHANGE;
				forgetPasswordFacade.forgottenPasswordForEmail(emailidLwCase, securePasswordUrl, Boolean.TRUE);
				userResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				userResultWsDto.setError(e.getErrorMessage());
			}
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				userResultWsDto.setError(e.getErrorMessage());
			}
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final MalformedURLException e)
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			message = Localization.getLocalizedString(MarketplacecommerceservicesConstants.EMAIL_NOT_FOUND);
			userResultWsDto.setError(message);
		}
		return userResultWsDto;
	}

	/**
	 * all email related validations used by login and registration
	 *
	 * @param login
	 * @return MplUserResultWsDto
	 */
	public UserResultWsDto validateEmail(final String login) throws EtailBusinessExceptions
	{
		final UserResultWsDto userResultWsDto = new UserResultWsDto();
		if (StringUtils.isEmpty(login))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9012);
		}

		if ((StringUtils.length(login) > MplUserHelper.MAX_EMAIL_LENGTH || !mplUserHelper.validateEmailAddress(login)))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9011);
		}
		else if (!mplUserHelper.validDomain(login))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9012);
		}
		else if ((forgetPasswordService.getCustomer(login)).isEmpty())
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9036);
		}
		else
		{
			userResultWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		return userResultWsDto;
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/customerForgotPassword", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public MplRegistrationResultWsDto customerForgotPassword(@RequestParam final String username,
			@RequestParam final int platformNumber, @RequestParam final String isPwa) throws Exception
	{
		MplRegistrationResultWsDto resultWsDto = new MplRegistrationResultWsDto();
		try
		{
			resultWsDto = mobileUserService.forgotPasswordOtp(username, platformNumber);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				resultWsDto.setMessage(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultWsDto.setErrorCode(e.getErrorCode());
			}
			resultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				resultWsDto.setMessage(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultWsDto.setErrorCode(e.getErrorCode());
			}
			resultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return resultWsDto;
	}

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(value = "/forgotPasswordOTPVerification", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public MplRegistrationResultWsDto forgotPasswordOTPVerification(@RequestParam final String username,
			@RequestParam final int platformNumber, @RequestParam final String otp, @RequestParam final String isPwa)
			throws Exception
	{
		final MplRegistrationResultWsDto resultWsDto = new MplRegistrationResultWsDto();
		boolean validOtpFlag = false;
		try
		{
			validOtpFlag = mobileUserService.validateOtpForRegistration(username, otp, OTPTypeEnum.FORGOT_PASSWORD);
			if (validOtpFlag)
			{
				resultWsDto.setMessage("OTP verified. Please reset your password");
				resultWsDto.setStatus("SUCCESS");

			}
			else
			{
				resultWsDto.setMessage("Incorrect OTP. Please try again");
				resultWsDto.setStatus("FAILURE");
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				resultWsDto.setMessage(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultWsDto.setErrorCode(e.getErrorCode());
			}
			resultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				resultWsDto.setMessage(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				resultWsDto.setErrorCode(e.getErrorCode());
			}
			resultWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return resultWsDto;
	}

	@Secured(
	{ ROLE_CLIENT, CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{userId}/resetCustomerPassword", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public UserResultWsDto resetCustomerPassword(@PathVariable final String userId, @RequestParam final String old,
			@RequestParam final String newPassword) throws RequestParameterException,
			de.hybris.platform.commerceservices.customer.PasswordMismatchException
	{
		final UserResultWsDto result = new UserResultWsDto();
		final MplUserResultWsDto validated = new MplUserResultWsDto();
		try
		{
			final String userIdLwCase = userId.toLowerCase();
			//		validated = mplUserHelper.validateRegistrationData(userIdLwCase, newPassword);//to-do
			if (null != validated.getStatus()
					&& validated.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.ERROR_FLAG))
			{
				//throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9010);
			}
			else
			{
				//				if (containsRole(auth, TRUSTED_CLIENT) || containsRole(auth, CUSTOMERMANAGER))
				//				{
				//					extUserService.setPassword(userIdLwCase, newPassword);
				//				}
				//				else
				//				{
				final UserModel user = userService.getCurrentUser();
				try
				{
					customerAccountService.changePassword(user, old, newPassword);
				}
				catch (final Exception e)
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0017);
				}
				//}
				//				final CustomerModel currentUser = null;
				//				try
				//				{
				//					if (null != userService.getCurrentUser())
				//					{
				//						currentUser = (CustomerModel) userService.getCurrentUser();
				//					}
				//					if (null != currentUser)
				//					{
				//						//						mplCustomerProfileFacade.sendEmailForChangePassword(currentUser);
				//						//
				//						final String specificUrl = MarketplacecommerceservicesConstants.LINK_MY_ACCOUNT
				//								+ MarketplacecommerceservicesConstants.LINK_UPDATE_PROFILE;
				//						final String profileUpdateUrl = urlForMobileEmailContext(request, specificUrl);
				//						final List<String> updatedDetailList = new ArrayList<String>();
				//						updatedDetailList.add(MarketplacecommerceservicesConstants.PASSWORD_suffix);
				//						mplCustomerProfileFacade.sendEmailForUpdateCustomerProfile(updatedDetailList, profileUpdateUrl);
				//					}
				//				}
				//				catch (final Exception e)
				//				{
				//					LOG.error("*************** Exception in sending mail after change password MOBILE ******************* " + e);
				//				}
				result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				result.setMessage("Password has been updated successfuly");
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}
}
