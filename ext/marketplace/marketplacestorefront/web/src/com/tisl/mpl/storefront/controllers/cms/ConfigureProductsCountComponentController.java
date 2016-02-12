/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.ConfigureProductsCountComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 *
 * @author TCS
 *
 */
/**
 */
@Controller("ConfigureProductsCountComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.ConfigureProductsCountComponent)
public class ConfigureProductsCountComponentController extends
		AbstractCMSComponentController<ConfigureProductsCountComponentModel>
{
	/**
	 * attribute added for configuring number of products to be added to the bag in pdp
	 */

	/**
	 * @description method is for configuring number of products count to be added to the bag in pdp
	 * @param request
	 * @param model
	 * @param component
	 *
	 **/

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final ConfigureProductsCountComponentModel component)
	{
		model.addAttribute(ControllerConstants.Actions.Cms.PRODUCT_COUNT, Integer.valueOf(component.getCount()));
	}

}
