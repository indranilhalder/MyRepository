/**
 *
 */
package com.tisl.mpl.facade.checkout;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.CartModel;

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
	boolean hasValidCart();

	/**
	 * @return
	 */
	boolean hasNoDeliveryAddress();

	/**
	 * @return
	 */
	boolean hasNoDeliveryMode();

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


}
