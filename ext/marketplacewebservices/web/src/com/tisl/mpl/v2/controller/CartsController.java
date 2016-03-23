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
package com.tisl.mpl.v2.controller;


import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModesData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.promotion.CommercePromotionRestrictionFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.promotion.CommercePromotionRestrictionException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.cache.CacheControl;
import de.hybris.platform.commercewebservicescommons.cache.CacheControlDirective;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.voucher.VoucherListWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartAddressException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.LowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.granule.json.JSON;
import com.tisl.mpl.cart.impl.CommerceWebServicesCartFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.exceptions.InvalidPaymentInfoException;
import com.tisl.mpl.exceptions.NoCheckoutCartException;
import com.tisl.mpl.exceptions.UnsupportedDeliveryModeException;
import com.tisl.mpl.exceptions.UnsupportedRequestException;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplCouponWebFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCommerceCartServiceImpl;
import com.tisl.mpl.order.data.CartDataList;
import com.tisl.mpl.order.data.OrderEntryDataList;
import com.tisl.mpl.product.data.PromotionResultDataList;
import com.tisl.mpl.request.support.impl.PaymentProviderRequestSupportedStrategy;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.stock.CommerceStockFacade;
import com.tisl.mpl.user.data.AddressDataList;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.voucher.data.VoucherDataList;
import com.tisl.mpl.wsdto.ApplyCouponsDTO;
import com.tisl.mpl.wsdto.BillingAddressWsDTO;
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.MplCartPinCodeResponseWsDTO;
import com.tisl.mpl.wsdto.ReleaseCouponsDTO;
import com.tisl.mpl.wsdto.ReservationListWsDTO;
import com.tisl.mpl.wsdto.ValidateOtpWsDto;
import com.tisl.mpl.wsdto.WebSerResponseWsDTO;


/**
 *
 * @pathparam userId User identifier or one of the literals below :
 *            <ul>
 *            <li>'current' for currently authenticated user</li>
 *            <li>'anonymous' for anonymous user</li>
 *            </ul>
 * @pathparam cartId Cart identifier
 *            <ul>
 *            <li>cart code for logged in user</li>
 *            <li>cart guid for anonymous user</li>
 *            <li>'current' for the last modified cart</li>
 *            </ul>
 * @pathparam entryNumber Entry number. Zero-based numbering.
 * @pathparam promotionId Promotion identifier (code)
 * @pathparam voucherId Voucher identifier (code)
 */
