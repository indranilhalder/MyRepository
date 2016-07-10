/**
 *
 */
package com.tis.mpl.facade.changedelivery.Impl;

import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tis.mpl.facade.changedelivery.MplChangeDeliveryAddressFacade;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.data.ReturnAddressInfo;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.MplChangeDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.service.MplChangeDeliveryAddressClientService;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.xml.pojo.MplChangeDeliveryAddressRequest;
import com.tisl.mpl.xml.pojo.MplChangeDeliveryAddressResponce;


/**
 * @author prasad1
 *
 */
public class MplChangeDeliveryAddressFacadeImpl implements MplChangeDeliveryAddressFacade
{
	@Autowired
	private MplChangeDeliveryAddressClientService mplChangeDeliveryAddressClientService;
	@Autowired
	private MplChangeDeliveryAddressService mplChangeDeliveryAddressService;
	@Autowired
	private TicketCreationCRMservice ticketCreate;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private OTPGenericService otpGenericService;
	@Autowired
	private AddressReversePopulator addressReversePopulator;

	private static final Logger LOG = Logger.getLogger(MplChangeDeliveryAddressFacadeImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tis.mpl.facade.changedelivery.ChangeDeliveryAddressFacade#changeDeliveryRequestToOMS(java.lang.String,
	 * de.hybris.platform.core.model.user.AddressModel)
	 */
	@Override
	public boolean changeDeliveryRequestCallToOMS(final String orderId, final AddressModel newDeliveryAddress)
	{
		final MplChangeDeliveryAddressRequest requestData = new MplChangeDeliveryAddressRequest();
		MplChangeDeliveryAddressResponce omsResponce = new MplChangeDeliveryAddressResponce();
		boolean addressChangable = false;
		if (null != orderId)
		{
			requestData.setOrderID(orderId);
		}
		if (null != newDeliveryAddress)
		{
			if (null != newDeliveryAddress.getFirstname())
			{
				requestData.setFName(newDeliveryAddress.getFirstname());
			}
			if (null != newDeliveryAddress.getLastname())
			{
				requestData.setLName(newDeliveryAddress.getLastname());
			}
			if (null != newDeliveryAddress.getEmail())
			{
				requestData.setEmailID(newDeliveryAddress.getEmail());
			}
			if (null != newDeliveryAddress.getPhone1())
			{
				requestData.setPhoneNo(newDeliveryAddress.getPhone1());
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
		}
		try
		{
			omsResponce = mplChangeDeliveryAddressClientService.changeDeliveryRequestCallToOMS(requestData);
		}
		catch (final Exception e)
		{
			LOG.error("Exception while calling to OMS" + e.getCause());
		}
		if (null != omsResponce && null != omsResponce.getResponse() && omsResponce.getResponse().equalsIgnoreCase("SUCCESS"))
		{
			addressChangable = true;
		}
		else
		{
			addressChangable = false;
		}
		return addressChangable;
	}

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
			ticketCreate.ticketCreationModeltoWsDTO(sendTicketRequestData);
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
	public boolean isDeliveryAddressChangable(final OrderModel orderModel)
	{

		final boolean changable = mplChangeDeliveryAddressService.isDeliveryAddressChangable(orderModel);
		return changable;
	}

	
	

	@Override
	public String saveAsTemproryAddressForCustomer(final String customerId, final String orderCode, final AddressData addressData)
	{
		boolean flag = true;
		String status = "fail";
		try
		{
			if (StringUtils.isNotEmpty(addressData.getPhone()))
			{
				TemproryAddressModel temproryAddressModel = new TemproryAddressModel();
				addressReversePopulator.populate(addressData, temproryAddressModel);
				//First Save Address into temproryAddressModel
		
				flag = mplChangeDeliveryAddressService.saveAsTemproryAddressForCustomer(orderCode ,temproryAddressModel);
				if (flag)
				{
					final String mobileNumber = addressData.getPhone();
					status = generateOTP(customerId, mobileNumber);
				}
			}

		}
		catch (final Exception exp)
		{
			exp.printStackTrace();

			return status;
		}
		return status;

	}


	/**
	 *
	 * @param customerId
	 * @param mobileNumber
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 */
	String generateOTP(final String customerId, final String mobileNumber) throws InvalidKeyException, NoSuchAlgorithmException
	{
		final String otp = otpGenericService.generateOTP(customerId, OTPTypeEnum.COD.getCode(), mobileNumber);
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
	@Override
	public String validateOTP(final String customerID, final String enteredOTPNumber, String orderCode)
	{
		String valditionMsg = null;
		Boolean otpValidate;

		final OTPResponseData otpResponse = otpGenericService.validateOTP(customerID, null, enteredOTPNumber, OTPTypeEnum.COD,
				Long.parseLong(configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.TIMEFOROTP)));

	   	otpValidate = otpResponse.getOTPValid();

		//If OTP Valid then call to OMS for Pincode ServiceableCheck
		if (otpValidate.booleanValue())
		{
			TemproryAddressModel addressModel = mplChangeDeliveryAddressService.geTemproryAddressModel(orderCode);

			boolean flag = changeDeliveryRequestCallToOMS(orderCode, addressModel);
			if (flag)
			{
				//if Serviceable Pincode then Save in Order and remove to temporaryAddressModel  
				flag = mplChangeDeliveryAddressService.changeDeliveryAddress(orderCode);
			}else{
				return valditionMsg = "This Pincode not serviceable";
			}
		}
		else	{
			valditionMsg = otpResponse.getInvalidErrorMessage();
		}
		return valditionMsg;
	}



}
