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
package com.tisl.mpl.core.cronjob.feedback.processor.impl;

import de.hybris.platform.acceleratorservices.checkout.flow.CheckoutFlowStrategy;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.cronjob.feedback.dao.MarketplaceFeedBackDao;
import com.tisl.mpl.core.cronjob.feedback.processor.MarketplaceFeedBackProcessor;
import com.tisl.mpl.core.jobs.converter.FeedBackStoringModelToStringListConverter;
import com.tisl.mpl.core.model.FeedBackStoringModel;
import com.tisl.mpl.core.util.CSVFileWriterHelper;


/**
 * Base {@link CheckoutFlowStrategy} implementation, gives {@link #defaultStrategy} fallback functionality.
 */
public class MarketplaceFeedBackProcessorImpl implements MarketplaceFeedBackProcessor
{
	@Autowired
	private MarketplaceFeedBackDao marketplaceFeedBackDao;
	@Autowired
	private FeedBackStoringModelToStringListConverter feedBackStoringModelToStringListConverter;

	private static final Logger LOG = Logger.getLogger(MarketplaceFeedBackProcessorImpl.class);

	@Override
	public List<FeedBackStoringModel> getFeedBackData(final Date lastRunTime)
	{
		return marketplaceFeedBackDao.getFeedBackData(lastRunTime);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.core.cronjob.feedback.processor.MarketplaceFeedBackProcessor#writeItemsToCSV(java.util.List,
	 * java.lang.String)
	 */
	@Override
	public void writeItemsToCSV(final List<FeedBackStoringModel> items, final String outputFilePath, final String csvHeader)
	{
		// YTODO Auto-generated method stub
		try
		{
			final CSVFileWriterHelper writerHelper = new CSVFileWriterHelper(outputFilePath);
			final Map<Integer, String> header = getHeaderMap(csvHeader);
			if (!header.isEmpty())
			{
				writerHelper.writeMapLine(header);
			}

			if (CollectionUtils.isNotEmpty(items) && items != null)
			{
				writerHelper.writeList(feedBackStoringModelToStringListConverter.convert(items));
			}

			writerHelper.closeCSVFile();
		}
		catch (final IOException e)
		{
			LOG.info("IOException for generating feedBack report");
		}
	}

	private Map<Integer, String> getHeaderMap(final String csvHeader)
	{
		final String[] headerColumns = csvHeader.split(",");
		final Map<Integer, String> headerLine = new HashMap<Integer, String>();
		int count = 0;
		for (final String column : headerColumns)
		{
			headerLine.put(Integer.valueOf(count), column);
			count++;
		}
		return headerLine;
	}
}
