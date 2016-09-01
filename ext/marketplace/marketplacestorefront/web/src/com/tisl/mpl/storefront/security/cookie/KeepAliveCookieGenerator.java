/**
 *
 */
package com.tisl.mpl.storefront.security.cookie;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * @author 765463
 *
 */
public class KeepAliveCookieGenerator extends EnhancedCookieGenerator
{
	private static final Logger LOG = Logger.getLogger(KeepAliveCookieGenerator.class);


	public KeepAliveCookieGenerator() throws NoSuchAlgorithmException
	{
		random = SecureRandom.getInstance("SHA1PRNG");
		sha = MessageDigest.getInstance("SHA-1");
		Assert.notNull(random);
		Assert.notNull(sha);
	}

	private final SecureRandom random;

	/**
	 * @return the random
	 */
	public SecureRandom getRandom()
	{
		return random;
	}

	/**
	 * @return the sha
	 */
	public MessageDigest getSha()
	{
		return sha;
	}

	private final MessageDigest sha;



	public void addCookie(final HttpServletResponse response)
	{
		final String keepAliveCookieValue = generateCookieValue();
		LOG.info("Keep Alive Cookie value is :::" + keepAliveCookieValue);
		super.addCookie(response, keepAliveCookieValue);
	}

	private String generateCookieValue()
	{
		final String randomNum = String.valueOf(getRandom().nextInt());
		final byte[] result = getSha().digest(randomNum.getBytes());
		return String.valueOf(Hex.encodeHex(result));
	}
}
