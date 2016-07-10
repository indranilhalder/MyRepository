/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.model.cms.components.NeedHelpComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */

/**
 * @description It is used for fetching contact Number from Model
 * @param NeedHelpComponentModel
 */


@SuppressWarnings("javadoc")
@Controller("NeedHelpComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.NeedHelpComponent)
public class NeedHelpComponentController extends AbstractCMSComponentController<NeedHelpComponentModel>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
	 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final NeedHelpComponentModel component)
	{
		model.addAttribute("contactNumber", component.getContactNumber());

	}

}