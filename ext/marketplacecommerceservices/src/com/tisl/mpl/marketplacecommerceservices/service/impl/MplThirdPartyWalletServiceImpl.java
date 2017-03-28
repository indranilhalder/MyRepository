/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplProcessOrderDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplThirdPartyWalletDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCheckoutService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplThirdPartyWalletService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.util.OrderStatusSpecifier;
import com.tisl.mpl.wallet.service.DefaultMplMrupeePaymentService;


/**
 * @author TCS
 *
 */
public class MplThirdPartyWalletServiceImpl implements MplThirdPartyWalletService
{
	/**
	 *
	 */
	private static final String T = "T";

	private static final String F = "F";

	/**
	 *
	 */
	private static final String SSL = "SSL";

	/**
	 *
	 */
	private static final String _14_140_248_13 = "14.140.248.13";

	/**
	 *
	 */
	private static final String SPLIT = "\\|";

	/**
	 *
	 */
	private static final String PAYMENT_PROXY_VALUE = "payment.proxy.value";

	/**
	 *
	 */
	private static final String POST = "POST";

	/**
	 *
	 */
	private static final String PAYMENT_M_RUPEE_MERCHANT_ID = "payment.mRupee.merchantID";

	/**
	 *
	 */
	private static final String PAYMENT_M_RUPEE_VERIFICATION_URL = "payment.mRupee.verification";

	/**
	 *
	 */
	private static final String S = "S";

	private final static Logger LOG = Logger.getLogger(MplThirdPartyWalletServiceImpl.class.getName());

	@Autowired
	private MplThirdPartyWalletDao mplThirdPartyWalletDao;

