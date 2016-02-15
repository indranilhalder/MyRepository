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
package com.tisl.mpl.storefront.security;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;
import de.hybris.platform.spring.security.CoreUserDetails;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * Derived authentication provider supporting additional authentication checks. See
 * {@link de.hybris.platform.spring.security.RejectUserPreAuthenticationChecks}.
 *
 * <ul>
 * <li>prevent login without password for users created via CSCockpit</li>
 * <li>prevent login as user in group admingroup</li>
 * </ul>
 *
 * any login as admin disables SearchRestrictions and therefore no page can be viewed correctly
 */
public class AcceleratorAuthenticationProvider extends CoreAuthenticationProvider
{
	private static final Logger LOG = Logger.getLogger(AcceleratorAuthenticationProvider.class);
	private static final String ROLE_ADMIN_GROUP = "ROLE_" + Constants.USER.ADMIN_USERGROUP.toUpperCase();

	private static final String CORE_AUTHENTICATION_PROVIDER = "CoreAuthenticationProvider.badCredentials";
	private static final String BAD_CREDENTIAL = "Bad credentials";


	private BruteForceAttackCounter bruteForceAttackCounter;
	private UserService userService;
	private ModelService modelService;
	private GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ROLE_ADMIN_GROUP);
	//private CartService cartService;
	private final UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

	@Autowired
	ExtendedUserService extUserService;

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException
	{
		if (Registry.hasCurrentTenant()/* && JaloConnection.getInstance().isSystemInitialized() */)
		{
			final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
			UserDetails userDetails = null;

			//This method was added to check if the temporary password of the customer has expired or not(For Cscockpit Customers)
			checkTemporaryPasswordExpiry(username);

			/*
			 * if (getBruteForceAttackCounter().isAttack(username)) {
			 */
			try
			{
				userDetails = retrieveUser(username);
			}
			catch (final UsernameNotFoundException notFound)
			{
				throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER, BAD_CREDENTIAL), notFound);
			}
			getPreAuthenticationChecks().check(userDetails);
			final UserModel userModel = getUserService().getUserForUID(StringUtils.lowerCase(username));
			final User user = modelService.getSource(userModel);

			// FORM based check
			Object credential = authentication.getCredentials();
			//Fix for defect TISEE-3986 : handling special character like #
			try
			{
				credential = java.net.URLDecoder.decode(credential.toString(), "UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				LOG.error("Exception occured while decoding the password");
			}
			if (credential instanceof String)
			{
				if (!user.checkPassword((String) credential))
				{
					throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER, BAD_CREDENTIAL));
				}
			}
			else if (credential instanceof LoginToken)
			{
				if (!user.checkPassword((LoginToken) credential))
				{
					throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER, BAD_CREDENTIAL));
				}
			}
			else
			{
				throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER, BAD_CREDENTIAL));
			}
			postAuthenticationChecks.check(userDetails);


			// finally, set user in session
			JaloSession.getCurrentSession().setUser(user);
			//}
			//	checkCartForUser(username);
			return createSuccessAuthentication(authentication, userDetails);

		}
		else
		{
			return createSuccessAuthentication(//
					authentication, //
					new CoreUserDetails("systemNotInitialized", "systemNotInitialized", true, false, true, true,
							Collections.EMPTY_LIST, null));
		}



		//	return super.authenticate(authentication);

	}

	/**
	 * @param username
	 */
	private void checkTemporaryPasswordExpiry(final String username)
	{
		// YTODO Auto-generated method stub
		if (username != null && !username.isEmpty())
		{
			final CustomerModel customer = extUserService.getUserForUid(username);

			if (customer != null)
			{

				if (customer.getIsTemporaryPasswordChanged() != null)
				{
					if (customer.getIsTemporaryPasswordChanged().equals(Boolean.FALSE))
					{
						if (GenericUtilityMethods.daysBetweenPresentDateAndGivenDate(customer.getCreationtime()) > 14)
						{
							LOG.info("Your password has expired");

							throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER, BAD_CREDENTIAL));
						}

					}
				}
			}
		}

	}


	/*
	*//**
	   * check if the user of the cart matches the current user and if the user is not anonymous. If otherwise, remove
	   * delete the session cart as it might be stolen / from another user
	   *
	   * @param username
	   *           the username of the current user
	   */
	/*
	 * public void checkCartForUser(final String username) { // check if the user of the cart matches the current user
	 * and if the // user is not anonymous. If otherwise, remove delete the session cart as it might // be stolen / from
	 * another user final String sessionCartUserId = getCartService().getSessionCart().getUser().getUid();
	 *
	 * if (!username.equals(sessionCartUserId) && !sessionCartUserId.equals(userService.getAnonymousUser().getUid())) {
	 * getCartService().setSessionCart(null); } }
	 */


	/**
	 * @see de.hybris.platform.spring.security.CoreAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails,
	 *      org.springframework.security.authentication.AbstractAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(final UserDetails details, final AbstractAuthenticationToken authentication)
			throws AuthenticationException
	{
		super.additionalAuthenticationChecks(details, authentication);

		// Check if user has supplied no password
		if (StringUtils.isEmpty((String) authentication.getCredentials()))
		{
			throw new BadCredentialsException("Login without password");
		}

		// Check if the user is in role admingroup
		if (getAdminAuthority() != null && details.getAuthorities().contains(getAdminAuthority()))
		{
			throw new LockedException("Login attempt as " + Constants.USER.ADMIN_USERGROUP + " is rejected");
		}
	}

	/**
	 * @param adminGroup
	 *           the adminGroup to set
	 */
	public void setAdminGroup(final String adminGroup)
	{
		if (StringUtils.isBlank(adminGroup))
		{
			adminAuthority = null;
		}
		else
		{
			adminAuthority = new SimpleGrantedAuthority(adminGroup);
		}
	}

	protected GrantedAuthority getAdminAuthority()
	{
		return adminAuthority;
	}

	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	@Required
	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
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

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	private class DefaultPostAuthenticationChecks implements UserDetailsChecker
	{
		@Override
		public void check(final UserDetails user)
		{
			if (!user.isCredentialsNonExpired())
			{
				throw new CredentialsExpiredException(
						messages.getMessage("CoreAuthenticationProvider.credentialsExpired", "User credentials have expired"), user);
			}
		}
	}

}
