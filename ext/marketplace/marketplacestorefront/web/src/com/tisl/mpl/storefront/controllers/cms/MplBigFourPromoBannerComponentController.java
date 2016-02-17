/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("MplBigFourPromoBannerComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplBigFourPromoBannerComponent)
public class MplBigFourPromoBannerComponentController extends AbstractCMSComponentController<MplBigFourPromoBannerComponentModel>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
	 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final MplBigFourPromoBannerComponentModel component)
	{
		/*
		 * component moved to model
		 */

	}

}
