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

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.PaymentDetailsForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.GenericSearchConstants.LOG;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.beans.BindingException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.granule.json.JSONException;
import com.granule.json.JSONObject;
import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.tisl.mpl.bin.facade.BinFacade;
import com.tisl.mpl.checkout.steps.validation.impl.ResponsivePaymentCheckoutStepValidator;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.core.enums.CodCheckMessage;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.enums.WalletEnum;
import com.tisl.mpl.core.model.BankforNetbankingModel;
import com.tisl.mpl.core.model.CustomerWalletDetailModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.model.SavedCardModel;
import com.tisl.mpl.data.BinData;
import com.tisl.mpl.data.CODData;
import com.tisl.mpl.data.EMIBankList;
import com.tisl.mpl.data.EMITermRateData;
import com.tisl.mpl.data.MplNetbankingData;
import com.tisl.mpl.data.MplPromoPriceData;
import com.tisl.mpl.data.SavedCardData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.exception.QCServiceCallException;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.product.PriceBreakupFacade;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.egv.data.EgvDetailsData;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.wallet.MplWalletFacade;
import com.tisl.mpl.juspay.response.ListCardsResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.egv.service.cart.MplEGVCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.BankModel;
import com.tisl.mpl.model.PaymentModeRestrictionModel;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pojo.request.Customer;
import com.tisl.mpl.pojo.request.QCCustomerRegisterRequest;
import com.tisl.mpl.pojo.response.BalanceBucketWise;
import com.tisl.mpl.pojo.response.Bucket;
import com.tisl.mpl.pojo.response.CustomerWalletDetailResponse;
import com.tisl.mpl.pojo.response.QCCustomerRegisterResponse;
import com.tisl.mpl.pojo.response.QCRedeeptionResponse;
import com.tisl.mpl.pojo.response.RedimGiftCardResponse;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.web.forms.AddToCardWalletForm;
import com.tisl.mpl.storefront.web.forms.EgvDetailForm;
import com.tisl.mpl.storefront.web.forms.PaymentForm;
import com.tisl.mpl.storefront.web.forms.validator.MplEgvFormValidator;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;

import reactor.function.support.UriUtils;

/**
 *
 * @author TCS
 *
 */
@Controller
@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLPAYMENTURL)
public class PaymentMethodCheckoutStepController extends AbstractCheckoutStepController
{


	private static final String GIFT_CARD = "/giftCard-";
	private static final String EGVGUID = "EGVGUID";
	private static final String MARKETPLACE_HEADER_EGV_PRODUCT_CODE = "marketplace.header.egvProductCode";




	private static final Logger LOG = Logger.getLogger(PaymentMethodCheckoutStepController.class);



	//Added for TPR-4461 (GETTING THE BANK OF THE USER'S PAYMENT MODE) starts here
	//public static String bankNameUserPaymentMode;

	private static final String GIFT_CART_PAYMENT = "/giftCartPayment";

	/**
	 * @return the bankNameUserPaymentMode
	 */
	//	public static String getBankNameUserPaymentMode()
	//	{
	//		return bankNameUserPaymentMode;
	//	}

	@Autowired
	MplEGVCartService mplEGVCartService;

	/**
	 * @param bankNameUserPaymentMode
	 *           the bankNameUserPaymentMode to set
	 */
	//	public static void setBankNameUserPaymentMode(final String bankNameUserPaymentMode)
	//	{
	//		PaymentMethodCheckoutStepController.bankNameUserPaymentMode = bankNameUserPaymentMode;
	//	}

	//TPR-4461 ends here

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
	//@Resource(name = "customerFacade")
	//private CustomerFacade customerFacade;
	//@Resource(name = "mplCustomerProfileFacade")
	//private MplCustomerProfileFacade mplCustomerProfileFacade;
	//@Resource(name = "sessionService")
	//private SessionService sessionService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;
	//@Resource(name = "promotionsService")
	//private PromotionsService promotionsService;
	@Resource(name = "binFacade")
	private BinFacade binFacade;
	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	//@Resource(name = "commerceCartCalculationStrategy")
	//private CommerceCartCalculationStrategy commerceCartCalculationStrategy;
	//@Resource(name = "blacklistService")
	//private BlacklistService blacklistService;

	@Autowired
	private MplOrderDao mplOrderDao;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private MplCartFacade mplCartFacade;

	//@Resource(name = "mplCustomerWebService")
	//private MplCustomerWebService mplCustomerWebService;

	@Resource(name = "mplSellerInformationService")
	private MplSellerInformationService mplSellerInformationService;

	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;

	//Added for TPR-4461 starts here
	@Resource(name = "voucherService")
	private VoucherService voucherService;
	//Added for TPR-4461 ends here

	//Added for tpr-3782
	@Resource(name = "priceBreakupFacade")
	private PriceBreakupFacade priceBreakupFacade;

	//@Autowired
	//private MplCouponFacade mplCouponFacade;

	//PMD
	//@Autowired
	//private VoucherFacade voucherFacade;
	//@Autowired
	//private FlexibleSearchService flexibleSearchService;
	//@Autowired
	//private Converter<OrderModel, OrderData> orderConverter;
	//@Autowired
	//private JuspayEBSService juspayEBSService;

	@Resource(name = "defaultResponsivePaymentMethodCheckoutValidator")
	private ResponsivePaymentCheckoutStepValidator paymentValidator;

	//PMD
	//@Resource(name = "mplDefaultCommerceCartCalculationStrategy")
	//private MplCommerceCartCalculationStrategy calculationStrategy;

	@Resource(name = "notificationFacade")
	private NotificationFacade notificationFacade;

	@Autowired
	private MplConfigFacade mplConfigFacade;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;
	@Resource(name = "mplWalletFacade")
	private MplWalletFacade mplWalletFacade;
	@Autowired
	MplEgvFormValidator mplEgvFormValidator;

	/**
	 * @return the mplWalletFacade
	 */
	public MplWalletFacade getMplWalletFacade()
	{
		return mplWalletFacade;
	}


	/**
	 * @param mplWalletFacade
	 *           the mplWalletFacade to set
	 */
	public void setMplWalletFacade(final MplWalletFacade mplWalletFacade)
	{
		this.mplWalletFacade = mplWalletFacade;
	}



	private final String checkoutPageName = "Payment Options";
	private final String RECEIVED_INR = "Congrats Received";
	private final String DISCOUNT_MSSG = " discount on purchase of Promoted Product";
	private static final String UTF = "UTF-8";


	/**
	 * This is the GET method which renders the Payment Page. Custom method written instead of overridden method to
	 * handle additional parameters for TPR-629
	 *
	 * @param model
	 * @param redirectAttributes
	 * @param guid
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	//@Override
	@RequestMapping(value = MarketplacecheckoutaddonConstants.PAYVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	//@PreValidateCheckoutStep(checkoutStep = MarketplacecheckoutaddonConstants.PAYMENT_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes,
			@RequestParam(value = "value", required = false, defaultValue = "") final String guid,
			@RequestParam(value = "dispMsg", required = false) final String dispMsg) throws CMSItemNotFoundException
	{
		//OrderIssues:-  multiple Payment Response from juspay restriction

		//redirecting to previous page for anonymous user
		if (getUserFacade().isAnonymousUser())
		{
			return getCheckoutStep().previousStep();
		}
		final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
		ValidationResults validationResult = null;

		//	final String cartGuid = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.CARTGUID);
		final String refNumber = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.REFNUMBER);

		LOG.debug("refNumber number is ...................." + refNumber);
		//Validator called explicitly TPR-629
		/*
		 * if (StringUtils.isEmpty(guid)) { validationResult = paymentValidator.validateOnEnterOptimized(cartData,
		 * redirectAttributes); } if (null != validationResult &&
		 * ValidationResults.REDIRECT_TO_CART.equals(validationResult)) { return
		 * MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART; }
		 */
		//Validator called explicitly TPR-629

		//Commented for implementing mRupee TISSQAEE-229
		//		if (StringUtils.isEmpty(guid))
		//		{
		//			validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
		//		}

		//Implementing mRupee Logic with cartGuid TISSQAEE-229
		OrderModel mRupeeorderModel = null;
		String cartGuid = null;
		/* Getting guid from audit table based on the reference no. received from mRupee */
		if (StringUtils.isNotEmpty(refNumber))
		{
			cartGuid = getMplPaymentFacade().getWalletAuditEntries(refNumber);
		}
		LOG.debug("cartGuid number is ...................." + cartGuid);
		if (StringUtils.isNotEmpty(cartGuid))
		{
			mRupeeorderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
		}

		LOG.debug("mRupeeorderModel is ++++++++" + mRupeeorderModel);

		if (StringUtils.isEmpty(guid) && null != mRupeeorderModel && null != mRupeeorderModel.getIsWallet()
				&& !WalletEnum.MRUPEE.equals(mRupeeorderModel.getIsWallet()))
		{
			LOG.debug("mRupeeorderModel.getIsWallet() is ++++++++" + mRupeeorderModel.getIsWallet());
			validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
		}
		if (null != validationResult && ValidationResults.REDIRECT_TO_CART.equals(validationResult))
		{
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
		}
		//Validator called explicitly TPR-629
		//Commented for implementing mRupee TISSQAEE-229
		//		if (StringUtils.isEmpty(guid))
		//		{
		//			validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
		//		}
		//Implementing mRupee Logic with cartGuid TISSQAEE-229
		if (StringUtils.isEmpty(guid) && null != mRupeeorderModel && null != mRupeeorderModel.getIsWallet()
				&& !WalletEnum.MRUPEE.equals(mRupeeorderModel.getIsWallet()))
		{
			validationResult = paymentValidator.validateOnEnterOptimized(cartData, redirectAttributes);
		}

		if (null != validationResult && ValidationResults.REDIRECT_TO_CART.equals(validationResult))
		{
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
		}

