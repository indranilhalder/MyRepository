/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.storefront.web.forms.AccountAddressForm;


/**
 * @author @TCS
 *
 *
 *         Validator for address forms. Enforces the order of validation
 *
 */
@Component("accountAddressValidator")
public class AccountAddressValidator implements Validator
{

	private static final int MAX_FIELD_LENGTH = 255;
	private static final int MAX_POSTCODE_LENGTH = 10;
	private static final int MAX_FIELD_LENGTH_NEW = 140;
	//private static final int MAX_FIELD_LENGTH_ADDLINE = 40;
	private static final int MAX_FIELD_LENGTH_UPDATED = 30; //TISUAT-4696
	private static final int MAX_FIELD_LENGTH_STATE = 20;
	private static final int MAX_FIELD_LENGTH_COUNTRY = 15;
	public static final String MOBILE_REGEX = "^[0-9]*$";
	public static final String NAME_REGEX = "[a-zA-Z]+\\.?";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return AccountAddressForm.class.equals(aClass);
	}

	/**
	 * @description method is called to validate the AccountAddressForm
	 */
	@Override
	public void validate(final Object object, final Errors errors)
	{
		final AccountAddressForm addressForm = (AccountAddressForm) object;
		validateStandardFields(addressForm, errors);
		validateCountrySpecificFields(addressForm, errors);
		//	addressValidator(addressForm, errors);
	}

	/**
	 * @description method is called to validate the validateStandardFields
	 * @param addressForm
	 * @param errors
	 */
	protected void validateStandardFields(final AccountAddressForm addressForm, final Errors errors)
	{
		validateStringField(addressForm.getCountryIso(), AddressField.COUNTRY, MAX_FIELD_LENGTH_COUNTRY, errors);
		validateStringField(addressForm.getFirstName(), AddressField.FIRSTNAME, MAX_FIELD_LENGTH_NEW, errors);
		validateStringField(addressForm.getLastName(), AddressField.LASTNAME, MAX_FIELD_LENGTH_NEW, errors);
		validateStringField(addressForm.getLine1(), AddressField.LINE1, MAX_FIELD_LENGTH_UPDATED, errors);
		//		validateStringField(addressForm.getLine2(), AddressField.LINE2, MAX_FIELD_LENGTH_ADDLINE, errors);
		//		validateStringField(addressForm.getLine3(), AddressField.LINE3, MAX_FIELD_LENGTH_ADDLINE, errors);
		validateStringField(addressForm.getTownCity(), AddressField.TOWN, MAX_FIELD_LENGTH_UPDATED, errors);
		validateStringField(addressForm.getPostcode(), AddressField.POSTCODE, MAX_POSTCODE_LENGTH, errors);
		validateStringField(addressForm.getAddressType(), AddressField.ADDRESSTYPE, MAX_FIELD_LENGTH, errors);
		validateStringField(addressForm.getState(), AddressField.STATE, MAX_FIELD_LENGTH_STATE, errors);
		validateStringField(addressForm.getMobileNo(), AddressField.MOBILE, MAX_POSTCODE_LENGTH, errors);
		//		validateStringField(addressForm.getLocality(), AddressField.LOCALITY, MAX_FIELD_LENGTH_NEW, errors);
	}

	/**
	 * @description method is called to validate the CountrySpecificFields
	 * @param addressForm
	 * @param errors
	 */
	protected void validateCountrySpecificFields(final AccountAddressForm addressForm, final Errors errors)
	{
		final String isoCode = addressForm.getCountryIso();
		if (isoCode != null)
		{
			switch (CountryCode.lookup(isoCode))
			{
				case CHINA:
					validateFieldNotNull(addressForm.getRegionIso(), AddressField.REGION, errors);
					break;
				case CANADA:
					validateFieldNotNull(addressForm.getRegionIso(), AddressField.REGION, errors);
					break;
				case USA:
					validateFieldNotNull(addressForm.getRegionIso(), AddressField.REGION, errors);
					break;
				case JAPAN:
					validateStringField(addressForm.getLine2(), AddressField.LINE2, MAX_FIELD_LENGTH, errors);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * @description method is called to validate the StringField
	 * @param addressField
	 * @param fieldType
	 * @param maxFieldLength
	 * @param errors
	 */
	protected static void validateStringField(final String addressField, final AddressField fieldType, final int maxFieldLength,
			final Errors errors)
	{
		if (addressField == null || StringUtils.isEmpty(addressField) || (StringUtils.length(addressField) > maxFieldLength))
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}

	protected static void validateFieldNotNull(final String addressField, final AddressField fieldType, final Errors errors)
	{
		if (addressField == null)
		{
			errors.rejectValue(fieldType.getFieldKey(), fieldType.getErrorKey());
		}
	}

	protected enum CountryCode
	{
		USA("US"), CANADA("CA"), JAPAN("JP"), CHINA("CN"), BRITAIN("GB"), GERMANY("DE"), DEFAULT("");

		private final String isoCode;

		private static Map<String, CountryCode> lookupMap = new HashMap<String, CountryCode>();
		static
		{
			for (final CountryCode code : CountryCode.values())
			{
				lookupMap.put(code.getIsoCode(), code);
			}
		}

		private CountryCode(final String isoCodeStr)
		{
			this.isoCode = isoCodeStr;
		}

		public static CountryCode lookup(final String isoCodeStr)
		{
			CountryCode code = lookupMap.get(isoCodeStr);
			if (code == null)
			{
				code = DEFAULT;
			}
			return code;
		}

		public String getIsoCode()
		{
			return isoCode;
		}
	}

	protected enum AddressField
	{
		FIRSTNAME("firstName", "address.firstName.invalid"), LASTNAME("lastName", "address.lastName.invalid"), LINE1("line1",
				"address.line1.invalid"), LINE2("line2", "address.line2.invalid"), LINE3("line3", "address.line3.invalid"), TOWN(
				"townCity", "address.townCity.invalid"), POSTCODE("postcode", "address.postcode.invalid"), REGION("regionIso",
				"address.regionIso.invalid"), COUNTRY("countryIso", "address.country.invalid"), ADDRESSTYPE("addressType",
				"address.addressType.invalid"), STATE("state", "address.selectState"), LOCALITY("locality",
				"address.locality.invalid"), MOBILE("mobileNo", "address.mobile.invalid");


		private final String fieldKey;
		private final String errorKey;

		private AddressField(final String fieldKey, final String errorKey)
		{
			this.fieldKey = fieldKey;
			this.errorKey = errorKey;
		}

		public String getFieldKey()
		{
			return fieldKey;
		}

		public String getErrorKey()
		{
			return errorKey;
		}
	}

	/**
	 * @description method is called to validate the fields of AddressForm
	 * @param addressForm
	 * @param errors
	 */
	//	public void addressValidator(final AccountAddressForm addressForm, final Errors errors)
	//	{
	//		final String firstName = addressForm.getFirstName();
	//		final String lastName = addressForm.getLastName();
	//		final String country = addressForm.getCountryIso();
	//		final String postcode = addressForm.getPostcode();
	//		final String line1 = addressForm.getLine1();
	//		final String line2 = addressForm.getLine2();
	//		final String line3 = addressForm.getLine3();
	//		final String townCity = addressForm.getTownCity();
	//		final String state = addressForm.getState();
	//		final String mobile = addressForm.getMobileNo();
	//		final String addressType = addressForm.getAddressType();
	//		final String locality = addressForm.getLocality();
	//
	//		if (null != firstName && !(MarketplacecommerceservicesConstants.EMPTY).equals(firstName)
	//				&& ((StringUtils.length(firstName) > 140)) && !firstName.matches(NAME_REGEX))
	//
	//		{
	//			errors.rejectValue("firstName", "address.firstName.invalid");
	//		}
	//
	//		if (null != lastName && !(MarketplacecommerceservicesConstants.EMPTY).equals(lastName)
	//				&& (StringUtils.length(lastName) > 40) && !lastName.matches(NAME_REGEX))
	//
	//		{
	//			errors.rejectValue("lastName", "address.lastName.invalid");
	//		}
	//
	//		if (null != country && !(MarketplacecommerceservicesConstants.EMPTY).equals(country) && (StringUtils.length(lastName) > 15))
	//
	//		{
	//			errors.rejectValue("countryIso", "address.country.invalid");
	//		}
	//
	//		if ((null != postcode) && (StringUtils.length(postcode) > 6 || StringUtils.length(postcode) < 6)
	//				&& !(MarketplacecommerceservicesConstants.EMPTY).equals(postcode))
	//
	//		{
	//			errors.rejectValue("postcode", "address.postcode.invalid");
	//		}
	//
	//		if ((null != line1) && (StringUtils.length(line1) > 40) && !(MarketplacecommerceservicesConstants.EMPTY).equals(line1))
	//		{
	//			errors.rejectValue("line1", "address.line1.invalid");
	//		}
	//		if ((null != line2) && (StringUtils.length(line2) > 40) && !(MarketplacecommerceservicesConstants.EMPTY).equals(line2))
	//		{
	//			errors.rejectValue("line2", "address.line2.invalid");
	//		}
	//
	//		if ((null != line3) && (StringUtils.length(line3) > 40) && !(MarketplacecommerceservicesConstants.EMPTY).equals(line3))
	//		{
	//			errors.rejectValue("line3", "address.line3.invalid");
	//		}
	//
	//		if (null != townCity && !(MarketplacecommerceservicesConstants.EMPTY).equals(townCity)
	//				&& (StringUtils.length(townCity) > 40))
	//
	//		{
	//			errors.rejectValue("townCity", "address.townCity.invalid");
	//		}
	//
	//		if (null != state && !(MarketplacecommerceservicesConstants.EMPTY).equals(state) && (StringUtils.length(state) > 20))
	//		{
	//			errors.rejectValue("state", "address.selectState");
	//		}
	//		if (null != addressType && !(MarketplacecommerceservicesConstants.EMPTY).equals(addressType))
	//
	//		{
	//			errors.rejectValue("addressType", "addressType");
	//		}
	//
	//		if (null != locality && !(MarketplacecommerceservicesConstants.EMPTY).equals(locality)
	//				&& (StringUtils.length(locality) > 140))
	//
	//		{
	//			errors.rejectValue("locality", "address.locality.invalid");
	//		}
	//
	//		if (null != mobile && !(MarketplacecommerceservicesConstants.EMPTY).equals(mobile) && (!mobile.matches(MOBILE_REGEX))
	//				&& (StringUtils.length(mobile) > 10))
	//
	//		{
	//			errors.rejectValue("mobileNo", "address.mobileNumber.invalid");
	//		}
	//	}
}
