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
	public boolean isDeliveryAddressChangable(final String orderId)
	{
		final List<String> ChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(), OrderStatus.PICK_LIST_GENERATED.getCode(),
				OrderStatus.ORDER_REALLOCATED.getCode(), OrderStatus.PICK_CONFIRMED.getCode(), OrderStatus.ORDER_REJECTED.getCode(),
				OrderStatus.PENDING_SELLER_ASSIGNMENT.getCode());
		boolean isAddressChangable = false;

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
								isAddressChangable = false;
								LOG.debug("Delivery Address changable : " + isAddressChangable);
								return isAddressChangable;
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
								isAddressChangable = false;
								LOG.debug("Delivery Address changable : " + isAddressChangable);
								return isAddressChangable;
							}
							else
							{
								isAddressChangable = true;
							}
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred while checking whether address is changable or not for order id " + orderId + " "
					+ e.getCause());
		}
		LOG.debug("Delivery Address changable : " + isAddressChangable);
		return isAddressChangable;
	}

	@Override
	public boolean saveTemporaryAddress(final OrderModel orderModel, final TemproryAddressModel temproryAddressModel)
	{
		boolean isTempAddressSave = false;
		try
		{
			if (temproryAddressModel != null)
			{
				temproryAddressModel.setOrderId(orderModel.getCode());
				temproryAddressModel.setOwner(orderModel);
				modelService.saveAll(temproryAddressModel);
				isTempAddressSave = true;
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("ModelSavingException while saving temprory address" + e.getMessage());
		}
		catch (final NullPointerException e)
		{
			LOG.error("NullPointerException while saving temprory address" + e.getMessage());
		}
		return isTempAddressSave;
	}




	@Override
	public boolean saveDeliveryAddress(final String orderCode)
	{
		LOG.info("Inside saveDeliveryAddress Method");
		boolean isDeliveryAddressChanged = false;
		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{
				final TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderCode);
				if (temproryAddressModel != null)
				{
					final OrderModel orderModel = orderModelDao.getOrderModel(orderCode);
					if (orderModel != null)
					{
						final UserModel user = orderModel.getUser();
						final List<AddressModel> deliveryAddressesList = new ArrayList<AddressModel>();
						final Collection<AddressModel> customerAddressesList = new ArrayList<AddressModel>();
						final Collection<AddressModel> deliveryAddresses = orderModel.getDeliveryAddresses();
						if (null != deliveryAddresses && !deliveryAddresses.isEmpty())
						{
							deliveryAddressesList.addAll(deliveryAddresses);
						}
						if (null != user.getAddresses())
						{
							customerAddressesList.addAll(user.getAddresses());
						}
						temproryAddressModel.setIsProcessed(Boolean.TRUE);
						deliveryAddressesList.add(temproryAddressModel);
						customerAddressesList.add(temproryAddressModel);
						orderModel.setDeliveryAddress(temproryAddressModel);
						for (final OrderModel childOrder : orderModel.getChildOrders())
						{
							childOrder.setDeliveryAddress(temproryAddressModel);
							childOrder.setDeliveryAddresses(deliveryAddressesList);
							modelService.save(childOrder);
						}
						user.setAddresses(customerAddressesList);
						orderModel.setDeliveryAddresses(deliveryAddressesList);
						modelService.save(temproryAddressModel);
						modelService.save(orderModel);
						modelService.save(user);
						isDeliveryAddressChanged = true;
					}
				}
			}

		}
		catch (final ModelSavingException expection)
		{
			LOG.error("ModelSavingException Exception while saving changed delivery Address" + expection.getMessage());
		}
		return isDeliveryAddressChanged;
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
				final TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderCode);
				if (temproryAddressModel != null && StringUtils.isNotEmpty(temproryAddressModel.getPostalcode()))
				{
					modelService.remove(temproryAddressModel);
				}
			}
		}
		catch (final NullPointerException exception)
		{
			LOG.error(" NullPointerException while removing TemprorydeliveryAddress" + exception.getMessage());
		}

	}



	@Override
	public void setStatusForTemporaryAddress(final String orderId, final boolean isProcessed)
	{
		final TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderId);
		try
		{
			if (temproryAddressModel != null)
			{
				temproryAddressModel.setIsProcessed(Boolean.FALSE);
				modelService.saveAll(temproryAddressModel);
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("ModelSavingException while setting status for TemporaryAddress" + e.getMessage());
		}
	}

	@Override
	public boolean updateContactDetails(final TemproryAddressModel temproryAddressModel, final OrderModel orderModel)
	{
		boolean isUpdatedDetails = false;
		try
		{
			if (temproryAddressModel != null)
			{
				temproryAddressModel.setIsProcessed(Boolean.TRUE);
				orderModel.setDeliveryAddress(temproryAddressModel);
				modelService.saveAll(temproryAddressModel);
				modelService.saveAll(orderModel);
				isUpdatedDetails = true;
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("ModelSavingException while updating contact details" + e.getMessage());
		}
		return isUpdatedDetails;
	}

	@Override
	public List<TemproryAddressModel> getTemporaryAddressModelList(final String dateFrom, final String toDate)
	{
		try
		{
			return mplDeliveryAddressDao.getTemporaryAddressModelList(dateFrom, toDate);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred while getting the TemporaryAddressModelList" + e.getMessage());
		}
		return null;
	}
}