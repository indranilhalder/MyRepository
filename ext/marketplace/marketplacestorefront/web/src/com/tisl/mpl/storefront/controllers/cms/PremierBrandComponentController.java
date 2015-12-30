/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.PremierBrandComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */

@Controller("PremierBrandComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.PremierBrandComponent)
public class PremierBrandComponentController extends AbstractCMSComponentController<PremierBrandComponentModel>
{



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
	 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final PremierBrandComponentModel component)
	{

		//to send the list of brands for premier brands through model

		if (component.getBrandList() != null)
		{


			model.addAttribute("brandList", component.getBrandList());
			model.addAttribute("imageSize", component.getImageSize());

		}


	}

}
