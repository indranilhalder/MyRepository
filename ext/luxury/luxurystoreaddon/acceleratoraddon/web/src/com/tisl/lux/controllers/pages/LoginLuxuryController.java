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

import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import com.tisl.mpl.storefront.web.forms.ExtRegisterForm;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.lux.constants.RequestMappingUrlConstants;
import com.tisl.lux.controllers.LuxurystoreaddonControllerConstants;
import org.springframework.web.bind.annotation.RequestParam;


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
	protected static final String IS_SIGN_IN_ACTIVE = "isSignInActive";
	protected static final String Y_CAPS_VAL = "Y";
	private static final Logger LOG = Logger.getLogger(LoginLuxuryController.class);


	@RequestMapping(method = RequestMethod.GET, value = "/signin")
	public String getLoginFragment(final Model model, final LoginForm form, @RequestParam(value = IS_SIGN_IN_ACTIVE, required = false) final String isSignInActive)
	{
//		LOG.info("inside getLoginFragment");
//		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
//		model.addAttribute("userName", currentCustomer.getFirstName());

		if (null != isSignInActive && !StringUtils.isEmpty(isSignInActive)){
			model.addAttribute(IS_SIGN_IN_ACTIVE, isSignInActive);
		} else {
			model.addAttribute(IS_SIGN_IN_ACTIVE, Y_CAPS_VAL);
		}

		return LuxurystoreaddonControllerConstants.Views.Fragments.Home.LoginPanelFragment;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/register")
	public String getRegisterForm(final Model model, final ExtRegisterForm form, @RequestParam(value = IS_SIGN_IN_ACTIVE, required = false) final String isSignInActive)
	{
		if (null != isSignInActive && !StringUtils.isEmpty(isSignInActive)){
			model.addAttribute(IS_SIGN_IN_ACTIVE, isSignInActive);
		} else {
			model.addAttribute(IS_SIGN_IN_ACTIVE, Y_CAPS_VAL);
		}
		return LuxurystoreaddonControllerConstants.Views.Fragments.Home.RegisterFragment;
	}

}
