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

import com.tisl.mpl.core.model.RefundTransactionEntryModel;
import com.tisl.mpl.data.RefundSmsData;
import com.tisl.mpl.sms.SendSmsService;
import com.tisl.mpl.wsdto.BulkSmsDTO;
import com.tisl.mpl.wsdto.BulkSmsListDTO;
import com.tisl.mpl.wsdto.BulkSmsPerBatch;


/**
 * @author TCS
 *
 */

public class RefundSmsDaoImpl extends AbstractItemDao implements RefundSmsDao
{
	private final static Logger LOG = Logger.getLogger(RefundSmsDaoImpl.class);

	@Autowired
	ConfigurationService ConfigurationService;

	@Autowired
	SendSmsService sendSmsService;

	@Override
	public List<BulkSmsPerBatch> searchResultsForRefund(final String dynamicQuery) throws Exception
	{
		final StringBuilder query = new StringBuilder();

		query.append("select {sms: amount}, {sms: utrNumber} ,{sms: arnNumber} ,{a:orderlineid},{add:firstname},{add:phone1} from {orderentry as a join order as b on {a:order}={b:pk} join address as add on {b:deliveryAddress}={add:pk} join RefundTransactionEntry as sms on {a:orderlineid}={sms:transactionId}}  where p_orderlineid in (");
		query.append(dynamicQuery);
		query.append(") and {b:type}='SubOrder' and {b:VersionID} is null");

		System.out.println("===================== :::::::::::" + query.toString());

		RefundSmsData refundSmsData = null;
		final List<RefundSmsData> refundSmsDataList = new ArrayList<RefundSmsData>();
		//final String queryString = "select {a:orderlineid},{add:firstname},{add:phone1} from {orderentry as a join order as b on {a:order}={b:pk} join address as add on {b:deliveryAddress}={add:pk}} where p_orderlineid ='242424000946947' and {b:type}='SubOrder' and {b:VersionID} is null";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.setResultClassList(Arrays.asList(String.class, String.class, String.class, String.class, String.class, String.class));


		final StringBuilder msg = new StringBuilder();

		final SearchResult<List<Object>> rows = search(fQuery);

		//final int count = rows.getCount();

		final BulkSmsDTO obj1 = new BulkSmsDTO();
		final List<BulkSmsDTO> obj2 = new ArrayList<BulkSmsDTO>();
		final BulkSmsListDTO obj3 = new BulkSmsListDTO();
		//final int batch = 5;
		BulkSmsPerBatch yyyy = null;
		final List<BulkSmsPerBatch> xxxxx = new ArrayList<BulkSmsPerBatch>();
		//final List<BulkSmsListDTO> abcd = new ArrayList<BulkSmsListDTO>();

		for (final List<Object> row : rows.getResult())
		{

			yyyy = new BulkSmsPerBatch();
			refundSmsData = new RefundSmsData();

			msg.append("Hey ");
			msg.append((String) (row.get(4)));
			msg.append("We have successfully refunded Rs ");
			msg.append((String) row.get(0));
			msg.append("to your bank account against Tata CLiQ order no ");
			msg.append((String) row.get(3));
			msg.append("For delay over 5 days please contact your bank with ref number ");
			if (null != row.get(1))
			{
				msg.append(row.get(1));
			}
			else
			{
				msg.append(row.get(2));
			}
			msg.append(".For few banks It may take up to 10-15 days to reflect in your account.");

			yyyy.setMobileNo((String) row.get(5));
			yyyy.setMsg(msg.toString());
			yyyy.setTxnId((String) row.get(3));
			yyyy.setSenderId("sdfsdgdg");

			xxxxx.add(yyyy);


			obj1.setMessage(msg.toString());
			obj1.setMobileNumber(Long.parseLong((String) row.get(5)));
			obj1.setTransactionId((String) row.get(3));
			obj2.add(obj1);



		}
		obj3.setBulkSmsDTO(obj2);
		obj3.setSenderId("abhijittest");

		return xxxxx;
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
	public void deleteRows(final StringBuilder str) throws Exception
	{
		final StringBuilder query = new StringBuilder();
		query.append("select {pk} from {RefundTransactionEntry} where {transactionid} in ( ");
		query.append(str);
		query.append(" )");

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
		final List<RefundTransactionEntryModel> refundSmsPkList = flexibleSearchService
				.<RefundTransactionEntryModel> search(fQuery).getResult();
		modelService.remove(refundSmsPkList);
	}


}
