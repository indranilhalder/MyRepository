/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.core.model.MplBrandLogoComponentModel;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@Controller("MplBrandLogoComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.MplBrandLogoComponent)
public class MplBrandLogoComponentController extends AbstractCMSComponentController<MplBrandLogoComponentModel>
{



	/**
	 * This method displays a list of brand image ,brand logo and brand descriptio
	 *
	 *
	 *
	 * @param model
	 * @param request
	 * @param MplBrandLogoComponentModel
	 *
	 *
	 */




	@SuppressWarnings("javadoc")
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final MplBrandLogoComponentModel component)
	{

		final List<CategoryModel> categories = component.getCategories();
		String logoUrl = null;
		String logoQualifier = null;
		String mediumImageUrl = null;
		String description = null;

		if (null != categories)
		{
			for (final CategoryModel categoryModel : categories)
			{
				if (null != categoryModel.getMedias() && categoryModel.getMedias().size() > 0)
				{
					for (final MediaModel mediaModel : categoryModel.getMedias())
					{
						logoQualifier = mediaModel.getMediaFormat().getQualifier();
						//
						//						if (logoQualifier.equals("135Wx60H")) Position literals first in String comparisons
						//						{
						//							logoUrl = mediaModel.getURL2();
						//						}
						//						else if (logoQualifier.equals("206Wx206H")) Position literals first in String comparisons
						//						{
						//							mediumImageUrl = mediaModel.getURL2();
						//						}

						if (("135Wx60H").equals(logoQualifier))
						{
							logoUrl = mediaModel.getURL2();
						}
						else if (("206Wx206H").equals(logoQualifier))
						{
							mediumImageUrl = mediaModel.getURL2();
						}

						description = mediaModel.getDescription();


					}


				}
			}



		}

		model.addAttribute("mediumImageUrl", mediumImageUrl);

		model.addAttribute("logoQualifier", logoQualifier);
		model.addAttribute("logoUrl", logoUrl);
		model.addAttribute("description", description);


	}

}
