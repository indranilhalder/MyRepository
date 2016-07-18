/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
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
	public boolean isDeliveryAddressChangable(OrderModel orderModel)
	{
		List<String> ChangableOrdeStatus = Arrays.asList(OrderStatus.PAYMENT_SUCCESSFUL.getCode(),
				OrderStatus.ORDER_ALLOCATED.getCode(), OrderStatus.PICK_LIST_GENERATED.getCode(),
				OrderStatus.PICK_CONFIRMED.getCode());
		boolean changable = false;

		try
		{
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
							final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerInformation(
									entry.getSelectedUSSID()).get(0);
							isCdAllowed = sellerInfoModel.getSellerMaster().getIsCDAllowed();
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
								ConsignmentStatus consignmentStatus = entry.getConsignmentEntries().iterator().next().getConsignment()
										.getStatus();
								entryStatus = consignmentStatus.getCode();
							}
							if (!ChangableOrdeStatus.contains(entryStatus))
							{
								return false;
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			LOG.error("Exception occurred " + e.getCause());
		}
		return changable;
	}
	

	   /**
	    * save AddressForCustomer in 
	    * TemproryAddressModel with orderId
	    * 
	    *return boolean true false
	    */
	@Override
	public boolean saveAsTemproryAddressForCustomer(final String orderCode, final TemproryAddressModel temproryAddressModel)
	{
		boolean flag = false;
		try
		{
			if (StringUtils.isNotEmpty(orderCode) && temproryAddressModel != null)
			{
				temproryAddressModel.setOrderId(orderCode);
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
	public boolean changeDeliveryAddress(final String orderCode)
	{
		boolean flag = false;
		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{
				OrderModel orderModel;
				final TemproryAddressModel temproryAddressModel = mplChangeDeliveryAddressDao.geTemproryAddressModel(orderCode);
				orderModel = orderModelDao.getOrder(orderCode);

				orderModel.setDeliveryAddress(temproryAddressModel);
				modelService.save(orderModel);
				modelService.remove(temproryAddressModel);
				flag = true;
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
	public void removeTemproryAddress(String orderCode)
	{
		try
		{
			if (StringUtils.isNotEmpty(orderCode))
			{
				final TemproryAddressModel temproryAddressModel = mplChangeDeliveryAddressDao.geTemproryAddressModel(orderCode);
				if (temproryAddressModel != null)
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

}