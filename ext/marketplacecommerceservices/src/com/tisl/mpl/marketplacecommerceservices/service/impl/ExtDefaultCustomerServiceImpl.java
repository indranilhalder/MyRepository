/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.commerceservices.event.RegisterEvent;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.marketplacecommerceservices.event.MplRegisterEvent;
import com.tisl.mpl.marketplacecommerceservices.service.ExtDefaultCustomerService;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPreferenceService;
import com.tisl.mpl.service.MplCustomerWebService;



/**
 * @author TCS
 *
 */
public class ExtDefaultCustomerServiceImpl extends DefaultCustomerAccountService implements ExtDefaultCustomerService
{
	private UserService userService;
	@Autowired
	private ExtendedUserService extUserService;
	//Customer Create CRM
	@Autowired
	private MplCustomerWebService mplCustomerWebService;


	@Autowired
	private MplPreferenceService mplPreferenceService;

	/**
	 * @return the mplCustomerWebService
	 */
	public MplCustomerWebService getCustomerWebService()
	{
		return mplCustomerWebService;
	}

	/**
	 * @param customerWebService
	 *           the customerWebService to set
	 */
	public void setCustomerWebService(final MplCustomerWebService customerWebService)
	{
		this.mplCustomerWebService = customerWebService;
	}


	protected CustomerModel getCurrentSessionCustomer()
	{
		return (CustomerModel) getUserService().getCurrentUser();
	}

	@Override
	protected UserService getUserService()
	{
		return userService;
	}

