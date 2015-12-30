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
package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;


/**
 * MultiStepCheckoutController
 */
@Controller
@RequestMapping(value = "/checkout/multi")
public class MultiStepCheckoutController extends AbstractCheckoutStepController
{
	protected static final Logger LOG = Logger.getLogger(MultiStepCheckoutController.class);
	private final static String MULTI = "multi";

	@Autowired
	private AcceleratorCheckoutFacade acceleratorCheckoutFacade;

	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;

	@Override
	@RequestMapping(method = RequestMethod.GET)
	@PreValidateCheckoutStep(checkoutStep = MULTI)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{
		// In case of Normal checkout delivery address will be removed while moving to checkout page
		//TIS-391
		acceleratorCheckoutFacade.setDeliveryAddress(null);
		return getCheckoutStep().nextStep();
	}


	@RequestMapping(value = "/termsAndConditions")
	@RequireHardLogIn
	public String getTermsAndConditions(final Model model) throws CMSItemNotFoundException
	{
		final ContentPageModel pageForRequest = getCmsPageService().getPageForLabel("/termsAndConditions");
		storeCmsPageInModel(model, pageForRequest);
		setUpMetaDataForContentPage(model, pageForRequest);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getContentPageBreadcrumbBuilder().getBreadcrumbs(pageForRequest));
		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.TermsAndConditionsPopup;
	}

	@RequestMapping(value = "/express", method = RequestMethod.GET)
	@RequireHardLogIn
	public String performExpressCheckout(final Model model, final RedirectAttributes redirectModel,
			@RequestParam("expressCheckoutAddressSelector") final String expressCheckoutAddressSelector)
			throws CMSItemNotFoundException, CommerceCartModificationException
	{
		try
		{
			//Implementing IQA changes
			final Object sessionObj = getSessionService().getAttribute(WebConstants.CART_RESTORATION);

			if (sessionObj != null && CollectionUtils.isNotEmpty(((CartRestorationData) sessionObj).getModifications()))
			{
				return REDIRECT_URL_CART;
			}
			if (getCheckoutFlowFacade().hasValidCart())
			{
				switch (mplCheckoutFacade.performExpressCheckout(expressCheckoutAddressSelector))
				{
					case SUCCESS:
						return getCheckoutStep().nextStep();

					case ERROR_DELIVERY_ADDRESS:
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
								"checkout.express.error.deliveryAddress");
						return REDIRECT_URL_ADD_DELIVERY_ADDRESS;

					case ERROR_DELIVERY_MODE:
					case ERROR_CHEAPEST_DELIVERY_MODE:
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
								"checkout.express.error.deliveryMode");
						return REDIRECT_URL_CHOOSE_DELIVERY_METHOD;

					case ERROR_PAYMENT_INFO:
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
								"checkout.express.error.paymentInfo");
						return REDIRECT_URL_ADD_PAYMENT_METHOD;

					default:
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
								"checkout.express.error.notAvailable");
				}
			}

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			LOG.error("EtailNonBusinessExceptions exception happens while perform express checkout " + ex);
			return getCheckoutStep().previousStep();
		}


		return enterStep(model, redirectModel);
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(MULTI);
	}

}
