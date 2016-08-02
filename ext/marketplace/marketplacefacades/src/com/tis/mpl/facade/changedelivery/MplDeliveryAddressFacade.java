/**
 *
 */
package com.tis.mpl.facade.changedelivery;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * @author Techouts
 *
 */
public interface MplDeliveryAddressFacade
{
	/**
	 * This method is used to Call OMS for changeDeliveryAddress Request If pincode is servisable it returns SUCCESS ,
	 * else it returns FAILED
	 *
	 * @param code
	 * @param newDeliveryAddress
	 * @return boolean
	 */
	public String changeDeliveryRequestCallToOMS(String orderId, AddressModel newDeliveryAddress);


	/**
	 * This method is used to create CRM Ticket for Change Delivery Address
	 *
	 * @author Techouts
	 * @param Order
	 * @param customerId
	 * @param source
	 * @return void
	 */
	public void createcrmTicketForChangeDeliveryAddress(OrderModel order, String customerId, String source);

	/**
	 * @ * @param orderid
	 * 
	 * @return
	 */
	public boolean isDeliveryAddressChangable(String orderid);

	/**
	 *
	 * @param customerId
	 * @param orderCode
	 * @param addressData
	 * @return String Status Msg Failure, success
	 */
	public boolean saveAsTemporaryAddressForCustomer(String orderCode, AddressData addressData);

	/**
	 *
	 * @param customerId
	 * @param enteredOTPNumber
	 * @return String msg Valid or not
	 */
	public String validateOTP(String customerId, String enteredOTPNumber, String orderCode);

	//Generate new OTP
	public boolean generateNewOTP(String orderCode);
	
	public String getPartialEncryptValue(String encryptSymbol,int encryptLength,String source);
}
