/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressDto;
import com.hybris.oms.domain.changedeliveryaddress.ChangeDeliveryAddressResponseDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionEddDto;
import com.hybris.oms.domain.changedeliveryaddress.TransactionSDDto;
//import com.sap.security.core.server.csi.util.StringUtils;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.integration.oms.order.service.impl.CustomOmsOrderService;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;
//import com.tis.mpl.facade.changedelivery.Impl.ChangeDeliveryAddressFacadeImpl;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author Techouts
 *
 */
public class MplDeliveryAddressServiceImpl implements MplDeliveryAddressService
{

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private MplDeliveryAddressDao mplDeliveryAddressDao;

	@Autowired
	private ModelService modelService;

	@Autowired
	OrderModelDao orderModelDao;

	@Autowired
	private CustomOmsOrderService customOmsOrderService;

	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressServiceImpl.class);



	@Override
	public boolean isDeliveryAddressChangable(final String orderId)
	{
		final List<String> ChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(), OrderStatus.PICK_LIST_GENERATED.getCode(),
				OrderStatus.ORDER_REALLOCATED.getCode(), OrderStatus.PICK_CONFIRMED.getCode(), OrderStatus.ORDER_REJECTED.getCode(),
				OrderStatus.PENDING_SELLER_ASSIGNMENT.getCode());
		boolean changable = true;

		try
		{
			LOG.info("Inside isDeliveryAddressChangable  method  , Checking for orderID " + orderId);
			final OrderModel orderModel = orderModelDao.getOrderModel(orderId);
			if (null != orderModel.getChildOrders())
			{
				for (final OrderModel sellerOrder : orderModel.getChildOrders())
				{
					for (final AbstractOrderEntryModel entry : sellerOrder.getEntries())
					{
						String deliveryMode = StringUtils.EMPTY;
						String entryStatus = orderModel.getStatus().getCode();
						String isCdAllowed = MarketplacecommerceservicesConstants.Y;

						if (null != entry && null != entry.getMplDeliveryMode() && null != entry.getMplDeliveryMode().getDeliveryMode())
						{
							deliveryMode = entry.getMplDeliveryMode().getDeliveryMode().getCode();
						}
						if (null != entry && null != entry.getSelectedUSSID())
						{
							final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
									.getSelectedUSSID());
							if (null != sellerInfoModel && null != sellerInfoModel.getSellerMaster()
									&& null != sellerInfoModel.getSellerMaster().getIsCDAllowed())
							{
								isCdAllowed = sellerInfoModel.getSellerMaster().getIsCDAllowed();
								if (LOG.isDebugEnabled())
								{
									LOG.debug("Is CD allowed for seller " + sellerInfoModel.getSellerID() + " " + isCdAllowed);
								}
							}

						}
						if (!deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)
								&& entry.getQuantity().longValue() > 0)
						{
							if (isCdAllowed.equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
							{
								changable = false;
								LOG.debug("Delivery Address changable : " + changable);
								return changable;
							}

							if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
							{
								final ConsignmentStatus consignmentStatus = entry.getConsignmentEntries().iterator().next()
										.getConsignment().getStatus();
								LOG.debug("Consignment status for entry " + entry.getTransactionID() + " is"
										+ consignmentStatus.getCode());
								entryStatus = consignmentStatus.getCode();
							}
							else
							{
								LOG.debug("No Consignment entries found for  " + entry.getTransactionID());
							}
							if (!ChangableOrdeStatus.contains(entryStatus))
							{
								changable = false;
								LOG.debug("Delivery Address changable : " + changable);
								return changable;
							}
							else
							{
								changable = true;
							}
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred " + e.getCause());
		}
		LOG.debug("Delivery Address changable : " + changable);
		return changable;
	}

	@Override
	public boolean saveTemporaryAddress(OrderModel orderModel, TemproryAddressModel temproryAddressModel)
	{
		boolean isTempAddressSave = false;
		try
		{
			if (temproryAddressModel != null)
			{
				temproryAddressModel.setOrderId(orderModel.getCode());
				temproryAddressModel.setOwner(orderModel);
				temproryAddressModel.setIsApproval(true);
				modelService.saveAll(temproryAddressModel);
				isTempAddressSave = true;
			}
		}
		catch (final ModelSavingException expection)
		{
			LOG.error("TemproryAddressModel" + expection.getMessage());
		}
		catch (final NullPointerException expection)
		{
			LOG.error("TemproryAddressModel" + expection.getMessage());
		}
		return isTempAddressSave;
	}




	@Override
	public boolean saveDeliveryAddress(String orderCode)
	{
		boolean isDeliveryAddressChange = false;
		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{

				TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderCode);

				if (temproryAddressModel != null)
				{
					if (temproryAddressModel.isIsApproval())
					{
						OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
						if (orderModel != null)
						{
							UserModel user = orderModel.getUser();
							List<AddressModel> deliveryAddressesList = new ArrayList<AddressModel>();
							Collection<AddressModel> customerAddressesList = new ArrayList<AddressModel>();
							Collection<AddressModel> deliveryAddresses = orderModel.getDeliveryAddresses();
							if (null != deliveryAddresses)
							{
								deliveryAddressesList.addAll(deliveryAddresses);
							}
							if (null != user.getAddresses())
							{
								customerAddressesList.addAll(user.getAddresses());
							}
							deliveryAddressesList.add(temproryAddressModel);
							customerAddressesList.add(temproryAddressModel);
							orderModel.setDeliveryAddress(temproryAddressModel);
							user.setAddresses(customerAddressesList);
							orderModel.setDeliveryAddresses(deliveryAddressesList);
							temproryAddressModel.setIsProcessed(Boolean.TRUE);
							temproryAddressModel.setIsApproval(false);
							modelService.saveAll(orderModel);
							modelService.saveAll(user);
							isDeliveryAddressChange = true;
						}
					}
				}
			}
		}
		catch (final ModelSavingException expection)
		{
			LOG.error("OrderModel chnage deliveryAddress" + expection.getMessage());
		}
		return isDeliveryAddressChange;
	}



	/***
    *
    *
    */
	@Override
	public TemproryAddressModel getTemporaryAddressModel(final String orderCode)
	{
		return mplDeliveryAddressDao.getTemporaryAddressModel(orderCode);
	}


	@Override
	public void removeTemporaryAddress(final String orderCode)
	{
		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{
				 TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderCode);
				if (temproryAddressModel != null && StringUtils.isNotEmpty(temproryAddressModel.getPostalcode()))
				{
					modelService.remove(temproryAddressModel);
				}
			}
		}
		catch (final NullPointerException exception)
		{
			LOG.error(" Remove TemprorydeliveryAddress" + exception.getMessage());
		}

	}

	

	@Override
	public boolean setStatusForTemporaryAddress(String orderId, boolean isApproval)
	{
		boolean flag = false;
		TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderId);
		try
		{
			if (temproryAddressModel != null)
			{
				temproryAddressModel.setIsApproval(false);
				modelService.saveAll(temproryAddressModel);
				flag = true;
			}
		}
		catch (final ModelSavingException expection)
		{
			LOG.error("OrderModel chnage deliveryAddress" + expection.getMessage());
		}
		return flag;
	}

	@Override
	public boolean updateContactDetails(TemproryAddressModel temproryAddressModel, OrderModel orderModel)
	{
		boolean flag = false;
		try
		{
			AddressModel addressModel = orderModel.getDeliveryAddress();
			if (temproryAddressModel != null && addressModel != null)
			{
				addressModel.setFirstname(temproryAddressModel.getFirstname());
				addressModel.setLastname(temproryAddressModel.getLastname());
				addressModel.setPhone1(temproryAddressModel.getPhone1());
				orderModel.setDeliveryAddress(addressModel);
				modelService.saveAll(orderModel);
				flag = true;
			}
		}
		catch (final ModelSavingException expection)
		{
			LOG.error("In OrderModel Update" + expection.getMessage());
		}
		return flag;
	}




	/**
	 * OMS Request Sending For ScheduledDeliveryDate
	 * Argument- OrderModel
	 * return List of Transaction Id and date 
	 */
	@Override
	public List<TransactionEddDto> getScheduledDeliveryDate(OrderModel orderModel, String newPincode)
	{
		List<TransactionEddDto> transactionEddDtosList =new ArrayList<TransactionEddDto>();
		try
		{
			ChangeDeliveryAddressResponseDto changeDeliveryAddressResponseDto = new ChangeDeliveryAddressResponseDto();
			
			changeDeliveryAddressResponseDto = scheduledDeliveryDateRequestToOMS(orderModel, newPincode);
			if (changeDeliveryAddressResponseDto.getResponse().equalsIgnoreCase("Success"))
			{
				 transactionEddDtosList=changeDeliveryAddressResponseDto.getTransactionEddDtos();
			}
			else
			{
				return null;
			}
		}
		catch (Exception exception)
		{
			LOG.error("Sending Oms request to OMS for scheduledDelivery "+exception.getMessage());
		}
		return transactionEddDtosList;
	}


	
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
	
	   	/*omsResponse = customOmsOrderService.changeDeliveryRequestCallToOMS(requestData);
	   	*/
		}
		catch (Exception exception)
		{
			LOG.error("Sending Oms request to OMS for scheduledDelivery "+exception.getMessage());
		}
		return omsResponse;
	}



	/**
	 * Preparing eligible Transaction Data for scheduledDelivery
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
				if (abstractOrderEntry.getDeliveryMode().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
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
	public Map<String,Object> getDeliveryDate(List<TransactionEddDto> transactionEddDtoList)
	{
		Map<String, Object> scheduledDeliveryDate = new HashMap<String,Object>();
		SimpleDateFormat to = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		SimpleDateFormat newFormat = new SimpleDateFormat("MMM-dd");
		ArrayList<String> datesList = new ArrayList<String>();
		try
		{
			for (TransactionEddDto transactionEddDto : transactionEddDtoList)
			{
				if (StringUtils.isNotEmpty(transactionEddDto.getEDD()))
				{
					Map<String, ArrayList<String>> scheduledDeliveryTime = new HashMap<String,ArrayList<String>>();
					String stringDate = transactionEddDto.getEDD();
					Date date = to.parse(stringDate);
					String  newdate=to.format(newFormat.parse(stringDate));
					datesList.add(newdate);
					DateTime dateTime = new DateTime(date);
					dateTime=dateTime.plusDays(1);
					datesList.add(dateTime.toString());
					dateTime=dateTime.plusDays(1);
					datesList.add(dateTime.toString());
					scheduledDeliveryDate.put(transactionEddDto.getTransactionID(), datesList);
				}
			}
		}
		catch (ParseException parseException)
		{
			LOG.info("parseException raing converrting time" + parseException.getMessage());
		}
		return scheduledDeliveryDate;
	}
}