package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import com.tisl.mpl.core.model.CustomerOldEmailDetailsModel;



/**
 * The Interface ExtendedUserService - provides additional user service methods.
 *
 * @author Prium Reply
 */
public interface ExtendedUserService extends UserService
{


	/**
	 * Gets the user for uid and site id.
	 *
	 * @param userId
	 *           - unique customer id
	 * @param siteId
	 *           - site id for the current site
	 * @return unique user model (customer) for a unique id and site id.
	 */
	public UserModel getUserForUID(String userId);

	/**
	 * Gets the new and modified users of yesterday.
	 *
	 * @return the new and modified users of yesterday
	 */
	public List<CustomerModel> getNewAndModifiedUsersOfYesterday();

	/**
	 * Checks if email is unique for site.
	 *
	 * @param userId
	 *           the user id
	 * @param siteId
	 *           the site id
	 * @return true if customer will be unique
	 */
	public boolean isEmailUniqueForSite(String userId);

	/**
	 * Gets the user for uid and site.
	 *
	 * @param userId
	 *           the user id
	 * @param siteId
	 *           the site id
	 * @return userModel or null if the user doesn't exist
	 */
	public CustomerModel getUserForUid(String userId);

	/**
	 * Checks if the user is in the ALLOWED_TO_PURCHASE_GROUP
	 *
	 * @param userModel
	 *           the user model
	 * @return true, if is guest user
	 */
	public boolean isGuestUser(UserModel userModel);

	/**
	 * Checks if the user is in the REGISTERED_CUSTOMER_GROUP
	 *
	 * @param userModel
	 *           the user model
	 * @return true, if is registered user
	 */
	public boolean isRegisteredUser(UserModel userModel);

	/**
	 * Adds the customer to the REGISTERED_CUSTOMER_GROUP
	 *
	 * @param customerModel
	 *           the customer model
	 */
	public void addToRegisteredGroup(CustomerModel customerModel);

	/**
	 * @param newUid
	 * @return
	 */
	public boolean getCheckUniqueId(String newUid);

	/**
	 * @param originalUid
	 * @return CustomerModel
	 */
	public CustomerModel getUserForOriginalUid(final String originalUid);


	/**
	 * set old emaild when user is changing the emailId
	 *
	 * @param oldOriginalUid
	 * @return CustomerOldEmailDetailsModel
	 */
	public CustomerOldEmailDetailsModel getUserForOldOriginalUid(final String oldOriginalUid);

	/**
	 * @param originalUid
	 * @return
	 * @throws AmbiguousIdentifierException
	 */
	public UserModel getUserForEmailid(final String originalUid) throws AmbiguousIdentifierException;

	/**
	 * @param userId
	 * @return UserModel
	 */
	public UserModel getUserForUIDAccessToken(final String userId);

	/**
	 * @param uid
	 * @return CustomerModel
	 */
	public CustomerModel getUserForCustomerUid(final String uid);

	/**
	 * Check whether the user email is registered in teh DB via Social Login
	 *
	 * @param userId
	 * @return
	 */
	public boolean isUserRegisteredViaSocial(String userId);

}
