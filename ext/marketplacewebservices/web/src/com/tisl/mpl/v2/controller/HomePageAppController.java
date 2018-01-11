/**
 *
 */
package com.tisl.mpl.v2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/cms/{userId}/page")
public class HomePageAppController
{

	@RequestMapping(value = "/homepage", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void getHomePageComponents(@RequestParam final String mcvid, @RequestParam final String lat,
			@RequestParam final String lng, @RequestParam final String pincode, @RequestParam final String channel,
			@RequestParam final String isPwa)
	{
		System.out.println("API LAyer will handle this");

	}

	@RequestMapping(value = "/themeOffer", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public void getThemeOffersComponents(@RequestParam final String mcvid, @RequestParam final String lat,
			@RequestParam final String lng, @RequestParam final String pincode, @RequestParam final String channel,
			@RequestParam final String isPwa, @RequestBody final )
	{


	}

}
