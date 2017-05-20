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
package com.tisl.lux.controllers.pages;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.lux.constants.RequestMappingUrlConstants;
import com.tisl.lux.controllers.LuxurystoreaddonControllerConstants;


/**
 * @author vishal.parmar
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.HEADER_LUXURY_LOGIN)
public class LoginLuxuryController
{

	@Autowired
	private UserService userService;
	@Autowired
	private SessionService sessionService;
	protected static final String SPRING_SECURITY_LAST_USERNAME = "SPRING_SECURITY_LAST_USERNAME";

	@RequestMapping(method = RequestMethod.GET, value = "/signin")
	public String getLoginFragment(final Model model)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		model.addAttribute("userName", currentCustomer.getFirstName());
		return LuxurystoreaddonControllerConstants.Views.Fragments.Home.LoginPanelFragment;
	}

}
