package com.tisl.mpl.core.servicelayer.impex.impl;

import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.impl.DefaultImportService;
import de.hybris.platform.servicelayer.impex.impl.ImportCronJobResult;
import org.apache.log4j.Logger;

/**
 * Created by 585070 on 6/11/2016.
 */
public class MplDefaultImportService extends DefaultImportService {

    private static final Logger LOG = Logger.getLogger(MplDefaultImportService.class);

    private ConfigurationService configurationService;

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Override
    public ImportResult importData(ImportConfig config) {
        ImpExImportCronJobModel cronJob = (ImpExImportCronJobModel)this.getModelService().create("ImpExImportCronJob");

        try {
            this.getModelService().initDefaults(cronJob);
        } catch (ModelInitializationException var4) {
            throw new SystemException(var4);
        }
        cronJob.setNodeID(getConfigurationService().getConfiguration().getInt("mpl.impex.import.node.id",0));

        this.configureCronJob(cronJob, config);
        this.getModelService().saveAll(new Object[]{cronJob.getJob(), cronJob});
        this.importData(cronJob, config.isSynchronous(), config.isRemoveOnSuccess());

        if(getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false)) {
            LOG.error("\n################################# Cronjob Stats ################################" +
                    "\nCode : " + cronJob.getCode() + "\nNodeID : " + cronJob.getNodeID() +
                    "\nStartTime : " + cronJob.getStartTime() + "\nEndTime : " + cronJob.getEndTime() +
                    "\nNumber of lines processed successfully : " + cronJob.getValueCount() +
                    "\nLast successfully processed line : " + cronJob.getLastSuccessfulLine() +
                    "\nStatus : " + cronJob.getStatus().name() +
                    "\nResult : " + cronJob.getResult().name() + "" +
                    "\n#############################################################################");
        }

        return ((Item)this.getModelService().getSource(cronJob)).isAlive()?new ImportCronJobResult(cronJob):new ImportCronJobResult((ImpExImportCronJobModel)null);
    }

}
