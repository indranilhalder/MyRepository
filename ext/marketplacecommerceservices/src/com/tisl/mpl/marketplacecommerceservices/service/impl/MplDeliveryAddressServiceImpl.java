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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

//import com.sap.security.core.server.csi.util.StringUtils;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
/*import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;*/
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
	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressServiceImpl.class);



	@Override
	public boolean isDeliveryAddressChangable(String orderId)
	{
		 List<String> ChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(), OrderStatus.PICK_LIST_GENERATED.getCode(),
				OrderStatus.ORDER_REALLOCATED.getCode(), OrderStatus.PICK_CONFIRMED.getCode(), OrderStatus.ORDER_REJECTED.getCode(),
				OrderStatus.PENDING_SELLER_ASSIGNMENT.getCode());
		boolean changable = true;

		try
		{
			LOG.info("Inside isDeliveryAddressChangable  method  , Checking for orderID " + orderId);
			 OrderModel orderModel = orderModelDao.getOrderModel(orderId);
			if (null != orderModel.getChildOrders())
			{
				for (OrderModel sellerOrder : orderModel.getChildOrders())
				{
					for (AbstractOrderEntryModel entry : sellerOrder.getEntries())
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
							 SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
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
								 ConsignmentStatus consignmentStatus = entry.getConsignmentEntries().iterator().next()
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
		catch ( Exception e)
		{
			LOG.error("Exception occurred " + e.getCause());
		}
		LOG.debug("Delivery Address changable : " + changable);
		return changable;
	}

	@Override
	public boolean saveTemporaryAddress(OrderModel orderModel,TemproryAddressModel temproryAddressModel)
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
		catch (ModelSavingException expection)
		{
			LOG.error("TemproryAddressModel" + expection.getMessage());
		}
		catch (NullPointerException expection)
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
		catch (ModelSavingException expection)
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
	public TemproryAddressModel getTemporaryAddressModel(String orderCode)
	{
		return mplDeliveryAddressDao.getTemporaryAddressModel(orderCode);
	}


	@Override
	public void removeTemporaryAddress(String orderCode)
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
		catch (NullPointerException exception)
		{
			LOG.error(" Remove TemprorydeliveryAddress" + exception.getMessage());
		}

	}



	@Override
	public boolean setStatusForTemporaryAddress(String orderId,boolean isApproval)
	{
		boolean isChangedStatus = false;
		 TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderId);
		try
		{
			if (temproryAddressModel != null)
			{
				temproryAddressModel.setIsApproval(false);
				modelService.saveAll(temproryAddressModel);
				isChangedStatus = true;
			}
		}
		catch (ModelSavingException expection)
		{
			LOG.error("OrderModel chnage deliveryAddress" + expection.getMessage());
		}
		return isChangedStatus;
	}

	@Override
	public boolean updateContactDetails(TemproryAddressModel temproryAddressModel,OrderModel orderModel)
	{
		boolean isUpdatedDetails = false;
		try
		{
			 AddressModel addressModel = orderModel.getDeliveryAddress();
			if (temproryAddressModel != null && addressModel != null)
			{
				addressModel.setFirstname(temproryAddressModel.getFirstname());
				addressModel.setLastname(temproryAddressModel.getLastname());
				addressModel.setPhone1(temproryAddressModel.getPhone1());
				modelService.saveAll(addressModel);
				orderModel.setDeliveryAddress(addressModel);
			   temproryAddressModel.setIsApproval(false);
			   temproryAddressModel.setIsProcessed(Boolean.TRUE);
				modelService.saveAll(orderModel);
				modelService.saveAll(temproryAddressModel);
				isUpdatedDetails = true;
			}
		}
		catch (ModelSavingException expection)
		{
			LOG.error("In OrderModel Update" + expection.getMessage());
		}
		return isUpdatedDetails;
	}

	@Override
	public List<TemproryAddressModel> getTemporaryAddressModelList(String dateFrom,String toDate){
		return mplDeliveryAddressDao.getTemporaryAddressModelList(dateFrom,toDate);
	}
}