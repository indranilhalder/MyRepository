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
package com.tisl.mpl.marketplacecommerceservices.event;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.io.Serializable;


public class PancardRejectEvent extends AbstractEvent
{
	private static final long serialVersionUID = 1L;

	//Added as per EQA comments
	private OrderModel order;

	public PancardRejectEvent()
	{
		super();
	}

	/**
	 * Attention: for backward compatibility this constructor invokes
	 * 
	 * <pre>
	 * setOrder(source)
	 * </pre>
	 * 
	 * in case the source object is a OrderModel!
	 */
	public PancardRejectEvent(final Serializable source)
	{
		super(source);

		// compatibility!
		if (source instanceof OrderModel)
		{
			setOrder((OrderModel) source);
		}
	}


	public void setOrder(final OrderModel order)
	{
		this.order = order;
	}


	public OrderModel getOrder()
	{
		return order;
	}
}
