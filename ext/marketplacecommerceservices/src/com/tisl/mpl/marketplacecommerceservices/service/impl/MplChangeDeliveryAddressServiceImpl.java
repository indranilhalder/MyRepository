/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

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
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplChangeDeliveryAddressDao;
//import com.tis.mpl.facade.changedelivery.Impl.ChangeDeliveryAddressFacadeImpl;
import com.tisl.mpl.marketplacecommerceservices.service.MplChangeDeliveryAddressService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author prasad1
 *
 */
public class MplChangeDeliveryAddressServiceImpl implements MplChangeDeliveryAddressService
{

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private MplChangeDeliveryAddressDao mplChangeDeliveryAddressDao;

	@Autowired
	private ModelService modelService;

	@Autowired
	OrderModelDao orderModelDao;

	private static final Logger LOG = Logger.getLogger(MplChangeDeliveryAddressServiceImpl.class);



	@Override
	public boolean isDeliveryAddressChangable(final String orderId)
	{
		final List<String> ChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(), OrderStatus.PICK_LIST_GENERATED.getCode(),OrderStatus.ORDER_REALLOCATED.getCode(),
				OrderStatus.PICK_CONFIRMED.getCode());
		boolean changable = true;

		try
		{
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
							}

						}
						if (!deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)
								&& entry.getQuantity().longValue() > 0)
						{
							if (isCdAllowed.equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
							{
								return false;
							}

							if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
							{
								final ConsignmentStatus consignmentStatus = entry.getConsignmentEntries().iterator().next()
										.getConsignment().getStatus();
								entryStatus = consignmentStatus.getCode();
							}
							if (!ChangableOrdeStatus.contains(entryStatus))
							{
								return false;
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
		return changable;
	}


	/**
	 * save AddressForCustomer in TemproryAddressModel with orderId
	 *
	 * return boolean true false
	 */
	@Override
	public boolean saveTemproryAddress(String orderCode,TemproryAddressModel temproryAddressModel)
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
				final TemproryAddressModel temproryAddressModel = mplChangeDeliveryAddressDao.geTemproryAddressModel(orderCode);
				orderModel = orderModelDao.getOrderModel(orderCode);
				if (orderModel != null)
				{
					orderModel.setDeliveryAddress(temproryAddressModel);

					/*final Collection<AddressModel> deliveryAddressLis = orderModel.getDeliveryAddresses();
					deliveryAddressLis.add(temproryAddressModel);
					orderModel.setDeliveryAddresses(deliveryAddressLis);*/
					modelService.save(orderModel);
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
	public TemproryAddressModel geTemproryAddressModel(final String orderCode)
	{
		return mplChangeDeliveryAddressDao.geTemproryAddressModel(orderCode);
	}


	@Override
	public void removeTemproryAddress(final String orderCode)
	{
		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{
				TemproryAddressModel temproryAddressModel = mplChangeDeliveryAddressDao.geTemproryAddressModel(orderCode);
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

}