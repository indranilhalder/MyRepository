/**
 *
 */
package com.tisl.mpl.facades.account.register.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.data.AllVoucherListData;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.data.VoucherDisplayData;
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
	private Converter<VoucherDisplayData, NotificationData> trackOrderCouponConverter;
	@Autowired
	private Converter<AbstractPromotionModel, NotificationData> trackOrderPromotionConverter;
	@Autowired
	private UserService userService;
	@Autowired
	private ConfigurationService configurationService;

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
	public Converter<VoucherDisplayData, NotificationData> getTrackOrderCouponConverter()
	{
		return trackOrderCouponConverter;
	}

	/**
	 * @param trackOrderCouponConverter
	 *           the trackOrderCouponConverter to set
	 */
	public void setTrackOrderCouponConverter(final Converter<VoucherDisplayData, NotificationData> trackOrderCouponConverter)
	{
		this.trackOrderCouponConverter = trackOrderCouponConverter;
	}

	/**
	 * @return the trackOrderPromotionConverter
	 */
	public Converter<AbstractPromotionModel, NotificationData> getTrackOrderPromotionConverter()
	{
		return trackOrderPromotionConverter;
	}

	/**
	 * @param trackOrderPromotionConverter
	 *           the trackOrderPromotionConverter to set
	 */
	public void setTrackOrderPromotionConverter(
			final Converter<AbstractPromotionModel, NotificationData> trackOrderPromotionConverter)
	{
		this.trackOrderPromotionConverter = trackOrderPromotionConverter;
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

		final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser();
		final List<OrderStatusNotificationModel> notificationModel = notificationService.getNotificationDetails(customerUID,
				isDesktop);
		final List<AbstractPromotionModel> promotionList = notificationService.getPromotion();
		final List<VoucherModel> voucherList = getAllCoupons();
		final AllVoucherListData allVoucherList = notificationService.getAllVoucherList(currentCustomer, voucherList);



		/*
		 * if (null != promotionList) { for (final AbstractPromotionModel promotion : promotionList) { final
		 * PromotionNotificationModel trackOrderPromotion = new PromotionNotificationModel();
		 *
		 * trackOrderPromotion.setPromotionIdentifier(promotion.getCode());
		 * trackOrderPromotion.setPromotionDescription(promotion.getDescription());
		 * trackOrderPromotion.setPromotionStartDate(promotion.getStartDate());
		 * trackOrderPromotion.setPromotionStatus("Promotion @ is available"); modelService.save(trackOrderPromotion);
		 *
		 * promotionModel.add(trackOrderPromotion); }
		 *
		 * }
		 */

		List<VoucherDisplayData> openVoucherDataList = new ArrayList<VoucherDisplayData>();
		List<VoucherDisplayData> closedVoucherDataList = new ArrayList<VoucherDisplayData>();
		//final List<CouponNotificationModel> couponList = new ArrayList<>();

		if (null != allVoucherList)
		{

			openVoucherDataList = allVoucherList.getOpenVoucherList();
			closedVoucherDataList = allVoucherList.getClosedVoucherList();
			/*
			 * if (null != openVoucherDataList) { for (final VoucherDisplayData v : openVoucherDataList) { final
			 * CouponNotificationModel trackOrderCoupon = new CouponNotificationModel();
			 *
			 * trackOrderCoupon.setCouponCode(v.getVoucherCode());
			 * trackOrderCoupon.setCouponStartDate(v.getVoucherCreationDate());
			 * trackOrderCoupon.setCouponStatus("Coupon @ is available"); modelService.save(trackOrderCoupon);
			 * couponList.add(trackOrderCoupon);
			 *
			 * }
			 *
			 *
			 * }
			 */

			/*
			 * if (null != closedVoucherDataList) { for (final VoucherDisplayData v : closedVoucherDataList) { final
			 * CouponNotificationModel trackOrderCoupon = new CouponNotificationModel();
			 *
			 * trackOrderCoupon.setCouponCode(v.getVoucherCode());
			 * trackOrderCoupon.setCouponStartDate(v.getVoucherCreationDate());
			 * trackOrderCoupon.setCouponStatus("Coupon @ is available"); modelService.save(trackOrderCoupon);
			 * couponList.add(trackOrderCoupon);
			 *
			 * }
			 */
		}




		List<NotificationData> notificationDataList = new ArrayList<>();


		for (final OrderStatusNotificationModel osnm : notificationModel)
		{
			final NotificationData tempnotificationData = trackOrderConverter.convert(osnm);

			notificationDataList.add(tempnotificationData);
		}

		for (final VoucherDisplayData v : openVoucherDataList)
		{
			final NotificationData dataForVoucher = trackOrderCouponConverter.convert(v);
			notificationDataList.add(dataForVoucher);
		}

		for (final VoucherDisplayData v : closedVoucherDataList)
		{
			final NotificationData dataForVoucher = trackOrderCouponConverter.convert(v);
			notificationDataList.add(dataForVoucher);
		}


		for (final AbstractPromotionModel promotion : promotionList)
		{
			final NotificationData promotionData = trackOrderPromotionConverter.convert(promotion);
			notificationDataList.add(promotionData);
		}


		notificationDataList = notificationService.getSortedNotificationData(notificationDataList);


		int couponCount = 0;
		if (isDesktop)
		{


			couponCount = Integer.valueOf(
					getConfigurationService().getConfiguration().getString("notification.display.topCount", "5")).intValue();

		}
		else
		{
			couponCount = Integer.valueOf(
					getConfigurationService().getConfiguration().getString("notification.display.topCountMobile", "5")).intValue();

		}

		if (notificationDataList.size() > couponCount)
		{
			notificationDataList.subList(couponCount, notificationDataList.size()).clear();
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

	public List<VoucherModel> getAllCoupons()
	{
		final List<VoucherModel> voucherList = new ArrayList<VoucherModel>();
		final List<VoucherModel> voucherColl = notificationService.getVoucher();
		if (CollectionUtils.isNotEmpty(voucherColl))
		{
			voucherList.addAll(voucherColl);
		}
		return voucherList;
	}


}
