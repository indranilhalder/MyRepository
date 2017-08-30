/**
 *
 */
package com.tisl.mpl.service.impl;



import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.tisl.mpl.cart.impl.CommerceWebServicesCartFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.data.MplPromotionData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplStockService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCommerceCartServiceImpl;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.utility.MplDiscountUtil;
import com.tisl.mpl.wsdto.BillingAddressWsDTO;
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.CartOfferDetailsWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
import com.tisl.mpl.wsdto.MaxLimitData;
import com.tisl.mpl.wsdto.MaxLimitWsDto;
import com.tisl.mpl.wsdto.MobdeliveryModeWsDTO;
import com.tisl.mpl.wsdto.WebSerResponseWsDTO;





/**
 * @author TCS
 *
 */
public class MplCartWebServiceImpl extends DefaultCartFacade implements MplCartWebService
{
	//TODO change commerceWebServicesCartFacade2 to commerceWebServicesCartFacade after removing it in commercefacades
	@Resource(name = "commerceWebServicesCartFacade2")
	protected CartFacade cartFacade;
	@Resource
	private MplCartFacade mplCartFacade;
	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;
	@Resource
	private MplCommerceCartServiceImpl mplCommerceCartService;
	@Resource
	private WishlistFacade wishlistFacade;
	@Resource(name = "pointOfServiceConverter")
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;
	@Resource(name = "mplDataMapper")
	protected DataMapper mplDataMapper;
	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;

