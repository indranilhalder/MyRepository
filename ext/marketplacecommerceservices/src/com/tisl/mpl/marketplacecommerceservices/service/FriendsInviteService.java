/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import com.tisl.mpl.core.model.FriendsModel;
import com.tisl.mpl.data.FriendsInviteData;


/**
 * @author 314180
 *
 */
public interface FriendsInviteService
{
	/**
	 * @description this is called to get invite Friends
	 * @return FriendsModel
	 */
	public FriendsModel inviteFriends(final CustomerModel customer);

	/**
	 * @description this is called to get Friends already Invited
	 * @return List<FriendsModel>
	 */
	public List<FriendsModel> getFriendsInvited(final String customerEmail);

	/**
	 * @description this is called to get Friend Invited For An Affiliated Id
	 * @return List<FriendsModel>
	 */
	public List<FriendsModel> getFriendsInvitedForAnAffiliate(final String affiliateId);

	/**
	 * @description this is called to get Friend Invited For An Affiliated Id
	 * @return List<FriendsModel>
	 */
	public List<FriendsModel> getFriendInvitedForAnAffiliate(final String affiliateId, final String friendsEmail);

	/**
	 * @description this is called to update the registered email flag
	 */
	void updateFriendsModel(FriendsModel friendsModel);

	/**
	 * @param emailId
	 */
	public CustomerModel checkUniquenessOfEmail(String emailId);

	/**
	 * @param friendsInviteData
	 * @return FriendsModel
	 */
	public boolean inviteNewFriends(FriendsInviteData friendsInviteData);

}
