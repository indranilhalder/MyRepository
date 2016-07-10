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
package com.tisl.mpl.controllers.pages;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public interface MplCheckoutStepController
{
	/**
	 * @param loginError
	 * @param session
	 * @param model
	 * @param request
	 * @return
	 * @throws CMSItemNotFoundException
	 * @throws CommerceCartModificationException
	 */
	@SuppressWarnings("javadoc")
	String enterStep(boolean loginError, HttpSession session, Model model, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, CommerceCartModificationException;

	/**
	 *
	 * @param redirectAttributes
	 * @return checkout step link for navigating back
	 */
	String back(final RedirectAttributes redirectAttributes);

	/**
	 *
	 * @param redirectAttributes
	 * @return checkoutstep link for navigating next
	 */
	String next(final RedirectAttributes redirectAttributes);

}
