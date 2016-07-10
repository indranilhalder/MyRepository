/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePwdForm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.storefront.businessvalidator.CommonAsciiValidator;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.web.forms.MplUpdatePwdForm;


/**
 * @author 682160
 *
 */
@Component("mplForgotPwdFormValidator")
public class MplForgotPwdFormValidator implements Validator
{
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?:.*[!@#$%*^&.()+].*).{8,16})";

	//"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*&.]).{8,16})";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdatePwdForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final MplUpdatePwdForm mplUpdatePwdForm = (MplUpdatePwdForm) object;

		final String pwd = mplUpdatePwdForm.getPwd();
		final String checkPwd = mplUpdatePwdForm.getCheckPwd();

		if (StringUtils.isEmpty(pwd))
		{
			errors.rejectValue(ModelAttributetConstants.PWD, "register.pwd.invalid");
		}
		else if (!CommonAsciiValidator.validatePassword(pwd))
		{
			errors.rejectValue(ModelAttributetConstants.PWD, "register.pwd.invalid.pp");
		}
		else if (StringUtils.length(pwd) < 8)
		{
			errors.rejectValue(ModelAttributetConstants.PWD, "register.pwd.invalid");
		}
		else if (StringUtils.length(pwd) > 16)
		{
			errors.rejectValue(ModelAttributetConstants.PWD, "register.pwd.invalid.long");
		}
		else if (checkWhiteSpace(pwd))
		{
			errors.rejectValue(ModelAttributetConstants.PWD, "register.pwd.invalid.space");
		}

		if (StringUtils.isNotEmpty(pwd) && StringUtils.isNotEmpty(checkPwd) && !StringUtils.equals(pwd, checkPwd))
		{
			errors.rejectValue("checkPwd", "validation.checkPwd.equals");
		}
		else
		{
			if (StringUtils.isEmpty(checkPwd))
			{
				errors.rejectValue("checkPwd", "register.checkPwd.invalid");
			}
		}
	}

	public boolean validatePasswordPolicy(final String pwd)
	{
		final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		final Matcher matcher = pattern.matcher(pwd);
		return matcher.matches();
	}

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
