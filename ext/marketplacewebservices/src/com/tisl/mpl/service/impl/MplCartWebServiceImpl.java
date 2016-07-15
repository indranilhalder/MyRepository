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
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
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
import org.springframework.security.access.AccessDeniedException;

import com.tisl.mpl.cart.impl.CommerceWebServicesCartFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.data.MplPromotionData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.wishlist.WishlistFacade;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.impl.MplCommerceCartServiceImpl;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.MplCartWebService;
import com.tisl.mpl.util.DiscountUtility;
import com.tisl.mpl.utility.MplDiscountUtil;
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.CartOfferDetailsWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
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
	@Resource(name = "defaultPromotionManager")
	private DefaultPromotionManager defaultPromotionManager;
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
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private MplPaymentWebFacade mplPaymentWebFacade;
	@Resource
	private SiteConfigService siteConfigService;
	@Resource
	private Converter<CartModel, CartData> mplExtendedCartConverter;
	@Resource
	private MplDiscountUtil mplDiscountUtil;
	@Resource(name = "addressReversePopulator")
	private Populator<AddressData, AddressModel> addressReversePopulator;

	private static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";

	private final static Logger LOG = Logger.getLogger(MplCartWebServiceImpl.class);

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

				final CommerceCartModification modification = mplCommerceCartService.addToCartWithUSSID(newCartParameter);

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
			final String USSID, final boolean addedToCartWl) throws InvalidCartException, CommerceCartModificationException
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

			if (userFacade.isAnonymousUser())
			{
				cartModel = mplPaymentWebFacade.findCartAnonymousValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Anonymous cart mobile (addProductToCart) **************" + cartId);
				}
			}
			else
			{
				cartModel = mplPaymentWebFacade.findCartValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Logged-in cart mobile (addProductToCart)**************" + cartId);
				}
			}

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
				selectedProductModel = productService.getProductForCode(defaultPromotionManager.catalogData(), productCode);
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
	public CartDataDetailsWsDTO getCartDetails(final String cartId, final AddressListWsDTO addressListWsDTO, final String pincode)
	{

		LOG.debug(String.format("Getcart details : Cart id : %s | Pincode: %s ", cartId, pincode));

		CartDataDetailsWsDTO cartDataDetails = new CartDataDetailsWsDTO();
		CartModel cart = null;
		String delistMessage = MarketplacewebservicesConstants.EMPTY;
		try
		{
			if (userFacade.isAnonymousUser())
			{

				cart = mplPaymentWebFacade.findCartAnonymousValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Anonymous cart mobile **************" + cartId);
				}
			}
			else
			{

				cart = mplPaymentWebFacade.findCartValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Logged-in cart mobile **************" + cartId);
				}
			}
			if (cart != null)
			{
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
			if (userFacade.isAnonymousUser())
			{

				cart = mplPaymentWebFacade.findCartAnonymousValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Anonymous cart mobile **************" + cartId);
				}
			}
			else
			{

				cart = mplPaymentWebFacade.findCartValues(cartId);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("************ Logged-in cart mobile **************" + cartId);
				}
			}
			if (cart != null)
			{
				final boolean deListedStatus = mplCartFacade.isCartEntryDelisted(cart);

				LOG.debug("Cart Delisted Status " + deListedStatus);

				//final CartModel newCartModel = mplCartFacade.removeDeliveryMode(cartModel);
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
	 * Method to fetch cart product details
	 *
	 * @param cartId
	 * @param cartModel
	 * @param cartData
	 * @param deliveryModeDataMap
	 * @param isPinCodeCheckRequired
	 * @param resetReqd
	 * @return gwlpList
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<GetWishListProductWsDTO> productDetails(final String cartId, final CartModel cartModel, final CartData cartData,
			final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap, final boolean isPinCodeCheckRequired,
			final boolean resetReqd) throws EtailBusinessExceptions, EtailNonBusinessExceptions
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(String.format("productDetails: |  cartId : %s | isPinCodeCheckRequired %s", cartId, Boolean.valueOf(resetReqd)));
		}
		CartModel finalCart = null;
		final List<GetWishListProductWsDTO> gwlpList = new ArrayList<>();
		ProductData productData1 = null;
		final List<MarketplaceDeliveryModeData> deliveryModeList = new ArrayList<>();
		try
		{
			cartModel.setChannel(SalesApplication.MOBILE);
			getModelService().save(cartModel);
			if (resetReqd)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("productDetails:********  resetReqd is true*******Mobile Removing the delivery mode mobile ******"
							+ cartId);
				}
				finalCart = mplCartFacade.removeDeliveryMode(cartModel);
			}
			else
			{
				commerceCartService.recalculateCart(cartModel);
				finalCart = cartModel;
			}
			List<PromotionResultModel> promotionResult = null;
			if (null != finalCart.getAllPromotionResults() && !finalCart.getAllPromotionResults().isEmpty())
			{
				promotionResult = new ArrayList(finalCart.getAllPromotionResults());
			}

			//Removed checkedPincode
			//	if (null != finalCart.getEntries() && !finalCart.getEntries().isEmpty())
			/*
			 * TISPT- 96 -- https://github.com/tcs-chennai/TCS_COMMERCE_REPO/pull/3577
			 * 
			 * {
			 */
			for (final AbstractOrderEntryModel abstractOrderEntry : finalCart.getEntries())
			{

				//if (null != abstractOrderEntry && null != abstractOrderEntry.getProduct())
				/*
				 * review comments incorporated TISPT- 96 -- https://github.com/tcs-chennai/TCS_COMMERCE_REPO/pull/3577
				 */
				//{
				productData1 = productFacade.getProductForOptions(abstractOrderEntry.getProduct(), Arrays.asList(ProductOption.BASIC,
						ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.CATEGORIES,
						ProductOption.PROMOTIONS, ProductOption.STOCK, ProductOption.DELIVERY_MODE_AVAILABILITY));
				final GetWishListProductWsDTO gwlp = new GetWishListProductWsDTO();
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
				if (null != productData1.getBrand() && StringUtils.isNotEmpty(productData1.getBrand().getBrandname()))
				{
					gwlp.setProductBrand(productData1.getBrand().getBrandname());
				}

				if (null != abstractOrderEntry.getGiveAway() && abstractOrderEntry.getGiveAway().booleanValue())
				{
					gwlp.setIsGiveAway("Y");
				}
				else
				{
					gwlp.setIsGiveAway("N");
				}
				if (CollectionUtils.isNotEmpty(abstractOrderEntry.getAssociatedItems()))
				{
					gwlp.setAssociatedBaseProducts(abstractOrderEntry.getAssociatedItems());
				}
				if (StringUtils.isNotEmpty(productData1.getRootCategory()))
				{
					gwlp.setRootCategory(productData1.getRootCategory());
				}
				else
				{
					LOG.info("*************** Mobile webservice root category is empty ********************");

				}

				final String catId = mplProductWebService.getCategoryCodeOfProduct(productData1);
				if (null != catId && !catId.isEmpty())
				{
					gwlp.setProductCategoryId(catId);
				}
				if (CollectionUtils.isNotEmpty(productData1.getImages()))
				{
					//Set product image(thumbnail) url
					for (final ImageData img : productData1.getImages())
					{
						if (null != img && null != img.getUrl() && StringUtils.isNotEmpty(img.getFormat())
						//&& img.getFormat().toLowerCase().equals(MarketplacecommerceservicesConstants.THUMBNAIL) Sonar fix
								&& img.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.THUMBNAIL))
						{
							gwlp.setImageURL(img.getUrl());
						}

					}
				}
				else
				{
					LOG.info("*************** Mobile webservice images are empty ********************");
				}

				if (StringUtils.isNotEmpty(productData1.getColour()))
				{
					gwlp.setColor(productData1.getColour());
				}
				else
				{
					LOG.info("*************** Mobile webservice color is empty ********************");
				}
				if (StringUtils.isNotEmpty(productData1.getSize()))
				{
					gwlp.setSize(productData1.getSize());
				}
				else
				{
					LOG.info("*************** Mobile webservice size is empty ********************");
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
								LOG.info("*************** Mobile webservice product capacity empty********************");
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
				final Predicate<SellerInformationData> pred2 = o -> o.getUssid().equals(abstractOrderEntry.getSelectedUSSID());

				if (null != productData1.getSeller() && productData1.getSeller().size() > 0)
				{
					productData1.getSeller().stream().filter(pred1.and(pred2)).findFirst().ifPresent(c -> {
						gwlp.setSellerId(c.getSellerID());
						gwlp.setSellerName(c.getSellername());
						gwlp.setFullfillmentType(c.getFullfillment());
					});
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
								if (null != gwlp.getFullfillmentType() && !gwlp.getFullfillmentType().isEmpty()
										&& gwlp.getFullfillmentType().equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIP))
								{
									delivery.setDeliveryCost(MarketplacecommerceservicesConstants.ZeroDeliveryCost);

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
				if (!resetReqd)
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
				if (null != abstractOrderEntry.getBasePrice() && null != abstractOrderEntry.getQuantity())
				{
					entryPrice = abstractOrderEntry.getBasePrice().doubleValue() * abstractOrderEntry.getQuantity().doubleValue();
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
					final PriceData cartLevelDisc = discountUtility.createPrice(finalCart,
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

					gwlp.setProductLevelDiscount(new Double(df2.format(abstractOrderEntry.getTotalProductLevelDisc())));
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
								Collection<ProductModel> promoProducts = null;
								if (null != productPromotion.getProducts() && !productPromotion.getProducts().isEmpty())
								{
									promoProducts = productPromotion.getProducts();
								}
								if (null != promoProducts && !promoProducts.isEmpty())
								{
									for (final ProductModel products : promoProducts)
									{
										if (null != products.getCode() && null != abstractOrderEntry.getProduct()
												&& null != abstractOrderEntry.getProduct().getCode()
												&& products.getCode().equals(abstractOrderEntry.getProduct().getCode()))
										{

											gwlp.setProductLevelDiscDesc(productPromotion.getDescription());
											//////////////////////////

											if (null != promo.getCertainty() && promo.getCertainty().floatValue() == 1.0F)
											{
												appliedResponseData = mplDiscountUtil.populateData(productPromotion, finalCart);
												if (null != appliedResponseData.getDiscountPrice())
												{
													gwlp.setDiscountPrice(appliedResponseData.getDiscountPrice());

												}
												if (null != appliedResponseData.getIsPercentage())
												{
													gwlp.setIsPercentage(appliedResponseData.getIsPercentage());


												}
												if (null != appliedResponseData.getPercentagePromotion())
												{

													gwlp.setPercentagePromotion(appliedResponseData.getPercentagePromotion());
												}
											}
											///////////////////////////////
										}
									}
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
										appliedResponseData = mplDiscountUtil.populateCartPromoData(orderPromotionModel, finalCart);
										if (null != appliedResponseData.getDiscountPrice())
										{

											gwlp.setCartDiscountPrice(appliedResponseData.getDiscountPrice());
										}
										if (null != appliedResponseData.getIsPercentage())
										{

											gwlp.setCartIsPercentage(appliedResponseData.getIsPercentage());

										}
										if (null != appliedResponseData.getPercentagePromotion())
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
				gwlpList.add(gwlp);
				//	}
			}
			//}
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
							ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
							ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.STOCK,
							ProductOption.DELIVERY_MODE_AVAILABILITY));
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
				if (null != promo.getPromotion().getChannel() && !promo.getPromotion().getChannel().isEmpty())
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
		try
		{
			//cartData = getMplExtendedCartConverter().convert(cartModel);
			cartDataOrdered = mplCartFacade.getSessionCartWithEntryOrderingMobile(cartModel, true);
			/**** Pincode check Details ***/
			try
			{
				if (null != pincode && !pincode.isEmpty())
				{
					//gwlpList = productDetails(cartModel, cartData, aoem, true, pincode, true, cartId);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("************ Mobile webservice Pincode check at OMS Mobile *******" + pincode);
					}
					final List<PinCodeResponseData> pinCodeRes = checkPinCodeAtCart(cartDataOrdered, pincode);
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartDataOrdered, pinCodeRes);

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

			/* Product Details */
			if (null != pincode && !pincode.isEmpty())
			{
				gwlpList = productDetails(cartId, cartModel, cartDataOrdered, deliveryModeDataMap, true, false);
			}
			else
			{
				gwlpList = productDetails(cartId, cartModel, cartDataOrdered, deliveryModeDataMap, false, false);
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
		/* Product Details */
		List<GetWishListProductWsDTO> gwlpList = new ArrayList<>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<>();
		try
		{
			//cartData = getMplExtendedCartConverter().convert(cartModel);
			cartDataOrdered = mplCartFacade.getSessionCartWithEntryOrderingMobile(cartModel, true);
			/**** Pincode check Details ***/
			try
			{
				if (null != pincode && !pincode.isEmpty())
				{
					//gwlpList = productDetails(cartModel, cartData, aoem, true, pincode, true, cartId);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("************ Mobile webservice Pincode check at OMS Mobile *******" + pincode);
					}

					final List<PinCodeResponseData> pinCodeRes = checkPinCodeAtCart(cartDataOrdered, pincode);
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartDataOrdered, pinCodeRes);
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

			/* Product Details */
			if (null != pincode && !pincode.isEmpty())
			{
				gwlpList = productDetails(cartId, cartModel, cartDataOrdered, deliveryModeDataMap, true, false);
			}
			else
			{
				gwlpList = productDetails(cartId, cartModel, cartDataOrdered, deliveryModeDataMap, false, false);
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
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public List<PinCodeResponseData> checkPinCodeAtCart(final CartData cartData, final String pincode)
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
					responseData = mplCartFacade.getOMSPincodeResponseData(pincode, cartData);
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartData, responseData);
				}
				else
				{
					deliveryModeDataMap = mplCartFacade.getDeliveryMode(cartData, null);
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
				final boolean isCOdEligible = mplCartFacade.addCartCodEligible(deliveryModeDataMap, responseData);
				LOG.info("isCOdEligible " + isCOdEligible);
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

	//	private static OrderEntryData getCartEntryForNumber(final CartData cart, final long number) throws CartEntryException
	//	{
	//		final List<OrderEntryData> entries = cart.getEntries();
	//		if (entries != null && !entries.isEmpty())
	//		{
	//			final Integer requestedEntryNumber = Integer.valueOf((int) number);
	//			for (final OrderEntryData entry : entries)
	//			{
	//				if (entry != null && requestedEntryNumber.equals(entry.getEntryNumber()))
	//				{
	//					return entry;
	//				}
	//			}
	//		}
	//		throw new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number));
	//	}


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
}
