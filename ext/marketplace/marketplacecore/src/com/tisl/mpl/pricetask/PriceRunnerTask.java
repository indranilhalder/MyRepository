/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.pricetask;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;


/**
 * Task that imports an impex file by executing impex.
 */
public abstract class PriceRunnerTask extends AbstractImpexRunnerTask
{

	private static final Logger LOG = Logger.getLogger(PriceRunnerTask.class);

	private SessionService sessionService;
	private ImportService importService;

	private FailedPriceImpexCreator failedPriceImpexCreator;

	@Autowired
	private ConfigurationService configurationService;

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

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

	/**
	 * Process an impex file using the given encoding
	 *
	 * @param file
	 * @param encoding
	 * @throws FileNotFoundException
	 */
	@Override
	protected void processFile(final File file, final String encoding) throws FileNotFoundException
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);


			final ImportConfig config = getImportConfig();


			if (config == null)
			{
				LOG.error(String.format(MarketplaceCoreConstants.IMPORTCONFIG_NOTFOUND, file == null ? null : file.getName()));
				return;
			}



			final ImpExResource resource = new StreamBasedImpExResource(fis, encoding);
			config.setScript(resource);
			final ImportResult importResult = getImportService().importData(config);
			if (importResult.isError()
					&& importResult.hasUnresolvedLines()
					&& importResult.getUnresolvedLines().getPreview().contains(MarketplaceCoreConstants.PRICE_HEADER)
					&& configurationService.getConfiguration().getString(MarketplaceCoreConstants.PRICE_FALLBACK)
							.equalsIgnoreCase(MarketplaceCoreConstants.YES))
			{
				failedPriceImpexCreator.createImpex(importResult.getUnresolvedLines().getPreview());


				LOG.error(importResult.getUnresolvedLines().getPreview());
				importResult.getUnresolvedLines();
				LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
		}
		finally
		{
			IOUtils.closeQuietly(fis);
		}
	}

	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Override
	@Required
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
	@Required
	public void setImportService(final ImportService importService)
	{
		this.importService = importService;
	}

	/**
	 * Lookup method to return the import config
	 *
	 * @return import config
	 */
	@Override
	public abstract ImportConfig getImportConfig();

	/**
	 * @return the failedPriceImpexCreator
	 */
	public FailedPriceImpexCreator getFailedPriceImpexCreator()
	{
		return failedPriceImpexCreator;
	}

	/**
	 * @param failedPriceImpexCreator
	 *           the failedPriceImpexCreator to set
	 */
	public void setFailedPriceImpexCreator(final FailedPriceImpexCreator failedPriceImpexCreator)
	{
		this.failedPriceImpexCreator = failedPriceImpexCreator;
	}
}
