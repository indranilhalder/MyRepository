/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.promotions.util.Tuple2;
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
import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListRequestWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListResponse;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListResponse;
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
	 *
	 *
	 *            CommerceCartModification addToCartWithUSSID(CommerceCartParameter paramCommerceCartParameter) throws
	 *            CommerceCartModificationException;
	 */
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
	 * @param cartModel
	 * @return : List<Wishlist2EntryModel>
	 * @return : Tuple2<?, ?>
	 * @throws CMSItemNotFoundException
	 */
	//TISPT-179
	//List<Wishlist2EntryModel> getGiftYourselfDetails(int minGiftQuantity, final List<Wishlist2Model> allWishlists, String pincode,CartModel cartModel) throws CMSItemNotFoundException;

	Tuple2<?, ?> getGiftYourselfDetails(int minGiftQuantity, final List<Wishlist2Model> allWishlists, String pincode,
			CartData cartData) throws CMSItemNotFoundException;

	List<Wishlist2EntryModel> getGiftYourselfDetailsMobile(int minGiftQuantity, final List<Wishlist2Model> allWishlists,
			String pincode, CartData cartData) throws EtailNonBusinessExceptions;


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

	boolean addItemToCart(final String cartId, final CartModel cartModel, final ProductModel productModel, final long quantity,
			final String ussid) throws InvalidCartException, CommerceCartModificationException;


	/**
	 * @description: It is responsible to find possible delivery mode
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @param cartModel
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	// Changes for Duplicate Cart fix
	/*
	 * public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData, final
	 * List<PinCodeResponseData> omsDeliveryResponse, CartModel cartModel) throws CMSItemNotFoundException;
	 */

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
	//commented for CAR:127
	/*
	 * ReservationListWsDTO getReservation(final AbstractOrderModel cartModel, final String pincode, final String type)
	 * throws EtailNonBusinessExceptions;
	 */
	//	ReservationListWsDTO getReservation(final AbstractOrderData cartData, final String pincode, final String type,
	//			AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;

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
	GetWishListWsDTO getTopTwoWishlistForUser(final UserModel userModel, final String pincode, final CartData cartData)
			throws EtailNonBusinessExceptions;

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
	/*
	 * boolean addCartCodEligible(final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeMap, final
	 * List<PinCodeResponseData> pincodeResponseData, CartModel cartModel) throws EtailNonBusinessExceptions;
	 */

	boolean addCartCodEligible(final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeMap,
			final List<PinCodeResponseData> pincodeResponseData, CartModel cartModel, CartData cartData)
			throws EtailNonBusinessExceptions;


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
	//commented for CAR:127
	/*
	 * boolean isInventoryReserved(AbstractOrderModel abstractOrderModel, String requestType, String defaultPinCodeId)
	 * throws EtailNonBusinessExceptions;
	 */
	boolean isInventoryReserved(AbstractOrderData abstractOrderData, String requestType, String defaultPinCodeId,
			AbstractOrderModel abstractOrderModel, InventoryReservListRequestWsDTO inventoryRequest,
			SalesApplication salesApplication) throws EtailNonBusinessExceptions;

	/**
	 * @description: It is responsible to find possible delivery mode of a cart entry
	 * @param cartData
	 * @param entry
	 * @param omsDeliveryResponse
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData, final OrderEntryData entry,
			final List<PinCodeResponseData> omsDeliveryResponse, final CartModel cartModel) throws CMSItemNotFoundException;

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
			List<Wishlist2EntryModel> entryModels, final Tuple2<?, ?> wishListPincodeObject); //TISPT-179 Point 3

	/**
	 * Returns Seller Data
	 *
	 * @param ussid
	 * @return SellerInformationModel
	 */
	public abstract SellerInformationModel getSellerDetailsData(String ussid);

	/**
	 * @param abstractOrderModel
	 * @param freebieModelMap
	 * @param freebieParentQtyMap
	 * @throws EtailNonBusinessExceptions
	 */
	void saveDeliveryMethForFreebie(AbstractOrderModel abstractOrderModel,
			Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, Map<String, Long> freebieParentQtyMap)
			throws EtailNonBusinessExceptions;

	/**
	 * @Desc Used as part of oms fallback
	 * @param pincode
	 * @param pincodeServiceDataList
	 * @return PinCodeDeliveryModeListResponse
	 */
	PinCodeDeliveryModeListResponse callPincodeServiceabilityCommerce(final String pincode,
			final List<PincodeServiceData> pincodeServiceDataList);

	/*
	 * @param storeLocationRequestDataList
	 *
	 * @return
	 */
	List<StoreLocationResponseData> getStoreLocationsforCnC(List<StoreLocationRequestData> storeLocationRequestDataList,
			CartModel cartModel);

	/**
	 * @Desc Used as part of oms fallback for inventory soft reservation
	 * @param cartDataList
	 * @return InventoryReservListResponse
	 */
	InventoryReservListResponse callInventoryReservationCommerce(final List<CartSoftReservationData> cartDataList);

	/**
	 * This Method is used to get Valid Delivery Modes by Inventory
	 *
	 * @param pinCodeResponseData
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	/*
	 * public PinCodeResponseData getVlaidDeliveryModesByInventory(final PinCodeResponseData pinCodeResponseData) throws
	 * EtailNonBusinessExceptions;
	 */

	public PinCodeResponseData getVlaidDeliveryModesByInventory(final PinCodeResponseData pinCodeResponseData,
			final CartData cartData) throws EtailNonBusinessExceptions;

	/**
	 * @param source
	 * @param prototype
	 * @return PriceData
	 */
	PriceData setTotalWithConvCharge(OrderModel source, OrderData prototype);

	/**
	 * @param orderModel
	 */
	void recalculateOrder(OrderModel orderModel);

	/**
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(CartData cartData,
			List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException;

	/**
	 * This method was developed for CAR-256
	 *
	 * @param guid
	 * @param site
	 * @param user
	 * @return CartModel
	 * @throws InvalidCartException
	 */
	public CartModel fetchLatestCart(final BaseSiteModel site, final UserModel user) throws InvalidCartException;

	/**
	 * @param abstractOrderModel
	 * @param pincode
	 * @param requestType
	 * @param inventoryRequest
	 * @param salesApplication
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	ReservationListWsDTO getReservation(final AbstractOrderData cartData, final String pincode, final String type,
			AbstractOrderModel abstractOrderModel, InventoryReservListRequestWsDTO inventoryRequest,
			SalesApplication salesApplication) throws EtailNonBusinessExceptions;

	/**
	 * @param cartdata
	 * @return
	 */
	InvReserForDeliverySlotsResponseData convertDeliverySlotsDatatoWsdto(InvReserForDeliverySlotsRequestData cartdata);

	//TPR-5346 STARTS
	long checkMaxLimit(String code, CartModel cartModel);

	boolean checkMaxLimitUpdate(long entryNumber, long quantityToBeUpdated);

	boolean checkMaxLimitCheckout(CartModel serviceCart);
	//TPR-5346 ENDS

	//TPR-5666 samsung cart changes
	/**
	 * @param cartGuid
	 * @return
	 * @throws InvalidCartException
	 */
	public CartModel fetchCartUsingGuid(String cartGuid) throws InvalidCartException;


}
