/**
 *
 */
package com.tisl.mpl.oauth2;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import com.tisl.mpl.auth.ExtAuthentication;


/**
 * @author TCS
 *
 */
public class ExtMplOauth2UserFilter extends ClientCredentialsTokenEndpointFilter
{
	private final boolean allowOnlyPost;
	private final AuthenticationEntryPoint authenticationEntryPoint;


	public ExtMplOauth2UserFilter()
	{
		this("/oauth/token");
	}

	public ExtMplOauth2UserFilter(final String path)
	{
		super(path);

		this.authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

		this.allowOnlyPost = false;

		setRequiresAuthenticationRequestMatcher(new ClientCredentialsRequestMatcher(path));

		((OAuth2AuthenticationEntryPoint) this.authenticationEntryPoint).setTypeName("Form");
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException
	{
		if ((this.allowOnlyPost) && (!("POST".equalsIgnoreCase(request.getMethod()))))
		{
			throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[]
			{ "POST" });
		}

		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		String isSocialMedia = request.getParameter("isSocialMedia");


		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if ((authentication != null) && (authentication.isAuthenticated()))
		{
			HashMap<String, String> authDetails = new HashMap<String, String>();
			if (null != authentication.getDetails())
			{
				authDetails = (HashMap<String, String>) authentication.getDetails();
			}
			else
			{
				authDetails = new HashMap();
			}
			if (null != isSocialMedia && !isSocialMedia.isEmpty())
			{
				authDetails.put("isSocialMedia", isSocialMedia);
			}
			final ExtAuthentication extAuth = new ExtAuthentication(authentication.getPrincipal(), authentication.getCredentials(),
					isSocialMedia, authentication.getAuthorities(), authentication.isAuthenticated(), authDetails);

			return extAuth;
		}

		if (clientId == null)
		{
			throw new BadCredentialsException("No client credentials presented");
		}

		if (clientSecret == null)
		{
			clientSecret = "";
		}

		clientId = clientId.trim();
		if (!StringUtils.isEmpty(isSocialMedia))
		{
			isSocialMedia = isSocialMedia.trim();
		}
		final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId, clientSecret);
		final Authentication auth = getAuthenticationManager().authenticate(authRequest);
		HashMap<String, String> authDetails = new HashMap<String, String>();
		if (null != auth.getDetails())
		{
			authDetails = (HashMap<String, String>) auth.getDetails();
		}
		else
		{
			authDetails = new HashMap();
		}
		if (null != isSocialMedia && !isSocialMedia.isEmpty())
		{
			authDetails.put("isSocialMedia", isSocialMedia);
		}
		if (null != authDetails && !authDetails.isEmpty())
		{
			authRequest.setDetails(authDetails);
		}
		return authRequest;
	}
}
