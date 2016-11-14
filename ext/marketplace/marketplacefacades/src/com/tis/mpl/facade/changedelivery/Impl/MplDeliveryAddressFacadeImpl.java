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
import org.apache.commons.lang.time.DateFormatUtils;
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
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.data.ReturnAddressInfo;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;
import com.tisl.mpl.facades.data.RescheduleData;
import com.tisl.mpl.facades.data.RescheduleDataList;
import com.tisl.mpl.facades.data.ScheduledDeliveryData;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.wsdto.MplDeliveryTimeSlotWsDTO;
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
	public String changeDeliveryRequestCallToOMS(String orderId,AddressModel newDeliveryAddress,
			 String interfaceType,List<TransactionSDDto> transactionSDDtos)
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
	private ChangeDeliveryAddressDto getChangeDeliveryRequestData(String orderId, AddressModel newDeliveryAddress,
			String interfaceType, List<TransactionSDDto> transactionSDDtos)
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



	/**
	 * This method is used to create CRM Ticket for Change Delivery Address
	 *
	 * @author Techouts
	 * @param Order
	 * @param source
	 */
	@Override
	public void createcrmTicketForChangeDeliveryAddress(OrderModel Order,String costomerId,String source)
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


	/**
	 * This method is used to check whether change delivery is possible or not , If possible it returns true else it
	 * returns false
	 *
	 * @param orderId
	 * @return
	 * @throws EtailNonBusinessExceptions
	 */

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


	/**
	 * Checking order are for Eligible Re ScheduledDelivery date 
	 * if not Eligible then return null value
	 * @param orderCode
	 * @return ScheduledDeliveryData
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public ScheduledDeliveryData scheduledDeliveryData(String orderCode, AddressData addressData)
	{
		ScheduledDeliveryData scheduledDeliveryData = new ScheduledDeliveryData();
		try
		{
			OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
			CustomerModel customer = (CustomerModel) orderModel.getUser();
			AddressModel newDeliveryAddress = tempAddressReverseConverter.convert(addressData);
			if (newDeliveryAddress != null)
			{
				ServicesUtil.validateParameterNotNull(customer.getOriginalUid(), "OriginalUid must not be null");
				newDeliveryAddress.setEmail(customer.getOriginalUid());
				//check order Eligible for ReScheduledDelivery
				boolean isEligibleScheduledDelivery = mplDeliveryAddressService.checkScheduledDeliveryForOrder(orderModel);
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
	 * Customer changed pincode for delivery address 
	 * Pincode Serviceable Checking for Order
	 * 
	 *  
	 */
	@Override
	public boolean pincodeServiceableCheck(AddressData addressData, String orderCode)
	{
		
		boolean isServiceable=false;
		try{
		// Pincode Serviceable Check for Order
		ServicesUtil.validateParameterNotNull(addressData.getPostalCode(), "Pincode must not be null");
		AddressModel newDeliveryAddress = new AddressModel();
		newDeliveryAddress.setPostalcode(addressData.getPostalCode());
		
   	String pincodeServiceableCheck = changeDeliveryRequestCallToOMS(orderCode, newDeliveryAddress,
				MarketplaceFacadesConstants.CDP, null);
		if (StringUtils.isNotEmpty(pincodeServiceableCheck))
		{
			 OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
			if (pincodeServiceableCheck.equalsIgnoreCase(MarketplaceFacadesConstants.SUCCESS))
			{
				 CustomerModel customer = (CustomerModel) orderModel.getUser();
				 ServicesUtil.validateParameterNotNull(customer.getOriginalUid(), "OriginalUid must not be null");
				 newDeliveryAddress.setEmail(customer.getOriginalUid());
				// set the Address in a session
				sessionService.setAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS, addressData);
				if (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()))
				{
					try
					{
						generateOTP(customer.getUid(), orderModel.getDeliveryAddress().getPhone1());
					}
					catch (final InvalidKeyException excption)
					{
						LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during otp generate"+excption.getMessage());
					}
					catch (final NoSuchAlgorithmException excption)
					{
						LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during otp generate"+excption.getMessage());
					}
				}
				isServiceable=true;
			}
			else
			{
				mplDeliveryAddressService.deliveryAddressFailureRequest(orderModel);
			}
		}
		}catch(Exception exp){
		LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during whille checking pincode Serviceable"
				+ exp.getMessage());
		}
		return isServiceable;
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
	String generateOTP(String customerId,String mobileNumber) throws InvalidKeyException, NoSuchAlgorithmException
	{
		 String otp = otpGenericService.generateOTP(customerId, OTPTypeEnum.CDA.getCode(), mobileNumber);
		return otp;
	}



	
	
	/***
	 * Checking OTP Validation
	 *  @return OTPResponseData
	 */
	@Override
	public OTPResponseData validateOTP(String customerID, String enteredOTPNumber){
		OTPResponseData otpResponse = otpGenericService.validateOTP(customerID, null, enteredOTPNumber, OTPTypeEnum.CDA,
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
	public String submitChangeDeliveryAddress(String customerID, String orderCode,AddressData newDeliveryAddressData,boolean isMobile,List<TransactionSDDto> transactionSDDtoList)
	{
		String valditionMsg = null;
		OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
		try
		{
			AddressModel newDeliveryAddressModel = tempAddressReverseConverter.convert(newDeliveryAddressData);
			boolean isDiffrentAddress = false;
			//Address Related filed is changed  then send to OMS  Request CA Type
			isDiffrentAddress = mplDeliveryAddressComparator.compareAddressModel(orderModel.getDeliveryAddress(),
					newDeliveryAddressModel);
			if (newDeliveryAddressModel != null)
			{
				if (isDiffrentAddress)
				{
					boolean isEligibleScheduledDelivery = mplDeliveryAddressService.checkScheduledDeliveryForOrder(orderModel);
					//checking  order Eligible for ReScheduledDelivery 
					if (isEligibleScheduledDelivery)
					{
						if (!isMobile)
						{
							RescheduleDataList rescheduleDataList = sessionService
									.getAttribute(MarketplacecommerceservicesConstants.RESCHEDULE_DATA_SESSION_KEY);
							if (CollectionUtils.isNotEmpty(rescheduleDataList.getRescheduleDataList()))
							{
								transactionSDDtoList = reScheduleddeliveryDate(orderModel, rescheduleDataList);
								sessionService.removeAttribute(MarketplacecommerceservicesConstants.RESCHEDULE_DATA_SESSION_KEY);
								//save selected Date and time
								mplDeliveryAddressService.saveSelectedDateAndTime(orderModel, transactionSDDtoList);
							}
						}
					}
					   mplDeliveryAddressService.saveDeliveryAddress(newDeliveryAddressModel,orderModel);
					   valditionMsg = MarketplaceFacadesConstants.SUCCESS;
				
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Change Delivery Address updated into commerce  then Call to OMS And CRM");
					}
					try
					{
						//OMS call
						changeDeliveryRequestCallToOMS(orderCode, newDeliveryAddressModel,
								MarketplaceFacadesConstants.CA, transactionSDDtoList);
						//CRM call
						createcrmTicketForChangeDeliveryAddress(orderModel, customerID, MarketplacecommerceservicesConstants.SOURCE);
					}
					catch (final Exception e)
					{
						LOG.debug("MplDeliveryAddressFacadesImpl:::Excepton raising while OMS  and CRM Call  >>>>>" + e.getMessage());
					}
				}
				else
				{
					mplDeliveryAddressService.saveDeliveryAddress(newDeliveryAddressModel, orderModel);
					valditionMsg = MarketplaceFacadesConstants.SUCCESS;
					//customer changed only first name Last name and mobile number then OMS Call CU interface Type
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Updated Contact Related filed  into commerce  then Call to OMS And CRM with CU interface");
					}
					try
					{
						//OMS call
						changeDeliveryRequestCallToOMS(orderCode, newDeliveryAddressModel,
								MarketplaceFacadesConstants.CU, null);
		             //CRM Call     
						createcrmTicketForChangeDeliveryAddress(orderModel, customerID, MarketplacecommerceservicesConstants.SOURCE);
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
	 * new OTP Request For  CDA
	 * Based On orderCode 
	 */
	@Override
	public boolean newOTPRequest(String orderCode)
	{
		boolean isGenerated = false;
		OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
		if (orderModel != null)
		{
			CustomerModel customer = (CustomerModel) orderModel.getUser();
			try
			{
				if (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPhone1()))
				{
					generateOTP(customer.getUid(), orderModel.getDeliveryAddress().getPhone1());
				}
				else if (StringUtils.isNotEmpty(customer.getMobileNumber()))
				{
					generateOTP(customer.getUid(), customer.getMobileNumber());
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


	/**
	 * OMS Request Sending For ScheduledDeliveryDate Argument- OrderModel 
	 * return List of Transaction Id and date
	 */

	@Override
	public List<TransactionEddDto> getScheduledDeliveryDate(OrderModel orderModel,String newPincode)
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
		catch (Exception exception)
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
		 TransactionSDDto transactionSDDto;
		for (OrderModel subOrder : orderModel.getChildOrders())
		{
			for (AbstractOrderEntryModel abstractOrderEntry : subOrder.getEntries())
			{
				if (!MarketplacecommerceservicesConstants.CLICK_COLLECT
						.equalsIgnoreCase(abstractOrderEntry.getMplDeliveryMode().getDeliveryMode().getCode()))
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
	 * Setting Delivery  date and time Slots for product 
	 * @return Map
	 */
	@Override
	public Map<String, Object> getDeliveryDate(List<TransactionEddDto> transactionEddDtoList, OrderModel orderModel)
	{
		Map<String, Object> scheduledDeliveryDate = new HashMap<String, Object>();
		try
		{
			String timeSlotType = null;
			Map<String, List<String>> scheduledDeliveryTime = null;
			Map<String, TransactionEddDto> mapTransactionEddDto = getEligibleEntry(orderModel, transactionEddDtoList);

			for (Entry<String, TransactionEddDto> key : mapTransactionEddDto.entrySet())
			{
				TransactionEddDto transactionEddDto = key.getValue();
				String deliveryMode = null;
				for (OrderModel subOrder : orderModel.getChildOrders())
				{
					for (AbstractOrderEntryModel abstractOrderEntryModel : subOrder.getEntries())
					{
						if (transactionEddDto.getTransactionID().equalsIgnoreCase(abstractOrderEntryModel.getTransactionID()))
						{
							deliveryMode = abstractOrderEntryModel.getMplDeliveryMode().getDeliveryMode().getCode();
							break;
						}
					}
					if (StringUtils.isNotEmpty(deliveryMode))
					{
						break;
					}
				}
				if (MarketplacecommerceservicesConstants.HOME_DELIVERY.equalsIgnoreCase(deliveryMode))
				{
					timeSlotType = MarketplacecommerceservicesConstants.INTERFACE_TYPE_SD;
				}
				else
				{
					timeSlotType = MarketplacecommerceservicesConstants.ED;
				}
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
		catch (Exception parseException)
		{
			LOG.info("parseException raising converrting time" + parseException.getMessage());
		}
		return scheduledDeliveryDate;
	}




	/***
	 * Argument dateFrom and dateTo Changed Delivery Address 
	 * Report Method return List<MplDeliveryAddressReportData>
	 */
	@Override
	public List<MplDeliveryAddressReportData> getDeliveryAddressRepot(Date dateFrom, Date   dateTo)
	{

		List<MplDeliveryAddressReportData> mplDeliveryAddressReportDataList = new ArrayList<MplDeliveryAddressReportData>();
		try
		{
			LOG.info("MplDeliveryAddressFacadeImpl:Change Delivery Address Request Purpose Preparing Report  ");
			ServicesUtil.validateParameterNotNull(dateFrom, "dateFrom must not be null");
			ServicesUtil.validateParameterNotNull(dateTo, "dateTo must not be null");

			List<MplDeliveryAddressInfoModel> mplDeliveryAddressInfoModelList = mplDeliveryAddressService.getMplDeliveryAddressReportModels(dateFrom, dateTo);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("MplDeliveryAddressFacadeImpl:Preparing Data For BackOffice DeliveryAddressRepot");
			}
			if (CollectionUtils.isNotEmpty(mplDeliveryAddressInfoModelList))
			{
				for (MplDeliveryAddressInfoModel mplDeliveryAddressInfoModel : mplDeliveryAddressInfoModelList)
				{
					MplDeliveryAddressReportData mplDeliveryAddressReportData = new MplDeliveryAddressReportData();
					mplDeliveryAddressReportData.setOrderId(mplDeliveryAddressInfoModel.getOrderId());
					mplDeliveryAddressReportData.setTotalRequestCount(mplDeliveryAddressInfoModel.getChangeDeliveryTotalRequests().intValue());
					mplDeliveryAddressReportData.setFailureRequsetCount(mplDeliveryAddressInfoModel.getChangeDeliveryRejectsCount().intValue());
					mplDeliveryAddressReportDataList.add(mplDeliveryAddressReportData);
				}
			}
		}
		catch (Exception exp)
		{
			LOG.error("MplDeliveryAddressFacadeImpl:Exception occurs during whille createing Report" + exp.getMessage());
			return null;
		}
		return mplDeliveryAddressReportDataList;
	}

	/**
	 * get Delivery Date And time slot
	 * @param edd
	 * @return Map
	 * @throws java.text.ParseException
	 */
	private Map<String, List<String>> getDateAndTimeMap(String timeSlotType, String edd) throws java.text.ParseException
	{

		DateUtilHelper dateUtilHelper = new DateUtilHelper();
		String estDeliveryDateAndTime = edd;
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String deteWithOutTIme = dateUtilHelper.getDateFromat(estDeliveryDateAndTime, format);
		String timeWithOutDate = dateUtilHelper.getTimeFromat(estDeliveryDateAndTime);
		List<String> calculatedDateList = null;
		if (timeSlotType.equalsIgnoreCase(MarketplacecommerceservicesConstants.INTERFACE_TYPE_SD))
		{
			calculatedDateList = dateUtilHelper.getDeteList(deteWithOutTIme, format, 3);
		}
		else
		{
			calculatedDateList = dateUtilHelper.getDeteList(deteWithOutTIme, format, 2);
		}
		List<MplTimeSlotsModel> modelList = null;
		modelList = mplConfigFacade.getDeliveryTimeSlotByKey(timeSlotType);
		LOG.debug("********* Delivery Mode :" + timeSlotType);
		if (null != modelList)
		{
			Date startTime = null;
			Date endTIme = null;
			Date searchTime = null;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			List<MplTimeSlotsModel> timeList = new ArrayList<MplTimeSlotsModel>();
			for (MplTimeSlotsModel mplTimeSlotsModel : modelList)
			{
				for (String selectedDate : calculatedDateList)
				{
					if (selectedDate.equalsIgnoreCase(deteWithOutTIme))
					{
						try
						{
							startTime = sdf.parse(mplTimeSlotsModel.getToTime());
							endTIme = sdf.parse(mplTimeSlotsModel.getFromTime());
							searchTime = sdf.parse(timeWithOutDate);
						}
						catch (java.text.ParseException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
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
				String nextDate = dateUtilHelper.getNextDete(deteWithOutTIme, format);
				calculatedDateList = dateUtilHelper.getDeteList(nextDate, format, 3);
				timeList.addAll(modelList);
			}
			List<String> finalTimeSlotList = null;
			Map<String, List<String>> dateTimeslotMapList = new LinkedHashMap<String, List<String>>();
			for (String selectedDate : calculatedDateList)
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


	/***
	 * Preparing product EligibleEntry for  reScheduleddeliveryDate
	 * which product are Eligible for reScheduleddeliveryDate
	 * @param orderModel
	 * @param transactionEddDtoList
	 * @return Map
	 * @throws ParseException
	 */
	public Map<String, TransactionEddDto> getEligibleEntry(OrderModel orderModel, List<TransactionEddDto> transactionEddDtoList)
			throws ParseException
	{
		Map<String, TransactionEddDto> mapTransactionEdd = new HashMap<String, TransactionEddDto>();
		for (OrderModel subOrder : orderModel.getChildOrders())
		{
			for (AbstractOrderEntryModel abstractOrderEntryModel : subOrder.getEntries())
			{
				if (!abstractOrderEntryModel.getMplDeliveryMode().getDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
				{
					if (abstractOrderEntryModel.getEdScheduledDate() != null)
					{
						for (TransactionEddDto transactionEddDto : transactionEddDtoList)
						{
							if (transactionEddDto.getTransactionID().equals(abstractOrderEntryModel.getTransactionID()))
							{
								TransactionEddDto rescheduleData = null;
								if (mapTransactionEdd.containsKey(abstractOrderEntryModel.getProduct().getCode()))
								{
									rescheduleData = mapTransactionEdd.get(abstractOrderEntryModel.getProduct().getCode());
									SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
									Date oldDate = formatter.parse(rescheduleData.getEDD());
									Date newDate = formatter.parse(transactionEddDto.getEDD());
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
  * Preparing  TransactionSDDto data related 
  * to customer selected date and time  
  *   
  */
	@Override
	public List<TransactionSDDto> reScheduleddeliveryDate(OrderModel orderModel, RescheduleDataList rescheduleDataListDto)
	{
		List<TransactionSDDto> transactionSDDtoList = new ArrayList<TransactionSDDto>();
		List<RescheduleData> rescheduleDataList = rescheduleDataListDto.getRescheduleDataList();
		for (OrderModel subModel : orderModel.getChildOrders())
		{
			for (AbstractOrderEntryModel abstractOrderEntryModel : subModel.getEntries())
			{
				for (RescheduleData rescheduleData : rescheduleDataList)
				{
					if (StringUtils.isNotEmpty(rescheduleData.getProductCode()))
					{
						if (abstractOrderEntryModel.getProduct().getCode().equalsIgnoreCase(rescheduleData.getProductCode()))
						{
							TransactionSDDto transactionSDDto = new TransactionSDDto();
							transactionSDDto.setTransactionID(abstractOrderEntryModel.getTransactionID());
							transactionSDDto.setPickupDate(rescheduleData.getDate());
							String timeFromTo = rescheduleData.getTime();
							if (StringUtils.isNotEmpty(timeFromTo))
							{
								transactionSDDto.setTimeSlotFrom(timeFromTo.substring(0, timeFromTo.indexOf("TO") - 1));
								transactionSDDto.setTimeSlotTo(timeFromTo.substring(timeFromTo.indexOf("TO") + 3, timeFromTo.length()));
								abstractOrderEntryModel.setEdScheduledDate(rescheduleData.getDate());
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
	public void saveChangeDeliveryRequests(OrderModel orderModel)
	{
		mplDeliveryAddressService.deliveryAddressFailureRequest(orderModel);

	}



	/***
	 * his method is used to check whether order has Schedule delivery items or not
	 * 
	 */
	@Override
	public boolean checkScheduledDeliveryForOrder(OrderModel orderModel)
	{
		return mplDeliveryAddressService.checkScheduledDeliveryForOrder(orderModel);
	}
	
	
	/***
	 * Preparing Re Scheduled Delivery Page Data
	 * @return OrderData
	 */
	@Override
	public OrderData reScheduledDeliveryPageData(OrderData orderData){
		
		for (final OrderEntryData orderEntry : orderData.getEntries())
		{
			//getting the product code
			ProductModel productModel = getMplOrderFacade().getProductForCode(orderEntry.getProduct().getCode());
			if (CollectionUtils.isNotEmpty(productModel.getBrands()))
			{
				for (final BrandModel brand : productModel.getBrands())
				{
					orderEntry.setBrandName(brand.getName());
					break;
				}
			}
			List<SellerInformationModel> sellerInfo = (List<SellerInformationModel>) productModel
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
    * Preparing EstimateDeliveryDate data
    * for Mobile  
    * 
    */
   @Override
	public List<MplSDInfoWsDTO> getSDDatesMobile(Object dataSDDates)
	{
      //ProductData
		List<MplSDInfoWsDTO> mplSDInfoWsDTOList = new ArrayList<MplSDInfoWsDTO>();
		MplSDInfoWsDTO mplSDInfoWsDTO;
		
		//DeliveyDate
		List<MplEstimateDeliveryDateWsDTO> mplEstimateDateList;
		MplEstimateDeliveryDateWsDTO mplEstimateDeliveryDateWsDTO;
		
		//DeliveryTimeSlot
		List<MplDeliveryTimeSlotWsDTO> mplDeliveryTimeSlotWsDTOList;
		MplDeliveryTimeSlotWsDTO mplDeliveryTimeSlotWsDTO;
		
		Map<String, Object> productData = (Map<String, Object>) dataSDDates;

		for (Entry<String, Object> estimateDates : productData.entrySet())
		{
		   // new instance create for each product
			mplSDInfoWsDTO = new MplSDInfoWsDTO();
			mplEstimateDateList=new ArrayList<MplEstimateDeliveryDateWsDTO>();
			
			//setting product code value 
			mplSDInfoWsDTO.setProductCode(estimateDates.getKey());
			Map<String, List<String>> estimateDateData = (Map<String, List<String>>) estimateDates.getValue();
			for (Entry<String, List<String>> dateData : estimateDateData.entrySet())
			{
			   // new instance create for each product
				mplEstimateDeliveryDateWsDTO = new MplEstimateDeliveryDateWsDTO();
				
				//Setting Delivery date
				mplEstimateDeliveryDateWsDTO.setDeliveryDate(dateData.getKey());
				
				// new instance create  for each date  
				 mplDeliveryTimeSlotWsDTOList = new ArrayList<MplDeliveryTimeSlotWsDTO>();
				
				for (String timeSlot : dateData.getValue())
				{
				// new instance create for each date 
					mplDeliveryTimeSlotWsDTO = new MplDeliveryTimeSlotWsDTO();
					mplDeliveryTimeSlotWsDTO.setTimeSlot(timeSlot);
					mplDeliveryTimeSlotWsDTOList.add(mplDeliveryTimeSlotWsDTO);

				}
				//setting value TimeSlots
				mplEstimateDeliveryDateWsDTO.setTimeSlots(mplDeliveryTimeSlotWsDTOList);
				
				//preparing list of Date Data 
				mplEstimateDateList.add(mplEstimateDeliveryDateWsDTO);
			}
			//Setting DeliveyDates Value and preparing List 
			mplSDInfoWsDTO.setEstimateDeliveryDates(mplEstimateDateList);
			mplSDInfoWsDTOList.add(mplSDInfoWsDTO);
		}
		return mplSDInfoWsDTOList;

	}
	
	
   /**
    * save selected DeliveryDate and time for 
    * selected through Mobile
    */
   @Override
	public void saveSelectedDateAndTime(OrderModel orderModel,List<TransactionSDDto> transactionSDDto){
   	mplDeliveryAddressService.saveSelectedDateAndTime(orderModel, transactionSDDto);
   }

   
	/**
	 * @return the mplOrderFacade
	 */
	public MplOrderFacade getMplOrderFacade()
	{
		return mplOrderFacade;
	}




}
	
