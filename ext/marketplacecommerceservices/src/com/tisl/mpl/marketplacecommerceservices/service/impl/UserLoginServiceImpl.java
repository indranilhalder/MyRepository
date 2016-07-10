/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.UserLoginService;


/**
 * @author TCS
 *
 */
public class UserLoginServiceImpl implements UserLoginService
{
	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	@Autowired
	private ModelService modelService;
	@Autowired
	private ExtendedUserService extUserService;

	/**
	 * @return the messages
	 */
	public MessageSourceAccessor getMessages()
	{
		return messages;
	}


	/**
	 * @param messages
	 *           the messages to set
	 */
	public void setMessages(final MessageSourceAccessor messages)
	{
		this.messages = messages;
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


	private UserDetailsService userDetailsService;




	/**
	 * @return the userDetailsService
	 */
	public UserDetailsService getUserDetailsService()
	{
		return userDetailsService;
	}


	/**
	 * @param userDetailsService
	 *           the userDetailsService to set
	 */
	public void setUserDetailsService(final UserDetailsService userDetailsService)
	{
		this.userDetailsService = userDetailsService;
	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.UserLoginService#loginUser()
	 */
	/*
	 * @Javadoc Method to login system user, if username exists , if exists , if the password matches
	 * 
	 * @param->login and password
	 * 
	 * @return->String, Success or Error
	 */
	@Override
	public String loginUser(final String login, final String password)
	{
		String result = null;
		UserDetails userDetails = null;
		try
		{
			//Check if the user exists in the system with login
			userDetails = retrieveUserforMarketplace(login);
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
		}
		catch (final UsernameNotFoundException notFound)
		{ //User name not found
			result = MarketplacecommerceservicesConstants.ERROR_FLAG;
			throw new BadCredentialsException(messages.getMessage("CoreAuthenticationProvider.badCredentials",
					MarketplacecommerceservicesConstants.BAD_CREDENTIALS), notFound);
		}
		if (!userDetails.isCredentialsNonExpired())
		{
			//User details have expired
			result = MarketplacecommerceservicesConstants.ERROR_FLAG;
			throw new CredentialsExpiredException(messages.getMessage("CoreAuthenticationProvider.credentialsExpired",
					MarketplacecommerceservicesConstants.CREDENTIALS_EXP), login);
		}
		//Get user for Login
		final UserModel userModel = extUserService.getUserForUID(StringUtils.lowerCase(login));
		final User user = modelService.getSource(userModel);

		//Check if the password matches
		if (!user.checkPassword(password))
		{
			//If not throw exception
			result = MarketplacecommerceservicesConstants.ERROR_FLAG;
			throw new BadCredentialsException(messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		// finally, set user in session
		JaloSession.getCurrentSession().setUser(user);
		//Return success flag
		result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
		return result;

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.UserLoginService#loginUser()
	 */
	/*
	 * @Javadoc Method to login user with social login, In this case the password validation is not done. Password
	 * validation is done by respective API
	 * 
	 * @param->login
	 * 
	 * @return->String, Success or Error
	 */
	@Override
	public String loginUserForSocial(final String login)
	{
		String result = null;
		UserDetails userDetails = null;
		try
		{
			//Retrieve user details
			userDetails = retrieveUserforMarketplace(login);
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
		}
		catch (final UsernameNotFoundException notFound)
		{
			//Throw exception bad credentials
			result = MarketplacecommerceservicesConstants.ERROR_FLAG;
			throw new BadCredentialsException(messages.getMessage("CoreAuthenticationProvider.badCredentials",
					MarketplacecommerceservicesConstants.BAD_CREDENTIALS), notFound);
		}
		if (!userDetails.isCredentialsNonExpired())
		{
			result = MarketplacecommerceservicesConstants.ERROR_FLAG;
			throw new CredentialsExpiredException(messages.getMessage("CoreAuthenticationProvider.credentialsExpired",
					MarketplacecommerceservicesConstants.CREDENTIALS_EXP), login);
		}
		//Get user for the login
		final UserModel userModel = extUserService.getUserForUID(StringUtils.lowerCase(login));
		final User user = modelService.getSource(userModel);
		// finally, set user in session
		JaloSession.getCurrentSession().setUser(user);
		result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
		//Return result
		return result;

	}

	/*
	 * @Javadoc Retrieve user for marketplace with login
	 * 
	 * @param->username
	 * 
	 * @return->UserDetails
	 */
	public UserDetails retrieveUserforMarketplace(final String username) throws AuthenticationException
	{
		//Try to load user with username
		UserDetails loadedUser;
		try
		{
			loadedUser = getUserDetailsService().loadUserByUsername(username);
		}
		catch (final DataAccessException repositoryProblem)
		{
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}
		if (loadedUser == null)
		{
			throw new AuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}

}
