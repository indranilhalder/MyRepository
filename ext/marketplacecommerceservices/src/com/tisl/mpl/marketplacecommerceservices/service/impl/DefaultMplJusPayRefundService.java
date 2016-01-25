/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.GlobalCodeMasterModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.Refund;
import com.tisl.mpl.juspay.request.RefundRequest;
import com.tisl.mpl.juspay.response.RefundResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.JuspayWebHookDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPaymentDao;
import com.tisl.mpl.marketplacecommerceservices.service.GlobalCodeService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.mplcommerceservices.service.data.RefundInfo;
import com.tisl.mpl.service.MplRefundStatusService;
import com.tisl.mpl.xml.pojo.RefundInfoResponse;


/**
 * @author 1035227
 *
 */
public class DefaultMplJusPayRefundService implements MplJusPayRefundService
{

	private static final Logger LOG = Logger.getLogger(DefaultMplJusPayRefundService.class);


	@Autowired
	private ConfigurationService configurationService;


	@Autowired
	private ModelService modelService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private MplRefundStatusService mplRefundStatusService;

	@Autowired
	private GlobalCodeService globalCodeService;

	@Autowired
	private MplPaymentDao mplPaymentDao;

	@Autowired
	private JuspayWebHookDao juspayWebHookDao;


