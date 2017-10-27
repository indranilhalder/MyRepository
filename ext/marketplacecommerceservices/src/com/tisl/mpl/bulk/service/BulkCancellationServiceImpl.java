/**
 *
 */
package com.tisl.mpl.bulk.service;

import de.hybris.platform.cronjob.model.CronJobModel;

import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.bulk.dao.BulkCancellationDao;


/**
 * @author TCS
 *
 */
public class BulkCancellationServiceImpl implements BulkCancellationService
{
	private static final Logger LOG = Logger.getLogger(BulkCancellationService.class);
	private BulkCancellationDao bulkCancellationDao;

	//@Resource(name = "modelService")
	//private ModelService modelService;

	/**
	 * @return the bulkCancellationDao
	 */
	public BulkCancellationDao getBulkCancellationDao()
	{
		return bulkCancellationDao;
	}

	/**
	 * @param bulkCancellationDao
	 *           the bulkCancellationDao to set
	 */
	public void setBulkCancellationDao(final BulkCancellationDao bulkCancellationDao)
	{
		this.bulkCancellationDao = bulkCancellationDao;
	}


	/**
	 * @Description : For Fetching details
	 * @param : code,oModel
	 */
	@Override
	public List<CronJobModel> fetchJobDetails(final String code)
	{
		return getBulkCancellationDao().fetchJobDetails(code);
	}


}