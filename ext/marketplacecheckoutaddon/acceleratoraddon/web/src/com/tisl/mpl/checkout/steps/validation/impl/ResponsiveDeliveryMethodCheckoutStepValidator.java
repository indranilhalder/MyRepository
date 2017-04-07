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
import de.hybris.platform.commercefacades.order.data.CartData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;


public class ResponsiveDeliveryMethodCheckoutStepValidator extends AbstractCheckoutStepValidator
{
	@Autowired
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Autowired
	private MplCartFacade mplCartFacade;


	@Override
	public ValidationResults validateOnEnter(final RedirectAttributes redirectAttributes)
	{
		final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);

		if (!mplCustomAddressFacade.hasValidCart(cartData))
		{
			LOG.info("Missing, empty or unsupported cart");
			return ValidationResults.REDIRECT_TO_CART;
		}

		// Commented for MArketplacefacade
		//		if (!getCheckoutFlowFacade().hasValidCart())
		//		{
		//			LOG.info("Missing, empty or unsupported cart");
		//			return ValidationResults.REDIRECT_TO_CART;
		//		}


		/*
		 * if (getCheckoutFacade().hasShippingItems() && getCheckoutFlowFacade().hasNoDeliveryAddress()) {
		 * GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
		 * "checkout.multi.deliveryAddress.notprovided"); return ValidationResults.REDIRECT_TO_DELIVERY_ADDRESS; }
		 */

		//Commented bellow code for TISPRDT-137 and TISPRD-696 defects
		/*
		 * if (!getCheckoutFacade().hasShippingItems() && getCheckoutFlowFacade().hasPickUpItems()) { return
		 * ValidationResults.REDIRECT_TO_PAYMENT_METHOD; }
		 */
		return ValidationResults.SUCCESS;
	}

}