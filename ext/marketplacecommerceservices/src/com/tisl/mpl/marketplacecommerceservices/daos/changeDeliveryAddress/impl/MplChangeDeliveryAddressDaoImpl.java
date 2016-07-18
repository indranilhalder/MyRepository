/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplChangeDeliveryAddressDao;


/**
 * @author pankajk
 *
 */
public class MplChangeDeliveryAddressDaoImpl implements MplChangeDeliveryAddressDao
{
	private static final Logger LOG = Logger.getLogger(MplChangeDeliveryAddressDaoImpl.class);
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired 
	private ModelService modelService;

	/***
	 * OrderId Based On We will get TemproryAddressModel
	 * 
	 * @param orderCode
	 * @return TemproryAddressModel
	 */

	@Override
	public void saveDeliveryAddress(OrderModel orderModel,AddressModel address)
	{
		try
		{
			orderModel.setDeliveryAddress(address);
			OrderModel parentorder = orderModel.getParentReference();
			UserModel user = orderModel.getUser();
			parentorder.setDeliveryAddress(address);
			modelService.save(orderModel);
			modelService.save(orderModel.getParentReference());
			//modelService.remove(address);
			Collection<AddressModel> addresses = orderModel.getParentReference().getDeliveryAddresses();
			addresses.add(address);
			modelService.save(addresses);
			if (null != user.getAddresses())
			{
				user.getAddresses().add(address);
			}
			modelService.save(user);
		}catch (ModelSavingException e)
		{
			LOG.debug("Model saving Exception" + e.getMessage());

		}
		catch (Exception e)
		{
			LOG.debug("Exception while saving Address" + e.getMessage());
		}
	}


	/***
	 * OrderId Based On We will get TemproryAddressModel
	 * 
	 * @param orderCode
	 * @return TemproryAddressModel
	 */
	@Override
	public TemproryAddressModel geTemproryAddressModel(String orderId)
	{
		TemproryAddressModel tempAddress = modelService.create(TemproryAddressModel.class);
		try
		{
			tempAddress.setOrderId(orderId);
			tempAddress = flexibleSearchService.getModelByExample(tempAddress);
		}
		catch (FlexibleSearchException e)
		{
			LOG.error(" FlexibleSearchException exception " + e.getMessage());
		}
		catch (Exception e)
		{
			LOG.error("Exception occurred while getting the temparory address " + e.getMessage());
		}
		return tempAddress;
	}

	
}
