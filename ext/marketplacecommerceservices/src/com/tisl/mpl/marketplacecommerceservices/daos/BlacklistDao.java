/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.BlacklistModel;


/**
 * @author TCS
 *
 */
public interface BlacklistDao
{
	/**
	 * It gets the list of blacklisted customers
	 *
	 * @return List<BlacklistModel>
	 *
	 */
	List<BlacklistModel> getAllCustomers();

	/**
	 * It gets the list of all the customers from the blacklist model with the given emailId
	 *
	 * @param emailId
	 * @return List<BlacklistModel>
	 *
	 */
	List<BlacklistModel> getBlacklistedCustomer(final String emailId);

	/**
	 * It checks whether the customer exists in blacklist model or not
	 *
	 * @param emailId
	 * @return boolean
	 *
	 */
	boolean isExistingCustomer(final String emailId);

	/**
	 * It checks whether the customer is valid or not
	 *
	 * @param emailId
	 * @return boolean
	 *
	 */
	boolean isValidUser(String emailId);

	/**
	 * It will take the customer id from the customer model
	 *
	 * @param emailId
	 * @return List<CustomerModel>
	 *
	 */
	List<CustomerModel> getId(String emailId);

	/**
	 * It will check whether the provided customer is black listed or not.
	 *
	 * @param customerPk
	 * @param emailId
	 * @param phoneNumber
	 * @param ipAddress
	 * @return List<BlacklistModel>
	 */
	List<BlacklistModel> isBlackListedCustomer(final String customerPk, final String emailId, final String phoneNumber,
			final String ipAddress);

	/**
	 * It gets the list of all the blacklist customers with the given phoneNumber
	 *
	 * @param phoneNumber
	 * @return List<BlacklistModel>
	 *
	 */
	List<BlacklistModel> getMobileNumber(final String phoneNumber);

	/**
	 * @param customer
	 * @param ipAddress
	 * @return boolean
	 */
	boolean isBlackListed(CustomerModel customer, String ipAddress);

	/**
	 * @Description : Returns data within a date range
	 * @param startDate
	 * @param endDate
	 * @return List<BlacklistModel>
	 */
	List<BlacklistModel> getSpecificCustomers(Date startDate, Date endDate);


}
