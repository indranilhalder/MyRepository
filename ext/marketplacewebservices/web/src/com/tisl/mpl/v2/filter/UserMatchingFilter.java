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
package com.tisl.mpl.v2.filter;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.CustomerOldEmailDetailsModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * Filter that puts user from the requested url into the session.
 */
public class UserMatchingFilter extends AbstractUrlMatchingFilter
{
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String ROLE_CUSTOMERGROUP = "ROLE_CUSTOMERGROUP";
	public static final String ROLE_CUSTOMERMANAGERGROUP = "ROLE_CUSTOMERMANAGERGROUP";
	public static final String ROLE_TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String CURRENT_USER = "current";
	private static final String ANONYMOUS_USER = "anonymous";
	private static final String REGISTRATION = "registration";
	private static final String SOCIAL_MEDIA_REGISTRATION = "socialMediaRegistration";
	private String regexp;
	private UserService userService;
	@Autowired
	private ExtendedUserService extUserService;

	/**
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}

	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Autowired
	private ModelService modelService;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		final Authentication auth = getAuth();

		String userID = getValue(request, regexp);
		if (null != getValue(request, regexp) && !getValue(request, regexp).isEmpty())
		{
			userID = getValue(request, regexp).toLowerCase();
		}
		//Mobile registration
		boolean isRegistrationorSocialMediaReg = false;
		if (!StringUtils.isEmpty(userID)
				&& (StringUtils.equalsIgnoreCase(REGISTRATION, userID) || (StringUtils.equalsIgnoreCase(SOCIAL_MEDIA_REGISTRATION,
						userID))))
		{
			isRegistrationorSocialMediaReg = true;
		}
		//Mobile registration
		if (userID == null || isRegistrationorSocialMediaReg)
		{
			//Mobile registration
			if (hasRole(ROLE_CUSTOMERGROUP, auth) || hasRole(ROLE_CUSTOMERMANAGERGROUP, auth))
			{
				setCurrentUser((String) auth.getPrincipal());
			}
			else
			{
				// fallback to anonymous
				setCurrentUser(userService.getAnonymousUser());
			}
		}
		else if (userID.equals(ANONYMOUS_USER))
		{
			setCurrentUser(userService.getAnonymousUser());
		}
		else if (hasRole(ROLE_TRUSTED_CLIENT, auth) || hasRole(ROLE_CUSTOMERMANAGERGROUP, auth))
		{
			setCurrentUser(userID);
		}
		else if (hasRole(ROLE_CUSTOMERGROUP, auth))
		{
			if (userID.equals(CURRENT_USER) || userID.equals(auth.getPrincipal()))
			{
				setCurrentUser((String) auth.getPrincipal());
			}
			else
			{
				throw new AccessDeniedException("Access is denied");
			}
		}
		else
		{
			// could not match any authorized role
			throw new AccessDeniedException("Access is denied");
		}

		filterChain.doFilter(request, response);
	}

	protected Authentication getAuth()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected String getRegexp()
	{
		return regexp;
	}

	@Required
	public void setRegexp(final String regexp)
	{
		this.regexp = regexp;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected boolean hasRole(final String role, final Authentication auth)
	{
		for (final GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
	}

	protected void setCurrentUser(final String uid)
	{
		UserModel userModel = null;
		if (null != uid && !uid.isEmpty())
		{
			try
			{
				userModel = userService.getUserForUID(uid.toLowerCase());
			}
			catch (final EtailNonBusinessExceptions e)
			{
				if (null != e.getErrorCode() && e.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.E0000))
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(e);
					detailsForOldOriginalUid(uid.toLowerCase());
				}

			}
			catch (final EtailBusinessExceptions e)
			{
				if (null != e.getErrorCode() && e.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.B0006))
				{
					detailsForOldOriginalUid(uid.toLowerCase());
				}
			}
		}
		if (null != userModel)
		{
			userService.setCurrentUser(userModel);
		}
		else
		{
			if (null != uid && !uid.isEmpty())
			{
				detailsForOldOriginalUid(uid.toLowerCase());
			}
		}
	}

	/**
	 * Fetching details for old email id
	 *
	 * @param uid
	 */
	private void detailsForOldOriginalUid(final String uid)
	{
		CustomerOldEmailDetailsModel custModel = getModelService().create(CustomerOldEmailDetailsModel.class);
		custModel = extUserService.getUserForOldOriginalUid(StringUtils.lowerCase(uid.toLowerCase()));
		if (null != custModel && null != custModel.getIsEmailChanged() && custModel.getIsEmailChanged().booleanValue())
		{
			throw new AccessDeniedException("Email id has been changed for this user.");
		}
	}

	protected void setCurrentUser(final UserModel user)
	{
		userService.setCurrentUser(user);
	}
}
