/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;

import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.MplMobileAppRegisterFacade;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.MplUserResultWsDto;


/**
 * @author TCS
 *
 */

@Controller
@RequestMapping(value = "/{baseSiteId}/users/{emailId}/mobileApp")
public class MobileAppRegistrationController
{

	@Autowired
	private MplMobileAppRegisterFacade mplMobileAppRegisterFacade;
	private static final String Y = "Y";
	private static final String N = "N";

	private static final Logger LOG = Logger.getLogger(MobileAppRegistrationController.class);


	/**
	 * save device info
	 *
	 * @param emailId
	 * @param platform
	 * @param deviceKey
	 * @param isActive
	 * @return MplUserResultWsDto
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = "/saveDeviseInfo", method = RequestMethod.POST)
	@ResponseBody
	public MplUserResultWsDto mobileAppRegister(@PathVariable final String emailId, @RequestParam final String platform,
			@RequestParam final String deviceKey, @RequestParam final String isActive) throws RequestParameterException,
			WebserviceValidationException, MalformedURLException
	{
		MplUserResultWsDto result = new MplUserResultWsDto();
		LOG.debug("************** Mobile web service save device info *****************");
		try
		{
			if (!(StringUtils.equalsIgnoreCase(platform.toLowerCase(), MarketplacewebservicesConstants.PLATFORM_GCM)
					|| (StringUtils.equalsIgnoreCase(platform.toLowerCase(), MarketplacewebservicesConstants.PLATFORM_APNS)) || (StringUtils
						.equalsIgnoreCase(platform.toLowerCase(), MarketplacewebservicesConstants.PLATFORM_APNS_SANDBOX))))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9062);
			}

			else if (!(StringUtils.equalsIgnoreCase(isActive.toLowerCase(), Y) || StringUtils.equalsIgnoreCase(
					isActive.toLowerCase(), N)))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9063);
			}
			result = mplMobileAppRegisterFacade.mobileKeyRegistration(emailId.toLowerCase(), platform, deviceKey, isActive);
			LOG.debug("************** Mobile web service save device info SUCCESS*****************");
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}





}
