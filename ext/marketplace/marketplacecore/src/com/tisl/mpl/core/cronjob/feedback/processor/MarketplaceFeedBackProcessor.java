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
package com.tisl.mpl.core.cronjob.feedback.processor;

import de.hybris.platform.acceleratorservices.checkout.flow.CheckoutFlowStrategy;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.FeedBackStoringModel;


/**
 * Base {@link CheckoutFlowStrategy} implementation, gives {@link #defaultStrategy} fallback functionality.
 */
public interface MarketplaceFeedBackProcessor
{
	public List<FeedBackStoringModel> getFeedBackData(Date lastRunTime);

	/**
	 * @param items
	 * @param outputFilePath
	 * @param csvHeader
	 */
	void writeItemsToCSV(List<FeedBackStoringModel> items, String outputFilePath, String csvHeader);

}
