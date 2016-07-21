/**
 *
 */
package com.cacheclear.eventlistener;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.cacheclear.event.CacheClearEvent;
import com.cacheclear.model.CacheClearCronJobModel;
import com.cacheclear.model.CacheClearingStatusModel;


/**
 * <h1>Cache Clear Event Listener Test</h1>
 * <p>
 * This junit is to test the functionality of the CacheClearEventListener.The test is passed if the event is published
 * without any exception
 * </p>
 *
 * @author Krishnakumar Raju
 *
 */

@IntegrationTest
public class CacheClearEventListenerTest extends ServicelayerTransactionalTest
{
	@Resource
	private CronJobService cronJobService;

	@Resource
	private ModelService modelService;

	@Resource
	private EventService eventService;

	private CacheClearEvent cacheClearEvent;
	private CacheClearCronJobModel cacheClearCronJobModel;
	private CacheClearingStatusModel cacheClearingStatusModel;


	/**
	 * During setup a cacheClearCronJobModel is fetched from database using cronJobService API. A
	 * cacheClearingStatusModel is also created and the values for code and cacheClearCronJob are set. An cacheClearEvent
	 * is created with cacheClearCronJobModel, a nodeID and cacheClearingStatusModel as params.
	 */
	@Before
	public void setUp()
	{

		cacheClearCronJobModel = (CacheClearCronJobModel) cronJobService.getCronJob("cacheClearJob");

	}

	/**
	 * The test is passed is the event is published without any exception
	 */

	@Test
	public void testCacheClearEventListener()
	{
		cacheClearingStatusModel = modelService.create(CacheClearingStatusModel.class);
		cacheClearingStatusModel.setCacheClearCronJob(cacheClearCronJobModel);
		cacheClearingStatusModel.setCode("testingEventListener" + System.currentTimeMillis());
		modelService.save(cacheClearingStatusModel);
		cacheClearEvent = new CacheClearEvent(cacheClearCronJobModel, 0, cacheClearingStatusModel);
		try
		{
			eventService.publishEvent(cacheClearEvent);
			assertTrue(true);
		}
		catch (final Exception e)
		{
			assertTrue(false);
		}

	}

}
