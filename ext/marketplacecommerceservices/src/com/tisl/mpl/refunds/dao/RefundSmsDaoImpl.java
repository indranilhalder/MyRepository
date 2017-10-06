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

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.RefundTransactionEntryModel;
import com.tisl.mpl.sms.SendSmsService;
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
		final List<BulkSmsPerBatch> bulkSmsPerBatchList = new ArrayList<BulkSmsPerBatch>();
		try
		{
			query.append(MarketplacecommerceservicesConstants.BULK_SMS_1.toString());
			query.append(dynamicQuery);
			query.append(MarketplacecommerceservicesConstants.BULK_SMS_2.toString());

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.setResultClassList(Arrays.asList(String.class, String.class, String.class, String.class, String.class,
					String.class));
			StringBuilder msg = new StringBuilder();
			final SearchResult<List<Object>> rows = search(fQuery);
			BulkSmsPerBatch bulkSmsPerBatch = null;

			for (final List<Object> row : rows.getResult())
			{
				msg = new StringBuilder();
				bulkSmsPerBatch = new BulkSmsPerBatch();
				msg.append("Hey ");
				msg.append((String) (row.get(4)));
				msg.append(", We have successfully refunded Rs ");
				msg.append((String) row.get(0));
				msg.append(" to your bank account against Tata CLiQ order no ");
				msg.append((String) row.get(3));
				msg.append(" For delay over 5 days please contact your bank with ref number ");
				if (null != row.get(1))
				{
					msg.append(row.get(1));
				}
				else
				{
					msg.append(row.get(2));
				}
				msg.append(".For few banks, It may take up to 10-15 days to reflect in your account.");
				bulkSmsPerBatch.setMobileNo((String) row.get(5));
				bulkSmsPerBatch.setMsg(msg.toString());
				bulkSmsPerBatch.setTxnId((String) row.get(3));
				bulkSmsPerBatchList.add(bulkSmsPerBatch);
			}

		}
		catch (final Exception ex)
		{
			LOG.error(ex.getCause());
		}
		return bulkSmsPerBatchList;
	}

	@Override
	public String getAllTransactionsForSms() throws Exception
	{
		String dynamicQuery = null;
		try
		{
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

		}
		catch (final Exception ex)
		{
			LOG.error(ex.getCause());
		}
		return dynamicQuery;
	}

	@Override
	public void deleteRows(final String str) throws Exception
	{
		final StringBuilder query = new StringBuilder();
		try
		{
			query.append("select {pk} from {RefundTransactionEntry} where {transactionid} in ( ");
			query.append(str);
			query.append(" )");

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
			final List<RefundTransactionEntryModel> refundSmsPkList = flexibleSearchService.<RefundTransactionEntryModel> search(
					fQuery).getResult();
			modelService.remove(refundSmsPkList);
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getCause());
		}
	}


}
