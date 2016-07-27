package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

/**
 * @author Techouts
 * 
 */
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.facades.data.PincodeData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

public interface MarketPlaceChangeDeliveryAddressController {
	/**
	 * this method is used to check whether delivery Address is changable or not
	 * if this method returns false ,then change delivery Address button is
	 * disabled
	 * 
	 * @author Techouts
	 * @param orderObject
	 * @return boolean
	 */
	public abstract boolean isDeliveryAddressChangable(TypedObject orderObject);

	/**
	 * This method is used to create CRM Ticket for Change Delivery Address
	 * @author Techouts
	 * @param Order
	 * @param customerId
	 * @param source
	 * @return void
	 */
	public abstract void ticketCreateToCrm(OrderModel Order, String customerId,
			String source);

	/**
	 * This method is used to Call OMS for changeDeliveryAddress Request
	 * 
	 * @author Techouts
	 * @param code
	 * @param newDeliveryAddress
	 * @return boolean
	 */
	public abstract String changeDeliveryAddressCallToOMS(String code,
			AddressModel newDeliveryAddress);

	/**
	 * This Method is used to Get the temprory Address
	 * 
	 * @author Techouts
	 * @param orderId
	 * @return TemproryAddressModel
	 */
	public TemproryAddressModel getTempororyAddress(String orderId);

	/**
	 * This method is used to save the delivery address and customer Addresses
	 * and delivery Addresses for an order
	 * 
	 * @author Techouts
	 * @param order
	 * @param address
	 * @return void
	 */
	public void saveDeliveryAddress(OrderModel order, AddressModel address);

	/**
	 * This method is used to get The PincodeData for a particular pincode
	 * 
	 * @author Techouts
	 * @param pincode
	 * @return PincodeData
	 */
	public PincodeData getPincodeData(String pincode);

}
