/**
 * 
 */
package com.tisl.mpl.storefront.web.forms.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.storefront.web.forms.SendSmsOtpForm;


/**
 * @author 682160
 * 
 */

@Component("sendSmsOtpFormValidator")
public class SendSmsOtpFormValidator implements Validator
{

	public static final String NUMBER_REGEX = "^[0-9]*$";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return SendSmsOtpForm.class.equals(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final SendSmsOtpForm otpForm = (SendSmsOtpForm) object;
		final String otpNumber = otpForm.getOTPNumber();

		if (null != otpNumber && !(MarketplacecommerceservicesConstants.EMPTY).equals(otpNumber))
		{
			if ((StringUtils.length(otpNumber) > 6))
			{
				errors.rejectValue("OTPNumber", "profile.OTPNumber.invalid");
			}
			else if (!otpNumber.matches(NUMBER_REGEX))
			{
				errors.rejectValue("OTPNumber", "profile.OTPNumber.invalid");
			}
		}
		else
		{
			errors.rejectValue("OTPNumber", "profile.OTPNumber.invalid");
		}
	}

}
