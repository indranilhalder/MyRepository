/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.storefront.businessvalidator.CommonAsciiValidator;
import com.tisl.mpl.storefront.web.forms.MplCustomerProfileForm;
import com.tisl.mpl.util.DateValidator;


/**
 * @author TCS
 *
 */
@Component("mplCustomerProfileFormValidator")
public class MplCustomerProfileFormValidator implements Validator
{
	public static final String MOBILE_REGEX = "^[0-9]*$";
	private static final int MAX_FIELD_LENGTH_40 = 40;

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return MplCustomerProfileForm.class.equals(aClass);
	}


	@Override
	public void validate(final Object object, final Errors errors)
	{
		final MplCustomerProfileForm profileForm = (MplCustomerProfileForm) object;
		final String firstName = profileForm.getFirstName();
		final String lastName = profileForm.getLastName();
		//		final String nickName = profileForm.getNickName();
		final String dateOfBirth = profileForm.getDateOfBirth();
		final String dateOfAnniversary = profileForm.getDateOfAnniversary();
		final String mobileNumber = profileForm.getMobileNumber();

		if (!StringUtils.isEmpty(firstName) && !CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(firstName)
				|| StringUtils.length(firstName) > MAX_FIELD_LENGTH_40)
		{
			errors.rejectValue("firstName", "profile.firstName.invalid");
		}
		else if (!StringUtils.isEmpty(lastName) && !CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(lastName)
				|| StringUtils.length(lastName) > MAX_FIELD_LENGTH_40)
		{
			errors.rejectValue("lastName", "profile.lastName.invalid");
		}

		//		if (null != nickName && !(MarketplacecommerceservicesConstants.EMPTY).equals(nickName) && StringUtils.length(nickName) > 40)
		//		{
		//			errors.rejectValue("nickName", "profile.nickName.invalid");
		//		}

		else if (null != dateOfBirth && !(MarketplacecommerceservicesConstants.EMPTY).equals(dateOfBirth))
		{
			final boolean isDateValid = DateValidator.dateValidate(dateOfBirth, "/");

			if (!isDateValid)
			{
				errors.rejectValue("dateOfBirth", "profile.dateOfBirth.invalid");
			}
			else
			{
				final boolean isFutureDate = DateValidator.checkFutureDate(dateOfBirth, "dd/MM/yyyy");
				if (isFutureDate)
				{
					errors.rejectValue("dateOfBirth", "profile.dateOfBirth.invalid.futuredate");
				}
			}
		}

		else if (null != dateOfAnniversary && !(MarketplacecommerceservicesConstants.EMPTY).equals(dateOfAnniversary))
		{
			final boolean isDateValid = DateValidator.dateValidate(dateOfAnniversary, "/");

			if (!isDateValid)
			{
				errors.rejectValue("dateOfAnniversary", "profile.dateOfAnniversary.invalid");
			}
			else
			{
				final boolean isFutureDate = DateValidator.checkFutureDate(dateOfAnniversary, "dd/MM/yyyy");
				if (isFutureDate)
				{
					errors.rejectValue("dateOfAnniversary", "profile.dateOfAnniversary.invalid.futuredate");
				}
			}
		}

		else if (null != mobileNumber && !(MarketplacecommerceservicesConstants.EMPTY).equals(mobileNumber)
				&& !mobileNumber.matches(MOBILE_REGEX))
		{
			errors.rejectValue("mobileNumber", "profile.mobileNumber.invalid");
		}
	}
}
