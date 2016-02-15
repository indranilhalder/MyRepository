/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.NotificationData;


/**
 *
 * @author TCS
 *
 * @param <SOURCE>
 * @param <TARGET>
 */


public class CustomNotificationPopulator<SOURCE extends OrderStatusNotificationModel, TARGET extends NotificationData> implements
		Populator<SOURCE, TARGET>
{

	protected static final Logger LOG = Logger.getLogger(CustomNotificationPopulator.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SOURCE orderStatusNotificationModel, final TARGET notificationData) throws ConversionException
	{
		if (null != orderStatusNotificationModel)
		{

			notificationData.setCustomerID(orderStatusNotificationModel.getCustomerUID());
			notificationData.setNotificationCreationDate(orderStatusNotificationModel.getCreationtime());
			notificationData.setNotificationCustomerStatus(orderStatusNotificationModel.getCustomerStatus());
			notificationData.setNotificationOrderStatus(orderStatusNotificationModel.getOrderStatus());
			notificationData.setNotificationRead(orderStatusNotificationModel.getIsRead());
			notificationData.setOrderDetails(orderStatusNotificationModel.getOrderDetails());
			//notificationData.setNotificationSent(orderStatusNotificationModel.get);
			notificationData.setOrderNumber(orderStatusNotificationModel.getOrderNumber());
			notificationData.setTransactionID(orderStatusNotificationModel.getTransactionId());
		}

	}



}
