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

import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PlaceOrderForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.checkout.impl.MplCheckoutFacadeImpl;


@Controller
@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLSUMMARYURL)
public class SummaryCheckoutStepController extends AbstractCheckoutStepController
{
	private final static String SUMMARY = "summary";
	private static final Logger LOG = Logger.getLogger(SummaryCheckoutStepController.class);
	@Resource
	private CartService cartService;
	@Resource
	private SessionService sessionService;

	@Autowired
	private MplCheckoutFacadeImpl mplCheckoutFacade;

	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;




	/**
	 * This is the GET method which renders the Summary Page
	 *
	 * @param model
	 * @param redirectAttributes
	 * @throws CMSItemNotFoundException
	 * @throws CommerceCartModificationException
	 * @return String
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.VIEWVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = SUMMARY)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{
		//final CartData cartData = getCheckoutFacade().getCheckoutCart();  // DSC_006 : Commented for Address state field addition
		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final String productCode = entry.getProduct().getCode();
				final ProductData product = getProductFacade().getProductForCodeAndOptions(productCode,
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
				entry.setProduct(product);
			}
		}

		//cartData.setConvenienceChargeForCOD(getCartService().getSessionCart().getConvenienceCharges()); //Added for Convenience Charges in COD
		//cartData.setTotalPriceWithConvCharge(getCartService().getSessionCart().getTotalPriceWithConv()); //Added for Convenience Charges in COD
		model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
		model.addAttribute(MarketplacecheckoutaddonConstants.ALLITEMSCART, cartData.getEntries());
		model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESS, cartData.getDeliveryAddress());
		model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYMODE, cartData.getDeliveryMode());
		model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTINFO, cartData.getPaymentInfo());
		final Map<String, Double> paymentMode = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);
		if (null != paymentMode)
		{
			for (final Map.Entry<String, Double> entry : paymentMode.entrySet())
			{
				if (!(MarketplacecheckoutaddonConstants.WALLET.equalsIgnoreCase(entry.getKey())))
				{
					model.addAttribute("paymentMode", entry.getKey());
				}
			}
		}

		// Only request the security code if the SubscriptionPciOption is set to Default.
		final boolean requestSecurityCode = (CheckoutPciOptionEnum.DEFAULT.equals(getCheckoutFlowFacade()
				.getSubscriptionPciOption()));
		model.addAttribute(MarketplacecheckoutaddonConstants.REQUESTSECURITYCODE, Boolean.valueOf(requestSecurityCode));

		model.addAttribute(new PlaceOrderForm());

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs(MarketplacecheckoutaddonConstants.SUMMARYBREADCRUMB));
		model.addAttribute(MarketplacecheckoutaddonConstants.METAROBOTS, MarketplacecheckoutaddonConstants.NOINDEX_NOFOLLOW);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.CheckoutSummaryPage;
	}


	@RequestMapping(value = MarketplacecheckoutaddonConstants.PLACEORDERURL)
	@RequireHardLogIn
	public String placeOrder(
			@ModelAttribute(MarketplacecheckoutaddonConstants.PLACEORDERFORM) final PlaceOrderForm placeOrderForm,
			final Model model, final HttpServletRequest request, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException, InvalidCartException, CommerceCartModificationException
	{
		if (validateOrderForm(placeOrderForm, model))
		{
			return enterStep(model, redirectModel);
		}

		//Validate the cart
		//if (validateCart(redirectModel))
		if (false)
		{
			// Invalid cart. Bounce back to the cart page.
			return REDIRECT_PREFIX + "/cart";
		}
		final OrderData orderData;
		try
		{
			orderData = mplCheckoutFacade.placeOrder();
			/*
			 * final OrderDataPopulatorForXML orderDataPopulatorForXML = new OrderDataPopulatorForXML(orderData); final
			 * OrderDataForXML orderDataForXML = orderDataPopulatorForXML.populateForXML();
			 */
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.ORDERPLACEFAIL, e);
			GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.PLACEORDERFAILED);
			return enterStep(model, redirectModel);
		}

		return redirectToOrderConfirmationPage(orderData);
	}

	/**
	 * Validates the order form before to filter out invalid order states
	 *
	 * @param placeOrderForm
	 *           The spring form of the order being submitted
	 * @param model
	 *           A spring Model
	 * @return True if the order form is invalid and false if everything is valid.
	 */
	protected boolean validateOrderForm(final PlaceOrderForm placeOrderForm, final Model model)
	{
		/*
		 * final String securityCode = placeOrderForm.getSecurityCode(); boolean invalid = false;
		 *
		 * if (getCheckoutFlowFacade().hasNoDeliveryAddress()) { GlobalMessages.addErrorMessage(model,
		 * MarketplacecheckoutaddonConstants.DELIVERYADDRESSNOTSELECTED); invalid = true; }
		 *
		 * if (getCheckoutFlowFacade().hasNoDeliveryMode()) { GlobalMessages.addErrorMessage(model,
		 * MarketplacecheckoutaddonConstants.DELIVERYMETHODNOTSELECTED); invalid = true; } // Commented as it will be
		 * needed later on // if (getCheckoutFlowFacade().hasNoPaymentInfo()) // { //
		 * GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.notSelected"); // invalid = true; // } else { //
		 * Only require the Security Code to be entered on the summary page if the SubscriptionPciOption is set to
		 * Default. if (CheckoutPciOptionEnum.DEFAULT.equals(getCheckoutFlowFacade().getSubscriptionPciOption()) &&
		 * StringUtils.isBlank(securityCode)) { GlobalMessages.addErrorMessage(model,
		 * MarketplacecheckoutaddonConstants.NOSECURITYCODE); invalid = true; } }
		 *
		 * if (!placeOrderForm.isTermsCheck()) { GlobalMessages.addErrorMessage(model,
		 * MarketplacecheckoutaddonConstants.TERMSNOTACCEPTED); invalid = true; return invalid; } final CartData cartData
		 * = getCheckoutFacade().getCheckoutCart();
		 *
		 * if (!getCheckoutFacade().containsTaxValues()) {
		 * LOG.error(String.format(MarketplacecheckoutaddonConstants.CARTERRORMESSAGE, cartData.getCode()));
		 * GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.TAXMISSING); invalid = true; }
		 *
		 * if (!cartData.isCalculated()) {
		 * LOG.error(String.format(MarketplacecheckoutaddonConstants.CARTCALCULATEDERRORMESSAGE, cartData.getCode()));
		 * GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.CARTNOTCALCULATED); invalid = true; }
		 *
		 * return invalid;
		 */
		return false;
	}

	@RequestMapping(value = MarketplacecheckoutaddonConstants.BACKVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = MarketplacecheckoutaddonConstants.NEXTVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(SUMMARY);
	}


	//Getters and Setters

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}


	/**
	 * @return the sessionService
	 */
	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}


	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
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
