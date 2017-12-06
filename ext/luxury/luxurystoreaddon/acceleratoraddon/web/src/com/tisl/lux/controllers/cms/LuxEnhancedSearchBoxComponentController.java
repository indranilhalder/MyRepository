/**
 *
 */
package com.tisl.lux.controllers.cms;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.lux.controllers.LuxurystoreaddonControllerConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.LuxEnhancedSearchBoxComponentModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController;


/**
 * @author BORN
 * 
 */
@Controller("LuxEnhancedSearchBoxComponentController")
@RequestMapping(value = LuxurystoreaddonControllerConstants.Actions.Cms.LuxEnhancedSearchBoxComponent)
public abstract class LuxEnhancedSearchBoxComponentController extends
		AbstractCMSComponentController<LuxEnhancedSearchBoxComponentModel>
{
	private static final Logger LOG = Logger.getLogger(LuxEnhancedSearchBoxComponentController.class);


	/*
	 * Method to load the component with configurations
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final LuxEnhancedSearchBoxComponentModel component) throws EtailNonBusinessExceptions
	{

		//load the category list box
		try
		{
			LOG.debug("Load all the Categories Associated");
			LOG.warn("Load all the Categories Associated");
			model.addAttribute("categoryList", component.getSearchBoxCategories());
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


}
