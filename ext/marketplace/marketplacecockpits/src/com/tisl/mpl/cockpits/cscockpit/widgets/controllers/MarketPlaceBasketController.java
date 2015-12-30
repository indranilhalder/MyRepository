package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import java.util.List;
import java.util.Map;

import org.zkoss.zul.Button;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;

// TODO: Auto-generated Javadoc
/**
 * The Interface MarketPlaceBasketController.
 */
public interface MarketPlaceBasketController extends BasketController {
	

  /**
   * Adds the to market place cart.
   *
   * @param product the product
   * @param quantity the quantity
   * @param ussId the uss id
   * @return true, if successful
   * @throws CommerceCartModificationException the commerce cart modification exception
   */
  boolean addToMarketPlaceCart(TypedObject product, final long quantity, final String ussId) throws CommerceCartModificationException;
  

	/**
	 * Gets the seller details.
	 *
	 * @param productModel the product model
	 * @return the seller details
	 */
	List<String> getSellerDetails(ProductModel productModel);

	/**
	 * Gets the response for pin code.
	 *
	 * @param product the product
	 * @param pin the pin
	 * @param isDeliveryDateRequired the is delivery date required
	 * @return the response for pin code
	 * @throws EtailNonBusinessExceptions the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions the client etail non business exceptions
	 */
	List<PinCodeResponseData> getResponseForPinCode(final ProductModel product,
			final String pin, final String isDeliveryDateRequired, final String ussid) 
					throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions;


	
	/**
	 * Gets the cart data.
	 *
	 * @param cart the cart
	 * @return the cart data
	 */
	Map<String, String> getCartData(final CartModel cart);



	/**
	 * Save phone number to cart.
	 *
	 * @param cellPhoneNumber the cell phone number
	 */
	void savePhoneNumberToCart(final Long cellPhoneNumber);





	/**
	 * Check cart reservation status.
	 *
	 * @param cart the cart
	 * @return true, if successful
	 */
	boolean checkCartReservationStatus(final CartModel cart);


	/**
	 * Reserve cart.
	 *
	 * @param cart the cart
	 * @return the boolean
	 * @throws ValidationException 
	 */
	Boolean reserveCart(final CartModel cart) throws ValidationException;


	boolean checkCustomerStatus() throws ValidationException;


	boolean validateWithOMS(TypedObject cartEntry, TypedObject deliveryMode)
			throws ValidationException;
 
}
