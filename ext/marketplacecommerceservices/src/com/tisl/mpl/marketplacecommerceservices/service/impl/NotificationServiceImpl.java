/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelCreationException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.core.model.VoucherStatusNotificationModel;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedEvent;
import com.tisl.mpl.marketplacecommerceservices.service.CouponRestrictionService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.sns.push.service.MplSNSMobilePushService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.NotificationDataComparator;
import com.tisl.mpl.wsdto.PushNotificationData;


/**
 * @author TCS
 *
 */
public class NotificationServiceImpl implements NotificationService
{
	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private EventService eventService;

	@Autowired
	private MplSNSMobilePushService mplSNSMobilePushService;
	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "couponRestrictionService")
	private CouponRestrictionService couponRestrictionService;


	private static final Logger LOG = Logger.getLogger(NotificationServiceImpl.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getNotification()
	 */
	@Override
	public List<NotificationData> getNotification()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * Getting notificationDetails of logged User (non-Javadoc) (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getNotificationDetails(com.tisl.mpl.data.
	 * NotificationData)
	 */
	@Override
	public List<OrderStatusNotificationModel> getNotificationDetails(final String customerUID, final boolean isDesktop)
	{
		List<OrderStatusNotificationModel> notificationdetailslist = null;
		/*
		 * final CustomerModel currentCustomer = (CustomerModel) userService.getCurrentUser(); final String email =
		 * currentCustomer.getOriginalUid();
		 */
		try
		{
			notificationdetailslist = getNotificationDao().getNotificationDetail(customerUID, isDesktop);
			return notificationdetailslist;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		return notificationdetailslist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#checkCustomerFacingEntry(com.tisl.mpl.core
	 * .model.OrderStatusNotificationModel)
	 */
	@Override
	public boolean checkCustomerFacingEntry(final OrderStatusNotificationModel osnm)
	{
		boolean statusPresent = false;

		final List<OrderStatusNotificationModel> notificationList = getNotificationDao().checkCustomerFacingEntry(
				osnm.getCustomerUID(), osnm.getOrderNumber(), osnm.getTransactionId(), osnm.getCustomerStatus());
		if (notificationList != null && !notificationList.isEmpty())
		{
			statusPresent = true;
		}

		return statusPresent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#markNotificationRead(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void markNotificationRead(final String customerId, final String orderNo, final String consignmentNo,
			final String shopperStatus) throws EtailNonBusinessExceptions
	{
		try
		{
			final List<OrderStatusNotificationModel> notificationList = getNotificationDao().getModelforDetails(customerId, orderNo,
					consignmentNo, shopperStatus);
			final List<VoucherStatusNotificationModel> voucherList = getModelForVoucherIdentifier(orderNo);
			final Boolean isRead = Boolean.TRUE;
			for (final OrderStatusNotificationModel osn : notificationList)
			{
				osn.setIsRead(isRead);
				getModelService().save(osn);

			}
			for (final VoucherStatusNotificationModel vsn : voucherList)
			{
				vsn.setIsRead(isRead);
			}
			if (CollectionUtils.isNotEmpty(voucherList)) //Saving the voucherList
			{
				getModelService().saveAll(voucherList);
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#markNotificationRead(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<OrderStatusNotificationModel> isAlreadyNotified(final String customerId, final String orderNo,
			final String transactionId, final String orderStatus)
	{
		List<OrderStatusNotificationModel> notificationList = new ArrayList<OrderStatusNotificationModel>();
		try
		{
			notificationList = getNotificationDao().getNotification(customerId, orderNo, transactionId, orderStatus);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return notificationList;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			return notificationList;
		}
		return notificationList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#triggerEmailAndSmsOnOrderConfirmation(de.
	 * hybris.platform.core.model.order.OrderModel, java.lang.String)
	 */
	@Override
	public void triggerEmailAndSmsOnOrderConfirmation(final OrderModel orderDetails, final String trackorderurl)
			throws JAXBException
	{
		//Moved the SMS service to EventListener
		//final String mobileNumber = orderDetails.getDeliveryAddress().getPhone1();
		//final String firstName = orderDetails.getDeliveryAddress().getFirstname();
		//final String orderReferenceNumber = orderDetails.getCode();
		//final String trackingUrl = configurationService.getConfiguration().getString(
		//		MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
		//		+ orderReferenceNumber;

		if (orderDetails.getStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL))
		{
			final OrderProcessModel orderProcessModel = new OrderProcessModel();
			orderProcessModel.setOrder(orderDetails);
			orderProcessModel.setOrderTrackUrl(trackorderurl);
			final OrderPlacedEvent orderplacedEvent = new OrderPlacedEvent(orderProcessModel);
			try
			{
				eventService.publishEvent(orderplacedEvent);
			}
			catch (final Exception e1)
			{ // YTODO
			  // Auto-generated catch block
				LOG.error("Exception during sending mail or SMS >> " + e1.getMessage());
			}

			//Moved the SMS service to OrderConfirmationEventListener
			//			try
			//			{
			//				final String content = MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED
			//						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
			//						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderReferenceNumber)
			//						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, trackingUrl);
			//
			//				final SendSMSRequestData smsRequestData = new SendSMSRequestData();
			//				smsRequestData.setSenderID(MarketplacecommerceservicesConstants.SMS_SENDER_ID);
			//				smsRequestData.setContent(content);
			//				smsRequestData.setRecipientPhoneNumber(mobileNumber);
			//				sendSMSService.sendSMS(smsRequestData);
			//
			//			}
			//			catch (final EtailNonBusinessExceptions ex)
			//			{
			//				LOG.error("EtailNonBusinessExceptions occured while sending sms " + ex);
			//			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#sendMobileNotifications(de.hybris.platform
	 * .core.model.order.OrderModel)
	 */
	@Override
	public void sendMobileNotifications(final OrderModel orderDetails)
	{
		try
		{
			String orderReferenceNumber = null;

			if (null != orderDetails.getCode() && !orderDetails.getCode().isEmpty())
			{
				orderReferenceNumber = orderDetails.getCode();
			}
			String uid = null;
			if (null != orderDetails.getUser() && null != orderDetails.getUser().getUid()
					&& !orderDetails.getUser().getUid().isEmpty())
			{
				uid = orderDetails.getUser().getUid();
			}
			PushNotificationData pushData = null;
			CustomerModel customer = getModelService().create(CustomerModel.class);
			if (null != uid && !uid.isEmpty())
			{
				customer = mplSNSMobilePushService.getCustForUId(uid);
				if (null != customer && null != customer.getDeviceKey() && !customer.getDeviceKey().isEmpty())
				{
					pushData = new PushNotificationData();
					if (null != orderReferenceNumber)
					{
						pushData.setMessage(MarketplacecommerceservicesConstants.PUSH_MESSAGE_ORDER_PLACED.replace(
								MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderReferenceNumber));
						pushData.setOrderId(orderReferenceNumber);
					}
					if (null != customer.getOriginalUid() && !customer.getOriginalUid().isEmpty() && null != customer.getIsActive()
							&& !customer.getIsActive().isEmpty() && customer.getIsActive().equalsIgnoreCase("Y"))
					{
						mplSNSMobilePushService.setUpNotification(customer.getOriginalUid(), pushData);
					}
				}
			}

		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9032);
		}

	}


	/**
	 * @return List<VoucherStatusNotificationModel>
	 */
	@Override
	public List<VoucherStatusNotificationModel> getVoucher() throws EtailNonBusinessExceptions
	{
		List<VoucherStatusNotificationModel> voucherList = new ArrayList<>();
		try
		{
			voucherList = getNotificationDao().findVoucher();
			if (null != voucherList)
			{
				for (final VoucherStatusNotificationModel voucher : voucherList)
				{
					if (voucher.getVoucherEndDate().before(new Date()))
					{
						getModelService().remove(voucher);
					}
				}
			}
		}
		catch (final ModelRemovalException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0020);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return voucherList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getPromotion()
	 */
	@Override
	public List<AbstractPromotionModel> getPromotion()
	{
		// YTODO Auto-generated method stub
		final ArrayList<AbstractPromotionModel> promotionList = new ArrayList<AbstractPromotionModel>();
		final List<AbstractPromotionModel> promotionColl = getNotificationDao().getPromotion();

		if (CollectionUtils.isNotEmpty(promotionColl))
		{
			promotionList.addAll(promotionColl);
		}

		return promotionList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getSortedNotificationData(java.util.List)
	 */
	@Override
	public List<NotificationData> getSortedNotificationData(final List<NotificationData> notificationDataList)
	{
		Collections.sort(notificationDataList, new NotificationDataComparator());
		return notificationDataList;
	}

	/**
	 *
	 */
	//	@Override
	//	public AllVoucherListData getAllVoucherList(final CustomerModel currentCustomer, final List<VoucherModel> voucherList)
	//	{
	//		final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, YYYY");
	//		final List<VoucherDisplayData> openVoucherDataList = new ArrayList<VoucherDisplayData>();
	//		final List<VoucherDisplayData> closedVoucherDataList = new ArrayList<VoucherDisplayData>();
	//		final AllVoucherListData allVoucherListData = new AllVoucherListData();
	//		for (final VoucherModel voucherModel : voucherList)
	//		{
	//			if (voucherModel instanceof PromotionVoucherModel)
	//			{
	//
	//				final PromotionVoucherModel VoucherObj = (PromotionVoucherModel) voucherModel;
	//
	//				final Set<RestrictionModel> restrictionList = voucherModel.getRestrictions();
	//				if (CollectionUtils.isNotEmpty(restrictionList))
	//				{
	//					boolean dateRestrExists = false;
	//					boolean userRestrExists = false;
	//					final boolean semiClosedRestrExists = false;
	//
	//					DateRestrictionModel dateRestrObj = null;
	//					UserRestrictionModel userRestrObj = null;
	//
	//					List<ProductModel> specificProductCoupon = new ArrayList<ProductModel>();
	//					List<CategoryModel> categoryBasedCoupon = new ArrayList<CategoryModel>();
	//					//List<ProductModel> productForCategoryBasedCoupon = new ArrayList<ProductModel>();
	//
	//					for (final RestrictionModel restrictionModel : restrictionList)
	//					{
	//
	//						//final VoucherDisplayData voucherDisplayData = new VoucherDisplayData();
	//						if (restrictionModel instanceof DateRestrictionModel)
	//						{
	//							dateRestrExists = true;
	//							dateRestrObj = (DateRestrictionModel) restrictionModel;
	//						}
	//						if (restrictionModel instanceof UserRestrictionModel)
	//						{
	//							userRestrExists = true;
	//							userRestrObj = (UserRestrictionModel) restrictionModel;
	//						}
	//
	//						if (restrictionModel instanceof ProductRestrictionModel)
	//						{
	//							final ProductRestrictionModel productRestriction = (ProductRestrictionModel) restrictionModel;
	//							specificProductCoupon = new ArrayList<ProductModel>(productRestriction.getProducts());
	//
	//						}
	//
	//						if (restrictionModel instanceof ProductCategoryRestrictionModel)
	//						{
	//							final ProductCategoryRestrictionModel categoryRestriction = (ProductCategoryRestrictionModel) restrictionModel;
	//							categoryBasedCoupon = new ArrayList<CategoryModel>(categoryRestriction.getCategories());
	//
	//							//productForCategoryBasedCoupon = new ArrayList<ProductModel>(categoryRestriction.getProducts());
	//						}
	//
	//					}
	//
	//					if (dateRestrExists)
	//					{
	//						final String voucherCode = VoucherObj.getVoucherCode();
	//
	//						if (dateRestrExists && voucherModelService.isReservable(voucherModel, voucherCode, currentCustomer))
	//						{
	//							final VoucherDisplayData voucherDisplayData = new VoucherDisplayData();
	//							if (userRestrExists)
	//							{
	//								//								final Collection<PrincipalModel> userList = userRestrObj != null ? userRestrObj.getUsers()
	//								//										: new ArrayList<PrincipalModel>();
	//								if (userRestrObj != null && userRestrObj.getUsers().contains(currentCustomer))
	//								{
	//									voucherDisplayData.setVoucherCode(VoucherObj.getVoucherCode());
	//									voucherDisplayData.setVoucherDescription(voucherModel.getDescription());
	//									final Date endDate = dateRestrObj.getEndDate() != null ? dateRestrObj.getEndDate() : new Date();
	//									voucherDisplayData.setVoucherExpiryDate(sdf.format(endDate));
	//									final Date startDate = dateRestrObj.getStartDate();
	//									voucherDisplayData.setVoucherCreationDate(startDate);
	//									voucherDisplayData.setProductsCoupon(specificProductCoupon);
	//									voucherDisplayData.setCategoryBasedCoupon(categoryBasedCoupon);
	//									closedVoucherDataList.add(voucherDisplayData);
	//								}
	//							}
	//							else if (!semiClosedRestrExists)
	//							{
	//								voucherDisplayData.setVoucherCode(VoucherObj.getVoucherCode());
	//								voucherDisplayData.setVoucherDescription(voucherModel.getDescription());
	//								final Date endDate = dateRestrObj.getEndDate() != null ? dateRestrObj.getEndDate() : new Date();
	//								voucherDisplayData.setVoucherExpiryDate(sdf.format(endDate));
	//								final Date startDate = dateRestrObj.getStartDate();
	//								voucherDisplayData.setVoucherCreationDate(startDate);
	//								openVoucherDataList.add(voucherDisplayData);
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//		allVoucherListData.setClosedVoucherList(closedVoucherDataList);
	//		allVoucherListData.setOpenVoucherList(openVoucherDataList);
	//		return allVoucherListData;
	//
	//	}

	/**
	 * @Description This method saves data into VoucherStatusNotificationModel while creating voucher
	 * @param voucher
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void saveToVoucherStatusNotification(final VoucherModel voucher) throws EtailNonBusinessExceptions
	{
		try
		{
			final Boolean isRead = Boolean.FALSE;
			String voucherCode = null;
			String voucherIndentifier = null;
			VoucherStatusNotificationModel voucherStatus = null;
			final String customerStatus = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.CUSTOMER_STATUS_FOR_COUPON_NOTIFICATION);

			if (voucher instanceof PromotionVoucherModel)
			{
				final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) voucher;
				voucherCode = promoVoucher.getVoucherCode();
				voucherIndentifier = promoVoucher.getCode();
				LOG.debug("voucher identifier :" + voucherIndentifier);
				List<VoucherStatusNotificationModel> existingVoucherList = new ArrayList<VoucherStatusNotificationModel>();
				if (StringUtils.isNotEmpty(voucherIndentifier))
				{
					existingVoucherList = getModelForVoucher(voucherIndentifier);
				}
				final DateRestrictionModel dateRestrObj = getCouponRestrictionService().getDateRestriction(voucher);
				final UserRestrictionModel userRestrObj = getCouponRestrictionService().getUserRestriction(voucher);
				final List<PrincipalModel> userList = userRestrObj != null ? getCouponRestrictionService()
						.getRestrictionCustomerList(userRestrObj) : new ArrayList<PrincipalModel>();
				final List<String> restrUserUidList = new ArrayList<String>();

				if (dateRestrObj != null && userRestrObj != null && userRestrObj.getPositive().booleanValue()
						&& CollectionUtils.isNotEmpty(userList))
				{
					for (final PrincipalModel user : userList)
					{
						restrUserUidList.add(user.getUid());
					}

					if (null != voucherIndentifier && null != voucherCode)
					{
						if (CollectionUtils.isEmpty(existingVoucherList))
						{
							voucherStatus = getModelService().create(VoucherStatusNotificationModel.class);
						}
						else
						{
							voucherStatus = existingVoucherList.get(0);
						}

						//Setting values in model
						voucherStatus.setVoucherIdentifier(voucherIndentifier);
						voucherStatus.setVoucherCode(voucherCode);
						voucherStatus.setCustomerUidList(restrUserUidList);
						voucherStatus.setVoucherStartDate(dateRestrObj.getStartDate());
						voucherStatus.setVoucherEndDate(dateRestrObj.getEndDate());
						voucherStatus.setIsRead(isRead);
						voucherStatus.setCustomerStatus(customerStatus);
						getModelService().save(voucherStatus);
					}
				}
				else if (dateRestrObj != null
						&& (userRestrObj == null || !userRestrObj.getPositive().booleanValue() || CollectionUtils.isEmpty(userList))
						&& CollectionUtils.isNotEmpty(existingVoucherList))
				{
					voucherStatus = existingVoucherList.get(0);
					getModelService().remove(voucherStatus);
				}
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelCreationException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0020);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}



	/**
	 * This method returns list of VoucherStatusNotificationModel based on voucherIndentifier
	 *
	 * @param voucherIndentifier
	 * @return List<VoucherStatusNotificationModel>
	 * @throws EtailNonBusinessExceptions
	 */
	private List<VoucherStatusNotificationModel> getModelForVoucher(final String voucherIndentifier)
			throws EtailNonBusinessExceptions
	{
		return getNotificationDao().getModelForVoucher(voucherIndentifier);
	}


	/**
	 * This method returns list of VoucherStatusNotificationModel based on voucherCode
	 *
	 * @param voucherCode
	 * @return List<VoucherStatusNotificationModel>
	 * @throws EtailNonBusinessExceptions
	 */
	private List<VoucherStatusNotificationModel> getModelForVoucherIdentifier(final String voucherCode)
			throws EtailNonBusinessExceptions
	{
		return getNotificationDao().getModelForVoucherIdentifier(voucherCode);
	}

	/**
	 * @return the voucherModelService
	 */
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}

	/**
	 * @param voucherModelService
	 *           the voucherModelService to set
	 */
	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}

	/**
	 * @return the couponRestrictionService
	 */
	public CouponRestrictionService getCouponRestrictionService()
	{
		return couponRestrictionService;
	}

	/**
	 * @param couponRestrictionService
	 *           the couponRestrictionService to set
	 */
	public void setCouponRestrictionService(final CouponRestrictionService couponRestrictionService)
	{
		this.couponRestrictionService = couponRestrictionService;
	}


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}



	/**
	 * @return the notificationDao
	 */
	public NotificationDao getNotificationDao()
	{
		return notificationDao;
	}

	/**
	 * @param notificationDao
	 *           the notificationDao to set
	 */
	public void setNotificationDao(final NotificationDao notificationDao)
	{
		this.notificationDao = notificationDao;
	}

	@Resource(name = "modelService")
	private ModelService modelService;


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
