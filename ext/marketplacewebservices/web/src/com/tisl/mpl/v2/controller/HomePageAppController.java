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

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.ComponentRequestDTO;
import com.tisl.mpl.wsdto.ThemeOffersDTO;


/**
 * @author TCS
 *
 */

@Controller
@RequestMapping(value = "/{baseSiteId}/cms/{userId}/page", headers = "Accept=application/xml,application/json")
public class HomePageAppController
{

	@RequestMapping(value = "/themeOffer", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ThemeOffersDTO getThemeOffersComponents(@RequestParam final String mcvid, @RequestParam final String lat,
			@RequestParam final String lng, @RequestParam final String pincode, @RequestParam final String channel,
			@RequestParam final String isPwa, @RequestBody final ComponentRequestDTO componentRequestDTO)
	{
		final ThemeOffersDTO themeOffersDTO = new ThemeOffersDTO();
		try
		{
			//
			themeOffersDTO.setError("error");
			themeOffersDTO.setErrorCode("6002");
			themeOffersDTO.setErrorMessage("status message");
			System.out.println("Theme offers dto" + componentRequestDTO);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				themeOffersDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				themeOffersDTO.setErrorCode(e.getErrorCode());
			}
			themeOffersDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				themeOffersDTO.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				themeOffersDTO.setErrorCode(e.getErrorCode());
			}
			themeOffersDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return themeOffersDTO;

	}
}
