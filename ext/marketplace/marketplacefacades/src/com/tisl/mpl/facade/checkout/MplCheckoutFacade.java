/**
 *
 */
package com.tisl.mpl.facade.checkout;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade.ExpressCheckoutResult;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.AddressTypeData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;


/**
 * @author TCS
 *
 */
public interface MplCheckoutFacade extends CheckoutFacade
{
	/**
	 * @param deliveryModeCode
	 *
	 * @return orderData representing the order
	 * @throws EtailNonBusinessExceptions
	 *
	 */

	boolean setMplDeliveryMode(final String deliveryModeCode) throws EtailNonBusinessExceptions;

	/**
	 * @description: It is used to populate delivery address
	 * @param deliveryAddress
	 * @return List<AddressData>
	 * @throws EtailNonBusinessExceptions
	 */
	List<AddressData> rePopulateDeliveryAddress(List<AddressData> deliveryAddress) throws EtailNonBusinessExceptions;

	/**
	 * @description: It is used for populating delivery mode and cost for sellerartickeSKU
	 * @param currency
	 * @param sellerartickeSKU
	 *
	 * @return List<MplZoneDeliveryModeValueModel>
	 * @throws EtailNonBusinessExceptions
	 */

	List<MplZoneDeliveryModeValueModel> populateDeliveryModesCostForUSSID(final String currency, final String sellerartickeSKU)
			throws EtailNonBusinessExceptions;

	/*
	 * @description: It is used for populating delivery code and cost for sellerartickeSKU
	 *
	 * @param deliveryCode
	 *
	 * @param currencyIsoCode
	 *
	 * @param sellerArticleSKU
	 *
	 * @return MplZoneDeliveryModeValueModel
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	MplZoneDeliveryModeValueModel populateDeliveryCostForUSSIDAndDeliveryMode(final String deliveryCode,
			final String currencyIsoCode, final String sellerArticleSKU) throws EtailNonBusinessExceptions;

	/**
	 * @description method is called to get the type of Address
	 * @return Collection<AddressTypeData>
	 * @throws EtailNonBusinessExceptions
	 */
	Collection<AddressTypeData> getAddressType() throws EtailNonBusinessExceptions;

	/*
	 * @ Selected Address set for express checkout : TIS 391
	 *
	 * @param addressId
	 *
	 * @return ExpressCheckoutResult
	 *
	 * @throws EtailNonBusinessExceptions,Exception
	 */
	ExpressCheckoutResult performExpressCheckout(String addressId) throws EtailNonBusinessExceptions;

	/*
	 * @description Re calculating cart delivery cost: TIS 400
	 *
	 * @param addressId
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	boolean reCalculateCart(final CartData cartData) throws EtailNonBusinessExceptions;


	/*
	 * @description Storing delivery cost while navigating from Delivery mode to address selection : TIS 400
	 *
	 * @param finalDeliveryCost
	 *
	 * @param deliveryCostPromotionMap
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	boolean populateDeliveryCost(final Double finalDeliveryCost, Map<String, Map<String, Double>> deliveryCostPromotionMap,
			CartModel cartModel) throws EtailNonBusinessExceptions;

	/**
	 * @description: It is used for fetching order details for code
	 * @param code
	 * @return OrderData
	 * @throws EtailNonBusinessExceptions
	 */

	OrderData getOrderDetailsForCode(final String code) throws EtailNonBusinessExceptions;

