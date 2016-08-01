/**
 *
 */
package com.cacheclear.jobs;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.cacheclear.model.CacheClearCronJobModel;


/**
 * <h1>Clear Cache Job Unit Test</h1>
 * <p>
 * This junit is to test the functionality of Clear Cache Job
 * </p>
 *
 * @author Krishnakumar Raju
 *
 */
@IntegrationTest
public class ClearCacheJobTest extends ServicelayerTransactionalTest
{
	@Resource
	CronJobService cronJobService;

	@Resource
	ModelService modelService;

	@Resource
	SessionService sessionService;

	CacheClearCronJobModel cacheClearCronJobModel;


	/**
	 * The cronjob to be performed is fetched from the database using cronjobService API by passing the code of the
	 * cronjob model
	 *
	 * @author Krishnakumar Raju
	 */
	@Before
	public void setUp()
	{
		cacheClearCronJobModel = (CacheClearCronJobModel) cronJobService.getCronJob("cacheClearJob");
		modelService.refresh(cacheClearCronJobModel);
	}

	/**
	 * The cronjob model obtained from the database is performed using cronjobService API. The test passes if the cronjob
	 * is successfully performed.
	 *
	 * @author Krishnakumar Raju
	 */

	@Test
	public void testClearCacheJob()
	{
		try
		{
			cronJobService.performCronJob(cacheClearCronJobModel);
			do
			{
				wait(3000);
			}
			while (cronJobService.isRunning(cacheClearCronJobModel));
			assertTrue(cronJobService.isSuccessful(cacheClearCronJobModel));

		}
		catch (final Exception e)
		{
			assertTrue(e.getMessage(), false);
			assertTrue(sessionService.getCurrentSession().getAttribute("language").toString(), false);
			assertTrue(cacheClearCronJobModel.getSessionLanguage().toString(), false);
		}

	}


}
