/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.web.forms;

import javax.validation.constraints.NotNull;


public class FriendsInviteForm
{
	@NotNull(message = "{general.required}")
	private String friendsEmail;

	/**
	 * @return the friendsEmail
	 */
	public String getFriendsEmail()
	{
		return friendsEmail;
	}

	/**
	 * @param friendsEmail
	 *           the friendsEmail to set
	 */
	public void setFriendsEmail(final String friendsEmail)
	{
		this.friendsEmail = friendsEmail;
	}


}
