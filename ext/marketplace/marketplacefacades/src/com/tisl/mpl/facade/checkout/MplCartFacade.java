/**
 *
 */
package com.tisl.mpl.facade.checkout;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.wsdto.GetWishListWsDTO;


/**
 * @author TCS
 *
 */
public interface MplCartFacade extends CartFacade
{

	/**
	 * Method for adding a product to cart.
	 *
	 * @param code
	 *           code of product to add
	 * @param quantity
	 *           the quantity of the product
	 * @return the cart modification data that includes a statusCode and the actual quantity added to the cart
	 * @throws CommerceCartModificationException
	 *            if the cart cannot be modified
	 */
	@Override
	CartModificationData addToCart(String code, long quantity, String ussid) throws CommerceCartModificationException;

	/*
	 * @Desc fetching seller info
	 * 

	 * @param cartData
	 * 

	 * @param ussid
	 * 

	 * @return Map<String, String>
	 * 

	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getSellerInfo(CartData cartData, final String ussid) throws CMSItemNotFoundException;

	/*
	 * @Desc fetching address
	 * 

	 * @param addressData
	 * 

	 * @return Map<String, String>
	 * 

	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getAddress(List<AddressData> addressData) throws CMSItemNotFoundException;

	/*
	 * @Desc fetching fulfilmentmode
	 * 

	 * @param cartData
	 * 

	 * @return Map<String, String>
	 * 

	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getFullfillmentMode(CartData cartData) throws CMSItemNotFoundException;


	/*
	 * @Desc fetching fulfilmentmode TISEE-6290
	 * 

	 * @param orderData
	 * 

	 * @return Map<String, String>
	 * 

	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getOrderEntryFullfillmentMode(OrderData orderData) throws CMSItemNotFoundException;

	/**
	 * Method for fetching default pincode details It will return pincode if exists in session else will fetch from
	 * Address
	 *
	 * @return default pin code
	 * @throws CMSItemNotFoundException
	 */

	String getDefaultPinCode(AddressData addressData, String defaultPinCodeId) throws CMSItemNotFoundException;

	/**
	 * @Description : GiftYourself two latest product from wishlists
	 * @return : List<Wishlist2EntryModel> finalgiftList
	 */

	List<Wishlist2EntryModel> getGiftYourselfDetails(int minGiftQuantity, List<Wishlist2Model> allWishlists, String pincode)
			throws CMSItemNotFoundException;


	/**
	 * Method for fetching cart details
	 *
	 * @param uid
	 *           User unique identification no
	 *
	 * @return Collections of Cart Model associated with user id
	 * @throws InvalidCartException
	 */
	Collection<CartModel> getCartDetails(String uid) throws InvalidCartException;

	/**
	 * Method for creating empty cart if no cart is associated with user id for Mobile service
	 *
	 * User emailId , for anonymous user emailId : null
	 **
	 * @param emailId
	 * @param baseSiteId
	 *
	 * @return CartModel
	 * @throws CommerceCartModificationException
	 */
	CartModel createCart(final String emailId, final String baseSiteId) throws InvalidCartException,
			CommerceCartModificationException;

	/**
	 * Method for adding item to cart for Mobile service
	 *
	 * @param cartId
	 *
	 * @param productCode
	 *
	 * @param quantity
	 *
	 * @param ussid
	 *
	 * @return boolean , for success return true else false
	 *
	 * @throws CommerceCartModificationException
	 *            if cartid , product code , quantity id null or empty if cart id , product id is invalid if quantity is
	 *            less than or equals to 0
	 */


	boolean addItemToCart(String cartId, String productCode, long quantity, String ussid) throws InvalidCartException,
			CommerceCartModificationException;

	/**
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 * @throws CMSItemNotFoundException
	 */
	Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData,
			final List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException;

	/*
	 * @Desc setting cart sub total
	 * 

	 * @throws EtailNonBusinessExceptions
	 */
	void setCartSubTotal() throws EtailNonBusinessExceptions;

	boolean setCartSubTotal2(CartModel model) throws EtailNonBusinessExceptions;

	/*
	 * @Desc fetching pincode response
	 * 

	 * @param pincode
	 * 

	 * @param cartData
	 * 

	 * @return List<PinCodeResponseData>
	 * 

	 * @throws EtailNonBusinessExceptions
	 */
	List<PinCodeResponseData> getOMSPincodeResponseData(String pincode, CartData cartData) throws EtailNonBusinessExceptions;


	/**
	 * @DESC Discount specific to the promotions TIS-386
	 * @return Map<String, String>
	 * @throws EtailNonBusinessExceptions
	 */
	Map<String, String> prepareForItemLevelDiscount() throws EtailNonBusinessExceptions;

