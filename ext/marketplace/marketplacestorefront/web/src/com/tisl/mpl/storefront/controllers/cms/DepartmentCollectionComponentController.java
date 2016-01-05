/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.DepartmentCollectionComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */

@Controller("DepartmentCollectionComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.DepartmentCollectionComponent)
public class DepartmentCollectionComponentController extends AbstractCMSComponentController<DepartmentCollectionComponentModel>
{

	/**
	 * This method fetches all the departments in the component and finds out the second level and third level
	 * hierarchies
	 *
	 * @param request
	 * @param model
	 * @param component
	 *
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final DepartmentCollectionComponentModel component)
	{
		//do nothing
	}

}
