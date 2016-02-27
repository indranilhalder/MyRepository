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
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCheckoutController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestRegisterForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.validation.GuestRegisterValidator;
import de.hybris.platform.acceleratorstorefrontcommons.security.AutoLoginStrategy;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;


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
	private static final String NBZ_ERROR_CMS_PAGE = "nonBusinessErrorFound";
	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	//	@Resource(name = "orderFacade")
	//	private OrderFacade orderFacade;
	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "guestRegisterValidator")
	private GuestRegisterValidator guestRegisterValidator;

	@Resource(name = "autoLoginStrategy")
	private AutoLoginStrategy autoLoginStrategy;
	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;
	@Autowired
	private WishlistFacade wishlistFacade;

	@Autowired
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;

	//	@Autowired
	//	private SendSMSFacade sendSMSFacade;

	//	@Autowired
	//	private MplSNSMobilePushServiceImpl mplSNSMobilePushService;

	@Autowired
	private BaseStoreService baseStoreService;

	//	@Autowired
	//	private EventService eventService;

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
		if (getMplCustomAddressFacade().hasValidCart())
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

		// Commented due to Marketplace facade
		//		if (getCheckoutFlowFacade().hasValidCart())
		//		{
		//			if (validateCart(redirectModel))
		//			{
		//				return REDIRECT_PREFIX + "/cart";
		//			}
		//			else
		//			{
		//				checkoutFacade.prepareCartForCheckout();
		//				return getCheckoutRedirectUrl();
		//			}
		//		}

		LOG.debug("Missing, empty or unsupported cart");

		// No session cart or empty session cart. Bounce back to the cart page.
		return REDIRECT_PREFIX + "/cart";
	}

	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String orderConfirmation(@PathVariable("orderCode") final String orderCode, final HttpServletRequest request,
			final Model model) throws CMSItemNotFoundException
	{
		try
		{
			wishlistFacade.removeProductFromWL(orderCode);
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();
			// for MSD
			final String msdjsURL = configurationService.getConfiguration().getString("msd.js.url");
			final Boolean isMSDEnabled = Boolean.valueOf(configurationService.getConfiguration().getString("msd.enabled"));
			model.addAttribute(ModelAttributetConstants.MSD_JS_URL, msdjsURL);
			model.addAttribute(ModelAttributetConstants.IS_MSD_ENABLED, isMSDEnabled);
			//End MSD
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MessageConstants.EXCEPTION_IS + e.getMessage());
			frontEndErrorHelper.callBusinessError(model, e.getErrorMessage());
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;

		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MessageConstants.EXCEPTION_IS + e.getMessage());
			frontEndErrorHelper.callNonBusinessError(model, e.getErrorMessage());
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		return processOrderCode(orderCode, model, request);
	}


	@RequestMapping(value = "/orderConfirmation/" + ORDER_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	public String orderConfirmation(final GuestRegisterForm form, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException
	{
		getGuestRegisterValidator().validate(form, bindingResult);
		return processRegisterGuestUserRequest(form, bindingResult, model, request, response, redirectModel);
	}

	/**
	 * Method used to determine the checkout redirect URL that will handle the checkout process.
	 *
	 * @return A <code>String</code> object of the URL to redirect to.
	 */
	protected String getCheckoutRedirectUrl()
	{
		//		if (getUserFacade().isAnonymousUser())
		//		{
		//			return REDIRECT_PREFIX + "/login/checkout";
		//		}

		// Default to the multi-step checkout
		return REDIRECT_PREFIX + "/checkout/multi";
	}



	protected String processRegisterGuestUserRequest(final GuestRegisterForm form, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return processOrderCode(form.getOrderCode(), model, request);
		}
		try
		{
			getCustomerFacade().changeGuestToCustomer(form.getPwd(), form.getOrderCode());
			getAutoLoginStrategy().login(getCustomerFacade().getCurrentCustomer().getUid(), form.getPwd(), request, response);
			getSessionService().removeAttribute(WebConstants.ANONYMOUS_CHECKOUT);
		}
		catch (final DuplicateUidException e)
		{
			// User already exists
			LOG.error("guest registration failed: " + e);
			model.addAttribute(new GuestRegisterForm());
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
					"guest.checkout.existingaccount.register.error", new Object[]
					{ form.getUid() });
			return REDIRECT_PREFIX + request.getHeader("Referer");
		}

		return REDIRECT_PREFIX + "/my-account";
	}

	private void callNonBusinessError(final Model model, final String messageKey) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(NBZ_ERROR_CMS_PAGE));

		model.addAttribute(WebConstants.MODEL_KEY_ADDITIONAL_BREADCRUMB,
				resourceBreadcrumbBuilder.getBreadcrumbs(MessageConstants.BREADCRUMB_NOT_FOUND));
		GlobalMessages.addErrorMessage(model, messageKey);

		storeContentPageTitleInModel(model, MessageConstants.NON_BUSINESS_ERROR);
	}

	//	private String urlForTrackEmailContext(final HttpServletRequest request) throws CMSItemNotFoundException
	//	{
	//		URL requestUrl;
	//		final Model model = null;
	//		String profileUpdateUrl = null;
	//		try
	//		{
	//			requestUrl = new URL(request.getRequestURL().toString());
	//			final String portString = requestUrl.getPort() == -1 ? "" : ":" + requestUrl.getPort();
	//			//final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString + ""; Do not add empty strings
	//			final String baseUrl = requestUrl.getProtocol() + "://" + requestUrl.getHost() + portString;
	//			final String profileUpdatePath = request.getContextPath() + RequestMappingUrlConstants.LINK_MY_ACCOUNT
	//					+ RequestMappingUrlConstants.LINK_UPDATE_PROFILE;
	//			profileUpdateUrl = baseUrl + profileUpdatePath;
	//
	//		}
	//		catch (final MalformedURLException e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
	//					MarketplacecommerceservicesConstants.E0016));
	//			callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
	//			return ModelAttributetConstants.FAILURE;
	//		}
	//		return profileUpdateUrl;
	//	}





	@SuppressWarnings("boxing")
	protected String processOrderCode(final String orderCode, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{

		try
		{
			//final String trackorderurl = urlForTrackEmailContext(request);

			//final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode); Commented for Delivery cost fix in order confirmation page

			final OrderData orderDetails = mplCheckoutFacade.getOrderDetailsForCode(orderCode);
			final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
			final OrderModel orderModel = getCheckoutCustomerStrategy().isAnonymousCheckout() ? getCustomerAccountService()
					.getOrderDetailsForGUID(orderCode, baseStoreModel) : getCustomerAccountService().getOrderForCode(
					(CustomerModel) getUserService().getCurrentUser(), orderCode, baseStoreModel);

			long totalItemCount = 0L;

			final List<OrderEntryData> orderEntryList = orderDetails.getEntries();

			if (orderDetails.isGuestCustomer()
					&& !StringUtils.substringBefore(orderDetails.getUser().getUid(), "|").equals(
							getSessionService().getAttribute(WebConstants.ANONYMOUS_CHECKOUT_GUID)))
			{
				return getCheckoutRedirectUrl();
			}

			if (orderDetails.getEntries() != null && !orderDetails.getEntries().isEmpty())
			{
				for (final OrderEntryData entry : orderDetails.getEntries())
				{
					final String productCode = entry.getProduct().getCode();
					final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
							Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
					entry.setProduct(product);
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
						LOG.error("Exception during double parsing " + e.getMessage());
						totalDiscount = totalDiscount + 0;
					}
				}
				final String promotionMssg = RECEIVED_INR + totalDiscount + DISCOUNT_MSSG;
				model.addAttribute("promotionMssg", promotionMssg);
				final String date = mplCheckoutFacade.ordinalDate(orderCode);
				model.addAttribute("date", date);
			}

			for (final OrderEntryData entry : orderEntryList)
			{
				if (entry != null && entry.getMplDeliveryMode() !=null)
				{
					if (!entry.getMplDeliveryMode().getCode().equals(MarketplacecommerceservicesConstants.CLICK_COLLECT))
					{
						totalItemCount += entry.getQuantity();
					}
				}
			}
			//saving IP of the Customer
			try
			{
				final String userIpAddress = request.getHeader("X-Forwarded-For");
				orderModel.setIpAddress(userIpAddress);
				getModelService().save(orderModel);
			}
			catch (final Exception e)
			{
				LOG.debug("Exception during IP save" + e.getMessage());
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




			//modelService.save(osn);

			final String continueUrl = (String) getSessionService().getAttribute(WebConstants.CONTINUE_URL);
			model.addAttribute(CONTINUE_URL_KEY, (continueUrl != null && !continueUrl.isEmpty()) ? continueUrl : ROOT);

			final AbstractPageModel cmsPage = getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL);
			storeCmsPageInModel(model, cmsPage);
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL));
			model.addAttribute("metaRobots", "noindex,nofollow");
			//sms changes for order place
			/*
			 * final String mobileNumber = orderDetails.getDeliveryAddress().getPhone(); final String firstName =
			 * orderDetails.getDeliveryAddress().getFirstName(); final String orderReferenceNumber =
			 * orderDetails.getCode(); final String trackingUrl = getConfigurationService().getConfiguration()
			 * .getString(MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL) + orderReferenceNumber;
			 *
			 *


			 * // Email ************* Order Placed final OrderProcessModel orderProcessModel = new OrderProcessModel();
			 * orderProcessModel.setOrder(orderModel); orderProcessModel.setOrderTrackUrl(trackorderurl); final
			 * OrderPlacedEvent orderplacedEvent = new OrderPlacedEvent(orderProcessModel); try {
			 * eventService.publishEvent(orderplacedEvent); } catch (final Exception e1) { // YTODO // Auto-generated catch
			 * block LOG.error("Exception during sending mail >> " + e1.getMessage()); }
			 *

			 * try { sendSMSFacade.sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID,
			 *

			 * MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED
			 * .replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
			 * .replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderReferenceNumber)
			 * .replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, trackingUrl), mobileNumber);
			 */
			/*


			 * try { //mplCheckoutFacade.triggerEmailAndSmsOnOrderConfirmation(orderModel, orderDetails, trackorderurl);
			 * //mplCheckoutFacade.sendMobileNotifications(orderDetails);
			 * 


			 * } catch (final EtailNonBusinessExceptions ex) {

			 * LOG.error("EtailNonBusinessExceptions occured while sending sms " + ex.getMessage()); }
			 */

			// TODO: TIS-1178: Email & SMS ********* On Hold Due to Risks

		}
		catch (final EtailNonBusinessExceptions ex)
		{
			LOG.error("Error while processing order code due to " + ex.getMessage());
			throw ex;
		}
		catch (final Exception ex)
		{
			LOG.error("EtailNonBusinessExceptions occured while sending sms " + ex.getMessage());
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
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

}

