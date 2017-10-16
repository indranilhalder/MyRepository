/**
 *
 */
package com.tisl.mpl.facade.checkout;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
/*
 * Facade created for Cart Delivery address modification (State field addition)
 */
public interface MplCustomAddressFacade
{
	CartData getCheckoutCart();

	List<? extends AddressData> getDeliveryAddresses(final AddressData selectedAddressData);

	AddressData getDeliveryAddressForCode(final String code);

	/**
	 * @param deliveryCode
	 * @param sellerArticleSKU
	 * @return Double
	 * */
	public Double populateDeliveryMethodData(String deliveryCode, String sellerArticleSKU);

	/**
	 * @return
	 */
	boolean hasValidCart(final CartData cart);

	/**
	 * @return
	 */
	boolean hasNoDeliveryAddress(final CartData cart);

	/**
	 * @return
	 */
	boolean hasNoDeliveryMode(final CartData cart);

	/**
	 * @return
	 */
	boolean hasNoPaymentInfo();

	/**
	 * @param source
	 * @param prototype
	 * @return PriceData
	 */
	public PriceData addConvCharge(CartModel source, CartData prototype);

	/**
	 * @param source
	 * @param prototype
	 * @return PriceData
	 */
	public PriceData setTotalWithConvCharge(CartModel source, CartData prototype);

	/**
	 * set delivery mode
	 *
	 * @param deliveryCode
	 * @param sellerArticleSKU
	 * */
	public void populateDeliveryMethod(String deliveryCode, String sellerArticleSKU) throws EtailNonBusinessExceptions;

	/**
	 * @param addressData
	 * @return boolean
	 */
	boolean setDeliveryAddress(final AddressData addressData);

	/**
	 * @param source
	 * @param prototype
	 * @return PriceData
	 */
	PriceData addConvCharge(OrderModel source, OrderData prototype);

	/**
	 * @param source
	 * @param prototype
	 * @return PriceData
	 */
	PriceData setTotalWithConvCharge(OrderModel source, OrderData prototype);


	/**
	 * @param deliveryCode
	 * @param sellerArticleSKU
	 * @param cartModel
	 * @return Double
	 */
	Double populateDeliveryMethodData(String deliveryCode, String sellerArticleSKU, CartModel cartModel);

	/**
	 * It is responsible for fetching Cart Data
	 *
	 * @param cartModel
	 * @return
	 */
	public CartData getCheckoutCartWS(final CartModel cartModel);

	/**
	 * @param deliveryAddress
	 * @param cartModel
	 * @return
	 */
	List<AddressData> getDeliveryAddresses(AddressData deliveryAddress, CartModel cartModel); //CAR-194

	/**
	 * UF:281
	 *
	 * @Description: Method to get delivery address.
	 * @return AddressData
	 *
	 */
	public AddressData getDeliveryAddress();

	/**
	 * @param cartModel
	 * @return
	 */
	public AddressData getDeliveryAddress(CartModel cartModel);

	/**
	 * @param cart
	 * @return
	 */
	public CartData getCheckoutCart(CartModel cart);
}
