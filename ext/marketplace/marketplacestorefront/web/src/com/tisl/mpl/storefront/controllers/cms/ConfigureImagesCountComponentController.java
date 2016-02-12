/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.ConfigureImagesCountComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 *
 * @author TCS
 *
 */
/**
 */
@Controller("ConfigureImagesCountComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.ConfigureImagesCountComponent)
public class ConfigureImagesCountComponentController extends AbstractCMSComponentController<ConfigureImagesCountComponentModel>
{
	/**
	 * attribute added for configuring number of images to be displayed in pdp
	 */

	/**
	 * @description method is for configuring number of images count to be displayed in pdp
	 * @param request
	 * @param model
	 * @param component
	 *
	 **/

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final ConfigureImagesCountComponentModel component)
	{
		model.addAttribute(ControllerConstants.Actions.Cms.IMAGE_COUNT, Integer.valueOf(component.getCount()));
	}

}
