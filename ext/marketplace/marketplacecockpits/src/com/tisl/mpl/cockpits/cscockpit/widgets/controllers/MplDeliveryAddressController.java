package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

/**
 * @author Techouts
 * 
 */
import java.util.List;
import java.util.Map;

import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.tisl.mpl.facades.data.PincodeData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

public interface MplDeliveryAddressController {
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
	 * @param interfaceType 
	 * @return void
	 */
	public abstract void ticketCreateToCrm(OrderModel Order, String customerId,
			String source, String interfaceType);

	/**
	 * This method is used to Call OMS for changeDeliveryAddress Request
	 * 
	 * @author Techouts
	 * @param code
	 * @param newDeliveryAddress
	 * @param interfaceType 
	 * @return boolean
	 */
	public abstract String changeDeliveryAddressCallToOMS(String code,
			AddressModel newDeliveryAddress, String interfaceType,List<TransactionSDDto> transactionSDDtos);


	/**
	 * This method is used to save the delivery address and customer Addresses
	 * and delivery Addresses for an order
	 * 
	 * @author Techouts
	 * @param order
	 * @param address
	 * @return void
	 */
	public void saveDeliveryAddress(OrderModel orderModel,AddressModel address,boolean newAddresss);

	/**
	 * This method is used to get The PincodeData for a particular pincode
	 * 
	 * @author Techouts
	 * @param pincode
	 * @return PincodeData
	 */
	public PincodeData getPincodeData(String pincode);

	/**
	 * This method is used to check whether Scheduled delivery is possible or not 
	 * @param parentReference
	 * @return
	 */
	public abstract boolean checkScheduledDeliveryForOrder(
			OrderModel parentReference);

	
	public  void saveChangeDeliveryRequests(OrderModel orderModel);
	
	/**
	 * service used to get the 3 dates for HD and 2 dates for ED Considering MPL Holidays
	 * @param timeSlotType
	 * @param edd
	 * @return
	 */

	public abstract Map<String, List<String>> getDateAndTimeMap(
			String timeSlotType, String edd);

}
