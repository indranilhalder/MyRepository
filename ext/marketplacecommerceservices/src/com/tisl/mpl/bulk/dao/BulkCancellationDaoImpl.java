/**
 *
 */
package com.tisl.mpl.bulk.dao;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
@Component(value = "BulkCancellationDao")
public class BulkCancellationDaoImpl implements BulkCancellationDao
{
	private static final Logger LOG = Logger.getLogger(BulkCancellationDaoImpl.class);
	private static final String QUERY_FROM = "FROM {";
	private static final String CODE = "code";


	@Autowired
	private FlexibleSearchService flexibleSearchService;


	//OrderIssues:-
	@Override
	public List<CronJobModel> fetchJobDetails(final String code)
	{
		//LOG.debug(MarketplacecommerceservicesConstants.PROMOTIONDEBUGLOG);
		try
		{
			final String queryString = //
			"SELECT {" + CronJobModel.PK + "} "//
					+ QUERY_FROM + CronJobModel._TYPECODE + " } where {" + CronJobModel.CODE + "} = ?code";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(CODE, code);
			return flexibleSearchService.<CronJobModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.FETCHCRONJOBDEBUGLOG, e);
			return null;
		}
	}

}