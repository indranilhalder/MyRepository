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
 * Created by TCS on 6/11/2016.
 */
public class MplDefaultImportService extends DefaultImportService
{

	private static final Logger LOG = Logger.getLogger(MplDefaultImportService.class);
	private static final String NODEID_STRING = "nodeid";
	private static final String NODEGROUP_STRING = "nodegroup";
	private static final String ADMINNODES_STRING = "adminnodes";

	private ConfigurationService configurationService;

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public ImportResult importData(final ImportConfig config)
	{
		final ImpExImportCronJobModel cronJob = (ImpExImportCronJobModel) this.getModelService().create("ImpExImportCronJob");

		try
		{
			this.getModelService().initDefaults(cronJob);
		}
		catch (final ModelInitializationException var4)
		{
			throw new SystemException(var4);
		}

		if (NODEID_STRING.equalsIgnoreCase(
				getConfigurationService().getConfiguration().getString("mpl.impex.import.node.executiontype", NODEID_STRING)))
		{
			cronJob.setNodeID(getConfigurationService().getConfiguration().getInt("mpl.impex.import.node.id", 0));
		}
		if (NODEGROUP_STRING.equalsIgnoreCase(
				getConfigurationService().getConfiguration().getString("mpl.impex.import.node.executiontype", NODEID_STRING)))
		{
			cronJob.setNodeGroup(
					getConfigurationService().getConfiguration().getString("mpl.impex.import.node.group", ADMINNODES_STRING));
		}

		this.configureCronJob(cronJob, config);
		this.getModelService().saveAll(new Object[]
		{ cronJob.getJob(), cronJob });
		this.importData(cronJob, config.isSynchronous(), config.isRemoveOnSuccess());

		if (getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false))
		{
			LOG.error("-cronjob-stats- Code : " + cronJob.getCode() + ", NodeID : " + cronJob.getNodeID() + ", StartTime : "
					+ cronJob.getStartTime() + ", EndTime : " + cronJob.getEndTime() + ", Number of lines processed successfully : "
					+ cronJob.getValueCount() + ", Last successfully processed line : " + cronJob.getLastSuccessfulLine()
					+ ", Status : " + cronJob.getStatus().name() + ", Result : " + cronJob.getResult().name());
		}

		return ((Item) this.getModelService().getSource(cronJob)).isAlive() ? new ImportCronJobResult(cronJob)
				: new ImportCronJobResult((ImpExImportCronJobModel) null);
	}

}
