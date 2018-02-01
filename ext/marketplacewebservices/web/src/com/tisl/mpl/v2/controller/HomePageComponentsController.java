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
import com.tisl.mpl.wsdto.HomepageComponentRequestDTO;
import com.tisl.mpl.wsdto.HomepageComponetsDTO;


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


	@RequestMapping(value = "/getHomepageComponetsData", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public HomepageComponetsDTO getHomepageComponetsData(@RequestParam(required = false) final String mcvid,
			@RequestParam(required = false) final String lat, @RequestParam(required = false) final String lng,
			@RequestParam(required = false) final String pincode, @RequestParam(required = false) final String channel,
			@RequestParam(required = false) final String isPwa,
			@RequestBody final HomepageComponentRequestDTO homepageComponentRequestDTO)
	{

		HomepageComponetsDTO homepageComponetsDTO = new HomepageComponetsDTO();
		try
		{
			homepageComponetsDTO = homePageAppFacade.gethomepageComponentsDTO(homepageComponentRequestDTO);
		}
		//System.out.println("Theme offers dto" + homepageComponetsDTO);

		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Error in home page components controller" + e.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				homepageComponetsDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				homepageComponetsDTO.setErrorCode(e.getErrorCode());
			}
			homepageComponetsDTO.setStatus(MarketplacewebservicesConstants.FAILURE);
		}
		catch (final Exception e)
		{
			LOG.error("Error in home page components controller" + e.getMessage());
			ExceptionUtil.getCustomizedExceptionTrace(e);
			homepageComponetsDTO.setError(Localization.getLocalizedString(MarketplacewebservicesConstants.H9002));
			homepageComponetsDTO.setErrorCode(MarketplacewebservicesConstants.H9002);
			homepageComponetsDTO.setStatus(MarketplacewebservicesConstants.FAILURE);
		}

		return homepageComponetsDTO;
	}
}
