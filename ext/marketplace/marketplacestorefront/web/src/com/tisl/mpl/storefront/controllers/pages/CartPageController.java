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
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateQuantityForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
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
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.Constants.USER;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.data.WishlistData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.storefront.constants.MessageConstants;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import com.tisl.mpl.storefront.controllers.helpers.FrontEndErrorHelper;
import com.tisl.mpl.util.ExceptionUtil;

import net.sourceforge.pmd.util.StringUtil;


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
	private Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
	private Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
	private Map<String, String> sellerInfoMap = new HashMap<String, String>();


	@Resource(name = "frontEndErrorHelper")
	private FrontEndErrorHelper frontEndErrorHelper;
	//	@Resource(name = "productService")
	//	private ProductService productService;

	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;
	@Autowired
	private UserService userService;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	//@Resource(name = "acceleratorCheckoutFacade")
	//private AcceleratorCheckoutFacade checkoutFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Autowired
	private WishlistFacade wishlistFacade;

	@Autowired
	private ProductFacade productFacade;

	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

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

	/*
	 * Display the cart page
	 */
	@SuppressWarnings(
	{ MarketplacecommerceservicesConstants.BOXING, "deprecation" })
	@RequestMapping(method = RequestMethod.GET)
	public String showCart(final Model model, @RequestParam(value = "ussid", required = false) final String ussid,
			@RequestParam(value = "pincode", required = false) final String pinCode)
					throws CMSItemNotFoundException, CommerceCartModificationException, CalculationException
	{
		LOG.debug("Entering into showCart" + "Class Nameshowcart :" + className + "pinCode " + pinCode);
		try
		{
			CartData cartDataOnLoad = mplCartFacade.getSessionCartWithEntryOrdering(true);

			//TISST-13012
			if (StringUtils.isNotEmpty(cartDataOnLoad.getGuid()))
			{
				final CartModel serviceCart = getCartService().getSessionCart();
				if (!serviceCart.getChannel().equals(SalesApplication.WEB))
				{
					serviceCart.setChannel(SalesApplication.WEB);
					getModelService().save(serviceCart);
				}

				//TISEE-3676 & TISEE-4013
				final boolean deListedStatus = getMplCartFacade().isCartEntryDelisted(serviceCart);
				LOG.debug("Cart Delisted Status " + deListedStatus);

				final CartModel cart = mplCartFacade.removeDeliveryMode(serviceCart);

				//TISST-13010
				getMplCartFacade().setCartSubTotal();

				final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);

				final boolean isUserAnym = getUserFacade().isAnonymousUser();
				model.addAttribute("isUserAnym", isUserAnym);

				//TISEE-432
				final String selectedPinCode = fetchPincode(isUserAnym);

				checkCartDataChange(cart, cartDataOnLoad, cartData, model);
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
				else
				{
					LOG.debug("CartPageController : product quanity is empty");
				}
				cartDataOnLoad = cartData;
			}

			prepareDataForPage(model, cartDataOnLoad);

			// for MSD

			final String msdjsURL = getConfigurationService().getConfiguration().getString("msd.js.url");
			final Boolean isMSDEnabled = Boolean.valueOf(getConfigurationService().getConfiguration().getString("msd.enabled"));
			model.addAttribute(ModelAttributetConstants.MSD_JS_URL, msdjsURL);
			model.addAttribute(ModelAttributetConstants.IS_MSD_ENABLED, isMSDEnabled);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{

			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		return ControllerConstants.Views.Pages.Cart.CartPage;
	}

	/**
	 *
	 * @param cartDataOld
	 * @param cartDataLatest
	 * @param model
	 */
	private void checkCartDataChange(final CartModel cart, final CartData cartDataOld, final CartData cartDataLatest,
			final Model model)
	{
		try
		{
			final Map<String, String> priceModified = new HashMap<String, String>();
			final Map<String, String> priceModifiedMssg = new HashMap<String, String>();
			final Map<String, PriceData> basePriceMap = new HashMap<String, PriceData>();
			final Map<String, String> promoModified = new HashMap<String, String>();
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
										.equalsIgnoreCase(entryOld.getSelectedSellerInformation().getUssid())
								&& !entryOld.isGiveAway() && !entryLatest.isGiveAway())
						{

							final BigDecimal updatedTotalPrice = new BigDecimal(entryLatest.getTotalPrice().getValue().toString());
							final BigDecimal oldTotalPrice = new BigDecimal(entryOld.getTotalPrice().getValue().toString());
							if (entryLatest.isIsBOGOapplied())
							{
								final Long qty = entryLatest.getQuantity();
								@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
								final Long priceForStrikeOff = ((entryLatest.getBasePrice().getValue().longValue()) * qty);
								final BigDecimal strikeOffPrice = new BigDecimal(priceForStrikeOff.longValue());
								final PriceData strikeoffprice = priceDataFactory.create(PriceDataType.BUY, strikeOffPrice,
										MarketplaceFacadesConstants.INR);
								model.addAttribute("strikeoffprice", strikeoffprice);
							}

							final int res = updatedTotalPrice.compareTo(oldTotalPrice);
							if (res != 0)
							{
								priceModified.put(entryLatest.getEntryNumber().toString(),
										cart.getCurrency().getSymbol() + entryOld.getTotalPrice().getValue());
								priceModifiedMssg.put(entryLatest.getEntryNumber().toString(),
										"Sorry! The price of this item has changed.");
							}

							final double oldPromoValue = (entryOld.getQuantity().doubleValue()
									* Double.parseDouble(entryOld.getBasePrice().getValue().toString()))
									- Double.parseDouble(entryOld.getTotalPrice().getValue().toString());
							final double latestPromoValue = (entryLatest.getQuantity().doubleValue()
									* Double.parseDouble(entryLatest.getBasePrice().getValue().toString()))
									- Double.parseDouble(entryLatest.getTotalPrice().getValue().toString());

							if (oldPromoValue != latestPromoValue)
							{
								promoModified.put(entryLatest.getEntryNumber().toString(), "Promotion has been modified");
							}
							//TISEE-535
							final BigDecimal basetotal = new BigDecimal(
									entryLatest.getBasePrice().getValue().doubleValue() * entryLatest.getQuantity());
							final PriceData baseTotalPrice = priceDataFactory.create(PriceDataType.BUY, basetotal,
									MarketplaceFacadesConstants.INR);

							basePriceMap.put(entryLatest.getEntryNumber().toString(), baseTotalPrice);

							model.addAttribute(ModelAttributetConstants.BASEPRICEMAP, basePriceMap);

							if (entryLatest.getCartLevelDisc() != null && entryLatest.getCartLevelDisc().getValue() != null)
							{
								if (entryOld.getCartLevelDisc() != null && entryOld.getCartLevelDisc().getValue() != null)
								{
									final double oldCartLevelDiscount = Double
											.parseDouble(entryOld.getCartLevelDisc().getValue().toString());
									final double latestCartLevelDiscount = Double
											.parseDouble(entryLatest.getCartLevelDisc().getValue().toString());

									//Adding to model
									compareCartLevelDiscount(oldCartLevelDiscount, latestCartLevelDiscount, model);
								}
								else
								{
									model.addAttribute("cartLevelDiscountModified", "Cart Promotion has been modified");
								}
							}
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
			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
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
		try
		{
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

			if (!getMplCartFacade().hasEntries() || validateCart(redirectModel))
			{
				LOG.debug("Class Namecartcheck :" + className + "Missing or empty cart");
				return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{

			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		return REDIRECT_PREFIX + "/checkout";
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
		LOG.debug("Entering into initCheck" + "Class Nameinitcheck :" + className);
		try
		{
			SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

			if (!getMplCartFacade().hasEntries())
			{
				LOG.debug("Class Nameinit :" + className + "Missing or empty cart");

				// No session cart or empty session cart. Bounce back to the cart page.
				return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
			}

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
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		catch (final Exception e)
		{

			ExceptionUtil
					.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
			getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
			return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
		}
		return REDIRECT_PREFIX + "/checkout";
	}



	@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateCartQuantities(@RequestParam("entryNumber") final long entryNumber, final Model model,
			@Valid final UpdateQuantityForm form, final BindingResult bindingResult, final HttpServletRequest request,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		LOG.debug("Entering into updateCartQuantities" + "Class NameupdateCartQuantities :" + className);
		final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
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
		else if (getMplCartFacade().hasEntries())
		{
			try
			{
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
						GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER,
								"basket.page.message.update.reducedNumberOfItemsAdded.noStock", new Object[]
						{ cartModification.getEntry().getProduct().getName(),
								request.getRequestURL().append(cartModification.getEntry().getProduct().getUrl()) });
					}
				}
				// Redirect to the cart page on update success so that the browser doesn't re-post again
				return REDIRECT_PREFIX + MarketplacecommerceservicesConstants.CART_URL;
			}
			catch (final CommerceCartModificationException ex)
			{
				LOG.error("Couldn't update product with the entry number: " + entryNumber + ".", ex);
				ExceptionUtil.etailBusinessExceptionHandler(new EtailBusinessExceptions(MarketplacecommerceservicesConstants.E0000),
						null);
				getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
				return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
			}
			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
				getFrontEndErrorHelper().callBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
				return ControllerConstants.Views.Pages.Error.CustomEtailBusinessErrorPage;
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
				getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
				return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
			}
			catch (final Exception e)
			{

				ExceptionUtil.etailNonBusinessExceptionHandler(
						new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000));
				getFrontEndErrorHelper().callNonBusinessError(model, MessageConstants.SYSTEM_ERROR_PAGE_NON_BUSINESS);
				return ControllerConstants.Views.Pages.Error.CustomEtailNonBusinessErrorPage;
			}
		}

		prepareDataForPage(model, cartData);
		return ControllerConstants.Views.Pages.Cart.CartPage;
	}

	private void createProductList(final Model model, final CartData cartData) throws CMSItemNotFoundException
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

		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.cart"));
		model.addAttribute("pageType", PageType.CART.name());
	}

	@RequestMapping(value = "/giftlist", method = RequestMethod.GET)
	public String showGiftList(final Model model) throws CMSItemNotFoundException
	{

		final boolean isUserAnym = getUserFacade().isAnonymousUser();
		if (!isUserAnym)
		{
			final String defaultPinCodeId = fetchPincode(isUserAnym);

			final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			if (StringUtils.isNotEmpty(cartData.getGuid()))
			{
				final Map<String, String> ussidMap = new HashMap<String, String>();
				Map<String, List<String>> giftYourselfDeliveryModeDataMap = new HashMap<String, List<String>>();

				final List<ProductData> productDataList = new ArrayList<ProductData>();
				List<Wishlist2EntryModel> entryModels = new ArrayList<Wishlist2EntryModel>();

				final int minimum_gift_quantity = getSiteConfigService().getInt(MessageConstants.MINIMUM_GIFT_QUANTIY, 0);
				LOG.debug("Class NameprepareDataForPag :" + className + " minimum_gift_quantity :" + minimum_gift_quantity);
				final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
				entryModels = getMplCartFacade().getGiftYourselfDetails(minimum_gift_quantity, allWishlists, defaultPinCodeId); // Code moved to Facade and Impl

				for (final Wishlist2EntryModel entryModel : entryModels)
				{
					boolean flag = true;
					//TISEE-6376
					if (entryModel.getProduct() != null)
					{
						ProductData productData = productFacade.getProductForOptions(entryModel.getProduct(),
								Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
										ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.REVIEW,
										ProductOption.DELIVERY_MODE_AVAILABILITY));
						productData = wishlistFacade.getBuyBoxPrice(entryModel.getUssid(), productData);

						final SellerInformationModel sellerInfoForWishlist = mplSellerInformationService
								.getSellerDetail(entryModel.getUssid());

						final String sellerName = sellerInfoForWishlist.getSellerName();
						if (entryModel.getUssid() != null && null != sellerName)
						{
							ussidMap.put(productData.getCode(), entryModel.getUssid());
							model.addAttribute("ussidMap", ussidMap);
							model.addAttribute("sellerName", sellerName);
						}
						for (final OrderEntryData cart : cartData.getEntries())
						{
							if ((cart.getSelectedUssid().equals(entryModel.getUssid())))
							{
								flag = false;
								break;
							}
						}
						if (flag)
						{
							productDataList.add(productData);
							model.addAttribute("ProductDatas", productDataList);
						}
					}
				}

				/* TISEE-435 : New Code Added */
				giftYourselfDeliveryModeDataMap = getMplCartFacade().checkPincodeGiftCartData(defaultPinCodeId, entryModels);
				if (MapUtils.isNotEmpty(giftYourselfDeliveryModeDataMap))
				{
					model.addAttribute("giftYourselfDeliveryModeDataMap", giftYourselfDeliveryModeDataMap);
				}
				else
				{
					model.addAttribute("giftYourselfDeliveryModeDataMap", null);
				}

				final ArrayList<Integer> quantityConfigurationList = getMplCartFacade().getQuantityConfiguratioList();
				if (CollectionUtils.isNotEmpty(quantityConfigurationList))
				{
					model.addAttribute("configuredQuantityList", quantityConfigurationList);
				}
				else
				{
					LOG.debug("CartPageController : product quanity is empty");
				}
				/* TISEE-435 : New Code Added section ends */
			}
		}
		return ControllerConstants.Views.Fragments.Cart.GiftList;

	}

	/**
	 * Get Product Delivery Modes
	 */
	private void prepareDataForPage(final Model model, final CartData cartData) throws CMSItemNotFoundException
	{
		LOG.debug("Entring into prepareDataForPage" + "Class NameprepareDataForPage :" + className);

		model.addAttribute(ModelAttributetConstants.CONTINUE_URL, ROOT);
		createProductList(model, cartData);
		setupCartPageRestorationData(model);
		clearSessionRestorationData();

		model.addAttribute("isOmsEnabled", Boolean.valueOf(getSiteConfigService().getBoolean("oms.enabled", false)));
		//model.addAttribute("supportedCountries", getMplCartFacade().getDeliveryCountries());
		//model.addAttribute("expressCheckoutAllowed", Boolean.valueOf(checkoutFacade.isExpressCheckoutAllowedForCart()));
		//model.addAttribute("taxEstimationEnabled", Boolean.valueOf(checkoutFacade.isTaxEstimationEnabledForCart()));

		//TISST-13012
		if (StringUtils.isNotEmpty(cartData.getGuid()))
		{
			//TIS-404
			final String payNowInventoryCheck = getSessionService()
					.getAttribute(MarketplacecheckoutaddonConstants.PAYNOWINVENTORYNOTPRESENT);

			final String payNowPromotionCheck = getSessionService()
					.getAttribute(MarketplacecheckoutaddonConstants.PAYNOWPROMOTIONEXPIRED);

			// TISUTO-12 TISUTO-11
			final String inventoryReservationCheck = getSessionService()
					.getAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID);

			final String pincodeServiceabiltyCheck = getSessionService()
					.getAttribute(MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_MSG_SESSION_ID);

			final String deliveryModeErrorHandler = getSessionService()
					.getAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID);

			final String cartItemDelisted = getSessionService()
					.getAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID);
			//TISEE-3676
			if (StringUtils.isNotEmpty(cartItemDelisted)
					&& cartItemDelisted.equalsIgnoreCase(MarketplacecommerceservicesConstants.TRUE))
			{
				getSessionService().removeAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID);
				GlobalMessages.addErrorMessage(model, MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_MESSAGE);
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
		}
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
	private void setupCartPageRestorationData(final Model model)
	{
		LOG.debug("Class NamesetupCartPageRestorationData :" + className + "Entring into setupCartPageRestorationData");
		if (getSessionService().getAttribute(WebConstants.CART_RESTORATION) != null)
		{
			if (getSessionService().getAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS) != null)
			{
				model.addAttribute("restorationErrorMsg",
						getSessionService().getAttribute(WebConstants.CART_RESTORATION_ERROR_STATUS));
			}
			else
			{
				model.addAttribute("restorationData", getSessionService().getAttribute(WebConstants.CART_RESTORATION));
			}
		}
		model.addAttribute("showModifications", Boolean.TRUE);
	}

	private boolean validateCart(final RedirectAttributes redirectModel)
	{
		LOG.debug("Entring into validateCart" + "Class NamevalidateCart :" + className);
		List<CartModificationData> modifications = new ArrayList<CartModificationData>();
		try
		{
			modifications = getMplCartFacade().validateCartData();
		}
		catch (final CommerceCartModificationException e)
		{
			LOG.error("Failed to validate cart", e);
		}
		if (!modifications.isEmpty())
		{
			redirectModel.addFlashAttribute("validationData", modifications);

			// Invalid cart. Bounce back to the cart page.
			return true;
		}
		return false;
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
		LOG.debug("Class NameremoveFromMinicar :" + className + "entry number is >>>>>>>>" + entryNumberString);
		final long entryNumber = Long.parseLong(entryNumberString);
		if (getMplCartFacade().hasEntries())
		{
			LOG.debug("Class NameremoveFromMinica :" + className + "#####Inside Remove Ajax call#####");
			final CartModificationData cartModification = getMplCartFacade().updateCartEntry(entryNumber, 0);

			if (cartModification.getQuantity() == 0)
			{
				LOG.debug("Class NameremoveFromMini :" + className + "#####Removed Product#####");
				return "Item has been Removed From the cart";
			}
			else
			{
				LOG.debug("Class NameremoveFromMin :" + className + "##########\t" + cartModification.getQuantity());
				return "Could not removed item from cart";
			}
		}
		LOG.debug("Class NameremoveFromMi :" + className + "Could find any cart entries");
		return "fail";
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
	public String setPinCode(@RequestParam final String defaultPinCodeId, final Model model)
			throws CMSItemNotFoundException, CommerceCartModificationException
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
			throws CMSItemNotFoundException
	{
		LOG.debug("Entring into showPincode" + "Class NameshowPincod :" + className);

		model.addAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, defaultPinCodeId);

		if ((cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
		{
			List<PinCodeResponseData> responseData = null;
			fullfillmentDataMap = getMplCartFacade().getFullfillmentMode(cartData);
			if (!StringUtil.isEmpty(defaultPinCodeId))
			{
				responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartData);
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartData, responseData);
			}
			else
			{
				LOG.debug("Selected pincode is null or empty while cart page load");
				//TISST-12585
				//deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartData, null);
			}
			model.addAttribute(ModelAttributetConstants.CART_FULFILMENTDATA, fullfillmentDataMap);
			model.addAttribute(ModelAttributetConstants.CART_PRODUCT_DELIVERYMODE_MAP, deliveryModeDataMap);
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
	@RequireHardLogIn
	public @ResponseBody String checkPincodeServiceability(
			@PathVariable(MarketplacecheckoutaddonConstants.PINCODE) final String selectedPincode) throws EtailNonBusinessExceptions
	{
		//TISSEC-11
		final String regex = "\\d{6}";

		String isServicable = MarketplacecommerceservicesConstants.Y;
		if (selectedPincode.matches(regex))
		{
			LOG.debug("selectedPincode " + selectedPincode);
			ServicesUtil.validateParameterNotNull(selectedPincode, "pincode cannot be null");

			List<PinCodeResponseData> responseData = null;
			String jsonResponse = "";

			if (StringUtil.isNotEmpty(selectedPincode))
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
			}
			try
			{
				final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
				if (cartData != null)
				{
					if ((cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
					{
						if (!StringUtil.isEmpty(selectedPincode))
						{
							responseData = getMplCartFacade().getOMSPincodeResponseData(selectedPincode, cartData);
						}
						if (responseData != null)
						{
							for (final PinCodeResponseData pinCodeResponseData : responseData)
							{
								if (pinCodeResponseData != null && pinCodeResponseData.getIsServicable() != null
										&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
								{
									isServicable = MarketplacecommerceservicesConstants.N;
									break;
								}
							}

						}
						else
						{
							isServicable = MarketplacecommerceservicesConstants.N;
						}

						final ObjectMapper objectMapper = new ObjectMapper();
						jsonResponse = objectMapper.writeValueAsString(responseData);
					}
				}

				LOG.debug(">> isServicable :" + isServicable + " >> json " + jsonResponse);
			}
			catch (final EtailNonBusinessExceptions ex)
			{
				LOG.error("EtailNonBusinessExceptions while checking pincode serviceabilty " + ex);
			}
			catch (final Exception ex)
			{
				LOG.error("Exception while checking pincode serviceabilty " + ex);
			}

			return isServicable + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + selectedPincode
					+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + jsonResponse;

		}
		else
		{
			isServicable = MarketplacecommerceservicesConstants.N;
			return isServicable;
		}


	}

	/**
	 * @Description :show Addresses of the customer present in addressbook
	 * @parameter:Model
	 */

	private void showAddress(final Model model) throws CMSItemNotFoundException
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
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
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

		//If the user is not logged in then ask customer to login.
		if (null != user.getName() && user.getName().equalsIgnoreCase(USER.ANONYMOUS_CUSTOMER))
		{
			return null;
		}

		final List<WishlistData> wishListData = new ArrayList<WishlistData>();
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
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return wishListData;
	}


	/**
	 *
	 * @param ProductCode
	 */
	private void removeEntryByProductCode(final String ProductCode)
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
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHECKEXPRESSCHECKOUTPINOCDESERVICEABILITY, method = RequestMethod.GET)
	@RequireHardLogIn
	public @ResponseBody String checkExpressCheckoutPincodeServiceability(
			@PathVariable(MarketplacecheckoutaddonConstants.SELECTEDADDRESSID) final String selectedAddressId)
					throws EtailNonBusinessExceptions
	{
		LOG.debug("selectedAddressId " + selectedAddressId);
		ServicesUtil.validateParameterNotNull(selectedAddressId, "Address Id cannot be null");
		String isServicable = MarketplacecommerceservicesConstants.Y;
		List<PinCodeResponseData> responseData = null;
		String jsonResponse = "";
		AddressData finaladdressData = new AddressData();
		String selectedPincode = "";
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
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
			}

			final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			if ((cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
			{
				if (!StringUtil.isEmpty(selectedPincode))
				{
					responseData = getMplCartFacade().getOMSPincodeResponseData(selectedPincode, cartData);
				}

				for (final PinCodeResponseData pinCodeResponseData : responseData)
				{
					if (pinCodeResponseData != null
							&& pinCodeResponseData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
					{
						isServicable = MarketplacecommerceservicesConstants.N;
						break;
					}
				}


				if ((selectedPincode == null || selectedPincode.isEmpty())
						|| (!selectedPincode.isEmpty() && responseData.size() == 0))
				{
					isServicable = MarketplacecommerceservicesConstants.N;
				}
			}
			final ObjectMapper objectMapper = new ObjectMapper();
			jsonResponse = objectMapper.writeValueAsString(responseData);
			LOG.debug(">> isServicable :" + isServicable + " >> json " + jsonResponse);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			LOG.error("EtailNonBusinessExceptions while checking pincode serviceabilty " + ex);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception while checking pincode serviceabilty " + ex);
		}

		return isServicable + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + selectedPincode
				+ MarketplacecheckoutaddonConstants.STRINGSEPARATOR + jsonResponse;

	}

	/*
	 * TISEE-432
	 */
	private String fetchPincode(final boolean isUserAnym)
	{
		final String pdpPinCode = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE_PDP);
		final String cartPincodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
		String selectedPincode = "";
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


}
