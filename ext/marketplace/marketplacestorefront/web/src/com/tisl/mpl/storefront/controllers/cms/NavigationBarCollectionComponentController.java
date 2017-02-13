/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;


import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.services.impl.DefaultCMSContentSlotService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */

/**
 * @description It is used for fetching contact Number from Model
 * @param NeedHelpComponentModel
 */


@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.SHOP_BY_DEPARTMENT_AJAX)
public class NavigationBarCollectionComponentController extends AbstractPageController

{

	@Autowired
	private DefaultCMSContentSlotService contentSlotService;


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
	 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getshopByDepartment(final Model model)
	{

		try
		{
			//final Map<String, String> nodeUrlTitle = new LinkedHashMap<String, String>();

			//final ArrayList<CategoryModel> departments = new ArrayList<CategoryModel>();

			final ContentSlotModel contentSlotModel = contentSlotService.getContentSlotForId("NavigationBarSlot");
			final List<AbstractCMSComponentModel> componentLists = contentSlotModel.getCmsComponents();
			for (final AbstractCMSComponentModel cmsmodel : componentLists)
			{
				if (cmsmodel instanceof NavigationBarCollectionComponentModel)
				{
					final NavigationBarCollectionComponentModel deptModel = (NavigationBarCollectionComponentModel) cmsmodel;

					model.addAttribute("component", deptModel);

				}

			}


		}
		catch (final EtailBusinessExceptions businessException)
		{
			ExceptionUtil.etailBusinessExceptionHandler(businessException, null);
		}
		catch (final EtailNonBusinessExceptions nonBusinessException)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(nonBusinessException);
		}
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
		}
		return "/cms/" + ControllerConstants.Views.Cms.ShopByDepartment;
	}
}
