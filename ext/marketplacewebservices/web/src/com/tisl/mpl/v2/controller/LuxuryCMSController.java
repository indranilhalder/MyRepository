/**
 *
 */
package com.tisl.mpl.v2.controller;

import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCMSPageServiceImpl;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.tisl.mpl.facade.cms.LuxCmsFacade;
import com.tisl.mpl.wsdto.LuxuryComponentsListWsDTO;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/lux")
public class LuxuryCMSController extends BaseController
{

	@Resource(name = "luxCmsFacade")
	private LuxCmsFacade luxCmsFacade;

	@Resource(name = "mplCMSPageService")
	private MplCMSPageServiceImpl mplCMSPageService;

	@Resource(name = "fieldSetBuilder")
	private FieldSetBuilder fieldSetBuilder;

	@Resource(name = "categoryService")
	private DefaultCategoryService categoryService;

	private static final String DEFAULT = "DEFAULT";

	private static final String CHANNEL = "mobile";

	private static final Logger LOG = Logger.getLogger(LuxuryCMSController.class);



	@RequestMapping(value = "/cms/{pageLabel}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public LuxuryComponentsListWsDTO getHomepage(
			@PathVariable final String pageLabel,
			@RequestParam(required = false, defaultValue = DEFAULT) final String brandCode)
	{
		try
		{

			 ContentPageModel contentPage = new ContentPageModel();
			LuxuryComponentsListWsDTO luxuryComponentsListWsDTO = new LuxuryComponentsListWsDTO();
			if(brandCode.equalsIgnoreCase(DEFAULT)){
				contentPage =  mplCMSPageService.getPageByLabelOrId(pageLabel);
				luxuryComponentsListWsDTO = luxCmsFacade.getLuxuryPage(contentPage);
			}else{
				final CategoryModel category = categoryService.getCategoryForCode(brandCode);
				contentPage = mplCMSPageService.getLandingPageForCategory(category);
				luxuryComponentsListWsDTO = luxCmsFacade.getLuxuryPage(contentPage);

			}
			return luxuryComponentsListWsDTO;
		}
		catch (final CMSItemNotFoundException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}