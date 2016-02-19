/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("BrandComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.BrandComponent)
public class BrandComponentController extends AbstractCMSComponentController<BrandComponentModel>
{






	/**
	 * This method fetches all the sub brands and iterates through them and groups them alphabetically if the layout is A
	 * to Z
	 *
	 * @param request
	 * @param model
	 * @param component
	 * @return
	 */
	@SuppressWarnings("boxing")
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final BrandComponentModel component)
	{
		//do nothing

	}






}
