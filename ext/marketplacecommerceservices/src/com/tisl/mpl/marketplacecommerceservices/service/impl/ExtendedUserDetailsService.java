package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.spring.security.CoreUserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;


/**
 * The Class ExtendedUserDetailsService. - service providing additional user details service.
 *
 * @author Prium Reply
 */
public class ExtendedUserDetailsService implements UserDetailsService
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ExtendedUserDetailsService.class.getName());

	private static final String ROLEPREFIX = "ROLE_";

	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}

	@Autowired
	private ModelService modelService;

	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
	}

	/**
	 * @return the defaultCMSSiteService
	 */
	public CMSSiteService getDefaultCMSSiteService()
	{
		return defaultCMSSiteService;
	}

	/**
	 * @param defaultCMSSiteService
	 *           the defaultCMSSiteService to set
	 */
	public void setDefaultCMSSiteService(final CMSSiteService defaultCMSSiteService)
	{
		this.defaultCMSSiteService = defaultCMSSiteService;
	}

	@Autowired
	private ExtendedUserService extendedUserService;

	@Autowired
	private CMSSiteService defaultCMSSiteService;


	/**
	 * To check customer existence since for google extendedUserService.getUserForUid(username) is null Need to revisit
	 * and change
	 *
	 * @param registration
	 * @return boolean
	 */
	/*
	 * private CustomerModel existingCustomer(final String registration) { CustomerModel custModel = null; try { final
	 * List<CustomerModel> allcustomers = extendedUserService.getAllUsers();
	 * 
	 * for (final CustomerModel custCheck : allcustomers) { if (null != custCheck.getOriginalUid() &&
	 * StringUtils.equalsIgnoreCase(custCheck.getOriginalUid().toLowerCase(), registration.toLowerCase())) { custModel =
	 * custCheck; return custModel; } } } catch (final Exception e) { throw new
	 * UsernameNotFoundException("No Such user: " + e); }
	 * 
	 * return custModel; }
	 */

	/**
	 * Locates the user based on the originalUserId
	 * <p>
	 * (see 'activateCatalogVersions' in the corresponding spring configuration).
	 *
	 * @param username
	 *           - the username presented to the {@link AuthenticationProvider}
	 * @return a fully populated user record (could be <code>null</code>)
	 */
	@Override
	public CoreUserDetails loadUserByUsername(final String username)
	{
		if (username == null)
		{
			return null;
		}

		CustomerModel customerModel = null;
		customerModel = extendedUserService.getUserForUid(username);
		/*
		 * if (customerModel == null) { customerModel = existingCustomer(username); //Still customer model is null if
		 * (null == customerModel) { throw new UsernameNotFoundException("No Such user: " + username); } }
		 */
		if (null == customerModel)
		{
			throw new UsernameNotFoundException("No Such user: " + username);
		}

		if (/* null == customerModel || */null == customerModel.getEncodedPassword())
		{
			LOG.warn("user has no password: " + username);
			throw new DataIntegrityViolationException("password set to null, invalid user");
		}

		final Collection<UserGroupModel> groups = extendedUserService.getAllUserGroupsForUser(customerModel);
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		final Iterator<UserGroupModel> itr = groups.iterator();

		while (itr.hasNext())
		{
			final UserGroupModel group = itr.next();
			authorities.add(new GrantedAuthorityImpl(ROLEPREFIX + group.getUid().toUpperCase()));
		}

		final boolean enabled = !customerModel.isLoginDisabled();

		//dummy values, will be delivered by the platform - checks done before.
		final boolean accountNonExpired = true;
		final boolean credentialsNonExpired = true;
		final boolean accountNonLocked = true;

		final CoreUserDetails userDetails = new CoreUserDetails(username, customerModel.getEncodedPassword(), enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, JaloSession.getCurrentSession()
						.getSessionContext().getLanguage().getIsoCode());

		return userDetails;



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
}
