/**
 *
 */
package com.tisl.mpl.facades.account.register.impl;

import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.CustomerOldEmailDetailsModel;
import com.tisl.mpl.core.model.SendUpdateProcessModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.MplCustomerProfileFacade;
import com.tisl.mpl.facades.product.data.DayData;
import com.tisl.mpl.facades.product.data.GenderData;
import com.tisl.mpl.facades.product.data.MonthData;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.facades.product.data.YearData;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.DeleteCardRequest;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;


/**
 * @author TCS
 *
 */
public class MplCustomerProfileFacadeImpl implements MplCustomerProfileFacade
{
	@Autowired
	private MplCustomerProfileService mplCustomerProfileService;
	@Autowired
	private UserService userService;
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
	private PasswordEncoderService passwordEncoderService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private I18NService i18NService;
	private static final String PASSWORDENCODING = "default.password.encoder.key";
	private final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);

	/**
	 * @return the mplCustomerProfileService
	 */
	public MplCustomerProfileService getMplCustomerProfileService()
	{
		return mplCustomerProfileService;
	}

	/**
	 * @param mplCustomerProfileService
	 *           the mplCustomerProfileService to set
	 */
	public void setMplCustomerProfileService(final MplCustomerProfileService mplCustomerProfileService)
	{
		this.mplCustomerProfileService = mplCustomerProfileService;
	}

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
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
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
	 * @return the passwordEncoderService
	 */
	public PasswordEncoderService getPasswordEncoderService()
	{
		return passwordEncoderService;
	}

	/**
	 * @param passwordEncoderService
	 *           the passwordEncoderService to set
	 */
	public void setPasswordEncoderService(final PasswordEncoderService passwordEncoderService)
	{
		this.passwordEncoderService = passwordEncoderService;
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

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return the i18NService
	 */
	public I18NService getI18NService()
	{
		return i18NService;
	}

	/**
	 * @param i18nService
	 *           the i18NService to set
	 */
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}


	private static final Logger LOG = Logger.getLogger(MplCustomerProfileFacadeImpl.class);


	/**
	 * @Description : Fetch Customer Profile Detail
	 */
	@Override
	public MplCustomerProfileData getCustomerProfileDetail(final String email)
	{
		try
		{
			MplCustomerProfileData customerProfileData = new MplCustomerProfileData();
			//	calling service to fetch customer detail
			customerProfileData = mplCustomerProfileService.getCustomerProfileDetail(email);
			return customerProfileData;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : Update Customer Profile Detail
	 */
	@Override
	public void updateCustomerProfile(final MplCustomerProfileData mplCustomerProfileData) throws DuplicateUidException
	{
		String name = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			//	configure Customer name field with proper combinations

			if (StringUtils.isNotEmpty(mplCustomerProfileData.getNickName()))
			{
				name = mplCustomerProfileData.getNickName();
			}
			else if (StringUtils.isNotEmpty(mplCustomerProfileData.getFirstName()))
			{
				name = mplCustomerProfileData.getFirstName();
			}

			if (null == name || MarketplacecommerceservicesConstants.EMPTY.equals(name))
			{
				name = MarketplacecommerceservicesConstants.SINGLE_SPACE;
			}

			final CustomerModel oCustomerModel = getCurrentSessionCustomer();
			oCustomerModel.setOriginalUid(mplCustomerProfileData.getDisplayUid());

			if (null != mplCustomerProfileData.getDateOfBirth()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getDateOfBirth()))
			{
				oCustomerModel.setDateOfBirth(sdf.parse(mplCustomerProfileData.getDateOfBirth()));
			}
			else
			{
				oCustomerModel.setDateOfBirth(null);
			}
			if (null != mplCustomerProfileData.getDateOfAnniversary()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getDateOfAnniversary()))
			{
				oCustomerModel.setDateOfAnniversary(sdf.parse(mplCustomerProfileData.getDateOfAnniversary()));
			}
			else
			{
				oCustomerModel.setDateOfAnniversary(null);
			}

			//			FirstName
			if (null != mplCustomerProfileData.getFirstName()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getFirstName()))
			{
				oCustomerModel.setFirstName(mplCustomerProfileData.getFirstName());
			}
			else
			{
				oCustomerModel.setFirstName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
			}

			//			LastName
			if (null != mplCustomerProfileData.getLastName()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getLastName()))
			{
				oCustomerModel.setLastName(mplCustomerProfileData.getLastName());
			}
			else
			{
				oCustomerModel.setLastName(MarketplacecommerceservicesConstants.SINGLE_SPACE);
			}

			//			MiddleName
			if (null != mplCustomerProfileData.getMiddleName()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getMiddleName()))
			{
				oCustomerModel.setMiddleName(mplCustomerProfileData.getMiddleName());
			}
			else
			{
				oCustomerModel.setMiddleName(MarketplacecommerceservicesConstants.EMPTY);
			}

			//			NickName
			if (null != mplCustomerProfileData.getNickName()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getNickName()))
			{
				oCustomerModel.setNickName(mplCustomerProfileData.getNickName());
			}
			else
			{
				oCustomerModel.setNickName(MarketplacecommerceservicesConstants.EMPTY);
			}

			//			MobileNumber
			if (null != mplCustomerProfileData.getMobileNumber()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getMobileNumber()))
			{
				oCustomerModel.setMobileNumber(mplCustomerProfileData.getMobileNumber());
			}
			else
			{
				oCustomerModel.setMobileNumber(MarketplacecommerceservicesConstants.EMPTY);
			}


			//	EmailId
			//			if (null != sessionService.getAttribute("isEmailUpdated")
			//					&& sessionService.getAttribute("isEmailUpdated").equals("true"))
			//			{
			if (null != mplCustomerProfileData.getEmailId()
					&& !MarketplacecommerceservicesConstants.EMPTY.equals(mplCustomerProfileData.getEmailId()))
			{
				try
				{
					final CustomerOldEmailDetailsModel custOldDetails = getModelService().create(CustomerOldEmailDetailsModel.class);

					if (StringUtils.isNotEmpty(mplCustomerProfileData.getEmailId()))
					{
						custOldDetails.setChangedOriginalUid(mplCustomerProfileData.getEmailId());
					}
					if (StringUtils.isNotEmpty(mplCustomerProfileData.getDisplayUid()))
					{
						custOldDetails.setOldOriginalUid(mplCustomerProfileData.getDisplayUid());
					}
					custOldDetails.setIsEmailChanged(Boolean.TRUE);
					custOldDetails.setChangedDate(new Date());
					modelService.save(custOldDetails);
				}
				catch (final Exception e)
				{
					LOG.error("************* Error in saving old email id details *************** " + e);
				}
				oCustomerModel.setOriginalUid(mplCustomerProfileData.getEmailId());

			}
			else
			{
				oCustomerModel.setOriginalUid(mplCustomerProfileData.getDisplayUid());
			}
			//			}

			//	Gender
			if (null != mplCustomerProfileData.getGender())
			{
				if (mplCustomerProfileData.getGender().equalsIgnoreCase(MarketplacecommerceservicesConstants.MALE_CAPS))
				{
					oCustomerModel.setGender(Gender.MALE);

				}
				else if (mplCustomerProfileData.getGender().equalsIgnoreCase(MarketplacecommerceservicesConstants.FEMALE_CAPS))
				{
					oCustomerModel.setGender(Gender.FEMALE);
				}
			}
			else
			{
				oCustomerModel.setGender(null);
			}
			//	calling service to save the customer detail
			mplCustomerProfileService.updateCustomerProfile(oCustomerModel, name, mplCustomerProfileData.getUid());
		}
		catch (final ParseException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	private CustomerModel getCurrentSessionCustomer()
	{
		return (CustomerModel) userService.getCurrentUser();
	}

	/**
	 * @Description : get Genders
	 */
	@Override
	public List<GenderData> getGenders()
	{
		try
		{
			//	calling service to fetch gender
			final List<GenderData> genderlist = mplCustomerProfileService.getGenders();
			return genderlist;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Description : get Day
	 */
	@Override
	public List<DayData> getDayList()
	{
		try
		{
			//	creating list with day
			final List<DayData> dayList = new ArrayList<DayData>();
			for (int i = 1; i < 32; i++)
			{
				String val = String.valueOf(i);
				if (val.length() == 1)
				{
					val = MarketplacecommerceservicesConstants.ZERO + val;
				}
				final DayData oDayData = new DayData();
				oDayData.setCode(val);
				oDayData.setName(val);
				dayList.add(oDayData);
			}
			return dayList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Description : get Month
	 */
	@Override
	public List<MonthData> getMonthList()
	{
		try
		{
			//	creating list with month
			final List<MonthData> monthList = new ArrayList<MonthData>();
			for (int i = 1; i < 13; i++)
			{
				String val = String.valueOf(i);
				if (val.length() == 1)
				{
					val = MarketplacecommerceservicesConstants.ZERO + val;
				}
				final MonthData oMonthData = new MonthData();
				oMonthData.setCode(val);
				oMonthData.setName(val);
				monthList.add(oMonthData);
			}
			return monthList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Description : get Genders
	 */
	@Override
	public List<YearData> getYearList()
	{
		try
		{
			//	creating list with year

			//	final int year = Calendar.getInstance().get(Calendar.YEAR);
			final int year = 2050;
			final List<YearData> yearList = new ArrayList<YearData>();
			for (int i = 1950; i <= year; i++)
			{
				final String val = String.valueOf(i);
				final YearData oYearData = new YearData();
				oYearData.setCode(val);
				oYearData.setName(val);
				yearList.add(oYearData);
			}
			return yearList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : send Email For Notification for Updating email
	 */
	@Override
	public void sendEmailForUpdate(final MplCustomerProfileData mplCustomerProfileData)
	{
		try
		{
			if (null != mplCustomerProfileData)
			{

				final SendUpdateProcessModel sendUpdateProcessModel = (SendUpdateProcessModel) businessProcessService.createProcess(
						"sendUpdate-" + mplCustomerProfileData.getDisplayUid() + "-" + System.currentTimeMillis(),
						"sendUpdateEmailProcess");

				sendUpdateProcessModel.setCustomer(getCurrentSessionCustomer());
				sendUpdateProcessModel.setSite(baseSiteService.getCurrentBaseSite());
				sendUpdateProcessModel.setCustomerEmail(sendUpdateProcessModel.getCustomer().getOriginalUid());
				sendUpdateProcessModel.setEncodedPassword(mplCustomerProfileData.getEncodedPassword());
				final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
				if (currentBaseStore != null)
				{
					sendUpdateProcessModel.setStore(currentBaseStore);
				}
				sendUpdateProcessModel.setCurrency(commonI18NService.getCurrentCurrency());
				sendUpdateProcessModel.setLanguage(commonI18NService.getCurrentLanguage());
				modelService.save(sendUpdateProcessModel);

				//	calling processService for sending email
				businessProcessService.startProcess(sendUpdateProcessModel);
			}
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public void sendEmailForUpdateCustomerProfile(final List<String> updatedDetailList, final String profileUpdateUrl)
	{
		try
		{
			if (null != updatedDetailList)
			{
				final CustomerModel customerCurrentData = (CustomerModel) userService.getCurrentUser();
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

	/**
	 * @Description : To change Uid of customer
	 */
	@Override
	public void changeUid(final String newUid, final String currentPassword) throws DuplicateUidException,
			PasswordMismatchException
	{
		try
		{
			// calling service to change uid of customer
			mplCustomerProfileService.changeUid(newUid, currentPassword);
		}
		catch (final de.hybris.platform.commerceservices.customer.PasswordMismatchException pse)
		{
			throw new EtailNonBusinessExceptions(pse, MarketplacecommerceservicesConstants.B0009);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @Description : To change Uid of customer with personal
	 */
	@Override
	public boolean checkUniquenessOfEmail(final String emailId)
	{
		boolean flag = false;
		try
		{
			// calling service to change uid of customer
			flag = mplCustomerProfileService.checkUniquenessOfEmail(emailId);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return flag;
	}

	/**
	 * @Description : To call payment service to remove saved card detail entry
	 */
	@Override
	public void removeCardDetail(final String cardToken)
	{
		try
		{
			//creating PaymentService of Juspay
			final PaymentService juspayService = new PaymentService();

			juspayService.setBaseUrl(configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.JUSPAYBASEURL));
			juspayService.withKey(
					configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
					.withMerchantId(
							configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));


			final DeleteCardRequest deleteCardRequest = new DeleteCardRequest();
			deleteCardRequest.setCardToken(cardToken);

			juspayService.deleteCard(deleteCardRequest);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 
	 * @see com.tisl.mpl.facades.account.register.MplCustomerProfileFacade#getYearAnniversaryList()
	 */
	@Override
	public List<YearData> getYearAnniversaryList()
	{
		try
		{
			//	creating list with year
			final List<YearData> yearAnniversaryList = new ArrayList<YearData>();
			for (int i = 1950; i < 2051; i++)
			{
				final String valAnniversary = String.valueOf(i);
				final YearData oYearDataAnniversary = new YearData();
				oYearDataAnniversary.setCode(valAnniversary);
				oYearDataAnniversary.setName(valAnniversary);
				yearAnniversaryList.add(oYearDataAnniversary);
			}
			return yearAnniversaryList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.account.register.MplCustomerProfileFacade#changePassword(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean changePassword(final String suppliedPassword)
	{
		try
		{
			boolean isSuppliedPwdCorrect = false;

			final UserModel currentUser = userService.getCurrentUser();

			if (null == currentUser.getPasswordEncoding())
			{
				currentUser.setPasswordEncoding(configurationService.getConfiguration().getString(PASSWORDENCODING));
			}
			final String encodedCurrentPassword = passwordEncoderService.encode(currentUser, suppliedPassword,
					currentUser.getPasswordEncoding());

			if (encodedCurrentPassword.equals(currentUser.getEncodedPassword()))
			{
				isSuppliedPwdCorrect = true;
			}
			return isSuppliedPwdCorrect;
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
	 * This method is called to get the data before editing
	 *
	 * @param email
	 */
	@Override
	public Map<String, String> setPreviousDataToMap(final String email, final String channel)
	{
		try
		{
			final Map<String, String> preSavedDetailMap = new HashMap<String, String>();
			final MplCustomerProfileData customerProfileData = getCustomerProfileDetail(email);

			if (StringUtils.isNotEmpty(customerProfileData.getFirstName())
					&& !customerProfileData.getFirstName().equals(MarketplacecommerceservicesConstants.SINGLE_SPACE))
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.F_NAME, customerProfileData.getFirstName());
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.F_NAME, MarketplacecommerceservicesConstants.EMPTY);
			}
			if (StringUtils.isNotEmpty(customerProfileData.getLastName())
					&& !customerProfileData.getLastName().equals(MarketplacecommerceservicesConstants.SINGLE_SPACE))
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.L_NAME, customerProfileData.getLastName());
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.L_NAME, MarketplacecommerceservicesConstants.EMPTY);
			}
			if (StringUtils.isNotEmpty(customerProfileData.getNickName()))
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.N_NAME, customerProfileData.getNickName());
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.N_NAME, MarketplacecommerceservicesConstants.EMPTY);
			}
			if (StringUtils.isNotEmpty(customerProfileData.getDateOfBirth()))
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.D_OF_BIRTH, customerProfileData.getDateOfBirth());
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.D_OF_BIRTH, MarketplacecommerceservicesConstants.EMPTY);
			}
			if (StringUtils.isNotEmpty(customerProfileData.getDateOfAnniversary()))
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.D_OF_ANNIVERSARY,
						customerProfileData.getDateOfAnniversary());
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.D_OF_ANNIVERSARY,
						MarketplacecommerceservicesConstants.EMPTY);
			}
			if (customerProfileData.getMobileNumber() != null && !customerProfileData.getMobileNumber().isEmpty())
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.M_NUMBER, customerProfileData.getMobileNumber());
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.M_NUMBER, MarketplacecommerceservicesConstants.EMPTY);
			}
			if (customerProfileData.getGender() != null)
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.GENDER_FIELD, customerProfileData.getGender());
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.GENDER_FIELD, MarketplacecommerceservicesConstants.EMPTY);
			}
			if (email != null && !email.isEmpty())
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.EMAIL_ID, email);
			}
			else
			{
				preSavedDetailMap.put(MarketplacecommerceservicesConstants.EMAIL_ID, MarketplacecommerceservicesConstants.EMPTY);
			}
			if (StringUtils.isNotEmpty(channel) && channel.equalsIgnoreCase("web"))
			{
				sessionService.setAttribute(MarketplacecommerceservicesConstants.PRE_SAVED_DETAIL_MAP, preSavedDetailMap);
				return null;
			}
			else
			{
				return preSavedDetailMap;
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * The method is called to compare the data before editing and after editing
	 *
	 * @param customerEmailId
	 */
	@SuppressWarnings("boxing")
	@Override
	public void checkChangesForSendingEmail(Map<String, String> preSavedDetailMap, final String customerEmailId,
			final String profileUpdateUrl)

	{
		try
		{
			final List<String> updatedDetailList = new ArrayList<String>();
			final MplCustomerProfileData customerProfileData = getCustomerProfileDetail(customerEmailId);

			if (null != sessionService.getAttribute(MarketplacecommerceservicesConstants.PRE_SAVED_DETAIL_MAP))
			{
				preSavedDetailMap = sessionService.getAttribute(MarketplacecommerceservicesConstants.PRE_SAVED_DETAIL_MAP);
			}
			final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);
			final SimpleDateFormat sdfDate = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT_INT);
			Integer preDob = Integer.valueOf(0);
			Integer currDob = Integer.valueOf(0);
			Integer preDoa = Integer.valueOf(0);
			Integer currDoa = Integer.valueOf(0);

			final Iterator<Map.Entry<String, String>> entries = preSavedDetailMap.entrySet().iterator();

			while (entries.hasNext())
			{
				final Map.Entry<String, String> entry = entries.next();

				if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.F_NAME)
						&& !customerProfileData.getFirstName().equals(MarketplacecommerceservicesConstants.SINGLE_SPACE)
						&& !entry.getValue().equalsIgnoreCase(customerProfileData.getFirstName()))
				{
					updatedDetailList.add(MarketplacecommerceservicesConstants.F_NAME_suffix);
				}
				else if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.L_NAME)
						&& !customerProfileData.getLastName().equals(MarketplacecommerceservicesConstants.SINGLE_SPACE)
						&& !entry.getValue().equalsIgnoreCase(customerProfileData.getLastName()))
				{
					updatedDetailList.add(MarketplacecommerceservicesConstants.L_NAME_suffix);
				}
				else if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.N_NAME)
						&& !entry.getValue().equalsIgnoreCase(customerProfileData.getNickName()))
				{
					updatedDetailList.add(MarketplacecommerceservicesConstants.N_NAME_suffix);
				}

				else if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.D_OF_BIRTH))
				{
					if (!StringUtils.isEmpty(entry.getValue()))
					{
						final Date dob = sdf.parse(entry.getValue());
						preDob = Integer.valueOf(sdfDate.format(dob));

						if (!StringUtils.isEmpty(customerProfileData.getDateOfBirth()))
						{
							final Date currentDob = sdf.parse(customerProfileData.getDateOfBirth());
							currDob = Integer.valueOf(sdfDate.format(currentDob));

							if ((preDob.compareTo(currDob) > 0) || (preDob.compareTo(currDob) < 0))
							{
								updatedDetailList.add(MarketplacecommerceservicesConstants.D_OF_BIRTH_suffix);
							}
						}
						else
						{
							updatedDetailList.add(MarketplacecommerceservicesConstants.D_OF_BIRTH_suffix);
						}
					}
					else
					{
						if (!StringUtils.isEmpty(customerProfileData.getDateOfBirth()))
						{
							updatedDetailList.add(MarketplacecommerceservicesConstants.D_OF_BIRTH_suffix);
						}
					}
				}


				else if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.D_OF_ANNIVERSARY))
				{
					if (!StringUtils.isEmpty(entry.getValue()))
					{
						final Date doa = sdf.parse(entry.getValue());

						preDoa = Integer.valueOf(sdfDate.format(doa));

						if (!StringUtils.isEmpty(customerProfileData.getDateOfAnniversary()))
						{
							final Date currentDoa = sdf.parse(customerProfileData.getDateOfAnniversary());
							currDoa = Integer.valueOf(sdfDate.format(currentDoa));

							if ((preDoa.compareTo(currDoa) > 0) || (preDoa.compareTo(currDoa) < 0))
							{
								updatedDetailList.add(MarketplacecommerceservicesConstants.D_OF_ANNIVERSARY_suffix);
							}
						}
						else
						{
							updatedDetailList.add(MarketplacecommerceservicesConstants.D_OF_ANNIVERSARY_suffix);
						}
					}
					else
					{
						if (!StringUtils.isEmpty(customerProfileData.getDateOfAnniversary()))
						{
							updatedDetailList.add(MarketplacecommerceservicesConstants.D_OF_ANNIVERSARY_suffix);
						}
					}
				}


				else if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.M_NUMBER)
						&& !entry.getValue().equalsIgnoreCase(customerProfileData.getMobileNumber()))
				{
					updatedDetailList.add(MarketplacecommerceservicesConstants.M_NUMBER_suffix);
				}

				else if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.GENDER_FIELD))
				{
					boolean flag = false;
					if (null != entry.getValue() && !MarketplacecommerceservicesConstants.EMPTY.equals(entry.getValue()))
					{
						if (entry.getValue().equalsIgnoreCase(customerProfileData.getGender()))
						{
							flag = false;
						}
						else
						{
							flag = true;
						}

						if (flag)
						{
							updatedDetailList.add(MarketplacecommerceservicesConstants.GENDER_suffix);
						}
					}
					else
					{
						if (StringUtils.isNotEmpty(customerProfileData.getGender()))
						{
							updatedDetailList.add(MarketplacecommerceservicesConstants.GENDER_suffix);
						}
					}
				}

				else if (entry.getKey().equalsIgnoreCase(MarketplacecommerceservicesConstants.EMAIL_ID)
						&& !entry.getValue().equalsIgnoreCase(customerProfileData.getEmailId()))
				{
					updatedDetailList.add(MarketplacecommerceservicesConstants.EMAIL_ID_suffix);
				}
			}

			//if (updatedDetailList.size() > 0)
			if (CollectionUtils.isNotEmpty(updatedDetailList))
			{
				sendEmailForUpdateCustomerProfile(updatedDetailList, profileUpdateUrl);
			}
		}
		catch (final NumberFormatException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0015);
		}
		catch (final ParseException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0014);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description This method will send e-mail after the updation of profile password
	 * @param currentUser
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void sendEmailForChangePassword(final CustomerModel currentUser)
	{
		try
		{
			final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel) businessProcessService
					.createProcess("changePasswordEmailProcess" + System.currentTimeMillis(), "changePasswordEmailProcess");
			storeFrontCustomerProcessModel.setSite(baseSiteService.getCurrentBaseSite());
			storeFrontCustomerProcessModel.setStore(baseStoreService.getCurrentBaseStore());
			storeFrontCustomerProcessModel.setCustomer(currentUser);
			storeFrontCustomerProcessModel.setLanguage(commonI18NService.getCurrentLanguage());
			storeFrontCustomerProcessModel.setCurrency(i18NService.getCurrentCurrency());
			modelService.save(storeFrontCustomerProcessModel);
			businessProcessService.startProcess(storeFrontCustomerProcessModel);
			currentUser.setIsTemporaryPasswordChanged(Boolean.TRUE);
			modelService.save(currentUser);
		}
		catch (final PasswordMismatchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B0009);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
