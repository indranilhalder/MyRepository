/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.GenericUtilityMethods;


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

	//SONAR FIX
	//@Autowired
	//private ConfigurationService configurationService; //Added for UF-93  

	//SONAR FIX
	//private static final Logger LOG = Logger.getLogger(LoginHiHeaderController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String get(final HttpServletRequest request, final HttpServletResponse response, final Model model)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		model.addAttribute("userName", currentCustomer.getFirstName());
		/** Added for UF-93 to show the last logged in user in log in field **/
		final Cookie cookie = GenericUtilityMethods.getCookieByName(request, "LastUserLogedIn");
		if (null != cookie && null != cookie.getValue())
		{
			final String encodedCookieValue = cookie.getValue();

			final String decodedCookieValue = new String(Base64.decodeBase64(encodedCookieValue.getBytes())); // No need of encodedCookieValue null check as cookie.value is check earlier.
			model.addAttribute("lastLoggedInUser", decodedCookieValue);

			//LOG.error("LoginHiHeaderController: Last user set into model: " + model.asMap().get("lastLoggedInUser"));
		}
		/** End UF-93 **/
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
