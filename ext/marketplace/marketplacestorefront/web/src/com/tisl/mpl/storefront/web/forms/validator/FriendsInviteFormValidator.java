/**
 *
 */
package com.tisl.mpl.storefront.web.forms.validator;

/**
 * @author TCS
 *
 */
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


import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tisl.mpl.storefront.web.forms.FriendsInviteForm;



/**
 * Validator for Emails
 */
@Component("friendsInviteFormValidator")
public class FriendsInviteFormValidator implements Validator
{

	public static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return FriendsInviteForm.class.equals(aClass);
	}

	/**
	 * @description method is used for validating the friends invite credentials
	 */
	@Override
	public void validate(final Object object, final Errors errors)
	{
		final FriendsInviteForm friendsInviteForm = (FriendsInviteForm) object;
		final String email = friendsInviteForm.getFriendsEmail();
		if (StringUtils.isEmpty(email))
		{
			errors.rejectValue("j_friendsEmail", "text.account.friendsInvite.emailInvalid");

		}
		else if (StringUtils.length(email) > 255 || !Pattern.matches(EMAIL_REGEX, email))
		{
			errors.rejectValue("j_friendsEmail", "text.account.friendsInvite.emailInvalid");
		}


	}
}
