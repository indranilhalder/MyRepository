/**
 *
 */
package com.tisl.mpl.returnwindowincrease.dao;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class ReturnWindowIncreaseDaoImpl implements ReturnWindowIncreaseDao
{


	private static final Logger LOG = Logger.getLogger(ReturnWindowIncreaseDaoImpl.class);



	@Autowired
	private FlexibleSearchService flexibleSearchService;


	//OrderIssues:-

	@Override
	@SuppressWarnings("finally")
	public List<ConsignmentModel> getConsignment(final List<String> list)
	{

		final List<ConsignmentModel> c = new ArrayList<ConsignmentModel>();

		//LOG.debug(MarketplacecommerceservicesConstants.PROMOTIONDEBUGLOG);
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.FetchConsignmentList;

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("code", list);
			c.addAll(flexibleSearchService.<ConsignmentModel> search(query).getResult());
		}
		catch (final FlexibleSearchException e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ConsignmentListFailure, e);

		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.ConsignmentListFailure, e);
		}

		return c;



	}
}