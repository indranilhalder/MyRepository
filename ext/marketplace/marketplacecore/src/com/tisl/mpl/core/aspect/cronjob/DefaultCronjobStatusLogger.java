package com.tisl.mpl.core.aspect.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;


/**
 * Created by 585070 on 6/11/2016.
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
				LOG.error(MarketplaceCoreConstants.SeperaterHashHead + "\nCode : " + cronjob.getCode() + "\nJob : "
						+ cronjob.getJob().getCode() + "\nNode ID : " + cronjob.getNodeID() + "\nStart time : " + cronjob.getStartTime()
						+ "\nEnd time : " + cronjob.getEndTime() + "\nResult : " + cronjob.getResult().name() + "\nStatus : "
						+ cronjob.getStatus().name() + "\nServer IP : " + InetAddress.getLocalHost().toString()
						+ MarketplaceCoreConstants.SeperaterHash);
			}
			catch (final UnknownHostException e)
			{
				//Never mind
				LOG.error(MarketplaceCoreConstants.SeperaterHashHead + "\nCode : " + cronjob.getCode() + "\nJob : "
						+ cronjob.getJob().getCode() + "\nNode ID : " + cronjob.getNodeID() + "\nStart time : " + cronjob.getStartTime()
						+ "\nEnd time : " + cronjob.getEndTime() + "\nResult : " + cronjob.getResult().name() + "\nStatus : "
						+ cronjob.getStatus().name() + MarketplaceCoreConstants.SeperaterHash);
			}
		}

	}

	@Before("target(de.hybris.platform.servicelayer.cronjob.CronJobService) && execution(* performCronJob(..)) && args(cronjob,..)")
	public void beforePerform(final CronJobModel cronjob)
	{

		if (getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false))
		{
			LOG.error(MarketplaceCoreConstants.SeperaterHash + "\nStarting cronjob with code " + cronjob.getCode()
					+ " running the job " + cronjob.getJob().getCode() + " at cluster node " + cronjob.getNodeID()
					+ MarketplaceCoreConstants.SeperaterHash);
		}

	}
}
