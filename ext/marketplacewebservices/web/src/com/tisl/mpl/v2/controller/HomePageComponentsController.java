/**
 *
 */
package com.tisl.mpl.v2.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.facade.homeapi.HomePageAppFacade;
import com.tisl.mpl.wsdto.ThemeOffersDTO;
import com.tisl.mpl.wsdto.ThemeOffersRequestDTO;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/cms/{userId}/page", headers = "Accept=application/xml,application/json")
public class HomePageComponentsController
{
	private static final Logger LOG = Logger.getLogger(HomePageComponentsController.class);
	@Resource(name = "homePageAppFacade")
	private HomePageAppFacade homePageAppFacade;

	@RequestMapping(value = "/themeOfferComponent", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ThemeOffersDTO getThemeOffersComponent(@RequestParam final String mcvid, @RequestParam final String lat,
			@RequestParam final String lng, @RequestParam final String pincode, @RequestParam final String channel,
			@RequestParam final String isPwa, @RequestBody final ThemeOffersRequestDTO themeOffersRequestDTO)
	{

		ThemeOffersDTO themeOffersDTO = new ThemeOffersDTO();

		final String themeOfferJsonString = themeOffersRequestDTO.getContent();
		themeOffersDTO = homePageAppFacade.getThemeOffersComponentDTO(themeOffersRequestDTO, themeOfferJsonString);
		themeOffersDTO.setError("error");
		themeOffersDTO.setErrorCode("6002");
		themeOffersDTO.setErrorMessage("status message");
		System.out.println("Theme offers dto" + themeOffersDTO);

		return themeOffersDTO;
	}
}
