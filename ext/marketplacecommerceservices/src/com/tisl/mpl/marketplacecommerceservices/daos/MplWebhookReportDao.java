/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;


/**
 * @author TCS
 *
 */
public interface MplWebhookReportDao
{
	/**
	 * @Description : Returns data within a date range
	 * @param startDate
	 * @param endDate
	 * @return List<MplPaymentAuditEntryModel>
	 */
	public List<MplPaymentAuditModel> getSpecificAuditEntries(Date startDate, Date endDate);

	/**
	 * @Description : Returns refund type for a refund request
	 * @param refundId
	 * @return RefundTransactionMappingModel
	 */
	public RefundTransactionMappingModel getRefundType(String refundId);
}
