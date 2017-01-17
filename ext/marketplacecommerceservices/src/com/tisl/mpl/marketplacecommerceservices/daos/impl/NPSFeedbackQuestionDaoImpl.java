/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

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
	public NPSFeedbackModel getFeedback(final String transactionId)
	{
		final String queryString = "SELECT {err: " + NPSFeedbackModel.PK + " } " + " FROM { " + NPSFeedbackModel._TYPECODE
				+ " AS err} " + "where " + " { err. " + NPSFeedbackModel.TRANSACTIONID + " }  = ?transactionId";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("transactionId", transactionId);
		LOG.debug("Fetching NPSFeedbackModel " + query);
		return (NPSFeedbackModel) flexibleSearchService.search(query).getResult();
	}
}