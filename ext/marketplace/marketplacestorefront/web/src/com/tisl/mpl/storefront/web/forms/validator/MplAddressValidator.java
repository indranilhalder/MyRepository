/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
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
	//private static final int MAX_FIELD_LENGTH_UPDATED = 30; //TISUAT-4696
	private static final int MAX_FIELD_LENGTH_UPDATED = 40; //TPR-215

	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";
	private static final String UTF = "UTF-8";

	private static final Logger LOG = Logger.getLogger(MplAddressValidator.class);
	@Autowired
	private MplAccountAddressFacade accountAddressFacade;

	public String validate(final AccountAddressForm addressForm) throws UnsupportedEncodingException
	{
		final String errorMsg = validateStandardFields(addressForm);
		return errorMsg;
	}

	/**
	 * @param addressForm
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	private String validateStandardFields(final AccountAddressForm addressForm) throws UnsupportedEncodingException
	{
		decodeAddressLineValues(addressForm);
		String returnStatement = null;
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
			returnStatement = "address.state.invalid";
		}

		else if (StringUtils.isEmpty(firstName))
		{
			returnStatement = "address.firstName.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithoutSpaceNoSpCh(firstName)
				|| StringUtils.length(firstName) > MAX_FIELD_LENGTH_140)
		{
			returnStatement = "address.firstName.invalid.new";
		}
		else if (StringUtils.isEmpty(lastName))
		{
			returnStatement = "address.lastName.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithoutSpaceNoSpCh(lastName)
				|| StringUtils.length(lastName) > MAX_FIELD_LENGTH_140)
		{
			returnStatement = "address.lastName.invalid.new";
		}

		else if (StringUtils.isEmpty(line1))
		{
			returnStatement = "address.line1.invalid";
		}
		else if (StringUtils.length(line1) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.line1.invalid.length";
		}
		/*
		 * else if (StringUtils.isEmpty(line2)) { returnStatement = "address.line2.invalid"; }
		 */
		else if (StringUtils.length(line2) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.line2.invalid.length";
		}
		/*
		 * else if (StringUtils.isEmpty(line3)) { returnStatement = "address.line3.invalid"; }
		 */
		else if (StringUtils.length(line3) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.line3.invalid.length";
		}

		else if (StringUtils.isEmpty(townCity) || StringUtils.length(townCity) > MAX_FIELD_LENGTH_UPDATED)
		{
			returnStatement = "address.townCity.invalid";
		}
		else if (!CommonAsciiValidator.validateAlphaWithoutSpaceNoSpCh(townCity))
		{
			returnStatement = "address.townCity.invalid.alphaAndSpace";
		}
		else if (StringUtils.isEmpty(state))
		{
			returnStatement = "address.state.invalid";
		}

		else if (StringUtils.isEmpty(mobile))
		{
			returnStatement = "address.mobileNumber.invalid";
		}
		else if (!CommonAsciiValidator.validateNumericWithoutSpace(mobile) || StringUtils.length(mobile) != MAX_FIELD_LENGTH_10)
		{
			returnStatement = "address.mobileNumber.invalid.numeric.length";
		}
		else if (StringUtils.isEmpty(postcode))
		{
			returnStatement = "address.postcode.invalid";
		}
		else if (!CommonAsciiValidator.validateNumericWithoutSpace(postcode) || StringUtils.length(postcode) != MAX_FIELD_LENGTH_6)
		{
			returnStatement = "address.postcode.invalid.numeric.length";
		}
		else if (StringUtils.isEmpty(addressType))
		{
			returnStatement = "address.addressType.select";
		}
		else
		{
			returnStatement = "success";
		}
		return returnStatement;

	}

	/**
	 * @param addressForm
	 * @throws UnsupportedEncodingException
	 */
	private void decodeAddressLineValues(final AccountAddressForm addressForm) throws UnsupportedEncodingException
	{
		try
		{
			final String line1 = java.net.URLDecoder.decode(addressForm.getLine1(), UTF);
			final String line2 = java.net.URLDecoder.decode(addressForm.getLine2(), UTF);
			final String line3 = java.net.URLDecoder.decode(addressForm.getLine3(), UTF);

			addressForm.setLine1(line1);
			addressForm.setLine2(line2);
			addressForm.setLine3(line3);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while decoding address lines ::::" + ex.getMessage());
		}


	}
}
