/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.checkout.steps.validation.impl;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.AbstractCheckoutStepValidator;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;


public class ResponsivePaymentCheckoutStepValidator extends AbstractCheckoutStepValidator
{

	@Autowired
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Override
	public ValidationResults validateOnEnter(final RedirectAttributes redirectAttributes)
	{
		if (!getMplCustomAddressFacade().hasValidCart())
		{
			LOG.info("Missing, empty or unsupported cart");
			return ValidationResults.REDIRECT_TO_CART;
		}

		//commented getMplCustomAddressFacade().hasNoDeliveryAddress() if condition block as part of Release 2.1 we have
		//one more delivery mode as click and collect which does not require delivery Address.
		/*if (getMplCustomAddressFacade().hasNoDeliveryAddress())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.deliveryAddress.notprovided");
			return ValidationResults.REDIRECT_TO_DELIVERY_METHOD;
		}*/

		if (getMplCustomAddressFacade().hasNoDeliveryMode())
		{
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.INFO_MESSAGES_HOLDER,
					"checkout.multi.deliveryMethod.notprovided");
			return ValidationResults.REDIRECT_TO_DELIVERY_METHOD;
		}

		// Commented to refer marketplacefacade
		/*
		 * if (!getCheckoutFlowFacade().hasValidCart()) { LOG.info("Missing, empty or unsupported cart"); return
		 * ValidationResults.REDIRECT_TO_CART; }
		 * 
		 * if (getCheckoutFlowFacade().hasNoDeliveryAddress()) { GlobalMessages.addFlashMessage(redirectAttributes,
		 * GlobalMessages.INFO_MESSAGES_HOLDER, "checkout.multi.deliveryAddress.notprovided"); return
		 * ValidationResults.REDIRECT_TO_DELIVERY_ADDRESS; }
		 * 
		 * if (getCheckoutFlowFacade().hasNoDeliveryMode()) { GlobalMessages.addFlashMessage(redirectAttributes,
		 * GlobalMessages.INFO_MESSAGES_HOLDER, "checkout.multi.deliveryMethod.notprovided"); return
		 * ValidationResults.REDIRECT_TO_DELIVERY_METHOD; }
		 */

		return ValidationResults.SUCCESS;
	}

	/**
	 * @return the mplCustomAddressFacade
	 */
	public MplCustomAddressFacade getMplCustomAddressFacade()
	{
		return mplCustomAddressFacade;
	}

	/**
	 * @param mplCustomAddressFacade
	 *           the mplCustomAddressFacade to set
	 */
	public void setMplCustomAddressFacade(final MplCustomAddressFacade mplCustomAddressFacade)
	{
		this.mplCustomAddressFacade = mplCustomAddressFacade;
	}
}