	@Resource(name = "mplPaymentService")
	private MplPaymentService mplPaymentService;
	@Autowired
	private ModelService modelService;
	@Resource(name = "mplProcessOrderDao")
	private MplProcessOrderDao mplProcessOrderDao;
	@Resource(name = "mplOrderDao")
	private MplOrderDao mplOrderDao;
	@Autowired
	private MplCommerceCheckoutService mplCommerceCheckoutService;
	private OrderService orderService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;
	@Autowired
	private MplVoucherService mplVoucherService;
	@Resource(name = "mplProcessOrderService")
	private MplProcessOrderService mplProcessOrderService;
	@Autowired
	private MplCommerceCartService mplCommerceCartService;
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "mRupeeService")
	private DefaultMplMrupeePaymentService mRupeeService;

	//For CAR:127 production code changes
	private Converter<OrderModel, OrderData> orderConverter;


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}



	/**
	 * return tat time from mrupee
	 */
	@Override
	public Double getmRupeeJobTAT()
	{
		Double mRupeejobTAT = Double.valueOf(0);
		try
		{
			final BaseStoreModel store = mplThirdPartyWalletDao.getJobTAT();
			if (null != store && null != store.getMRupeeJobTAT())
			{
				mRupeejobTAT = store.getMRupeeJobTAT();
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error(exception.getMessage());
		}
		return mRupeejobTAT;
	}

	/**
	 * fetch order in pending or refund initiated status perform action only for mrupee orders perform verification and
	 * processing with the verification response from mrupee save data to audit entry table submitting orders
	 */
	@Override
	public void fetchThirdPartyAuditTableData()
	{

		try
		{
			LOG.debug("#######################inside cron job for payment pending#######");
			List<OrderModel> pendingOrders = new ArrayList<OrderModel>();
			//DAO call to fetch PAYMENT PENDING or REFUND-INITIATED orders
			pendingOrders = mplProcessOrderDao.getPendingOrRefundInitiatedOrders(OrderStatus.PAYMENT_PENDING.toString(),
					OrderStatus.REFUND_INITIATED.toString());
			LOG.debug("#####################pendingOrders**" + pendingOrders);
			boolean isPayment = true;
			boolean isReturn = false;
			for (final OrderModel order : pendingOrders)
			{
				if (StringUtils.isNotEmpty(order.getGuid()))
				{
					final String cartGuid = order.getGuid();
					final Date orderTATForTimeout = getTatTimeOut(new Date(), getmRupeeJobTAT(), order.getCreationtime());
					final MplPaymentAuditModel auditModelData = mplOrderDao.getAuditList(cartGuid);

					//For CAR:127
					final OrderData orderData = getOrderConverter().convert(order);

					if (auditModelData != null && !auditModelData.getIsExpired().booleanValue())
					{
						final List<MplPaymentAuditEntryModel> entryList = Lists.newArrayList(auditModelData.getAuditEntries());
						String status = "";
						if (CollectionUtils.isNotEmpty(entryList) && OrderStatus.PAYMENT_PENDING.equals(order.getStatus())
								&& !auditModelData.getIsExpired().booleanValue() && new Date().after(orderTATForTimeout))
						{
							isPayment = true;
							LOG.debug("###################timeout############" + order.getCode());
							//For CAR:127 production code changes
							performProcessingOrder(auditModelData, order, T, isPayment, isReturn, orderData);
							sendNotification(order, T);
						}
						else
						{
							final String response = getMrupeeResponse(auditModelData);//getting mrupee response
							LOG.debug("response from CronJob Mrupee#####################" + response);
							if (StringUtils.isNotEmpty(response) && response.contains("|"))
							{
								final String[] params1 = response.split(SPLIT);
								status = params1[0];
							}
							//orderTATForTimeout = getTatTimeOut(new Date(), getmRupeeJobTAT(), order.getCreationtime());
							if (CollectionUtils.isNotEmpty(entryList) && OrderStatus.PAYMENT_PENDING.equals(order.getStatus())
									&& !auditModelData.getIsExpired().booleanValue() && status.equalsIgnoreCase(S)
									&& new Date().before(orderTATForTimeout))
							{
								//updating audit details
								final Map<String, Double> paymentMode = sessionService
										.getAttribute(MarketplacecommerceservicesConstants.PAYMENTMODE);
								final String[] params1 = response.split(SPLIT);
								Double transAmt = Double.valueOf(0.0);
								if (params1.length == 6)
								{
									transAmt = Double.valueOf(params1[5]);
								}

								mplPaymentService
										.setTPWalletPaymentTransaction(paymentMode, order, auditModelData.getAuditId(), transAmt);
								final CustomerModel mplCustomer = (CustomerModel) order.getUser();
								updateAuditInfoForPayment(auditModelData, entryList, mplCustomer, order);
								//sending notification mail
								final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
										MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
										+ order.getCode();

								LOG.debug("#######################payment processiong in mrupee cronjob" + order.getCode());

								isPayment = true;
								try
								{
									notificationService.triggerEmailAndSmsOnOrderConfirmation(order, trackOrderUrl);
								}
								catch (final JAXBException e)
								{
									LOG.error("**************************error in notification sending for order****" + order.getCode());
								}

							}
							//refund flow handling
							if (CollectionUtils.isNotEmpty(entryList) && OrderStatus.REFUND_INITIATED.equals(order.getStatus())
									&& !auditModelData.getIsExpired().booleanValue() && new Date().before(orderTATForTimeout))
							{

								isPayment = false;
								if (CollectionUtils.isNotEmpty(order.getPaymentTransactions()))
								{
									final PaymentTransactionModel paymentTransactionModel = order.getPaymentTransactions().get(0);
									for (final PaymentTransactionEntryModel trans : paymentTransactionModel.getEntries())
									{
										if (null != trans.getType() && "RETURN".equalsIgnoreCase(trans.getType().toString()))
										{
											isReturn = true;
											break;
										}
									}


									for (final AbstractOrderEntryModel orderEntry : order.getEntries())
									{
										ConsignmentStatus newStatus = null;
										if (orderEntry != null && CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries())
												&& isReturn)
										{
											MplPaymentAuditEntryModel refundAuditEntry = null;
											if (status.equalsIgnoreCase(S))
											{
												newStatus = ConsignmentStatus.RETURN_COMPLETED;
												refundAuditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
												refundAuditEntry.setStatus(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL);
											}
											else if (!(status.equalsIgnoreCase(S)))
											{
												refundAuditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
												newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
												refundAuditEntry.setStatus(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL);
											}
											refundAuditEntry.setAuditId(auditModelData.getAuditId());
											getModelService().save(refundAuditEntry);
											entryList.add(refundAuditEntry);
											auditModelData.setAuditEntries(entryList);
											auditModelData.setIsExpired(Boolean.TRUE);
											getModelService().save(auditModelData);
											//	final PaymentTransactionModel paymentTransactionModel = order.getPaymentTransactions().get(0);
											mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
													orderEntry.getNetAmountAfterAllDisc(), newStatus);
										}
										LOG.debug("#######################refund processiong in mrupee cronjob" + order.getCode());

									}
								}
							}


							if (StringUtils.isEmpty(status) && new Date().before(orderTATForTimeout))
							{
								//For CAR:127 production code changes
								//								performProcessingOrder(auditModelData, order, "", isPayment, isReturn);
								performProcessingOrder(auditModelData, order, "", isPayment, isReturn, orderData);
								sendNotification(order, F);
							}
							//getting response other than S
							if (!(S.equalsIgnoreCase(status)) && new Date().before(orderTATForTimeout))
							{
								//For CAR:127 production code changes
								//								performProcessingOrder(auditModelData, order, "", isPayment, isReturn);
								performProcessingOrder(auditModelData, order, status, isPayment, isReturn, orderData);
								sendNotification(order, F);
							}
						}
					}
				}
			}
			LOG.debug("#######################cron job finished#######");
		}
		catch (final AdapterException e)
		{
			LOG.error("****************error in connection>>>>>>", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);

		}
		catch (final Exception e)
		{
			LOG.error("*******************exception in  processing orders >>>>>>", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * send notification on payment timeout
	 *
	 * @param order
	 * @param status
	 *
	 */
	private void sendNotification(final OrderModel order, final String status)
	{
		// YTODO Auto-generated method stub
		final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
				+ order.getCode();
		try
		{
			if (status.equalsIgnoreCase(T))
			{
				notificationService.triggerEmailAndSmsOnPaymentTimeout(order, trackOrderUrl);
				LOG.debug("***************** Trigger notification for TimeOut Email");
			}
			else if (status.equalsIgnoreCase(F))
			{
				notificationService.triggerEmailAndSmsOnPaymentFailed(order, trackOrderUrl);
				LOG.debug("***************** Trigger notification for Failed Email");
			}
		}
		catch (final JAXBException e)
		{
			LOG.error("***********************error final in sending payment final timeout notification>>>>>>" + order.getCode());
			LOG.error("***********************error final in sending payment final timeout notification>>>>>>", e);
		}
	}

	/**
	 * perform order processing on time out
	 *
	 * @param auditModelData
	 * @param order
	 * @param status
	 * @param isReturn
	 * @param isPayment
	 */
	private void performProcessingOrder(final MplPaymentAuditModel auditModelData, final OrderModel order, final String status,
			final boolean isPayment, final boolean isReturn, final OrderData orderData)
	{
		try
		{
			if (isPayment)
			{
				final String defaultPinCode = mplProcessOrderService.getPinCodeForOrder(order);
				//OMS Deallocation call for failed order
				//				mplCommerceCartService.isInventoryReserved(order,
				//						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);

				//New code has been changed From TCS prod Support For CAR:127

				mplCommerceCartService.isInventoryReserved(orderData,
						MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode, order);

				final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
				auditEntry.setStatus(MplPaymentAuditStatusEnum.DECLINED);
				auditEntry.setAuditId(auditModelData.getAuditId());
				getModelService().save(auditEntry);
				final List<MplPaymentAuditEntryModel> entryList = Lists.newArrayList(auditModelData.getAuditEntries());
				entryList.add(auditEntry);
				auditModelData.setAuditEntries(entryList);
				auditModelData.setIsExpired(Boolean.TRUE);
				getModelService().save(auditModelData);
				if (CollectionUtils.isNotEmpty(order.getDiscounts()))
				{
					final PromotionVoucherModel voucherModel = (PromotionVoucherModel) order.getDiscounts().get(0);
					try
					{
						mplVoucherService.releaseVoucher(voucherModel.getVoucherCode(), null, order);
					}
					catch (final VoucherOperationException e)
					{

						LOG.error("exception##### error in updating voucher models >>>>>>", e);
					}
					mplVoucherService.recalculateCartForCoupon(null, order);
				}
				if (T.equalsIgnoreCase(status))
				{
					LOG.debug("#######################timeout  in mrupee cronjob" + order.getCode());
					orderStatusSpecifier.setOrderStatus(order, OrderStatus.PAYMENT_TIMEOUT);
				}
				else
				{
					orderStatusSpecifier.setOrderStatus(order, OrderStatus.PAYMENT_FAILED);
					LOG.debug("#######################payment failed in mrupee cronjob" + order.getCode());
				}
				getModelService().save(order);
			}
		}
		catch (final ModelSavingException e)
		{
			LOG.error("*****************ERROR IN MODEL SAVING FOR ORDER ***************>>>>>" + order.getCode());
			LOG.error("exception##### ERROR IN SAVING DETAIL FOR  ORDER>>>>>>", e);
		}

	}

	/**
	 * update audit details on successful mrupee response
	 *
	 * @param auditModelData
	 * @param entryList
	 * @param mplCustomer
	 * @param order
	 */
	private void updateAuditInfoForPayment(final MplPaymentAuditModel auditModelData,
			final List<MplPaymentAuditEntryModel> entryList, final CustomerModel mplCustomer, final OrderModel order)
	{

		final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
		auditEntry.setAuditId(auditModelData.getAuditId());
		auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
		getModelService().save(auditEntry);
		entryList.add(auditEntry);
		auditModelData.setAuditEntries(entryList);
		auditModelData.setIsExpired(Boolean.TRUE);
		getModelService().save(auditModelData);
		final List<AbstractOrderEntryModel> entries = order.getEntries();

		if (null != mplCustomer)
		{
			if (StringUtils.isNotEmpty(mplCustomer.getName()) && !mplCustomer.getName().equalsIgnoreCase(" "))
			{
				final String custName = mplCustomer.getName();
				mplPaymentService.saveTPWalletPaymentInfo(custName, entries, order, order.getCode());
			}
			else
			{
				final String custEmail = mplCustomer.getOriginalUid();
				mplPaymentService.saveTPWalletPaymentInfo(custEmail, entries, order, order.getCode());
			}
		}

		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setSalesApplication(order.getSalesApplication());
		final CommerceOrderResult result = new CommerceOrderResult();
		result.setOrder(order);
		try
		{
			mplCommerceCheckoutService.beforeSubmitOrder(parameter, result);
		}
		catch (final InvalidCartException e)
		{
			LOG.error("***************** ORDER INVALIDATION***************>>>>>" + order.getCode());
			LOG.error("******************exception##### : INAVLID CART>>>>>>", e);

		}
		catch (final CalculationException e)
		{
			LOG.error("*****************ERROR IN CART CALCULATION FOR ORDER ***************>>>>>" + order.getCode());
			LOG.error("******************exception##### ERROR IN ORDER TOTAL CALCULATION>>>>>>", e);
		}
		getOrderService().submitOrder(order);
		orderStatusSpecifier.setOrderStatus(order, OrderStatus.PAYMENT_SUCCESSFUL);
		getModelService().save(order);
	}

	/**
	 * getting mrupee response
	 *
	 * @param auditModelData
	 *
	 */
	private String getMrupeeResponse(final MplPaymentAuditModel auditModelData)
	{
		final String checksumKey = mRupeeService.generateCheckSumForVerification(auditModelData.getAuditId());
		final String mId = configurationService.getConfiguration().getString(PAYMENT_M_RUPEE_MERCHANT_ID);
		final String url = configurationService.getConfiguration().getString(PAYMENT_M_RUPEE_VERIFICATION_URL);
		final String serializedParams = "MCODE=" + mId + "&TXNTYPE=V" + "&REFNO=" + auditModelData.getAuditId() + "&CHECKSUM="
				+ checksumKey;
		return makeConnectivityCall(url, serializedParams);
	}

	/**
	 * get TAT time out
	 *
	 * @param date
	 * @param mRetryTAT
	 * @param crtdTime
	 * @return tatTimeout for pending orders
	 */
	private Date getTatTimeOut(final Date date, final Double mRetryTAT, final Date crtdTime)
	{
		// YTODO Auto-generated method stub
		Date orderTATForTimeout = new Date();
		if (null != mRetryTAT && mRetryTAT.doubleValue() > 0)
		{
			final Calendar cal = Calendar.getInstance();
			cal.setTime(crtdTime);
			//adding the TAT to order modified time to get the time upto which the Payment Pending Orders will be checked for Processing
			cal.add(Calendar.MINUTE, +mRetryTAT.intValue());
			orderTATForTimeout = cal.getTime();
		}
		return orderTATForTimeout;
	}




	/**
	 * @return the mplThirdPartyWalletDao
	 */
	public MplThirdPartyWalletDao getMplThirdPartyWalletDao()
	{
		return mplThirdPartyWalletDao;
	}

	/**
	 * @param mplThirdPartyWalletDao
	 *           the mplThirdPartyWalletDao to set
	 */
	public void setMplThirdPartyWalletDao(final MplThirdPartyWalletDao mplThirdPartyWalletDao)
	{
		this.mplThirdPartyWalletDao = mplThirdPartyWalletDao;
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
	 * It opens the connection to the given endPoint and returns the http response as String.
	 *
	 * @param endPoint
	 *           - The HTTP URL of the request
	 * @return HTTP response as string
	 */
	private String makeConnectivityCall(final String endPoint, final String encodedParams)
	{

		disableSslVerification();
		final String proxyEnableStatus = "true";
		HttpsURLConnection connection = null;
		final StringBuilder buffer = new StringBuilder();
		final int connectionTimeout = 5 * 10000;
		final int readTimeout = 5 * 1000;
		DataOutputStream wr = null;
		try
		{

			final SSLContext ssl_ctx = SSLContext.getInstance("TLS");
			final TrustManager[] trust_mgr = new TrustManager[]
			{ new X509TrustManager()
			{
				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(final X509Certificate[] certs, final String t)
				{
					/**
					 * DO NOTHING
					 */
				}

				public void checkServerTrusted(final X509Certificate[] certs, final String t)
				{
					/**
					 * DO NOTHING
					 */
				}
			} };
			ssl_ctx.init(null, // key manager
					trust_mgr, // trust manager
					new SecureRandom()); // random number generator
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
				final String proxyName = configurationService.getConfiguration().getString(PAYMENT_PROXY_VALUE);
				final int proxyPort = 8080;
				final SocketAddress addr = new InetSocketAddress(proxyName, proxyPort);
				final Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection(proxy);
			}
			else
			{
				final URL url = new URL(endPoint);
				connection = (HttpsURLConnection) url.openConnection();
			}
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod(POST);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(encodedParams.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setRequestProperty("charset", "utf-8");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(encodedParams);
			wr.flush();
			wr.close();
			final InputStream inputStream = connection.getInputStream();
			final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null)
			{
				buffer.append(line);
			}
		}
		catch (final Exception e)
		{
			LOG.error("******************Exception in connectivity in  mrupeee verification======================", e);
		}
		finally
		{
			try
			{
				if (wr != null)
				{
					wr.close();
				}
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
		return buffer.toString();
	}

	private void disableSslVerification()
	{
		try
		{
			// Create a trust manager that does not validate certificate chains

			final TrustManager[] trustAllCerts = new TrustManager[]
			{ new X509TrustManager()
			{
				public java.security.cert.X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkClientTrusted(final X509Certificate[] certs, final String authType)
				{
					//
				}

				public void checkServerTrusted(final X509Certificate[] certs, final String authType)
				{
					//
				}
			} };

			// Install the all-trusting trust manager
			final SSLContext sc = SSLContext.getInstance(SSL);
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			final HostnameVerifier allHostsValid = new HostnameVerifier()
			{
				public boolean verify(final String hostname, final SSLSession session)
				{
					final String mRupeehostname = getConfigurationService().getConfiguration().getString(
							MarketplacecommerceservicesConstants.MRUPEEHOSTNAME);
					//	if (hostname.equals(_14_140_248_13))
					if (hostname.equals(mRupeehostname))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		}
		catch (final NoSuchAlgorithmException e)
		{
			LOG.error("******************Exception occured in connectivity setting======================", e);
		}
		catch (final KeyManagementException e)
		{
			LOG.error("******************Exception occured in connectivity setting======================", e);
		}
	}

	/**
	 * @param entries
	 * @return
	 */
	private boolean ifRefundInitiated(final List<AbstractOrderEntryModel> entries)
	{
		boolean isRefund = false;
		for (final AbstractOrderEntryModel entry : entries)
		{
			final List<ReturnEntryModel> data = getReturnEntry(entry);
			if (CollectionUtils.isNotEmpty(data))
			{
				isRefund = true;
				break;
			}
		}
		return isRefund;
	}

	protected List<ReturnEntryModel> getReturnEntry(final AbstractOrderEntryModel entry)
	{
		final Map params = new HashMap();
		params.put("entry", entry);
		final String query = "SELECT {ret." + Item.PK + "} FROM { " + "ReturnEntry" + " AS ret} WHERE {" + "orderEntry"
				+ "} = ?entry ORDER BY {ret." + Item.PK + "} ASC";
		return this.flexibleSearchService.search(query, params).getResult();
	}



	/**
	 * @return the orderConverter
	 */
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	/**
	 * @param orderConverter
	 *           the orderConverter to set
	 */
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

}
