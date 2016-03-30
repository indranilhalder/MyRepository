/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;


import com.tisl.mpl.storefront.businessvalidator.CommonAsciiValidator;
import com.tisl.mpl.storefront.web.forms.ReturnPincodeCheckForm;


/**
 * @author Dileep
 *
 */
@Component("returnItemFormValidator")
public class ReturnItemFormValidator
{
	private static final int MAX_FIELD_LENGTH_140 = 140;
	private static final int MAX_FIELD_LENGTH_10 = 10;
	private static final int MAX_FIELD_LENGTH_6 = 6;
	private static final int MAX_FIELD_LENGTH_UPDATED = 30;
	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";


	public String returnValidate(final ReturnPincodeCheckForm addressForm)
	{
		final String errorMsg = validateStandardFields(addressForm);
		return errorMsg;
	}

	public String validateStandardFields(final ReturnPincodeCheckForm addressForm)
	{
		final String firstName = addressForm.getFirstName();
		final String lastName = addressForm.getLastName();
		final String line1 = addressForm.getAddressLane1();
		final String line2 = addressForm.getAddressLane2();
		final String townCity = addressForm.getCity();
		final String state = addressForm.getState();
		final String mobile = addressForm.getMobileNo();
		final String postcode = addressForm.getPincode();
		final String country = addressForm.getCountry();
		final String landmark = addressForm.getLandmark();

		if (StringUtils.isEmpty(firstName))
		{
			return "address.firstName.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(firstName)
				|| StringUtils.length(firstName) > MAX_FIELD_LENGTH_140)
		{
			return "address.firstName.invalid.new";
		}

		else if (StringUtils.isEmpty(lastName))
		{
			return "address.lastName.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(lastName)
				|| StringUtils.length(lastName) > MAX_FIELD_LENGTH_140)
		{
			return "address.lastName.invalid.new";
		}
		else if (StringUtils.isEmpty(line1))
		{
			return "address.line1.invalid";
		}
		else if (StringUtils.length(line1) > MAX_FIELD_LENGTH_UPDATED)
		{
			return "address.line1.invalid.length";
		}
		else if (StringUtils.isEmpty(line2))
		{
			return "address.line2.invalid";
		}
		else if (StringUtils.length(line2) > MAX_FIELD_LENGTH_UPDATED)
		{
			return "address.line2.invalid.length";
		}
		else if (StringUtils.isEmpty(townCity) || StringUtils.length(townCity) > MAX_FIELD_LENGTH_UPDATED)
		{
			return "address.townCity.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(townCity))
		{
			return "address.townCity.invalid.alphaAndSpace";
		}
		else if (StringUtils.isEmpty(state) || StringUtils.length(state) > MAX_FIELD_LENGTH_UPDATED)
		{
			return "address.state.invalid";
		}
		else if (StringUtils.isEmpty(mobile) || StringUtils.length(mobile) > MAX_FIELD_LENGTH_10)
		{
			return "address.mobileNumber.invalid";
		}
		else if (!CommonAsciiValidator.validateNumericWithoutSpace(mobile) || StringUtils.length(mobile) != MAX_FIELD_LENGTH_10)
		{
			return "address.mobileNumber.invalid.numeric.length";
		}
		else if (StringUtils.isEmpty(postcode))
		{
			return "address.postcode.invalid";
		}
		else if (!CommonAsciiValidator.validateNumericWithoutSpace(postcode) || StringUtils.length(postcode) != MAX_FIELD_LENGTH_6)
		{
			return "address.postcode.invalid.numeric.length";
		}
		else if (StringUtils.isEmpty(country))
		{
			return "address.country.invalid";
		}
		else if (StringUtils.isEmpty(landmark))
		{
			return "address.line3.invalid";
		}
		return "success";

	}
}