	/*
	 * @DESC MobileWS105 : get top two wish list for mobile web service
	 * 

	 * @param userModel
	 * 

	 * @param pincode
	 * 

	 * @return GetWishListWsDTO
	 * 

	 * @throws EtailNonBusinessExceptions
	 */

	GetWishListWsDTO getTopTwoWishlistForUser(UserModel userModel, String pincode, Collection<CartModel> cartModelList)
			throws CMSItemNotFoundException;

	/*
	 * @DESC TISST-6994,TISST-6990 adding to cart COD eligible or not with Pincode serviceabilty and sship product
	 * 

	 * @param pincodeResponseData
	 * 

	 * @param deliveryModeMap
	 * 

	 * @return boolean
	 * 

	 * @throws EtailNonBusinessExceptions
	 */
	boolean addCartCodEligible(Map<String, List<MarketplaceDeliveryModeData>> deliveryModeMap,
			List<PinCodeResponseData> pincodeResponseData) throws EtailNonBusinessExceptions;

	/*
	 * @Desc checking max added quantity with store configuration
	 * 

	 * @param productCode
	 * 

	 * @param qty
	 * 

	 * @return String
	 * 

	 * @throws CommerceCartModificationException
	 */
	String isMaxQuantityAlreadyAdded(final String productCode, final long qty, final long stock, final String ussid)
			throws CommerceCartModificationException;

	/*
	 * @Desc used for inventory soft reservation from Commerce Checkout and Payment
	 * 

	 * @param requestType
	 * 

	 * @return boolean
	 * 

	 * @throws EtailNonBusinessExceptions
	 */
	boolean isInventoryReserved(String requestType) throws EtailNonBusinessExceptions;

	/**
	 * @param cart
	 * @return
	 */
	CartModel removeDeliveryMode(CartModel cart);

	boolean removeDeliveryMode2(CartModel cart);

	void setDeliveryDate(final CartData cartData, final List<PinCodeResponseData> omsDeliveryResponse)
			throws CMSItemNotFoundException, ParseException;

	/*
	 * @Desc used for In case pincode is changed in delivery address selection page then this method will be called to
	 * check pincode serviceabilty
	 * 

	 * @param selectedPincode
	 * 

	 * @return String
	 * 

	 * @throws EtailNonBusinessExceptions
	 */
	String checkPincodeAndInventory(final String selectedPincode) throws EtailNonBusinessExceptions, CMSItemNotFoundException;

	/*
	 * @Desc used to display quantity in cart page
	 * 

	 * @return ArrayList<Integer>
	 */
	ArrayList<Integer> getQuantityConfiguratioList() throws EtailNonBusinessExceptions;

	/**
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 * @throws CMSItemNotFoundException
	 */
	List<PinCodeResponseData> getOMSPincodeResponseData(final String pincode, final CartData cartData,
			final OrderEntryData entryData) throws EtailNonBusinessExceptions;

	/**
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 * @throws CMSItemNotFoundException
	 */
	Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData, OrderEntryData cartEntry,
			final List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException;

	/**
	 * @Desc : To validate if any of the product is delisted or not TISEE-3676
	 * @param cartModel
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	boolean isCartEntryDelisted(CartModel cartModel) throws CommerceCartModificationException, EtailNonBusinessExceptions;

	CartModificationData updateCartEntryMobile(final long entryNumber, final long quantity, final CartModel cartModel)
			throws CommerceCartModificationException;

	/*
	 * @Desc checking wishlist entry is valid or not , delisted , end date , online from TISEE-5185
	 * 

	 * @param wishlistEntryModel
	 * 

	 * @return boolean
	 * 

	 * @throws EtailNonBusinessExceptions
	 */
	boolean isWishlistEntryValid(final Wishlist2EntryModel wishlistEntryModel) throws EtailNonBusinessExceptions;

	/**
	 * The Method used to validate serviceability check for Gift Yourself Section in Cart
	 *
	 * @param defaultPinCodeId
	 * @param entryModels
	 * @return Map<String, List<String>>
	 */
	Map<String, List<String>> checkPincodeGiftCartData(String defaultPinCodeId, List<Wishlist2EntryModel> entryModels);

	/**
	 * @Desc : To validate if any of the product is delisted or not TISEE-3676
	 * @param cartModel
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	boolean isCartEntryDelistedMobile(final CartModel cartModel) throws CommerceCartModificationException,
			EtailNonBusinessExceptions;

	public CartModel getCalculatedCart() throws CommerceCartModificationException, EtailNonBusinessExceptions;

	OrderEntryData getCartEntryByUssid(final String ussid, CartData cart);

	/**
	 * @author TECH
	 * @param storeLocationRequestDataList
	 * @return
	 */
	List<StoreLocationResponseData> getStoreLocationsforCnC(List<StoreLocationRequestData> storeLocationRequestDataList);
}
