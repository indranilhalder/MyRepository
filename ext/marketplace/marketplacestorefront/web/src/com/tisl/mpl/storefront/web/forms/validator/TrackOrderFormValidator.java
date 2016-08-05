/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.springframework.stereotype.Component;

import com.tisl.mpl.storefront.web.forms.TrackOrderForm;


/**
 * @author Techouts
 *
 *This class is used for validating the track order form
 */
@Component("trackOrderFormValidator")
public class TrackOrderFormValidator
{
	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	public String validateTrackOrderForm(final TrackOrderForm trackOrderForm)
	{
		return validate(trackOrderForm);
	}


	public String validate(final TrackOrderForm trackOrderForm)
	{

		final String orderCode = trackOrderForm.getOrderCode();
		final String emailId = trackOrderForm.getEmailId();

		if (StringUtils.isEmpty(orderCode))
		{
			return "trackorder.ordercode.invalid";
		}
		else if (StringUtils.length(emailId) > 255 || !validateEmailAddress(emailId))
		{
			return "trackorder.email.invalid";
		}
		else if (!validDomain(emailId))
		{
			return "trackorder.email.invalid";
		}
		return "success";
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
