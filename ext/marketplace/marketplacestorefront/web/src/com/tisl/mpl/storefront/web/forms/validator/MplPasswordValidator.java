package com.tisl.mpl.storefront.web.forms.validator;

/**
 *
 */


import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.storefront.businessvalidator.CommonAsciiValidator;


/**
 * @author TCS
 *
 */

/**
 * Validator for password forms.
 */
@Component("mplPasswordValidator")
public class MplPasswordValidator implements Validator
{
	//Fix for defect TISEE-3986
	//private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?:.*[!@#$%*^&.()+].*).{8,16})";
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*&.]).{8,16})";

	//"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#^$%*&.]).{8,16})";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdatePasswordForm.class.equals(aClass);
	}

	/**
	 * @description this method is used to validate password detail
	 */
	@Override
	public void validate(final Object object, final Errors errors)
	{
		final UpdatePasswordForm passwordForm = (UpdatePasswordForm) object;
		final String currPasswd = passwordForm.getCurrentPassword();
		final String newPasswd = passwordForm.getNewPassword();
		final String checkPasswd = passwordForm.getCheckNewPassword();

		if (StringUtils.isEmpty(currPasswd))
		{
			errors.rejectValue("currentPassword", "profile.currentPassword.invalid");
		}

		if (StringUtils.isEmpty(newPasswd))
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "updatePwd.pwd.invalid");
		}
		else if (!CommonAsciiValidator.validatePassword(newPasswd))
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid.pp");
		}
		else if (StringUtils.length(newPasswd) < 8)
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid");
		}
		else if (StringUtils.length(newPasswd) > 16)
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid.long");
		}
		else if (checkWhiteSpace(newPasswd))
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid.space");
		}

		if (StringUtils.isNotEmpty(newPasswd) && StringUtils.isNotEmpty(checkPasswd) && !StringUtils.equals(newPasswd, checkPasswd))
		{
			errors.rejectValue("checkNewPassword", "validation.checkPwd.equals");
		}
		else
		{
			if (StringUtils.isEmpty(checkPasswd))
			{
				errors.rejectValue("checkNewPassword", "register.checkPwd.invalid");
			}
		}
	}

	/**
	 * @description this method is used to validate password detail with Password regex
	 */
	public boolean validatePasswordPolicy(final String pwd)
	{
		final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		final Matcher matcher = pattern.matcher(pwd);
		return matcher.matches();
	}

	/**
	 * @description this method is used to validate password by checking whitespace
	 */
	private static boolean checkWhiteSpace(final String Str)
	{
		boolean isSpace = false;
		char s;
		if (Str.length() > 0)
		{
			for (int i = 0; i < Str.length(); i++)
			{
				s = Str.charAt(i);
				final char ch = s;
				if (ch == 32)
				{
					isSpace = true;
					break;
				}
			}
		}
		return isSpace;
	}

}