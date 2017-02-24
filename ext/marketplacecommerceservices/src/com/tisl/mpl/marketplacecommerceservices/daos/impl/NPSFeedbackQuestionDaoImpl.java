/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.NPSFeedbackModel;
import com.tisl.mpl.core.model.NPSFeedbackQuestionModel;
import com.tisl.mpl.marketplacecommerceservices.daos.NPSFeedbackQuestionDao;


/**
 * @author TCS
 *
 */
//@Component(value = "npsFeedbackQuestionDao")
public class NPSFeedbackQuestionDaoImpl implements NPSFeedbackQuestionDao
{

	private static final Logger LOG = Logger.getLogger(NPSFeedbackQuestionDaoImpl.class);

	//private static final String Err = "{err."; // Blocked for SONAR FIX
	@SuppressWarnings("unused")
	private static final String QUERY_FROM = "FROM {";

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<NPSFeedbackQuestionModel> getFeedbackQuestionDao()
	{
		final String queryString = "SELECT {err: " + NPSFeedbackQuestionModel.PK + " } " + " FROM { "
				+ NPSFeedbackQuestionModel._TYPECODE + " AS err} " + "where " + " { err. " + NPSFeedbackQuestionModel.ENABLE
				+ " }  = 1 ";
		LOG.debug("Fetching NPSFeedbackQuestionModel " + queryString);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<NPSFeedbackQuestionModel> search(query).getResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.NPSFeedbackQuestionDao#getFeedback(java.lang.String)
	 */
	@Override
	public int getFeedback(final String transactionId)
	{
		final String queryString = "SELECT COUNT(*) FROM { " + NPSFeedbackModel._TYPECODE + "} " + "WHERE " + " { "
				+ NPSFeedbackModel.TRANSACTIONID + " }  = ?transactionId";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("transactionId", transactionId);
		final List<Class> resultClassList = new ArrayList<Class>();
		resultClassList.add(Integer.class);
		query.setResultClassList(resultClassList);
		LOG.debug("Fetching getFeedback " + query);
		final Integer count = (Integer) flexibleSearchService.search(query).getResult().iterator().next();
		return count.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.NPSFeedbackQuestionDao#validateCustomerForTransaction(java.lang.
	 * String)
	 */
	@Override
	public CustomerModel validateCustomerForTransaction(final String transactionId)
	{
		final String queryString = "SELECT {o.user} FROM  {" + AbstractOrderEntryModel._TYPECODE + " AS aoe JOIN "
				+ OrderModel._TYPECODE + " AS o ON {aoe.order} = {o.pk}} WHERE {aoe.transactionid} = ?transactionId";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("transactionId", transactionId);
		LOG.debug("Fetching validateCustomerForTransaction " + query);

		return flexibleSearchService.<CustomerModel> search(query).getResult().get(0);
	}


}