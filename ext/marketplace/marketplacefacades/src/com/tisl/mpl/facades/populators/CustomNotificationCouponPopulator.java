/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.log4j.Logger;

import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.data.VoucherDisplayData;


/**
 * @author TCS
 *
 */
public class CustomNotificationCouponPopulator<SOURCE extends VoucherDisplayData, TARGET extends NotificationData> implements
		Populator<SOURCE, TARGET>
{


	protected static final Logger LOG = Logger.getLogger(CustomNotificationCouponPopulator.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final SOURCE VoucherDisplayData, final TARGET notificationData) throws ConversionException
	{
		if (null != VoucherDisplayData)
		{
			notificationData.setCouponCode(VoucherDisplayData.getVoucherCode());
			notificationData.setNotificationRead(Boolean.FALSE);
			notificationData.setNotificationCreationDate(VoucherDisplayData.getVoucherCreationDate());
			notificationData.setNotificationCustomerStatus("Coupon @ is available");


		}

	}




}
