/**
 *
 */
package com.tis.mpl.facade.changedelivery;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;

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
	public ScheduledDeliveryData scheduledDeliveryData(String orderCode, AddressData addressData);

	/**
	 *
	 * @param customerId
	 * @param enteredOTPNumber
	 * @return String msg Valid or not
	 */
	public String submitChangeDeliveryAddress(String customerID, String orderCode);

	//Generate new OTP
	public boolean newOTPRequest(String orderCode);

	public String getPartialEncryptValue(String encryptSymbol, int encryptLength, String source);

	public List<TransactionSDDto> reScheduleddeliveryDate(OrderModel orderModel,RescheduleDataList rescheduleDataListDto);

	public Map<String, Object> getDeliveryDate(List<TransactionEddDto> transactionEddDtoList, OrderModel orderModel);

	public List<TransactionEddDto> getScheduledDeliveryDate(OrderModel orderModel, String newPincode);

	public ChangeDeliveryAddressResponseDto scheduledDeliveryDateRequestToOMS(OrderModel orderModel, String newPincode);

	public Collection<MplDeliveryAddressReportData> getDeliveryAddressRepot(Date dateFrom, Date toDate);
	
	public OTPResponseData validteOTP(String customerID, String enteredOTPNumber);
    
    /**
	 * @param orderId
	 * @param status
	 */
	public void saveChangeDeliveryRequests(OrderModel orderModel);


/**
 * @param orderModel
 * @return
 */
public boolean checkScheduledDeliveryForOrder(OrderModel orderModel);

public boolean pincodeServiceableCheck(AddressData addressData, String orderCode);

public OrderData reScheduledDeliveryPageData(OrderData orderData);
	
}
