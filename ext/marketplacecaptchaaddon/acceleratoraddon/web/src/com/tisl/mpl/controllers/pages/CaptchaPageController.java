/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.constants.MplCaptchaaddonConstants;
import com.tisl.mpl.security.captcha.MplRecaptchaImpl;
import com.tisl.mpl.security.captcha.MplRecaptchaResponse;



/**
 * This controller responsible to render captcha widget
 */
@Controller
@RequestMapping(value = MplCaptchaaddonConstants.LINK_LOGIN_CAPTCHA)
public class CaptchaPageController
{
	@Autowired
	private SessionService sessionService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private SiteConfigService siteConfigService;

	private static final String RECAPTCHA_PRIVATE_KEY_PROPERTY = "recaptcha.privatekey";
	private static final String RECAPTCHA_PUBLIC_KEY_PROPERTY = "recaptcha.publickey";


	/**
	 * @description this method is called to populate the captcha page
	 * @param widgetName
	 * @param model
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = MplCaptchaaddonConstants.LINK_WIDGETNAME, method = RequestMethod.GET)
	public String getWidget(@PathVariable("widgetName") final String widgetName, final Model model,
			final HttpServletRequest request)
	{
		return MplCaptchaaddonConstants.LINK_ADDON_MARKETPLACECAPTCHAADDON_PAGES + widgetName;
	}

	/**
	 * @description this method accepts user input to save and validate the captcha
	 * @param responseFieldValue
	 * @param challengeFieldValue
	 * @param request
	 * @return String
	 */
	@RequestMapping(value = MplCaptchaaddonConstants.LINK_SAVEDATA + MplCaptchaaddonConstants.LINK_CHALLENGE
			+ MplCaptchaaddonConstants.LINK_CHALLENGEFIELDVALUE, method = RequestMethod.GET)
	public @ResponseBody String saveData(
			@PathVariable(MplCaptchaaddonConstants.RESPONSE_FIELD_VALUE) final String responseFieldValue,
			@PathVariable(MplCaptchaaddonConstants.CHALLENGE_FIELD_VALUE) final String challengeFieldValue,
			final HttpServletRequest request)
	{
		//	check for enability of captcha for current store
		final boolean captcaEnabledForCurrentStore = isCaptcaEnabledForCurrentStore();
		if (captcaEnabledForCurrentStore)
		{
			request.setAttribute(MplCaptchaaddonConstants.CAPTCA_ENABLED_FOR_CURRENT_STORE,
					Boolean.valueOf(captcaEnabledForCurrentStore));
			request.setAttribute(MplCaptchaaddonConstants.RECAPTCHA_PUBLIC_KEY,
					siteConfigService.getProperty(RECAPTCHA_PUBLIC_KEY_PROPERTY));
			if ((StringUtils.isBlank(challengeFieldValue) || StringUtils.isBlank(responseFieldValue))
					|| !checkAnswer(request, challengeFieldValue, responseFieldValue))
			{
				sessionService.setAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED, Boolean.FALSE);
			}
			else
			{
				sessionService.setAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED, Boolean.TRUE);
			}
		}
		return resultOfCatpcha();

	}

	/**
	 * @description this method is called to return success or failure after validating captcha
	 * @return String
	 */
	public String resultOfCatpcha()
	{
		if (sessionService.getAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED) != null)
		{
			final Boolean boolean1 = sessionService.getAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED);
			if (boolean1.booleanValue())
			{
				sessionService.removeAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED);
				return MplCaptchaaddonConstants.SUCCESS;
			}
			else
			{
				sessionService.removeAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED);
				return MplCaptchaaddonConstants.FAIL;
			}
		}
		return MplCaptchaaddonConstants.FAIL;
	}

	/**
	 * @description this method is called internally to check enability of captcha for the current store
	 * @return boolean
	 */
	private boolean isCaptcaEnabledForCurrentStore()
	{
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		return currentBaseStore != null && Boolean.TRUE.equals(currentBaseStore.getCaptchaCheckEnabled());
	}

	/**
	 * @description this method is called internally to validate the entered captcha
	 * @param request
	 * @param challengeFieldValue
	 * @param responseFieldValue
	 * @return boolean
	 */
	private boolean checkAnswer(final HttpServletRequest request, final String challengeFieldValue, final String responseFieldValue)
	{
		// validate the entered captcha
		final MplRecaptchaImpl reCaptcha = new MplRecaptchaImpl();
		reCaptcha.setPrivateKey(siteConfigService.getProperty(RECAPTCHA_PRIVATE_KEY_PROPERTY));
		final MplRecaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(), challengeFieldValue,
				responseFieldValue);

		return reCaptchaResponse.isValid();
	}


}
