/**
 *
 */
package com.tisl.mpl.facades.account.register;

import java.util.List;

import com.tisl.mpl.data.FriendsInviteData;



/**
 * @author TCS
 *
 */
public interface FriendsInviteFacade
{
	/**
	 * @description This method is used to invite friends
	 * @return FriendsInviteData
	 */
	public boolean inviteFriends(final FriendsInviteData friendsInviteData);

	/**
	 * @param textMessage
	 * @description This method is used to initiate mail with Inviting friends
	 */
	void sendInvite(final FriendsInviteData friendsInviteData, String textMessage);

	/**
	 * @description This method is used to update friends model in case of requested registration
	 */
	void updateFriendsModel(FriendsInviteData friendsData);

	/**
	 * @param friendsEmailList
	 */
	boolean checkUniquenessOfEmail(List<String> friendsEmailList);

	/**
	 * @param friendsEmailList
	 * @return
	 */
	boolean isEmailEqualsToCustomer(List<String> friendsEmailList);


}
