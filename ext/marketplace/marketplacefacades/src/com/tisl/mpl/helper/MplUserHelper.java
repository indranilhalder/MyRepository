/**
 *
 */
package com.tisl.mpl.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.DomainValidator;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.wsdto.MplUserResultWsDto;


/**
 * @author TCS
 *
 */
public class MplUserHelper
{

	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*&.]).{8,16})";
	public static final String EMAIL_SYMBOL = "@";
	public static final int MAX_EMAIL_LENGTH = 240;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final int MAX_PASSWORD_LENGTH = 16;


	/**
	 * all email related validations used by login and registration
	 *
	 * @param login
	 * @return MplUserResultWsDto
	 */
	public MplUserResultWsDto validateEmail(final String login) throws EtailBusinessExceptions
	{
		final MplUserResultWsDto validatedResult = new MplUserResultWsDto();
		if (StringUtils.isEmpty(login))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9012);
		}

		if ((StringUtils.length(login) > MAX_EMAIL_LENGTH || !validateEmailAddress(login)))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9011);
		}
		else if (!validDomain(login))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9012);
		}
		else
		{
			validatedResult.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		return validatedResult;
	}

	/**
	 * Validating registration details from mobile before registration
	 *
	 * @param login
	 * @param password
	 * @return MplUserResultWsDto
	 */

	public MplUserResultWsDto validateRegistrationData(final String login, final String password) throws EtailBusinessExceptions
	{
		MplUserResultWsDto validatedResult = new MplUserResultWsDto();
		try
		{
			validatedResult = validateEmail(login);
		}
		catch (final EtailBusinessExceptions businessException)
		{
			//Throw exception when the input details are invalid
			throw businessException;
		}
		if ((StringUtils.isEmpty(password) || StringUtils.length(password) < MIN_PASSWORD_LENGTH))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9008);
		}
		else if (!validatePasswordPolicy(password))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9010);
		}

		else if (StringUtils.length(password) > MAX_PASSWORD_LENGTH)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9009);
		}
		else if (checkWhiteSpace(password))
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9014);
		}
		else
		{
			validatedResult.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		return validatedResult;
	}

	/**
	 * Validating email address
	 *
	 * @param email
	 * @return boolean
	 */
	public boolean validateEmailAddress(final String email)
	{
		final Pattern pattern = Pattern.compile(EMAIL_REGEX);
		final Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * Validating password pattern
	 *
	 * @param password
	 * @return boolean
	 */
	public boolean validatePasswordPolicy(final String password)
	{
		final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		final Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * Validating domain
	 *
	 * @param email
	 * @return boolean
	 */
	public boolean validDomain(final String email)
	{
		final DomainValidator domainvalidator = DomainValidator.getInstance();
		final int index = email.indexOf(EMAIL_SYMBOL);
		final String substringEmail = email.substring(index + 1);
		final boolean valid = domainvalidator.isValid(substringEmail);
		return valid;
	}

	/**
	 * Validating that password should not contain any space
	 *
	 * @param password
	 * @return boolean
	 */
	private boolean checkWhiteSpace(final String password)
	{
		char literal;
		if (password.length() > 0)
		{
			for (int i = 0; i < password.length(); i++)
			{
				literal = password.charAt(i);
				if (literal == 32)
				{
					return true;
				}
			}
		}
		return false;
	}

}
