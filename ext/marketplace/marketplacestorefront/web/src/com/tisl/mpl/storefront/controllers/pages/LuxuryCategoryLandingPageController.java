/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;




/**
 * @author cruz
 *
 */

/**
 * @description this is called to load home page
 * @param logout
 * @param model
 * @param redirectModel
 * @return getViewForPage
 * @throws CMSItemNotFoundException
 */

@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = RequestMappingUrlConstants.LUXURYCATEGORYLANDINGPAGE)
public class LuxuryCategoryLandingPageController extends AbstractPageController
{


	/* @RequestMapping(method = RequestMethod.GET, params = "!q") */
	@RequestMapping(method = RequestMethod.GET)
	public String home(
			@RequestParam(value = ModelAttributetConstants.LOGOUT, defaultValue = ModelAttributetConstants.FALSE) final boolean logout,
			final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (logout)
		{
			//GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER, "account.confirmation.signout.title");
			return REDIRECT_PREFIX + ROOT;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId("luxuryCategoryLandingPage"));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId("luxuryCategoryLandingPage"));
		updatePageTitle(model, getContentPageForLabelOrId("luxuryCategoryLandingPage"));

		return getViewForPage(model);
	}


	/**
	 * @description this is called to load home page
	 * @param logout
	 * @param model
	 * @param redirectModel
	 * @return getViewForPage
	 * @throws CMSItemNotFoundException
	 */
	/*
	 * @RequestMapping(method = RequestMethod.GET) public String home(
	 *
	 * @RequestParam(value = ModelAttributetConstants.LOGOUT, defaultValue = ModelAttributetConstants.FALSE) final
	 * boolean logout, final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException { if
	 * (logout) { //GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.INFO_MESSAGES_HOLDER,
	 * "account.confirmation.signout.title"); return REDIRECT_PREFIX + ROOT; }
	 *
	 * storeCmsPageInModel(model, getContentPageForLabelOrId("luxuryCategoryLandingPage"));
	 * setUpMetaDataForContentPage(model, getContentPageForLabelOrId("luxuryCategoryLandingPage"));
	 * updatePageTitle(model, getContentPageForLabelOrId("luxuryCategoryLandingPage"));
	 *
	 * return getViewForPage(model); }
	 */

	protected void updatePageTitle(final Model model, final AbstractPageModel cmsPage)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveHomePageTitle(cmsPage.getTitle()));
	}
}