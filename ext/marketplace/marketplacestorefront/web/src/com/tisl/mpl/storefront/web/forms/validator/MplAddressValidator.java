/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.storefront.businessvalidator.CommonAsciiValidator;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;


/**
 * @author @TCS
 *
 *
 *         Validator for address forms. Enforces the order of validation
 *
 */
@Component("mplAddressValidator")
public class MplAddressValidator
{
	private static final int MAX_FIELD_LENGTH_140 = 140;
	//private static final int MAX_FIELD_LENGTH_100 = 100;
	//private static final int MAX_FIELD_LENGTH_40 = 40;
	private static final int MAX_FIELD_LENGTH_10 = 10;
	private static final int MAX_FIELD_LENGTH_6 = 6;
	private static final int MAX_FIELD_LENGTH_UPDATED = 30; //TISUAT-4696

	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";

	@Autowired
	private AccountAddressFacade accountAddressFacade;

	public String validate(final AccountAddressForm addressForm)
	{
		final String errorMsg = validateStandardFields(addressForm);
		return errorMsg;
	}

	/**
	 * @param addressForm
	 * @return String
	 */
	private String validateStandardFields(final AccountAddressForm addressForm)
	{
		final List<StateData> stateDataList = accountAddressFacade.getStates();
		boolean validState = false;
		final String firstName = addressForm.getFirstName();
		final String lastName = addressForm.getLastName();
		final String line1 = addressForm.getLine1();
		final String line2 = addressForm.getLine2();
		final String line3 = addressForm.getLine3();
		final String townCity = addressForm.getTownCity();
		final String state = addressForm.getState();
		final String mobile = addressForm.getMobileNo();
		final String postcode = addressForm.getPostcode();
		final String addressType = addressForm.getAddressType();

		for (final StateData stateData : stateDataList)
		{
			if (addressForm.getState() != null && stateData.getName().equalsIgnoreCase(addressForm.getState()))
			{
				validState = true;
				break;
			}
		}

		if (!validState)
		{
			return "address.state.invalid";
		}

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
		else if (StringUtils.isEmpty(line3))
		{
			return "address.line3.invalid";
		}
		else if (StringUtils.length(line3) > MAX_FIELD_LENGTH_UPDATED)
		{
			return "address.line3.invalid.length";
		}

		else if (StringUtils.isEmpty(townCity) || StringUtils.length(townCity) > MAX_FIELD_LENGTH_UPDATED)
		{
			return "address.townCity.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithSpaceNoSpCh(townCity))
		{
			return "address.townCity.invalid.alphaAndSpace";
		}
		else if (StringUtils.isEmpty(state))
		{
			return "address.state.invalid";
		}

		else if (StringUtils.isEmpty(mobile))
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
		else if (StringUtils.isEmpty(addressType))
		{
			return "address.addressType.select";
		}
		else
		{
			return "success";
		}
	}

}
