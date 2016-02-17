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



}
