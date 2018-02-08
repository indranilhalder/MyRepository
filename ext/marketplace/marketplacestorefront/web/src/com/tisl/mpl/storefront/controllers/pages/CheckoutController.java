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
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorfacades.flow.impl.SessionOverrideCheckoutFlowFacade;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCheckoutController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestRegisterForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.GuestRegisterValidator;
import de.hybris.platform.acceleratorstorefrontcommons.security.AutoLoginStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.product.MplJewelleryFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;


/**
 * CheckoutController
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/checkout")
public class CheckoutController extends AbstractCheckoutController
{
	protected static final Logger LOG = Logger.getLogger(CheckoutController.class);
	/**
	 * We use this suffix pattern because of an issue with Spring 3.1 where a Uri value is incorrectly extracted if it
	 * contains on or more '.' characters. Please see https://jira.springsource.org/browse/SPR-6164 for a discussion on
	 * the issue and future resolution.
	 */
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
	private static final String RECEIVED_INR = "Received INR ";
	private static final String DISCOUNT_MSSG = " discount on purchase of Promoted Product";
	private static final String CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL = "orderConfirmation";
	private static final String CONTINUE_URL_KEY = "continueUrl";
	//TISSEC-51
	public static final String FORWARD_PREFIX = "forward:";

	//private static final String NBZ_ERROR_CMS_PAGE = "nonBusinessErrorFound";
	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "guestRegisterValidator")
	private GuestRegisterValidator guestRegisterValidator;

	@Resource(name = "autoLoginStrategy")
	private AutoLoginStrategy autoLoginStrategy;

	@Autowired
	private WishlistFacade wishlistFacade;

	@Autowired
	private MplCartFacade mplCartFacade;

	@Autowired
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;

	@Autowired
	private BaseStoreService baseStoreService;

	@Autowired
	private CheckoutCustomerStrategy checkoutCustomerStrategy;

	@Autowired
	private CustomerAccountService customerAccountService;

	@Autowired
	private UserService userService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Autowired
	private ModelService modelService;

	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;

	@Resource(name = "mplSellerInformationService")
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private MplOrderFacade mplOrderFacade;//TISPT-175

	@Resource(name = "mplPaymentFacade")
	//TISSQAEE-242
	private MplPaymentFacade mplPaymentFacade;

	//JEWELLERY CHANGES STARTS
	@Resource(name = "mplJewelleryFacade")
	private MplJewelleryFacade mplJewelleryFacade;

	//JEWELLERY CHANGES ENDS

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
	 * @return the checkoutCustomerStrategy
	 */
	@Override
	public CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	{
		return checkoutCustomerStrategy;
	}

	/**
	 * @param checkoutCustomerStrategy
	 *           the checkoutCustomerStrategy to set
	 */
	public void setCheckoutCustomerStrategy(final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}

	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	@ExceptionHandler(ModelNotFoundException.class)
	public String handleModelNotFoundException(final ModelNotFoundException exception, final HttpServletRequest request)
	{
		request.setAttribute("message", exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String checkout(final RedirectAttributes redirectModel)
	{
		final CartData cartData = mplCartFacade.getSessionCartWithEntryOrdering(true);
		if (getMplCustomAddressFacade().hasValidCart(cartData))
		{
			if (validateCart(redirectModel))
			{
				return REDIRECT_PREFIX + "/cart";
			}
			else
			{
				checkoutFacade.prepareCartForCheckout();
				return getCheckoutRedirectUrl();
			}
		}
		LOG.debug("Missing, empty or unsupported cart");
		// No session cart or empty session cart. Bounce back to the cart page.
		return REDIRECT_PREFIX + "/cart";
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String orderConfirmation(@PathVariable("orderCode") final String orderCode, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		OrderModel orderModel = null;
		OrderData orderDetails = null;
		try
		{

			if (getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				LOG.debug("Inside Anonymous Checkout::" + orderCode);
				orderModel = getMplPaymentFacade().getOrderByGuid(orderCode);//TISSQAEE-242
			}
			else
			{
				LOG.debug("Inside Normal Checkout::" + orderCode);
				orderModel = mplOrderFacade.getOrder(orderCode); //TISPT-175 --- order model changes : reduce same call from two places
			}


			orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderModel); //TISPT-175 --- order details : reduce same call from two places
			//wishlistFacade.removeProductFromWL(orderCode);
			wishlistFacade.remProdFromWLForConf(orderDetails, orderModel.getUser()); //TISPT-175 --- removing products from wishlist : passing order data as it was fetching order data based on code again inside the method
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();
			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, orderDetails);
			GenericUtilityMethods.populateCheckoutSellersOrderConfirmation(model, orderModel, orderDetails);

			//UF-260
			GenericUtilityMethods.getCartPriceDetails(model, orderModel, null);

			// for MSD
			final String msdjsURL = configurationService.getConfiguration().getString("msd.js.url");
			final Boolean isMSDEnabled = Boolean.valueOf(configurationService.getConfiguration().getString("msd.enabled"));
			model.addAttribute(ModelAttributetConstants.MSD_JS_URL, msdjsURL);
			model.addAttribute(ModelAttributetConstants.IS_MSD_ENABLED, isMSDEnabled);
			//End MSD
			//LW-225,230 start
			boolean luxFlag = false;
			if (null != orderDetails && null != orderDetails.getEntries() && !orderDetails.getEntries().isEmpty())
			{
				for (final OrderEntryData entry : orderDetails.getEntries())
				{
					if (null != entry.getProduct())
					{
						if (null != entry.getProduct().getLuxIndicator()
								&& entry.getProduct().getLuxIndicator()
										.equalsIgnoreCase(ControllerConstants.Views.Pages.Cart.LUX_INDICATOR))
						{
							luxFlag = true;
						}
					}
				}
				model.addAttribute(ModelAttributetConstants.IS_LUXURY, luxFlag);
			}
			// LW-225,230 end
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(MessageConstants.EXCEPTION_IS, e);
			frontEndErrorHelper.callBusinessError(model, e.getMessage());
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MessageConstants.EXCEPTION_IS, e);
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MessageConstants.EXCEPTION_IS, e);
			frontEndErrorHelper.callNonBusinessError(model, e.getErrorMessage());
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		//return processOrderCode(orderCode, model, request);	//TISPT-175 : Changing method parameters
		return processOrderCode(orderCode, orderModel, orderDetails, model, request); //TISPT-175 : Changing method parameters
	}

	//TISPT-175 ---- Not used
	//	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	//	public String orderConfirmation(final GuestRegisterForm form, final BindingResult bindingResult, final Model model,
	//			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
	//					throws CMSItemNotFoundException
	//	{
	//		getGuestRegisterValidator().validate(form, bindingResult);
	//		return processRegisterGuestUserRequest(form, bindingResult, model, request, response, redirectModel);
	//	}

	/**
	 * Method used to determine the checkout redirect URL that will handle the checkout process.
	 *
	 * @return A <code>String</code> object of the URL to redirect to.
	 */
	protected String getCheckoutRedirectUrl()
	{
		//TPR-174

		if (checkoutFacade.getCheckoutCart() != null && checkoutFacade.getCheckoutCart().isGotMerged())
		{
			return REDIRECT_PREFIX + "/cart";
		}
		// Default to the multi-step checkout
		return REDIRECT_PREFIX + "/checkout/multi";
	}

	//	/**
	//	 * Checks if there are any items in the cart.
	//	 *
	//	 * @return returns true if items found in cart.
	//	 */
	//	@SuppressWarnings("boxing")
	//	protected boolean hasItemsInCart()
	//	{
	//
	//		if (checkoutFacade.getCheckoutCart() != null && checkoutFacade.getCheckoutCart().isGotMerged())
	//		{
	//			return true;
	//		}
	//		return false;
	//	}

	//TPR-174 ends

	//TISPT-175 ---- Not used
	//	protected String processRegisterGuestUserRequest(final GuestRegisterForm form, final BindingResult bindingResult,
	//			final Model model, final HttpServletRequest request, final HttpServletResponse response,
	//			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	//	{
	//		if (bindingResult.hasErrors())
	//		{
	//			GlobalMessages.addErrorMessage(model, "form.global.error");
	//			return processOrderCode(form.getOrderCode(), model, request);
	//		}
	//		try
	//		{
	//			getCustomerFacade().changeGuestToCustomer(form.getPwd(), form.getOrderCode());
	//			getAutoLoginStrategy().login(getCustomerFacade().getCurrentCustomer().getUid(), form.getPwd(), request, response);
	//			getSessionService().removeAttribute(WebConstants.ANONYMOUS_CHECKOUT);
	//		}
	//		catch (final DuplicateUidException e)
	//		{
	//			// User already exists
	//			LOG.error("guest registration failed: " + e);
	//			model.addAttribute(new GuestRegisterForm());
	//			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
	//					"guest.checkout.existingaccount.register.error", new Object[]
	//			{ form.getUid() });
	//			return REDIRECT_PREFIX + request.getHeader("Referer");
	//		}
	//
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			LOG.error(MessageConstants.EXCEPTION_IS, e);
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//			frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
	//			return REDIRECT_PREFIX + request.getHeader("Referer");
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//			LOG.error(MessageConstants.EXCEPTION_IS, e);
	//			frontEndErrorHelper.callNonBusinessError(model, e.getErrorMessage());
	//			return REDIRECT_PREFIX + request.getHeader("Referer");
	//		}
	//
	//		return REDIRECT_PREFIX + "/my-account";
	//	}

	/*
	 * private void callNonBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException {
	 * storeCmsPageInModel(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE)); setUpMetaDataForContentPage(model,
	 * getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));
	 *
	 * model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
	 * resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.BREADCRUMB_NOT_FOUND));
	 * GlobalMessages.addErrorMessage(model, messageKey);
	 *
	 * storeContentPageTitleInModel(model, MessageConstants.NON_BUSINESS_ERROR); }
	 */

	@SuppressWarnings("boxing")
	//	protected String processOrderCode(final String orderCode, final Model model, final HttpServletRequest request)
	//			throws CMSItemNotFoundException TISPT-175
	protected String processOrderCode(final String orderCode, final OrderModel orderModel, final OrderData orderDetails,
			final Model model, final HttpServletRequest request) throws CMSItemNotFoundException

	{
		try
		{
			//final OrderData orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			//			final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
			//			final OrderModel orderModel = getCheckoutCustomerStrategy().isAnonymousCheckout()
			//					? getCustomerAccountService().getOrderDetailsForGUID(orderCode, baseStoreModel)
			//					: getCustomerAccountService().getOrderForCode((CustomerModel) getUserService().getCurrentUser(), orderCode,
			//							baseStoreModel);
			//TISPT-175
			long totalItemCount = 0L;

			if (orderDetails != null)
			{
				//final List<OrderEntryData> orderEntryList = orderDetails.getEntries();	//TISPT-175 : check it below return
				if (orderDetails.isGuestCustomer()
						&& !StringUtils.substringBefore(orderDetails.getUser().getUid(), "|").equals(
								getSessionService().getAttribute(WebConstants.ANONYMOUS_CHECKOUT_GUID)))
				{
					return getCheckoutRedirectUrl();
				}

				final List<OrderEntryData> orderEntryList = orderDetails.getEntries(); //TISPT-175

				//if (orderDetails.getEntries() != null && !orderDetails.getEntries().isEmpty())	//TISPT-175
				if (CollectionUtils.isNotEmpty(orderEntryList))
				{
					//for (final OrderEntryData entry : orderDetails.getEntries())	//TISPT-175
					for (final OrderEntryData entry : orderEntryList) //TISPT-175
					{
						if (entry != null && entry.getProduct() != null) //TISPT-175
						{
							final String productCode = entry.getProduct().getCode();
							final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
									Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
							entry.setProduct(product);
						}
					}
				}

				double totalDiscount = 0l;
				if (orderDetails.getAppliedOrderPromotions() != null)
				{
					for (final PromotionResultData promotionResultData : orderDetails.getAppliedOrderPromotions())
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
					model.addAttribute("promotionMssg", promotionMssg);
					//final String date = mplCheckoutFacade.ordinalDate(orderCode); //TISPT-175
					final String date = mplCheckoutFacade.ordinalDate(orderDetails); //TISPT-175
					model.addAttribute("date", date);
				}

				for (final OrderEntryData entry : orderEntryList)
				{
					if (entry != null && entry.getMplDeliveryMode() != null)
					{
						if (!entry.getMplDeliveryMode().getCode().equals(MarketplacecommerceservicesConstants.CLICK_COLLECT))
						{
							totalItemCount += entry.getQuantity();
						}
					}
				}
				//bug TISRLUAT-954 Start
				/*
				 * Map<String ,String>
				 * selectedDateMap=getSessionService().getAttribute(MarketplacecheckoutaddonConstants.DELIVERY_SLOTS_TO_SESSION
				 * ); for(OrderData data:orderDetails.getSellerOrderList()){ for( DeliveryOrderEntryGroupData
				 * orderEntry:data.getDeliveryOrderGroups()){ for(OrderEntryData orderEntryData:orderEntry.getEntries()){
				 * if(null!=selectedDateMap){ for (Entry<String, String> entryForDate :selectedDateMap.entrySet()) {
				 * if(entryForDate.getKey().equalsIgnoreCase(orderEntryData.getSelectedUssid())){
				 * orderEntryData.setEddDateBetWeen(entryForDate.getValue()); } } } } } }
				 */
				//bug TISRLUAT-954 End
				//saving IP of the Customer
				try
				{
					double totalQcTotalAmount =0d;
					boolean isCliqCashApplied =false;
					if(null != orderModel && null!=orderModel.getSplitModeInfo() && orderModel.getSplitModeInfo().equalsIgnoreCase("Split")){
						if(null!=orderModel.getTotalWalletAmount() && orderModel.getTotalWalletAmount()>0){
						totalQcTotalAmount=orderModel.getTotalWalletAmount();
						isCliqCashApplied=true;
						}
					}else if(null != orderModel && null!=orderModel.getSplitModeInfo() && orderModel.getSplitModeInfo().equalsIgnoreCase("CliqCash")){
						isCliqCashApplied =true;
					}
					model.addAttribute("totalQcTotalAmount", totalQcTotalAmount);
					model.addAttribute("isCliqCashApplied", isCliqCashApplied);
					final String userIpAddress = request.getHeader("X-Forwarded-For");
					orderModel.setIpAddress(userIpAddress);
					getModelService().save(orderModel);
				}
				catch (final Exception e)
				{
					LOG.debug("Exception during IP save", e);
				}
				//End saving IP of the Customer

				orderDetails.setNet(true);

				model.addAttribute("totalCount", totalItemCount);

				model.addAttribute("orderCode", orderCode);
				model.addAttribute("orderData", orderDetails);
				model.addAttribute("allItems", orderDetails.getEntries());
				model.addAttribute("deliveryAddress", orderDetails.getDeliveryAddress());
				model.addAttribute("deliveryMode", orderDetails.getDeliveryMode());
				model.addAttribute("paymentInfo", orderDetails.getMplPaymentInfo());
				model.addAttribute("pageType", PageType.ORDERCONFIRMATION.name());

				//Handling of Order Status Message display
				if (null != orderModel && null != orderModel.getStatus() && StringUtils.isNotEmpty(orderModel.getStatus().toString()))
				{
					if (orderModel.getStatus().equals(OrderStatus.RMS_VERIFICATION_PENDING))
					{
						model.addAttribute(ModelAttributetConstants.ORDER_STATUS_MSG, getConfigurationService().getConfiguration()
								.getString(ModelAttributetConstants.ORDER_CONF_HELD));
					}
					else if (orderModel.getStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL))
					{
						model.addAttribute(ModelAttributetConstants.ORDER_STATUS_MSG, getConfigurationService().getConfiguration()
								.getString(ModelAttributetConstants.ORDER_CONF_SUCCESS));
					}
				}

				final String uid;
				
				if (orderDetails.isGuestCustomer() && !model.containsAttribute("guestRegisterForm"))
				{
					final GuestRegisterForm guestRegisterForm = new GuestRegisterForm();
					guestRegisterForm.setOrderCode(orderDetails.getGuid());
					uid = orderDetails.getPaymentInfo().getBillingAddress().getEmail();
					guestRegisterForm.setUid(uid);
					model.addAttribute(guestRegisterForm);
				}
				else
				{
					uid = orderDetails.getUser().getUid();
				}
				model.addAttribute("email", uid);
				final String continueUrl = (String) getSessionService().getAttribute(WebConstants.CONTINUE_URL);
				model.addAttribute(CONTINUE_URL_KEY, (continueUrl != null && !continueUrl.isEmpty()) ? continueUrl : ROOT);

				final AbstractPageModel cmsPage = getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL);
				storeCmsPageInModel(model, cmsPage);
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL));
				model.addAttribute("metaRobots", "noindex,nofollow");

				//TISCR-413
				if(null != orderModel.getIsEGVCart() && !orderModel.getIsEGVCart()){
				final Map<String, Integer> deliveryTimeMap = getDeliveryTime(orderModel);

				model.addAttribute("deliveryStartTime", deliveryTimeMap.get(MarketplacecommerceservicesConstants.DELIVERY_STARTTIME));
				model.addAttribute("deliveryEndTime", deliveryTimeMap.get(MarketplacecommerceservicesConstants.DELIVERY_ENDTIME));
			   }
			}
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("Error while processing order code due to ", ex);
			//TISSEC-51
			return FORWARD_PREFIX + "/404";
		}
		catch (final Exception ex)
		{
			LOG.error("EtailNonBusinessExceptions occured while sending sms ", ex);
			//TISSEC-51
			return FORWARD_PREFIX + "/404";
		}

		return ControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage;
	}

	private static String stripNonDigits(final CharSequence input)
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
	 * @Description: It finds the max of start and end delivery time between entries
	 * @param orderModel
	 * @return Map<String, Integer>
	 */
	private Map<String, Integer> getDeliveryTime(final OrderModel orderModel)
	{
		final Map<String, Integer> deliveryTimeMap = new HashMap<String, Integer>();
		final List<Integer> deliveryStartTimeList = new ArrayList<Integer>();
		final List<Integer> deliveryEndTimeList = new ArrayList<Integer>();
		Integer leadTime = null;

		for (final AbstractOrderEntryModel entry : orderModel.getEntries())
		{
			String selectedUSSID = entry.getSelectedUSSID();

			//JEWELLERY CHANGES STARTS
			if (entry.getProduct().getProductCategoryType().equalsIgnoreCase("FineJewellery"))
			{
				selectedUSSID = mplJewelleryFacade.getJewelleryInfoByUssid(selectedUSSID).get(0).getPCMUSSID();
			}
			//JEWELLERY CHANGES ENDS
			final String selectedDeliveryMode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
			final MplZoneDeliveryModeValueModel deliveryModel = mplDeliveryCostService.getDeliveryCost(selectedDeliveryMode,
					MarketplacecommerceservicesConstants.INR, selectedUSSID);
			String startValue = deliveryModel.getDeliveryMode().getStart() != null ? deliveryModel.getDeliveryMode().getStart()
					.toString() : MarketplacecommerceservicesConstants.DEFAULT_START_TIME;
			String endValue = deliveryModel.getDeliveryMode().getEnd() != null ? deliveryModel.getDeliveryMode().getEnd().toString()
					: MarketplacecommerceservicesConstants.DEFAULT_END_TIME;
			List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();

			final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(selectedUSSID);


			if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
			{
				richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
				if (CollectionUtils.isNotEmpty(richAttributeModel))
				{
					leadTime = richAttributeModel.get(0).getLeadTimeForHomeDelivery() != null ? richAttributeModel.get(0)
							.getLeadTimeForHomeDelivery() : Integer.valueOf(0);
				}
				LOG.debug(" >> Lead time for Home delivery  " + leadTime);
			}

			if (selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
			{
				startValue = String.valueOf(Integer.parseInt(startValue) + leadTime.intValue());
				endValue = String.valueOf(Integer.parseInt(endValue) + leadTime.intValue());
			}
			deliveryStartTimeList.add(Integer.valueOf(startValue));
			deliveryEndTimeList.add(Integer.valueOf(endValue));
		}

		//		Collections.sort(deliveryStartTimeList);
		//		Collections.reverse(deliveryStartTimeList);
		//
		//		Collections.sort(deliveryEndTimeList);
		//		Collections.reverse(deliveryEndTimeList);

		deliveryTimeMap.put(MarketplacecommerceservicesConstants.DELIVERY_STARTTIME, Collections.max(deliveryStartTimeList));
		deliveryTimeMap.put(MarketplacecommerceservicesConstants.DELIVERY_ENDTIME, Collections.max(deliveryEndTimeList));

		return deliveryTimeMap;
	}

	protected GuestRegisterValidator getGuestRegisterValidator()
	{
		return guestRegisterValidator;
	}

	protected AutoLoginStrategy getAutoLoginStrategy()
	{
		return autoLoginStrategy;
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
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

}