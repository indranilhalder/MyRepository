/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
			boolean addedToCartWl, HttpServletRequest request) throws InvalidCartException, CommerceCartModificationException;

	/**
	 * Service to get cart details
	 *
	 * @param cartId
	 * @param addressListDTO
	 * @return CartDataDetailsWsDTO
	 */
	CartDataDetailsWsDTO getCartDetails(String cartId, AddressListWsDTO addressListDTO, String pincode, HttpServletRequest request);

	/**
	 * @param aoem
	 * @return List<GetWishListProductWsDTO>
	 * @throws EtailBusinessExceptions
	 * @throws EtailNonBusinessExceptions
	 */
	List<GetWishListProductWsDTO> freeItems(List<AbstractOrderEntryModel> aoem) throws EtailBusinessExceptions,
			EtailNonBusinessExceptions;

	/**
	 * Service to get product details
	 *
	 * @param aoem
	 * @return List<GetWishListProductWsDTO>
	 * @throws EtailBusinessExceptions
	 * @throws EtailNonBusinessExceptions
	 */
	List<GetWishListProductWsDTO> productDetails(String cartId, CartModel cartModel, CartData cartData,
			Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMapfinal, boolean isPinCodeCheckRequired,
			boolean resetReqd) throws EtailBusinessExceptions, EtailNonBusinessExceptions;

	/**
	 * pincode response from OMS at cart level
	 *
	 * @param cartData
	 * @param pincode
	 * @return List<PinCodeResponseData>
	 * @throws EtailNonBusinessExceptions
	 */
	public List<PinCodeResponseData> checkPinCodeAtCart(final CartData cartData, final String pincode)
			throws EtailBusinessExceptions, EtailNonBusinessExceptions;


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
	public CartDataDetailsWsDTO getCartDetailsWithPOS(String cartId, AddressListWsDTO addressListDTO, String pincode,
			HttpServletRequest request);
}
