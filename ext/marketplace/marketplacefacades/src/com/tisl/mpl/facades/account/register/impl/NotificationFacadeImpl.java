/**
 *
 */
package com.tisl.mpl.facades.account.register.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.NotificationFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;


/**
 * @author TCS
 *
 */
public class NotificationFacadeImpl implements NotificationFacade
{
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private Converter<OrderStatusNotificationModel, NotificationData> trackOrderConverter;
	@Autowired
	private ExtendedUserService extendedUserService;



	/**
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}

	/**
	 * @return the trackOrderConverter
	 */
	public Converter<OrderStatusNotificationModel, NotificationData> getTrackOrderConverter()
	{
		return trackOrderConverter;
	}

	/**
	 * @param trackOrderConverter
	 *           the trackOrderConverter to set
	 */
	public void setTrackOrderConverter(final Converter<OrderStatusNotificationModel, NotificationData> trackOrderConverter)
	{
		this.trackOrderConverter = trackOrderConverter;
	}

	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}

	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.facades.account.register.NotificationFacade#getNotificationDetail(com.tisl.mpl.data.NotificationData)
	 */
	@Override
	public List<NotificationData> getNotificationDetail(final String customerUID, final boolean isDesktop)
	{
		final List<OrderStatusNotificationModel> notificationModel = notificationService.getNotificationDetails(customerUID,
				isDesktop);
		final List<NotificationData> notificationDataList = new ArrayList<>();
		for (final OrderStatusNotificationModel osnm : notificationModel)
		{
			final NotificationData tempnotificationData = trackOrderConverter.convert(osnm);
			notificationDataList.add(tempnotificationData);
		}


		return notificationDataList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.account.register.NotificationFacade#checkCustomerFacingEntry(com.tisl.mpl.core.model.
	 * OrderStatusNotificationModel)
	 */
	@Override
	public boolean checkCustomerFacingEntry(final OrderStatusNotificationModel osnm)
	{
		return notificationService.checkCustomerFacingEntry(osnm);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.account.register.NotificationFacade#getNotificationDetailForEmailID(java.lang.String)
	 */
	@Override
	public List<NotificationData> getNotificationDetailForEmailID(final String emailID, final boolean isDesktop)
	{
		try
		{
			final UserModel user = extendedUserService.getUserForOriginalUid(emailID);
			if (user != null)
			{
				return getNotificationDetail(user.getUid(), isDesktop);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return null;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.account.register.NotificationFacade#markNotificationRead(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void markNotificationRead(final String customerId, final String orderNo, final String consignmentNo,
			final String shopperStatus)
	{

		notificationService.markNotificationRead(customerId, orderNo, consignmentNo, shopperStatus);


	}

	@Override
	public void markNotificationReadForOriginalUid(final String emailId, final String orderNo, final String consignmentNo,
			final String shopperStatus)
	{

		final UserModel user = extendedUserService.getUserForOriginalUid(emailId);
		if (user != null)
		{
			markNotificationRead(user.getUid(), orderNo, consignmentNo, shopperStatus);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facades.account.register.NotificationFacade#getUnReadNotificationCount(java.util.List)
	 */
	@Override
	public Integer getUnReadNotificationCount(final List<NotificationData> notificationDatas)
	{
		Integer count = Integer.valueOf(0);
		try
		{
			if (notificationDatas != null)
			{
				for (final NotificationData notification : notificationDatas)
				{
					if (!notification.getNotificationRead().booleanValue())
					{
						count = Integer.valueOf(count.intValue() + 1);
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return count;
	}


}
