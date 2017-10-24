/**
 *
 */
package com.tisl.mpl.facade.checkout;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListRequestWsDTO;
import com.tisl.mpl.wsdto.MaxLimitData;
import com.tisl.mpl.wsdto.MplEDDInfoWsDTO;
import com.tisl.mpl.wsdto.MplSelectedEDDForUssID;


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
	CartModificationData addToCart(String code, long quantity, String ussid) throws CommerceCartModificationException;

	/*
	 * @Desc fetching seller info
	 * 
	 * 
	 * @param cartData
	 * 
	 * 
	 * @param ussid
	 * 
	 * 
	 * @return Map<String, String>
	 * 
	 * 
	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getSellerInfo(CartData cartData, final String ussid) throws CMSItemNotFoundException;

	/*
	 * @Desc fetching address
	 * 
	 * 
	 * @param addressData
	 * 
	 * 
	 * @return Map<String, String>
	 * 
	 * 
	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getAddress(List<AddressData> addressData) throws CMSItemNotFoundException;

	/*
	 * @Desc fetching fulfilmentmode
	 * 
	 * 
	 * @param cartData
	 * 
	 * 
	 * @return Map<String, String>
	 * 
	 * 
	 * @throws CMSItemNotFoundException
	 */
	Map<String, String> getFullfillmentMode(CartData cartData) throws CMSItemNotFoundException;


	/*
	 * @Desc fetching fulfilmentmode TISEE-6290
	 * 
	 * 
	 * @param orderData
	 * 
	 * 
	 * @return Map<String, String>
	 * 
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
	//TISPT-179 Point 2
	//List<Wishlist2EntryModel> getGiftYourselfDetails(int minGiftQuantity, List<Wishlist2Model> allWishlists, String pincode, CartModel cartModel) throws CMSItemNotFoundException;
	//TISPT-179 Point 3
	Tuple2<?, ?> getGiftYourselfDetails(int minGiftQuantity, List<Wishlist2Model> allWishlists, String pincode, CartData cartData)
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


	boolean addItemToCart(String cartId, CartModel cartModel, ProductModel productModel, long quantity, String ussid)
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


	boolean addItemToCartwithExchange(String cartId, CartModel cartModel, ProductModel productModel, long quantity, String ussid,
			String exchangeParam) throws InvalidCartException, CommerceCartModificationException;



	/**
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @param cartModel
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 * @throws CMSItemNotFoundException
	 */
	// Changes for Duplicate Cart fix
	Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData,
			final List<PinCodeResponseData> omsDeliveryResponse, CartModel cartModel) throws CMSItemNotFoundException;

	/*
	 * @Desc setting cart sub total
	 * 
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	void setCartSubTotal(CartModel cartModel) throws EtailNonBusinessExceptions;

	boolean setCartSubTotal2(CartModel model) throws EtailNonBusinessExceptions;

	/*
	 * @Desc fetching pincode response
	 * 
	 * 
	 * @param pincode
	 * 
	 * 
	 * @param cartData
	 * 
	 * 
	 * @return List<PinCodeResponseData>
	 * 
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	List<PinCodeResponseData> getOMSPincodeResponseData(String pincode, CartData cartData, CartModel cartModel)
			throws EtailNonBusinessExceptions;


	/**
	 * @DESC Discount specific to the promotions TIS-386
	 * @return Map<String, String>
	 * @throws EtailNonBusinessExceptions
	 */
	Map<String, String> prepareForItemLevelDiscount() throws EtailNonBusinessExceptions;

	/*
	 * @DESC MobileWS105 : get top two wish list for mobile web service
	 * 
	 * 
	 * @param userModel
	 * 
	 * 
	 * @param pincode
	 * 
	 * 
	 * @return GetWishListWsDTO
	 * 
	 * 
	 * @throws EtailNonBusinessExceptions
	 */

	GetWishListWsDTO getTopTwoWishlistForUser(UserModel userModel, String pincode, CartData cartData)
			throws EtailNonBusinessExceptions;

	/*
	 * @DESC TISST-6994,TISST-6990 adding to cart COD eligible or not with Pincode serviceabilty and sship product
	 * 
	 * @param pincodeResponseData
	 * 
	 * @param deliveryModeMap
	 * 
	 * @param cartModel
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

	/*
	 * @Desc checking max added quantity with store configuration
	 * 
	 * 
	 * @param productCode
	 * 
	 * 
	 * @param qty
	 * 
	 * 
	 * @return String
	 * 
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
	//commented for CAR:127
	//boolean isInventoryReserved(String requestType, AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;
	boolean isInventoryReserved(String requestType, AbstractOrderData abstractOrderData, AbstractOrderModel abstractOrderModel)
			throws EtailNonBusinessExceptions;



	/*
	 * @Desc used for inventory soft reservation from Commerce Checkout and Payment For Mobile
	 * 
	 * @param requestType
	 * 
	 * @return boolean
	 * 
	 * @throws EtailNonBusinessExceptions
	 */


	public boolean isInventoryReservedMobile(final String requestType, final AbstractOrderModel abstractOrderModel,
			final String defaultPinCodeId, final InventoryReservListRequestWsDTO item, SalesApplication salesApplication)
			throws EtailNonBusinessExceptions;


	/**
	 * @param cart
	 * @return
	 */

	CartModel removeDeliveryMode(CartModel cart);

	boolean removeDeliveryMode2(final AbstractOrderModel cart);

	void setDeliveryDate(final CartData cartData, final List<PinCodeResponseData> omsDeliveryResponse)
			throws CMSItemNotFoundException, ParseException;

	/*
	 * @Desc used for In case pincode is changed in delivery address selection page then this method will be called to
	 * check pincode serviceabilty
	 * 
	 * 
	 * @param selectedPincode
	 * 
	 * 
	 * @return String
	 * 
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	String checkPincodeAndInventory(final String selectedPincode) throws EtailNonBusinessExceptions, CMSItemNotFoundException;

	/*
	 * @Desc used to display quantity in cart page
	 * 
	 * 
	 * @return ArrayList<Integer>
	 */
	ArrayList<Integer> getQuantityConfiguratioList() throws EtailNonBusinessExceptions;


	//added for jewellery

	ArrayList<Integer> getQuantityConfiguratioListforJewellery() throws EtailNonBusinessExceptions;


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
	 * 
	 * @param wishlistEntryModel
	 * 
	 * 
	 * @return boolean
	 * 
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
	//Map<String, List<String>> checkPincodeGiftCartData(String defaultPinCodeId, List<Wishlist2EntryModel> entryModels); TISPT-179 Point 3

	Map<String, List<String>> checkPincodeGiftCartData(String defaultPinCodeId, List<Wishlist2EntryModel> entryModels,
			Tuple2<?, ?> wishListPincodeObject);


	/**
	 * @Desc : To validate if any of the product is delisted or not TISEE-3676
	 * @param cartModel
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	boolean isCartEntryDelistedMobile(final CartModel cartModel) throws CommerceCartModificationException,
			EtailNonBusinessExceptions;


	CartModel getCalculatedCart(CartModel cart) throws CommerceCartModificationException, EtailNonBusinessExceptions;

	OrderEntryData getCartEntryByUssid(final String ussid, CartData cart);

	/**
	 * @author TECH
	 * @param storeLocationRequestDataList
	 * @return
	 */
	List<StoreLocationResponseData> getStoreLocationsforCnC(List<StoreLocationRequestData> storeLocationRequestDataList,
			final CartModel cartModel);

	/**
	 * This Method is used to get Valid Delivery Modes by Inventory
	 *
	 * @param pinCodeResponseData
	 * @return PinCodeResponseData
	 * @throws EtailNonBusinessExceptions
	 */

	/*
	 * PinCodeResponseData getVlaidDeliveryModesByInventory(PinCodeResponseData pinCodeResponseData) throws
	 * EtailNonBusinessExceptions;
	 */

	/**
	 * This Method is used to get Ordered Cart entry in Mobile
	 *
	 * @param recentlyAddedFirst
	 * @return CartData
	 * @throws EtailNonBusinessExceptions
	 */
	CartData getSessionCartWithEntryOrderingMobile(final CartModel cart, final boolean recentlyAddedFirst,
			final boolean isrecalculate, final boolean resetReqd) throws EtailNonBusinessExceptions;

	/* TPR-970 changes ,populate city and stae details in cart */
	public void populatePinCodeData(final CartModel cartmodel, final String pincode);

	/**
	 * TPR-774
	 *
	 * @doc To calculate discount percentage amount for display purpose
	 * @param cartModel
	 */
	public void totalMrpCal(final CartModel cartModel) throws EtailNonBusinessExceptions;

	/**
	 * @param orderModel
	 */
	void recalculateOrder(OrderModel orderModel);

	/**
	 * @Desc : To notify user about inventory reserve fail TPR-815
	 * @param orderModel
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	boolean notifyEmailAndSmsOnInventoryFail(final OrderModel orderModel) throws EtailNonBusinessExceptions;

	/**
	 * @param entryNumber
	 * @param storeId
	 * @param cartModel
	 * @return
	 * @throws CommerceCartModificationException
	 */
	CartModificationData updateCartEntryMobile(long entryNumber, String storeId, CartModel cartModel)
			throws CommerceCartModificationException;

	/**
	 * @return
	 */
	CartData getLuxCart() throws CommerceCartModificationException;

	/**
	 * @param cart
	 * @throws CommerceCartModificationException
	 */
	public CartModel getCalculatedCartMobile(CartModel cart) throws CommerceCartModificationException, EtailNonBusinessExceptions;

	/**
	 * @param pinCodeResponseData
	 * @param cartData
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */
	PinCodeResponseData getVlaidDeliveryModesByInventory(PinCodeResponseData pinCodeResponseData, CartData cartData);

	public CartData getCartDataFromCartModel(final CartModel cartModel, final boolean recentlyAddedFirst)
			throws EtailNonBusinessExceptions;

	/**
	 * get EDD INFormation for Ussid
	 *
	 * @param cartModel
	 * @param invReserForDeliverySlotsItemEDDInfoData
	 * @return MplEDDInfoWsDTO
	 * @throws ParseException
	 */
	public MplEDDInfoWsDTO getEDDInfo(CartModel cartModel,
			List<InvReserForDeliverySlotsItemEDDInfoData> invReserForDeliverySlotsItemEDDInfoData) throws ParseException;


	/**
	 * @param selectedPincode
	 * @param sessionPincode
	 * @return
	 * @throws EtailNonBusinessExceptions
	 * @throws CMSItemNotFoundException
	 */
	String singlePagecheckPincode(String selectedPincode, String sessionPincode) throws EtailNonBusinessExceptions,
			CMSItemNotFoundException;


	/*
	 * *
	 * 
	 * @param cartModel
	 * 
	 * @param mplSelectedEDDInfo
	 * 
	 * @return boolean
	 */
	public boolean addSelectedEDD(CartModel cartModel, List<MplSelectedEDDForUssID> mplSelectedEDDInfo);

	/**
	 * @param cartdata
	 * @return
	 */
	InvReserForDeliverySlotsResponseData convertDeliverySlotsDatatoWsdto(InvReserForDeliverySlotsRequestData cartdata,
			CartModel cart);


	/**
	 * For TPR-5666
	 *
	 * @param cartGuid
	 * @return
	 */
	CartModel getCartByGuid(String cartGuid);

	/**
	 * For TPR-5666
	 *
	 * @param cartModel
	 * @param sourceCartModel
	 * @return
	 */
	public void mergeCarts(CartModel sourceCartModel, CartModel targetCartModel);

	/**
	 * This method is used to check if any product in the cart is having pincode restricted promotion configured.
	 *
	 * @param cart
	 * @return boolean
	 */
	boolean checkPincodeRestrictedPromoOnCartProduct(CartModel cart);

	//TPR-5346 STARTS
	/**
	 * @param code
	 * @param qty
	 * @param stock
	 * @param ussid
	 * @return String
	 */
	public String isMaxProductQuantityAlreadyAdded(Integer maxCount, String code, long qty, long stock, String ussid,
			long checkMaxLimList);

	/**
	 * @param code
	 * @return long
	 */
	long checkMaxLimit(String code, CartModel cartModel);

	boolean checkMaxLimitUpdate(long entryNumber, long quantityToBeUpdated);

	boolean checkMaxLimitCheckout(CartModel serviceCart);

	/**
	 * @param cartModel
	 * @return boolean
	 */
	//boolean UpdateCartOnMaxLimExceeds(CartModel cartModel);

	Map<String, MaxLimitData> updateCartOnMaxLimExceeds(CartModel cartModel);

	//TPR-5346 ENDS

	/**
	 * TPR-1083 Add to Cart Exchange Method for adding a product to cart.
	 *
	 * @param code
	 *           code of product to add
	 * @param quantity
	 *           the quantity of the product
	 * @param ussid
	 *           the ussid to be added to cart
	 * @param exchangeParam
	 *           ]]]]]]] the exchange parameter for adding product with exchange to cart
	 * @return the cart modification data that includes a statusCode and the actual quantity added to the cart
	 * @throws CommerceCartModificationException
	 *            if the cart cannot be modified
	 */
	CartModificationData addToCartwithExchange(String code, long quantity, String ussid, String exchangeParam)
			throws CommerceCartModificationException;

	/**
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return
	 * @throws CMSItemNotFoundException
	 */
	Map<String, MarketplaceDeliveryModeData> getDeliveryModeMapForReviewOrder(CartData cartData,
			List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException;

	/**
	 * @param cartModel
	 * @throws EtailNonBusinessExceptions
	 */
	void setCartSubTotalForReviewOrder(CartModel cartModel) throws EtailNonBusinessExceptions;

	/**
	 * @param cart
	 * @return
	 */
	public CartModel removeDeliveryModeWdoutRecalclt(CartModel cart);

	/**
	 * @param cart
	 * @return
	 */
	public Map<String, Boolean> checkRestrictedPromoOnCartProduct(CartModel cart);

	/**
	 *
	 *
	 * @param cart
	 * @return boolean
	 */
	boolean validatePincodeRestrictedPromoOnCartProduct(CartModel cart);

	/**
	 * @param storeLocationRequestDataList
	 * @return
	 */
	public List<StoreLocationResponseData> getStoreLocationsforCnC(
			final List<StoreLocationRequestData> storeLocationRequestDataList, final String sellerUssId);

	public String populatePriceDisclaimerCart(final CartModel cartModel);
}
