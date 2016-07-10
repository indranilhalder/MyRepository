/**
 * 
 */
package com.tisl.mpl.storefront.web.forms.validator;

import de.hybris.platform.acceleratorstorefrontcommons.forms.ForgottenPwdForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * @author 594031
 * 
 */



@Component("mplEmailValidator")
public class MplEmailValidator implements Validator
{
	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return ForgottenPwdForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final ForgottenPwdForm forgottenPwdForm = (ForgottenPwdForm) object;
		final String email = forgottenPwdForm.getEmail();

		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("email", "register.email.invalid");
		}
		else if (StringUtils.length(email) > 255 || !validateEmailAddress(email))
		{
			errors.rejectValue("email", "register.email.invalid");
		}
		else if (!validDomain(email))
		{
			errors.rejectValue("email", "register.email.invalid");
		}
	}


	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean validDomain(final String email)
	{
		final DomainValidator domainvalidator = DomainValidator.getInstance();
		final int index = email.indexOf('@');
		final String substringEmail = email.substring(index + 1);
		final boolean valid = domainvalidator.isValid(substringEmail);
		return valid;
	}
}
