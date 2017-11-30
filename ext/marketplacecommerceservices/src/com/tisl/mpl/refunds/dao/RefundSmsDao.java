/**
 *
 */
package com.tisl.mpl.refunds.dao;

import java.util.List;

import com.tisl.mpl.wsdto.BulkSmsPerBatch;


/**
 * @author TCS
 *
 */
public interface RefundSmsDao
{
	public List<BulkSmsPerBatch> searchResultsForRefund(final String dynamicQuery) throws Exception;

	public String getAllTransactionsForSms(String query) throws Exception;

	public void deleteRows(final String str) throws Exception;

	public int eligibleSmsCount() throws Exception;
}
