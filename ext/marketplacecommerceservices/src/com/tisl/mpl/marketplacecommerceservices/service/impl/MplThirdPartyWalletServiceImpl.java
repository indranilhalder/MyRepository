/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
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
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.OrderStatusSpecifier;
import com.tisl.mpl.wallet.service.MrupeePaymentService;


/**
 * @author 682160
 *
 */
public class MplThirdPartyWalletServiceImpl implements MplThirdPartyWalletService
{
	/**
	 *
	 */
	private static final String POST = "POST";

	/**
	 *
	 */
	private static final String PROXY_TCS_COM = "proxy.tcs.com";

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
	MplProcessOrderService mplProcessOrderService;
	@Autowired
	private MplCommerceCartService mplCommerceCartService;
	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;



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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplThirdPartyWalletService#getCronDetails(java.lang.String)
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		// YTODO Auto-generated method stub
		return mplThirdPartyWalletDao.getCronDetails(code);
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
			List<OrderModel> pendingOrders = new ArrayList<OrderModel>();
			//DAO call to fetch PAYMENT PENDING or REFUND-INITIATED orders
			pendingOrders = mplProcessOrderDao.getPendingOrRefundInitiatedOrders(OrderStatus.PAYMENT_PENDING.toString(),
					OrderStatus.REFUND_INITIATED.toString());
			Date orderTATForTimeout = new Date();
			for (final OrderModel order : pendingOrders)
			{
				if (StringUtils.isNotEmpty(order.getGuid()))
				{
					final String cartGuid = order.getGuid();
					final MplPaymentAuditModel auditModelData = mplOrderDao.getAuditList(cartGuid);
					if (auditModelData != null)
					{
						final List<MplPaymentAuditEntryModel> entryList = Lists.newArrayList(auditModelData.getAuditEntries());
						final MrupeePaymentService mRupeeService = new MrupeePaymentService();
						final String checksumKey = mRupeeService.generateCheckSumForVerification(auditModelData.getAuditId(), order
								.getTotalPriceWithConv().toString());
						final String mId = configurationService.getConfiguration().getString(PAYMENT_M_RUPEE_MERCHANT_ID);
						final String url = configurationService.getConfiguration().getString(PAYMENT_M_RUPEE_VERIFICATION_URL);
						final String serializedParams = "MCODE=" + mId + "&TXNTYPE=V" + "&REFNO=" + auditModelData.getAuditId()
								+ "&CHECKSUM=" + checksumKey;
						String status = "";
						final String response = makeConnectivityCall(url, serializedParams);
						if (StringUtils.isNotEmpty(response) && response.contains("|"))
						{
							final String[] params1 = response.split("|");
							status = params1[0];
						}
						orderTATForTimeout = getTatTimeOut(new Date(), getmRupeeJobTAT(), order.getCreationtime());
						if (status.isEmpty())
						{
							status = "S";
						}
						if (CollectionUtils.isNotEmpty(entryList) && OrderStatus.PAYMENT_PENDING.equals(order.getStatus())
								&& !auditModelData.getIsExpired().booleanValue() && status.equalsIgnoreCase(S)
								&& new Date().before(orderTATForTimeout))
						{
							//updating audit details
							final MplPaymentAuditEntryModel auditEntry = getModelService().create(MplPaymentAuditEntryModel.class);
							auditEntry.setAuditId(auditModelData.getAuditId());
							auditEntry.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
							getModelService().save(auditEntry);
							entryList.add(auditEntry);
							auditModelData.setAuditEntries(entryList);
							auditModelData.setIsExpired(Boolean.valueOf(true));
							getModelService().save(auditModelData);

							//updating payment info details
							final CustomerModel mplCustomer = (CustomerModel) order.getUser();
							final List<AbstractOrderEntryModel> entries = order.getEntries();
							if (null != mplCustomer)
							{
								if (StringUtils.isNotEmpty(mplCustomer.getName()) && !mplCustomer.getName().equalsIgnoreCase(" "))
								{
									final String custName = mplCustomer.getName();
									//Commented for Mobile use
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
							mplCommerceCheckoutService.beforeSubmitOrder(parameter, result);
							getOrderService().submitOrder(order);
							orderStatusSpecifier.setOrderStatus(order, OrderStatus.PAYMENT_SUCCESSFUL);
							getModelService().save(order);
							//order confirmation email and sms
							final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
									MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
									+ order.getCode();
							try
							{
								notificationService.triggerEmailAndSmsOnOrderConfirmation(order, trackOrderUrl);
							}
							catch (final JAXBException e)
							{
								LOG.error("**************************error final in sending order confirmation notification>>>>>>", e);
								throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
							}

						}

						//refund flow handling
						if (CollectionUtils.isNotEmpty(entryList) && OrderStatus.REFUND_INITIATED.equals(order.getStatus())
								&& !auditModelData.getIsExpired().booleanValue() && new Date().before(orderTATForTimeout))
						{
							for (final AbstractOrderEntryModel orderEntry : order.getEntries())
							{
								ConsignmentStatus newStatus = null;
								if (orderEntry != null && CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
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
									final PaymentTransactionModel paymentTransactionModel = order.getPaymentTransactions().get(0);
									mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
											orderEntry.getNetAmountAfterAllDisc(), newStatus);

								}
							}
						}

						//timeout handling
						if (new Date().after(orderTATForTimeout) || StringUtils.isEmpty(status) || !(S.equalsIgnoreCase(status)))
						{
							//getting PinCode against Order
							final String defaultPinCode = mplProcessOrderService.getPinCodeForOrder(order);
							//OMS Deallocation call for failed order
							final boolean test = true;
							if (!test)
							{
								mplCommerceCartService.isInventoryReserved(order,
										MarketplacecommerceservicesConstants.OMS_INVENTORY_RESV_TYPE_ORDERDEALLOCATE, defaultPinCode);
								if (CollectionUtils.isNotEmpty(order.getDiscounts()))
								{
									final PromotionVoucherModel voucherModel = (PromotionVoucherModel) order.getDiscounts().get(0);
									mplVoucherService.releaseVoucher(voucherModel.getVoucherCode(), null, order);
									mplVoucherService.recalculateCartForCoupon(null, order);
								}
								orderStatusSpecifier.setOrderStatus(order, OrderStatus.PAYMENT_TIMEOUT);
								getModelService().save(order);
								final String trackOrderUrl = getConfigurationService().getConfiguration().getString(
										MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
										+ order.getCode();
								try
								{
									notificationService.triggerEmailAndSmsOnPaymentTimeout(order, trackOrderUrl);
								}
								catch (final JAXBException e)
								{
									LOG.error("***********************error final in sending payment final timeout notification>>>>>>", e);
									throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
								}

							}
						}
					}
				}
			}
		}
		catch (final AdapterException e)
		{
			LOG.error("exception##### : error in connection>>>>>>", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);

		}
		catch (final InvalidCartException e)
		{
			LOG.error("exception##### : INAVLID CART>>>>>>", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);

		}
		catch (final CalculationException e)
		{
			LOG.error("exception##### ERROR IN ORDER TOTAL CALCULATION>>>>>>", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("exception##### error in processing of mrupee pending orders >>>>>>", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception e)
		{
			LOG.error("exception##### in refund processing of  >>>>>>", e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @param date
	 * @param crtdTime
	 * @param double1
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplThirdPartyWalletService#saveCronDetails(java.util.Date,
	 * java.lang.String)
	 */
	@Override
	public void saveCronDetails(final Date startTime, final String code)
	{
		final MplConfigurationModel oModel = mplThirdPartyWalletDao.getCronDetails(code);
		if (null != oModel && null != oModel.getMplConfigCode())
		{
			LOG.debug("Saving CronJob Run Time :" + startTime);
			oModel.setMplConfigDate(startTime);
			getModelService().save(oModel);
			LOG.debug("Cron Job Details Saved for Code :" + code);
		}

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

		final String proxyEnableStatus = "true";
		HttpsURLConnection connection = null;
		final StringBuilder buffer = new StringBuilder();
		final int connectionTimeout = 5 * 10000;
		final int readTimeout = 5 * 1000;
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
					//
				}

				public void checkServerTrusted(final X509Certificate[] certs, final String t)
				{
					//
				}
			} };
			ssl_ctx.init(null, // key manager
					trust_mgr, // trust manager
					new SecureRandom()); // random number generator
			HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

			if (proxyEnableStatus.equalsIgnoreCase("true"))
			{
				final String proxyName = PROXY_TCS_COM;
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
			final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
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
		return buffer.toString();
	}
}
