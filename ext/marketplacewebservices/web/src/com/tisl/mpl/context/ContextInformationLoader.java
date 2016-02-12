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
package com.tisl.mpl.context;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import com.tisl.mpl.exceptions.InvalidResourceException;
import com.tisl.mpl.exceptions.RecalculationException;
import com.tisl.mpl.exceptions.UnsupportedCurrencyException;
import com.tisl.mpl.exceptions.UnsupportedLanguageException;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface for context information loader
 */
public interface ContextInformationLoader
{
	/**
	 * Method resolves base site uid from URL and set it as current site i.e<br>
	 * <i>/rest/v1/mysite/cart</i>, or <br>
	 * <i>/rest/v1/mysite/customers/current</i><br>
	 * would try to set base site with uid=mysite as a current site.<br>
	 * 
	 * @param request
	 *           - request from which we should get base site uid
	 * 
	 * @return baseSite set as current site or null
	 * @throws InvalidResourceException
	 */
	BaseSiteModel initializeSiteFromRequest(final HttpServletRequest request) throws InvalidResourceException;

	/**
	 * Method set current language base on information from request
	 * 
	 * @param request
	 *           - request from which we should get language information
	 * @return language set as current
	 * @throws UnsupportedLanguageException
	 */
	LanguageModel setLanguageFromRequest(final HttpServletRequest request) throws UnsupportedLanguageException;

	/**
	 * Method set current currency based on information from request and recalculate cart for current session
	 * 
	 * @param request
	 *           - request from which we should get currency information
	 * @return currency set as current
	 * @throws UnsupportedCurrencyException
	 * @throws RecalculationException
	 */
	CurrencyModel setCurrencyFromRequest(final HttpServletRequest request) throws UnsupportedCurrencyException,
			RecalculationException;

}
