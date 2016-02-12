/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.GenderData;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCustomerProfileDao;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.ExtendedUserDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;




/**
 * @author 594031
 *
 */
public class MplCustomerProfileServiceImpl implements MplCustomerProfileService
{
	@Autowired
	private MplCustomerProfileDao mplCustomerProfileDao;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;
	@Autowired
	private UserService userService;
	@Autowired
	private ExtendedUserService extUserService;
	@Autowired
	private PasswordEncoderService passwordEncoderService;
	@Autowired
	private ExtendedUserDaoImpl userDao;


	/**
	 * @return the mplCustomerProfileDao
	 */
	public MplCustomerProfileDao getMplCustomerProfileDao()
	{
		return mplCustomerProfileDao;
	}

	/**
	 * @param mplCustomerProfileDao
	 *           the mplCustomerProfileDao to set
	 */
	public void setMplCustomerProfileDao(final MplCustomerProfileDao mplCustomerProfileDao)
	{
		this.mplCustomerProfileDao = mplCustomerProfileDao;
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
	 * @return the mplEnumerationHelper
	 */
	public MplEnumerationHelper getMplEnumerationHelper()
	{
		return mplEnumerationHelper;
	}

	/**
	 * @param mplEnumerationHelper
	 *           the mplEnumerationHelper to set
	 */
	public void setMplEnumerationHelper(final MplEnumerationHelper mplEnumerationHelper)
	{
		this.mplEnumerationHelper = mplEnumerationHelper;
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
	 * @return the userDao
	 */
	public ExtendedUserDaoImpl getUserDao()
	{
		return userDao;
	}

	/**
	 * @param userDao
	 *           the userDao to set
	 */
	public void setUserDao(final ExtendedUserDaoImpl userDao)
	{
		this.userDao = userDao;
	}



	private final SimpleDateFormat sdf = new SimpleDateFormat(MarketplacecommerceservicesConstants.DMY_DATE_FORMAT);

	/**
	 * @description this method is called to get Customer Profile Detail
	 * @return MplCustomerProfileData
	 */
	@Override
	public MplCustomerProfileData getCustomerProfileDetail(final String emailId)
	{
		try
		{
			final CustomerModel oCustomerModel = new CustomerModel();
			oCustomerModel.setOriginalUid(emailId);
			//	calling DAO to fetch Customer Profile Detail
			final List<CustomerModel> customerProfileDetail = mplCustomerProfileDao.getCustomerProfileDetail(oCustomerModel);

			MplCustomerProfileData oMplCustomerProfileData = new MplCustomerProfileData();
			//if (null != customerProfileDetail && customerProfileDetail.size() > 0)
			if (CollectionUtils.isNotEmpty(customerProfileDetail))
			{
				for (final CustomerModel customerModelData : customerProfileDetail)
				{
					oMplCustomerProfileData = setModelToData(customerModelData);
					break;
				}
			}
			return oMplCustomerProfileData;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this method is called to save data to model
	 * @param customerModelData
	 * @return MplCustomerProfileData
	 */
	private MplCustomerProfileData setModelToData(final CustomerModel customerModelData)
	{
		try
		{
			final MplCustomerProfileData mplCustomerProfileData = new MplCustomerProfileData();


			// set FIRST NAME
			if (null != customerModelData.getFirstName())
			{
				mplCustomerProfileData.setFirstName(customerModelData.getFirstName());
			}
			else
			{
				mplCustomerProfileData.setFirstName(MarketplacecommerceservicesConstants.EMPTY);
			}

			// set LAST NAME
			if (null != customerModelData.getLastName())
			{
				mplCustomerProfileData.setLastName(customerModelData.getLastName());
			}
			else
			{
				mplCustomerProfileData.setLastName(MarketplacecommerceservicesConstants.EMPTY);
			}

			// set MIDDLE NAME
			if (null != customerModelData.getNickName())
			{
				mplCustomerProfileData.setNickName(customerModelData.getNickName());
			}
			else
			{
				mplCustomerProfileData.setNickName(MarketplacecommerceservicesConstants.EMPTY);
			}

			// set DATE OF ANNIVERSARY
			if (null != customerModelData.getDateOfAnniversary())
			{
				mplCustomerProfileData.setDateOfAnniversary(sdf.format(customerModelData.getDateOfAnniversary()));
			}
			else
			{
				mplCustomerProfileData.setDateOfAnniversary(MarketplacecommerceservicesConstants.EMPTY);
			}

			// set DATE OF BIRTH
			if (null != customerModelData.getDateOfBirth())
			{
				mplCustomerProfileData.setDateOfBirth(sdf.format(customerModelData.getDateOfBirth()));
			}
			else
			{
				mplCustomerProfileData.setDateOfBirth(MarketplacecommerceservicesConstants.EMPTY);
			}

			// set MOBILE NO
			if (null != customerModelData.getMobileNumber())
			{
				mplCustomerProfileData.setMobileNumber(customerModelData.getMobileNumber());
			}
			else
			{
				mplCustomerProfileData.setMobileNumber(MarketplacecommerceservicesConstants.EMPTY);
			}

			// set GENDER
			if (null != customerModelData.getGender())
			{
				if (customerModelData.getGender().equals(Gender.MALE))
				{
					mplCustomerProfileData.setGender(MarketplacecommerceservicesConstants.MALE);
				}
				else if (customerModelData.getGender().equals(Gender.FEMALE))
				{
					mplCustomerProfileData.setGender(MarketplacecommerceservicesConstants.FEMALE);
				}
			}
			else
			{
				mplCustomerProfileData.setGender(MarketplacecommerceservicesConstants.EMPTY);
			}

			// set UID
			if (null != customerModelData.getUid())
			{
				mplCustomerProfileData.setUid(customerModelData.getUid());
			}
			else
			{
				mplCustomerProfileData.setUid(MarketplacecommerceservicesConstants.EMPTY);
			}
			// set email
			if (null != customerModelData.getOriginalUid())
			{
				mplCustomerProfileData.setEmailId(customerModelData.getOriginalUid());
			}
			else
			{
				mplCustomerProfileData.setEmailId(MarketplacecommerceservicesConstants.EMPTY);
			}
			return mplCustomerProfileData;
		}
		catch (final NullPointerException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this method is called to save data to model
	 * @param oCustomerModel
	 *           ,name,emailUid
	 */
	@Override
	public void updateCustomerProfile(final CustomerModel oCustomerModel, final String name, final String uid)
	{
		try
		{
			oCustomerModel.setUid(uid);
			oCustomerModel.setName(name);
			oCustomerModel.setTitle(null);

			internalSaveCustomer(oCustomerModel);
		}
		catch (final DuplicateUidException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B0002);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this method is called internally to save data to model
	 * @param customerModel
	 * @throws DuplicateUidException
	 */
	private void internalSaveCustomer(final CustomerModel customerModel) throws DuplicateUidException
	{
		try
		{
			modelService.save(customerModel);
		}
		catch (final ModelSavingException ex)
		{
			if ((ex.getCause() instanceof InterceptorException)
					&& (((InterceptorException) ex.getCause()).getInterceptor().getClass().equals(UniqueAttributesInterceptor.class)))
			{
				throw new DuplicateUidException(MarketplacecommerceservicesConstants.B0002, ex);
			}

			throw new EtailNonBusinessExceptions(ex);
		}
		catch (final AmbiguousIdentifierException ex)
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0002, ex);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this method calls mplEnumerationHelper to fetch Gender enum values
	 * @return List<GenderData>
	 */
	@Override
	public List<GenderData> getGenders()
	{
		try
		{
			final List<GenderData> genderList = new ArrayList<GenderData>();

			final List<EnumerationValueModel> enumList = mplEnumerationHelper.getEnumerationValuesForCode(Gender._TYPECODE);
			//if (null != enumList && enumList.size() > 0)
			if (CollectionUtils.isNotEmpty(enumList))
			{
				for (int i = 0; i < enumList.size(); i++)
				{
					EnumerationValueModel oModelData = new EnumerationValueModel();
					oModelData = enumList.get(i);

					final GenderData oGenderData = new GenderData();
					oGenderData.setCode(oModelData.getCode());
					oGenderData.setName(oModelData.getName());
					genderList.add(oGenderData);
				}
			}
			return genderList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description this method is called to change uid of customer
	 * @param newUid
	 *           ,currentPassword
	 */
	@Override
	public void changeUid(final String newUid, final String currentPassword) throws DuplicateUidException,
			PasswordMismatchException
	{
		try
		{
			final String newUidLower = newUid.toLowerCase();
			final CustomerModel currentUser = (CustomerModel) userService.getCurrentUser();

			if (!currentUser.getName().equals(MarketplacecommerceservicesConstants.EMPTY)
					&& (currentUser.getName().equalsIgnoreCase(currentUser.getOriginalUid())))
			{
				currentUser.setName(newUid);
			}
			currentUser.setOriginalUid(newUid);

			if (!(currentUser.getUid().equalsIgnoreCase(newUid)))
			{
				checkUidUniqueness(newUidLower);
			}
			adjustPassword(currentUser, newUidLower, currentPassword);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description this method is called to change uid of customer
	 * @param newUid
	 *           ,currentPassword
	 */
	@Override
	public boolean checkUniquenessOfEmail(final String newUid)
	{
		boolean flag = true;
		try
		{
			final String newUidLower = newUid.toLowerCase();
			final CustomerModel currentUser = (CustomerModel) userService.getCurrentUser();

			if (!(currentUser.getOriginalUid().equalsIgnoreCase(newUidLower)))
			{
				if (!extUserService.getCheckUniqueId(newUidLower))
				{
					flag = false;
				}
				else
				{
					flag = true;
				}
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return flag;
	}

	/**
	 * @description this method is called internally to adjust Password
	 * @param currentUser
	 * @param newUid
	 * @param currentPassword
	 * @throws PasswordMismatchException
	 */
	private void adjustPassword(final UserModel currentUser, final String newUid, final String currentPassword)
			throws PasswordMismatchException
	{
		try
		{
			final String encodedCurrentPassword = passwordEncoderService.encode(currentUser, currentPassword,
					currentUser.getPasswordEncoding());
			if (!(encodedCurrentPassword.equals(currentUser.getEncodedPassword())))
			{
				throw new PasswordMismatchException(currentUser.getUid());
			}
			modelService.save(currentUser);
			final CustomerModel currentUserLastOrgUid = (CustomerModel) userService.getCurrentUser();
			setUserPassword(currentUserLastOrgUid.getOriginalUid());
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
	 * @description this method is called internally to set User Password
	 * @param userId
	 */
	private void setUserPassword(final String userId)
	{
		try
		{
			final UserModel userModel = getUserForUID(userId);
			modelService.save(userModel);
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
	 * @description this method is called internally to get User For UID
	 * @param userId
	 * @return UserModel
	 */
	private UserModel getUserForUID(final String userId)
	{
		try
		{
			validateParameterNotNull(userId, MplConstants.M4_ASSERT_UID_NULL);
			final UserModel user = this.userDao.findUserByUID(userId.toLowerCase());
			if (user == null)
			{
				throw new UnknownIdentifierException(MplConstants.M11_CANNOT_FIND_THE_UID + userId);
			}
			return user;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description this method is called to check Uid Uniqueness
	 * @param newUid
	 * @throws DuplicateUidException
	 */
	private void checkUidUniqueness(final String newUid) throws DuplicateUidException
	{
		try
		{
			if (!extUserService.getCheckUniqueId(newUid))
			{
				throw new DuplicateUidException(MarketplacecommerceservicesConstants.B0002);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}
}
