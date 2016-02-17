/**
 *
 */
package com.tisl.mpl.facades.account.register.impl;

import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.ForgetPasswordFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.ForgetPasswordService;
import com.tisl.mpl.marketplacecommerceservices.service.GenerateOTPService;



/**
 * @author TCS
 *
 */
public class ForgetPasswordFacadeImpl implements ForgetPasswordFacade
{
	@Autowired
	private ForgetPasswordService forgetPasswordService;
	@Autowired
	private ExtendedUserService userService;
	@Autowired
	private PasswordEncoderService passwordEncoderService;
	@Autowired
	private GenerateOTPService genotpservice;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CommonI18NService commonI18NService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private ConfigurationService configurationService;

	private static final String PASSWORDENCODING = "default.password.encoder.key";


	/**
	 * @description method is called to get the details of the Current Customer
	 * @param email
	 * @return CustomerModel
	 */

	private CustomerModel getCurrentCustomerByEmail(final String email)
	{
		try
		{
			CustomerModel customerModel = new CustomerModel();
			List<CustomerModel> myList = null;
			myList = forgetPasswordService.getCustomer(email);
			if (null != myList && !myList.isEmpty())
			{
				customerModel = myList.get(0);
			}
			return customerModel;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description method is called to change the Password of the customer through Email
	 */
	@Override
	public void forgottenPasswordForEmail(final String uid, final String securePasswordUrl)
	{
		try
		{
			Assert.hasText(uid, MarketplacecommerceservicesConstants.UID_CANNOT_BE_EMPTY);
			final CustomerModel customerModel = userService.getUserForUID(uid.toLowerCase(), CustomerModel.class);
			forgetPasswordService.forgottenPasswordEmail(customerModel, securePasswordUrl);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to check token retrieved
	 */

	@Override
	public String getOriginalToken(final String tokenFromURL, final String emailId)
	{
		try
		{
			Assert.hasText(emailId, MarketplacecommerceservicesConstants.UID_CANNOT_BE_EMPTY);

			final CustomerModel customerModel = userService.getUserForUID(emailId.toLowerCase(), CustomerModel.class);
			final String originalToken = forgetPasswordService.getOriginalToken(tokenFromURL, customerModel);
			return originalToken;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/**
	 * @description method is called to check the EncodedFormat of Password
	 */

	@Override
	public boolean checkPasswordEncodedFormat(final String emailId, final String currPwd)
	{
		try
		{
			boolean isValid = false;
			final CustomerModel currentUser = getCurrentCustomerByEmail(emailId);
			if (null != currentUser && null != currentUser.getEncodedPassword()
					&& matchPassword(currentUser, emailId.toLowerCase(), currPwd))
			{
				isValid = true;
			}
			return isValid;
		}
		catch (final PasswordMismatchException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0009);
		}
		catch (final PasswordEncoderNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0010);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to match the current Password
	 * @param currentUser
	 * @param lowerCase
	 * @param currPwd
	 * @return boolean
	 */
	private boolean matchPassword(final UserModel currentUser, final String lowerCase, final String currPwd)
	{
		try
		{
			boolean isMismatched = false;
			if (null == currentUser.getPasswordEncoding())
			{
				currentUser.setPasswordEncoding(configurationService.getConfiguration().getString(PASSWORDENCODING));
			}
			final String encodedCurrentPassword = passwordEncoderService.encode(currentUser, currPwd,
					currentUser.getPasswordEncoding());
			if (!encodedCurrentPassword.equals(currentUser.getEncodedPassword()))
			{
				isMismatched = true;
			}
			return isMismatched;
		}
		catch (final PasswordMismatchException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0009);
		}
		catch (final PasswordEncoderNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0010);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to update the Password
	 */
	@Override
	public void updatePassword(final String token, final String newPassword) throws TokenInvalidatedException
	{
		try
		{
			forgetPasswordService.updatePassword(token, newPassword);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/**
	 * @description method is called to validate the Mobile number
	 */
	@Override
	public String validateMobile(final String email)
	{
		try
		{
			final CustomerModel customerModel = getCurrentCustomerByEmail(email);
			if (null != customerModel)
			{
				if (customerModel.getMobileNumber() != null)
				{
					return customerModel.getMobileNumber();
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to generate the OTPNumber
	 */
	@Override
	public String generateOTPNumber(final String emailId)
	{
		try
		{
			String otp = null;
			try
			{
				final CustomerModel customerModel = getCurrentCustomerByEmail(emailId);
				if (null != customerModel && null != customerModel.getUid())
				{
					otp = genotpservice.generateOTPNumber(customerModel.getUid(), OTPTypeEnum.FORGOT_PASSWORD.getCode(), emailId);
				}
			}
			catch (final InvalidKeyException ex)
			{
				throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0011);
			}
			catch (final NoSuchAlgorithmException ex)
			{
				throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0010);
			}
			return otp;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to validate the OTP Number
	 * @param uid
	 * @param enteredOTPNumber
	 * @param OTPType
	 * @param expiryTime
	 * @return boolean
	 */
	@Override
	public boolean validateOTP(final String uid, final String enteredOTPNumber, final OTPTypeEnum OTPType, final long expiryTime)
	{
		boolean validate = false;
		try
		{
			validate = genotpservice.validaterOTP(uid, enteredOTPNumber, OTPType, expiryTime);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}


		return validate;
	}


	/**
	 * @description method is called to change the Password of the customer through SMS Service
	 */
	@Override
	public String forgottenPassword(final String uid)
	{
		try
		{
			Assert.hasText(uid, MarketplacecommerceservicesConstants.UID_CANNOT_BE_EMPTY);
			final CustomerModel customerModel = userService.getUserForUID(uid.toLowerCase(), CustomerModel.class);
			final String token = forgetPasswordService.forgottenPasswordSMS(customerModel);
			return token;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	@Override
	public void sendEmailForUpdateCustomerProfile(final CustomerModel customerCurrentData, final List<String> updatedDetailList,
			final String profileUpdateUrl)
	{
		try
		{
			if (null != updatedDetailList)
			{
				final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel) businessProcessService
						.createProcess("customerProfileUpdateEmailProcess" + System.currentTimeMillis(),
								"customerProfileUpdateEmailProcess");


				storeFrontCustomerProcessModel.setSite(baseSiteService.getCurrentBaseSite());
				storeFrontCustomerProcessModel.setStore(baseStoreService.getCurrentBaseStore());
				storeFrontCustomerProcessModel.setCustomer(customerCurrentData);
				storeFrontCustomerProcessModel.setLanguage(commonI18NService.getCurrentLanguage());
				storeFrontCustomerProcessModel.setCurrency(commonI18NService.getCurrentCurrency());
				storeFrontCustomerProcessModel.setCustomerProfileList(updatedDetailList);
				storeFrontCustomerProcessModel.setCustomerUpdateProfileURL(profileUpdateUrl);
				modelService.save(storeFrontCustomerProcessModel);
				businessProcessService.startProcess(storeFrontCustomerProcessModel);
			}
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E0000, ex);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}





}
