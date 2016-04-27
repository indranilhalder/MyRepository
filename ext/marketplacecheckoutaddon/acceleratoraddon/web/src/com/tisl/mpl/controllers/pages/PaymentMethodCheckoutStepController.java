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

import de.hybris.platform.acceleratorservices.payment.data.PaymentData;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PaymentDetailsForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.beans.BindingException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.bin.facade.BinFacade;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.core.enums.CodCheckMessage;
import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.BinData;
import com.tisl.mpl.data.CODData;
import com.tisl.mpl.data.EMIBankList;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplNetbankingData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.account.register.MplCustomerProfileFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.MplCustomerWebService;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.PaymentForm;
import com.tisl.mpl.util.ExceptionUtil;


/**
 *
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLPAYMENTURL)
public class PaymentMethodCheckoutStepController extends AbstractCheckoutStepController
{
	private static final Logger LOG = Logger.getLogger(PaymentMethodCheckoutStepController.class);

	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "checkoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "mplCustomerProfileFacade")
	private MplCustomerProfileFacade mplCustomerProfileFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;
	@Resource(name = "promotionsService")
	private PromotionsService promotionsService;
	@Resource(name = "binFacade")
	private BinFacade binFacade;
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "commerceCartCalculationStrategy")
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	@Resource(name = "blacklistService")
	private BlacklistService blacklistService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MplCartFacade mplCartFacade;

	@Resource(name = "mplCustomerWebService")
	private MplCustomerWebService mplCustomerWebService;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	@Autowired
	private MplCouponFacade mplCouponFacade;
	@Autowired
	private VoucherFacade voucherFacade;

	private final String checkoutPageName = "Payment Options";
	private final String RECEIVED_INR = "Received INR ";
	private final String DISCOUNT_MSSG = " discount on purchase of Promoted Product";

	/**
	 * This is the GET method which renders the Payment Page
	 *
	 * @param model
	 * @param redirectAttributes
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@Override
	@RequestMapping(value = MarketplacecheckoutaddonConstants.ADDVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateCheckoutStep(checkoutStep = MarketplacecheckoutaddonConstants.PAYMENT_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final CartModel serviceCart = getCartService().getSessionCart();
		serviceCart.setIsExpressCheckoutSelected(Boolean.valueOf(true));
		modelService.save(serviceCart);
		//redirecting to previous page for anonymous user
		if (getUserFacade().isAnonymousUser())
		{
			return getCheckoutStep().previousStep();
		}

		boolean selectPickupDetails = false;

		//code to restrict user to continue the checkout if he has not selected pickup person name and mobile number.
		//this is only when cart entry contains cnc delivery mode.
		final CartModel cartModel = getCartService().getSessionCart();
		for (final AbstractOrderEntryModel abstractOrderEntryModel : cartModel.getEntries())
		{
			if (null != abstractOrderEntryModel.getDeliveryPointOfService())
			{
				final String pickupPersonName = cartModel.getPickupPersonName();
				final String pickupPersonMobile = cartModel.getPickupPersonMobile();
				if ((pickupPersonName == null) || (pickupPersonMobile == null))
				{
					selectPickupDetails = true;
					model.addAttribute("selectPickupDetails", Boolean.valueOf(selectPickupDetails));
					return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/check";
				}
			}
		}

		//creating new Payment Form
		final PaymentForm paymentForm = new PaymentForm();
		try
		{
			//set up payment page
			setupAddPaymentPage(model);


			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			//Getting Payment modes
			final Map<String, Boolean> paymentModeMap = getMplPaymentFacade().getPaymentModes(
					MarketplacecheckoutaddonConstants.MPLSTORE, false, null);
			if (!paymentModeMap.isEmpty())
			{
				//Adding payment modes in model to be accessed from jsp
				model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODES, paymentModeMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.TRANERRORMSG, "");
				timeOutSet(model);

				//setting silent orders
				setupSilentOrderPostPage(paymentForm, model);
			}

		}
		catch (final NullPointerException e)
		{
			//LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.ERRORMSG);
			return getCheckoutStep().previousStep();
		}
		catch (final BindingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.ERRORMSG);
			return getCheckoutStep().previousStep();
		}

		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error(MarketplacecommerceservicesConstants.B6001, e);
			frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.ERRORMSG);
			return getCheckoutStep().previousStep();
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MarketplacecommerceservicesConstants.B6001, e);
			frontEndErrorHelper.callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.ERRORMSG);
			return getCheckoutStep().previousStep();
		}

		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.ERRORMSG);
			return getCheckoutStep().previousStep();
		}

		//return values
		model.addAttribute("checkoutPageName", checkoutPageName);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
	}


	/**
	 * This method sets timeout
	 *
	 * @param model
	 */
	private void timeOutSet(final Model model)
	{
		final double deliveryMode = Double.parseDouble(configurationService.getConfiguration().getString(
				"checkout.deliverymode.timeout"));
		final String timeOut = Double.toString(deliveryMode * 60 * 1000);
		model.addAttribute(MarketplacecheckoutaddonConstants.TIMEOUT, timeOut);

	}

	/**
	 * This is an OOTB method
	 *
	 * @param paymentMethodId
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.REMOVEVALUE, method = RequestMethod.POST)
	@RequireHardLogIn
	public String remove(@RequestParam(value = MarketplacecheckoutaddonConstants.PAYMENTINFOID) final String paymentMethodId,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		getUserFacade().unlinkCCPaymentInfo(paymentMethodId);
		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
				MarketplacecheckoutaddonConstants.REMOVEMESSAGE);
		return getCheckoutStep().currentStep();
	}

	/**
	 * This method gets called when the "Use These Payment Details" button is clicked. It sets the selected payment
	 * method on the checkout facade and reloads the page highlighting the selected payment method.
	 *
	 * @param selectedPaymentMethodId
	 *           - the id of the payment method to use.
	 * @return - a URL to the page to load.
	 * @throws CommerceCartModificationException
	 * @throws InvalidCartException
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHOOSEVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectPaymentMethod(
			@RequestParam(MarketplacecheckoutaddonConstants.SELECTEDPAYMENTMETHODID) final String selectedPaymentMethodId,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, InvalidCartException,
			CommerceCartModificationException
	{
		if (StringUtils.isNotBlank(selectedPaymentMethodId))
		{
			getCheckoutFacade().setPaymentDetails(selectedPaymentMethodId);
		}
		return placeOrder(model, redirectAttributes);
	}

	/**
	 * This is an OOTB method to go back to the previous checkout step
	 *
	 * @param redirectAttributes
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.BACKVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	/**
	 * This is an OOTB method to go to the next checkout step
	 *
	 * @param redirectAttributes
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.NEXTVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	/**
	 * This method is used to set up the Payment page
	 *
	 * @param model
	 * @throws CMSItemNotFoundException
	 */
	private void setupAddPaymentPage(final Model model) throws CMSItemNotFoundException, Exception
	{
		model.addAttribute(MarketplacecheckoutaddonConstants.METAROBOTS, MarketplacecheckoutaddonConstants.NOINDEX_NOFOLLOW);

		model.addAttribute(MarketplacecheckoutaddonConstants.HASNOPAYMENTINFO,
				Boolean.valueOf(getMplCustomAddressFacade().hasNoPaymentInfo()));

		prepareDataForPage(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs(MarketplacecheckoutaddonConstants.PAYMENTBREADCRUMB));
		final ContentPageModel contentPage = getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL);
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
	}


	/**
	 * This is the GET method which fetches the bank terms for EMI mode of Payment
	 *
	 * @param selectedEMIBank
	 * @return List<EMITermRateData>
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETTERMS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody List<EMITermRateData> getBankTerms(final String selectedEMIBank)
	{
		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		List<EMITermRateData> emiTermRate = new ArrayList<EMITermRateData>();
		if (null != cartData && null != cartData.getTotalPrice())
		{
			final Double totalAmount = Double.valueOf((cartData.getTotalPrice().getValue().doubleValue()));

			try
			{
				emiTermRate = getMplPaymentFacade().getBankTerms(selectedEMIBank, totalAmount);
			}
			catch (final NullPointerException e)
			{
				LOG.error(MarketplacecheckoutaddonConstants.B6008, e);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error(MarketplacecheckoutaddonConstants.B6008, e);
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
			catch (final EtailBusinessExceptions e)
			{
				LOG.error(MarketplacecommerceservicesConstants.B6008, e);

				ExceptionUtil.etailBusinessExceptionHandler(e, null);
			}

			catch (final Exception e)
			{
				LOG.error(MarketplacecommerceservicesConstants.B6008, e);
			}
		}
		return emiTermRate;
	}

	/**
	 * This is the POST method which performs basic logic related to specific payment modes and redirects to the next
	 * step Order Confirmation
	 *
	 *
	 * @param model
	 * @param paymentForm
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws InvalidCartException
	 * @throws CommerceCartModificationException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.VIEWVALUE, method = RequestMethod.POST)
	@RequireHardLogIn
	public String add(final Model model, @Valid final PaymentForm paymentForm, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, InvalidCartException, CommerceCartModificationException
	{
		//TISPRD-361
		try
		{
			setupAddPaymentPage(model);

			//getting Cartdata
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if (null != cartData)
			{
				//Adding cartdata into model
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			}

			//Logic when Payment mode is COD
			if (MarketplacecheckoutaddonConstants.PAYMENTCOD.equalsIgnoreCase(paymentForm.getPaymentMode()))
			{
				final Double cartValue = Double.valueOf(cartData.getSubTotal().getValue().doubleValue());
				final Double totalCODCharge = Double.valueOf(cartData.getConvenienceChargeForCOD().getValue().doubleValue());

				//saving COD Payment related info
				getMplPaymentFacade().saveCODPaymentInfo(cartValue, totalCODCharge);
			}

			//adding Payment id to model
			model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTID, null);
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			return placeOrder(model, redirectAttributes);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			//Start TISPRD-181
			LOG.error("Exception while completing COD Payment", e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return getCheckoutStep().currentStep();
			//End TISPRD-181
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);

			//Start TISPRD-181
			LOG.error("Exception while completing COD Payment", e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return getCheckoutStep().currentStep();
			//End TISPRD-181
		}
		catch (final Exception e)
		{
			LOG.error("Exception while completing COD Payment", e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return getCheckoutStep().currentStep();
		}


	}

	/**
	 * This method is responsible for adding the convenience charges for COD and recalculating the cart values for
	 * display
	 *
	 * @param model
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws CalculationException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.PROCESSCONVCHARGESURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody CODData processConvChargesForCOD(final Model model, final String paymentMode) throws InvalidKeyException,
			NoSuchAlgorithmException, CalculationException
	{
		final CartModel cart = getCartService().getSessionCart();
		final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
		final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
		CODData codData = new CODData();

		try
		{

			if (!mplCheckoutFacade.isPromotionValid(cart))
			{

				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
				codData = null;
			}
			else if (!mplCheckoutFacade.isCouponValid(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
				codData = null;
			}
			else
			{
				if (cart != null && cart.getEntries() != null)
				{


					for (final AbstractOrderEntryModel cartEntryModel : cart.getEntries())
					{
						if (cartEntryModel != null && !cartEntryModel.getGiveAway().booleanValue()
								&& cartEntryModel.getSelectedUSSID() != null)
						{
							freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
							freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
						}
					}
				}

				//final CartData cartData = getCheckoutFacade().getCheckoutCart(); //// Commented to refer marketplacefacade
				final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

				Long convenienceCharge = getBaseStoreService().getCurrentBaseStore().getConvenienceChargeForCOD();
				if (null == convenienceCharge)
				{
					convenienceCharge = Long.valueOf(0);
				}

				//setting conv charge in cartmodel
				cart.setConvenienceCharges(Double.valueOf(convenienceCharge.longValue()));

				//saving the cartmodel
				getMplPaymentFacade().saveCart(cart);

				final PriceData totalPriceAfterConvCharge = getMplCustomAddressFacade().setTotalWithConvCharge(cart, cartData);
				final PriceData conveniCharge = getMplCustomAddressFacade().addConvCharge(cart, cartData);

				if (StringUtils.isNotEmpty(paymentMode))
				{

					//recalculating cart
					final Double deliveyCost = cart.getDeliveryCost();
					getCommerceCartService().recalculateCart(cart);
					cart.setDeliveryCost(deliveyCost);
					getMplPaymentFacade().saveCart(cart);

					//setting the payment modes and the amount against it in session to be used later
					final Map<String, Double> paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(totalPriceAfterConvCharge.getValue().doubleValue()));
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE, paymentInfo);
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);

					// Freebie item changes
					if (cart.getEntries() != null && !freebieModelMap.isEmpty())
					{


						for (final AbstractOrderEntryModel cartEntryModel : cart.getEntries())
						{
							if (cartEntryModel != null && cartEntryModel.getGiveAway().booleanValue()
									&& cartEntryModel.getAssociatedItems() != null && cartEntryModel.getAssociatedItems().size() > 0)
							{
								saveDeliveryMethForFreebie(cartEntryModel, freebieModelMap, freebieParentQtyMap);
							}
						}
					}
				}


				//TISEE-5555
				//getting customer mobile number
				final String mplCustomerIDCellNumber = getMplPaymentFacade().fetchPhoneNumber(cart);

				codData.setConvCharge(conveniCharge);
				codData.setTotalPrice(totalPriceAfterConvCharge);
				codData.setCellNo(mplCustomerIDCellNumber);
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions while processConvChargesForCOD ", ex);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error("EtailBusinessExceptions while processConvChargesForCOD", e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception  while processConvChargesForCOD ", ex);
		}
		return codData;
	}

	/**
	 * This method is responsible for generating the OTP for COD confirmation
	 *
	 * @param model
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GENERATEOTPURL, method = RequestMethod.POST)
	@RequireHardLogIn
	//public @ResponseBody String generateOTPforCOD(final Model model, final String mobileNumber, final String prefix)
	public @ResponseBody String generateOTPforCOD(final Model model, final String mobileNumber) throws InvalidKeyException,
			NoSuchAlgorithmException
	{
		//getting current user
		final String mplCustomerID = (null == getUserService().getCurrentUser().getUid()) ? "" : getUserService().getCurrentUser()
				.getUid();
		final String mplCustomerName = (null == getUserService().getCurrentUser().getName()) ? "" : getUserService()
				.getCurrentUser().getName();
		boolean redirectFlag = false;

		final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
				MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENT);
		if (!inventoryReservationStatus)
		{
			getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
			redirectFlag = true;
		}
		if (redirectFlag)
		{
			return "redirect";
		}

		//If customer is not null
		else if (null != mplCustomerID)
		{
			//calling generate OTP with customerID
			final String otp = getMplPaymentFacade().generateOTPforCODWeb(mplCustomerID, mobileNumber, mplCustomerName, null);

			//Code refracted to MplPaymentFacadeImpl.java
			if (otp == null)
			{
				return MarketplacecheckoutaddonConstants.FAIL;
			}

			return MarketplacecheckoutaddonConstants.SUCCESS;
		}
		else
		{
			return MarketplacecheckoutaddonConstants.FAIL;
		}
	}

	/**
	 * This is a POST method which performs validation of the OTP entered by the customer for COD mode of Payment
	 *
	 * @param enteredOTPNumber
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.VALIDATEOTPURL, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody String validateOTPforCOD(
			@PathVariable(MarketplacecheckoutaddonConstants.OTPNUMFIELD) final String enteredOTPNumber) throws InvalidKeyException,
			NoSuchAlgorithmException
	{
		//getting current user
		final String mplCustomerID = getUserService().getCurrentUser().getUid();
		final CartModel cart = getCartService().getSessionCart();
		boolean redirectFlag = false;
		String validationMsg = "";

		try
		{

			LOG.debug(" TIS-414 : Checking - onclick of pay now button pincode servicabilty and promotion");
			if (!mplCheckoutFacade.isPromotionValid(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
				redirectFlag = true;
			}

			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cart);
			if (!redirectFlag && cartItemDelistedStatus)
			{
				redirectFlag = true;
			}

			//TISUTO-12 , TISUTO-11
			if (!redirectFlag)
			{
				final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENT);
				if (!inventoryReservationStatus)
				{
					getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
					redirectFlag = true;
				}
			}

			if (!redirectFlag && !mplCheckoutFacade.isCouponValid(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
				redirectFlag = true;
			}

			if (redirectFlag)
			{
				return "redirect";
			}
			else
			{
				//If customer is not null
				if (null != mplCustomerID)
				{
					validationMsg = getMplPaymentFacade().validateOTPforCODWeb(mplCustomerID, enteredOTPNumber);
				}
				else
				{
					return null;
				}
			}
		}
		catch (final NullPointerException e)
		{
			LOG.error("Error in validating OTP", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("Error in validating OTP", e);

		}
		catch (final Exception e)
		{
			LOG.error("Error in validating OTP", e);
		}

		return validationMsg;
	}


	/**
	 * This is a GET method which helps in resetting the convenience charges whenever it is not required
	 *
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws CalculationException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.RESETCONVCHARGEELSEWHEREURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String resetConvChargeElsewhere() throws InvalidKeyException, NoSuchAlgorithmException,
			CalculationException
	{
		//getting cartdata
		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		final CartModel cart = getCartService().getSessionCart();

		//setting in cartmodel
		cart.setConvenienceCharges(Double.valueOf(0));
		//saving cartmodel
		getMplPaymentFacade().saveCart(cart);

		final PriceData totalPriceAfterConvCharge = getMplCustomAddressFacade().setTotalWithConvCharge(cart, cartData);
		final PriceData conveniCharge = getMplCustomAddressFacade().addConvCharge(cart, cartData);

		if (null != getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE))
		{
			getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE);
			getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION);
		}
		//return both total price with conv charge & conv charge
		return totalPriceAfterConvCharge.getFormattedValue() + MarketplacecheckoutaddonConstants.STRINGSEPARATOR
				+ conveniCharge.getFormattedValue();
	}

	/**
	 * This method is used to set up the form and rendering it with the necessary values
	 *
	 * @param paymentForm
	 * @param model
	 */
	private void setupSilentOrderPostPage(final PaymentForm paymentForm, final Model model)
	{
		try
		{
			final PaymentData silentOrderPageData = getPaymentFacade().beginSopCreateSubscription(
					MarketplacecheckoutaddonConstants.CHECKOUTRESPONSEURL, MarketplacecheckoutaddonConstants.CHECKOUTCALLBACKURL);
			model.addAttribute(MarketplacecheckoutaddonConstants.SOPPAGEDATA, silentOrderPageData);
			paymentForm.setParameters(silentOrderPageData.getParameters());
			model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTFORMMPLURL, MarketplacecheckoutaddonConstants.PAYMENTVIEWURL);

			setupMplPaymentPage(model);
			model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTFORM, paymentForm);

		}
		catch (final IllegalArgumentException e)
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTFORMMPLURL, "");
			model.addAttribute(MarketplacecheckoutaddonConstants.SOPPAGEDATA, null);
			LOG.error(MarketplacecheckoutaddonConstants.LOGWARN, e);
			GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.GLOBALERROR);
		}

		catch (final Exception ex)
		{
			LOG.error(" Exception in setupSilentOrderPostPage", ex);
		}
	}


	/**
	 * This method is used to set up the form and rendering it with the necessary values
	 *
	 * @param model
	 */
	private void setupMplPaymentPage(final Model model) throws Exception
	{
		//getting cartdata
		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();


		double totalDiscount = 0l;
		if (cartData.getAppliedOrderPromotions() != null)
		{
			for (final PromotionResultData promotionResultData : cartData.getAppliedOrderPromotions())
			{
				final String st = promotionResultData.getDescription();
				final String result = stripNonDigits(st);

				try
				{
					totalDiscount = totalDiscount + Double.parseDouble(result);
				}
				catch (final Exception e)
				{
					LOG.error("Exception during double parsing ", e);
					totalDiscount = totalDiscount + 0;
				}
			}
			final String promotionMssg = RECEIVED_INR + totalDiscount + DISCOUNT_MSSG;
			model.addAttribute("promotionMssgDeliveryMode", promotionMssg);
		}


		//For Voucher when reloaded
		//		final List<VoucherData> voucherDataList = voucherFacade.getVouchersForCart();
		//
		//		for (final VoucherData voucher : voucherDataList)
		//		{
		//			try
		//			{
		//				//voucherFacade.releaseVoucher(voucher.getVoucherCode());
		//				mplCouponFacade.releaseVoucher(voucher.getVoucherCode(), getCartService().getSessionCart());
		//			}
		//			catch (final VoucherOperationException e)
		//			{
		//				LOG.error("Voucher with voucher code " + voucher.getVoucherCode() + " could not be released");
		//				e.printStackTrace();
		//			}
		//		}


		//getting cart subtotal value
		final Double cartValue = Double.valueOf(cartData.getSubTotal().getValue().doubleValue());
		//getting totalprice of cart
		final Double cartTotal = new Double(cartData.getTotalPrice().getValue().doubleValue());

		setupMplNetbankingForm(model);
		setupMplCODForm(model, cartValue, cartData);
		setupMplCardForm(model, cartTotal);

		//Adding all the details in model to be accessed from jsp
		model.addAttribute(MarketplacecheckoutaddonConstants.JUSPAYJSNAME,
				getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYJSNAMEVALUE));
		model.addAttribute(MarketplacecheckoutaddonConstants.SOPFORM, new PaymentDetailsForm());
		model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
		//Terms n Conditions Link
		model.addAttribute(MarketplacecheckoutaddonConstants.TNCLINK,
				getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.TNCLINKVALUE));

		//TODO: Top 5 coupons-----Commented as functionality out of scope of R2.1   Uncomment when in scope
		//model.addAttribute("voucherDataList",
		//		displayTopCoupons(getCartService().getSessionCart(), (CustomerModel) getUserService().getCurrentUser()));

		//saving cartmodel
		getMplPaymentFacade().saveCart(getCartService().getSessionCart());
	}


	/**
	 *
	 * @param input
	 * @return String
	 */
	private static String stripNonDigits(final CharSequence input) throws Exception
	{
		final StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++)
		{
			final char c = input.charAt(i);
			if ((c > 47 && c < 58) || (c == 46))
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * This method is used to set up the details for Netbanking
	 *
	 * @param model
	 * @throws EtailBusinessExceptions
	 */
	private void setupMplNetbankingForm(final Model model) throws EtailBusinessExceptions, Exception
	{
		List<MplNetbankingData> popularBankList = new ArrayList<MplNetbankingData>();
		try
		{
			//getting the popular banks
			popularBankList = getMplPaymentFacade().getBanksByPriority();
			model.addAttribute(MarketplacecheckoutaddonConstants.POPULARBANKS, popularBankList);

		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.B6002, e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.B6002, e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			//logging error message
			LOG.error(MarketplacecommerceservicesConstants.B6002, e);
		}

		Map<String, String> otherBankCodeMap = new HashMap<String, String>();
		try
		{
			//getting the other banks
			otherBankCodeMap = getMplPaymentFacade().getOtherBanks();
			model.addAttribute(MarketplacecheckoutaddonConstants.OTHERBANKS, otherBankCodeMap);

		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.B6003, e);

			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.B6003, e);

			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			//logging error message
			LOG.error(MarketplacecommerceservicesConstants.B6003, e);
		}
	}

	/**
	 * This method is used to set up the details for COD
	 *
	 * @param model
	 * @param cartValue
	 * @param cartData
	 *
	 */
	private void setupMplCODForm(final Model model, final Double cartValue, final CartData cartData)
	{

		//getting the session cart
		final CartModel cart = getCartService().getSessionCart();

		//to check customer is blacklisted or not against customer id, email, phone no. & ip address
		final String ip = getBlacklistByIPStatus();
		LOG.debug("The ip of the system is::::::::::::::::::::::::" + ip);

		//TISEE-5555
		model.addAttribute(MarketplacecheckoutaddonConstants.CELLNO, getMplPaymentFacade().fetchPhoneNumber(cart));

		try
		{
			final boolean mplCustomerIsBlackListed = getMplPaymentFacade().isBlackListed(ip, cart);

			if (!mplCustomerIsBlackListed)
			{
				//adding blacklist status to model
				model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.NOT_BLACKLISTED.toString());

				//to check items are seller fulfilled or not
				final List<String> fulfillmentDataList = new ArrayList<String>();
				for (final OrderEntryData entry : cartData.getEntries())
				{
					if (entry != null && entry.getSelectedUssid() != null)
					{
						final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
								.getSelectedUssid());
						List<RichAttributeModel> richAttributeModel = null;
						if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
						{
							richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
							if (richAttributeModel != null && richAttributeModel.get(0).getDeliveryFulfillModes() != null)
							{
								final String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
								fulfillmentDataList.add(fulfillmentType.toUpperCase());
							}
						}
					}
				}

				int flagForfulfillment = 0;
				//iterating through the fulfillment data list
				for (final String fulfillment : fulfillmentDataList)
				{
					if (!(com.tisl.mpl.core.enums.DeliveryFulfillModesEnum.TSHIP.toString().equalsIgnoreCase(fulfillment)))
					{
						flagForfulfillment = 0;
						break;
					}
					else
					{
						flagForfulfillment = 1;
					}
				}

				if (flagForfulfillment == 1)
				{
					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.TSHIP.toString());

					//item eligible for COD or not
					final List<String> paymentTypeList = new ArrayList<String>();

					//iterating over all the cart entries
					//TISBOX-883
					for (final OrderEntryData entry : cartData.getEntries())
					{
						if (entry != null && entry.getSelectedUssid() != null)
						{
							final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
									.getSelectedUssid());
							List<RichAttributeModel> richAttributeModel = null;
							if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
							{
								richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
								if (richAttributeModel != null && richAttributeModel.get(0).getPaymentModes() != null)
								{
									final String paymentMode = richAttributeModel.get(0).getPaymentModes().toString();
									if (StringUtils.isNotEmpty(paymentMode))
									{
										//setting the payment mode in a list
										paymentTypeList.add(paymentMode);
									}
								}
							}
						}

					}

					//declaring a flag
					boolean codEligibilityFlag = false;

					//iterating over the list of Payment types for all the cart entries
					for (final String paymentType : paymentTypeList)
					{
						if (PaymentModesEnum.COD.toString().equalsIgnoreCase(paymentType)
								|| PaymentModesEnum.BOTH.toString().equalsIgnoreCase(paymentType))
						{
							//flag set to true if the item's payment type is either COD or Both
							codEligibilityFlag = true;
						}
						else
						{ //flag set to false if the item's payment type is Prepaid
							codEligibilityFlag = false;
						}
					}
					if (codEligibilityFlag)
					{
						//Adding to model true if the flag value is true
						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_ELIGIBLE.toString());

						//pincode serviceability check
						if (null != cart.getIsCODEligible() && cart.getIsCODEligible().booleanValue())
						{
							//Adding to model true if the pincode is serviceable
							model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
									CodCheckMessage.PINCODE_SERVICEABLE.toString());
						}
						else
						{
							//Adding to model true if the pincode is serviceable
							model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
									CodCheckMessage.NOT_PINCODE_SERVICEABLE.toString());
						}
					}
					else
					{
						//Adding to model true if the flag value is true
						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
					}
				}
				else
				{
					//error message for Fulfillment will go here
					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.NOT_TSHIP.toString());
				}
			}
			else
			{
				//error message for Blacklisted users will go here
				model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.BLACKLISTED.toString());
			}
		}
		catch (final NullPointerException e)
		{
			//logging error message
			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);

		}
		catch (final Exception e)
		{
			//logging error message
			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
		}
	}


	/**
	 * This method is used to set up the details for Card Payment
	 *
	 * @param model
	 * @param cartTotal
	 *
	 */
	private void setupMplCardForm(final Model model, final Double cartTotal)
	{
		//getting merchant ID ofJuspay
		model.addAttribute(MarketplacecheckoutaddonConstants.MERCHANT_ID,
				getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.MERCHANTID));

		//getting redirect value(redirect or pop-up)
		model.addAttribute(MarketplacecheckoutaddonConstants.JUSPAYREDIRECT, getConfigurationService().getConfiguration()
				.getString(MarketplacecheckoutaddonConstants.JUSPAYREDIRECTKEY));

		//getting the current customer to fetch customer Id and customer email
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();

		//getting the list of stored cards
		Map<Date, SavedCardData> savedCreditCards = new TreeMap<Date, SavedCardData>();
		Map<Date, SavedCardData> savedDebitCards = new TreeMap<Date, SavedCardData>();

		try
		{
			savedCreditCards = getMplPaymentFacade().listStoredCreditCards(customer);
			if (MapUtils.isNotEmpty(savedCreditCards))
			{
				//adding cards to model
				model.addAttribute(MarketplacecheckoutaddonConstants.CREDITCARDS, savedCreditCards);
			}
			else
			{
				LOG.info("No Saved credit cards found !!");

			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6005, e);

			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6005, e);
		}

		try
		{
			savedDebitCards = getMplPaymentFacade().listStoredDebitCards(customer);
			if (!savedDebitCards.isEmpty())
			{
				//adding cards to model
				model.addAttribute(MarketplacecheckoutaddonConstants.DEBITCARDS, savedDebitCards);
			}
			else
			{
				LOG.info("No Saved debit cards found !!");

			}
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6006, e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MarketplacecheckoutaddonConstants.B6006, e);

		}

		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6006, e);
		}


		try
		{
			final List<String> countryList = getMplPaymentFacade().getCountries();
			if (CollectionUtils.isNotEmpty(countryList))
			{
				model.addAttribute(MarketplacecheckoutaddonConstants.COUNTRY, countryList);
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6007, e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6007, e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.B6007, e);
		}


		final String ebsDowntime = getConfigurationService().getConfiguration().getString(
				MarketplacecheckoutaddonConstants.EBSDOWNTIME);

		if (StringUtils.isNotEmpty(ebsDowntime))
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.EBSDOWNCHECK, ebsDowntime);
		}
		else
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.EBSDOWNCHECK, MarketplacecheckoutaddonConstants.NA);
		}

		final String noOfExpYear = getConfigurationService().getConfiguration().getString(
				MarketplacecheckoutaddonConstants.NOOFYEARS, "25");
		model.addAttribute(MarketplacecheckoutaddonConstants.EXPYEARS, noOfExpYear);

		setupMplMessages(model);
	}


	/**
	 * To add CVV text help messages
	 *
	 * @param model
	 */
	private void setupMplMessages(final Model model)
	{
		if (null != getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.CVV_HELP))
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.CVV_HELP_VAR, getConfigurationService().getConfiguration()
					.getString(MarketplacecheckoutaddonConstants.CVV_HELP));
		}
		else
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.CVV_HELP_VAR, "");
		}
	}



	/**
	 * This method is used to get the IP address of the client and check whether it is blacklisted
	 *
	 * @return Boolean
	 *
	 */
	private String getBlacklistByIPStatus()
	{
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}

		return ip;
	}


	/**
	 * This method is used to get the response from Juspay after processing card payment using iFrame
	 *
	 * @param paymentForm
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws InvalidCartException
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CARDPAYMENT, method = RequestMethod.GET)
	@RequireHardLogIn
	public String cardPayment(final Model model, @Valid final PaymentForm paymentForm, final RedirectAttributes redirectAttributes)
			throws CMSItemNotFoundException, InvalidCartException
	{
		//getting cartdata
		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		LOG.info(getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE));
		if (null != cartData)
		{
			//adding cartdata to model
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
		}

		//Order Status from Juspay
		try
		{
			final String orderStatusResponse = getMplPaymentFacade().getOrderStatusFromJuspay();
			//Redirection when transaction is successful i.e. CHARGED
			if (null != orderStatusResponse)
			{
				if (MarketplacecheckoutaddonConstants.CHARGED.equalsIgnoreCase(orderStatusResponse))
				{
					model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTID, null);
					setCheckoutStepLinksForModel(model, getCheckoutStep());
					return placeOrder(model, redirectAttributes);
				}
				else if (MarketplacecheckoutaddonConstants.JUSPAY_DECLINED.equalsIgnoreCase(orderStatusResponse))
				{
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							MarketplacecheckoutaddonConstants.DECLINEDERRORMSG);
					return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
							+ MarketplacecheckoutaddonConstants.ADDVALUE;
				}
				else if (MarketplacecheckoutaddonConstants.AUTHORIZATION_FAILED.equalsIgnoreCase(orderStatusResponse)
						|| MarketplacecheckoutaddonConstants.AUTHENTICATION_FAILED.equalsIgnoreCase(orderStatusResponse)
						|| MarketplacecheckoutaddonConstants.PENDING_VBV.equalsIgnoreCase(orderStatusResponse))
				{
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							MarketplacecheckoutaddonConstants.VBVERRORMSG);
					return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
							+ MarketplacecheckoutaddonConstants.ADDVALUE;
				}
				else
				{
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							MarketplacecheckoutaddonConstants.TRANERRORMSG);
					return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
							+ MarketplacecheckoutaddonConstants.ADDVALUE;
				}
			}
			//Redirection when transaction is failed
			else
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
						+ MarketplacecheckoutaddonConstants.ADDVALUE;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("Exception in cardPayment", e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.ADDVALUE;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("Exception in cardPayment", e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.ADDVALUE;
		}
		catch (final Exception e)
		{
			LOG.error("Exception in cardPayment", e);
			//Redirection when transaction is failed
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.ADDVALUE;
		}
	}


	/**
	 * This method is used to set the shipping address when the field is checked true by the customer
	 *
	 * @return String
	 * @throws InvalidCartException
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.SETSHIPPINGADDRESS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String setShippingAddress() throws InvalidCartException
	{
		CartData cartData = new CartData();
		String concatAddress = MarketplacecommerceservicesConstants.EMPTY;

		if (null != getMplCustomAddressFacade().getCheckoutCart())
		{
			cartData = getMplCustomAddressFacade().getCheckoutCart();
		}
		//Getting the fields from delivery address
		try
		{
			if (cartData.getDeliveryAddress() != null)
			{
				final String firstName = cartData.getDeliveryAddress().getFirstName();
				final String lastName = cartData.getDeliveryAddress().getLastName();
				final String addressLine1 = cartData.getDeliveryAddress().getLine1();
				final String addressLine2 = cartData.getDeliveryAddress().getLine2();
				final String addressLine3 = cartData.getDeliveryAddress().getLine3();
				final String country = cartData.getDeliveryAddress().getCountry().getName();
				final String state = cartData.getDeliveryAddress().getState();
				final String city = cartData.getDeliveryAddress().getTown();
				final String pincode = cartData.getDeliveryAddress().getPostalCode();

			concatAddress = firstName + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + lastName
					+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + addressLine1
					+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + addressLine2
					+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + addressLine3
					+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + country + MarketplacecheckoutaddonConstants.STRINGSEPARATOR
					+ state + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + city
					+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + pincode;
			}
			return concatAddress;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions in setShippingAddress", e);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions in setShippingAddress", e);

		}
		catch (final Exception e)
		{
			LOG.error("Exception in setShippingAddress", e);
		}

		return concatAddress;
	}

	/**
	 * This method is used to apply Payment specific promotions- Payment mode specific and Bank/Card specific
	 *
	 * @return String
	 * @throws CalculationException
	 * @throws JaloPriceFactoryException
	 * @throws JaloSecurityException
	 * @throws VoucherOperationException
	 * @throws JaloInvalidParameterException
	 * @throws NumberFormatException
	 * @throws ModelSavingException
	 *
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = MarketplacecheckoutaddonConstants.APPLYPROMOTIONS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody MplPromoPriceData applyPromotions(final String paymentMode, final String bankName)
			throws CMSItemNotFoundException, InvalidCartException, CalculationException, ModelSavingException,
			NumberFormatException, JaloInvalidParameterException, VoucherOperationException, JaloSecurityException,
			JaloPriceFactoryException
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Entering Controller applyPromotions()=====" + System.currentTimeMillis());


		final CartModel cart = getCartService().getSessionCart();
		//TISEE-510 ,TISEE-5555

		if (null != bankName && !bankName.equalsIgnoreCase("null"))
		{
			getMplPaymentFacade().setBankForSavedCard(bankName);
		}

		MplPromoPriceData responseData = new MplPromoPriceData();

		//TISPT-29
		if (null != cart
				&& StringUtils.isNotEmpty(paymentMode)
				&& (paymentMode.equalsIgnoreCase("Credit Card") || paymentMode.equalsIgnoreCase("Debit Card")
						|| paymentMode.equalsIgnoreCase("Netbanking") || paymentMode.equalsIgnoreCase("EMI")))
		{
			//setting in cartmodel
			cart.setConvenienceCharges(Double.valueOf(0));
			//saving cartmodel
			getMplPaymentFacade().saveCart(cart);
		}

		if (!mplCheckoutFacade.isPromotionValid(cart))
		{
			getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
			responseData.setErrorMsgForEMI("redirect");
		}
		else
		{

			String jsonResponse = MarketplacecommerceservicesConstants.EMPTY;
			try
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);

				final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
				final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
				if (cart != null && cart.getEntries() != null)
				{
					for (final AbstractOrderEntryModel cartEntryModel : cart.getEntries())
					{
						if (cartEntryModel != null && cartEntryModel.getGiveAway() != null
								& !cartEntryModel.getGiveAway().booleanValue() && cartEntryModel.getSelectedUSSID() != null)
						{
							freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
							freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
						}
					}
				}

				final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
				responseData = getMplPaymentFacade().applyPromotions(cartData, cart);
				if (cart != null && cart.getEntries() != null && !freebieModelMap.isEmpty())
				{
					for (final AbstractOrderEntryModel cartEntryModel : cart.getEntries())
					{
						if (cartEntryModel != null && cartEntryModel.getGiveAway().booleanValue()
								&& cartEntryModel.getAssociatedItems() != null && cartEntryModel.getAssociatedItems().size() > 0)
						{
							saveDeliveryMethForFreebie(cartEntryModel, freebieModelMap, freebieParentQtyMap);
							//start populate deliveryPointOfService for freebie
							if (LOG.isDebugEnabled())
							{
								LOG.debug("***Before Populating deliveryPointOfService for freebie product has ussID "
										+ cartEntryModel.getSelectedUSSID());
							}
							PointOfServiceModel posModel = null;
							for (final AbstractOrderEntryModel cEntry : cart.getEntries())
							{
								if (cartEntryModel.getAssociatedItems().size() == 1)
								{
									if (cEntry.getSelectedUSSID().equalsIgnoreCase(cartEntryModel.getAssociatedItems().get(0)))
									{
										if (null != cEntry.getDeliveryPointOfService())
										{
											if (LOG.isDebugEnabled())
											{
												LOG.debug("Populating deliveryPointOfService for freebie from parent, parent ussid " + cartEntryModel.getAssociatedItems().get(0));
											}
											posModel = cEntry.getDeliveryPointOfService();
										}
									}
								}
								else 
								{
									String parentUssId = findParentUssId(cartEntryModel, cart);
									if (cEntry.getSelectedUSSID().equalsIgnoreCase(parentUssId))
									{
										if (null != cEntry.getDeliveryPointOfService())
										{
											if (LOG.isDebugEnabled())
											{
												LOG.debug("Populating deliveryPointOfService for freebie from parent, parent ussid " + parentUssId);
											}
											posModel = cEntry.getDeliveryPointOfService();
										}
									}
								}
							}
							if (null != posModel)
							{
								cartEntryModel.setDeliveryPointOfService(posModel);
								modelService.save(cartEntryModel);
							}
							if (LOG.isDebugEnabled())
							{
								LOG.debug("After Populating deliveryPointOfService for freebie product has ussID "
										+ cartEntryModel.getSelectedUSSID());
							}
							//end populate deliveryPointOfService for freebie
						}
					}
				}

				//Wallet amount assigned. Will be changed after release1
				final double walletAmount = MarketplacecheckoutaddonConstants.WALLETAMOUNT;

				//setting the payment modes and the amount against it in session to be used later
				final Map<String, Double> paymentInfo = new HashMap<String, Double>();
				paymentInfo.put(paymentMode, Double.valueOf(cart.getTotalPriceWithConv().doubleValue() - walletAmount));
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE, paymentInfo);

				//TISST-7955
				final CartData promotedCartData = getMplCustomAddressFacade().getCheckoutCart();
				final Map<String, String> ussidPricemap = new HashMap<String, String>();
				for (final OrderEntryData entryData : promotedCartData.getEntries())
				{
					ussidPricemap.put(entryData.getSelectedUssid() + "_" + entryData.isGiveAway(), entryData.getTotalPrice()
							.getFormattedValue());
				}
				final ObjectMapper objectMapper = new ObjectMapper();
				jsonResponse = objectMapper.writeValueAsString(ussidPricemap);
			}
			catch (final JsonGenerationException e)
			{
				LOG.error("Error while generating JSON ", e);
			}
			catch (final JsonMappingException e)
			{
				LOG.error("Error while generating JSON ", e);
			}
			catch (final IOException e)
			{
				LOG.error("Error while generating JSON ", e);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				LOG.error("Error while generating JSON ", e);
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
			catch (final Exception e)
			{
				LOG.error("Error while generating JSON ", e);
			}
			responseData.setUssidPriceDetails(jsonResponse);
		}

		final long endTime = System.currentTimeMillis();
		LOG.debug("Time taken within Controller applyPromotions()=====" + (endTime - startTime));
		return responseData;

	}

	/**
	 * This method is used to perform BIN check when customer enters the card number in Payment screen
	 *
	 * @param binNumber
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.BINCHECK, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody BinData binCheck(@PathVariable(MarketplacecheckoutaddonConstants.BINNO) final String binNumber)
			throws EtailNonBusinessExceptions
	{
		BinData binData = null;

		try
		{
			//Code Change for TISPRO-175
			if (StringUtils.isNotEmpty(binNumber))
			{
				binData = getBinFacade().binCheck(binNumber);
			}

		}
		catch (final NullPointerException e)
		{
			LOG.error("Exception in binCheck", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("Exception in binCheck", e);
		}

		catch (final Exception e)
		{
			LOG.error("Exception in binCheck", e);
		}
		return binData;
	}




	/**
	 * This method is used to check blacklisted customers using mobile number in Payment screen -shreya
	 *
	 * @param mobileNumber
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MOBILEBLACKLIST, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody boolean mobileBlacklist(final String mobileNumber) throws EtailNonBusinessExceptions
	{
		//to check mobile is blacklisted or not
		final boolean mplMobileIsBlackListed = getMplPaymentFacade().isMobileBlackListed(mobileNumber);
		return mplMobileIsBlackListed;
	}


	/**
	 * This method is used to submit the Netbanking Payment details to Juspay for processing
	 *
	 * @param selectedNBBank
	 * @param juspayOrderId
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.SUBMITNBFORM, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String submitNBForm(final String selectedNBBank, final String juspayOrderId)
			throws EtailNonBusinessExceptions
	{
		final String paymentMethodType = MarketplacecheckoutaddonConstants.PAYMENTMETHODTYPE;
		final String redirectAfterPayment = MarketplacecheckoutaddonConstants.TRUEVALUE;
		final String format = MarketplacecheckoutaddonConstants.JSON;
		String netbankingResponse = null;

		//Logic when payment mode is Netbanking
		try
		{
			if (null != selectedNBBank)
			{
				netbankingResponse = getMplPaymentFacade().getNetbankingOrderStatus(juspayOrderId, paymentMethodType, selectedNBBank,
						redirectAfterPayment, format);
				if (null != netbankingResponse)
				{
					return netbankingResponse;
				}
			}
		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}

		catch (final Exception e)
		{
			LOG.error("Exception in submitNBForm", e);
		}

		return netbankingResponse;
	}

	@RequestMapping(value = MarketplacecheckoutaddonConstants.CREATEJUSPAYORDER, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String createJuspayOrder(final String firstName, final String lastName, final String addressLine1,
			final String addressLine2, final String addressLine3, final String country, final String state, final String city,
			final String pincode, final String cardSaved, final String sameAsShipping) throws EtailNonBusinessExceptions
	{
		String orderId = null;
		final CartModel cart = getCartService().getSessionCart();
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final String uid = customer.getUid();
		//final Double cartTotals = cart.getTotalPriceWithConv();
		boolean redirectFlag = false;
		final String returnUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8))
				+ request.getContextPath()
				+ getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYRETURNMETHOD);
		try
		{

			LOG.debug(" TIS-414  : Checking - onclick of pay now button pincode servicabilty and promotion");
			if (!redirectFlag && !mplCheckoutFacade.isPromotionValid(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
				redirectFlag = true;
			}
			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cart);
			if (!redirectFlag && cartItemDelistedStatus)
			{
				redirectFlag = true;
			}

			//TISUTO-12 , TISUTO-11
			if (!redirectFlag)
			{
				final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENT);
				if (!inventoryReservationStatus)
				{
					getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
					redirectFlag = true;
				}
			}

			if (!redirectFlag && !mplCheckoutFacade.isCouponValid(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
				redirectFlag = true;
			}

			if (redirectFlag)
			{
				return "redirect";
			}
			else
			{
				orderId = getMplPaymentFacade().createJuspayOrder(cart, firstName, lastName, addressLine1, addressLine2,
						addressLine3, country, state, city, pincode,
						cardSaved + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + sameAsShipping, returnUrl, uid,
						MarketplacecheckoutaddonConstants.CHANNEL_WEB);
			}


		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}
		catch (final AdapterException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			orderId = "JUSPAY_CONN_ERROR";
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}

		return orderId;
	}


	/**
	 * This method is used to get the banks for EMI
	 *
	 * @param cartTotal
	 * @return EMIBankList
	 * @throws InvalidCartException
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETEMIBANKS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody List<EMIBankList> getEMIBanks(final Double cartTotal) throws InvalidCartException
	{
		LOG.info("Cart Total value is:::::" + cartTotal);
		final Map<String, String> emiBankNames = getMplPaymentFacade().getEMIBankNames(cartTotal);
		final List<EMIBankList> emiBankList = new ArrayList<EMIBankList>();
		for (final Map.Entry<String, String> entry : emiBankNames.entrySet())
		{
			final EMIBankList emiBank = new EMIBankList();
			emiBank.setBankName(entry.getKey());
			emiBank.setBankCode(entry.getValue());
			emiBankList.add(emiBank);
		}

		return emiBankList;
	}




	/**
	 * This method is used to get the cards for the selected EMI Bank
	 *
	 * @param bankName
	 * @return Map<Date, SavedCardData>
	 * @throws InvalidCartException
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.LISTEMICARDS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody Map<Date, SavedCardData> getEMIBankCards(final String bankName) throws InvalidCartException
	{
		LOG.info("Bank selected is:::::" + bankName);
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final Map<Date, SavedCardData> listStoredEMICards = getMplPaymentFacade().listStoredEMICards(customer, bankName);
		return listStoredEMICards;
	}



	/**
	 * This method saves delivery mode for freebie items
	 *
	 * @param cartEntryModel
	 * @param freebieModelMap
	 * @param freebieParentQtyMap
	 */
	private void saveDeliveryMethForFreebie(final AbstractOrderEntryModel cartEntryModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
			throws Exception

	{

		// final String ussId = cartEntryModel.getAssociatedItems().get(0);
		//final MplZoneDeliveryModeValueModel mplDeliveryMode = freebieModelMap.get(ussId);
		MplZoneDeliveryModeValueModel mplDeliveryMode = null;
		if (cartEntryModel.getAssociatedItems().size() == 1)
		{
			mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
		}
		else if (cartEntryModel.getAssociatedItems().size() == 2
				&& freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode() != null
				&& freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1)).getDeliveryMode() != null
				&& freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode().getCode() != null)
		{
			if ((freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode().getCode())
					.equals((freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1)).getDeliveryMode().getCode())))
			{
				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
			}

			else if (freebieParentQtyMap.get(cartEntryModel.getAssociatedItems().get(0)).doubleValue() > freebieParentQtyMap.get(
					cartEntryModel.getAssociatedItems().get(1)).doubleValue())
			{

				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));

			}
			else
			{
				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1));
			}

			if (freebieParentQtyMap.get(cartEntryModel.getAssociatedItems().get(0)).doubleValue() == freebieParentQtyMap.get(
					cartEntryModel.getAssociatedItems().get(1)).doubleValue()
					&& freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode().getCode()
							.equalsIgnoreCase("home-delivery"))
			{
				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
			}
			else
			{
				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1));
			}

		}

		if (mplDeliveryMode != null)
		{
			//saving parent product delivery mode to freebie item
			cartEntryModel.setMplDeliveryMode(mplDeliveryMode);
			modelService.save(cartEntryModel);
		}

	}



	/**
	 * This method displays top coupons in the payments page
	 *
	 *
	 * @param cart
	 * @param customer
	 */
	// Do not delete ---- to be used later
	//	private List<VoucherDisplayData> displayTopCoupons(final CartModel cart, final CustomerModel customer)
	//	{
	//		final List<VoucherModel> voucherList = getMplCouponFacade().getAllCoupons();
	//
	//		return getMplCouponFacade().displayTopCoupons(cart, customer, voucherList);
	//	}


	/**
	 * This method is used to place an order
	 *
	 * @param model
	 * @param redirectModel
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws InvalidCartException
	 * @throws CommerceCartModificationException
	 */
	public String placeOrder(final Model model, final RedirectAttributes redirectModel) throws CMSItemNotFoundException,
			InvalidCartException, CommerceCartModificationException
	{
		LOG.info("before validate form");
		final OrderData orderData;
		try
		{
			orderData = mplCheckoutFacade.placeOrder();
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.ORDERPLACEFAIL, e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return getCheckoutStep().previousStep();
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.ORDERPLACEFAIL, e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return getCheckoutStep().previousStep();
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.ORDERPLACEFAIL, e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PLACEORDERFAILED);
			return getCheckoutStep().previousStep();
		}
		return redirectToOrderConfirmationPage(orderData);
	}

	/**
	 * This methods find delivery mode for freebie if it has more than one parents.
	 * @param entryModel
	 * @param abstractOrderModel
	 * @return delivery mode
	 */
	private String findParentUssId(final AbstractOrderEntryModel entryModel, final CartModel abstractOrderModel)
	{
		final Long ussIdA = getQuantity(entryModel.getAssociatedItems().get(0), abstractOrderModel);
		final Long ussIdB = getQuantity(entryModel.getAssociatedItems().get(1), abstractOrderModel);
		final String ussIdADelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(0), abstractOrderModel);
		final String ussIdBDelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(1), abstractOrderModel);
		String deliveryMode = null;
		if (ussIdA.doubleValue() < ussIdB.doubleValue())
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else
		{
			deliveryMode = entryModel.getAssociatedItems().get(1);
		}
		if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT) && 
				ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) &&
				ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)) 
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) &&
				ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)) 
		{
			deliveryMode = entryModel.getAssociatedItems().get(1);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) &&
				ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)) 
		{
			deliveryMode = entryModel.getAssociatedItems().get(0);
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) &&
				ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)) 
		{
			deliveryMode = entryModel.getAssociatedItems().get(1);
		}
		return deliveryMode;
	}
	
	/**
	 * Finds delivery mode for freebie.
	 * @param ussid
	 * @param abstractOrderModel
	 * @return delivery mode
	 */
	private String getDeliverModeForABgetC(final String ussid, final CartModel abstractOrderModel)
	{
		// YTODO Auto-generated method stub
		String deliveryMode = null;
		for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
		{
			if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
			{
				deliveryMode = cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
			}

		}

		return deliveryMode;
	}
	
	/**
	 * @param ussid
	 * @param abstractOrderModel
	 * @return
	 */
	private Long getQuantity(final String ussid, final AbstractOrderModel abstractOrderModel)
	{
		// YTODO Auto-generated method stub
		Long qty = null;
		for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
		{
			if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
			{
				qty = cartEntry.getQuantity();
				cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
			}

		}

		return qty;
	}
	
	/**
	 * To get the checkout step
	 *
	 * @return CheckoutStep
	 */
	private CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(MarketplacecheckoutaddonConstants.PAYMENT_METHOD);
	}


	//Getters and Setters
	/**
	 * @return the mplPaymentFacade
	 */
	public MplPaymentFacade getMplPaymentFacade()
	{
		return mplPaymentFacade;
	}

	/**
	 * @param mplPaymentFacade
	 *           the mplPaymentFacade to set
	 */
	public void setMplPaymentFacade(final MplPaymentFacade mplPaymentFacade)
	{
		this.mplPaymentFacade = mplPaymentFacade;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @return the mplCheckoutFacade
	 */
	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}


	/**
	 * @param mplCheckoutFacade
	 *           the mplCheckoutFacade to set
	 */
	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}


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
	 * @return the customerFacade
	 */
	@Override
	public CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	/**
	 * @param customerFacade
	 *           the customerFacade to set
	 */
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
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
	 * @return the mplCustomerProfileFacade
	 */
	public MplCustomerProfileFacade getMplCustomerProfileFacade()
	{
		return mplCustomerProfileFacade;
	}


	/**
	 * @param mplCustomerProfileFacade
	 *           the mplCustomerProfileFacade to set
	 */
	public void setMplCustomerProfileFacade(final MplCustomerProfileFacade mplCustomerProfileFacade)
	{
		this.mplCustomerProfileFacade = mplCustomerProfileFacade;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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

	/**
	 * @return the promotionsService
	 */
	public PromotionsService getPromotionsService()
	{
		return promotionsService;
	}


	/**
	 * @param promotionsService
	 *           the promotionsService to set
	 */
	public void setPromotionsService(final PromotionsService promotionsService)
	{
		this.promotionsService = promotionsService;
	}


	/**
	 * @return the binFacade
	 */
	public BinFacade getBinFacade()
	{
		return binFacade;
	}


	/**
	 * @param binFacade
	 *           the binFacade to set
	 */
	public void setBinFacade(final BinFacade binFacade)
	{
		this.binFacade = binFacade;
	}


	/**
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}


	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	/**
	 * @return the commerceCartCalculationStrategy
	 */
	public CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	/**
	 * @param commerceCartCalculationStrategy
	 *           the commerceCartCalculationStrategy to set
	 */
	public void setCommerceCartCalculationStrategy(final CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
	}

	/**
	 * @return the mplCustomerWebService
	 */
	public MplCustomerWebService getMplCustomerWebService()
	{
		return mplCustomerWebService;
	}

	/**
	 * @param mplCustomerWebService
	 *           the mplCustomerWebService to set
	 */
	public void setMplCustomerWebService(final MplCustomerWebService mplCustomerWebService)
	{
		this.mplCustomerWebService = mplCustomerWebService;
	}

	/**
	 * @return the blacklistService
	 */
	public BlacklistService getBlacklistService()
	{
		return blacklistService;
	}

	/**
	 * @param blacklistService
	 *           the blacklistService to set
	 */
	public void setBlacklistService(final BlacklistService blacklistService)
	{
		this.blacklistService = blacklistService;
	}


	/**
	 * @return the mplCartFacade
	 */
	public MplCartFacade getMplCartFacade()
	{
		return mplCartFacade;
	}


	/**
	 * @param mplCartFacade
	 *           the mplCartFacade to set
	 */
	public void setMplCartFacade(final MplCartFacade mplCartFacade)
	{
		this.mplCartFacade = mplCartFacade;
	}


	/**
	 * @return the mplCouponFacade
	 */
	public MplCouponFacade getMplCouponFacade()
	{
		return mplCouponFacade;
	}


	/**
	 * @param mplCouponFacade
	 *           the mplCouponFacade to set
	 */
	public void setMplCouponFacade(final MplCouponFacade mplCouponFacade)
	{
		this.mplCouponFacade = mplCouponFacade;
	}


	/**
	 * @return the voucherFacade
	 */
	public VoucherFacade getVoucherFacade()
	{
		return voucherFacade;
	}


	/**
	 * @param voucherFacade
	 *           the voucherFacade to set
	 */
	public void setVoucherFacade(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
	}

}