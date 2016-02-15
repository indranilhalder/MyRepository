/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.ProductCategoryRestrictionModel;
import de.hybris.platform.voucher.model.ProductRestrictionModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.core.model.VoucherStatusNotificationModel;
import com.tisl.mpl.data.AllVoucherListData;
import com.tisl.mpl.data.NotificationData;
import com.tisl.mpl.data.VoucherDisplayData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedEvent;
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
	@Autowired
	private VoucherModelService voucherModelService;
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
			final String shopperStatus)
	{
		final List<OrderStatusNotificationModel> notificationList = getNotificationDao().getModelforDetails(customerId, orderNo,
				consignmentNo, shopperStatus);
		final List<VoucherStatusNotificationModel> voucherList = getModelForVoucherIdentifier(orderNo);
		final Boolean isRead = Boolean.TRUE;
		for (final OrderStatusNotificationModel osn : notificationList)
		{
			osn.setIsRead(isRead);
			modelService.save(osn);

		}
		for (final VoucherStatusNotificationModel v : voucherList)
		{
			v.setIsRead(isRead);
			modelService.save(v);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getVoucher()
	 */

	@Override
	public List<VoucherStatusNotificationModel> getVoucher()
	{
		List<VoucherStatusNotificationModel> voucherList = new ArrayList<>();
		voucherList = getNotificationDao().findVoucher();
		if (null != voucherList)
		{
			for (final VoucherStatusNotificationModel v : voucherList)
			{
				if (v.getVoucherEndDate().before(new Date()))
				{
					modelService.remove(v);
				}
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.NotificationService#getAllVoucherList(de.hybris.platform.core
	 * .model.user.CustomerModel, java.util.List)
	 */
	@Override
	public AllVoucherListData getAllVoucherList(final CustomerModel currentCustomer, final List<VoucherModel> voucherList)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, YYYY");
		final List<VoucherDisplayData> openVoucherDataList = new ArrayList<VoucherDisplayData>();
		final List<VoucherDisplayData> closedVoucherDataList = new ArrayList<VoucherDisplayData>();
		final AllVoucherListData allVoucherListData = new AllVoucherListData();
		for (final VoucherModel voucherModel : voucherList)
		{
			if (voucherModel instanceof PromotionVoucherModel)
			{

				final PromotionVoucherModel VoucherObj = (PromotionVoucherModel) voucherModel;

				final Set<RestrictionModel> restrictionList = voucherModel.getRestrictions();
				if (CollectionUtils.isNotEmpty(restrictionList))
				{
					boolean dateRestrExists = false;
					boolean userRestrExists = false;
					final boolean semiClosedRestrExists = false;

					DateRestrictionModel dateRestrObj = null;
					UserRestrictionModel userRestrObj = null;

					List<ProductModel> specificProductCoupon = new ArrayList<ProductModel>();
					List<CategoryModel> categoryBasedCoupon = new ArrayList<CategoryModel>();
					//List<ProductModel> productForCategoryBasedCoupon = new ArrayList<ProductModel>();

					for (final RestrictionModel restrictionModel : restrictionList)
					{

						//final VoucherDisplayData voucherDisplayData = new VoucherDisplayData();
						if (restrictionModel instanceof DateRestrictionModel)
						{
							dateRestrExists = true;
							dateRestrObj = (DateRestrictionModel) restrictionModel;
						}
						if (restrictionModel instanceof UserRestrictionModel)
						{
							userRestrExists = true;
							userRestrObj = (UserRestrictionModel) restrictionModel;
						}

						if (restrictionModel instanceof ProductRestrictionModel)
						{
							final ProductRestrictionModel productRestriction = (ProductRestrictionModel) restrictionModel;
							specificProductCoupon = new ArrayList<ProductModel>(productRestriction.getProducts());

						}

						if (restrictionModel instanceof ProductCategoryRestrictionModel)
						{
							final ProductCategoryRestrictionModel categoryRestriction = (ProductCategoryRestrictionModel) restrictionModel;
							categoryBasedCoupon = new ArrayList<CategoryModel>(categoryRestriction.getCategories());

							//productForCategoryBasedCoupon = new ArrayList<ProductModel>(categoryRestriction.getProducts());
						}

					}

					if (dateRestrExists)
					{
						final String voucherCode = VoucherObj.getVoucherCode();

						if (dateRestrExists && voucherModelService.isReservable(voucherModel, voucherCode, currentCustomer))
						{
							final VoucherDisplayData voucherDisplayData = new VoucherDisplayData();
							if (userRestrExists)
							{
								//								final Collection<PrincipalModel> userList = userRestrObj != null ? userRestrObj.getUsers()
								//										: new ArrayList<PrincipalModel>();
								if (userRestrObj != null && userRestrObj.getUsers().contains(currentCustomer))
								{
									voucherDisplayData.setVoucherCode(VoucherObj.getVoucherCode());
									voucherDisplayData.setVoucherDescription(voucherModel.getDescription());
									final Date endDate = dateRestrObj.getEndDate() != null ? dateRestrObj.getEndDate() : new Date();
									voucherDisplayData.setVoucherExpiryDate(sdf.format(endDate));
									final Date startDate = dateRestrObj.getStartDate();
									voucherDisplayData.setVoucherCreationDate(startDate);
									voucherDisplayData.setProductsCoupon(specificProductCoupon);
									voucherDisplayData.setCategoryBasedCoupon(categoryBasedCoupon);
									closedVoucherDataList.add(voucherDisplayData);
								}
							}
							else if (!semiClosedRestrExists)
							{
								voucherDisplayData.setVoucherCode(VoucherObj.getVoucherCode());
								voucherDisplayData.setVoucherDescription(voucherModel.getDescription());
								final Date endDate = dateRestrObj.getEndDate() != null ? dateRestrObj.getEndDate() : new Date();
								voucherDisplayData.setVoucherExpiryDate(sdf.format(endDate));
								final Date startDate = dateRestrObj.getStartDate();
								voucherDisplayData.setVoucherCreationDate(startDate);
								openVoucherDataList.add(voucherDisplayData);
							}
						}
					}
				}
			}
		}
		allVoucherListData.setClosedVoucherList(closedVoucherDataList);
		allVoucherListData.setOpenVoucherList(openVoucherDataList);
		return allVoucherListData;

	}

	/**
	 * @Description This method saves data into VoucherStatusNotificationModel while creating voucher
	 * @param VoucherModel
	 */
	@Override
	public void saveToVoucherStatusNotification(final VoucherModel voucher)
	{
		final Boolean isRead = Boolean.FALSE;
		List<ProductModel> productAssociated = new ArrayList<ProductModel>();
		List<CategoryModel> categoryAssociated = new ArrayList<CategoryModel>();
		String voucherCode = "";
		String voucherIndentifier = "";
		Date voucherStartDate = null;
		Date voucherEndDate = null;
		final Set<RestrictionModel> restrictionList = voucher.getRestrictions();

		final List<PrincipalModel> userList = new ArrayList<PrincipalModel>();
		boolean userRestrExists = false;
		boolean dateRestrExists = false;
		UserRestrictionModel userRestrObj = null;

		for (final RestrictionModel restrictionModel : restrictionList)
		{
			if (restrictionModel instanceof UserRestrictionModel)
			{
				userRestrObj = (UserRestrictionModel) restrictionModel;
				userList.addAll((userRestrObj).getUsers());
				userRestrExists = true;
			}
			else if (restrictionModel instanceof DateRestrictionModel)
			{
				voucherStartDate = ((DateRestrictionModel) restrictionModel).getStartDate();
				voucherEndDate = ((DateRestrictionModel) restrictionModel).getEndDate();
				dateRestrExists = true;
			}
			else if (restrictionModel instanceof ProductCategoryRestrictionModel)
			{
				final ProductCategoryRestrictionModel categoryRestriction = (ProductCategoryRestrictionModel) restrictionModel;
				categoryAssociated = new ArrayList<CategoryModel>(categoryRestriction.getCategories());
			}
			else if (restrictionModel instanceof ProductRestrictionModel)
			{
				final ProductRestrictionModel productRestriction = (ProductRestrictionModel) restrictionModel;
				productAssociated = new ArrayList<ProductModel>(productRestriction.getProducts());
			}
		}

		VoucherStatusNotificationModel voucherStatus = null;
		final String customerStatus = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.CUSTOMER_STATUS_FOR_COUPON_NOTIFICATION);

		if (voucher instanceof PromotionVoucherModel)
		{
			final PromotionVoucherModel promoVoucher = (PromotionVoucherModel) voucher;
			voucherCode = promoVoucher.getVoucherCode();
			voucherIndentifier = promoVoucher.getCode();
			LOG.debug("voucher identifier :" + voucherIndentifier);
		}

		final List<String> userUidList = new ArrayList<String>();

		if (dateRestrExists && userRestrExists && userRestrObj != null && userRestrObj.getPositive().booleanValue())
		{
			final List<String> restrUserUidList = new ArrayList<String>();

			for (final PrincipalModel user : userList)
			{
				if (user instanceof UserGroupModel)
				{
					final UserGroupModel userGroup = (UserGroupModel) user;
					final List<PrincipalModel> grpMemberList = new ArrayList<PrincipalModel>(userGroup.getMembers());

					for (final PrincipalModel grpMember : grpMemberList)
					{
						restrUserUidList.add(grpMember.getUid());
					}
				}
				else if (user instanceof UserModel)
				{
					restrUserUidList.add(user.getUid());
				}
			}

			if (null != voucherIndentifier && null != voucherCode)
			{

				final List<VoucherStatusNotificationModel> existingVoucherList = getModelForVoucher(voucherIndentifier);

				if (existingVoucherList.isEmpty())
				{
					voucherStatus = modelService.create(VoucherStatusNotificationModel.class);
				}
				else
				{
					voucherStatus = existingVoucherList.get(0);
				}

				userUidList.addAll(restrUserUidList);

				//Setting values in model
				//voucherStatus.setIfUserRestrictionExist(Boolean.TRUE);
				voucherStatus.setVoucherIdentifier(voucherIndentifier);
				voucherStatus.setVoucherCode(voucherCode);
				voucherStatus.setCustomerUidList(userUidList);
				voucherStatus.setVoucherStartDate(voucherStartDate);
				voucherStatus.setVoucherEndDate(voucherEndDate);
				voucherStatus.setIsRead(isRead);
				voucherStatus.setCustomerStatus(customerStatus);
				voucherStatus.setCategoryAssociated(categoryAssociated);
				voucherStatus.setProductAssociated(productAssociated);
				modelService.save(voucherStatus);
			}
		}
		else if (dateRestrExists && (!userRestrExists || userRestrObj == null || !userRestrObj.getPositive().booleanValue()))
		{
			final List<VoucherStatusNotificationModel> existingVoucherList = getModelForVoucher(voucherIndentifier);
			if (!existingVoucherList.isEmpty())
			{
				voucherStatus = existingVoucherList.get(0);
				modelService.remove(voucherStatus);

			}
		}
	}

	private List<VoucherStatusNotificationModel> getModelForVoucher(final String voucherIndentifier)
	{

		return getNotificationDao().getModelForVoucher(voucherIndentifier);


	}

	private List<VoucherStatusNotificationModel> getModelForVoucherIdentifier(final String voucherCode)
	{

		return getNotificationDao().getModelForVoucherIdentifier(voucherCode);


	}
}
