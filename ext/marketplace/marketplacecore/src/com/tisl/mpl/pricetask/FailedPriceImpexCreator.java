/**
 *
 */
package com.tisl.mpl.pricetask;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.MultiThreadedImpExImportReader;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.core.constants.MarketplaceCoreConstants;


/**
 * @author TCS
 *
 */
public class FailedPriceImpexCreator
{

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	private static final Logger LOG = Logger.getLogger(FailedPriceImpexCreator.class);

	public void createImpex(final String unResolvedLines)
	{

		String impex = MarketplaceCoreConstants.NEW_LINE;

		try
		{
			final String arr[] = unResolvedLines.split(MarketplaceCoreConstants.NEW_LINE);

			boolean firstLine = true;
			for (final String sin : arr)
			{
				if (firstLine && StringUtils.isNotBlank(sin))
				{
					final String headerPart1 = sin.substring(0, sin.indexOf(MarketplaceCoreConstants.SEMICOLON));
					final String headerPart2 = sin.substring(sin.indexOf(MarketplaceCoreConstants.PRODUCT));
					impex = impex + headerPart1 + MarketplaceCoreConstants.PRODUCTCODE_STRING + headerPart2
							+ MarketplaceCoreConstants.NEW_LINE;
					firstLine = false;
				}
				else if (StringUtils.isNotBlank(sin))
				{
					final String impexPart1 = sin.substring(sin.indexOf(MarketplaceCoreConstants.FOR) + 4,
							sin.indexOf(MarketplaceCoreConstants.SEMICOLON));

					impex = impex
							+ MarketplaceCoreConstants.SEMICOLON
							+ impexPart1
							+ MarketplaceCoreConstants.SEMICOLON
							+ configurationService.getConfiguration().getString(MarketplaceCoreConstants.PRODUCT_CODE)
							+ sin.substring(sin.indexOf(MarketplaceCoreConstants.SEMICOLON,
									sin.indexOf(MarketplaceCoreConstants.SEMICOLON) + 1)) + MarketplaceCoreConstants.NEW_LINE;

				}
			}
			readFile(impex);

		}
		catch (final Exception e)
		{
			LOG.error(e);
		}

	}

	//Write to File
	public void readFile(final String data)
	{

		final MultiThreadedImpExImportReader mImpExImportReader = new MultiThreadedImpExImportReader(data);

		mImpExImportReader.setMaxThreads(configurationService.getConfiguration().getInt(MarketplaceCoreConstants.MAX_THREADS));

		try
		{

			mImpExImportReader.readAll();
		}
		catch (final ImpExException e)
		{
			LOG.error(e);
		}


	}


}
