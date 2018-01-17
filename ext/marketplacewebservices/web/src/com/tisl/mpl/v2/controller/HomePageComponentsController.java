/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.util.localization.Localization;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.homeapi.HomePageAppFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.AutomatedBrandProductCarouselDTO;
import com.tisl.mpl.wsdto.BannersCarouselDTO;
import com.tisl.mpl.wsdto.HomepageComponentRequestDTO;
import com.tisl.mpl.wsdto.ThemeOffersDTO;
import com.tisl.mpl.wsdto.VideoProductCaraouselDTO;


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
			@RequestParam final String isPwa, @RequestBody final HomepageComponentRequestDTO themeOffersRequestDTO)
	{

		ThemeOffersDTO themeOffersDTO = new ThemeOffersDTO();

		try
		{
			themeOffersDTO = homePageAppFacade.getThemeOffersComponentDTO(themeOffersRequestDTO);
			//here we have to decide whether we are going to use  datamapper  or not

		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Error in home page components controller");
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				themeOffersDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				themeOffersDTO.setErrorCode(e.getErrorCode());
			}
			themeOffersDTO.setStatus(MarketplacewebservicesConstants.FAILURE);
		}
		catch (final Exception e)
		{
			LOG.error("Error in home page components controller");
			ExceptionUtil.getCustomizedExceptionTrace(e);
			themeOffersDTO.setError(Localization.getLocalizedString(MarketplacewebservicesConstants.H9002));
			themeOffersDTO.setErrorCode(MarketplacewebservicesConstants.H9002);
			themeOffersDTO.setStatus(MarketplacewebservicesConstants.FAILURE);
		}
		return themeOffersDTO;
	}

	//bannercomponent
	@RequestMapping(value = "/bannerProductCarouselComponent", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public BannersCarouselDTO getbannerProductCarouselComponent(@RequestParam final String mcvid, @RequestParam final String lat,
			@RequestParam final String lng, @RequestParam final String pincode, @RequestParam final String channel,
			@RequestParam final String isPwa, @RequestBody final HomepageComponentRequestDTO bannerCarouselRequestDTO)
	{

		BannersCarouselDTO bannerProductCarouselDTO = new BannersCarouselDTO();

		bannerProductCarouselDTO = homePageAppFacade.getBannerProductCarouselDTO(bannerCarouselRequestDTO);
		System.out.println("Theme offers dto" + bannerProductCarouselDTO);

		return bannerProductCarouselDTO;
	}


	//videoProductCarousel
	@RequestMapping(value = "/videoProductCarouselComponent", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public VideoProductCaraouselDTO getvideoProductCarouselComponent(@RequestParam final String mcvid,
			@RequestParam final String lat, @RequestParam final String lng, @RequestParam final String pincode,
			@RequestParam final String channel, @RequestParam final String isPwa,
			@RequestBody final HomepageComponentRequestDTO videoProductCaraouselRequestDTO)
	{

		VideoProductCaraouselDTO videoProductCaraouselDTO = new VideoProductCaraouselDTO();

		videoProductCaraouselDTO = homePageAppFacade.getVideoProductCarouselDTO(videoProductCaraouselRequestDTO);
		System.out.println("Theme offers dto" + videoProductCaraouselDTO);

		return videoProductCaraouselDTO;
	}


	//automatedBrandproductCarousel

	@RequestMapping(value = "/automatedBrandCarouselComponent", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public AutomatedBrandProductCarouselDTO getautomatedBrandCarouselComponent(@RequestParam final String mcvid,
			@RequestParam final String lat, @RequestParam final String lng, @RequestParam final String pincode,
			@RequestParam final String channel, @RequestParam final String isPwa,
			@RequestBody final HomepageComponentRequestDTO automatedBrandRequestDTO)
	{

		AutomatedBrandProductCarouselDTO automatedBrandCaraouselDTO = new AutomatedBrandProductCarouselDTO();

		automatedBrandCaraouselDTO = homePageAppFacade.getautomatedBrandCarouselDTO(automatedBrandRequestDTO);
		System.out.println("Theme offers dto" + automatedBrandCaraouselDTO);

		return automatedBrandCaraouselDTO;
	}

}
