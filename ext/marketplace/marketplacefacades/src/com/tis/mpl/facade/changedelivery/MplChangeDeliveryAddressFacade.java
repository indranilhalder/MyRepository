/**
 *
 */
package com.tis.mpl.facade.changedelivery;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * @author prasad1
 *
 */
public interface MplChangeDeliveryAddressFacade
{
	public boolean changeDeliveryRequestCallToOMS(String orderId, AddressModel newDeliveryAddress);

	public void createcrmTicketForChangeDeliveryAddress(OrderModel order, String customerId, String source);

	public boolean isDeliveryAddressChangable(OrderData orderData);

	/**
	 *
	 * @param customerId
	 * @param orderCode
	 * @param addressData
	 * @return String Status Msg Failure, success
	 */
	public boolean saveAsTemproryAddressForCustomer(String orderCode, AddressData addressData);

	/**
	 *
	 * @param customerId
	 * @param enteredOTPNumber
	 * @return String msg Valid or not
	 */
	public String validateOTP(String customerId, String enteredOTPNumber, String orderCode);

}
