/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.JuspayWebHookDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplVoucherDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCheckoutService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
public class MplProcessOrderServiceImpl implements MplProcessOrderService
{
	private static final Logger LOG = Logger.getLogger(MplProcessOrderServiceImpl.class);

	@Resource(name = "mplProcessOrderDao")
	private MplProcessOrderDao mplProcessOrderDao;
	@Autowired
	private MplCommerceCartService mplCommerceCartService;
	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "mplOrderDao")
	private MplOrderDao mplOrderDao;
	private OrderService orderService;
	@Autowired
	private MplCommerceCheckoutService mplCommerceCheckoutService;
	@Resource(name = "juspayWebHookDao")
	private JuspayWebHookDao juspayWebHookDao;
	private Converter<JuspayOrderStatusModel, GetOrderStatusResponse> juspayOrderResponseConverter;
	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private NotificationService notificationService;
	@Resource(name = "mplVoucherService")
	private MplVoucherService mplVoucherService;
	@Resource(name = "mplVoucherDao")
	private MplVoucherDao mplVoucherDao;
	private static final String ERROR_NOTIF = "Error while sending notifications>>>>>>";

	/**
	 * This method processes pending orders
	 */
	@Override
	public void processPaymentPedingOrders()
	{
		List<OrderModel> orders = new ArrayList<OrderModel>();
		try
		{
			//DAO call to fetch PAYMENT PENDING orders
			orders = getMplProcessOrderDao().getPaymentPedingOrders(OrderStatus.PAYMENT_PENDING.toString());
		}
		catch (final EtailNonBusinessExceptions e) //IQA for TPR-629
		{
			LOG.error("Error in getPaymentPedingOrders while fetching pending orders================================", e);
		}
		catch (final Exception e)
		{
			LOG.error("Error in getPaymentPedingOrders while fetching pending orders================================", e);
		}

		if (CollectionUtils.isNotEmpty(orders))
		{
			//getting list of Juspay req ids for Payment Pending orders
			for (final OrderModel orderModel : orders)
			{
				try
				{
					final String cartGuid = orderModel.getGuid();
					MplPaymentAuditModel auditModel = null;
					if (StringUtils.isNotEmpty(cartGuid)) //IQA for TPR-629
					{
						auditModel = getMplOrderDao().getAuditList(cartGuid);
					}

					if (null != auditModel && StringUtils.isNotEmpty(auditModel.getAuditId()))
					{
						//to check webhook entry status at Juspay corresponding to Payment Pending orders
						final List<JuspayWebhookModel> hooks = checkstatusAtJuspay(auditModel.getAuditId());

						Date orderTATForTimeout = new Date();
						//getting the TAT
						final Double juspayWebhookRetryTAT = getJuspayWebhookRetryTAT();
						if (null != juspayWebhookRetryTAT && juspayWebhookRetryTAT.doubleValue() > 0)
						{
							final Calendar cal = Calendar.getInstance();
							cal.setTime(orderModel.getCreationtime());
							//adding the TAT to order modified time to get the time upto which the Payment Pending Orders will be checked for Processing
							cal.add(Calendar.MINUTE, +juspayWebhookRetryTAT.intValue());
							orderTATForTimeout = cal.getTime();
						}

						if ((new Date()).before(orderTATForTimeout) && CollectionUtils.isNotEmpty(hooks))
						{
							final List<JuspayWebhookModel> uniqueList = new ArrayList<JuspayWebhookModel>();
							for (final JuspayWebhookModel oModel : hooks)
							{
								if (null != oModel.getOrderStatus() && oModel.getIsExpired().booleanValue())
								{
									//getting all the webhook data where isExpired is Y and adding into a list
									uniqueList.add(oModel);
								}
							}

							for (final JuspayWebhookModel juspayWebhookModel : hooks)
							{
								if (CollectionUtils.isNotEmpty(uniqueList) && !juspayWebhookModel.getIsExpired().booleanValue())
								{
									//iterating through the new list against the whole webhook data list
									boolean duplicateFound = false;
									for (final JuspayWebhookModel unique : uniqueList)
									{
										//if there is duplicate order id which is not expired(N) then setting it to Y
										if (unique != null
												&& unique.getOrderStatus() != null
												&& juspayWebhookModel.getOrderStatus() != null
												&& StringUtils.equalsIgnoreCase(unique.getOrderStatus().getOrderId(), juspayWebhookModel
														.getOrderStatus().getOrderId())
												&& ((StringUtils.equalsIgnoreCase(juspayWebhookModel.getEventName(), "ORDER_SUCCEEDED") || (StringUtils
														.equalsIgnoreCase(juspayWebhookModel.getEventName(), "ORDER_FAILED")))))

										{
											duplicateFound = true;
											break;
										}
									}
									if (duplicateFound)
									{
										juspayWebhookModel.setIsExpired(Boolean.TRUE);
										getModelService().save(juspayWebhookModel);
									}
									else
									{
										//Change order status & process order based on Webhook status
										takeActionAgainstOrder(juspayWebhookModel, orderModel, true);
										uniqueList.add(juspayWebhookModel);
									}
								}
								else
								{
									//Change order status & process order based on Webhook status
									takeActionAgainstOrder(juspayWebhookModel, orderModel, true);
									uniqueList.add(juspayWebhookModel);
								}
							}
						}
						else if ((new Date()).after(orderTATForTimeout) && CollectionUtils.isEmpty(hooks))
						{
							//getting PinCode against Order
							final String defaultPinCode = getPinCodeForOrder(orderModel);

							//OMS Deallocation call for failed order
							getMplCommerceCartService().isInventoryReserved(orderModel,
									MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

							getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_TIMEOUT);
							//Code to remove coupon for Payment_Timeout orders
							if (CollectionUtils.isNotEmpty(orderModel.getDiscounts()))
							{
								final PromotionVoucherModel voucherModel = (PromotionVoucherModel) orderModel.getDiscounts().get(0);
								getMplVoucherService().releaseVoucher(voucherModel.getVoucherCode(), null, orderModel);
								getMplVoucherService().recalculateCartForCoupon(null, orderModel);
							}

							//Email and sms for Payment_Timeout
							final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
									MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
									+ orderModel.getCode();
							try
							{
								getNotificationService().triggerEmailAndSmsOnPaymentTimeout(orderModel, trackOrderUrl);
							}
							catch (final JAXBException e)
							{
								LOG.error(ERROR_NOTIF, e);
							}
							catch (final Exception ex)
							{
								LOG.error(ERROR_NOTIF, ex);
							}

						}
						else if ((new Date()).after(orderTATForTimeout) && CollectionUtils.isNotEmpty(hooks))
						{
							for (final JuspayWebhookModel juspayWebhookModel : hooks)
							{
								if (!juspayWebhookModel.getIsExpired().booleanValue())
								{
									takeActionAgainstOrder(juspayWebhookModel, orderModel, false);
								}
							}
						}
						else if ((new Date()).before(orderTATForTimeout) && CollectionUtils.isEmpty(hooks))
						{
							if (null != orderModel.getIsPendingNotSent() && !orderModel.getIsPendingNotSent().booleanValue())
							{
								//Email and sms for Payment_Pending
								boolean flag = false;
								final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
										MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
										+ orderModel.getCode();
								try
								{
									flag = getNotificationService().triggerEmailAndSmsOnPaymentPending(orderModel, trackOrderUrl);
								}
								catch (final JAXBException e)
								{
									LOG.error(ERROR_NOTIF, e);
								}
								catch (final Exception ex)
								{
									LOG.error(ERROR_NOTIF, ex);
								}

								orderModel.setIsPendingNotSent(Boolean.valueOf(flag));
								getModelService().save(orderModel);
							}

						}
					}

				}
				catch (final InvalidCartException | CalculationException | EtailNonBusinessExceptions | VoucherOperationException e)
				{
					LOG.error("Error in getPaymentPedingOrders================================", e);
				}
				catch (final Exception e)
				{
					LOG.error("Error in getPaymentPedingOrders================================", e);
				}
			}
		}

	}

	/**
	 * This method returns list of JuspayWebhookModel against juspayOrderId
	 *
	 * @param juspayOrder
	 * @return List<JuspayWebhookModel>
	 */
	private List<JuspayWebhookModel> checkstatusAtJuspay(final String juspayOrder)
	{
		return getMplProcessOrderDao().getEventsForPendingOrders(juspayOrder);
	}



	/**
	 * This method returns pincode for the orderModel
	 *
	 * @param orderModel
	 * @return String
	 */
	private String getPinCodeForOrder(final OrderModel orderModel)
	{
		String defaultPinCode = "".intern();
		if (null != orderModel.getDeliveryAddress() && StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getPostalcode()))
		{
			defaultPinCode = orderModel.getDeliveryAddress().getPostalcode();
		}
		else
		{
			/*
			 * This block will execute only incase of standalone CNC cart and OMS is not using pincode to deallocate cart
			 * reservation. As pincode is mandatory in Inventory reservation adding dummy pincode for cart deallocation
			 */
			defaultPinCode = MarketplacecommerceservicesConstants.PINCODE;
		}

		return defaultPinCode;
	}



	/**
	 * This method takes action against all orders
	 *
	 * @param juspayWebhookModel
	 * @param orderModel
	 * @param positive
	 * @throws CalculationException
	 * @throws InvalidCartException
	 * @throws VoucherOperationException
	 */
	private void takeActionAgainstOrder(final JuspayWebhookModel juspayWebhookModel, final OrderModel orderModel,
			final boolean positive) throws InvalidCartException, CalculationException, EtailNonBusinessExceptions,
			VoucherOperationException
	{
		final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
				+ orderModel.getCode();
		if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_SUCCEEDED")
				&& !juspayWebhookModel.getIsExpired().booleanValue() && positive)
		{
			if (null == orderModel.getPaymentInfo())
			{
				updateOrder(orderModel, juspayWebhookModel);

				//Re-trigger submit order process from Payment_Pending to Payment_Successful
				final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
				parameter.setEnableHooks(true);
				parameter.setSalesApplication(orderModel.getSalesApplication());

				final CommerceOrderResult result = new CommerceOrderResult();
				result.setOrder(orderModel);

				mplCommerceCheckoutService.beforeSubmitOrder(parameter, result);
				getOrderService().submitOrder(orderModel);

				//Email and sms for Payment_Successful
				try
				{
					getNotificationService().triggerEmailAndSmsOnOrderConfirmation(orderModel, trackOrderUrl);
				}
				catch (final JAXBException e)
				{
					LOG.error("Error while sending notifications from job>>>>>>", e);
				}
				catch (final Exception ex)
				{
					LOG.error(ERROR_NOTIF, ex);
				}
			}

			juspayWebhookModel.setIsExpired(Boolean.TRUE);
		}
		else if (juspayWebhookModel.getEventName().equalsIgnoreCase("ORDER_FAILED")
				&& !juspayWebhookModel.getIsExpired().booleanValue() && !positive)
		{
			//Added for failure transactions
			if (null == orderModel.getPaymentInfo())
			{
				updateOrder(orderModel, juspayWebhookModel);

				//getting PinCode against Order
				final String defaultPinCode = getPinCodeForOrder(orderModel);

				//OMS Deallocation call for failed order
				getMplCommerceCartService().isInventoryReserved(orderModel,
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

				getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_FAILED);

				if (CollectionUtils.isNotEmpty(orderModel.getDiscounts()))
				{
					final PromotionVoucherModel voucherModel = (PromotionVoucherModel) orderModel.getDiscounts().get(0);
					getMplVoucherService().releaseVoucher(voucherModel.getVoucherCode(), null, orderModel);
					getMplVoucherService().recalculateCartForCoupon(null, orderModel);
				}
				//Email and sms for Payment_Failed
				try
				{
					getNotificationService().triggerEmailAndSmsOnPaymentFailed(orderModel, trackOrderUrl);
				}
				catch (final JAXBException e)
				{
					LOG.error("Error while sending notifications from job>>>>>>", e);
				}
				catch (final Exception ex)
				{
					LOG.error(ERROR_NOTIF, ex);
				}
			}

			juspayWebhookModel.setIsExpired(Boolean.TRUE);
		}
	}

	/**
	 * This method returns the tat for processing the order in the job
	 *
	 * @return Double
	 */
	private Double getJuspayWebhookRetryTAT()
	{
		final BaseStoreModel baseStore = getJuspayWebHookDao().getJobTAT();
		if (null != baseStore && null != baseStore.getJuspayWebhookRetryTAT()
				&& baseStore.getJuspayWebhookRetryTAT().doubleValue() > 0)
		{
			return baseStore.getJuspayWebhookRetryTAT();
		}
		else
		{
			return Double.valueOf(0.0);
		}
	}






	/**
	 * This method updates Order after successfully executed in job
	 *
	 * @param orderModel
	 * @param juspayWebhookModel
	 */
	private void updateOrder(final OrderModel orderModel, final JuspayWebhookModel juspayWebhookModel)
			throws EtailNonBusinessExceptions
	{
		if (null != juspayWebhookModel)
		{
			final JuspayOrderStatusModel juspayOrderStatusModel = juspayWebhookModel.getOrderStatus();

			final GetOrderStatusResponse orderStatusResponse = getJuspayOrderResponseConverter().convert(juspayOrderStatusModel);

			final Map<String, Double> paymentMode = new HashMap<String, Double>();
			paymentMode.put(orderModel.getModeOfOrderPayment(), orderModel.getTotalPriceWithConv());

			//Payment Changes - Order before Payment
			getMplPaymentService().updateAuditEntry(orderStatusResponse, null, orderModel, paymentMode);

			if (orderModel.getTotalPrice().equals(orderStatusResponse.getAmount()))
			{
				getMplPaymentService().setPaymentTransaction(orderStatusResponse, paymentMode, orderModel);
			}

			//Logic when transaction is successful i.e. CHARGED
			if (MarketplacecommerceservicesConstants.CHARGED.equalsIgnoreCase(juspayOrderStatusModel.getStatus()))
			{
				LOG.error("Payment successful with transaction ID::::" + juspayWebhookModel.getOrderStatus().getOrderId());
				//saving card details
				getMplPaymentService().saveCardDetailsFromJuspay(orderStatusResponse, paymentMode, orderModel);
			}
			else
			{
				LOG.error("Payment failure with transaction ID::::" + juspayWebhookModel.getOrderStatus().getOrderId());
			}
			getMplPaymentService().paymentModeApportion(orderModel);
		}

	}



	/**
	 * This method removes voucher invalidation model when payment is timed-out, without releasing the coupon.
	 *
	 * @param orderModel
	 * @throws EtailNonBusinessExceptions
	 */
	private void removeVoucherInvalidation(final OrderModel orderModel) throws EtailNonBusinessExceptions
	{
		try
		{
			if (CollectionUtils.isNotEmpty(orderModel.getDiscounts()))
			{

				final CustomerModel customer = (CustomerModel) orderModel.getUser();
				final List<VoucherInvalidationModel> invalidationList = new ArrayList<VoucherInvalidationModel>(getMplVoucherDao()
						.findVoucherInvalidation(orderModel.getDiscounts().get(0).getCode(), customer.getOriginalUid(),
								orderModel.getCode()));

				final Iterator<VoucherInvalidationModel> iter = invalidationList.iterator();

				//Remove the existing discount
				while (iter.hasNext())
				{
					final VoucherInvalidationModel model = iter.next();
					getModelService().remove(model);
				}
			}
		}
		catch (final ModelRemovalException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0020);
		}
	}




	/**
	 * @return the mplProcessOrderDao
	 */
	public MplProcessOrderDao getMplProcessOrderDao()
	{
		return mplProcessOrderDao;
	}

	/**
	 * @param mplProcessOrderDao
	 *           the mplProcessOrderDao to set
	 */
	public void setMplProcessOrderDao(final MplProcessOrderDao mplProcessOrderDao)
	{
		this.mplProcessOrderDao = mplProcessOrderDao;
	}


	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}

	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}


	/**
	 * @return the orderStatusSpecifier
	 */
	public OrderStatusSpecifier getOrderStatusSpecifier()
	{
		return orderStatusSpecifier;
	}

	/**
	 * @param orderStatusSpecifier
	 *           the orderStatusSpecifier to set
	 */
	public void setOrderStatusSpecifier(final OrderStatusSpecifier orderStatusSpecifier)
	{
		this.orderStatusSpecifier = orderStatusSpecifier;
	}

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

	/**
	 * @return the mplOrderDao
	 */
	public MplOrderDao getMplOrderDao()
	{
		return mplOrderDao;
	}

	/**
	 * @param mplOrderDao
	 *           the mplOrderDao to set
	 */
	public void setMplOrderDao(final MplOrderDao mplOrderDao)
	{
		this.mplOrderDao = mplOrderDao;
	}

	/**
	 * @return the orderService
	 */
	public OrderService getOrderService()
	{
		return orderService;
	}

	/**
	 * @param orderService
	 *           the orderService to set
	 */
	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	/**
	 * @return the juspayWebHookDao
	 */
	public JuspayWebHookDao getJuspayWebHookDao()
	{
		return juspayWebHookDao;
	}

	/**
	 * @param juspayWebHookDao
	 *           the juspayWebHookDao to set
	 */
	public void setJuspayWebHookDao(final JuspayWebHookDao juspayWebHookDao)
	{
		this.juspayWebHookDao = juspayWebHookDao;
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


	/**
	 * @return the juspayOrderResponseConverter
	 */
	public Converter<JuspayOrderStatusModel, GetOrderStatusResponse> getJuspayOrderResponseConverter()
	{
		return juspayOrderResponseConverter;
	}

	/**
	 * @param juspayOrderResponseConverter
	 *           the juspayOrderResponseConverter to set
	 */
	@Required
	public void setJuspayOrderResponseConverter(
			final Converter<JuspayOrderStatusModel, GetOrderStatusResponse> juspayOrderResponseConverter)
	{
		this.juspayOrderResponseConverter = juspayOrderResponseConverter;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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
	 * @return the mplVoucherService
	 */
	public MplVoucherService getMplVoucherService()
	{
		return mplVoucherService;
	}

	/**
	 * @param mplVoucherService
	 *           the mplVoucherService to set
	 */
	public void setMplVoucherService(final MplVoucherService mplVoucherService)
	{
		this.mplVoucherService = mplVoucherService;
	}

	/**
	 * @return the mplVoucherDao
	 */
	public MplVoucherDao getMplVoucherDao()
	{
		return mplVoucherDao;
	}

	/**
	 * @param mplVoucherDao
	 *           the mplVoucherDao to set
	 */
	public void setMplVoucherDao(final MplVoucherDao mplVoucherDao)
	{
		this.mplVoucherDao = mplVoucherDao;
	}






}
