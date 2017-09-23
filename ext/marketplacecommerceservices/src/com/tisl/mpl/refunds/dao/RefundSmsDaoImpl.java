/**
 *
 */
package com.tisl.mpl.refunds.dao;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.data.RefundSmsData;
import com.tisl.mpl.pojo.BulkSmsDto;


/**
 * @author TCS
 *
 */

public class RefundSmsDaoImpl extends AbstractItemDao implements RefundSmsDao
{
	private final static Logger LOG = Logger.getLogger(RefundSmsDaoImpl.class);

	@Autowired
	ConfigurationService ConfigurationService;


	@Override
	public List<RefundSmsData> searchResultsForRefund(final String dynamicQuery) throws Exception
	{
		final StringBuilder query = new StringBuilder();

		query.append("select {a:orderlineid},{add:firstname},{add:phone1} from {orderentry as a join order as b on {a:order}={b:pk} join address as add on {b:deliveryAddress}={add:pk}} where p_orderlineid in (");
		query.append(dynamicQuery);
		query.append(") and {b:type}='SubOrder' and {b:VersionID} is null");

		System.out.println("===================== :::::::::::" + query.toString());

		RefundSmsData refundSmsData = null;
		final List<RefundSmsData> refundSmsDataList = new ArrayList<RefundSmsData>();
		//final String queryString = "select {a:orderlineid},{add:firstname},{add:phone1} from {orderentry as a join order as b on {a:order}={b:pk} join address as add on {b:deliveryAddress}={add:pk}} where p_orderlineid ='242424000946947' and {b:type}='SubOrder' and {b:VersionID} is null";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
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
	public String getAllTransactionsForSms() throws Exception
	{
		String dynamicQuery = null;
		final StringBuilder query = new StringBuilder();
		final String queryString = "select {transactionId} from {RefundTransactionEntry}";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
		fQuery.setResultClassList(Arrays.asList(String.class));

		final SearchResult<String> rows = search(fQuery);

		for (final String row : rows.getResult())
		{
			query.append("'");
			query.append(row);
			query.append("'");
			query.append(",");
		}
		dynamicQuery = query.substring(0, query.length() - 1);

		System.out.println("=========================================" + dynamicQuery.toString());
		return dynamicQuery;
	}

	@Override
	public Object triggerBulkSms(final List<RefundSmsData> refundEligibleList) throws Exception
	{
		//ConfigurationService.getConfiguration().getString("bulksms_batch_quantity");
		final BulkSmsDto bulkSmsObj = new BulkSmsDto();
		final List<BulkSmsDto> bulkSmsList = new ArrayList<BulkSmsDto>();
		//BulkSmsListDto bulkSmsList = new BulkSmsListDto();
		for (final RefundSmsData data : refundEligibleList)
		{
			bulkSmsObj.setMobileNumber(data.getPhoneNo());
		}



		return null;
	}


}
