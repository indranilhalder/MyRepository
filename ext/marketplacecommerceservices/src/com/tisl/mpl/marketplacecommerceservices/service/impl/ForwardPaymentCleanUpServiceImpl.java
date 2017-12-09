/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.ForwardPaymentCleanUpDao;
import com.tisl.mpl.marketplacecommerceservices.service.ForwardPaymentCleanUpService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class ForwardPaymentCleanUpServiceImpl implements ForwardPaymentCleanUpService
{

	@Autowired
	ForwardPaymentCleanUpDao forwardPaymentCleanUpDao;

	@Autowired
	ConfigurationService configurationService;

	@Autowired
	MplJusPayRefundService mplJusPayRefundService;

	private final static Logger LOG = Logger.getLogger(ForwardPaymentCleanUpServiceImpl.class.getName());

	@Override
	public List<OrderModel> fetchSpecificOrders(final Date startTime, final Date endTime)
	{
		return forwardPaymentCleanUpDao.fetchSpecificOrders(startTime, endTime);
	}

	@Override
	public MplConfigurationModel fetchConfigDetails(final String code)
	{
		return forwardPaymentCleanUpDao.fetchConfigDetails(code);
	}

	@Override
	public void cleanUpMultiplePayments(final OrderModel orderModel)
	{
		try
		{
			if (!checkCOD(orderModel))
			{
				final List<MplPaymentAuditModel> paymentAuditList = forwardPaymentCleanUpDao.fetchAuditsForGUID(orderModel.getGuid());

				if (paymentAuditList.size() > 1)
				{
					LOG.debug("Found multiple audits for order: " + orderModel.getCode());
					final List<PaymentTransactionModel> paymentTransactionList = orderModel.getPaymentTransactions();

					String firstAuditId = null;
					for (final PaymentTransactionModel paymentTransaction : paymentTransactionList)
					{
						if (null != firstAuditId)
						{
							break;
						}
						for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
						{
							if ((PaymentTransactionType.CAPTURE.equals(paymentTransactionEntry.getType()) || PaymentTransactionType.AUTHORIZATION
									.equals(paymentTransactionEntry.getType()))
									&& ("success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus()) || "ACCEPTED"
											.equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus())))
							{
								firstAuditId = paymentTransactionEntry.getRequestToken();
								break;
							}
						}
					}
					if (null != firstAuditId)
					{
						LOG.debug("Valid audit ID for the order: " + firstAuditId);
						LOG.debug("Requsting JuspayOrderStatus for the valid audit id: " + firstAuditId);

						final GetOrderStatusResponse orderStatusResponse = getOrderStatusFromJuspay(firstAuditId);

						if (null != orderStatusResponse && null != orderStatusResponse.getStatus())
						{
							LOG.debug("Status for the valid audit id " + firstAuditId + " : " + orderStatusResponse.getStatus());

							if (orderStatusResponse.getStatus().equalsIgnoreCase(MarketplacecommerceservicesConstants.CHARGED)
									&& CollectionUtils.isEmpty(orderStatusResponse.getRefunds()))
							{
								for (final MplPaymentAuditModel paymentAudit : paymentAuditList)
								{
									try
									{
										if (!StringUtils.equalsIgnoreCase(paymentAudit.getAuditId(), firstAuditId))
										{
											LOG.debug("Requsting JuspayOrderStatus for audit id: " + paymentAudit.getAuditId());

											final GetOrderStatusResponse dupOrderStatusResponse = getOrderStatusFromJuspay(paymentAudit
													.getAuditId());
											if (null != dupOrderStatusResponse && null != dupOrderStatusResponse.getStatus())
											{
												if (dupOrderStatusResponse.getStatus().equalsIgnoreCase(
														MarketplacecommerceservicesConstants.CHARGED)
														&& CollectionUtils.isEmpty(dupOrderStatusResponse.getRefunds()))
												{
													LOG.debug("Initiating refund for Audit ID " + paymentAudit.getAuditId());

													initiateOrderRefund(dupOrderStatusResponse, paymentAudit);

													LOG.debug("Refund initiated successfully for Audit ID " + paymentAudit.getAuditId());
												}
												else
												{
													LOG.debug("Audit ID " + paymentAudit.getAuditId() + " is not CHARGED or already refunded");
												}
											}
											else
											{
												LOG.error("Failed to retrieve status from Juspay for audit id: " + paymentAudit.getAuditId());
											}
										}
									}
									catch (final Exception e)
									{
										LOG.error("Error while processing audit id: " + paymentAudit.getAuditId() + " Order ID: "
												+ orderModel.getCode());
										LOG.error(e.getMessage(), e);
									}
								}
							}
							else
							{
								LOG.debug("Valid Audit ID " + firstAuditId
										+ " is not CHARGED or already refunded. Aborting auto-refund for the order :"
										+ orderModel.getCode());
							}
						}
						else
						{
							LOG.error("Failed to retrieve status for the valid audit id from Juspay");
						}
					}
					else
					{
						LOG.debug("No valid audit id found for the order: " + orderModel.getCode());
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error while processing the cleanup for the order: " + orderModel.getCode());
			LOG.error(e.getMessage(), e);
		}
	}

	private boolean checkCOD(final OrderModel orderModel)
	{
		boolean isCOD = false;

		if (null != orderModel.getPaymentInfo() && orderModel.getPaymentInfo() instanceof CODPaymentInfoModel)
		{
			isCOD = true;
		}
		return isCOD;
	}

	private GetOrderStatusResponse getOrderStatusFromJuspay(final String orderId)
	{
		GetOrderStatusResponse orderStatusResponse = null;
		try
		{
			final PaymentService juspayService = new PaymentService();

			juspayService.setBaseUrl(configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.JUSPAYBASEURL));
			juspayService.withKey(
					configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
					.withMerchantId(
							configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

			final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();
			orderStatusRequest.withOrderId(orderId);

			orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);
		}
		catch (final Exception e)
		{
			LOG.error("Exception while fetching order status for juspay order id : " + orderId);
			LOG.error(e.getMessage(), e);
		}

		return orderStatusResponse;
	}

	private void initiateOrderRefund(final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditModel paymentAudit)
	{
		try
		{
			if (null != orderStatusResponse.getPaymentMethodType()
					&& orderStatusResponse.getPaymentMethodType().equalsIgnoreCase(
							MarketplacecommerceservicesConstants.PAYMENT_METHOD_NB))
			{
				mplJusPayRefundService.doRefund(paymentAudit.getAuditId(), orderStatusResponse.getPaymentMethodType());
			}
			else if (StringUtils.isNotEmpty(orderStatusResponse.getBankEmi())
					&& StringUtils.isNotEmpty(orderStatusResponse.getBankTenure()))
			{
				mplJusPayRefundService.doRefund(paymentAudit.getAuditId(), MarketplacecommerceservicesConstants.EMI);
			}
			else
			{
				mplJusPayRefundService.doRefund(paymentAudit.getAuditId(), orderStatusResponse.getCardResponse().getCardType());
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

}
