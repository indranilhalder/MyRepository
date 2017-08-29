/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.mapping.FieldSetBuilder;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
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
@RequestMapping(value = "/lux/cms")
public class LuxuryCMSController extends BaseController
{

	@Resource(name = "luxCmsFacade")
	private LuxCmsFacade luxCmsFacade;

	@Resource(name = "fieldSetBuilder")
	private FieldSetBuilder fieldSetBuilder;

	private static final String DEFAULT = "DEFAULT";

	private static final String CHANNEL = "mobile";

	private static final Logger LOG = Logger.getLogger(LuxuryCMSController.class);



	@RequestMapping(value = "/homepage1", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public LuxuryComponentsListWsDTO getHomepage(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		try
		{
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO = luxCmsFacade.getLuxuryHomePage();
			return luxuryComponentsListWsDTO;
		}
		catch (final CMSItemNotFoundException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/luxuryWomenlandingPage", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public LuxuryComponentsListWsDTO getWomenlandingPage(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		try
		{
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO = luxCmsFacade.getWomenlandingPage();
			return luxuryComponentsListWsDTO;
		}
		catch (final CMSItemNotFoundException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/luxuryBrandLandingPage", method = RequestMethod.GET)
	@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 300)
	@ResponseBody
	public LuxuryComponentsListWsDTO getBrandLandingPage(@RequestParam(defaultValue = DEFAULT) final String fields)
	{
		try
		{
			final LuxuryComponentsListWsDTO luxuryComponentsListWsDTO = luxCmsFacade.getBrandLandingPage();
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