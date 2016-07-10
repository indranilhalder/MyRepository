/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface MplWebhookReportService
{
	/**
	 * @Desctription : This Method returns data within a date range
	 *
	 * @param startDate
	 * @param endDate
	 */
	public List<MplPaymentAuditEntryModel> getSpecificAuditEntries(final Date startDate, final Date endDate)
			throws EtailNonBusinessExceptions;

	/**
	 * @Desctription : This Method fetches the Refund Type from RefundTransactionMapping
	 * @param refundId
	 * @return RefundTransactionMappingModel
	 */
	public RefundTransactionMappingModel fetchRefundType(final String refundId) throws EtailNonBusinessExceptions;

}
