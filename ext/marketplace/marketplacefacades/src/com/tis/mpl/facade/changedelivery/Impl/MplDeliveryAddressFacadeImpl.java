/**
 *@author Techouts
 *
 */
package com.tis.mpl.facade.changedelivery.Impl;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressDto;
import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressResponseDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tis.mpl.facade.address.validator.MplDeliveryAddressComparator;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.MplDeliveryAddressInfoModel;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.data.ReturnAddressInfo;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;
import com.tisl.mpl.facades.data.RescheduleData;
import com.tisl.mpl.facades.data.RescheduleDataList;
import com.tisl.mpl.facades.data.ScheduledDeliveryData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.wsdto.MplEstimateDeliveryDateWsDTO;
import com.tisl.mpl.wsdto.MplSDInfoWsDTO;


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
	private Converter<AddressData, AddressModel> tempAddressReverseConverter;

	@Autowired
	OrderModelDao orderModelDao;

	@Autowired
	MplDeliveryAddressComparator mplDeliveryAddressComparator;

	@Autowired
	SessionService sessionService;

	@Autowired
	MplConfigFacade mplConfigFacade;

	@Autowired
	private MplOrderFacade mplOrderFacade;

	@Autowired
	private SendSMSFacade sendSMSFacade;
	
	@Autowired
	private AccountAddressFacade  accountAddressFacade;


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
					if (null != newDeliveryAddress.getLine1())
					{
						requestData.setAddress1(newDeliveryAddress.getLine1());
					}
					if (null != newDeliveryAddress.getLine2())
					{
						requestData.setAddress2(newDeliveryAddress.getLine2());
					}
					
					if (null != newDeliveryAddress.getStreetname())
					{
						requestData.setAddress1(newDeliveryAddress.getStreetname());
					}
					if (null != newDeliveryAddress.getStreetnumber())
					{
						requestData.setAddress2(newDeliveryAddress.getStreetnumber());
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
						requestData.setState(getStateCode(newDeliveryAddress.getState()));
					}
					if (null != newDeliveryAddress.getPostalcode())
					{
						requestData.setPincode(newDeliveryAddress.getPostalcode());
					}
					if (CollectionUtils.isNotEmpty(transactionSDDtos))
					{
						requestData.setTransactionSDDtos(getConvertedTimeSlot(transactionSDDtos));
					}

				}
				if (interfaceType.equalsIgnoreCase(MarketplaceFacadesConstants.CDP))
				{
					if (null != newDeliveryAddress.getPostalcode())
					{
						requestData.setPincode(newDeliveryAddress.getPostalcode());
					}
				}
				if (!interfaceType.equalsIgnoreCase(MarketplaceFacadesConstants.CDP))
				{
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
				requestData.setInterfaceType(interfaceType);
			}
		}
		return requestData;
	}

	// Added in R2.3 TISRLUAT-926
	 private List<TransactionSDDto> getConvertedTimeSlot(List<TransactionSDDto> transactionSDDtos)
	 {

	  SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	  SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	  String timeFromWithDate = null;
	  String timeToWithDate = null;


	  for (TransactionSDDto sdDto : transactionSDDtos)
	  {
	   timeFromWithDate = sdDto.getPickupDate().concat(" " + sdDto.getTimeSlotFrom());
	   timeToWithDate = sdDto.getPickupDate().concat(" " + sdDto.getTimeSlotTo());
	   try
	   {
	    sdDto.setTimeSlotFrom(String.valueOf(format2.format(format1.parse(timeFromWithDate))));
	    sdDto.setTimeSlotTo(String.valueOf(format2.format(format1.parse(timeToWithDate))));
	   }
	   catch (ParseException parseException)
	   {
	    LOG.error("Parse Exception MplDeliveryAddressFacadeImpl::::::" + parseException.getMessage());
	   }

	  }

	  return transactionSDDtos;
	 }


	/**
	 * This method is used to create CRM Ticket for Change Delivery Address
	 *
	 * @author Techouts
	 * @param Order
	 * @param source
	 */
	@Override
	public void createcrmTicketForChangeDeliveryAddress(final OrderModel Order, final String costomerId, final String source,
			final String ticketType)
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
		if(MarketplacecommerceservicesConstants.TICKET_SUB_TYPE_CDA.equalsIgnoreCase(ticketType)){
		if (null != changeDeliveryAddress.getLine1())
		{
			addressinfo.setAddress1(changeDeliveryAddress.getLine1());
		}
		if (null != changeDeliveryAddress.getLine2())
		{
			addressinfo.setAddress2(changeDeliveryAddress.getLine2());
		}
		//BUG ID 1623
		if (null != changeDeliveryAddress.getAddressLine3())
		{
			addressinfo.setAddress3(changeDeliveryAddress.getAddressLine3());
		}
		
		if (null != changeDeliveryAddress.getState())
		{
			addressinfo.setState(getStateCode(changeDeliveryAddress.getState()));
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
		sendTicketRequestData.setTicketSubType(ticketType);
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
			LOG.error("Exception occurred " + e.getMessage());
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred " + e.getMessage());
		}
		return changable;
	}


	/**
	 * Checking order are for Eligible Re ScheduledDelivery date if not Eligible then return null value
	 *
	 * @param orderCode
	 * @return ScheduledDeliveryData
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public ScheduledDeliveryData getScheduledDeliveryData(final String orderCode, final AddressData addressData)
	{
		final ScheduledDeliveryData scheduledDeliveryData = new ScheduledDeliveryData();
		try
		{
			final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
			
			final CustomerModel customer = (CustomerModel) orderModel.getUser();
			final AddressModel newDeliveryAddress = tempAddressReverseConverter.convert(addressData);
			if (newDeliveryAddress != null)
			{
				ServicesUtil.validateParameterNotNull(customer.getOriginalUid(), "OriginalUid must not be null");
				newDeliveryAddress.setEmail(customer.getOriginalUid());
				//check order Eligible for ReScheduledDelivery
				final boolean isEligibleScheduledDelivery = mplDeliveryAddressService.checkScheduledDeliveryForOrder(orderModel);
				if (isEligibleScheduledDelivery)
				{
					List<TransactionEddDto> transactionEddDtoList = new ArrayList<TransactionEddDto>();
					transactionEddDtoList = getScheduledDeliveryDate(orderModel, newDeliveryAddress.getPostalcode());
					if (CollectionUtils.isNotEmpty(transactionEddDtoList))
					{
						//setting changed delivery address in a session
						sessionService.setAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS, addressData);
						Map<String, Object> scheduledDeliveryDate = null;
						scheduledDeliveryDate = getDeliveryDate(transactionEddDtoList, orderModel);
						scheduledDeliveryData.setEntries(scheduledDeliveryDate);
						scheduledDeliveryData.setIsActive(Boolean.TRUE);
						scheduledDeliveryData.setIsPincodeServiceable(Boolean.TRUE);
					}
					else
					{
						mplDeliveryAddressService.deliveryAddressFailureRequest(orderModel);
						scheduledDeliveryData.setIsActive(Boolean.TRUE);
						scheduledDeliveryData.setIsPincodeServiceable(Boolean.FALSE);
						
					}

				}
				else
				{
					return null;
				}
			}
		}
		catch (final NullPointerException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, e);
		}
		catch (final Exception exp)
		{
			LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during whille checking ReScheduledDeliveryData "
					+ exp.getMessage());
		}

		return scheduledDeliveryData;
	}



	/**
	 * Customer changed pincode for delivery address Pincode Serviceable Checking for Order
	 *
	 *
	 */
	@Override
	public boolean pincodeServiceableCheck(final AddressData addressData, final String orderCode)
	{

		boolean isServiceable = false;
		try
		{
			// Pincode Serviceable Check for Order
			ServicesUtil.validateParameterNotNull(addressData.getPostalCode(), "Pincode must not be null");
			final AddressModel newDeliveryAddress = new AddressModel();
			newDeliveryAddress.setPostalcode(addressData.getPostalCode());

			final String pincodeServiceableCheck = changeDeliveryRequestCallToOMS(orderCode, newDeliveryAddress,
					MarketplaceFacadesConstants.CDP, null);
			if (StringUtils.isNotEmpty(pincodeServiceableCheck))
			{
				final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
				if (pincodeServiceableCheck.equalsIgnoreCase(MarketplaceFacadesConstants.SUCCESS))
				{
					final CustomerModel customer = (CustomerModel) orderModel.getUser();
					ServicesUtil.validateParameterNotNull(customer.getOriginalUid(), "OriginalUid must not be null");
					newDeliveryAddress.setEmail(customer.getOriginalUid());
					// set the Address in a session
					sessionService.setAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS, addressData);
					String mobileNumber=null;
		         if (orderModel.getDeliveryAddress() != null)
					{

						mobileNumber = StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()) ? orderModel.getDeliveryAddress()
								.getPhone1() : (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone2()) ? orderModel
								.getDeliveryAddress().getPhone2() : orderModel.getDeliveryAddress().getCellphone());
					}
					if (StringUtils.isNotEmpty(mobileNumber))
					{
						try
						{
							generateOTP(customer, mobileNumber);
						}
						catch (final InvalidKeyException excption)
						{
							LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during otp generate" + excption.getMessage());
						}
						catch (final NoSuchAlgorithmException excption)
						{
							LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during otp generate" + excption.getMessage());
						}
					}
					isServiceable = true;
				}
				else
				{
					mplDeliveryAddressService.deliveryAddressFailureRequest(orderModel);
				}
			}
		}
		catch (final Exception exp)
		{
			LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during whille checking pincode Serviceable" + exp.getMessage());
		}
		return isServiceable;
	}

	/**
	 * This method is used to generate the OTP
	 *
	 * @param mobileNumber
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	String generateOTP(final CustomerModel customerModel, final String mobileNumber) throws InvalidKeyException,
			NoSuchAlgorithmException
	{
		final String otp = otpGenericService.generateOTP(customerModel.getUid(), OTPTypeEnum.CDA.getCode(), mobileNumber);
		sendPushNotificationForCDA(customerModel, otp, mobileNumber);
		return otp;
	}





	/***
	 * Checking OTP Validation
	 *
	 * @return OTPResponseData
	 */
	@Override
	public OTPResponseData validateOTP(final String customerID, final String enteredOTPNumber)
	{
		final OTPResponseData otpResponse = otpGenericService.validateLatestOTP(customerID, null, enteredOTPNumber, OTPTypeEnum.CDA,
				Long.parseLong(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP)));
		return otpResponse;
	}


	/**
	 * Changed Address Save in data base and call to OMS And CRM
	 *
	 * @param customerID
	 * @param orderCode
	 * @return Validation flag Value
	 */
	@Override
	public String submitChangeDeliveryAddress(final String customerID, final String orderCode,
			final AddressData newDeliveryAddressData, final boolean isMobile, List<TransactionSDDto> transactionSDDtoList)
	{
		String valditionMsg = null;
		final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
		try
		{
			final AddressModel newDeliveryAddressModel = tempAddressReverseConverter.convert(newDeliveryAddressData);
			boolean isDiffrentAddress = false;
			if (newDeliveryAddressModel != null)
			{
				//Address Related filed is changed  then send to OMS  Request CA Type
				isDiffrentAddress = mplDeliveryAddressComparator.compareAddressModel(orderModel.getDeliveryAddress(),
						newDeliveryAddressModel);
				if (isDiffrentAddress)
				{
					try
					{
						
						 final boolean isEligibleScheduledDelivery = mplDeliveryAddressService.checkScheduledDeliveryForOrder(orderModel);
							//checking  order Eligible for ReScheduledDelivery
							if (isEligibleScheduledDelivery)
							{
								if (!isMobile)
								{
									final RescheduleDataList rescheduleDataList = sessionService
											.getAttribute(MarketplacecommerceservicesConstants.RESCHEDULE_DATA_SESSION_KEY);
									//Bug ID 686
									if (rescheduleDataList != null && CollectionUtils.isNotEmpty(rescheduleDataList.getRescheduleDataList()))
									{
										transactionSDDtoList = reScheduleddeliveryDate(orderModel, rescheduleDataList);
										sessionService.removeAttribute(MarketplacecommerceservicesConstants.RESCHEDULE_DATA_SESSION_KEY);
										
									}
								}
							}
							
						//OMS realTime Call
						valditionMsg=changeDeliveryRequestCallToOMS(orderCode, newDeliveryAddressModel, MarketplaceFacadesConstants.CA,
								transactionSDDtoList);
						if (MarketplaceFacadesConstants.SUCCESS.equalsIgnoreCase(valditionMsg))
						{
							 //save changed address  
							mplDeliveryAddressService.saveDeliveryAddress(newDeliveryAddressModel, orderModel,false);
							
							if (isEligibleScheduledDelivery && transactionSDDtoList!=null)
							{
								//save selected Date and time
								mplDeliveryAddressService.saveSelectedDateAndTime(orderModel, transactionSDDtoList);
							}
						  
							//CRM call
							createcrmTicketForChangeDeliveryAddress(orderModel, customerID, MarketplacecommerceservicesConstants.SOURCE,
									MarketplacecommerceservicesConstants.TICKET_SUB_TYPE_CDA);
						}
						else
						{
							return MarketplacecommerceservicesConstants.IS_NOT_CHANABLE;
						}
						
					}
					catch (final Exception e)
					{
						LOG.debug("MplDeliveryAddressFacadesImpl:::Excepton raising while OMS  and CRM Call  >>>>>" + e.getMessage());
					}
				}
				else
				{
					//customer changed only first name Last name and mobile number then OMS Call CU interface Type
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Updated Contact Related filed  into commerce  then Call to OMS And CRM with CU interface");
					}
					try
					{
						//OMS realTime Call
						valditionMsg=changeDeliveryRequestCallToOMS(orderCode, newDeliveryAddressModel, MarketplaceFacadesConstants.CU, null);
						
						if (MarketplaceFacadesConstants.SUCCESS.equalsIgnoreCase(valditionMsg))
						{
							try
							{
								boolean isDNC = mplDeliveryAddressComparator.compareNameDetails(orderModel.getDeliveryAddress(),
										newDeliveryAddressModel);
							   boolean isDMC = mplDeliveryAddressComparator.compareMobileNumber(orderModel.getDeliveryAddress(),
											newDeliveryAddressModel);
							    //save address 
							 	 mplDeliveryAddressService.saveDeliveryAddress(newDeliveryAddressModel, orderModel, false);
								//changed Name Details 
								if (isDNC)
								{
									createcrmTicketForChangeDeliveryAddress(orderModel, customerID,
											MarketplacecommerceservicesConstants.SOURCE,
											MarketplacecommerceservicesConstants.TICKET_SUB_TYPE_DNC);
								}
							//changed Mobile Details 
								if (isDMC)
								{
									//CRM Call
									createcrmTicketForChangeDeliveryAddress(orderModel, customerID,
											MarketplacecommerceservicesConstants.SOURCE,
											MarketplacecommerceservicesConstants.TICKET_SUB_TYPE_DMC);
								}
							}
							catch (final Exception e)
							{
								LOG.debug("MplDeliveryAddressFacadesImpl:::Excepton raising while OMS  and CRM Call  >>>>>"
										+ e.getMessage());
							}

							
							
						}
						else
						{
							return MarketplacecommerceservicesConstants.IS_NOT_CHANABLE;
						}
					}
					catch (final Exception e)
					{
						LOG.debug("MplDeliveryAddressFacadesImpl:::Excepton raising while OMS  and CRM Call  >>>>>" + e.getMessage());
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS + e.getMessage());
			return MarketplaceFacadesConstants.STATUS_FAILURE;
		}
		return valditionMsg;
	}

	/**
	 * new OTP Request For CDA Based On orderCode
	 */
	@Override
	public boolean newOTPRequest(final String orderCode)
	{
		boolean isGenerated = false;
		final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
		if (orderModel != null)
		{
			final CustomerModel customer = (CustomerModel) orderModel.getUser();
         
			String mobileNumber=null;
         if (orderModel.getDeliveryAddress() != null)
			{

				mobileNumber = StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()) ? orderModel.getDeliveryAddress()
						.getPhone1() : (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone2()) ? orderModel
						.getDeliveryAddress().getPhone2() : orderModel.getDeliveryAddress().getCellphone());
			}
			try
			{
				if (StringUtils.isNotEmpty(mobileNumber))
				{
					generateOTP(customer, mobileNumber);
				}
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
			if (MarketplaceFacadesConstants.SUCCESS.equalsIgnoreCase(changeDeliveryAddressResponseDto.getResponse()))
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
		TransactionSDDto transactionSDDto;
		for (final OrderModel subOrder : orderModel.getChildOrders())
		{
			for (final AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
			{
				if (!MarketplacecommerceservicesConstants.CLICK_COLLECT.equalsIgnoreCase(abstractOrderEntry.getMplDeliveryMode()
						.getDeliveryMode().getCode()))
				{
					if (abstractOrderEntry.getEdScheduledDate() != null)
					{
						if (StringUtils.isNotEmpty(abstractOrderEntry.getTransactionID()))
						{
							transactionSDDto = new TransactionSDDto();
							transactionSDDto.setTransactionID(abstractOrderEntry.getTransactionID());
							transactionSDDtoList.add(transactionSDDto);
						}
					}
				}
			}
		}
		return transactionSDDtoList;
	}



	/***
	 *
	 * Setting Delivery date and time Slots for product
	 *
	 * @return Map
	 */
	@Override
	public Map<String, Object> getDeliveryDate(final List<TransactionEddDto> transactionEddDtoList, final OrderModel orderModel)
	{
		final Map<String, Object> scheduledDeliveryDate = new HashMap<String, Object>();
		try
		{
			String timeSlotType = null;
			Map<String, List<String>> scheduledDeliveryTime = null;
			final Map<String, TransactionEddDto> mapTransactionEddDto = getEligibleEntry(orderModel, transactionEddDtoList);

			for (final Entry<String, TransactionEddDto> key : mapTransactionEddDto.entrySet())
			{
				final TransactionEddDto transactionEddDto = key.getValue();
			   timeSlotType = MarketplacecommerceservicesConstants.INTERFACE_TYPE_SD;
				ServicesUtil.validateParameterNotNull(timeSlotType, "timeSlotType must not be null");
				if (LOG.isDebugEnabled())
				{
					LOG.info("send timeSlotType and date  get List of Date and Time Slot for transactionId::::::::");
				}
				scheduledDeliveryTime = getDateAndTimeMap(timeSlotType, transactionEddDto.getEDD());

				if (scheduledDeliveryTime != null && StringUtils.isNotEmpty(transactionEddDto.getEDD()))
				{
					scheduledDeliveryDate.put(key.getKey(), scheduledDeliveryTime);
				}
			}
		}
		catch (final Exception parseException)
		{
			LOG.info("parseException raising converrting time" + parseException.getMessage());
		}
		return scheduledDeliveryDate;
	}




	/***
	 * Argument dateFrom and dateTo Changed Delivery Address Report Method return List<MplDeliveryAddressReportData>
	 */
	@Override
	public List<MplDeliveryAddressReportData> getDeliveryAddressRepot(final Date dateFrom, final Date dateTo)
	{

		final List<MplDeliveryAddressReportData> mplDeliveryAddressReportDataList = new ArrayList<MplDeliveryAddressReportData>();
		try
		{
			LOG.info("MplDeliveryAddressFacadeImpl:Change Delivery Address Request Purpose Preparing Report  ");
			ServicesUtil.validateParameterNotNull(dateFrom, "dateFrom must not be null");
			ServicesUtil.validateParameterNotNull(dateTo, "dateTo must not be null");

			final List<MplDeliveryAddressInfoModel> mplDeliveryAddressInfoModelList = mplDeliveryAddressService
					.getMplDeliveryAddressReportModels(dateFrom, dateTo);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("MplDeliveryAddressFacadeImpl:Preparing Data For BackOffice DeliveryAddressRepot");
			}
			if (CollectionUtils.isNotEmpty(mplDeliveryAddressInfoModelList))
			{
				for (final MplDeliveryAddressInfoModel mplDeliveryAddressInfoModel : mplDeliveryAddressInfoModelList)
				{
					final MplDeliveryAddressReportData mplDeliveryAddressReportData = new MplDeliveryAddressReportData();
					mplDeliveryAddressReportData.setOrderId(mplDeliveryAddressInfoModel.getOrderId());
					mplDeliveryAddressReportData.setTotalRequestCount(mplDeliveryAddressInfoModel.getChangeDeliveryTotalRequests()
							.intValue());
					mplDeliveryAddressReportData.setFailureRequsetCount(mplDeliveryAddressInfoModel.getChangeDeliveryRejectsCount()
							.intValue());
					mplDeliveryAddressReportDataList.add(mplDeliveryAddressReportData);
				}
			}
		}
		catch (final Exception exp)
		{
			LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during whille createing Report" + exp.getMessage());
			return null;
		}
		return mplDeliveryAddressReportDataList;
	}

	/**
	 * get Delivery Date And time slot
	 *
	 * @param edd
	 * @return Map
	 * @throws java.text.ParseException
	 */
	@Override
	public Map<String, List<String>> getDateAndTimeMap(final String timeSlotType, final String edd)
			throws java.text.ParseException {

		DateUtilHelper dateUtilHelper = new DateUtilHelper();
		String estDeliveryDateAndTime= edd;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String  deteWithOutTIme=dateUtilHelper.getDateFromat(estDeliveryDateAndTime,format);
		String timeWithOutDate=dateUtilHelper.getTimeFromat(estDeliveryDateAndTime);
		List<String>   calculatedDateList=new ArrayList<String>();
		List<MplTimeSlotsModel> modelList=null;
		
		if(timeSlotType.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD)){
			LOG.debug("Getting HD delivery time slots ");
			modelList=mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);
		}else if (timeSlotType.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERY_MODE_ED)){
			LOG.debug("Getting ED delivery time slots ");
			modelList=mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.DELIVERY_MODE_ED);
		}

		boolean slotsAvailableOnGivenDate = false;
		if(null != modelList) {
			for (MplTimeSlotsModel model : modelList) {
				Date endTime = null;
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				endTime=sdf.parse(model.getFromTime());
				Date givenTime = sdf.parse(timeWithOutDate);
				if(endTime.compareTo(givenTime) > 0) {
					slotsAvailableOnGivenDate = true;
					break;
				}
			}
		}
		if(!slotsAvailableOnGivenDate) {
			LOG.debug("Delivery slots not available for given Date"+edd);
			deteWithOutTIme = dateUtilHelper.getNextDete(deteWithOutTIme, format);
		}
		List<MplTimeSlotsModel> timeList=new ArrayList<MplTimeSlotsModel>();
		if(slotsAvailableOnGivenDate) {
			for (MplTimeSlotsModel model : modelList) {
				Date endTime = null;
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				endTime=sdf.parse(model.getFromTime());
				Date givenTime = sdf.parse(timeWithOutDate);
				if(endTime.compareTo(givenTime) > 0) {
					timeList.add(model);
				}
			}
		}
		if(null!= modelList){
			final MplLPHolidaysModel mplLPHolidaysModel = mplConfigFacade
					.getMplLPHolidays(MarketplacecommerceservicesConstants.CAMPAIGN_URL_ALL);
			if(timeSlotType.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD)) {
				if (mplLPHolidaysModel.getWorkingDays().contains("0"))
				{
					calculatedDateList = dateUtilHelper.calculatedLpHolidays(deteWithOutTIme, 3);
				}else {
					calculatedDateList=dateUtilHelper.getDeteList(deteWithOutTIme,format,3);
				}

			}else if(timeSlotType.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERY_MODE_ED)) {
				if (mplLPHolidaysModel.getWorkingDays().contains("0"))
				{
					calculatedDateList = dateUtilHelper.calculatedLpHolidays(deteWithOutTIme,2);
				}else {
					calculatedDateList=dateUtilHelper.getDeteList(deteWithOutTIme,format,2);
				}
			}
			List<String> finalTimeSlotList=null;
			Map<String, List<String>> dateTimeslotMapList=new LinkedHashMap<String, List<String>>();
			for(String selectedDate: calculatedDateList){
				if(selectedDate.equalsIgnoreCase(deteWithOutTIme) && slotsAvailableOnGivenDate){
					finalTimeSlotList= dateUtilHelper.convertFromAndToTimeSlots(timeList);
				}else{
					finalTimeSlotList= dateUtilHelper.convertFromAndToTimeSlots(modelList);
				}
				dateTimeslotMapList.put(selectedDate, finalTimeSlotList);
			}
			return dateTimeslotMapList;
		}
		return null;
	}


	/***
	 * Preparing product EligibleEntry for reScheduleddeliveryDate which product are Eligible for reScheduleddeliveryDate
	 *
	 * @param orderModel
	 * @param transactionEddDtoList
	 * @return Map
	 * @throws ParseException
	 */
	public Map<String, TransactionEddDto> getEligibleEntry(final OrderModel orderModel,
			final List<TransactionEddDto> transactionEddDtoList) throws ParseException
	{
		final Map<String, TransactionEddDto> mapTransactionEdd = new HashMap<String, TransactionEddDto>();
		for (final OrderModel subOrder : orderModel.getChildOrders())
		{
			for (final AbstractOrderEntryModel abstractOrderEntryModel : subOrder.getEntries())
			{
				if (!abstractOrderEntryModel.getMplDeliveryMode().getDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
				{
					if (abstractOrderEntryModel.getEdScheduledDate() != null)
					{
						for (final TransactionEddDto transactionEddDto : transactionEddDtoList)
						{
							if (transactionEddDto.getTransactionID().equals(abstractOrderEntryModel.getTransactionID()))
							{
								TransactionEddDto rescheduleData = null;
								if (mapTransactionEdd.containsKey(abstractOrderEntryModel.getProduct().getCode()))
								{
									rescheduleData = mapTransactionEdd.get(abstractOrderEntryModel.getProduct().getCode());
									final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
									final Date oldDate = formatter.parse(rescheduleData.getEDD());
									final Date newDate = formatter.parse(transactionEddDto.getEDD());
									if (oldDate.before(newDate))
									{
										mapTransactionEdd.replace(abstractOrderEntryModel.getProduct().getCode(), transactionEddDto);
									}
								}
								else
								{
									mapTransactionEdd.put(abstractOrderEntryModel.getProduct().getCode(), transactionEddDto);
								}

							}
						}
					}

				}
			}
		}
		return mapTransactionEdd;
	}

	/***
	 * Preparing TransactionSDDto data related to customer selected date and time
	 *
	 */
	@Override
	public List<TransactionSDDto> reScheduleddeliveryDate(final OrderModel orderModel,
			final RescheduleDataList rescheduleDataListDto)
	{
		SimpleDateFormat format1 = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
		String timeSlotFrom = null;
		String timeSlotTo = null;
		final List<TransactionSDDto> transactionSDDtoList = new ArrayList<TransactionSDDto>();
		final List<RescheduleData> rescheduleDataList = rescheduleDataListDto.getRescheduleDataList();
		for (final OrderModel subModel : orderModel.getChildOrders())
		{
			for (final AbstractOrderEntryModel abstractOrderEntryModel : subModel.getEntries())
			{
				for (final RescheduleData rescheduleData : rescheduleDataList)
				{
					if (StringUtils.isNotEmpty(rescheduleData.getProductCode()))
					{
						if (abstractOrderEntryModel.getProduct().getCode().equalsIgnoreCase(rescheduleData.getProductCode()))
						{
							final TransactionSDDto transactionSDDto = new TransactionSDDto();
							transactionSDDto.setTransactionID(abstractOrderEntryModel.getTransactionID());
							transactionSDDto.setPickupDate(rescheduleData.getDate());
							final String timeFromTo = rescheduleData.getTime();
							if (StringUtils.isNotEmpty(timeFromTo))
							{
								timeSlotFrom = timeFromTo.substring(0, timeFromTo.indexOf("TO") - 1);
								timeSlotTo = timeFromTo.substring(timeFromTo.indexOf("TO") + 3, timeFromTo.length());
								try
								{
								transactionSDDto.setTimeSlotFrom(String.valueOf(format2.format(format1.parse(timeSlotFrom))));
								transactionSDDto.setTimeSlotTo(String.valueOf(format2.format(format1.parse(timeSlotTo))));
								abstractOrderEntryModel.setEdScheduledDate(rescheduleData.getDate());
								}
								catch (ParseException e)
								{
									LOG.error("unable to parse timeslots "+ e.getMessage());
								}
							}
							transactionSDDtoList.add(transactionSDDto);
							break;
						}
					}
				}
			}
		}
		return transactionSDDtoList;
	}


	@Override
	public void saveChangeDeliveryRequests(final OrderModel orderModel)
	{
		mplDeliveryAddressService.deliveryAddressFailureRequest(orderModel);

	}



	/***
	 * his method is used to check whether order has Schedule delivery items or not
	 *
	 */
	@Override
	public boolean checkScheduledDeliveryForOrder(final OrderModel orderModel)
	{
		return mplDeliveryAddressService.checkScheduledDeliveryForOrder(orderModel);
	}


	/***
	 * Preparing Re Scheduled Delivery Page Data
	 *
	 * @return OrderData
	 */
	@Override
	public OrderData getReScheduledDeliveryPageData(final OrderData orderData)
	{

		for (final OrderEntryData orderEntry : orderData.getEntries())
		{
			//getting the product code
			final ProductModel productModel = getMplOrderFacade().getProductForCode(orderEntry.getProduct().getCode());
			if (CollectionUtils.isNotEmpty(productModel.getBrands()))
			{
				for (final BrandModel brand : productModel.getBrands())
				{
					orderEntry.setBrandName(brand.getName());
					break;
				}
			}
			final List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
					.getSellerInformationRelator();
			for (final SellerInformationModel sellerInformationModel : sellerInfo)
			{
				if (sellerInformationModel.getSellerArticleSKU().equals(orderEntry.getSelectedUssid()))
				{
					final SellerInformationData sellerInfoData = new SellerInformationData();
					sellerInfoData.setSellername(sellerInformationModel.getSellerName());
					sellerInfoData.setUssid(sellerInformationModel.getSellerArticleSKU());
					sellerInfoData.setSellerID(sellerInformationModel.getSellerID());
					orderEntry.setSelectedSellerInformation(sellerInfoData);
				}
			}
		}
		return orderData;

	}



	/**
	 * Preparing EstimateDeliveryDate data for Mobile
	 *
	 */
	@Override
	public List<MplSDInfoWsDTO> getSDDatesMobile(final Object dataSDDates)
	{
		//ProductData
		final List<MplSDInfoWsDTO> mplSDInfoWsDTOList = new ArrayList<MplSDInfoWsDTO>();
		MplSDInfoWsDTO mplSDInfoWsDTO;

		//DeliveyDate
		List<MplEstimateDeliveryDateWsDTO> mplEstimateDateList;
		MplEstimateDeliveryDateWsDTO mplEstimateDeliveryDateWsDTO;

		//DeliveryTimeSlot
		List<String> mplDeliveryTimeSlotList;

		final Map<String, Object> productData = (Map<String, Object>) dataSDDates;

		for (final Entry<String, Object> estimateDates : productData.entrySet())
		{
			// new instance create for each product
			mplSDInfoWsDTO = new MplSDInfoWsDTO();
			mplEstimateDateList = new ArrayList<MplEstimateDeliveryDateWsDTO>();

			//setting product code value
			mplSDInfoWsDTO.setProductCode(estimateDates.getKey());
			final Map<String, List<String>> estimateDateData = (Map<String, List<String>>) estimateDates.getValue();
			for (final Entry<String, List<String>> dateData : estimateDateData.entrySet())
			{
				// new instance create for each product
				mplEstimateDeliveryDateWsDTO = new MplEstimateDeliveryDateWsDTO();

				//Setting Delivery date
				mplEstimateDeliveryDateWsDTO.setDeliveryDate(dateData.getKey());

				// new instance create  for each date
				mplDeliveryTimeSlotList = new ArrayList<String>();
				for (final String timeSlot : dateData.getValue())
				{
					// new instance create for each Time
					final String timeSlotValue = timeSlot;
					mplDeliveryTimeSlotList.add(timeSlotValue);

				}
				//setting value TimeSlots
				mplEstimateDeliveryDateWsDTO.setTimeSlotList(mplDeliveryTimeSlotList);

				//preparing list of Date Data
				mplEstimateDateList.add(mplEstimateDeliveryDateWsDTO);
			}
			//Setting DeliveyDates Value and preparing List
			mplSDInfoWsDTO.setEstimateDeliveryDateList(mplEstimateDateList);
			mplSDInfoWsDTOList.add(mplSDInfoWsDTO);
		}
		return mplSDInfoWsDTOList;

	}


	/**
	 * save selected DeliveryDate and time for selected through Mobile
	 */
	@Override
	public void saveSelectedDateAndTime(final OrderModel orderModel, final List<TransactionSDDto> transactionSDDto)
	{
		mplDeliveryAddressService.saveSelectedDateAndTime(orderModel, transactionSDDto);
	}


	/**
	 * @return the mplOrderFacade
	 */
	public MplOrderFacade getMplOrderFacade()
	{
		return mplOrderFacade;
	}

	/***
	 * Send Notification For CDA
	 *
	 */
	@Override
	public void sendPushNotificationForCDA(final CustomerModel customerModel, final String otPNumber, final String mobileNumber)
	{
		final String mplCustomerName = customerModel.getFirstName();
		final String contactNumber = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);

		sendSMSFacade.sendSms(
				MarketplacecommerceservicesConstants.SMS_SENDER_ID,
				MarketplacecommerceservicesConstants.SMS_MESSAGE_CD_OTP
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
								mplCustomerName != null ? mplCustomerName : "There")
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, otPNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, contactNumber), mobileNumber);

	}
	
	    //Get State code
		private String getStateCode(String stateName)
		{
			for (final StateData state : accountAddressFacade.getStates())
			{
				if (state.getName().equalsIgnoreCase(stateName))
				{
					return state.getCode();
				}
			}
			return null;
		}

		
		/**
		 * This method will check  ED Transaction 
		 */
	@Override
	public boolean isEDOrder(OrderData orderData)
	{
		boolean isEDEntry = false;
		for (OrderData subOrder : orderData.getSellerOrderList())
		{
			for (OrderEntryData entry : subOrder.getEntries())
			{
				if (MarketplaceFacadesConstants.EXPRESS_DELIVERY.equalsIgnoreCase(entry.getMplDeliveryMode().getCode()))
				{
					isEDEntry = true;
					return isEDEntry;
				}
			}
		}
		return isEDEntry;
	}
}
