/**
 *
 */
package com.tisl.mpl.bulk.service;

import de.hybris.platform.cronjob.model.CronJobModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface BulkCancellationService
{


	List<CronJobModel> fetchJobDetails(String code);

}