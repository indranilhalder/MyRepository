/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.StateModel;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.ReservationListWsDTO;


/**
 * @author TCS
 *
 */
public interface MplCommerceCartService
{
	/**
	 * @description: It is responsible for adding product to cart at ussid level
	 * @param paramCommerceCartParameter
	 * @return CommerceCartModification
	 * @throws CommerceCartModificationException
	 */

	public abstract CommerceCartModification addToCartWithUSSID(CommerceCartParameter paramCommerceCartParameter)
			throws CommerceCartModificationException;

	/**
	 * @description: It is responsible for fetching seller information
	 * @param cartData
	 * @param ussid
	 *
	 * @return Map<String, String>
	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getSellerInfo(CartData cartData, final String ussid) throws CMSItemNotFoundException;

	/**
	 * @description: It is responsible for fetching address
	 * @param addressData
	 *
	 * @return Map<String, String>
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
	 * @Desc Method for default pincode
	 * @param addressData
	 * @param defaultPinCodeId
	 * @return Collection<CartModel>
	 * @throws CMSItemNotFoundException
	 *
	 */
	String getDefaultPinCode(AddressData addressData, String defaultPinCodeId) throws CMSItemNotFoundException;

	/**
	 * @Description : GiftYourself two latest product from wishlists
	 * @param minGiftQuantity
	 * @param allWishlists
	 * @param pincode
	 * @return : List<Wishlist2EntryModel>
	 * @throws CMSItemNotFoundException
	 */
	/**
	 * @param minGiftQuantity
	 * @param allWishlists
	 * @param pincode
	 * @return
	 */
	List<Wishlist2EntryModel> getGiftYourselfDetails(int minGiftQuantity, final List<Wishlist2Model> allWishlists, String pincode)
			throws CMSItemNotFoundException;

	List<Wishlist2EntryModel> getGiftYourselfDetailsMobile(int minGiftQuantity, final List<Wishlist2Model> allWishlists,
			String pincode, Collection<CartModel> cartModelList) throws CMSItemNotFoundException;

	/**
	 * Method for fetching cart details without session
	 *
	 * @param ussid
	 * @return Collection<CartModel>
	 * @throws InvalidCartException
	 *
	 */
	Collection<CartModel> getCartDetails(String ussid) throws InvalidCartException;

	/**
	 * Method for creating empty cart if no cart is associated with user id
	 *
	 * User emailId , for anonymous user emailId : null
	 **
	 * @param emailId
	 * @param baseSiteId
	 *
	 * @return CartModel
	 * @throws CommerceCartModificationException
	 *            ,InvalidCartException
	 *
	 */
	CartModel createCart(final String emailId, final String baseSiteId)
			throws InvalidCartException, CommerceCartModificationException;

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

	boolean addItemToCart(String cartId, String productCode, long quantity, String ussid)
			throws InvalidCartException, CommerceCartModificationException;

	/**
	 * @description: It is responsible to find possible delivery mode
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData,
			final List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException;

	/**
	 * @Desc fetching oms pincode response data
	 * @param pincode
	 * @param cartData
	 * @return List<PinCodeResponseData>
	 * @throws EtailNonBusinessExceptions
	 */
	List<PinCodeResponseData> getOMSPincodeResponseData(final String pincode, final CartData cartData)
			throws EtailNonBusinessExceptions;

	/**
	 * @description this method checks the restriction list and calls pincode service accordingly
	 *
	 * @param pincode
	 * @param requestData
	 * @return List<PinCodeResponseData>
	 * @throws EtailNonBusinessExceptions
	 */
	List<PinCodeResponseData> getServiceablePinCodeCart(final String pincode, final List<PincodeServiceData> requestData)
			throws EtailNonBusinessExceptions;

	/**
	 * @description this method gets all the responses about servicable pincodes from OMS
	 *
	 * @param pin
	 * @param reqData
	 * @throws EtailNonBusinessExceptions
	 */
	List<PinCodeResponseData> getAllResponsesForPinCode(final String pin, final List<PincodeServiceData> reqData)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions;

