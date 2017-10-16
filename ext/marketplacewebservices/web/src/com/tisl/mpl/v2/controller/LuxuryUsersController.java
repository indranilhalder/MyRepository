/**
 *
 */
package com.tisl.mpl.v2.controller;

import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.model.user.CustomerModel;

import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.reviews.GigyaFacade;
import com.tisl.mpl.service.MplMobileUserService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.webservice.businessvalidator.DefaultCommonAsciiValidator;
import com.tisl.mpl.wsdto.GigyaWsDTO;
import com.tisl.mpl.wsdto.MplUserResultWsDto;


@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
@Controller
@RequestMapping(value = "/lux/users", headers = "Accept=application/xml,application/json")
@CacheControl(directive = CacheControlDirective.PRIVATE)
public class LuxuryUsersController extends BaseCommerceController
{
	@Autowired
	private GigyaFacade gigyaFacade;

	@Resource
	private MplMobileUserService mobileUserService;

	@Autowired
	private MplPaymentWebFacade mplPaymentWebFacade;

	private static final Logger LOG = Logger.getLogger(LuxuryUsersController.class);

	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String ROLE_CLIENT = "ROLE_CLIENT";
	private static final String APPLICATION_TYPE = "application/json";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";

	//Mobile registration
	@Secured(
	{ ROLE_CLIENT, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/registration", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public MplUserResultWsDto registerUser(@RequestParam final String emailId, @RequestParam final String password,
			@RequestParam final String mobileNumber, @RequestParam final String firstName, @RequestParam final String lastName,
			@RequestParam final String gender, @RequestParam(required = false) final boolean tataTreatsEnable)
					throws RequestParameterException, WebserviceValidationException, MalformedURLException

	{
		LOG.debug("****************** User Registration mobile web service ***********" + emailId);
		MplUserResultWsDto userResult = new MplUserResultWsDto();
		GigyaWsDTO gigyaWsDto = new GigyaWsDTO();
		final boolean isNewusers = true;
		try
		{
			/* TPR-1140 Case-sensitive nature resulting in duplicate customer e-mails IDs */
			final String emailIdLwCase = emailId.toLowerCase();
			userResult = mobileUserService.registerNewLuxUser(emailIdLwCase, password, getMobileNumber(mobileNumber),
					getFirstName(firstName), getLastName(lastName), getGender(gender), tataTreatsEnable);
			final CustomerModel customerModel = mplPaymentWebFacade.getCustomer(emailIdLwCase);
			gigyaWsDto = gigyaFacade.gigyaLoginHelper(customerModel, isNewusers);
			if (StringUtils.isNotEmpty(gigyaWsDto.getSessionSecret()))
			{
				userResult.setSessionSecret(gigyaWsDto.getSessionSecret());
			}
			if (StringUtils.isNotEmpty(gigyaWsDto.getSessionToken()))
			{
				userResult.setSessionToken(gigyaWsDto.getSessionToken());
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				userResult.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResult.setErrorCode(e.getErrorCode());
			}
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				userResult.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				userResult.setErrorCode(e.getErrorCode());
			}
			userResult.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return userResult;
	}

	private String getFirstName(final String firstName)
	{
		if (StringUtils.isNotEmpty(firstName) && DefaultCommonAsciiValidator.validateAlphaWithSpaceNoSpCh(firstName)
				&& StringUtils.length(firstName) <= MarketplacecommerceservicesConstants.NAME)
		{
			return firstName;
		}
		return null;
	}

	private String getLastName(final String lastName)
	{
		if (StringUtils.isNotEmpty(lastName) && DefaultCommonAsciiValidator.validateAlphaWithSpaceNoSpCh(lastName)
				&& StringUtils.length(lastName) <= MarketplacecommerceservicesConstants.NAME)
		{
			return lastName;
		}

		return null;
	}

	private String getMobileNumber(final String mobileNumber)
	{
		if (StringUtils.isNotEmpty(mobileNumber))
		{
			if (StringUtils.length(mobileNumber) == MarketplacecommerceservicesConstants.MOBLENGTH
					&& mobileNumber.matches(MarketplacecommerceservicesConstants.MOBILE_REGEX))
			{
				return mobileNumber;
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9023);
			}
		}

		return null;
	}

	private String getGender(final String gender)
	{
		if (MarketplacecommerceservicesConstants.MALE_CAPS.equalsIgnoreCase(gender))
		{
			return MarketplacecommerceservicesConstants.MALE_CAPS;
		}
		if (MarketplacecommerceservicesConstants.FEMALE_CAPS.equalsIgnoreCase(gender))
		{
			return MarketplacecommerceservicesConstants.FEMALE_CAPS;
		}
		return null;
	}
}
