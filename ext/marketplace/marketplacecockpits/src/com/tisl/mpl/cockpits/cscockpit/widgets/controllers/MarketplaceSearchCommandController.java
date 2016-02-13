package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.services.search.CsSearchCommand;
import de.hybris.platform.cscockpit.widgets.controllers.search.SearchCommandController;
import de.hybris.platform.europe1.model.PriceRowModel;

public interface MarketplaceSearchCommandController extends
		SearchCommandController<CsSearchCommand> {

	/**
	 * Sets the pin code.
	 *
	 * @param pinCode
	 *            the new pin code
	 */
	void setPinCode(Long pinCode);

	/**
	 * Gets the pin code.
	 *
	 * @return the pin code
	 */
	Long getPinCode();

	/**
	 * Gets the seller details.
	 *
	 * @param productModel
	 *            the product model
	 * @return the seller details
	 */
	List<String> getSellerDetails(ProductModel productModel);

	/**
	 * Gets the response for pin code.
	 *
	 * @param product
	 *            the product
	 * @param pin
	 *            the pin
	 * @param isDeliveryDateRequired
	 *            the is delivery date required
	 * @return the response for pin code
	 * @throws EtailNonBusinessExceptions
	 *             the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions
	 *             the client etail non business exceptions
	 */
	List<PinCodeResponseData> getResponseForPinCode(final ProductModel product,
			final String pin, final String isDeliveryDateRequired, final String ussid)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions;

	/**
	 * Sets the current site.
	 */
	void setCurrentSite();

	/**
	 * Gets the seller specific prices.
	 *
	 * @param product
	 *            the product
	 * @return the seller specific prices
	 */
	Map<String, PriceRowModel> getsellerSpecificPrices(
			final ProductModel product);

	/**
	 * Gets the seller specific buy box prices.
	 *
	 * @param product
	 *            the product
	 * @return the seller specific buy box prices
	 */
	List<BuyBoxModel> getsellerSpecificBuyBoxPrices(
			final ProductModel product);

	/**
	 * Adds the to market place cart.
	 *
	 * @param product
	 *            the product
	 * @param quantity
	 *            the quantity
	 * @return true, if successful
	 * @throws CommerceCartModificationException
	 *             the commerce cart modification exception
	 */
	boolean addToMarketPlaceCart(TypedObject product, final long quantity,
			final String ussId) throws CommerceCartModificationException;

	/**
	 * Dispatch event.
	 */
	void dispatchEvent();

	/**
	 * Gets the pricing flag.
	 *
	 * @return the pricing flag
	 */
	boolean getPricingFlag();

	/**
	 * Format product price.
	 *
	 * @param price the price
	 * @return the string
	 */
	String formatProductPrice(Double price);

}
