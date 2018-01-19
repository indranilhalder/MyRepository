/**
 *
 */
package com.tisl.mpl.bulk.dao;

import de.hybris.platform.core.model.BulkCancellationProcessModel;
import de.hybris.platform.cronjob.model.CronJobModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface BulkCancellationDao
{

	List<CronJobModel> fetchJobDetails(String code);

	List<BulkCancellationProcessModel> getOrderCancelData();

}