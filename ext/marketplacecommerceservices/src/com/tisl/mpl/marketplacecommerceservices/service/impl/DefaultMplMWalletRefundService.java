/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.juspay.Refund;
import com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService;
import com.tisl.mpl.model.PaymentTypeModel;
import com.tisl.mpl.wallet.refund.MRupeeRefundResponse;
import com.tisl.mpl.wallet.request.MRupeeRefundRequest;
import com.tisl.mpl.wallet.service.MRupeeRefundService;



/**
 * @author TCS
 *
 */
public class DefaultMplMWalletRefundService implements MplMWalletRefundService
{


	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DefaultMplJusPayRefundService mplMrueeRefundService;

	private static final Logger LOG = Logger.getLogger(DefaultMplMWalletRefundService.class);

	private final List<PaymentTransactionType> validPaymentType = Arrays.asList(PaymentTransactionType.CAPTURE,
			PaymentTransactionType.COD_PAYMENT, PaymentTransactionType.AUTHORIZATION);

	/**
	 * Method is called for doing refund at the time of cancel and return
	 */

	@Override
	public PaymentTransactionModel doRefund(final OrderModel order, final double refundAmount,
			final PaymentTransactionType paymentTransactionType, final String uniqueRequestId) throws Exception
	{

		try
		{
			String auditid = null;
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
				MplPaymentAuditEntryModel mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);

				mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
				mplPaymentAuditEntryModel.setStatus(MplPaymentAuditStatusEnum.REFUND_INITIATED);

				//pick up base url from local properties
				final MRupeeRefundService mRupeeRefundService = new MRupeeRefundService();
				//mRupeeRefundService.setBaseUrl("https://14.140.248.13/Mwallet/startpaymentgatewayS2S.do");
				mRupeeRefundService.setBaseUrl(
						configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.MRUPEERETURNURL));
				final MRupeeRefundRequest refundRequest = new MRupeeRefundRequest();
				double adjustedRefundAmt = 0.0;
				if (order.getType().equalsIgnoreCase("PARENT"))
				{
					adjustedRefundAmt = refundAmount;
					refundRequest.setAmount(Double.valueOf(adjustedRefundAmt));
				}
				else
				{
					adjustedRefundAmt = validateRefundAmount(refundAmount, order);
					refundRequest.setAmount(Double.valueOf(adjustedRefundAmt));
				}
				refundRequest.setPurchaseRefNo(mplPaymentAuditModel.getAuditId());
				refundRequest.setRefNo(uniqueRequestId);
				refundRequest.setmCode("TUL");
				refundRequest.setNarration("uat");

