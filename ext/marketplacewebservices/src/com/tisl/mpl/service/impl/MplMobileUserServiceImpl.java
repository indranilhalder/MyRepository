/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.enums.Frequency;
import com.tisl.mpl.core.model.MarketplacePreferenceModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.data.MplPreferencePopulationData;
import com.tisl.mpl.facades.product.data.ExtRegisterData;
import com.tisl.mpl.helper.MplUserHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPreferenceService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.service.MplMobileUserService;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.wsdto.FetchCategoryBrandWsDTO;
import com.tisl.mpl.wsdto.FetchNewsLetterSubscriptionWsDTO;
import com.tisl.mpl.wsdto.FetchNewsLetterWsDTO;
import com.tisl.mpl.wsdto.MplRegistrationResultWsDto;
import com.tisl.mpl.wsdto.MplUserResultWsDto;


//import com.google.api.client.util.Charsets;


/**
 * @author TCS
 *
 */
public class MplMobileUserServiceImpl implements MplMobileUserService
{

	/**
	 *
	 */
	@Resource
	private RegisterCustomerFacade registerCustomerFacade;
	@Resource
	private ModelService modelService;
	@Resource
	private MplPreferenceService mplPreferenceService;
	@Resource
	private UserService userService;
	@Resource
	private ConfigurationService configurationService;
	@Resource(name = "extendedUserService")
	private ExtendedUserService extUserService;
	@Resource
	private MplUserHelper mplUserHelper;

	private UserDetailsService userDetailsService;

	@Resource
	private OTPGenericService otpGenericService;
	@Resource
	private SendSMSFacade sendSMSFacade;

	private static final Logger LOG = Logger.getLogger(MplMobileUserServiceImpl.class);

	private String gigyaUID;

	/**
	 * Register new user for luxury
	 *
	 * @param login
	 * @param password
	 * @param mobileNumber
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws DuplicateUidException
	 */
	@SuppressWarnings("javadoc")
	@Override
	public MplUserResultWsDto registerNewLuxUser(final String login, final String password, final String mobileNumber,
			final String firstName, final String lastName, final String gender, final boolean tataTreatsEnable)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		boolean successFlag = false;
		try
		{
			result = mplUserHelper.validateRegistrationData(login, password);
			LOG.debug("************** User details validated mobile web service ************" + login);
			//Set login and password
			final ExtRegisterData registration = new ExtRegisterData();
			registration.setLogin(login);
			registration.setPassword(password);
			registration.setFirstName(firstName);
			registration.setLastName(lastName);
			registration.setGender(gender);
			registration.setMobilenumber(mobileNumber);

			//TPR-1372
			if (tataTreatsEnable)
			{
				registration.setCheckTataRewards(true);
			}
			//Register the user, call facade
			if (registerCustomerFacade.checkUniquenessOfEmail(registration))
			{
				registerCustomerFacade.register(registration, 0);
				//Set success flag
				successFlag = true;
				LOG.debug("************** User registered via mobile web service *************" + login);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0001);
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			//Throw exception when the input details are invalid
			throw businessException;
		}

