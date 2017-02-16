/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;


import de.hybris.platform.commercefacades.component.data.BrandComponentData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.BrandCollectionComponentModel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("BrandCollectionComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.BrandCollectionComponent)
public class BrandCollectionComponentController extends AbstractCMSComponentController<BrandCollectionComponentModel>
{


	@Resource(name = "mplBrandCollectionConvertor")
	private Converter<BrandComponentModel, BrandComponentData> brandCollectionConverter;


	/**
	 * This method fetches all the brand components associated with the brand collection component
	 *
	 * @param request
	 * @param model
	 * @param component
	 * @return
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final BrandCollectionComponentModel component)
	{
		// Finding all the Brand Collections associated with this component
		if (component.getBrandCollection() != null)
		{

			model.addAttribute(ModelAttributetConstants.BRAND_COMPONENT_COLLECTION,
					Converters.convertAll(component.getBrandCollection(), brandCollectionConverter));
		}
	}
}
