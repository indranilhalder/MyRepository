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
package com.tisl.mpl.core.cronjob.feedback.jobs;

import de.hybris.platform.acceleratorservices.checkout.flow.CheckoutFlowStrategy;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.cronjob.feedback.processor.MarketplaceFeedBackProcessor;
import com.tisl.mpl.core.model.FeedBackStoringModel;
import com.tisl.mpl.core.model.FeedbackCronJobModel;


/**
 * Base {@link CheckoutFlowStrategy} implementation, gives {@link #defaultStrategy} fallback functionality.
 */
public class FeedBackSendFeedCronJob extends AbstractJobPerformable<FeedbackCronJobModel>
{
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private MarketplaceFeedBackProcessor mplFeedBackProcessor;
	@Autowired
	private CronJobService cronJobService;

	private static final Logger LOG = Logger.getLogger(FeedBackSendFeedCronJob.class);



	@Override
	public PerformResult perform(final FeedbackCronJobModel cronJobModel)
	{
		LOG.info("Starting feedBack generation");

		final String outputFilePath = getOutputFilePath();
		List<FeedBackStoringModel> items = new ArrayList<FeedBackStoringModel>();
		final FeedbackCronJobModel currentCronJob = (FeedbackCronJobModel) cronJobService.getCronJob("sendFeedBackSearchCronJob");
		final Date lastRunTime = currentCronJob.getLastExecutionTime();
		//final Date lastRunTime = cronJobService.getCronJob("sendFeedBackCronJob").getCreationtime();
		if (lastRunTime != null)
		{

			items = mplFeedBackProcessor.getFeedBackData(lastRunTime);
		}
		else
		{
			final Calendar cal = Calendar.getInstance();
			final Date currentTime = new Date();
			cal.setTime(currentTime);
			cal.add(Calendar.DATE, -1);
			final Date dateBeforeOneDay = cal.getTime();
			items = mplFeedBackProcessor.getFeedBackData(dateBeforeOneDay);



		}

		//final List<FeedBackStoringModel> items = mplFeedBackProcessor.getFeedBackData(lastRunTime);
		mplFeedBackProcessor.writeItemsToCSV(items, outputFilePath, getCSVHeaderLine());
		currentCronJob.setLastExecutionTime(currentCronJob.getStartTime());
		modelService.save(currentCronJob);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected String getOutputFilePath()
	{

		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
		final String timestamp = df.format(new Date());
		final StringBuilder output_file_path = new StringBuilder();
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.search.feedback.path", ""));
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.search.feedback.prefix", ""));
		//output_file_path.append("_"); Avoid appending characters as strings
		output_file_path.append('_');
		//output_file_path.append(timestamp.toString());
		output_file_path.append(timestamp);
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.search.feedback.extension", ""));
		return output_file_path.toString();
	}

	protected String getCSVHeaderLine()
	{
		return configurationService.getConfiguration().getString("cronjob.search.feedback.header", "");
	}

}