@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
@CacheControl(directive = CacheControlDirective.NO_CACHE)
public class CartsController extends BaseCommerceController
{
	private final static Logger LOG = Logger.getLogger(CartsController.class);
	private final static long DEFAULT_PRODUCT_QUANTITY = 1;
	@Resource(name = "commercePromotionRestrictionFacade")
	private CommercePromotionRestrictionFacade commercePromotionRestrictionFacade;
	@Resource(name = "commerceStockFacade")
	private CommerceStockFacade commerceStockFacade;
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "pointOfServiceValidator")
	private Validator pointOfServiceValidator;
	@Resource(name = "orderEntryCreateValidator")
	private Validator orderEntryCreateValidator;
	@Resource(name = "orderEntryUpdateValidator")
	private Validator orderEntryUpdateValidator;
	@Resource(name = "orderEntryReplaceValidator")
	private Validator orderEntryReplaceValidator;
	@Resource(name = "greaterThanZeroValidator")
	private Validator greaterThanZeroValidator;
	@Resource(name = "paymentProviderRequestSupportedStrategy")
	private PaymentProviderRequestSupportedStrategy paymentProviderRequestSupportedStrategy;
	@Resource(name = "saveCartFacade")
	private SaveCartFacade saveCartFacade;
	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MplPaymentWebFacade mplPaymentWebFacade;
	@Autowired
	private MplCartFacade mplCartFacade;
	@Autowired
	private MplCommerceCartServiceImpl mplCommerceCartService;
	@Autowired
	private MplCartWebService mplCartWebService;
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Resource
	private UserService userService;
	@Autowired
	private CustomerAccountService customerAccountService;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private MplCustomerProfileService mplCustomerProfileService;
	@Autowired
	private MplCommerceCartCalculationStrategy calculationStrategy;

	@Autowired
	private CommerceCartService commerceCartService;
	@Autowired
	private ExtendedUserService extUserService;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Autowired
	private WishlistFacade wishlistFacade;

	private static final String APPLICATION_TYPE = "application/json";


	@Autowired
	private MplCouponWebFacade mplCouponWebFacade;

	@Autowired
	private MplCouponFacade mplCouponFacade;

	/**
	 * @return the calculationStrategy
	 */
	public MplCommerceCartCalculationStrategy getCalculationStrategy()
	{
		return calculationStrategy;
	}

	/**
	 * @param calculationStrategy
	 *           the calculationStrategy to set
	 */
	public void setCalculationStrategy(final MplCommerceCartCalculationStrategy calculationStrategy)
	{
		this.calculationStrategy = calculationStrategy;
	}

	private static final String CUSTOMER = "ROLE_CUSTOMERGROUP";
	private static final String CUSTOMERMANAGER = "ROLE_CUSTOMERMANAGERGROUP";
	private static final String TRUSTED_CLIENT = "ROLE_TRUSTED_CLIENT";
	private static final String ROLE_CLIENT = "ROLE_CLIENT";
	private static final String ROLE_GUEST = "ROLE_GUEST";
	private static final String PICK_UP_STORE = "pickupStore";
	private static final String ENTRY = "entry";
	private static final String CART_URL = "/{cartId}/entries/{entryNumber}";
	private static final String ENTRY_NUMBER = "entryNumber";
	private static final String ADDRESS_DELIVERY = "/{cartId}/addresses/delivery";
	private static final String PRODUCT = "Product [";

	@Resource(name = "discountUtility")
	private DiscountUtility discountUtility;
	@Autowired
	private CartService cartService;
	//@Autowired
	//private CommerceCartService commerceCartService;
	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;

	//private static final String DEPRECATION = "deprecation";

	/**
	 * @return the mplExtendedCartConverter
	 */
	public Converter<CartModel, CartData> getMplExtendedCartConverter()
	{
		return mplExtendedCartConverter;
	}

	/**
	 * @param mplExtendedCartConverter
	 *           the mplExtendedCartConverter to set
	 */
	public void setMplExtendedCartConverter(final Converter<CartModel, CartData> mplExtendedCartConverter)
	{
		this.mplExtendedCartConverter = mplExtendedCartConverter;
	}

	private static CartModificationData mergeCartModificationData(final CartModificationData cmd1, final CartModificationData cmd2)
	{
		if ((cmd1 == null) && (cmd2 == null))
		{
			return new CartModificationData();
		}
		if (cmd1 == null)
		{
			return cmd2;
		}
		if (cmd2 == null)
		{
			return cmd1;
		}
		final CartModificationData cmd = new CartModificationData();
		cmd.setDeliveryModeChanged(Boolean
				.valueOf(Boolean.TRUE.equals(cmd1.getDeliveryModeChanged()) || Boolean.TRUE.equals(cmd2.getDeliveryModeChanged())));
		cmd.setEntry(cmd2.getEntry());
		cmd.setQuantity(cmd2.getQuantity());
		cmd.setQuantityAdded(cmd1.getQuantityAdded() + cmd2.getQuantityAdded());
		cmd.setStatusCode(cmd2.getStatusCode());
		return cmd;
	}

	private static OrderEntryData getCartEntryForNumber(final CartData cart, final long number) throws CartEntryException
	{
		final List<OrderEntryData> entries = cart.getEntries();
		if (entries != null && !entries.isEmpty())
		{
			final Integer requestedEntryNumber = Integer.valueOf((int) number);
			for (final OrderEntryData entry : entries)
			{
				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
				{
					return entry;
				}
			}
		}
		throw new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number));
	}

	private static OrderEntryData getCartEntry(final CartData cart, final String productCode, final String pickupStore)
	{
		for (final OrderEntryData oed : cart.getEntries())
		{
			if (oed.getProduct().getCode().equals(productCode))
			{
				if (pickupStore == null && oed.getDeliveryPointOfService() == null)
				{
					return oed;
				}
				//Not Null Check Removed For Sonar Fix
				else if (null != oed.getDeliveryPointOfService().getName()
						&& oed.getDeliveryPointOfService().getName().equals(pickupStore))
				{
					return oed;
				}
			}
		}
		return null;
	}

	private static void validateForAmbiguousPositions(final CartData currentCart, final OrderEntryData currentEntry,
			final String newPickupStore) throws CommerceCartModificationException
	{
		final OrderEntryData entryToBeModified = getCartEntry(currentCart, currentEntry.getProduct().getCode(), newPickupStore);
		if (entryToBeModified != null && !entryToBeModified.equals(currentEntry))
		{
			throw new CartEntryException(
					"Ambiguous cart entries! Entry number " + currentEntry.getEntryNumber()
							+ " after change would be the same as entry " + entryToBeModified.getEntryNumber(),
					CartEntryException.AMBIGIOUS_ENTRY, entryToBeModified.getEntryNumber().toString());
		}
	}

	//	/**
	//	 * Lists all customer carts. Allowed only for non-anonymous users.
	//	 *
	//	 * @formparam savedCartsOnly optional parameter. If the parameter is provided and its value is true only saved carts
	//	 *            are returned.
	//	 * @formparam currentPage optional pagination parameter in case of savedCartsOnly == true. Default value 0.
	//	 * @formparam pageSize optional {@link PaginationData} parameter in case of savedCartsOnly == true. Default value 20.
	//	 * @formparam sort optional sort criterion in case of savedCartsOnly == true. No default value.
	//	 * @queryparam fields Response configuration (list of fields, which should be returned in response).
	//	 * @return All customer carts
	//	 */
	//	@RequestMapping(method = RequestMethod.GET)
	//	@ResponseBody
	//	public CartListWsDTO getCarts(
	//			@RequestParam(required = false, defaultValue = FieldSetLevelHelper.BASIC_LEVEL) final String fields,
	//			@RequestParam(required = false, defaultValue = "false") final boolean savedCartsOnly,
	//			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
	//			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
	//			@RequestParam(required = false) final String sort)
	//	{
	//		if (userFacade.isAnonymousUser())
	//		{
	//			throw new AccessDeniedException("Access is denied");
	//		}
	//
	//		final CartDataList cartDataList = new CartDataList();
	//
	//		if (savedCartsOnly)
	//		{
	//			final PageableData pageableData = new PageableData();
	//			pageableData.setCurrentPage(currentPage);
	//			pageableData.setPageSize(pageSize);
	//			pageableData.setSort(sort);
	//			cartDataList.setCarts(saveCartFacade.getSavedCartsForCurrentUser(pageableData, null).getResults());
	//		}
	//		else
	//		{
	//			cartDataList.setCarts(cartFacade.getCartsForCurrentUser());
	//		}
	//
	//		return dataMapper.map(cartDataList, CartListWsDTO.class, fields);
	//	}

	/**
	 * The response paramater of this out of the box service was modified in order to send only the latest cart details
	 * to mobility, as per their request. Lists all customer carts. Allowed only for non-anonymous users.
	 *
	 * @formparam savedCartsOnly optional parameter. If the parameter is provided and its value is true only saved carts
	 *            are returned.
	 * @formparam currentPage optional pagination parameter in case of savedCartsOnly == true. Default value 0.
	 * @formparam pageSize optional {@link PaginationData} parameter in case of savedCartsOnly == true. Default value 20.
	 * @formparam sort optional sort criterion in case of savedCartsOnly == true. No default value.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response).
	 * @return All customer carts
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public WebSerResponseWsDTO getCarts(@RequestParam(required = false, defaultValue = "false") final boolean savedCartsOnly,
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@RequestParam(required = false) final String sort)
	{
		int count = 0;
		if (userFacade.isAnonymousUser())
		{
			throw new AccessDeniedException("Access is denied");
		}
		final WebSerResponseWsDTO response = new WebSerResponseWsDTO();
		final CartDataList cartDataList = new CartDataList();
		List<CartData> cartsList = null;
		if (savedCartsOnly)
		{
			final PageableData pageableData = new PageableData();
			pageableData.setCurrentPage(currentPage);
			pageableData.setPageSize(pageSize);
			pageableData.setSort(sort);
			cartDataList.setCarts(saveCartFacade.getSavedCartsForCurrentUser(pageableData, null).getResults());
		}
		else
		{
			cartDataList.setCarts(cartFacade.getCartsForCurrentUser());
		}
		if (null != cartDataList.getCarts())
		{
			cartsList = cartDataList.getCarts();
		}
		for (final CartData cartData : cartsList)
		{
			if (null != cartData)
			{
				if (StringUtils.isNotEmpty(cartData.getCode()))
				{
					response.setCode(cartData.getCode());
				}
				if (StringUtils.isNotEmpty(cartData.getGuid()))
				{
					response.setGuid(cartData.getGuid());
				}
				for (final OrderEntryData entry : cartData.getEntries())
				{
					if (!entry.isGiveAway())
					{
						count++;
					}
				}
				response.setCount(String.valueOf(count));
			}
			break;
		}

		return response;
	}

	/**
	 * Returns the cart with a given identifier.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Details of cart and it's entries
	 */
	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	@ResponseBody
	public CartWsDTO getCart(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		// CartMatchingFilter sets current cart based on cartId, so we can return cart from the session
		return dataMapper.map(getSessionCart(), CartWsDTO.class, fields);
	}

	/**
	 * This function is out of box and has been commented to meet mobility requirement.
	 *
	 * Creates a new cart or restores an anonymous cart as a user's cart (if an old Cart Id is given in the request)
	 *
	 * @formparam oldCartId Anonymous cart GUID
	 * @formparam toMergeCartGuid User's cart GUID to merge anonymous cart to
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Created cart data
	 */
	//	@RequestMapping(method = RequestMethod.POST)
	//	@ResponseStatus(HttpStatus.CREATED)
	//	@ResponseBody
	//	public CartWsDTO createCart(@RequestParam(required = false) final String oldCartId,
	//
	//	@RequestParam(required = false) String toMergeCartGuid,
	//
	//	@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	//	{
	//		if (LOG.isDebugEnabled())
	//		{
	//			LOG.debug("createCart");
	//		}
	//
	//		if (StringUtils.isNotEmpty(oldCartId))
	//		{
	//			if (userFacade.isAnonymousUser())
	//			{
	//				throw new RuntimeException("Anonymous user is not allowed to copy cart!");
	//			}
	//
	//			if (!isCartAnonymous(oldCartId))
	//			{
	//				throw new CartException("Cart is not anonymous", CartException.CANNOT_RESTORE, oldCartId);
	//			}
	//
	//			if (StringUtils.isEmpty(toMergeCartGuid))
	//			{
	//				toMergeCartGuid = getSessionCart().getGuid();
	//			}
	//			else
	//			{
	//				if (!isUserCart(toMergeCartGuid))
	//				{
	//					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE, toMergeCartGuid);
	//				}
	//			}
	//
	//			try
	//			{
	//				cartFacade.restoreAnonymousCartAndMerge(oldCartId, toMergeCartGuid);
	//				return dataMapper.map(getSessionCart(), CartWsDTO.class, fields);
	//			}
	//			catch (final CommerceCartMergingException e)
	//			{
	//				throw new CartException("Couldn't merge carts", CartException.CANNOT_MERGE, e);
	//			}
	//			catch (final CommerceCartRestorationException e)
	//			{
	//				throw new CartException("Couldn't restore cart", CartException.CANNOT_RESTORE, e);
	//			}
	//		}
	//		else
	//		{
	//			if (StringUtils.isNotEmpty(toMergeCartGuid))
	//			{
	//				if (!isUserCart(toMergeCartGuid))
	//				{
	//					throw new CartException("Cart is not current user's cart", CartException.CANNOT_RESTORE, toMergeCartGuid);
	//				}
	//
	//				try
	//				{
	//					cartFacade.restoreSavedCart(toMergeCartGuid);
	//					return dataMapper.map(getSessionCart(), CartWsDTO.class, fields);
	//				}
	//				catch (final CommerceCartRestorationException e)
	//				{
	//					throw new CartException("Couldn't restore cart", CartException.CANNOT_RESTORE, oldCartId, e);
	//				}
	//
	//			}
	//			return dataMapper.map(getSessionCart(), CartWsDTO.class, fields);
	//		}
	//	}

	//OOB functions moved to MplCartWebService
	//private boolean isUserCart(final String toMergeCartGuid)
	//	{
	//		if (cartFacade instanceof CommerceWebServicesCartFacade)
	//		{
	//			final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) cartFacade;
	//			return commerceWebServicesCartFacade.isCurrentUserCart(toMergeCartGuid);
	//		}
	//		return true;
	//	}
	//
	//	private boolean isCartAnonymous(final String cartGuid)
	//	{
	//		if (cartFacade instanceof CommerceWebServicesCartFacade)
	//		{
	//			final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) cartFacade;
	//			return commerceWebServicesCartFacade.isAnonymousUserCart(cartGuid);
	//		}
	//		return true;
	//	}

	/**
	 * Deletes a cart with a given cart id.
	 */
	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteCart()
	{
		cartFacade.removeSessionCart();
	}

	/**
	 * Assigns an email to the cart. This step is required to make a guest checkout.
	 *
	 * @formparam email Email of the guest user. It will be used during checkout process
	 * @throws de.hybris.platform.commerceservices.customer.DuplicateUidException
	 */
	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ ROLE_CLIENT, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/email", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void guestLogin(@RequestParam final String email) throws DuplicateUidException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("createGuestUserForAnonymousCheckout: email=" + sanitize(email));
		}

		if (!EmailValidator.getInstance().isValid(email))
		{
			throw new RequestParameterException("Email [" + sanitize(email) + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, "login");
		}

		customerFacade.createGuestUserForAnonymousCheckout(email, "guest");
	}

	/**
	 * Returns cart entries.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Cart entries list
	 */
	@RequestMapping(value = "/{cartId}/entries", method = RequestMethod.GET)
	@ResponseBody
	public OrderEntryListWsDTO getCartEntries(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getCartEntries");
		}
		final OrderEntryDataList dataList = new OrderEntryDataList();
		dataList.setOrderEntries(getSessionCart().getEntries());
		return dataMapper.map(dataList, OrderEntryListWsDTO.class, fields);
	}

	/**
	 * Adds a product to the cart.
	 *
	 * @formparam code Code of the product to be added to cart. Product look-up is performed for the current product
	 *            catalog version.
	 * @formparam qty Quantity of product.
	 * @formparam pickupStore Name of the store where product will be picked. Set only if want to pick up from a store.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about cart modification.
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 * @throws WebserviceValidationException
	 *            When store given in pickupStore parameter doesn't exist
	 * @throws ProductLowStockException
	 *            When product is out of stock in store (when pickupStore parameter is filled)
	 * @throws StockSystemException
	 *            When there is no information about stock for stores (when pickupStore parameter is filled).
	 */
	@RequestMapping(value = "/{cartId}/entries", method = RequestMethod.POST)
	@ResponseBody
	public CartModificationWsDTO addCartEntry(@PathVariable final String baseSiteId,
			@RequestParam(required = true) final String code, @RequestParam(required = false, defaultValue = "1") final long qty,
			@RequestParam(required = false) final String pickupStore,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException,
					StockSystemException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("addCartEntry: " + logParam("code", code) + ", " + logParam("qty", qty) + ", "
					+ logParam(PICK_UP_STORE, pickupStore));
		}

		if (StringUtils.isNotEmpty(pickupStore))
		{
			validate(pickupStore, PICK_UP_STORE, pointOfServiceValidator);
		}

		return addCartEntryInternal(baseSiteId, code, qty, pickupStore, fields);
	}

	private CartModificationWsDTO addCartEntryInternal(final String baseSiteId, final String code, final long qty,
			final String pickupStore, final String fields) throws CommerceCartModificationException
	{
		final CartModificationData cartModificationData;
		if (StringUtils.isNotEmpty(pickupStore))
		{
			validateIfProductIsInStockInPOS(baseSiteId, code, pickupStore, null);
			cartModificationData = cartFacade.addToCart(code, qty, pickupStore);
		}
		else
		{
			validateIfProductIsInStockOnline(baseSiteId, code, null);
			cartModificationData = cartFacade.addToCart(code, qty);
		}
		return dataMapper.map(cartModificationData, CartModificationWsDTO.class, fields);
	}

	/**
	 * Adds a product to the cart.
	 *
	 * @param entry
	 *           Request body parameter (DTO in xml or json format) which contains details like : product code
	 *           (product.code), quantity of product (quantity), pickup store name (deliveryPointOfService.name)
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @bodyparams entry,quantity,deliveryPointOfService.name,product.code
	 * @return Information about cart modification.
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 * @throws WebserviceValidationException
	 *            When there is no product code value When store given in pickupStore parameter doesn't exist
	 * @throws ProductLowStockException
	 *            When product is out of stock in store (when pickupStore parameter is filled)
	 * @throws StockSystemException
	 *            When there is no information about stock for stores (when pickupStore parameter is filled).
	 */
	@RequestMapping(value = "/{cartId}/entries", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public CartModificationWsDTO addCartEntry(@PathVariable final String baseSiteId, @RequestBody final OrderEntryWsDTO entry,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException,
					StockSystemException
	{
		if (entry.getQuantity() == null)
		{
			entry.setQuantity(Long.valueOf(DEFAULT_PRODUCT_QUANTITY));
		}

		validate(entry, ENTRY, orderEntryCreateValidator);

		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return addCartEntryInternal(baseSiteId, entry.getProduct().getCode(), entry.getQuantity().longValue(), pickupStore, fields);
	}

	/**
	 * Returns the details of the cart entries.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Cart entry data
	 * @throws CartEntryException
	 *            When entry with given number doesn't exist
	 */
	@RequestMapping(value = CART_URL, method = RequestMethod.GET)
	@ResponseBody
	public OrderEntryWsDTO getCartEntry(@PathVariable final long entryNumber,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getCartEntry: " + logParam(ENTRY_NUMBER, entryNumber));
		}
		final OrderEntryData orderEntry = getCartEntryForNumber(getSessionCart(), entryNumber);
		return dataMapper.map(orderEntry, OrderEntryWsDTO.class, fields);
	}

	/**
	 * Updates the quantity of a single cart entry and details of the store where the cart entry will be picked.
	 * Attributes not provided in request will be defined again (set to null or default)
	 *
	 * @formparam qty Quantity of product.
	 * @formparam pickupStore Name of the store where product will be picked. Set only if want to pick up from a store.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about cart modification
	 * @throws CartEntryException
	 *            When entry with given number doesn't exist or in case of ambiguity of cart entries
	 * @throws WebserviceValidationException
	 *            When store given in pickupStore parameter doesn't exist
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 * @throws ProductLowStockException
	 *            When product is out of stock in store (when pickupStore parameter is filled)
	 * @throws StockSystemException
	 *            When there is no information about stock for stores (when pickupStore parameter is filled).
	 */
	@RequestMapping(value = CART_URL, method = RequestMethod.PUT)
	@ResponseBody
	public CartModificationWsDTO setCartEntry(@PathVariable final String baseSiteId, @PathVariable final long entryNumber,
			@RequestParam(required = true) final Long qty, @RequestParam(required = false) final String pickupStore,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("setCartEntry: " + logParam(ENTRY_NUMBER, entryNumber) + ", " + logParam("qty", qty) + ", "
					+ logParam(PICK_UP_STORE, pickupStore));
		}
		final CartData cart = getSessionCart();
		final OrderEntryData orderEntry = getCartEntryForNumber(cart, entryNumber);
		if (!StringUtils.isEmpty(pickupStore))
		{
			validate(pickupStore, PICK_UP_STORE, pointOfServiceValidator);
		}

		return updateCartEntryInternal(baseSiteId, cart, orderEntry, qty, pickupStore, fields, true);
	}

	private CartModificationWsDTO updateCartEntryInternal(final String baseSiteId, final CartData cart,
			final OrderEntryData orderEntry, final Long qty, final String pickupStore, final String fields, final boolean putMode)
					throws CommerceCartModificationException
	{
		final long entryNumber = orderEntry.getEntryNumber().longValue();
		final String productCode = orderEntry.getProduct().getCode();
		final PointOfServiceData currentPointOfService = orderEntry.getDeliveryPointOfService();

		CartModificationData cartModificationData1 = null;
		CartModificationData cartModificationData2 = null;

		if (!StringUtils.isEmpty(pickupStore))
		{
			if (currentPointOfService == null || !currentPointOfService.getName().equals(pickupStore))
			{
				//was 'shipping mode' or store is changed
				validateForAmbiguousPositions(cart, orderEntry, pickupStore);
				validateIfProductIsInStockInPOS(baseSiteId, productCode, pickupStore, Long.valueOf(entryNumber));
				cartModificationData1 = cartFacade.updateCartEntry(entryNumber, pickupStore);
			}
		}
		else if (putMode && currentPointOfService != null)
		{
			//was 'pickup in store', now switch to 'shipping mode'
			validateForAmbiguousPositions(cart, orderEntry, pickupStore);
			validateIfProductIsInStockOnline(baseSiteId, productCode, Long.valueOf(entryNumber));
			cartModificationData1 = cartFacade.updateCartEntry(entryNumber, pickupStore);
		}

		if (qty != null)
		{
			cartModificationData2 = cartFacade.updateCartEntry(entryNumber, qty.longValue());
		}

		return dataMapper.map(mergeCartModificationData(cartModificationData1, cartModificationData2), CartModificationWsDTO.class,
				fields);
	}

	/**
	 * Updates the quantity of a single cart entry and details of the store where the cart entry will be picked.
	 * Attributes not provided in request will be defined again (set to null or default)
	 *
	 * @param entry
	 *           Request body parameter (DTO in xml or json format) which contains details like : quantity of product
	 *           (quantity), pickup store name (deliveryPointOfService.name)
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @bodyparams entry,quantity,deliveryPointOfService.name,product.code
	 * @return Information about cart modification
	 * @throws CartEntryException
	 *            When entry with given number doesn't exist or in case of ambiguity of cart entries
	 * @throws WebserviceValidationException
	 *            When store given in pickupStore parameter doesn't exist
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 * @throws ProductLowStockException
	 *            When product is out of stock in store (when pickupStore parameter is filled)
	 * @throws StockSystemException
	 *            When there is no information about stock for stores (when pickupStore parameter is filled).
	 */
	@RequestMapping(value = CART_URL, method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public CartModificationWsDTO setCartEntry(@PathVariable final String baseSiteId, @PathVariable final long entryNumber,
			@RequestBody final OrderEntryWsDTO entry,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException
	{
		final CartData cart = getSessionCart();
		final OrderEntryData orderEntry = getCartEntryForNumber(cart, entryNumber);
		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();

		validateCartEntryForReplace(orderEntry, entry);

		return updateCartEntryInternal(baseSiteId, cart, orderEntry, entry.getQuantity(), pickupStore, fields, true);
	}

	private void validateCartEntryForReplace(final OrderEntryData oryginalEntry, final OrderEntryWsDTO entry)
	{
		final String productCode = oryginalEntry.getProduct().getCode();
		final Errors errors = new BeanPropertyBindingResult(entry, ENTRY);
		if (entry.getProduct() != null && entry.getProduct().getCode() != null && !entry.getProduct().getCode().equals(productCode))
		{
			errors.reject("cartEntry.productCodeNotMatch");
			throw new WebserviceValidationException(errors);
		}

		validate(entry, ENTRY, orderEntryReplaceValidator);
	}

	/**
	 * Updates the quantity of a single cart entry and details of the store where the cart entry will be picked.
	 *
	 * @formparam qty Quantity of product.
	 * @formparam pickupStore Name of the store where product will be picked. Set only if want to pick up from a store.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about cart modification
	 * @throws CartEntryException
	 *            When entry with given number doesn't exist or in case of ambiguity of cart entries
	 * @throws WebserviceValidationException
	 *            When store given in pickupStore parameter doesn't exist
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 * @throws ProductLowStockException
	 *            When product is out of stock in store (when pickupStore parameter is filled)
	 * @throws StockSystemException
	 *            When there is no information about stock for stores (when pickupStore parameter is filled).
	 */
	@RequestMapping(value = CART_URL, method = RequestMethod.PATCH)
	@ResponseBody
	public CartModificationWsDTO updateCartEntry(@PathVariable final String baseSiteId, @PathVariable final long entryNumber,
			@RequestParam(required = false) final Long qty, @RequestParam(required = false) final String pickupStore,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateCartEntry: " + logParam(ENTRY_NUMBER, entryNumber) + ", " + logParam("qty", qty) + ", "
					+ logParam(PICK_UP_STORE, pickupStore));
		}

		final CartData cart = getSessionCart();
		final OrderEntryData orderEntry = getCartEntryForNumber(cart, entryNumber);

		if (qty == null && StringUtils.isEmpty(pickupStore))
		{
			throw new RequestParameterException("At least one parameter (qty,pickupStore) should be set!",
					RequestParameterException.MISSING);
		}

		if (qty != null)
		{
			validate(qty, "quantity", greaterThanZeroValidator);
		}

		if (pickupStore != null)
		{
			validate(pickupStore, PICK_UP_STORE, pointOfServiceValidator);
		}

		return updateCartEntryInternal(baseSiteId, cart, orderEntry, qty, pickupStore, fields, false);
	}

	/**
	 * Updates the quantity of a single cart entry and details of the store where the cart entry will be picked.
	 *
	 * @param entry
	 *           Request body parameter (DTO in xml or json format) which contains details like : quantity of product
	 *           (quantity), pickup store name (deliveryPointOfService.name)
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @bodyparams entry,quantity,deliveryPointOfService.name,product.code
	 * @return Information about cart modification
	 * @throws CartEntryException
	 *            When entry with given number doesn't exist or in case of ambiguity of cart entries
	 * @throws WebserviceValidationException
	 *            When store given in pickupStore parameter doesn't exist
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 * @throws ProductLowStockException
	 *            When product is out of stock in store (when pickupStore parameter is filled)
	 * @throws StockSystemException
	 *            When there is no information about stock for stores (when pickupStore parameter is filled).
	 */
	@RequestMapping(value = CART_URL, method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public CartModificationWsDTO updateCartEntry(@PathVariable final String baseSiteId, @PathVariable final long entryNumber,
			@RequestBody final OrderEntryWsDTO entry,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException
	{
		final CartData cart = getSessionCart();
		final OrderEntryData orderEntry = getCartEntryForNumber(cart, entryNumber);

		final String productCode = orderEntry.getProduct().getCode();
		final Errors errors = new BeanPropertyBindingResult(entry, ENTRY);
		if (entry.getProduct() != null && entry.getProduct().getCode() != null && !entry.getProduct().getCode().equals(productCode))
		{
			errors.reject("cartEntry.productCodeNotMatch");
			throw new WebserviceValidationException(errors);
		}

		if (entry.getQuantity() == null)
		{
			entry.setQuantity(orderEntry.getQuantity());
		}

		validate(entry, ENTRY, orderEntryUpdateValidator);

		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return updateCartEntryInternal(baseSiteId, cart, orderEntry, entry.getQuantity(), pickupStore, fields, false);
	}

	/**
	 * Deletes cart entry.
	 *
	 * @throws CartEntryException
	 *            When entry with given number doesn't exist
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 */
	@RequestMapping(value = CART_URL, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeCartEntry(@PathVariable final long entryNumber) throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removeCartEntry: " + logParam(ENTRY_NUMBER, entryNumber));
		}

		final CartData cart = getSessionCart();
		getCartEntryForNumber(cart, entryNumber);
		cartFacade.updateCartEntry(entryNumber, 0);
	}

	/**
	 * Creates an address and assigns it to the cart as the delivery address.
	 *
	 * @formparam firstName Customer's first name. This parameter is required.
	 * @formparam lastName Customer's last name. This parameter is required.
	 * @formparam titleCode Customer's title code. This parameter is required. For a list of codes, see
	 *            /{baseSiteId}/titles resource
	 * @formparam country.isocode Country isocode. This parameter is required and have influence on how rest of
	 *            parameters are validated (e.g. if parameters are required : line1,line2,town,postalCode,region.isocode)
	 * @formparam line1 First part of address. If this parameter is required depends on country (usually it is required).
	 * @formparam line2 Second part of address. If this parameter is required depends on country (usually it is not
	 *            required)
	 * @formparam town Town name. If this parameter is required depends on country (usually it is required)
	 * @formparam postalCode Postal code. If this parameter is required depends on country (usually it is required)
	 * @formparam region.isocode Isocode for region. If this parameter is required depends on country.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Created address
	 * @throws WebserviceValidationException
	 *            When address parameters are incorrect
	 */
	@Secured(
	{ ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = ADDRESS_DELIVERY, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public AddressWsDTO createAndSetAddress(final HttpServletRequest request,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws WebserviceValidationException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("createAddress");
		}
		final AddressData addressData = super.createAddressInternal(request);
		final String addressId = addressData.getId();
		super.setCartDeliveryAddressInternal(addressId);
		return dataMapper.map(addressData, AddressWsDTO.class, fields);
	}

	/**
	 * Creates an address and assigns it to the cart as the delivery address.
	 *
	 * @param address
	 *           Request body parameter (DTO in xml or json format) which contains details like : Customer's first
	 *           name(firstName), Customer's last name(lastName), Customer's title code(titleCode),
	 *           country(country.isocode), first part of address(line1) , second part of address(line2), town (town),
	 *           postal code(postalCode), region (region.isocode)
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @bodyparams titleCode,firstName,lastName,line1,line2,town,postalCode,country(isocode),region(isocode),
	 *             defaultAddress >>>>>>> refs/remotes/origin/Release1
	 * @return Created address
	 * @throws WebserviceValidationException
	 *            When address parameters are incorrect
	 */
	@Secured(
	{ ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = ADDRESS_DELIVERY, method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public AddressWsDTO createAndSetAddress(@RequestBody final AddressWsDTO address,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws WebserviceValidationException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("createAddress");
		}
		validate(address, "address", addressDTOValidator);
		AddressData addressData = dataMapper.map(address, AddressData.class,
				"titleCode,firstName,lastName,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress");
		addressData = createAddressInternal(addressData);
		setCartDeliveryAddressInternal(addressData.getId());
		return dataMapper.map(addressData, AddressWsDTO.class, fields);
	}

	/**
	 * Sets a delivery address for the cart. The address country must be placed among the delivery countries of the
	 * current base store.
	 *
	 * @formparam addressId Address identifier
	 * @throws CartAddressException
	 *            When address with given id is not valid or doesn't exists
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = ADDRESS_DELIVERY, method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void setCartDeliveryAddress(@RequestParam(required = true) final String addressId) throws NoCheckoutCartException
	{
		super.setCartDeliveryAddressInternal(addressId);
	}

	/**
	 * Removes the delivery address from the cart.
	 *
	 * @throws CartException
	 *            When removing delivery address failed
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = ADDRESS_DELIVERY, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeCartDeliveryAddress()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removeDeliveryAddress");
		}
		if (!checkoutFacade.removeDeliveryAddress())
		{
			throw new CartException("Cannot reset address!", CartException.CANNOT_RESET_ADDRESS);
		}
	}

	/**
	 * Returns the delivery mode selected for the cart.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Delivery mode selected for the cart
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/deliverymode", method = RequestMethod.GET)
	@ResponseBody
	public DeliveryModeWsDTO getCartDeliveryMode(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getCartDeliveryMode");
		}
		return dataMapper.map(getSessionCart().getDeliveryMode(), DeliveryModeWsDTO.class, fields);
	}

	/**
	 * Sets the delivery mode with a given identifier for the cart.
	 *
	 * @formparam deliveryModeId Delivery mode identifier (code)
	 * @throws UnsupportedDeliveryModeException
	 *            When the delivery mode does not exist or when the delivery address is not set for the cart
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/deliverymode", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void setCartDeliveryMode(@RequestParam(required = true) final String deliveryModeId)
			throws UnsupportedDeliveryModeException
	{
		super.setCartDeliveryModeInternal(deliveryModeId);
	}

	/**
	 * Removes the delivery mode from the cart.
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/deliverymode", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removeDeliveryMode()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removeDeliveryMode");
		}
		if (!checkoutFacade.removeDeliveryMode())
		{
			throw new CartException("Cannot reset delivery mode!", CartException.CANNOT_RESET_DELIVERYMODE);
		}
	}

	/**
	 * Returns all delivery modes supported for the current base store and cart delivery address. A delivery address must
	 * be set for the cart, otherwise an empty list will be returned.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return All supported delivery modes
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/deliverymodes", method = RequestMethod.GET)
	@ResponseBody
	public DeliveryModeListWsDTO getSupportedDeliveryModes(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getSupportedDeliveryModes");
		}
		final DeliveryModesData deliveryModesData = new DeliveryModesData();
		deliveryModesData.setDeliveryModes(checkoutFacade.getSupportedDeliveryModes());
		final DeliveryModeListWsDTO dto = dataMapper.map(deliveryModesData, DeliveryModeListWsDTO.class, fields);
		return dto;
	}

	/**
	 * Defines details of a new credit card payment details and assigns the payment to the cart.
	 *
	 * @formparam accountHolderName Name on card. This parameter is required.
	 * @formparam cardNumber Card number. This parameter is required.
	 * @formparam cardType Card type. This parameter is required. Call GET /{baseSiteId}/cardtypes beforehand to see what
	 *            card types are supported
	 * @formparam expiryMonth Month of expiry date. This parameter is required.
	 * @formparam expiryYear Year of expiry date. This parameter is required.
	 * @formparam issueNumber
	 * @formparam startMonth
	 * @formparam startYear
	 * @formparam subscriptionId
	 * @formparam saved Parameter defines if the payment details should be saved for the customer and than could be
	 *            reused for future orders.
	 * @formparam defaultPaymentInfo Parameter defines if the payment details should be used as default for customer.
	 * @formparam billingAddress.firstName Customer's first name. This parameter is required.
	 * @formparam billingAddress.lastName Customer's last name. This parameter is required.
	 * @formparam billingAddress.titleCode Customer's title code. This parameter is required. For a list of codes, see
	 *            /{baseSiteId}/titles resource
	 * @formparam billingAddress.country.isocode Country isocode. This parameter is required and have influence on how
	 *            rest of address parameters are validated (e.g. if parameters are required :
	 *            line1,line2,town,postalCode,region.isocode)
	 * @formparam billingAddress.line1 First part of address. If this parameter is required depends on country (usually
	 *            it is required).
	 * @formparam billingAddress.line2 Second part of address. If this parameter is required depends on country (usually
	 *            it is not required)
	 * @formparam billingAddress.town Town name. If this parameter is required depends on country (usually it is
	 *            required)
	 * @formparam billingAddress.postalCode Postal code. If this parameter is required depends on country (usually it is
	 *            required)
	 * @formparam billingAddress.region.isocode Isocode for region. If this parameter is required depends on country.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Created payment details
	 * @throws WebserviceValidationException
	 * @throws InvalidPaymentInfoException
	 * @throws NoCheckoutCartException
	 * @throws UnsupportedRequestException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/paymentdetails", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PaymentDetailsWsDTO addPaymentDetails(final HttpServletRequest request,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException,
					UnsupportedRequestException
	{
		paymentProviderRequestSupportedStrategy.checkIfRequestSupported("addPaymentDetails");
		final CCPaymentInfoData paymentInfoData = super.addPaymentDetailsInternal(request).getPaymentInfo();
		return dataMapper.map(paymentInfoData, PaymentDetailsWsDTO.class, fields);
	}

	/**
	 * Defines details of a new credit card payment details and assigns the payment to the cart.
	 *
	 * @param paymentDetails
	 *           Request body parameter (DTO in xml or json format) which contains details like : Name on card
	 *           (accountHolderName), card number(cardNumber), card type (cardType.code), Month of expiry date
	 *           (expiryMonth), Year of expiry date (expiryYear), if payment details should be saved (saved), if if the
	 *           payment details should be used as default (defaultPaymentInfo), billing address (
	 *           billingAddress.firstName,billingAddress.lastName, billingAddress.titleCode,
	 *           billingAddress.country.isocode, billingAddress.line1, billingAddress.line2, billingAddress.town,
	 *           billingAddress.postalCode, billingAddress.region.isocode)
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @bodyparams accountHolderName,cardNumber,cardType,cardTypeData(code),expiryMonth,expiryYear,issueNumber,
	 *             startMonth, startYear
	 *             ,subscriptionId,defaultPaymentInfo,saved,billingAddress(titleCode,firstName,lastName,line1,line2
	 *             ,town,postalCode,country(isocode),region(isocode),defaultAddress)
	 * @return Created payment details
	 * @throws WebserviceValidationException
	 * @throws InvalidPaymentInfoException
	 * @throws NoCheckoutCartException
	 * @throws UnsupportedRequestException
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/paymentdetails", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public PaymentDetailsWsDTO addPaymentDetails(@RequestBody final PaymentDetailsWsDTO paymentDetails,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException,
					UnsupportedRequestException
	{
		paymentProviderRequestSupportedStrategy.checkIfRequestSupported("addPaymentDetails");
		validatePayment(paymentDetails);
		final String copiedfields = "accountHolderName,cardNumber,cardType,cardTypeData(code),expiryMonth,expiryYear,issueNumber,startMonth,startYear,subscriptionId,defaultPaymentInfo,saved,"
				+ "billingAddress(titleCode,firstName,lastName,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress)";
		CCPaymentInfoData paymentInfoData = dataMapper.map(paymentDetails, CCPaymentInfoData.class, copiedfields);
		paymentInfoData = addPaymentDetailsInternal(paymentInfoData).getPaymentInfo();
		return dataMapper.map(paymentInfoData, PaymentDetailsWsDTO.class, fields);
	}

	private void validatePayment(final PaymentDetailsWsDTO paymentDetails) throws NoCheckoutCartException
	{
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot add PaymentInfo. There was no checkout cart created yet!");
		}
		validate(paymentDetails, "paymentDetails", paymentDetailsDTOValidator);
	}

	/**
	 * Sets credit card payment details for the cart.
	 *
	 * @formparam paymentDetailsId Payment details identifier
	 * @throws InvalidPaymentInfoException
	 *            When payment details with given id doesn't exists or belong to another user
	 */
	@Secured(
	{ CUSTOMER, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/paymentdetails", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void setPaymentDetails(@RequestParam(required = true) final String paymentDetailsId) throws InvalidPaymentInfoException
	{
		super.setPaymentDetailsInternal(paymentDetailsId);
	}

	/**
	 * Return information about promotions applied on cart
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about promotions applied on cart
	 */
	@Secured(
	{ CUSTOMER, ROLE_CLIENT, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/promotions", method = RequestMethod.GET)
	@ResponseBody
	public PromotionResultListWsDTO getPromotions(
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getPromotions");
		}
		final List<PromotionResultData> appliedPromotions = new ArrayList<>();
		final List<PromotionResultData> orderPromotions = getSessionCart().getAppliedOrderPromotions();
		final List<PromotionResultData> productPromotions = getSessionCart().getAppliedProductPromotions();
		appliedPromotions.addAll(orderPromotions);
		appliedPromotions.addAll(productPromotions);

		final PromotionResultDataList dataList = new PromotionResultDataList();
		dataList.setPromotions(appliedPromotions);
		return dataMapper.map(dataList, PromotionResultListWsDTO.class, fields);
	}

	/**
	 * Return information about promotion with given id, applied on cart.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Information about promotion with given id, applied on cart
	 */
	@Secured(
	{ CUSTOMER, ROLE_CLIENT, ROLE_GUEST, CUSTOMERMANAGER, TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/promotions/{promotionId}", method = RequestMethod.GET)
	@ResponseBody
	public PromotionResultListWsDTO getPromotion(@PathVariable final String promotionId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getPromotion: promotionId = " + sanitize(promotionId));
		}
		final List<PromotionResultData> appliedPromotions = new ArrayList<PromotionResultData>();
		final List<PromotionResultData> orderPromotions = getSessionCart().getAppliedOrderPromotions();
		final List<PromotionResultData> productPromotions = getSessionCart().getAppliedProductPromotions();
		for (final PromotionResultData prd : orderPromotions)
		{
			if (prd.getPromotionData().getCode().equals(promotionId))
			{
				appliedPromotions.add(prd);
			}
		}
		for (final PromotionResultData prd : productPromotions)
		{
			if (prd.getPromotionData().getCode().equals(promotionId))
			{
				appliedPromotions.add(prd);
			}
		}

		final PromotionResultDataList dataList = new PromotionResultDataList();
		dataList.setPromotions(appliedPromotions);
		return dataMapper.map(dataList, PromotionResultListWsDTO.class, fields);
	}

	/**
	 * Enables the promotion for the order based on the promotionId defined for the cart.
	 *
	 * @formparam promotionId Promotion identifier
	 * @throws CommercePromotionRestrictionException
	 *            When there is no PromotionOrderRestriction for the promotion
	 */
	@Secured(
	{ TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/promotions", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void applyPromotion(@RequestParam(required = true) final String promotionId)
			throws CommercePromotionRestrictionException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("applyPromotion: promotionId = " + sanitize(promotionId));
		}
		commercePromotionRestrictionFacade.enablePromotionForCurrentCart(promotionId);
	}

	/**
	 * Disables the promotion for the order based on the promotionId defined for the cart.
	 *
	 * @throws CommercePromotionRestrictionException
	 *            When there is no PromotionOrderRestriction for the promotion
	 */
	@Secured(
	{ TRUSTED_CLIENT })
	@RequestMapping(value = "/{cartId}/promotions/{promotionId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void removePromotion(@PathVariable final String promotionId)
			throws CommercePromotionRestrictionException, NoCheckoutCartException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removePromotion: promotionId = " + sanitize(promotionId));
		}
		commercePromotionRestrictionFacade.disablePromotionForCurrentCart(promotionId);
	}

	/**
	 * Returns list of vouchers applied to the cart.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return List of vouchers applied to the cart.
	 */
	@Secured(
	{ ROLE_CLIENT, CUSTOMER, CUSTOMERMANAGER, TRUSTED_CLIENT, ROLE_GUEST })
	@RequestMapping(value = "/{cartId}/vouchers", method = RequestMethod.GET)
	@ResponseBody
	public VoucherListWsDTO getVouchers(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getVouchers");
		}
		final VoucherDataList dataList = new VoucherDataList();
		dataList.setVouchers(getSessionCart().getAppliedVouchers());
		return dataMapper.map(dataList, VoucherListWsDTO.class, fields);
	}

	/**
	 * Applies a voucher based on the voucherId defined for the cart.
	 *
	 * @formparam voucherId Voucher identifier
	 * @throws VoucherOperationException
	 *            When trying to apply a non-existent voucher or other error occurs during the voucher-application
	 *            process.
	 */
	@Secured(
	{ ROLE_CLIENT, CUSTOMER, CUSTOMERMANAGER, TRUSTED_CLIENT, ROLE_GUEST })
	@RequestMapping(value = "/{cartId}/vouchers", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void applyVoucherForCart(@RequestParam(required = true) final String voucherId)
			throws NoCheckoutCartException, VoucherOperationException
	{
		super.applyVoucherForCartInternal(voucherId);
	}

	/**
	 * Removes a voucher based on the voucherId defined for the current cart.
	 *
	 * @throws VoucherOperationException
	 *            When an error occurs during the release voucher process.
	 */
	@Secured(
	{ ROLE_CLIENT, CUSTOMER, CUSTOMERMANAGER, TRUSTED_CLIENT, ROLE_GUEST })
	@RequestMapping(value = "/{cartId}/vouchers/{voucherId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void releaseVoucherFromCart(@PathVariable final String voucherId)
			throws NoCheckoutCartException, VoucherOperationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("release voucher : voucherCode = " + sanitize(voucherId));
		}
		if (!checkoutFacade.hasCheckoutCart())
		{
			throw new NoCheckoutCartException("Cannot realese voucher. There was no checkout cart created yet!");
		}
		voucherFacade.releaseVoucher(voucherId);
	}

	protected void validateIfProductIsInStockInPOS(final String baseSiteId, final String productCode, final String storeName,
			final Long entryNumber)
	{
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId))
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stock = commerceStockFacade.getStockDataForProductAndPointOfService(productCode, storeName);
		if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.OUTOFSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException(PRODUCT + sanitize(productCode) + "] is currently out of stock",
						LowStockException.NO_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException(PRODUCT + sanitize(productCode) + "] is currently out of stock",
						LowStockException.NO_STOCK, productCode);
			}
		}
		else if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.LOWSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException("Not enough product in stock", LowStockException.LOW_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException("Not enough product in stock", LowStockException.LOW_STOCK, productCode);
			}
		}
	}

	protected void validateIfProductIsInStockOnline(final String baseSiteId, final String productCode, final Long entryNumber)
	{
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId))
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stock = commerceStockFacade.getStockDataForProductAndBaseSite(productCode, baseSiteId);
		if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.OUTOFSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException(PRODUCT + sanitize(productCode) + "] cannot be shipped - out of stock online",
						LowStockException.NO_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException(PRODUCT + sanitize(productCode) + "] cannot be shipped - out of stock online",
						LowStockException.NO_STOCK, productCode);
			}
		}
	}

	//	/**
	//	 * Create Cart from mobile
	//	 *
	//	 * @param baseSiteId
	//	 * @param userId
	//	 * @return result
	//	 * @throws RequestParameterException
	//	 */
	//	@RequestMapping(method = RequestMethod.POST)
	//	@ResponseStatus(HttpStatus.CREATED)
	//	@ResponseBody
	//	public WebSerResponseWsDTO createCart(@PathVariable final String baseSiteId, @PathVariable final String userId,
	//			@RequestParam(required = false) final String oldCartId, @RequestParam(required = false) final String toMergeCartGuid)
	//			throws RequestParameterException
	//	{
	//		final WebSerResponseWsDTO result = mplCartWebService.createCart(oldCartId, toMergeCartGuid);
	//		return result;
	//	}// End of create cart

	/**
	 * Create Cart from mobile
	 *
	 * @param baseSiteId
	 * @param userId
	 * @return result
	 * @throws RequestParameterException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public WebSerResponseWsDTO createCart(@PathVariable final String baseSiteId, @PathVariable final String userId,
			@RequestParam(required = false) final String oldCartId, @RequestParam(required = false) String toMergeCartGuid)
					throws RequestParameterException
	{

		final WebSerResponseWsDTO result = new WebSerResponseWsDTO();
		LOG.debug("**************** Creating cart mobile web service *********************");
		final CartData cart;
		CartModel cartModel = null;
		CartModel newCartModel = null;
		int count = 0;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		boolean deListedStatus = false;
		try
		{
			if (StringUtils.isNotEmpty(oldCartId))
			{
				if (userFacade.isAnonymousUser())
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9042);
				}

				if (!isCartAnonymous(oldCartId))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9043);
				}

				if (StringUtils.isEmpty(toMergeCartGuid))
				{
					toMergeCartGuid = getSessionCart().getGuid();
				}
				else
				{
					if (!isUserCart(toMergeCartGuid))
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9044);
					}
				}

				try
				{
					cartFacade.restoreAnonymousCartAndMerge(oldCartId, toMergeCartGuid);
					cart = getSessionCart();
					if (null != cart && StringUtils.isNotEmpty(cart.getCode()))
					{
						LOG.debug("**************** Created cart mobile web service *********************" + cart.getCode());

						result.setCode(cart.getCode());
					}
					//					if (null != cart && null != cart.getEntries() && !cart.getEntries().isEmpty())
					//					{
					//						for (final OrderEntryData entry : cart.getEntries())
					//						{
					//							if (!entry.isGiveAway())
					//							{
					//								count++;
					//							}
					//						}
					//					}
					//					result.setCount(String.valueOf(count));
					if (null != cart && StringUtils.isNotEmpty(cart.getGuid()))
					{
						LOG.debug("**************** Created cart mobile web service *********************" + cart.getGuid());

						result.setGuid(cart.getGuid());
					}
					try
					{
						final CartModel newcartModel = mplPaymentWebFacade.findCartValues(cart.getCode());
						deListedStatus = mplCartFacade.isCartEntryDelisted(newcartModel);
						LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);
						newCartModel = mplCartFacade.removeDeliveryMode(newcartModel);
					}
					catch (final Exception ex)
					{
						throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B2002);
					}
					if (deListedStatus)
					{
						delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
						result.setDelistedMessage(delistMessage);
					}

					final CartData cartData = getMplExtendedCartConverter().convert(newCartModel);

					if (null != cartData && null != cartData.getEntries() && !cartData.getEntries().isEmpty())
					{
						for (final OrderEntryData entry : cartData.getEntries())
						{
							if (!entry.isGiveAway())
							{
								count++;
							}
						}
					}
					result.setCount(String.valueOf(count));

					result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					return result;
				}
				catch (final CommerceCartMergingException e)
				{
					throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9045);
				}
				catch (final CommerceCartRestorationException e)
				{
					throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9046);
				}
			}
			else
			{
				if (StringUtils.isNotEmpty(toMergeCartGuid))
				{
					if (!isUserCart(toMergeCartGuid))
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9046);
					}

					try
					{
						cartFacade.restoreSavedCart(toMergeCartGuid);
						cart = getSessionCart();
						if (null != cart && StringUtils.isNotEmpty(cart.getCode()))
						{
							result.setCode(cart.getCode());
						}
						if (null != cart && StringUtils.isNotEmpty(cart.getGuid()))
						{
							result.setGuid(cart.getGuid());
						}
						result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						return result;
					}
					catch (final CommerceCartRestorationException e)
					{
						throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9044);
					}

				}
				cart = getSessionCart();

				if (userFacade.isAnonymousUser() && StringUtils.isNotEmpty(cart.getGuid()))
				{
					cartModel = mplPaymentWebFacade.findCartAnonymousValues(cart.getGuid());
				}
				else if (!userFacade.isAnonymousUser() && StringUtils.isNotEmpty(cart.getCode()))
				{
					cartModel = mplPaymentWebFacade.findCartValues(cart.getCode());
				}
				//Setting channel as mobile
				cartModel.setChannel(SalesApplication.MOBILE);
				getModelService().save(cartModel);

				try
				{
					final CartModel newcartModel = mplPaymentWebFacade.findCartValues(cart.getCode());
					deListedStatus = mplCartFacade.isCartEntryDelisted(newcartModel);
					LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);
					newCartModel = mplCartFacade.removeDeliveryMode(newcartModel);
				}
				catch (final Exception ex)
				{
					throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B2002);
				}
				if (deListedStatus)
				{
					delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
					result.setDelistedMessage(delistMessage);
				}
				if (StringUtils.isNotEmpty(newCartModel.getCode()))
				{
					result.setCode(newCartModel.getCode());
				}
				if (StringUtils.isNotEmpty(newCartModel.getGuid()))
				{
					result.setGuid(newCartModel.getGuid());
				}
				result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				LOG.debug("**************** Cart created successfully mobile web service *********************");
				return result;
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}// End of create cart


	private boolean isUserCart(final String toMergeCartGuid)
	{
		if (cartFacade instanceof CommerceWebServicesCartFacade)
		{
			final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) cartFacade;
			return commerceWebServicesCartFacade.isCurrentUserCart(toMergeCartGuid);
		}
		return true;
	}

	private boolean isCartAnonymous(final String cartGuid)
	{
		if (cartFacade instanceof CommerceWebServicesCartFacade)
		{
			final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) cartFacade;
			return commerceWebServicesCartFacade.isAnonymousUserCart(cartGuid);
		}
		return true;
	}

	/**
	 * Add product to Cart for mobile
	 *
	 * @param cartId
	 * @param productCode
	 * @param USSID
	 * @param quantity
	 * @return result
	 * @throws CommerceCartModificationException
	 * @throws InvalidCartException
	 */
	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@RequestMapping(value = "/{cartId}/addProductToCart", method = RequestMethod.POST)
	@ResponseBody
	public WebSerResponseWsDTO addProductToCartMobile(@PathVariable final String cartId,
			@RequestParam(required = true) final String productCode, @RequestParam(required = true) final String USSID,
			@RequestParam(required = false, defaultValue = "1") final String quantity,
			@RequestParam(required = true) final boolean addedToCartWl)
					throws InvalidCartException, CommerceCartModificationException
	{
		WebSerResponseWsDTO result = new WebSerResponseWsDTO();
		LOG.debug("**************** Adding ptoduct to cart mobile web service *********************" + cartId + "::: USSID ::::"
				+ USSID + ":::: quantity ::::" + quantity + ":::: productCode ::::" + productCode);
		try
		{
			result = mplCartWebService.addProductToCart(productCode, cartId, quantity, USSID, addedToCartWl);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);

			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}
			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				result.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				result.setErrorCode(e.getErrorCode());
			}

			result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return result;
	}// End of Add product to cart

	/**
	 * Returns cart entries for mobile.
	 *
	 * @PathVariable cartId
	 * @PathVariable userId
	 * @param fields
	 * @return CartDataDetailsWsDTO
	 */
	@RequestMapping(value = "/{cartId}/cartDetails", method = RequestMethod.GET)
	@ResponseBody
	public CartDataDetailsWsDTO getCartDetails(@PathVariable final String cartId,
			@RequestParam(required = false) final String pincode,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final AddressListWsDTO addressListDTO = addressList(fields);
		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		try
		{
			if (null != cartId)
			{
				LOG.debug("************ get cart details mobile web service *********" + cartId);

				cartDataDetails = mplCartWebService.getCartDetails(cartId, addressListDTO, pincode);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorCode() && e.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.B9038))
			{
				cartDataDetails.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}

		}
		return cartDataDetails;
	}

	/**
	 * Deletes cart entry.
	 *
	 *
	 * @PathVariable cartId
	 * @PathVariable entryNumber , entryNumber refers to a cart entry.
	 * @return CartDataDetailsWsDTO
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 */
	@RequestMapping(value = "/{cartId}/deleteEntries/{entryNumber}", method = RequestMethod.GET)
	@ResponseBody
	public CartDataDetailsWsDTO removeCartEntryMobile(@PathVariable final String cartId, @PathVariable final long entryNumber)
			throws CommerceCartModificationException, InvalidCartException, ConversionException
	{
		final CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO(); //Object to store result
		int count = 0;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removeCartEntry: " + logParam(ENTRY_NUMBER, entryNumber));
		}
		try
		{
			CartModel cartModel = null;
			if (userFacade.isAnonymousUser())
			{
				LOG.debug("productDetails:  AnonymousUser ");
				cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartId);
				LOG.debug("************ Anonymous cart mobile **************" + cartId);
			}
			else
			{
				LOG.debug("productDetails:  loged in User ");
				cartModel = mplPaymentWebFacade.findCartValues(cartId);
				LOG.debug("************ Logged-in cart mobile **************" + cartId);
			}

			cartModel.setChannel(SalesApplication.MOBILE);

			LOG.debug("productDetails:  Cart Channel: " + cartModel.getChannel());

			getModelService().save(cartModel);

			try
			{
				//cartFacade.updateCartEntry(entryNumber, 0);
				mplCartFacade.updateCartEntryMobile(entryNumber, 0, cartModel);
			}
			catch (final Exception e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9056);
			}
			/*
			 * String cartIdentifier; Collection<CartModel> cartModelList = null;
			 *
			 * cartModelList = mplCartFacade.getCartDetails(customerFacade.getCurrentCustomer().getUid());
			 *
			 *
			 * if (null != cartModelList && cartModelList.size() > 0) { for (final CartModel cartModel : cartModelList) {
			 * if (userFacade.isAnonymousUser()) { cartIdentifier = cartModel.getGuid(); } else { cartIdentifier =
			 * cartModel.getCode(); } if (cartIdentifier.equals(cartId)) {
			 */

			final List<Wishlist2Model> allWishlists = wishlistFacade.getAllWishlists();
			if (!CollectionUtils.isEmpty(cartModel.getEntries()))
			{
				for (final AbstractOrderEntryModel entry : cartModel.getEntries())
				{
					if (null != entry.getEntryNumber() && entryNumber == entry.getEntryNumber().longValue())
					{
						for (final Wishlist2Model wishlist2Model : allWishlists)
						{
							if (!CollectionUtils.isEmpty(wishlist2Model.getEntries()))
							{
								for (final Wishlist2EntryModel entryModel : wishlist2Model.getEntries())
								{
									if (null != entryModel.getAddToCartFromWl() && entryModel.getAddToCartFromWl().equals(Boolean.TRUE))
									{
										LOG.debug("*********** Remove entry from cart WL mobile web service *************"
												+ entryModel.getAddToCartFromWl() + "::entryNumber::" + entryNumber);
										entryModel.setAddToCartFromWl(Boolean.FALSE);
										modelService.save(entryModel);
										LOG.debug("*********** Remove entry from cart WL mobile web service  SAVED in DB*************"
												+ entryModel.getAddToCartFromWl() + "::entryNumber::" + entryNumber);
										break;
									}
								}
							}
						}
					}
				}
			}


			if (StringUtils.isNotEmpty(cartModel.getSubtotal().toString()))
			{
				final PriceData subtotalprice = discountUtility.createPrice(cartModel,
						Double.valueOf(cartModel.getSubtotal().toString()));
				if (null != subtotalprice && null != subtotalprice.getValue())
				{
					cartDataDetails.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			if (StringUtils.isNotEmpty(cartModel.getTotalPrice().toString()))
			{
				final PriceData totalPrice = discountUtility.createPrice(cartModel,
						Double.valueOf(cartModel.getTotalPrice().toString()));
				if (null != totalPrice && null != totalPrice.getValue())
				{
					cartDataDetails.setTotalPrice(String.valueOf(totalPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			if (StringUtils.isNotEmpty(cartModel.getDeliveryCost().toString()))
			{
				cartDataDetails.setDeliveryCharge(cartModel.getDeliveryCost().toString());
			}

			if (null != cartModel.getEntries() && !cartModel.getEntries().isEmpty())
			{
				for (final AbstractOrderEntryModel entry : cartModel.getEntries())
				{
					if (null != entry.getGiveAway() && !entry.getGiveAway().booleanValue())
					{
						count++;
					}
				}

				cartDataDetails.setCount(count);
			}
			double discount = 0.0;
			double totalPrice = 0.0D;
			if (null != cartModel.getEntries() && !cartModel.getEntries().isEmpty())
			{
				for (final AbstractOrderEntryModel entry : cartModel.getEntries())
				{
					totalPrice = totalPrice + (entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue());
				}

				discount = (totalPrice + cartModel.getDeliveryCost().doubleValue() + cartModel.getConvenienceCharges().doubleValue())
						- cartModel.getTotalPriceWithConv().doubleValue();
			}
			if (discount >= 0)
			{
				final PriceData priceDiscount = discountUtility.createPrice(cartModel, Double.valueOf(discount));
				if (null != priceDiscount && null != priceDiscount.getValue())
				{
					cartDataDetails.setDiscountPrice(String.valueOf(priceDiscount.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			//}
			//}
			/*
			 * } else { throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9029); }
			 */
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				cartDataDetails.setErrorCode(e.getErrorCode());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				cartDataDetails.setErrorCode(e.getErrorCode());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return cartDataDetails;
	}

	//End of delete cart entry

	/**
	 * Updates the quantity of a single cart entry and details of the store where the cart entry will be picked,for
	 * Mobile.
	 *
	 * @formparam quantity Quantity of product.
	 * @formparam pickupStore Name of the store where product will be picked. Set only if want to pick up from a store.
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return CartDataDetailsWsDTO-Information about cart modification
	 * @throws CommerceCartModificationException
	 *            When there are some problems with cart modification
	 *
	 */
	@RequestMapping(value = "/{cartId}/updateEntries/{entryNumber}", method = RequestMethod.POST)
	@ResponseBody
	public CartDataDetailsWsDTO updateCartEntryMobile(@PathVariable final String cartId, @PathVariable final String baseSiteId,
			@PathVariable final long entryNumber, @RequestParam(required = true) final Long quantity,
			@RequestParam(required = false) final String pickupStore,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateCartEntry: " + logParam(ENTRY_NUMBER, entryNumber) + ", " + logParam("quantity", quantity) + ", "
					+ logParam(PICK_UP_STORE, pickupStore));
		}
		final CartData cart = getSessionCart();
		final OrderEntryData orderEntry = getCartEntryForNumber(cart, entryNumber);
		CartDataDetailsWsDTO cartDataDetails = null;
		cartDataDetails = new CartDataDetailsWsDTO();
		Collection<CartModel> cartModelList = null;
		String cartIdentifier;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		try
		{

			if (quantity == null && StringUtils.isEmpty(pickupStore))
			{
				LOG.debug(MarketplacecommerceservicesConstants.FIELD_NOT_EMPTY_MSG);
				cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				cartDataDetails.setError(MarketplacecommerceservicesConstants.FIELD_QUANTITY
						+ MarketplacecommerceservicesConstants.SINGLE_SPACE + MarketplacecommerceservicesConstants.FIELD_NOT_EMPTY_MSG);
				return cartDataDetails;
			}

			if (quantity != null)
			{
				validate(quantity, "quantity", greaterThanZeroValidator);
			}

			if (pickupStore != null)
			{
				validate(pickupStore, PICK_UP_STORE, pointOfServiceValidator);
			}

			updateCartEntryInternal(baseSiteId, cart, orderEntry, quantity, pickupStore, fields, false);


			cartModelList = mplCartFacade.getCartDetails(customerFacade.getCurrentCustomer().getUid());

			if (null != cartModelList && cartModelList.size() > 0)
			{
				for (final CartModel cartModel : cartModelList)
				{
					if (userFacade.isAnonymousUser())
					{
						cartIdentifier = cartModel.getGuid();
					}
					else
					{
						cartIdentifier = cartModel.getCode();
					}
					if (cartIdentifier.equals(cartId))
					{
						//duplciate cart fix for mobile
						final boolean deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cartModel);

						LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);
						final CartModel newCartModel = mplCartFacade.removeDeliveryMode(cartModel);

						final List<AbstractOrderEntryModel> abstractOrderEntryList = newCartModel.getEntries();
						final List<GetWishListProductWsDTO> gwlpList = new ArrayList<GetWishListProductWsDTO>();
						List<GetWishListProductWsDTO> gwlpFreeItemList = new ArrayList<GetWishListProductWsDTO>();
						GetWishListProductWsDTO gwlp = null;
						for (final AbstractOrderEntryModel abstractOrderEntry : abstractOrderEntryList)
						{
							if (null != abstractOrderEntry)
							{
								gwlp = new GetWishListProductWsDTO();

								if (StringUtils.isNotEmpty((abstractOrderEntry.getQuantity().toString())))
								{
									gwlp.setQtySelectedByUser(abstractOrderEntry.getQuantity().toString());
								}
								gwlpList.add(gwlp);
							}
						}
						//End of AbstractOrderEntryModel for loop,Product Details
						cartDataDetails.setProducts(gwlpList);
						gwlpFreeItemList = mplCartWebService.freeItems(abstractOrderEntryList);
						if (null != gwlpFreeItemList)
						{
							cartDataDetails.setFreeItemsList(gwlpFreeItemList);
						}
						if (StringUtils.isNotEmpty(newCartModel.getSubtotal().toString()))
						{
							final PriceData subtotalprice = discountUtility.createPrice(newCartModel,
									Double.valueOf(newCartModel.getSubtotal().toString()));
							if (null != subtotalprice && null != subtotalprice.getValue())
							{
								cartDataDetails
										.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
							}
						}
						if (StringUtils.isNotEmpty(newCartModel.getTotalPrice().toString()))
						{
							final PriceData totalPrice = discountUtility.createPrice(newCartModel,
									Double.valueOf(newCartModel.getTotalPrice().toString()));
							if (null != totalPrice && null != totalPrice.getValue())
							{
								cartDataDetails
										.setTotalPrice(String.valueOf(totalPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
							}
						}
						if (StringUtils.isNotEmpty(newCartModel.getDeliveryCost().toString()))
						{
							cartDataDetails.setDeliveryCharge(newCartModel.getDeliveryCost().toString());
						}
						if (deListedStatus)
						{
							delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
							cartDataDetails.setDelistedMessage(delistMessage);
						}
					}
				}
			}
			else
			{
				throw new RequestParameterException(MarketplacecommerceservicesConstants.B9038);
			}

		}
		catch (final InvalidCartException ce)
		{
			cartDataDetails = new CartDataDetailsWsDTO();
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ce);
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			cartDataDetails.setError(MarketplacecommerceservicesConstants.COULD_NOT_MODIFY_CART + ce);
			return cartDataDetails;
		}
		catch (final ConversionException ce)
		{
			cartDataDetails = new CartDataDetailsWsDTO();
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ce);
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			cartDataDetails.setError(MarketplacecommerceservicesConstants.COULD_NOT_MODIFY_CART + ce);
			return cartDataDetails;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				cartDataDetails.setErrorCode(e.getErrorCode());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				cartDataDetails.setErrorCode(e.getErrorCode());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception ce)
		{
			cartDataDetails = new CartDataDetailsWsDTO();
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ce);
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			cartDataDetails.setError(MarketplacecommerceservicesConstants.EXCEPTION_IS + ce);
			return cartDataDetails;
		}
		return cartDataDetails;
	}

	/**
	 * Returns cart checkout data for mobile.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Cart entries list
	 *
	 * @param cartId
	 * @param fields
	 * @return:CartDataDetailsWsDTO
	 * @throws CommerceCartModificationException
	 *
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/productCheckout", method = RequestMethod.GET)
	@ResponseBody
	public CartDataDetailsWsDTO cartCheckout(@PathVariable final String cartId, @RequestParam final String postalCode,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
					throws CommerceCartModificationException
	{
		final CartDataDetailsWsDTO cartDetailsData = new CartDataDetailsWsDTO();
		Collection<CartModel> cartModelList = null;
		String cartIdentifier;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		try
		{
			if (checkoutFacade.hasCheckoutCart() && null != customerFacade.getCurrentCustomer())
			{
				cartModelList = mplCartFacade.getCartDetails(customerFacade.getCurrentCustomer().getUid());

				if (null != cartModelList && cartModelList.size() > 0)
				{
					for (final CartModel cartModel : cartModelList)
					{
						if (userFacade.isAnonymousUser())
						{
							cartIdentifier = cartModel.getGuid();
						}
						else
						{
							cartIdentifier = cartModel.getCode();
						}

						if (cartIdentifier.equals(cartId))
						{
							//duplcaite cart fix
							final boolean deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cartModel);

							LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);

							//	final CartModel newCartModel = mplCartFacade.removeDeliveryMode(cartModel);

							/*** Product Details ***/
							final List<AbstractOrderEntryModel> aoem = cartModel.getEntries();
							final List<GetWishListProductWsDTO> gwlpList = mplCartWebService.productDetails(aoem, true, postalCode, true,
									cartId);
							cartDetailsData.setProducts(gwlpList);
							/*** End of Product Details ***/
							/*** Address details ***/
							if (deListedStatus)
							{
								delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
								cartDetailsData.setDelistedMessage(delistMessage);
							}
							final AddressListWsDTO addressListWsDTO = addressList(fields);
							if (null != addressListWsDTO)
							{
								cartDetailsData.setAddressDetailsList(addressListWsDTO);
							}
							/*** End Of Address details ***/
							/* Pincode */
							if (postalCode != null)
							{
								cartDetailsData.setPincode(postalCode);
							}
							/* Pincode */
						}

					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9040);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9040);
			}
		}
		catch (final InvalidCartException ce)
		{
			throw new EtailNonBusinessExceptions(ce, MarketplacecommerceservicesConstants.B9056);
		}
		catch (final ConversionException ce)
		{
			throw new EtailNonBusinessExceptions(ce, MarketplacecommerceservicesConstants.B9056);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				cartDetailsData.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				cartDetailsData.setErrorCode(e.getErrorCode());
			}
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				cartDetailsData.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				cartDetailsData.setErrorCode(e.getErrorCode());
			}
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return cartDetailsData;
	}

	/**
	 * Returns order summary for mobile.
	 *
	 * @param cartId
	 * @param request
	 * @return cData
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/displayOrderSummary", method = RequestMethod.GET)
	@ResponseBody
	public CartDataDetailsWsDTO displayOrderSummary(@PathVariable final String userId, @PathVariable final String cartId,
			@RequestParam final String pincode)
	{
		LOG.debug("displayOrderSummary:  cartId : " + cartId + ", pincode:" + pincode);
		final CartDataDetailsWsDTO cartDetailsData = new CartDataDetailsWsDTO();
		//		final Collection<CartModel> cartModelList = null;
		String cartIdentifier;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		try
		{ //fetch usermodel against customer
			final UserModel user = getExtUserService().getUserForOriginalUid(userId);
			LOG.debug("displayOrderSummary : user : " + user);
			// Check userModel null
			if (null != user)
			{
				//getting cartmodel using cart id and user
				final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartId, user);
				// Validate Cart Model is not null
				if (null != cartModel)
				{
					if (userFacade.isAnonymousUser())
					{
						cartIdentifier = cartModel.getGuid();
						LOG.debug("displayOrderSummary: Anonymous User" + cartIdentifier + "  cartId: " + cartId);
					}
					else
					{
						cartIdentifier = cartModel.getCode();
						LOG.debug("displayOrderSummary: Loged in User" + cartIdentifier + "  cartId: " + cartId);
					}
					if (cartIdentifier.equals(cartId))
					{
						final boolean deListedStatus = mplCartFacade.isCartEntryDelisted(cartModel);
						final List<AbstractOrderEntryModel> aoem = cartModel.getEntries();
						final List<GetWishListProductWsDTO> gwlpList = mplCartWebService.productDetails(aoem, true, pincode, false,
								cartId);
						cartDetailsData.setProducts(gwlpList);

						if (null != cartModel.getDeliveryAddress() && null != getShippingAddress(cartModel.getDeliveryAddress()))
						{
							cartDetailsData.setShippingAddress(getShippingAddress(cartModel.getDeliveryAddress()));
						}
						else
						{
							cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
							cartDetailsData.setError(MarketplacecommerceservicesConstants.NODELIVERYADDRESS);
						}
						if (StringUtils.isNotEmpty(cartModel.getSubtotal().toString()))
						{
							final PriceData subtotalprice = discountUtility.createPrice(cartModel,
									Double.valueOf(cartModel.getSubtotal().toString()));
							if (null != subtotalprice && null != subtotalprice.getValue())
							{
								cartDetailsData
										.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
							}
						}
						else
						{
							cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
							cartDetailsData.setError(MarketplacecommerceservicesConstants.NOSUBTOTAL);
						}
						final CartData cartData = mplCustomAddressFacade.getCheckoutCart();
						//final CartData cartData = getMplExtendedCartConverter().convert(cartModel);
						/*
						 * double totalPrice = 0.0D; if (null != cartModel.getEntries() && !cartModel.getEntries().isEmpty())
						 * { for (final AbstractOrderEntryModel entry : cartModel.getEntries()) { totalPrice = totalPrice +
						 * (entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue()); }
						 *
						 * final PriceData priceDiscount = cartData.getTotalDiscounts(); if (null != priceDiscount && null !=
						 * priceDiscount.getFormattedValue()) {
						 * cartDetailsData.setDiscountPrice(String.valueOf(priceDiscount.getFormattedValue())); } }
						 */

						final PriceData discountPrice = cartData.getTotalDiscounts();
						if (null != discountPrice.getValue())
						{
							cartDetailsData
									.setDiscountPrice(String.valueOf(discountPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
						}
						//Added for Setting Delivery Charge
						if (cartModel.getDeliveryCost() != null)
						{
							cartDetailsData.setDeliveryCharge(cartModel.getDeliveryCost().toString());
						}
						if (null != cartModel.getSubtotal() && cartModel.getSubtotal().doubleValue() >= 0.0
								&& null != cartModel.getDeliveryCost() && null != cartModel.getDeliveryCost()
								&& null != cartData.getTotalDiscounts() && null != cartData.getTotalDiscounts().getValue()
								&& null != cartData.getTotalDiscounts().getValue().toString())
						{
							final double totalafterpromotion = cartModel.getSubtotal().doubleValue()
									+ cartModel.getDeliveryCost().doubleValue()
									- Double.parseDouble(cartData.getTotalDiscounts().getValue().toString());

							LOG.debug("********** displayOrderSummary *************** totalAfterPromotion" + totalafterpromotion);
							final PriceData totalPrice = discountUtility.createPrice(cartModel, Double.valueOf(totalafterpromotion));
							if (null != totalPrice && null != totalPrice.getValue())
							{
								cartDetailsData
										.setTotalPrice(String.valueOf(totalPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
							}
						}
						else
						{
							cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
							cartDetailsData.setError(MarketplacecommerceservicesConstants.NOTOTALPRICE);
						}

						//Delisted

						//						final boolean deListedStatus = mplCartFacade.isCartEntryDelisted(cartModel);

						LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);
						if (deListedStatus)
						{
							//							cartModel = mplCartFacade.removeDeliveryMode(cartModel);
							delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
							cartDetailsData.setDelistedMessage(delistMessage);
						}
					}
				}
				else
				{
					//If  Cart Model is null display error message
					cartDetailsData.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
				}
			}
			else
			{
				//If  User Model is null display error message
				cartDetailsData.setError(MarketplacewebservicesConstants.USEREMPTY);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9055);
			}

		}
		catch (final ModelSavingException me)
		{
			// Error message for ModelSavingException Exceptions
			LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + me.getMessage());
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			cartDetailsData.setErrorCode(MarketplacecommerceservicesConstants.B9200);
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9200);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				cartDetailsData.setError(ex.getErrorMessage());
				cartDetailsData.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				cartDetailsData.setError(ex.getErrorMessage());
				cartDetailsData.setErrorCode(ex.getErrorCode());
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				cartDetailsData.setError(((EtailNonBusinessExceptions) ex).getErrorMessage());
				cartDetailsData.setErrorCode(((EtailNonBusinessExceptions) ex).getErrorCode());
			}
		}

		return cartDetailsData;
	}

	/**
	 * shipping address
	 *
	 * @param address
	 * @return BillingAddressWsDTO
	 */
	private BillingAddressWsDTO getShippingAddress(final AddressModel address)
	{
		final BillingAddressWsDTO shippingAddress = new BillingAddressWsDTO();
		if (null != address.getLine1())
		{
			shippingAddress.setAddressLine1(address.getLine1());
		}
		if (null != address.getLine2())
		{
			shippingAddress.setAddressLine2(address.getLine2());
		}
		if (null != address.getAddressLine3())
		{
			shippingAddress.setAddressLine3(address.getAddressLine3());
		}
		if (null != address.getCountry() && null != address.getCountry().getName())
		{
			shippingAddress.setCountry(address.getCountry().getName());
		}
		if (null != address.getTown())
		{
			shippingAddress.setTown(address.getTown());
		}
		if (null != address.getDistrict())
		{

			shippingAddress.setState(address.getDistrict());
		}
		if (null != address.getFirstname())
		{
			shippingAddress.setFirstName(address.getFirstname());
		}
		if (null != address.getLastname())
		{
			shippingAddress.setLastName(address.getLastname());
		}
		if (null != address.getPostalcode())
		{
			shippingAddress.setPostalcode(address.getPostalcode());
		}
		if (null != address.getShippingAddress())
		{
			shippingAddress.setShippingFlag(address.getShippingAddress());
		}
		if (null != address.getPhone1())
		{
			shippingAddress.setPhone(address.getPhone1());
		}
		if (null != address.getAddressType())
		{
			shippingAddress.setAddressType(address.getAddressType());
		}
		if (null != address.getPk())
		{
			shippingAddress.setId(address.getPk().toString());
		}
		//shippingAddress.setDefaultAddress(new Boolean(checkDefaultAddress(address))); Avoid instantiating Boolean objects; reference Boolean.TRUE or Boolean.FALSE or call Boolean.valueOf() instead.
		shippingAddress.setDefaultAddress(Boolean.valueOf(checkDefaultAddress(address)));
		return shippingAddress;
	}

	/**
	 * check default address
	 *
	 * @param address
	 * @return boolean
	 */
	private boolean checkDefaultAddress(final AddressModel address)
	{
		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		final AddressModel defaultAddress = customerAccountService.getDefaultAddress(currentCustomer);
		if (null != defaultAddress && null != defaultAddress.getPk() && null != address.getPk()
				&& address.getPk().equals(defaultAddress.getPk()))
		{
			return true;
		}
		return false;
	}

	/**
	 * Cart Reservation.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Details of cart and it's entries
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/softReservation", method = RequestMethod.POST)
	@ResponseBody
	public ReservationListWsDTO getCartReservation(@PathVariable final String cartId, @RequestParam final String pincode,
			@RequestParam final String type)
	{
		ReservationListWsDTO reservationList = new ReservationListWsDTO();
		try
		{
			LOG.debug("******************* Soft reservation Mobile web service ******************" + cartId + pincode);
			if (setFreebieDeliverMode(cartId))
			{
				reservationList = mplCommerceCartService.getReservation(cartId, mplCartFacade.getSessionCartWithEntryOrdering(true),
						pincode, type);
				LOG.debug("******************* Soft reservation Mobile web service response received from OMS ******************"
						+ cartId);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9201);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				reservationList.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				reservationList.setErrorCode(e.getErrorCode());
			}
			reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				reservationList.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				reservationList.setErrorCode(e.getErrorCode());
			}
			reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return reservationList;
	}

	/**
	 * Cart Reservation for Payment.
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Details of cart and it's entries
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/softReservationForPayment", method = RequestMethod.POST)
	@ResponseBody
	public ReservationListWsDTO getCartReservationForPayment(@PathVariable final String cartId, @RequestParam final String pincode,
			@RequestParam final String type)
	{
		ReservationListWsDTO reservationList = new ReservationListWsDTO();
		CartModel cart = null;
		boolean deListedStatus = false;
		boolean delvieryModeset = false;
		String delistMessage = MarketplacecommerceservicesConstants.EMPTY;
		//List<PinCodeResponseData> pinCodeResponse = null;
		try
		{
			cart = mplPaymentWebFacade.findCartValues(cartId);
			delvieryModeset = setFreebieDeliverMode(cartId);
			LOG.debug("************ Logged-in cart mobile checking validity of promotion **************" + cartId);
			if (!mplCheckoutFacade.isPromotionValid(cart))
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9075);
			}
			LOG.debug("************ Logged-in cart mobile promotion is valid **************" + cartId);
			/*
			 * try { LOG.debug(String.format("Checking servicibility for the pincode %s", pincode)); pinCodeResponse =
			 * mplCartWebService.checkPinCodeAtCart(mplCartFacade.getSessionCartWithEntryOrdering(true), pincode); if (null
			 * != pinCodeResponse) { for(PinCodeResponseData pinResponse:pinCodeResponse){ pinResponse.getIsServicable()//
			 * } } } catch (final CMSItemNotFoundException e) { throw new EtailNonBusinessExceptions(e,
			 * MarketplacecommerceservicesConstants.B9048);
			 *
			 * }
			 */

			try
			{
				LOG.debug("************ Logged-in cart mobile delisting **************" + cartId);
				//duplicate cart fix for mobile
				deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cart);
				LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);

			}
			catch (final Exception ex)
			{
				throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B2002);
			}

			if (deListedStatus)
			{
				cart = mplCartFacade.removeDeliveryMode(cart);
				LOG.debug("************ Logged-in cart mobile delisting success **************" + cartId);
				delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
				reservationList.setDelistedMessage(delistMessage);
				reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			else
			{
				if (delvieryModeset)
				{
					reservationList = mplCommerceCartService.getReservation(cartId,
							mplCartFacade.getSessionCartWithEntryOrdering(true), pincode, type);
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9201);
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				reservationList.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				reservationList.setErrorCode(e.getErrorCode());
			}
			reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				reservationList.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				reservationList.setErrorCode(e.getErrorCode());
			}
			reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return reservationList;
	}

	/**
	 * set freebie delivery mode
	 *
	 * @param cartId
	 * @return boolean
	 */
	private boolean setFreebieDeliverMode(final String cartId)
	{
		boolean success = false;

		try
		{
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
			final CartModel cartModel = mplPaymentWebFacade.findCartValues(cartId);

			if (cartModel != null)
			{
				cartModel.setChannel(SalesApplication.MOBILE);
				getModelService().save(cartModel);

				if (cartModel.getEntries() != null)
				{
					for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
					{
						if (cartEntryModel != null && !cartEntryModel.getGiveAway().booleanValue()
								&& cartEntryModel.getSelectedUSSID() != null)
						{
							freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
							freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
						}
					}
				}

				applyPromotions();
				LOG.debug("CartsController : setFreebieDeliverMode  : Step 2 Freebie map population done with Map Size"
						+ freebieModelMap.size());

				getMplCheckoutFacade().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);
				LOG.debug("CartsController : setFreebieDeliverMode  : Step 3 Freebie delivery mode set done");

				success = true;
			}
			else
			{
				success = false;
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		return success;
	}

	private void applyPromotions()
	{
		final CartModel cart = cartService.getSessionCart();
		//recalculating cart
		try
		{
			//commerceCartService.recalculateCart(cart);
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cart);
			getCalculationStrategy().recalculateCart(parameter);
		}
		catch (final Exception e)
		{
			LOG.error(" Exception in applyPromotions due to " + e);
		}
	}

	/**
	 * @description Select the delivery mode against the unique SKU (ussId)
	 * @param cartId
	 * @param deliverymode
	 * @param ussId
	 * @return response
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/selectDeliveryMode", method = RequestMethod.POST)
	@ResponseBody
	public WebSerResponseWsDTO selectDeliveryMode(@PathVariable final String cartId,
			@RequestParam(required = true, value = "deliverymodeussId") final String deliverymodeussId)
	{
		final WebSerResponseWsDTO response = new WebSerResponseWsDTO();
		try
		{
			final Map<String, String> delModeUssId = (Map<String, String>) JSON.parse(deliverymodeussId);
			Double finalDeliveryCost = Double.valueOf(0.0);
			for (final Map.Entry<String, String> element : delModeUssId.entrySet())
			{
				if (null != element && null != element.getValue() && null != element.getKey())
				{
					final Double deliveryCost = mplCustomAddressFacade.populateDeliveryMethodData(element.getValue(),
							element.getKey());
					finalDeliveryCost = Double.valueOf(finalDeliveryCost.doubleValue() + deliveryCost.doubleValue());

					LOG.debug("CartsController : selectDeliveryMode  : Step 1 finalDeliveryCost after Delivery Mode Set "
							+ finalDeliveryCost);
				}
			}
			if (setFreebieDeliverMode(cartId))
			{
				LOG.debug("CartsController : selectDeliveryMode  : Step 3 Freebie delivery mode set done");
				//applyPromotions();
				final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
				getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost, deliveryChargePromotionMap);
				response.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9203);
			}
		}

		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				response.setError(e.getMessage());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return response;
	}

	/**
	 * fetch pincode serviceability from OMS at Cart for all USSIDs present at entries of a cart
	 *
	 * @param pincode
	 * @return MplCartPinCodeResponseWsDTO
	 */
	@RequestMapping(value = "/{cartId}/checkPinCodeAtCart", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public MplCartPinCodeResponseWsDTO checkPinCodeAtCart(@RequestParam final String pincode)
	{
		final MplCartPinCodeResponseWsDTO response = new MplCartPinCodeResponseWsDTO();
		List<PinCodeResponseData> pinCodeResponse = null;
		try
		{
			LOG.debug(String.format("Checking servicibility for the pincode %s", pincode));
			pinCodeResponse = mplCartWebService.checkPinCodeAtCart(mplCartFacade.getSessionCartWithEntryOrdering(true), pincode);
			if (null != pinCodeResponse)
			{
				response.setPinCodeResponseList(pinCodeResponse);
				response.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9048);

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				response.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				response.setErrorCode(e.getErrorCode());
			}
			response.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return response;
	}

	/**
	 * @param fields
	 * @return addressListWsDTO
	 * @throws EtailBusinessExceptions
	 * @throws EtailNonBusinessExceptions
	 */
	public AddressListWsDTO addressList(final String fields) throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		AddressListWsDTO addressListWsDTO;
		if (null != accountAddressFacade)
		{
			final List<AddressData> addressList = accountAddressFacade.getAddressBook();
			final AddressDataList addressDataList = new AddressDataList();
			if (null != addressList)
			{
				addressDataList.setAddresses(addressList);
			}
			addressListWsDTO = dataMapper.map(addressDataList, AddressListWsDTO.class, fields);
			return addressListWsDTO;
		}
		else
		{
			return null;
		}
	}

	/*	*//**
		   * @description Update Transaction and related Retails for COD alos create Order
		   * @param paymentMode
		   *           (Json)
		   * @param totalCODCharge
		   * @param custName
		   * @param cartValue
		   * @param otpPin
		   * @return PaymentServiceWsData
		   * @throws EtailNonBusinessExceptions
		   */

	/*
	 *
	 * //GetOrderStatusResponse
	 *
	 * @Secured( { ROLE_CLIENT, CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	 *
	 * @RequestMapping(value = "/{cartId}/updateTransactionDetailsforCOD", method = RequestMethod.POST, produces =
	 * "application/json")
	 *
	 * @ResponseBody public PaymentServiceWsData updateTransactionDetailsforCOD(@RequestParam final String paymentMode,
	 *
	 * @PathVariable final String cartId, @PathVariable final String userId, @RequestParam final String custName,
	 *
	 * @RequestParam final Double cartValue, @RequestParam final Double totalCODCharge, @RequestParam final String
	 * otpPin) { PaymentServiceWsData updateTransactionDtls = new PaymentServiceWsData();
	 *
	 * LOG.debug(String.format("PaymentMode: %s | CartId: %s | UserId : %s | Cart value :  %s | COD charge:  %s",
	 * paymentMode, cartId, userId, cartValue, totalCODCharge));
	 *
	 * try { updateTransactionDtls = mplPaymentWebFacade.updateCODTransactionDetails(paymentMode, cartId, custName,
	 * cartValue, totalCODCharge, userId); } catch (final Exception e) {
	 * updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	 * updateTransactionDtls.setError(MarketplacecommerceservicesConstants.ORDER_ERROR);
	 * LOG.error(MarketplacewebservicesConstants.UPDATE_COD_TRAN_FAILED, e); }
	 *
	 * try { if (updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.UPDATE_SUCCESS)) {
	 * final UserModel user = extUserService.getUserForOriginalUid(userId); final String validationMsg =
	 * mplPaymentFacade.validateOTPforCODWeb(user.getUid(), otpPin); if (null != validationMsg) { if (validationMsg ==
	 * MarketplacecommerceservicesConstants.OTPVALIDITY) { //after validation create order final OrderData orderdata =
	 * mplCheckoutFacade.placeOrderByCartId(cartId, userId); if (orderdata != null) {
	 * updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
	 * updateTransactionDtls.setOrderId(orderdata.getCode()); } else {
	 * updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	 * updateTransactionDtls.setError(MarketplacecommerceservicesConstants.ORDER_ERROR); }
	 *
	 * } else { updateTransactionDtls.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
	 * updateTransactionDtls.setError(MarketplacecommerceservicesConstants.OTPERROR);
	 *
	 * } } } } catch (final EtailNonBusinessExceptions ex) { // Error message for All Exceptions
	 * ExceptionUtil.etailNonBusinessExceptionHandler(ex); if (null != ex.getErrorMessage()) {
	 * updateTransactionDtls.setError(ex.getErrorMessage()); } } catch (final EtailBusinessExceptions ex) { // Error
	 * message for All Exceptions ExceptionUtil.etailBusinessExceptionHandler(ex, null); if (null !=
	 * ex.getErrorMessage()) { updateTransactionDtls.setError(ex.getErrorMessage()); } } catch (final Exception ex) { //
	 * Error message for All Exceptions if (null != ((EtailBusinessExceptions) ex).getErrorMessage()) {
	 * updateTransactionDtls.setError(((EtailBusinessExceptions) ex).getErrorMessage()); } } return
	 * updateTransactionDtls;
	 *
	 * }
	 *
	 * // Update Transaction Details for Credit Card /Debit Card / EMI
	 *//**
	   * @Description Update Transaction and related Retails for Credit Card /Debit Card / EMI and Create Card
	   * @param paymentMode
	   *           (Json)
	   * @param orderStatusResponse
	   *           (Json)
	   * @return PaymentServiceWsData
	   * @throws EtailNonBusinessExceptions
	   */
	/*
	 * //GetOrderStatusResponse
	 *
	 * @Secured( { ROLE_CLIENT, CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	 *
	 * @RequestMapping(value = "/{cartId}/updateTransactionDetailsforCard", method = RequestMethod.POST, produces =
	 * "application/json")
	 *
	 * @ResponseBody public PaymentServiceWsData updateTransactionDetailsforCard(@RequestParam final String
	 * juspayOrderID,
	 *
	 * @RequestParam final String paymentMode, @PathVariable final String userId, @PathVariable final String cartId) {
	 * PaymentServiceWsData updateTransactionDtls = new PaymentServiceWsData();
	 *
	 * LOG.debug(String.format("Order Status Response : %s ", juspayOrderID)); LOG.debug(String.format(
	 * "PaymentMode: %s | CartId: %s | UserId : %s", paymentMode, cartId, userId));
	 *
	 * try { updateTransactionDtls = mplPaymentWebFacade.updateCardTransactionDetails(juspayOrderID, paymentMode, cartId,
	 * userId);
	 *
	 *
	 *
	 * LOG.debug(String.format("Update transaction details status %s ", ((null != updateTransactionDtls.getStatus()) ?
	 * updateTransactionDtls.getStatus() : ""))); } catch (final Exception e) {
	 * updateTransactionDtls.setError(MarketplacewebservicesConstants.UPDATE_CARD_TRAN_FAILED);
	 * LOG.error(MarketplacewebservicesConstants.UPDATE_CARD_TRAN_FAILED, e); } try { if (null !=
	 * updateTransactionDtls.getStatus() &&
	 * updateTransactionDtls.getStatus().equalsIgnoreCase(MarketplacewebservicesConstants.UPDATE_SUCCESS)) { final
	 * OrderData orderData = mplCheckoutFacade.placeOrderByCartId(cartId, userId); if (orderData != null) {
	 * updateTransactionDtls.setOrderId(orderData.getCode()); } else {
	 * updateTransactionDtls.setError(MarketplacewebservicesConstants.ORDER_ERROR); } } else {
	 * updateTransactionDtls.setError(MarketplacewebservicesConstants.PAYMENTUPDATE_ERROR); }
	 *
	 * } catch (final EtailNonBusinessExceptions ex) { // Error message for All Exceptions
	 * ExceptionUtil.etailNonBusinessExceptionHandler(ex); if (null != ex.getErrorMessage()) {
	 * updateTransactionDtls.setError(ex.getErrorMessage()); } } catch (final EtailBusinessExceptions ex) { // Error
	 * message for All Exceptions ExceptionUtil.etailBusinessExceptionHandler(ex, null); if (null !=
	 * ex.getErrorMessage()) { updateTransactionDtls.setError(ex.getErrorMessage()); } } catch (final Exception ex) { //
	 * Error message for All Exceptions if (null != ((EtailBusinessExceptions) ex).getErrorMessage()) {
	 * updateTransactionDtls.setError(((EtailBusinessExceptions) ex).getErrorMessage()); } } return
	 * updateTransactionDtls;
	 *
	 * }
	 */

	@SuppressWarnings(MarketplacewebservicesConstants.DEPRECATION)
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/getTopTwoWishlistForUser", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public GetWishListWsDTO getTopTwoWishlistForUser(@PathVariable final String userId,
			@RequestParam(required = false) final String pincode) throws RequestParameterException
	{
		GetWishListWsDTO getWishListWsDTO = new GetWishListWsDTO();
		boolean successFlag = true;
		boolean dtoFlag = true;
		if (userFacade.isAnonymousUser())
		{
			throw new AccessDeniedException("Access is denied");
		}
		Collection<CartModel> cartModelList = null;
		String error = null;
		String errorCode = null;
		try
		{
			if (userId != null)
			{
				cartModelList = mplCartFacade.getCartDetails(customerFacade.getCurrentCustomer().getUid());
				MplCustomerProfileData mplCustData = new MplCustomerProfileData();
				mplCustData = mplCustomerProfileService.getCustomerProfileDetail(userId);
				if (mplCustData == null)
				{
					throw new CMSItemNotFoundException("Customer data is null");
				}
				LOG.debug("Customer UID is : " + mplCustData.getUid());
				final UserModel user = userService.getUserForUID(mplCustData.getUid());
				getWishListWsDTO = mplCartFacade.getTopTwoWishlistForUser(user, pincode, cartModelList);
				if (null != getWishListWsDTO)
				{
					if (null != getWishListWsDTO.getStatus()
							&& getWishListWsDTO.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.ERROR_FLAG))
					{
						successFlag = false;
						if (null != getWishListWsDTO.getError() && !getWishListWsDTO.getError().isEmpty())
						{
							error = getWishListWsDTO.getError();
							errorCode = MarketplacecommerceservicesConstants.B9801;
						}
					}
				}
				else
				{
					dtoFlag = false;
				}

			}
			else
			{
				successFlag = false;
			}

		}
		catch (final CMSItemNotFoundException me)
		{
			// Error message for ModelSavingException Exceptions
			LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + me.getMessage());
			error = MarketplacecommerceservicesConstants.ERROR_FLAG;
			errorCode = MarketplacecommerceservicesConstants.B9800;
			successFlag = false;
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9800);
		}
		catch (InvalidCartException | ConversionException ex)
		{
			// Error message for ModelSavingException Exceptions
			LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex.getMessage());
			error = MarketplacecommerceservicesConstants.ERROR_FLAG;
			errorCode = MarketplacecommerceservicesConstants.B9800;
			successFlag = false;
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9800);
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			// Error message for EtailNonBusinessExceptions Exceptions
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			if (null != ex.getErrorMessage())
			{
				error = ex.getErrorMessage();
				errorCode = ex.getErrorCode();
				successFlag = false;
			}
		}
		catch (final EtailBusinessExceptions ex)
		{
			// Error message for EtailBusinessExceptions Exceptions
			ExceptionUtil.etailBusinessExceptionHandler(ex, null);
			if (null != ex.getErrorMessage())
			{
				error = ex.getErrorMessage();
				errorCode = ex.getErrorCode();
				successFlag = false;
			}
		}
		catch (final Exception ex)
		{
			// Error message for All Exceptions
			if (null != ((EtailNonBusinessExceptions) ex).getErrorMessage())
			{
				error = ((EtailNonBusinessExceptions) ex).getErrorMessage();
				errorCode = ((EtailNonBusinessExceptions) ex).getErrorCode();
				successFlag = false;
			}
		}

		if (successFlag && dtoFlag)
		{
			getWishListWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		//else if (successFlag && dtoFlag == false) Avoid unnecessary comparisons in boolean expressions
		else if (successFlag && !dtoFlag)
		{
			getWishListWsDTO = new GetWishListWsDTO();
			//getWishListWsDTO.setStatus(MarketplacecommerceservicesConstants.NOWISHLISTAVAILABLE);
		}
		//else if (dtoFlag && successFlag == false) Avoid unnecessary comparisons in boolean expressions
		else if (dtoFlag && !successFlag)
		{
			getWishListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != error && !error.isEmpty())
			{
				getWishListWsDTO.setError(error);
				getWishListWsDTO.setErrorCode(errorCode);
			}

		}

		return getWishListWsDTO;
	}

	/**
	 * @description method is called to resend the OTP Number for COD
	 * @param emailid
	 * @param fields
	 * @return ValidateOtpWsDto
	 * @throws DuplicateUidException
	 *            , InvalidKeyException ,NoSuchAlgorithmException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/resendOtpforcod", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ValidateOtpWsDto resendOTP(@PathVariable final String userId, @PathVariable final String cartId,
			@RequestParam final String mobilenumber) throws DuplicateUidException, InvalidKeyException, NoSuchAlgorithmException
	{
		final ValidateOtpWsDto validateOtpWsDto = new ValidateOtpWsDto();
		try
		{
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			if (null == customerData)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			final String mplCustomerID = customerData.getUid();
			final String mplCustomerName = customerData.getName() != null ? customerData.getName() : "";


			if (null != mplCustomerID && StringUtils.isNotEmpty(mplCustomerID))
			{

				if (null != mobilenumber && StringUtils.isNotEmpty(mobilenumber))
				{
					if (StringUtils.length(mobilenumber) == MarketplacecommerceservicesConstants.MOBLENGTH
							&& mobilenumber.matches(MarketplacecommerceservicesConstants.MOBILE_REGEX))
					{
						/////////
						final boolean notBlackListed = mplPaymentFacade.isMobileBlackListed(mobilenumber);
						if (notBlackListed)
						{ ////////
							final String validation = mplPaymentFacade.generateOTPforCODWeb(mplCustomerID, mobilenumber, mplCustomerName,
									cartId);
							if (null != validation && StringUtils.isNotEmpty(validation))
							{

								validateOtpWsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
							}
							else
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9022);
							}
						}
						else
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9202);
						}
					}
					else
					{
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9023);
					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9024);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				validateOtpWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				validateOtpWsDto.setErrorCode(e.getErrorCode());
			}
			validateOtpWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				validateOtpWsDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				validateOtpWsDto.setErrorCode(e.getErrorCode());
			}
			validateOtpWsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return validateOtpWsDto;
	}

	/**
	 * @description apply the Coupon at payment page and get discount
	 * @param cartId
	 * @param couponCode
	 * @return ApplyCouponsDTO
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 * @throws NumberFormatException
	 * @throws JaloInvalidParameterException
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/applyCoupons", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ApplyCouponsDTO applyCoupons(@PathVariable final String cartId, @RequestParam final String couponCode)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException, NumberFormatException,
			JaloInvalidParameterException, VoucherOperationException, CalculationException, JaloSecurityException
	{
		ApplyCouponsDTO applycouponDto = new ApplyCouponsDTO();
		CartModel cartModel = null;
		try
		{
			cartModel = mplPaymentWebFacade.findCartValues(cartId);
			if (cartModel == null)
			{
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + cartId);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
			}
			else
			{
				cartModel.setChannel(SalesApplication.MOBILE);
				getModelService().save(cartModel);
				applycouponDto = mplCouponWebFacade.applyVoucher(couponCode, cartModel);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				applycouponDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				applycouponDto.setErrorCode(e.getErrorCode());
			}
			applycouponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != cartModel)
			{
				applycouponDto.setTotal(String.valueOf(getMplCheckoutFacade()
						.createPrice(cartModel, cartModel.getTotalPriceWithConv()).getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				applycouponDto.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				applycouponDto.setErrorCode(e.getErrorCode());
			}
			applycouponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != cartModel)
			{
				applycouponDto.setTotal(String.valueOf(getMplCheckoutFacade()
						.createPrice(cartModel, cartModel.getTotalPriceWithConv()).getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
			}
		}
		return applycouponDto;
	}

	/**
	 * @description release the Coupon for the particular user
	 * @param cartId
	 * @param couponCode
	 * @return ReleaseCouponsDTO
	 * @throws RequestParameterException
	 * @throws WebserviceValidationException
	 * @throws MalformedURLException
	 * @throws NumberFormatException
	 * @throws JaloInvalidParameterException
	 * @throws VoucherOperationException
	 * @throws CalculationException
	 * @throws JaloSecurityException
	 * @throws JaloPriceFactoryException
	 * @throws CalculationException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/releaseCoupons", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ReleaseCouponsDTO releaseCoupons(@PathVariable final String cartId, @RequestParam final String couponCode)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException, NumberFormatException,
			JaloInvalidParameterException, VoucherOperationException, CalculationException, JaloSecurityException,
			JaloPriceFactoryException, CalculationException
	{
		ReleaseCouponsDTO releaseCouponDto = new ReleaseCouponsDTO();
		CartModel cartModel = null;
		try
		{
			cartModel = mplPaymentWebFacade.findCartValues(cartId);
			if (cartModel == null)
			{
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + cartId);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
			}
			else
			{
				cartModel.setChannel(SalesApplication.MOBILE);
				getModelService().save(cartModel);
				releaseCouponDto = mplCouponWebFacade.releaseVoucher(couponCode, cartModel);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				releaseCouponDto.setError(e.getErrorMessage());
			}
			releaseCouponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != cartModel)
			{
				releaseCouponDto.setTotal(String.valueOf(getMplCheckoutFacade()
						.createPrice(cartModel, cartModel.getTotalPriceWithConv()).getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				releaseCouponDto.setError(e.getErrorMessage());
			}
			releaseCouponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			if (null != cartModel)
			{
				releaseCouponDto.setTotal(String.valueOf(getMplCheckoutFacade()
						.createPrice(cartModel, cartModel.getTotalPriceWithConv()).getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
			}
		}
		return releaseCouponDto;
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
	 * @return the extUserService
	 */
	public ExtendedUserService getExtUserService()
	{
		return extUserService;
	}

	/**
	 * @param extUserService
	 *           the extUserService to set
	 */
	public void setExtUserService(final ExtendedUserService extUserService)
	{
		this.extUserService = extUserService;
	}

}
