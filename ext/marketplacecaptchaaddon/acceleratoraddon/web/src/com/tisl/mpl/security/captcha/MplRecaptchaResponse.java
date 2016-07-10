/**
 * 
 */
package com.tisl.mpl.security.captcha;

/**
 * @author 762787
 * 
 */
public class MplRecaptchaResponse
{
	private final boolean valid;
	private final String errorMessage;

	protected MplRecaptchaResponse(final boolean valid, final String errorMessage)
	{
		this.valid = valid;
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage()
	{
		return this.errorMessage;
	}

	public boolean isValid()
	{
		return this.valid;
	}

}
