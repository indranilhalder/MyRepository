/**
 *
 */
package com.tisl.mpl.auth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;


/**
 * @author TCS
 *
 */
public class ExtAuthentication extends UsernamePasswordAuthenticationToken
{

	private final String socialMedia;
	private final Collection<GrantedAuthority> authorities;
	private Object details;

	/**
	 * @return the details
	 */
	@Override
	public Object getDetails()
	{
		return details;
	}

	/**
	 * @param details
	 *           the details to set
	 */
	@Override
	public void setDetails(final Object details)
	{
		this.details = details;
	}

	/**
	 * @return the authorities
	 */
	@Override
	public Collection<GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	private boolean authenticated;



	/**
	 * @return the authenticated
	 */
	@Override
	public boolean isAuthenticated()
	{
		return authenticated;
	}

	/**
	 * @param authenticated
	 *           the authenticated to set
	 */
	@Override
	public void setAuthenticated(final boolean authenticated)
	{
		this.authenticated = authenticated;
	}

	/**
	 * @return the socialMedia
	 */
	public String getSocialMedia()
	{
		return socialMedia;
	}

	/**
	 * @param principal
	 * @param credentials
	 */
	public ExtAuthentication(final Object principal, final Object credentials, final String socialMedia,
			final Collection<? extends GrantedAuthority> authorities, final boolean authenticated, final Object details)
	{

		super(principal, credentials);
		this.socialMedia = socialMedia;
		this.authenticated = authenticated;
		this.authorities = (Collection<GrantedAuthority>) authorities;
		this.details = details;
		// YTODO Auto-generated constructor stub
	}



}
