/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.LimitedStockPromoInvalidationModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
//import com.tisl.mpl.core.enums.WalletEnum;//Sonar Fix
import com.tisl.mpl.core.model.JuspayOrderStatusModel;
import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.JuspayWebHookDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplVoucherDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtStockLevelPromotionCheckService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCheckoutService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.model.EtailLimitedStockRestrictionModel;
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
	//For CAR:127
	private Converter<OrderModel, OrderData> orderConverter;
	//For CAR:127
	private static final String ERROR_NOTIF = "Error while sending notifications>>>>>>";

	@Resource(name = "stockPromoCheckService")
	private ExtStockLevelPromotionCheckService stockPromoCheckService;

	final Double skipPendingOrdersTATStFinal = new Double(10);
	@Autowired
	private BaseSiteService baseSiteService;

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
			//PaymentFix2017:- System time minus configured time from property file
			final String skipPendingOrdersTATSt = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.PAYMENTPENDING_SKIPTIME);
			final Double skipPendingOrdersTAT = (null != skipPendingOrdersTATSt ? Double.valueOf(skipPendingOrdersTATSt)
					: skipPendingOrdersTATStFinal);
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, -skipPendingOrdersTAT.intValue());
			final Date queryTAT = cal.getTime();

			//PaymentFix2017:- queryTAT added
			orders = getMplProcessOrderDao().getPaymentPedingOrders(OrderStatus.PAYMENT_PENDING.toString(), queryTAT);

			LOG.debug("Skip Time configured in the job::" + skipPendingOrdersTATSt);

			LOG.debug("Number of Orders fetched by the job::" + orders.size());

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
				baseSiteService.setCurrentBaseSite(orderModel.getSite(), true);

				try
				{
					//For CAR:127
					final OrderData orderData = getOrderConverter().convert(orderModel);
					//For CAR:127
					final String cartGuid = orderModel.getGuid();
					MplPaymentAuditModel auditModel = null;
					if (StringUtils.isNotEmpty(cartGuid)) //IQA for TPR-629
					{
						auditModel = getMplOrderDao().getAuditList(cartGuid);
						LOG.debug("Latest Audit ID:- " + auditModel + "for respective GUID:- " + cartGuid);
					}


					//LOG.debug("Wallet details of orderModel:- " + orderModel.getIsWallet().getCode());


					if (null != auditModel && StringUtils.isNotEmpty(auditModel.getAuditId()))
					{
						//					if (null != auditModel && StringUtils.isNotEmpty(auditModel.getAuditId())
						//							&& ((null != orderModel.getIsWallet()
						//									&& WalletEnum.NONWALLET.toString().equals(orderModel.getIsWallet().getCode()))
						//									|| orderModel.getIsWallet() == null))
						//					{**********Commented for mRupee****
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

						LOG.debug("***orderTATForTimeout--->" + orderTATForTimeout);
						//PaymentFix2017:- Logic to execute the webhook posted events
						if (CollectionUtils.isNotEmpty(hooks))
						{
							final List<JuspayWebhookModel> postedBeforeTime = new ArrayList<JuspayWebhookModel>();
							final List<JuspayWebhookModel> postedAfterTime = new ArrayList<JuspayWebhookModel>();
							JuspayWebhookModel latestSuccess = null;
							//final JuspayWebhookModel latestFailed = null;
							for (final JuspayWebhookModel juspayWebhookModel : hooks)
							{
								LOG.debug("Juspay Event Creation Time:- " + juspayWebhookModel.getCreationtime());
								final Date eventCreationDate = juspayWebhookModel.getCreationtime();
								if ((eventCreationDate).before(orderTATForTimeout))
								{
									LOG.debug("Juspay Event Creation Time postedBeforeTime");
									postedBeforeTime.add(juspayWebhookModel);
								}
								else
								{
									LOG.debug("Juspay Event Creation Time postedAfterTime");
									postedAfterTime.add(juspayWebhookModel);
								}
							}
							if (CollectionUtils.isNotEmpty(postedBeforeTime))
							{
								LOG.debug("Change the Order status for less then juspayWebhookRetryTAT time ");
								//If ORDER_SUCCEEDED event posted with in juspayWebhookRetryTAT minute
								latestSuccess = postedBeforeTime.get(0);
								LOG.debug("latest Juspay Event ID:- " + latestSuccess.getEventId() + " Event Neme:- "
										+ latestSuccess.getEventName());
								if (!latestSuccess.getIsExpired().booleanValue()
										&& StringUtils.equalsIgnoreCase(latestSuccess.getEventName(), "ORDER_SUCCEEDED"))
								{
									LOG.debug("latest Juspay Event Success");
									//commented for CAR:127
									//takeActionAgainstOrder(latestSuccess, orderModel, true);
									takeActionAgainstOrder(latestSuccess, orderModel, true, orderData);

									for (final JuspayWebhookModel jspayPostBefore : postedBeforeTime)
									{
										jspayPostBefore.setIsExpired(Boolean.TRUE);
									}
									getModelService().saveAll(postedBeforeTime);
								}
								else if (latestSuccess.getIsExpired().booleanValue()
										&& StringUtils.equalsIgnoreCase(latestSuccess.getEventName(), "ORDER_SUCCEEDED"))
								{
									LOG.error("For juspay id:- " + latestSuccess.getOrderStatus().getOrderId()
											+ "  one Parent ID already been processed.  this is duplicate Order ID");
									LOG.error("Hence , changing the Order to Payment Failed");

									getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_TIMEOUT);
								}
								//If ORDER_FAILED event posted with in juspayWebhookRetryTAT time with no ORDER_SUCCEEDED event
								else if ((new Date()).after(orderTATForTimeout)
										&& StringUtils.equalsIgnoreCase(latestSuccess.getEventName(), "ORDER_FAILED"))
								{
									LOG.debug("latest Juspay Event Failed");
									takeActionAgainstOrder(latestSuccess, orderModel, false, orderData);
									for (final JuspayWebhookModel jspayPostBefore : postedBeforeTime)
									{
										jspayPostBefore.setIsExpired(Boolean.TRUE);
									}
									getModelService().saveAll(postedBeforeTime);
								}
							}

							//If ORDER_FAILED event posted after juspayWebhookRetryTAT
							if (CollectionUtils.isNotEmpty(postedAfterTime))
							{
								LOG.debug("Change the Order to Order Failed for greater then juspayWebhookRetryTAT time ");
								takeActionAgainstOrder(latestSuccess, orderModel, false, orderData);

								for (final JuspayWebhookModel jspayPostAfter : postedAfterTime)
								{
									jspayPostAfter.setIsExpired(Boolean.TRUE);
								}
								getModelService().saveAll(postedBeforeTime);
							}
						}
						else if ((new Date()).after(orderTATForTimeout))
						{
							LOG.debug("No Event after juspayWebhookRetryTAT time ");
							//getting PinCode against Order
							final String defaultPinCode = getPinCodeForOrder(orderModel);

							//OMS Deallocation call for failed order
							//commented For CAR:127
							/*
							 * getMplCommerceCartService().isInventoryReserved(orderModel,
							 * MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);
							 */

							getMplCommerceCartService().isInventoryReserved(orderData,
									MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode,
									orderModel, null, SalesApplication.WEB);
							getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_TIMEOUT);

							//TPR-965
							removePromotionInvalidation(orderModel);

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

						else if ((new Date()).before(orderTATForTimeout))
						{
							LOG.debug("No Event before juspayWebhookRetryTAT time ");
							if (null != orderModel.getIsPendingNotSent() && !orderModel.getIsPendingNotSent().booleanValue())
							{
								//Email and sms for Payment_Pending
								boolean flag = false;
								final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
										MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
										+ orderModel.getCode();
								LOG.debug("Starting Payment Pending Notification");
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
	@Override
	public String getPinCodeForOrder(final OrderModel orderModel)
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
			final boolean positive, final OrderData orderData) throws InvalidCartException, CalculationException,
			EtailNonBusinessExceptions, VoucherOperationException
	{
		//SprintPaymentFixes:- Try catch added to handle Exception and set the Order Status to Payment_Timeout
		try
		{
			final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
					+ orderModel.getCode();
			if (positive)
			{
				if (null == orderModel.getPaymentInfo())
				{
					LOG.debug("Inside processing order-->No PaymentInfo");
					updateOrder(orderModel, juspayWebhookModel);

					//Re-trigger submit order process from Payment_Pending to Payment_Successful
					final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
					parameter.setEnableHooks(true);
					parameter.setSalesApplication(orderModel.getSalesApplication());

					final CommerceOrderResult result = new CommerceOrderResult();
					result.setOrder(orderModel);

					//SprintPaymentFixes:- ModeOfpayment set same as in Payment Info
					if (setModeOfPayment(orderModel))
					{
						modelService.save(orderModel);
					}

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

					juspayWebhookModel.setIsExpired(Boolean.TRUE);
					//SprintPaymentFixes:- juspayModel save added
					modelService.save(juspayWebhookModel);
				}
				//SprintPaymentFixes:- if any case Order Status Updation fails and Order is ready to mode to other Environment then change status to Payment_Successful
				else if (null != orderModel.getPaymentInfo() && CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions())
						&& CollectionUtils.isEmpty(orderModel.getChildOrders()))
				{
					LOG.debug("Inside processing order-->With PaymentInfo");
					//Re-trigger submit order process from Payment_Pending to Payment_Successful
					final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
					parameter.setEnableHooks(true);
					parameter.setSalesApplication(orderModel.getSalesApplication());

					final CommerceOrderResult result = new CommerceOrderResult();
					result.setOrder(orderModel);

					//SprintPaymentFixes:- ModeOfpayment set same as in Payment Info
					if (setModeOfPayment(orderModel))
					{
						modelService.save(orderModel);
					}

					//PaymentFix2017: setIsSentToOMS not required
					//No Need to change to FALSE
					//orderModel.setIsSentToOMS(Boolean.FALSE);
					//orderModel.setOmsSubmitStatus("");

					getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
				}
				//SprintPaymentFixes:- if any case Order Status Updation fails and Order is ready to mode to other Environment then change status to Payment_Successful
				else if (null != orderModel.getPaymentInfo() && CollectionUtils.isNotEmpty(orderModel.getChildOrders())
						&& CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions()))
				{
					LOG.debug("Inside payment successful order-->With PaymentInfo");
					//SprintPaymentFixes:- ModeOfpayment set same as in Payment Info
					//PaymentFix2017: setIsSentToOMS not required
					//No Need to change to FALSE
					//orderModel.setIsSentToOMS(Boolean.FALSE);
					//orderModel.setOmsSubmitStatus("");

					getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);

					juspayWebhookModel.setIsExpired(Boolean.TRUE);
					//SprintPaymentFixes:- juspayModel save added
					modelService.save(juspayWebhookModel);
				}

				//SprintPaymentFixes:- for modeOfPayment as COD, if there is no child orders the Order will be failed
				if (null != orderModel.getModeOfOrderPayment() && orderModel.getModeOfOrderPayment().equalsIgnoreCase("COD")
						&& null != orderModel.getPaymentInfo() && CollectionUtils.isNotEmpty(orderModel.getChildOrders())
						&& CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions()))
				{
					LOG.debug("Inside COD-->payment Successful");
					getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
				}


			}
			else if (!positive)
			{
				//Added for failure transactions
				if (null == orderModel.getPaymentInfo())
				{
					LOG.debug("Inside payment failed scenario");
					updateOrder(orderModel, juspayWebhookModel);

					//SprintPaymentFixes:- ModeOfpayment set same as in Payment Info
					if (setModeOfPayment(orderModel))
					{
						modelService.save(orderModel);
					}

					//getting PinCode against Order
					final String defaultPinCode = getPinCodeForOrder(orderModel);

					//OMS Deallocation call for failed order
					getMplCommerceCartService().isInventoryReserved(orderData,
							MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode, orderModel,
							null, SalesApplication.WEB);

					getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_FAILED);
					//limited stock promotion issue starts here
					removePromotionInvalidation(orderModel);
					//limited stock promotion issue ends here


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

					juspayWebhookModel.setIsExpired(Boolean.TRUE);
					//SprintPaymentFixes:- juspayModel save added
					modelService.save(juspayWebhookModel);
				}

				//SprintPaymentFixes:- if any case Order Status Updation fails and Order is ready to mode to other Environment then change status to Payment_Successful or Payment Timeout
				else if (null != orderModel.getPaymentInfo() && CollectionUtils.isNotEmpty(orderModel.getChildOrders())
						&& CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions()))
				{

					LOG.debug("Inside With payment Info-->payment Successful");
					boolean successFlag = false;
					for (final PaymentTransactionModel paymentTransaction : orderModel.getPaymentTransactions())
					{
						if (paymentTransaction.getStatus().equalsIgnoreCase("SUCCESS"))
						{

							//PaymentFix2017: setIsSentToOMS not required
							//No Need to change to FALSE
							//orderModel.setIsSentToOMS(Boolean.FALSE);
							//orderModel.setOmsSubmitStatus("");
							getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_SUCCESSFUL);
							successFlag = true;
							break;
						}
					}
					if (!successFlag)
					{
						getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_FAILED);
						//limited stock promotion issue starts here
						removePromotionInvalidation(orderModel);
						//limited stock promotion issue ends here
					}

					juspayWebhookModel.setIsExpired(Boolean.TRUE);
					//SprintPaymentFixes:- juspayModel save added
					modelService.save(juspayWebhookModel);
				}

				//SprintPaymentFixes:- for modeOfPayment as COD, if there is no child orders the Order will be failed
				if (null != orderModel.getModeOfOrderPayment() && orderModel.getModeOfOrderPayment().equalsIgnoreCase("COD")
						&& CollectionUtils.isEmpty(orderModel.getChildOrders()))
				{
					LOG.debug("Inside With payment Info-->payment failed");

					getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_FAILED);
					//limited stock promotion issue starts here
					removePromotionInvalidation(orderModel);
					//limited stock promotion issue ends here
				}

			}
		}
		catch (final ModelSavingException e)
		{
			//getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_PENDING);
			LOG.error("Error while saving into DB from job>>>>>>", e);
		}
		catch (final Exception e)
		{
			//getOrderStatusSpecifier().setOrderStatus(orderModel, OrderStatus.PAYMENT_PENDING);
			LOG.error("Error while updating updateOrder from job>>>>>>", e);
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
	{
		if (null != juspayWebhookModel)
		{
			final JuspayOrderStatusModel juspayOrderStatusModel = juspayWebhookModel.getOrderStatus();

			final GetOrderStatusResponse orderStatusResponse = getJuspayOrderResponseConverter().convert(juspayOrderStatusModel);

			final Map<String, Double> paymentMode = new HashMap<String, Double>();
			paymentMode.put(orderModel.getModeOfOrderPayment(), orderModel.getTotalPriceWithConv());

			//Payment Changes - Order before Payment
			getMplPaymentService().updateAuditEntry(orderStatusResponse, null, orderModel, paymentMode);
			LOG.warn("Order model total price " + orderModel.getTotalPrice() + "order status response amount"
					+ orderStatusResponse.getAmount());
			//SprintPaymentFixes:- Added:- CollectionUtils.isEmpty(orderModel.getPaymentTransactions()
			boolean faliurFlag = false;
			final List<PaymentTransactionModel> payTranModelList = orderModel.getPaymentTransactions();
			for (final PaymentTransactionModel payTranModel : payTranModelList)
			{
				if (payTranModel.getStatus().equalsIgnoreCase("success"))
				{
					faliurFlag = true;
					break;
				}
			}
			if (!faliurFlag && orderModel.getTotalPrice().equals(orderStatusResponse.getAmount())
					&& orderStatusResponse.getAmount().doubleValue() > 0)
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

	//TPR-965
	/**
	 * removing the released stocks
	 *
	 * @param orderModel
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void removePromotionInvalidation(final OrderModel orderModel) throws EtailNonBusinessExceptions
	{
		LOG.debug("Chcking to remove promotion invalidation entries..............");
		try
		{
			//TISSQAUAT-468 and TISSQAUATS-752  code starts here for promotion offercount revert back logic
			//marketplace.PaymentPending.skipTime
			boolean isLimitedStockRestrictionAppliedflag = false;
			final List<PromotionResultModel> promotionlist = new ArrayList<PromotionResultModel>(orderModel.getAllPromotionResults());
			final Iterator<PromotionResultModel> iter2 = promotionlist.iterator();
			while (iter2.hasNext())
			{
				//final EtailLimitedStockRestriction limitedStockRestriction = new EtailLimitedStockRestriction();
				final PromotionResultModel model2 = iter2.next();
				if (CollectionUtils.isNotEmpty(model2.getPromotion().getRestrictions())
						&& checkForLimitedStockPromo(model2.getPromotion().getRestrictions()))//TISSQAUATS-941

				{
					isLimitedStockRestrictionAppliedflag = true;
					LOG.debug("Promotion Type of Limited Offer");
				}

				//TISSQAUAT-468 and TISSQAUATS-752 code ends here for promotion offercount revert back logic
				//SONAR FIX
				if (CollectionUtils.isNotEmpty(orderModel.getDiscounts()) || isLimitedStockRestrictionAppliedflag)//TISSQAUAT-468 and TISSQAUATS-752  flag condition added for limited stock promotion check(promotion offercount revert back logic)
				{
					final List<LimitedStockPromoInvalidationModel> invalidationList = new ArrayList<LimitedStockPromoInvalidationModel>(
							stockPromoCheckService.getPromoInvalidationList(orderModel.getGuid()));

					final Iterator<LimitedStockPromoInvalidationModel> iter = invalidationList.iterator();

					//Remove the existing discount
					while (iter.hasNext())
					{
						final LimitedStockPromoInvalidationModel model = iter.next();
						LOG.debug("Removing Invalidation Entries" + model.getPk());
						getModelService().remove(model);
					}
				}

			}
		}
		catch (final ModelRemovalException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0020);
		}
	}

	/**
	 * Validating for Limited Offer Promotion
	 *
	 *
	 * @param restrictions
	 * @return boolean
	 */
	private boolean checkForLimitedStockPromo(final Collection<AbstractPromotionRestrictionModel> restrictions)
	{
		if (CollectionUtils.isNotEmpty(restrictions))
		{
			for (final AbstractPromotionRestrictionModel restriction : restrictions)
			{
				if (restriction instanceof EtailLimitedStockRestrictionModel)
				{
					return true;
				}
			}
		}
		return false;
	}

	//Not used
	//	/**
	//	 * This method removes voucher invalidation model when payment is timed-out, without releasing the coupon.
	//	 *
	//	 * @param orderModel
	//	 * @throws EtailNonBusinessExceptions
	//	 */
	//	private void removeVoucherInvalidation(final OrderModel orderModel) throws EtailNonBusinessExceptions
	//	{
	//		try
	//		{
	//			if (CollectionUtils.isNotEmpty(orderModel.getDiscounts()))
	//			{
	//
	//				final CustomerModel customer = (CustomerModel) orderModel.getUser();
	//				final List<VoucherInvalidationModel> invalidationList = new ArrayList<VoucherInvalidationModel>(getMplVoucherDao()
	//						.findVoucherInvalidation(orderModel.getDiscounts().get(0).getCode(), customer.getOriginalUid(),
	//								orderModel.getCode()));
	//
	//				final Iterator<VoucherInvalidationModel> iter = invalidationList.iterator();
	//
	//				//Remove the existing discount
	//				while (iter.hasNext())
	//				{
	//					final VoucherInvalidationModel model = iter.next();
	//					getModelService().remove(model);
	//				}
	//			}
	//		}
	//		catch (final ModelRemovalException e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0020);
	//		}
	//	}



	private boolean setModeOfPayment(final OrderModel orderModel)
	{
		//SprintPaymentFixes:- ModeOfpayment set same as in Payment Info
		boolean returnFlag = false;
		try
		{
			if (null != orderModel.getPaymentInfo())
			{
				final PaymentInfoModel payInfo = orderModel.getPaymentInfo();
				final String paymentModeFromInfo = getMplPaymentService().getPaymentModeFrompayInfo(payInfo);
				orderModel.setModeOfOrderPayment(paymentModeFromInfo);

				//modelService.save(orderModel);
				returnFlag = true;
			}
		}
		catch (final ModelSavingException me)
		{
			returnFlag = false;
		}
		catch (final Exception e)
		{
			returnFlag = false;
		}
		return returnFlag;
	}


	//GETTERS AND SETTERS

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


	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}



}
