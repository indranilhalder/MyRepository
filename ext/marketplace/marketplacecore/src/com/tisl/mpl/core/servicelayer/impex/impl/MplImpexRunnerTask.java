package com.tisl.mpl.core.servicelayer.impex.impl;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;


/**
 * Created by TCS on 6/11/2016.
 */
public abstract class MplImpexRunnerTask extends AbstractImpexRunnerTask
{

	private SessionService sessionService;
	private ImportService importService;
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
	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Override
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Override
	public ImportService getImportService()
	{
		return importService;
	}

	@Override
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

	private static final Logger LOG = Logger.getLogger(MplImpexRunnerTask.class);

	@Override
	public BatchHeader execute(final BatchHeader header) throws FileNotFoundException
	{
		Assert.notNull(header);
		Assert.notNull(header.getEncoding());
		if (CollectionUtils.isNotEmpty(header.getTransformedFiles()))
		{
			final Session localSession = getSessionService().createNewSession();
			try
			{
				for (final File file : header.getTransformedFiles())
				{
					if (getConfigurationService().getConfiguration().getBoolean("mpl.log.cronjob.enabled", false))
					{
						LOG.error("-impex-import-file-Started to process the file with name " + file.getName() + " from the path "
								+ file.getAbsolutePath());
					}
					processFile(file, header.getEncoding());
				}
			}
			finally
			{
				getSessionService().closeSession(localSession);
			}
		}
		return header;
	}

	@Override
	public abstract ImportConfig getImportConfig();
}
