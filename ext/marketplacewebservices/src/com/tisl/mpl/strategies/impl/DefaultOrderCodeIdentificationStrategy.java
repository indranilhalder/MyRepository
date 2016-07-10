/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.strategies.impl;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.strategies.OrderCodeIdentificationStrategy;


/**
 * Default implementation of {@link com.tisl.mpl.strategies.OrderCodeIdentificationStrategy}.
 */
public class DefaultOrderCodeIdentificationStrategy implements OrderCodeIdentificationStrategy
{
	//private boolean failIfNotFound; Critical Sonar fixes
	private String idPattern;

	/**
	 * Checks if given string is GUID
	 *
	 * @param potentialId
	 *           - string to check
	 * @return result
	 */
	@Override
	public boolean isID(final String potentialId)
	{
		validateParameterNotNull(potentialId, "identifier must not be null");
		boolean isIdStatus = false;


		if (StringUtils.isNotEmpty(potentialId))
		{
			final Pattern pattern = Pattern.compile(this.idPattern);
			final Matcher matcher = pattern.matcher(potentialId);
			isIdStatus = matcher.find();
		}

		return isIdStatus;

		//		if (potentialId == null || potentialId.isEmpty())
		//		{
		//			return false;
		//		}
		//		if (matcher.find())
		//		{
		//			return true;
		//		}
		//		else
		//		{
		//			return false;
		//		}
	}

	@Required
	public void setIdPattern(final String idPattern)
	{
		this.idPattern = idPattern;
	}
}
