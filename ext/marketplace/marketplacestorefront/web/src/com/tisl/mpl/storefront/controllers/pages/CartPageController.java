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
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorservices.enums.CheckoutFlowEnum;
import de.hybris.platform.acceleratorservices.enums.CheckoutPciOptionEnum;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateQuantityForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.Constants.USER;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.granule.json.JSONException;
import com.granule.json.JSONObject;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.storefront.security.cookie.PDPPincodeCookieGenerator;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.MaxLimitData;


/*@author TCS*/
/**
 * Controller for cart page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = MarketplacecommerceservicesConstants.CART_URL)
public class CartPageController extends AbstractPageController
{
	private static final Logger LOG = Logger.getLogger(CartPageController.class);
	private static final String className = "CartPageController";
	//private Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
	//private final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
	private Map<String, String> sellerInfoMap = new HashMap<String, String>();


	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;


	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;
	@Autowired
	private UserService userService;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	//TPR-6654
	@Resource(name = "pdpPincodeCookieGenerator")
	private PDPPincodeCookieGenerator pdpPincodeCookie;

	@Autowired
	private WishlistFacade wishlistFacade;

	@Autowired
	private ProductFacade productFacade;

	@Resource(name = "cartService")
	private CartService cartService;

	@Autowired
	private UserFacade userFacade;

	@Autowired
	private AccountAddressFacade accountAddressFacade;

	@Autowired
	private PriceDataFactory priceDataFactory;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private ConfigurationService configurationService;
	@Resource(name = "modelService")
	private ModelService modelService;

	//Exchange Changes
	@Resource(name = "exchangeGuideFacade")
	private ExchangeGuideFacade exchangeGuideFacade;

	//Blocked for SONAR FIX
	//	@Resource(name = "pincodeServiceFacade")
	//	private PincodeServiceFacade pincodeServiceFacade;
	//	@Resource(name = "pinCodeFacade")
	//	private PinCodeServiceAvilabilityFacade pinCodeFacade;
	@Autowired
	private MplCouponFacade mplCouponFacade;

	@Resource(name = "checkoutFacade")
	private CheckoutFacade checkoutFacade;

	/*
	 * Display the cart page
	 */
	@SuppressWarnings(
	{ MarketplacecommerceservicesConstants.BOXING, "deprecation" })
	@RequestMapping(method = RequestMethod.GET)
	public String showCart(final Model model, @RequestParam(value = "ussid", required = false) final String ussid,
			@RequestParam(value = "pincode", required = false) final String pinCode,
			@RequestParam(value = "isLux", required = false) final boolean isLux,
			@RequestParam(value = "cartGuid", required = false) final String cartGuid, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException,
			CommerceCartModificationException, CalculationException
	{
		LOG.debug("Entering into showCart" + "Class Nameshowcart :" + className + "pinCode " + pinCode);
		String returnPage = ControllerConstants.Views.Pages.Cart.CartPage;
		//TPR-6654
		int pincodeCookieMaxAge;
		final Cookie cookiePdp = GenericUtilityMethods.getCookieByName(request, "pdpPincode");
		final String cookieMaxAge = getConfigurationService().getConfiguration().getString("pdpPincode.cookie.age");
		pincodeCookieMaxAge = (Integer.valueOf(cookieMaxAge)).intValue();
		final String domain = getConfigurationService().getConfiguration().getString("shared.cookies.domain");

		try
		{

			CartModel externalCart = null;
			CartModel cartModel = null;
			final String currentUser = userService.getCurrentUser().getUid();
			//TPR-5666, TPR-5667 | check on cartGuid parameter
			if (cartGuid != null)
			{
				externalCart = mplCartFacade.getCartByGuid(cartGuid);
				// If invalid cartGuid
				if (externalCart == null)
				{
					LOG.debug("Fetched invalid cart with guid " + cartGuid);
					return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.INVALID_CART_URL;
				}
				else
				{
					final String externalCartUser = externalCart.getUser().getUid();
					// fetching existing cart by guid
					if (!externalCartUser.equalsIgnoreCase(MarketplacecommerceservicesConstants.ANONYMOUS))
					{
						LOG.debug("Fetched existing user cart with guid " + cartGuid);
						return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
					}
					// fetching anonymous cart by guid
					else
					{
						//merging in case of logged in user
						if (!currentUser.equalsIgnoreCase(MarketplacecommerceservicesConstants.ANONYMOUS))
						{
							mplCartFacade.mergeCarts(externalCart, getCartService().getSessionCart());
							final CartModel sessionCart = getCartService().getSessionCart();
							sessionCart.setChannel(SalesApplication.SAMSUNG);
							modelService.save(sessionCart);
						}
						// show fetched cart for anonymous user
						else
						{
							externalCart.setChannel(SalesApplication.SAMSUNG);
							modelService.save(externalCart);
							getCartService().setSessionCart(externalCart);
						}
						return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
					}
				}

			}
			/*
			 * else { cartModel = getCartService().getSessionCart(); }
			 */

			//TPR-3780

			final String flashupdateStatus = getSessionService().getAttribute("flashupdateStatus");
			if (flashupdateStatus != null)
			{
				model.addAttribute("priceNotificationUpdateStatus", flashupdateStatus);
				getSessionService().removeAttribute("flashupdateStatus");
			}
			//final String flashupdateStatus = (String) model.asMap().get("flashupdateStatus");
			final String flashtotalCartPriceAsString = (String) model.asMap().get("flashtotalCartPriceAsString");
			//model.addAttribute("priceNotificationUpdateStatus", flashupdateStatus);
			model.addAttribute("totalCartPriceAsStringStatus", flashtotalCartPriceAsString);
			//TPR-3780
			//TISST-13012
			//if (StringUtils.isNotEmpty(cartDataOnLoad.getGuid())) //TISPT-104
			if (getCartService().hasSessionCart())
			{

				cartModel = getCartService().getSessionCart();
				CartData cartDataOnLoad = mplCartFacade.getSessionCartWithEntryOrdering(true);

				final List<AbstractOrderEntryModel> entryJewl = new ArrayList<>(cartModel.getEntries());
				final List<String> discSellerNameList = new ArrayList<String>();

				for (final AbstractOrderEntryModel orderEntry : entryJewl)
				{
					if ("FineJewellery".equalsIgnoreCase(orderEntry.getProduct().getProductCategoryType()))
					{
						final String sellerId = orderEntry.getSellerInfo();
						final String sellers = getConfigurationService().getConfiguration().getString(
								"cart.jewellery.disclaimer.sellerName");
						if (StringUtils.isNotEmpty(sellers))
						{
							final String sellersArray[] = sellers.split(",");
							if (StringUtils.isNotEmpty(sellerId))
							{
								for (final String s : sellersArray)
								{
									if (StringUtils.equalsIgnoreCase(sellerId, s))
									{
										discSellerNameList.add(s + ",");
									}
								}
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(discSellerNameList))
				{
					model.addAttribute("discSellerNameList", discSellerNameList);
					//String displayMsg = getConfigurationService().getConfiguration().getString("cart.price.disclaimer");
					String displayMsg = "";
					for (final String sellerName : discSellerNameList)
					{
						if (StringUtils.isEmpty(displayMsg))
						{
							displayMsg = sellerName;
						}
						else
						{
							displayMsg = displayMsg + sellerName;
						}
					}
					final String displayMsgFinal = getConfigurationService().getConfiguration().getString(
							"cart.price.disclaimer.first")
							+ MarketplacecommerceservicesConstants.SPACE
							+ displayMsg.substring(0, displayMsg.length() - 1)
							+ MarketplacecommerceservicesConstants.SPACE
							+ getConfigurationService().getConfiguration().getString("cart.price.disclaimer.second");
					//GlobalMessages.addConfMessage(model, displayMsg.substring(0, displayMsg.length() - 1));
					GlobalMessages.addConfMessage(model, displayMsgFinal);
				}
				//TPR-5346 STARTS

				//This method will update the cart  with respect to the max quantity configured for the product
				//String ReachedMaxLimitforproduct = null;
				final Map<String, MaxLimitData> updateCartOnMaxLimExceeds = getMplCartFacade().updateCartOnMaxLimExceeds(cartModel);

				if (MapUtils.isNotEmpty(updateCartOnMaxLimExceeds) && updateCartOnMaxLimExceeds.size() > 0)
				{
					String errorMsg = null;
					final Map<String, String> msgMap = new HashMap<String, String>();

					final List<AbstractOrderEntryModel> entryList = new ArrayList<>(cartModel.getEntries());

					if (CollectionUtils.isNotEmpty(entryList))
					{
						for (final AbstractOrderEntryModel orderEntry : entryList)
						{
							final ProductModel product = orderEntry.getProduct();
							final Integer maxQuantity = product.getMaxOrderQuantity();

							if (null != product.getName() && null != maxQuantity)
							{
								errorMsg = MarketplacecommerceservicesConstants.PRECOUNTMSG
										+ MarketplacecommerceservicesConstants.SINGLE_SPACE + product.getName()
										+ MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.MIDCOUNTMSG
										+ MarketplacecommerceservicesConstants.SINGLE_SPACE + maxQuantity.toString()
										+ MarketplacecommerceservicesConstants.SINGLE_SPACE
										+ MarketplacecommerceservicesConstants.LASTCOUNTMSG;
								msgMap.put(product.getCode(), errorMsg);
							}
						}
					}
					if (MapUtils.isNotEmpty(msgMap))
					{
						for (final Map.Entry<String, String> entry : msgMap.entrySet())
						{
							if (updateCartOnMaxLimExceeds.containsKey(entry.getKey()))
							{
								GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, entry.getValue());
							}
						}
					}
					returnPage = REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
				}
				//TPR-5346 ENDS


				//setExpressCheckout(serviceCart); //TISPT-104
				/*
				 * if (!serviceCart.getChannel().equals(SalesApplication.WEB)) {
				 * serviceCart.setChannel(SalesApplication.WEB); getModelService().save(serviceCart); }
				 */
				//TISEE-3676 & TISEE-4013
				//final boolean deListedStatus = getMplCartFacade().isCartEntryDelisted(serviceCart); Moved to facade layer //TISPT-104
				//LOG.debug("Cart Delisted Status " + deListedStatus);


				final boolean isUserAnym = getUserFacade().isAnonymousUser();//UF-70
				model.addAttribute("isUserAnym", isUserAnym);//UF-70

				//TISEE-432//UF-70
				final String selectedPinCode = StringUtil.isNotEmpty(pinCode) ? pinCode : fetchPincode(isUserAnym);//UF-70
				//CAR-246//UF-70
				if (StringUtils.isNotEmpty(selectedPinCode))
				{
					//TPR-6654
					if (cookiePdp != null && cookiePdp.getValue() != null)
					{
						cookiePdp.setValue(selectedPinCode);
						cookiePdp.setMaxAge(pincodeCookieMaxAge);
						cookiePdp.setPath("/");

						if (null != domain && !domain.equalsIgnoreCase("localhost"))
						{
							cookiePdp.setSecure(true);
						}
						cookiePdp.setDomain(domain);
						response.addCookie(cookiePdp);
						getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPinCode);
					}
					else
					{
						pdpPincodeCookie.addCookie(response, selectedPinCode);
						getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPinCode);
					}
					//getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPinCode);
					mplCartFacade.populatePinCodeData(cartModel, selectedPinCode);
				}

				//final Object sessionPincode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
				//				if (null != sessionPincode)
				//				{
				//					//TPR-970 changes
				//					mplCartFacade.populatePinCodeData(cartModel, sessionPincode.toString());
				//					//	getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
				//				}
				getMplCouponFacade().releaseVoucherInCheckout(cartModel); //TISPT-104
				cartModel = getMplCartFacade().getCalculatedCart(cartModel); /// Cart recalculation method invoked inside this method

				//final CartModel cart = mplCartFacade.removeDeliveryMode(serviceCart); // Contains recalculate cart TISPT-104
				//TISST-13010

				getMplCartFacade().setCartSubTotal(cartModel);
				//final CartModel cartModel = getCartService().getSessionCart();

				//To calculate discount percentage amount for display purpose
				// TPR-774-- Total MRP calculation and the Product percentage calculation
				getMplCartFacade().totalMrpCal(cartModel);
				//final CartModel cartModel = getCartService().getSessionCart();


				final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);


				// TPR-429
				String cartLevelSellerID = null;
				final List<OrderEntryData> sellerList = cartData.getEntries();
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
				model.addAttribute(ModelAttributetConstants.CHECKOUT_SELLER_IDS, cartLevelSellerID);



				checkCartDataChange(cartModel, cartDataOnLoad, cartData, model);
				showPincode(model, selectedPinCode, cartData);
				showAddress(model);

				if (StringUtil.isNotEmpty(ussid))
				{
					sellerInfoMap = getMplCartFacade().getSellerInfo(cartData, ussid);
					model.addAttribute("sellerInfoMap", sellerInfoMap);
				}

				final ArrayList<Integer> quantityConfigurationList = getMplCartFacade().getQuantityConfiguratioList();
				if (CollectionUtils.isNotEmpty(quantityConfigurationList))
				{
					model.addAttribute("configuredQuantityList", quantityConfigurationList);
				}

				//added for jewellery
				final ArrayList<Integer> quantityConfigurationListForJewellery = getMplCartFacade()
						.getQuantityConfiguratioListforJewellery();
				if (CollectionUtils.isNotEmpty(quantityConfigurationListForJewellery))
				{
					model.addAttribute("configuredQuantityForJewellery", quantityConfigurationListForJewellery);
				}
				else
				{
					LOG.debug("CartPageController : product quanity is empty");
				}

				//LUX-225,230
				final int luxuryProducts = countLuxuryProductsInCart(cartData);
				int marketplaceProducts = 0;
				boolean luxFlag = false;
				boolean isLuxFlag = false;
				boolean marketplaceFlag = false;
				if (CollectionUtils.isNotEmpty(cartData.getEntries()))
				{
					marketplaceProducts = cartData.getEntries().size() - luxuryProducts;
					luxFlag = luxuryProducts > 0 ? true : false;
					marketplaceFlag = marketplaceProducts > 0 ? true : false;
					model.addAttribute(ModelAttributetConstants.IS_LUXURY, luxFlag);
				}
				else
				{
					model.addAttribute(ModelAttributetConstants.IS_LUXURY, ControllerConstants.Views.Pages.Cart.EMPTY_CART);
				}


				final String siteId = getSiteConfigService().getProperty("luxury.site.id");
				if ((getCmsSiteService().getCurrentSite().getUid()).equalsIgnoreCase(siteId))
				{
					isLuxFlag = true;

				}
				else
				{
					isLuxFlag = false;
				}

				showMessageToUser(luxFlag, marketplaceFlag, isLuxFlag, model);
				// LW-230 End

				cartDataOnLoad = cartData;
				prepareDataForPage(model, cartDataOnLoad);

				//TPR-6371| DTM Track promotions start
				List<PromotionResultData> productPromoList = new ArrayList<PromotionResultData>();
				List<PromotionResultData> orderPromoList = new ArrayList<PromotionResultData>();
				String promoTitle = "";
				String promoCode = "";
				String promo_id_product = "";
				String promo_id_cart = "";
				final List<String> promolist = new ArrayList<String>();
				productPromoList = cartData.getAppliedOrderPromotions();
				orderPromoList = cartData.getAppliedProductPromotions();

				if (CollectionUtils.isNotEmpty(productPromoList))
				{
					if (productPromoList.get(0).getPromotionData() != null)
					{
						promoTitle = productPromoList.get(0).getPromotionData().getTitle();
						promoCode = productPromoList.get(0).getPromotionData().getCode();
						promo_id_product = promoTitle + ":" + promoCode;
						promolist.add(promo_id_product);
					}

				}
				if (CollectionUtils.isNotEmpty(orderPromoList))
				{
					if (orderPromoList.get(0).getPromotionData() != null)
					{
						promoTitle = orderPromoList.get(0).getPromotionData().getTitle();
						promoCode = orderPromoList.get(0).getPromotionData().getCode();
						promo_id_cart = promoTitle + ":" + promoCode;
						promolist.add(promo_id_cart);
					}

				}
				model.addAttribute("promolist", promolist);
				//TPR-6371| DTM Track promotions end




				model.addAttribute("isPincodeRestrictedPromoPresent",
						mplCartFacade.checkPincodeRestrictedPromoOnCartProduct(cartModel));

			}
			else if (isLux)
			{
				boolean found = false;
				CartData luxCart = null;
				for (final Cookie cookie : request.getCookies())
				{
					if (cookie.getName().equals("mpl-cart") && StringUtils.isNotEmpty(cookie.getValue()))
					{
						found = true;
						break;
					}
				}
				if (found)
				{
					luxCart = mplCartFacade.getLuxCart();
					prepareDataForPage(model, luxCart);
				}
				else
				{
					prepareDataForPage(model, mplCartFacade.getSessionCartWithEntryOrdering(true));
				}
			}
			else
			{
				prepareDataForPage(model, new CartData());
			}
			// for MSD
			//TPR-174

			if (checkoutFacade.getCheckoutCart() != null && checkoutFacade.getCheckoutCart().isGotMerged())
			{
				model.addAttribute(ModelAttributetConstants.WELCOME_BACK_MESSAGE, MessageConstants.WELCOME_BACK_MESSAGE);
			}
			//TPR-174

			//TISSQAUATS-522
			if (null != cartModel)
			{
				//UF-260
				GenericUtilityMethods.getCartPriceDetails(model, cartModel, null);

				cartModel.setMerged(false);
				modelService.save(cartModel);
			}

			LOG.error("CartPageController : showCart : cartModel setMerged saved");

			final String msdjsURL = getConfigurationService().getConfiguration().getString("msd.js.url");
			final Boolean isMSDEnabled = Boolean.valueOf(getConfigurationService().getConfiguration().getString("msd.enabled"));
			model.addAttribute(ModelAttributetConstants.MSD_JS_URL, msdjsURL);
			model.addAttribute(ModelAttributetConstants.IS_MSD_ENABLED, isMSDEnabled);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}

		return returnPage;
	}

	/**
	 * @param serviceCart
	 */
	/*
	 * private void setExpressCheckout(final CartModel serviceCart) {
	 * serviceCart.setIsExpressCheckoutSelected(Boolean.FALSE); if (serviceCart.getDeliveryAddress() != null) {
	 * serviceCart.setDeliveryAddress(null); modelService.save(serviceCart); }
	 * 
	 * }
	 */

	/**
	 * @param luxFlag
	 * @param isLux
	 * @param isLux2
	 */
	private void showMessageToUser(final boolean luxFlag, final boolean marketplaceFlag, final boolean isLux, final Model model)
	{
		if (!isLux)
		{
			if (luxFlag && !marketplaceFlag) //If source is Not Luxury and cart contains luxury product
			{
				GlobalMessages.addInfoMessage(model, "cart.merge.mpl.onlyLuxury");
			}
			else if (luxFlag && marketplaceFlag) //If source is Not Luxury and cart contains both luxury product and mpl product
			{
				GlobalMessages.addInfoMessage(model, "cart.merge.mpl.both");
			}
		}
		else
		{
			if (!luxFlag && marketplaceFlag) //If source is Luxury and cart contains mpl product
			{
				GlobalMessages.addInfoMessage(model, "cart.merge.lux.onlyMpl");
			}
			else if (luxFlag && marketplaceFlag) //If source is Luxury and cart contains both luxury product and mpl product
			{
				GlobalMessages.addInfoMessage(model, "cart.merge.lux.both");
			}
		}

	}

	/**
	 * @param cartData
	 * @return
	 */
	private int countLuxuryProductsInCart(final CartData cartData)
	{
		int luxCount = 0;
		if (null != cartData.getEntries())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				if (null != entry.getProduct())
				{
					if (entry.getProduct().getLuxIndicator() != null
							&& entry.getProduct().getLuxIndicator().equalsIgnoreCase(ControllerConstants.Views.Pages.Cart.LUX_INDICATOR))
					{
						luxCount++; //Setting true if at least one luxury product found

					}
				}
			}
		}
		return luxCount;
	}


	/**
	 *
	 * @param cartDataOld
	 * @param cartDataLatest
	 * @param model
	 */
	private void checkCartDataChange(final CartModel cart, final CartData cartDataOld, final CartData cartDataLatest,
			final Model model) throws EtailBusinessExceptions, EtailNonBusinessExceptions, Exception
	{
		try

		{
			final Map<String, String> priceModified = new HashMap<String, String>();
			final Map<String, String> priceModifiedMssg = new HashMap<String, String>();
			final Map<String, PriceData> basePriceMap = new HashMap<String, PriceData>();
			final Map<String, String> promoModified = new HashMap<String, String>();
			//TPR-774- Map for Entry respective MRP
			final Map<String, PriceData> mrpPriceMap = new HashMap<String, PriceData>();
			if (cartDataOld != null && cartDataLatest != null && cartDataOld.getEntries() != null)
			{
				for (final OrderEntryData entryOld : cartDataOld.getEntries())
				{
					for (final OrderEntryData entryLatest : cartDataLatest.getEntries())
					{
						if (entryLatest.getSelectedSellerInformation() != null
								&& entryLatest.getSelectedSellerInformation().getUssid() != null
								&& entryOld.getSelectedSellerInformation() != null
								&& entryOld.getSelectedSellerInformation().getUssid() != null
								&& entryLatest.getSelectedSellerInformation().getUssid()
										.equalsIgnoreCase(entryOld.getSelectedSellerInformation().getUssid()) && !entryOld.isGiveAway()
								&& !entryLatest.isGiveAway())
						{

							final BigDecimal updatedTotalPrice = new BigDecimal(entryLatest.getTotalPrice().getValue().toString());
							final BigDecimal oldTotalPrice = new BigDecimal(entryOld.getTotalPrice().getValue().toString());
							if (entryLatest.isIsBOGOapplied())
							{
								final Long qty = entryLatest.getQuantity();
								@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
								/*
								 * final Long priceForStrikeOff = ((entryLatest.getBasePrice().getValue().longValue()) * qty);
								 * final BigDecimal strikeOffPrice = new BigDecimal(priceForStrikeOff.longValue()); final
								 * PriceData strikeoffprice = priceDataFactory.create(PriceDataType.BUY, strikeOffPrice,
								 * MarketplaceFacadesConstants.INR);
								 */
								//final Long priceForStrikeOff = ((entryLatest.getBasePrice().getValue().longValue()) * qty);
								//TPR-774
								final Long priceForStrikeOff = ((entryLatest.getMrp().getValue().longValue()) * qty);
								final BigDecimal strikeOffPrice = new BigDecimal(priceForStrikeOff.longValue());
								final PriceData strikeoffprice = priceDataFactory.create(PriceDataType.BUY, strikeOffPrice,
										MarketplaceFacadesConstants.INR);


								model.addAttribute("strikeoffprice", strikeoffprice);
							}


							final int res = updatedTotalPrice.compareTo(oldTotalPrice);
							if (res != 0)
							{

								priceModified.put(entryLatest.getEntryNumber().toString(), cart.getCurrency().getSymbol()
										+ entryOld.getTotalPrice().getValue());
								priceModifiedMssg.put(entryLatest.getEntryNumber().toString(),
										"Sorry! The price of this item has changed.");
							}


							final double oldPromoValue = (entryOld.getQuantity().doubleValue() * Double.parseDouble(entryOld
									.getBasePrice().getValue().toString()))
									- Double.parseDouble(entryOld.getTotalPrice().getValue().toString());
							final double latestPromoValue = (entryLatest.getQuantity().doubleValue() * Double.parseDouble(entryLatest
									.getBasePrice().getValue().toString()))
									- Double.parseDouble(entryLatest.getTotalPrice().getValue().toString());
							if (oldPromoValue != latestPromoValue)
							{
								//TISSTRT-1594 UF-277 message
								promoModified.put(entryLatest.getEntryNumber().toString(), "Sorry! The price of this item has changed");
							}

							//TISEE-535
							final BigDecimal basetotal = new BigDecimal(entryLatest.getBasePrice().getValue().doubleValue()
									* entryLatest.getQuantity());
							final PriceData baseTotalPrice = priceDataFactory.create(PriceDataType.BUY, basetotal,
									MarketplaceFacadesConstants.INR);
							basePriceMap.put(entryLatest.getEntryNumber().toString(), baseTotalPrice);
							model.addAttribute(ModelAttributetConstants.BASEPRICEMAP, basePriceMap);

							//TPR-774
							final BigDecimal mrptotal = new BigDecimal(entryLatest.getMrp().getValue().doubleValue()
									* entryLatest.getQuantity());
							final PriceData mrpTotalPrice = priceDataFactory.create(PriceDataType.BUY, mrptotal,
									MarketplaceFacadesConstants.INR);
							mrpPriceMap.put(entryLatest.getEntryNumber().toString(), mrpTotalPrice);
							model.addAttribute(ModelAttributetConstants.MRPPRICEMAP, mrpPriceMap);
							//TPR-774



							if (entryLatest.getCartLevelDisc() != null && entryLatest.getCartLevelDisc().getValue() != null)
							{
								if (entryOld.getCartLevelDisc() != null && entryOld.getCartLevelDisc().getValue() != null)
								{
									final double oldCartLevelDiscount = Double.parseDouble(entryOld.getCartLevelDisc().getValue()
											.toString());
									final double latestCartLevelDiscount = Double.parseDouble(entryLatest.getCartLevelDisc().getValue()
											.toString());

									//Adding to model
									compareCartLevelDiscount(oldCartLevelDiscount, latestCartLevelDiscount, model);
								}
								else
								{
									model.addAttribute("cartLevelDiscountModified", "Cart Promotion has been modified");
								}
							}
							//TPR-774
							/*
							 * if (null != entryLatest.getTotalSalePrice() && null != entryLatest.getAmountAfterAllDisc()) {
							 * final double savingPriceCal = entryLatest.getTotalSalePrice().getDoubleValue() -
							 * entryLatest.getAmountAfterAllDisc().getDoubleValue(); final double savingPriceCalPer =
							 * (savingPriceCal / entryLatest.getTotalSalePrice().getDoubleValue()) * 100; final double
							 * roundedOffValue = Math.round(savingPriceCalPer * 100.0) / 100.0;
							 * model.addAttribute(ModelAttributetConstants.SAVINGONPRODUCT, roundedOffValue); } else if (null
							 * != entryLatest.getTotalPrice()) { model.addAttribute(ModelAttributetConstants.SAVINGONPRODUCT,
							 * null); }
							 */
							//TPR-774

						}
					}
				}
			}

			model.addAttribute("priceModified", priceModified);
			model.addAttribute("promoModified", promoModified);
			model.addAttribute("priceModifiedMssg", priceModifiedMssg);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
		}
	}

	/**
	 *
	 * @param oldCartLevelDiscount
	 * @param latestCartLevelDiscount
	 * @param model
	 */
	private void compareCartLevelDiscount(final double oldCartLevelDiscount, final double latestCartLevelDiscount,
			final Model model)
	{
		if (oldCartLevelDiscount != latestCartLevelDiscount)
		{
			model.addAttribute("cartLevelDiscountModified", "Cart Promotion has been modified");
		}
	}

	/**
	 * Handle the '/cart/checkout' request url. This method checks to see if the cart is valid before allowing the
	 * checkout to begin. Note that this method does not require the user to be authenticated and therefore allows us to
	 * validate that the cart is valid without first forcing the user to login. The cart will be checked again once the
	 * user has logged in.
	 *
	 * @return The page to redirect to
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	@RequireHardLogIn
	public String cartCheck(@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final Model model,
			final RedirectAttributes redirectModel) throws CommerceCartModificationException, CMSItemNotFoundException
	{
		String returnPage = REDIRECT_PREFIX + "/checkout";
		try
		{
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

			if (!getMplCartFacade().hasEntries() || validateCart(redirectModel))
			{
				LOG.debug("Class Namecartcheck :" + className + "Missing or empty cart");
				returnPage = REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{

			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		return returnPage;

	}

	/*
	 * @description This controller method is used to allow the site to force the visitor through a specified checkout
	 * flow. If you only have a static configured checkout flow then you can remove this method.
	 * 
	 * @param model ,redirectModel
	 */

	@RequestMapping(value = "/checkout/select-flow", method = RequestMethod.GET)
	@RequireHardLogIn
	public String initCheck(@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final Model model,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final RedirectAttributes redirectModel,
			@RequestParam(value = "flow", required = false) final CheckoutFlowEnum checkoutFlow,
			@RequestParam(value = "pci", required = false) final CheckoutPciOptionEnum checkoutPci)
			throws CommerceCartModificationException, CMSItemNotFoundException
	{

		String returnPage = REDIRECT_PREFIX + "/checkout";
		LOG.debug("Entering into initCheck" + "Class Nameinitcheck :" + className);
		try
		{
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

			if (!getMplCartFacade().hasEntries())
			{
				LOG.debug("Class Nameinit :" + className + "Missing or empty cart");

				// No session cart or empty session cart. Bounce back to the cart page.
				//return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
				returnPage = REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
			}
			else
			{
				// Override the Checkout Flow setting in the session
				if (checkoutFlow != null && StringUtils.isNotBlank(checkoutFlow.getCode()))
				{
					SessionOverrideCheckoutFlowFacade.setSessionOverrideCheckoutFlow(checkoutFlow);
				}
				// Override the Checkout PCI setting in the session
				if (checkoutPci != null && StringUtils.isNotBlank(checkoutPci.getCode()))
				{

					SessionOverrideCheckoutFlowFacade.setSessionOverrideSubscriptionPciOption(checkoutPci);
				}

				// Redirect to the start of the checkout flow to begin the checkout process
				// We just redirect to the generic '/checkout' page which will actually select the checkout flow
				// to use. The customer is not necessarily logged in on this request, but will be forced to login
				// when they arrive on the '/checkout' page.
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{

			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		return returnPage;

	}

	@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateCartQuantities(@RequestParam("entryNumber") final long entryNumber, final Model model,
			@Valid final UpdateQuantityForm form, final BindingResult bindingResult, final HttpServletRequest request,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		String returnPage = ControllerConstants.Views.Pages.Cart.CartPage;
		LOG.debug("Entering into updateCartQuantities" + "Class NameupdateCartQuantities :" + className);
		try
		{

			final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			//TPR-1083
			boolean isExchangeEntry = false;
			//else if block for Exchange
			if (cartData != null && cartData.getEntries() != null)
			{
				for (final OrderEntryData od : cartData.getEntries())
				{
					if (od.getEntryNumber().longValue() == entryNumber)
					{
						if (StringUtils.isNotBlank(od.getExchangeApplied()))
						{
							isExchangeEntry = true;
							break;
						}
					}
				}
			}
			if (bindingResult.hasErrors())
			{
				for (final ObjectError error : bindingResult.getAllErrors())
				{
					if (error.getCode().equals("typeMismatch"))
					{
						GlobalMessages.addErrorMessage(model, MessageConstants.ERROR_QUANTITY_INVALID);
					}
					else
					{
						GlobalMessages.addErrorMessage(model, error.getDefaultMessage());
					}
				}
			}

			else if (getMplCartFacade().hasEntries() && !isExchangeEntry)


			{
				//TPR-5346 start
				final boolean checkMaxLimList = getMplCartFacade().checkMaxLimitUpdate(entryNumber, form.getQuantity().longValue());
				if (!checkMaxLimList)
				{
					final CartModel checkCartCount = getCartService().getSessionCart();
					for (final AbstractOrderEntryModel orderEntry : checkCartCount.getEntries())
					{

						if (null != orderEntry.getProduct() && null != orderEntry.getProduct().getName()
								&& null != orderEntry.getProduct().getMaxOrderQuantity())
						{
							final String errorMsg = MarketplacecommerceservicesConstants.PRECOUNTMSG
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE + orderEntry.getProduct().getName().toString()
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE + MarketplacecommerceservicesConstants.MIDCOUNTMSG
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE
									+ orderEntry.getProduct().getMaxOrderQuantity().toString()
									+ MarketplacecommerceservicesConstants.SINGLE_SPACE
									+ MarketplacecommerceservicesConstants.LASTCOUNTMSG;


							GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, errorMsg);
							//						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
							//								"basket.page.message.product.maxcnterror");
							returnPage = REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
							prepareDataForPage(model, cartData);
							return returnPage;
						}
					}
				}
				//TPR-5346 end
				final CartModificationData cartModification = getMplCartFacade().updateCartEntry(entryNumber,
						form.getQuantity().longValue());
				if (cartModification != null)
				{
					if (cartModification.getQuantity() == form.getQuantity().longValue())
					{
						// Success

						if (cartModification.getQuantity() == 0)
						{
							// Success in removing entry
							GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
									"basket.page.message.remove");
						}
						else
						{
							// Success in update quantity
							GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER,
									"basket.page.message.update");
						}
					}
					else if (cartModification.getQuantity() > 0)
					{
						// Less than successful
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
								"basket.page.message.update.reducedNumberOfItemsAdded.lowStock", new Object[]
								{ cartModification.getEntry().getProduct().getName(), cartModification.getQuantity(), form.getQuantity(),
										request.getRequestURL().append(cartModification.getEntry().getProduct().getUrl()) });
					}
					else
					{
						// No more stock available
						GlobalMessages.addFlashMessage(
								redirectModel,
								GlobalMessages.ERROR_MESSAGES_HOLDER,
								"basket.page.message.update.reducedNumberOfItemsAdded.noStock",
								new Object[]
								{ cartModification.getEntry().getProduct().getName(),
										request.getRequestURL().append(cartModification.getEntry().getProduct().getUrl()) });
					}
				}
				// Redirect to the cart page on update success so that the browser doesn't re-post again
				return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
				//				//TPR-5666 | check on cartGuid Parameter
				//				if (StringUtil.isEmpty(request.getParameter("cartGuid")))
				//				{
				//					returnPage = REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
				//				}
				//				else
				//				{
				//					returnPage = REDIRECT_PREFIX + "/cart?cartGuid=" + request.getParameter("cartGuid");
				//				}
			}

			if (isExchangeEntry)
			{
				// Success in update quantity
				GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
						"basket.page.message.exchange.error.productcount");
				returnPage = REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
			}

			prepareDataForPage(model, cartData);

		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.error("Couldn't update product with the entry number: " + entryNumber + ".", ex);
			ExceptionUtil.etailBusinessExceptionHandler(new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E0000),
					null);
			getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			returnPage = ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		return returnPage;
	}

	private void createProductList(final Model model, final CartData cartData) throws CMSItemNotFoundException,
			EtailNonBusinessExceptions, EtailBusinessExceptions, Exception

	{
		LOG.debug("Entring into createProductList" + "Class NamecreateProductList :" + className);

		boolean hasPickUpCartEntries = false;

		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				if (!hasPickUpCartEntries && entry.getDeliveryPointOfService() != null)
				{
					hasPickUpCartEntries = true;
				}
				final UpdateQuantityForm uqf = new UpdateQuantityForm();
				uqf.setQuantity(entry.getQuantity());
				model.addAttribute("updateQuantityForm" + entry.getEntryNumber(), uqf);
			}
		}



		model.addAttribute(ModelAttributetConstants.CART_DATA, cartData);
		model.addAttribute(ModelAttributetConstants.HAS_PICKUP_CART_ENTRIES, Boolean.valueOf(hasPickUpCartEntries));

		storeCmsPageInModel(model, getContentPageForLabelOrId(ModelAttributetConstants.CART_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ModelAttributetConstants.CART_CMS_PAGE_LABEL));

		//model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.cart"));
		model.addAttribute("pageType", PageType.CART.name());

	}



	@RequestMapping(value = "/giftlist", method = RequestMethod.GET)
	public String showGiftList(final Model model) throws CMSItemNotFoundException, EtailNonBusinessExceptions,
			EtailBusinessExceptions, Exception
	{

		final boolean isUserAnym = getUserFacade().isAnonymousUser();
		try
		{
			if (!isUserAnym)
			{
				final String defaultPinCodeId = fetchPincode(isUserAnym);

				//final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true); TISPT-169
				final CartModel cartModel = getCartService().getSessionCart();
				//if (cartData != null && StringUtils.isNotEmpty(cartData.getGuid())) TISPT-169
				if (getCartService().hasSessionCart())
				{
					final Map<String, String> ussidMap = new HashMap<String, String>();
					Map<String, List<String>> giftYourselfDeliveryModeDataMap = new HashMap<String, List<String>>();

					final List<ProductData> productDataList = new ArrayList<ProductData>();
					List<Wishlist2EntryModel> entryModels = new ArrayList<Wishlist2EntryModel>();

					final int minimum_gift_quantity = getSiteConfigService().getInt(MessageConstants.MINIMUM_GIFT_QUANTIY, 0);
					LOG.debug("Class NameprepareDataForPag :" + className + " minimum_gift_quantity :" + minimum_gift_quantity);

					//final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists(); TISPT-179 Point 1
					final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlistsForCustomer(cartModel.getUser());
					//TISPT-179 Point 3
					//entryModels = getMplCartFacade().getGiftYourselfDetails(minimum_gift_quantity, allWishlists, defaultPinCodeId,cartModel); // Code moved to Facade and Impl

					final Tuple2<?, ?> wishListPincodeObject = getMplCartFacade().getGiftYourselfDetails(minimum_gift_quantity,
							allWishlists, defaultPinCodeId, getMplCartFacade().getSessionCartWithEntryOrdering(true));
					entryModels = (List<Wishlist2EntryModel>) wishListPincodeObject.getFirst();

					for (final Wishlist2EntryModel entryModel : entryModels)
					{
						boolean flag = true;
						//TISEE-6376
						if (entryModel.getProduct() != null
								&& (entryModel.getIsDeleted() == null || (entryModel.getIsDeleted() != null && !entryModel.getIsDeleted()
										.booleanValue())))//TPR-5787 check added
						{

							/*
							 * ProductData productData = productFacade.getProductForOptions(entryModel.getProduct(),
							 * Arrays.asList( ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY,
							 * ProductOption.DESCRIPTION, ProductOption.CATEGORIES, ProductOption.PROMOTIONS,
							 * ProductOption.STOCK, ProductOption.REVIEW, ProductOption.DELIVERY_MODE_AVAILABILITY));
							 */

							ProductData productData = productFacade.getProductForOptions(entryModel.getProduct(), Arrays.asList(
									ProductOption.BASIC, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
									ProductOption.SELLER, ProductOption.VARIANT_FULL));

							if (!entryModel.getSizeSelected().booleanValue())
							{
								productData.setSize(StringUtils.EMPTY);
							}
							productData = wishlistFacade.getBuyBoxPrice(entryModel.getUssid(), productData);

							final SellerInformationModel sellerInfoForWishlist = mplSellerInformationService.getSellerDetail(entryModel
									.getUssid());
							//TISPRO-165 Putting Fulfillment type for Treat Yourself Section
							if (sellerInfoForWishlist != null
									&& sellerInfoForWishlist.getRichAttribute() != null
									&& sellerInfoForWishlist.getRichAttribute().size() > 0
									&& ((List<RichAttributeModel>) sellerInfoForWishlist.getRichAttribute()).get(0)
											.getDeliveryFulfillModes() != null
									&& ((List<RichAttributeModel>) sellerInfoForWishlist.getRichAttribute()).get(0)
											.getDeliveryFulfillModes().getCode() != null)

							{
								final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoForWishlist.getRichAttribute())
										.get(0).getDeliveryFulfillModes().getCode();

								final String sellerName = sellerInfoForWishlist.getSellerName();
								if (entryModel.getUssid() != null && null != sellerName)
								{
									ussidMap.put(productData.getCode(), entryModel.getUssid());
									model.addAttribute("ussidMap", ussidMap);
									//model.addAttribute("sellerName", sellerName);
									productData.setSellerName(sellerName); //Added for TISPRD-3799
									LOG.info("Category of the product selected >>>>>>>>>>>>>>>>>>" + productData.getRootCategory());
								}
								if (StringUtils.isNotEmpty(fulfillmentType))
								{
									//model.addAttribute("fulfillmentType", fulfillmentType); //Added for TISPRD-3799
									productData.setFulfillmentType(fulfillmentType);
								}
							}

							//TISPT-169
							for (final AbstractOrderEntryModel cart : cartModel.getEntries())
							{
								if ((cart.getSelectedUSSID().equals(entryModel.getUssid())))
								{
									flag = false;
									break;
								}
							}

							/*
							 * TISPT-169 cartModel for (final OrderEntryData cart : cartData.getEntries()) { if
							 * ((cart.getSelectedUssid().equals(entryModel.getUssid()))) { flag = false; break; } }
							 */
							if (flag)
							{
								productDataList.add(productData);
								model.addAttribute("ProductDatas", productDataList);
							}
						}
					}

					/* TISEE-435 : New Code Added */
					giftYourselfDeliveryModeDataMap = getMplCartFacade().checkPincodeGiftCartData(defaultPinCodeId, entryModels,
							wishListPincodeObject);
					if (MapUtils.isNotEmpty(giftYourselfDeliveryModeDataMap))
					{
						model.addAttribute("giftYourselfDeliveryModeDataMap", giftYourselfDeliveryModeDataMap);
					}
					else
					{
						model.addAttribute("giftYourselfDeliveryModeDataMap", null);
					}
					/* TISEE-435 : New Code Added section ends */


					final ArrayList<Integer> quantityConfigurationList = getMplCartFacade().getQuantityConfiguratioList();
					if (CollectionUtils.isNotEmpty(quantityConfigurationList))
					{
						model.addAttribute("configuredQuantityList", quantityConfigurationList);
					}
					else
					{
						LOG.debug("CartPageController : product quanity is empty");
					}

				}
			}
		}
		catch (final Exception e)
		{

			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));

		}

		return ControllerConstants.Views.Fragments.Cart.GiftList;

	}

	/**
	 * Get Product Delivery Modes
	 */
	private void prepareDataForPage(final Model model, final CartData cartData) throws CMSItemNotFoundException,
			EtailNonBusinessExceptions, EtailBusinessExceptions, Exception

	{
		LOG.debug("Entring into prepareDataForPage" + "Class NameprepareDataForPage :" + className);

		model.addAttribute(ModelAttributetConstants.CONTINUE_URL, ROOT);
		createProductList(model, cartData);
		setupCartPageRestorationData(model);
		clearSessionRestorationData();

		model.addAttribute("isOmsEnabled", Boolean.valueOf(getSiteConfigService().getBoolean("oms.enabled", false)));
		//TISST-13012
		if (StringUtils.isNotEmpty(cartData.getGuid()) && null != cartData.getTotalPrice()
				&& null != cartData.getTotalPriceWithConvCharge())
		{
			//TIS-404
			final String payNowInventoryCheck = getSessionService().getAttribute(
					MarketplacecheckoutaddonConstants.PAYNOWINVENTORYNOTPRESENT);

			final String payNowPromotionCheck = getSessionService().getAttribute(
					MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);

			// TISUTO-12 TISUTO-11
			final String inventoryReservationCheck = getSessionService().getAttribute(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID);

			final String pincodeServiceabiltyCheck = getSessionService().getAttribute(
					MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_MSG_SESSION_ID);

			final String deliveryModeErrorHandler = getSessionService().getAttribute(
					MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID);

			final String cartItemDelisted = getSessionService().getAttribute(
					MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID);

			//TISPRO-497
			final String cartAmountInvalid = getSessionService()
					.getAttribute(MarketplacecommerceservicesConstants.CARTAMOUNTINVALID);

			final String payNowCouponCheck = getSessionService().getAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID);

			//TISPRO-578
			final String cartInvalidCheck = getSessionService().getAttribute(
					MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID);

			// TISUTO-12 TISUTO-11
			final String orderInventoryReservationCheck = getSessionService().getAttribute(
					MarketplacecclientservicesConstants.OMS_ORDER_INVENTORY_RESV_SESSION_ID);

			//TISEE-3676
			if (StringUtils.isNotEmpty(cartItemDelisted)
					&& cartItemDelisted.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID);
				GlobalMessages.addErrorMessage(model, MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_MESSAGE);
			}
			//TISPRO-497
			else if (StringUtils.isNotEmpty(cartAmountInvalid)
					&& cartAmountInvalid.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecommerceservicesConstants.CARTAMOUNTINVALID);
				GlobalMessages.addErrorMessage(model, MarketplacecommerceservicesConstants.CART_TOTAL_INVALID_MESSAGE);
			}
			else if (StringUtils.isNotEmpty(payNowInventoryCheck)
					&& payNowInventoryCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.PAYNOWINVENTORYNOTPRESENT);
				GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.INVENTORYNOTAVAILABLE);
			}
			else if (StringUtils.isNotEmpty(payNowPromotionCheck)
					&& payNowPromotionCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);
				GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.PROMOTIONEXPIRED);
			}
			else if (StringUtils.isNotEmpty(inventoryReservationCheck)
					&& inventoryReservationCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID);
				GlobalMessages.addErrorMessage(model, MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_FAILURE_MESSAGE);
			}
			else if (StringUtils.isNotEmpty(pincodeServiceabiltyCheck)
					&& pincodeServiceabiltyCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_MSG_SESSION_ID);
				GlobalMessages.addErrorMessage(model, MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_FAILURE_MESSAGE);
			}
			else if (StringUtils.isNotEmpty(deliveryModeErrorHandler)
					&& deliveryModeErrorHandler.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID);
				GlobalMessages.addErrorMessage(model, MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_MESSAGE);
			}
			else if (StringUtils.isNotEmpty(payNowCouponCheck)
					&& payNowCouponCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.PAYNOWCOUPONINVALID);
				GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.COUPONINVALID);
			}
			//TISPRO-578
			else if (StringUtils.isNotEmpty(cartInvalidCheck)
					&& cartInvalidCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID);
				GlobalMessages.addErrorMessage(model, MarketplacecheckoutaddonConstants.CART_DELIVERYMODE_ADDRESS_INVALID_MSG);
			}
			else if (StringUtils.isNotEmpty(orderInventoryReservationCheck)
					&& orderInventoryReservationCheck.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecclientservicesConstants.OMS_ORDER_INVENTORY_RESV_SESSION_ID);
				GlobalMessages.addErrorMessage(model, MarketplacecclientservicesConstants.ORDER_INV_FAIL_MSG);
			}

		}
		//TISPT-174
		//populateTealiumData(model, cartData);
		//merge changes of tcs_dev_master and jewellery
		//final CartModel cartModel = getCartService().getSessionCart();
		GenericUtilityMethods.populateTealiumDataForCartCheckout(model, cartData);
	}


	/**
	 * Remove the session data of the cart restoration.
	 */
	private void clearSessionRestorationData()
	{
		LOG.debug("Entring into clearSessionRestorationData" + "Class NameclearSessionRestorationData :" + className);
		getSessionService().removeAttribute(WebConstants.CART_RESTORATION);
		getSessionService().removeAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS);
	}

	/**
	 * Prepare the restoration data and always display any modifications on the cart page.
	 *
	 * @param model
	 *
	 */
	private void setupCartPageRestorationData(final Model model) throws Exception
	{
		LOG.debug("Class NamesetupCartPageRestorationData :" + className + "Entring into setupCartPageRestorationData");
		if (getSessionService().getAttribute(WebConstants.CART_RESTORATION) != null)
		{
			if (getSessionService().getAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS) != null)
			{
				model.addAttribute("restorationErrorMsg", getSessionService()
						.getAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS));
			}
			else
			{
				model.addAttribute("restorationData", getSessionService().getAttribute(WebConstants.CART_RESTORATION));
			}
		}
		model.addAttribute("showModifications", Boolean.TRUE);
	}

	private boolean validateCart(final RedirectAttributes redirectModel) throws CommerceCartModificationException, Exception
	{
		LOG.debug("Entring into validateCart" + "Class NamevalidateCart :" + className);
		List<CartModificationData> modifications = new ArrayList<CartModificationData>();

		modifications = getMplCartFacade().validateCartData();

		if (CollectionUtils.isNotEmpty(modifications))
		{
			redirectModel.addFlashAttribute("validationData", modifications);



		}

		return (CollectionUtils.isNotEmpty(modifications)) ? true : false;
	}

	/**
	 * Cart Item Removal
	 *
	 *
	 *
	 */
	@RequestMapping(value = "/removeFromMinicart", method = RequestMethod.GET)
	public @ResponseBody String removeFromMinicart(final HttpServletRequest request) throws CommerceCartModificationException
	{
		LOG.debug("Entring into removeFromMinicart" + "Class NameremoveFromMinicart :" + className);
		final String entryNumberString = request.getParameter("entryNumber");
		String returnStatement = "fail";

		try
		{
			LOG.debug("Class NameremoveFromMinicar :" + className + "entry number is >>>>>>>>" + entryNumberString);
			final long entryNumber = Long.parseLong(entryNumberString);

			if (getMplCartFacade().hasEntries())
			{
				LOG.debug("Class NameremoveFromMinica :" + className + "#####Inside Remove Ajax call#####");
				final CartModificationData cartModification = getMplCartFacade().updateCartEntry(entryNumber, 0);

				if (cartModification.getQuantity() == 0)
				{
					LOG.debug("Class NameremoveFromMini :" + className + "#####Removed Product#####");
					//return "Item has been Removed From the cart";
					returnStatement = "Item has been Removed From the cart";
				}
				else
				{
					LOG.debug("Class NameremoveFromMin :" + className + "##########\t" + cartModification.getQuantity());
					returnStatement = "Could not removed item from cart";
				}
			}
		}
		catch (final CommerceCartModificationException ex)
		{
			LOG.error("CommerceCartModificationException while remove item from minicart ", ex);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions while remove item from minicart ", ex);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while remove item from minicart ", ex);
		}
		LOG.debug("Class NameremoveFromMi :" + className + "Could find any cart entries");
		return returnStatement;
	}

	/**
	 *
	 * @param defaultPinCodeId
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 * @throws CommerceCartModificationException
	 */
	@RequestMapping(value = "/setPincode", method = RequestMethod.GET)
	public String setPinCode(@RequestParam final String defaultPinCodeId, final Model model) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{
		LOG.debug("Entring into setPinCode" + "Class NamesetPinCode :" + className);
		if (StringUtil.isNotEmpty(defaultPinCodeId))
		{
			getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, defaultPinCodeId);
		}
		model.addAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, defaultPinCodeId);

		return REDIRECT_PREFIX + "/cart?pincode=" + defaultPinCodeId;
	}

	/**
	 * @Description :show default Pincode of the customer
	 * @parameter:Model
	 */
	@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
	private void showPincode(final Model model, final String defaultPinCodeId, final CartData cartData)
			throws CMSItemNotFoundException, EtailNonBusinessExceptions, EtailBusinessExceptions, Exception
	{
		LOG.debug("Entring into showPincode" + "Class NameshowPincod :" + className);
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();

		model.addAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, defaultPinCodeId);

		if ((cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
		{
			//final List<PinCodeResponseData> responseData = null; TISPT-104
			fullfillmentDataMap = getMplCartFacade().getFullfillmentMode(cartData);
			/*
			 * Commented as part of Performance fix TISPT-104 if (!StringUtil.isEmpty(defaultPinCodeId)) { responseData =
			 * getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartData); //deliveryModeDataMap =
			 * getMplCartFacade().getDeliveryMode(cartData, responseData); } else { LOG.debug(
			 * "Selected pincode is null or empty while cart page load"); //TISST-12585 //deliveryModeDataMap =
			 * mplCartFacade.getDeliveryMode(cartData, null); }
			 */
			model.addAttribute(ModelAttributetConstants.CART_FULFILMENTDATA, fullfillmentDataMap);
			//model.addAttribute(ModelAttributetConstants.CART_PRODUCT_DELIVERYMODE_MAP, deliveryModeDataMap); TISPT-104
			model.addAttribute(ModelAttributetConstants.CART_SELECTED_PINCODE, defaultPinCodeId);

			//TIS-390 Express checkout button available for the logged in customer
			if (getUserFacade().isAnonymousUser())
			{
				model.addAttribute("isLoggedIn", Boolean.FALSE);
			}
			else
			{
				model.addAttribute("isLoggedIn", Boolean.TRUE);
			}
		}

	}

	/**
	 * This is a GET method to check pincode serviceability
	 *
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHECKPINCODESERVICEABILITY, method = RequestMethod.GET)
	//@RequireHardLogIn
	public @ResponseBody JSONObject checkPincodeServiceability(
			@PathVariable(MarketplacecheckoutaddonConstants.PINCODE) final String selectedPincode, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		String returnStatement = null;
		final JSONObject jsonObject = new JSONObject();
		//TISSEC-11
		final String regex = "\\d{6}";
		//final CartModel cart = getCartService().getSessionCart();
		CartData cartData = null;
		int pincodeCookieMaxAge;
		final Cookie cookie = GenericUtilityMethods.getCookieByName(request, "pdpPincode");
		final String cookieMaxAge = getConfigurationService().getConfiguration().getString("pdpPincode.cookie.age");
		pincodeCookieMaxAge = (Integer.valueOf(cookieMaxAge)).intValue();
		final String domain = getConfigurationService().getConfiguration().getString("shared.cookies.domain");
		try
		{
			String isServicable = MarketplacecommerceservicesConstants.Y;

			if (selectedPincode.matches(regex))
			{
				LOG.debug("selectedPincode " + selectedPincode);
				ServicesUtil.validateParameterNotNull(selectedPincode, "pincode cannot be null");

				List<PinCodeResponseData> responseData = null;
				String jsonResponse = MarketplacecommerceservicesConstants.EMPTY;

				if (StringUtil.isNotEmpty(selectedPincode))
				{
					//TPR-6654
					if (cookie != null && cookie.getValue() != null)
					{
						cookie.setValue(selectedPincode);
						cookie.setMaxAge(pincodeCookieMaxAge);
						cookie.setPath("/");

						if (null != domain && !domain.equalsIgnoreCase("localhost"))
						{
							cookie.setSecure(true);
						}
						cookie.setDomain(domain);
						response.addCookie(cookie);
						getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
					}
					else
					{
						pdpPincodeCookie.addCookie(response, selectedPincode);
						getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
					}
				}
				try
				{
					cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);

					if (cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries()))
					{
						//if ((cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
						//{
						if (!StringUtil.isEmpty(selectedPincode))
						{
							responseData = getMplCartFacade().getOMSPincodeResponseData(selectedPincode, cartData, null);
							getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_RES, responseData); //CAR-126/128/129

						}

						if (responseData != null)
						{
							getSessionService().setAttribute(MarketplacecommerceservicesConstants.PINCODE_RESPONSE_DATA_TO_SESSION,
									responseData);

							for (PinCodeResponseData pinCodeResponseData : responseData)
							{

								if (pinCodeResponseData != null && pinCodeResponseData.getIsServicable() != null
										&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
								{
									isServicable = MarketplacecommerceservicesConstants.N;
									break;
								}
								else if (pinCodeResponseData != null && pinCodeResponseData.getIsServicable() != null
										&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
								{
									//  TISPRD-1951  START //

									// Checking whether inventory is availbale or not
									// if inventory is not available for particular delivery Mode
									// then removing that deliveryMode in Choose DeliveryMode Page
									try
									{
										//added for exchange TPR-1083
										pinCodeResponseData.setExchangePincode(selectedPincode);
										pinCodeResponseData = getMplCartFacade().getVlaidDeliveryModesByInventory(pinCodeResponseData,
												cartData);
									}
									catch (final Exception e)
									{
										LOG.error("Exception occured while checking inventory " + e.getCause());
									}
									//  TISPRD-1951  END //
								}
							}
						}
						else
						{
							isServicable = MarketplacecommerceservicesConstants.N;
						}
						if (isServicable.equals(MarketplacecommerceservicesConstants.Y))
						{
							CartModel cart = getCartService().getSessionCart();
							if (mplCartFacade.validatePincodeRestrictedPromoOnCartProduct(cart))
							{
								mplCartFacade.populatePinCodeData(cart, selectedPincode);
								cart = getMplCartFacade().getCalculatedCart(cart);
								getMplCartFacade().setCartSubTotal(cart);

								//To calculate discount percentage amount for display purpose
								// TPR-774-- Total MRP calculation and the Product percentage calculation
								getMplCartFacade().totalMrpCal(cart);
								cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
							}
							//TPR-970 changes

							final String var_prev = getSessionService().getAttribute("lastpercentage").toString();

							LOG.debug(">> var_prev :" + var_prev);

							//To calculate discount percentage amount for display purpose
							// TPR-774-- Total MRP calculation and the Product percentage calculation
							getMplCartFacade().totalMrpCal(cart);

							final String var_next = getSessionService().getAttribute("lastpercentage").toString();

							LOG.debug(">> var_next :" + var_next);

							if (!var_prev.equalsIgnoreCase(var_next))
							{
								LOG.debug("var_prev not equal to var_next :");
								jsonObject.put("isPincodeRestrictedPromotionPresent", true);
							}

							cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
							jsonObject.put("cartData", cartData);
							jsonObject.put("cartEntries", cartData.getEntries());

							//								getMplCartFacade().getCalculatedCart().getEntries()
							//								final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
							//								cartData.get
							//
							//								getMplCartFacade().setCartSubTotal();
						}
						final ObjectMapper objectMapper = new ObjectMapper();
						jsonResponse = objectMapper.writeValueAsString(responseData);
						//}
						getSessionService().setAttribute("isCartPincodeServiceable", isServicable);
					}

					LOG.debug(">> isServicable :" + isServicable + " >> json " + jsonResponse);
				}
				catch (final EtailNonBusinessExceptions ex)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(ex);
					LOG.error("EtailNonBusinessExceptions while checking pincode serviceabilty ", ex);
				}
				catch (final Exception ex)
				{
					LOG.error("Exception while checking pincode serviceabilty ", ex);
				}

				returnStatement = isServicable + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + selectedPincode
						+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + jsonResponse;
			}
			else
			{
				isServicable = MarketplacecommerceservicesConstants.N;
				returnStatement = isServicable;
			}
			jsonObject.put("pincodeData", returnStatement);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions while checkPincodeServiceability ", ex);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in checkPincodeServiceability ", ex);
		}
		return jsonObject;
	}

	/**
	 * @Description :show Addresses of the customer present in addressbook
	 * @parameter:Model
	 */

	private void showAddress(final Model model) throws CMSItemNotFoundException, EtailNonBusinessExceptions, Exception
	{
		Map<String, String> expressCheckoutAddressMap = new HashMap<String, String>();
		LOG.debug("Entring into showAddress" + "Class NameshowAddres :" + className);

		final List<AddressData> addressData1 = getUserFacade().getAddressBook();

		if (null != addressData1 && !addressData1.isEmpty())
		{
			LOG.debug("Class NameshowAddres :" + className + "We have found  some addres of current user");
			expressCheckoutAddressMap = getMplCartFacade().getAddress(addressData1); // Code moved to MplCartFacade
			model.addAttribute("Addresses", expressCheckoutAddressMap);
		}
		else
		{
			LOG.debug("Class NameshowAddre :" + className + "We have not found  some addres of current user");
			model.addAttribute("Addresses", null);
		}
	}

	/*
	 * @Description adding wishlist popup in cart page
	 * 
	 * @param String productCode,String wishName, model
	 */

	@ResponseBody
	@RequestMapping(value = "/addToWishListFromCart", method = RequestMethod.GET)
	public boolean addWishListsForCartPage(@RequestParam("product") final String productCode,
			@RequestParam("ussid") final String ussid, @RequestParam("wish") final String wishName, final Model model,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletRequest request,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletResponse response)
			throws CMSItemNotFoundException
	{

		LOG.debug("Entring into addWishListsForCartPage" + "Class NameaddWishListsForCartPage :" + className);
		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
		boolean add = false;
		try
		{
			final Wishlist2Model existingWishlist = wishlistFacade.getWishlistForName(wishName);
			if (null != existingWishlist)
			{
				wishlistFacade.addProductToWishlist(existingWishlist, productCode, ussid, Boolean.valueOf(Boolean.TRUE));
			}
			for (final Wishlist2EntryModel wlEntry : existingWishlist.getEntries())
			{
				final String product = wlEntry.getProduct().getCode();
				if (productCode.equals(product))
				{
					add = true;
					removeEntryByProductCode(productCode);
				}
			}
		}
		catch (final CMSItemNotFoundException cmsex)
		{
			LOG.error("CMSItemNotFoundException while adding to wishlist from cart ", cmsex);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("Exception while adding to wishlist from cart ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("Exception while adding to wishlist from cart ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while adding to wishlist from cart ", ex);
		}

		return add;
	}

	/*
	 * @Description showing wishlist popup in cart page
	 * 
	 * @param String productCode, model
	 */
	@ResponseBody
	@RequestMapping(value = "/wishlists", method = RequestMethod.GET)
	public List<WishlistData> showWishListsForCartPage(@RequestParam("productCode") final String productCode,
			@RequestParam("ussid") final String ussid, final Model model,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletRequest request,
			@SuppressWarnings(MarketplacecommerceservicesConstants.UNUSED) final HttpServletResponse response)
			throws CMSItemNotFoundException

	{
		LOG.debug("Entring into showWishListsForCartPage" + "Class NameshowWishListsForCartPag :" + className);
		model.addAttribute(ModelAttributetConstants.MY_ACCOUNT_FLAG, ModelAttributetConstants.N_CAPS_VAL);
		final UserModel user = userService.getCurrentUser();
		List<WishlistData> wishListData = null;

		//If the user is not logged in then ask customer to login.
		if (null != user.getName() && user.getName().equalsIgnoreCase(USER.ANONYMOUS_CUSTOMER))
		{
			wishListData = new ArrayList<WishlistData>();
			try

			{
				final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
				final int wishListSize = allWishlists.size();
				final String nameSet = ModelAttributetConstants.WISHLIST_NO + ModelAttributetConstants.UNDER_SCORE + "1";


				//check whether any wishlist exits for user or not or else create a new wishlist and add product to it
				if (wishListSize == 0)
				{
					//add product to new wishlist
					final Wishlist2Model createdWishlist = wishlistFacade.createNewWishlist(user, nameSet, productCode);

					wishlistFacade.addProductToWishlist(createdWishlist, productCode, ussid, Boolean.valueOf(Boolean.TRUE));
					final WishlistData wishData = new WishlistData();
					wishData.setParticularWishlistName(createdWishlist.getName());
					wishData.setProductCode(productCode);
					wishListData.add(wishData);
				}
				else
				{

					//view existing wishlists
					for (final Wishlist2Model wish : allWishlists)
					{
						final WishlistData wishList = new WishlistData();
						wishList.setParticularWishlistName(wish.getName());
						for (final Wishlist2EntryModel wlEntry : wish.getEntries())
						{
							if (wlEntry.getProduct().getCode().equals(productCode))
							{
								wishList.setProductCode(productCode);
							}
						}
						wishListData.add(wishList);
					}

				}
			}

			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
				LOG.error("Exception occured while showWishListsForCartPage ", e);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				LOG.error("Exception occured while showWishListsForCartPage ", e);
			}
			catch (final Exception ex)
			{
				LOG.error("Exception occured while showWishListsForCartPage ", ex);
			}
		}
		return wishListData;

	}


	/**
	 *
	 * @param ProductCode
	 */
	private void removeEntryByProductCode(final String ProductCode) throws EtailBusinessExceptions, EtailNonBusinessExceptions,
			Exception

	{
		LOG.debug("Entring into removeEntryByProductCode" + "Class NameremoveEntryByProductCode :" + className);
		final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);


		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				if (entry.getProduct().getCode().equalsIgnoreCase(ProductCode))
				{
					try
					{
						@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
						final CartModificationData cartModification = getMplCartFacade().updateCartEntry(entry.getEntryNumber(), 0);
						LOG.debug("Class NameremoveEntryByProductCod :" + className + "Comformation of cart delete"
								+ cartModification.getQuantity());
					}
					catch (final CommerceCartModificationException e)
					{
						LOG.error("Class NameremoveEntryByProductCo :" + className + "Error on cart delete" + e);
					}
				}
			}
		}

	}

	/**
	 * This is a Get ajax method to check pincode serviceability for a Address ID
	 *
	 * @return String
	 * @throws EtailNonBusinessExceptions
	 * @throws JSONException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHECKEXPRESSCHECKOUTPINOCDESERVICEABILITY, method = RequestMethod.GET)
	//@RequireHardLogIn
	public @ResponseBody JSONObject checkExpressCheckoutPincodeServiceability(
			@PathVariable(MarketplacecheckoutaddonConstants.SELECTEDADDRESSID) final String selectedAddressId,
			final HttpServletRequest request, final HttpServletResponse response) throws EtailNonBusinessExceptions, JSONException
	{
		LOG.debug("selectedAddressId " + selectedAddressId);
		final JSONObject jsonObject = new JSONObject();
		ServicesUtil.validateParameterNotNull(selectedAddressId, "Address Id cannot be null");
		String isServicable = MarketplacecommerceservicesConstants.Y;
		List<PinCodeResponseData> responseData = null;
		String jsonResponse = "";
		AddressData finaladdressData = new AddressData();
		String selectedPincode = "";
		//TPR-6654
		int pincodeCookieMaxAge;
		final Cookie cookie = GenericUtilityMethods.getCookieByName(request, "pdpPincode");
		final String cookieMaxAge = getConfigurationService().getConfiguration().getString("pdpPincode.cookie.age");
		pincodeCookieMaxAge = (Integer.valueOf(cookieMaxAge)).intValue();
		final String domain = getConfigurationService().getConfiguration().getString("shared.cookies.domain");
		try
		{
			for (final AddressData addressData : accountAddressFacade.getAddressBook())
			{
				if (null != addressData && null != addressData.getId() && addressData.getId().equals(selectedAddressId))
				{
					finaladdressData = addressData;
					break;
				}
			}

			selectedPincode = finaladdressData.getPostalCode();

			if (!StringUtil.isEmpty(selectedPincode))
			{
				//TPR-6654
				if (cookie != null && cookie.getValue() != null)
				{
					cookie.setValue(selectedPincode);
					cookie.setMaxAge(pincodeCookieMaxAge);
					cookie.setPath("/");

					if (null != domain && !domain.equalsIgnoreCase("localhost"))
					{
						cookie.setSecure(true);
					}
					cookie.setDomain(domain);
					response.addCookie(cookie);
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
				}
				else
				{
					pdpPincodeCookie.addCookie(response, selectedPincode);
					getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
				}
				//getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
			}

			CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			if ((cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
			{
				if (StringUtil.isNotEmpty(selectedPincode))
				{
					responseData = getMplCartFacade().getOMSPincodeResponseData(selectedPincode, cartData, null);
				}

				for (PinCodeResponseData pinCodeResponseData : responseData)
				{

					if (pinCodeResponseData != null
							&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
					{
						isServicable = MarketplacecommerceservicesConstants.N;
						break;
					}
					else if (pinCodeResponseData != null
							&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
					{
						//  TISPRD-1951  START //

						// Checking whether inventory is availbale or not
						// if inventory is not available for particular delivery Mode
						// then removing that deliveryMode in Choose DeliveryMode Page
						try
						{
							pinCodeResponseData = getMplCartFacade().getVlaidDeliveryModesByInventory(pinCodeResponseData, cartData);
						}
						catch (final Exception e)
						{
							LOG.error("Exception occured while checking inventory " + e.getCause());
						}
						//  TISPRD-1951  END //
					}
				}
				//if ((selectedPincode == null || selectedPincode.isEmpty()) 	|| (!selectedPincode.isEmpty() && responseData.size() == 0))
				if (StringUtil.isEmpty(selectedPincode)
						|| (StringUtil.isNotEmpty(selectedPincode) && CollectionUtils.isEmpty(responseData)))

				{
					isServicable = MarketplacecommerceservicesConstants.N;
					//		returnStatement = isServicable;
				}

				if (isServicable.equals(MarketplacecommerceservicesConstants.Y))
				{
					final CartModel cartModel = getCartService().getSessionCart();
					getMplCartFacade().getCalculatedCart(cartModel);
					cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
					jsonObject.put("cartData", cartData);
					jsonObject.put("cartEntries", cartData.getEntries());
				}

			}
			final ObjectMapper objectMapper = new ObjectMapper();
			jsonResponse = objectMapper.writeValueAsString(responseData);
			LOG.debug(">> isServicable :" + isServicable + " >> json " + jsonResponse);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions while checking pincode serviceabilty ", ex);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while checking pincode serviceabilty ", ex);
		}
		final String returnStatement = isServicable + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + selectedPincode
				+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + jsonResponse;
		jsonObject.put("pincodeData", returnStatement);
		return jsonObject;

	}

	/*
	 * TISEE-432
	 */
	private String fetchPincode(final boolean isUserAnym) throws EtailNonBusinessExceptions, Exception
	{
		final String pdpPinCode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_PDP);
		final String cartPincodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
		String selectedPincode = MarketplacecommerceservicesConstants.EMPTY;

		if (StringUtils.isNotEmpty(pdpPinCode))
		{
			getSessionService().removeAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_PDP);
			selectedPincode = pdpPinCode;
		}
		else if (StringUtils.isNotEmpty(cartPincodeId))
		{
			selectedPincode = cartPincodeId;
		}
		else if (!isUserAnym)
		{
			final AddressData defaultAddress = getUserFacade().getDefaultAddress();
			if (defaultAddress != null && defaultAddress.getPostalCode() != null)
			{
				selectedPincode = defaultAddress.getPostalCode();
			}
		}

		return selectedPincode;
	}

	/**
	 * Method to handel Exceptions in Server-Side from Network
	 *
	 * @return void
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.NETWORK_ERROR, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody void checkNetworkError(final String errorDetails)
	{
		try
		{
			LOG.error("**** errorDetails ****** " + errorDetails);
			LOG.debug("**** errorDetails ****** " + errorDetails);
			LOG.info("**** errorDetails ****** " + errorDetails);
		}

		catch (final Exception ex)
		{

			LOG.error("NETWORK_ERROR: ", ex);
			LOG.debug("NETWORK_ERROR: ", ex);
			LOG.info("NETWORK_ERROR: ", ex);
		}
	}

	// Public getter used in a test
	@Override
	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@ModelAttribute("showCheckoutStrategies")
	public boolean isCheckoutStrategyVisible()
	{
		return getSiteConfigService().getBoolean(MessageConstants.SHOW_CHECKOUT_STRATEGY_OPTIONS, false);
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
	 * @return the userFacade
	 */
	public UserFacade getUserFacade()
	{
		return userFacade;
	}

	/**
	 * @param userFacade
	 *           the userFacade to set
	 */
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}

	/**
	 * @return the frontEndErrorHelper
	 */
	public FrontEndErrorHelper getFrontEndErrorHelper()
	{
		return frontEndErrorHelper;
	}

	/**
	 * @param frontEndErrorHelper
	 *           the frontEndErrorHelper to set
	 */
	public void setFrontEndErrorHelper(final FrontEndErrorHelper frontEndErrorHelper)
	{
		this.frontEndErrorHelper = frontEndErrorHelper;
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

	//TPR-1083
	/**
	 * @description method is used to remove exchange from Cart when the pincode is not serviceable
	 * @return Json Object
	 * @throws JSONException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.REMOVEEXCHANGEFROMCART, method = RequestMethod.GET)
	public @ResponseBody JSONObject removeAllExchangeFromCart(
			@RequestParam(ControllerConstants.Views.Fragments.Cart.pincode) final String pincode) throws JSONException
	{
		final JSONObject jsonObj = new JSONObject();
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Inside Remove from Cart...");
			}
			final CartModel cart = getCartService().getSessionCart();

			final Boolean exchangeCart = cart.getExchangeAppliedCart();
			if (exchangeCart.booleanValue() && !exchangeGuideFacade.isBackwardServiceble(pincode))
			{
				exchangeGuideFacade.removeExchangefromCart(cart);
				jsonObj.put("exchangeItemsRemoved", "true");

			}




		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions Removing Exchange from Cart ", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  Removing Exchange from Cart ", e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while Removing Exchange from Cart" + e);
			jsonObj.put("displaymessage", "jsonExceptionMsg");
			jsonObj.put("type", "errorCode");
		}
		return jsonObj;
	}
	//TPR-1083
}