	@Resource
	private ExtendedUserService extendedUserService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "userFacade")
	protected UserFacade userFacade;
	@Resource(name = "cartService")
	protected CartService cartService;
	@Resource
	private ModelService modelService;
	@Resource
	private DiscountUtility discountUtility;
	@Resource
	private MplProductWebServiceImpl mplProductWebService;

	@Resource(name = "exchangeGuideService")
	private ExchangeGuideService exchangeService;
	@Resource
	private SiteConfigService siteConfigService;
	@Resource
	private Converter<CartModel, CartData> mplExtendedCartConverter;
	@Resource
	private MplDiscountUtil mplDiscountUtil;
	@Resource(name = "addressReversePopulator")
	private Populator<AddressData, AddressModel> addressReversePopulator;
	@Resource
	private UserService userService;
	@Autowired
	private CustomerAccountService customerAccountService;
	@Autowired
	private PriceDataFactory priceDataFactory;
	@Autowired
	private MplCouponFacade mplCouponFacade;



	private static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";

	private final static Logger LOG = Logger.getLogger(MplCartWebServiceImpl.class);

	//TPR-6117
	@Resource(name = "mplStockService")
	private MplStockService mplStockService;

	@Resource(name = "buyBoxFacade")
	private BuyBoxFacade buyBoxFacade;

	@Autowired
	private CommerceCartService commerceCartService;
	/* SONAR FIX JEWELLERY */
	//	@Autowired
	//	private MplPaymentWebService mplPaymentWebService;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService mplJewelleryService;

	/**
	 * Service to create cart
	 *
	 * @param oldCartId
	 * @param toMergeCartGuid
	 * @return WebSerResponseWsDTO
	 */
	@Override
	public WebSerResponseWsDTO createCart(final String oldCartId, String toMergeCartGuid)
	{
		final WebSerResponseWsDTO result = new WebSerResponseWsDTO();
		CartData cart = null;
		CartModificationData cartModificationData = null;
		CartRestorationData resultCartRestorationData = new CartRestorationData();
		try
		{
			if (StringUtils.isNotEmpty(oldCartId))
			{
				if (userFacade.isAnonymousUser())
				{
					LOG.debug(MarketplacecommerceservicesConstants.ANONYMOUS_USER_NOT_ALLOWED_MSG);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9042);
				}
				if (!isCartAnonymous(oldCartId))
				{
					LOG.debug(MarketplacecommerceservicesConstants.CART_NOT_ANONYMOUS_MSG
							+ MarketplacecommerceservicesConstants.SINGLE_SPACE + CartException.CANNOT_RESTORE);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9043);
				}
				if (StringUtils.isEmpty(toMergeCartGuid))
				{
					//toMergeCartGuid = getSessionCart().getGuid();
					toMergeCartGuid = getCurrentSessionCart().getGuid();
					LOG.debug("toMergeCartGuid " + toMergeCartGuid);
				}
				else
				{
					if (!isUserCart(toMergeCartGuid))
					{
						LOG.debug(MarketplacecommerceservicesConstants.CART_NOT_CURRENT_USERS_MSG
								+ MarketplacecommerceservicesConstants.SINGLE_SPACE + CartException.CANNOT_RESTORE);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9044);
					}
				}
				//cartFacade.restoreAnonymousCartAndMerge(oldCartId, toMergeCartGuid);

				cartModificationData = restoreAnonymousOldCartAndMerge(oldCartId, toMergeCartGuid);

				if (cartModificationData != null
						&& cartModificationData.getStatusCode() != MarketplacecommerceservicesConstants.ERROR_FLAG
						&& cartModificationData.getEntry() != null && cartModificationData.getQuantity() > 0)
				{
					//cart = mplCartFacade.getSessionCart();
					cart = cartFacade.getSessionCart();

					if (null != cart && StringUtils.isNotEmpty(cart.getCode()))
					{
						result.setCode(cart.getCode());
					}
					if (null != cart && StringUtils.isNotEmpty(cart.getGuid()))
					{
						result.setGuid(cart.getGuid());
					}
					result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
				}
				return result;
			}
			else
			{
				if (StringUtils.isNotEmpty(toMergeCartGuid))
				{
					if (!isUserCart(toMergeCartGuid))
					{
						LOG.debug(MarketplacecommerceservicesConstants.COULD_NOT_RESTORE_CARTS_MSG
								+ MarketplacecommerceservicesConstants.SINGLE_SPACE + CartException.CANNOT_RESTORE);

						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9046);
					}
					try
					{
						//cartFacade.restoreSavedCart(toMergeCartGuid);
						resultCartRestorationData = restoreCustomerSavedCart(toMergeCartGuid);
						if (resultCartRestorationData != null)
						{
							LOG.debug("resultCartRestorationData is not null");
							cart = cartFacade.getSessionCart();
							if (null != cart && StringUtils.isNotEmpty(cart.getCode()))
							{
								result.setCode(cart.getCode());
							}
							if (null != cart && StringUtils.isNotEmpty(cart.getGuid()))
							{
								result.setGuid(cart.getGuid());
							}
							result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						}
						return result;
					}
					catch (final EtailBusinessExceptions e)
					{
						LOG.error(MarketplacecommerceservicesConstants.CART_NOT_CURRENT_USERS_MSG
								+ MarketplacecommerceservicesConstants.SINGLE_SPACE + CartException.CANNOT_RESTORE);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9044);
					}
				}
				cart = cartFacade.getSessionCart();
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
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			throw e;
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);

			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9046);

		}
	}


	/**
	 * @param oldCartId
	 * @param toMergeCartGuid
	 * @return CartModificationData description : It merges the cart of a customer created in anonymous state to the cart
	 *         created after signing in.
	 */

	private CartModificationData restoreAnonymousOldCartAndMerge(final String oldCartId, final String toMergeCartGuid)
	{

		CartModificationData resultCartModificationData = new CartModificationData();
		final UserModel currentUser = extendedUserService.getCurrentUser();
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		final CommerceCartParameter newCartParameter = new CommerceCartParameter();

		try
		{
			if ((currentUser == null) || (extendedUserService.isAnonymousUser(currentUser)))
			{
				throw new AccessDeniedException("Only logged user can merge carts!");
			}
			LOG.debug("oldCartId " + oldCartId);
			LOG.debug("toMergeCartGuid " + toMergeCartGuid);

			final CartModel fromCart = mplCommerceCartService.getMplCommerceCartDao().getCartByGuid(oldCartId);
			if (fromCart == null)
			{
				LOG.debug("fromCart is null");
				resultCartModificationData.setStatusCode(MarketplacecommerceservicesConstants.ERROR_FLAG);
				resultCartModificationData.setStatusMessage(MarketplacecommerceservicesConstants.INVALID_CART_ID + oldCartId);
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + oldCartId);
				return resultCartModificationData;
			}
			if (!(currentBaseSite.equals(fromCart.getSite())))
			{
				throw new CommerceCartMergingException(String.format("Current site %s is not equal to cart %s site %s", new Object[]
				{ currentBaseSite, fromCart, fromCart.getSite() }));
			}
			final CartModel toCart = mplCommerceCartService.getMplCommerceCartDao().getCartByGuid(toMergeCartGuid);
			if (toCart == null)
			{
				LOG.debug("toCart is null");
				resultCartModificationData.setStatusCode(MarketplacecommerceservicesConstants.ERROR_FLAG);
				resultCartModificationData.setStatusMessage(MarketplacecommerceservicesConstants.INVALID_CART_ID + toMergeCartGuid);
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + toMergeCartGuid);
				throw new CommerceCartRestorationException("Cart cannot be null");
			}

			if (!(currentBaseSite.equals(toCart.getSite())))
			{
				throw new CommerceCartMergingException(String.format("Current site %s is not equal to cart %s site %s", new Object[]
				{ currentBaseSite, toCart, toCart.getSite() }));
			}
			ServicesUtil.validateParameterNotNull(fromCart, "fromCart can not be null");
			ServicesUtil.validateParameterNotNull(toCart, "toCart can not be null");

			if (fromCart.getGuid().equals(toCart.getGuid()))
			{
				throw new CommerceCartMergingException("Cannot merge cart to itself!");
			}

			for (final AbstractOrderEntryModel orderEntry : fromCart.getEntries())
			{
				if (orderEntry != null && orderEntry.getSelectedUSSID() != null && orderEntry.getSelectedUSSID() != null)
				{
					newCartParameter.setCreateNewEntry(false);
					newCartParameter.setEnableHooks(true);
					newCartParameter.setCart(toCart);
					newCartParameter.setProduct(orderEntry.getProduct());
					newCartParameter.setPointOfService(orderEntry.getDeliveryPointOfService());
					newCartParameter.setQuantity((orderEntry.getQuantity() == null) ? 0L : orderEntry.getQuantity().longValue());
					newCartParameter.setUnit(orderEntry.getUnit());
					newCartParameter.setUssid(orderEntry.getSelectedUSSID());
					cartService.setSessionCart(toCart);

				}
				else
				{
					LOG.debug("orderEntry is either null or SelectedUSSID for product id invalid");
					newCartParameter.setCreateNewEntry(true);
					break;
				}

				//final CommerceCartModification modification = mplCommerceCartService.addToCartWithUSSID(newCartParameter);
				final CommerceCartModification modification = mplCommerceCartService.addToCart(newCartParameter);
				resultCartModificationData = getCartModificationConverter().convert(modification);
				LOG.debug("resultCartModificationData is not null");
			}

		}
		catch (final InvalidCartException e)
		{
			resultCartModificationData.setStatusCode(MarketplacecommerceservicesConstants.ERROR_FLAG);
			resultCartModificationData.setStatusMessage(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return resultCartModificationData;
		}
		catch (final CommerceCartRestorationException e)
		{
			resultCartModificationData.setStatusCode(MarketplacecommerceservicesConstants.ERROR_FLAG);
			resultCartModificationData.setStatusMessage(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return resultCartModificationData;
		}

		catch (final AccessDeniedException e)
		{
			resultCartModificationData.setStatusCode(MarketplacecommerceservicesConstants.ERROR_FLAG);
			resultCartModificationData.setStatusMessage(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return resultCartModificationData;
		}

		catch (final CommerceCartMergingException e)
		{
			resultCartModificationData.setStatusCode(MarketplacecommerceservicesConstants.ERROR_FLAG);
			resultCartModificationData.setStatusMessage(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return resultCartModificationData;
		}
		catch (final CommerceCartModificationException e)
		{
			resultCartModificationData.setStatusCode(MarketplacecommerceservicesConstants.ERROR_FLAG);
			resultCartModificationData.setStatusMessage(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return resultCartModificationData;
		}

		return resultCartModificationData;
	}


	/**
	 * @param toMergeCartGuid
	 * @return CartModificationData description : Restore already existing cart of the customer
	 */
	private CartRestorationData restoreCustomerSavedCart(final String toMergeCartGuid)
	{
		CartRestorationData resultCartRestorationData = new CartRestorationData();
		final UserModel currentUser = extendedUserService.getCurrentUser();
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		final CommerceCartParameter parameter = new CommerceCartParameter();


		if (!hasEntries())
		{
			cartService.setSessionCart(null);
			LOG.debug("SessionCart has no entries or products hence set to null");
		}

		try
		{
			parameter.setEnableHooks(true);

			final CartModel cartForGuidAndSiteAndUser = mplCommerceCartService.getMplCommerceCartDao().getCartForGuidAndSiteAndUser(
					toMergeCartGuid, currentBaseSite, currentUser);

			if (null != cartForGuidAndSiteAndUser.getCode())
			{
				LOG.debug("cartForGuidAndSiteAndUser is not null");
			}

			parameter.setCart(cartForGuidAndSiteAndUser);
			resultCartRestorationData = getCartRestorationConverter().convert(getCommerceCartService().restoreCart(parameter));


			return resultCartRestorationData;


		}
		catch (final CommerceCartRestorationException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return resultCartRestorationData;
		}
		catch (final InvalidCartException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e);
			return resultCartRestorationData;
		}

	}


	/**
	 * @param cartGuid
	 * @return boolean
	 */
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
	 * @param toMergeCartGuid
	 * @return boolean
	 */
	private boolean isUserCart(final String toMergeCartGuid)
	{
		if (cartFacade instanceof CommerceWebServicesCartFacade)
		{
			final CommerceWebServicesCartFacade commerceWebServicesCartFacade = (CommerceWebServicesCartFacade) cartFacade;
			return commerceWebServicesCartFacade.isCurrentUserCart(toMergeCartGuid);
		}
		return true;
	}

	/**
	 * Service to add product to cart
	 *
	 * @param productCode
	 * @param cartId
	 * @param quantity
	 * @param USSID
	 * @return WebSerResponseWsDTO
	 * @throws CommerceCartModificationException
	 * @throws InvalidCartException
	 */
	@Override
	public WebSerResponseWsDTO addProductToCart(final String productCode, final String cartId, final String quantity,
			final String USSID, final boolean addedToCartWl, final String channel) throws InvalidCartException,
			CommerceCartModificationException
	{
		final WebSerResponseWsDTO result = new WebSerResponseWsDTO();
		final long quant = Long.parseLong(quantity);
		boolean addedToCart = false;
		int count = 0;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		final List<Wishlist2EntryModel> entryModelList = new ArrayList<Wishlist2EntryModel>();
		CartModel cartModel = null;
		boolean delisted = false;
		ProductModel productModel = null;
		ProductModel selectedProductModel = null;
		boolean maxQuantityAlreadyAdded = false;
		try
		{

			//changes for CarProject
			//cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartId);
			cartModel = cartService.getSessionCart();
			//changes for CarProject ends
			if (cartModel == null)
			{
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + cartId);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
			}
			else
			{
				for (final AbstractOrderEntryModel pr : cartModel.getEntries())
				{
					productModel = pr.getProduct();
					if (productCode.equals(productModel.getCode()) && USSID.equals(pr.getSelectedUSSID()))
					{
						final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);

						//TPR-6117  start

						//						if (productModel.getMaxOrderQuantity() == null || productModel.getMaxOrderQuantity().intValue() <= 0
						//								|| productModel.getMaxOrderQuantity().intValue() >= maximum_configured_quantiy
						//								|| pr.getQuantity().longValue() >= maximum_configured_quantiy)
						//						{
						//							//maxQuantityAlreadyAdded = mplCartFacade.isMaxQuantityAlreadyAdded(code, qty, stock, ussid);
						//							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9065);
						//						}
						if (productModel.getMaxOrderQuantity() == null && pr.getQuantity().longValue() >= maximum_configured_quantiy)
						{
							//maxQuantityAlreadyAdded = mplCartFacade.isMaxQuantityAlreadyAdded(code, qty, stock, ussid);
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9065);
						}
						else if (null != productModel.getMaxOrderQuantity())
						{
							if (productModel.getMaxOrderQuantity().intValue() <= 0
									&& productModel.getMaxOrderQuantity().intValue() >= maximum_configured_quantiy)
							{
								//maxQuantityAlreadyAdded = mplCartFacade.isMaxQuantityAlreadyAdded(code, qty, stock, ussid);
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9065);
							}
							else if (null != productModel.getCode())
							{
								int productStock = 0;
								final BuyBoxData buyboxdata = buyBoxFacade.buyboxPrice(productCode);


								if (buyboxdata.getAvailable() != null)
								{
									productStock = buyboxdata.getAvailable().intValue();

								}
								final long checkMaxLimList = mplCartFacade.checkMaxLimit(productModel.getCode(), cartModel);
								maxQuantityAlreadyAdded = isMaxProductQuantityAlreadyAdded(productModel.getMaxOrderQuantity(),
										productModel.getCode(), quant, productStock, USSID, checkMaxLimList);
							}
							if (maxQuantityAlreadyAdded)
							{
								throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9110);
							}
						}
						//TPR-6117   End
						//						if (pr.getQuantity().longValue() >= maximum_configured_quantiy)
						//						{
						//							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9065);
						//

						if (Long.parseLong(quantity) + pr.getQuantity().longValue() > maximum_configured_quantiy)
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9066);
						}
						selectedProductModel = productModel;
						//break;
					}
					//counting no of items in cart not freebie
					if (null != pr.getGiveAway() && !pr.getGiveAway().booleanValue())
					{
						count++;
					}
				}
			}
			result.setCount(String.valueOf(count));

			if (selectedProductModel == null)
			{
				//changes for CarProject
				//selectedProductModel = productService.getProductForCode(defaultPromotionManager.catalogData(), productCode);
				selectedProductModel = productService.getProductForCode(productCode);
				// changes for CarProject ends
				if (selectedProductModel == null)
				{
					LOG.debug(MarketplacecommerceservicesConstants.INVALID_PRODUCT_CODE + productCode);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9037);
				}
			}
			if (quant <= 0)
			{
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_PRODUCT_QUANTITY + quantity);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9068);
			}
			for (final SellerInformationModel seller : selectedProductModel.getSellerInformationRelator())
			{
				if (seller.getSellerArticleSKU().equalsIgnoreCase(USSID)
						&& (seller.getSellerAssociationStatus() != null && (seller.getSellerAssociationStatus().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.NO) || (seller.getEndDate() != null && new Date()
								.after(seller.getEndDate())))))
				{
					delisted = true;
					break;
				}
			}
			if (delisted)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("*********** Items delisted *************" + delistMessage);
				}
				delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
				result.setDelistedMessage(delistMessage);
			}
			else
			{
				addedToCart = mplCartFacade.addItemToCart(cartId, cartModel, selectedProductModel, quant, USSID);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("*********** Products added status in cart *************  ::::USSID::::" + USSID + ":::added???"
							+ addedToCart);
				}
				final List<Wishlist2EntryModel> allWishlistEntry = wishlistFacade.getAllWishlistByUssid(USSID);
				for (final Wishlist2EntryModel entryModel : allWishlistEntry)
				{
					entryModel.setAddToCartFromWl(Boolean.valueOf(addedToCartWl));
					if (LOG.isDebugEnabled())
					{


						LOG.debug("*********** Add to cart from WL mobile web service *************" + addedToCart + "::USSID::"
								+ USSID);
					}
					entryModelList.add(entryModel);
				}
				//For saving all the data at once rather in loop;
				modelService.saveAll(entryModelList);
				//TISLUX-1823 -For LuxuryWeb
				if (channel != null && channel.equalsIgnoreCase(SalesApplication.WEB.getCode()))
				{
					cartModel.setChannel(SalesApplication.WEB);
					modelService.save(cartModel);
				}
			}

			if (!addedToCart && !delisted)
			{
				LOG.debug(MarketplacecommerceservicesConstants.FAILURE_CARTADD);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9069);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("*********** Products added successfully  Mobile web service *************");
			}
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final InvalidCartException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9064);
		}

		catch (final CommerceCartModificationException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9070);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return result;
	}

	/**
	 * Service to get cart details
	 *
	 * @param cartId
	 * @param addressListWsDTO
	 * @return CartDataDetailsWsDTO
	 */
	@Override
	public CartDataDetailsWsDTO getCartDetails(final String cartId, final AddressListWsDTO addressListWsDTO, final String pincode,
			final String channel)
	{

		LOG.debug(String.format("Getcart details : Cart id : %s | Pincode: %s ", cartId, pincode));

		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		CartModel cart = null;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		try
		{
			//CAR changes
			cart = cartService.getSessionCart();
			if (LOG.isDebugEnabled())
			{
				LOG.debug("************ Cart mobile **************" + cartId);
			}
			if (cart != null)
			{
				// Changes for Duplicate Cart fix
				final boolean deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cart);
				LOG.debug("Cart Delisted Status " + deListedStatus);
				///newCartModel = mplCartFacade.removeDeliveryMode(cart); already used in productDetails
				cartDataDetails = cartDetails(cart, addressListWsDTO, pincode, cartId);
				if (deListedStatus)
				{
					delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
					cartDataDetails.setDelistedMessage(delistMessage);
				}
				if (cart.getPickupPersonName() != null)
				{
					cartDataDetails.setPickupPersonName(cart.getPickupPersonName());
				}
				if (cart.getPickupPersonMobile() != null)
				{
					cartDataDetails.setPickupPersonMobile(cart.getPickupPersonMobile());
				}
				//TISLUX-1823 -For LuxuryWeb
				if (channel != null && channel.equalsIgnoreCase(SalesApplication.WEB.getCode()))
				{
					cart.setChannel(SalesApplication.WEB);
					modelService.save(cart);
				}
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Cart model is empty");
				}
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9038);
			}
		}
		catch (final ConversionException ce)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.error("GetCartDetails :: ConversionException", ce);
			}
			throw new EtailNonBusinessExceptions(ce, MarketplacecommerceservicesConstants.B9004);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return cartDataDetails;
	}

	/**
	 * Service to get cart details with POS
	 *
	 * @param cartId
	 * @param addressListDTO
	 * @param pincode
	 * @return CartDataDetailsWsDTO
	 */
	@Override
	public CartDataDetailsWsDTO getCartDetailsWithPOS(final String cartId, final AddressListWsDTO addressListDTO,
			final String pincode)
	{

		CartModel cart = null;
		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		try
		{

			//CAR Project performance issue fixed
			cart = cartService.getSessionCart();

			if (cart != null)
			{
				final boolean deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cart);

				LOG.debug("Cart Delisted Status " + deListedStatus);

				cartDataDetails = cartDetailsWithPos(cart, addressListDTO, pincode, cartId);

				if (deListedStatus)
				{
					delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
					cartDataDetails.setDelistedMessage(delistMessage);
				}

				if (null != cart.getPickupPersonName())
				{
					cartDataDetails.setPickupPersonName(cart.getPickupPersonName());
				}
				if (null != cart.getPickupPersonMobile())
				{
					cartDataDetails.setPickupPersonMobile(cart.getPickupPersonMobile());
				}


			}
			else
			{
				LOG.debug("Cart model is empty");
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9038);
			}
		}
		catch (final ConversionException ce)
		{
			LOG.error("GetCartDetails :: ConversionException", ce);
			throw new EtailNonBusinessExceptions(ce, MarketplacecommerceservicesConstants.B9004);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return cartDataDetails;
	}

	/**
	 * Method to fetch cart product details -TPR-629
	 *
	 * @param abstractOrderModel
	 * @param deliveryModeDataMap
	 * @param isPinCodeCheckRequired
	 * @param resetReqd
	 * @return gwlpList
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<GetWishListProductWsDTO> productDetails(final AbstractOrderModel abstractOrderModel,
			final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap, final boolean isPinCodeCheckRequired,
			final boolean resetRequired, final List<PinCodeResponseData> pincodeList, final String pincode)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{

		String mediaFormat = null;
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("productDetails: |  cartId : %s | isPinCodeCheckRequired %s", abstractOrderModel.getCode(),
					Boolean.valueOf(isPinCodeCheckRequired)));
		}

		final List<GetWishListProductWsDTO> gwlpList = new ArrayList<>();
		ProductData productData = null;
		final List<MarketplaceDeliveryModeData> deliveryModeList = new ArrayList<>();
		List<PromotionResultModel> promotionResult = null;
		try
		{


			if (CollectionUtils.isNotEmpty(abstractOrderModel.getAllPromotionResults()))
			{
				promotionResult = new ArrayList(abstractOrderModel.getAllPromotionResults());
			}

			//Removed checkedPincode
			//	if (null != finalCart.getEntries() && !finalCart.getEntries().isEmpty())
			/* TISPT- 96 { */
			for (final AbstractOrderEntryModel abstractOrderEntry : abstractOrderModel.getEntries())
			{

				//if (null != abstractOrderEntry && null != abstractOrderEntry.getProduct())
				/*
				 * review comments incorporated TISPT- 96
				 */
				//{
				productData = productFacade.getProductForOptions(abstractOrderEntry.getProduct(),
						Arrays.asList(ProductOption.BASIC, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES));

				/*
				 * productData = productFacade.getProductForOptions(abstractOrderEntry.getProduct(),
				 * Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
				 * ProductOption.CATEGORIES ProductOption.PROMOTIONS, ProductOption.STOCK,
				 * ProductOption.DELIVERY_MODE_AVAILABILITY ));
				 */
				//TISJEW-3517
				boolean isExchangeApplicable = false;
				final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
				final GetWishListProductWsDTO gwlp = new GetWishListProductWsDTO();
				//TPR-1083
				if (StringUtils.isNotEmpty(abstractOrderEntry.getExchangeId()))
				{
					gwlp.setExchangeId(abstractOrderEntry.getExchangeId());
					if (StringUtils.isNotEmpty(pincode) && exchangeService.isBackwardServiceble(pincode))
					{
						gwlp.setExchangeMessage(MarketplacewebservicesConstants.EXCHANGEAPPLIED);
						//TISJEW-3517
						isExchangeApplicable = true;
					}
					else
					{
						gwlp.setExchangeMessage(MarketplacewebservicesConstants.EXCHANGENOTAPPLIED
								+ MarketplacecommerceservicesConstants.SINGLE_SPACE + pincode);
					}
				}
				if (null != abstractOrderEntry.getDeliveryPointOfService())
				{
					PointOfServiceData pointOfServiceData = null;
					pointOfServiceData = pointOfServiceConverter.convert(abstractOrderEntry.getDeliveryPointOfService());
					gwlp.setStoreDetails(mplDataMapper.map(pointOfServiceData, PointOfServiceWsDTO.class, "DEFAULT"));
				}
				if (null != abstractOrderEntry.getEntryNumber())
				{
					gwlp.setEntryNumber(abstractOrderEntry.getEntryNumber().toString());
				}
				if (StringUtils.isNotEmpty(abstractOrderEntry.getProduct().getCode()))
				{
					gwlp.setProductcode(abstractOrderEntry.getProduct().getCode());
				}
				if (StringUtils.isNotEmpty(abstractOrderEntry.getProduct().getName()))
				{
					gwlp.setProductName(abstractOrderEntry.getProduct().getName());
				}
				if (null != productData.getBrand() && StringUtils.isNotEmpty(productData.getBrand().getBrandname()))
				{
					gwlp.setProductBrand(productData.getBrand().getBrandname());
				}

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
					//final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
					gwlp.setMaxQuantityAllowed(String.valueOf(maximum_configured_quantiy));
				}

				//TPR-6117 Ends

				//Luxury LW-174
				if (null != productData.getLuxIndicator()
						&& (MarketplaceCoreConstants.LUXURY).equalsIgnoreCase(productData.getLuxIndicator()))
				{
					gwlp.setIsLuxury(productData.getLuxIndicator());
					mediaFormat = MarketplacecommerceservicesConstants.LUXURY_CARTICON;
				}
				else if ((null == productData.getLuxIndicator())
						|| (null != productData.getLuxIndicator() && (MarketplaceCoreConstants.Marketplace)
								.equalsIgnoreCase(productData.getLuxIndicator())))
				{
					gwlp.setIsLuxury(MarketplaceCoreConstants.Marketplace);
					mediaFormat = MarketplacecommerceservicesConstants.THUMBNAIL;
				}

				//Luxury LW-174 Ends

				if (null != abstractOrderEntry.getGiveAway() && abstractOrderEntry.getGiveAway().booleanValue())
				{
					gwlp.setIsGiveAway(MarketplacecommerceservicesConstants.Y);
				}
				else
				{
					gwlp.setIsGiveAway(MarketplacecommerceservicesConstants.N);
				}
				if (CollectionUtils.isNotEmpty(abstractOrderEntry.getAssociatedItems()))
				{
					gwlp.setAssociatedBaseProducts(abstractOrderEntry.getAssociatedItems());
				}
				if (StringUtils.isNotEmpty(productData.getRootCategory()))
				{
					gwlp.setRootCategory(productData.getRootCategory());
				}
				else
				{
					LOG.debug("*************** Mobile webservice root category is empty ********************");
				}

				if ((MarketplacecommerceservicesConstants.FINEJEWELLERY).equalsIgnoreCase(productData.getRootCategory()))
				{
					gwlp.setPriceDisclaimerTextJwlry(MarketplacewebservicesConstants.PRICE_DISCLAIMER_JEWELLERY);
				}

				final String catId = mplProductWebService.getCategoryCodeOfProduct(productData);
				if (null != catId && !catId.isEmpty())
				{
					gwlp.setProductCategoryId(catId);
				}
				if (CollectionUtils.isNotEmpty(productData.getImages()))
				{
					//Set product image(thumbnail) url
					for (final ImageData img : productData.getImages())
					{
						if (null != img && null != img.getUrl() && StringUtils.isNotEmpty(img.getFormat())
						//&& img.getFormat().toLowerCase().equals(MarketplacecommerceservicesConstants.THUMBNAIL) Sonar fix
								&& img.getFormat().equalsIgnoreCase(mediaFormat))
						{
							gwlp.setImageURL(img.getUrl());
						}

					}
				}
				else
				{
					LOG.debug("*************** Mobile webservice images are empty ********************");
				}

				if (StringUtils.isNotEmpty(productData.getColour()))
				{
					gwlp.setColor(productData.getColour());
				}
				else
				{
					LOG.debug("*************** Mobile webservice color is empty ********************");
				}
				if (StringUtils.isNotEmpty(productData.getSize()))
				{
					gwlp.setSize(productData.getSize());
				}
				else
				{
					LOG.debug("*************** Mobile webservice size is empty ********************");
				}
				/* capacity */
				if (abstractOrderEntry.getProduct() instanceof PcmProductVariantModel)
				{
					final PcmProductVariantModel selectedVariantModel = (PcmProductVariantModel) abstractOrderEntry.getProduct();
					final String selectedCapacity = selectedVariantModel.getCapacity();
					final ProductModel baseProduct = selectedVariantModel.getBaseProduct();
					if (null != baseProduct.getVariants() && null != selectedCapacity)
					{
						for (final VariantProductModel vm : baseProduct.getVariants())
						{
							final PcmProductVariantModel pm = (PcmProductVariantModel) vm;
							if (!selectedCapacity.isEmpty() && null != pm.getCapacity() && selectedCapacity.equals(pm.getCapacity()))
							{

								gwlp.setCapacity(pm.getCapacity());
							}
							else
							{
								LOG.debug("*************** Mobile webservice product capacity empty********************");
							}
						}
					}
				}

				/* capacity */
				if (StringUtils.isNotEmpty(abstractOrderEntry.getSelectedUSSID()))
				{
					gwlp.setUSSID(abstractOrderEntry.getSelectedUSSID());

				}

				if (null != abstractOrderEntry.getQuantity() && StringUtils.isNotEmpty((abstractOrderEntry.getQuantity().toString())))
				{
					gwlp.setQtySelectedByUser(abstractOrderEntry.getQuantity().toString());

				}

				final Predicate<SellerInformationData> pred1 = o -> o != null;
				//final Predicate<SellerInformationData> pred2 = o -> o.getUssid().equals(abstractOrderEntry.getSelectedUSSID());

				//for jewellery fetching psmussid
				Predicate<SellerInformationData> pred2 = o -> o != null;
				if (StringUtils.equalsIgnoreCase(abstractOrderEntry.getProduct().getProductCategoryType(),
						MarketplacewebservicesConstants.FINEJEWELLERY))

				{
					final String ussid = mplJewelleryService.getJewelleryInfoByUssid(abstractOrderEntry.getSelectedUSSID()).get(0)
							.getPCMUSSID();
					LOG.debug("pcm ussid : " + ussid);
					pred2 = o -> o.getUssid().equals(ussid);
				}
				else
				{
					pred2 = o -> o.getUssid().equals(abstractOrderEntry.getSelectedUSSID());
				}
				//Ends for jewellery

				if (null != productData.getSeller() && productData.getSeller().size() > 0)
				{
					productData.getSeller().stream().filter(pred1.and(pred2)).findFirst().ifPresent(c -> {
						gwlp.setSellerId(c.getSellerID());
						gwlp.setSellerName(c.getSellername());
						gwlp.setFullfillmentType(c.getFullfillment());
					});
				}
				if (null != productData.getSeller() && productData.getSeller().size() > 0)
				{
					productData.getSeller().stream().filter(pred1.and(pred2)).findFirst().ifPresent(c -> {
						gwlp.setSellerId(c.getSellerID());
						gwlp.setSellerName(c.getSellername());
						gwlp.setFullfillmentType(c.getFullfillment());
					});
				}
				if (null != abstractOrderEntry.getFulfillmentMode())
				{
					gwlp.setFullfillmentType(abstractOrderEntry.getFulfillmentMode());
				}
				///Delivery mode ///
				final List<MobdeliveryModeWsDTO> deliveryList = new ArrayList<MobdeliveryModeWsDTO>();
				MobdeliveryModeWsDTO delivery = null;
				if (isPinCodeCheckRequired)
				{
					try
					{

						if (null != deliveryModeDataMap && !deliveryModeDataMap.isEmpty())
						{
							for (final Map.Entry<String, List<MarketplaceDeliveryModeData>> entry : deliveryModeDataMap.entrySet())
							{
								if (entry.getValue() != null && null != entry.getKey() && null != abstractOrderEntry.getEntryNumber()
										&& entry.getKey().equalsIgnoreCase(abstractOrderEntry.getEntryNumber().toString()))
								{
									for (final MarketplaceDeliveryModeData modeData : entry.getValue())
									{

										if (modeData.getSellerArticleSKU().equalsIgnoreCase(abstractOrderEntry.getSelectedUSSID()))
										{
											//TISUT-1795 TISST-13632
											deliveryModeList.add(modeData);
										}
									}

									break;
								}
							}
						}
						//	if (deliveryModeList.size() >= 1)
						//	{ TISPT-96
						for (final MarketplaceDeliveryModeData deliveryMode : deliveryModeList)
						{
							if (null != abstractOrderEntry.getSelectedUSSID() && !abstractOrderEntry.getSelectedUSSID().isEmpty()
									&& null != deliveryMode.getSellerArticleSKU() && !deliveryMode.getSellerArticleSKU().isEmpty()
									&& abstractOrderEntry.getSelectedUSSID().equalsIgnoreCase(deliveryMode.getSellerArticleSKU()))
							{
								delivery = new MobdeliveryModeWsDTO();
								if (StringUtils.isNotEmpty(deliveryMode.getCode()))
								{
									delivery.setCode(deliveryMode.getCode());
								}
								if (StringUtils.isNotEmpty(deliveryMode.getName()))
								{
									delivery.setName(deliveryMode.getName());
								}


								//TPR-4421
								if (null != deliveryMode.getDeliveryCost() && null != deliveryMode.getDeliveryCost().getValue())
								{




									delivery.setDeliveryCost(String.valueOf(deliveryMode.getDeliveryCost().getValue()
											.setScale(2, BigDecimal.ROUND_HALF_UP)));
								}
								else
								{
									//	for defect TISEE-5534
									if (null != deliveryMode.getDeliveryCost() && null != deliveryMode.getDeliveryCost().getValue())
									{


										delivery.setDeliveryCost(String.valueOf(deliveryMode.getDeliveryCost().getValue()
												.setScale(2, BigDecimal.ROUND_HALF_UP)));
									}
								}
								if (StringUtils.isNotEmpty(deliveryMode.getDescription()))
								{
									delivery.setDesc(deliveryMode.getDescription());
								}
								deliveryList.add(delivery);
							}
						}
						//}// TISPT-96 comments
					}
					catch (final Exception e)
					{
						LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK, e);
					}
				}
				else
				{
					Collection<MplZoneDeliveryModeValueModel> deliverymodeList = null;
					try
					{
						if (null != abstractOrderEntry.getMplZoneDeliveryModeValue())
						{
							deliverymodeList = abstractOrderEntry.getMplZoneDeliveryModeValue();
							for (final MplZoneDeliveryModeValueModel deliveryMode : deliverymodeList)
							{
								delivery = new MobdeliveryModeWsDTO();
								if (null != deliveryMode.getDeliveryMode()
										&& StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getCode()))
								{
									delivery.setCode(deliveryMode.getDeliveryMode().getCode());

									//TISEE-950
									String startValue = null;
									String endValue = null;
									if (null != deliveryMode.getDeliveryMode())
									{
										startValue = deliveryMode.getDeliveryMode().getStart() != null ? deliveryMode.getDeliveryMode()


										.getStart().toString() : MarketplacecommerceservicesConstants.DEFAULT_START_TIME;

										endValue = deliveryMode.getDeliveryMode().getEnd() != null ? deliveryMode.getDeliveryMode()



										.getEnd().toString() : MarketplacecommerceservicesConstants.DEFAULT_END_TIME;

									}
									if (StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getCode())
											&& StringUtils.isNotEmpty(startValue) && StringUtils.isNotEmpty(endValue)
											&& StringUtils.isNotEmpty(deliveryMode.getSellerArticleSKU())
											&& StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getCode()))
									{


										delivery.setDesc(getMplCommerceCartService().getDeliveryModeDescription(
												deliveryMode.getSellerArticleSKU(), deliveryMode.getDeliveryMode().getCode(), startValue,
												endValue));
									}
									if (null != deliveryMode.getDeliveryMode()
											&& StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getName()))
									{
										delivery.setName(deliveryMode.getDeliveryMode().getName());
									}


									//TPR-4421
									if (null != gwlp.getFullfillmentType() && !gwlp.getFullfillmentType().isEmpty()
											&& gwlp.getFullfillmentType().equalsIgnoreCase("tship"))
									{
										delivery.setDeliveryCost("0.0");
									}
									else



									{
										if (LOG.isDebugEnabled())
										{
											LOG.debug("************ Mobile webservice Sship product ************* Delivery cost "
													+ deliveryMode.getValue().toString() + "for" + gwlp.getFullfillmentType());
										}
										if (null != abstractOrderEntry.getGiveAway() && !abstractOrderEntry.getGiveAway().booleanValue()
												&& null != deliveryMode.getValue() && null != abstractOrderEntry.getQuantity())
										{
											delivery.setDeliveryCost(deliveryMode.getValue().toString());

										}
									}

								}

								deliveryList.add(delivery);
							}
						}
						else
						{
							final String ussid = abstractOrderEntry.getSelectedUSSID();
							if (ussid != null && !ussid.isEmpty())
							{
								abstractOrderEntry.setSelectedUSSID(ussid);
								final List<MplZoneDeliveryModeValueModel> MplZoneDeliveryModeValueModel = getMplDeliveryCostService()
										.getDeliveryModesAndCost(MarketplacecommerceservicesConstants.INR, ussid);
								abstractOrderEntry.setMplZoneDeliveryModeValue(MplZoneDeliveryModeValueModel);
								modelService.save(abstractOrderEntry);
							}

							deliverymodeList = abstractOrderEntry.getMplZoneDeliveryModeValue();
							for (final MplZoneDeliveryModeValueModel deliveryMode : deliverymodeList)
							{
								delivery = new MobdeliveryModeWsDTO();
								if (null != deliveryMode.getDeliveryMode()
										&& StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getCode()))
								{
									delivery.setCode(deliveryMode.getDeliveryMode().getCode());

									//TISEE-950
									String startValue = null;
									String endValue = null;
									if (null != deliveryMode.getDeliveryMode())
									{
										startValue = deliveryMode.getDeliveryMode().getStart() != null ? deliveryMode.getDeliveryMode()


										.getStart().toString() : MarketplacecommerceservicesConstants.DEFAULT_START_TIME;

										endValue = deliveryMode.getDeliveryMode().getEnd() != null ? deliveryMode.getDeliveryMode()



										.getEnd().toString() : MarketplacecommerceservicesConstants.DEFAULT_END_TIME;

									}
									if (StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getCode())
											&& StringUtils.isNotEmpty(startValue) && StringUtils.isNotEmpty(endValue)
											&& StringUtils.isNotEmpty(deliveryMode.getSellerArticleSKU())
											&& StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getCode()))
									{


										delivery.setDesc(getMplCommerceCartService().getDeliveryModeDescription(
												deliveryMode.getSellerArticleSKU(), deliveryMode.getDeliveryMode().getCode(), startValue,
												endValue));
									}
									if (null != deliveryMode.getDeliveryMode()
											&& StringUtils.isNotEmpty(deliveryMode.getDeliveryMode().getName()))
									{
										delivery.setName(deliveryMode.getDeliveryMode().getName());
									}

									//TPR-4421
									if (null != gwlp.getFullfillmentType() && !gwlp.getFullfillmentType().isEmpty()
											&& gwlp.getFullfillmentType().equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP))
									{
										if (LOG.isDebugEnabled())
										{
											LOG.debug("************ Mobile webservice Tship product ************* Delivery cost 0"
													+ gwlp.getFullfillmentType());
										}
										delivery.setDeliveryCost(MarketplacecommerceservicesConstants.ZeroDeliveryCost);
									}
									else



									{
										if (LOG.isDebugEnabled())
										{
											LOG.debug("************ Mobile webservice Sship product ************* Delivery cost "
													+ deliveryMode.getValue().toString() + "for" + gwlp.getFullfillmentType());
										}
										if (null != abstractOrderEntry.getGiveAway() && !abstractOrderEntry.getGiveAway().booleanValue()
												&& null != deliveryMode.getValue() && null != abstractOrderEntry.getQuantity())
										{
											delivery.setDeliveryCost(deliveryMode.getValue().toString());

										}
									}

								}
								deliveryList.add(delivery);

							}
						}


					}
					catch (final Exception e)
					{
						LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR, e);
						//throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9041);
					}
				}
				if (!deliveryList.isEmpty())
				{
					gwlp.setElligibleDeliveryMode(deliveryList);
				}
				if (!resetRequired)
				{
					MobdeliveryModeWsDTO selectedDelivery = null;
					final MplZoneDeliveryModeValueModel val = abstractOrderEntry.getMplDeliveryMode();
					if (null != val && null != val.getDeliveryMode())
					{
						selectedDelivery = new MobdeliveryModeWsDTO();
						if (null != val.getDeliveryMode() && StringUtils.isNotEmpty(val.getDeliveryMode().getCode()))
						{
							selectedDelivery.setCode(val.getDeliveryMode().getCode());
						}
						if (null != val.getDeliveryMode() && StringUtils.isNotEmpty(val.getDeliveryMode().getName()))
						{
							selectedDelivery.setName(val.getDeliveryMode().getName());
						}

						//TISEE-950
						String startValue = null;
						String endValue = null;
						if (null != abstractOrderEntry.getMplDeliveryMode()
								&& null != abstractOrderEntry.getMplDeliveryMode().getDeliveryMode())
						{
							startValue = abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getStart() != null ? abstractOrderEntry
									.getMplDeliveryMode().getDeliveryMode().getStart().toString()
									: MarketplacecommerceservicesConstants.DEFAULT_START_TIME;

							endValue = abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getEnd() != null ? abstractOrderEntry
									.getMplDeliveryMode().getDeliveryMode().getEnd().toString()
									: MarketplacecommerceservicesConstants.DEFAULT_END_TIME;

							if (StringUtils.isNotEmpty(abstractOrderEntry.getMplDeliveryMode().getSellerArticleSKU())
									&& StringUtils.isNotEmpty(startValue) && StringUtils.isNotEmpty(endValue)
									&& StringUtils.isNotEmpty(abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getCode()))
							{
								selectedDelivery.setDesc(getMplCommerceCartService().getDeliveryModeDescription(
										abstractOrderEntry.getMplDeliveryMode().getSellerArticleSKU(),
										abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getCode(), startValue, endValue));
							}

						}

						//TPR-4421
						if (null != gwlp.getFullfillmentType() && !gwlp.getFullfillmentType().isEmpty()
								&& gwlp.getFullfillmentType().equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP)
								&& delivery != null)
						{
							delivery.setDeliveryCost(MarketplacecommerceservicesConstants.ZeroDeliveryCost);
						}
						else


						{

							if (null != abstractOrderEntry.getCurrDelCharge())

							{

								selectedDelivery.setDeliveryCost(String.valueOf(abstractOrderEntry.getCurrDelCharge()));
								if (LOG.isDebugEnabled())
								{
									LOG.debug("************ Mobile webservice Sship product ************* Delivery cost "
											+ abstractOrderEntry.getCurrDelCharge() + "for" + gwlp.getFullfillmentType());
								}
							}

						}
					}
					if (null != selectedDelivery)
					{
						gwlp.setSelectedDeliveryMode(selectedDelivery);
					}
				}

				//Set the price
				double entryPrice = 0;
				if (null != abstractOrderEntry.getTotalMrp() && null != abstractOrderEntry.getQuantity())
				{
					//entryPrice = abstractOrderEntry.getBasePrice().doubleValue() * abstractOrderEntry.getQuantity().doubleValue();
					//setting mrp here--tpr-3823
					entryPrice = abstractOrderEntry.getTotalMrp().doubleValue();

				}
				final Double price = new Double(entryPrice);
				gwlp.setPrice(price);

				if (null != abstractOrderEntry.getTotalPrice()
						&& Double.compare(entryPrice, abstractOrderEntry.getTotalPrice().doubleValue()) != 0)
				{
					gwlp.setOfferPrice(abstractOrderEntry.getTotalPrice().toString());

				}

				if (null != abstractOrderEntry.getNetAmountAfterAllDisc()
						&& abstractOrderEntry.getNetAmountAfterAllDisc().doubleValue() > 0.0D)
				{
					final PriceData cartLevelDisc = discountUtility.createPrice(abstractOrderModel,
							Double.valueOf(abstractOrderEntry.getNetAmountAfterAllDisc().doubleValue()));
					if (null != cartLevelDisc && null != cartLevelDisc.getValue())
					{
						gwlp.setCartLevelDiscount(String.valueOf(cartLevelDisc.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
					}
				}

				final DecimalFormat df2 = new DecimalFormat(".##");
				//product level Disc
				if (null != abstractOrderEntry.getTotalPrice() && null != abstractOrderEntry.getTotalProductLevelDisc()
						&& abstractOrderEntry.getTotalProductLevelDisc().doubleValue() > 0.0D)
				{
					gwlp.setProductLevelDisc(new Double(df2.format(abstractOrderEntry.getTotalPrice())));//ProductLevel Price after Discount

					//product level Disc Value
					//gwlp.setProductLevelDiscount(new Double(df2.format(abstractOrderEntry.getTotalProductLevelDisc())));
					//Re-using this field for product discount number rather than difference  of two prices
				}


				//setting separately here productPerDiscDisplay for  all cases
				if (null != abstractOrderEntry.getProductPerDiscDisplay())
				{
					//setting productPerDiscDisplay here--tpr-3823
					gwlp.setProductLevelDiscount(new Double(df2.format(abstractOrderEntry.getProductPerDiscDisplay())));
				}

				MplPromotionData appliedResponseData = new MplPromotionData();
				// promotion description
				if (null != promotionResult && !promotionResult.isEmpty())
				{
					for (final PromotionResultModel promo : promotionResult)
					{
						if (null != promo)
						{
							if (promo.getPromotion() instanceof ProductPromotionModel)
							{

								final ProductPromotionModel productPromotion = (ProductPromotionModel) promo.getPromotion();
								//								Collection<ProductModel> promoProducts = null;
								//								if (null != productPromotion.getProducts() && !productPromotion.getProducts().isEmpty())
								//								{
								//									promoProducts = productPromotion.getProducts();
								//								}
								//								if (null != promoProducts && !promoProducts.isEmpty())
								//								{
								final String promoCode = abstractOrderEntry.getProductPromoCode();
								if (StringUtils.isNotEmpty(promoCode)
										&& StringUtils.equalsIgnoreCase(promoCode, promo.getPromotion().getCode()))
								{
									//									for (final ProductModel products : promoProducts)
									//									{
									//										if (null != products.getCode() && null != abstractOrderEntry.getProduct()
									//												&& null != abstractOrderEntry.getProduct().getCode()
									//												&& products.getCode().equals(abstractOrderEntry.getProduct().getCode()))
									//										{

									gwlp.setProductLevelDiscDesc(productPromotion.getDescription());
									//////////////////////////

									if (null != promo.getCertainty() && promo.getCertainty().floatValue() == 1.0F)
									{
										if (abstractOrderModel instanceof CartModel)
										{
											appliedResponseData = mplDiscountUtil.populateData(productPromotion,
													(CartModel) abstractOrderModel);
										}

										if (null != appliedResponseData && null != appliedResponseData.getDiscountPrice())
										{
											gwlp.setDiscountPrice(appliedResponseData.getDiscountPrice());

										}
										if (null != appliedResponseData && null != appliedResponseData.getIsPercentage())
										{
											gwlp.setIsPercentage(appliedResponseData.getIsPercentage());
										}
										if (null != appliedResponseData && null != appliedResponseData.getPercentagePromotion())
										{
											gwlp.setPercentagePromotion(appliedResponseData.getPercentagePromotion());
										}
									}
									///////////////////////////////
									//}
									//	}
								}
							}
							if (promo.getPromotion() instanceof OrderPromotionModel)
							{
								final OrderPromotionModel orderPromotionModel = (OrderPromotionModel) promo.getPromotion();
								if (null != orderPromotionModel && null != orderPromotionModel.getCode()
										&& null != abstractOrderEntry.getCartPromoCode()
										&& orderPromotionModel.getCode().equals(abstractOrderEntry.getCartPromoCode())
										&& null != orderPromotionModel.getDescription())
								{
									gwlp.setCartLevelDiscDesc(orderPromotionModel.getDescription());
									/////////////////////////
									if (null != promo.getCertainty() && promo.getCertainty().floatValue() == 1.0F)
									{
										if (abstractOrderModel instanceof CartModel)
										{
											appliedResponseData = mplDiscountUtil.populateCartPromoData(orderPromotionModel,
													(CartModel) abstractOrderModel);
										}
										if (null != appliedResponseData && null != appliedResponseData.getDiscountPrice())
										{
											gwlp.setCartDiscountPrice(appliedResponseData.getDiscountPrice());
										}
										if (null != appliedResponseData && null != appliedResponseData.getIsPercentage())
										{
											gwlp.setCartIsPercentage(appliedResponseData.getIsPercentage());
										}
										if (null != appliedResponseData && null != appliedResponseData.getPercentagePromotion())
										{
											gwlp.setCartPercentagePromotion(appliedResponseData.getPercentagePromotion());
										}
									}
									////////////////////////

								}
							}
						}

					}
				}

				if (null != abstractOrderEntry.getGiveAway() && abstractOrderEntry.getGiveAway().booleanValue()
						&& null != abstractOrderEntry.getBasePrice() && abstractOrderEntry.getBasePrice().doubleValue() > 0)
				{
					gwlp.setPrice(new Double(abstractOrderEntry.getBasePrice().doubleValue()));
				}
				//CAR-57
				PinCodeResponseData obj = null;
				if (null != pincodeList)
				{
					for (final PinCodeResponseData pinCodeObj : pincodeList)
					{
						if (pinCodeObj.getUssid().equalsIgnoreCase(gwlp.getUSSID()))
						{
							obj = new PinCodeResponseData();
							if (null != pinCodeObj.getCod())
							{
								obj.setCod(pinCodeObj.getCod());
							}
							if (null != pinCodeObj.getIsCODLimitFailed())
							{
								obj.setIsCODLimitFailed(pinCodeObj.getIsCODLimitFailed());
							}
							if (null != pinCodeObj.getIsPrepaidEligible())
							{
								obj.setIsPrepaidEligible(pinCodeObj.getIsPrepaidEligible());
							}
							if (null != pinCodeObj.getIsServicable())
							{
								obj.setIsServicable(pinCodeObj.getIsServicable());
							}
							if (null != pinCodeObj.getUssid())
							{
								obj.setUssid(pinCodeObj.getUssid());
							}
							if (null != pinCodeObj.getValidDeliveryModes())
							{
								obj.setValidDeliveryModes(pinCodeObj.getValidDeliveryModes());
							}
							//TISJEW-3517
							if (isExchangeApplicable)
							{
								obj.setExchangeServiceable(isExchangeApplicable);
							}
							break;
						}

					}
				}
				if (null != obj)
				{
					gwlp.setPinCodeResponse(obj);
				}

				/* Added in R2.3 TISRLUAT-812 start */
				if (null != abstractOrderEntry.getEdScheduledDate())
				{
					gwlp.setScheduleDeliveryDate(abstractOrderEntry.getEdScheduledDate());
				}
				if (null != abstractOrderEntry.getTimeSlotTo() && null != abstractOrderEntry.getTimeSlotFrom())
				{
					gwlp.setScheduleDeliveryTime(abstractOrderEntry.getTimeSlotFrom()
							.concat(" " + MarketplacewebservicesConstants.TO + " ").concat(abstractOrderEntry.getTimeSlotTo()));
				}
				gwlpList.add(gwlp);

				//	}
			}
			//}
		}
		catch (

		final EtailBusinessExceptions e)

		{
			throw e;
		}
		catch (

		final EtailNonBusinessExceptions e)

		{
			throw e;
		}
		catch (

		final Exception e)

		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		//End of AbstractOrderEntryModel for loop,Product Details
		return gwlpList;

	}

	/**
	 * Method to fetch cart product details
	 *
	 * @param abstractOrderEntryList
	 * @return gwlpList
	 */
	@Override
	public List<GetWishListProductWsDTO> freeItems(final List<AbstractOrderEntryModel> abstractOrderEntryList)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		ProductData productData1 = null;
		final List<GetWishListProductWsDTO> gwlpFreeItemList = new ArrayList<GetWishListProductWsDTO>();
		try
		{
			GetWishListProductWsDTO gwlpFreeItem;
			for (final AbstractOrderEntryModel abstractOrderEntry : abstractOrderEntryList)
			{
				if (null != abstractOrderEntry)
				{
					productData1 = productFacade.getProductForOptions(abstractOrderEntry.getProduct(), Arrays.asList(
							ProductOption.BASIC, /* ProductOption.PRICE, */ProductOption.SUMMARY, ProductOption.DESCRIPTION,


							ProductOption.CATEGORIES/*
															 * , ProductOption.PROMOTIONS, ProductOption.STOCK,
															 * ProductOption.DELIVERY_MODE_AVAILABILITY
															 */));


					/*** Free Items ***/
					if (abstractOrderEntry.getGiveAway().booleanValue())
					{
						gwlpFreeItem = new GetWishListProductWsDTO();
						if (StringUtils.isNotEmpty(abstractOrderEntry.getSelectedUSSID()))
						{
							gwlpFreeItem.setUSSID(abstractOrderEntry.getSelectedUSSID());
						}
						if (StringUtils.isNotEmpty(productData1.getName()))
						{
							gwlpFreeItem.setProductName(productData1.getName());
						}
						if (StringUtils.isNotEmpty(productData1.getCode()))
						{
							gwlpFreeItem.setProductcode(productData1.getCode());
						}
						if (StringUtils.isNotEmpty(productData1.getColour()))
						{
							gwlpFreeItem.setColor(productData1.getColour());
						}
						if (StringUtils.isNotEmpty(productData1.getSize()))
						{
							gwlpFreeItem.setSize(productData1.getSize());
						}
						if (StringUtils.isNotEmpty(productData1.getDescription()))
						{
							gwlpFreeItem.setDescription(productData1.getDescription());
						}
						if (null != productData1.getImages())
						{
							final String imgUrl = productData1.getImages() == null ? "" : productData1.getImages().stream()






							.filter(im -> im != null)


							.filter(im -> im.getFormat().toLowerCase().equals(MarketplacecommerceservicesConstants.THUMBNAIL))
									.map(s -> s.getUrl()).findFirst().get();
							gwlpFreeItem.setImageURL(imgUrl);
						}
						gwlpFreeItemList.add(gwlpFreeItem);
					}
					/*** End Of Free Items ***/
				}
			}
		}
		catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		//End of AbstractOrderEntryModel for loop,Product Details
		return gwlpFreeItemList;
	}

	/**
	 * @param promotionResult
	 * @return List<CartOfferDetailsWsDTO>
	 */
	private List<CartOfferDetailsWsDTO> offerDetails(final List<PromotionResultModel> promotionResult, final CartModel cart)
	{
		CartOfferDetailsWsDTO cartOffer = null;
		/*
		 * CartModel cart = null; if (userFacade.isAnonymousUser()) { cart =
		 * mplPaymentWebFacade.findCartAnonymousValues(cartId); } else { cart =
		 * mplPaymentWebFacade.findCartValues(cartId); }
		 */
		final List<CartOfferDetailsWsDTO> cartOfferList = new ArrayList<>();
		MplPromotionData responseData = new MplPromotionData();
		float orderPriorityInitial = -1;
		float productPriorityInitial = -1;
		List<ProductModel> productList = new ArrayList<>();
		List<CategoryModel> categoryList = new ArrayList<>();
		boolean flag = false;

		for (final PromotionResultModel promo : promotionResult)
		{
			if (null != promo && null != promo.getPromotion())
			{
				if (CollectionUtils.isNotEmpty(promo.getPromotion().getChannel()))
				{
					flag = promo.getPromotion().getChannel().stream()
							.anyMatch(s -> SalesApplication.MOBILE.getCode().equals(s.getCode()));
				}
				else
				{
					flag = true;
				}
			}

			if (flag)
			{
				LOG.debug(" ***************** Offer details mobile web service *****************" + flag);
				if (null != promo.getCertainty() && promo.getCertainty().floatValue() < 1.0F)
				{
					cartOffer = new CartOfferDetailsWsDTO();
					if (null != promo.getCertainty() && promo.getPromotion() instanceof OrderPromotionModel
							&& promo.getCertainty().floatValue() > orderPriorityInitial)
					{
						orderPriorityInitial = promo.getCertainty().floatValue();
						final OrderPromotionModel orderPromotion = (OrderPromotionModel) promo.getPromotion();
						responseData = mplDiscountUtil.populatePotentialOrderPromoData(orderPromotion, cart);
						cartOffer.setKey(MarketplacewebservicesConstants.CART_LEVEL);
						if (null != responseData && null != responseData.getPotentialPromotion())
						{

							if (StringUtils.isNotEmpty(responseData.getPotentialPromotion().getPromoMessage()))
							{
								cartOffer.setPotentialOfferDescription(responseData.getPotentialPromotion().getPromoMessage());
							}

							if (StringUtils.isNotEmpty(responseData.getPotentialPromotion().getPromoMessage()))
							{
								cartOffer.setPotentialOfferTitle(responseData.getPotentialPromotion().getPromoPotMessage());
							}

							if (null != responseData.getPotentialPromotion().getPromoStartDate())
							{
								cartOffer.setPotentialOfferStartDate(responseData.getPotentialPromotion().getPromoStartDate());
							}

							if (null != responseData.getPotentialPromotion().getPromoEndDate())
							{
								cartOffer.setPotentialOfferEndDate(responseData.getPotentialPromotion().getPromoEndDate());
							}

							if (StringUtils.isNotEmpty(responseData.getPotentialPromotion().getPromoType()))
							{
								cartOffer.setPotentialPromotionType(responseData.getPotentialPromotion().getPromoType());
							}

						}
					}
					else
					{
						if (null != promo.getCertainty() && promo.getPromotion() instanceof ProductPromotionModel
								&& promo.getCertainty().floatValue() > productPriorityInitial)
						{
							productPriorityInitial = promo.getCertainty().floatValue();
							final ProductPromotionModel productPromotion = (ProductPromotionModel) promo.getPromotion();
							responseData = mplDiscountUtil.populatePotentialPromoData(productPromotion, cart);
							cartOffer.setKey(MarketplacewebservicesConstants.PRODUCT_LEVEL);

							if (null != responseData && null != responseData.getPotentialPromotion())
							{
								if (StringUtils.isNotEmpty(responseData.getPotentialPromotion().getPromoMessage()))
								{
									cartOffer.setPotentialOfferDescription(responseData.getPotentialPromotion().getPromoMessage());
								}

								if (StringUtils.isNotEmpty(responseData.getPotentialPromotion().getPromoMessage()))
								{
									cartOffer.setPotentialOfferTitle(responseData.getPotentialPromotion().getPromoPotMessage());
								}

								if (null != responseData.getPotentialPromotion().getPromoStartDate())
								{
									cartOffer.setPotentialOfferStartDate(responseData.getPotentialPromotion().getPromoStartDate());
								}

								if (null != responseData.getPotentialPromotion().getPromoEndDate())
								{
									cartOffer.setPotentialOfferEndDate(responseData.getPotentialPromotion().getPromoEndDate());
								}

								if (StringUtils.isNotEmpty(responseData.getPotentialPromotion().getPromoType()))
								{
									cartOffer.setPotentialPromotionType(responseData.getPotentialPromotion().getPromoType());
								}

								if (null != responseData.getPotentialPromotion().getPromoPotProducts())
								{
									productList = responseData.getPotentialPromotion().getPromoPotProducts();
								}

								if (null != responseData.getPotentialPromotion().getPromoPotCategories())
								{
									categoryList = responseData.getPotentialPromotion().getPromoPotCategories();
								}
							}


							cartOffer.setPotentialProducts(productList.stream().filter(p -> p != null).map(p -> p.getCode())
									.collect(Collectors.toList()));



							cartOffer.setPotentialCategories(categoryList.stream().filter(c -> c != null).map(c -> c.getCode())
									.collect(Collectors.toList()));

						}
					}
					cartOfferList.add(cartOffer);
				}
			}
		}
		return cartOfferList;
	}

	/**
	 * @param cartModel
	 * @param addressListWsDto
	 * @return CartDataDetailsWsDTO
	 */
	private CartDataDetailsWsDTO cartDetails(final CartModel cartModel, final AddressListWsDTO addressListWsDto,
			final String pincode, final String cartId)
	{
		final CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		int count = 0;
		CartData cartDataOrdered = null;
		/* Product Details */
		List<GetWishListProductWsDTO> gwlpList = new ArrayList<>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<>();
		//CAR-57
		List<PinCodeResponseData> pinCodeRes = null;


		try
		{
			if (cartModel != null)
			{
				mplCouponFacade.releaseVoucherInCheckout(cartModel); //TISPT-104
				cartDataOrdered = mplCartFacade.getSessionCartWithEntryOrderingMobile(cartModel, true, true, true);
				//To calculate discount percentage amount for display purpose
				// TPR-774-- Total MRP calculation and the Product percentage calculation
				/**** Pincode check Details ***/
			}
			try
			{
				if (StringUtils.isNotEmpty(pincode))
				{
					//gwlpList = productDetails(cartModel, cartData, aoem, true, pincode, true, cartId);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("************ Mobile webservice Pincode check at OMS Mobile *******" + pincode);
					}
					//CAR-57
					pinCodeRes = checkPinCodeAtCart(cartDataOrdered, cartModel, pincode);
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartDataOrdered, pinCodeRes, cartModel);
				}
			}
			catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
			{
				LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK, e);
			}
			catch (final Exception e)
			{
				LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK, e);
			}

			//TPR-6117 STARTS
			final Map<String, MaxLimitData> updateCartOnMaxLimExceeds = mplCartFacade.updateCartOnMaxLimExceeds(cartModel);
			MaxLimitWsDto maxLimitWsDto = null;
			final ArrayList<MaxLimitWsDto> maxLimitWsDtoList = new ArrayList<MaxLimitWsDto>();
			if (MapUtils.isNotEmpty(updateCartOnMaxLimExceeds) && updateCartOnMaxLimExceeds.size() > 0)
			{

				for (final Map.Entry<String, MaxLimitData> entry : updateCartOnMaxLimExceeds.entrySet())
				{
					maxLimitWsDto = new MaxLimitWsDto();
					if (null != entry.getValue())
					{
						final MaxLimitData maxCount = entry.getValue();

						maxLimitWsDto.setMaxQuantityAllowed(maxCount.getMaxQuantityAllowed());
						maxLimitWsDto.setProductCode(maxCount.getProductCode());
						maxLimitWsDto.setProductName(maxCount.getProductName());
						maxLimitWsDto.setUserSelectedQty(maxCount.getUserSelectedQty());
						maxLimitWsDto.setUssid(maxCount.getUssid());
						maxLimitWsDtoList.add(maxLimitWsDto);
					}
				}
				cartDataDetails.setReachedMaxLimitforproduct(maxLimitWsDtoList);
			}
			//TPR-6117 ENDS

			/* Product Details */
			if (StringUtils.isNotEmpty(pincode))
			{
				//CAR-57
				gwlpList = productDetails(cartModel, deliveryModeDataMap, true, true, pinCodeRes, pincode);
			}
			else
			{
				//CAR-57
				gwlpList = productDetails(cartModel, deliveryModeDataMap, false, true, pinCodeRes, pincode);
			}

			if (null != gwlpList && !gwlpList.isEmpty())
			{
				for (final GetWishListProductWsDTO entry : gwlpList)
				{
					if (null != entry.getIsGiveAway() && entry.getIsGiveAway().equalsIgnoreCase("N"))
					{
						count++;
					}
				}
				cartDataDetails.setCount(count);
			}
			if (null != gwlpList)
			{
				cartDataDetails.setProducts(gwlpList);
			}
			/* Product Details */
			if (null != cartModel.getSubtotal() && StringUtils.isNotEmpty(cartModel.getSubtotal().toString()))
			{
				final PriceData subtotalprice = discountUtility.createPrice(cartModel,
						Double.valueOf(cartModel.getSubtotal().toString()));
				if (null != subtotalprice && null != subtotalprice.getValue())
				{
					cartDataDetails.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			if (null != cartModel.getTotalPrice() && StringUtils.isNotEmpty(cartModel.getTotalPrice().toString())
					&& null != cartModel.getDeliveryCost() && StringUtils.isNotEmpty(cartModel.getDeliveryCost().toString()))
			{
				final Double totalPriceWithoutDeliveryCharge = new Double(cartModel.getTotalPrice().doubleValue()


				- cartModel.getDeliveryCost().doubleValue());

				final PriceData totalPrice = discountUtility.createPrice(cartModel,
						Double.valueOf(totalPriceWithoutDeliveryCharge.toString()));
				if (null != totalPrice && null != totalPrice.getValue())
				{
					cartDataDetails.setTotalPrice(String.valueOf(totalPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}

			if (null != cartDataOrdered.getTotalDiscounts())
			{
				final PriceData discountPrice = cartDataOrdered.getTotalDiscounts();
				if (null != discountPrice.getValue())
				{
					cartDataDetails.setDiscountPrice(String.valueOf(discountPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			/*** Address details ***/
			if (null != addressListWsDto)
			{
				cartDataDetails.setAddressDetailsList(addressListWsDto);
			}
			/*** End Of Address details ***/
			/*** Offer Details ***/
			List<PromotionResultModel> promotionResult = null;
			if (null != cartModel.getAllPromotionResults() && !cartModel.getAllPromotionResults().isEmpty())
			{
				promotionResult = new ArrayList(cartModel.getAllPromotionResults());
			}
			List<CartOfferDetailsWsDTO> cartOfferList = null;
			if (null != promotionResult && !promotionResult.isEmpty())
			{
				cartOfferList = offerDetails(promotionResult, cartModel);
			}
			if (null != cartOfferList && !cartOfferList.isEmpty())
			{
				cartDataDetails.setOfferDetails(cartOfferList);
			}


		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		/*** End Of Offer Details ***/
		return cartDataDetails;
	}

	/**
	 * @param cartModel
	 * @param addressListWsDto
	 * @return CartDataDetailsWsDTO
	 */
	private CartDataDetailsWsDTO cartDetailsWithPos(final CartModel cartModel, final AddressListWsDTO addressListWsDto,
			final String pincode, final String cartId)
	{
		final CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		int count = 0;
		CartData cartDataOrdered = null;
		//CAR-57
		List<PinCodeResponseData> pinCodeRes = null;
		/* Product Details */
		List<GetWishListProductWsDTO> gwlpList = new ArrayList<>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<>();

		try
		{
			if (cartModel != null)
			{
				cartDataOrdered = mplCartFacade.getSessionCartWithEntryOrderingMobile(cartModel, true, false, false);
				/**** Pincode check Details ***/
				try
				{
					if (StringUtils.isNotEmpty(pincode))
					{
						//gwlpList = productDetails(cartModel, cartData, aoem, true, pincode, true, cartId);
						if (LOG.isDebugEnabled())
						{
							LOG.debug("************ Mobile webservice Pincode check at OMS Mobile *******" + pincode);
						}
						//CAR-57
						pinCodeRes = checkPinCodeAtCart(cartDataOrdered, cartModel, pincode);
						deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartDataOrdered, pinCodeRes, cartModel);
					}
				}
				catch (final CMSItemNotFoundException e)
				{
					LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK, e);
				}
				catch (final Exception e)
				{
					LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK, e);
				}


				//TPR-6117 STARTS
				//	boolean updateCartOnMaxLimExceeds = true;

				final Map<String, MaxLimitData> updateCartOnMaxLimExceeds = mplCartFacade.updateCartOnMaxLimExceeds(cartModel);
				MaxLimitWsDto maxLimitWsDto = null;
				final ArrayList<MaxLimitWsDto> maxLimitWsDtoList = new ArrayList<MaxLimitWsDto>();
				if (MapUtils.isNotEmpty(updateCartOnMaxLimExceeds) && updateCartOnMaxLimExceeds.size() > 0)
				{

					for (final Map.Entry<String, MaxLimitData> entry : updateCartOnMaxLimExceeds.entrySet())
					{
						maxLimitWsDto = new MaxLimitWsDto();
						if (null != entry.getValue())
						{
							final MaxLimitData maxCount = entry.getValue();

							maxLimitWsDto.setMaxQuantityAllowed(maxCount.getMaxQuantityAllowed());
							maxLimitWsDto.setProductCode(maxCount.getProductCode());
							maxLimitWsDto.setProductName(maxCount.getProductName());
							maxLimitWsDto.setUserSelectedQty(maxCount.getUserSelectedQty());
							maxLimitWsDto.setUssid(maxCount.getUssid());
							maxLimitWsDtoList.add(maxLimitWsDto);
						}
					}
					cartDataDetails.setReachedMaxLimitforproduct(maxLimitWsDtoList);
				}
				//TPR-6117 ENDS

				/* Product Details */
				if (StringUtils.isNotEmpty(pincode))
				{
					//CAR-57
					gwlpList = productDetails(cartModel, deliveryModeDataMap, true, false, pinCodeRes, pincode);
				}
				else
				{
					//CAR-57
					gwlpList = productDetails(cartModel, deliveryModeDataMap, false, false, pinCodeRes, pincode);
				}


				if (null != gwlpList && !gwlpList.isEmpty())
				{
					for (final GetWishListProductWsDTO entry : gwlpList)
					{
						if (null != entry.getIsGiveAway() && entry.getIsGiveAway().equalsIgnoreCase("N"))
						{
							count++;
						}
					}

					cartDataDetails.setCount(count);

				}
				if (null != gwlpList)
				{
					cartDataDetails.setProducts(gwlpList);
				}
				/* Product Details */
				if (null != cartModel.getSubtotal() && StringUtils.isNotEmpty(cartModel.getSubtotal().toString()))
				{
					final PriceData subtotalprice = discountUtility.createPrice(cartModel,
							Double.valueOf(cartModel.getSubtotal().toString()));
					if (null != subtotalprice && null != subtotalprice.getValue())
					{
						cartDataDetails
								.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
					}
				}
				if (null != cartModel.getTotalPrice() && StringUtils.isNotEmpty(cartModel.getTotalPrice().toString())
						&& null != cartModel.getDeliveryCost() && StringUtils.isNotEmpty(cartModel.getDeliveryCost().toString()))
				{
					final Double totalPriceWithoutDeliveryCharge = new Double(cartModel.getTotalPrice().doubleValue()


					- cartModel.getDeliveryCost().doubleValue());

					final PriceData totalPrice = discountUtility.createPrice(cartModel,
							Double.valueOf(totalPriceWithoutDeliveryCharge.toString()));
					if (null != totalPrice && null != totalPrice.getValue())
					{
						cartDataDetails.setTotalPrice(String.valueOf(totalPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
					}
				}
				if (null != cartDataOrdered.getTotalDiscounts())
				{
					final PriceData discountPrice = cartDataOrdered.getTotalDiscounts();
					if (null != discountPrice.getValue())
					{
						cartDataDetails
								.setDiscountPrice(String.valueOf(discountPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
					}
				}
				/*** Address details ***/
				if (null != addressListWsDto)
				{
					cartDataDetails.setAddressDetailsList(addressListWsDto);
				}
				/*** End Of Address details ***/
				/*** Offer Details ***/
				List<PromotionResultModel> promotionResult = null;
				if (null != cartModel.getAllPromotionResults() && !cartModel.getAllPromotionResults().isEmpty())
				{
					promotionResult = new ArrayList(cartModel.getAllPromotionResults());
				}
				List<CartOfferDetailsWsDTO> cartOfferList = null;
				if (null != promotionResult && !promotionResult.isEmpty())
				{
					cartOfferList = offerDetails(promotionResult, cartModel);
				}
				if (null != cartOfferList && !cartOfferList.isEmpty())
				{
					cartDataDetails.setOfferDetails(cartOfferList);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9004);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		/*** End Of Offer Details ***/
		return cartDataDetails;
	}

	protected CartData getCurrentSessionCart()
	{
		return cartFacade.getSessionCart();
	}

	/**
	 * pincode response from OMS at cart level
	 *
	 * @param cartData
	 * @param pincode
	 * @return List<PinCodeResponseData>
	 */
	@Override
	public List<PinCodeResponseData> checkPinCodeAtCart(final CartData cartData, final CartModel cartModel, final String pincode)
			throws EtailNonBusinessExceptions
	{

		List<PinCodeResponseData> responseData = null;
		try
		{
			Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
			Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
			if ((null != cartData && cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
			{
				fullfillmentDataMap = mplCartFacade.getFullfillmentMode(cartData);
				if (!StringUtil.isEmpty(pincode))
				{
					responseData = mplCartFacade.getOMSPincodeResponseData(pincode, cartData, cartModel);

					// Changes for Duplicate Cart fix
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartData, responseData, cartModel);
				}
				else
				{
					// Changes for Duplicate Cart fix
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartData, null, cartModel);
				}

				String isServicable = MarketplacecommerceservicesConstants.Y;
				if (responseData != null)
				{
					for (int i = 0; i < responseData.size(); i++)
					{
						if (responseData.get(i).getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
						{
							isServicable = MarketplacecommerceservicesConstants.N;
							break;
						}
					}
				}

				//if ((pincode == null || pincode.isEmpty()) || (!pincode.isEmpty() && deliveryModeDataMap.size() == 0))
				if (StringUtils.isEmpty(pincode) || (StringUtils.isNotEmpty(pincode) && MapUtils.isEmpty(deliveryModeDataMap)))
				{
					isServicable = MarketplacecommerceservicesConstants.N;
				}
				if (LOG.isDebugEnabled())
				{
					LOG.debug(">> isServicable :" + isServicable);
					LOG.debug(">> fullfillmentDataMap :" + fullfillmentDataMap);
				}
			}
			//TISEE-957 isCodEligible flag update
			if (null != responseData)
			{
				final boolean isCOdEligible = mplCartFacade
						.addCartCodEligible(deliveryModeDataMap, responseData, cartModel, cartData);




				LOG.debug("isCOdEligible " + isCOdEligible);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9048);
		}
		return responseData;
	}

	@Override
	public PriceData calculateTotalDiscount(final CartModel cart)
	{
		double discount = 0.0d;
		double totalPrice = 0.0D;
		if (null != cart && null != cart.getEntries() && !cart.getEntries().isEmpty())
		{
			for (final AbstractOrderEntryModel entry : cart.getEntries())
			{
				totalPrice = totalPrice + (entry.getBasePrice().doubleValue() * entry.getQuantity().doubleValue());
			}

			discount = (totalPrice + cart.getDeliveryCost().doubleValue() + cart.getConvenienceCharges().doubleValue())
					- cart.getTotalPriceWithConv().doubleValue();
		}

		final PriceData dicountData = getDiscountUtility().createPrice(cart, Double.valueOf(discount));
		return dicountData;
	}

	/**
	 * create delivery address and adding to cart
	 *
	 * @param addressData
	 * @param cartModel
	 * @return AddressModel
	 */
	@Override
	public AddressModel createDeliveryAddressModel(final AddressData addressData, final CartModel cartModel)
	{
		final AddressModel addressModel = getModelService().create(AddressModel.class);
		try
		{
			getAddressReversePopulator().populate(addressData, addressModel);
			if (null != addressData.getState())
			{
				addressModel.setDistrict(addressData.getState());
			}
			addressModel.setOwner(cartModel);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		return addressModel;
	}

	/**
	 * create delivery address and adding to cart
	 *
	 * @param cartModel
	 * @return AddressModel
	 */
	@Override
	public CartDataDetailsWsDTO displayOrderSummary(final String pincode, final CartModel cartModel,
			final CartDataDetailsWsDTO cartDetailsData)
	{
		//CartModel cartModel = null;
		CartData cartDataOrdered = null;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		boolean deListedStatus = false;
		List<GetWishListProductWsDTO> gwlpList = new ArrayList<GetWishListProductWsDTO>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		//CAR-57
		List<PinCodeResponseData> pinCodeRes = null;
		try
		{
			deListedStatus = mplCartFacade.isCartEntryDelistedMobile(cartModel);

			//Delisted
			if (deListedStatus)
			{
				delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
				cartDetailsData.setDelistedMessage(delistMessage);
			}
			cartDataOrdered = mplCartFacade.getSessionCartWithEntryOrderingMobile(cartModel, true, true, false);
			/**** Pincode check Details ***/
			try
			{
				if (null != pincode && !pincode.isEmpty())
				{
					//CAR-57
					pinCodeRes = checkPinCodeAtCart(cartDataOrdered, cartModel, pincode);
					// Changes for Duplicate Cart fix
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartDataOrdered, pinCodeRes, cartModel);
				}
			}
			catch (final Exception e)
			{
				LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK + pincode, e);
			}

			/* Product Details */
			if (StringUtils.isNotEmpty(pincode))
			{
				//CAR-57
				gwlpList = productDetails(cartModel, deliveryModeDataMap, true, false, pinCodeRes, pincode);
			}
			else
			{
				//CAR-57
				gwlpList = productDetails(cartModel, deliveryModeDataMap, false, false, pinCodeRes, pincode);
			}
			cartDetailsData.setProducts(gwlpList);

			if (null != cartModel.getDeliveryAddress())
			{
				cartDetailsData.setShippingAddress(getShippingAddress(cartModel.getDeliveryAddress()));
			}
			//set Pickup person details ,if cart contains
			if (null != cartModel.getPickupPersonName())
			{
				cartDetailsData.setPickupPersonName(cartModel.getPickupPersonName());
			}
			if (null != cartModel.getPickupPersonMobile())
			{
				cartDetailsData.setPickupPersonMobile(cartModel.getPickupPersonMobile());
			}
			if (StringUtils.isNotEmpty(cartModel.getSubtotal().toString()))
			{
				final PriceData subtotalprice = discountUtility.createPrice(cartModel,
						Double.valueOf(cartModel.getSubtotal().toString()));
				if (null != subtotalprice && null != subtotalprice.getValue())
				{
					cartDetailsData.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			else
			{
				cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				cartDetailsData.setError(MarketplacecommerceservicesConstants.NOSUBTOTAL);
			}
			final PriceData discountPrice = cartDataOrdered.getTotalDiscounts();
			if (null != discountPrice.getValue())
			{
				cartDetailsData.setDiscountPrice(String.valueOf(discountPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
			}
			//Added for Setting Delivery Charge
			if (cartModel.getDeliveryCost() != null)
			{
				cartDetailsData.setDeliveryCharge(cartModel.getDeliveryCost().toString());
			}
			if (null != cartModel.getSubtotal() && cartModel.getSubtotal().doubleValue() >= 0.0
					&& null != cartModel.getDeliveryCost() && null != cartModel.getDeliveryCost()
					&& null != cartDataOrdered.getTotalDiscounts() && null != cartDataOrdered.getTotalDiscounts().getValue()
					&& null != cartDataOrdered.getTotalDiscounts().getValue().toString())
			{
				final double totalafterpromotion = cartModel.getSubtotal().doubleValue() + cartModel.getDeliveryCost().doubleValue()
						- Double.parseDouble(cartDataOrdered.getTotalDiscounts().getValue().toString());
				final PriceData totalPrice = discountUtility.createPrice(cartModel, Double.valueOf(totalafterpromotion));
				if (null != totalPrice && null != totalPrice.getValue())
				{
					cartDetailsData.setTotalPrice(String.valueOf(totalPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
		}
		catch (final Exception e)
		{
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			cartDetailsData.setError(MarketplacecommerceservicesConstants.NOTOTALPRICE);
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
		/* Added in R2.3 for TISRLUAT-904 start */
		if (null != address.getLandmark())
		{
			shippingAddress.setLandmark(address.getLandmark());
		}
		/* Added in R2.3 for TISRLUAT-904 end */

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
	 * create delivery address and adding to cart
	 *
	 * @param orderModel
	 * @return AddressModel
	 */
	@Override
	public CartDataDetailsWsDTO displayOrderSummary(final String pincode, final OrderModel orderModel,
			final CartDataDetailsWsDTO cartDetailsData)
	{
		List<GetWishListProductWsDTO> gwlpList = new ArrayList<GetWishListProductWsDTO>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		try
		{
			/**** Pincode check Details ***/
			try
			{
				deliveryModeDataMap = getDeliveryMode(orderModel);
			}
			catch (final Exception e)
			{
				LOG.error(MarketplacewebservicesConstants.CART_PINCODE_ERROR_OMS_CHECK + pincode, e);
			}

			/* Product Details */
			if (StringUtils.isNotEmpty(pincode))
			{
				//CAR-57--TO-DO: For time being, passing null
				gwlpList = productDetails(orderModel, deliveryModeDataMap, true, false, null, pincode);
			}
			else
			{
				//CAR-57--TO-DO: For time being, passing null
				gwlpList = productDetails(orderModel, deliveryModeDataMap, false, false, null, pincode);
			}
			cartDetailsData.setProducts(gwlpList);

			if (null != orderModel.getDeliveryAddress())
			{
				cartDetailsData.setShippingAddress(getShippingAddress(orderModel.getDeliveryAddress()));
			}
			//set Pickup person details ,if cart contains
			if (null != orderModel.getPickupPersonName())
			{
				cartDetailsData.setPickupPersonName(orderModel.getPickupPersonName());
			}
			if (null != orderModel.getPickupPersonMobile())
			{
				cartDetailsData.setPickupPersonMobile(orderModel.getPickupPersonMobile());
			}
			if (StringUtils.isNotEmpty(orderModel.getSubtotal().toString()))
			{
				final PriceData subtotalprice = discountUtility.createPrice(orderModel,
						Double.valueOf(orderModel.getSubtotal().toString()));
				if (null != subtotalprice && null != subtotalprice.getValue())
				{
					cartDetailsData.setSubtotalPrice(String.valueOf(subtotalprice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
			else
			{
				cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
				cartDetailsData.setError(MarketplacecommerceservicesConstants.NOSUBTOTAL);
			}
			if (null != orderModel.getTotalDiscounts())
			{
				cartDetailsData.setDiscountPrice(String.valueOf(orderModel.getTotalDiscounts().intValue()));
			}
			//Added for Setting Delivery Charge
			if (orderModel.getDeliveryCost() != null)
			{
				cartDetailsData.setDeliveryCharge(orderModel.getDeliveryCost().toString());
			}
			if (null != orderModel.getSubtotal() && orderModel.getSubtotal().doubleValue() >= 0.0
					&& null != orderModel.getDeliveryCost() && null != orderModel.getDeliveryCost()
					&& null != orderModel.getTotalDiscounts())
			{
				final double totalafterpromotion = orderModel.getSubtotal().doubleValue()
						+ orderModel.getDeliveryCost().doubleValue() - Double.parseDouble(orderModel.getTotalDiscounts().toString());
				final PriceData totalPrice = discountUtility.createPrice(orderModel, Double.valueOf(totalafterpromotion));
				if (null != totalPrice && null != totalPrice.getValue())
				{
					cartDetailsData.setTotalPrice(String.valueOf(totalPrice.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)));
				}
			}
		}
		catch (final Exception e)
		{
			cartDetailsData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			cartDetailsData.setError(MarketplacecommerceservicesConstants.NOTOTALPRICE);
		}

		return cartDetailsData;
	}

	public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final OrderModel orderModel)
	{
		List<MarketplaceDeliveryModeData> deliveryModeDataList = null;
		PriceData deliveryCost = null;
		final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		for (final AbstractOrderEntryModel entry : orderModel.getEntries())
		{
			deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
			if (null != entry.getMplDeliveryMode())
			{
				final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
				if (null != entry.getMplDeliveryMode().getDeliveryMode())
				{
					if (entry.getMplDeliveryMode().getDeliveryMode().getCode() != null)
					{
						deliveryModeData.setCode(entry.getMplDeliveryMode().getDeliveryMode().getCode());
					}
					if (entry.getMplDeliveryMode().getDeliveryMode().getDescription() != null)
					{
						deliveryModeData.setDescription(entry.getMplDeliveryMode().getDeliveryMode().getDescription());
					}
					if (entry.getMplDeliveryMode().getDeliveryMode().getName() != null)
					{
						deliveryModeData.setName(entry.getMplDeliveryMode().getDeliveryMode().getName());
					}
					if (entry.getMplDeliveryMode().getValue() != null)
					{
						deliveryCost = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(entry.getMplDeliveryMode()


						.getValue().doubleValue()), orderModel.getCurrency().getIsocode());
						deliveryModeData.setDeliveryCost(deliveryCost);
					}
				}
				if (entry.getMplDeliveryMode().getSellerArticleSKU() != null)
				{
					deliveryModeData.setSellerArticleSKU(entry.getMplDeliveryMode().getSellerArticleSKU());
				}

				deliveryModeDataList.add(deliveryModeData);
			}


			if (!deliveryModeDataList.isEmpty())
			{
				deliveryModeDataMap.put(entry.getEntryNumber().toString(), deliveryModeDataList);
			}
			else
			{
				deliveryModeDataMap.clear();
			}
		}
		return deliveryModeDataMap;
	}


	//TPR-6117 STARTS
	/**
	 * @description Returns the max quantity added in the cart count
	 * @param maxCount
	 * @param code
	 * @param qty
	 * @param stock
	 * @param ussid
	 * @param checkMaxLimList
	 * @return boolean
	 */
	public boolean isMaxProductQuantityAlreadyAdded(final Integer maxCount, final String code, final long qty, long stock,
			final String ussid, final long checkMaxLimList)
	{
		// YTODO Auto-generated method stub

		if (stock < 0)
		{
			stock = mplStockService.getStockLevelDetail(ussid).getAvailable();
		}

		final Integer maxProductQuantityAlreadyAdded = maxCount;

		//		String addToCartFlag = "";
		boolean addToCartFlag = false;
		if (code != null && qty > 0)
		{
			final CartData cartData = getSessionCartWithEntryOrdering(true);

			if (cartData != null && cartData.getEntries() != null && !cartData.getEntries().isEmpty())
			{
				for (final OrderEntryData entry : cartData.getEntries())
				{
					final ProductData productData = entry.getProduct();

					if (code.equals(productData.getCode()))
					{
						if (checkMaxLimList != 0 && checkMaxLimList >= maxProductQuantityAlreadyAdded.intValue())
						{
							LOG.debug("isMaxProductQuantityAlreadyAdded::You are about to exceede the max quantity");
							addToCartFlag = true;
							break;

						}
						else
						{
							LOG.debug("isMaxProductQuantityAlreadyAdded::Product already present in the cart so now we will check the qunatity present in the cart already");

							if (entry.getQuantity().longValue() >= stock)
							{
								LOG.debug("isMaxProductQuantityAlreadyAdded::You are about to exceede the max inventory");
								addToCartFlag = true;
								break;
							}

							if (qty + entry.getQuantity().longValue() > stock)
							{
								LOG.debug("isMaxProductQuantityAlreadyAdded::You have reached the max inventory");
								addToCartFlag = true;
								break;
							}

							if (entry.getQuantity().longValue() >= maxProductQuantityAlreadyAdded.intValue())
							{
								LOG.debug("isMaxProductQuantityAlreadyAdded::You have reached the max quantity");
								addToCartFlag = true;
								break;
							}

							if (qty + entry.getQuantity().longValue() > maxProductQuantityAlreadyAdded.intValue())
							{
								LOG.debug("isMaxProductQuantityAlreadyAdded::You have reached the max quantity");
								addToCartFlag = true;
								break;
							}
						}
					}
				}
			}
			else
			{
				if (qty > stock)
				{
					addToCartFlag = true;
					LOG.debug("isMaxProductQuantityAlreadyAdded(EmptyCartEntry)::You are about to exceede the max inventory");
				}
				if (qty > maxProductQuantityAlreadyAdded.intValue())
				{
					addToCartFlag = true;
					LOG.debug("isMaxProductQuantityAlreadyAdded(EmptyCartEntry)::You are about to exceede the max quantity");
				}

			}
			return addToCartFlag;
		}
		else
		{
			addToCartFlag = true;
		}
		return addToCartFlag;
	}

	/**
	 * @return the mplDeliveryCostService
	 */
	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}

	/**
	 * @param mplDeliveryCostService
	 *           the mplDeliveryCostService to set
	 */
	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}

	/**
	 * @return the wishlistFacade
	 */
	public WishlistFacade getWishlistFacade()
	{
		return wishlistFacade;
	}

	/**
	 * @param wishlistFacade
	 *           the wishlistFacade to set
	 */
	public void setWishlistFacade(final WishlistFacade wishlistFacade)
	{
		this.wishlistFacade = wishlistFacade;
	}

	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartServiceImpl getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartServiceImpl mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}

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

	/**
	 * @return the mplProductWebService
	 */
	public MplProductWebServiceImpl getMplProductWebService()
	{
		return mplProductWebService;
	}

	/**
	 * @param mplProductWebService
	 *           the mplProductWebService to set
	 */
	public void setMplProductWebService(final MplProductWebServiceImpl mplProductWebService)
	{
		this.mplProductWebService = mplProductWebService;
	}

	/**
	 * @return the discountUtility
	 */
	public DiscountUtility getDiscountUtility()
	{
		return discountUtility;
	}

	/**
	 * @param discountUtility
	 *           the discountUtility to set
	 */
	public void setDiscountUtility(final DiscountUtility discountUtility)
	{
		this.discountUtility = discountUtility;
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
	 * @return the addressReversePopulator
	 */
	public Populator<AddressData, AddressModel> getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	/**
	 * @param addressReversePopulator
	 *           the addressReversePopulator to set
	 */
	public void setAddressReversePopulator(final Populator<AddressData, AddressModel> addressReversePopulator)
	{
		this.addressReversePopulator = addressReversePopulator;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.service.MplCartWebService#addProductToCartwithExchange(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	@Override
	public WebSerResponseWsDTO addProductToCartwithExchange(final String productCode, final String cartId, final String quantity,
			final String USSID, final boolean addedToCartWl, final String channel, final String exchangeParam)
			throws InvalidCartException, CommerceCartModificationException
	{
		final WebSerResponseWsDTO result = new WebSerResponseWsDTO();
		final long quant = Long.parseLong(quantity);
		boolean addedToCart = false;
		int count = 0;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		final List<Wishlist2EntryModel> entryModelList = new ArrayList<Wishlist2EntryModel>();
		CartModel cartModel = null;
		boolean delisted = false;
		ProductModel productModel = null;
		ProductModel selectedProductModel = null;
		try
		{

			//changes for CarProject
			//cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartId);
			cartModel = cartService.getSessionCart();
			//changes for CarProject ends
			if (cartModel == null)
			{
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_CART_ID + cartId);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9064);
			}
			else
			{
				for (final AbstractOrderEntryModel pr : cartModel.getEntries())
				{
					productModel = pr.getProduct();
					if (productCode.equals(productModel.getCode()) && USSID.equals(pr.getSelectedUSSID()))
					{
						final int maximum_configured_quantiy = siteConfigService.getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
						if (StringUtils.isNotEmpty(pr.getExchangeId()))
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9305);
						}
						if (pr.getQuantity().longValue() >= maximum_configured_quantiy)
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9065);
						}
						if (Long.parseLong(quantity) + pr.getQuantity().longValue() > maximum_configured_quantiy)
						{
							throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9066);
						}
						selectedProductModel = productModel;
						//break;
					}
					//counting no of items in cart not freebie
					if (null != pr.getGiveAway() && !pr.getGiveAway().booleanValue())
					{
						count++;
					}
				}
			}
			result.setCount(String.valueOf(count));

			if (selectedProductModel == null)
			{
				//changes for CarProject
				//selectedProductModel = productService.getProductForCode(defaultPromotionManager.catalogData(), productCode);
				selectedProductModel = productService.getProductForCode(productCode);
				// changes for CarProject ends
				if (selectedProductModel == null)
				{
					LOG.debug(MarketplacecommerceservicesConstants.INVALID_PRODUCT_CODE + productCode);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9037);
				}
			}
			if (quant <= 0)
			{
				LOG.debug(MarketplacecommerceservicesConstants.INVALID_PRODUCT_QUANTITY + quantity);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9068);
			}
			for (final SellerInformationModel seller : selectedProductModel.getSellerInformationRelator())
			{
				if (seller.getSellerArticleSKU().equalsIgnoreCase(USSID)
						&& (seller.getSellerAssociationStatus() != null && (seller.getSellerAssociationStatus().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.NO) || (seller.getEndDate() != null && new Date()
								.after(seller.getEndDate())))))
				{
					delisted = true;
					break;
				}
			}
			if (delisted)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("*********** Items delisted *************" + delistMessage);
				}
				delistMessage = Localization.getLocalizedString(MarketplacewebservicesConstants.DELISTED_MESSAGE_CART);
				result.setDelistedMessage(delistMessage);
			}
			else
			{
				addedToCart = mplCartFacade.addItemToCartwithExchange(cartId, cartModel, selectedProductModel, quant, USSID,
						exchangeParam);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("*********** Products added status in cart *************  ::::USSID::::" + USSID + ":::added???"
							+ addedToCart);
				}
				final List<Wishlist2EntryModel> allWishlistEntry = wishlistFacade.getAllWishlistByUssid(USSID);
				for (final Wishlist2EntryModel entryModel : allWishlistEntry)
				{
					entryModel.setAddToCartFromWl(Boolean.valueOf(addedToCartWl));
					if (LOG.isDebugEnabled())
					{
						LOG.debug("*********** Add to cart from WL mobile web service *************" + addedToCart + "::USSID::"
								+ USSID);
					}
					entryModelList.add(entryModel);
				}
				//For saving all the data at once rather in loop;
				modelService.saveAll(entryModelList);
				//TISLUX-1823 -For LuxuryWeb
				if (channel != null && channel.equalsIgnoreCase(SalesApplication.WEB.getCode()))
				{
					cartModel.setChannel(SalesApplication.WEB);
					modelService.save(cartModel);
				}
			}

			if (!addedToCart && !delisted)
			{
				LOG.debug(MarketplacecommerceservicesConstants.FAILURE_CARTADD);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9069);
			}
			if (LOG.isDebugEnabled())
			{
				LOG.debug("*********** Products added successfully  Mobile web service *************");
			}
			result.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
		}
		catch (final InvalidCartException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9064);
		}

		catch (final CommerceCartModificationException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9070);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return result;
	}

	/**
	 * Service to merge carts
	 *
	 * @param fromAnonymousCartGuid
	 * @param toUserCartGuid
	 * @return CartRestorationData
	 * @throws CommerceCartRestorationException
	 * @throws CommerceCartMergingException
	 */
	@Override
	public CartRestorationData restoreAnonymousCartAndMerge(final String fromAnonymousCartGuid, final String toUserCartGuid)
			throws CommerceCartRestorationException, CommerceCartMergingException
	{
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
		final CartModel fromCart = getCommerceCartService().getCartForGuidAndSiteAndUser(fromAnonymousCartGuid, currentBaseSite,
				userService.getAnonymousUser());

		final CartModel toCart = getCommerceCartService().getCartForGuidAndSiteAndUser(toUserCartGuid, currentBaseSite,
				userService.getCurrentUser());
		/* SONAR FIX JEWELLERY */
		//	final CartModel anonymousCartModel = null;
		if (toCart == null)
		{
			throw new CommerceCartRestorationException("Cart cannot be null");
		}

		if (fromCart == null)
		{
			return restoreSavedCart(toUserCartGuid);
		}

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		//		parameter.setCart(toCart);
		//TPR-1083 Online Exchange facilities to the customer for Large Appliances
		if (null != fromCart && null != fromCart.getExchangeAppliedCart() && fromCart.getExchangeAppliedCart().booleanValue()
				&& CollectionUtils.isNotEmpty(fromCart.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : fromCart.getEntries())
			{
				if (StringUtils.isNotEmpty(entry.getExchangeId()))
				{
					parameter.setCart(fromCart);
				}
			}
		}
		else
		{
			parameter.setCart(toCart);
		}
		final CommerceCartRestoration restoration = getCommerceCartService().restoreCart(parameter);
		parameter.setCart(cartService.getSessionCart());

		//		commerceCartService.mergeCarts(fromCart, parameter.getCart(), restoration.getModifications());

		//	TPR-1083 Online Exchange facilities to the customer for Large Appliances
		if (null != fromCart && null != fromCart.getExchangeAppliedCart() && fromCart.getExchangeAppliedCart().booleanValue()
				&& CollectionUtils.isNotEmpty(fromCart.getEntries()))
		{
			//			commerceCartService.mergeCarts(parameter.getCart(), toCart, restoration.getModifications());
			commerceCartService.mergeCarts(toCart, parameter.getCart(), restoration.getModifications());
		}
		else
		{
			commerceCartService.mergeCarts(fromCart, parameter.getCart(), restoration.getModifications());
		}

		final CommerceCartRestoration commerceCartRestoration = getCommerceCartService().restoreCart(parameter);

		commerceCartRestoration.setModifications(restoration.getModifications());

		cartService.changeCurrentCartUser(userService.getCurrentUser());
		return getCartRestorationConverter().convert(commerceCartRestoration);
	}

}
