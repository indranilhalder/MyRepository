package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.model.BlacklistModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BlacklistDao;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;


/**
 * @author TCS
 *
 */
public class BlacklistServiceImpl implements BlacklistService
{
	private BlacklistDao blacklistDao;
	private UserService userService;
	private static final Logger LOG = Logger.getLogger(BlacklistServiceImpl.class);

	/**
	 * Checks whether the customer is blacklisted or not
	 *
	 * @param uid
	 * @return boolean
	 *
	 */
	@Override
	public boolean blacklistedOrNot(final String uid)
	{

		final UserModel user = getUserService().getUserForUID(uid);
		final List<BlacklistModel> customerList = getBlacklistDao().getAllCustomers();
		boolean blacklistedOrNot = false;
		for (final BlacklistModel customer : customerList)
		{
			if (null != customer.getCustomerId() && null != customer.getCustomerId().getUid())
			{
				if (user.getUid().equals(customer.getCustomerId().getUid()))
				{
					blacklistedOrNot = true;
					break;
				}
			}
		}
		return blacklistedOrNot;

	}

	/**
	 * Checks whether the Mobile Number is blacklisted or not
	 *
	 * @param phoneNumber
	 * @return boolean
	 *
	 */
	@Override
	public boolean mobileBlacklistedOrNot(final String phoneNumber)
	{

		final List<BlacklistModel> mobileList = getBlacklistDao().getMobileNumber(phoneNumber);
		boolean flag = false;

		if (null == mobileList || mobileList.isEmpty())
		{
			flag = true;
		}
		return flag;
	}




	/**
	 * Checks whether the email id exists or not
	 *
	 * @param uid
	 * @return boolean
	 *
	 */
	@Override
	public boolean checkExistence(final String uid)
	{
		return getBlacklistDao().isValidUser(uid);
	}


	/**
	 * It will take the customer id from the customer model
	 *
	 * @param customerId
	 * @return List<CustomerModel>
	 *
	 */
	@Override
	public List<CustomerModel> addId(final String customerId)
	{
		return getBlacklistDao().getId(customerId);
	}


	/**
	 * It checks whether the customer exists in blacklist model or not
	 *
	 * @param emailId
	 * @return boolean
	 *
	 */
	@Override
	public boolean isExistingCustomer(final String emailId)
	{
		return getBlacklistDao().isExistingCustomer(emailId);
	}

	/**
	 * It gets the list of all the customers from the blacklist model with the given emailId
	 *
	 * @param emailId
	 * @return List<BlacklistModel>
	 *
	 */
	@Override
	public List<BlacklistModel> getBlacklistedCustomer(final String emailId)
	{
		return getBlacklistDao().getBlacklistedCustomer(emailId);
	}

	/**
	 * It gets the list of all the customers from the blacklist model
	 *
	 * @param customerPk
	 * @param emailId
	 * @param phoneNumber
	 * @param ipAddress
	 * @return Boolean
	 *
	 */
	@Override
	public Boolean getBlacklistedCustomerforCOD(final String customerPk, final String emailId, final String phoneNumber,
			final String ipAddress) throws EtailNonBusinessExceptions
	{
		Boolean isBalcklisted = Boolean.FALSE;
		try
		{
			final List<BlacklistModel> blacklistedCustomerList = getBlacklistDao().isBlackListedCustomer(customerPk, emailId,
					phoneNumber, ipAddress);
			if (null != blacklistedCustomerList && !(blacklistedCustomerList.isEmpty()))
			{
				isBalcklisted = Boolean.TRUE;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error("Exception while fetching blacklisted customer for COD==== ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Exception while fetching blacklisted customer for COD==== ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception while fetching blacklisted customer for COD==== ", e);
		}

		return isBalcklisted;
	}


	/**
	 * It gets the list of blacklisted customers
	 *
	 * @return List<BlacklistModel>
	 *
	 */
	@Override
	public List<BlacklistModel> getAllBlacklistedCustomers()
	{
		return getBlacklistDao().getAllCustomers();
	}


	/**
	 * This method returns whether a customer is blacklisted or not
	 *
	 * @param customer
	 * @param ipAddress
	 * @return boolean
	 *
	 */
	@Override
	public boolean blacklistedOrNot(final UserModel customer, final String ipAddress)
	{
		boolean blacklistedOrNot = true;
		if (customer instanceof CustomerModel)
		{
			//return getBlaDao().isBlackListed((CustomerModel) customer, ipAddress);	SONAR Fix
			blacklistedOrNot = getBlacklistDao().isBlackListed((CustomerModel) customer, ipAddress);
		}
		//return true; SONAR Fix
		return blacklistedOrNot;
	}


	/**
	 * @Desctription : This Method returns data within a date range
	 *
	 * @param startDate
	 * @param endDate
	 * @return List<BlacklistModel>
	 *
	 */
	@Override
	public List<BlacklistModel> getSpecificBlacklistedCustomers(final Date startDate, final Date endDate)
	{
		return getBlacklistDao().getSpecificCustomers(startDate, endDate);
	}



	//Getters and Setters
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
	 * @return the blacklistDao
	 */
	public BlacklistDao getBlacklistDao()
	{
		return blacklistDao;
	}

	/**
	 * @param blacklistDao
	 *           the blacklistDao to set
	 */
	public void setBlacklistDao(final BlacklistDao blacklistDao)
	{
		this.blacklistDao = blacklistDao;
	}
}