	/*
	 * @Desc fetching reservation details
	 *
	 * @param cartId
	 *
	 * @param cartData
	 *
	 * @param pincode
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	ReservationListWsDTO getReservation(final String cartId, final CartData cartData, final String pincode, final String type)
			throws EtailNonBusinessExceptions;

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
	GetWishListWsDTO getTopTwoWishlistForUser(final UserModel userModel, final String pincode,
			final Collection<CartModel> cartModelList) throws CMSItemNotFoundException;

	/*
	 * @DESC TISST-6994,TISST-6990 adding to cart COD eligible or not with Pincode serviceabilty and sship product
	 *
	 * @param deliveryModeMap
	 *
	 * @param pincodeResponseData
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	boolean addCartCodEligible(final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeMap,
			final List<PinCodeResponseData> pincodeResponseData) throws EtailNonBusinessExceptions;

	public PriceData addConvCharge(final CartModel source, final CartData prototype);

	public PriceData setTotalWithConvCharge(final CartModel source, final CartData prototype);

	/**
	 * @Desc Update cart Entry
	 * @param parameters
	 * @return CommerceCartModification
	 * @throws CommerceCartModificationException
	 */
	public CommerceCartModification updateToShippingModeForCartEntry(CommerceCartParameter parameters)
			throws CommerceCartModificationException;

	/**
	 * @Desc Update cart Entry
	 * @param parameters
	 * @return CommerceCartModification
	 * @throws CommerceCartModificationException
	 */
	public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException;

	/*
	 * @Desc fetching state details for a state name
	 *
	 * @param stateName
	 *
	 * @return StateModel
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	StateModel fetchStateDetails(String stateName) throws EtailNonBusinessExceptions;

	/*
	 * @Desc to generate Sub order id
	 *
	 * @return String
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	String generateSubOrderId() throws EtailNonBusinessExceptions;

	/*
	 * @Desc to generate Order Line id and transaction id
	 *
	 * @return String
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	String generateOrderLineId() throws EtailNonBusinessExceptions;


	/*
	 * @Desc to generate Order Id
	 *
	 * @return String
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	String generateOrderId() throws EtailNonBusinessExceptions;

	/**
	 * @Desc Get delivery Date for Selected Delivery Mode
	 * @param cartData
	 *           ,omsDeliveryResponse
	 * @return null
	 * @throws CMSItemNotFoundException
	 * @throws ParseException
	 */
	public void setDeliveryDate(final CartData cartData, final List<PinCodeResponseData> omsDeliveryResponse)
			throws CMSItemNotFoundException, ParseException;



	/*
	 * @Desc used for inventory soft reservation from Commerce Checkout and Payment
	 *
	 * @param abstractOrderModel
	 *
	 * @param requestType
	 *
	 * @param defaultPinCodeId
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	boolean isInventoryReserved(AbstractOrderModel abstractOrderModel, String requestType, String defaultPinCodeId)
			throws EtailNonBusinessExceptions;

	/**
	 * @description: It is responsible to find possible delivery mode of a cart entry
	 * @param cartData
	 * @param entry
	 * @param omsDeliveryResponse
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData, final OrderEntryData entry,
			final List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException;

	/*
	 * @Desc : used to fetch delivery mode description details TISEE-950
	 *
	 * @param ussId
	 *
	 * @param deliveryMode
	 *
	 * @param startTime
	 *
	 * @param endTime
	 *
	 * @return String
	 */
	String getDeliveryModeDescription(final String ussId, final String deliveryMode, final String startTime, final String endTime);


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
	public abstract Map<String, List<String>> checkPincodeGiftCartData(String defaultPinCodeId,
			List<Wishlist2EntryModel> entryModels);

	/**
	 * Returns Seller Data
	 *
	 * @param ussid
	 * @return SellerInformationModel
	 */
	public abstract SellerInformationModel getSellerDetailsData(String ussid);

	/**
	 * @param cartModel
	 * @param freebieModelMap
	 * @param freebieParentQtyMap
	 * @throws EtailNonBusinessExceptions
	 */
	void saveDeliveryMethForFreebie(CartModel cartModel, Map<String, MplZoneDeliveryModeValueModel> freebieModelMap,
			Map<String, Long> freebieParentQtyMap) throws EtailNonBusinessExceptions;

	/**
	 * @param storeLocationRequestDataList
	 * @return
	 */
	public abstract List<StoreLocationResponseData> getStoreLocationsforCnC(
			List<StoreLocationRequestData> storeLocationRequestDataList);


}
