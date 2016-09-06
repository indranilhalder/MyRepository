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
import de.hybris.platform.servicelayer.session.SessionService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressDto;
import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressResponseDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tis.mpl.facade.address.validator.MplAddressValidator;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.data.ReturnAddressInfo;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;
import com.tisl.mpl.facades.data.RescheduleData;
import com.tisl.mpl.facades.data.RescheduleDataList;
import com.tisl.mpl.facades.data.ScheduledDeliveryData;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.service.TicketCreationCRMservice;


/**
 * @author Techouts
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

	@Autowired
	SessionService sessionService;

	@Autowired
	MplConfigFacade mplConfigFacade;

	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressFacadeImpl.class);


	/**
	 * This method is used to Call OMS for changeDeliveryAddress Request If pincode is servisable it returns SUCCESS ,
	 * else it returns FAILED
	 *
	 * @author Techouts
	 * @param orderId
	 * @param newDeliveryAddress
	 * @param interfaceType
	 * @param transactionSDDtos
	 * @return boolean
	 */

	@Override
	public String changeDeliveryRequestCallToOMS(final String orderId, final AddressModel newDeliveryAddress,
			final String interfaceType, final List<TransactionSDDto> transactionSDDtos)
	{
		LOG.info("Inside  changeDeliveryRequestCallToOMS Method");
		ChangeDeliveryAddressDto requestData = new ChangeDeliveryAddressDto();
		ChangeDeliveryAddressResponseDto omsResponse = new ChangeDeliveryAddressResponseDto();

		requestData = getChangeDeliveryRequestData(orderId, newDeliveryAddress, interfaceType, transactionSDDtos);

		try
		{
			if (null != orderId && null != interfaceType)
			{
				omsResponse = customOmsOrderService.changeDeliveryRequestCallToOMS(requestData);

			}
			else
			{
				LOG.debug("in changeDeliveryRequestCallToOMS  Order Id   " + orderId + " and Interface type " + interfaceType);
				return null;
			}

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
	 * This method is used to get the change delivery Request data
	 *
	 * @author Techouts
	 * @param orderId
	 * @param newDeliveryAddress
	 * @param transactionSDDtos
	 * @param interfaceType
	 * @return ChangeDeliveryAddressDto
	 */
	private ChangeDeliveryAddressDto getChangeDeliveryRequestData(final String orderId, final AddressModel newDeliveryAddress,
			final String interfaceType, final List<TransactionSDDto> transactionSDDtos)
	{
		final ChangeDeliveryAddressDto requestData = new ChangeDeliveryAddressDto();
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
					if (null != newDeliveryAddress.getLine1())
					{
						requestData.setAddress1(newDeliveryAddress.getLine1());
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
					if (CollectionUtils.isNotEmpty(transactionSDDtos))
					{
						requestData.setTransactionSDDtos(transactionSDDtos);
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
	 * @param source
	 */
	@Override
	public void createcrmTicketForChangeDeliveryAddress(final OrderModel Order, final String costomerId, final String source)
	{
		final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
		final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();
		if (null != Order.getChildOrders() && !Order.getChildOrders().isEmpty())
		{
			for (final OrderModel sellerOrder : Order.getChildOrders())
			{
				if (null != sellerOrder.getEntries() && !sellerOrder.getEntries().isEmpty())
				{
					for (final AbstractOrderEntryModel entry : sellerOrder.getEntries())
					{
						final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();

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
		final ReturnAddressInfo addressinfo = new ReturnAddressInfo();
		final AddressModel changeDeliveryAddress = Order.getDeliveryAddress();
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


	/**
	 * This method is used to check whether change delivery is possible or not , If possible it returns true else it
	 * returns false
	 *
	 * @param orderId
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public boolean isDeliveryAddressChangable(final String orderId) throws EtailNonBusinessExceptions
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
	public ScheduledDeliveryData saveAsTemporaryAddressForCustomer(final String orderCode, final AddressData addressData)
	{
		final ScheduledDeliveryData scheduledDeliveryData = new ScheduledDeliveryData();
		try
		{
			final TemproryAddressModel temproryAddressModel = tempAddressReverseConverter.convert(addressData);
			final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
			final CustomerModel customer = (CustomerModel) orderModel.getUser();
			temproryAddressModel.setEmail(customer.getOriginalUid());
			final String customerId = customer.getUid();
			final AddressModel deliveryAddressModel = orderModel.getDeliveryAddress();

			final boolean isDifferentAddress = mplAddressValidator.compareAddress(deliveryAddressModel, temproryAddressModel);
			final boolean isDiffrentContact = mplAddressValidator.compareContactDetails(deliveryAddressModel, temproryAddressModel);

			if (isDifferentAddress || isDiffrentContact)
			{
				mplDeliveryAddressService.saveTemporaryAddress(orderModel, temproryAddressModel);
				String mobileNumber = null;
				if (!temproryAddressModel.getPostalcode().equalsIgnoreCase(deliveryAddressModel.getPostalcode()))
				{
					final boolean isEligibleScheduledDelivery = checkScheduledDeliveryForOrder(orderModel);
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
							mplDeliveryAddressService.setStatusForTemporaryAddress(orderCode, false);
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
			else
			{
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
	 * This method is used to generate the OTP
	 *
	 * @param customerId
	 * @param mobileNumber
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	String generateOTP(final String customerId, final String mobileNumber) throws InvalidKeyException, NoSuchAlgorithmException
	{
		final String otp = otpGenericService.generateOTP(customerId, OTPTypeEnum.CDA.getCode(), mobileNumber);
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
	public String submitChangeDeliveryAddress(final String customerID, final String enteredOTPNumber, final String orderCode)
	{
		String valditionMsg = null;

		final OTPResponseData otpResponse = otpGenericService.validateOTP(customerID, null, enteredOTPNumber, OTPTypeEnum.CDA,
				Long.parseLong(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP)));
		//If OTP Valid then call to OMS for Pincode ServiceableCheck
		if (otpResponse.getOTPValid().booleanValue())
		{
			final TemproryAddressModel temproryAddressModel = mplDeliveryAddressService.getTemporaryAddressModel(orderCode);
			final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);

			LOG.debug("pincode serviceable Checking::MplChangeDeliveryAddressFacadeImpl");
			try
			{
				boolean isDiffrentAddress = false;
				isDiffrentAddress = mplAddressValidator.compareAddress(orderModel.getDeliveryAddress(), temproryAddressModel);
				if (isDiffrentAddress)
				{
					final List<TransactionSDDto> transactionEddDtoList = sessionService.getAttribute("transactionEddDtoList");
					valditionMsg = changeDeliveryRequestCallToOMS(orderCode, temproryAddressModel, MarketplaceFacadesConstants.CA,
							transactionEddDtoList);
					if (valditionMsg != null)
					{
						if (valditionMsg.equalsIgnoreCase(MarketplaceFacadesConstants.SUCCESS))
						{
							//if Pincode Serviceable  then Save in Order and set
							LOG.debug("change delivery address:MplChangeDeliveryAddressFacadeImpl");
							final boolean isAddressSaved = mplDeliveryAddressService.saveDeliveryAddress(orderCode);
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
							valditionMsg = MarketplaceFacadesConstants.PINCODE_NOT_SERVICEABLE;
						}
					}
					else
					{
						valditionMsg = MarketplaceFacadesConstants.SERVER_EXCEPTION;
					}

				}
				else
				{
					valditionMsg = changeDeliveryRequestCallToOMS(orderCode, temproryAddressModel, MarketplaceFacadesConstants.CU,
							null);
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
	public boolean generateNewOTP(final String orderCode)
	{
		boolean isGenerated = false;
		final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
		final CustomerModel customer = (CustomerModel) orderModel.getUser();

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
	public String getPartialEncryptValue(final String encryptSymbol, final int encryptLength, final String source)
	{
		String result = "";
		if (StringUtils.isNotEmpty(source) && source.length() >= encryptLength)
		{
			final char charValue[] = source.toCharArray();
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


	/**
	 * This method is used to check whether order has Schedule delivery items or not
	 *
	 * @param orderModel
	 * @return boolean
	 */
	@Override
	public boolean checkScheduledDeliveryForOrder(final OrderModel orderModel)
	{
		boolean isEligibleScheduledDelivery = false;
		final List<OrderModel> chaidOrderList = orderModel.getChildOrders();
		for (final OrderModel subOrder : chaidOrderList)
		{
			for (final AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
			{
				if (!abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
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
	public void reScheduleddeliveryDate(final RescheduleDataList reschList)
	{
		if (CollectionUtils.isNotEmpty(reschList.getRescheduleDataList()))
		{
			final List<TransactionSDDto> transactionEddDtoList = new ArrayList<TransactionSDDto>();
			final TransactionSDDto transactionEddDto = new TransactionSDDto();
			String timeSlotTo;
			String timeSlotFrom;
			for (final RescheduleData rescheduleData : reschList.getRescheduleDataList())
			{
				if (StringUtils.isNotEmpty(rescheduleData.getTransactionId()))
				{
					LOG.info("PopulateData" + rescheduleData.getTransactionId());
					transactionEddDto.setPickupDate(rescheduleData.getDate());
					transactionEddDto.setTransactionID(rescheduleData.getTransactionId());
					timeSlotTo = rescheduleData.getTime().split("-")[0];
					timeSlotFrom = rescheduleData.getTime().split("-")[1];
					transactionEddDto.setTimeSlotTo(timeSlotTo);
					transactionEddDto.setTimeSlotFrom(timeSlotFrom);
					transactionEddDtoList.add(transactionEddDto);
				}
			}
			sessionService.setAttribute("transactionEddDtoList", transactionEddDtoList);

		}
	}

	/**
	 * OMS Request Sending For ScheduledDeliveryDate Argument- OrderModel return List of Transaction Id and date
	 */

	@Override
	public List<TransactionEddDto> getScheduledDeliveryDate(final OrderModel orderModel, final String newPincode)
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


	/**
	 * This method is used to call OMS for Schedule delivery dates
	 *
	 * @param orderModel
	 * @param newPincode
	 * @return ChangeDeliveryAddressResponseDto
	 */

	@Override
	public ChangeDeliveryAddressResponseDto scheduledDeliveryDateRequestToOMS(final OrderModel orderModel, final String newPincode)
	{
		ChangeDeliveryAddressResponseDto omsResponse = new ChangeDeliveryAddressResponseDto();
		try
		{
			LOG.info("get scheduled DeliveryDate to Oms" + orderModel.getCode() + newPincode);
			final ChangeDeliveryAddressDto requestData = new ChangeDeliveryAddressDto();
			final List<TransactionSDDto> transactionSDDtoList = setTransactionSDDto(orderModel);
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
	List<TransactionSDDto> setTransactionSDDto(final OrderModel orderModel)
	{
		final List<TransactionSDDto> transactionSDDtoList = new ArrayList<TransactionSDDto>();
		final TransactionSDDto transactionSDDto = new TransactionSDDto();
		for (final OrderModel subOrder : orderModel.getChildOrders())
		{
			for (final AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
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
	public Map<String, Object> getDeliveryDate(final List<TransactionEddDto> transactionEddDtoList)
	{
		final Map<String, Object> scheduledDeliveryDate = new HashMap<String, Object>();
		try
		{
			Map<String, List<String>> scheduledDeliveryTime = null;
			for (final TransactionEddDto transactionEddDto : transactionEddDtoList)
			{
				scheduledDeliveryTime = getDateAndTimeMap(transactionEddDto.getEDD());
				if (scheduledDeliveryTime != null && StringUtils.isNotEmpty(transactionEddDto.getEDD()))
				{
					scheduledDeliveryDate.put(transactionEddDto.getEDD(), scheduledDeliveryTime);
				}
			}
		}
		catch (final Exception parseException)
		{
			LOG.info("parseException raing converrting time" + parseException.getMessage());
		}
		return scheduledDeliveryDate;
	}



	/***
	 * Argument dateFrom and dateTo Changed Delivery Address Report Method return List<MplDeliveryAddressReportData>
	 */
	@Override
	public List<MplDeliveryAddressReportData> getDeliveryAddressRepot(final String dateFrom, final String dateTo)
	{
		final List<TemproryAddressModel> temproryAddressModelList = mplDeliveryAddressService.getTemporaryAddressModelList(
				dateFrom, dateTo);
		final Map<String, MplDeliveryAddressReportData> orderIDList = new HashMap<String, MplDeliveryAddressReportData>();

		if (CollectionUtils.isNotEmpty(temproryAddressModelList))
		{
			for (final TemproryAddressModel temproryAddressModel : temproryAddressModelList)
			{
				MplDeliveryAddressReportData mplDeliveryAddressReportData = null;
				if (orderIDList.containsKey(temproryAddressModel.getOrderId()))
				{
					mplDeliveryAddressReportData = orderIDList.get(temproryAddressModel.getOrderId());
				}
				else
				{
					mplDeliveryAddressReportData = new MplDeliveryAddressReportData();
					mplDeliveryAddressReportData.setOrderId(temproryAddressModel.getOrderId());

					orderIDList.put(temproryAddressModel.getOrderId(), mplDeliveryAddressReportData);
				}
				if (!temproryAddressModel.getIsProcessed().booleanValue())
				{
					mplDeliveryAddressReportData.setFailureRequsetCount(mplDeliveryAddressReportData.getFailureRequsetCount() + 1);
				}
				mplDeliveryAddressReportData.setTotalRequestCount(mplDeliveryAddressReportData.getTotalRequestCount() + 1);
			}
		}
		final List<MplDeliveryAddressReportData> list = new ArrayList<MplDeliveryAddressReportData>(orderIDList.values());
		return list;
	}

	/**
	 * 
	 * @param edd
	 * @return
	 * @throws java.text.ParseException
	 */
	private Map<String, List<String>> getDateAndTimeMap(final String edd) throws java.text.ParseException
	{

		final DateUtilHelper dateUtilHelper = new DateUtilHelper();
		final String estDeliveryDateAndTime = edd;
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		final String deteWithOutTIme = dateUtilHelper.getDateFromat(estDeliveryDateAndTime, format);
		final String timeWithOutDate = dateUtilHelper.getTimeFromat(estDeliveryDateAndTime);
		List<String> calculatedDateList = dateUtilHelper.getDeteList(deteWithOutTIme, format);
		List<MplTimeSlotsModel> modelList = null;
		modelList = mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);
		LOG.debug("********* Delivery Mode :" + MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);

		if (null != modelList)
		{
			Date startTime = null;
			Date endTIme = null;
			Date searchTime = null;
			final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			final List<MplTimeSlotsModel> timeList = new ArrayList<MplTimeSlotsModel>();
			for (final MplTimeSlotsModel mplTimeSlotsModel : modelList)
			{
				for (final String selectedDate : calculatedDateList)
				{
					if (selectedDate.equalsIgnoreCase(deteWithOutTIme))
					{
						try
						{
							startTime = sdf.parse(mplTimeSlotsModel.getToTime());
							endTIme = sdf.parse(mplTimeSlotsModel.getFromTime());
							searchTime = sdf.parse(timeWithOutDate);
						}
						catch (final Exception parseException)
						{
							LOG.info("parseException raing converrting time" + parseException.getMessage());
						}
						if (startTime.compareTo(searchTime) > 0 && endTIme.compareTo(searchTime) > 0
								&& startTime.compareTo(searchTime) != 0 && endTIme.compareTo(searchTime) != 0)
						{
							LOG.debug("startDate:" + DateFormatUtils.format(startTime, "HH:mm") + "endDate:"
									+ DateFormatUtils.format(sdf.parse(mplTimeSlotsModel.getFromTime()), "HH:mm"));
							timeList.add(mplTimeSlotsModel);
						}
					}
				}
			}
			LOG.debug("timeList.size()**************" + timeList.size());
			if (timeList.size() == 0)
			{
				final String nextDate = dateUtilHelper.getNextDete(deteWithOutTIme, format);
				calculatedDateList = dateUtilHelper.getDeteList(nextDate, format);
				timeList.addAll(modelList);
			}
			List<String> finalTimeSlotList = null;
			final Map<String, List<String>> dateTimeslotMapList = new LinkedHashMap<String, List<String>>();
			for (final String selectedDate : calculatedDateList)
			{

				if (selectedDate.equalsIgnoreCase(deteWithOutTIme))
				{
					finalTimeSlotList = dateUtilHelper.convertFromAndToTimeSlots(timeList);
				}
				else
				{
					finalTimeSlotList = dateUtilHelper.convertFromAndToTimeSlots(modelList);
				}
				dateTimeslotMapList.put(selectedDate, finalTimeSlotList);
			}
			return dateTimeslotMapList;
		}

		return null;
	}
}