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
				OrderStatus.ORDER_REALLOCATED.getCode(), OrderStatus.PICK_CONFIRMED.getCode(), OrderStatus.ORDER_REJECTED.getCode());
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
	public boolean saveTemporaryAddress(final String orderCode, final TemproryAddressModel temproryAddressModel)
	{
		boolean flag = false;
		try
		{
			if (StringUtils.isNotEmpty(orderCode) && temproryAddressModel != null)
			{
				temproryAddressModel.setOrderId(orderCode);
				temproryAddressModel.setOwner(orderModelDao.getOrderModel(orderCode));
				modelService.save(temproryAddressModel);
				flag = true;
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
		return flag;
	}



	@Override
	public boolean saveDeliveryAddress(final String orderCode)
	{
		boolean flag = false;
		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{
				OrderModel orderModel;
				final TemproryAddressModel temproryAddressModel = mplDeliveryAddressDao.getTemporaryAddressModel(orderCode);
				orderModel = orderModelDao.getOrderModel(orderCode);
				if (orderModel != null)
				{
					final UserModel user = orderModel.getUser();
					final List<AddressModel> deliveryAddressesList = new ArrayList<AddressModel>();
					final Collection<AddressModel> customerAddressesList = new ArrayList<AddressModel>();
					final Collection<AddressModel> deliveryAddresses = orderModel.getDeliveryAddresses();
					if (null != deliveryAddresses)
					{
						deliveryAddressesList.addAll(deliveryAddresses);
					}
					if (null != user.getAddresses())
					{
						customerAddressesList.addAll(user.getAddresses());
					}

					AddressModel addrModel = new AddressModel();
					addrModel = setNewDeliveryAddress(temproryAddressModel);
					addrModel.setOwner(temproryAddressModel.getOwner());
					modelService.save(addrModel);
					deliveryAddressesList.add(addrModel);
					customerAddressesList.add(addrModel);
					orderModel.setDeliveryAddress(addrModel);
					user.setAddresses(customerAddressesList);
					orderModel.setDeliveryAddresses(deliveryAddressesList);
					modelService.saveAll(orderModel);
					modelService.saveAll(user);
					modelService.remove(temproryAddressModel);
					flag = true;
				}
			}
		}
		catch (final ModelSavingException expection)
		{
			LOG.error("OrderModel chnage deliveryAddress" + expection.getMessage());
		}
		return flag;
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
			LOG.error(" Remove TemprorydeliveryAddress" + exception.getMessage());
		}

	}

	private AddressModel setNewDeliveryAddress(final TemproryAddressModel newDeliveryAddress)
	{
		final AddressModel deliveryAddress = new AddressModel();
		if (null != newDeliveryAddress.getFirstname())
		{
			deliveryAddress.setFirstname(newDeliveryAddress.getFirstname());
		}
		if (null != newDeliveryAddress.getLastname())
		{
			deliveryAddress.setLastname(newDeliveryAddress.getLastname());
		}
		if (null != newDeliveryAddress.getStreetname())
		{
			deliveryAddress.setStreetname(newDeliveryAddress.getStreetname());
		}
		if (null != newDeliveryAddress.getStreetnumber())
		{
			deliveryAddress.setStreetnumber(newDeliveryAddress.getStreetnumber());
		}
		if (null != newDeliveryAddress.getAddressLine3())
		{
			deliveryAddress.setAddressLine3(newDeliveryAddress.getAddressLine3());
		}
		if (null != newDeliveryAddress.getEmail())
		{
			deliveryAddress.setEmail(newDeliveryAddress.getEmail());
		}
		if (null != newDeliveryAddress.getPostalcode())
		{
			deliveryAddress.setPostalcode(newDeliveryAddress.getPostalcode());
		}
		if (null != newDeliveryAddress.getCountry())
		{
			deliveryAddress.setCountry(newDeliveryAddress.getCountry());
		}
		if (null != newDeliveryAddress.getCity())
		{
			deliveryAddress.setCity(newDeliveryAddress.getCity());
		}
		if (null != newDeliveryAddress.getState())
		{
			deliveryAddress.setState(newDeliveryAddress.getState());
		}
		if (null != newDeliveryAddress.getLandmark())
		{
			deliveryAddress.setLandmark(newDeliveryAddress.getLandmark());
		}
		if (null != newDeliveryAddress.getPhone1())
		{
			deliveryAddress.setPhone1(newDeliveryAddress.getPhone1());
		}
		return deliveryAddress;
	}


}