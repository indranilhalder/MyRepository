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
package com.tisl.mpl.storefront.security.impl;

import de.hybris.platform.acceleratorstorefrontcommons.security.GUIDCookieStrategy;
import de.hybris.platform.core.model.user.CustomerModel;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.web.util.CookieGenerator;

import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.storefront.interceptors.beforecontroller.RequireHardLoginBeforeControllerHandler;
import com.tisl.mpl.storefront.security.cookie.KeepAliveCookieGenerator;
import com.tisl.mpl.storefront.security.cookie.LastUserLoggedInCookieGenerator;
import com.tisl.mpl.storefront.security.cookie.LuxuryEmailCookieGenerator;
import com.tisl.mpl.storefront.security.cookie.LuxuryUserCookieGenerator;

//Sonar Issue Fixed For Kidswear
//import com.tisl.mpl.util.GenericUtilityMethods;

/**
 * Default implementation of {@link GUIDCookieStrategy}
 */
public class DefaultGUIDCookieStrategy implements GUIDCookieStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultGUIDCookieStrategy.class);

	private final SecureRandom random;
	private final MessageDigest sha;

	private CookieGenerator cookieGenerator;

	private KeepAliveCookieGenerator keepAliveCookieGenerator;

	private LuxuryEmailCookieGenerator luxuryEmailCookieGenerator;

	private ExtendedUserService userService;

	//Added for UF-93
	private LastUserLoggedInCookieGenerator lastUserLoggedInCookieGenerator;

	/**
	 * @return the lastUserLoggedInCookieGenerator
	 */
	public LastUserLoggedInCookieGenerator getLastUserLoggedInCookieGenerator()
	{
		return lastUserLoggedInCookieGenerator;
	}

	/**
	 * @param lastUserLoggedInCookieGenerator
	 *           the lastUserLoggedInCookieGenerator to set
	 */
	public void setLastUserLoggedInCookieGenerator(final LastUserLoggedInCookieGenerator lastUserLoggedInCookieGenerator)
	{
		this.lastUserLoggedInCookieGenerator = lastUserLoggedInCookieGenerator;
	}

	/**
	 * @return the userService
	 */
	public ExtendedUserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final ExtendedUserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the luxuryEmailCookieGenerator
	 */
	public LuxuryEmailCookieGenerator getLuxuryEmailCookieGenerator()
	{
		return luxuryEmailCookieGenerator;
	}

	/**
	 * @param luxuryEmailCookieGenerator
	 *           the luxuryEmailCookieGenerator to set
	 */
	public void setLuxuryEmailCookieGenerator(final LuxuryEmailCookieGenerator luxuryEmailCookieGenerator)
	{
		this.luxuryEmailCookieGenerator = luxuryEmailCookieGenerator;
	}

	/**
	 * @return the luxuryUserCookieGenerator
	 */
	public LuxuryUserCookieGenerator getLuxuryUserCookieGenerator()
	{
		return luxuryUserCookieGenerator;
	}

	/**
	 * @param luxuryUserCookieGenerator
	 *           the luxuryUserCookieGenerator to set
	 */
	public void setLuxuryUserCookieGenerator(final LuxuryUserCookieGenerator luxuryUserCookieGenerator)
	{
		this.luxuryUserCookieGenerator = luxuryUserCookieGenerator;
	}

	private LuxuryUserCookieGenerator luxuryUserCookieGenerator;

	/**
	 * @return the keepAliveCookieGenerator
	 */
	public KeepAliveCookieGenerator getKeepAliveCookieGenerator()
	{
		return keepAliveCookieGenerator;
	}

	/**
	 * @param keepAliveCookieGenerator
	 *           the keepAliveCookieGenerator to set
	 */
	public void setKeepAliveCookieGenerator(final KeepAliveCookieGenerator keepAliveCookieGenerator)
	{
		this.keepAliveCookieGenerator = keepAliveCookieGenerator;
	}

	public DefaultGUIDCookieStrategy() throws NoSuchAlgorithmException
	{
		random = SecureRandom.getInstance("SHA1PRNG");
		sha = MessageDigest.getInstance("SHA-1");
		Assert.notNull(random);
		Assert.notNull(sha);
	}

	@Override
	public void setCookie(final HttpServletRequest request, final HttpServletResponse response)
	{
		//Commenting code, due to secure requests received only.
		/*
		 * if (!request.isSecure()) { // We must not generate the cookie for insecure requests, otherwise there is not
		 * point doing this at all throw new IllegalStateException("Cannot set GUIDCookie on an insecure request!"); }
		 */

		final String guid = createGUID();

		getCookieGenerator().addCookie(response, guid);
		//Add the Keep Alive Cookie on login
		getKeepAliveCookieGenerator().addCookie(response,
				new String(Base64.encodeBase64String(request.getSession().getId().getBytes())));

		//Set the luxury site cookies
		if (userService.getCurrentUser() != null)
		{
			final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
			if (StringUtils.isNotEmpty(customer.getOriginalUid()))
			{
				LOG.info("Adding cookie for luxury cookie");
				//Encrypt the customer email id and store it in luxury cookie
				getLuxuryEmailCookieGenerator().addCookie(response, encrypt(customer.getOriginalUid()));
				//Commenting this as this is not required
				//getLuxuryUserCookieGenerator().addCookie(response, userService.getAccessTokenForUser(customer.getOriginalUid()));
				/** Added for UF-93 **/
				//				if ("true".equalsIgnoreCase(request.getParameter("j_RememberMe")))
				//				{
				//					lastUserLoggedInCookieGenerator.addCookie(response,
				//							new String(Base64.encodeBase64String(customer.getOriginalUid().getBytes())));
				//					LOG.error("DefaultGUIDCookieStrategy.setCookie() 'RememberMe':: "
				//							+ request.getSession().getAttribute("j_RememberMe"));
				//				}
				//				else
				//				{
				//					final Cookie cookie = GenericUtilityMethods.getCookieByName(request, "LastUserLogedIn");
				//					if (null != cookie)
				//					{
				//						lastUserLoggedInCookieGenerator.removeCookie(response); // Remove the Cookie if Remember Me not Seleceted. Thi sis not to be displayed next time.
				//					}
				//				}
				lastUserLoggedInCookieGenerator.addCookie(response,
						new String(Base64.encodeBase64String(customer.getOriginalUid().getBytes())));
				//LOG.error("DefaultGUIDCookieStrategy.setCookie() 'customer.getOriginalUid().getBytes()':: "
				//+ customer.getOriginalUid().getBytes());
				/** Ends for UF-93 **/
			}
		}

		request.getSession().setAttribute(RequireHardLoginBeforeControllerHandler.SECURE_GUID_SESSION_KEY, guid);

		if (LOG.isInfoEnabled())
		{
			LOG.info("Setting guid cookie and session attribute: " + guid);
		}
	}

	/**
	 * @param originalUid
	 * @return
	 */
	private String encrypt(final String originalUid)
	{
		final String encryptionKey = "encryptor key";
		final String encryptedText = encryptAES(originalUid.trim(), encryptionKey);

		LOG.debug("String to Encrypt: " + originalUid);
		LOG.debug("Encrypted: " + encryptedText);
		return encryptedText;
	}

	/**
	 * @param trim
	 */
	private String encryptAES(final String encryptionText, final String key)
	{
		String encryptedText = null;
		try
		{

			final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));


			encryptedText = Base64.encodeBase64String(cipher.doFinal(encryptionText.getBytes("UTF-8")));

		}
		catch (final Exception e)
		{

			LOG.error("Error while encrypting: " + e.toString());
		}
		return encryptedText;


	}


	//			public String decrypt(final String strToDecrypt, final String encryptionKey)
	//			{
	//				String decryptedText = null;
	//				try
	//				{
	//					final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	//
	//					cipher.init(Cipher.DECRYPT_MODE, getSecretKey(encryptionKey));
	//					decryptedText = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
	//
	//				}
	//				catch (final Exception e)
	//				{
	//
	//					System.out.println("Error while decrypting: " + e.toString());
	//				}
	//				return decryptedText;
	//			}


	/**
	 * @param key
	 * @return
	 */
	private SecretKeySpec getSecretKey(final String encryptionKey)
	{
		MessageDigest sha = null;
		byte[] key = null;
		try
		{
			key = encryptionKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			LOG.debug("Key Length" + key.length);
			LOG.debug(new String(key, "UTF-8"));



		}
		catch (final NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new SecretKeySpec(key, "AES");
	}

	@Override
	public void deleteCookie(final HttpServletRequest request, final HttpServletResponse response)
	{
		/*
		 * if (!request.isSecure()) { LOG.error(
		 * "Cannot remove secure GUIDCookie during an insecure request. I should have been called from a secure page."); }
		 * else {
		 */
		// Its a secure page, we can delete the cookie
		getCookieGenerator().removeCookie(response);
		//Delete the Keep alive cookie
		getKeepAliveCookieGenerator().removeCookie(response);
		//Update the luxury cookies to anonymous
		//updateLuxuryCookies(request, response, getLuxuryUserCookieGenerator().getCookieName());
		updateLuxuryCookies(request, response, getLuxuryEmailCookieGenerator().getCookieName());
		//}
	}

	/**
	 * @param request
	 * @param response
	 */
	private void updateLuxuryCookies(final HttpServletRequest request, final HttpServletResponse response, final String cookieName)
	{
		final Cookie[] cookies = request.getCookies();

		if (cookies != null)
		{
			for (final Cookie cookie : cookies)
			{
				if (cookie.getName().equals(cookieName))
				{
					cookie.setValue("anonymous");
					if (StringUtils.isNotEmpty(getLuxuryEmailCookieGenerator().getCustomDomain()))
					{
						cookie.setDomain(getLuxuryEmailCookieGenerator().getCustomDomain());
						cookie.setPath("/");
					}
					response.addCookie(cookie);
					break;
				}
			}
		}

	}

	protected String createGUID()
	{
		final String randomNum = String.valueOf(getRandom().nextInt());
		final byte[] result = getSha().digest(randomNum.getBytes());
		return String.valueOf(Hex.encodeHex(result));
	}

	protected CookieGenerator getCookieGenerator()
	{
		return cookieGenerator;
	}

	/**
	 * @param cookieGenerator
	 *           the cookieGenerator to set
	 */
	@Required
	public void setCookieGenerator(final CookieGenerator cookieGenerator)
	{
		this.cookieGenerator = cookieGenerator;
	}


	protected SecureRandom getRandom()
	{
		return random;
	}

	protected MessageDigest getSha()
	{
		return sha;
	}
}
