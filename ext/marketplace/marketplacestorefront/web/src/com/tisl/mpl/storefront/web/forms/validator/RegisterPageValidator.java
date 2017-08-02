/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

/**
 * @author TCS
 *
 */


import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.site.BaseSiteService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.storefront.businessvalidator.CommonAsciiValidator;
import com.tisl.mpl.storefront.web.forms.ExtRegisterForm;


/**
 * Validates entries on Guest register user forms.
 */
@Component("registerPageValidator")
public class RegisterPageValidator implements Validator
{
	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?:.*[!@#$%*^&.()+].*).{8,16})";
	public static final String MOBILE_REGEX = "^[0-9]*$";
	private static final int MAX_FIELD_LENGTH_40 = 40;
	private static final int MAX_MOBILE_FIELD_LENGTH_10 = 10;
	//"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*&.]).{8,16})";
	@Autowired
	private BaseSiteService baseSiteService;

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return RegisterForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final ExtRegisterForm registerForm = (ExtRegisterForm) object;
		final String email = registerForm.getEmail();
		final String pwd = registerForm.getPwd();
		final String checkPwd = registerForm.getCheckPwd();

		final String firstName = registerForm.getFirstName();
		final String lastName = registerForm.getLastName();
		final String mobileNumber = registerForm.getMobileNumber();
		final String gender = registerForm.getGender();


		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("email", "register.email.invalid");
		}
		else if (StringUtils.length(email) > 240 || !validateEmailAddress(email))
		{
			errors.rejectValue("email", "register.email.invalid.long");
		}
		else if (!validDomain(email))
		{
			errors.rejectValue("email", "register.email.invalid");
		}


		if (StringUtils.isEmpty(pwd))
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.PWD, "register.pwd.invalid");
		}
		//TISPRM-11
		/*
		 * else if (!CommonAsciiValidator.validatePassword(pwd)) {
		 * errors.rejectValue(MarketplacecommerceservicesConstants.PWD, "register.pwd.invalid.pp"); }
		 */
		else if (StringUtils.length(pwd) < 8)
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.PWD, "register.pwd.invalid");
		}
		/*
		 * else if (StringUtils.length(pwd) > 16) { errors.rejectValue(MarketplacecommerceservicesConstants.PWD,
		 * "register.pwd.invalid.long"); }
		 */
		else if (checkWhiteSpace(pwd))
		{
			errors.rejectValue(MarketplacecommerceservicesConstants.PWD, "register.pwd.invalid.space");
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


		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		final String site = currentBaseSite.getUid();

		if (MarketplaceFacadesConstants.LuxuryPrefix.equals(site))
		{
			if (StringUtils.isEmpty(firstName))
			{
				errors.rejectValue("firstName", "profile.firstName.invalid");
			}

			else if (!StringUtils.isEmpty(firstName) && !CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(firstName)
					|| StringUtils.length(firstName) > MAX_FIELD_LENGTH_40)
			{
				errors.rejectValue("firstName", "profile.firstName.invalidlength");
			}
			if (StringUtils.isEmpty(lastName))
			{
				errors.rejectValue("lastName", "profile.lastName.invalid");
			}
			else if (!StringUtils.isEmpty(lastName) && !CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(lastName)
					|| StringUtils.length(lastName) > MAX_FIELD_LENGTH_40)
			{
				errors.rejectValue("lastName", "profile.lastName.invalidlength");
			}

			if (!StringUtils.isEmpty(mobileNumber) && StringUtils.length(mobileNumber) > MAX_MOBILE_FIELD_LENGTH_10)
			{
				errors.rejectValue("mobileNumber", "profile.mobileNumber.invalidlength");
			}
			else if (null != mobileNumber && !(MarketplacecommerceservicesConstants.EMPTY).equals(mobileNumber)
					&& !mobileNumber.matches(MOBILE_REGEX))
			{
				errors.rejectValue("mobileNumber", "profile.mobileNumber.invalid");
			}
			if (StringUtils.isEmpty(gender))
			{
				errors.rejectValue("gender", "profile.select.gender");
			}
		}
	}


	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean validatePasswordPolicy(final String pwd)
	{
		final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		final Matcher matcher = pattern.matcher(pwd);
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

	private static boolean checkWhiteSpace(final String str)
	{
		boolean isSpace = false;
		char s;
		if (str.length() > 0)
		{
			for (int i = 0; i < str.length(); i++)
			{
				s = str.charAt(i);
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
