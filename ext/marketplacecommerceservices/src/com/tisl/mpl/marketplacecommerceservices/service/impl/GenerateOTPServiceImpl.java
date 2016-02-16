/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.GenerateOTPDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.GenerateOTPService;


/**
 * @author TCS
 *
 */
public class GenerateOTPServiceImpl implements GenerateOTPService
{

	@Autowired
	private GenerateOTPDao generateOTPDao;
	private static final Logger LOG = Logger.getLogger(GenerateOTPServiceImpl.class);
	private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	@Resource
	private ModelService modelService;
	@Resource
	private ExtendedUserService extendedUserService;


	/**
	 * @return the generateOTPDao
	 */
	public GenerateOTPDao getGenerateOTPDao()
	{
		return generateOTPDao;
	}

	/**
	 * @param generateOTPDao
	 *           the generateOTPDao to set
	 */
	public void setGenerateOTPDao(final GenerateOTPDao generateOTPDao)
	{
		this.generateOTPDao = generateOTPDao;
	}

	private static final int[] doubleDigits =
	{ 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 };
	private static final int[] DIGITS_POWER =
	{ 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };

	/**
	 * @description method is called to calculate the Checksum of num and digits return int
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
					digit = doubleDigits[digit];
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
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to get hmacSha1
	 */
	@Override
	public byte[] hmacSha1(final byte[] keyBytes, final byte[] text) throws NoSuchAlgorithmException, InvalidKeyException
	{
		try
		{
			Mac hmacSha1;
			try
			{
				hmacSha1 = Mac.getInstance(MarketplacecommerceservicesConstants.HMAC_SHA1);
			}
			catch (final NoSuchAlgorithmException nsae)
			{
				hmacSha1 = Mac.getInstance(MarketplacecommerceservicesConstants.HMAC_SHA_1);
			}
			final SecretKeySpec macKey = new SecretKeySpec(keyBytes, MarketplacecommerceservicesConstants.RAW);
			hmacSha1.init(macKey);
			return hmacSha1.doFinal(text);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to get otp
	 */
	@Override
	public String otpOutput(final byte[] secret, long movingFactor, final int codeDigits, final boolean addChecksum,
			final int truncationOffset) throws NoSuchAlgorithmException, InvalidKeyException
	{
		try
		{
			String result = null;
			final int digits = addChecksum ? (codeDigits + 1) : codeDigits;
			final byte[] text = new byte[8];
			for (int i = text.length - 1; i >= 0; i--)
			{
				text[i] = (byte) (movingFactor & 0xff);
				movingFactor >>= 8;
			}

			final byte[] hash = hmacSha1(secret, text);
			int offset = hash[hash.length - 1] & 0xf;
			if ((0 <= truncationOffset) && (truncationOffset < (hash.length - 4)))
			{
				offset = truncationOffset;
			}
			final int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8)
					| (hash[offset + 3] & 0xff);

			int otp = binary % DIGITS_POWER[codeDigits];
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
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called for generation of random Long Number
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
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to generate the OTPNumber return String
	 */
	@Override
	public String generateOTPNumber(final String custId, final String OTPType, final String emailId) throws InvalidKeyException,
			NoSuchAlgorithmException
	{
		try
		{
			final byte a[] = new byte[100];
			final long b = randomLongNumberGenerattion(custId);
			final String otp = (otpOutput(a, b, 6, false, (int) Math.round(Math.random() * 100)));
			LOG.warn(MarketplacecommerceservicesConstants.OTP + otp);
			final OTPModel otpmodel = getModelService().create(OTPModel.class);
			final UserModel user = getExtendedUserService().getUserForUID(emailId);

			if (user != null)

			{
				otpmodel.setCustomerId(user.getPk().toString());
			}
			otpmodel.setOTPNumber(otp);
			otpmodel.setOTPType(OTPType);
			getModelService().save(otpmodel);

			return otp;
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to get Current Date
	 * @return String
	 */
	private String getCurrentDate()
	{
		final Date today = new Date();
		final String todayDate = dateFormat.format(today);
		return todayDate;
	}

	/**
	 * @description method is called to validate the OTPNumber return boolean
	 */
	@Override
	public boolean validaterOTP(final String uid, final String enteredOTPNumber, final OTPTypeEnum oTPType, final long expiryTime)
	{
		try
		{
			final UserModel user = getExtendedUserService().getUserForUID(uid);
			if (user != null)
			{
				final List<OTPModel> otplist = generateOTPDao.fetchOTP(user.getPk().toString(), oTPType);
				if (otplist.isEmpty() && otplist.size() > 0)
				{
					final OTPModel latestOTP = otplist.get(0);
					Date currentDate = null;
					if (null != latestOTP && latestOTP.getOTPNumber().equals(enteredOTPNumber))
					{
						try
						{
							currentDate = dateFormat.parse(getCurrentDate());
						}
						catch (final ParseException e)
						{
							throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0014);
						}
						final long difference = currentDate.getTime() - latestOTP.getCreationtime().getTime();
						if (difference > expiryTime)
						{
							LOG.warn(MarketplacecommerceservicesConstants.OTP_EXPIRED);
							return false;
						}
						else
						{
							LOG.warn(MarketplacecommerceservicesConstants.OTP_MATCHED);
							latestOTP.setIsValidated(Boolean.TRUE);
							getModelService().save(latestOTP);
							return true;
						}
					}
				}
			}
			return false;
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to generate the TempPassword return String
	 */
	@Override
	public String generateTempPassword(final String custId) throws InvalidKeyException, NoSuchAlgorithmException
	{
		try
		{
			final byte a[] = new byte[100];
			final long b = randomLongNumberGenerattion(custId);
			final String otp = (otpOutput(a, b, 6, false, (int) Math.round(Math.random() * 100)));
			LOG.warn(MarketplacecommerceservicesConstants.OTP + otp);

			return otp;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}

	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
	}
}
