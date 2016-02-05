/**
 *
 */
package com.tisl.mpl.facades.account.register.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.core.model.VoucherStatusNotificationModel;
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
	@Autowired
	private Converter<VoucherStatusNotificationModel, NotificationData> trackOrderCouponConverter;
	@Autowired
	private ConfigurationService configurationService;
	protected static final Logger LOG = Logger.getLogger(NotificationFacadeImpl.class);

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}



	/**
	 * @return the trackOrderCouponConverter
	 */
	public Converter<VoucherStatusNotificationModel, NotificationData> getTrackOrderCouponConverter()
	{
		return trackOrderCouponConverter;
	}

	/**
	 * @param trackOrderCouponConverter
	 *           the trackOrderCouponConverter to set
	 */
	public void setTrackOrderCouponConverter(
			final Converter<VoucherStatusNotificationModel, NotificationData> trackOrderCouponConverter)
	{
		this.trackOrderCouponConverter = trackOrderCouponConverter;
	}


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
		final List<VoucherStatusNotificationModel> voucherList = getAllCoupons();
		List<NotificationData> notificationDataList = new ArrayList<>();
		for (final OrderStatusNotificationModel osnm : notificationModel)
		{
			final NotificationData tempnotificationData = trackOrderConverter.convert(osnm);
			notificationDataList.add(tempnotificationData);
		}

		for (final VoucherStatusNotificationModel v : voucherList)
		{
			if (v.getCustomerUidList().contains(customerUID))
			{
				final NotificationData dataForVoucher = trackOrderCouponConverter.convert(v);
				notificationDataList.add(dataForVoucher);
			}
		}

		notificationDataList = notificationService.getSortedNotificationData(notificationDataList);
		String couponCount = null;
		if (isDesktop)
		{
			couponCount = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.NOTIFICATION_COUNT);
		}
		else
		{
			couponCount = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.NOTIFICATION_COUNT_MOBILE);
		}
		int count = 0;
		try
		{
			count = Integer.parseInt(couponCount);
		}
		catch (final NumberFormatException e)
		{
			LOG.debug("Number format exception occured while parsing");
		}
		if (notificationDataList.size() > count)
		{
			notificationDataList.subList(count, notificationDataList.size()).clear();
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

	private List<VoucherStatusNotificationModel> getAllCoupons()
	{
		final List<VoucherStatusNotificationModel> voucherList = new ArrayList<VoucherStatusNotificationModel>();
		final List<VoucherStatusNotificationModel> voucherColl = notificationService.getVoucher();
		if (CollectionUtils.isNotEmpty(voucherColl))
		{
			voucherList.addAll(voucherColl);
		}
		return voucherList;
	}


}