	private final List<PaymentTransactionType> validPaymentType = Arrays.asList(PaymentTransactionType.CAPTURE,
			PaymentTransactionType.COD_PAYMENT, PaymentTransactionType.AUTHORIZATION);

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
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


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService#doRefund(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public PaymentTransactionModel doRefund(final OrderModel order, final double refundAmount,
			final PaymentTransactionType paymentTransactionType) throws Exception
	{
		try
		{
			String auditid = null;
			//Find the correct juspay orderid(auditid) model for sucess //AT

			for (final PaymentTransactionModel paymentTransaction : order.getPaymentTransactions())
			{
				if (auditid != null)
				{
					break;
				}
				for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
				{
					if (PaymentTransactionType.CAPTURE.equals(paymentTransactionEntry.getType())
							&& "success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus()))
					{
						auditid = paymentTransactionEntry.getRequestToken();
						break;
					}
				}


			}

			MplPaymentAuditModel mplPaymentAuditModel = new MplPaymentAuditModel();
			mplPaymentAuditModel.setAuditId(auditid);
			final List<MplPaymentAuditModel> mplPaymentAuditModels = flexibleSearchService.getModelsByExample(mplPaymentAuditModel);


			if (CollectionUtils.isNotEmpty(mplPaymentAuditModels) && auditid != null && !isCODOrder(order))
			{
				mplPaymentAuditModel = mplPaymentAuditModels.get(0);
				final MplPaymentAuditEntryModel mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);
				mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
				mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_INITIATED);

				//Changed to pick up base url from local properties
				final PaymentService paymentService = new PaymentService();

				paymentService.setBaseUrl(configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.JUSPAYBASEURL));
				paymentService.withKey(
						configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
						.withMerchantId(
								configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));


				final RefundRequest refundRequest = new RefundRequest();
				double adjustedRefundAmt = 0.0;
				if (order.getType().equalsIgnoreCase("PARENT"))
				{
					// In case of held order is rejection from EBS , total order amount needs to be refunded
					//TISSIT-1781
					adjustedRefundAmt = refundAmount;
					refundRequest.setAmount(Double.valueOf(adjustedRefundAmt));
				}
				else
				{
					//adjusting .01 amt in caese of aprtioned  rounding off  TISEE-6279
					adjustedRefundAmt = validateRefundAmount(refundAmount, order);
					refundRequest.setAmount(Double.valueOf(adjustedRefundAmt));
				}

				refundRequest.setOrderId(mplPaymentAuditModel.getAuditId());
				final Random random = new Random(System.nanoTime());
				final String uniqueId = MarketplacecommerceservicesConstants.EMPTYSTRING + random.nextInt(1000000000);
				refundRequest.setUniqueRequestId(uniqueId);
				mplPaymentAuditEntryModel.setRefundReqId(uniqueId);
				modelService.save(mplPaymentAuditEntryModel);
				final List<MplPaymentAuditEntryModel> auditEnteries = new ArrayList<MplPaymentAuditEntryModel>(
						mplPaymentAuditModel.getAuditEntries());
				auditEnteries.add(mplPaymentAuditEntryModel);
				mplPaymentAuditModel.setAuditEntries(auditEnteries);
				modelService.save(mplPaymentAuditModel);

				LOG.debug("Refund status for unique ID genrated :" + uniqueId);
				RefundResponse refundResponse = null;
				LOG.debug("before calling refund service *******************************");
				refundResponse = paymentService.refund(refundRequest);
				LOG.debug("after calling refund service *******************************");

				//TISBOX-1779
				//				mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);
				//				mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
				//				mplPaymentAuditEntryModel.setResponseDate(new Date());
				PaymentTransactionModel paymentTransactionModel = null;
				if (refundResponse != null && CollectionUtils.isNotEmpty(refundResponse.getRefunds()))
				{
					for (final Refund refund : refundResponse.getRefunds())
					{
						if (refund.getUniqueRequestId().equalsIgnoreCase(uniqueId))
						{
							LOG.debug("Refund status for ID:" + refund.getUniqueRequestId() + " is :" + refund.getStatus());
							switch (refund.getStatus().toUpperCase())
							{
								case MarketplacecommerceservicesConstants.SUCCESS_VAL:
									//mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL);
									//modelService.save(mplPaymentAuditEntryModel);
									//TISBOX-1779
									createAuditEntryForRefund(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL, mplPaymentAuditModel, refund);
									break;
								case MarketplacecommerceservicesConstants.PENDING_VAL:
									//Doing nothing as audit entry with status "Refund_initiated already created.
									break;
								default:
									//mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL);
									//modelService.save(mplPaymentAuditEntryModel);
									//TISBOX-1779
									createAuditEntryForRefund(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL, mplPaymentAuditModel, refund);
							}
							paymentTransactionModel = createPaymentTransactionModel(order, refund.getStatus().toUpperCase(),
									Double.valueOf(adjustedRefundAmt), paymentTransactionType, refund.getStatus(), uniqueId);
						}
					}
				}
				else
				{
					final Refund refund = new Refund();
					refund.setUniqueRequestId(refundRequest.getUniqueRequestId());
					createAuditEntryForRefund(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL, mplPaymentAuditModel, refund);
					//TISBOX-1779
					//mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL);
					//modelService.save(mplPaymentAuditEntryModel);
					paymentTransactionModel = createPaymentTransactionModel(order, "FAILURE", Double.valueOf(refundAmount),
							paymentTransactionType, "NO REFUND FROM PG", uniqueId);
				}

				//TISBOX-1779
				//auditEnteries = new ArrayList<MplPaymentAuditEntryModel>(mplPaymentAuditModel.getAuditEntries());
				//				auditEnteries.add(mplPaymentAuditEntryModel);
				//				mplPaymentAuditModel.setAuditEntries(auditEnteries);
				//				modelService.save(mplPaymentAuditModel);
				return paymentTransactionModel;

			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw e;
		}
		return null;
	}



	/**
	 * This method creates audit entry except for Refund Pending status //TISBOX-1779
	 *
	 * @param auditStatus
	 * @param mplPaymentAuditModel
	 */
	private void createAuditEntryForRefund(final MplPaymentAuditStatusEnum auditStatus,
			final MplPaymentAuditModel mplPaymentAuditModel, final Refund refund)
	{
		try
		{
			final List<MplPaymentAuditEntryModel> auditEnteries = new ArrayList<MplPaymentAuditEntryModel>(
					mplPaymentAuditModel.getAuditEntries());
			final MplPaymentAuditEntryModel mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);
			mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
			mplPaymentAuditEntryModel.setResponseDate(new Date());
			mplPaymentAuditEntryModel.setStatus(auditStatus);

			if (null != refund && null != refund.getUniqueRequestId())
			{
				//setting the unique request id of the refund request sent to Juspay in Audit entry
				mplPaymentAuditEntryModel.setRefundReqId(refund.getUniqueRequestId());
			}

			modelService.save(mplPaymentAuditEntryModel);

			auditEnteries.add(mplPaymentAuditEntryModel);
			mplPaymentAuditModel.setAuditEntries(auditEnteries);
			modelService.save(mplPaymentAuditModel);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw e;
		}

	}

	/**
	 * This method is used for manual refund when order did not get created but the transaction is SUCCESSFUL
	 *
	 * @param orderId
	 */
	@Override
	public void doRefund(final String orderId, final String paymentType)
	{
		try
		{
			LOG.debug("Inside doRefund====Fetching Audit================");
			final MplPaymentAuditModel mplPaymentAuditModel = juspayWebHookDao.fetchAuditData(orderId);

			if (null != mplPaymentAuditModel && StringUtils.isNotEmpty(mplPaymentAuditModel.getAuditId()))
			{
				MplPaymentAuditEntryModel mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);
				mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
				mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_INITIATED);

				final PaymentService paymentService = new PaymentService();

				paymentService.setBaseUrl(configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.JUSPAYBASEURL));
				paymentService.withKey(
						configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
						.withMerchantId(
								configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

				final RefundRequest refundRequest = new RefundRequest();
				refundRequest.setOrderId(mplPaymentAuditModel.getAuditId());
				final Random random = new Random(9999);
				final String uniqueId = mplPaymentAuditModel.getAuditId() + "-" + random.nextInt();
				refundRequest.setUniqueRequestId(uniqueId);
				if (null != mplPaymentAuditModel.getPaymentAmount())
				{
					refundRequest.setAmount(mplPaymentAuditModel.getPaymentAmount());
				}
				else
				{
					refundRequest.setAmount(Double.valueOf(0.0));
					LOG.debug("doRefund for Webhook======Payment Amount is null for UniqueRequestId" + uniqueId + "for Audit id-"
							+ mplPaymentAuditModel.getAuditId());
				}

				mplPaymentAuditEntryModel.setRefundReqId(uniqueId);
				modelService.save(mplPaymentAuditEntryModel);
				final List<MplPaymentAuditEntryModel> auditEnteries = new ArrayList<MplPaymentAuditEntryModel>(
						mplPaymentAuditModel.getAuditEntries());
				auditEnteries.add(mplPaymentAuditEntryModel);
				mplPaymentAuditModel.setAuditEntries(auditEnteries);
				modelService.save(mplPaymentAuditModel);

				LOG.debug("Refund status for unique ID genrated :" + uniqueId);
				RefundResponse refundResponse = null;
				LOG.debug("before calling refund service *******************************");
				final PaymentTransactionModel paymentTransactionModel = modelService.create(PaymentTransactionModel.class);
				final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService
						.create(PaymentTransactionEntryModel.class);
				refundResponse = paymentService.refund(refundRequest);
				LOG.debug("after calling refund service *******************************");

				mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);
				mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
				mplPaymentAuditEntryModel.setResponseDate(new Date());

				if (refundResponse != null && CollectionUtils.isNotEmpty(refundResponse.getRefunds()))
				{
					for (final Refund refund : refundResponse.getRefunds())
					{
						if (refund.getUniqueRequestId().equalsIgnoreCase(uniqueId))
						{
							final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
							BigDecimal bigAmount = null;
							LOG.debug("Refund status for ID:" + refund.getUniqueRequestId() + " is :" + refund.getStatus());

							switch (refund.getStatus().toUpperCase())
							{
								case MarketplacecommerceservicesConstants.SUCCESS_VAL:
									LOG.debug("Inside SUCCESS case for refund================");
									paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.SUCCESS_VAL);
									paymentTransactionModel.setCode(UUID.randomUUID().toString());
									paymentTransactionEntryModel.setCode(UUID.randomUUID().toString());
									bigAmount = new BigDecimal(refundResponse.getAmount().doubleValue(), MathContext.DECIMAL64);
									paymentTransactionEntryModel.setAmount(bigAmount);
									paymentTransactionEntryModel.setTime(new Date());
									//paymentTransactionEntryModel.setCurrency(orderModel.getPaymentTransactions().get(0).getEntries().get(0).getCurrency());
									paymentTransactionEntryModel.setTransactionStatus(MarketplacecommerceservicesConstants.SUCCESS_VAL);

									final PaymentTypeModel paymentTypeModel = getPaymentModeDetails(paymentType);

									//Setting Payment Mode in Payment Transaction Entry
									setPaymentModeInTransaction(paymentTypeModel, paymentTransactionEntryModel);

									paymentTransactionEntryModel
											.setTransactionStatusDetails(MarketplacecommerceservicesConstants.SUCCESS_VAL);
									paymentTransactionEntryModel.setType(PaymentTransactionType.REFUND_STANDALONE);
									modelService.save(paymentTransactionEntryModel);

									entries.add(paymentTransactionEntryModel);
									paymentTransactionModel.setEntries(entries);
									modelService.save(paymentTransactionModel);

									mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL);

									//setting Payment transaction against auditentry in case where we dont have the order
									mplPaymentAuditEntryModel.setPaymentTransaction(paymentTransactionModel);

									//setting the unique request id of the refund request sent to Juspay in Audit entry
									mplPaymentAuditEntryModel.setRefundReqId(refund.getUniqueRequestId());

									break;

								case MarketplacecommerceservicesConstants.PENDING_VAL:
									LOG.debug("Inside PENDING case for refund================");
									paymentTransactionModel.setStatus(MarketplacecommerceservicesConstants.PENDING_VAL);
									paymentTransactionModel.setCode(UUID.randomUUID().toString());

									paymentTransactionEntryModel.setCode(UUID.randomUUID().toString());
									bigAmount = new BigDecimal(refundResponse.getAmount().doubleValue(), MathContext.DECIMAL64);
									paymentTransactionEntryModel.setAmount(bigAmount);
									paymentTransactionEntryModel.setTime(new Date());
									//paymentTransactionEntryModel.setCurrency(orderModel.getPaymentTransactions().get(0).getEntries().get(0).getCurrency());

									//Note : Get Payment Details
									final PaymentTypeModel paymentModel = getPaymentModeDetails(paymentType);

									//Setting Payment Mode in Payment Transaction Entry
									setPaymentModeInTransaction(paymentModel, paymentTransactionEntryModel);

									paymentTransactionEntryModel.setTransactionStatus(MarketplacecommerceservicesConstants.PENDING_VAL);
									paymentTransactionEntryModel
											.setTransactionStatusDetails(MarketplacecommerceservicesConstants.PENDING_VAL);
									paymentTransactionEntryModel.setType(PaymentTransactionType.REFUND_STANDALONE);
									modelService.save(paymentTransactionEntryModel);

									entries.add(paymentTransactionEntryModel);
									paymentTransactionModel.setEntries(entries);
									modelService.save(paymentTransactionModel);

									mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_INITIATED);

									//setting Payment transaction against auditentry in case where we dont have the order
									mplPaymentAuditEntryModel.setPaymentTransaction(paymentTransactionModel);

									//setting the unique request id of the refund request sent to Juspay in Audit entry
									mplPaymentAuditEntryModel.setRefundReqId(refund.getUniqueRequestId());

									break;

								default:
									LOG.debug("Inside UNSUCCESSFUL case for refund================");
									mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL);
							}
						}
					}
				}

				modelService.save(mplPaymentAuditEntryModel);
				//auditEnteries = new ArrayList<MplPaymentAuditEntryModel>(mplPaymentAuditModel.getAuditEntries());
				auditEnteries.add(mplPaymentAuditEntryModel);
				mplPaymentAuditModel.setAuditEntries(auditEnteries);
				modelService.save(mplPaymentAuditModel);
			}
		}
		catch (final ModelSavingException exception)
		{
			LOG.error(exception.getMessage(), exception);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param paymentTypeModel
	 * @param paymentTransactionEntryModel
	 */
	private void setPaymentModeInTransaction(final PaymentTypeModel paymentTypeModel,
			final PaymentTransactionEntryModel paymentTransactionEntryModel)
	{
		if (null != paymentTypeModel)
		{
			paymentTransactionEntryModel.setPaymentMode(paymentTypeModel);
		}
	}

	/**
	 * The Method is used to fetch the Payment Mode Details available in PG
	 *
	 *
	 * @param paymentType
	 */
	private PaymentTypeModel getPaymentModeDetails(final String paymentType)
	{
		PaymentTypeModel oModel = modelService.create(PaymentTypeModel.class);
		String paymentMode = MarketplacecommerceservicesConstants.EMPTY;
		if (StringUtils.isNotEmpty(paymentType))
		{
			if (paymentType.equalsIgnoreCase("CREDIT"))
			{
				paymentMode = "Credit Card";
				oModel = mplPaymentDao.getPaymentMode(paymentMode);
			}
			else if (paymentType.equalsIgnoreCase("DEBIT"))
			{
				paymentMode = "Debit Card";
				oModel = mplPaymentDao.getPaymentMode(paymentMode);
			}
		}
		return oModel;
	}


	/**
	 * The Method is used to fetch the Payment Mode Details available in PG
	 *
	 *
	 * @param paymentType
	 */
	@Override
	public PaymentTypeModel getValidPaymentModeType(final AbstractOrderModel order)
	{


		for (final PaymentTransactionModel paymentTransaction : order.getPaymentTransactions())
		{
			for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
			{
				if ("success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus())
						|| "ACCEPTED".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus())
						&& validPaymentType.contains(paymentTransactionEntry.getType()))
				{
					return paymentTransactionEntry.getPaymentMode();
				}

			}

		}

		return null;
	}

	/**
	 * The Method is used to fetch the Payment Mode Details available in PG
	 *
	 *
	 * @param paymentType
	 */
	@Override
	public boolean isCODOrder(final AbstractOrderModel order)
	{

		return "cod".equalsIgnoreCase(getValidPaymentModeType(order).getMode());
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService#attachPaymentTransactionModel(de.hybris
	 * .platform.core.model.order.OrderModel, de.hybris.platform.payment.model.PaymentTransactionModel)
	 */
	@Override
	public boolean checkAndAttachPaymentTransactionModel(final OrderModel orderModel,
			final PaymentTransactionEntryModel paymentTransactionEntryModel, final PaymentTransactionModel paymentTransactionModel)
	{
		if (orderModel != null)
		{
			final List<PaymentTransactionModel> paymentTransactions = new ArrayList<PaymentTransactionModel>(
					orderModel.getPaymentTransactions());
			final List<PaymentTransactionEntryModel> entries = new ArrayList<PaymentTransactionEntryModel>();
			if (!paymentTransactions.isEmpty())
			{
				paymentTransactionEntryModel.setPaymentMode(getValidPaymentModeType(orderModel));

			}
			final Date date = new Date();

			paymentTransactionEntryModel.setTime(date);
			paymentTransactionEntryModel.setTransactionStatus("FAILURE");
			paymentTransactionEntryModel.setTransactionStatusDetails("FAILURE");
			entries.add(paymentTransactionEntryModel);
			paymentTransactionModel.setEntries(entries);

			paymentTransactions.add(paymentTransactionModel);
			orderModel.setPaymentTransactions(paymentTransactions);

			modelService.save(orderModel);
			return true;
		}
		return false;
	}


	@Override
	public boolean attachPaymentTransactionModel(final OrderModel orderModel, final PaymentTransactionModel paymentTransactionModel)
	{
		if (orderModel != null)
		{
			final List<PaymentTransactionModel> paymentTransactions = new ArrayList<PaymentTransactionModel>(
					orderModel.getPaymentTransactions());
			paymentTransactions.add(paymentTransactionModel);
			orderModel.setPaymentTransactions(paymentTransactions);
			modelService.saveAll(paymentTransactions);
			modelService.saveAll(orderModel);
			return true;
		}
		return false;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService#createPaymentTransactionModel(de.hybris
	 * .platform.core.model.order.OrderModel, java.lang.String, java.math.BigDecimal,
	 * de.hybris.platform.payment.enums.PaymentTransactionType)
	 */


	@Override
	public PaymentTransactionModel createPaymentTransactionModel(final OrderModel orderModel, final String status,
			final Double amount, final PaymentTransactionType paymentTransactionType, final String statusDetails, final String code)
	{
		if (orderModel != null && CollectionUtils.isNotEmpty(orderModel.getPaymentTransactions()))
		{
			final PaymentTransactionModel paymentTransactionModel = modelService.create(PaymentTransactionModel.class);
			paymentTransactionModel.setStatus(status);
			paymentTransactionModel.setCode(code);
			paymentTransactionModel.setRequestId(code);
			paymentTransactionModel.setOrder(orderModel);
			final PaymentTransactionEntryModel paymentTransactionEntryModel = modelService
					.create(PaymentTransactionEntryModel.class);
			paymentTransactionEntryModel.setCode(code);
			final BigDecimal bigAmount = new BigDecimal(amount.doubleValue(), MathContext.DECIMAL64);
			paymentTransactionEntryModel.setAmount(bigAmount);
			paymentTransactionEntryModel.setRequestId(code);
			paymentTransactionEntryModel.setTime(new Date());
			paymentTransactionEntryModel.setCurrency(orderModel.getCurrency());
			paymentTransactionEntryModel.setTransactionStatus(status);
			paymentTransactionEntryModel.setTransactionStatusDetails(statusDetails);
			paymentTransactionEntryModel.setPaymentMode(getValidPaymentModeType(orderModel));
			paymentTransactionEntryModel.setType(paymentTransactionType);
			final List<PaymentTransactionEntryModel> entries = new ArrayList<>();
			entries.add(paymentTransactionEntryModel);
			paymentTransactionModel.setEntries(entries);
			modelService.save(paymentTransactionModel);
			return paymentTransactionModel;
		}
		return null;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService#makeRefundOMSCall(de.hybris.platform.core
	 * .model.order.OrderEntryModel, java.lang.Double)
	 */
	@Override
	public boolean makeRefundOMSCall(final AbstractOrderEntryModel orderEntry,
			final PaymentTransactionModel paymentTransactionModel, final Double amount, final ConsignmentStatus newOrderLineStatus)
	{

		if (orderEntry != null)
		{
			final RefundInfo refundInfo = new RefundInfo();

			if (paymentTransactionModel != null)
			{
				refundInfo.setRefundedBankTrxID(paymentTransactionModel.getCode() != null ? paymentTransactionModel.getCode()
						: StringUtils.EMPTY);
				refundInfo.setRefundedBankTrxStatus(paymentTransactionModel.getStatus() != null ? paymentTransactionModel.getStatus()
						: StringUtils.EMPTY);
				refundInfo.setRefundedAmt(amount.floatValue());
			}
			else
			{
				refundInfo.setRefundedBankTrxID("ERROROCCURED");
				refundInfo.setRefundedBankTrxStatus("ERROROCCURED");
				//refundInfo.setRefundedAmt(NumberUtils.FLOAT_ZERO.floatValue());
				refundInfo.setRefundedAmt(amount.floatValue());

			}
			refundInfo.setRefundedBy("CsAgent");//TODO
			refundInfo.setRefundType("Back to Source");//TODO
			refundInfo.setRefundTriggeredDate(new Date());



			final List<RefundInfo> refundInfos = new ArrayList<RefundInfo>(2);
			refundInfos.add(refundInfo);
			String statusCode = null;
			if (newOrderLineStatus == null)
			{
				if (CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
				{
					final ConsignmentEntryModel entry = orderEntry.getConsignmentEntries().iterator().next();
					statusCode = entry.getConsignment() != null ? entry.getConsignment().getStatus().getCode() : StringUtils.EMPTY;
				}
				else
				{
					statusCode = orderEntry.getOrder().getStatus().getCode();
				}
			}
			else
			{
				statusCode = newOrderLineStatus.getCode();
			}
			final GlobalCodeMasterModel globalCode = globalCodeService.getGlobalMasterCode(newOrderLineStatus.getCode());
			statusCode = globalCode != null ? globalCode.getGlobalCode() : statusCode;
			final String referenceNumber = ((OrderModel) orderEntry.getOrder()).getParentReference().getCode();
			final RefundInfoResponse resp = mplRefundStatusService.refundStatusDatatoWsdto(refundInfos, referenceNumber,
					orderEntry.getTransactionID(), statusCode);
			if (resp != null && "true".equalsIgnoreCase(resp.getReceived()))
			{
				return true;
			}

		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService#makeOMSStatusUpdate(de.hybris.platform
	 * .core.model.order.AbstractOrderEntryModel)
	 */
	@Override
	public boolean makeOMSStatusUpdate(final AbstractOrderEntryModel orderEntry, final ConsignmentStatus newOrderLineStatus)
	{
		final String referenceNumber = ((OrderModel) orderEntry.getOrder()).getParentReference().getCode();
		String statusCode = null;
		//if (CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
		//{
		final GlobalCodeMasterModel globalCode = globalCodeService.getGlobalMasterCode(newOrderLineStatus.getCode());
		statusCode = globalCode != null ? globalCode.getGlobalCode() : statusCode;
		//}

		final RefundInfoResponse resp = mplRefundStatusService.refundStatusDatatoWsdto(new ArrayList<RefundInfo>(),
				referenceNumber, orderEntry.getTransactionID(), statusCode);

		if (resp != null && "true".equalsIgnoreCase(resp.getReceived()))
		{
			LOG.debug("Status Update sucessfull from OMS");
			return true;
		}

		LOG.debug("Status Update unsucessfull from OMS");
		return false;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService#validateRefundAmount(double,
	 * de.hybris.platform.core.model.order.OrderModel)
	 */
	@Override
	public double validateRefundAmount(double refundAmount, final OrderModel subOrderModel)
	{

		// YTODO Auto-generated method stub
		LOG.info("validateRefundAmount ():" + this.toString());
		final List<PaymentTransactionEntryModel> cancelTanactionsEntry = new ArrayList<PaymentTransactionEntryModel>();
		final DecimalFormat df = new DecimalFormat("#.##");
		double totalAmountPaied = 0;
		double alreadyRefundDone = 0;
		for (final OrderModel suborder : subOrderModel.getParentReference().getChildOrders())
		{
			for (final PaymentTransactionModel transaction : suborder.getPaymentTransactions())
			{
				if (MarketplacecommerceservicesConstants.SUCCESS.equalsIgnoreCase(transaction.getStatus()))
				{
					for (final PaymentTransactionEntryModel transactionEntry : transaction.getEntries())
					{
						if (MarketplacecommerceservicesConstants.SUCCESS.equalsIgnoreCase(transactionEntry.getTransactionStatus())
								&& (PaymentTransactionType.CANCEL.equals(transactionEntry.getType()) || PaymentTransactionType.RETURN
										.equals(transactionEntry.getType())))
						{
							cancelTanactionsEntry.add(transactionEntry);
						}
						if (MarketplacecommerceservicesConstants.SUCCESS.equalsIgnoreCase(transactionEntry.getTransactionStatus())
								&& PaymentTransactionType.CAPTURE.equals(transactionEntry.getType())
								&& transactionEntry.getAmount() != null)
						{
							totalAmountPaied = transactionEntry.getAmount().doubleValue();
						}
					}

				}
			}

		}
		for (final PaymentTransactionEntryModel alreadyRefunded : cancelTanactionsEntry)
		{
			alreadyRefundDone += alreadyRefunded.getAmount().doubleValue();

		}

		LOG.info("totalAmountPaied :  : validateRefundAmount ():" + totalAmountPaied);
		LOG.info("alreadyRefundDone :  : validateRefundAmount ():" + alreadyRefundDone);
		final double amountRemaining = Double.parseDouble(df.format(totalAmountPaied - alreadyRefundDone));
		final double adjustableAmount = Math.abs(amountRemaining - refundAmount);
		LOG.info("adjustableAmount :  : validateRefundAmount ():" + adjustableAmount);
		final double threshold = Double.parseDouble(configurationService.getConfiguration()
				.getString(MarketplacecommerceservicesConstants.REFUNDTHRESHOLD).trim());
		LOG.info("threshold :  : validateRefundAmount ():" + threshold);

		if (amountRemaining < refundAmount && threshold > adjustableAmount)
		{
			refundAmount = amountRemaining;
		}

		LOG.info("refundAmount :  : validateRefundAmount ():" + refundAmount);

		return refundAmount;

	}
}
