/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.model.cms.components.TrackOrderHeaderComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("TrackOrderHeaderComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.TrackOrderHeaderComponent)
public class TrackOrderHeaderComponentController extends AbstractCMSComponentController<TrackOrderHeaderComponentModel>
{



	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final TrackOrderHeaderComponentModel component)
	{
		//do nothing
	}

}
