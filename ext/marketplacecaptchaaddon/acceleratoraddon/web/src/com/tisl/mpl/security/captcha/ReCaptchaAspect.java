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
package com.tisl.mpl.security.captcha;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindingResult;

import com.tisl.mpl.constants.MplCaptchaaddonConstants;


/**
 * An aspect which uses google ReCaptcha api to validate captcha answer on the storefront Registration form.
 */

public class ReCaptchaAspect
{
	@Autowired
	private SessionService sessionService;
	private static final String RECAPTCHA_PRIVATE_KEY_PROPERTY = "recaptcha.privatekey";
	private static final String RECAPTCHA_PUBLIC_KEY_PROPERTY = "recaptcha.publickey";
	private SiteConfigService siteConfigService;
	private BaseStoreService baseStoreService;

	/**
	 * @description this method is called from CaptchaPageController to prepare the images and audio for catcha
	 * @param joinPoint
	 * @return Object
	 * @throws Throwable
	 */
	public Object prepare(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		final List<Object> args = Arrays.asList(joinPoint.getArgs());
		final HttpServletRequest request = (HttpServletRequest) CollectionUtils.find(args,
				PredicateUtils.instanceofPredicate(HttpServletRequest.class));
		if (request != null)
		{
			//			check for enability of captcha for current store
			final boolean captcaEnabledForCurrentStore = isCaptcaEnabledForCurrentStore();

			request.setAttribute(MplCaptchaaddonConstants.CAPTCA_ENABLED_FOR_CURRENT_STORE,
					Boolean.valueOf(captcaEnabledForCurrentStore));
			if (captcaEnabledForCurrentStore)
			{
				request.setAttribute(MplCaptchaaddonConstants.RECAPTCHA_PUBLIC_KEY,
						getSiteConfigService().getProperty(RECAPTCHA_PUBLIC_KEY_PROPERTY));
			}
		}
		return joinPoint.proceed();
	}

	/**
	 * @description this method is used to validate entered captcha
	 * @param joinPoint
	 * @throws Throwable
	 */
	public void advise(final JoinPoint joinPoint) throws Throwable
	{
		final boolean captcaEnabledForCurrentStore = isCaptcaEnabledForCurrentStore();
		if (captcaEnabledForCurrentStore)
		{
			final List<Object> args = Arrays.asList(joinPoint.getArgs());
			final HttpServletRequest request = (HttpServletRequest) CollectionUtils.find(args,
					PredicateUtils.instanceofPredicate(HttpServletRequest.class));
			if (request != null)
			{
				request.setAttribute(MplCaptchaaddonConstants.CAPTCA_ENABLED_FOR_CURRENT_STORE,
						Boolean.valueOf(captcaEnabledForCurrentStore));
				request.setAttribute(MplCaptchaaddonConstants.RECAPTCHA_PUBLIC_KEY,
						getSiteConfigService().getProperty(RECAPTCHA_PUBLIC_KEY_PROPERTY));
				final String challengeFieldValue = sessionService.getAttribute(MplCaptchaaddonConstants.CHALLENGE);
				final String responseFieldValue = sessionService.getAttribute(MplCaptchaaddonConstants.RESPONSE);
				if ((StringUtils.isBlank(challengeFieldValue) || StringUtils.isBlank(responseFieldValue))
						|| !checkAnswer(request, challengeFieldValue, responseFieldValue))
				{
					final BindingResult bindingResult = (BindingResult) CollectionUtils.find(args,
							PredicateUtils.instanceofPredicate(BindingResult.class));

					if (bindingResult != null)
					{
						bindingResult.reject(MplCaptchaaddonConstants.RECAPTCHA_CHALLENGE_FIELD_INVALID,
								MplCaptchaaddonConstants.CHALLENGE_ANSWER_INVALID);
					}
					sessionService.setAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED, Boolean.FALSE);
				}
				else
				{
					sessionService.setAttribute(MplCaptchaaddonConstants.RECAPTCHA_CHALLANGE_ANSWERED, Boolean.TRUE);
				}
			}
		}
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
		//		validate entered captcha
		final MplRecaptchaImpl reCaptcha = new MplRecaptchaImpl();
		reCaptcha.setPrivateKey(getSiteConfigService().getProperty(RECAPTCHA_PRIVATE_KEY_PROPERTY));
		final MplRecaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(), challengeFieldValue,
				responseFieldValue);

		return reCaptchaResponse.isValid();
	}

	/**
	 * @description this method is called internally to check enability of captcha for the current store
	 * @return boolean
	 */
	private boolean isCaptcaEnabledForCurrentStore()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		return currentBaseStore != null && Boolean.TRUE.equals(currentBaseStore.getCaptchaCheckEnabled());
	}

	private SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	private BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}
