/**
 *
 */
package com.tisl.mpl.refunds.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;

import com.tisl.mpl.data.RefundSmsData;


/**
 * @author TCS
 *
 */
public interface RefundSmsDao
{
	public List<RefundSmsData> searchResultsForRefund(FlexibleSearchQuery fx);

	public StringBuilder getAllTransactionsForSms();
}
