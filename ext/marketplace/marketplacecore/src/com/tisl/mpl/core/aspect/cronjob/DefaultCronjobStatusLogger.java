package com.tisl.mpl.core.aspect.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by 585070 on 6/11/2016.
 */

@Aspect
public class DefaultCronjobStatusLogger {

    private static final Logger LOG = Logger.getLogger(DefaultCronjobStatusLogger.class);

    private ConfigurationService configurationService;

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @After("target(de.hybris.platform.servicelayer.cronjob.CronJobService) && execution(* performCronJob(..)) && args(cronjob,..)")
    public void afterPerform(final CronJobModel cronjob) {

        if(getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false))
        {
            try {
                LOG.error("\n################################### Cronjob Stats ##################################" +
                        "\nCode : " + cronjob.getCode() + "\nJob : " + cronjob.getJob().getCode() +
                        "\nNode ID : " + cronjob.getNodeID() + "\nStart time : " + cronjob.getStartTime() +
                        "\nEnd time : " + cronjob.getEndTime() + "\nResult : " + cronjob.getResult().name() +
                        "\nStatus : " + cronjob.getStatus().name() + "\nServer IP : " + InetAddress.getLocalHost().toString() +
                        "\n###################################################################################");
            } catch (UnknownHostException e) {
                //Never mind
                LOG.error("\n################################### Cronjob Stats ##################################" +
                        "\nCode : " + cronjob.getCode() + "\nJob : " + cronjob.getJob().getCode() +
                        "\nNode ID : " + cronjob.getNodeID() + "\nStart time : " + cronjob.getStartTime() +
                        "\nEnd time : " + cronjob.getEndTime() + "\nResult : " + cronjob.getResult().name() +
                        "\nStatus : " + cronjob.getStatus().name() +
                        "\n###################################################################################");
            }
        }

    }

    @Before("target(de.hybris.platform.servicelayer.cronjob.CronJobService) && execution(* performCronJob(..)) && args(cronjob,..)")
    public void beforePerform(final CronJobModel cronjob) {

        if(getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false))
        {
            LOG.error("\n###################################################################################" +
                    "\nStarting cronjob with code " + cronjob.getCode() + " running the job "
                    + cronjob.getJob().getCode() + " at cluster node " + cronjob.getNodeID() +
                    "\n###################################################################################");
        }

    }
}
