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
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.ForgetPasswordFacade;
import com.tisl.mpl.helper.MplUserHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ForgetPasswordService;
import com.tisl.mpl.util.ExceptionUtil;
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
	//	@Autowired
	//	private SessionService sessionService;
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	@Autowired
	MplUserHelper mplUserHelper;

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
				validateEmail(emailid);
				final URL requestUrl = new URL(request.getRequestURL().toString());
				final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
				//final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + ""; Do not add empty strings
				final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;
				final String securePasswordPath = MarketplacewebservicesConstants.FORGOTPASSWORD_URL
						+ MarketplacecommerceservicesConstants.LINK_PASSWORD_CHANGE;
				final String securePasswordUrl = baseUrl + securePasswordPath;
				forgetPasswordFacade.forgottenPasswordForEmail(emailid, securePasswordUrl);
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
}
