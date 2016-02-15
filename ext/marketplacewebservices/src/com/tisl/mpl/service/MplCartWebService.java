/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
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

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
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
			boolean addedToCartWl) throws InvalidCartException, CommerceCartModificationException;

	/**
	 * Service to get cart details
	 *
	 * @param cartId
	 * @param addressListDTO
	 * @return CartDataDetailsWsDTO
	 */
	CartDataDetailsWsDTO getCartDetails(String cartId, AddressListWsDTO addressListDTO, String pincode);

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
	List<GetWishListProductWsDTO> productDetails(List<AbstractOrderEntryModel> aoem, boolean isPinCodeCheckRequired,
			String pincode, boolean resetReqd, String cartId) throws EtailBusinessExceptions, EtailNonBusinessExceptions;

	/**
	 * pincode response from OMS at cart level
	 *
	 * @param cartData
	 * @param pincode
	 * @return List<PinCodeResponseData>
	 * @throws CMSItemNotFoundException
	 */
	public List<PinCodeResponseData> checkPinCodeAtCart(final CartData cartData, final String pincode)
			throws CMSItemNotFoundException;


	/**
	 * create delivery address and adding to cart
	 *
	 * @param addressData
	 * @param cartModel
	 * @return AddressModel
	 */
	public AddressModel createDeliveryAddressModel(final AddressData addressData, final CartModel cartModel);

	public PriceData calculateTotalDiscount(final CartModel cart);
}
