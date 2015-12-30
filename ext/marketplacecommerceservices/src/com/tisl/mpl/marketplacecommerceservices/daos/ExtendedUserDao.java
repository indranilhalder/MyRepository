package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.CustomerOldEmailDetailsModel;



/**
 * The Interface ExtendedUserDao - Dao to retrieve customers from the database used by {@link ExtendedUserServiceImpl}.
 *
 *
 */
/**
 * @author TCS
 *
 */
public interface ExtendedUserDao
{

	/**
	 * Finds user by uid and site id.
	 *
	 * @param uid
	 *           the uid
	 * @param siteId
	 *           the site id
	 * @return the customer model
	 */
	CustomerModel findUserByUID(final String uid);

	/**
	 * Find all users.
	 *
	 * @return the list of Customers
	 */
	List<CustomerModel> findAllUsers();

	/**
	 * Finds new and modified users by date range.
	 *
	 * @param startDate
	 *           the start date for the search
	 * @param endDate
	 *           the end date for the search
	 * @return the list of Customers for specifc time range
	 */
	List<CustomerModel> findNewAndModifiedUsersByDateRange(Date startDate, Date endDate);

	/**
	 * @param originalUID
	 * @return CustomerModel
	 */
	public CustomerModel findUserByoriginalUID(final String originalUID);

	/**
	 * set old emaild when user is changing the emailId
	 *
	 * @param oldOriginalUID
	 * @return CustomerModel
	 */
	public CustomerOldEmailDetailsModel findUserByOldoriginalUID(final String oldOriginalUID);

	/**
	 * @param uid
	 * @return UserModel
	 */
	public UserModel findUserByEmailId(final String uid);

	/**
	 * @param uid
	 * @return CustomerModel
	 */
	public CustomerModel findCustomerModelByUID(final String uid);

}
