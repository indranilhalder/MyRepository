/**
 *
 */
package com.tis.mpl.facade.changedelivery;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressResponseDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;
import com.tisl.mpl.facades.data.RescheduleDataList;
import com.tisl.mpl.facades.data.ScheduledDeliveryData;
import com.tisl.mpl.wsdto.MplSDInfoWsDTO;


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
	 * @param ticketType
	 */
	public void createcrmTicketForChangeDeliveryAddress(OrderModel order, String customerId, String source, String ticketType);

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
	public ScheduledDeliveryData getScheduledDeliveryData(String orderCode, AddressData addressData);

	/**
	 * after OTP validation address save in commerce and call to OMS and CRM
	 * 
	 * @return String msg Valid or not
	 */
	public String submitChangeDeliveryAddress(String customerID, String orderCode, AddressData addressData, boolean isMobile,
			List<TransactionSDDto> transactionSDDtoList);

	//Generate new OTP
	public boolean newOTPRequest(String orderCode);

	public String getPartialEncryptValue(String encryptSymbol, int encryptLength, String source);

	public List<TransactionSDDto> reScheduleddeliveryDate(OrderModel orderModel, RescheduleDataList rescheduleDataListDto);

	public Map<String, Object> getDeliveryDate(List<TransactionEddDto> transactionEddDtoList, OrderModel orderModel);

	public List<TransactionEddDto> getScheduledDeliveryDate(OrderModel orderModel, String newPincode);

	public ChangeDeliveryAddressResponseDto scheduledDeliveryDateRequestToOMS(OrderModel orderModel, String newPincode);

	public Collection<MplDeliveryAddressReportData> getDeliveryAddressRepot(Date dateFrom, Date toDate);

	public OTPResponseData validateOTP(String customerID, String enteredOTPNumber);

	public void saveChangeDeliveryRequests(OrderModel orderModel);


	public boolean checkScheduledDeliveryForOrder(OrderModel orderModel);

	public boolean pincodeServiceableCheck(AddressData addressData, String orderCode);

	public OrderData getReScheduledDeliveryPageData(OrderData orderData);

	/* added new Method for MobileAppData */
	public List<MplSDInfoWsDTO> getSDDatesMobile(Object deliverySDEDData);

	public void saveSelectedDateAndTime(OrderModel orderModel, List<TransactionSDDto> transactionSDDto);

	public void sendPushNotificationForCDA(CustomerModel customerModel, String otoNumber, String mobileNumber);

	public Map<String, List<String>> getDateAndTimeMap(String timeSlotType, String edd) throws ParseException;
	
	public boolean isEDOrder(OrderData orderData);

}