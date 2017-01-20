/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.fulfilmentprocess.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.fulfilmentprocess.CheckOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;


public class DefaultCheckOrderService implements CheckOrderService
{
	private static final Logger LOG = Logger.getLogger(DefaultCheckOrderService.class);

	private MplPaymentService mplPaymentService;


	@Override
	public boolean check(final OrderModel order)
	{
		boolean status = true;

		if (!order.getCalculated().booleanValue())
		{
			// Order must be calculated
			//return false;
			status = false;
		}
		else if (order.getEntries().isEmpty())
		{
			// Order must have some lines
			//return false;
			status = false;
		}
		else if (order.getPaymentInfo() == null)
		{
			// Order must have some payment info to use in the process
			//return false;
			status = false;
		}
		//TISPRO-497
		else if (order.getTotalPrice().doubleValue() <= 0.0 || order.getTotalPriceWithConv().doubleValue() <= 0.0)
		{
			// Order must have some payment info to use in the process
			//return false;
			status = false;
		}
		else
		{
			// Order delivery options must be valid
			//return checkDeliveryOptions(order);
			status = checkDeliveryOptions(order);
		}

		return status;
	}

	protected boolean checkDeliveryOptions(final OrderModel order)
	{

		//Commented for Custom Logic
		//		if (order.getDeliveryMode() == null)
		//		{
		//			// Order must have an overall delivery mode
		//			return false;
		//		}

		boolean deliveryOptionCheck = true;

		if (null != order.getChildOrders() && !order.getChildOrders().isEmpty())
		{
			for (final OrderModel subOrder : order.getChildOrders())
			{
				if (null != subOrder.getEntries() && !subOrder.getEntries().isEmpty())
				{
					for (final AbstractOrderEntryModel entry : subOrder.getEntries())
					{
						if (null == entry.getMplDeliveryMode())
						{
							//return false;
							deliveryOptionCheck = false;
						}
					}
				}
			}
		}

		if (order.getDeliveryAddress() == null)
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				if (entry.getDeliveryPointOfService() == null && entry.getDeliveryAddress() == null)
				{
					// Order and Entry have no delivery address and some entries are not for pickup
					deliveryOptionCheck = false;
					//return false;
				}
			}
		}

		//return true;
		return deliveryOptionCheck;
	}

	/*
	 *
	 * //SprintPaymentFixes:- To handle missing paymentTransaction ,Create the Transaction
	 */
	@Override
	public boolean checkMissintPaymentTrancsaction(final OrderModel order)
	{
		boolean status = false;

		if (CollectionUtils.isEmpty(order.getPaymentTransactions()))
		{

			status = mplPaymentService.createPaymentTransactionFromSubmitOrderJob(order);
		}
		return status;
	}

	/**
	 * @return the mplPaymentService
	 */
	public MplPaymentService getMplPaymentService()
	{
		return mplPaymentService;
	}

	/**
	 * @param mplPaymentService
	 *           the mplPaymentService to set
	 */
	public void setMplPaymentService(final MplPaymentService mplPaymentService)
	{
		this.mplPaymentService = mplPaymentService;
	}



}