				mplPaymentAuditEntryModel.setRefundReqId(uniqueRequestId);
				modelService.save(mplPaymentAuditEntryModel);
				final List<MplPaymentAuditEntryModel> auditEnteries = new ArrayList<MplPaymentAuditEntryModel>(
						mplPaymentAuditModel.getAuditEntries());
				auditEnteries.add(mplPaymentAuditEntryModel);
				mplPaymentAuditModel.setAuditEntries(auditEnteries);
				modelService.save(mplPaymentAuditModel);
				LOG.debug("Refund status for unique ID genrated :" + uniqueRequestId);
				MRupeeRefundResponse refundResponse = null;
				LOG.debug("before calling refund service *******************************");
				refundResponse = mRupeeRefundService.refund(refundRequest);
				System.out.println("MRUPEE REFUND RESPONSE" + refundResponse);
				LOG.debug("after calling refund service *******************************");
				mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);
				mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
				mplPaymentAuditEntryModel.setResponseDate(new Date());
				PaymentTransactionModel paymentTransactionModel = null;

				if (refundResponse != null && refundResponse.isSuccess())
				{
					switch (refundResponse.getStatus().toUpperCase())
					{
						case MarketplacecommerceservicesConstants.SUCCESS_VAL:
							createAuditEntryForRefund(MplPaymentAuditStatusEnum.REFUND_SUCCESSFUL, mplPaymentAuditModel);
							break;
						default:
							createAuditEntryForRefund(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL, mplPaymentAuditModel);
					}
					paymentTransactionModel = mplMrueeRefundService.createPaymentTransactionModel(order,
							refundResponse.getStatus().toUpperCase(), Double.valueOf(adjustedRefundAmt), paymentTransactionType,
							refundResponse.getStatus(), uniqueRequestId);
				}
				else
				{
					final Refund refund = new Refund();
					refund.setUniqueRequestId(refundRequest.getUniqueRequestId());
					createAuditEntryForRefund(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL, mplPaymentAuditModel);
					paymentTransactionModel = mplMrueeRefundService.createPaymentTransactionModel(order, "FAILURE",
							Double.valueOf(refundAmount), paymentTransactionType, "NO REFUND FROM PG", uniqueRequestId);
				}
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
	 * This method creates audit entry except for Refund Pending status
	 *
	 * @param auditStatus
	 * @param mplPaymentAuditModel
	 */
	private void createAuditEntryForRefund(final MplPaymentAuditStatusEnum auditStatus,
			final MplPaymentAuditModel mplPaymentAuditModel)
	{
		try
		{
			final List<MplPaymentAuditEntryModel> auditEnteries = new ArrayList<MplPaymentAuditEntryModel>(
					mplPaymentAuditModel.getAuditEntries());
			final MplPaymentAuditEntryModel mplPaymentAuditEntryModel = modelService.create(MplPaymentAuditEntryModel.class);
			mplPaymentAuditEntryModel.setAuditId(mplPaymentAuditModel.getAuditId());
			mplPaymentAuditEntryModel.setResponseDate(new Date());
			mplPaymentAuditEntryModel.setStatus(auditStatus);
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


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService#getRefundUniqueRequestId()
	 */
	@Override
	public String getRefundUniqueRequestId()
	{
		final Random random = new Random(System.nanoTime());
		final String uniqueRequestId = MarketplacecommerceservicesConstants.EMPTYSTRING + random.nextInt(1000000000);
		return uniqueRequestId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService#isCODOrder(de.hybris.platform.core.model
	 * .order.AbstractOrderModel)
	 */
	@Override
	public boolean isCODOrder(final AbstractOrderModel order)
	{
		return "cod".equalsIgnoreCase(getValidPaymentModeType(order).getMode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService#getValidPaymentModeType(de.hybris.
	 * platform .core.model.order.AbstractOrderModel)
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplMWalletRefundService#validateRefundAmount(double,
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
								&& (PaymentTransactionType.CANCEL.equals(transactionEntry.getType())
										|| PaymentTransactionType.RETURN.equals(transactionEntry.getType())))
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

		LOG.info("totalAmountPaid :  : validateRefundAmount ():" + totalAmountPaied);
		LOG.info("alreadyRefundDone :  : validateRefundAmount ():" + alreadyRefundDone);
		final double amountRemaining = Double.parseDouble(df.format(totalAmountPaied - alreadyRefundDone));
		final double adjustableAmount = Math.abs(amountRemaining - refundAmount);
		LOG.info("adjustableAmount :  : validateRefundAmount ():" + adjustableAmount);
		final double threshold = Double.parseDouble(
				configurationService.getConfiguration().getString(MarketplacecommerceservicesConstants.REFUNDTHRESHOLD).trim());
		LOG.info("threshold :  : validateRefundAmount ():" + threshold);

		if (amountRemaining < refundAmount && threshold > adjustableAmount)
		{
			refundAmount = amountRemaining;
		}

		LOG.info("refundAmount :  : validateRefundAmount ():" + refundAmount);

		return refundAmount;


	}

	/*
	 * @Desc used in web and cscockpit for in case no response received from mRupee while cancellation refund
	 *
	 * @param orderRequestRecord
	 *
	 * @param paymentTransactionType
	 *
	 * @param uniqueRequestId
	 *
	 * @return void
	 */
	@Override
	public void createCancelRefundPgErrorEntry(final OrderCancelRecordEntryModel orderRequestRecord,
			final PaymentTransactionType paymentTransactionType, final String uniqueRequestId)
	{
		for (final OrderEntryModificationRecordEntryModel modificationEntry : orderRequestRecord
				.getOrderEntriesModificationEntries())
		{
			final OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
			if (orderEntry != null)
			{
				final double refundedAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue()
						+ orderEntry.getCurrDelCharge().doubleValue();


				final double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge().doubleValue()
						: NumberUtils.DOUBLE_ZERO.doubleValue();

				orderEntry.setRefundedDeliveryChargeAmt(Double.valueOf(deliveryCost));
				orderEntry.setCurrDelCharge(NumberUtils.DOUBLE_ZERO);
				modelService.save(orderEntry);

				mplMrueeRefundService.makeRefundOMSCall(orderEntry, null, Double.valueOf(refundedAmount),
						ConsignmentStatus.REFUND_IN_PROGRESS);

			}
		}

		final PaymentTransactionModel paymentTransactionModel = mplMrueeRefundService.createPaymentTransactionModel(
				orderRequestRecord.getOriginalVersion().getOrder(), MarketplacecommerceservicesConstants.FAILURE_FLAG,
				orderRequestRecord.getRefundableAmount(), paymentTransactionType, "NO Response FROM PG", uniqueRequestId);
		mplMrueeRefundService.attachPaymentTransactionModel(orderRequestRecord.getOriginalVersion().getOrder(),
				paymentTransactionModel);
	}

	/*
	 * @Desc used in web and cscockpit for handling network exception while cancellation
	 *
	 * @param orderRequestRecord
	 *
	 * @param paymentTransactionType
	 *
	 * @param uniqueRequestId
	 *
	 * @return void
	 */
	@Override
	public void createCancelRefundExceptionEntry(final OrderCancelRecordEntryModel orderRequestRecord,
			final PaymentTransactionType paymentTransactionType, final String uniqueRequestId)
	{

		for (final OrderEntryModificationRecordEntryModel modificationEntry : orderRequestRecord
				.getOrderEntriesModificationEntries())
		{
			final OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
			if (orderEntry != null)
			{
				final double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge().doubleValue()
						: NumberUtils.DOUBLE_ZERO.doubleValue();

				final double refundedAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost;

				orderEntry.setRefundedDeliveryChargeAmt(Double.valueOf(deliveryCost));
				orderEntry.setCurrDelCharge(NumberUtils.DOUBLE_ZERO);
				modelService.save(orderEntry);

				mplMrueeRefundService.makeRefundOMSCall(orderEntry, null, Double.valueOf(refundedAmount),
						ConsignmentStatus.REFUND_INITIATED);

			}
		}

		final PaymentTransactionModel paymentTransactionModel = mplMrueeRefundService.createPaymentTransactionModel(
				orderRequestRecord.getOriginalVersion().getOrder(), MarketplacecommerceservicesConstants.FAILURE_FLAG,
				orderRequestRecord.getRefundableAmount(), paymentTransactionType, "An Exception Occured.", uniqueRequestId);
		mplMrueeRefundService.attachPaymentTransactionModel(orderRequestRecord.getOriginalVersion().getOrder(),
				paymentTransactionModel);

	}
}
