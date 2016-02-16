/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.daos.MplTicketDao;
import com.tisl.mpl.model.CRMTicketDetailModel;


/**
 * @author TCS
 *
 */
public class MplTicketDaoImpl implements MplTicketDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	private static final String SELECT_CLASS = "Select {";

	private static final String FROM_CLASS = "} From {";

	private final static Logger LOG = Logger.getLogger(MplTicketDaoImpl.class);

	@Override
	public List<CRMTicketDetailModel> findCRMTicketDetail(final boolean isTicketCreatedInCRM)
	{
		final StringBuilder queryString = getQuery();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.addQueryParameter("isTicketCreatedInCRM", Boolean.valueOf(isTicketCreatedInCRM));
		return flexibleSearchService.<CRMTicketDetailModel> search(query).getResult();

	}

	public StringBuilder getQuery()
	{
		final StringBuilder queryString = new StringBuilder(SELECT_CLASS).append(CRMTicketDetailModel.PK).append(FROM_CLASS)
				.append(CRMTicketDetailModel._TYPECODE).append(" as ct}")
				.append(" where {ct.isTicketCreatedInCRM} = ?isTicketCreatedInCRM ");
		LOG.info(queryString);
		return queryString;
	}

}
