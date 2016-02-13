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
package com.tisl.mpl.authentication;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;


//Mobile Registration

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
public class ExtCoreAuthenticationProvider extends CoreAuthenticationProvider
{
	private static final String ROLE_ADMIN_GROUP = "ROLE_" + Constants.USER.ADMIN_USERGROUP.toUpperCase();
	private static final String NO_PRINICIPAL = "NONE_PROVIDED";
	private static final Logger LOG = Logger.getLogger(ExtCoreAuthenticationProvider.class);

	private UserService userService;
	private ModelService modelService;
	private GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ROLE_ADMIN_GROUP);
	private final UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();

	@Autowired
	private ExtendedUserService extUserService;

	/*
	 * To authenticate users
	 *
	 * @see de.hybris.platform.spring.security.CoreAuthenticationProvider#authenticate(org.springframework.security.core.
	 * Authentication)
	 */
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException
	{
		if (Registry.hasCurrentTenant()/* && JaloConnection.getInstance().isSystemInitialized() */)
		{
			final String userName = (authentication.getPrincipal() == null) ? NO_PRINICIPAL : authentication.getName();
			boolean isSocialMedia = false;
			String isSocialMediaInput = null;

			if (null != authentication.getDetails())
			{
				final HashMap<String, String> authDetails = (HashMap<String, String>) authentication.getDetails();
				if (null != authDetails && !authDetails.isEmpty())
				{
					for (final Map.Entry<String, String> entry : authDetails.entrySet())
					{
						if (null != entry.getKey() && entry.getKey().equalsIgnoreCase("isSocialMedia") && null != entry.getValue())
						{
							isSocialMediaInput = entry.getValue();
						}
					}
				}
			}

			if (null != isSocialMediaInput && isSocialMediaInput.equalsIgnoreCase("Y"))
			{
				isSocialMedia = true;
			}

			UserDetails userDetails = null;

			try
			{
				userDetails = retrieveUser(userName.toLowerCase());
			}
			catch (final UsernameNotFoundException notFound)
			{
				LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + notFound);
				throw new BadCredentialsException(
						messages.getMessage(MarketplacewebservicesConstants.COREAUTH_BADCRED, "Email id does not exist"), notFound);
			}
			catch (final DataIntegrityViolationException dataIntegrity)
			{
				LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + dataIntegrity);
				throw new BadCredentialsException(
						messages.getMessage(MarketplacewebservicesConstants.COREAUTH_BADCRED, "Email id does not exist"),
						dataIntegrity);
			}
			getPreAuthenticationChecks().check(userDetails);
			final UserModel userModel = extUserService.getUserForUIDAccessToken(StringUtils.lowerCase(userName));
			User user = null;
			if (null != userModel)
			{
				user = modelService.getSource(userModel);
			}
			CustomerModel custModel = getModelService().create(CustomerModel.class);
			// FORM based check
			//Social Media Registration check Mobile web service starts
			if (null != user)
			{
				custModel = extUserService.getUserForUid(StringUtils.lowerCase(authentication.getName()));
			}

			boolean userRegisteredBySocialMedia = false;
			if (null != custModel.getCustomerRegisteredBySocialMedia())
			{
				userRegisteredBySocialMedia = custModel.getCustomerRegisteredBySocialMedia().booleanValue();
			}
			boolean isSocial = false;
			if (isSocialMedia || userRegisteredBySocialMedia)
			{
				isSocial = true;
			}
			if ((!isSocial) && null != user)
			{
				final Object credential = authentication.getCredentials();


				if (credential instanceof String)
				{
					if (!user.checkPassword((String) credential))
					{
						throw new BadCredentialsException(
								messages.getMessage(MarketplacewebservicesConstants.COREAUTH_BADCRED, "Bad credentials"));
					}
				}
				else if (credential instanceof LoginToken)
				{
					if (!user.checkPassword((LoginToken) credential))
					{
						throw new BadCredentialsException(
								messages.getMessage(MarketplacewebservicesConstants.COREAUTH_BADCRED, "Bad credentials"));
					}
				}

				else
				{
					throw new BadCredentialsException(
							messages.getMessage(MarketplacewebservicesConstants.COREAUTH_BADCRED, "Bad credentials"));
				}
				//Social Media Registration check Mobile web service ends
			}

			postAuthenticationChecks.check(userDetails);
			if (null != user)
			{
				// finally, set user in session
				JaloSession.getCurrentSession().setUser(user);
			}

			return createSuccessAuthentication(authentication, userDetails);

		}
		else
		{
			return createSuccessAuthentication(//
					authentication, //
					new CoreUserDetails("systemNotInitialized", "systemNotInitialized", true, false, true, true,
							Collections.EMPTY_LIST, null));
		}


	}

	/**
	 * creating token details on successful authentication
	 *
	 * @param authentication
	 * @param user
	 * @return Authentication
	 */
	/*
	 * protected Authentication createSuccessAuthentication(final ExtAuthentication authentication, final UserDetails
	 * user) { final UsernamePasswordAuthenticationToken result = new
	 * UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
	 * if (null != authentication.getDetails()) { result.setDetails(authentication.getDetails()); }
	 *
	 * return result; }
	 */

	//Social media registration google issue
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
				LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS
						+ messages.getMessage("CoreAuthenticationProvider.credentialsExpired", "User credentials have expired"));
				throw new CredentialsExpiredException(
						messages.getMessage("CoreAuthenticationProvider.credentialsExpired", "User credentials have expired"), user);
			}
		}
	}




}
