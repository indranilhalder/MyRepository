/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.JuspayWebhookModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public interface JuspayWebHookDao
{
	/**
	 * To fetch all the webhook records posted by Juspay
	 *
	 * @return List<JuspayWebhookModel>
	 */
	List<JuspayWebhookModel> fetchWebHookData();

	/***
	 * To fetch audit records wrt to juspay order ids
	 *
	 * @param orderId
	 * @return MplPaymentAuditModel
	 */
	MplPaymentAuditModel fetchAuditData(String orderId);

	/**
	 * to fetch the cron details
	 *
	 * @param code
	 * @return MplConfigurationModel
	 */
	MplConfigurationModel getCronDetails(String code);

	/**
	 * To fetch webhook records based on duration
	 *
	 * @param mplConfigDate
	 * @param startTime
	 * @return List<JuspayWebhookModel>
	 */
	List<JuspayWebhookModel> fetchSpecificWebHookData(Date mplConfigDate, Date startTime);

	/**
	 * To fetch the TAT set for the webhook job
	 *
	 * @return BaseStoreModel
	 */
	BaseStoreModel getJobTAT();

	/**
	 * To fetch audit records based on ebs status
	 *
	 * @param ebsStatusReview
	 * @return List<MplPaymentAuditModel>
	 */
	List<MplPaymentAuditModel> fetchAUDITonEBS(String ebsStatusReview);

	/**
	 * To fetch order based on guid
	 *
	 * @param guid
	 * @return OrderModel
	 */
	OrderModel fetchOrderOnGUID(String guid);

	/**
	 * To fetch parent order based on guid
	 *
	 * @param cartGUID
	 * @return OrderModel
	 */
	OrderModel fetchParentOrder(String cartGUID);

	/**
	 *
	 * @param guid
	 * @return List<OrderModel>
	 */
	List<OrderModel> fetchOrder(final String guid);

	/**
	 * To fetch audit records when risk response is empty
	 *
	 * @param ebsRiskPercentage
	 * @return List<MplPaymentAuditModel>
	 */
	public List<MplPaymentAuditModel> fetchAuditForEmptyRisk(String ebsRiskPercentage);

	/**
	 * To fetch records from RefundTransactionMappingModel based on refund request id
	 *
	 * @param juspayRefundId
	 * @return List<RefundTransactionMappingModel>
	 */
	List<RefundTransactionMappingModel> fetchRefundTransactionMapping(String juspayRefundId);

}
