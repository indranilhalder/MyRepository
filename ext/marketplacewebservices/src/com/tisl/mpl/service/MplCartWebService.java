/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartRestorationData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.wsdto.CartDataDetailsWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
import com.tisl.mpl.wsdto.WebSerResponseWsDTO;


/**
 * @author TCS
 *
 */
public interface MplCartWebService
{
	/**
	 * Service to create cart
	 *
	 * @param oldCartId
	 * @param toMergeCartGuid
	 * @return WebSerResponseWsDTO
	 */
	WebSerResponseWsDTO createCart(String oldCartId, String toMergeCartGuid);

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
	WebSerResponseWsDTO addProductToCart(final String productCode, final String cartId, final String quantity, String USSID,
			boolean addedToCartWl, String channel) throws InvalidCartException, CommerceCartModificationException;

	/**
	 * Service to get cart details
	 *
	 * @param cartId
	 * @param addressListWsDTO
	 * @param pincode
	 * @param channel
	 * @return CartDataDetailsWsDTO
	 */
	CartDataDetailsWsDTO getCartDetails(final String cartId, final AddressListWsDTO addressListWsDTO, final String pincode,
			String channel);

	/**
	 * @param aoem
	 * @return List<GetWishListProductWsDTO>
	 * @throws EtailBusinessExceptions
	 * @throws EtailNonBusinessExceptions
	 */
	List<GetWishListProductWsDTO> freeItems(List<AbstractOrderEntryModel> aoem)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions;

	/**
	 * Service to get product details
	 *
	 * @param cartModel
	 * @param deliveryModeDataMap
	 * @param isPinCodeCheckRequired
	 * @param resetReqd
	 * @return List<GetWishListProductWsDTO>
	 * @throws EtailBusinessExceptions
	 * @throws EtailNonBusinessExceptions
	 */
	//CAR-57
	public List<GetWishListProductWsDTO> productDetails(final AbstractOrderModel abstractOrderModel,
			final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap, final boolean isPinCodeCheckRequired,
			final boolean resetRequired, final List<PinCodeResponseData> pincodeList, final String pincode)
					throws EtailBusinessExceptions, EtailNonBusinessExceptions;

	/**
	 * pincode response from OMS at cart level
	 *
	 * @param cartData
	 * @param cartModel
	 * @param pincode
	 * @return List<PinCodeResponseData>
	 * @throws EtailNonBusinessExceptions
	 */
	public List<PinCodeResponseData> checkPinCodeAtCart(final CartData cartData, CartModel cartModel, final String pincode)
			throws EtailNonBusinessExceptions;


	/**
	 * create delivery address and adding to cart
	 *
	 * @param addressData
	 * @param cartModel
	 * @return AddressModel
	 */
	public AddressModel createDeliveryAddressModel(final AddressData addressData, final CartModel cartModel);

	public PriceData calculateTotalDiscount(final CartModel cart);

	/**
	 * Service to get cart details with POS
	 *
	 * @param cartId
	 * @param addressListDTO
	 * @param pincode
	 * @return CartDataDetailsWsDTO
	 */
	public CartDataDetailsWsDTO getCartDetailsWithPOS(String cartId, AddressListWsDTO addressListDTO, String pincode);

	/**
	 * Service to get cart details with with all summary using cartModel
	 *
	 * @param cartModel
	 * @param pincode
	 * @param cartDetailsData
	 * @return CartDataDetailsWsDTO
	 */
	public CartDataDetailsWsDTO displayOrderSummary(final String pincode, final CartModel cartModel,
			final CartDataDetailsWsDTO cartDetailsData);

	/**
	 * Service to get cart details with with all summary using OrderModel
	 *
	 * @param cartModel
	 * @param pincode
	 * @param cartDetailsData
	 * @return CartDataDetailsWsDTO
	 */
	public CartDataDetailsWsDTO displayOrderSummary(final String pincode, final OrderModel cartModel,
			final CartDataDetailsWsDTO cartDetailsData);

	/**
	 * Service to add product to cart
	 *
	 * @param productCode
	 * @param cartId
	 * @param quantity
	 * @param USSID
	 * @param exchangeParam
	 * @return WebSerResponseWsDTO
	 * @throws CommerceCartModificationException
	 * @throws InvalidCartException
	 */
	WebSerResponseWsDTO addProductToCartwithExchange(final String productCode, final String cartId, final String quantity,
			String USSID, boolean addedToCartWl, String channel, String exchangeParam)
					throws InvalidCartException, CommerceCartModificationException;

	/**
	 * Service to merge carts
	 * 
	 * @param fromAnonymousCartGuid
	 * @param toUserCartGuid
	 * @return CartRestorationData
	 * @throws CommerceCartRestorationException
	 * @throws CommerceCartMergingException
	 */
	public CartRestorationData restoreAnonymousCartAndMerge(final String fromAnonymousCartGuid, final String toUserCartGuid)
			throws CommerceCartRestorationException, CommerceCartMergingException;

}
