/**
 *
 */
package com.tisl.mpl.security.captcha;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

import net.tanesha.recaptcha.http.HttpLoader;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MplCaptchaaddonConstants;


/**
 * @author TCS
 *
 */
public class MplRecaptchaImpl
{
	public static final String PROPERTY_THEME = "theme";
	public static final String PROPERTY_TABINDEX = "tabindex";
	public static final String HTTP_SERVER = "http://api.recaptcha.net";
	public static final String HTTPS_SERVER = "https://api-secure.recaptcha.net";
	public static final String VERIFY_URL = "http://api-verify.recaptcha.net/verify";
	public static final String JSOPTION = "<script type=\"text/javascript\">\r\nvar RecaptchaOptions = {";
	private String privateKey;
	private String publicKey;
	private String recaptchaServer;
	private boolean includeNoscript;
	private HttpLoader httpLoader;
	protected static final Logger LOG = Logger.getLogger(MplRecaptchaImpl.class);

	public MplRecaptchaImpl()
	{
		this.recaptchaServer = HTTP_SERVER;
		this.includeNoscript = false;
		this.httpLoader = new MplHttpLoader();
	}

	public void setPrivateKey(final String privateKey)
	{
		this.privateKey = privateKey;
	}

	public void setPublicKey(final String publicKey)
	{
		this.publicKey = publicKey;
	}

	public void setRecaptchaServer(final String recaptchaServer)
	{
		this.recaptchaServer = recaptchaServer;
	}

	public void setIncludeNoscript(final boolean includeNoscript)
	{
		this.includeNoscript = includeNoscript;
	}

	public void setHttpLoader(final HttpLoader httpLoader)
	{
		this.httpLoader = httpLoader;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.tanesha.recaptcha.ReCaptcha#checkAnswer(java.lang.String, java.lang.String, java.lang.String)
	 */
	/**
	 * @description this is called to validate captcha
	 * @param remoteAddr
	 * @param challenge
	 * @param response
	 * @return MplRecaptchaResponse
	 */
	@SuppressWarnings("deprecation")
	public MplRecaptchaResponse checkAnswer(final String remoteAddr, final String challenge, final String response)
	{
		final String postParameters = MplCaptchaaddonConstants.part_PRIVATEKEY + URLEncoder.encode(this.privateKey)
				+ MplCaptchaaddonConstants.part_REMOTEIP + URLEncoder.encode(remoteAddr) + MplCaptchaaddonConstants.part_CHALLENGE
				+ URLEncoder.encode(challenge) + MplCaptchaaddonConstants.part_RESPONSE + URLEncoder.encode(response);

		final String message = this.httpLoader.httpPost(VERIFY_URL, postParameters);

		if (message == null)
		{
			LOG.info(MplCaptchaaddonConstants.MSG_NULL_READ_FROM_SERVER);
			return new MplRecaptchaResponse(false, MplCaptchaaddonConstants.MSG_NULL_READ_FROM_SERVER);
		}

		final String[] a = message.split(MplCaptchaaddonConstants.NEXT_LINE);
		if (a.length < 1)
		{
			LOG.info(MplCaptchaaddonConstants.MSG_NO_ANS_FROM_CAPTCHA + message);
			return new MplRecaptchaResponse(false, MplCaptchaaddonConstants.MSG_NO_ANS_FROM_CAPTCHA + message);
		}
		final boolean valid = "true".equals(a[0]);
		String errorMessage = null;
		if (!(valid))
		{
			if (a.length > 1)
			{
				errorMessage = a[1];
			}
			else
			{
				errorMessage = MplCaptchaaddonConstants.MSG_RECAPTCHA_MISSING_ERROR_MSG;
			}
		}
		return new MplRecaptchaResponse(valid, errorMessage);
	}

	/**
	 * @description this method is called to create recaptcha html format
	 * @param errorMessage
	 * @param options
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public String createRecaptchaHtml(final String errorMessage, final Properties options)
	{
		final String errorPart = MplCaptchaaddonConstants.part_AMP_ERROR + URLEncoder.encode(errorMessage);

		String message = fetchJSOptions(options);

		message = message + "<script type=\"text/javascript\" src=\"" + this.recaptchaServer + "/challenge?k=" + this.publicKey
				+ errorPart + "\"></script>\r\n";

		if (this.includeNoscript)
		{
			final String noscript = "<noscript>\r\n\t<iframe src=\"" + this.recaptchaServer + "/noscript?k=" + this.publicKey
					+ errorPart + "\" height=\"300\" width=\"500\" frameborder=\"0\"></iframe><br>\r\n"
					+ "\t<textarea name=\"recaptcha_challenge_field\" rows=\"3\" cols=\"40\"></textarea>\r\n"
					+ "\t<input type=\"hidden\" name=\"recaptcha_response_field\" value=\"manual_challenge\">\r\n" + "</noscript>";

			message = message + noscript;
		}

		return message;
	}

	/**
	 * @description this method is called to set property i.e theme, tabindex in creating recaptcha html
	 * @param errorMessage
	 * @param theme
	 * @param tabindex
	 * @return String
	 */
	public String createRecaptchaHtml(final String errorMessage, final String theme, final Integer tabindex)
	{
		final Properties options = new Properties();

		if (theme != null)
		{
			options.setProperty(MplCaptchaaddonConstants.THEME, theme);
		}
		if (tabindex != null)
		{
			options.setProperty(MplCaptchaaddonConstants.TABINDEX, String.valueOf(tabindex));
		}

		return createRecaptchaHtml(errorMessage, options);
	}

	/**
	 * @description this method is called to fetch java script for creating captcha format
	 * @param properties
	 * @return String
	 */
	private String fetchJSOptions(final Properties properties)
	{
		if ((properties == null) || (properties.size() == 0))
		{
			return "";
		}

		String jsOptions = JSOPTION;

		for (final Enumeration e = properties.keys(); e.hasMoreElements();)
		{
			final String property = (String) e.nextElement();

			jsOptions = jsOptions + property + ":'" + properties.getProperty(property) + "'";

			if (e.hasMoreElements())
			{
				jsOptions = jsOptions + ",";
			}

		}

		jsOptions = jsOptions + "};\r\n</script>\r\n";

		return jsOptions;
	}
}
