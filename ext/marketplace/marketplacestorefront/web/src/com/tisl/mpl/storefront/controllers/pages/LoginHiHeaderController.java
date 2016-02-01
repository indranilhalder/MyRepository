/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.HEADER_LOGIN_HI)
public class LoginHiHeaderController
{

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		model.addAttribute("userName", currentCustomer.getFirstName());
		if (!userService.isAnonymousUser(currentCustomer))
		{
			return ControllerConstants.Views.Fragments.Home.MyAccountPanel;
		}
		else
		{
			return ControllerConstants.Views.Fragments.Home.LoginPanel;
		}
	}
}