	/*
	 * @ to check pincode inventory for Pay now TIS 414
	 *
	 * @param cartData
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	boolean isPincodeInventoryAvailable(final String defaultPinCodeId, final CartData cartData) throws EtailNonBusinessExceptions;

	/*
	 * @ to check promotion expired or not for Pay now : TIS 414
	 *
	 * @param abstractOrderModel
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	boolean isPromotionValid(final AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;

	/*
	 * @ Override TSHIP : TIS 397
	 *
	 * @param fullfillmentDataMap
	 *
	 * @param deliveryModeDataMap
	 *
	 * @param cartData
	 *
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	Map<String, List<MarketplaceDeliveryModeData>> repopulateTshipDeliveryCost(
			final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap, final CartData cartData)
			throws EtailNonBusinessExceptions;

	/**
	 * @description: It is used for fetching order details for code
	 * @param cartID
	 * @return OrderData
	 * @throws EtailNonBusinessExceptions
	 */
	//CAR-110
	//public OrderData placeOrderByCartId(final String cartID) throws EtailNonBusinessExceptions;
	public String placeOrderMobile(final CartModel cart) throws EtailNonBusinessExceptions;

	/**
	 * @description: It is used for converting date into ordinal date
	 * @param orderDetails
	 * @return string
	 * @throws EtailNonBusinessExceptions
	 */
	//String ordinalDate(String orderCode) throws EtailNonBusinessExceptions;//TISPT-175
	String ordinalDate(OrderData orderDetails) throws EtailNonBusinessExceptions;

	/**
	 * send mobile notifications sms and push
	 *
	 * @param orderDetails
	 */
	void sendMobileNotifications(final OrderData orderDetails);

	void triggerEmailAndSmsOnOrderConfirmation(OrderModel order, OrderData orderDetails, String trackorderurl);

	/*
	 * @desc use to save freebie delivery mode
	 *
	 * @param cartModel
	 *
	 * @param freebieModelMap
	 *
	 * @param freebieParentQtyMap
	 *
	 * @return void
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	void saveDeliveryMethForFreebie(AbstractOrderModel abstractOrderModel,
			Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, Map<String, Long> freebieParentQtyMap)
			throws EtailNonBusinessExceptions;

	public PriceData createPrice(final AbstractOrderModel source, final Double val);

	/*
	 * @ to check coupon expired or not for Pay now
	 *
	 * @param cartData
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	boolean isCouponValid(final AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions;

	/**
	 * TISPT-175
	 *
	 * @param orderModel
	 * @return OrderData
	 */
	OrderData getOrderDetailsForCode(OrderModel orderModel);

	/**
	 * @description: It is used for fetching order data by order code for non-logged in users
	 * @param orderCode
	 * @return OrderData
	 */
	OrderData getOrderDetailsForAnonymousUser(String orderCode);

	/**
	 * @param orderModel
	 * @throws InvalidCartException
	 * @throws CalculationException
	 */
	void beforeSubmitOrder(OrderModel orderModel) throws InvalidCartException, CalculationException;


	/**
	 * INC144314180 PRDI-25
	 *
	 * @param orderModel
	 * @throws InvalidCartException
	 * @throws CalculationException
	 */
	void beforeSubmitOrderMobile(OrderModel orderModel) throws InvalidCartException, CalculationException;

	/**
	 * @param orderModel
	 */
	void submitOrder(OrderModel orderModel);

	/**
	 * @param deliveryPOSMap
	 * @param cartModel
	 */
	void rePopulateDeliveryPointOfService(Map deliveryPOSMap, CartModel cartModel);

	public OrderData getOrderDetailsForCockpitUser(String orderCode, CustomerModel customerModel);


	public Map<String, List<String>> getDateAndTimeslotMapList(List<MplTimeSlotsModel> modelList, List<String> calculatedDateList,
			String deteWithOutTime, String timeWithOutDate, OrderEntryData cartEntryData, MplLPHolidaysModel mplLPHolidaysModel);


	public void constructDeliverySlotsForEDAndHD(InvReserForDeliverySlotsItemEDDInfoData deliverySlotsResponse,
			OrderEntryData cartEntryData, MplLPHolidaysModel mplLPHolidaysModel);

	/**
	 * UF-281/282:used for reseting values inserted for SD
	 *
	 * @param cartEntryList
	 */
	void resetSlotEntries(List<AbstractOrderEntryModel> cartEntryList);
}