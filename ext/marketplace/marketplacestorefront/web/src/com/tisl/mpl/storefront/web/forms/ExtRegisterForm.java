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

import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;


public class ExtRegisterForm extends RegisterForm
{

	private String affiliateId;
	private boolean checkMyRewards;
	private String gender;


	//private String isFromLuxury;//TPR-6272 attribute added

	/**
	 * @return the affiliateId
	 */
	public String getAffiliateId()
	{
		return affiliateId;
	}

	/**
	 * @param affiliateId
	 *           the affiliateId to set
	 */
	public void setAffiliateId(final String affiliateId)
	{
		this.affiliateId = affiliateId;
	}

	/**
	 * @return the check_MyRewards
	 */
	public boolean isCheckMyRewards()
	{
		return checkMyRewards;
	}

	/**
	 * @param checkMyRewards
	 *           the check_MyRewards to set
	 */
	public void setCheckMyRewards(final boolean checkMyRewards)
	{
		this.checkMyRewards = checkMyRewards;
	}

	/*
	 * //TPR-6272 starts here
	 *//**
	 * @return the isFromLuxury
	 */
	/*
	 * public String getIsFromLuxury() { return isFromLuxury; }
	 *//**
	 * @param isFromLuxury
	 *           the isFromLuxury to set
	 */
	/*
	 * public void setIsFromLuxury(final String isFromLuxury) { this.isFromLuxury = isFromLuxury; }
	 * 
	 * //TPR-6272 ends here
	 */

	/**
	 * @return the gender
	 */
	public String getGender()
	{
		return gender;
	}

	/**
	 * @param gender
	 *           the gender to set
	 */
	public void setGender(final String gender)
	{
		this.gender = gender;
	}






}