		try
		{
			boolean selectPickupDetails = false;
			//Payment page will be shown based on cart or order(after first failure payment) TPR-629
			OrderModel orderModel = null;

			//Commented for implementing mRupee TISSQAEE-229

			//			if (StringUtils.isNotEmpty(guid))
			//			{
			//				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			//			}

			//Implementing mRupee Logic with cartGuid TISSQAEE-229
			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}
			else if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}
			//code to restrict user to continue the checkout if he has not selected pickup person name and mobile number.
			//this is only when cart entry contains cnc delivery mode.
			final Map<String, MarketplaceDeliveryModeData> freebieModelMap = new HashMap<String, MarketplaceDeliveryModeData>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
			Map<String, Boolean> paymentModeMap = null;
			OrderData orderData = null;
			//final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			if (null == orderModel)
			{
				//Existing code


				// TPR-429 START

				final String checkoutSellerID = populateCheckoutSellers(cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, checkoutSellerID);
				// TPR-429 END

				if (cartData != null)
				{
					//cartModel.setIsExpressCheckoutSelected(Boolean.valueOf(true));
					//getModelService().save(cartModel);

					for (final OrderEntryData orderEntry : cartData.getEntries())
					{
						if (null != orderEntry.getDeliveryPointOfService())
						{
							final String pickupPersonName = cartData.getPickupPersonName();
							final String pickupPersonMobile = cartData.getPickupPersonMobile();
							if ((pickupPersonName == null) || (pickupPersonMobile == null))
							{
								selectPickupDetails = true;
								model.addAttribute("selectPickupDetails", Boolean.valueOf(selectPickupDetails));
								//return MarketplacecommerceservicesConstants.REDIRECT + "/checkout/multi/delivery-method/check";
								return getCheckoutStep().previousStep();
							}
						}
						if (!orderEntry.isGiveAway() && orderEntry.getSelectedUssid() != null)
						{
							freebieModelMap.put(orderEntry.getSelectedUssid(), orderEntry.getMplDeliveryMode());
							freebieParentQtyMap.put(orderEntry.getSelectedUssid(), orderEntry.getQuantity());
						}
						//Added for 3782
						//if (null != orderEntry.getProduct()
						//		&& MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(orderEntry.getProduct()
						//				.getProductCategoryType()))
						//{
						//	final boolean breakupLoad = priceBreakupFacade.createPricebreakupOrder(orderEntry);

						//}
						//End of changes for TPR-3782
					}
				}

				final CartModel cartModel = getCartService().getSessionCart();
				//Moved to single method in facade TPR-629
				getMplPaymentFacade().populateDeliveryPointOfServ(cartModel);

				//TISST-13012
				//final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart()); TISPT-169
				final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cartModel); //Changed to cartModel as it is already assigned from session(reduce session call)

				if (cartItemDelistedStatus)
				{
					return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
				}

				//Getting Payment modes
				paymentModeMap = getMplPaymentFacade().getPaymentModes(MarketplacecheckoutaddonConstants.MPLSTORE, false, cartData);

				// OrderIssues:- Set the value duplicatJuspayResponse in session to false  ones cart GUID available in session
				final Map<String, Boolean> duplicatJuspayResponseMap = new HashMap<String, Boolean>();
				duplicatJuspayResponseMap.put(cartData.getGuid(), Boolean.FALSE);

				getSessionService().setAttribute(MarketplacecommerceservicesConstants.DUPLICATEJUSPAYRESONSE,
						duplicatJuspayResponseMap);

				//Cart guid added to propagate to further methods via jsp
				model.addAttribute(MarketplacecheckoutaddonConstants.GUID, cartData.getGuid());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTTOORDERCONVERT, Boolean.FALSE); //INC144315475

				GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);

				//UF-260
				GenericUtilityMethods.getCartPriceDetails(model, cartModel, null);

			}
			//TPR-629 --- based on orderModel
			else
			{
				orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				// TPR-429 START
				final String checkoutSellerID = populateCheckoutSellersForOrder(orderData);
				model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, checkoutSellerID);
				// TPR-429 END
				//Getting Payment modes
				paymentModeMap = getMplPaymentFacade().getPaymentModes(MarketplacecheckoutaddonConstants.MPLSTORE, orderData);


				//TISSQAUAT-536 fixes

				model.addAttribute(MarketplacecheckoutaddonConstants.GUID, orderModel.getGuid());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTTOORDERCONVERT, Boolean.TRUE); //INC144315475

				GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
				//UF-260
				GenericUtilityMethods.getCartPriceDetails(model, orderModel, null);
			}

			//creating new Payment Form
			final PaymentForm paymentForm = new PaymentForm();
			setupAddPaymentPage(model);

			if (MapUtils.isNotEmpty(paymentModeMap)) // Code optimization for performance fix TISPT-169
			{
				//Adding payment modes in model to be accessed from jsp
				model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODES, paymentModeMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.TRANERRORMSG, "");
				timeOutSet(model);

				//setting silent orders
				setupSilentOrderPostPage(paymentForm, model, orderData, cartData);
			}

			final String payNowPromotionCheck = getSessionService().getAttribute(
					MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);
			//Egv Changes
			model.addAttribute("isEGVCart", Boolean.FALSE);
			if (StringUtils.isNotEmpty(payNowPromotionCheck)
					&& payNowPromotionCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);
				GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.PROMOTIONEXPIRED);
			}


		}
		//Nullpointer exception commented TPR-629
		//		catch (final NullPointerException e)
		//		{
		//			//LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
		//					MarketplacecheckoutaddonConstants.ERRORMSG);
		//			return getCheckoutStep().previousStep();
		//		}
		catch (final BindingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.ERRORMSG);
			return getCheckoutStep().previousStep();
		}

		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.E0007, e);
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
		if (StringUtils.isNotEmpty(dispMsg))
		{
			model.addAttribute("dispMsg", dispMsg);
		}
		//return values
		model.addAttribute("checkoutPageName", checkoutPageName);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
	}


	private String getCaptchaKey()
	{
		final String recaptchaKey = configurationService.getConfiguration().getString(
				ModelAttributetConstants.RECAPTCHA_PUBLIC_KEY_PROPERTY);

		return recaptchaKey;
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





	//Commented as this method is not used anymore as per Payment Solution TPR-629
	//	/**
	//	 * This is an OOTB method
	//	 *
	//	 * @param paymentMethodId
	//	 * @param redirectAttributes
	//	 * @return String
	//	 * @throws CMSItemNotFoundException
	//	 */
	//	@RequestMapping(value = MarketplacecheckoutaddonConstants.REMOVEVALUE, method = RequestMethod.POST)
	//	@RequireHardLogIn
	//	public String remove(@RequestParam(value = MarketplacecheckoutaddonConstants.PAYMENTINFOID) final String paymentMethodId,
	//			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	//	{
	//		getUserFacade().unlinkCCPaymentInfo(paymentMethodId);
	//		GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.CONF_MESSAGES_HOLDER,
	//				MarketplacecheckoutaddonConstants.REMOVEMESSAGE);
	//		return getCheckoutStep().currentStep();
	//	}

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
	//	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHOOSEVALUE, method = RequestMethod.GET)
	//	@RequireHardLogIn
	//	public String doSelectPaymentMethod(
	//			@RequestParam(MarketplacecheckoutaddonConstants.SELECTEDPAYMENTMETHODID) final String selectedPaymentMethodId,
	//			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException, InvalidCartException,
	//			CommerceCartModificationException
	//	{
	//		if (StringUtils.isNotBlank(selectedPaymentMethodId))
	//		{
	//			getCheckoutFacade().setPaymentDetails(selectedPaymentMethodId);
	//		}
	//		return placeOrder(model, redirectAttributes);
	//	}





	/**
	 * This is an OOTB method to go back to the previous checkout step
	 *
	 * @param redirectAttributes
	 * @return String
	 *
	 */
	//@RequestMapping(value = MarketplacecheckoutaddonConstants.BACKVALUE, method = RequestMethod.GET)
	//@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}




	/**
	 * This is an OOTB method to go to the next checkout step
	 *
	 * @param redirectAttributes
	 * @return String
	 *
	 */
	//@RequestMapping(value = MarketplacecheckoutaddonConstants.NEXTVALUE, method = RequestMethod.GET)
	//@RequireHardLogIn
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
	 * @throws Exception
	 *
	 */
	private void setupAddPaymentPage(final Model model) throws CMSItemNotFoundException, Exception
	{
		model.addAttribute(MarketplacecheckoutaddonConstants.METAROBOTS, MarketplacecheckoutaddonConstants.NOINDEX_NOFOLLOW);

		//model.addAttribute(MarketplacecheckoutaddonConstants.HASNOPAYMENTINFO,
		//		Boolean.valueOf(getMplCustomAddressFacade().hasNoPaymentInfo()));

		prepareDataForPage(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs(MarketplacecheckoutaddonConstants.PAYMENTBREADCRUMB));
		final ContentPageModel contentPage = getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL);
		storeCmsPageInModel(model, contentPage);
		setUpMetaDataForContentPage(model, contentPage);
		setCheckoutStepLinksForModel(model, getCheckoutStep());
		model.addAttribute("paymentPage", "paymentPage");
	}






	/**
	 * This is the GET method which fetches the bank terms for EMI mode of Payment
	 *
	 * @param selectedEMIBank
	 * @param cartTotal
	 * @return List<EMITermRateData>
	 *
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GETTERMS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody List<EMITermRateData> getBankTerms(final String selectedEMIBank, final Double cartTotal)
	{
		//final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		List<EMITermRateData> emiTermRate = new ArrayList<EMITermRateData>();

		/////////////////TPR-4461///////////////////

		//getSessionService().setAttribute(MarketplacecommerceservicesConstants.BANKFROMBINFORPROMOTION,
		//	(null != selectedEMIBank && StringUtils.isNotEmpty(selectedEMIBank)) ? selectedEMIBank : null);//selected EMI BANK setting for voucher payment mode and bank specific restriction
		//		if (null != selectedEMIBank && StringUtils.isNotEmpty(selectedEMIBank))
		//		{
		//			setBankNameUserPaymentMode(selectedEMIBank);
		//		}
		/////////////////TPR-4461///////////////////

		//if (null != cartData && null != cartData.getTotalPrice())
		//{
		//final Double totalAmount = Double.valueOf((cartData.getTotalPrice().getValue().doubleValue()));

		try
		{
			emiTermRate = getMplPaymentFacade().getBankTerms(selectedEMIBank, cartTotal);
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
		//}
		return emiTermRate;
	}




	/**
	 * This is the POST method which performs basic logic related to specific payment modes and redirects to the next
	 * step Order Confirmation based on TPR-629
	 *
	 *
	 * @param model
	 * @param paymentForm
	 * @param redirectAttributes
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws InvalidCartException
	 * @throws CommerceCartModificationException
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.VIEWVALUE, method = RequestMethod.POST)
	@RequireHardLogIn
	public String add(final Model model, @Valid final PaymentForm paymentForm, final RedirectAttributes redirectAttributes)
	//OrderIssues:-
			throws CMSItemNotFoundException, InvalidCartException, CommerceCartModificationException, EtailNonBusinessExceptions
	{
		//TISPRD-361
		try
		{
			setupAddPaymentPage(model);

			//COD is submitted based on cart or order(after first failure payment) TPR-629
			OrderModel orderModel = null;
			if (null != paymentForm && StringUtils.isNotEmpty(paymentForm.getGuid()))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(paymentForm.getGuid());
			}

			if (null == orderModel)
			{
				//Existing code based on cartModel during first payment try
				//getting CartModel
				final CartModel cartModel = cartService.getSessionCart();
				//getting Cartdata
				final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

				//Logic when Payment mode is COD
				if (null != cartData
						&& MarketplacecheckoutaddonConstants.PAYMENTCOD.equalsIgnoreCase(paymentForm.getPaymentModeValue()))
				{
					//Adding cartdata into model
					model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);

					final Double cartValue = Double.valueOf(cartData.getSubTotal().getValue().doubleValue());
					final Double totalCODCharge = Double.valueOf(cartData.getConvenienceChargeForCOD().getValue().doubleValue());

					//saving COD Payment related info
					getMplPaymentFacade().saveCODPaymentInfo(cartValue, totalCODCharge, cartModel);

					//adding Payment id to model
					model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTID, null);
					setCheckoutStepLinksForModel(model, getCheckoutStep());
					return placeOrder(model, redirectAttributes);
				}
				else
				{
					LOG.error("Exception while completing COD Payment in /view");
					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
					return getCheckoutStep().previousStep();
				}
			}
			//Handled for OrderModel TPR-629
			else
			{

				if (null == orderModel.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(orderModel.getStatus()))
				{
					final Double orderValue = orderModel.getSubtotal();
					final Double totalCODCharge = orderModel.getConvenienceCharges();

					//saving COD Payment related info
					getMplPaymentFacade().saveCODPaymentInfo(orderValue, totalCODCharge, orderModel);

					//adding Payment id to model
					model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTID, null);
					setCheckoutStepLinksForModel(model, getCheckoutStep());
					return updateOrder(orderModel, redirectAttributes);
				}
				else
				{
					return updateOrder(orderModel, redirectAttributes);
				}
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception while completing COD Payment", e);
			ExceptionUtil.getCustomizedExceptionTrace(e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return getCheckoutStep().currentStep();
			//End TISPRD-181
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
	 * display modified based on TPR-629
	 *
	 * @param model
	 * @param paymentMode
	 * @param guid
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws CalculationException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.PROCESSCONVCHARGESURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody CODData processConvChargesForCOD(final Model model, final String paymentMode, final String guid)
			throws InvalidKeyException, NoSuchAlgorithmException, CalculationException
	{
		//CODData codData = new CODData();	//Commented to handle COD Data properly for exception IQA TPR-629
		CODData codData = null;
		try
		{
			//Conv Charge processed based on CartModel or OrderModel TPR-629
			OrderModel orderModel = null;
			if (StringUtils.isNotEmpty(guid))
			{
				//final String orderGuid = decryptKey(guid);
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
			if (null == orderModel)
			{
				//Existing code for CartModel
				final CartModel cart = getCartService().getSessionCart();

				//IQA for TPR-629
				if (null != cart)
				{
					if (!getMplCheckoutFacade().isPromotionValid(cart))
					{

						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
						//codData = null;		//Commented to handle COD Data properly for exception IQA TPR-629
					}
					else if (!getMplCheckoutFacade().isCouponValid(cart))
					{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
						//codData = null;		//Commented to handle COD Data properly for exception IQA TPR-629
					}
					else
					{
						if (cart.getEntries() != null)
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

						//final CartData cartData = getCheckoutFacade().getCheckoutCart(); // Commented to refer marketplacefacade
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
									if (cartEntryModel != null && null != cartEntryModel.getGiveAway()
											&& cartEntryModel.getGiveAway().booleanValue() && cartEntryModel.getAssociatedItems() != null
											&& cartEntryModel.getAssociatedItems().size() > 0)
									{
										saveDeliveryMethForFreebie(cartEntryModel, freebieModelMap, freebieParentQtyMap);
									}
								}
							}
						}

						//TISEE-5555
						//getting customer mobile number
						final String mplCustomerIDCellNumber = getMplPaymentFacade().fetchPhoneNumber(cart);
						codData = new CODData(); //Added to handle COD Data properly for exception IQA TPR-629
						codData.setConvCharge(conveniCharge);
						codData.setTotalPrice(totalPriceAfterConvCharge);
						codData.setCellNo(mplCustomerIDCellNumber);
					}
				}
			}
			//Code implementation for OrderModel TPR-629
			else
			{
				if (orderModel.getEntries() != null)
				{
					for (final AbstractOrderEntryModel cartEntryModel : orderModel.getEntries())
					{
						if (cartEntryModel != null && !cartEntryModel.getGiveAway().booleanValue()
								&& cartEntryModel.getSelectedUSSID() != null)
						{
							freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
							freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
						}
					}
				}

				final OrderData orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				Long convenienceCharge = getBaseStoreService().getCurrentBaseStore().getConvenienceChargeForCOD();
				if (null == convenienceCharge)
				{
					convenienceCharge = Long.valueOf(0);
				}

				//setting conv charge in cartmodel
				orderModel.setConvenienceCharges(Double.valueOf(convenienceCharge.longValue()));

				//saving the cartmodel
				getModelService().save(orderModel);

				final PriceData totalPriceAfterConvCharge = getMplCustomAddressFacade().setTotalWithConvCharge(orderModel, orderData);
				final PriceData conveniCharge = getMplCustomAddressFacade().addConvCharge(orderModel, orderData);

				if (StringUtils.isNotEmpty(paymentMode))
				{
					//recalculating cart
					final Double deliveyCost = orderModel.getDeliveryCost();
					getMplCartFacade().recalculateOrder(orderModel);
					orderModel.setDeliveryCost(deliveyCost);
					getModelService().save(orderModel);

					//setting the payment modes and the amount against it in session to be used later
					final Map<String, Double> paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(totalPriceAfterConvCharge.getValue().doubleValue()));
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE, paymentInfo);
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);

					// Freebie item changes
					if (orderModel.getEntries() != null && !freebieModelMap.isEmpty())
					{
						for (final AbstractOrderEntryModel orderEntryModel : orderModel.getEntries())
						{
							if (orderEntryModel != null && null != orderEntryModel.getGiveAway()
									&& orderEntryModel.getGiveAway().booleanValue() && orderEntryModel.getAssociatedItems() != null
									&& orderEntryModel.getAssociatedItems().size() > 0)
							{
								saveDeliveryMethForFreebie(orderEntryModel, freebieModelMap, freebieParentQtyMap);
							}
						}
					}

					//TISEE-5555
					//getting customer mobile number
					final String mplCustomerIDCellNumber = getMplPaymentFacade().fetchPhoneNumber(orderModel);

					codData = new CODData(); //Added to handle COD Data properly for exception IQA TPR-629
					codData.setConvCharge(conveniCharge);
					codData.setTotalPrice(totalPriceAfterConvCharge);
					codData.setCellNo(mplCustomerIDCellNumber);
				}
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error("Exception  while processConvChargesForCOD in saving model", e);
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
	 * This method is responsible for generating the OTP for COD confirmation for both cart and order TPR-629 (try/catch
	 * added)
	 *
	 * @param model
	 * @param mobileNumber
	 * @param guid
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.GENERATEOTPURL, method = RequestMethod.POST)
	@RequireHardLogIn
	//public @ResponseBody String generateOTPforCOD(final Model model, final String mobileNumber, final String prefix)
	public @ResponseBody String generateOTPforCOD(final Model model, final String mobileNumber, final String guid) //guid added as parameter TPR-629
			throws InvalidKeyException, NoSuchAlgorithmException
	{
		//String mplCustomerID = "";
		OrderModel orderModel = null;
		OrderData odata = null;
		try
		{

			//OTP handled for both cart and order
			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);

			}

			//getting current user
			final String mplCustomerID = (null == getUserService().getCurrentUser().getUid()) ? "" : getUserService()
					.getCurrentUser().getUid();
			final String mplCustomerName = (null == getUserService().getCurrentUser().getName()) ? "" : getUserService()
					.getCurrentUser().getName();
			if (null == orderModel)
			{
				final CartModel cart = getCartService().getSessionCart();
				//added for CAR:127
				final CartData cData = getMplCartFacade().getCartDataFromCartModel(cart, false);
				//added for CAR:127
				//Existing code for cart
				boolean redirectFlag = false;


				//TPR3780 STARTS HERE
				final double prevTotalCartPrice = cart.getTotalPrice().doubleValue();
				//TPR3780 ENDS HERE


				//commented for CAR:127
				/*
				 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
				 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
				 */

				final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cData, cart);


				//TPR3780 STARTS HERE
				final CartModel cartModelAfterinventoryCheck = getCartService().getSessionCart();
				final double newTotalCartPrice = cartModelAfterinventoryCheck.getTotalPrice().doubleValue();


				if (!StringUtils.equals(String.valueOf(prevTotalCartPrice), String.valueOf(newTotalCartPrice)))
				{
					return MarketplacecheckoutaddonConstants.INVENTORYRESERVED;
				}

				//TPR3780 ENDS HERE



				if (!inventoryReservationStatus)
				{
					getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
					redirectFlag = true;
				}
				if (redirectFlag)
				{
					return MarketplacecheckoutaddonConstants.REDIRECTSTRING;
				}

				//If customer is not null
				else if (null != mplCustomerID)
				{
					//calling generate OTP with customerID
					final String otp = getMplPaymentFacade().generateOTPforCOD(mplCustomerID, mobileNumber, mplCustomerName, cart);

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
			//Code implemented for Order TPR-629
			else
			{
				//added for CAR:127
				odata = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				//added for CAR:127
				boolean redirectFlag = false;
				if (!getMplCheckoutFacade().isPromotionValid(orderModel))
				{

					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
					getMplCartFacade().recalculateOrder(orderModel);
					redirectFlag = true;
				}

				if (!redirectFlag)
				{
					//commented for CAR:127
					/*
					 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel);
					 */
					final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, odata, orderModel);
					if (!inventoryReservationStatus)
					{
						getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_ORDER_INVENTORY_RESV_SESSION_ID,
								"TRUE");
						getMplCartFacade().recalculateOrder(orderModel);
						redirectFlag = true;
						//notify EMAil SMS TPR-815
						mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
					}
				}
				if (redirectFlag)
				{
					return MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT;
				}
				else if (null != mplCustomerID)
				{
					//calling generate OTP with customerID
					final String otp = getMplPaymentFacade().generateOTPforCOD(mplCustomerID, mobileNumber, mplCustomerName,
							orderModel);

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
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("Error in generating COD OTP", e);
			return MarketplacecheckoutaddonConstants.FAIL;
		}
		catch (final Exception e)
		{
			LOG.error("Error in generating COD OTP", e);
			return MarketplacecheckoutaddonConstants.FAIL;
		}

	}


	/**
	 * This is a POST method which performs validation of the OTP entered by the customer for COD mode of Payment (code
	 * added as per TPR-629)
	 *
	 * @param enteredOTPNumber
	 * @param guid
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.VALIDATEOTPURL, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody String validateOTPforCOD(
			@PathVariable(MarketplacecheckoutaddonConstants.OTPNUMFIELD) final String enteredOTPNumber, final String guid)
			throws InvalidKeyException, NoSuchAlgorithmException
	{

		boolean redirectFlag = false;
		String validationMsg = "";
		OrderModel orderModel = null;
		String emailId = null;
		//added for CAR:127
		OrderData oData = null;
		//added for CAR:127
		try
		{
			//getting current user
			emailId = getUserService().getCurrentUser().getUid();
			//OTP handled for both cart and order
			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);

			}
			if (null == orderModel)
			{
				//Existing code for cartModel
				final CartModel cart = getCartService().getSessionCart();
				//added for CAR:127
				final CartData cData = getMplCartFacade().getCartDataFromCartModel(cart, false);
				//added for CAR:127
				LOG.debug(" TIS-414 : Checking - onclick of pay now button pincode servicabilty and promotion");
				if (!getMplCheckoutFacade().isPromotionValid(cart))
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


					//TPR3780 STARTS HERE
					final double prevTotalCartPrice = cart.getTotalPrice().doubleValue();
					//TPR3780 ENDS HERE


					//commented for CAR:127
					/*
					 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
					 */

					final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					//MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cData, cart);


					//TPR3780 STARTS HERE
					final CartModel cartModelAfterinventoryCheck = getCartService().getSessionCart();
					final double newTotalCartPrice = cartModelAfterinventoryCheck.getTotalPrice().doubleValue();


					if (!StringUtils.equals(String.valueOf(prevTotalCartPrice), String.valueOf(newTotalCartPrice)))
					{
						return MarketplacecheckoutaddonConstants.INVENTORYRESERVED;
					}

					//TPR3780 ENDS HERE



					if (!inventoryReservationStatus)
					{
						getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
						redirectFlag = true;
					}
				}

				if (!redirectFlag && !getMplCheckoutFacade().isCouponValid(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
					redirectFlag = true;
				}

				//TISPRO-497
				final Double cartTotal = cart.getTotalPrice();
				final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();

				if (!redirectFlag && cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
					redirectFlag = true;
				}
				//TISPRO-578
				if (!redirectFlag && !getMplPaymentFacade().isValidCart(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID, "TRUE");
					redirectFlag = true;
				}

				if (redirectFlag)
				{
					return MarketplacecheckoutaddonConstants.REDIRECTSTRING;
				}
				else
				{
					//If customer is not null
					if (null != emailId)
					{
						validationMsg = getMplPaymentFacade().validateOTPforCODWeb(emailId, enteredOTPNumber);
					}
					else
					{
						return null;
					}
				}

			}
			//Code implemented for Order TPR-629
			else
			{
				//added for CAR:127
				oData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				//added for CAR:127
				//TPR-815
				if (!getMplCheckoutFacade().isPromotionValid(orderModel))
				{
					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
					getMplCartFacade().recalculateOrder(orderModel);
					redirectFlag = true;
				}

				if (!redirectFlag)
				{
					//commented for CAR:127
					/*
					 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel);
					 */
					final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, oData, orderModel);
					if (!inventoryReservationStatus)
					{
						getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_ORDER_INVENTORY_RESV_SESSION_ID,
								"TRUE");
						getMplCartFacade().recalculateOrder(orderModel);
						redirectFlag = true;
						//notify EMAil SMS TPR-815
						mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
					}
				}
				if (redirectFlag)
				{
					return MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT;
				}


				//If customer is not null
				if (null != emailId)
				{
					validationMsg = getMplPaymentFacade().validateOTPforCODWeb(emailId, enteredOTPNumber);
				}
				else
				{
					return null;
				}
			}

		}
		//		catch (final NullPointerException e)
		//		{
		//			LOG.error("Error in validating OTP", e);
		//		}
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

		if (null != cart)
		{
			//setting in cartmodel
			cart.setConvenienceCharges(Double.valueOf(0));
		}
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
	private void setupSilentOrderPostPage(final PaymentForm paymentForm, final Model model, final OrderData orderData,
			final CartData cartData)
	{
		try
		{
			//TISSTRT-1390
			//Commented as not used TPR-629
			//final PaymentData silentOrderPageData = getPaymentFacade().beginSopCreateSubscription(
			//		MarketplacecheckoutaddonConstants.CHECKOUTRESPONSEURL, MarketplacecheckoutaddonConstants.CHECKOUTCALLBACKURL);
			//model.addAttribute(MarketplacecheckoutaddonConstants.SOPPAGEDATA, silentOrderPageData);
			//paymentForm.setParameters(silentOrderPageData.getParameters());
			model.addAttribute(MarketplacecheckoutaddonConstants.NEWPAYMENTFORMMPLURL,
					MarketplacecheckoutaddonConstants.NEWPAYMENTVIEWURL);

			setupMplPaymentPage(model, orderData, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTFORM, paymentForm);

		}
		catch (final IllegalArgumentException e)
		{
			model.addAttribute(MarketplacecheckoutaddonConstants.NEWPAYMENTFORMMPLURL, "");
			model.addAttribute(MarketplacecheckoutaddonConstants.SOPPAGEDATA, null);
			LOG.error(MarketplacecheckoutaddonConstants.LOGWARN, e);
			GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.GLOBALERROR);
		}

		catch (final Exception ex)
		{
			LOG.error(" Exception in setupSilentOrderPostPage", ex);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}




	/**
	 * This method is used to set up the form and rendering it with the necessary values(code added as per TPR-629)
	 *
	 * @param model
	 */
	private void setupMplPaymentPage(final Model model, final OrderData orderData, final CartData cartData) throws Exception
	{
		if (null == orderData)
		{
			//Existing code for cart
			//getting cartdata
			//final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if (null != cartData && cartData.getAppliedOrderPromotions() != null)
			{
				//final double totalDiscount = getMplPaymentFacade().calculateTotalDiscount(cartData.getAppliedOrderPromotions());
				//				for (final PromotionResultData promotionResultData : cartData.getAppliedOrderPromotions())
				//				{
				//					final String st = promotionResultData.getDescription();
				//					final String result = stripNonDigits(st);
				//
				//					try
				//					{
				//						totalDiscount = totalDiscount + Double.parseDouble(result);
				//					}
				//					catch (final Exception e)
				//					{
				//						LOG.error("Exception during double parsing ", e);
				//						totalDiscount = totalDiscount + 0;
				//					}
				//				}
				//final String promotionMssg = RECEIVED_INR /*+ totalDiscount*/ + DISCOUNT_MSSG;
				final String promotionMssg = RECEIVED_INR + DISCOUNT_MSSG;
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
			//final Double cartValue = Double.valueOf(cartData.getSubTotal().getValue().doubleValue());
			//getting totalprice of cart
			final Double cartTotal = new Double(cartData.getTotalPrice().getValue().doubleValue());

			//setupMplNetbankingForm(model);		//TISPT-235 Commented to make ajax call for netbanking
			//setupMplCODForm(model, cartValue, cartData);
			setupMplCardForm(model, cartTotal);

			//Adding all the details in model to be accessed from jsp

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			//TODO: Top 5 coupons-----Commented as functionality out of scope of R2.1   Uncomment when in scope
			//model.addAttribute("voucherDataList",
			//		displayTopCoupons(getCartService().getSessionCart(), (CustomerModel) getUserService().getCurrentUser()));

			//saving cartmodel
			getMplPaymentFacade().saveCart(getCartService().getSessionCart());
			model.addAttribute("isCart", Boolean.TRUE);
		}
		else
		{
			//Code added for order TPR-629
			if (orderData.getAppliedOrderPromotions() != null)
			{
				final double totalDiscount = getMplPaymentFacade().calculateTotalDiscount(orderData.getAppliedOrderPromotions());
				final String promotionMssg = RECEIVED_INR + totalDiscount + DISCOUNT_MSSG;
				model.addAttribute("promotionMssgDeliveryMode", promotionMssg);
			}

			final Double cartTotal = new Double(orderData.getTotalPrice().getValue().doubleValue());

			setupMplCardForm(model, cartTotal);

			//Adding all the details in model to be accessed from jsp
			model.addAttribute(MarketplacecheckoutaddonConstants.ORDERDATA, orderData);
			model.addAttribute("isCart", Boolean.FALSE);
		}

		//Added for mRupee

		//getting merchant for mRupee
		model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_MERCHANT_URL, getConfigurationService().getConfiguration()
				.getString(MarketplacecheckoutaddonConstants.MRUPEEURL));

		//getting redirect url mRupee
		model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_CODE,
				getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.MRUPEE_MERCHANT_CODE));

		model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_NARRATION, getConfigurationService().getConfiguration()
				.getString(MarketplacecheckoutaddonConstants.MRUPEE_NARRATION_VALUE));

		//mRupee configuration ends



		//		for (final OrderEntryData cartEntryData : cartData.getEntries())
		//		{
		//			final CartModel cartModel = getCartService().getSessionCart();
		//			final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
		//			for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
		//			{
		if (null != cartData)
		{
			if (null != cartData.getEntries())
			{
				//TISSTRT-1501 ends
				for (final OrderEntryData cartEntryData : cartData.getEntries())
				{
					final CartModel cartModel = getCartService().getSessionCart();
					final List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries();
					for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
					{
						if (null != cartEntryModel && null != cartEntryModel.getMplDeliveryMode())
						{
							if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(cartEntryData.getSelectedUssid()))
							{
								cartEntryData.setEddDateBetWeen(cartEntryModel.getSddDateBetween());
							}
						}
					}

					if (null != cartEntryData && cartEntryData.getScheduledDeliveryCharge() != null)
					{
						if (cartEntryData.getScheduledDeliveryCharge().doubleValue() > 0)
						{
							// final CartModel cartModel = getCartService().getSessionCart();
							final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
							cartData.setDeliverySlotCharge(mplCheckoutFacade.createPrice(cartModel,
									Double.valueOf(configModel.getSdCharge())));
						}
					}
				}
				//TISSTRT-1501 starts
			}
			//			if (null != cartEntryData && cartEntryData.getScheduledDeliveryCharge() != null)
			//			{
			//				if (cartEntryData.getScheduledDeliveryCharge().doubleValue() > 0)
			//				{
			//					// final CartModel cartModel = getCartService().getSessionCart();
			//					final MplBUCConfigurationsModel configModel = mplConfigFacade.getDeliveryCharges();
			//					cartData
			//							.setDeliverySlotCharge(mplCheckoutFacade.createPrice(cartModel, Double.valueOf(configModel.getSdCharge())));
			//				}
			//			}


		}
		//			}
		//TISSTRT-1501 ends
		model.addAttribute(MarketplacecheckoutaddonConstants.JUSPAYJSNAME,
				getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYJSNAMEVALUE));
		model.addAttribute(MarketplacecheckoutaddonConstants.SOPFORM, new PaymentDetailsForm());
		//Terms n Conditions Link
		model.addAttribute(MarketplacecheckoutaddonConstants.TNCLINK,
				getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.TNCLINKVALUE));

	}

	//COde commented as not used
	//	/**
	//	 *
	//	 * @param input
	//	 * @return String
	//	 */
	//	private static String stripNonDigits(final CharSequence input) throws Exception
	//	{
	//		final StringBuilder sb = new StringBuilder(input.length());
	//		for (int i = 0; i < input.length(); i++)
	//		{
	//			final char c = input.charAt(i);
	//			if ((c > 47 && c < 58) || (c == 46))
	//			{
	//				sb.append(c);
	//			}
	//		}
	//		return sb.toString();
	//	}



	//	/**
	//	 * This method is used to set up the details for Netbanking This method is commented to handle netbanking using AJAX
	//	 * call
	//	 *
	//	 * @param model
	//	 * @throws EtailBusinessExceptions
	//	 */
	//	private void setupMplNetbankingForm(final Model model) throws EtailBusinessExceptions, Exception
	//	{
	//		List<MplNetbankingData> popularBankList = new ArrayList<MplNetbankingData>();
	//		// TISPT-169
	//		List<BankforNetbankingModel> netBankingList = new ArrayList<BankforNetbankingModel>(); //TISPT-169
	//
	//		try
	//		{
	//			netBankingList = getMplPaymentFacade().getNetBankingBanks(); // TISPT-169
	//			//getting the popular banks
	//			popularBankList = getMplPaymentFacade().getBanksByPriority(netBankingList); // TISPT-169
	//			model.addAttribute(MarketplacecheckoutaddonConstants.POPULARBANKS, popularBankList);
	//
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			LOG.error(MarketplacecommerceservicesConstants.B6002, e);
	//			ExceptionUtil.getCustomizedExceptionTrace(e);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			LOG.error(MarketplacecommerceservicesConstants.B6002, e);
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//		}
	//		catch (final Exception e)
	//		{
	//			LOG.error(MarketplacecommerceservicesConstants.B6002, e);
	//		}
	//
	//		Map<String, String> otherBankCodeMap = new HashMap<String, String>();
	//		try
	//		{
	//			//getting the other banks
	//			otherBankCodeMap = getMplPaymentFacade().getOtherBanks(netBankingList); // TISPT-169
	//			model.addAttribute(MarketplacecheckoutaddonConstants.OTHERBANKS, otherBankCodeMap);
	//
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			LOG.error(MarketplacecommerceservicesConstants.B6003, e);
	//
	//			ExceptionUtil.getCustomizedExceptionTrace(e);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			LOG.error(MarketplacecommerceservicesConstants.B6003, e);
	//
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//		}
	//		catch (final Exception e)
	//		{
	//			//logging error message
	//			LOG.error(MarketplacecommerceservicesConstants.B6003, e);
	//		}
	//	}





	/**
	 * This method is used to set up the details for Netbanking TISPT-235
	 *
	 * @param model
	 * @throws EtailBusinessExceptions
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.SETUPMPLNETBANKINGFORM, method = RequestMethod.GET)
	@RequireHardLogIn
	public String setupMplNetbankingForm(final Model model) throws EtailBusinessExceptions, Exception
	{
		List<MplNetbankingData> popularBankList = new ArrayList<MplNetbankingData>();
		// TISPT-169
		List<BankforNetbankingModel> netBankingList = new ArrayList<BankforNetbankingModel>(); //TISPT-169

		try
		{
			netBankingList = getMplPaymentFacade().getNetBankingBanks(); // TISPT-169
			//getting the popular banks
			popularBankList = getMplPaymentFacade().getBanksByPriority(netBankingList); // TISPT-169
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
			LOG.error(MarketplacecommerceservicesConstants.B6002, e);
		}

		Map<String, String> otherBankCodeMap = new HashMap<String, String>();
		try
		{
			//getting the other banks
			otherBankCodeMap = getMplPaymentFacade().getOtherBanks(netBankingList); // TISPT-169
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

		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.NetbankingPanel;
	}




	/**
	 * This method is used to set up the details for COD | Code commented as a fix for TISPT-235. Handling COD using Ajax
	 *
	 * @param model
	 * @param cartValue
	 * @param cartData
	 *
	 */
	//	private void setupMplCODForm(final Model model, final Double cartValue, final CartData cartData)
	//	{
	//
	//		//getting the session cart
	//		final CartModel cart = getCartService().getSessionCart();
	//
	//		//to check customer is blacklisted or not against customer id, email, phone no. & ip address
	//		//final String ip = getBlacklistByIPStatus(); TISPT-204 Point No 2
	//		final String ip = getMplPaymentFacade().getBlacklistByIPStatus(request);
	//		LOG.debug("The ip of the system is::::::::::::::::::::::::" + ip);
	//
	//		//TISEE-5555
	//		model.addAttribute(MarketplacecheckoutaddonConstants.CELLNO, getMplPaymentFacade().fetchPhoneNumber(cart));
	//
	//		try
	//		{
	//			final boolean mplCustomerIsBlackListed = getMplPaymentFacade().isBlackListed(ip, cart);
	//
	//			if (!mplCustomerIsBlackListed)
	//			{
	//				//adding blacklist status to model
	//				model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.NOT_BLACKLISTED.toString());
	//
	//				//to check items are seller fulfilled or not
	//				final List<String> fulfillmentDataList = new ArrayList<String>();
	//				final List<String> paymentTypeList = new ArrayList<String>(); //TISPT-204
	//				for (final OrderEntryData entry : cartData.getEntries())
	//				{
	//					if (entry != null && entry.getSelectedUssid() != null)
	//					{
	//						final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
	//								.getSelectedUssid());
	//						List<RichAttributeModel> richAttributeModel = null;
	//						if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
	//						{
	//							richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
	//							if (richAttributeModel != null && richAttributeModel.get(0).getDeliveryFulfillModes() != null)
	//							{
	//								final String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
	//								fulfillmentDataList.add(fulfillmentType.toUpperCase());
	//							}
	//
	//							//Start TISPT-204 Point No 1
	//							if (richAttributeModel != null && richAttributeModel.get(0) != null
	//									&& richAttributeModel.get(0).getPaymentModes() != null)
	//							{
	//								final String paymentMode = richAttributeModel.get(0).getPaymentModes().toString();
	//								if (StringUtils.isNotEmpty(paymentMode))
	//								{
	//									//setting the payment mode in a list
	//									paymentTypeList.add(paymentMode);
	//								}
	//								//End TISPT-204 Point No 1
	//							}
	//						}
	//					}
	//				}
	//
	//				int flagForfulfillment = 0;
	//				//iterating through the fulfillment data list
	//				for (final String fulfillment : fulfillmentDataList)
	//				{
	//					if (!(com.tisl.mpl.core.enums.DeliveryFulfillModesEnum.TSHIP.toString().equalsIgnoreCase(fulfillment)))
	//					{
	//						flagForfulfillment = 0;
	//						break;
	//					}
	//					else
	//					{
	//						flagForfulfillment = 1;
	//					}
	//				}
	//
	//				if (flagForfulfillment == 1)
	//				{
	//					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.TSHIP.toString());
	//
	//					//item eligible for COD or not
	//					//final List<String> paymentTypeList = new ArrayList<String>(); TISPT-204
	//					// Code commented as part of TISPT-204 Point No 1 , paymentTypeList is populated in earlier for loop
	//					//iterating over all the cart entries
	//					//TISBOX-883
	//					//					for (final OrderEntryData entry : cartData.getEntries())
	//					//					{
	//					//						if (entry != null && entry.getSelectedUssid() != null)
	//					//						{
	//					//							final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
	//					//									.getSelectedUssid());
	//					//							List<RichAttributeModel> richAttributeModel = null;
	//					//							if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
	//					//							{
	//					//								richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
	//					//								if (richAttributeModel != null && richAttributeModel.get(0).getPaymentModes() != null)
	//					//								{
	//					//									final String paymentMode = richAttributeModel.get(0).getPaymentModes().toString();
	//					//									if (StringUtils.isNotEmpty(paymentMode))
	//					//									{
	//					//										//setting the payment mode in a list
	//					//										paymentTypeList.add(paymentMode);
	//					//									}
	//					//								}
	//					//							}
	//					//						}
	//					//
	//					//					}
	//
	//					//declaring a flag
	//					boolean codEligibilityFlag = false;
	//
	//					//iterating over the list of Payment types for all the cart entries
	//					for (final String paymentType : paymentTypeList)
	//					{
	//						if (PaymentModesEnum.COD.toString().equalsIgnoreCase(paymentType)
	//								|| PaymentModesEnum.BOTH.toString().equalsIgnoreCase(paymentType))
	//						{
	//							//flag set to true if the item's payment type is either COD or Both
	//							codEligibilityFlag = true;
	//						}
	//						else
	//						{ //flag set to false if the item's payment type is Prepaid
	//							codEligibilityFlag = false;
	//						}
	//					}
	//					if (codEligibilityFlag)
	//					{
	//						//Adding to model true if the flag value is true
	//						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_ELIGIBLE.toString());
	//
	//						//pincode serviceability check
	//						if (null != cart && null != cart.getIsCODEligible() && cart.getIsCODEligible().booleanValue())
	//						{
	//							//Adding to model true if the pincode is serviceable
	//							model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
	//									CodCheckMessage.PINCODE_SERVICEABLE.toString());
	//						}
	//						else
	//						{
	//							//Adding to model true if the pincode is serviceable
	//							model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
	//									CodCheckMessage.NOT_PINCODE_SERVICEABLE.toString());
	//						}
	//					}
	//					else
	//					{
	//						//Adding to model true if the flag value is true
	//						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
	//					}
	//				}
	//				else
	//				{
	//					//error message for Fulfillment will go here
	//					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.NOT_TSHIP.toString());
	//				}
	//			}
	//			else
	//			{
	//				//error message for Blacklisted users will go here
	//				model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.BLACKLISTED.toString());
	//			}
	//		}
	//		catch (final NullPointerException e)
	//		{
	//			//logging error message
	//			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
	//		}
	//		catch (final EtailBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailBusinessExceptionHandler(e, null);
	//			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
	//		}
	//		catch (final EtailNonBusinessExceptions e)
	//		{
	//			ExceptionUtil.etailNonBusinessExceptionHandler(e);
	//			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
	//
	//		}
	//		catch (final Exception e)
	//		{
	//			//logging error message
	//			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
	//		}
	//	}



	/**
	 * This method is used to set up the details for COD using AJAX Call TISPT-235
	 *
	 * @param model
	 * @param guid
	 * @param request
	 * @return String
	 *
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.SETUPMPLCODFORM, method = RequestMethod.GET)
	@RequireHardLogIn
	private String setupMplCODForm(final Model model, final HttpServletRequest request, final String guid)
	{
		try
		{
			//COD form handled for both cart and order
			OrderModel orderModel = null;
			if (StringUtils.isNotEmpty(guid))
			{
				//final String orderGuid = decryptKey(guid);
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}

			//final String ip = getBlacklistByIPStatus(); TISPT-204 Point No 2
			final String ip = getMplPaymentFacade().getBlacklistByIPStatus(request);
			LOG.debug("The ip of the system is::::::::::::::::::::::::" + ip);

			//INC144316663

			final Long codUpperLimit = getBaseStoreService().getCurrentBaseStore().getCodUpperLimit();
			final Long codLowerLimit = getBaseStoreService().getCurrentBaseStore().getCodLowerLimit();

			if (null == orderModel)
			{
				//Existing code for cart
				//getting the session cart
				final CartModel cart = getCartService().getSessionCart();
				//UF-281/282 Starts
				if (!checkCODforCNC(cart))
				{
					LOG.debug("COD payment is not allwoed if an user selects CNC as payment mode");
					final String requestQueryParam = UriUtils.encodeQuery("?msg=" + "codNotallowed" + "&type=error", UTF);
					return FORWARD_PREFIX + "/checkout/single/message" + requestQueryParam;
				}
				//UF-281/282 End
				//to check customer is blacklisted or not against customer id, email, phone no. & ip address
				//TISEE-5555
				model.addAttribute(MarketplacecheckoutaddonConstants.CELLNO, getMplPaymentFacade().fetchPhoneNumber(cart));

				final boolean mplCustomerIsBlackListed = null != cart ? getMplPaymentFacade().isBlackListed(ip, cart) : true;

				if (null != cart && !mplCustomerIsBlackListed)
				{

					//Commented for TISPT-400
					//to check items are seller fulfilled or not
					//final List<String> fulfillmentDataList = new ArrayList<String>();
					//final List<String> paymentTypeList = new ArrayList<String>(); //TISPT-204
					//					for (final AbstractOrderEntryModel entry : cart.getEntries())
					//					{
					//						if (entry != null && entry.getSelectedUSSID() != null)
					//						{
					//							final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
					//									entry.getSelectedUSSID());
					//							//List<RichAttributeModel> richAttributeModel = null;
					//							//TISPT-400
					//							if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
					//							{
					//								final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) sellerInfoModel
					//										.getRichAttribute();
					//								if (richAttributeModel != null && richAttributeModel.get(0) != null
					//										&& richAttributeModel.get(0).getDeliveryFulfillModes() != null)
					//								{
					//									final String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
					//									if (DeliveryFulfillModesEnum.TSHIP.toString().equalsIgnoreCase(fulfillmentType))
					//									{
					//										//Start TISPT-204 Point No 1
					//										if (richAttributeModel.get(0).getPaymentModes() != null)
					//										{
					//											final PaymentModesEnum paymentMode = richAttributeModel.get(0).getPaymentModes();
					//											if (null != paymentMode)
					//											{
					//												if (PaymentModesEnum.COD.equals(paymentMode) || PaymentModesEnum.BOTH.equals(paymentMode))
					//												{
					//													if (null != cart.getIsCODEligible() && cart.getIsCODEligible().equals(Boolean.FALSE))
					//													{
					//														//Adding to model true if the pincode is serviceable
					//														model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
					//																CodCheckMessage.NOT_PINCODE_SERVICEABLE.toString());
					//														break;
					//													}
					//												}
					//												else
					//												{
					//													//Adding to model true if the flag value is true
					//													model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
					//															CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
					//													break;
					//												}
					//											}
					//											//End TISPT-204 Point No 1
					//											else
					//											{
					//												//Adding to model true if the flag value is true
					//												model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
					//														CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
					//												break;
					//											}
					//										}
					//										else
					//										{
					//											//Adding to model true if the flag value is true
					//											model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
					//													CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
					//											break;
					//										}
					//									}
					//									else
					//									{
					//										//error message for Fulfillment will go here
					//										model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
					//												CodCheckMessage.NOT_TSHIP.toString());
					//										break;
					//									}
					//								}
					//							}
					//						}
					//					}

					//INC144316663

					//final boolean isCodLimitFailed = (cart.getTotalPrice().longValue() <= codUpperLimit.longValue()) ? false : true;
					final boolean isCodLimitFailed = ((cart.getTotalPrice().longValue() <= codUpperLimit.longValue()) && (cart
							.getTotalPrice().longValue() >= codLowerLimit.longValue())) ? false : true;
					final boolean isCodEligible = (isCodLimitFailed || !cart.getIsCODEligible().booleanValue()) ? false : true;

					if (isCodEligible)
					{
						//For UF-277 message changed
						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_ELIGIBLE.toString());
						addDataForCODToModel(model, cart); //moved to single code for reuse
					}
					else
					{
						//For UF-277 message changed
						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
					}
				}
				else
				{
					//error message for Blacklisted users will go here
					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.BLACKLISTED.toString());
				}

				//Commented as code modulated above as part of TISPT-400

				//								fulfillmentDataList.add(fulfillmentType.toUpperCase());
				//							}
				//
				//							//Start TISPT-204 Point No 1
				//							if (richAttributeModel != null && richAttributeModel.get(0) != null
				//									&& richAttributeModel.get(0).getPaymentModes() != null)
				//							{
				//								final String paymentMode = richAttributeModel.get(0).getPaymentModes().toString();
				//								if (StringUtils.isNotEmpty(paymentMode))
				//								{
				//									//setting the payment mode in a list
				//									paymentTypeList.add(paymentMode);
				//								}
				//								//End TISPT-204 Point No 1
				//							}
				//						}
				//					}
				//				}
				//
				//				int flagForfulfillment = 0;
				//				//iterating through the fulfillment data list
				//				for (final String fulfillment : fulfillmentDataList)
				//				{
				//					if (!(com.tisl.mpl.core.enums.DeliveryFulfillModesEnum.TSHIP.toString().equalsIgnoreCase(fulfillment)))
				//					{
				//						flagForfulfillment = 0;
				//						break;
				//					}
				//					else
				//					{
				//						flagForfulfillment = 1;
				//					}
				//				}
				//
				//				if (flagForfulfillment == 1)
				//				{
				//					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.TSHIP.toString());
				//
				//					//item eligible for COD or not
				//					//final List<String> paymentTypeList = new ArrayList<String>(); TISPT-204
				//					// Code commented as part of TISPT-204 Point No 1 , paymentTypeList is populated in earlier for loop
				//					//iterating over all the cart entries
				//					//TISBOX-883
				//					//					for (final OrderEntryData entry : cartData.getEntries())
				//					//					{
				//					//						if (entry != null && entry.getSelectedUssid() != null)
				//					//						{
				//					//							final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
				//					//									.getSelectedUssid());
				//					//							List<RichAttributeModel> richAttributeModel = null;
				//					//							if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
				//					//							{
				//					//								richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
				//					//								if (richAttributeModel != null && richAttributeModel.get(0).getPaymentModes() != null)
				//					//								{
				//					//									final String paymentMode = richAttributeModel.get(0).getPaymentModes().toString();
				//					//									if (StringUtils.isNotEmpty(paymentMode))
				//					//									{
				//					//										//setting the payment mode in a list
				//					//										paymentTypeList.add(paymentMode);
				//					//									}
				//					//								}
				//					//							}
				//					//						}
				//					//
				//					//					}
				//
				//					//declaring a flag
				//					boolean codEligibilityFlag = false;
				//
				//					//iterating over the list of Payment types for all the cart entries
				//					for (final String paymentType : paymentTypeList)
				//					{
				//						if (PaymentModesEnum.COD.toString().equalsIgnoreCase(paymentType)
				//								|| PaymentModesEnum.BOTH.toString().equalsIgnoreCase(paymentType))
				//						{
				//							//flag set to true if the item's payment type is either COD or Both
				//							codEligibilityFlag = true;
				//						}
				//						else
				//						{ //flag set to false if the item's payment type is Prepaid
				//							codEligibilityFlag = false;
				//						}
				//					}
				//					if (codEligibilityFlag)
				//					{
				//						//Adding to model true if the flag value is true
				//						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_ELIGIBLE.toString());
				//
				//						//pincode serviceability check
				//						if (null != cart.getIsCODEligible() && cart.getIsCODEligible().booleanValue())
				//						{
				//							//Adding to model true if the pincode is serviceable
				//							model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
				//									CodCheckMessage.PINCODE_SERVICEABLE.toString());
				//						}
				//						else
				//						{
				//							//Adding to model true if the pincode is serviceable
				//							model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
				//									CodCheckMessage.NOT_PINCODE_SERVICEABLE.toString());
				//						}
				//					}
				//					else
				//					{
				//						//Adding to model true if the flag value is true
				//						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
				//					}
				//				}
				//				else
				//				{
				//					//error message for Fulfillment will go here
				//					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.NOT_TSHIP.toString());
				//				}
				//			else
				//			{
				//				//error message for Blacklisted users will go here
				//				model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.BLACKLISTED.toString());
				//			}
			}
			else
			{
				//Code for order TPR-629
				//TISEE-5555
				model.addAttribute(MarketplacecheckoutaddonConstants.CELLNO, getMplPaymentFacade().fetchPhoneNumber(orderModel));

				final boolean mplCustomerIsBlackListed = getMplPaymentFacade().isBlackListed(ip, orderModel);

				if (!mplCustomerIsBlackListed)
				{
					//adding blacklist status to model
					//INC144316663

					//model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.NOT_BLACKLISTED.toString());
					final boolean isCodLimitFailed = (orderModel.getTotalPrice().longValue() <= codUpperLimit.longValue()) ? false
							: true;
					final boolean isCodEligible = (isCodLimitFailed || !orderModel.getIsCODEligible().booleanValue()) ? false : true;

					if (isCodEligible)
					{
						//For UF-277 message changed
						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_ELIGIBLE.toString());
						addDataForCODToModel(model, orderModel);
					}
					else
					{
						//For UF-277 message changed
						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
					}
				}
				else
				{
					//error message for Blacklisted users will go here
					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.BLACKLISTED.toString());
				}

			}
			model.addAttribute(ModelAttributetConstants.CODRECAPTCHA_KEY, getCaptchaKey());
		}
		//		catch (final NullPointerException e)			//Nullpointer exception commented
		//		{
		//			//logging error message
		//			LOG.error(MarketplacecheckoutaddonConstants.B6004, e);
		//		}
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

		return MarketplacecheckoutaddonControllerConstants.Views.Fragments.Checkout.CODPanel;
	}




	/**
	 * TPR-627, TPR-622 Separate method the check COD Eligibility to avoid redundant code
	 *
	 * @param richAttributeModel
	 * @param cart
	 * @param model
	 * @return boolean
	 */
	private boolean paymentModecheckForCOD(final List<RichAttributeModel> richAttributeModel,
			final AbstractOrderModel abstractOrderModel, final Model model)
	{
		boolean breakFlag = true;
		//Start TISPT-204 Point No 1
		if (richAttributeModel.get(0).getPaymentModes() != null)
		{
			final PaymentModesEnum paymentMode = richAttributeModel.get(0).getPaymentModes();
			if (null != paymentMode)
			{
				if (PaymentModesEnum.COD.equals(paymentMode) || PaymentModesEnum.BOTH.equals(paymentMode))
				{
					if (null != abstractOrderModel.getIsCODEligible() && abstractOrderModel.getIsCODEligible().equals(Boolean.FALSE))
					{
						//Adding to model true if the pincode is serviceable
						model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE,
								CodCheckMessage.NOT_PINCODE_SERVICEABLE.toString());
						breakFlag = false;
					}
				}
				else
				{
					//Adding to model true if the flag value is true
					model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
					breakFlag = false;
				}
			}
			//End TISPT-204 Point No 1
			else
			{
				//Adding to model true if the flag value is true
				model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
				breakFlag = false;
			}
		}
		else
		{
			//Adding to model true if the flag value is true
			model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.ITEMS_NOT_ELIGIBLE.toString());
			breakFlag = false;
		}
		return breakFlag;
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

		//TISCR-421 : getting accountId to be sent to EBS
		model.addAttribute(MarketplacecheckoutaddonConstants.EBS_ACCOUNT_ID, getConfigurationService().getConfiguration()
				.getString(MarketplacecheckoutaddonConstants.EBS_ACCOUNT_ID_KEY));

		//Juspay-EBS session id
		final String sessionId = getMd5Encoding(getRandomAlphaNum(getConfigurationService().getConfiguration().getString(
				MarketplacecheckoutaddonConstants.EBS_SESSION_ID_KEY)));
		getSessionService().setAttribute(MarketplacecheckoutaddonConstants.EBS_SESSION_ID, sessionId);
		model.addAttribute(MarketplacecheckoutaddonConstants.EBS_SESSION_ID, sessionId);

		//getting the current customer to fetch customer Id and customer email
		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();

		//getting the list of stored cards
		Map<Date, SavedCardData> savedCreditCards = new TreeMap<Date, SavedCardData>();
		Map<Date, SavedCardData> savedDebitCards = new TreeMap<Date, SavedCardData>();
		final Collection<SavedCardModel> savedCardForCustomer = customer.getSavedCard(); //TISPT-204 Point no 5

		// Code addition start TISPT-204 Point no 4
		ListCardsResponse listCardsResponse = null;
		try
		{
			if (CollectionUtils.isNotEmpty(savedCardForCustomer)) // Code added for TISPT-204 Point no  5
			{
				listCardsResponse = getMplPaymentFacade().getJuspayCardResponse(customer);
				LOG.debug("*********************************************************************");
				LOG.debug("The Juspay Responce for Saved Card : " + listCardsResponse);
				final Tuple2<?, ?> storedSavedCards = getMplPaymentFacade().listStoredCards(customer, listCardsResponse);
				LOG.debug("*********************************************************************");

				LOG.debug("Stored Card" + storedSavedCards);

				savedCreditCards = (Map<Date, SavedCardData>) storedSavedCards.getFirst();
				savedDebitCards = (Map<Date, SavedCardData>) storedSavedCards.getSecond();
				LOG.debug("Credit Card" + savedCreditCards);
				LOG.debug("Debit Card" + savedDebitCards);
				LOG.debug("*********************************************************************");
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
		// Code addition end TISPT-204 Point no 4
		try
		{
			//savedCreditCards = getMplPaymentFacade().listStoredCreditCards(customer); Code commented for TISPT-204 Point no  4
			if (MapUtils.isNotEmpty(savedCreditCards))
			{
				//adding cards to model
				model.addAttribute(MarketplacecheckoutaddonConstants.CREDITCARDS, savedCreditCards);
			}
			//TISRLUAT-03 starts
			else if (MapUtils.isNotEmpty(savedDebitCards))
			{
				model.addAttribute(MarketplacecheckoutaddonConstants.CREDITCARDS, savedDebitCards);
			}
			//TISRLUAT-03 end
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
			//savedDebitCards = getMplPaymentFacade().listStoredDebitCards(customer); //Code commented for TISPT-204 Point no  4
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
	 * This method returns random alpha numeric number
	 *
	 * @param len
	 * @return String
	 */
	private String getRandomAlphaNum(final String len)
	{
		return RandomStringUtils.randomAlphanumeric(Integer.parseInt(len)).toUpperCase();
	}



	/**
	 * This method handles MD5 encoding
	 *
	 * @param input
	 * @return String
	 */
	private String getMd5Encoding(final String input)
	{
		MessageDigest messageDigest;
		String result = null;
		try
		{
			messageDigest = MessageDigest.getInstance(getConfigurationService().getConfiguration().getString(
					MarketplacecheckoutaddonConstants.JUSPAY_ENCODING_TYPE));
			messageDigest.reset();
			messageDigest.update(input.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			result = new String(Hex.encodeHex(resultByte));
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOG.error("Error while encoding=======");
		}

		return result;
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



	//	/**
	//	 * This method is used to get the IP address of the client and check whether it is blacklisted
	//	 *
	//	 * @return Boolean
	//	 *
	//	 */
	//  Code moved to facade layer TISPT-204 Point 2
	//	private String getBlacklistByIPStatus()
	//	{
	//		String ip = request.getHeader("X-Forwarded-For");
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("Proxy-Client-IP");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("WL-Proxy-Client-IP");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("HTTP_X_FORWARDED");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("HTTP_CLIENT_IP");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("HTTP_FORWARDED_FOR");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("HTTP_FORWARDED");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("HTTP_VIA");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getHeader("REMOTE_ADDR");
	//		}
	//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	//		{
	//			ip = request.getRemoteAddr();
	//		}
	//
	//		return ip;
	//	}


	/**
	 * This method is used to get the response from Juspay after processing card payment using iFrame(code added for
	 * order TPR-629)
	 *
	 * @param paymentForm
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundExceptionr
	 * @throws InvalidCartException
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CARDPAYMENT, method = RequestMethod.GET)
	@RequireHardLogIn
	public String cardPayment(final Model model, @Valid final PaymentForm paymentForm,
			final RedirectAttributes redirectAttributes, @PathVariable("guid") final String guid) throws CMSItemNotFoundException,
			InvalidCartException
	{
		//Commented as not needed for TPR-629
		//getting cartdata
		//		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		//		LOG.info(getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE));
		//		if (null != cartData)
		//		{
		//			//adding cartdata to model
		//			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
		//		}


		//Egv Order changes
		final CartModel cart = mplEGVCartService.getEGVCartModel(guid);
		if (cart != null && cart.getIsEGVCart().booleanValue())
		{
			try
			{
				return getEGVOrderStatus(model, redirectAttributes, guid);
			}
			catch (final CalculationException e)
			{
				LOG.error("Exception while creating EGV Child Order:: " + e.getMessage());
				mplEGVCartService.removeOldEGVCartCurrentCustomer();
				return MarketplacecheckoutaddonConstants.REDIRECT + GIFT_CARD
						+ getConfigurationService().getConfiguration().getString(MARKETPLACE_HEADER_EGV_PRODUCT_CODE);
			}

		}

		//Order Status from Juspay
		try
		{
			final Map<String, Boolean> duplicateJuspayResMap = getSessionService().getAttribute(
					MarketplacecommerceservicesConstants.DUPLICATEJUSPAYRESONSE);
			// OrderIssues:-  multiple Payment Response from juspay restriction
			if (MapUtils.isNotEmpty(duplicateJuspayResMap) && null != duplicateJuspayResMap.get(guid)
					&& !duplicateJuspayResMap.get(guid).booleanValue())
			{
				final OrderModel orderToBeUpdated = getMplPaymentFacade().getOrderByGuid(guid);
				if (null != orderToBeUpdated && null == orderToBeUpdated.getPaymentInfo()
						&& !OrderStatus.PAYMENT_TIMEOUT.equals(orderToBeUpdated.getStatus()))
				{
					final String orderStatusResponse = getMplPaymentFacade().getOrderStatusFromJuspay(guid, null, orderToBeUpdated,
							null);
					//Redirection when transaction is successful i.e. CHARGED
					if (null != orderStatusResponse)
					{

						/**
						 * Wallet Changes
						 */

						try
						{
							boolean qcFlag = false;
							if (orderToBeUpdated.getSplitModeInfo().equalsIgnoreCase("Split"))
							{
								if (MarketplacecheckoutaddonConstants.CHARGED.equalsIgnoreCase(orderStatusResponse))
								{
									final List<MplPaymentAuditModel> mplAudit = mplOrderDao.getAuditLists(orderToBeUpdated.getGuid());
									if (null != mplAudit)
									{
										for (final MplPaymentAuditModel mplPaymentAuditModel : mplAudit)
										{
											if (null != mplPaymentAuditModel && null != mplPaymentAuditModel.getAuditEntries())
											{
												for (final MplPaymentAuditEntryModel mplPaymentAuditEntry : mplPaymentAuditModel
														.getAuditEntries())
												{
													if (null != mplPaymentAuditEntry.getStatus()
															&& !mplPaymentAuditEntry.getStatus().toString().equalsIgnoreCase("DECLINED")) // case for EBS....
													{
														qcFlag = true;

													}
													else
													{
														qcFlag = false;
														break;
													}
												}
											}
										}

										if (qcFlag)
										{
											model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTID, null);
											setCheckoutStepLinksForModel(model, getCheckoutStep());
											QCRedeeptionResponse qcResponse = new QCRedeeptionResponse();
											try
											{

												final String qcUniqueCode = getMplPaymentFacade().generateQCCode();
												final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
												qcResponse = getMplPaymentFacade().createQCOrderRequest(orderToBeUpdated.getGuid(),
														orderToBeUpdated, currentCustomer.getCustomerWalletDetail().getWalletId(), "Cliq Cash",
														qcUniqueCode, "WEB", 0.0D, 0.0D);

												if (null != qcResponse && null != qcResponse.getResponseCode()
														&& qcResponse.getResponseCode().intValue() == 0)
												{
													return updateOrder(orderToBeUpdated, redirectAttributes);
												}
												else if (null != qcResponse && null != qcResponse.getResponseCode()
														&& qcResponse.getResponseCode().intValue() != 0)
												{

													orderToBeUpdated.setStatus(OrderStatus.RMS_VERIFICATION_FAILED); /// retuen for Juspay becsause qc fail
													getModelService().save(orderToBeUpdated);
													LOG.error("For GUID:- " + guid + " order failed Can not be placed Qlick Cash Error");
													GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
															MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
													return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
												}
												else if (null == qcResponse || null == qcResponse.getResponseCode())
												{
													orderToBeUpdated.setStatus(OrderStatus.RMS_VERIFICATION_FAILED); /// NO Exception No qcResponse Try With Juspay
													getModelService().save(orderToBeUpdated);

													LOG.error("For GUID:- " + guid + " order already been processed");
													GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
															MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
													return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
												}

											}
											catch (final Exception ex)
											{
												ex.printStackTrace();
												GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
														MarketplacecheckoutaddonConstants.TRANERRORMSG);
												if (null != qcResponse && null != qcResponse.getResponseCode()
														&& qcResponse.getResponseCode().intValue() == 0
														&& MarketplacecheckoutaddonConstants.CHARGED.equalsIgnoreCase(orderStatusResponse))
												{
													orderToBeUpdated.setStatus(OrderStatus.RMS_VERIFICATION_FAILED); // return for Juspay and Qc Retuen Trigger exception acccured
													getModelService().save(orderToBeUpdated);

													LOG.error("For GUID:- " + guid + " order already been processed");
													GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
															MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
													return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
												}
												else if (null != qcResponse && null != qcResponse.getResponseCode()
														&& qcResponse.getResponseCode().intValue() != 0
														&& MarketplacecheckoutaddonConstants.CHARGED.equalsIgnoreCase(orderStatusResponse))
												{

													orderToBeUpdated.setStatus(OrderStatus.RMS_VERIFICATION_FAILED);// return for Juspay only no dudection from QC
													getModelService().save(orderToBeUpdated); /////////////////////////////// need to update Aduit entries for Juspay and QC on condiction basices
													LOG.error("For GUID:- " + guid + " order already been processed");
													GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
															MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
													return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
												}
											}
										}
										else
										{
											System.out.println("PARTIAL OREDER JUSPAY FAIL *****************************");
											orderToBeUpdated.setStatus(OrderStatus.RMS_VERIFICATION_FAILED); //// need to discuess this case when ebs is DECLINED and juspay is chared what status to put
											LOG.error("For GUID:- " + guid + " order already been processed");
											GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
													MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
											return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
										}

									}
									else
									{
										System.out.println("PARTIAL OREDER JUSPAY FAIL *****************************");
										orderToBeUpdated.setStatus(OrderStatus.RMS_VERIFICATION_FAILED); //// need to discuess this case when ebs is DECLINED and juspay is chared what status to put

										LOG.error("For GUID:- " + guid + " order already been processed");
										GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
												MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
										return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
									}

								}
							}
						}
						catch (final Exception ex)
						{

							ex.printStackTrace();
						}

						/**
						 * Wallet Changes End
						 */


						if (MarketplacecheckoutaddonConstants.CHARGED.equalsIgnoreCase(orderStatusResponse))
						{
							model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTID, null);
							setCheckoutStepLinksForModel(model, getCheckoutStep());
							//return placeOrder(model, redirectAttributes);
							//Code commented and new code added for TPR-629
							return updateOrder(orderToBeUpdated, redirectAttributes);
						}
						else if (MarketplacecheckoutaddonConstants.JUSPAY_DECLINED.equalsIgnoreCase(orderStatusResponse))
						{
							GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
									MarketplacecheckoutaddonConstants.DECLINEDERRORMSG);
							return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
									+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
						}
						else if (MarketplacecheckoutaddonConstants.AUTHORIZATION_FAILED.equalsIgnoreCase(orderStatusResponse)
								|| MarketplacecheckoutaddonConstants.AUTHENTICATION_FAILED.equalsIgnoreCase(orderStatusResponse)
								|| MarketplacecheckoutaddonConstants.PENDING_VBV.equalsIgnoreCase(orderStatusResponse))
						{
							GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
									MarketplacecheckoutaddonConstants.VBVERRORMSG);
							//LOG.info("redirecting to::" + MarketplacecheckoutaddonConstants.REDIRECT
							//		+ MarketplacecheckoutaddonConstants.MPLPAYMENTURL + MarketplacecheckoutaddonConstants.PAYVALUE
							//		+ "  with redirect parameters::" + redirectAttributes.toString());
							return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
									+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
							//return MarketplacecheckoutaddonConstants.REDIRECT + safeRedirect(errorUrl);
							//httpResponse.sendRedirect(errorUrl);
						}
						else
						{
							GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
									MarketplacecheckoutaddonConstants.TRANERRORMSG);
							return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
									+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
							//httpResponse.sendRedirect(errorUrl);
						}
					}
					//Redirection when transaction is failed
					else
					{
						GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
								MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
						return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
								+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
					}
				}
				else
				{
					final String juspayOrderId = getSessionService()
							.getAttribute(MarketplacecommerceservicesConstants.JUSPAY_ORDER_ID);
					LOG.error("Payment Info already created for Order ID:- " + orderToBeUpdated.getCode());
					LOG.error("Juspay Order ID:- " + null != juspayOrderId ? juspayOrderId : "No ID avilable in session");
					return updateOrder(orderToBeUpdated, redirectAttributes);
				}
			}
			else
			{
				LOG.error("For GUID:- " + guid + " order already been processed");
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
				//return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
				//		+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("Exception in cardPayment", e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			//return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
			//	+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("Exception in cardPayment", e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
		}
		catch (final Exception e)
		{
			LOG.error("Exception in cardPayment", e);
			//Redirection when transaction is failed
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
		}
	}


	/**
	 * This method is used to set the shipping address when the field is checked true by the customer(code added as per
	 * TPR-629)
	 *
	 * @return String
	 * @throws InvalidCartException
	 *
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.SETSHIPPINGADDRESS, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String setShippingAddress(@RequestParam(MarketplacecheckoutaddonConstants.GUID) final String guid)
			throws InvalidCartException //Parameter guid added for handling orderModel
	{
		String concatAddress = MarketplacecommerceservicesConstants.EMPTY;

		try
		{
			//Code for both cart and order
			OrderModel orderModel = null;
			if (StringUtils.isNotEmpty(guid))
			{
				//final String orderGuid = decryptKey(guid);
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}

			if (null == orderModel)
			{
				//Existing code for cartModel
				//CartData cartData = new CartData(); //Blocked for PT fix

				//				if (null != getMplCustomAddressFacade().getCheckoutCart())
				//				{
				//					cartData = getMplCustomAddressFacade().getCheckoutCart();
				//				}
				//Getting the fields from delivery address

				final AddressData addressData = mplCustomAddressFacade.getDeliveryAddress();

				//if (null != cartData && cartData.getDeliveryAddress() != null)
				if (null != addressData)
				{
					//					final String firstName = cartData.getDeliveryAddress().getFirstName();
					//					final String lastName = cartData.getDeliveryAddress().getLastName();
					//					final String addressLine1 = cartData.getDeliveryAddress().getLine1();
					//					final String addressLine2 = cartData.getDeliveryAddress().getLine2();
					//					final String addressLine3 = cartData.getDeliveryAddress().getLine3();
					//					final String country = cartData.getDeliveryAddress().getCountry().getName();
					//					final String state = cartData.getDeliveryAddress().getState();
					//					final String city = cartData.getDeliveryAddress().getTown();
					//					final String pincode = cartData.getDeliveryAddress().getPostalCode();

					final String firstName = addressData.getFirstName();
					final String lastName = addressData.getLastName();
					final String addressLine1 = addressData.getLine1();
					final String addressLine2 = addressData.getLine2();
					final String addressLine3 = addressData.getLine3();
					final String country = addressData.getCountry().getName();
					final String state = addressData.getState();
					final String city = addressData.getTown();
					final String pincode = addressData.getPostalCode();

					final StringBuilder addressBuilder = new StringBuilder();

					addressBuilder.append(firstName).append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(lastName)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(addressLine1)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(addressLine2)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(addressLine3)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(country)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(state)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(city)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(pincode);

					//					concatAddress = firstName + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + lastName
					//							+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + addressLine1
					//							+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + addressLine2
					//							+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + addressLine3
					//							+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + country
					//							+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + state
					//							+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + city
					//							+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + pincode;
					concatAddress = addressBuilder.toString();
				}
			}
			//Code for orderModel
			else
			{
				final OrderData orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				if (null != orderData && orderData.getDeliveryAddress() != null)
				{
					final String firstName = orderData.getDeliveryAddress().getFirstName();
					final String lastName = orderData.getDeliveryAddress().getLastName();
					final String addressLine1 = orderData.getDeliveryAddress().getLine1();
					final String addressLine2 = orderData.getDeliveryAddress().getLine2();
					final String addressLine3 = orderData.getDeliveryAddress().getLine3();
					final String country = orderData.getDeliveryAddress().getCountry().getName();
					final String state = orderData.getDeliveryAddress().getState();
					final String city = orderData.getDeliveryAddress().getTown();
					final String pincode = orderData.getDeliveryAddress().getPostalCode();

					final StringBuilder addressBuilder = new StringBuilder();

					addressBuilder.append(firstName).append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(lastName)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(addressLine1)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(addressLine2)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(addressLine3)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(country)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(state)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(city)
							.append(MarketplacecheckoutaddonConstants.STRINGSEPARATOR).append(pincode);

					concatAddress = addressBuilder.toString();

				}
			}
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
	 * @param paymentMode
	 * @param guid
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
	public @ResponseBody MplPromoPriceData applyPromotions(final String paymentMode, final String bankName, final String guid,
			final boolean isNewCard) throws CMSItemNotFoundException, InvalidCartException, CalculationException,
			ModelSavingException, NumberFormatException, JaloInvalidParameterException, VoucherOperationException,
			JaloSecurityException, JaloPriceFactoryException //Parameters added for TPR-629
	{
		final long startTime = System.currentTimeMillis();
		LOG.debug("Entering Controller applyPromotions()=====" + System.currentTimeMillis());


		//Payment Soln changes
		OrderModel orderModel = null;
		MplPromoPriceData responseData = new MplPromoPriceData();
		String jsonResponse = MarketplacecommerceservicesConstants.EMPTY;
		CartData cartData = null;
		OrderData orderData = null;
		try
		{
			if (StringUtils.isNotEmpty(guid))
			{
				//final String orderGuid = decryptKey(guid);
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}

			if (null == orderModel)
			{
				//Existing code for cartModel
				final CartModel cart = getCartService().getSessionCart();
				//TISEE-510 ,TISEE-5555

				//				if (null != bankName && !bankName.equalsIgnoreCase("null"))
				//				{
				//					getMplPaymentFacade().setBankForSavedCard(bankName);
				//				}

				//TISPT-29
				if (null != cart
						&& StringUtils.isNotEmpty(paymentMode)
						&& (paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CREDITCARDMODE)
								|| paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.DEBITCARDMODE)
								|| paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.NETBANKINGMODE)
								|| paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.EMIMODE) || paymentMode
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.MRUPEE)))
				{
					//setting in cartmodel
					cart.setConvenienceCharges(Double.valueOf(0));
					//saving cartmodel
					getMplPaymentFacade().saveCart(cart);
				}

				if (!getMplCheckoutFacade().isPromotionValid(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
					//responseData.setErrorMsgForEMI("redirect");
					responseData.setPromoExpiryMsg(MarketplacecheckoutaddonConstants.REDIRECTSTRING);
				}
				else
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);

					//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode Restriction doesn't work
					//INC144318909 - EMI Option is not working in "Order threshold discount promotion"
					//INC144319439 - Tata eTail:Payment mode restricition not appliable on saved cards



					//SDI-2154/SDI-2155/SDI-2157 starts here

					/*
					 * if (getSessionService().getAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN) == null &&
					 * StringUtils.isNotEmpty(bankName) &&
					 * !bankName.equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL)) {
					 * getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN, bankName); }
					 */
					if (!isNewCard)
					{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN, bankName);
					}

					//SDI-2154/SDI-2155/SDI-2157 ends here

					//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode Restriction doesn't work
					//					if (StringUtils.isNotEmpty(paymentMode)
					//							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.NETBANKINGMODE))
					//					{
					//						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN, bankName);
					//						//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKNAMEFORNETBANKING, bankName);
					//					}

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

					cartData = getMplCartFacade().getCartDataFromCartModel(cart, false);
					responseData = getMplPaymentFacade().applyPromotions(cartData, null, cart, null, responseData);
					//					if (cart != null && cart.getEntries() != null && MapUtils.isNotEmpty(freebieModelMap))
					//					{
					//						for (final AbstractOrderEntryModel cartEntryModel : cart.getEntries())
					//						{
					//							if (cartEntryModel != null && cartEntryModel.getGiveAway().booleanValue()
					//									&& CollectionUtils.isNotEmpty(cartEntryModel.getAssociatedItems()))
					//							{
					//								mplCheckoutFacade.saveDeliveryMethForFreebie(cart, freebieModelMap, freebieParentQtyMap);
					//								//start populate deliveryPointOfService for freebie
					//								if (LOG.isDebugEnabled())
					//								{
					//									LOG.debug("***Before Populating deliveryPointOfService for freebie product has ussID "
					//											+ cartEntryModel.getSelectedUSSID());
					//								}
					//								PointOfServiceModel posModel = null;
					//								for (final AbstractOrderEntryModel cEntry : cart.getEntries())
					//								{
					//									if (cartEntryModel.getAssociatedItems().size() == 1)
					//									{
					//										if (cEntry.getSelectedUSSID().equalsIgnoreCase(cartEntryModel.getAssociatedItems().get(0)))
					//										{
					//											if (null != cEntry.getDeliveryPointOfService())
					//											{
					//												if (LOG.isDebugEnabled())
					//												{
					//													LOG.debug("Populating deliveryPointOfService for freebie from parent, parent ussid "
					//															+ cartEntryModel.getAssociatedItems().get(0));
					//												}
					//												posModel = cEntry.getDeliveryPointOfService();
					//											}
					//										}
					//									}
					//									else
					//									{
					//										final String parentUssId = findParentUssId(cartEntryModel, cart);
					//										if (cEntry.getSelectedUSSID().equalsIgnoreCase(parentUssId))
					//										{
					//											if (null != cEntry.getDeliveryPointOfService())
					//											{
					//												if (LOG.isDebugEnabled())
					//												{
					//													LOG.debug("Populating deliveryPointOfService for freebie from parent, parent ussid "
					//															+ parentUssId);
					//												}
					//												posModel = cEntry.getDeliveryPointOfService();
					//											}
					//										}
					//									}
					//								}
					//								if (null != posModel)
					//								{
					//									cartEntryModel.setDeliveryPointOfService(posModel);
					//									modelService.save(cartEntryModel);
					//								}
					//								if (LOG.isDebugEnabled())
					//								{
					//									LOG.debug("After Populating deliveryPointOfService for freebie product has ussID "
					//											+ cartEntryModel.getSelectedUSSID());
					//								}
					//								//end populate deliveryPointOfService for freebie
					//							}
					//						}
					//					}

					getMplPaymentFacade().populateDelvPOSForFreebie(cart, freebieModelMap, freebieParentQtyMap);

					//Wallet amount assigned. Will be changed after release1
					final double walletAmount = MarketplacecheckoutaddonConstants.WALLETAMOUNT;

					//setting the payment modes and the amount against it in session to be used later
					final Map<String, Double> paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(cart.getTotalPriceWithConv().doubleValue() - walletAmount));
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE, paymentInfo);

					//TISPRO-540 - Setting Payment mode in Cart
					if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CREDITCARDMODE))
					{
						cart.setModeOfPayment(MarketplacecheckoutaddonConstants.CREDITCARDMODE);
						getModelService().save(cart);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.DEBITCARDMODE))
					{
						cart.setModeOfPayment(MarketplacecheckoutaddonConstants.DEBITCARDMODE);
						getModelService().save(cart);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.NETBANKINGMODE))
					{
						cart.setModeOfPayment(MarketplacecheckoutaddonConstants.NETBANKINGMODE);
						getModelService().save(cart);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.EMIMODE))
					{
						cart.setModeOfPayment(MarketplacecheckoutaddonConstants.EMIMODE);
						getModelService().save(cart);
					}
					else if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase("COD"))
					{
						cart.setModeOfPayment("COD");
						getModelService().save(cart);
					}

					//Added for third party wallet
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.MRUPEE))
					{
						cart.setModeOfPayment(MarketplacecommerceservicesConstants.MRUPEE);
						getModelService().save(cart);
					}

					//TISST-7955
					final Map<String, String> ussidPricemap = new HashMap<String, String>();
					if (cartData != null)
					{
						for (final OrderEntryData entryData : cartData.getEntries())
						{
							ussidPricemap.put(entryData.getSelectedUssid() + "_" + entryData.isGiveAway(), entryData.getTotalPrice()
									.getFormattedValue());
						}
					}
					final ObjectMapper objectMapper = new ObjectMapper();
					jsonResponse = objectMapper.writeValueAsString(ussidPricemap);

					//UF-260
					GenericUtilityMethods.getCartPriceDetails(null, cart, responseData);
				}

			}
			else
			{
				//Added for INC144317089: Promotion is getting reapplied on order if user clicks on "back" button from order confirmation page.
				if (!OrderStatus.PAYMENT_SUCCESSFUL.equals(orderModel.getStatus())
						&& CollectionUtils.isEmpty(orderModel.getChildOrders()))
				{
					//Code for orderModel TPR-629
					if (null != bankName && !bankName.equalsIgnoreCase("null"))
					{
						getMplPaymentFacade().setBankForSavedCard(bankName);
					}

					//TISPT-29
					if (StringUtils.isNotEmpty(paymentMode)
							&& (paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CREDITCARDMODE)
									|| paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.DEBITCARDMODE)
									|| paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.NETBANKINGMODE)
									|| paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.EMIMODE) || paymentMode
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.MRUPEE)))
					{
						//setting in orderModel
						orderModel.setConvenienceCharges(Double.valueOf(0));
						getModelService().save(orderModel);
					}

					if (!getMplCheckoutFacade().isPromotionValid(orderModel))
					{
						responseData.setPromoExpiryMsg(MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT);
					}

					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODEFORPROMOTION, paymentMode);
					//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode Restriction doesn't work
					//INC144318909 - EMI Option is not working in "Order threshold discount promotion"
					//INC144319439 - Tata eTail:Payment mode restricition not appliable on saved cards


					//SDI-2154/SDI-2155/SDI-2157 changes starts here

					/*
					 * if (getSessionService().getAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN) == null &&
					 * StringUtils.isNotEmpty(bankName) &&
					 * !bankName.equalsIgnoreCase(MarketplacecommerceservicesConstants.NULL)) {
					 * getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN, bankName); }
					 */

					if (!isNewCard)
					{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN, bankName);
					}
					//SDI-2154/SDI-2155/SDI-2157 changes ends here


					//INC144317480: Order Threshold Discount Promotion: Netbanking Payment Mode Restriction doesn't work
					//					if (StringUtils.isNotEmpty(paymentMode)
					//							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.NETBANKINGMODE))
					//					{
					//						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKFROMBIN, bankName);
					//						//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.BANKNAMEFORNETBANKING, bankName);
					//					}

					final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
					final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
					if (orderModel.getEntries() != null)
					{
						for (final AbstractOrderEntryModel cartEntryModel : orderModel.getEntries())
						{
							if (cartEntryModel != null && cartEntryModel.getGiveAway() != null
									& !cartEntryModel.getGiveAway().booleanValue() && cartEntryModel.getSelectedUSSID() != null)
							{
								freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
								freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
							}
						}
					}

					orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
					responseData = getMplPaymentFacade().applyPromotions(null, orderData, null, orderModel, responseData);

					getMplPaymentFacade().populateDelvPOSForFreebie(orderModel, freebieModelMap, freebieParentQtyMap);

					//Wallet amount assigned. Will be changed after release1
					final double walletAmount = MarketplacecheckoutaddonConstants.WALLETAMOUNT;

					//setting the payment modes and the amount against it in session to be used later
					final Map<String, Double> paymentInfo = new HashMap<String, Double>();
					paymentInfo.put(paymentMode, Double.valueOf(orderModel.getTotalPriceWithConv().doubleValue() - walletAmount));
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODE, paymentInfo);

					//TISPRO-540 - Setting Payment mode in orderModel
					if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.CREDITCARDMODE))
					{
						orderModel.setModeOfOrderPayment(MarketplacecheckoutaddonConstants.CREDITCARDMODE);
						getModelService().save(orderModel);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.DEBITCARDMODE))
					{
						orderModel.setModeOfOrderPayment(MarketplacecheckoutaddonConstants.DEBITCARDMODE);
						getModelService().save(orderModel);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.NETBANKINGMODE))
					{
						orderModel.setModeOfOrderPayment(MarketplacecheckoutaddonConstants.NETBANKINGMODE);
						getModelService().save(orderModel);
					}
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecheckoutaddonConstants.EMIMODE))
					{
						orderModel.setModeOfOrderPayment(MarketplacecheckoutaddonConstants.EMIMODE);
						getModelService().save(orderModel);
					}
					else if (StringUtils.isNotEmpty(paymentMode) && paymentMode.equalsIgnoreCase("COD"))
					{
						orderModel.setModeOfOrderPayment("COD");
						getModelService().save(orderModel);
					}

					//Added for third party wallet
					else if (StringUtils.isNotEmpty(paymentMode)
							&& paymentMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.MRUPEE))
					{
						orderModel.setModeOfOrderPayment(MarketplacecommerceservicesConstants.MRUPEE);
						getModelService().save(orderModel);
					}

					//TISST-7955
					final Map<String, String> ussidPricemap = new HashMap<String, String>();
					if (orderData != null)
					{
						for (final OrderEntryData entryData : orderData.getEntries())
						{
							ussidPricemap.put(entryData.getSelectedUssid() + "_" + entryData.isGiveAway(), entryData.getTotalPrice()
									.getFormattedValue());
						}
					}
					final ObjectMapper objectMapper = new ObjectMapper();
					jsonResponse = objectMapper.writeValueAsString(ussidPricemap);

					//UF-260
					GenericUtilityMethods.getCartPriceDetails(null, orderModel, responseData);
				}
			}
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
		catch (final ModelSavingException e)
		{
			LOG.error("Error while saving model in applyPromotion ", e);
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
		//getting current user
		final String mplCustomerID = (null == getUserService().getCurrentUser().getUid()) ? "" : getUserService().getCurrentUser()
				.getUid();
		final String cardType = request.getParameter("cardType");
		try
		{
			//Code Change for TISPRO-175
			if (StringUtils.isNotEmpty(binNumber) && !cardType.equals(""))
			{
				//binData = getBinFacade().binCheck(binNumber);
				//Added For TPR-1035
				binData = getBinFacade().binCheck(binNumber, cardType, mplCustomerID, true);
				//Added for TPR-4461 starts here for voucher
				LOG.debug("Inside bincheck::::::The bank name to be set while selecting the card for payment is "
						+ binData.getBankName());
				//setBankNameUserPaymentMode(binData.getBankName());
				//TPR-4461 ends here
			}

		}
		//		catch (final NullPointerException e)
		//		{
		//			LOG.error("Exception in binCheck", e);
		//		}
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
		//		catch (final NullPointerException e)
		//		{
		//			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);		//Nullpointer exception commented
		//		}
		catch (final AdapterException e)
		{
			LOG.error("AdapterException in submitNBForm", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception in submitNBForm", e);
		}

		return netbankingResponse;
	}





	/**
	 * This method creates juspay order. TPR-629 incorporated
	 *
	 * @param firstName
	 * @param lastName
	 * @param addressLine1
	 * @param addressLine2
	 * @param addressLine3
	 * @param country
	 * @param state
	 * @param city
	 * @param pincode
	 * @param cardSaved
	 * @param sameAsShipping
	 * @param guid
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CREATEJUSPAYORDER, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String createJuspayOrder(final String firstName, final String lastName, final String netBankName,
			final String addressLine1, final String addressLine2, final String addressLine3, final String country,
			final String state, final String city, final String pincode, final String cardSaved, final String sameAsShipping,
			final String guid, final Model model, final boolean isEGVOrder) //Parameter guid added for TPR-629 //parameter netBankName added for TPR-4461
			throws EtailNonBusinessExceptions
	{
		//TPR-4461 parameter netBankName added starts here added only for getting bank name for netbanking/saved credit card/saved debit card
		LOG.debug("The bank name for netbanking is" + netBankName);
		//		if (StringUtils.isNotEmpty(netBankName) && netBankName != null)
		//		{
		//			setBankNameUserPaymentMode(netBankName);//Card's Bank List
		//		}

		//TPR-4461 parameter netBankName added ends here added only for netbanking bankname
		String orderId = null;
		try
		{
			final StringBuilder returnUrlBuilder = new StringBuilder();
			returnUrlBuilder
					.append(request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8)))
					.append(request.getContextPath())
					.append(
							getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYRETURNMETHOD));
			if (StringUtils.isNotEmpty(guid))
			{
				returnUrlBuilder.append("/").append(guid);
			}
			String paymentAddressLine1 = "";
			String paymentAddressLine2 = "";
			String paymentAddressLine3 = "";

			if (StringUtils.isNotEmpty(addressLine1))
			{
				paymentAddressLine1 = java.net.URLDecoder.decode(addressLine1, UTF);
			}
			if (StringUtils.isNotEmpty(addressLine2))
			{
				paymentAddressLine2 = java.net.URLDecoder.decode(addressLine2, UTF);
			}
			if (StringUtils.isNotEmpty(addressLine3))
			{
				paymentAddressLine3 = java.net.URLDecoder.decode(addressLine3, UTF);
			}
			//final String paymentAddressLine1 = java.net.URLDecoder.decode(addressLine1, UTF);
			//final String paymentAddressLine2 = java.net.URLDecoder.decode(addressLine2, UTF);
			//final String paymentAddressLine3 = java.net.URLDecoder.decode(addressLine3, UTF);

			final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
			String uid = "";
			if (null != customer)
			{
				uid = customer.getUid();
			}

			if (!isEGVOrder)
			{
				mplEGVCartService.removeOldEGVCartCurrentCustomer();
			}

			//Payment Soln changes	TPR-629
			OrderModel orderModel = null;
			//added for CAR:127
			OrderData oData = null;
			//added for CAR:127
			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}

			if (orderModel == null)
			{
				//Existing code for catModel
				//Existing code for catModel
				CartModel cart;
				if (isEGVOrder)
				{
					try
					{
						orderId = createJuspayOrderForEGV(firstName, lastName, country, state, city, pincode, cardSaved, sameAsShipping,
								guid, orderId, returnUrlBuilder, paymentAddressLine1, paymentAddressLine2, paymentAddressLine3, uid);
					}
					catch (final ModelSavingException e)
					{
						LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
					}

					return orderId;
				}
				else
				{
					cart = getCartService().getSessionCart();
				}

				//TPR-4461 Starts here for payment mode and bank restriction validation for Voucher
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService()
						.getAppliedVouchers(cart));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount != null && discount instanceof PromotionVoucherModel)//null check added for discount as per IQA review
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;

								final String paymentModeCard = cart.getModeOfPayment();//Card Payment Mode

								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode
								final List<BankModel> bankLists = ((PaymentModeRestrictionModel) restriction).getBanks(); //Voucher Bank Restriction List
								LOG.debug("Inside createjuspay order method ::cart model:: TISSTRT-1526-1  : the banklist set in voucher is: "
										+ bankLists);

								//final String banknameforUserPaymentMode = getBankNameUserPaymentMode(); // Bank of User's Payment Mode
								final String banknameforUserPaymentMode = getSessionService().getAttribute(
										MarketplacecheckoutaddonConstants.BANKFROMBIN);

								LOG.debug("Inside createjuspay order method ::cart model:: TISSTRT-1526-2  : the bank selected while paying through card is: "
										+ banknameforUserPaymentMode);

								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												if (CollectionUtils.isEmpty(bankLists))
												{
													willApply = true;
												}
												else
												{
													LOG.debug("Inside createjuspay order method ::cart model::before sending for bin validation:: TISSTRT-1526-3  : the parameters passed for bank validation are: "
															+ " banklist set in voucher: "
															+ bankLists
															+ " card bank: "
															+ banknameforUserPaymentMode);
													willApply = getMplPaymentFacade().validateBank(bankLists, banknameforUserPaymentMode);
												}
												break;

											}

										}
									}
									else
									{
										willApply = true;
									}
								}

								if (willApply == false)
								{
									return MarketplacecheckoutaddonConstants.REDIRECTTOCOUPON;
								}
							}

						}
					}
				}
				//TPR-4461 Ends here for payment mode and bank restriction validation for Voucher


				//added for CAR:127
				final CartData cData = getMplCartFacade().getCartDataFromCartModel(cart, false);
				//added for CAR:127

				//final Double cartTotals = cart.getTotalPriceWithConv();
				boolean redirectFlag = false;
				//final String returnUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8))
				//		+ request.getContextPath()
				//		+ getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYRETURNMETHOD);
				//try
				//{
				//TISPRD-3025 - LOG to handle address --- moved to facade layer

				//				final StringBuilder sb = new StringBuilder();
				//				sb.append("firstName:::").append(firstName).append("|lastName:::").append(lastName).append("|addressLine1:::")
				//						.append(paymentAddressLine1).append("|addressLine2:::").append(paymentAddressLine2).append("|addressLine3:::")
				//						.append(paymentAddressLine3).append("|country:::").append(country).append("|state:::").append(state)
				//						.append("|city:::").append(city).append("|pincode:::").append(pincode).append("|cardSaved:::")
				//						.append(cardSaved).append("|sameAsShipping:::").append(sameAsShipping).append("|cartGUID:::").append(cartGuid);
				//
				//				LOG.error("Address details entered >>>" + sb.toString());
				//Address log code moved to facade to handle both web and mobile

				LOG.debug(" TIS-414  : Checking - onclick of pay now button pincode servicabilty and promotion");
				if (!redirectFlag && !getMplCheckoutFacade().isPromotionValid(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
					redirectFlag = true;
					LOG.info("::setting redirect flag--1::");
				}
				//TISST-13012
				//				final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cart);
				//				if (!redirectFlag && cartItemDelistedStatus)
				//				{
				//					redirectFlag = true;
				//				}

				if (!redirectFlag)
				{
					final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cart);
					if (cartItemDelistedStatus)
					{
						redirectFlag = true;
						LOG.info("::setting redirect flag--2::");
					}
				}


				//TISUTO-12 , TISUTO-11
				if (!redirectFlag)
				{

					//TPR3780 STARTS HERE
					//	final double prevTotalCartPrice = cart.getTotalPrice().doubleValue();
					//TPR3780 ENDS HERE

					//commented for CAR:127
					/*
					 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
					 */

					final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(

					//MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cData, cart);

					//TPR3780 STARTS HERE

					Boolean replaced = Boolean.FALSE;
					replaced = getSessionService().getAttribute("replacedUssid");
					//	final CartModel cartModelAfterinventoryCheck = getCartService().getSessionCart();
					//	final double newTotalCartPrice = cartModelAfterinventoryCheck.getTotalPrice().doubleValue();


					if (null != replaced && replaced.booleanValue())
					{
						//return MarketplacecheckoutaddonConstants.INVENTORYRESERVED;
						final String updateStatus = MarketplacecheckoutaddonConstants.UPDATED;
						getSessionService().removeAttribute("replacedUssid");
						redirectFlag = true;
						LOG.info("::setting redirect flag--inv reservation changed for new item::");
						getSessionService().setAttribute("flashupdateStatus", updateStatus);
					}

					//TPR3780 ENDS HERE


					if (!inventoryReservationStatus)
					{

						getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
						redirectFlag = true;
						LOG.info("::setting redirect flag--3::");
					}
				}

				if (!redirectFlag && !getMplCheckoutFacade().isCouponValid(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
					redirectFlag = true;
					LOG.info("::setting redirect flag--4::");
				}

				//TISPRO-497
				//				final Double cartTotal = cart.getTotalPrice();
				//				final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();
				//
				//				if (!redirectFlag && cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
				//				{
				//					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
				//					redirectFlag = true;
				//				}
				if (!redirectFlag)
				{
					final Double cartTotal = cart.getTotalPrice();
					final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();

					LOG.info("::cartTotal**::" + cartTotal);
					LOG.info("::cartTotalWithConvCharge**::" + cartTotalWithConvCharge);
					if (cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
					{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
						redirectFlag = true;
						LOG.info("::setting redirect flag--5::");
					}
				}

				//TISPRO-578
				if (!redirectFlag && !getMplPaymentFacade().isValidCart(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID, "TRUE");
					redirectFlag = true;
					LOG.info("::setting redirect flag--6::");
				}

				if (redirectFlag)
				{
					LOG.info("::returning redirect String::");
					return MarketplacecheckoutaddonConstants.REDIRECTSTRING; //IQA for TPR-629
				}
				else
				{
					/**
					 * Wallet Changes
					 */


					final Double totalCartVal = cart.getTotalPrice();

					/**
					 * Wallet Changes END
					 */

					LOG.info("::Going to Create Juspay OrderId::");
					orderId = getMplPaymentFacade().createJuspayOrder(cart, null, firstName, lastName, paymentAddressLine1,
							paymentAddressLine2, paymentAddressLine3, country, state, city, pincode,
							cardSaved + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + sameAsShipping, returnUrlBuilder.toString(),
							uid, MarketplacecheckoutaddonConstants.CHANNEL_WEB, 0.0D);

					//orderId = "987654321";

					LOG.info("::Created Juspay OrderId::" + orderId);

					//create order here --- order will be created during first try TPR-629

					//Mandatory checks agains cart
					final boolean isValidCart = getMplPaymentFacade().checkCart(cart);
					if (isValidCart)
					{
						/**
						 * Change for Wallet
						 */
						if (cart.getSplitModeInfo().equalsIgnoreCase("Split"))
						{
							cart.setTotalPrice(totalCartVal);
						}
						/**
						 * Change for Wallet end
						 */

						//final OrderData orderData = getMplCheckoutFacade().placeOrder();
						getMplCheckoutFacade().placeOrder();
					}
					else
					{
						throw new InvalidCartException("************PaymentMethodCheckoutStepController : placeOrder : Invalid Cart!!!"
								+ (StringUtils.isNotEmpty(cart.getGuid()) ? cart.getGuid() : MarketplacecommerceservicesConstants.EMPTY));
					}

				}

			}
			else
			{
				//TPR-4461 Starts here for payment mode and bank restriction validation for Voucher
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService().getAppliedVouchers(
						orderModel));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount != null && discount instanceof PromotionVoucherModel)//null check added for discount as per IQA review
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;

								final String paymentModeCard = orderModel.getModeOfOrderPayment();//Card Payment Mode

								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode
								final List<BankModel> bankLists = ((PaymentModeRestrictionModel) restriction).getBanks(); //Voucher Bank Restriction List
								//final String banknameforUserPaymentMode = getBankNameUserPaymentMode(); // Bank of User's Payment Mode
								final String banknameforUserPaymentMode = getSessionService().getAttribute(
										MarketplacecheckoutaddonConstants.BANKFROMBIN);


								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												if (CollectionUtils.isEmpty(bankLists))
												{
													willApply = true;
												}
												else
												{
													willApply = getMplPaymentFacade().validateBank(bankLists, banknameforUserPaymentMode);

												}
												break;
											}


										}
									}
									else
									{
										willApply = true;
									}
								}


								if (willApply == false)
								{
									return MarketplacecheckoutaddonConstants.REDIRECTTOCOUPON;
								}
							}

						}
					}
				}
				//TPR-4461 Ends here for payment mode and bank restriction validation for Voucher

				//added for CAR:127
				oData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				//added for CAR:127
				if (null == orderModel.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(orderModel.getStatus()))
				{
					//Code for orderModel --- do not create order again TPR-629
					boolean redirectFlag = false;
					if (!getMplCheckoutFacade().isPromotionValid(orderModel))
					{
						getMplCartFacade().recalculateOrder(orderModel);
						redirectFlag = true;
					}

					if (!redirectFlag)
					{
						//commmented for CAR:127
						/*
						 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
						 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel);
						 */
						final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
								MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, oData, orderModel);

						if (!inventoryReservationStatus)
						{
							//TPR-815
							getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_ORDER_INVENTORY_RESV_SESSION_ID,
									"TRUE");
							getMplCartFacade().recalculateOrder(orderModel);
							redirectFlag = true;
							//notify EMAil SMS TPR-815
							mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
						}
					}
					if (redirectFlag)
					{
						return MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT; //IQA for TPR-629
					}
					else
					{
						orderId = getMplPaymentFacade().createJuspayOrder(null, orderModel, firstName, lastName, paymentAddressLine1,
								paymentAddressLine2, paymentAddressLine3, country, state, city, pincode,
								cardSaved + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + sameAsShipping,
								returnUrlBuilder.toString(), uid, MarketplacecheckoutaddonConstants.CHANNEL_WEB, 0.0D);
					}
				}
				else if (null != orderModel.getPaymentInfo())
				{
					LOG.error("Order already has payment info >>>" + orderModel.getPaymentInfo().getCode());
					return "redirect_with_details";
				}
				else
				{
					LOG.error("Order status is Payment_Pending for orderCode>>>" + orderModel.getCode());
					return "redirect_with_details";
				}
			}

			orderId = orderId + "|" + guid;
		}
		//		catch (final NullPointerException e)
		//		{
		//			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);		//Nullpointer commented
		//		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}
		catch (final AdapterException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			orderId = "JUSPAY_CONN_ERROR";
			//to be check
			//return MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			orderId = "NONBusinessException" + e.getErrorMessage();
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

		/////////////////TPR-4461///////////////////

		//getSessionService().setAttribute(MarketplacecommerceservicesConstants.BANKFROMBINFORPROMOTION,
		//	(null != bankName && StringUtils.isNotEmpty(bankName)) ? bankName : null);//selected EMI BANK setting for voucher payment mode and bank specific restriction
		//		if (null != bankName && StringUtils.isNotEmpty(bankName))
		//		{
		//			setBankNameUserPaymentMode(bankName);
		//		}
		/////////////////TPR-4461///////////////////

		final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
		final Map<Date, SavedCardData> listStoredEMICards = getMplPaymentFacade().listStoredEMICards(customer, bankName);
		return listStoredEMICards;
	}





	/**
	 * This method saves delivery mode for freebie items --- private method so that it can be used multiple TPR-629
	 *
	 * @param cartEntryModel
	 * @param freebieModelMap
	 * @param freebieParentQtyMap
	 */
	private void saveDeliveryMethForFreebie(final AbstractOrderEntryModel cartEntryModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
	{
		try
		{
			// final String ussId = cartEntryModel.getAssociatedItems().get(0);
			//final MplZoneDeliveryModeValueModel mplDeliveryMode = freebieModelMap.get(ussId);
			MplZoneDeliveryModeValueModel mplDeliveryMode = null;
			if (null != cartEntryModel && CollectionUtils.isNotEmpty(cartEntryModel.getAssociatedItems())) //IQA for TPR-629
			{
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

					else if (freebieParentQtyMap.get(cartEntryModel.getAssociatedItems().get(0)).doubleValue() > freebieParentQtyMap
							.get(cartEntryModel.getAssociatedItems().get(1)).doubleValue())
					{

						mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));

					}
					else
					{
						mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1));
					}

					if (freebieParentQtyMap.get(cartEntryModel.getAssociatedItems().get(0)).doubleValue() == freebieParentQtyMap.get(
							cartEntryModel.getAssociatedItems().get(1)).doubleValue()
							&& ("home-delivery").equalsIgnoreCase(freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0))
									.getDeliveryMode().getCode()))
					{
						mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
					}
					else
					{
						mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1));
					}

				}
			}

			if (mplDeliveryMode != null)
			{
				//saving parent product delivery mode to freebie item
				cartEntryModel.setMplDeliveryMode(mplDeliveryMode);
				modelService.save(cartEntryModel);
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
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
	 * This method updates already created order as per new Payment Soln - Order before Payment TPR-629
	 *
	 * @param orderToBeUpdated
	 * @return String
	 * @throws InvalidCartException
	 * @throws CalculationException
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	private String updateOrder(final OrderModel orderToBeUpdated, final RedirectAttributes redirectAttributes)
			throws InvalidCartException, CalculationException, EtailNonBusinessExceptions
	{
		LOG.debug("========================Inside Update Order============================");
		try
		{
			if (null != orderToBeUpdated && null != orderToBeUpdated.getPaymentInfo()
					&& CollectionUtils.isEmpty(orderToBeUpdated.getChildOrders()))
			{
				getMplCheckoutFacade().beforeSubmitOrder(orderToBeUpdated);
				getMplCheckoutFacade().submitOrder(orderToBeUpdated);

				//order confirmation email and sms
				getNotificationFacade().sendOrderConfirmationNotification(orderToBeUpdated);

				final OrderData orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderToBeUpdated);

				return redirectToOrderConfirmationPage(orderData);
			}
			else if (null != orderToBeUpdated && null != orderToBeUpdated.getPaymentInfo()
					&& CollectionUtils.isNotEmpty(orderToBeUpdated.getChildOrders()))
			{
				final OrderData orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderToBeUpdated);
				return redirectToOrderConfirmationPage(orderData);
			}
			else if (null != orderToBeUpdated && null == orderToBeUpdated.getPaymentInfo()
					&& OrderStatus.PAYMENT_TIMEOUT.equals(orderToBeUpdated.getStatus()))
			{
				LOG.error("Issue with update order...redirecting to payment page only");
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.PAYMENTIMEOUTRRORMSG);
				return getCheckoutStep().currentStep();
			}
			else
			{
				LOG.error("Issue with update order...redirecting to payment page only");
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);


				if (orderToBeUpdated.getIsEGVCart().booleanValue())
				{
					mplEGVCartService.removeOldEGVCartCurrentCustomer();
					return MarketplacecheckoutaddonConstants.REDIRECT + GIFT_CARD
							+ getConfigurationService().getConfiguration().getString(MARKETPLACE_HEADER_EGV_PRODUCT_CODE);
				}

				return getCheckoutStep().currentStep();
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



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
			//TISPRO-540
			final CartModel cart = getCartService().getSessionCart();

			//Mandatory checks agains cart
			final boolean isValidCart = getMplPaymentFacade().checkCart(cart);

			if (isValidCart)
			{
				orderData = getMplCheckoutFacade().placeOrder();
			}
			else
			{
				throw new InvalidCartException("************PaymentMethodCheckoutStepController : placeOrder : Invalid Cart!!!"
						+ (cart != null && StringUtils.isNotEmpty(cart.getGuid()) ? cart.getGuid()
								: MarketplacecommerceservicesConstants.EMPTY));
			}

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

	//	/**
	//	 * This methods find delivery mode for freebie if it has more than one parents.
	//	 *
	//	 * @param entryModel
	//	 * @param abstractOrderModel
	//	 * @return delivery mode
	//	 */
	//	private String findParentUssId(final AbstractOrderEntryModel entryModel, final AbstractOrderModel abstractOrderModel)
	//	{
	//		final Long ussIdA = getQuantity(entryModel.getAssociatedItems().get(0), abstractOrderModel);
	//		final Long ussIdB = getQuantity(entryModel.getAssociatedItems().get(1), abstractOrderModel);
	//		final String ussIdADelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(0), abstractOrderModel);
	//		final String ussIdBDelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(1), abstractOrderModel);
	//		String deliveryMode = null;
	//		if (ussIdA.doubleValue() < ussIdB.doubleValue())
	//		{
	//			deliveryMode = entryModel.getAssociatedItems().get(0);
	//		}
	//		else
	//		{
	//			deliveryMode = entryModel.getAssociatedItems().get(1);
	//		}
	//		if (ussIdA.doubleValue() == ussIdB.doubleValue()
	//				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)
	//				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
	//		{
	//			deliveryMode = entryModel.getAssociatedItems().get(0);
	//		}
	//		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
	//				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)
	//				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
	//		{
	//			deliveryMode = entryModel.getAssociatedItems().get(0);
	//		}
	//		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
	//				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)
	//				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
	//		{
	//			deliveryMode = entryModel.getAssociatedItems().get(1);
	//		}
	//		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
	//				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)
	//				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
	//		{
	//			deliveryMode = entryModel.getAssociatedItems().get(0);
	//		}
	//		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
	//				&& ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)
	//				&& ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
	//		{
	//			deliveryMode = entryModel.getAssociatedItems().get(1);
	//		}
	//		return deliveryMode;
	//	}

	//	/**
	//	 * Finds delivery mode for freebie.
	//	 *
	//	 * @param ussid
	//	 * @param abstractOrderModel
	//	 * @return delivery mode
	//	 */
	//	private String getDeliverModeForABgetC(final String ussid, final AbstractOrderModel abstractOrderModel)
	//	{
	//		String deliveryMode = null;
	//		for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
	//		{
	//			if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
	//			{
	//				deliveryMode = cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
	//			}
	//
	//		}
	//
	//		return deliveryMode;
	//	}
	//
	//	/**
	//	 * @param ussid
	//	 * @param abstractOrderModel
	//	 * @return Long
	//	 */
	//	private Long getQuantity(final String ussid, final AbstractOrderModel abstractOrderModel)
	//	{
	//		Long qty = null;
	//		for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
	//		{
	//			if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
	//			{
	//				qty = cartEntry.getQuantity();
	//				cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
	//			}
	//
	//		}
	//
	//		return qty;
	//	}

	/**
	 * To get the checkout step
	 *
	 * @return CheckoutStep
	 */
	private CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(MarketplacecheckoutaddonConstants.PAYMENT_METHOD);
	}

	/**
	 * @param request
	 * @return boolean This method checks whether the session is active
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHECKSESSIONACTIVE, method = RequestMethod.GET)
	@ResponseBody
	private boolean checkSessionActive(final HttpServletRequest request)
	{
		return GenericUtilityMethods.checkSessionActive(request);
	}




	/**
	 * This method populates model for COD moved to a single method TPR-629 Logs added as IQA
	 *
	 * @param model
	 * @param abstractOrder
	 */
	private void addDataForCODToModel(final Model model, final AbstractOrderModel abstractOrder)
	{
		if (null != abstractOrder) //IQA for TPR-629
		{
			for (final AbstractOrderEntryModel entry : abstractOrder.getEntries())
			{
				if (entry != null && entry.getSelectedUSSID() != null)
				{
					SellerInformationModel sellerInfoModel = null;

					//TPR-6959:Change in COD eligibility logic for fine jewellery category
					if (StringUtils.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY, entry.getProduct()
							.getProductCategoryType()))
					{
						final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(entry
								.getSelectedUSSID());
						if (CollectionUtils.isNotEmpty(jewelleryInfo))
						{
							if (StringUtils.isNotEmpty(jewelleryInfo.get(0).getPCMUSSID()))
							{
								sellerInfoModel = getMplSellerInformationService().getSellerDetail(jewelleryInfo.get(0).getPCMUSSID());
							}
							else
							{
								LOG.error("PCMUSSID is empty for Vussid : " + entry.getSelectedUSSID());
							}
						}
						else
						{
							LOG.error("JewelleryInformationModel is empty for Vussid : " + entry.getSelectedUSSID());
						}
					}
					else
					{
						sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUSSID());
					}
					//List<RichAttributeModel> richAttributeModel = null;
					//TISPT-400
					if (sellerInfoModel != null && CollectionUtils.isNotEmpty(sellerInfoModel.getRichAttribute()))
					{
						final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) sellerInfoModel
								.getRichAttribute();
						if (richAttributeModel != null && richAttributeModel.get(0) != null
								&& richAttributeModel.get(0).getDeliveryFulfillModes() != null)
						{
							String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
							/* Added in R2.3 for START */
							if (DeliveryFulfillModesEnum.BOTH.toString().equalsIgnoreCase(fulfillmentType))
							{
								if (null != entry.getFulfillmentType())
								{
									fulfillmentType = entry.getFulfillmentType();
								}
								else if (null != entry.getFulfillmentTypeP1())
								{
									fulfillmentType = entry.getFulfillmentTypeP1();
								}
								else if (null != richAttributeModel.get(0).getDeliveryFulfillModeByP1()
										&& null != richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode())
								{
									fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode();
								}
							}
							/* Added in R2.3 END */
							if (DeliveryFulfillModesEnum.TSHIP.toString().equalsIgnoreCase(fulfillmentType))
							{
								LOG.debug("Entry is TSHIP");
								final boolean returnFlag = paymentModecheckForCOD(richAttributeModel, abstractOrder, model);
								if (!returnFlag)
								{
									break;
								}
							}
							else
							{
								LOG.debug("At least one entry is SSHIP");
								//TPR-627, TPR-622
								//Changes to TRUE & FALSE
								final String isSshipCodEligble = (richAttributeModel.get(0).getIsSshipCodEligible() != null ? richAttributeModel
										.get(0).getIsSshipCodEligible().getCode()
										: MarketplacecheckoutaddonConstants.FALSE);
								// isSshipCodEligble to enable disable COD Eligible for SSHIP Products
								//Changes to TRUE & FALSE
								if (StringUtils.isNotEmpty(isSshipCodEligble)
										&& isSshipCodEligble.equalsIgnoreCase(MarketplacecheckoutaddonConstants.TRUE))
								{
									//TPR-627,TPR-622 Separate method the check COD Eligibility to avoid redundant code
									final boolean returnFlag = paymentModecheckForCOD(richAttributeModel, abstractOrder, model);
									if (!returnFlag)
									{
										break;
									}
								}
								else
								{
									//error message for Fulfillment will go here
									model.addAttribute(MarketplacecheckoutaddonConstants.CODELIGIBLE, CodCheckMessage.NOT_TSHIP.toString());
									break;
								}
							}
						}
					}
				}
			}
		}

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
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}


	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}






	/**
	 * @return the notificationFacade
	 */
	public NotificationFacade getNotificationFacade()
	{
		return notificationFacade;
	}





	/**
	 * @param notificationFacade
	 *           the notificationFacade to set
	 */
	public void setNotificationFacade(final NotificationFacade notificationFacade)
	{
		this.notificationFacade = notificationFacade;
	}





	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.controllers.pages.CheckoutStepController#enterStep(org.springframework.ui.Model,
	 * org.springframework.web.servlet.mvc.support.RedirectAttributes)
	 */
	@Override
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{
		return null;
	}

	//TPR-429 change
	public static String populateCheckoutSellers(final CartData cartData)
	{
		String cartLevelSellerID = null;
		final List<OrderEntryData> sellerList = cartData.getEntries();
		for (final OrderEntryData seller : sellerList)
		{
			if (seller.getSelectedSellerInformation() != null)
			{
				final String sellerID = seller.getSelectedSellerInformation().getSellerID();
				if (cartLevelSellerID != null)
				{
					cartLevelSellerID += "_" + sellerID;
				}
				else
				{
					cartLevelSellerID = sellerID;
				}
			}
		}
		return cartLevelSellerID;
	}

	//added for 3782

	/**
	 * @return the priceBreakupFacade
	 */
	public PriceBreakupFacade getPriceBreakupFacade()
	{
		return priceBreakupFacade;
	}

	/**
	 * @param priceBreakupFacade
	 *           the priceBreakupFacade to set
	 */
	public void setPriceBreakupFacade(final PriceBreakupFacade priceBreakupFacade)
	{
		this.priceBreakupFacade = priceBreakupFacade;
	}

	//TPR-429 change:- PaymentPage redirect fix
	public static String populateCheckoutSellersForOrder(final OrderData orderData)
	{
		String cartLevelSellerID = null;
		final List<OrderEntryData> sellerList = orderData.getEntries();
		for (final OrderEntryData seller : sellerList)
		{
			final String sellerID = seller.getSelectedSellerInformation().getSellerID();
			if (cartLevelSellerID != null)
			{
				cartLevelSellerID += "_" + sellerID;
			}
			else
			{
				cartLevelSellerID = sellerID;
			}
		}
		return cartLevelSellerID;
	}

	/**
	 * Captcha at cod Payment implementation
	 *
	 * @param guid
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CONFIRMCODORDER, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody String validateforCOD(final String guid) throws InvalidKeyException, NoSuchAlgorithmException
	{

		boolean redirectFlag = false;
		String validationMsg = "";
		OrderModel orderModel = null;
		String emailId = null;
		//added for CAR:127
		OrderData oData = null;
		//added for CAR:127
		try
		{
			//getting current user
			emailId = getUserService().getCurrentUser().getUid();
			//OTP handled for both cart and order

			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);
			}
			if (null == orderModel)
			{
				//Existing code for cartModel
				final CartModel cart = getCartService().getSessionCart();
				//UF-281/282 Starts
				if (!checkCODforCNC(cart))
				{
					LOG.debug("COD payment is not allwoed if an user selects CNC as payment mode");
					return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
				}
				//UF-281/282 Ends
				//TPR-4461 Starts here for payment mode and bank restriction validation for Voucher
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService()
						.getAppliedVouchers(cart));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount != null && discount instanceof PromotionVoucherModel)//null check added for discount as per IQA review
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;


								final String paymentModeCard = cart.getModeOfPayment();//Card Payment Mode


								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode


								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												willApply = true;
												break;
											}

										}
									}
									else
									{
										willApply = true;
									}
								}

								if (willApply == false)
								{
									return MarketplacecheckoutaddonConstants.REDIRECTTOCOUPON;
								}
							}

						}
					}
				}
				//TPR-4461 Ends here for payment mode and bank restriction validation for Voucher


				//added for CAR:127
				final CartData cData = getMplCartFacade().getCartDataFromCartModel(cart, false);
				//added for CAR:127
				LOG.debug(" TIS-414 : Checking - onclick of pay now button pincode servicabilty and promotion");
				if (!getMplCheckoutFacade().isPromotionValid(cart))
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
					//commented for CAR:127
					/*
					 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
					 */
					final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cData, cart);
					if (!inventoryReservationStatus)
					{
						getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
						redirectFlag = true;
					}
				}

				if (!redirectFlag && !getMplCheckoutFacade().isCouponValid(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
					redirectFlag = true;
				}

				//TISPRO-497
				final Double cartTotal = cart.getTotalPrice();
				final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();

				if (!redirectFlag && cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
					redirectFlag = true;
				}
				//TISPRO-578
				if (!redirectFlag && !getMplPaymentFacade().isValidCart(cart))
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID, "TRUE");
					redirectFlag = true;
				}

				if (redirectFlag)
				{
					return MarketplacecheckoutaddonConstants.REDIRECTSTRING;
				}
				else
				{
					//If customer is not null
					if (null != emailId)
					{
						validationMsg = "SUCCESS";
					}
					else
					{
						return null;
					}
				}

			}
			//Code implemented for Order TPR-629
			else
			{
				//TPR-4461 Starts here for payment mode and bank restriction validation for Voucher
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService().getAppliedVouchers(
						orderModel));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount != null && discount instanceof PromotionVoucherModel)//null check added for discount as per IQA review
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;

								final String paymentModeCard = orderModel.getModeOfOrderPayment();//Card Payment Mode

								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode

								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												willApply = true;
												break;
											}

										}
									}
									else
									{
										willApply = true;
									}
								}

								if (willApply == false)
								{
									return MarketplacecheckoutaddonConstants.REDIRECTTOCOUPON;
								}
							}

						}
					}
				}
				//TPR-4461 Ends here for payment mode and bank restriction validation for Voucher

				//TPR-815
				if (!getMplCheckoutFacade().isPromotionValid(orderModel))
				{
					//getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
					getMplCartFacade().recalculateOrder(orderModel);
					redirectFlag = true;
				}

				if (!redirectFlag)
				{
					//added for CAR:127
					oData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
					//added for CAR:127
					//commented for CAR:127
					/*
					 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel);
					 */
					final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, oData, orderModel);
					if (!inventoryReservationStatus)
					{
						getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_ORDER_INVENTORY_RESV_SESSION_ID,
								"TRUE");
						getMplCartFacade().recalculateOrder(orderModel);
						redirectFlag = true;
						//notify EMAil SMS TPR-815
						mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
					}
				}
				if (redirectFlag)
				{
					return MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT;
				}
				//If customer is not null
				if (null != emailId)
				{
					validationMsg = "SUCCESS";
				}
				else
				{
					return null;
				}
			}

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
	 * This method creates mRupee related order.
	 *
	 * @param walletName
	 * @param cartGuid
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	@RequestMapping(value = "createWalletorder", method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String createWalletorder(final String walletName, final String cartGuid)
			throws EtailNonBusinessExceptions
	{
		List<String> orderId = new ArrayList<String>();
		String response = null;
		String refNumber = null;
		String checksum = null;
		try
		{
			//Getting redirect url to send to mRupee
			final StringBuilder returnUrlBuilder = new StringBuilder();
			returnUrlBuilder
					.append(request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8)))
					.append(request.getContextPath())
					.append(
							getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.MRUPEERETURNMETHOD));
			OrderModel orderModel = null;
			//added for CAR:127
			OrderData oData = null;
			//added for CAR:127

			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(cartGuid);
			}

			if (orderModel == null)
			{
				//As per EQA comments checking for hasSessionCart
				if (getCartService().hasSessionCart())
				{
					final CartModel cart = getCartService().getSessionCart();

					//TPR-4461 Starts here for payment mode and bank restriction validation for Voucher
					final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService().getAppliedVouchers(
							cart));

					if (CollectionUtils.isNotEmpty(voucherList))
					{
						VoucherModel appliedVoucher = null;

						final DiscountModel discount = voucherList.get(0);

						if (discount != null && discount instanceof PromotionVoucherModel)//null check added for discount as per IQA review
						{
							final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
							appliedVoucher = promotionVoucherModel;

							final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
							for (final RestrictionModel restriction : restrictions)
							{
								if (restriction instanceof PaymentModeRestrictionModel)
								{
									boolean willApply = false;


									final String paymentModeCard = cart.getModeOfPayment();//Customer's selected Payment Mode


									final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
											.getPaymentTypeData(); //Voucher Payment mode


									if (CollectionUtils.isNotEmpty(paymentTypeList))
									{
										if (StringUtils.isNotEmpty(paymentModeCard))
										{
											for (final PaymentTypeModel paymentType : paymentTypeList)
											{
												if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
												{
													willApply = true;
													break;
												}

											}
										}
										else
										{
											willApply = true;
										}
									}

									if (willApply == false)
									{
										return MarketplacecheckoutaddonConstants.REDIRECTTOCOUPON;
									}
								}

							}
						}
					}
					//TPR-4461 Ends here for payment mode and bank restriction validation for Voucher



					//added for CAR:127
					final CartData cData = getMplCartFacade().getCartDataFromCartModel(cart, false);
					//added for CAR:127

					final Double cartTotal = cart.getTotalPrice();
					final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();
					boolean redirectFlag = false;


					LOG.debug("Checking - onclick of pay now button pincode servicabilty and promotion");
					if (!redirectFlag && !getMplCheckoutFacade().isPromotionValid(cart))
					{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
						redirectFlag = true;
						LOG.info("::setting redirect flag--1::");
					}


					if (!redirectFlag)
					{
						final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cart);
						if (cartItemDelistedStatus)
						{
							redirectFlag = true;
							LOG.info("::setting redirect flag--2::");
						}
					}

					if (!redirectFlag)
					{

						//commented for CAR:127
						/*
						 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
						 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
						 */
						final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
								MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cData, cart);
						if (!inventoryReservationStatus)
						{

							getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
							redirectFlag = true;
							LOG.info("::setting redirect flag--3::");
						}
					}

					if (!redirectFlag && !getMplCheckoutFacade().isCouponValid(cart))
					{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
						redirectFlag = true;
						LOG.info("::setting redirect flag--4::");
					}

					//Merged nested if statement as for PMD
					if (!redirectFlag && (cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0))
					{
						//if (cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
						//{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
						redirectFlag = true;
						LOG.info("::setting redirect flag--5::");
						//}
					}

					if (!redirectFlag && !getMplPaymentFacade().isValidCart(cart))
					{
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID, "TRUE");
						redirectFlag = true;
						LOG.info("::setting redirect flag--6::");
					}

					if (redirectFlag)
					{
						LOG.info("::returning redirect String::");
						return MarketplacecheckoutaddonConstants.REDIRECTSTRING;
					}
					else
					{

						//getCommerceCartService().recalculateCart(cart);
						LOG.info("::Going to Create Wallet OrderId::");

						orderId = getMplPaymentFacade().createWalletorder(cart, walletName,
								MarketplacecheckoutaddonConstants.CHANNEL_WEB);

						if (CollectionUtils.isNotEmpty(orderId))
						{
							refNumber = orderId.get(0);
							checksum = orderId.get(1);
						}

						final boolean isValidCart = getMplPaymentFacade().checkCart(cart);

						if (isValidCart)
						{
							getMplPaymentFacade().entryInTPWaltAudit(null, MarketplacecheckoutaddonConstants.CHANNEL_WEB, cartGuid,
									refNumber);
							getMplCheckoutFacade().placeOrder();
						}
						else
						{
							throw new InvalidCartException(
									"************PaymentMethodCheckoutStepController : placeOrder : Invalid Cart!!!"
											+ (StringUtils.isNotEmpty(cart.getGuid()) ? cart.getGuid()
													: MarketplacecommerceservicesConstants.EMPTY));
						}

						LOG.info("::Created Wallet OrderId::" + orderId);

					}

					response = refNumber + "|" + checksum + "|" + cartGuid + "|" + cartTotal + "|" + returnUrlBuilder;
					LOG.info("Reference number from createWalletOrder when order is null -- " + refNumber);
					LOG.info("Response from createWalletOrder when order is null -- " + response);

					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.REFNUMBER, refNumber);
					return response;
				}
				else
				{
					LOG.info("User has no cart in session from createWalletOrder when order is null -- ");
				}
			}

			else
			{
				//TPR-4461 Starts here for payment mode and bank restriction validation for Voucher
				final ArrayList<DiscountModel> voucherList = new ArrayList<DiscountModel>(getVoucherService().getAppliedVouchers(
						orderModel));

				if (CollectionUtils.isNotEmpty(voucherList))
				{
					VoucherModel appliedVoucher = null;

					final DiscountModel discount = voucherList.get(0);

					if (discount != null && discount instanceof PromotionVoucherModel)//null check added for discount as per IQA review
					{
						final PromotionVoucherModel promotionVoucherModel = (PromotionVoucherModel) discount;
						appliedVoucher = promotionVoucherModel;

						final Set<RestrictionModel> restrictions = appliedVoucher.getRestrictions();
						for (final RestrictionModel restriction : restrictions)
						{
							if (restriction instanceof PaymentModeRestrictionModel)
							{
								boolean willApply = false;

								final String paymentModeCard = orderModel.getModeOfOrderPayment();////Customer's selected Payment Mode

								final List<PaymentTypeModel> paymentTypeList = ((PaymentModeRestrictionModel) restriction)
										.getPaymentTypeData(); //Voucher Payment mode

								if (CollectionUtils.isNotEmpty(paymentTypeList))
								{
									if (StringUtils.isNotEmpty(paymentModeCard))
									{
										for (final PaymentTypeModel paymentType : paymentTypeList)
										{
											if (StringUtils.equalsIgnoreCase(paymentType.getMode(), paymentModeCard))
											{
												willApply = true;
												break;
											}

										}
									}
									else
									{
										willApply = true;
									}
								}

								if (willApply == false)
								{
									return MarketplacecheckoutaddonConstants.REDIRECTTOCOUPON;
								}
							}

						}
					}
				}
				//TPR-4461 Ends here for payment mode and bank restriction validation for Voucher

				//added for CAR:127
				oData = getMplCheckoutFacade().getOrderDetailsForCode(orderModel);
				//added for CAR:127

				if (null == orderModel.getPaymentInfo() && !OrderStatus.PAYMENT_TIMEOUT.equals(orderModel.getStatus()))
				{
					boolean redirectFlag = false;
					if (!getMplCheckoutFacade().isPromotionValid(orderModel))
					{
						getMplCartFacade().recalculateOrder(orderModel);
						redirectFlag = true;
					}

					if (!redirectFlag)
					{
						//commmented for CAR:127
						/*
						 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
						 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel);
						 */
						final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
								MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, oData, orderModel);
						if (!inventoryReservationStatus)
						{
							getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_ORDER_INVENTORY_RESV_SESSION_ID,
									"TRUE");
							getMplCartFacade().recalculateOrder(orderModel);
							redirectFlag = true;
							mplCartFacade.notifyEmailAndSmsOnInventoryFail(orderModel);
						}
					}
					if (redirectFlag)
					{
						return MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT;
					}
					else
					{
						orderId = getMplPaymentFacade().createWalletorder(orderModel, walletName,
								MarketplacecheckoutaddonConstants.CHANNEL_WEB);

						if (CollectionUtils.isNotEmpty(orderId))
						{
							refNumber = orderId.get(0);
							checksum = orderId.get(1);
						}
						getMplPaymentFacade().entryInTPWaltAudit(null, MarketplacecheckoutaddonConstants.CHANNEL_WEB, cartGuid,
								refNumber);
						LOG.info("::Created Wallet OrderId::" + orderId);

						response = refNumber + "|" + checksum + "|" + cartGuid + "|" + orderModel.getTotalPrice() + "|"
								+ returnUrlBuilder;
						LOG.info("Reference number from createWalletOrder when order is not null -- " + refNumber);
						LOG.info("Response from createWalletOrder when order is not null -- " + response);
						getSessionService().setAttribute(MarketplacecheckoutaddonConstants.REFNUMBER, refNumber);
						return response;

					}
				}
				else if (null != orderModel.getPaymentInfo())
				{
					LOG.error("Order already has payment info >>>" + orderModel.getPaymentInfo().getCode());
					return "redirect_with_details";
				}
				else
				{
					LOG.error("Order status is Payment_Pending for orderCode>>>" + orderModel.getCode());
					return "redirect_with_details";
				}
			}

		}

		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
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

		return response;


	}

	/**
	 * This method processes mRupee related order after getting response from mRupee.
	 *
	 * @param redirectAttributes
	 * @param request
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */

	@RequestMapping(value = "walletPayment", method = RequestMethod.GET)
	@RequireHardLogIn
	public String walletPayment(final RedirectAttributes redirectAttributes, final HttpServletRequest request)
			throws EtailNonBusinessExceptions
	{
		String guid = null;

		OrderModel orderModel = null;

		String status = null;

		final DecimalFormat format = new DecimalFormat("0.##");

		try
		{
			final String refNo = request.getParameter("REFNO");

			LOG.info("walletPayment-method***REF NO:" + refNo);

			final Double transactionAmount = Double.valueOf(request.getParameter("AMT"));
			/* Getting guid from audit table based on the reference no. received from mRupee */
			if (StringUtils.isNotEmpty(refNo))
			{
				guid = getMplPaymentFacade().getWalletAuditEntries(refNo);

			}

			if (StringUtils.isNotEmpty(guid))
			{
				orderModel = getMplPaymentFacade().getOrderByGuid(guid);

			}

			if (null != orderModel)
			{
				final Double orderAmount = orderModel.getTotalPriceWithConv();

				LOG.info("Total Convenience Price in walletPayment::" + orderAmount);

				LOG.info("Total Price in walletPayment::" + orderModel.getTotalPrice());

				/* Checking transaction status from mRupee */
				if (null != request.getParameter("STATUS") && "S".equalsIgnoreCase(request.getParameter("STATUS")))
				{
					LOG.info("Order Amount -- " + format.format(orderAmount) + "Transaction Amount is --- "
							+ format.format(transactionAmount));

					/* Checking transaction amount received from mRupee and order amount */
					/*
					 * if (format.format(orderAmount).compareTo(format.format(transactionAmount)) == 0) {
					 */
					status = MarketplacecheckoutaddonConstants.SUCCESS;
					getMplPaymentFacade().entryInTPWaltAudit(status, MarketplacecheckoutaddonConstants.CHANNEL_WEB,
							orderModel.getGuid(), refNo);

					//saving TPWallet Payment related info
					getMplPaymentFacade().saveTPWalletPaymentInfo(orderModel, request);

					LOG.info("Removing Attribute inside IF::");

					getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.REFNUMBER);

					return updateOrder(orderModel, redirectAttributes);
					/*
					 * } else { LOG.info("OrderAmount--TransactionAmount Mismatch"); status =
					 * MarketplacecheckoutaddonConstants.FAIL; //setting the audit table to DECLINED if order amount is not
					 * equal getMplPaymentFacade().entryInTPWaltAudit(status, MarketplacecheckoutaddonConstants.CHANNEL_WEB,
					 * orderModel.getGuid(), refNo); GlobalMessages.addFlashMessage(redirectAttributes,
					 * GlobalMessages.ERROR_MESSAGES_HOLDER, MarketplacecheckoutaddonConstants.TRANERRORMSG); return
					 * MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL +
					 * MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid; }
					 */
				}

				else
				{

					final String reason = request.getParameter("REASON");

					LOG.info("The reason is for mRupee payment failure for reference number -- " + refNo + " is --- " + reason);

					//setting the audit table to DECLINED if transaction status is not successful
					status = MarketplacecheckoutaddonConstants.FAIL;
					getMplPaymentFacade().entryInTPWaltAudit(status, MarketplacecheckoutaddonConstants.CHANNEL_WEB, guid, refNo);

					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);

					getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.REFNUMBER);

					LOG.info("Removing Attribute REFNUMBER inside ELSE::");

					return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
							+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
				}
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions in walletPayment", e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			//ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions in walletPayment", e);
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
		}
		catch (final Exception e)
		{
			LOG.error("Exception in walletPayment", e);
			//Redirection when transaction is failed
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
					MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
			return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
					+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
		}

		return null;

	}


	//TPR-4461 starts here

	/**
	 * @return the voucherService
	 */
	public VoucherService getVoucherService()
	{
		return voucherService;
	}



	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}

	//TPR-4461 ends here
	/**
	 * @Description Check if cart contsins CNC product
	 * @param cart
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	//UF-281/282 Start
	public boolean checkCODforCNC(final CartModel cart) throws UnsupportedEncodingException
	{

		boolean codEligible = true;

		if (cart != null)
		{
			for (final AbstractOrderEntryModel entry : cart.getEntries())
			{

				if (entry.getMplDeliveryMode() != null && entry.getMplDeliveryMode().getDeliveryMode() != null)
				{
					if (entry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplaceFacadesConstants.C_C))
					{
						codEligible = false;
						LOG.debug("CNC not allowed for CNC");
					}
				}

			}
		}
		return codEligible;
	}
	//UF-281/282 End


	@RequestMapping(value = "/useWalletForPayment", method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody JSONObject UseWalletForPayment(@RequestParam("walletMode") final String value)
			throws UnsupportedEncodingException, JSONException
	{

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("disableJsMode", false);
		jsonObject.put("apportionMode", "Juspay");
		final CartModel cart = getCartService().getSessionCart();
		final DecimalFormat df = new DecimalFormat("#.##");
		try
		{

			if (StringUtils.isNotEmpty(value) && value.equalsIgnoreCase("true"))
			{

				final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();

				final BalanceBucketWise balBucketwise = mplWalletFacade
						.getQCBucketBalance(currentCustomer.getCustomerWalletDetail().getWalletId());

				if (balBucketwise.getResponseCode() == Integer.valueOf(0) && null != balBucketwise.getWallet().getBalance())
				{
					final Double WalletAmt = Double.valueOf((df.format(balBucketwise.getWallet().getBalance().doubleValue())));
					
					double totalCartAmt = cart.getTotalPrice().doubleValue();
					
				//	System.out.println("totalAmt"+totalCartAmt);

//					totalCartAmt += (null != cart.getScheduleDelCharge() ? cart.getScheduleDelCharge().doubleValue() : 0)
//							+ (null != cart.getDeliveryCost() ? cart.getDeliveryCost().doubleValue() : 0);

					System.out.println("**************  totalAmt"+totalCartAmt);
					
					if (Double.parseDouble("" + WalletAmt) >= Double.parseDouble("" + totalCartAmt))
					{

						getSessionService().setAttribute("WalletTotal", "" + totalCartAmt);
						getSessionService().setAttribute("getCliqCashMode", value);
						getSessionService().setAttribute("juspayTotalAmt", "" + 0);
						jsonObject.put("disableJsMode", true);
						cart.setSplitModeInfo("CliqCash");
						jsonObject.put("apportionMode", "CliqCash");
						//jsonObject.put("juspayAmt", 0);
						getModelService().save(cart);
						getModelService().refresh(cart);
					}
					else
					{
						double juspayTotalAmt = Double.parseDouble("" + totalCartAmt) - Double.parseDouble("" + WalletAmt);
						juspayTotalAmt = Double.parseDouble(df.format(juspayTotalAmt));
						getSessionService().setAttribute("WalletTotal", "" + WalletAmt);
						getSessionService().setAttribute("juspayTotalAmt", "" + juspayTotalAmt);
						getSessionService().setAttribute("cliqCashPaymentMode", "Cliq Cash");
						jsonObject.put("disableJsMode", false);
						jsonObject.put("juspayAmt", juspayTotalAmt);
						cart.setSplitModeInfo("Split");
						jsonObject.put("apportionMode", "Split");
						getModelService().save(cart);
						getModelService().refresh(cart);
					}
				}
			}
			else
			{
				jsonObject.put("disableJsMode", false);
				cart.setSplitModeInfo("Juspay");
				jsonObject.put("apportionMode", "Juspay");
				getSessionService().setAttribute("WalletTotal", "" + 0);
				getModelService().save(cart);
				getModelService().refresh(cart);
				jsonObject.put("juspayAmt", 0);
			}
		
		}
		catch (

		final Exception ex)
		{
			ex.printStackTrace();
			jsonObject.put("disableJsMode", false);
		}
		return jsonObject;
	}

	@RequestMapping(value = "/setWalletForPayment", method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody JSONObject setWalletForPayment(@RequestParam("walletMode") final String value)
			throws UnsupportedEncodingException, JSONException
	{
		final CartModel cart = getCartService().getSessionCart();
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("disableJsMode", false);
		try
		{

			if (StringUtils.isNotEmpty(value) && value.equalsIgnoreCase("true"))
			{

				//final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();

				//CustomerWalletDetailResponse customerWalletDetailResponse = mplWalletFacade.getCustomerWallet(currentCustomer.getCustomerWalletDetail().getWalletId());

				if (null != cart.getTotalWalletAmount())
				{
					final Double WalletAmt = cart.getTotalWalletAmount();
					
					double totalCartAmt = cart.getTotalPrice().doubleValue();
					
				//	System.out.println("totalAmt"+totalCartAmt);

//					totalCartAmt += (null != cart.getScheduleDelCharge() ? cart.getScheduleDelCharge().doubleValue() : 0)
//							+ (null != cart.getDeliveryCost() ? cart.getDeliveryCost().doubleValue() : 0);

					System.out.println("**************  totalAmt"+totalCartAmt);
					
					if (Double.parseDouble("" + WalletAmt) >= Double.parseDouble("" + totalCartAmt))
					{
						cart.setSplitModeInfo("CliqCash");
						getModelService().save(cart);
						getModelService().refresh(cart);
						jsonObject.put("disableJsMode", true);
					}
					else
					{
						jsonObject.put("disableJsMode", false);
						cart.setSplitModeInfo("Split");
						getModelService().save(cart);
						getModelService().refresh(cart);
					}
				}
			}
			else
			{
				jsonObject.put("disableJsMode", false);
				cart.setSplitModeInfo("Juspay");
				getModelService().save(cart);
				getModelService().refresh(cart);
			}
			return jsonObject;
		
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		return jsonObject;
	}
	

	/**
	 * @param request
	 * @throws JSONException
	 */
	@RequestMapping(value = "/useWalletDetail", method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody JSONObject getCurrentUserWallet(final HttpServletRequest request)
			throws UnsupportedEncodingException, JSONException
	{
		final JSONObject jsonObject = new JSONObject();
		BalanceBucketWise balBucketwise = new BalanceBucketWise();
		final CartModel cart = getCartService().getSessionCart();
		//final Double totalCartAmt = cart.getTotalPriceWithConv();
		double totalCartAmt = cart.getTotalPrice().doubleValue();

		totalCartAmt += (null != cart.getScheduleDelCharge() ? cart.getScheduleDelCharge().doubleValue() : 0)
				+ (null != cart.getDeliveryCost() ? cart.getDeliveryCost().doubleValue() : 0);
		final DecimalFormat df = new DecimalFormat("#.##");
		
		System.out.println("totalCartAmt Load Wallet ***** "+totalCartAmt);

		double cashBalance = 0;
		double egvBalance = 0;
		double walletPoint = 0;
		double totalWalletAmt = 0;
		double juspayAmt = 0;

		try
		{
			final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();

			if (customer.getIsWalletActivated().booleanValue())
			{
				balBucketwise = mplWalletFacade.getQCBucketBalance(customer.getCustomerWalletDetail().getWalletId());

				if (balBucketwise.getResponseCode() == Integer.valueOf(0) && null != balBucketwise.getBuckets() && null != balBucketwise.getWallet())
				{
					
					cart.setTotalWalletAmount(balBucketwise.getWallet().getBalance());
					getModelService().save(cart);
					getModelService().refresh(cart);

					for (final Bucket bucketType : balBucketwise.getBuckets())
					{
						if (bucketType.getType().equalsIgnoreCase("CUSTOMER"))
						{
							egvBalance += Double.parseDouble(bucketType.getAmount().toString().isEmpty() ? "0"
									: "" + Double.parseDouble(bucketType.getAmount().toString()));
							egvBalance = Double.parseDouble(df.format(egvBalance));
						}
						if (bucketType.getType().equalsIgnoreCase("CREDIT"))
						{
							cashBalance += Double.parseDouble(bucketType.getAmount().toString().isEmpty() ? "0"
									: "" + Double.parseDouble(bucketType.getAmount().toString()));
							cashBalance = Double.parseDouble(df.format(cashBalance));
						}
						if (bucketType.getType().equalsIgnoreCase("GOODWILL") || bucketType.getType().equalsIgnoreCase("PROMOTION")
								|| bucketType.getType().equalsIgnoreCase("CASHBACK"))
						{

							walletPoint += Double.parseDouble(bucketType.getAmount().toString().isEmpty() ? "0"
									: "" + Double.parseDouble(bucketType.getAmount().toString()));
							walletPoint = Double.parseDouble(df.format(walletPoint));
						}

					}

					totalWalletAmt = Double.parseDouble(balBucketwise.getWallet().getBalance().toString().isEmpty() ? "0"
							: "" + Double.parseDouble(balBucketwise.getWallet().getBalance().toString()));
					totalWalletAmt = Double.parseDouble(df.format(totalWalletAmt));

					if (Double.parseDouble("" + totalWalletAmt) < Double.parseDouble("" + totalCartAmt))
					{
						juspayAmt = Double.parseDouble("" + totalCartAmt) - Double.parseDouble("" + totalWalletAmt);
						juspayAmt = Double.parseDouble(df.format(juspayAmt));

					}

				}

				if (totalWalletAmt == 0)
				{
					getSessionService().setAttribute("getCliqCashMode", "false");
					getSessionService().setAttribute("cliqCashPaymentMode", StringUtils.EMPTY);
					jsonObject.put("totalWalletAmt", "0");
					jsonObject.put("totalCash", "" + 0);
					jsonObject.put("totalEgvBalance", "" + 0);
					jsonObject.put("walletPoint", "" + 0);
					jsonObject.put("disableWallet", true);
					jsonObject.put("isWalletActive", true);
					jsonObject.put("walletDisableMsg", "");

				}
				else
				{

					jsonObject.put("totalWalletAmt", totalWalletAmt);
					jsonObject.put("totalCash", "" + cashBalance);
					jsonObject.put("totalEgvBalance", "" + egvBalance);
					jsonObject.put("walletPoint", "" + walletPoint);
					jsonObject.put("disableWallet", false);
					jsonObject.put("isWalletActive", true);
					jsonObject.put("walletDisableMsg", "");
				}
			}
			else if (!customer.getIsWalletActivated().booleanValue() && null != customer.getCustomerWalletDetail())
			{
				jsonObject.put("totalWalletAmt", "0");
				jsonObject.put("totalCash", "" + 0);
				jsonObject.put("totalEgvBalance", "" + 0);
				jsonObject.put("walletPoint", "" + 0);
				jsonObject.put("disableWallet", true);
				jsonObject.put("isWalletActive", false);
				jsonObject.put("walletDisableMsg", Localization.getLocalizedString("text.cliq.cash.payment.wallet.disable.label"));
			}
			else
			{
				jsonObject.put("totalWalletAmt", "0");
				jsonObject.put("totalCash", "" + 0);
				jsonObject.put("totalEgvBalance", "" + 0);
				jsonObject.put("walletPoint", "" + 0);
				jsonObject.put("disableWallet", true);
				jsonObject.put("isWalletActive", true);
				jsonObject.put("walletDisableMsg", "");

			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			System.out.println(" Error in QuickSilver Api --" + balBucketwise.getResponseCode() + " Message-"
					+ balBucketwise.getResponseMessage());
			jsonObject.put("totalWalletAmt", "0");
			jsonObject.put("walletPoint", "" + 0);
			jsonObject.put("totalCash", "" + 0);
			jsonObject.put("totalEgvBalance", "" + 0);
			jsonObject.put("disableWallet", true);
			jsonObject.put("juspayAmt", "0");
			jsonObject.put("isWalletActive", true);
			jsonObject.put("walletDisableMsg", "");

		}

		return jsonObject;
	}

	/**
	 * @param model
	 */
	@RequestMapping(value = "/createWalletOrder", method = RequestMethod.GET)
	@RequireHardLogIn
	@ResponseBody
	public String createWalletOrder(final Model model, final RedirectAttributes redirectAttributes)
			throws EtailNonBusinessExceptions
	{
		String orderId = null;
		try
		{
			final CustomerModel customer = (CustomerModel) getUserService().getCurrentUser();
			String uid = "";
			if (null != customer)
			{
				uid = customer.getUid();
			}
			LOG.info(uid);

			final CartModel cart = getCartService().getSessionCart();


			final CartData cData = getMplCartFacade().getCartDataFromCartModel(cart, false);
			//added for CAR:127

			//final Double cartTotals = cart.getTotalPriceWithConv();
			boolean redirectFlag = false;
			//final String returnUrl = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/", 8))
			//		+ request.getContextPath()
			//		+ getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYRETURNMETHOD);
			//try
			//{
			//TISPRD-3025 - LOG to handle address --- moved to facade layer

			//				final StringBuilder sb = new StringBuilder();
			//				sb.append("firstName:::").append(firstName).append("|lastName:::").append(lastName).append("|addressLine1:::")
			//						.append(paymentAddressLine1).append("|addressLine2:::").append(paymentAddressLine2).append("|addressLine3:::")
			//						.append(paymentAddressLine3).append("|country:::").append(country).append("|state:::").append(state)
			//						.append("|city:::").append(city).append("|pincode:::").append(pincode).append("|cardSaved:::")
			//						.append(cardSaved).append("|sameAsShipping:::").append(sameAsShipping).append("|cartGUID:::").append(cartGuid);
			//
			//				LOG.error("Address details entered >>>" + sb.toString());
			//Address log code moved to facade to handle both web and mobile

			LOG.debug(" TIS-414  : Checking - onclick of pay now button pincode servicabilty and promotion");
			if (!redirectFlag && !getMplCheckoutFacade().isPromotionValid(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED, "TRUE");
				redirectFlag = true;
				LOG.info("::setting redirect flag--1::");
			}
			//TISST-13012
			//				final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cart);
			//				if (!redirectFlag && cartItemDelistedStatus)
			//				{
			//					redirectFlag = true;
			//				}

			if (!redirectFlag)
			{
				final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(cart);
				if (cartItemDelistedStatus)
				{
					redirectFlag = true;
					LOG.info("::setting redirect flag--2::");
				}
			}


			//TISUTO-12 , TISUTO-11
			if (!redirectFlag)
			{

				//TPR3780 STARTS HERE
				//	final double prevTotalCartPrice = cart.getTotalPrice().doubleValue();
				//TPR3780 ENDS HERE

				//commented for CAR:127
				/*
				 * final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
				 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
				 */

				final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(

						//MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, null);
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cData, cart);

				//TPR3780 STARTS HERE

				Boolean replaced = Boolean.FALSE;
				replaced = getSessionService().getAttribute("replacedUssid");
				//	final CartModel cartModelAfterinventoryCheck = getCartService().getSessionCart();
				//	final double newTotalCartPrice = cartModelAfterinventoryCheck.getTotalPrice().doubleValue();


				if (null != replaced && replaced.booleanValue())
				{
					//return MarketplacecheckoutaddonConstants.INVENTORYRESERVED;
					final String updateStatus = MarketplacecheckoutaddonConstants.UPDATED;
					getSessionService().removeAttribute("replacedUssid");
					redirectFlag = true;
					LOG.info("::setting redirect flag--inv reservation changed for new item::");
					getSessionService().setAttribute("flashupdateStatus", updateStatus);
				}

				//TPR3780 ENDS HERE


				if (!inventoryReservationStatus)
				{

					getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
					redirectFlag = true;
					LOG.info("::setting redirect flag--3::");
				}
			}

			if (!redirectFlag && !getMplCheckoutFacade().isCouponValid(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID, "TRUE");
				redirectFlag = true;
				LOG.info("::setting redirect flag--4::");
			}

			//TISPRO-497
			//				final Double cartTotal = cart.getTotalPrice();
			//				final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();
			//
			//				if (!redirectFlag && cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
			//				{
			//					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
			//					redirectFlag = true;
			//				}
			if (!redirectFlag)
			{
				final Double cartTotal = cart.getTotalPrice();
				final Double cartTotalWithConvCharge = cart.getTotalPriceWithConv();

				LOG.info("::cartTotal**::" + cartTotal);
				LOG.info("::cartTotalWithConvCharge**::" + cartTotalWithConvCharge);
				if (cartTotal.doubleValue() <= 0.0 || cartTotalWithConvCharge.doubleValue() <= 0.0)
				{
					getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CARTAMOUNTINVALID, "TRUE");
					redirectFlag = true;
					LOG.info("::setting redirect flag--5::");
				}
			}

			//TISPRO-578
			if (!redirectFlag && !getMplPaymentFacade().isValidCart(cart))
			{
				getSessionService().setAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID, "TRUE");
				redirectFlag = true;
				LOG.info("::setting redirect flag--6::");
			}

			if (redirectFlag)
			{
				LOG.info("::returning redirect String::");
				return MarketplacecheckoutaddonConstants.REDIRECTSTRING; //IQA for TPR-629
			}


			if (cart.getSplitModeInfo().equalsIgnoreCase("CliqCash"))
			{
				final OrderData orderData;
				final boolean isValidCart = getMplPaymentFacade().checkCart(cart);
				if (isValidCart)
				{
					orderData = getMplCheckoutFacade().placeOrder();

				}
				else
				{
					throw new InvalidCartException("************PaymentMethodCheckoutStepController : placeOrder : Invalid Cart!!!"
							+ (StringUtils.isNotEmpty(cart.getGuid()) ? cart.getGuid() : MarketplacecommerceservicesConstants.EMPTY));
				}


				final OrderModel orderToBeUpdated = getMplPaymentFacade().getOrderByGuid(orderData.getGuid());

				QCRedeeptionResponse qcResponse = new QCRedeeptionResponse();

				try
				{
					final String qcUniqueCode = getMplPaymentFacade().generateQCCode();
					final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
					qcResponse = getMplPaymentFacade().createQCOrderRequest(orderToBeUpdated.getGuid(), orderToBeUpdated,
							currentCustomer.getCustomerWalletDetail().getWalletId(), "Cliq Cash", qcUniqueCode, "WEB", 0.0D, 0.0D);

					if (null != qcResponse && null != qcResponse.getResponseCode() && qcResponse.getResponseCode().intValue() == 0)
					{

						return updateQCOrder(orderToBeUpdated, redirectAttributes);
					}
					else if (null != qcResponse && null != qcResponse.getResponseCode()
							&& qcResponse.getResponseCode().intValue() != 0)
					{

						orderToBeUpdated.setStatus(OrderStatus.PAYMENT_FAILED); /// return QC fail and Update Audit Entry Try With Juspay
						orderToBeUpdated.setSplitModeInfo("juspay");
						getModelService().save(orderToBeUpdated);
						//getSessionService().setAttribute("cliqCashPaymentMode", "false");

						GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
								MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
						return MarketplacecheckoutaddonConstants.MPLPAYMENTURL + MarketplacecheckoutaddonConstants.PAYVALUE
								+ MarketplacecheckoutaddonConstants.VALUE + orderToBeUpdated.getGuid();
					}

					else if (null == qcResponse || null == qcResponse.getResponseCode())
					{

						orderToBeUpdated.setStatus(OrderStatus.PAYMENT_FAILED); /// NO Exception No qcResponse Try With Juspay
						orderToBeUpdated.setSplitModeInfo("juspay");
						getModelService().save(orderToBeUpdated);

						GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
								MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
						return MarketplacecheckoutaddonConstants.MPLPAYMENTURL + MarketplacecheckoutaddonConstants.PAYVALUE
								+ MarketplacecheckoutaddonConstants.VALUE + orderToBeUpdated.getGuid();
					}

				}
				catch (final Exception ex)
				{

					if (null != qcResponse && null != qcResponse.getResponseCode() && qcResponse.getResponseCode().intValue() == 0)
					{
						orderToBeUpdated.setStatus(OrderStatus.RMS_VERIFICATION_FAILED);
						orderToBeUpdated.setSplitModeInfo("juspay");
						getModelService().save(orderToBeUpdated);
						LOG.error("Some Error in QC Service");
						System.out.println("Some Error in QC Service");
						return "QC PAYMENT SUCCESS EXCEPTION";
						/// Return In JS Ajax Call  And Execute Refund call refund...........
					}

					GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
							MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
					return "QC PAYMENT EXCEPTION"; /// Return In JS Ajax Call
				}
			}

		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}
		catch (final AdapterException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			orderId = "QC_CONN_ERROR";
			//to be check
			//return MarketplacecheckoutaddonConstants.REDIRECTTOPAYMENT;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			orderId = "NONBusinessException" + e.getErrorMessage();
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}

		return orderId;
	}


	private String updateQCOrder(final OrderModel orderToBeUpdated, final RedirectAttributes redirectAttributes)
			throws InvalidCartException, CalculationException, EtailNonBusinessExceptions
	{
		try
		{
			if (null != orderToBeUpdated && CollectionUtils.isEmpty(orderToBeUpdated.getChildOrders())
					&& null != orderToBeUpdated.getPaymentInfo())
			{
				getMplCheckoutFacade().beforeSubmitOrder(orderToBeUpdated);


				getMplCheckoutFacade().submitOrder(orderToBeUpdated);

				//order confirmation email and sms
				getNotificationFacade().sendOrderConfirmationNotification(orderToBeUpdated);

				final OrderData orderData = getMplCheckoutFacade().getOrderDetailsForCode(orderToBeUpdated);

				System.out.println("------------------------ Final QC OrderData Status ::::::::: " + orderData.getStatus());

				return "/checkout/orderConfirmation/" + orderData.getCode();
			}

			else if (null != orderToBeUpdated && null == orderToBeUpdated.getPaymentInfo()
					&& OrderStatus.PAYMENT_TIMEOUT.equals(orderToBeUpdated.getStatus()))
			{
				LOG.error("Issue with update order...redirecting to payment page only");
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.PAYMENTIMEOUTRRORMSG);
				return getCheckoutStep().currentStep();
			}
			else
			{
				LOG.error("Issue with update order...redirecting to payment page only");
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.PAYMENTTRANERRORMSG);
				return getCheckoutStep().currentStep();
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	//GiFT CART Payment Start 13-09-2017
	@RequireHardLogIn
	@RequestMapping(value = GIFT_CART_PAYMENT, method =
	{ RequestMethod.POST, RequestMethod.GET })
	public String getGiftPayment(@ModelAttribute("egvDetailsform") final EgvDetailForm egvDetailForm,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request)
			throws UnsupportedEncodingException
	{
		try
		{
			CartData giftCartData = null;

			if (egvDetailForm.getProductCode() == null)
			{
				final String guid = getSessionService().getAttribute(EGVGUID);
				giftCartData = mplCartFacade.getGiftCartData(guid);
			}
			else
			{
				if (mplEgvFormValidator.validate(egvDetailForm))
				{
					final EgvDetailsData egvDetailsData = populateEGVFormToData(egvDetailForm);
					giftCartData = mplCartFacade.getGiftCartModel(egvDetailsData);
					giftCartData.setEgvTotelAmount(egvDetailsData.getGiftRange());
				}
				else
				{

					return MarketplacecheckoutaddonConstants.REDIRECT + GIFT_CARD
							+ getConfigurationService().getConfiguration().getString(MARKETPLACE_HEADER_EGV_PRODUCT_CODE)
							+ "/?egvErrorMsg=" + "formValidation";
				}
			}
			giftCartData.setIsEGVCart(true);
			Map<String, Boolean> paymentModeMap = null;
			final OrderData orderData = null;
			final String checkoutSellerID = populateCheckoutSellers(giftCartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.CHECKOUT_SELLER_IDS, checkoutSellerID);
			//Getting Payment modes
			paymentModeMap = getMplPaymentFacade().getPaymentModes(MarketplacecheckoutaddonConstants.MPLSTORE, false, giftCartData);
			//Cart guid added to propagate to further methods via jsp
			model.addAttribute(MarketplacecheckoutaddonConstants.GUID, giftCartData.getGuid());
			getSessionService().setAttribute(EGVGUID, giftCartData.getGuid());
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTTOORDERCONVERT, Boolean.FALSE); //INC144315475
			GenericUtilityMethods.populateTealiumDataForCartCheckout(model, giftCartData);
			final PriceDataFactory priceDataFactory = Registry.getApplicationContext().getBean("priceDataFactory",
					PriceDataFactory.class);
			Long cartTotalMrp = Long.valueOf(0);

			try
			{
				final long cost = (long) giftCartData.getEgvTotelAmount();
				cartTotalMrp = Long.valueOf(cost);
			}
			catch (final Exception exception)
			{
				LOG.error("Error Occure " + exception);
			}

			final double couponDiscount = 0.0D;
			final BigDecimal totalDiscount = new BigDecimal(couponDiscount);
			final BigDecimal cartTotalMrpValue = new BigDecimal(cartTotalMrp.longValue());
			final PriceData cartTotalMrpVal = priceDataFactory.create(PriceDataType.BUY, cartTotalMrpValue,
					MarketplacecommerceservicesConstants.INR);
			final PriceData totalDiscountVal = priceDataFactory.create(PriceDataType.BUY, totalDiscount,
					MarketplacecommerceservicesConstants.INR);
			model.addAttribute("cartTotalMrp", cartTotalMrpVal);
			model.addAttribute("totalDiscount", totalDiscountVal);
			model.addAttribute("egvProductCode",
					getConfigurationService().getConfiguration().getString(MARKETPLACE_HEADER_EGV_PRODUCT_CODE));
			final PaymentForm paymentForm = new PaymentForm();
			setupAddPaymentPage(model);
			if (MapUtils.isNotEmpty(paymentModeMap)) // Code optimization for performance fix TISPT-169
			{
				//Adding payment modes in model to be accessed from jsp
				model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTMODES, paymentModeMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.TRANERRORMSG, "");
				timeOutSet(model);


				model.addAttribute(MarketplacecheckoutaddonConstants.NEWPAYMENTFORMMPLURL,
						MarketplacecheckoutaddonConstants.NEWPAYMENTVIEWURL);
				model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTFORM, paymentForm);

				final Double cartTotal = Double.valueOf(egvDetailForm.getGiftRange());
				setupMplCardForm(model, cartTotal);
				//Adding all the details in model to be accessed from jsp
				model.addAttribute(MarketplacecheckoutaddonConstants.ORDERDATA, orderData);
				model.addAttribute("isCart", Boolean.TRUE);
				model.addAttribute("isEGVCart", Boolean.TRUE);

				model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_MERCHANT_URL,
						getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.MRUPEEURL));
				model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_CODE,
						getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.MRUPEE_MERCHANT_CODE));

				model.addAttribute(MarketplacecheckoutaddonConstants.MRUPEE_NARRATION, getConfigurationService().getConfiguration()
						.getString(MarketplacecheckoutaddonConstants.MRUPEE_NARRATION_VALUE));
				model.addAttribute(MarketplacecheckoutaddonConstants.JUSPAYJSNAME,
						getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.JUSPAYJSNAMEVALUE));
				model.addAttribute(MarketplacecheckoutaddonConstants.SOPFORM, new PaymentDetailsForm());
				//Terms n Conditions Link
				model.addAttribute(MarketplacecheckoutaddonConstants.TNCLINK,
						getConfigurationService().getConfiguration().getString(MarketplacecheckoutaddonConstants.TNCLINKVALUE));
				model.addAttribute(MarketplacecheckoutaddonConstants.NEWPAYMENTFORMMPLURL,
						MarketplacecheckoutaddonConstants.NEWPAYMENTVIEWURL);
			}
		}
		catch (final BindingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}

		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.E0007, e);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error(MarketplacecommerceservicesConstants.B6001, e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error(MarketplacecommerceservicesConstants.B6001, e);
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
		}
		model.addAttribute("checkoutPageName", checkoutPageName);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
	}

	/**
	 * @param egvDetailForm
	 * @return
	 */
	private EgvDetailsData populateEGVFormToData(final EgvDetailForm egvDetailForm)
	{
		final EgvDetailsData egvDetailsData = new EgvDetailsData();
		egvDetailsData.setFromEmailAddress(egvDetailForm.getFromEmailAddress());
		egvDetailsData.setProductCode(egvDetailForm.getProductCode());
		egvDetailsData.setGiftRange(egvDetailForm.getGiftRange());
		egvDetailsData.setOpenTextAmount(egvDetailForm.getOpenTextAmount());
		egvDetailsData.setToEmailAddress(egvDetailForm.getToEmailAddress());
		egvDetailsData.setMessageBox(egvDetailForm.getMessageBox());
		egvDetailsData.setTotalEGV(egvDetailForm.getTotalEGV());
		egvDetailsData.setFromFirstName(egvDetailForm.getFromFirstName());
		egvDetailsData.setFromLastName(egvDetailForm.getFromLastName());
		egvDetailsData.setFromPhoneNo(egvDetailForm.getFromPhoneNo());
		return egvDetailsData;
	}

	/**
	 * @param model
	 * @param redirectAttributes
	 * @param guid
	 * @return
	 * @throws InvalidCartException
	 * @throws CalculationException
	 */
	private String getEGVOrderStatus(final Model model, final RedirectAttributes redirectAttributes, final String guid)
			throws InvalidCartException, CalculationException
	{
		final OrderModel orderToBeUpdated = getMplPaymentFacade().getOrderByGuid(guid);
		String orderStatusResponse = null;
		orderStatusResponse = getMplPaymentFacade().getOrderStatusFromJuspay(guid, null, orderToBeUpdated, null);
		//Redirection when transaction is successful i.e. CHARGED
		if (null != orderStatusResponse)
		{
			if (MarketplacecheckoutaddonConstants.CHARGED.equalsIgnoreCase(orderStatusResponse))
			{
				model.addAttribute(MarketplacecheckoutaddonConstants.PAYMENTID, null);
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				return updateOrder(orderToBeUpdated, redirectAttributes);
			}
			else if (MarketplacecheckoutaddonConstants.JUSPAY_DECLINED.equalsIgnoreCase(orderStatusResponse))
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.DECLINEDERRORMSG);
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
						+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
			}
			else if (MarketplacecheckoutaddonConstants.AUTHORIZATION_FAILED.equalsIgnoreCase(orderStatusResponse)
					|| MarketplacecheckoutaddonConstants.AUTHENTICATION_FAILED.equalsIgnoreCase(orderStatusResponse)
					|| MarketplacecheckoutaddonConstants.PENDING_VBV.equalsIgnoreCase(orderStatusResponse))
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.VBVERRORMSG);
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
						+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
			}
			else
			{
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
						MarketplacecheckoutaddonConstants.TRANERRORMSG);
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.MPLPAYMENTURL
						+ MarketplacecheckoutaddonConstants.PAYVALUE + MarketplacecheckoutaddonConstants.VALUE + guid;
			}
		}
		else
		{
			return updateOrder(orderToBeUpdated, redirectAttributes);
		}

	}

	/**
	 * @param firstName
	 * @param lastName
	 * @param country
	 * @param state
	 * @param city
	 * @param pincode
	 * @param cardSaved
	 * @param sameAsShipping
	 * @param guid
	 * @param orderId
	 * @param returnUrlBuilder
	 * @param paymentAddressLine1
	 * @param paymentAddressLine2
	 * @param paymentAddressLine3
	 * @param uid
	 * @return
	 * @throws InvalidCartException
	 */
	private String createJuspayOrderForEGV(final String firstName, final String lastName, final String country, final String state,
			final String city, final String pincode, final String cardSaved, final String sameAsShipping, final String guid,
			String orderId, final StringBuilder returnUrlBuilder, final String paymentAddressLine1, final String paymentAddressLine2,
			final String paymentAddressLine3, final String uid) throws InvalidCartException
	{
		try
		{

			final CartModel cart = mplEGVCartService.getEGVCartModel(guid);
			LOG.info("::Going to Create Juspay OrderId::");
			orderId = getMplPaymentFacade().createJuspayOrder(cart, null, firstName, lastName, paymentAddressLine1,
					paymentAddressLine2, paymentAddressLine3, country, state, city, pincode,
					cardSaved + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + sameAsShipping, returnUrlBuilder.toString(), uid,
					MarketplacecheckoutaddonConstants.CHANNEL_WEB, 0.0D);
			getMplCheckoutFacade().placeEGVOrder(cart);
			return orderId + "|" + guid;
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			mplEGVCartService.removeOldEGVCartCurrentCustomer();
			return "EGVOderError";
		}
		catch (final AdapterException e)
		{
			LOG.error(MarketplacecheckoutaddonConstants.LOGERROR, e);
			mplEGVCartService.removeOldEGVCartCurrentCustomer();
			return "EGVOderError";
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			mplEGVCartService.removeOldEGVCartCurrentCustomer();
			return "EGVOderError";
		}
	}


	@RequireHardLogIn
	@RequestMapping(value = "/addEGV", method = RequestMethod.GET)
	public String showAddEGVPopup(final Model model)

			throws CMSItemNotFoundException, QCServiceCallException
	{

		model.addAttribute("addToCardWalletForm", new AddToCardWalletForm());
		return "pages/payment/addGiftCard";
	}

	@RequireHardLogIn
	@RequestMapping(value = "/addEGVToWallet", method = RequestMethod.POST)
	public @ResponseBody String addEGV(final AddToCardWalletForm addToCardWalletForm, final Model model)
			throws CMSItemNotFoundException, QCServiceCallException
	{
		try
		{
			final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();

			if (null != currentCustomer && null != currentCustomer.getIsWalletActivated()
					&& !currentCustomer.getIsWalletActivated().booleanValue() && null == currentCustomer.getCustomerWalletDetail())
			{
				final QCCustomerRegisterRequest customerRegisterReq = new QCCustomerRegisterRequest();
				final Customer custInfo = new Customer();
				custInfo.setEmail(currentCustomer.getOriginalUid());
				custInfo.setEmployeeID(currentCustomer.getUid());
				custInfo.setCorporateName("Tata Unistore Ltd");

				if (null != currentCustomer.getFirstName())
				{
					custInfo.setFirstname(currentCustomer.getFirstName());
				}
				if (null != currentCustomer.getLastName())
				{
					custInfo.setLastName(currentCustomer.getLastName());
				}

				customerRegisterReq.setExternalwalletid(currentCustomer.getOriginalUid());
				customerRegisterReq.setCustomer(custInfo);
				customerRegisterReq.setNotes("Activating Customer " + currentCustomer.getOriginalUid());
				final QCCustomerRegisterResponse customerRegisterResponse = mplWalletFacade
						.createWalletContainer(customerRegisterReq);

				if (null != customerRegisterResponse && null != customerRegisterResponse.getResponseCode()
						&& customerRegisterResponse.getResponseCode() == Integer.valueOf(0))
				{
					final CustomerWalletDetailModel custWalletDetail = modelService.create(CustomerWalletDetailModel.class);
					custWalletDetail.setWalletId(customerRegisterResponse.getWallet().getWalletNumber());
					custWalletDetail.setWalletState(customerRegisterResponse.getWallet().getStatus());
					custWalletDetail.setCustomer(currentCustomer);
					custWalletDetail.setServiceProvider("Tata Unistore Ltd");

					modelService.save(custWalletDetail);

					currentCustomer.setCustomerWalletDetail(custWalletDetail);
					currentCustomer.setIsWalletActivated(Boolean.TRUE);
					modelService.save(currentCustomer);

					final RedimGiftCardResponse response = mplWalletFacade.getAddEGVToWallet(addToCardWalletForm.getCardNumber(),
							addToCardWalletForm.getCardPin());

					if (null != response && null != response.getResponseCode() && response.getResponseCode() == Integer.valueOf(0))
					{
						
						return "SUCCESS";
					}
					else
					{

						LOG.error("card Add Error " + response.getResponseMessage());
						//	return "ERROR";
						return setValidErrorCodeHandling(response.getResponseCode().intValue());
					}

				}
			}
			else if (null != currentCustomer && null != currentCustomer.getIsWalletActivated()
					&& currentCustomer.getIsWalletActivated().booleanValue())
			{
				final RedimGiftCardResponse response = mplWalletFacade.getAddEGVToWallet(addToCardWalletForm.getCardNumber(),
						addToCardWalletForm.getCardPin());

				if (null != response && null != response.getResponseCode() && response.getResponseCode() == Integer.valueOf(0))
				{
					return "SUCCESS";
				}
				else
				{
					LOG.error("card Add Error " + response.getResponseMessage());
					//return "ERROR";
					return setValidErrorCodeHandling(response.getResponseCode().intValue());
				}
			}

		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
		}
		return "ERROR";
	}

	private String setValidErrorCodeHandling(final int errorCode)
	{
		String errorMessage = "";
		if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10004).intValue())
		{
			errorMessage = ModelAttributetConstants.ERROR_CODE_10004_DESC;
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10027).intValue())
		{
			errorMessage = ModelAttributetConstants.ERROR_CODE_10027_DESC;
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10528).intValue())
		{
			errorMessage = ModelAttributetConstants.ERROR_CODE_10528_DESC;
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10086).intValue())
		{
			errorMessage = ModelAttributetConstants.ERROR_CODE_10086_DESC;
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10096).intValue())
		{
			errorMessage = ModelAttributetConstants.ERROR_CODE_10096_DESC;
		}
		else if (errorCode == Integer.valueOf(ModelAttributetConstants.ERROR_CODE_10550).intValue())
		{
			errorMessage = ModelAttributetConstants.ERROR_CODE_10550_DESC;
		}
		else
		{
			errorMessage = "Error";
		}
		return errorMessage;
	}

}
