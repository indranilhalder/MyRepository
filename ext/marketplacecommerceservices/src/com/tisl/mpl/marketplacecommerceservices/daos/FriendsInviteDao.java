/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import com.tisl.mpl.core.model.FriendsModel;


/**
 * @author TCS
 *
 */
public interface FriendsInviteDao
{
	public List<FriendsModel> findFriendsForACustomer(final String customerEmail);

	/**
	 * @param affiliateId
	 * @param friendsEmail
	 * @return
	 */
	@SuppressWarnings("javadoc")
	public List<FriendsModel> findFriendInvitedForAnAffiliate(String affiliateId, String friendsEmail);

	/**
	 * @param affiliateId
	 * @return
	 */
	public List<FriendsModel> findFriendsInvitedForAnAffiliate(String affiliateId);

	/**
	 * @param friendsModel
	 * @return
	 */
	public List<FriendsModel> findFriendsInvitedByAffIdAndFriendsEmail(FriendsModel friendsModel);

	/**
	 * @param uid
	 * @return
	 */
	public List<CustomerModel> findCustomerForUid(String uid);
}
