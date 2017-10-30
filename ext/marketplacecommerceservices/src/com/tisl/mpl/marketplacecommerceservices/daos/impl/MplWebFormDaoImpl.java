/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao;


/**
 * @author TCS
 *
 */
public class MplWebFormDaoImpl implements MplWebFormDao
{

	private static final Logger LOG = Logger.getLogger(MplWebFormDaoImpl.class);

	@Resource
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#getWebCRMParentNodes()
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMParentNodes()
	{
		final String queryString = "SELECT {form: " + MplWebCrmModel.PK + " } " + " FROM { " + MplWebCrmModel._TYPECODE
				+ " AS form} ";
		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<MplWebCrmModel> search(query).getResult();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#getWebCRMByNodes(java.lang.String)
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMByNodes(final String nodeParent)
	{
		final String queryString = "SELECT {form: " + MplWebCrmModel.PK + " } " + " FROM { " + MplWebCrmModel._TYPECODE
				+ " AS form} " + "where " + " { form. " + MplWebCrmModel.NODEPARENT + " }  = ?nodeParent ";
		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("nodeParent", nodeParent);
		return flexibleSearchService.<MplWebCrmModel> search(query).getResult();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#getWebCRMTicket(java.lang.String)
	 */
	@Override
	public MplWebCrmTicketModel getWebCRMTicket(final String commerceTicketId)
	{
		final String queryString = "SELECT {form: " + MplWebCrmTicketModel.PK + " } " + " FROM { " + MplWebCrmTicketModel._TYPECODE
				+ " AS form} " + "where " + " { form. " + MplWebCrmTicketModel.COMMERCETICKETID + " }  = ?commerceTicketId ";
		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("commerceTicketId", commerceTicketId);
		return flexibleSearchService.<MplWebCrmTicketModel> search(query).getResult().get(0);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao#checkDuplicateWebCRMTickets(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkDuplicateWebCRMTickets(final String ticketType, final String orderCode, final String subOrderCode,
			final String transactionId, final String L0code, final String L1code, final String L2code, final String L3code,
			final String L4code, final String customerId)
	{
		boolean returnResult = false;
		final String queryString = "SELECT {form: " + MplWebCrmTicketModel.PK + " } " + " FROM { " + MplWebCrmTicketModel._TYPECODE
				+ " AS form } " + "where " + " { form. " + MplWebCrmTicketModel.TICKETTYPE + " }  = ?ticketType " + " { form. "
				+ MplWebCrmTicketModel.ORDERCODE + " }  = ?orderCode " + " { form. " + MplWebCrmTicketModel.SUBORDERCODE
				+ " }  = ?subOrderCode " + " { form. " + MplWebCrmTicketModel.L0CODE + " }  = ?L0code " + " { form. "
				+ MplWebCrmTicketModel.L1CODE + " }  = ?L1code " + " { form. " + MplWebCrmTicketModel.L2CODE + " }  = ?L2code "
				+ " { form. " + MplWebCrmTicketModel.L3CODE + " }  = ?L3code " + " { form. " + MplWebCrmTicketModel.L4CODE
				+ " }  = ?L4code " + " { form. " + MplWebCrmTicketModel.CUSTOMERID + " }  = ?customerId ";


		LOG.debug("Fetching MplWebCrmModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("ticketType", ticketType);
		query.addQueryParameter("orderCode", orderCode);
		query.addQueryParameter("subOrderCode", subOrderCode);
		query.addQueryParameter("transactionId", transactionId);
		query.addQueryParameter("L0code", L0code);
		query.addQueryParameter("L1code", L1code);
		query.addQueryParameter("L2code", L2code);
		query.addQueryParameter("L3code", L3code);
		query.addQueryParameter("L4code", L4code);
		query.addQueryParameter("customerId", customerId);

		final List<MplWebCrmTicketModel> result = flexibleSearchService.<MplWebCrmTicketModel> search(query).getResult();
		if (CollectionUtils.isNotEmpty(result) && result.size() > 0)
		{
			returnResult = true;
		}

		return returnResult;
	}



}