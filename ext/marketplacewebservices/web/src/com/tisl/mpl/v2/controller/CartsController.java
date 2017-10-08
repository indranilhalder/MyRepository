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

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.SaveCartFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModesData;
import de.hybris.platform.commercefacades.order.data.OrderData;
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
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.promotion.CommercePromotionRestrictionException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
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
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.http.ParseException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
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
import com.tis.mpl.facade.data.ProductValidationData;
import com.tisl.mpl.cart.impl.CommerceWebServicesCartFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
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
import com.tisl.mpl.facade.checkout.storelocator.MplStoreLocatorFacade;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplCouponWebFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.MplSlaveMasterFacade;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.payment.MplPaymentFacade;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.helper.AddToCartHelper;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCommerceCartServiceImpl;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;
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
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListRequestWsDTO;
import com.tisl.mpl.wsdto.MplCartPinCodeResponseWsDTO;
import com.tisl.mpl.wsdto.MplEDDInfoWsDTO;
import com.tisl.mpl.wsdto.MplSelectedEDDInfoWsDTO;
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
	@Resource
	private ModelService modelService;
	@Resource
	private MplPaymentWebFacade mplPaymentWebFacade;
	@Resource
	private MplCartFacade mplCartFacade;
	@Resource
	private MplCommerceCartServiceImpl mplCommerceCartService;
	@Resource
	private MplCartWebService mplCartWebService;
	@Resource
	private MplAccountAddressFacade accountAddressFacade;
	@Resource
	private UserService userService;
	@Resource(name = "mplCheckoutFacade")
	private MplCheckoutFacade mplCheckoutFacade;

	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private ExtendedUserService extUserService;
	@Resource(name = "mplPaymentFacade")
	private MplPaymentFacade mplPaymentFacade;
	@Resource
	private WishlistFacade wishlistFacade;
	@Resource
	private SiteConfigService siteConfigService;
	@Resource(name = "mplStoreLocatorFacade")
	private MplStoreLocatorFacade mplStoreLocatorFacade;
	@Resource
	private BaseSiteService baseSiteService;
	@Autowired
	private MplCouponWebFacade mplCouponWebFacade;
	@Resource(name = "mplSlaveMasterFacade")
	private MplSlaveMasterFacade mplSlaveMasterFacade;
	@Resource(name = "discountUtility")
	private DiscountUtility discountUtility;


	@Resource(name = "addToCartHelper")
	private AddToCartHelper addToCartHelper;


	@Resource(name = "voucherService")
	private VoucherService voucherService;

	@Resource(name = "mplCouponFacade")
	private MplCouponFacade mplCouponFacade;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	//TPR-6971
	@Resource(name = "exchangeGuideFacade")
	private ExchangeGuideFacade exchangeFacade;

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


	//	@Autowired
	//	private CartService cartService;
	//@Autowired
	//private CommerceCartService commerceCartService;
	@Resource(name = "cartService")
	protected CartService cartService;

	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;

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


	//private static final String DEPRECATION = "deprecation";
	private static final String APPLICATION_TYPE = "application/json";
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
	private static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";


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
		cmd.setDeliveryModeChanged(Boolean.valueOf(Boolean.TRUE.equals(cmd1.getDeliveryModeChanged())
				|| Boolean.TRUE.equals(cmd2.getDeliveryModeChanged())));
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
			throw new CartEntryException("Ambiguous cart entries! Entry number " + currentEntry.getEntryNumber()
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
	public OrderEntryListWsDTO getCartEntries(@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
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
			throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException
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
			throws CommerceCartModificationException, WebserviceValidationException, ProductLowStockException, StockSystemException
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

		return updateCartEntryInternal(baseSiteId, cart, orderEntry, qty, pickupStore, fields, true, null);
	}

	private CartModificationWsDTO updateCartEntryInternal(final String baseSiteId, final CartData cart,
			final OrderEntryData orderEntry, final Long qty, final String pickupStore, final String fields, final boolean putMode,
			final CartModel cartModel) throws CommerceCartModificationException
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
				//				cartModificationData1 = cartFacade.updateCartEntry(entryNumber, pickupStore);
				cartModificationData1 = mplCartFacade.updateCartEntryMobile(entryNumber, pickupStore, cartModel);
			}
		}
		else if (putMode && currentPointOfService != null)
		{
			//was 'pickup in store', now switch to 'shipping mode'
			validateForAmbiguousPositions(cart, orderEntry, pickupStore);
			validateIfProductIsInStockOnline(baseSiteId, productCode, Long.valueOf(entryNumber));
			cartModificationData1 = mplCartFacade.updateCartEntryMobile(entryNumber, pickupStore, cartModel);
		}

		if (qty != null)
		{
			cartModificationData2 = mplCartFacade.updateCartEntryMobile(entryNumber, qty.longValue(), cartModel);
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

		return updateCartEntryInternal(baseSiteId, cart, orderEntry, entry.getQuantity(), pickupStore, fields, true, null);
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

		return updateCartEntryInternal(baseSiteId, cart, orderEntry, qty, pickupStore, fields, false, null);
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
		return updateCartEntryInternal(baseSiteId, cart, orderEntry, entry.getQuantity(), pickupStore, fields, false, null);
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
	 *             defaultAddress
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
			throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException, UnsupportedRequestException
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
			throws WebserviceValidationException, InvalidPaymentInfoException, NoCheckoutCartException, UnsupportedRequestException
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
	public void removePromotion(@PathVariable final String promotionId) throws CommercePromotionRestrictionException,
			NoCheckoutCartException
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
	public void applyVoucherForCart(@RequestParam(required = true) final String voucherId) throws NoCheckoutCartException,
			VoucherOperationException
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
	public void releaseVoucherFromCart(@PathVariable final String voucherId) throws NoCheckoutCartException,
			VoucherOperationException
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
					//TPR-1083 Online Exchange facilities to the customer for Large Appliances
					//	cartFacade.restoreAnonymousCartAndMerge(oldCartId, toMergeCartGuid);
					mplCartWebService.restoreAnonymousCartAndMerge(oldCartId, toMergeCartGuid);
					cart = getSessionCart();
					if (null != cart && StringUtils.isNotEmpty(cart.getCode()))
					{
						if (LOG.isDebugEnabled())
						{
							LOG.debug("**************** Created cart mobile web service *********************" + cart.getCode());
						}
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
						if (LOG.isDebugEnabled())
						{
							LOG.debug("**************** Created cart mobile web service *********************" + cart.getGuid());
						}
						result.setGuid(cart.getGuid());
					}
					try
					{
						newCartModel = mplPaymentWebFacade.findCartValues(cart.getCode());
						deListedStatus = mplCartFacade.isCartEntryDelisted(newCartModel);
						//newCartModel = mplCartFacade.removeDeliveryMode(newCartModel);
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

					final CartData cartData = mplExtendedCartConverter.convert(newCartModel);

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
					newCartModel = mplPaymentWebFacade.findCartValues(cart.getCode());
					deListedStatus = mplCartFacade.isCartEntryDelisted(newCartModel);
					//newCartModel = mplCartFacade.removeDeliveryMode(newcartModel);
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
			@RequestParam(required = true) final boolean addedToCartWl, @RequestParam(required = false) final String channel,
			@RequestParam(required = false) final String l3code, @RequestParam(required = false) final String exchangeParam,
			@RequestParam(required = false) final String brandParam, @RequestParam(required = false) final String pinParam)
			throws InvalidCartException, CommerceCartModificationException
	{
		WebSerResponseWsDTO result = new WebSerResponseWsDTO();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("**************** Adding ptoduct to cart mobile web service *********************" + cartId + "::: USSID ::::"
					+ USSID + ":::: quantity ::::" + quantity + ":::: productCode ::::" + productCode);
		}
		try
		{
			final String exParam = l3code + "|" + exchangeParam + "|" + brandParam + "|" + pinParam;
			//INC144313608
			//final boolean isProductFreebie = getAddToCartHelper().isProductFreebie(productCode);

			/* cart not opening issue --due to ussid mismatch */

			final ProductValidationData isProductValid = getAddToCartHelper().isProductValid(productCode, USSID);


			if (isProductValid != null
					&& (!isProductValid.getValidproduct().booleanValue() || isProductValid.getFreebie().booleanValue())) //ussid valid or not and freebie product or not
			{
				result.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

			}
			else if (StringUtils.isNotEmpty(l3code) && StringUtils.isNotEmpty(exchangeParam) && StringUtils.isNotEmpty(brandParam)
					&& StringUtils.isNotEmpty(pinParam))
			{
				result = mplCartWebService.addProductToCartwithExchange(productCode, cartId, quantity, USSID, addedToCartWl, channel,
						exParam);
			}
			else
			{
				result = mplCartWebService.addProductToCart(productCode, cartId, quantity, USSID, addedToCartWl, channel);
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
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields,
			@RequestParam(required = false) final String channel)
	{
		final AddressListWsDTO addressListDTO = addressList(fields);
		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		try
		{
			if (null != cartId)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ get cart details mobile web service *********" + cartId);
				}
				cartDataDetails = mplCartWebService.getCartDetails(cartId, addressListDTO, pincode, channel);
				final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
				cartDataDetails.setMaxAllowed(maximum_configured_quantiy);
			}
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
			if (null != e.getErrorCode() && e.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.B9038))
			{
				cartDataDetails.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			if (null != e.getErrorCode())
			{
				cartDataDetails.setErrorCode(e.getErrorCode());
			}
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}

		}
		//TPR-799
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			cartDataDetails.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			cartDataDetails.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
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
	public CartDataDetailsWsDTO removeCartEntryMobile(@PathVariable final String cartId, @PathVariable final Long entryNumber)
			throws CommerceCartModificationException, InvalidCartException, ConversionException
	{
		final CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO(); //Object to store result
		int count = 0;
		String selectedUSSID = MarketplacecommerceservicesConstants.EMPTY;
		final List<Wishlist2EntryModel> entryModelList = new ArrayList<>();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("removeCartEntry: " + logParam(ENTRY_NUMBER, entryNumber));
		}
		try
		{
			CartModel cartModel = null;

			//CAR Project performance issue fixed
			//				cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartId);
			cartModel = cartService.getSessionCart();
			try
			{
				//cartFacade.updateCartEntry(entryNumber, 0);
				mplCartFacade.updateCartEntryMobile(entryNumber.longValue(), 0, cartModel);
			}
			catch (final Exception e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9056);
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

			//CAR Project performance issue fixed

			double discount = 0.0;
			double totalPrice = 0.0D;
			if (null != cartModel.getEntries() && !cartModel.getEntries().isEmpty())
			{
				for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
				{
					if (cartEntry.getEntryNumber().intValue() == entryNumber.intValue())
					{
						selectedUSSID = cartEntry.getSelectedUSSID();
					}
					if (null != cartEntry.getGiveAway() && !cartEntry.getGiveAway().booleanValue())
					{
						count++;
					}
					totalPrice = totalPrice + (cartEntry.getBasePrice().doubleValue() * cartEntry.getQuantity().doubleValue());
				}

				discount = (totalPrice + cartModel.getDeliveryCost().doubleValue() + cartModel.getConvenienceCharges().doubleValue())
						- cartModel.getTotalPriceWithConv().doubleValue();

				cartDataDetails.setCount(count);
			}
			if (discount >= 0)
			{
				final PriceData priceDiscount = discountUtility.createPrice(cartModel, Double.valueOf(discount));
				if (null != priceDiscount && null != priceDiscount.getValue())
				{
					cartDataDetails.setDiscountPrice(String.valueOf(priceDiscount.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			final List<Wishlist2EntryModel> allWishlistEntry = wishlistFacade.getAllWishlistByUssid(selectedUSSID);
			for (final Wishlist2EntryModel entryModel : allWishlistEntry)
			{
				entryModel.setAddToCartFromWl(Boolean.FALSE);
				entryModelList.add(entryModel);
			}
			//For saving all the data at once rather in loop;
			modelService.saveAll(entryModelList);
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);

			cartModel.setChannel(SalesApplication.MOBILE);
			getModelService().save(cartModel);
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
		CartDataDetailsWsDTO cartDataDetails = null;
		cartDataDetails = new CartDataDetailsWsDTO();
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		CartModel cartModel = null;
		CartData cartData = null;
		try
		{

			//	cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartId);

			//CAR Project performance issue fixed
			cartModel = cartService.getSessionCart();

			if (cartModel != null)
			{
				//duplicate cart fix for mobile and delisting
				final boolean deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cartModel);
				LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);

				cartData = mplExtendedCartConverter.convert(cartModel);
				final OrderEntryData orderEntry = getCartEntryForNumber(cartData, entryNumber);

				if (quantity == null && StringUtils.isEmpty(pickupStore))
				{
					LOG.debug(MarketplacecommerceservicesConstants.FIELD_NOT_EMPTY_MSG);
					cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					cartDataDetails.setError(MarketplacecommerceservicesConstants.FIELD_QUANTITY
							+ MarketplacecommerceservicesConstants.SINGLE_SPACE
							+ MarketplacecommerceservicesConstants.FIELD_NOT_EMPTY_MSG);
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

				//TPR-6117
				final boolean checkMaxLimList = mplCartFacade.checkMaxLimitUpdate(entryNumber, quantity.longValue());
				if (!checkMaxLimList)
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9110);
				}
				//TPR-6117

				updateCartEntryInternal(baseSiteId, cartData, orderEntry, quantity, pickupStore, fields, false, cartModel);
				//final CartModel newCartModel = mplCartFacade.removeDeliveryMode(cartModel);
				final List<AbstractOrderEntryModel> abstractOrderEntryList = cartModel.getEntries();
				final List<GetWishListProductWsDTO> gwlpList = new ArrayList<GetWishListProductWsDTO>();
				List<GetWishListProductWsDTO> gwlpFreeItemList = new ArrayList<GetWishListProductWsDTO>();
				GetWishListProductWsDTO gwlp = null;
				final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
				for (final AbstractOrderEntryModel abstractOrderEntry : abstractOrderEntryList)
				{
					if (null != abstractOrderEntry)
					{
						gwlp = new GetWishListProductWsDTO();

						if (StringUtils.isNotEmpty((abstractOrderEntry.getQuantity().toString())))
						{
							/////////// TISSAM-14
							for (final AbstractOrderEntryModel pr : cartModel.getEntries())
							{
								if (pr.getQuantity().longValue() >= maximum_configured_quantiy)
								{
									throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9065);
								}
								/*
								 * if ((abstractOrderEntry.getQuantity().longValue() + pr.getQuantity().longValue()) >
								 * maximum_configured_quantiy) { throw new
								 * EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9066); }
								 */
								break;

							}
							////////
							gwlp.setQtySelectedByUser(abstractOrderEntry.getQuantity().toString());

							//TPR-1083
							if (StringUtils.isNotEmpty(abstractOrderEntry.getExchangeId()))
							{
								gwlp.setMaxQuantityAllowed(MarketplacewebservicesConstants.MAXIMUM_CONFIGURED_QUANTIY_FOR_EXCHANGE);
							}
							//TPR-6117 STARTS
							else if (abstractOrderEntry.getProduct().getMaxOrderQuantity() != null
									&& abstractOrderEntry.getProduct().getMaxOrderQuantity().intValue() > 0
									&& abstractOrderEntry.getProduct().getMaxOrderQuantity().intValue() < maximum_configured_quantiy)
							{
								gwlp.setMaxQuantityAllowed(abstractOrderEntry.getProduct().getMaxOrderQuantity().toString());
							}
							else
							{
								gwlp.setMaxQuantityAllowed(String.valueOf(maximum_configured_quantiy));
							}

							//TPR-6117 Ends

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
				if (StringUtils.isNotEmpty(cartModel.getSubtotal().toString()))
				{
					final PriceData subtotalprice = discountUtility.createPrice(cartModel,
							Double.valueOf(cartModel.getSubtotal().toString()));
					if (null != subtotalprice && null != subtotalprice.getValue())
					{
						cartDataDetails
								.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
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
				if (deListedStatus)
				{
					delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
					cartDataDetails.setDelistedMessage(delistMessage);
				}

			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9040);
			}
		}
		catch (final ConversionException ce)
		{
			cartDataDetails = new CartDataDetailsWsDTO();
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ce);
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			cartDataDetails.setError(MarketplacecommerceservicesConstants.COULD_NOT_MODIFY_CART + ce);
			cartDataDetails.setErrorCode(MarketplacecommerceservicesConstants.B9056);
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
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				cartDataDetails.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				cartDataDetails.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
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
		CartModel cartModel = null;
		CartData cartDataOrdered = null;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		List<GetWishListProductWsDTO> gwlpList = new ArrayList<GetWishListProductWsDTO>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		//CAR-57
		List<PinCodeResponseData> pinCodeRes = null;
		try
		{
			if (userFacade.isAnonymousUser())
			{
				cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Anonymous cart mobile cartCheckout**************" + cartId);
				}
			}
			else
			{
				cartModel = mplPaymentWebFacade.findCartValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Logged-in cart mobile cartCheckout**************" + cartId);
				}
			}
			// Validate Cart Model is not null
			if (null != cartModel)
			{
				final boolean deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cartModel);
				LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus + "productCheckout:" + cartId);
				//cartData = getMplExtendedCartConverter().convert(cartModel);
				cartDataOrdered = mplCartFacade.getSessionCartWithEntryOrderingMobile(cartModel, true, true, false);
				/**** Pincode check Details ***/
				try
				{
					if (null != postalCode && !postalCode.isEmpty())
					{
						//gwlpList = productDetails(cartModel, cartData, aoem, true, pincode, true, cartId);
						LOG.debug("************ Mobile webservice Pincode check at OMS Mobile *******" + postalCode);
						//CAR-57
						pinCodeRes = mplCartWebService.checkPinCodeAtCart(cartDataOrdered, cartModel, postalCode);
						deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartDataOrdered, pinCodeRes, cartModel);
						LOG.debug("************ Mobile webservice DeliveryModeData Map Mobile *******" + deliveryModeDataMap);
					}
				}
				catch (final Exception e)
				{
					LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK, e);
				}

				/* Product Details */
				if (StringUtils.isNotEmpty(postalCode))
				{
					//CAR-57
					gwlpList = mplCartWebService.productDetails(cartModel, deliveryModeDataMap, true, false, pinCodeRes, postalCode);
				}
				else
				{
					//CAR-57
					gwlpList = mplCartWebService.productDetails(cartModel, deliveryModeDataMap, false, false, pinCodeRes, postalCode);
				}

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
			else
			{
				//If  Cart Model is null display error message
				cartDetailsData.setError(MarketplacewebservicesConstants.CARTMODELEMPTY);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9050);
			}
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
		catch (final Exception ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex);
			// Error message for All Exceptions
			if (null != ex.getMessage())
			{
				cartDetailsData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
				cartDetailsData.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			}
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return cartDetailsData;
	}

	/**
	 * Returns order summary for mobile. --TPR-629
	 *
	 * @param cartId
	 * @param pincode
	 * @param userId
	 * @return cData
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/displayOrderSummary", method = RequestMethod.GET)
	@ResponseBody
	public CartDataDetailsWsDTO displayOrderSummary(@PathVariable final String userId, @PathVariable final String cartId,
			@RequestParam final String pincode, @RequestParam(required = false) final String cartGuid)
	{
		CartDataDetailsWsDTO cartDetailsData = new CartDataDetailsWsDTO();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("displayOrderSummary:  cartId : " + cartId + "cartGuid : " + cartGuid + ", pincode:" + pincode);
		}
		try
		{
			cartDetailsData = mplPaymentWebFacade.displayOrderSummary(userId, cartId, cartGuid, pincode);
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
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
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
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			cartDetailsData.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
			cartDetailsData.setErrorCode(MarketplacecommerceservicesConstants.B9004);
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return cartDetailsData;
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
			@RequestBody final InventoryReservListRequestWsDTO item, @RequestParam final String type)
	{
		ReservationListWsDTO reservationList = new ReservationListWsDTO();
		CartModel cart = null;
		try
		{
			LOG.debug("******************* Soft reservation Mobile web service ******************" + cartId + pincode);
			//	cart = mplPaymentWebFacade.findCartValues(cartId);
			//CAR Project performance issue fixed
			cart = cartService.getSessionCart();
			if (setFreebieDeliverMode(cart))
			{
				//added for CAR:127
				final CartData caData = mplCartFacade.getCartDataFromCartModel(cart, false);
				//commented for CAR:127
				//reservationList = mplCommerceCartService.getReservation(cart, pincode, type);
				reservationList = mplCommerceCartService.getReservation(caData, pincode, type, cart, item, SalesApplication.MOBILE);
				//INC144317815
				if (reservationList == null || CollectionUtils.isEmpty(reservationList.getReservationItem()))//INC144317815
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9201);
				}
				LOG.debug("******************* Soft reservation Mobile web service response received from OMS ******************"
						+ cartId);

				Boolean replaced = Boolean.FALSE;
				replaced = sessionService.getAttribute(MarketplacecommerceservicesConstants.REPLACEDUSSID);
				LOG.debug("Replaced Soft reservation ForJewellery is " + replaced);
				if (null != replaced && replaced.booleanValue())
				{
					reservationList.setPriceChangeNotificationMsg(MarketplacecommerceservicesConstants.INVENTORY_RESV_JWLRY_CART);
					sessionService.removeAttribute(MarketplacecommerceservicesConstants.REPLACEDUSSID);
					//Added as per comments in TISJEW-4481
					reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
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
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			reservationList.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			reservationList.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return reservationList;
	}

	/**
	 * Cart Reservation for Payment. --TPR-629
	 *
	 * @queryparam fields Response configuration (list of fields, which should be returned in response)
	 * @return Details of cart and it's entries
	 */

	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/softReservationForPayment", method = RequestMethod.POST)
	@ResponseBody
	public ReservationListWsDTO getCartReservationForPayment(@RequestParam final String cartGuid,
			@RequestParam final String pincode, @RequestBody final InventoryReservListRequestWsDTO item)
	{
		ReservationListWsDTO reservationList = new ReservationListWsDTO();
		CartModel cart = null;
		OrderModel orderModel = null;
		OrderData orderData = null;
		boolean deListedStatus = false;
		boolean delvieryModeset = false;
		String delistMessage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			//getSessionService().setAttribute(MarketplacewebservicesConstants.PAYMENTMODEFORPROMOTION, paymentMode);
			//LOG.debug("************ Logged-in cart mobile soft reservation paymentMode **************" + paymentMode);
			/*
			 * if (StringUtils.isNotEmpty(bankName)) {
			 * getSessionService().setAttribute(MarketplacewebservicesConstants.BANKFROMBIN, bankName); } else { BinModel
			 * bin = null; if (StringUtils.isNotEmpty(binNo)) { bin = getBinService().checkBin(binNo); } if (null != bin &&
			 * StringUtils.isNotEmpty(bin.getBankName())) {
			 * getSessionService().setAttribute(MarketplacewebservicesConstants.BANKFROMBIN, bin.getBankName());
			 *
			 * LOG.debug("************ Logged-in cart mobile soft reservation BANKFROMBIN **************" +
			 * bin.getBankName()); } }
			 */
			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
			}
			if (null == orderModel)
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				//CAR:127
				final CartData caData = mplCartFacade.getCartDataFromCartModel(cart, false);
				//CAR:127
				delvieryModeset = setFreebieDeliverMode(cart);
				LOG.debug("************ Logged-in cart mobile checking validity of promotion **************" + cartGuid);
				if (!mplCheckoutFacade.isPromotionValid(cart))
				{
					//Added For TPR-1035
					//bin = getBinService().checkBin(binNo, paymentMode, null, false);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9075);
				}
				try
				{
					//duplicate cart fix for mobile
					deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cart);
					LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus + "::::" + cartGuid);
				}
				catch (final Exception ex)
				{
					throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B2002);
				}
				if (deListedStatus)
				{
					cart = mplCartFacade.removeDeliveryMode(cart);
					LOG.debug("************ Logged-in cart mobile delisting success **************" + cartGuid);
					delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
					reservationList.setDelistedMessage(delistMessage);
					reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
				if (delvieryModeset)
				{
					//commented for CAR:127
					/*
					 * reservationList = mplCommerceCartService.getReservation(cart, pincode,
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING);
					 */
					reservationList = mplCommerceCartService.getReservation(caData, pincode,
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, cart, item,
							SalesApplication.MOBILE);

					Boolean replaced = Boolean.FALSE;
					replaced = sessionService.getAttribute(MarketplacecommerceservicesConstants.REPLACEDUSSID);
					LOG.debug("Replaced Soft reservation ForJewellery is " + replaced);
					if (null != replaced && replaced.booleanValue())
					{
						reservationList.setPriceChangeNotificationMsg(MarketplacecommerceservicesConstants.INVENTORY_RESV_JWLRY_CART);
						sessionService.removeAttribute(MarketplacecommerceservicesConstants.REPLACEDUSSID);
						//Added as per comments in TISJEW-4481
						reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					}

				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9201);
				}
			}
			else
			{
				delvieryModeset = setFreebieDeliverMode(orderModel);
				LOG.debug("************ Logged-in cart mobile checking validity of promotion **************" + cartGuid);
				if (!mplCheckoutFacade.isPromotionValid(orderModel))
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9075);
				}
				LOG.debug("************ Logged-in cart mobile promotion is valid **************" + cartGuid);
				try
				{
					LOG.debug("************ Logged-in cart mobile delisting **************" + cartGuid);
					//duplicate cart fix for mobile
					//not happen if order is already placed
					//deListedStatus = mplCartFacade.isCartEntryDelistedMobile(orderModel);
					LOG.debug(MarketplacecommerceservicesConstants.CART_DELISTED_STATUS + deListedStatus);
				}
				catch (final Exception ex)
				{
					throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B2002);
				}
				if (deListedStatus)
				{
					//cart = mplCartFacade.removeDeliveryMode2(orderModel);
					LOG.debug("************ Logged-in cart mobile delisting success **************" + cartGuid);
					delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
					reservationList.setDelistedMessage(delistMessage);
					reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				}
				if (delvieryModeset)
				{
					//commented for CAR:127
					/*
					 * reservationList = mplCommerceCartService.getReservation(orderModel, pincode,
					 * MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING);
					 */
					//added for CAR:127
					orderData = mplCheckoutFacade.getOrderDetailsForCode(orderModel);
					reservationList = mplCommerceCartService.getReservation(orderData, pincode,
							MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_PAYMENTPENDING, orderModel, item,
							SalesApplication.MOBILE);

					Boolean replacedPay = Boolean.FALSE;
					replacedPay = sessionService.getAttribute(MarketplacecommerceservicesConstants.REPLACEDUSSID);
					LOG.debug("Replaced Soft reservation ForJewellery is " + replacedPay);
					if (null != replacedPay && replacedPay.booleanValue())
					{
						reservationList.setPriceChangeNotificationMsg(MarketplacecommerceservicesConstants.INVENTORY_RESV_JWLRY_CART);
						sessionService.removeAttribute(MarketplacecommerceservicesConstants.REPLACEDUSSID);
						//Added as per comments in TISJEW-4481
						reservationList.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
					}

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
	 * @param cartModel
	 * @return boolean
	 */
	private boolean setFreebieDeliverMode(final AbstractOrderModel cartModel)
	{
		boolean success = false;

		try
		{
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();
			if (cartModel != null)
			{
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

				//applyPromotions(cartModel);
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

	/**
	 * @description Select the delivery mode against the unique SKU (ussId)
	 * @param cartId
	 * @param deliverymodeussId
	 * @return response
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/{cartId}/selectDeliveryMode", method = RequestMethod.POST)
	@ResponseBody
	public WebSerResponseWsDTO selectDeliveryMode(@PathVariable final String cartId,
			@RequestParam(required = true, value = "deliverymodeussId") final String deliverymodeussId,
			@RequestParam(required = false, defaultValue = "false", value = "removeExchange") final boolean removeExchangefromCNCcart)
	{
		final WebSerResponseWsDTO response = new WebSerResponseWsDTO();
		CartModel cart = null;
		try
		{
			//cart = mplPaymentWebFacade.findCartValues(cartId);
			//CAR Project performance issue fixed
			cart = cartService.getSessionCart();
			final Map<String, String> delModeUssId = (Map<String, String>) JSON.parse(deliverymodeussId);
			Double finalDeliveryCost = Double.valueOf(0.0);
			for (final Map.Entry<String, String> element : delModeUssId.entrySet())
			{
				if (null != element && null != element.getValue() && null != element.getKey())
				{
					final Double deliveryCost = mplCustomAddressFacade.populateDeliveryMethodData(element.getValue(),
							element.getKey(), cart);
					finalDeliveryCost = Double.valueOf(finalDeliveryCost.doubleValue() + deliveryCost.doubleValue());

					LOG.debug("CartsController : selectDeliveryMode  : Step 1 finalDeliveryCost after Delivery Mode Set "
							+ finalDeliveryCost);
				}
			}
			//if cart doesn't contain cnc products clean up pickup person details
			cleanupPickUpDetails(cart, removeExchangefromCNCcart);

			if (setFreebieDeliverMode(cart))
			{
				LOG.debug("CartsController : selectDeliveryMode  : Step 3 Freebie delivery mode set done");
				//applyPromotions();
				final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
				getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost, deliveryChargePromotionMap, cart);
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
			ExceptionUtil.getCustomizedExceptionTrace(e);
			response.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9004));
			response.setErrorCode(MarketplacecommerceservicesConstants.B9004);
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
	public MplCartPinCodeResponseWsDTO checkPinCodeAtCart(@PathVariable final String cartId, @RequestParam final String pincode)
	{
		final MplCartPinCodeResponseWsDTO response = new MplCartPinCodeResponseWsDTO();
		List<PinCodeResponseData> pinCodeResponse = null;
		CartModel cart = null;
		try
		{
			LOG.debug(String.format("Checking servicibility for the pincode %s", pincode));

			//anonymous user cart issue fixed
			if (userFacade.isAnonymousUser())
			{
				cart = mplPaymentWebFacade.findCartAnonymousValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Anonymous cart mobile cartCheckout**************" + cartId);
				}
			}
			else
			{
				cart = mplPaymentWebFacade.findCartValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Logged-in cart mobile cartCheckout**************" + cartId);
				}
			}
			//	cart = mplPaymentWebFacade.findCartValues(cartId);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("checkPinCodeAtCart-------" + cart.getCode());
			}

			pinCodeResponse = mplCartWebService.checkPinCodeAtCart(
					mplCartFacade.getSessionCartWithEntryOrderingMobile(cart, true, false, false), cart, pincode);
			if (null != pinCodeResponse)
			{
				response.setPinCodeResponseList(pinCodeResponse);
				response.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
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
		//TPR-799
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			response.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			response.setErrorCode(MarketplacecommerceservicesConstants.E0000);
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
		CartModel cartModel = null;
		String error = null;
		String errorCode = null;
		try
		{
			//anonymous user must not get toptwo wishlist
			if (!userFacade.isAnonymousUser())
			{
				//				MplCustomerProfileData mplCustData = new MplCustomerProfileData();
				//				mplCustData = mplCustomerProfileService.getCustomerProfileDetail(userId);

				final UserModel user = userService.getCurrentUser();

				if (user == null)
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9007);
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Customer UID is : " + user);
				}
				//	final UserModel user = userService.getUserForUID(mplCustData.getUid());
				cartModel = commerceCartService.getCartForGuidAndSiteAndUser(null, baseSiteService.getCurrentBaseSite(), user);
				//final CartData cartData = getSessionCart();
				final CartData cartData = mplExtendedCartConverter.convert(cartModel);
				getWishListWsDTO = mplCartFacade.getTopTwoWishlistForUser(user, pincode, cartData);
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
		catch (final ConversionException ex)
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
			// Error message for Exceptions
			LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + ex.getMessage());
			error = MarketplacecommerceservicesConstants.ERROR_FLAG;
			errorCode = MarketplacecommerceservicesConstants.B9800;
			successFlag = false;

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
	 * @description method is called to resend the OTP Number for COD --TPR-629
	 * @return ValidateOtpWsDto
	 * @throws DuplicateUidException
	 *            , InvalidKeyException ,NoSuchAlgorithmException
	 */
	@Secured(
	{ CUSTOMER, TRUSTED_CLIENT, CUSTOMERMANAGER })
	@RequestMapping(value = "/resendOtpforcod", method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ValidateOtpWsDto resendOTP(@PathVariable final String userId, @RequestParam final String cartGuid,
			@RequestParam final String mobilenumber) throws DuplicateUidException, InvalidKeyException, NoSuchAlgorithmException
	{
		final ValidateOtpWsDto validateOtpWsDto = new ValidateOtpWsDto();
		OrderModel orderModel = null;
		CartModel cart = null;
		String validation = null;
		try
		{
			final CustomerData customerData = customerFacade.getCurrentCustomer();
			if (null == customerData)
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9025);
			}
			final String mplCustomerID = customerData.getDisplayUid();
			final String mplCustomerName = customerData.getName() != null ? customerData.getName() : "";
			if (StringUtils.isNotEmpty(mplCustomerID))
			{
				if (StringUtils.isNotEmpty(mobilenumber))
				{
					if (StringUtils.length(mobilenumber) == MarketplacecommerceservicesConstants.MOBLENGTH
							&& mobilenumber.matches(MarketplacecommerceservicesConstants.MOBILE_REGEX))
					{
						/////////
						final boolean notBlackListed = mplPaymentFacade.isMobileBlackListed(mobilenumber);
						if (notBlackListed)
						{
							//final String orderGuid = decryptKey(guid);
							orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
							if (null == orderModel)
							{
								cart = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
								////////
								validation = mplPaymentFacade.generateOTPforCOD(mplCustomerID, mobilenumber, mplCustomerName, cart);
							}
							else
							{
								////////
								validation = mplPaymentFacade.generateOTPforCOD(mplCustomerID, mobilenumber, mplCustomerName, orderModel);
							}
							if (StringUtils.isNotEmpty(validation))
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
	@RequestMapping(value =
	{ "/applyCoupons", "/{cartId}/applyCoupons" }, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ApplyCouponsDTO applyCoupons(@RequestParam final String couponCode,
			@RequestParam(required = false) final String cartGuid, @RequestParam(required = false) final String paymentMode)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException, NumberFormatException,
			JaloInvalidParameterException, VoucherOperationException, CalculationException, JaloSecurityException
	{
		ApplyCouponsDTO applycouponDto = new ApplyCouponsDTO();
		CartModel cartModel = null;
		OrderModel orderModel = null;
		try
		{
			final StringBuilder logBuilder = new StringBuilder();
			LOG.debug(logBuilder.append("Step 1:::The coupon code entered by the customer is :::").append(couponCode));

			//Fetching orderModel based on guid TPR-629
			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
			}
			//Redeem coupon for cartModel
			if (orderModel == null)
			{
				cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);
				//cartModel = mplPaymentWebFacade.findCartValues(cartGuid);
				if (cartModel == null)
				{
					LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + cartGuid);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
				}
				else
				{
					cartModel.setChannel(SalesApplication.MOBILE);
					getModelService().save(cartModel);
					final Double totalWithoutCoupon = cartModel.getTotalPrice();
					if (null != totalWithoutCoupon)
					{
						applycouponDto.setTotalWithoutCoupon(totalWithoutCoupon);
					}
					applycouponDto = mplCouponWebFacade.applyVoucher(couponCode, cartModel, null, paymentMode);

					applycouponDto
							.setTotal(String.valueOf(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv())
									.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

					try
					{
						final double totalAmount = cartModel.getTotalPrice().doubleValue();
						double payableWalletAmount = 0.0D;
						if (null != cartModel.getPayableWalletAmount() && cartModel.getPayableWalletAmount().doubleValue() > 0.0D)
						{
							payableWalletAmount = cartModel.getPayableWalletAmount().doubleValue();
						}
						if (totalAmount <= payableWalletAmount)
						{
							cartModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
							cartModel.setPayableWalletAmount(Double.valueOf(totalAmount));
							getModelService().save(cartModel);
							getModelService().refresh(cartModel);
							applycouponDto.setCliqCashApplied(true);
							applycouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(totalAmount));
							applycouponDto.setRemaingPaybleAmount(Double.valueOf(0.0D));
						}
						else
						{
							if (payableWalletAmount > 0.0D)
							{
								cartModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
								cartModel.setPayableWalletAmount(Double.valueOf(payableWalletAmount));
								applycouponDto.setCliqCashApplied(true);
								applycouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(payableWalletAmount));
								applycouponDto.setRemaingPaybleAmount(
										Double.valueOf(cartModel.getTotalPrice().doubleValue() - payableWalletAmount));
							}
							else
							{
								cartModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
								cartModel.setPayableWalletAmount(Double.valueOf(0.0D));
								applycouponDto.setCliqCashApplied(false);
								applycouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(0.0D));
								applycouponDto.setRemaingPaybleAmount(Double.valueOf(cartModel.getTotalPrice().doubleValue()));
							}
							getModelService().save(cartModel);
							getModelService().refresh(cartModel);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception occurred while setting splitPaymentMode" + e.getMessage());
					}

					applycouponDto.setCouponMessage(getMplCouponFacade().getCouponMessageInfo(cartModel));
				}
			}
			else
			{
				applycouponDto = mplCouponWebFacade.applyVoucher(couponCode, null, orderModel, paymentMode);
				applycouponDto.setTotal(String.valueOf(getMplCheckoutFacade()
						.createPrice(orderModel, orderModel.getTotalPriceWithConv()).getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				final Double totalWithoutCoupon = orderModel.getTotalPrice();
				if (null != totalWithoutCoupon)
				{
					applycouponDto.setTotalWithoutCoupon(totalWithoutCoupon);
				}
				try
				{
					final double totalAmount = orderModel.getTotalPrice().doubleValue();
					double payableWalletAmount = 0.0D;
					if (null != orderModel.getPayableWalletAmount() && orderModel.getPayableWalletAmount().doubleValue() > 0.0D)
					{
						payableWalletAmount = orderModel.getPayableWalletAmount().doubleValue();
					}
					if (totalAmount <= payableWalletAmount)
					{
						orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
						orderModel.setPayableWalletAmount(Double.valueOf(totalAmount));
						getModelService().save(orderModel);
						getModelService().refresh(orderModel);
						applycouponDto.setCliqCashApplied(true);
						applycouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(totalAmount));
						applycouponDto.setRemaingPaybleAmount(Double.valueOf(0.0D));
					}
					else
					{
						if (payableWalletAmount > 0.0D)
						{
							orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
							orderModel.setPayableWalletAmount(Double.valueOf(payableWalletAmount));
							applycouponDto.setCliqCashApplied(true);
							applycouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(payableWalletAmount));
							applycouponDto.setRemaingPaybleAmount(
									Double.valueOf(orderModel.getTotalPrice().doubleValue() - payableWalletAmount));
						}
						else
						{
							orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
							orderModel.setPayableWalletAmount(Double.valueOf(0.0D));
							applycouponDto.setCliqCashApplied(false);
							applycouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(0.0D));
							applycouponDto.setRemaingPaybleAmount(Double.valueOf(orderModel.getTotalPrice().doubleValue()));
						}
						getModelService().save(orderModel);
						getModelService().refresh(orderModel);
					}
				}
				catch (final Exception e)
				{
					LOG.error("Exception occurred while setting splitPaymentMode" + e.getMessage());
				}
				applycouponDto.setCouponMessage(getMplCouponFacade().getCouponMessageInfo(orderModel));
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
	@RequestMapping(value =
	{ "/releaseCoupons", "/{cartId}/releaseCoupons" }, method = RequestMethod.POST, produces = APPLICATION_TYPE)
	@ResponseBody
	public ReleaseCouponsDTO releaseCoupons(@RequestParam final String couponCode,
			@RequestParam(required = false) final String cartGuid, @RequestParam(required = false) final String paymentMode)
			throws RequestParameterException, WebserviceValidationException, MalformedURLException, NumberFormatException,
			JaloInvalidParameterException, VoucherOperationException, CalculationException, JaloSecurityException,
			JaloPriceFactoryException, CalculationException
	{
		ReleaseCouponsDTO releaseCouponDto = new ReleaseCouponsDTO();
		CartModel cartModel = null;
		OrderModel orderModel = null;
		double walletAmount = 0.0D;
		double discountAmount=0.0D;
		try
		{

			if (StringUtils.isNotEmpty(cartGuid))
			{
				orderModel = mplPaymentFacade.getOrderByGuid(cartGuid);
			}
			
			
			//Release coupon for cartModel
			if (null == orderModel)
			{
				cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartGuid);

				//cartModel = mplPaymentWebFacade.findCartValues(cartGuid);
				if (cartModel == null)
				{
					LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + cartGuid);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
				}
				else
				{
					//final Double totalWithoutCoupon = cartModel.getTotalPrice();
					if(null != cartModel.getTotalDiscounts()&& cartModel.getTotalDiscounts().doubleValue() > 0.0D) {
						discountAmount = cartModel.getTotalDiscounts().doubleValue();
					}
					final Double totalWithoutCoupon = Double.valueOf(cartModel.getTotalPrice().doubleValue()+Double.valueOf(discountAmount).doubleValue());
					if (null != totalWithoutCoupon)
					{
						releaseCouponDto.setTotalWithoutCoupon(totalWithoutCoupon);
					}
					
					cartModel.setChannel(SalesApplication.MOBILE);
					getModelService().save(cartModel);
					if(null != cartModel.getTotalWalletAmount() && cartModel.getTotalWalletAmount().doubleValue() > 0.0D) {
						walletAmount = cartModel.getTotalWalletAmount().doubleValue();
					}
					if(null != cartModel.getTotalDiscounts()&& cartModel.getTotalDiscounts().doubleValue() > 0.0D) {
						discountAmount = cartModel.getTotalDiscounts().doubleValue();
					}
					
					releaseCouponDto = mplCouponWebFacade.releaseVoucher(couponCode, cartModel, null, paymentMode);
					try
					{
						final double totalAmount = cartModel.getTotalPrice().doubleValue();
					//	double payableWalletAmount = 0.0D;
//						if (walletAmount > 0.0D)
//						{
//							payableWalletAmountIncludingDiscount = cartModel.getPayableWalletAmount().doubleValue()+discountAmount;
//						}
						if (totalAmount <= walletAmount)
						{
							cartModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
							cartModel.setPayableWalletAmount(Double.valueOf(totalAmount));
							getModelService().save(cartModel);
							getModelService().refresh(cartModel);
							releaseCouponDto.setCliqCashApplied(true);
							releaseCouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(totalAmount));
							releaseCouponDto.setRemaingPaybleAmount(Double.valueOf(0.0D));
						}
						else
						{
							if (walletAmount > 0.0D)
							{
								cartModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
								cartModel.setPayableWalletAmount(Double.valueOf(walletAmount));
								releaseCouponDto.setCliqCashApplied(true);
								releaseCouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(walletAmount));
								releaseCouponDto.setRemaingPaybleAmount(
										Double.valueOf(cartModel.getTotalPrice().doubleValue() - walletAmount));
							}
							else
							{
								cartModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
								cartModel.setPayableWalletAmount(Double.valueOf(0.0D));
								releaseCouponDto.setCliqCashApplied(false);
								releaseCouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(0.0D));
								releaseCouponDto.setRemaingPaybleAmount(Double.valueOf(cartModel.getTotalPrice().doubleValue()));
							}
							getModelService().save(cartModel);
							getModelService().refresh(cartModel);
						}
					}
					catch (final Exception e)
					{
						LOG.error("Exception occurred while setting splitPaymentMode" + e.getMessage());
					}
					releaseCouponDto
							.setTotal(String.valueOf(getMplCheckoutFacade().createPrice(cartModel, cartModel.getTotalPriceWithConv())
									.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			else
			{
				if(null != orderModel.getTotalWalletAmount() && orderModel.getTotalWalletAmount().doubleValue() > 0.0D) {
					walletAmount = orderModel.getTotalWalletAmount().doubleValue();
				}
				if(null != orderModel.getTotalDiscounts()&& orderModel.getTotalDiscounts().doubleValue() > 0.0D) {
					discountAmount = orderModel.getTotalDiscounts().doubleValue();
				}
				releaseCouponDto = mplCouponWebFacade.releaseVoucher(couponCode, null, orderModel, paymentMode);
				final Double totalWithoutCoupon = Double.valueOf(orderModel.getTotalPrice().doubleValue()+Double.valueOf(discountAmount).doubleValue());
				if (null != totalWithoutCoupon)
				{
					releaseCouponDto.setTotalWithoutCoupon(totalWithoutCoupon);
				}
				try
				{
					final double totalAmount = orderModel.getTotalPrice().doubleValue();
				//	double payableWalletAmount = 0.0D;
//					if (null != orderModel.getPayableWalletAmount() && orderModel.getPayableWalletAmount().doubleValue() > 0.0D)
//					{
//						payableWalletAmount = orderModel.getPayableWalletAmount().doubleValue();
//					}
					if (totalAmount <= walletAmount)
					{
						orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_CLIQ_CASH);
						orderModel.setPayableWalletAmount(Double.valueOf(totalAmount));
						getModelService().save(orderModel);
						getModelService().refresh(orderModel);
						releaseCouponDto.setCliqCashApplied(true);
						releaseCouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(totalAmount));
						releaseCouponDto.setRemaingPaybleAmount(Double.valueOf(0.0D));
					}
					else
					{
						if (walletAmount > 0.0D)
						{
							orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT_MODE_SPLIT);
							orderModel.setPayableWalletAmount(Double.valueOf(walletAmount));
							releaseCouponDto.setCliqCashApplied(true);
							releaseCouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(walletAmount));
							releaseCouponDto.setRemaingPaybleAmount(
									Double.valueOf(orderModel.getTotalPrice().doubleValue() - walletAmount));
						}
						else
						{
							orderModel.setSplitModeInfo(MarketplacewebservicesConstants.PAYMENT__MODE_JUSPAY);
							orderModel.setPayableWalletAmount(Double.valueOf(0.0D));
							releaseCouponDto.setCliqCashApplied(false);
							releaseCouponDto.setAmountTobePaidUsingCliqCash(Double.valueOf(0.0D));
							releaseCouponDto.setRemaingPaybleAmount(Double.valueOf(orderModel.getTotalPrice().doubleValue()));
						}
						getModelService().save(orderModel);
						getModelService().refresh(orderModel);
					}
				}
				catch (final Exception e)
				{
					LOG.error("Exception occurred while setting splitPaymentMode" + e.getMessage());
				}
				releaseCouponDto.setTotal(String.valueOf(getMplCheckoutFacade()
						.createPrice(orderModel, orderModel.getTotalPriceWithConv()).getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));

			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorCode())
			{
				releaseCouponDto.setErrorCode(e.getErrorCode());
			}
			if (null != e.getErrorMessage())
			{
				releaseCouponDto.setError(e.getErrorMessage());
			}
			releaseCouponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorCode())
			{
				releaseCouponDto.setErrorCode(e.getErrorCode());
			}
			if (null != e.getErrorMessage())
			{
				releaseCouponDto.setError(e.getErrorMessage());
			}
			releaseCouponDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
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


	/**
	 * Returns cart entries with POS details.
	 *
	 * @PathVariable cartId
	 * @PathVariable userId
	 * @param fields
	 * @return CartDataDetailsWsDTO
	 */
	@RequestMapping(value = "/{cartId}/cartDetailsCNC", method = RequestMethod.GET)
	@ResponseBody
	public CartDataDetailsWsDTO getCartDetailsWithPOS(@PathVariable final String cartId,
			@RequestParam(required = false) final String pincode,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final AddressListWsDTO addressListDTO = addressList(fields);
		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		try
		{
			if (null != cartId)
			{
				LOG.debug("************ get cart details with POS mobile web service *********" + cartId);
				cartDataDetails = mplCartWebService.getCartDetailsWithPOS(cartId, addressListDTO, pincode);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorCode() && e.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.B9038))
			{
				cartDataDetails.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			if (null != e.getErrorCode())
			{
				cartDataDetails.setErrorCode(e.getErrorCode());
			}
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
			if (null != e.getErrorCode())
			{
				cartDataDetails.setErrorCode(e.getErrorCode());
			}
			if (null != e.getErrorMessage())
			{
				cartDataDetails.setError(e.getErrorMessage());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		//TPR-799
		catch (final Exception e)
		{
			ExceptionUtil.getCustomizedExceptionTrace(e);
			cartDataDetails.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.E0000));
			cartDataDetails.setErrorCode(MarketplacecommerceservicesConstants.E0000);
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return cartDataDetails;
	}

	/**
	 * Set Point of service to c&c entries
	 *
	 * @PathVariable cartId
	 * @PathVariable userId
	 * @RequestParam USSID
	 * @RequestParam slaveId
	 * @param fields
	 * @return CartDataDetailsWsDTO
	 */
	@RequestMapping(value = "/{cartId}/addStore", method = RequestMethod.POST)
	@ResponseBody
	public CartDataDetailsWsDTO addStoreToCCEntry(@PathVariable final String cartId,
			@RequestParam(required = true) final String USSID, @RequestParam(required = true) final String slaveId,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("from addStoreToCCEntry method :" + cartId + "USSID :" + USSID + "slaveId :" + slaveId);
		}
		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		try
		{
			//call service to retrieve POSModel for given slaveId
			// changed name to slaveId in R2.3 TISRLUAT-803
			final PointOfServiceModel posModel = mplSlaveMasterFacade.checkPOSForSlave(slaveId);
			if (null != posModel)
			{
				final String response = mplStoreLocatorFacade.saveStoreForSelectedProduct(posModel, USSID);
				LOG.debug("from addStoreToCCEntry response :" + response);
				if ("yes".equalsIgnoreCase(response))
				{
					LOG.debug("************ in addStoreToCCEntry :get cart details mobile web service *********" + cartId);
					final AddressListWsDTO addressListDTO = addressList(fields);
					cartDataDetails = mplCartWebService.getCartDetailsWithPOS(cartId, addressListDTO, null);
					cartDataDetails.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
					return cartDataDetails;
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9520);
				}
			}
			else
			{
				LOG.debug("************ no store found with pos name *********" + slaveId);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9514);
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
			cartDataDetails.setErrorCode(e.getErrorCode());
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
			cartDataDetails.setErrorCode(e.getErrorCode());
		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				cartDataDetails.setError(e.getMessage());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return cartDataDetails;
	}


	/**
	 * Adds the pickup person details to cart
	 *
	 * @PathVariable cartId
	 * @PathVariable userId
	 * @RequestParam USSID
	 * @RequestParam slaveId
	 * @param fields
	 * @return CartDataDetailsWsDTO
	 */
	@RequestMapping(value = "/{cartId}/addPickupPerson", method = RequestMethod.POST)
	@ResponseBody
	public CartDataDetailsWsDTO addPickupPersonDetails(@PathVariable final String cartId, @RequestParam final String personName,
			@RequestParam final String personMobile,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("from addPickupPersonDetails method, to mobile");
		}
		try
		{
			if (null != cartId)
			{
				//final CartModel cartModel = mplPaymentWebFacade.findCartValues(cartId);
				//CAR Project performance issue fixed
				final CartModel cartModel = cartService.getSessionCart();

				if (null != cartModel)
				{
					if (null != personName)
					{
						cartModel.setPickupPersonName(personName);
					}
					if (null != personMobile)
					{
						cartModel.setPickupPersonMobile(personMobile);
					}
					//save the cart with pickup person details
					modelService.save(cartModel);

					LOG.debug("************ in addPickupPersonDetails :get cart details mobile web service *********" + cartId);
					final AddressListWsDTO addressListDTO = addressList(fields);
					cartDataDetails = mplCartWebService.getCartDetailsWithPOS(cartId, addressListDTO, null);
					cartDataDetails.setStatus(MarketplacecommerceservicesConstants.SUCCESSS_RESP);
				}
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
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				cartDataDetails.setError(e.getMessage());
			}
			cartDataDetails.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return cartDataDetails;
	}


	/**
	 * Removes the pickup person details if cart doesn't contains the CNC entries and removes delivery address if cart
	 * only contains CNC
	 *
	 * @param cartId
	 * @return void
	 */
	protected void cleanupPickUpDetails(final CartModel cartModel, final boolean removeExchangefromCNCcart)
	{
		//final CartModel cartModel = mplPaymentWebFacade.findCartValues(cartId);
		int cncDelModeCount = 0;
		int otherDelModeCount = 0;
		for (final AbstractOrderEntryModel orderEntryModel : cartModel.getEntries())
		{
			if (null != orderEntryModel.getGiveAway() && !orderEntryModel.getGiveAway().booleanValue())
			{
				if (null != orderEntryModel.getMplDeliveryMode()
						&& null != orderEntryModel.getMplDeliveryMode().getDeliveryMode()
						&& MarketplacecommerceservicesConstants.CLICK_COLLECT.equalsIgnoreCase((orderEntryModel.getMplDeliveryMode()
								.getDeliveryMode().getCode())))
				{
					++cncDelModeCount;
				}
				else
				{
					++otherDelModeCount;
				}
			}
		}
		//if entry does not have any click and collect,remove pickup details
		if (cncDelModeCount == 0)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cart Entries does not have any CNC mode");
			}
			cartModel.setPickupPersonMobile(null);
			cartModel.setPickupPersonName(null);
		}
		//if cart has only cnc and cart contains del address remove it
		if (cncDelModeCount > 0 && otherDelModeCount == 0 && null != cartModel.getDeliveryAddress())
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cart Entries have only CNC mode and del address is not empty");
			}

			cartModel.setDeliveryAddress(null);
		}
		//TPR-6971
		if (cncDelModeCount > 0 && otherDelModeCount == 0 && removeExchangefromCNCcart)
		{
			exchangeFacade.removeExchangefromCart(cartModel);
		}
		modelService.save(cartModel);
	}



	//R2.3 FL06 new API getEDD
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/getEDD", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public MplEDDInfoWsDTO getAvailableDeliverySlots(@PathVariable final String cartId) throws WebserviceValidationException
	{
		MplEDDInfoWsDTO mplEDDInfoWsDTO = new MplEDDInfoWsDTO();
		try
		{
			if (StringUtils.isEmpty(cartId))
			{
				mplEDDInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				return mplEDDInfoWsDTO;
			}
			CartModel cartModel = null;
			cartModel = mplPaymentWebFacade.findCartValues(cartId);
			final InvReserForDeliverySlotsRequestData deliverySlotsRequestData = new InvReserForDeliverySlotsRequestData();
			deliverySlotsRequestData.setCartId(cartModel.getGuid());
			final InvReserForDeliverySlotsResponseData deliverySlotsResponseData = mplCartFacade.convertDeliverySlotsDatatoWsdto(
					deliverySlotsRequestData, cartModel);

			if (CollectionUtils.isNotEmpty(deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData()))
			{
				try
				{
					mplEDDInfoWsDTO = mplCartFacade.getEDDInfo(cartModel,
							deliverySlotsResponseData.getInvReserForDeliverySlotsItemEDDInfoData());
				}
				catch (final ParseException parseException)
				{
					LOG.error("CartsController WEB Extension" + parseException.getMessage());
				}
			}
			else
			{
				mplEDDInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				mplEDDInfoWsDTO.setError(e.getErrorMessage());
			}
			mplEDDInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorCode() && e.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.B9038))
			{
				mplEDDInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			else
			{
				mplEDDInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			if (null != e.getErrorMessage())
			{
				mplEDDInfoWsDTO.setError(e.getErrorMessage());
			}
		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				mplEDDInfoWsDTO.setError(e.getMessage());
			}
			mplEDDInfoWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return mplEDDInfoWsDTO;
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{cartId}/selectedEDD", method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public WebSerResponseWsDTO setSelectedDeliverySlots(@PathVariable final String cartId,
			@RequestBody final MplSelectedEDDInfoWsDTO mplSelectedEDDInfoWsDTO) throws WebserviceValidationException
	{
		final WebSerResponseWsDTO webSerResponseWsDTO = new WebSerResponseWsDTO();
		if (CollectionUtils.isEmpty(mplSelectedEDDInfoWsDTO.getSelectedEDDInfo()) || StringUtils.isEmpty(cartId))
		{
			webSerResponseWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			return webSerResponseWsDTO;
		}

		try
		{
			CartModel cartModel = null;
			cartModel = mplPaymentWebFacade.findCartValues(cartId);
			final boolean isSaved = mplCartFacade.addSelectedEDD(cartModel, mplSelectedEDDInfoWsDTO.getSelectedEDDInfo());
			if (isSaved)
			{
				webSerResponseWsDTO.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				webSerResponseWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			if (null != e.getErrorMessage())
			{
				webSerResponseWsDTO.setError(e.getErrorMessage());
			}
			webSerResponseWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorCode() && e.getErrorCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.B9038))
			{
				webSerResponseWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			else
			{
				webSerResponseWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
			if (null != e.getErrorMessage())
			{
				webSerResponseWsDTO.setError(e.getErrorMessage());
			}
		}
		catch (final Exception e)
		{
			if (null != e.getMessage())
			{
				webSerResponseWsDTO.setError(e.getMessage());
			}
			webSerResponseWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}

		return webSerResponseWsDTO;
	}


	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @return the addToCartHelper
	 */
	public AddToCartHelper getAddToCartHelper()
	{
		return addToCartHelper;
	}


	/**
	 * @param addToCartHelper
	 *           the addToCartHelper to set
	 */
	public void setAddToCartHelper(final AddToCartHelper addToCartHelper)
	{
		this.addToCartHelper = addToCartHelper;
	}



}
