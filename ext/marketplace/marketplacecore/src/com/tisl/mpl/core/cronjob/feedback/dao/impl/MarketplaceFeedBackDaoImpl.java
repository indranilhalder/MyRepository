/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.cronjob.feedback.dao.impl;

import de.hybris.platform.acceleratorservices.checkout.flow.CheckoutFlowStrategy;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.cronjob.feedback.dao.MarketplaceFeedBackDao;
import com.tisl.mpl.core.model.FeedBackStoringModel;


/**
 * Base {@link CheckoutFlowStrategy} implementation, gives {@link #defaultStrategy} fallback functionality.
 */
public class MarketplaceFeedBackDaoImpl extends DefaultGenericDao<FeedBackStoringModel> implements MarketplaceFeedBackDao
{

	private static final String FIND_ALL_FEEDBACK_BY_DATE_RANGE = "SELECT {c.pk} FROM {FeedBackStoring as c} WHERE ({c.modifiedtime} >= ?startDate AND {c.modifiedtime} <= ?endDate)";

	public MarketplaceFeedBackDaoImpl()
	{
		super(FeedBackStoringModel._TYPECODE);
		// YTODO Auto-generated constructor stub
	}

	public boolean createFeedBack(final InputStream input, final OutputStream output) throws IOException
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.core.cronjob.feedback.dao.MarketplaceFeedBackDao#getFeedBackData(java.util.Date)
	 */
	@Override
	public List<FeedBackStoringModel> getFeedBackData(final Date lastRunDate)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_FEEDBACK_BY_DATE_RANGE);
		final Date currentTime = new Date();
		query.addQueryParameter("startDate", lastRunDate);
		query.addQueryParameter("endDate", currentTime);
		final List<FeedBackStoringModel> resultList = getFlexibleSearchService().<FeedBackStoringModel> search(query).getResult();
		// YTODO Auto-generated method stub
		return resultList;
	}
}
