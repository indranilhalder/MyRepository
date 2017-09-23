/**
 *
 */
package com.tisl.mpl.refunds.dao;

import java.util.List;

import com.tisl.mpl.data.RefundSmsData;


/**
 * @author TCS
 *
 */
public interface RefundSmsDao
{
	public List<RefundSmsData> searchResultsForRefund(final String dynamicQuery) throws Exception;

	public String getAllTransactionsForSms() throws Exception;

	public Object triggerBulkSms(final List<RefundSmsData> refundEligibleList) throws Exception;
}