	@Override
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}



	//Customer Create CRM


	/**
	 * @description to change uid
	 */
	@Override
	public void changeUid(final String newUid, final String currentPassword) throws DuplicateUidException,
			PasswordMismatchException
	{
		try
		{
			Assert.hasText(newUid, MplConstants.M6_ASSERT_EMAIL_NOT_NULL);
			Assert.hasText(currentPassword, MplConstants.M7_ASSERT_CURR_PWD_NOT_NULL);

			final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
			final UserModel userForUID = userService.getUserForUID(currentUser.getOriginalUid());
			if (userForUID != null && userForUID instanceof CustomerModel && !currentUser.getOriginalUid().equals(newUid))
			{
				checkUidUniqueness(newUid);
				adjustPassword(currentUser, newUid, currentPassword);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description check Uid Uniqueness
	 */
	@Override
	protected void checkUidUniqueness(final String newUid) throws DuplicateUidException
	{
		try
		{
			if (userService.getUserForUID(newUid) != null)
			{
				throw new DuplicateUidException(MplConstants.M9_USER_WITH_EMAIL + newUid + MplConstants.M10_ALREADY_EXIST);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description to adjust password
	 * @param currentUser
	 * @param newUid
	 * @param currentPassword
	 * @throws PasswordMismatchException
	 */
	private void adjustPassword(final CustomerModel currentUser, final String newUid, final String currentPassword)
			throws PasswordMismatchException
	{
		try
		{
			// Validate that the current password is correct
			final String encodedCurrentPassword = getPasswordEncoderService().encode(currentUser, currentPassword,
					currentUser.getPasswordEncoding());
			if (!encodedCurrentPassword.equals(currentUser.getEncodedPassword()))
			{
				throw new PasswordMismatchException(currentUser.getUid());
			}

			// Save the newUid
			currentUser.setOriginalUid(newUid);
			getModelService().save(currentUser);

			// Update the password
			getUserService().setPassword(currentUser, currentPassword, currentUser.getPasswordEncoding());
			getModelService().save(currentUser);
		}
		catch (final PasswordEncoderNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0009);
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
	 * @description to register user
	 */
	@Override
	public void register(final CustomerModel customerModel, final String password) throws DuplicateUidException
	{
		try
		{
			validateParameterNotNullStandardMessage(MplConstants.CUSTOMER_MODEL, customerModel);

			generateCustomerId(customerModel);
			if (password != null)
			{
				getUserService().setPassword(customerModel, password, getPasswordEncoding());
			}
			internalSaveCustomer(customerModel);
			getEventService().publishEvent(initializeEvent(new RegisterEvent(), customerModel));
		}
		catch (final PasswordEncoderNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0009);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description to register user in cscockpit
	 */
	@Override
	public void registerCockpit(final CustomerModel customerModel, final String password) throws DuplicateUidException
	{
		try
		{
			validateParameterNotNullStandardMessage(MplConstants.CUSTOMER_MODEL, customerModel);

			if (extUserService.isEmailUniqueForSite(customerModel.getOriginalUid()))
			{
				generateCustomerId(customerModel);
				if (password != null)
				{
					getUserService().setPassword(customerModel, password, getPasswordEncoding());
				}
				customerModel.setIsTemporaryPasswordChanged(Boolean.FALSE);
				customerModel.setIsCustomerCreatedInCScockpit(Boolean.TRUE);

				internalSaveCustomer(customerModel);
				getEventService().publishEvent(initializeEvent(new MplRegisterEvent(), customerModel));
				//Customer Create CRM
				//customerWebService.customerModeltoWsDTO(customerModel);
				//Customer Create CRM

				//				getEventService().publishEvent(initializeEvent(new RegisterEvent(), customerModel));
				/*
				 * final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel)
				 * businessProcessService .createProcess( "customerRegistrationInCsEmailProcess-" + customerModel.getUid() +
				 * "-" + System.currentTimeMillis(), "customerRegistrationInCsEmailProcess"); final Random randomGenerator =
				 * new Random(); final String registerPassword = String.valueOf(randomGenerator.nextInt(1000000));
				 * storeFrontCustomerProcessModel.setSite(baseSiteService.getCurrentBaseSite());
				 * storeFrontCustomerProcessModel.setCustomer(customerModel); if
				 * (customerModel.getIsCustomerCreatedInCScockpit().booleanValue()) {
				 * storeFrontCustomerProcessModel.setPassword(registerPassword); userService.setPassword(customerModel,
				 * registerPassword, customerModel.getPasswordEncoding()); getModelService().save(customerModel); }
				 * storeFrontCustomerProcessModel.setLanguage(commonI18NService.getCurrentLanguage());
				 * storeFrontCustomerProcessModel.setCurrency(commonI18NService.getCurrentCurrency()); final BaseStoreModel
				 * currentBaseStore = baseStoreService.getCurrentBaseStore(); if (currentBaseStore != null) {
				 * storeFrontCustomerProcessModel.setStore(currentBaseStore); }
				 * getModelService().save(storeFrontCustomerProcessModel);
				 * businessProcessService.startProcess(storeFrontCustomerProcessModel);
				 * mplCustomerWebService.customerModeltoWsData(customerModel,
				 * MarketplacecommerceservicesConstants.NEW_CUSTOMER_CREATE_FLAG, false);
				 */
			}
			else
			{
				throw new DuplicateUidException(MplConstants.M1_CANNOT_REGISTER + (customerModel.getOriginalUid())
						+ MplConstants.M2_ALREADY_EXISTS);
			}
		}
		catch (final PasswordEncoderNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0009);
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
	 * @description to register user
	 */
	@Override
	public void registerUser(final CustomerModel customerModel, final String password, final String affiliateId)
			throws DuplicateUidException
	{
		try
		{
			validateParameterNotNullStandardMessage(MplConstants.CUSTOMER_MODEL, customerModel);

			generateCustomerId(customerModel);
			if (password != null)
			{
				getUserService().setPassword(customerModel, password, getPasswordEncoding());
			}
			internalSaveCustomer(customerModel);

			//Commented this code as this is not required while sign in/sign up
			//if (null != customerModel.getOriginalUid())
			//	{
			//
			//		mplSNSMobilePushService.setUpNotification(customerModel.getOriginalUid(), null);
			//	}

			getEventService().publishEvent(initializeEvent(new MplRegisterEvent(), customerModel));//commented
			/*
			 * final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel)
			 * businessProcessService .createProcess("customerRegistrationEmailProcess-" + customerModel.getUid() + "-" +
			 * System.currentTimeMillis(), "customerRegistrationEmailProcess"); final Random randomGenerator = new
			 * Random(); final String registerPassword = String.valueOf(randomGenerator.nextInt(1000000));
			 * storeFrontCustomerProcessModel.setSite(baseSiteService.getCurrentBaseSite());
			 * storeFrontCustomerProcessModel.setCustomer(customerModel); if
			 * (customerModel.getIsCustomerCreatedInCScockpit().booleanValue()) {
			 * storeFrontCustomerProcessModel.setPassword(registerPassword); userService.setPassword(customerModel,
			 * registerPassword, customerModel.getPasswordEncoding()); getModelService().save(customerModel); }
			 * storeFrontCustomerProcessModel.setLanguage(commonI18NService.getCurrentLanguage());
			 * storeFrontCustomerProcessModel.setCurrency(commonI18NService.getCurrentCurrency()); final BaseStoreModel
			 * currentBaseStore = baseStoreService.getCurrentBaseStore(); if (currentBaseStore != null) {
			 * storeFrontCustomerProcessModel.setStore(currentBaseStore); }
			 * getModelService().save(storeFrontCustomerProcessModel);
			 * businessProcessService.startProcess(storeFrontCustomerProcessModel);
			 */
		}
		catch (final PasswordEncoderNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0010);
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
	 * @description to change Uid
	 */
	@Override
	public void changeUid(final String newUid) throws DuplicateUidException
	{
		try
		{
			Assert.hasText(newUid, MplConstants.M8_ASSERT_UID_NOT_NULL);

			final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();
			final UserModel userForUIDAndSiteID = userService.getUserForUID(currentUser.getOriginalUid());

			if (userForUIDAndSiteID != null && userForUIDAndSiteID instanceof CustomerModel
					&& !currentUser.getOriginalUid().equals(newUid))
			{
				checkUidUniqueness(newUid);
				currentUser.setOriginalUid(newUid);
				getModelService().save(currentUser);
			}
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
	 * @description method called internally to save customer model data
	 */
	@Override
	public void internalSaveCustomer(final CustomerModel customerModel) throws DuplicateUidException
	{
		try
		{
			getModelService().save(customerModel);
			mplPreferenceService.saveUserSpecificMplPrefDataInitially(customerModel);
		}
		catch (final ModelSavingException e)
		{
			if ((e.getCause() instanceof InterceptorException)
					&& (((InterceptorException) e.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class)))
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
			}

			throw e;
		}
		catch (final AmbiguousIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0009);
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
	 * @description method called internally to save customer model data for social login
	 */
	@Override
	public void internalSaveCustomerForSocialLogin(final CustomerModel customerModel)
	{
		try
		{
			getModelService().save(customerModel);
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
	 * @description method called internally to register User For Social Signup
	 * @return ExtRegisterData
	 */
	@Override
	public ExtRegisterData registerUserForSocialSignup(final CustomerModel customerModel)
	{
		try
		{
			validateParameterNotNullStandardMessage(MplConstants.CUSTOMER_MODEL, customerModel);

			generateCustomerId(customerModel);
			final String password = generateOTPNumber(customerModel.getUid());

			if (password != null)
			{
				getUserService().setPasswordWithDefaultEncoding(customerModel, password);
			}
			internalSaveCustomerForSocialLogin(customerModel);
			final ExtRegisterData registerData = new ExtRegisterData();
			registerData.setLogin(customerModel.getDisplayName());
			registerData.setPassword(password);
			//Customer Create CRM
			//customerWebService.customerModeltoWsDTO(customerModel);
			//Customer Create CRM
			//TISUATPII-615 Fix for Email for  Customer Registration through social login
			getEventService().publishEvent(initializeEvent(new MplRegisterEvent(), customerModel));
			return registerData;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description generate OTP Number
	 * @return String otp
	 */
	@Override
	public String generateOTPNumber(final String customerUid)
	{
		String otp = null;
		try
		{
			//otp = genotpservice.generateTempPassword(customerUid);
			//otp = MplConstants.TEMP_OTP + otp;
			final Random randomGenerator = new Random();
			final String registerPassword = String.valueOf(randomGenerator.nextInt(1000000));
			otp = registerPassword;
		}
		/*
		 * catch (final InvalidKeyException ex) { throw new EtailNonBusinessExceptions(ex,
		 * MarketplacecommerceservicesConstants.E0011); } catch (final NoSuchAlgorithmException ex) { throw new
		 * EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0010); }
		 */
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return otp;
	}




}
