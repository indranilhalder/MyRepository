/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.BlacklistModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface BlacklistService
{
	/**
	 * Checks whether the customer is blacklisted or not
	 *
	 * @param uId
	 * @return boolean
	 *
	 */
	boolean blacklistedOrNot(String uId);

	/**
	 * Checks whether the email id exists or not
	 *
	 * @param uid
	 * @return boolean
	 *
	 */
	boolean checkExistence(String uid);

	/**
	 * It will take the customer id from the customer model
	 *
	 * @param customerId
	 * @return List<CustomerModel>
	 *
	 */
	List<CustomerModel> addId(String customerId);

	/**
	 * It checks whether the customer exists in blacklist model or not
	 *
	 * @param emailId
	 * @return boolean
	 *
	 */
	boolean isExistingCustomer(final String emailId);

	/**
	 * It gets the list of all the customers from the blacklist model with the given emailId
	 *
	 * @param emailId
	 * @return List<BlacklistModel>
	 *
	 */
	List<BlacklistModel> getBlacklistedCustomer(final String emailId);

	/**
	 * It gets the list of blacklisted customers
	 *
	 * @return List<BlacklistModel>
	 *
	 */
	List<BlacklistModel> getAllBlacklistedCustomers();

	/**
	 *
	 * Checks whether the customer is blacklisted or not
	 *
	 * @param Uid
	 * @return boolean
	 *
	 */
	boolean blacklistedOrNot(UserModel customer, String ipAddress);


	/**
	 * Checks whether the mobile number is blacklisted or not
	 *
	 * @param phoneNumbers
	 *
	 * @return boolean
	 */
	boolean mobileBlacklistedOrNot(String phoneNumbers);

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
	Boolean getBlacklistedCustomerforCOD(final String customerPk, final String emailId, final String phoneNumber,
			final String ipAddress) throws EtailNonBusinessExceptions;

	/**
	 * Returns data within a date range
	 *
	 * @param startDate
	 * @param endDate
	 * @return List<BlacklistModel>
	 */
	List<BlacklistModel> getSpecificBlacklistedCustomers(Date startDate, Date endDate);

}
