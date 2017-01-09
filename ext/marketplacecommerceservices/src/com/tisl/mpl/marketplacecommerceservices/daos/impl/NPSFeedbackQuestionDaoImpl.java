/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.NPSFeedbackQuestionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.marketplacecommerceservices.daos.NPSFeedbackQuestionDao;
import com.tisl.mpl.promotion.dao.impl.MplBinErrorDaoImpl;


/**
 * @author TCS
 *
 */
@Component(value = "MplNPSFeedbackDao")
public class NPSFeedbackQuestionDaoImpl implements NPSFeedbackQuestionDao
{

	private static final Logger LOG = Logger.getLogger(MplBinErrorDaoImpl.class);

	//private static final String Err = "{err."; // Blocked for SONAR FIX
	@SuppressWarnings("unused")
	private static final String QUERY_FROM = "FROM {";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<NPSFeedbackQuestionModel> getFeedbackQuestionDao()
	{
		LOG.debug("Fetching Feedback Question Details");
		final String queryString = "SELECT {err: " + NPSFeedbackQuestionModel.PK + " } " + " FROM { "
				+ NPSFeedbackQuestionModel._TYPECODE + " AS err} " + "where " + " { err. " + NPSFeedbackQuestionModel.ENABLE
				+ " }  = 1 ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<NPSFeedbackQuestionModel> search(query).getResult();
	}
}