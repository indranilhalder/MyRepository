package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.user.exceptions.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.core.model.CustomerOldEmailDetailsModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.ExtendedUserDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;


/**
 * The Class ExtendedUserServiceImpl.
 *
 * @author TCS
 */
public class ExtendedUserServiceImpl extends DefaultUserService implements ExtendedUserService
{
	@Autowired
	private ExtendedUserDaoImpl userDao;
	public static final String ALLOWED_TO_PURCHASE_GROUP = "allowedtopurchasegroup";
	public static final String REGISTERED_CUSTOMER_GROUP = "registeredcustomergroup";
	public static final String CUSTOMER_GROUP = "customergroup";
	public static final String GUEST_USER_EMAIL_PATTERN = "guest_checkout";
	public static final String COUNTRY_ISOCODE = "GB";

	/**
	 * @description Fetching user for UID
	 * @return UserModel
	 */
	@Override
	public UserModel getUserForUID(final String userId)
	{
		try
		{
			validateParameterNotNull(userId, MplConstants.M4_ASSERT_UID_NULL);
			final UserModel user = this.userDao.findUserByUID(userId.toLowerCase());
			if (user == null)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0006);
			}
			return user;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description Fetching user for UID
	 * @return UserModel
	 */
	@Override
	public UserModel getUserForUIDAccessToken(final String userId)
	{
		try
		{
			validateParameterNotNull(userId, MplConstants.M4_ASSERT_UID_NULL);
			final UserModel user = this.userDao.findUserByUID(userId.toLowerCase());

			return user;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description Fetching user for UID
	 * @return UserModel
	 */
	@Override
	public CustomerModel getUserForUid(final String userId)
	{
		try
		{
			validateParameterNotNull(userId, MplConstants.M4_ASSERT_UID_NULL);
			return this.userDao.findUserByUID(userId.toLowerCase());
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public CustomerModel getUserForOriginalUid(final String originalUid) throws AmbiguousIdentifierException
	{
		try
		{
			validateParameterNotNull(originalUid, MplConstants.M4_ASSERT_UID_NULL);
			return this.userDao.findUserByoriginalUID(originalUid.toLowerCase());
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	/**
	 * set old emaild when user is changing the emailId
	 *
	 * @param oldOriginalUid
	 * @return CustomerModel
	 */
	public CustomerOldEmailDetailsModel getUserForOldOriginalUid(final String oldOriginalUid) throws AmbiguousIdentifierException
	{
		try
		{
			validateParameterNotNull(oldOriginalUid, MplConstants.M4_ASSERT_UID_NULL);
			return this.userDao.findUserByOldoriginalUID(oldOriginalUid.toLowerCase());
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public UserModel getUserForEmailid(final String originalUid) throws AmbiguousIdentifierException
	{
		try
		{
			validateParameterNotNull(originalUid, MplConstants.M4_ASSERT_UID_NULL);
			return this.userDao.findUserByEmailId(originalUid.toLowerCase());
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description Fetching new and modified Users of previous day
	 * @return List<CustomerModel>
	 */
	@Override
	public List<CustomerModel> getNewAndModifiedUsersOfYesterday()
	{
		try
		{
			final Calendar yesterday_start = Calendar.getInstance();
			yesterday_start.set(Calendar.HOUR_OF_DAY, 0);
			yesterday_start.set(Calendar.MINUTE, 0);
			yesterday_start.set(Calendar.SECOND, 0);
			yesterday_start.set(Calendar.MILLISECOND, 0);
			yesterday_start.add(Calendar.DAY_OF_MONTH, -1);

			final Calendar yesterday_end = Calendar.getInstance();
			yesterday_end.set(Calendar.HOUR_OF_DAY, 23);
			yesterday_end.set(Calendar.MINUTE, 59);
			yesterday_end.set(Calendar.SECOND, 59);
			yesterday_end.set(Calendar.MILLISECOND, 999);
			yesterday_end.add(Calendar.DAY_OF_MONTH, -1);

			return this.userDao.findNewAndModifiedUsersByDateRange(yesterday_start.getTime(), yesterday_end.getTime());
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description checking uniqueness of email
	 * @return boolean
	 */
	@Override
	public boolean isEmailUniqueForSite(final String userId)
	{
		try
		{
			validateParameterNotNull(userId, MplConstants.M4_ASSERT_UID_NULL);
			final UserModel user = this.userDao.findUserByUID(userId.toLowerCase());
			if (user == null)
			{
				return true;
			}
			return false;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description checking guest user
	 * @return boolean
	 */
	@Override
	public boolean isGuestUser(final UserModel userModel)
	{
		try
		{
			final UserGroupModel userGroup = getUserGroupForUID(ALLOWED_TO_PURCHASE_GROUP);
			return isMemberOfGroup(userModel, userGroup);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description checking registered user
	 * @return boolean
	 */
	@Override
	public boolean isRegisteredUser(final UserModel userModel)
	{
		final UserGroupModel userGroup = getUserGroupForUID(REGISTERED_CUSTOMER_GROUP);
		return isMemberOfGroup(userModel, userGroup);
	}

	/**
	 * @description adding to registered user group
	 */
	@Override
	public void addToRegisteredGroup(final CustomerModel customerModel)
	{
		try
		{
			final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(customerModel.getGroups());
			/*
			 * final PrincipalGroupModel guestGroupModel = getUserGroupForUID(ALLOWED_TO_PURCHASE_GROUP); if
			 * (!newGroups.isEmpty() && newGroups.contains(guestGroupModel)) { newGroups.remove(guestGroupModel); }
			 */

			/*
			 * final PrincipalGroupModel registeredCustomerGroupModel = getUserGroupForUID(REGISTERED_CUSTOMER_GROUP);
			 * newGroups.add(registeredCustomerGroupModel);
			 */

			final PrincipalGroupModel customerGroupModel = getUserGroupForUID(CUSTOMER_GROUP);
			newGroups.add(customerGroupModel);

			customerModel.setGroups(newGroups);
			getModelService().save(customerModel);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description set password
	 */
	@Override
	public void setPassword(final String userId, final String password, final String encoding)
			throws PasswordEncoderNotFoundException
	{
		try
		{
			final UserModel userModel = getUserForUID(userId);
			setPassword(userModel, password, encoding);
			getModelService().save(userModel);
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description fetching password
	 * @return String
	 */
	@Override
	public String getPassword(final String userId) throws CannotDecodePasswordException, PasswordEncoderNotFoundException
	{
		try
		{
			final UserModel userModel = getUserForUID(userId);
			return getPassword(userModel);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description checking unique id
	 * @return boolean
	 */
	@Override
	public boolean getCheckUniqueId(final String userId)
	{
		try
		{
			validateParameterNotNull(userId, MplConstants.M4_ASSERT_UID_NULL);
			final UserModel user = this.userDao.findUserByUID(userId.toLowerCase());
			if (user == null)
			{
				return true;
			}
			return false;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description checking unique customer id
	 * @return CustomerModel
	 */
	@Override
	public CustomerModel getUserForCustomerUid(final String uid)
	{
		try
		{
			validateParameterNotNull(uid, MplConstants.M4_ASSERT_UID_NULL);
			return this.userDao.findCustomerModelByUID(uid);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public boolean isUserRegisteredViaSocial(final String userId)
	{
		try
		{
			validateParameterNotNull(userId, MplConstants.M4_ASSERT_UID_NULL);
			final CustomerModel user = this.userDao.findUserByUID(userId.toLowerCase());
			if (user != null)
			{
				final Boolean userType = user.getCustomerRegisteredBySocialMedia();
				return userType.booleanValue();
			}
			return false;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

}
