/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdatePasswordForm;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author 682160
 *
 */
@Component("mplUpdatePasswordFormValidator")
public class MplUpdatePasswordFormValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return UpdatePasswordForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final UpdatePasswordForm updatePasswordForm = (UpdatePasswordForm) object;
		final String currentPassword = updatePasswordForm.getCurrentPassword();
		final String newPassword = updatePasswordForm.getNewPassword();
		final String checkNewPassword = updatePasswordForm.getCheckNewPassword();

		if (StringUtils.isEmpty(currentPassword))
		{
			errors.rejectValue("currentPassword", "register.pwd.invalid");
		}
		else
		{
			if (StringUtils.isEmpty(newPassword))
			{
				errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid");
			}
			else if (StringUtils.length(newPassword) < 8)
			{
				errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid");
			}
			else if (StringUtils.length(newPassword) > 16)
			{
				errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid.long");
			}
			else if (checkWhiteSpace(newPassword))
			{
				errors.rejectValue(MarketplacecommerceservicesConstants.NEW_PASSWORD, "register.pwd.invalid.space");
			}

			if (StringUtils.isNotEmpty(newPassword) && StringUtils.isNotEmpty(checkNewPassword)
					&& !StringUtils.equals(newPassword, checkNewPassword))
			{
				errors.rejectValue("checkNewPassword", "validation.checkPwd.equals");
			}

			else
			{
				if (StringUtils.isEmpty(checkNewPassword))
				{
					errors.rejectValue("checkNewPassword", "register.checkPwd.invalid");
				}
			}
		}
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
