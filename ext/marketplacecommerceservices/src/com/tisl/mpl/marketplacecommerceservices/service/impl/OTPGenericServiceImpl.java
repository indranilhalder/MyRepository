<<<<<<< HEAD
/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.OTPDao;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;


/**
 * @author TCS
 *
 */


public class OTPGenericServiceImpl implements OTPGenericService
{

	private static final Logger LOG = Logger.getLogger(OTPGenericServiceImpl.class);
	private OTPDao otpDao;
	private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private ModelService modelservice;
	private UserService userService;
	private final String EMAILPATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Calculates the checksum using the credit card algorithm. This algorithm has the advantage that it detects any
	 * single mistyped digit and any single transposition of adjacent digits.
	 *
	 * @param num
	 *           the number to calculate the checksum for
	 * @param digits
	 *           number of significant places in the number
	 *
	 * @return the checksum of num
	 */
	@Override
	public int calcChecksum(long num, int digits)
	{
		try
		{
			boolean doubleDigit = true;
			int total = 0;
			while (0 < digits--)
			{
				int digit = (int) (num % 10);
				num /= 10;
				if (doubleDigit)
				{
					digit = MarketplacecommerceservicesConstants.DOUBLEDIGITS[digit];
				}
				total += digit;
				doubleDigit = !doubleDigit;
			}
			int result = total % 10;
			if (result > 0)
			{
				result = 10 - result;
			}
			return result;
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/**
	 * This method uses the JCE to provide the HMAC-SHA-1 algorithm. HMAC computes a Hashed Message Authentication Code
	 * and in this case SHA1 is the hash algorithm used.
	 *
	 * @param keyBytes
	 *           the bytes to use for the HMAC-SHA-1 key
	 * @param text
	 *           the message or text to be authenticated.
	 *
	 * @throws NoSuchAlgorithmException
	 *            if no provider makes either HmacSHA1 or HMAC-SHA-1 digest algorithms available.
	 * @throws InvalidKeyException
	 *            The secret provided was not a valid HMAC-SHA-1 key.
	 *
	 */
	@Override
	public byte[] hmacSha1(final byte[] keyBytes, final byte[] text) throws NoSuchAlgorithmException, InvalidKeyException
	{
		try
		{
			Mac hmacSha1;
			try
			{
				hmacSha1 = Mac.getInstance("HmacSHA1");
			}
			catch (final NoSuchAlgorithmException nsae)
			{
				hmacSha1 = Mac.getInstance("HMAC-SHA-1");
			}
			final SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
			hmacSha1.init(macKey);
			return hmacSha1.doFinal(text);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/**
	 * This method generates an OTP value for the given set of parameters.
	 *
	 * @param secret
	 *           the shared secret
	 * @param movingFactor
	 *           the counter, time, or other value that changes on a per use basis.
	 * @param codeDigits
	 *           the number of digits in the OTP, not including the checksum, if any.
	 * @param addChecksum
	 *           a flag that indicates if a checksum digit should be appended to the OTP.
	 * @param truncationOffset
	 *           the offset into the MAC result to begin truncation. If this value is out of the range of 0 ... 15, then
	 *           dynamic truncation will be used. Dynamic truncation is when the last 4 bits of the last byte of the MAC
	 *           are used to determine the start offset.
	 * @throws NoSuchAlgorithmException
	 *            if no provider makes either HmacSHA1 or HMAC-SHA-1 digest algorithms available.
	 * @throws InvalidKeyException
	 *            The secret provided was not a valid HMAC-SHA-1 key.
	 *
	 * @return A numeric String in base 10 that includes {@link codeDigits} digits plus the optional checksum digit if
	 *         requested.
	 */
	@Override
	public String otpOutput(final byte[] secret, long movingFactor, final int codeDigits, final boolean addChecksum,
			final int truncationOffset) throws NoSuchAlgorithmException, InvalidKeyException
	{
		try
		{
			// put movingFactor value into text byte array
			String result = null;
			final int digits = addChecksum ? (codeDigits + 1) : codeDigits;
			final byte[] text = new byte[8];
			for (int i = text.length - 1; i >= 0; i--)
			{
				text[i] = (byte) (movingFactor & 0xff);
				movingFactor >>= 8;
			}

			// compute hmac hash
			final byte[] hash = hmacSha1(secret, text);

			// put selected bytes into result int
			int offset = hash[hash.length - 1] & 0xf;
			if ((0 <= truncationOffset) && (truncationOffset < (hash.length - 4)))
			{
				offset = truncationOffset;
			}
			final int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8)
					| (hash[offset + 3] & 0xff);

			int otp = binary % MarketplacecommerceservicesConstants.DIGITS_POWER[codeDigits];
			if (addChecksum)
			{
				otp = (otp * 10) + calcChecksum(otp, codeDigits);
			}
			result = Integer.toString(otp);
			while (result.length() < digits)
			{
				result = "0" + result;
			}
			return result;
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * This method generates random number for OTP
	 *
	 * @param customerId
	 *
	 * @return long
	 *
	 */
	@Override
	public long randomLongNumberGenerattion(final String customerId)
	{
		try
		{
			int sumOfOrderAndUserId = 0;
			double all = 0;

			for (int i = 0; i < customerId.length(); i++)
			{
				sumOfOrderAndUserId = sumOfOrderAndUserId + customerId.charAt(i);
			}
			all = sumOfOrderAndUserId * Math.random();
			return Math.round(all);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/**
	 * This method generates OTP
	 *
	 * @param userIdOrEmail
	 * @param OTPType
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 *
	 */
	@Override
	public String generateOTP(final String userIdOrEmail, final String OTPType, final String mobileNumber)
			throws InvalidKeyException, NoSuchAlgorithmException
	{
		Pattern pattern;
		pattern = Pattern.compile(EMAILPATTERN);
		UserModel user = null;
		final byte a[] = new byte[100];
		final long b = randomLongNumberGenerattion(userIdOrEmail);
		final String otp = (otpOutput(a, b, 6, false, (int) Math.round(Math.random() * 100)));
		LOG.info("Mobile Number====" + mobileNumber);

		final OTPModel otpmodel = getModelservice().create(OTPModel.class);
		try
		{
			user = userService.getUserForUID(userIdOrEmail);
			otpmodel.setCustomerId(user.getPk().toString());
			otpmodel.setEmailId("");
		}
		catch (final UnknownIdentifierException e)
		{
			if (pattern.matcher(userIdOrEmail).matches())
			{
				otpmodel.setEmailId(userIdOrEmail);
				otpmodel.setCustomerId("");
			}
			else
			{
				throw new EtailBusinessExceptions("UnknownIdentifierException", e);
			}
		}
		catch (final ModelSavingException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailBusinessExceptions("ModelSavingException", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}

		LOG.info(" OTP======  " + otp);
		otpmodel.setOTPNumber(otp);
		otpmodel.setOTPType(OTPType);
		otpmodel.setMobileNo(mobileNumber);
		getModelservice().save(otpmodel);
		return otp;
	}

	/**
	 * This method validates OTP
	 *
	 * @param userIdOrEmail
	 * @param enteredOTPNumber
	 * @param OTPType
	 * @param expiryTime
	 * @return boolean
	 *
	 */
	@Override
	public OTPResponseData validateOTP(final String userIdOrEmail, final String mobileNo, final String enteredOTPNumber,
			final OTPTypeEnum OTPType, final long expiryTime)
	{
		List<OTPModel> otplist = null;
		final OTPResponseData otpResponse = new OTPResponseData();

		try
		{
			final UserModel user = userService.getUserForUID(userIdOrEmail);
			otplist = otpDao.fetchOTP(user.getPk().toString(), OTPType);

		}
		catch (final UnknownIdentifierException e)
		{
			otplist = otpDao.fetchOTP(userIdOrEmail, mobileNo, OTPType);
		}
		catch (final ModelSavingException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailBusinessExceptions("ModelSavingException", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}

		if (otplist.size() > 0)
		{
			final OTPModel latestOTP = otplist.get(0);
			LOG.debug("OTP" + latestOTP.getOTPNumber());

			Date currentDate = null;
			if (latestOTP.getOTPNumber().equals(enteredOTPNumber))
			{
				try
				{
					currentDate = dateFormat.parse(getCurrentDate());
				}
				catch (final ParseException e)
				{
					LOG.debug(e);
				}
				final long difference = currentDate.getTime() - latestOTP.getCreationtime().getTime();

				LOG.debug("Time Difference is" + difference);
				if (difference > expiryTime)
				{
					LOG.debug("Otp has expired");
					otpResponse.setOTPValid(Boolean.FALSE);
					otpResponse.setInvalidErrorMessage("EXPIRED");
					return otpResponse;
				}
				else
				{
					LOG.debug("Otp matched!!!!");
					latestOTP.setIsValidated(Boolean.TRUE);
					getModelservice().save(latestOTP);
					otpResponse.setOTPValid(Boolean.TRUE);
					otpResponse.setInvalidErrorMessage("VALID");
					return otpResponse;
				}
			}
			else
			{
				otpResponse.setOTPValid(Boolean.FALSE);
				otpResponse.setInvalidErrorMessage("INVALID");
				return otpResponse;
			}
		}
		else
		{
			otpResponse.setOTPValid(Boolean.FALSE);
			otpResponse.setInvalidErrorMessage("INVALID");
			return otpResponse;
		}
	}


	/**
	 * This method gets the current date
	 *
	 * @return String
	 *
	 */
	private String getCurrentDate()
	{
		final Date today = new Date();
		final String todayDate = dateFormat.format(today);
		return todayDate;
	}


	/**
	 * This method returns the latest OTP against a customer
	 *
	 * @param customerID
	 * @param OTPType
	 * @return String
	 *
	 */
	@Override
	public String getLatestOTP(final String customerID, final OTPTypeEnum OTPType)
	{
		try
		{
			final UserModel user = userService.getUserForUID(customerID);
			final List<OTPModel> otplist = otpDao.fetchOTP(user.getPk().toString(), OTPType);

			if (otplist.size() > 0)
			{
				final OTPModel latestOTP = otplist.get(0);
				LOG.debug("OTP" + latestOTP.getOTPNumber());
				return latestOTP.getOTPNumber();
			}
			else
			{
				return null;
			}
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService#getLatestOTPModel(java.lang.String,
	 * com.tisl.mpl.enums.OTPTypeEnum)
	 */
	@Override
	public OTPModel getLatestOTPModel(final String customerID, final OTPTypeEnum OTPType)
	{
		try
		{
			final UserModel user = userService.getUserForUID(customerID);
			final List<OTPModel> otplist = otpDao.fetchOTP(user.getPk().toString(), OTPType);

			if (otplist.size() > 0)
			{
				final OTPModel latestOTP = otplist.get(0);
				LOG.debug("OTP" + latestOTP.getOTPNumber());
				return latestOTP;
			}
			else
			{
				return null;
			}
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	//Getters and setters
	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the modelservice
	 */
	public ModelService getModelservice()
	{
		return modelservice;
	}

	/**
	 * @param modelservice
	 *           the modelservice to set
	 */
	@Required
	public void setModelservice(final ModelService modelservice)
	{
		this.modelservice = modelservice;
	}

	/**
	 * @return the otpDao
	 */
	public OTPDao getOtpDao()
	{
		return otpDao;
	}

	/**
	 * @param otpDao
	 *           the otpDao to set
	 */
	@Required
	public void setOtpDao(final OTPDao otpDao)
	{
		this.otpDao = otpDao;
	}

	/**
	 * @return the dateFormat
	 */
	public DateFormat getDateFormat()
	{
		return dateFormat;
	}

	/**
	 * @return the eMAILPATTERN
	 */
	public String getEMAILPATTERN()
	{
		return EMAILPATTERN;
	}



}
=======
/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.OTPDao;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;


/**
 * @author TCS
 *
 */


public class OTPGenericServiceImpl implements OTPGenericService
{

	private static final Logger LOG = Logger.getLogger(OTPGenericServiceImpl.class);
	private OTPDao otpDao;
	private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	private static final String OTP_ENABLED_STRING = "marketplace.otp.enabled";
	private ConfigurationService configurationService;
	private ModelService modelservice;
	private UserService userService;
	private final String EMAILPATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Calculates the checksum using the credit card algorithm. This algorithm has the advantage that it detects any
	 * single mistyped digit and any single transposition of adjacent digits.
	 *
	 * @param num
	 *           the number to calculate the checksum for
	 * @param digits
	 *           number of significant places in the number
	 *
	 * @return the checksum of num
	 */
	@Override
	public int calcChecksum(long num, int digits)
	{
		try
		{
			boolean doubleDigit = true;
			int total = 0;
			while (0 < digits--)
			{
				int digit = (int) (num % 10);
				num /= 10;
				if (doubleDigit)
				{
					digit = MarketplacecommerceservicesConstants.DOUBLEDIGITS[digit];
				}
				total += digit;
				doubleDigit = !doubleDigit;
			}
			int result = total % 10;
			if (result > 0)
			{
				result = 10 - result;
			}
			return result;
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/**
	 * This method uses the JCE to provide the HMAC-SHA-1 algorithm. HMAC computes a Hashed Message Authentication Code
	 * and in this case SHA1 is the hash algorithm used.
	 *
	 * @param keyBytes
	 *           the bytes to use for the HMAC-SHA-1 key
	 * @param text
	 *           the message or text to be authenticated.
	 *
	 * @throws NoSuchAlgorithmException
	 *            if no provider makes either HmacSHA1 or HMAC-SHA-1 digest algorithms available.
	 * @throws InvalidKeyException
	 *            The secret provided was not a valid HMAC-SHA-1 key.
	 *
	 */
	@Override
	public byte[] hmacSha1(final byte[] keyBytes, final byte[] text) throws NoSuchAlgorithmException, InvalidKeyException
	{
		try
		{
			Mac hmacSha1;
			try
			{
				hmacSha1 = Mac.getInstance("HmacSHA1");
			}
			catch (final NoSuchAlgorithmException nsae)
			{
				hmacSha1 = Mac.getInstance("HMAC-SHA-1");
			}
			final SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
			hmacSha1.init(macKey);
			return hmacSha1.doFinal(text);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/**
	 * This method generates an OTP value for the given set of parameters.
	 *
	 * @param secret
	 *           the shared secret
	 * @param movingFactor
	 *           the counter, time, or other value that changes on a per use basis.
	 * @param codeDigits
	 *           the number of digits in the OTP, not including the checksum, if any.
	 * @param addChecksum
	 *           a flag that indicates if a checksum digit should be appended to the OTP.
	 * @param truncationOffset
	 *           the offset into the MAC result to begin truncation. If this value is out of the range of 0 ... 15, then
	 *           dynamic truncation will be used. Dynamic truncation is when the last 4 bits of the last byte of the MAC
	 *           are used to determine the start offset.
	 * @throws NoSuchAlgorithmException
	 *            if no provider makes either HmacSHA1 or HMAC-SHA-1 digest algorithms available.
	 * @throws InvalidKeyException
	 *            The secret provided was not a valid HMAC-SHA-1 key.
	 *
	 * @return A numeric String in base 10 that includes {@link codeDigits} digits plus the optional checksum digit if
	 *         requested.
	 */
	@Override
	public String otpOutput(final byte[] secret, long movingFactor, final int codeDigits, final boolean addChecksum,
			final int truncationOffset) throws NoSuchAlgorithmException, InvalidKeyException
	{
		try
		{
			// put movingFactor value into text byte array
			String result = null;
			final int digits = addChecksum ? (codeDigits + 1) : codeDigits;
			final byte[] text = new byte[8];
			for (int i = text.length - 1; i >= 0; i--)
			{
				text[i] = (byte) (movingFactor & 0xff);
				movingFactor >>= 8;
			}

			// compute hmac hash
			final byte[] hash = hmacSha1(secret, text);

			// put selected bytes into result int
			int offset = hash[hash.length - 1] & 0xf;
			if ((0 <= truncationOffset) && (truncationOffset < (hash.length - 4)))
			{
				offset = truncationOffset;
			}
			final int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8)
					| (hash[offset + 3] & 0xff);

			int otp = binary % MarketplacecommerceservicesConstants.DIGITS_POWER[codeDigits];
			if (addChecksum)
			{
				otp = (otp * 10) + calcChecksum(otp, codeDigits);
			}
			result = Integer.toString(otp);
			while (result.length() < digits)
			{
				result = "0" + result;
			}
			return result;
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * This method generates random number for OTP
	 *
	 * @param customerId
	 *
	 * @return long
	 *
	 */
	@Override
	public long randomLongNumberGenerattion(final String customerId)
	{
		try
		{
			int sumOfOrderAndUserId = 0;
			double all = 0;

			for (int i = 0; i < customerId.length(); i++)
			{
				sumOfOrderAndUserId = sumOfOrderAndUserId + customerId.charAt(i);
			}
			all = sumOfOrderAndUserId * Math.random();
			return Math.round(all);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/**
	 * This method generates OTP
	 *
	 * @param userIdOrEmail
	 * @param OTPType
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 *
	 */
	@Override
	public String generateOTP(final String userIdOrEmail, final String OTPType, final String mobileNumber)
			throws InvalidKeyException, NoSuchAlgorithmException
	{
		Pattern pattern;
		pattern = Pattern.compile(EMAILPATTERN);
		UserModel user = null;
		final byte a[] = new byte[100];
		final long b = randomLongNumberGenerattion(userIdOrEmail);
		final String otp = (otpOutput(a, b, 6, false, (int) Math.round(Math.random() * 100)));
		LOG.info("Mobile Number====" + mobileNumber);

		final OTPModel otpmodel = getModelservice().create(OTPModel.class);
		try
		{
			user = userService.getUserForUID(userIdOrEmail);
			otpmodel.setCustomerId(user.getPk().toString());
			otpmodel.setEmailId("");
		}
		catch (final UnknownIdentifierException e)
		{
			if (pattern.matcher(userIdOrEmail).matches())
			{
				otpmodel.setEmailId(userIdOrEmail);
				otpmodel.setCustomerId("");
			}
			else
			{
				throw new EtailBusinessExceptions("UnknownIdentifierException", e);
			}
		}
		catch (final ModelSavingException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailBusinessExceptions("ModelSavingException", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}

		LOG.info(" OTP======  " + otp);
		otpmodel.setOTPNumber(otp);
		otpmodel.setOTPType(OTPType);
		otpmodel.setMobileNo(mobileNumber);
		getModelservice().save(otpmodel);
		return otp;
	}

	/**
	 * This method validates OTP
	 *
	 * @param userIdOrEmail
	 * @param enteredOTPNumber
	 * @param OTPType
	 * @param expiryTime
	 * @return boolean
	 *
	 */
	@Override
	public OTPResponseData validateOTP(final String userIdOrEmail, final String mobileNo, final String enteredOTPNumber,
			final OTPTypeEnum OTPType, final long expiryTime)
	{
		List<OTPModel> otplist = null;
		final OTPResponseData otpResponse = new OTPResponseData();

		try
		{
			if (getConfigurationService().getConfiguration().getBoolean(OTP_ENABLED_STRING, true))
			{
				final UserModel user = userService.getUserForUID(userIdOrEmail);
				otplist = otpDao.fetchOTP(user.getPk().toString(), OTPType);
			}
			else
			{
				otpResponse.setOTPValid(Boolean.TRUE);
				otpResponse.setInvalidErrorMessage("VALID");
				return otpResponse;
			}
		}
		catch (final UnknownIdentifierException e)
		{
			otplist = otpDao.fetchOTP(userIdOrEmail, mobileNo, OTPType);
		}
		catch (final ModelSavingException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailBusinessExceptions("ModelSavingException", ex);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}

		if (otplist.size() > 0)
		{
			final OTPModel latestOTP = otplist.get(0);
			LOG.debug("OTP" + latestOTP.getOTPNumber());

			Date currentDate = null;
			if (latestOTP.getOTPNumber().equals(enteredOTPNumber))
			{
				try
				{
					currentDate = dateFormat.parse(getCurrentDate());
				}
				catch (final ParseException e)
				{
					LOG.debug(e);
				}
				final long difference = currentDate.getTime() - latestOTP.getCreationtime().getTime();

				LOG.debug("Time Difference is" + difference);
				if (difference > expiryTime)
				{
					LOG.debug("Otp has expired");
					otpResponse.setOTPValid(Boolean.FALSE);
					otpResponse.setInvalidErrorMessage("EXPIRED");
					return otpResponse;
				}
				else
				{
					LOG.debug("Otp matched!!!!");
					latestOTP.setIsValidated(Boolean.TRUE);
					getModelservice().save(latestOTP);
					otpResponse.setOTPValid(Boolean.TRUE);
					otpResponse.setInvalidErrorMessage("VALID");
					return otpResponse;
				}
			}
			else
			{
				otpResponse.setOTPValid(Boolean.FALSE);
				otpResponse.setInvalidErrorMessage("INVALID");
				return otpResponse;
			}
		}
		else
		{
			otpResponse.setOTPValid(Boolean.FALSE);
			otpResponse.setInvalidErrorMessage("INVALID");
			return otpResponse;
		}
	}


	/**
	 * This method gets the current date
	 *
	 * @return String
	 *
	 */
	private String getCurrentDate()
	{
		final Date today = new Date();
		final String todayDate = dateFormat.format(today);
		return todayDate;
	}


	/**
	 * This method returns the latest OTP against a customer
	 *
	 * @param customerID
	 * @param OTPType
	 * @return String
	 *
	 */
	@Override
	public String getLatestOTP(final String customerID, final OTPTypeEnum OTPType)
	{
		try
		{
			final UserModel user = userService.getUserForUID(customerID);
			final List<OTPModel> otplist = otpDao.fetchOTP(user.getPk().toString(), OTPType);

			if (otplist.size() > 0)
			{
				final OTPModel latestOTP = otplist.get(0);
				LOG.debug("OTP" + latestOTP.getOTPNumber());
				return latestOTP.getOTPNumber();
			}
			else
			{
				return null;
			}
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService#getLatestOTPModel(java.lang.String,
	 * com.tisl.mpl.enums.OTPTypeEnum)
	 */
	@Override
	public OTPModel getLatestOTPModel(final String customerID, final OTPTypeEnum OTPType)
	{
		try
		{
			final UserModel user = userService.getUserForUID(customerID);
			final List<OTPModel> otplist = otpDao.fetchOTP(user.getPk().toString(), OTPType);

			if (otplist.size() > 0)
			{
				final OTPModel latestOTP = otplist.get(0);
				LOG.debug("OTP" + latestOTP.getOTPNumber());
				return latestOTP;
			}
			else
			{
				return null;
			}
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	//Getters and setters
	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the modelservice
	 */
	public ModelService getModelservice()
	{
		return modelservice;
	}

	/**
	 * @param modelservice
	 *           the modelservice to set
	 */
	@Required
	public void setModelservice(final ModelService modelservice)
	{
		this.modelservice = modelservice;
	}

	/**
	 * @return the otpDao
	 */
	public OTPDao getOtpDao()
	{
		return otpDao;
	}

	/**
	 * @param otpDao
	 *           the otpDao to set
	 */
	@Required
	public void setOtpDao(final OTPDao otpDao)
	{
		this.otpDao = otpDao;
	}

	/**
	 * @return the dateFormat
	 */
	public DateFormat getDateFormat()
	{
		return dateFormat;
	}

	/**
	 * @return the eMAILPATTERN
	 */
	public String getEMAILPATTERN()
	{
		return EMAILPATTERN;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}



}
>>>>>>> BRANCH_TCS-HYCOMM-R1PS-BN-38
