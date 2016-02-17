/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;


import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.model.cms.components.HelpMeShopSearchComponentModel;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("HelpMeShopSearchComponentController")
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = ControllerConstants.Actions.Cms.HelpMeShopSearchComponent)
public class HelpMeShopSearchComponentController extends AbstractCMSComponentController<HelpMeShopSearchComponentModel>
{

	/**
	 * @Description: It is called automatically when HelpMeShopSearchComponent is used
	 * @param request
	 * @param model
	 * @param component
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final HelpMeShopSearchComponentModel component)
	{
		//do nothing
	}
}
