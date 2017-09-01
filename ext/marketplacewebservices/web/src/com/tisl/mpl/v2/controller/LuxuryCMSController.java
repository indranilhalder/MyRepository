/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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


	private static final String DEFAULT = "DEFAULT";


	@RequestMapping(value = "/cms/{pageLabel}", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public LuxuryComponentsListWsDTO getContentPages(@PathVariable final String pageLabel)
	{
		return luxCmsFacade.getContentPagesBylableOrId(pageLabel);
	}


	@RequestMapping(value = "/cms/luxuryhomepage", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public LuxuryComponentsListWsDTO getluxuryHomepage()
	{
		return luxCmsFacade.getLuxHomepage();
	}


	@RequestMapping(value = "/cms/luxuryBLP", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public LuxuryComponentsListWsDTO getBrandpage(@RequestParam(required = false, defaultValue = DEFAULT) final String brandCode)

	{
		return luxCmsFacade.getBrandById(brandCode);
	}
}
