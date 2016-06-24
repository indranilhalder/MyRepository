package com.tisl.mpl.core.aspect.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;


/**
 * Created by TCS on 6/11/2016.
 */

@Aspect
public class DefaultCronjobStatusLogger
{

	private static final Logger LOG = Logger.getLogger(DefaultCronjobStatusLogger.class);

	private ConfigurationService configurationService;

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@After("target(de.hybris.platform.servicelayer.cronjob.CronJobService) && execution(* performCronJob(..)) && args(cronjob,..)")
	public void afterPerform(final CronJobModel cronjob)
	{

		if (getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false))
		{
			try
			{
				LOG.error("-cronjob-stats- Processed cronjob with Code : " + cronjob.getCode() + ", Job : "
						+ cronjob.getJob().getCode() + ", Node ID : " + cronjob.getNodeID() + ", Start time : " + cronjob.getStartTime()
						+ ", End time : " + cronjob.getEndTime() + ", Result : " + cronjob.getResult().name() + ", Status : "
						+ cronjob.getStatus().name() + ", Server IP : " + InetAddress.getLocalHost().toString());
			}
			catch (final UnknownHostException e)
			{
				//Never mind just don'e log the IP
				LOG.error("-cronjob-stats- Processed cronjob with Code : " + cronjob.getCode() + ", Job : "
						+ cronjob.getJob().getCode() + ", Node ID : " + cronjob.getNodeID() + ", Start time : " + cronjob.getStartTime()
						+ ", End time : " + cronjob.getEndTime() + ", Result : " + cronjob.getResult().name() + ", Status : "
						+ cronjob.getStatus().name());
			}
		}

	}

	@Before("target(de.hybris.platform.servicelayer.cronjob.CronJobService) && execution(* performCronJob(..)) && args(cronjob,..)")
	public void beforePerform(final CronJobModel cronjob)
	{

		if (getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false))
		{
			LOG.error("-cronjob-stats- Starting cronjob with code " + cronjob.getCode() + " running the job "
					+ cronjob.getJob().getCode() + " at cluster node " + cronjob.getNodeID());
		}

	}
}
