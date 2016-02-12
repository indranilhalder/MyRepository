/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * @author TCS
 * @description Validator for Emails
 */


@Component("mplUpdateEmailFormValidator")
public class MplUpdateEmailFormValidator implements Validator
{
	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdateEmailForm.class.equals(aClass);
	}

	/**
	 * @description this method is called to validate all data of UpdateEmailForm
	 */
	@Override
	public void validate(final Object object, final Errors errors)
	{
		final UpdateEmailForm updateEmailForm = (UpdateEmailForm) object;
		final String email = updateEmailForm.getEmail();
		final String chkEmail = updateEmailForm.getChkEmail();
		final String password = updateEmailForm.getPassword();

		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("email", "profile.email.invalid");
		}
		else if (StringUtils.length(email) > 240 || !Pattern.matches(EMAIL_REGEX, email))
		{
			errors.rejectValue("email", "profile.email.invalid");
		}

		if (StringUtils.isEmpty(chkEmail))
		{
			errors.rejectValue("chkEmail", "profile.checkEmail.invalid");
		}

		if (StringUtils.isEmpty(password))
		{
			errors.rejectValue("password", "profile.pwd.invalid");
		}
	}
}