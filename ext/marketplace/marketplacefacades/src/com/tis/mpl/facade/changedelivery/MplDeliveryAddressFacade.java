/**
 *
 */
package com.tis.mpl.facade.changedelivery;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressResponseDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;
import com.tisl.mpl.facades.data.RescheduleDataList;
import com.tisl.mpl.facades.data.ScheduledDeliveryData;


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
	public String changeDeliveryRequestCallToOMS(String orderId, AddressModel newDeliveryAddress, String interfaceType,
			List<TransactionSDDto> transactionSDDtos);


	/**
	 * This method is used to create CRM Ticket for Change Delivery Address
	 *
	 * @author Techouts
	 * @param Order
	 * @param customerId
	 * @param source
	 */
	public void createcrmTicketForChangeDeliveryAddress(OrderModel order, String customerId, String source);

	/**
	 * @*@param orderid
	 *
	 * @return boolean
	 */
	public boolean isDeliveryAddressChangable(String orderid);

	/**
	 *
	 * @param orderCode
	 * @param addressData
	 * @return String Status Msg Failure, success
	 */
	public ScheduledDeliveryData saveAsTemporaryAddressForCustomer(String orderCode, AddressData addressData);

	/**
	 *
	 * @param customerId
	 * @param enteredOTPNumber
	 * @return String msg Valid or not
	 */
	public String submitChangeDeliveryAddress(String customerId, String enteredOTPNumber, String orderCode);

	//Generate new OTP
	public boolean generateNewOTP(String orderCode);

	public String getPartialEncryptValue(String encryptSymbol, int encryptLength, String source);

	List<TransactionSDDto> reScheduleddeliveryDate(OrderModel orderModel,RescheduleDataList rescheduleDataListDto);

	public Map<String, Object> getDeliveryDate(List<TransactionEddDto> transactionEddDtoList,OrderModel orderModel);

	public List<TransactionEddDto> getScheduledDeliveryDate(OrderModel orderModel, String newPincode);

	public ChangeDeliveryAddressResponseDto scheduledDeliveryDateRequestToOMS(OrderModel orderModel, String newPincode);

	public Collection<MplDeliveryAddressReportData> getDeliveryAddressRepot(String dateFrom, String toDate);


	/**
	 * @param orderModel
	 */
	public boolean checkScheduledDeliveryForOrder(OrderModel orderModel);
}
