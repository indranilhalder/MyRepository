/**
 *
 */
package com.tisl.mpl.refunds.dao;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.data.RefundSmsData;


/**
 * @author TCS
 *
 */

public class RefundSmsDaoImpl extends AbstractItemDao implements RefundSmsDao
{
	private final static Logger LOG = Logger.getLogger(RefundSmsDaoImpl.class);


	@Override
	public List<RefundSmsData> searchResultsForRefund(final FlexibleSearchQuery fx)
	{
		RefundSmsData refundSmsData = null;
		final List<RefundSmsData> refundSmsDataList = new ArrayList<RefundSmsData>();
		final String queryString = "select {a:orderlineid},{add:firstname},{add:phone1} from {orderentry as a join order as b on {a:order}={b:pk} join address as add on {b:deliveryAddress}={add:pk}} where p_orderlineid ='242424000946947' and {b:type}='SubOrder' and {b:VersionID} is null";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
		fQuery.setResultClassList(Arrays.asList(String.class, String.class, String.class));


		final SearchResult<List<Object>> rows = search(fQuery);

		for (final List<Object> row : rows.getResult())
		{
			refundSmsData = new RefundSmsData();
			refundSmsData.setTransactionId((String) (row.get(0)));
			refundSmsData.setName((String) (row.get(1)));
			refundSmsData.setPhoneNo((String) (row.get(2)));
			refundSmsDataList.add(refundSmsData);
		}
		return refundSmsDataList;
	}

	@Override
	public StringBuilder getAllTransactionsForSms()
	{

		final StringBuilder dynamicQuery = new StringBuilder();
		final String queryString = "select {transactionId} from {RefundTransactionEntry}";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
		fQuery.setResultClassList(Arrays.asList(String.class));

		final SearchResult<List<Object>> rows = search(fQuery);

		for (final List<Object> row : rows.getResult())
		{
			dynamicQuery.append("'");
			dynamicQuery.append(row.get(0));
			dynamicQuery.append("'");
			dynamicQuery.append(",");
		}
		dynamicQuery.substring(0, dynamicQuery.length() - 1);

		System.out.println("=========================================" + dynamicQuery.toString());
		return dynamicQuery;
	}
}