		catch (final DuplicateUidException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B0001);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//Catch and throw exception as it is when obtained from commerce
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		if (successFlag)
		{
			//Set success flag
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			if (null != login && null != getCustomerId(login))
			{
				LOG.debug("************ Fetching customer id mobile web service for **************" + login);
				result.setCustomerId(getCustomerId(login));
			}
		}
		return result;
	}

	/**
	 * Register new user TPR-1372
	 *
	 * @param login
	 * @param password
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws DuplicateUidException
	 */
	@SuppressWarnings("javadoc")
	@Override
	public MplUserResultWsDto registerNewMplUser(final String login, final String password, final boolean tataTreatsEnable,
			final int platformNumber) throws EtailBusinessExceptions, EtailNonBusinessExceptions//TPR-6272 parameter platformNumber added
	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		boolean successFlag = false;
		try
		{
			result = mplUserHelper.validateRegistrationData(login, password);
			LOG.debug("************** User details validated mobile web service ************" + login);
			//Set login and password
			final ExtRegisterData registration = new ExtRegisterData();
			registration.setLogin(login);
			registration.setPassword(password);
			//TPR-1372
			if (tataTreatsEnable)
			{
				registration.setCheckTataRewards(true);
			}
			//Register the user, call facade
			if (registerCustomerFacade.checkUniquenessOfEmail(registration))
			{
				registerCustomerFacade.register(registration, platformNumber);//TPR-6272 parameter platformNumber passed
				//Set success flag
				successFlag = true;
				LOG.debug("************** User registered via mobile web service *************" + login);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0001);
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			//Throw exception when the input details are invalid
			throw businessException;
		}

		catch (final DuplicateUidException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B0001);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//Catch and throw exception as it is when obtained from commerce
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		if (successFlag)
		{
			//Set success flag
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			if (null != login && null != getCustomerId(login))
			{
				LOG.debug("************ Fetching customer id mobile web service for **************" + login);
				result.setCustomerId(getCustomerId(login));
			}
		}
		return result;
	}

	/**
	 * New API for API project-phase 1 || For registration
	 */
	@SuppressWarnings("javadoc")
	@Override
	public MplRegistrationResultWsDto registerAppUser(final String mobileNumber, final int platformNumber, final String emailId)
	{
		final MplRegistrationResultWsDto result = new MplRegistrationResultWsDto();
		boolean successFlag = false;
		try
		{
			LOG.debug("User details validated mobile web service :::::::::::" + mobileNumber);
			final ExtRegisterData registration = new ExtRegisterData();
			if (StringUtils.isNotEmpty(emailId))
			{
				registration.setUid(emailId);
			}
			registration.setLogin(mobileNumber);
			if (registerCustomerFacade.checkMobileNumberUnique(registration))
			{
				if (StringUtils.isNotEmpty(emailId))
				{
					if (!registerCustomerFacade.checkEmailIdUnique(registration))
					{
						successFlag = false;
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.NU002);
					}
				}
				final String otp = otpGenericService.generateOTPForRegister(mobileNumber, OTPTypeEnum.REG.getCode(), mobileNumber);
				try
				{
					sendSMSFacade.sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID,
							MarketplacecommerceservicesConstants.SMS_MESSAGE_C2C_OTP.replace(
									MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, otp), mobileNumber);
				}
				catch (final ModelSavingException e)
				{
					LOG.error(MarketplacecommerceservicesConstants.LOGERROR, e);
				}
				catch (final EtailNonBusinessExceptions e)
				{
					LOG.error(MarketplacecommerceservicesConstants.LOGERROR, e);
				}
				//Set success flag
				successFlag = true;
				LOG.debug("************** User registered via mobile web service *************" + mobileNumber);

			}
			else
			{
				successFlag = false;
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.NU003);
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			//Throw exception when the input details are invalid
			throw businessException;
		}

		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//Catch and throw exception as it is when obtained from commerce
			throw e;
		}
		//New
		catch (final AmbiguousIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.NU000);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			result.setUsername(mobileNumber);
			result.setMessage("OTP has been sent on your specified email id or phone number");
		}
		return result;
	}

	//Mobile registration

	/**
	 * Login user
	 *
	 * @param login
	 * @param password
	 * @return MplUserResultWsDto
	 */
	@Override
	public MplUserResultWsDto loginUser(final String login, final String password) throws EtailNonBusinessExceptions,
			EtailBusinessExceptions
	{
		final MplUserResultWsDto output = new MplUserResultWsDto();
		String result = null;
		UserDetails userDetails = null;
		try
		{
			//Check if the user exists in the system with login
			userDetails = retrieveUserforMarketplace(login);
			LOG.debug("********* Mobile Web Service ********" + MarketplacewebservicesConstants.USER_DETAILS + userDetails);
		}
		catch (final UsernameNotFoundException notFound)
		{
			//User name not found
			throw new EtailNonBusinessExceptions(notFound, MarketplacecommerceservicesConstants.B9001);
		}
		catch (final DataAccessException repositoryProblem)
		{
			throw new EtailNonBusinessExceptions(repositoryProblem, MarketplacecommerceservicesConstants.B9000);
		}

		catch (final EtailBusinessExceptions businessException)
		{
			//User is null
			throw businessException;
		}
		catch (final AuthenticationException authException)
		{
			throw new EtailNonBusinessExceptions(authException, MarketplacecommerceservicesConstants.B9006);
		}
		catch (final Exception e)
		{ //Any other exception
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		User user = null;
		try
		{
			//Get user for Login
			final UserModel userModel = extUserService.getUserForUID(StringUtils.lowerCase(login));

			if (null != userModel)
			{
				user = modelService.getSource(userModel);
			}

			//Check if the password matches
			if (null != user && !user.checkPassword(password))
			{
				//If not throw exception
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9003);
			}
			else
			{
				result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;

				if (null != login && null != getCustomerId(login))
				{
					output.setCustomerId(getCustomerId(login));
				}

				if (!StringUtils.isEmpty(login) && !StringUtils.isEmpty(isTemporaryPassword(login)))
				{
					output.setIsTemporaryPassword(isTemporaryPassword(login));
					LOG.debug("*********** Mobile web service isTemporaryPassword for" + login + " is >>> "
							+ isTemporaryPassword(login));
				}
			}
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9007);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9007);
		}
		if (null != result)
		{
			output.setStatus(result);
		}
		return output;
	}

	/**
	 * Retrieve user for marketplace with login
	 *
	 * @param username
	 * @return UserDetails
	 * @throws DataAccessException
	 * @throws AuthenticationServiceException
	 * @throws AuthenticationException
	 */
	public UserDetails retrieveUserforMarketplace(final String username) throws DataAccessException, EtailBusinessExceptions,
			AuthenticationException
	{
		//Try to load user with username
		UserDetails loadedUser;
		loadedUser = getUserDetailsService().loadUserByUsername(username);
		if (loadedUser == null)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9001);
		}
		return loadedUser;
	}


	/**
	 * Registering social user TPR-1372
	 *
	 * @param login
	 * @param socialMediaType
	 * @return MplUserResultWsDto
	 * @throws EtailNonBusinessExceptions
	 * @throws EtailNonBusinessExceptions
	 */
	private MplUserResultWsDto registerNewUserSocial(final String login, final String socialMediaType,
			final boolean tataTreatsEnable, final int platformNumber) throws EtailNonBusinessExceptions, EtailNonBusinessExceptions //SDI-639
	{
		final MplUserResultWsDto result = new MplUserResultWsDto();
		boolean successFlag = false;
		boolean isExisting = false;
		ExtRegisterData registration = new ExtRegisterData();
		registration.setLogin(login);
		registration.setSocialMediaType(socialMediaType);

		LOG.debug("*************** Mobile web service social media registration ***********" + login + socialMediaType);
		try
		{
			//TPR-1372
			if (tataTreatsEnable)
			{
				registration.setCheckTataRewards(true);
			}

			if (null != extUserService.getUserForUid(StringUtils.lowerCase(login)))
			{
				isExisting = true;
			}
			if (isExisting)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0001);
			}
			else
			{
				//Register user.Password will be auto generated.
				final boolean isMobile = true;
				registration = registerCustomerFacade.registerSocial(registration, isMobile, platformNumber); //SDI-639
				saveSocialMediaIndicatorForUser(registration.getLogin());
				//Set Success Flag to true
				successFlag = true;
				if (null != login && null != getCustomerId(login))
				{
					result.setCustomerId(getCustomerId(login));
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9007);
				}
			}
		}

		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
		}

		if (successFlag)
		{
			//Set result status to success
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			//Set result status to error
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}

	/**
	 * get customer id
	 *
	 * @param emailId
	 * @return String
	 */
	public String getCustomerId(final String emailId)
	{
		String customerId = null;
		CustomerModel custModel = null;
		custModel = getCustomerDetails(emailId);
		if (null != custModel && null != custModel.getUid())
		{
			customerId = custModel.getUid();
		}
		LOG.debug("************ mobile seb service Customer id *********" + customerId + "for " + emailId);
		return customerId;
	}

	/**
	 * check password is temp or not
	 *
	 * @param emailId
	 * @return boolean
	 */
	private String isTemporaryPassword(final String emailId)
	{
		String isTempPasswordChanged = MarketplacecommerceservicesConstants.N;
		CustomerModel custModel = null;
		custModel = getCustomerDetails(emailId);
		if (null != custModel.getIsTemporaryPasswordChanged() && custModel.getIsTemporaryPasswordChanged().booleanValue())
		{
			isTempPasswordChanged = MarketplacecommerceservicesConstants.Y;
		}
		return isTempPasswordChanged;
	}


	/**
	 * get customer details
	 *
	 * @param emailId
	 * @return CustomerModel
	 */
	private CustomerModel getCustomerDetails(final String emailId)
	{
		CustomerModel custModel = null;
		try
		{
			if (StringUtils.isNotEmpty(emailId))
			{
				custModel = extUserService.getUserForUid(StringUtils.lowerCase(emailId));
			}
		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9007);
		}
		return custModel;
	}

	/**
	 * Saves an indicator for a customer checking whether it registered via social media or normal registration
	 *
	 * @param registration
	 */
	private void saveSocialMediaIndicatorForUser(final String registration)
	{
		try
		{
			CustomerModel custModel = getModelService().create(CustomerModel.class);
			custModel = getCustomerDetails(registration);

			if (null != custModel)
			{
				LOG.debug("********* Mobile webservice saving social mdeia indicator **********" + registration);
				custModel.setCustomerRegisteredBySocialMedia(Boolean.TRUE);
				modelService.save(custModel);
			}

		}
		catch (final AmbiguousIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9015);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9015);
		}
	}

	// fetchNewsLetter webservice code

	@Override
	public FetchNewsLetterSubscriptionWsDTO fetchMplPreferenceContents()
	{
		final FetchNewsLetterSubscriptionWsDTO fetchNewsLetterSubscriptionWsDTO = new FetchNewsLetterSubscriptionWsDTO();
		final MplPreferencePopulationData mplPreferenceDataForAllDetail = mplPreferenceService.fetchAllMplPreferenceContents();
		if (mplPreferenceDataForAllDetail != null)
		{
			final CustomerModel currentCustomer = getCurrentSessionCustomer();
			final MarketplacePreferenceModel mplPreferenceModel = currentCustomer.getMarketplacepreference();
			final List<CategoryModel> categoriesList = mplPreferenceDataForAllDetail.getPreferredCategory();
			final List<CategoryModel> brandsList = mplPreferenceDataForAllDetail.getPreferredBrand();
			final List<String> frequencyAllList = mplPreferenceDataForAllDetail.getFrequencyList();
			final List<String> feedbackAreaAllList = mplPreferenceDataForAllDetail.getFeedbackAreaList();

			if (feedbackAreaAllList != null && !feedbackAreaAllList.isEmpty())
			{
				final List<FetchNewsLetterWsDTO> feedbackAreaList = new ArrayList<FetchNewsLetterWsDTO>();
				for (final String code : feedbackAreaAllList)
				{

					final FetchNewsLetterWsDTO feedbackAreaSelected = new FetchNewsLetterWsDTO();
					feedbackAreaSelected.setValue(code);
					if (code.equalsIgnoreCase(MarketplacecommerceservicesConstants.USERREVIEWS))
					{
						if (null != mplPreferenceModel)
						{
							if (Boolean.TRUE.equals(mplPreferenceModel.getUserReview()))
							{
								feedbackAreaSelected.setSelected(MarketplacecommerceservicesConstants.Y);
							}
							else
							{
								feedbackAreaSelected.setSelected(MarketplacecommerceservicesConstants.N);
							}

						}
						else
						{
							feedbackAreaSelected.setSelected(MarketplacecommerceservicesConstants.Y);
						}
					}
					else if (code.equalsIgnoreCase(MarketplacecommerceservicesConstants.CUSTOMERSURVEYS))
					{
						feedbackAreaSelected.setValue(code);
						if (null != mplPreferenceModel)
						{
							if (Boolean.TRUE.equals(mplPreferenceModel.getCustomerSurveys()))
							{
								feedbackAreaSelected.setSelected("Y");
							}
							else
							{
								feedbackAreaSelected.setSelected(MarketplacecommerceservicesConstants.N);
							}
						}
						else
						{
							feedbackAreaSelected.setSelected("Y");
						}
					}
					feedbackAreaList.add(feedbackAreaSelected);
				}
				fetchNewsLetterSubscriptionWsDTO.setFeedbackAreaList(feedbackAreaList);
			}


			if (frequencyAllList != null && !frequencyAllList.isEmpty())
			{
				final List<FetchNewsLetterWsDTO> frequencyList = new ArrayList<FetchNewsLetterWsDTO>();
				if (mplPreferenceModel != null && mplPreferenceModel.getEmailFrequency() != null)
				{
					for (final String frequency : frequencyAllList)
					{
						final FetchNewsLetterWsDTO frequencySelected = new FetchNewsLetterWsDTO();
						frequencySelected.setValue(frequency);
						if (frequency.equalsIgnoreCase(mplPreferenceModel.getEmailFrequency().getCode()))
						{
							frequencySelected.setSelected("Y");
						}
						else
						{
							frequencySelected.setSelected(MarketplacecommerceservicesConstants.N);
						}
						frequencyList.add(frequencySelected);
					}
					fetchNewsLetterSubscriptionWsDTO.setFrequencyList(frequencyList);
				}
				else
				{
					for (final String frequency : frequencyAllList)
					{
						final FetchNewsLetterWsDTO frequencySelected = new FetchNewsLetterWsDTO();
						frequencySelected.setValue(frequency);
						if (frequency.equalsIgnoreCase(Frequency.WEEKLY.toString()))
						{
							frequencySelected.setSelected("Y");
						}
						else
						{
							frequencySelected.setSelected(MarketplacecommerceservicesConstants.N);
						}
						frequencyList.add(frequencySelected);
					}
					fetchNewsLetterSubscriptionWsDTO.setFrequencyList(frequencyList);
				}
			}

			final List<FetchNewsLetterWsDTO> interestList = new ArrayList<FetchNewsLetterWsDTO>();
			final FetchNewsLetterWsDTO interest1 = new FetchNewsLetterWsDTO();
			final FetchNewsLetterWsDTO interest2 = new FetchNewsLetterWsDTO();
			interest1.setValue(MarketplacecommerceservicesConstants.INTERESTED_IN_EMAIL);
			interest2.setValue(MarketplacecommerceservicesConstants.NOT_INTERESTED_IN_EMAIL);
			if (null != mplPreferenceModel)
			{
				if (Boolean.TRUE.equals(mplPreferenceModel.getIsInterestedInEmail()))
				{
					interest1.setSelected(MarketplacecommerceservicesConstants.Y);
					interest2.setSelected(MarketplacecommerceservicesConstants.N);
				}
				else
				{
					interest1.setSelected(MarketplacecommerceservicesConstants.N);
					interest2.setSelected(MarketplacecommerceservicesConstants.Y);
				}
			}
			else
			{
				interest1.setSelected(MarketplacecommerceservicesConstants.Y);
				interest2.setSelected(MarketplacecommerceservicesConstants.N);
			}
			interestList.add(interest1);
			interestList.add(interest2);
			fetchNewsLetterSubscriptionWsDTO.setInterestList(interestList);


			if (brandsList != null && !brandsList.isEmpty())
			{
				final List<FetchCategoryBrandWsDTO> interestedBrands = new ArrayList<FetchCategoryBrandWsDTO>();
				Collection<CategoryModel> preferrredBrand = new ArrayList<CategoryModel>();
				if (mplPreferenceModel != null)
				{
					preferrredBrand = mplPreferenceModel.getPreferredBrand();
				}
				else
				{
					preferrredBrand = null;
				}

				for (final CategoryModel brand : brandsList)
				{
					final FetchCategoryBrandWsDTO interestedBrand = new FetchCategoryBrandWsDTO();
					interestedBrand.setValue(brand.getName());
					interestedBrand.setCode(brand.getCode());
					if (null != preferrredBrand)
					{
						if (!preferrredBrand.isEmpty() && preferrredBrand.contains(brand))
						{
							interestedBrand.setSelected(MarketplacecommerceservicesConstants.Y);
						}
						else
						{
							interestedBrand.setSelected(MarketplacecommerceservicesConstants.N);
						}
					}
					else
					{
						interestedBrand.setSelected(MarketplacecommerceservicesConstants.Y);
					}
					interestedBrands.add(interestedBrand);
				}
				fetchNewsLetterSubscriptionWsDTO.setInterestedBrand(interestedBrands);
			}

			if (categoriesList != null && !categoriesList.isEmpty())
			{
				final List<FetchCategoryBrandWsDTO> interestedCategories = new ArrayList<FetchCategoryBrandWsDTO>();
				Collection<CategoryModel> preferrredCategories = new ArrayList<CategoryModel>();

				if (mplPreferenceModel != null)
				{
					preferrredCategories = mplPreferenceModel.getPreferredCategory();
				}
				else
				{
					preferrredCategories = null;
				}

				for (final CategoryModel category : categoriesList)
				{
					final FetchCategoryBrandWsDTO interestedCategory = new FetchCategoryBrandWsDTO();
					interestedCategory.setValue(category.getName());
					interestedCategory.setCode(category.getCode());
					if (null != preferrredCategories)
					{

						if (!preferrredCategories.isEmpty() && preferrredCategories.contains(category))
						{
							interestedCategory.setSelected(MarketplacecommerceservicesConstants.Y);
						}
						else
						{
							interestedCategory.setSelected(MarketplacecommerceservicesConstants.N);
						}
					}
					else
					{
						interestedCategory.setSelected(MarketplacecommerceservicesConstants.Y);
					}
					interestedCategories.add(interestedCategory);
				}
				fetchNewsLetterSubscriptionWsDTO.setInterestedCategory(interestedCategories);
			}
		}

		return fetchNewsLetterSubscriptionWsDTO;
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
	public void setCoationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	/**
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}


	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
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

	protected CustomerModel getCurrentSessionCustomer()
	{
		return (CustomerModel) userService.getCurrentUser();
	}


	/**
	 * @return the gigyaUID
	 */
	public String getGigyaUID()
	{
		return gigyaUID;
	}


	/**
	 * @param gigyaUID
	 *           the gigyaUID to set
	 */
	public void setGigyaUID(final String gigyaUID)
	{
		this.gigyaUID = gigyaUID;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplMobileUserService#loginSocialUser(java.lang.String, java.lang.String)
	 */
	@Override
	public MplUserResultWsDto loginSocialUser(final String login, final String socialMedia)
	{
		final MplUserResultWsDto output = new MplUserResultWsDto();
		String result = null;
		UserDetails userDetails = null;
		boolean successflag = false;

		final MplUserResultWsDto validatedResult = mplUserHelper.validateEmail(login);
		if (null != validatedResult.getStatus()
				&& validatedResult.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS_FLAG))
		{
			successflag = true;
		}
		try
		{
			userDetails = retrieveUserforMarketplace(login);
			LOG.debug(MarketplacewebservicesConstants.USER_DETAILS + userDetails);
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
			if (null != login && null != getCustomerId(login))
			{
				output.setCustomerId(getCustomerId(login));
			}
		}
		catch (final UsernameNotFoundException notFound)

		{ //User name not found
			throw new EtailNonBusinessExceptions(notFound, MarketplacecommerceservicesConstants.B9001);
		}
		catch (final DataAccessException repositoryProblem)

		{
			throw new EtailNonBusinessExceptions(repositoryProblem, MarketplacecommerceservicesConstants.B9000);
		}

		catch (final EtailBusinessExceptions businessException)
		{
			throw businessException;
		}
		catch (final AuthenticationException authException)
		{
			throw new EtailNonBusinessExceptions(authException, MarketplacecommerceservicesConstants.B9006);
		}

		catch (final EtailNonBusinessExceptions e)

		{
			if (null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_400_ERROR)
					&& e.toString().contains(
							Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_400_ERROR))
					&& null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_ERROR_MSG))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9021);
			}
			else if (null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_401_ERROR)
					&& e.toString().contains(
							Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_401_ERROR))
					&& null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_ERROR_MSG))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9021);
			}
			else if (null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_403_ERROR)
					&& e.toString().contains(
							Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_403_ERROR))
					&& null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_ERROR_MSG))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9021);
			}
			else
			{
				throw e;
			}
		}
		catch (

		final Exception e)

		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		//Return success flag
		if (successflag)

		{
			result = MarketplacecommerceservicesConstants.SUCCESS_FLAG;
		}
		if (null != result)

		{
			output.setStatus(result);
		}
		return output;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplMobileUserService#socialMediaRegistration(java.lang.String, java.lang.String)
	 */
	@Override
	public MplUserResultWsDto socialMediaRegistration(final String emailId, final String socialMedia,
			final boolean tataTreatsEnable, final int platformNumber) //SDI-639
	{
		boolean successflag = false;
		MplUserResultWsDto socialRegister = new MplUserResultWsDto();
		final MplUserResultWsDto validatedResult = mplUserHelper.validateEmail(emailId);
		if (null != validatedResult.getStatus()
				&& validatedResult.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.ERROR_FLAG))
		{
			return validatedResult;
		}
		try
		{
			socialRegister = registerNewUserSocial(emailId, socialMedia, tataTreatsEnable, platformNumber);
			if (!StringUtils.isEmpty(socialRegister.getStatus())
					&& socialRegister.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.SUCCESS_FLAG))
			{
				successflag = true;
			}
		}

		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//Set success flag as false
			successflag = false;
			if (null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_400_ERROR)
					&& e.toString().contains(
							Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_400_ERROR))
					&& null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_ERROR_MSG))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9021);
			}
			else if (null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_401_ERROR)
					&& e.toString().contains(
							Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_401_ERROR))
					&& null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_ERROR_MSG))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9021);
			}
			else if (null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_403_ERROR)
					&& e.toString().contains(
							Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_403_ERROR))
					&& null != Localization.getLocalizedString(MarketplacewebservicesConstants.SOCIAL_RESPONSE_CODE_ERROR_MSG))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9021);
			}
			else
			{
				throw e;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		if (successflag)
		{
			//Set success flag as true
			socialRegister.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		else
		{
			//Set success flag as false
			socialRegister.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return socialRegister;
	}

	/**
	 * @return the userDetailsService
	 */
	public UserDetailsService getUserDetailsService()
	{
		return userDetailsService;
	}

	/**
	 * @param userDetailsService
	 *           the userDetailsService to set
	 */
	public void setUserDetailsService(final UserDetailsService userDetailsService)
	{
		this.userDetailsService = userDetailsService;
	}

	@Override
	public boolean validateOtp(final String mobileNumber, final String otp, final OTPTypeEnum enumType)
	{
		final OTPResponseData otpResponse = otpGenericService
				.validateLatestOTP(
						mobileNumber,
						mobileNumber,
						otp,
						enumType,
						Long.parseLong(getConfigurationService().getConfiguration().getString(
								MarketplacecommerceservicesConstants.OTP_TIME)));
		if (null != otpResponse && null != otpResponse.getInvalidErrorMessage()
				&& otpResponse.getInvalidErrorMessage().equalsIgnoreCase("VALID"))
		{
			LOG.debug("Otp response fetched is ::::::::::::" + otpResponse.getInvalidErrorMessage());
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Register new user || Using mobile number
	 *
	 * @param login
	 * @param password
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws DuplicateUidException
	 */
	@SuppressWarnings("javadoc")
	@Override
	public MplUserResultWsDto registerNewMplUserWithMobile(final String login, final String password,
			final boolean tataTreatsEnable, final int platformNumber, final String emailId) throws EtailBusinessExceptions,
			EtailNonBusinessExceptions
	{
		LOG.debug("Step 1>>>>>>>>>>>>>>>>>");
		LOG.debug("UserName :::::::::::::" + login);
		LOG.debug("Password :::::::::::::" + password);
		LOG.debug("Email ID :::::::::::::" + emailId);

		final MplUserResultWsDto result = new MplUserResultWsDto();
		boolean successFlag = false;
		try
		{
			mplUserHelper.validateRegistrationDataForMobileNumber(login, password);
			LOG.debug("Step 2>>>>>>>>>>>>>>>>>");
			LOG.debug("************** User details validated mobile web service ************" + login);
			final ExtRegisterData registration = new ExtRegisterData();
			registration.setLogin(login);
			registration.setPassword(password);
			registration.setMobilenumber(login);
			if (StringUtils.isNotEmpty(emailId))
			{
				registration.setEmailId(emailId);
			}
			if (tataTreatsEnable)
			{
				registration.setCheckTataRewards(true);
			}
			if (registerCustomerFacade.checkMobileNumberUnique(registration))
			{
				LOG.debug("Step 3>>>>>>>>>>>>>>>>>");
				if (StringUtils.isNotEmpty(emailId))
				{
					LOG.debug("Step 4>>>>>>>>>>>>>>>>>");
					registration.setUid(emailId);
					if (!registerCustomerFacade.checkEmailIdUnique(registration))
					{
						LOG.debug("Step 5>>>>>>>>>>>>>>>>>");
						successFlag = false;
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.NU002);
					}
				}
				LOG.debug("Step 6>>>>>>>>>>>>>>>>>");
				registerCustomerFacade.register(registration, platformNumber);
				successFlag = true;
				LOG.debug("************** User registered via mobile web service *************" + login);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.NU003);
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			if (businessException.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.NU003))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.NU003);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0001);
			}
		}
		catch (final DuplicateUidException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B0001);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//Catch and throw exception as it is when obtained from commerce
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		LOG.debug("Step 7>>>>>>>>>>>>>>>>>");
		if (successFlag)
		{
			//Set success flag
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			if (null != login && null != getCustomerId(login))
			{
				LOG.debug("************ Fetching customer id mobile web service for **************" + login);
				result.setCustomerId(getCustomerId(login));
			}
		}
		return result;
	}

	/**
	 * New API for API project-phase 1 || For registration
	 */
	//NU-31
	@Override
	public MplRegistrationResultWsDto forgotPasswordOtp(final String mobileNumber, final int platformNumber)
	{
		final MplRegistrationResultWsDto result = new MplRegistrationResultWsDto();
		boolean successFlag = false;
		try
		{
			//result = mplUserHelper.validateRegistrationData(login, null);//to-do validate email & mobile number
			LOG.debug("************** User details validated mobile web service ************" + mobileNumber);
			final ExtRegisterData registration = new ExtRegisterData();
			registration.setLogin(mobileNumber);
			if (!registerCustomerFacade.checkUniquenessOfEmail(registration))
			{
				final String otp = otpGenericService.generateOTPForRegister(mobileNumber, OTPTypeEnum.FORGOT_PASSWORD.getCode(),
						mobileNumber);
				try
				{
					sendSMSFacade.sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID,
							MarketplacecommerceservicesConstants.SMS_MESSAGE_C2C_OTP.replace(
									MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, otp), mobileNumber);
				}
				catch (final ModelSavingException e)
				{
					LOG.error(MarketplacecommerceservicesConstants.LOGERROR, e);
				}
				catch (final EtailNonBusinessExceptions e)
				{
					LOG.error(MarketplacecommerceservicesConstants.LOGERROR, e);
				}
				successFlag = true;
				LOG.debug("************** User registered via mobile web service *************" + mobileNumber);
			}
			else
			{
				successFlag = false;
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0016);
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			throw businessException;
		}

		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9013);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		if (successFlag)
		{
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			result.setUsername(mobileNumber);
			result.setMessage("OTP has been sent on your specified email id/phone number");
		}
		return result;
	}

	/**
	 * NU-51
	 */
	@Override
	public boolean validateOtpWithoutExpiryTime(final String mobileNumber, final String otp, final OTPTypeEnum enumType)
	{
		final OTPResponseData otpResponse = otpGenericService.otpValidationWithoutExpiryTime(mobileNumber, mobileNumber, otp,
				enumType);

		if (null != otpResponse && null != otpResponse.getInvalidErrorMessage()
				&& otpResponse.getInvalidErrorMessage().equalsIgnoreCase("VALID"))
		{
			LOG.debug("Otp response fetched is ::::::::::::" + otpResponse.getInvalidErrorMessage());
			return true;
		}
		else
		{
			return false;
		}
	}
}
