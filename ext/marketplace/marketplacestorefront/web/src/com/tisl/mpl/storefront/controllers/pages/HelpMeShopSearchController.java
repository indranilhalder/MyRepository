/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tisl.mpl.facade.helpmeshop.impl.DefaultHelpMeShopFacadeImpl;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 *
 */
@Controller
@Scope("tenant")
@RequestMapping(value = RequestMappingUrlConstants.HELP_ME_SHOP_SEARCH)
public class HelpMeShopSearchController
{
	@Resource(name = "helpMeShopFacade")
	private DefaultHelpMeShopFacadeImpl helpMeShopFacade;

	/**
	 * @Description: It is called automatically when HelpMeShopSearchComponent is used
	 *
	 * @param request
	 * @param model
	 *
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String get(final Model model, final HttpServletRequest request)
	{
		model.addAttribute("genderOrTitleList", helpMeShopFacade.getGenderOrTitle());
		model.addAttribute("reasonOrEventList", helpMeShopFacade.getReasonOrEvent());
		model.addAttribute("typeOfProductList", helpMeShopFacade.getTypeOfProduct());
		model.addAttribute("startAgeLimit", Integer.valueOf("1"));
		model.addAttribute("endAgeLimit", Integer.valueOf("100"));

		return ControllerConstants.Views.Fragments.Home.HelpMeShop;
	}

}
