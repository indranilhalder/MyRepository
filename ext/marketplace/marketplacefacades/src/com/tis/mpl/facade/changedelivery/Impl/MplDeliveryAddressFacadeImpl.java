/**
 *@author Techouts
 *
 */
package com.tis.mpl.facade.changedelivery.Impl;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressDto;
import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressResponseDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.tis.mpl.facade.address.validator.MplAddressValidator;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.data.ReturnAddressInfo;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.RescheduleData;
import com.tisl.mpl.facades.data.RescheduleDataList;
import com.tisl.mpl.facades.data.ScheduledDeliveryData;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.service.TicketCreationCRMservice;


/**
 * @author prasad1
 *
 */
public class MplDeliveryAddressFacadeImpl implements MplDeliveryAddressFacade
{

	@Autowired
	private MplDeliveryAddressService mplDeliveryAddressService;
	@Autowired
	private TicketCreationCRMservice ticketCreationCRMservice;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private OTPGenericService otpGenericService;

	@Autowired
	private CustomOmsOrderService customOmsOrderService;
	@Resource(name = "tempAddressReverseConverter")
	private Converter<AddressData, TemproryAddressModel> tempAddressReverseConverter;

	@Autowired
	OrderModelDao orderModelDao;

	@Autowired
	MplAddressValidator mplAddressValidator;

	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressFacadeImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tis.mpl.facade.changedelivery.ChangeDeliveryAddressFacade#changeDeliveryRequestToOMS(java.lang.String,
	 * de.hybris.platform.core.model.user.AddressModel)
	 */

	@Override
	public String changeDeliveryRequestCallToOMS(String orderId, AddressModel newDeliveryAddress, String interfaceType)
	{
		LOG.info("Inside  changeDeliveryRequestCallToOMS Method");
		ChangeDeliveryAddressDto requestData = new ChangeDeliveryAddressDto();
		ChangeDeliveryAddressResponseDto omsResponse = new ChangeDeliveryAddressResponseDto();
		requestData = getChangeDeliveryRequestData(orderId, newDeliveryAddress, interfaceType);
		try
		{
			omsResponse = customOmsOrderService.changeDeliveryRequestCallToOMS(requestData);
		}
		catch (final Exception e)
		{
			LOG.error("Exception while calling OMS for  Change delivery , for order ID " + orderId + " " + e.getMessage());
		}
		if (null != omsResponse)
		{
			return omsResponse.getResponse();
		}
		else
		{
			LOG.error(" OMS responce is null for change change delivery address request ");
		}
		return null;
	}

	/**
	 * @param orderId
	 * @param newDeliveryAddress
	 * @return
	 */
	private ChangeDeliveryAddressDto getChangeDeliveryRequestData(String orderId,AddressModel newDeliveryAddress,
			 String interfaceType)
	{
		 ChangeDeliveryAddressDto requestData = new ChangeDeliveryAddressDto();
		if (null != orderId)
		{
			requestData.setOrderID(orderId);
		}
		if (null != newDeliveryAddress)
		{
			if (StringUtils.isNotEmpty(interfaceType))
			{
				if (interfaceType.equalsIgnoreCase(MarketplacecommerceservicesConstants.INTERFACE_TYPE_CA))
				{
					if (null != newDeliveryAddress.getEmail())
					{
						requestData.setEmailID(newDeliveryAddress.getEmail());
					}

					if (StringUtils.isNotEmpty(interfaceType))
					{
						requestData.setInterfaceType(interfaceType);
					}
					if (null != newDeliveryAddress.getLine2())
					{
						requestData.setAddress2(newDeliveryAddress.getLine2());
					}
					if (null != newDeliveryAddress.getAddressLine3())
					{
						requestData.setAddress3(newDeliveryAddress.getAddressLine3());
					}
					if (null != newDeliveryAddress.getLandmark())
					{
						requestData.setLandmark(newDeliveryAddress.getLandmark());
					}
					if (null != newDeliveryAddress.getCountry() && null != newDeliveryAddress.getCountry().getName())
					{
						requestData.setCountry(newDeliveryAddress.getCountry().getName());
					}
					if (null != newDeliveryAddress.getCity())
					{
						requestData.setCity(newDeliveryAddress.getCity());
					}
					if (null != newDeliveryAddress.getState())
					{
						requestData.setState(newDeliveryAddress.getState());
					}
					if (null != newDeliveryAddress.getPostalcode())
					{
						requestData.setPincode(newDeliveryAddress.getPostalcode());
					}
				}
				requestData.setInterfaceType(interfaceType);
			}
			
		
			if (null != newDeliveryAddress.getFirstname())
			{
				requestData.setFName(newDeliveryAddress.getFirstname());
			}
			if (null != newDeliveryAddress.getLastname())
			{
				requestData.setLName(newDeliveryAddress.getLastname());
			}
			if (null != newDeliveryAddress.getPhone1())
			{
				requestData.setPhoneNo(newDeliveryAddress.getPhone1());
			}
		}
		return requestData;
	}



	/**
	 * This method is used to create CRM Ticket for Change Delivery Address
	 *
	 * @author Techouts
	 * @param Order
	 * @param customerId
	 * @param source
	 * @return void
	 */
	@Override
	public void createcrmTicketForChangeDeliveryAddress(OrderModel Order, String costomerId,String source)
	{
		 List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
		 SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();
		if (null != Order.getChildOrders() && !Order.getChildOrders().isEmpty())
		{
			for (OrderModel sellerOrder : Order.getChildOrders())
			{
				if (null != sellerOrder.getEntries() && !sellerOrder.getEntries().isEmpty())
				{
					for (AbstractOrderEntryModel entry : sellerOrder.getEntries())
					{
						 SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();

						if (entry.getQuantity().longValue() >= 1
								&& !entry.getMplDeliveryMode().getDeliveryMode().getCode()
										.equalsIgnoreCase(MarketplaceFacadesConstants.CLICK_AND_COLLECT))
						{
							if (StringUtils.isNotEmpty(entry.getOrderLineId()))
							{
								sendTicketLineItemData.setLineItemId(entry.getTransactionID());

							}
						}
						lineItemDataList.add(sendTicketLineItemData);
					}
				}
			}
		}
		 ReturnAddressInfo addressinfo = new ReturnAddressInfo();
		 AddressModel changeDeliveryAddress = Order.getDeliveryAddress();
		if (null != changeDeliveryAddress.getFirstname())
		{
			addressinfo.setShippingFirstName(changeDeliveryAddress.getFirstname());
		}
		if (null != changeDeliveryAddress.getLastname())
		{
			addressinfo.setShippingLastName(changeDeliveryAddress.getLastname());
		}
		if (null != changeDeliveryAddress.getLine1())
		{
			addressinfo.setAddress1(changeDeliveryAddress.getLine1());
		}
		if (null != changeDeliveryAddress.getLine2())
		{
			addressinfo.setAddress2(changeDeliveryAddress.getLine2());
		}
		if (null != changeDeliveryAddress.getState())
		{
			addressinfo.setState(changeDeliveryAddress.getState());
		}
		if (null != changeDeliveryAddress.getCity())
		{
			addressinfo.setCity(changeDeliveryAddress.getCity());
		}
		if (null != changeDeliveryAddress.getLandmark())
		{
			addressinfo.setLandmark(changeDeliveryAddress.getLandmark());
		}
		if (null != changeDeliveryAddress.getCountry())
		{
			addressinfo.setCountry(changeDeliveryAddress.getCountry().getName());
		}
		if (null != changeDeliveryAddress.getPostalcode())
		{
			addressinfo.setPincode(changeDeliveryAddress.getPostalcode());
		}
		if (null != changeDeliveryAddress.getPhone1())
		{
			addressinfo.setPhoneNo(changeDeliveryAddress.getPhone1());
		}


		sendTicketRequestData.setAddressInfo(addressinfo);

		sendTicketRequestData.setCustomerID(Order.getUser().getUid());
		sendTicketRequestData.setLineItemDataList(lineItemDataList);
		sendTicketRequestData.setOrderId(Order.getCode());
		//sendTicketRequestData.setSubOrderId(orderModel.getChildOrders().get(0).getCode());
		sendTicketRequestData.setTicketType(MarketplacecommerceservicesConstants.TICKET_TYPE_CDA);
		sendTicketRequestData.setTicketSubType(MarketplacecommerceservicesConstants.TICKET_SUB_TYPE_CDA);
		sendTicketRequestData.setSource(source);
		try
		{
			ticketCreationCRMservice.ticketCreationModeltoWsDTO(sendTicketRequestData);
		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final ModelSavingException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final JAXBException e)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final Exception e)
		{
			LOG.info(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
	}


	@Override
	public boolean isDeliveryAddressChangable(String orderId) throws EtailNonBusinessExceptions
	{
		boolean changable = false;
		try
		{
			if (null != orderId)
			{
				changable = mplDeliveryAddressService.isDeliveryAddressChangable(orderId);
			}
			else
			{
				LOG.debug("Order ID is null ");
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw new EtailNonBusinessExceptions(e.getRootCause());
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred " + e.getMessage());
		}
		return changable;
	}


    @Override
	public ScheduledDeliveryData saveAsTemporaryAddressForCustomer(String orderCode,AddressData addressData)
	{
		 ScheduledDeliveryData scheduledDeliveryData = new ScheduledDeliveryData();
		try
		{
			if (addressData != null)
			{

				 TemproryAddressModel temproryAddressModel = tempAddressReverseConverter.convert(addressData);
				 OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
				 CustomerModel customer = (CustomerModel) orderModel.getUser();
				 temproryAddressModel.setEmail(customer.getOriginalUid());
				 String customerId = customer.getUid();
				 AddressModel deliveryAddressModel = orderModel.getDeliveryAddress();
				boolean isDifferentAddress = false;
				boolean isDiffrentContact = false;

				isDifferentAddress = mplAddressValidator.compareAddress(deliveryAddressModel, temproryAddressModel);
				isDiffrentContact = mplAddressValidator.compareContactDetails(deliveryAddressModel, temproryAddressModel);
				if (isDifferentAddress || isDiffrentContact)
				{
					mplDeliveryAddressService.setStatusForTemporaryAddress(orderCode, false);
					mplDeliveryAddressService.saveTemporaryAddress(orderModel, temproryAddressModel);
					String mobileNumber = null;
					if (!temproryAddressModel.getPostalcode().equalsIgnoreCase(deliveryAddressModel.getPostalcode()))
					{
						boolean isEligibleScheduledDelivery=checkScheduledDeliveryForOrder(orderModel);
						if (isEligibleScheduledDelivery)
						{

							List<TransactionEddDto> transactionEddDtoList;
							transactionEddDtoList = getScheduledDeliveryDate(orderModel, temproryAddressModel.getPostalcode());
							if (CollectionUtils.isNotEmpty(transactionEddDtoList))
							{
								Map<String, Object> scheduledDeliveryDate = null;
								scheduledDeliveryDate = getDeliveryDate(transactionEddDtoList);
								scheduledDeliveryData.setEntries(scheduledDeliveryDate);
								scheduledDeliveryData.setIsActive(Boolean.TRUE);
								scheduledDeliveryData.setIsPincodeServiceable(Boolean.TRUE);
							}
							else
							{
								scheduledDeliveryData.setIsPincodeServiceable(Boolean.FALSE);
							}

						}
						else
						{
							if (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()))
							{
								mobileNumber = orderModel.getDeliveryAddress().getPhone1();
							}
							else
							{
								mobileNumber = addressData.getPhone();
							}
							generateOTP(customerId, mobileNumber);
							scheduledDeliveryData.setIsActive(Boolean.FALSE);
						}
					}
					else
					{
						if (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()))
						{
							mobileNumber = orderModel.getDeliveryAddress().getPhone1();
						}
						else
						{
							mobileNumber = addressData.getPhone();
						}
						generateOTP(customerId, mobileNumber);
						scheduledDeliveryData.setIsActive(Boolean.FALSE);
					}

				}
				return null;
			}

		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final Exception exp)
		{
			exp.printStackTrace();
		}
		
		return scheduledDeliveryData;
	}


	/**
	 *
	 * @param customerId
	 * @param mobileNumber
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	String generateOTP(String customerId,String mobileNumber) throws InvalidKeyException, NoSuchAlgorithmException
	{
		 String otp = otpGenericService.generateOTP(customerId, OTPTypeEnum.COD.getCode(), mobileNumber);
		return otp;
	}



	/**
	 * First Check OTP If OTP Validate then Call To OMS
	 *
	 * @param customerID
	 * @param enteredOTPNumber
	 * @param orderCode
	 * @return Validation flag Value
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String validateOTP(String customerID,String enteredOTPNumber, String orderCode)
	{
		String valditionMsg = null;

		 OTPResponseData otpResponse = otpGenericService.validateOTP(customerID, null, enteredOTPNumber, OTPTypeEnum.COD,
				Long.parseLong(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP)));

		//If OTP Valid then call to OMS for Pincode ServiceableCheck
		if (otpResponse.getOTPValid().booleanValue())
		{
			 TemproryAddressModel temproryAddressModel = mplDeliveryAddressService.getTemporaryAddressModel(orderCode);
			 OrderModel orderModel = orderModelDao.getOrderModel(orderCode);

			LOG.debug("pincode serviceable Checking::MplChangeDeliveryAddressFacadeImpl");
			try
			{
				boolean isDiffrentAddress = false;
				isDiffrentAddress = mplAddressValidator.compareAddress(orderModel.getDeliveryAddress(), temproryAddressModel);
				if (isDiffrentAddress)
				{
					valditionMsg = changeDeliveryRequestCallToOMS(orderCode, temproryAddressModel, MarketplaceFacadesConstants.CA);
					if (valditionMsg != null)
					{
						if (valditionMsg.equalsIgnoreCase(MarketplaceFacadesConstants.SUCCESS))
						{
							boolean isAddressSaved = false;
							//if Serviceable Pincode then Save in Order and set
							LOG.debug("change delivery address:MplChangeDeliveryAddressFacadeImpl");
							isAddressSaved = mplDeliveryAddressService.saveDeliveryAddress(orderCode);
							valditionMsg = MarketplaceFacadesConstants.SUCCESS;
							try
							{
								if (isAddressSaved)
								{
									LOG.debug("Change Delivery Address updated into commerce then Call to CRM");
									createcrmTicketForChangeDeliveryAddress(orderModel, customerID,
											MarketplacecommerceservicesConstants.SOURCE);
								}
							}
							catch (final Exception e)
							{
								LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
							}
						}
						else
						{
							mplDeliveryAddressService.setStatusForTemporaryAddress(orderCode, false);
							valditionMsg =MarketplaceFacadesConstants.FAILURE;
						}
					}
					else
					{
						valditionMsg = MarketplaceFacadesConstants.SERVER_EXCEPTION;
					}

				}
				else
				{
					valditionMsg = changeDeliveryRequestCallToOMS(orderCode, temproryAddressModel, MarketplaceFacadesConstants.CU);
					if (valditionMsg != null)
					{
						boolean isContactUpdated = false;
						isContactUpdated = mplDeliveryAddressService.updateContactDetails(temproryAddressModel, orderModel);
						valditionMsg = MarketplaceFacadesConstants.SUCCESS;
						if (isContactUpdated)
						{
							createcrmTicketForChangeDeliveryAddress(orderModel, customerID, MarketplacecommerceservicesConstants.SOURCE);
						}
					}
					else
					{
						valditionMsg = MarketplaceFacadesConstants.SERVER_EXCEPTION;
					}
				}

			}
			catch (final Exception e)
			{
				LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());

			}
		}
		else
		{
			valditionMsg = otpResponse.getInvalidErrorMessage();
		}
		return valditionMsg;
	}

	@Override
	public boolean generateNewOTP(String orderCode)
	{
		boolean isGenerated = false;
		 OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
		 CustomerModel customer = (CustomerModel) orderModel.getUser();

		try
		{
			String mobileNumber = null;
			if (StringUtils.isNotEmpty(customer.getMobileNumber()))
			{
				mobileNumber = customer.getMobileNumber();
			}
			else if (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()))
			{
				mobileNumber = orderModel.getDeliveryAddress().getPhone1();
			}
			generateOTP(customer.getUid(), mobileNumber);
			isGenerated = true;
		}
		catch (final InvalidKeyException excption)
		{
			LOG.error(excption.getMessage());
		}
		catch (final NoSuchAlgorithmException excption)
		{
			LOG.error(excption.getMessage());
		}
		return isGenerated;
	}



	@Override
	public String getPartialEncryptValue(String encryptSymbol,int encryptLength,String source)
	{
		String result = "";
		if (StringUtils.isNotEmpty(source) && source.length() >= encryptLength)
		{
			 char charValue[] = source.toCharArray();
			for (int count = 0; count < charValue.length; count++)
			{
				if (count <= encryptLength)
				{
					result += encryptSymbol;
				}
				else
				{
					result += String.valueOf(charValue[count]);
				}

			}
		}
		else
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Unable to ecrypt the value[{}] : Reason :encrypt length value greater than of source value");
			}
		}
		return result;

	}


	public boolean checkScheduledDeliveryForOrder(OrderModel orderModel)
	{
		boolean isEligibleScheduledDelivery = false;
		 List<OrderModel> chaidOrderList = orderModel.getChildOrders();
		for (OrderModel subOrder : chaidOrderList)
		{
			for (AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
			{
				if (!abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
				{
					if (abstractOrderEntry.getEdScheduledDate() != null)
					{
						isEligibleScheduledDelivery = true;
						break;
					}
				}
			}
			if (isEligibleScheduledDelivery)
			{
				break;
			}
		}
		return isEligibleScheduledDelivery;
	}



	/**
	 *
	 * ReScheduleding DeliveryDate for Order return list
	 */

	@Override
	public void reScheduleddeliveryDate(RescheduleDataList reschList)
	{
		if (CollectionUtils.isNotEmpty(reschList.getRescheduleDataList()))
		{
			 List<TransactionSDDto> transactionEddDtoList = new ArrayList<TransactionSDDto>();
			 TransactionSDDto transactionEddDto = new TransactionSDDto();
			for (RescheduleData rescheduleData : reschList.getRescheduleDataList())
			{
				if (StringUtils.isNotEmpty(rescheduleData.getTransactionId()))
				{
					LOG.info("PopulateData" + rescheduleData.getTransactionId());
					transactionEddDto.setPickupDate(rescheduleData.getDate());
					transactionEddDto.setTransactionID(rescheduleData.getTransactionId());
					transactionEddDto.setTimeSlotTo(rescheduleData.getTime());
					transactionEddDtoList.add(transactionEddDto);
				}
			}

		}
	}

	/**
	 * OMS Request Sending For ScheduledDeliveryDate Argument- OrderModel return List of Transaction Id and date
	 */

	@Override
	public List<TransactionEddDto> getScheduledDeliveryDate(OrderModel orderModel,String newPincode)
	{
		List<TransactionEddDto> transactionEddDtosList = new ArrayList<TransactionEddDto>();
		try
		{
			ChangeDeliveryAddressResponseDto changeDeliveryAddressResponseDto = new ChangeDeliveryAddressResponseDto();

			changeDeliveryAddressResponseDto = scheduledDeliveryDateRequestToOMS(orderModel, newPincode);
			if (changeDeliveryAddressResponseDto.getResponse().equalsIgnoreCase(MarketplaceFacadesConstants.SUCCESS))
			{
				transactionEddDtosList = changeDeliveryAddressResponseDto.getTransactionEddDtos();
			}
			else
			{
				return null;
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Sending Oms request to OMS for scheduledDelivery " + exception.getMessage());
		}
		return transactionEddDtosList;
	}



	@Override
	public ChangeDeliveryAddressResponseDto scheduledDeliveryDateRequestToOMS(OrderModel orderModel, String newPincode)
	{
		ChangeDeliveryAddressResponseDto omsResponse = new ChangeDeliveryAddressResponseDto();
		try
		{
			LOG.info("get scheduled DeliveryDate to Oms" + orderModel.getCode() + newPincode);
			 ChangeDeliveryAddressDto requestData = new ChangeDeliveryAddressDto();
			 List<TransactionSDDto> transactionSDDtoList = setTransactionSDDto(orderModel);
			requestData.setInterfaceType(MarketplacecommerceservicesConstants.INTERFACE_TYPE_SD);
			if (StringUtils.isNotEmpty(orderModel.getCode()))
			{
				requestData.setOrderID(orderModel.getCode());
			}
			if (StringUtils.isNotEmpty(newPincode))
			{
				requestData.setPincode(newPincode);
			}
			if (CollectionUtils.isNotEmpty(transactionSDDtoList))
			{
				requestData.setTransactionSDDtos(transactionSDDtoList);
			}

			omsResponse = customOmsOrderService.changeDeliveryRequestCallToOMS(requestData);

		}
		catch (final Exception exception)
		{
			LOG.error("Sending Oms request to OMS for scheduledDelivery " + exception.getMessage());
		}
		return omsResponse;
	}



	/**
	 * Preparing eligible Transaction Data for scheduledDelivery
	 *
	 * @param orderModel
	 * @return List<TransactionSDDto>
	 */
	List<TransactionSDDto> setTransactionSDDto(OrderModel orderModel)
	{
		 List<TransactionSDDto> transactionSDDtoList = new ArrayList<TransactionSDDto>();
		 TransactionSDDto transactionSDDto = new TransactionSDDto();
		for (OrderModel subOrder : orderModel.getChildOrders())
		{
			for (AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
			{
				if (abstractOrderEntry.getDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
				{
					if (abstractOrderEntry.getEdScheduledDate() != null)
					{
						if (StringUtils.isNotEmpty(abstractOrderEntry.getTransactionID()))
						{
							transactionSDDto.setTransactionID(abstractOrderEntry.getTransactionID());
							transactionSDDtoList.add(transactionSDDto);
						}
					}
				}
			}
		}
		return transactionSDDtoList;
	}



	@Override
	public Map<String, Object> getDeliveryDate(List<TransactionEddDto> transactionEddDtoList)
	{
		 Map<String, Object> scheduledDeliveryDate = new HashMap<String, Object>();
		 SimpleDateFormat to = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		 SimpleDateFormat newFormat = new SimpleDateFormat("MMM-dd");
		 ArrayList<String> datesList = new ArrayList<String>();
		try
		{
			for (TransactionEddDto transactionEddDto : transactionEddDtoList)
			{
				if (StringUtils.isNotEmpty(transactionEddDto.getEDD()))
				{
					 Map<String, ArrayList<String>> scheduledDeliveryTime = new HashMap<String, ArrayList<String>>();
					 String stringDate = transactionEddDto.getEDD();
					 Date date = to.parse(stringDate);
					 String newdate = to.format(newFormat.parse(stringDate));
					datesList.add(newdate);
					DateTime dateTime = new DateTime(date);
					dateTime = dateTime.plusDays(1);
					datesList.add(dateTime.toString());
					dateTime = dateTime.plusDays(1);
					datesList.add(dateTime.toString());
					scheduledDeliveryDate.put(transactionEddDto.getTransactionID(), datesList);
				}
			}
		}
		catch (final ParseException parseException)
		{
			LOG.info("parseException raing converrting time" + parseException.getMessage());
		}
		return scheduledDeliveryDate;
	}




}